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
import org.wss.weixin.action.IOAuth2Servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
public class OAuth2WarehouseServlet extends IOAuth2Servlet {

	private static final long serialVersionUID = 1L;
	@Override
	protected String getRedirectView(HttpServletRequest request,
			HttpServletResponse response, String openId) {
		System.out.println("openId:"+openId);
		String parms=request.getParameter("parms");//0 仓 1柜   11新柜
		System.out.println("parms&&&&&&"+parms);
		String url="";
		String BaseURL = ZkPropertyUtil.get("BaseUrlMember")+"memberManage/getMember";
		Map<String,String> memberMap = new HashMap<String,String>();
		memberMap.put("openId", openId);
		memberMap.put("isSub", "1");
		//String sendMember = HttpGetUtils.httpGet(BaseURL,null,memberMap,"UTF-8");
		JSONObject sendMember = null;
		try {
			sendMember = ApiUtils.excuteGet(BaseURL, null, memberMap);
			//换成alibaba的JSONOBject
			JSONObject dataMember = sendMember.getJSONObject("data");
			if ("false".equals(dataMember.get("isMember")) || dataMember==null ) {
				url = ZkPropertyUtil.get("baseURL") + "web/whc/member/memberVerifyResult.jsp?openId="+openId;
			} else {
				//新柜
				if(parms.equals("11")){

					url = ZkPropertyUtil.get("baseURL") + "web/whc/warehouse/storeList.html?openId="+ openId+"&wahoType="+parms+"&t="+(new Date()).getTime();
				}else{
					System.out.println("仓--openId--："+openId);
					url = ZkPropertyUtil.get("baseURL") + "web/whc/warehouse/warehouseList.jsp?openId=" + openId+"&wahoType="+parms+"&t="+(new Date()).getTime();
				}
			}
		} catch (Exception e) {
			url="ERROR:9999";
			e.printStackTrace();
		}

		return url;
	}
}
