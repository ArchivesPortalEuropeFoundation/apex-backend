<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<div id="controlTabContent">
	<table id="controlTable">
		<tr>
			<td id="tdDescriptionIdentifier">
				<s:property value="getText('label.ai.control.descriptionIdentifier')" />
				<span class="required">*</span>:
			</td>
			<td>
				<input type="text" id="textDescriptionIdentifier" value="<s:property value="#descriptionIdentifier" />" />
				<s:fielderror fieldName="descriptionIdentifier"/>
			</td>
			<td colspan="2">
			</td>
		</tr>

		<tr>
			<td id="tdPesonResponsible">
				<s:property value="getText('label.ai.control.pesonresponsible')" />
				<span class="required">*</span>:
			</td>
			<td>
				<input type="text" id="textPesonResponsible" value="<s:property value="#pesonResponsible" />" />
				<s:fielderror fieldName="pesonresponsible"/>
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
				<s:property value="getText('label.ai.control.identifierOfResponsible')" />
				<span class="required">*</span>:
			</td>
			<td>
				<input type="text" id="textIdentifierOfResponsible" value="<s:property value="#identifierOfResponsible" />" />
				<s:fielderror fieldName="identifierOfResponsible"/>
			</td>
			<td colspan="2">
			</td>
		</tr>

		<tr>
			<td id="usedLanguagesLabel" colspan="2">
				<s:property value="getText('label.ai.control.usedLanguages')" />
			</td>
			<td id="tdAddFurtherLangsAnsScripts" colspan="2">
				<input type="button" value="<s:property value='getText("label.ai.control.addFurtherLangsAnsScripts")' />" class="longButton" />
			</td>
		</tr>

		<tr>
			<td id="tdDescriptionLanguage">
				<s:property value="getText('label.ai.control.descriptionLanguage')" />
				<span class="required">*</span>:
			</td>
			<td>
				<s:select theme="simple" id="selectDescriptionScript" list="languageISOList"></s:select>
				<s:fielderror fieldName="descriptionLanguage"/>
			</td>
			<td colspan="2">
			</td>
		</tr>

		<tr>
			<td id="tdDescriptionScript">
				<s:property value="getText('label.ai.control.descriptionScript')" />
				<span class="required">*</span>:
			</td>
			<td>
				<s:select theme="simple" id="selectDescriptionScript" list="scriptList"></s:select>
				<s:fielderror fieldName="descriptionScript"/>
			</td>
			<td colspan="2">
			</td>
		</tr>

		<tr>
			<td id="usedRulesLabel" colspan="2">
				<s:property value="getText('label.ai.control.usedRules')" />
			</td>
			<td id="tdAddFurtherRules" colspan="2">
				<input type="button" value="<s:property value='getText("label.ai.control.addFurtherRules")' />" class="longButton" />
			</td>
		</tr>

		<tr>
			<td id="tdContactAbbreviation">
				<s:property value="getText('label.ai.control.abbreviation')" />:
			</td>
			<td>
				<input type="text" id="textContactAbbreviation" value="<s:property value="#contactAbbreviation" />" />
			</td>
			<td colspan="2">
			</td>
		</tr>

		<tr>
			<td id="tdContactFullName">
				<s:property value="getText('label.ai.control.fullName')" />:
			</td>
			<td>
				<input type="text" id="textContactFullName" value="<s:property value="#contactFullName" />" />
			</td>
			<td colspan="2">
			</td>
		</tr>

		<tr>
			<td id="tdButtonsDescriptionTab" colspan="4">
				<input type="button" id="buttonControlTabNext" value="<s:property value='getText("label.ai.tabs.commons.button.nextTab")' />" class="rightButton" />
				<input type="button" id="buttonControlTabSave" value="<s:property value='getText("label.ai.tabs.commons.button.save")' />" class="rightButton" />
				<script type="text/javascript">
					//current tab
					$("table#controlTable input#buttonControlTabSave").click(clickControlAction);
				</script>
			</td>
		</tr>

	</table>
</div>
