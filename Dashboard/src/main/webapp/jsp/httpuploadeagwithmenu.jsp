<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
        <div >
        <s:actionmessage/>
		<s:form method="post" enctype= "multipart/form-data">
				<s:hidden name="ai_id" value="%{ai_id}"></s:hidden>
				<s:file name="httpFile" label="Select file to upload"/>
				<s:submit key="label.upload" action="eaghttpuploadwithmenu"/>				
				<s:submit key="label.cancel" action="dashboardHome"/>
		</s:form>
        </div>
