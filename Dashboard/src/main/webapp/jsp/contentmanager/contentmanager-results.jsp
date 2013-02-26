<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="ape" uri="http://commons.archivesportaleurope.eu/tags"%>
<%@ taglib prefix="apenet" uri="http://commons.apenet.eu/tags"%>
<script type="text/javascript">
	$(document).ready(function() {
		initResultsHandlers();
	});
</script>
<div id="batchBlock">
	<form id="batchActionsForm">
		<div class="right">
			<span class="bold"><s:text name="content.message.batch" />:</span> <input type="radio" checked="checked"
				name="batchItems" value="all">
			<s:text name="content.message.all" />
			<input type="radio" checked="checked" name="batchItems" value="only_selected"/>
			<s:text name="content.message.onlyselected" />
			<input type="radio" name="batchItems" value="only_searched">
			<s:text name="content.message.onlysearched" />
			<input type="hidden" name="xmlTypeId" value="<s:property value="%{xmlTypeId}"/>" /> <select
				id="batchSelectedAction" name="action">
				<option value="convert_validate_publish">
					<s:text name="content.message.doitall" />
				</option>
				<option value="convert">
					<s:text name="content.message.convert" />
				</option>
				<option value="validate">
					<s:text name="content.message.validate" />
				</option>
				<option value="publish">
					<s:text name="content.message.publish" />
				</option>
				<%--<option value="displayEseConvert">--%>
					<%--<s:text name="content.message.convert.ese" />--%>
				<%--</option>--%>
				<%--<option value="deliverToEuropeana">--%>
					<%--<s:text name="content.message.deliver.europeana" />--%>
				<%--</option>--%>
				<option value="unpublish">
					<s:text name="content.message.unpublish" />
				</option>
				<%--<option value="deleteFromEuropeana">--%>
					<%--<s:text name="content.message.delete.europeana" />--%>
				<%--</option>--%>
				<%--<option value="deleteEseEdm">--%>
					<%--<s:text name="content.message.delete.ese" />--%>
				<%--</option>--%>
				<option value="deleteFromQueue">
					<s:text name="content.message.delete.queue" />
				</option>	
				<option value="delete">
					<s:text name="content.message.delete" />
				</option>

			</select> <input id="batchActionButton" type="submit" value="<s:text name="content.message.go" />" />
		</div>
	</form>
</div>
<div id="ead-results" class="box">
	<s:form id="updateCurrentSearch" action="updateContentmanager" theme="simple">
		<s:hidden name="pageNumber" />
		<s:hidden name="orderByField" />
		<s:hidden name="orderByAscending" />
		<s:hidden name="xmlTypeId" />
		<input name="ajax" type="hidden" value="true" />

		<div id="ead-results-header" class="boxtitle">
			<div id="numberOfResults">
				<span class="bold"><s:text name="content.message.results" />:</span>
				<ape:pageDescription numberOfItems="${results.totalNumberOfResults}" pageSize="${results.eadSearchOptions.pageSize}"
					pageNumber="${results.eadSearchOptions.pageNumber}" />
			</div>
			<div id="resultPerPageContainer">
				<label class="bold" id="pageSizeLabel" for="updateCurrentSearch_resultPerPage"><s:text
						name="content.message.resultsperpage" />: </label>
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
						<th><s:text name="content.message.id" /> <a class="order" href="javascript:changeOrder('eadid','true')"><img
								class="noStyle" src="images/expand/arrow-down.gif" alt="down" /></a> <a class="order"
							href="javascript:changeOrder('eadid','false')"><img class="noStyle" src="images/expand/arrow-up.gif" alt="up" /></a></th>
						<th><s:text name="content.message.title" /> <a class="order" href="javascript:changeOrder('title','true')"><img
								class="noStyle" src="images/expand/arrow-down.gif" alt="down" /></a> <a class="order"
							href="javascript:changeOrder('title','false')"><img class="noStyle" src="images/expand/arrow-up.gif" alt="up" /></a></th>
						<th><s:text name="content.message.date" /> <a class="order"
							href="javascript:changeOrder('uploadDate','true')"><img class="noStyle" src="images/expand/arrow-down.gif"
								alt="down" /></a> <a class="order" href="javascript:changeOrder('uploadDate','false')"><img class="noStyle"
								src="images/expand/arrow-up.gif" alt="up" /></a></th>
						<th><s:text name="content.message.converted" /> <a class="order"
							href="javascript:changeOrder('converted','true')"><img class="noStyle" src="images/expand/arrow-down.gif"
								alt="down" /></a> <a class="order" href="javascript:changeOrder('converted','false')"><img class="noStyle"
								src="images/expand/arrow-up.gif" alt="up" /></a><br /> <a href="#conversionOptsDiv" class="link_right"
							id="conversionOpts"><s:text name="content.message.conversion.options" /></a></th>
						<th><s:text name="content.message.validated" /> <a class="order"
							href="javascript:changeOrder('validated','true')"><img class="noStyle" src="images/expand/arrow-down.gif"
								alt="down" /></a> <a class="order" href="javascript:changeOrder('validated','false')"><img class="noStyle"
								src="images/expand/arrow-up.gif" alt="up" /></a></th>
						<th><s:text name="content.message.published" /> <a class="order"
							href="javascript:changeOrder('totalNumberOfUnits','true')"><img class="noStyle"
								src="images/expand/arrow-down.gif" alt="down" /></a> <a class="order"
							href="javascript:changeOrder('totalNumberOfUnits','false')"><img class="noStyle"
								src="images/expand/arrow-up.gif" alt="up" /></a></th>
						<c:choose>
							<c:when test="${results.findingAid}">
								<th><s:text name="content.message.holdings" /></th>
								<th><s:text name="content.message.eseedm" /> <a class="order"
									href="javascript:changeOrder('totalNumberOfDaos','true')"><img class="noStyle"
										src="images/expand/arrow-down.gif" alt="down" /></a> <a class="order"
									href="javascript:changeOrder('totalNumberOfDaos','false')"><img class="noStyle"
										src="images/expand/arrow-up.gif" alt="up" /></a></th>
								<th><s:text name="content.message.europeana" /> <a class="order"
									href="javascript:changeOrder('europeana','true')"><img class="noStyle" src="images/expand/arrow-down.gif"
										alt="down" /></a> <a class="order" href="javascript:changeOrder('europeana','false')"><img class="noStyle"
										src="images/expand/arrow-up.gif" alt="up" /></a></th>
							</c:when>
							<c:when test="${results.holdingsGuide}">
								<th><s:text name="content.message.linked" /></th>
							</c:when>
						</c:choose>
						<th><s:text name="content.message.queue" /> <a class="order" href="javascript:changeOrder('queuing','true')"><img
								class="noStyle" src="images/expand/arrow-down.gif" alt="down" /></a> <a class="order"
							href="javascript:changeOrder('queuing','false')"><img class="noStyle" src="images/expand/arrow-up.gif"
								alt="up" /></a></th>

						<th><s:text name="content.message.actions" /></th>
					</tr>
				</thead>
				<c:forEach var="eadResult" items="${results.eadResults}">
					<tr class="${eadResult.cssClass}">
						<td><input class="checkboxSave" type="checkbox" name="check" id="check_${eadResult.id}"
							value="${eadResult.id}" /></td>
						<td class="nocenter">${eadResult.eadid}</td>
						<td><span class="title">${eadResult.title}</span></td>
						<td>${eadResult.date}</td>
						<td class="${eadResult.convertedCssClass}"><apenet:resource>${eadResult.convertedText}</apenet:resource></td>
						<td class="${eadResult.validatedCssClass}"><apenet:resource>${eadResult.validatedText}</apenet:resource></td>
						<td class="${eadResult.indexedCssClass}"><c:choose>
								<c:when test="${eadResult.published}">${eadResult.units}</c:when>
								<c:otherwise>
									<apenet:resource>${eadResult.indexedText}</apenet:resource>
								</c:otherwise>
							</c:choose></td>
						<c:choose>
							<c:when test="${results.findingAid}">
								<td>${eadResult.holdingsGuideTitle}</td>
								<td class="${eadResult.eseEdmCssClass}"><c:choose>
										<c:when
											test="${(eadResult.convertedToEseEdm or eadResult.deliveredToEuropeana) and eadResult.totalNumberOfDaos > 0}">${eadResult.totalNumberOfDaos}</c:when>
										<c:otherwise>
											<apenet:resource>${eadResult.eseEdmText}</apenet:resource>
										</c:otherwise>
									</c:choose></td>
								<td class="${eadResult.europeanaCssClass}"><apenet:resource>${eadResult.europeanaText}</apenet:resource></td>
							</c:when>
							<c:when test="${results.holdingsGuide}"><td>${eadResult.findingAidsLinked} / ${eadResult.possibleFindingAidsLinked}</td></c:when>

						</c:choose>
						<td class="${eadResult.queueCssClass}"><c:if
								test="${eadResult.queueReady or eadResult.queueProcessing or eadResult.queueError}">
								<c:if test="${eadResult.queueError}">
									<s:text name="content.message.error" />:</c:if>
								<apenet:resource>${eadResult.queueAction.resourceName }</apenet:resource>
							</c:if></td>

						<td class="actions"><c:choose>
								<c:when test="${eadResult.queueReady}">
									<select class="selectedAction" name="selectedAction">
										<option value="action|deleteFromQueue">
											<s:text name="content.message.delete.queue" />
										</option>							
									</select>
									<input type="button" value="<s:text name="content.message.go" />" />
								</c:when>
								<c:when test="${eadResult.queueError} ">
									<select class="selectedAction" name="selectedAction">
										<option value="action|deleteFromQueue">
											<s:text name="content.message.delete.queue" />
										</option>								
									</select>
									<input type="button" value="<s:text name="content.message.go" />" />
								</c:when>																		
								<c:when test="${eadResult.queueProcessing}">
									<s:text name="content.message.queueprocessing" />
								</c:when>
								<c:otherwise>

									<select class="selectedAction" name="selectedAction">
										<c:if test="${not eadResult.converted}">
											<option value="action|convert">
												<s:text name="content.message.convert" />
											</option>
										</c:if>
										<c:if test="${not eadResult.validated and not eadResult.validatedFatalError}">
											<option value="action|validate">
												<s:text name="content.message.validate" />
											</option>
										</c:if>

										<c:if test="${eadResult.validated and not eadResult.published}">
											<option value="action|publish">
												<s:text name="content.message.publish" />
											</option>
										</c:if>
										<c:if test="${eadResult.editable}">
											<option value="_blank|editEad.action">
												<s:text name="label.edit" />
											</option>
										</c:if>
										<c:if test="${eadResult.validated}">
											<option value="_blank|preview.action">
												<s:text name="content.message.preview" />
											</option>
											<option value="_blank|download.action">
												<s:text name="content.message.download" />
											</option>

										</c:if>
										<c:if test="${eadResult.containWarnings}">
											<option value="colorbox|showwarnings.action?iswarning=true">
												<s:text name="content.message.showerror" />
											</option>

										</c:if>
										<c:if test="${results.holdingsGuide and (eadResult.findingAidsLinked != eadResult.possibleFindingAidsLinked)}">
											<option value="_blank|getFindingAidsNotLinked.action">
												<s:text name="content.message.getFindingAidsNotLinked" />
											</option>
										
										</c:if>
										<%--<c:if test="${results.findingAid}">--%>
											<%--<c:if--%>
												<%--test="${eadResult.validated and not eadResult.convertedToEseEdm and not eadResult.deliveredToEuropeana}">--%>
												<%--<option value="_self|displayEseConvert.action">--%>
													<%--<s:text name="content.message.convert.ese" />--%>
												<%--</option>--%>
											<%--</c:if>--%>
											<%--<c:if test="${eadResult.convertedToEseEdm and eadResult.totalNumberOfDaos > 0}">--%>
												<%--<option value="action|deliverToEuropeana">--%>
													<%--<s:text name="content.message.deliver.europeana" />--%>
												<%--</option>--%>
											<%--</c:if>--%>
											<%--<c:if test="${eadResult.hasEseEdmFiles}">--%>
												<%--<option value="_blank|htmlPreview">--%>
													<%--<s:text name="content.message.preview.ese" />--%>
												<%--</option>--%>
												<%--<option value="_blank|downloadEse.action">--%>
													<%--<s:text name="content.message.download.ese" />--%>
												<%--</option>--%>
											<%--</c:if>--%>
											<%--<c:if test="${eadResult.convertedToEseEdm}">--%>
												<%--<option value="action|deleteEseEdm">--%>
													<%--<s:text name="content.message.delete.ese" />--%>
												<%--</option>--%>
											<%--</c:if>--%>

											<%--<c:if test="${eadResult.deliveredToEuropeana}">--%>
												<%--<option value="action|deleteFromEuropeana">--%>
													<%--<s:text name="content.message.delete.europeana" />--%>
												<%--</option>--%>
											<%--</c:if>--%>
										<%--</c:if>--%>
										<c:if test="${eadResult.published}">
											<option value="action|unpublish">
												<s:text name="content.message.unpublish" />
											</option>
										</c:if>
										<option value="action|delete">
											<s:text name="content.message.delete" />
										</option>

									</select>
									<input type="button" value="<s:text name="content.message.go" />" />

								</c:otherwise>
							</c:choose></td>

					</tr>
				</c:forEach>
					<tr class="total">
						<td colspan="4"><s:text name="content.statistics.title.all"/></td>
						<td>${results.totalConvertedFiles}</td>
						<td>${results.totalValidatedFiles}</td>
						<td>${results.totalPublishedUnits}</td>
						<td>&nbsp;</td>
						<c:choose>
							<c:when test="${results.findingAid}">
								<td>${results.totalDaosConvertedToEseEdm}</td>
								<td>${results.totalDaosDeliveredToEuropea}</td>
								<td>&nbsp;</td>
							</c:when>
							<c:when test="${results.holdingsGuide}"><td>&nbsp;</td></c:when>
						</c:choose>

						<td>&nbsp;</td>
					</tr>
			</table>
		</div>
	</s:form>
    <div class="hidden">
        <div id="conversionOptsDiv">
            <form action="">
                <table>
                    <tr>
                        <td><s:text name="content.message.roletype.useexisting"/> <input type="checkbox" name="useExistingRole" id="useExistingRole" value="useExistingRole" /></td>
                        <td><s:text name="content.message.roletype.defaultrole"/></td>
                    </tr>
                    <tr>
                        <td></td>
                        <td><input type="radio" class="roleTypeRadio" name="roleType" value="IMAGE" />IMAGE</td>
                    </tr>
                    <tr>
                        <td></td>
                        <td><input type="radio" class="roleTypeRadio" name="roleType" value="VIDEO" />VIDEO</td>
                    </tr>
                    <tr>
                        <td></td>
                        <td><input type="radio" class="roleTypeRadio" name="roleType" value="SOUND" />SOUND</td>
                    </tr>
                    <tr>
                        <td></td>
                        <td><input type="radio" class="roleTypeRadio" name="roleType" value="TEXT" />TEXT</td>
                    </tr>
                    <tr>
                        <td></td>
                        <td><input type="radio" class="roleTypeRadio" name="roleType" value="UNSPECIFIED" />UNSPECIFIED</td>
                    </tr>
                </table>
                <input type="button" id="submitBtnRoleType" value="Submit"/>
                <input type="button" id="cancelBtnRoleType" value="Cancel"/>
            </form>
        </div>
    </div>
</div>
