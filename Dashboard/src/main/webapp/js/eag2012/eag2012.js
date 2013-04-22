function hideAndShow(idPrefix,shown){
	$("div[id^='"+idPrefix+"']").each(function(){
		$(this).hide();
	});
	$("div[id='"+shown+"']").show();
}
var clickAction = function(){
	
	var jsonData = "{";
	
		//content before institutions part
		$("table#yourInstitutionTable input[type='text']").each(function(){
			if(jsonData.length>1){
				jsonData += ",";
			}
			jsonData += "'"+$(this).attr("id")+"' : '"+$(this).attr("value")+"'";
		});
		//select options selected
		$("table#yourInstitutionTable select").each(function(){
			if(jsonData.charAt(jsonData.length-1)!=':'){
				jsonData += ",";
			}
			jsonData += "'"+$(this).attr("id")+"' : '"+$(this).attr("value")+"'";
		});
		
	
	
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
		});
		//select options selected
		$("#"+formData[i]+" select").each(function(){
			if(jsonData.charAt(jsonData.length-1)!=':'){
				jsonData += ",";
			}
			jsonData += "'"+$(this).attr("id")+"' : '"+$(this).attr("value")+"'";
		});
		jsonData += "}";
	}
	jsonData += "]}";
	//alert(jsonData);
}