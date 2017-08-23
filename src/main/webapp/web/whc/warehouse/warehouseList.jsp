<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="com.wanhuchina.common.util.weixin.cgi.WeixinUtil"%>
<%@ page import="com.wanhuchina.common.util.zk.ZkPropertyUtil" %>
<%@page import="java.text.SimpleDateFormat"%>
<%
	String openId = (String)request.getParameter("openId");
	System.out.println("-----warehouseList仓--openId--："+openId);
	String wahoType=(String)request.getParameter("wahoType");

//String openId = "o7FePswSTzPeQhIvJlC1jCq-lW4M";
	String baseUrl =ZkPropertyUtil.get("baseURL");
	String supportUrl = ZkPropertyUtil.get("supportURL");
	Map<String, String> configMap = null;
	configMap = WeixinUtil.getJsConfigMap(request.getScheme()
			+ "://"
			+ request.getServerName()
			+ request.getRequestURI()
			+ "?"
			+ request.getQueryString(), ZkPropertyUtil.get("corpId"),ZkPropertyUtil.get("secret"));
%>
<!doctype html>
<html>
<head>
	<title>存:(在线预订)</title>
	<meta name="viewport" content="width=device-width,initial-scale=1.0,maximum-scale=1.0,user-scalable=no" />
	<meta charset="utf-8" />
	<meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate" />
	<meta http-equiv="Pragma" content="no-cache" />
	<meta http-equiv="Expires" content="0" />
	<link rel="stylesheet" type="text/css" href="/web/whc/warehouse/css/base.css" />
	<link rel="stylesheet" type="text/css" href="/web/whc/warehouse/css/page3.css" />
	<style>
		.us_footer{
			width:100%;
			position:fixed;
			left:0;
			bottom:0;
			z-index:100;
			color:#464545;
			font-size:16px;
			font-family:"Microsoft YaHei";
			border-top:1px solid #bebebe;
			height:45px;
			line-height:45px;
			background:#f5f5f5;
		}
		div.clear{
			font: 0px Arial; line-height:0;
			height:0;
			overflow:hidden;
			clear:both;
		}
		.us_footer a{
			width:33.33%;
			display:block;
			border-left:1px solid #bebebe;
			float:left;
			margin-left:-2px;
			text-align:center;
			color:#010101;
			text-decoration:none;
		}
		.us_zw{
			height:3.75rem;
			margin-top:1rem;
		}
		.us_footer .shopselect{
			position:absolute;
			bottom:45px;
			left:0%;
			width:32.3%;
			border:solid 1px  #bebebe;

			background-color:#f5f5f5;
			z-index:101;
			display:none;
		}
		.us_footer .shopselect .shopInfo{
			width:100%;
			text-align:center;
			position:relative;
		}

		.shopselect .shopInfo a{
			float:none;
			clear:both;
			width:100%;
			border-left:none;
		}

		.us_footer .shopselect .firstA{
			border-bottom:solid 1px  #e5e5e4;
		}
	</style>
	<script type="text/javascript" src="/web/whc/common/js/jquery-1.11.1.min.js"></script>
	<script type="text/javascript" src="/web/whc/common/js/zepto.js"></script>
	<script type="text/javascript" src="/web/whc/common/js/tmpl.min.js"></script>
	<script charset="utf-8" src="http://map.qq.com/api/js?v=2.exp&libraries=geometry&key=RUWBZ-I5NR4-R7UUS-DTNZL-ZCVHH-UWB5T"></script>
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
</head>
<body>
<div id="zzdiv" style="position:fixed;left:0;top:0;z-index:200;opacity:0.75;width:100%;height:100%;display:none;	"></div>
<div id="showMe" style="display: none;position:absolute; left:40%; top:40%; width:20%; height:20%; z-index:1000;">
	<div id="center" style="position:absolute;">
		<table border="0">
			<tr>
				<td>
						<span id="disp">
							<img alt="" style="width:100%" src="img/loading-.png?v=<%=new SimpleDateFormat("yyyyMMdd").format(new Date()) %>">
						</span>
				</td>
			</tr>
			<tr>
				<td>
						<span id="disp">
							<img alt="" style="width:100%" src="img/loading-.gif?v=<%=new SimpleDateFormat("yyyyMMdd").format(new Date()) %>">
						</span>
				</td>
			</tr>
		</table>
	</div>
</div>

<div class="topbar clearfix" id="ssdiv" style="display:none">
	<a href="javascript:void(0)" class="location" style="margin-top: 5px;"><span id="areaSpan" ">北京</span> <img src="/web/whc/warehouse/img/icon3.png" /> </a>
	<div class="search clearfix">
		<input type="text" class="textBox" placeholder="输入门店、区域名称" id="keyWord"/>
		<a href="javascript:search();" class="find-btn" > <img src="/web/whc/warehouse/img/icon1212.png" class="full" /> </a>
	</div>
</div>
<ul class="cangku-list clearfix" style="display:none" id="uudiv">
	<%if("0".equals(wahoType)){%>
	<li><a href="#" onclick="setType(3, this);">小型仓</a></li>
	<li><a href="#" onclick="setType(4, this);">中型仓</a></li>
	<li><a href="#" onclick="setType(5, this);">大型仓</a></li>
	<li><a href="#" onclick="setType(6, this);">定制仓</a></li>
	<%} else if("1".equals(wahoType)){%>
	<%--<li><a href="#" onclick="setType(2, this);">红酒仓</a></li>--%>
	<%--<li><a href="#" onclick="setType(7, this);">智能储物柜</a></li>--%>
	<%--<li><a href="#" onclick="setType(8, this);">即时储存柜</a></li>--%>
	<li><a href="#" onclick="setPropertyType(10, this);">全部</a></li>
	<li><a href="#" onclick="setPropertyType(0, this);">商业中心</a></li>
	<li><a href="#" onclick="setPropertyType(1, this);">场馆会所</a></li>
	<%--<li><a href="#" onclick="setPropertyType(2, this);">公共交通</a></li>--%>
	<li><a href="#" onclick="setPropertyType(3, this);">旅游景点</a></li>
	<li><a href="#" onclick="setPropertyType(4, this);">医院</a></li>
	<%--<li><a href="#" onclick="setPropertyType(5, this);">社区</a></li>--%>
	<%--<li><a href="#" onclick="setPropertyType(6, this);">写字楼 </a></li>--%>
	<%--<li><a href="#" onclick="setPropertyType(7, this);">创业空间</a></li>--%>
	<%--<li><a href="#" onclick="setPropertyType(9, this);">其他</a></li>--%>

	<%} else{%>
	<li><a href="#" onclick="setType(3, this);">小型仓</a></li>
	<li><a href="#" onclick="setType(4, this);">中型仓</a></li>
	<li><a href="#" onclick="setType(5, this);">大型仓</a></li>
	<li><a href="#" onclick="setType(6, this);">定制仓</a></li>
	<li><a href="#" onclick="setType(2, this);">红酒仓</a></li>
	<%--<li><a href="#" onclick="setType(1, this);">冷冻仓</a></li>--%>
	<li><a href="#" onclick="setType(7, this);">智能储物柜</a></li>
	<li><a href="#" onclick="setType(8, this);">即时储存柜</a></li>
	<%--<li><a href="#" onclick="setType(9, this);">冷藏仓</a></li>--%>
	<%} %>

</ul>
<div class="tab_con" style="margin-bottom:6em" id="warehouseList">

</div>
<a href="tel:4000027287" class="online" style="display:none"> <img src="/web/whc/warehouse/img/online1.png" class="full" /> <span class="onlineClose"></span> </a>
<div class="page-mask"></div>
<div class="windowBox loca-box">
	<div class="inner loca-list clearfix">
		<ul class="clearfix" id="areaBox">
			<li class="curr" onclick="setArea(this);"><a href="#"  >全城</a></li>
			<li onclick="setArea(this);"><a href="#" >东城区</a></li>
			<li onclick="setArea(this);"><a href="#" >西城区</a></li>
			<li onclick="setArea(this);"><a href="#" >崇文区</a></li>
			<li onclick="setArea(this);"><a href="#" >宣武区</a></li>
			<li onclick="setArea(this);"><a href="#" >朝阳区</a></li>
			<li onclick="setArea(this);"><a href="#" >海淀区</a></li>
			<li onclick="setArea(this);"><a href="#" >丰台区</a></li>
			<li onclick="setArea(this);"><a href="#" >石景山区</a></li>
			<li onclick="setArea(this);"><a href="#" >门头沟区</a></li>
			<li onclick="setArea(this);"><a href="#" >房山区</a></li>
			<li onclick="setArea(this);"><a href="#" >通州区</a></li>
			<li onclick="setArea(this);"><a href="#" >顺义区</a></li>
			<li onclick="setArea(this);"><a href="#" >昌平区</a></li>
			<li onclick="setArea(this);"><a href="#" >大兴区</a></li>
			<li onclick="setArea(this);"><a href="#" >平谷区</a></li>
			<li onclick="setArea(this);"><a href="#" >怀柔区</a></li>
			<li class="last2"></li>
			<li class="last"></li>
		</ul>
		<div class="line"></div>
		<div class="cur" style="line-height: 3em">
			<img src="/web/whc/warehouse/img/icon5.png" />
			<span class="ccity"><i class="col666">当前城市：</i><i class="col000">北京</i></span>
		</div>
		<a href="" class="qiehuan" style="line-height: 3em"><i>切换</i>   <img src="/web/whc/warehouse/img/icon4.png" /></a>
	</div>
</div>
<div class="us_footer">
	<div class="shopselect" id="shopSelect">
		<p class="shopInfo firstA"><a href="/web/whc/warehouse/warehouseList.jsp?openId=<%=openId %>&wahoType=0" >自助迷你仓</a></p>
		<p class="shopInfo firstA"><a href="http://wap.wanhuchina.com">微官网</a></p>
		<p class="shopInfo"><a href="http://mp.weixin.qq.com/mp/getmasssendmsg?__biz=MzAxMjUxNTgyMA==#wechat_webview_type=1&wechat_redirect" >往期回顾</a></p>
	</div>
	<div class="clear"></div>
	<!--<a href="javascript:selectShop();">万户有仓</a>
    -->
	<a href="/web/whc/warehouse/storeList.html?openId=<%=openId %>&wahoType=11">存</a>
	<a onclick="clickMenu(this, '/web/whc/member/center.html?openId=<%=openId %>');">取</a>
	<a href="/web/whc/warehouse/warehouseList.jsp?openId=<%=openId %>&wahoType=0">自助仓</a>
</div>
<%@ include file="/web/whc/warehouse/warehouseTmpl.jsp" %>
<%@ include file="/web/whc/warehouse/warehouseTmpl2.jsp" %>
<script type="text/javascript">

	var globalVar = {
		baseUrl : '<%=baseUrl %>',
		type : null,
		area : null,
		propertyType : null
	}
	var start;
	function getData(sUrl, sData) {// 封装好的Ajax方法 获取数据
		var returnData;
		$.ajax({
			async : false,
			type : "post",
			url : sUrl,
			data : sData,
			beforeSend:function(){
				$("#zzdiv").show();
				$("#showMe").show();


			},
			complete:function(){
				$("#showMe").hide();
				$("#zzdiv").hide();
				$("#ssdiv").show();
				$("#uudiv").show();
				$('.online').show();
			},
			success : function(data) {
				returnData = data;
			},
			dataType : "json",
			timeout : 2000,
			error : function() {
				alert("服务器错误，请稍后重试!");
			}
		});
		return returnData;
	}
	function show(){
		$("#zzdiv").show();
		$("#showMe").show();

	}
	function compare(propertyName) {
		return function (object1, object2) {
			var value1 = object1[propertyName];
			var value2 = object2[propertyName];
			if (value2 < value1) {
				return 1;
			}
			else if (value2 > value1) {
				return -1;
			}
			else {
				return 0;
			}
		}
	}
	function openWarehouse(_warehouseId,_type){
		if("0"==_type){
			var _url = globalVar.baseUrl + "web/whc/warehouse/selectWarehouseCell.jsp?openId=<%=openId%>&warehouseId=" + _warehouseId;
		}else{
			var _url = globalVar.baseUrl + "web/whc/warehouse/selectIntelligentWarehouseCell.jsp?openId=<%=openId%>&warehouseId=" + _warehouseId;
		}
		window.location.href = _url + '#mp.weixin.qq.com';
	}
	function getWarehouseList(_keyWord, _area, _type,_PropertyType){
		wx.getLocation({
			success: function (res) {
				var lati = res.latitude; // 纬度，浮点数，范围为90 ~ -90
				var longi = res.longitude; // 经度，浮点数，范围为180 ~ -180
				start = new qq.maps.LatLng(lati, longi);
				var _url = globalVar.baseUrl + "getStorehouseList?_webserviceName=GetWarehouseList&keyWord="
						+ _keyWord + "&area=" + _area + "&type=" + _type+"&PropertyType="+_PropertyType+"&wahoType="+'<%=wahoType%>';
				var data = getData(_url, "");
				//alert("code:"+data.code);
				$('#warehouseList').empty();
				if(data.code=="10000") {
					var warehouseList = data.data;
					var _html1 = tmpl("tmpl-warehouseList", warehouseList);
					$('#warehouseList').html(_html1);
				}

			},
			fail:function(res){
				var _url = globalVar.baseUrl + "getStorehouseList?_webserviceName=GetWarehouseList&keyWord="
						+ _keyWord + "&area=" + _area + "&type=" + _type+"&PropertyType="+_PropertyType+"&wahoType="+'<%=wahoType%>';

				var data = getData(_url, "");
				//alert("code:"+data.code);
				$('#warehouseList').empty();
				if(data.code=="10000") {
					var warehouseList = data.data;
					var _html1 = tmpl("tmpl-warehouseList2", warehouseList);
					$('#warehouseList').html(_html1);
				}
			}
		});

	}
	function clickMenu(_obj, _url) {

		window.location.href = globalVar.baseUrl + _url;
	}
	//$("#shopSelect").on('click',function(){return false;});
	function selectShop(){
		$("#shopSelect").toggle();
	}
	function setPropertyType(_PropertyType,_obj){
		$('.cangku-list').children().each(function () {
			$(this).removeClass('curr');
		});

		$(_obj).parent().addClass('curr');
		if(_PropertyType=='10'){
			globalVar.propertyType =null;
		}else{
			globalVar.propertyType = _PropertyType;
		}

		getWarehouseList($('#keyWord').val() || null, globalVar.area, null,globalVar.propertyType);
		$(".page-mask").hide();
		$(".repository-box").hide();
	}
	function setType(_type, _obj) {
		$('.cangku-list').children().each(function () {
			$(this).removeClass('curr');

		});

		$(_obj).parent().addClass('curr');
		globalVar.type = _type;
		getWarehouseList($('#keyWord').val() || null, globalVar.area, globalVar.type,null);
		$(".page-mask").hide();
		$(".repository-box").hide();
	}
	function setArea(_obj) {
		var _area = $(_obj).children('a').html();
		$('#areaSpan').html(_area);
		if(_area == '全城') _area = null;
		$('#areaBox').children().each(function () {
			$(this).removeClass('curr');
		});

		$(_obj).addClass('curr');
		globalVar.area = _area;
		getWarehouseList($('#keyWord').val() || null, globalVar.area, globalVar.type,globalVar.propertyType);
		$(".page-mask").hide();
		$(".loca-box").hide();
	}
	function search() {
		getWarehouseList($('#keyWord').val() || null, globalVar.area, globalVar.type,globalVar.propertyType);
	}
	$(function(){
		$(".onlineClose").on("touchstart",function(){
			$(".online").hide();
			return false;
		});
		$(".page-mask").on("touchstart",function(){
			$(this).hide();
			$(".windowBox").hide();
		});
		$(".location").on("touchstart",function(){
			$(".page-mask").show();
			$(".loca-box").show();
		});
		$(".tabs li").each(function(i){
			$(this).on("touchstart",function(){
				$(this).addClass("cur").siblings().removeClass("cur");
				$(".tab_con").hide();
				$(".tab_con").eq(i).show();
			});
		});
	});
	wx.ready(function(){
		show();
		getWarehouseList(null, null, null,null);
	});
</script>

</body>
</html>
