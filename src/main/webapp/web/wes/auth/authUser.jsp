<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="org.wes.weixin.common.cgi.WeixinUtil"%>
<%@page import="org.simpro.util.ConfigUtils"%>
<%
String openId = request.getParameter("openId");
String result = WeixinUtil.userAuthSucc(ConfigUtils.getValue("config/wes", "corpId"), ConfigUtils.getValue("config/wes", "secret"), openId);
%>
