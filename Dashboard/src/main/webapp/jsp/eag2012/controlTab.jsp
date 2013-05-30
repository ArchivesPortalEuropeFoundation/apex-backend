<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<div id="headerContainer">
</div>

<div id="controlTabContent">
	<table id="controlTable" class="tablePadding">
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
				<select id="selectLanguagePesonresponsible">
					<s:iterator value="languageList" var="language"> 
						<option value="<s:property value="#language.key" />"<s:if test="%{#language.key == loader.agentLang}" > selected=selected </s:if>><s:property value="#language.value" /></option>
					</s:iterator>
				</select>
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
				<select id="selectDescriptionLanguage" class="selectControlTab">
					<s:iterator value="languageList" var="language"> 
						<option value="<s:property value="#language.key" />"<s:if test="%{#language.key == loader.language}" > selected=selected </s:if>><s:property value="#language.value" /></option>
					</s:iterator>
				</select>
			</td>
			<td colspan="2">
			</td>
		</tr>
		<tr id="trControlAddFurtherLangsAnsScriptsTwo">
			<td id="tdDescriptionScript">
				<label for="selectDescriptionScript"><s:property value="getText('label.ai.control.descriptionScript')" /><span class="required">*</span>:</label>
			</td>
			<td>
				<select id="selectDescriptionScript" class="selectControlTab">
					<s:iterator value="scriptList" var="script"> 
						<option value="<s:property value="#script.key" />"<s:if test="%{#script.key == loader.script}" > selected=selected </s:if>><s:property value="#script.value" /></option>
					</s:iterator>
				</select>
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
