package org.wss.weixin.common.cgi;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.wss.weixin.common.cgi.AccessToken;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 请求数据通用类
 * 
 * @author LoriYue
 */
public class WeixinUtil {
	
	// 获取access_token
	public final static String CGI_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
	//获取微信服务器IP地址
	public final static String CGI_GETCALLBACKIP_URL = "https://api.weixin.qq.com/cgi-bin/getcallbackip?access_token=ACCESS_TOKEN";
	/***自定义菜单**/
	// 自定义菜单创建
	public final static String CGI_CREATE_MENU_URL = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";
	// 自定义菜单查询
	public final static String CGI_GET_MENU_URL = "https://api.weixin.qq.com/cgi-bin/menu/get?access_token=ACCESS_TOKEN";
	// 自定义菜单删除
	public final static String CGI_DELETE_MENU_URL = "https://api.weixin.qq.com/cgi-bin/menu/delete?access_token=ACCESS_TOKEN";
	/***模板消息**/
	// 设置所属行业
	public final static String CGI_SET_INDUSTY_URL = "https://api.weixin.qq.com/cgi-bin/template/api_set_industry?access_token=ACCESS_TOKEN";
	//从行业模板库选择模板到账号后台
	public final static String CGI_ADD_TEMPLATE_URL = "https://api.weixin.qq.com/cgi-bin/template/api_add_template?access_token=ACCESS_TOKEN";
	//发送模板消息
	public final static String CGI_SEND_TEMPLATE_MESSAGE_URL = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=ACCESS_TOKEN";
	/***粉丝相关**/
	//根据OAuth2.0获取openId
	public final static String AUTHORIZE_TOKE_URL = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
	//创建分组
	public final static String CGI_CREATE_GROUP_URL = "https://api.weixin.qq.com/cgi-bin/groups/create?access_token=ACCESS_TOKEN";
	//查询所有分组
	public final static String CGI_GET_GROUPS_URL = "https://api.weixin.qq.com/cgi-bin/groups/get?access_token=ACCESS_TOKEN";
	//查询用户所在分组
	public final static String CGI_GET_GROUP_BY_OPENID_URL = "https://api.weixin.qq.com/cgi-bin/groups/getid?access_token=ACCESS_TOKEN";
	//修改分组名
	public final static String CGI_UPDATE_GROUP_URL = "https://api.weixin.qq.com/cgi-bin/groups/update?access_token=ACCESS_TOKEN";
	//移动用户分组
	public final static String CGI_MOVE_MEMBER_URL = "https://api.weixin.qq.com/cgi-bin/groups/members/update?access_token=ACCESS_TOKEN";
	//批量移动用户分组
	public final static String CGI_MOVE_MEMBERS_URL = "https://api.weixin.qq.com/cgi-bin/groups/members/batchupdate?access_token=ACCESS_TOKEN";
	//设置备注名
	public final static String CGI_UPDATE_MEMBER_URL = "https://api.weixin.qq.com/cgi-bin/user/info/updateremark?access_token=ACCESS_TOKEN";
	//获取用户基本信息
	public final static String CGI_GET_MEMBER_URL = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";
	//获取用户列表
	public final static String CGI_GET_MEMBERS_URL = "https://api.weixin.qq.com/cgi-bin/user/get?access_token=ACCESS_TOKEN&next_openid=NEXT_OPENID";
	//创建二维码ticket
	public final static String CGI_CREATE_QRCODE_URL = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=ACCESS_TOKEN";
	//通过ticket换取二维码
	public final static String CGI_SHOW_QRCODE_URL = "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=TICKET";
	public static Logger apiAccesslogger = Logger.getLogger("apiAccess"); 
	// 获取jsapi_ticket的接口地址（GET）
	public final static String CGI_JSAPI_TICKET = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=ACCESS_TOKEN&type=jsapi";
	/**
	 * 获取access_token
	 * 
	 * @param appid
	 *            管理组的凭证密钥，每个secret代表了对应用、通讯录、接口的不同权限；不同的管理组拥有不同的secret
	 * @return
	 */
	public static AccessToken getAccessToken(String appid, String secret) {
		AccessToken accessToken = null;
		Object cache = getCache("weixin", "access_token_" + appid);
		if(cache != null) {
			System.out.println("access_token_" + appid + "未过期！");
			return (AccessToken)cache;
		}
		System.out.println("access_token_" + appid + "不存在或已过期！");
		
		String requestUrl = CGI_TOKEN_URL.replace("APPID", appid).replace("APPSECRET", secret);
		JSONObject jsonObject = httpRequest(requestUrl, "GET", null);
		// 如果请求成功
		if (null != jsonObject) {
			try {
				accessToken = new AccessToken();
				accessToken.setToken(jsonObject.getString("access_token"));
				accessToken.setExpiresIn(jsonObject.getInt("expires_in"));
				System.out.println("获取token成功:" + jsonObject.getString("access_token") + "————" + jsonObject.getInt("expires_in"));
			} catch (Exception e) {
				accessToken = null;
				e.printStackTrace();
				// 获取token失败
				System.out.println(jsonObject.toString());
			}
		}
		setCache("weixin", "access_token_" + appid, accessToken);
		return accessToken;
	}
	
	/**
	 * 获取微信服务器IP地址
	 * @param appid
	 * @param secret
	 * @return ipList
	 */
	public static String[] getCallbackIpList(String appid, String secret) {
		AccessToken accessToken = getAccessToken(appid, secret);
		String requestUrl = CGI_GETCALLBACKIP_URL.replace("ACCESS_TOKEN", accessToken.getToken());;
		JSONObject jsonObject = httpRequest(requestUrl, "GET", null);
		String[] ipList = null;
		// 如果请求成功
		if (null != jsonObject) {
			try {
				JSONArray jsonArray = jsonObject.getJSONArray("ip_list");
				StringBuilder stringBuilder = new StringBuilder("");
				for (int i = 0; i < jsonArray.size(); i++) {
					stringBuilder.append(jsonArray.getString(i));
					if (i < jsonArray.size()-1) {
						stringBuilder.append(",");
					}
				}
				ipList = stringBuilder.toString().split(",");
			} catch (Exception e) {
				String error = String.format("获取ip_list失败 errcode:{} errmsg:{}", jsonObject.getInt("errcode"), jsonObject.toString());
				System.out.println(error);
			}
		}
		return ipList;
	}
	
	/**
	 * 自定义菜单创建
	 * @param appid
	 * @param secret
	 * @param jsonStr jsonStr 格式参考 http://mp.weixin.qq.com/wiki/13/43de8269be54a0a6f64413e4dfa94f39.html
	 * @return 0 成功
	 */
	public static int createMenu(String appid, String secret, String jsonStr) {
		AccessToken accessToken = getAccessToken(appid, secret);
		String requestUrl = CGI_CREATE_MENU_URL.replace("ACCESS_TOKEN", accessToken.getToken());
		return httpRequest(requestUrl, "POST", jsonStr).getInt("errcode");
	}
	
	/**
	 * 自定义菜单查询
	 * @param appid
	 * @param secret
	 * @return jsonObject
	 */
	public static JSONObject getMenu(String appid, String secret) {
		AccessToken accessToken = getAccessToken(appid, secret);
		String requestUrl = CGI_GET_MENU_URL.replace("ACCESS_TOKEN", accessToken.getToken());
		JSONObject jsonObject = httpRequest(requestUrl, "GET", null);
		// 如果请求成功
		if (null != jsonObject) {
			if (!jsonObject.has("menu")) {
				String error = String.format("获取menu失败 errcode:{} errmsg:{}", jsonObject.getInt("errcode"), jsonObject.toString());
				System.out.println(error);
			}
		}
		return jsonObject;
	}
	
	/**
	 * 自定义菜单删除
	 * @param appid
	 * @param secret
	 * @return 0 成功
	 */
	public static int deleteMenu(String appid, String secret) {
		AccessToken accessToken = getAccessToken(appid, secret);
		String requestUrl = CGI_DELETE_MENU_URL.replace("ACCESS_TOKEN", accessToken.getToken());
		return httpRequest(requestUrl, "GET", null).getInt("errcode");
	}
	
	/**
	 * 设置所属行业
	 * @param appid
	 * @param secret
	 * @param industryId1
	 * @param industryId2
	 * @return 0 成功
	 */
	public static int setTemplateIndusty(String appid, String secret, String industryId1, String industryId2) {
		AccessToken accessToken = getAccessToken(appid, secret);
		String requestUrl = CGI_SET_INDUSTY_URL.replace("ACCESS_TOKEN", accessToken.getToken());
		String jsonStr = "{\"industry_id1\":\"" + industryId1 +"\", \"industry_id2\":\"" + industryId2 + "\"}";
		return httpRequest(requestUrl, "POST", jsonStr).getInt("errcode");
	}
	
	/**
	 * 从行业模板库选择模板到账号后台
	 * @param appid
	 * @param secret
	 * @param templateNumber 模板编号
	 * @return templateId
	 */
	public static String addTemplate(String appid, String secret, String templateNumber) {
		AccessToken accessToken = getAccessToken(appid, secret);
		String templateId = null;
		String requestUrl = CGI_ADD_TEMPLATE_URL.replace("ACCESS_TOKEN", accessToken.getToken());
		String jsonStr = "{\"template_id_short\":\"" + templateNumber +"\"}";
		JSONObject jsonObject = httpRequest(requestUrl, "POST", jsonStr);
		if (null != jsonObject) {
			try {
				templateId = jsonObject.getString("template_id");
			} catch (Exception e) {
				String error = String.format("添加template失败 errcode:{} errmsg:{}", jsonObject.getInt("errcode"), jsonObject.toString());
				System.out.println(error);
			}
		}
		return templateId;
	}
	
	/**
	 * 发送模板消息
	 * @param appid
	 * @param secret
	 * @param jsonStr jsonStr 格式参考http://mp.weixin.qq.com/wiki/17/304c1885ea66dbedf7dc170d84999a9d.html#.E8.8E.B7.E5.BE.97.E6.A8.A1.E6.9D.BFID
	 * @return jsonObject
	 */
	public static synchronized JSONObject sendTemplateMessage(String appid, String secret, String jsonStr) {
		AccessToken accessToken = getAccessToken(appid, secret);
		String msgid = null;
		String requestUrl = CGI_SEND_TEMPLATE_MESSAGE_URL.replace("ACCESS_TOKEN", accessToken.getToken());
		JSONObject jsonObject = httpRequest(requestUrl, "POST", jsonStr);
		if (null != jsonObject) {
			try {
				msgid = jsonObject.getString("msgid");
			} catch (Exception e) {
				String error = String.format("发送模板消息失败 errcode:{} errmsg:{}", jsonObject.getInt("errcode"), jsonObject.toString());
				System.out.println(error);
			}
		}
		return jsonObject;
	}
	
	/**
	 * 通过auth2认证获得openId
	 * @param appid
	 * @param secret
	 * @param code
	 * @return openId
	 */
	public static String getOpenIdByOAuth2Code(String appid, String secret, String code) {
		String openId = null;
		String requestUrl = AUTHORIZE_TOKE_URL.replace("APPID", appid).replace("SECRET", secret).replace("CODE", code);
		JSONObject jsonObject = httpRequest(requestUrl, "GET", null);
		// 如果请求成功
		if (null != jsonObject) {
			try {
				openId = jsonObject.getString("openid");
			} catch (Exception e) {
				String error = String.format("获取openId失败 errcode:{} errmsg:{}", jsonObject.getInt("errcode"), jsonObject.toString());
				System.out.println(error);
			}
		}
		return openId;
	}
	
	/**
	 * 创建分组
	 * @param appid
	 * @param secret
	 * @param groupName 分组名字（30个字符以内）
	 * @return groupId
	 */
	public static String createGroup(String appid, String secret, String groupName) {
		AccessToken accessToken = getAccessToken(appid, secret);
		String groupId = null;
		String requestUrl = CGI_CREATE_GROUP_URL.replace("ACCESS_TOKEN", accessToken.getToken());
		String jsonStr = "{\"group\":{\"name\":\"" + groupName +"\"}}";
		JSONObject jsonObject = httpRequest(requestUrl, "POST", jsonStr);
		if (null != jsonObject) {
			try {
				groupId = jsonObject.getJSONObject("group").getInt("id") + "";
			} catch (Exception e) {
				String error = String.format("创建分组失败 errcode:{} errmsg:{}", jsonObject.getInt("errcode"), jsonObject.toString());
				System.out.println(error);
			}
		}
		return groupId;
	}
	
	/**
	 * 查询所有分组
	 * @param appid
	 * @param secret
	 * @return groupList 
	 */
	public static List<Map<String, String>> getGroups(String appid, String secret) {
		AccessToken accessToken = getAccessToken(appid, secret);
		String requestUrl = CGI_GET_GROUPS_URL.replace("ACCESS_TOKEN", accessToken.getToken());
		List<Map<String, String>> groupList = null;
		JSONObject jsonObject = httpRequest(requestUrl, "GET", null);
		if (null != jsonObject) {
			try {
				groupList = new ArrayList<Map<String,String>>();
				JSONArray jsonArray = jsonObject.getJSONArray("groups");
				for (int i = 0; i < jsonArray.size(); i++) {
					Map<String, String> map = new HashMap<String, String>();
					map.put("id", jsonArray.getJSONObject(i).getInt("id") + "");
					map.put("name", jsonArray.getJSONObject(i).getString("name"));
					map.put("count", jsonArray.getJSONObject(i).getInt("count") + "");
					groupList.add(map);
				}
			} catch (Exception e) {
				groupList = null;
				String error = String.format("查询所有分组失败 errcode:{} errmsg:{}", jsonObject.getInt("errcode"), jsonObject.toString());
				System.out.println(error);
			}
		}
		return groupList;
	}
	
	/**
	 * 查询用户所在分组
	 * @param appid
	 * @param secret
	 * @param openId
	 * @return groupId
	 */
	public static String getGroupByOpenId(String appid, String secret, String openId) {
		AccessToken accessToken = getAccessToken(appid, secret);
		String groupId = null;
		String requestUrl = CGI_GET_GROUP_BY_OPENID_URL.replace("ACCESS_TOKEN", accessToken.getToken());
		String jsonStr = "{\"openid\":\"" + openId +"\"}";
		JSONObject jsonObject = httpRequest(requestUrl, "POST", jsonStr);
		if (null != jsonObject) {
			try {
				groupId = jsonObject.getJSONObject("group").getInt("id") + "";
			} catch (Exception e) {
				String error = String.format("查询用户所在分组失败 errcode:{} errmsg:{}", jsonObject.getInt("errcode"), jsonObject.toString());
				System.out.println(error);
			}
		}
		return groupId;
	}
	
	/**
	 * 修改分组名
	 * @param appid
	 * @param secret
	 * @param groupId
	 * @param groupName 分组名字（30个字符以内）
	 * @return 0 成功
	 */
	public static int updateGroup(String appid, String secret, int groupId, String groupName) {
		AccessToken accessToken = getAccessToken(appid, secret);
		String requestUrl = CGI_UPDATE_GROUP_URL.replace("ACCESS_TOKEN", accessToken.getToken());
		String jsonStr = "{\"group\":\"{\"id\":" + groupId +",\"name\":\"" + groupName +"\"}}";
		return httpRequest(requestUrl, "POST", jsonStr).getInt("errcode");
	}
	
	/**
	 * 移动用户分组
	 * @param appid
	 * @param secret
	 * @param openId
	 * @param groupId
	 * @return 0 成功
	 */
	public static int moveMember(String appid, String secret, String openId, String groupId) {
		AccessToken accessToken = getAccessToken(appid, secret);
		String requestUrl = CGI_MOVE_MEMBER_URL.replace("ACCESS_TOKEN", accessToken.getToken());
		String jsonStr = "{\"openid\":\"" + openId +"\", \"to_groupid\":" + Integer.parseInt(groupId) +"}";
		return httpRequest(requestUrl, "POST", jsonStr).getInt("errcode");
	}
	
	/**
	 * 批量移动用户分组
	 * @param appid
	 * @param secret
	 * @param openIds
	 * @param groupId
	 * @return 0 成功
	 */
	public static int moveMembers(String appid, String secret, String openIds, String groupId) {
		AccessToken accessToken = getAccessToken(appid, secret);
		String requestUrl = CGI_MOVE_MEMBERS_URL.replace("ACCESS_TOKEN", accessToken.getToken());
		String [] openIdList = openIds.split(",");
		StringBuilder stringBuilder = new StringBuilder("");
		for (int i = 0; i < openIdList.length; i++) {
			stringBuilder.append("\"" + openIdList[i] + "\"");
			if (i < openIdList.length - 1) {
				stringBuilder.append(",");
			}
		}
		String jsonStr = "{\"openid_list\":[" + stringBuilder.toString() +"], \"to_groupid\":" + Integer.parseInt(groupId) +"}";
		return httpRequest(requestUrl, "POST", jsonStr).getInt("errcode");
	}
	
	/**
	 * 设置备注名
	 * @param appid
	 * @param secret
	 * @param openId
	 * @param memo
	 * @return 0 成功
	 */
	public static int updateMember(String appid, String secret, String openId, String memo) {
		AccessToken accessToken = getAccessToken(appid, secret);
		String requestUrl = CGI_UPDATE_MEMBER_URL.replace("ACCESS_TOKEN", accessToken.getToken());
		String jsonStr = "{\"openid\":\"" + openId +"\", \"remark\":\"" + memo +"\"}";
		return httpRequest(requestUrl, "POST", jsonStr).getInt("errcode");
	}
	
	/**
	 * 获取用户基本信息
	 * @param appid
	 * @param secret
	 * @param openId
	 * @return map
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, String> getMemberInfoByOpenId(String appid, String secret, String openId) {
		AccessToken accessToken = getAccessToken(appid, secret);
		String requestUrl = CGI_GET_MEMBER_URL.replace("ACCESS_TOKEN", accessToken.getToken()).replace("OPENID", openId);
		JSONObject jsonObject = httpRequest(requestUrl, "GET", null);
		Map<String, String> map = null;
		// 如果请求成功
		if (null != jsonObject) {
			try {
				map = new HashMap<String, String>();
				if(jsonObject.has("openid")){
					openId = jsonObject.getString("openid");
				}
				Iterator iterator = jsonObject.keys();
				while (iterator.hasNext()) {
					String key = (String)iterator.next();
					map.put(key, jsonObject.get(key) + "");
				}
			} catch (Exception e) {
				String error = String.format("获取用户基本信息 errcode:{} errmsg:{}", jsonObject.getInt("errcode"), jsonObject.toString());
				System.out.println(error);
			}
		}
		return map;
	}
	
	/**
	 * 获取用户列表(一次拉取调用最多拉取10000个关注者的OpenID，可以通过多次拉取的方式来满足需求。)
	 * @param appid
	 * @param secret
	 * @param nextOpenId 第一个拉取的OPENID，不填默认从头开始拉取
	 * @return jsonObect
	 */
	public static JSONObject getOpenIdList(String appid, String secret, String nextOpenId) {
		AccessToken accessToken = getAccessToken(appid, secret);
		String requestUrl = CGI_GET_MEMBERS_URL.replace("ACCESS_TOKEN", accessToken.getToken()).replace("NEXT_OPENID", nextOpenId);
		JSONObject jsonObject = httpRequest(requestUrl, "GET", null);
		// 如果请求成功
		if (null != jsonObject) {
			if (!jsonObject.has("total")) {
				String error = String.format("操作失败 errcode:{} errmsg:{}", jsonObject.getInt("errcode"), jsonObject.getString("errmsg"));
				System.out.println(error);
				jsonObject = null;
			} 
		}
		return jsonObject;
	}
	
	/**
	 * 创建临时二维码
	 * @param appid
	 * @param secret
	 * @param sceneId 32位非0整型
	 * @param expireSeconds 该二维码有效时间，以秒为单位。 最大不超过604800（即7天）
	 * @return url
	 */
	public static String createTempQrcode(String appid, String secret, int sceneId, int expireSeconds) {
		AccessToken accessToken = getAccessToken(appid, secret);
		String url = null;
		String requestUrl = CGI_CREATE_QRCODE_URL.replace("ACCESS_TOKEN", accessToken.getToken());
		String jsonStr = "{\"expire_seconds\": " + expireSeconds + ", \"action_name\": \"QR_SCENE\", \"action_info\": {\"scene\": {\"scene_id\": " + sceneId + "}}}";
		JSONObject jsonObject = httpRequest(requestUrl, "POST", jsonStr);
		if (!jsonObject.has("ticket")) {
			String error = String.format("操作失败 errcode:{} errmsg:{}", jsonObject.getInt("errcode"), jsonObject.getString("errmsg"));
			System.out.println(error);
		} else {
			url = CGI_SHOW_QRCODE_URL.replace("TICKET", jsonObject.getString("ticket"));
		}
		return url;
	}
	
	/**
	 * 创建永久二维码，是无过期时间的，但数量较少（目前为最多10万个）
	 * @param appid
	 * @param secret
	 * @param sceneStr 长度限制为1到64
	 * @return url
	 */
	public static JSONObject createQrcode(String appid, String secret, String sceneStr) {
		AccessToken accessToken = getAccessToken(appid, secret);
		String url = null;
		String requestUrl = CGI_CREATE_QRCODE_URL.replace("ACCESS_TOKEN", accessToken.getToken());
		String jsonStr = "{\"action_name\": \"QR_LIMIT_STR_SCENE\", \"action_info\": {\"scene\": {\"scene_str\": \"" + sceneStr + "\"}}}";
		System.out.println(jsonStr);
		JSONObject jsonObject = httpRequest(requestUrl, "POST", jsonStr);
		if (!jsonObject.has("ticket")) {
			String error = String.format("操作失败 errcode:{} errmsg:{}", jsonObject.getInt("errcode"), jsonObject.getString("errmsg"));
			System.out.println(error);
		} else {
			url = CGI_SHOW_QRCODE_URL.replace("TICKET", jsonObject.getString("ticket"));
		}
		return jsonObject;
	}
	
	@SuppressWarnings("unchecked")
	public static Map<String, String> getJsConfigMap(String url, String appId, String secret) {
		AccessToken token = getAccessToken(appId, secret);
		JSONObject jsonObject = getJsapiTicket(token, appId, url);
		String jsapiTicket = jsonObject.getString("ticket");
        Map<String, String> ret = sign(jsapiTicket, url);
        for (Map.Entry entry : ret.entrySet()) {
            System.out.println(entry.getKey() + ", " + entry.getValue());
        }
		return ret;
	}
	
	public static JSONObject getJsapiTicket(AccessToken token, String appId, String url) {
		Object cache = getCache("weixin", "jsapi_ticket_" + appId);
		if(cache != null) {
			System.out.println("jsapi_ticket_" + appId + "未过期！");
			return (JSONObject)cache;
		}
		System.out.println("jsapi_ticket_" + appId + "不存在或已过期！");
		String requestUrl = CGI_JSAPI_TICKET.replace("ACCESS_TOKEN", token.getToken());
		JSONObject jsonObject = httpRequest(requestUrl, "GET", null);
		System.out.println("getJsapiTicket-jsonObject:"+jsonObject.toString());
		setCache("weixin", "jsapi_ticket_" + appId, jsonObject);
		return jsonObject;
	}
	
	/**
	 * 发起https请求并获取结果
	 *            提交的数据
	 * @return JSONObject(通过JSONObject.get(key)的方式获取json对象的属性值)
	 */
	public static JSONObject httpRequest(String request, String RequestMethod, String output) {
		apiAccesslogger.info("httpRequest request:"+  request);
		JSONObject jsonObject = null;
		StringBuffer buffer = new StringBuffer();
		try {
			// 建立连接
			URL url = new URL(request);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setUseCaches(false);
			connection.setRequestMethod(RequestMethod);
			if (output != null) {
				apiAccesslogger.info("output:"+output);
				OutputStream out = connection.getOutputStream();
				out.write(output.getBytes("UTF-8"));
				out.close();
			}
			// 流处理
			InputStream input = connection.getInputStream();
			InputStreamReader inputReader = new InputStreamReader(input, "UTF-8");
			BufferedReader reader = new BufferedReader(inputReader);
			String line;
			while ((line = reader.readLine()) != null) {
				buffer.append(line);
			}
			// 关闭连接、释放资源
			reader.close();
			inputReader.close();
			input.close();
			input = null;
			connection.disconnect();
			apiAccesslogger.info("httpRequest response:"+  buffer.toString());
			jsonObject = JSONObject.fromObject(buffer.toString());
		} catch (Exception e) {
			apiAccesslogger.info("httpRequest response Exception:"+  e);
		}
		System.out.println("request:" + request+"\r\nresponse:" + jsonObject.toString());
		return jsonObject; 
	}
	
	public static Object getCache(String name, String key) {
		Object result = null;
		CacheManager manager = CacheManager.getInstance();
		Cache cache = manager.getCache(name);
		Element element = cache.get(key);
		try {
			Object value = element.getObjectValue();
			result = value; 
		} catch (RuntimeException e) {
			//e.printStackTrace();
		} 
		return result;
	}
	
	public static void setCache(String name, String key, Object value) {
		CacheManager manager = CacheManager.getInstance();
		Cache cache = manager.getCache(name);
		Element element = new Element(key, value);
		cache.put(element);
	}
	public static void removeAllCache(String name) {
		CacheManager manager = CacheManager.getInstance();
		Cache cache = manager.getCache(name);
		if (cache != null) {
			cache.removeAll();
		}
	}
	public static void removeCache(String name, String key) {
		CacheManager manager = CacheManager.getInstance();
		Cache cache = manager.getCache(name);
		cache.remove(key);
	}
	
	public static Cache getAllCaches(String name){
		return CacheManager.getInstance().getCache(name);
	}
	
	private static Map<String, String> sign(String jsapi_ticket, String url) {
        Map<String, String> ret = new HashMap<String, String>();
        String nonce_str = create_nonce_str();
        String timestamp = create_timestamp();
        String string1;
        String signature = "";

        //注意这里参数名必须全部小写，且必须有序
        string1 = "jsapi_ticket=" + jsapi_ticket +
                  "&noncestr=" + nonce_str +
                  "&timestamp=" + timestamp +
                  "&url=" + url;
        System.out.println(string1);

        try
        {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(string1.getBytes("UTF-8"));
            signature = byteToHex(crypt.digest());
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }

        ret.put("url", url);
        ret.put("jsapi_ticket", jsapi_ticket);
        ret.put("nonceStr", nonce_str);
        ret.put("timestamp", timestamp);
        ret.put("signature", signature);

        return ret;
    }

    private static String byteToHex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash)
        {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }

    private static String create_nonce_str() {
        return UUID.randomUUID().toString();
    }

    private static String create_timestamp() {
        return Long.toString(System.currentTimeMillis() / 1000);
    }
    
    public static String URLEncoder(String str) {
		String result = str;
		try {
			result = java.net.URLEncoder.encode(result, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
    
	public static void main(String args[]){
		AccessToken accessToken = getAccessToken("wx7af4c93160937924", "7fc5aaa119ef4d2632a3747d59c8c660");
		String url = "https://api.weixin.qq.com/cgi-bin/message/mass/preview?access_token=ACCESS_TOKEN".replace("ACCESS_TOKEN", accessToken.getToken());
		JSONObject object = new JSONObject();
		object.put("touser", "oCHobtye75gI3depvxosz7LHnme0");
		JSONObject mpnews = new JSONObject();
		mpnews.put("media_id", "7IznFGriM0qw9howJfkAq_tYafG5GmyXbrN47gOs3Vc");
		object.put("mpnews", mpnews);
		object.put("msgtype", "mpnews");
		httpRequest(url, "POST", object.toString());
		/*System.out.println(
				//createGroup("wxcd7543bbc8b077e8", "59b11f02d54b2a2a112d8db98ba43ffc", "yulong2015")
				//getGroups("wxcd7543bbc8b077e8", "59b11f02d54b2a2a112d8db98ba43ffc")
		createQrcode("wxcd7543bbc8b077e8", "59b11f02d54b2a2a112d8db98ba43ffc", "402881413a504f69013a527d03f10003")
		);*/
	}
}
