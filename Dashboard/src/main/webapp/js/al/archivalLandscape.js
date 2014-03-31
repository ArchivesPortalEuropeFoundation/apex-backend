function checkAndUpload(label){
	if($("input:file#httpFile").val()){
		if($("span#nothingSelected").length>0){
			$("span#nothingSelected").parent().remove(); //div
		}
		$("form#uploadALForm").submit();
	}else{
		if(!($("span#nothingSelected").length>0)){
			$("input:file#httpFile").parent().before("<div><span id=\"nothingSelected\">"+label+"</span></div>");
		}
		$("input:file#httpFile").change(function (){
			var val = $("input:file#httpFile").val();
			if(!val || val.length==0){
				if(!($("span#nothingSelected").length>0)){
					$("input:file#httpFile").parent().before("<div><span id=\"nothingSelected\">"+label+"</span></div>");
				}
			}else{
				$("span#nothingSelected").parent().remove();
			}
			
	    });
	}
}

function initReport(){
	if ($("div#noChangesDiv").attr("id") == undefined) {
		showComparableIdentifiers();
		$('#contentReport').hide();

		//buttons triggers
		// Trigger for button that enable the edition of identifiers.
		$("div#buttonChangeIdentifiers").click(function(){
			if ($('div#contentReport').is(':hidden')) {
				$('div#contentReport').show();
			}
			hideComparableIdentifiers();
			$('html, body').stop().animate({
		        'scrollTop': $("div#divDescription").offset().top + 30
		    }, 900, 'swing', function () {
		    	//logAction("scroll moved to: ", $("div#divDescription").offset().top);
		    });
		});

		// Trigger for button that recheck the changes in the identifies.
		$("div#buttonRecheckIdentifiers").click(function() {
			var context = $("input#contextPath").val();
			$("#updatesPartForm").attr("action", context + "/ALRecheckIdentifiers.action");
			$("#updatesPartForm").submit();
		});

		// Trigger for button that cancel the edition process.
		$("div#buttonCancelEdition").click(function() {
			$("#cancelEditionForm").submit();
		});

		// Trigger for minimize/maximize details of changes section.
		$('div#divDescription').click(function(){
			if (!$('img#imgExpandDetails').is(':hidden')) {
				if($('div#contentReport').is(':hidden')){
					$('div#contentReport').show('slow');
					$('img#imgExpandDetails').attr("src","images/expand/menos.gif");
					$('html, body').stop().animate({
				        'scrollTop': $("div#divDescription").offset().top
				    }, 900, 'swing', function () {
				    	//logAction("scroll moved to: ", $("div#divDescription").offset().top);
				    });
				}else{
					$('div#contentReport').hide('slow');
					$('img#imgExpandDetails').attr("src","images/expand/mas.gif");
					$('html, body').stop().animate({
				        'scrollTop': $("div#bodyt").offset().top
				    }, 900, 'swing', function () {
				    	//logAction("scroll moved to: ", $("div#bodyt").offset().top);
				    });
				}
			}
		});

		// Add method when element change.
		addOnchangeInput();
	} else {
		$("div#divChangeIdentifiers").hide();
	}

	// Trigger for button that continue the upload process.
	$("div#buttonContinueUpload").click(function() {
		$("#continueUploadForm").submit();
	});

	// Trigger for button that cancel the upload process.
	$("div#buttonCancelOverwrite").click(function() {
		$("#cancelOverwriteForm").submit();
	});
}

function showComparableIdentifiers(){
	// Same name section.
	$("div#institutionsWithSameNameNewDiv").removeClass("middleDiv");
	$("div#institutionsWithSameNameNewDiv").addClass("fullDiv");
	$("label[for^='newSameNameInstitution[']").each(function(){
		$(this).parent().removeClass("fullDiv");
		$(this).parent().addClass("middleDiv");
	});
	$("input[id^='newSameNameInstitution[']").each(function(){
		$(this).attr("readonly");
		$(this).attr("onfocus", "this.blur()");
		$(this).addClass("readOnlyInput");
		$(this).parent().removeClass("fullDiv");
		$(this).parent().addClass("middleDiv");
	});
	$("div#institutionsWithSameNameOldDiv").hide();

	// Same identifier section.
	$("div#institutionsWithSameIdNewDiv").removeClass("middleDiv");
	$("div#institutionsWithSameIdNewDiv").addClass("fullDiv");
	$("label[for^='newSameIdentifierInstitution[']").each(function(){
		$(this).parent().removeClass("fullDiv");
		$(this).parent().addClass("middleDiv");
	});
	$("input[id^='newSameIdentifierInstitution[']").each(function(){
		$(this).attr("readonly");
		$(this).attr("onfocus", "this.blur()");
		$(this).addClass("readOnlyInput");
		$(this).parent().removeClass("fullDiv");
		$(this).parent().addClass("middleDiv");
	});
	$("div#institutionsWithSameIdOldDiv").hide();

	// Empty identifier section.
	$("div#institutionsWithEmptyIdNewDiv").removeClass("middleDiv");
	$("div#institutionsWithEmptyIdNewDiv").addClass("fullDiv");
	$("label[for^='newEmptyIdentifierInstitution[']").each(function(){
		$(this).parent().removeClass("fullDiv");
		$(this).parent().addClass("middleDiv");
	});
	$("input[id^='newEmptyIdentifierInstitution[']").each(function(){
		$(this).attr("readonly");
		$(this).attr("onfocus", "this.blur()");
		$(this).addClass("readOnlyInput");
		$(this).parent().removeClass("fullDiv");
		$(this).parent().addClass("middleDiv");
	});
	$("div#institutionsWithEmptyIdOldDiv").hide();

	showRestDivs();
}

function hideComparableIdentifiers(){
	// Same name section.
	$("div#institutionsWithSameNameNewDiv").removeClass("fullDiv");
	$("div#institutionsWithSameNameNewDiv").addClass("middleDiv");
	$("label[for^='newSameNameInstitution[']").each(function(){
		$(this).parent().removeClass("middleDiv");
		$(this).parent().addClass("fullDiv");
	});
	$("input[id^='newSameNameInstitution[']").each(function(){
		$(this).removeAttr("readonly");
		$(this).removeAttr("onfocus");
		$(this).removeClass("readOnlyInput");
		$(this).parent().removeClass("middleDiv");
		$(this).parent().addClass("fullDiv");
	});
	$("div#institutionsWithSameNameOldDiv").show();

	// Same identifier section.
	$("div#institutionsWithSameIdNewDiv").removeClass("fullDiv");
	$("div#institutionsWithSameIdNewDiv").addClass("middleDiv");
	$("label[for^='newSameIdentifierInstitution[']").each(function(){
		$(this).parent().removeClass("middleDiv");
		$(this).parent().addClass("fullDiv");
	});
	$("input[id^='newSameIdentifierInstitution[']").each(function(){
		$(this).removeAttr("readonly");
		$(this).removeAttr("onfocus");
		$(this).removeClass("readOnlyInput");
		$(this).parent().removeClass("middleDiv");
		$(this).parent().addClass("fullDiv");
	});
	$("div#institutionsWithSameIdOldDiv").show();

	// Empty identifier section.
	$("div#institutionsWithEmptyIdNewDiv").removeClass("fullDiv");
	$("div#institutionsWithEmptyIdNewDiv").addClass("middleDiv");
	$("label[for^='newEmptyIdentifierInstitution[']").each(function(){
		$(this).parent().removeClass("middleDiv");
		$(this).parent().addClass("fullDiv");
	});
	$("input[id^='newEmptyIdentifierInstitution[']").each(function(){
		$(this).removeAttr("readonly");
		$(this).removeAttr("onfocus");
		$(this).removeClass("readOnlyInput");
		$(this).parent().removeClass("middleDiv");
		$(this).parent().addClass("fullDiv");
	});
	$("div#institutionsWithEmptyIdOldDiv").show();

	hideRestDivs();
}

function hideRestDivs(){
	$("div#reportMessage").hide();
	$("div#insertsPart").hide();
	$("div#deletesPart").hide();
	$("div#alupdatesMessage").hide();
	$("div#updatedInstitutionsDiv div.alupdates").each(function(){
		$(this).hide();
	});
	$("div#divChangeIdentifiers").hide();
	$("div#divCancelOverwrite").hide();
	$("div#divCancelEdition").removeClass("hidden");
	$("div#divCancelEdition").show();

	// Remove possibility expand.
	$("img#imgExpandDetails").hide();
	$("div#divDescription").removeClass("alDivShowHide");
	$("div#divDescription").addClass("alDivSummary");
}

function showRestDivs(){
	$("div#insertsPart").show();
	$("div#deletesPart").show();
	$("div#alupdatesMessage").show();
	$("div#updatedInstitutionsDiv div.alupdates").each(function(){
		$(this).show();
	});
	$("div#divChangeIdentifiers").show();
	$("div#divCancelOverwrite").show();
	$("div#divCancelEdition").hide();
}

/**
 * Function to add action "onchange" to those elements that should be editable.
 */
function addOnchangeInput() {
	// Same name section.
	$("input[id^='newSameNameInstitution[']").each(function(){
		$(this).on('input', function() {
			$("div#divRecheckIdentifiers").removeClass("hidden");
			$("div#divRecheckIdentifiers").show();
			$("div#divCancelEdition").removeClass("hidden");
			$("div#divCancelEdition").show();
			$("div#divContinueUpload").hide();
		});
	});

	// Same identifier section.
	$("input[id^='newSameIdentifierInstitution[']").each(function(){
		$(this).on('input', function() {
			$("div#divRecheckIdentifiers").removeClass("hidden");
			$("div#divRecheckIdentifiers").show();
			$("div#divCancelEdition").removeClass("hidden");
			$("div#divCancelEdition").show();
			$("div#divContinueUpload").hide();
		});
	});

	// Empty identifier section.
	$("input[id^='newEmptyIdentifierInstitution[']").each(function(){
		$(this).on('input', function() {
			$("div#divRecheckIdentifiers").removeClass("hidden");
			$("div#divRecheckIdentifiers").show();
			$("div#divCancelEdition").removeClass("hidden");
			$("div#divCancelEdition").show();
			$("div#divContinueUpload").hide();
		});
	});
}