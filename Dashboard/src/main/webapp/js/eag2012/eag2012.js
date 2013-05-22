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
	var jsonData =  "{'yourInstitution':" + jsonDataYourInstitution + "," +
	"'identity':" + jsonDataIdentity + "," +
	"'contact':" + jsonDataContact + "," +
	"'accessAndServices':" + jsonDataAccessAndServices + "," +
	"'description':" + jsonDataDescription + "," +
	"'control':" + jsonDataControl + "," +
	"'relations':" + jsonDataRelations + "}";

	$.post("storeEAG2012.action", { form:jsonData });
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
			if (position != -1) {
				yiMandatoryElements.splice(position, 1);
			}
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
			if (position != -1) {
				yiMandatoryElements.splice(position, 1);
			}
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
	jsonData += ",'visitorsAddress':{";
	for(var j=0; j<visitorsAddress.length; j++) {
		var yiMEVisitorsAddress = new Array("selectYIVASelectLanguage", "textYIStreet",
				"textYICity", "textYICountry");
		
		if(jsonData.substring(jsonData.length-1)!='{'){
			jsonData += ",";
		}
		jsonData += "'"+visitorsAddress[j]+"':{";
		//input type text
		$("#"+visitorsAddress[j]+" input[type='text']").each(function(){
			if(jsonData.charAt(jsonData.length-1)!=':'
				&& jsonData.charAt(jsonData.length-1)!='{'){
				jsonData += ",";
			}
			jsonData += "'"+$(this).attr("id")+"' : '"+$(this).attr("value")+"'";

			// Check fill mandatory fields.
			if ($(this).attr("value") != '' && j == 0) {
				var position = yiMEVisitorsAddress.indexOf($(this).attr("id"));
				if (position != -1) {
					yiMEVisitorsAddress.splice(position, 1);
				}
			}
		});
		//select options selected
		$("#"+visitorsAddress[j]+" select").each(function(){
			if(jsonData.charAt(jsonData.length-1)!=':'){
				jsonData += ",";
			}
			jsonData += "'"+$(this).attr("id")+"' : '"+$(this).attr("value")+"'";

			// Check fill mandatory fields.
			if ($(this).attr("value") != 'none' && j == 0) {
				var position = yiMEVisitorsAddress.indexOf($(this).attr("id"));
				if (position != -1) {
					yiMEVisitorsAddress.splice(position, 1);
				}
			}
		});
		if(yiMEVisitorsAddress.length>0 && j == 0){
			validationArray.push(visitorsAddress[j],yiMEVisitorsAddress);
		}
		jsonData += "}";
	}
	
	jsonData += "}";

	// Postal address.
	var postalAddress = new Array();
	$("table[id^='yiTablePostalAddress_']").each(function(){
		var id = $(this).attr("id");
		if(id.indexOf("#")>-1){
			id = id.substring(id.indexOf("#"));
		}
		postalAddress.push(id);
	});
	jsonData += ",'postalAddress':{";
	for(var j=0; j<postalAddress.length; j++) {
		var yiMEPostalAddress = new Array("selectYIPASelectLanguage", "textYIPAStreet",
				"textYIPACity");
		
		if(jsonData.substring(jsonData.length-1)!='{'){
			jsonData += ",";
		}
		jsonData += "'"+postalAddress[j]+"':{";
		//input type text
		$("#"+postalAddress[j]+" input[type='text']").each(function(){
			if(jsonData.charAt(jsonData.length-1)!=':'
				&& jsonData.charAt(jsonData.length-1)!='{'){
				jsonData += ",";
			}
			jsonData += "'"+$(this).attr("id")+"' : '"+$(this).attr("value")+"'";

			// Check fill mandatory fields.
			if ($(this).attr("value") != '') {
				var position = yiMEPostalAddress.indexOf($(this).attr("id"));
				if (position != -1) {
					yiMEPostalAddress.splice(position, 1);
				}
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
				if (position != -1) {
					yiMEPostalAddress.splice(position, 1);
				}
			}
		});
		if(yiMEPostalAddress.length>0){
			validationArray.push(postalAddress[j],yiMEPostalAddress);
		}
		jsonData += "}";
	}
	
	jsonData += "}";

	// Other fields.
	var yiMERepositories = new Array("textYIOpeningTimes");

	//input type text
	$("#yiTableOthers input[type='text']").each(function(){
		if(jsonData.charAt(jsonData.length-1)!=':'
			&& jsonData.charAt(jsonData.length-1)!='{'){
			jsonData += ",";
		}
		jsonData += "'"+$(this).attr("id")+"' : '"+$(this).attr("value")+"'";

		// Check fill mandatory fields.
		if ($(this).attr("value") != '') {
			var position = yiMERepositories.indexOf($(this).attr("id"));
			if (position != -1) {
				yiMERepositories.splice(position, 1);
			}
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

	// Table identityTable.
	//content from selects
	$("table#identitySelectTypeOfTheInstitution select").each(function(){
		if(jsonData.charAt(jsonData.length-1)!=':'){
			jsonData += ",";
		}
		var texts ="";
		$("select#" + $(this).attr("id") + " option").each(function(){
			if($(this).attr("selected")) {
				texts += $(this).text() + '_';
			}
		});
		texts = texts.substring(0, (texts.length - 1));

		jsonData += "'"+$(this).attr("id")+"' : '"+texts+"'";
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

	jsonData += ",'institutionNames':{";
	for(var j=0; j<institutionNames.length; j++) {
		if(jsonData.substring(jsonData.length-1)!='{'){
			jsonData += ",";
		}
		jsonData += "'"+institutionNames[j]+"':{";
		//input type text
		$("#"+institutionNames[j]+" input[type='text']").each(function(){
			if(jsonData.charAt(jsonData.length-1)!=':'
				&& jsonData.charAt(jsonData.length-1)!='{'){
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
		jsonData += "}";
	}
	jsonData += "}";

	// Parallel names.
	var parallelNames = new Array();
	$("table[id^='identityTableParallelNameOfTheInstitution_']").each(function(){
		var id = $(this).attr("id");
		if(id.indexOf("#")>-1){
			id = id.substring(id.indexOf("#"));
		}
		parallelNames.push(id);
	});

	jsonData += ",'parallelNames':{";
	for(var j=0; j<parallelNames.length; j++) {
		if(jsonData.substring(jsonData.length-1)!='{'){
			jsonData += ",";
		}
		jsonData += "'"+parallelNames[j]+"':{";
		//input type text
		$("#"+parallelNames[j]+" input[type='text']").each(function(){
			if(jsonData.charAt(jsonData.length-1)!=':'
				&& jsonData.charAt(jsonData.length-1)!='{'){
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
		jsonData += "}";
	}
	jsonData += "}";

	// Formerly names.
	var formerlyNames = new Array();
	$("table[id^='identityTableFormerlyUsedName_']").each(function(){
		var id = $(this).attr("id");
		if(id.indexOf("#")>-1){
			id = id.substring(id.indexOf("#"));
		}
		formerlyNames.push(id);
	});

	jsonData += ",'formerlyNames':{";
	for(var j=0; j<formerlyNames.length; j++) {
		if(jsonData.substring(jsonData.length-1)!='{'){
			jsonData += ",";
		}
		jsonData += "'"+formerlyNames[j]+"':{";
		//input type text
		$("#"+formerlyNames[j]+" input[type='text']").each(function(){
			if(jsonData.charAt(jsonData.length-1)!=':'
				&& jsonData.charAt(jsonData.length-1)!='{'){
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
		jsonData += "}";
	}
	jsonData += "}}";

	return jsonData;
};

var clickContactAction = function(text1){
	var currentTab = getCurrentTab();

	return checkContactTab(currentTab, text1);
};

function checkAllContactTabs(text1) {
	var counter = $("table[id^='contactTable_']").length;
	var jsonData = "{";

	for (var i = 1; i <= counter; i++) {
		if(jsonData.substring(jsonData.length-1)=='}'){
			jsonData += ",";
		}

		jsonData += "'contactTable_" + i + "':";

		var check = checkContactTab("_" + i, text1);

		if (!check){
			return false;
		}

		jsonData += check;
	}

	jsonData += "}";

	return jsonData;
};

function checkContactTab(currentTab, text1) {
	// Delete old checks
	deleteChecks();

	// Mandatory elements
	var contactMandatoryElements = new Array();
	var contactRepoMandatoryElements = new Array("textNameOfRepository", "selectRoleOfRepository");

	if (parseInt(currentTab.substring(currentTab.length-1))>'1') {
		contactMandatoryElements = contactMandatoryElements.concat(contactRepoMandatoryElements);
	}

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

		// Check fill mandatory fields.
		if ($(this).attr("value") != '') {
			var position = contactMandatoryElements.indexOf($(this).attr("id"));
			if (position != -1) {
				contactMandatoryElements.splice(position, 1);
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

		// Check fill mandatory fields.
		if ($(this).attr("value") != 'none') {
			var position = contactMandatoryElements.indexOf($(this).attr("id"));
			if (position != -1) {
				contactMandatoryElements.splice(position, 1);
			}
		}
	});

	//validation array
	var validationArray = new Array();

	// Visitors address.
	var visitorsAddress = new Array();
	$("table#contactTable" + currentTab + " table[id^='contactTableVisitorsAddress_']").each(function(){
		var id = $(this).attr("id");
		if(id.indexOf("#")>-1){
			id = id.substring(id.indexOf("#"));
		}
		visitorsAddress.push(id);
	});
	jsonData += ",'visitorsAddress':{";
	for(var j=0; j<visitorsAddress.length; j++) {
		var contactVAMandatoryElements = new Array("textContactStreetOfTheInstitution", "textContactCityOfTheInstitution",
				"textContactCountryOfTheInstitution", "selectLanguageVisitorAddress");

		if(jsonData.substring(jsonData.length-1)!='{'){
			if(jsonData.substring(jsonData.length-1)!=','){
				jsonData += ",";
			}
		}
		jsonData += "'"+visitorsAddress[j]+"':{";
		//input type text
		$("table#contactTable" + currentTab + " table#"+visitorsAddress[j]+" input[type='text']").each(function(){
			if(jsonData.charAt(jsonData.length-1)!=':'
				&& jsonData.charAt(jsonData.length-1)!='{'){
				if(jsonData.substring(jsonData.length-1)!=','){
					jsonData += ",";
				}
			}
			if ($(this).parent().parent().parent().parent().parent().parent().parent().parent().parent().attr("id").indexOf("divTempContainter") == -1) {
				jsonData += "'"+$(this).attr("id")+"' : '"+$(this).attr("value")+"'";
			}

			// Check fill mandatory fields.
			if ($(this).attr("value") != '' && j == 0) {
				var position = contactVAMandatoryElements.indexOf($(this).attr("id"));
				if (position != -1) {
					contactVAMandatoryElements.splice(position, 1);
				}
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

			// Check fill mandatory fields.
			if ($(this).attr("value") != 'none' && j == 0) {
				var position = contactVAMandatoryElements.indexOf($(this).attr("id"));
				if (position != -1) {
					contactVAMandatoryElements.splice(position, 1);
				}
			}
		});
		if(contactVAMandatoryElements.length>0 && j == 0){
			validationArray.push(visitorsAddress[j],contactVAMandatoryElements);
		}
		jsonData += "}";
	}
	
	jsonData += "}";

	// Postal address.
	var postalAddress = new Array();
	$("table#contactTable" + currentTab + " table[id^='contactTablePostalAddress_']").each(function(){
		var id = $(this).attr("id");
		if(id.indexOf("#")>-1){
			id = id.substring(id.indexOf("#"));
		}
		postalAddress.push(id);
	});
	jsonData += ",'postalAddress':{";
	for(var j=0; j<postalAddress.length; j++) {		
		if(jsonData.substring(jsonData.length-1)!='{'){
			jsonData += ",";
		}
		jsonData += "'"+postalAddress[j]+"':{";
		//input type text
		$("table#contactTable" + currentTab + " table#"+postalAddress[j]+" input[type='text']").each(function(){
			if(jsonData.charAt(jsonData.length-1)!=':'
				&& jsonData.charAt(jsonData.length-1)!='{'){
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
		jsonData += "}";
	}
	
	jsonData += "}}";

	for (var i = 0; i < contactMandatoryElements.length; i++) {
		var pFieldError = "<p id=\""+contactMandatoryElements[i]+"_required\" class=\"fieldRequired\">"+text1+"</p>";
		$("table#contactTable" + currentTab + " #" + contactMandatoryElements[i]).after(pFieldError);
	}

	for (var i = 0; i < validationArray.length; i = (i + 2)) {
		var array = validationArray[i+1];

		for (var j = 0; j < array.length; j++) {
			var pFieldError = "<p id=\""+validationArray[i]+"_required\" class=\"fieldRequired\">"+text1+"</p>";
			$("table#contactTable" + currentTab + " #"+validationArray[i]+" #" + array[j]).after(pFieldError);
		}
	}

	if (contactMandatoryElements.length != 0 || validationArray.length != 0) {
		return false;
	}

	return jsonData;
};

var clickAccessAndServicesAction = function(text1){
	var currentTab = getCurrentTab();

	return checkAccessAndServicesTab(currentTab, text1);
};

function checkAllAccessAndServicesTabs(text1) {
	var counter = $("table[id^='accessAndServicesTable_']").length;
	var jsonData = "{";

	for (var i = 1; i <= counter; i++) {
		if(jsonData.substring(jsonData.length-1)=='}'){
			jsonData += ",";
		}

		jsonData += "'accessAndServicesTable_" + i + "':";

		var check = checkAccessAndServicesTab("_" + i, text1);

		if (!check){
			return false;
		}

		jsonData += check;
	}
	
	jsonData += "}";

	return jsonData;
};

function checkAccessAndServicesTab(currentTab, text1) {
	// Delete old checks
	deleteChecks();

	// Mandatory elements
	var aasMandatoryElements = new Array("textOpeningTimes");

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
			if (position != -1) {
				aasMandatoryElements.splice(position, 1);
			}
		}
	});
	//content from selects
	$("table#accessAndServicesTable" + currentTab + " select").each(function(){
		if(jsonData.charAt(jsonData.length-1)!=':'){
			jsonData += ",";
		}
		if ($(this).attr("id)") == "selectASSRPhotographAllowance") {
			jsonData += "'"+$(this).attr("id")+"' : '"+$(this).text()+"'";
		} else if ($(this).attr("id") == "selectLanguageOpeningTimes"
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
		var pFieldError = "<p id=\""+aasMandatoryElements[i]+"_required\" class=\"fieldRequired\">"+text1+"</p>";
		$("table#accessAndServicesTable" + currentTab + " #" + aasMandatoryElements[i]).after(pFieldError);
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
	var jsonData = "{";

	for (var i = 1; i <= counter; i++) {
		if(jsonData.substring(jsonData.length-1)=='}'){
			jsonData += ",";
		}

		jsonData += "'descriptionTable_" + i + "':";

		jsonData += checkDescriptionTab("_" + i, text1);
	}
	
	jsonData += "}";

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
				if (position != -1) {
					controlMandatoryElements.splice(position, 1);
				}
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
	jsonData += "'resourceRelations':{";
	for(var j=0; j<resourceRelations.length; j++) {
		if(jsonData.substring(jsonData.length-1)!='{'){
			jsonData += ",";
		}
		jsonData += "'"+resourceRelations[j]+"':{";
		//input type text
		$("#"+resourceRelations[j]+" input[type='text']").each(function(){
			if(jsonData.charAt(jsonData.length-1)!=':'
				&& jsonData.charAt(jsonData.length-1)!='{'){
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
	
	jsonData += "}";

	// Institution relations.
	var institutionRelations = new Array();
	$("table[id^='institutionRelationTable_']").each(function(){
		var id = $(this).attr("id");
		if(id.indexOf("#")>-1){
			id = id.substring(id.indexOf("#"));
		}
		institutionRelations.push(id);
	});
	jsonData += ",'institutionRelations':{";
	for(var j=0; j<institutionRelations.length; j++) {
		if(jsonData.substring(jsonData.length-1)!='{'){
			jsonData += ",";
		}
		jsonData += "'"+institutionRelations[j]+"':{";
		//input type text
		$("#"+institutionRelations[j]+" input[type='text']").each(function(){
			if(jsonData.charAt(jsonData.length-1)!=':'
				&& jsonData.charAt(jsonData.length-1)!='{'){
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
	
	jsonData += "}}";

	return jsonData;
};

function checkAndShowNextTab(table, text1, text2){
	// Check table passed.
	var id =  $(table).attr("id");

	if (id == "yiTableOthers") {
		if (!clickYourInstitutionAction(text1)) {
			alertFillFieldsBeforeChangeTab(text2);
			return;
		} else {
			$("ul#eag2012TabsContainer a[href='#tab-identity']").trigger('click');
		}
	} else if (id == "identitySelectTypeOfTheInstitution") {
		if (!clickIdentityAction(text1)) {
			alertFillFieldsBeforeChangeTab(text2);
			return;
		} else {
			$("ul#eag2012TabsContainer a[href='#tab-contact']").trigger('click');
		}
	} else if (id.indexOf("contactTable") != -1) {
		if (!clickContactAction(text1)) {
			alertFillFieldsBeforeChangeTab(text2);
			return;
		} else {
			$("ul#eag2012TabsContainer a[href='#tab-accessAndServices']").trigger('click');
		}
	} else if (id.indexOf("accessAndServicesTable") != -1) {
		if (!clickAccessAndServicesAction(text1)) {
			alertFillFieldsBeforeChangeTab(text2);
			return;
		} else {
			$("ul#eag2012TabsContainer a[href='#tab-description']").trigger('click');
		}
	} else if (id.indexOf("descriptionTable") != -1) {
		if (!clickDescriptionAction(text1)) {
			alertFillFieldsBeforeChangeTab(text2);
			return;
		} else {
			$("ul#eag2012TabsContainer a[href='#tab-control']").trigger('click');
		}
	} else if (id == "controlTable") {
		if (!clickControlAction(text1)) {
			alertFillFieldsBeforeChangeTab(text2);
			return;
		} else {
			$("ul#eag2012TabsContainer a[href='#tab-relations']").trigger('click');
		}
	}
}

function checkFillValue(element) {
	if (($(element).attr("value") != null && $(element).attr("value") != "")
			|| ($(element).attr("value") != 'none')) {
		return true;
	}
}

function yiAddVisitorsAddressTranslation(text1) {
	var counter = $("table[id^='yiTableVisitorsAddress_']").length;

	var street = $("table#yiTableVisitorsAddress_"+counter+" input#textYIStreet").attr("value");
	var valanguage = $("table#yiTableVisitorsAddress_"+counter+" select#selectYIVASelectLanguage").attr("value");
	var city = $("table#yiTableVisitorsAddress_"+counter+" input#textYICity").attr("value");
	var country = $("table#yiTableVisitorsAddress_"+counter+" input#textYICountry").attr("value");

	if (street != null && street != "" && valanguage != "none"
			&& city != null && city != "" && country != null && country != "") {
		var clone = $("table[id^='yiTableVisitorsAddress_"+counter+"']").clone();
		clone = "<table id='"+("yiTableVisitorsAddress_"+(counter+1))+"'>"+clone.html()+"</table>";
		$("table[id^='yiTableVisitorsAddress_"+counter+"']").after(clone);
		// Reset parametters.
		$("table#yiTableVisitorsAddress_"+(counter+1)+" input[type='text']").each(function(){
			$(this).val(""); // Clean all input_text.
		});
		// Remove "*".
		$("table#yiTableVisitorsAddress_"+(counter+1)).find("span").remove();
	} else {
		alertEmptyFields(text1);
	}
}

function yiAddPostalAddressIfDifferent(text1, text2, text3, text4) {
	$("#buttonAddPostalAddressIfDifferent").hide();

	var select = '<select id="selectYIPASelectLanguage">'+$("#selectYIPNOTISelectLanguage").html()+'</select>';

	$("table#yiTableOthers").before('<table id="yiTablePostalAddress_1">'+
		'<tr id="yiPostalAddressLabel">'+
			'<td id="postalAddressLabel" colspan="4">'+text1+
			'</td>'+
		'</tr>'+
		'<tr id="yiPostalAddressStreet">'+
			'<td>'+
				'<label for="textYIPAStreet">'+text3+'<span class="required">*</span>:</label>'+
			'</td>'+
			'<td>'+
				'<input type="text" id="textYIPAStreet" />'+
			'</td>'+
			'<td id="yiPostalAddressLanguage" class="labelLeft">'+
				'<label for="selectYIPASelectLanguage">'+text2+'<span class="required">*</span>:</label>'+
			'</td>'+
			'<td>'+select+
			'</td>'+
		'</tr>'+
		'<tr id="yiPostalAddressCity">'+
			'<td>'+
				'<label for="textYIPACity">'+text4+'<span class="required">*</span>:</label>'+
			'</td>'+
			'<td>'+
				'<input type="text" id="textYIPACity" />'+
			'</td>'+
		'</tr></table>');

	$("table#yiTableOthers tr#yiPostalAddressTranslation").show();
	
}

function yiAddPostalAddressTranslation(text1) {
	var counter = $("table[id^='yiTablePostalAddress_']").length;

	var street = $("table#yiTablePostalAddress_"+counter+" input#textYIPAStreet").attr("value");
	var valanguage = $("table#yiTablePostalAddress_"+counter+" select#selectYIPASelectLanguage").attr("value");
	var city = $("table#yiTablePostalAddress_"+counter+" input#textYIPACity").attr("value");

	if (street != null && street != "" && valanguage != "none"
		&& city != null && city != "") {
		var clone = $("table[id^='yiTablePostalAddress_"+counter+"']").clone();
		clone = "<table id='"+("yiTablePostalAddress_"+(counter+1))+"'>"+clone.html()+"</table>";
		$("table[id^='yiTablePostalAddress_"+counter+"']").after(clone);
		// Reset parametters.
		$("table#yiTablePostalAddress_"+(counter+1)+" input[type='text']").each(function(){
			$(this).val(""); // Clean all input_text.
		});
	} else {
		alertEmptyFields(text1);
	}
}

function yiAddClosingDates() {
	$("#buttonAddClosingDates").hide();
	$("table#yiTableOthers tr#fieldClosingDates").show();
}
function yiFutherAccessInformation() {
	$("#buttonFutherAccessInformation").hide();
	$("#buttonFutherAccessInformation").after('<input type="text" id="futherAccessInformation" onchange="futherAccessInformationChanged();" />');
}
function yiAddFutherInformationOnExistingFacilities() {
	$("#buttonAddFutherInformationOnExistingFacilities").after('<input type="text" id="futherInformationOnExistingFacilities" onchange="futherInformationOnExistingFacilitiesChanged();" />');
	$("#buttonAddFutherInformationOnExistingFacilities").hide();
}

function addFurtherIds(text1, text2){
	var counter = $("input[id^='otherRepositorId']").length;
	if (counter == 0) {
		$("input#buttonAddFutherIds").parent().parent().before("<tr><td colspan=\"2\"></td><td class='labelLeft'><label for=\"otherRepositorId_"+($("input[id^='otherRepositorId']").length)+"\"> "+text1+":</label></td><td><input type=\"text\" id=\"otherRepositorId_"+($("input[id^='otherRepositorId']").length)+"\" /></td></tr>");
	} else {
		var value = $("input#otherRepositorId_" + (counter-1)).attr("value");
		if (value != null && value != "") {
			$("input#buttonAddFutherIds").parent().parent().before("<tr><td colspan=\"2\"></td><td class='labelLeft'><label for=\"otherRepositorId_"+($("input[id^='otherRepositorId']").length)+"\"> "+text1+":</label></td><td><input type=\"text\" id=\"otherRepositorId_"+($("input[id^='otherRepositorId']").length)+"\" /></td></tr>");
		} else {
			alertEmptyFields(text2);
		}
	}
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

	// Remove attr "disabled" for all elements in "contact" tab.
	$("table#contactTable_"+(counter+1)+" input#textContactStreetOfTheInstitution").removeAttr("disabled");
	$("table#contactTable_"+(counter+1)+" select#selectLanguageVisitorAddress").removeAttr("disabled");
	$("table#contactTable_"+(counter+1)+" input#textContactCityOfTheInstitution").removeAttr("disabled");
	$("table#contactTable_"+(counter+1)+" input#textContactCountryOfTheInstitution").removeAttr("disabled");
	$("table#contactTable_"+(counter+1)+" input#textContactLatitudeOfTheInstitution").removeAttr("disabled");
	$("table#contactTable_"+(counter+1)+" input#textContactLongitudeOfTheInstitution").removeAttr("disabled");
	$("table#contactTable_"+(counter+1)+" select#selectContinentOfTheInstitution").removeAttr("disabled");
	$("table#contactTable_"+(counter+1)+" input#textContactTelephoneOfTheInstitution").removeAttr("disabled");
	$("table#contactTable_"+(counter+1)+" input#textContactEmailOfTheInstitution").removeAttr("disabled");
	$("table#contactTable_"+(counter+1)+" input#textContactWebOfTheInstitution").removeAttr("disabled");

	// Remove attr "onchange" for all elements in "contact" tab.
	$("table#contactTable_"+(counter+1)+" input#textContactLinkTitleForEmailOfTheInstitution").removeAttr("onchange");
	$("table#contactTable_"+(counter+1)+" input#textContactLinkTitleForWebOfTheInstitution").removeAttr("onchange");

	// Remove "Next tab" button from tab "description".
	$("table#descriptionTable_"+(counter+1)+" td#tdButtonsDescriptionTab #buttonDescriptionTabNext").remove();

	//fill values with the current "your institution" values provided by user
	//contact tab
	var selectedIndex = document.getElementById('selectYIContinent').selectedIndex;
	var latitude = $("#textYILatitude").val();
	var longitude = $("#textYILongitude").val();
	var country = $("#textYICountry").val();
	var city = $("#textYICity").val();
	var street = $("#textYIStreet").val();
	var streetLanguage = $("#selectYIVASelectLanguage").val();
	var telephone = $("#textYITelephone").val();
	var email = $("#textYIEmailAddress").val();
	var emailLinkTitle = $("#textYIEmailLinkTitle").val();
	var web = $("#textYIWebpage").val();
	var webLinkTitle = $("#textYIWebpageLinkTitle").val();
	//contact table
	$("table#contactTable_"+(counter+1)+" #selectContinentOfTheInstitution option").eq(selectedIndex).prop("selected",true);
	$("table#contactTable_"+(counter+1)+" #selectLanguageVisitorAddress").attr("value",streetLanguage);
	$("table#contactTable_"+(counter+1)+" #textContactLatitudeOfTheInstitution").attr("value",latitude);
	$("table#contactTable_"+(counter+1)+" #textContactLongitudeOfTheInstitution").attr("value",longitude);
	$("table#contactTable_"+(counter+1)+" #textContactCountryOfTheInstitution").attr("value",country);
	$("table#contactTable_"+(counter+1)+" #textContactCityOfTheInstitution").attr("value",city);
	$("table#contactTable_"+(counter+1)+" #textContactStreetOfTheInstitution").attr("value",street);
	$("table#contactTable_"+(counter+1)+" #textContactTelephoneOfTheInstitution").attr("value",telephone);
	$("table#contactTable_"+(counter+1)+" #textContactEmailOfTheInstitution").attr("value",email);
	$("table#contactTable_"+(counter+1)+" #textContactLinkTitleForEmailOfTheInstitution").attr("value",emailLinkTitle);
	$("table#contactTable_"+(counter+1)+" #textContactWebOfTheInstitution").attr("value",web);
	$("table#contactTable_"+(counter+1)+" #textContactLinkTitleForWebOfTheInstitution").attr("value",webLinkTitle);

	// add name of repository to contact tab.
	$("table#contactTable_"+(counter+1)+" tr#trVisitorsAddressLabel").before("<tr>"+
			"<td id=\"tdNameOfRepository\">"+
				"<label for=\"textNameOfRepository\">"+text3+"<span class=\"required\">*</span>:</label>"+
			"</td>"+
			"<td>"+
				"<input type=\"text\" id=\"textNameOfRepository\" />"+
			"<td>"+
				"<label for=\"selectRoleOfRepository\">"+text4+"<span class=\"required\">*</span>:</label>"+
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
	var closing = $("#yourInstitutionClosingDates").val();
	var accessPublic = document.getElementById('selectAccessibleToThePublic').selectedIndex;
	var accessibilityDisabledPeople = $("#selectFacilitiesForDisabledPeopleAvailable").val();
	$("table#accessAndServicesTable_"+(counter+1)+" #textOpeningTimes").attr("value",opening);
	$("table#accessAndServicesTable_"+(counter+1)+" #textClosingDates").attr("value",closing);
	$("table#accessAndServicesTable_"+(counter+1)+" #selectASAccesibleToThePublic option").eq(accessPublic).prop("selected",true);
	$("table#accessAndServicesTable_"+(counter+1)+" #selectASFacilitiesForDisabledPeopleAvailable").attr("value",accessibilityDisabledPeople);
	
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

function addFurtherEmailsOfTheInstitution(text1){
	var currentTab = getCurrentTab();
	var count = $("table#contactTable"+currentTab+" tr[id^='trEmailOfTheInstitution']").length;

	var email = "";
	var title = "";

	if (count == 1) {
		email = "filled";
		title = $("table#contactTable"+currentTab+" tr#trEmailOfTheInstitution input#textContactLinkTitleForEmailOfTheInstitution").attr("value");
	} else {
		email = $("table#contactTable"+currentTab+" tr#trEmailOfTheInstitution_"+count+" input#textContactEmailOfTheInstitution_"+count).attr("value");
		title = $("table#contactTable"+currentTab+" tr#trEmailOfTheInstitution_"+count+" input#textContactLinkTitleForEmailOfTheInstitution_"+count).attr("value");
	}

	if (email == null || email == ""
			|| title == null || title == "") {
			alertEmptyFields(text1);
			return;
	}

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

function addAnotherFormOfTheAuthorizedName(text1){
	var counter = $("table[id^='identityTableNameOfTheInstitution_']").length;

	if (counter > 1) {
		var nameOfInstitution = $("table#identityTableNameOfTheInstitution_"+counter+" input#textNameOfTheInstitution").attr("value");
		var nameOfInstitutionLanguage = $("table#identityTableNameOfTheInstitution_"+counter+" select#noti_languageList").attr("value");

		if (nameOfInstitution == null || nameOfInstitution == ""
				|| nameOfInstitutionLanguage == "none") {
			alertEmptyFields(text1);
			return;
		}
	}

	var clone = $("table[id^='identityTableNameOfTheInstitution_"+counter+"']").clone();
	clone = "<table id='"+("identityTableNameOfTheInstitution_"+(counter+1))+"'>"+clone.html()+"</table>";
	$("table[id^='identityTableNameOfTheInstitution_"+counter+"']").after(clone);
	// Reset parametters.
	$("table#identityTableNameOfTheInstitution_"+(counter+1)+" input[type='text']").each(function(){
		$(this).val(""); // Clean all input_text.
	});
	$("table#identityTableNameOfTheInstitution_"+(counter+1)+" tr#trNameOfTheInstitution input#textNameOfTheInstitution").removeAttr("disabled");
	$("table#identityTableNameOfTheInstitution_"+(counter+1)+" tr#trNameOfTheInstitution td#tdNameOfTheInstitution").find("span").remove();
	$("table#identityTableNameOfTheInstitution_"+(counter+1)+" tr#trNameOfTheInstitution select#noti_languageList").removeAttr("disabled");
	$("table#identityTableNameOfTheInstitution_"+(counter+1)+" tr#trNameOfTheInstitution td#tdNameOfTheInstitutionLanguage").find("span").remove();
}

function addParallelNameOfTheInstitution(text1){
	var counter = $("table[id^='identityTableParallelNameOfTheInstitution_']").length;

	if (counter > 1) {
		var nameOfInstitution = $("table#identityTableParallelNameOfTheInstitution_"+counter+" input#textParallelNameOfTheInstitution").attr("value");
		var nameOfInstitutionLanguage = $("table#identityTableParallelNameOfTheInstitution_"+counter+" select#pnoti_languageList").attr("value");

		if (nameOfInstitution == null || nameOfInstitution == ""
				|| nameOfInstitutionLanguage == "none") {
			alertEmptyFields(text1);
			return;
		}
	}

	var clone = $("table[id^='identityTableParallelNameOfTheInstitution_"+counter+"']").clone();
	clone = "<table id='"+("identityTableParallelNameOfTheInstitution_"+(counter+1))+"'>"+clone.html()+"</table>";
	$("table[id^='identityTableParallelNameOfTheInstitution_"+counter+"']").after(clone);
	// Reset parametters.
	$("table#identityTableParallelNameOfTheInstitution_"+(counter+1)+" input[type='text']").each(function(){
		$(this).val(""); // Clean all input_text.
	});
	$("table#identityTableParallelNameOfTheInstitution_"+(counter+1)+" tr#trParallelNameOfTheInstitution input#textParallelNameOfTheInstitution").removeAttr("disabled");
	$("table#identityTableParallelNameOfTheInstitution_"+(counter+1)+" tr#trParallelNameOfTheInstitution select#pnoti_languageList").removeAttr("disabled");
}

function addMoreAnotherFormerlyUsedName(text1, text2, text3, text4, text5, text6, text7, text8, text9){
	var counter = $("table[id^='identityTableFormerlyUsedName_']").length;
	var select = '<select id="tfun_languageList">'+$("#pnoti_languageList").html()+'</select>';

	if (counter == 0) {
		$("table#identityButtonAddFormerlyUsedName").before('<table id="identityTableFormerlyUsedName_1">'+
			'<tr id="trTextFormerlyUsedName" class="marginTop">'+
				'<td>'+
					'<label for="textFormerlyUsedName">'+text1+':</label>'+
				'</td>'+
				'<td>'+
					'<input type="text" id="textFormerlyUsedName" value=""/>'+
				'</td>'+
				'<td class="labelLeft">'+
					'<label for="tfun_languageList">'+text2+':</label>'+
				'</td>'+
				'<td>'+select+
				'</td>'+
			'</tr>'+
			'<tr id="trLabelDatesWhenThisNameWasUsed">'+
				'<td colspan="4">'+
					'<label for="textDatesWhenThisNameWasUsed">'+text3+':</label>'+
				'</td>'+
			'</tr>'+
			'<tr id="trYearWhenThisNameWasUsed_1">'+
				'<td>'+
					'<label for="textYearWhenThisNameWasUsed_1">'+text4+':</label>'+
				'</td>'+
				'<td>'+
					'<input type="text" id="textYearWhenThisNameWasUsed_1" value=""/>'+
				'</td>'+
				'<td colspan="2">'+
				'</td>'+
			'</tr>'+
			'<tr>'+
				'<td>'+
					'<input type="button" id="buttonAddSingleYear" value="'+text5+'" onclick="addSingleYear($(this).parent().parent().parent().parent(), \''+text9+'\');" />'+
				'</td>'+
				'<td>'+
				'<input type="button" id="buttonAddYearRange" value="'+text6+'" onclick="addRangeYear($(this).parent().parent().parent().parent(), \''+text7+'\', \''+text8+'\', \''+text9+'\');" />'+
			'</td>'+
				'<td colspan="2">'+
				'</td>'+
			'</tr></table>');
	} else {
		var formerlyUsedName = $("table#identityTableFormerlyUsedName_"+counter+" input#textFormerlyUsedName").attr("value");
		var formerlyUsedNameLanguage = $("table#identityTableFormerlyUsedName_"+counter+" select#tfun_languageList").attr("value");

		var counterYears = $("table#identityTableFormerlyUsedName_"+counter+" input[id^='textYearWhenThisNameWasUsed_']").length;
		var year = $("table#identityTableFormerlyUsedName_"+counter+" input#textYearWhenThisNameWasUsed_"+counterYears).attr("value");

		if (formerlyUsedName == null || formerlyUsedName == ""
				|| formerlyUsedNameLanguage == "none"
				|| year == null || year == "") {
			alertEmptyFields(text9);
			return;
		}

		$("table#identityButtonAddFormerlyUsedName").before('<table id="identityTableFormerlyUsedName_'+(counter+1)+'">'+
			'<tr id="trTextFormerlyUsedName" class="marginTop">'+
				'<td>'+
					'<label for="textFormerlyUsedName">'+text1+':</label>'+
				'</td>'+
				'<td>'+
					'<input type="text" id="textFormerlyUsedName" value=""/>'+
				'</td>'+
				'<td class="labelLeft">'+
					'<label for="tfun_languageList">'+text2+':</label>'+
				'</td>'+
				'<td>'+select+
				'</td>'+
			'</tr>'+
			'<tr id="trLabelDatesWhenThisNameWasUsed">'+
				'<td colspan="4">'+
					'<label for="textDatesWhenThisNameWasUsed">'+text3+':</label>'+
				'</td>'+
			'</tr>'+
			'<tr id="trYearWhenThisNameWasUsed_1">'+
				'<td>'+
					'<label for="textYearWhenThisNameWasUsed_1">'+text4+':</label>'+
				'</td>'+
				'<td>'+
					'<input type="text" id="textYearWhenThisNameWasUsed_1" value=""/>'+
				'</td>'+
				'<td colspan="2">'+
				'</td>'+
			'</tr>'+
			'<tr>'+
				'<td>'+
					'<input type="button" id="buttonAddSingleYear" value="'+text5+'" onclick="addSingleYear($(this).parent().parent().parent().parent(), \''+text9+'\');" />'+
				'</td>'+
				'<td>'+
				'<input type="button" id="buttonAddYearRange" value="'+text6+'" onclick="addRangeYear($(this).parent().parent().parent().parent(), \''+text7+'\', \''+text8+'\', \''+text9+'\');" />'+
			'</td>'+
				'<td colspan="2">'+
				'</td>'+
			'</tr></table>');
	}
}

function addSingleYear(table, text1){
	var id =  $(table).attr("id");
	var counter = $("table#"+id+" tr[id^='trYearWhenThisNameWasUsed_']").length;

	var year = $("table#"+id+" tr#trYearWhenThisNameWasUsed_"+counter+" input#textYearWhenThisNameWasUsed_"+counter).attr("value");

	if (year == null || year == "") {
		alertEmptyFields(text1);
		return;
	}

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

function addRangeYear(table, text1, text2, text3){
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
					"<td class=\"labelLeft\">"+
						"<label for=\"textYearWhenThisNameWasUsedTo_1\">"+text2+":</label>"+
					"</td>"+
					"<td>"+
						"<input type=\"text\" id=\"textYearWhenThisNameWasUsedTo_1\" value=\"\" />"+
					"</td>"+
				"</tr>";
		$("table#"+id+" tr[id^='trYearWhenThisNameWasUsed_"+count+"']").after(yearRange);
	} else {
		var yearFrom = $("table#"+id+" tr#trYearRangeWhenThisNameWasUsed_"+counter+" input#textYearWhenThisNameWasUsedFrom_"+counter).attr("value");
		var yearTo = $("table#"+id+" tr#trYearRangeWhenThisNameWasUsed_"+counter+" input#textYearWhenThisNameWasUsedTo_"+counter).attr("value");

		if (yearFrom == null || yearFrom == ""
				|| yearTo == null || yearTo == "") {
			alertEmptyFields(text3);
			return;
		}

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

function contactAddVisitorsAddressTranslation(text1) {
	var currentTab = getCurrentTab();
	var counter = $("table#contactTable"+currentTab+" table[id^='contactTableVisitorsAddress_']").length;

	var street = $("table#contactTable"+currentTab+" table#contactTableVisitorsAddress_"+counter+" input#textContactStreetOfTheInstitution").attr("value");
	var valanguage = $("table#contactTable"+currentTab+" table#contactTableVisitorsAddress_"+counter+" select#selectLanguageVisitorAddress").attr("value");
	var city = $("table#contactTable"+currentTab+" table#contactTableVisitorsAddress_"+counter+" input#textContactCityOfTheInstitution").attr("value");
//	var district = $("table#contactTable"+currentTab+" table#contactTableVisitorsAddress_"+counter+" input#textContactDistrictOfTheInstitution").attr("value");
//	var county = $("table#contactTable"+currentTab+" table#contactTableVisitorsAddress_"+counter+" input#textContactCountyOfTheInstitution").attr("value");
//	var region = $("table#contactTable"+currentTab+" table#contactTableVisitorsAddress_"+counter+" input#textContactRegionOfTheInstitution").attr("value");
	var country = $("table#contactTable"+currentTab+" table#contactTableVisitorsAddress_"+counter+" input#textContactCountryOfTheInstitution").attr("value");

	if (street == null || street == "" || valanguage == "none"
			|| city == null || city == "" /*|| district == null || district == ""
			|| county == null || county == "" || region == null || region == "" */
			|| country == null || country == "") {
		alertEmptyFields(text1);
		return;
	}

	var clone = $("table#contactTable"+currentTab+" table[id^='contactTableVisitorsAddress_"+counter+"']").clone();
	clone = "<table id='"+("contactTableVisitorsAddress_"+(counter+1))+"'>"+clone.html()+"</table>";
	$("table#contactTable"+currentTab+" table[id^='contactTableVisitorsAddress_"+counter+"']").after(clone);
	// Reset parametters and enable fields.
	$("table#contactTable"+currentTab+" table#contactTableVisitorsAddress_"+(counter+1)+" input[type='text']").each(function(){
		$(this).val(""); // Clean all input_text.
		$(this).removeAttr("disabled");
	});
	$("table#contactTable"+currentTab+" table#contactTableVisitorsAddress_"+(counter+1)+" select").removeAttr("disabled");
	// Remove "*".
	$("table#contactTable"+currentTab+" table#contactTableVisitorsAddress_"+(counter+1)).find("span").remove();
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
				'<label for="selectContactLanguagePostalAddress">'+property2+':</label>'+
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

function contactAddPostalAddressTranslation(text1) {
	var currentTab = getCurrentTab();
	var counter = $("table#contactTable"+currentTab+" table[id^='contactTablePostalAddress_']").length;

	var street = $("table#contactTable"+currentTab+" table#contactTablePostalAddress_"+counter+" input#textContactPAStreet").attr("value");
	var valanguage = $("table#contactTable"+currentTab+" table#contactTablePostalAddress_"+counter+" select#selectContactLanguagePostalAddress").attr("value");
	var city = $("table#contactTable"+currentTab+" table#contactTablePostalAddress_"+counter+" input#textContactPACity").attr("value");

	if (street == null || street == "" || valanguage == "none"
			|| city == null || city == "") {
		alertEmptyFields(text1);
		return;
	}

	var clone = $("table#contactTable"+currentTab+" table[id^='contactTablePostalAddress_"+counter+"']").clone();
	clone = "<table id='"+("contactTablePostalAddress_"+(counter+1))+"'>"+clone.html()+"</table>";
	$("table#contactTable"+currentTab+" table[id^='contactTablePostalAddress_"+counter+"']").after(clone);
	// Reset parametters and enable fields.
	$("table#contactTable"+currentTab+" table#contactTablePostalAddress_"+(counter+1)+" input[type='text']").each(function(){
		$(this).val(""); // Clean all input_text.
	});
}

function addFurtherTelephoneOfTheInstitution(text1){
	var currentTab = getCurrentTab();
	var count = $("table#contactTable"+currentTab+" tr[id^='trTelephoneOfTheInstitution']").length;

	if (count>1) {
		var telephone = $("table#contactTable"+currentTab+" tr#trTelephoneOfTheInstitution_"+count+" input#textContactTelephoneOfTheInstitution_"+count).attr("value");

		if (telephone == null || telephone == "") {
				alertEmptyFields(text1);
				return;
			}
	}

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

function addFurtherFaxOfTheInstitution(text1){
	var currentTab = getCurrentTab();
	var count = $("table#contactTable"+currentTab+" tr[id^='trFaxOfTheInstitution']").length;

	var id = "";
	if (count > 1) {
		id +="_"+count;
	}
	var fax = $("table#contactTable"+currentTab+" tr#trFaxOfTheInstitution"+id+" input#textContactFaxOfTheInstitution"+id).attr("value");

	if (fax == null || fax == "") {
			alertEmptyFields(text1);
			return;
	}

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

function addFurtherWebsOfTheInstitution(text1){
	var currentTab = getCurrentTab();
	var count = $("table#contactTable"+currentTab+" tr[id^='trWebOfTheInstitution']").length;

	var web = "";
	var title = "";

	if (count == 1) {
		web = "filled";
		title = $("table#contactTable"+currentTab+" tr#trWebOfTheInstitution input#textContactLinkTitleForWebOfTheInstitution").attr("value");
	} else {
		web = $("table#contactTable"+currentTab+" tr#trWebOfTheInstitution_"+count+" input#textContactWebOfTheInstitution_"+count).attr("value");
		title = $("table#contactTable"+currentTab+" tr#trWebOfTheInstitution_"+count+" input#textContactLinkTitleForWebOfTheInstitution_"+count).attr("value");
	}

	if (web == null || web == ""
			|| title == null || title == "") {
			alertEmptyFields(text1);
			return;
	}

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
	$("table#contactTable"+currentTab+" tr#"+newId+" td#tdLabelTextContactWebOfTheInstitution").find("span").remove();
}

function aSAddOpeningTimes(text1){
	var currentTab = getCurrentTab();
	// trASOpeningTimes
	var count = $("table#accessAndServicesTable" + currentTab + " tr[id^='trASOpeningTimes']").length;

	var id = "";
	if (count > 1) {
		id +="_"+count;
	}

	var opening = $("table#accessAndServicesTable"+currentTab+" tr#trASOpeningTimes"+id+" input#textOpeningTimes"+id).attr("value");
	var openingLanguage = $("table#accessAndServicesTable"+currentTab+" tr#trASOpeningTimes"+id+" select#selectLanguageOpeningTimes"+id).attr("value");

	if (opening == null || opening == ""
			|| openingLanguage == "none") {
		alertEmptyFields(text1);
		return;
	}

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
	$("table#accessAndServicesTable"+currentTab+" tr#"+newId+" td#tdOpeningTimes").find("span").remove();

	// Reset parametters.
	$("table#accessAndServicesTable"+currentTab+" tr#"+newId+" input[type='text']").each(function(){
		$(this).val(""); // Clean all input_text.
	});
}

function aSAddClosingDates(text1){
	var currentTab = getCurrentTab();
	// trASClosingDates
	var count = $("table#accessAndServicesTable" + currentTab + " tr[id^='trASClosingDates']").length;

	var id = "";
	if (count > 1) {
		id +="_"+count;
	}

	var closing = $("table#accessAndServicesTable"+currentTab+" tr#trASClosingDates"+id+" input#textClosingDates"+id).attr("value");
	var closingLanguage = $("table#accessAndServicesTable"+currentTab+" tr#trASClosingDates"+id+" select#selectLanguageClosingDates"+id).attr("value");

	if (closing == null || closing == ""
			|| closingLanguage == "none") {
		alertEmptyFields(text1);
		return;
	}

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

	// Reset parametters.
	$("table#accessAndServicesTable"+currentTab+" tr#"+newId+" input[type='text']").each(function(){
		$(this).val(""); // Clean all input_text.
	});
}

function aSAddTravellingDirections(text1){
	var currentTab = getCurrentTab();
	var count = $("table#accessAndServicesTable"+currentTab+" tr[id^='trTravellingDirections']").length;

	var id = "";
	if (count > 1) {
		id +="_"+count;
	}

	var travel = $("table#accessAndServicesTable"+currentTab+" tr#trTravellingDirections"+id+" textarea#textTravellingDirections"+id).attr("value");
	var travelLanguage = $("table#accessAndServicesTable"+currentTab+" tr#trTravellingDirections"+id+" select#selectASATDSelectLanguage"+id).attr("value");
	var travelLink = $("table#accessAndServicesTable"+currentTab+" tr#tr2TravellingDirections"+id+" input#textTravelLink"+id).attr("value");

	if (travel == null || travel == ""
			|| travelLanguage == "none"
			|| travelLink == null || travelLink == "") {
		alertEmptyFields(text1);
		return;
	}

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
	$("table#accessAndServicesTable"+currentTab+" tr#"+target2+" label[for='textTravelLink']").attr("for","textTravelLink_"+(count+1));
	$("table#accessAndServicesTable"+currentTab+" tr#"+target2+" input#textTravelLink").attr("id","textTravelLink_"+(count+1));
	$("table#accessAndServicesTable"+currentTab+" tr#"+target1+" label[for='selectASATDSelectLanguage']").attr("for","selectASATDSelectLanguage_"+(count+1));
	$("table#accessAndServicesTable"+currentTab+" tr#"+target1+" select#selectASATDSelectLanguage").attr("id","selectASATDSelectLanguage_"+(count+1));
}

function addFutherAccessInformation(text1){
	var currentTab = getCurrentTab();
	var count = $("table#accessAndServicesTable"+currentTab+" tr[id^='trASAccessRestrictions']").length;

	var id = "";
	if (count > 1) {
		id +="_"+count;
	}

	var access = $("table#accessAndServicesTable"+currentTab+" tr#trASAccessRestrictions"+id+" input#textASAccessRestrictions"+id).attr("value");
	var accessLanguage = $("table#accessAndServicesTable"+currentTab+" tr#trASAccessRestrictions"+id+" select#selectASARSelectLanguage"+id).attr("value");

	if (access == null || access == ""
			|| accessLanguage == "none") {
		alertEmptyFields(text1);
		return;
	}

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

	// Reset parametters.
	$("table#accessAndServicesTable"+currentTab+" tr#"+newId+" input[type='text']").each(function(){
		$(this).val(""); // Clean all input_text.
	});
}

function aSAddFutherTermOfUse(text1){
	var currentTab = getCurrentTab();
	var count = $("table#accessAndServicesTable"+currentTab+" tr[id^='trASAddFutherTermOfUse']").length;

	var id = "";
	if (count > 1) {
		id +="_"+count;
	}

	var terms = $("table#accessAndServicesTable"+currentTab+" tr#trASAddFutherTermOfUse"+id+" textarea#textASTermOfUse"+id).attr("value");
	var termsLanguage = $("table#accessAndServicesTable"+currentTab+" tr#trASAddFutherTermOfUse"+id+" select#selectASAFTOUSelectLanguage"+id).attr("value");
	var termsLink = $("table#accessAndServicesTable"+currentTab+" tr#tr2ASAddFutherTermOfUse"+id+" input#textASTOULink"+id).attr("value");

	if (terms == null || terms == ""
			|| termsLanguage == "none"
			|| termsLink == null || termsLink == "") {
		alertEmptyFields(text1);
		return;
	}

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
	$("table#accessAndServicesTable"+currentTab+" tr#"+target2+" label[for='textTravelLink']").attr("for","textTravelLink_"+(count+1));
	$("table#accessAndServicesTable"+currentTab+" tr#"+target2+" input#textASTOULink").attr("id","textASTOULink_"+(count+1));
	$("table#accessAndServicesTable"+currentTab+" tr#"+target1+" label[for='textASTOULink']").attr("for","textASTOULink_"+(count+1));
	$("table#accessAndServicesTable"+currentTab+" tr#"+target1+" select#selectASAFTOUSelectLanguage").attr("id","selectASAFTOUSelectLanguage_"+(count+1));
}

function addAccessibilityInformation(text1){
	var currentTab = getCurrentTab();
	var count = $("table#accessAndServicesTable"+currentTab+" tr[id^='trAccessibilityInformation']").length;

	var id = "";
	if (count > 1) {
		id +="_"+count;
	}

	var facilities = $("table#accessAndServicesTable"+currentTab+" tr#trAccessibilityInformation"+id+" input#textASAccessibility"+id).attr("value");
	var facilitiesLanguage = $("table#accessAndServicesTable"+currentTab+" tr#trAccessibilityInformation"+id+" select#selectASASelectLanguage"+id).attr("value");

	if (facilities == null || facilities == ""
			|| facilitiesLanguage == "none") {
		alertEmptyFields(text1);
		return;
	}

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

	// Reset parametters.
	$("table#accessAndServicesTable"+currentTab+" tr#"+newId+" input[type='text']").each(function(){
		$(this).val(""); // Clean all input_text.
	});
}

function aSSRAddDescriptionOfYourComputerPlaces(property1, property2, text1){
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
		var id = "";
		if (count > 1) {
			id +="_"+count;
		}

		var facilities = $("table#accessAndServicesTable"+currentTab+" tr#trASSRDescriptionOfYourComputerPlaces"+id+" input#textDescriptionOfYourComputerPlaces"+id).attr("value");
		var facilitiesLanguage = $("table#accessAndServicesTable"+currentTab+" tr#trASSRDescriptionOfYourComputerPlaces"+id+" select#selectDescriptionOfYourComputerPlaces"+id).attr("value");

		if (facilities == null || facilities == ""
				|| facilitiesLanguage == "none") {
			alertEmptyFields(text1);
			return;
		}

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

function aSSRAddReadersTicket(text1){
	var currentTab = getCurrentTab();
	var count = $("table#accessAndServicesTable"+currentTab+" tr[id^='trASSRReadersTicket']").length;

	var id = "";
	if (count > 1) {
		id +="_"+count;
	}

	var reader = $("table#accessAndServicesTable"+currentTab+" tr#trASSRReadersTicket"+id+" input#textASSRReadersTicket"+id).attr("value");
	var readerLanguage = $("table#accessAndServicesTable"+currentTab+" tr#trASSRReadersTicket"+id+" select#selectReadersTickectLanguage"+id).attr("value");
	var readerLink = $("table#accessAndServicesTable"+currentTab+" tr#tr2ASSRReadersTicket"+id+" input#textASSRRTLink"+id).attr("value");

	if (reader == null || reader == ""
			|| readerLanguage == "none"
			|| readerLink == null || readerLink == "") {
		alertEmptyFields(text1);
		return;
	}

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

function aSSRAddFurtherOrderInformation(text1){
	var currentTab = getCurrentTab();
	var count = $("table#accessAndServicesTable"+currentTab+" tr[id^='trASSRAddFurtherOrderInformation']").length;

	var id = "";
	if (count > 1) {
		id +="_"+count;
	}

	var further = $("table#accessAndServicesTable"+currentTab+" tr#trASSRAddFurtherOrderInformation"+id+" input#textASSRAdvancedOrders"+id).attr("value");
	var furtherLanguage = $("table#accessAndServicesTable"+currentTab+" tr#trASSRAddFurtherOrderInformation"+id+" select#selectASSRAFOIUSelectLanguage"+id).attr("value");
	var furtherLink = $("table#accessAndServicesTable"+currentTab+" tr#tr2ASSRAddFurtherOrderInformation"+id+" input#textASSRAOLink"+id).attr("value");

	if (further == null || further == ""
			|| furtherLanguage == "none"
			|| furtherLink == null || furtherLink == "") {
		alertEmptyFields(text1);
		return;
	}

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

function aSAddResearchServices(text1){
	var currentTab = getCurrentTab();
	var count = $("table#accessAndServicesTable"+currentTab+" tr[id^='trASSRResearchServices']").length;

	var id = "";
	if (count > 1) {
		id +="_"+count;
	}

	var research = $("table#accessAndServicesTable"+currentTab+" tr#trASSRResearchServices"+id+" input#textASSRResearchServices"+id).attr("value");
	var researchLanguage = $("table#accessAndServicesTable"+currentTab+" tr#trASSRResearchServices"+id+" select#textASSRRSSelectLanguage"+id).attr("value");

	if (research == null || research == ""
			|| researchLanguage == "none") {
		alertEmptyFields(text1);
		return;
	}

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

function aSPIAAddInternetAccessInformation(text1){
	var currentTab = getCurrentTab();
	var count = $("table#accessAndServicesTable"+currentTab+" tr[id^='trASPIAAddInternetAccessInformation']").length;

	var id = "";
	if (count > 1) {
		id +="_"+count;
	}

	var description = $("table#accessAndServicesTable"+currentTab+" tr#trASPIAAddInternetAccessInformation"+id+" input#textASDescription"+id).attr("value");
	var descriptionLanguage = $("table#accessAndServicesTable"+currentTab+" tr#trASPIAAddInternetAccessInformation"+id+" select#selectASDSelectLanguage"+id).attr("value");

	if (description == null || description == ""
			|| descriptionLanguage == "none") {
		alertEmptyFields(text1);
		return;
	}

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

function addADescriptionOfYourRestaurationLab(text1){
	var currentTab = getCurrentTab();
	var count = $("table#accessAndServicesTable"+currentTab+" tr[id^='trASTSDescriptionOfRestaurationLab']").length;

	var id = "";
	if (count > 1) {
		id +="_"+count;
	}

	var description = $("table#accessAndServicesTable"+currentTab+" tr#trASTSDescriptionOfRestaurationLab"+id+" input#textASTSDescriptionOfRestaurationLab"+id).attr("value");
	var descriptionLanguage = $("table#accessAndServicesTable"+currentTab+" tr#trASTSDescriptionOfRestaurationLab"+id+" select#selectASTSSelectLanguage"+id).attr("value");

	if (description == null || description == ""
			|| descriptionLanguage == "none") {
		alertEmptyFields(text1);
		return;
	}

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

function aSAddADescriptionOfYourReproductionService(text1){
	var currentTab = getCurrentTab();
	var count = $("table#accessAndServicesTable"+currentTab+" tr[id^='trASTSDescriptionOfReproductionService']").length;

	var id = "";
	if (count > 1) {
		id +="_"+count;
	}

	var description = $("table#accessAndServicesTable"+currentTab+" tr#trASTSDescriptionOfReproductionService"+id+" input#textASTSDescriptionOfReproductionService"+id).attr("value");
	var descriptionLanguage = $("table#accessAndServicesTable"+currentTab+" tr#trASTSDescriptionOfReproductionService"+id+" select#selectASTSRSSelectLanguage"+id).attr("value");

	if (description == null || description == ""
			|| descriptionLanguage == "none") {
		alertEmptyFields(text1);
		return;
	}

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

function aSReSeAddExhibition(text1){
	var currentTab = getCurrentTab();
	var count = $("table#accessAndServicesTable"+currentTab+" tr[id^='trASReSeExhibition']").length;

	var id = "";
	if (count > 1) {
		id +="_"+count;
	}

	var exhibition = $("table#accessAndServicesTable"+currentTab+" tr#trASReSeExhibition"+id+" input#textASReSeExhibition"+id).attr("value");
	var exhibitionLanguage = $("table#accessAndServicesTable"+currentTab+" tr#trASReSeExhibition"+id+" select#selectASReSeExhibitionSelectLanguage"+id).attr("value");
	var web = $("table#accessAndServicesTable"+currentTab+" tr#tr2ASReSeExhibition"+id+" input#textASReSeWebpage"+id).attr("value");
	var webLink = $("table#accessAndServicesTable"+currentTab+" tr#tr2ASReSeExhibition"+id+" input#textASReSeWebpageLinkTitle"+id).attr("value");

	if (exhibition == null || exhibition == ""
			|| exhibitionLanguage == "none"
			|| web == null || web == ""
			|| webLink == null || webLink == "") {
		alertEmptyFields(text1);
		return;
	}

	var target1 = "trASReSeExhibition_"+(count+1);
	var target2 = "tr2ASReSeExhibition_"+(count+1);
	var lastId = "table#accessAndServicesTable"+currentTab+" tr#tr2ASReSeExhibition";
	if(count>1){
		lastId+="_"+(count);
	}
	var tr2HTML = "<tr id=\""+target1+"\">";
	tr2HTML += $("table#accessAndServicesTable"+currentTab+" tr#trASReSeExhibition").clone().html();
	tr2HTML += "</tr>\n";
	tr2HTML += "<tr id=\""+target2+"\">"+$("table#accessAndServicesTable"+currentTab+" tr#tr2ASReSeExhibition").clone().html();
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

function aSReSeToursAndSessions(text1){
	var currentTab = getCurrentTab();
	var count = $("table#accessAndServicesTable"+currentTab+" tr[id^='trASReSeToursAndSessions']").length;

	var id = "";
	if (count > 1) {
		id +="_"+count;
	}

	var tours = $("table#accessAndServicesTable"+currentTab+" tr#trASReSeToursAndSessions"+id+" input#textASReSeToursAndSessions"+id).attr("value");
	var toursLanguage = $("table#accessAndServicesTable"+currentTab+" tr#trASReSeToursAndSessions"+id+" select#selectASReSeToursAndSessionsSelectLanguage"+id).attr("value");
	var web = $("table#accessAndServicesTable"+currentTab+" tr#tr2ASReSeToursAndSessions"+id+" input#textASReSeTSWebpage"+id).attr("value");
	var webLink = $("table#accessAndServicesTable"+currentTab+" tr#tr2ASReSeToursAndSessions"+id+" input#textASReSeWebpageTSLinkTitle"+id).attr("value");

	if (tours == null || tours == ""
			|| toursLanguage == "none"
			|| web == null || web == ""
			|| webLink == null || webLink == "") {
		alertEmptyFields(text1);
		return;
	}

	var target1 = "trASReSeToursAndSessions_"+(count+1);
	var target2 = "tr2ASReSeToursAndSessions_"+(count+1);
	var lastId = "table#accessAndServicesTable"+currentTab+" tr#tr2ASReSeToursAndSessions";
	if(count>1){
		lastId+="_"+(count);
	}
	var tr2HTML = "<tr id=\""+target1+"\">";
	tr2HTML += $("table#accessAndServicesTable"+currentTab+" tr#trASReSeToursAndSessions").clone().html();
	tr2HTML += "</tr>\n";
	tr2HTML += "<tr id=\""+target2+"\">"+$("table#accessAndServicesTable"+currentTab+" tr#tr2ASReSeToursAndSessions").clone().html();
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

function aSAddServices(text1){
	var currentTab = getCurrentTab();
	var count = $("table#accessAndServicesTable"+currentTab+" tr[id^='trASReSeOtherServices']").length;

	var id = "";
	if (count > 1) {
		id +="_"+count;
	}

	var other = $("table#accessAndServicesTable"+currentTab+" tr#trASReSeOtherServices"+id+" input#textASReSeOtherServices"+id).attr("value");
	var otherLanguage = $("table#accessAndServicesTable"+currentTab+" tr#trASReSeOtherServices"+id+" select#selectASReSeOtherServicesSelectLanguage"+id).attr("value");
	var web = $("table#accessAndServicesTable"+currentTab+" tr#tr2ASReSeOtherServices"+id+" input#textASReSeOSWebpage"+id).attr("value");
	var webLink = $("table#accessAndServicesTable"+currentTab+" tr#tr2ASReSeOtherServices"+id+" input#textASReSeWebpageOSLinkTitle"+id).attr("value");

	if (other == null || other == ""
			|| otherLanguage == "none"
			|| web == null || web == ""
			|| webLink == null || webLink == "") {
		alertEmptyFields(text1);
		return;
	}

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

function descriptionAddHistoryDescription(text1){
	var currentTab = getCurrentTab();
	// trAddHistory
	var count = $("table#descriptionTable"+currentTab+" tr[id^='trRepositoryHistory']").length;

	var id = "";
	if (count > 1) {
		id +="_"+count;
	}

	var history = $("table#descriptionTable"+currentTab+" tr#trRepositoryHistory"+id+" textarea#textRepositoryHistory"+id).attr("value");
	var historyLanguage = $("table#descriptionTable"+currentTab+" tr#trRepositoryHistory"+id+" select#selectLanguageRepositoryHistory"+id).attr("value");

	if (history == null || history == ""
			|| historyLanguage == "none") {
		alertEmptyFields(text1);
		return;
	}

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

function descriptionAddFoundationInformation(text1){
	var currentTab = getCurrentTab();
	//trRuleOfRepositoryFoundation
	var count = $("table#descriptionTable"+currentTab+" tr[id^='trRuleOfRepositoryFoundation']").length;

	var id = "";
	if (count > 1) {
		id +="_"+count;
	}

	var foundation = $("table#descriptionTable"+currentTab+" tr#trRuleOfRepositoryFoundation"+id+" input#textRuleOfRepositoryFoundation"+id).attr("value");
	var foundationLanguage = $("table#descriptionTable"+currentTab+" tr#trRuleOfRepositoryFoundation"+id+" select#selectLanguageRuleOfRepositoryFoundation"+id).attr("value");

	if (foundation == null || foundation == ""
			|| foundationLanguage == "none") {
		alertEmptyFields(text1);
		return;
	}

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

function descriptionAddSuppressionInformation(text1){
	var currentTab = getCurrentTab();
	//trDescriptionAddSuppressionInformation
	var count = $("table#descriptionTable"+currentTab+" tr[id^='trDescriptionAddSuppressionInformation']").length;

	var id = "";
	if (count > 1) {
		id +="_"+count;
	}

	var suppress = $("table#descriptionTable"+currentTab+" tr#trDescriptionAddSuppressionInformation"+id+" input#textRuleOfRepositorySuppression"+id).attr("value");
	var suppressLanguage = $("table#descriptionTable"+currentTab+" tr#trDescriptionAddSuppressionInformation"+id+" select#selectLanguageRuleOfRepositorySuppression"+id).attr("value");

	if (suppress == null || suppress == ""
			|| suppressLanguage == "none") {
		alertEmptyFields(text1);
		return;
	}

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

function descriptionAddAdministrationUnits(text1){
	// trDescriptionAddAdministrationUnits
	var currentTab = getCurrentTab();
	var count = $("table#descriptionTable"+currentTab+" tr[id^='trDescriptionAddAdministrationUnits']").length;

	var id = "";
	if (count > 1) {
		id +="_"+count;
	}

	var unit = $("table#descriptionTable"+currentTab+" tr#trDescriptionAddAdministrationUnits"+id+" textarea#textUnitOfAdministrativeStructure"+id).attr("value");
	var unitLanguage = $("table#descriptionTable"+currentTab+" tr#trDescriptionAddAdministrationUnits"+id+" select#selectLanguageUnitOfAdministrativeStructure"+id).attr("value");

	if (unit == null || unit == ""
			|| unitLanguage == "none") {
		alertEmptyFields(text1);
		return;
	}

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

function descriptionAddBuildingDescription(text1){
	// trBuildingDescription
	var currentTab = getCurrentTab();
	var count = $("table#descriptionTable"+currentTab+" tr[id^='trBuildingDescription']").length;

	var id = "";
	if (count > 1) {
		id +="_"+count;
	}

	var building = $("table#descriptionTable"+currentTab+" tr#trBuildingDescription"+id+" textarea#textBuilding"+id).attr("value");
	var buildingLanguage = $("table#descriptionTable"+currentTab+" tr#trBuildingDescription"+id+" select#selectLanguageBuilding"+id).attr("value");

	if (building == null || building == ""
			|| buildingLanguage == "none") {
		alertEmptyFields(text1);
		return;
	}

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

function descriptionAddAnotherArchivalDescription(text1) {
	// trArchivalAndOtherHoldings
	var currentTab = getCurrentTab();
	var count = $("table#descriptionTable"+currentTab+" tr[id^='trArchivalAndOtherHoldings']").length;

	var id = "";
	if (count > 1) {
		id +="_"+count;
	}

	var archival = $("table#descriptionTable"+currentTab+" tr#trArchivalAndOtherHoldings"+id+" textarea#textArchivalAndOtherHoldings"+id).attr("value");
	var archivalLanguage = $("table#descriptionTable"+currentTab+" tr#trArchivalAndOtherHoldings"+id+" select#selectLanguageArchivalAndOtherHoldings"+id).attr("value");

	if (archival == null || archival == ""
			|| archivalLanguage == "none") {
		alertEmptyFields(text1);
		return;
	}

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

function controlAddFurtherLangsAnsScripts(text1){
	var count = $("table#controlTable tr[id^='trControlAddFurtherLangsAnsScriptsOne']").length;

	var id = "";
	if (count > 1) {
		id +="_"+count;
	}
	var language = $("table#controlTable tr#trControlAddFurtherLangsAnsScriptsOne"+id+" select#selectDescriptionLanguage"+id).attr("value");
	var script = $("table#controlTable tr#trControlAddFurtherLangsAnsScriptsTwo"+id+" select#selectDescriptionScript"+id).attr("value");

	if (language == "none" || script == "none") {
			alertEmptyFields(text1);
			return;
	}

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
	$("table#controlTable tr#"+target1+" td#tdDescriptionLanguage").find("span").remove();
	$("table#controlTable tr#"+target2+" td#tdDescriptionScript").find("span").remove();
}

function addContactAbbreviation(text1){
	var count = $("table#controlTable tr[id^='trContactAbbreviationOne']").length;

	var id = "";
	if (count > 1) {
		id +="_"+count;
	}
	var abbrev = $("table#controlTable tr#trContactAbbreviationOne"+id+" input#textContactAbbreviation"+id).attr("value");
	var full = $("table#controlTable tr#trContactAbbreviationTwo"+id+" input#textContactFullName"+id).attr("value");

	if (abbrev == null || abbrev == ""
			|| full == null || full == "") {
			alertEmptyFields(text1);
			return;
	}

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

function relationAddNewResourceRelation(text1){
	var counter = $("table[id^='resourceRelationTable_']").length;

	var web = $("table#resourceRelationTable_"+counter+" input#textWebsiteOfResource").attr("value");
	var title = $("table#resourceRelationTable_"+counter+" input#textTitleOfRelatedMaterial").attr("value");

	if (web == null || web == ""
			|| title == null || title == "") {
		alertEmptyFields(text1);
		return;
	}

	var clone = $("table[id^='resourceRelationTable_"+counter+"']").clone();
	clone = "<table id='"+("resourceRelationTable_"+(counter+1))+"'>"+clone.html()+"</table>";
	$("table[id^='resourceRelationTable_"+counter+"']").after(clone);
	// Reset parametters.
	$("table#resourceRelationTable_"+(counter+1)+" input[type='text']").each(function(){
		$(this).val(""); // Clean all input_text.
	});
}

function relationAddNewInstitutionRelation(text1){
	var counter = $("table[id^='institutionRelationTable_']").length;

	var web = $("table#institutionRelationTable_"+counter+" input#textWebsiteOfDescription").attr("value");
	var typeOfTheRelation = $("table#institutionRelationTable_"+counter+" select#selectTypeOftheRelation").attr("value");
	var title = $("table#institutionRelationTable_"+counter+" input#textTitleOfRelatedInstitution").attr("value");

	if (web == null || web == "" || typeOfTheRelation == "none"
			|| title == null || title == "") {
		alertEmptyFields(text1);
		return;
	}

	var clone = $("table[id^='institutionRelationTable_"+counter+"']").clone();
	clone = "<table id='"+("institutionRelationTable_"+(counter+1))+"'>"+clone.html()+"</table>";
	$("table[id^='institutionRelationTable_"+counter+"']").after(clone);
	// Reset parametters.
	$("table#institutionRelationTable_"+(counter+1)+" input[type='text']").each(function(){
		$(this).val(""); // Clean all input_text.
	});
}

function alertEmptyFields(text1) {
	alert(text1);
}

function alertFillFieldsBeforeChangeTab(text1) {
	alert(text1);
}

// Copy content functions.
function personResponsibleForDescriptionChanged(){
	$("#textPesonResponsible").attr("value", $("#textYIPersonInstitutionResposibleForTheDescription").val());
}

function idOfInstitutionChanged(){
	$("#textIdentityIdentifierOfTheInstitution").attr("value", $("#textYIIdentifierOfTheInstitution").val());
}

function nameOfInstitutionChanged(){
	$("#textNameOfTheInstitution").attr("value", $("#textYINameOfTheInstitution").val());
}

function nameOfInstitutionLanguageChanged(){
	$("#noti_languageList").attr("value", $("#selectYINOTISelectLanguage").val());
}

function parallelNameOfInstitutionChanged(){
	$("#textParallelNameOfTheInstitution").attr("value", $("#textYIParallelNameOfTheInstitution").val());
}

function parallelNameOfInstitutionLanguageChanged(){
	$("#pnoti_languageList").attr("value", $("#selectYIPNOTISelectLanguage").val());
}

function streetOfInstitutionChanged(){
	$("table#contactTable_1 table#contactTableVisitorsAddress_1 #textContactStreetOfTheInstitution").attr("value", $("table#yiTableVisitorsAddress_1 #textYIStreet").val());
}

function streetOfInstitutionLanguageChanged(){
	$("table#contactTable_1 table#contactTableVisitorsAddress_1 #selectLanguageVisitorAddress").attr("value", $("table#yiTableVisitorsAddress_1 #selectYIVASelectLanguage").val());
}

function cityOfInstitutionChanged(){
	$("table#contactTable_1 table#contactTableVisitorsAddress_1 #textContactCityOfTheInstitution").attr("value", $("table#yiTableVisitorsAddress_1 #textYICity").val());
}

function countryOfInstitutionChanged(){
	$("table#contactTable_1 table#contactTableVisitorsAddress_1 #textContactCountryOfTheInstitution").attr("value", $("table#yiTableVisitorsAddress_1 #textYICountry").val());
}

function latitudeOfInstitutionChanged(){
	$("table#contactTable_1 table#contactTableVisitorsAddress_1 #textContactLatitudeOfTheInstitution").attr("value", $("table#yiTableVisitorsAddress_1 #textYILatitude").val());
}

function longitudeOfInstitutionChanged(){
	$("table#contactTable_1 table#contactTableVisitorsAddress_1 #textContactLongitudeOfTheInstitution").attr("value", $("table#yiTableVisitorsAddress_1 #textYILongitude").val());
}

function continentOfInstitutionChanged(){
	$("table#contactTable_1 #selectContinentOfTheInstitution").attr("value", $("table#yiTableOthers #selectYIContinent").val());
}

function telephoneOfInstitutionChanged(){
	$("table#contactTable_1 tr#trTelephoneOfTheInstitution #textContactTelephoneOfTheInstitution").attr("value", $("table#yiTableOthers #textYITelephone").val());
}

function emailOfInstitutionChanged(){
	$("table#contactTable_1 tr#trEmailOfTheInstitution #textContactEmailOfTheInstitution").attr("value", $("table#yiTableOthers #textYIEmailAddress").val());
}

function emailOfInstitutionLinkChanged(){
	$("table#contactTable_1 tr#trEmailOfTheInstitution #textContactLinkTitleForEmailOfTheInstitution").attr("value", $("table#yiTableOthers #textYIEmailLinkTitle").val());
}

function webOfInstitutionChanged(){
	$("table#contactTable_1 tr#trWebOfTheInstitution #textContactWebOfTheInstitution").attr("value", $("table#yiTableOthers #textYIWebpage").val());
}

function openingHoursOfInstitutionChanged(){
	$("table#accessAndServicesTable_1 tr#trASOpeningTimes #textOpeningTimes").attr("value", $("table#yiTableOthers #textYIOpeningTimes").val());
}

function closingHoursOfInstitutionChanged(){
	$("table#accessAndServicesTable_1 tr#trASClosingDates #textClosingDates").attr("value", $("table#yiTableOthers #yourInstitutionClosingDates").val());
}

function accessibleToThePublicChanged() {
	$("table#accessAndServicesTable_1 #selectASAccesibleToThePublic").attr("value", $("table#yiTableOthers #selectAccessibleToThePublic").val());
}

function futherAccessInformationChanged() {
	$("table#accessAndServicesTable_1 #textASAccessRestrictions").attr("value", $("table#yiTableOthers #futherAccessInformation").val());
}

function facilitiesForDisabledPeopleAvailableChanged() {
	$("table#accessAndServicesTable_1 #selectASFacilitiesForDisabledPeopleAvailable").attr("value", $("table#yiTableOthers #selectFacilitiesForDisabledPeopleAvailable").val());
}

function futherInformationOnExistingFacilitiesChanged() {
	$("table#accessAndServicesTable_1 #textASAccessibility").attr("value", $("table#yiTableOthers #futherInformationOnExistingFacilities").val());
}

function webOfInstitutionLinkChanged(){
	$("table#contactTable_1 tr#trWebOfTheInstitution #textContactLinkTitleForWebOfTheInstitution").attr("value", $("table#yiTableOthers #textYIWebpageLinkTitle").val());
}

function contactEmailOfInstitutionLinkChanged(){
	$("table#yiTableOthers #textYIEmailLinkTitle").attr("value", $("table#contactTable_1 tr#trEmailOfTheInstitution #textContactLinkTitleForEmailOfTheInstitution").val());
}

function contactWebOfInstitutionLinkChanged(){
	$("table#yiTableOthers #textYIWebpageLinkTitle").attr("value", $("table#contactTable_1 tr#trWebOfTheInstitution #textContactLinkTitleForWebOfTheInstitution").val());
}

function aSOpeningHoursOfInstitutionChanged(){
	$("table#yiTableOthers #textYIOpeningTimes").attr("value", $("table#accessAndServicesTable_1 tr#trASOpeningTimes #textOpeningTimes").val());
}

function aSClosingHoursOfInstitutionChanged(){
	$("table#yiTableOthers #yourInstitutionClosingDates").attr("value", $("table#accessAndServicesTable_1 tr#trASClosingDates #textClosingDates").val());
}

function aSAccessibleToThePublicChanged() {
	$("table#yiTableOthers #selectAccessibleToThePublic").attr("value", $("table#accessAndServicesTable_1 #selectASAccesibleToThePublic").val());
}

function aSFutherAccessInformationChanged() {
	$("table#yiTableOthers #futherAccessInformation").attr("value", $("table#accessAndServicesTable_1 #textASAccessRestrictions").val());
}

function aSFacilitiesForDisabledPeopleAvailableChanged() {
	$("table#yiTableOthers #selectFacilitiesForDisabledPeopleAvailable").attr("value", $().val("table#accessAndServicesTable_1 #selectASFacilitiesForDisabledPeopleAvailable"));
}

function aSFutherInformationOnExistingFacilitiesChanged() {
	$("table#yiTableOthers #futherInformationOnExistingFacilities").attr("value", $("table#accessAndServicesTable_1 #textASAccessibility").val());
}

function controlPersonResponsibleForDescriptionChanged(){
	$("#textYIPersonInstitutionResposibleForTheDescription").attr("value", $("#textPesonResponsible").val());
}

function linkToYourHolndingsGuideChanged(){
	$("table#resourceRelationTable_1 #textWebsiteOfResource").attr("value", $("#textReferencetoyourinstitutionsholdingsguide").val());
}

function linkToYourHolndingsGuideTitleChanged(){
	$("table#resourceRelationTable_1 #textTitleOfRelatedMaterial").attr("value", $("#textYIHoldingsGuideLinkTitle").val());
}
