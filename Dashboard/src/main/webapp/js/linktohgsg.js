function initPage(){
	$('#selectHgSg').change(function() {
		var id = $("#displayLinkToHgSg_id").val();
		var params = {ecId : $("#selectHgSg").val(), batchItems : $("#batchItems").val()};
		if (id != undefined && id.lenght != 0){
			params['id'] = id;
		}
		$.get("displayLinkToHgSgAjax.action", params, function(data) {
			$("#dynamicContent").html(data);
			$("#ead-results").html($("#dynamicContent #new-ead-results").html());
			$("#selectClevelTd").html($("#dynamicContent #selectedPart").html());
			$("#dynamicContent").empty();
			
		});
	});
}