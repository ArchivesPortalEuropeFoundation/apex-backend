<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<script type='text/javascript'>
    var continueHarvest = true;
    $(document).ready(function() {
        $("#harvestingWheel").html("<img src=\"images/colorbox/loading.gif\" />");
        $("#harvestingWheel").append("<br/> <button id=\"stopHarvest\">Stop harvesting</button>");
        $("#stopHarvest").click(function(){
            continueHarvest = false;
            stopHarvest();
        });
        var data = {
            oaiUrl: '${oaiUrl}',
            oaiMetadataFormat: '${oaiMetadataFormat}',
            oaiSet: '${oaiSet}',
            oaiType: '${oaiType}',
            oaiToken: '${oaiToken}'
        };
        ajaxHarvest(data);
    });
    function stopHarvest() {
        $.post("${pageContext.request.contextPath}/stopHarvest.action", function(databack){
            if(databack != null && databack.finished == true){
                $("#harvestingWheel").html("");
            }
        });
    }
    function ajaxHarvest(data){
        $.post("${pageContext.request.contextPath}/harvesting.action", data, function(databack){
            if(databack != null){
                $("#harvestInfo").prepend("<s:property value="getText('harvest.currentTesumptionToken')" />" + databack.currentToken + "<br/>");
                if(databack.finished) {
                    continueHarvest = false;
                    checkThreadLevel();
                }

                if(continueHarvest)
                    ajaxHarvest({
                        oaiUrl: '${oaiUrl}',
                        oaiMetadataFormat: '${oaiMetadataFormat}',
                        oaiSet: '${oaiSet}',
                        oaiType: '${oaiType}',
                        oaiToken: databack.currentToken
                    });
                else
                    $("#harvestingWheel").html("<img src=\"images/colorbox/loading.gif\" />");
            }
        }, "json");
    }
    function checkThreadLevel() {
//        $("#harvestInfo").html("Harvest finished");
//        $("#harvestInfo").append("<br/><button id=\"step1\">Construct Finding Aids</button><br/>(Can be very long depending on the size of the harvest)");
//        $("#step1").click(function(){
            $("#harvestInfo").html("<s:property value="getText('harvest.step2')" />");
            getThreadLevel();
//        });
    }
    function getThreadLevel() {
        $.post("${pageContext.request.contextPath}/getThreadLevel.action", {oaiType: '${oaiType}'}, function(databack){
            if(databack != null){
                if(databack.countLeft != null){
                    $("#harvestInfo").append(databack.countLeft + " " + "<s:property value="getText('harvest.OAI-PMH')" />");
                    checkThreadLevel();
                }
                if(databack.fullFinished){
                    $("#harvestInfo").html(databack.numberEadContent + " " + "<s:property value="getText('harvest.successfullyConvertedIntoEAD')" />");
                    $("#harvestingWheel").html("");
                } else if(databack.finished){
                    $("#harvestInfo").html("<s:property value="getText('harvest.step3')" />" + "<br>" + "<s:property value="getText('harvest.step31')" />");
                    convertToEad();
                } else {
                    $("#harvestInfo").html("<s:property value="getText('harvest.insertionInDatabaseFailed')" />");
                }
            }
        }, "json");
    }
    function convertToEad(){
        $.post("${pageContext.request.contextPath}/dbToEadAjax.action", function(databack){
            if(databack != null){
                if(databack.finished){
                    $("#harvestInfo").html(databack.numberEadContent + " " + "<s:property value="getText('harvest.successfully')" />");
                    $("#harvestingWheel").html("");
                } else {
                    $("#harvestingWheel").html("");
                    $("#harvestInfo").html("<s:property value="getText('harvest.error')" />");
                }
            }
        }, "json");
    }
</script>

        <div>
            <div id="information">
                Step 1/3: OAI-PMH repository being harvested: <b><s:property value="%{oaiUrl}"/></b><br/>
                Using <b><s:property value="%{oaiMetadataFormat}"/></b> metadata format and <b><s:property value="%{oaiSet}"/></b> set.
            </div>
            <div id="actionsDone"></div>
            <div id="harvestingWheel"></div>
            <div id="harvestInfo"></div>
        </div>
