<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="com.google.common.base.Strings" %>
<%@ page import="com.wanhuchina.common.util.zk.ZkPropertyUtil" %>
<%@ page import="com.wanhuchina.common.util.http.ApiUtils" %>
<%@ page import="com.whc.wx.web.controller.order.service.impl.WarehouseServcieImpl" %>
<%@ page import="com.alibaba.fastjson.JSONObject" %>
<%@ page import="com.alibaba.fastjson.JSONArray" %>

<%
	String openId = (String) request.getParameter("openId");
	System.out.println("-----selectwarehouseCell仓--openId--："+openId);
	String warehouseId = (String) request.getParameter("warehouseId");
	String warehouseCellId = Strings.isNullOrEmpty((String)request.
			getParameter("warehouseCellId")) ? "" : (String) request
			.getParameter("warehouseCellId");
	String baseUrl = ZkPropertyUtil.get("baseURL");
	Map<String,String> memberMap = new HashMap<String, String>();
	memberMap.put("openId",openId);
	String memberURL = ZkPropertyUtil.get("BaseUrlMember")+"memberManage/getMemberByOpenId";
	JSONObject memberjson = new JSONObject();
	memberjson = ApiUtils.excutePost(memberURL,null,null,memberMap);
	String memberId = null;
	String memberName = null;
	if(memberjson.getIntValue("code")==10000){
		memberId = memberjson.getJSONObject("data").getString("id");
		memberName = memberjson.getJSONObject("data").getString("name");

	}else{
		memberId="";
		memberName = "";
	}
	WarehouseServcieImpl iWarehouseService = new WarehouseServcieImpl();
	JSONObject json = iWarehouseService.getWarehouseData(warehouseId);
	JSONArray array = new JSONArray();
	try {
		array = json.getJSONArray("warehouseCellList");
	} catch (Exception e) {
		e.printStackTrace();
	}
	String supportUrl = ZkPropertyUtil.get("supportURL");
%>
<!doctype html>
<html>
	<head>
		<title><%=json.getString("name")%></title>
		<meta name="viewport"
			content="width=device-width,initial-scale=1.0,maximum-scale=1.0,user-scalable=no" />
		<meta charset="utf-8" />
		<link rel="stylesheet" type="text/css"
			href="/web/whc/warehouse/css/base.css" />
		<link rel="stylesheet" type="text/css"
			href="/web/whc/warehouse/css/page11.css" />
	
		<script type="text/javascript">


		function start1DateSelectBefore(){
			if(!globalVar.curCell) {
				$('.failed .p2').html('请选择点击<em> 仓位类型和体积</em>！');
				$(".page-mask").show();
				$(".failed").show();
				$('#start1Date').val('');
				return;
			  } 
			}

		  function end1DateSelectBefore(){
          var start1Date=$('#start1Date').val();
          if( start1Date==""|| start1Date== null){
        	  $('.failed .p2').html('请选择先选择入仓日期！');
				$(".page-mask").show();
				$(".failed").show();
				$('#end1Date').val('');
				return;
            }
			}

			 function countDay(){
				 
               var startDate=$('#start1Date').val();
               var endDate=$('#end1Date').val();
			   
    	      	if(startDate!="" && endDate !=""){
    	      		var _startDate = new Date();
        	      	_startDate.setTime(Date.parse(startDate.replace(/-/g,"/")));
        	      	var _endDate = new Date();
        	      	_endDate.setTime(Date.parse(endDate.replace(/-/g,"/")));
					  if(_startDate >_endDate){
						   $('#days').text("0天");
						   $('#hejiTd').text('合计： ￥0元');
						  
					return;
					
		      	  }
    	           getDays(_startDate, _endDate);
    	      	}
			}
		  //add by fjc 20151028 reason:租用天数是一个月零几天的格式
		function getDays(_startDate,_endDate) { 
	      	  $('#days').text('0');
	      	$('#hejiTd').text('0');
			// 计算年份相差月份
			var _startYear = _startDate.getFullYear();// 开始日期-年份
			var _endYear = _endDate.getFullYear();// 结束日期-年份
			var _startMon = _startDate.getMonth()+1;// 开始日期-月份
			var _endMon = _endDate.getMonth()+1;// 结束日期-月份
			var _startDay = _startDate.getDate();// 开始日期-日
			var _endDay = _endDate.getDate();// 开始日期-日
			//计算年的数量
			var _yearNumber=_endYear - _startYear;
			//计算月的数量
			var _yearToMonth = parseFloat(_yearNumber) * 12;
			var _monthToMonth = _endMon - _startMon;
			var _monthNumber=_monthToMonth-_yearToMonth;
			 var startDate=$('#start1Date').val();
             var endDate=$('#end1Date').val();
           
			var _days=getMonthAndDay(startDate,endDate);
			  var _totalDay = getOffDays(_endDate.getTime(), _startDate.getTime());
			     var arr=new Array();
			     var days="";
			     arr=_days.split('-');
			     var month=parseInt(arr[0]);
			     var day=parseInt(arr[1]);
			     if(month==0){
                      days=day+"天";
				    }else if(month>0 && month<12){
					     if(day==0){
					    	 days=month+"月";
						     }else{
                            days=month+"月"+day+"天";
						     }
					}else if(month>=12){
						var yc=parseInt(month/12);
						var m=month%12;
						if(m==0){
						    if(day==0){
					    	 days=yc+"年";
						     }else{
                            days=yc+"年"+day+"天";
						     }

						}else{
							if(day==0){
					    	 days=yc+"年"+m+"月";
						     }else{
							 days=yc+"年"+m+"月"+day+"天";
						     }
						}
						   
					}
			  $('#totalDayInput').val(_totalDay);
			  $('#days').text(days);
                
			  var _curCell = $(globalVar.curCell);
				var _dayPrice = _curCell.attr("data-dayPrice");
				var _monthPrice = _curCell.attr("data-monthPrice");
				var _halfPrice = _curCell.attr("data-halfPrice");
				var _yearPrice = _curCell.attr("data-yearPrice");
				var _specialPrice = _curCell.attr("data-specialPrice");
				var _size = _curCell.attr("data-size");
				var _price = 0;
				 var _amount="";
				  var d=new Date(_endYear,_endDate.getMonth()-1,0);
		           var monthDay=d.getDate();
				if(month == 0) {
					_price = _dayPrice;
					  _amount =day* _price * _size;
				} else if(month <6 && month>0) {
					_price = _monthPrice;
					  _amount =(month * _price * _size)+((day/monthDay) * _price * _size);
				} else if(month >=6 &month <12) {
					_price = _halfPrice;
					  _amount =(month * _price * _size)+((day/monthDay) * _price * _size);
				} else if(month >=12) {
					_price = _yearPrice;
					  _amount =(month * _price * _size)+((day/monthDay) * _price * _size);
				}
				if(_specialPrice && _specialPrice != '0'&& month >= 1) {
					_price = _specialPrice;
					 _amount =(month * _price * _size)+((day/monthDay) * _price * _size);
				}
				_amount = Math.floor(_amount);
				$('#hejiTd').text('合计： ￥' + _amount + '元');
				$('#amountInput').val(_amount);
	    }
		
		function getMonthAndDay(startDate,endDate){
			var _startDate = new Date();
	      	_startDate.setTime(Date.parse(startDate.replace(/-/g,"/")));
	      	var _endDate = new Date();
	      	_endDate.setTime(Date.parse(endDate.replace(/-/g,"/")));

	      	var date1 = startDate.split('-');
	     // 得到月数
	    var tempStartMonthCount = parseInt(date1[0]) * 12 + parseInt(date1[1]);
	     // 拆分年月日
	     date2 = endDate.split('-');
	     // 得到月数
	      var tempEndMonthCount = parseInt(date2[0]) * 12 + parseInt(date2[1]);
	     var month = Math.abs(tempStartMonthCount - tempEndMonthCount);
	     var _startYear = _startDate.getFullYear();// 开始日期-年份
			var _endYear = _endDate.getFullYear();// 结束日期-年份
			var _startMon = _startDate.getMonth();// 开始日期-月份
			var _endMon = _endDate.getMonth();// 结束日期-月份
			var _startDay = _startDate.getDate();// 开始日期-日
			var _endDay = _endDate.getDate();// 开始日期-日
			 var days;
			 var t=parseInt(month/12);
			 var t2=month%12;
		     days=getOffDays(_startDate.getTime(),_endDate.getTime())+1;
            if(month==0){
				days=getOffDays(_startDate.getTime(),_endDate.getTime())+1;
				var temp=new Date(_startYear,_startMon+1,0);
				if(days==temp.getDate()){
                 month=month+1;
                 days=0;
                  }
				
			}else if(month==1){
								var temp=new Date(_startYear,_endMon,_startDay);
								var tempDay=getOffDays(_startDate.getTime(),temp.getTime());
								var tempC=days-tempDay;
								if(tempC>=0){
									days=tempC;
									var temp=new Date(_startYear,_startMon+1,0);
				                   if(days>=temp.getDate()){
                                    month=month+1;
                                      days=days-temp.getDate();
                                     }
								}else{
									days=getOffDays(_startDate.getTime(),_endDate.getTime())+1;
                                      month=0;	
				                   
								}
								

			}else if(month>1){
				var tempMonthCount=_startMon+1+month;
				if(tempMonthCount<12){
                      				var temp=new Date(_startYear,_endMon,_startDay);
									var tempDay=getOffDays(_startDate.getTime(),temp.getTime());
									var tempC=days-tempDay;
									if(tempC>=0){
									days=tempC;
									   var temp=new Date(_startYear,_endMon,0);
									   if(days>=temp.getDate()){
                                       month=month+1;
                                              days=days-temp.getDate();
                                     }

								    }else{
									 temp=new Date(_startYear,_endMon-1,_startDay);
									days=getOffDays(temp.getTime(),_endDate.getTime())+1;
                                      month=month-1;	
									  var temp=new Date(_startYear,_endMon-1,0);
				                      if(days>=temp.getDate()){
                                       month=month+1;
                                              days=days-temp.getDate();
                                     }
				                   
								    }


					
				}else if(tempMonthCount>=12){
					    var temp=new Date(_endYear,_endMon,_startDay);
						var tempDay=getOffDays(_startDate.getTime(),temp.getTime());
                         var tempC=days-tempDay;
									if(tempC>=0){
									days=tempC;
								    }else{
									 temp=new Date(_endYear,_endMon-1,_startDay);
									days=getOffDays(temp.getTime(),_endDate.getTime())+1;
                                      month=month-1;	
									  temp=new Date(_endYear,_endMon,0);
				                      if(days==temp.getDate()){
                                       month=month+1;
                                              days=0;
                                     }
				                   
								    }
				}
				
			}
           
		return month+"-"+days;
        
		}

		function  dateDiff(sDate1,  sDate2){    
		         //sDate1和sDate2是2006-12-18格式    
		          var  aDate,  oDate1,  oDate2,  iDays    
		          aDate  =  sDate1.split("-")    
		          oDate1  =  new  Date(aDate[1]  +  '/'  +  aDate[2]  +  '/'  +  aDate[0])    //转换为12-18-2006格式    
		          aDate  =  sDate2.split("-")    
		          oDate2  =  new  Date(aDate[1]  +  '/'  +  aDate[2]  +  '/'  +  aDate[0])    
		          iDays  =  parseInt(Math.abs(oDate1  -  oDate2)  /  1000  /  60  /  60  /24)    //把相差的毫秒数转换为天数   
		          return  iDays;  
		}
		</script>
	</head>
	<body>

		<div class="slide" id="slide3">
			<ul>
				<%
					int imageCount = 0;
					if (json.containsKey("image1")) {
						imageCount++;
				%>
				<li>
					<img
						src="<%=supportUrl%>app/whc/uploadFile/import/<%=json.getString("image1")%>"
						alt="">
				</li>
				<%
					}
				%>
				<%
					if (json.containsKey("image2")) {
						imageCount++;
				%>
				<li>
					<img
						src="<%=supportUrl%>app/whc/uploadFile/import/<%=json.getString("image2")%>"
						alt="">
				</li>
				<%
					}
				%>
				<%
					if (json.containsKey("image3")) {
						imageCount++;
				%>
				<li>
					<img
						src="<%=supportUrl%>app/whc/uploadFile/import/<%=json.getString("image3")%>"
						alt="">
				</li>
				<%
					}
				%>
				<%
					if (json.containsKey("image4")) {
						imageCount++;
				%>
				<li>
					<img
						src="<%=supportUrl%>app/whc/uploadFile/import/<%=json.getString("image4")%>"
						alt="">
				</li>
				<%
					}
				%>
				<%
					if (json.containsKey("image5")) {
						imageCount++;
				%>
				<li>
					<img
						src="<%=supportUrl%>app/whc/uploadFile/import/<%=json.getString("image5")%>"
						alt="">
				</li>
				<%
					}
				%>
			</ul>
			<div class="dot">
				<%
					for (; imageCount > 0; imageCount--) {
				%>
				<span></span>
				<%
					}
				%>
			</div>
		</div>
		<div class="bar clearfix">
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td width="16%">
						<div class="img">

							<img src="/web/whc/warehouse/img/img10.jpg" class="full" />
						</div>
					</td>
					<td width="84%">
						<p class="p1">
							<%=json.getString("preferential")%>
						</p>
					</td>
				</tr>
			</table>
		</div>
		<div class="block">
			<div class="inblock">
				<div class="adress clearfix">
					<div class="l ll">
						<img src="/web/whc/warehouse/img/icon5.png" alt="" class="img1" />
						<span class="ss1"><%=json.getString("addr")%></span>
					</div>
					<div class="r">
						<a
							href="/web/whc/map/map.jsp?openId=<%=openId%>&warehouseId=<%=warehouseId%>">
							<span class="v1 map-w">地图</span> <img
								src="/web/whc/warehouse/img/icon23.png" alt="" class="v1 arrow-w" />
						</a>
					</div>
				</div>

				<div class="line-w"></div>

				<%--<div class="adress comment clearfix">
				<div class="l ll">
					<img src="/app/whc/index/images/icon23242.png" alt="" class="img1" /> <span class="ss1">130 条评论</span>
				</div>
				<div class="r">
					<a href="#"> <span class="v1 map-w">评论</span> <img src="/app/whc/index/images/icon23.png" alt="" class="v1 arrow-w" /> </a>
				</div>
			</div>

			<div class="line-w"></div>

			--%>
				<div class="adress events clearfix">
					<div class="l ll">
						<span class="ss1">客服热线：400-002-7287</span>
						<ul class="iconlist">
							<li>
								<a href="#"> <img src="/web/whc/warehouse/img/icon50.png"
										alt="" class="icon1" /> </a>
							</li>
							<li>
								<a href="#"> <img src="/web/whc/warehouse/img/icon51.png"
										alt="" class="icon2" /> </a>
							</li>
							<li>
								<a href="#"> <img src="/web/whc/warehouse/img/icon52.png"
										alt="" class="icon3" /> </a>
							</li>
							<li>
								<a href="#"> <img src="/web/whc/warehouse/img/icon53.png"
										alt="" class="icon4" /> </a>
							</li>
							<li>
								<a href="#"> <img src="/web/whc/warehouse/img/icon54.png"
										alt="" class="icon5" /> </a>
							</li>
							<li>
								<a href="#"> <img src="/web/whc/warehouse/img/icon55.png"
										alt="" class="icon6" /> </a>
							</li>
						</ul>
					</div>
					<div class="r">
						<a
							href="warehouseDetail.jsp?openId=<%=openId%>&warehouseId=<%=warehouseId%>">
							<span class="v1 map-w">详情</span> <img
								src="/web/whc/warehouse/img/icon23.png" alt="" class="v1 arrow-w" />
						</a>
					</div>
				</div>
			</div>
		</div>

		<%
			if (array.size() > 0) {
		%>
		<div class="block clearfix">
			<p class="p1">
				请点击选择仓位：
			</p>
			<%
				for (int i = 0; i < array.size(); i++) {
						JSONObject wahoceTypeJson = array.getJSONObject(i);
						JSONArray warehouseCellArray = wahoceTypeJson
								.getJSONArray("warehouseCells");
						int length = warehouseCellArray.size();
						if (length > 0) {
			%>
			<div class="img">
				<img src="<%=wahoceTypeJson.getString("icon")%>" class="full"
					style="width: 90%" />
			</div>
			<div class="table">
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<%
							int row = 0;
										for (int j = 0; j < length; j++) {
											row++;
											JSONObject object = warehouseCellArray
													.getJSONObject(j);
											if (!object.containsKey("size") || !object.containsKey("count"))
												continue;
						%>
						<td>
							<div class="td cur" onclick="selectWarehouseCell(this);"
								data-size="<%=object.getString("size")%>"
								data-count="<%=object.getString("count")%>"
								data-dayPrice="<%=object.getString("dayPrice")%>"
								data-monthPrice="<%=object.getString("monthPrice")%>"
								data-halfPrice="<%=object.getString("halfPrice")%>"
								data-yearPrice="<%=object.getString("yearPrice")%>"
								data-specialPrice="<%=object.getString("specialPrice")%>"
								data-typeId="<%=wahoceTypeJson.getString("typeId")%>"
								data-typeCode="<%=wahoceTypeJson.getString("typeCode")%>"
								data-typeName="<%=wahoceTypeJson.getString("typeName")%>">
								<p style="color: #f67f00;">
									<em class="s1"><%=object.getString("size")%></em>m3
								</p>
								<%
									if (object.getDouble("specialPrice") > 0) {
								%>
								<p>
									<span class="s4">￥<em class="s2"><%=object.getString("specialPrice")%></em>
									</span>
								</p>
								<p style="text-decoration: line-through; color: #f67f00">
									￥
									<em class="s3"><%=object.getString("monthPrice")%></em>
								</p>
								<%
									} else {
								%>
								<p style="line-height: 2.4">
									<span class="s4">￥<em class="s2"><%=object.getString("monthPrice")%></em>
									</span>
								</p>
								<%
									}
								%>
							</div>
						</td>
						<%
							if (row == 3) {
												row = 0;
						%>
					</tr>
					</tr>
					<%
						}
									}
									if (row % 3 == 1) {
					%>
					<td>
						<div class="td " style="display: none;">
							<p>
								<em class="s1">x</em>x
							</p>
							<p>
								<span class="s4">x<em class="s2">x</em>
								</span>
							</p>
							<p>
								<em class="s3">x</em>
							</p>
						</div>
					</td>
					<td>
						<div class="td " style="display: none;">
							<p>
								<em class="s1">x</em>x
							</p>
							<p>
								<span class="s4">x<em class="s2">x</em>
								</span>
							</p>
							<p>
								<em class="s3">x</em>
							</p>
						</div>
					</td>
					<%
						} else if (row % 3 == 2) {
					%>
					<td>
						<div class="td " style="display: none;">
							<p>
								<em class="s1">x</em>x
							</p>
							<p>
								<span class="s4">￥<em class="s2">x</em>
								</span>
							</p>
							<p>
								<em class="s3">x</em>
							</p>
						</div>
					</td>
					<%
						}
					%>
					</tr>
				</table>
			</div>
			<%
				}
					}
			%>
		</div>
		<%
			}
		%>
	
	
		
		<div class="block">
			<div class="inblock">
				<div class="adress name clearfix">
					<div class="l">
						<span class="ss2">入仓日期<font color="red">&nbsp;&nbsp;*</font>
						</span>
					</div>
					<div class="r" >
						<input type="date"  id="start1Date"   onblur="countDay()" onchange="countDay()"
							 class="textBox">

					
					</div>
				</div>
				<div class="adress name clearfix">
					<div class="l">
						<span class="ss2">出仓日期<font color="red">&nbsp;&nbsp;*</font>
						</span>
					</div>
					<div class="r" >
						<input type="date" id="end1Date"  
							class="textBox"  onchange="countDay()" >
							 <input type="hidden" name="" id=""/>
					</div>
				</div>
				<div class="adress rucang clearfix">
					<div class="l">
						<span class="ss2">租用天数</span>
					</div>
					<div class="r">
						<span class="v1 map-w"><strong id="days"
							style="color: orange">0天</strong></span>
					</div>
				</div>
				<div class="adress name clearfix">
					<div class="l">
						<span class="ss2">客户姓名<font color="red">&nbsp;&nbsp;*</font>
						</span>
					</div>
					<div class="r">
						<input id="custName" type="text" value="<%=memberName%>"
							class="textBox" />
					</div>
				</div>
				<div class="adress name clearfix">
					<div class="l">
						<span class="ss2">客户手机<font color="red">&nbsp;&nbsp;*</font>
						</span>
					</div>
					<div class="r">
						<input id="custTel" type="text" placeholder="请填写您的电话"
							class="textBox" />
					</div>
				</div>
				<div class="adress name clearfix">
					<div class="l">
						<span class="ss2">留 言</span>
					</div>
					<div class="r">
						<textarea placeholder="请填写您的留言" class="msg"></textarea>
					</div>
				</div>
			</div>
		</div>

		<div class="block" style="font-size: 12px;">
			<div class="inblock zhinan">
				<h3 class="title">
					选仓指南：
				</h3>
				<div class="img1">
					<img src="/web/whc/warehouse/img/img15.png " alt="" class="full" />
				</div>
				<p class="p1">
					小型仓 (<3m3)
				</p>
				<p class="p2">
					足够容纳12个中型纸箱 或 6个标准登机箱
				</p>
				<ul class="list">
					<li>
						家居物品（换季衣服、照片、玩具等等）
					</li>
					<li>
						小家电
					</li>
					<li>
						私密个人物品
					</li>
					<li>
						艺术品及收藏品（字画、小型雕塑、模型）
					</li>
					<li>
						小型乐器
					</li>
					<li>
						文件档案
					</li>
					<li>
						建材及电动工具
					</li>
					<li>
						商用设备（实验室、医疗、科研仪器）
					</li>
				</ul>
				<div class="img1">
					<img src="/web/whc/warehouse/img/img16.png" alt="" class="full" />
				</div>
				<p class="p1">
					中型仓 (3m3-10m3)
				</p>
				<p class="p2">
					足够容纳38个中型纸箱 或 小型公寓的家具家电
				</p>
				<ul class="list">
					<li>
						家具（单人床、沙发、双门衣柜、办公桌）
					</li>
					<li>
						家电（电视、洗衣机、冰箱）
					</li>
					<li>
						大件衣物及道具（婚纱、长裙、裘皮大衣）
					</li>
					<li>
						艺术品及收藏品（字画、小型雕塑、模型）
					</li>
					<li>
						运动器材（高尔夫球杆、皮划艇、滑雪板、山地车、跑步机）
					</li>
					<li>
						乐器（提琴、铜号、古筝、竖琴）
					</li>
					<li>
						展柜、展架、样品
					</li>
					<li>
						建材及电动工具
					</li>
					<li>
						货物（淘宝店、小型商户库存）
					</li>
					<li>
						大型商用设备（实验室、医疗、科研仪器）
					</li>
				</ul>
				<div class="img1">
					<img src="/web/whc/warehouse/img/img17.png" alt="" class="full" />
				</div>
				<p class="p1">
					大型仓 (10m3-20m3)
				</p>
				<p class="p2">
					足够容纳70个中型纸箱 或 1居室公寓的家具家电
				</p>
				<ul class="list">
					<li>
						家具（双人床、沙发、三门衣柜、餐桌）
					</li>
					<li>
						家电（电视、洗衣机、冰箱）
					</li>
					<li>
						大件衣物及道具（婚纱、长裙、裘皮大衣）
					</li>
					<li>
						艺术品及收藏品（大型字画、雕塑、模型）
					</li>
					<li>
						运动器材（高尔夫球杆、皮划艇、滑雪板、山地车、跑步机）
					</li>
					<li>
						大型乐器（钢琴、架子鼓）
					</li>
					<li>
						展柜、展架、样品
					</li>
					<li>
						建材及电动工具
					</li>
					<li>
						货物（淘宝店、小型商户库存）
					</li>
					<li>
						大型商用设备（实验室、医疗、科研仪器）
					</li>
				</ul>
				<div class="img1">
					<img src="/web/whc/warehouse/img/img18.png" alt="" class="full" />
				</div>
				<p class="p1">
					定制仓 (>20 m3)
				</p>
				<p class="p2">
					足够容纳116个中型纸箱 或 1-2居室公寓的家具家电
				</p>
				<ul class="list">
					<li>
						家具（特大双人床、大型沙发、四门衣柜、大型餐桌）
					</li>
					<li>
						家电（电视、洗衣机、冰箱）
					</li>
					<li>
						大件衣物及道具（婚纱、长裙、裘皮大衣）
					</li>
					<li>
						艺术品及收藏品（大型字画、雕塑、模型）
					</li>
					<li>
						运动器材（高尔夫球杆、皮划艇、滑雪板、山地车、大型器械）
					</li>
					<li>
						大型乐器（钢琴、架子鼓）
					</li>
					<li>
						展柜、展架、样品
					</li>
					<li>
						建材及电动工具
					</li>
					<li>
						货物（淘宝店、小型商户库存）
					</li>
					<li>
						大型商用设备（实验室、医疗、科研仪器）
					</li>
				</ul>
				<h3 class="title">
					用户须知：
				</h3>
				<!-- 	<p class="p3">万户仓存储须知</p> -->
				<p class="p4">
					一、存放条例
				</p>
				<p class="p3">
					以下物品为禁止存放物品，禁止在万户仓中存放：
				</p>
				<p class="p3">
					1.易燃、易爆、易渗漏、易挥发、易潮物品；
				</p>
				<p class="p3">
					2.有毒有害、有辐射、化学性、突变性危险品；
				</p>
				<p class="p3">
					3.易腐烂、易霉变、易变质及超限等特殊物品；
				</p>
				<p class="p3">
					4.毒品、枪支弹药等违禁违纪类物品；
				</p>
				<p class="p3">
					5.法律法规等规定禁止的其他物品。
				</p>
				<p class="p4">
					二、登记管理
				</p>
				<p class="p3">
					1.客户为个人的，开仓时需持本人合法有效的身份证件（身份证、军官证、回乡证、港澳通行证、护照），经万户仓审验认为符合条件的，则填写相关表单，办妥开仓手续后，领取由万户仓提供的门禁磁卡。
				</p>
				<p class="p3">
					2.客户授权委托他人代理，应出具《授权委托书》指定授权代理人，明确授权范围和授权期限，代理人也须出示合法有效的身份证件，登录身份证号码，并预留个人资料。
				</p>
				<p class="p3">
					3.客户为两人联名的，开仓时双方均应出示合法有效的身份证件，登记身份证号码，并预留个人资料，所有手续均需两人同时到场办理。
				</p>
				<p class="p3">
					4.客户为单位的，开仓时需持企业营业执照副本复印件（加盖公章）或单位法人签字盖章的合法有效证明文件及代理人身份证件，经万户仓审验认为符合条件的，则填写万户仓《客户服务确认单》，并出具《授权委托书》指定授权代理人，同时登记代理人的身份信息；如授权代理人变更，应重新填制《授权委托书》。
				</p>
				<p class="p3">
					5.客户也可要求自行保管门禁磁卡，客户自行保管门禁磁卡的，客户开仓时万户仓不再进行相关核验。
				</p>
				<p class="p4">
					三、使用期
				</p>
				<p class="p3">
					1.使用期满，客户应及时办理续用或退租手续。退仓时，在结算费用，清除仓内所有物品、恢复仓内原状、交还门禁卡和全部客户钥匙后，办理退仓手续，可凭保证金收据取回保证金，若有箱体损坏，门禁卡丢失，万户仓按公示赔偿标准从保证金中扣收；如果逾期办理退仓、续仓，应补交逾期使用的许可费，并按预期许可费的5%缴纳滞纳金，从保证金中扣收，以上不足部分万户仓有追索权。
				</p>
				<p class="p3">
					2.协议到期或客户未及时缴纳使用费。协议到期或缴费期限到期前七天万户仓按客户预留的联系方式通知客户续费，通知后客户仍未办理缴纳或续仓或退仓手续或客户变更联系方式致使万户仓无法通知的，且期满后15天仍未支付使用费的客户，或者客户没有或忽略遵守履行本协议任何条款的，万户仓可行使以下权利，直至客户结清相应款项：
				</p>
				<p class="p3">
					3.期满后30天仍未支付使用费，或者客户没有或忽略遵守履行本协议任何条款，万户仓可行使以下权利：
				</p>
				<p class="p3">
					a.开启客户被许可使用之仓储单元
				</p>
				<p class="p3">
					b.将客户所存储的物品迁移至万户仓可能决定的其他储存设施，而万户仓不因此项迁移所发生的损失或损毁承担责任
				</p>
				<p class="p3">
					c.要求客户偿付万户仓迁移物品的全部费用和其他地址的储存费
				</p>
				<p class="p3">
					d.终止此协议和将客户所存储的物品视为被弃置，并在之后代客户出售上述物品（通过拍卖或者买卖协议）和将上述物品的所有权转移给买方，代客户销毁或以其他方式处置上述物品，任何出售的收入可由万户仓保留，所得价款优先偿付客户应缴费用，如有不足仍由客户负担，万户仓有权追索。
				</p>
				<p class="p4">
					四、费用
				</p>
				<p class="p3">
					1.万户仓提供存储仓由客户使用，根据存储仓的规格和使用期，收取使用费和保证金，保证金为一个月租金，使用费金额见《客户服务确认单》。
				</p>
				<p class="p3">
					2.其他计算项目包括装卸搬运费、包装整理费、保险费及其他增值服务费，据实际情况计算。
				</p>
				<p class="p3">
					3.存储仓使用费一律预交，最短付费方式为日付，第一期使用费应于开仓时交纳，以后每次到期前七个工作日内交纳下一期使用费。客户也可选择一次性交纳使用费，（不受期间收费标准调整的影响）。保证金是客户的履约保证，不计付利息，使用费、保证金如有调整，均按签约时标准交纳。
				</p>
				<p class="p4">
					五、检查
				</p>
				<p class="p3">
					客户确认，已察看和检查过存储仓及整体门店，并对其面积、大小、适合性、安全性等方面（尤其是安全和安保方面）表示满意。
				</p>
				<h3 class="title">
					租用流程：
				</h3>
				<div class="img2">
					<img src="/web/whc/warehouse/img/img19.jpg" alt="" class="full" />
				</div>
				<div class="img2">
					<img src="/web/whc/warehouse/img/img20.jpg" alt="" class="full" />
				</div>
				<div class="img2">
					<img src="/web/whc/warehouse/img/img21.jpg" alt="" class="full" />
				</div>
				<div class="img2">
					<img src="/web/whc/warehouse/img/img22.jpg" alt="" class="full" />
				</div>
			</div>
		</div>

		<div class="xiadanpop">
			<div class="xiadanHeader">
				<h3>
					请选择下单方式
				</h3>
			</div>
			<div class="xiadanLine">
				<img src="/web/whc/warehouse/img/xiadanLine.jpg" alt="" />
			</div>
			<a href="#" class="weixinyuyue" onclick="submit();">微信预约</a>
			<a href="tel:4000027287" class="dianhuayuyue">电话预约</a>
			<p class="xiadantellphone">
				点击拨打400电话
			</p>
		</div>

		<div class="failed" style="display: none;">
			<p class="p2">
				请填写
				<em> </em>！
			</p>
			<p class="p3">
				<a href="javascript:void(0)" class="btn sureBtn">确定</a>
			</p>
		</div>

		<div class="loading" style="display: none;">
			<p class="p2">
				正在处理数据，请稍候
				<em> </em>...
			</p>
		</div>

		<div class="page-mask"></div>

		<div class="now" onclick="popMenu();">
			<table width="100%">
				<tr>
					<td id="hejiTd" style="font-size: .8em;">

					</td>
					<td rowspan="2" width="40%"
						style="font-weight: bold; padding-right: 5%">
						立即预约
					</td>
				</tr>
				<tr>
					<td id="gongshiTd" style="font-size: .5em;">

					</td>
				</tr>
			</table>
		</div>

		<form id="my-form"
			action="/dataMessageService?_webserviceName=AddOrderByWeixin"
			method="post">
			<input class="s1" type="hidden" name="memberId" id="memberIdInput"
				value="<%=memberId%>" />
			<input class="s1" type="hidden" name="status" id="statusInput"
				value="5" />
			<!--
		<input type="hidden" name="order.type" id="typeInput" value="0"/>
		-->
			<input class="s1" type="hidden" name="wahoceId" id="wahoceIdInput"
				value="<%=warehouseCellId%>" />
			<input class="s1" type="hidden" name="custTel" id="custTelInput">
			<input class="s1" type="hidden" name="custMemo" id="custMemoInput">
			<input class="s1" type="hidden" name="startDate" id="startTimeInput" />
			<input class="s1" type="hidden" name="endDate" id="endTimeInput" />
			<input class="s1" type="hidden" name="totalAmount" id="amountInput" />
			<input class="s1" type="hidden" name="custName" id="custNameInput" />
			<input class="s1" type="hidden" name="warehouseId"
				id="warehouseIdInput" value="<%=warehouseId%>" />
			<input class="s1" type="hidden" name="type" id="typeInput" />
			<input class="s1" type="hidden" name="size" id="sizeInput" />
			<input class="s1" type="hidden" name="openId" id="openIdInput" value="<%=openId%>" />
				
				
				<input class="s1" type="hidden" name="totalDay" id="totalDayInput" />
		</form>
		<script type="text/javascript" src="/web/whc/common/js/zepto.js"></script>
		<script type="text/javascript" src="/web/whc/common/js/webcom.webserviceclient.js"></script>
		<script type="text/javascript" src="/web/whc/common/js/swipeSlide.min.js"></script>
		<script type="text/javascript">
		var globalVar = {
			baseUrl : '<%=baseUrl%>',
			curCell : null,
			rentMethod : "日租"
		}
		function submit() {
			var _curCell = $(globalVar.curCell);
			$('#typeInput').val(_curCell.attr("data-typeId"));
			$('#sizeInput').val(_curCell.attr("data-size"));
			$('#custNameInput').val($('#custName').val());
			$('#custTelInput').val($('#custTel').val());
			$('#custMemoInput').val($('#custMemo').text());
			var _start1Date = new Date($('#start1Date').val());
			var _end1Date = new Date($('#end1Date').val());
			$('#startTimeInput').val(_start1Date.Format("yyyy-MM-dd"));
			$('#endTimeInput').val(_end1Date.Format("yyyy-MM-dd"));
			
			var _dataMessageSender = new webcom.DataMessageSender();
		
			_dataMessageSender.setWebServiceName("AddOrderByWeixin");
			
			
	        $(".s1").each(function() {
				 var attrValue = this.getAttribute("value") || "";
				 if (attrValue != "") {
					 _dataMessageSender.addParameter(this.getAttribute("name"), this.getAttribute("value"));
                 }
            });
	        _dataMessageSender.sendJSON(function (_responseDataMessage) {
				var _json = eval("("+ _responseDataMessage +")");
				if (_json.messageType=="error") {
				
				
				} else {
					var _url =  globalVar.baseUrl + '/web/whc/member/center.html?openId=<%=openId%>&flag=1'
										+ '&warehouseCellType=' + escape(escape(_curCell.attr("data-typeName"))) 
										+ '&warehouseName=' + escape(escape('<%=json.getString("name")%>'))
										+ '&warehouseCellsize=' + _curCell.attr("data-size")
										+ '&days=' + escape(escape($('#days').text()));
					window.location.href = _url;
				}
			});
	        
			$(".xiadanpop").hide();
		}
		function selectWarehouseCell(_obj) {
			$('.td').each(function () {
				$(this).removeClass('cur');
			});
			$(_obj).addClass('cur');
			globalVar.curCell = _obj;
			var _startDate = new Date($('#startDate').val());
			var _endDate = new Date($('#endDate').val());
			countDay();
		}
		
		
		function popMenu() {
			var _startDate = new Date($('#start1Date').val());
			var _endDate = new Date($('#end1Date').val());
			var _amount =$('#amountInput').val();
			var startDate = $('#start1Date').val();
			var endDate = $('#end1Date').val();
			var custName = $('#custName').val();
			var custTel = $('#custTel').val();
			if(!globalVar.curCell) {
				$('.failed .p2').html('请选择点击<em> 仓位类型和体积</em>！');
				$(".page-mask").show();
				$(".failed").show();
				return;
			} 
			if(!startDate) {
				$('.failed .p2 em').text(' 入仓时间');
				$(".page-mask").show();
				$(".failed").show();
				return;
			}
			if(!endDate) {
				$('.failed .p2 em').text(' 出仓时间');
				$(".page-mask").show();
				$(".failed").show();
				return;
			}
			if(!custName) {
				$('.failed .p2 em').text(' 客户姓名');
				$(".page-mask").show();
				$(".failed").show();
				return;
			}
			if(!custTel) {
				$('.failed .p2 em').text(' 客户手机');
				$(".page-mask").show();
				$(".failed").show();
				return;
			}
			
			var heji=$('#hejiTd').text();
			if(heji==""||heji==null||heji=='合计： ￥0元'){
				 $('.failed .p2').html('出仓日期需大于入仓日期!');
					$(".page-mask").show();
					$(".failed").show();
				return;
				
			}
			   var ab=/^(0|86|17951)?(13[0-9]|15[012356789]|17[678]|18[0-9]|14[57])[0-9]{8}$/;
			     if(!ab.test(custTel)){
			    $('.failed .p2 em').text(' 有效的手机号码');
				$(".page-mask").show();
				$(".failed").show();
			     return ;
			    }
			$(".page-mask").show();
			$(".xiadanpop").show();
		}
		function getOffDays(startDate, endDate) {    
	    	var mmSec = (endDate - startDate); //得到时间戳相减 得到以毫秒为单位的差    
	      	return parseInt(mmSec / 3600000 / 24); //单位转换为天并返回    
	    }; 
	    Date.prototype.Format = function (fmt) { //author: meizz 
		    var o = {
		        "M+": this.getMonth() + 1, //月份 
		        "d+": this.getDate(), //日 
		        "h+": this.getHours(), //小时 
		        "m+": this.getMinutes(), //分 
		        "s+": this.getSeconds(), //秒 
		        "q+": Math.floor((this.getMonth() + 3) / 3), //季度 
		        "S": this.getMilliseconds() //毫秒 
		    };
		    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
		    for (var k in o)
		    if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
		    return fmt;
		}	
		$(function(){
			var pageMask = $(".page-mask").css("height",$("body").height());
			var xiadan = $(".xiadanpop");
			$.each([pageMask,xiadan],function(v1,v2){
				$(v2).on("touchmove", function () {
					return !1;
				})
			});
			$('#slide3').swipeSlide({
		        continuousScroll:true,
		        speed : 3000,
		        transitionType : 'cubic-bezier(0.22, 0.69, 0.72, 0.88)',
		        callback : function(i){
		            $('.dot').children().eq(i).addClass('cur').siblings().removeClass('cur');
		        }
		    });
			$('.td').each(function () {
				$(this).removeClass('cur');
			});
			$(".sureBtn").on("touchstart",function(){
				pageMask.hide();
				$(".failed").hide();
			});
		});
	


		$(document).ready(function () {
			$("#_userDateSelectIn").on("touchstart",function (){	

				$.userSelectDate({eId:"#_userDateSelectIn",lock:true,});
	            return false;
			});	

			$("#_userDateSelectOut").on("touchstart",function (){	

				$.userSelectDate({eId:"#_userDateSelectOut",type:"end"});
				console.log($.userGetDate());
				return false;
			});	
		});
	</script>
	</body>
</html>
