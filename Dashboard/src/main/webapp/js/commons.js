//based on Bastiaan Verhoef comment on issue Bug #1411
function HTMLDecode(s){
    return $('<div></div>').html(s).text();
}

function alertAndDecode(text){
	alert(HTMLDecode(text));
}

function confirmAndDecode(text){
	return confirm(HTMLDecode(text));
}

function displayExitDialog(dialogText,dialogYes,dialogNo){
	dialogText = HTMLDecode(dialogText);
	dialogYes = HTMLDecode(dialogYes);
	dialogNo = HTMLDecode(dialogNo);
	if($(".ui-dialog").length>0){
		$(".ui-dialog").remove();
	}
	$("body").unbind("click");
	var dialog = $("<p id=\"pAlertExitDialog\">"+dialogText+"</p>").dialog({
		closeOnEscape : true,
        buttons: 
    	[{
        	text: dialogYes,
        	click : function() {
        		$(this).dialog("close");
        		$("input#btnYes").trigger("click");
        	}
    	},{
        	text : dialogNo,
        	click :  function() {
        		$(this).dialog("close");
        		$("input#btnNo").trigger("click");
        	}
    	}]
	});
	setTimeout("putClickOutsideCloseDialog();","100");
}

function displayAlertDialog(message){
	message = HTMLDecode(message);
	if($(".ui-dialog").length>0){
		$(".ui-dialog").remove();
	}
	$("body").unbind("click");
	var dialog = $("<p id=\"pAlertExitDialog\">"+message+"</p>").dialog({
		closeOnEscape : true,
        buttons: {
        	"OK" : function() {
        		$("body").unbind("click");
        		$(this).dialog("close");
        	}
    	}
	});
	setTimeout("putClickOutsideCloseDialog();","100");
}

function putClickOutsideCloseDialog(){
	$("body").bind("click",function(e){
		if($("#pAlertExitDialog").dialog("isOpen")
				&& !jQuery(e.target).is(".ui-dialog")
				&& !jQuery(e.target).closest(".ui-dialog").length ){
			$('#pAlertExitDialog').dialog("close");
			$("body").unbind("click");
		}
	});
}