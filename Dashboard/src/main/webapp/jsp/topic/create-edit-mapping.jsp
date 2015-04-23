<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="dashboard" uri="http://dashboard.archivesportaleurope.eu/tags" %>
<dashboard:securityContext var="securityContext" />
<c:set var="countryManager" value=""/>
<c:if test="${securityContext.countryManager}">
	<c:set var="countryManager" value="countryManager"/>
</c:if>
<div>
		<s:actionerror />
	<form action="createEditTopicMapping${countryManager}" method="post" theme="simple">
	<table>
		<s:if test="%{topicMappingId}">
			<tr>
				<td class="inputLabel"><s:label key="topicmapping.topic.select.label" for="selectTopic" />:</td>
				<td><s:property value="topicDescription"/></td>
			</tr>
		</s:if>
		<s:else>
			<tr>
				<td class="inputLabel"><s:label key="topicmapping.topic.select.label" for="selectTopic" /><span class="required">*</span>:</td>
				<td><s:select id="selectTopic" name="topicId" cssClass="large" key="topicmapping.topic.select.label"  listKey="value" headerKey="" headerValue="%{getText('label.select')}" listValue="content" list="topics"/><s:fielderror fieldName="topicId"/></td>
			</tr>
		</s:else>
		<tr>
			<td class="inputLabel"><s:label key="topicmapping.sourceguide.select.label" for="selectSourceGuide" />:</td>
			<td><s:select id="selectSourceGuide" name="sourceGuideId" cssClass="large" key="topicmapping.sourceguide.select.label"  listKey="value" headerKey="" headerValue="%{getText('label.select')}" listValue="content" list="sourceGuides"/></td>
		</tr>		
		
		<tr>
			<td class="inputLabel"><s:label key="topicmapping.keywords.label" for="keywords" /> &lt;controlaccess&gt;&lt;subject&gt;:<br/>(<s:text name="topicmapping.keywords.controllaccess.split"/>)</td>
			<td><textarea id="keywords" rows="" cols="" maxlength="1500" name="keywords"><c:out value="${keywords}"/></textarea><s:fielderror fieldName="keywords"/></td>
		</tr>
	</table>
	<table>
		<tr>
			<td colspan="2"><s:submit key="label.ok" cssClass="mainButton" name="okButton" /><s:submit method="cancel" key="label.cancel" name="cancelButton" onclick="form.onsubmit=null"/>
			</td>
		</tr>
	</table>			

		
		<s:hidden name="topicMappingId"/>	

		
		
	</form>
</div>
