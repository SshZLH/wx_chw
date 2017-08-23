var wahoName=null;
var wahoId=null;
var priceLists=null;
var wahoceSize=null;
//价格
var price=null;
//续租时长
var hourRenews=null;
var dayRenews=null;
var monthRenews=null;
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
var adv1=null;
var adv2=null;
var adv3=null;
var unit = null;
var url = location.search; //获取url中"?"符后的字串
var appId=null;
var orderEndDate=null;
var aboveRentTime=null;
if (url.indexOf("?") != -1) { 
      var str = url.substr(1);
      strs = str.split("&");
      var strOrderId = strs[0];
      var strOrderIdlist=strOrderId.split("=");
      orderId=strOrderIdlist[1];
      var strAboveRentTime = strs[1];
      var strAboveRentTimelist=strAboveRentTime.split("=");
      aboveRentTime=strAboveRentTimelist[1];
}
function defaultMob(demo){
	  $(demo).mobiscroll().select({
        theme: 'mobiscroll',
        lang: 'zh',
        display: 'inline',
        minWidth: 400,
      });
	}
$(function(){
		//返回上一页面
		$(".titLeft").on('click',function(){
			window.history.go(-1);
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
		var wahoName = null;
		var waceNumber = null;
		var custName = null;
		var custTel = null;
		$.ajax({
			url:"/addContinuCabView",
			type:'GET',
			data:{
				orderId:orderId
			},
			success:function(msg){
				if(msg.result=="true"){
					var json=msg.info;
					var warehouseMap = msg.info.warehouse;
					//alert(warehouseMap.adv1);
					wahoName = json.wahoName;
					waceNumber = json.waceNumber;
					custName = json.custName;
					custTel = json.custTel;
					unit = json.unit;
					wahoceSize=json.waceSize;
					hourRenews=json.hourRenews;
					dayRenews=json.dayRenews;
					monthRenews=json.monthRenews;
					appId = json.appId; 
					priceLists=msg.info.priceList.data.immediateWahocePrices;
					adv1=warehouseMap.adv1;
					adv2=warehouseMap.adv2;
					adv3=warehouseMap.adv3;
					$('.price_des1').html(adv1);
					$('.price_des2').html(adv2);
					$('.price_des3').html(adv3);
					//alert(adv1);
					orderEndDate = json.orderEndDate;
					$(".address").html(wahoName);
					$(".orderNumber").html(waceNumber);
					$(".nameInput").val(custName);
					$(".phoneInput").val(custTel);
					
					if(unit=="1"){
						rentTime=hourRenews;
						$("#rentTime span").html(hourRenews+"小时");
					}else if(unit=="2"){
						rentTime=dayRenews;
						$("#rentTime span").html(dayRenews+"天");
					}else if(unit=="3"){
						rentTime=monthRenews;
						$("#rentTime span").html(monthRenews+"月");
					}
					//调用计算钱的方法
					time_OnChange(unit,rentTime);
				}
				
			},error:function(){
				//alert("获取订单信息失败！");
				createStyle("body","获取订单信息失败","2.6rem");
			}
		});

	//续租去支付
		//支付
		
		$(".payButton").bind('click',function(){
			$(this).unbind('click');
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
			var unit = document.getElementById("unitInput").value;
			var rentalTime = document.getElementById("rentalTimeInput").value;
			var numberInput = document.getElementById("numberInput").value;
			var wahoceIdInput = document.getElementById("wahoceIdInput").value;
			var memberTel = $(".phoneInput").val();
			var memberName = $(".nameInput").val();
			orderEndDate="";
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
					endDate:orderEndDate
				},
				success:function(msg){
					var json=msg.info;
						if (orderId== undefined)
						{
							alert("订单失败！");
							return;
						}
						var flag="1";
						if(totals=="0.00" || totals=="0.0" || totals=="0" ){
							//var weixinUrl="/updateOrderStatus?orderId="+orderId +"&ver=2&time="+(new Date());
							//window.location.href=weixinUrl;
							updateOrderStatus(orderId);
						}else{
							var parms=orderId+"-"+flag;

						    var url = globalVar.baseUrl+"web/whc/warehouse/pay2.jsp";
						    var weixinUrl="https://open.weixin.qq.com/connect/oauth2/authorize?appid="+appId+"&redirect_uri="+url+"?parms="+parms+"&flag="+flag+"&response_type=code&scope=snsapi_base&state=123#wechat_redirect";
						    window.location.href=weixinUrl;
						}
				},error:function(){
					//alert("订单失败！");
					createStyle("body","订单失败","2.4rem");
				}
			});
		});

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
	//起租的进行转
	$("#rentTime").on('click',function(){
			var xzhour=Number(hourRenews);
			var xzday=Number(dayRenews);
			var xzmonth=Number(monthRenews);
			if(unit=="1"){
				var nextNumber=xzhour;
				for(var i=0;i<=$("#demo1 option").length;i++){
					for(var n=i;n<=("#demo1 option").length;n++){
						var thisNumber=parseInt($("#demo1 option").eq(i).val());
						if(thisNumber!=nextNumber){
						$("#demo1 option").eq(i).remove();
						}
					}
					nextNumber=nextNumber+xzhour;
				}
			}
			if(unit=="2"){
			var nextNumber=xzday;
				for(var i=0;i<=$("#demo2 option").length;i++){
					for(var n=i;n<=("#demo2 option").length;n++){
						var thisNumber=parseInt($("#demo2 option").eq(i).val());
						if(thisNumber!=nextNumber){
							$("#demo2 option").eq(i).remove();
						}
					}
					nextNumber=nextNumber+xzday;
				}
			}
			if(unit=="3"){
				var nextNumber=xzmonth;
				for(var i=0;i<=$("#demo3 option").length;i++){
					for(var n=i;n<=("#demo3 option").length;n++){
						var thisNumber=parseInt($("#demo3 option").eq(i).val());
						if(thisNumber!=nextNumber){
						$("#demo3 option").eq(i).remove();
						}
					}
					nextNumber=nextNumber+xzmonth;
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
			} else if(unit=="3"){
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
					$('#li2').removeClass('on')
					$('#li3').removeClass('on');
	    	 		$('#li1').addClass('on');
				}
				if($(".slideTime div").eq(i).html()=="按天选择"){
					rentTime=$(".boxSid select").eq(1).val();
					$('#li1').removeClass('on')
					$('#li3').removeClass('on');
	    	 		$('#li2').addClass('on');
				}
				if($(".slideTime div").eq(i).html()=="按月选择"){
					rentTime=$(".boxSid select").eq(2).val();
					$('#li1').removeClass('on')
					$('#li2').removeClass('on');
	    	 		$('#li3').addClass('on');
				}							
					
				var unitName = $(".slideTime div").eq(i).attr("id");
				if(unitName=="li1"){
					units="1";
					$("#rentTime span").html(rentTime+"小时");
				}
				if(unitName=="li2"){
					units="2";
					$("#rentTime span").html(rentTime+"天");
				}
				if(unitName=="li3"){
					units="3";
					$("#rentTime span").html(rentTime+"月");
				}
			}
		})
		time_OnChange(units,rentTime);
 		$(".transparent").hide();
		$(".slideChange").hide();   				
	})     		
	$(".stampInput").focus(function(){
		$(this).val('');
	});
	$(".stampInput").focus(function(){
		$(this).val('');
	});
})

//计算即时柜价格
function time_OnChange(unit,returnTime){
	//首租时长
	var overTime=Number(aboveRentTime);
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