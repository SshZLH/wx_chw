package org.wss.weixin.action.impl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.wanhuchina.common.util.zk.ZkPropertyUtil;
import org.wss.weixin.action.IOAuth2Servlet;

public class SampleOAuth2ServletImpl extends IOAuth2Servlet {
	@Override
	protected String getRedirectView(HttpServletRequest request, HttpServletResponse response, String openId) {
		return ZkPropertyUtil.get("baseURL") + "/web/wes/auth/authsucc.jsp?openId=" + openId;
	}
	
}
