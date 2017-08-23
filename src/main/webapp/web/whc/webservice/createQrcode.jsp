<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.wanhuchina.common.util.weixin.cgi.WeixinUtil"%>
<%@page import="com.wanhuchina.common.util.weixin.cgi.AccessToken"%>
<%@page import="net.sf.json.JSONObject"%>
<%@ page import="com.wanhuchina.common.util.zk.ZkPropertyUtil" %>
<%!	//创建二维码ticket
	public final static String CGI_CREATE_QRCODE_URL = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=TOKEN";

	//通过ticket换取二维码
	public final static String CGI_SHOW_QRCODE_URL = "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=TICKET";
	/**
	 * 创建永久二维码，是无过期时间的，但数量较少（目前为最多10万个）
	 * @param appid
	 * @param secret
	 * @param sceneStr 长度限制为1到64
	 * @return url
	 */
	public  JSONObject createQrcode(String appid, String secret, String sceneStr) {
		AccessToken accessToken = WeixinUtil.getAccessToken(appid, secret);
		System.out.println("NNBBBBBBBB"+accessToken);
		String url = null;
		String requestUrl = CGI_CREATE_QRCODE_URL.replace("TOKEN", accessToken.getToken());
		String jsonStr = "{\"action_name\": \"QR_LIMIT_STR_SCENE\", \"action_info\": {\"scene\": {\"scene_str\": \"" + sceneStr + "\"}}}";
		JSONObject jsonObject = WeixinUtil.httpRequest(requestUrl, "POST", jsonStr);
		if (!jsonObject.has("ticket")) {
			String error = String.format("操作失败 errcode:{} errmsg:{}", jsonObject.getInt("errcode"), jsonObject.getString("errmsg"));
			System.out.println(error);
		} else {
			url = CGI_SHOW_QRCODE_URL.replace("TICKET", jsonObject.getString("ticket"));
		}
		return jsonObject;
	}
	
	%>
<%
String sceneStr=request.getParameter("sceneStr").toString();
JSONObject json=createQrcode(ZkPropertyUtil.get("corpId"), ZkPropertyUtil.get("secret"),sceneStr);
String res=json.get("url").toString();
out.println(res);
%>

