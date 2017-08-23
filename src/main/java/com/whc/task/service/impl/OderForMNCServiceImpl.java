package com.whc.task.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wanhuchina.common.util.http.ApiUtils;
import com.wanhuchina.common.util.zk.ZkPropertyUtil;
import com.whc.wx.web.util.SMSUtils;
import org.springframework.stereotype.Service;
import com.whc.task.service.IOrderForMNCService;
import org.springframework.util.StringUtils;
import org.wss.utils.WeixinMessageUtil;

@Service("com.whc.task.service.IOrderForMNCService")
public class OderForMNCServiceImpl implements IOrderForMNCService {

  public void checkOrderStatus()
  {
    String CustserviceMNCTel= ZkPropertyUtil.get("CustserviceMNCTel");
    JSONObject res = new JSONObject();
    Map<String,String> resultMap = new HashMap<String, String>();
    resultMap.put("status","1");
    JSONObject resultJson = new JSONObject();
    String memberURL = ZkPropertyUtil.get("BaseUrlMember")+"orderManage/getOrderStorageByStatus";
    try {
        resultJson = ApiUtils.excutePost(memberURL,null,null,resultMap);
          if(resultJson.getIntValue("code")==10000){
              JSONArray resultList = resultJson.getJSONArray("data");
              if(resultList==null || resultList.size()==0){
                  res.put("result", "-1");
              }else{
                  StringBuilder orderIdStringBuilder2 = new StringBuilder("");
                  for (int i = 0; i < resultList.size(); ++i) {
                      Map map = (Map)resultList.get(i);
                      if((String)map.get("remian")==null){
                          break;
                      }
                      int remian = Integer.parseInt((String)map.get("remian"));
                      String orderId = (String)map.get("id");
                      String memberId = (String)map.get("memberId");
                      JSONObject orderJson = new JSONObject();
                      Map<String,String> orderMap = new HashMap<String, String>();
                      orderMap.put("orderId",orderId);
                      String orderURL = ZkPropertyUtil.get("BaseUrlMember")+"orderManage/getOrderById";
                      orderJson = ApiUtils.excutePost(orderURL,null,null,orderMap);
                      if(orderJson.getIntValue("code")==10000){
                          JSONObject order = orderJson.getJSONObject("data").getJSONObject("Order");
                          JSONObject memberJson = new JSONObject();
                          Map<String,String> memberMap = new HashMap<String, String>();
                          memberMap.put("memberId",memberId);
                          String member1URL = ZkPropertyUtil.get("BaseUrlMember")+"memberManage/getMemberByMemId";
                          memberJson = ApiUtils.excutePost(member1URL,null,null,memberMap);
                          if(memberJson.getIntValue("code")==10000){
                              JSONObject member = memberJson.getJSONObject("data");
                              Map<String,String> waceMap =new HashMap<String, String>();
                              waceMap.put("wahoceId",order.getString("wahoceId"));
                              String waceURL = ZkPropertyUtil.get("BaseUrlStorage")+"Warehouse/getWarehouseCell";
                              JSONObject wacejson = ApiUtils.excutePost(waceURL,null,null,waceMap);
                              if(wacejson.getIntValue("code")==10000){
                                  JSONObject warehouseCell = wacejson.getJSONObject("data").getJSONObject("warehouseCell");
                                  Map<String,String> warehouseMap = new HashMap<String, String>();
                                  warehouseMap.put("wahoId",warehouseCell.getString("wahoId"));
                                  String warehouseURL = ZkPropertyUtil.get("BaseUrlStorage")+"Warehouse/getWarehouseInfo";
                                  JSONObject warehousejson = ApiUtils.excutePost(warehouseURL,null,null,warehouseMap);
                                  if (warehousejson.getIntValue("code")==10000){
                                      JSONObject _warehouse = warehousejson.getJSONObject("data").getJSONObject("Warehouse");
                                      String personId = _warehouse.getString("persId");
                                      if (remian == 0) {
                                          orderIdStringBuilder2.append((String)map.get("id"));
                                          Map<String,String> updwaceMap = new HashMap<String, String>();
                                          updwaceMap.put("status","0");
                                          updwaceMap.put("wahoceId",warehouseCell.getString("id"));
                                          String updwaceURL = ZkPropertyUtil.get("BaseUrlStorage")+"Warehouse/updateWarehouseCellStatus";
                                          JSONObject upWaHoCelljson = ApiUtils.excutePost(updwaceURL,null,null,updwaceMap);
                                          if(upWaHoCelljson.getIntValue("code")==10000){
                                              String context = "您好！客户:XX先生/女士，在万户仓XX店的XX号已到期，电话是XXTEL.请您及时联系客户办理续仓手续。";
                                              String name = "";
                                              if ("1".equals(member.getString("sex"))) {
                                                  name = order.getString("custName") + "先生";
                                              }
                                              else if ("2".equals(member.getString("sex"))) {
                                                  name = order.getString("custName") + "女士";
                                              }
                                              else {
                                                  name = order.getString("custName") + "先生/女士";
                                              }
                                              String warehouseName = _warehouse.getString("name");
                                              String whCell = warehouseCell.getString("number") + "仓位";
                                              context = context.replace("XX先生/女士", name);
                                              context = context.replace("XX店", warehouseName);
                                              context = context.replace("XX号", whCell);
                                              String tel = order.getString("custTel");
                                              context = context.replace("XXTEL", tel);
                                              //SMSUtils.sendSMS(CustserviceMNCTel, context);
                                              String context2 = "尊敬的XX先生/女士，您在万户仓XX店XX号已到期，请及时办理续仓手续。";
                                              context2 = context2.replace("XX先生/女士", name);
                                              context2 = context2.replace("XX店", warehouseName);
                                              context2 = context2.replace("XX号", whCell);
                                              tel = order.getString("custTel");
                                              SMSUtils.sendSMS(tel, context2);
                                              WeixinMessageUtil.AysncSendSmsLog(tel,context2,"1");
                                          }else{
                                              System.out.println("---------Warehouse/updateWarehouseCellStatus--------:"+upWaHoCelljson.getIntValue("code"));
                                          }

                                      }else if (remian == 7) {
                                          String context = "尊敬的XX先生/女士，您在万户仓XX店XX号还有7天到期，请及时办理续仓手续。";
                                          String name = "";
                                          if ("1".equals(member.getString("sex"))) {
                                              name = order.getString("custName") + "先生";
                                          }
                                          else if ("2".equals(member.getString("sex"))) {
                                              name = order.getString("custName") + "女士";
                                          }
                                          else {
                                              name = order.getString("custName") + "先生/女士";
                                          }
                                          String warehouseName = _warehouse.getString("name");
                                          String whCell = warehouseCell.getString("number") + "仓位";
                                          context = context.replace("XX先生/女士", name);
                                          context = context.replace("XX店", warehouseName);
                                          context = context.replace("XX号", whCell);
                                          String tel = order.getString("custTel");
                                          if (StringUtils.isEmpty(tel)) {
                                              SMSUtils.sendSMS(order.getString("custTel"), context);
                                              WeixinMessageUtil.AysncSendSmsLog(order.getString("custTel"),context,"1");
                                          }

                                          String context2 = "您好！尊敬的XX先生/女士，在万户仓XX店XX号还有7天到期，客户电话是：XX电话。请您及时联系客户办理续仓手续。";
                                          context2 = context2.replace("XX先生/女士", name);
                                          context2 = context2.replace("XX店", warehouseName);
                                          context2 = context2.replace("XX号", whCell);
                                          context2 = context2.replace("XX电话", tel);
                                          //SMSUtils.sendSMS(CustserviceMNCTel, context2);
                                      } else if (remian == 1) {
                                          String context = "尊敬的XX先生/女士，您在万户仓XX店XX号还有1天到期，请您及时办理续仓手续。";
                                          String name = "";
                                          if ("1".equals(member.getString("sex"))) {
                                              name = order.getString("custName") + "先生";
                                          }
                                          else if ("2".equals(member.getString("sex"))) {
                                              name = order.getString("custName") + "女士";
                                          }
                                          else {
                                              name = order.getString("custName") + "先生/女士";
                                          }
                                          String warehouseName = _warehouse.getString("name");
                                          String whCell = warehouseCell.getString("number") + "仓位";
                                          context = context.replace("XX先生/女士", name);
                                          context = context.replace("XX店", warehouseName);
                                          context = context.replace("XX号", whCell);
                                          String tel = order.getString("custTel");
                                          if (StringUtils.isEmpty(tel)) {
                                              SMSUtils.sendSMS(order.getString("custTel"), context);
                                              WeixinMessageUtil.AysncSendSmsLog(order.getString("custTel"),context,"1");
                                          }
                                          String context2 = "您好！尊敬的XX先生/女士，在万户仓XX店XX号还有1天到期，电话是：XX电话。请您及时联系客户办理续仓手续。";
                                          context2 = context2.replace("XX先生/女士", name);
                                          context2 = context2.replace("XX店", warehouseName);
                                          context2 = context2.replace("XX号", whCell);
                                          context2 = context2.replace("XX电话", tel);
                                          //SMSUtils.sendSMS(CustserviceMNCTel, context2);
                                      }




                                  }else{
                                      System.out.println("------------Warehouse/getWarehouseInfo-------------:"+warehousejson.getIntValue("code"));
                                  }
                              }else{
                                  System.out.println("--------------Warehouse/getWarehouseCell---------:"+wacejson.getIntValue("code"));
                              }
                          }else{
                              System.out.println("-------------------memberManage/getMemberByMemId-----------------:"+memberJson.getIntValue("code"));
                          }
                      }else{
                          System.out.println("--------------orderManage/getOrderById----------------："+orderJson.getIntValue("code"));
                      }


                  }

                  System.out.println(orderIdStringBuilder2.toString());
              }
          }else{
              System.out.println("-----------memberManage/getMember-----------");
          }
    } catch (Exception e) {
        e.printStackTrace();
    }
  }
}