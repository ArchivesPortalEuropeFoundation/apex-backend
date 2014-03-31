<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
        <div >
			<p id="formP">
				<s:form id="uploadALForm" action="ALUpload.action" method="post" enctype= "multipart/form-data">
					<s:actionmessage/>
					<div>
						<label id="httpFileLabel" for="httpFile" ><s:property value="getText('label.filetoupload')" /></label>
						<s:file id="httpFile" theme="simple" name="httpFile" style="margin-left:3px;"/>
						<input type="button" id="uploadActionButton" value="<s:property value='getText("label.upload")' />" style="margin-left:6px;" onclick="checkAndUpload('<s:property value="getText('label.uploadfile')" />')"/>
					</div>
				</s:form>
			</p>
        </div>
