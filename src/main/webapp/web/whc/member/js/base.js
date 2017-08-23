(function(){

})();

$(function(){

	//mh_select 美化select窗体
	if($(".mh_select").length != 0){
		$(".mh_select").each(function(){
			
			var _par = this;
			
			$("select",_par).on("change",function(){				
				$(".mh_val",_par).html($(this).val());
			});
				
		});	
	}		
	
	//mh_time 美化time,date弹窗
	if($(".mh_time").length != 0){
		$(".mh_time").each(function(){
			
			var _par = this;
			
			$("input",_par).on("click",function(){				
				$(".mh_val",_par).hide();
				$("input",_par).css("opacity",1);
			});
				
		});	
	}	

});	