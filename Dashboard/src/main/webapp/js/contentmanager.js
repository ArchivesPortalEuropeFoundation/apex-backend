function initContentManager() {
	$("#searchButton").click(function(event) {
		event.preventDefault();
		performNewSearch();
	});

}
function initSubpage(){
	$(".actions input").click(function(event) {
		event.preventDefault();
		performEadAction($(this).parent().find(".selectedAction").val(),$(this).parent().parent().find(".checkboxSave").val(), "0" );
	});
	$("#batchActionButton").click(function(event) {
		event.preventDefault();
		performBatchEadAction($(this).parent().find("#batchSelectedAction").val(),"0" );
	});
}
function performEadAction(action, id, type){
	$("#ead-results-container").html("<div class='icon_waiting'></div>");
	$.post("eadActions.action", { id: id, type: type,action: action }, function(data) {
		updateCurrentSearchResults();
	});
}
function performBatchEadAction(action, type ){
	$("#ead-results-container").html("<div class='icon_waiting'></div>");
	$.post("batchEadActions.action", { type: type,action: action }, function(data) {
		updateCurrentSearchResults();
	});
}
function performNewSearch() {
	$("#ead-results-container").html("<div class='icon_waiting'></div>");
	$.post("contentmanagerResults.action", $("#newSearchForm").serialize(), function(data) {
		$("#ead-results-container").html(data);
		initSubpage();
		document.getElementById("ead-results-container").scrollIntoView(true);
		
	});
}
function updateCurrentSearchResults(){
	var formData = $("#updateCurrentSearch").serialize();
	$("#ead-results-container").html("<div class='icon_waiting'></div>");
	$.post("contentmanagerResults.action", formData, function(data) {
		$("#ead-results-container").html(data);
		initSubpage();
	});
}
function changeOrder(fieldValue, fieldSorting) {
	$("#updateCurrentSearch_orderByField").attr("value", fieldValue);
	$("#updateCurrentSearch_orderByAscending").attr("value", fieldSorting);
	updateCurrentSearchResults();
}

function updatePageNumber(url) {
	var pageNumber = url.split("=")[1];
	$("#updateCurrentSearch_pageNumber").attr("value", pageNumber);
	updateCurrentSearchResults();
}
function initResultsHandlers(){
	$("#updateCurrentSearch_resultPerPage").change(function(event) {
		$("#updateCurrentSearch_pageNumber").attr("value","1");
		updateCurrentSearchResults();
	});
}

