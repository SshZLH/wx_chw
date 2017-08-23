package com.whc.wx.web.controller.openDoor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wanhuchina.common.code.CommonCode;
import com.wanhuchina.common.code.TxResultResponse;
import com.wanhuchina.common.util.date.DateUtil;
import com.wanhuchina.common.util.http.ApiUtils;
import com.wanhuchina.common.util.zk.ZkPropertyUtil;
import com.whc.wx.web.util.OpenDoorUtils;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by shenshanghua
 * Email shenshanghua@wanhuchina.com
 * Date：2017/6/21
 * Time：13:41
 */
@RestController
@RequestMapping("/openDoor")
public class openDoorController {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(openDoorController.class);

    @RequestMapping(value = "/openDoorByOrderId", method = RequestMethod.GET)
    public TxResultResponse openDoorByOrderId(@RequestParam String orderId

    ) {

        TxResultResponse resultResponse = new TxResultResponse();
        Map<String, String> orderParamsMap = new HashMap<String, String>();
        orderParamsMap.put("orderId", orderId);
        Map<String, String> wareCellParamsMap = new HashMap<String, String>();
        Map<String, String> cabinetParamsMap = new HashMap<String, String>();

        Map<String, String> memberParamsMap = new HashMap<String, String>();

        JSONObject JSONorder = null;
        JSONObject JSONwarehouseCell = null;
        JSONObject JSONmember = null;
        try {
            JSONorder = ApiUtils.excutePost(ZkPropertyUtil.get("BaseUrlMember") + "orderManage/getOrderById", null, null, orderParamsMap);
            if(JSONorder.getInteger("code")==10000){
                wareCellParamsMap.put("wahoceId",JSONorder.getJSONObject("data").getJSONObject("Order").getString("wahoceId"));
                JSONwarehouseCell = ApiUtils.excutePost(ZkPropertyUtil.get("BaseUrlStorage") + "Warehouse/getWarehouseCell", null, null, wareCellParamsMap);
                cabinetParamsMap.put("id",JSONwarehouseCell.getJSONObject("data").getJSONObject("warehouseCell").getString("cabinetId"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        try {


            if (JSONorder.getInteger("code")==10000) {//order不为空
                if ("1".equals(JSONorder.getJSONObject("data").getJSONObject("Order").getString("status"))) {//
                    Calendar calendar1 = Calendar.getInstance();
                    Calendar calendar = Calendar.getInstance();
                    long nowtime = 0;
                    SimpleDateFormat dateFormat = null;
                    if ("8".endsWith(JSONwarehouseCell.getJSONObject("data").getJSONObject("warehouseCell").getString("type")) || "10".endsWith(JSONwarehouseCell.getJSONObject("data").getJSONObject("warehouseCell").getString("type"))) {
                        nowtime = System.currentTimeMillis();
                        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    } else {
                        nowtime = System.currentTimeMillis() / 1000 / 3600 / 24 * 24 * 3600 * 1000 - 8 * 3600 * 1000;
                        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    }
                    try {
                        calendar.setTime(dateFormat.parse(JSONorder.getJSONObject("data").getJSONObject("Order").getString(("endDate"))));
                        calendar1.setTime(dateFormat.parse(JSONorder.getJSONObject("data").getJSONObject("Order").getString(("startDate"))));
                    } catch (ParseException e) {
                        return new TxResultResponse(CommonCode.PARAM_ERROR.getCode(), "时间转换出错");
                    }
                    if (nowtime >= calendar1.getTimeInMillis() && nowtime <= calendar.getTimeInMillis()) {
                        log.debug(String.valueOf(System.currentTimeMillis()));
                        log.debug(String.valueOf(calendar1.getTimeInMillis()));
                        if ("8,10".contains(JSONwarehouseCell.getJSONObject("data").getJSONObject("warehouseCell").getString("type"))) {
                            JSONObject JSONcabinet = ApiUtils.excutePost(ZkPropertyUtil.get("BaseUrlStorage") + "/cabinet/getCabinet", null, null, cabinetParamsMap);
                            wareCellParamsMap.put("wahoceId",JSONorder.getJSONObject("data").getJSONObject("Order").getString("wahoceId"));
                            JSONwarehouseCell = ApiUtils.excutePost(ZkPropertyUtil.get("BaseUrlStorage") + "Warehouse/getWarehouseCell", null, null, wareCellParamsMap);
                            memberParamsMap.put("memberId",JSONorder.getJSONObject("data").getJSONObject("Order").getString("memberId"));
                            JSONmember = ApiUtils.excutePost(ZkPropertyUtil.get("BaseUrlMember")+"memberManage/getMemberByMemId",null,null,memberParamsMap);
                            if(JSONcabinet!=null){
                                Map<String,String>  para = new HashMap<String, String>();
                                para.put("id",JSONorder.getJSONObject("data").getJSONObject("Order").getString("id"));
                                para.put("cabinetId",JSONcabinet.getJSONObject("data").getJSONObject("cabinet").getString("id"));
                                para.put("createTime", DateUtil.fromDate(new Date()));
                                para.put("cabinetStatus",JSONcabinet.getJSONObject("data").getJSONObject("cabinet").getString("cabinetStatus"));
                                para.put("wahoceId",JSONcabinet.getJSONObject("data").getJSONObject("cabinet").getString("wahoceId"));
                                para.put("operator",JSONorder.getJSONObject("data").getJSONObject("Order").getString("custName"));
                                String jsonParams = JSON.toJSONString(para);
                                JSONObject jsonRecordResult = ApiUtils.excutePost(ZkPropertyUtil.get("BaseUrlStorage") + "cabinet/getCabinetRecordId", null, jsonParams, null);//这边的返回值是新增的开门记录
                                if(jsonRecordResult.getInteger("code") == 10000){
                                    //请求开门的参数
                                    String originalDateMapStr=JSONcabinet.getJSONObject("data").getJSONObject("cabinet").getString("id")+"-"+JSONcabinet.getJSONObject("data").getJSONObject("cabinet").getString("no")+"-"+JSONcabinet.getJSONObject("data").getJSONObject("cabinet").getString("doorNo")+"-"+jsonRecordResult.getJSONObject("data").getString("id");
                                    String boardNo =JSONcabinet.getJSONObject("data").getJSONObject("cabinet").getString("no").substring(0, 2);
                                    String string = JSONcabinet.getJSONObject("data").getJSONObject("cabinet").getString("status");
                                    if("0".equals(JSONcabinet.getJSONObject("data").getJSONObject("cabinet").getString("status"))){
                                        //如果门柜是空闲状态，则先占用
                                        cabinetParamsMap = new HashMap<String, String>();
                                        cabinetParamsMap.put("cabinetId",JSONcabinet.getJSONObject("data").getJSONObject("cabinet").getString("id"));
                                        cabinetParamsMap.put("status","1");
                                        ApiUtils.excutePost(ZkPropertyUtil.get("BaseUrlStorage")+"cabinet/updateCabinetStatus",null,null,cabinetParamsMap);
                                        //经过开门的工具类
                                        TxResultResponse resultResponse1 = OpenDoorUtils.openDoor(boardNo, originalDateMapStr,JSONwarehouseCell,JSONmember);
                                        Map<String,String> dataMap  = new HashMap<String, String>();
                                        dataMap.put("cabinetDoorNo",JSONwarehouseCell.getJSONObject("data").getJSONObject("warehouseCell").getString("number"));
                                        dataMap.put("orderId",orderId);
                                        resultResponse1.setData(dataMap);
                                        resultResponse  =  resultResponse1;
                                        return resultResponse;

                                    }else{
                                        TxResultResponse resultResponse1 = OpenDoorUtils.openDoor(boardNo, originalDateMapStr,JSONwarehouseCell,JSONmember);
                                        Map<String,String> dataMap  = new HashMap<String, String>();
                                        dataMap.put("cabinetDoorNo",JSONwarehouseCell.getJSONObject("data").getJSONObject("warehouseCell").getString("number"));
                                        dataMap.put("orderId",orderId);
                                        resultResponse1.setData(dataMap);
                                        resultResponse  =  resultResponse1;

                                        resultResponse  =  resultResponse1;

                                        return resultResponse;
                                    }
                                }else{
                                    return new TxResultResponse(CommonCode.PARAM_ERROR.getCode(),"添加开门记录失败！");
                                }


                            }
                        }
                    }
                } else {

                }
                return resultResponse;

            }else{
                return new TxResultResponse(CommonCode.PARAM_ERROR.getCode(),CommonCode.PARAM_ERROR.getMsg());
            }
        } catch (Exception e) {
            return new TxResultResponse(CommonCode.SERVER_ERROR.getCode(),CommonCode.SERVER_ERROR.getMsg());
        }
    }



}