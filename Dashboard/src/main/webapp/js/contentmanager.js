function initContentManager() {
	$("#searchButton").click(function(event) {
		event.preventDefault();
		performNewSearch();
	});
}
function performNewSearch() {
	$.post("contentmanagerResults.action", $("#newSearchForm").serialize(), function(data) {
		$("#ead-results-container").html(data);
		document.getElementById("ead-results-container").scrollIntoView(true);
	});
}
function updateCurrentSearchResults(){
	$.post("contentmanagerResults.action", $("#updateCurrentSearch").serialize(), function(data) {
		$("#ead-results-container").html(data);
		document.getElementById("ead-results-container").scrollIntoView(true);
	});
}
function updateSorting(fieldValue, fieldSorting) {
	$("#updateCurrentSearch_order").attr("value", fieldValue);
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