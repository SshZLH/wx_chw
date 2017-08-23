<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="com.wanhuchina.common.util.zk.ZkPropertyUtil" %>
<%@ page import="com.alibaba.fastjson.JSONObject" %>
<%@ page import="com.wanhuchina.common.util.http.ApiUtils" %>
<%
String openId = (String)request.getParameter("openId");
String warehouseId = (String)request.getParameter("warehouseId");
Map<String,String> warehouseMap = new HashMap<String,String>();
warehouseMap.put("wahoId",warehouseId);
String URL = ZkPropertyUtil.get("BaseUrlStorage")+"Warehouse/getWarehouseInfo";
JSONObject json=null;
json =  ApiUtils.excutePost(URL,null,null,warehouseMap);
JSONObject warehouseJson = json.getJSONObject("data").getJSONObject("Warehouse");
%>
<!doctype html>
<html>
<head>
	<title></title>
	<meta charset="utf-8" />
	<meta name="viewport" content="width=device-width,initial-scale=1.0,maximum-scale=1.0,user-scalable=0">
    <meta name="apple-mobile-web-app-capable" content="yes">
	<meta name="apple-mobile-web-app-status-bar-style" content="black">
	<link rel="stylesheet" type="text/css" href="css/base.css" />
	<link rel="stylesheet" type="text/css" href="css/map.css" />
    <title>地图界面</title>
    <link rel="stylesheet" href="css/init.css" />
    <link rel="stylesheet" href="css/cabinet.css"/>
    <link rel="stylesheet" href="css/flexslider.css" />
	<script src="../common/js/init.js"></script>
	<script src="js/jquery-1.11.1.min.js"></script>
	<script src="js/jquery.flexslider-min.js"></script>
	<script type="text/javascript" src="js/zepto.js"></script>
	<style>
		#map{
			z-index:-1;
			height:100%;
		}
		.o-cardview .item-inner{
			display:none
		}
		.telDiv_bottom .right{
			width:2.9rem;
			height:1.15rem;
			line-height:1.15rem;
			text-align:center;
			color:#a9a9a9;
			font-size:.4rem;
			float:right;
		}
	</style>
</head>
<body>

	<!--头部信息-->
	<div class="titleInfo" id="headerDivID">
	</div>
	<div class="transparent"></div>
	<div class="telDiv" id="telDivID" style="display: none;">
	</div>
	<div class="map" style="height:100%;z-index:-1">
		<iframe width="100%" heigth="100%"src="http://m.amap.com/?q=<%=warehouseJson.getString("lati") %>,<%=warehouseJson.getString("longi") %>" id="map"></iframe>
	</div>

	<script type="text/javascript">
		initHeader("headerDivID", "地理位置");
		initCSPhone("telDivID");
	</script>
	<script type="text/javascript">
		$(function(){
			$('.titLeft').on('click',function(){
				window.history.go(-1);
			})
			/*点击打电话按钮打电话*/
			$(".telPhone").on('click',function(){
				$(".transparent").show();
				$(".telDiv").show();
				$(".telDiv .left").on('click',function(){
					$(".transparent").hide();
					$(".telDiv").hide();
				})
			});
		})
		$(function(){
			$("#map").css("height",$(window).height()*1);
		})
	</script>

</body>
</html>
