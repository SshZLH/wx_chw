package com.whc.wx.web.util;

import com.alibaba.fastjson.JSONObject;
import com.wanhuchina.common.code.CommonCode;
import com.wanhuchina.common.code.TxResultResponse;
import com.wanhuchina.common.exception.CommonException;
import com.wanhuchina.common.util.http.ApiUtils;
import com.wanhuchina.common.util.zk.ZkPropertyUtil;
import com.whc.wx.web.controller.order.service.impl.OderForYSServiceImpl;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by shenshanghua
 * Email shenshanghua@wanhuchina.com
 * Date：2017/6/25
 * Time：17:16
 */
public  class OpenDoorUtils {


    public static TxResultResponse openDoor(String boardNo, String originalDateMapStr,JSONObject JSONwarehouseCell,JSONObject JSONmember) throws CommonException {
        TxResultResponse resultResponse = new TxResultResponse();
        Map<String, String> openDoorParamMap = new HashMap<String, String>();
        Map<String, String> paramsMap = new HashMap<String, String>();
        openDoorParamMap.put("requestOpenDoorStr", originalDateMapStr);
        if ("AC".equals(boardNo)) {
            //旧板子开门的路径
            try {

                String URL = ZkPropertyUtil.get("oldBoard")+"?requestOpenDoorStr="+originalDateMapStr;

                OderForYSServiceImpl.httpRequest(URL);
                resultResponse.setCode(CommonCode.SUCCESS.getCode());
                resultResponse.setMsg("1代板子开门成功");
            } catch (Exception e) {
                resultResponse.setCode(CommonCode.HTTP_ERROR.getCode());
                resultResponse.setMsg(CommonCode.HTTP_ERROR.getMsg());
            }
        } else if ("WH".equals(boardNo)) {
            //新板子的路径
            String key = ZkPropertyUtil.get("signKeyByOpenCabinet");
            String Encrypted = originalDateMapStr + key;
            String combineString = DigestUtils.md5Hex(Encrypted);//加密串
            openDoorParamMap.put("combineString", combineString);//加密串
            JSONObject responLock = null;
            try {
                responLock = ApiUtils.excutePost(ZkPropertyUtil.get("BaseUrlLock") + "lock/auto/openLock", null, null, openDoorParamMap);

            } catch (Exception e) {
                resultResponse.setCode(CommonCode.HTTP_ERROR.getCode());
                resultResponse.setMsg(CommonCode.HTTP_ERROR.getMsg());
            }

            //判断状态码返回参数
            switch (responLock.getInteger("code")) {
                case 10000:
                    resultResponse.setCode(CommonCode.SUCCESS.getCode());
                    resultResponse.setMsg("门已打开");
                    paramsMap.put("cabinetDoorNo" , JSONwarehouseCell.getJSONObject("data").getJSONObject("warehouseCell").getString("number"));
                    paramsMap.put("open" , JSONmember.getJSONObject("data").getString("openId"));
                    resultResponse.setData(paramsMap);
                    break;
                case 90004:
                    resultResponse.setCode(CommonCode.PARAM_ERROR.getCode());
                    resultResponse.setMsg(CommonCode.PARAM_ERROR.getMsg());
                    break;

                case 77777:
                    resultResponse.setCode(CommonCode.KEY_ERROR.getCode());
                    resultResponse.setMsg(CommonCode.KEY_ERROR.getMsg());
                    break;

                case 90001:
                    resultResponse.setCode(CommonCode.SQL_SELECT_ERROR.getCode());
                    resultResponse.setMsg(CommonCode.SQL_SELECT_ERROR.getMsg());
                    break;

                default:
                    resultResponse.setCode(CommonCode.SERVER_ERROR.getCode());
                    resultResponse.setMsg(CommonCode.SERVER_ERROR.getMsg());
                    break;
            }
        }else{
            resultResponse.setCode(CommonCode.PARAM_ERROR.getCode());
            resultResponse.setMsg("板子号不符合规范");
        }
        return resultResponse;

    }
}