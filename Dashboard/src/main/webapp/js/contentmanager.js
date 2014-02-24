var globalRefresh_interval, globalIndex;

function initContentManager() {
    clearFAsFromSession();
    hideOrShowSelectAllFAsWindow();
	initSearchOptions();
	initSubpage();

}
function initSearchOptions() {
	$(".typeRadio").click(function(event) {
		if ($(this).val() == 0){
			$(".findingAidOptions").removeClass("hidden");
		}else {
			$(".findingAidOptions").addClass("hidden");
		}
		performNewSearch();
	});
	$("#searchTerms").focus();
	$("#searchTerms").keypress(function(event) {
		if (event.keyCode == 13) {
			performNewSearch();
		}
	});
	$("#searchButton").click(function(event) {
		event.preventDefault();
		performNewSearch();
	});
}
function initSubpage() {
	$(".actions input").click(
			function(event) {
				event.preventDefault();
				performEadAction(
						$(this).parent().find(".selectedAction").val(), $(this)
								.parent().parent().find(".checkboxSave").val(),
						$("#updateCurrentSearch_xmlTypeId").val());
			});
	$("#batchActionButton").click(function(event) {
		event.preventDefault();
		performBatchEadAction();
	});
	$("#clearAll").bind("click", function(value) {
		clearFAsFromSession();
	});
	$("#selectAllFAs").bind("click", function(value) {
		addAllFAsInSession();
	});
	$(".checkboxSave").bind("click", function() {
		addOneFA($(this).val());
	});
	$("#selectAll").bind("click", function() {
		var ids = new Array();
		$("input:checkbox[name=check]").each(function() {
			if (!$(this).is(":checked")) {
				$(this).attr('checked', 'checked');
				ids.push(this.value);
			}
		});
		addFewFAs(ids);
	});
	$("#selectNone").bind("click", function() {
		var ids = new Array();
		$("input:checkbox[name=check]").each(function() {
			if ($(this).is(":checked")) {
				$(this).removeAttr('checked');
				ids.push(this.value);
			}
		});
		addFewFAs(ids);
	});
	count();
}

function select_all()
{
	$("#selectAll").click();
}
function select_none()
{
	$("#selectNone").click();
}
function enable_features(){
}

function performEadAction(action, id, type) {
	var actionSplitted = action.split("|");
	var windowType = actionSplitted[0];
	var actionOrUrl = actionSplitted[1];
	if ("action" == windowType) {
		var updateForm = getUpdateCurrentSearchResultsForm();
		var actionUrl = "eadActions.action";
		$("#ead-results-container").html("<div class='icon_waiting'></div>");
		$.post(actionUrl, {
			id : id,
			xmlTypeId : type,
			action : actionOrUrl
		}, function(data) {
			updateCurrentSearchResults(updateForm);
		});
	} else {
		var parameters = "id=" + id + "&xmlTypeId=" + type;
		var actionUrl = actionOrUrl;
		if (actionUrl.indexOf("?") > 0) {
			actionUrl = actionUrl + "&" + parameters;
		} else {
			actionUrl = actionUrl + "?" + parameters;
		}
		if ("colorbox" == windowType) {
			jQuery().colorbox({
				width : "80%",
				inline : false,
				href : actionUrl
			});

		} else {
			window.open(actionUrl, windowType);
		}
	}

}
function performBatchEadAction() {
	var formData = $("#batchActionsForm").serializeArray();
	var updateForm = getUpdateCurrentSearchResultsForm();
	var json = {};
	for (i in formData) {
		json[formData[i].name] = formData[i].value;
	}
	$("#ead-results-container").html("<div class='icon_waiting'></div>");
	if (json['action'] == "displayEseConvert") {
		window.open(
				json['action'] + ".action?batchItems=" + json['batchItems'],
				"_self");
	} else if (json['action'] == "displayLinkToHgSg") {
		window.open(
				json['action'] + ".action?batchItems=" + json['batchItems'],
				"_self");
	} else {
		$.post("batchEadActions.action", formData, function(data) {
			if(data.indexOf("error")>-1){
				var message = data;
				if(message.length>"error".length){
					message = data.substring("error".length+1);
				}
				alert(message);
			}
			updateCurrentSearchResults(updateForm);
		});
	}
}
function performNewSearch() {
	$("#ead-results-container").html("<div class='icon_waiting'></div>");
	$.post("updateContentmanager.action", $("#newSearchForm").serialize(),
			function(data) {
				$("#ead-results-container").html(data);
				initSubpage();
				document.getElementById("ead-results-container")
						.scrollIntoView(true);

			});
}
function getUpdateCurrentSearchResultsForm() {
	return $("#updateCurrentSearch").serialize();
}

function updateCurrentSearchResults(formData) {
    hideOrShowSelectAllFAsWindow();
    
	if (formData == null) {
		formData = getUpdateCurrentSearchResultsForm();
	}

	//check reload part
	var index = $("select#refreshInterval").prop("selectedIndex");
	var seconds = globalRefresh_interval;
	$("#ead-results-container").html("<div class='icon_waiting'></div>");
	$.post("updateContentmanager.action", formData, function(data) {
		$("#ead-results-container").html(data);
		initSubpage();
		$("select#refreshInterval option").eq(globalIndex).prop("selected",true);
		if(index!=0){ //reloads if different
			refreshIntervalFunc(globalIndex);
		}
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

/***
 * 
 * @param refresh_interval defined in SecurityContext.java, this var stores in the session the timeout refresh, 5 secs by default.
 */
function initResultsHandlers(refresh_interval) { 
	globalRefresh_interval = refresh_interval;
	$("#updateCurrentSearch_resultPerPage").change(function(event) {
		$("#updateCurrentSearch_pageNumber").attr("value", "1");
		//do not update wrong the timeout, if there is an active refresh, do not refresh again and again.
		if (globalIndex!=1){
			updateCurrentSearchResults();
		}
	});
    createColorboxForConversionOptions();
}

function hideOrShowSelectAllFAsWindow() {
    if($('#newSearchForm_xmlTypeId0').is(':checked')) {
        $("#listFiles").removeClass("hidden");
    } else {
        $("#listFiles").addClass("hidden");
    }
}
function createColorboxForConversionOptions() {
    $("#conversionOpts").colorbox(
        {
            width:"80%",
            height:"200px",
            inline:true,
            overlayClose:false,
            onLoad:function(){ checkCurrentOpts(); },
            href: "#conversionOptsDiv"
        }
    );
}
function checkCurrentOpts() {
    var loadUrl = "checkCurrentConversionOptions.action";
    $.post(loadUrl, null, function(databack){
        if(databack){
            if(databack.error){
                console.log("ERROR");
                $("input:radio[name=roleType]").val(["UNSPECIFIED"]);
                $("input:checkbox[name=useExistingRole]").val(["useExistingRole"]);
            } else {
                $("input:radio[name=roleType]").val([databack.optsDefault]);
                if(databack.optsUseExisting == 'true') {
                    $("input:checkbox[name=useExistingRole]").val(["useExistingRole"]);
                } else {
                    $("input:checkbox[name=useExistingRole]").removeAttr("checked");
                }
            }
        }
    }, 'json');
    prepareSubmitAndCancelBtns();
}
function prepareSubmitAndCancelBtns() {
    $("#submitBtnRoleType").unbind();
    $("#cancelBtnRoleType").unbind();
    $("#submitBtnRoleType").bind("click", function(){
        var loadUrl = "saveConversionOptions.action";
        var data = {optsUseExisting: $("#useExistingRole").is(":checked"), optsDefault: $("input:radio[name=roleType]:checked").val()};
        $.post(loadUrl, data, function(databack){
            if(databack){
                if(databack.error){
                    console.log("ERROR");
                } else {
                    $.fn.colorbox.close();
                }
            }
        }, 'json');
    });
    $("#cancelBtnRoleType").bind("click", function(){
        $.fn.colorbox.close();
    });
}
function refreshIntervalFunc(lastIndex) {
	var list = $("select#refreshInterval");
	var index=lastIndex;// keeps the last index
	globalIndex = index;
	var valueOption=globalRefresh_interval;
	if (index!=0){
		var action = "reloadBottom("+(index)+","+globalRefresh_interval+");";
		if($.isNumeric(lastIndex) && index===lastIndex){
			setTimeout(action,valueOption*1000);
		}
	}
}
function reloadBottom(index,seconds) {
	var selectedIndex = $("select#refreshInterval").prop("selectedIndex");
	if(index==selectedIndex){
		updateCurrentSearchResults();
	}
}