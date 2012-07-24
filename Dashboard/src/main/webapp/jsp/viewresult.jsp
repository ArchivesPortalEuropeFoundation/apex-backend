<%@page import="eu.apenet.dashboard.manual.ManualHTTPUploader"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
        <div id="fileList">
        <s:actionmessage/>
        	<s:property value="getText('content.message.summary')" />
        </div>
		<br>
		<br>
		<br>
		<s:if test="filesUploaded.size()>0" >
        	<div id="filesUploadedList">
        		<s:property value="getText('content.message.correctly.stored')" />
            	<br>
				<select name="listUploaded" multiple="multiple" size="10" disabled="disabled" style="background-color:white;width:300px;">
        			<s:iterator value="filesUploaded" >
        				<option value="<s:property />"><s:property /></option>
        			</s:iterator>
        		</select>
        	</div>
        	<br>
        	<br>
        </s:if>

		<s:if test="filesNotUploaded.size()>0" >
        	<div id="filesNotUploadedList">
        		<s:property value="getText('content.message.not.correctly.stored')" />        	
            	<br>
				<select name="listNotUploaded" multiple="multiple" size="10" disabled="disabled" style="background-color:white;width:300px;">
        			<s:iterator value="filesNotUploaded" >
        				<option value="<s:property />"><s:property /></option>
        			</s:iterator>
        		</select>
        	</div>
        </s:if>
       	<s:if test="%{ai_id}">
	        <s:form theme="simple" action="upload.action" id="returnFrm" method="post">
	        	<s:hidden id="ai_id" name="ai_id" value="%{ai_id}" theme="simple" />
	        	<s:submit key="dashboard.menu.uploadcontent" theme="simple" action="upload" />
	        </s:form>
        </s:if>
        