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
				<input type="text" id="textIdentityCountryCodeOfTheInstitution" value="<s:property value="#countryCode" />" disabled="disabled" />
				<s:fielderror fieldName="textIdentityCountryCodeOfTheInstitution"/>
			</td>
		</tr>

		<tr>
			<td id="tdIdentifierOfTheInstitution">
				<s:property value="getText('label.ai.tabs.commons.identifierOfTheInstitution')" />
				<span class="required">*</span>:
			</td>
			<td>
				<input type="text" id="textIdentityIdentifierOfTheInstitution" value="<s:property value="#identifierOfTheInstitution" />" disabled="disabled" />
				<s:fielderror fieldName="textIdentityIdentifierOfTheInstitution"/>
			</td>
			<td id="tdIdUsedInAPE" class="labelLeft">
				<s:property value="getText('label.ai.tabs.commons.idUsedInAPE')" />:
			</td>
			<td>
				<input type="text" id="textIdentityIdUsedInAPE" value="<s:property value="#idUsedInAPE" />" disabled="disabled" />
			</td>
		</tr>

		<tr id="trNameOfTheInstitution">
			<td id="tdNameOfTheInstitution">
				<s:property value="getText('label.ai.tabs.commons.nameOfTheInstitution')" />
				<span class="required">*</span>:
			</td>
			<td>
				<input type="text" id="textIdentityIdUsedInAPE" value="<s:property value="#nameOfTheInstitution" />" disabled="disabled" />
				<s:fielderror fieldName="nameOfTheInstitution"/>
			</td>
			<td id="tdNameOfTheInstitutionLanguage" class="labelLeft">
				<label for="noti_languageList"><s:property value="getText('label.ai.tabs.commons.selectLanguage')" /></label>
				<span class="required">*</span>:
			</td>
			<td>
				<s:select theme="simple" id="noti_languageList" list="languageList" disabled="true"></s:select>
				<s:fielderror fieldName="noti_languageList"/>
			</td>
		</tr>

		<tr>
			<td colspan="2"><input id="buttonAddAnotherFormOfTheAuthorizedName" type="button" value="<s:property value='getText("label.ai.identity.addAnotherFormOfTheAuthorizedName")' />" onclick="addAnotherFormOfTheAuthorizedName();"/></td>
		</tr>

		<tr class="marginTop" id="trParallelNameOfTheInstitution" >
			<td>
				<label for="textParallelNameOfTheInstitution"><s:property value="getText('label.ai.tabs.commons.parallelNameOfTheInstitution')" />:</label>
			</td>
			<td>
				<input type="text" id="textParallelNameOfTheInstitution" value=""/>
			</td>
			<td id="tdNameOfTheInstitutionLanguage" class="labelLeft">
				<label for="pnoti_languageList"><s:property value="getText('label.ai.tabs.commons.selectLanguage')" />:</label>
			</td>
			<td>
				<s:select theme="simple" id="pnoti_languageList" list="languageList"></s:select>
			</td>
		</tr>

		<tr>
			<td colspan="2">
				<input type="button" id="buttonAddParallelNameOfTheInstitution" value="<s:property value='getText("label.ai.identity.addAnotherParallelNameOfTheInstitution")' />" onclick="addParallelNameOfTheInstitution();"/>
			</td>
		</tr>

		<tr id="trTextFormerlyUsedName" class="marginTop">
			<td>
				<label for="textFormerlyUsedName"><s:property value="getText('label.ai.identity.formerlyUsedName')" />:</label>
			</td>
			<td>
				<input type="text" id="textFormerlyUsedName" value=""/>
			</td>
			<td class="labelLeft">
				<label for="tfun_languageList"><s:property value="getText('label.ai.tabs.commons.selectLanguage')" />:</label>
			</td>
			<td>
				<s:select theme="simple" id="tfun_languageList" list="languageList"></s:select>
			</td>
		</tr>

		<tr id="trLabelDatesWhenThisNameWasUsed">
			<td colspan="4">
				<label for="textDatesWhenThisNameWasUsed"><s:property value="getText('label.ai.identity.datesWhenThisNameWasUsed')" />:</label>
			</td>
		</tr>

		<tr id="trDatesWhenThisNameWasUsed">
			<td>
				<label for="textDatesWhenThisNameWasUsedFrom"><s:property value="getText('label.ai.tabs.commons.textFrom')" />:</label>
			</td>
			<td>
				<input type="text" id="textDatesWhenThisNameWasUsedFrom" value=""/>
			</td>
			<td class="labelLeft">
				<label for="textDatesWhenThisNameWasUsedTo"><s:property value="getText('label.ai.tabs.commons.textTo')" />:</label>
			</td>
			<td>
				<input type="text" id="textDatesWhenThisNameWasUsedTo" value=""/>
			</td>
		</tr>

		<tr>
			<td>
				<input type="button" id="buttonAddMoreDates" value="<s:property value='getText("label.ai.identity.addMoreDates")' />" onclick="addMoreDates();" />
			</td>
			<td colspan="3">
			</td>
		</tr>

		<tr id="trAddMoreAnotherFormerlyUsedName">
			<td colspan="2">
				<input type="button" id="buttonAddMoreAnotherFormerlyUsedName" value="<s:property value='getText("label.ai.identity.addAnotherFormerlyUsedName")' />" onclick="addMoreAnotherFormerlyUsedName();"/>
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
				<input type="button" id="buttonIdentityTabNext" value="<s:property value='getText("label.ai.tabs.commons.button.nextTab")' />" class="rightButton" />
				<input type="button" id="buttonIdentityTabSave" value="<s:property value='getText("label.ai.tabs.commons.button.save")' />" class="rightButton" />
				<script type="text/javascript">
					//current tab
					$("table#identityTable input#buttonIdentityTabSave").click(clickIdentityAction);
				</script>
			</td>
		</tr>
	</table>
</div>