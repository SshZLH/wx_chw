package com.whc.wx.web.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.ConnectionPoolTimeoutException;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;


public class SMSUtils {
	private static final String SEND_SMS_API_URL = "http://cloud.baiwutong.com:8080/post_sms.do";
	
	private static final String ACCOUNT = "wj9995";
	private static final String PASSWORD = "8907wd";
	private static final String BUSINESS_CODE = "1069033909995";
	private static final String END_STR = "回复TD退订";
	
	public static String md5Encode(String inStr)  {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
            return "";
        }

        byte[] byteArray = null;
		try {
			byteArray = inStr.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        byte[] md5Bytes = md5.digest(byteArray);
        StringBuffer hexValue = new StringBuffer();
        for (int i = 0; i < md5Bytes.length; i++) {
            int val = ((int) md5Bytes[i]) & 0xff;
            if (val < 16) {
                hexValue.append("0");
            }
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();
    }
	private static String stream2String (InputStream in) throws IOException {
		System.out.println(":::::::::::::::::::::::::::::::::::hello!");
		int i = -1;
		//org.apache.commons.io.output.ByteArrayOutputStream
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		while ((i = in.read()) != -1) {
		    baos.write(i);
		}
		return baos.toString("utf-8");
	}
	
	public static void sendSMS(String mobile, String msg_content) {
		String url = SEND_SMS_API_URL;
		String formatParams="";
		List<NameValuePair> values = new ArrayList<NameValuePair>();
		values.add(new BasicNameValuePair("id", ACCOUNT));
		values.add(new BasicNameValuePair("MD5_td_code", md5Encode(PASSWORD + BUSINESS_CODE)));
		values.add(new BasicNameValuePair("mobile", mobile));
		values.add(new BasicNameValuePair("msg_content", msg_content ));
	System.out.println("msg_content11111111111111111111111111:"+msg_content );
		values.add(new BasicNameValuePair("msg_id", ""));
		values.add(new BasicNameValuePair("ext", ""));

		// 将参数进行utf-8编码  
        if (null != values && values.size() > 0) {  
            formatParams = URLEncodedUtils.format(values, "gbk");  
        }
		
		if (formatParams != null) {  
			url = url.indexOf("?") < 0 ? (url + "?" + formatParams): (url + "&" + formatParams);  
        }
		
		HttpPost  httpPost=null;
		HttpClient httpClient = new DefaultHttpClient();
		//实例HttpClient 并执行带有HttpPost的方法,返回HttpResponse 响应，再进行操作
		try{
			httpPost = new HttpPost(url);
			StringEntity entity = new UrlEncodedFormEntity(values,Consts.UTF_8);
			entity.setContentType("application/x-www-form-urlencoded");
			httpPost.setEntity(entity);
			HttpResponse httpResponse = httpClient.execute(httpPost);
			
			int statusCode = httpResponse.getStatusLine().getStatusCode();
			if (statusCode == HttpStatus.SC_OK) {
				HttpEntity _entity = httpResponse.getEntity();
				try {
					if (_entity != null) {
						System.out.println(stream2String(new ByteArrayInputStream(EntityUtils.toByteArray(_entity))));
					}
				} finally {
					if (httpResponse != null) {
						// 会自动释放连接
						EntityUtils.consume(_entity);
					}
				}
			} else {
				System.out.println("服务器连接失败！错误编号：" + statusCode+ ",错误信息：" +statusCode);
			}
		
		} catch (ConnectionPoolTimeoutException e) {
			e.printStackTrace();
		} catch (ConnectTimeoutException e) {
			e.printStackTrace();
		} catch (SocketTimeoutException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(httpPost != null)httpPost.releaseConnection();
			httpPost=null;
			httpClient=null;
		}
	}
	
	public static void main(String[] args) {
		String mobile = "18353314680";
		String msg_content = "下发短信内容：老杨 "+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		sendSMS(mobile, msg_content);
	}
}
