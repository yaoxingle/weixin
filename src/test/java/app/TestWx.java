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
		//�˵�����
		Button btn = new Button();
		//��һ��һ���˵�
		btn.getButton().add(new ClickButton("���","1"));
		//�ڶ���һ���˵�
		btn.getButton().add(new ViewButton("��ת","http://www.baidu.com"));
		//������һ���˵�
		SubButton sb = new SubButton("���Բ˵�");
		//Ϊ������һ���˵������Ӳ˵�
		sb.getSub_button().add(new PhotoOrAlbumButton("ͼ��ʶ������","31"));
		sb.getSub_button().add(new ClickButton("ǩ��","32"));
		sb.getSub_button().add(new ViewButton("��������","http://news.163.com"));
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
		BaseMessage tm = new TextMessage(map,"�ظ�����Ϣ");
		String xml = stream.toXML(tm);
		System.out.println(xml);
	}
	
	@Test
	public void testApi() {
		//����APPID/AK/SK
		String APP_ID = "15356285";
	    String API_KEY = "wEbWRgGulaQsj915X5ziUyxN";
	    String SECRET_KEY = "hqteVG52dfHDboQ3sDw4jgToUyY0za6C";

	        // ��ʼ��һ��AipOcr
	        AipOcr client = new AipOcr(APP_ID, API_KEY, SECRET_KEY);

	        // ��ѡ�������������Ӳ���
	        client.setConnectionTimeoutInMillis(2000);
	        client.setSocketTimeoutInMillis(60000);

	        // ���ýӿ�
	        String path = "C:\\tmp\\test.png";
	        org.json.JSONObject res = client.basicGeneral(path, new HashMap<String, String>());
	        System.out.println(res.toString(2));
	}
}
