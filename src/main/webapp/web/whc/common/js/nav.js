function navList(){
	/*导航选项，点击变颜色*/
	var timestamp=new Date().getTime();
		$(".list1").on('click',function(){
		$(".list1_top img").attr('src','img/save_blue_icon .png');
		$(".list1_bottom").css({"color":"#00aaee"});
		$(".list2_top img").attr('src','img/take_black_icon.png');
		$(".list2_bottom").css({"color":"#444444"})
		$(".list3_top img").attr('src','img/warehouse_black_icon.png');
		$(".list3_bottom").css({"color":"#444444"});
		window.location.href="/web/whc/warehouse/storeList.html?openId="+openId+"&wahoType=11&t="+timestamp;
	});
	$(".list2").on('click',function(){
		$(".list1_top img").attr('src','img/save_black_icon.png');
		$(".list1_bottom").css({"color":"#444444"});
		$(".list3_top img").attr('src','img/warehouse_black_icon.png');
		$(".list3_bottom").css({"color":"#444444"});
		$(".list2_top img").attr('src','img/take_blue_icon.png');
		$(".list2_bottom").css({"color":"#00aaee"});
		window.location.href="/web/whc/member/center.html?openId="+openId+"&t="+timestamp;
	});
	$(".list3").on('click',function(){
		$(".list1_top img").attr('src','img/save_black_icon.png');
		$(".list1_bottom").css({"color":"#444444"});
		$(".list2_top img").attr('src','img/take_black_icon.png');
		$(".list2_bottom").css({"color":"#444444"});
		$(".list3_top img").attr('src','img/warehouse_blue_icon.png');
		$(".list3_bottom").css({"color":"#00aaee"});
		window.location.href="/web/whc/warehouse/warehouseList.jsp?openId="+openId+"&wahoType=0&t="+timestamp;
	});
}