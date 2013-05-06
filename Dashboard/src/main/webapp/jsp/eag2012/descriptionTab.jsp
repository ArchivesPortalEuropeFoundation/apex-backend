<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<div id=descriptionTabContent>
	<table id="descriptionTable">
		<tr>
			<td id="repositoryLabel" colspan="4">
				<s:property value="getText('label.ai.description.repositoryDescription')" />
			</td>
			
		</tr>

		<tr id="trRepositoryHistory">
			<td id="tdRepositoryHistory">
				<label for="textRepositoryHistory"><s:property value="getText('label.ai.description.repositoryHistory')" />:</label>
			</td>
			<td>
				<textarea id="textRepositoryHistory"></textarea>
			</td>
			<td id="tdLanguageRepositoryHistory">
				<label for="selectLanguageRepositoryHistory"><s:property value="getText('label.ai.tabs.commons.selectLanguage')" />:</label>
			</td>
			<td>
				<s:select theme="simple" id="selectLanguageRepositoryHistory" list="languageList"></s:select>
			</td>
		</tr>

		<tr id="trAddHistoryDescription">
			<td id="tdAddHistoryDescription" colspan="2">
				<input type="button" id="buttonAddHistoryDescription" onclick="descriptionAddHistoryDescription();" value="<s:property value='getText("label.ai.description.addHistoryDescription")' />"/>
			</td>
			<td colspan="2">
			</td>
		</tr>

		<tr>
			<td id="tdDateOfRepositoryFoundation">
				<label for="textDateOfRepositoryFoundation"><s:property value="getText('label.ai.description.dateOfRepositoryFoundation')" />:</label>
			</td>
			<td>
				<input type="text" id="textDateOfRepositoryFoundation" value="<s:property value="#dateOfRepositoryFoundation" />" />
			</td>
			<td colspan="2"></td>
		</tr>

		<tr id="trRuleOfRepositoryFoundation">
			<td id="tdRuleOfRepositoryFoundation">
				<label for="textRuleOfRepositoryFoundation"><s:property value="getText('label.ai.description.ruleOfRepositoryFoundation')" />:</label>
			</td>
			<td>
				<input type="text" id="textRuleOfRepositoryFoundation" value="<s:property value="#ruleOfRepositoryFoundation" />" />
			</td>
			<td id="tdLanguageRuleOfRepositoryFoundation">
				<label for="selectLanguageRuleOfRepositoryFoundation"><s:property value="getText('label.ai.tabs.commons.selectLanguage')" />:</label>
			</td>
			<td>
				<s:select theme="simple" id="selectLanguageRuleOfRepositoryFoundation" list="languageList"></s:select>
			</td>
		</tr>

		<tr>
			<td id="tdAddFoundationInformation" colspan="2">
				<input type="button" id="buttonDescriptionAddFoundationInformation" value="<s:property value='getText("label.ai.description.addRule")' />" onclick="descriptionAddFoundationInformation();"/>
			</td>
			<td colspan="2">
			</td>
		</tr>

		<tr>
			<td id="tdDateOfRepositorySuppression">
				<label for="textDateOfRepositorySuppression"><s:property value="getText('label.ai.description.dateOfRepositorySuppression')" />:</label>
			</td>
			<td>
				<input type="text" id="textDateOfRepositorySuppression" value="<s:property value="#dateOfRepositorySuppression" />" />
			</td>
			<td colspan="2"></td>
		</tr>

		<tr id="trDescriptionAddSuppressionInformation">
			<td id="tdRuleOfRepositorySuppression">
				<label for="textRuleOfRepositorySuppression"><s:property value="getText('label.ai.description.ruleOfRepositorySuppression')" />:</label>
			</td>
			<td>
				<input type="text" id="textRuleOfRepositorySuppression" value="<s:property value="#ruleOfRepositorySuppression" />" />
			</td>
			<td id="tdLanguageRuleOfRepositorySuppression">
				<label for="selectLanguageRuleOfRepositorySuppression"><s:property value="getText('label.ai.tabs.commons.selectLanguage')" />:</label>
			</td>
			<td>
				<s:select theme="simple" id="selectLanguageRuleOfRepositorySuppression" list="languageList"></s:select>
			</td>
		</tr>

		<tr>
			<td id="tdAddSuppressionInformation" colspan="2">
				<input type="button" id="buttonDescriptionAddSuppressionInformation" onclick="descriptionAddSuppressionInformation();" value="<s:property value='getText("label.ai.description.addRule")' />"/>
			</td>
			<td colspan="2">
			</td>
		</tr>

		<tr>
			<td id="administrativeLabel" colspan="4">
				<s:property value="getText('label.ai.description.administrativeStructure')" />
			</td>
			
		</tr>

		<tr id="trDescriptionAddAdministrationUnits">
			<td id="tdUnitOfAdministrativeStructure">
				<label for="textUnitOfAdministrativeStructure"><s:property value="getText('label.ai.description.unitOfAdministrativeStructure')" />:</label>
			</td>
			<td>
				<textarea id="textUnitOfAdministrativeStructure"></textarea>
			</td>
			<td id="tdLanguageUnitOfAdministrativeStructure">
				<label for="selectLanguageUnitOfAdministrativeStructure"><s:property value="getText('label.ai.tabs.commons.selectLanguage')" />:</label>
			</td>
			<td>
				<s:select theme="simple" id="selectLanguageUnitOfAdministrativeStructure" list="languageList"></s:select>
			</td>
		</tr>

		<tr>
			<td id="tdAddAdministrationUnits" colspan="2">
				<input type="button" id="buttonDescriptionAddAdministrationUnits" onclick="descriptionAddAdministrationUnits();" value="<s:property value='getText("label.ai.description.addAdministrativeUnit")' />" />
			</td>
			<td colspan="2">
			</td>
		</tr>

		<tr>
			<td id="buildingLabel" colspan="4">
				<s:property value="getText('label.ai.description.buildingDescription')" />
			</td>
			
		</tr>

		<tr id="trBuildingDescription">
			<td id="tdBuilding">
				<label for="textBuilding"><s:property value="getText('label.ai.description.building')" />:</label>
			</td>
			<td>
				<textarea id="textBuilding"></textarea>
			</td>
			<td id="tdLanguageBuilding">
				<label for="selectLanguageBuilding"><s:property value="getText('label.ai.tabs.commons.selectLanguage')" />:</label>
			</td>
			<td>
				<s:select theme="simple" id="selectLanguageBuilding" list="languageList"></s:select>
			</td>
		</tr>

		<tr>
			<td id="tdAddBuildingDescription" colspan="2">
				<input type="button" id="buttonDescriptionBuildingDescription" onclick="descriptionAddBuildingDescription();" value="<s:property value='getText("label.ai.description.addBuildingDescription")' />" />
			</td>
			<td colspan="2">
			</td>
		</tr>

		<tr>
			<td id="tdRepositoryArea">
				<label for="textRepositoryArea"><s:property value="getText('label.ai.description.repositoryArea')" />:</label>
			</td>
			<td>
				<input type="text" id="textRepositoryArea" value="<s:property value="#repositoryArea" />" />
			</td>
			<td colspan="2"></td>
		</tr>

		<tr>
			<td id="tdLengthOfShelf">
				<label for="textLengthOfShelf"><s:property value="getText('label.ai.description.lengthOfShelf')" />:</label>
			</td>
			<td>
				<input type="text" id="textLengthOfShelf" value="<s:property value="#lengthOfShelf" />" />
			</td>
			<td colspan="2"></td>
		</tr>

		<tr>
			<td id="holdingLabel" colspan="4">
				<s:property value="getText('label.ai.description.holdingDescription')" />
			</td>
			
		</tr>

		<tr id="trArchivalAndOtherHoldings">
			<td id="tdArchivalAndOtherHoldings">
				<label for="textArchivalAndOtherHoldings"><s:property value="getText('label.ai.description.archivalAndOtherHoldings')" />:</label>
			</td>
			<td>
				<textarea id="textArchivalAndOtherHoldings"></textarea>
			</td>
			<td id="tdLanguageArchivalAndOtherHoldings">
				<label for="selectLanguageArchivalAndOtherHoldings"><s:property value="getText('label.ai.tabs.commons.selectLanguage')" />:</label>
			</td>
			<td>
				<s:select theme="simple" id="selectLanguageArchivalAndOtherHoldings" list="languageList"></s:select>
			</td>
		</tr>

		<tr>
			<td id="tdAddAnotherArchivalDescription" colspan="2">
				<input type="button" id="buttonDescriptionAddAnotherArchivalDescription" onclick="descriptionAddAnotherArchivalDescription();" value="<s:property value='getText("label.ai.description.addAnotherArchivalDescription")' />" />
			</td>
			<td colspan="2">
			</td>
		</tr>

		<tr>
			<td colspan="4">
				<s:property value="getText('label.ai.description.yearsOfTheHoldings')" />
			</td>
			
		</tr>

		<tr id="trYearWhenThisNameWasUsed_1">
			<td>
				<label for="textYearWhenThisNameWasUsed_1"><s:property value="getText('label.ai.tabs.commons.year')" />:</label>
			</td>
			<td>
				<input type="text" id="textYearWhenThisNameWasUsed_1" value=""/>
			</td>
			<td colspan="2">
			</td>
		</tr>

		<tr>
			<td>
				<input type="button" id="buttonDescriptionAddSingleYear" value="<s:property value="getText('label.ai.tabs.commons.addSingleYear')" />" onclick="addSingleYear($(this).parent().parent().parent().parent());" />
			</td>
			<td>
				<input type="button" id="buttonDescriptionAddYearRange" value="<s:property value="getText('label.ai.tabs.commons.addYearRange')" />" onclick="addRangeYear($(this).parent().parent().parent().parent(), '<s:property value="getText('label.ai.tabs.commons.yearFrom')" />', '<s:property value="getText('label.ai.tabs.commons.textTo')" />');" />
			</td>
			<td colspan="2">
			</td>
		</tr>

		<tr>
			<td id="tdExtent">
				<label for="textExtent"><s:property value="getText('label.ai.description.extent')" />:</label>
			</td>
			<td>
				<input type="text" id="textExtent" value="<s:property value="#extent" />" />
			</td>
			<td colspan="2"></td>
		</tr>

		<tr>
			<td id="tdButtonsDescriptionTab" colspan="4">
				<input type="button" id="buttonDescriptionTabNext" value="<s:property value='getText("label.ai.tabs.commons.button.nextTab")' />" class="rightButton" />
				<input type="button" id="buttonDescriptionTabCheck" value="<s:property value='getText("label.ai.tabs.commons.button.check")' />" class="rightButton" onclick="clickDescriptionAction();"/>
			</td>
		</tr>
	</table>
</div>
