<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<div id="fileList" align = "left">
	<s:property value="getText('updateErrorFormatAL.errors')" /><br>
	<p>&nbsp;</p>
	<s:actionmessage/>
	<p>&nbsp;</p>
	<s:property value="getText('al.message.changeIdentifier.errorIdentifier')" /><br>
</div>