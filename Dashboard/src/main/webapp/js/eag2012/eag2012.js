function hideAndShow(idPrefix,shown){
	$("div[id^='"+idPrefix+"']").each(function(){
		$(this).hide();
	});
	$("div[id='"+shown+"']").show();
}