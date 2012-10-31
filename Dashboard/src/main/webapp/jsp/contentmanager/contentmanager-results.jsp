<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="ape" uri="http://commons.archivesportaleurope.eu/tags"%>
<script type="text/javascript">
	$(document).ready(function() {
		initResultsHandlers();
	});
</script>
<span class="bold">Batch:</span>
<input type="radio" checked="checked" value="1">
All
</input>
<input type="radio">
Only selected
</input>
<select><option>Convert</option>
	<option>Validate</option>
	<option>Index</option>
	<option>Convert to ESE</option>
</select>
<input type="submit" value="Go" />
<div id="ead-results" class="box">
	<s:form id="updateCurrentSearch" action="contentmanagerResults" theme="simple">
		<s:hidden name="pageNumber" />
		<s:hidden name="orderByField" />
		<s:hidden name="orderByAscending" />
		<div id="ead-results-header" class="boxtitle">
			<div id="numberOfResults">
				<span class="bold">Resultaten:</span>
				<ape:pageDescription numberOfItems="${results.totalNumberOfResults}" pageSize="${results.eadSearchOptions.pageSize}"
					pageNumber="${results.eadSearchOptions.pageNumber}" />
			</div>
			<div id="resultPerPageContainer">
				<label class="bold" id="pageSizeLabel" for="updateCurrentSearch_resultPerPage">Resultaten per pagina: </label>
				<s:select name="resultPerPage" list="#{'10':'10','20':'20','30':'30','50':'50','100':'100'}" />
			</div>
			<div id="ead-paging" class="paging">
				<ape:paging numberOfItems="${results.totalNumberOfResults}" pageSize="${results.eadSearchOptions.pageSize}"
					pageNumber="${results.eadSearchOptions.pageNumber}" refreshUrl="javascript:updatePageNumber('');"
					pageNumberId="pageNumber" />
			</div>
		</div>
		<div id="content_div_table">
			<table>
				<thead>
					<tr>
						<th><s:text name="content.message.select.label" /><br /> <span class="linkList" id="selectAll">[<s:text
									name="content.message.select.all" />]
						</span> - <span class="linkList" id="selectNone">[<s:text name="content.message.select.none" />]
						</span></th>
						<th><s:text name="content.message.id" /> <a class="order" href="javascript:changeOrder('id','true')"><img
								class="noStyle" src="images/expand/arrow-down.gif" alt="down" /></a> <a class="order"
							href="javascript:changeOrder('id','false')"><img class="noStyle" src="images/expand/arrow-up.gif" alt="up" /></a>
						</th>
						<th><s:text name="content.message.title" /> <a class="order" href="javascript:changeOrder('title','true')"><img
								class="noStyle" src="images/expand/arrow-down.gif" alt="down" /></a> <a class="order"
							href="javascript:changeOrder('title','false')"><img class="noStyle" src="images/expand/arrow-up.gif" alt="up" /></a>
						</th>
						<th><s:text name="content.message.date" /> <a class="order"
							href="javascript:changeOrder('uploadDate','true')"><img class="noStyle" src="images/expand/arrow-down.gif"
								alt="down" /></a> <a class="order" href="javascript:changeOrder('uploadDate','false')"><img class="noStyle"
								src="images/expand/arrow-up.gif" alt="up" /></th>
						<th><s:text name="content.message.conversion" /> <a class="order"
							href="javascript:changeOrder('converted','true')"><img class="noStyle" src="images/expand/arrow-down.gif"
								alt="down" /></a> <a class="order" href="javascript:changeOrder('converted','false')"><img class="noStyle"
								src="images/expand/arrow-up.gif" alt="up" /><br /> <a href="#conversionOptsDiv" class="link_right"
								id="conversionOpts"><s:text name="content.message.conversion.options" /></a></th>
						<th><s:text name="content.message.validation" /> <a class="order"
							href="javascript:changeOrder('validated','true')"><img class="noStyle" src="images/expand/arrow-down.gif"
								alt="down" /></a> <a class="order" href="javascript:changeOrder('validated','false')"><img class="noStyle"
								src="images/expand/arrow-up.gif" alt="up" /></a></th>
						<th><s:text name="content.message.indexed" /> <a class="order"
							href="javascript:changeOrder('totalNumberOfUnits','true')"><img class="noStyle" src="images/expand/arrow-down.gif"
								alt="down" /></a> <a class="order" href="javascript:changeOrder('totalNumberOfUnits','false')"><img class="noStyle"
								src="images/expand/arrow-up.gif" alt="up" /></a></th>
						<c:if test="${results.findingAid}">
							<th><s:text name="content.message.convert2" /> <a class="order"
								href="javascript:changeOrder('totalNumberOfDaos','true')"><img class="noStyle" src="images/expand/arrow-down.gif"
									alt="down" /></a> <a class="order" href="javascript:changeOrder('totalNumberOfDaos','false')"><img class="noStyle"
									src="images/expand/arrow-up.gif" alt="up" /></a></th>
							<th><s:text name="content.message.deliver" /> <a class="order"
								href="javascript:changeOrder('europeana','true')"><img class="noStyle" src="images/expand/arrow-down.gif"
									alt="down" /></a> <a class="order" href="javascript:changeOrder('europeana','false')"><img class="noStyle"
									src="images/expand/arrow-up.gif" alt="up" /></a></th>
						</c:if>
						<th><s:text name="content.message.actions" /></th>
					</tr>
				</thead>
				<c:forEach var="eadResult" items="${results.eadResults}">
					<tr>
						<td><input class="checkboxSave" type="checkbox" name="check" id="check_${eadResult.id}"
							value="${eadResult.id}" /></td>
						<td class="nocenter">${eadResult.eadid}</td>
						<td><span class="title">${eadResult.title}</span></td>
						<td>${eadResult.date}</td>
						<td><c:choose>
								<c:when test="${eadResult.converted}">
									<span class="status_ok"><s:text name="content.message.ok" /></span>
								</c:when>
								<c:otherwise>
									<span class="status_no"><s:text name="content.message.no" /></span>
								</c:otherwise>
							</c:choose></td>
						<td><c:choose>
								<c:when test="${eadResult.validated}">
									<span class="status_ok"><s:text name="content.message.ok" /></span>
								</c:when>
								<c:when test="${eadResult.validatedFatalError}">
									<span class="status_error"><s:text name="content.message.fatalerror" /></span>
								</c:when>
								<c:otherwise>
									<span class="status_no"><s:text name="content.message.no" /></span>
								</c:otherwise>
							</c:choose></td>
						<td><c:choose>
								<c:when test="${eadResult.searchable}">
									<span class="status_ok">${eadResult.units}</span>
								</c:when>
								<c:otherwise>
									<span class="status_no"><s:text name="content.message.no" /></span>
								</c:otherwise>
							</c:choose></td>
						<c:if test="${results.findingAid}">
							<td><c:choose>
									<c:when test="${eadResult.convertedToEuropeana}">
										<span class="status_ok">${eadResult.totalNumberOfDaos}</span>
									</c:when>
									<c:otherwise>
										<span class="status_no"><s:text name="content.message.no" /></span>
									</c:otherwise>
								</c:choose></td>
							<td><c:choose>
									<c:when test="${eadResult.harvestedByEuropeana}">
										<span class="status_ok"><s:text name="content.message.europeana.harvested" /></span>
									</c:when>
									<c:when test="${eadResult.deliveredToEuropeana}">
										<span class="status_ok"><s:text name="content.message.europeana.delivered" /></span>
									</c:when>
									<c:otherwise>
										<span class="status_no"><s:text name="content.message.no" /></span>
									</c:otherwise>
								</c:choose></td>
								<td class="actions"><select>
									<c:if test="${not eadResult.validated and not eadResult.validatedFatalError}">
										<option value="validate.action"><s:text name="content.message.validate"/></option>
									</c:if>
									<c:if test="${not eadResult.converted}">
										<option value="validate.action"><s:text name="content.message.convert"/></option>
									</c:if>
									<c:if test="${eadResult.validated and not eadResult.searchable}">
										<option value="validate.action"><s:text name="content.message.index"/></option>
									</c:if>
									<c:if test="${eadResult.validated and not eadResult.convertedToEuropeana}">
										<option value="validate.action"><s:text name="content.message.convert.ese"/></option>
									</c:if>
									<c:if test="${eadResult.convertedToEuropeana and eadResult.totalNumberOfDaos > 0 and not eadResult.deliveredToEuropeana}">
										<option value="validate.action"><s:text name="content.message.deliver.europeana"/></option>
									</c:if>
									<c:if test="${eadResult.deliveredToEuropeana}">
										<option value="validate.action"><s:text name="content.message.delete.europeana"/></option>
									</c:if>
									<c:if test="${eadResult.convertedToEuropeana}">
										<option value="validate.action"><s:text name="content.message.delete.ese"/></option>
									</c:if>
									<option value="validate.action"><s:text name="content.message.delete2"/></option>
									<option value="validate.action"><s:text name="content.message.deletefromIndexqueue"/></option>
									<option value="validate.action"><s:text name="content.message.delete"/></option>
									
								</select><input type="button" value="Go"/></td>
						</c:if>
					</tr>
				</c:forEach>

			</table>
		</div>
	</s:form>
</div>
