package com.whc.wx.web.controller.memberCenter;

import com.alibaba.fastjson.JSONObject;
import com.wanhuchina.common.util.http.ApiUtils;
import com.wanhuchina.common.util.http.HttpPostUtils;
import com.wanhuchina.common.util.http.HttpGetUtils;
import com.wanhuchina.common.util.zk.ZkPropertyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by WangShengZhan
 * Email：WangShengZhan@wanhuchina.com
 * Date：2017/6/20
 * Time：16:10
 */
@Controller
public class memberCenterController {
    private static final Logger logger= LoggerFactory.getLogger(memberCenterController.class);
    /**
     * 订单中心获取 order 当前订单和历史订单
     * @param req (openId,memberId,status)
     * @return String
     */
    @RequestMapping(value="getOrderList",produces="application/json; charset=utf-8")
    @ResponseBody
    public String getOrderList(HttpServletRequest req){
        String send = null;
        JSONObject json=null;
        String openId = req.getParameter("openId");
        String memberId = req.getParameter("memberId");
        String status = req.getParameter("status");
        logger.info("-----getOrderList----openId-"+openId+"----memberId----"+memberId+"----status----"+status);
        Map<String,String> orderMap = new HashMap<String, String>();
        orderMap.put("openId",openId);
        orderMap.put("memberId",memberId);
        orderMap.put("status",status);
        String URL = ZkPropertyUtil.get("BaseUrlMember")+"memberCenter/getMemCenterList";
        //send = HttpGetUtils.httpGet(URL,null,orderMap,"UTF-8");
        //send = HttpPostUtils.httpPost(URL,null,null,null,orderMap,"UTF-8");
        try {
            json = ApiUtils.excuteGet(URL,null,orderMap);
            send = json.toString();
            System.out.println("---getOrderList---send---"+send);
            logger.info("--getOrderList--send----:"+send);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return send;
    }
    /**
     * 订单中心获取 voucherList (优惠券)
     * @param req (memberId,units,warehouseId)
     * @return String
     */
    @RequestMapping(value="getVoucherList",produces="application/json; charset=utf-8")
    @ResponseBody
    public String getVoucherList(HttpServletRequest req){
        String send = null;
        JSONObject json=null;
        String memberId = req.getParameter("memberId");
        String units = req.getParameter("units");
        String warehouseId = req.getParameter("warehouseId");
        logger.info("-----getVoucherList----memberId-"+memberId+"----units----"+units+"----warehouseId----"+warehouseId);
        Map<String,String> voucherMap = new HashMap<String, String>();
        voucherMap.put("memberId",memberId);
        voucherMap.put("units",units);
        voucherMap.put("warehouseId",warehouseId);
        String URL = ZkPropertyUtil.get("BaseUrlMember")+"memberCenter/getMemVoucherList";
        //send = HttpGetUtils.httpGet(URL,null,voucherMap,"UTF-8");
        //send = HttpPostUtils.httpPost(URL,null,null,null,voucherMap,"UTF-8");
        try {
            json = ApiUtils.excutePost(URL,null,null,voucherMap);
            send = json.toString();
            logger.info("--getVoucherList--send----:"+send);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return send;
    }
    /**
     * 订单中心 bindVoucher 绑定优惠券
     * @param req (voucherCode,memberId,openId)
     * @return String 返回 "10000" 为成功
     */
    @RequestMapping(value="bindVoucher",produces="application/json; charset=utf-8")
    @ResponseBody
    public String bindVoucher(HttpServletRequest req){
        String send = null;
        JSONObject json=null;
        String voucherCode = req.getParameter("voucherCode");
        String memberId = req.getParameter("memberId");
        String openId = req.getParameter("openId");
        logger.info("-----bindVoucher----memberId-"+memberId+"----voucherCode----"+voucherCode+"----openId----"+openId);
        Map<String,String> bindVoucherMap = new HashMap<String, String>();
        bindVoucherMap.put("voucherCode",voucherCode);
        bindVoucherMap.put("memberId",memberId);
        bindVoucherMap.put("openId",openId);
        String URL = ZkPropertyUtil.get("BaseUrlMember")+"memberCenter/bindVoucher";
        //send = HttpGetUtils.httpGet(URL,null,bindVoucherMap,"UTF-8");
        //send = HttpPostUtils.httpPost(URL,null,null,null,bindVoucherMap,"UTF-8");

        try {
            json = ApiUtils.excutePost(URL,null,null,bindVoucherMap);
            send = json.toString();
            logger.info("--bindVoucher--send----:"+send);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return send;
    }
    /**
     * 订单中心 delHistoryOrder 删除历史订单
     * @param req (orderId)
     * @return String 返回 "10000" 为成功
     */
    @RequestMapping(value="delHistoryOrder",produces="application/json; charset=utf-8")
    @ResponseBody
    public String delHistoryOrder(HttpServletRequest req){
        String send = null;
        JSONObject json=null;
        String orderId = req.getParameter("orderId");
        logger.info("-----delHistoryOrder----orderId-"+orderId);
        Map<String,String> delOrderMap = new HashMap<String, String>();
        delOrderMap.put("orderId",orderId);
        String URL = ZkPropertyUtil.get("BaseUrlMember")+"memberCenter/DelHistoryOrder";
        //send = HttpGetUtils.httpGet(URL,null,delOrderMap,"UTF-8");
        //send = HttpPostUtils.httpPost(URL,null,null,null,delOrderMap,"UTF-8");
        try {
            json = ApiUtils.excutePost(URL,null,null,delOrderMap);
            send = json.toString();
            logger.info("--delHistoryOrder--send----:"+send);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return send;
    }
    /**
     * 获取订单详情 getOrderDetail
     * @param req (orderId)
     * @return String
     */
    @RequestMapping(value="getOrderDetail",produces="application/json; charset=utf-8")
    @ResponseBody
    public String getOrderDetail(HttpServletRequest req){
        String send = null;
        JSONObject json=null;
        String orderId = req.getParameter("orderId");
        logger.info("-----getOrderDetail----orderId-"+orderId);
        Map<String,String> orderDetailMap = new HashMap<String, String>();
        orderDetailMap.put("orderId",orderId);
        String URL = ZkPropertyUtil.get("BaseUrlMember")+"getOrderDetail/getOrderDetail";
        //send = HttpGetUtils.httpGet(URL,null,orderDetailMap,"UTF-8");
        //send = HttpPostUtils.httpPost(URL,null,null,null,orderDetailMap,"UTF-8");
        try {
            json = ApiUtils.excutePost(URL,null,null,orderDetailMap);
            send = json.toString();
            logger.info("---getOrderDetail-send----:"+send);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return send;
    }
}
