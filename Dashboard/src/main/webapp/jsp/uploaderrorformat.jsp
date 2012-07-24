<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
        <div id="fileList">
            Some errors occurred during uploading:<br>
            Please, check the file format: only ZIP, XML and XSL(T) formats are allowed.<br> 
            In addition, only files smaller than 30 Mbytes are allowed.
        </div>
        <s:form theme="simple" action="upload.action" id="returnFrm" method="post">
        	<s:hidden id="ai_id" name="ai_id" value="%{ai_id}" theme="simple" />
        	<s:submit key="dashboard.menu.uploadcontent" theme="simple" action="upload" />
        </s:form>