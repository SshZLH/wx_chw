package org.wss.weixin.action;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.wanhuchina.common.util.zk.ZkPropertyUtil;
import com.whc.wx.service.IWeixinBizService;
import org.apache.commons.io.IOUtils;
import org.wss.weixin.common.encryption.AesException;
import org.wss.weixin.common.encryption.WXBizMsgCrypt;


/**
 * 核心Servlet类
 * 
 * @author LoriYue
 */
abstract public class IWeixinServlet extends HttpServlet {
	public String execute(String msg) {
		return getWeixinBizService().weixinExecute(msg);
	}
	abstract protected IWeixinBizService getWeixinBizService();

	protected IWeixinBizService weixinBizService=getWeixinBizService();

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 微信加密签名
		String msg_signature = request.getParameter("msg_signature");
		// 时间戳
		String timestamp = request.getParameter("timestamp");
		// 随机数
		String nonce = request.getParameter("nonce");
		// 随机字符串
		String echostr = request.getParameter("echostr");
		// 打印请求地址
		System.out.println("request=" + request.getRequestURL());
		System.out.println("msg_signature="+msg_signature);
		System.out.println("timestamp="+timestamp);
		System.out.println("nonce1="+nonce);
		System.out.println("echostr="+echostr);
		// 流
		PrintWriter out = response.getWriter();
		// 通过检验signature对请求进行校验，若校验成功则原样返回echostr，表示接入成功，否则接入失败
		String result = null;
		try {
			WXBizMsgCrypt wxcpt = new WXBizMsgCrypt(ZkPropertyUtil.get("token"), ZkPropertyUtil.get("encodingAESKey"), ZkPropertyUtil.get("corpId"));
			if(msg_signature !=null && timestamp != null && nonce != null && echostr !=null){
			// 验证URL函数
			result = wxcpt.verifyUrl(msg_signature, timestamp, nonce, echostr);
			}
		} catch (AesException e) {
			e.printStackTrace();
		}
		if (result == null) {
			// result为空，赋予token
			result = ZkPropertyUtil.get("token");
		}
		// 拼接请求参数
		String str = msg_signature + " " + timestamp + " " + nonce + " " + echostr;
		// 打印参数+地址+result
		System.out.println("Exception:" + result + " " + request.getRequestURL() + " " + "FourParames:" + str);
		out.print(result);
		out.close();
		out = null;
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 将请求、响应的编码均设置为UTF-8（防止中文乱码）
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");

		// 微信加密签名
		String msg_signature = request.getParameter("msg_signature");
		// 时间戳
		String timestamp = request.getParameter("timestamp");
		// 随机数
		String nonce = request.getParameter("nonce");

		// 从请求中读取整个post数据
		InputStream inputStream = request.getInputStream();
		// commons.io.jar 方法请求参数
		//打印结果
		String Post = IOUtils.toString(inputStream, "UTF-8");
		System.out.println("Post:"+Post);
		System.out.println("request=" + request.getRequestURL());
		System.out.println("msg_signature="+msg_signature);
		System.out.println("timestamp="+timestamp);
		System.out.println("nonce="+nonce);

		String Msg = "";
		WXBizMsgCrypt wxcpt = null;
		try {
			wxcpt = new WXBizMsgCrypt(ZkPropertyUtil.get("token"), ZkPropertyUtil.get("encodingAESKey"), ZkPropertyUtil.get("corpId"));
			// 解密消息
			Msg = wxcpt.decryptMsg(msg_signature, timestamp, nonce, Post);
			System.out.println("--Msg---:"+Msg);
		} catch (AesException e) {
			e.printStackTrace();
		}
		// Msg打印结果
		System.out.println("Msg打印结果：" + Msg);

		// 调用核心业务类接收消息、处理消息
		String respMessage = execute(Msg);

		// respMessage打印结果
		System.out.println("respMessage打印结果：" + respMessage);
		if (respMessage != null) {
			String encryptMsg = "";
			try {
				// 加密回复消息
				encryptMsg = wxcpt.encryptMsg(respMessage, timestamp, nonce);
				System.out.println("---encryptMsg--:"+encryptMsg);
			} catch (AesException e) {
				e.printStackTrace();
			}
			if(encryptMsg != null) {
				// 响应消息
				PrintWriter out = response.getWriter();
				out.print(encryptMsg);
				out.close();
			}
		}
		
	}
	
}
