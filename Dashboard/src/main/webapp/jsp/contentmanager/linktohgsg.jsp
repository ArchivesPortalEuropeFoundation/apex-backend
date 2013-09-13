<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="ape" uri="http://commons.archivesportaleurope.eu/tags"%>
<script type='text/javascript'>
$(document).ready(function() {
	initPage();
});

</script>

<s:form method="POST" theme="simple">
	<table>
		<tr>
			<td class="inputLabel"><s:label key="dashboard.hgcreation.selecthgsg" for="selectHgSg" />:</td>
			<td><s:select id="selectHgSg" name="ecId" list="dynamicHgSgs" listKey="value" listValue="content"
					required="true"></s:select></td>
		</tr>
		<tr>
			<td class="inputLabel"><s:label key="dashboard.hgcreation.selectclevel" for="selectCLevel" />:</td>
			<td id="selectClevelTd"><s:select id="selectCLevel" name="parentCLevelId" list="clevels" listKey="value"
					listValue="content" required="true"></s:select></td>
		</tr>
		<tr>
			<td class="inputLabel"><s:label key="dashboard.hgcreation.selectprefixmethod" for="selectPrefixMethod" />:</td>
			<td><s:radio name="selectPrefixMethod" list="selectPrefixMethodSet" listKey="value" listValue="content"
					id="selectPrefixMethod"></s:radio></td>
		</tr>
		<tr>
			<td colspan="2"><s:submit action="linkToHgSg" key="dashboard.hgcreation.addfindingaids" cssClass="mainButton" />
				<s:submit action="contentmanager" key="label.cancel" /></td>
		</tr>
	</table>
	<h3>
		<s:text name="dashboard.hgcreation.addfindingaids.preview" />
	</h3>
	<div id="ead-results" class="box">
		<div id="ead-results-header" class="boxtitle">
			<div id="numberOfResults">
				<span class="bold"><s:text name="content.message.results" />:</span>
				<ape:pageDescription numberOfItems="${totalNumberOfFindingAids}" pageSize="100" pageNumber="1" />
			</div>
		</div>
		<div id="content_div_table">
			<table>
				<thead>
					<tr>
						<th><s:text name="content.message.id" /></th>
						<th><s:text name="content.message.title" /></th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="findingAid" items="${findingAids}">
						<tr>
							<td class="nocenter">${findingAid.eadid}</td>
							<td class="nocenter">${findingAid.title}</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
	</div>
	<div id="dynamicContent" class="">
	
	</div>
	<s:hidden id ="batchItems" name="batchItems" />
	<s:hidden name="id" />
</s:form>