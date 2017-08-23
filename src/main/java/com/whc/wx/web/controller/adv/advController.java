package com.whc.wx.web.controller.adv;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wanhuchina.common.util.http.ApiUtils;
import com.wanhuchina.common.util.zk.ZkPropertyUtil;
import com.whc.wx.web.controller.order.service.impl.OderForYSServiceImpl;
import com.whc.wx.web.util.renewTimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by WangShengZhan
 * Email：WangShengZhan@wanhuchina.com
 * Date：2017/6/28
 * Time：10:31
 */

@Controller
public class advController {
    private static final Logger logger= LoggerFactory.getLogger(advController.class);
    /**
     * 订单广告
     * @param req
     * @return
     */
    @RequestMapping(value="advManage",produces="application/json; charset=utf-8")
    @ResponseBody
    public String advManage(HttpServletRequest req){
        String advStr=null;
        String openId = req.getParameter("openId");
        String orderId = req.getParameter("orderId");
        try {
//            Map<String,String> wahoMap = new HashMap<String,String>();
//            wahoMap.put("orderId",orderId);
//            JSONObject wahojson = new JSONObject();
//            String wahoURL = ZkPropertyUtil.get("BaseUrlAdv")+"advManage/getWahoIdByOrderId";
//            wahojson = ApiUtils.excutePost(wahoURL,null,null,wahoMap);
//            if(wahojson.getIntValue("code")==10000){
//                String wahoId = wahojson.getJSONObject("data").getString("wahoId");
                Map<String,String> memberMap = new HashMap<String, String>();
                memberMap.put("openId",openId);
                JSONObject memberjson = new JSONObject();
                String memberURL = ZkPropertyUtil.get("BaseUrlMember")+"memberManage/getMemberByOpenId";
                memberjson = ApiUtils.excutePost(memberURL,null,null,memberMap);
                if(memberjson.getIntValue("code")==10000){
                    JSONObject member = memberjson.getJSONObject("data");
                    String memberId = member.getString("id");
                    Map<String,String> advMap = new HashMap<String,String>();
                    advMap.put("orderId",orderId);
                    advMap.put("memberId",memberId);
                    String advURL = ZkPropertyUtil.get("BaseUrlAdv")+"advManage/showAdvByOrderEnd";
                    JSONObject advjson = new JSONObject();
                    advjson = ApiUtils.excutePost(advURL,null,null,advMap);
                    if(advjson.getIntValue("code")==10000){
                        advStr=advjson.getString("data");
                    }else{
                        advStr="error";
                        logger.info("--------advManage/showAdvByOrderEnd---------"+advjson.getIntValue("code"));
                    }
                }else{
                    logger.info("---------memberManage/getMemberByOpenId--------"+memberjson.getIntValue("code"));
                }
//            }else{
//                logger.info("--------advManage/getWahoIdByOrderId---------"+wahojson.getIntValue("code"));
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return advStr;
    }
    /**
     * 门店广告
     * @param req
     * @return
     */
    @RequestMapping(value="ShowAdvByWace",produces="application/json; charset=utf-8")
    @ResponseBody
    public String ShowAdvByWace(HttpServletRequest req){
        String send = null;
        try {
            String wahoId = req.getParameter("wahoId");
            String openId = req.getParameter("openId");
            Map<String,String> openidMap = new HashMap<String,String>();
            openidMap.put("openId",openId);
            String openIdURL = ZkPropertyUtil.get("BaseUrlMember")+"memberManage/getMemberByOpenId";
            JSONObject memberIdjson = ApiUtils.excutePost(openIdURL,null,null,openidMap);
            if(memberIdjson.getIntValue("code")==10000){
                JSONObject member = memberIdjson.getJSONObject("data");
                Map<String,String> advMap = new HashMap<String,String>();
                advMap.put("wahoId",wahoId);
                advMap.put("memberId",member.getString("id"));
                String advURL = ZkPropertyUtil.get("BaseUrlAdv")+"advManage/ShowAdvByWace";
                JSONObject advjson = null;

                advjson = ApiUtils.excutePost(advURL,null,null,advMap);
                send = advjson.toString();
            }else{
                logger.info("---------memberManage/getMemberByOpenId--------"+memberIdjson.getIntValue("code"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return send;
    }
    /**
     * 广告点击次数
     * @param req
     */
    @RequestMapping(value="advClicks",produces="application/json; charset=utf-8")
    @ResponseBody
    public String AdvClicks( HttpServletRequest req){
        try {
            String openId = req.getParameter("openId");
            Map<String,String> memberMap = new HashMap<String, String>();
            memberMap.put("openId",openId);
            JSONObject memberjson = new JSONObject();
            String memberURL = ZkPropertyUtil.get("BaseUrlMember")+"memberManage/getMemberByOpenId";
            memberjson = ApiUtils.excutePost(memberURL,null,null,memberMap);
            if(memberjson.getIntValue("code")==10000){
                JSONObject member = memberjson.getJSONObject("data");
                Map<String,String> wahoMap = new HashMap<String,String>();
                String orderId = req.getParameter("orderId");
                wahoMap.put("orderId",orderId);
                String wahoURL = ZkPropertyUtil.get("BaseUrlAdv")+"advManage/getWahoIdByOrderId";
                JSONObject wahoIdjson = ApiUtils.excutePost(wahoURL,null,null,wahoMap);
                if(wahoIdjson.getIntValue("code")==1000){
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String activityId = req.getParameter("activityId");
                    String contentId = req.getParameter("contentId");
                    String clicksPhoneType = req.getParameter("clicksPhoneType");
                    String clicksUserIp = req.getParameter("clicksUserIp");
                    String memberId = member.getString("id");
                    String wahoId = req.getParameter("wahoId");
                    String Time = req.getParameter(dateFormat.format(new Date()));
                    Map<String,String> clickMap = new HashMap<String,String>();
                    clickMap.put("activityId",activityId);
                    clickMap.put("contentId",contentId);
                    clickMap.put("clicksPhoneType",clicksPhoneType);
                    clickMap.put("clicksUserIp",clicksUserIp);
                    clickMap.put("memberId",memberId);
                    clickMap.put("wahoId",wahoId);
                    clickMap.put("Time",Time);
                    String clickURL = ZkPropertyUtil.get("BaseUrlAdv")+"advManage/addClickAdv";
                    String insclick = JSON.toJSONString(clickMap);
                    JSONObject clickObject =new JSONObject();
                    clickObject = ApiUtils.excutePost(clickURL,null,insclick,null);
                    if(clickObject.getIntValue("code")==10000){
                        logger.info("----------advManage/addClickAdv-------成功");
                    }else{
                        logger.info("----------advManage/addClickAdv-------:失败:"+clickObject.getIntValue("code"));
                    }
                }else{
                    logger.info("----------advManage/getWahoIdByOrderId-------"+wahoIdjson.getIntValue("code"));
                }
            }else{
                logger.info("----------memberManage/getMemberByOpenId-------"+memberjson.getIntValue("code"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
