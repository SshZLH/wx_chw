//样式初始化  适应性
(function (d, w) {
	var docEl = d.documentElement,resizeEvt = 'orientationchange' in w ? 'orientationchange' : 'resize',
	recalc = function () {
		var clientWidth = docEl.clientWidth;
		if (!clientWidth) return;
			docEl.style.fontSize = 100 * (clientWidth / 750) + 'px';
		};
		if (!d.addEventListener) return;
		w.addEventListener(resizeEvt, recalc, false);
		d.addEventListener('DOMContentLoaded', recalc, false);
})(document, window);

var globalVar = {
		baseUrl : 'http://test.wanhuchina.com/',
		supportUrl : 'http://terp.wanhuchina.com:9004/',
		appId : 'wx266d92b77ba29197'
	};
//初始化头部
function initHeader(containerID, title){
	var contentStr = '	<div class="titLeft"><img class="return" src="/web/whc/warehouse/img/return.png"/></div>\
		 	<div class="selCab">';
	contentStr += title +'</div><div class="phone" onclick="callCSPhone();">	<img class="telPhone" src="/web/whc/warehouse/img/phone_icon.png"/></div>';
	$("#" + containerID).html(contentStr);
}
//初始化客服电话
function initCSPhone(containerID){
	var contentStr = '	<div class="telDiv_top"> <p class="p1">拨打客服电话</p>\
		<p class="p2">400-0027-287</p>	</div>\
		<div class="telDiv_bottom">\
 			<div class="left">取消</div>\
 			<div class="right"><a href="tel:4000027287"><font color="#00aaee">拨打</font></a></div>\
 		</div>';
	$("#" + containerID).html(contentStr);
}
function getUrlParameter(name){
	var url = location.search; //获取url中"?"符后的字串
    var val= "";
    if (url.indexOf("?") != -1) { 
	      var str = url.substr(1);
	      strs = str.split("&");
	      for(var i=0; i< strs.length ;i++)
	      {
	    	  var strlist=strs[i].split("=");
	    	  if (strlist[0]==name)
	    		  val = strlist[1];
	      }
	    //  alert(name + ":"+val);
   	}
    return val;
}


/*点击打电话按钮打电话*/
function callCSPhone(){
	$(".transparent").show();
	$(".telDiv").show();
	$(".telDiv .left").on('click',function(){
		$(".transparent").hide();
		$(".telDiv").hide();
	})
};
/*淡入淡出的提示效果*/
function createStyle(obj,txt,_width,_height,_lineHeight){
	$("<div class='altTxt'>"+txt+"</div>").appendTo(obj);
	$(".altTxt").css({
		"width":_width,
		"height":_height,
		"line-height":_lineHeight
		});
	setTimeout('$(".altTxt").fadeIn("slow")',30);
	setTimeout('$(".altTxt").fadeOut("slow")',1200);
}
