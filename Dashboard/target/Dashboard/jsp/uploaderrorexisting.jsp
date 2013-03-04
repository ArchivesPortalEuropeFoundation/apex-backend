<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
        <div id="fileList">
            <p>File not uploaded. It is already stored in your Dashboard.</p>
            <s:form theme="simple" action="upload.action" id="returnFrm" method="post">
	        	<s:hidden id="ai_id" name="ai_id" value="%{ai_id}" theme="simple" />
	        	<p><s:submit key="dashboard.menu.uploadcontent" theme="simple" action="upload" /></p>
	        </s:form>
        </div>
