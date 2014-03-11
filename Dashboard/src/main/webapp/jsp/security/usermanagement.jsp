<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div >  
	<table class="defaultlayout">
		<tr>
			<th><s:text name="usermanagement.country"/></th>
			<th><s:text name="usermanagement.email"/></th>
			<th><s:text name="usermanagement.name"/></th>			
			<th><s:text name="usermanagement.actions"/></th>
		</tr>
	<c:forEach var="countryAndManager" items="${countryAndManagers}">
		<tr <c:if test="${!countryAndManager.countryManager.active && !(empty countryAndManager.countryManager)}"> class="grayedout" </c:if>>
			<td><a href="institutionManagerManagement.action?countryId=${countryAndManager.country.id}" ><c:out value="${countryAndManager.country.cname}"/></a></td>
			<c:choose>
				<c:when test="${empty countryAndManager.countryManager}">
					<td colspan="2"></td>
					<td>
						<s:form action="displayCreateCountryManager" theme="simple" method="GET">
							<input type="hidden" name="countryId" value="${countryAndManager.country.id}"/>
							<s:submit key="usermanagement.create.countrymanager" name="createCountryManager"/>
						</s:form>
					</td>				
				</c:when>
				<c:otherwise>
					<td><a href="mailto:${countryAndManager.countryManager.emailAddress}"><c:out value="${countryAndManager.countryManager.emailAddress}"/></a> </td>
					<td><c:out value="${countryAndManager.countryManager.name}"/></td>
					<td>
						<c:choose>
							<c:when test="${countryAndManager.countryManager.active}">
								<s:form action="disableCountryManager" theme="simple">
									<input type="hidden" name="partnerId"   value="${countryAndManager.countryManager.id}"/>
									<s:submit key="usermanagement.disable" name="disable"/>
								</s:form>
							</c:when>
							<c:otherwise>
								<s:form action="enableCountryManager" theme="simple">
									<input type="hidden" name="partnerId"   value="${countryAndManager.countryManager.id}"/>
									<s:submit key="usermanagement.enable" name="enable"/>
								</s:form>							
							</c:otherwise>
						</c:choose>
							<c:if test="${!countryAndManager.inuse}">
								<s:form action="deleteCountryManager" theme="simple">
									<input type="hidden"  name="partnerId"  value="${countryAndManager.countryManager.id}"/>
									<s:submit key="usermanagement.delete" name="delete"/>
								</s:form>	
								<s:form action="changeToCountryManager" theme="simple">
									<input type="hidden"  name="partnerId"  value="${countryAndManager.countryManager.id}"/>
									<s:submit key="usermanagement.takeover" name="changeToCountryManager"/>
								</s:form>
							</c:if>										
					</td>
				</c:otherwise>
			</c:choose>
			</td>


		</tr>
	</c:forEach>
	</table>

</div>

