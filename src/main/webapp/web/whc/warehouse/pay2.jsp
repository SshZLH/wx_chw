<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>

<%@page import="com.alibaba.fastjson.JSONObject"%>
<%@page import="java.text.SimpleDateFormat" %>
<%@page import="java.text.DecimalFormat" %>
<%@page import="org.apache.log4j.Logger" %>
<%@page import="com.wanhuchina.common.util.weixin.cgi.WeixinUtil"%>
<%@ page import="com.wanhuchina.common.util.zk.ZkPropertyUtil" %>
<%@ page import="com.wanhuchina.common.util.http.ApiUtils" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String parms=request.getParameter("parms");
String[] _parms=parms.split("-");


String orderId =_parms[0];
String flag=_parms[1];
//if(StringUtils.isNotNull(request.getParameter("flag"))){
//	flag=request.getParameter("flag");
//}
final Logger payLog = Logger.getLogger("pay");
JSONObject json=null;
Map<String,String> orderMap = new HashMap<String,String>();
orderMap.put("orderId",orderId);
String URL = ZkPropertyUtil.get("BaseUrlMember")+"orderManage/getOrderInfo";
json = ApiUtils.excutePost(URL,null,null,orderMap);
	System.out.println("======pay2======json=============="+json.toString());
String _weixinPayPluging_code = request.getParameter("code");
String _weixinPayPluging_openId="";
	if ((_weixinPayPluging_code!=null) && (!"authdeny".equals(_weixinPayPluging_code))) {
		String _weixinPayPluging_requestUrl = WeixinUtil.AUTHORIZE_TOKE_URL.replace("APPID",ZkPropertyUtil.get("corpId")).replace("SECRET",ZkPropertyUtil.get("secret")).replace("CODE", _weixinPayPluging_code);
		_weixinPayPluging_requestUrl = _weixinPayPluging_requestUrl.replace("?","-");
		String[] _weixinPayPluging_requestUrls = _weixinPayPluging_requestUrl.split("-");
		System.out.println("_weixinPayPluging_requestUrl[0]*******"+_weixinPayPluging_requestUrls[0]);
		Map<String,String> _weixinPayMap = new HashMap<String,String>();
		_weixinPayMap.put("APPID",ZkPropertyUtil.get("corpId"));
		_weixinPayMap.put("SECRET",ZkPropertyUtil.get("secret"));
		_weixinPayMap.put("CODE",_weixinPayPluging_code);
		_weixinPayMap.put("grant_type","authorization_code");
		JSONObject _weixinPayPluging_jsonObject=null;
		_weixinPayPluging_jsonObject = ApiUtils.excuteGet(_weixinPayPluging_requestUrls[0],null,_weixinPayMap);
		System.out.println("_weixinPayPluging_jsonObject:"+_weixinPayPluging_jsonObject.toString());
		try {
			_weixinPayPluging_openId = _weixinPayPluging_jsonObject.getString("openid");

		} catch (Exception e) {
			String _weixinPayPluging_error = String.format("获取openId失败 errcode:{} errmsg:{}", _weixinPayPluging_jsonObject.getIntValue("errcode"), _weixinPayPluging_jsonObject.toString());
			payLog.info("pay2.jsp页面下catch抛出异常_weixinPayPluging_error"+_weixinPayPluging_error);
			out.print(_weixinPayPluging_error);
			return;
		}
	} else {
		out.print("授权失败");
		payLog.info("授权失败_weixinPayPluging_openId的值为:"+_weixinPayPluging_openId);
		return;
	}
	System.out.println("_weixinPayPluging_openId:"+_weixinPayPluging_openId);
	char[] orderIds = orderId.toCharArray();
	Random r = new Random();
	int i = r.nextInt(31);
	int k = r.nextInt(31);
	char temp1 = orderIds[i];
	orderIds[i] = orderIds[k];
	orderIds[k] = temp1;
	String orderIdNew  =String.valueOf(orderIds);
	String totalFee="0";
%>

<html>
<head>
<meta charset="utf-8">
<meta name="viewport" content="target-densitydpi=device-dpi, width=device-width,height=device-height, initial-scale=1, user-scalable=no, minimum-scale=1.0, maximum-scale=1.0">
<meta name="format-detection" content="telephone=no" />
<!--em标准js代码，请放在页面的最上方，前面最好不要再有JS代码或JS文件-->
<script type="text/javascript">
	document.getElementsByTagName('html')[0].style.fontSize = Math.min(window.innerWidth*12/320,200)+"px";		
</script>
<link type="text/css" rel="stylesheet" href="css/flexslider.css" />
<link type="text/css" rel="stylesheet" href="css/wexincss.css" />
<link type="text/css" rel="stylesheet" href="css/weui.min.css" />
<title>支付</title>
</head>

<body>	
	<!--header 固定顶部-->
	<div class="header">
		<a href="javascript:history.go(-1);" class="head_back"></a>
		<h2>购买支付</h2>
	</div>
	<div class="header_zw"></div>
	<!--header 固定顶部-->
	
	<div class="pay_info">
		<dl>
			<dt><%=json.getJSONObject("data").getString("wahoName") %></dt>
			<%if("8".equals(json.getJSONObject("data").getString("waceType"))){
			        String startDate="";
			        String endDate="";
			        String totalAmount="";
			        String totalHour="";
			        String number="";
			        if(flag.equals("0")){
			        	number = json.getJSONObject("data").getString("number");
			        }else{
			        	number = json.getJSONObject("data").getString("ordertotaltimes");
			        }
			        
			        String unit = json.getJSONObject("data").getString("unit");
			        if("1".equals(flag)){
			        	payLog.info("orderId:"+orderId+"orderInfo.get(orderEndDate):"+json.getJSONObject("data").getString("orderEndDate")+"orderInfo.get(nextEndDate):"+json.getJSONObject("data").getString("nextEndDate"));
			        	startDate=json.getJSONObject("data").getString("orderEndDate").substring(0,16);
			        	endDate=json.getJSONObject("data").getString("nextEndDate").substring(0,16);
			        	totalAmount=json.getJSONObject("data").getString("nextAmount");
			        }else {
			        	startDate=json.getJSONObject("data").getString("orderStartDate").substring(0,16);
			        	endDate=json.getJSONObject("data").getString("orderEndDate").substring(0,16);
			        	totalAmount=json.getJSONObject("data").getString("orderTotalAmount");
			        }
			        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm"); 
			    	Date beginTime =  df.parse(startDate);
			    	Date endTime =  df.parse(endDate);
			    	 long diff = endTime.getTime() - beginTime.getTime();
			    	 long hours = diff/(1000* 60 * 60);
			    	 totalHour=Long.toString(hours);
			    	 totalFee=totalAmount;
			        %>
			<dd>类型：<%=json.getJSONObject("data").getString("waceName") %></dd>
			<dd>仓位编号：<%=json.getJSONObject("data").getString("waceNumber") %></dd>
			<dd>租用时间：</dd>
			<dd><%=startDate %>至<%=endDate %>
			<% if("1".equals(unit)){%>
				<span class="rt"><%=number %>小时</span>
			<%}else if("2".equals(unit)){%>
				<span class="rt"><%=number %>天</span>
			<%}else if("3".equals(unit)){%>
				<span class="rt"><%=number %>个月</span>
			<%} %>
			</dd>
			<dd>租金：￥<%=totalAmount %></dd>
			<% }else if("10".equals(json.getJSONObject("data").getString("waceType"))){
				  String startDate="";
			        String endDate="";
			        String totalAmount="";
			        String totalDay="";
			        if("1".equals(flag)){
			        	startDate=json.getJSONObject("data").getString("orderEndDate").substring(0,16);
			        	endDate=json.getJSONObject("data").getString("nextEndDate").substring(0,16);
			        	totalAmount=json.getJSONObject("data").getString("nextAmount");
			        }else {
			        	startDate=json.getJSONObject("data").getString("orderStartDate").substring(0,16);
			        	endDate=json.getJSONObject("data").getString("orderEndDate").substring(0,16);
			        	totalAmount=json.getJSONObject("data").getString("orderTotalAmount");
			        }
			        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm"); 
			    	Date beginTime =  df.parse(startDate);
			    	Date endTime =  df.parse(endDate);
			    	 long diff = endTime.getTime() - beginTime.getTime();
			    	 long days = diff/(1000 * 60 * 60 * 24);
			    	 totalDay=Long.toString(days);
			    	 totalFee=totalAmount;
			        %>
			<dd>类型：<%=json.getJSONObject("data").getString("waceName") %></dd>
			<dd>仓位编号：<%=json.getJSONObject("data").getString("waceNumber") %></dd>
			<dd>租用时间：</dd>
			<dd><%=startDate %>至<%=endDate %><span class="rt"><%=totalDay %>天</span></dd>
			<dd>租金：￥<%=totalAmount %></dd>
			<%}else{
				    String startDate="";
			        String endDate="";
			        String totalAmount="";
			        String totalDays="";
			        String orderDeposit="0";
			        Double t=0.0;
			        if("1".equals(flag)){
			        	startDate=json.getJSONObject("data").getString("orderEndDate");
			        	endDate=json.getJSONObject("data").getString("nextEndDate");
			        	totalAmount=json.getJSONObject("data").getString("nextAmount");
			        	orderDeposit="0";
			        	totalFee=totalAmount;
			        }else {
			        	startDate=json.getJSONObject("data").getString("orderStartDate");
			        	endDate=json.getJSONObject("data").getString("orderEndDate");
			        	totalAmount=json.getJSONObject("data").getString("orderTotalAmount");
			        	orderDeposit=json.getJSONObject("data").getString("orderDeposit");
			        	t=Double.parseDouble(totalAmount)+Double.parseDouble(orderDeposit);
						DecimalFormat df=new DecimalFormat("######0.00");
						totalFee=df.format(t);
			        }
			        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd"); 
			    	Date beginTime =  df.parse(startDate);
			    	Date endTime =  df.parse(endDate);
			    	 long diff = endTime.getTime() - beginTime.getTime();
			    	 long days = diff/(1000 * 60 * 60 * 24);
			    	 totalDays=Long.toString(days);
			%>
			
			<dd>类型：<%=json.getJSONObject("data").getString("waceName") %>~<%=json.getJSONObject("data").getString("waceSize") %>m³</dd>
			<dd>仓位编号：<%=json.getJSONObject("data").getString("waceNumber") %></dd>
			<dd>租用时间：<%=startDate %>至<%=endDate %><span class="rt"><%=totalDays %>天</span></dd>
			<dd>租金：￥<%=totalAmount %></dd>
			<%} %>
			
		</dl>
		<div class="count">
			总计：<span id = "totalFee">￥<%=totalFee%></span>	
		</div>	
	</div>
	<div class="line15"></div>
	
	<div class="pay_way">
		
		<label style="display: none;">
			<div class="payway_item">
				<img src="payImg/pay_way1.png" class="ico" />
				<span>支付宝</span>
			</div>
			<input type="radio" class="chk" name="payway" style="display:none;" />
		</label>
		<label>
			<div class="payway_item payway_itemhover">
				<img src="payImg/pay_way2.png" class="ico" />
				<span>微信支付</span>
			</div>
			<input type="radio" class="chk" name="payway" checked="checked" style="display:none;" />
		</label>
			
	</div>
	<div class="line15"></div>
	<span href="#" class="pay_btn" style="background:#00aaee;">支付</span>
	<div class="weui_dialog_alert" style="display:none;">
	    <div class="weui_mask"></div>
	    <div class="weui_dialog">
	        <div class="weui_dialog_hd"><strong class="weui_dialog_title">提示</strong></div>
	        <div class="weui_dialog_bd">弹窗内容，告知当前页面信息等</div>
	        <div class="weui_dialog_ft">
	            <a href="javascript:closeAlert();" class="weui_btn_dialog primary" onclick="closeAlert()">确定</a>
	        </div>
	    </div>
	</div>
	<%--<form id="orderForm"  form_autoplacement="true"--%>
			<%--form_onComplete="memberForm_onComplete" form_loadingable="false"--%>
			<%--action="<%=request.getContextPath()%>/orderAction!updateOrderStatus.do"--%>
			<%--method="post" enctype="multipart/form-data">--%>
			<%--<input id="orderIdInput" name="orderId" value='<%=orderId %>' type="hidden"/>--%>
			<%--</from>--%>
</body>
<script type="text/javascript" src="/web/whc/common/js/jquery-1.11.1.min.js"></script>
<script type="text/javascript" src="js/jquery.flexslider-min.js"></script>
<script type="text/javascript" src="js/base.js"></script>
<script type="text/javascript" >

	//payway_item 的选中效果
	$(".payway_item").on("click",function(){
		$(".payway_item").removeClass("payway_itemhover");
		$(this).addClass("payway_itemhover");
	});
	$(".pay_btn").on("click",function(){
		var openId = "<%=_weixinPayPluging_openId%>";
		alert(openId);
		$(".pay_btn").hide();
		var orderId = "<%=orderId%>";
		var orderIdNew = "<%=orderIdNew%>";
		var totalFee = '<%=totalFee%>';
		var describe = "<%=json.getJSONObject("data").getString("waceName") %>";
		var oldOrderId='<%=orderId%>';
		var waceNumber='<%=json.getJSONObject("data").getString("waceNumber")%>';
		var weipayFlag='<%=flag%>';
		var flag="0";
		var memo="";
		var succURL=null;
		var attach =  "支付";
		if(weipayFlag=="0"){		//首次
			succURL ="/web/whc/member/paySuccess.html?cabinetDoorNo="+ waceNumber + "&orderId="+orderId+"&openId="+openId;	//直接开锁
		}else if(weipayFlag=="1"){
			succURL ="/web/whc/member/doDetail.html?orderId=" +orderId ;	 //2017-3-29 不直接开锁
		}
		var failURL	="/web/whc/warehouse/weixinPay2.jsp?orderId="+orderId+"&flag="+weipayFlag;
		var cancURL	="/web/whc/warehouse/weixinPay2.jsp?orderId="+orderId+"&flag="+weipayFlag;

		WeiXinPay(orderIdNew,oldOrderId,flag,memo,totalFee,describe,attach,succURL,failURL,cancURL);
	});
	function WeiXinPay(orderId,oldOrderId,flag,memo,totalFee,describe,attach,succURL,failURL,cancURL){
		var openId = "<%=_weixinPayPluging_openId%>";
		  //totalFee=1;
			$.ajax({
				data:{"orderId":orderId,"oldOrderId":oldOrderId,"flag":flag,"memo":memo,"openId":openId,"totalFee":totalFee,"describe":describe,"attach":attach},
				url:"testWebservice.jsp",
				success:function(msg){
					var timeStamp = new Date().getTime();
					var jsonObj = eval("("+msg+")");
					if(jsonObj.return_code=="SUCCESS"&&jsonObj.result_code=="SUCCESS"){
						WeixinJSBridge.invoke(
							'getBrandWCPayRequest', {
								"appId" : jsonObj.appid,   //公众号名称，由商户传入
								"timeStamp": jsonObj.timeStamp+"",     //时间戳，自1970年以来的秒数
								"nonceStr" : jsonObj.nonce_str, //随机串
								"package" : "prepay_id="+jsonObj.prepay_id,
								"signType" : "MD5",     //微信签名方式:
								"paySign" : jsonObj.sign //微信签名
							},
							function(res){
								if(res.err_msg == "get_brand_wcpay_request:ok" ) {
									location.href = succURL;
									//var _orderForm=document.getElementById("orderForm");
									//_orderForm.submit();
								}   // 使用以上方式判断前端返回,微信团队郑重提示：res.err_msg将在用户支付成功后返回  ok，但并不保证它绝对可靠。
								if(res.err_msg == "get_brand_wcpay_request:fail" ) {
									location.href = failURL;
								}
								if(res.err_msg == "get_brand_wcpay_request:cancel" ) {
									location.href = cancURL;
								}
							}
						);

					}else{
						var jsonObj = eval("("+msg+")");
						//openAlert('提示', jsonObj.err_code_des, '知道了');
						alert(jsonObj.err_code_des);
					}

				},error:function(a,b,c){
					alert("网络繁忙！请稍后重试上一次操作！  b:" +b +" c:" +c +" a:"+ a.responseText);
				}
			});
		}

	//打开微UI Alert弹窗
	function openAlert(_tilte, _content, _buttonText) {
		$('.weui_dialog_alert').show().find('.weui_dialog_title').text(_tilte);
		$('.weui_dialog_alert').find('.weui_dialog_bd').text(_content);
		$('.weui_dialog_alert').find('.weui_btn_dialog ').text(_buttonText);
	}
	//关闭微信UI Alert弹窗
	function closeAlert() {
		$('.weui_dialog_alert').hide();
	}

</script>
</html>
