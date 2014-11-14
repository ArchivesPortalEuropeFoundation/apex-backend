function initPage() {
	checkActionMessages();
    $("#profileCb").off("change").on("change", function() {
    	var params = {profilelist: $("#profileCb").val()};
        $.get("ingestionprofiles.action", params, function(data) {
        	createColorboxForProcessing();
        	$("#principal").replaceWith(data);
            $("body meta").remove();
            $("body title").remove();
        });
    });
   
}

function checkActionMessages(){
	if($("#actionMessages span").length==1){
		var text = $("#actionMessages span").text();
		if(text.length>0 && text.substring(0,1)=='[' && text.substring(text.length-1)==']'){
			text = text.substring(1,text.length-1);
			$("#actionMessages").text(text);
		}
		setTimeout('$("#actionMessages").fadeOut("slow");',"5000");
	}
}

function hideAndShow(idPrefix, shown) {
    $("div[id^='" + idPrefix + "']").each(function() {
        $(this).hide();
    });
    $("ul#ingestionprofileTabsContainer li a[href^='#tab']").each(function() {
        $(this).removeClass("ingestionprofileCurrenttab");
    });
    $("div[id='" + shown + "']").show();
    $("ul#ingestionprofileTabsContainer li a[href='#" + shown + "']").addClass("ingestionprofileCurrenttab");
}

function validateAndSave(profileNameError, dataProviderError, edmDaoError, languageError, europeanaLicenseError, ingestionProfileSaveMessage,yesText,noText) {
	avoidCancellations();
    var profilename = $("#profilename").attr("value");
    if (profilename == null || profilename == "") {
    	alertAndDecode(profileNameError);
    	enableButtons();
        return;
    }
    var upFileAction = $("#uploadedFileAction").attr("value");
    if (upFileAction == "2") {
        var dataProvider = $("#textDataProvider").attr("value");
        if (dataProvider == null || dataProvider == "") {
        	alertAndDecode(dataProviderError);
        	enableButtons();
            return;
        }
        var edmDaoType = $("#edmDaoType").attr("value");
        if (edmDaoType == null || edmDaoType == "") {
        	alertAndDecode(edmDaoError);
        	enableButtons();
            return;
        }
        var languageSelection = $("#languageselection").attr("value");
        if (languageSelection == null || languageSelection == "") {
        	alertAndDecode(languageError);
        	enableButtons();
            return;
        }
        var license = $("#licenseeuropeana").attr("checked");
        if (license == "checked") {
            var europeanaLicense = $("#europeanaLicense").attr("value");
            if (europeanaLicense == null || europeanaLicense == "") {
            	alertAndDecode(europeanaLicenseError);
            	enableButtons();
                return;
            }
        }
    }
    $('#webformIngestionprofile').submit();
}

function avoidCancellations(){
	$('#ingestionprofilesSave').attr("disabled", "disabled");
	$('#ingestionprofilesCancel').attr("disabled", "disabled");
}

function enableButtons(){
	$('#ingestionprofilesSave').removeAttr("disabled");
	$('#ingestionprofilesCancel').removeAttr("disabled");
}

function changeDefaultOptionSet() {
    var assocType = $("#associatedFiletypeCb").val();
    var upFileAct = $("#uploadedFileAction").val();
    if (assocType != "0" && upFileAct == "2") {
        $("#uploadedFileAction").val("1");
    }
    if (assocType == "0") {
        var optionText = "Publish to Archives Portal Europe and Europeana";
        $('#uploadedFileAction option[value="1"]').after('<option value="2">' + optionText + '</option>');
    } else {
        $("#uploadedFileAction option[value='2']").remove();
    }
}

/**
 * Function to display the processing information.
 */
function createColorboxForProcessing() {
	$("#colorbox_load_finished").each(function(){
		$(this).remove();
	});
	// Create colorbox.
	$(document).colorbox({
		html:function(){
			var htmlCode = $("#processingInfoDiv").html(); 
			return htmlCode;
		},
		overlayClose:false, // Prevent close the colorbox when clicks on window.
		escKey:false, // Prevent close the colorbox when hit escape key.
		innerWidth:"150px",
		innerHeight:"36px",
		initialWidth:"0px",
		initialHeight:"0px",
		open:true,
		onLoad:function(){
			$("#colorbox").show();
			$("#cboxOverlay").show();

		},
		onComplete: function(){
			if(!$("#colorbox_load_finished").length){
				$("#processingInfoDiv").append("<input type=\"hidden\" id=\"colorbox_load_finished\" value=\"true\" />");
			}
        }
	});
	
	// Remove the close button from colorbox.
	$("#cboxClose").remove();
	// Prevent reload page.
	$(document).on("keydown", disableReload);
}
/**
 * Function to prevent reload the page using F5.
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
	if($("input#colorbox_load_finished").length){
		//removes flag
		$("#colorbox_load_finished").each(function(){
			$(this).remove(); 
		});
		// Close colorbox.
		$.colorbox.close(); 
		// Enable the page reload using F5.
		$(document).off("keydown", disableReload);
	}else{
		setTimeout(function(){deleteColorboxForProcessing();},500);
	}
	$("div[id='cboxOverlay']").each(function(i) {
		$(this).fadeOut(1000);
		
	});
	$("div[id='colorbox']").each(function(i) {
		$(this).fadeOut(1000);
	});
}