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
	       			</s:iterator>
	        	</div>
	        	<br>
	        	<br>
	        	<br>		
			</s:if>
			<s:if test="existingNewXmlFilesUploaded.size()>0" >
	        	<div id="existingNewXmlFilesUploadedList">
	            	<strong><s:property value="getText('content.message.filesselection')"/></strong>
	            	<br></br>
	            	<br></br>
                       <s:set name="endVariable"><s:property value="existingNewXmlFilesUploaded.size()"/></s:set>
                       <s:if test="#endVariable > 3000"><s:set name="endVariable">3000</s:set></s:if>
	        		<s:iterator value="existingNewXmlFilesUploaded" status="stat" var="row" begin="0" end="#endVariable - 1">
	        			<s:if test="#row.eadType=='Holdings Guide'">
	        			<!-- '2':'EAC_CPF' is disabled for the moment -->
	        				<s:select list="#{'0':'Finding Aid', '1':'Holdings Guide', '3':'Source Guide'}" label="%{(#stat.index+1) + '- ' + top.fileName}" name="filesTypeAnswers" value="1"></s:select>
	        				<%--<s:label label="%{(#stat.index+1) + '- ' + top.fileName}"></s:label>		        				--%>
	        				<%--<s:hidden name="filesTypeAnswers" value="1"></s:hidden>--%>
	        			</s:if>
	        			<s:elseif test="#row.eadType=='Finding Aid'">
	        			<!-- '2':'EAC_CPF' is disabled for the moment -->
	        				<s:select list="#{'0':'Finding Aid', '1':'Holdings Guide', '3':'Source Guide'}" label="%{(#stat.index+1) + '- ' + top.fileName}" name="filesTypeAnswers" value="0"></s:select>
	        				<%--<s:label label="%{(#stat.index+1) + '- ' + top.fileName}"></s:label>--%>
	        				<%--<s:hidden name="filesTypeAnswers" value="0"></s:hidden>--%>
	        			</s:elseif>
	        			<s:else> <!-- '2':'EAC_CPF' is disabled for the moment -->
	        				<s:select list="#{'0':'Finding Aid', '1':'Holdings Guide', '3':'Source Guide'}" label="%{(#stat.index+1) + '- ' + top.fileName}" name="filesTypeAnswers" value="0"></s:select>
	        			</s:else>
	       				<s:hidden name="existingNewXmlFilesUploaded[%{#stat.index}].fileId" value="%{top.fileId}"></s:hidden>
	       				<s:hidden name="existingNewXmlFilesUploaded[%{#stat.index}].fileType" value="%{top.fileType}"></s:hidden>
	       				<s:hidden name="existingNewXmlFilesUploaded[%{#stat.index}].fileName" value="%{top.fileName}"></s:hidden>		        			
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