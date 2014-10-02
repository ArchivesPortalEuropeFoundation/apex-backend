<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<script type="text/javascript">
    var keyOfElements = 1;
    function bindContextMenu(emptyTitle) {
        $("#myMenu .edit a").unbind();
        $("#myMenu .add a").unbind();
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
        	 $.colorbox(
   	            {
   	            	width:"30%",
   	                height:"160px",
   	                inline:true,
   	                overlayClose:false,
   	                escKey:false,
   	                onOpen:function(){
   	                    $(document).unbind('keydown.cbox_close');
   	                    $("#myMenu").hide();
   	                },
   	                onLoad:function(){
   	                    $('#cboxClose').remove();
   	                    doConfirm();
   	                },
   	                onCleanup:function(){ $("#myMenu").show(); },
   	                href:"#deleteColorBox"
   	            }
   	        );
        }); 
         
    }

    function doConfirm() {
		unbindDeleteBtn();
        $("#deleteBtnYes").click(yesFn);
        $("#deleteBtnNo").click(noFn);
    }

    function unbindDeleteBtn(){
        $("#deleteBtnYes").unbind('click');
        $("#deleteBtnNo").unbind('click');
    }

    function yesFn() {
        getResponse();
        $("#html").show();
    }

    function noFn() {
        $.fn.colorbox.close();
    }
 
    function getResponse() {
	    var node = $("#tree").dynatree("getActiveNode");
	    $.post("${pageContext.request.contextPath}/deleteLevelHG.action", {key: node.data.key}, function(databack){
	        if(databack.success){
	            var parent = node.parent;
	            node.remove();
	            parent.render();
	        }
	    });
	        $.fn.colorbox.close();
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
            if(confirmAndDecode('<s:property value="getText('dashboard.hgcreation.areyousuredelete')" />')){
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
            var params = {name: $("#editTitle").val(), identifier: $("#editIdentifier").val(), desc: $("#editDesc").val()};
            if (!isAddedLevel){
            	params["key"]=node.data.key;
            }
            if(isForEadContent(node.data.key)){
                $("#waitImg").css("display", "inline");
                $("#editBtnSave").css("display", "none");
                
                $.post("${pageContext.request.contextPath}/addEadContentData.action", params , function(databack){
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
                params["parentId"]=node.parent.data.key;
                $.post("${pageContext.request.contextPath}/addCLevelData.action", params, function(databack){
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
                $("#tree").dynatree("getTree").activateKey(parent.data.key);
            }
            $.fn.colorbox.close();
        });
    }

    function isForEadContent(key){
        return (key.charAt(0) == 'e');
    }

    function createTreeWithoutData(emptyTitle){
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
                        createEditPanel($("#tree").dynatree("getActiveNode"), false, true);
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

    $(document).ready(function() {
    	var emptyTitle = $("#emptyTitle").val();
       
        createTreeWithoutData(emptyTitle);

        unbindContextMenu();
        bindDefaultContextMenu();
    });

    function bindDefaultContextMenu() {
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
        width: auto;
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
        display: inline-block;
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
                <li data="isFolder: true, isLazy: false, title: '<s:property value="getText(\'dashboard.hgcreation.label.emptyTitle\')" />', key: 'ec_0'" id="0"></li>
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
        <div id="editColorbox" class="colorboxLeft">
            <form>
                <table>
                    <tr>
                        <td colspan="2" align="center"><b><s:property value="getText('dashboard.hgcreation.label.editdata')" /></b></td>
                    </tr>
                    <tr id="unitid-input">
                        <td class="left"><s:property value="getText('dashboard.hgcreation.label.identifier')" /> <s:property value="getText('dashboard.hgcreation.label.unitid')" /></td>
                        <td class="right"><input type="text" id="editIdentifier" name="identifier" /></td>
                    </tr>
                    <tr>
                        <td class="left"><s:property value="getText('dashboard.hgcreation.label.title')" /> <s:property value="getText('dashboard.hgcreation.label.unittitle')" /></td>
                        <td class="right"><input type="text" id="editTitle" name="name" /></td>
                    </tr>
                    <tr>
                        <td class="left"><s:property value="getText('dashboard.hgcreation.label.description')" /> <s:property value="getText('dashboard.hgcreation.label.scopecontent')" /></td>
                        <td class="right"><textarea id="editDesc" name="desc" cols="" rows="" style="width:100%; height:200px;"></textarea></td>
                    </tr>
                </table>
                <input type="button" id="editBtnSave" value="<s:property value="getText('dashboard.hgcreation.sbm.btn.save')" />" />
                <input type="button" id="editBtnCancel" value="<s:property value="getText('dashboard.hgcreation.sbm.btn.cancel')" />" />
                <img id="waitImg" style="display:none;" src="images/colorbox/loading.gif" />
            </form>
        </div>
    </div>

    <div style="display:none;">
    	<div id="deleteColorBox" class="colorboxLeft">
    		<table>
    		<tr>
					<td colspan="2" align="left">
						<div><br/></div>
					</td>
				</tr>    
    			<tr>
					<td colspan="2" align="left">
						<div id="question"><s:property value="getText('dashboard.hgcreation.areyousuredeletechildren')" /></div>
					</td>
				</tr>  
				<tr>
					<td colspan="2" align="left">
						<div><br/></div>
					</td>
				</tr>    
    			<tr>
					<td class="left">
           		 		<input type="button" id="deleteBtnYes" value="<s:property value="getText('content.message.yes')" />" />
					</td>					
					<td class="left">
			            <input type="button" id="deleteBtnNo" value="<s:property value="getText('ead2ese.content.no')" />" />
					</td>
				</tr>    			
    		</table>
		</div>
    </div>

    <input type="hidden" id="emptyTitle" value="<s:property value="getText('dashboard.hgcreation.label.emptyTitle')" />"/>
</div>