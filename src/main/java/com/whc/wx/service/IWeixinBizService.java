package com.whc.wx.service;


public interface IWeixinBizService  {
	public static final String WEIXINBIZSERVICE = IWeixinBizService.class.getName();
	
	public String weixinExecute(String msg);
}
