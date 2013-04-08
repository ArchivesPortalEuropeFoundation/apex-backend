<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<script src="${pageContext.request.contextPath}/js/jquery/jquery_1.4.2.min.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/js/jquery/jquery-ui-1.8.9.custom.min.js" type="text/javascript"></script>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/cssnew.css" type="text/css"/>

<script type="text/javascript">
    $(document).ready(function() {
        var continueLoop = false;
        var dataRetrieved;
        var loop = 0;
        var totalFa = 0;
        var typeBatch = ""; //Can be "search", "small", "full"
        var loadUrlMain = "${pageContext.request.contextPath}/batchActionAjax.action";
        $("#cboxClose").unbind();
        $("#cboxClose").css("display", "none");
        if('${nbFindingAidForBatch}' == 0){
            $("#start").css("display", "none");
        }

        $("#start").click(function() {
            hideCloseButton();
            typeBatch = "full";
            var data;
            continueLoop = true;
            if(dataRetrieved == null){
                data = { type: "${type}", 
                		aiId: "${aiId}",
                		nbFindingAid: '${nbFindingAidForBatch}',
                        typeBatch: typeBatch
                		};
                totalFa = ${nbFindingAidForBatch};
            } else {
                data = dataRetrieved;
            }

            $("#abort").css("display", "inline");
            $("#start").css("display", "none");
            $("#start_small").css("display", "none");
            $("#start_search").css("display", "none");

            var load = loadUrlMain;
            callajax(load, data, typeBatch);
        });

        $("#start_small").click(function() {
            hideCloseButton();
            typeBatch = "small";
            var data;
            continueLoop = true;
            if(dataRetrieved == null){
                data = { type: "${type}",
                		aiId: "${aiId}",
                		nbFindingAid: '${nbSelectedFindingAidForBatch}',
                        typeBatch: typeBatch
                		};
                totalFa = ${nbSelectedFindingAidForBatch};
            } else {
                data = dataRetrieved;
            }

            $("#abort").css("display", "inline");
            $("#start").css("display", "none");
            $("#start_small").css("display", "none");
            $("#start_search").css("display", "none");

            var load = loadUrlMain;
            callajax(load, data, typeBatch);
        });

        $("#start_search").click(function() {
            hideCloseButton();
            typeBatch = "search";
            var data;
            continueLoop = true;
            if(dataRetrieved == null){
                data = { type: "${type}",
                		aiId: "${aiId}",
                		nbFindingAid: '${nbSearchFindingAidForBatch}',
                        typeBatch: typeBatch
                		};
                totalFa = ${nbSearchFindingAidForBatch};
            } else {
                data = dataRetrieved;
            }

            $("#abort").css("display", "inline");
            $("#start").css("display", "none");
            $("#start_small").css("display", "none");
            $("#start_search").css("display", "none");

            var load = loadUrlMain;
            callajax(load, data, typeBatch);
        });

        function hideCloseButton(){
            $("#cboxClose").unbind();
            $("#cboxClose").css("display", "none");
        }

        $("#abort").click(function() {
            stop();
        });

        $("#cboxClose").click(function(){
            if(typeBatch == "full" && '${nbFindingAidForBatch}' == 0){
//                $.fn.colorbox.close();
                location.reload(true);
            } else if(typeBatch == "small" && '${nbSelectedFindingAidForBatch}' == 0) {
//                $.fn.colorbox.close();
                location.reload(true);
            } else if(typeBatch == "search" && '${nbSearchFindingAidForBatch}' == 0){
//                $.fn.colorbox.close();
                location.reload(true);
            } else if(!continueLoop) {
//                $.fn.colorbox.close();
                location.reload(true);
            } else {
                stop();
            }
        });

        $("#mytext").append("<div id=\"progressbar\"></div>");
        $("#mytext").append("<div id=\"progresstext\"></div>");


        function callajax(loadUrl, data, typeBatch){
            $.post(loadUrl,data,function(databack) {
                $("#fa_nb").html("");
                var doClose = true;
                if(databack != null){
                    if(databack.error){
                        $("#mytext").html("<s:property value="getText('batch.err')" />" + "<br/>" + databack.error);
                        $("#cboxClose").css("display", "inline");
                        $("#abort").css("display", "none");
                        $("#progressbar").css("display", "none");
                        $("#progresstext").css("display", "none");
                        continueLoop = false;
                        doClose = false;
                        $("#cboxClose").bind("click", function(){
//                            $.fn.colorbox.close();
                            location.reload(true);
                        });
                    } else if(databack.nbFindingAid == 0){
                        stop();
                    } else {
                        dataRetrieved = createPostData(databack);
                        $(databack.nbFindingAid).appendTo("#fa_nb");
                        var pourcentage = Math.floor(100 * (totalFa - databack.nbFindingAid) / totalFa);
                        $("#progressbar").progressbar({ value : pourcentage });
                        $("#progresstext").html("<p>" + (totalFa - databack.nbFindingAid) + "/" + totalFa + " (" + pourcentage + "%)</p>");
                    }
                }

                if(continueLoop){
                    callajax(loadUrlMain, dataRetrieved, typeBatch);
                } else if(doClose){
                    parent.location.reload(true);
//                    $.fn.colorbox.close();
                }
            },'json');
        }
        function createPostData(json){
        	return { type: json.type,
        		aiId: json.aiId,
        		nbFindingAid: json.nbFindingAid,
                typeBatch: json.typeBatch
        		};
        }


        function stop(){
            $("#mytext").html("<s:property value="getText('batch.cleaning=')" />");
            continueLoop = false;
        }

        var nbStringSelected = "";
        var nbStringSearch = "";
        if('${nbSearchFindingAidForBatch}' == '0'){
            $("#start_search").css("display", "none");
        } else {
            nbStringSearch = " / ${nbSearchFindingAidForBatch} (searched files)";
        }
        if('${nbSelectedFindingAidForBatch}' == '0'){
            $("#start_small").css("display", "none");
        } else {
            nbStringSelected = " / ${nbSelectedFindingAidForBatch} (selected files)";
        }
        if('${nbFindingAidForBatch}' == '0'){
            $("#start").css("display", "none");
        }
        if(nbStringSelected != ""){
            $("#fa_nb").append(nbStringSelected);
        }
        if(nbStringSearch != ""){
            $("#fa_nb").append(nbStringSearch);
        }
    });
</script>
</head>
<body>
<h3 id="typeBatch"><s:property value="type"/></h3>
<span id="mytext">
    Number of Finding Aids left to be used in the batch: <span id="fa_nb">${nbFindingAidForBatch} (total)</span>.<br/>
</span>

<br/><br/><br/>

<input type="button" id="start" value="Start batch (All eligible files)" />
<input type="button" id="start_small" value="Start batch (Only selected files)" />
<input type="button" id="start_search" value="Start batch (Only files searched)" />
<input type="button" id="abort" value="Stop batch" style="display:none;"/>


</body>
</html>
