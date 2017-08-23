package com.whc.wx.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Strings;
import com.wanhuchina.common.util.http.ApiUtils;
import com.wanhuchina.common.util.weixin.cgi.WeixinUtil;
import com.wanhuchina.common.util.zk.ZkPropertyUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


import org.wss.weixin.common.msg.MessageUtil;
import com.whc.wx.service.IWeixinBizService;

@Service("iWeixinBizService")
public abstract class WeixinBizServiceImpl implements IWeixinBizService {
  private static final Logger logger= LoggerFactory.getLogger(WeixinBizServiceImpl.class);

  public String weixinExecute(String msg)
  {
    return execute(msg);
  }

  public String execute(String request){
    StringBuffer responseText = new StringBuffer();
    try {
      Map requestMap = MessageUtil.parseXml(request);

      String msgType = (String)requestMap.get("MsgType");

      if (msgType.equals("text")) {
        responseTextMessage(requestMap, responseText);
      }
      else if (msgType.equals("image")) {
        responseImageMessage(requestMap, responseText);
      }
      else if (msgType.equals("voice")) {
        responseVoiceMessage(requestMap, responseText);
      }
      else if (msgType.equals("video")) {
        responseVideoMessage(requestMap, responseText);
      }
      else if (msgType.equals("shortvideo")) {
        responseShortVideoMessage(requestMap, responseText);
      }
      else if (msgType.equals("LOCATION")) {
        responseLocationMessage(requestMap, responseText);
      }
      else if (msgType.equals("link")) {
        responseLinkMessage(requestMap, responseText);
      }
      else if (msgType.equals("event"))
      {
        String eventType = (String)requestMap.get("Event");

        if (eventType.equals("subscribe")) {
          responseSubscribeEvent(requestMap, responseText);
        }
        else if (eventType.equals("unsubscribe"))
        {
          responseUnsubscribeEvent(requestMap, responseText);
        }
        else if (eventType.equals("SCAN")) {
          responseScanEvent(requestMap, responseText);
        }
        else if (eventType.equals("LOCATION")) {
          responseLocationEvent(requestMap, responseText);
        }
        else if (eventType.equals("enter_agent")) {
          responseEnterAgentEvent(requestMap, responseText);
        }
        else if (eventType.equals("batch_job_result")) {
          responseBatchJobResultEvent(requestMap, responseText);
        }
        else if (eventType.equals("CLICK"))
          responseClickEvent(requestMap, responseText);
        else if (eventType.equals("VIEW"))
          responseViewEvent(requestMap, responseText);
        else if (eventType.equals("scancode_push"))
          responseScanCodePushEvent(requestMap, responseText);
        else if (eventType.equals("scancode_waitmsg"))
          responseScanCodeWaitMsgEvent(requestMap, responseText);
        else if (eventType.equals("pic_photo_or_album"))
          responsePicPhotoOrAlbumEvent(requestMap, responseText);
        else if (eventType.equals("pic_sysphoto"))
          responsePicSysPhotoEvent(requestMap, responseText);
        else if (eventType.equals("pic_weixin"))
          responsePicWeixinEvent(requestMap, responseText);
        else if (eventType.equals("location_select"))
          responseLocationSelectEvent(requestMap, responseText);
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return responseText.toString();
  }

protected void responseTextMessage(Map<String, String> requestMap, StringBuffer responseText)
  {
    logger.info("------------微信自动回复-------------");
    String fromUserName = (String)requestMap.get("FromUserName");
    String toUserName = (String)requestMap.get("ToUserName");
    String content = (String)requestMap.get("Content");
    StringBuffer buffer = new StringBuffer();
    logger.info("------------content-------------"+content);
    if(content.equals("1")){
    	 buffer.append("使用微信“扫一扫”扫描柜体二维码\n");
    	 buffer.append("然后选择你要存的柜号\n");
    	 buffer.append("填写订单并支付\n");
    	 buffer.append("完成支付柜门就会自动打开了");
    }else if(content.equals("2")){
   	 	 buffer.append("使用微信“扫一扫”扫描柜体二维码\n");
   	 	 buffer.append("然后点击当前订单“开锁”按钮\n");
   	 	 buffer.append("温馨提示：\n");
   	 	 buffer.append("避免继续计费，不再使用时记得结束订单哦");
    }else if(content.equals("3")){
    	 buffer.append("首先，订单超时柜门是不会自动打开的哦\n");
  	 	 buffer.append("但是“开锁”按钮会变成“续费”\n");
  	 	 buffer.append("您需要续费后“开锁”按钮才可用");
    }else if(content.equals("4")){
    	 buffer.append("点击底部菜单“订单中心”\n");
  	 	 buffer.append("进行中的订单可以点击“续租”\n");
  	 	 buffer.append("在订单续租界面选择续费时长\n");
  	 	 buffer.append("支付完成就可以延长使用时间了");
    }else if(content.equals("5")){
    	 buffer.append("点击底部菜单“门店列表”\n");
 	 	 buffer.append("您可以通过搜索查询门店\n");
 	 	 buffer.append("也可以看到当前离您最近的门店\n");
 	 	 buffer.append("如果您当前所在城市还没有，也可以告诉我们，我们会尽快把更加智能的储物服务带到您的身边");
    }else if(content.equals("6")){
 	 	 buffer.append("感谢您关注万户仓，国内领先的智能储物运营平台\n");
 	 	 buffer.append("商务合作请拨打：400-0027-287");
    }else{
    	 buffer.append("欢迎使用万户仓智能储物柜，回复数字，看看有什么可以快速帮到您的:\n");
    	 buffer.append("【1】怎么订柜\n");
  	 	 buffer.append("【2】如何开柜\n");
  	 	 buffer.append("【3】使用超时了怎么办\n");
  	 	 buffer.append("【4】想要续租\n");
  	 	 buffer.append("【5】有哪些门店\n");
  	 	 buffer.append("【6】了解合作\n");
  	 	 buffer.append(" 如果问题还未解决，您可以拨打:400-0027-287寻求帮助哦。");
    }
    //String respContent = "欢迎您来到万户仓，万户君温馨提示您如需要帮助，请拨打客服电话：400-0027-287。";
    String resXml = null;
    resXml = "<xml><ToUserName><![CDATA[" + 
      fromUserName + "]]></ToUserName>" + 
      "<FromUserName><![CDATA[" + toUserName + "]]></FromUserName>" + 
      "<CreateTime>" + new Date().getTime() + "</CreateTime>" + 
      "<MsgType><![CDATA[text]]></MsgType>" + 
      "<Content><![CDATA[" + buffer.toString() + "]]></Content>" + 
      "</xml>";
    logger.info("------------resXml:-------------"+resXml);
    responseText.append(resXml);
    doResponseTextMessage(requestMap, responseText);
  }

  protected void responseImageMessage(Map<String, String> requestMap, StringBuffer responseText) {
    doResponseImageMessage(requestMap, responseText);
  }
  protected void responseVoiceMessage(Map<String, String> requestMap, StringBuffer responseText) {
    doResponseVoiceMessage(requestMap, responseText);
  }
  protected void responseVideoMessage(Map<String, String> requestMap, StringBuffer responseText) {
    doResponseVideoMessage(requestMap, responseText);
  }
  protected void responseShortVideoMessage(Map<String, String> requestMap, StringBuffer responseText) {
    doResponseShortVideoMessage(requestMap, responseText);
  }
  protected void responseLocationMessage(Map<String, String> requestMap, StringBuffer responseText) {
    doResponseLocationMessage(requestMap, responseText);
  }
  protected void responseLinkMessage(Map<String, String> requestMap, StringBuffer responseText) {
    doResponseLinkMessage(requestMap, responseText);
  }

  /**
   * 进入关注
   * @param requestMap
   * @param responseText
     */
  protected void responseSubscribeEvent(Map<String, String> requestMap, StringBuffer responseText) {
    logger.info("------------微信关注-------------");
	//进入关注
	String fromUserName = (String)requestMap.get("FromUserName");

    String toUserName = (String)requestMap.get("ToUserName");

    String msgType = (String)requestMap.get("MsgType");

    String content = (String)requestMap.get("Content");
    String personId = "";
    String wahoId = "";
    try {
      String eventKey = (String)requestMap.get("EventKey");
      if (StringUtils.isNotEmpty(eventKey)) {
        System.out.println("++++++++" + eventKey.toString());
        String[] strs = eventKey.split("_");

        if ("qrscene".equals(strs[0])) {
          if ((strs.length == 3) && (strs[2].equals("mdId")))
            wahoId = strs[1];
          else {
            personId = eventKey.split("_")[1];
          }
        }
      }
      /**
       * fromUserName is openid
       */
      Map<String,String> memberMap = new HashMap<String, String>();
      memberMap.put("openId",fromUserName);
      JSONObject memberjson = new JSONObject();
      String BaseURL = ZkPropertyUtil.get("BaseUrlMember")+"memberManage/getMemberByOpenId";
      memberjson = ApiUtils.excutePost(BaseURL,null,null,memberMap);
      logger.info("------------memberjson:-------------"+memberjson.toString());
      if(memberjson.getIntValue("code")==10000){
        JSONObject datajson = memberjson.getJSONObject("data");
        String s = datajson.toString();
        logger.info("------------datajson:-------------"+datajson.toString());
        //member为空 首次关注
        if("{}".equals(s)){
          logger.info("------------首次关注:-------------");
          Map<String,String> memberSaveMap = new HashMap<String, String>();
          memberSaveMap.put("openId",fromUserName);
          memberSaveMap.put("isdel","0");
          memberSaveMap.put("regdate",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
          Map userInfo = WeixinUtil.getMemberInfoByOpenId(ZkPropertyUtil.get("corpId"), ZkPropertyUtil.get("secret"), fromUserName);
          logger.info("------------首次关注:-------------"+userInfo.toString());
          String nickName = "";
          String headimgurl = "";
          String sex = "";
          if (userInfo != null) {
            if (StringUtils.isNotEmpty((String)userInfo.get("nickname"))){
              nickName = (String)userInfo.get("nickname");
            }
            if (StringUtils.isNotEmpty((String)userInfo.get("headimgurl"))) {
              headimgurl = (String)userInfo.get("headimgurl");
            }
            if (StringUtils.isNotEmpty((String)userInfo.get("sex"))) {
              sex = (String)userInfo.get("sex");
            }
          }
          memberSaveMap.put("nickName",nickName);
          memberSaveMap.put("name",nickName);
          memberSaveMap.put("photo",headimgurl);
          memberSaveMap.put("sex",sex);
          memberSaveMap.put("weigateStatus","0");
          memberSaveMap.put("source",personId);
          memberSaveMap.put("isSub","1");
          memberSaveMap.put("wahoId",wahoId);
          memberSaveMap.put("source",personId);
          //首次关注保存
          String member = JSON.toJSONString(memberSaveMap);
          System.out.println("wang+member="+member);
          String insMemberBaseURL = ZkPropertyUtil.get("BaseUrlMember")+"memberManage/insMember";
          System.out.println("wang+insMemberBaseURL="+insMemberBaseURL);
          JSONObject insMemberObject = ApiUtils.excutePost(insMemberBaseURL,null,member,null);
          logger.info("------------首次关注:-------------"+insMemberObject.toString());
        }else{
          logger.info("------------再次关注:-------------");
          //再次关注
          Map userInfo = WeixinUtil.getMemberInfoByOpenId(ZkPropertyUtil.get("corpId"),ZkPropertyUtil.get("secret"), fromUserName);
          logger.info("------------再次关注:-----userInfo:--------"+userInfo.toString());
          String nickName = "";
          String headimgurl = "";
          String sex = "";
          if (userInfo != null) {
            if (StringUtils.isNotEmpty((String)userInfo.get("nickname"))) {
              nickName = (String)userInfo.get("nickname");
            }
            if (StringUtils.isNotEmpty((String)userInfo.get("headimgurl"))) {
              headimgurl = (String)userInfo.get("headimgurl");
            }
            if (StringUtils.isNotEmpty((String)userInfo.get("sex"))) {
              sex = (String)userInfo.get("sex");
            }
          }
          Map<String,String> againMeberMap = new HashMap<String, String>();
          //查询member.getWahoId()

          if(!Strings.isNullOrEmpty(datajson.getString("wahoId"))){
            againMeberMap.put("wahoId",wahoId);
          }
          againMeberMap.put("nickName",nickName);
          againMeberMap.put("isdel","0");
          againMeberMap.put("isSub","1");
          againMeberMap.put("photo",headimgurl);
          againMeberMap.put("sex",sex);

          againMeberMap.put("id",datajson.getString("id"));//根据memberid修改
          againMeberMap.put("againTime",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
          //修改member
          String member = JSON.toJSONString(againMeberMap);
          String upMemberBaseURL = ZkPropertyUtil.get("BaseUrlMember")+"memberManage/updMember";
          JSONObject upMemberObject = ApiUtils.excutePost(upMemberBaseURL,null,member,null);
          logger.info("------------再次关注:-------upMemberObject------"+upMemberObject.toString());
        }
      }else{
          logger.info("------------微信关注请求member失败--memberManage/getMemberByOpenId----------");
     }
    } catch (Exception e) {
      e.printStackTrace();
    }
    StringBuffer buffer = new StringBuffer();
 //  buffer.append("存储物，找万户！").append("\n");
 //  buffer.append("官网").append("\n");
 //  buffer.append("www.wanhuchina.com").append("\n");
 //  buffer.append("客服热线").append("\n");
 //  buffer.append("400-0027-287").append("\n").append("\n");
 //  buffer.append("【万户仓】— 自助迷你仓：便捷存储、空间释放、优质生活。").append("\n");
 //  buffer.append("【羊舍】— 智能储物柜：作为万户仓旗下全新独立品牌，旨在追求方便、快捷、打造互联网时代的仓储新生态。").append("\n").append("\n");
    
    buffer.append(" 存储物，找万户！ \n");
    buffer.append(" 便捷、智能、安全，释放双手享受“自由体验”！\n\n");

    buffer.append(" 热线 400-0027-287").append("\n");


    Map<String,String> warehouseMap = new HashMap<String, String>();
    warehouseMap.put("wahoId",wahoId);
    String warehouseURL = ZkPropertyUtil.get("BaseUrlStorage")+"Warehouse/getWarehouseInfo";
    try {
      JSONObject warehouseObject = ApiUtils.excutePost(warehouseURL,null,null,warehouseMap);
      String warehouseName = "";
      if(warehouseObject.getIntValue("code")==10000){
        JSONObject wareDataJson = warehouseObject.getJSONObject("data");
        if(wareDataJson.getJSONObject("Warehouse")!=null){
          buffer.append(String.valueOf(Character.toChars(0x1F447)))
                  .append("点此存包")
                  .append(String.valueOf(Character.toChars(0x1F447)))
                  .append("点此存包")
                  .append(String.valueOf(Character.toChars(0x1F447)))
                  .append("\n");
          String baseURL = ZkPropertyUtil.get("baseURL");
          warehouseName = wareDataJson.getJSONObject("Warehouse").getString("name");
          String url = "";
          if ("0".equals(wareDataJson.getJSONObject("Warehouse").getString("type"))) {
            url = baseURL + "web/whc/warehouse/selectWarehouseCell.jsp?openId=" + fromUserName + "&warehouseId=" + wahoId;
            buffer.append(String.valueOf(Character.toChars(0x1F449))).append("<a href=\"" + url + "\">" + warehouseName + "</a>").append(String.valueOf(Character.toChars(0x1F448)));	// 20170221 added by zms; Emoji表情代码表  0x1F449 手指右指
          } else {
            //url = baseURL + "app/whc/warehouse/selectIntelligentWarehouseCell.jsp?openId=" + fromUserName + "&warehouseId=" + wahoId;
            url = baseURL + "web/whc/warehouse/cabinetSelect.html?openId=" + fromUserName + "&warehouseId=" + wahoId;
            buffer.append(String.valueOf(Character.toChars(0x1F449))).append("<a href=\"" + url + "\">" + warehouseName + "</a>").append(String.valueOf(Character.toChars(0x1F448)));
          }
        }
        String resXml = null;
        resXml = "<xml><ToUserName><![CDATA[" +
                fromUserName + "]]></ToUserName>" +
                "<FromUserName><![CDATA[" + toUserName + "]]></FromUserName>" +
                "<CreateTime>" + new Date().getTime() + "</CreateTime>" +
                "<MsgType><![CDATA[text]]></MsgType>" +
                "<Content><![CDATA[" + buffer.toString() + "]]></Content>" +
                "</xml>";
        logger.info("------------微信关注:-------resXml------"+resXml);
        responseText.append(resXml);
        doResponseSubscribeEvent(requestMap, responseText);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  protected void responseUnsubscribeEvent(Map<String, String> requestMap, StringBuffer responseText) {
    logger.info("------------取消关注:-------------");
    String fromUserName = (String)requestMap.get("FromUserName");

    String toUserName = (String)requestMap.get("ToUserName");

    String msgType = (String)requestMap.get("MsgType");

    String content = (String)requestMap.get("Content");
  /**
   * fromUserName is openid
   */
    Map<String,String> memberMap = new HashMap<String, String>();
    memberMap.put("openId",fromUserName);
    memberMap.put("isdel","1");
    memberMap.put("isSub","0");
    String member = JSON.toJSONString(memberMap);
    JSONObject memberjson = new JSONObject();
    String BaseURL = ZkPropertyUtil.get("BaseUrlMember")+"memberManage/updMemberByOpenId";
    try {
      memberjson = ApiUtils.excutePost(BaseURL,null,member,null);
      logger.info("------------memberjson:-------------"+memberjson.toString());
      if(memberjson.getIntValue("code")==10000){
        logger.info("------------取消关注成功:-------------");
      }else{
        logger.info("------------取消关注失败:-------------");
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    doResponseUnsubscribeEvent(requestMap, responseText);
  }
  protected void responseScanEvent(Map<String, String> requestMap, StringBuffer responseText) {
	  doResponseScanEvent(requestMap, responseText);
  }
  protected void responseLocationEvent(Map<String, String> requestMap, StringBuffer responseText) {
    doResponseLocationEvent(requestMap, responseText);
  }
  protected void responseEnterAgentEvent(Map<String, String> requestMap, StringBuffer responseText) {
    doResponseEnterAgentEvent(requestMap, responseText);
  }
  protected void responseBatchJobResultEvent(Map<String, String> requestMap, StringBuffer responseText) {
    doResponseBatchJobResultEvent(requestMap, responseText);
  }
  protected void responseClickEvent(Map<String, String> requestMap, StringBuffer responseText) {
    String fromUserName = (String)requestMap.get("FromUserName");

    String toUserName = (String)requestMap.get("ToUserName");

    String msgType = (String)requestMap.get("MsgType");

    String content = (String)requestMap.get("Content");

    String eventKey = (String)requestMap.get("EventKey");
    String res = "";
    if ("recent news".equals(eventKey)) {
      res = "<xml><ToUserName><![CDATA[" + 
        fromUserName + "]]></ToUserName>" + 
        "<FromUserName><![CDATA[" + toUserName + "]]></FromUserName>" + 
        "<CreateTime>" + new Date().getTime() + "</CreateTime>" + 
        "<MsgType><![CDATA[news]]></MsgType>" + 
        "<ArticleCount>5</ArticleCount>" + 
        "<Articles>" + 
        "<item>" + 
        "<Title><![CDATA[哎呦！不错！！]]></Title> " + 
        "<Description><![CDATA[]]></Description>" + 
        "<PicUrl><![CDATA[http://whc.wanhuchina.com/app/whc/images/zhongqiu.jpg]]></PicUrl>" + 
        "<Url><![CDATA[http://mp.weixin.qq.com/s?__biz=MzAxMjUxNTgyMA==&mid=207892049&idx=1&sn=bc8d5fec7e2788bc2ead11487155a455&scene=0#rd]]></Url></item>" + 
        "<item>" + 
        "<Title><![CDATA[欢迎外国朋友入驻万户仓【仓品故事】]]></Title> " + 
        "<Description><![CDATA[]]></Description>" + 
        "<PicUrl><![CDATA[http://whc.wanhuchina.com/app/whc/images/for.jpg]]></PicUrl>" + 
        "<Url><![CDATA[http://mp.weixin.qq.com/s?__biz=MzAxMjUxNTgyMA==&mid=207892049&idx=2&sn=8251bcbfbe920a18966a3a755126c28b&scene=0#rd]]></Url></item>" + 
        "<item>" + 
        "<Title><![CDATA[让我们一起记录最美的笑容【仓友会】]]></Title> " + 
        "<Description><![CDATA[]]></Description>" + 
        "<PicUrl><![CDATA[http://whc.wanhuchina.com/app/whc/images/smail.jpg]]></PicUrl>" + 
        "<Url><![CDATA[http://mp.weixin.qq.com/s?__biz=MzAxMjUxNTgyMA==&mid=207866281&idx=3&sn=2bc96bec046e2aa8cb7ee43a0de77133#rd]]></Url></item>" + 
        "<item>" + 
        "<Title><![CDATA[首城的您，我们让您了解了多少？【最新动态】]]></Title> " + 
        "<Description><![CDATA[]]></Description>" + 
        "<PicUrl><![CDATA[http://whc.wanhuchina.com/app/whc/images/shou.jpg]]></PicUrl>" + 
        "<Url><![CDATA[http://mp.weixin.qq.com/s?__biz=MzAxMjUxNTgyMA==&mid=207866281&idx=4&sn=8cbc3e5a8a779fb345b9e30b36625e63#rd]]></Url></item>" + 
        "<item>" + 
        "<Title><![CDATA[万户仓自助式仓储服务需求调查问卷]]></Title> " + 
        "<Description><![CDATA[]]></Description>" + 
        "<PicUrl><![CDATA[http://whc.wanhuchina.com/app/whc/images/juan.jpg]]></PicUrl>" + 
        "<Url><![CDATA[http://mp.weixin.qq.com/s?__biz=MzAxMjUxNTgyMA==&mid=207866281&idx=5&sn=bf2d92a2d2821e545d2de15f9fc401f0#rd]]></Url></item>" + 
        "</Articles>" + 
        "</xml> ";
    }
    if ("stores information".equals(eventKey)) {
      res = 
        "<xml><ToUserName><![CDATA[" + 
        fromUserName + "]]></ToUserName>" + 
        "<FromUserName><![CDATA[" + toUserName + "]]></FromUserName>" + 
        "<CreateTime>" + new Date().getTime() + "</CreateTime>" + 
        "<MsgType><![CDATA[news]]></MsgType>" + 
        "<ArticleCount>1</ArticleCount>" + 
        "<Articles>" + 
        "<item>" + 
        "<Title><![CDATA[仓有惠]]></Title> " + 
        "<Description><![CDATA[]]></Description>" + 
        "<PicUrl><![CDATA[http://whc.wanhuchina.com/app/whc/images/introduce.jpg]]></PicUrl>" + 
        "<Url><![CDATA[http://mp.weixin.qq.com/s?__biz=MzAxMjUxNTgyMA==&mid=207573569&idx=1&sn=65fb89aff7c2b6a0c14b5d1e204811db#rd]]></Url></item>" + 
        "</Articles>" + 
        "</xml> ";
    }
    if ("introduce".equals(eventKey)) {
      res = "<xml><ToUserName><![CDATA[" + 
        fromUserName + "]]></ToUserName>" + 
        "<FromUserName><![CDATA[" + toUserName + "]]></FromUserName>" + 
        "<CreateTime>" + new Date().getTime() + "</CreateTime>" + 
        "<MsgType><![CDATA[news]]></MsgType>" + 
        "<ArticleCount>2</ArticleCount>" + 
        "<Articles>" + 
        "<item>" + 
        "<Title><![CDATA[【仓友会】万户周刊开始公开征稿了！快说出您与仓品的故事~]]></Title> " + 
        "<Description><![CDATA[]]></Description>" + 
        "<PicUrl><![CDATA[http://whc.wanhuchina.com/app/whc/images/zg.jpg]]></PicUrl>" + 
        "<Url><![CDATA[http://mp.weixin.qq.com/s?__biz=MzAxMjUxNTgyMA==&mid=207573569&idx=3&sn=ad887d365ffcc8583faf802e30e28287#rd]]></Url></item>" + 
        "<item>" + 
        "<Title><![CDATA[【仓友会】又一位妈妈选择了万户仓~ ]]></Title> " + 
        "<Description><![CDATA[]]></Description>" + 
        "<PicUrl><![CDATA[http://whc.wanhuchina.com/app/whc/images/2.jpg]]></PicUrl>" + 
        "<Url><![CDATA[http://mp.weixin.qq.com/s?__biz=MzAxMjUxNTgyMA==&mid=207427302&idx=2&sn=fd9311ba97496d4e10c02261e89976d8#rd]]></Url></item>" + 
        "</Articles>" + 
        "</xml> ";
    }
    if ("YOU SHOULD".equals(eventKey)) {
      res = "<xml><ToUserName><![CDATA[" + 
        fromUserName + "]]></ToUserName>" + 
        "<FromUserName><![CDATA[" + toUserName + "]]></FromUserName>" + 
        "<CreateTime>" + new Date().getTime() + "</CreateTime>" + 
        "<MsgType><![CDATA[news]]></MsgType>" + 
        "<ArticleCount>1</ArticleCount>" + 
        "<Articles>" + 
        "<item>" + 
        "<Title><![CDATA[你该有个仓]]></Title> " + 
        "<Description><![CDATA[]]></Description>" + 
        "<PicUrl><![CDATA[http://whc.wanhuchina.com/app/whc/images/youshould.jpg]]></PicUrl>" + 
        "<Url><![CDATA[http://mp.weixin.qq.com/s?__biz=MzAxMjUxNTgyMA==&mid=206970950&idx=1&sn=8838f0f31e74315cde2759c63e1e2af3#rd]]></Url></item>" + 
        "</Articles>" + 
        "</xml> ";
    }
    responseText.append(res);
    doResponseClickEvent(requestMap, responseText);
  }
  protected void responseViewEvent(Map<String, String> requestMap, StringBuffer responseText) {
    doResponseViewEvent(requestMap, responseText);
  }
  protected void responseScanCodePushEvent(Map<String, String> requestMap, StringBuffer responseText) {
    doResponseScanCodePushEvent(requestMap, responseText);
  }
  protected void responseScanCodeWaitMsgEvent(Map<String, String> requestMap, StringBuffer responseText) {
    doResponseScanCodeWaitMsgEvent(requestMap, responseText);
  }
  protected void responsePicSysPhotoEvent(Map<String, String> requestMap, StringBuffer responseText) {
    doResponsePicSysPhotoEvent(requestMap, responseText);
  }
  protected void responsePicPhotoOrAlbumEvent(Map<String, String> requestMap, StringBuffer responseText) {
    doResponsePicPhotoOrAlbumEvent(requestMap, responseText);
  }
  protected void responsePicWeixinEvent(Map<String, String> requestMap, StringBuffer responseText) {
    doResponsePicWeixinEvent(requestMap, responseText);
  }
  protected void responseLocationSelectEvent(Map<String, String> requestMap, StringBuffer responseText) {
    doResponseLocationSelectEvent(requestMap, responseText);
  }

//  protected IResponseService getResponseService() {
//      return (IResponseService)SpringManager.getComponent(IResponseService.RESPONSESERVICE);
//  }
  protected abstract void doResponseTextMessage(Map<String, String> paramMap, StringBuffer paramStringBuffer);

  protected abstract void doResponseImageMessage(Map<String, String> paramMap, StringBuffer paramStringBuffer);

  protected abstract void doResponseVoiceMessage(Map<String, String> paramMap, StringBuffer paramStringBuffer);

  protected abstract void doResponseVideoMessage(Map<String, String> paramMap, StringBuffer paramStringBuffer);

  protected abstract void doResponseShortVideoMessage(Map<String, String> paramMap, StringBuffer paramStringBuffer);

  protected abstract void doResponseLocationMessage(Map<String, String> paramMap, StringBuffer paramStringBuffer);

  protected abstract void doResponseLinkMessage(Map<String, String> paramMap, StringBuffer paramStringBuffer);

  protected abstract void doResponseSubscribeEvent(Map<String, String> paramMap, StringBuffer paramStringBuffer);

  protected abstract void doResponseUnsubscribeEvent(Map<String, String> paramMap, StringBuffer paramStringBuffer);

  protected abstract void doResponseScanEvent(Map<String, String> paramMap, StringBuffer paramStringBuffer);

  protected abstract void doResponseLocationEvent(Map<String, String> paramMap, StringBuffer paramStringBuffer);

  protected abstract void doResponseEnterAgentEvent(Map<String, String> paramMap, StringBuffer paramStringBuffer);

  protected abstract void doResponseBatchJobResultEvent(Map<String, String> paramMap, StringBuffer paramStringBuffer);

  protected abstract void doResponseClickEvent(Map<String, String> paramMap, StringBuffer paramStringBuffer);

  protected abstract void doResponseViewEvent(Map<String, String> paramMap, StringBuffer paramStringBuffer);

  protected abstract void doResponseScanCodePushEvent(Map<String, String> paramMap, StringBuffer paramStringBuffer);

  protected abstract void doResponseScanCodeWaitMsgEvent(Map<String, String> paramMap, StringBuffer paramStringBuffer);

  protected abstract void doResponsePicSysPhotoEvent(Map<String, String> paramMap, StringBuffer paramStringBuffer);

  protected abstract void doResponsePicPhotoOrAlbumEvent(Map<String, String> paramMap, StringBuffer paramStringBuffer);

  protected abstract void doResponsePicWeixinEvent(Map<String, String> paramMap, StringBuffer paramStringBuffer);

  protected abstract void doResponseLocationSelectEvent(Map<String, String> paramMap, StringBuffer paramStringBuffer);

//  private static IMemberService getMemberService() { return (IMemberService)SpringManager.getComponent(IMemberService.MEMBERSERVICE); }

}