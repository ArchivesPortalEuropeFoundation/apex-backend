var globalMessageNormalWithSpecialChars, globalMessageNormalCorrecVal, globalMessageEmptyNormal, globalMessageEmptyPreviousLang;

function initEadEdition(){
//	initEadTree();
//	alert("yes");
}

function initEadTree(fileId, xmlTypeId, messageSpecialChars, messageEmptyEADID, messageEmptyWhenSave, messageNormalWithSpecialChars, messageNormalCorrecVal, messageEmptyNormal, messageNotCorrectDate, messageEmptyTitleproper, messageEmptyPreviousLang){
	globalMessageNormalWithSpecialChars = messageNormalWithSpecialChars;
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
                $.post("editEadXml.action", {id: correctId, fileId: fileId, xmlTypeId: xmlTypeId}, function(databack){
                    if(databack.xml) {
                        $("#editionFormContainer").html(databack.xml);
                        executeActionsWhenLoadXML(fileId,xmlTypeId, messageEmptyWhenSave, messageEmptyEADID, messageEmptyNormal, messageNotCorrectDate, messageSpecialChars, messageNormalWithSpecialChars, messageNormalCorrecVal, messageEmptyTitleproper);
                    }
                }, "json");
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
        },
        minExpandLevel: 2,
        generateIds: true
    });
}

/**
 * Function to execute all the necessary actions when a new XML (part of EAD file)
 * is loaded.
 */
function executeActionsWhenLoadXML(fileId,xmlTypeId, messageEmptyWhenSave, messageEmptyEADID, messageEmptyNormal, messageNotCorrectDate, messageSpecialChars, messageNormalWithSpecialChars, messageNormalCorrecVal, messageEmptyTitleproper) {
	// Initialize the buttons panel.
    initButtons(fileId,xmlTypeId, messageEmptyWhenSave, messageEmptyEADID, messageEmptyNormal, messageNotCorrectDate, messageEmptyTitleproper);
    // Checks the value of the EADID when is modified.
    checkEADIDValue(messageSpecialChars, messageEmptyEADID);
    // Checks the value of the attribute normal when is modified.
    checkNormalValue(messageNormalWithSpecialChars, messageNormalCorrecVal, messageEmptyNormal);
    // Checks the value of the element titleproper.
    checkTitleproperValue(messageEmptyTitleproper);
    // Checks if needed to add "onclick" actions.
    addOnclickActionToButtons();
}

function initButtons(fileId,xmlTypeId, messageEmptyWhenSave, messageEmptyEADID, messageEmptyNormal, messageNotCorrectDate, messageEmptyTitleproper){
	$("#controls").show();

	// Action for button delete.
	$("#deleteButton").click(function(event){
		confirmed = confirm('dashboard.hgcreation.areyousuredeletechildren');
		//event.preventDefault();

	    if(confirmed){
	        var node = $("#eadTree").dynatree("getActiveNode");
	        $.post("deleteLevelHG.action", {id: node.data.id}, function(databack){
	            if(databack.success){
	                var parent = node.parent;
	                node.remove();
	                parent.render();
	                $("#editionFormContainer").html("");
	            	$("#controls").hide();
	            }
	        });
	    }
	});

	// Action for button save.
    $("#saveEADButton").click(function(){
    	saveEAD(fileId,xmlTypeId, messageEmptyWhenSave, messageEmptyEADID, messageEmptyNormal, messageNotCorrectDate, messageEmptyTitleproper);
    });
}

function saveEAD(fileId,xmlTypeId, messageEmptyWhenSave, messageEmptyEADID, messageEmptyNormal, messageNotCorrectDate, messageEmptyTitleproper){
	// Remove the previous alerts.
	removeAlerts();

	// Checks all the information inserted.
	if (!checkAllData(fileId, messageEmptyEADID, messageEmptyWhenSave, messageEmptyNormal, messageNotCorrectDate, messageNormalCorrecVal, messageEmptyTitleproper)) {
		return;
	}

	// Try to save the data.
	var node = $("#eadTree").dynatree("getActiveNode");
	//get all input and selected/option info into editionElement div
	var start = "'formValues'={";
	var content = start;
	$("p#editionFormContainer div .editionElement").find("input").each(function() {
		// Check if it's necessary to add the current value.
		if ($(this).val() != undefined) {
			content += (content.length>start.length) ? "," : "";
			content += "'" + $(this).attr("name") + "':" + "'" + $(this).val() + "'";
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


	$.post("editEadXmlSaveLevel.action", content, function(data) {
		//savedText
		if (data != undefined
				&& data.saved != undefined /*&& data.saved*/
				&& data.savedText != undefined) {
			alert(data.savedText);
		}
	});
}

/**
 * Function to remove all the previous displayed alerts.
 */
function removeAlerts(){
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
 * @param messageEmpty
 * @param messageEmptyWhenSave
 * @param messageEmptyNormal
 * @param messageNotCorrectDate
 * @param messageNormalCorrecVal
 * @param messageEmptyTitleproper
 * @returns {Boolean}
 */
function checkAllData(fileId, messageEmptyEADID, messageEmptyWhenSave, messageEmptyNormal, messageNotCorrectDate, messageNormalCorrecVal, messageEmptyTitleproper) {
	var result = true;

	// First of all, checks if the EADID is not empty.
	if (isEmptyEADID(messageEmptyWhenSave, messageEmptyEADID, result)) {
		result = false;
	}

	// Second, checks the availability of the new EADID.
	if (result) {
		if (!isNewEADIDavailable(fileId)) {
			result = false;
		}
	}

	// Third, checks if all elements "titleproper" has content.
	if (isEmptyTitleproper(messageEmptyWhenSave, messageEmptyTitleproper, result)) {
		result = false;
	}

	// Fourth, checks if all the attributes "normal" has content.
	// And checks if all the attributes "normal" has correct content.
	if (isEmptyNormal(messageEmptyWhenSave, messageEmptyNormal, result)) {
		result = false;
	} else if (isCorrectNormal(messageNotCorrectDate, messageNormalCorrecVal, result)) {
		result = false;
	}

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
 * Function to check the correct value of the "EADID" field.
 *
 * @param messageSpecialChars
 * @param messageEmptyEADID
 */
function checkEADIDValue(messageSpecialChars, messageEmptyEADID) {
	$("input[name^='eadid']").each(function(){
		var name = $(this).attr("name");
		var firstIndex = name.indexOf("_");
		var lastIndex = name.lastIndexOf("_");

		if (firstIndex == lastIndex) {
			$("input[name="+ name + "]").on('input', function() {
				$("p#alertEADID").remove();
				var value = $("input[name="+ name + "]").val();
				if(value.length > 0) {
					//begin pattern check
					var pattern = new RegExp("^[a-zA-Z0-9\\s\.\\-\\_]+$");
					var resultTest = pattern.test(value);
					if(!resultTest){
						//The EADID must not include special characters.
						alert(messageSpecialChars);

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
 * @param messageNormalWithSpecialChars
 * @param messageNormalCorrecVal
 * @param messageEmptyNormal
 */
function checkNormalValue(messageNormalWithSpecialChars, messageNormalCorrecVal, messageEmptyNormal) {
	$("input[name^='unitdate_normal']").each(function(){
		var name = $(this).attr("name");
		$("input[name="+ name + "]").on('input', function() {
			var position = name.substring(name.lastIndexOf("_"));
			$("p#alertNormal" + position).remove();

			var value = $("input[name="+ name + "]").val();
			if(value.length > 0) {
				//begin pattern check
				var pattern = new RegExp("^[0-9\\-]+$");
				var resultTest = pattern.test(value);
				if(!resultTest){
					//The attribute must not include special characters.
					alert(messageNormalWithSpecialChars + " " + messageNormalCorrecVal);

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
 * @param messageNotCorrectDate
 * @param messageNormalCorrecVal
 * @param showAlert
 * @returns {Boolean}
 */
function isCorrectNormal(messageNotCorrectDate, messageNormalCorrecVal, showAlert) {
	var result = false;
	$("input[name^='unitdate_normal']").each(function(){
		var name = $(this).attr("name");
		var value = $.trim($("input[name="+ name + "]").val());
		// Regular expression to check the date.
		var pattern = new RegExp("(-?(0|1|2)([0-9]{3})(((01|02|03|04|05|06|07|08|09|10|11|12)((0[1-9])|((1|2)[0-9])|(3[0-1])))|-((01|02|03|04|05|06|07|08|09|10|11|12)(-((0[1-9])|((1|2)[0-9])|(3[0-1])))?))?)(/-?(0|1|2)([0-9]{3})(((01|02|03|04|05|06|07|08|09|10|11|12)((0[1-9])|((1|2)[0-9])|(3[0-1])))|-((01|02|03|04|05|06|07|08|09|10|11|12)(-((0[1-9])|((1|2)[0-9])|(3[0-1])))?))?)?");
		if (!value.match(pattern)) {
			var position = name.substring(name.lastIndexOf("_"));
			$("input[name=" + name + "]").after("<p id=\"alertNormal" + position + "\" class=\"alertMessage\">" + messageNormalCorrecVal + "</p>");
			$("input[name=" + name + "]").val(value);
			if (!result) {
				if (showAlert) {
					alert(messageNotCorrectDate + " " + messageNormalCorrecVal);
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
			});
		} else if (name.indexOf("_language_") != -1) {
			$(this).on('click', function(){
				addLanguageElement(name);
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
	checkNormalValue(globalMessageNormalWithSpecialChars, globalMessageNormalCorrecVal, globalMessageEmptyNormal);
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
