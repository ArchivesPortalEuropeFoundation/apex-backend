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
                <option value="displayEseConvert">
                    <s:text name="content.message.convert.ese" />
                </option>
                <option value="deliverToEuropeana">
                    <s:text name="content.message.deliver.europeana" />
                </option>
                <option value="unpublish">
                    <s:text name="content.message.unpublish" />
                </option>
                <option value="deleteFromEuropeana">
                    <s:text name="content.message.delete.europeana" />
                </option>
                <option value="deleteEseEdm">
                    <s:text name="content.message.delete.ese" />
                </option>
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
                    <a class="order" href="javascript:changeOrder('identifier','true')"><img class="noStyle" src="images/expand/arrow-down.gif" alt="down" /></a>
                    <a class="order" href="javascript:changeOrder('identifier','false')"><img class="noStyle" src="images/expand/arrow-up.gif" alt="up" /></a>
                </div>
                </th>
                <th><s:text name="content.message.name" />
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
                    <a class="order" href="javascript:changeOrder('published','true')"><img class="noStyle" src="images/expand/arrow-down.gif" alt="down" /></a>
                    <a class="order" href="javascript:changeOrder('published','false')"><img class="noStyle" src="images/expand/arrow-up.gif" alt="up" /></a>
                </div>
                </th>
                <th><s:text name="content.message.relations" /></th>
                <th><s:text name="content.message.eseedm" />
<%--                <div class="arrows">
                    <a class="order" href="javascript:changeOrder('convertedToEseEdm','true')"><img class="noStyle" src="images/expand/arrow-down.gif" alt="down" /></a>
                    <a class="order" href="javascript:changeOrder('convertedToEseEdm','false')"><img class="noStyle" src="images/expand/arrow-up.gif" alt="up" /></a>
                </div>--%>
                </th>
                <th>
                    <s:text name="content.message.europeana" />
                <div class="arrows">
                    <a class="order" href="javascript:changeOrder('europeana','true')"><img class="noStyle" src="images/expand/arrow-down.gif" alt="down" /></a>
                    <a class="order" href="javascript:changeOrder('europeana','false')"><img class="noStyle" src="images/expand/arrow-up.gif" alt="up" /></a>
                </div>
                </th>
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
                <c:forEach var="eacCpfResult" items="${results.eacCpfResults}">
                    <tr class="${eacCpfResult.cssClass}">
                        <td><input class="checkboxSave" type="checkbox" name="check" id="check_${eacCpfResult.id}"
                                   value="${eacCpfResult.id}" onclick="enable_features"/></td>
                        <td class="nocenter">${eacCpfResult.identifier}</td>
                        <td><span class="title">${eacCpfResult.title}</span></td>
                        <td>${eacCpfResult.date}</td>
                        <td class="${eacCpfResult.convertedCssClass}"><apenet:resource>${eacCpfResult.convertedText}</apenet:resource></td>
                        <td class="${eacCpfResult.validatedCssClass}"><apenet:resource>${eacCpfResult.validatedText}</apenet:resource></td>
                        <td class="${eacCpfResult.indexedCssClass}"><apenet:resource>${eacCpfResult.indexedText}</apenet:resource></td>
                        <td><apenet:resource>${eacCpfResult.cpfRelations} / ${eacCpfResult.resourceRelations} / ${eacCpfResult.functionRelations}</apenet:resource></td>
                        <td class="${eacCpfResult.eseEdmCssClass}"><apenet:resource>${eacCpfResult.eseEdmText}</apenet:resource>
                        <td class="${eacCpfResult.europeanaCssClass}"><apenet:resource>${eacCpfResult.europeanaText}</apenet:resource></td>
                        <td class="${eacCpfResult.queueCssClass}">
                            <c:if test="${eacCpfResult.queueReady or eacCpfResult.queueProcessing or eacCpfResult.queueError}">
                                <c:if test="${eacCpfResult.queueError}">
                                    <s:text name="content.message.error" />:
                                </c:if>
                                <apenet:resource>${eacCpfResult.queueAction.resourceName }</apenet:resource>
                            </c:if>
                        </td>

                        <td class="actions">
                            <c:choose>
                                <c:when test="${eacCpfResult.queueReady}">
                                    <select class="selectedAction" name="selectedAction">
                                        <option value="action|deleteFromQueue">
                                            <s:text name="content.message.delete.queue" />
                                        </option>
                                    </select>
                                    <input type="button" value="<s:text name="content.message.go" />" />
                                </c:when>
                                <c:when test="${eacCpfResult.queueError} ">
                                    <select class="selectedAction" name="selectedAction">
                                        <option value="action|deleteFromQueue">
                                            <s:text name="content.message.delete.queue" />
                                        </option>
                                    </select>
                                    <input type="button" value="<s:text name="content.message.go" />" />
                                </c:when>
                                <c:when test="${eacCpfResult.queueProcessing}">
                                    <s:text name="content.message.queueprocessing" />
                                </c:when>
                                <c:otherwise>
                                    <select class="selectedAction" name="selectedAction">
                                        <c:if test="${not eacCpfResult.converted and not eacCpfResult.validated}">
                                            <option value="action|convert">
                                                <s:text name="content.message.convert" />
                                            </option>
                                        </c:if>
                                        <c:if test="${not eacCpfResult.validated and not eacCpfResult.validatedFatalError}">
                                            <option value="action|validate">
                                                <s:text name="content.message.validate" />
                                            </option>
                                        </c:if>

                                        <c:if test="${eacCpfResult.validated and not eacCpfResult.published}">
                                            <option value="action|publish">
                                                <s:text name="content.message.publish" />
                                            </option>
                                        </c:if>
                                        <c:if test="${eacCpfResult.editable}">
                                            <option value="_blank|editEad.action">
                                                <s:text name="label.edit" />
                                            </option>
                                        </c:if>
                                        <c:if test="${eacCpfResult.validated}">
<%--                                            <option value="_blank|preview.action">
                                                <s:text name="content.message.preview" />
                                            </option>--%>
                                            <option value="_self|downloadEacCpf.action">
                                                <s:text name="content.message.download" />
                                            </option>
                                        </c:if>
                                        <c:if test="${eacCpfResult.containWarnings}">
                                            <option value="colorbox|showwarnings.action?iswarning=true">
                                                <s:text name="content.message.showerror" />
                                            </option>
                                        </c:if>
                                        <c:if test="${eacCpfResult.published}">
                                            <option value="action|unpublish">
                                                <s:text name="content.message.unpublish" />
                                            </option>
                                        </c:if>
                                        <%--<c:if
                                            test="${eacCpfResult.validated and not eacCpfResult.convertedToEseEdm and not eacCpfResult.deliveredToEuropeana}">
                                            <option value="_self|displayEseConvert.action">
                                                <s:text name="content.message.convert.ese" />
                                            </option>
                                        </c:if>
                                        <c:if test="${eacCpfResult.convertedToEseEdm}">
                                            <option value="action|deliverToEuropeana">
                                                <s:text name="content.message.deliver.europeana" />
                                            </option>
                                            <option value="_blank|previewEdm.action">
                                                <s:text name="content.message.preview.ese" />
                                            </option>
                                            <option value="_self|downloadEse.action">
                                                <s:text name="content.message.download.ese" />
                                            </option>
                                            <option value="action|deleteEseEdm">
                                                <s:text name="content.message.delete.ese" />
                                            </option>
                                        </c:if>
                                        <c:if test="${eacCpfResult.deliveredToEuropeana}">
                                            <option value="action|deleteFromEuropeana">
                                                <s:text name="content.message.delete.europeana" />
                                            </option>
                                        </c:if>--%>
                                        <option value="action|delete">
                                            <s:text name="content.message.delete" />
                                        </option>
                                    </select>
                                    <input type="button" value="<s:text name="content.message.go" />" />
                                </c:otherwise>
                            </c:choose>
                        </td>
                    </tr>
                </c:forEach>
                <tr class="total">
                    <td colspan="4"><s:text name="content.statistics.title.all"/></td>
                    <td>${results.totalConvertedFiles}</td>
                    <td>${results.totalValidatedFiles}</td>
                    <td>${results.totalPublishedUnits}</td>
                    <td>&nbsp;</td>
                    <td>${results.totalChos}</td>
                    <td>${results.totalChosDeliveredToEuropeana}</td>
                    <td>&nbsp;</td>
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
