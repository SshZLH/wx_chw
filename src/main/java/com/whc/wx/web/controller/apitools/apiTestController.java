package com.whc.wx.web.controller.apitools;





import javax.servlet.http.HttpServletRequest;

import com.wanhuchina.common.util.http.HttpPostUtils;
import com.wanhuchina.common.util.zk.ZkPropertyUtil;
import com.whc.wx.web.util.HttpClientUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;


@Controller
public  class apiTestController{
	
	/**
	 * 获取SessionID的集合
	 */
	@RequestMapping(value="apiTestTojson",produces="application/json; charset=utf-8")
	@ResponseBody
	public String gainSessionIDs(HttpServletRequest req){
		String send=null;
        try {
			String method = req.getParameter("method");
			String URL=req.getParameter("URL");
			String parameter=req.getParameter("parameter");
			String[] parames = parameter.split("&");
			Map<String,String> map = new HashMap<String, String>();
			for (int i=0;i<parames.length;i++){
				String[] parame = parames[i].split("=");
				if(parame.length==1){
					map.put(parame[0],"");
				}else{
					map.put(parame[0],parame[1]);
				}
			}
			//String  corpId = ZkPropertyUtil.get("corpId");
			//System.out.println("corpId:"+corpId);

			if("POST".equals(method)){
				//send = HttpClientUtil.sendHttpPost(URL, parameter);
				send = HttpPostUtils.httpPost(URL,null,null,null,map,"UTF-8");
				//send = URLEncoder.encode(send, "UTF-8").replace("+","%20");
			}else{
				URL=URL+"?"+parameter;
				send = HttpClientUtil.sendHttpGet(URL);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return send;
	}
}
