function loadUpPart(context,titleDT,countryId){
//	$.post("getALTree.action",{},function(e){
//		alert(e);
//	});
	hideAll();
	$(function(){
		$("#archivalLandscapeEditorUp").dynatree({
			//Navigated Search Tree for Countries, Archival Institution Groups and Archival Institutions configuration
			title: titleDT,
			rootVisible: false,
			fx: { height: "toggle", duration: 200 },
			selectMode: 1,
			//Tree initialization
			initAjax: {
   				url: context+"/getALTree.action",
   				data: {couId: countryId}
   			},
   			onLazyRead: function(node){
   				node.appendAjax({
   					url: context+"/getALTree.action",
   					data: {nodeId: node.data.key}
   				});
   				cleanInformation();
   			},
   			onActivate: function(node) {
   				$("#divForm").show();
				loadDownPart(node);
				cleanInformation();
			},
			onDeactivate: function(node) {
				$("#divForm").hide(); /*loadDownPart(node.data.key);*/
				cleanInformation();
			},
			onSelect: function(select,node){
				node.select(select);
			}
		});
	});	
}
function editAlternativeNames(){
	$("#editDiv").hide();
	$("#editLanguagesDiv").show();
	$("#selectedLangTranslations option").each(function(){
		if($(this).attr("selected")){
			$(this).removeAttr("selected");
		}
		if($(this).val()!=null && $(this).val().toLowerCase()=="eng"){
			$(this).prop('selected', true);
		}
	});
	if(document.getElementById("alternativeNames").options.length>1){
		$("select#alternativeNames").removeAttr("disabled");
		$("#deleteTargetSubmitDiv").show();
	}else{
		$("select#alternativeNames").attr("disabled","disabled");
		$("#deleteTargetSubmitDiv").hide();
	}
	$("#target").val("");

	recoverAlternativeName();

	var lang = $("#selectedLangTranslations option:selected").val();
	$("#alternativeNames option").each(function() {
		if ($(this).val() == lang) {
			$(this).attr("selected", "selected");
		}
	});

	checkPossibleAlternativeNamesActions($("select#alternativeNames").val());
}

function cancelEditAlternativeNames(){
	cleanInformation();
	$("#editDiv").show();
	$("select#alternativeNames").attr("disabled","disabled");
	$("#editLanguagesDiv").hide();
}

function sendAlternativeNames(){
	var dynatree = $("#archivalLandscapeEditorUp").dynatree("getTree");
	var activeNode = dynatree.getActiveNode();
	var aiId = activeNode.data.key;
	var lang = $("select#selectedLangTranslations option:selected").val();
	var text = $("input#target").val();
	$.post("launchALActions.action",{"action":"create_alternative_name","aiId":aiId,"lang":lang,"name":text},function(d){
		if(d.info){
//			showInformation(d.info);
			hideAll();
			dynatree.reload();
			displayNode(activeNode,d.info,true);
		}else if(d.error){
			showInformation(d.error,true);
		}
	});
}

function hideAll(){
	$("#filterSelectContainer").hide();
	$("#alternativesNamesDiv").hide();
	$("#editDiv").hide();
	$("#deleteDiv").hide();
	$("#editorActions").hide();
	$("#divGroupNodesContainer").hide();
	$("#editLanguagesDiv").hide();
	$("input#textAL").val("");
}

function alternativeNameSelected(){
	var attr = $("select#alternativeNames").attr("disabled");
	if(typeof attr!==undefined && attr!==false){
		var lang = $("select#alternativeNames").val().toLowerCase();
		var text = $("select#alternativeNames option:selected").text();

		if (text.indexOf("-") != "-1") {
			text = text.substring(text.indexOf("-") + 2);
		}

		$("#selectedLangTranslations option").each(function(){
			if($(this).val().toLowerCase()==lang){
				$(this).attr("selected","selected");
			}
		});
		$("input#target").val(text);

		checkPossibleAlternativeNamesActions(lang);
	}
}

function loadDownPart(node){
	$.post("getALActions.action",{nodeKey:node.data.key},function(e){
		hideAll();
		$("#alternativeNames").remove();
		var hideMoveButtons = false;
		$.each(e,function(key,value){
			if(value.enableAddToList=="true"){
				$("#filterSelectContainer").show();
			}else if(value.showAlternatives=="true"){
				$("#alternativesNamesDiv").show();
				$("#showLanguagesDiv").show();
				$("#editDiv").hide();
			}else if(value.showMoveDeleteActions=="true"){
				$("#editorActions").show();
				var node = $("#archivalLandscapeEditorUp").dynatree("getTree").getActiveNode();
				if(node.getPrevSibling()){
					$("#moveUpDiv").show();
				}else{
					$("#moveUpDiv").hide();
				}
				if(node.getNextSibling()){
					$("#moveDownDiv").show();
				}else{
					$("#moveDownDiv").hide();
				}
			}else if(value.showDeleteAction=="true"){
				$("#deleteDiv").show();
				$("#divGroupNodesContainer").show();
				$("div .secondFilterSelect").show();
				getGroups();
			}else if(value.canBeMoved=="true"){
				$("#divGroupNodesContainer").show();
				$("div .secondFilterSelect").show();
				getGroups();
				//restore onclick information
				$("#changeNodeDiv").attr("onclick","changeGroup();");
			}else if(value.hasContentPublished!=undefined && value.hasContentPublished!="false"){
				$("#divGroupNodesContainer").hide();
				$("div .secondFilterSelect").show();
				hideMoveButtons = true;
			}else if(value.info){
				showInformation(d.info);
			}else if(value.error){
				showInformation(d.error,true);
			}
		});
		if(!hideMoveButtons){
			//restore possible onclick event
			$("#moveUpDiv").attr("onclick","moveUp();");
			$("#moveDownDiv").attr("onclick","moveDown();");
		}
	});
}

function appendNode(){
	var nodeName = $("#textAL").val();
	var nodeType = $("#element").val();
	var dynatree = $("#archivalLandscapeEditorUp").dynatree("getTree");
	var activeNode = dynatree.getActiveNode();
	var fatherId = activeNode.data.key;
	var language = $("#selectedLang option:selected").val();
	if(fatherId.indexOf("_")!=-1){
		$.post("launchALActions.action",{"action":"create","name":nodeName,"father":fatherId,"type":nodeType,"lang":language},function(e){
			var message = "";
			if(e.info){
//				showInformation(e.info);
				message = e.info;
				dynatree.reload();
				displayNode(activeNode,message,true);
				hideAll();
			}else if(e.error){
				showInformation(e.error,true);
			}else{
				cleanInformation();
				dynatree.reload();
				displayNode(activeNode,message,true);
				hideAll();
			}
		});
	}
}

function deleteNode(){
	// Show colorbox.
	createColorboxForProcessing();
	var dynatree = $("#archivalLandscapeEditorUp").dynatree("getTree");
	var activeNode = dynatree.getActiveNode();
	var parent = activeNode.getParent();
	var aiId = activeNode.data.key;
	if(aiId.indexOf("_")!=-1){
		aiId = aiId.substring(aiId.indexOf("_")+1);
		$.post("launchALActions.action",{"action":"delete","aiId":aiId},function(e){
			var error = false;
			var message = "";
			if(e.info){
//				showInformation(e.info);
				message = e.info;
				hideAll();
			}else if(e.error){
				showInformation(e.error,true);
				error = true;
			}else{
				cleanInformation();
			}
			if(!error){
				// Close colorbox.
				deleteColorboxForProcessing();
				dynatree.reload();
				displayNode(parent,message,true);
				//showInformation(message);
			}
		});
	}
}

function getGroups(){
	var dynatree = $("#archivalLandscapeEditorUp").dynatree("getTree");
	var activeNode = dynatree.getActiveNode();
	var activeId = "";
	if(activeNode){
		activeId = activeNode.data.key;
	}
	$.post("launchALActions.action",{"action":"get_groups","aiId":activeId},function(d){
		var groupSelect = "<div class=\"secondFilterSelect\"><select id=\"groupSelect\">";
		var groups = 0;
		$.each(d,function(k,group){
			optionStart = "";
			optionEnd = "";
			optionMiddle = "";
			$.each(group,function(k2,v){
				if(v.key){
					optionStart = "<option value=\""+v.key+"\"";
				}else if(v.name){
					optionEnd = v.name+"</option>";
				}else if(v.disabled){
					optionMiddle = " disabled=\"disabled\"";
				}
			});
			groupSelect += optionStart+optionMiddle+">"+optionEnd;
			groups++;
		});
		groupSelect+= "</select></div>";
		if(groups>0){
			$("div .secondFilterSelect").each(function(){
				$(this).remove();
			});
			$("#divGroupNodesContainer").show();
			$("#changeNodeDiv").before(groupSelect);
			$("div .secondFilterSelect").show();
		}else{
			$("#divGroupNodesContainer").hide();
		}
	});
}

function moveUp(){
	var dynatree = $("#archivalLandscapeEditorUp").dynatree("getTree");
	var activeNode = dynatree.getActiveNode();
	var currentId = activeNode.data.key;
	$.post("launchALActions.action",{"action":"move_up","aiId":currentId},function(d){
		if (d.error) {
			showInformation(d.error,true);
		} else {
			hideAll();
			dynatree.reload();
			displayNode(activeNode,d.info,true); //in this case old parents are the same, so reuse that information
		}
	});
}

function moveDown(){
	var dynatree = $("#archivalLandscapeEditorUp").dynatree("getTree");
	var activeNode = dynatree.getActiveNode();
	var currentId = activeNode.data.key;
	$.post("launchALActions.action",{"action":"move_down","aiId":currentId},function(d){
		if (d.error) {
			showInformation(d.error,true);
		} else{
			hideAll();
			dynatree.reload();
			displayNode(activeNode,d.info,true); //in this case old parents are the same, so reuse that information
		}
	});
}

function displayNode(node,message,extra){
	var parents = new Array();
	if(node.getParent()!=null){
		//get parent structure
		var currentNode = node;
		var i = 0;
		if(extra){
			parents[i++] = currentNode;
		}
		do{
			currentNode = currentNode.getParent();
			parents[i++] = currentNode;
		}while(currentNode.getParent()!=null);
		//use parent structure to display target node
		expandParents(parents,i-2,message,node); //review i
	}
}

function expandParents(parents,i,message,targetNode){
	var dynatree = $("#archivalLandscapeEditorUp").dynatree("getTree");
	if(i>=0){
		var key = "";
		//could be used for dynatree_node[] and string[] keys
		if(parents[i].data){
			key = parents[i].data.key;
		}else{
			key = parents[i];
		}
		var target = dynatree.getNodeByKey(key);
		if(!target){
			setTimeout(function(){expandParents(parents,i,message,targetNode);},40);
		}else{
			target.expand(true);
			expandParents(parents,i-1,message,targetNode);
		}
	}else{
//		launchFinalAction();
		setTimeout(function(){launchFinalAction(targetNode.data.key,message);},40);
	}
}

function launchFinalAction(key,message){
	//targetNode.select(true);
	var dynatree = $("#archivalLandscapeEditorUp").dynatree("getTree");
	var target = dynatree.getNodeByKey(key);
	if(!target){
		setTimeout(function(){launchFinalAction(key,message);},40);
	}else{
		target.activate(true);
		//target.select(true);
		//targetNode.activate(true);
		showInformation(message);
	}
}

function getAlternativeNames(){
	var dynatree = $("#archivalLandscapeEditorUp").dynatree("getTree");
	var activeNode = dynatree.getActiveNode();
	var currentId = activeNode.data.key;
	$.post("launchALActions.action",{"action":"get_alternative_names","aiId":currentId},function(d){
		if(d.alternativeNames){
			$("div .alternativeNamesSelect").remove();
			optionStart = "";
			optionEnd = "";
			var alternativeNamesCounter = 0;
			var options = "";
			$.each(d.alternativeNames,function(k,alternativeName){
				$.each(alternativeName,function(k2,v){
					alternativeNamesCounter++;
					if(v.lang){
						optionStart = "<option value=\""+v.lang+"\" >" + v.lang + " - ";
					}else if(v.name){
						optionEnd = unescape(v.name)+"</option>";
					}
				});
				options += optionStart+optionEnd+"\n";
			});
			var groupSelect = "<div class=\"alternativeNamesSelect\"><select "+((alternativeNamesCounter>1)?"":"disabled=\"disabled\"")+" size=\""+alternativeNamesCounter+"\" id=\"alternativeNames\" onclick=\"alternativeNameSelected();\" onkeyup=\"alternativeNameSelected();\" >";
			groupSelect += options;
			groupSelect += "</select></div>";
			$("#alternativesNamesDiv").append(groupSelect);
			$("#showLanguagesDiv").hide();
			$("select#alternativeNames").attr("disabled","disabled");
			$("#editDiv").show();
		}
	});

}

function changeGroup(){
	var dynatree = $("#archivalLandscapeEditorUp").dynatree("getTree");
	var activeNode = dynatree.getActiveNode();
	var currentId = activeNode.data.key;
	var groupSelect = $("#groupSelect option:selected").val();
	$.post("launchALActions.action",{"action":"change_group","aiId":currentId,"groupSelected":groupSelect},function(d){
		var message = "";
		var expanded = false;
		$.each(d,function(k,v){
			if(v.info){
//				showInformation(d.info);
				message = v.info;
				dynatree.reload();
				hideAll();
			}else if(v.error){
				showInformation(d.error,true);
			}else if(v.newparents){ //get parent structure
				var parents = new Array();
				var found;
				if ($.browser.msie && $.browser.version == 8){   //internet explorer 8
					found = v.newparents.indexOf(",");
				}else{
					found = $.inArray(",",v.newparents);
				}
				if(found != -1){
					parents = v.newparents.split(",");
				}else{
					parents[0] = v.newparents;
				}
				var i = parents.length-1;
				if(i>=0){
					expandParents(parents,i,message,activeNode); //review i
					expanded = true;
				}
			}
		});
		
		if(!expanded){
			showInformation(message);
		}
	});
}

function recoverAlternativeName() {
	var lang = $("select#selectedLangTranslations option:selected").val();
	var text = "";
	$("#alternativeNames option").each(function() {
		if ($(this).val() == lang) {
			text = $(this).text();

			if (text.indexOf("-") != "-1") {
				text = text.substring(text.indexOf("-") + 2);
			}
		}
	});
	$("input#target").attr("value", text);
	checkPossibleAlternativeNamesActions(lang);
}

function deleteAlternativeNames() {
	var dynatree = $("#archivalLandscapeEditorUp").dynatree("getTree");
	var activeNode = dynatree.getActiveNode();
	var aiId = activeNode.data.key;
	var lang = $("select#selectedLangTranslations option:selected").val();
	var text = $("input#target").val();
	$.post("launchALActions.action",{"action":"delete_alternative_name","aiId":aiId,"lang":lang,"name":text},function(d){
		if(d.info){
			//showInformation(d.info);
			hideAll();
			dynatree.reload();
			displayNode(activeNode,d.info,true); //in this case old parents are the same, so reuse that information
		}else if(d.error){
			showInformation(d.error,true);
		}
	});
}



function checkPossibleAlternativeNamesActions(lang) {
	var dynatree = $("#archivalLandscapeEditorUp").dynatree("getTree");
	var activeNode = dynatree.getActiveNode();
	$.post("getALActions.action", {nodeKey:activeNode.data.key}, function(d){
		$.each(d, function(key, value) {
			if (value.mainAlternativeName != "" && lang.toUpperCase() == value.mainAlternativeName) {
				$("#editTargetSubmitDiv").hide();
				$("#deleteTargetSubmitDiv").hide();
			} else {
				$("#editTargetSubmitDiv").show();
				$("#deleteTargetSubmitDiv").show();
			}
		});
	});
}

function showInformation(information,error){
	var message = "<span";
	if(error){
		message += " style=\"color:red;font-weight:bold;\"";
	}else{
		message += " style=\"color:green;\"";
	}
	message += ">"+information+"</span>";
	$("#informationDiv").html(message);
	$("#informationDiv").fadeIn("slow");
}

function cleanInformation(){
	$("#informationDiv").fadeOut("slow");
}

/**
 * Function to display the processing information.
 */
function createColorboxForProcessing() {
	// Create colorbox.
	$.colorbox({html:function(){
			var htmlCode = $("#processingInfoDiv").html();
			return htmlCode;
		},
		overlayClose:false, // Prevent close the colorbox when clicks on window.
		escKey:false, // Prevent close the colorbox when hit escape key.
		innerWidth:"150px",
		innerHeight:"36px",
		initialWidth:"0px",
		initialHeight:"0px"
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
	// Close colorbox.
	$.fn.colorbox.close();

	// Enable the page reload using F5.
	$(document).off("keydown", disableReload)
}
