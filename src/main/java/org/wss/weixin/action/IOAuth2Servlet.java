package org.wss.weixin.action;

/**
 * OAuth2 servlet类
 * 
 * @author ivhhs
 * @date 2014.10.16
 */
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.wanhuchina.common.util.weixin.cgi.WeixinUtil;
import com.wanhuchina.common.util.zk.ZkPropertyUtil;
import org.apache.log4j.Logger;


abstract public class IOAuth2Servlet extends HttpServlet {
	protected String openId;
	public static Logger iOAuth2ServletWeixinUtil = Logger.getLogger("iOAuth2ServletWeixinUtil");	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		String code = request.getParameter("code");
		String url = "";
		if (code !=null && !"authdeny".equals(code)) {
			openId = WeixinUtil.getOpenIdByOAuth2Code(ZkPropertyUtil.get("corpId"), ZkPropertyUtil.get("secret"), code);
			iOAuth2ServletWeixinUtil.info("IOAuth2Servlet类中的传参code："+code+"此类中WeixinUtil.getOpenIdByOAuth2Code获取的openId："+openId);
			url = getRedirectView(request, response, openId);
		} else {
			out.print("授权获取失败，至于为什么，自己找原因。。。");
			iOAuth2ServletWeixinUtil.info("IOAuth2Servlet调用WeixinUtil的getOpenIdByOAuth2Code方法中code传过来的值:"+code+"获取到的openID为:"+openId);
		}
		if (url.equals(null)) {
			url = "";
		}
		response.sendRedirect(url);
		return;
	}
	
	abstract protected String getRedirectView(HttpServletRequest request, HttpServletResponse response, String openId);

}
