<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<script type='text/javascript'>
$(document).ready(function() {
	initPage();
});

</script>

<div id="userprofiles">
    <%--<s:debug />--%>
    <s:form method="POST" theme="simple">
        <table>
            <tr>
                <th>
                    <s:label key="userprofiles.chooseprofile" />
                </th>
            </tr>
            <tr>
                <td>
                    <s:select id="profileCb" name="profilelist" list="userprofiles" listKey="value" listValue="content"></s:select>
                    <input type="button" class="rightButton" value="<s:property value='getText("userprofiles.createprofile")' />" />
                </td>
            </tr>
            <tr>
                <td>
                    <s:label key="userprofiles.profilename" for="profilenameTf" />
                    <s:textfield id="profilename" name="profileName" />
                </td>
            </tr>
            <tr>
                <td>
                    <s:label key="userprofiles.associatedFiletype" for="associatedFiletypeCb" />
                    <s:select id="associatedFiletypeCb" name="associatedFiletype" list="associatedFiletypes" listKey="value" listValue="content"></s:select>
                </td>
            </tr>
            <tr>
                <th>
                    <s:label key="userprofiles.defaultActionUpload" />
                </th>
            </tr>
            <s:iterator value="uploadedFileActions">
                <tr>
                    <td>
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
                    <td>
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
                    <td>
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
                    <td>
                        <s:radio theme="simple" name="daoType" list="#{value:content}" />
                    </td>
                </tr>
            </s:iterator>
            <tr>
                <td>
                    <input type="button" class="rightButton" value="<s:property value='getText("userprofiles.editEuropeanaPreferences")' />" />
                </td>
            </tr>
            <tr>
                <td>
                    <s:submit key="userprofiles.save" />
                    <input type="button" class="rightButton" value="<s:property value='getText("userprofiles.cancel")' />" />
                </td>
            </tr>
        </table>
    </s:form>
</div>