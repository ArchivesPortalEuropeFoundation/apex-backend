<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<link rel="stylesheet" href="${pageContext.request.contextPath}/css/eaccpf/eaccpf.css" type="text/css"/>

<div id="eacCpfDiv">
	<div class="hidden">
		<div id="processingInfoDiv">
			<table id="processingInfoTable">
				<tr>
					<td>
						<img id="processingInfoImg" src="images/colorbox/loading.gif" />
					</td>
					<td>
						<label class="bold" id="processingInfoLabel" for="processingInfoImg">
							<s:text name="al.message.processing" />
						</label>
					</td>
				</tr>
			</table>
		</div>
	</div>	
	<script type="text/javascript">
		createColorboxForProcessing();
	</script>	
    <div id="validationEacCpfErrorsDiv">
        <s:actionmessage id="validationEacCpfErrors" />
    </div>
    <div id="spanMessage" class="spanEacSave" style="display: none;"></div>
    <%--<p><s:debug/></p>--%>
    <form id="webformEacCpf" name="webformEacCpf" method="POST" action="storeEacCpf.action">
        <s:hidden id="saveOrExit" name="saveOrExit" value="save"></s:hidden>
        <s:hidden id="returnPage" name="returnPage" value=""></s:hidden>
        <s:hidden id="fileId" name="fileId"></s:hidden>
        <s:hidden id="eacDaoId" name="eacDaoId" value="0"></s:hidden>
            <div id="eacCpfTabs" class="corner-all helper-clearfix">
                <ul id="eacCpfTabsContainer" class="eacCpfTabsContainer">
                    <li id="tab-identity">
                        <a href="#tab-identity"><s:property value="getText('eaccpf.tab.identity')" /></a>
                </li>
                <li id="tab-description">
                    <a href="#tab-description"><s:property value="getText('eaccpf.tab.description')" /></a>
                </li>
                <li id="tab-relations">
                    <a href="#tab-relations"><s:property value="getText('eaccpf.tab.relations')" /></a>
                </li>
                <li id="tab-control">
                    <a href="#tab-control"><s:property value="getText('eaccpf.tab.control')" /></a>
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
                    deleteColorboxForProcessing();
                });</script>
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
                <td><input type="button" class="rightButton" id="buttonPreviousTab" value="<s:property value="getText('eaccpf.commons.previousTab')" />" onclick='loadPreviousTab();' /></td>
                <td><input type="button" id="buttonNextTab" value="<s:property value="getText('eaccpf.commons.nextTab')" />" onclick='loadNextTab();' /></td>
                <td><input type="button" class="rightButton" id="buttonSaveEacCpf" value="<s:property value="getText('eaccpf.commons.save')" />" onclick="clickSaveAction('true', '<s:property value="getText('eaccpf.identity.error.empty.name.label')" />', '<s:property value="getText('eaccpf.identity.error.empty.date.label')" />', '<s:property value="getText('eaccpf.commons.error.empty.from.date')" />', '<s:property value="getText('eaccpf.commons.error.empty.to.date')" />', '<s:property value="getText('eaccpf.relations.error.typemissing.cpf')" />', '<s:property value="getText('eaccpf.relations.error.typemissing.resource')" />', '<s:property value="getText('eaccpf.relations.error.typemissing.function')" />', '<s:property value="getText('eaccpf.control.error.emptylanguage.popup')" />', '<s:property value="getText('eaccpf.control.error.emptyscript.popup')" />', '<s:property value='getText("eaccpf.commons.error.no.standard.date")' />', '<s:property value='getText("eaccpf.commons.error.no.standard.dateRange")' />', '<s:property value='getText("eaccpf.description.error.notAWebsite.popup")' />', '<s:property value='getText("eaccpf.description.error.invalidLinkSyntax.popup")' />');" /></td>
                <td><input type="button" id="buttonExit" value="<s:property value="getText('eaccpf.commons.exit')" />" onclick="clickExitAction();" /></td>
            </tr>
        </table>
    </form>
</div>
<div id="dialog-saveOnQuit">
    <div>
        <p id="DlgContent"><s:property value="getText('eaccpf.commons.exitConfirm')" /></p>
        <input id="btnYes" type="button" value="<s:property value="getText('content.message.yes')" />" 
        	onclick="clickSaveAction(false, '<s:property value="getText('eaccpf.identity.error.empty.name.label')" />', '<s:property value="getText('eaccpf.identity.error.empty.date.label')" />', '<s:property value="getText('eaccpf.commons.error.empty.from.date')" />', '<s:property value="getText('eaccpf.commons.error.empty.to.date')" />', '<s:property value="getText('eaccpf.relations.error.typemissing.cpf')" />', '<s:property value="getText('eaccpf.relations.error.typemissing.resource')" />', '<s:property value="getText('eaccpf.relations.error.typemissing.function')" />', '<s:property value="getText('eaccpf.control.error.emptylanguage.popup')" />', '<s:property value="getText('eaccpf.control.error.emptyscript.popup')" />', '<s:property value="getText('eaccpf.control.error.notAWebsite.popup')" />', '<s:property value="getText('eaccpf.control.error.invalidLinkSyntax.popup')" />');" />
        <input id="btnNo" type="button" value="<s:property value="getText('content.message.no')" />" 
        	onclick="clickExitWithoutSaveAction();" />
    </div>
</div>
