<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="dashboard" uri="http://dashboard.archivesportaleurope.eu/tags" %>
<dashboard:securityContext var="securityContext" />
	<script type="text/javascript">
	
	$(function() {
		$("table").tablesorter({
			debug: false,
			theme : 'tablesorter' 
		});
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
			<td style="max-width:100px"><c:out value="${topicMapping.controlaccessKeyword}"/></td>
			<td>
				<c:choose>
					<c:when test="${securityContext.countryManager}">
						<s:form theme="simple" method="GET" action="displayCreateEditTopicMappingCountryManager">
							<input type="hidden" name="topicMappingId"   value="${topicMapping.id}"/>
							<s:submit key="label.edit" name="edit" />
						</s:form>
						<s:form theme="simple" action="deleteTopicMappingCountryManager">
							<input type="hidden" name="topicMappingId"   value="${topicMapping.id}"/>
							<s:submit key="content.message.delete" name="delete" />
						</s:form>
					</c:when>
					<c:otherwise>
						<s:form theme="simple" method="GET" action="displayCreateEditTopicMapping">
							<input type="hidden" name="topicMappingId"   value="${topicMapping.id}"/>
							<s:submit key="label.edit" name="edit" />
						</s:form>
						<s:form theme="simple" action="deleteTopicMapping">
							<input type="hidden" name="topicMappingId"   value="${topicMapping.id}"/>
							<s:submit key="content.message.delete" name="delete" />
						</s:form>
					</c:otherwise>
				</c:choose>
			</td>
		</tr>
		</c:forEach>
		</tbody>
	</table>
	<c:choose>
		<c:when test="${securityContext.countryManager}">
			<s:form theme="simple"  method="GET" action="displayCreateEditTopicMappingCountryManager">
				<s:submit key="topicmapping.create" name="add" />
			</s:form>
		</c:when>
		<c:otherwise>
			<s:form theme="simple"  method="GET" action="displayCreateEditTopicMapping">
				<s:submit key="topicmapping.create" name="add" />
			</s:form>
		</c:otherwise>
	</c:choose>
</div>

