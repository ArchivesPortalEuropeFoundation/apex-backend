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
			});
		}
	}
}

function deleteChecks() {
	$('.fieldRequired').remove();
}

var clickYourInstitutionAction = function(){
	// Delete old checks
	deleteChecks();

	// Mandatory elements
	var yiMandatoryElements = new Array("textYIInstitutionCountryCode", "textYIIdentifierOfTheInstitution",
	                           "textYINameOfTheInstitution", "selectYINOTISelectLanguage");

	var jsonData = "{";
	
		//content before institutions part
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

		// Visitors addess.
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
			jsonData += "{'"+visitorsAddress[j]+"':";
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
			});
			if(yiMEVisitorsAddress.length>0){
				validationArray.push(visitorsAddress[j],yiMEVisitorsAddress);
			}
			jsonData += "}";
		}
		
		jsonData += "]";

		// Postal addess.
		var postalAddress = new Array();
		$("table[id^='yiTablePostalAddress_']").each(function(){
			var id = $(this).attr("id");
			if(id.indexOf("#")>-1){
				id = id.substring(id.indexOf("#"));
			}
			visitorsAddress.push(id);
		});
		jsonData += ",'postalAddress':[";
		for(var j=0; j<postalAddress.length; j++) {
			var yiMEPostalAddress = new Array("selectYIPASelectLanguage", "textYIPAStreet",
					"textYIPACity");
			
			if(jsonData.substring(jsonData.length-1)!='['){
				jsonData += ",";
			}
			jsonData += "{'"+postalAddress[j]+"':";
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
			});
			if(yiMEPostalAddress.length>0){
				validationArray.push(postalAddress[j],yiMEPostalAddress);
			}
			jsonData += "}";
		}
		
		jsonData += "]";

		var yiMERepositories = new Array("textYIOpeningTimes");

		if(jsonData.substring(jsonData.length-1)!='['){
			jsonData += ",";
		}

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

//	jsonData += "]}";
	jsonData += "}";
//	alert(jsonData);

	for (var i = 0; i < yiMandatoryElements.length; i++) {
		var element = document.getElementById(yiMandatoryElements[i].toString());
		var subelement = document.createElement('p');
		
		subelement.appendChild(document.createTextNode('Field required'));
		subelement.id = yiMandatoryElements[i].toString() + '_required';
		subelement.className="fieldRequired";
		element.parentNode.insertBefore(subelement, element.nextSibling);
	}

	for (var i = 0; i < validationArray.length; i = (i + 2)) {
		var array = validationArray[i+1];

		for (var j = 0; j < array.length; j++) {
			var subelement = document.createElement('p');

			subelement.appendChild(document.createTextNode('Field required'));
			subelement.id = array[j].toString() + '_required';
			subelement.className="fieldRequired";

			$('#' + validationArray[i].toString()).find('#' + array[j].toString()).after(subelement);
		}

	}
};

var clickIdentityAction = function(){
	var jsonData = "{";
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
	jsonData += "]}";
//	alert(jsonData);
};

var clickContactAction = function(){
	var jsonData = "{";
		//content from texts
		$("table#contactTable input[type='text']").each(function(){
			if(jsonData.length>1){
				jsonData += ",";
			}
			jsonData += "'"+$(this).attr("id")+"' : '"+$(this).attr("value")+"'";
		});
		//content from selects
		$("table#contactTable select").each(function(){
			if(jsonData.charAt(jsonData.length-1)!=':'){
				jsonData += ",";
			}
			jsonData += "'"+$(this).attr("id")+"' : '"+$(this).attr("value")+"'";
		});
	jsonData += "]}";
//	alert(jsonData);
};

var clickAccessAndServicesAction = function(){
	// Delete old checks
	deleteChecks();

	// Mandatory elements
	var aasMandatoryElements = new Array("textASSRWorkPlaces");

	var jsonData = "{";
		//content from texts
		$("table#accessAndServicesTable input[type='text']").each(function(){
			if(jsonData.length>1){
				jsonData += ",";
			}
			jsonData += "'"+$(this).attr("id")+"' : '"+$(this).attr("value")+"'";

			// Check fill mandatory fields.
			if ($(this).attr("value") != '') {
				var position = aasMandatoryElements.indexOf($(this).attr("id"));
				aasMandatoryElements.splice(position, 1);
			}
		});
		//content from selects
		$("table#accessAndServicesTable select").each(function(){
			if(jsonData.charAt(jsonData.length-1)!=':'){
				jsonData += ",";
			}
			jsonData += "'"+$(this).attr("id")+"' : '"+$(this).attr("value")+"'";
		});
	jsonData += "]}";
//	alert(jsonData);

	for (var i = 0; i < aasMandatoryElements.length; i++) {
		var element = document.getElementById(aasMandatoryElements[i].toString());
		var subelement = document.createElement('p');
		
		subelement.appendChild(document.createTextNode('Field required'));
		subelement.id = aasMandatoryElements[i].toString() + '_required';
		subelement.className="fieldRequired";
		element.parentNode.insertBefore(subelement, element.nextSibling);
	}
};

var clickDescriptionAction = function(){
	var jsonData = "{";
		//content from texts
		$("table#descriptionTable input[type='text']").each(function(){
			if(jsonData.length>1){
				jsonData += ",";
			}
			jsonData += "'"+$(this).attr("id")+"' : '"+$(this).attr("value")+"'";
		});
		//content from selects
		$("table#descriptionTable select").each(function(){
			if(jsonData.charAt(jsonData.length-1)!=':'){
				jsonData += ",";
			}
			jsonData += "'"+$(this).attr("id")+"' : '"+$(this).attr("value")+"'";
		});
	jsonData += "]}";
//	alert(jsonData);
};

var clickControlAction = function(){
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
			jsonData += "'"+$(this).attr("id")+"' : '"+$(this).attr("value")+"'";
		});
		//content from selects
		$("table#controlTable select").each(function(){
			if(jsonData.charAt(jsonData.length-1)!=':'){
				jsonData += ",";
			}
			jsonData += "'"+$(this).attr("id")+"' : '"+$(this).attr("value")+"'";

			// Check fill mandatory fields.
			if ($(this).attr("value") != 'none') {
				var position = controlMandatoryElements.indexOf($(this).attr("id"));
				controlMandatoryElements.splice(position, 1);
			}
		});
	jsonData += "]}";
//	alert(jsonData);

	for (var i = 0; i < controlMandatoryElements.length; i++) {
		var element = document.getElementById(controlMandatoryElements[i].toString());
		var subelement = document.createElement('p');
		
		subelement.appendChild(document.createTextNode('Field required'));
		subelement.id = controlMandatoryElements[i].toString() + '_required';
		subelement.className="fieldRequired";
		element.parentNode.insertBefore(subelement, element.nextSibling);
	}
};

var clickRelationsAction = function(){
	var jsonData = "{";
		//content from texts
		$("table#relationsTable input[type='text']").each(function(){
			if(jsonData.length>1){
				jsonData += ",";
			}
			jsonData += "'"+$(this).attr("id")+"' : '"+$(this).attr("value")+"'";
		});
		//content from selects
		$("table#relationsTable select").each(function(){
			if(jsonData.charAt(jsonData.length-1)!=':'){
				jsonData += ",";
			}
			jsonData += "'"+$(this).attr("id")+"' : '"+$(this).attr("value")+"'";
		});
	jsonData += "]}";
//	alert(jsonData);
};
function addFurtherIds(text1){
	$("input#buttonAddFutherIds").parent().parent().before("<tr><td colspan=\"2\"></td><td><label for=\"otherRepositorId_"+($("input[id^='otherRepositorId']").length)+"\"> "+text1+":</label></td><td><input type=\"text\" id=\"otherRepositorId_"+($("input[id^='otherRepositorId']").length)+"\" /></td></tr>");
}
function addRepositories(text1,text2){
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
	//fill all button events cloned
	//contactTable
	var firstId = "table#contactTable";
	$(firstId+"_"+(counter+1)+" input#buttonAddFurtherTelephoneOfTheInstitution").click($._data($(firstId+"_1 input#buttonAddFurtherTelephoneOfTheInstitution")[0],"events")["click"][0].handler);
	$(firstId+"_"+(counter+1)+" input#buttonAddFurtherFaxOfTheInstitution").click($._data($(firstId+"_1 input#buttonAddFurtherFaxOfTheInstitution")[0],"events")["click"][0].handler);
	//$(firstId+"_"+(counter+1)+"input#buttonAddFurtherEmailsOfTheInstitution").click($._data($(firstId+"_1 input#buttonAddFurtherEmailsOfTheInstitution")[0],"events")["click"][0].handler);
	$(firstId+"_"+(counter+1)+" input#buttonAddFurtherWebsOfTheInstitution").click($._data($(firstId+"_1 input#buttonAddFurtherWebsOfTheInstitution")[0],"events")["click"][0].handler);
	//accessAndServicesTable
	firstId = "table#accessAndServicesTable";
	$(firstId+"_"+(counter+1)+" input#buttonASAddTravellingDirections").click($._data($(firstId+"_1 input#buttonASAddTravellingDirections")[0],"events")["click"][0].handler);
	$(firstId+"_"+(counter+1)+" input#buttonAddFutherAccessInformation").click($._data($(firstId+"_1 input#buttonAddFutherAccessInformation")[0],"events")["click"][0].handler);
	$(firstId+"_"+(counter+1)+" input#buttonASAddFutherTermOfUse").click($._data($(firstId+"_1 input#buttonASAddFutherTermOfUse")[0],"events")["click"][0].handler);
	$(firstId+"_"+(counter+1)+" input#buttonAddAccessibilityInformation").click($._data($(firstId+"_1 input#buttonAddAccessibilityInformation")[0],"events")["click"][0].handler);
	$(firstId+"_"+(counter+1)+" input#buttonASSRAddadescriptionofyourcomputerplaces").click($._data($(firstId+"_1 input#buttonASSRAddadescriptionofyourcomputerplaces")[0],"events")["click"][0].handler);
	$(firstId+"_"+(counter+1)+" input#buttonASSRAddReadersTicket").click($._data($(firstId+"_1 input#buttonASSRAddReadersTicket")[0],"events")["click"][0].handler);
	$(firstId+"_"+(counter+1)+" input#buttonASSRAddFurtherOrderInformation").click($._data($(firstId+"_1 input#buttonASSRAddFurtherOrderInformation")[0],"events")["click"][0].handler);
	$(firstId+"_"+(counter+1)+" input#buttonASAddResearchServices").click($._data($(firstId+"_1 input#buttonASAddResearchServices")[0],"events")["click"][0].handler);
	$(firstId+"_"+(counter+1)+" input#buttonAddADescriptionOfYourRestaurationLab").click($._data($(firstId+"_1 input#buttonAddADescriptionOfYourRestaurationLab")[0],"events")["click"][0].handler);
	$(firstId+"_"+(counter+1)+" input#buttonASAddADescriptionOfYourReproductionService").click($._data($(firstId+" input#buttonASAddADescriptionOfYourReproductionService")[0],"events")["click"][0].handler);
	$(firstId+"_"+(counter+1)+" input#buttonASReSeAddExhibition").click($._data($(firstId+"_1 input#buttonASReSeAddExhibition")[0],"events")["click"][0].handler);
	$(firstId+"_"+(counter+1)+" input#buttonASReSeToursAndSessions").click($._data($(firstId+"_1 input#buttonASReSeToursAndSessions")[0],"events")["click"][0].handler);
	$(firstId+"_"+(counter+1)+" input#buttonASAddServices").click($._data($(firstId+"_1 input#buttonASAddServices")[0],"events")["click"][0].handler);
	
	//current tab
	$("table#yourInstitutionTable_"+(counter+1)+" input#buttonYourInstitutionTabSave").click(clickYourInstitutionAction);
}
function addFurtherEmailsOfTheInstitution(){
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
	var count = $("table#contactTable"+currentTab+" tr[id^='trEmailOfTheInstitution']").length;
	var newId = "trEmailOfTheInstitution"+currentTab;
	var trHtml = "<tr id=\""+newId+"\">"+$("table#contactTable tr#trEmailOfTheInstitution").clone().html()+"</tr>";
	var lastId = "table#contactTable"+currentTab+" tr#trEmailOfTheInstitution";
	
	$(lastId).after(trHtml);
	//update last content
	$("table#contactTable"+currentTab+" tr#"+newId+" tr#trEmailOfTheInstitution").attr("id","trEmailOfTheInstitution_"+(count+1));
	$("table#contactTable"+currentTab+" tr#"+newId+" label[for='textContactEmailOfTheInstitution']").attr("for","textContactEmailOfTheInstitution_"+(count+1));
	$("table#contactTable"+currentTab+" tr#"+newId+" input#textContactEmailOfTheInstitution").attr("id","textContactEmailOfTheInstitution_"+(count+1));
	$("table#contactTable"+currentTab+" tr#"+newId+" label[for='textContactLinkTitleForEmailOfTheInstitution']").attr("for","textContactLinkTitleForEmailOfTheInstitution_"+(count+1));
	$("table#contactTable"+currentTab+" tr#"+newId+" input#textContactLinkTitleForEmailOfTheInstitution").attr("id","textContactLinkTitleForEmailOfTheInstitution_"+(count+1));
}
function addAnotherFormOfTheAuthorizedName(){
	var count = $("table#identityTable tr[id^='trNameOfTheInstitution']").length;
	var newId = "trNameOfTheInstitution_"+(count+1);
	var trHtml = "<tr id=\""+newId+"\">"+$("table#identityTable tr[id^='trNameOfTheInstitution']").clone().html()+"</tr>";
	var lastId = "table#identityTable tr#trNameOfTheInstitution";
	if(count>1){
		lastId+="_"+(count);
	}
	$(lastId).after(trHtml);
	//update last content
	$("table#identityTable tr#"+newId+" input#textIdentityIdUsedInAPE").attr("id","textIdentityIdUsedInAPE_"+(count+1));
	$("table#identityTable tr#"+newId+" td#tdNameOfTheInstitution").attr("id","tdNameOfTheInstitution_"+(count+1));
	$("table#identityTable tr#"+newId+" input#textIdentityIdUsedInAPE_"+(count+1)).attr("value","");
	$("table#identityTable tr#"+newId+" input#textIdentityIdUsedInAPE_"+(count+1)).removeAttr("disabled");
	$("table#identityTable tr#"+newId+" td#tdNameOfTheInstitutionLanguage").attr("id","tdNameOfTheInstitutionLanguage_"+(count+1));
	$("table#identityTable tr#"+newId+" label[for='noti_languageList']").attr("for","noti_languageList_"+(count+1));
	$("table#identityTable tr#"+newId+" select#noti_languageList").attr("id","noti_languageList_"+(count+1));
}
function addParallelNameOfTheInstitution(){
	var count = $("table#identityTable tr[id^='trParallelNameOfTheInstitution']").length;
	var newId = "trParallelNameOfTheInstitution_"+(count+1);
	var trHtml = "<tr id=\""+newId+"\">"+$("table#identityTable tr[id^='trParallelNameOfTheInstitution']").clone().html()+"</tr>";
	var lastId = "table#identityTable tr#trParallelNameOfTheInstitution";
	if(count>1){
		lastId+="_"+(count);
	}
	$(lastId).after(trHtml);
	//update last content
	$("table#identityTable tr#"+newId+" input#textParallelNameOfTheInstitution").attr("id","textParallelNameOfTheInstitution_"+(count+1));
	$("table#identityTable tr#"+newId+" td#tdNameOfTheInstitutionLanguage").attr("id","tdNameOfTheInstitutionLanguage_"+(count+1));
	$("table#identityTable tr#"+newId+" label[for='textParallelNameOfTheInstitution']").attr("for","textParallelNameOfTheInstitution_"+(count+1));
	$("table#identityTable tr#"+newId+" input#textParallelNameOfTheInstitution").attr("id","textParallelNameOfTheInstitution_"+(count+1));
	$("table#identityTable tr#"+newId+" input#textParallelNameOfTheInstitution_"+(count+1)).attr("value","");
	$("table#identityTable tr#"+newId+" td#tdNameOfTheInstitutionLanguage").attr("id","tdNameOfTheInstitutionLanguage_"+(count+1));
	$("table#identityTable tr#"+newId+" label[for='pnoti_languageList']").attr("for","pnoti_languageList_"+(count+1));
	$("table#identityTable tr#"+newId+" select#pnoti_languageList").attr("id","pnoti_languageList_"+(count+1));
}
function addMoreDates(){
	var count = $("table#identityTable tr[id^='trDatesWhenThisNameWasUsed']").length;
	var newId = "trDatesWhenThisNameWasUsed_"+(count+1);
	var trHtml = "<tr id=\""+newId+"\">"+$("table#identityTable tr[id^='trDatesWhenThisNameWasUsed']").clone().html()+"</tr>";
	var lastId = "table#identityTable tr#trDatesWhenThisNameWasUsed";
	if(count>1){
		lastId+="_"+(count);
	}
	$(lastId).after(trHtml);
	//update last content
	$("#"+newId+" label[for='textDatesWhenThisNameWasUsedFrom']").attr("for","textDatesWhenThisNameWasUsedFrom_"+(count+1));
	$("#"+newId+" label[for='textDatesWhenThisNameWasUsedTo']").attr("for","textDatesWhenThisNameWasUsedTo_"+(count+1));
	$("#"+newId+" input#textDatesWhenThisNameWasUsedFrom").attr("id","textDatesWhenThisNameWasUsedFrom_"+(count+1));
	$("#"+newId+" input#textDatesWhenThisNameWasUsedTo").attr("id","textDatesWhenThisNameWasUsedTo_"+(count+1));
	$("#"+newId+" tr#trDatesWhenThisNameWasUsed").attr("id","trDatesWhenThisNameWasUsed_"+(count+1));
}
function addMoreAnotherFormerlyUsedName(){
	var count = $("table#identityTable tr[id^='trTextFormerlyUsedName']").length;
	var newId = "trTextFormerlyUsedName_"+(count+1);
	var trHtml = "<tr id=\""+newId+"\">"+$("table#identityTable tr[id^='trTextFormerlyUsedName']").clone().html()+"</tr>";
	var lastId = "table#identityTable tr#trTextFormerlyUsedName";
	if(count>1){
		lastId+="_"+(count);
	}
	$(lastId).after(trHtml);
	//update last content
	$("#"+newId+" tr#trTextFormerlyUsedName").attr("id","trTextFormerlyUsedName_"+(count+1));
	$("#"+newId+" label[for='textFormerlyUsedName']").attr("for","textFormerlyUsedName_"+(count+1));
	$("#"+newId+" input#textFormerlyUsedName").attr("id","textFormerlyUsedName_"+(count+1));
	$("#"+newId+" label[for='tfun_languageList']").attr("for","tfun_languageList_"+(count+1));
	$("#"+newId+" select#tfun_languageList").attr("id","tfun_languageList_"+(count+1));
}
function addFurtherTelephoneOfTheInstitution(){
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
	var count = $("table#contactTable"+currentTab+" tr[id^='trTelephoneOfTheInstitution']").length;
	var newId = "trTelephoneOfTheInstitution_"+(count+1);
	var trHtml = "<tr id=\""+newId+"\">"+$("table#contactTable tr[id^='trTelephoneOfTheInstitution']").clone().html()+"</tr>";
	var lastId = "table#contactTable"+currentTab+" tr#trTelephoneOfTheInstitution";
	if(count>1){
		lastId+="_"+(count);
	}
	$(lastId).after(trHtml);
	//delete cloned button
	$("table#contactTable"+currentTab+" tr#"+newId+" td#tdAddFurtherTelephoneOfTheInstitution").remove();
	//update last content
	$("table#contactTable"+currentTab+" tr#"+newId+" td#tdTelephoneOfTheInstitution").attr("id","tdTelephoneOfTheInstitution_"+(count+1));
	$("table#contactTable"+currentTab+" tr#"+newId+" label[for='textContactTelephoneOfTheInstitution']").attr("for","textContactTelephoneOfTheInstitution_"+(count+1));
	$("table#contactTable"+currentTab+" tr#"+newId+" input#textContactTelephoneOfTheInstitution").attr("id","textContactTelephoneOfTheInstitution_"+(count+1));
}
function addFurtherFaxOfTheInstitution(){
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
	var count = $("table#contactTable"+currentTab+" table#contactTable tr[id^='trFaxOfTheInstitution']").length;
	var newId = "trFaxOfTheInstitution_"+(count+1);
	var trHtml = "<tr id=\""+newId+"\">"+$("table#contactTable tr[id^='trFaxOfTheInstitution']").clone().html()+"</tr>";
	var lastId = "table#contactTable"+currentTab+" tr#trFaxOfTheInstitution";
	if(count>1){
		lastId+="_"+(count);
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
	var count = $("table#contactTable"+currentTab+" tr[id^='trWebOfTheInstitution']").length;
	var newId = "trWebOfTheInstitution_"+(count+1);
	var trHtml = "<tr id=\""+newId+"\">"+$("table#contactTable tr[id^='trWebOfTheInstitution']").clone().html()+"</tr>";
	var lastId = "table#contactTable"+currentTab+" tr#trWebOfTheInstitution";
	if(count>1){
		lastId+="_"+(count);
	}
	$(lastId).after(trHtml);
	//update last content
	$("table#contactTable"+currentTab+" tr#"+newId+" tr#trWebOfTheInstitution").attr("id","trWebOfTheInstitution_"+(count+1));
	$("table#contactTable"+currentTab+" tr#"+newId+" label[for='textContactWebOfTheInstitution']").attr("for","textContactWebOfTheInstitution_"+(count+1));
	$("table#contactTable"+currentTab+" tr#"+newId+" input#textContactWebOfTheInstitution").attr("id","textContactWebOfTheInstitution_"+(count+1));
	$("table#contactTable"+currentTab+" tr#"+newId+" label[for='textContactLinkTitleForWebOfTheInstitution']").attr("for","textContactLinkTitleForWebOfTheInstitution_"+(count+1));
	$("table#contactTable"+currentTab+" tr#"+newId+" input#textContactLinkTitleForWebOfTheInstitution").attr("id","textContactLinkTitleForWebOfTheInstitution_"+(count+1));
}
function aSAddTravellingDirections(){
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
	$("input#buttonASAddTravellingDirections").each(function(){$(this).remove();});
	$(lastId).after(tr2HTML);
	//update with new elements
	//put .click event to only new visible button
	$("table#accessAndServicesTable"+currentTab+" tr#"+target2+" input#buttonASAddTravellingDirections").click($._data($(lastId+" input#buttonASAddTravellingDirections")[0],"events")["click"][0].handler);
	if(count==1){
		$("table#accessAndServicesTable"+currentTab+" tr#tr2TravellingDirections input#buttonASAddTravellingDirections").remove();
	}else{
		$("table#accessAndServicesTable"+currentTab+" tr#tr2TravellingDirections_"+(count)+" input#buttonASAddTravellingDirections").remove();
	}
	//update rest of new elements
	$("table#accessAndServicesTable"+currentTab+" tr#"+target1+" label[for='textTravellingDirections']").attr("for","textTravellingDirections_"+(count+1));
	$("table#accessAndServicesTable"+currentTab+" tr#"+target1+" textarea#textTravellingDirections").attr("id","textTravellingDirections_"+(count+1));
	$("table#accessAndServicesTable"+currentTab+" tr#"+target1+" label[for='textTravelLink']").attr("for","textTravelLink_"+(count+1));
	$("table#accessAndServicesTable"+currentTab+" tr#"+target1+" input#textTravelLink").attr("id","textTravelLink_"+(count+1));
	$("table#accessAndServicesTable"+currentTab+" tr#"+target2+" label[for='selectASATDSelectLanguage']").attr("for","selectASATDSelectLanguage_"+(count+1));
	$("table#accessAndServicesTable"+currentTab+" tr#"+target2+" select#selectASATDSelectLanguage").attr("id","selectASATDSelectLanguage_"+(count+1));
}
function addFutherAccessInformation(){
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
	$("input#buttonASAddFutherTermOfUse").each(function(){$(this).remove();});
	$(lastId).after(tr2HTML);
	//update with new elements
	//put .click event to only new visible button
	$("table#accessAndServicesTable"+currentTab+" tr#"+target2+" input#buttonASAddFutherTermOfUse").click($._data($(lastId+" input#buttonASAddFutherTermOfUse")[0],"events")["click"][0].handler);
	if(count==1){
		$("table#accessAndServicesTable"+currentTab+" tr#tr2ASAddFutherTermOfUse input#buttonASAddFutherTermOfUse").remove();
	}else{
		$("table#accessAndServicesTable"+currentTab+" tr#tr2ASAddFutherTermOfUse_"+(count)+" input#buttonASAddFutherTermOfUse").remove();
	}
	//update rest of new elements
	$("table#accessAndServicesTable"+currentTab+" tr#"+target1+" label[for='textASTermOfUse']").attr("for","textASTermOfUse_"+(count+1));
	$("table#accessAndServicesTable"+currentTab+" tr#"+target1+" textarea#textASTermOfUse").attr("id","textASTermOfUse_"+(count+1));
	$("table#accessAndServicesTable"+currentTab+" tr#"+target1+" label[for='textTravelLink']").attr("for","textTravelLink_"+(count+1));
	$("table#accessAndServicesTable"+currentTab+" tr#"+target1+" input#textASTOULink").attr("id","textASTOULink_"+(count+1));
	$("table#accessAndServicesTable"+currentTab+" tr#"+target2+" label[for='textASTOULink']").attr("for","textASTOULink_"+(count+1));
	$("table#accessAndServicesTable"+currentTab+" tr#"+target2+" select#selectASAFTOUSelectLanguage").attr("id","selectASAFTOUSelectLanguage_"+(count+1));
}
function addAccessibilityInformation(){
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
function aSSRAddadescriptionofyourcomputerplaces(){
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
	var count = $("table#accessAndServicesTable"+currentTab+" tr[id^='trASSRAddadescriptionofyourcomputerplaces']").length;
	var newId = "trASSRAddadescriptionofyourcomputerplaces_"+(count+1);
	var trHtml = "<tr id=\""+newId+"\">"+$("table#accessAndServicesTable tr[id='trASSRAddadescriptionofyourcomputerplaces']").clone().html()+"</tr>";
	var lastId = "table#accessAndServicesTable"+currentTab+" tr#trASSRAddadescriptionofyourcomputerplaces";
	if(count>1){
		lastId+="_"+(count);
	}
	$(lastId).after(trHtml);
	//update last content
	$("table#accessAndServicesTable"+currentTab+" tr#"+newId+" label[for='textASSRComputerPlaces']").attr("for","textASAccessibility_"+(count+1));
	$("table#accessAndServicesTable"+currentTab+" tr#"+newId+" input#textASSRComputerPlaces").attr("id","textASAccessibility_"+(count+1));
	$("table#accessAndServicesTable"+currentTab+" tr#"+newId+" input#buttonASSRAddadescriptionofyourcomputerplaces").parent().remove();
}
function aSSRAddReadersTicket(){
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
	var count = $("table#accessAndServicesTable"+currentTab+" tr[id^='trASSRReadersTicket']").length;
	var newId = "trASSRReadersTicket_"+(count+1);
	var trHtml = "<tr id=\""+newId+"\">"+$("table#accessAndServicesTable tr[id='trASSRReadersTicket']").clone().html()+"</tr>";
	var lastId = "table#accessAndServicesTable"+currentTab+" tr#trASSRReadersTicket";
	if(count>1){
		lastId+="_"+(count);
	}
	$(lastId).after(trHtml);
	//update last content
	$("table#accessAndServicesTable"+currentTab+" tr#"+newId+" label[for='textASSRReadersTicket']").attr("for","textASSRReadersTicket_"+(count+1));
	$("table#accessAndServicesTable"+currentTab+" tr#"+newId+" input#textASSRReadersTicket").attr("id","textASSRReadersTicket_"+(count+1));
	$("table#accessAndServicesTable"+currentTab+" tr#"+newId+" label[for='textASSRRTLink']").attr("for","textASSRRTLink_"+(count+1));
	$("table#accessAndServicesTable"+currentTab+" tr#"+newId+" input#textASSRRTLink").attr("id","textASSRRTLink_"+(count+1));
}
function aSSRAddFurtherOrderInformation(){
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
	var count = $("table#accessAndServicesTable"+currentTab+" tr[id^='trASSRAddFurtherOrderInformation']").length;
	var newId = "trASSRAddFurtherOrderInformation_"+(count+1);
	var trHtml = "<tr id=\""+newId+"\">"+$("table#accessAndServicesTable tr[id='trASSRAddFurtherOrderInformation']").clone().html()+"</tr>";
	var lastId = "table#accessAndServicesTable"+currentTab+" tr#trASSRAddFurtherOrderInformation";
	if(count>1){
		lastId+="_"+(count);
	}
	$(lastId).after(trHtml);
	//update last content
	$("table#accessAndServicesTable"+currentTab+" tr#"+newId+" label[for='textASSRAdvancedOrders']").attr("for","textASSRAdvancedOrders_"+(count+1));
	$("table#accessAndServicesTable"+currentTab+" tr#"+newId+" input#textASSRAdvancedOrders").attr("id","textASSRAdvancedOrders_"+(count+1));
	$("table#accessAndServicesTable"+currentTab+" tr#"+newId+" label[for='textASSRAOLink']").attr("for","textASSRAOLink_"+(count+1));
	$("table#accessAndServicesTable"+currentTab+" tr#"+newId+" input#textASSRAOLink").attr("id","textASSRAOLink_"+(count+1));
}
function aSAddResearchServices(){
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
function addADescriptionOfYourRestaurationLab(){
	$("input#buttonAddADescriptionOfYourRestaurationLab").before("<input type=\"text\" id=\"textDescriptionOfYourRestaurationLab\" />");
	$("input#buttonAddADescriptionOfYourRestaurationLab").remove();
}
function aSAddADescriptionOfYourReproductionService(){
	$("input#buttonASAddADescriptionOfYourReproductionService").before("<input type=\"text\" id=\"textASADescriptionOfYourReproductionService\" />");
	$("input#buttonASAddADescriptionOfYourReproductionService").remove();
}
function aSReSeAddExhibition(){
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
	var count = $("table#accessAndServicesTable"+currentTab+" tr[id^='trASReSeExhibition']").length;
	var newId = "trASReSeExhibition_"+(count+1);
	var trHtml = "<tr id=\""+newId+"\">"+$("table#accessAndServicesTable tr[id='trASReSeExhibition']").clone().html()+"</tr>";
	var lastId = "table#accessAndServicesTable"+currentTab+" tr#trASReSeExhibition";
	if(count>1){
		lastId+="_"+(count);
	}
	$(lastId).after(trHtml);
	//update last content
	$("table#accessAndServicesTable"+currentTab+" tr#"+newId+" label[for='textASReSeExhibition']").attr("for","textASReSeExhibition_"+(count+1));
	$("table#accessAndServicesTable"+currentTab+" tr#"+newId+" input#textASReSeExhibition").attr("id","textASReSeExhibition_"+(count+1));
	$("table#accessAndServicesTable"+currentTab+" tr#"+newId+" label[for='selectASReSeExhibitionSelectLanguage']").attr("for","selectASReSeExhibitionSelectLanguage_"+(count+1));
	$("table#accessAndServicesTable"+currentTab+" tr#"+newId+" select#selectASReSeExhibitionSelectLanguage").attr("id","selectASReSeExhibitionSelectLanguage_"+(count+1));
}
function aSReSeToursAndSessions(){
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
	var count = $("table#accessAndServicesTable"+currentTab+" tr[id^='trASReSeToursAndSessions']").length;
	var newId = "trASReSeToursAndSessions_"+(count+1);
	var trHtml = "<tr id=\""+newId+"\">"+$("table#accessAndServicesTable tr[id='trASReSeToursAndSessions']").clone().html()+"</tr>";
	var lastId = "table#accessAndServicesTable"+currentTab+" tr#trASReSeToursAndSessions";
	if(count>1){
		lastId+="_"+(count);
	}
	$(lastId).after(trHtml);
	//update last content
	$("table#accessAndServicesTable"+currentTab+" tr#"+newId+" label[for='textASReSeToursAndSessions']").attr("for","textASReSeToursAndSessions_"+(count+1));
	$("table#accessAndServicesTable"+currentTab+" tr#"+newId+" input#textASReSeToursAndSessions").attr("id","textASReSeToursAndSessions_"+(count+1));
	$("table#accessAndServicesTable"+currentTab+" tr#"+newId+" label[for='selectASReSeToursAndSessionsSelectLanguage']").attr("for","selectASReSeToursAndSessionsSelectLanguage_"+(count+1));
	$("table#accessAndServicesTable"+currentTab+" tr#"+newId+" select#selectASReSeToursAndSessionsSelectLanguage").attr("id","selectASReSeToursAndSessionsSelectLanguage_"+(count+1));
}
function aSAddServices(){
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
	var count = $("table#accessAndServicesTable"+currentTab+" tr[id^='trASReSeOtherServices']").length;
	
	var newId = "trASReSeOtherServices_"+(count+1);
	var trHtml = "<tr id=\""+newId+"\">"+$("table#accessAndServicesTable tr[id='trASReSeOtherServices']").clone().html()+"</tr>";
	var lastId = "table#accessAndServicesTable"+currentTab+" tr#trASReSeOtherServices";
	if(count>1){
		lastId+="_"+(count);
	}
	$(lastId).after(trHtml);
	//update last content
	$("table#accessAndServicesTable"+currentTab+" tr#"+newId+" label[for='textASReSeOtherServices']").attr("for","textASReSeOtherServices_"+(count+1));
	$("table#accessAndServicesTable"+currentTab+" tr#"+newId+" input#textASReSeOtherServices").attr("id","textASReSeOtherServices_"+(count+1));
	$("table#accessAndServicesTable"+currentTab+" tr#"+newId+" label[for='selectASReSeOtherServicesSelectLanguage']").attr("for","selectASReSeOtherServicesSelectLanguage_"+(count+1));
	$("table#accessAndServicesTable"+currentTab+" tr#"+newId+" select#selectASReSeOtherServicesSelectLanguage").attr("id","selectASReSeOtherServicesSelectLanguage_"+(count+1));
}
function aSReSeAddExhibition(){
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
	var count = $("table#accessAndServicesTable"+currentTab+" tr[id^='trASReSeExhibition']").length;
	
	var newId = "trASReSeExhibition_"+(count+1);
	var trHtml = "<tr id=\""+newId+"\">"+$("table#accessAndServicesTable"+currentTab+" tr[id='trASReSeExhibition']").clone().html()+"</tr>";
	var lastId = "table#accessAndServicesTable"+currentTab+" tr#trASReSeExhibition";
	if(count>1){
		lastId+="_"+(count);
	}
	$(lastId).after(trHtml);
	//update last content
	$("table#accessAndServicesTable"+currentTab+" tr#"+newId+" label[for='textASReSeExhibition']").attr("for","textASReSeExhibition_"+(count+1));
	$("table#accessAndServicesTable"+currentTab+" tr#"+newId+" input#textASReSeExhibition").attr("id","textASReSeExhibition_"+(count+1));
	$("table#accessAndServicesTable"+currentTab+" tr#"+newId+" label[for='selectASReSeExhibitionSelectLanguage']").attr("for","selectASReSeExhibitionSelectLanguage_"+(count+1));
	$("table#accessAndServicesTable"+currentTab+" tr#"+newId+" select#selectASReSeExhibitionSelectLanguage").attr("id","selectASReSeExhibitionSelectLanguage_"+(count+1));
}
function descriptionAddFoundationInformation(){
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
	$("table#descriptionTable"+currentTab+" tr#"+newId+" input#textUnitOfAdministrativeStructure").attr("id","textUnitOfAdministrativeStructure_"+(count+1));
	$("table#descriptionTable"+currentTab+" tr#"+newId+" label[for='selectLanguageUnitOfAdministrativeStructure']").attr("for","selectLanguageUnitOfAdministrativeStructure_"+(count+1));
	$("table#descriptionTable"+currentTab+" tr#"+newId+" select#selectLanguageUnitOfAdministrativeStructure").attr("id","selectLanguageUnitOfAdministrativeStructure_"+(count+1));
}