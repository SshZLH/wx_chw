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
        if (status == "finish") window.location.href = "/app/whc2/member/toBePaid.html?orderId=" + orderId; else window.location.href = "/app/whc2/member/doDetail.html?orderId=" + orderId;
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
        var orderCenterUrl = "/app/whc2/member/center.html?openId=" + openId;
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
                type:"get",
                url:"/memberManage/setQuestionAction.do",
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
                    $(".feedBackCheck").hide();
                    $(".closeFeed").hide();
                    $(".transparent").hide();
                    console.log();
                    //createStyle("body", "提交成功,感谢您的反馈", "3.4rem");
                    if (msg.info == "添加成功") {
                        $(".feedBackCheck").hide();
                        $(".closeFeed").hide();
                        $(".transparent").hide();
                        createStyle("body", "提交成功,感谢您的反馈", "3.4rem");
                        setTimeout(function() {
                            window.location.href = "/app/whc2/member/center.html?openId=" + openId;
                        }, 1450);
                    }
                },
                error:function(msg) {
                	createStyle("body", "提交失败,请重新提交", "3.4rem")
                }
            });
        }
    });
});