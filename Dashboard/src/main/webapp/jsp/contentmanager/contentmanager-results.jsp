<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="ape" uri="http://commons.archivesportaleurope.eu/tags"%>
<%@ taglib prefix="apenet" uri="http://commons.apenet.eu/tags"%>

<script type="text/javascript">
    $(document).ready(function() {
        //${securityContext.refresh_interval} is defined in SecurityContext.java to store in session the refresh timeout.
        initResultsHandlers(${securityContext.refresh_interval});
	});
</script>

<div id="batchBlock">
    <form id="batchActionsForm">
        <c:if test="${results.holdingsGuide}">
            <div class="left">
                <a href="hgTreeCreation.action"><s:text name="dashboard.hgcreation.title"/></a>
            </div>
        </c:if>
        <div class="right">
            <span class="bold">
                <s:actionerror />
                <s:text name="content.message.batch" />:</span>
            <input type="radio" name="batchItems" value="all" onchange="select_all();">
            <s:text name="content.message.all" />
            <input type="radio" name="batchItems" value="only_selected" onchange="select_none();" checked="checked"/>
            <s:text name="content.message.onlyselected" />
            <input type="radio" name="batchItems" value="only_searched" onchange="select_none();">
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
                <c:if test="${results.findingAid}">
                    <option value="displayEseConvert">
                        <s:text name="content.message.convert.ese" />
                    </option>
                    <option value="deliverToEuropeana">
                        <s:text name="content.message.deliver.europeana" />
                    </option>
                    <c:if test="${results.hasDynamicHgSg}">
                        <option value="displayLinkToHgSg">
                            <s:text name="content.message.linktohgsg" />
                        </option>
                    </c:if>
                </c:if>
                <c:if test="${results.holdingsGuide or results.sourceGuide}">
                    <option value="changeToStatic">
                        <s:text name="content.message.static.change" />
                    </option>
                    <option value="changeToDynamic">
                        <s:text name="content.message.dynamic.change" />
                    </option>
                </c:if>
                <option value="unpublish">
                    <s:text name="content.message.unpublish" />
                </option>
                <c:if test="${results.findingAid}">
                    <option value="deleteFromEuropeana">
                        <s:text name="content.message.delete.europeana" />
                    </option>
                    <option value="deleteEseEdm">
                        <s:text name="content.message.delete.ese" />
                    </option>
                </c:if>
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
    <s:form id="updateCurrentSearch" action="updateContentmanager" theme="simple" method="POST">
        <s:hidden name="pageNumber" />
        <s:hidden name="orderByField" />
        <s:hidden name="orderByAscending" />
        <s:hidden name="xmlTypeId" />
        <input name="updateSearchResults" type="hidden" value="true" />
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
            <div id="eadRefreshInterval">
                <label class="bold" id="pageSizeLabel" for="updateCurrentSearch_resultPerPage"><s:text
                        name="content.message.refreshInterval" />: </label>
                <select  id= "refreshInterval" name="refreshInterval" onchange="javascript:refreshIntervalFunc($(this).prop('selectedIndex'), true);">
                    <option value="nonRefresh">
                        <s:text name="content.message.nonRefresh"/>
                    </option>
                    <option value="refresh">
                        <s:text name="content.message.refresh"/>
                    </option>
                </select>
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
                        <th><s:text name="content.message.id" />
                <div class="arrows">
                    <a class="order" href="javascript:changeOrder('eadid','true')"><img class="noStyle" src="images/expand/arrow-down.gif" alt="down" /></a>
                    <a class="order" href="javascript:changeOrder('eadid','false')"><img class="noStyle" src="images/expand/arrow-up.gif" alt="up" /></a>
                </div>
                </th>
                <th><s:text name="content.message.title" />
                <div class="arrows">
                    <a class="order" href="javascript:changeOrder('title','true')"><img class="noStyle" src="images/expand/arrow-down.gif" alt="down" /></a>
                    <a class="order" href="javascript:changeOrder('title','false')"><img class="noStyle" src="images/expand/arrow-up.gif" alt="up" /></a>
                </div>
                </th>
                <th><s:text name="content.message.date" />
                <div class="arrows">
                    <a class="order" href="javascript:changeOrder('uploadDate','true')"><img class="noStyle" src="images/expand/arrow-down.gif"
                                                                                             alt="down" /></a> <a class="order" href="javascript:changeOrder('uploadDate','false')"><img class="noStyle"
                                                                                                                src="images/expand/arrow-up.gif" alt="up" /></a>
                </div>
                </th>
                <th><s:text name="content.message.converted" />
                <div class="arrows">
                    <a class="order" href="javascript:changeOrder('converted','true')"><img class="noStyle" src="images/expand/arrow-down.gif" alt="down" /></a>
                    <a class="order" href="javascript:changeOrder('converted','false')"><img class="noStyle" src="images/expand/arrow-up.gif" alt="up" /></a>
                </div>
                <br /> <a href="#conversionOptsDiv" class="link_right" id="conversionOpts"><s:text name="content.message.conversion.options" /></a>
                </th>
                <th><s:text name="content.message.validated" />
                <div class="arrows">
                    <a class="order" href="javascript:changeOrder('validated','true')"><img class="noStyle" src="images/expand/arrow-down.gif" alt="down" /></a>
                    <a class="order" href="javascript:changeOrder('validated','false')"><img class="noStyle" src="images/expand/arrow-up.gif" alt="up" /></a>
                </div>
                </th>
                <th><s:text name="content.message.published" />
                <div class="arrows">
                    <a class="order" href="javascript:changeOrder('totalNumberOfUnits','true')"><img class="noStyle" src="images/expand/arrow-down.gif" alt="down" /></a>
                    <a class="order" href="javascript:changeOrder('totalNumberOfUnits','false')"><img class="noStyle" src="images/expand/arrow-up.gif" alt="up" /></a>
                </div>
                </th>
                <c:choose>
                    <c:when test="${results.findingAid}">
                        <th><s:text name="content.message.holdings" /></th>
                        <th><s:text name="content.message.eseedm" />
                        <div class="arrows">
                            <a class="order" href="javascript:changeOrder('totalNumberOfChos','true')"><img class="noStyle" src="images/expand/arrow-down.gif" alt="down" /></a>
                            <a class="order" href="javascript:changeOrder('totalNumberOfChos','false')"><img class="noStyle" src="images/expand/arrow-up.gif" alt="up" /></a>
                        </div>
                        </th>
                        <th>
                            <s:text name="content.message.europeana" />
                        <div class="arrows">
                            <a class="order"
                               href="javascript:changeOrder('europeana','true')"><img class="noStyle" src="images/expand/arrow-down.gif"
                                                                                   alt="down" /></a> <a class="order" href="javascript:changeOrder('europeana','false')"><img class="noStyle"
                                                                                                                       src="images/expand/arrow-up.gif" alt="up" /></a>
                        </div>
                        </th>
                    </c:when>
                    <c:when test="${results.holdingsGuide or results.sourceGuide}">
                        <th><s:text name="content.message.dynamic" /></th>
                        <th><s:text name="content.message.linked" /></th>
                        </c:when>
                    </c:choose>
                <th><s:text name="content.message.queue" />
                <div class="arrows">
                    <a class="order" href="javascript:changeOrder('queuing','true')"><img class="noStyle" src="images/expand/arrow-down.gif" alt="down" /></a> <a class="order"
                                                                                                                                                                  href="javascript:changeOrder('queuing','false')"><img class="noStyle" src="images/expand/arrow-up.gif" alt="up" />
                    </a>
                </div>
                </th>
                <th><s:text name="content.message.actions" /></th>
                </tr>
                </thead>
                <c:forEach var="eadResult" items="${results.eadResults}">
                    <tr class="${eadResult.cssClass}">
                        <td><input class="checkboxSave" type="checkbox" name="check" id="check_${eadResult.id}"
                                   value="${eadResult.id}" onclick="enable_features"/></td>
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
                                            test="${(eadResult.convertedToEseEdm or eadResult.deliveredToEuropeana) and eadResult.totalNumberOfChos > 0}">${eadResult.totalNumberOfChos}</c:when>
                                        <c:otherwise>
                                            <apenet:resource>${eadResult.eseEdmText}</apenet:resource>
                                        </c:otherwise>
                                    </c:choose></td>
                                <td class="${eadResult.europeanaCssClass}"><apenet:resource>${eadResult.europeanaText}</apenet:resource></td>
                            </c:when>
                            <c:when test="${results.holdingsGuide or results.sourceGuide}">
                                <td class="${eadResult.dynamicCssClass}"><apenet:resource>${eadResult.dynamicText}</apenet:resource></td>
                                <td class="${eadResult.linkedCssClass}" title="<s:text name='content.message.linked.tooltip' />"><c:if test="${eadResult.published or eadResult.dynamic}">${eadResult.findingAidsLinkedAndPublished}/${eadResult.findingAidsLinked}/${eadResult.possibleFindingAidsLinked}</c:if></td>
                            </c:when>

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
                                        <c:if test="${not eadResult.converted and not eadResult.validated}">
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
                                            <c:if test="${not eadResult.dynamic}">
                                                <option value="_self|download.action">
                                                    <s:text name="content.message.download" />
                                                </option>
                                            </c:if>
                                        </c:if>
                                        <c:if test="${eadResult.containWarnings}">
                                            <option value="colorbox|showwarnings.action?iswarning=true">
                                                <s:text name="content.message.showerror" />
                                            </option>

                                        </c:if>
                                        <c:if test="${eadResult.published}">
                                            <option value="action|unpublish">
                                                <s:text name="content.message.unpublish" />
                                            </option>
                                        </c:if>
                                        <c:if test="${results.holdingsGuide or results.sourceGuide}">
                                            <c:if test="${eadResult.findingAidsLinkedAndPublished != eadResult.possibleFindingAidsLinked}">
                                                <option value="_self|downloadHgSgStatistics.action">
                                                    <s:text name="content.message.download.statistics" />
                                                </option>
                                            </c:if>
                                            <c:choose>
                                                <c:when test="${eadResult.dynamic}">
                                                    <option value="action|changeToStatic">
                                                        <s:text name="content.message.static.change" />
                                                    </option>
                                                </c:when>
                                                <c:otherwise>
                                                    <option value="action|changeToDynamic">
                                                        <s:text name="content.message.dynamic.change" />
                                                    </option>
                                                </c:otherwise>
                                            </c:choose>
                                        </c:if>
                                        <c:if test="${results.findingAid}">
                                            <c:if
                                                test="${eadResult.validated and not eadResult.convertedToEseEdm and not eadResult.deliveredToEuropeana and not eadResult.noEuropeanaCandidate}">
                                                <option value="_self|displayEseConvert.action">
                                                    <s:text name="content.message.convert.ese" />
                                                </option>
                                            </c:if>
                                            <c:if test="${eadResult.convertedToEseEdm and eadResult.totalNumberOfChos > 0}">
                                                <option value="action|deliverToEuropeana">
                                                    <s:text name="content.message.deliver.europeana" />
                                                </option>
                                            </c:if>
                                            <c:if test="${eadResult.convertedToEseEdm}">
                                                <option value="_blank|previewEdm.action">
                                                    <s:text name="content.message.preview.ese" />
                                                </option>
                                            </c:if>
                                            <c:if test="${eadResult.hasEseEdmFiles}">
                                                <option value="_self|downloadEse.action">
                                                    <s:text name="content.message.download.ese" />
                                                </option>
                                            </c:if>
                                            <c:if test="${eadResult.convertedToEseEdm}">
                                                <option value="action|deleteEseEdm">
                                                    <s:text name="content.message.delete.ese" />
                                                </option>
                                            </c:if>

                                            <c:if test="${eadResult.deliveredToEuropeana}">
                                                <option value="action|deleteFromEuropeana">
                                                    <s:text name="content.message.delete.europeana" />
                                                </option>
                                            </c:if>
                                            <c:if test="${eadResult.validated and results.hasDynamicHgSg}">
                                                <option value="_self|displayLinkToHgSg.action">
                                                    <s:text name="content.message.linktohgsg" />
                                                </option>
                                            </c:if>
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
                            <td>${results.totalChos}</td>
                            <td>${results.totalChosDeliveredToEuropeana}</td>
                            <td>&nbsp;</td>
                        </c:when>
                        <c:when test="${results.holdingsGuide or results.sourceGuide}"><td>&nbsp;</td><td>&nbsp;</td></c:when>
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
