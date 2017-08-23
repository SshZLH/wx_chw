package com.whc.wx.web.controller.question;

import com.alibaba.fastjson.JSON;
import com.wanhuchina.common.code.CommonCode;
import com.wanhuchina.common.code.TxResultResponse;
import com.wanhuchina.common.exception.CommonException;
import com.wanhuchina.common.util.http.ApiUtils;
import com.wanhuchina.common.util.zk.ZkPropertyUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by shenshanghua
 * Email shenshanghua@wanhuchina.com
 * Date：2017/7/17
 * Time：16:14
 */
@RestController
@RequestMapping(value = "/questionManage")
public class QuestionController {

    @RequestMapping(value = "/addQuestion" , method = RequestMethod.POST)
    @ResponseBody
    public TxResultResponse addQuestion(HttpServletRequest req){
        TxResultResponse resultResponse = new TxResultResponse(CommonCode.SUCCESS.getCode(),"提交成功");

        try {
            Map<String,String> questionParamMap = new HashMap<String, String>();
            questionParamMap.put("type",req.getParameter("quest_type"));
            questionParamMap.put("comment",req.getParameter("quest_comment"));
            questionParamMap.put("orderId",req.getParameter("quest_order_id"));
            questionParamMap.put("sourceUrl",req.getParameter("quest_source_url"));
            questionParamMap.put("accessIp",req.getParameter("quest_access_ip"));
            questionParamMap.put("accessAddr",req.getParameter("quest_access_addr"));
            String userQuestion = JSON.toJSONString(questionParamMap);
            Map<String,String> ParamMap = new HashMap<String, String>();
            ParamMap.put("quest_browse_message",req.getParameter("quest_browse_message"));
            ApiUtils.excutePost( ZkPropertyUtil.get("BaseUrlMember")+"questionManage/addQuestion", null, userQuestion, ParamMap);//这边的返回值是新增的开门记录
            return  resultResponse;
        } catch (CommonException e) {
            return new TxResultResponse(CommonCode.ERROR.getCode(),"远程调用失败！");
        } catch (Exception e){
            return new TxResultResponse(CommonCode.SERVER_ERROR.getCode(),CommonCode.SERVER_ERROR.getMsg());
        }
    }
}
