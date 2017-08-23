package org.wss.weixin.common.media;

/**媒体接口类
 * 
 * @author ivhhs
 * @date 2014.10.16
 */
import com.wanhuchina.common.util.weixin.cgi.WeixinUtil;
import com.wanhuchina.common.util.zk.ZkPropertyUtil;
import net.sf.json.JSONObject;
import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.tags.ImageTag;
import org.htmlparser.util.NodeIterator;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.zeroturnaround.exec.ProcessExecutor;
import org.zeroturnaround.exec.ProcessResult;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MediaUtil {
	/**
	 * 上传图文消息内的图片获取URL
	 * 
	 * @param appid
	 * @param secret
	 * @param mediaFileUrl
	 *            本接口所上传的图片不占用公众号的素材库中图片数量的5000个的限制。图片仅支持jpg/png格式，大小必须在1MB以下
	 * */
	public static WeixinMedia uploadNewsImage(String appid, String secret, String mediaFileUrl) {
		WeixinMedia weixinMedia = null;
		// 拼装请求地址
		String accessToken = WeixinUtil.getAccessToken(appid, secret).getToken();
		String uploadMediaUrl = "https://api.weixin.qq.com/cgi-bin/media/uploadimg?access_token=ACCESS_TOKEN";
		uploadMediaUrl = uploadMediaUrl.replace("ACCESS_TOKEN", accessToken);

		// 定义数据分隔符
		String boundary = "------------7da2e536604c8";
		try {
			URL uploadUrl = new URL(uploadMediaUrl);
			HttpURLConnection uploadConn = (HttpURLConnection) uploadUrl.openConnection();
			uploadConn.setDoOutput(true);
			uploadConn.setDoInput(true);
			uploadConn.setRequestMethod("POST");
			// 设置请求头Content-Type
			uploadConn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
			// 获取媒体文件上传的输出流（往微信服务器写数据）
			OutputStream outputStream = uploadConn.getOutputStream();

			URL mediaUrl = new URL(mediaFileUrl);
			HttpURLConnection meidaConn = (HttpURLConnection) mediaUrl.openConnection();
			meidaConn.setDoOutput(true);
			meidaConn.setRequestMethod("GET");

			// 从请求头中获取内容类型
			String contentType = meidaConn.getHeaderField("Content-Type");
			// 根据内容类型判断文件扩展名
			String fileExt = getFileEndWitsh(contentType);
			// 请求体开始
			outputStream.write(("--" + boundary + "\r\n").getBytes());
			outputStream.write(String.format("Content-Disposition: form-data; name=\"media\"; filename=\"file1%s\"\r\n", fileExt).getBytes());
			outputStream.write(String.format("Content-Type: %s\r\n\r\n", contentType).getBytes());

			// 获取媒体文件的输入流（读取文件）
			BufferedInputStream bis = new BufferedInputStream(meidaConn.getInputStream());
			byte[] buf = new byte[8096];
			int size = 0;
			while ((size = bis.read(buf)) != -1) {
				// 将媒体文件写到输出流（往微信服务器写数据）
				outputStream.write(buf, 0, size);
			}
			// 请求体结束
			outputStream.write(("\r\n--" + boundary + "--\r\n").getBytes());
			outputStream.close();
			bis.close();
			meidaConn.disconnect();

			// 获取媒体文件上传的输入流（从微信服务器读数据）
			InputStream inputStream = uploadConn.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
			StringBuffer buffer = new StringBuffer();
			String str = null;
			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
			}
			bufferedReader.close();
			inputStreamReader.close();
			// 释放资源
			inputStream.close();
			inputStream = null;
			uploadConn.disconnect();

			// 使用JSON-lib解析返回结果
			JSONObject jsonObject = JSONObject.fromObject(buffer.toString());
			System.out.println("jsonObject:"+jsonObject.toString());
			weixinMedia = new WeixinMedia();
			weixinMedia.setUrl(jsonObject.getString("url"));
		} catch (Exception e) {
			weixinMedia = null;
			String error = String.format("上传媒体文件失败：%s", e);
			System.out.println(error);
		}
		return weixinMedia;
	}
	/**
	 * 上传图文素材文件
	 * 
	 * @param appid
	 * @param secret
	 * @param postData
	 * */
	public static WeixinMedia uploadNews(String appid, String secret, JSONObject postData) {
		WeixinMedia weixinMedia = null;
		String accessToken = WeixinUtil.getAccessToken(appid, secret).getToken();
		String requestURl = "https://api.weixin.qq.com/cgi-bin/material/add_news?access_token=ACCESS_TOKEN";
		requestURl = requestURl.replace("ACCESS_TOKEN", accessToken);
		System.out.println("postData:"+postData.toString());
		JSONObject jsonObject = WeixinUtil.httpRequest(requestURl, "POST", postData.toString());
		System.out.println("jsonObject:"+jsonObject.toString());
		try {
			weixinMedia = new WeixinMedia();
			weixinMedia.setMediaId(jsonObject.getString("media_id"));
		} catch (Exception e) {
			weixinMedia = null;
			String error = String.format("上传媒体文件失败：%s", e);
			System.out.println(error);
		}
		return weixinMedia;
	}
	/**
	 * 上传永久素材文件
	 * 
	 * @param appid
	 * @param secret
	 * @param type
	 *            媒体文件类型，分别有图片（image）、语音（voice）、视频（video）
	 *            form-data中媒体文件标识，有filename、filelength、content-type等信息
	 * @param mediaFileUrl
	 *            媒体文件的url 上传的媒体文件限制 图片（image）:1MB，支持JPG格式 语音（voice）：2MB，播放长度不超过60s，支持AMR格式 视频（video）：10MB，支持MP4格式 普通文件（file）：10MB
	 * */
	public static WeixinMedia uploadMaterial(String appid, String secret, String type, String mediaFileUrl) {
		WeixinMedia weixinMedia = null;
		String accessToken = WeixinUtil.getAccessToken(appid, secret).getToken();
		// 拼装请求地址
		String uploadMediaUrl = "https://api.weixin.qq.com/cgi-bin/material/add_material?access_token=ACCESS_TOKEN";
		uploadMediaUrl = uploadMediaUrl.replace("ACCESS_TOKEN", accessToken).replace("TYPE", type);

		// 定义数据分隔符
		String boundary = "------------7da2e536604c8";
		try {
			URL uploadUrl = new URL(uploadMediaUrl);
			HttpURLConnection uploadConn = (HttpURLConnection) uploadUrl.openConnection();
			uploadConn.setDoOutput(true);
			uploadConn.setDoInput(true);
			uploadConn.setRequestMethod("POST");
			// 设置请求头Content-Type
			uploadConn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
			// 获取媒体文件上传的输出流（往微信服务器写数据）
			OutputStream outputStream = uploadConn.getOutputStream();

			URL mediaUrl = new URL(mediaFileUrl);
			HttpURLConnection meidaConn = (HttpURLConnection) mediaUrl.openConnection();
			meidaConn.setDoOutput(true);
			meidaConn.setRequestMethod("GET");

			// 从请求头中获取内容类型
			String contentType = meidaConn.getHeaderField("Content-Type");
			// 根据内容类型判断文件扩展名
			String fileExt = getFileEndWitsh(contentType);
			// 请求体开始
			outputStream.write(("--" + boundary + "\r\n").getBytes());
			outputStream.write(String.format("Content-Disposition: form-data; name=\"media\"; filename=\"file1%s\"\r\n", fileExt).getBytes());
			outputStream.write(String.format("Content-Type: %s\r\n\r\n", contentType).getBytes());

			// 获取媒体文件的输入流（读取文件）
			BufferedInputStream bis = new BufferedInputStream(meidaConn.getInputStream());
			byte[] buf = new byte[8096];
			int size = 0;
			while ((size = bis.read(buf)) != -1) {
				// 将媒体文件写到输出流（往微信服务器写数据）
				outputStream.write(buf, 0, size);
			}
			// 请求体结束
			outputStream.write(("\r\n--" + boundary + "--\r\n").getBytes());
			outputStream.close();
			bis.close();
			meidaConn.disconnect();

			// 获取媒体文件上传的输入流（从微信服务器读数据）
			InputStream inputStream = uploadConn.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
			StringBuffer buffer = new StringBuffer();
			String str = null;
			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
			}
			bufferedReader.close();
			inputStreamReader.close();
			// 释放资源
			inputStream.close();
			inputStream = null;
			uploadConn.disconnect();

			// 使用JSON-lib解析返回结果
			JSONObject jsonObject = JSONObject.fromObject(buffer.toString());
			System.out.println("jsonObject:"+jsonObject.toString());
			weixinMedia = new WeixinMedia();
			weixinMedia.setType(type);
			// type等于 缩略图（thumb） 时的返回结果和其它类型不一样
			if ("thumb".equals(type))
				weixinMedia.setMediaId(jsonObject.getString("thumb_media_id"));
			else
				weixinMedia.setMediaId(jsonObject.getString("media_id"));
			if(jsonObject.has("created_at")) weixinMedia.setCreatedAt(jsonObject.getInt("created_at"));
			if(jsonObject.has("url")) weixinMedia.setUrl(jsonObject.getString("url"));
		} catch (Exception e) {
			weixinMedia = null;
			String error = String.format("上传媒体文件失败：%s", e);
			System.out.println(error);
		}
		return weixinMedia;
	}
	
	/**
	 * 上传媒体文件
	 * 
	 * @param appid
	 * @param secret
	 * @param type
	 *            媒体文件类型，分别有图片（image）、语音（voice）、视频（video），普通文件(file)
	 *            form-data中媒体文件标识，有filename、filelength、content-type等信息
	 * @param mediaFileUrl
	 *            媒体文件的url 上传的媒体文件限制 图片（image）:1MB，支持JPG格式 语音（voice）：2MB，播放长度不超过60s，支持AMR格式 视频（video）：10MB，支持MP4格式 普通文件（file）：10MB
	 * */
	public static WeixinMedia uploadMedia(String appid, String secret, String type, String mediaFileUrl) {
		WeixinMedia weixinMedia = null;
		String accessToken = WeixinUtil.getAccessToken(appid, secret).getToken();
		// 拼装请求地址
		String uploadMediaUrl = "https://api.weixin.qq.com/cgi-bin/media/upload?access_token=ACCESS_TOKEN&type=TYPE";
		uploadMediaUrl = uploadMediaUrl.replace("ACCESS_TOKEN", accessToken).replace("TYPE", type);

		// 定义数据分隔符
		String boundary = "------------7da2e536604c8";
		try {
			URL uploadUrl = new URL(uploadMediaUrl);
			HttpURLConnection uploadConn = (HttpURLConnection) uploadUrl.openConnection();
			uploadConn.setDoOutput(true);
			uploadConn.setDoInput(true);
			uploadConn.setRequestMethod("POST");
			// 设置请求头Content-Type
			uploadConn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
			// 获取媒体文件上传的输出流（往微信服务器写数据）
			OutputStream outputStream = uploadConn.getOutputStream();

			URL mediaUrl = new URL(mediaFileUrl);
			HttpURLConnection meidaConn = (HttpURLConnection) mediaUrl.openConnection();
			meidaConn.setDoOutput(true);
			meidaConn.setRequestMethod("GET");

			// 从请求头中获取内容类型
			String contentType = meidaConn.getHeaderField("Content-Type");
			// 根据内容类型判断文件扩展名
			String fileExt = getFileEndWitsh(contentType);
			// 请求体开始
			outputStream.write(("--" + boundary + "\r\n").getBytes());
			outputStream.write(String.format("Content-Disposition: form-data; name=\"media\"; filename=\"file1%s\"\r\n", fileExt).getBytes());
			outputStream.write(String.format("Content-Type: %s\r\n\r\n", contentType).getBytes());

			// 获取媒体文件的输入流（读取文件）
			BufferedInputStream bis = new BufferedInputStream(meidaConn.getInputStream());
			byte[] buf = new byte[8096];
			int size = 0;
			while ((size = bis.read(buf)) != -1) {
				// 将媒体文件写到输出流（往微信服务器写数据）
				outputStream.write(buf, 0, size);
			}
			// 请求体结束
			outputStream.write(("\r\n--" + boundary + "--\r\n").getBytes());
			outputStream.close();
			bis.close();
			meidaConn.disconnect();

			// 获取媒体文件上传的输入流（从微信服务器读数据）
			InputStream inputStream = uploadConn.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
			StringBuffer buffer = new StringBuffer();
			String str = null;
			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
			}
			bufferedReader.close();
			inputStreamReader.close();
			// 释放资源
			inputStream.close();
			inputStream = null;
			uploadConn.disconnect();

			// 使用JSON-lib解析返回结果
			JSONObject jsonObject = JSONObject.fromObject(buffer.toString());
			weixinMedia = new WeixinMedia();
			weixinMedia.setType(jsonObject.getString("type"));
			// type等于 缩略图（thumb） 时的返回结果和其它类型不一样
			if ("thumb".equals(type))
				weixinMedia.setMediaId(jsonObject.getString("thumb_media_id"));
			else
				weixinMedia.setMediaId(jsonObject.getString("media_id"));
			weixinMedia.setCreatedAt(jsonObject.getInt("created_at"));
		} catch (Exception e) {
			weixinMedia = null;
			String error = String.format("上传媒体文件失败：%s", e);
			System.out.println(error);
		}
		return weixinMedia;
	}
	/**
	 * 根据内容类型判断文件扩展名
	 * 
	 * @param contentType
	 *            内容类型
	 * @return
	 */
	public static String getFileEndWitsh(String contentType) {
		String fileEndWitsh = "";
		if ("image/jpeg".equals(contentType))
			fileEndWitsh = ".jpg";
		else if ("audio/mpeg".equals(contentType))
			fileEndWitsh = ".mp3";
		else if ("audio/amr".equals(contentType))
			fileEndWitsh = ".amr";
		else if ("video/mp4".equals(contentType))
			fileEndWitsh = ".mp4";
		else if ("video/mpeg4".equals(contentType))
			fileEndWitsh = ".mp4";
		return fileEndWitsh;
	}
	/**
	 * 获取媒体文件
	 *            媒体文件id
	 * @param savePath
	 *            文件在服务器上的存储路径
	 * */
	public static String downloadMedia(String accessToken, String mediaId, String savePath) {
		String filePath = null;
		// 拼接请求地址
		String requestUrl = "https://api.weixin.qq.com/cgi-bin/media/get?access_token=ACCESS_TOKEN&media_id=MEDIA_ID";
		requestUrl = requestUrl.replace("ACCESS_TOKEN", accessToken).replace("MEDIA_ID", mediaId);
		System.out.println(requestUrl);
		try {
			URL url = new URL(requestUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoInput(true);
			conn.setRequestMethod("GET");
			File file =new File(savePath.replace("/", "\\"));    
			//如果文件夹不存在则创建    
			if  (!file .exists()  && !file .isDirectory()) file .mkdir(); 
			if (!savePath.endsWith("/")) {
				savePath += "/";
			}
			// 根据内容类型获取扩展名
			String fileExt = getFileEndWitsh(conn.getHeaderField("Content-Type"));
			// 将mediaId作为文件名
			filePath = savePath + mediaId + fileExt;
			
			BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
			FileOutputStream fos = new FileOutputStream(new File(filePath));
			byte[] buf = new byte[8096];
			int size = 0;
			while ((size = bis.read(buf)) != -1)
				fos.write(buf, 0, size);
			fos.close();
			bis.close();

			conn.disconnect();
			String info = String.format("下载媒体文件成功，filePath=" + filePath);
			System.out.println(info);
		} catch (Exception e) {
			filePath = null;
			String error = String.format("下载媒体文件失败：%s", e);
			System.out.println(error);
		}
		return filePath;
	}
	
	public static boolean changeToMp3(final String sourcePath,final String targetPath) {  
		boolean isSucess = false;
		try{
			List<String> args = new ArrayList<String>() {
				{
			        add(ZkPropertyUtil.get("converter.audio2mp3"));
			        add("-i");
			        add(sourcePath);
			        add("-codec:a");
			        add("libmp3lame");
			        add("-qscale:a");
			        add("3");
			        add("-y");
			        add(targetPath);
				}
			};
			ProcessExecutor exec = new ProcessExecutor();
		    exec.command(args);
		    ProcessResult result = exec.readOutput(true).execute();
		    isSucess = true ;
		    System.out.println("ExitValue="+result.getExitValue()+",output="+result.outputUTF8());
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return isSucess ;
    }  
	
	public static String replaceImgHtml(String appid, String secret, Node node, String html) {
		String text = node.getText();
		if (text.startsWith("img")) {
			ImageTag tag = (ImageTag)node;
			String src = tag.getImageURL();
			if(!src.toLowerCase().startsWith("http")) src = ZkPropertyUtil.get("baseURL") + src;
			WeixinMedia weixinMedia = uploadNewsImage(appid, secret, src);
			if(weixinMedia == null) throw new RuntimeException("转换图片失败");
			tag.setImageURL(weixinMedia.getUrl());
			String newText = tag.getText();
			html = html.replace(text, newText);
			//System.out.println("转换图片成功\n"+html);
		}
		return html;
	}
	
	public static String replaceImgNodes(NodeList nodeList, String html, String appid, String secret) {
		for(int i=0; i<nodeList.size(); i++) {
			Node node = nodeList.elementAt(i);
			NodeList list = node.getChildren();
			if (list != null && list.size() > 0) {
				html = replaceImgNodes(list, html, appid, secret);
			} else {
				html = replaceImgHtml(appid, secret, node, html);
			}
			//System.out.println("遍历子节点\n"+html);
		}
		return html;
	}
	
	public static String replaceHtmlContent(String appid, String secret, String html) {
		Parser parser = Parser.createParser(html, "utf-8");
		try {
			for (NodeIterator i = parser.elements (); i.hasMoreNodes(); ) {
				Node node = i.nextNode();
				NodeList nodeList = node.getChildren();
				if (nodeList != null && nodeList.size() > 0) {
					html = replaceImgNodes(nodeList, html, appid, secret);
				} else {
					html = replaceImgHtml(appid, secret, node, html);
				}
				//System.out.println("遍历第一层节点\n"+html);
			}
		} catch (ParserException e) {
			e.printStackTrace();
		}
		return html;
	}
	
	// 示例
	public static void main(String[] args) {
		//AccessToken accessToken = WeixinUtil.getAccessToken("wx7af4c93160937924", "7fc5aaa119ef4d2632a3747d59c8c660");
		/*uploadNewsImage("wx7af4c93160937924", "7fc5aaa119ef4d2632a3747d59c8c660", 
				"http://wss.weilinker.com:7004/wcmsUpload/20151231/51521451569241227.jpg");
		uploadNews("_keZWLBz14pNDV04F58-_TK_pQ3k8WvSCl7OgDxTRQ7A5fgM-SsQEAcOxWG2Lq0aLrQlEcCMOWCzZ8rFBht9SS3FILMYZ1vTyjQFK9BcS_4GUEcAGARFP", 
				"测试上传图文", 
				"IpCorOGcfmS21fC3CQOX1I7y45S5CgyjGXYXMp7iIa0", 
				"帕拉丁", 
				"几十种科普", 
				"0", 
				"<p style=\"text-align: center; \"><img src=\"http://wss.weilinker.com:7004/wcmsUpload/20151231/51521451569241227.jpg\" title=\"QQ截图20151231213945.jpg\"/></p><p>的的的的拉风的阿斯电风扇倒萨范德萨科萨法的萨拉菲倒萨；风力可达撒弗雷德萨法肯定撒福仕达飞洒多了范德萨科飞洒多了飞洒多了发撒旦拉沙德发了三大肯定撒拉沙德弗雷德萨打开；雷声大受到罚款了sadly</p>", 
				"");*/
		String a = "<p style=\"text-align: center; \"><img src=\"http://wss.weilinker.com:7004/wcmsUpload/20151231/51521451569241227.jpg\" title=\"QQ截图20151231213945.jpg\"/></p><p>的的的的拉风的阿斯电风扇倒萨范德萨科萨法的萨拉菲倒萨；</p>";
		a = replaceHtmlContent("wx7af4c93160937924", "7fc5aaa119ef4d2632a3747d59c8c660", a);
		System.out.println("==="+a);
	}

}
