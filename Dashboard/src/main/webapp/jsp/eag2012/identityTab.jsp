<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<div id="headerContainer">
</div>

<div id="identityTabContent" style="float:left;width:100%;">
	<table id="identityTable" class="tablePadding">
		<tr>
			<td id="countryCodeFieldColumn">
				<label  for="textIdentityCountryCodeOfTheInstitution"><s:property value="getText('label.ai.tabs.commons.countryCode')" /><span class="required">*</span>:</label>
			</td>
			<td>
				<input type="text" id="textIdentityCountryCodeOfTheInstitution" value="${loader.countryCode}" disabled="disabled" />
			</td>
			<td colspan="2">
			</td>
		</tr>

		<tr>
			<td id="tdIdentifierOfTheInstitution">
				<label  for="textIdentityIdentifierOfTheInstitution"><s:property value="getText('label.ai.tabs.commons.identifierOfTheInstitution')" /><span class="required">*</span>:</label>
			</td>
			<td>
				<input type="text" id="textIdentityIdentifierOfTheInstitution" value="${loader.otherRepositorId}" disabled="disabled" />
			</td>
			<td id="tdIdUsedInAPE" class="labelLeft">
				<label  for="textIdentityIdUsedInAPE"><s:property value="getText('label.ai.tabs.commons.idUsedInAPE')" />:</label>
			</td>
			<td>
				<input type="text" id="textIdentityIdUsedInAPE" value="<s:if test="%{!newEag}">${loader.recordId}</s:if>" disabled="disabled" />
			</td>
		</tr>
	</table>

	<table id="identityTableNameOfTheInstitution_1" class="tablePadding">
		<tr id="trNameOfTheInstitution">
			<td id="tdNameOfTheInstitution">
				<label for="textNameOfTheInstitution"><s:property value="getText('label.ai.tabs.commons.nameOfTheInstitution')" /><span class="required">*</span>:</label>
			</td>
			<td>
				<input type="text" id="textNameOfTheInstitution" value="${loader.autform}" disabled="disabled" />
			</td>
			<td id="tdNameOfTheInstitutionLanguage" class="labelLeft">
				<label for="noti_languageList"><s:property value="getText('label.ai.tabs.commons.selectLanguage')" />:</label>
			</td>
			<td>
				<select id="noti_languageList" disabled="disabled" >
					<s:iterator value="languageList" var="language"> 
						<option value="<s:property value="#language.key" />"<s:if test="%{#language.key == loader.autformLang}" > selected=selected </s:if>><s:property value="#language.value" /></option>
					</s:iterator>
				</select>
			</td>
		</tr>
	</table>

	<table id="identityButtonAddNames" class="tablePadding">
		<tr>
			<td colspan="3">
				<input id="buttonAddAnotherFormOfTheAuthorizedName" type="button" value="<s:property value='getText("label.ai.identity.addAnotherFormOfTheAuthorizedName")' />" onclick="addAnotherFormOfTheAuthorizedName('<s:property value="getText('label.ai.tabs.commons.pleaseFillData')" />');"/>
			</td>
			<td>
			</td>
		</tr>
	</table>

	<table id="identityTableParallelNameOfTheInstitution_1" class="tablePadding">
		<tr class="marginTop" id="trParallelNameOfTheInstitution" >
			<td>
				<label for="textParallelNameOfTheInstitution"><s:property value="getText('label.ai.tabs.commons.parallelNameOfTheInstitution')" />:</label>
			</td>
			<td>
				<input type="text" id="textParallelNameOfTheInstitution" value="${loader.parform}" disabled="disabled" />
			</td>
			<td id="tdNameOfTheInstitutionLanguage" class="labelLeft">
				<label for="pnoti_languageList"><s:property value="getText('label.ai.tabs.commons.selectLanguage')" />:</label>
			</td>
			<td>
				<select id="pnoti_languageList" disabled="disabled" >
					<s:iterator value="languageList" var="language"> 
						<option value="<s:property value="#language.key" />"<s:if test="%{#language.key == loader.parformLang}" > selected=selected </s:if>><s:property value="#language.value" /></option>
					</s:iterator>
				</select>
			</td>
		</tr>
	</table>

	<table id="identityButtonAddParallelNames" class="tablePadding">
		<tr>
			<td colspan="2">
				<input type="button" id="buttonAddParallelNameOfTheInstitution" value="<s:property value='getText("label.ai.identity.addAnotherParallelNameOfTheInstitution")' />" onclick="addParallelNameOfTheInstitution('<s:property value="getText('label.ai.tabs.commons.pleaseFillData')" />');"/>
			</td>
			<td colspan="2">
			</td>
		</tr>
	</table>

	<table id="identityButtonAddFormerlyUsedName" class="tablePadding">
		<tr id="trAddMoreAnotherFormerlyUsedName">
			<td colspan="2">
				<input type="button" id="buttonAddMoreAnotherFormerlyUsedName" value="<s:property value='getText("label.ai.identity.addAnotherFormerlyUsedName")' />" onclick="addMoreAnotherFormerlyUsedName('<s:property value="getText('label.ai.identity.formerlyUsedName')" />', '<s:property value="getText('label.ai.tabs.commons.selectLanguage')" />', '<s:property value="getText('label.ai.identity.datesWhenThisNameWasUsed')" />', '<s:property value="getText('label.ai.tabs.commons.year')" />', '<s:property value="getText('label.ai.tabs.commons.addSingleYear')" />', '<s:property value="getText('label.ai.tabs.commons.addYearRange')" />', '<s:property value="getText('label.ai.tabs.commons.yearFrom')" />', '<s:property value="getText('label.ai.tabs.commons.textTo')" />', '<s:property value="getText('label.ai.tabs.commons.pleaseFillData')" />');" />
			</td>
			<td colspan="2">
			</td>
		</tr>
	</table>

	<table id="identitySelectTypeOfTheInstitution" class="tablePadding">
		<tr>
			<td>
				<label for="textSelectTypeOfTheInstitution"><s:property value="getText('label.ai.identity.selectTypeOfTheInstitution')" />:</label>
			</td>
			<td>
				<select id="selectTypeOfTheInstitution" multiple="multiple" size="4">
					<s:iterator value="typeOfInstitutionList" var="type"> 
						<option value="<s:property value="#type.key" />"<s:if test="%{#type.key == loader.repositoryType}" > selected=selected </s:if>><s:property value="#type.value" /></option>
					</s:iterator>
				</select>
			</td>
			<td colspan="2">
			</td>
		</tr>

		<tr>
			<td id="tdButtonsContactTab" colspan="4">
				<input type="button" id="buttonIdentityTabNext" value="<s:property value='getText("label.ai.tabs.commons.button.nextTab")' />" class="rightButton" onclick="checkAndShowNextTab($(this).parent().parent().parent().parent(), '<s:property value="getText('label.ai.tabs.commons.fieldRequired')" />', '<s:property value="getText('label.ai.tabs.commons.pleaseFillMandatoryFields')" />');" />
				<input type="button" id="buttonIdentityTabCheck" value="<s:property value='getText("label.ai.tabs.commons.button.check")' />" class="rightButton" onclick="clickIdentityAction('<s:property value="getText('label.ai.tabs.commons.fieldRequired')" />');" />
			</td>
		</tr>
	</table>
</div>