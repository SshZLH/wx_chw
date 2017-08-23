package wechat.oauth2;

/**
 * OAuth2 servlet类
 * 
 * @author ivhhs
 * @date 2014.10.16
 */
import com.alibaba.fastjson.JSONObject;
import com.wanhuchina.common.util.http.ApiUtils;
import com.wanhuchina.common.util.zk.ZkPropertyUtil;
import org.slf4j.LoggerFactory;
import org.wss.weixin.action.IOAuth2Servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class OAuth2MemberCenterServlet extends IOAuth2Servlet {

	private static final org.slf4j.Logger log = LoggerFactory.getLogger(OAuth2MemberCenterServlet.class);


	private static final long serialVersionUID = 1L;
	@Override
	protected String getRedirectView(HttpServletRequest request,
			HttpServletResponse response, String openId) {
		System.out.println("------------OAuth2MemberCenterServlet:--openId---"+openId);
		String BaseURLMember = ZkPropertyUtil.get("BaseUrlMember")+"memberManage/getMember";
		Map<String,String> memberMap = new HashMap<String,String>();
		memberMap.put("openId", openId);
		memberMap.put("isSub", "1");   //关注
		//String sendMember = HttpGetUtils.httpGet(BaseURLMember,null,memberMap,"UTF-8");
		JSONObject memberObject = null;
		try {
			memberObject = ApiUtils.excuteGet(BaseURLMember, null, memberMap);
		} catch (Exception e) {
			e.printStackTrace();
			log.info("调用"+BaseURLMember+"接口错误:"+e.getMessage());
			return "错误";

		}

		JSONObject dataMember = memberObject.getJSONObject("data");
		String BaseURL=ZkPropertyUtil.get("baseURL");
		String url="";
		System.out.println("------------OAuth2MemberCenterServlet:---isMember--"+dataMember.get("isMember"));
		if ("false".equals(dataMember.get("isMember"))) {
			url = BaseURL + "web/whc/member/memberVerifyResult.html?openId="+openId;
		} else {
			url = BaseURL + "web/whc/member/center.html?openId=" + openId+"&t="+(new Date()).getTime();
		}
		return url;
	}
}
