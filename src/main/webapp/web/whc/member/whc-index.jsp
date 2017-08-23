<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String openId = (String)request.getParameter("openId");
//String openId = "o7FePswSTzPeQhIvJlC1jCq-lW4M";
%>
<!doctype html>
<html>
<head>
	<title>万户皆有仓</title>
	<meta name="viewport" content="width=device-width,initial-scale=1.0,maximum-scale=1.0,user-scalable=no" />
	<meta charset="utf-8" />
	<link rel="stylesheet" type="text/css" href="js/index.css" />
	<script type="text/javascript" src="js/zepto.js"></script>
	<script type="text/javascript" src="js/swipeSlide.min.js"></script>
</head>
<body>

	<div style="position:fixed;top:0;left:0;z-index:999;">
		<img src="img/img1.jpg" class="full" />
		<a href="javascript:void(0)" class="menu"> <img src="img/icon1.png" class="full" /> </a>
	</div>
	<div class="slide" id="slide3">
	    <ul>
	        <li>
	            <a href="/web/whc/warehouse/warehouseList.jsp?openId=<%=openId %>">
	                <img src="img/img2_1.jpg" alt="" class="full">
	            </a>
	       </li>
	        <li>
	            <a href="/web/whc/warehouse/warehouseList.jsp?openId=<%=openId %>">
	                <img src="img/img2_1.jpg" alt="" class="full">
	            </a>
	        </li>
	        <li>
	            <a href="/web/whc/warehouse/warehouseList.jsp?openId=<%=openId %>">
	                <img src="img/img2_1.jpg" alt="" class="full">
	            </a>
	        </li>
	        <li>
	           <a href="/web/whc/warehouse/warehouseList.jsp?openId=<%=openId %>">
	                <img src="img/img2_1.jpg" alt="">
	            </a>
	        </li>
	    </ul>
	    <div class="dot">
	        <span></span>
	        <span></span>
	        <span></span>
	        <span></span>
	    </div> 
	</div>
	<a name="img3">
	<div class="box">
		<img src="img/img3.jpg" class="full" />
	</div>
	</a>
	<a name="img4">
	<div class="box">
		<img src="img/img4.jpg" class="full" />
	</div>
	</a>
	<a name="img5">
	<div class="box">
		<img src="img/img5.jpg" class="full" />
	</div>
	</a>
	<a name="img6">
	<div class="box">
		<img src="img/img6.jpg" class="full" />
	</div>
	</a>
	<a name="img7">
	<div class="box">
		<img src="img/img7.jpg" class="full" />
	</div>
	</a>
	<a name="img8">
	<div class="box">
		<img src="img/img8.jpg" class="full" />
	</div>
	</a>
	<a name="img9">
	<div class="box block">
		<a href="/web/whc/warehouse/warehouseList.jsp?openId=<%=openId %>" class="btn btn1"> <img src="img/btn.jpg" class="full" /> </a>
		<img src="img/img9.jpg" class="full" />
	</div>
	</a>
	<div class="box block">
		<a href="/web/whc/warehouse/warehouseList.jsp?openId=<%=openId %>" class="btn btn2"> <img src="img/btn.jpg" class="full" /> </a>
		<img src="img/img10.jpg" class="full" />
	</div>

	<div class="box block">
		<a href="/web/whc/warehouse/warehouseList.jsp?openId=<%=openId %>" class="btn btn3"> <img src="img/btn.jpg" class="full" /> </a>
		<img src="img/img11.jpg" class="full" />
	</div>

	<div class="box block">
		<a href="/web/whc/warehouse/warehouseList.jsp?openId=<%=openId %>" class="btn btn4"> <img src="img/btn.jpg" class="full" /> </a>
		<img src="img/img12.jpg" class="full" />
	</div>

	<!--<div class="box block">
		<a href="/app/whc/warehouse/warehouseList.jsp?openId=<%=openId %>" class="btn btn5"> <img src="images/index/btn.jpg" class="full" /> </a>
		<img src="images/index/img13.jpg" class="full" />
	</div>
	--><a name="img14">
	<div class="box block">
		<!--<a href="" class="btn btn6"> <img src="images/index/btn1.jpg" class="full" /> </a>
		--><img src="img/img16.jpg" class="full" />
	</div>
	</a>
	<div class="menus">
		<div class="shopselect" id="shopSelect">
			<div class="firstA shopInfo"><a href="/web/whc/warehouse/warehouseList.jsp?openId=<%=openId %>&wahoType=0">迷你自助仓</a></div>
		    <div class="shopInfo"><a href="/web/whc/warehouse/storeList.html?openId=<%=openId %>&wahoType=1">羊舍储物柜</a></div>
		</div>
		<a href="javascript:selectShop();" class="item2"> <img src="img/item2.jpg" class="full" /> </a>
		<a href="/web/whc/member/center.html?openId=<%=openId %>" class="item3"> <img src="img/item3.jpg" class="full" /> </a>
	    <a href="" class="item1"> <img src="img/item1.jpg" class="full" /> </a>
	</div>

	<a href="tel:4000027287" class="online"> <img src="img/online1.png" class="full" /> <span class="onlineClose"></span> </a>

	<div class="menuPop">
		<span class="arrow"> <img src="img/icon2.png" class="full" /> </span>
		<ul class="list">
			<li><a href="#img3">关于万户仓</a></li>
			<!--<li><a href="">免费评估</a></li>
			-->
			<li><a href="#img15">关于我们</a></li>
			<li><a href="#img4">个人用仓</a></li>
			<li><a href="#img9">仓储须知</a></li>
			<!--<li><a href="">门店位置</a></li>
			-->
			<li><a href="#img5">家庭用仓</a></li>
			<!--<li><a href="">常见问题</a></li>
			-->
		
			<li><a href="#img14">实景展示</a></li>
			<li><a href="#img6">企业用仓</a></li>
			<li><a href="#img8">租用流程</a></li>
			<!--<li><a href="">在线预定</a></li>
			-->
			<li><a href="#img7">选择万户仓</a></li>
		</ul>
	</div>
	<div class="page-mask"></div>
	<script type="text/javascript">
		function selectShop(){
			$("#shopSelect").toggle();
		}
		
		window.onload = function(){
			//$("#shopSelect").on('click',function(){return false;});

			//$(".menuPop").css("top",$(".menu").height()+$(".menu").offset().top+$(".arrow").height()+4)
			$(".menu").on("click",function(){
				$(".menuPop").toggle();
				$(".page-mask").toggle();
			});

			/*$(".page-mask").on("touchstart",function(){
				$(".menuPop").hide();
				$(this).hide();
			});*/

			$(".onlineClose").on("touchstart",function(){
				$(".online").hide();
				return false;
			});
			
			$('#slide3').swipeSlide({
		        continuousScroll:true,
		        speed : 6000,
		        transitionType : 'cubic-bezier(0.22, 0.69, 0.72, 0.88)',
		        callback : function(i){
		            $('.dot').children().eq(i).addClass('cur').siblings().removeClass('cur');
		        }
		    });
		}
		
	</script>

</body>
</html>