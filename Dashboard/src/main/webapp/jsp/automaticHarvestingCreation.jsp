<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div align="middle">
    <s:actionerror />

    <c:choose>
        <c:when test="${step == 0}">
            <form action="automaticharvestingcreationpage2.action" method="POST">
                <label for="oaiprofiles">
                    <s:property value="getText('label.harvesting.currentoaipmhprofiles')"/>
                </label>
                <select name="oaiprofiles" id="oaiprofiles">
                    <option value="-1" ><s:property value="getText('label.harvesting.createnewprofile')"/></option>
                    <c:forEach var="archivalInstitutionOaiPmh" items="${archivalInstitutionOaiPmhs}">
                        <option value="${archivalInstitutionOaiPmh.id}">${archivalInstitutionOaiPmh.url} (${archivalInstitutionOaiPmh.set} - ${archivalInstitutionOaiPmh.metadataPrefix})</option>
                    </c:forEach>
                </select>
                <br />
                <input type="submit" value="<s:property value="getText('label.ok')"/>"/>
            </form>
        </c:when>
        <c:when test="${step == 1}">
            <form action="automaticharvestingcreationpage3.action" method="POST">
                <input type="hidden" id="oaiprofiles" name="oaiprofiles" value="${oaiprofiles}" />
                <label for="url">
                    <s:property value="getText('label.harvesting.enterurl')"/>
                </label>
                <input type="text" id="url" name="url" size="255" value="${url}"/>
                <br />
                <input type="submit" value="<s:property value="getText('label.ok')"/>"/>
            </form>
        </c:when>
        <c:when test="${step == 2}">
            <form action="automaticharvestingcreationsave.action" method="POST">
                <input type="hidden" id="oaiprofiles" name="oaiprofiles" value="${oaiprofiles}" />
                <input type="hidden" id="url" name="url" value="${url}" size="255"/>
                <c:if test="${not empty sets}">
                    <label for="sets">
                        <s:property value="getText('label.harvesting.sets')"/>
                    </label>
                    <select name="selectedSet" id="sets">
                        <c:forEach var="set" items="${sets}">
                            <option value="${set}"<c:if test="${selectedSet == set}"> selected="selected"</c:if>>${set}</option>
                        </c:forEach>
                    </select>
                    <br />
                </c:if>
                <label for="metadataFormats">
                    <s:property value="getText('label.harvesting.metadataformats')"/>
                </label>
                <select name="selectedMetadataFormat" id="metadataFormats">
                    <c:forEach var="metadataFormat" items="${metadataFormats}">
                        <option value="${metadataFormat}"<c:if test="${selectedMetadataFormat == metadataFormat}"> selected="selected"</c:if>>${metadataFormat}</option>
                    </c:forEach>
                </select>
                <br />
                <label for="intervals">
                    <s:property value="getText('label.harvesting.intervals')"/>
                </label>
                <select name="intervalHarvest" id="intervals">
                    <c:forEach var="interval" items="${intervals}">
                        <option value="${interval.time}"<c:if test="${intervalHarvest == interval.time}"> selected="selected"</c:if>>${interval.months}</option>
                    </c:forEach>
                </select>
                <br />
                <label for="userProfiles">
                    <s:property value="getText('label.harvesting.userprofiles')"/>
                </label>
                <select name="selectedUserProfile" id="userProfiles">
                    <c:forEach var="userProfile" items="${userProfiles}">
                        <option value="${userProfile.id}"<c:if test="${selectedUserProfile == userProfile.id}"> selected="selected"</c:if>>${userProfile.nameProfile}</option>
                    </c:forEach>
                </select>
                <br />
                <input type="submit" value="<s:property value="getText('label.ok')"/>"/>
            </form>
        </c:when>
        <c:when test="${step == 3}">
            <s:property value="getText('label.harvesting.congratulations')"/>
        </c:when>
    </c:choose>
</div>