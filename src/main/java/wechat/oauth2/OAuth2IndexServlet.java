package wechat.oauth2;

/**
 * OAuth2 servlet类
 * 
 * @author
 * @date 2014.10.16
 */
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.wanhuchina.common.util.http.HttpGetUtils;
import com.wanhuchina.common.util.zk.ZkPropertyUtil;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.wss.weixin.action.IOAuth2Servlet;

import java.util.HashMap;
import java.util.Map;

public class OAuth2IndexServlet extends IOAuth2Servlet {
	public static Logger IOAuth2ServletImplLoglogger = Logger.getLogger("IOAuth2ServletImpl");
	private static final long serialVersionUID = 1L;

	@Override
	protected String getRedirectView(HttpServletRequest request,
			HttpServletResponse response, String openId) {
		String BaseURLMember = ZkPropertyUtil.get("BaseUrlMember")+"memberManage/getMember";
		Map<String,String> memberMap = new HashMap<String,String>();
		memberMap.put("openId", openId);
		memberMap.put("isSub", "1");
		String sendMember = HttpGetUtils.httpGet(BaseURLMember,null,memberMap,"UTF-8");
		JSONObject memberObject = JSONObject.fromObject(sendMember);
		JSONObject dataMember = memberObject.getJSONObject("data");
		String BaseURL=ZkPropertyUtil.get("baseURL");
		String url="";
		if ("false".equals(dataMember.get("isMember"))) {
			url = BaseURL+ "web/whc/member/memberVerifyResult.html?openId="+openId;
		} else {
			url = BaseURL + "web/whc/member/whc-index.jsp?openId=" + openId;
		}
		return url;
	}
}
