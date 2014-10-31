/**************************************
 * main function, ensures tabbed look and feel
 **************************************/
function hideAndShow(idPrefix, shown) {
    $("div[id^='" + idPrefix + "']").each(function() {
        $(this).hide();
    });
    $("ul#eacCpfTabsContainer li a[href^='#tab']").each(function() {
        $(this).removeClass("eacCpfCurrenttab");
    });
    $("div[id='" + shown + "']").show();
    $("ul#eacCpfTabsContainer li a[href='#" + shown + "']").addClass("eacCpfCurrenttab");
    $('#buttonPreviousTab').show();
    $('#buttonNextTab').show();
    if (shown == "tab-identity") {
        $('#buttonPreviousTab').hide();
    }
    if (shown == "tab-control") {
        $('#buttonNextTab').hide();
    }
}

function loadPreviousTab() {
    var id = $(".eacCpfCurrenttab").parent().attr("id");
    if (id == "tab-description") {
        $("ul#eacCpfTabsContainer a[href='#tab-identity']").trigger('click');
        $("#currentTab").attr("value", "tab-identity");
    } else if (id == "tab-relations") {
        $("ul#eacCpfTabsContainer a[href='#tab-description']").trigger('click');
        $("#currentTab").attr("value", "tab-description");
    } else if (id == "tab-control") {
        $("ul#eacCpfTabsContainer a[href='#tab-relations']").trigger('click');
        $("#currentTab").attr("value", "tab-relations");
    }
}

function loadNextTab() {
    var id = $(".eacCpfCurrenttab").parent().attr("id");
    if (id == "tab-identity") {
        $("ul#eacCpfTabsContainer a[href='#tab-description']").trigger('click');
        $("#currentTab").attr("value", "tab-description");
    } else if (id == "tab-description") {
        $("ul#eacCpfTabsContainer a[href='#tab-relations']").trigger('click');
        $("#currentTab").attr("value", "tab-relations");
    } else if (id == "tab-relations") {
        $("ul#eacCpfTabsContainer a[href='#tab-control']").trigger('click');
        $("#currentTab").attr("value", "tab-control");
    }
}

/**************************************
 * Functions for correct display of context
 * according to chosen use mode
 **************************************/
function loadUseModeContext() {
    $('.area').hide();
    var useMode = $('input[name=useMode]').val();
    if (useMode == "new") {
        $('#newType').show();
        $('#newLanguage').show();
    } else {
        $('#load').show();
    }
}

/**************************************
 * Start function, checks mandatory fields
 * before submitting the form
 **************************************/
function clickGoAction() {
    $('#webformChooseMode').submit();
}

/**************************************
 * Save button function, checks mandatory fields
 * before submitting the form
 **************************************/
function clickSaveAction(onlySave, nameMissing, dateMissing, startDateMissing, endDateMissing, cpfTypeMissing, resourceTypeMissing, functionTypeMissing, languageMissing, scriptMissing, invalidDateMessage, invalidRangeMessage) {
    var Dlg = document.getElementById('dialog-saveOnQuit');
    Dlg.style.visibility = 'hidden';

    // Check fill mandatory fields in tab "your institution".
    var identityValidation = checkIdentityTab(nameMissing, dateMissing, startDateMissing, endDateMissing, invalidDateMessage, invalidRangeMessage);
    if (identityValidation !== "ok") {
        return;
    }
    var descriptionValidation = checkDescriptionTab(invalidDateMessage, invalidRangeMessage);
    if (descriptionValidation !== "ok") {
        return;
    }
    var relationValidation = checkRelationsTab(cpfTypeMissing, resourceTypeMissing, functionTypeMissing);
    if (relationValidation !== "ok") {
        return;
    }
    var controlValidation = checkControlTab(languageMissing, scriptMissing);
    if (controlValidation !== "ok") {
        return;
    }

    if (onlySave) {
// TODO: issue 1223, for complete the reload when save first needed the edit
// of an apeEAC-CPF will be implemented
        $("input#saveOrExit").attr("value", "save");
        $('#webformEacCpf').submit();

// Try to save without refresh the page.
//        $.post("storeEacCpf.action", $('#webformEacCpf').serialize(), function(d) {
//            if (d.fileId) {
//                $("input#fileId").attr("value", d.fileId);
//            }
//            if (d.eacDaoId) {
//                $("input#eacDaoId").attr("value", d.eacDaoId);
//            }
//            if (d.resultMessage) {
//                $("ul#eacCpfTabsContainer a[href='#tab-identity']").trigger('click');
//                $("div#spanMessage").html("<span>" + d.resultMessage + "</span>")
//                $("div#spanMessage").fadeIn("slow");
//                $(document).bind('keyup mousedown', function() {
//                    $("div#spanMessage").fadeOut("slow");
//                });
//            }
//            if (d.error) {
//                alert(d.error);
//            }
//        });
    } else {
        $("input#saveOrExit").attr("value", "save_exit");
        $('#webformEacCpf').submit();
    }
}

/**************************************
 * Exit button function for Start tab, leaves
 * the form without saving the contents
 **************************************/
function clickExitActionStartPage() {
    var useMode = $('input[name=useMode]').val();
    if (useMode == "load") {
        location.href = "contentmanager.action";
    } else {
        location.href = "dashboardHome.action";
    }
}

/**************************************
 * Exit button function, ask the user to save
 * the contents or leaves the form without
 * saving them
 **************************************/
function clickExitAction() {
// Checks the return page on exit.
    var useMode = $('input[name=useMode]').val();
    if (useMode == "load") {
        $("input#returnPage").attr("value", "contentmanager");
    } else {
        $("input#returnPage").attr("value", "dashboardHome");
    }

    // Display the dialog box.
    displayExitDialog($("#DlgContent").text(),$("input#btnYes").val(),$("input#btnNo").val());
//    $("#eacCpfDiv :input").attr("readonly", true);
//    var Dlg = document.getElementById("dialog-saveOnQuit");
//    Dlg.style.visibility = "visible";

// Ask user for the action.
//    if (confirmAndDecode(question)) {
//        clickSaveAction(false, nameMissing, dateMissing, startDateMissing, endDateMissing, cpfTypeMissing, resourceTypeMissing, functionTypeMissing, languageMissing, scriptMissing);
//    } else {
//        $("input#saveOrExit").attr("value", "exit");
//        $('#webformEacCpf').submit();
//    }
//
//    $("#dialog-saveOnQuit").dialog({
//        resizable: false,
//        height: 140,
//        modal: true,
//        buttons: {
//            "Yes": function() {
//                clickSaveAction(false, nameMissing, dateMissing, startDateMissing, endDateMissing, cpfTypeMissing, resourceTypeMissing, functionTypeMissing, languageMissing, scriptMissing);
//            },
//            "No": function() {
//                $("input#saveOrExit").attr("value", "exit");
//                $('#webformEacCpf').submit();
//            }
//        }
//    });
}

function displayExitDialog(dialogText,dialogYes,dialogNo){
	dialogText = HTMLDecode(dialogText);
	dialogYes = HTMLDecode(dialogYes);
	dialogNo = HTMLDecode(dialogNo);
	if($(".ui-dialog").length>0){
		$(".ui-dialog").remove();
	}
	$("body").unbind("click");
	var dialog = $("<p id=\"pAlertExitDialog\">"+dialogText+"</p>").dialog({
		closeOnEscape : true,
        buttons: 
    	[{
        	text: dialogYes,
        	click : function() {
        		$(this).dialog("close");
        		$("input#btnYes").trigger("click");
        		/*$("input#saveOrExit").attr("value", "save_exit");
                $('#webformEacCpf').submit();*/
        	}
    	},{
        	text : dialogNo,
        	click :  function() {
        		$(this).dialog("close");
        		$("input#btnNo").trigger("click");
        		/*$("input#saveOrExit").attr("value", "exit");
                $('#webformEacCpf').submit();*/
        	}
    	}]
	});
	setTimeout("putClickOutsideCloseDialog();","100");
}

function displayAlertDialog(message){
	message = HTMLDecode(message);
	if($(".ui-dialog").length>0){
		$(".ui-dialog").remove();
	}
	$("body").unbind("click");
	var dialog = $("<p id=\"pAlertExitDialog\">"+message+"</p>").dialog({
		closeOnEscape : true,
        buttons: {
        	"OK" : function() {
        		$("body").unbind("click");
        		$(this).dialog("close");
        	}
    	}
	});
	setTimeout("putClickOutsideCloseDialog();","100");
}

function putClickOutsideCloseDialog(){
	$("body").bind("click",function(e){
		if($("#pAlertExitDialog").dialog("isOpen")
				&& !jQuery(e.target).is(".ui-dialog")
				&& !jQuery(e.target).closest(".ui-dialog").length ){
			$('#pAlertExitDialog').dialog("close");
			$("body").unbind("click");
		}
	});
}

/**************************************
 * Exit button function, ask the user to save
 * the contents or leaves the form without
 * saving them
 **************************************/
function clickExitWithoutSaveAction() {
    var Dlg = document.getElementById("dialog-saveOnQuit");
    Dlg.style.visibility = "hidden";
    $("input#saveOrExit").attr("value", "exit");
    $('#webformEacCpf').submit();
}

/**************************************
 * general purpose functions
 **************************************/

function deleteChecks() {
    $('.fieldRequired').remove();
}

var checkIdentityTab = function(nameMissing, dateMissing, startDateMissing, endDateMissing, invalidDateMessage, invalidRangeMessage) {
    //check if at least one name was added
    var personName = $("table#identityPersonName_1 input#textPersonName").attr("value");
    if (personName == null || personName == "") {
        alertEmptyFields(nameMissing);
        return;
    }

    //check if at least one existDates entry exists
    var date1 = $("table#dateExistenceTable tr#trDate_text_1 input#date_1").attr("value");
    var date1Checked = $("table#dateExistenceTable tr#trDate_radio_1 input[name^='dateExistenceTable_date_1_']:checked").val();
    var date2 = $("table#dateExistenceTable tr#trDate_text_1 input#date_2").attr("value");
    var date2Checked = $("table#dateExistenceTable tr#trDate_radio_1 input[name^='dateExistenceTable_date_2_']:checked").val();
    if ((date1 == null || date1 == "") && date1Checked == "known") {
        if (date2 == "" && date2Checked == "known") {
            alertEmptyFields(dateMissing);
            return;
        } else {
            alertEmptyFields(startDateMissing);
            return;
        }
    } else {
        if (date2 == "" && date2Checked == "known") {
            alertEmptyFields(endDateMissing);
            return;
        }
    }

    //check any ISO date rows in identity tab for validity one more time
    var checkResult = "ok";
    var tableCounter = $("table[id^='identityPersonName_']").length;
    var rowCounter;
    var dateCounter;
    //check name dates
    if (tableCounter > 0) {
        for (var tc = 1; tc <= tableCounter; tc++) {
            rowCounter = $("table#identityPersonName_" + tc + " tr[id^='trDate_text_']").length;
            if (rowCounter > 0) {
                for (var rc = 1; rc <= rowCounter; rc++) {
                    if (dateRowNotEmpty("identityPersonName_" + tc, rc)) {
                        dateCounter = $("table#identityPersonName_" + tc + " tr#trDate_text_" + rc + " input[id^='date_']").length;
                        for (var dc = 1; dc <= dateCounter; dc++) {
                            checkResult = checkIsoDateRow("identityPersonName_" + tc, rc, dc, invalidDateMessage, invalidRangeMessage);
                            if (checkResult != "ok") {
                                return;
                            }
                        }
                    }
                }
            }
        }
    }
    //check existence dates
    rowCounter = $("table#dateExistenceTable tr[id^='trDate_text_']").length;
    if (rowCounter > 0) {
        for (var rc = 1; rc <= rowCounter; rc++) {
            if (dateRowNotEmpty("dateExistenceTable", rc)) {
                dateCounter = $("table#dateExistenceTable tr#trDate_text_" + rc + " input[id^='date_']").length;
                for (var dc = 1; dc <= dateCounter; dc++) {
                    checkResult = checkIsoDateRow("dateExistenceTable", rc, dc, invalidDateMessage, invalidRangeMessage);
                    if (checkResult != "ok") {
                        return;
                    }
                }
            }
        }
    }

    //return "ok" if everything is valid
    return "ok";
};

var checkDescriptionTab = function(invalidDateMessage, invalidRangeMessage) {
    //check any ISO date rows in description tab for validity one more time
    var checkResult = "ok";
    var tableCounter = $("table[id^='placeTable_']").length;
    var rowCounter;
    var dateCounter;
    //check place dates
    if (tableCounter > 0) {
        for (var tc = 1; tc <= tableCounter; tc++) {
            rowCounter = $("table#placeTable_" + tc + " tr[id^='trDate_text_']").length;
            if (rowCounter > 0) {
                for (var rc = 1; rc <= rowCounter; rc++) {
                    if (dateRowNotEmpty("placeTable_" + tc, rc)) {
                        dateCounter = $("table#placeTable_" + tc + " tr#trDate_text_" + rc + " input[id^='date_']").length;
                        for (var dc = 1; dc <= dateCounter; dc++) {
                            checkResult = checkIsoDateRow("placeTable_" + tc, rc, dc, invalidDateMessage, invalidRangeMessage);
                            if (checkResult != "ok") {
                                return;
                            }
                        }
                    }
                }
            }
        }
    }

    //check function dates
    var tableCounter = $("table[id^='functionTable_']").length;
    if (tableCounter > 0) {
        for (var tc = 1; tc <= tableCounter; tc++) {
            rowCounter = $("table#functionTable_" + tc + " tr[id^='trDate_text_']").length;
            if (rowCounter > 0) {
                for (var rc = 1; rc <= rowCounter; rc++) {
                    if (dateRowNotEmpty("functionTable_" + tc, rc)) {
                        dateCounter = $("table#functionTable_" + tc + " tr#trDate_text_" + rc + " input[id^='date_']").length;
                        for (var dc = 1; dc <= dateCounter; dc++) {
                            checkResult = checkIsoDateRow("functionTable_" + tc, rc, dc, invalidDateMessage, invalidRangeMessage);
                            if (checkResult != "ok") {
                                return;
                            }
                        }
                    }
                }
            }
        }
    }

    //check occupation dates
    var tableCounter = $("table[id^='occupationTable_']").length;
    if (tableCounter > 0) {
        for (var tc = 1; tc <= tableCounter; tc++) {
            rowCounter = $("table#occupationTable_" + tc + " tr[id^='trDate_text_']").length;
            if (rowCounter > 0) {
                for (var rc = 1; rc <= rowCounter; rc++) {
                    if (dateRowNotEmpty("occupationTable_" + tc, rc)) {
                        dateCounter = $("table#occupationTable_" + tc + " tr#trDate_text_" + rc + " input[id^='date_']").length;
                        for (var dc = 1; dc <= dateCounter; dc++) {
                            checkResult = checkIsoDateRow("occupationTable_" + tc, rc, dc, invalidDateMessage, invalidRangeMessage);
                            if (checkResult != "ok") {
                                return;
                            }
                        }
                    }
                }
            }
        }
    }

    //return "ok" if everything is valid
    return "ok";
}

var checkRelationsTab = function(cpfTypeMissing, resourceTypeMissing, functionTypeMissing) {
    var cpfCounter = $("table[id^='cpfRelationsTable_']").length;
    var resCounter = $("table[id^='resRelationsTable_']").length;
    var fncCounter = $("table[id^='fncRelationsTable_']").length;
    var cpfResult = checkForEmptyRelationContent("cpf", cpfCounter);
    if (cpfResult != "ok") {
        alertEmptyFields(cpfTypeMissing);
        return;
    }
    var resResult = checkForEmptyRelationContent("res", resCounter);
    if (resResult != "ok") {
        alertEmptyFields(resourceTypeMissing);
        return;
    }
    var fncResult = checkForEmptyRelationContent("fnc", fncCounter);
    if (fncResult != "ok") {
        alertEmptyFields(functionTypeMissing);
        return;
    }
    return "ok";
};

var checkForEmptyRelationContent = function(abb, counterValue) {
    for (i = 1; i <= counterValue; i++) {
        var text = "";
        $("table#" + abb + "RelationsTable_" + counterValue + " input[type='text']").each(function() {
            text += $(this).val();
        });
        text += $("table#" + abb + "RelationsTable_" + counterValue + " textarea").val();
        if (text != "") {
            var relType = $("table#" + abb + "RelationsTable_" + counterValue + " select#" + abb + "RelationType").attr("value");
            if (relType == "") {
                return;
            }
        }
    }
    return "ok";
};

var checkControlTab = function(languageMissing, scriptMissing) {
    var language = $("table#usedLanguagesAndScripts select#controlLanguage").attr("value");
    var script = $("table#usedLanguagesAndScripts select#controlScript").attr("value");
    if (language == null || language == "") {
        alertEmptyFields(languageMissing);
        return;
    }
    if (script == null || script == "") {
        alertEmptyFields(scriptMissing);
        return;
    }
    return "ok";
};

function checkWebpages(target, message) {
    var checkFails = false;
    var value = target.val();
    if (value && value.length > 0) {
        value = value.toLowerCase();
        value = $.trim(value);
        if (!(value.indexOf("https://") === 0 || value.indexOf("http://") === 0 || value.indexOf("ftp://") === 0)) {
            var pFieldError = "<p id=\"" + $(this).attr("id") + "_w_required\" class=\"fieldRequired\">" + message + "</p>";
            var id = target.attr("id");
            if (id.indexOf("textWebsiteOfResource") === "-1" && id.indexOf("textWebsiteOfDescription") === "-1") {
                target.after(pFieldError);
            } else {
                var parent = target.parent().parent().parent().parent().attr("id");
                $("table#" + parent + " input#" + id).after(pFieldError);
            }
            checkFails = true;
        }
    }
    return checkFails;
}

function alertEmptyFields(text1) {
	displayAlertDialog(text1);
}

function alertFillFieldsBeforeChangeTab(text) {
	displayAlertDialog(text);
}

/**************************************
 * date-related functions
 **************************************/

function insertDateAfter(tableName, anchorId, incrCounter, dateLabel, dateTypeLabel, knownLabel, unknownLabel, openLabel, isoLabel, invalidDateMessage, invalidRangeMessage) {
    var newDate = $('<tr id="trDate_text_' + incrCounter + '">' +
            '<td>' + dateLabel + '</td>' +
            '<td>' +
            '<input type="text" id="date_1" name="' + tableName + '_date_1_' + incrCounter + '" onchange="parseDateToISO($(this).attr(\'value\'), $(this).parent().parent().parent().parent().attr(\'id\'), $(this).parent().parent().attr(\'id\'), $(this).attr(\'id\'), \'' + invalidDateMessage + '\', \'' + invalidRangeMessage + '\');"><br />' +
            '</td>' +
            '<td></td><td></td>' +
            '</tr>' +
            '<tr id="trDate_radio_' + incrCounter + '">' +
            '<td>' +
            dateTypeLabel +
            '</td>' +
            '<td>' +
            '<input type="radio" name="' + tableName + '_date_1_radio_' + incrCounter + '" value="known" checked="checked" onchange="toggleDateTextfields($(this));">' + knownLabel + '&nbsp;' +
            '<input type="radio" name="' + tableName + '_date_1_radio_' + incrCounter + '" value="unknown" onchange="toggleDateTextfields($(this));">' + unknownLabel + '&nbsp;' +
            '<input type="radio" name="' + tableName + '_date_1_radio_' + incrCounter + '" value="open" onchange="toggleDateTextfields($(this));">' + openLabel +
            '</td>' +
            '<td></td><td></td>' +
            '</tr>' +
            '<tr id="trDate_iso_' + incrCounter + '">' +
            '<td>' + isoLabel + '</td>' +
            '<td>' +
            '<table style="width: 50%;">' +
            '<tr>' +
            '<td style="padding: 0px;"><input type="text" title="YYYY" id="date_1_Year" name="' + tableName + '_date_1_Year_' + incrCounter + '" size="4" maxlength="4" onchange="validateIsoDates($(this), \'' + invalidDateMessage + '\', \'' + invalidRangeMessage + '\');" /></td>' +
            '<td style="padding: 0px;">&ndash;</td>' +
            '<td style="padding: 0px;"><input type="text" title="MM" id="date_1_Month" name="' + tableName + '_date_1_Month_' + incrCounter + '" size="2" maxlength="2" onchange="validateIsoDates($(this), \'' + invalidDateMessage + '\', \'' + invalidRangeMessage + '\');" /></td>' +
            '<td style="padding: 0px;">&ndash;</td>' +
            '<td style="padding: 0px;"><input type="text" title="DD" id="date_1_Day" name="' + tableName + '_date_1_Day_' + incrCounter + '" size="2" maxlength="2" onchange="validateIsoDates($(this), \'' + invalidDateMessage + '\', \'' + invalidRangeMessage + '\');" /></td>' +
            '</tr>' +
            '<tr>' +
            '<td style="padding: 0px;">YYYY</td>' +
            '<td style="padding: 0px;">&ndash;</td>' +
            '<td style="padding: 0px;">MM</td>' +
            '<td style="padding: 0px;">&ndash;</td>' +
            '<td style="padding: 0px;">DD</td>' +
            '</tr>' +
            '</table>' +
            '</td>' +
            '<td></td><td></td>' +
            '</tr>');
    $("table#" + tableName + " " + anchorId).after(newDate);
}

function insertDateRangeAfter(tableName, anchorId, incrCounter, fromDateLabel, toDateLabel, dateTypeLabel, knownLabel, unknownLabel, openLabel, isoLabel, invalidDateMessage, invalidRangeMessage) {
    var newDateRange = $('<tr id="trDate_text_' + incrCounter + '">' +
            '<td>' + fromDateLabel + '</td>' +
            '<td>' +
            '<input type="text" id="date_1" name="' + tableName + '_date_1_' + incrCounter + '" onchange="parseDateToISO($(this).attr(\'value\'), $(this).parent().parent().parent().parent().attr(\'id\'), $(this).parent().parent().attr(\'id\'), $(this).attr(\'id\'), \'' + invalidDateMessage + '\', \'' + invalidRangeMessage + '\');"><br />' +
            '</td>' +
            '<td>' + toDateLabel + '</td>' +
            '<td>' +
            '<input type="text" id="date_2" name="' + tableName + '_date_2_' + incrCounter + '" onchange="parseDateToISO($(this).attr(\'value\'), $(this).parent().parent().parent().parent().attr(\'id\'), $(this).parent().parent().attr(\'id\'), $(this).attr(\'id\'), \'' + invalidDateMessage + '\', \'' + invalidRangeMessage + '\');"><br />' +
            '</td>' +
            '<tr id="trDate_radio_' + incrCounter + '">' +
            '<td>' +
            dateTypeLabel +
            '</td>' +
            '<td>' +
            '<input type="radio" name="' + tableName + '_date_1_radio_' + incrCounter + '" value="known" checked="checked" onchange="toggleDateTextfields($(this));">' + knownLabel + '&nbsp;' +
            '<input type="radio" name="' + tableName + '_date_1_radio_' + incrCounter + '" value="unknown" onchange="toggleDateTextfields($(this));">' + unknownLabel + '&nbsp;' +
            '</td>' +
            '<td></td>' +
            '<td>' +
            '<input type="radio" name="' + tableName + '_date_2_radio_' + incrCounter + '" value="known" checked="checked" onchange="toggleDateTextfields($(this));">' + knownLabel + '&nbsp;' +
            '<input type="radio" name="' + tableName + '_date_2_radio_' + incrCounter + '" value="unknown" onchange="toggleDateTextfields($(this));">' + unknownLabel + '&nbsp;' +
            '<input type="radio" name="' + tableName + '_date_2_radio_' + incrCounter + '" value="open" onchange="toggleDateTextfields($(this));">' + openLabel +
            '</td>' +
            '</tr>' +
            '</tr>' +
            '<tr id="trDate_iso_' + incrCounter + '">' +
            '<td>' + isoLabel + '</td>' +
            '<td>' +
            '<table style="width: 50%;">' +
            '<tr>' +
            '<td style="padding: 0px;"><input type="text" title="YYYY" id="date_1_Year" name="' + tableName + '_date_1_Year_' + incrCounter + '" size="4" maxlength="4" onchange="validateIsoDates($(this), \'' + invalidDateMessage + '\', \'' + invalidRangeMessage + '\');" /></td>' +
            '<td style="padding: 0px;">&ndash;</td>' +
            '<td style="padding: 0px;"><input type="text" title="MM" id="date_1_Month" name="' + tableName + '_date_1_Month_' + incrCounter + '" size="2" maxlength="2" onchange="validateIsoDates($(this), \'' + invalidDateMessage + '\', \'' + invalidRangeMessage + '\');" /></td>' +
            '<td style="padding: 0px;">&ndash;</td>' +
            '<td style="padding: 0px;"><input type="text" title="DD" id="date_1_Day" name="' + tableName + '_date_1_Day_' + incrCounter + '" size="2" maxlength="2" onchange="validateIsoDates($(this), \'' + invalidDateMessage + '\', \'' + invalidRangeMessage + '\');" /></td>' +
            '</tr>' +
            '<tr>' +
            '<td style="padding: 0px;">YYYY</td>' +
            '<td style="padding: 0px;">&ndash;</td>' +
            '<td style="padding: 0px;">MM</td>' +
            '<td style="padding: 0px;">&ndash;</td>' +
            '<td style="padding: 0px;">DD</td>' +
            '</tr>' +
            '</table>' +
            '</td>' +
            '<td></td>' +
            '<td>' +
            '<table style="width: 50%;">' +
            '<tr>' +
            '<td style="padding: 0px;"><input type="text" title="YYYY" id="date_2_Year" name="' + tableName + '_date_2_Year_' + incrCounter + '" size="4" maxlength="4" onchange="validateIsoDates($(this), \'' + invalidDateMessage + '\', \'' + invalidRangeMessage + '\');" /></td>' +
            '<td style="padding: 0px;">&ndash;</td>' +
            '<td style="padding: 0px;"><input type="text" title="MM" id="date_2_Month" name="' + tableName + '_date_2_Month_' + incrCounter + '" size="2" maxlength="2" onchange="validateIsoDates($(this), \'' + invalidDateMessage + '\', \'' + invalidRangeMessage + '\');" /></td>' +
            '<td style="padding: 0px;">&ndash;</td>' +
            '<td style="padding: 0px;"><input type="text" title="DD" id="date_2_Day" name="' + tableName + '_date_2_Day_' + incrCounter + '" size="2" maxlength="2" onchange="validateIsoDates($(this), \'' + invalidDateMessage + '\', \'' + invalidRangeMessage + '\');" /></td>' +
            '</tr>' +
            '<tr>' +
            '<td style="padding: 0px;">YYYY</td>' +
            '<td style="padding: 0px;">&ndash;</td>' +
            '<td style="padding: 0px;">MM</td>' +
            '<td style="padding: 0px;">&ndash;</td>' +
            '<td style="padding: 0px;">DD</td>' +
            '</tr>' +
            '</table>' +
            '</td>' +
            '</tr>');
    $("table#" + tableName + " " + anchorId).after(newDateRange);
}


function dateRowNotEmpty(table, counter) {
    var testYear1 = $("table#" + table + " tr#trDate_text_" + counter + " input#date_1").attr("value");
    var testYear1Checked = $("table#" + table + " tr#trDate_radio_" + counter + " input[name^='" + table + "_date_1_']:checked").val();
    if ($("table#" + table + " tr#trDate_text_" + counter + " input#date_2").length != 0) {
        var testYear2 = $("table#" + table + " tr#trDate_text_" + counter + " input#date_2").attr("value");
        var testYear2Checked = $("table#" + table + " tr#trDate_radio_" + counter + " input[name^='" + table + "_date_2_']:checked").val();
    } else {
        var testYear2 = null;
    }
    if (testYear2 == null) {
        if (testYear1 == "" && testYear1Checked == "known")
            return false;
        else
            return true;
    } else if (testYear2 == "" && testYear1 == "" && testYear1Checked == "known" && testYear2Checked == "known") {
        return false;
    } else
        return true;
}

function parseDateToISO(content, table, row, date, invalidDateMessage, invalidRangeMessage) {
    var counterRow = row.split('_');
    var counterDate = date.split('_');
//Normal ISO pattern
    var PATTERN_ISO = /(-?(0|1|2)([0-9]{3})(((01|02|03|04|05|06|07|08|09|10|11|12|1|2|3|4|5|6|7|8|9)(01|02|03|04|05|06|07|08|09|10|11|12|13|14|15|16|17|18|19|20|21|22|23|24|25|26|27|28|29|30|31|1|2|3|4|5|6|7|8|9))|-((01|02|03|04|05|06|07|08|09|10|11|12|1|2|3|4|5|6|7|8|9)(-(01|02|03|04|05|06|07|08|09|10|11|12|13|14|15|16|17|18|19|20|21|22|23|24|25|26|27|28|29|30|31|1|2|3|4|5|6|7|8|9))?))?)/;
    //01.01.1985
    var PATTERN_DD_MM_YYYY = /((0?[1-9])|((1|2)[0-9])|(3[0-1]))[\.\/](1|2|3|4|5|6|7|8|9|01|02|03|04|05|06|07|08|09|10|11|12)[\.\/]([0-9]{4})/;
    var PATTERN_YYYYMM = /(0|1|2)([0-9]{3})([0-9]{2})/;
    //1985.01.01
    var PATTERN_YYYY_MM_DD = /([0-9]{4})\.(1|2|3|4|5|6|7|8|9|01|02|03|04|05|06|07|08|09|10|11|12)\.(01|02|03|04|05|06|07|08|09|10|11|12|13|14|15|16|17|18|19|20|21|22|23|24|25|26|27|28|29|30|31|1|2|3|4|5|6|7|8|9)/;
    //01011985
    var PATTERN_DDMMYYYY = /((0[1-9])|((1|2)[0-9])|(3[0-1]))(01|02|03|04|05|06|07|08|09|10|11|12)((0|1|2)([0-9]{3}))/;
    //19850101
    var PATTERN_YYYYMMDD = /((0|1|2)([0-9]{3}))(01|02|03|04|05|06|07|08|09|10|11|12)((0[1-9])|((1|2)[0-9])|(3[0-1]))/;
    /*    //avant 1985
     var PATTERN_BEFORE = /(avant|vor|before)*\\s*([0-9]{4})/;
     //apres 1985
     var PATTERN_AFTER = /(apres|après|nach|after)*\\s*([0-9]{4})/;
     //around 1985
     var PATTERN_AROUND = /(environ|around|ca\\.?|env\\.?|etwa\\.?|um\\.?)*\\s*([0-9]{4})/;
     //century
     var PATTERN_CENTURY = /(1[0-9]{1})(ieme|\\.|th|de)* (siècle|siecle|Jhd|century|Century|Jahrhundert|Jh|eeuw)*\\s*\\.*/

    var result;
    if (PATTERN_ISO.test(content) == true) {
//We need a second check for YYYYMMDD and YYYYMMDD/YYYYMMDD which passes above
        var PATTERN_SECONDCHECK = /([0-9]{8})/;
        if (!PATTERN_SECONDCHECK.test(content) == true) {
            result = PATTERN_ISO.exec(content);
            $("table#" + table + " tr#trDate_iso_" + counterRow[2] + " input#date_" + counterDate[1] + "_Year").attr("value", result[2] + result[3]);
            if (result[9] != null) {
                $("table#" + table + " tr#trDate_iso_" + counterRow[2] + " input#date_" + counterDate[1] + "_Month").attr("value", addTrailingZero(result[9]));
            } else {
                $("table#" + table + " tr#trDate_iso_" + counterRow[2] + " input#date_" + counterDate[1] + "_Month").removeAttr("value");
            }
            if (result[11] != null) {
                $("table#" + table + " tr#trDate_iso_" + counterRow[2] + " input#date_" + counterDate[1] + "_Day").attr("value", addTrailingZero(result[11]));
            } else {
                $("table#" + table + " tr#trDate_iso_" + counterRow[2] + " input#date_" + counterDate[1] + "_Day").removeAttr("value");
            }
        }
    }
    if (PATTERN_DD_MM_YYYY.test(content) == true) {
        result = PATTERN_DD_MM_YYYY.exec(content);
        $("table#" + table + " tr#trDate_iso_" + counterRow[2] + " input#date_" + counterDate[1] + "_Year").attr("value", result[7]);
        $("table#" + table + " tr#trDate_iso_" + counterRow[2] + " input#date_" + counterDate[1] + "_Month").attr("value", addTrailingZero(result[6]));
        $("table#" + table + " tr#trDate_iso_" + counterRow[2] + " input#date_" + counterDate[1] + "_Day").attr("value", addTrailingZero(result[1]));
    }
    if (PATTERN_YYYYMM.test(content) == true) {
        result = PATTERN_YYYYMM.exec(content);
        $("table#" + table + " tr#trDate_iso_" + counterRow[2] + " input#date_" + counterDate[1] + "_Year").attr("value", result[1] + result[2]);
        $("table#" + table + " tr#trDate_iso_" + counterRow[2] + " input#date_" + counterDate[1] + "_Month").attr("value", addTrailingZero(result[3]));
        $("table#" + table + " tr#trDate_iso_" + counterRow[2] + " input#date_" + counterDate[1] + "_Day").removeAttr("value");
    }
    if (PATTERN_YYYY_MM_DD.test(content) == true) {
        result = PATTERN_YYYY_MM_DD.exec(content);
        if (999 < result[1] < 3000) {
            $("table#" + table + " tr#trDate_iso_" + counterRow[2] + " input#date_" + counterDate[1] + "_Year").attr("value", result[1]);
            $("table#" + table + " tr#trDate_iso_" + counterRow[2] + " input#date_" + counterDate[1] + "_Month").attr("value", addTrailingZero(result[2]));
            $("table#" + table + " tr#trDate_iso_" + counterRow[2] + " input#date_" + counterDate[1] + "_Day").attr("value", addTrailingZero(result[3]));
        }
    }
    if (PATTERN_DDMMYYYY.test(content) == true) {
        result = PATTERN_DDMMYYYY.exec(content);
        $("table#" + table + " tr#trDate_iso_" + counterRow[2] + " input#date_" + counterDate[1] + "_Year").attr("value", result[7]);
        $("table#" + table + " tr#trDate_iso_" + counterRow[2] + " input#date_" + counterDate[1] + "_Month").attr("value", addTrailingZero(result[6]));
        $("table#" + table + " tr#trDate_iso_" + counterRow[2] + " input#date_" + counterDate[1] + "_Day").attr("value", addTrailingZero(result[1]));
    }
    if (PATTERN_YYYYMMDD.test(content) == true) {
        result = PATTERN_YYYYMMDD.exec(content);
        $("table#" + table + " tr#trDate_iso_" + counterRow[2] + " input#date_" + counterDate[1] + "_Year").attr("value", result[1]);
        $("table#" + table + " tr#trDate_iso_" + counterRow[2] + " input#date_" + counterDate[1] + "_Month").attr("value", addTrailingZero(result[4]));
        $("table#" + table + " tr#trDate_iso_" + counterRow[2] + " input#date_" + counterDate[1] + "_Day").attr("value", addTrailingZero(result[5]));
    }

    // validate changed content for plausibility
    checkIsoDateRow(table, counterRow[2], counterDate[1], invalidDateMessage, invalidRangeMessage);
}

function addTrailingZero(value) {
    if (value > 0 && value < 10 && !(/^0/).test(value)) {
        return "0" + value;
    } else
        return value;
}

function toggleDateTextfields(radiobutton) {
    var counterDate = $(radiobutton).attr("name").split('_');
    var tableName = $(radiobutton).parent().parent().parent().parent().attr("id");
    if ($(radiobutton).val() == "unknown") {
        $('table#' + tableName + ' tr#trDate_text_' + counterDate[counterDate.length - 1] + ' input#date_' + counterDate[counterDate.length - 3]).attr("value", "unknown");
        $('table#' + tableName + ' tr#trDate_text_' + counterDate[counterDate.length - 1] + ' input#date_' + counterDate[counterDate.length - 3]).attr("disabled", "disabled");
        $('table#' + tableName + ' tr#trDate_iso_' + counterDate[counterDate.length - 1] + ' input[id^="date_' + counterDate[counterDate.length - 3] + '_"]').each(function() {
            $(this).attr("value", "");
            $(this).attr("disabled", "disabled");
        });
    } else if ($(radiobutton).val() == "open") {
        $('table#' + tableName + ' tr#trDate_text_' + counterDate[counterDate.length - 1] + ' input#date_' + counterDate[counterDate.length - 3]).attr("value", "open");
        $('table#' + tableName + ' tr#trDate_text_' + counterDate[counterDate.length - 1] + ' input#date_' + counterDate[counterDate.length - 3]).attr("disabled", "disabled");
        $('table#' + tableName + ' tr#trDate_iso_' + counterDate[counterDate.length - 1] + ' input[id^="date_' + counterDate[counterDate.length - 3] + '_"]').each(function() {
            $(this).attr("value", "");
            $(this).attr("disabled", "disabled");
        });
    } else {
        $('table#' + tableName + ' tr#trDate_text_' + counterDate[counterDate.length - 1] + ' input#date_' + counterDate[counterDate.length - 3]).attr("value", "");
        $('table#' + tableName + ' tr#trDate_text_' + counterDate[counterDate.length - 1] + ' input#date_' + counterDate[counterDate.length - 3]).removeAttr("disabled");
        $('table#' + tableName + ' tr#trDate_iso_' + counterDate[counterDate.length - 1] + ' input[id^="date_' + counterDate[counterDate.length - 3] + '_"]').each(function() {
            $(this).removeAttr("disabled");
        });
    }
}

function validateIsoDates(textfield, invalidDateMessage, invalidRangeMessage) {
    var id = $(textfield).attr("id");
    var idParts = id.split("_");
    var name = $(textfield).attr("name");
    var nameParts = name.split("_");
    var tableName = $(textfield).parent().parent().parent().parent().attr("id");
    var counter = nameParts[nameParts.length - 1];
    // add trailing zero for month and day fields if necessary
    if (idParts[2] == "Month" || idParts[2] == "Day") {
        var value = $(textfield).attr("value");
        $(textfield).attr("value", addTrailingZero(value));
    }
    // check ISO date field set and, if available, date range for plausibility
    checkIsoDateRow(tableName, counter, idParts[1], invalidDateMessage, invalidRangeMessage);
}

var checkIsoDateRow = function(tableName, rowCounter, dateCounter, invalidDateMessage, invalidRangeMessage) {
//1. check date for general validity
    var year = 1;
    var month = 1;
    var day = 1;
    if ($("table#" + tableName + " tr#trDate_radio_" + rowCounter + " input[name^='" + tableName + "_date_" + dateCounter + "_']:checked").val() == "known") {
        if ($("table#" + tableName + " tr#trDate_iso_" + rowCounter + " td input#date_" + dateCounter + "_Day").attr("value") != "") {
            day = $("table#" + tableName + " tr#trDate_iso_" + rowCounter + " td input#date_" + dateCounter + "_Day").attr("value");
        }
        if ($("table#" + tableName + " tr#trDate_iso_" + rowCounter + " td input#date_" + dateCounter + "_Month").attr("value") != "") {
            month = $("table#" + tableName + " tr#trDate_iso_" + rowCounter + " td input#date_" + dateCounter + "_Month").attr("value");
        }
        if ($("table#" + tableName + " tr#trDate_iso_" + rowCounter + " td input#date_" + dateCounter + "_Year").attr("value") != "") {
            year = $("table#" + tableName + " tr#trDate_iso_" + rowCounter + " td input#date_" + dateCounter + "_Year").attr("value");
        }
        var date = new Date(year, month - 1, day);
        if (date.getFullYear() != Number(year) || (date.getMonth() + 1) != Number(month) || date.getDate() != Number(day)) {
        	displayAlertDialog(year + "-" + month + "-" + day + ": " + invalidDateMessage);
            return;
        }
    }

//2. check date range for temporal order, i.e. no ranges like 1985-1983
    var date1;
    var date2;

    if (dateCounter == 2) {
        if (year == 1) {
            date2 = new Date(9999, 0, 1)
        } else {
            date2 = date;
        }
        year = -9999;
        month = 1;
        day = 1;
        if ($("table#" + tableName + " tr#trDate_iso_" + rowCounter + " td input#date_1_Day").attr("value") != "") {
            day = $("table#" + tableName + " tr#trDate_iso_" + rowCounter + " td input#date_1_Day").attr("value");
        }
        if ($("table#" + tableName + " tr#trDate_iso_" + rowCounter + " td input#date_1_Month").attr("value") != "") {
            month = $("table#" + tableName + " tr#trDate_iso_" + rowCounter + " td input#date_1_Month").attr("value");
        }
        if ($("table#" + tableName + " tr#trDate_iso_" + rowCounter + " td input#date_1_Year").attr("value") != "") {
            year = $("table#" + tableName + " tr#trDate_iso_" + rowCounter + " td input#date_1_Year").attr("value");
        }
        date1 = new Date(year, month - 1, day);
    }
    if (dateCounter == 1 && ($("table#" + tableName + " tr#trDate_text_" + rowCounter + " td input#date_2_text").attr("value") != "" || $("table#" + tableName + " tr#trDate_radio_" + rowCounter + " input[name^='" + tableName + "_date_2_']:checked").val() != "known")) {
        if (year == 1) {
            date1 = new Date(-9999, 0, 1)
        } else {
            date1 = date;
        }
        year = 9999;
        month = 1;
        day = 1;
        if ($("table#" + tableName + " tr#trDate_iso_" + rowCounter + " td input#date_2_Day").attr("value") != "") {
            day = $("table#" + tableName + " tr#trDate_iso_" + rowCounter + " td input#date_2_Day").attr("value");
        }
        if ($("table#" + tableName + " tr#trDate_iso_" + rowCounter + " td input#date_2_Month").attr("value") != "") {
            month = $("table#" + tableName + " tr#trDate_iso_" + rowCounter + " td input#date_2_Month").attr("value");
        }
        if ($("table#" + tableName + " tr#trDate_iso_" + rowCounter + " td input#date_2_Year").attr("value") != "") {
            year = $("table#" + tableName + " tr#trDate_iso_" + rowCounter + " td input#date_2_Year").attr("value");
        }
        date2 = new Date(year, month - 1, day);
    }
    if (date1 > date2) {
    	displayAlertDialog(invalidRangeMessage);
        return;
    }
    return "ok";
};

/**************************************
 * Identity tab functions
 **************************************/

function addPartName(tableName, nameMissing) {
    var counter = $("table#" + tableName + " tr[id^='trNamePart_']").length;
    var part = $("table#" + tableName + " tr#trNamePart_" + counter + " input#textPersonName").attr("value");
    if (part == null || part == "") {
        alertEmptyFields(nameMissing);
        return;
    }

    var clone = $("table#" + tableName + " tr[id^='trNamePart_" + counter + "']").clone();
    clone = "<tr id='" + ("trNamePart_" + (counter + 1)) + "'>" + clone.html() + "</tr>";
    $("table#" + tableName + " tr[id^='trNamePart_" + counter + "']").after(clone);
    // Reset parameters
    if (counter == 1) {
        $("table#" + tableName + " tr#trNamePart_" + (counter + 1) + " label[for='textPersonName']").text("Part of name:");
    }
    $("table#" + tableName + " tr#trNamePart_" + (counter + 1) + " input[type='text']").each(function() {
        $(this).val(""); // Clean all input_text.
        $(this).removeAttr("name");
        $(this).attr("name", tableName + "_part_" + (counter + 1));
    });
    $("table#" + tableName + " tr#trNamePart_" + (counter + 1) + " select").each(function() {
        $(this).attr("value", ""); // Reset dropdown boxes.
        $(this).removeAttr("name");
        $(this).attr("name", tableName + "_comp_" + (counter + 1));
    });
}
;

function addDateOrDateRangeName(buttonClicked, tableName, dateLabel, fromDateLabel, toDateLabel, dateTypeLabel, knownLabel, unknownLabel, openLabel, isoLabel, invalidDateMessage, invalidRangeMessage) {
    var counter = $("table#" + tableName + " tr[id^='trDate_text_']").length;
    if (buttonClicked == "addNameDate") {
        if (counter == 0) {
            insertDateAfter(tableName, "tr#trNameForm", counter + 1, dateLabel, dateTypeLabel, knownLabel, unknownLabel, openLabel, isoLabel, invalidDateMessage, invalidRangeMessage);
        } else {
            if (dateRowNotEmpty(tableName, counter)) {
                insertDateAfter(tableName, "tr#trDate_iso_" + counter, counter + 1, dateLabel, dateTypeLabel, knownLabel, unknownLabel, openLabel, isoLabel, invalidDateMessage, invalidRangeMessage);
            } else {
                if (counter == 1) {
                    $("table#" + tableName + " tr#trDate_radio_" + counter).remove();
                    $("table#" + tableName + " tr#trDate_iso_" + counter).remove();
                    $("table#" + tableName + " tr#trDate_text_" + counter).replaceWith(insertDateAfter(tableName, "tr#trNameForm", counter, dateLabel, dateTypeLabel, knownLabel, unknownLabel, openLabel, isoLabel, invalidDateMessage, invalidRangeMessage));
                } else {
                    $("table#" + tableName + " tr#trDate_radio_" + counter).remove();
                    $("table#" + tableName + " tr#trDate_iso_" + counter).remove();
                    $("table#" + tableName + " tr#trDate_text_" + counter).replaceWith(insertDateAfter(tableName, "tr#trDate_iso_" + (counter - 1), counter, dateLabel, dateTypeLabel, knownLabel, unknownLabel, openLabel, isoLabel, invalidDateMessage, invalidRangeMessage));
                }
            }
        }
    }
    if (buttonClicked == "addNameDateRange") {
        if (counter == 0) {
            insertDateRangeAfter(tableName, "tr#trNameForm", counter + 1, fromDateLabel, toDateLabel, dateTypeLabel, knownLabel, unknownLabel, openLabel, isoLabel, invalidDateMessage, invalidRangeMessage);
        } else {
            if (dateRowNotEmpty(tableName, counter)) {
                insertDateRangeAfter(tableName, "tr#trDate_iso_" + counter, counter + 1, fromDateLabel, toDateLabel, dateTypeLabel, knownLabel, unknownLabel, openLabel, isoLabel, invalidDateMessage, invalidRangeMessage);
            } else {
                if (counter == 1) {
                    $("table#" + tableName + " tr#trDate_radio_" + counter).remove();
                    $("table#" + tableName + " tr#trDate_iso_" + counter).remove();
                    $("table#" + tableName + " tr#trDate_text_" + counter).replaceWith(insertDateRangeAfter(tableName, "tr#trNameForm", counter, fromDateLabel, toDateLabel, dateTypeLabel, knownLabel, unknownLabel, openLabel, isoLabel, invalidDateMessage, invalidRangeMessage));
                } else {
                    $("table#" + tableName + " tr#trDate_radio_" + counter).remove();
                    $("table#" + tableName + " tr#trDate_iso_" + counter).remove();
                    $("table#" + tableName + " tr#trDate_text_" + counter).replaceWith(insertDateRangeAfter(tableName, "tr#trDate_iso_" + (counter - 1), counter, fromDateLabel, toDateLabel, dateTypeLabel, knownLabel, unknownLabel, openLabel, isoLabel, invalidDateMessage, invalidRangeMessage));
                }
            }
        }
    }
    $("input#" + tableName + "_rows").val($("table#" + tableName + " tr[id^='trDate_text_']").length);
}

function addNameForm(text1, defaultLanguage) {
    var counter = $("table[id^='identityPersonName_']").length;
    var personName = $("table#identityPersonName_" + counter + " input#textPersonName").attr("value");
    if (personName == null || personName == "") {
        alertEmptyFields(text1);
        return;
    }

    //counting var for determining the total number of elements; initialized for value replication of date radiobuttons
    var idCounter = $("table#identityPersonName_" + counter + " tr[id^='trDate_text_']").length;

    //collecting of radiobutton values in array
    var dateRadioValues = new Array();
    if (idCounter > 0) {
        for (var i = 1; i <= idCounter; i++) {
            var elementCounter = $("table#identityPersonName_" + counter + " tr#trDate_text_" + i + " input[id^='date_']").length;
            for (var j = 1; j <= elementCounter; j++) {
                var dateRadioValue = new Array();
                dateRadioValue.push(i);
                dateRadioValue.push("identityPersonName_" + counter + "_date_" + j + "_radio_" + i);
                dateRadioValue.push($("table#identityPersonName_" + counter + " tr#trDate_radio_" + i + " input[name=identityPersonName_" + counter + "_date_" + j + "_radio_" + i + "]:checked").val());
                dateRadioValues.push(dateRadioValue);
            }
        }
    }

    var clone = $("table[id^='identityPersonName_" + counter + "']").clone();
    clone = "<table id='" + ("identityPersonName_" + (counter + 1)) + "' class=\"tablePadding\">" + clone.html() + "</table>";
    $("table[id^='identityPersonName_" + counter + "']").after(clone);
    $('<hr />').insertAfter($("table[id^='identityPersonName_" + counter + "']"));
    // delete superfluous name part rows
    var idCounter = $("table#identityPersonName_" + (counter + 1) + " tr[id^='trNamePart_']").length;
    if (idCounter > 1) {
        for (var i = idCounter; i > 1; i--) {
            $("table#identityPersonName_" + (counter + 1) + " tr#trNamePart_" + i).remove();
        }
    }
    //delete superfluous date rows in clone
    idCounter = $("table#identityPersonName_" + (counter + 1) + " tr[id^='trDate_text_']").length;
    if (idCounter > 0) {
        for (var i = idCounter; i >= 0; i--) {
            $("table#identityPersonName_" + (counter + 1) + " tr#trDate_text_" + i).remove();
            $("table#identityPersonName_" + (counter + 1) + " tr#trDate_radio_" + i).remove();
            $("table#identityPersonName_" + (counter + 1) + " tr#trDate_iso_" + i).remove();
        }
    }
    //Set correct names
    $("table#identityPersonName_" + (counter + 1) + " tr#trNamePart_1 input#textPersonName").removeAttr("name");
    $("table#identityPersonName_" + (counter + 1) + " tr#trNamePart_1 input#textPersonName").attr("name", "identityPersonName_" + (counter + 1) + "_part_1");
    $("table#identityPersonName_" + (counter + 1) + " tr#trNameForm select#identityNameLanguage").removeAttr("name");
    $("table#identityPersonName_" + (counter + 1) + " tr#trNameForm select#identityNameLanguage").attr("name", "identityNameLanguage_" + (counter + 1));
    $("table#identityPersonName_" + (counter + 1) + " tr#trNameForm select#identityFormOfName").removeAttr("name");
    $("table#identityPersonName_" + (counter + 1) + " tr#trNameForm select#identityFormOfName").attr("name", "identityFormOfName_" + (counter + 1));
    $("table#identityPersonName_" + (counter + 1) + " tr#trNamePart_1 select#identityComponentOfName").removeAttr("name");
    $("table#identityPersonName_" + (counter + 1) + " tr#trNamePart_1 select#identityComponentOfName").attr("name", "identityPersonName_" + (counter + 1) + "_comp_1");
    $("table#identityPersonName_" + (counter + 1) + " input#identityPersonName_" + counter + "_rows").removeAttr("name");
    $("table#identityPersonName_" + (counter + 1) + " input#identityPersonName_" + counter + "_rows").attr("name", "identityPersonName_" + (counter + 1) + "_rows");
    $("table#identityPersonName_" + (counter + 1) + " input[name^='identityPersonName_" + (counter + 1) + "_rows']").removeAttr("id");
    $("table#identityPersonName_" + (counter + 1) + " input[name^='identityPersonName_" + (counter + 1) + "_rows']").attr("id", "identityPersonName_" + (counter + 1) + "_rows");
    // Reset parameters
    $("table#identityPersonName_" + (counter + 1) + " input[type='text']").each(function() {
        $(this).val(""); // Clean all input_text.
    });
    if (defaultLanguage != null) {
        $("table#identityPersonName_" + (counter + 1) + " tr#trNameForm select#identityNameLanguage").attr("value", defaultLanguage);
    } else {
        $("table#identityPersonName_" + (counter + 1) + " tr#trNameForm select#identityNameLanguage").attr("value", "");
    }
    $("table#identityPersonName_" + (counter + 1) + " tr#trNameForm select#identityFormOfName").attr("value", "authorized");
    $("table#identityPersonName_" + (counter + 1) + " tr#trNamePart_1 select#identityComponentOfName").attr("value", "persname");
    $("table#identityPersonName_" + (counter + 1) + " input#identityPersonName_" + (counter + 1) + "_rows").attr("value", 0);
    // Finally, set correct values for original radio buttons once again
    for (var i = 0; i < dateRadioValues.length; i++) {
        $("table#identityPersonName_" + counter + " tr#trDate_radio_" + dateRadioValues[i][0] + " input[name=" + dateRadioValues[i][1] + "][value=" + dateRadioValues[i][2] + "]").attr('checked', 'checked');
    }
}

function addPersonId(identifierMissing) {
    var counter = $("table[id^='identityPersonId_']").length;
    var personId = $("table#identityPersonId_" + counter + " input#textPersonId").attr("value");
    var personTypeId = $("table#identityPersonId_" + counter + " input#textPersonTypeId").attr("value");
    if (personId == null || personId == "" || personTypeId == null || personTypeId == "") {
        alertEmptyFields(identifierMissing);
        return;
    }

    var clone = $("table[id^='identityPersonId_" + counter + "']").clone();
    clone = "<table id='" + ("identityPersonId_" + (counter + 1)) + "' class=\"tablePadding\">" + clone.html() + "</table>";
    $("table[id^='identityPersonId_" + counter + "']").after(clone);
    // Reset parameters
    $("table#identityPersonId_" + (counter + 1) + " input[type='text']").each(function() {
        $(this).val(""); // Clean all input_text.
        $(this).removeAttr("name");
    });
    //Set correct names
    $("table#identityPersonId_" + (counter + 1) + " input#textPersonId").attr("name", "textPersonId_" + (counter + 1));
    $("table#identityPersonId_" + (counter + 1) + " input#textPersonTypeId").attr("name", "textPersonTypeId_" + (counter + 1));
}

function addDateOrDateRangeExistence(buttonClicked, dateLabel, fromDateLabel, toDateLabel, dateTypeLabel, knownLabel, unknownLabel, openLabel, isoLabel, invalidDateMessage, invalidRangeMessage) {
    var counter = $("table#dateExistenceTable tr[id^='trDate_text_']").length;
    if (buttonClicked == "addExistDate") {
        if (counter == 0) {
            insertDateAfter("dateExistenceTable", "tr#trDateExistenceTableHeader", counter + 1, dateLabel, dateTypeLabel, knownLabel, unknownLabel, openLabel, isoLabel, invalidDateMessage, invalidRangeMessage);
        } else {
            if (dateRowNotEmpty("dateExistenceTable", counter)) {
                insertDateAfter("dateExistenceTable", "tr#trDate_iso_" + counter, counter + 1, dateLabel, dateTypeLabel, knownLabel, unknownLabel, openLabel, isoLabel, invalidDateMessage, invalidRangeMessage);
            } else {
                if (counter == 1) {
                    $("table#dateExistenceTable tr#trDate_radio_" + counter).remove();
                    $("table#dateExistenceTable tr#trDate_iso_" + counter).remove();
                    $("table#dateExistenceTable tr#trDate_text_" + counter).replaceWith(insertDateAfter("dateExistenceTable", "tr#trDateExistenceTableHeader", counter, dateLabel, dateTypeLabel, knownLabel, unknownLabel, openLabel, isoLabel, invalidDateMessage, invalidRangeMessage));
                } else {
                    $("table#dateExistenceTable tr#trDate_radio_" + counter).remove();
                    $("table#dateExistenceTable tr#trDate_iso_" + counter).remove();
                    $("table#dateExistenceTable tr#trDate_text_" + counter).replaceWith(insertDateAfter("dateExistenceTable", "tr#trDate_iso_" + (counter - 1), counter, dateLabel, dateTypeLabel, knownLabel, unknownLabel, openLabel, isoLabel, invalidDateMessage, invalidRangeMessage));
                }
            }
        }
    }
    if (buttonClicked == "addExistDateRange") {
        if (counter == 0) {
            insertDateRangeAfter("dateExistenceTable", "tr#trDateExistenceTableHeader", counter + 1, fromDateLabel, toDateLabel, dateTypeLabel, knownLabel, unknownLabel, openLabel, isoLabel, invalidDateMessage, invalidRangeMessage);
        } else {
            if (dateRowNotEmpty("dateExistenceTable", counter)) {
                insertDateRangeAfter("dateExistenceTable", "tr#trDate_iso_" + counter, counter + 1, fromDateLabel, toDateLabel, dateTypeLabel, knownLabel, unknownLabel, openLabel, isoLabel, invalidDateMessage, invalidRangeMessage);
            } else {
                if (counter == 1) {
                    $("table#dateExistenceTable tr#trDate_radio_" + counter).remove();
                    $("table#dateExistenceTable tr#trDate_iso_" + counter).remove();
                    $("table#dateExistenceTable tr#trDate_text_" + counter).replaceWith(insertDateRangeAfter("dateExistenceTable", "tr#trDateExistenceTableHeader", counter, fromDateLabel, toDateLabel, dateTypeLabel, knownLabel, unknownLabel, openLabel, isoLabel, invalidDateMessage, invalidRangeMessage));
                } else {
                    $("table#dateExistenceTable tr#trDate_radio_" + counter).remove();
                    $("table#dateExistenceTable tr#trDate_iso_" + counter).remove();
                    $("table#dateExistenceTable tr#trDate_text_" + counter).replaceWith(insertDateRangeAfter("dateExistenceTable", "tr#trDate_iso_" + (counter - 1), counter, fromDateLabel, toDateLabel, dateTypeLabel, knownLabel, unknownLabel, openLabel, isoLabel, invalidDateMessage, invalidRangeMessage));
                }
            }
        }
    }
    $("input#dateExistenceTable_rows").val($("table#dateExistenceTable tr[id^='trDate_text_']").length);
}

/**************************************
 * Description tab functions
 **************************************/

function togglePlaceFields(nameTf) {
    var tableName = $(nameTf).parent().parent().parent().parent().attr("id");
    var addrCompCounter = $("table#" + tableName + " tr[id^='trAddressComponent_']").length;
    var dateCounter = $("table#" + tableName + " tr[id^='trDate_text_']").length;

    if ($(nameTf).val() == "") {
        $('table#' + tableName + ' input#linkPlaceVocab').attr("disabled", "disabled");
        $('table#' + tableName + ' select#placeCountry').attr("disabled", "disabled");
        for (var i = 1; i <= addrCompCounter; i++) {
            $('table#' + tableName + " tr#trAddressComponent_" + i + ' input#addressDetails').attr("disabled", "disabled");
            $('table#' + tableName + " tr#trAddressComponent_" + i + ' select#addressComponent').attr("disabled", "disabled");
        }
        $('table#' + tableName + ' input#addAddressComponentButton').attr("disabled", "disabled");
        $('table#' + tableName + ' tr#trPlaceRole select#placeRole_1').attr("disabled", "disabled");
        for (var i = 1; i <= dateCounter; i++) {
            $('table#' + tableName + ' tr#trDate_text_' + i + ' input[type="text"]').each(function() {
                $(this).attr("disabled", "disabled");
            });
            $('table#' + tableName + ' tr#trDate_radio_' + i + ' input[type="radio"]').each(function() {
                $(this).attr("disabled", "disabled");
            });
            $('table#' + tableName + ' tr#trDate_iso_' + i + ' input[type="text"]').each(function() {
                $(this).attr("disabled", "disabled");
            });
        }
        $('table#' + tableName + ' input#addPlaceDate').attr("disabled", "disabled");
        $('table#' + tableName + ' input#addPlaceDateRange').attr("disabled", "disabled");
    } else {
        $('table#' + tableName + ' input#linkPlaceVocab').removeAttr("disabled");
        $('table#' + tableName + ' select#placeCountry').removeAttr("disabled");
        for (var i = 1; i <= addrCompCounter; i++) {
            $('table#' + tableName + " tr#trAddressComponent_" + i + ' input#addressDetails').removeAttr("disabled");
            $('table#' + tableName + " tr#trAddressComponent_" + i + ' select#addressComponent').removeAttr("disabled");
        }
        $('table#' + tableName + ' input#addAddressComponentButton').removeAttr("disabled");
        $('table#' + tableName + ' tr#trPlaceRole select#placeRole_1').removeAttr("disabled");
        for (var i = 1; i <= dateCounter; i++) {
            var elementCounter = $('table#' + tableName + ' tr#trDate_radio_' + i + ' input[id="date_"]').length;
            $('table#' + tableName + ' tr#trDate_radio_' + i + ' input[type="radio"]').each(function() {
                $(this).removeAttr("disabled");
            });
            for (var j = 1; j <= elementCounter; j++) {
                if ($('table#' + tableName + ' tr#trDate_radio_' + i + ' input[name="' + tableName + '_date_' + j + '_radio_' + i + '"]:checked').val() == "known") {
                    $('table#' + tableName + ' tr#trDate_text_' + i + ' input#date_' + j).removeAttr("disabled");
                    $('table#' + tableName + ' tr#trDate_text_' + i + ' input#date_' + j + '_Year').removeAttr("disabled");
                    $('table#' + tableName + ' tr#trDate_text_' + i + ' input#date_' + j + '_Month').removeAttr("disabled");
                    $('table#' + tableName + ' tr#trDate_text_' + i + ' input#date_' + j + '_Day').removeAttr("disabled");
                }
            }
        }
        $('table#' + tableName + ' input#addPlaceDate').removeAttr("disabled");
        $('table#' + tableName + ' input#addPlaceDateRange').removeAttr("disabled");
    }
}

function addAddressComponent(tableName, componentMissing) {
    var counter = $("table#" + tableName + " tr[id^='trAddressComponent_']").length;
    var component = $("table#" + tableName + " tr#trAddressComponent_" + counter + " input#addressDetails").attr("value");
    if (component == null || component == "") {
        alertEmptyFields(componentMissing);
        return;
    }

    var clone = $("table#" + tableName + " tr[id^='trAddressComponent_" + counter + "']").clone();
    clone = "<tr id='" + ("trAddressComponent_" + (counter + 1)) + "'>" + clone.html() + "</tr>";
    $("table#" + tableName + " tr[id^='trAddressComponent_" + counter + "']").after(clone);
    // Reset parameters
    $("table#" + tableName + " tr#trAddressComponent_" + (counter + 1) + " input[type='text']").each(function() {
        $(this).val(""); // Clean all input_text.
        $(this).removeAttr("name");
        $(this).attr("name", tableName + "_addressDetails_" + (counter + 1));
    });
    $("table#" + tableName + " tr#trAddressComponent_" + (counter + 1) + " select").each(function() {
        $(this).attr("value", ""); // Reset dropdown boxes.
        $(this).removeAttr("name");
        $(this).attr("name", tableName + "_addressComponent_" + (counter + 1));
    });
}

function addDateOrDateRangePlace(buttonClicked, tableName, dateLabel, fromDateLabel, toDateLabel, dateTypeLabel, knownLabel, unknownLabel, openLabel, isoLabel, invalidDateMessage, invalidRangeMessage) {
    var counter = $("table#" + tableName + " tr[id^='trDate_text_']").length;
    if (buttonClicked == "addPlaceDate") {
        if (counter == 0) {
            insertDateAfter(tableName, "tr#trPlaceRole", counter + 1, dateLabel, dateTypeLabel, knownLabel, unknownLabel, openLabel, isoLabel, invalidDateMessage, invalidRangeMessage);
        } else {
            if (dateRowNotEmpty(tableName, counter)) {
                insertDateAfter(tableName, "tr#trDate_iso_" + counter, counter + 1, dateLabel, dateTypeLabel, knownLabel, unknownLabel, openLabel, isoLabel, invalidDateMessage, invalidRangeMessage);
            } else {
                if (counter == 1) {
                    $("table#" + tableName + " tr#trDate_radio_" + counter).remove();
                    $("table#" + tableName + " tr#trDate_iso_" + counter).remove();
                    $("table#" + tableName + " tr#trDate_text_" + counter).replaceWith(insertDateAfter(tableName, "tr#trPlaceRole", counter, dateLabel, dateTypeLabel, knownLabel, unknownLabel, openLabel, isoLabel, invalidDateMessage, invalidRangeMessage));
                } else {
                    $("table#" + tableName + " tr#trDate_radio_" + counter).remove();
                    $("table#" + tableName + " tr#trDate_iso_" + counter).remove();
                    $("table#" + tableName + " tr#trDate_text_" + counter).replaceWith(insertDateAfter(tableName, "tr#trDate_iso_" + (counter - 1), counter, dateLabel, dateTypeLabel, knownLabel, unknownLabel, openLabel, isoLabel, invalidDateMessage, invalidRangeMessage));
                }
            }
        }
    }
    if (buttonClicked == "addPlaceDateRange") {
        if (counter == 0) {
            insertDateRangeAfter(tableName, "tr#trPlaceRole", counter + 1, fromDateLabel, toDateLabel, dateTypeLabel, knownLabel, unknownLabel, openLabel, isoLabel, invalidDateMessage, invalidRangeMessage);
        } else {
            if (dateRowNotEmpty(tableName, counter)) {
                insertDateRangeAfter(tableName, "tr#trDate_iso_" + counter, counter + 1, fromDateLabel, toDateLabel, dateTypeLabel, knownLabel, unknownLabel, openLabel, isoLabel, invalidDateMessage, invalidRangeMessage);
            } else {
                if (counter == 1) {
                    $("table#" + tableName + " tr#trDate_radio_" + counter).remove();
                    $("table#" + tableName + " tr#trDate_iso_" + counter).remove();
                    $("table#" + tableName + " tr#trDate_text_" + counter).replaceWith(insertDateRangeAfter(tableName, "tr#trPlaceRole", counter, fromDateLabel, toDateLabel, dateTypeLabel, knownLabel, unknownLabel, openLabel, isoLabel, invalidDateMessage, invalidRangeMessage));
                } else {
                    $("table#" + tableName + " tr#trDate_radio_" + counter).remove();
                    $("table#" + tableName + " tr#trDate_iso_" + counter).remove();
                    $("table#" + tableName + " tr#trDate_text_" + counter).replaceWith(insertDateRangeAfter(tableName, "tr#trDate_iso_" + (counter - 1), counter, fromDateLabel, toDateLabel, dateTypeLabel, knownLabel, unknownLabel, openLabel, isoLabel, invalidDateMessage, invalidRangeMessage));
                }
            }
        }
    }
    $("table#" + tableName + " input#" + tableName + "_rows").val($("table#" + tableName + " tr[id^='trDate_text_']").length);
}

function addPlace(defaultLanguage, placeMissing) {
    var counter = $("table[id^='placeTable_']").length;
    var place = $("table#placeTable_" + counter + " input#place").attr("value");
    if (place == null || place == "") {
        alertEmptyFields(placeMissing);
        return;
    }

    //counting var for determining the total number of elements; initialized for value replication of date radiobuttons
    var idCounter = $("table#placeTable_" + counter + " tr[id^='trDate_text_']").length;

    //collecting of radiobutton values in array
    var dateRadioValues = new Array();
    if (idCounter > 0) {
        for (var i = 1; i <= idCounter; i++) {
            var elementCounter = $("table#placeTable_" + counter + " tr#trDate_text_" + i + " input[id^='date_']").length;
            for (var j = 1; j <= elementCounter; j++) {
                var dateRadioValue = new Array();
                dateRadioValue.push(i);
                dateRadioValue.push("placeTable_" + counter + "_date_" + j + "_radio_" + i);
                dateRadioValue.push($("table#placeTable_" + counter + " tr#trDate_radio_" + i + " input[name=placeTable_" + counter + "_date_" + j + "_radio_" + i + "]:checked").val());
                dateRadioValues.push(dateRadioValue);
            }
        }
    }

    var clone = $("table[id^='placeTable_" + counter + "']").clone();
    clone = "<table id='" + ("placeTable_" + (counter + 1)) + "' class=\"tablePadding\">" + clone.html() + "</table>";
    $("table[id^='placeTable_" + counter + "']").after(clone);
    // rename header of clone
    $("table#placeTable_" + (counter + 1) + " th#thPlaceTableHeader").html("Place " + (counter + 1));
    // delete superfluous address component rows
    idCounter = $("table#placeTable_" + (counter + 1) + " tr[id^='trAddressComponent_']").length;
    if (idCounter > 1) {
        for (var i = idCounter; i > 1; i--) {
            $("table#placeTable_" + (counter + 1) + " tr#trAddressComponent_" + i).remove();
        }
    }
    //delete superfluous date rows in clone
    idCounter = $("table#placeTable_" + (counter + 1) + " tr[id^='trDate_text_']").length;
    if (idCounter > 0) {
        for (var i = idCounter; i >= 0; i--) {
            $("table#placeTable_" + (counter + 1) + " tr#trDate_text_" + i).remove();
            $("table#placeTable_" + (counter + 1) + " tr#trDate_radio_" + i).remove();
            $("table#placeTable_" + (counter + 1) + " tr#trDate_iso_" + i).remove();
        }
    }

    // Reset parameters
    $("table#placeTable_" + (counter + 1) + " input[type='text']").each(function() {
        $(this).val(""); // Clean all input_text.
        $(this).removeAttr("name"); //remove old name
    });
    $("table#placeTable_" + (counter + 1) + " select").each(function() {
        $(this).attr("value", ""); // Reset dropdown boxes.
        $(this).removeAttr("name"); //remove old name
    });
    // Set correct names
    $("table#placeTable_" + (counter + 1) + " input#place").attr("name", "place_" + (counter + 1));
    $("table#placeTable_" + (counter + 1) + " select#placeLanguage").attr("name", "placeLanguage_" + (counter + 1));
    $("table#placeTable_" + (counter + 1) + " input#linkPlaceVocab").attr("name", "linkPlaceVocab_" + (counter + 1));
    $("table#placeTable_" + (counter + 1) + " select#placeCountry").attr("name", "placeCountry_" + (counter + 1));
    $("table#placeTable_" + (counter + 1) + " input#addressDetails").attr("name", "placeTable_" + (counter + 1) + "_addressDetails_1");
    $("table#placeTable_" + (counter + 1) + " select#addressComponent").attr("name", "placeTable_" + (counter + 1) + "_addressComponent_1");
    $("table#placeTable_" + (counter + 1) + " input#placeRole").attr("name", "placeRole_" + (counter + 1));
    // Set elements as inactive
    $("table#placeTable_" + (counter + 1) + " input#linkPlaceVocab").attr("disabled", "disabled");
    $("table#placeTable_" + (counter + 1) + " select#placeCountry").attr("disabled", "disabled");
    $("table#placeTable_" + (counter + 1) + " input#addressDetails").attr("disabled", "disabled");
    $("table#placeTable_" + (counter + 1) + " select#addressComponent").attr("disabled", "disabled");
    $("table#placeTable_" + (counter + 1) + " input#placeRole").attr("disabled", "disabled");
    $("table#placeTable_" + (counter + 1) + " input#addAddressComponentButton").attr("disabled", "disabled");
    $("table#placeTable_" + (counter + 1) + " input#addPlaceDate").attr("disabled", "disabled");
    $("table#placeTable_" + (counter + 1) + " input#addPlaceDateRange").attr("disabled", "disabled");
    // Set correct params to hidden counter
    $("table#placeTable_" + (counter + 1) + " input[type='hidden']").removeAttr("id");
    $("table#placeTable_" + (counter + 1) + " input[type='hidden']").removeAttr("name");
    $("table#placeTable_" + (counter + 1) + " input[type='hidden']").attr("id", "placeTable_" + (counter + 1) + "_rows");
    $("table#placeTable_" + (counter + 1) + " input[type='hidden']").attr("name", "placeTable_" + (counter + 1) + "_rows");
    $("table#placeTable_" + (counter + 1) + " input[type='hidden']").val("0")
    // If default language given, set language field to this value
    if (defaultLanguage != null) {
        $("table#placeTable_" + (counter + 1) + " select#placeLanguage").attr("value", defaultLanguage);
    }
    // Finally, set correct values for original radio buttons once again
    for (var i = 0; i < dateRadioValues.length; i++) {
        $("table#placeTable_" + counter + " tr#trDate_radio_" + dateRadioValues[i][0] + " input[name=" + dateRadioValues[i][1] + "][value=" + dateRadioValues[i][2] + "]").attr('checked', 'checked');
    }
}

function addPlaceFunction(tableName, placeMissing) {
    var counter = $("table#" + tableName + " tr[id^='trPlaceFunction_']").length;
    var place = $("table#" + tableName + " tr#trPlaceFunction_" + counter + " input#textPlaceFunction").attr("value");
    if (place == null || place == "") {
        alertEmptyFields(placeMissing);
        return;
    }

    var clone = $("table#" + tableName + " tr[id^='trPlaceFunction_" + counter + "']").clone();
    clone = "<tr id='" + ("trPlaceFunction_" + (counter + 1)) + "'>" + clone.html() + "</tr>";
    $("table#" + tableName + " tr[id^='trPlaceFunction_" + counter + "']").after(clone);
    // Reset parameters
    $("table#" + tableName + " tr#trPlaceFunction_" + (counter + 1) + " input[type='text']").each(function() {
        $(this).val(""); // Clean all input_text.
        $(this).removeAttr("name");
        $(this).attr("name", tableName + "_" + $(this).attr("id") + "_" + (counter + 1));
    });
    $("table#" + tableName + " tr#trPlaceFunction_" + (counter + 1) + " select").each(function() {
        $(this).attr("value", ""); // Reset dropdown boxes.
        $(this).removeAttr("name");
        $(this).attr("name", tableName + "_" + $(this).attr("id") + "_" + (counter + 1));
    });
}

function addDateOrDateRangeFunction(buttonClicked, tableName, dateLabel, fromDateLabel, toDateLabel, dateTypeLabel, knownLabel, unknownLabel, openLabel, isoLabel, invalidDateMessage, invalidRangeMessage) {
    var counter = $("table#" + tableName + " tr[id^='trDate_text_']").length;
    if (buttonClicked == "addFunctionDate") {
        if (counter == 0) {
            insertDateAfter(tableName, "tr#trPlaceFunctionButton", counter + 1, dateLabel, dateTypeLabel, knownLabel, unknownLabel, openLabel, isoLabel, invalidDateMessage, invalidRangeMessage);
        } else {
            if (dateRowNotEmpty(tableName, counter)) {
                insertDateAfter(tableName, "tr#trDate_iso_" + counter, counter + 1, dateLabel, dateTypeLabel, knownLabel, unknownLabel, openLabel, isoLabel, invalidDateMessage, invalidRangeMessage);
            } else {
                if (counter == 1) {
                    $("table#" + tableName + " tr#trDate_radio_" + counter).remove();
                    $("table#" + tableName + " tr#trDate_iso_" + counter).remove();
                    $("table#" + tableName + " tr#trDate_text_" + counter).replaceWith(insertDateAfter(tableName, "tr#trPlaceFunctionButton", counter, dateLabel, dateTypeLabel, knownLabel, unknownLabel, openLabel, isoLabel, invalidDateMessage, invalidRangeMessage));
                } else {
                    $("table#" + tableName + " tr#trDate_radio_" + counter).remove();
                    $("table#" + tableName + " tr#trDate_iso_" + counter).remove();
                    $("table#" + tableName + " tr#trDate_text_" + counter).replaceWith(insertDateAfter(tableName, "tr#trDate_iso_" + (counter - 1), counter, dateLabel, dateTypeLabel, knownLabel, unknownLabel, openLabel, isoLabel, invalidDateMessage, invalidRangeMessage));
                }
            }
        }
    }
    if (buttonClicked == "addFunctionDateRange") {
        if (counter == 0) {
            insertDateRangeAfter(tableName, "tr#trPlaceFunctionButton", counter + 1, fromDateLabel, toDateLabel, dateTypeLabel, knownLabel, unknownLabel, openLabel, isoLabel, invalidDateMessage, invalidRangeMessage);
        } else {
            if (dateRowNotEmpty(tableName, counter)) {
                insertDateRangeAfter(tableName, "tr#trDate_iso_" + counter, counter + 1, fromDateLabel, toDateLabel, dateTypeLabel, knownLabel, unknownLabel, openLabel, isoLabel, invalidDateMessage, invalidRangeMessage);
            } else {
                if (counter == 1) {
                    $("table#" + tableName + " tr#trDate_radio_" + counter).remove();
                    $("table#" + tableName + " tr#trDate_iso_" + counter).remove();
                    $("table#" + tableName + " tr#trDate_text_" + counter).replaceWith(insertDateRangeAfter(tableName, "tr#trPlaceFunctionButton", counter, fromDateLabel, toDateLabel, dateTypeLabel, knownLabel, unknownLabel, openLabel, isoLabel, invalidDateMessage, invalidRangeMessage));
                } else {
                    $("table#" + tableName + " tr#trDate_radio_" + counter).remove();
                    $("table#" + tableName + " tr#trDate_iso_" + counter).remove();
                    $("table#" + tableName + " tr#trDate_text_" + counter).replaceWith(insertDateRangeAfter(tableName, "tr#trDate_iso_" + (counter - 1), counter, fromDateLabel, toDateLabel, dateTypeLabel, knownLabel, unknownLabel, openLabel, isoLabel, invalidDateMessage, invalidRangeMessage));
                }
            }
        }
    }
    $("input#" + tableName + "_rows").val($("table#" + tableName + " tr[id^='trDate_text']").length);
}

function addFunction(defaultLanguage, functionMissing) {
    var counter = $("table[id^='functionTable_']").length;
    var functionName = $("table#functionTable_" + counter + " input#function").attr("value");
    if (functionName == null || functionName == "") {
        alertEmptyFields(functionMissing);
        return;
    }

    //counting var for determining the total number of elements; initialized for value replication of date radiobuttons
    var idCounter = $("table#functionTable_" + counter + " tr[id^='trDate_text_']").length;

    //collecting of radiobutton values in array
    var dateRadioValues = new Array();
    if (idCounter > 0) {
        for (var i = 1; i <= idCounter; i++) {
            var elementCounter = $("table#functionTable_" + counter + " tr#trDate_text_" + i + " input[id^='date_']").length;
            for (var j = 1; j <= elementCounter; j++) {
                var dateRadioValue = new Array();
                dateRadioValue.push(i);
                dateRadioValue.push("functionTable_" + counter + "_date_" + j + "_radio_" + i);
                dateRadioValue.push($("table#functionTable_" + counter + " tr#trDate_radio_" + i + " input[name=functionTable_" + counter + "_date_" + j + "_radio_" + i + "]:checked").val());
                dateRadioValues.push(dateRadioValue);
            }
        }
    }

    var clone = $("table[id^='functionTable_" + counter + "']").clone();
    clone = "<table id='" + ("functionTable_" + (counter + 1)) + "' class=\"tablePadding\">" + clone.html() + "</table>";
    $("table[id^='functionTable_" + counter + "']").after(clone);
    // rename header of clone
    $("table#functionTable_" + (counter + 1) + " th#thFunctionTableHeader").html("Function " + (counter + 1));
    // delete superfluous place rows
    var idCounter = $("table#functionTable_" + (counter + 1) + " tr[id^='trPlaceFunction_']").length;
    if (idCounter > 1) {
        for (var i = idCounter; i > 1; i--) {
            $("table#functionTable_" + (counter + 1) + " tr#trPlaceFunction_" + i).remove();
        }
    }
    //delete superfluous date rows in clone
    idCounter = $("table#functionTable_" + (counter + 1) + " tr[id^='trDate_text_']").length;
    if (idCounter > 0) {
        for (var i = idCounter; i >= 0; i--) {
            $("table#functionTable_" + (counter + 1) + " tr#trDate_text_" + i).remove();
            $("table#functionTable_" + (counter + 1) + " tr#trDate_radio_" + i).remove();
            $("table#functionTable_" + (counter + 1) + " tr#trDate_iso_" + i).remove();
        }
    }
    // Reset parameters
    $("table#functionTable_" + (counter + 1) + " input[type='text']").each(function() {
        $(this).val(""); // Clean all input_text.
        $(this).removeAttr("name");
    });
    $("table#functionTable_" + (counter + 1) + " select").each(function() {
        $(this).attr("value", ""); // Reset dropdown boxes.
        $(this).removeAttr("name");
    });
    $("table#functionTable_" + (counter + 1) + " textarea").val("");
    $("table#functionTable_" + (counter + 1) + " textarea").removeAttr("name");
    // Set correct names
    $("table#functionTable_" + (counter + 1) + " input#function").attr("name", "function_" + (counter + 1));
    $("table#functionTable_" + (counter + 1) + " select#functionLanguage").attr("name", "functionLanguage_" + (counter + 1));
    $("table#functionTable_" + (counter + 1) + " input#linkFunctionVocab").attr("name", "linkFunctionVocab_" + (counter + 1));
    $("table#functionTable_" + (counter + 1) + " textarea#functionDescription").attr("name", "functionDescription_" + (counter + 1));
    $("table#functionTable_" + (counter + 1) + " input#textPlaceFunction").attr("name", "functionTable_" + (counter + 1) + "_place_1");
    $("table#functionTable_" + (counter + 1) + " select#functionCountry").attr("name", "functionTable_" + (counter + 1) + "_country_1");
    // Set correct params to hidden counter
    $("table#functionTable_" + (counter + 1) + " input[type='hidden']").removeAttr("id");
    $("table#functionTable_" + (counter + 1) + " input[type='hidden']").removeAttr("name");
    $("table#functionTable_" + (counter + 1) + " input[type='hidden']").attr("id", "functionTable_" + (counter + 1) + "_rows");
    $("table#functionTable_" + (counter + 1) + " input[type='hidden']").attr("name", "functionTable_" + (counter + 1) + "_rows");
    $("table#functionTable_" + (counter + 1) + " input[type='hidden']").val("0")
    // If default language given, set language field to this value
    if (defaultLanguage != null) {
        $("table#functionTable_" + (counter + 1) + " select#functionLanguage").attr("value", defaultLanguage);
    }
    // Finally, set correct values for original radio buttons once again
    for (var i = 0; i < dateRadioValues.length; i++) {
        $("table#functionTable_" + counter + " tr#trDate_radio_" + dateRadioValues[i][0] + " input[name=" + dateRadioValues[i][1] + "][value=" + dateRadioValues[i][2] + "]").attr('checked', 'checked');
    }
}

function addPlaceOccupation(tableName, placeMissing) {
    var counter = $("table#" + tableName + " tr[id^='trPlaceOccupation_']").length;
    var place = $("table#" + tableName + " tr#trPlaceOccupation_" + counter + " input#textPlaceOccupation").attr("value");
    if (place == null || place == "") {
        alertEmptyFields(placeMissing);
        return;
    }

    var clone = $("table#" + tableName + " tr[id^='trPlaceOccupation_" + counter + "']").clone();
    clone = "<tr id='" + ("trPlaceOccupation_" + (counter + 1)) + "'>" + clone.html() + "</tr>";
    $("table#" + tableName + " tr[id^='trPlaceOccupation_" + counter + "']").after(clone);
    // Reset parameters
    $("table#" + tableName + " tr#trPlaceOccupation_" + (counter + 1) + " input[type='text']").each(function() {
        $(this).val(""); // Clean all input_text.
        $(this).removeAttr("name");
        $(this).attr("name", tableName + "_" + $(this).attr("id") + "_" + (counter + 1));
    });
    $("table#" + tableName + " tr#trPlaceOccupation_" + (counter + 1) + " select").each(function() {
        $(this).attr("value", ""); // Reset dropdown boxes.
        $(this).removeAttr("name");
        $(this).attr("name", tableName + "_" + $(this).attr("id") + "_" + (counter + 1));
    });
}

function addDateOrDateRangeOccupation(buttonClicked, tableName, dateLabel, fromDateLabel, toDateLabel, dateTypeLabel, knownLabel, unknownLabel, openLabel, isoLabel, invalidDateMessage, invalidRangeMessage) {
    var counter = $("table#" + tableName + " tr[id^='trDate_text_']").length;
    if (buttonClicked == "addOccupationDate") {
        if (counter == 0) {
            insertDateAfter(tableName, "tr#trPlaceOccupationButton", counter + 1, dateLabel, dateTypeLabel, knownLabel, unknownLabel, openLabel, isoLabel, invalidDateMessage, invalidRangeMessage);
        } else {
            if (dateRowNotEmpty(tableName, counter)) {
                insertDateAfter(tableName, "tr#trDate_iso_" + counter, counter + 1, dateLabel, dateTypeLabel, knownLabel, unknownLabel, openLabel, isoLabel, invalidDateMessage, invalidRangeMessage);
            } else {
                if (counter == 1) {
                    $("table#" + tableName + " tr#trDate_radio_" + counter).remove();
                    $("table#" + tableName + " tr#trDate_iso_" + counter).remove();
                    $("table#" + tableName + " tr#trDate_text_" + counter).replaceWith(insertDateAfter(tableName, "tr#trPlaceOccupationButton", counter, dateLabel, dateTypeLabel, knownLabel, unknownLabel, openLabel, isoLabel, invalidDateMessage, invalidRangeMessage));
                } else {
                    $("table#" + tableName + " tr#trDate_radio_" + counter).remove();
                    $("table#" + tableName + " tr#trDate_iso_" + counter).remove();
                    $("table#" + tableName + " tr#trDate_text_" + counter).replaceWith(insertDateAfter(tableName, "tr#trDate_iso_" + (counter - 1), counter, dateLabel, dateTypeLabel, knownLabel, unknownLabel, openLabel, isoLabel, invalidDateMessage, invalidRangeMessage));
                }
            }
        }
    }
    if (buttonClicked == "addOccupationDateRange") {
        if (counter == 0) {
            insertDateRangeAfter(tableName, "tr#trPlaceOccupationButton", counter + 1, fromDateLabel, toDateLabel, dateTypeLabel, knownLabel, unknownLabel, openLabel, isoLabel, invalidDateMessage, invalidRangeMessage);
        } else {
            if (dateRowNotEmpty(tableName, counter)) {
                insertDateRangeAfter(tableName, "tr#trDate_iso_" + counter, counter + 1, fromDateLabel, toDateLabel, dateTypeLabel, knownLabel, unknownLabel, openLabel, isoLabel, invalidDateMessage, invalidRangeMessage);
            } else {
                if (counter == 1) {
                    $("table#" + tableName + " tr#trDate_radio_" + counter).remove();
                    $("tr#trDate_iso_" + counter).remove();
                    $("table#" + tableName + " tr#trDate_text_" + counter).replaceWith(insertDateRangeAfter(tableName, "tr#trPlaceOccupationButton", counter, fromDateLabel, toDateLabel, dateTypeLabel, knownLabel, unknownLabel, openLabel, isoLabel, invalidDateMessage, invalidRangeMessage));
                } else {
                    $("table#" + tableName + " tr#trDate_radio_" + counter).remove();
                    $("tr#trDate_iso_" + counter).remove();
                    $("table#" + tableName + " tr#trDate_text_" + counter).replaceWith(insertDateRangeAfter(tableName, "tr#trDate_iso_" + (counter - 1), counter, fromDateLabel, toDateLabel, dateTypeLabel, knownLabel, unknownLabel, openLabel, isoLabel, invalidDateMessage, invalidRangeMessage));
                }
            }
        }
    }
    $("input#" + tableName + "_rows").val($("table#" + tableName + " tr[id^='trDate_text_']").length);
}

function addOccupation(defaultLanguage, occupationMissing) {
    var counter = $("table[id^='occupationTable_']").length;
    var occupation = $("table#occupationTable_" + counter + " input#occupation").attr("value");
    if (occupation == null || occupation == "") {
        alertEmptyFields(occupationMissing);
        return;
    }

    //counting var for determining the total number of elements; initialized for value replication of date radiobuttons
    var idCounter = $("table#occupationTable_" + counter + " tr[id^='trDate_text_']").length;

    //collecting of radiobutton values in array
    var dateRadioValues = new Array();
    if (idCounter > 0) {
        for (var i = 1; i <= idCounter; i++) {
            var elementCounter = $("table#occupationTable_" + counter + " tr#trDate_text_" + i + " input[id^='date_']").length;
            for (var j = 1; j <= elementCounter; j++) {
                var dateRadioValue = new Array();
                dateRadioValue.push(i);
                dateRadioValue.push("occupationTable_" + counter + "_date_" + j + "_radio_" + i);
                dateRadioValue.push($("table#occupationTable_" + counter + " tr#trDate_radio_" + i + " input[name=occupationTable_" + counter + "_date_" + j + "_radio_" + i + "]:checked").val());
                dateRadioValues.push(dateRadioValue);
            }
        }
    }

    var clone = $("table[id^='occupationTable_" + counter + "']").clone();
    clone = "<table id='" + ("occupationTable_" + (counter + 1)) + "' class=\"tablePadding\">" + clone.html() + "</table>";
    $("table[id^='occupationTable_" + counter + "']").after(clone);
    // rename header of clone
    $("table#occupationTable_" + (counter + 1) + " th#thOccupationTableHeader").html("Occupation " + (counter + 1));
    // delete superfluous place rows
    var idCounter = $("table#occupationTable_" + (counter + 1) + " tr[id^='trPlaceOccupation_']").length;
    if (idCounter > 1) {
        for (var i = idCounter; i > 1; i--) {
            $("table#occupationTable_" + (counter + 1) + " tr#trPlaceOccupation_" + i).remove();
        }
    }
    //delete superfluous date rows in clone
    idCounter = $("table#occupationTable_" + (counter + 1) + " tr[id^='trDate_text_']").length;
    if (idCounter > 0) {
        for (var i = idCounter; i >= 0; i--) {
            $("table#occupationTable_" + (counter + 1) + " tr#trDate_text_" + i).remove();
            $("table#occupationTable_" + (counter + 1) + " tr#trDate_radio_" + i).remove();
            $("table#occupationTable_" + (counter + 1) + " tr#trDate_iso_" + i).remove();
        }
    }
    // Reset parameters
    $("table#occupationTable_" + (counter + 1) + " input[type='text']").each(function() {
        $(this).val(""); // Clean all input_text.
        $(this).removeAttr("name");
    });
    $("table#occupationTable_" + (counter + 1) + " select").each(function() {
        $(this).attr("value", ""); // Reset dropdown boxes.
        $(this).removeAttr("name");
    });
    $("table#occupationTable_" + (counter + 1) + " textarea").val("");
    $("table#occupationTable_" + (counter + 1) + " textarea").removeAttr("name");
    // Set correct names
    $("table#occupationTable_" + (counter + 1) + " input#occupation").attr("name", "occupation_" + (counter + 1));
    $("table#occupationTable_" + (counter + 1) + " select#occupationLanguage").attr("name", "occupationLanguage_" + (counter + 1));
    $("table#occupationTable_" + (counter + 1) + " input#linkOccupationVocab").attr("name", "linkOccupationVocab_" + (counter + 1));
    $("table#occupationTable_" + (counter + 1) + " textarea#occupationDescription").attr("name", "occupationDescription_" + (counter + 1));
    $("table#occupationTable_" + (counter + 1) + " input#textPlaceOccupation").attr("name", "occupationTable_" + (counter + 1) + "_place_1");
    $("table#occupationTable_" + (counter + 1) + " select#occupationCountry").attr("name", "occupationTable_" + (counter + 1) + "_country_1");
    // Set correct params to hidden counter
    $("table#occupationTable_" + (counter + 1) + " input[type='hidden']").removeAttr("id");
    $("table#occupationTable_" + (counter + 1) + " input[type='hidden']").removeAttr("name");
    $("table#occupationTable_" + (counter + 1) + " input[type='hidden']").attr("id", "occupationTable_" + (counter + 1) + "_rows");
    $("table#occupationTable_" + (counter + 1) + " input[type='hidden']").attr("name", "occupationTable_" + (counter + 1) + "_rows");
    $("table#occupationTable_" + (counter + 1) + " input[type='hidden']").val("0")
    // If default language given, set language field to this value
    if (defaultLanguage != null) {
        $("table#occupationTable_" + (counter + 1) + " select#occupationLanguage").attr("value", defaultLanguage);
    }
    // Finally, set correct values for original radio buttons once again
    for (var i = 0; i < dateRadioValues.length; i++) {
        $("table#occupationTable_" + counter + " tr#trDate_radio_" + dateRadioValues[i][0] + " input[name=" + dateRadioValues[i][1] + "][value=" + dateRadioValues[i][2] + "]").attr('checked', 'checked');
    }
}

function addGenealogy(genealogyMissing) {
    var counter = $("[id^='genealogyContent_']").length;
    var genealogy = $("table#genealogyContent_" + counter + " textarea").attr("value");
    if (genealogy == null || genealogy == "") {
        alertEmptyFields(genealogyMissing);
        return;
    }

    var clone = $("table[id^='genealogyContent_" + counter + "']").clone();
    clone = "<table id='" + ("genealogyContent_" + (counter + 1)) + "' class=\"tablePadding\">" + clone.html() + "</table>";
    $("table[id^='genealogyContent_" + counter + "']").after(clone);
    // Set correct names
    $("table#genealogyContent_" + (counter + 1)).each(function() {
        $(this).removeAttr("name");
    });
    $("table#genealogyContent_" + (counter + 1) + " textarea").attr("name", "genealogyDescription_" + (counter + 1));
    $("table#genealogyContent_" + (counter + 1) + " select").attr("name", "genealogyLanguage_" + (counter + 1));
    // Reset parameters
    $("table#genealogyContent_" + (counter + 1) + " textarea").val("");
    $("table#genealogyContent_" + (counter + 1) + " select").each(function() {
        $(this).attr("value", ""); // Reset dropdown boxes.
    });
}

function addBiography(biographyMissing) {
    var counter = $("table[id^='biographyContent_']").length;
    var biography = $("table#biographyContent_" + counter + " textarea").attr("value");
    if (biography == null || biography == "") {
        alertEmptyFields(biographyMissing);
        return;
    }

    var clone = $("table[id^='biographyContent_" + counter + "']").clone();
    clone = "<table id='" + ("biographyContent_" + (counter + 1)) + "' class=\"tablePadding\">" + clone.html() + "</table>";
    $("table[id^='biographyContent_" + counter + "']").after(clone);
    // Set correct names
    $("table#biographyContent_" + (counter + 1)).each(function() {
        $(this).removeAttr("name");
    });
    $("table#biographyContent_" + (counter + 1) + " textarea").attr("name", "biographyDescription_" + (counter + 1));
    $("table#biographyContent_" + (counter + 1) + " select").attr("name", "biographyLanguage_" + (counter + 1));
    // Reset parameters
    $("table#biographyContent_" + (counter + 1) + " textarea").val("");
    $("table#biographyContent_" + (counter + 1) + " select").each(function() {
        $(this).attr("value", ""); // Reset dropdown boxes.
    });
}

/**************************************
 * Relations tab functions
 **************************************/

function addCpfRelation(defaultLanguage, relationMissing) {
    var counter = $("table[id^='cpfRelationsTable_']").length;
    var relation = $("table#cpfRelationsTable_" + counter + " input#textCpfRelationName").attr("value");
    if (relation == null || relation == "") {
        alertEmptyFields(relationMissing);
        return;
    }

    var clone = $("table[id^='cpfRelationsTable_" + counter + "']").clone();
    clone = "<table id='" + ("cpfRelationsTable_" + (counter + 1)) + "' class=\"tablePadding\">" + clone.html() + "</table>";
    $("table[id^='cpfRelationsTable_" + counter + "']").after(clone);
    // rename header of clone
    $("table#cpfRelationsTable_" + (counter + 1) + " th#thCpfRelTableHeader").html("CPF relation " + (counter + 1));
    // delete superfluous identifier rows
    var idCounter = $("table#cpfRelationsTable_" + (counter + 1) + " tr[id^='trCpfRelationRespOrg_']").length;
    if (idCounter > 1) {
        for (var i = idCounter; i > 1; i--) {
            $("table#cpfRelationsTable_" + (counter + 1) + " tr#trCpfRelationRespOrg_" + i).remove();
        }
    }
// Set correct names
    $("table#cpfRelationsTable_" + (counter + 1)).each(function() {
        $(this).removeAttr("name");
    });
    $("table#cpfRelationsTable_" + (counter + 1) + " input#textCpfRelationName").attr("name", "textCpfRelationName_" + (counter + 1));
    $("table#cpfRelationsTable_" + (counter + 1) + " select#cpfRelationLanguage").attr("name", "cpfRelationLanguage_" + (counter + 1));
    $("table#cpfRelationsTable_" + (counter + 1) + " input#textCpfRelationId").attr("name", "textCpfRelationId_" + (counter + 1));
    $("table#cpfRelationsTable_" + (counter + 1) + " input#textCpfRelationLink").attr("name", "textCpfRelationLink_" + (counter + 1));
    $("table#cpfRelationsTable_" + (counter + 1) + " select#cpfRelationType").attr("name", "cpfRelationType_" + (counter + 1));
    $("table#cpfRelationsTable_" + (counter + 1) + " textarea#textareaCpfRelationDescription").attr("name", "textareaCpfRelationDescription_" + (counter + 1));
    $("table#cpfRelationsTable_" + (counter + 1) + " input#textCpfRelRespOrgPerson").attr("name", "cpfRelationsTable_" + (counter + 1) + "_textCpfRelRespOrgPerson_1");
    $("table#cpfRelationsTable_" + (counter + 1) + " input#textCpfRelRespOrgId").attr("name", "cpfRelationsTable_" + (counter + 1) + "_textCpfRelRespOrgId_1");
    // Reset parameters
    $("table#cpfRelationsTable_" + (counter + 1) + " input[type='text']").each(function() {
        $(this).val(""); // Clean all input_text.
    });
    $("table#cpfRelationsTable_" + (counter + 1) + " select").each(function() {
        $(this).attr("value", ""); // Reset dropdown boxes.
    });
    $("table#cpfRelationsTable_" + (counter + 1) + " textarea").val("");
    // If default language given, set language field to this value
    if (defaultLanguage != null) {
        $("table#cpfRelationsTable_" + (counter + 1) + " select#cpfRelationLanguage").attr("value", defaultLanguage);
    }
}

function addResRelation(defaultLanguage, relationMissing) {
    var counter = $("table[id^='resRelationsTable_']").length;
    var relation = $("table#resRelationsTable_" + counter + " input#textResRelationName").attr("value");
    if (relation == null || relation == "") {
        alertEmptyFields(relationMissing);
        return;
    }

    var clone = $("table[id^='resRelationsTable_" + counter + "']").clone();
    clone = "<table id='" + ("resRelationsTable_" + (counter + 1)) + "' class=\"tablePadding\">" + clone.html() + "</table>";
    $("table[id^='resRelationsTable_" + counter + "']").after(clone);
    // rename header of clone
    $("table#resRelationsTable_" + (counter + 1) + " th#thResRelTableHeader").html("Resource relation " + (counter + 1));
    // delete superfluous identifier rows
    var idCounter = $("table#resRelationsTable_" + (counter + 1) + " tr[id^='trResRelationRespOrg_']").length;
    if (idCounter > 1) {
        for (var i = idCounter; i > 1; i--) {
            $("table#resRelationsTable_" + (counter + 1) + " tr#trResRelationRespOrg_" + i).remove();
        }
    }
// Set correct names
    $("table#resRelationsTable_" + (counter + 1)).each(function() {
        $(this).removeAttr("name");
    });
    $("table#resRelationsTable_" + (counter + 1) + " input#textResRelationName").attr("name", "textResRelationName_" + (counter + 1));
    $("table#resRelationsTable_" + (counter + 1) + " select#resRelationLanguage").attr("name", "resRelationLanguage_" + (counter + 1));
    $("table#resRelationsTable_" + (counter + 1) + " input#textResRelationId").attr("name", "textResRelationId_" + (counter + 1));
    $("table#resRelationsTable_" + (counter + 1) + " input#textResRelationLink").attr("name", "textResRelationLink_" + (counter + 1));
    $("table#resRelationsTable_" + (counter + 1) + " select#resRelationType").attr("name", "resRelationType_" + (counter + 1));
    $("table#resRelationsTable_" + (counter + 1) + " textarea#textareaResRelationDescription").attr("name", "textareaResRelationDescription_" + (counter + 1));
    $("table#resRelationsTable_" + (counter + 1) + " input#textResRelRespOrgPerson").attr("name", "resRelationsTable_" + (counter + 1) + "_textResRelRespOrgPerson_1");
    $("table#resRelationsTable_" + (counter + 1) + " input#textResRelRespOrgId").attr("name", "resRelationsTable_" + (counter + 1) + "_textResRelRespOrgId_1");
    // Reset parameters
    $("table#resRelationsTable_" + (counter + 1) + " input[type='text']").each(function() {
        $(this).val(""); // Clean all input_text.
    });
    $("table#resRelationsTable_" + (counter + 1) + " select").each(function() {
        $(this).attr("value", ""); // Reset dropdown boxes.
    });
    $("table#resRelationsTable_" + (counter + 1) + " textarea").val("");
    // If default language given, set language field to this value
    if (defaultLanguage != null) {
        $("table#resRelationsTable_" + (counter + 1) + " select#resRelationLanguage").attr("value", defaultLanguage);
    }
}

function addFncRelation(defaultLanguage, relationMissing) {
    var counter = $("table[id^='fncRelationsTable_']").length;
    var relation = $("table#fncRelationsTable_" + counter + " input#textFncRelationName").attr("value");
    if (relation == null || relation == "") {
        alertEmptyFields(relationMissing);
        return;
    }

    var clone = $("table[id^='fncRelationsTable_" + counter + "']").clone();
    clone = "<table id='" + ("fncRelationsTable_" + (counter + 1)) + "' class=\"tablePadding\">" + clone.html() + "</table>";
    $("table[id^='fncRelationsTable_" + counter + "']").after(clone);
    // rename header of clone
    $("table#fncRelationsTable_" + (counter + 1) + " th#thFncRelTableHeader").html("Function relation " + (counter + 1));
    // delete superfluous identifier rows
    var idCounter = $("table#fncRelationsTable_" + (counter + 1) + " tr[id^='trFncRelationRespOrg_']").length;
    if (idCounter > 1) {
        for (var i = idCounter; i > 1; i--) {
            $("table#fncRelationsTable_" + (counter + 1) + " tr#trFncRelationRespOrg_" + i).remove();
        }
    }
// Set correct names
    $("table#fncRelationsTable_" + (counter + 1)).each(function() {
        $(this).removeAttr("name");
    });
    $("table#fncRelationsTable_" + (counter + 1) + " input#textFncRelationName").attr("name", "textFncRelationName_" + (counter + 1));
    $("table#fncRelationsTable_" + (counter + 1) + " select#fncRelationLanguage").attr("name", "fncRelationLanguage_" + (counter + 1));
    $("table#fncRelationsTable_" + (counter + 1) + " input#textFncRelationId").attr("name", "textFncRelationId_" + (counter + 1));
    $("table#fncRelationsTable_" + (counter + 1) + " input#textFncRelationLink").attr("name", "textFncRelationLink_" + (counter + 1));
    $("table#fncRelationsTable_" + (counter + 1) + " select#fncRelationType").attr("name", "fncRelationType_" + (counter + 1));
    $("table#fncRelationsTable_" + (counter + 1) + " textarea#textareaFncRelationDescription").attr("name", "textareaFncRelationDescription_" + (counter + 1));
    $("table#fncRelationsTable_" + (counter + 1) + " input#textFncRelRespOrgPerson").attr("name", "fncRelationsTable_" + (counter + 1) + "_textFncRelRespOrgPerson_1");
    $("table#fncRelationsTable_" + (counter + 1) + " input#textFncRelRespOrgId").attr("name", "fncRelationsTable_" + (counter + 1) + "_textFncRelRespOrgId_1");
    // Reset parameters
    $("table#fncRelationsTable_" + (counter + 1) + " input[type='text']").each(function() {
        $(this).val(""); // Clean all input_text.
    });
    $("table#fncRelationsTable_" + (counter + 1) + " select").each(function() {
        $(this).attr("value", ""); // Reset dropdown boxes.
    });
    $("table#fncRelationsTable_" + (counter + 1) + " textarea").val("");
    // If default language given, set language field to this value
    if (defaultLanguage != null) {
        $("table#fncRelationsTable_" + (counter + 1) + " select#fncRelationLanguage").attr("value", defaultLanguage);
    }
}

/**************************************
 * Control tab functions
 **************************************/

function addLocalId(fieldsMissing) {
    var counter = $("table[id^='localId_']").length;
    var localId = $("table#localId_" + counter + " input#textLocalId_" + counter).attr("value");
    if (localId == null || localId == "") {
        alertEmptyFields(fieldsMissing);
        return;
    }

    var newLocalId = $('<table id="localId_' + (counter + 1) + '" class="tablePadding">' +
            '<tr>' +
            '<td><label for="textLocalId_' + (counter + 1) + '">Local identifier for the institution</label></td>' +
            '<td><input type="text" id="textLocalId_' + (counter + 1) + '" name="textLocalId_' + (counter + 1) + '" /></td>' +
            '</tr>' +
            '</table>');
    $("table#localId_" + counter).after(newLocalId);
}

/*****************************************************
 * Tab validation functions
 ****************************************************/
var clickChooseTypeAction = function(text1) {
	displayAlertDialog(text);
}

var clickIdentityAction = function(text, message) {
	displayAlertDialog(text + '; ' + message);
}

/*************************************************************************************
 *Functions related to second-display EAC-CPF
 **************************************************************************************/
function init() {
	drawListDiscs();
    eraseData();
    $(".displayLinkShowLess").addClass("hidden");
    $('.displayLinkShowMore').addClass("hidden");
    $(".moreDisplay").each(function(index) {
        if ($(this).find('p').length > 3) {
            $(this).find('.displayLinkShowMore').removeClass("hidden");
            $(this).find('p').each(function(index) {
                if (index > 2) {
                    $(this).addClass("hidden");
                }
            });
        } else if ($(this).find('li.item').length > 3) {
            $(this).find('.displayLinkShowMore').removeClass("hidden");
            $(this).find('li.item').each(function(index) {
                if (index > 2) {
                    $(this).addClass("hidden");
                }
            });
        } else {
            $(this).find('.displayLinkShowMore').addClass("hidden");
        }
    });
    expandedSection();
    sameHeight();
}
function drawListDiscs(){
	if($("div#structureOrGenealogy").find("li.item").length==1){
		$("div#structureOrGenealogy").find("li.item").css("list-style","none outside none");
	}
	if($("div#generalContext").find("li.item").length==1){
		$("div#generalContext").find("li.item").css("list-style","none outside none");
	}
}
function initPrint() {
    eraseData();
    sameHeight();
    try {
        $("body").css("cursor", "progress");
        $(".displayLinkShowMore").each(function() {
            $(this).remove();
        });
        $(".displayLinkShowLess").each(function() {
            $(this).remove();
        });

        self.print();
    }
    catch (e) {
        $("body").css("cursor", "default");
    }
    $("body").css("cursor", "default");
}
function printEacDetails(url) {
    try {
        $("body").css("cursor", "progress");
        var preview = window.open(url, 'printeaccpf', 'width=1100,height=600,left=10,top=10,menubar=0,toolbar=0,status=0,location=0,scrollbars=1,resizable=1');
        preview.focus();
    } catch (e) {
        $("body").css("cursor", "default");
    }
    $("body").css("cursor", "default");
}

/**
 * Function to expand or collapsed the relations
 */
function makeRelationsCollapsible() {
    $('#relations .boxtitle').each(function(index) {
        $(this).click(function() {
            if ($(this).find(".collapsibleIcon").hasClass("expanded")) {
                $(this).find(".collapsibleIcon").removeClass("expanded").addClass("collapsed");
                $(this).parent().find('ul').addClass("hidden");
                $(this).parent().find('.whitespace').addClass("hidden");
                $(this).parent().find('.displayLinkShowLess').addClass('hidden');
                $(this).parent().find('.displayLinkShowMore').addClass('hidden');
            } else {
                $(this).find(".collapsibleIcon").removeClass("collapsed").addClass("expanded");
                $(this).parent().find('ul').removeClass("hidden");
                if ($(this).parent().find('li').length > 3) {
                    $(this).parent().find('.whitespace').removeClass("hidden");
                    $(this).parent().find('.displayLinkShowMore').removeClass("hidden");
                    $(this).parent().find('li').each(function(index) {
                        if (index > 2) {
                            $(this).addClass("hidden");
                        }
                    });
                } else {
                    $(this).parent().find('.whitespace').addClass("hidden");
                    $(this).parent().find('.displayLinkShowMore').addClass("hidden");
                }
            }
        });
        if ($(this).parent().find('li').length > 3) {
            $(this).parent().find('.whitespace').removeClass("hidden");
            $(this).parent().find('.displayLinkShowMore').removeClass("hidden");
            $(this).parent().find('li').each(function(index) {
                if (index > 2) {
                    $(this).addClass("hidden");
                }
            });
        } else {
            $(this).parent().find('.whitespace').addClass("hidden");
            $(this).parent().find('.displayLinkShowMore').addClass("hidden");
        }

    });
}

/**
 * Function to show more eac-cpf details
 * @param clazz
 * @param id
 */
function showLess(clazz, id) {
    var prefix = "#" + clazz + " ";
    $(prefix + ".displayLinkShowLess").click(function() {
        $(this).addClass("hidden");
        $(prefix + ".displayLinkShowMore").removeClass("hidden");
        $(prefix + id).each(function(index) {
            if (index > 2) {
                $(this).addClass("hidden");
            }
        });
    });
    $(prefix + ".displayLinkShowLess").trigger("click");
    sameHeight();
}

/**
 * Function to show less eac-cpf details
 * @param clazz
 * @param id
 */
function showMore(clazz, id) {
    var prefix = "#" + clazz + " ";
    $(prefix + ".displayLinkShowMore").click(function() {
        $(this).addClass("hidden");
        $(prefix + ".displayLinkShowLess").removeClass("hidden");
        $(prefix + id).each(function(index) {
            if (index > 2) {
                $(this).removeClass("hidden");
            }
        });
    });
    $(prefix + ".displayLinkShowMore").trigger("click");
    sameHeight();
}

function redirect(country) {
    var hostname = window.location.hostname;
    var finalHref;
    if (hostname == "contentchecker.archivesportaleurope.net") {
        finalHref = "http://contentchecker.archivesportaleurope.net/" + country + "/home";
    } else {
        finalHref = "http://www.archivesportaleurope.net/" + country + "/home";
    }
    location.href = finalHref;
}

/**
 * Function to expand or collapse the different eac-cpf's sections
 */
function expandedSection() {
    $('h2.title').click(function() {
        var target = $(this).next();
        if ($(this).hasClass("expanded")) {
            $(this).removeClass("expanded").addClass("collapsed");
            target.hide();
            $(target).find('.displayLinkShowMore').each(function() {
                $(this).addClass("hidden");
            });
            $(target).find('.displayLinkShowLess').each(function() {
                $(this).addClass("hidden");
            });
        } else {
            $(this).removeClass("collapsed").addClass("expanded");
            target.show();
            $(target).find('.moreDisplay').each(function(index) {
                if ($(this).find('p').length > 3) {
                    $(this).find('.displayLinkShowMore').removeClass("hidden");
                    $(this).find('p').each(function(index) {
                        if (index > 2) {
                            $(this).addClass("hidden");
                        }
                    });
                    sameHeight();
                } else if ($(this).find('li.item').length > 3) {
                    $(this).find('.displayLinkShowMore').removeClass("hidden");
                    $(this).find('li.item').each(function(index) {
                        if (index > 2) {
                            $(this).addClass("hidden");
                        }
                    });
                    sameHeight();
                } else {
                    $(this).find('.displayLinkShowMore').addClass("hidden");
                }
            });
        }

    });
}

/**
 * Function to assign the same height that its container
 */
function sameHeight() {
    $('#eacCpfDisplayPortlet .row').each(function() {
        $(this).css("height", "");
        $(this).children().css("height", "");
        var height = $(this).height();
        if(height == "auto"){
        	height = $(this).css("height");
        }
        $(this).children().css("height", height);
    });
}
