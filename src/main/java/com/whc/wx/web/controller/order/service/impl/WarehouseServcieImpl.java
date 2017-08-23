package com.whc.wx.web.controller.order.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.util.StringUtil;
import com.wanhuchina.common.util.http.ApiUtils;
import com.wanhuchina.common.util.zk.ZkPropertyUtil;
import com.whc.wx.web.controller.order.service.IWarehouseService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by WangShengZhan
 * Email：WangShengZhan@wanhuchina.com
 * Date：2017/7/18
 * Time：15:08
 */
@Service("iWarehouseService")
public class WarehouseServcieImpl implements IWarehouseService{
    /**
     *
     * @param warehouseId
     * @return
     * @throws Exception
     */
    public JSONObject getWarehouseData(String warehouseId) throws Exception{
        JSONObject jobject = new JSONObject();
        Map<String,String>  WarehouseMap = new HashMap<String, String>();
        WarehouseMap.put("wahoId",warehouseId);
        String warehouseURL = ZkPropertyUtil.get("BaseUrlStorage")+"Warehouse/getWarehouseInfo";
        JSONObject warehouseObject = ApiUtils.excutePost(warehouseURL,null,null,WarehouseMap);
        if(warehouseObject.getIntValue("code")==10000){
            JSONObject warehouse = warehouseObject.getJSONObject("data").getJSONObject("Warehouse");
            jobject.put("id", warehouse.getString("id"));
            jobject.put("name", warehouse.getString("name"));
            jobject.put("addr", warehouse.getString("addr"));
            jobject.put("city", warehouse.getString("city"));
            jobject.put("area", warehouse.getString("area"));
            jobject.put("province", warehouse.getString("province"));
            jobject.put("image1", warehouse.getString("image1"));
            jobject.put("image2", warehouse.getString("image2"));
            jobject.put("image3", warehouse.getString("image3"));
            jobject.put("image4", warehouse.getString("image4"));
            jobject.put("image5", warehouse.getString("image5"));
            jobject.put("introduce", warehouse.getString("introduce"));
            jobject.put("type", warehouse.getString("type"));
            jobject.put("preferential", warehouse.getString("preferential"));
            Map<String,String> priceMap = new HashMap<String, String>();
            priceMap.put("warehouseId",warehouseId);
            String priceURL = ZkPropertyUtil.get("BaseUrlStorage")+"wahoce/selWahocePriceParam";
            JSONObject priceObject = ApiUtils.excutePost(priceURL,null,null,priceMap);
            if(priceObject.getIntValue("code")==10000){
                JSONArray priceList = priceObject.getJSONArray("data");
                Map<String,String> wahoceTypesMap = new HashMap<String, String>();
                wahoceTypesMap.put("status","1");
                String wahoceTypesURL = ZkPropertyUtil.get("BaseUrlStorage")+"wahoce/selWahoceTypeByStatus";
                JSONObject wahoceTypesObject = ApiUtils.excutePost(wahoceTypesURL,null,null,wahoceTypesMap);
                if(wahoceTypesObject.getIntValue("code")==10000){
                    System.out.println("----wahoceTypesObject----"+wahoceTypesObject.getJSONArray("data"));
                    JSONArray wahoceType = wahoceTypesObject.getJSONArray("data");
                    System.out.println("----wahoceType-----"+wahoceType.toString()+"-----priceList.size()---"+priceList.size());
                    /**
                     * 判断priceObject 的list 不为null
                     */
                    if(priceList!=null && priceList.size()!=0){
                        JSONArray wahoceTypeList = new JSONArray();
                        JSONArray warehouseCellList = new JSONArray();
                        System.out.println("----wahoceType1-----"+wahoceType.toString());
                        for(int i = 0 ;i < wahoceType.size();i++){
                            JSONObject wahoceTypes = wahoceType.getJSONObject(i);

                            System.out.println("----wahoceTypes-----"+wahoceTypes.toString());
                            String typeId = wahoceTypes.getString("id");
                            String typeCode = wahoceTypes.getString("code");
                            String typeName = wahoceTypes.getString("name");
                            String icon = "";
                            if ("1".equals(typeCode)) {
                                icon = "/web/whc/warehouse/img/s5.jpg";
                            } else if ("2".equals(typeCode)) {
                                icon = "/web/whc/warehouse/img/s5.jpg";
                            } else if ("3".equals(typeCode)) {
                                icon = "/web/whc/warehouse/img/s1.jpg";
                            } else if ("4".equals(typeCode)) {
                                icon = "/web/whc/warehouse/img/s2.jpg";
                            } else if ("5".equals(typeCode)) {
                                icon = "/web/whc/warehouse/img/s3.jpg";
                            } else if ("6".equals(typeCode)) {
                                icon = "/web/whc/warehouse/img/s4.jpg";
                            }
                            JSONObject wahoceTypeJson = new JSONObject();
                            wahoceTypeJson.put("typeId", typeId);
                            wahoceTypeJson.put("typeCode", typeCode);
                            wahoceTypeJson.put("typeName", typeName);
                            wahoceTypeJson.put("icon", icon);
                            JSONArray warehouseCellArray = new JSONArray();
                            for (int n = 0;n < priceList.size();n++) {
                                JSONObject prices = priceList.getJSONObject(n);
                                System.out.println("----prices-----"+prices.toString());
                                if(typeCode.equals(prices.getString("typeCode"))){
                                    if(StringUtil.isNotEmpty(prices.getString("size"))
                                            || StringUtil.isNotEmpty(prices.getString("count"))){
                                        JSONObject object = new JSONObject();
                                        object.put("size", prices.getString("size"));
                                        object.put("count", prices.getString("count"));
                                        object.put("dayPrice", prices.getString("dayPrice"));
                                        object.put("monthPrice", prices.getString("monthPrice"));
                                        object.put("halfPrice", prices.getString("halfPrice"));
                                        object.put("yearPrice", prices.getString("yearPrice"));
                                        object.put("specialPrice", prices.getString("specialPrice"));
                                        warehouseCellArray.add(object);
                                    }

                                }
                            }
                            wahoceTypeJson.put("warehouseCells", warehouseCellArray);
                            warehouseCellList.add(wahoceTypeJson);
                        }
                        jobject.put("warehouseCellList", warehouseCellList);
                    }else{
                        System.out.println("------------priceObject.getJSONObject(data)---null-------------");
                    }
                }else{
                    System.out.println("-------------wahoce/selWahoceTypeByStatus---------------");
                }
            }else{
                System.out.println("----------------wahoce/selWahocePriceParam------------");
            }
        }else{
            System.out.println("--------------Warehouse/getWarehouseInfo--------------");
        }
        System.out.println("----jobject-----："+jobject.toString());
        return jobject;
    }

}
