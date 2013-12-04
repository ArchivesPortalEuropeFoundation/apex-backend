<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div>
    <table class="defaultlayout">
        <tr>
            <th><s:text name="dptversion.version"/></th>
            <th><s:text name="dptversion.newVersion"/></th>
            <th><s:text name="dptversion.actions"/></th>
        </tr>
        <c:forEach var="dptUpdate" items="${dptUpdates}">
            <tr>
                <td><c:out value="${dptUpdate.version}"/></td>
                <td><c:out value="${dptUpdate.newVersion}"/></td>
                <td>
                    <s:form action="deleteVersion" theme="simple">
                        <input type="hidden" name="versionId" value="${dptUpdate.id}"/>
                        <s:submit key="dptversion.delete" name="delete" />
                    </s:form>
                </td>
            </tr>
        </c:forEach>
        <s:form action="addVersion" theme="simple">
            <tr>
                <td>
                    <input type="text" name="versionNb" />
                </td>
                <td>
                    <input type="text" name="newVersionNb" />
                </td>
                <td>
                    <s:submit key="dptversion.add" name="add" />
                </td>
            </tr>
        </s:form>
    </table>
</div>

