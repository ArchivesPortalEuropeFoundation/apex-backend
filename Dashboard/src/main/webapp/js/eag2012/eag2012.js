function hideAndShow(idPrefix,shown){
	//first check if the click action can happens
	if(!($("ul#eag2012TabsContainer li a[href='#"+shown+"']").hasClass("eag2012disabled"))){
		$("div[id^='"+idPrefix+"']").each(function(){
			$(this).hide();
		});
		$("ul#eag2012TabsContainer li a[href^='#tab']").each(function(){
			$(this).removeClass("eag2012currenttab");
		});
		$("div[id='"+shown+"']").show();
		$("ul#eag2012TabsContainer li a[href='#"+shown+"']").addClass("eag2012currenttab");
		if(shown=="tab-accessAndServices" || shown=="tab-contact" || shown=="tab-description"){
			var id = "";
			$("ul#eag2012tabs_institution_tabs li a").each(function(){
				if($(this).hasClass("eag2012currenttab")){
					id = $(this).attr("id");
				}
			});

			id = id.substring(id.lastIndexOf("_"));
			//hide all other tables and show target table
			$("div[id='"+shown+"'] table").each(function(){
				$(this).hide();
				var newId = $(this).attr("id");
				if(newId.indexOf(id)>-1){
					$(this).show();
				}

				var showContactTables = new Array("contactTableVisitorsAddress", "contactTablePostalAddress");
				if (shown == "tab-contact") {
					for (var i = 0; i < showContactTables.length; i++) {
						// Show visitor address tables.
						var parentId = $(this).parent().parent().parent().parent().parent().parent().attr("id");
						if($(this).attr("id").indexOf(showContactTables[i])>-1 && ("contactTable" + id) == parentId){
							$(this).show();
						}
						// Show postal address tables.
						parentId = $(this).parent().parent().parent().parent().attr("id");
						if($(this).attr("id").indexOf(showContactTables[i])>-1 && ("contactTable" + id) == parentId){
							$(this).show();
						}
					}
				}
			});
		}
	}
}

function clickSaveAction(form, text1, error1, error2, error3, error4, error5, error6, error7) {
	// Check fill mandatory fields in tab "your institution".
	var jsonDataYourInstitution =  clickYourInstitutionAction(text1);
	if (!jsonDataYourInstitution) {
		alert(error1);
		return;
	}

	// Check fill mandatory fields in tab "identity".
	var jsonDataIdentity =  clickIdentityAction(text1);
	if (!jsonDataIdentity) {
		alert(error2);
		return;
	}

	// Check fill mandatory fields in tab "contact".
	var jsonDataContact =  checkAllContactTabs(text1);
	if (!jsonDataContact) {
		alert(error3);
		return;
	}

	// Check fill mandatory fields in tab "access and services".
	var jsonDataAccessAndServices =  checkAllAccessAndServicesTabs(text1);
	if (!jsonDataAccessAndServices) {
		alert(error4);
		return;
	}

	// Check fill mandatory fields in tab "description".
	var jsonDataDescription =  checkAllDescriptionTabs(text1);
	if (!jsonDataDescription) {
		alert(error5);
		return;
	}

	// Check fill mandatory fields in tab "control".
	var jsonDataControl =  clickControlAction(text1);
	if (!jsonDataControl) {
		alert(error6);
		return;
	}

	// Check fill mandatory fields in tab "relations".
	var jsonDataRelations =  clickRelationsAction(text1);
	if (!jsonDataRelations) {
		alert(error7);
		return;
	}

	// Create final json object.
	var jsonData =  "{'yourInstitution':[" + jsonDataYourInstitution + "]," +
	"'identity':[" + jsonDataIdentity + "]," +
	"'contact':[" + jsonDataContact + "]," +
	"'accessAndServices':[" + jsonDataAccessAndServices + "]," +
	"'description':[" + jsonDataDescription + "]," +
	"'control':[" + jsonDataControl + "]," +
	"'relations':[" + jsonDataRelations + "]}";

	$.post("storeEAG2012.action", { form:JSON.stringify(jsonData) });
}

function deleteChecks() {
	$('.fieldRequired').remove();
}

var clickYourInstitutionAction = function(text1){
	// Delete old checks
	deleteChecks();

	// Mandatory elements
	var yiMandatoryElements = new Array("textYIInstitutionCountryCode", "textYIIdentifierOfTheInstitution",
	                           "textYINameOfTheInstitution", "selectYINOTISelectLanguage");

	var jsonData = "{";
	
	// Common part.
	$("table#yourInstitutionTable_1 input[type='text']").each(function(){
		if(jsonData.length>1){
			jsonData += ",";
		}
		jsonData += "'"+$(this).attr("id")+"' : '"+$(this).attr("value")+"'";

		// Check fill mandatory fields.
		if ($(this).attr("value") != '') {
			var position = yiMandatoryElements.indexOf($(this).attr("id"));
			yiMandatoryElements.splice(position, 1);
		}
	});
	//select options selected
	$("table#yourInstitutionTable_1 select").each(function(){
		if(jsonData.charAt(jsonData.length-1)!=':'){
			jsonData += ",";
		}
		jsonData += "'"+$(this).attr("id")+"' : '"+$(this).attr("value")+"'";

		// Check fill mandatory fields.
		if ($(this).attr("value") != 'none') {
			var position = yiMandatoryElements.indexOf($(this).attr("id"));
			yiMandatoryElements.splice(position, 1);
		}
	});

	//validation array
	var validationArray = new Array();

	// Visitors address.
	var visitorsAddress = new Array();
	$("table[id^='yiTableVisitorsAddress_']").each(function(){
		var id = $(this).attr("id");
		if(id.indexOf("#")>-1){
			id = id.substring(id.indexOf("#"));
		}
		visitorsAddress.push(id);
	});
	jsonData += ",'visitorsAddress':[";
	for(var j=0; j<visitorsAddress.length; j++) {
		var yiMEVisitorsAddress = new Array("selectYIVASelectLanguage", "textYIStreet",
				"textYICity", "textYICountry");
		
		if(jsonData.substring(jsonData.length-1)!='['){
			jsonData += ",";
		}
		jsonData += "{'"+visitorsAddress[j]+"':{";
		//input type text
		$("#"+visitorsAddress[j]+" input[type='text']").each(function(){
			if(jsonData.charAt(jsonData.length-1)!=':'){
				jsonData += ",";
			}
			jsonData += "'"+$(this).attr("id")+"' : '"+$(this).attr("value")+"'";

			// Check fill mandatory fields.
			if ($(this).attr("value") != '') {
				var position = yiMEVisitorsAddress.indexOf($(this).attr("id"));
				yiMEVisitorsAddress.splice(position, 1);
			}
		});
		//select options selected
		$("#"+visitorsAddress[j]+" select").each(function(){
			if(jsonData.charAt(jsonData.length-1)!=':'){
				jsonData += ",";
			}
			jsonData += "'"+$(this).attr("id")+"' : '"+$(this).attr("value")+"'";

			// Check fill mandatory fields.
			if ($(this).attr("value") != 'none') {
				var position = yiMEVisitorsAddress.indexOf($(this).attr("id"));
				yiMEVisitorsAddress.splice(position, 1);
			}
		});
		if(yiMEVisitorsAddress.length>0){
			validationArray.push(visitorsAddress[j],yiMEVisitorsAddress);
		}
		jsonData += "}}";
	}
	
	jsonData += "]";

	// Postal address.
	var postalAddress = new Array();
	$("table[id^='yiTablePostalAddress_']").each(function(){
		var id = $(this).attr("id");
		if(id.indexOf("#")>-1){
			id = id.substring(id.indexOf("#"));
		}
		postalAddress.push(id);
	});
	jsonData += ",'postalAddress':[";
	for(var j=0; j<postalAddress.length; j++) {
		var yiMEPostalAddress = new Array("selectYIPASelectLanguage", "textYIPAStreet",
				"textYIPACity");
		
		if(jsonData.substring(jsonData.length-1)!='['){
			jsonData += ",";
		}
		jsonData += "{'"+postalAddress[j]+"':{";
		//input type text
		$("#"+postalAddress[j]+" input[type='text']").each(function(){
			if(jsonData.charAt(jsonData.length-1)!=':'){
				jsonData += ",";
			}
			jsonData += "'"+$(this).attr("id")+"' : '"+$(this).attr("value")+"'";

			// Check fill mandatory fields.
			if ($(this).attr("value") != '') {
				var position = yiMEPostalAddress.indexOf($(this).attr("id"));
				yiMEPostalAddress.splice(position, 1);
			}
		});
		//select options selected
		$("#"+postalAddress[j]+" select").each(function(){
			if(jsonData.charAt(jsonData.length-1)!=':'){
				jsonData += ",";
			}
			jsonData += "'"+$(this).attr("id")+"' : '"+$(this).attr("value")+"'";

			// Check fill mandatory fields.
			if ($(this).attr("value") != 'none') {
				var position = yiMEPostalAddress.indexOf($(this).attr("id"));
				yiMEPostalAddress.splice(position, 1);
			}
		});
		if(yiMEPostalAddress.length>0){
			validationArray.push(postalAddress[j],yiMEPostalAddress);
		}
		jsonData += "}}";
	}
	
	jsonData += "]";

	// Other fields.
	var yiMERepositories = new Array("textYIOpeningTimes");

	//input type text
	$("#yiTableOthers input[type='text']").each(function(){
		if(jsonData.charAt(jsonData.length-1)!=':'){
			jsonData += ",";
		}
		jsonData += "'"+$(this).attr("id")+"' : '"+$(this).attr("value")+"'";

		// Check fill mandatory fields.
		if ($(this).attr("value") != '') {
			var position = yiMERepositories.indexOf($(this).attr("id"));
			yiMERepositories.splice(position, 1);
		}
	});
	//select options selected
	$("#yiTableOthers select").each(function(){
		if(jsonData.charAt(jsonData.length-1)!=':'){
			jsonData += ",";
		}
		jsonData += "'"+$(this).attr("id")+"' : '"+$(this).attr("value")+"'";
	});
	if(yiMERepositories.length>0){
		validationArray.push("yiTableOthers",yiMERepositories);
	}
	jsonData += "}";

	for (var i = 0; i < yiMandatoryElements.length; i++) {
		var element = document.getElementById(yiMandatoryElements[i].toString());
		var subelement = document.createElement('p');
		
		subelement.appendChild(document.createTextNode(text1));
		subelement.id = yiMandatoryElements[i].toString() + '_required';
		subelement.className="fieldRequired";
		element.parentNode.insertBefore(subelement, element.nextSibling);
	}

	for (var i = 0; i < validationArray.length; i = (i + 2)) {
		var array = validationArray[i+1];

		for (var j = 0; j < array.length; j++) {
			var subelement = document.createElement('p');

			subelement.appendChild(document.createTextNode(text1));
			subelement.id = array[j].toString() + '_required';
			subelement.className="fieldRequired";

			$('#' + validationArray[i].toString()).find('#' + array[j].toString()).after(subelement);
		}
	}

	if (yiMandatoryElements.length != 0 || validationArray.length != 0) {
		return false;
	}

	return jsonData;
};

var clickIdentityAction = function(text1){
	var jsonData = "{";
	
	// Table identityTable.
	//content from texts
	$("table#identityTable input[type='text']").each(function(){
		if(jsonData.length>1){
			jsonData += ",";
		}
		jsonData += "'"+$(this).attr("id")+"' : '"+$(this).attr("value")+"'";
	});
	//content from selects
	$("table#identityTable select").each(function(){
		if(jsonData.charAt(jsonData.length-1)!=':'){
			jsonData += ",";
		}
		jsonData += "'"+$(this).attr("id")+"' : '"+$(this).attr("value")+"'";
	});

	// Institution names.
	var institutionNames = new Array();
	$("table[id^='identityTableNameOfTheInstitution_']").each(function(){
		var id = $(this).attr("id");
		if(id.indexOf("#")>-1){
			id = id.substring(id.indexOf("#"));
		}
		institutionNames.push(id);
	});

	jsonData += ",'institutionNames':[";
	for(var j=0; j<institutionNames.length; j++) {
		if(jsonData.substring(jsonData.length-1)!='['){
			jsonData += ",";
		}
		jsonData += "{'"+institutionNames[j]+"':{";
		//input type text
		$("#"+institutionNames[j]+" input[type='text']").each(function(){
			if(jsonData.charAt(jsonData.length-1)!=':'){
				jsonData += ",";
			}
			jsonData += "'"+$(this).attr("id")+"' : '"+$(this).attr("value")+"'";
		});
		//select options selected
		$("#"+institutionNames[j]+" select").each(function(){
			if(jsonData.charAt(jsonData.length-1)!=':'){
				jsonData += ",";
			}
			jsonData += "'"+$(this).attr("id")+"' : '"+$(this).attr("value")+"'";
		});
		jsonData += "}}";
	}
	jsonData += "]";

	// Parallel names.
	var parallelNames = new Array();
	$("table[id^='identityTableParallelNameOfTheInstitution_']").each(function(){
		var id = $(this).attr("id");
		if(id.indexOf("#")>-1){
			id = id.substring(id.indexOf("#"));
		}
		parallelNames.push(id);
	});

	jsonData += ",'parallelNames':[";
	for(var j=0; j<parallelNames.length; j++) {
		if(jsonData.substring(jsonData.length-1)!='['){
			jsonData += ",";
		}
		jsonData += "{'"+parallelNames[j]+"':{";
		//input type text
		$("#"+parallelNames[j]+" input[type='text']").each(function(){
			if(jsonData.charAt(jsonData.length-1)!=':'){
				jsonData += ",";
			}
			jsonData += "'"+$(this).attr("id")+"' : '"+$(this).attr("value")+"'";
		});
		//select options selected
		$("#"+parallelNames[j]+" select").each(function(){
			if(jsonData.charAt(jsonData.length-1)!=':'){
				jsonData += ",";
			}
			jsonData += "'"+$(this).attr("id")+"' : '"+$(this).attr("value")+"'";
		});
		jsonData += "}}";
	}
	jsonData += "]";

	// Formerly names.
	var formerlyNames = new Array();
	$("table[id^='identityTableFormerlyUsedName_']").each(function(){
		var id = $(this).attr("id");
		if(id.indexOf("#")>-1){
			id = id.substring(id.indexOf("#"));
		}
		formerlyNames.push(id);
	});

	jsonData += ",'formerlyNames':[";
	for(var j=0; j<formerlyNames.length; j++) {
		if(jsonData.substring(jsonData.length-1)!='['){
			jsonData += ",";
		}
		jsonData += "{'"+formerlyNames[j]+"':{";
		//input type text
		$("#"+formerlyNames[j]+" input[type='text']").each(function(){
			if(jsonData.charAt(jsonData.length-1)!=':'){
				jsonData += ",";
			}
			jsonData += "'"+$(this).attr("id")+"' : '"+$(this).attr("value")+"'";
		});
		//select options selected
		$("#"+formerlyNames[j]+" select").each(function(){
			if(jsonData.charAt(jsonData.length-1)!=':'){
				jsonData += ",";
			}
			jsonData += "'"+$(this).attr("id")+"' : '"+$(this).attr("value")+"'";
		});
		jsonData += "}}";
	}
	jsonData += "]}";

	return jsonData;
};

var clickContactAction = function(text1){
	var currentTab = getCurrentTab();

	return checkContactTab(currentTab, text1);
};

function checkAllContactTabs(text1) {
	var counter = $("table[id^='contactTable_']").length;
	var jsonData = "";

	for (var i = 1; i <= counter; i++) {
		if(jsonData.substring(jsonData.length-1)=='}'){
			jsonData += ",";
		}

		jsonData += "{'contactTable_" + i + "':[";

		jsonData += checkContactTab("_" + i, text1);
		
		jsonData += "]}";
	}

	return jsonData;
};

function checkContactTab(currentTab, text1) {
	var jsonData = "{";
	//content from texts
	$("table#contactTable" + currentTab + " input[type='text']").each(function(){
		if(jsonData.length>1){
			if(jsonData.substring(jsonData.length-1)!=','){
				jsonData += ",";
			}
		}
		if ($(this).parent().parent().parent().parent().attr("id").indexOf("contactTableVisitorsAddress") == -1) {
			if ($(this).parent().parent().parent().parent().attr("id").indexOf("contactTablePostalAddress") == -1) {
				jsonData += "'"+$(this).attr("id")+"' : '"+$(this).attr("value")+"'";
			}
		}
	});
	//content from selects
	$("table#contactTable" + currentTab + " select").each(function(){
		if(jsonData.charAt(jsonData.length-1)!=':'){
			if(jsonData.substring(jsonData.length-1)!=','){
				jsonData += ",";
			}
		}


		if ($(this).parent().parent().parent().parent().attr("id").indexOf("contactTableVisitorsAddress") == -1) {
			if ($(this).parent().parent().parent().parent().attr("id").indexOf("contactTablePostalAddress") == -1) {
				jsonData += "'"+$(this).attr("id")+"' : '"+$(this).attr("value")+"'";
			}
		}
	});

	// Visitors address.
	var visitorsAddress = new Array();
	$("table#contactTable" + currentTab + " table[id^='contactTableVisitorsAddress_']").each(function(){
		var id = $(this).attr("id");
		if(id.indexOf("#")>-1){
			id = id.substring(id.indexOf("#"));
		}
		visitorsAddress.push(id);
	});
	jsonData += ",'visitorsAddress':[";
	for(var j=0; j<visitorsAddress.length; j++) {
		if(jsonData.substring(jsonData.length-1)!='['){
			if(jsonData.substring(jsonData.length-1)!=','){
				jsonData += ",";
			}
		}
		jsonData += "{'"+visitorsAddress[j]+"':{";
		//input type text
		$("table#contactTable" + currentTab + " table#"+visitorsAddress[j]+" input[type='text']").each(function(){
			if(jsonData.charAt(jsonData.length-1)!=':'){
				if(jsonData.substring(jsonData.length-1)!=','){
					jsonData += ",";
				}
			}
			if ($(this).parent().parent().parent().parent().parent().parent().parent().parent().parent().attr("id").indexOf("divTempContainter") == -1) {
				jsonData += "'"+$(this).attr("id")+"' : '"+$(this).attr("value")+"'";
			}
		});
		//select options selected
		$("table#contactTable" + currentTab + " table#"+visitorsAddress[j]+" select").each(function(){
			if(jsonData.charAt(jsonData.length-1)!=':'){
				if(jsonData.substring(jsonData.length-1)!=','){
					jsonData += ",";
				}
			}
			if ($(this).parent().parent().parent().parent().parent().parent().parent().parent().parent().attr("id").indexOf("divTempContainter") == -1) {
				jsonData += "'"+$(this).attr("id")+"' : '"+$(this).attr("value")+"'";
			}
		});
		jsonData += "}}";
	}
	
	jsonData += "]";

	// Postal address.
	var postalAddress = new Array();
	$("table#contactTable" + currentTab + " table[id^='contactTablePostalAddress_']").each(function(){
		var id = $(this).attr("id");
		if(id.indexOf("#")>-1){
			id = id.substring(id.indexOf("#"));
		}
		postalAddress.push(id);
	});
	jsonData += ",'postalAddress':[";
	for(var j=0; j<postalAddress.length; j++) {		
		if(jsonData.substring(jsonData.length-1)!='['){
			jsonData += ",";
		}
		jsonData += "{'"+postalAddress[j]+"':{";
		//input type text
		$("table#contactTable" + currentTab + " table#"+postalAddress[j]+" input[type='text']").each(function(){
			if(jsonData.charAt(jsonData.length-1)!=':'){
				jsonData += ",";
			}
			jsonData += "'"+$(this).attr("id")+"' : '"+$(this).attr("value")+"'";
		});
		//select options selected
		$("table#contactTable" + currentTab + " table#"+postalAddress[j]+" select").each(function(){
			if(jsonData.charAt(jsonData.length-1)!=':'){
				jsonData += ",";
			}
			jsonData += "'"+$(this).attr("id")+"' : '"+$(this).attr("value")+"'";
		});
		jsonData += "}}";
	}
	
	jsonData += "]}";

	return jsonData;
};

var clickAccessAndServicesAction = function(text1){
	var currentTab = getCurrentTab();

	return checkAccessAndServicesTab(currentTab, text1);
};

function checkAllAccessAndServicesTabs(text1) {
	var counter = $("table[id^='accessAndServicesTable_']").length;
	var jsonData = "";

	for (var i = 1; i <= counter; i++) {
		if(jsonData.substring(jsonData.length-1)=='}'){
			jsonData += ",";
		}

		jsonData += "{'accessAndServicesTable_" + i + "':[";

		jsonData += checkAccessAndServicesTab("_" + i, text1);
		
		jsonData += "]}";
	}

	return jsonData;
};


function checkAccessAndServicesTab(currentTab, text1) {
	// Delete old checks
	deleteChecks();

	// Mandatory elements
	var aasMandatoryElements = new Array("textASSRWorkPlaces");

	var jsonData = "{";
	//content from textareas
	$("table#accessAndServicesTable" + currentTab + " textarea").each(function(){
		if(jsonData.length>1){
			jsonData += ",";
		}
		if ($(this).attr("id") == "textTravellingDirections"
			|| $(this).attr("id") == "textASTermOfUse") {
				jsonData += "'"+$(this).attr("id")+"_1' : '"+$(this).attr("value")+"'";
		} else {
			jsonData += "'"+$(this).attr("id")+"' : '"+$(this).attr("value")+"'";
		}
	});
	//content from texts
	$("table#accessAndServicesTable" + currentTab + " input[type='text']").each(function(){
		if(jsonData.length>1){
			jsonData += ",";
		}
		if ($(this).attr("id") == "textOpeningTimes"
			|| $(this).attr("id") == "textClosingDates"
			|| $(this).attr("id") == "textTravelLink"
			|| $(this).attr("id") == "textASAccessRestrictions"
			|| $(this).attr("id") == "textASTOULink"
			|| $(this).attr("id") == "textASAccessibility"
			|| $(this).attr("id") == "textDescriptionOfYourComputerPlaces"
			|| $(this).attr("id") == "textASSRReadersTicket"
			|| $(this).attr("id") == "textASSRRTLink"
			|| $(this).attr("id") == "textASSRAdvancedOrders"
			|| $(this).attr("id") == "textASSRAOLink"
			|| $(this).attr("id") == "textASSRResearchServices"
			|| $(this).attr("id") == "textASDescription"
			|| $(this).attr("id") == "textASTSDescriptionOfRestaurationLab"
			|| $(this).attr("id") == "textASTSDescriptionOfReproductionService"
			|| $(this).attr("id") == "textASReSeExhibition"
			|| $(this).attr("id") == "textASReSeWebpage"
			|| $(this).attr("id") == "textASReSeWebpageLinkTitle"
			|| $(this).attr("id") == "textASReSeToursAndSessions"
			|| $(this).attr("id") == "textASReSeTSWebpage"
			|| $(this).attr("id") == "textASReSeWebpageTSLinkTitle"
			|| $(this).attr("id") == "textASReSeOtherServices"
			|| $(this).attr("id") == "textASReSeOSWebpage"
			|| $(this).attr("id") == "textASReSeWebpageOSLinkTitle") {
				jsonData += "'"+$(this).attr("id")+"_1' : '"+$(this).attr("value")+"'";
		} else {
			jsonData += "'"+$(this).attr("id")+"' : '"+$(this).attr("value")+"'";
		}

		// Check fill mandatory fields.
		if ($(this).attr("value") != '') {
			var position = aasMandatoryElements.indexOf($(this).attr("id"));
			aasMandatoryElements.splice(position, 1);
		}
	});
	//content from selects
	$("table#accessAndServicesTable" + currentTab + " select").each(function(){
		if(jsonData.charAt(jsonData.length-1)!=':'){
			jsonData += ",";
		}
		if ($(this).attr("id") == "selectLanguageOpeningTimes"
			|| $(this).attr("id") == "selectLanguageClosingDates"
			|| $(this).attr("id") == "selectASATDSelectLanguage"
			|| $(this).attr("id") == "selectASARSelectLanguage"
			|| $(this).attr("id") == "selectASAFTOUSelectLanguage"
			|| $(this).attr("id") == "selectASASelectLanguage"
			|| $(this).attr("id") == "selectDescriptionOfYourComputerPlaces"
			|| $(this).attr("id") == "selectReadersTickectLanguage"
			|| $(this).attr("id") == "selectASSRAFOIUSelectLanguage"
			|| $(this).attr("id") == "textASSRRSSelectLanguage"
			|| $(this).attr("id") == "selectASDSelectLanguage"
			|| $(this).attr("id") == "selectASTSSelectLanguage"
			|| $(this).attr("id") == "selectASTSRSSelectLanguage"
			|| $(this).attr("id") == "selectASReSeExhibitionSelectLanguage"
			|| $(this).attr("id") == "selectASReSeToursAndSessionsSelectLanguage"
			|| $(this).attr("id") == "selectASReSeOtherServicesSelectLanguage") {
				jsonData += "'"+$(this).attr("id")+"_1' : '"+$(this).attr("value")+"'";
		} else {
			jsonData += "'"+$(this).attr("id")+"' : '"+$(this).attr("value")+"'";
		}
	});
	jsonData += "}";

	for (var i = 0; i < aasMandatoryElements.length; i++) {
		var element = document.getElementById(aasMandatoryElements[i].toString());
		var subelement = document.createElement('p');
		
		subelement.appendChild(document.createTextNode(text1));
		subelement.id = aasMandatoryElements[i].toString() + '_required';
		subelement.className="fieldRequired";
		element.parentNode.insertBefore(subelement, element.nextSibling);
	}

	if (aasMandatoryElements.length != 0) {
		return false;
	}

	return jsonData;
};

var clickDescriptionAction = function(text1){
	var currentTab = getCurrentTab();

	return checkDescriptionTab(currentTab, text1);
};

function checkAllDescriptionTabs(text1) {
	var counter = $("table[id^='descriptionTable_']").length;
	var jsonData = "";

	for (var i = 1; i <= counter; i++) {
		if(jsonData.substring(jsonData.length-1)=='}'){
			jsonData += ",";
		}

		jsonData += "{'descriptionTable_" + i + "':[";

		jsonData += checkDescriptionTab("_" + i, text1);
		
		jsonData += "]}";
	}

	return jsonData;
};

function checkDescriptionTab(currentTab, text1) {
	var jsonData = "{";
	//content from textareas
	$("table#descriptionTable" + currentTab + " textarea").each(function(){
		if(jsonData.length>1){
			jsonData += ",";
		}
		if ($(this).attr("id") == "textRepositoryHistory"
			|| $(this).attr("id") == "textUnitOfAdministrativeStructure"
			|| $(this).attr("id") == "textBuilding"
			|| $(this).attr("id") == "textArchivalAndOtherHoldings") {
			jsonData += "'"+$(this).attr("id")+"_1' : '"+$(this).attr("value")+"'";
		} else {
			jsonData += "'"+$(this).attr("id")+"' : '"+$(this).attr("value")+"'";
		}
	});
	//content from texts
	$("table#descriptionTable" + currentTab + " input[type='text']").each(function(){
		if(jsonData.length>1){
			jsonData += ",";
		}
		if ($(this).attr("id") == "textRuleOfRepositoryFoundation"
			|| $(this).attr("id") == "textRuleOfRepositorySuppression") {
			jsonData += "'"+$(this).attr("id")+"_1' : '"+$(this).attr("value")+"'";
		} else {
			jsonData += "'"+$(this).attr("id")+"' : '"+$(this).attr("value")+"'";
		}
	});
	//content from selects
	$("table#descriptionTable" + currentTab + " select").each(function(){
		if(jsonData.charAt(jsonData.length-1)!=':'){
			jsonData += ",";
		}
		if ($(this).attr("id") == "selectLanguageRepositoryHistory"
			|| $(this).attr("id") == "selectLanguageUnitOfAdministrativeStructure"
			|| $(this).attr("id") == "selectLanguageBuilding"
			|| $(this).attr("id") == "selectLanguageArchivalAndOtherHoldings"
			|| $(this).attr("id") == "selectLanguageRuleOfRepositoryFoundation"
			|| $(this).attr("id") == "selectLanguageRuleOfRepositorySuppression") {
			jsonData += "'"+$(this).attr("id")+"_1' : '"+$(this).attr("value")+"'";
		} else {
			jsonData += "'"+$(this).attr("id")+"' : '"+$(this).attr("value")+"'";
		}
	});
	jsonData += "}";
	
	return jsonData;
}

var clickControlAction = function(text1){
	// Delete old checks
	deleteChecks();

	// Mandatory elements
	var controlMandatoryElements = new Array("selectDescriptionLanguage", "selectDescriptionScript");

	var jsonData = "{";
		//content from texts
		$("table#controlTable input[type='text']").each(function(){
			if(jsonData.length>1){
				jsonData += ",";
			}
			if ($(this).attr("id") == "textContactAbbreviation") {
				jsonData += "'"+$(this).attr("id")+"_1' : '"+$(this).attr("value")+"'";
			}else if ($(this).attr("id") == "textContactFullName") {
				jsonData += "'"+$(this).attr("id")+"_1' : '"+$(this).attr("value")+"'";
			} else {
				jsonData += "'"+$(this).attr("id")+"' : '"+$(this).attr("value")+"'";
			}
		});
		//content from selects
		$("table#controlTable select").each(function(){
			if(jsonData.charAt(jsonData.length-1)!=':'){
				jsonData += ",";
			}
			if ($(this).attr("id") == "selectDescriptionLanguage") {
				jsonData += "'"+$(this).attr("id")+"_1' : '"+$(this).attr("value")+"'";
			}else if ($(this).attr("id") == "selectDescriptionScript") {
				jsonData += "'"+$(this).attr("id")+"_1' : '"+$(this).attr("value")+"'";
			} else {
				jsonData += "'"+$(this).attr("id")+"' : '"+$(this).attr("value")+"'";
			}

			// Check fill mandatory fields.
			if ($(this).attr("value") != 'none') {
				var position = controlMandatoryElements.indexOf($(this).attr("id"));
				controlMandatoryElements.splice(position, 1);
			}
		});
	jsonData += "}";

	for (var i = 0; i < controlMandatoryElements.length; i++) {
		var element = document.getElementById(controlMandatoryElements[i].toString());
		var subelement = document.createElement('p');
		
		subelement.appendChild(document.createTextNode(text1));
		subelement.id = controlMandatoryElements[i].toString() + '_required';
		subelement.className="fieldRequired";
		element.parentNode.insertBefore(subelement, element.nextSibling);
	}

	if (controlMandatoryElements.length != 0) {
		return false;
	}

	return jsonData;
};

var clickRelationsAction = function(text1){
	var jsonData = "{";

	// Resource relations.
	var resourceRelations = new Array();
	$("table[id^='resourceRelationTable_']").each(function(){
		var id = $(this).attr("id");
		if(id.indexOf("#")>-1){
			id = id.substring(id.indexOf("#"));
		}
		resourceRelations.push(id);
	});
	jsonData += "'resourceRelations':[";
	for(var j=0; j<resourceRelations.length; j++) {
		if(jsonData.substring(jsonData.length-1)!='['){
			jsonData += ",";
		}
		jsonData += "{'"+resourceRelations[j]+"':";
		//input type text
		$("#"+resourceRelations[j]+" input[type='text']").each(function(){
			if(jsonData.charAt(jsonData.length-1)!=':'){
				jsonData += ",";
			}
			jsonData += "'"+$(this).attr("id")+"' : '"+$(this).attr("value")+"'";
		});
		//select options selected
		$("#"+resourceRelations[j]+" select").each(function(){
			if(jsonData.charAt(jsonData.length-1)!=':'){
				jsonData += ",";
			}
			jsonData += "'"+$(this).attr("id")+"' : '"+$(this).attr("value")+"'";
		});
		jsonData += "}";
	}
	
	jsonData += "]";

	// Institution relations.
	var institutionRelations = new Array();
	$("table[id^='institutionRelationTable_']").each(function(){
		var id = $(this).attr("id");
		if(id.indexOf("#")>-1){
			id = id.substring(id.indexOf("#"));
		}
		institutionRelations.push(id);
	});
	jsonData += ",'institutionRelations':[";
	for(var j=0; j<institutionRelations.length; j++) {
		if(jsonData.substring(jsonData.length-1)!='['){
			jsonData += ",";
		}
		jsonData += "{'"+institutionRelations[j]+"':";
		//input type text
		$("#"+institutionRelations[j]+" input[type='text']").each(function(){
			if(jsonData.charAt(jsonData.length-1)!=':'){
				jsonData += ",";
			}
			jsonData += "'"+$(this).attr("id")+"' : '"+$(this).attr("value")+"'";
		});
		//select options selected
		$("#"+institutionRelations[j]+" select").each(function(){
			if(jsonData.charAt(jsonData.length-1)!=':'){
				jsonData += ",";
			}
			jsonData += "'"+$(this).attr("id")+"' : '"+$(this).attr("value")+"'";
		});
		jsonData += "}";
	}
	
	jsonData += "]}";

	return jsonData;
};

function yiAddVisitorsAddressTranslation() {
	var counter = $("table[id^='yiTableVisitorsAddress_']").length;
	var clone = $("table[id^='yiTableVisitorsAddress_"+counter+"']").clone();
	clone = "<table id='"+("yiTableVisitorsAddress_"+(counter+1))+"'>"+clone.html()+"</table>";
	$("table[id^='yiTableVisitorsAddress_"+counter+"']").after(clone);
	// Reset parametters.
	$("table#yiTableVisitorsAddress_"+(counter+1)+" input[type='text']").each(function(){
		$(this).val(""); // Clean all input_text.
	});
}
function yiAddPostalAddressTranslation() {
	var counter = $("table[id^='yiTablePostalAddress_']").length;
	var clone = $("table[id^='yiTablePostalAddress_"+counter+"']").clone();
	clone = "<table id='"+("yiTablePostalAddress_"+(counter+1))+"'>"+clone.html()+"</table>";
	$("table[id^='yiTablePostalAddress_"+counter+"']").after(clone);
	// Reset parametters.
	$("table#yiTablePostalAddress_"+(counter+1)+" input[type='text']").each(function(){
		$(this).val(""); // Clean all input_text.
	});
}
function yiAddClosingDates() {
	$("#buttonAddClosingDates").hide();
	$("table#yiTableOthers tr#fieldClosingDates").show();
}
function yiFutherAccessInformation() {
	$("#buttonFutherAccessInformation").hide();
	$("#buttonFutherAccessInformation").after('<input type="text" id="futherAccessInformation" />');
}
function yiAddFutherInformationOnExistingFacilities() {
	$("#buttonAddFutherInformationOnExistingFacilities").after('<input type="text" id="futherInformationOnExistingFacilities" />');
	$("#buttonAddFutherInformationOnExistingFacilities").hide();
}

function addFurtherIds(text1){
	$("input#buttonAddFutherIds").parent().parent().before("<tr><td colspan=\"2\"></td><td class='labelLeft'><label for=\"otherRepositorId_"+($("input[id^='otherRepositorId']").length)+"\"> "+text1+":</label></td><td><input type=\"text\" id=\"otherRepositorId_"+($("input[id^='otherRepositorId']").length)+"\" /></td></tr>");
}
function addRepositories(text1, text2, text3, text4, text5, text6, text7){
	var counter = $("table[id^='yourInstitutionTable_']").length;
	var clone = $("table[id^='yourInstitutionTable_"+counter+"']").clone();
	clone = "<table id='"+("yourInstitutionTable_"+(counter+1))+"'>"+clone.html()+"</table>";
	$("table[id^='yourInstitutionTable_"+counter+"']").after(clone);
	//reset parametters
	$("table#yourInstitutionTable_"+(counter+1)+" input[type='text']").each(function(){
		$(this).val(""); //clean all input_text
	});
	$("table[id^='yourInstitutionTable_']").hide();
	var localId = "";
	if(counter==1){
		localId = "yourInstitutionTable_"+counter;
		$("#eag2012tabs_institution_tabs").append("<li><a id=\"tab_"+localId+"\" href=\"#repositories\" >"+text1+"</a></li>");
	}
	localId = "yourInstitutionTable_"+(counter+1);
	$("#eag2012tabs_institution_tabs").append("<li><a id=\"tab_"+localId+"\" href=\"#repositories\" >"+text2+" "+(counter)+"</a></li>");
	//clone and put the 3 new tables
	$("div#tab-contact").append("<table id=\"contactTable_"+(counter+1)+"\">"+$("table#contactTable").clone().html()+"</table>");
	$("div#tab-accessAndServices").append("<table id=\"accessAndServicesTable_"+(counter+1)+"\">"+$("table#accessAndServicesTable").clone().html()+"</table>");
	$("div#tab-description").append("<table id=\"descriptionTable_"+(counter+1)+"\">"+$("table#descriptionTable").clone().html()+"</table>");
	//fill values with the current "your institution" values provided by user
	
	//contact tab
	var selectedIndex = document.getElementById('selectYIContinent').selectedIndex;
	var latitude = $("#textYILatitude").val();
	var longitude = $("#textYILongitude").val();
	var country = $("#textYICountry").val();
	var city = $("#textYICity").val();
	var street = $("#textYIStreet").val();
	var telephone = $("#textYITelephone").val();
	var email = $("#textYIEmailAddress").val();
	var web = $("#textYIEmailAddress").val();
	//contact table
	$("table#contactTable_"+(counter+1)+" #selectContinentOfTheInstitution option").eq(selectedIndex).prop("selected",true);
	$("table#contactTable_"+(counter+1)+" #textcontactLatitudeOfTheInstitution").attr("value",latitude);
	$("table#contactTable_"+(counter+1)+" #textContactLongitudeOfTheInstitution").attr("value",longitude);
	$("table#contactTable_"+(counter+1)+" #textContactCountryOfTheInstitution").attr("value",country);
	$("table#contactTable_"+(counter+1)+" #textContactCityOfTheInstitution").attr("value",city);
	$("table#contactTable_"+(counter+1)+" #textContactStreetOfTheInstitution").attr("value",street);
	$("table#contactTable_"+(counter+1)+" #textContactTelephoneOfTheInstitution").attr("value",telephone);
	$("table#contactTable_"+(counter+1)+" #textContactEmailOfTheInstitution").attr("value",email);
	$("table#contactTable_"+(counter+1)+" #textContactWebOfTheInstitution").attr("value",web);

	// add name of repository to contact tab.
	$("table#contactTable_"+(counter+1)+" tr#trVisitorsAddressLabel").before("<tr>"+
			"<td id=\"tdNameOfRepository\">"+
				"<label for=\"textNameOfRepository\">"+text3+"<span class=\"required\">*</span>:</label>"+
			"</td>"+
			"<td>"+
				"<input type=\"text\" id=\"textNameOfRepository\" />"+
			"<td>"+
				"<label for=\"selectRoleOfRepository\">"+text4+":</label>"+
			"</td>"+
			"<td>"+
				"<select id=\"selectRoleOfRepository\">"+
					"<option value=\"none\">---</option>"+
					"<option value=\"headquarters\">"+text5+"</option>"+
					"<option value=\"branch\">"+text6+"</option>"+
					"<option value=\"interim\">"+text7+"</option>"+
				"</select>"+
			"</td></tr>");

	//access and services
	var opening = $("#textYIOpeningTimes").val();
	var accessPublic = document.getElementById('selectAccessibleToThePublic').selectedIndex;
	var access = $("#selectFacilitiesForDisabledPeopleAvailable").val();
	$("table#contactTable_"+(counter+1)+" #textContactWebOfTheInstitution").attr("value",opening);
	$("table#contactTable_"+(counter+1)+" #selectASFacilitiesForDisabledPeopleAvailable option").eq(accessPublic).prop("selected",true);
	$("table#contactTable_"+(counter+1)+" #textASAccessibility").attr("value",access);
	
	$("table#"+localId).show();
	$("a[id^='tab_']").click(function(){
		$("a[id^='tab_']").each(function(){
			$(this).removeClass("eag2012currenttab");
		});
		$(this).addClass("eag2012currenttab");
		var localId = $(this).attr("id");
		if(localId.indexOf("tab_")==0){
			localId = localId.substring("tab_".length);
		}
		$("table[id^='yourInstitutionTable_']").hide();
		$("table#"+localId).show();
		//show/hide right tabs and content
		if(localId.indexOf("_1")>-1){
			$("ul#eag2012TabsContainer li a").each(function(){
				var id = $(this).parent().attr("id");
				if(id.indexOf("tab-yourInstitution")>-1){
					$(this).removeClass("eag2012disabled");
				}else if(id.indexOf("tab-identity")>-1){
					$(this).removeClass("eag2012disabled");
				}else if(id.indexOf("tab-control")>-1){
					$(this).removeClass("eag2012disabled");
				}else if(id.indexOf("tab-relations")>-1){
					$(this).removeClass("eag2012disabled");
				}
			});
		}else{
			$("ul#eag2012TabsContainer li a").each(function(){
				var id = $(this).parent().attr("id");
				if(id.indexOf("tab-yourInstitution")>-1 || id.indexOf("tab-identity")>-1 || id.indexOf("tab-control")>-1 || id.indexOf("tab-relations")>-1){
					$(this).addClass("eag2012disabled");
					$("ul#eag2012TabsContainer a[href='#tab-contact']").trigger('click');
				}
			});
//			if($("ul#eag2012TabsContainer a .eag2012currenttab").hasClass("eag2012disabled")){
//				$("ul#eag2012TabsContainer a[href='#tab-contact']").trigger('click');
//			}
		}
	});
	$("a#tab_"+localId).trigger('click');
	$("a#tab-contact").parent().trigger('click');
	
	//current tab
	$("table#yourInstitutionTable_"+(counter+1)+" input#buttonYourInstitutionTabCheck").click(clickYourInstitutionAction);
}
function getCurrentTab(){
	var currentTab = "";
	$("ul#eag2012tabs_institution_tabs li a").each(function(){
		if($(this).hasClass("eag2012currenttab")){
			currentTab = $(this).attr("id");
			currentTab = currentTab.substring(currentTab.lastIndexOf("_"));
		}
	});
	if(currentTab.length==0){
		currentTab = "_1";
	}
	return currentTab;
}
function addFurtherEmailsOfTheInstitution(){
	var currentTab = getCurrentTab();
	var count = $("table#contactTable"+currentTab+" tr[id^='trEmailOfTheInstitution']").length;
	var newId = "trEmailOfTheInstitution_"+(count+1);
	var trHtml = "<tr id=\""+newId+"\">"+$("table#contactTable tr#trEmailOfTheInstitution").clone().html()+"</tr>";
	var lastId = "table#contactTable"+currentTab+" tr#trEmailOfTheInstitution";
	if(count>1){
		lastId+="_"+(count);
	} else {
		$("table#contactTable"+currentTab+" tr#trEmailOfTheInstitution tr#trEmailOfTheInstitution").attr("id","trEmailOfTheInstitution_"+count);
		$("table#contactTable"+currentTab+" tr#trEmailOfTheInstitution label[for='textContactEmailOfTheInstitution']").attr("for","textContactEmailOfTheInstitution_"+count);
		$("table#contactTable"+currentTab+" tr#trEmailOfTheInstitution input#textContactEmailOfTheInstitution").attr("id","textContactEmailOfTheInstitution_"+count);
		$("table#contactTable"+currentTab+" tr#trEmailOfTheInstitution label[for='textContactLinkTitleForEmailOfTheInstitution']").attr("for","textContactLinkTitleForEmailOfTheInstitution_"+count);
		$("table#contactTable"+currentTab+" tr#trEmailOfTheInstitution input#textContactLinkTitleForEmailOfTheInstitution").attr("id","textContactLinkTitleForEmailOfTheInstitution_"+count);
	}

	$(lastId).after(trHtml);
	//update last content
	$("table#contactTable"+currentTab+" tr#"+newId+" tr#trEmailOfTheInstitution").attr("id","trEmailOfTheInstitution_"+(count+1));
	$("table#contactTable"+currentTab+" tr#"+newId+" label[for='textContactEmailOfTheInstitution']").attr("for","textContactEmailOfTheInstitution_"+(count+1));
	$("table#contactTable"+currentTab+" tr#"+newId+" input#textContactEmailOfTheInstitution").attr("id","textContactEmailOfTheInstitution_"+(count+1));
	$("table#contactTable"+currentTab+" tr#"+newId+" label[for='textContactLinkTitleForEmailOfTheInstitution']").attr("for","textContactLinkTitleForEmailOfTheInstitution_"+(count+1));
	$("table#contactTable"+currentTab+" tr#"+newId+" input#textContactLinkTitleForEmailOfTheInstitution").attr("id","textContactLinkTitleForEmailOfTheInstitution_"+(count+1));
	$("table#contactTable"+currentTab+" tr#"+newId+" input#textContactEmailOfTheInstitution_"+(count+1)).removeAttr("disabled");
}
function addAnotherFormOfTheAuthorizedName(){
	var counter = $("table[id^='identityTableNameOfTheInstitution_']").length;
	var clone = $("table[id^='identityTableNameOfTheInstitution_"+counter+"']").clone();
	clone = "<table id='"+("identityTableNameOfTheInstitution_"+(counter+1))+"'>"+clone.html()+"</table>";
	$("table[id^='identityTableNameOfTheInstitution_"+counter+"']").after(clone);
	// Reset parametters.
	$("table#identityTableNameOfTheInstitution_"+(counter+1)+" input[type='text']").each(function(){
		$(this).val(""); // Clean all input_text.
	});
	$("table#identityTableNameOfTheInstitution_"+(counter+1)+" tr#trNameOfTheInstitution input#textIdentityIdUsedInAPE").removeAttr("disabled");
	$("table#identityTableNameOfTheInstitution_"+(counter+1)+" tr#trNameOfTheInstitution select#noti_languageList").removeAttr("disabled");
}
function addParallelNameOfTheInstitution(){
	var counter = $("table[id^='identityTableParallelNameOfTheInstitution_']").length;
	var clone = $("table[id^='identityTableParallelNameOfTheInstitution_"+counter+"']").clone();
	clone = "<table id='"+("identityTableParallelNameOfTheInstitution_"+(counter+1))+"'>"+clone.html()+"</table>";
	$("table[id^='identityTableParallelNameOfTheInstitution_"+counter+"']").after(clone);
	// Reset parametters.
	$("table#identityTableParallelNameOfTheInstitution_"+(counter+1)+" input[type='text']").each(function(){
		$(this).val(""); // Clean all input_text.
	});
}
function addSingleYear(table){
	var id =  $(table).attr("id");
	var counter = $("table#"+id+" tr[id^='trYearWhenThisNameWasUsed_']").length;
	var clone = $("table#"+id+" tr[id^='trYearWhenThisNameWasUsed_"+counter+"']").clone();
	clone = "<tr id='"+("trYearWhenThisNameWasUsed_"+(counter+1))+"'>"+clone.html()+"</tr>";
	$("table#"+id+" tr[id^='trYearWhenThisNameWasUsed_"+counter+"']").after(clone);
	// Reset parametters and change IDs.
	$("table#"+id+" tr#trYearWhenThisNameWasUsed_"+(counter+1)+" input[type='text']").each(function(){
		var tdId = $(this).attr("id");
		tdId = tdId.substring(0, (tdId.lastIndexOf("_") + 1)) + (counter+1);
		$(this).attr("id", tdId);
		$(this).val(""); // Clean all input_text.
	});
	$("table#"+id+" tr#trYearWhenThisNameWasUsed_"+(counter+1)+" label").each(function(){
		var labelFor = $(this).attr("for");
		labelFor = labelFor.substring(0, (labelFor.lastIndexOf("_") + 1)) + (counter+1);
		$(this).attr("for", labelFor);
	});
}

function addRangeYear(table, text1, text2){
	var id =  $(table).attr("id");
	var counter = $("table#"+id+" tr[id^='trYearRangeWhenThisNameWasUsed_']").length;

	if (counter == 0) {
		var count = $("table#"+id+" tr[id^='trYearWhenThisNameWasUsed_']").length;
		var yearRange = "<tr id=\"trYearRangeWhenThisNameWasUsed_1\">"+
					"<td>"+
						"<label for=\"textIdentityYearFrom_1\">"+text1+":</label>"+
					"</td>"+
					"<td>"+
						"<input type=\"text\" id=\"textYearWhenThisNameWasUsedFrom_1\" value=\"\" />"+
					"</td>"+
					"<td>"+
						"<label for=\"textYearWhenThisNameWasUsedTo_1\" class=\"labelLeft\">"+text2+":</label>"+
					"</td>"+
					"<td>"+
						"<input type=\"text\" id=\"textYearWhenThisNameWasUsedTo_1\" value=\"\" />"+
					"</td>"+
				"</tr>";
		$("table#"+id+" tr[id^='trYearWhenThisNameWasUsed_"+count+"']").after(yearRange);
	} else {
		var clone = $("table#"+id+" tr[id^='trYearRangeWhenThisNameWasUsed_"+counter+"']").clone();
		clone = "<tr id='"+("trYearRangeWhenThisNameWasUsed_"+(counter+1))+"'>"+clone.html()+"</tr>";
		$("table#"+id+" tr[id^='trYearRangeWhenThisNameWasUsed_"+counter+"']").after(clone);
		// Reset parametters and change IDs.
		$("table#"+id+" tr#trYearRangeWhenThisNameWasUsed_"+(counter+1)+" input[type='text']").each(function(){
			var tdId = $(this).attr("id");
			tdId = tdId.substring(0, (tdId.lastIndexOf("_") + 1)) + (counter+1);
			$(this).attr("id", tdId);
			$(this).val(""); // Clean all input_text.
		});
		$("table#"+id+" tr#trYearRangeWhenThisNameWasUsed_"+(counter+1)+" label").each(function(){
			var labelFor = $(this).attr("for");
			labelFor = labelFor.substring(0, (labelFor.lastIndexOf("_") + 1)) + (counter+1);
			$(this).attr("for", labelFor);
		});
	}
}

function contactAddVisitorsAddressTranslation() {
	var currentTab = getCurrentTab();
	var counter = $("table#contactTable"+currentTab+" table[id^='contactTableVisitorsAddress_']").length;
	var clone = $("table#contactTable"+currentTab+" table[id^='contactTableVisitorsAddress_"+counter+"']").clone();
	clone = "<table id='"+("contactTableVisitorsAddress_"+(counter+1))+"'>"+clone.html()+"</table>";
	$("table#contactTable"+currentTab+" table[id^='contactTableVisitorsAddress_"+counter+"']").after(clone);
	// Reset parametters and enable fields.
	$("table#contactTable"+currentTab+" table#contactTableVisitorsAddress_"+(counter+1)+" input[type='text']").each(function(){
		$(this).val(""); // Clean all input_text.
		$(this).removeAttr("disabled");
	});
	$("table#contactTable"+currentTab+" table#contactTableVisitorsAddress_"+(counter+1)+" select").removeAttr("disabled");
}

function contactAddPostalAddressIfDifferent(property1, property2, property3, property4) {
	var currentTab = getCurrentTab();
	var select = '<select id="selectContactLanguagePostalAddress">'+$("#selectLanguageVisitorAddress").html()+'</select>';
	var counter = $("table#contactTable"+currentTab+" table[id^='contactTableVisitorsAddress_']").length;

	$("table#contactTable"+currentTab+" input#buttonContactAddPostalAddressIfDifferent").hide();

	$("table#contactTable"+currentTab+" table#contactTableVisitorsAddress_"+counter).after('<tr><td><table id="contactTablePostalAddress_1">'+
		'<tr id="trContactPostalAddressLabel">'+
			'<td id="postalAddressLabel" colspan="4">'+property1+
			'</td>'+
		'</tr>'+
		'<tr id="contactPostalAddressStreet">'+
			'<td>'+
				'<label for="textContactPAStreet">'+property3+':</label>'+
			'</td>'+
			'<td>'+
				'<input type="text" id="textContactPAStreet" />'+
			'</td>'+
			'<td id="contactPostalAddressLanguage">'+
				'<label for="selectContactPASelectLanguage">'+property2+':</label>'+
			'</td>'+
			'<td>'+select+
			'</td>'+
		'</tr>'+
		'<tr id="contactPostalAddressCity">'+
			'<td>'+
				'<label for="textContactPACity">'+property4+':</label>'+
			'</td>'+
			'<td>'+
				'<input type="text" id="textContactPACity" />'+
			'</td>'+
		'</tr></table></td></tr>');

	$("table#contactTable"+currentTab+" tr#trButtonContacPostalAddressTranslation").show();
}

function contactAddPostalAddressTranslation() {
	var currentTab = getCurrentTab();
	var counter = $("table#contactTable"+currentTab+" table[id^='contactTablePostalAddress_']").length;
	var clone = $("table#contactTable"+currentTab+" table[id^='contactTablePostalAddress_"+counter+"']").clone();
	clone = "<table id='"+("contactTablePostalAddress_"+(counter+1))+"'>"+clone.html()+"</table>";
	$("table#contactTable"+currentTab+" table[id^='contactTablePostalAddress_"+counter+"']").after(clone);
	// Reset parametters and enable fields.
	$("table#contactTable"+currentTab+" table#contactTablePostalAddress_"+(counter+1)+" input[type='text']").each(function(){
		$(this).val(""); // Clean all input_text.
	});
}

function addFurtherTelephoneOfTheInstitution(){
	var currentTab = getCurrentTab();
	var count = $("table#contactTable"+currentTab+" tr[id^='trTelephoneOfTheInstitution']").length;
	var newId = "trTelephoneOfTheInstitution_"+(count+1);
	var trHtml = "<tr id=\""+newId+"\">"+$("table#contactTable tr[id^='trTelephoneOfTheInstitution']").clone().html()+"</tr>";
	var lastId = "table#contactTable"+currentTab+" tr#trTelephoneOfTheInstitution";
	if(count>1){
		lastId+="_"+(count);
	} else {
		$("table#contactTable"+currentTab+" tr#trTelephoneOfTheInstitution td#tdTelephoneOfTheInstitution").attr("id","tdTelephoneOfTheInstitution_"+count);
		$("table#contactTable"+currentTab+" tr#trTelephoneOfTheInstitution label[for='textContactTelephoneOfTheInstitution']").attr("for","textContactTelephoneOfTheInstitution_"+count);
		$("table#contactTable"+currentTab+" tr#trTelephoneOfTheInstitution input#textContactTelephoneOfTheInstitution").attr("id","textContactTelephoneOfTheInstitution_"+count);
	}
	$(lastId).after(trHtml);
	//delete cloned button
	$("table#contactTable"+currentTab+" tr#"+newId+" td#tdAddFurtherTelephoneOfTheInstitution").remove();
	//update last content
	$("table#contactTable"+currentTab+" tr#"+newId+" td#tdTelephoneOfTheInstitution").attr("id","tdTelephoneOfTheInstitution_"+(count+1));
	$("table#contactTable"+currentTab+" tr#"+newId+" label[for='textContactTelephoneOfTheInstitution']").attr("for","textContactTelephoneOfTheInstitution_"+(count+1));
	$("table#contactTable"+currentTab+" tr#"+newId+" input#textContactTelephoneOfTheInstitution").attr("id","textContactTelephoneOfTheInstitution_"+(count+1));
	$("table#contactTable"+currentTab+" tr#"+newId+" input#textContactTelephoneOfTheInstitution_"+(count+1)).removeAttr("disabled");
}

function addFurtherFaxOfTheInstitution(){
	var currentTab = getCurrentTab();
	var count = $("table#contactTable"+currentTab+" tr[id^='trFaxOfTheInstitution']").length;
	var newId = "trFaxOfTheInstitution_"+(count+1);
	var trHtml = "<tr id=\""+newId+"\">"+$("table#contactTable tr[id^='trFaxOfTheInstitution']").clone().html()+"</tr>";
	var lastId = "table#contactTable"+currentTab+" tr#trFaxOfTheInstitution";
	if(count>1){
		lastId+="_"+(count);
	} else {
		$("table#contactTable"+currentTab+" tr#trFaxOfTheInstitution td#tdFaxOfTheInstitution").attr("id","tdFaxOfTheInstitution_"+count);
		$("table#contactTable"+currentTab+" tr#trFaxOfTheInstitution label[for='textContactFaxOfTheInstitution']").attr("for","textContactFaxOfTheInstitution_"+count);
		$("table#contactTable"+currentTab+" tr#trFaxOfTheInstitution input#textContactFaxOfTheInstitution").attr("id","textContactFaxOfTheInstitution_"+count);
	}
	$(lastId).after(trHtml);
	//delete cloned button
	$("table#contactTable"+currentTab+" tr#"+newId+" td#tdAddFurtherFaxOfTheInstitution").remove();
	//update last content
	$("table#contactTable"+currentTab+" tr#"+newId+" td#tdFaxOfTheInstitution").attr("id","tdFaxOfTheInstitution_"+(count+1));
	$("table#contactTable"+currentTab+" tr#"+newId+" label[for='textContactFaxOfTheInstitution']").attr("for","textContactFaxOfTheInstitution_"+(count+1));
	$("table#contactTable"+currentTab+" tr#"+newId+" input#textContactFaxOfTheInstitution").attr("id","textContactFaxOfTheInstitution_"+(count+1));
}
function addFurtherWebsOfTheInstitution(){
	var currentTab = getCurrentTab();
	var count = $("table#contactTable"+currentTab+" tr[id^='trWebOfTheInstitution']").length;
	var newId = "trWebOfTheInstitution_"+(count+1);
	var trHtml = "<tr id=\""+newId+"\">"+$("table#contactTable tr[id^='trWebOfTheInstitution']").clone().html()+"</tr>";
	var lastId = "table#contactTable"+currentTab+" tr#trWebOfTheInstitution";
	if(count>1){
		lastId+="_"+(count);
	} else {
		$("table#contactTable"+currentTab+" tr#trWebOfTheInstitution tr#trWebOfTheInstitution").attr("id","trWebOfTheInstitution_"+count);
		$("table#contactTable"+currentTab+" tr#trWebOfTheInstitution label[for='textContactWebOfTheInstitution']").attr("for","textContactWebOfTheInstitution_"+count);
		$("table#contactTable"+currentTab+" tr#trWebOfTheInstitution input#textContactWebOfTheInstitution").attr("id","textContactWebOfTheInstitution_"+count);
		$("table#contactTable"+currentTab+" tr#trWebOfTheInstitution label[for='textContactLinkTitleForWebOfTheInstitution']").attr("for","textContactLinkTitleForWebOfTheInstitution_"+count);
		$("table#contactTable"+currentTab+" tr#trWebOfTheInstitution input#textContactLinkTitleForWebOfTheInstitution").attr("id","textContactLinkTitleForWebOfTheInstitution_"+count);
	}
	$(lastId).after(trHtml);
	//update last content
	$("table#contactTable"+currentTab+" tr#"+newId+" tr#trWebOfTheInstitution").attr("id","trWebOfTheInstitution_"+(count+1));
	$("table#contactTable"+currentTab+" tr#"+newId+" label[for='textContactWebOfTheInstitution']").attr("for","textContactWebOfTheInstitution_"+(count+1));
	$("table#contactTable"+currentTab+" tr#"+newId+" input#textContactWebOfTheInstitution").attr("id","textContactWebOfTheInstitution_"+(count+1));
	$("table#contactTable"+currentTab+" tr#"+newId+" label[for='textContactLinkTitleForWebOfTheInstitution']").attr("for","textContactLinkTitleForWebOfTheInstitution_"+(count+1));
	$("table#contactTable"+currentTab+" tr#"+newId+" input#textContactLinkTitleForWebOfTheInstitution").attr("id","textContactLinkTitleForWebOfTheInstitution_"+(count+1));
	$("table#contactTable"+currentTab+" tr#"+newId+" input#textContactWebOfTheInstitution_"+(count+1)).removeAttr("disabled");
}

function aSAddOpeningTimes(){
	var currentTab = getCurrentTab();
	// trASOpeningTimes
	var count = $("table#accessAndServicesTable" + currentTab + " tr[id^='trASOpeningTimes']").length;
	var newId = "trASOpeningTimes_"+(count+1);
	var trHtml = "<tr id=\""+newId+"\">"+$("table#accessAndServicesTable"+currentTab+" tr[id='trASOpeningTimes']").clone().html()+"</tr>";
	var lastId= "table#accessAndServicesTable"+currentTab+" tr#trASOpeningTimes";

	if(count>1){
		lastId += "_" + (count);
	}
	$(lastId).after(trHtml);
	// update last content
	$("table#accessAndServicesTable"+currentTab+" tr#"+newId+" label[for='textOpeningTimes']").attr("for","textOpeningTimes_"+(count+1));
	$("table#accessAndServicesTable"+currentTab+" tr#"+newId+" input#textOpeningTimes").attr("id","textOpeningTimes_"+(count+1));
	$("table#accessAndServicesTable"+currentTab+" tr#"+newId+" label[for='selectLanguageOpeningTimes']").attr("for","selectLanguageOpeningTimes_"+(count+1));
	$("table#accessAndServicesTable"+currentTab+" tr#"+newId+" select#selectLanguageOpeningTimes").attr("id","selectLanguageOpeningTimes_"+(count+1));
}

function aSAddClosingDates(){
	var currentTab = getCurrentTab();
	// trASClosingDates
	var count = $("table#accessAndServicesTable" + currentTab + " tr[id^='trASClosingDates']").length;
	var newId = "trASClosingDates_"+(count+1);
	var trHtml = "<tr id=\""+newId+"\">"+$("table#accessAndServicesTable"+currentTab+" tr[id='trASClosingDates']").clone().html()+"</tr>";
	var lastId= "table#accessAndServicesTable"+currentTab+" tr#trASClosingDates";

	if(count>1){
		lastId += "_" + (count);
	}
	$(lastId).after(trHtml);
	// update last content
	$("table#accessAndServicesTable"+currentTab+" tr#"+newId+" label[for='textClosingDates']").attr("for","textClosingDates_"+(count+1));
	$("table#accessAndServicesTable"+currentTab+" tr#"+newId+" input#textClosingDates").attr("id","textClosingDates_"+(count+1));
	$("table#accessAndServicesTable"+currentTab+" tr#"+newId+" label[for='selectLanguageClosingDates']").attr("for","selectLanguageClosingDates_"+(count+1));
	$("table#accessAndServicesTable"+currentTab+" tr#"+newId+" select#selectLanguageClosingDates").attr("id","selectLanguageClosingDates_"+(count+1));
}

function aSAddTravellingDirections(){
	var currentTab = getCurrentTab();
	var count = $("table#accessAndServicesTable"+currentTab+" tr[id^='trTravellingDirections']").length;
	var target1 = "trTravellingDirections_"+(count+1);
	var target2 = "tr2TravellingDirections_"+(count+1);
	var lastId = "table#accessAndServicesTable"+currentTab+" tr#tr2TravellingDirections";
	if(count>1){
		lastId+="_"+(count);
	}
	var tr2HTML = "<tr id=\""+target1+"\">";
	tr2HTML += $("table#accessAndServicesTable"+currentTab+" tr#trTravellingDirections").clone().html();
	tr2HTML += "</tr>\n";
	if(count==1){
		tr2HTML += "<tr id=\""+target2+"\">"+$("table#accessAndServicesTable"+currentTab+" tr#tr2TravellingDirections").clone().html();
	}else{
		tr2HTML += "<tr id=\""+target2+"\">"+$("table#accessAndServicesTable"+currentTab+" tr#tr2TravellingDirections_"+(count)).clone().html();
	}
	tr2HTML += "</tr>";
	$(lastId).after(tr2HTML);
	//update elements
	$("table#accessAndServicesTable"+currentTab+" tr#"+target1+" label[for='textTravellingDirections']").attr("for","textTravellingDirections_"+(count+1));
	$("table#accessAndServicesTable"+currentTab+" tr#"+target1+" textarea#textTravellingDirections").attr("id","textTravellingDirections_"+(count+1));
	$("table#accessAndServicesTable"+currentTab+" tr#"+target1+" label[for='textTravelLink']").attr("for","textTravelLink_"+(count+1));
	$("table#accessAndServicesTable"+currentTab+" tr#"+target1+" input#textTravelLink").attr("id","textTravelLink_"+(count+1));
	$("table#accessAndServicesTable"+currentTab+" tr#"+target2+" label[for='selectASATDSelectLanguage']").attr("for","selectASATDSelectLanguage_"+(count+1));
	$("table#accessAndServicesTable"+currentTab+" tr#"+target2+" select#selectASATDSelectLanguage").attr("id","selectASATDSelectLanguage_"+(count+1));
}

function addFutherAccessInformation(){
	var currentTab = getCurrentTab();
	var count = $("table#accessAndServicesTable"+currentTab+" tr[id^='trASAccessRestrictions']").length;
	var newId = "trASAccessRestrictions_"+(count+1);
	var trHtml = "<tr id=\""+newId+"\">"+$("table#accessAndServicesTable"+currentTab+" tr[id='trASAccessRestrictions']").clone().html()+"</tr>";
	var lastId = "table#accessAndServicesTable"+currentTab+" tr#trASAccessRestrictions";
	if(count>1){
		lastId+="_"+(count);
	}
	$(lastId).after(trHtml);
	//update last content
	$("table#accessAndServicesTable"+currentTab+" tr#"+newId+" label[for='textASAccessRestrictions']").attr("for","textASAccessRestrictions_"+(count+1));
	$("table#accessAndServicesTable"+currentTab+" tr#"+newId+" input#textASAccessRestrictions").attr("id","textASAccessRestrictions_"+(count+1));
	$("table#accessAndServicesTable"+currentTab+" tr#"+newId+" label[for='selectASARSelectLanguage']").attr("for","selectASARSelectLanguage_"+(count+1));
	$("table#accessAndServicesTable"+currentTab+" tr#"+newId+" select#selectASARSelectLanguage").attr("id","selectASARSelectLanguage_"+(count+1));
}

function aSAddFutherTermOfUse(){
	var currentTab = getCurrentTab();
	var count = $("table#accessAndServicesTable"+currentTab+" tr[id^='trASAddFutherTermOfUse']").length;
	var target1 = "trASAddFutherTermOfUse_"+(count+1);
	var target2 = "tr2ASAddFutherTermOfUse_"+(count+1);
	var lastId = "table#accessAndServicesTable"+currentTab+" tr#tr2ASAddFutherTermOfUse";
	if(count>1){
		lastId+="_"+(count);
	}
	var tr2HTML = "<tr id=\""+target1+"\">";
	tr2HTML += $("table#accessAndServicesTable"+currentTab+" tr#trASAddFutherTermOfUse").clone().html();
	tr2HTML += "</tr>\n";
	if(count==1){
		tr2HTML += "<tr id=\""+target2+"\">"+$("table#accessAndServicesTable"+currentTab+" tr#tr2ASAddFutherTermOfUse").clone().html();
	}else{
		tr2HTML += "<tr id=\""+target2+"\">"+$("table#accessAndServicesTable"+currentTab+" tr#tr2ASAddFutherTermOfUse_"+(count)).clone().html();
	}
	tr2HTML += "</tr>";
	$(lastId).after(tr2HTML);
	//update elements
	$("table#accessAndServicesTable"+currentTab+" tr#"+target1+" label[for='textASTermOfUse']").attr("for","textASTermOfUse_"+(count+1));
	$("table#accessAndServicesTable"+currentTab+" tr#"+target1+" textarea#textASTermOfUse").attr("id","textASTermOfUse_"+(count+1));
	$("table#accessAndServicesTable"+currentTab+" tr#"+target1+" label[for='textTravelLink']").attr("for","textTravelLink_"+(count+1));
	$("table#accessAndServicesTable"+currentTab+" tr#"+target1+" input#textASTOULink").attr("id","textASTOULink_"+(count+1));
	$("table#accessAndServicesTable"+currentTab+" tr#"+target2+" label[for='textASTOULink']").attr("for","textASTOULink_"+(count+1));
	$("table#accessAndServicesTable"+currentTab+" tr#"+target2+" select#selectASAFTOUSelectLanguage").attr("id","selectASAFTOUSelectLanguage_"+(count+1));
}

function addAccessibilityInformation(){
	var currentTab = getCurrentTab();
	var count = $("table#accessAndServicesTable"+currentTab+" tr[id^='trAccessibilityInformation']").length;
	var newId = "trAccessibilityInformation_"+(count+1);
	var trHtml = "<tr id=\""+newId+"\">"+$("table#accessAndServicesTable tr[id='trAccessibilityInformation']").clone().html()+"</tr>";
	var lastId = "table#accessAndServicesTable"+currentTab+" tr#trAccessibilityInformation";
	if(count>1){
		lastId+="_"+(count);
	}
	$(lastId).after(trHtml);
	//update last content
	$("table#accessAndServicesTable"+currentTab+" tr#"+newId+" label[for='textASAccessibility']").attr("for","textASAccessibility_"+(count+1));
	$("table#accessAndServicesTable"+currentTab+" tr#"+newId+" input#textASAccessibility").attr("id","textASAccessibility_"+(count+1));
	$("table#accessAndServicesTable"+currentTab+" tr#"+newId+" label[for='selectASASelectLanguage']").attr("for","selectASASelectLanguage_"+(count+1));
	$("table#accessAndServicesTable"+currentTab+" tr#"+newId+" select#selectASASelectLanguage").attr("id","selectASASelectLanguage_"+(count+1));
}

function aSSRAddDescriptionOfYourComputerPlaces(property1, property2){
	// add description of computer places.
	var currentTab = getCurrentTab();
	var count = $("table#accessAndServicesTable"+currentTab+" tr[id^='trASSRDescriptionOfYourComputerPlaces']").length;
	var select = '<select id="selectDescriptionOfYourComputerPlaces">'+$("#selectLanguageOpeningTimes").html()+'</select>';

	if (count == 0) {
	$("table#accessAndServicesTable_"+(count+1)+" tr#trASSRAddDescriptionOfYourComputerPlaces").before("<tr id=\"trASSRDescriptionOfYourComputerPlaces\">"+
			"<td id=\"tdDescriptionOfYourComputerPlaces\">"+
				"<label for=\"textDescriptionOfYourComputerPlaces\">"+property1+":</label>"+
			"</td>"+
			"<td>"+
				"<input type=\"text\" id=\"textDescriptionOfYourComputerPlaces\" />"+
			"<td id=\"tdSelectDescriptionOfYourComputerPlaces\" class=\"labelLeft\">"+
				"<label for=\"selectDescriptionOfYourComputerPlaces\">"+property2+":</label>"+
			"</td>"+
			"<td>"+select+
			"</td></tr>");
	} else {
		var newId = "trASSRDescriptionOfYourComputerPlaces_"+(count+1);
		var trHtml = "<tr id=\""+newId+"\">"+$("table#accessAndServicesTable"+currentTab+" tr[id='trASSRDescriptionOfYourComputerPlaces']").clone().html()+"</tr>";
		var lastId = "table#accessAndServicesTable"+currentTab+" tr#trASSRDescriptionOfYourComputerPlaces";
		if(count>1){
			lastId+="_"+(count);
		}
		$(lastId).after(trHtml);
		//update last content
		$("table#accessAndServicesTable"+currentTab+" tr#"+newId+" label[for='textDescriptionOfYourComputerPlaces']").attr("for","textDescriptionOfYourComputerPlaces_"+(count+1));
		$("table#accessAndServicesTable"+currentTab+" tr#"+newId+" input#textDescriptionOfYourComputerPlaces").attr("id","textDescriptionOfYourComputerPlaces_"+(count+1));
		$("table#accessAndServicesTable"+currentTab+" tr#"+newId+" label[for='selectDescriptionOfYourComputerPlaces']").attr("for","selectDescriptionOfYourComputerPlaces_"+(count+1));
		$("table#accessAndServicesTable"+currentTab+" tr#"+newId+" select#selectDescriptionOfYourComputerPlaces").attr("id","selectDescriptionOfYourComputerPlaces_"+(count+1));
	}
}

function aSSRAddReadersTicket(){
	var currentTab = getCurrentTab();
	var count = $("table#accessAndServicesTable"+currentTab+" tr[id^='trASSRReadersTicket']").length;
	var target1 = "trASSRReadersTicket_"+(count+1);
	var target2 = "tr2ASSRReadersTicket_"+(count+1);
	var lastId = "table#accessAndServicesTable"+currentTab+" tr#tr2ASSRReadersTicket";
	if(count>1){
		lastId+="_"+(count);
	}
	var tr2HTML = "<tr id=\""+target1+"\">";
	tr2HTML += $("table#accessAndServicesTable"+currentTab+" tr#trASSRReadersTicket").clone().html();
	tr2HTML += "</tr>\n";
	if(count==1){
		tr2HTML += "<tr id=\""+target2+"\">"+$("table#accessAndServicesTable"+currentTab+" tr#tr2ASSRReadersTicket").clone().html();
	} else{
		tr2HTML += "<tr id=\""+target2+"\">"+$("table#accessAndServicesTable"+currentTab+" tr#tr2ASSRReadersTicket_"+(count)).clone().html();
	}
	tr2HTML += "</tr>";
	$(lastId).after(tr2HTML);

	//update elements
	$("table#accessAndServicesTable"+currentTab+" tr#"+target1+" label[for='textASSRReadersTicket']").attr("for","textASSRReadersTicket_"+(count+1));
	$("table#accessAndServicesTable"+currentTab+" tr#"+target1+" input#textASSRReadersTicket").attr("id","textASSRReadersTicket_"+(count+1));
	$("table#accessAndServicesTable"+currentTab+" tr#"+target1+" label[for='selectReadersTickectLanguage']").attr("for","selectReadersTickectLanguage_"+(count+1));
	$("table#accessAndServicesTable"+currentTab+" tr#"+target1+" select#selectReadersTickectLanguage").attr("id","selectReadersTickectLanguage_"+(count+1));
	$("table#accessAndServicesTable"+currentTab+" tr#"+target2+" label[for='textASSRRTLink']").attr("for","textASSRRTLink_"+(count+1));
	$("table#accessAndServicesTable"+currentTab+" tr#"+target2+" input#textASSRRTLink").attr("id","textASSRRTLink_"+(count+1));
}

function aSSRAddFurtherOrderInformation(){
	var currentTab = getCurrentTab();
	var count = $("table#accessAndServicesTable"+currentTab+" tr[id^='trASSRAddFurtherOrderInformation']").length;
	var target1 = "trASSRAddFurtherOrderInformation_"+(count+1);
	var target2 = "tr2ASSRAddFurtherOrderInformation_"+(count+1);
	var lastId = "table#accessAndServicesTable"+currentTab+" tr#tr2ASSRAddFurtherOrderInformation";
	if(count>1){
		lastId+="_"+(count);
	}
	var tr2HTML = "<tr id=\""+target1+"\">";
	tr2HTML += $("table#accessAndServicesTable"+currentTab+" tr#trASSRAddFurtherOrderInformation").clone().html();
	tr2HTML += "</tr>\n";
	if(count==1){
		tr2HTML += "<tr id=\""+target2+"\">"+$("table#accessAndServicesTable"+currentTab+" tr#tr2ASSRAddFurtherOrderInformation").clone().html();
	} else{
		tr2HTML += "<tr id=\""+target2+"\">"+$("table#accessAndServicesTable"+currentTab+" tr#tr2ASSRAddFurtherOrderInformation_"+(count)).clone().html();
	}
	tr2HTML += "</tr>";
	$(lastId).after(tr2HTML);

	//update elements
	$("table#accessAndServicesTable"+currentTab+" tr#"+target1+" label[for='textASSRAdvancedOrders']").attr("for","textASSRAdvancedOrders_"+(count+1));
	$("table#accessAndServicesTable"+currentTab+" tr#"+target1+" input#textASSRAdvancedOrders").attr("id","textASSRAdvancedOrders_"+(count+1));
	$("table#accessAndServicesTable"+currentTab+" tr#"+target1+" label[for='selectASSRAFOIUSelectLanguage']").attr("for","selectASSRAFOIUSelectLanguage_"+(count+1));
	$("table#accessAndServicesTable"+currentTab+" tr#"+target1+" select#selectASSRAFOIUSelectLanguage").attr("id","selectASSRAFOIUSelectLanguage_"+(count+1));
	$("table#accessAndServicesTable"+currentTab+" tr#"+target2+" label[for='textASSRAOLink']").attr("for","textASSRAOLink_"+(count+1));
	$("table#accessAndServicesTable"+currentTab+" tr#"+target2+" input#textASSRAOLink").attr("id","textASSRAOLink_"+(count+1));
}

function aSAddResearchServices(){
	var currentTab = getCurrentTab();
	var count = $("table#accessAndServicesTable"+currentTab+" tr[id^='trASSRResearchServices']").length;
	var newId = "trASSRResearchServices_"+(count+1);
	var trHtml = "<tr id=\""+newId+"\">"+$("table#accessAndServicesTable tr[id='trASSRResearchServices']").clone().html()+"</tr>";
	var lastId = "table#accessAndServicesTable"+currentTab+" tr#trASSRResearchServices";
	if(count>1){
		lastId+="_"+(count);
	}
	$(lastId).after(trHtml);
	//update last content
	$("table#accessAndServicesTable"+currentTab+" tr#"+newId+" label[for='textASSRResearchServices']").attr("for","textASSRResearchServices_"+(count+1));
	$("table#accessAndServicesTable"+currentTab+" tr#"+newId+" input#textASSRResearchServices").attr("id","textASSRResearchServices_"+(count+1));
	$("table#accessAndServicesTable"+currentTab+" tr#"+newId+" label[for='textASSRRSSelectLanguage']").attr("for","textASSRRSSelectLanguage_"+(count+1));
	$("table#accessAndServicesTable"+currentTab+" tr#"+newId+" select#textASSRRSSelectLanguage").attr("id","textASSRRSSelectLanguage_"+(count+1));
}

function aSPIAAddInternetAccessInformation(){
	var currentTab = getCurrentTab();
	var count = $("table#accessAndServicesTable"+currentTab+" tr[id^='trASPIAAddInternetAccessInformation']").length;
	var newId = "trASPIAAddInternetAccessInformation_"+(count+1);
	var trHtml = "<tr id=\""+newId+"\">"+$("table#accessAndServicesTable tr[id='trASPIAAddInternetAccessInformation']").clone().html()+"</tr>";
	var lastId = "table#accessAndServicesTable"+currentTab+" tr#trASPIAAddInternetAccessInformation";
	if(count>1){
		lastId+="_"+(count);
	}
	$(lastId).after(trHtml);
	//update last content
	$("table#accessAndServicesTable"+currentTab+" tr#"+newId+" label[for='textASDescription']").attr("for","textASDescription_"+(count+1));
	$("table#accessAndServicesTable"+currentTab+" tr#"+newId+" input#textASDescription").attr("id","textASDescription_"+(count+1));
	$("table#accessAndServicesTable"+currentTab+" tr#"+newId+" label[for='selectASDSelectLanguage']").attr("for","selectASDSelectLanguage_"+(count+1));
	$("table#accessAndServicesTable"+currentTab+" tr#"+newId+" select#selectASDSelectLanguage").attr("id","selectASDSelectLanguage_"+(count+1));
}

function addADescriptionOfYourRestaurationLab(){
	var currentTab = getCurrentTab();
	var count = $("table#accessAndServicesTable"+currentTab+" tr[id^='trASTSDescriptionOfRestaurationLab']").length;
	var newId = "trASTSDescriptionOfRestaurationLab_"+(count+1);
	var trHtml = "<tr id=\""+newId+"\">"+$("table#accessAndServicesTable tr[id='trASTSDescriptionOfRestaurationLab']").clone().html()+"</tr>";
	var lastId = "table#accessAndServicesTable"+currentTab+" tr#trASTSDescriptionOfRestaurationLab";
	if(count>1){
		lastId+="_"+(count);
	}
	$(lastId).after(trHtml);
	//update last content
	$("table#accessAndServicesTable"+currentTab+" tr#"+newId+" label[for='textASTSDescriptionOfRestaurationLab']").attr("for","textASTSDescriptionOfRestaurationLab_"+(count+1));
	$("table#accessAndServicesTable"+currentTab+" tr#"+newId+" input#textASTSDescriptionOfRestaurationLab").attr("id","textASTSDescriptionOfRestaurationLab_"+(count+1));
	$("table#accessAndServicesTable"+currentTab+" tr#"+newId+" label[for='selectASTSSelectLanguage']").attr("for","selectASTSSelectLanguage_"+(count+1));
	$("table#accessAndServicesTable"+currentTab+" tr#"+newId+" select#selectASTSSelectLanguage").attr("id","selectASTSSelectLanguage_"+(count+1));
}

function aSAddADescriptionOfYourReproductionService(){
	var currentTab = getCurrentTab();
	var count = $("table#accessAndServicesTable"+currentTab+" tr[id^='trASTSDescriptionOfReproductionService']").length;
	var newId = "trASTSDescriptionOfReproductionService_"+(count+1);
	var trHtml = "<tr id=\""+newId+"\">"+$("table#accessAndServicesTable tr[id='trASTSDescriptionOfReproductionService']").clone().html()+"</tr>";
	var lastId = "table#accessAndServicesTable"+currentTab+" tr#trASTSDescriptionOfReproductionService";
	if(count>1){
		lastId+="_"+(count);
	}
	$(lastId).after(trHtml);
	//update last content
	$("table#accessAndServicesTable"+currentTab+" tr#"+newId+" label[for='textASTSDescriptionOfReproductionService']").attr("for","textASTSDescriptionOfReproductionService_"+(count+1));
	$("table#accessAndServicesTable"+currentTab+" tr#"+newId+" input#textASTSDescriptionOfReproductionService").attr("id","textASTSDescriptionOfReproductionService_"+(count+1));
	$("table#accessAndServicesTable"+currentTab+" tr#"+newId+" label[for='selectASTSRSSelectLanguage']").attr("for","selectASTSRSSelectLanguage_"+(count+1));
	$("table#accessAndServicesTable"+currentTab+" tr#"+newId+" select#selectASTSRSSelectLanguage").attr("id","selectASTSRSSelectLanguage_"+(count+1));
}

function aSReSeAddExhibition(){
	var currentTab = getCurrentTab();
	var count = $("table#accessAndServicesTable"+currentTab+" tr[id^='trASReSeExhibition']").length;
	var target1 = "trASReSeExhibition_"+(count+1);
	var target2 = "tr2ASReSeExhibition_"+(count+1);
	var lastId = "table#accessAndServicesTable"+currentTab+" tr#tr2ASReSeExhibition";
	if(count>1){
		lastId+="_"+(count);
	}
	var tr2HTML = "<tr id=\""+target1+"\">";
	tr2HTML += $("table#accessAndServicesTable"+currentTab+" tr#trASReSeExhibition").clone().html();
	tr2HTML += "</tr>\n";
	if(count==1){
		tr2HTML += "<tr id=\""+target2+"\">"+$("table#accessAndServicesTable"+currentTab+" tr#tr2ASReSeExhibition").clone().html();
	}else{
		tr2HTML += "<tr id=\""+target2+"\">"+$("table#accessAndServicesTable"+currentTab+" tr#tr2ASReSeExhibition_"+(count)).clone().html();
	}
	tr2HTML += "</tr>";
	$(lastId).after(tr2HTML);
	//update new elements
	$("table#accessAndServicesTable"+currentTab+" tr#"+target1+" label[for='textASReSeExhibition']").attr("for","textASReSeExhibition_"+(count+1));
	$("table#accessAndServicesTable"+currentTab+" tr#"+target1+" input#textASReSeExhibition").attr("id","textASReSeExhibition_"+(count+1));
	$("table#accessAndServicesTable"+currentTab+" tr#"+target1+" label[for='selectASReSeExhibitionSelectLanguage']").attr("for","selectASReSeExhibitionSelectLanguage_"+(count+1));
	$("table#accessAndServicesTable"+currentTab+" tr#"+target1+" select#selectASReSeExhibitionSelectLanguage").attr("id","selectASReSeExhibitionSelectLanguage_"+(count+1));
	$("table#accessAndServicesTable"+currentTab+" tr#"+target2+" label[for='textASReSeWebpage']").attr("for","textASReSeWebpage_"+(count+1));
	$("table#accessAndServicesTable"+currentTab+" tr#"+target2+" input#textASReSeWebpage").attr("id","textASReSeWebpage_"+(count+1));
	$("table#accessAndServicesTable"+currentTab+" tr#"+target2+" label[for='textASReSeWebpageLinkTitle']").attr("for","textASReSeWebpageLinkTitle_"+(count+1));
	$("table#accessAndServicesTable"+currentTab+" tr#"+target2+" input#textASReSeWebpageLinkTitle").attr("id","textASReSeWebpageLinkTitle_"+(count+1));
}

function aSReSeToursAndSessions(){
	var currentTab = getCurrentTab();
	var count = $("table#accessAndServicesTable"+currentTab+" tr[id^='trASReSeToursAndSessions']").length;
	var target1 = "trASReSeToursAndSessions_"+(count+1);
	var target2 = "tr2ASReSeToursAndSessions_"+(count+1);
	var lastId = "table#accessAndServicesTable"+currentTab+" tr#tr2ASReSeToursAndSessions";
	if(count>1){
		lastId+="_"+(count);
	}
	var tr2HTML = "<tr id=\""+target1+"\">";
	tr2HTML += $("table#accessAndServicesTable"+currentTab+" tr#trASReSeToursAndSessions").clone().html();
	tr2HTML += "</tr>\n";
	if(count==1){
		tr2HTML += "<tr id=\""+target2+"\">"+$("table#accessAndServicesTable"+currentTab+" tr#tr2ASReSeToursAndSessions").clone().html();
	}else{
		tr2HTML += "<tr id=\""+target2+"\">"+$("table#accessAndServicesTable"+currentTab+" tr#tr2ASReSeToursAndSessions_"+(count)).clone().html();
	}
	tr2HTML += "</tr>";
	$(lastId).after(tr2HTML);
	//update new elements
	$("table#accessAndServicesTable"+currentTab+" tr#"+target1+" label[for='textASReSeToursAndSessions']").attr("for","textASReSeToursAndSessions_"+(count+1));
	$("table#accessAndServicesTable"+currentTab+" tr#"+target1+" input#textASReSeToursAndSessions").attr("id","textASReSeToursAndSessions_"+(count+1));
	$("table#accessAndServicesTable"+currentTab+" tr#"+target1+" label[for='selectASReSeToursAndSessionsSelectLanguage']").attr("for","selectASReSeToursAndSessionsSelectLanguage_"+(count+1));
	$("table#accessAndServicesTable"+currentTab+" tr#"+target1+" select#selectASReSeToursAndSessionsSelectLanguage").attr("id","selectASReSeToursAndSessionsSelectLanguage_"+(count+1));
	$("table#accessAndServicesTable"+currentTab+" tr#"+target2+" label[for='textASReSeTSWebpage']").attr("for","textASReSeTSWebpage_"+(count+1));
	$("table#accessAndServicesTable"+currentTab+" tr#"+target2+" input#textASReSeTSWebpage").attr("id","textASReSeTSWebpage_"+(count+1));
	$("table#accessAndServicesTable"+currentTab+" tr#"+target2+" label[for='textASReSeWebpageTSLinkTitle']").attr("for","textASReSeWebpageTSLinkTitle_"+(count+1));
	$("table#accessAndServicesTable"+currentTab+" tr#"+target2+" input#textASReSeWebpageTSLinkTitle").attr("id","textASReSeWebpageTSLinkTitle_"+(count+1));
}

function aSAddServices(){
	var currentTab = getCurrentTab();
	var count = $("table#accessAndServicesTable"+currentTab+" tr[id^='trASReSeOtherServices']").length;
	var target1 = "trASReSeOtherServices_"+(count+1);
	var target2 = "tr2ASReSeOtherServices_"+(count+1);
	var lastId = "table#accessAndServicesTable"+currentTab+" tr#tr2ASReSeOtherServices";
	if(count>1){
		lastId+="_"+(count);
	}
	var tr2HTML = "<tr id=\""+target1+"\">";
	tr2HTML += $("table#accessAndServicesTable"+currentTab+" tr#trASReSeOtherServices").clone().html();
	tr2HTML += "</tr>\n";
	if(count==1){
		tr2HTML += "<tr id=\""+target2+"\">"+$("table#accessAndServicesTable"+currentTab+" tr#tr2ASReSeOtherServices").clone().html();
	}else{
		tr2HTML += "<tr id=\""+target2+"\">"+$("table#accessAndServicesTable"+currentTab+" tr#tr2ASReSeOtherServices_"+(count)).clone().html();
	}
	tr2HTML += "</tr>";
	$(lastId).after(tr2HTML);
	//update new elements
	$("table#accessAndServicesTable"+currentTab+" tr#"+target1+" label[for='textASReSeOtherServices']").attr("for","textASReSeOtherServices_"+(count+1));
	$("table#accessAndServicesTable"+currentTab+" tr#"+target1+" input#textASReSeOtherServices").attr("id","textASReSeOtherServices_"+(count+1));
	$("table#accessAndServicesTable"+currentTab+" tr#"+target1+" label[for='selectASReSeOtherServicesSelectLanguage']").attr("for","selectASReSeOtherServicesSelectLanguage_"+(count+1));
	$("table#accessAndServicesTable"+currentTab+" tr#"+target1+" select#selectASReSeOtherServicesSelectLanguage").attr("id","selectASReSeOtherServicesSelectLanguage_"+(count+1));
	$("table#accessAndServicesTable"+currentTab+" tr#"+target2+" label[for='textASReSeOSWebpage']").attr("for","textASReSeOSWebpage_"+(count+1));
	$("table#accessAndServicesTable"+currentTab+" tr#"+target2+" input#textASReSeOSWebpage").attr("id","textASReSeOSWebpage_"+(count+1));
	$("table#accessAndServicesTable"+currentTab+" tr#"+target2+" label[for='textASReSeWebpageOSLinkTitle']").attr("for","textASReSeWebpageOSLinkTitle_"+(count+1));
	$("table#accessAndServicesTable"+currentTab+" tr#"+target2+" input#textASReSeWebpageOSLinkTitle").attr("id","textASReSeWebpageOSLinkTitle_"+(count+1));
}

function descriptionAddHistoryDescription(){
	var currentTab = getCurrentTab();
	// trAddHistory
	var count = $("table#descriptionTable"+currentTab+" tr[id^='trRepositoryHistory']").length;
	var newId = "trRepositoryHistory_"+(count+1);
	var trHtml = "<tr id=\""+newId+"\">"+$("table#descriptionTable"+currentTab+" tr[id='trRepositoryHistory']").clone().html()+"</tr>";
	var lastId= "table#descriptionTable"+currentTab+" tr#trRepositoryHistory";

	if(count>1){
		lastId += "_" + (count);
	}
	$(lastId).after(trHtml);
	// update last content
	$("table#descriptionTable"+currentTab+" tr#"+newId+" label[for='textRepositoryHistory']").attr("for","textRepositoryHistory_"+(count+1));
	$("table#descriptionTable"+currentTab+" tr#"+newId+" textarea#textRepositoryHistory").attr("id","textRepositoryHistory_"+(count+1));
	$("table#descriptionTable"+currentTab+" tr#"+newId+" label[for='selectLanguageRepositoryHistory']").attr("for","selectLanguageRepositoryHistory_"+(count+1));
	$("table#descriptionTable"+currentTab+" tr#"+newId+" select#selectLanguageRepositoryHistory").attr("id","selectLanguageRepositoryHistory_"+(count+1));
}

function descriptionAddFoundationInformation(){
	var currentTab = getCurrentTab();
	//trRuleOfRepositoryFoundation
	var count = $("table#descriptionTable"+currentTab+" tr[id^='trRuleOfRepositoryFoundation']").length;
	var newId = "trRuleOfRepositoryFoundation_"+(count+1);
	var trHtml = "<tr id=\""+newId+"\">"+$("table#descriptionTable"+currentTab+" tr[id='trRuleOfRepositoryFoundation']").clone().html()+"</tr>";
	var lastId = "table#descriptionTable"+currentTab+" tr#trRuleOfRepositoryFoundation";
	if(count>1){
		lastId+="_"+(count);
	}
	$(lastId).after(trHtml);
	//update last content
	$("table#descriptionTable"+currentTab+" tr#"+newId+" label[for='textRuleOfRepositoryFoundation']").attr("for","textRuleOfRepositoryFoundation_"+(count+1));
	$("table#descriptionTable"+currentTab+" tr#"+newId+" input#textRuleOfRepositoryFoundation").attr("id","textRuleOfRepositoryFoundation_"+(count+1));
	$("table#descriptionTable"+currentTab+" tr#"+newId+" label[for='selectLanguageRuleOfRepositoryFoundation']").attr("for","selectLanguageRuleOfRepositoryFoundation_"+(count+1));
	$("table#descriptionTable"+currentTab+" tr#"+newId+" select#selectLanguageRuleOfRepositoryFoundation").attr("id","selectLanguageRuleOfRepositoryFoundation_"+(count+1));
}
function descriptionAddSuppressionInformation(){
	var currentTab = getCurrentTab();
	//trDescriptionAddSuppressionInformation
	var count = $("table#descriptionTable"+currentTab+" tr[id^='trDescriptionAddSuppressionInformation']").length;
	var newId = "trDescriptionAddSuppressionInformation_"+(count+1);
	var trHtml = "<tr id=\""+newId+"\">"+$("table#descriptionTable"+currentTab+" tr[id='trDescriptionAddSuppressionInformation']").clone().html()+"</tr>";
	var lastId = "table#descriptionTable"+currentTab+" tr#trDescriptionAddSuppressionInformation";
	if(count>1){
		lastId+="_"+(count);
	}
	$(lastId).after(trHtml);
	//update last content
	$("table#descriptionTable"+currentTab+" tr#"+newId+" label[for='textRuleOfRepositorySuppression']").attr("for","textRuleOfRepositorySuppression_"+(count+1));
	$("table#descriptionTable"+currentTab+" tr#"+newId+" input#textRuleOfRepositorySuppression").attr("id","textRuleOfRepositorySuppression_"+(count+1));
	$("table#descriptionTable"+currentTab+" tr#"+newId+" label[for='selectLanguageRuleOfRepositorySuppression']").attr("for","selectLanguageRuleOfRepositorySuppression_"+(count+1));
	$("table#descriptionTable"+currentTab+" tr#"+newId+" select#selectLanguageRuleOfRepositorySuppression").attr("id","selectLanguageRuleOfRepositorySuppression_"+(count+1));
}

function descriptionAddAdministrationUnits(){
	// trDescriptionAddAdministrationUnits
	var currentTab = getCurrentTab();
	var count = $("table#descriptionTable"+currentTab+" tr[id^='trDescriptionAddAdministrationUnits']").length;
	var newId = "trDescriptionAddAdministrationUnits_"+(count+1);
	var trHtml = "<tr id=\""+newId+"\">"+$("table#descriptionTable"+currentTab+" tr[id='trDescriptionAddAdministrationUnits']").clone().html()+"</tr>";
	var lastId = "table#descriptionTable"+currentTab+" tr#trDescriptionAddAdministrationUnits";

	if(count>1){
		lastId+="_"+(count);
	}
	$(lastId).after(trHtml);
	//update last content
	$("table#descriptionTable"+currentTab+" tr#"+newId+" label[for='textUnitOfAdministrativeStructure']").attr("for","textUnitOfAdministrativeStructure_"+(count+1));
	$("table#descriptionTable"+currentTab+" tr#"+newId+" textarea#textUnitOfAdministrativeStructure").attr("id","textUnitOfAdministrativeStructure_"+(count+1));
	$("table#descriptionTable"+currentTab+" tr#"+newId+" label[for='selectLanguageUnitOfAdministrativeStructure']").attr("for","selectLanguageUnitOfAdministrativeStructure_"+(count+1));
	$("table#descriptionTable"+currentTab+" tr#"+newId+" select#selectLanguageUnitOfAdministrativeStructure").attr("id","selectLanguageUnitOfAdministrativeStructure_"+(count+1));
}

function descriptionAddBuildingDescription(){
	// trBuildingDescription
	var currentTab = getCurrentTab();
	var count = $("table#descriptionTable"+currentTab+" tr[id^='trBuildingDescription']").length;
	var newId = "trBuildingDescription_"+(count+1);
	var trHtml = "<tr id=\""+newId+"\">"+$("table#descriptionTable"+currentTab+" tr[id='trBuildingDescription']").clone().html()+"</tr>";
	var lastId = "table#descriptionTable"+currentTab+" tr#trBuildingDescription";

	if(count>1){
		lastId+="_"+(count);
	}
	$(lastId).after(trHtml);
	//update last content
	$("table#descriptionTable"+currentTab+" tr#"+newId+" label[for='textBuilding']").attr("for","textBuilding_"+(count+1));
	$("table#descriptionTable"+currentTab+" tr#"+newId+" textarea#textBuilding").attr("id","textBuilding_"+(count+1));
	$("table#descriptionTable"+currentTab+" tr#"+newId+" label[for='selectLanguageBuilding']").attr("for","selectLanguageBuilding_"+(count+1));
	$("table#descriptionTable"+currentTab+" tr#"+newId+" select#selectLanguageBuilding").attr("id","selectLanguageBuilding_"+(count+1));
}

function descriptionAddAnotherArchivalDescription() {
	// trArchivalAndOtherHoldings
	var currentTab = getCurrentTab();
	var count = $("table#descriptionTable"+currentTab+" tr[id^='trArchivalAndOtherHoldings']").length;
	var newId = "trArchivalAndOtherHoldings_"+(count+1);
	var trHtml = "<tr id=\""+newId+"\">"+$("table#descriptionTable"+currentTab+" tr[id='trArchivalAndOtherHoldings']").clone().html()+"</tr>";
	var lastId = "table#descriptionTable"+currentTab+" tr#trArchivalAndOtherHoldings";

	if(count>1){
		lastId+="_"+(count);
	}
	$(lastId).after(trHtml);
	//update last content
	$("table#descriptionTable"+currentTab+" tr#"+newId+" label[for='textArchivalAndOtherHoldings']").attr("for","textArchivalAndOtherHoldings_"+(count+1));
	$("table#descriptionTable"+currentTab+" tr#"+newId+" textarea#textArchivalAndOtherHoldings").attr("id","textArchivalAndOtherHoldings_"+(count+1));
	$("table#descriptionTable"+currentTab+" tr#"+newId+" label[for='selectLanguageArchivalAndOtherHoldings']").attr("for","selectLanguageArchivalAndOtherHoldings_"+(count+1));
	$("table#descriptionTable"+currentTab+" tr#"+newId+" select#selectLanguageArchivalAndOtherHoldings").attr("id","selectLanguageArchivalAndOtherHoldings_"+(count+1));
}

function controlAddFurtherLangsAnsScripts(){
	var count = $("table#controlTable tr[id^='trControlAddFurtherLangsAnsScriptsOne']").length;
	var target1 = "trControlAddFurtherLangsAnsScriptsOne_"+(count+1);
	var target2 = "trControlAddFurtherLangsAnsScriptsTwo_"+(count+1);
	var lastId = "table#controlTable tr#trControlAddFurtherLangsAnsScriptsTwo";
	if(count>1){
		lastId+="_"+(count);
	}
	var tr2HTML = "<tr id=\""+target1+"\">";
	tr2HTML += $("table#controlTable tr#trControlAddFurtherLangsAnsScriptsOne").clone().html();
	tr2HTML += "</tr>\n";
	tr2HTML += "<tr id=\""+target2+"\">"+$("table#controlTable tr#trControlAddFurtherLangsAnsScriptsTwo").clone().html();
	tr2HTML += "</tr>";
	$(lastId).after(tr2HTML);
	//update rest of new elements
	$("table#controlTable tr#"+target1+" label[for='selectDescriptionLanguage']").attr("for","selectDescriptionLanguage_"+(count+1));
	$("table#controlTable tr#"+target1+" select#selectDescriptionLanguage").attr("id","selectDescriptionLanguage_"+(count+1));
	$("table#controlTable tr#"+target2+" label[for='selectDescriptionScript']").attr("for","selectDescriptionScript_"+(count+1));
	$("table#controlTable tr#"+target2+" select#selectDescriptionScript").attr("id","selectDescriptionScript_"+(count+1));
}
function addContactAbbreviation(){
	var count = $("table#controlTable tr[id^='trContactAbbreviationOne']").length;
	var target1 = "trContactAbbreviationOne_"+(count+1);
	var target2 = "trContactAbbreviationTwo_"+(count+1);
	var lastId = "table#controlTable tr#trContactAbbreviationTwo";
	if(count>1){
		lastId+="_"+(count);
	}
	var tr2HTML = "<tr id=\""+target1+"\">";
	tr2HTML += $("table#controlTable tr#trContactAbbreviationOne").clone().html();
	tr2HTML += "</tr>\n";
	tr2HTML += "<tr id=\""+target2+"\">"+$("table#controlTable tr#trContactAbbreviationTwo").clone().html();
	tr2HTML += "</tr>";
	$(lastId).after(tr2HTML);
	//update rest of new elements
	$("table#controlTable tr#"+target1+" label[for='textContactAbbreviation']").attr("for","textContactAbbreviation_"+(count+1));
	$("table#controlTable tr#"+target1+" input#textContactAbbreviation").attr("id","textContactAbbreviation_"+(count+1));
	$("table#controlTable tr#"+target2+" label[for='textContactFullName']").attr("for","textContactFullName_"+(count+1));
	$("table#controlTable tr#"+target2+" input#textContactFullName").attr("id","textContactFullName_"+(count+1));
}

function relationAddNewResourceRelation(){
	var counter = $("table[id^='resourceRelationTable_']").length;
	var clone = $("table[id^='resourceRelationTable_"+counter+"']").clone();
	clone = "<table id='"+("resourceRelationTable_"+(counter+1))+"'>"+clone.html()+"</table>";
	$("table[id^='resourceRelationTable_"+counter+"']").after(clone);
	// Reset parametters.
	$("table#resourceRelationTable_"+(counter+1)+" input[type='text']").each(function(){
		$(this).val(""); // Clean all input_text.
	});
}

function relationAddNewInstitutionRelation(){
	var counter = $("table[id^='institutionRelationTable_']").length;
	var clone = $("table[id^='institutionRelationTable_"+counter+"']").clone();
	clone = "<table id='"+("institutionRelationTable_"+(counter+1))+"'>"+clone.html()+"</table>";
	$("table[id^='institutionRelationTable_"+counter+"']").after(clone);
	// Reset parametters.
	$("table#institutionRelationTable_"+(counter+1)+" input[type='text']").each(function(){
		$(this).val(""); // Clean all input_text.
	});
}
