package com.whc.wx.web.controller.order.service;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by WangShengZhan
 * Email：WangShengZhan@wanhuchina.com
 * Date：2017/7/20
 * Time：16:52
 */
public interface IMemberService {

    public JSONObject getMemberDoorPermissionByOpenId(String openId);
    public JSONObject openDoor(String deviceId, String openId, String type);


}
