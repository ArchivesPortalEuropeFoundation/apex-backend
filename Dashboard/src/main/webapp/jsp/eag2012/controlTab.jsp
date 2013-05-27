<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<div id="controlTabContent">
	<table id="controlTable">
		<tr>
			<td id="tdDescriptionIdentifier">
				<label for="textDescriptionIdentifier"><s:property value="getText('label.ai.control.descriptionIdentifier')" /><span class="required">*</span>:</label>
			</td>
			<td>
				<input type="text" id="textDescriptionIdentifier" value="${loader.recordId}" disabled="disabled" />
			</td>
			<td colspan="2">
			</td>
		</tr>

		<tr>
			<td id="tdPesonResponsible">
				<label for="textPesonResponsible"><s:property value="getText('label.ai.control.pesonresponsible')" /><span class="required">*</span>:</label>
			</td>
			<td>
				<input type="text" id="textPesonResponsible" value="${loader.agent}" onchange="controlPersonResponsibleForDescriptionChanged();" />
			</td>
			<td id="tdLanguagePesonresponsible" class="labelLeft">
				<label for="selectLanguagePesonresponsible"><s:property value="getText('label.ai.tabs.commons.selectLanguage')" />:</label>
			</td>
			<td>
				<s:select theme="simple" id="selectLanguagePesonresponsible" list="languageList" value="agentLang"></s:select>
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
				<s:select theme="simple" id="selectDescriptionLanguage" cssClass="selectControlTab" list="languageList" value="language"></s:select>
			</td>
			<td colspan="2">
			</td>
		</tr>
		<tr id="trControlAddFurtherLangsAnsScriptsTwo">
			<td id="tdDescriptionScript">
				<label for="selectDescriptionScript"><s:property value="getText('label.ai.control.descriptionScript')" /><span class="required">*</span>:</label>
			</td>
			<td>
				<s:select theme="simple" id="selectDescriptionScript" cssClass="selectControlTab"  list="scriptList" value="script"></s:select>
			</td>
			<td colspan="2">
			</td>
		</tr>
		<tr>
			<td id="tdAddFurtherLangsAnsScripts" colspan="2">
				<input type="button" onclick="controlAddFurtherLangsAnsScripts('<s:property value="getText('label.ai.tabs.commons.pleaseFillData')" />');" id="buttonControlAddFurtherLangsAnsScripts" value="<s:property value='getText("label.ai.control.addFurtherLangsAnsScripts")' />" />
			</td>
			<td colspan="2">
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
				<input type="text" id="textContactAbbreviation" value="${loader.abbreviation}" />
			</td>
			<td colspan="2">
			</td>
		</tr>
		<tr id="trContactAbbreviationTwo">
			<td id="tdContactFullName">
				<label for="textContactFullName"><s:property value="getText('label.ai.control.fullName')" />:</label>
			</td>
			<td>
				<input type="text" id="textContactFullName" value="${loader.citation}" />
			</td>
			<td colspan="2">
			</td>
		</tr>
		<tr>
			<td id="tdAddFurtherRules" colspan="2">
				<input type="button" id="buttonControlAddFurtherRules" onclick="addContactAbbreviation('<s:property value="getText('label.ai.tabs.commons.pleaseFillData')" />');" value="<s:property value='getText("label.ai.control.addFurtherRules")' />" />
			</td>
			<td colspan="2">
			</td>
		</tr>
		<tr>
			<td id="tdButtonsDescriptionTab" colspan="4">
				<input type="button" id="buttonControlTabNext" value="<s:property value='getText("label.ai.tabs.commons.button.nextTab")' />" class="rightButton" onclick="checkAndShowNextTab($(this).parent().parent().parent().parent(), '<s:property value="getText('label.ai.tabs.commons.fieldRequired')" />', '<s:property value="getText('label.ai.tabs.commons.pleaseFillMandatoryFields')" />');" />
				<input type="button" id="buttonControlTabCheck" value="<s:property value='getText("label.ai.tabs.commons.button.check")' />" class="rightButton" onclick="clickControlAction('<s:property value="getText('label.ai.tabs.commons.fieldRequired')" />');" />
			</td>
		</tr>

	</table>
</div>
