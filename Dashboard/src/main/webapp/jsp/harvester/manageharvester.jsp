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
    </table>
    <s:form action="startStopHarvester" method="post">
        <s:actionerror />
        <c:choose>
            <c:when test="${harvestActive}">
                <s:submit key="admin.harvestermanagement.harvester.stop" cssClass="mainButton" name="startButton" />
            </c:when>
            <c:otherwise>
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
                <td><c:out value="${item.ingestionprofile.nameProfile}" /></td>
                <td>
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
                        <s:submit key="admin.harvestermanagement.harvester.start" name="startHarvest" disabled="true" />
                    </s:form>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>


    <%--<h2>Items in the harvester with errors:</h2>--%>
    <%--<table class="defaultlayout">--%>
        <%--<thead>--%>
        <%--<th><s:text name="admin.harvestermanagement.institution.name" /></th>--%>
        <%--<th><s:text name="admin.harvestermanagement.eadid" /></th>--%>
        <%--<th><s:text name="admin.harvestermanagement.queue.action" /></th>--%>
        <%--<th><s:text name="admin.harvestermanagement.queue.priority" /></th>--%>
        <%--<th><s:text name="admin.harvestermanagement.queue.actions" /></th>--%>
        <%--<th><s:text name="admin.harvestermanagement.queue.errors" /></th>--%>

        <%--</thead>--%>
        <%--<tbody>--%>
        <%--<c:forEach var="item" items="${itemsWithErrors}">--%>
            <%--<tr>--%>
                <%--<td><c:out value="${item.ead.archivalInstitution.ainame}" /></td>--%>
                <%--<td><c:out value="${item.ead.eadid}" /></td>--%>
                <%--<td><c:out value="${item.action}" /></td>--%>
                <%--<td><c:out value="${item.priority}" /></td>--%>
                <%--<td><s:form action="deleteQueueItem" theme="simple">--%>
                    <%--<input type="hidden" name="queueItemId" value="${item.id}" />--%>
                    <%--<s:submit key="content.message.delete.queue"></s:submit>--%>
                <%--</s:form></td>--%>
                <%--<td><c:out value="${item.errors}" /></td>--%>

            <%--</tr>--%>
        <%--</c:forEach>--%>
        <%--</tbody>--%>
    <%--</table>--%>
</div>
