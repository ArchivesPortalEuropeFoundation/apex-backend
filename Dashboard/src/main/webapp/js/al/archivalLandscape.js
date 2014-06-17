/**
 * Function to check if the user has selected a file.
 *
 * @param label Error message in case the user hasn't selected a file.
 */
function checkAndUpload(label) {
	if ($("input:file#httpFile").val()) {
		if ($("span#nothingSelected").length > 0) {
			$("span#nothingSelected").parent().remove(); //div
		}
		$("form#uploadALForm").submit();
	} else {
		if (!($("span#nothingSelected").length>0)) {
			$("input:file#httpFile").parent().before("<div><span id=\"nothingSelected\">" + label + "</span></div>");
		}
		$("input:file#httpFile").change(function() {
			var val = $("input:file#httpFile").val();
			if (!val || val.length == 0){
				if (!($("span#nothingSelected").length > 0)) {
					$("input:file#httpFile").parent().before("<div><span id=\"nothingSelected\">" + label + "</span></div>");
				}
			} else {
				$("span#nothingSelected").parent().remove();
			}
		});
	}
}

/**
 * Function to initialize the information Report.
 */
function initReport(selectMessage){
	// Hide elements.
	$("div#detailsDiv").hide();
	$("div#divOverwriteIds").hide();
	$("div#divKeepIds").hide();

	if ($("div#noChangesDiv").attr("id") == undefined) {
		if($("div#reportMessage").attr("id") != undefined) {
			$("div#divContinueUpload").hide();
		} else {
			$("div#divDisplayDetails").hide();
		}

		// Trigger for button that shows the details.
		$("div#buttonDisplayDetails").click(function() {
			// Hide elements.
			$("div#reportMessage").hide();
			$("div#informationMessage").hide();
			$("div#divDisplayDetails").hide();
			$("div#divCancelOverwrite").hide();

			// Show elements.
			$("div#detailsDiv").show();
			$("div#divOverwriteIds").show();
			$("div#divKeepIds").show();

			// Put the view to the top.
			$('html, body').stop().animate({
		        'scrollTop': $("div#detailsDiv").offset().top - 30
		    }, 900, 'swing', function () {
		    	//logAction("scroll moved to: ", $("div#detailsDiv").offset().top - 30);
		    });
		});

		// Trigger for button that overwrites the identifiers.
		$("div#buttonOverwriteIds").click(function() {
			// Check if all the values are filled.
			var filled = checkAllSelectsFilled();
			if (filled) {
				// Fill the lists with the selection of the user.
				fillLists();

				$("input#overwriteIDs").attr("value", "overwriteIds");
				$("#updatesPartForm").submit();
			} else {
				alert(selectMessage);
			}
		});

		// Trigger for button that keeps the identifiers.
		$("div#buttonKeepIds").click(function() {
			// Check if all the values are filled.
			var filled = checkAllSelectsFilled();
			if (filled) {
				// Fill the lists with the selection of the user.
				fillLists();

				$("input#overwriteIDs").attr("value", "keepIds");
				$("#updatesPartForm").submit();
			} else {
				alert(selectMessage);
			}
		});
	} else {
		$("div#divDisplayDetails").hide();
	}

	// Trigger for button that continue the upload process.
	$("div#buttonContinueUpload").click(function() {
		$("#continueUploadForm").submit();
	});

	// Trigger for button that cancels the upload process.
	$("div#buttonCancelOverwrite").click(function() {
		$("#cancelOverwriteForm").submit();
	});
}

/**
 * Function to check if all selects are filled.
 */
function checkAllSelectsFilled() {
	var result = true;

	$("select[id^='selectNew']").each(function(){
		if ($(this).val() == "---") {
			result = false;
		}
	});

	return result;
}

/**
 * Main function to fill the lists with the selection of the user.
 */
function fillLists() {
	// Constructs the list of elements to add.
	addInternalIdentifiers();

	// Constructs the list of elements to delete.
	deleteInternalIdentifiers();

	// Construct the lists of old and new elements.
	oldAndNewInternalIdentifiers();
}

/**
 * Function to create the list of internal identifiers that should be added
 * after select them in the selects.
 */
function addInternalIdentifiers() {
	var count = 0;
	$("input[id^='oldDuplicateNameInstitutionAdd']").each(function(){
		var id = $(this).attr("id");
		id = id.substring(id.indexOf("_"));
		var internalId = $("#selectNew" + id).val();

		$("input#overwriteIDs").before('<input id="addInstitutionsFromSelect[' + count + ']" type="hidden" value="' + internalId + '" name="addInstitutionsFromSelect[' + count + ']">');
		count++;
	});
}

/**
 * Function to create the list of internal identifiers that should be deleted
 * after select them in the selects.
 */
function deleteInternalIdentifiers() {
	var count = 0;
	$("select[id^='selectNew_']").each(function(){
		if ($(this).val() == "delete") {
			var id = $(this).attr("id");
			id = id.substring(id.indexOf("_"));
			var internalId = $("input#oldDuplicateNameInstitution" + id).val();

			$("input#overwriteIDs").before('<input id="deleteInstitutionsFromSelect[' + count + ']" type="hidden" value="' + internalId + '" name="deleteInstitutionsFromSelect[' + count + ']">');
			count++;
		}
	});
}

/**
 * Function to create the lists of internal identifiers with the mapping
 * between old and new ones after the user select them in the selects.
 */
function oldAndNewInternalIdentifiers() {
	var count = 0;
	$("input[id^='oldDuplicateNameInstitution_']").each(function(){
		// Value of the old internal identifier.
		var oldInternalId = $(this).val();

		// Value of the new internal identifier.
		var id = $(this).attr("id");
		id = id.substring(id.indexOf("_"));
		var newInternalId = $("#selectNew" + id).val();

		if (newInternalId != "delete") {
			// create element for the old institution.
			$("input#overwriteIDs").before('<input id="oldInstitutionsFromSelect[' + count + ']" type="hidden" value="' + oldInternalId + '" name="oldInstitutionsFromSelect[' + count + ']">');

			// Create element for the new institution.
			$("input#overwriteIDs").before('<input id="newInstitutionsFromSelect[' + count + ']" type="hidden" value="' + newInternalId + '" name="newInstitutionsFromSelect[' + count + ']">');

			count++;
		}
	});
}

/**
 * Function invoked each time a selection is changed.
 *
 * @param select Select changed.
 */
function selectionChanged(select) {
	// Identifier of the current select.
	var currentId = select.attr("id");
	// Value of the current select.
	var currentValue = select.val();

	// Recover the identifier of the selects to check.
	var id = currentId.substring(0, currentId.lastIndexOf("_"));

	$("select[id^='" + id + "']").each(function(){
		if ($(this).attr("id") != currentId
				&& $(this).val() == currentValue
				&& currentValue != "delete")  {
			$(this).val($("#" + $(this).attr("id") + " option:first").val())
		}
	});
}