<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div >  
	<table class="defaultlayout">
		<tr>
			<th><s:text name="admin.sessionmanagement.logintime"/></th>
			<th><s:text name="admin.sessionmanagement.lastaccesstime"/></th>
			<th><s:text name="admin.sessionmanagement.country"/></th>
			<th><s:text name="admin.sessionmanagement.institution"/></th>			
			<th><s:text name="admin.sessionmanagement.email"/></th>
			<th><s:text name="admin.sessionmanagement.role"/></th>
			<th><s:text name="admin.sessionmanagement.name"/></th>
			<th><s:text name="admin.sessionmanagement.orginal"/></th>
			<th><s:text name="admin.sessionmanagement.webdav"/></th>
			<th><s:text name="admin.sessionmanagement.actions"/></th>
		</tr>
	<c:forEach var="sessionInfo" items="${sessionInfos}">
	<c:choose>
		<c:when test="${sessionInfo.webdav }">
			<c:set var="webdavClass" value="webdavsession"/>
		</c:when>
		<c:otherwise>
		<c:set var="webdavClass" value=""/>
		</c:otherwise>
	</c:choose>

		<tr class="${webdavClass}">
			<td><c:out value="${sessionInfo.creationTime}"/></td>
			<td><c:out value="${sessionInfo.lastAccessedTime}"/></td>				
			<td><c:out value="${sessionInfo.country}"/></td>
			<td><c:out value="${sessionInfo.institution}"/></td>
			<td><c:out value="${sessionInfo.emailAddress}"/></td>
			<td><c:out value="${sessionInfo.role}"/></td>
			<td><c:out value="${sessionInfo.name}"/></td>			
			<td class="otherroles"><ul>
				<c:forEach var="parentInfo" items="${sessionInfo.parentInfo}">
					<li><c:out value="${parentInfo}"/></li>
				</c:forEach>
			</ul>
			</td>
			<td><c:if test="${sessionInfo.webdav }"><s:text name="admin.sessionmanagement.usewebdav"/></c:if> </td>

			<td><s:form action="deleteSession" theme="simple"> 
				<input type="hidden" name="sessionId" value="${sessionInfo.sessionId}"/>
				<s:submit key="admin.sessionmanagement.delete"></s:submit>
			</s:form></td>
		</tr>
	</c:forEach>
	</table>

</div>

