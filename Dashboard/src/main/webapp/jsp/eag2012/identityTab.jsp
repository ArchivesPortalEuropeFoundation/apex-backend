<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<div id="identityTabContent" style="float:left;width:100%;">
	<table id="identityTable">
		<tr>
			<td id="countryCodeFieldColumn">
				<s:property value="getText('label.ai.identity.countryCode')" />
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
				<s:property value="getText('label.ai.identity.identifierOfTheInstitution')" />
				<span class="required">*</span>:
			</td>
			<td>
				<s:property value="#identifierOfTheInstitution" />
				<s:fielderror fieldName="identifierOfTheInstitution"/>
			</td>
			<td id="tdIdUsedInAPE">
				<s:property value="getText('label.ai.identity.idUsedInAPE')" />:
			</td>
			<td>
				<s:property value="#idUsedInAPE" />
			</td>
		</tr>

		<tr>
			<td id="tdNameOfTheInstitution">
				<s:property value="getText('label.ai.identity.nameOfTheInstitution')" />
				<span class="required">*</span>:
			</td>
			<td>
				<s:property value="#nameOfTheInstitution" />
				<s:fielderror fieldName="nameOfTheInstitution"/>
			</td>
			<td id="tdNameOfTheInstitutionLanguage">
				<label for="noti_languageList"><s:property value="getText('label.ai.identity.selectALanguage')" /></label>
				<span class="required">*</span>:
			</td>
			<td>
				<s:label>TODO: Language list</s:label>
				<%-- <s:select id="noti_languageList" list="languageList" />--%>
				<s:fielderror fieldName="noti_languageList"/>
			</td>
		</tr>

		<tr id="tr4">
			<td colspan="2"><input type="button" value="<s:property value='getText("label.ai.identity.addAnotherFormOfTheAuthorizedName")' />" class="longButton" /></td>
		</tr>

		<tr class="marginTop">
			<td>
				<label for="textParallelNameOfTheInstitution"><s:property value="getText('label.ai.identity.selectALanguage')" />:</label>
			</td>
			<td>
				<input type="text" id="textParallelNameOfTheInstitution" value=""/>
			</td>
			<td id="tdNameOfTheInstitutionLanguage">
				<label for="pnoti_languageList"><s:property value="getText('label.ai.identity.selectALanguage')" />:</label>
			</td>
			<td>
				<s:label>TODO: Language list</s:label>
				<%-- <s:select id="pnoti_languageList" list="languageList" />--%>
			</td>
		</tr>

		<tr>
			<td>
				<input type="button" value="<s:property value='getText("label.ai.identity.addAnotherFormOfTheAuthorizedName")' />" class="longButton" />
			</td>
			<td colspan="3">
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
				<label for="tfun_languageList"><s:property value="getText('label.ai.identity.selectALanguage')" />:</label>
			</td>
			<td>
				<s:label>TODO: Language list</s:label>
				<%-- <s:select id="tfun_languageList" list="languageList" />--%>
			</td>
		</tr>

		<tr>
			<td>
				<label for="textDatesWhenThisNameWasUsed"><s:property value="getText('label.ai.identity.datesWhenThisNameWasUsed')" />:</label>
			</td>
			<td colspan="3">
			</td>
		</tr>

		<tr>
			<td>
				<label for="textDatesWhenThisNameWasUsedFrom"><s:property value="getText('label.ai.identity.textFrom')" />:</label>
			</td>
			<td>
				<input type="text" id="textDatesWhenThisNameWasUsedFrom" value=""/>
			</td>
			<td>
				<label for="textDatesWhenThisNameWasUsedTo"><s:property value="getText('label.ai.identity.textTo')" />:</label>
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
			<td>
				<input type="button" id="buttonAddMoreAnotherFormerlyUsedName" value="<s:property value='getText("label.ai.identity.addMoreAnotherFormerlyUsedName")' />" class="longButton" />
			</td>
			<td colspan="3">
			</td>
		</tr>

		<tr>
			<td>
				<label for="textSelectTypeOfTheInstitution"><s:property value="getText('label.ai.identity.selectTypeOfTheInstitution')" />:</label>
			</td>
			<td>
				<s:label>TODO: Type list</s:label>
				<%-- <s:select id="typeList" list="typeList" />--%>
			</td>
			<td colspan="2">
			</td>
		</tr>

		<tr>
			<td id="tdButtonsContactTab" colspan="4">
				<input type="button" id="buttonContactTabSave" value="<s:property value='getText("label.ai.common.save")' />" class="rightButton" />
				<input type="button" id="buttonContactTabExit" value="<s:property value='getText("label.ai.common.exit")' />" class="rightButton" />
				<input type="button" id="buttonContactTabNext" value="<s:property value='getText("label.ai.common.nextTab")' />" class="rightButton" />
			</td>
		</tr>
	</table>
</div>