<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!-- warehouseList -->
<script type="text/x-tmpl" id="tmpl-warehouseList2">
{% 
	var arr = new Array();
	for(var k=0; k<o.length; k++) {
		
		var warehouse = o[k];
         var num1=parseFloat(o[k].disLevel1);
		var num2=parseFloat(o[k].disLevel2);
		var num3=parseFloat(o[k].disLevel3);
     var temp1 = num1<num2?num1:num2;
     var disLevel = temp1<num3?temp1:num3;
         warehouse.disLevel=(disLevel*10);
		arr.push(warehouse);
	}
	for(var i=0; i<arr.length; i++) {
%}
		<div class="block" onClick="openWarehouse('{%=arr[i].id%}','{%=arr[i].type%}');">
			<div class="box clearfix">
			<a class="img"> <img src="<%=supportUrl%>app/whc/uploadFile/import/{%=arr[i].image1%}" /> </a>
				<div class="r">
					<div class="hd clearfix">
						<a class="title">{%=arr[i].name%}</a>
						{% 
							if(arr[i].type3 > 0) {
						%}
								<em style="background:#ffbc01">小</em>
						{%
							}
						%}
						{% 
							if(arr[i].type4 > 0) {
						%}
								<em style="background:#6ada94">中</em>
						{%
							}
						%}
						{% 
							if(arr[i].type5 > 0) {
						%}
								<em style="background:#73d7f4">大</em>
						{%
							}
						%}
						{% 
							if(arr[i].type6 > 0) {
						%}
								<em style="background:#73d7f4">定制</em>
						{%
							}
						%}

						{% 
							if(arr[i].type2 > 0) {
						%}
								<em style="background:#f48c8d">红酒</em>
						{%
							}
						%}
						{% 
							if(arr[i].type7 > 0) {
						%}
								<em style="background:#f48c8d">智</em>
						{%
							}
						%}
							{% 
							if(arr[i].type8 > 0) {
						%}
								<em style="background:#f48c8d">即储</em>
						{%
							}
						%}
						
						
					</div>
					<div class="ft clearfix">
						<ul class="stars clearfix" style="line-height: 2.5;">
							<li> <img src="/web/whc/warehouse/img/star.png" /> </li>
							<li> <img src="/web/whc/warehouse/img/star.png" /> </li>
							<li> <img src="/web/whc/warehouse/img/star.png" /> </li>
							<li> <img src="/web/whc/warehouse/img/star.png" /> </li>
							<li> <img src="/web/whc/warehouse/img/star.png" /> </li>
						</ul>
						<p class="street">{%=arr[i].addr%}</p>
					</div>
					<div class="price">
						<em class="rmb">￥</em><span class="s1">{%=arr[i].price%}</span><span class="s2">起</br></span>
                       
                         {% 
							if(arr[i].type !='1') {
						%}
                        <span>月/m3</br></span>
						<div class="xianshi" style="width:80%">
							限时
						{%=arr[i].disLevel%}
							折</br>
						</div>
						{%
							}%}


					</div>
				</div>
			</div>
		</div>

{%	
	}
%}
</script>
<!-- warehouseCellList -->
<script type="text/x-tmpl" id="tmpl-warehouseCellList">
{%
	for(var i=0; i<o.length; i++) {
		var name = "";
		if(o[i].type == '1') {
			name = "冷冻仓"
		} else if(o[i].type == '2') {
			name = "红酒仓"
		} else if(o[i].type == '3') {
			name = "小型仓"
		} else if(o[i].type == '4') {
			name = "中型仓"
		} else if(o[i].type == '5') {
			name = "大型仓"
		} else if(o[i].type == '6') {
			name = "定制仓"
		}
%}
		<div class="block">
			<div class="box clearfix">
				<a href="" class="img"> <img src="IMG_3749.JPG" /> </a>
				<div class="r">
					<div class="hd clearfix">
						<a href="" class="title">{%=name%} ({%=o[i].warehouseName%} )</a>

					</div>
					<div class="md">
						<span class="price">￥{%=o[i].discountPrice%}</span> <span class="days">元/天 起</span>
					</div>
					<div class="ft clearfix">
						<ul class="stars clearfix">
							<li> <img src="/web/whc/warehouse/img/star.png" /> </li>
							<li> <img src="/web/whc/warehouse/img/star.png" /> </li>
							<li> <img src="/web/whc/warehouse/img/star.png" /> </li>
						</ul>
						<span class="at">{%=o[i].warehouseArea%}</span>
					</div>
				</div>
			</div>
		</div>

{%	
	}
%}
</script>


