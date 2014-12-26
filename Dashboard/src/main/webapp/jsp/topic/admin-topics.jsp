<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
	<script type="text/javascript">
		$(document).ready(function() {
			initAdminTopics();
		});
	</script>
<div>
	<div id="actionMessages">
		<span style="color:red;font-weight:bold;"><s:actionerror /></span>
		<span style="color:green;"><s:actionmessage /></span>
	</div>
	<c:if test="${adminTopics!=null && adminTopics.size()>0}">
	<p id="topicFooterButton">
		<s:form action="adminEditTopic" theme="simple"  method="POST">
			<s:submit key="admin.topic.create" name="add" theme="simple"/>
		</s:form>
	</p>
	<div id="pager" class="pager">
		<form>
			<label class="pageDisplayLabel" for="resultPerPage"><s:property value="getText('content.message.resultsperpage')" />: </label>
			<s:select onclick="fixPagerTop();" theme="simple" cssClass="pagesize" name="resultPerPage" list="#{'10':'10','20':'20','30':'30','50':'50','100':'100'}" />
			<div class="last" onclick="fixPagerTop();" >&gt;&gt;</div>
			<div class="next" onclick="fixPagerTop();" >&gt;</div>
			<input type="text" name="pagedisplay" class="pagedisplay" disabled="disabled" />
			<div class="prev" onclick="fixPagerTop();">&lt;</div>
			<div class="first" onclick="fixPagerTop();">&lt;&lt;</div>
		</form>
	</div>
	
	<table id="adminTopicsTable" class="defaultlayout fullWidth tablesorter">
		<thead>
			<tr>
				<th><s:property value="getText('admin.topic.id')" /></th>
				<th><s:property value="getText('topic.property')" /></th>
				<th><s:property value="getText('topic.description')" /></th> 
				<th><s:property value="getText('content.message.actions')" /></th>
			</tr>
			</thead>
			<tbody>
		<c:forEach var="topic" items="${adminTopics}">
			<tr>
				<td><c:out value="${topic.id}"/></td>
				<td><c:out value="${topic.propertyKey}"/></td>
				<td><c:out value="${topic.description}"/></td>
				<td>
					<s:form action="adminEditTopic" theme="simple" method="POST">
						<input type="hidden" name="topicId"   value="${topic.id}"/>
						<s:submit key="label.edit" name="edit"/>
					</s:form>
					<s:form action="adminDeleteTopic" theme="simple" method="POST">
						<input type="hidden" name="topicId"  value="${topic.id}"/>
						<s:submit key="content.message.delete" name="delete"/>
					</s:form>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	</c:if>
</div>

