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
   			},
   			onActivate: function(node) {
   				$("#divForm").show();
				loadDownPart(node);
			},
			onDeactivate: function(node) {
				$("#divForm").hide(); /*loadDownPart(node.data.key);*/
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
//	$("#selectedLang").val("ENG");
//	var changed = false;
//	var selection = document.getElementById("selectedLang");
//	for(var i=0;i<selection.options.length && !changed;i++){
//        if (selection.options[i].value == "eng") {
//            selection.selectedIndex = i;
//            changed = true;
//        }
//    }
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
			$("#informationDiv").text(d.info);
			hideAll();
			dynatree.reload();
		}else if(d.error){
			$("#informationDiv").text(d.error);
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
				getGroups();
			}
		});
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
			dynatree.reload();
		});
	}
}

function deleteNode(){
	var dynatree = $("#archivalLandscapeEditorUp").dynatree("getTree");
	var activeNode = dynatree.getActiveNode();
	var aiId = activeNode.data.key;
	if(aiId.indexOf("_")!=-1){
		aiId = aiId.substring(aiId.indexOf("_")+1);
		$.post("launchALActions.action",{"action":"delete","aiId":aiId},function(e){
			dynatree.reload();
		});
	}
}

function getGroups(){
	$.post("launchALActions.action",{"action":"get_groups"},function(d){
		$("#groupSelect").remove();
		var groupSelect = "<select id=\"groupSelect\">";
		var groups = 0;
		$.each(d,function(k,group){
			optionStart = "";
			optionEnd = "";
			$.each(group,function(k2,v){
				if(v.key){
					optionStart = "<option value=\""+v.key+"\">";
				}else if(v.name){
					optionEnd = v.name+"</option>";
				}
			});
			groupSelect += optionStart+optionEnd;
			groups++;
		});
		groupSelect+= "</select>";
		if(groups>0){
			$("#divGroupNodesContainer").show();
			$("#changeNodeDiv").before(groupSelect);
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
			$("#informationDiv").text(d.error);
		} else {
			dynatree.reload();
		}
	});
}

function moveDown(){
	var dynatree = $("#archivalLandscapeEditorUp").dynatree("getTree");
	var activeNode = dynatree.getActiveNode();
	var currentId = activeNode.data.key;
	$.post("launchALActions.action",{"action":"move_down","aiId":currentId},function(d){
		if (d.error) {
			$("#informationDiv").text(d.error);
		} else {
			dynatree.reload();
		}
	});
}

function getAlternativeNames(){
	var dynatree = $("#archivalLandscapeEditorUp").dynatree("getTree");
	var activeNode = dynatree.getActiveNode();
	var currentId = activeNode.data.key;
	$.post("launchALActions.action",{"action":"get_alternative_names","aiId":currentId},function(d){
		if(d.alternativeNames){
			$("#alternativeNames").remove();
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
			var groupSelect = "<select "+((alternativeNamesCounter>1)?"":"disabled=\"disabled\"")+" size=\""+alternativeNamesCounter+"\" id=\"alternativeNames\" onclick=\"alternativeNameSelected();\" onkeyup=\"alternativeNameSelected();\" >";
			groupSelect += options;
			groupSelect += "</select>";
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
		if(d.info){
			$("#informationDiv").text(d.info);
			dynatree.reload();
		}else if(d.error){
			$("#informationDiv").text(d.error);
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
			$("#informationDiv").text(d.info);
			hideAll();
			dynatree.reload();
		}else if(d.error){
			$("#informationDiv").text(d.error);
		}
	});
}



function checkPossibleAlternativeNamesActions(lang) {
	var dynatree = $("#archivalLandscapeEditorUp").dynatree("getTree");
	var activeNode = dynatree.getActiveNode();

	$.post("getALActions.action", {nodeKey:activeNode.data.key}, function(d){
		$.each(d, function(key, value) {
			if (value.mainAlternativeName != ""
				&& lang.toUpperCase() == value.mainAlternativeName) {	
				$("#editTargetSubmitDiv").hide();
				$("#deleteTargetSubmitDiv").hide();
			} else {
				$("#editTargetSubmitDiv").show();
				$("#deleteTargetSubmitDiv").show();
			}
		});
});
}
