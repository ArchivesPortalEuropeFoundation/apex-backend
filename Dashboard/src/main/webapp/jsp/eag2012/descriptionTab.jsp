<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<div id=descriptionTabContent>
	<table id="descriptionTable">
		<tr>
			<td id="repositoryLabel" colspan="4">
				<s:property value="getText('label.ai.description.visitorAddress')" />
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
				<label for="selectLanguageRepositoryHistory"><s:property value="getText('label.ai.common.select.language')" /></label>
			</td>
			<td>
				<s:select theme="simple" id="selectLanguageRepositoryHistory" list="languageList"></s:select>
			</td>
		</tr>

		<tr>
			<td id="tdDateOfRepositoryFoundation">
				<s:property value="getText('label.ai.description.dateOfRepositoryFoundation')" />:
			</td>
			<td>
				<s:property value="#dateOfRepositoryFoundation" />
			</td>
			<td colspan="2"></td>
		</tr>

		<tr>
			<td id="tdRuleOfRepositoryFoundation">
				<s:property value="getText('label.ai.description.ruleOfRepositoryFoundation')" />:
			</td>
			<td>
				<s:property value="#ruleOfRepositoryFoundation" />
			</td>
			<td id="tdLanguageRuleOfRepositoryFoundation">
				<label for="selectLanguageRuleOfRepositoryFoundation"><s:property value="getText('label.ai.common.select.language')" /></label>
			</td>
			<td>
				<s:select theme="simple" id="selectLanguageRuleOfRepositoryFoundation" list="languageList"></s:select>
			</td>
		</tr>

		<tr>
			<td id="tdAddFoundationInformation" colspan="4">
				<input type="button" value="<s:property value='getText("label.ai.description.addFoundationInformation")' />" class="longButton" />
			</td>
		</tr>

		<tr>
			<td id="tdDateOfRepositorySuppression">
				<s:property value="getText('label.ai.description.dateOfRepositorySuppression')" />:
			</td>
			<td>
				<s:property value="#dateOfRepositorySuppression" />
			</td>
			<td colspan="2"></td>
		</tr>

		<tr>
			<td id="tdRuleOfRepositorySuppression">
				<s:property value="getText('label.ai.description.ruleOfRepositorySuppression')" />:
			</td>
			<td>
				<s:property value="#ruleOfRepositorySuppression" />
			</td>
			<td id="tdLanguageRuleOfRepositorySuppression">
				<label for="selectLanguageRuleOfRepositorySuppression"><s:property value="getText('label.ai.common.select.language')" /></label>
			</td>
			<td>
				<s:select theme="simple" id="selectLanguageRuleOfRepositorySuppression" list="languageList"></s:select>
			</td>
		</tr>

		<tr>
			<td id="tdAddSuppressionInformation" colspan="4">
				<input type="button" value="<s:property value='getText("label.ai.description.addSuppressionInformation")' />" class="longButton" />
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
				<s:property value="#unitOfAdministrativeStructure" />
			</td>
			<td id="tdLanguageUnitOfAdministrativeStructure">
				<label for="selectLanguageUnitOfAdministrativeStructure"><s:property value="getText('label.ai.common.select.language')" /></label>
			</td>
			<td>
				<s:select theme="simple" id="selectLanguageUnitOfAdministrativeStructure" list="languageList"></s:select>
			</td>
		</tr>

		<tr>
			<td id="tdAddAdministrationUnits" colspan="4">
				<input type="button" value="<s:property value='getText("label.ai.description.addAdministrationUnits")' />" class="longButton" />
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
				<s:property value="#building" />
			</td>
			<td id="tdLanguageBuilding">
				<label for="selectLanguageBuilding"><s:property value="getText('label.ai.common.select.language')" /></label>
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
				<s:property value="#repositoryArea" />
			</td>
			<td colspan="2"></td>
		</tr>

		<tr>
			<td id="tdLengthOfShelf">
				<s:property value="getText('label.ai.description.lengthOfShelf')" />:
			</td>
			<td>
				<s:property value="#lengthOfShelf" />
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
				<s:property value="#archivalAndOtherHoldings" />
			</td>
			<td id="tdLanguageArchivalAndOtherHoldings">
				<label for="selectLanguageArchivalAndOtherHoldings"><s:property value="getText('label.ai.common.select.language')" /></label>
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
				<s:property value="#dateOfHoldings" />
			</td>
			<td id="tdDateOfHoldingsFrom">
				<s:property value="getText('label.ai.description.dateOfHoldingsFrom')" />:
			</td>
			<td>
				<s:property value="#dateOfHoldingsFrom" />
			</td>
			<td id="tdDateOfHoldingsTo">
				<s:property value="getText('label.ai.description.dateOfHoldingsTo')" />:
			</td>
			<td>
				<s:property value="#dateOfHoldingsTo" />
			</td>
		</tr>

		<tr>
			<td id="tdExtent">
				<s:property value="getText('label.ai.description.extent')" />:
			</td>
			<td>
				<s:property value="#extent" />
			</td>
			<td colspan="2"></td>
		</tr>

		<tr>
			<td id="tdButtonsDescriptionTab" colspan="4">
				<input type="button" id="buttonDescriptionTabNext" value="<s:property value='getText("label.ai.common.nextTab")' />" class="rightButton" />
				<input type="button" id="buttonDescriptionTabExit" value="<s:property value='getText("label.ai.common.exit")' />" class="rightButton" />
				<input type="button" id="buttonDescriptionTabSave" value="<s:property value='getText("label.ai.common.save")' />" class="rightButton" />
			</td>
		</tr>
	</table>
</div>
