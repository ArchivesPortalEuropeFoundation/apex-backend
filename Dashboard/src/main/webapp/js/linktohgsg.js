function initPage(){
	$('#selectHgSg').change(function() {
		$.get("displayLinkToHgSgAjax.action", {
			ecId : $("#selectHgSg").val(), batchItems : $("#batchItems").val()
		}, function(data) {
			$("#dynamicContent").html(data);
			$("#ead-results").html($("#dynamicContent #new-ead-results").html());
			$("#selectClevelTd").html($("#dynamicContent #selectedPart").html());
			$("#dynamicContent").empty();
			
		});
	});
}