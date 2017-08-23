<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="com.alibaba.fastjson.JSONObject"%>
<%@page import="com.whc.wx.web.controller.order.service.impl.MemberServiceImpl"%>


<%
String deviceId = request.getParameter("deviceId");
String openId = request.getParameter("openId");
String type = request.getParameter("type");
JSONObject res = new JSONObject();
MemberServiceImpl iMemberService = new MemberServiceImpl();
res=iMemberService.openDoor(deviceId,openId,type);
out.print(res.toString());
%>

