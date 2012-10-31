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
		<input type="radio" checked="checked" value="1" >All</input><input type="radio" >Only selected</input>
		<select><option>Convert</option><option>Validate</option><option>Index</option><option>Convert to ESE</option> </select>
		<input type="submit" value="Go"/>
<div id="ead-results" class="box">
	<s:form id="updateCurrentSearch" action="contentmanagerResults" theme="simple">
		<s:hidden name="pageNumber" />
		<s:hidden name="orderByField" />
		<s:hidden name="orderByAscending" />
		<div id="ead-results-header" class="boxtitle">
			<div id="numberOfResults">
				<span class="bold">Resultaten:</span><ape:pageDescription numberOfItems="${results.totalNumberOfResults}" pageSize="${results.eadSearchOptions.pageSize}" pageNumber="${results.eadSearchOptions.pageNumber}" />
			</div>
			<div id="resultPerPageContainer">
				<label class="bold" id="pageSizeLabel" for="updateCurrentSearch_resultPerPage">Resultaten per pagina: </label>
					<s:select name="resultPerPage" list="#{'10':'10','20':'20','30':'30','50':'50','100':'100'}"/>
				</div>
			<div id="ead-paging" class="paging">
					<ape:paging numberOfItems="${results.totalNumberOfResults}" pageSize="${results.eadSearchOptions.pageSize}" pageNumber="${results.eadSearchOptions.pageNumber}"
							refreshUrl="javascript:updatePageNumber('');" pageNumberId="pageNumber"/>	
			</div>
		</div>
		<div  id="content_div_table">
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
					<th><s:text name="content.message.date"/> <a class="order" href="javascript:changeOrder('uploadDate','true')"><img
							class="noStyle" src="images/expand/arrow-down.gif" alt="down" /></a> <a class="order"
						href="javascript:changeOrder('uploadDate','false')"><img class="noStyle" src="images/expand/arrow-up.gif" alt="up" /></th>

				</tr>
			</thead>
			<c:forEach var="result" items="${results.eadResults}">
				<tr>
					<td><input class="checkboxSave" type="checkbox" name="check" id="check_${result.id}" value="${result.id}" /></td>
					<td class="nocenter">${result.eadid}</td>
					<td  class="title">${result.title}</td>
					<td  class="title">${result.date}</td>
				</tr>
			</c:forEach>

		</table>
		</div>
	</s:form>
</div>
