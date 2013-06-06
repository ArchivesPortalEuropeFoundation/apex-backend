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
                        initButtons();
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
        minExpandLevel: 2
    });

}
function initButtons(){
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
