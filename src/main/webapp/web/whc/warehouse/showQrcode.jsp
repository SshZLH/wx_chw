<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.net.*"%>
<%@page import="java.io.*"%>
<%@page import="java.util.*"%>
<%@page import="com.alibaba.fastjson.JSONObject"%>
<%@ page import="com.wanhuchina.common.util.http.ApiUtils" %>
<%@ page import="com.wanhuchina.common.util.zk.ZkPropertyUtil" %>
<%@ page import="org.apache.commons.lang.StringUtils" %>


<%!
	public static String httpRequest(String request) {
		StringBuffer buffer = new StringBuffer();
		try {
			// 建立连接
			URL url = new URL(request);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setUseCaches(false);
			connection.setRequestMethod("GET");
			// 流处理
			InputStream input = connection.getInputStream();
			InputStreamReader inputReader = new InputStreamReader(input, "UTF-8");
			BufferedReader reader = new BufferedReader(inputReader);
			String line;
			while ((line = reader.readLine()) != null) {
				buffer.append(line);
			}
			// 关闭连接、释放资源
			reader.close();
			inputReader.close();
			input.close();
			input = null;
			connection.disconnect();
			//System.out.println("buffer:"+buffer.toString());
		} catch (Exception e) {
		}
		return buffer.toString();
	}
%>
<%
String imgUrl="";
String reqUrl="";
JSONObject json = new JSONObject();
String warehouseId=request.getParameter("warehouseId").toString();
Map<String,String> warehouseIdMap = new HashMap<String, String>();
warehouseIdMap.put("wahoId",warehouseId);
String URL = ZkPropertyUtil.get("BaseUrlStorage")+"Warehouse/getWarehouseInfo";
json = ApiUtils.excutePost(URL,null,null,warehouseIdMap);


System.out.println("code:::::::"+json.getIntValue("code"));
if(json.getIntValue("code")==10000){

	JSONObject warehousejson = json.getJSONObject("data").getJSONObject("Warehouse");
	System.out.println("warehousejson:::::::"+warehousejson.toString());
	if(warehousejson!=null && StringUtils.isNotEmpty(warehousejson.getString("qrcodeUrl"))){
		reqUrl = warehousejson.getString("qrcodeUrl");
		imgUrl="http://qr.topscan.com/api.php?text="+reqUrl;
	}else{
		String s=warehouseId+"_mdId";
		String createQrcodeURL = ZkPropertyUtil.get("baseURL")+"whc/webservice/createQrcode.jsp?sceneStr="+s;
		reqUrl =httpRequest(createQrcodeURL);
		Map<String,String> createQrcodeMap = new HashMap<String, String>();
		createQrcodeMap.put("wahoId",warehouseId);
		createQrcodeMap.put("qrcodeUrl",reqUrl);
		JSONObject createQrcodejson = new JSONObject();
		createQrcodejson = ApiUtils.excutePost(URL,null,null,createQrcodeMap);
//		if(createQrcodejson.getIntValue("code") == 10000 && createQrcodejson.getJSONObject("data").getIntValue("result")==1){
//
//		}
		imgUrl="http://qr.topscan.com/api.php?text="+reqUrl;
	}
}
System.out.println("imgUrl:::::::"+imgUrl);
	 %>
	<!DOCTYPE html>
	<html>
	<head>
		<meta charset="utf-8">
		<meta name="viewport" content="width=device-width,height=device-height, initial-scale=1, user-scalable=no, minimum-scale=1.0, maximum-scale=1.0">
		<style>
			html,body{
				height: 100%;
				overflow: hidden;
				text-align: center;
				font-family:"Microsoft Yahei";	
				background-color: #0ae;
				color: #fff;
			}
			#output{
				width: 60%;
				margin: 35% 20%;		
			}
			#output img{
				width: 100%;
			}
		</style>
	</head>
	<body>
  	<div id="output"><img src="<%=imgUrl %>"/></div>
  	<div>请长按二维码图片，识别二维码。关注我们！</div>
	<script src="<%=request.getContextPath() %>/resource/js/jquery-2.0.3.min.js"></script>
	</body>
	</html>