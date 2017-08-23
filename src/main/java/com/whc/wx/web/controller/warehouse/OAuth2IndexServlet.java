package com.whc.wx.web.controller.warehouse;

import com.alibaba.fastjson.JSONObject;
import com.wanhuchina.common.util.http.ApiUtils;
import com.wanhuchina.common.util.zk.ZkPropertyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by WangShengZhan
 * Email：WangShengZhan@wanhuchina.com
 * Date：2017/7/20
 * Time：11:16
 */
public class OAuth2IndexServlet extends IOAuth2Servlet {
    private static final Logger logger= LoggerFactory.getLogger(OAuth2IndexServlet.class);
    @Override
    public  String getRedirectView(HttpServletRequest request, HttpServletResponse response, String openId) {
        String url = "";
        Map<String, String> memberMap = new HashMap<String, String>();
        memberMap.put("openId", openId);
        memberMap.put("isSub", "1");
        JSONObject memberjson = new JSONObject();
        String memberURL = ZkPropertyUtil.get("BaseUrlMember") + "memberManage/getMember";
        try {
            memberjson = ApiUtils.excuteGet(memberURL, null, memberMap);
            if (memberjson.getIntValue("code") == 10000
                    && memberjson.getJSONObject("data") != null) {

                url = ZkPropertyUtil.get("baseURL") + "web/whc/member/memberVerifyResult.jsp?openId=" + openId;

            } else {
                url = ZkPropertyUtil.get("baseURL") + "web/whc/member/whc-index.jsp?openId=" + openId;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}
