var unit=null;
var minorderEndDate=null;
var priceLists=null;
var appId = null; 
var orderId =null;
var timeNumber=null;
//结束时间
var endDate=null;
//最小续费结束时间
var minEndDates=null;
//最下续费时长
var minEndTime=null;

//续租时长
var hourRenews=null;
var dayRenews=null;
var monthRenews=null;
var wahoName=null;
var wahoId=null;

var wahoceSize=null;
//价格
var price=null;

//起租时长
var hourStarts=null;
var dayStarts=null;
var monthStarts=null;
//可用优惠券
var listvouchers=null;
//会员id
var memberIds=null;
//优惠券id
var voucherIds=null;
//优惠券Code
var voucherCodes=null;
var wahoceId=null;
var openId=null;
//选定时长/默认时长
var rentTime=null;
//选定单位
var units=null;
var jsonVoucher=null;
//门店类型
var wahoType=null;
var orderEndDate=null;
var adv1=null;
var adv2=null;
var adv3=null;
var url = location.search; //获取url中"?"符后的字串
if (url.indexOf("?") != -1) { 
    var str = url.substr(1);
    strs = str.split("&");
    var strName = strs[0];
    var strNamelist=strName.split("=");
	orderId=strNamelist[1];

}
	   	
function defaultMob(demo){
	  $(demo).mobiscroll().select({
        theme: 'mobiscroll',
        lang: 'zh',
        display: 'inline',
        minWidth: 400,
      });
	}
 //展示柜体信息   		
 $.ajax({
		url:"/queryOrder",
		type:'GET',
		data:{
			orderId:orderId,
			status:status
		},
		success:function(msg){
			//alert(msg.info.appId);
			if(msg.result=="true"){
				var  listorderRenew =msg.info.order;
				//alert(listorderRenew.tel);
				var tal=listorderRenew.tel;
				wahoceSize=listorderRenew.waceSize;
				$(".address").html(listorderRenew.wahoName);
				$(".orderNumber").html(listorderRenew.waceNumber+"号柜");
				$(".nameInput").val(listorderRenew.name);
				$(".phoneInput").val(listorderRenew.tel);
				var orderStartDate = listorderRenew.orderStartDate;
				var orderEndDate = listorderRenew.orderEndDate;
				rentTime=msg.info.minorderEndDate;
				minorderEndDate=msg.info.minorderEndDate;
				//alert("minorderEndDate:"+minorderEndDate);
				//最小结束时间
				minEndDates=msg.info.minEndDates;
				endDate=minEndDates;
				//最小结束时长
				minEndTime=msg.info.minorderEndDate;
				timeNumber=Number(listorderRenew.number);
				
				unit=listorderRenew.unit;
				appId=msg.info.appId;
				//alert("appId:"+appId);
				var warehouseMap=msg.info.Warehouse;
				hourRenews=warehouseMap.hourRenew;
				dayRenews=warehouseMap.dayRenew;
				monthRenews=warehouseMap.monthRenew;
				priceLists=msg.info.priceList.immediateWahocePrices;
				//alert("priceLists:"+priceLists[0].id);
				adv1=warehouseMap.adv1;
				adv2=warehouseMap.adv2;
				adv3=warehouseMap.adv3;
				$('.price_des1').html(adv1);
				$('.price_des2').html(adv2);
				$('.price_des3').html(adv3);
				//alert(adv1);
				if(unit=="1"){
					$("#rentTime span").html(msg.info.minorderEndDate+"小时");
					$(".csxx").html("您已超时"+msg.info.orderEndDate+",最低续费时长"+msg.info.minorderEndDate+"小时");
				}else if(unit=="2"){
					$("#rentTime span").html(msg.info.minorderEndDate+"天");
					$(".csxx").html("您已超时"+msg.info.orderEndDate+",最低续费时长"+msg.info.minorderEndDate+"天");
				}else if(unit=="3"){
					$("#rentTime span").html(msg.info.minorderEndDate+"月");
					$(".csxx").html("您已超时"+msg.info.orderEndDate+",最低续费时长"+msg.info.minorderEndDate+"个月");
				}
				time_OnChange(unit,msg.info.minorderEndDate);
			}
		},
		error:function(){
			//alert("失败");
			createStyle("body","失败","2.2rem");
		}
	 });
$(function(){
	//返回上一页面
	$(".titLeft").on('click',function(){
		window.history.go(-1);
	})	
	
	//将后台返回的string类型的转换为数值类型
	//起租的进行转换
	
	
	$("#coupons .titLeft").on('click',function(){
		$("#fillTxt").show();
		$("#coupons").hide();
	});
	var a=1;
	$("#rentTime").on('click',function(){
		if(unit=="1"){
			if(Number(minorderEndDate)+Number(hourRenews)>=24){
				//alert("您所超期的值以大于24小时！已不可选时！");
				createStyle("body","您所超期的值以大于24小时,已不可选时","3rem");
				return;
			}
		}else if(unit=="2"){
			if(Number(minorderEndDate)+Number(dayRenews)>=30){
				//alert("您所超期的值以大于30天！已不可选时！");
				createStyle("body","您所超期的值以大于30天,已不可选时","3rem");
				return;
			}
		}else if(unit=="3"){
			if(Number(minorderEndDate)+Number(monthRenews)>=12){
				//alert("您所超期的值以大于12个月！已不可选时！");
				createStyle("body","您所超期的值以大于12个月,已不可选时","3rem");
				return;
			}
		}
			//续租的进行类型的转化
			var xzhour=Number(hourRenews);
			var xzday=Number(dayRenews);
			var xzmonth=Number(monthRenews);
			if(a==1){
				a=a+1;
			if(unit=="1"){
				var nextNumber=minorderEndDate;
				//alert("nextNumber:"+nextNumber);
				var hourStr ="";
				for(var i=0; i<24;i++){
					if ( i == nextNumber ){				
						hourStr+="<option value='"+i+"'>"+i+"小时</option>" ;
						nextNumber = nextNumber + xzhour;
					}	
				}
				$("#demo1").empty();
				$("#demo1").append(hourStr);
			}
			if(unit=="2"){
				var nextNumber=minorderEndDate;
				var dayStr ="";
				for(var i=0; i<24;i++){
					if ( i == nextNumber ){				
						dayStr+="<option value='"+i+"'>"+i+"天</option>" ;
						nextNumber = nextNumber + xzday;
					}	
				}
				$("#demo2").empty();
				$("#demo2").append(dayStr);
			}
			if(unit=="3"){
				var nextNumber=Number(minorderEndDate);
				var monthStr ="";
				for(var i=0; i<24;i++){
					if ( i == nextNumber ){				
						monthStr+="<option value='"+i+"'>"+i+"个月</option>" ;
						nextNumber = nextNumber + xzmonth;
					}	
				}
				$("#demo2").empty();
				$("#demo2").append(monthStr);
			}	
			}
			$(".slideTime div").eq(0).show();
			if(unit=="1"){
				$("#slideTime").html("<div id='li1' class='hour on' style='width:6.44rem;float:left;border-right:0;border-top-right-radius:.2rem;border-top-left-radius:.2rem;'>按时选择</div>");
				defaultMob("#demo1");
	    	 	$(".price_des1").show();
	    	 	$(".price_des2").hide();
	    	 	$(".price_des3").hide();
			}else if(unit=="2"){
				$("#slideTime").html("<div id='li2' class='day on' style='width:6.44rem;float:left;border-right:0;border-top-right-radius:.2rem;border-top-left-radius:.2rem;'>按天选择</div>");
				defaultMob("#demo2");
	    	 	$(".price_des2").show();
	    	 	$(".price_des1").hide();
	    	 	$(".price_des3").hide();
			}else if(unit=="3"){
				$("#slideTime").html("<div id='li3' class='month on' style='width:6.44rem;float:left;border-right:0;border-top-right-radius:.2rem;border-top-left-radius:.2rem;'>按月选择</div>");
				defaultMob("#demo3");
	    	 	$(".price_des3").show();
	    	 	$(".price_des1").hide();
	    	 	$(".price_des2").hide();
			}
			//根据参数判断option的值
			$(".transparent").show();
			$(".slideChange").show();
            $('#li1').on('click',function(){
	    	 	$('#li2').removeClass('on');
	    	 	$('#li3').removeClass('on');
	    	 	$('#li1').addClass('on');
	    	 	$('#box2').hide();
	    	 	$('#box3').hide();
	    	 	$('#box1').show();
	    	 	$(".price_des1").show();
	    	 	$(".price_des2").hide();
	    	 	$(".price_des3").hide();
	    	 	$('#demo1').mobiscroll().select({
		        theme: 'mobiscroll',
		        lang: 'zh',
		        display: 'inline',
		        minWidth: 400,
	        	});
	    	 });
	    	 $('#li2').on('click',function(){
	    	 	$('#li1').removeClass('on');
	    	 	$('#li3').removeClass('on');
	    	 	$('#li2').addClass('on');
	    	 	$('#box1').hide();
	    	 	$('#box3').hide();
	    	 	$('#box2').show();
	    	 	$(".price_des2").show();
	    	 	$(".price_des1").hide();
	    	 	$(".price_des3").hide();
	    	 	$('#demo2').mobiscroll().select({
		        theme: 'mobiscroll',
		        lang: 'zh',
		        display: 'inline',
		        minWidth: 400,
	        	});
	 		});
	    	$('#li3').on('click',function(){
	    	 	$('#li1').removeClass('on');
	    	 	$('#li2').removeClass('on');
	    	 	$('#li3').addClass('on');
	    	 	$('#box1').hide();
	    	 	$('#box2').hide();
	    	 	$('#box3').show();
	    	 	$(".price_des3").show();
	    	 	$(".price_des1").hide();
	    	 	$(".price_des2").hide();
	    	 	$('#demo3').mobiscroll().select({
		        theme: 'mobiscroll',
		        lang: 'zh',
		        display: 'inline',
		        minWidth: 400,
	        	});
	    	});    			   			
		})
		$(".line_bottom .left").on('click',function(){
		$(".transparent").hide();
		$(".slideChange").hide();   				
		})
		$(".line_bottom .right").on('click',function(){
			$(".slideTime div").each(function(i){
				if($(".slideTime div").eq(i).hasClass("on")){
					if($(".slideTime div").eq(i).html()=="按时选择"){
						rentTime=$(".boxSid select").eq(0).val();
						$('#li2').removeClass('on');
						$('#li3').removeClass('on');
	    	 			$('#li1').addClass('on');
					}
					if($(".slideTime div").eq(i).html()=="按天选择"){
						rentTime=$(".boxSid select").eq(1).val();
						$('#li1').removeClass('on');
						$('#li3').removeClass('on');
	    	 			$('#li2').addClass('on');
					}
					if($(".slideTime div").eq(i).html()=="按月选择"){
						rentTime=$(".boxSid select").eq(2).val();
						$('#li1').removeClass('on');
						$('#li2').removeClass('on');
	    	 			$('#li3').addClass('on');
					}							
					var unitName = $(".slideTime div").eq(i).attr("id");
					if(unitName=="li1"){
						units="1";
						$("#rentTime span").html(Number(rentTime)+"小时");
					}
					if(unitName=="li2"){
						units="2";
						$("#rentTime span").html(Number(rentTime)+"天");
					}
					if(unitName=="li3"){
						units="3";
						$("#rentTime span").html(Number(rentTime)+"月");
					}
					
				}
				
			})
			$.ajax({
				 url:"/coverSelectTime",
					type:'GET',
					data:{
						unit:units,
						selectTime:rentTime,
						minEndTime:minEndTime,
						minEndDates:minEndDates,
					},
					success:function(msg){
						if(msg.result=="true"){
							endDate=msg.info.selectEndDate;
						}else{
							//alert("选时失败，请重新选择，谢谢！");
							createStyle("body","选时失败,请重新选择,谢谢","3rem");
							
						}
					},error:function(){
						//alert("选时失败，请重新选择，谢谢！");
						createStyle("body","选时失败,请重新选择,谢谢","3rem");
					}
			 });
 			$(".transparent").hide();
			$(".slideChange").hide(); 
			time_OnChange(units,rentTime);
	})     		
	/*用户协议*/
	$(".agreement").on('click',function(){
		$(".transparent").show();
		$(".userSay").show();
		$(".content").html(getAgreement());
		$(".bottom").on('click',function(){
			$(".transparent").hide();
			$(".userSay").hide();
		})
	});
	$(".stampInput").focus(function(){
		$(this).val('');
	});    	
	$(".agreement").on('click',function(){
		$(".transparent").show();
		$(".userSay").show();
		$(".bottom").on('click',function(){
			$(".transparent").hide();
			$(".userSay").hide();
		})
	});
	$(".stampInput").focus(function(){
		$(this).val('');
	});
	
	//支付
	$(".payButton").bind('click',function(){
		
		//$(this).css({"background":"#898989"})
		var name = $(".nameInput").val();
	  	if(name == null || name == "" || name=="请输入姓名"){
	  		alert("请输入姓名！");
	  		return false;
	  	}
	  	var phone = $(".phoneInput").val();
	  	if(phone == null || phone == "" || phone == "请输入手机号"){
	  		alert("请输入手机号码！");
	  		return false;
	  	}else{
		    if(!(/^1[34578]\d{9}$/.test(phone))){ 
		        alert("手机号码有误，请重填！");  
		        return false; 
		    }
	  	}
	  	var chk= document.getElementById('ckbox');
	  	if(chk.checked==false){
	  		//alert("请勾选用户使用协议！");
	  		createStyle("body","请勾选用户使用协议","2.8rem");
	  		return false;
	  	}
		var totals = document.getElementById("totalInput").value;
		var coverWarehouseAmount=totals;
		var unit = document.getElementById("unitInput").value;
		var rentalTime = document.getElementById("rentalTimeInput").value;
		var numberInput = document.getElementById("numberInput").value;
		var wahoceIdInput = document.getElementById("wahoceIdInput").value;
		var memberTel = $(".phoneInput").val();
		var memberName = $(".nameInput").val();
		document.getElementById("openIdInput").value = openId;
		var openIdInput = document.getElementById("openIdInput").value; 
		$.ajax({
			url:"/continueWarehouse",
			type:'GET',
			data:{
				unit:unit,
				number:numberInput,
				total:totals,
				orderId:orderId,
				openId:openIdInput,
				rentalTime:rentalTime,
				endDate:endDate
			},
			success:function(msg){
					
					//alert("订单成功！"+orderId);
					var flag="1";
					//alert("totals"+totals);
					if(totals=="0.00" || totals=="0.0" || totals=="0" ){
						alert("totals"+totals);
						//var weixinUrl="/updateOrderStatus?orderId="+orderId+"&ver="+2+"&time="+(new Date());
						updateOrderStatus(orderId);
					}else{
						var parms=orderId+"-"+flag;
					    var url =globalVar.baseUrl+ "web/whc/warehouse/pay2.jsp";
					    var weixinUrl="https://open.weixin.qq.com/connect/oauth2/authorize?appid="+appId+"&redirect_uri="+url+"?parms="+parms+"&flag="+flag+"&response_type=code&scope=snsapi_base&state=123#wechat_redirect";
					    window.location.href=weixinUrl;
					}
			},error:function(){
				//alert("订单失败！");
				createStyle("body","订单失败","2.4rem");
			}
		});
		$(this).unbind('click');
	});
})

//续租价格为0元是
function updateOrderStatus(orderId){
	$.ajax({
		url:"/updateOrderStatus",
		type:'GET',
		data:{
			orderId:orderId
		},
		success:function(msg){
			window.location.href="/web/whc/member/doDetail.html?orderId=" +orderId;
		},
		error:function(){
			alert("修改订单失败，请重试！");
		}
	})


}
//计算即时柜价格
function time_OnChange(unit,returnTime){
	var overTime=timeNumber;
	//alert("timeLength:"+overTime);
	//alert("werunit"+unit+"returnTime:"+returnTime);
	var rentTimes=Number(returnTime);
	//判断价格表不为null
	var total=0.0;
	if(priceLists != null){
		for(var i=0;i<priceLists.length;i++){
			var price=0.0;
		    var _total=0.0;
		     var beginTime=Number(priceLists[i].beginTime);
		     var endTime=Number(priceLists[i].endTime);
		    
		    if(unit==priceLists[i].unit){
		    	//去价格
		    	if(wahoceSize=='1.0'||wahoceSize=='1'){
                	 price=Number(priceLists[i].size1Price);
                 }else if(wahoceSize=='2.0'||wahoceSize=='2'){
                     price=Number(priceLists[i].size2Price);
               	 }else if(wahoceSize=='3.0'||wahoceSize=='3'){
                     price=Number(priceLists[i].size3Price);
                 }else if(wahoceSize=='4.0'||wahoceSize=='4'){
                     price=Number(priceLists[i].size4Price);
                 }
                 //计算金额
		    	if(endTime != null && endTime != ""){
		    		if(beginTime<=overTime && overTime < endTime){
              			if((overTime+rentTimes)<=endTime){
              				_total=price*(rentTimes).toFixed(2);
              			}else if((overTime+rentTimes)>endTime){
              				_total=price*(endTime-overTime).toFixed(2);
              			}
              		}else if(overTime <=  beginTime && overTime < endTime ){
              			if((overTime+rentTimes) >= beginTime && (overTime+rentTimes) < endTime){
              				_total=price*((overTime+rentTimes)-beginTime);
              			}else if((overTime+rentTimes) >= endTime){
              				_total=price*(endTime-beginTime);
              			}
              		}
		    	}else{
		    		if(overTime >= beginTime){
		    			_total=price*(rentTimes);
              		}
              		if(overTime < beginTime){
              			_total=price*((overTime+rentTimes)-beginTime);
              		}
		    	}
                 
		    }
		      total=total+_total;
		}
	}
	
	if(unit=='3'){
		if(returnTime!=0){
			var MonthToDays = 0;
			var ThisMonth  = new Date();
			var ThisDay=ThisMonth.getDate();
			// 1、跑到下一个月
			ThisMonth.setMonth(ThisMonth.getMonth()+1);
			// 2、设置日期为0号 - 自动进位到上个月的最后一天
			ThisMonth.setDate(0);
			// 3、获取到日期
			var ThisMaxDay=ThisMonth.getDate();
			MonthToDays = MonthToDays+ThisMaxDay-ThisDay;
			for(var i = 1;i<rentTimes;i++){
				var NextMonth = new Date();
				NextMonth.setMonth(NextMonth.getMonth()+i+1);
				NextMonth.setDate(0);
				var NextMaxDay=NextMonth.getDate();
				MonthToDays = MonthToDays+NextMaxDay;
			}
			var LastMonth = new Date();
			LastMonth.setMonth(LastMonth.getMonth()+returnTime+1);
			LastMonth.setDate(0);
			var LastMaxDay=LastMonth.getDate();
			if (ThisDay <= LastMaxDay){
				//计算结束时间
				document.getElementById("rentalTimeInput").value = (MonthToDays+ThisDay)*24;
			}else if(ThisDay>LastMaxDay){
				//计算结束时间
				document.getElementById("rentalTimeInput").value = (MonthToDays+LastMaxDay)*24;
			}
		}else{
			//计算结束时间
			document.getElementById("rentalTimeInput").value = rentTime;
		}
	}
	if(unit=='2'){
		//计算结束时间
		document.getElementById("rentalTimeInput").value = rentTime*24;
	}else if(unit=='1'){
		//计算结束时间
		document.getElementById("rentalTimeInput").value = rentTime;
	}
	
	//alert("total:"+total.toFixed(2));
	$("#price").html(total.toFixed(2)+"元");
	if("0.00"==total.toFixed(2)){
		$(".payButton").html("提交");
	}else{
		$(".payButton").html("去支付");
	}	
	document.getElementById("numberInput").value = rentTime;
	document.getElementById("unitInput").value = unit;
	document.getElementById("totalInput").value = total.toFixed(2);
}