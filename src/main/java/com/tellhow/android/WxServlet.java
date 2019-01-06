package com.tellhow.android;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Check
 */
@WebServlet(urlPatterns="/wx")
public class WxServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
    /**
     * Default constructor. 
     */
    public WxServlet() {
        // TODO Auto-generated constructor stubget
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("微信开发验证");
		/**
		 * signature	微信加密签名，signature结合了开发者填写的token参数和请求中的timestamp参数、nonce参数。
			timestamp	时间戳
			nonce	随机数
			echostr	随机字符串
		 */
		String signature = request.getParameter("signature");
		String timestamp = request.getParameter("timestamp");
		String nonce = request.getParameter("nonce");
		String echostr = request.getParameter("echostr");
		
		if(WxService.check(timestamp,nonce,signature)) {
			System.out.println("接入成功.");
			//验证成功 原文返回echostr
			response.getWriter().print(echostr);
		}else {
			System.out.println("接入失败.");
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//WxService.printMessage(request.getInputStream());
		response.setCharacterEncoding("utf-8");
		Map<String,String> map = WxService.parseMessage(request.getInputStream());
		System.out.println("接收信息:" + map);
		String respXml = WxService.getRepsonse(map);
		System.out.println("回复信息:" + respXml);
		PrintWriter out = response.getWriter();
		out.print(respXml);
		out.flush();
		out.close();
	}
}
