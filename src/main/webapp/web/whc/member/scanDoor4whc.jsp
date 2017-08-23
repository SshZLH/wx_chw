<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="com.wanhuchina.common.util.weixin.cgi.WeixinUtil"%>

<%@ page import="com.wanhuchina.common.util.zk.ZkPropertyUtil" %>
<%@ page import="com.whc.wx.web.controller.order.service.impl.MemberServiceImpl" %>
<%@ page import="com.alibaba.fastjson.JSONObject" %>
<%@ page import="com.wanhuchina.common.util.http.ApiUtils" %>

<%
String openId = (String)request.getParameter("openId");
boolean flag = false; 
String baseUrl = null;
Map<String, String> configMap = null;
MemberServiceImpl iMemberService = new MemberServiceImpl();
JSONObject jsonObject = iMemberService.getMemberDoorPermissionByOpenId(openId);
if("0".equals(jsonObject.get("result"))) {
	flag = true;
} else if("-2".equals(jsonObject.get("result"))) {
	Map<String,String> suppermanMap = new HashMap<String,String>();
	suppermanMap.put("memberId",jsonObject.getString("memberId"));
	String suppermanURL = ZkPropertyUtil.get("BaseUrlMember")+"memberManage/getSupperMan";
	JSONObject suppermanJson = ApiUtils.excutePost(suppermanURL,null,null,suppermanMap);
	if(suppermanJson.getIntValue("code")==1000) {
		if(suppermanJson.getJSONObject("data")!=null &&
				!"".equals(suppermanJson.getJSONObject("data"))){
			flag = true;
		}
	}
}

if(flag) {
	baseUrl = ZkPropertyUtil.get("baseURL");
	configMap = WeixinUtil.getJsConfigMap(request.getScheme()
			+ "://"
			+ request.getServerName()
			+ request.getRequestURI()
			+ "?"
			+ request.getQueryString(), ZkPropertyUtil.get("corpId"), ZkPropertyUtil.get("secret"));
}
%>
<!DOCTYPE html>
<html>
	<head>
	    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0" />
	     <link rel="stylesheet" href="/web/whc/member/css/framework7.css">
	    <title>扫码开门</title>
		 <%if(!flag) { %>
	</head>
	<body >
		<h2>
			<font size='25' color='green'>
				您没有权限使用此功能
			</font>
		</h2>
	</body>
	    <%} else { %>

		<script src="/web/whc/member/js/jquery-2.0.3.min.js" type="text/javascript" charset="utf-8"></script>
		<script src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js" type="text/javascript" charset="utf-8"></script>
		<script type="text/javascript">
	    wx.config({
		    debug: false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
		    appId: '<%= ZkPropertyUtil.get("corpId")%>', // 必填，企业号的唯一标识，此处填写企业号corpid
		    timestamp: <%=configMap.get("timestamp")%>, // 必填，生成签名的时间戳
		    nonceStr: '<%=configMap.get("nonceStr")%>', // 必填，生成签名的随机串
		    signature: '<%=configMap.get("signature")%>',// 必填，签名，见附录1
		    jsApiList: [
	        'scanQRCode'
		    ] // 必填，需要使用的JS接口列表，所有JS接口列表见附录2
		});
	    </script>
	</head>
	<body >
		<div id="locationDiv">
			<h2></h2>
		</div>

		<script type="text/javascript" src="/web/whc/member/js/framework7.min.js"></script>
		<script type="text/javascript">
		var myApp = new Framework7({ });
		var globalVar = {
			baseUrl : '<%=baseUrl%>',
			openId : '<%=openId%>',
		};
		wx.ready(function(){
			wx.scanQRCode({
			    needResult: 1, // 默认为0，扫描结果由微信处理，1则直接返回扫描结果，
			    scanType: ["qrCode"], // 可以指定扫二维码还是一维码，默认二者都有
			    success: function (res) {
				myApp.showPreloader('正在发送指令，请稍候...');
			    	var result = res.resultStr; // 当needResult 为 1 时，扫码返回的结果
			    	//把信息异步发送到后台检验门禁编号，返回result: 0|通过验证并向门禁服务器发送开门指令 -1|已经是打开状态
					var url2 = globalVar.baseUrl + "web/whc/member/openDoor.jsp?deviceId=" + result + "&openId=" + globalVar.openId + "&type=1";
			    	$.post(url2, function(json2) {
			    		myApp.hidePreloader();
						if($.trim(json2)) {
							var res = eval("("+ json2 +")").result;
							if(res == "0") {
								setTimeout(function(){
						            window.location.href = globalVar.baseUrl + "/app/weigate/door/home.html";
						        }, 300);
							} else if(res == "-1") {
								$("#locationDiv").html("<h2>用户不存在</h2>");
							} else if(res == "-2") {
								$("#locationDiv").html("<h2>用户没有获得任何门禁权限</h2>");
							} else if(res == "-3") {
								$("#locationDiv").html("<h2>门禁不存在</h2>");
							} else if(res == "-4") {
								$("#locationDiv").html("<h2>门禁状态不正常</h2>");
							} else if(res == "-5") {
								$("#locationDiv").html("<h2>用户没有获得此门禁的权限</h2>");
							}
						} 
					});
				},
				cancel: function(res) {
					$("#locationDiv").html("<font size='30' color='green'>已取消操作,请重新选择操作菜单</font>");
				}
			});
		});
	    </script>
	</body>
	<%} %>
</html>
	


