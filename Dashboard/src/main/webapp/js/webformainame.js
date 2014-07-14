/**
	 * This function remove the special characters <, >, % when the user put them in the institution's name
	 */
	function checkName(text){
		var name = $("#changeainame_newname").val();
		var indexPercentage = name.indexOf("\%");
		var indexLessThan = name.indexOf("\<");
		var indexGreaterThan = name.indexOf("\>");
		var showAlert = true;
		while (indexPercentage > -1 || indexLessThan > -1 || indexGreaterThan > -1){
			if (showAlert) {
				alert(text);
				showAlert = false;
			}
			name =  name.replace("\%",'');
			name =  name.replace("\<",'');
			name =  name.replace("\>",'');
			$("#changeainame_newname").attr("value",name);
			indexPercentage =  name.indexOf("\%");
			indexLessThan =  name.indexOf("\<");
			indexGreaterThan =  name.indexOf("\>");
		}
	}