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

    <h2>All saved harvest processes:</h2>
    <table class="defaultlayout">
        <thead>
            <tr>
                <th><s:text name="admin.harvestermanagement.institution.name" /></th>
                <th><s:text name="admin.harvestermanagement.oai.url" /></th>
                <th><s:text name="admin.harvestermanagement.oai.set" /></th>
                <th><s:text name="admin.harvestermanagement.oai.metadata" /></th>
                <th><s:text name="admin.harvestermanagement.oai.lastHarvest" /></th>
                <th><s:text name="admin.harvestermanagement.harvester.userprofile" /></th>
                <th><s:text name="admin.harvestermanagement.harvester.action" /></th>
            </tr>
        </thead>
        <tbody>
        <c:forEach var="item" items="${archivalInstitutionOaiPmhs}">
            <tr>
                <td><c:out value="${item.archivalInstitution.ainame}" /></td>
                <td><c:out value="${item.url}" /></td>
                <td><c:out value="${item.set}" /></td>
                <td><c:out value="${item.metadataPrefix}" /></td>
                <td><c:out value="${item.lastHarvesting}" /></td>
                <td><c:out value="${item.ingestionprofile.nameProfile}" /></td>
                <td>
                    <c:choose>
                        <c:when test="${harvestProcessing}">
                            <s:text name="admin.harvestermanagement.harvester.processing.noactions" />
                        </c:when>
                        <c:otherwise>
                            <c:choose>
                                <c:when test="${item.enabled}">
                                    <s:form action="idleHarvest" theme="simple">
                                        <input type="hidden" name="harvestId" value="${item.id}" />
                                        <s:submit key="admin.harvestermanagement.harvester.idle" name="idleHarvest" />
                                    </s:form>
                                </c:when>
                                <c:otherwise>
                                    <s:form action="activateHarvest" theme="simple">
                                        <input type="hidden" name="harvestId" value="${item.id}" />
                                        <s:submit key="admin.harvestermanagement.harvester.activate" name="activateHarvest" />
                                    </s:form>
                                </c:otherwise>
                            </c:choose>
                            <s:form action="startHarvest" theme="simple">
                                <input type="hidden" name="harvestId" value="${item.id}" />
                                <s:submit key="admin.harvestermanagement.harvester.start" name="startHarvest" />
                            </s:form>
                        </c:otherwise>
                    </c:choose>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
