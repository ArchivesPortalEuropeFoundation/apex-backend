<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="/struts-tags" prefix="s" %>
<div id="headerContainer">
</div>

<div id="basicTabContent">
    <table>
        <tr>
            <td><s:label key="userprofiles.defaultActionUpload" /></td>
            <td><s:select id="uploadedFileAction" name="uploadedFileAction" list="uploadedFileActions" listKey="value" listValue="content" /></td>
        </tr>
        <tr>
            <td><s:label key="userprofiles.defaultActionExisting" /></td>
            <td><s:select id="existingFileAction" name="existingFileAction" list="existingFileActions" listKey="value" listValue="content" /></td>
        </tr>
        <tr>
            <td><s:label key="userprofiles.defaultActionNoEadid" /></td>
            <td><s:select id="noEadidAction" name="noEadidAction" list="noEadidActions" listKey="value" listValue="content" /></td>
        </tr>
        <tr>
            <td><s:label key="userprofiles.defaultDao" /></td>
            <td>
                <s:select id="daoType" name="daoType" list="daoTypes" listKey="value" listValue="content" />
                <s:checkbox id="daoTypeCheck" name="daoTypeCheck" />
                <s:label key="ead2ese.label.type.file" for="daoType"/>
            </td>
        </tr>
    </table>
</div>