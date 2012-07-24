<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<script type="text/javascript">
    $(document).ready(function() {
        var continueLoop = false;
        var dataRetrieved;
        var totalFa = 0;
        var typeBatch = ""; //Can be "search", "small", "full"
        var loadUrlMain = "${pageContext.request.contextPath}/batchActionAjax.action";

        $("input[name='batchOpts']").click(function(){
            $("#start").css("display", "inline");
            typeBatch = $(this).val();
            if(typeBatch == "full"){
                $("#fa_nb").html(${nbFindingAidForBatch});
            } else if(typeBatch == "search") {
                $("#fa_nb").html(${nbSearchFindingAidForBatch});
            } else {
                $("#fa_nb").html(${nbSelectedFindingAidForBatch});
            }
        });

        $("#start").click(function() {
            hideCloseButton();
            if(typeBatch == "") {
                alert("Please fill in the type of batch"); //Should never happen
            } else {
                var data;
                continueLoop = true;
                if(dataRetrieved == null) {
                    if(typeBatch == "full"){
                        totalFa = ${nbFindingAidForBatch};
                    } else if(typeBatch == "search") {
                        totalFa = ${nbSearchFindingAidForBatch};
                    } else {
                        totalFa = ${nbSelectedFindingAidForBatch};
                    }
                    data = {type: "${type}", aiId: "${aiId}", nbFindingAid: totalFa, typeBatch: typeBatch};
                } else {
                    data = dataRetrieved;
                }

                $("#abort").css("display", "inline");
                $("#start").css("display", "none");

                callajax(loadUrlMain, data, typeBatch);
            }
        });

        function hideCloseButton(){
            $("#cboxClose").unbind();
            $("#cboxClose").css("display", "none");
        }

        $("#abort").click(function() {
            stop();
        });

        $("#cboxClose").click(function(){
            if(typeBatch == "full" && '${nbFindingAidForBatch}' == 0)
                location.reload(true);
            else if(typeBatch == "small" && '${nbSelectedFindingAidForBatch}' == 0)
                location.reload(true);
            else if(typeBatch == "search" && '${nbSearchFindingAidForBatch}' == 0)
                location.reload(true);
            else if(!continueLoop)
                location.reload(true);
            else
                stop();
        });

        appendProgressBar();

        function callajax(loadUrl, data, typeBatch){
            $.post(loadUrl,data,function(databack) {
                $("#fa_nb").html("");
                var doClose = true;
                if(databack != null){
                    if(databack.error){
                        $("#mytext").html("Error parsing your request... Sorry, please try again or contact the core team:<br/>" + databack.error);
                        $("#cboxClose").css("display", "inline");
                        $("#abort").css("display", "none");
                        $("#progressbar").css("display", "none");
                        $("#progresstext").css("display", "none");
                        continueLoop = false;
                        doClose = false;
                        $("#cboxClose").bind("click", function() {
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
                    location.reload(true);
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
            $("#mytext").html("Cleaning up and closing the batch processing in a minute...");
            continueLoop = false;
        }

        if('${nbSearchFindingAidForBatch}' == 0)
            $("#batchOpts_search").css("display", "none");
        if('${nbSelectedFindingAidForBatch}' == 0)
            $("#batchOpts_small").css("display", "none");
        if('${nbFindingAidForBatch}' == 0)
            $("#batchOpts_normal").css("display", "none");

        if('${nbSearchFindingAidForBatch}' == 0 && '${nbSelectedFindingAidForBatch}' == 0 && '${nbFindingAidForBatch}' == 0) {
        	if ('${harvesting}' != 1) {
                $("#mytext").html("There are no finding aids eligible for this batch<br/><br/>(This popup will close automatically)");
                setTimeout(closeColorbox, 3000);
        	}
        }

        function closeColorbox() {
            $.fn.colorbox.close();
        }

        function appendProgressBar(){
        	var indexing = '${indexing}';
        	var harvesting = '${harvesting}';
        	var type = '${type}';
        	if (type == "indexing"){
        		if (indexing != 1){
                    $("#mytext").append("<div id=\"progressbar\"></div>");
                    $("#mytext").append("<div id=\"progresstext\"></div>");
        		}
        	}else{
        		if(harvesting != 1){
                    $("#mytext").append("<div id=\"progressbar\"></div>");
                    $("#mytext").append("<div id=\"progresstext\"></div>");
        		}
        	}        	     	
        }
    });
</script>
<h3 id="typeBatch"><s:property value="type"/></h3>
<s:if test=" harvesting==1 || indexing==1">
	<s:if test="indexing == 1">
		<span id="mytext">
	    	<s:property value="getText('content.message.queue.schedulingNotPossible')"></s:property><br/>
		</span>
	</s:if>
	<s:else>
		<span id="mytext">
	    	<s:property value="getText('content.message.harvesting')"/><br/>
		</span>		
	</s:else>
</s:if>
<s:else>
	<span id="mytext">
	    Number of Finding Aids left to be used in the batch: <span id="fa_nb">?</span>.<br/>
	</span>
	
	<br/>
	
	<span id="batchOpts_normal"><input type="radio" name="batchOpts" value="full" /> All eligible files (<s:property value="nbFindingAidForBatch"/> files)<br/></span>
	<span id="batchOpts_small"><input type="radio" name="batchOpts" value="small" /> Only selected files (<s:property value="nbSelectedFindingAidForBatch"/> files)<br/></span>
	<span id="batchOpts_search"><input type="radio" name="batchOpts" value="search" /> Only searched files (<s:property value="nbSearchFindingAidForBatch"/> files)<br/></span>
	<br/>
	<input type="button" id="start" value="Start batch" style="display:none;"/>
	<input type="button" id="abort" value="Stop batch" style="display:none;"/>
</s:else>
