package com.whc.wx.web.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.wanhuchina.common.util.zk.ZkPropertyUtil;
import net.sf.json.JSONObject;



public class WPayUtil {
	public final static String CGI_PAY_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
	
	public static JSONObject wPay(HttpServletRequest request,HttpServletResponse response,String openId,String orderId,String oldOrderId,String flag,String memo,String describe, String totalFee, String attach) throws Exception{
		// TODO Auto-generated method stub
		
		JSONObject jsonObject = null;
		
		String appid = ZkPropertyUtil.get("corpId");
		String nonce_str = NonceStr.getRandomString(10);
		String spbill_create_ip = request.getRemoteAddr();//终端IP(我理解是客户IP)
		String mch_id = ZkPropertyUtil.get("mch_id");
		//接收财付通通知的URL
		String p=oldOrderId+"-"+flag;
		String notify_url = ZkPropertyUtil.get("notify_url")+"?orderId="+oldOrderId+"&flag="+flag+"&memo="+memo;
		System.out.println("notify_url++++++++"+notify_url);
		System.out.println("openId++++++++"+openId);
		System.out.println("describe:"+describe+"attach:"+attach+"nonce_str"+nonce_str);
		SortedMap<Object,Object> parameters = new TreeMap<Object,Object>();
        parameters.put("appid", appid);
        parameters.put("mch_id", mch_id);  
        parameters.put("device_info", "WEB");  
        parameters.put("body", describe);  
        parameters.put("attach", attach); 
        parameters.put("nonce_str", nonce_str);  
        parameters.put("detail", describe);  
        parameters.put("notify_url", notify_url);  
        parameters.put("openid", openId);  
        parameters.put("out_trade_no", orderId);  
        parameters.put("spbill_create_ip", spbill_create_ip);  
       // parameters.put("total_fee", Integer.parseInt(totalFee));
        parameters.put("total_fee", Integer.valueOf((int) (Double.valueOf(totalFee)*100)));  
        parameters.put("trade_type", "JSAPI");
		System.out.println("notify_url++++123++++"+notify_url);
        String characterEncoding = "utf-8";  
        String sign = MD5Sign.createSign(characterEncoding,parameters);
		System.out.println("notify_url++++1234++++"+notify_url);

		StringBuilder sb = new StringBuilder();
		sb.append("<xml>");
		sb.append("<appid>"+appid+"</appid>");
		sb.append("<attach>"+attach+"</attach>");
		sb.append("<body>"+describe+"</body>");
		sb.append("<detail>"+describe+"</detail>");
		sb.append("<device_info>WEB</device_info>");
		sb.append("<mch_id>"+mch_id+"</mch_id>");
		sb.append("<nonce_str>"+nonce_str+"</nonce_str>");
		sb.append("<notify_url>"+notify_url+"</notify_url>");
		sb.append("<openid>"+openId+"</openid>");
		sb.append("<out_trade_no>"+orderId+"</out_trade_no>");
		sb.append("<spbill_create_ip>"+spbill_create_ip+"</spbill_create_ip>");
		//sb.append("<total_fee>"+Integer.parseInt(totalFee)+"</total_fee>");
		sb.append("<total_fee>"+Integer.valueOf((int) (Double.valueOf(totalFee)*100))+"</total_fee>");

		sb.append("<trade_type>JSAPI</trade_type>");
		sb.append("<sign>"+sign+"</sign>");
		sb.append("</xml>");
		String requestUrl = CGI_PAY_URL;
		System.out.println("notify_url++++12345++++"+notify_url);
		jsonObject = httpRequest(requestUrl, "POST", sb.toString());
		System.out.println("notify_url++++123456++++"+notify_url);
		String return_code = "";
		String result_code = "";
		System.out.println("jsonObject******"+jsonObject.toString());
		if (null != jsonObject) {
			return_code = jsonObject.get("return_code").toString();
			result_code = jsonObject.get("result_code").toString();
			if("SUCCESS".equals(return_code)&&"SUCCESS".equals(result_code)){
				Date date = new Date();
				Long timeStamp = date.getTime();
				nonce_str = NonceStr.getRandomString(10);
				SortedMap<Object,Object> _parameters = new TreeMap<Object,Object>();  
				_parameters.put("appId", appid);  
				_parameters.put("package", "prepay_id="+jsonObject.get("prepay_id").toString());  
				_parameters.put("signType", "MD5"); 
				_parameters.put("nonceStr", nonce_str);  
				_parameters.put("timeStamp", timeStamp);  
				String _sign = MD5Sign.createSign(characterEncoding,_parameters);
				jsonObject.put("sign", _sign);
				jsonObject.put("timeStamp", timeStamp);
				jsonObject.put("nonce_str", nonce_str);
			}
		}
		return jsonObject;
	}
	
	/**
	 * 发起https请求并获取结果
	 * 
	 * @param
	 *
	 * @param
	 *
	 * @param
	 *
	 * @return JSONObject(通过JSONObject.get(key)的方式获取json对象的属性值)
	 */
	public static synchronized JSONObject httpRequest(String request, String RequestMethod, String output) {
		JSONObject jsonObject = null;
		StringBuffer buffer = new StringBuffer();
		System.out.println("----buffer1---"+buffer.toString());
		try {
			// 建立连接
			URL url = new URL(request);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setUseCaches(false);
			connection.setRequestMethod(RequestMethod);
			if (output != null) {
				OutputStream out = connection.getOutputStream();
				out.write(output.getBytes("UTF-8"));
				out.close();
			}
			// 流处理
			InputStream input = connection.getInputStream();
			InputStreamReader inputReader = new InputStreamReader(input, "UTF-8");
			BufferedReader reader = new BufferedReader(inputReader);
			String line;
			while ((line = reader.readLine()) != null) {
				buffer.append(line);
			}
			// 关闭连接、释放资源
			reader.close();
			inputReader.close();
			input.close();
			input = null;
			connection.disconnect();
			System.out.println("----buffer2---"+buffer.toString());
			jsonObject = GetXML.getJSONFromXML(buffer.toString());
			System.out.println("----jsonObject1---"+jsonObject.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonObject;
	}
}
