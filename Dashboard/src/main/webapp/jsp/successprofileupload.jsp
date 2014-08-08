<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<div id="divForm">
    <div style="align: center;">
        <p><s:property value="getText('dashboard.uploadcontent.successprofileupload')" /></p>
            <s:form theme="simple" action="contentmanager.action" id="returnFrm" method="post">
	        	<s:hidden id="ai_id" name="ai_id" value="%{ai_id}" theme="simple" />
	        	<p><s:submit key="dashboard.menu.contentmanager" theme="simple" /></p>
	        </s:form>
    </div>
</div>
