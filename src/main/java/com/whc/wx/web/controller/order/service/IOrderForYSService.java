package com.whc.wx.web.controller.order.service;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by WangShengZhan
 * Email：WangShengZhan@wanhuchina.com
 * Date：2017/6/28
 * Time：17:51
 */

public interface IOrderForYSService {
    public static final String ORDERFORYSSERVICE = IOrderForYSService.class.getName();
    /**
     * 修改订单状态
     * @param orderId
     * @param startDate
     * @param endDate
     * @return
     */
    public String updateOrderStatus(String orderId,String startDate,String endDate) throws Exception;

    /**
     *
     * @param voucherId
     * @param orderId
     * @return
     */
    public String modifyVoucherState(String voucherId,String orderId) throws Exception;

    /**
     *
     * @param orderId
     * @param rentalTime
     * @param total
     * @param endDate
     * @param unit
     * @param number
     * @return
     */
    public JSONObject continueWarehouse(String orderId, String rentalTime, String total, String endDate, String unit, String number)  throws Exception;

    /**
     * 退柜/退仓
     * @param orderId
     * @param startDate
     * @param endDate
     * @return
     */
    public JSONObject returnWarehouse(String orderId,String startDate,String endDate)  throws Exception;
}
