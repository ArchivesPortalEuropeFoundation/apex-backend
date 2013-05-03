<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<div id="controlTabContent">
	<table id="controlTable">
		<tr>
			<td id="tdDescriptionIdentifier">
				<label for="textDescriptionIdentifier"><s:property value="getText('label.ai.control.descriptionIdentifier')" /><span class="required">*</span>:</label>
			</td>
			<td>
				<input type="text" id="textDescriptionIdentifier" value="<s:property value="#descriptionIdentifier" />" disabled="disabled" />
			</td>
			<td colspan="2">
			</td>
		</tr>

		<tr>
			<td id="tdPesonResponsible">
				<label for="textPesonResponsible"><s:property value="getText('label.ai.control.pesonresponsible')" /><span class="required">*</span>:</label>
			</td>
			<td>
				<input type="text" id="textPesonResponsible" value="<s:property value="#pesonResponsible" />" />
			</td>
			<td id="tdLanguagePesonresponsible">
				<label for="selectLanguagePesonresponsible"><s:property value="getText('label.ai.tabs.commons.selectLanguage')" />:</label>
			</td>
			<td>
				<s:select theme="simple" id="selectLanguagePesonresponsible" list="languageList"></s:select>
			</td>
		</tr>

		<tr>
			<td id="tdIdentifierOfResponsible">
				<label for="textIdentifierOfResponsible"><s:property value="getText('label.ai.control.identifierOfResponsible')" /><span class="required">*</span>:</label>
			</td>
			<td>
				<input type="text" id="textIdentifierOfResponsible" value="<s:property value="#identifierOfResponsible" />" />
			</td>
			<td colspan="2">
			</td>
		</tr>

		<tr>
			<td id="usedLanguagesLabel" colspan="4">
				<s:property value="getText('label.ai.control.usedLanguages')" />
			</td>
			
		</tr>

		<tr id="trControlAddFurtherLangsAnsScriptsOne">
			<td id="tdDescriptionLanguage">
				<label for="selectDescriptionLanguage"><s:property value="getText('label.ai.control.descriptionLanguage')" /><span class="required">*</span>:</label>
			</td>
			<td>
				<s:select theme="simple" id="selectDescriptionLanguage" cssClass="selectControlTab" list="languageISOList"></s:select>
			</td>
			<td colspan="2">
			</td>
		</tr>
		<tr id="trControlAddFurtherLangsAnsScriptsTwo">
			<td id="tdDescriptionScript">
				<label for="selectDescriptionScript"><s:property value="getText('label.ai.control.descriptionScript')" /><span class="required">*</span>:</label>
			</td>
			<td>
				<s:select theme="simple" id="selectDescriptionScript" cssClass="selectControlTab"  list="scriptList"></s:select>
			</td>
			<td colspan="2">
			</td>
		</tr>
		<tr>
			<td id="tdAddFurtherLangsAnsScripts" colspan="2">
				<input type="button" onclick="controlAddFurtherLangsAnsScripts();" id="buttonControlAddFurtherLangsAnsScripts" value="<s:property value='getText("label.ai.control.addFurtherLangsAnsScripts")' />" />
			</td>
		</tr>
		<tr>
			<td id="usedRulesLabel" colspan="4">
				<s:property value="getText('label.ai.control.usedRules')" />
			</td>
			
		</tr>
		<tr id="trContactAbbreviationOne">
			<td id="tdContactAbbreviation">
				<label for="textContactAbbreviation"><s:property value="getText('label.ai.control.abbreviation')" />:</label>
			</td>
			<td>
				<input type="text" id="textContactAbbreviation" value="<s:property value="#contactAbbreviation" />" />
			</td>
			<td colspan="2">
			</td>
		</tr>
		<tr id="trContactAbbreviationTwo">
			<td id="tdContactFullName">
				<label for="textContactFullName"><s:property value="getText('label.ai.control.fullName')" />:</label>
			</td>
			<td>
				<input type="text" id="textContactFullName" value="<s:property value="#contactFullName" />" />
			</td>
			<td colspan="2">
			</td>
		</tr>
		<tr>
			<td id="tdAddFurtherRules" colspan="2">
				<input type="button" id="buttonControlAddFurtherRules" onclick="addContactAbbreviation();" value="<s:property value='getText("label.ai.control.addFurtherRules")' />" />
			</td>
		</tr>
		<tr>
			<td id="tdButtonsDescriptionTab" colspan="4">
				<input type="button" id="buttonControlTabNext" value="<s:property value='getText("label.ai.tabs.commons.button.nextTab")' />" class="rightButton" />
				<input type="button" id="buttonControlTabCheck" value="<s:property value='getText("label.ai.tabs.commons.button.check")' />" class="rightButton" />
				<script type="text/javascript">
					//current tab
					$("table#controlTable input#buttonControlTabCheck").click(clickControlAction);
				</script>
			</td>
		</tr>

	</table>
</div>
