var globalRefresh_interval, globalIndex, globalRefresh;

function initContentManager(xmlTypeId) {
	selectSelected(xmlTypeId);
    clearFilesFromSession();
	initSearchOptions();
	initSubpage();
}

function selectSelected(xmlTypeId){
	$('#newSearchForm_xmlTypeId' + xmlTypeId).attr('checked', true);
}

function initSearchOptions() {
	$(".typeRadio").click(function(event) {
		 hideOrShowSelectAllFAsWindow();
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
				performContentAction(
						$(this).parent().find(".selectedAction").val(), $(this)
								.parent().parent().find(".checkboxSave").val(),
						$("#updateCurrentSearch_xmlTypeId").val());
			});
	$("#batchActionButton").click(function(event) {
		event.preventDefault();
		performBatchContentAction($("#updateCurrentSearch_xmlTypeId").val());
	});
	
	//floating box
	$("#clearAll").bind("click", function(value) {
		clearFilesFromSession();
		select_none();
	});
	//floating box
	$("#selectAllFiles").bind("click", function(value) {
		select_all();
	});
	//----------------------------------------------------
	
	$(".checkboxSave").bind("click", function() {
		addOneFile($(this).val());
	});
	//listview
	$("#selectAll").bind("click", function() {
		var ids = new Array();
		$("input:checkbox[name=check]").each(function() {
			if (!$(this).is(":checked")) {
				$(this).attr('checked', 'checked');
				ids.push(this.value);
			}
		});
		addFewFiles(ids);
	});
	//listview
	$("#selectNone").bind("click", function() {
		var ids = new Array();
		$("input:checkbox[name=check]").each(function() {
			if ($(this).is(":checked")) {
				$(this).removeAttr('checked');
				ids.push(this.value);
			}
		});
		addFewFiles(ids);
	});
	//----------------------------------------------------------

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

function performContentAction(action, id, type) {

	//If the select option refresh equals to refresh, then, set it to not refresh, perform action and later set it again to refresh
	var originalStatus=globalIndex;
	if(globalIndex!=0){
		reloadRefresh(false);
	}


	var actionSplitted = action.split("|");
	var windowType = actionSplitted[0];
	var actionOrUrl = actionSplitted[1];
	if ("action" == windowType) {
		var updateForm = getUpdateCurrentSearchResultsForm();
		var actionUrl;
                if (type == "2"){
                    actionUrl = "eacCpfActions.action";
                } else {
                    actionUrl = "eadActions.action";
                }
		$("#ead-results-container").html("<div class='icon_waiting'></div>");
		$.post(actionUrl, {
			id : id,
			xmlTypeId : type,
			action : actionOrUrl
		}, function(data) {

			updateCurrentSearchResults(updateForm);

			if (originalStatus==1){
				reloadRefresh(true);
			}

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
	count();
}
function performBatchContentAction(type) {
	//If the select option refresh equals to refresh, then, set it to not refresh, perform action and later set it again to refresh
	var originalStatus=globalIndex;
	if(globalIndex!=0){
		reloadRefresh(false);
	}

	var formData = $("#batchActionsForm").serializeArray();
	var updateForm = getUpdateCurrentSearchResultsForm();
	var actionUrl;
        if (type == "2"){
            actionUrl = "batchEacCpfActions.action";
        } else {
            actionUrl = "batchEadActions.action";
        }
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
		$.post(actionUrl, formData, function(data) {
			if(data.indexOf("error")>-1){
				var message = data;
				if(message.length>"error".length){
					message = data.substring("error".length+1);
				}
				alert(message);
			}

			updateCurrentSearchResults(updateForm);
			if (originalStatus==1){
				reloadRefresh(true);
			}
			
			clearFilesFromSession();
		});
		count();
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
			refreshIntervalFunc(globalIndex, false);
		}
	});
	
	clearFilesFromSession();
	count();
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
	clearFilesFromSession();
	count();
    if(($('#newSearchForm_xmlTypeId0').is(':checked')) || ($('#newSearchForm_xmlTypeId2').is(':checked'))) {
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
function refreshIntervalFunc(lastIndex, execute) {
	var list = $("select#refreshInterval");
	var index=lastIndex;// keeps the last index
	globalIndex = index;
	var valueOption=globalRefresh_interval;
	if (index!=0){
		if (execute) {
			globalRefresh = true;
		}
		var action = "reloadBottom("+(index)+","+globalRefresh_interval+");";
		if($.isNumeric(lastIndex) && index===lastIndex){
			setTimeout(action,globalRefresh_interval*1000);
		}
	} else {
		globalRefresh = false;
	}
	count();
}
function reloadBottom(index,seconds) {
	var selectedIndex = $("select#refreshInterval").prop("selectedIndex");
	if(index==selectedIndex && globalRefresh){ //
		updateCurrentSearchResults();
	} else  if (globalIndex != 0) {
		globalRefresh = true;
	}
}


/***
 * This function sets refresh intervals to refresh or non refresh depending of the value
 * @param value If true the select option will refresh, if false will not.
 */
function reloadRefresh(value){
	if (value){
		//set to refresh
		$("select#refreshInterval").prop('selectedIndex', '1');
		refreshIntervalFunc(1, false);
	}
	else{
		//set to do not refresh
		refreshIntervalFunc(0, false);
	}
}

function drawColumns(fa, hg, sg, eac){
	
	var width = document.getElementById("ead-results-header").offsetWidth;
	var small = Math.round(width*0.05);
	var id = Math.round(width*0.10);
	var title = Math.round(width*0.30);
	var actions = Math.round(width*0.15);
	//eaccpf
	var name = Math.round(width*0.25);
	var relations = Math.round(width*0.10);

	$("#thLabel").width(small);
	$("#thLabel" ).css( "maxWidth", (small) + "px" );
	$("table [id^='tdLabel_']").each(function() {
		$(this).width(small);
		$(this).css( "maxWidth", (small) + "px" );
	});
	
	$("#thId").width(id);
	$("#thId" ).css( "maxWidth", (id) + "px" );
	$("table [id^='tdId_']").each(function() {
		$(this).width(id);
		$(this).css( "maxWidth", (id) + "px" );
	});
	
	$("#thDate").width(small);
	$("#thDate" ).css( "maxWidth", (small) + "px" );
	$("table [id^='tdDate_']").each(function() {
		$(this).width(small);
		$(this).css( "maxWidth", (small) + "px" );
	});
	
	$("#thConverted").width(small);
	$("#thConverted" ).css( "maxWidth", (small) + "px" );
	$("table [id^='tdConverted_']").each(function() {
		$(this).width(small);
		$(this).css( "maxWidth", (small) + "px" );
	});
	
	$("#thValidated").width(small);
	$("#thValidated" ).css( "maxWidth", (small) + "px" );
	$("table [id^='tdValidated_']").each(function() {
		$(this).width(small);
		$(this).css( "maxWidth", (small) + "px" );
	});
	
	$("#thPublished").width(small);
	$("#thPublished" ).css( "maxWidth", (small) + "px" );
	$("table [id^='tdPublished_']").each(function() {
		$(this).width(small);
		$(this).css( "maxWidth", (small) + "px" );
	});
	
	$("#thQueue").width(small);
	$("#thQueue" ).css( "maxWidth", (small) + "px" );
	$("table [id^='tdQueue_']").each(function() {
		$(this).width(small);
		$(this).css( "maxWidth", (small) + "px" );
	});
	
	$("#thActions").width(actions);
	$("#thActions" ).css( "maxWidth", (actions) + "px" );
	$("table [id^='tdActions_']").each(function() {
		$(this).width(actions);
		$(this).css( "maxWidth", (actions) + "px" );
	});
	
	//	FA				HG/SG			EACCPF
	//	Title			Title			Name
	//	Holdings		Dynamic			Relations
	//	Edm				Lynked		
	//	Europeana
	
	if(hg || sg){
		$("#thTitle").width(title);
		$("#thTitle" ).css( "maxWidth", (title) + "px" );
		$("table [id^='tdTitle_']").each(function() {
			$(this).width(title);
			$(this).css( "maxWidth", (title) + "px" );
		});

		$("#thDynamic").width(small*1.5);
		$("#thDynamic" ).css( "maxWidth", (small*1.5) + "px" );
		$("table [id^='tdDynamic_']").each(function() {
			$(this).width(small*1.5);
			$(this).css( "maxWidth", (small*1.5) + "px" );
		});
		
		$("#thLynked").width(small*1.5);
		$("#thLynked" ).css( "maxWidth", (small*1.5) + "px" );
		$("table [id^='tdLynked_']").each(function() {
			$(this).width(small*1.5);
			$(this).css( "maxWidth", (small*1.5) + "px" );
		});
	}else if (fa){	
		$("#thTitle").width(title);
		$("#thTitle" ).css( "maxWidth", (title) + "px" );
		$("table [id^='tdTitle_']").each(function() {
			$(this).width(title);
			$(this).css( "maxWidth", (title) + "px" );
		});

		$("#thHoldings").width(small);
		$("#thHoldings" ).css( "maxWidth", (small) + "px" );
		$("table [id^='tdHoldings_']").each(function() {
			$(this).width(small);
			$(this).css( "maxWidth", (small) + "px" );
		});
		
		$("#thEdm").width(small);
		$("#thEdm" ).css( "maxWidth", (small) + "px" );
		$("table [id^='tdEdm_']").each(function() {
			$(this).width(small);
			$(this).css( "maxWidth", (small) + "px" );
		});
		
		$("#thEuropeana").width(small);
		$("#thEuropeana" ).css( "maxWidth", (small) + "px" );
		$("table [id^='tdEuropeana_']").each(function() {
			$(this).width(small);
			$(this).css( "maxWidth", (small) + "px" );
		});
	}
	else{
		//eaccpf
		$("#thName").width(name);
		$("#thName" ).css( "maxWidth", (name) + "px" );
		$("table [id^='tdName_']").each(function() {
			$(this).width(name);
			$(this).css( "maxWidth", (name) + "px" );
		});

		$("#thRelations").width(relations);
		$("#thRelations" ).css( "maxWidth", (relations) + "px" );
		$("table [id^='tdRelations_']").each(function() {
			$(this).width(relations);
			$(this).css( "maxWidth", (relations) + "px" );
		});
		
		$("#thEdm").width(small);
		$("#thEdm" ).css( "maxWidth", (small) + "px" );
		$("table [id^='tdEdm_']").each(function() {
			$(this).width(small);
			$(this).css( "maxWidth", (small) + "px" );
		});
		
		$("#thEuropeana").width(small);
		$("#thEuropeana" ).css( "maxWidth", (small) + "px" );
		$("table [id^='tdEuropeana_']").each(function() {
			$(this).width(small);
			$(this).css( "maxWidth", (small) + "px" );
		});
	}

	
}