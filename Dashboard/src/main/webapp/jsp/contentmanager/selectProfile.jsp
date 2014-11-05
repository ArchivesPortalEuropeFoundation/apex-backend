<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="ape" uri="http://commons.archivesportaleurope.eu/tags"%>

<link rel="stylesheet" href="${pageContext.request.contextPath}/css/ingestionprofile.css" type="text/css"/>

<script type='text/javascript'>
	$(document).ready(function() {
		initPage();
	});
</script>

<s:form method="POST" theme="simple">
	<c:choose>
		<c:when test="${not empty profilesSet}">
			<table>
				<tr>
					<td class="inputLabel">
						<s:label key="dashboard.applyProfile.selectProfile" for="selectProfile" />:
					</td>
					<td>
						<s:select id="selectProfile" name="selectProfile" list="profilesSet" listKey="value" listValue="content"
							required="true">
						</s:select>
					</td>
				</tr>
				<tr>
					<td colspan="2">
						<br/>
						<s:submit action="applyProfile" key="content.message.applyProfile" cssClass="applyProfileMainButton" />
						<s:submit action="contentmanager" key="label.cancel" cssClass="applyProfileOtherButton" />
					</td>
				</tr>
			</table>
		</c:when>
		<c:otherwise>
			<table>
				<tr>
					<td colspan="2">
						<div class="warning">
							<s:text name="dashboard.applyProfile.noProfiles" />
						</div>
						<br/>
					</td>
				</tr>
				<tr>
					<td colspan="2">
						<s:submit action="contentmanager" key="dashboard.editead.btn.cancelandquit" />
					</td>
				</tr>
			</table>
		</c:otherwise>
	</c:choose>

	<s:hidden id ="batchItems" name="batchItems" />
	<s:hidden name="id" />
	<s:hidden name="xmlTypeId" />
	<s:hidden name="selectedProfile" id="selectedProfile"/>
</s:form>