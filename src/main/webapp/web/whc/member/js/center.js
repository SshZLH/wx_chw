var jsonOpen;
var json;
var openId;
var cabinetDoorNo;
var wahoType;
var memberId;
var statusList;
var orderId;
var url = location.search; //获取url中"?"符后的字串
 if (url.indexOf("?") != -1) {
 var str = url.substr(1);
 strs = str.split("&");
 var strName = strs[0];
 var strNamelist=strName.split("=");
 openId=strNamelist[1];
 }
//开门指令
//alert(openId);
function openDoor(orderId){
    $.ajax({
        type: "get",
        data:{"orderId":orderId,
            "onlyOpenDoor":''
        },
        url:"/openDoor/openDoorByOrderId",
        success:function(msg){
            //alert(msg);

            //cabinetDoorNo = jsonOpen.warehouseCell.number;
           // alert(msg.code);
            if(msg.code=="10000"){
               // alert(msg.data.cabinetDoorNo);
               // alert(msg.data.orderId);
                cabinetDoorNo=msg.data.cabinetDoorNo;
                var dataOrderId = msg.data.orderId;
                $(".transparent").hide();
                $(".CloseOrder").hide();
                $(".qxzh").hide();
                window.location.href="/web/whc/member/openSuccess.html?cabinetDoorNo="+cabinetDoorNo+"&orderId="+dataOrderId+"&openId="+openId+"&t="+new Date().getTime();
            }
            else{
                alert("开门失败,请您重新尝试");
                $(".transparent").hide();
            }

        },
        error:function(msg){
        }
    });
}
//仅退柜指令
function justExitCabi(orderId,cabinetDoorNo,openId){
    $.ajax({
        type: "get",
        data:{"orderId":orderId},
        url:"/returnWarehouse",
        success:function(msg){
            $(".transparent").hide();
            window.location.href="/web/whc/member/quitSuccess.html?cabinetDoorNo="+cabinetDoorNo+"&orderId="+orderId+"&openId="+openId+"&t="+new Date().getTime();
        },
        error:function(msg){
            if(msg.result=='false')
                alert(msg.info);
        }
    });
}
//优惠券列表界面
function couponsList(){
    //alert(memberId);
    //var URL="http://192.168.0.131:8083/memberCenter/getMemVoucherList";
    //var parameter = "memberId=fbb8f2925852dceb015852e015f50000&units=&warehouseId=";
    //var method="POST";
    $.ajax({
        type:"post",
        url:"/getVoucherList",
        async:false,
        data:{
             memberId:memberId,
             units:'',
             warehouseId:''
        },
        success:function(msg){
            //document.write(msg);
            // alert("成功");
            //alert(msg);
            //console.log("msg"+msg);
            var listLength=msg.data.voucherList;
            var couList=document.getElementById("couponList");
            var vocherTime='';
            for(var i=0;i<listLength.length;i++){
                if(listLength[i].unit==="2"){
                    vocherTime='天';
                }
                else if(listLength[i].unit==="1"){
                    vocherTime='小时';
                }
                var couTimeTo=listLength[i].createTime;
                var subStr= listLength[i].validTo;
                var validTo = typeof(subStr)=="undefined" ? "长期" : subStr.substring(0,10) ;
                var voucherTitle = typeof(listLength[i].wahoName) == "undefined" ? "通用券": listLength[i].wahoName;
                //alert("voucherTitle"+voucherTitle);
                //alert("voucherTitle123"+voucherTitle);
                var couponlistDiv = document.createElement("div");
                couponlistDiv.className='couponsInfo';
                couponlistDiv.innerHTML='<div class="infoName">\
   														<p>'+listLength[i].amount+vocherTime+'</p>\
   														<div class="yLine"></div>\
   														<div class="infoRight">\
   															<div class="top">'+ voucherTitle +'</div>\
   															<div class="bottom">有效期至：'+ validTo +'</div>\
   														</div>\
   											   		</div>\
   											   <img src="img/jx.png" class="checkYes" />\
											   <img src="img/ye.png" class="ye"/>';
                couList.appendChild(couponlistDiv);
            }
            if(!couponlistDiv){
                $('.couponTxt').show();
            }
            else{
                $('.couponTxt').hide();
            }
        }
    })
}
//开柜且退柜按钮
function openDoorAndExitCabi(orderId){
    $.ajax({
        type: "get",
        data:{"orderId":orderId,
            "closeOrder":''
        },
        url:"/openDoor/openDoorByOrderId",
        success:function(msg){
            if(msg.code=="10000"){
                cabinetDoorNo = msg.data.cabinetDoorNo;
                justExitCabi(orderId,cabinetDoorNo,openId);
                $(".qxzh").hide();
            }
            else{
                alert("开门失败,请您重新尝试");
                $(".transparent").hide();
            }
        },
        error:function(msg){
            alert(msg);
        }
    });
}
//历史订单
function hisList(){
    $.ajax({
        type:"get",
        url:"/getOrderList",
        async:false,
        data:{
            openId:openId,
            memberId:'',
            status:'2'
        },
        success:function(msg){
            //document.write(msg);
            //jsonObj= eval("("+msg+")");
           //memberId=jsonObj.member.id;
           // json=jsonObj.resultList;
           // document
            json=msg.data;
            memberId=json.member.id;
            var hisList=document.getElementById("hisList");
            for(var i=0;i<json.resultList.length;i++){
               // statusList=json[i].status;
                var listDiv2 = document.createElement("div");
                var unitName = json.resultList[i].orderUnit=="1"?" 小时": json.resultList[i].orderUnit=="2"?" 天" : " 个月";
                var orderRentalTime=null;
                if(json.resultList[i].orderUnit=="1"){
                    orderRentalTime=json.resultList[i].totalTimes;
                }else{
                    orderRentalTime=json.resultList[i].orderNumber;
                }
                listDiv2.className='currentList2';
                listDiv2.innerHTML='<div class="listLeft2">\
		 						<div class="listInfo2" onclick="orderDetail('+"'"+json.resultList[i].orderId+"','"+json.resultList[i].status+"','"+json.resultList[i].wahoType +"'"+',false)">\
		 						<h1>'+json.resultList[i].wahoName+'</h1>\
		 							<div class="listBody2">\
		 								<p>\
		 									<span class="explain">柜号</span>\
		 									<span class="specificList">'+json.resultList[i].wahoceNumber+'号</span>\
		 								</p>\
		 								<p>\
		 									<span class="explain">金额</span>\
		 									<span class="specificList">'+json.resultList[i].orderAmount+'元</span>\
		 								</p>\
		 								<p>\
		 									<span class="explain">时长</span>\
		 									<span class="specificList">'+orderRentalTime+' '+unitName+'</span>\
		 								</p>\
		 								<p>\
		 									<span class="explain">时间</span>\
		 									<span class="specificList">'+json.resultList[i].orderStartDate+'至'+json.resultList[i].orderEndDate+'</span>\
		 								</p>\
		 								<p>\
		 									<span class="explain">地址</span>\
		 									<span class="specificList adre2" style="width:4.54rem">'+json.resultList[i].wahoAddr+'</span>\
		 								</p>\
		 							</div>\
		 						</div>\
		 		 			</div>\
							<div class="listLRight2">\
		 		 				<div class="listStatus">\
									<img class="statusImg2" src="img/'+json.resultList[i].status+'.png"/>\
								</div>\
		 		 			</div>';
                hisList.appendChild(listDiv2);
            }
            $('.listInfo2').each(function(i){
                $(this).bind('mousedown',function(event){
                    event.preventDefault();
                    timeout = setTimeout(function() {
                        $(".removeAlert").show();
                        $(".transparent").show();
                        $(".remove_bottom .left").on('click',function(){
                            $(".removeAlert").hide();
                            $(".transparent").hide();
                        })
                        $(".remove_bottom .right").on('click',function(){

                            $.ajax({
                                type:"get",
                                url:"/delHistoryOrder",
                                async:false,
                                data:{
                                    orderId:json.resultList[i].orderId
                                },
                                success:function(msg){
                                    alert("删除成功");
                                    window.location.reload();
                                },
                                error:function(msg){
                                    alert("删除失败");
                                }

                            })
                            $(".removeAlert").hide();
                            $(".transparent").hide();

                        })
                    }, 1000);
                });
                $(this).bind("mouseup", function(event) {
                    event.preventDefault();
                    clearTimeout(timeout);
                });
            })
        },
        error:function(msg){
            alert(msg);
        }
    })
}
/*
* 当前订单
* */
function listDate(){
    if (openId==null) {
        alert("OpenId 为空，退出重新进入");
        return;
    }
    $.ajax({
        type:"get",
        url:"/getOrderList",
        async:false,
        data:{
            openId:openId,
            memberId:'',
            status:'1'
        },
        success:function(msg){

            json=msg.data;
           // alert("json:"+json);
            //console.log(msg);
            memberId=json.member.id;
            var orderList=document.getElementById("orderList");
            $('.personl').html(json.member.name);
            $('.circleImg').attr('src',json.member.photo);
            $('.blur').attr('src',json.member.photo);
            json.supperman=="1" ? $('#openDoorID').show():$('#openDoorID').hide();
            $('#openDoorID').attr('href','/web/whc/member/scanDoor4whc.jsp?openId='+openId);
            for(var i=0;i<json.resultList.length;i++){
                var oOrder =json.resultList[i];
                orderId=oOrder.orderId;
                var str=oOrder.orderEndDate;
                var val = Date.parse(str.replace(/-/g,'/'));
                var newDate = new Date(val);
                var oDate=new Date();
                var isOverTime = oDate.getTime()>newDate.getTime()&&oOrder.status=="1";
                var listDiv = document.createElement("div");
                var unitName = oOrder.orderUnit=="1"?" 小时": oOrder.orderUnit=="2"?" 天" : " 个月";
                var orderRentalTime=null;
                if(oOrder.orderUnit=="1"){
                    orderRentalTime=oOrder.totalTimes;
                }else{
                    orderRentalTime=oOrder.orderNumber;
                }
                listDiv.className='currentList';
                var orderDivStr='<div class="listLeft">\
		 						<div class="listInfo" onclick="orderDetail('+"'"+oOrder.orderId+"','"+oOrder.status+"','"+json.resultList[i].wahoType + "',"+  isOverTime +')">\
		 							<h1>'+oOrder.wahoName+'</h1>\
		 							<div class="listBody">\
		 								<p>\
		 									<span class="explain">'+ (oOrder.wahoType =="1" ? "柜号" : "仓位") +'</span>\
		 									<span class="specificList">'+oOrder.wahoceNumber+'号</span>\
		 								</p>\
		 								<p>\
		 									<span class="explain">金额</span>\
		 									<span class="specificList">'+oOrder.orderAmount+'元</span>\
		 								</p>';
                if ( oOrder.wahoType =="1") //柜
                    orderDivStr +='<p>\
		 									<span class="explain">时长</span>\
		 									<span class="specificList">'+orderRentalTime+' '+unitName+'</span>\
		 								</p>';

                orderDivStr +='<p>\
		 									<span class="explain">时间</span>\
		 									<span class="specificList">'+oOrder.orderStartDate+'至'+oOrder.orderEndDate+'</span>\
		 								</p>\
		 								<p>\
		 									<span class="explain">地址</span>\
		 									<span class="specificList adre" style="width:4.54rem">'+oOrder.wahoAddr+'</span>\
		 								</p>\
		 							</div>\
		 						</div>\
		 		 			</div>\
		 		 			<div class="listLRight">\
		 		 				<div class="listStatus">\
									<img class="statusImg" src="img/'+oOrder.status+'.png"/>\
								</div>';
                if ( oOrder.wahoType =="0") //仓
                    orderDivStr +='<div class="renewList" >\
									<span></span>\
								</div>\
								<div class="unlock" onclick="openWahoDoor('+"'"+openId+"'"  +')">\
									<div class="circleUnlock">\
										<div class="icon">\
											<img class="lockIcon" src="img/open_lock_icon.png"/>\
										</div>\
										<div class="openIcon">\
										开门\
										</div>\
									</div>\
								</div>';
                else
                    orderDivStr +='<div class="renewList" onclick="renewOrder2('+"'"+oOrder.orderId+"','"+oOrder.totalTimes+"'" + ')">\
									<span>续租</span>\
								</div>\
								<div class="unlock" onclick="backCabinet('+"'"+oOrder.orderId+"'" + ')">\
									<div class="circleUnlock">\
										<div class="icon">\
											<img class="lockIcon" src="img/open_lock_icon.png"/>\
										</div>\
										<div class="openIcon">\
										开锁\
										</div>\
									</div>\
								</div>\
								<div class="unlock2" onclick="renewOrder('+"'"+oOrder.orderId+"','"+oOrder.status+"'" + ')">\
									<div class="circleUnlock2">\
										<div class="icon2">\
											<img class="lockIcon2" src="img/xf.png"/>\
										</div>\
										<div class="openIcon2">\
										续费\
										</div>\
									</div>\
								</div>';
                listDiv.innerHTML = orderDivStr + '</div>';
                orderList.appendChild(listDiv);
                if (oOrder.status=="0"){
                    $(".renewList span").eq(i).hide();
                    $(".unlock").eq(i).hide();
                    $(".unlock2").eq(i).show();
                    $(".lockIcon2").eq(i).attr("src","img/current_order_paid_icon.png");
                    $(".openIcon2").eq(i).html("支付");		//
                }
                if(isOverTime){
                    $(".statusImg").eq(i).attr("src","img/3.png");		//超时
                    $(".renewList span").eq(i).hide();
                    $(".unlock").eq(i).hide();
                    $(".unlock2").eq(i).show();
                }
            }
            $(".currentList").each(function(i){
                if($(".specificList.adre").eq(i).css("height")>30+'px'){
                    $(".currentList").eq(i).css({"height":"3.5rem"})
                }
            })
        },
        error:function (msg) {
            alert("失败");
        }
    });
}
$(function(){
    // navList();
    navList();
    listDate();
    hisList();
    navList();
    var H=$(window).height();
    $(window).resize(function() {
        if($(window).height()<H){
            $(".navList").hide();
        }
        if($(window).height()>=H){
            $(".navList").show();
        }
    });
    if($('.listInfo').length==0){
        $('.zwdd').show();
    }
    else{
        $('.zwdd').hide();
    }

    //nav导航滑动选项
    //当前订单
    $(".recList").on('click',function(){
        //alert(1);
        listDate();
        $('.zwdd2').hide();
        if($('.listInfo').length==0){
            $('.zwdd').show();
        }
        else{
            $('.zwdd').hide();
        }
        $('.hisList').hide();
        $('.orderList').show();
        $('.couponList').hide();
        $('.triangle').animate({left:'.9rem'});
        $('.rect').animate({left:'.78rem'});
        $('.currentImg').attr('src','img/current_order_white_icon.png');
        $('.hisImg').attr('src','img/history_order_blue.png');
        $('.conponImg').attr('src','img/coupon_blue_icon.png');
    })
    $(".histList").on('click',function(){
        hisList();
        $('.zwdd').hide();
        if($('.listInfo2').length==0){
            $('.zwdd2').show();
        }
        else{
            $('.zwdd2').hide();
        }
        $('.orderList').hide();
        $('.hisList').show();
        $('.couponList').hide();
        $('.triangle').animate({left:'3.4rem'});
        $('.rect').animate({left:'3.28rem'});
        $('.hisImg').attr('src','img/history_order_white_icon.png');
        $('.currentImg').attr('src','img/current_order_blue_icon.png');
        $('.conponImg').attr('src','img/coupon_blue_icon.png');
        $(".currentList2").each(function(i){
            if($(".specificList.adre2").eq(i).css("height")>30+'px'){
                $(".currentList2").eq(i).css({"height":"3.5rem"})
            }
        })
    })
    $(".couLists").on('click',function(){
        $('.zwdd').hide();
        $('.zwdd2').hide();
        $('.hisList').hide();
        $('.orderList').hide();
        $('.couponsInfo').hide();
        $('.couponList').show();
        couponsList();
        $('.triangle').animate({left:'5.94rem'});
        $('.rect').animate({left:'5.82rem'});
        $('.conponImg').attr('src','img/coupon_white_icon.png');
        $('.currentImg').attr('src','img/current_order_blue_icon.png');
        $('.hisImg').attr('src','img/history_order_blue.png');
    })
    //优惠券获取焦点后,导航栏隐藏，失去焦点后，显示导航栏
    //优惠券绑定交互和点击
    $(".bind").on('click',function(){
        var voucherCode=$(".stampInput").val();
       
        if(voucherCode==""){
            alert("请输入优惠券编码！");
        }else{
            $.ajax({
                type:"post",
                url:"/bindVoucher",
                async:false,
                data:{
                    voucherCode:voucherCode,
                    memberId:'',
                    openId:openId
                },
                success:function(message){
                    alert(message.msg);
                },
                error:function(){
                    alert("绑定失败,请重新绑定");
                }
            });
        }

    });
})

function orderDetail(orderId,status,wahoType,isOverTime){
    if (wahoType=="0")
        window.location.href="../member/wahoOrderDetail.html?orderId="+orderId+"&t="+new Date().getTime();	//仓订单
    else if (isOverTime)
        window.location.href="../member/renewDetail.html?orderId="+orderId+"&t="+new Date().getTime();
    else if (status=="0")	//待支付
        window.location.href="../member/waitDetail.html?orderId="+orderId+"&t="+new Date().getTime();
    else if (status=="1")	//进行中
        window.location.href="../member/doDetail.html?orderId="+orderId+"&t="+new Date().getTime();
    else if (status=="2")	//完成
        window.location.href="../member/completeDetail.html?orderId="+orderId+"&t="+new Date().getTime();
}
function renewOrder(orderId,status){
    if(status!="0"){
        var time=new Date().getTime();
        //续费
        window.location.href="/web/whc/member/renew.html?orderId="+orderId+"&Time="+time;
    }else{
        $.ajax({
            url:"/getOrder",
            type:'post',
            data:{
                orderId : orderId
            },
            success:function(msg){
               // console.log(msg);
                if(msg.result=="true"){
                   // alert(msg);
                    var order = msg.info.order.Order;
                    //alert(order);
                    //alert(order.id);
                   // alert(order.status);
                    if(order.status=="0"){
                        var flag="0";
                        var parms=orderId+"-0";	//待支付 首次付费
                        var url = globalVar.baseUrl+"web/whc/warehouse/pay2.jsp";
                        var appId = globalVar.appId;
                        var weixinUrl="https://open.weixin.qq.com/connect/oauth2/authorize?appid="+appId+"&redirect_uri="+url+"?parms="+parms+"&flag="+flag+"&response_type=code&scope=snsapi_base&state=123#wechat_redirect";
                        window.location.href=weixinUrl;
                    }else{
                        alert("您的未支付订单已超期未支付，请重新下单。");
                    }
                }else{
                    alert(msg.info);
                }
            },
            error:function(){
                alert("查询数据失败，请稍后再试，谢谢");
            }
        });
    }
}
/*
 *续租
 */
function renewOrder2(orderId,totalTimes){
    window.location.href="/web/whc/member/continuCab.html?orderId="+orderId+""+"&aboveRentTime="+totalTimes+"&t="+new Date().getTime();
    return false;
}
//开库门
function openWahoDoor(openId){

    window.location.href="/web/whc/member/scanDoor4whc.jsp?openId="+openId+"&t="+new Date().getTime();
}
// 开锁
function backCabinet(orderId){
    $(".transparent").show();
    $(".CloseOrder").css({'display':'block','position':'fixed'}).animate({
        bottom:'.44rem'
    });
    $(".qxzh").css({'display':'block','position':'fixed'}).animate({
        bottom:'.18rem'
    });
    //点击是，调用开柜(开柜并退柜)
    $(".midium1").on('click',function(){
        $(".midium2").removeClass('onClass');
        $(".qxzh").removeClass('onClass');
        $(this).addClass('onClass');
        $(".CloseOrder").hide();
        $(".qxzh").hide();
        openDoorAndExitCabi(orderId);

    });
    //点击否，（仅开柜）
    $(".midium2").on('click',function(){
        $(".midium1").removeClass('onClass');
        $(".qxzh").removeClass('onClass');
        $(this).addClass('onClass');
        $(".CloseOrder").hide();
        $(".qxzh").hide();
        openDoor(orderId);
    });
    $(".qxzh").on('click',function(){
        $(".midium1").removeClass('onClass');
        $(".midium2").removeClass('onClass');
        $(this).addClass('onClass');
        $(".qxzh").removeClass('onClass');
        $(".CloseOrder").hide();
        $(".qxzh").hide();
        $(".transparent").hide();
    });
    return false;
}