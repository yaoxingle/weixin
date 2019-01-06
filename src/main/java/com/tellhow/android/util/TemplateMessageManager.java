/**  
 * @Package com.tellhow.android.util   
 * @Description:    TODO  
 * @author: 姚星乐     
 * @date:   2019年1月6日 下午4:23:41   
 * @version V1.0 
 * 
 */
package com.tellhow.android.util;

import java.util.Map;

import org.junit.Test;

import com.tellhow.android.WxService;

import net.sf.json.JSONObject;

public class TemplateMessageManager {
	
	/**
	 * 
	 */
	@Test
	public void set() {
		String at = WxService.getAccessToken().getAccessToken();
		String url = "https://api.weixin.qq.com/cgi-bin/template/api_set_industry?access_token=" + at; 
		String data =  "{"+
		          "\"industry_id1\":\"1\","+
		          "\"industry_id2\":\"4\""+
		       "};";
		String resp = HttpUtil.post(url, data);
		System.out.println(resp);
	}
	
	public static void sendSignIn(String touser) {
		String at = WxService.getAccessToken().getAccessToken();
		String lat = "";
		String lon = "";
		String address = "";
		Map<String,String> locationMap = WxService.locations.get(touser);
		if(locationMap != null) {
			lat = locationMap.get("Latitude");
			lon	= locationMap.get("Longitude");
			String url = "http://api.map.baidu.com/geocoder/v2/?location="+lat+","+lon+"&output=json&pois=1&ak=CSTIUfGBcb21uGRx8yvqs0XSAD09Fzpx";
			String resp = HttpUtil.get(url);
			address = JSONObject.fromObject(resp).getJSONObject("result").getString("formatted_address");
			System.out.println(resp);
		}
		String url = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=" + at;
		String data = "{\r\n" + 
				"	           \"touser\":\""+touser+"\",\r\n" + 
				"	           \"template_id\":\"VxW5nUuS5jXJQlfm4nAMvrKSprWpPy_npyLK5eXeClw\",\r\n" + 
				"	           \"data\":{\r\n" + 
				"	                   \"first\": {\r\n" + 
				"	                       \"value\":\"您好，您的签到已被确认\",\r\n" + 
				"	                       \"color\":\"#173177\"\r\n" + 
				"	                   },\r\n" + 
				"	                   \"keyword1\":{\r\n" + 
				"	                       \"value\":\"UI设计师\",\r\n" + 
				"	                       \"color\":\"#173177\"\r\n" + 
				"	                   },\r\n" + 
				"	                   \"keyword2\": {\r\n" + 
				"	                       \"value\":\""+address+"\",\r\n" + 
				"	                       \"color\":\"#173177\"\r\n" + 
				"	                   },\r\n" + 
				"	                   \"keyword3\": {\r\n" + 
				"	                       \"value\":\"2014年7月21日 08:36\",\r\n" + 
				"	                       \"color\":\"#173177\"\r\n" + 
				"	                   },\r\n" + 
				"	                   \"keyword4\": {\r\n" + 
				"	                       \"value\":\" 2014年7月21日 09:01\",\r\n" + 
				"	                       \"color\":\"#173177\"\r\n" + 
				"	                   },\r\n" + 
				"	                   \"remark\":{\r\n" + 
				"	                       \"value\":\"快去工作吧，少年。\",\r\n" + 
				"	                       \"color\":\"#173177\"\r\n" + 
				"	                   }\r\n" + 
				"	           }\r\n" + 
				"}";
		String resp = HttpUtil.post(url, data);
		System.out.println(resp);
	}
}
