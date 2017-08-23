//ajax异步变同步，使用全局变量
//var openId;
var wahoType;
var supportUrl;
var baseURL;
//var json;
var start = null;
var url = location.search;
var openId;
var wahoType;
//var openId='oj75GwQ3Sx7x2IQVGwJa0W-yWX7A';
//var
var wahoType='11';
var url = location.search; //获取url中"?"符后的字串
if (url.indexOf("?") != -1) {
    var str = url.substr(1);
    strs = str.split("&");
    var strOpenId = strs[0];
    var strOpenIdlist=strOpenId.split("=");
    openId=strOpenIdlist[1];
    var strWahoType = strs[1];
    var strWahoTypelist=strWahoType.split("=");
    wahoType=strWahoTypelist[1];
}
function warList(_wahoType,_keyWord){
    //先获取地理位置
    wx.getLocation({
        success: function (res) {
            //alert(res.latitude+" :longitude:"+ res.longitude+"  :"+res.toSource())

            var lati = res.latitude; // 纬度，浮点数，范围为90 ~ -90
            var longi = res.longitude; // 经度，浮点数，范围为180 ~ -180
            start = new qq.maps.LatLng(lati, longi);
            getStoreList(start,_wahoType,_keyWord);
        },
        fail:function(res){
            getStoreList(null,_wahoType,_keyWord);
        }
    });
}

$(function() {
    $(window).scroll(function() {
        if ($(window).scrollTop() >= 100) {
            $(".flyTop").fadeIn(300);
        } else {
            $(".flyTop").fadeOut(300);
        }
    });
    $(".flyTop").click(function() {
        $("html,body").animate({
            scrollTop:"0px"
        }, 800);
    });
    //
    //搜索栏显示问题
    $(".returnLast").on("click", function() {
        $(".search_btn").hide();
        $(this).hide();
        $(".searchInput").css({
            width:"7.13rem"
        });
        $(".searchInput").val("搜索门店、位置");
    });
    navList();
    $(".searchInput").focus(function() {
        //alert(1);
        $(this).css("width", "5.85rem");
        $(this).css({
            color:"#5c5d5d"
        });
        $(".returnLast").show();
        $(".search_btn").show();
        $(this).val("");
        $(".searchButton").hide();
    });
    $(".search_btn").on("click", function() {
        $("#storeList").html("");
        $(".searchInput").css({
            color:"#5c5d5d"
        });
        warList("1",$(".searchInput").val());

        //alert($(".searchInput").val());
        //warList("1", $(".searchInput").val());
    });
    //alert(realPath);
    //var openId = "wahoId=fbb8f2925afa8801015b185fa2cf000c";
    //var _keyWord=$(".searchInput").val();

    //alert(window.location.href);
    $.ajax({
        type:'POST',
        async:false,
        data:{
            "openId":openId,
            "url": window.location.href
        },
        url:"/getSignature",
        success:function(msg){
            //console.log(msg);
            //document.write('msg'+msg);
            supportUrl = msg.supportUrl;
            baseURL = msg.baseURL;
            appId = msg.appId;
            timestamp = msg.timestamp;
            nonceStr = msg.nonceStr;
            signature = msg.signature;
           //alert('supportUrl:'+supportUrl+'baseURL:'+baseURL+'appId:'+appId+'timestamp:'+timestamp+'nonceStr:'+nonceStr+'signature:'+signature);
            //alert(signature);
            wx.config({
                debug: false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
                appId: appId, // 必填，企业号的唯一标识，此处填写企业号corpid
                timestamp: timestamp, // 必填，生成签名的时间戳
                nonceStr: nonceStr, // 必填，生成签名的随机串
                signature: signature,// 必填，签名，见附录1
                jsApiList: [
                    'getLocation'
                ] // 必填，需要使用的JS接口列表，所有JS接口列表见附录2
            });
        },
        error:function(msg){
            alert("huoquditu失败");
        }

    });

    //var searchInput=$(".searchInput").val();
    //alert(searchInput);
    //页面加载请求获取签名

});

//$的结尾

wx.ready(function(){
    //alert("we微信签名及其接口成功");
    warList("1",null);
});
wx.error(function(res){
    // config信息验证失败会执行error函数，如签名过期导致验证失败，具体错误信息可以打开config的debug模式查看，也可以在返回的res参数中查看，对于SPA可以在这里更新签名。
});
//alert(arr);
function getStoreList(start,_wahoType,_keyWord){
    $.ajax({
        type:"post",
        url:"/getWarehouseList",
        async:false,
        data:{
            keyWord:_keyWord,
            wahoType:_wahoType
        },
        success:function(message){
            console.log(message);
            //console.log(message);
            //alert('message'+message);
            $(".loading22").hide();
            var orderList = document.getElementById("storeList");
            //alert(message.data[0].id);
            var json = message.data;
            //alert(json[2].image1);
            if (json.length==0){
                orderList.innerHTML= '<div class="searchInputTxt">未搜到符合条件的门店,请输入其它内容</div>';
                $(".returnLast").on('click',function(){
                    window.location.reload();
                })
            }
            else{
                //var json=jsonObj.dataSet[0].entityList;
                if (start != null) json = sortWarehouseList(json,start)
                for(var i=0;i<json.length;i++){
                    // alert(json[i].type9);
                    if(json[i].price=='null'||json[i].price==undefined){
                        //json[i].price=='0';
                        json[i].price="0.00";
                        // $(".addrInfo .bottom").eq(i).html("0");
                        //alert($(".addrInfo .bottom").eq(i).html("0"));
                    }
                    var listDiv = document.createElement("div");
                    listDiv.className='storeInfo';
                    listDiv.innerHTML = '';
                    if ((i==0) || (json[i].type7<200))
                        listDiv.innerHTML += '<img src="img/zj.png" class="zj" /><img src="img/zj1.png" class="zj1"/>';
                    listDiv.innerHTML += '<div class="right">\
                                                        <img class="right_img" src='+globalVar.supportUrl+'app/whc/uploadFile/import/'+json[i].image1+'>\
                                                        <img class="preloading" src="img/Preloading.png"/>\
                                                        <div class="info">\
                                                            <div class="top">'+json[i].name+'</div>\
                                                            <div class="bottom">\
                                                                <img src="img/address.png" class="posImg" />\
                                                                <div class="addrInfo">\
                                                                    <div class="top">'+json[i].addr.substring(0,13)+'...</div>\
                                                                    <div class="bottom">距离 ' + json[i].type9 + '</div>\
                                                                </div>\
                                                            </div>\
                                                            <div class="Prides">\
                                                                <p class="rise">起</p>\
                                                                <p class="yuan">'+json[i].price+'</p>\
                                                                <p class="money">￥</p>\
                                                            </div>\
                                                        </div>\
                                                   </div>';

                    orderList.appendChild(listDiv);
                    $(".loading22").hide();
                }
                $(".storeInfo").each(function(i) {
                    $(this).on("click", function() {
                        window.location.href = "/web/whc/warehouse/cabinetSelect.html?openId=" + openId + "&warehouseId=" + json[i].id;
                        //alert(json[i].id);
                    });
                    $(".right_img").eq(i).load(function(){
                        $(".preloading").eq(i).hide();
                    })
                    if(i>4){
                        $(".storeInfo").eq(i).addClass("divHide");
                        $(".storeInfo .right_img").not(".divHide").show();
                    }
                });

            }


            $(".storeList").dropload({
                scrollArea:window,
                loadDownFn:function(me) {
                    setTimeout(function() {
                        $(".storeInfo.divHide").eq(0).removeClass("divHide");
                        $(".storeInfo.divHide").eq(1).removeClass("divHide");
                        $(".storeInfo.divHide").eq(2).removeClass("divHide");
                        $(".storeInfo.divHide").eq(3).removeClass("divHide");
                        $(".storeInfo .right_img").not(".divHide").show();
                        // 每次数据加载完，必须重置
                        me.resetload();
                    }, 1e3);
                    if ($(".storeInfo.divHide").length == 0) {
                        me.lock();
                        // 无数据
                        me.noData();
                    }
                }
            });
            if (json.length==0){
                $(".dropload-down").remove();
                $(".dropload-noData").remove();
            }

        },
        error:function(msg){
            alert("shuju失败");
        }

    })
}
//排序
function sortWarehouseList(o, start) {
    var arr = new Array();
    for (var k = 0; k < o.length; k++) {
        var end = new qq.maps.LatLng(parseFloat(o[k].lati), parseFloat(o[k].longi));
        var distance = Math.round(qq.maps.geometry.spherical.computeDistanceBetween(start, end) * 10) / 10;
        var warehouse = o[k];
        var num1 = parseFloat(o[k].disLevel1);
        var num2 = parseFloat(o[k].disLevel2);
        var num3 = parseFloat(o[k].disLevel3);
        var temp1 = num1 < num2 ? num1 :num2;
        var disLevel = temp1 < num3 ? temp1 :num3;
        // warehouse.disLevel=(disLevel*10);
        warehouse.type7 = distance;
        if (distance > 1e3) distance = Math.round(distance / 1e3 * 10) / 10 + "km"; else distance = distance + "m";
        warehouse.type9 = distance;
        arr.push(warehouse);
    }
    arr.sort(compare("type7"));
    return arr;
}

function compare(propertyName) {
    return function(object1, object2) {
        var value1 = object1[propertyName];
        var value2 = object2[propertyName];
        if (value2 < value1) {
            return 1;
        } else if (value2 > value1) {
            return -1;
        } else {
            return 0;
        }
    };
}