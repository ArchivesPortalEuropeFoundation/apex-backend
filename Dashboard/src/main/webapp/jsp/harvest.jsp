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
                $("#harvestInfo").prepend("Current resumption token: " + databack.currentToken + "<br/>");
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
            $("#harvestInfo").html("Step 2/3: Preparation of EAD files began");
            getThreadLevel();
//        });
    }
    function getThreadLevel() {
        $.post("${pageContext.request.contextPath}/getThreadLevel.action", {oaiType: '${oaiType}'}, function(databack){
            if(databack != null){
                if(databack.countLeft != null){
                    $("#harvestInfo").append(databack.countLeft + " OAI-PMH files left to examine");
                    checkThreadLevel();
                }
                if(databack.fullFinished){
                    $("#harvestInfo").html(databack.numberEadContent + " finding aids have been successfully converted into EAD, you can see the files in the content manager.");
                    $("#harvestingWheel").html("");
                } else if(databack.finished){
                    $("#harvestInfo").html("Insertion of data in database is finished.<br/>Step 3/3: Conversion to data into EAD.");
                    convertToEad();
                } else {
                    $("#harvestInfo").html("Insertion of data in database failed...");
                }
            }
        }, "json");
    }
    function convertToEad(){
        $.post("${pageContext.request.contextPath}/dbToEadAjax.action", function(databack){
            if(databack != null){
                if(databack.finished){
                    $("#harvestInfo").html(databack.numberEadContent + " finding aids have been successfully converted into EAD, you can see the files in the content manager.");
                    $("#harvestingWheel").html("");
                } else {
                    $("#harvestingWheel").html("");
                    $("#harvestInfo").html("Error: Data has not been successfully converted to EAD");
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
