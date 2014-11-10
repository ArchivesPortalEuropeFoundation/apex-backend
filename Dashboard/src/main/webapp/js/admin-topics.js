function initAdminTopics(){
	if($("table").length>0){
		$("table").tablesorter({
			debug: false,
			theme : 'tablesorter' 
		});
		$("table").tablesorterPager({
			container: $("#pager")
		});
	}
	fixPagerTop();
	setTimeout("$('#actionMessages').fadeOut('fast');",5000);
}

function fixPagerTop(){
	$("#pager").css("margin-top","5px");
	$(".pager").css("width",$("table").css("width"));
}