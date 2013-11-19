<%@page import="eu.apenet.dashboard.manual.ManualHTTPUploader"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<div id="fileList">
    <s:debug />
    <s:actionmessage/>
    <s:property value="getText('content.message.summary')" />
</div>
<br>
<br>
<br>
<s:form theme="simple" action="processWithProfile" method="post">
    <s:hidden name="ingestionprofile" value="%{ingestionprofile}"></s:hidden>
    <s:if test="filesUploaded.size()>0" >
        <div id="filesUploadedList">
            <s:property value="getText('content.message.correctly.stored')" />
            <br>
            <s:select name="listUploaded" list="filesUploaded" multiple="multiple" size="10" disabled="disabled" style="background-color:white;width:300px;"/>
        </div>
        <br>
        <br>
    </s:if>

    <s:if test="filesNotUploaded.size()>0" >
        <div id="filesNotUploadedList">
            <s:property value="getText('content.message.not.correctly.stored')" />
            <br>
            <s:select name="listNotUploaded" list="filesNotUploaded" multiple="multiple" size="10" disabled="disabled" style="background-color:white;width:300px;" />
        </div>
    </s:if>
    <s:if test="%{ai_id}">
        <s:hidden id="ai_id" name="ai_id" value="%{ai_id}" theme="simple" />
    </s:if>
    <s:submit value="OK" />
</s:form>
