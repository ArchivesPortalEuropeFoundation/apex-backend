<%@ page import="eu.apenet.commons.types.XmlType" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>

<s:set name="xmlTypeIdEAD_FA"><%=XmlType.EAD_FA.getIdentifier()%></s:set>
<s:set name="xmlTypeIdEAD_HG"><%=XmlType.EAD_HG.getIdentifier()%></s:set>
<s:set name="xmlTypeIdEAD_SG"><%=XmlType.EAD_SG.getIdentifier()%></s:set>

<style type="text/css">
    #allInfo {
        border: 2px solid #666699;
        min-width: 500px;
        min-height: 500px;
        height: 100%;
    }
    #tree {
        overflow: auto;
        width: 200px;
        min-width: 150px;
        max-width: 450px;
    }
    .vsplitbar {
        width: 2px;
        background: #666699;
    }
    .largeOpt{
        width:100%;
    }
    #information {
        position:absolute;
        left:0;
        top:0;
        display:none;
        z-index:9999;
        width:375px;
        height:203px;
        padding:10px;
        background-color:#ffffff;
    }
    #mask {
        position:absolute;
        left:0;
        top:0;
        z-index:9000;
        background-color:#000;
        display:none;
    }
    #edition {
        overflow: auto;
    }
    .editionElement {
        padding: 5px 5px 0px 5px;
        margin: 5px;
    }

    ul#topnav {
        margin: 10 10 0 0; padding: 0;
        float:right;
        width: 100%;
        list-style: none;
        font-size: 1.1em;
    }
    ul#topnav li {
        vertical-align: middle;
        float: right;
        margin: 0; padding: 0;
        position: relative; /*--Important--*/
    }
    ul#topnav li a {
        float: right;
        /*text-indent: -9999px; *//*--Push text off of page--*/
        /*padding: 7px 5px 7px 15px;*/
        text-indent: 0;
        display: block;
        text-decoration: none;
        height: 44px;
    }
    ul#topnav li:hover a, ul#topnav li a:hover {
        background-position: right bottom;  /*--Hover State--*/
    }
    ul#topnav a.home {
        background: #7fffd4;
        width: 78px;
    }
    ul#topnav li .sub {
        position: absolute; /*--Important--*/
        top: 44px; left: -100px;
        z-index: 99999;
        background: #1fcf94; /*--Background gradient--*/
        padding: 20px 20px 20px;
        float: right;
        -moz-border-radius-bottomleft: 5px;
        -khtml-border-radius-bottomleft: 5px;
        -webkit-border-bottom-left-radius: 5px;
        -moz-border-radius-topleft: 5px;
        -khtml-border-radius-topleft: 5px;
        -webkit-border-toptom-left-radius: 5px;

        display: none; /*--Hidden for those with js turned off--*/
    }
    ul#topnav li .sub ul{
        list-style: none;
        margin: 0; padding: 0;
        width: 150px;
        float: right;
    }
    ul#topnav .sub ul li {
        width: 100%; /*--Override parent list item--*/
        color: #fff;
    }
    ul#topnav .sub ul li a {
        float: none;
        text-indent: 0; /*--Override text-indent from parent list item--*/
        height: auto; /*--Override height from parent list item--*/
        background: url(navlist_arrow.png) no-repeat 5px 12px;
        padding: 7px 5px 7px 15px;
        display: block;
        text-decoration: none;
        color: #fff;
    }
    ul#topnav .sub ul li a:hover {
        color: #cccc00;
        background-position: 5px 12px ;/*--Override background position--*/
    }
</style>

<script type="text/javascript" src="js/jquery/jquery.hoverIntent.minified.js"></script>

<script type='text/javascript'>
    var correctId = "";
    var correctFaId = "";
    var correctHgId = "";
    var xmlTypeId = ${xmlTypeId};
    $(document).ready(function() {
        showMask();

        correctFaId = ${id};
        correctHgId = ${id};
        var data = {id: ${id}, xmlTypeId: xmlTypeId};
        $("#infoStatic").html("<s:property value="getText('dashboard.editead.preparepage')" />");
        $("#infoDb").html("1. <s:property value="getText('dashboard.editead.databasepreparation')" />");
        $("#infoDb").append("<br /><img src=\"images/colorbox/loading.gif\" />");
        $.post("${pageContext.request.contextPath}/editEadCreateDbEntries.action", data, function(databack){
            if(databack != null){
                if(databack.dbEntriesCreated){
                    $("#infoDb").html("1. <s:property value="getText('dashboard.editead.databasepreparationfinished')" />");
                    $("#infoTree").html("2. <s:property value="getText('dashboard.editead.treeinitialization')" />");
                    initializeTree();
                } else {
                    $("#infoDb").html("1. <s:property value="getText('dashboard.editead.errordatabase')" />:<br />");
                    $("#infoDb").append(databack.errorMsg);
                }
            }
        }, "json");
        $("#exitButton").bind("click", function(){
            if(!$("#saveFullXml").is(":visible") || ($("#saveFullXml").is(":visible") && confirmAndDecode("<s:property value="getText('dashboard.editead.info.areyousure')" />"))){
                $("#information").html("");
                $("#information").append("<br /><img src=\"images/colorbox/loading.gif\" />");
                $("#information").css("visibility", "visible");
                $('#information').fadeIn(500);
                $('#mask').fadeIn(500);
                window.setTimeout(function() {
                    window.opener.location.reload();
                    window.close();
                }, 1000);
            }
        });
        $("#saveFullXmlButton").bind("click", function(){
            $("#information").html("<s:property value="getText('dashboard.editead.savingwait')" />");
            $("#information").append("<br /><img src=\"images/colorbox/loading.gif\" />");
            $("#information").css("visibility", "visible");
            $('#information').fadeIn(500);
            $('#mask').fadeIn(500);
            $.post("${pageContext.request.contextPath}/editEadXmlSaveAll.action", {faId: correctFaId, hgId: correctHgId, xmlTypeId: xmlTypeId}, function(databack){
                if(databack != null){
                    if(databack.saved){
                        $("#information").html("<s:property value="getText('dashboard.editead.xmlsavedsuccess')" />");
                        window.setTimeout(function() {
                            window.opener.location.reload();
                            window.close();
                        }, 3000);
                    } else {
                        $("#information").html("<s:property value="getText('dashboard.editead.errorsaving')" />");
                    }
                }
            }, "json");

        });

        var config = {
             sensitivity: 2,
             interval: 10,
             over: megaHoverOver,
             timeout: 200,
             out: megaHoverOut
        };

        $("ul#topnav li .sub").css({'opacity':'0'});
        $("ul#topnav li").hoverIntent(config);

    });

    $(window).unload(function() {
        $.post("${pageContext.request.contextPath}/editEadXmlDeleteEntries.action", {faId: correctFaId, hgId: correctHgId, xmlTypeId: xmlTypeId}, function(databack){
            console.log("We deleted the DB entries in eadcontent and clevelcontent for this FA");
        }, "json");
    });


    function showMask(){
        var maskHeight = $(document).height();
        var maskWidth = $(window).width();
        $('#mask').css({'width':maskWidth,'height':maskHeight});
        $('#mask').fadeIn(1000);
        $('#mask').fadeTo("slow",0.8);
        var winH = $(window).height();
        var winW = $(window).width();
        $('#information').css('top',  winH/2-$('#information').height()/2);
        $('#information').css('left', winW/2-$('#information').width()/2);
        $('#information').fadeIn(2000);
    }

    function maskFadeOut(){
        $("#information").fadeOut(1000);
        $('#mask').fadeOut(1000);
        $("#information").css("visibility", "hidden");
    }

    function callbackAfterTreeLoaded(){
        $('#mask').fadeOut(1000);

        $("#information").css("visibility", "hidden");
        $("#infoStatic").remove();
        $("#infoDb").remove();
        $("#infoTree").remove();

        $("#allInfo").splitter({
            splitVertical: true,
		    outline: true,
		    sizeLeft: true,
		    resizeTo: window
	    });
        $('#allInfo').css("visibility", "visible");

        $.post("${pageContext.request.contextPath}/editEadXml.action", {id: -1, faId: correctFaId, hgId: correctHgId, xmlTypeId: xmlTypeId}, function(databack){
            if(databack.xml) {
                $("#editionFormContainer").html(databack.xml);
                wireFormSubmit();
            }
        }, "json");
    }

    function wireFormSubmit(){
        $('#editionFormSubmit').unbind();
        $('#editionFormSubmit').bind('click', function() {
            $('#saveFullXml:hidden').show("fast");

            var $inputs = $('#editionForm :input');
            var values = {};

            $inputs.each(function() {
                if(this.name != "")
                    values["'" + this.name + "'"] = $(this).val();
            });

            $("#information").html("<s:property value="getText('dashboard.editead.label.saving')" />");
            $("#information").append("<br /><img src=\"images/colorbox/loading.gif\" />");
            $("#information").css("visibility", "visible");
            $('#information').fadeIn(500);
            $('#mask').fadeIn(500);

            $.post("${pageContext.request.contextPath}/editEadXmlSaveLevel.action", {id: correctId, faId: correctFaId, hgId: correctHgId, xmlTypeId: xmlTypeId, formValues: values}, function(databack){
                if(databack != null) {
                    if(databack.saved){
                        $("#information").html("<s:property value="getText('dashboard.editead.label.saved')" />");
                        window.setTimeout(function() {
                            maskFadeOut();
                        }, 1000);
                    } else {
                        $("#information").html("<s:property value="getText('dashboard.editead.label.savefailed')" />");
                        window.setTimeout(function() {
                            maskFadeOut();
                        }, 3000);
                    }
                }
            }, "json");
        });

        $("#addThisLevel").unbind();
        $("#addThisLevel").bind('click', function() {
            //Create the form in a mask
            $("#information").html("");
            createForm("thisLevel");
            $("#information").css("visibility", "visible");
            $('#information').fadeIn(500);
            $('#mask').fadeIn(500);
        });

        $("#addAllLevels").unbind();
        $("#addAllLevels").bind('click', function() {
            //Create the form in a mask
            $("#information").html("");
            createForm("allLevels");
            $("#information").css("visibility", "visible");
            $('#information').fadeIn(500);
            $('#mask').fadeIn(500);
        });

        $("#addLowLevels").unbind();
        $("#addLowLevels").bind('click', function() {
            //Create the form in a mask
            $("#information").html("");
            createForm("lowLevels");
            $("#information").css("visibility", "visible");
            $('#information').fadeIn(500);
            $('#mask').fadeIn(500);
        });
    }

    function createForm(type) {
        if(type == 'thisLevel'){
            $("#information").append("<h2><s:property value="getText('dashboard.editead.addfieldthislevel')" /></h2>");
        } else if(type == 'allLevels'){
            $("#information").append("<h2><s:property value="getText('dashboard.editead.addfieldalllevels')" /></h2>");
        } else if(type == 'addLowLevels'){
            $("#information").append("<h2><s:property value="getText('dashboard.editead.addfieldlowlevels')" /></h2>");
        }
        $.post("${pageContext.request.contextPath}/editEadGetFields.action", {id: correctId, faId: correctFaId, hgId: correctHgId, xmlTypeId: xmlTypeId, type: type}, function(databack){
            if(databack != null){
                $("#information").css("width", "750px");
                //{"langusage":"c/did/language/langusage","something":"c/did/something"}
                $("#information").append("<form id='formLevel'/>");
                $("#formLevel").append("<select id='selectLevel' name='selectLevel'/>");
                $.each(databack, function(key, value){
                    $("#selectLevel").append("<option class='largeOpt' value='" + key + "'>" + key + " (XPATH: " + value + ")</option><br/>");
                });
                $("#formLevel").append("<br/>");
                $("#formLevel").append("<s:property value="getText('dashboard.editead.label.value')" />: ");
                $("#formLevel").append("<input type='textarea' name='value' id='valueForm'/>");
                $("#formLevel").append("<br/>");
                $("#formLevel").append("<input type='button' name='cancel' value='<s:property value="getText('dashboard.editead.btn.cancel')" />' class='cancelBtn'/>");

                if(type == 'thisLevel'){
                    $("#formLevel").append("<input type='button' name='add' id='addThisLevel' value='<s:property value="getText('dashboard.editead.btn.addlevel')" />'/>");
                    wireUpSubmitBtnId("addThisLevel", type);
                } else if(type == 'allLevels'){
                    $("#formLevel").append("<input type='button' name='add' id='addAllLevels' value='<s:property value="getText('dashboard.editead.btn.addalllevels')" />'/>");
                    wireUpSubmitBtnId("addAllLevels", type);
                } else if(type == 'addLowLevels'){
                    $("#formLevel").append("<input type='button' name='add' id='addLowLevels' value='<s:property value="getText('dashboard.editead.btn.addlowlevels')" />'/>");
                    wireUpSubmitBtnId("addLowLevels", type);
                }
            }
        }, "json");
    }

    function wireUpSubmitBtnId(id, type){
        $(".cancelBtn").unbind();
        $(".cancelBtn").bind("click", function(){
            window.setTimeout(function() {
                maskFadeOut();
            }, 10);
        });

        $("#" + id).unbind();
        $("#" + id).bind("click", function(){
            var values = {};
            values["'" + $("#selectLevel").val() + "'"] = $("#valueForm").val();
            $("#information").html("<s:property value="getText('dashboard.editead.saving')" />");
            $.post("${pageContext.request.contextPath}/editEadAddField.action", {id: correctId, faId: correctFaId, hgId: correctHgId, xmlTypeId: xmlTypeId, type: type, formValues: values}, function(databack){
                if(databack != null){
                    if(databack.saved){
                        $("#information").html("<s:property value="getText('dashboard.editead.saved')" />");
                        //When saving is finished, then "reload" the current c level to show the changes
                        $.post("${pageContext.request.contextPath}/editEadXml.action", {id: correctId, faId: correctFaId, hgId: correctHgId, xmlTypeId: xmlTypeId}, function(databack2){
                            if(databack2.xml) {
                                $("#editionFormContainer").html(databack2.xml);
                                wireFormSubmit();
                            }
                        }, "json");
                    } else {
                        $("#information").html("<s:property value="getText('dashboard.editead.failedsave')" />");
                    }
                }
            }, "json");
            window.setTimeout(function() {
                maskFadeOut();
            }, 3000);
        });
    }

	function initializeTree() {
        var findingAidId = "";
        var holdingsGuideId = "";
        <s:if test="xmlTypeId == #xmlTypeIdEAD_FA">
            findingAidId = "${param['id']}";
        </s:if>
        <s:else>
            holdingsGuideId = "${param['id']}";
        </s:else>
		$("#tree").dynatree({
            fx: {
                height: "toggle",
                duration: 200
            },

            initAjax: {
                url: "${pageContext.request.contextPath}/generateTreeJSONWithoutPreface.action",
                data: {
                    findingAidId: findingAidId,
                    holdingsGuideId: holdingsGuideId,
                    isWithUrl: false
                },
                callback: callbackAfterTreeLoaded()
            },

            onSelect: function(select, dtnode){
            },

            onActivate: function(dtnode) {
                if(dtnode.data.id)
                    correctId = dtnode.data.id;
                else
                    correctId = -1;
                if(dtnode.data.more == null){
                    $.post("${pageContext.request.contextPath}/editEadXml.action", {id: correctId, faId: correctFaId, hgId: correctHgId, xmlTypeId: xmlTypeId}, function(databack){
                        if(databack.xml) {
                            $("#editionFormContainer").html(databack.xml);
                            wireFormSubmit();
                        }
                    }, "json");
                }
			},

            onLazyRead: function(dtnode){
				if (dtnode.data.more == "after"){
	            	dtnode.parent.appendAjaxWithoutRemove({
	            	    url: "${pageContext.request.contextPath}/generateTreeJSON.action",
			            data: {
			            	parentId: dtnode.data.parentId,
			            	orderId: dtnode.data.orderId,
			            	ecId: dtnode.data.ecId,
			            	more: dtnode.data.more
			            	}
	                });
                     var parent = dtnode.parent;
                     dtnode.remove();
                     var children = parent.getChildren();
                     var index = children.length-1;
                     var lastChild = children[index];
                     lastChild.focus();
                     var height = $('#tree').outerHeight();
                     $('html, body').animate({scrollTop: height}, 500);
				} else if (dtnode.data.more == "before"){
	            	dtnode.parent.insertBeforeAjaxWithoutRemove({
	            	    url: "${pageContext.request.contextPath}/generateTreeJSON.action",
			            data: {
			            	parentId: dtnode.data.parentId,
			            	orderId: dtnode.data.orderId,
			            	ecId: dtnode.data.ecId,
			            	more: dtnode.data.more,
			            	max: dtnode.data.max
			            	}
	                }, dtnode);
	                dtnode.remove();
				} else {
	            	dtnode.appendAjax({
	            	    url: "${pageContext.request.contextPath}/generateTreeJSON.action",
			            data: {parentId: dtnode.data.id}

	            	});
				}
			},

			minExpandLevel: 2
		});
	}

    function megaHoverOver(){
        $(this).find(".sub").stop().fadeTo('fast', 1).show();
        (function($) {
            jQuery.fn.calcSubWidth = function() {
                rowWidth = 0;
                $(this).find("ul").each(function() {
                    rowWidth += $(this).width();
                });
            };
        })(jQuery);

        $(this).calcSubWidth();
        $(this).find(".sub").css({'width' : rowWidth});
    }

    function megaHoverOut(){
        $(this).find(".sub").stop().fadeTo('fast', 0, function() {
            $(this).hide();
        });
    }

</script>
        <div id="information">
            <div id="infoStatic"></div>
            <div id="infoDb"></div>
            <div id="infoTree"></div>
        </div>
        <button id="exitButton"><s:property value="getText('dashboard.editead.btn.cancelandquit')" /></button>
        <div id="saveFullXml" style="display:none;">
            <button id="saveFullXmlButton"><s:property value="getText('dashboard.editead.btn.saveandquit')" /></button>
        </div>
        <div id="allInfo" style="visibility:hidden;">
            <div id="tree"></div>
            <div id="edition">
                <form id="editionForm" action="">
                    <ul id="topnav">
                        <li>
                            <a href="#" class="home"><s:property value="getText('dashboard.editead.label.add')" /></a>
                            <div class="sub">
                                <ul>
                                    <li><a href="#" id="addThisLevel"><s:property value="getText('dashboard.editead.addlevel')" /></a></li>
                                    <li><a href="#" id="addLowLevels"><s:property value="getText('dashboard.editead.addlowlevels')" /></a></li>
                                    <li><a href="#" id="addAllLevels"><s:property value="getText('dashboard.editead.addalllevels')" /></a></li>
                                </ul>
                            </div>
                        </li>
                    </ul>
                    <p id="editionFormContainer">
                    </p>
                    <input type="button" id="editionFormSubmit" value="<s:property value="getText('dashboard.editead.btn.saveleveldata')" />" />
                </form>
            </div>
        </div>
<div id="mask"></div>
