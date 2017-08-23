package com.whc.wx.web.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wanhuchina.common.util.http.ApiUtils;
import com.wanhuchina.common.util.http.HttpPostUtils;
import com.wanhuchina.common.util.zk.ZkPropertyUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by WangShengZhan
 * Email：WangShengZhan@wanhuchina.com
 * Date：2017/6/12
 * Time：12:16
 */
public class Test {

    public static void main(String[] args) {
        Map<String,String> orderMap = new HashMap<String, String>();
        orderMap.put("status","1");
        JSONObject orderjson = new JSONObject();
        String memberURL = "http://192.168.0.131:8083/orderManage/getOrderStorageByStatus";
        System.out.println("------12345-----"+memberURL);
        try {
            orderjson = ApiUtils.excutePost(memberURL,null,null,orderMap);
            if(orderjson.getIntValue("code")==10000){
                JSONArray resultList = orderjson.getJSONArray("data");
                if(resultList==null || resultList.size()==0){

                }else{
                    Map map = new HashMap();
                    for (int i = 0; i < resultList.size(); ++i) {
                        map = (Map)resultList.get(i);
                        System.out.println(map.toString());
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
