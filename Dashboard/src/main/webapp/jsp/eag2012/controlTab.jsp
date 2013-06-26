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
				<input type="text" id="textDescriptionIdentifier" value="<s:if test="%{!newEag}">${loader.recordId}</s:if>" disabled="disabled" />
			</td>
			<td colspan="2">
			</td>
		</tr>

		<tr>
			<td id="tdPesonResponsible">
				<label for="textPesonResponsible"><s:property value="getText('label.ai.control.pesonresponsible')" />:</label>
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
						<option value="<s:property value="#language.key" />"<s:if test="%{#language.key == loader.controlAgentLang}" > selected=selected </s:if>><s:property value="#language.value" /></option>
					</s:iterator>
				</select>
			</td>
		</tr>

		<tr>
			<td id="usedLanguagesLabel" colspan="4">
				<s:property value="getText('label.ai.control.usedLanguages')" />
			</td>
		</tr>

		<s:if test="%{loader.controlNumberOfLanguages.size() > 0}">
			<s:set var="counter" value="0"/>
			<s:iterator var="current" value="loader.controlNumberOfLanguages" status="status">
				<s:set var="usedLanguage" value="loader.controlLanguageDeclaration[#counter]"/>
				<s:set var="usedScript" value="loader.controlScript[#counter]"/>
				<tr id="trControlAddFurtherLangsAnsScriptsOne_<s:property value="%{#status.index + 1}" />">
					<td id="tdDescriptionLanguage_<s:property value="%{#status.index + 1}" />">
						<label for="selectDescriptionLanguage_<s:property value="%{#status.index + 1}" />"><s:property value="getText('label.ai.control.descriptionLanguage')" />:</label>
					</td>
					<td>
						<select id="selectDescriptionLanguage_<s:property value="%{#status.index + 1}" />" class="selectControlTab">
							<s:iterator value="languageList" var="language"> 
								<option value="<s:property value="#language.key" />"<s:if test="%{#language.key == #usedLanguage}" > selected=selected </s:if>><s:property value="#language.value" /></option>
							</s:iterator>
						</select>
					</td>
					<td colspan="2">
					</td>
				</tr>
	
				<tr id="trControlAddFurtherLangsAnsScriptsTwo_<s:property value="%{#status.index + 1}" />">
					<td id="tdDescriptionScript_<s:property value="%{#status.index + 1}" />">
						<label for="selectDescriptionScript_<s:property value="%{#status.index + 1}" />"><s:property value="getText('label.ai.control.descriptionScript')" />:</label>
					</td>
					<td>
						<select id="selectDescriptionScript_<s:property value="%{#status.index + 1}" />" class="selectControlTab">
							<s:iterator value="scriptList" var="script"> 
								<option value="<s:property value="#script.key" />"<s:if test="%{#script.key == #usedScript}" > selected=selected </s:if>><s:property value="#script.value" /></option>
							</s:iterator>
						</select>
					</td>
					<td colspan="2">
					</td>
				</tr>
				<s:set var="counter" value="%{#counter + 1}"/>
			</s:iterator>
		</s:if>
		<s:else>
			<tr id="trControlAddFurtherLangsAnsScriptsOne_1">
				<td id="tdDescriptionLanguage_1">
					<label for="selectDescriptionLanguage_1"><s:property value="getText('label.ai.control.descriptionLanguage')" />:</label>
				</td>
				<td>
					<select id="selectDescriptionLanguage_1" class="selectControlTab">
						<s:iterator value="languageList" var="language"> 
							<option value="<s:property value="#language.key" />"><s:property value="#language.value" /></option>
						</s:iterator>
					</select>
				</td>
				<td colspan="2">
				</td>
			</tr>

			<tr id="trControlAddFurtherLangsAnsScriptsTwo_1">
				<td id="tdDescriptionScript_1">
					<label for="selectDescriptionScript_1"><s:property value="getText('label.ai.control.descriptionScript')" />:</label>
				</td>
				<td>
					<select id="selectDescriptionScript_1" class="selectControlTab">
						<s:iterator value="scriptList" var="script"> 
							<option value="<s:property value="#script.key" />"><s:property value="#script.value" /></option>
						</s:iterator>
					</select>
				</td>
				<td colspan="2">
				</td>
			</tr>
		</s:else>

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

		<s:if test="loader.controlNumberOfRules.size() > 0">
			<s:set var="counter" value="0"/>
			<s:iterator var="current" value="loader.controlNumberOfRules" status="status">
				<s:set var="usedAbbreviation" value="loader.controlAbbreviation[#counter]"/>
				<s:set var="usedFullName" value="loader.controlCitation[#counter]"/>
				<tr id="trContactAbbreviationOne_<s:property value="%{#status.index + 1}" />">
					<td id="tdContactAbbreviation_<s:property value="%{#status.index + 1}" />">
						<label for="textContactAbbreviation_<s:property value="%{#status.index + 1}" />"><s:property value="getText('label.ai.control.abbreviation')" />:</label>
					</td>
					<td>
						<input type="text" id="textContactAbbreviation_<s:property value="%{#status.index + 1}" />" value="<s:property value="#usedAbbreviation" />" />
					</td>
					<td colspan="2">
					</td>
				</tr>

				<tr id="trContactAbbreviationTwo_<s:property value="%{#status.index + 1}" />">
					<td id="tdContactFullName_<s:property value="%{#status.index + 1}" />">
						<label for="textContactFullName_<s:property value="%{#status.index + 1}" />"><s:property value="getText('label.ai.control.fullName')" />:</label>
					</td>
					<td>
						<input type="text" id="textContactFullName_<s:property value="%{#status.index + 1}" />" value="<s:property value="#usedFullName" />" />
					</td>
					<td colspan="2">
					</td>
				</tr>
				<s:set var="counter" value="%{#counter + 1}"/>
			</s:iterator>
		</s:if>
		<s:else>
			<tr id="trContactAbbreviationOne_1">
				<td id="tdContactAbbreviation_1">
					<label for="textContactAbbreviation_1"><s:property value="getText('label.ai.control.abbreviation')" />:</label>
				</td>
				<td>
					<input type="text" id="textContactAbbreviation_1" />
				</td>
				<td colspan="2">
				</td>
			</tr>

			<tr id="trContactAbbreviationTwo_1">
				<td id="tdContactFullName_1">
					<label for="textContactFullName_1"><s:property value="getText('label.ai.control.fullName')" />:</label>
				</td>
				<td>
					<input type="text" id="textContactFullName_1" />
				</td>
				<td colspan="2">
				</td>
			</tr>
		</s:else>

		<tr>
			<td id="tdAddFurtherRules" colspan="2">
				<input type="button" id="buttonControlAddFurtherRules" onclick="addContactAbbreviation('<s:property value="getText('label.ai.tabs.commons.pleaseFillData')" />');" value="<s:property value='getText("label.ai.control.addFurtherRules")' />" />
			</td>
			<td colspan="2">
			</td>
		</tr>

		<tr>
			<td id="tdButtonsDescriptionTab" colspan="4">
				<input type="button" id="buttonControlTabNext" value="<s:property value='getText("label.ai.tabs.commons.button.nextTab")' />" class="rightButton" onclick="checkAndShowNextTab($(this).parent().parent().parent().parent(), '<s:property value="getText('label.ai.tabs.commons.fieldRequired')" />', '<s:property value="getText('label.ai.tabs.commons.pleaseFillMandatoryFields')" />','<s:property value="getText('label.eag.eagwithurlwarnings')"/>');" />
			    <input type="button" id="buttonControlTabPrevious" value="<s:property value='getText("label.ai.tabs.commons.button.previousTab")' />" class="rightButton" onclick="checkAndShowPreviousTab($(this).parent().parent().parent().parent(), '<s:property value="getText('label.ai.tabs.commons.fieldRequired')" />', '<s:property value="getText('label.ai.tabs.commons.pleaseFillMandatoryFields')" />','<s:property value="getText('label.eag.eagwithurlwarnings')"/>');" />
			</td>
		</tr>

	</table>
</div>
