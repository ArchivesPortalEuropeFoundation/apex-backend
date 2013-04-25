function hideAndShow(idPrefix,shown){
	$("div[id^='"+idPrefix+"']").each(function(){
		$(this).hide();
	});
	$("ul#eag2012TabsContainer li a[href^='#tab']").each(function(){
		$(this).removeClass("eag2012currenttab");
	});
	$("div[id='"+shown+"']").show();
	$("ul#eag2012TabsContainer li a[href='#"+shown+"']").addClass("eag2012currenttab");
}

function deleteChecks() {
	$('.required').remove();
}

var clickYourInstitutionAction = function(){
	// Delete old checks
	deleteChecks();

	// Mandatory elements
	var yiMandatoryElements = new Array("textYIInstitutionCountryCode", "textYIIdentifierOfTheInstitution",
	                           "textYINameOfTheInstitution", "selectYINOTISelectLanguage");

	var jsonData = "{";
	
		//content before institutions part
		$("table#yourInstitutionTable input[type='text']").each(function(){
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
		$("table#yourInstitutionTable select").each(function(){
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

		//institutions
		var formData = new Array();
		$("table[id^='yourInstitutionTabContent_']").each(function(){
			var id = $(this).attr("id");
			if(id.indexOf("#")>-1){
				id = id.substring(id.indexOf("#"));
			}
			formData.push(id);
		});
		jsonData += ",'institutions':[";
		for(var i=0;i<formData.length;i++){
			
			var yiMERepositories = new Array("textYIStreet", "textYICity", "textYICountry", "textYIWebpage",
            "textYIOpeningTimes");
			
			if(jsonData.substring(jsonData.length-1)!='['){
				jsonData += ",";
			}
			jsonData += "{'"+formData[i]+"':";
			//input type text
			$("#"+formData[i]+" input[type='text']").each(function(){
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
			$("#"+formData[i]+" select").each(function(){
				if(jsonData.charAt(jsonData.length-1)!=':'){
					jsonData += ",";
				}
				jsonData += "'"+$(this).attr("id")+"' : '"+$(this).attr("value")+"'";
			});
			if(yiMERepositories.length>0){
				validationArray.push(formData[i],yiMERepositories);
			}
			jsonData += "}";
		}
		
	jsonData += "]}";
//	alert(jsonData);

	for (var i = 0; i < yiMandatoryElements.length; i++) {
		var element = document.getElementById(yiMandatoryElements[i].toString());
		var subelement = document.createElement('p');
		
		subelement.appendChild(document.createTextNode('Field required'));
		subelement.id = yiMandatoryElements[i].toString() + '_required';
		subelement.className="required";
		element.parentNode.insertBefore(subelement, element.nextSibling);
	}

	for (var i = 0; i < validationArray.length; i = (i + 2)) {
		var array = validationArray[i+1];

		for (var j = 0; j < array.length; j++) {
			var subelement = document.createElement('p');

			subelement.appendChild(document.createTextNode('Field required'));
			subelement.id = array[j].toString() + '_required';
			subelement.className="required";

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
		});
		//content from selects
		$("table#accessAndServicesTable select").each(function(){
			if(jsonData.charAt(jsonData.length-1)!=':'){
				jsonData += ",";
			}
			jsonData += "'"+$(this).attr("id")+"' : '"+$(this).attr("value")+"'";

			// Check fill mandatory fields.
			if ($(this).attr("value") != '') {
				var position = aasMandatoryElements.indexOf($(this).attr("id"));
				aasMandatoryElements.splice(position, 1);
			}
		});
	jsonData += "]}";
//	alert(jsonData);

	for (var i = 0; i < aasMandatoryElements.length; i++) {
		var element = document.getElementById(aasMandatoryElements[i].toString());
		var subelement = document.createElement('p');
		
		subelement.appendChild(document.createTextNode('Field required'));
		subelement.id = aasMandatoryElements[i].toString() + '_required';
		subelement.className="required";
		element.parentNode.insertBefore(subelement, element.nextSibling);
	}
};