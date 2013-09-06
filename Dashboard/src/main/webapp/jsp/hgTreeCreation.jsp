<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<script type="text/javascript">
    var keyOfElements = 1;
    function bindContextMenu() {
        $("#myMenu .edit a").unbind();
        $("#myMenu .add a").unbind();
        $("#myMenu .addFA a").unbind();
        $("#myMenu .delete a").unbind();
        $("#myMenu").find("li.disabled").removeClass('disabled');
        $("#myMenu .addFA a").colorbox(
            {
                width:"80%",
                height:"80%",
                inline:true,
                overlayClose:false,
                escKey:false,
                onOpen:function(){
                    $("#myMenu").hide();
                    $(document).unbind('keydown.cbox_close');
                    retrieveDataOfFAs();
                    createAddFAsPanel($("#tree").dynatree("getActiveNode"));
                },
                onLoad:function(){
                    $('#cboxClose').remove();
                },
                onCleanup:function(){ $("#myMenu").show(); },
                href:"#addFAColorbox"
            }
        );
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
                    var obj = [ { title: 'Empty title', isLazy: false, isFolder: true, key: 'cl_'+keyOfElements }];
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
            if(confirm('<s:property value="getText('dashboard.hgcreation.areyousuredeletechildren')" />')){
                var node = $("#tree").dynatree("getActiveNode");
                $.post("${pageContext.request.contextPath}/deleteLevelHG.action", {key: node.data.key}, function(databack){
                    if(databack.success){
                        var parent = node.parent;
                        node.remove();
                        parent.render();
                    }
                });
            }
        });
    }
    function retrieveDataOfFAs(){
        $("#allFAs").html("<img src='images/waiting.gif' />");
        $.post("${pageContext.request.contextPath}/getPossibleFAs.action","", function(databack){
            $("#allFAs").html("");
            $.each(databack, function(key, value){
                if(value != null)
                    $("#allFAs").append("<span class='block' id='"+ value.id +"'><input type='checkbox' name='possibleFAs' class='possibleFAs' value='"+ value.id +"' />" + value.eadId + ": " + value.title + "</span>");
            });
        }, "json");
    }

    function createAddFAsPanel(node){
        unbindAllBtn();
        $("#addFAsBtn").click(function(){
            $(this).unbind('click');
            var selectedFAs = "[";
            $.each($(".possibleFAs"), function() {
                if($(this).attr("checked")){
                    if(selectedFAs != "[")
                        selectedFAs += ",";
                    selectedFAs += $(this).val();
                }
            });
            selectedFAs += "]";
            $("#waitImg_addFA").css("display", "inline");
            $("#addFAsBtn").css("display", "none");

            $.post("${pageContext.request.contextPath}/addFAsToCurrentLevel.action", {key: node.data.key, selectedFAs: selectedFAs}, function(databack){
                if(databack.success){
                    $("#tree").dynatree("getActiveNode").addChild(databack.data);
                    $("#waitImg_addFA").css("display", "none");
                    node.render();
                    $("#addFAsBtn").css("display", "block");
                    $.fn.colorbox.close();
                    $("#tree").dynatree("getActiveNode").expand();
                }
            }, "json");
        });
        $("#addFAsBtnCancel").click(function(){
            $(this).unbind('click');
            $.fn.colorbox.close();
        });
    }

    function unbindContextMenu(){
        $("#myMenu .edit a").unbind();
        $("#myMenu .add a").unbind();
        $("#myMenu .delete a").unbind();
        $("#myMenu .addFA a").unbind();
        $("#myMenu").find("li").addClass('disabled');

        $("#myMenu li.delete").removeClass('disabled');
        $("#myMenu li.save").removeClass('disabled');
        $("#myMenu li.cancel").removeClass('disabled');
        $("#myMenu .delete a").click(function(){
            if(confirm('<s:property value="getText('dashboard.hgcreation.areyousuredelete')" />')){
                var node = $("#tree").dynatree("getActiveNode");
                $.post("${pageContext.request.contextPath}/deleteLevelHG.action", {key: node.data.key}, function(databack){
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
        $("#addFAsBtn").unbind('click');
        $("#addFAsBtnCancel").unbind('click');
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
                $.post("${pageContext.request.contextPath}/addEadContentData.action", {name: $("#editTitle").val(), identifier: $("#editIdentifier").val(), desc: $("#editDesc").val(), key: node.data.key}, function(databack){
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
                }, "json");
            } else {
                $("#waitImg").css("display", "inline");
                $("#editBtnSave").css("display", "none");
                $.post("${pageContext.request.contextPath}/addCLevelData.action", {name: $("#editTitle").val(), identifier: $("#editIdentifier").val(), desc: $("#editDesc").val(), key: node.data.key, parentId: node.parent.data.key}, function(databack){
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

    function createTreeWithoutData(){
        $("#tree").dynatree({
            fx: {
                height: "toggle",
                duration: 200
            },

            dnd: {
                preventVoidMoves: false,
                onDragStart: function(node) {
                    return true;
                },
                onDragEnter: function(node, sourceNode) {
                    return true;
                },
                onDrop: function(node, sourceNode, hitMode, ui, draggable) {
                    logMsg(hitMode);
                    //todo: Also do some DB changes
                    alert("<s:property value="getText('hgTreeCreation.notImplemented')" />");
                    if(hitMode == "over"){
                        node.addChild(sourceNode);
                        sourceNode.remove();
                        node.expand(true);
                    }else if(hitMode == "before" || hitMode == "after"){
                        sourceNode.move(node, hitMode);
                    }
                }
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
                        bindContextMenu();
                        $("#myMenu").show();
                    },
                    href:"#editColorbox"
                });
            },

            onClick: function(dtnode){
                logMsg("onClick");
                if(dtnode.data.isFolder === true)
                    bindContextMenu();
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

    $(document).ready(function() {
        var hgId = "${param['hgId']}";
        
        createTreeWithoutData();

        unbindContextMenu();
        bindDefaultContextMenu();
    });

    function bindDefaultContextMenu() {
        $("#myMenu .save a").click(function(){
            if(confirm('<s:property value="getText('dashboard.hgcreation.saveandquit')" />')){
                var root = $("#tree").dynatree("getRoot").getChildren()[0];
                $.post("${pageContext.request.contextPath}/createHoldingsGuide.action", {key: root.data.key}, function(databack){
                    if(databack.success){
                        alert('<s:property value="getText('dashboard.hgcreation.saved')" />');
                        window.location = '${pageContext.request.contextPath}/checkfilesuploaded.action';
                    } else {
                        alert("Oups...");
                    }
                });
            }
        });
        $("#myMenu .cancel a").click(function(){
        	window.location = '${pageContext.request.contextPath}/contentmanager.action';
        });
    }
</script>
<style type="text/css">
    #wrapper {
        width: 100%;
        height: 100%;
    }
    #left {
        width: 50%;
        float:left;
    }
    #right {
        width: 50%;
        float:left;
    }
    .contextMenu {
        position: absolute;
        width: 440px;
        z-index: 99999;
        border: solid 1px #CCC;
        background: #EEE;
        padding: 0;
        margin: 0;
    }

    .contextMenu li {
        list-style: none;
        padding: 0;
        margin: 0;
    }

    .contextMenu li.separator {
        margin: 10px 0 10px 0;
        border-top: 1px solid black;
    }

    .contextMenu a {
        color: #333;
        text-decoration: none;
        display: block;
        line-height: 20px;
        height: 20px;
        background-position: 6px center;
        background-repeat: no-repeat;
        outline: none;
        padding: 1px 5px;
        padding-left: 28px;
    }

    .contextMenu li a:hover {
        color: #FFF;
        background-color: #3399FF;
    }

    .contextMenu li.disabled a:hover {
        color: #AAA;
        cursor: default;
        background-color: #EEE;
    }

    .colorboxLeft {
        text-align: left;
    }

    span.block {
        display: block;
    }

    span.block input {
        margin-right: 5px;
    }

    #allFAs {
        height: 90%;
    }

    #saveAddFAs {
        border-top: 1px solid #4e4f4f;
        padding-top: 10px;
        padding-left: 40%;
        height: 9%;
    }

    #editColorbox form table {
        width:100%;
    }
    #editColorbox form table tr td.left {
        width:20%;
    }
    #editColorbox form table tr td.right {
        width:80%;
    }
</style>
<div id="wrapper">
    <div id="left">
        <div id="tree">
            <ul>
                <li data="isFolder: true, isLazy: false, title: 'Empty title', key: 'ec_0'" id="0"></li>
            </ul>
        </div>
    </div>
    <div id="right">
        <ul id="myMenu" class="contextMenu">
            <li class="edit">
                <a href="#edit" id="hrefEdit"><s:property value="getText('dashboard.hgcreation.btn.editcurrentlevel')" /></a>
            </li>
            <li class="add">
                <a href="#add" id="hrefAdd"><s:property value="getText('dashboard.hgcreation.btn.addnewlevel')" /></a>
            </li>
            <li class="delete">
                <a href="#delete" id="hrefDelete"><s:property value="getText('dashboard.hgcreation.btn.deletelevel')" /></a>
            </li>
            <li class="separator"></li>
            <li class="cancel">
                <a href="#cancel" id="hrefCancel"><s:property value="getText('dashboard.hgcreation.quit')" /></a>
            </li>
        </ul>
    </div>
    <div style="display:none;">
        <div id="addFAColorbox" class="colorboxLeft">
            <form>
                <div id="allFAs"></div>
                <div id="saveAddFAs"><input type="button" id="addFAsBtn" value="<s:property value="getText('dashboard.hgcreation.sbm.btn.insert')" />"/><input type="button" id="addFAsBtnCancel" value="<s:property value="getText('dashboard.hgcreation.sbm.btn.cancel')" />"/><img id="waitImg_addFA" style="display:none;" src="images/colorbox/loading.gif" /></div>
            </form>
        </div>
        <div id="editColorbox" class="colorboxLeft">
            <form>
                <table border="0">
                    <tr>
                        <td colspan="2" align="center"><b><s:property value="getText('dashboard.hgcreation.label.editdata')" /></b></td>
                    </tr>
                    <tr>
                        <td class="left"><s:property value="getText('dashboard.hgcreation.label.identifier')" /> (unitid):</td>
                        <td class="right"><input type="text" id="editIdentifier" name="identifier" /></td>
                    </tr>
                    <tr>
                        <td class="left"><s:property value="getText('dashboard.hgcreation.label.title')" /> (unittitle):</td>
                        <td class="right"><input type="text" id="editTitle" name="name" /></td>
                    </tr>
                    <tr>
                        <td class="left"><s:property value="getText('dashboard.hgcreation.label.description')" /> (scopecontent):</td>
                        <td class="right"><textarea id="editDesc" name="desc" cols="" rows="" style="width:100%; height:200px;"></textarea></td>
                    </tr>
                </table>
                <input type="button" id="editBtnSave" value="<s:property value="getText('dashboard.hgcreation.sbm.btn.save')" />" />
                <input type="button" id="editBtnCancel" value="<s:property value="getText('dashboard.hgcreation.sbm.btn.cancel')" />" />
                <img id="waitImg" style="display:none;" src="images/colorbox/loading.gif" />
            </form>
        </div>
    </div>
</div>