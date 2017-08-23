package org.wss.utils;

import com.alibaba.fastjson.JSON;
import com.wanhuchina.common.util.http.ApiUtils;
import com.wanhuchina.common.util.zk.ZkPropertyUtil;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.wanhuchina.common.util.weixin.cgi.AccessToken;
import com.wanhuchina.common.util.weixin.cgi.WeixinUtil;

import java.util.HashMap;
import java.util.Map;

public class WeixinMessageUtil {
	private static final Logger logger= LoggerFactory.getLogger(WeixinMessageUtil.class);

	public static void sendWcmsMessage(String mediaId, String toGroupIds, boolean isToAll) {
		//if(StringUtils.isNull(mediaId))  BizException.handleMessageException("操作失败,参数mediaId不能为空！");
		//if(StringUtils.isNull(isToAll))  BizException.handleMessageException("操作失败,参数isToAll不能为空！");
		//if(!isToAll)
			//if(StringUtils.isNull(toGroupIds))  BizException.handleMessageException("操作失败,参数toGroupIds不能为空！");
		String url = "https://api.weixin.qq.com/cgi-bin/message/mass/sendall?access_token=ACCESS_TOKEN";
		AccessToken accessToken = WeixinUtil.getAccessToken(ZkPropertyUtil.get("corpId"), ZkPropertyUtil.get("secret"));
		url = url.replace("ACCESS_TOKEN", accessToken.getToken());
		JSONObject postData = new JSONObject();
		JSONObject filter = new JSONObject();
		filter.put("is_to_all", isToAll);
		filter.put("group_id", toGroupIds);
		postData.put("filter", filter);
		JSONObject mpnews = new JSONObject();
		mpnews.put("media_id", mediaId);
		postData.put("mpnews", mpnews);
		postData.put("msgtype", "mpnews");
		WeixinUtil.httpRequest(url, "POST", postData.toString());
	}

	/**
	 * 记录短信日志
	 * @param telephone
	 * @param content
     */
	public static void AysncSendSmsLog(String telephone,String content,String platform){
		JSONObject smsJson = new JSONObject();
		Map<String,String> smsMap = new HashMap<String, String>();
		smsMap.put("telephone",telephone);
		smsMap.put("content",content);
		smsMap.put("platform",platform);
		String smsURL = ZkPropertyUtil.get("BaseUrlMember")+"MessageSms/saveMsgSms";
		String result = JSON.toJSONString(smsMap);
		try{
			smsJson = ApiUtils.excutePost(smsURL,null,result,null);
			if(smsJson.getIntValue("code")!=10000){
				logger.info("-----------短信接收失败，telephone={}",telephone);
			}
		}catch (Exception e){
			e.printStackTrace();
		}
	}
			
}
