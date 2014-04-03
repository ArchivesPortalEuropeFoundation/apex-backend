var globalMessageNormalCorrecVal, globalMessageEmptyNormal, globalMessageEmptyPreviousLang;

function initEadTree(fileId, xmlTypeId, messageEmptyEADID, messageEmptyWhenSave, messageInvalidCountrycode, messageEmptyCountrycode, messageEmptyMainagencycode, messageNormalCorrecVal, messageEmptyNormal, messageEmptyTitleproper, messageEmptyPreviousLang, messagePleaseSaveChanges){
	globalMessageNormalCorrecVal = messageNormalCorrecVal;
	globalMessageEmptyNormal = messageEmptyNormal;
	globalMessageEmptyPreviousLang = messageEmptyPreviousLang;
	$("#controls").hide();
    $("#eadTree").dynatree({
        title: "Bestandsdelen",
        rootVisible: false,
        autoFocus: false,
        fx: { height: "toggle", duration: 200 },
        // In real life we would call a URL on the server like this:
        initAjax: {
          url: "generateTreeJSONWithoutPreface.action",
          data: {
              fileId: fileId,
              xmlTypeId: xmlTypeId,
              isWithUrl: false
              }
          },

        onActivate: function(dtnode) {
            if(dtnode.data.id)
                correctId = dtnode.data.id;
            else
                correctId = -1;
            if(dtnode.data.more == null){
            	createColorboxForProcessing();
                $.post("editEadXml.action", {id: correctId, fileId: fileId, xmlTypeId: xmlTypeId}, function(databack){
                    if(databack.xml) {
                        $("#editionFormContainer").html(databack.xml);
                        executeActionsWhenLoadXML(fileId,xmlTypeId, messageEmptyWhenSave, messageInvalidCountrycode, messageEmptyCountrycode, messageEmptyMainagencycode, messageEmptyEADID, messageEmptyNormal, messageNormalCorrecVal, messageEmptyTitleproper);
                    }
                    deleteColorboxForProcessing();
                }, "json");
            }
            cleanInformation();
        },

        onClick: function(dtnode, event) {
        	var value = $("input#changed").val();
        	if (value == "true") {
        		alert(messagePleaseSaveChanges);
        		return false;
        	}
        },

        onLazyRead: function(dtnode){
            if (dtnode.data.more == "after"){
                dtnode.parent.appendAjaxWithoutRemove({
                    url: "generateTreeJSON.action",
                    data: {
                        parentId: dtnode.data.parentId,
                        orderId: dtnode.data.orderId,
                        ecId: dtnode.data.ecId,
                        more: dtnode.data.more,
                        isWithUrl: false
                        }
                });
                var parent = dtnode.parent;
                dtnode.remove();
                var children = parent.getChildren();
                var index = children.length-1;
                var lastChild = children[index];
                var relativeTop =  $('#tree').scrollTop() + $(lastChild.span).offset().top - $("#tree").offset().top - 40;
	             $('#tree').animate({scrollTop: relativeTop}, 500);
            } else if (dtnode.data.more == "before"){
                dtnode.parent.insertBeforeAjaxWithoutRemove({
                    url: "generateTreeJSON.action",
                    data: {
                        parentId: dtnode.data.parentId,
                        orderId: dtnode.data.orderId,
                        ecId: dtnode.data.ecId,
                        more: dtnode.data.more,
                        max: dtnode.data.max,
                        isWithUrl: false
                        }
              }, dtnode);
              dtnode.remove();
            }

            else {
                dtnode.appendAjax({
                    url: "generateTreeJSON.action",
                    data: {parentId: dtnode.data.id, isWithUrl: false}

                });
            }
            cleanInformation();
        },
		onSelect: function(select,node){
			node.select(select);
		},
        minExpandLevel: 2,
        generateIds: true
    });
}

/**
 * Function to execute all the necessary actions when a new XML (part of EAD file)
 * is loaded.
 */
function executeActionsWhenLoadXML(fileId,xmlTypeId, messageEmptyWhenSave, messageInvalidCountrycode, messageEmptyCountrycode, messageEmptyMainagencycode, messageEmptyEADID, messageEmptyNormal, messageNormalCorrecVal, messageEmptyTitleproper) {
	// Initialize the buttons panel.
    initButtons(fileId,xmlTypeId, messageEmptyWhenSave, messageInvalidCountrycode, messageEmptyCountrycode, messageEmptyMainagencycode, messageEmptyEADID, messageEmptyNormal, messageEmptyTitleproper);
    // Checks the value of the countrycode when is modified.
    checkCountryCodeValue(messageEmptyCountrycode);
    // Checks the value of the mainagencycode when is modified.
    checkMainagencycodeValue(messageEmptyMainagencycode);
    // Checks the value of the EADID when is modified.
    checkEADIDValue(messageEmptyEADID);
    // Checks the value of the attribute normal when is modified.
    checkNormalValue(messageNormalCorrecVal, messageEmptyNormal);
    // Checks the value of the element titleproper.
    checkTitleproperValue(messageEmptyTitleproper);
    // Checks if needed to add "onclick" actions.
    addOnclickActionToButtons();
    // Add on change value to inputs and selects.
    addOnChangeValue();
}

function initButtons(fileId,xmlTypeId, messageEmptyWhenSave, messageInvalidCountrycode, messageEmptyCountrycode, messageEmptyMainagencycode, messageEmptyEADID, messageEmptyNormal, messageEmptyTitleproper){
	if ($("#controls").is(':hidden')) {
		$("#controls").show();

		// Action for button save.
	    $("#saveEADButton").click(function(){
	    	cleanInformation();
	    	saveEAD(fileId,xmlTypeId, messageEmptyWhenSave, messageInvalidCountrycode, messageEmptyCountrycode, messageEmptyMainagencycode, messageEmptyEADID, messageEmptyNormal, messageEmptyTitleproper);
	    });
	}
}

function saveEAD(fileId,xmlTypeId, messageEmptyWhenSave, messageInvalidCountrycode, messageEmptyCountrycode, messageEmptyMainagencycode, messageEmptyEADID, messageEmptyNormal, messageEmptyTitleproper){
	// Remove the previous alerts.
	removeAlerts();

	// Checks all the information inserted.
	if (!checkAllData(fileId, messageEmptyEADID, messageEmptyWhenSave, messageInvalidCountrycode, messageEmptyCountrycode, messageEmptyMainagencycode, messageEmptyNormal, messageNormalCorrecVal, messageEmptyTitleproper)) {
		return;
	}

	// Try to save the data.
	var node = $("#eadTree").dynatree("getActiveNode");
	//get all input and selected/option info into editionElement div
	var start = "{";
	var content = start;
	$("p#editionFormContainer div .editionElement").find("input").each(function() {
		// Check if it's necessary to add the current value.
		if ($(this).val() != undefined) {
			content += (content.length>start.length) ? "," : "";
			var value = $(this).val();
			while (value.indexOf("'") != -1) {
				value = value.replace(/'/g, "%27");
			}
			content += "'" + $(this).attr("name") + "':" + "'" + value + "'";
		}
	});
	$("p#editionFormContainer select").each(function() {
		// Check if it's necessary to add the current value.
		if ($(this).find("option:selected").val() != undefined) {
			content += (content.length>1) ? "," : "";
			if ($(this).find("option:selected").val() == "---") {
				content += "'" + $(this).attr("name") + "':" + "''";
			} else {
				content += "'" + $(this).attr("name") + "':" + "'" + $(this).find("option:selected").val() + "'";
			}
		}
	});

	// Hide elements ans show processing info.
	$("p#editionFormContainer").hide();
	createColorboxForProcessing();

	// Add "id" if exists.
	if (node.data.id != undefined) {
		content += ",'id':'"+node.data.id+"'";
	}
	// Add "key" if exists.
	if (node.data.key != undefined) {
		content += ",'key':'"+node.data.key+"'";
	}
	// Add "file id" if exists.
	if (fileId != undefined) {
		content += ",'fileId':'"+fileId+"'";
	}
	// Add "xml type Id" if exists.
	if (xmlTypeId != undefined) {
		content += ",'xmlTypeId':'"+xmlTypeId+"'";
	}
	content += "}";

	var dynatree = $("#eadTree").dynatree("getTree");

	// Hide the content.
	$("p#editionFormContainer").hide();
	// Hide the buttons.
	$("#controls").hide();

	content = encodeURIComponent(content);
	content = "'formValues'=" + content;

	$.post("editEadXmlSaveLevel.action", content, function(data) {
		//savedText
		if (data != undefined
				&& data.saved != undefined) {
			if (data.saved) {
				dynatree.reload();
				displayNode(node, data.savedText);
				$("div#right-pane input#changed").val("false");
			} else {
				showInformation(data.savedText, true);
			}
		} else {
			showInformation("", true);
		}
	});
}

/**
 * Function to remove all the previous displayed alerts.
 */
function removeAlerts(){
	// Remove countrycode alerts.
	$("p#alertCountryCode").remove();
	// Remove mainagencycode alerts.
	$("p#alertMainagencyname").remove();
	// Remove EADID alerts.
	$("p#alertEADID").remove();
	// Remove titleproper alerts.
	$("p[id^='alertTitleproper']").each(function(){
		$(this).remove();
	});
	// Remove @normal alerts.
	$("p[id^='alertNormal']").each(function(){
		$(this).remove();
	});
}

/**
 * Function that loads all the methods to check if the values inserted are correct ones.
 *
 * @param fileId
 * @param messageEmptyEADID
 * @param messageEmptyWhenSave
 * @param messageInvalidCountrycode
 * @param messageEmptyCountrycode
 * @param messageEmptyMainagencycode
 * @param messageEmptyNormal
 * @param messageNormalCorrecVal
 * @param messageEmptyTitleproper
 * @returns
 */
function checkAllData(fileId, messageEmptyEADID, messageEmptyWhenSave, messageInvalidCountrycode, messageEmptyCountrycode, messageEmptyMainagencycode, messageEmptyNormal, messageNormalCorrecVal, messageEmptyTitleproper) {
	var result = true;

	// First, checks if the countrycode is valid.
	if (!isValidCountryCode(messageEmptyWhenSave, messageInvalidCountrycode, messageEmptyCountrycode, result)) {
		result = false;
	}

	// Second, checks if the mainagencycode is not empty.
	if(isEmptyMainagencycode(messageEmptyMainagencycode, result)) {
		result = false;
	}

	// Third, checks if the EADID is not empty.
	if (isEmptyEADID(messageEmptyWhenSave, messageEmptyEADID, result)) {
		result = false;
	}

	// Fourth, checks the availability of the new EADID.
	if (result) {
		if (!isNewEADIDavailable(fileId)) {
			result = false;
		}
	}

	// Fifth, checks if all elements "titleproper" has content.
	if (isEmptyTitleproper(messageEmptyWhenSave, messageEmptyTitleproper, result)) {
		result = false;
	}

	// Sixth , checks if all the attributes "normal" has content.
	// And checks if all the attributes "normal" has correct content.
	if (isEmptyNormal(messageEmptyWhenSave, messageEmptyNormal, result)) {
		result = false;
	} else if (isCorrectNormal(messageNormalCorrecVal, result)) {
		result = false;
	}

	return result;
}

/**
 * Function to check if countrycode is valid.
 *
 * @param messageEmptyWhenSave
 * @param messageInvalidCountrycode
 * @param messageEmptyCountrycode
 * @param showAlert
 * @returns {Boolean}
 */
function isValidCountryCode(messageEmptyWhenSave, messageInvalidCountrycode, messageEmptyCountrycode, showAlert) {
	var result = true;
	$("input[name^='eadid_countrycode']").each(function(){
		var name = $(this).attr("name");
		var value = $.trim($("input[name="+ name + "]").val());
		if(value.length <= 0) {
			if (showAlert) {
				alert(messageEmptyWhenSave);
			}
			result = false;
			$("input[name=" + name + "]").after("<p id=\"alertCountryCode\" class=\"alertMessage\">" + messageEmptyCountrycode + "</p>");
			$("input[name=" + name + "]").val(value);
			$('html, body').stop().animate({
		        'scrollTop': $("p#editionFormContainer").offset().top
		    }, 900, 'swing', function () {
		    	//logAction("scroll moved to: ", $("#eagDetails").offset().top);
		    });
		} else {
			//begin pattern check
			var pattern = new RegExp("(AF|AX|AL|DZ|AS|AD|AO|AI|AQ|AG|AR|AM|AW|AU|AT|AZ|BS|BH|BD|BB|BY|BE|BZ|BJ|BM|BT|BO|BA|BW|BV|BR|IO|BN|BG|BF|BI|KH|CM|CA|CV|KY|CF|TD|CL|CN|CX|CC|CO|KM|CG|CD|CK|CR|CI|HR|CU|CY|CZ|DK|DJ|DM|DO|EC|EG|SV|GQ|ER|EE|ET|FK|FO|FJ|FI|FR|GF|PF|TF|GA|GM|GE|DE|GH|GI|GR|GL|GD|GP|GU|GT|GN|GW|GY|HT|HM|VA|HN|HK|HU|IS|IN|ID|IR|IQ|IE|IL|IT|JM|JP|JO|KZ|KE|KI|KP|KR|KW|KG|LA|LV|LB|LS|LR|LY|LI|LT|LU|MO|MK|MG|MW|MY|MV|ML|MT|MH|MQ|MR|MU|YT|MX|FM|MD|MC|MN|MS|MA|MZ|MM|NA|NR|NP|NL|AN|NC|NZ|NI|NE|NG|NU|NF|MP|NO|OM|PK|PW|PS|PA|PG|PY|PE|PH|PN|PL|PT|PR|QA|RE|RO|RU|RW|SH|KN|LC|PM|VC|WS|SM|ST|SA|SN|CS|SC|SL|SG|SK|SI|SB|SO|ZA|GS|ES|LK|SD|SR|SJ|SZ|SE|CH|SY|TW|TJ|TZ|TH|TL|TG|TK|TO|TT|TN|TR|TM|TC|TV|UG|UA|AE|GB|US|UM|UY|UZ|VU|VE|VN|VG|VI|WF|EH|YE|ZM|ZW|RS|ME|EU)+$");
			var resultTest = pattern.test(value);
			if(!resultTest){
				if (showAlert) {
					alert(messageInvalidCountrycode);
				}
				result = false;
				$("input[name=" + name + "]").after("<p id=\"alertCountryCode\" class=\"alertMessage\">" + messageInvalidCountrycode + "</p>");
				$("input[name=" + name + "]").val(value);
				$('html, body').stop().animate({
			        'scrollTop': $("p#editionFormContainer").offset().top
			    }, 900, 'swing', function () {
			    	//logAction("scroll moved to: ", $("#eagDetails").offset().top);
			    });
			}
		}
	});

	return result;
} 

/**
 * Function to check if mainagencycode is empty.
 *
 * @param messageEmptyMainagencycode
 * @param showAlert
 * @returns {Boolean}
 */
function isEmptyMainagencycode(messageEmptyMainagencycode, showAlert) {
	var result = false;
	$("input[name^='eadid_mainagencycode']").each(function(){
		var name = $(this).attr("name");
		var value = $.trim($("input[name="+ name + "]").val());
		if(value.length <= 0) {
			if (showAlert) {
				alert(messageEmptyWhenSave);
			}
			result = true;
			$("input[name=" + name + "]").after("<p id=\"alertMainagencyname\" class=\"alertMessage\">" + messageEmptyMainagencycode + "</p>");
			$("input[name=" + name + "]").val(value);
			$('html, body').stop().animate({
		        'scrollTop': $("p#editionFormContainer").offset().top
		    }, 900, 'swing', function () {
		    	//logAction("scroll moved to: ", $("#eagDetails").offset().top);
		    });
		}
	});
	return result;
}

/**
 * Function to check if EADID is empty.
 *
 * @param messageEmptyWhenSave
 * @param messageEmptyEADID
 * @param showAlert
 * @returns {Boolean}
 */
function isEmptyEADID(messageEmptyWhenSave, messageEmptyEADID, showAlert) {
	var result = false;
	$("input[name^='eadid']").each(function(){
		var name = $(this).attr("name");
		var firstIndex = name.indexOf("_");
		var lastIndex = name.lastIndexOf("_");

		if (firstIndex == lastIndex) {
			var value = $.trim($("input[name="+ name + "]").val());
			if(value.length <= 0) {
				if (showAlert) {
					alert(messageEmptyWhenSave);
				}
				result = true;
				$("input[name=" + name + "]").after("<p id=\"alertEADID\" class=\"alertMessage\">" + messageEmptyEADID + "</p>");
				$("input[name=" + name + "]").val(value);
				$('html, body').stop().animate({
			        'scrollTop': $("p#editionFormContainer").offset().top
			    }, 900, 'swing', function () {
			    	//logAction("scroll moved to: ", $("#eagDetails").offset().top);
			    });
			}
		}
	});
	return result;
}

/**
 * Function to checks if the new ID is available.
 *
 * @param fileId
 * @returns {Boolean}
 */
function isNewEADIDavailable(fileId) {
	var result = true;

	$("input[name^='eadid']").each(function(){
		var name = $(this).attr("name");
		var firstIndex = name.indexOf("_");
		var lastIndex = name.lastIndexOf("_");
		if (firstIndex == lastIndex) {
			var neweadid = $.trim($("input[name=" + name + "]").val());
			var oldeadid = $.trim($("input#oldEADID").val());
			if (neweadid != oldeadid) {
				$.ajaxSetup({async: false}); // If timeout needed add ", timeout: 5000"
				$.post("generateEadidResponseJSONWhenEdit.action", 
						{ 'eadid': oldeadid, 'neweadid': neweadid, 'fileId': fileId }, 
						function(dataResponse) {
							if (dataResponse.existingChangeEADIDAnswers == "KO") {
								alert(dataResponse.message);
								result = false;
								$("input[name=" + name + "]").after("<p id=\"alertEADID\" class=\"alertMessage\">" + dataResponse.message + "</p>");
								$("input[name=" + name + "]").val(neweadid);
								$('html, body').stop().animate({
							        'scrollTop': $("p#editionFormContainer").offset().top
							    }, 900, 'swing', function () {
							    	//logAction("scroll moved to: ", $("#eagDetails").offset().top);
							    });
							}
				});
				$.ajaxSetup({async: true});
			}
		}
	});

	return result;
}

/**
 * Function to check the correct value of the "countrycode" field.
 *
 * @param messageEmptyCountrycode
 */
function checkCountryCodeValue(messageEmptyCountrycode) {
	$("input[name^='eadid_countrycode']").each(function(){
		var name = $(this).attr("name");
		$("input[name="+ name + "]").on('input', function() {
			$("p#alertCountryCode").remove();
            cleanInformation();

			var value = $("input[name="+ name + "]").val();
			if(value.length == 0) {
				alert(messageEmptyCountrycode);
			}
    	});
	});
}

function checkMainagencycodeValue(messageEmptyMainagencycode) {
	$("input[name^='eadid_mainagencycode']").each(function(){
		var name = $(this).attr("name");
		$("input[name="+ name + "]").on('input', function() {
			$("p#alertMainagencyname").remove();
            cleanInformation();

			var value = $("input[name="+ name + "]").val();
			if(value.length == 0) {
				alert(messageEmptyMainagencycode);
			}
    	});
	});
}

/**
 * Function to check the correct value of the "EADID" field.
 *
 * @param messageEmptyEADID
 */
function checkEADIDValue(messageEmptyEADID) {
	$("input[name^='eadid']").each(function(){
		var name = $(this).attr("name");
		var firstIndex = name.indexOf("_");
		var lastIndex = name.lastIndexOf("_");

		if (firstIndex == lastIndex) {
			$("input[name="+ name + "]").on('input', function() {
				$("p#alertEADID").remove();
	            cleanInformation();

				var value = $("input[name="+ name + "]").val();
				if(value.length == 0) {
					alert(messageEmptyEADID);
				}
        	});

			// Add the initial value as hidden field.
			var value = $("input[name="+ name + "]").val();
			$("input[name="+ name + "]").before("<input type=\"hidden\" id=\"oldEADID\" value=\""+ value +"\" \">");
		}
	});
}

/**
 * Function to check the correct value of the attribute "normal" in element "unitdate".
 *
 * @param messageNormalCorrecVal
 * @param messageEmptyNormal
 */
function checkNormalValue(messageNormalCorrecVal, messageEmptyNormal) {
	$("input[name^='unitdate_normal']").each(function(){
		var name = $(this).attr("name");
		$("input[name="+ name + "]").on('input', function() {
			var position = name.substring(name.lastIndexOf("_"));
			$("p#alertNormal" + position).remove();
            cleanInformation();

			var value = $("input[name="+ name + "]").val();
			if(value.length > 0) {
				//begin pattern check
				var pattern = new RegExp("^[0-9/\\-]+$");
				var resultTest = pattern.test(value);
				if(!resultTest){
					//The attribute must not include special characters.
					alert(messageNormalCorrecVal);

					// Check char by char to find all the special characters.
					var newString = "";
					$.each(value, function(index, value){
						if (pattern.test(value)) {
							newString += value;
						}
					});

					// Change the content for the correct one.
					$("input[name="+ name + "]").val(newString);
				}
			} else {
				alert(messageEmptyNormal + " " + messageNormalCorrecVal);
			}
    	});
	});
}

/**
 * Function to check if element titleproper is empty.
 *
 * @param messageEmptyWhenSave
 * @param messageEmptyTitleproper
 * @param showAlert
 * @returns {Boolean}
 */
function isEmptyTitleproper(messageEmptyWhenSave, messageEmptyTitleproper, showAlert) {
	var result = false;
	$("input[name^='titleproper']").each(function(){
		var name = $(this).attr("name");
		var value = $.trim($("input[name="+ name + "]").val());
		if(value.length <= 0) {
			var position = name.substring(name.lastIndexOf("_"));
			$("input[name=" + name + "]").after("<p id=\"alertTitleproper" + position + "\" class=\"alertMessage\">" + messageEmptyTitleproper + "</p>");
			$("input[name=" + name + "]").val(value);
			if (!result) {
				if (showAlert) {
					alert(messageEmptyWhenSave);
					$('html, body').stop().animate({
				        'scrollTop': $("input[name=" + name + "]").offset().top - 30
				    }, 900, 'swing', function () {
				    	//logAction("scroll moved to: ", $("#eagDetails").offset().top);
				    });
				}
			}
			result = true;
		}
	});
	return result;
}

/**
 * Function to check if attribute normal is empty.
 *
 * @param messageEmptyWhenSave
 * @param messageEmptyNormal
 * @param showAlert
 * @returns {Boolean}
 */
function isEmptyNormal(messageEmptyWhenSave, messageEmptyNormal, showAlert) {
	var result = false;
	$("input[name^='unitdate_normal']").each(function(){
		var name = $(this).attr("name");
		var value = $.trim($("input[name="+ name + "]").val());
		if(value.length <= 0) {
			var position = name.substring(name.lastIndexOf("_"));
			$("input[name=" + name + "]").after("<p id=\"alertNormal" + position + "\" class=\"alertMessage\">" + messageEmptyNormal + "</p>");
			$("input[name=" + name + "]").val(value);
			if (!result) {
				if (showAlert) {
					alert(messageEmptyWhenSave);
					$('html, body').stop().animate({
				        'scrollTop': $("input[name=" + name + "]").offset().top - 30
				    }, 900, 'swing', function () {
				    	//logAction("scroll moved to: ", $("#eagDetails").offset().top);
				    });
				}
			}
			result = true;
		}
	});
	return result;
}

/**
 * Function to check if attrubute normal is well formed.
 *
 * @param messageNormalCorrecVal
 * @param showAlert
 * @returns {Boolean}
 */
function isCorrectNormal(messageNormalCorrecVal, showAlert) {
	var result = false;
	$("input[name^='unitdate_normal']").each(function(){
		var name = $(this).attr("name");
		var value = $.trim($("input[name="+ name + "]").val());
		// Regular expressions to check the date.
		var patterYear = new RegExp("^(0|1|2)([0-9]{3})$");
		var patterYears = new RegExp("^(0|1|2)([0-9]{3})/(0|1|2)([0-9]{3})$");
		var patterYearMonth = new RegExp("^(0|1|2)([0-9]{3})-(01|02|03|04|05|06|07|08|09|10|11|12)$");
		var patterYearMonthDay = new RegExp("^(0|1|2)([0-9]{3})-(01|02|03|04|05|06|07|08|09|10|11|12)-((0[1-9])|((1|2)[0-9])|(3[0-1]))$");

		var date = value.split("-");
		var matches;

		if (date.length == 1) {
			if (value.indexOf("/") != -1) {
				matches = value.match(patterYears);
			} else {
				matches = value.match(patterYear);
			}
		} else if (date.length == 2) {
			matches = value.match(patterYearMonth);
		} else if (date.length == 3) {
			matches = value.match(patterYearMonthDay);
		} else {
			matches = false;
		}

		if (!matches) {
			var position = name.substring(name.lastIndexOf("_"));
			$("input[name=" + name + "]").after("<p id=\"alertNormal" + position + "\" class=\"alertMessage\">" + messageNormalCorrecVal + "</p>");
			$("input[name=" + name + "]").val(value);
			if (!result) {
				if (showAlert) {
					alert(messageNormalCorrecVal);
					$('html, body').stop().animate({
				        'scrollTop': $("input[name=" + name + "]").offset().top - 30
				    }, 900, 'swing', function () {
				    	//logAction("scroll moved to: ", $("#eagDetails").offset().top);
				    });
				}
			}
			result = true;
		}
	});
	return result;
}

/**
 * Function to check the correct value of the element "titleproper".
 *
 * @param messageEmptyTitleproper The alert message
 */
function checkTitleproperValue(messageEmptyTitleproper) {
	$("input[name^='titleproper']").each(function(){
		var name = $(this).attr("name");
		$("input[name="+ name + "]").on('input', function() {
			var position = name.substring(name.lastIndexOf("_"));
			$("p#alertTitleproper" + position).remove();
            cleanInformation();

			var value = $("input[name="+ name + "]").val();
			if(value.length == 0) {
				alert(messageEmptyTitleproper);
			}
    	});
	});
}

/**
 * Function to add action "onclick" to those buttons that have been created in java classes.
 */
function addOnclickActionToButtons() {
	$("input[name^='btn_']").each(function(){
		var name = $(this).attr("name");
		// Check the tipe of the button.
		if (name.indexOf("_normal_") != -1) {
			$(this).on('click', function(){
				addNormalAttribute(name);
				$("div#right-pane input#changed").val("true");
	            cleanInformation();
			});
		} else if (name.indexOf("_language_") != -1) {
			$(this).on('click', function(){
				addLanguageElement(name);
				$("div#right-pane input#changed").val("true");
	            cleanInformation();
			});
		}
	});
}

/**
 * Function to replace button to add attribute normal for the coresponding label and input.
 *
 * @param name
 */
function addNormalAttribute(name) {
	var inputName = name.substring((name.indexOf("_") + 1));
	// Add the necessary elements and delete the button.
	$("input[name='" + name + "']").before("<span>@normal: <input type=\"text\" value=\"\" name=\"" + inputName + "\"></span>");
	$("input[name='" + name + "']").remove();
	// Call the funtions that checks the correct value of the attribute "normal" in element "unitdate".
	checkNormalValue(globalMessageNormalCorrecVal, globalMessageEmptyNormal);
}

/**
 * Function to add new section for element language.
 *
 * @param name
 */
function addLanguageElement(name) {
	// Check if the previous elelements have content.
	var result = isPreviousLanguageFilled(name);

	if (!result) {
		return;
	}

	// Get the name of the new input.
	var newInputName = name.substring((name.indexOf("_") + 1));
	// Get the name of the new button.
	var newId = name.substring((name.lastIndexOf("_") + 1));
	newId = parseInt(newId) + 1;
	var newButtonName = name.substring(0, name.lastIndexOf("_"));
	newButtonName = newButtonName + "_" + newId;
	// Get the select combo. btn_language_2_8
	var currentSection = name.substring((name.indexOf("_") + 1), name.lastIndexOf("_"));
	currentSection = currentSection.substring((currentSection.lastIndexOf("_") + 1));
	var nameSelect = "language_langcode_" + currentSection + "_" + name.substring((name.lastIndexOf("_") + 1));
	var select = $("select[name^='language_langcode_1_1']").clone();
	select.attr("name", nameSelect);
	select.attr("value","none");

	// Add the div for the new element.
	var html = "<div class=\"editionElement\">" +
			"language: <br>" +
			"<span>@langcode: <input type=\"hidden\" id=\"replaceHidden\"> </span> <br>" +
			"<span>@scriptcode: Latn</span> <br>" +
			"#text: <input type=\"text\" name=\"" + newInputName + "\" value=\"\">" +
			"</div>" +
			"<input type=\"button\" value=\"" + $("input[name='" + name + "']").val() + "\" name=\"" + newButtonName + "\" onclick=\"addLanguageElement(name)\">";

	// Add the new elements and remove the old ones.
	$("input[name='" + name + "']").after(html);
	$("input[name='" + name + "']").remove();
	$("input#replaceHidden").before(select);
	$("input#replaceHidden").remove();
}

/**
 * Function to cheks if the previous sections of language are filled.
 *
 * @param name
 * @returns {Boolean}
 */
function isPreviousLanguageFilled(name) {
	var result = true;
	// Check if the previous elelements have content.
	// Selects.
	var baseNameSelects = name.substring((name.indexOf("_") + 1), name.lastIndexOf("_"));
	baseNameSelects = baseNameSelects.substring(0, (baseNameSelects.indexOf("_") + 1)) + "langcode" + baseNameSelects.substring(baseNameSelects.lastIndexOf("_"));
	$("select[name^='" + baseNameSelects + "']").each(function(){
		var name = $(this).attr("name");
		if ($("select[name^='" + name + "']").val() == "none") {
			alert(globalMessageEmptyPreviousLang);
			result = false;
		}
	});
	// Inputs.
	var baseNameInputs = name.substring((name.indexOf("_") + 1), name.lastIndexOf("_"));
	$("input[name^='" + baseNameInputs + "']").each(function(){
		var name = $(this).attr("name");
		if ($("input[name^='" + name + "']").val() == "") {
			if (result) {
				alert(globalMessageEmptyPreviousLang);
				result = false;
			}
		}
	});

	return result;
}

/**
 * Function to checks the changes in all inputs and selects.
 */
function addOnChangeValue() {
	$("p#editionFormContainer :input").each(function(){
		$(this).on('input', function() {
			$("div#right-pane input#changed").val("true");
		});
	});
	$("p#editionFormContainer select[name^='language_langcode']").each(function(){
		$(this).on('change', function() {
			$("div#right-pane input#changed").val("true");
		});
	});
	$("p#editionFormContainer select[name^='c_level']").each(function(){
		$(this).on('change', function() {
			$("div#right-pane input#changed").val("true");
		});
	});
}

/**
 * Function to display the previous selected node after reload the tree. 
 *
 * @param node
 * @param message
 */
function displayNode(node, message) {
	var parents = new Array();
	if (node.getParent() != null) {
		//get parent structure
		var currentNode = node;
		var i = 0;
		parents[i++] = currentNode;
		do{
			currentNode = currentNode.getParent();
			parents[i++] = currentNode;
		} while (currentNode.getParent() != null);
		//use parent structure to display target node
		expandParents(parents, i-2, message, node); //review i
	}
}

/**
 * Funtion that expands the parents of the selected node.
 *
 * @param parents
 * @param i
 * @param message
 * @param targetNode
 */
function expandParents(parents, i, message, targetNode) {
	var dynatree = $("#eadTree").dynatree("getTree");
	if (i >= 0) {
		var key = "";
		//could be used for dynatree_node[] and string[] keys
		if (parents[i].data) {
			key = parents[i].data.key;
		} else {
			key = parents[i];
		}
		var target = dynatree.getNodeByKey(key);
		if (!target) {
			setTimeout(function(){expandParents(parents, i, message, targetNode);},40);
		} else {
			target.expand(true);
			expandParents(parents, i-1, message, targetNode);
		}
	} else {
//		launchFinalAction();
		setTimeout(function(){launchFinalAction(targetNode.data.key, message);},40);
	}
}

/**
 * Function to check if is the moment of display the result message.
 *
 * @param key
 * @param message
 */
function launchFinalAction(key, message) {
	//targetNode.select(true);
	var dynatree = $("#eadTree").dynatree("getTree");
	var target = dynatree.getNodeByKey(key);
	if (!target) {
		setTimeout(function(){launchFinalAction(key, message);},40);
	} else {
		target.activate(true);
		//target.select(true);
		//targetNode.activate(true);
		showInformation(message);
	}
}

/**
 * Function to display the result message.
 *
 * @param information
 * @param error
 */
function showInformation(information, error) {
	var message = "<span";
	if (error) {
		message += " style=\"color:red;font-weight:bold;\"";
	} else {
		message += " style=\"color:green;\"";
	}
	message += ">"+information+"</span>";
	$("#informationDiv").html(message);
	$("#informationDiv").fadeIn("slow");

	// Show elements ans hide processing info.
	$("p#editionFormContainer").show();
	$("#controls").show();
	if (error) {
		// Show the content.
		$("p#editionFormContainer").show();
		deleteColorboxForProcessing();
	}
}

/**
 * Function to remove the result message.
 */
function cleanInformation(){
	$("#informationDiv").fadeOut("slow");
}

/**
 * Function to display the processing information.
 */
function createColorboxForProcessing() {
	// Create colorbox.
	$.colorbox({html:function(){
			var htmlCode = $("#processingInfoDiv").html();
			return htmlCode;
		},
		overlayClose:false, // Prevent close the colorbox when clicks on window.
		escKey:false, // Prevent close the colorbox when hit escape key.
		innerWidth:"150px",
		innerHeight:"36px",
		initialWidth:"0px",
		initialHeight:"0px"
	});

	// Remove the close button from colorbox.
	$("#cboxClose").remove();

	// Prevent reload page.
	$(document).on("keydown", disableReload);
}

/**
 * Function to prevent reload the page using F5.
 * @param e
 */
function disableReload(e) {
	if (((e.which || e.keyCode) == 116)
			|| (((e.ctrlKey && e.which) || (e.ctrlKey && e.keyCode)) == 82)) {
		e.preventDefault();
	}
};

/**
 * Function to close the processing information.
 */
function deleteColorboxForProcessing() {
	// Close colorbox.
	$.fn.colorbox.close();

	// Enable the page reload using F5.
	$(document).off("keydown", disableReload)
}
