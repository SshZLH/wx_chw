package com.whc.wx.web.controller.warehouse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


import com.wanhuchina.common.util.http.ApiUtils;
import com.wanhuchina.common.util.weixin.cgi.WeixinUtil;
import com.wanhuchina.common.util.zk.ZkPropertyUtil;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by WangShengZhan
 * Email：WangShengZhan@wanhuchina.com
 * Date：2017/6/9
 * Time：11:30
 * function:微信扫码 用户扫码时是否关注公众号，是，进入门店列表，否，跳到微信关注页
 * type：Servlet接口
 */
public class IntelligentWarehouseAction extends HttpServlet{
    private static final Logger logger= LoggerFactory.getLogger(IntelligentWarehouseAction.class);
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.info("-------微信扫码----------");
        String view = "redirect:about:blank";
        String code = request.getParameter("code");
        String openId = "";
        String warehouseId = request.getParameter("warehouseId");
        String BaseURL = "";
        try {
            /**
             * 判断code
             */
            if (code !=null && !"authdeny".equals(code)) {
                /**
                 *获取accessToken
                 */
                JSONObject accessToken = getAccessToken(code);
                try {
                    openId = accessToken.getString("openid");
                } catch (Exception e) {
                    e.printStackTrace();
                    request.getRequestDispatcher(view).forward(request, response);
                    return;
                }
                logger.info("-------openId----"+openId+"------warehouseId----"+warehouseId);
                /**
                 * 请求会员系统，获取会员信息
                 * 有会员，进入门店
                 * 无会员，进入关注页
                 */
                BaseURL = ZkPropertyUtil.get("BaseUrlMember")+"memberManage/getMember";
                Map<String,String> memberMap = new HashMap<String, String>();
                memberMap.put("openId",openId);
                memberMap.put("isSub","1");
                JSONObject memberjson = new JSONObject();
                memberjson = ApiUtils.excuteGet(BaseURL,null,memberMap);
                logger.info("-------memberjson----"+memberjson.toString());
                if(memberjson.getIntValue("code")==10000){
                    JSONObject datajson = memberjson.getJSONObject("data");
                    if(!"true".equals(datajson.getString("isMember"))){
                        view =ZkPropertyUtil.get("baseURL")+"web/whc/warehouse/showQrcode.jsp?warehouseId="+warehouseId;
                    }else{
                        /**
                         * 请求查看用户是否有订单
                         * 有，跳转到订单中心
                         * 无，跳转到门店列表
                         */
                        BaseURL = ZkPropertyUtil.get("BaseUrlMember")+"orderManage/selBySub";
                        Map<String,String> orderMap = new HashMap<String,String>();
                        orderMap.put("openId", openId);
                        orderMap.put("warehouseId", warehouseId);
                        JSONObject orderObject = ApiUtils.excutePost(BaseURL,null,null,orderMap);
                        JSONObject dataOrder = orderObject.getJSONObject("data");
                        String orderList = dataOrder.getString("isOrder");
                        if("false".equals(orderList)){
                            //无，进入门店选柜界面
                            logger.info("------view-----"+view);
                            view = ZkPropertyUtil.get("baseURL") + "web/whc/warehouse/cabinetSelect.html?openId=" + openId + "&warehouseId=" + warehouseId ;
                        }else{
                            //有，跳转到订单中心
                            logger.info("------view-----"+view);
                            view = ZkPropertyUtil.get("baseURL") + "web/whc/member/center.html?openId="+ openId;
                        }
                    }

                }
            }else{
                logger.info("------view-----"+view);
                request.getRequestDispatcher(view).forward(request, response);
                return;
            }
            response.sendRedirect(view);
            return;
        } catch (Exception e) {
            logger.info("-------异常----"+e.getStackTrace());
            e.printStackTrace();
            request.getRequestDispatcher(view).forward(request, response);
            return;
        }
    }
    private JSONObject getAccessToken(String code) {
        logger.info("------getAccessToken-----");
        JSONObject jsonObject = null;
        //?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code
        String requestUrl = WeixinUtil.AUTHORIZE_TOKE_URL;
        requestUrl = requestUrl.replace("?","-");
        String[] requestUrls= requestUrl.split("-");
        try {
            Map<String,String> map = new HashMap<String, String>();
            map.put("appid",ZkPropertyUtil.get("corpId"));
            map.put("SECRET",ZkPropertyUtil.get("secret"));
            map.put("CODE",code);
            map.put("grant_type","authorization_code");
            jsonObject = ApiUtils.excuteGet(requestUrls[0],null,map);
        } catch (Exception e) {
            logger.info("-------异常----"+e.getStackTrace());
            e.printStackTrace();
        }
        logger.info("------jsonObject-----"+jsonObject);
        return jsonObject;
    }
}
