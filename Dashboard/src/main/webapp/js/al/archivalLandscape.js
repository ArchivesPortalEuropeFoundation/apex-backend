$(document).ready(function(){
	$("input#uploadActionButton").click(function(){
//		$("#checkboxPreview").hide();
//		$("label[for='checkboxPreview']").hide();
//		if($("#checkboxPreview").is(':checked')){
//			$("div#uploadPanelDiv").html("<span>uploading...</span>");
//			$("#uploadActionButton").hide();
//	        var frm = $("form#uploadALForm");
//	        var fd = new FormData(frm[0]);
//	        var xmlHttpRequest = getXmlHttpRequest();
//	        xmlHttpRequest.open(frm.attr("method"), frm.attr("action"), true);
//	        xmlHttpRequest.onreadystatechange = function() {
//	            if (xmlHttpRequest.readyState == 4) {
//	            	try{
//	            		$("div#uploadPanelDiv").html("<span>File uploaded, processing response...</span>");
//	            		var response = JSON.parse(xmlHttpRequest.responseText);
//	                	successAlUpload(response);
//	            	}catch(e){
//	            		$("div#uploadPanelDiv").html("<span>Something wrong happened.</span>");
//	            	}
//	            }
//	        };
//	        xmlHttpRequest.send(fd);
//		}else{
			submitForm();
//		}
	});
});

function getXmlHttpRequest(){
	var xmlHttpRequest = null;
	if (window.XMLHttpRequest) { //New browsers
        xmlHttpRequest = new XMLHttpRequest();
    }else if (window.ActiveXObject) {//ActiveXObjects (Microsoft not compatible versions)
        try {
            xmlHttpRequest = new ActiveXObject("MSXML2.XMLHTTP");
        } catch (e) {
            try {
                xmlHttpRequest = new ActiveXObject("Microsoft.XMLHTTP");
            } catch (e) {
                console.log("error :: " + e.message);
            }
        }
    }
	return xmlHttpRequest;
}

function successAlUpload(e){
	$("#httpFile").hide();
	$("#httpFileLabel").hide();
	$("#checkboxPreview").hide();
	$("label[for='checkboxPreview']").hide();
	if(e.error){
		$("div#uploadPanelDiv").html("<p>"+e.error+"</p>");
	}else{
		var oldDiv = newDiv = "";
		var thereAreChildren = e.oldtree && e.oldtree.length>0;
		if(e.oldtree){
			oldDiv = "<div id=\"oldTreeParent\" style=\"width:"+((thereAreChildren)?"50":"100")+"%;float:left;\"><div>"+e.oldtreeMessage+"</div><div id=\"oldtree\"></div></div>";
		}
		if(e.newtree){
			var stateDiv = "<div style=\"margin-bottom:5px;\"><span>"+e.status+":</span></div>";
			if(!thereAreChildren){
				oldDiv = "";
			}
			newDiv = "<div id=\"newTreeParent\" style=\"width:"+((thereAreChildren)?"50":"100")+"%;float:left;\"><div>"+e.newtreeMessage+"</div><div id=\"newtree\"></div></div></div>";
			$("div#uploadPanelDiv").html("<p>"+stateDiv+"</p><p><div id=\"allTrees\" style=\"max-height:400px;overflow-y:auto;overflow-x:hidden;\">"+oldDiv+newDiv+"</div></p>");
			$("#oldtree").attr("style","width:50%;float:left;");
			if(e.oldtree){
				$("#oldtree").dynatree({
					initAjax: null,
					title: "Old AL dynatree",
					children: e.oldtree,
					fx: { height: "toggle", duration: 200 },
					selectMode: 1,
					minExpandLevel: 99,
					expand: true
				});
			}
			if(e.newtree){
				$("#newtree").dynatree({
					initAjax: null,
					title: "New AL dynatree",
					children: e.newtree,
					fx: { height: "toggle", duration: 200 },
					selectMode: 1,
					minExpandLevel: 99,
					expand: true
				});
			}
			var questionText = e.question;
			var yesText = e.yes;
			var noText = e.no;
			$("#allTrees").after("<div style=\"float:left;width:100%;\"><p id=\"questionP\"><div>"+questionText+"</div><div id=\"divResponse\"></div></p></div>");
			$("#divResponse").append("<input type=\"button\" id=\"responseYes\" value=\""+yesText+"\" />");
			$("#divResponse").append("<input type=\"button\" id=\"responseNo\" value=\""+noText+"\" />");
			$("#responseYes").click(function(){
				$("#divResponse").hide();
				submitForm();
			});
			$("#responseNo").click(function(){
				$("#httpFile").show();
				$("#uploadActionButton").show();
				$("#httpFileLabel").show();
				$("div#uploadPanelDiv").html("");
				$("#checkboxPreview").show();
				$("label[for='checkboxPreview']").show();
			});
		}
	}
}

function submitForm(){
	$("form#uploadALForm").attr("action","ALUpload.action");
	$("form#uploadALForm").submit();
	$("input#uploadActionButton").hide();
}