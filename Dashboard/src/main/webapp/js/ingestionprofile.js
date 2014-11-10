function initPage() {
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

function validateAndSave(profileNameError, dataProviderError, edmDaoError, languageError, europeanaLicenseError) {
    var profilename = $("#profilename").attr("value");
    if (profilename == null || profilename == "") {
    	alertAndDecode(profileNameError);
        return;
    }
    var upFileAction = $("#uploadedFileAction").attr("value");
    if (upFileAction == "2") {
        var dataProvider = $("#textDataProvider").attr("value");
        if (dataProvider == null || dataProvider == "") {
        	alertAndDecode(dataProviderError);
            return;
        }
        var edmDaoType = $("#edmDaoType").attr("value");
        if (edmDaoType == null || edmDaoType == "") {
        	alertAndDecode(edmDaoError);
            return;
        }
        var languageSelection = $("#languageselection").attr("value");
        if (languageSelection == null || languageSelection == "") {
        	alertAndDecode(languageError);
            return;
        }
        var license = $("#licenseeuropeana").attr("checked");
        if (license == "checked") {
            var europeanaLicense = $("#europeanaLicense").attr("value");
            if (europeanaLicense == null || europeanaLicense == "") {
            	alertAndDecode(europeanaLicenseError);
                return;
            }
        }
    }
    $('#ingestionprofilesSave').attr("disabled", "disabled");
    $('#webformIngestionprofile').submit();
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