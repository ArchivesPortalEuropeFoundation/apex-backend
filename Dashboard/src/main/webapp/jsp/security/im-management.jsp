<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="dashboard" uri="http://dashboard.archivesportaleurope.eu/tags"%>

<c:set var="countryId"><s:property value="countryId"/></c:set>
<div >  
	<table class="defaultlayout">
		<tr>
			<th><s:text name="usermanagement.institution"/></th>
			<th><s:text name="institution.mets"/></th>
			<th><s:text name="usermanagement.email"/></th>
			<th><s:text name="usermanagement.name"/></th>			
			<th><s:text name="usermanagement.actions"/></th>
		</tr>
	<c:forEach var="ai" items="${ais}">
		<tr>
			<td><c:out value="${ai.ainame}"/></td>
			<td><c:if test="${ai.usingMets}"><s:text name="content.message.yes"/></c:if></td>
			<c:choose>
				<c:when test="${empty ai.partner}">
					<td colspan="2" class="nomanager" ><s:text name="usermanagement.no.institutionmanager"/></td>
					<td>
						<s:form action="displayCreateInstitutionManager" theme="simple" method="GET">
							<input type="hidden" name="aiId" value="${ai.aiId}"/>
							<input type="hidden" name="countryId" value="${countryId}"/>
							<s:submit key="usermanagement.create.institutionmanager" name="displayCreateInstitutionManager"/>
						</s:form>
						<c:choose>
							<c:when test="${ai.usingMets}">
								<s:form action="changeMets" theme="simple">
									<input type="hidden" name="aiId" value="${ai.aiId}"/>
									<input type="hidden" name="countryId" value="${countryId}"/>
									<s:submit key="institution.mets.disable" cssClass="changeMets" name="disable"/>
								</s:form>								
							</c:when>
							<c:otherwise>
								<s:form action="changeMets" theme="simple">
									<input type="hidden" name="aiId" value="${ai.aiId}"/>
									<input type="hidden" name="countryId" value="${countryId}"/>
									<s:submit key="institution.mets.enable"  name="enable" cssClass="changeMets"/>
								</s:form>								
							</c:otherwise>
						</c:choose>							
					</td>				
				</c:when>
				<c:otherwise>
					<td><a href="mailto:${ai.partner.emailAddress}"><c:out value="${ai.partner.emailAddress}"/></a> </td>
					<td><c:out value="${ai.partner.name}"/></td>
					<td>
						<c:choose>
							<c:when test="${ai.partner.active}">
								<s:form action="disableInstitutionManager" theme="simple">
									<input type="hidden" name="partnerId"   value="${ai.partner.id}"/>
									<input type="hidden" name="countryId" value="${countryId}"/>
									<s:submit key="usermanagement.disable" name="enable"/>
								</s:form>
							</c:when>
							<c:otherwise>
								<s:form action="enableInstitutionManager" theme="simple">
									<input type="hidden" name="partnerId"   value="${ai.partner.id}"/>
									<input type="hidden" name="countryId" value="${countryId}"/>
									<s:submit key="usermanagement.enable"  name="disable"/>
								</s:form>							
							</c:otherwise>
						</c:choose>
						<dashboard:userAvailable var="userAvailable" userId="${ai.partner.id}"/>
						<c:if test="${userAvailable}">
							<s:form action="deleteInstitutionManager" theme="simple">
								<input type="hidden"  name="partnerId"  value="${ai.partner.id}"/>
								<input type="hidden" name="countryId" value="${countryId}"/>
								<input type="hidden" name="aiId" value="${ai.aiId}"/>
								<s:submit key="usermanagement.delete"  name="delete"/>
							</s:form>	
							<s:form action="changeToInstitutionManager" theme="simple">
								<input type="hidden" name="aiId" value="${ai.aiId}"/>
								<s:submit key="usermanagement.takeover"  name="changeToInstitutionManager"/>
							</s:form>
							<c:choose>
								<c:when test="${ai.usingMets}">
									<s:form action="changeMets" theme="simple">
										<input type="hidden" name="aiId" value="${ai.aiId}"/>
										<input type="hidden" name="countryId" value="${countryId}"/>
										<s:submit key="institution.mets.disable" cssClass="changeMets" name="disable"/>
									</s:form>								
								</c:when>
								<c:otherwise>
									<s:form action="changeMets" theme="simple">
										<input type="hidden" name="aiId" value="${ai.aiId}"/>
										<input type="hidden" name="countryId" value="${countryId}"/>
										<s:submit key="institution.mets.enable" cssClass="changeMets" name="enable"/>
									</s:form>								
								</c:otherwise>
							</c:choose>								
						</c:if>		
					</td>
				</c:otherwise>
			</c:choose>
			</td>


		</tr>
	</c:forEach>
	</table>

</div>

