<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div align="middle">
    <s:actionerror />

    <c:choose>
        <c:when test="${step == 0}">
                <table class="defaultlayout fullWidth">
			        <thead>
			            <tr>
			                <th><s:text name="label.harvesting.url" /></th>
			                <th><s:text name="label.harvesting.set" /></th>
			                <th><s:text name="label.harvesting.metadata" /></th>
			                <th><s:text name="label.harvesting.lastHarvest" /></th>
			                <th><s:text name="label.harvesting.newHarvest" /></th>
			                <th><s:text name="label.harvesting.from" /></th>
			                <th><s:text name="label.harvesting.userprofile" /></th>
			                <th><s:text name="label.harvesting.errors" /></th>
			                <th><s:text name="content.message.actions" /></th>
			            </tr>
			        </thead>
			        <tbody>
			        <c:forEach var="item" items="${harvestProfileItems}">

			            <tr class="${item.globalCss}">
			                <td><c:out value="${item.url}" /></td>
			                <td><c:out value="${item.set}" /></td>
			                <td><c:out value="${item.metadataPrefix}" /></td>
			                <td><c:out value="${item.lastHarvesting}" /></td>
			                <td><c:out value="${item.newHarvesting}" /></td>
			                <td><c:out value="${item.from}" /></td>
			                <td><c:out value="${item.ingestionProfile}" /></td>
			                <td  class="${item.errorCss}">
			                	<c:if test="${item.errors}">
			                		<a href="downloadHarvesterErrorsText.action?harvestId=${item.id}">ERRORS</a>
			                		<c:if test="${!empty item.errorResponsePath}">
			                			<br/><a href="downloadHarvesterErrorsXml.action?harvestId=${item.id}">OAI-PMH Response</a>
			                		</c:if>
			                	</c:if>
							</td>
			                <td>
	                            <s:form action="automaticharvestingcreationpage3.action" method="POST" theme="simple">
	                               <input type="hidden" name="oaiprofiles" value="${item.id}" />
	                               <input type="hidden" name="url" value="${item.url}" />
	                               <s:submit key="dashboard.edit.title" />
	                            </s:form>
		
			                </td>
			            </tr>
			        </c:forEach>
			        <tr>
                        <td colspan="9">
	                        <s:form action="automaticharvestingcreationpage2.action" method="POST" theme="simple">
	                           <input type="hidden" name="oaiprofiles" value="-1" />
	                          <s:submit key="label.harvesting.createnewprofile" />
	                        </s:form>           
                        </td>
                    </tr>
			        </tbody>
			    </table>
        </c:when>
        <c:when test="${step == 1}">
            <form action="automaticharvestingcreationpage3.action" method="POST">
                <table>
                    <tr>
                        <td>
                            <input type="hidden" id="oaiprofiles" name="oaiprofiles" value="${oaiprofiles}" />
                            <label for="url">
                                <s:property value="getText('label.harvesting.enterurl')"/>
                            </label>
                        </td>
                        <td>
                            <input type="text" id="url" name="url" size="255" value="${url}"/>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="2">
                            <input type="submit" value="<s:property value="getText('label.ok')"/>"/>
                        </td>
                    </tr>
                </table>
            </form>
        </c:when>
        <c:when test="${step == 2}">
            <form action="automaticharvestingcreationsave.action" method="POST">
                <input type="hidden" id="oaiprofiles" name="oaiprofiles" value="${oaiprofiles}" />
                <input type="hidden" id="url" name="url" value="${url}" size="255"/>
                <table>
                    <c:if test="${not empty sets}">
	                     <tr>
	                        <td>
	                            <label for="url">
	                                <s:property value="getText('label.harvesting.enterurl')"/>
	                            </label>
	                        </td>
	                        <td>
	                            <c:out value="${url}"/>
	                        </td>
	                    </tr>
                        <tr>
                            <td>
                                <label for="sets">
                                    <s:property value="getText('label.harvesting.sets')"/>
                                </label>
                            </td>
                            <td>
                            	<c:choose>
                            		<c:when test="${oaiprofiles > 0}">
                            			<c:out value="${selectedSet}"/>
                            		</c:when>
                            		<c:otherwise>
		                                <select name="selectedSet" id="sets">
		                                    <c:forEach var="set" items="${sets}">
		                                        <option value="${set.value}"<c:if test="${selectedSet == set.value}"> selected="selected"</c:if>>${set.content}</option>
		                                    </c:forEach>
		                                </select>                            		
                            		</c:otherwise>
                            	</c:choose>
                            </td>
                        </tr>
                    </c:if>
                    <tr>
                        <td>
                            <label for="metadataFormats">
                                <s:property value="getText('label.harvesting.metadataformats')"/>
                            </label>
                        </td>
                        <td>
                            	<c:choose>
                            		<c:when test="${oaiprofiles > 0}">
                            			<c:out value="${selectedMetadataFormat}"/>
                            		</c:when>
                            		<c:otherwise>
			                            <select name="selectedMetadataFormat" id="metadataFormats">
			                                <c:forEach var="metadataFormat" items="${metadataFormats}">
			                                    <option value="${metadataFormat.value}"<c:if test="${selectedMetadataFormat == metadataFormat.value}"> selected="selected"</c:if>>${metadataFormat.content}</option>
			                                </c:forEach>
			                            </select>                           		
                            		</c:otherwise>
                            	</c:choose>                        

                        </td>
                    </tr>
                    <tr>
                        <td>
                            <label for="intervals">
                                <s:property value="getText('label.harvesting.intervals')"/>
                            </label>
                        </td>
                        <td>
                            <select name="intervalHarvest" id="intervals">
                                <c:forEach var="interval" items="${intervals}">
                                    <option value="${interval.time}"<c:if test="${intervalHarvest == interval.time}"> selected="selected"</c:if>>${interval.months}</option>
                                </c:forEach>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <label for="weekend">
                                <s:property value="getText('label.harvesting.weekend')"/>
                            </label>
                        </td>
                        <td>
                            <select name="selectedWeekend" id="weekend">
                                <option value="false"<c:if test="${selectedWeekend == 'false'}"> selected="selected"</c:if>><s:property value="getText('content.message.no')"/></option>
                                <option value="true"<c:if test="${selectedWeekend == 'true'}"> selected="selected"</c:if>><s:property value="getText('content.message.yes')"/></option>
                            </select>
                        </td>
                    </tr>
                    <c:if test="${oaiprofiles == -1}">
                    <tr>
                        <td>
                            <label for="lastHarvestDate">
                                <s:property value="getText('label.harvesting.lastHarvesting')"/> - dd/MM/YYYY:
                            </label>
                        </td>
                        <td>
                            <input type="text" id="lastHarvestDate" name="lastHarvestDate" size="10" value="${lastHarvestDate}" />
                        </td>
                    </tr>
                    </c:if>
                    <tr>
                        <td>
                            <label for="ingestionprofiles">
                                <s:property value="getText('label.harvesting.ingestionprofiles')"/>
                            </label>
                        </td>
                        <td>
                            <select name="selectedIngestionProfile" id="ingestionProfiles">
                                <c:forEach var="ingestionProfile" items="${ingestionProfiles}">
                                    <option value="${ingestionProfile.id}"<c:if test="${selectedIngestionProfile == ingestionProfile.id}"> selected="selected"</c:if>>${ingestionProfile.nameProfile}</option>
                                </c:forEach>
                            </select>
                        </td>
                    </tr>
                        <tr>
                            <td>
                                <label for="activation">
                                    <s:property value="getText('label.harvesting.activation')"/>
                                </label>
                            </td>
                            <td>
                                <select name="selectedActivation" id="activation">
                                    <option value="true"<c:if test="${selectedActivation == 'true'}"> selected="selected"</c:if>><s:text name="label.harvesting.enabled"/></option>
                                    <option value="false"<c:if test="${selectedActivation == 'false'}"> selected="selected"</c:if>><s:text name="label.harvesting.disabled"/></option>
                                </select>
                            </td>
                        </tr>
                    <tr>
                        <td colspan="2">
                            <input type="submit" value="<s:property value="getText('label.ok')"/>"/>
                        </td>
                    </tr>
                </table>
            </form>
        </c:when>
        <c:when test="${step == 3}">
            <s:property value="getText('label.harvesting.congratulations')"/>
        </c:when>
    </c:choose>
</div>