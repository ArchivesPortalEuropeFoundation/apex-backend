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

function loadPreviousTab(currentTab) {
    if (currentTab == "tab-identity") {

    } else if (currentTab == "tab-description") {
        $("ul#eacCpfTabsContainer a[href='#tab-identity']").trigger('click');
        $("#currentTab").attr("value", "tab-identity");
    } else if (currentTab == "tab-relations") {
        $("ul#eacCpfTabsContainer a[href='#tab-description']").trigger('click');
        $("#currentTab").attr("value", "tab-description");
    } else if (currentTab == "tab-control") {
        $("ul#eacCpfTabsContainer a[href='#tab-relations']").trigger('click');
        $("#currentTab").attr("value", "tab-relations");
    }
}

function loadNextTab(currentTab) {
    if (currentTab == "tab-identity") {
        $("ul#eacCpfTabsContainer a[href='#tab-description']").trigger('click');
        $("#currentTab").attr("value", "tab-description");
    } else if (currentTab == "tab-description") {
        $("ul#eacCpfTabsContainer a[href='#tab-relations']").trigger('click');
        $("#currentTab").attr("value", "tab-relations");
    } else if (currentTab == "tab-relations") {
        $("ul#eacCpfTabsContainer a[href='#tab-control']").trigger('click');
        $("#currentTab").attr("value", "tab-control");
    } else if (currentTab == "tab-control") {

    }
}

/**************************************
 * Functions for correct display of context
 * according to chosen use mode
 **************************************/
function loadUseModeContext() {
    $('.area').hide();
    var useMode = $('input[name=useMode]:checked', '#webformChooseMode').val();
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
function clickSaveAction(text, chooseTypeError, identityError, descriptionError, relationsError, controlError, message) {
// Check fill mandatory fields in tab "your institution".
    var identityValidation = checkIdentityTab();
    if (identityValidation !== "ok") {
        return;
    }
    $('#webformEacCpf').submit();
}

/**************************************
 * Exit button function, leaves the form without
 * saving the contents
 **************************************/
function clickExitAction() {
    location.href = "index.action";
}

/**************************************
 * general purpose functions
 **************************************/

function deleteChecks() {
    $('.fieldRequired').remove();
}

var checkIdentityTab = function() {
    var personName = $("table#identityPersonName_1 input#textPersonName").attr("value");
    if (personName == null || personName == "") {
        alertEmptyFields("Please enter at least one entity name!");
        return;
    }
    var date1 = $("table#dateExistenceTable tr#trDate_text_1 input#date_1").attr("value");
    var date2 = $("table#dateExistenceTable tr#trDate_text_1 input#date_2").attr("value");
    if (date1 == null || date1 == "") {
        if (date2 == "") {
            alertEmptyFields("Please enter at least one complete date or date range!");
            return;
        } else {
            alertEmptyFields("Please fill in the start date of the range or use a single date!");
            return;
        }
    } else {
        if (date2 == "") {
            alertEmptyFields("Please fill in the end date of the range or use a single date!");
            return;
        }
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

function insertDateAfter(tableName, anchorId, incrCounter) {
    var newDate = $('<tr id="trDate_text_' + incrCounter + '">' +
            '<td>Date</td>' +
            '<td>' +
            '<input type="text" id="date_1" name="' + tableName + '_date_1_' + incrCounter + '" onblur="parseDateToISO($(this).attr(\'value\'), $(this).parent().parent().parent().parent().attr(\'id\'), $(this).parent().parent().attr(\'id\'), $(this).attr(\'id\'));"><br />' +
            '<input type="checkbox" id="date_unknown_1" name="' + tableName + '_date_unknown_1_' + incrCounter + '" onchange="toggleDateTextfields($(this));" /><label for="date_unknown_1">unknown</label>' +
            '</td>' +
            '<td></td><td></td>' +
            '</tr>' +
            '<tr id="trDate_iso_' + incrCounter + '">' +
            '<td>(ISO dates; optional)</td>' +
            '<td><input type="text" title="YYYY" id="date_1_Year" name="' + tableName + '_date_1_Year_' + incrCounter + '" size="4" maxlength="4" /> &ndash; ' +
            '<input type="text" title="MM" id="date_1_Month" name="' + tableName + '_date_1_Month_' + incrCounter + '" size="2" maxlength="2" /> &ndash; ' +
            '<input type="text" title="DD" id="date_1_Day" name="' + tableName + '_date_1_Day_' + incrCounter + '" size="2" maxlength="2" /></td>' +
            '<td></td><td></td>' +
            '</tr>');
    $("table#" + tableName + " " + anchorId).after(newDate);
}

function insertDateRangeAfter(tableName, anchorId, incrCounter) {
    var newDateRange = $('<tr id="trDate_text_' + incrCounter + '">' +
            '<td>Date range from</td>' +
            '<td>' +
            '<input type="text" id="date_1" name="' + tableName + '_date_1_' + incrCounter + '" onblur="parseDateToISO($(this).attr(\'value\'), $(this).parent().parent().parent().parent().attr(\'id\'), $(this).parent().parent().attr(\'id\'), $(this).attr(\'id\'));"><br />' +
            '<input type="checkbox" id="date_unknown_1" name="' + tableName + '_date_unknown_1_' + incrCounter + '" onchange="toggleDateTextfields($(this));" /><label for="date_unknown_1">unknown</label>' +
            '</td>' +
            '<td>to</td>' +
            '<td>' +
            '<input type="text" id="date_2" name="' + tableName + '_date_2_' + incrCounter + '" onblur="parseDateToISO($(this).attr(\'value\'), $(this).parent().parent().parent().parent().attr(\'id\'), $(this).parent().parent().attr(\'id\'), $(this).attr(\'id\'));"><br />' +
            '<input type="checkbox" id="date_unknown_2" name="' + tableName + '_date_unknown_2_' + incrCounter + '" onchange="toggleDateTextfields($(this));" /><label for="date_unknown_2">unknown</label>' +
            '</td>' +
            '</tr>' +
            '<tr id="trDate_iso_' + incrCounter + '">' +
            '<td>(ISO dates; optional)</td>' +
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

function addTrailingZero(value){
    if(value < 10 && !(/^0/).test(value)){
        return "0" + value;
    } else return value;
}

function toggleDateTextfields(checkbox) {
    var counterDate = $(checkbox).attr("name").split('_');
    var tableName = $(checkbox).parent().parent().parent().parent().attr("id");
    if ($(checkbox).is(":checked") == true) {
        $('table#' + tableName + ' tr#trDate_text_' + counterDate[counterDate.length - 1] + ' input#date_' + counterDate[counterDate.length - 2]).attr("value", "unknown");
        $('table#' + tableName + ' tr#trDate_text_' + counterDate[counterDate.length - 1] + ' input#date_' + counterDate[counterDate.length - 2]).attr("disabled", "disabled");
        if ($(checkbox).attr("id") == "date_unknown_1") {
            $('table#' + tableName + ' tr#trDate_iso_' + counterDate[counterDate.length - 1] + ' input#date_1_Year').attr("value", "0000");
        } else if ($(checkbox).attr("id") == "date_unknown_2") {
            $('table#' + tableName + ' tr#trDate_iso_' + counterDate[counterDate.length - 1] + ' input#date_2_Year').attr("value", "2999");
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

function addDateOrDateRangeName(buttonClicked, tableName) {
    var counter = $("table#" + tableName + " tr[id^='trDate_text_']").length;
    if (buttonClicked == "addNameDate") {
        if (counter == 0) {
            insertDateAfter(tableName, "tr#trPersonName_2", counter + 1);
        } else {
            if (dateRowNotEmpty(tableName, "tr#trDate_text_" + counter)) {
                insertDateAfter(tableName, "tr#trDate_iso_" + counter, counter + 1);
            } else {
                if (counter == 1) {
                    $("table#" + tableName + " tr#trDate_iso_" + counter).remove();
                    $("table#" + tableName + " tr#trDate_text_" + counter).replaceWith(insertDateAfter(tableName, "tr#trPersonName_2", counter));
                } else {
                    $("table#" + tableName + " tr#trDate_iso_" + counter).remove();
                    $("table#" + tableName + " tr#trDate_text_" + counter).replaceWith(insertDateAfter(tableName, "tr#trDate_iso_" + (counter - 1), counter));
                }
            }
        }
    }
    if (buttonClicked == "addNameDateRange") {
        if (counter == 0) {
            insertDateRangeAfter(tableName, "tr#trPersonName_2", counter + 1);
        } else {
            if (dateRowNotEmpty(tableName, "tr#trDate_text_" + counter)) {
                insertDateRangeAfter(tableName, "tr#trDate_iso_" + counter, counter + 1);
            } else {
                if (counter == 1) {
                    $("table#" + tableName + " tr#trDate_iso_" + counter).remove();
                    $("table#" + tableName + " tr#trDate_text_" + counter).replaceWith(insertDateRangeAfter(tableName, "tr#trPersonName_2", counter));
                } else {
                    $("table#" + tableName + " tr#trDate_iso_" + counter).remove();
                    $("table#" + tableName + " tr#trDate_text_" + counter).replaceWith(insertDateRangeAfter(tableName, "tr#trDate_iso_" + (counter - 1), counter));
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
    //Set correct names
    $("table#identityPersonName_" + (counter + 1) + " tr#trPersonName_1 input#textPersonName").removeAttr("name");
    $("table#identityPersonName_" + (counter + 1) + " tr#trPersonName_1 input#textPersonName").attr("name", "textPersonName_" + (counter + 1));
    $("table#identityPersonName_" + (counter + 1) + " tr#trPersonName_1 select#identityNameLanguage").removeAttr("name");
    $("table#identityPersonName_" + (counter + 1) + " tr#trPersonName_1 select#identityNameLanguage").attr("name", "identityNameLanguage_" + (counter + 1));
    $("table#identityPersonName_" + (counter + 1) + " tr#trPersonName_2 select#identityFormOfName").removeAttr("name");
    $("table#identityPersonName_" + (counter + 1) + " tr#trPersonName_2 select#identityFormOfName").attr("name", "identityFormOfName_" + (counter + 1));
    $("table#identityPersonName_" + (counter + 1) + " tr#trPersonName_2 select#identityComponentOfName").removeAttr("name");
    $("table#identityPersonName_" + (counter + 1) + " tr#trPersonName_2 select#identityComponentOfName").attr("name", "identityComponentOfName_" + (counter + 1));
    $("table#identityPersonName_" + (counter + 1) + " input#identityPersonName_" + counter + "_rows").removeAttr("name");
    $("table#identityPersonName_" + (counter + 1) + " input#identityPersonName_" + counter + "_rows").attr("name", "identityPersonName_" + (counter + 1) + "_rows");
    $("table#identityPersonName_" + (counter + 1) + " input[name^='identityPersonName_" + (counter + 1) + "_rows']").removeAttr("id");
    $("table#identityPersonName_" + (counter + 1) + " input[name^='identityPersonName_" + (counter + 1) + "_rows']").attr("id", "identityPersonName_" + (counter + 1) + "_rows");

    // Reset parameters
    $("table#identityPersonName_" + (counter + 1) + " input[type='text']").each(function() {
        $(this).val(""); // Clean all input_text.
    });
    if (defaultLanguage != null) {
        $("table#identityPersonName_" + (counter + 1) + " tr#trPersonName_1 select#identityNameLanguage").attr("value", defaultLanguage);
    } else {
        $("table#identityPersonName_" + (counter + 1) + " tr#trPersonName_1 select#identityNameLanguage").attr("value", "");
    }
    $("table#identityPersonName_" + (counter + 1) + " tr#trPersonName_2 select#identityFormOfName").attr("value", "authorized");
    $("table#identityPersonName_" + (counter + 1) + " tr#trPersonName_2 select#identityComponentOfName").attr("value", "persname");
}

function addPersonId() {
    var counter = $("table[id^='identityPersonId_']").length;
    var personId = $("table#identityPersonId_" + counter + " input#textPersonId").attr("value");
    var personTypeId = $("table#identityPersonId_" + counter + " input#textPersonTypeId").attr("value");
    if (personId == null || personId == "" || personTypeId == null || personTypeId == "") {
        alertEmptyFields("Please add an identifier before creating another set of fields!");
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

function addDateOrDateRangeExistence(buttonClicked) {
    var counter = $("table#dateExistenceTable tr[id^='trDate_text_']").length;
    if (buttonClicked == "addExistDate") {
        if (counter == 0) {
            insertDateAfter("dateExistenceTable", "tr#trDateExistenceTableHeader", counter + 1);
        } else {
            if (dateRowNotEmpty("dateExistenceTable", "tr#trDate_text_" + counter)) {
                insertDateAfter("dateExistenceTable", "tr#trDate_iso_" + counter, counter + 1);
            } else {
                if (counter == 1) {
                    $("table#dateExistenceTable tr#trDate_iso_" + counter).remove();
                    $("table#dateExistenceTable tr#trDate_text_" + counter).replaceWith(insertDateAfter("dateExistenceTable", "tr#trDateExistenceTableHeader", counter));
                } else {
                    $("table#dateExistenceTable tr#trDate_iso_" + counter).remove();
                    $("table#dateExistenceTable tr#trDate_text_" + counter).replaceWith(insertDateAfter("dateExistenceTable", "tr#trDate_iso_" + (counter - 1), counter));
                }
            }
        }
    }
    if (buttonClicked == "addExistDateRange") {
        if (counter == 0) {
            insertDateRangeAfter("dateExistenceTable", "tr#trDateExistenceTableHeader", counter + 1);
        } else {
            if (dateRowNotEmpty("dateExistenceTable", "tr#trDate_text_" + counter)) {
                insertDateRangeAfter("dateExistenceTable", "tr#trDate_iso_" + counter, counter + 1);
            } else {
                if (counter == 1) {
                    $("table#dateExistenceTable tr#trDate_iso_" + counter).remove();
                    $("table#dateExistenceTable tr#trDate_text_" + counter).replaceWith(insertDateRangeAfter("dateExistenceTable", "tr#trDateExistenceTableHeader", counter));
                } else {
                    $("table#dateExistenceTable tr#trDate_iso_" + counter).remove();
                    $("table#dateExistenceTable tr#trDate_text_" + counter).replaceWith(insertDateRangeAfter("dateExistenceTable", "tr#trDate_iso_" + (counter - 1), counter));
                }
            }
        }
    }
    $("input#dateExistenceTable_rows").val($("table#dateExistenceTable tr[id^='trDate_text_']").length);
}

/**************************************
 * Description tab functions
 **************************************/

function addAddressComponent(tableName) {
    var counter = $("table#" + tableName + " tr[id^='trAddressComponent_']").length;
    var component = $("table#" + tableName + " tr#trAddressComponent_" + counter + " input#addressDetails").attr("value");
    if (component == null || component == "") {
        alertEmptyFields("Please enter address details before adding another set of fields!");
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

function addDateOrDateRangePlace(buttonClicked, tableName) {
    var counter = $("table#" + tableName + " tr[id^='trDate_text_']").length;
    if (buttonClicked == "addPlaceDate") {
        if (counter == 0) {
            insertDateAfter(tableName, "tr#trPlaceRole", counter + 1);
        } else {
            if (dateRowNotEmpty(tableName, "tr#trDate_text_" + counter)) {
                insertDateAfter(tableName, "tr#trDate_iso_" + counter, counter + 1);
            } else {
                if (counter == 1) {
                    $("table#" + tableName + " tr#trDate_iso_" + counter).remove();
                    $("table#" + tableName + " tr#trDate_text_" + counter).replaceWith(insertDateAfter(tableName, "tr#trPlaceRole", counter));
                } else {
                    $("table#" + tableName + " tr#trDate_iso_" + counter).remove();
                    $("table#" + tableName + " tr#trDate_text_" + counter).replaceWith(insertDateAfter(tableName, "tr#trDate_iso_" + (counter - 1), counter));
                }
            }
        }
    }
    if (buttonClicked == "addPlaceDateRange") {
        if (counter == 0) {
            insertDateRangeAfter(tableName, "tr#trPlaceRole", counter + 1);
        } else {
            if (dateRowNotEmpty(tableName, "tr#trDate_text_" + counter)) {
                insertDateRangeAfter(tableName, "tr#trDate_iso_" + counter, counter + 1);
            } else {
                if (counter == 1) {
                    $("table#" + tableName + " tr#trDate_iso_" + counter).remove();
                    $("table#" + tableName + " tr#trDate_text_" + counter).replaceWith(insertDateRangeAfter(tableName, "tr#trPlaceRole", counter));
                } else {
                    $("table#" + tableName + " tr#trDate_iso_" + counter).remove();
                    $("table#" + tableName + " tr#trDate_text_" + counter).replaceWith(insertDateRangeAfter(tableName, "tr#trDate_iso_" + (counter - 1), counter));
                }
            }
        }
    }
    $("input#" + tableName + "_rows").val($("table#" + tableName + " tr[id^='trDate_text_']").length);
}

function addPlace(defaultLanguage) {
    var counter = $("table[id^='placeTable_']").length;
    var place = $("table#placeTable_" + counter + " input#place").attr("value");
    if (place == null || place == "") {
        alertEmptyFields("Please enter at least a place name before adding another set of fields!");
        return;
    }

    var clone = $("table[id^='placeTable_" + counter + "']").clone();
    clone = "<table id='" + ("placeTable_" + (counter + 1)) + "' class=\"tablePadding\">" + clone.html() + "</table>";
    $("table[id^='placeTable_" + counter + "']").after(clone);
    // rename header of clone
    $("table#placeTable_" + (counter + 1) + " th#thPlaceTableHeader").html("Place " + (counter + 1));
    // delete superfluous rows
    var idCounter = $("table#placeTable_" + (counter + 1) + " tr[id^='trAddressComponent_']").length;
    if (idCounter > 1) {
        for (var i = idCounter; i > 1; i--) {
            $("table#placeTable_" + (counter + 1) + " tr#trAddressComponent_" + i).remove();
        }
    }
    idCounter = $("table#placeTable_" + (counter + 1) + " tr[id^='trDate_']").length;
    if (idCounter > 0) {
        for (var i = idCounter; i > 0; i--) {
            $("table#placeTable_" + (counter + 1) + " tr#trDate_" + i).remove();
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

function addPlaceFunction(tableName) {
    var counter = $("table#" + tableName + " tr[id^='trPlaceFunction_']").length;
    var place = $("table#" + tableName + " tr#trPlaceFunction_" + counter + " input#textPlaceFunction").attr("value");
    if (place == null || place == "") {
        alertEmptyFields("Please enter a place name before adding another set of fields!");
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

function addDateOrDateRangeFunction(buttonClicked, tableName) {
    var counter = $("table#" + tableName + " tr[id^='trDate_text_']").length;
    if (buttonClicked == "addFunctionDate") {
        if (counter == 0) {
            insertDateAfter(tableName, "tr#trPlaceFunctionButton", counter + 1);
        } else {
            if (dateRowNotEmpty(tableName, "tr#trDate_text_" + counter)) {
                insertDateAfter(tableName, "tr#trDate_iso_" + counter, counter + 1);
            } else {
                if (counter == 1) {
                    $("table#" + tableName + " tr#trDate_iso_" + counter).remove();
                    $("table#" + tableName + " tr#trDate_text_" + counter).replaceWith(insertDateAfter(tableName, "tr#trPlaceFunctionButton", counter));
                } else {
                    $("table#" + tableName + " tr#trDate_iso_" + counter).remove();
                    $("table#" + tableName + " tr#trDate_text_" + counter).replaceWith(insertDateAfter(tableName, "tr#trDate_iso_" + (counter - 1), counter));
                }
            }
        }
    }
    if (buttonClicked == "addFunctionDateRange") {
        if (counter == 0) {
            insertDateRangeAfter(tableName, "tr#trPlaceFunctionButton", counter + 1);
        } else {
            if (dateRowNotEmpty(tableName, "tr#trDate_text_" + counter)) {
                insertDateRangeAfter(tableName, "tr#trDate_iso_" + counter, counter + 1);
            } else {
                if (counter == 1) {
                    $("table#" + tableName + " tr#trDate_iso_" + counter).remove();
                    $("table#" + tableName + " tr#trDate_text_" + counter).replaceWith(insertDateRangeAfter(tableName, "tr#trPlaceFunctionButton", counter));
                } else {
                    $("table#" + tableName + " tr#trDate_iso_" + counter).remove();
                    $("table#" + tableName + " tr#trDate_text_" + counter).replaceWith(insertDateRangeAfter(tableName, "tr#trDate_iso_" + (counter - 1), counter));
                }
            }
        }
    }
    $("input#" + tableName + "_rows").val($("table#" + tableName + " tr[id^='trDate_text']").length);
}

function addFunction(defaultLanguage) {
    var counter = $("table[id^='functionTable_']").length;
    var functionName = $("table#functionTable_" + counter + " input#function").attr("value");
    if (functionName == null || functionName == "") {
        alertEmptyFields("Please enter at least a function name before adding another set of fields!");
        return;
    }

    var clone = $("table[id^='functionTable_" + counter + "']").clone();
    clone = "<table id='" + ("functionTable_" + (counter + 1)) + "' class=\"tablePadding\">" + clone.html() + "</table>";
    $("table[id^='functionTable_" + counter + "']").after(clone);
    // rename header of clone
    $("table#functionTable_" + (counter + 1) + " th#thFunctionTableHeader").html("Function " + (counter + 1));
    // delete superfluous rows
    var idCounter = $("table#functionTable_" + (counter + 1) + " tr[id^='trPlaceFunction_']").length;
    if (idCounter > 1) {
        for (var i = idCounter; i > 1; i--) {
            $("table#functionTable_" + (counter + 1) + " tr#trPlaceFunction_" + i).remove();
        }
    }
    idCounter = $("table#functionTable_" + (counter + 1) + " tr[id^='trDate_']").length;
    if (idCounter > 0) {
        for (var i = idCounter; i > 0; i--) {
            $("table#functionTable_" + (counter + 1) + " tr#trDate_" + i).remove();
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

function addPlaceOccupation(tableName) {
    var counter = $("table#" + tableName + " tr[id^='trPlaceOccupation_']").length;
    var place = $("table#" + tableName + " tr#trPlaceOccupation_" + counter + " input#textPlaceOccupation").attr("value");
    if (place == null || place == "") {
        alertEmptyFields("Please enter a place name before adding another set of fields!");
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

function addDateOrDateRangeOccupation(buttonClicked, tableName) {
    var counter = $("table#" + tableName + " tr[id^='trDate_text_']").length;
    if (buttonClicked == "addOccupationDate") {
        if (counter == 0) {
            insertDateAfter(tableName, "tr#trPlaceOccupationButton", counter + 1);
        } else {
            if (dateRowNotEmpty(tableName, "tr#trDate_text_" + counter)) {
                insertDateAfter(tableName, "tr#trDate_iso_" + counter, counter + 1);
            } else {
                if (counter == 1) {
                    $("table#" + tableName + " tr#trDate_iso_" + counter).remove();
                    $("table#" + tableName + " tr#trDate_text_" + counter).replaceWith(insertDateAfter(tableName, "tr#trPlaceOccupationButton", counter));
                } else {
                    $("table#" + tableName + " tr#trDate_iso_" + counter).remove();
                    $("table#" + tableName + " tr#trDate_text_" + counter).replaceWith(insertDateAfter(tableName, "tr#trDate_iso_" + (counter - 1), counter));
                }
            }
        }
    }
    if (buttonClicked == "addOccupationDateRange") {
        if (counter == 0) {
            insertDateRangeAfter(tableName, "tr#trPlaceOccupationButton", counter + 1);
        } else {
            if (dateRowNotEmpty(tableName, "tr#trDate_text_" + counter)) {
                insertDateRangeAfter(tableName, "tr#trDate_iso_" + counter, counter + 1);
            } else {
                if (counter == 1) {
                    $("tr#trDate_iso_" + counter).remove();
                    $("table#" + tableName + " tr#trDate_text_" + counter).replaceWith(insertDateRangeAfter(tableName, "tr#trPlaceOccupationButton", counter));
                } else {
                    $("tr#trDate_iso_" + counter).remove();
                    $("table#" + tableName + " tr#trDate_text_" + counter).replaceWith(insertDateRangeAfter(tableName, "tr#trDate_iso_" + (counter - 1), counter));
                }
            }
        }
    }
    $("input#" + tableName + "_rows").val($("table#" + tableName + " tr[id^='trDate_text_']").length);
}

function addOccupation(defaultLanguage) {
    var counter = $("table[id^='occupationTable_']").length;
    var occupation = $("table#occupationTable_" + counter + " input#occupation").attr("value");
    if (occupation == null || occupation == "") {
        alertEmptyFields("Please enter at least an occupation name before adding another set of fields!");
        return;
    }

    var clone = $("table[id^='occupationTable_" + counter + "']").clone();
    clone = "<table id='" + ("occupationTable_" + (counter + 1)) + "' class=\"tablePadding\">" + clone.html() + "</table>";
    $("table[id^='occupationTable_" + counter + "']").after(clone);
    // rename header of clone
    $("table#occupationTable_" + (counter + 1) + " th#thOccupationTableHeader").html("Occupation " + (counter + 1));
    // delete superfluous rows
    var idCounter = $("table#occupationTable_" + (counter + 1) + " tr[id^='trPlaceOccupation_']").length;
    if (idCounter > 1) {
        for (var i = idCounter; i > 1; i--) {
            $("table#occupationTable_" + (counter + 1) + " tr#trPlaceOccupation_" + i).remove();
        }
    }
    idCounter = $("table#occupationTable_" + (counter + 1) + " tr[id^='trDate_']").length;
    if (idCounter > 0) {
        for (var i = idCounter; i > 0; i--) {
            $("table#occupationTable_" + (counter + 1) + " tr#trDate_" + i).remove();
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

function addGenealogy() {
    var counter = $("[id^='genealogyContent_']").length;
    var genealogy = $("table#genealogyContent_" + counter + " textarea").attr("value");
    if (genealogy == null || genealogy == "") {
        alertEmptyFields("Please enter some genealogic content before adding another set of fields!");
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

function addBiography() {
    var counter = $("table[id^='biographyContent_']").length;
    var biography = $("table#biographyContent_" + counter + " textarea").attr("value");
    if (biography == null || biography == "") {
        alertEmptyFields("Please enter some biographic content before adding another set of fields!");
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

function addCpfRelation(defaultLanguage) {
    var counter = $("table[id^='cpfRelationsTable_']").length;
    var relation = $("table#cpfRelationsTable_" + counter + " input#textCpfRelationName").attr("value");
    if (relation == null || relation == "") {
        alertEmptyFields("Please enter the name and the type of the relation before adding a new set of fields!");
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

function addCpfRelRespOrg(table) {
    var id = $(table).attr("id");
    var counter = $("table#" + id + " tr[id^='trCpfRelationRespOrg_']").length;
    var respOrgName = $("table#" + id + " tr#trCpfRelationRespOrg_" + counter + " td input#textCpfRelRespOrgPerson").attr("value");
    var respOrgId = $("table#" + id + " tr#trCpfRelationRespOrg_" + counter + " td input#textCpfRelRespOrgId").attr("value");
    if (respOrgName == null || respOrgName == "" || respOrgId == null || respOrgId == "") {
        alertEmptyFields("Please enter both name and identifier of the organisation before adding another set of fields!");
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

function addResRelation(defaultLanguage) {
    var counter = $("table[id^='resRelationsTable_']").length;
    var relation = $("table#resRelationsTable_" + counter + " input#textResRelationName").attr("value");
    if (relation == null || relation == "") {
        alertEmptyFields("Please enter the name and the type of the relation before adding a new set of fields!");
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

function addResRelRespOrg(table) {
    var id = $(table).attr("id");
    var counter = $("table#" + id + " tr[id^='trResRelationRespOrg_']").length;
    var respOrgName = $("table#" + id + " tr#trResRelationRespOrg_" + counter + " td input#textResRelRespOrgPerson").attr("value");
    var respOrgId = $("table#" + id + " tr#trResRelationRespOrg_" + counter + " td input#textResRelRespOrgId").attr("value");
    if (respOrgName == null || respOrgName == "" || respOrgId == null || respOrgId == "") {
        alertEmptyFields("Please enter both name and identifier of the organisation before adding another set of fields!");
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

function addFncRelation(defaultLanguage) {
    var counter = $("table[id^='fncRelationsTable_']").length;
    var relation = $("table#fncRelationsTable_" + counter + " input#textFncRelationName").attr("value");
    if (relation == null || relation == "") {
        alertEmptyFields("Please enter the name and the type of the relation before adding a new set of fields!");
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

function addFncRelRespOrg(table) {
    var id = $(table).attr("id");
    var counter = $("table#" + id + " tr[id^='trFncRelationRespOrg_']").length;
    var respOrgName = $("table#" + id + " tr#trFncRelationRespOrg_" + counter + " td input#textFncRelRespOrgPerson").attr("value");
    var respOrgId = $("table#" + id + " tr#trFncRelationRespOrg_" + counter + " td input#textFncRelRespOrgId").attr("value");
    if (respOrgName == null || respOrgName == "" || respOrgId == null || respOrgId == "") {
        alertEmptyFields("Please enter both name and identifier of the organisation before adding another set of fields!");
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

function addLocalId() {
    var counter = $("table[id^='localId_']").length;
    var localId = $("table#localId_" + counter + " input#textLocalId_" + counter).attr("value");
    if (localId == null || localId == "") {
        alertEmptyFields("Empty fields!");
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
