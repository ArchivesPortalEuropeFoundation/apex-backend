<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<div id="identityTabContent" style="float:left;width:100%;">
	<table id="identityTable">
		<tr>
			<td id="countryCodeFieldColumn">
				<label  for="textIdentityCountryCodeOfTheInstitution"><s:property value="getText('label.ai.tabs.commons.countryCode')" /><span class="required">*</span>:</label>
			</td>
			<td>
				<input type="text" id="textIdentityCountryCodeOfTheInstitution" value="${countryCode}" disabled="disabled" />
			</td>
		</tr>

		<tr>
			<td id="tdIdentifierOfTheInstitution">
				<label  for="textIdentityIdentifierOfTheInstitution"><s:property value="getText('label.ai.tabs.commons.identifierOfTheInstitution')" /><span class="required">*</span>:</label>
			</td>
			<td>
				<input type="text" id="textIdentityIdentifierOfTheInstitution" value="${idOfInstitution}" disabled="disabled" />
			</td>
			<td id="tdIdUsedInAPE" class="labelLeft">
				<label  for="textIdentityIdUsedInAPE"><s:property value="getText('label.ai.tabs.commons.idUsedInAPE')" />:</label>
			</td>
			<td>
				<input type="text" id="textIdentityIdUsedInAPE" value="${idUsedInAPE}" disabled="disabled" />
			</td>
		</tr>
	</table>

	<table id="identityTableNameOfTheInstitution_1">
		<tr id="trNameOfTheInstitution">
			<td id="tdNameOfTheInstitution">
				<label for="textNameOfTheInstitution"><s:property value="getText('label.ai.tabs.commons.nameOfTheInstitution')" /><span class="required">*</span>:</label>
			</td>
			<td>
				<input type="text" id="textNameOfTheInstitution" value="${nameOfInstitution}" disabled="disabled" />
			</td>
			<td id="tdNameOfTheInstitutionLanguage" class="labelLeft">
				<label for="noti_languageList"><s:property value="getText('label.ai.tabs.commons.selectLanguage')" /><span class="required">*</span>:</label>
			</td>
			<td>
				<s:select theme="simple" id="noti_languageList" list="languageList" disabled="true"></s:select>
			</td>
		</tr>
	</table>

	<table id="identityButtonAddNames">
		<tr>
			<td colspan="2"><input id="buttonAddAnotherFormOfTheAuthorizedName" type="button" value="<s:property value='getText("label.ai.identity.addAnotherFormOfTheAuthorizedName")' />" onclick="addAnotherFormOfTheAuthorizedName('<s:property value="getText('label.ai.tabs.commons.pleaseFillData')" />');"/></td>
		</tr>
	</table>

	<table id="identityTableParallelNameOfTheInstitution_1">
		<tr class="marginTop" id="trParallelNameOfTheInstitution" >
			<td>
				<label for="textParallelNameOfTheInstitution"><s:property value="getText('label.ai.tabs.commons.parallelNameOfTheInstitution')" />:</label>
			</td>
			<td>
				<input type="text" id="textParallelNameOfTheInstitution" value="${parallelNameOfInstitution}" disabled="disabled" />
			</td>
			<td id="tdNameOfTheInstitutionLanguage" class="labelLeft">
				<label for="pnoti_languageList"><s:property value="getText('label.ai.tabs.commons.selectLanguage')" />:</label>
			</td>
			<td>
				<s:select theme="simple" id="pnoti_languageList" list="languageList" disabled="true"></s:select>
			</td>
		</tr>
	</table>

	<table id="identityButtonAddParallelNames">
		<tr>
			<td colspan="2">
				<input type="button" id="buttonAddParallelNameOfTheInstitution" value="<s:property value='getText("label.ai.identity.addAnotherParallelNameOfTheInstitution")' />" onclick="addParallelNameOfTheInstitution('<s:property value="getText('label.ai.tabs.commons.pleaseFillData')" />');"/>
			</td>
		</tr>
	</table>

	<table id="identityButtonAddFormerlyUsedName">
		<tr id="trAddMoreAnotherFormerlyUsedName">
			<td colspan="2">
				<input type="button" id="buttonAddMoreAnotherFormerlyUsedName" value="<s:property value='getText("label.ai.identity.addAnotherFormerlyUsedName")' />" onclick="addMoreAnotherFormerlyUsedName('<s:property value="getText('label.ai.identity.formerlyUsedName')" />', '<s:property value="getText('label.ai.tabs.commons.selectLanguage')" />', '<s:property value="getText('label.ai.identity.datesWhenThisNameWasUsed')" />', '<s:property value="getText('label.ai.tabs.commons.year')" />', '<s:property value="getText('label.ai.tabs.commons.addSingleYear')" />', '<s:property value="getText('label.ai.tabs.commons.addYearRange')" />', '<s:property value="getText('label.ai.tabs.commons.yearFrom')" />', '<s:property value="getText('label.ai.tabs.commons.textTo')" />', '<s:property value="getText('label.ai.tabs.commons.pleaseFillData')" />');" />
			</td>
			<td colspan="2">
			</td>
		</tr>
	</table>

	<table id="identitySelectTypeOfTheInstitution">
		<tr>
			<td>
				<label for="textSelectTypeOfTheInstitution"><s:property value="getText('label.ai.identity.selectTypeOfTheInstitution')" />:</label>
			</td>
			<td>
				<s:select theme="simple" id="selectTypeOfTheInstitution" list="typeOfInstitutionList" ></s:select>
			</td>
			<td colspan="2">
			</td>
		</tr>

		<tr>
			<td id="tdButtonsContactTab" colspan="4">
				<input type="button" id="buttonIdentityTabNext" value="<s:property value='getText("label.ai.tabs.commons.button.nextTab")' />" class="rightButton" />
				<input type="button" id="buttonIdentityTabCheck" value="<s:property value='getText("label.ai.tabs.commons.button.check")' />" class="rightButton" onclick="clickIdentityAction('<s:property value="getText('label.ai.tabs.commons.fieldRequired')" />');" />
			</td>
		</tr>
	</table>
</div>