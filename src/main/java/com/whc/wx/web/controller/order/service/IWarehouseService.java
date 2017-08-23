package com.whc.wx.web.controller.order.service;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by WangShengZhan
 * Email：WangShengZhan@wanhuchina.com
 * Date：2017/7/18
 * Time：15:05
 */
public interface IWarehouseService {

    public JSONObject getWarehouseData(String warehouseId) throws Exception;

}
