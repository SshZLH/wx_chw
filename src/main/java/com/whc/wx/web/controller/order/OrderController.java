package com.whc.wx.web.controller.order;

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
public class OrderController {
    private static final Logger logger= LoggerFactory.getLogger(OrderController.class);
    OderForYSServiceImpl orderService = new OderForYSServiceImpl();
    /**
     * 新增订单
     * @param req
     * @return
     */
    @RequestMapping(value="addImmediateView",produces="application/json; charset=utf-8")
    @ResponseBody
    public String addImmediateView(HttpServletRequest req){
        JSONObject object = new JSONObject();
        JSONObject 	ject = new JSONObject();
        JSONObject insOrderObject = null;
        List<Map> returnList = new ArrayList<Map>();
        String message="";
        try {
            //获取Memebr信息
            Map<String,String> memberMap = new HashMap<String, String>();
            memberMap.put("openId",req.getParameter("openId"));
            String mamberURL = ZkPropertyUtil.get("BaseUrlMember")+"memberManage/getMemberByOpenId";
            JSONObject memberjson = ApiUtils.excutePost(mamberURL,null,null,memberMap);
            //获取WarehouseCell信息
            Map<String,String> WaHoCellMap = new HashMap<String, String>();
            WaHoCellMap.put("wahoceId",req.getParameter("wahoceId"));
            String WaHoCellURL = ZkPropertyUtil.get("BaseUrlStorage")+"Warehouse/getWarehouseCell";
            JSONObject WaHoCelljson = ApiUtils.excutePost(WaHoCellURL,null,null,WaHoCellMap);
            System.out.println("------------wahojson.toString()------------"+WaHoCelljson.toString());
            //获取Warehouse信息
            Map<String,String> wahoMap = new HashMap<String, String>();
            wahoMap.put("wahoId",req.getParameter("wahoId"));
            String wahoURL = ZkPropertyUtil.get("BaseUrlStorage")+"Warehouse/getWarehouseInfo";
            JSONObject wahojson = ApiUtils.excutePost(wahoURL,null,null,wahoMap);
            System.out.println("------------wahojson.toString()------------"+wahojson.toString());
            if(wahojson.getIntValue("code")==10000){
                JSONObject warehouseJson = wahojson.getJSONObject("data").getJSONObject("Warehouse");
                if("1".equals(warehouseJson.getString("status"))){
                    message= "门店已停用.";
                }else if("0".equals(WaHoCelljson.getJSONObject("data").getJSONObject("warehouseCell").getString("status"))){
                    //修改门柜状态
                    Map<String,String> upWaHoCellMap = new HashMap<String, String>();
                    upWaHoCellMap.put("wahoceId",req.getParameter("wahoceId"));
                    upWaHoCellMap.put("status","4");//状态：0：空闲 1：已使用 2停用，3:可预定，4：预约
                    String upWaHoCellURL = ZkPropertyUtil.get("BaseUrlStorage")+"Warehouse/updateWarehouseCellStatus";
                    JSONObject upWaHoCelljson = ApiUtils.excutePost(upWaHoCellURL,null,null,upWaHoCellMap);
                    if(upWaHoCelljson.getIntValue("code")==10000){
                        if(memberjson.getIntValue("code")==10000){
                            //新增订单
                            Map<String,String> insOrderMap= new HashMap<String, String>();
                            insOrderMap.put("memberId",memberjson.getJSONObject("data").getString("id"));
                            //add by fjc 增加已发生租金
                            insOrderMap.put("amount","0");
                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            Calendar calendar = Calendar.getInstance();
                            long time = System.currentTimeMillis();
                            calendar.setTimeInMillis(time/1000/60*60*1000);
                            insOrderMap.put("startDate",String.valueOf(dateFormat.format(calendar.getTime())));
                            calendar.setTimeInMillis(System.currentTimeMillis()/1000/60*60*1000+Long.valueOf(req.getParameter("rentalTime"))*3600*1000);
                            insOrderMap.put("endDate",String.valueOf(String.valueOf(dateFormat.format(calendar.getTime()))));
                            insOrderMap.put("exeAccount",req.getParameter("total"));
                            insOrderMap.put("totalAmount",req.getParameter("total"));
                            insOrderMap.put("wahoceId",req.getParameter("wahoceId"));
                            insOrderMap.put("exeAccount",req.getParameter("total"));
                            insOrderMap.put("custName",req.getParameter("memberName"));
                            insOrderMap.put("custTel",req.getParameter("memberTel"));
                            insOrderMap.put("isDisplay","0");//历史订单删除项（是否显示） ssh 0310
                            insOrderMap.put("status","0");//订单状态：0 待付款(待支付) 1 已生效（已支付） 2 已过期 3 作废 4退单（已支付后退单的）5待审核
                            insOrderMap.put("type","0");//订单类型：1续仓，0新增
                            SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                            String createDate = dateFormat1.format(new Date());
                            insOrderMap.put("createDate",createDate);
                            insOrderMap.put("payType","3");//订单的支付方式：1现金;2 汇款 3在线支付
                            insOrderMap.put("source","0");//订单来源,0:微信端,1:pc端,2APP
                            insOrderMap.put("amountType","3");//订单租金支付方式：0:月付 1:季付 2:年付 3:一次性付清
                            insOrderMap.put("typeMoeny",req.getParameter("total"));//订单按租金支付方式的每次应付的金额
                            insOrderMap.put("depositStatus","0");//押金状态：0：未退还，1：已退还
                            insOrderMap.put("nextOrder","-1");//续仓的订单的id 初始值:-1 表示此订单没有续仓
                            insOrderMap.put("number",req.getParameter("number"));//租用时长
                            insOrderMap.put("unit",req.getParameter("unit"));//租用单位
                            insOrderMap.put("deposit1","0");
                            //add by fjc 20160926 begin reason:去掉押金
                            DecimalFormat df=new DecimalFormat("######0.00");
                            String totalAmountStr=df.format(Double.valueOf(req.getParameter("total")));
                            insOrderMap.put("totalAmount",totalAmountStr);
                            String insOrder = JSON.toJSONString(insOrderMap);
                            String insOrderBaseURL = ZkPropertyUtil.get("BaseUrlMember")+"orderManage/insOrder";
                            insOrderObject = ApiUtils.excutePost(insOrderBaseURL,null,insOrder,null);
                            if(insOrderObject.getIntValue("code")==10000){
                                //imageUrl = "/orderAction!addView.do";
                                message= "immediateRental";
                            }else{
                                System.out.println("--------请求--orderManage/insOrder 报错------------");
                            }
                        }else{
                            System.out.println("--------请求--memberManage/getMemberByOpenId 报错------------");
                        }
                    }else{
                        System.out.println("--------请求--修改门柜状态接口 报错------------");
                    }
                }else{
                    message= "柜体被占用."+warehouseJson.getString("status");
                }
                Map  WahoIdMap=new HashMap();
                WahoIdMap.put("wahoId", WaHoCelljson.getJSONObject("data").getJSONObject("warehouseCell").getString("id"));
                Map  wahoTypeMap=new HashMap();
                wahoTypeMap.put("wahoType", wahojson.getJSONObject("data").getJSONObject("Warehouse").getString("id"));
                Map  map=new HashMap();
                map.put("message", message);
                Map orderIdMap=new HashMap();
                System.out.println("---id-:"+insOrderObject.getJSONObject("data").getString("id"));
                orderIdMap.put("orderId", insOrderObject.getJSONObject("data").getString("id"));
                returnList.add(map);
                returnList.add(orderIdMap);
                returnList.add(WahoIdMap);
                returnList.add(wahoTypeMap);
                object.put("orderId", insOrderObject.getJSONObject("data").getString("id"));
                object.put("wahoId", wahojson.getJSONObject("data").getJSONObject("Warehouse").getString("id"));
                object.put("wahoType", wahojson.getJSONObject("data").getJSONObject("Warehouse").getString("wahoType"));
                object.put("appId", ZkPropertyUtil.get("corpId"));
                object.put("returnList", returnList);
                ject.put("result", "true");
                ject.put("info", object);

                System.out.println("------ject.toString();-----"+ject.toString());
            }else{
                System.out.println("--------请求--Warehouse/getWarehouseInfo 报错------------");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ject.toString();
    }

    /**
     * 修改订单状态
     * @param req
     * @return
     */

    @RequestMapping(value="updateOrderStatus",produces="application/json; charset=utf-8")
    @ResponseBody
    public String updateOrderStatus(HttpServletRequest req, HttpServletResponse res){
        System.out.println("voucherId========########################======"+req.getParameter("voucherId"));
        res.setCharacterEncoding("UTF-8");
        JSONObject js = new JSONObject();
        String message = null;
        try {
            message = orderService.updateOrderStatus(req.getParameter("orderId"),req.getParameter("startDate"),req.getParameter("endDate"));
            /**
             * 根据订单id查询订单
             */
            Map<String,String> orderMap = new HashMap<String, String>();
            orderMap.put("orderId",req.getParameter("orderId"));
            String orderURL = ZkPropertyUtil.get("BaseUrlMember")+"orderManage/getOrderById";
            JSONObject orderjson = ApiUtils.excutePost(orderURL,null,null,orderMap);
            /**
             * 根据门柜id查询门柜信息
             */
            JSONObject order = orderjson.getJSONObject("data").getJSONObject("Order");
            Map<String,String> wahoceMap = new HashMap<String, String>();
            wahoceMap.put("wahoceId",order.getString("wahoceId"));
            String wahoceURL = ZkPropertyUtil.get("BaseUrlStorage")+"Warehouse/getWarehouseCell";
            JSONObject wahocejson = ApiUtils.excutePost(wahoceURL,null,null,wahoceMap);
            JSONObject warehouseCell = wahocejson.getJSONObject("data").getJSONObject("warehouseCell");
            /**
             * 根据cabinetId 获取 Cabinet 信息
             */
            Map<String,String> cabinetMap = new HashMap<String, String>();
            cabinetMap.put("id",warehouseCell.getString("cabinetId"));
            String cabinetParam = JSON.toJSONString(cabinetMap);
            String cabinetURL = ZkPropertyUtil.get("BaseUrlStorage")+"cabinet/getCabinet";
            JSONObject cabinetObject = ApiUtils.excutePost(cabinetURL,null,null,cabinetMap);
            JSONObject cabinet = cabinetObject.getJSONObject("data").getJSONObject("cabinet");
            System.out.println("cabinet.getCabinetNo().substring(0,2):"+cabinet.getString("no").substring(0,2));

            //设置密码
            if(cabinet.getString("no").substring(0,2).equals("WH")){
                System.out.println("cabinet.getCabinetNo().substring(0,2)2:"+cabinet.getString("no").substring(0,2));
                String SetPadPass =  cabinet.getString("id")+"-"+cabinet.getString("no")+"-"+cabinet.getString("doorNo")+"-"+warehouseCell.getString("number")+"-"+order.getString("custTel")+"-"+req.getParameter("orderId");
                String createPasswordUrl=ZkPropertyUtil.get("BaseUrlLock")+"lock/auto/createPassword";
                System.out.println(createPasswordUrl);
                Map<String,String> createPassworMap = new HashMap<String, String>();
                createPassworMap.put("SetPadPass",SetPadPass);
                JSONObject createPasswordObject = ApiUtils.excutePost(createPasswordUrl,null,null,createPassworMap);
            }
            System.out.println("voucherId========########################======"+ req.getParameter("voucherId"));
            if(req.getParameter("voucherId") != null  && !"".equals(req.getParameter("voucherId"))){
                orderService.modifyVoucherState(req.getParameter("voucherId"),req.getParameter("orderId"));
            }
            if("success".equals(message)){
                message="toSuccess1";
                if (req.getParameter("ver") != null && req.getParameter("ver") != "" && "2".equals(req.getParameter("ver"))) {
                    message="toSuccess2";
                }
            }
            js.put("result", "true");
            js.put("info", message);
        } catch (Exception e) {
            js.put("result", "false");
            js.put("info", "失败");
            e.printStackTrace();
        }

        return js.toString();
    }

    /**
     * 续费前查询order订单 renew.html
     * @param req
     * @return
     */
    @RequestMapping(value="queryOrder",produces="application/json; charset=utf-8")
    @ResponseBody
    public String queryOrder(HttpServletRequest req, HttpServletResponse res){
        System.out.println("---------------------xiugai------");
        JSONObject object = new JSONObject();
        JSONObject ject = new JSONObject();
        String orderId = req.getParameter("orderId");
        Map<String,String> queryOrderMap = new HashMap<String, String>();
        queryOrderMap.put("orderId",req.getParameter("orderId"));
        String orderURL = ZkPropertyUtil.get("BaseUrlMember")+"orderManage/getOrderInfo";
        try {
            JSONObject orderJson = ApiUtils.excutePost(orderURL,null,null,queryOrderMap);
            if(orderJson.getIntValue("code")==10000){
                JSONObject order = orderJson.getJSONObject("data");
                Map<String,String>  warehouseMap  = new HashMap<String, String>();
                warehouseMap.put("wahoId",order.getString("wahoId"));
                String warehouseURL = ZkPropertyUtil.get("BaseUrlStorage")+"Warehouse/getWarehouseInfo";
                JSONObject warehousejson = ApiUtils.excutePost(warehouseURL,null,null,warehouseMap);
                if(warehousejson.getIntValue("code")==10000){
                    JSONObject warehouse = warehousejson.getJSONObject("data").getJSONObject("Warehouse");
                    Map<String,String> priceMap = new HashMap<String, String>();
                    priceMap.put("wahoId",order.getString("wahoId"));
                    String priceURL = ZkPropertyUtil.get("BaseUrlStorage")+"wahoce/getImmediateWahocePrice";
                    JSONObject pricejson = ApiUtils.excutePost(priceURL,null,null,priceMap);

                    JSONObject  priceList = pricejson.getJSONObject("data");

                    System.out.println("----priceList-----"+priceList);
                    if(pricejson.getIntValue("code")==10000){
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String orderEndDate = order.getString("orderEndDate");
                        String endDates = null;
                        Date date = dateFormat.parse(orderEndDate);
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(date);
                        long end =System.currentTimeMillis()- calendar.getTimeInMillis();
                        System.out.println("end:"+end);
                        long endDate=end/1000/60;
                        //单位小时
                        if("1".equals(order.getString("unit"))){
                            if(endDate<60){
                                object.put("orderEndDate", endDate+"分钟");
                                object.put("minorderEndDate", endDate/60+1);
                                endDates=renewTimeUtil.timeConversion(1,orderEndDate);
                            }else if(endDate>60){
                                long endDates1 = endDate%60;
                                long endDates2 = endDate/60;
                                object.put("orderEndDate", endDates2+"小时"+endDates1+"分钟");
                                object.put("minorderEndDate", endDates2+1);
                                endDates=renewTimeUtil.timeConversion((int)(endDates2+1),orderEndDate);
                            }
                        }
                        //天
                        if("2".equals(order.getString("unit"))){
                            if(endDate/60+1<24){
                                object.put("orderEndDate", endDate/60+1+"小时");
                                object.put("minorderEndDate", endDate/60/24+1);
                                endDates=renewTimeUtil.timeConversion(24,orderEndDate);
                            }else{
                                object.put("orderEndDate", endDate/60/24+"天"+((endDate/60)-(endDate/60/24)*24)/60+1+"小时");
                                object.put("minorderEndDate", endDate/60/24+1);
                                endDates=renewTimeUtil.timeConversion((int)(endDate/60+1),orderEndDate);
                            }
                        }
                        //月
                        if("3".equals(order.getString("unit"))){
                            Map<String,String> map = renewTimeUtil.renewTime(orderEndDate);
                            if(map.get("renewMonth")!="0" && map.get("OverDay")!="0"){
                                object.put("orderEndDate", Integer.parseInt(map.get("renewMonth"))-1+"个月"+map.get("OverDay")+"天");
                                object.put("minorderEndDate", map.get("renewMonth"));
                            }
                            if(map.get("renewMonth")!="0" && map.get("OverDay")=="0"){
                                object.put("orderEndDate", map.get("renewMonth")+"个月");
                                object.put("minorderEndDate", map.get("renewMonth")+1);
                            }
                            endDates=renewTimeUtil.timeConversion(Integer.parseInt(map.get("lengthdays"))*24,orderEndDate);
                        }
                        String appId =ZkPropertyUtil.get("corpId");
                        System.out.println("appId:"+appId);
                        object.put("appId", appId);
                        System.out.println("----order------"+order.toString());
                        object.put("order", order);
                        object.put("Warehouse", warehouse);
                        object.put("priceList", priceList);
                        System.out.println("priceList:"+priceList.toString());
                        object.put("minEndDates", endDates);
                        ject.put("result", "true");
                        ject.put("info", object);
                        System.out.println("-------ject----------："+ject.toString());
                    }else{
                        System.out.println("--------queryOrder---------查询价格------------失败-------");
                    }
                }else{
                    System.out.println("--------queryOrder-------Warehouse/getWarehouseInfo-----失败-------");
                }
            }else{
                System.out.println("--------queryOrder-------orderManage/getOrderInfo-----失败-------");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ject.toString();
    }
    //续费选时
    @RequestMapping(value="coverSelectTime",produces="application/json; charset=utf-8")
    @ResponseBody
    public String coverSelectTime(HttpServletRequest req, HttpServletResponse res){
        JSONObject object = new JSONObject();
        JSONObject ject = new JSONObject();
        try {
            String unit = req.getParameter("unit");
            String minEndDates = req.getParameter("minEndDates");
            String selectTime = req.getParameter("selectTime");
            String minEndTime = req.getParameter("minEndTime");
            Map<String,String> map = renewTimeUtil.renewSelectTime(unit, minEndDates,String.valueOf(Integer.valueOf(selectTime)-Integer.valueOf(minEndTime)) );
            object.put("selectEndDate", map.get("selectEndDate"));
            ject.put("result", "true");
            ject.put("info", object);
        } catch (NumberFormatException e) {
            ject.put("result", "false");
            ject.put("info", object);
            e.printStackTrace();
        }
        return ject.toString();
    }
    /**
     * 新增续柜
     */
    @RequestMapping(value="addContinuCabView",produces="application/json; charset=utf-8")
    @ResponseBody
    public String addContinuCabView(HttpServletRequest req, HttpServletResponse res){
        JSONObject object = new JSONObject();
        JSONObject ject = new JSONObject();
        String orderId = req.getParameter("orderId");
        Map<String,String> queryOrderMap = new HashMap<String, String>();
        queryOrderMap.put("orderId",req.getParameter("orderId"));
        String orderURL = ZkPropertyUtil.get("BaseUrlMember")+"orderManage/getOrderInfo";
        JSONObject orderJson =null;
        try {
            orderJson = ApiUtils.excutePost(orderURL,null,null,queryOrderMap);
            if(orderJson.getIntValue("code")==10000){
                JSONObject order = orderJson.getJSONObject("data");
                Map<String,String>  warehouseMap  = new HashMap<String, String>();
                warehouseMap.put("wahoId",order.getString("wahoId"));
                String warehouseURL = ZkPropertyUtil.get("BaseUrlStorage")+"Warehouse/getWarehouseInfo";
                JSONObject warehousejson = ApiUtils.excutePost(warehouseURL,null,null,warehouseMap);
                if(warehousejson.getIntValue("code")==10000){
                    String appId = ZkPropertyUtil.get("corpId");
                    Map<String,String> priceMap = new HashMap<String, String>();
                    priceMap.put("wahoId",order.getString("wahoId"));
                    String priceURL = ZkPropertyUtil.get("BaseUrlStorage")+"wahoce/getImmediateWahocePrice";
                    JSONObject pricejson = ApiUtils.excutePost(priceURL,null,null,priceMap);
                    JSONObject priceList = pricejson.getJSONObject("data");
                    System.out.println("-----priceList-----"+priceList.toString());
                    if(pricejson.getIntValue("code")==10000){

                        JSONObject warehouse = warehousejson.getJSONObject("data").getJSONObject("Warehouse");
                        object.put("priceList", pricejson);
                        object.put("waceSize", order.getString("waceSize"));
                        object.put("wahoName", order.getString("wahoName"));
                        object.put("waceNumber", order.getString("waceNumber"));
                        object.put("custName", order.getString("name"));
                        object.put("custTel", order.getString("tel"));
                        object.put("unit", order.getString("unit"));
                        object.put("hourRenews", warehouse.getString("hourRenew"));
                        object.put("dayRenews", warehouse.getString("dayRenew"));
                        object.put("monthRenews", warehouse.getString("monthRenew"));
                        object.put("warehouse", warehouse);
                        object.put("appId", appId);
                        object.put("orderEndDate",order.getString("orderEndDate"));
                        ject.put("result", "true");
                        ject.put("info", object);
                    }else{
                        System.out.println("--------addContinuCabView---------查询价格------------失败-------");
                    }
                }else{
                    System.out.println("--------addContinuCabView-------Warehouse/getWarehouseInfo-----失败-------");
                }
            }else{
                System.out.println("--------addContinuCabView-------orderManage/getOrderInfo-----失败-------");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ject.toString();
    }

    //续租
    @RequestMapping(value="continueWarehouse",produces="application/json; charset=utf-8")
    @ResponseBody
    public String continueWarehouse(HttpServletRequest req, HttpServletResponse res){
        String orderId = req.getParameter("orderId");
        String rentalTime = req.getParameter("rentalTime");
        String total = req.getParameter("total");
        System.out.println("----------total---------："+total);
        String endDate = req.getParameter("endDate");
        String unit = req.getParameter("unit");
        String number = req.getParameter("number");
        JSONObject js=new JSONObject();
        try {
            js = orderService.continueWarehouse( orderId, rentalTime, total,endDate,unit,number);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return js.toString();
    }


    //点击待支付校验待支付订单
    @RequestMapping(value="getOrder",produces="application/json; charset=utf-8")
    @ResponseBody
    public String getOrder(HttpServletRequest req, HttpServletResponse res){
        JSONObject object = new JSONObject();
        JSONObject ject = new JSONObject();
        try {
            Map<String,String> orderMap = new HashMap<String, String>();
            orderMap.put("orderId",req.getParameter("orderId"));
            String orderURL = ZkPropertyUtil.get("BaseUrlMember")+"orderManage/getOrderById";
            JSONObject orderJson = ApiUtils.excutePost(orderURL,null,null,orderMap);
            if(orderJson.getIntValue("code")==10000){
                object.put("order",orderJson.getJSONObject("data"));
            }else{
                object.put("order","Order为空");
                System.out.println("----------getOrder----请求失败-----");
            }
            ject.put("result", "true");
            ject.put("info", object);
        } catch (Exception e) {
            ject.put("result", "false");
            ject.put("info", "查询订单失败!");
            e.printStackTrace();
        }
        return ject.toString();
    }

    /**
     * 退柜/退仓
     */
    @RequestMapping(value="returnWarehouse",produces="application/json; charset=utf-8")
    @ResponseBody
    public String returnWarehouse(HttpServletRequest req, HttpServletResponse res){
        JSONObject js = null;
        try {
            js=orderService.returnWarehouse(req.getParameter("orderId"), req.getParameter("startDate"),req.getParameter("endDate"));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return js.toString();
    }
    /**
     * 优惠券的使用
     */
    @RequestMapping(value="modifyVoucherState",produces="application/json; charset=utf-8")
    @ResponseBody
    public String modifyVoucherState(HttpServletRequest req, HttpServletResponse res){
        Map<String,String> upVouchermap = new HashMap<String,String>();
        JSONObject updVoucherObject = null;
        try {
            upVouchermap.put("id",req.getParameter("voucherId"));
            upVouchermap.put("orderId",req.getParameter("orderId"));
            upVouchermap.put("status","4");
            Date now = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            upVouchermap.put("usedTime",dateFormat.format(now));
            String updVoucher = JSON.toJSONString(upVouchermap);
            String updVoucherURL = ZkPropertyUtil.get("BaseUrlMember")+"voucherManage/updByVoucher";
            updVoucherObject = ApiUtils.excutePost(updVoucherURL,null,updVoucher,null);
        } catch (Exception e) {
            System.out.println("----------voucherManage/updByVoucher----请求失败-----"+updVoucherObject.toString());
            e.printStackTrace();
        }
        return updVoucherObject.toString();
    }

}
