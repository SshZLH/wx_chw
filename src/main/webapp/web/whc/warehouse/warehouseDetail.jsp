<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="com.wanhuchina.common.util.zk.ZkPropertyUtil" %>
<%@ page import="com.alibaba.fastjson.JSONObject" %>
<%@ page import="com.wanhuchina.common.util.http.ApiUtils" %>

<%
String openId = (String)request.getParameter("openId");
String warehouseId = (String)request.getParameter("warehouseId");
Map<String,String> warehouseMap = new HashMap<String,String>();
warehouseMap.put("wahoId",warehouseId);
String warehouseURL = ZkPropertyUtil.get("BaseUrlStorage")+"Warehouse/getWarehouseInfo";
JSONObject warehouseJSON= ApiUtils.excutePost(warehouseURL,null,null,warehouseMap);
    JSONObject warehouse = null;
if(warehouseJSON.getIntValue("code")==10000){
    warehouse = warehouseJSON.getJSONObject("data").getJSONObject("Warehouse");
}else {
    System.out.println("--------Warehouse/getWarehouseInfo--------");
}




String supportUrl = ZkPropertyUtil.get("supportURL");
%>

<!DOCTYPE html>
<html >
<head>
    <meta charset="UTF-8">
    <title></title>
    <meta name="viewport" content="width=device-width,initial-scale=1.0,maximum-scale=1.0,user-scalable=no" />
    <link rel="stylesheet" type="text/css" href="/web/whc/common/css/base.css" />
    <link rel="stylesheet" href="/web/whc/warehouse/css/page4.css" />
</head>
<body>
    <div class="img22">
       <a href=""> <img src="<%=supportUrl%>app/whc/uploadFile/import/<%=warehouse.getString("image1") %>" class="full" alt="" /></a>
    </div>

<div class="block page44">
    <p>地址:<%=warehouse.getString("addr")%></p>
    <p>介绍:<%=warehouse.getString("introduce")%></p>
    <!--<p>如果您：在8月5日——9月5日期间办理入仓手续，</p>
    <p>您就可以享受：免费租仓、免费搬运、免费配锁……的全免服务！！</p>-->
</div>
</body>
</html>
