<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="com.whc.wx.web.util.WPayUtil"%>
<%
	String describe     = request.getParameter("describe");
	String totalFee = request.getParameter("totalFee");
	String openId   = request.getParameter("openId");
	String orderId  = request.getParameter("orderId");
	String attach = request.getParameter("attach");
	String oldOrderId=request.getParameter("oldOrderId");
	String flag=request.getParameter("flag");
	String memo=request.getParameter("memo");
	System.out.println("memo:"+memo);
	out.println(WPayUtil.wPay(request, response, openId, orderId,oldOrderId,flag,memo,describe,totalFee, attach));
%>

