    function bindDefaultContextMenu(message) {
        $("#myMenu .cancel a").click(function(){
            if(confirm(message)){
                window.location = 'contentmanager.action';
            }
        });
    }
    function createTreeWithoutData(message, emptyTitle){
        $("#tree").dynatree({
            fx: {
                height: "toggle",
                duration: 200
            },
            
            onPostInit: function(isReloading, isError) {
                $.fn.colorbox({
                    width:"80%",
                    height:"400px",
                    inline:true,
                    overlayClose:false,
                    escKey:false,
                    onOpen:function(){
                        $("#myMenu").hide();
                        $(document).unbind('keydown.cbox_close');
                    },
                    onLoad:function(){
                        $('#cboxClose').remove();
                        $("#tree").dynatree("getTree").activateKey('ec_0');
                        createEditPanel($("#tree").dynatree("getActiveNode"), false, false);
                    },
                    onCleanup:function(){
                        bindContextMenu(emptyTitle);
                        $("#myMenu").show();
                    },
                    href:"#editColorbox"
                });
            },

            onClick: function(dtnode){
                logMsg("onClick");
                if(dtnode.data.isFolder === true)
                    bindContextMenu(emptyTitle);
                else
                    unbindContextMenu();
            },

            onSelect: function(select, dtnode){
                logMsg("onSelect");
            },

            onActivate: function(dtnode) {
                logMsg("onActivate");
            },

            onLazyRead: function(dtnode){
                logMsg("onLazyRead");
            },

            minExpandLevel: 1
        });
    }
    var keyOfElements = 1;
    function bindContextMenu(emptyTitle) {
        $("#myMenu .edit a").unbind();
        $("#myMenu .add a").unbind();
        $("#myMenu .addFA a").unbind();
        $("#myMenu .delete a").unbind();
        $("#myMenu").find("li.disabled").removeClass('disabled');
        $("#myMenu .add a").colorbox(
            {
                width:"80%",
                height:"400px",
                inline:true,
                overlayClose:false,
                escKey:false,
                onOpen:function(){
                    $(document).unbind('keydown.cbox_close');
                    $("#myMenu").hide();
                },
                onLoad:function(){
                    $('#cboxClose').remove();
                    //I have to create a level here
                    var node = $("#tree").dynatree("getActiveNode");
                    var obj = [ { title: emptyTitle, isLazy: false, isFolder: true, key: 'cl_'+keyOfElements }];
                    node.addChild(obj);
                    $("#tree").dynatree("getTree").activateKey('cl_'+keyOfElements++);
                    createEditPanel($("#tree").dynatree("getActiveNode"), true, true);
                },
                onCleanup:function(){ $("#myMenu").show(); },
                href:"#editColorbox"
            }
        );
        $("#myMenu .edit a").colorbox(
            {
                width:"80%",
                height:"400px",
                inline:true,
                overlayClose:false,
                escKey:false,
                onOpen:function(){
                    $(document).unbind('keydown.cbox_close');
                    $("#myMenu").hide();
                },
                onLoad:function(){
                    $('#cboxClose').remove();
                    createEditPanel($("#tree").dynatree("getActiveNode"), true, false);
                },
                onCleanup:function(){ $("#myMenu").show(); },
                href:"#editColorbox"
            }
        );
        $("#myMenu .delete a").click(function(){
            if(confirm('dashboard.hgcreation.areyousuredeletechildren')){
                var node = $("#tree").dynatree("getActiveNode");
                $.post("deleteLevelHG.action", {key: node.data.key}, function(databack){
                    if(databack.success){
                        var parent = node.parent;
                        node.remove();
                        parent.render();
                    }
                });
            }
        });
    }


    function unbindContextMenu(){
        $("#myMenu .edit a").unbind();
        $("#myMenu .add a").unbind();
        $("#myMenu .delete a").unbind();
        $("#myMenu").find("li").addClass('disabled');

        $("#myMenu li.delete").removeClass('disabled');
        $("#myMenu li.save").removeClass('disabled');
        $("#myMenu li.cancel").removeClass('disabled');
        $("#myMenu .delete a").click(function(){
            if(confirm('dashboard.hgcreation.areyousuredelete')){
                var node = $("#tree").dynatree("getActiveNode");
                $.post("deleteLevelHG.action", {key: node.data.key}, function(databack){
                    if(databack.success){
                        var parent = node.parent;
                        node.remove();
                        parent.render();
                    }
                });
            }
        });
    }

    function unbindAllBtn(){
        $("#editBtnSave").unbind('click');
        $("#editBtnCancel").unbind('click');
    }

    function createEditPanel(node, isCancelable, isAddedLevel){
        unbindAllBtn();
        logMsg("Create Edit Panel With Node: " + node.data.key);
        $("#editTitle").val("");
        $("#editIdentifier").val("");
        $("#editDesc").val("");

        if(!isCancelable)
            $("#editBtnCancel").css("display", "none");
        else
            $("#editBtnCancel").css("display", "inline");

        if(node.data.identifier != null)
            $("#editIdentifier").val(node.data.identifier);
        if(node.data.title != null)
            $("#editTitle").val(node.data.title);
        if(node.data.desc != null)
            $("#editDesc").val(node.data.desc);

        $("#editBtnSave").click(function(){
            $(this).unbind('click');
            if(isForEadContent(node.data.key)){
                $("#waitImg").css("display", "inline");
                $("#editBtnSave").css("display", "none");
                $.post("addEadContentData.action", {name: $("#editTitle").val(), identifier: $("#editIdentifier").val(), desc: $("#editDesc").val(), key: node.data.key}, function(databack){
                    if(databack.success){
                        $("#waitImg").css("display", "none");
                        node.data.identifier = $("#editIdentifier").val();
                        node.data.title = $("#editTitle").val();
                        node.data.desc = $("#editDesc").val();
                        node.data.key = databack.newId;
                        node.render();
                        $("#editBtnSave").css("display", "block");
                        $.fn.colorbox.close();
                    }
                    hideOrShowSelectAllFAsWindow(); //from contentmanager.js
                }, "json");
            } else {
                $("#waitImg").css("display", "inline");
                $("#editBtnSave").css("display", "none");
                $.post("addCLevelData.action", {name: $("#editTitle").val(), identifier: $("#editIdentifier").val(), desc: $("#editDesc").val(), key: node.data.key, parentId: node.parent.data.key}, function(databack){
                    if(databack.success){
                        $("#waitImg").css("display", "none");
                        node.data.identifier = $("#editIdentifier").val();
                        node.data.title = $("#editTitle").val();
                        node.data.desc = $("#editDesc").val();
                        node.data.key = databack.newId;
//                        node.data.dataToEdit = databack.dataToEdit;
                        node.render();
                        $("#editBtnSave").css("display", "block");
                        $.fn.colorbox.close();
                    }
                }, "json");
            }
        });
        $("#editBtnCancel").click(function(){
            $(this).unbind('click');
            if(isAddedLevel){
                var parent = node.parent;
                node.remove();
                parent.render();
            }
            $.fn.colorbox.close();
        });
    }

    function isForEadContent(key){
        return (key.charAt(0) == 'e');
    }