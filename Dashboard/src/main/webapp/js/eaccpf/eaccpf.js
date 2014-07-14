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
function clickSaveAction(onlySave, nameMissing, dateMissing, startDateMissing, endDateMissing, cpfTypeMissing, resourceTypeMissing, functionTypeMissing, languageMissing, scriptMissing) {
// Check fill mandatory fields in tab "your institution".
    var identityValidation = checkIdentityTab(nameMissing, dateMissing, startDateMissing, endDateMissing);
    if (identityValidation !== "ok") {
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
//    	$("input#saveOrExit").attr("value", "save");

    	// Try to save without refresh the page.
    	$.post("storeEacCpf.action", $('#webformEacCpf').serialize(), function(d) {
    		if (d.fileId) {
    			$("input#fileId").attr("value", d.fileId);
    		}
    		if (d.eacDaoId) {
    			$("input#eacDaoId").attr("value", d.eacDaoId);
    		}
    		if (d.resultMessage) {
    			$("ul#eacCpfTabsContainer a[href='#tab-identity']").trigger('click');
    			$("div#spanMessage").html("<span>" + d.resultMessage + "</span>")
				$("div#spanMessage").fadeIn("slow");
    			$(document).bind('keyup mousedown', function(){
    				$("div#spanMessage").fadeOut("slow");
				});
    		}
    		if (d.error) {
    			alert(d.error);
    		}
    	});
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
function clickExitAction(question, nameMissing, dateMissing, startDateMissing, endDateMissing, cpfTypeMissing, resourceTypeMissing, functionTypeMissing, languageMissing, scriptMissing) {
	// Checks the return page on exit.
	var useMode = $('input[name=useMode]').val();
	if (useMode == "load") {
		$("input#returnPage").attr("value", "contentmanager");
	} else {
		$("input#returnPage").attr("value", "dashboardHome");
	}

	// Ask user for the action.
	if (confirm(question)) {
		clickSaveAction(false, nameMissing, dateMissing, startDateMissing, endDateMissing, cpfTypeMissing, resourceTypeMissing, functionTypeMissing, languageMissing, scriptMissing);
	} else {
		$("input#saveOrExit").attr("value", "exit");
		$('#webformEacCpf').submit();
	}
}

/**************************************
 * general purpose functions
 **************************************/

function deleteChecks() {
    $('.fieldRequired').remove();
}

var checkIdentityTab = function(nameMissing, dateMissing, startDateMissing, endDateMissing) {
    var personName = $("table#identityPersonName_1 input#textPersonName").attr("value");
    if (personName == null || personName == "") {
        alertEmptyFields(nameMissing);
        return;
    }
    var date1 = $("table#dateExistenceTable tr#trDate_text_1 input#date_1").attr("value");
    var date2 = $("table#dateExistenceTable tr#trDate_text_1 input#date_2").attr("value");
    if (date1 == null || date1 == "") {
        if (date2 == "") {
            alertEmptyFields(dateMissing);
            return;
        } else {
            alertEmptyFields(startDateMissing);
            return;
        }
    } else {
        if (date2 == "") {
            alertEmptyFields(endDateMissing);
            return;
        }
    }
    return "ok";
};

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
    alert(text1);
}

function alertFillFieldsBeforeChangeTab(text) {
    alert(text);
}

/**************************************
 * date-related functions
 **************************************/

function insertDateAfter(tableName, anchorId, incrCounter, dateLabel, unknownLabel, isoLabel) {
    var newDate = $('<tr id="trDate_text_' + incrCounter + '">' +
            '<td>' + dateLabel + '</td>' +
            '<td>' +
            '<input type="text" id="date_1" name="' + tableName + '_date_1_' + incrCounter + '" onchange="parseDateToISO($(this).attr(\'value\'), $(this).parent().parent().parent().parent().attr(\'id\'), $(this).parent().parent().attr(\'id\'), $(this).attr(\'id\'));"><br />' +
            '<input type="checkbox" id="date_unknown_1" name="' + tableName + '_date_unknown_1_' + incrCounter + '" onchange="toggleDateTextfields($(this));" /><label for="date_unknown_1">' + unknownLabel + '</label>' +
            '</td>' +
            '<td></td><td></td>' +
            '</tr>' +
            '<tr id="trDate_iso_' + incrCounter + '">' +
            '<td>' + isoLabel + '</td>' +
            '<td><input type="text" title="YYYY" id="date_1_Year" name="' + tableName + '_date_1_Year_' + incrCounter + '" size="4" maxlength="4" /> &ndash; ' +
            '<input type="text" title="MM" id="date_1_Month" name="' + tableName + '_date_1_Month_' + incrCounter + '" size="2" maxlength="2" /> &ndash; ' +
            '<input type="text" title="DD" id="date_1_Day" name="' + tableName + '_date_1_Day_' + incrCounter + '" size="2" maxlength="2" /></td>' +
            '<td></td><td></td>' +
            '</tr>');
    $("table#" + tableName + " " + anchorId).after(newDate);
}

function insertDateRangeAfter(tableName, anchorId, incrCounter, fromDateLabel, toDateLabel, unknownLabel, isoLabel) {
    var newDateRange = $('<tr id="trDate_text_' + incrCounter + '">' +
            '<td>' + fromDateLabel + '</td>' +
            '<td>' +
            '<input type="text" id="date_1" name="' + tableName + '_date_1_' + incrCounter + '" onchange="parseDateToISO($(this).attr(\'value\'), $(this).parent().parent().parent().parent().attr(\'id\'), $(this).parent().parent().attr(\'id\'), $(this).attr(\'id\'));"><br />' +
            '<input type="checkbox" id="date_unknown_1" name="' + tableName + '_date_unknown_1_' + incrCounter + '" onchange="toggleDateTextfields($(this));" /><label for="date_unknown_1">' + unknownLabel + '</label>' +
            '</td>' +
            '<td>' + toDateLabel + '</td>' +
            '<td>' +
            '<input type="text" id="date_2" name="' + tableName + '_date_2_' + incrCounter + '" onchange="parseDateToISO($(this).attr(\'value\'), $(this).parent().parent().parent().parent().attr(\'id\'), $(this).parent().parent().attr(\'id\'), $(this).attr(\'id\'));"><br />' +
            '<input type="checkbox" id="date_unknown_2" name="' + tableName + '_date_unknown_2_' + incrCounter + '" onchange="toggleDateTextfields($(this));" /><label for="date_unknown_2">' + unknownLabel + '</label>' +
            '</td>' +
            '</tr>' +
            '<tr id="trDate_iso_' + incrCounter + '">' +
            '<td>' + isoLabel + '</td>' +
            '<td><input type="text" title="YYYY" id="date_1_Year" name="' + tableName + '_date_1_Year_' + incrCounter + '" size="4" maxlength="4" /> &ndash; ' +
            '<input type="text" title="MM" id="date_1_Month" name="' + tableName + '_date_1_Month_' + incrCounter + '" size="2" maxlength="2" /> &ndash; ' +
            '<input type="text" title="DD" id="date_1_Day" name="' + tableName + '_date_1_Day_' + incrCounter + '" size="2" maxlength="2" /></td>' +
            '<td></td>' +
            '<td><input type="text" title="YYYY" id="date_2_Year" name="' + tableName + '_date_2_Year_' + incrCounter + '" size="4" maxlength="4" /> &ndash; ' +
            '<input type="text" title="MM" id="date_2_Month" name="' + tableName + '_date_2_Month_' + incrCounter + '" size="2" maxlength="2" /> &ndash; ' +
            '<input type="text" title="DD" id="date_2_Day" name="' + tableName + '_date_2_Day_' + incrCounter + '" size="2" maxlength="2" /></td>' +
            '</tr>');
    $("table#" + tableName + " " + anchorId).after(newDateRange);
}


function dateRowNotEmpty(table, row) {
    var testYear1 = $("table#" + table + " " + row + " input#date_1").attr("value");
    var testYear1Checked = $("table#" + table + " " + row + " input#date_unknown_1").is(":checked");
    if ($("table#" + table + " " + row + " input#date_2").length != 0) {
        var testYear2 = $("table#" + table + " " + row + " input#date_2").attr("value");
        var testYear2Checked = $("table#" + table + " " + row + " input#date_unknown_2").is(":checked");
    } else {
        var testYear2 = null;
    }
    if (testYear2 == null) {
        if (testYear1 == "" && testYear1Checked == false)
            return false;
        else
            return true;
    } else if ((testYear2 == "" && testYear1 == "") && testYear1Checked == false && testYear2Checked == false) {
        return false;
    } else
        return true;
}

function parseDateToISO(content, table, row, date) {
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
}

function addTrailingZero(value) {
    if (value < 10 && !(/^0/).test(value)) {
        return "0" + value;
    } else
        return value;
}

function toggleDateTextfields(checkbox) {
    var counterDate = $(checkbox).attr("name").split('_');
    var tableName = $(checkbox).parent().parent().parent().parent().attr("id");
    if ($(checkbox).is(":checked") == true) {
        $('table#' + tableName + ' tr#trDate_text_' + counterDate[counterDate.length - 1] + ' input#date_' + counterDate[counterDate.length - 2]).attr("value", "unknown");
        $('table#' + tableName + ' tr#trDate_text_' + counterDate[counterDate.length - 1] + ' input#date_' + counterDate[counterDate.length - 2]).attr("disabled", "disabled");
        if ($(checkbox).attr("id") == "date_unknown_1") {
            $('table#' + tableName + ' tr#trDate_iso_' + counterDate[counterDate.length - 1] + ' input#date_1_Year').attr("value", "0001");
        } else if ($(checkbox).attr("id") == "date_unknown_2") {
            $('table#' + tableName + ' tr#trDate_iso_' + counterDate[counterDate.length - 1] + ' input#date_2_Year').attr("value", "2099");
        }
        $('table#' + tableName + ' tr#trDate_iso_' + counterDate[counterDate.length - 1] + ' input[id^="date_' + counterDate[counterDate.length - 2] + '_"]').each(function() {
            $(this).attr("disabled", "disabled");
        });
    } else {
        $('table#' + tableName + ' tr#trDate_text_' + counterDate[counterDate.length - 1] + ' input#date_' + counterDate[counterDate.length - 2]).attr("value", "");
        $('table#' + tableName + ' tr#trDate_text_' + counterDate[counterDate.length - 1] + ' input#date_' + counterDate[counterDate.length - 2]).removeAttr("disabled");
        if ($(checkbox).attr("id") == "date_unknown_1") {
            $('table#' + tableName + ' tr#trDate_iso_' + counterDate[counterDate.length - 1] + ' input#date_1_Year').attr("value", "");
        } else if ($(checkbox).attr("id") == "date_unknown_2") {
            $('table#' + tableName + ' tr#trDate_iso_' + counterDate[counterDate.length - 1] + ' input#date_2_Year').attr("value", "");
        }
        $('table#' + tableName + ' tr#trDate_iso_' + counterDate[counterDate.length - 1] + ' input[id^="date_' + counterDate[counterDate.length - 2] + '_"]').each(function() {
            $(this).removeAttr("disabled");
        });
    }
}

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

function addDateOrDateRangeName(buttonClicked, tableName, dateLabel, fromDateLabel, toDateLabel, unknownLabel, isoLabel) {
    var counter = $("table#" + tableName + " tr[id^='trDate_text_']").length;
    if (buttonClicked == "addNameDate") {
        if (counter == 0) {
            insertDateAfter(tableName, "tr#trNameForm", counter + 1, dateLabel, unknownLabel, isoLabel);
        } else {
            if (dateRowNotEmpty(tableName, "tr#trDate_text_" + counter)) {
                insertDateAfter(tableName, "tr#trDate_iso_" + counter, counter + 1, dateLabel, unknownLabel, isoLabel);
            } else {
                if (counter == 1) {
                    $("table#" + tableName + " tr#trDate_iso_" + counter).remove();
                    $("table#" + tableName + " tr#trDate_text_" + counter).replaceWith(insertDateAfter(tableName, "tr#trNameForm", counter, dateLabel, unknownLabel, isoLabel));
                } else {
                    $("table#" + tableName + " tr#trDate_iso_" + counter).remove();
                    $("table#" + tableName + " tr#trDate_text_" + counter).replaceWith(insertDateAfter(tableName, "tr#trDate_iso_" + (counter - 1), counter, dateLabel, unknownLabel, isoLabel));
                }
            }
        }
    }
    if (buttonClicked == "addNameDateRange") {
        if (counter == 0) {
            insertDateRangeAfter(tableName, "tr#trNameForm", counter + 1, fromDateLabel, toDateLabel, unknownLabel, isoLabel);
        } else {
            if (dateRowNotEmpty(tableName, "tr#trDate_text_" + counter)) {
                insertDateRangeAfter(tableName, "tr#trDate_iso_" + counter, counter + 1, fromDateLabel, toDateLabel, unknownLabel, isoLabel);
            } else {
                if (counter == 1) {
                    $("table#" + tableName + " tr#trDate_iso_" + counter).remove();
                    $("table#" + tableName + " tr#trDate_text_" + counter).replaceWith(insertDateRangeAfter(tableName, "tr#trNameForm", counter, fromDateLabel, toDateLabel, unknownLabel, isoLabel));
                } else {
                    $("table#" + tableName + " tr#trDate_iso_" + counter).remove();
                    $("table#" + tableName + " tr#trDate_text_" + counter).replaceWith(insertDateRangeAfter(tableName, "tr#trDate_iso_" + (counter - 1), counter, fromDateLabel, toDateLabel, unknownLabel, isoLabel));
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
    //Remove superfluous date rows
    var idCounter = $("table#identityPersonName_" + (counter + 1) + " tr[id^='trDate_']").length;
    if (idCounter > 0) {
        idCounter = idCounter / 2;
        for (var i = idCounter; i > 0; i--) {
            $("table#identityPersonName_" + (counter + 1) + " tr#trDate_text_" + i).remove();
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

function addDateOrDateRangeExistence(buttonClicked, dateLabel, fromDateLabel, toDateLabel, unknownLabel, isoLabel) {
    var counter = $("table#dateExistenceTable tr[id^='trDate_text_']").length;
    if (buttonClicked == "addExistDate") {
        if (counter == 0) {
            insertDateAfter("dateExistenceTable", "tr#trDateExistenceTableHeader", counter + 1, dateLabel, unknownLabel, isoLabel);
        } else {
            if (dateRowNotEmpty("dateExistenceTable", "tr#trDate_text_" + counter)) {
                insertDateAfter("dateExistenceTable", "tr#trDate_iso_" + counter, counter + 1, dateLabel, unknownLabel, isoLabel);
            } else {
                if (counter == 1) {
                    $("table#dateExistenceTable tr#trDate_iso_" + counter).remove();
                    $("table#dateExistenceTable tr#trDate_text_" + counter).replaceWith(insertDateAfter("dateExistenceTable", "tr#trDateExistenceTableHeader", counter, dateLabel, unknownLabel, isoLabel));
                } else {
                    $("table#dateExistenceTable tr#trDate_iso_" + counter).remove();
                    $("table#dateExistenceTable tr#trDate_text_" + counter).replaceWith(insertDateAfter("dateExistenceTable", "tr#trDate_iso_" + (counter - 1), counter, dateLabel, unknownLabel, isoLabel));
                }
            }
        }
    }
    if (buttonClicked == "addExistDateRange") {
        if (counter == 0) {
            insertDateRangeAfter("dateExistenceTable", "tr#trDateExistenceTableHeader", counter + 1, fromDateLabel, toDateLabel, unknownLabel, isoLabel);
        } else {
            if (dateRowNotEmpty("dateExistenceTable", "tr#trDate_text_" + counter)) {
                insertDateRangeAfter("dateExistenceTable", "tr#trDate_iso_" + counter, counter + 1, fromDateLabel, toDateLabel, unknownLabel, isoLabel);
            } else {
                if (counter == 1) {
                    $("table#dateExistenceTable tr#trDate_iso_" + counter).remove();
                    $("table#dateExistenceTable tr#trDate_text_" + counter).replaceWith(insertDateRangeAfter("dateExistenceTable", "tr#trDateExistenceTableHeader", counter, fromDateLabel, toDateLabel, unknownLabel, isoLabel));
                } else {
                    $("table#dateExistenceTable tr#trDate_iso_" + counter).remove();
                    $("table#dateExistenceTable tr#trDate_text_" + counter).replaceWith(insertDateRangeAfter("dateExistenceTable", "tr#trDate_iso_" + (counter - 1), counter, fromDateLabel, toDateLabel, unknownLabel, isoLabel));
                }
            }
        }
    }
    $("input#dateExistenceTable_rows").val($("table#dateExistenceTable tr[id^='trDate_text_']").length);
}

/**************************************
 * Description tab functions
 **************************************/

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

function addDateOrDateRangePlace(buttonClicked, tableName, dateLabel, fromDateLabel, toDateLabel, unknownLabel, isoLabel) {
    var counter = $("table#" + tableName + " tr[id^='trDate_text_']").length;
    if (buttonClicked == "addPlaceDate") {
        if (counter == 0) {
            insertDateAfter(tableName, "tr#trPlaceRole", counter + 1, dateLabel, unknownLabel, isoLabel);
        } else {
            if (dateRowNotEmpty(tableName, "tr#trDate_text_" + counter)) {
                insertDateAfter(tableName, "tr#trDate_iso_" + counter, counter + 1, dateLabel, unknownLabel, isoLabel);
            } else {
                if (counter == 1) {
                    $("table#" + tableName + " tr#trDate_iso_" + counter).remove();
                    $("table#" + tableName + " tr#trDate_text_" + counter).replaceWith(insertDateAfter(tableName, "tr#trPlaceRole", counter, dateLabel, unknownLabel, isoLabel));
                } else {
                    $("table#" + tableName + " tr#trDate_iso_" + counter).remove();
                    $("table#" + tableName + " tr#trDate_text_" + counter).replaceWith(insertDateAfter(tableName, "tr#trDate_iso_" + (counter - 1), counter, dateLabel, unknownLabel, isoLabel));
                }
            }
        }
    }
    if (buttonClicked == "addPlaceDateRange") {
        if (counter == 0) {
            insertDateRangeAfter(tableName, "tr#trPlaceRole", counter + 1, fromDateLabel, toDateLabel, unknownLabel, isoLabel);
        } else {
            if (dateRowNotEmpty(tableName, "tr#trDate_text_" + counter)) {
                insertDateRangeAfter(tableName, "tr#trDate_iso_" + counter, counter + 1, fromDateLabel, toDateLabel, unknownLabel, isoLabel);
            } else {
                if (counter == 1) {
                    $("table#" + tableName + " tr#trDate_iso_" + counter).remove();
                    $("table#" + tableName + " tr#trDate_text_" + counter).replaceWith(insertDateRangeAfter(tableName, "tr#trPlaceRole", counter, fromDateLabel, toDateLabel, unknownLabel, isoLabel));
                } else {
                    $("table#" + tableName + " tr#trDate_iso_" + counter).remove();
                    $("table#" + tableName + " tr#trDate_text_" + counter).replaceWith(insertDateRangeAfter(tableName, "tr#trDate_iso_" + (counter - 1), counter, fromDateLabel, toDateLabel, unknownLabel, isoLabel));
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

    var clone = $("table[id^='placeTable_" + counter + "']").clone();
    clone = "<table id='" + ("placeTable_" + (counter + 1)) + "' class=\"tablePadding\">" + clone.html() + "</table>";
    $("table[id^='placeTable_" + counter + "']").after(clone);
    // rename header of clone
    $("table#placeTable_" + (counter + 1) + " th#thPlaceTableHeader").html("Place " + (counter + 1));
    // delete superfluous address component rows
    var idCounter = $("table#placeTable_" + (counter + 1) + " tr[id^='trAddressComponent_']").length;
    if (idCounter > 1) {
        for (var i = idCounter; i > 1; i--) {
            $("table#placeTable_" + (counter + 1) + " tr#trAddressComponent_" + i).remove();
        }
    }
    // delete superfluous date rows
    idCounter = $("table#placeTable_" + (counter + 1) + " tr[id^='trDate_']").length;
    if (idCounter > 0) {
        idCounter = idCounter / 2;
        for (var i = idCounter; i > 0; i--) {
            $("table#placeTable_" + (counter + 1) + " tr#trDate_text_" + i).remove();
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

function addDateOrDateRangeFunction(buttonClicked, tableName, dateLabel, fromDateLabel, toDateLabel, unknownLabel, isoLabel) {
    var counter = $("table#" + tableName + " tr[id^='trDate_text_']").length;
    if (buttonClicked == "addFunctionDate") {
        if (counter == 0) {
            insertDateAfter(tableName, "tr#trPlaceFunctionButton", counter + 1, dateLabel, unknownLabel, isoLabel);
        } else {
            if (dateRowNotEmpty(tableName, "tr#trDate_text_" + counter)) {
                insertDateAfter(tableName, "tr#trDate_iso_" + counter, counter + 1, dateLabel, unknownLabel, isoLabel);
            } else {
                if (counter == 1) {
                    $("table#" + tableName + " tr#trDate_iso_" + counter).remove();
                    $("table#" + tableName + " tr#trDate_text_" + counter).replaceWith(insertDateAfter(tableName, "tr#trPlaceFunctionButton", counter, dateLabel, unknownLabel, isoLabel));
                } else {
                    $("table#" + tableName + " tr#trDate_iso_" + counter).remove();
                    $("table#" + tableName + " tr#trDate_text_" + counter).replaceWith(insertDateAfter(tableName, "tr#trDate_iso_" + (counter - 1), counter, dateLabel, unknownLabel, isoLabel));
                }
            }
        }
    }
    if (buttonClicked == "addFunctionDateRange") {
        if (counter == 0) {
            insertDateRangeAfter(tableName, "tr#trPlaceFunctionButton", counter + 1, fromDateLabel, toDateLabel, unknownLabel, isoLabel);
        } else {
            if (dateRowNotEmpty(tableName, "tr#trDate_text_" + counter)) {
                insertDateRangeAfter(tableName, "tr#trDate_iso_" + counter, counter + 1, fromDateLabel, toDateLabel, unknownLabel, isoLabel);
            } else {
                if (counter == 1) {
                    $("table#" + tableName + " tr#trDate_iso_" + counter).remove();
                    $("table#" + tableName + " tr#trDate_text_" + counter).replaceWith(insertDateRangeAfter(tableName, "tr#trPlaceFunctionButton", counter, fromDateLabel, toDateLabel, unknownLabel, isoLabel));
                } else {
                    $("table#" + tableName + " tr#trDate_iso_" + counter).remove();
                    $("table#" + tableName + " tr#trDate_text_" + counter).replaceWith(insertDateRangeAfter(tableName, "tr#trDate_iso_" + (counter - 1), counter, fromDateLabel, toDateLabel, unknownLabel, isoLabel));
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
    // delete superfluous date rows
    idCounter = $("table#functionTable_" + (counter + 1) + " tr[id^='trDate_']").length;
    if (idCounter > 0) {
        idCounter = idCounter / 2;
        for (var i = idCounter; i > 0; i--) {
            $("table#functionTable_" + (counter + 1) + " tr#trDate_text_" + i).remove();
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

function addDateOrDateRangeOccupation(buttonClicked, tableName, dateLabel, fromDateLabel, toDateLabel, unknownLabel, isoLabel) {
    var counter = $("table#" + tableName + " tr[id^='trDate_text_']").length;
    if (buttonClicked == "addOccupationDate") {
        if (counter == 0) {
            insertDateAfter(tableName, "tr#trPlaceOccupationButton", counter + 1, dateLabel, unknownLabel, isoLabel);
        } else {
            if (dateRowNotEmpty(tableName, "tr#trDate_text_" + counter)) {
                insertDateAfter(tableName, "tr#trDate_iso_" + counter, counter + 1, dateLabel, unknownLabel, isoLabel);
            } else {
                if (counter == 1) {
                    $("table#" + tableName + " tr#trDate_iso_" + counter).remove();
                    $("table#" + tableName + " tr#trDate_text_" + counter).replaceWith(insertDateAfter(tableName, "tr#trPlaceOccupationButton", counter, dateLabel, unknownLabel, isoLabel));
                } else {
                    $("table#" + tableName + " tr#trDate_iso_" + counter).remove();
                    $("table#" + tableName + " tr#trDate_text_" + counter).replaceWith(insertDateAfter(tableName, "tr#trDate_iso_" + (counter - 1), counter, dateLabel, unknownLabel, isoLabel));
                }
            }
        }
    }
    if (buttonClicked == "addOccupationDateRange") {
        if (counter == 0) {
            insertDateRangeAfter(tableName, "tr#trPlaceOccupationButton", counter + 1, fromDateLabel, toDateLabel, unknownLabel, isoLabel);
        } else {
            if (dateRowNotEmpty(tableName, "tr#trDate_text_" + counter)) {
                insertDateRangeAfter(tableName, "tr#trDate_iso_" + counter, counter + 1, fromDateLabel, toDateLabel, unknownLabel, isoLabel);
            } else {
                if (counter == 1) {
                    $("tr#trDate_iso_" + counter).remove();
                    $("table#" + tableName + " tr#trDate_text_" + counter).replaceWith(insertDateRangeAfter(tableName, "tr#trPlaceOccupationButton", counter, fromDateLabel, toDateLabel, unknownLabel, isoLabel));
                } else {
                    $("tr#trDate_iso_" + counter).remove();
                    $("table#" + tableName + " tr#trDate_text_" + counter).replaceWith(insertDateRangeAfter(tableName, "tr#trDate_iso_" + (counter - 1), counter, fromDateLabel, toDateLabel, unknownLabel, isoLabel));
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
    // delete superfluous date rows
    idCounter = $("table#occupationTable_" + (counter + 1) + " tr[id^='trDate_']").length;
    if (idCounter > 0) {
        idCounter = idCounter / 2;
        for (var i = idCounter; i > 0; i--) {
            $("table#occupationTable_" + (counter + 1) + " tr#trDate_text_" + i).remove();
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

function addCpfRelRespOrg(table, organisationMissing) {
    var id = $(table).attr("id");
    var counter = $("table#" + id + " tr[id^='trCpfRelationRespOrg_']").length;
    var respOrgName = $("table#" + id + " tr#trCpfRelationRespOrg_" + counter + " td input#textCpfRelRespOrgPerson").attr("value");
    var respOrgId = $("table#" + id + " tr#trCpfRelationRespOrg_" + counter + " td input#textCpfRelRespOrgId").attr("value");
    if (respOrgName == null || respOrgName == "" || respOrgId == null || respOrgId == "") {
        alertEmptyFields(organisationMissing);
        return;
    }

    var clone = $("table#" + id + " tr[id^='trCpfRelationRespOrg_" + counter + "']").clone();
    clone = "<tr id='" + ("trCpfRelationRespOrg_" + (counter + 1)) + "'>" + clone.html() + "</tr>";
    $("table#" + id + " tr[id^='trCpfRelationRespOrg_" + counter + "']").after(clone);
    // Delete old names and reset parameters
    $("table#" + id + " tr#trCpfRelationRespOrg_" + (counter + 1) + " input[type='text']").each(function() {
        $(this).removeAttr("name");
        $(this).val(""); // Clean all input_text.
    });
    // Set new names
    $("table#" + id + " tr#trCpfRelationRespOrg_" + (counter + 1) + " input#textCpfRelRespOrgPerson").attr("name", id + "_textCpfRelRespOrgPerson_" + (counter + 1));
    $("table#" + id + " tr#trCpfRelationRespOrg_" + (counter + 1) + " input#textCpfRelRespOrgId").attr("name", id + "_textCpfRelRespOrgId_" + (counter + 1));
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

function addResRelRespOrg(table, organisationMissing) {
    var id = $(table).attr("id");
    var counter = $("table#" + id + " tr[id^='trResRelationRespOrg_']").length;
    var respOrgName = $("table#" + id + " tr#trResRelationRespOrg_" + counter + " td input#textResRelRespOrgPerson").attr("value");
    var respOrgId = $("table#" + id + " tr#trResRelationRespOrg_" + counter + " td input#textResRelRespOrgId").attr("value");
    if (respOrgName == null || respOrgName == "" || respOrgId == null || respOrgId == "") {
        alertEmptyFields(organisationMissing);
        return;
    }

    var clone = $("table#" + id + " tr[id^='trResRelationRespOrg_" + counter + "']").clone();
    clone = "<tr id='" + ("trResRelationRespOrg_" + (counter + 1)) + "'>" + clone.html() + "</tr>";
    $("table#" + id + " tr[id^='trResRelationRespOrg_" + counter + "']").after(clone);
    // Delete old names and reset parameters
    $("table#" + id + " tr#trResRelationRespOrg_" + (counter + 1) + " input[type='text']").each(function() {
        $(this).removeAttr("name");
        $(this).val(""); // Clean all input_text.
    });
    // Set new names
    $("table#" + id + " tr#trResRelationRespOrg_" + (counter + 1) + " input#textResRelRespOrgPerson").attr("name", id + "_textResRelRespOrgPerson_" + (counter + 1));
    $("table#" + id + " tr#trResRelationRespOrg_" + (counter + 1) + " input#textResRelRespOrgId").attr("name", id + "_textResRelRespOrgId_" + (counter + 1));
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

function addFncRelRespOrg(table, organisationMissing) {
    var id = $(table).attr("id");
    var counter = $("table#" + id + " tr[id^='trFncRelationRespOrg_']").length;
    var respOrgName = $("table#" + id + " tr#trFncRelationRespOrg_" + counter + " td input#textFncRelRespOrgPerson").attr("value");
    var respOrgId = $("table#" + id + " tr#trFncRelationRespOrg_" + counter + " td input#textFncRelRespOrgId").attr("value");
    if (respOrgName == null || respOrgName == "" || respOrgId == null || respOrgId == "") {
        alertEmptyFields(organisationMissing);
        return;
    }

    var clone = $("table#" + id + " tr[id^='trFncRelationRespOrg_" + counter + "']").clone();
    clone = "<tr id='" + ("trFncRelationRespOrg_" + (counter + 1)) + "'>" + clone.html() + "</tr>";
    $("table#" + id + " tr[id^='trFncRelationRespOrg_" + counter + "']").after(clone);
    // Delete old names and reset parameters
    $("table#" + id + " tr#trFncRelationRespOrg_" + (counter + 1) + " input[type='text']").each(function() {
        $(this).removeAttr("name");
        $(this).val(""); // Clean all input_text.
    });
    // Set new names
    $("table#" + id + " tr#trFncRelationRespOrg_" + (counter + 1) + " input#textFncRelRespOrgPerson").attr("name", id + "_textFncRelRespOrgPerson_" + (counter + 1));
    $("table#" + id + " tr#trFncRelationRespOrg_" + (counter + 1) + " input#textFncRelRespOrgId").attr("name", id + "_textFncRelRespOrgId_" + (counter + 1));
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
    alert(text);
}

var clickIdentityAction = function(text, message) {
    alert(text + '; ' + message);
}

/*************************************************************************************
 *Functions related to second-display EAC-CPF
 **************************************************************************************/
function init() {
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
        } else if ($(this).find('pre').length > 3) {
            $(this).find('.displayLinkShowMore').removeClass("hidden");
            $(this).find('pre').each(function(index) {
                if (index > 2) {
                    $(this).addClass("hidden");
                }
            });
        } else {
            $(this).find('.displayLinkShowMore').addClass("hidden");
        }
    });

}
function initPrint() {
	eraseData();
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
            //var expanded = false;
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