<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script type="text/javascript">
    var continueLoop = true;
    $(document).ready(function() {
        var loadUrl = "${pageContext.request.contextPath}/superUserIndexAll.action";
        var data = null;
        $("#indexAll").click(function() {
            $("#stopAll").css("display", "block");
            $("#indexAll").css("display", "none");
            $("#introduction").html("Please wait patiently... It will take quite some time to finish...");
            doIndexingLoop(loadUrl, data);
        });
        $("#stopAll").click(function() {
            $("#stopAll").css("display", "none");
            $("#introduction").html("We are closing the connection, please wait...");
            continueLoop = false;
        });
    });
    function doIndexingLoop(loadUrl, data){
        $.post(loadUrl, data, function(databack) {
            $("#ajaxContent").append(databack.result).append("<br/>");
            if(databack.continueIndexing != null){
                if(continueLoop) {
                    doIndexingLoop(loadUrl, data);
                } else {
                    $("#introduction").html("Stopped - you can restart it when you want.");
                    $("#indexAll").css("display", "block");
                }
            } else {
                $("#introduction").html("Your batch is finished!");
            }
        }, 'json');
    }
</script>
<div id="fileList">
    Ok, you are admin user!<br/>
    <br/>
    Some quick stats for GA:<br/>
    <table style="float:left;" border="1">
        <thead>
            <tr>
                <td>Country</td>
                <td colspan="6">Counters</td>
            </tr>
        </thead>
    <c:forEach var="entry" items="${allDataPerCountry}">
        <tr>
            <td>${entry.key}</td>
            <c:forEach var="secondEntry" items="${entry.value}" varStatus="ind">
                <td>
                    ${secondEntry.key}: ${secondEntry.value}
                </td>
            </c:forEach>
        </tr>
    </c:forEach>
    </table>
</div>
<div id="buttonIndex" style="clear:both; padding-top:20px;">
    <button id="indexAll">Index All validated content</button>
    <button id="stopAll" style="display:none;">Stop Indexing All</button>
</div>
<div id="introduction"></div>
<div id="ajaxContent"></div>