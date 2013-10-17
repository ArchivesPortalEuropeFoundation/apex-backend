<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div align="middle">
    <form action="harvestingsetsandmetadata.action" method="POST">
        <s:actionerror />

        <c:choose>
            <c:when test="${step == 0}">
                <label for="oaiprofiles">
                    <s:property value="getText('label.harvesting.currentoaipmhprofiles')"/>
                </label>
                <select name="oaiprofiles" id="oaiprofiles">
                    <option value="-1" ><s:property value="getText('label.harvesting.createnewprofile')"/></option>
                    <c:forEach var="archivalInstitutionOaiPmh" items="${archivalInstitutionOaiPmhs}">
                        <option value="${archivalInstitutionOaiPmh.id}">${archivalInstitutionOaiPmh.url} (${archivalInstitutionOaiPmh.set} - ${archivalInstitutionOaiPmh.metadataPrefix})</option>
                    </c:forEach>
                </select>
            </c:when>
            <c:when test="${step == 1}">
                <label for="url">
                    <s:property value="getText('label.harvesting.enterurl')"/>
                </label>
                <input type="text" id="url" name="url" size="255"/>
            </c:when>
            <c:when test="${step == 2}">
                <input type="hidden" id="url" name="url" value="${url}" size="255"/>
                <c:if test="${not empty sets}">
                    <label for="sets">
                        <s:property value="getText('label.harvesting.sets')"/>
                    </label>
                    <select name="selectedSet" id="sets">
                        <c:forEach var="set" items="${sets}">
                            <option value="${set}">${set}</option>
                        </c:forEach>
                    </select>
                    <br />
                </c:if>
                <label for="metadataFormats">
                    <s:property value="getText('label.harvesting.metadataformats')"/>
                </label>
                <select name="selectedMetadataFormat" id="metadataFormats">
                    <c:forEach var="metadataFormat" items="${metadataFormats}">
                        <option value="${metadataFormat}">${metadataFormat}</option>
                    </c:forEach>
                </select>
                <br />
                <label for="intervals">
                    <s:property value="getText('label.harvesting.intervals')"/>
                </label>
                <select name="intervalHarvest" id="intervals">
                    <c:forEach var="interval" items="${intervals}">
                        <option value="${interval.time}">${interval.months}</option>
                    </c:forEach>
                </select>
                <br />
                <label for="userProfiles">
                    <s:property value="getText('label.harvesting.userprofiles')"/>
                </label>
                <select name="selectedUserProfile" id="userProfiles">
                    <c:forEach var="userProfile" items="${userProfiles}">
                        <option value="${userProfile.id}">${userProfile.nameProfile}</option>
                    </c:forEach>
                </select>
            </c:when>
            <c:when test="${step == 3}">
                <s:property value="getText('label.harvesting.congratulations')"/>
            </c:when>
        </c:choose>

        <c:if test="${step != 3}">
            <br />
            <input type="submit" value="<s:property value="getText('label.ok')"/>"/>
        </c:if>
    </form>
</div>