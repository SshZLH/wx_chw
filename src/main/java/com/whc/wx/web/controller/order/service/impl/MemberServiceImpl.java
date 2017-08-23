package com.whc.wx.web.controller.order.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wanhuchina.common.util.http.ApiUtils;
import com.wanhuchina.common.util.weixin.cgi.WeixinUtil;
import com.wanhuchina.common.util.zk.ZkPropertyUtil;
import com.whc.wx.web.controller.order.service.IMemberService;
import com.whc.wx.web.util.SMSUtils;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.wss.utils.WeixinMessageUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by WangShengZhan
 * Email：WangShengZhan@wanhuchina.com
 * Date：2017/7/20
 * Time：16:53
 */
@Service("iMemberService")
public class MemberServiceImpl implements IMemberService {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(MemberServiceImpl.class);
    @Override
    public JSONObject getMemberDoorPermissionByOpenId(String openId) {
        JSONObject res = new JSONObject();
        Map<String,String> memberMap = new HashMap<String, String>();
        memberMap.put("openId",openId);
        JSONObject memberjson = new JSONObject();
        String memberURL = ZkPropertyUtil.get("BaseUrlMember")+"memberManage/getMemberByOpenId";
        try {
             memberjson = ApiUtils.excutePost(memberURL,null,null,memberMap);
             if(memberjson.getIntValue("code")==10000){
             JSONObject member = memberjson.getJSONObject("data");
             if(member==null || "".equals(member)){
                 res.put("result", "-1");
             }else{
                 Map<String, String> resultMap = new HashMap<String, String>();
                 resultMap.put("memberId", member.getString("id"));
                 String resultURL = ZkPropertyUtil.get("BaseUrlStorage")+"storageManage/getDoorGuardList";
                 JSONObject resultJson = ApiUtils.excutePost(resultURL,null,null,resultMap);
                 if(resultJson.getIntValue("code")==10000){

                     JSONArray jsonArray = new JSONArray();
                     if(resultJson.getJSONArray("data")==null || "".equals(resultJson.getJSONArray("data"))){
                         res.put("memberId", member.getString("id"));
                         res.put("result", "-2");
                         res.put("resultList", jsonArray);
                     }else{
                         jsonArray.add(resultJson.getJSONArray("data"));
                         res.put("result", "0");
                         res.put("memberId", member.getString("id"));
                         res.put("resultList", jsonArray);
                     }
                 }else{
                     System.out.println("-----storage/getDoorGuardList--------");
                 }
             }
            }else{
                System.out.println("-----------memberManage/getMember-----------");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    public JSONObject openDoor(String deviceId, String openId, String type){

        String baseUrlStorage = ZkPropertyUtil.get("BaseUrlStorage");
        String baseUrlMember = ZkPropertyUtil.get("BaseUrlMember");
        JSONObject res = new JSONObject();
        MemberServiceImpl iMemberService = new MemberServiceImpl();
        JSONObject jsonObject = iMemberService.getMemberDoorPermissionByOpenId(openId);
        if(!"-1".equals(jsonObject.get("result"))) {
            Map<String,String> getDoorMap = new HashMap<String,String>();
            Map<String,String> getMemMap = new HashMap<String,String>();

            try {
                // 先通过openId找到memberId
                getMemMap.put("openId",openId);
                JSONObject jsonMember = ApiUtils.excutePost(baseUrlMember + "memberManage/getMemberByOpenId", null, null, getMemMap);
                if(jsonMember.getInteger("code") == 10000){//接口调用成功
                    if(jsonMember.getJSONObject("data")!=null){//如果member不为空
                        Map<String,String> getDoorGuardMap = new HashMap<String,String>();
                        getDoorGuardMap.put("memberId",jsonMember.getJSONObject("data").getString("id"));
                        JSONObject jsonGuardList = ApiUtils.excutePost(baseUrlStorage + "storageManage/getDoorGuardList", null, null, getDoorGuardMap);
                        getDoorMap.put("deviceId",deviceId);
                        getDoorMap.put("isDel","0");
                        JSONObject jsonDoor = ApiUtils.excutePost(baseUrlStorage + "storageManage/getDoor", null, null, getDoorMap);
                        if(jsonDoor.getJSONObject("data") == null){ //如果door 为null
                            //标记为门禁不存在
                        }else{
                            boolean flag = false;
                            if(!flag){
                                //通过memberId查询superman 判断用户是否具有超级权限
                                Map<String,String> getSuperman = new HashMap<String,String>();
                                getSuperman.put("memberId",jsonMember.getJSONObject("data").getString("id"));
                                JSONObject jsonSupperman = ApiUtils.excutePost(baseUrlMember + "memberManage/getSupperMan", null, null, getSuperman);
                                if(jsonSupperman.getInteger("code")==10000){//后台校验 返回返回值不为空10000则
                                    flag = true;
                                }
                            }
                            if(!flag){
                                JSONArray GuardList = jsonGuardList.getJSONArray("data");
                                if(GuardList.size()>0){
                                    for(int i = 0 ; i<GuardList.size();i++){
                                        JSONObject object = GuardList.getJSONObject(i);
                                        if(object.getString("deviceNo").equals(deviceId)){
                                            flag = true;
                                            break;
                                        }
                                    }
                                }else{

                                    res = jsonObject;
                                }
                            }
                            if(flag){
                                String url = ZkPropertyUtil.get("weilinkpushmsgURL") + "weilinkpushmsg1.0/dataMessageService?_webserviceName=pushMsgAction&content=D:OPEN:0&clientId="
                                        +jsonDoor.getJSONObject("data").getString("deviceNo") + "&time=" + new Date().getTime();

                                if("success".equals(WeixinUtil.httpRequest(url, "GET", null).getString("messageType"))) {
                                    Map<String,String> insDoorRecord = new HashMap<String,String>();
                                    insDoorRecord.put("doreUserId",openId);
                                    insDoorRecord.put("doorId",jsonDoor.getJSONObject("data").getString("id"));
                                    insDoorRecord.put("doreTime",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                                    insDoorRecord.put("dore_open_type",type);
                                    String insDoorRecordParam = JSON.toJSONString(insDoorRecord);
                                    JSONObject responParm = ApiUtils.excutePost(baseUrlStorage + "storageManage/insDoorRecord", null, insDoorRecordParam, null);
                                    if(responParm.getInteger("code") == 10000){
                                        res.put("result", "0");
                                    }

                                }else{
                                    res.put("result", "-4");//门禁状态不正常
                                }
                            }else{

                                res.put("result", "-5");//门禁不存在
                            }
                        }

                        //然后发短信
                        String name="";
                        if("1".equals(jsonMember.getJSONObject("data").getString("sex"))){
                            name=jsonMember.getJSONObject("data").getString("name")+"先生";
                        }
                        else if("2".equals(jsonMember.getJSONObject("data").getString("sex"))){
                            name=jsonMember.getJSONObject("data").getString("name")+"女士";
                        }
                        else{
                            name=jsonMember.getJSONObject("data").getString("name")+"先生/女士";
                        }
                        String time=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                        String commId=jsonDoor.getJSONObject("data").getString("commId");
                        System.out.println("####@@@@@SS  "+commId);
                        Map<String,String> warehousePam = new HashMap<String,String>();
                        warehousePam.put("wahoId",commId);
                        JSONObject jsonWarehouse = ApiUtils.excutePost(baseUrlStorage + "Warehouse/getWarehouseInfo", null, null, warehousePam);
                        String whcName=jsonWarehouse.getJSONObject("data").getJSONObject("Warehouse").getString("name");
                        System.out.println("门店名称："+whcName);
                        //modify by fjc 20161011 reason：将万户仓的服务电话从配置文件中取
                        String Custservice400Tel=ZkPropertyUtil.get("Custservice400Tel");
                        String context="尊敬的XX先生/女士，您于XX年XX月XX日XX时（时间）扫码进入XX门店，欢迎回到万户仓。如非本人操作，请联系客服Custservice400Tel。";
                        context=context.replace("Custservice400Tel", Custservice400Tel);
                        context=context.replace("XX先生/女士", name);
                        context=context.replace("XX年XX月XX日XX时（时间）", time);
                        context=context.replace("XX门店", whcName);
                        String tel=jsonMember.getJSONObject("data").getString("tel");
                        System.out.println("******"+context);
                        System.out.println("&&&&"+name);

                        SMSUtils.sendSMS(tel, context);
                        WeixinMessageUtil.AysncSendSmsLog(tel,context,"1");
                        log.info("扫码进门客户接收短信："+context);
                    }else{//如果member为空 即之前版本判断-1
                        res.put("result", "-3");//门禁不存在
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


        }else{
            res = jsonObject;
        }
        return null;
    }
}
