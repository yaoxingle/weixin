package app;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.baidu.aip.ocr.AipOcr;
import com.tellhow.android.WxService;
import com.tellhow.android.entity.BaseMessage;
import com.tellhow.android.entity.Button;
import com.tellhow.android.entity.ClickButton;
import com.tellhow.android.entity.PhotoOrAlbumButton;
import com.tellhow.android.entity.SubButton;
import com.tellhow.android.entity.TextMessage;
import com.tellhow.android.entity.ViewButton;
import com.tellhow.android.util.HttpUtil;
import com.thoughtworks.xstream.XStream;

import net.sf.json.JSONObject;

public class TestWx{
	
	@Test
	public void testButton() {
		//菜单对象
		Button btn = new Button();
		//第一个一级菜单
		btn.getButton().add(new ClickButton("点击","1"));
		//第二个一级菜单
		btn.getButton().add(new ViewButton("跳转","http://www.baidu.com"));
		//第三个一级菜单
		SubButton sb = new SubButton("测试菜单");
		//为第三个一级菜单增加子菜单
		sb.getSub_button().add(new PhotoOrAlbumButton("图像识别文字","31"));
		sb.getSub_button().add(new ClickButton("签到","32"));
		sb.getSub_button().add(new ViewButton("网易新闻","http://news.163.com"));
		btn.getButton().add(sb);
		JSONObject jsonObject = JSONObject.fromObject(btn);
		System.out.println(jsonObject.toString());
		String url = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";
		url = url.replace("ACCESS_TOKEN", WxService.getAccessToken().getAccessToken());
		String result = HttpUtil.post(url, jsonObject.toString());
		System.out.println(result);
	}
	
	@Test
	public void testToke() {
		WxService.getAccessToken();
		WxService.getAccessToken();
	}
	
	@Test
	public void testMsg() {
		Map<String,String> map = new HashMap<>();
		map.put("ToUserName", "to");
		map.put("FromUserName", "from");
		map.put("MsgType", "text");
		
		XStream stream = new XStream();
		stream.processAnnotations(BaseMessage.class);
		stream.processAnnotations(TextMessage.class);
		BaseMessage tm = new TextMessage(map,"回复的信息");
		String xml = stream.toXML(tm);
		System.out.println(xml);
	}
	
	@Test
	public void testApi() {
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
	        String path = "C:\\tmp\\test.png";
	        org.json.JSONObject res = client.basicGeneral(path, new HashMap<String, String>());
	        System.out.println(res.toString(2));
	}
}
