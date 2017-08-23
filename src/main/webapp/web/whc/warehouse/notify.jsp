<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@page import="java.io.ByteArrayInputStream" %>
<%@page import="java.io.InputStream" %>
<%@page import="java.io.Writer" %>
<%@page import="java.util.HashMap" %>
<%@page import="java.util.List" %>
<%@page import="java.util.Map" %>
<%@page import="org.dom4j.Document" %>
<%@page import="org.dom4j.Element" %>
<%@page import="org.dom4j.io.SAXReader" %>
<%@page import="java.text.SimpleDateFormat" %>
<%@page import="java.text.DecimalFormat" %>
<%@page import="org.apache.log4j.Logger" %>
<%@ page import="com.google.common.base.Strings" %>
<%@page import="com.whc.wx.web.controller.order.service.impl.OderPayForYSServiceImpl" %>
<%!

public static Map<String, String> parseXml(String xml) throws Exception {
	System.out.println("------xml-----:"+xml);
	// 将解析结果存储在HashMap中
	Map<String, String> map = new HashMap<String, String>();

	// 从request中取得输入流
	InputStream inputStream = new ByteArrayInputStream(xml.getBytes("UTF-8"));
	// 读取输入流
	SAXReader reader = new SAXReader();
	Document document=null;
	try{
	 document = reader.read(inputStream);
	}catch(Exception e){
		e.printStackTrace();
	}
	// 得到xml根元素
	Element root = document.getRootElement();
	// 得到根元素的所有子节点
	List<Element> elementList = root.elements();

	// 遍历所有子节点
	for (Element e : elementList)
		map.put(e.getName(), e.getText());

	// 释放资源
	inputStream.close();
	inputStream = null;

	return map;
}
%>
<%
	System.out.println("------xml123-----:");
String inputLine;
String notityXml = "";
String resXml = "";
String message="";
	System.out.println("------xml123-----:");
	OderPayForYSServiceImpl iOrderPayForYSService = new OderPayForYSServiceImpl();
try {
	while ((inputLine = request.getReader().readLine()) != null) {
	notityXml += inputLine;
	}
	request.getReader().close();
}catch (Exception e) {
	e.printStackTrace();
}
 System.out.println("notityXml"+notityXml);
 Logger notifyLog = Logger.getLogger("Notify"); 
 notifyLog.info("notityXml: "+ notityXml);
 Map m=new HashMap();
 m=parseXml(notityXml);
 String orderId=m.get("orderId").toString();
 System.out.println("orderId******"+orderId);
 String flag=m.get("flag").toString();
 String transactionId=m.get("transaction_id").toString();
System.out.println("-----flag---123---"+flag);
 synchronized (this)  {
		 if("0".equals(flag)){
			 System.out.println("-----flag---1234---"+flag);
			  message=iOrderPayForYSService.updateOrderStatus(orderId,transactionId);
		 
		 }else if("1".equals(flag)){
			 System.out.println("-----flag---12345---"+flag);
			 String coverWarehouseAmount="";
			 String endDate="";

			 if(Strings.isNullOrEmpty((String)m.get("memo"))){
		     String memo=m.get("memo").toString();
			 String[] memos=memo.split("~");
			  coverWarehouseAmount=memos[0];
			  endDate=memos[1];
			  System.out.println("memo******"+memo);
			  System.out.println("coverWarehouseAmount******"+coverWarehouseAmount);
			  System.out.println("endDate******"+endDate);
			  }
			 
			 message=iOrderPayForYSService.updateOrderStatusByCoverWarehouse(coverWarehouseAmount,endDate,orderId,transactionId);
		
		 }
 }
 if("success".equals(message)){
response.setContentType("text/xml");
out.println("<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>");
 }

%>

