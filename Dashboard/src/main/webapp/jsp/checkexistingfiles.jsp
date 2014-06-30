<%@ page import="eu.apenet.commons.types.XmlType" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<input type="hidden" id="contextPath" value="${pageContext.request.contextPath}">

	<s:if test="existingNewXslFilesUploaded.size()>0 || existingNewXmlFilesUploaded.size()>0" >
       	<div id="existingNewXslFilesUploadedList">
       	<s:property value="getText('content.message.filesuploaded')"/>	       	
		</div>
		<br>
		<br>
		<br>
		<s:form id="checkexistingfiles" method="POST">
			<s:hidden name="ai_id" value="%{ai_id}"></s:hidden>
	        <s:if test="existingNewXslFilesUploaded.size()>0" >
	        	<div id="existingNewXslFilesUploadedList">
	            	<strong><s:property value="getText('content.message.xsltfiles')"/></strong>
	            	<br>
	            	<br>
	       			<s:iterator value="existingNewXslFilesUploaded" status="stat">
	       				<s:property value="%{(#stat.index+1) + '- ' + top.fileName}"/>
	       				<s:hidden name="existingNewXslFilesUploaded[%{#stat.index}].fileId" value="%{top.fileId}"></s:hidden>
	       				<s:hidden name="existingNewXslFilesUploaded[%{#stat.index}].fileType" value="%{top.fileType}"></s:hidden>
	       				<s:hidden name="existingNewXslFilesUploaded[%{#stat.index}].fileName" value="%{top.fileName}"></s:hidden>
	       				<s:hidden name="existingNewXslFilesUploaded[%{#stat.index}].filePath" value="%{top.filePath}"></s:hidden>
	       			</s:iterator>
	        	</div>
	        	<br>
	        	<br>
	        	<br>		
			</s:if>
			<s:if test="existingNewXmlFilesUploaded.size()>0" >
	        	<div id="existingNewXmlFilesUploadedList">
         				<h1><s:property value='newXmlFilesTitle'/></h1>
	            	<h3><s:property value="getText('content.message.filesselection')"/></h3>           	
	        		<s:iterator value="existingNewXmlFilesUploaded" status="stat" >
	        			<s:select list="typeSet" label="%{(#stat.index+1) + '- ' + top.fileName}" listKey="value" listValue="content" name="filesTypeAnswers" value="%{top.eadTypeId}"></s:select>
	       				<s:hidden name="existingNewXmlFilesUploaded[%{#stat.index}].fileId" value="%{top.fileId}"></s:hidden>
	       				<s:hidden name="existingNewXmlFilesUploaded[%{#stat.index}].fileType" value="%{top.fileType}"></s:hidden>
	       				<s:hidden name="existingNewXmlFilesUploaded[%{#stat.index}].fileName" value="%{top.fileName}"></s:hidden>		        			
	       				<s:hidden name="existingNewXmlFilesUploaded[%{#stat.index}].filePath" value="%{top.filePath}"></s:hidden>
	        		</s:iterator>
	        	</div>
	        	<br>
	        	<br>
	        </s:if>
	        <s:token />
		</s:form>     
        <div>&nbsp;</div>
        <div id="actions">
   	        <input type="button" key="label.accept" value="<s:property value="getText('label.accept')"/>" id="checkexistingfiles_label_accept" onclick="disableButtons('accept');"/>&nbsp;
	        <input type="button" key="label.cancel" value="<s:property value="getText('label.cancel')"/>" id="checkexistingfiles_label_cancel" style="padding-top:0px;" onclick="disableButtons('cancel');"/>
        </div> 
	</s:if>
	<s:else>
		<!-- Redirection to content.action. It is mapped in struts.xml, so it is not necessary to do anything here -->
	</s:else>
<div style="margin-bottom:30px;"></div>

<script type="text/javascript">

function disableButtons(action) {
    $("#checkexistingfiles_label_accept").attr('disabled', 'disabled');
    $("#checkexistingfiles_label_cancel").attr('disabled', 'disabled');
    var context = $("#contextPath").val();

    if (action == "accept")
    	$("form#checkexistingfiles").attr("action", context + "/checkexistingfiles.action");
    else
    	$("form#checkexistingfiles").attr("action", context + "/cancelCheckexistingfiles.action");
    
	$("form#checkexistingfiles").submit();
}
</script>