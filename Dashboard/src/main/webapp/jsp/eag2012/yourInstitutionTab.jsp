<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<div id="yourInstitutionTabContent">
	<table id="yourInstitutionTable">
		<tr>
			<td colspan="4">
				<label class="middleText" for="textYIPersonInstitutionResposibleForTheDescription"><s:property value="getText('label.ai.yourinstitution.personinstitutionresposibleforthedescription')"/>:</label>
				<input type="text" id="textYIPersonInstitutionResposibleForTheDescription" class="middleText"/>
			</td>
		</tr>

		<tr>
			<td>
				<label for="textYIInstitutionCountryCode"><s:property value="getText('label.ai.tabs.commons.countryCode')"/></label>
				<span class="required">*</span>:
			</td>
			<td>
				<input type="text" id="textYIInstitutionCountryCode" />
				<s:fielderror fieldName="yourInstitutionCountryCode"/>
			</td>
			<td colspan="2">
			</td>
		</tr>

		<tr>
			<td>
				<label for="textYIIdentifierOfTheInstitution"><s:property value="getText('label.ai.tabs.commons.identifierOfTheInstitution')"/></label>
				<span class="required">*</span>:
			</td>
			<td>
				<input type="text" id="textYIIdentifierOfTheInstitution" />
				<s:fielderror fieldName="yourInstitutionIdentifierOfTheInstitution"/>
			</td>
			<td>
				<label for="textYIIdUsedInAPE"><s:property value="getText('label.ai.tabs.commons.idUsedInAPE')"/>:</label>
			</td>
			<td>
				<input type="text" id="textYIIdUsedInAPE" disabled="disabled"/>
			</td>
		</tr>

		<tr>
			<td colspan="3">
			</td>
			<td>
				<input type="button" id="buttonAddFutherIds" value="<s:property value="getText('label.ai.yourinstitution.addFutherIds')" />" />
			</td>
		</tr>

		<tr>
			<td>
				<label for="textYINameOfTheInstitution"><s:property value="getText('label.ai.tabs.commons.nameOfTheInstitution')"/></label>
				<span class="required">*</span>:
			</td>
			<td>
				<input type="text" id="textYINameOfTheInstitution" />
				<s:fielderror fieldName="yourInstitutionNameOfTheInstitution"/>
			</td>
			<td>
				<label for="selectYINOTISelectLanguage"><s:property value="getText('label.ai.tabs.commons.selectLanguage')"/></label>
				<span class="required">*</span>:
			</td>
			<td>
				<s:select theme="simple" id="selectYINOTISelectLanguage" list="languageList"></s:select>
				<s:fielderror fieldName="yourInstitutionLanguageNameOfTheInstitution"/>
			</td>
		</tr>

		<tr>
			<td colspan="3">
			</td>
			<td>
				<input type="button" id="buttonAddRepositories" value="<s:property value="getText('label.ai.yourinstitution.addRepositories')" />" />
			</td>
		</tr>

		<tr>
			<td>
				<label for="textYIParallelNameOfTheInstitution"><s:property value="getText('label.ai.tabs.commons.parallelNameOfTheInstitution')"/>:</label>
			</td>
			<td>
				<input type="text" id="textYIParallelNameOfTheInstitution" />
			</td>
			<td>
				<label for="selectYIPNOTISelectLanguage"><s:property value="getText('label.ai.tabs.commons.selectLanguage')"/>:</label>
			</td>
			<td>
				<s:select theme="simple" id="selectYIPNOTISelectLanguage" list="languageList"></s:select>
			</td>
		</tr>
	
		<tr>
			<td id="visitorAdressLabel" colspan="4">
				<s:property value="getText('label.ai.tabs.commons.visitorAddress')" />
			</td>
		</tr>

		<tr>
			<td>
				<label for="textYIStreet"><s:property value="getText('label.ai.tabs.commons.street')"/></label>
				<span class="required">*</span>:
			</td>
			<td>
				<input type="text" id="textYIStreet" />
				<s:fielderror fieldName="yourInstitutionStreetOfTheInstitution"/>
			</td>
			<td>
				<label for="selectYISSelectLanguage"><s:property value="getText('label.ai.tabs.commons.selectLanguage')"/>:</label>
			</td>
			<td>
				<s:select theme="simple" id="selectYISSelectLanguage" list="languageList"></s:select>
			</td>
		</tr>

		<tr>
			<td>
				<label for="textYICity"><s:property value="getText('label.ai.tabs.commons.cityTownWithPostalcode')"/></label>
				<span class="required">*</span>:
			</td>
			<td>
				<input type="text" id="textYICity" />
				<s:fielderror fieldName="yourInstitutionCityOfTheInstitution"/>
			</td>
			<td>
				<label for="selectYICSelectLanguage"><s:property value="getText('label.ai.tabs.commons.selectLanguage')"/>:</label>
			</td>
			<td>
				<s:select theme="simple" id="selectYICSelectLanguage" list="languageList"></s:select>
			</td>
		</tr>

		<tr>
			<td>
				<label for="textYICountry"><s:property value="getText('label.ai.tabs.commons.country')"/></label>
				<span class="required">*</span>:
			</td>
			<td>
				<input type="text" id="textYICountry" />
				<s:fielderror fieldName="yourInstitutionCountryOfTheInstitution"/>
			</td>
			<td>
				<label for="selectYICoSelectLanguage"><s:property value="getText('label.ai.tabs.commons.selectLanguage')"/>:</label>
			</td>
			<td>
				<s:select theme="simple" id="selectYICoSelectLanguage" list="languageList"></s:select>
			</td>
		</tr>

		<tr>
			<td id="coordinatesLabel" colspan="4">
				<s:property value="getText('label.ai.tabs.commons.coordinates')" />
			</td>
		</tr>

		<tr>
			<td>
				<label for="textYILatitude"><s:property value="getText('label.ai.tabs.commons.latitude')"/></label>
			</td>
			<td>
				<input type="text" id="textYILatitude" />
			</td>
			<td>
				<label for="selectYILongitude"><s:property value="getText('label.ai.tabs.commons.longitude')"/></label>
			</td>
			<td>
				<input type="text" id="selectYILongitude" />
			</td>
		</tr>

		<tr>
			<td colspan="2">
				<input type="button" id="buttonAddPostalAddressIfDifferent" value="<s:property value="getText('label.ai.yourinstitution.addPostalAddressIfDifferent')"/>" />
			</td>
			<td colspan="2">
			</td>
		</tr>

		<tr>
			<td>
				<label for="selectYIContinent" ><s:property value="getText('label.ai.tabs.commons.continent')"/>:</label>
			</td>
			<td>
				<s:select theme="simple" id="selectYIContinent" list="languageList"></s:select>
			</td>
		</tr>

		<tr>
			<td>
				<label for="textYITelephone" ><s:property value="getText('label.ai.tabs.commons.telephone')"/>:</label>
			</td>
			<td>
				<input type="text" id="textYITelephone" />
			</td>
		</tr>

		<tr>
			<td>
				<label for="textYIEmailAddress" ><s:property value="getText('label.ai.tabs.commons.emailAddress')"/>:</label>
			</td>
			<td>
				<input type="text" id="textYIEmailAddress" />
			</td>
			<td>
				<label for="textYIEmailLinkTitle" ><s:property value="getText('label.ai.tabs.commons.linkTitle')"/>:</label>
			</td>
			<td>
				<input type="text" id="textYIEmailLinkTitle" />
			</td>
		</tr>

		<tr>
			<td>
				<label for="textYIWebpage" ><s:property value="getText('label.ai.tabs.commons.webpage')"/></label>
				<span class="required">*</span>:
			</td>
			<td>
				<input type="text" id="textYIWebpage" />
				<s:fielderror fieldName="yourInstitutionWebPageOfTheInstitution"/>
			</td>
			<td>
				<label for="textYIWebpageLinkTitle" ><s:property value="getText('label.ai.tabs.commons.linkTitle')"/>:</label>
			</td>
			<td>
				<input type="text" id="textYIWebpageLinkTitle" />
			</td>
		</tr>

		<tr>
			<td>
				<label for="textYIOpeningTimes" ><s:property value="getText('label.ai.yourinstitution.openingTimes')"/></label>
				<span class="required">*</span>:
			</td>
			<td>
				<input type="text" id="textYIOpeningTimes" />
				<s:fielderror fieldName="yourInstitutionOpeningimes"/>
			</td>
			<td colspan="2">
			</td>
		</tr>

		<tr>
			<td>
				<input type="button" id="buttonAddClosingDates" value="<s:property value="getText('label.ai.yourinstitution.addClosingDates')"/>" />
			</td>
			<td colspan="3">
			</td>
		</tr>

		<tr>
			<td>
				<label for="selectAccessibleToThePublic" ><s:property value="getText('label.ai.yourinstitution.accessibleToThePublic')"/></label>
				<span class="required">*</span>:
			</td>
			<td>
				<s:label>TODO: Yes/No list</s:label>
				<%-- <s:select id="selectAccessibleToThePublic" list="yesnoList" />--%>
				<s:fielderror fieldName="yourInstitutionAccesibleToPublic"/>
			</td>
			<td colspan="2">
				<input type="button" id="buttonFutherAccessInformation" value="<s:property value="getText('label.ai.yourinstitution.addFurtherAccessInformation')"/>" />
			</td>
		</tr>

		<tr>
			<td>
				<label for="selectFacilitiesForDisabledPeopleAvailable" ><s:property value="getText('label.ai.yourinstitution.facilitiesForFisabledPeopleAvailable')"/></label>
				<span class="required">*</span>:
			</td>
			<td>
				<s:label>TODO: Yes/No list</s:label>
				<%-- <s:select id="selectFacilitiesForDisabledPeopleAvailable" list="yesnoList" />--%>
				<s:fielderror fieldName="yourInstitutionFacilitiesForDisabledPeople"/>
			</td>
			<td colspan="2">
				<input type="button" id="buttonAddFutherInformationOnExistingFacilities" value="<s:property value="getText('label.ai.yourinstitution.addFutherInformationOnExistingFacilities')"/>" />
			</td>
		</tr>

		<tr>
			<td colspan="2">
				<label for="textReferencetoyourinstitutionsholdingsguide" ><s:property value="getText('label.ai.yourinstitution.referenceToYourInstitutionsHoldingsGuide')"/>:</label>
			</td>
			<td colspan="2">
				<input type="text" id="textReferencetoyourinstitutionsholdingsguide" />
			</td>
		</tr>

		<tr>
			<td>
				<label for="textYIHoldingsGuideLinkTitle" ><s:property value="getText('label.ai.tabs.commons.linkTitle')"/>:</label>
			</td>
			<td>
				<input type="text" id="textYIHoldingsGuideLinkTitle" />
			</td>
			<td colspan="2">
			</td>
		</tr>

		<tr>
			<td id="tdButtonsYourInstitutionTab" colspan="4">
				<input type="button" id="buttonYourInstitutionTabNext" value="<s:property value='getText("label.ai.tabs.commons.button.nextTab")' />" class="rightButton" />
				<input type="button" id="buttonYourInstitutionTabExit" value="<s:property value='getText("label.ai.tabs.commons.button.exit")' />" class="rightButton" />
				<input type="button" id="buttonYourInstitutionTabSave" value="<s:property value='getText("label.ai.tabs.commons.button.save")' />" class="rightButton" />
			</td>
		</tr>
	</table>
</div>