<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ page import="com.wanhuchina.common.util.zk.ZkPropertyUtil" %>
<%@page import="java.text.SimpleDateFormat"%>
<%@ page import="com.google.common.base.Strings" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName();
String url = "/web/whc/warehouse/pay2.jsp";
String appId = ZkPropertyUtil.get("corpId");
url = basePath+url;
String orderId = "";
Object orderId1 = request.getAttribute("orderId");
if(null==orderId1){
	orderId = request.getParameter("orderId");
}else{
	orderId = orderId1.toString();
}
String flag="0";

if(!Strings.isNullOrEmpty(request.getParameter("flag"))){
	flag=request.getParameter("flag");
}
String parms=orderId+"-"+flag;
String weixinUrl = "https://open.weixin.qq.com/connect/oauth2/authorize?appid="+appId+"&redirect_uri="+url+"?parms="+parms+"&flag="+flag+"&response_type=code&scope=snsapi_base&state=123#wechat_redirect";

%>

<html>
  <head>
  <script type="text/javascript">
  $(document).ready(function () { 
	    show();
		location.href="<%=weixinUrl%>";
	  });
  function show(){
		$("#zzdiv").show();
		$("#showMe").show(); 
		
	  }
	 
  </script>
    
  </head>
  
  <body>
  	<div id="zzdiv" style="position:fixed;left:0;top:0;z-index:200;opacity:0.75;width:100%;height:100%;display:none;	"></div>
		<div id="showMe" style="display: none;position:absolute; left:40%; top:40%; width:20%; height:20%; z-index:1000;">
		<div id="center" style="position:absolute;">
		<table border="0">
			<tr>
				<td>
					<span id="disp">
						<img alt="" style="width:100%" src="img/loading-.png?v=<%=new SimpleDateFormat("yyyyMMdd").format(new Date()) %>">
					</span>
				</td>
			</tr>
			<tr>
				<td>
					<span id="disp2">
						<img alt="" style="width:100%" src="img/loading-.gif?v=<%=new SimpleDateFormat("yyyyMMdd").format(new Date()) %>">
					</span>
				</td>
			</tr>
		</table>
		</div>
	</div>
  </body>
</html>
