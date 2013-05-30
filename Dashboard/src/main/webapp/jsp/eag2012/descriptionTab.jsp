<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<div id="headerContainer">
</div>

<div id=descriptionTabContent>
	<table id="descriptionTable" class="tablePadding">
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
				<textarea id="textRepositoryHistory" >${loader.repositorhist}</textarea>
			</td>
			<td id="tdLanguageRepositoryHistory" class="labelLeft">
				<label for="selectLanguageRepositoryHistory"><s:property value="getText('label.ai.tabs.commons.selectLanguage')" />:</label>
			</td>
			<td>
				<select id="selectLanguageRepositoryHistory">
					<s:iterator value="languageList" var="language"> 
						<option value="<s:property value="#language.key" />"<s:if test="%{#language.key == loader.repositorhistLang}" > selected=selected </s:if>><s:property value="#language.value" /></option>
					</s:iterator>
				</select>
			</td>
		</tr>

		<tr id="trAddHistoryDescription">
			<td id="tdAddHistoryDescription" colspan="2">
				<input type="button" id="buttonAddHistoryDescription" onclick="descriptionAddHistoryDescription('<s:property value="getText('label.ai.tabs.commons.pleaseFillData')" />');" value="<s:property value='getText("label.ai.description.addHistoryDescription")' />"/>
			</td>
			<td colspan="2">
			</td>
		</tr>

		<tr>
			<td id="tdDateOfRepositoryFoundation">
				<label for="textDateOfRepositoryFoundation"><s:property value="getText('label.ai.description.dateOfRepositoryFoundation')" />:</label>
			</td>
			<td>
				<input type="text" id="textDateOfRepositoryFoundation" value="${loader.repositorFoundDate}" />
			</td>
			<td colspan="2"></td>
		</tr>

		<tr id="trRuleOfRepositoryFoundation">
			<td id="tdRuleOfRepositoryFoundation">
				<label for="textRuleOfRepositoryFoundation"><s:property value="getText('label.ai.description.ruleOfRepositoryFoundation')" />:</label>
			</td>
			<td>
				<input type="text" id="textRuleOfRepositoryFoundation" value="${loader.repositorFoundRule}" />
			</td>
			<td id="tdLanguageRuleOfRepositoryFoundation" class="labelLeft">
				<label for="selectLanguageRuleOfRepositoryFoundation"><s:property value="getText('label.ai.tabs.commons.selectLanguage')" />:</label>
			</td>
			<td>
				<select id="selectLanguageRuleOfRepositoryFoundation">
					<s:iterator value="languageList" var="language"> 
						<option value="<s:property value="#language.key" />"<s:if test="%{#language.key == loader.repositorFoundRuleLang}" > selected=selected </s:if>><s:property value="#language.value" /></option>
					</s:iterator>
				</select>
			</td>
		</tr>

		<tr>
			<td id="tdAddFoundationInformation" colspan="2">
				<input type="button" id="buttonDescriptionAddFoundationInformation" value="<s:property value='getText("label.ai.description.addRule")' />" onclick="descriptionAddFoundationInformation('<s:property value="getText('label.ai.tabs.commons.pleaseFillData')" />');"/>
			</td>
			<td colspan="2">
			</td>
		</tr>

		<tr>
			<td id="tdDateOfRepositorySuppression">
				<label for="textDateOfRepositorySuppression"><s:property value="getText('label.ai.description.dateOfRepositorySuppression')" />:</label>
			</td>
			<td>
				<input type="text" id="textDateOfRepositorySuppression" value="${loader.repositorSupDate}" />
			</td>
			<td colspan="2"></td>
		</tr>

		<tr id="trDescriptionAddSuppressionInformation">
			<td id="tdRuleOfRepositorySuppression">
				<label for="textRuleOfRepositorySuppression"><s:property value="getText('label.ai.description.ruleOfRepositorySuppression')" />:</label>
			</td>
			<td>
				<input type="text" id="textRuleOfRepositorySuppression" value="${loader.repositorSupRule}" />
			</td>
			<td id="tdLanguageRuleOfRepositorySuppression" class="labelLeft">
				<label for="selectLanguageRuleOfRepositorySuppression"><s:property value="getText('label.ai.tabs.commons.selectLanguage')" />:</label>
			</td>
			<td>
				<select id="selectLanguageRuleOfRepositorySuppression">
					<s:iterator value="languageList" var="language"> 
						<option value="<s:property value="#language.key" />"<s:if test="%{#language.key == loader.repositorSupRuleLang}" > selected=selected </s:if>><s:property value="#language.value" /></option>
					</s:iterator>
				</select>
			</td>
		</tr>

		<tr>
			<td id="tdAddSuppressionInformation" colspan="2">
				<input type="button" id="buttonDescriptionAddSuppressionInformation" onclick="descriptionAddSuppressionInformation('<s:property value="getText('label.ai.tabs.commons.pleaseFillData')" />');" value="<s:property value='getText("label.ai.description.addRule")' />"/>
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
				<textarea id="textUnitOfAdministrativeStructure">${loader.adminunit}</textarea>
			</td>
			<td id="tdLanguageUnitOfAdministrativeStructure" class="labelLeft">
				<label for="selectLanguageUnitOfAdministrativeStructure"><s:property value="getText('label.ai.tabs.commons.selectLanguage')" />:</label>
			</td>
			<td>
				<select id="selectLanguageUnitOfAdministrativeStructure">
					<s:iterator value="languageList" var="language"> 
						<option value="<s:property value="#language.key" />"<s:if test="%{#language.key == loader.adminunitLang}" > selected=selected </s:if>><s:property value="#language.value" /></option>
					</s:iterator>
				</select>
			</td>
		</tr>

		<tr>
			<td id="tdAddAdministrationUnits" colspan="2">
				<input type="button" id="buttonDescriptionAddAdministrationUnits" onclick="descriptionAddAdministrationUnits('<s:property value="getText('label.ai.tabs.commons.pleaseFillData')" />');" value="<s:property value='getText("label.ai.description.addAdministrativeUnit")' />" />
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
				<textarea id="textBuilding">${loader.building}</textarea>
			</td>
			<td id="tdLanguageBuilding" class="labelLeft">
				<label for="selectLanguageBuilding"><s:property value="getText('label.ai.tabs.commons.selectLanguage')" />:</label>
			</td>
			<td>
				<select id="selectLanguageBuilding">
					<s:iterator value="languageList" var="language"> 
						<option value="<s:property value="#language.key" />"<s:if test="%{#language.key == loader.buildingLang}" > selected=selected </s:if>><s:property value="#language.value" /></option>
					</s:iterator>
				</select>
			</td>
		</tr>

		<tr>
			<td id="tdAddBuildingDescription" colspan="2">
				<input type="button" id="buttonDescriptionBuildingDescription" onclick="descriptionAddBuildingDescription('<s:property value="getText('label.ai.tabs.commons.pleaseFillData')" />');" value="<s:property value='getText("label.ai.description.addBuildingDescription")' />" />
			</td>
			<td colspan="2">
			</td>
		</tr>

		<tr>
			<td id="tdRepositoryArea">
				<label for="textRepositoryArea"><s:property value="getText('label.ai.description.repositoryArea')" />:</label>
			</td>
			<td>
				<input type="text" id="textRepositoryArea" value="${loader.repositorarea}" />
			</td>
			<td colspan="2"></td>
		</tr>

		<tr>
			<td id="tdLengthOfShelf">
				<label for="textLengthOfShelf"><s:property value="getText('label.ai.description.lengthOfShelf')" />:</label>
			</td>
			<td>
				<input type="text" id="textLengthOfShelf" value="${loader.lengthshelf}" />
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
				<textarea id="textArchivalAndOtherHoldings">${loader.holdings}</textarea>
			</td>
			<td id="tdLanguageArchivalAndOtherHoldings" class="labelLeft">
				<label for="selectLanguageArchivalAndOtherHoldings"><s:property value="getText('label.ai.tabs.commons.selectLanguage')" />:</label>
			</td>
			<td>
				<select id="selectLanguageArchivalAndOtherHoldings">
					<s:iterator value="languageList" var="language"> 
						<option value="<s:property value="#language.key" />"<s:if test="%{#language.key == loader.holdingsLang}" > selected=selected </s:if>><s:property value="#language.value" /></option>
					</s:iterator>
				</select>
			</td>
		</tr>

		<tr>
			<td id="tdAddAnotherArchivalDescription" colspan="2">
				<input type="button" id="buttonDescriptionAddAnotherArchivalDescription" onclick="descriptionAddAnotherArchivalDescription('<s:property value="getText('label.ai.tabs.commons.pleaseFillData')" />');" value="<s:property value='getText("label.ai.description.addAnotherArchivalDescription")' />" />
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
				<input type="text" id="textYearWhenThisNameWasUsed_1" value="${loader.holdingsDate}"/>
			</td>
			<td colspan="2">
			</td>
		</tr>

		<tr>
			<td>
				<input type="button" id="buttonDescriptionAddSingleYear" value="<s:property value="getText('label.ai.tabs.commons.addSingleYear')" />" onclick="addSingleYear($(this).parent().parent().parent().parent(), '<s:property value="getText('label.ai.tabs.commons.pleaseFillData')" />');" />
			</td>
			<td>
				<input type="button" id="buttonDescriptionAddYearRange" value="<s:property value="getText('label.ai.tabs.commons.addYearRange')" />" onclick="addRangeYear($(this).parent().parent().parent().parent(), '<s:property value="getText('label.ai.tabs.commons.yearFrom')" />', '<s:property value="getText('label.ai.tabs.commons.textTo')" />', '<s:property value="getText('label.ai.tabs.commons.pleaseFillData')" />');" />
			</td>
			<td colspan="2">
			</td>
		</tr>

		<tr>
			<td id="tdExtent">
				<label for="textExtent"><s:property value="getText('label.ai.description.extent')" />:</label>
			</td>
			<td>
				<input type="text" id="textExtent" value="${loader.extent}" />
			</td>
			<td colspan="2"></td>
		</tr>

		<tr>
			<td id="tdButtonsDescriptionTab" colspan="4">
				<input type="button" id="buttonDescriptionTabNext" value="<s:property value='getText("label.ai.tabs.commons.button.nextTab")' />" class="rightButton" onclick="checkAndShowNextTab($(this).parent().parent().parent().parent(), '<s:property value="getText('label.ai.tabs.commons.fieldRequired')" />', '<s:property value="getText('label.ai.tabs.commons.pleaseFillMandatoryFields')" />');" />
				<input type="button" id="buttonDescriptionTabCheck" value="<s:property value='getText("label.ai.tabs.commons.button.check")' />" class="rightButton" onclick="clickDescriptionAction('<s:property value="getText('label.ai.tabs.commons.fieldRequired')" />');"/>
			</td>
		</tr>
	</table>
</div>
