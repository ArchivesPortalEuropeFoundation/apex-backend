<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
	<script type="text/javascript">
	
	$(function() {
		$("table").tablesorter({debug: false});

		
	});
	</script>
<div >  
	<table class="defaultlayout fullWidth tablesorter">
	<thead>
		<tr>
			<th><s:text name="topic.property"/></th>
			<th><s:text name="topic.description"/></th>
			<th><s:text name="content.message.sg"/></th>			
			<th><s:text name="topic.mapping.keyword"/></th>
			<th><s:text name="content.message.actions" /></th>
		</tr>
		</thead>
		<tbody>
		<c:forEach var="topicMapping" items="${topicMappings}">
		<tr>
			<td><c:out value="${topicMapping.topic.propertyKey}"/></td>
			<td><c:out value="${topicMapping.topic.description}"/></td>
			<td>
				<c:if test="${!empty topicMapping.sourceGuide}">
					<c:out value="${topicMapping.sourceGuide.identifier}"/> - <c:out value="${topicMapping.sourceGuide.title}"/>
				</c:if>
			</td>
			<td><c:out value="${topicMapping.controlaccessKeyword}"/></td>
			<td>
								<s:form action="displayCreateEditTopicMapping" theme="simple">
									<input type="hidden" name="topicMappingId"   value="${topicMapping.id}"/>
									<s:submit key="label.edit" name="edit"/>
								</s:form>				
								<s:form action="deleteTopicMapping" theme="simple">
									<input type="hidden" name="topicMappingId"   value="${topicMapping.id}"/>
									<s:submit key="content.message.delete" name="delete"/>
								</s:form>					
			</td>
		</tr>
		</c:forEach>
		</tbody>
	</table>
<s:form action="displayCreateEditTopicMapping" theme="simple">
									<s:submit key="topicmapping.create" name="add"/>
								</s:form>
</div>

