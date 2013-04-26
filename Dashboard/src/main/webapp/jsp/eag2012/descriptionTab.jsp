<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<div id=descriptionTabContent>
	<table id="descriptionTable">
		<tr>
			<td id="repositoryLabel" colspan="4">
				<s:property value="getText('label.ai.description.repositoryDescription')" />
			</td>
		</tr>

		<tr>
			<td id="tdRepositoryHistory">
				<s:property value="getText('label.ai.description.repositoryHistory')" />:
			</td>
			<td rowspan="2">
				<textarea id="textRepositoryHistory"></textarea>
			</td>
			<td id="tdLanguageRepositoryHistory">
				<label for="selectLanguageRepositoryHistory"><s:property value="getText('label.ai.tabs.commons.selectLanguage')" />:</label>
			</td>
			<td>
				<s:select theme="simple" id="selectLanguageRepositoryHistory" list="languageList"></s:select>
			</td>
		</tr>

		<tr>
			<td>
			</td>
			<td colspan="2">
			</td>
		</tr>

		<tr>
			<td id="tdDateOfRepositoryFoundation">
				<s:property value="getText('label.ai.description.dateOfRepositoryFoundation')" />:
			</td>
			<td>
				<input type="text" id="textDateOfRepositoryFoundation" value="<s:property value="#dateOfRepositoryFoundation" />" />
			</td>
			<td colspan="2"></td>
		</tr>

		<tr>
			<td id="tdRuleOfRepositoryFoundation">
				<s:property value="getText('label.ai.description.ruleOfRepositoryFoundation')" />:
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
				<input type="button" value="<s:property value='getText("label.ai.description.addFoundationInformation")' />" />
			</td>
			<td colspan="2">
			</td>
		</tr>

		<tr>
			<td id="tdDateOfRepositorySuppression">
				<s:property value="getText('label.ai.description.dateOfRepositorySuppression')" />:
			</td>
			<td>
				<input type="text" id="textDateOfRepositorySuppression" value="<s:property value="#dateOfRepositorySuppression" />" />
			</td>
			<td colspan="2"></td>
		</tr>

		<tr>
			<td id="tdRuleOfRepositorySuppression">
				<s:property value="getText('label.ai.description.ruleOfRepositorySuppression')" />:
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
				<input type="button" value="<s:property value='getText("label.ai.description.addSuppressionInformation")' />"/>
			</td>
			<td colspan="2">
			</td>
		</tr>

		<tr>
			<td id="administrativeLabel" colspan="4">
				<s:property value="getText('label.ai.description.administrativeStructure')" />
			</td>
		</tr>

		<tr>
			<td id="tdUnitOfAdministrativeStructure">
				<s:property value="getText('label.ai.description.unitOfAdministrativeStructure')" />:
			</td>
			<td>
				<input type="text" id="textUnitOfAdministrativeStructure" value="<s:property value="#unitOfAdministrativeStructure" />" />
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
				<input type="button" value="<s:property value='getText("label.ai.description.addAdministrationUnits")' />" />
			</td>
			<td colspan="2">
			</td>
		</tr>

		<tr>
			<td id="buildingLabel" colspan="4">
				<s:property value="getText('label.ai.description.buildingDescription')" />
			</td>
		</tr>

		<tr>
			<td id="tdBuilding">
				<s:property value="getText('label.ai.description.building')" />:
			</td>
			<td>
				<input type="text" id="textBuilding" value="<s:property value="#building" />" />
			</td>
			<td id="tdLanguageBuilding">
				<label for="selectLanguageBuilding"><s:property value="getText('label.ai.tabs.commons.selectLanguage')" />:</label>
			</td>
			<td>
				<s:select theme="simple" id="selectLanguageBuilding" list="languageList"></s:select>
			</td>
		</tr>

		<tr>
			<td id="tdRepositoryArea">
				<s:property value="getText('label.ai.description.repositoryArea')" />:
			</td>
			<td>
				<input type="text" id="textRepositoryArea" value="<s:property value="#repositoryArea" />" />
			</td>
			<td colspan="2"></td>
		</tr>

		<tr>
			<td id="tdLengthOfShelf">
				<s:property value="getText('label.ai.description.lengthOfShelf')" />:
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

		<tr>
			<td id="tdArchivalAndOtherHoldings">
				<s:property value="getText('label.ai.description.archivalAndOtherHoldings')" />:
			</td>
			<td>
				<input type="text" id="textArchivalAndOtherHoldings" value="<s:property value="#archivalAndOtherHoldings" />" />
			</td>
			<td id="tdLanguageArchivalAndOtherHoldings">
				<label for="selectLanguageArchivalAndOtherHoldings"><s:property value="getText('label.ai.tabs.commons.selectLanguage')" />:</label>
			</td>
			<td>
				<s:select theme="simple" id="selectLanguageArchivalAndOtherHoldings" list="languageList"></s:select>
			</td>
		</tr>

		<tr>
			<td id="tdDateOfHoldings">
				<s:property value="getText('label.ai.description.dateOfHoldings')" />:
			</td>
			<td>
				<input type="text" id="textDateOfHoldings" value="<s:property value="#dateOfHoldings" />" />
			</td>
			<td colspan="2">
			</td>
		</tr>

		<tr>
			<td id="tdDateOfHoldingsFrom">
				<s:property value="getText('label.ai.description.dateOfHoldingsFrom')" />:
			</td>
			<td>
				<input type="text" id="textDateOfHoldingsFrom" value="<s:property value="#dateOfHoldingsFrom" />" />
			</td>
			<td id="tdDateOfHoldingsTo">
				<s:property value="getText('label.ai.tabs.commons.textTo')" />:
			</td>
			<td>
				<input type="text" id="textDateOfHoldingsTo" value="<s:property value="#dateOfHoldingsTo" />" />
			</td>
		</tr>

		<tr>
			<td id="tdExtent">
				<s:property value="getText('label.ai.description.extent')" />:
			</td>
			<td>
				<input type="text" id="textExtent" value="<s:property value="#extent" />" />
			</td>
			<td colspan="2"></td>
		</tr>

		<tr>
			<td id="tdButtonsDescriptionTab" colspan="4">
				<input type="button" id="buttonDescriptionTabNext" value="<s:property value='getText("label.ai.tabs.commons.button.nextTab")' />" class="rightButton" />
				<input type="button" id="buttonDescriptionTabSave" value="<s:property value='getText("label.ai.tabs.commons.button.save")' />" class="rightButton" />
				<script type="text/javascript">
					//current tab
					$("table#descriptionTable input#buttonDescriptionTabSave").click(clickDescriptionAction);
				</script>
			</td>
		</tr>
	</table>
</div>
