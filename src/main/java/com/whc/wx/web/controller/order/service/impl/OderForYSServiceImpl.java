package com.whc.wx.web.controller.order.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Strings;
import com.wanhuchina.common.code.CommonCode;
import com.wanhuchina.common.exception.CommonException;
import com.wanhuchina.common.util.http.ApiUtils;
import com.wanhuchina.common.util.zk.ZkPropertyUtil;
import com.whc.wx.web.controller.order.service.IOrderForYSService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.whc.wx.web.util.SMSUtils;
import org.wss.utils.WeixinMessageUtil;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by WangShengZhan
 * Email：WangShengZhan@wanhuchina.com
 * Date：2017/6/28
 * Time：17:52
 */
@Service("iOrderForYSService")
public class OderForYSServiceImpl implements IOrderForYSService {
    private static final Logger logger= LoggerFactory.getLogger(OderForYSServiceImpl.class);
    /**
     * 修改订单状态
     * @param orderId
     * @param startDate
     * @param endDate
     * @return
     */

    public String updateOrderStatus(String orderId, String startDate, String endDate)  throws Exception {
        String message="success";
        JSONObject js = new JSONObject();
        String totalTimes="";
        String context = "";
        String number = "";
        String unit = "";
        try {
            /**
             * 根据订单id查询订单
             */
            Map<String,String> orderMap = new HashMap<String, String>();
            orderMap.put("orderId",orderId);
            String orderURL = ZkPropertyUtil.get("BaseUrlMember")+"orderManage/getOrderById";
            JSONObject orderjson = ApiUtils.excutePost(orderURL,null,null,orderMap);
            System.out.println("orderjson===order:"+orderjson.getIntValue("code"));
            if(orderjson.getIntValue("code")==10000){
                /**
                 * 根据门柜id查询门柜信息
                 */
                JSONObject order = orderjson.getJSONObject("data").getJSONObject("Order");
                System.out.println("order===order:"+order.toString());
                Map<String,String> wahoceMap = new HashMap<String, String>();
                System.out.println("wahoceId===order:"+order.getString("wahoceId"));
                wahoceMap.put("wahoceId",order.getString("wahoceId"));
                String wahoceURL = ZkPropertyUtil.get("BaseUrlStorage")+"Warehouse/getWarehouseCell";
                JSONObject wahocejson = ApiUtils.excutePost(wahoceURL,null,null,wahoceMap);
                if(wahocejson.getIntValue("code")==10000){
                    /**
                     * 根据门店id查询门店信息
                     */
                    JSONObject warehouseCell = wahocejson.getJSONObject("data").getJSONObject("warehouseCell");
                    Map<String,String>  warehouseMap  = new HashMap<String, String>();
                    warehouseMap.put("wahoId",warehouseCell.getString("wahoId"));
                    String warehouseURL = ZkPropertyUtil.get("BaseUrlStorage")+"Warehouse/getWarehouseInfo";
                    JSONObject warehousejson = ApiUtils.excutePost(warehouseURL,null,null,warehouseMap);
                    JSONObject warehouse = warehousejson.getJSONObject("data").getJSONObject("Warehouse");
                    if(!Strings.isNullOrEmpty(order.getString("id"))){
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                        Map<String,String> orderRenewMap = new HashMap<String, String>();
                        Map<String,String> updorderMap = new HashMap<String, String>();
                        //订单是续仓的支付
                        if(!Strings.isNullOrEmpty(order.getString("nextEndDate"))&&!Strings.isNullOrEmpty(order.getString("nextAmount"))){
                            orderRenewMap.put("orderId","");
                            if("8".equals(warehouseCell.getString("type"))){//8 即时柜
                                orderRenewMap.put("startDate",order.getString("endDate"));
                                startDate = order.getString("endDate").substring(0,16);
                                endDate = order.getString("nextEndDate").substring(0,16);
                                number = order.getString("number");
                                unit = order.getString("unit");
                                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                                if("1".equals(unit)){
                                    totalTimes=number+"小时";
                                }
                                if("2".equals(unit)){
                                    totalTimes=number+"天";
                                }
                                if("3".equals(unit)){
                                    totalTimes=number+"个月";
                                }
                            }else if("10".equals(warehouseCell.getString("type"))){//
                                orderRenewMap.put("startDate",order.getString("endDate"));
                                startDate=order.getString("endDate").substring(0,16);
                                endDate=order.getString("nextEndDate").substring(0,16);
                                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                                Date beginTime =  df.parse(startDate);
                                Date endTime =  df.parse(endDate);
                                long diff = endTime.getTime() - beginTime.getTime();
                                long hours = diff/(1000* 60 * 60*24);
                                totalTimes=Long.toString(hours)+"天";
                            }else{
                                Calendar calendar = Calendar.getInstance();
                                startDate=order.getString("endDate");
                                endDate=order.getString("nextEndDate");
                                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                                Date beginTime =  df.parse(startDate);
                                Date endTime =  df.parse(endDate);
                                long diff = endTime.getTime() - beginTime.getTime();
                                long days = diff/(1000 * 60 * 60 * 24);
                                totalTimes=Long.toString(days)+"天";
                                calendar.setTime(format.parse(order.getString("endDate")));
                                long end = calendar.getTimeInMillis()+24*3600*1000;
                                calendar.setTimeInMillis(end);
                                orderRenewMap.put("startDate",format.format(calendar.getTime()));
                            }
                            orderRenewMap.put("endDate",order.getString("nextEndDate"));
                            orderRenewMap.put("date",format.format(new Date()));
                            orderRenewMap.put("money",order.getString("nextAmount"));
                            orderRenewMap.put("status","1");//缴费状态：0;待缴费，1：已缴费
                            orderRenewMap.put("paymentType","2");//0现金;1 汇款 2在线支付
                            orderRenewMap.put("endDate",order.getString("nextEndDate"));
                            /**
                             * 新增缴费记录
                             */
                            String insorderRenew = JSON.toJSONString(orderRenewMap);
                            String insorderRenewURL = ZkPropertyUtil.get("BaseUrlMember")+"orderManage/insOrderRenew";
                            JSONObject insOrderRenewObject = ApiUtils.excutePost(insorderRenewURL,null,insorderRenew,null);

                            updorderMap.put("endDate",order.getString("nextEndDate"));
                            updorderMap.put("nextEndDate","");
                            Double newTotal = Double.valueOf(order.getString("totalAmount"))+Double.valueOf(order.getString("nextAmount"));
                            DecimalFormat df=new DecimalFormat("######0.00");
                            String newTotalStr=df.format(newTotal);
                            System.out.println("String.valueOf()***"+newTotalStr);
                            updorderMap.put("totalAmount",newTotalStr);
                            updorderMap.put("nextAmount","");
                            updorderMap.put("id",orderId);
                            /**
                             * 根据id修改订单
                             */
                            String updorder = JSON.toJSONString(updorderMap);
                            String updorderURL = ZkPropertyUtil.get("BaseUrlMember")+"orderManage/updOrder";
                            JSONObject updorderObject = ApiUtils.excutePost(updorderURL,null,updorder,null);

                            context="尊敬的"+order.getString("custName")+"先生/女士，您成功获得"+warehouse.getString("name")+warehouseCell.getString("number")+"号柜"+totalTimes+"的使用权，到期时间是"+endDate+"。";
                            //context = "尊敬的"+order.getCustName()+"先生/女士您好，您已成功续仓了"+warehouse.getName()+"门店,"+wace.getNumber()+"仓位,谢谢！感谢您选择并信任万户仓。";
                            js.put("result", "success");
                            js.put("info", "修改订单状态订单成功！");
                        }else{//首次缴费
                            //根据id修改订单
                            updorderMap.put("status","1");//订单状态：0 待付款(待支付) 1 已生效（已支付） 2 已过期 3 作废 4退单（已支付后退单的）5待审核
                            updorderMap.put("id",orderId);
                            String updorder = JSON.toJSONString(updorderMap);
                            String updorderURL = ZkPropertyUtil.get("BaseUrlMember")+"orderManage/updOrder";
                            JSONObject updorderObject = ApiUtils.excutePost(updorderURL,null,updorder,null);
                            //新增缴费记录
                            orderRenewMap.put("orderId",orderId);
                            orderRenewMap.put("startDate",order.getString("startDate"));
                            orderRenewMap.put("endDate",order.getString("endDate"));
                            orderRenewMap.put("date",format.format(new Date()));
                            orderRenewMap.put("money",order.getString("totalAmount"));
                            orderRenewMap.put("status","1");//缴费状态：0;待缴费，1：已缴费
                            orderRenewMap.put("paymentType","2");//0现金;1 汇款 2在线支付
                            String insorderRenew = JSON.toJSONString(orderRenewMap);
                            String insorderRenewURL = ZkPropertyUtil.get("BaseUrlMember")+"orderManage/insOrderRenew";
                            JSONObject insOrderRenewObject = ApiUtils.excutePost(insorderRenewURL,null,insorderRenew,null);

                            //修改仓位状态
                            Map<String,String> updwahoceMap = new HashMap<String, String>();
                            updwahoceMap.put("status","1");
                            updwahoceMap.put("wahoceId",warehouseCell.getString("id"));
                            String updwahoce = JSON.toJSONString(updorderMap);
                            String updwahoceURL = ZkPropertyUtil.get("BaseUrlStorage")+"Warehouse/updateWarehouseCellStatus";
                            JSONObject updwahoceObject = ApiUtils.excutePost(updwahoceURL,null,null,updwahoceMap);

                            int password = (int) (Math.random()*90000)+10000;
                            /**
                             * 根据cabinetId 获取 Cabinet 信息
                             */
                            Map<String,String> cabinetMap = new HashMap<String, String>();
                            cabinetMap.put("id",warehouseCell.getString("cabinetId"));
                            String cabinet = JSON.toJSONString(cabinetMap);
                            String cabinetURL = ZkPropertyUtil.get("BaseUrlStorage")+"cabinet/getCabinet";
                            JSONObject cabinetObject = ApiUtils.excutePost(cabinetURL,null,null,cabinetMap);
                            if("8".equals(warehouseCell.getString("type"))){//8 即时柜
                                startDate=order.getString("startDate").substring(0,16);
                                endDate=order.getString("endDate").substring(0,16);
                                number = order.getString("number");
                                unit = order.getString("unit");
                                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                                if("1".equals(unit)){
                                    totalTimes=number+"小时";
                                }
                                if("2".equals(unit)){
                                    totalTimes=number+"天";
                                }
                                if("3".equals(unit)){
                                    totalTimes=number+"个月";
                                }
                            }  else if("10".equals(warehouseCell.getString("type"))){
                                startDate=order.getString("startDate").substring(0,16);
                                endDate=order.getString("endDate").substring(0,16);
                                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                                Date beginTime =  df.parse(startDate);
                                Date endTime =  df.parse(endDate);
                                long diff = endTime.getTime() - beginTime.getTime();
                                long hours = diff/(1000* 60 * 60*24);
                                totalTimes=Long.toString(hours)+"天";
                            }else{
                                startDate=order.getString("startDate");
                                endDate=order.getString("endDate");
                                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                                Date beginTime =  df.parse(startDate);
                                Date endTime =  df.parse(endDate);
                                long diff = endTime.getTime() - beginTime.getTime();
                                long days = diff/(1000 * 60 * 60 * 24);
                                totalTimes=Long.toString(days)+1+"天";
                            }
                            System.out.println("custName"+order.getString("custName"));
                            System.out.println("name"+warehouse.getString("name"));
                            System.out.println("number"+warehouseCell.getString("number"));
                            System.out.println("totalTimes"+totalTimes);
                            System.out.println("endDate"+endDate);
                            context="尊敬的"+order.getString("custName")+"先生/女士，您成功获得"+warehouse.getString("name")+warehouseCell.getString("number")+"号柜"+totalTimes+"的使用权，到期时间是"+endDate+"。";
                            js.put("result", "success");
                            js.put("info", "修改订单状态订单成功！");
                        }
                        SMSUtils.sendSMS(order.getString("custTel"), context);
                        WeixinMessageUtil.AysncSendSmsLog(order.getString("custTel"),context,"1");
                        logger.info("购买成功短信: "+ order.getString("custTel") +":" +context);
                    }
                }else{
                    System.out.println("--------查询-------Warehouse/getWarehouseCell----失败------");
                }
            }else{
                System.out.println("--------查询order--失败------");
            }
        } catch (Exception e) {
            message="error";
            e.printStackTrace();

        }
        return message;
    }

    //使用优惠券之后更改优惠券状态
    public String modifyVoucherState(String voucherId,String orderId)  throws CommonException{
        JSONObject voucherObject = null;

        Map<String,String> vouchermap = new HashMap<String, String>();
        vouchermap.put("voucherId",voucherId);
        String voucherURL = ZkPropertyUtil.get("BaseUrlMember")+"voucherManage/selVoucherById";
        try {
            voucherObject = ApiUtils.excutePost(voucherURL,null,null,vouchermap);
            if (voucherObject != null || "".equals(voucherObject)){
                Map<String,String> updVoucherMap = new HashMap<String, String>();
                updVoucherMap.put("id",voucherId);
                updVoucherMap.put("status","4");
                updVoucherMap.put("orderId",orderId);
                Date now = new Date();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                updVoucherMap.put("usedTime",dateFormat.format(now));
                String updVoucher = JSON.toJSONString(updVoucherMap);
                String updVoucherURL = ZkPropertyUtil.get("BaseUrlMember")+"voucherManage/updByVoucher";
                JSONObject updVoucherObject = ApiUtils.excutePost(updVoucherURL,null,updVoucher,null);
                if(updVoucherObject.getInteger("code") == 10000){
                    return "success";
                }else{
                    throw new CommonException(CommonCode.PARAM_ERROR.getCode(),"优惠券绑定失败");
                }
            }else{
                return "error";
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new CommonException(CommonCode.PARAM_ERROR.getCode(),"优惠券绑定失败");
        }

    }
    /**
     * 续租
     */
    public JSONObject continueWarehouse(String orderId, String rentalTime, String total, String endDate, String unit, String number)  throws Exception{
        JSONObject js = new JSONObject();
        /**
         * 查询order订单
         */
        Map<String,String> orderMap = new HashMap<String, String>();
        orderMap.put("orderId",orderId);
        System.out.println("orderId:"+orderId);
        String orderURL = ZkPropertyUtil.get("BaseUrlMember")+"orderManage/getOrderById";
        JSONObject orderjson;
        try {
            orderjson = ApiUtils.excutePost(orderURL,null,null,orderMap);
            if(orderjson.getIntValue("code")==10000){
                JSONObject order = orderjson.getJSONObject("data").getJSONObject("Order");
                /**
                 * 查询WarehouseCell
                 */
                Map<String,String> wahoceMap = new HashMap<String, String>();
                wahoceMap.put("wahoceId",order.getString("wahoceId"));
                String wahoceURL = ZkPropertyUtil.get("BaseUrlStorage")+"Warehouse/getWarehouseCell";
                JSONObject wahoCelljson = ApiUtils.excutePost(wahoceURL,null,null,wahoceMap);
                if(wahoCelljson.getIntValue("code")==10000){
                    JSONObject warehouseCell =  wahoCelljson.getJSONObject("data").getJSONObject("warehouseCell");
                    /**
                     * 查询wahoceTypePrice
                     */
                    Map<String,String>  wahoceTypePriceMap = new HashMap<String, String>();
                    wahoceTypePriceMap.put("wahoId",warehouseCell.getString("wahoId"));
                    wahoceTypePriceMap.put("watyId",warehouseCell.getString("type"));
                    String wahoceTypePriceURL =  ZkPropertyUtil.get("BaseUrlStorage")+"wahoce/getWahoceTypePrice";
                    JSONObject wahoceTypePriceJson = ApiUtils.excutePost(orderURL,null,null,orderMap);
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    /**
                     * 修改Order
                     */
                    Map<String,String> updOrderMap = new HashMap<String, String>();
                    System.out.println("========warehouseCell.type==========="+warehouseCell.getString("type"));
                    if("8".equals(warehouseCell.getString("type"))){
                        //计算结束时间
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Calendar calendar =Calendar.getInstance();
                        calendar.setTime(dateFormat.parse(order.getString("endDate")));
                        calendar.setTimeInMillis(calendar.getTimeInMillis()+Long.valueOf(rentalTime)*3600*1000);
                        updOrderMap.put("nextEndDate",String.valueOf(dateFormat.format(calendar.getTime())));
                        updOrderMap.put("amount",order.getString("amount"));
                        updOrderMap.put("nextAmount",total);
                        updOrderMap.put("unit",order.getString("unit"));
                        updOrderMap.put("totalTimes",number);
                    }else if("10".equals(warehouseCell.getString("type"))){
                        //计算结束时间
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Calendar calendar =Calendar.getInstance();
                        calendar.setTime(dateFormat.parse(order.getString("endDate")));
                        calendar.setTimeInMillis(calendar.getTimeInMillis()+Long.valueOf(rentalTime)*24*3600*1000);
                        updOrderMap.put("nextEndDate",String.valueOf(dateFormat.format(calendar.getTime())));
                        updOrderMap.put("amount",order.getString("amount"));
                        updOrderMap.put("nextAmount",total);
                    }else{
                        updOrderMap.put("nextEndDate",endDate);
                        updOrderMap.put("amount",order.getString("totalAmount"));
                        updOrderMap.put("nextAmount",total);
                    }
                    System.out.println("========updOrderMap==========="+updOrderMap.toString());
                    String updorderURL = ZkPropertyUtil.get("BaseUrlMember")+"orderManage/updOrder";
                    updOrderMap.put("id",orderId);
                    System.out.println("-------orderId--------"+orderId);
                    String updorder = JSON.toJSONString(updOrderMap);
                    JSONObject updorderObject = ApiUtils.excutePost(updorderURL,null,updorder,null);
                    System.out.println("-------updorderObject--------"+updorderObject.toString());
                    js.put("orderId", orderId);
                }else{
                    System.out.println("------continueWarehouse-----Warehouse/getWarehouseCell-------失败----------");
                }
            }else{
                System.out.println("------continueWarehouse-----orderManage/getOrderById-------失败----------");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return js;
    }

    public JSONObject returnWarehouse(String orderId,String startDate,String endDate)  throws Exception{
        JSONObject js = new JSONObject();
        Map<String,String> orderMap = new HashMap<String, String>();
        orderMap.put("orderId",orderId);
        String orderURL = ZkPropertyUtil.get("BaseUrlMember")+"orderManage/getOrderById";
        JSONObject orderjson = ApiUtils.excutePost(orderURL,null,null,orderMap);
        if(orderjson.getIntValue("code")==10000) {
            JSONObject order = orderjson.getJSONObject("data").getJSONObject("Order");
            /**
             * 修改订单updOrderMap状态
             */
            Map<String,String> updorderMap = new HashMap<String, String>();
            if(order.getString("id")!=null && !"".equals(order.getString("id"))){
                updorderMap.put("id",order.getString("id"));//订单id
                updorderMap.put("status","2");//订单状态：0 待付款(待支付) 1 已生效（已支付） 2 已过期 3 作废 4退单（已支付后退单的）5待审核
                Date date = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                updorderMap.put("returnCellTime",sdf.format(new Date()));
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                updorderMap.put("overDate",sdf.format(new Date()));
                String updorderURL = ZkPropertyUtil.get("BaseUrlMember")+"orderManage/updOrder";
                String updorder = JSON.toJSONString(updorderMap);
                JSONObject updorderObject = ApiUtils.excutePost(updorderURL,null,updorder,null);
                Map<String,String> wahoceMap = new HashMap<String, String>();
                wahoceMap.put("wahoceId",order.getString("wahoceId"));
                String wahoceURL = ZkPropertyUtil.get("BaseUrlStorage")+"Warehouse/getWarehouseCell";
                JSONObject wahocejson = ApiUtils.excutePost(wahoceURL,null,null,wahoceMap);
                /**
                 * 修改WarehouseCell
                 */
                Map<String,String> updwahoceMap = new HashMap<String, String>();
                if(wahocejson.getIntValue("code")==10000){
                    JSONObject warehouseCell = wahocejson.getJSONObject("data").getJSONObject("warehouseCell");
                    updwahoceMap.put("status","0");
                    updwahoceMap.put("wahoceId",warehouseCell.getString("id"));
                    String updwahoce = JSON.toJSONString(updwahoceMap);
                    String updwahoceURL = ZkPropertyUtil.get("BaseUrlStorage")+"Warehouse/updateWarehouseCellStatus";
                    JSONObject updwahoceObject = ApiUtils.excutePost(updwahoceURL,null,null,updwahoceMap);
                    /**
                     * 查询Warehouse
                     */
                    Map<String,String>  warehouseMap  = new HashMap<String, String>();
                    warehouseMap.put("wahoId",warehouseCell.getString("wahoId"));
                    String warehouseURL = ZkPropertyUtil.get("BaseUrlStorage")+"Warehouse/getWarehouseInfo";
                    JSONObject warehousejson = ApiUtils.excutePost(warehouseURL,null,null,warehouseMap);
                    JSONObject warehouse = warehousejson.getJSONObject("data").getJSONObject("Warehouse");
                    if(warehouseCell!=null && "8".equals(warehouseCell.getString("type"))){
                        Map<String,String> cabinetMap = new HashMap<String, String>();
                        cabinetMap.put("id",warehouseCell.getString("cabinetId"));
                        String cabinetURL = ZkPropertyUtil.get("BaseUrlStorage")+"cabinet/getCabinet";
                        JSONObject cabinetObject = ApiUtils.excutePost(cabinetURL,null,null,cabinetMap);
                        if(cabinetObject.getIntValue("code")==10000){
                            String totalTimes="";
                            startDate=order.getString("startDate").substring(0,16);
                            endDate=order.getString("endDate").substring(0,16);
                            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                            Date beginTime =  df.parse(startDate);
                            Date endTime =  df.parse(endDate);
                            long diff = endTime.getTime() - beginTime.getTime();
                            long hours = diff/(1000 * 60 * 60);
                            totalTimes=Long.toString(hours)+"小时";
                            String context="";
                            System.out.println("custName:"+order.getString("custName"));
                            System.out.println("name:"+warehouse.getString("name"));
                            System.out.println("number:"+warehouseCell.getString("number"));
                            context="尊敬的"+order.getString("custName")+"先生/女士，您成功结束"+warehouse.getString("name")+warehouseCell.getString("number")+"号柜"+totalTimes+"的使用权。";
                            SMSUtils.sendSMS(order.getString("custTel"), context);
                            WeixinMessageUtil.AysncSendSmsLog(order.getString("custTel"),context,"1");
                            js.put("result", "success");
                            js.put("info", "退仓成功！仓门已开,请及时取走物件!");
                        }else{
                            System.out.println("------------请求-cabinet/getCabinet-----------"+cabinetObject.getString("code"));
                        }
                    }else{
                        //发送短信给客户
                        String totalTimes="";
                        startDate=order.getString("startDate");
                        endDate=order.getString("endDate");
                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                        Date beginTime;
                        try {
                            beginTime = df.parse(startDate);
                            Date endTime =  df.parse(endDate);
                            long diff = endTime.getTime() - beginTime.getTime();
                            long days = diff/(1000 * 60 * 60 * 24);
                            totalTimes=Long.toString(days)+"天";
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        String context = "";
                        context="尊敬的"+order.getString("custName")+"先生/女士，您已成功结束"+warehouse.getString("name")+warehouseCell.getString("number")+"号柜"+totalTimes+"的使用权。";
                        SMSUtils.sendSMS(order.getString("custTel"), context);
                        WeixinMessageUtil.AysncSendSmsLog(order.getString("custTel"),context,"1");
                        js.put("result", "success");
                        js.put("info", "退仓成功！仓门已开,请及时取走物件!");
                    }
                }else{
                    System.out.println("------------请求-Warehouse/getWarehouseCell-----------"+wahocejson.getString("code"));
                }
            }
        }else{
            System.out.println("------------请求-orderManage/getOrderById-----------"+orderjson.getString("code"));
        }
        return js;
    }
    public static String httpRequest(String request) {
        StringBuffer buffer = new StringBuffer();
        try {
            // 建立连接
            URL url = new URL(request);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setUseCaches(false);
            connection.setRequestMethod("GET");
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
        } catch (Exception e) {
        }
        return buffer.toString();
    }



}
