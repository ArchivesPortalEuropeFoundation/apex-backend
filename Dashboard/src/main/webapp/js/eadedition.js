function initEadEdition(){
//	initEadTree();
//	alert("yes");
}

function initEadTree(fileId, xmlTypeId){
    $("#eadTree").dynatree({
        title: "Bestandsdelen",
        rootVisible: false,
        autoFocus: false,
        fx: { height: "toggle", duration: 200 },
        // In real life we would call a URL on the server like this:
        initAjax: {
          url: "generateTreeJSONWithoutPreface.action",
          data: {
              fileId: fileId,
              xmlTypeId: xmlTypeId,
              isWithUrl: false
              }
          },

        onActivate: function(dtnode) {
            if(dtnode.data.id)
                correctId = dtnode.data.id;
            else
                correctId = -1;
            if(dtnode.data.more == null){
                $.post("editEadXml.action", {id: correctId, fileId: fileId, xmlTypeId: xmlTypeId}, function(databack){
                    if(databack.xml) {
                        $("#editionFormContainer").html(databack.xml);
                        initButtons(fileId,xmlTypeId);
                        $("#saveEADButton").click(function(){
                        	saveEAD(fileId,xmlTypeId);
                        });
                    }
                }, "json");
            }
        },

        onLazyRead: function(dtnode){
            if (dtnode.data.more == "after"){
                dtnode.parent.appendAjaxWithoutRemove({
                    url: "generateTreeJSON.action",
                    data: {
                        parentId: dtnode.data.parentId,
                        orderId: dtnode.data.orderId,
                        ecId: dtnode.data.ecId,
                        more: dtnode.data.more,
                        isWithUrl: false
                        }
                });
                var parent = dtnode.parent;
                dtnode.remove();
                var children = parent.getChildren();
                var index = children.length-1;
                var lastChild = children[index];
                var relativeTop =  $('#tree').scrollTop() + $(lastChild.span).offset().top - $("#tree").offset().top - 40;
	             $('#tree').animate({scrollTop: relativeTop}, 500);
            } else if (dtnode.data.more == "before"){
                dtnode.parent.insertBeforeAjaxWithoutRemove({
                    url: "generateTreeJSON.action",
                    data: {
                        parentId: dtnode.data.parentId,
                        orderId: dtnode.data.orderId,
                        ecId: dtnode.data.ecId,
                        more: dtnode.data.more,
                        max: dtnode.data.max,
                        isWithUrl: false
                        }
              }, dtnode);
              dtnode.remove();
            }

            else {
                dtnode.appendAjax({
                    url: "generateTreeJSON.action",
                    data: {parentId: dtnode.data.id, isWithUrl: false}

                });
            }
        },
        minExpandLevel: 2,
        generateIds: true
    });

}
function initButtons(fileId,xmlTypeId){
	$("#controls").removeClass("hidden");
	$("#deleteButton").click(function(event){
		confirmed =  confirm('dashboard.hgcreation.areyousuredeletechildren');
		//event.preventDefault();

	    if(confirmed){
	        var node = $("#eadTree").dynatree("getActiveNode");
	        $.post("deleteLevelHG.action", {id: node.data.id}, function(databack){
	            if(databack.success){
	                var parent = node.parent;
	                node.remove();
	                parent.render();
	                $("#editionFormContainer").html("");
	                $("#controls").addClass("hidden");
	            }
	        });
	    }
	});
}
function saveEAD(fileId,xmlTypeId){
	var node = $("#eadTree").dynatree("getActiveNode");
	//get all input and selected/option info into editionElement div
	var start = "'formValues'={";
	var content = start;
	$("p#editionFormContainer div .editionElement").find("input").each(function() {
		// Check if it's necessary to add the current value.
		if ($(this).val() != undefined) {
			content += (content.length>start.length) ? "," : "";
			content += "'" + $(this).attr("name") + "':" + "'" + $(this).val() + "'";
		}
	});
	$("p#editionFormContainer select").each(function() {
		// Check if it's necessary to add the current value.
		if ($(this).find("option:selected").val() != undefined) {
			content += (content.length>1) ? "," : "";
			content += "'" + $(this).attr("name") + "':" + "'" + $(this).find("option:selected").val() + "'";
		}
	});
	// Add "id" if exists.
	if (node.data.id != undefined) {
		content += ",'id':'"+node.data.id+"'";
	}
	// Add "key" if exists.
	if (node.data.key != undefined) {
		content += ",'key':'"+node.data.key+"'";
	}
	// Add "file id" if exists.
	if (fileId != undefined) {
		content += ",'fileId':'"+fileId+"'";
	}
	// Add "xml type Id" if exists.
	if (xmlTypeId != undefined) {
		content += ",'xmlTypeId':'"+xmlTypeId+"'";
	}
	content += "}";
//	content += (content.length>1)?",":"";
//	content += "'id':'"+node.data.id+"', 'fileId' : '"+fileId+"', 'xmlTypeId' : '"+xmlTypeId+"'}";
	alert("content: " + content);
	$.post("editEadXmlSaveLevel.action", content, function(data) {
		var response = "Not saved";
		if(data != undefined && data.saved != undefined && data.saved){
			response = "Saved";
		}
		alert("response: " + response);
	});
}