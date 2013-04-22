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
				<s:property value="#descriptionIdentifier" />
				<s:fielderror fieldName="descriptionIdentifier"/>
			</td>
			<td colspan="2">
			</td>
		</tr>

		<tr>
			<td id="tdPesonresponsible">
				<s:property value="getText('label.ai.control.pesonresponsible')" />
				<span class="required">*</span>:
			</td>
			<td>
				<s:property value="#pesonresponsible" />
				<s:fielderror fieldName="pesonresponsible"/>
			</td>
			<td id="tdLanguagePesonresponsible">
				<s:property value="getText('label.ai.common.select.language')" />
			</td>
			<td>
				<s:label>TODO: Language list</s:label>
				<%-- <s:select id="noti_languageList" list="languageList" />--%>
			</td>
		</tr>

		<tr>
			<td id="tdIdentifierOfResponsible">
				<s:property value="getText('label.ai.control.identifierOfResponsible')" />
				<span class="required">*</span>:
			</td>
			<td>
				<s:property value="#identifierOfResponsible" />
				<s:fielderror fieldName="identifierOfResponsible"/>
			</td>
			<td colspan="2">
			</td>
		</tr>

		<tr>
			<td id="usedLanguagesLabel" colspan="4">
				<s:property value="getText('label.ai.control.usedLanguages')" />
			</td>
		</tr>

		<tr>
			<td id="tdContactLanguage">
				<s:property value="getText('label.ai.control.contactLanguage')" />
				<span class="required">*</span>:
			</td>
			<td>
				<s:property value="#contactLanguage" />
				<s:fielderror fieldName="contactLanguage"/>
			</td>
			<td colspan="2">
			</td>
		</tr>

		<tr>
			<td id="tdContactScript">
				<s:property value="getText('label.ai.control.contactScript')" />
				<span class="required">*</span>:
			</td>
			<td>
				<s:property value="#contactScript" />
				<s:fielderror fieldName="contactScript"/>
			</td>
			<td colspan="2">
			</td>
		</tr>

		<tr>
			<td id="tdAddFurtherLangsAnsScripts" colspan="4">
				<input type="button" value="<s:property value='getText("label.ai.control.addFurtherLangsAnsScripts")' />" class="longButton" />
			</td>
		</tr>

		<tr>
			<td id="usedRulesLabel" colspan="4">
				<s:property value="getText('label.ai.control.usedRules')" />
			</td>
		</tr>

		<tr>
			<td id="tdContactAbbreviation">
				<s:property value="getText('label.ai.control.contactAbbreviation')" />:
			</td>
			<td>
				<s:property value="#contactAbbreviation" />
			</td>
			<td colspan="2">
			</td>
		</tr>

		<tr>
			<td id="tdContactFullName">
				<s:property value="getText('label.ai.control.contactFullName')" />:
			</td>
			<td>
				<s:property value="#contactFullName" />
			</td>
			<td colspan="2">
			</td>
		</tr>

		<tr>
			<td id="tdAddFurtherRules" colspan="4">
				<input type="button" value="<s:property value='getText("label.ai.description.addFurtherRules")' />" class="longButton" />
			</td>
		</tr>

		<tr>
			<td id="tdButtonsDescriptionTab" colspan="4">
				<input type="button" id="buttonControlTabSave" value="<s:property value='getText("label.ai.common.save")' />" class="rightButton" />
				<input type="button" id="buttonControlTabExit" value="<s:property value='getText("label.ai.common.exit")' />" class="rightButton" />
				<input type="button" id="buttonControlTabNext" value="<s:property value='getText("label.ai.common.nextTab")' />" class="rightButton" />
			</td>
		</tr>

	</table>
</div>
