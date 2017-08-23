package org.wss.weixin.action.impl;

import javax.servlet.ServletException;
import com.wanhuchina.common.util.springcontext.SpringContextUtil;
import com.whc.wx.service.IWeixinBizService;
import org.wss.weixin.action.IWeixinServlet;



public class SampleWeixinServletImpl extends IWeixinServlet {
	
	@Override
	protected IWeixinBizService getWeixinBizService() {
		return (IWeixinBizService)SpringContextUtil.getBean("iSampleWeixinBizService");
	}
	@Override
	public void init() throws ServletException {
		// TODO Auto-generated method stub
		super.init();
	}
	

}
