<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
        <div >
			<p id="formP">
				<s:form id="uploadALForm" action="ALDisplayTrees.action" method="post" enctype= "multipart/form-data">
					<s:actionmessage/>
					<s:label id="httpFileLabel" for="httpFile" key="label.filetoupload" theme="simple"></s:label>
					<s:file id="httpFile" theme="simple" name="httpFile" style="margin-left:3px;"/>
					<input type="button" id="uploadActionButton" value="<s:property value='getText("label.upload")' />" style="margin-left:6px;"/>
<!-- 					<div>						
						<input type="checkbox" id="checkboxPreview" checked="checked"></input>
						<label for="checkboxPreview" ><s:property value="getText('al.message.previewdifferentsbeforeupload')"/></label>
					</div> -->
				</s:form>
			</p>
			<p id="resultP">
				<div id="uploadPanelDiv"> </div>
			</p>
        </div>
