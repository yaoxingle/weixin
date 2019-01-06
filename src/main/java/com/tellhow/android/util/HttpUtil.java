package com.tellhow.android.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import org.mortbay.jetty.AbstractGenerator.Output;

public class HttpUtil {
	
	public static String post(String url,String data) {
		try {
			URL urlObj = new URL(url);
			URLConnection connection = urlObj.openConnection();
			connection.setDoOutput(true);;
			OutputStream os = connection.getOutputStream();
			os.write(data.getBytes("utf-8"));
			os.close();
			StringBuilder sb = new StringBuilder();
			InputStream in = connection.getInputStream();
			byte[] b = new byte[1024];
			int len;
			while((len = in.read(b, 0, 1024))!=-1) {
				sb.append(new String(b,0,len));
			}
			return sb.toString();
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}
	
	public static String get(String url) {
		URL urlObj;
		StringBuilder sb = new StringBuilder();
		InputStream in = null;
		URLConnection con = null;
		try {
			urlObj = new URL(url);
			con = urlObj.openConnection();
			in = con.getInputStream();
			byte[] b = new byte[1024];
			int len;
			while((len = in.read(b, 0, 1024))!=-1) {
				sb.append(new String(b,0,len,"utf-8"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(in != null) {
				try {
					in.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return sb.toString();
	}
}
