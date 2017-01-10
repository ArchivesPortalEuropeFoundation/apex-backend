<%-- 
    Document   : contentmanager-ead3-results
    Created on : Oct 24, 2016, 3:40:54 PM
    Author     : kaisar
--%>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="ape" uri="http://commons.archivesportaleurope.eu/tags"%>
<%@ taglib prefix="apenet" uri="http://commons.apenet.eu/tags"%>

<script type="text/javascript">
    $(document).ready(function () {
        //${securityContext.refresh_interval} is defined in SecurityContext.java to store in session the refresh timeout.
        initResultsHandlers(${securityContext.refresh_interval}, '<s:property value="getText('content.message.please.select.rights.statement')" />');
        drawColumns(false, false, false, false);
    });
</script>
<div id="queueSize">
    <c:if test="${aiItemsInQueue > 0}"><s:text name="content.message.queue.size.youritems" /> ${aiItemsInQueue}, </c:if><s:text name="content.message.queue.size.allitems" /> ${totalItemsInQueue}<c:if test="${!empty positionInQueue}">, <s:text name="content.message.queue.size.itemsbeforeyou" /> ${positionInQueue}</c:if>
        <br />
    <c:if test="${not empty errorItems}">There are problems in the queue with your files, click <span class="link" id="seeErrors">here to see</span>.
        <div class="hidden" id="errorItems">
            <c:forEach var="errorItem" items="${errorItems}">
                ${errorItem.eadidOrFilename} - ${errorItem.action}: ${errorItem.errors}<br/>
            </c:forEach>
        </div></c:if>
    </div>
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
                <!-- Option to call the action for apply a profile. -->
                <option value="displayProfile">
                    <s:text name="content.message.applyProfile" />
                </option>
                <option value="convert_validate_publish">
                    <s:text name="content.message.doitall" />
                </option>
                <%--<option value="convert">
                    <s:text name="content.message.convert" />
</option>--%>
                <option value="validate">
                    <s:text name="content.message.validate" />
                </option>
                <option value="publish">
                    <s:text name="content.message.publish" />
                </option>
                <option value="unpublish">
                    <s:text name="content.message.unpublish" />
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
                    <s:select name="resultPerPage" list="#@java.util.LinkedHashMap@{'10':'10','20':'20','30':'30','50':'50','100':'100'}" />
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
                        <th id="thLabel">
                            <s:text name="content.message.select.label" /><br /> <span class="linkList" id="selectAll">[<s:text name="content.message.select.all" />]
                            </span> - <span class="linkList" id="selectNone">[<s:text name="content.message.select.none" />]</span>
                       	</th>
                        <th id="thId">
                            <s:text name="content.message.id" />
                            <div class="arrows">
                                <a class="order" href="javascript:changeOrder('identifier','true')"><img class="noStyle" src="images/expand/arrow-down.gif" alt="down" /></a>
                                <a class="order" href="javascript:changeOrder('identifier','false')"><img class="noStyle" src="images/expand/arrow-up.gif" alt="up" /></a>
                            </div>
                        </th>
                        <th id="thTitle">
                            <s:text name="content.message.title" />
                            <div class="arrows">
                                <a class="order" href="javascript:changeOrder('title','true')"><img class="noStyle" src="images/expand/arrow-down.gif" alt="down" /></a>
                                <a class="order" href="javascript:changeOrder('title','false')"><img class="noStyle" src="images/expand/arrow-up.gif" alt="up" /></a>
                            </div>
                        </th>
                        <th id="thDate">
                            <s:text name="content.message.date" />
                            <div class="arrows">
                                <a class="order" href="javascript:changeOrder('uploadDate','true')">
                                    <img class="noStyle" src="images/expand/arrow-down.gif"	 alt="down" /></a> <a class="order" href="javascript:changeOrder('uploadDate','false')"><img class="noStyle"                                                                                      src="images/expand/arrow-up.gif" alt="up" /></a>
                            </div>
                        </th>
                        <%--<th id="thConverted">
                            <s:text name="content.message.converted" />
                            <div class="arrows">
                                <a class="order" href="javascript:changeOrder('converted','true')"><img class="noStyle" src="images/expand/arrow-down.gif" alt="down" /></a>
                                <a class="order" href="javascript:changeOrder('converted','false')"><img class="noStyle" src="images/expand/arrow-up.gif" alt="up" /></a>
                            </div>
                            <br/> <a href="#conversionOptsDiv" class="link_right" id="conversionOpts"><s:text name="content.message.conversion.options" /></a>
</th>--%>
                        <th id="thValidated">
                            <s:text name="content.message.validated" />
                            <div class="arrows">
                                <a class="order" href="javascript:changeOrder('validated','true')"><img class="noStyle" src="images/expand/arrow-down.gif" alt="down" /></a>
                                <a class="order" href="javascript:changeOrder('validated','false')"><img class="noStyle" src="images/expand/arrow-up.gif" alt="up" /></a>
                            </div>
                        </th>
                        <th id="thPublished">
                            <s:text name="content.message.published" />
                            <div class="arrows">
                                <a class="order" href="javascript:changeOrder('totalNumberOfUnits','true')"><img class="noStyle" src="images/expand/arrow-down.gif" alt="down" /></a>
                                <a class="order" href="javascript:changeOrder('totalNumberOfUnits','false')"><img class="noStyle" src="images/expand/arrow-up.gif" alt="up" /></a>
                            </div>
                        </th>

                        <th id="thQueue">
                            <s:text name="content.message.queue" />
                            <div class="arrows">
                                <a class="order" href="javascript:changeOrder('queuing','true')"><img class="noStyle" src="images/expand/arrow-down.gif" alt="down" /></a> 
                                <a class="order" href="javascript:changeOrder('queuing','false')"><img class="noStyle" src="images/expand/arrow-up.gif" alt="up" /></a>
                            </div>
                        </th>
                        <th id="thActions">
                            <s:text name="content.message.actions" />
                        </th>
                    </tr>
                </thead>
                <c:forEach var="eadResult" items="${results.ead3Results}">
                    <tr class="${eadResult.cssClass}">
                        <td id="tdLabel_${eadResult.id}"><input class="checkboxSave" type="checkbox" name="check" id="check_${eadResult.id}" value="${eadResult.id}" onclick="enable_features"/></td>
                        <td id="tdId_${eadResult.id}" class="nocenter"><c:out value="${eadResult.ead3id}"/></td>
                        <td id="tdTitle_${eadResult.id}" class="nocenter">
                            <span class="title"><c:out value="${eadResult.title}"/></span>
                        </td>
                        <td id="tdDate_${eadResult.id}">${eadResult.date}</td>
                        <%--<!--<td id="tdConverted_${eadResult.id}" class="${eadResult.convertedCssClass}"><apenet:resource>${eadResult.convertedText}</apenet:resource></td>-->--%>
                        <td id="tdValidated_${eadResult.id}" class="${eadResult.validatedCssClass}"><apenet:resource>${eadResult.validatedText}</apenet:resource></td>
                        <td id="tdPublished_${eadResult.id}" class="${eadResult.indexedCssClass}">
                            <c:choose>
                                <c:when test="${eadResult.published}">
                                    ${eadResult.units}
                                </c:when>
                            </c:choose>
                        </td>
                        <td id="tdQueue_${eadResult.id}" class="${eadResult.queueCssClass}">
                            <c:if test="${eadResult.queueReady or eadResult.queueProcessing or eadResult.queueError}">
                                <c:if test="${eadResult.queueError}"><s:text name="content.message.error" />:</c:if>
                                <apenet:resource>${eadResult.queueAction.resourceName }</apenet:resource>
                            </c:if>
                        </td>
                        <td id="tdActions_${eadResult.id}" class="actions"><c:choose>
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
                                        <%--<c:if test="${not eadResult.converted and not eadResult.validated}">
                                            <option value="action|convert">
                                                <s:text name="content.message.convert" />
                                            </option>
                                        </c:if>--%>
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
                                            <option value="_self|editEad.action">
                                                <s:text name="label.edit" />
                                            </option>
                                        </c:if>
                                        <c:if test="${eadResult.validated}">
                                            <option value="_blank|preview.action">
                                                <s:text name="content.message.preview" />
                                            </option>
                                            <c:if test="${not eadResult.dynamic}">
                                                <option value="_self|downloadEAD3.action">
                                                    <s:text name="content.message.download" />
                                                </option>
                                            </c:if>
                                        </c:if>
                                        <c:if test="${eadResult.containWarnings}">
                                            <option value="colorbox|showwarnings.action?iswarning=true">
                                                <s:text name="content.message.showerror" />
                                            </option>
                                            <c:if test="${not eadResult.validated and not eadResult.dynamic}">
                                                <option value="_self|downloadEAD3.action">
                                                    <s:text name="content.message.download" />
                                                </option>
                                            </c:if>
                                        </c:if>
                                        <c:if test="${eadResult.validated}">
                                            <option value="colorbox|showEadReport.action">
                                                <s:text name="content.message.report.ead" />
                                            </option>
                                        </c:if>

                                        <!-- Option to call the action for apply a profile. -->
                                        <!-- For finding aids display if is not deliver to Europeana. -->

                                        <!-- For holdings guides and source guides display if is not published. -->


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
                    <%--<!--<td>${results.totalConvertedFiles}</td>-->--%>
                    <td>${results.totalValidatedFiles}</td>
                    <td>${results.totalPublishedUnits}</td>
                    <td>&nbsp;</td>


                    <td>&nbsp;</td>
                </tr>
            </table>
        </div>
    </s:form>
    <div class="hidden">
        <div id="conversionOptsDiv">
            <jsp:include page="conversionOptions.jsp" />
        </div>
    </div>
</div>