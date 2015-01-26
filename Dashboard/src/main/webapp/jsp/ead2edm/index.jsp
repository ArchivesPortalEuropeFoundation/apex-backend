<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<div id="whole">
<table width="100%">
	<TR>
		<TH>EAD ID</TH>
		<TH>EAD Title</TH>
		<TH>Status</TH>
		<TH>Upload Date</TH>
		<TH>Upload method</TH>
		<TH>Path</TH>
		<TH>Number of DAO</TH>
		<TH>ESE download</TH>
		<TH>ESE HTML Preview</TH>
		<TH>Europeana</TH>
	</TR>
	<c:forEach var="findingAid" items="${findingAids}">
		<tr>
			<td><c:out value="${findingAid.faEadid}" /></td>
			<td><c:out value="${findingAid.faTitle}" /></td>
			<td><c:out value="${findingAid.fileState.state}" /></td>
			<td><fmt:formatDate value="${findingAid.uploadDate}" /></td>
			<td><c:out value="${findingAid.uploadMethod.method}" /></td>
			<td><c:out value="${findingAid.pathApenetead}" /></td>
			<c:choose>
				<c:when test="${fn:length(findingAid.eses) > 0}">

					<td><c:forEach var="ese" items="${findingAid.eses}">
						<c:out value="${ese.numberOfRecords}" />
						<br />
					</c:forEach></td>
					<td><c:forEach var="ese" items="${findingAid.eses}">
						<s:a action="downloadEdm">Download
							    <s:param name="eseId">
								<c:out value="${ese.eseId}" />
							</s:param>
						</s:a>
						<br />
					</c:forEach></td>
					<td><c:forEach var="ese" items="${findingAid.eses}">
						<c:if test="${!empty ese.pathHtml }">
						<a href='<c:url value="/htmlPreview"><c:param name="eseId" value="${ese.eseId}"/></c:url>'>Preview HTML</a>
						</c:if>
						<br />
					</c:forEach></td>
					<td><c:forEach var="ese" items="${findingAid.eses}">
						<s:a action="generateHTML">Generate HTML
							    <s:param name="eseId">
								<c:out value="${ese.eseId}" />
							</s:param>
						</s:a>
						<br />
					</c:forEach></td>
				</c:when>
				<c:otherwise>
					<td></td>
					<td></td>
					<td></td>
					<td><c:choose>
						<c:when test="${findingAid.fileState.fsId == 8}">
							<s:a action="displayConvert">Convert to ESE
								    <s:param name="findingAidId">
									<c:out value="${findingAid.faId}" />
								</s:param>
							</s:a>
						</c:when>
						<c:otherwise>
								Not indexed
							</c:otherwise>
					</c:choose></td>
				</c:otherwise>
			</c:choose>


		</tr>
	</c:forEach>
</table>
</div>



