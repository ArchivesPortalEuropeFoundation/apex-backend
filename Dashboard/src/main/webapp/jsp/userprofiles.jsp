<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<script type='text/javascript'>
    $(document).ready(function() {
        initPage();

        hideAndShow("tab-", "tab-basic");
        $("#currentTab").attr("value", "tab-basic");
        $("a[href^='#tab-']").click(function() {
            hideAndShow("tab-", $(this).attr("href").substring(1));
        });
    });

</script>

<div id="userprofiles">
    <s:form id="webformUserprofile" method="POST" theme="simple" action="storeUserprofile">
        <table>
            <tr>
                <th>
                    <s:label key="userprofiles.chooseprofile" />
                </th>
            </tr>
            <tr>
                <td>
                    <s:select id="profileCb" name="profilelist" list="userprofiles" listKey="value" listValue="content" />
                    <s:submit key="userprofiles.createprofile" action="addUserprofile" />
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
                    <s:select id="associatedFiletypeCb" name="associatedFiletype" list="associatedFiletypes" listKey="value" listValue="content" />
                </td>
            </tr>
        </table>
        <div id="userprofileTabs" class="corner-all helper-clearfix">
            <ul id="userprofileTabsContainer">
                <li id="tab-basic">
                    <a href="#tab-basic"><s:property value="getText('userprofiles.tab.basic')" /></a>
                </li>
                <li id="tab-europeana">
                    <a href="#tab-europeana"><s:property value="getText('userprofiles.tab.europeana')" /></a>
                </li>
            </ul>
            <input type="hidden" id="currentTab" value="" />
            <div id="container" class="container">
                <div id="tab-basic">
                    <jsp:include page="userprofileBasic.jsp" />
                </div>
                <div id="tab-europeana">
                    <jsp:include page="userprofileEuropeana.jsp" />
                </div>
            </div>
        </div>
        <table id="saveButtonPanel">
            <tr>
                <td>
                    <s:submit id="userprofilesSave" key="userprofiles.save" />
                    <s:submit id="userprofilesCancel" key="userprofiles.cancel" action="cancelUserprofileEditing" />
                </td>
            </tr>
        </table>
    </s:form>
</div>