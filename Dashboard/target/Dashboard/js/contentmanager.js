function initContentManager() {
    clearFAsFromSession();
    hideOrShowSelectAllFAsWindow();
	initType();
	initSubpage();
}
function initType() {
	$(".typeRadio").click(function(event) {
		if ($(this).val() == 0){
			$(".findingAidOptions").removeClass("hidden");
		}else {
			$(".findingAidOptions").addClass("hidden");
		}
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
	} else {
		$.post("batchEadActions.action", formData, function(data) {
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
	$("#ead-results-container").html("<div class='icon_waiting'></div>");
	$.post("updateContentmanager.action", formData, function(data) {
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
function initResultsHandlers() {
	$("#updateCurrentSearch_resultPerPage").change(function(event) {
		$("#updateCurrentSearch_pageNumber").attr("value", "1");
		updateCurrentSearchResults();
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