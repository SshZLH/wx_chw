var clicksPhoneType;
var jsonObject;
if(navigator.userAgent.indexOf("Android") > -1){
    //alert("安卓");
    clicksPhoneType="安卓";
}
else if(navigator.userAgent.indexOf("iPhone") > -1){
    clicksPhoneType="苹果";
}
else{
    clicksPhoneType="其他";
}
$(function() {
    
    var url = location.search;
    //获取url中"?"符后的字串
    var cabinetDoorNo;
    var orderId;
    var status = getUrlParameter("status");
    var openId = getUrlParameter("openId");
    cabinetDoorNo = getUrlParameter("cabinetDoorNo");
    orderId = getUrlParameter("orderId");
    var result = cabinetDoorNo + "号柜门已打开";
   // alert();
    var feedJson = {
        quest_type:null,
        quest_comment:null,
        quest_order_id:orderId,
        quest_source_url:document.referrer,
        quest_access_ip:returnCitySN["cip"],
        quest_access_addr:returnCitySN["cname"],
        quest_browse_message:navigator.userAgent
    };
    $("#center").html(result);
    $("#seeOrder").on("click", function() {
        if (status == "finish") window.location.href = "/web/whc/member/completeDetail.html?orderId=" + orderId; else window.location.href = "/web/whc/member/doDetail.html?orderId=" + orderId;
    });
    //点击显示，点击关闭进行隐藏
    $("#feedBack").on("click", function() {
        $(".txtArea textarea").val("请描述您的问题");
        $(".checkBtn").removeClass("active");
        $(".closeFeed").show();
        $(".transparent").show();
        $(".feedBackCheck").show();
        $(".closeFeed").on("click", function() {
            $(".checkBtn").removeClass("active");
            $(".txtArea").hide();
            $(".feedBackCheck").hide();
            $(".closeFeed").hide();
            $(".transparent").hide();
        });
    });
    //反馈的内容区域
    $(".feedContent").each(function(i) {
        $(this).on("click", function() {
            //alert(i);
            $(".feedDiv.submit").css({
                background:"#FFF"
            });
            $(".checkBtn").removeClass("active");
            $(".checkBtn").html("");
            $(".checkBtn").eq(i).addClass("active");
            $(".checkBtn").html("<img src='img/selected.png'>");
            if ($(".checkBtn.active").eq(i) && i < 4) {
            	feedJson.quest_comment = '无';
                $(".txtArea textarea").val("请描述您的问题");
                $(".txtArea").hide();
                $(".count1").html("0");
                feedJson.quest_type = $(".feedContent span").eq(i).html();
            }
            if ($(".checkBtn").eq(4).hasClass("active")) {
                $(".txtArea").show();
                feedJson.quest_type = $(".feedContent span").eq(4).html();
            }
        });
    });
    //计算文本域里面的数目
    $(".txtArea textarea").focus(function() {
        if ($(".txtArea textarea").val() == "请描述您的问题") {
            $(".txtArea textarea").val("");
        }
    });
    $(".txtArea textarea").blur(function() {
        $(".txtArea textarea").focus(function() {
            $(".count1").html("0");
        });
        if ($(".txtArea textarea").val() != "请描述您的问题") {}
    });
    $(".txtArea textarea").bind("input propertychange", function() {	
        $(".count1").html($(".txtArea textarea").val().length);
    });
    $(".titLeft").on("click", function() {
        var orderCenterUrl = "/web/whc/member/center.html?openId=" + openId;
        window.location.href = orderCenterUrl;
    });
    //页面创建一个新的弹出提示效果
    $(".feedDiv.submit").on("click", function() {
        if ($(".txtArea textarea").val() == "请描述您的问题" || $(".txtArea textarea").val() == "") {
            if ($(".checkBtn").eq(4).hasClass("active")) {
                if ($(".txtArea textarea").val() == "" || $(".txtArea textarea").val() == "请描述您的问题") {
                    setTimeout('$(".altTxtLine2").fadeIn("slow")', 30);
                    setTimeout('$(".altTxtLine2").fadeOut("slow")', 1200);
                    return false;
                }
            }
        } else {
            feedJson.quest_comment = $(".txtArea textarea").val();
        }
        if (!$(".checkBtn").hasClass("active")) {
            setTimeout('$(".altTxtLine").fadeIn("slow")', 30);
            setTimeout('$(".altTxtLine").fadeOut("slow")', 1200);
            return false;
        } else {
            $.ajax({
                type:"post",
                url:"/questionManage/addQuestion",
                data:{
                    quest_type:feedJson.quest_type,
                    quest_comment:feedJson.quest_comment,
                    quest_order_id:feedJson.quest_order_id,
                    quest_source_url:feedJson.quest_source_url,
                    quest_access_ip:feedJson.quest_access_ip,
                    quest_access_addr:feedJson.quest_access_addr,
                    quest_browse_message:feedJson.quest_browse_message
                },
                success:function(msg) {
                   // console.log(msg);
                    //alert("11");
                    //console.log(msg);
                    $(".feedBackCheck").hide();
                    $(".closeFeed").hide();
                    $(".transparent").hide();
                    //console.log();
                    //createStyle("body", "提交成功,感谢您的反馈", "3.4rem");
                    if (msg.code == "10000") {
                        $(".feedBackCheck").hide();
                        $(".closeFeed").hide();
                        $(".transparent").hide();
                        createStyle("body", "提交成功,感谢您的反馈", "3.4rem");
                        setTimeout(function() {
                            window.location.href = "/web/whc/member/center.html?openId=" + openId;
                        }, 1450);
                    }
                },
                error:function(msg) {
                	createStyle("body", "提交失败,请重新提交", "3.4rem")
                }
            });
        }
    });
    //广告位
    //广告贴片
    $.ajax({
        url:"/advManage",
        data:{
            openId:openId,
            orderId:orderId
        },
        type:"get",
        success:function(msg){
            //alert(1);
            jsonObject=msg[0]
            //console.log(msg[0]);
            //json=msg.info;
            var slide=document.getElementById("tjtp");
           // var slide=document.getElementById("tjtp");
            slide.innerHTML='<img class="closeAdv" style="width:.25rem;height:.25rem;position:absolute;right:.02rem;top:.02rem;z-index:99" src="img/closeAdv.png"><img class="advImg" style="width:100%;height:100%" src='+globalVar.supportUrl+'app/whc/uploadFile/import/'+msg[0].imgUrl+'>';
           // alert(slide);
            //slide.innerHTML='';
            $("#tjtp .closeAdv").on('click',function(){
                $("#tjtp").hide();
            });
            $("#tjtp .advImg").on('click',function(){
                //alert(i);
                //alert(json.advShowList[0].activityId);
                $.ajax({
                    url:'/advClicks',
                    data:{
                        activityId:msg[0].imgUrl.activityId,
                        contentId:msg[0].imgUrl.contentId,
                        openId:openId,
                        clicksPhoneType:clicksPhoneType,
                        clicksUserIp:returnCitySN["cip"],
                        orderId:orderId
                    },
                    type:'GET',
                    success:function(msg){
                        //alert(msg);
                        //alert(msg);
                        //alert("成功");
                        //console.log(msg);
                        window.location.href="http://"+jsonObject.contentUrl;
                    },
                    error:function(){
                        alert("失败");
                    }
                })

            });


        },
        error:function(){
            alert("失败");
        }
    })
});