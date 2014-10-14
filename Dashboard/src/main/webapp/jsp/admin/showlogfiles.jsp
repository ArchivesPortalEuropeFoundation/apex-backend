<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	
	<script type="text/javascript">
	
	$(function() {
		$("table").tablesorter({debug: false});

		
	});
	</script>	
<div id="showLogFiles">
	<h2>Own log files</h2>
	<table class="defaultlayout tablesorter">
		<thead>
			<tr>
				<th><s:text name="admin.showlogfiles.filename" /></th>
				<th><s:text name="admin.showlogfiles.size" /></th>
				<th><s:text name="admin.showlogfiles.lastmodified" /></th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="logFile" items="${ownLogFiles}">
				<tr>
					<td><a href="downloadLogFile.action?filename=${logFile.filename}" ><c:out value="${logFile.filename}" /></a></td>
					<td><c:out value="${logFile.filesize}" /></td>
					<td><c:out value="${logFile.lastModified}" /></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<h2>Other log files</h2>
	<table class="defaultlayout tablesorter">
		<thead>
			<tr>
				<th><s:text name="admin.showlogfiles.filename" /></th>
				<th><s:text name="admin.showlogfiles.size" /></th>
				<th><s:text name="admin.showlogfiles.lastmodified" /></th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="logFile" items="${otherLogFiles}">
				<tr>
					<td><a href="downloadLogFile.action?filename=${logFile.filename}" ><c:out value="${logFile.filename}" /></a></td>
					<td><c:out value="${logFile.filesize}" /></td>
					<td><c:out value="${logFile.lastModified}" /></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>

</div>
