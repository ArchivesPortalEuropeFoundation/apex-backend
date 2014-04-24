<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<link rel="stylesheet" href="${pageContext.request.contextPath}/css/eaccpf/eaccpf.css" type="text/css"/>

<div id="eacCpfDiv">
    <div id="validationEacCpfErrorsDiv">
        <s:actionmessage id="validationEacCpfErrors" />
    </div>
    <%--<p><s:debug/></p>--%>
    <form id="webformEacCpf" name="webformEacCpf" method="POST" action="storeEacCpf.action">
        <div id="eacCpfTabs" class="corner-all helper-clearfix">
            <ul id="eacCpfTabsContainer" class="eacCpfTabsContainer">
                <li id="tab-identity">
                    <a href="#tab-identity">Identity</a>
                </li>
                <li id="tab-description">
                    <a href="#tab-description">Description</a>
                </li>
                <li id="tab-relations">
                    <a href="#tab-relations">Relations</a>
                </li>
                <li id="tab-control">
                    <a href="#tab-control">Control</a>
                </li>
            </ul>
            <input type="hidden" id="currentTab" value="" />
            <script type="text/javascript">
                $(document).ready(function() {
                    hideAndShow("tab-", "tab-identity");
                    $("#currentTab").attr("value", "tab-identity");
                    $("a[href^='#tab-']").click(function() {
                        hideAndShow("tab-", $(this).attr("href").substring(1));
                    });
                });
            </script>
            <div id="container" class="container">
                <div id="tab-identity">
                    <jsp:include page="identity.jsp" />
                </div>
                <div id="tab-description">
                    <jsp:include page="description.jsp" />
                </div>
                <div id="tab-relations">
                    <jsp:include page="relations.jsp" />
                </div>
                <div id="tab-control">
                    <jsp:include page="control.jsp" />
                </div>
            </div>
        </div>
        <table id="formButtonPanel">
            <tr>
                <td><input type="button" class="rightButton" id="buttonPreviousTab" value="Previous tab" onclick='loadPreviousTab($("#currentTab").attr("value"));' /></td>
                <td><input type="button" id="buttonNextTab" value="Next tab" onclick='loadNextTab($("#currentTab").attr("value"));' /></td>
                <td><input type="button" class="rightButton" id="buttonSaveEacCpf" value="Save and exit" onclick='clickSaveAction("yada", "chooseTypeError", "identityError", "descriptionError", "relationsError", "controlError", "some message");' /></td>
                <td><input type="button" id="buttonExit" value="Exit without save" onclick='clickExitAction();' /></td>
            </tr>
        </table>
    </form>
</div>
