package com.tellhow.android;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.eclipse.jdt.internal.compiler.util.HashtableOfPackage;
import org.mortbay.jetty.servlet.HashSessionIdManager;

import com.baidu.aip.ocr.AipOcr;
import com.tellhow.android.entity.AccessToken;
import com.tellhow.android.entity.Article;
import com.tellhow.android.entity.BaseMessage;
import com.tellhow.android.entity.ImageMessage;
import com.tellhow.android.entity.MusicMessage;
import com.tellhow.android.entity.NewsMessage;
import com.tellhow.android.entity.TextMessage;
import com.tellhow.android.entity.VideoMessage;
import com.tellhow.android.entity.VoiceMessage;
import com.tellhow.android.util.ChartUtil;
import com.tellhow.android.util.HttpUtil;
import com.tellhow.android.util.TemplateMessageManager;
import com.thoughtworks.xstream.XStream;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class WxService {
	private static final String TOKEN = "yxl";
	
	private static final String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=wx28b769ebe44e5038&secret=808be02cad964b0e6091233dabdd49ef";
	
	private static AccessToken accessToken;
	
	public static AccessToken getAccessToken() {
		if(accessToken == null || accessToken.isExpires()) {
			initAccessToken();
		}
		System.out.println(accessToken.getAccessToken());
		return accessToken;
	}
	
	private static void initAccessToken() {
		String tokenStr = HttpUtil.get(ACCESS_TOKEN_URL);
		JSONObject jsonObject = JSONObject.fromObject(tokenStr);
		String token = jsonObject.getString("access_token");
		String expireIn = jsonObject.getString("expires_in");
		accessToken = new AccessToken(token, expireIn);
	}
	
	/**
	 * 
	 * @param token
	 * @param timestamp
	 * @param nonce
	 * @return
	 */
	public static boolean check(String timestamp, String nonce,String si) {
		//1）将token、timestamp、nonce三个参数进行字典序排序 
		String[] strs = new String[] {TOKEN,timestamp,nonce};
		Arrays.sort(strs);
		String str = strs[0] + strs[1] + strs[2];
		//2）将三个参数字符串拼接成一个字符串进行sha1加密
		String decodeStr = sha1(str);
		//3）开发者获得加密后的字符串可与signature对比，标识该请求来源于微信
		return decodeStr.equalsIgnoreCase(si);
	}
	private static String sha1(String src) {
		if (src == null || src.length() == 0) {
            return null;
        }
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            MessageDigest mdTemp = MessageDigest.getInstance("SHA1");
            mdTemp.update(src.getBytes("UTF-8"));
            byte[] md = mdTemp.digest();
            int j = md.length;
            char buf[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                buf[k++] = hexDigits[byte0 >>> 4 & 0xf];
                buf[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(buf);
        } catch (Exception e) {
            return null;
        }
	}
	
	public static void printMessage(InputStream inputStream) {
		StringBuilder buffer = new StringBuilder();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
			String line = null;
			while ((line = reader.readLine()) != null) {
				buffer.append(line).append("\r\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != reader) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		System.out.println("接收信息[" + buffer + "]");
	}
	
	public static String getRepsonse(Map<String,String> reqMap) {
		BaseMessage msg = null;
		String msgType = reqMap.get("MsgType");
		switch(msgType) {
			case "text":
				msg = dealTextMessage(reqMap);
				break;
			case "image":
				msg = dealImageMesage(reqMap);
				break;
			case "event":
				msg = dealEvent(reqMap);
			default:
				break;
		}
		if(msg != null) {
			return beanToXml(msg);
		}
		return null;
	}
	
	/**
	 * 进行图片识别
	 * @param reqMap
	 * @return
	 */
	private static BaseMessage dealImageMesage(Map<String, String> reqMap) {
		//设置APPID/AK/SK
				String APP_ID = "15356285";
			    String API_KEY = "wEbWRgGulaQsj915X5ziUyxN";
			    String SECRET_KEY = "hqteVG52dfHDboQ3sDw4jgToUyY0za6C";

			        // 初始化一个AipOcr
			        AipOcr client = new AipOcr(APP_ID, API_KEY, SECRET_KEY);

			        // 可选：设置网络连接参数
			        client.setConnectionTimeoutInMillis(2000);
			        client.setSocketTimeoutInMillis(60000);

			        // 调用接口
			        String path = reqMap.get("PicUrl");
			        org.json.JSONObject res = client.generalUrl(path, new HashMap<String, String>());
			      
			        String json = res.toString();
			        JSONObject jsonObject = JSONObject.fromObject(json);
			        JSONArray jsonArray = jsonObject.getJSONArray("words_result");
			        Iterator<JSONObject> it = jsonArray.iterator();
			        StringBuilder sb = new StringBuilder();
			        while(it.hasNext()) {
			        	JSONObject next = it.next();
			        	sb.append(next.getString("words"));
			        }
			        return new TextMessage(reqMap, sb.toString());
	}

	/**
	 * 处理事件推送
	 * @param reqMap
	 * @return
	 */
	private static BaseMessage dealEvent(Map<String, String> reqMap) {
		String event = reqMap.get("Event");
		switch(event) {
			case "CLICK":
				return dealClick(reqMap);
			case "View":
				return dealView(reqMap);
			case "LOCATION":
				 dealLocation(reqMap);
				 break;
			default:
				return null;
		}
		return null;
	}
	
	public static Map<String,Map<String,String>> locations = new HashMap<>();
	
	private static void dealLocation(Map<String, String> reqMap) {
		Map<String,String> locationMap = new HashMap<>();
		locationMap.put("Latitude", reqMap.get("Latitude"));
		locationMap.put("Longitude", reqMap.get("Longitude"));
		locations.put(reqMap.get("FromUserName"),locationMap);
		
	}

	private static BaseMessage dealView(Map<String, String> reqMap) {
		return null;
	}

	/**
	 * 处理click菜单
	 * @param reqMap
	 * @return
	 */
	private static BaseMessage dealClick(Map<String, String> reqMap) {
		String eventKey = reqMap.get("EventKey");
		switch(eventKey) {
			case "1":
				TextMessage msg = new TextMessage(reqMap,"你好呀余雯青.");
				return msg;
			case "31":
				break;
			case "32":
				TemplateMessageManager.sendSignIn(reqMap.get("FromUserName"));
				break;
			default:
				break;
		}
		return null;
	}

	private static BaseMessage dealTextMessage(Map<String, String> map) {
		String content = map.get("Content");
		if("图文".equals(content)) {
			List<Article> aritcles = new ArrayList<>();
			aritcles.add(new Article("标题","内容","http://mmbiz.qpic.cn/mmbiz_jpg/PmF1O7ZHngAnELjxIChV7qAqcFIqjoI0faBpJvCdW3YYTU4mibZSEhmB2hRNMC1TroSz3cs8vXlzhLkNMeDeDtQ/0","http://www.baidu.com"));
			NewsMessage nm = new NewsMessage(map,aritcles);
			return nm;
		}
		//调用方法返回聊天的内容
		TextMessage tm = new TextMessage(map,chat(content));
		return tm;
	}
	/**
	 * 调用机器人聊天
	 * @param string
	 * @return
	 */
	private static String chat(String msg) {
		return ChartUtil.ask(msg);
	}
	private static String beanToXml(BaseMessage msg) {
		XStream stream = new XStream();
		stream.processAnnotations(TextMessage.class);
		stream.processAnnotations(ImageMessage.class);
		stream.processAnnotations(MusicMessage.class);
		stream.processAnnotations(NewsMessage.class);
		stream.processAnnotations(TextMessage.class);
		stream.processAnnotations(VideoMessage.class);
		stream.processAnnotations(VoiceMessage.class);
		String xml = stream.toXML(msg);
		return xml;
	}
	
	public static Map<String,String> parseMessage(InputStream inputStream) {
		SAXReader reader = new SAXReader();
		Map<String,String> map = new HashMap<>();
		try {
			Document document = reader.read(inputStream);
			Element root = document.getRootElement();
			List<Element> elements = root.elements();
			for(Element e : elements) {
				map.put(e.getName(), e.getStringValue());
			}
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return map;
	}
}
