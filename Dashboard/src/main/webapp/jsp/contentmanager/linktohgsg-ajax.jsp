<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="ape" uri="http://commons.archivesportaleurope.eu/tags"%>
	<div id="selectedPart">
		<s:select id="selectCLevel" name="parentCLevelId" list="clevels" listKey="value" listValue="content" required="true"></s:select>
	</div>
	<div id="new-ead-results">
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