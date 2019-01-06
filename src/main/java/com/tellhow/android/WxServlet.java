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
		System.out.println("΢�ſ�����֤");
		/**
		 * signature	΢�ż���ǩ����signature����˿�������д��token�����������е�timestamp������nonce������
			timestamp	ʱ���
			nonce	�����
			echostr	����ַ���
		 */
		String signature = request.getParameter("signature");
		String timestamp = request.getParameter("timestamp");
		String nonce = request.getParameter("nonce");
		String echostr = request.getParameter("echostr");
		
		if(WxService.check(timestamp,nonce,signature)) {
			System.out.println("����ɹ�.");
			//��֤�ɹ� ԭ�ķ���echostr
			response.getWriter().print(echostr);
		}else {
			System.out.println("����ʧ��.");
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//WxService.printMessage(request.getInputStream());
		response.setCharacterEncoding("utf-8");
		Map<String,String> map = WxService.parseMessage(request.getInputStream());
		System.out.println("������Ϣ:" + map);
		String respXml = WxService.getRepsonse(map);
		System.out.println("�ظ���Ϣ:" + respXml);
		PrintWriter out = response.getWriter();
		out.print(respXml);
		out.flush();
		out.close();
	}
}
