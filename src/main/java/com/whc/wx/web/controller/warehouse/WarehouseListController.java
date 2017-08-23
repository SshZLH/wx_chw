package com.whc.wx.web.controller.warehouse;

import com.wanhuchina.common.util.http.HttpPostUtils;
import com.wanhuchina.common.util.http.ApiUtils;
import com.wanhuchina.common.util.weixin.cgi.WeixinUtil;
import com.wanhuchina.common.util.zk.ZkPropertyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import  com.alibaba.fastjson.JSONObject;
//import net.sf.json.JSONObject;

/**
 * Created by WangShengZhan
 * Email：WangShengZhan@wanhuchina.com
 * Date：2017/6/20
 * Time：11:22
 */
@Controller
public class WarehouseListController {
    private static final Logger logger= LoggerFactory.getLogger(WarehouseListController.class);


    /**
     * 获取WarehouseList的集合
     * parameter:keyWord wahoType
     * return:String
     */
    @RequestMapping(value="getWarehouseList",produces="application/json; charset=utf-8")
    @ResponseBody
    public String getWarehouseList(HttpServletRequest req){
        String keyWord = req.getParameter("keyWord");
        String wahoType = req.getParameter("wahoType");
        logger.info("-----getWarehouseList----keyWord-"+keyWord+"----wahoType----"+wahoType);
        String  send = null;
        JSONObject json=null;
        String URL = ZkPropertyUtil.get("BaseUrlStorage")+"Warehouse/getWarehouseList";
        Map<String,String> warehouseMap = new HashMap<String, String>();
        warehouseMap.put("keyWord",keyWord);
        warehouseMap.put("wahoType",wahoType);

        try {
            json =  ApiUtils.excutePost(URL,null,null,warehouseMap);
            send = json.toString();
            logger.info("----send----:"+send);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return send;
    }





    /**
     * 获取WarehouseList的集合
     * parameter:keyWord wahoType
     * return:String
     */
    @RequestMapping(value="getStorehouseList",produces="application/json; charset=utf-8")
    @ResponseBody
    public String getStorehouseList(HttpServletRequest req){
        String keyWord = req.getParameter("keyWord");
        String wahoType = req.getParameter("wahoType");
        if("null".equals(keyWord)){
            keyWord="";
        }
        logger.info("-----getWarehouseList----keyWord-"+keyWord+"----wahoType----"+wahoType);
        String  send = null;
        JSONObject json=null;
        String URL = ZkPropertyUtil.get("BaseUrlStorage")+"Warehouse/getWarehouseList";
        Map<String,String> warehouseMap = new HashMap<String, String>();
        warehouseMap.put("keyWord",keyWord);
        warehouseMap.put("wahoType",wahoType);
        try {
            json =  ApiUtils.excutePost(URL,null,null,warehouseMap);
            send = json.toString();
            logger.info("----send----:"+send);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return send;
    }
    /**
     * 获取timestamp,nonceStr,signature的签名
     *  parameter:url
     *  return:String
     */
    @RequestMapping(value="getSignature",produces="application/json; charset=utf-8")
    @ResponseBody
    public String getSignature(HttpServletRequest req){
        JSONObject res = null;

        String url = req.getParameter("url");
        logger.info("-----getSignature----url-"+url);
        String send= null;
        res = new JSONObject();
        String appId = ZkPropertyUtil.get("corpId");
        String secret = ZkPropertyUtil.get("secret");
        System.out.println("secret:"+secret+"===appId==="+appId);
        Map<String, String> configMap = WeixinUtil.getJsConfigMap(url, appId, secret);
        res.put("supportUrl", ZkPropertyUtil.get("supportURL"));//supportUrl
        res.put("baseURL", ZkPropertyUtil.get("baseURL"));//baseURL
        res.put("appId", appId);
        res.put("timestamp", configMap.get("timestamp"));
        res.put("nonceStr", configMap.get("nonceStr"));
        res.put("signature", configMap.get("signature"));
        logger.info("-----getSignature----url-"+res);
        send = res.toString();
        return send;
    }

    /**
     * 获取getWarehouseCellList
     * @param req
     * @return String 
     */
    @RequestMapping(value="getWarehouseCellList",produces="application/json; charset=utf-8")
    @ResponseBody
    public String getWarehouseCellList(HttpServletRequest req){
        String send = null;
        JSONObject json=null;
        String wahoId = req.getParameter("wahoId");
        logger.info("-----getWarehouseCellList----wahoId-"+wahoId);
        Map<String,String> wahoMap = new HashMap<String, String>();
        wahoMap.put("wahoId",wahoId);
        String URL = ZkPropertyUtil.get("BaseUrlStorage")+"Warehouse/getWarehouseCellInfo";
        //send = HttpPostUtils.httpPost(URL,null,null,null,wahoMap,"UTF-8");
        try {
            json =  ApiUtils.excutePost(URL,null,null,wahoMap);
            send =  json.toString();
            logger.info("-----getWarehouseCellList----send-"+send);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return send;
    }

    /**
     * 获取选柜信息getWahoceCallInfo
     * @param req
     * @return
     */
    @RequestMapping(value="getWahoceCallInfo",produces="application/json; charset=utf-8")
    @ResponseBody
    public String getWahoceCallInfo(HttpServletRequest req){
        String send = null;
        JSONObject json=null;
        String wahoceId = req.getParameter("wahoceId");
        String openId = req.getParameter("openId");
        String wahoId = req.getParameter("wahoId");
        logger.info("-----getWarehouseCellList----wahoceId-"+wahoceId+"--openId--"+openId+"---wahoId--"+wahoId);
        Map<String,String> WahoceCallInfoMap = new HashMap<String, String>();
        WahoceCallInfoMap.put("wahoceId",wahoceId);
        WahoceCallInfoMap.put("openId",openId);
        WahoceCallInfoMap.put("wahoId",wahoId);
        String URL = ZkPropertyUtil.get("BaseUrlStorage")+"Warehouse/getWahoce";
        try {
            json =  ApiUtils.excutePost(URL,null,null,WahoceCallInfoMap);
            send =  json.toString();
            logger.info("-----getWahoceCallInfo----send-"+send);
        } catch (Exception e) {
            logger.info("-----getWahoceCallInfo---e:--"+e.getStackTrace());
            e.printStackTrace();
        }
        return send;


    }
    /**
     * 获取选柜信息getOrderInformation
     * @param req
     * @return
     */
    @RequestMapping(value="getOrderInformation",produces="application/json; charset=utf-8")
    @ResponseBody
    public String getOrderInformation(HttpServletRequest req){
        String basePath = req.getScheme()+"://"+req.getServerName();
        String baseUrl = ZkPropertyUtil.get("baseURL");
        String appId = ZkPropertyUtil.get("corpId");
        logger.info("-----getOrderInformation----orderId-"+req.getParameter("orderId")+"--baseUrl--"+ZkPropertyUtil.get("baseURL")+"---basePath--"+req.getScheme()+"://"+req.getServerName());
        JSONObject res = new JSONObject();
        JSONObject Jsonres = new JSONObject();
        Map<String,String> bascMap = new HashMap<String,String>();
        bascMap.put("orderId",req.getParameter("orderId"));
        String orderURL = ZkPropertyUtil.get("BaseUrlMember")+"memberCenter/getOrderDetail";
        JSONObject orderjson =null;
        try {
            orderjson =  ApiUtils.excutePost(orderURL,null,null,bascMap);
            if(orderjson.getIntValue("code")==10000){
                String orderRenewInfoURL =  ZkPropertyUtil.get("BaseUrlMember")+"orderManage/selOrderRunningWater";
                JSONObject orderRenewInfoJson =  ApiUtils.excutePost(orderRenewInfoURL,null,null,bascMap);
                if(orderRenewInfoJson.getIntValue("code")==10000){
                    String weigateCabinetRecordURL =  ZkPropertyUtil.get("BaseUrlStorage")+"cabinet/getWeigateCabinetRecordList";
                    JSONObject weigateCabinetRecordJson =  ApiUtils.excutePost(weigateCabinetRecordURL,null,null,bascMap);
                    if(weigateCabinetRecordJson.getIntValue("code")==10000){


                        res.put("orderInfo", orderjson);//订单详情列表
                        res.put("weigateCabinetRecordList", weigateCabinetRecordJson.getJSONObject("data"));//开门记录列表
                        res.put("orderRenewInfoList", orderRenewInfoJson);//付款信息列表
                        System.out.println("orderRenewInfoJson:;;;;"+orderRenewInfoJson.toString());
                        res.put("baseUrl", baseUrl);
                        res.put("basePath", basePath);
                        res.put("appId", appId);
                        Jsonres.put("result", "true");
                        Jsonres.put("info", res);

                    }else{
                        System.out.println("--------------orderRenewInfoJson------------------"+orderRenewInfoJson.getIntValue("code"));
                        logger.info("-----getWarehouseCellList----orderId-"+req.getParameter("orderId")+"--baseUrl--"+ZkPropertyUtil.get("baseURL")+"---basePath--"+req.getScheme()+"://"+req.getServerName());

                    }
                }else{
                    System.out.println("--------------orderRenewInfoJson------------------"+orderRenewInfoJson.getIntValue("code"));
                }
            }else{
                System.out.println("--------------orderjson------------------"+orderjson.getIntValue("code"));
            }
        } catch (Exception e) {
            Jsonres.put("result", "false");
            Jsonres.put("info", "获取出错");
            e.printStackTrace();
        }
        return Jsonres.toString();
    }

}
