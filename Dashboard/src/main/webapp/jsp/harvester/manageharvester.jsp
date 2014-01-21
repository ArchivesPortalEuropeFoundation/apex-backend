<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div id="manageHarvester">
    <table class="defaultlayout">
        <tr>
            <th><s:text name="admin.harvestermanagement.currenttime" /></th>
            <td>${currentTime}</td>
        </tr>
        <tr>
            <th><s:text name="admin.harvestermanagement.harvester.totalactiveitem" /></th>
            <td>${numberOfActiveItems}</td>
        </tr>
        <tr>
            <th><s:text name="admin.harvestermanagement.harvester.active" /></th>
            <td>${harvestActive}</td>
        </tr>
        <tr>
            <th><s:text name="admin.harvestermanagement.harvester.processing" /></th>
            <td>${harvestProcessing}</td>
        </tr>
        <tr>
            <th><s:text name="admin.harvestermanagement.harvester.daily" /></th>
            <td>${dailyHarvesting}</td>
        </tr>
        <tr>
            <th>Default Harvesting processing</th>
            <td>${defaultHarvestingProcessing}</td>
        </tr>
        
    </table>
    <s:form action="startStopHarvester" method="post">
        <s:actionerror />
        <c:choose>
            <c:when test="${harvestActive}">
                <s:submit key="admin.harvestermanagement.harvester.stop" cssClass="mainButton" name="startButton" />
            </c:when>
            <c:otherwise>
           		<s:checkbox name="processOnceADay" key="admin.harvestermanagement.harvester.daily"/>
                <s:submit key="admin.harvestermanagement.harvester.start" cssClass="mainButton" name="startButton" />
            </c:otherwise>
        </c:choose>
    </s:form>
	<h2>First items</h2>
                <table class="defaultlayout fullWidth">
			        <thead>
			            <tr>
			            	<th>ID</th>
			             	<th>Country</th>
			             	<th>Archival Institution</th>
			                <th><s:text name="label.harvesting.url" /></th>
			                <th><s:text name="label.harvesting.set" /></th>
			                <th><s:text name="label.harvesting.metadata" /></th>
			                <th><s:text name="label.harvesting.lastHarvest" /></th>
			                <th><s:text name="label.harvesting.newHarvest" /></th>
			                <th><s:text name="label.harvesting.from" /></th>
			                <th>Only weekend</th>
			                <th><s:text name="label.harvesting.userprofile" /></th>
			                <th><s:text name="label.harvesting.errors" /></th>
			            </tr>
			        </thead>
			        <tbody>
			        <c:forEach var="item" items="${firstItems}">

			            <tr class="${item.globalCss}">
			            	<td><c:out value="${item.id}" /></td>
			            	<td><c:out value="${item.country}" /></td>
			            	<td><c:out value="${item.ainame}" /></td>
			                <td><c:out value="${item.url}" /></td>
			                <td><c:out value="${item.set}" /></td>
			                <td><c:out value="${item.metadataPrefix}" /></td>
			                <td><c:out value="${item.lastHarvesting}" /></td>
			                <td><c:out value="${item.newHarvesting}" /></td>
			                <td><c:out value="${item.from}" /></td>
			                <td><c:out value="${item.harvestOnlyWeekend}" /></td>
			                <td><c:out value="${item.ingestionProfile}" /></td>
			                <td class="${item.errorCss}"><c:out value="${item.errors}" /></td>

			            </tr>
			        </c:forEach>
			        </tbody>
			    </table>
    	<h2>All saved harvest processes:</h2>
                <table class="defaultlayout fullWidth">
			        <thead>
			            <tr>
			            	<th>ID</th>
			             	<th>Country</th>
			             	<th>Archival Institution</th>
			                <th><s:text name="label.harvesting.url" /></th>
			                <th><s:text name="label.harvesting.set" /></th>
			                <th><s:text name="label.harvesting.metadata" /></th>
			                <th><s:text name="label.harvesting.lastHarvest" /></th>
			                <th><s:text name="label.harvesting.newHarvest" /></th>
			                <th><s:text name="label.harvesting.from" /></th>
			                <th>Only weekend</th>
			                <th><s:text name="label.harvesting.userprofile" /></th>
			                <th><s:text name="label.harvesting.errors" /></th>
			                <th>Actions</th>
			            </tr>
			        </thead>
			        <tbody>
			        <c:forEach var="item" items="${allOaiProfiles}">

			            <tr class="${item.globalCss}">
			            	<td><c:out value="${item.id}" /></td>
			            	<td><c:out value="${item.country}" /></td>
			            	<td><c:out value="${item.ainame}" /></td>
			                <td><c:out value="${item.url}" /></td>
			                <td><c:out value="${item.set}" /></td>
			                <td><c:out value="${item.metadataPrefix}" /></td>
			                <td><c:out value="${item.lastHarvesting}" /></td>
			                <td><c:out value="${item.newHarvesting}" /></td>
			                <td><c:out value="${item.from}" /></td>
			                <td><c:out value="${item.harvestOnlyWeekend}" /></td>
			                <td><c:out value="${item.ingestionProfile}" /></td>
			                <td  class="${item.errorCss}"><c:out value="${item.errors}" /></td>
			                <td class="actions">
			                    <c:choose>
			                        <c:when test="${harvestProcessing}">
			                            <s:text name="admin.harvestermanagement.harvester.processing.noactions" />
			                        </c:when>
			                        <c:otherwise>
			 
			                        <s:form action="manageHarvestItem" theme="simple">
			                        	<input type="hidden" name="harvestId" value="${item.id}" />
			                        	<select class="selectedAction" name="selectedAction">
			                            <c:choose>
			                                <c:when test="${item.enabled}">
			                                	<option value="NOW">
													<s:text name="admin.harvestermanagement.harvester.start" />
												</option>			                                
			                                	<option value="DISABLE">
													<s:text name="admin.harvestermanagement.harvester.idle" />
												</option>
			                                </c:when>
			                                <c:otherwise>
			                                	<option value="ENABLE">
													<s:text name="admin.harvestermanagement.harvester.activate" />
												</option>
			                                	<option value="DELETE">
													<s:text name="admin.harvestermanagement.harvester.delete" />
												</option>
			                                </c:otherwise>
			                            </c:choose>
			                            		<option value="FULL">
													Harvest everything
												</option>
			                            </select>
			                            	<input type="submit" value="<s:text name="content.message.go" />" />	
			                            </s:form>
			                        </c:otherwise>
			                    </c:choose>
			                </td>
			            </tr>
			        </c:forEach>
			        </tbody>
			    </table>
</div>
