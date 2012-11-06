function initContentManager() {
//	$("#searchButton").click(function(event) {
//		event.preventDefault();
//		performNewSearch();
//	});
	initSubpage();
	
}
function initSubpage(){
	$(".actions input").click(function(event) {
		event.preventDefault();
		performEadAction($(this).parent().find(".selectedAction").val(),$(this).parent().parent().find(".checkboxSave").val(), $("#updateCurrentSearch_type").val() );
	});
	$("#batchActionButton").click(function(event) {
		event.preventDefault();
		performBatchEadAction();
	});
    $("#clearAll").bind("click", function(value){
        clearFAsFromSession();
    });
    $("#selectAllFAs").bind("click", function(value){
        addAllFAsInSession();
    });
    $(".checkboxSave").bind("click", function(){
        addOneFA($(this).val());
    });
    $("#selectAll").bind("click", function(){
        var ids = new Array();
        $("input:checkbox[name=check]").each(function(){
            if(!$(this).is(":checked")) {
                $(this).attr('checked','checked');
                ids.push(this.value);
            }
        });
        addFewFAs(ids);
    });
    $("#selectNone").bind("click", function(){
        var ids = new Array();
        $("input:checkbox[name=check]").each(function(){
            if($(this).is(":checked")) {
                $(this).removeAttr('checked');
                ids.push(this.value);
            }
        });
        addFewFAs(ids);
    });
    count();
}
function performEadAction(action, id, type){
	$("#ead-results-container").html("<div class='icon_waiting'></div>");
	$.post("eadActions.action", { id: id, type: type,action: action }, function(data) {
		updateCurrentSearchResults();
	});
}
function performBatchEadAction(){
	var formData = $("#batchActionsForm").serialize();
	$("#ead-results-container").html("<div class='icon_waiting'></div>");
	$.post("batchEadActions.action",formData, function(data) {
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

