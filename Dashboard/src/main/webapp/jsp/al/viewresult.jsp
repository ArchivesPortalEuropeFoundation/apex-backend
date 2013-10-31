<%@page import="eu.apenet.dashboard.manual.ManualHTTPUploader"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
        <div id="fileList">
        <s:actionmessage/>
        	<s:property value="getText('content.message.summary')" />
        </div>
        <p>
			<s:if test="totalInstitutions.size()>0" >
	        	<div id="totalInstitutionsList">
	        		<s:property value="getText('content.message.correctly.total')" />
	            	<br>
					<select name="listUploaded" multiple="multiple" size="10" disabled="disabled" style="background-color:white;width:300px;">
	        			<s:iterator value="totalInstitutions" var="institution">
	        				<option value="<s:property value="#institution.ainame" />"><s:property value="#institution.ainame" /></option>
	        			</s:iterator>
	        		</select>
	        	</div>
	        </s:if>
        </p>
        <p>
	        <s:if test="insertedInstitutions.size()>0" >
	        	<div id="insertedInstitutions">
	        		<s:property value="getText('content.message.correctly.inserted')" />
	            	<br>
					<select name="listUploaded" multiple="multiple" size="10" disabled="disabled" style="background-color:white;width:300px;">
	        			<s:iterator value="insertedInstitutions" var="institution">
	        				<option value="<s:property value="#institution.ainame" />"><s:property value="#institution.ainame" /></option>
	        			</s:iterator>
	        		</select>
	        	</div>
	        </s:if>
        </p>
        <p>
	        <s:if test="updatedInstitutions.size()>0" >
	        	<div id="updatedInstitutions">
	        		<s:property value="getText('content.message.correctly.updated')" />
	            	<br>
					<select name="listUploaded" multiple="multiple" size="10" disabled="disabled" style="background-color:white;width:300px;">
	        			<s:iterator value="updatedInstitutions" var="institution">
	        				<option value="<s:property value="#institution.ainame" />"><s:property value="#institution.ainame" /></option>
	        			</s:iterator>
	        		</select>
	        	</div>
	        </s:if>
		</p>
		<p>
			<s:if test="deletedInstitutions.size()>0" >
	        	<div id="deletedInstitutionsList">
	        		<s:property value="getText('content.message.not.correctly.deleted')" />        	
	            	<br>
					<select name="deletedInstitutions" multiple="multiple" size="10" disabled="disabled" style="background-color:white;width:300px;">
	        			<s:iterator value="deletedInstitutions" var="institution">
	        				<option value="<s:property value="#institution.ainame" />"><s:property value="#institution.ainame" /></option>
	        			</s:iterator>
	        		</select>
	        	</div>
	        </s:if>
        </p>
        <p>
			<s:if test="notChangedInstitutions.size()>0" >
	        	<div id="notChangedInstitutions">
	        		<s:property value="getText('content.message.not.correctly.notchanged')" />        	
	            	<br>
					<select name="deletedInstitutions" multiple="multiple" size="10" disabled="disabled" style="background-color:white;width:300px;">
	        			<s:iterator value="notChangedInstitutions" var="institution">
	        				<option value="<s:property value="#institution.ainame" />"><s:property value="#institution.ainame" /></option>
	        			</s:iterator>
	        		</select>
	        	</div>
	        </s:if>
        </p>
        <p>
	        <s:form theme="simple" action="ALUploadForm.action" id="returnFrm" method="post">
	        	<s:submit key="dashboard.menu.uploadcontent" theme="simple" action="upload" />
	        </s:form>
        </p>