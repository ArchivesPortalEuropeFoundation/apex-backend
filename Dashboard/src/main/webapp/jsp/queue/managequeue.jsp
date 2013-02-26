<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<div id="manageQueue">
	<table class="defaultlayout">
		<tr>
			<th><s:text name="admin.queuemanagement.queue.totalnumber" /></th>
			<td>${numberOfItemsInQueue}</td>
		</tr>
		<tr>
			<th><s:text name="admin.queuemanagement.queue.active" /></th>
			<td>${queueActive}</td>
		</tr>
	</table>
	<s:form action="startStopQueue" method="post">
		<s:actionerror />
		<c:choose>
			<c:when test="${queueActive}">
				<s:submit key="admin.queuemanagement.queue.stop"
					cssClass="mainButton" name="startButton" />
			</c:when>
			<c:otherwise>
				<s:submit key="admin.queuemanagement.queue.start"
					cssClass="mainButton" name="startButton" />
			</c:otherwise>
		</c:choose>

	</s:form>
	<h2>First items:</h2>
	<table class="defaultlayout">
		<thead>
			<tr>
				<th><s:text name="admin.queuemanagement.eadid" /></th>
				<th><s:text name="admin.queuemanagement.institution.name" /></th>
				<th><s:text name="admin.queuemanagement.queue.action" /></th>
				<th><s:text name="admin.queuemanagement.queue.priority" /></th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="item" items="${firstItems}">
				<tr>
					<td><c:out value="${item.ead.eadid}" /></td>
					<td><c:out value="${item.ead.archivalInstitution.ainame}" /></td>
					<td><c:out value="${item.action}" /></td>
					<td><c:out value="${item.priority}" /></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<h2>Items in the queue with errors:</h2>
	<table class="defaultlayout">
		<thead>
			<th><s:text name="admin.queuemanagement.eadid" /></th>
			<th><s:text name="admin.queuemanagement.institution.name" /></th>
			<th><s:text name="admin.queuemanagement.queue.action" /></th>
			<th><s:text name="admin.queuemanagement.queue.priority" /></th>
			<th><s:text name="admin.queuemanagement.queue.actions" /></th>
			<th><s:text name="admin.queuemanagement.queue.errors" /></th>

		</thead>
		<tbody>
			<c:forEach var="item" items="${itemsWithErrors}">
				<tr>
					<td><c:out value="${item.ead.eadid}" /></td>
					<td><c:out value="${item.ead.archivalInstitution.ainame}" /></td>
					<td><c:out value="${item.action}" /></td>
					<td><c:out value="${item.priority}" /></td>
					<td><s:form action="deleteQueueItem" theme="simple">
							<input type="hidden" name="queueItemId" value="${item.id}" />
							<s:submit key="content.message.delete.queue"></s:submit>
						</s:form></td>
					<td><pre><c:out value="${item.errors}" /></pre></td>

				</tr>
			</c:forEach>
		</tbody>
	</table>

</div>
