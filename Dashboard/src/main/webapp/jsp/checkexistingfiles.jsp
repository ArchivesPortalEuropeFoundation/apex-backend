<%@ page import="eu.apenet.commons.types.XmlType" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

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
	        <div>
	        <s:submit key="label.accept" action="checkexistingfiles"/>&nbsp;
	        <s:submit key="label.cancel" style="padding-top:0px;" action="cancelCheckexistingfiles"/>
	        </div>
		</s:form>        
	</s:if>
	<s:else>
		<!-- Redirection to content.action. It is mapped in struts.xml, so it is not necessary to do anything here -->
	</s:else>
<div style="margin-bottom:30px;"></div>