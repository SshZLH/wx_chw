<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="com.wanhuchina.common.util.zk.ZkPropertyUtil" %>
<%@ page import="com.wanhuchina.common.util.weixin.cgi.WeixinUtil" %>
<%
String openId = request.getParameter("openId");
//IBaseBizService baseBizService = SpringManager.getComponent(IBaseBizService.BASEBIZSERVICE);
//WeixinUser weixinUser = (WeixinUser)baseBizService.load(WeixinUser.class, openId);
String baseUrl = ZkPropertyUtil.get("baseURL");
Map<String, String> configMap = WeixinUtil.getJsConfigMap(baseUrl.subSequence(0, baseUrl.length()-1)
		+ request.getRequestURI()
		+ "?"
		+ request.getQueryString(), ZkPropertyUtil.get("corpId"), ZkPropertyUtil.get("secret"));
%>

<!DOCTYPE html PUBLIC "-//WAPFORUM//DTD XHTML Mobile 1.0//EN" "http://www.wapforum.org/DTD/xhtml-mobile10.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" class="ht1">
	<head>
		<base href="<%=baseUrl%>">
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, minimum-scale=1, user-scalable=no, minimal-ui">
		<link rel="stylesheet" href="/app/wes/auth/css/style.css" type="text/css" />
    	<script src="/resource/js/jquery-2.0.3.min.js" type="text/javascript" charset="utf-8"></script>
		<script src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js" type="text/javascript" charset="utf-8"></script>
		<script type="text/javascript">
	    wx.config({
		    debug: false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
		    appId: '<%=ZkPropertyUtil.get("corpId")%>', // 必填，企业号的唯一标识，此处填写企业号corpid
		    timestamp: <%=configMap.get("timestamp")%>, // 必填，生成签名的时间戳
		    nonceStr: '<%=configMap.get("nonceStr")%>', // 必填，生成签名的随机串
		    signature: '<%=configMap.get("signature")%>',// 必填，签名，见附录1
		    jsApiList: [
	        'getLocation'
		    ] // 必填，需要使用的JS接口列表，所有JS接口列表见附录2
		});
	    </script>
	    <script>
		   document.addEventListener("WeixinJSBridgeReady",function(){
		   	   WeixinJSBridge.call("hideOptionMenu");
		   });
		</script>
  </head>
  
  <body class="ht2">
		<div class="wrap w1 mg por ht3">
			<div id="loading" class="ctrl" style="display:none;">
                <dl class="tips tp1">
                    <dd>
                        <div>
                            <img src="/app/wes/auth/images/ico-gif.gif" />
                        </div>
                        <span>请等待，正在获取位置信息！</span>
                    </dd>
                    <div class="blank1">
                    </div>
                    <dt>
                        <p class="p1 mg">
                           	您好！首次认证系统要求采集你的位置信息，请务必同意“允许”，否则无法认证。
                        </p>
                    </dt>
                </dl>
            </div>
            <div id="failed" class="ctrl" style="display:none;">
                <dl class="tips tp1">
                    <dd>
                        <img src="/app/wes/auth/images/ico03.png" /><span class="span2">抱歉本次认证失败，
                            <br/>
                            请您重新安装微信并关注再次进行认证。
                        </span>
                    </dd>
                    <div class="blank1">
                    </div>
                    <dt>
                        <p class="p2 mg fz0">
                            <a href="javascript:closeWindow();" class="a1">返回</a>
                            <div class="clear">
                            </div>
                        </p>
                        <div class="clear">
                        </div>
                    </dt>
                </dl>
            </div>
            <div id="begin" class="ctrl" style="display:none;">
                <dl class="tips tp1">
                    <dd>
                        <img src="/app/wes/auth/images/ico01.png" />
                    </dd>
                    <div class="blank1">
                    </div>
                    <dt>
                        <p class="p1 mg">
                            	您好！首次认证系统要求采集你的位置信息，请务必同意“允许”，否则无法认证。
                        </p>
                        <div class="blank1">
                        </div>
                        <p class="p2 mg fz0">
                            <a href="javascript:closeWindow();">返回</a>
                            <button id="beginButton">
                               	开始认证
                            </button>
                            <div class="clear">
                            </div>
                        </p>
                        <div class="clear">
                        </div>
                    </dt>
                </dl>
            </div>
            <div id="success" class="ctrl" style="display:none;">
                <dl class="tips tp1">
                    <dd>
                        <img src="/app/wes/auth/images/ico02.png" /><span class="span1">您已通过认证！</span>
                    </dd>
                    <div class="blank1">
                    </div>
                    <dt>
                        <p class="p2 mg fz0">
                            <a href="javascript:closeWindow();" class="a1">返回</a>
                            <div class="clear">
                            </div>
                        </p>
                        <div class="clear">
                        </div>
                    </dt>
                </dl>
            </div>
		</div>
  		<script type="text/javascript">
  		$(function () {
			$('.ctrl').hide();
			$('#loading').show();
			$('#beginButton').on('touchstart', function () {
				wx.getLocation({
					success: function (res) {
						var lati = res.latitude; // 纬度，浮点数，范围为90 ~ -90
				        var longi = res.longitude; // 经度，浮点数，范围为180 ~ -180
						var _url = '<%=baseUrl%>/app/wes/auth/authUser.jsp?openId=<%=openId%>';
						$.post(_url, function (json) {
							$('.ctrl').hide();
							$('#success').show();
						});
					},
					fail : function () {
						$('.ctrl').hide();
						$('#failed').show();
					},
					cancel : function () {
						$('.ctrl').hide();
						$('#failed').show();
					}
				});
			});
		});
  		wx.ready(function(){
  	  		$('.ctrl').hide();
			$('#begin').show();
		});
		function closeWindow(){
			setInterval(function(){
				WeixinJSBridge.invoke('closeWindow',{},function(res){
				});
			},20);
		}
		</script>

  </body>
</html>
