package org.wss.utils;

import com.wanhuchina.common.util.weixin.cgi.WeixinUtil;
import com.wanhuchina.common.util.zk.ZkPropertyUtil;
import net.sf.json.JSONObject;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TemplateMessageUtil {
	
	public static void sendWcmsMessage(String templateId, JSONObject messageJSON, String toUsers) {
		//if(StringUtils.isNull(toUsers))  BizException.handleMessageException("操作失败,参数toUsers不能为空！");
		//if(StringUtils.isNull(templateId))  BizException.handleMessageException("操作失败,参数templateId不能为空！");
		messageJSON.put("template_id", templateId);
		String[] arr = toUsers.split(",");
		ExecutorService pool = Executors.newFixedThreadPool(2);
		for (int i = 0; i < arr.length; i++) {
			messageJSON.put("touser", arr[i]);
			final JSONObject msgJOSN = JSONObject.fromObject(messageJSON.toString());
			final long timer = i*800;
			Runnable runnable = new Runnable() {
				//@Override
				public void run() {
					try {
						Thread.sleep(timer);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					WeixinUtil.sendTemplateMessage(ZkPropertyUtil.get("corpId"), ZkPropertyUtil.get("secret"), msgJOSN.toString());
				}
			};
			pool.execute(runnable);
		}
		pool.shutdown();
	}
}
