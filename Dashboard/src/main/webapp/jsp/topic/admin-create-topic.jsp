<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div>
	<script type="text/javascript">
		$(document).ready(function(){
			$("#adminTopicPosition").live("change",function(){
				checkNumeric($(this));
			});
			$("#adminTopicPosition").live("keydown",function(){
				checkNumeric($(this));
			});
		});
		function checkNumeric(jqueryBox){
			if(!$.isNumeric(jqueryBox.val())){
				var value = jqueryBox.val();
				var newValue = "";
				for(var i=0;i<value.length;i++){
					if($.isNumeric(value[i])){
						newValue+=value[i];
					}
				}
				jqueryBox.val(newValue);
			}
		}
	</script>
	<div id="actionMessages">
		<span style="color:red;font-weight:bold;"><s:actionerror /></span>
		<span style="color:green;"><s:actionmessage /></span>
	</div>
	<s:form action="adminStoreTopic" theme="simple" method="POST">
		<input type="hidden" id="validate" name="validate" value="true" />
		<div>
			<div>
			<c:if test="${topicId!=null}">
				<input type="hidden" id="topicId" name="topicId" value="<c:out value="${topicId}"/>" />
			</c:if>
			</div>
			<div>
				<div class="margin">
					<label for="adminTopicPropertyKey"><s:property value="getText('topic.property')" />*</label>
					<s:fielderror fieldName="adminTopicPropertyKey"/>
				</div>
				<div>
					<input type="text" name="adminTopicPropertyKey" value="<c:out value="${adminTopicPropertyKey}"/>" /> 
				</div>
			</div>
			<div>
				<div class="margin">
					<label for="adminTopicDescription"><s:property value="getText('topic.description')" />*</label>
					<s:fielderror fieldName="adminTopicDescription"/>
				</div>
				<div>
					<input type="text" name="adminTopicDescription" value="<c:out value="${adminTopicDescription}"/>" />
				</div>
			</div>
			<div>
				<div class="margin">
					<label for="adminTopicPosition"><s:property value="getText('admin.topic.position')" />*</label>
					<s:fielderror fieldName="adminTopicPosition"/>	
				</div>
				<div>
					<input type="text" id="adminTopicPosition" name="adminTopicPosition" value="<c:out value="${adminTopicPosition}"/>" />
				</div>
			</div>
		</div>
		<s:submit key="ingestionprofiles.save" name="add" theme="simple" />
		<input type="button" value="<s:property value="getText('ingestionprofiles.cancel')" />" onclick="location.href='adminTopic.action'" />
	</s:form>
</div>

