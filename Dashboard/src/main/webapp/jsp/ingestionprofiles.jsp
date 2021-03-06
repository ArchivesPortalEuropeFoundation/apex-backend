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
      deleteColorboxForProcessing();
    });
</script>
<div>
	<span id="actionMessages" style="color:green;"><s:actionmessage theme="simple"/></span>
</div>
<div id="ingestionprofiles">
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
	<script type='text/javascript'>
    	createColorboxForProcessing(); 
	</script>
	
    <s:form id="webformIngestionprofile" method="POST" theme="simple" action="storeIngestionprofile">
        <s:actionerror />
        <table>
            <tr>
                <th>
                    <s:label key="ingestionprofiles.chooseprofile" />
                </th>
            </tr>
            <tr>
                <td>
                    <s:if test="%{ingestionprofiles.size() > 0}">
                        <s:select id="profileCb" name="profilelist" list="ingestionprofiles" listKey="value" listValue="content" value="lastSelection" />
                    </s:if>
                    <s:submit key="ingestionprofiles.createprofile" action="addIngestionprofile" />
                </td>
            </tr>
            <s:if test="%{ingestionprofiles.size() > 0}">
                <tr>
                    <td>
                        <s:label key="ingestionprofiles.profilename" for="profilenameTf" />
                        <s:textfield id="profilename" name="profileName" required="true" maxLength="100" />
                    </td>
                </tr>
                <tr>
                    <td>
                        <s:label key="ingestionprofiles.associatedFiletype" for="associatedFiletypeCb" />
                        <s:select id="associatedFiletypeCb" name="associatedFiletype" list="associatedFiletypes" listKey="value" listValue="content" onchange="changeAvailableOptions();" />
                    </td>
                </tr>
            </s:if>
        </table>
        <s:if test="%{ingestionprofiles.size() > 0}">
            <div id="ingestionprofileTabs" class="corner-all helper-clearfix">
                <ul id="ingestionprofileTabsContainer">
                    <li id="tab-basic">
                        <a href="#tab-basic"><s:property value="getText('ingestionprofiles.tab.basic')" /></a>
                    </li>
                    <li id="tab-europeana">
                        <a href="#tab-europeana"><s:property value="getText('ingestionprofiles.tab.europeana')" /></a>
                    </li>
                </ul>
                <input type="hidden" id="currentTab" value="" />
                <div id="container" class="container">
                    <div id="tab-basic">
                        <jsp:include page="ingestionprofileBasic.jsp" />
                    </div>
                    <div id="tab-europeana">
                        <jsp:include page="ingestionprofileEuropeana.jsp" />
                    </div>
                </div>
            </div>
            <table id="saveButtonPanel">
                <tr>
                    <td>
                        <input type="button" id="ingestionprofilesSave" value="<s:property value='getText("ingestionprofiles.save")' />" onclick="validateAndSave('<s:property value="getText('ingestionprofiles.error.profilename')" />', '<s:property value="getText('ingestionprofiles.error.dataprovider')" />', '<s:property value="getText('ingestionprofiles.error.edmDaoType')" />', '<s:property value="getText('ingestionprofiles.error.language')" />', '<s:property value="getText('ingestionprofiles.error.europeanalicense')" />', '<s:property value="getText('content.message.please.select.rights.statement')" />');" />
                        <s:submit id="ingestionprofilesCancel" key="ingestionprofiles.cancel" action="cancelIngestionprofileEditing" />
                    </td>
                </tr>
            </table>
        </s:if>
    </s:form>
</div>