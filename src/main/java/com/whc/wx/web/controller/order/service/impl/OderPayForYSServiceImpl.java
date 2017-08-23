package com.whc.wx.web.controller.order.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Strings;
import com.wanhuchina.common.util.http.ApiUtils;
import com.wanhuchina.common.util.zk.ZkPropertyUtil;
import com.whc.wx.web.controller.order.service.IOrderPayForYSService;
import org.springframework.stereotype.Service;
import com.whc.wx.web.util.SMSUtils;
import org.wss.utils.WeixinMessageUtil;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by WangShengZhan
 * Email：WangShengZhan@wanhuchina.com
 * Date：2017/6/30
 * Time：16:08
 */
@Service("iOrderPayForYSService")
public class OderPayForYSServiceImpl implements IOrderPayForYSService {
    /**
     * 微信回调修改订单状态
     * @param orderId
     * @param transactionId
     * @return
     */
    public String updateOrderStatus(String orderId, String transactionId) {
        String message = "success";
        Map<String,String> orderRenewMap = new HashMap<String, String>();
        orderRenewMap.put("accountNumber",transactionId);
        String orderRenewURL = ZkPropertyUtil.get("BaseUrlMember")+"orderManage/selOrdRenewByAccNum";
        try {
            JSONObject orderRenewObject = ApiUtils.excutePost(orderRenewURL,null,null,orderRenewMap);
            if(orderRenewObject.getIntValue("code")==10001){
                if(orderRenewObject.getString("msg").equals("未有订单")){
                    // 修改订单状态
                    Map<String,String> orderMap = new HashMap<String, String>();
                    orderMap.put("orderId",orderId);
                    String orderURL = ZkPropertyUtil.get("BaseUrlMember")+"orderManage/getOrderById";
                    JSONObject orderjson = ApiUtils.excutePost(orderURL,null,null,orderMap);
                    if(orderjson.getIntValue("code")==10000){
                        JSONObject order = orderjson.getJSONObject("data").getJSONObject("Order");
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
                            String totalTimes = "";
                            String context = "";
                            String startDate = "";
                            String endDate = "";
                            String number = "";
                            String unit = "";
                            // 改变仓位状态为使用中
                            Map<String,String> updwaceMap = new HashMap<String, String>();
                            updwaceMap.put("status","1");
                            updwaceMap.put("wahoceId",order.getString("wahoceId"));
                            String updwaceURL = ZkPropertyUtil.get("BaseUrlStorage")+"Warehouse/updateWarehouseCellStatus";

                            JSONObject upWaHoCelljson = ApiUtils.excutePost(updwaceURL,null,null,updwaceMap);
                            if(order.getString("id")!=null &&!order.getString("id").equals("")){
                                // 新增缴费记录
                                Map<String,String> insRenewMap = new HashMap<String, String>();
                                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                                insRenewMap.put("accountNumber",transactionId);
                                // 订单是续仓的支付
                                if (!Strings.isNullOrEmpty(order.getString("nextEndDate"))&& !Strings.isNullOrEmpty(order.getString("nextAmount"))) {
                                    System.out.println("---------再次缴费---------");
                                    insRenewMap.put("orderId",orderId);
                                    if("8".equals(warehouseCell.getString("type"))){
                                        number = order.getString("number");
                                        unit = order.getString("unit");
                                        insRenewMap.put("startDate",order.getString("endDate"));
                                        startDate = order.getString("endDate").substring(0, 16);
                                        endDate = order.getString("nextEndDate").substring(0,16);
                                        if("1".equals(unit)){
                                            totalTimes=number+"小时";
                                        }
                                        if("2".equals(unit)){
                                            totalTimes=number+"天";
                                        }
                                        if("3".equals(unit)){
                                            totalTimes=number+"个月";
                                        }
                                    }else if("10".equals(warehouseCell.getString("type"))){
                                        insRenewMap.put("startDate",order.getString("endDate"));
                                        startDate = order.getString("endDate").substring(0, 16);
                                        endDate = order.getString("nextEndDate").substring(0,16);
                                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                                        Date beginTime = df.parse(startDate);
                                        Date endTime = df.parse(endDate);
                                        long diff = endTime.getTime() - beginTime.getTime();
                                        long hours = diff / (1000 * 60 * 60 * 24);
                                        totalTimes = Long.toString(hours) + "天";

                                    }else{
                                        Calendar calendar = Calendar.getInstance();
                                        startDate = order.getString("endDate");
                                        endDate = order.getString("nextEndDate");
                                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                                        Date beginTime = df.parse(startDate);
                                        Date endTime = df.parse(endDate);
                                        long diff = endTime.getTime()
                                                - beginTime.getTime();
                                        long days = diff / (1000 * 60 * 60 * 24);
                                        totalTimes = Long.toString(days) + "天";

                                        try {
                                            calendar.setTime(format.parse(order.getString("endDate")));
                                            long end = calendar.getTimeInMillis() + 24 * 3600 * 1000;
                                            calendar.setTimeInMillis(end);
                                            insRenewMap.put("startDate",format.format(calendar.getTime()));
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    insRenewMap.put("endDate",order.getString("nextEndDate"));
                                    insRenewMap.put("date",format.format(new Date()));
                                    insRenewMap.put("money",order.getString("nextAmount"));
                                    insRenewMap.put("status","1");// 缴费状态：0;待缴费，1：已缴费
                                    insRenewMap.put("paymentType","2");// 0现金;1 汇款 2在线支付
                                    //保存 insRenewMap
                                    String insRenew = JSON.toJSONString(insRenewMap);
                                    String insRenewsURL = ZkPropertyUtil.get("BaseUrlMember")+"orderManage/insOrderRenew";
                                    JSONObject  renewsjson= ApiUtils.excutePost(insRenewsURL,null,insRenew,null);

                                    Map<String,String> updOrdermap = new HashMap<String, String>();
                                    updOrdermap.put("id",orderId);
                                    updOrdermap.put("endDate",order.getString("nextEndDate"));
                                    updOrdermap.put("nextEndDate","");
                                    System.out.println("id:::::"+order.getString("id"));
                                    System.out.println("totalAmount:::::"+order.getString("totalAmount"));
                                    System.out.println("nextAmount:::::"+order.getString("nextAmount"));
                                    Double newTotal = Double.valueOf(order.getString("totalAmount")) + Double.valueOf(order.getString("nextAmount"));
                                    DecimalFormat df = new DecimalFormat("######0.00");

                                    String newTotalStr = df.format(newTotal);
                                    updOrdermap.put("totalAmount",newTotalStr);
                                    updOrdermap.put("amount",newTotalStr);
                                    updOrdermap.put("number",String.valueOf(Integer.parseInt(order.getString("totalTimes"))+Integer.parseInt(order.getString("number"))));
                                    updOrdermap.put("nextAmount","");
                                    //修改 order
                                    String updOrder = JSON.toJSONString(updOrdermap);
                                    String updOrderURL = ZkPropertyUtil.get("BaseUrlMember")+"orderManage/updOrder";
                                    JSONObject  updOrderjson= ApiUtils.excutePost(updOrderURL,null,updOrder,null);
                                    context = "尊敬的" + order.getString("custName")
                                            + "先生/女士，您已成功获得" + warehousejson.getJSONObject("data").getJSONObject("Warehouse").getString("name")
                                            + warehouseCell.getString("number") + "号柜" + totalTimes
                                            + "的使用权，到期时间是" + endDate
                                            + "。";

                                }else{
                                    System.out.println("---------首次缴费---------");
                                    Map<String,String> updOrdermap = new HashMap<String, String>();
                                    updOrdermap.put("id",orderId);
                                    updOrdermap.put("status","1");// 订单状态：0 待付款(待支付) 1 已生效（已支付） 2
                                    updOrdermap.put("amount",order.getString("totalAmount"));
                                    String updOrder = JSON.toJSONString(updOrdermap);
                                    String updOrderURL = ZkPropertyUtil.get("BaseUrlMember")+"orderManage/updOrder";
                                    JSONObject  updOrderjson= ApiUtils.excutePost(updOrderURL,null,updOrder,null);
                                    System.out.println("--------updOrderjson-------"+updOrderjson.toString());
                                    // 新增缴费记录
                                    insRenewMap.put("orderId",orderId);
                                    insRenewMap.put("startDate",order.getString("startDate"));
                                    insRenewMap.put("endDate",order.getString("endDate"));
                                    insRenewMap.put("date",format.format(new Date()));
                                    insRenewMap.put("money",order.getString("totalAmount"));
                                    insRenewMap.put("status","1");// 缴费状态：0;待缴费，1：已缴费
                                    insRenewMap.put("PaymentType","2");// 0现金;1 汇款 2在线支付
                                    //保存 insRenewMap
                                    String insRenew = JSON.toJSONString(insRenewMap);
                                    String insRenewsURL = ZkPropertyUtil.get("BaseUrlMember")+"orderManage/insOrderRenew";
                                    JSONObject  renewsjson= ApiUtils.excutePost(insRenewsURL,null,insRenew,null);

                                    int password = (int) (Math.random() * 90000) + 10000;
                                    if("8".equals(warehouseCell.getString("type"))){
                                        number = order.getString("number");
                                        unit = order.getString("unit");
                                        startDate = order.getString("startDate").substring(0,16);
                                        endDate = order.getString("endDate").substring(0, 16);
                                        if("1".equals(unit)){
                                            totalTimes=number+"小时";
                                        }
                                        if("2".equals(unit)){
                                            totalTimes=number+"天";
                                        }
                                        if("3".equals(unit)){
                                            totalTimes=number+"个月";
                                        }
                                    }else if("10".equals(warehouseCell.getString("type"))){
                                        startDate = order.getString("startDate").substring(0,16);
                                        endDate = order.getString("endDate").substring(0, 16);
                                        SimpleDateFormat df = new SimpleDateFormat(
                                                "yyyy-MM-dd HH:mm");
                                        Date beginTime = df.parse(startDate);
                                        Date endTime = df.parse(endDate);
                                        long diff = endTime.getTime()
                                                - beginTime.getTime();
                                        long hours = diff / (1000 * 60 * 60 * 24);
                                        totalTimes = Long.toString(hours) + "天";
                                    }else{
                                        startDate = order.getString("startDate");
                                        endDate = order.getString("endDate");
                                        SimpleDateFormat df = new SimpleDateFormat(
                                                "yyyy-MM-dd");
                                        Date beginTime = df.parse(startDate);
                                        Date endTime = df.parse(endDate);
                                        long diff = endTime.getTime()
                                                - beginTime.getTime();
                                        long days = diff / (1000 * 60 * 60 * 24);
                                        totalTimes = Long.toString(days) + "天";
                                    }
                                    System.out.println("---------rrrrrrrrrrrrrr----123321------"+warehousejson.getJSONObject("data"));
                                    System.out.println("---------rrrrrrrrrrrrrr----123------"+warehousejson.getJSONObject("data").getJSONObject("Warehouse"));
                                    System.out.println("---------rrrrrrrrrrrrrr----------"+warehousejson.getJSONObject("data").getJSONObject("Warehouse").getString("name"));
                                    context = "尊敬的" + order.getString("custName")
                                            + "先生/女士，您已成功获得" + warehousejson.getJSONObject("data").getJSONObject("Warehouse").getString("name")
                                            + warehouseCell.getString("number") + "号柜" + totalTimes
                                            + "的使用权，到期时间是" + endDate
                                            + "。";
                                }
                            }
                            //设置密码
                            Map<String,String> selCabinetMap = new HashMap<String, String>();
                            selCabinetMap.put("id",warehouseCell.getString("cabinetId"));
                            System.out.println("warehouseCell========================"+warehouseCell);
                            String cabinetURL = ZkPropertyUtil.get("BaseUrlStorage")+"cabinet/getCabinet";
                            JSONObject cabinetjson = ApiUtils.excutePost(cabinetURL,null,null,selCabinetMap);
                            JSONObject cabinet = cabinetjson.getJSONObject("data").getJSONObject("cabinet");
                            if(cabinet.getString("no").substring(0,2).equals("WH")){
                                System.out.println("cabinet.getCabinetNo().substring(0,2)2:"+cabinet.getString("no").substring(0,2));
                                String SetPadPass =  cabinet.getString("id")+"-"+cabinet.getString("no")+"-"+cabinet.getString("doorNo")+"-"+warehouseCell.getString("number")+"-"+order.getString("custTel")+"-"+orderId;
                                String CreatePasswordUrl = ZkPropertyUtil.get("CreatePassword");
                                String url=CreatePasswordUrl+"?SetPadPass="+SetPadPass;
                                String createPasswordUrl=ZkPropertyUtil.get("BaseUrlLock")+"lock/auto/createPassword";
                                System.out.println(createPasswordUrl);
                                Map<String,String> createPassworMap = new HashMap<String, String>();
                                createPassworMap.put("SetPadPass",SetPadPass);
                                JSONObject createPasswordObject = ApiUtils.excutePost(createPasswordUrl,null,null,createPassworMap);
                            }
                            //设置密码结束
                            SMSUtils.sendSMS(order.getString("custTel"), context);
                            WeixinMessageUtil.AysncSendSmsLog(order.getString("custTel"),context,"1");
                            System.out.println("-------购买成功日志--------:"+context);
                        }else{
                            System.out.println("------------------查询-wacejson--失败-----------------");
                        }
                    }else{
                        System.out.println("------------------查询order--失败-----------------");
                    }
                }
            }else if(orderRenewObject.getIntValue("code")==10000){
                System.out.println("--------updateOrderStatus----------查询orderRenew--有订单-----------------");
            }else{
                System.out.println("------------------查询orderRenew--失败-----------------");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return message;
    }

    public String updateOrderStatusByCoverWarehouse(
            String coverWarehouseAmount, String endDate, String orderId,
            String transactionId) {
        String message = "success";
        Map<String,String> orderRenewMap = new HashMap<String, String>();
        orderRenewMap.put("accountNumber",transactionId);
        String orderRenewURL = ZkPropertyUtil.get("BaseUrlMember")+"orderManage/selOrdRenewByAccNum";
        JSONObject orderRenewObject = null;
        try {
            orderRenewObject = ApiUtils.excutePost(orderRenewURL,null,null,orderRenewMap);
            if(orderRenewObject.getIntValue("code")==10001){
                // 修改订单状态
                Map<String,String> orderMap = new HashMap<String, String>();
                orderMap.put("orderId",orderId);
                String orderURL = ZkPropertyUtil.get("BaseUrlMember")+"orderManage/getOrderById";

                JSONObject orderjson = ApiUtils.excutePost(orderURL,null,null,orderMap);
                if(orderjson.getIntValue("code")==10000){
                    JSONObject order = orderjson.getJSONObject("data").getJSONObject("Order");
                    Map<String,String> waceMap =new HashMap<String, String>();
                    waceMap.put("wahoceId",order.getString("wahoceId"));
                    String waceURL = ZkPropertyUtil.get("BaseUrlStorage")+"Warehouse/getWarehouseCell";
                    JSONObject wacejson = ApiUtils.excutePost(waceURL,null,null,waceMap);
                    if(wacejson.getIntValue("code")==10000){
                        JSONObject warehouseCell = wacejson.getJSONObject("data").getJSONObject("warehouseCell");
                        Map<String,String> warehouseMap = new HashMap<String, String>();
                        warehouseMap.put("id",warehouseCell.getString("wahoId"));
                        String warehouseURL = ZkPropertyUtil.get("BaseUrlStorage")+"Warehouse/getWarehouseInfo";
                        JSONObject warehousejson = ApiUtils.excutePost(warehouseURL,null,null,warehouseMap);

                        //新增缴费记录
                        Map<String,String> renewMap = new HashMap<String, String>();
                        renewMap.put("orderId",orderId);
                        renewMap.put("startDate",order.getString("startDate"));
                        renewMap.put("endDate",endDate);
                        renewMap.put("accountNumber",transactionId);
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                        Long date = System.currentTimeMillis();
                        renewMap.put("date",format.format(date));
                        renewMap.put("money",coverWarehouseAmount);
                        renewMap.put("status","1");// 缴费状态：0;待缴费，1：已缴费
                        renewMap.put("paymentType","2");// 0现金;1 汇款 2在线支付
                        renewMap.put("status","1");
                        String insRenew = JSON.toJSONString(renewMap);
                        String insRenewsURL = ZkPropertyUtil.get("BaseUrlMember")+"orderManage/insOrderRenew";
                        JSONObject  renewsjson= ApiUtils.excutePost(insRenewsURL,null,insRenew,null);

                        String   context = "尊敬的"+order.getString("custName")+"先生/女士，您已成功获得"+warehousejson.getJSONObject("data").getJSONObject("Warehouse").getString("name")+warehouseCell.getString("number")+"号柜,到期时间是"+endDate.substring(0,16);
                        SMSUtils.sendSMS(order.getString("custTel"), context);
                        WeixinMessageUtil.AysncSendSmsLog(order.getString("custTel"),context,"1");
                        Double newTotal = Double.valueOf(order.getString("totalAmount"))+ Double.valueOf(coverWarehouseAmount);
                        DecimalFormat df = new DecimalFormat("######0.00");
                        String newTotalStr = df.format(newTotal);
                        //修改订单
                        Map<String,String> updorderMap = new HashMap<String, String>();

                        updorderMap.put("totalAmount",newTotalStr);
                        updorderMap.put("amount",newTotalStr);
                        updorderMap.put("endDate",endDate);
                        String updOrder = JSON.toJSONString(updorderMap);
                        String updOrderURL = ZkPropertyUtil.get("BaseUrlMember")+"orderManage/updOrder";
                        JSONObject  updOrderjson= ApiUtils.excutePost(updOrderURL,null,updOrder,null);

                    }else{
                        System.out.println("------------------查询wacejson--失败-----------------");
                    }
                }else{
                    System.out.println("------------------查询orderjson--失败-----------------");
                }
            }else if(orderRenewObject.getIntValue("code")==10001){
                System.out.println("---------updateOrderStatusByCoverWarehouse---------查询orderRenew--有订单-----------------");
            }else{
                System.out.println("------------------查询orderRenew--失败-----------------");
            }
        } catch (Exception e) {
            message = "error";
            e.printStackTrace();
        }


        return message;
    }


}
