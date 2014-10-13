<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div >  
	<table class="defaultlayout fullWidth">
		<tr>
			<th><s:text name="topic.property"/></th>
			<th><s:text name="topic.description"/></th>
			<th><s:text name="content.message.sg"/></th>			
			<th><s:text name="topic.mapping.keyword"/></th>
			<th><s:text name="content.message.actions" /></th>
		</tr>
		<c:forEach var="topicMapping" items="${topicMappings}">
		<tr>
			<td><c:out value="${topicMapping.topic.propertyKey}"/></td>
			<td><c:out value="${topicMapping.topic.description}"/></td>
			<td>
				<c:if test="${!empty topicMapping.sourceGuide}">
					<b><c:out value="${topicMapping.sourceGuide.identifier}"/></b>: <c:out value="${topicMapping.sourceGuide.title}"/>
				</c:if>
			</td>
			<td><c:out value="${topicMapping.controlaccessKeyword}"/></td>
			<td>
								<s:form action="editTopicMapping" theme="simple">
									<input type="hidden" name="id"   value="${topicMapping.id}"/>
									<s:submit key="content.message.edit" name="edit"/>
								</s:form>				
								<s:form action="deleteTopicMapping" theme="simple">
									<input type="hidden" name="id"   value="${topicMapping.id}"/>
									<s:submit key="content.message.delete" name="delete"/>
								</s:form>					
			</td>
		</tr>
		</c:forEach>
		<tr>
			<td colspan="5">								<s:form action="addTopicMapping" theme="simple">
									<s:submit key="content.message.add" name="add"/>
								</s:form>	</td>
		</tr>
	</table>

</div>

