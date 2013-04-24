<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<div id="identityTabContent" style="float:left;width:100%;">
	<table id="identityTable">
		<tr>
			<td id="countryCodeFieldColumn">
				<s:property value="getText('label.ai.tabs.commons.countryCode')" />
				<span class="required">*</span>:
			</td>
			<td>
				<s:property value="#countryCode" />
				<s:fielderror fieldName="countryCodeOfTheInstitution"/>
			</td>
			<td colspan="2">
			</td>
		</tr>

		<tr>
			<td id="tdIdentifierOfTheInstitution">
				<s:property value="getText('label.ai.tabs.commons.identifierOfTheInstitution')" />
				<span class="required">*</span>:
			</td>
			<td>
				<s:property value="#identifierOfTheInstitution" />
				<s:fielderror fieldName="identifierOfTheInstitution"/>
			</td>
			<td id="tdIdUsedInAPE">
				<s:property value="getText('label.ai.tabs.commons.idUsedInAPE')" />:
			</td>
			<td>
				<s:property value="#idUsedInAPE" />
			</td>
		</tr>

		<tr>
			<td id="tdNameOfTheInstitution">
				<s:property value="getText('label.ai.tabs.commons.nameOfTheInstitution')" />
				<span class="required">*</span>:
			</td>
			<td>
				<s:property value="#nameOfTheInstitution" />
				<s:fielderror fieldName="nameOfTheInstitution"/>
			</td>
			<td id="tdNameOfTheInstitutionLanguage">
				<label for="noti_languageList"><s:property value="getText('label.ai.tabs.commons.selectLanguage')" /></label>
				<span class="required">*</span>:
			</td>
			<td>
				<s:select theme="simple" id="noti_languageList" list="languageList"></s:select>
				<s:fielderror fieldName="noti_languageList"/>
			</td>
		</tr>

		<tr id="tr4">
			<td colspan="2"><input type="button" value="<s:property value='getText("label.ai.identity.addAnotherFormOfTheAuthorizedName")' />" class="longButton" /></td>
		</tr>

		<tr class="marginTop">
			<td>
				<label for="textParallelNameOfTheInstitution"><s:property value="getText('label.ai.tabs.commons.parallelNameOfTheInstitution')" />:</label>
			</td>
			<td>
				<input type="text" id="textParallelNameOfTheInstitution" value=""/>
			</td>
			<td id="tdNameOfTheInstitutionLanguage">
				<label for="pnoti_languageList"><s:property value="getText('label.ai.tabs.commons.selectLanguage')" />:</label>
			</td>
			<td>
				<s:select theme="simple" id="pnoti_languageList" list="languageList"></s:select>
			</td>
		</tr>

		<tr>
			<td colspan="2">
				<input type="button" value="<s:property value='getText("label.ai.identity.addAnotherParallelNameOfTheInstitution")' />" class="longButton" />
			</td>
			<td colspan="2">
			</td>
		</tr>

		<tr class="marginTop">
			<td>
				<label for="textFormerlyUsedName"><s:property value="getText('label.ai.identity.formerlyUsedName')" />:</label>
			</td>
			<td>
				<input type="text" id="textFormerlyUsedName" value=""/>
			</td>
			<td id="tdTextFormerlyUsedName">
				<label for="tfun_languageList"><s:property value="getText('label.ai.tabs.commons.selectLanguage')" />:</label>
			</td>
			<td>
				<s:select theme="simple" id="tfun_languageList" list="languageList"></s:select>
			</td>
		</tr>

		<tr>
			<td colspan="4">
				<label for="textDatesWhenThisNameWasUsed"><s:property value="getText('label.ai.identity.datesWhenThisNameWasUsed')" />:</label>
			</td>
		</tr>

		<tr>
			<td>
				<label for="textDatesWhenThisNameWasUsedFrom"><s:property value="getText('label.ai.tabs.commons.textFrom')" />:</label>
			</td>
			<td>
				<input type="text" id="textDatesWhenThisNameWasUsedFrom" value=""/>
			</td>
			<td>
				<label for="textDatesWhenThisNameWasUsedTo"><s:property value="getText('label.ai.tabs.commons.textTo')" />:</label>
			</td>
			<td>
				<input type="text" id="textDatesWhenThisNameWasUsedTo" value=""/>
			</td>
		</tr>

		<tr>
			<td>
				<input type="button" id="buttonAddMoreDates" value="<s:property value='getText("label.ai.identity.addMoreDates")' />" class="longButton" />
			</td>
			<td colspan="3">
			</td>
		</tr>

		<tr>
			<td colspan="2">
				<input type="button" id="buttonAddMoreAnotherFormerlyUsedName" value="<s:property value='getText("label.ai.identity.addAnotherFormerlyUsedName")' />" class="longButton" />
			</td>
			<td colspan="2">
			</td>
		</tr>

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
				<input type="button" id="buttonContactTabNext" value="<s:property value='getText("label.ai.tabs.commons.button.nextTab")' />" class="rightButton" />
				<input type="button" id="buttonContactTabExit" value="<s:property value='getText("label.ai.tabs.commons.button.exit")' />" class="rightButton" />
				<input type="button" id="buttonContactTabSave" value="<s:property value='getText("label.ai.tabs.commons.button.save")' />" class="rightButton" />
			</td>
		</tr>
	</table>
</div>