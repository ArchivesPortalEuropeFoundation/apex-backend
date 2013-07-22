<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
        <div >
		<s:form  method="post" enctype= "multipart/form-data">
		<s:actionmessage/>
				<s:file name="httpFile" key="label.filetoupload"/>
				<s:submit key="label.upload" action="httpUploadAL"/>
		</s:form>
        </div>
