<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<div id="fileList" align = "left">
	<s:property value="getText('updateErrorFormatAL.errors')" /><br>
	<p>&nbsp;</p>
	<s:property value="getText('updateErrorFormatAL.error.contentNotPublished.final')" /><br>
	<p>&nbsp;</p>
	<s:iterator var="current" value="institutionsWithContentNotPublished" status="status">
		<p><s:property value="#current" /></p>
	</s:iterator>
</div>