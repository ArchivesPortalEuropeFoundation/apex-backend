<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<script type='text/javascript'>
	$(function(){
		$("#tree").dynatree({
            checkbox: true,
            fx: { height: "toggle", duration: 200 },
            
            initAjax: {
                url: "${pageContext.request.contextPath}/updateFtp.action",
                data: {parentName: ''}
            },

            onSelect: function(select, dtnode) {
                if(select) {
                    $("#contentlist").append($('<li id="node_' + dtnode.data.name + '">'+dtnode.data.title+'</li>').append($("<input type='hidden' name='filesToUpload'/>").attr("value", dtnode.data.name)));
                } else {
                    var d = document.getElementById('contentlist');
                    d.removeChild(document.getElementById('node_' + dtnode.data.name));
                }
            }, 

            onActivate: function(dtnode) {
			},

			onLazyRead: function(dtnode){
            	dtnode.appendAjax({
            	    url: "${pageContext.request.contextPath}/updateFtp.action",
		            data: {parentName: dtnode.data.name}
                });
			},

			minExpandLevel: 2
		});
	});
</script>

        <div id="treeContainer" class="dojo" align="left" style="float:left;">
            <div id="tree"></div>
        </div>
        <div id="searchContainer" class="actions dojo" style="float:right;">
            <div id="contents">
                <s:form action="saveFtpFiles" method="post" onsubmit="uploadBlackout('Downloading files...');">
                    <h3>Selected files:</h3>
                    <ul id="contentlist"/>
                    <s:hidden name="ai_id" value="%{ai_id}" />
                    <s:submit method="saveFtpFiles" value="Download those FTP files"/>
                </s:form>
            </div>
        </div>
