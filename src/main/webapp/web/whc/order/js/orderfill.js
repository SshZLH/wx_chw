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
var josn;
var openId;
var wahoceId;
var method="POST";
//选定时长/默认时长
var rentTime=null;
//选定单位
var units=null;
var jsonVoucher=null;
//门店类型
var wahoType=null;
var voucherId=null;
var url = location.search; //获取url中"?"符后的字串
var selectedVoucherId=null;
var selectedVoucherAmount=null;
var selectedVoucherUNIT=null;
var defaultVoucherId = null;
var voucherList=null;	//可用优惠券列表
var adv1=null;
var adv2=null;
var adv3=null;
if (url.indexOf("?") != -1) {
	var str = url.substr(1);
	strs = str.split("&");
	var strName = strs[0];
	var strNamelist=strName.split("=");
	wahoceId=strNamelist[1];
	var strid = strs[1];
	var strIdlist=strid.split("=");
	openId=strIdlist[1];
	var strid2 = strs[2];
	var strIdlist2=strid2.split("=");
	wahoId=strIdlist2[1];
}
console.log("openId"+openId);
console.log("wahoId"+wahoId);
console.log("wahoceId"+wahoceId);

function defValue(obj,txt){
	if($(obj).val()==''){
		$(obj).val(txt);
	}
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

	//获取门店信息
	$.ajax({
		type:"post",
		url:"/getWahoceCallInfo",
		dataType:"JSON",
		async:false,
		data:{
			"wahoceId":wahoceId,
			"openId":openId,
			"wahoId":wahoId
		},
		success:function(msg){
			//alert(msg);
			//alert(msg.code);
			//alert(msg);
			if(msg.code=="10000"){
				josn=msg.data;
				//alert(josn.wahoceInfo[0].wahoName);
				$(".address").html(josn.wahoceInfo[0].wahoName);
				wahoceSize=josn.wahoceInfo[0].wahoceSize;
				hourRenews=josn.wahoceInfo[0].hourRenew;
				dayRenews=josn.wahoceInfo[0].dayRenew;
				hourRenews=josn.wahoceInfo[0].hourRenew;
				dayRenews=josn.wahoceInfo[0].dayRenew;
				monthRenews=josn.wahoceInfo[0].monthRenew;
				hourStarts=josn.wahoceInfo[0].hourStart;
				dayStarts=josn.wahoceInfo[0].dayStart;
				monthStarts=josn.wahoceInfo[0].monthStart;
				$(".orderNumber").html(josn.wahoceInfo[0].wahoceNumber+"号柜");
				document.getElementById("wahoceIdInput").value=josn.wahoceInfo[0].wahoceId;
				wahoId=josn.wahoceInfo[0].wahoId;
				wahoType=josn.wahoceInfo[0].wahoType;
				adv1=josn.wahoceInfo[0].adv1;
				adv2=josn.wahoceInfo[0].adv2;
				adv3=josn.wahoceInfo[0].adv3;
				priceLists=josn.immediateWahocePrices;
				$('.price_des1').html(adv1);
				$('.price_des2').html(adv2);
				$('.price_des3').html(adv3);
				listvouchers = josn.listMemberVoucher;
				//alert(typeof listvouchers);
				//listvouchers
				if(hourRenews !="0" && dayRenews !="0" && monthRenews !="0"){
					units="1";
					rentTime=Number(hourStarts);
					nearbyNumber(units,listvouchers,rentTime);
					voucherId=voucherIds;
				} if(hourRenews !="0" && dayRenews =="0" && monthRenews !="0"){
					units="1";
					rentTime=Number(hourStarts);
					nearbyNumber(units,listvouchers,rentTime);
					voucherId=voucherIds;
				} if(hourRenews !="0" && dayRenews !="0" && monthRenews =="0"){
					units="1";
					rentTime=Number(hourStarts);
					nearbyNumber(units,listvouchers,rentTime);
					voucherId=voucherIds;
				} if(hourRenews !="0" && dayRenews =="0" && monthRenews =="0"){
					units="1";
					rentTime=Number(hourStarts);
					nearbyNumber(units,listvouchers,rentTime);
					voucherId=voucherIds;
				} if(hourRenews =="0" && dayRenews !="0" && monthRenews !="0"){
					units="2";
					rentTime=Number(dayStarts);
					nearbyNumber(units,listvouchers,rentTime);
					voucherId=voucherIds;
				} if(hourRenews =="0" && dayRenews !="0" && monthRenews =="0"){
					units="2";
					rentTime=Number(dayStarts);
					nearbyNumber(units,listvouchers,rentTime);
					voucherId=voucherIds;
				} if(hourRenews =="0" && dayRenews =="0" && monthRenews !="0"){
					units="3";
					rentTime=Number(monthStarts);
					nearbyNumber(units,listvouchers,rentTime);
					voucherId=voucherIds;
				}

			}

		},
		error:function(){
			alert("获取订单信息失败");
		}
	});
	//返回上一级
	$(".titLeft").on('click',function(){
		//window.history.back(-1);
		window.location.href=document.referrer;
	});
	//优惠券返回上一级
	$(".titLeft2").on('click',function(){
		//alert(1);
		$("#coupons").hide();
		$("#fillTxt").show();
		$("#couponsInfo").hide();
		if(selectedVoucherId!=null || selectedVoucherId!=""){
			if(selectedVoucherUNIT==1){
				if(selectedVoucherAmount!=0){
					$("#lengthTime span").html(selectedVoucherAmount+"小时");
				}else{
					$("#lengthTime span").html("暂无");
				}
			}
			if(selectedVoucherUNIT=="2"){
				if(selectedVoucherAmount!=0){
					$("#lengthTime span").html(selectedVoucherAmount+"天");
				}else{
					$("#lengthTime span").html("暂无");
				}
			}
			if(selectedVoucherUNIT=="3"){
				if(selectedVoucherAmount!=0){
					$("#lengthTime span").html(selectedVoucherAmount+"月");
				}else{
					$("#lengthTime span").html("暂无");
				}
			}
			time_OnChange(units,rentTime,selectedVoucherAmount);
		}
	})
	//支付

	$(".payButton").bind('click',function(){

		var name = $(".nameInput").val();
		if(name == null || name == "" ){
			alert("请输入姓名！");
			//createStyle("body", "提交成功,感谢您的反馈", "2.4rem");
			createStyle("body","请输入姓名","2.4rem");
			return false;
		}
		var phone = $(".phoneInput").val();
		if(phone == null || phone == ""  ){
			alert("请输入手机号码！");
			createStyle("body","请输入手机号码","2.8rem");
			return false;
		}else{
			if(!(/^1[34578]\d{9}$/.test(phone))){
				alert("手机号码有误，请重填！");
				createStyle("body","手机号码有误,请重填","3.2rem");
				return false;
			}
		}
		var chk= document.getElementById('ckbox');
		if(chk.checked==false){
			alert("请勾选用户使用协议！");
			createStyle("body","请勾选用户协议","2.6rem");
			return false;
		}

		addCookie("member_name",encodeURI(name),30);
		addCookie("member_phone",phone,30);
		var totals = document.getElementById("totalInput").value;
		var unit = document.getElementById("unitInput").value;
		if(unit==null || unit==""){
			alert("请从新选择时间，谢谢");
			createStyle("body","请重新选择时间","2.6rem");
			return false;
		}
		//document.getElementById('payButton').style.display='none';
		//document.getElementById('price').style.display='none';
		var rentalTime = document.getElementById("rentalTimeInput").value;
		var numberInput = document.getElementById("numberInput").value;
		var wahoceIdInput = document.getElementById("wahoceIdInput").value;
		var memberTel = $(".phoneInput").val();
		var memberName = $(".nameInput").val();
		document.getElementById("openIdInput").value = openId;
		var openIdInput = document.getElementById("openIdInput").value;
		voucherId=defaultVoucherId;
		//alert("1:1+addImmediateView");
		$.ajax({
			url:"/addImmediateView",
			type:'POST',
			data:{
				unit:unit,
				number:numberInput,
				total:totals,
				memberName:memberName,
				memberTel:memberTel,
				openId:openIdInput,
				wahoceId:wahoceIdInput,
				wahoId:wahoId,
				wahoType:wahoType,
				rentalTime:rentalTime
			},
			success:function(msg){
				//alert("1:1+msg");
				var json=msg.info;
				var orderId = json.orderId;
				//alert("orderId:"+orderId);
				if (orderId== undefined){
					alert("订单失败！"+json.returnList[0].message);
					return;
				}
				var flag="0";
				//alert("1:1+flag"+flag);
				if(totals=="0.00" || totals=="0.0" || totals=="0" ){
					//alert("2:2+flag"+flag);
					updateOrderStatus(orderId,voucherId,openIdInput);
				}else{

					//alert("voucherId:"+voucherId);
					var appId = json.appId;
					//alert("appId:"+appId);
					//优惠券更改
					if(voucherId != null || voucherId != "" ){
						//alert("appId1:"+appId);
						voucherStateChange(voucherId,orderId,flag,appId);
					}else{
						var parms=orderId+"-"+flag;
						var url = globalVar.baseUrl+"web/whc/warehouse/pay2.jsp";

						var weixinUrl="https://open.weixin.qq.com/connect/oauth2/authorize?appid="+appId+"&redirect_uri="+url+"?parms="+parms+"&flag="+flag+"&response_type=code&scope=snsapi_base&state=123#wechat_redirect";
						window.location.href=weixinUrl;
					}
				}
			},error:function(){
				alert("订单失败！");
				createStyle("body","22222222","2.4rem");
			}
		});
		$(".payButton").unbind('click');
	});
	//使用优惠券后优惠券状态改变
	function voucherStateChange(voucherId,orderId,flag,appId){
		//alert("voucherId:"+voucherId);
		//alert("appId2:"+appId);
		$.ajax({
			type: "post",
			data:{"voucherId":voucherId,"orderId":orderId},
			url:"/modifyVoucherState",
			success:function(msg){
				var parms=orderId+"-"+flag;
				var url = globalVar.baseUrl+"web/whc/warehouse/pay2.jsp";
				var weixinUrl="https://open.weixin.qq.com/connect/oauth2/authorize?appid="+appId+"&redirect_uri="+url+"?parms="+parms+"&flag="+flag+"&response_type=code&scope=snsapi_base&state=123#wechat_redirect";
				window.location.href=weixinUrl;
			},
			error:function(msg){
				alert("优惠券使用失败");
			}
		});
	}
	//修改订单状态
	function updateOrderStatus(orderId,voucherId,openIdInput){

		$.ajax({
			type: "get",
			data:{"orderId":orderId,
				  "startDate":"",
				  "endDate":"",
				  "voucherId":voucherId
			},
			url:"/updateOrderStatus?ver=&time="+(new Date()),
			success:function(msg){
				//alert("成功去开锁");
				openDoor(orderId,openIdInput);
			},
			error:function(msg){
				alert(msg);
			}

		});
	}
	//调用开锁功能
	function openDoor(orderId,openIdInput){

		$.ajax({
			type: "get",
			data:{"orderId":orderId},
			url:"/openDoor/openDoorByOrderId?time="+(new Date()),
			success:function(msg){
				jsonOpen=msg;
				cabinetDoorNo = jsonOpen.data.cabinetDoorNo;
				//alert("cabinetDoorNo:"+cabinetDoorNo);
				window.location.href="/web/whc/member/openSuccess.html?cabinetDoorNo="+cabinetDoorNo+"&orderId="+orderId+"&openId="+openIdInput;
			},
			error:function(msg){

				createStyle("body","开5555","2.8rem");
			}
		});
	}

	//将后台返回的string类型的转换为数值类型
	//起租的进行转换
	//优惠券展示
	$("#lengthTime").on('click',function(){
		$("#fillTxt").hide();
		$("#coupons").show();
		$(".couponsList").html('');
		console.log(memberIds);
		console.log(units);
		console.log(wahoId);
		$.ajax({
			url:"/getVoucherList",
			type:"post",
			async:false,
			data:{
				memberId:memberIds,
				units:units,
				warehouseId:''
			},
			success:function(msg){
				//document.write(msg);
				//alert("msg:"+msg.data.voucherList);
				voucherList=msg.data.voucherList;
				console.log("msg.data"+msg.data);
				console.log("msg.result"+msg.result);
				//alert(msg.code);
				//alert("voucherList[i].id:"+voucherList[0].id);
				//alert('voucherList:'+voucherList);
				if(msg.code!="10000"){
					$("#couponsList").html('<div class="infoName"><p>'+msg.data+'</p></div>');
				}else{
					var listVouc=msg.data;
					if (voucherList.length==0){
						$("#couponsList").html('<div class="infoName2"><p>没有可用优惠券</p></div>');
					}
				}
				//voucherList=msg.info.voucherList;
				var couList=document.getElementById("couponsList");
				var vocherTime='';
				for(var i=0;i<voucherList.length;i++){
					//alert(voucherList[i].id);
					if(voucherList[i].unit==="2"){
						vocherTime='天';
					}
					else if(voucherList[i].unit==="1"){
						vocherTime='小时';
					}
					var sdefaultVoucherId = voucherList[i].id;
					var couTimeTo=voucherList[i].createTime;
					var subStr= voucherList[i].validTo;
					var validTo = typeof(subStr)=="undefined" ? "长期" : subStr.substring(0,10) ;
					var voucherTitle = typeof(voucherList[i].name) == "undefined" ? "通用券": voucherList[i].name;
					var couponlistDiv = document.createElement("div");
					couponlistDiv.className='couponsInfo';
					couponlistDiv.innerHTML='<div class="infoName" onclick=voucherClick("'+i+'")>\
													<p>'+voucherList[i].amount+vocherTime+'</p>\
													<div class="yLine"></div>\
													<div class="infoRight">\
														<div class="top">'+ voucherTitle +'</div>\
														<div class="bottom">有效期至：'+ validTo +'</div>\
													</div>\
										   		</div>\
										   <img src="img/jx.png" class="checkYes" />\
									   <img src="img/ye.png" class="ye"/>';
					couList.appendChild(couponlistDiv);
					if(defaultVoucherId==voucherList[i].id)
						$('.checkYes').eq(i).show();

				/*	$('.couponsInfo').eq(i).on('click',function(){

					})*/
				}
				if(!couponlistDiv){
					$('.couponTxt').show();
				}
				else{
					$('.couponTxt').hide();
				}
			}
		});


	});
//alert(voucherList);
	$("#coupons .titLeft").on('click',function(){
		$("#fillTxt").show();
		$("#coupons").hide();
	});
	$("#rentTime").on('click',function(){

		$('.line_bottom .right').css({'color':'#989898'})
		var numhourS=Number(hourStarts);
		var numdayS=Number(dayStarts);
		//alert(numdayS);
		var nummonthS=Number(monthStarts);

		var xzhour=Number(hourRenews);
		var xzday=Number(dayRenews);
		var xzmonth=Number(monthRenews);
		if(hourRenews!="0"){
			var nextNumber=numhourS;
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
		if(dayRenews!="0"){
			//console.log(typeof numdayS);
			var nextNumber=numdayS;
			if(nextNumber==1&&xzday==2){
				$("#demo2 option").eq(27).remove();
				$("#demo2 option").eq(28).remove();
			}
			if(nextNumber==2&&xzday==2){
				$("#demo2 option").eq(26).remove();
				$("#demo2 option").eq(27).remove();
				$("#demo2 option").eq(28).remove();
			}

			//alert($("#demo2 option").length);
			for(var i=0;i<=$("#demo2 option").length;i++){
					for(var n=i;n<=("#demo2 option").length;n++){
					var thisNumber=parseInt($("#demo2 option").eq(i).val());
						//alert($("#demo2 option").length);
						//alert(thisNumber);
						//alert(nextNumber);
						console.log(thisNumber);
					if(thisNumber!==nextNumber){
						//
						//alert($("#demo2 option").eq(i).val());
						//alert(thisNumber);
						$("#demo2 option").eq(i).remove();
					}

				}
				//alert("nextNumber:"+nextNumber+"xzday:"+xzday);
				nextNumber=nextNumber+xzday;

			}
		}
		if(monthRenews!="0"){
			var nextNumber=nummonthS;
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
		if(hourRenews !="0" && dayRenews !="0" && monthRenews !="0"){
			$("#slideTime").html("<div id='li1' class='hour'>按时选择</div><div id='li2' class='day'>按天选择</div><div id='li3' class='month'>按月选择</div>");
			defaultMob("#demo1");
		} if(hourRenews !="0" && dayRenews =="0" && monthRenews !="0"){
			$("#slideTime").html("<div id='li1' class='hour' style='width:3.185rem;float:left;border-top-left-radius:.2rem;'>按时选择</div><div id='li3' class='month' style='width:3.185rem;float:left;border-right:0;border-top-right-radius:.2rem;'>按月选择</div>");
			defaultMob("#demo1");
		} if(hourRenews !="0" && dayRenews !="0" && monthRenews =="0"){
			$("#slideTime").html("<div id='li1' class='hour' style='width:3.185rem;float:left;border-top-left-radius:.2rem;'>按时选择</div><div id='li2' class='day' style='width:3.185rem;float:left;border-right:0;border-top-right-radius:.2rem;border-right:0;'>按天选择</div>");
			defaultMob("#demo1");
		} if(hourRenews !="0" && dayRenews =="0" && monthRenews =="0"){
			$("#slideTime").html("<div id='li1' class='hour' style='width:6.44rem;float:left;border-right:0;border-top-right-radius:.2rem;border-top-left-radius:.2rem;'>按时选择</div>");
			defaultMob("#demo1");
		} if(hourRenews =="0" && dayRenews !="0" && monthRenews !="0"){
			$("#slideTime").html("<div id='li2' class='day' style='width:3.185rem;float:left;border-top-left-radius:.2rem;'>按天选择</div><div id='li3' class='month' style='width:3.185rem;float:left;border-right:0;border-top-right-radius:.2rem;'>按月选择</div>");
			defaultMob("#demo2");
		} if(hourRenews =="0" && dayRenews !="0" && monthRenews =="0"){
			$("#slideTime").html("<div id='li2' class='day' style='width:6.44rem;float:left;border-right:0;border-top-right-radius:.2rem;border-top-left-radius:.2rem;'>按天选择</div>");
			defaultMob("#demo2");
		} if(hourRenews =="0" && dayRenews =="0" && monthRenews !="0"){
			$("#slideTime").html("<div id='li3' class='month' style='width:6.44rem;float:left;border-right:0;border-top-right-radius:.2rem;border-top-left-radius:.2rem;'>按月选择</div>");
			defaultMob("#demo3");
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
		var str=$("#rentTime span").html();
		var strLast=str.substr(str.length-1,1);
		//alert(strLast);
		if(strLast=='天'){
			$(".day").addClass('on');
			$(".price_des2").show();
			$(".price_des1").hide();
			$(".price_des3").hide();
		}
		if(strLast=='月'){
			$(".month").addClass('on');
			$(".price_des3").show();
			$(".price_des1").hide();
			$(".price_des2").hide();
		}
		if(strLast=='时'){
			$(".hour").addClass('on');
			$(".price_des1").show();
			$(".price_des2").hide();
			$(".price_des3").hide();
		}
	})
	$(".line_bottom .left").on('click',function(){
		$(".transparent").hide();
		$(".slideChange").hide();
	})
	$(".line_bottom .right").on('click',function(){
		$(this).css({'color':'#19b2f0'});
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
					//alert($('#li2').attr("id"));
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
				}
				if(unitName=="li2"){
					units="2";
				}
				if(unitName=="li3"){
					units="3";
				}
				nearbyNumber(units,listvouchers,Number(rentTime));
			}

		})
		$(".transparent").hide();
		$(".slideChange").hide();
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
	$(".nameInput").focus(function(){
		//$(".payment").hide();
	});
	$(".phoneInput").focus(function(){
		//$(".payment").hide();
	});
	$(".stampInput").focus(function(){
		//$(".payment").hide();
	});
	$(".agreement").on('click',function(){
		$(".transparent").show();
		$(".userSay").show();
		$(".bottom").on('click',function(){
			$(".transparent").hide();
			$(".userSay").hide();
		})
	});
	//$(".nameInput").blur(function(){
	//$(".payment").show();
	//});
	//$(".phoneInput").blur(function(){
	//$(".payment").show();
	//});
	//$(".stampInput").blur(function(){
	//	$(".payment").show();
	//});
	//绑定优惠券 填单子的绑定
	$("#bind_vau").on('click',function(){
		var voucherCodes=$("#stamp_Input").val();
		if(voucherCode==""){
			alert("请输入优惠券！");
			//createStyle("body","请输入优惠券","2.6rem");
		}else{
			$.ajax({
				url:"/bindVoucher",
				type:'post',
				data:{
					voucherCode:voucherCodes,
					memberId:'',
					openId:openId
				},
				success:function(msg){
					 //alert(message.msg);
					/*if(msg.result=="true"){
						//alert('绑定成功！');
						createStyle("body","绑定成功","2.4rem");
						jsonVoucher=msg.info;
						var code=jsonVoucher.code;
						var amount=jsonVoucher.amount;
						var mberId=jsonVoucher.memberId;
						var id=jsonVoucher.id;
						var unit=jsonVoucher.unit;
						var arr={"memberId":mberId,"voucherAmount":amount,"voucherUnit":unit,"voucherCode":code,"voucherId":id};
						listvouchers.push(arr);
						nearbyNumber(units,listvouchers,Number(rentTime));
						//alert(jsonVoucher);
					}else if(msg.result=="false"){
						alert(msg.info);
					}*/
				},
				error:function(){
					alert("失败");
					createStyle("body","失败","2.2rem");
				}
			});
		}
	});
	//绑定优惠券2
	$("#bindVoucher").on('click',function(){
		var voucherCode=$("#stamp_Input").val();
		if(voucherCode==""){
			alert("请输入优惠券！");
			//createStyle("body","请输入优惠券","2.4rem");
		}else{
			$.ajax({
				url:"/bindVoucher",
				type:'post',
				data:{
					//memberId:memberId,
					voucherCode:voucherCode,
					memberId:'',
					openId:openId
					//status:status
				},
				success:function(message){
					alert(message.msg);
				/*	if(msg.result=="true"){
						//alert('绑定成功！');
						createStyle("body","绑定成功","2.4rem");
						jsonVoucher=msg.info;
						var code=jsonVoucher.code;
						var amount=jsonVoucher.amount;
						var mberId=jsonVoucher.memberId;
						var id=jsonVoucher.id;
						var unit=jsonVoucher.unit;
						var arr={"memberId":mberId,"voucherAmount":amount,"voucherUnit":unit,"voucherCode":code,"voucherId":id};
						listvouchers.push(arr);
						nearbyNumber(units,listvouchers,Number(rentTime));
						alert(jsonVoucher);
					}else if(msg.result=="false"){
						alert(msg.info);
					}*/
				},
				error:function(){
					alert("请求失败");
					//alert("失败");
					//createStyle("body","请输入优惠券","2.2rem");
				}
			});
		}
	});
});
//屏幕改变尺寸
var H=$(window).height();
$(window).resize(function() {
	if($(window).height()<H){
		$(".payment").hide();
	}
	if($(window).height()>=H){
		$(".payment").show();
	}
});
//筛选最近优惠券1
function nearbyNumber(unit,listvouchers,lengthTime){

	var objArray=new Array();
	var map=new Map();
	var listvoucherNuit=null;
	for(var i=0;i<listvouchers.length;i++){
		if(unit==1 && Number(listvouchers[i].voucherUnit)==1){
			listvoucherNuit=1;
			var numberAmount=Number(listvouchers[i].voucherAmount);
			if(lengthTime-numberAmount<0){
				var n=lengthTime-numberAmount;
				map.put(-n,numberAmount);
				objArray[i]=-n;
			}else{
				var n=lengthTime-numberAmount;
				map.put(n,numberAmount);
				objArray[i]=n;
			}
		}else if(unit==2 && Number(listvouchers[i].voucherUnit)==2){
			listvoucherNuit=2;
			var numberAmount=Number(listvouchers[i].voucherAmount);
			if(lengthTime-numberAmount<0){
				var n=lengthTime-numberAmount;
				map.put(-n,numberAmount);
				objArray[i]=-n;
			}else{
				var n=lengthTime-numberAmount;
				map.put(n,numberAmount);
				objArray[i]=n;
			}
		}
	}
	objArray.sort(function(n1,n2){
		return n1-n2;
	});
	if(unit==1 && listvoucherNuit==1){
		var voucherTime = map.get(objArray[0]);

		//优惠券可用时长
		$("#lengthTime span").html(voucherTime+"小时");
		//默认使用时长
		$("#rentTime span").html(lengthTime+"小时");

		time_OnChange(unit,lengthTime,Number(voucherTime));

	}else if(unit==2 && listvoucherNuit==2){
		//优惠券可用时长
		$("#lengthTime span").html(map.get(objArray[0])+"天");
		//默认使用时长
		$("#rentTime span").html(lengthTime+"天");
		time_OnChange(unit,lengthTime,map.get(objArray[0]));
	}else{
		//默认使用时长
		if(unit==1){$("#rentTime span").html(lengthTime+"小时");}
		if(unit==2){$("#rentTime span").html(lengthTime+"天");}
		if(unit==3){$("#rentTime span").html(lengthTime+"月");}
		time_OnChange(unit,lengthTime,0);
		$("#lengthTime span").html("暂无");
	}
	for(var n=0;n<listvouchers.length;n++){
		if(Number(listvouchers[n].voucherAmount)==map.get(objArray[0])){
			memberIds=listvouchers[n].memberId;
			//优惠券id
			voucherIds=listvouchers[n].voucherId;
			defaultVoucherId=voucherIds;
			//优惠券Code
			voucherCodes=listvouchers[n].voucherCode;

			return;
		}
	}
}

function voucherClick(i){
	if($('.checkYes').eq(i).css('display')=='none')
	{
		$('.checkYes').hide();
		$('.checkYes').eq(i).show();
		//alert(voucherList);
		defaultVoucherId = voucherList[i].id;
		//selectedVoucherId = voucherList[i].VOUCHER_ID;
		selectedVoucherAmount = voucherList[i].amount;
		selectedVoucherUNIT = voucherList[i].unit;
		//alert(defaultVoucherId);
	}
	else
	{
		$('.checkYes').eq(i).hide();
		defaultVoucherId = null;
		selectedVoucherAmount =0;
		selectedVoucherUNIT = voucherList[i].unit;
		time_OnChange(selectedVoucherUNIT,voucherList,0);
	}
}
//计算即时柜价格rentTime
function time_OnChange(unit,returnTime,voucherTime){
	var rentTimes;
	if(returnTime-voucherTime<0){
		rentTimes=0;
	}else{
		rentTimes=returnTime-voucherTime;
	}

	//判断价格表不为null
	var total=0.0;
	if(priceLists != null){
		for(var i=0;i<priceLists.length;i++){
			var price=0.0;
			var _total=0.0;
			var beginTime=Number(priceLists[i].beginTime);
			var endTime=Number(priceLists[i].endTime);
			console.log(endTime);
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
				if(endTime != null && beginTime < rentTimes){
					if(rentTimes <  endTime){
						_total=price*(rentTimes-beginTime).toFixed(2);
					}else{
						_total=price*(endTime-beginTime).toFixed(2);
					}
				}else if(endTime==null&&beginTime < rentTimes){
					_total=price*(rentTimes-beginTime).toFixed(2);
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

//jquery map工具类
function Map() {
	/** 存放键的数组(遍历用到) */
	this.keys = new Array();
	/** 存放数据 */
	this.data = new Object();
	/**
	 * 放入一个键值对
	 * @param {String} key
	 * @param {Object} value
	 */
	this.put = function(key, value) {
		if(this.data[key] == null){
			this.keys.push(key);
		}
		this.data[key] = value;
	};

	/**
	 * 获取某键对应的值
	 * @param {String} key
	 * @return {Object} value
	 */
	this.get = function(key) {
		return this.data[key];
	};

	/**
	 * 删除一个键值对
	 * @param {String} key
	 */
	this.remove = function(key) {
		this.keys.remove(key);
		this.data[key] = null;
	};

	/**
	 * 遍历Map,执行处理函数
	 *
	 * @param {Function} 回调函数 function(key,value,index){..}
	 */
	this.each = function(fn){
		if(typeof fn != 'function'){
			return;
		}
		var len = this.keys.length;
		for(var i=0;i<len;i++){
			var k = this.keys[i];
			fn(k,this.data[k],i);
		}
	};

	/**
	 * 获取键值数组(类似Java的entrySet())
	 * @return 键值对象{key,value}的数组
	 */
	this.entrys = function() {
		var len = this.keys.length;
		var entrys = new Array(len);
		for (var i = 0; i < len; i++) {
			entrys[i] = {
				key : this.keys[i],
				value : this.data[i]
			};
		}
		return entrys;
	};

	/**
	 * 判断Map是否为空
	 */
	this.isEmpty = function() {
		return this.keys.length == 0;
	};

	/**
	 * 获取键值对数量
	 */
	this.size = function(){
		return this.keys.length;
	};

	/**
	 * 重写toString
	 */
	this.toString = function(){
		var s = "{";
		for(var i=0;i<this.keys.length;i++,s+=','){
			var k = this.keys[i];
			s += k+"="+this.data[k];
		}
		s+="}";
		return s;
	};
}
