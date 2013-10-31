<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="/struts-tags" prefix="s" %>
<div id="headerContainer">
</div>

<div id="basicTabContent">
    <table>
        <tr>
            <th>
                <s:label key="userprofiles.defaultActionUpload" />
            </th>
        </tr>
        <s:iterator value="uploadedFileActions">
            <tr>
                <td class="tdVertical">
                    <s:radio theme="simple" name="uploadedFileAction" list="#{value:content}" />
                </td>
            </tr>
        </s:iterator>
        <tr>
            <th>
                <s:label key="userprofiles.defaultActionExisting" />
            </th>
        </tr>
        <s:iterator value="existingFileActions">
            <tr>
                <td class="tdVertical">
                    <s:radio theme="simple" name="existingFileAction" list="#{value:content}" />
                </td>
            </tr>
        </s:iterator>
        <tr>
            <th>
                <s:label key="userprofiles.defaultActionNoEadid" />
            </th>
        </tr>
        <s:iterator value="noEadidActions">
            <tr>
                <td class="tdVertical">
                    <s:radio theme="simple" name="noEadidAction" list="#{value:content}" />
                </td>
            </tr>
        </s:iterator>
        <tr>
            <th>
                <s:label key="userprofiles.defaultDao" />
            </th>
        </tr>
        <s:iterator value="daoTypes">
            <tr>
                <td class="tdVertical">
                    <s:radio theme="simple" name="daoType" list="#{value:content}" />
                </td>
            </tr>
        </s:iterator>
    </table>
</div>