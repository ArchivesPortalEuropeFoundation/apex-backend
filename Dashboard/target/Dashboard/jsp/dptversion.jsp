<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div>
    <table class="defaultlayout">
        <tr>
            <th><s:text name="dptversion.version"/></th>
            <th><s:text name="dptversion.actions"/></th>
        </tr>
        <c:forEach var="dptUpdate" items="${dptUpdates}">
            <tr>
                <td><c:out value="${dptUpdate.version}"/></td>
                <td>
                    <s:form action="deleteVersion" theme="simple">
                        <input type="hidden" name="versionId" value="${dptUpdate.id}"/>
                        <s:submit key="dptversion.delete" name="delete" />
                    </s:form>
                </td>
            </tr>
        </c:forEach>
        <tr>
            <td colspan="2">
                <s:form action="addVersion" theme="simple">
                    <input type="text" name="versionNb" />
                    <s:submit key="dptversion.add" name="add" />
                </s:form>
            </td>
        </tr>
    </table>
</div>

