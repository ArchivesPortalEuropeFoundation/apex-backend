<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<div id="yourInstitutionTabContent">
	<table id="yourInstitutionTable_1">	
		<tr>
			<td id="yourInstitutionLabel" colspan="4">
				<s:property value="getText('label.ai.tab.yourinstitution.title')" />
			</td>
		</tr>

		<tr>
			<td colspan="2">
				<label  for="textYIPersonInstitutionResposibleForTheDescription"><s:property value="getText('label.ai.yourinstitution.personinstitutionresposibleforthedescription')"/>:</label>
			</td>
			<td colspan="2" class="labelLeft">
				<input type="text" id="textYIPersonInstitutionResposibleForTheDescription" class="middleText" value="${loader.agent}" onchange="personResponsibleForDescriptionChanged();" />
			</td>
		</tr>

		<tr>
			<td>
				<label for="textYIInstitutionCountryCode"><s:property value="getText('label.ai.tabs.commons.countryCode')"/><span class="required">*</span>:</label>
			</td>
			<td>
				<input type="text" id="textYIInstitutionCountryCode" value="${loader.countryCode}" disabled="disabled"/>
			</td>
			<td colspan="2">
			</td>
		</tr>

		<tr>
			<td>
				<label for="textYIIdentifierOfTheInstitution"><s:property value="getText('label.ai.tabs.commons.identifierOfTheInstitution')"/><span class="required">*</span>:</label>
			</td>
			<td>
				<input type="text" id="textYIIdentifierOfTheInstitution" value="${loader.otherRepositorId}" onchange="idOfInstitutionChanged();" />
			</td>
			<td class="labelLeft">
				<label for="textYIIdUsedInAPE"><s:property value="getText('label.ai.tabs.commons.idUsedInAPE')"/>:</label>
			</td>
			<td>
				<input type="text" id="textYIIdUsedInAPE" value="${loader.recordId}" disabled="disabled" />
			</td>
		</tr>

		<tr>
			<td colspan="3">
			</td>
			<td>
				<input type="button" id="buttonAddFutherIds" value="<s:property value="getText('label.ai.yourinstitution.addFutherIds')" />" onclick="addFurtherIds('<s:property value="getText('label.ai.yourinstitution.futherId')" />','<s:property value="getText('label.ai.tabs.commons.pleaseFillData')" />');" />
			</td>
		</tr>
		<tr>
			<td>
				<label for="textYINameOfTheInstitution"><s:property value="getText('label.ai.tabs.commons.nameOfTheInstitution')"/><span class="required">*</span>:</label>
			</td>
			<td>
				<input type="text" id="textYINameOfTheInstitution" value="${loader.autform}" onchange="nameOfInstitutionChanged();" />
			</td>
			<td class="labelLeft">
				<label for="selectYINOTISelectLanguage"><s:property value="getText('label.ai.tabs.commons.selectLanguage')"/><span class="required">*</span>:</label>
			</td>
			<td>
				<s:select theme="simple" id="selectYINOTISelectLanguage" list="languageList" onchange="nameOfInstitutionLanguageChanged();" value="#loader.autformLang" ></s:select>
			</td>
		</tr>

		<tr>
			<td colspan="3">
			</td>
			<td>
				<input type="button" id="buttonAddRepositories" value="<s:property value="getText('label.ai.yourinstitution.addRepositories')" />" onclick="addRepositories('<s:property value="getText('label.ai.yourinstitution.institution')" />','<s:property value="getText('label.ai.yourinstitution.repository')" />','<s:property value="getText('label.ai.contact.nameOfRepository')" />','<s:property value="getText('label.ai.contact.roleOfRepository')" />','<s:property value="getText('label.ai.tabs.commons.option.role.headquarters')" />','<s:property value="getText('label.ai.tabs.commons.option.role.branch')" />','<s:property value="getText('label.ai.tabs.commons.option.role.interimArchive')" />');" />
			</td>
		</tr>
		<tr>
			<td>
				<label for="textYIParallelNameOfTheInstitution"><s:property value="getText('label.ai.tabs.commons.parallelNameOfTheInstitution')"/>:</label>
			</td>
			<td>
				<input type="text" id="textYIParallelNameOfTheInstitution" value="${loader.parform}" onchange="parallelNameOfInstitutionChanged();" />
			</td>
			<td class="labelLeft">
				<label for="selectYIPNOTISelectLanguage"><s:property value="getText('label.ai.tabs.commons.selectLanguage')"/>:</label>
			</td>
			<td class="labelLeftselect">
				<s:select theme="simple" id="selectYIPNOTISelectLanguage" list="languageList" onchange="parallelNameOfInstitutionLanguageChanged();" value="parformLang"></s:select>
			</td>
		</tr>
	</table>

	<table id="yiTableVisitorsAddress_1">
		<tr>
			<td id="visitorAdressLabel" colspan="4">
				<s:property value="getText('label.ai.tabs.commons.visitorAddress')" />
			</td>
			
		</tr>
		<tr>
			<td>
				<label for="textYIStreet"><s:property value="getText('label.ai.tabs.commons.street')"/><span class="required">*</span>:</label>
			</td>
			<td>
				<input type="text" id="textYIStreet" onchange="streetOfInstitutionChanged();" value="${loader.street}" />
			</td>
			<td class="labelLeft">
				<label for="selectYIVASelectLanguage"><s:property value="getText('label.ai.tabs.commons.selectLanguage')"/><span class="required">*</span>:</label>
			</td>
			<td>
				<s:select theme="simple" id="selectYIVASelectLanguage" list="languageList" onchange="streetOfInstitutionLanguageChanged();" value="streetLang"></s:select>
			</td>
		</tr>

		<tr>
			<td>
				<label for="textYICity"><s:property value="getText('label.ai.tabs.commons.cityTownWithPostalcode')"/><span class="required">*</span>:</label>
			</td>
			<td>
				<input type="text" id="textYICity" onchange="cityOfInstitutionChanged();" value="${loader.municipalityPostalcode}" />
			</td>
			<td colspan="2">
			</td>
		</tr>

		<tr>
			<td>
				<label for="textYICountry"><s:property value="getText('label.ai.tabs.commons.country')"/><span class="required">*</span>:</label>
			</td>
			<td>
				<input type="text" id="textYICountry" onchange="countryOfInstitutionChanged();" value="${loader.country}" />
			</td>
			<td colspan="2">
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
				<input type="text" id="textYILatitude" onchange="latitudeOfInstitutionChanged();" value="${loader.latitude}" />
			</td>
			<td class="labelLeft">
				<label for="textYILongitude"><s:property value="getText('label.ai.tabs.commons.longitude')"/></label>
			</td>
			<td>
				<input type="text" id="textYILongitude" onchange="longitudeOfInstitutionChanged();" value="${loader.longitude}" />
			</td>
		</tr>
	</table>

	<table id="yiTableOthers">
		<tr>
			<td colspan="2">
				<input type="button" id="buttonAddVisitorsAddressTranslation" value="<s:property value="getText('label.ai.tabs.commons.addFurtherVisitorsAddress')"/>" onclick="yiAddVisitorsAddressTranslation('<s:property value="getText('label.ai.tabs.commons.pleaseFillData')" />');" />
			</td>
			<td colspan="2">
			</td>
		</tr>

		<tr>
			<td colspan="4">
				<input type="button" id="buttonAddPostalAddressIfDifferent" value="<s:property value="getText('label.ai.tabs.commons.addPostalAddressIfDifferent')"/>" onclick="yiAddPostalAddressIfDifferent('<s:property value="getText('label.ai.yourinstitution.postalAddress')" />', '<s:property value="getText('label.ai.tabs.commons.selectLanguage')" />', '<s:property value="getText('label.ai.tabs.commons.street')" />', '<s:property value="getText('label.ai.tabs.commons.cityTownWithPostalcode')" />')" />
			</td>
		</tr>

		<tr id="yiPostalAddressTranslation" style="display:none;">
			<td colspan="2">
				<input type="button" id="buttonAddPostalAddressTranslation" value="<s:property value="getText('label.ai.yourinstitution.addPostalAddressTranslation')"/>" onclick="yiAddPostalAddressTranslation('<s:property value="getText('label.ai.tabs.commons.pleaseFillData')" />');" />
			</td>
			<td colspan="2">
			</td>
		</tr>

		<tr>
			<td>
				<label for="selectYIContinent" ><s:property value="getText('label.ai.tabs.commons.continent')"/>:</label>
			</td>
			<td>
				<s:select theme="simple" id="selectYIContinent" list="continentOfTheInstitutionList" onchange="continentOfInstitutionChanged();" value="geogarea"></s:select>
			</td>
		</tr>

		<tr>
			<td>
				<label for="textYITelephone" ><s:property value="getText('label.ai.tabs.commons.telephone')"/>:</label>
			</td>
			<td>
				<input type="text" id="textYITelephone" onchange="telephoneOfInstitutionChanged();" value="${loader.telephone}" />
			</td>
		</tr>

		<tr>
			<td>
				<label for="textYIEmailAddress" ><s:property value="getText('label.ai.tabs.commons.emailAddress')"/>:</label>
			</td>
			<td>
				<input type="text" id="textYIEmailAddress" onchange="emailOfInstitutionChanged();" value="${loader.email}" />
			</td>
			<td class="labelLeft">
				<label for="textYIEmailLinkTitle" ><s:property value="getText('label.ai.tabs.commons.linkTitle')"/>:</label>
			</td>
			<td>
				<input type="text" id="textYIEmailLinkTitle" onchange="emailOfInstitutionLinkChanged();" value="${loader.emailTitle}" />
			</td>
		</tr>

		<tr>
			<td>
				<label for="textYIWebpage" ><s:property value="getText('label.ai.tabs.commons.webpage')"/>:</label>
			</td>
			<td>
				<input type="text" id="textYIWebpage" onchange="webOfInstitutionChanged();" value="${loader.webpage}" />
			</td>
			<td class="labelLeft">
				<label for="textYIWebpageLinkTitle" ><s:property value="getText('label.ai.tabs.commons.linkTitle')"/>:</label>
			</td>
			<td>
				<input type="text" id="textYIWebpageLinkTitle" onchange="webOfInstitutionLinkChanged();" value="${loader.webpageTitle}" />
			</td>
		</tr>

		<tr>
			<td>
				<label for="textYIOpeningTimes" ><s:property value="getText('label.ai.yourinstitution.openingTimes')"/><span class="required">*</span>:</label>
			</td>
			<td>
				<input type="text" id="textYIOpeningTimes" onchange="openingHoursOfInstitutionChanged();" value="${loader.opening}" />
			</td>
			<td colspan="2">
			</td>
		</tr>

		<tr>
			<td>
				<input type="button" id="buttonAddClosingDates" value="<s:property value="getText('label.ai.tabs.commons.closingDates')"/>" onclick="yiAddClosingDates();" />
			</td>
			<td colspan="3">
			</td>
		</tr>

		<tr id="fieldClosingDates" style="display:none;">
			<td><label for="yourInstitutionClosingDates"><s:property value="getText('label.ai.yourinstitution.closingDates')"/>:</label></td>
			<td><input type="text" id="yourInstitutionClosingDates" onchange="closingHoursOfInstitutionChanged();" value="${loader.closing}" /></td>
			<td colspan="2">
			</td>
		</tr>

		<tr>
			<td>
				<label for="selectAccessibleToThePublic" ><s:property value="getText('label.ai.yourinstitution.accessibleToThePublic')"/><span class="required">*</span>:</label>
			</td>
			<td>
				<s:select theme="simple" id="selectAccessibleToThePublic" list="yesNoList" onchange="accessibleToThePublicChanged();" value="accessQuestion"></s:select>
			</td>
			<td colspan="2" class="labelLeft">
				<input type="button" id="buttonFutherAccessInformation" value="<s:property value="getText('label.ai.yourinstitution.addFurtherAccessInformation')"/>" onclick="yiFutherAccessInformation();" />
			</td>
		</tr>

		<tr>
			<td>
				<label for="selectFacilitiesForDisabledPeopleAvailable" ><s:property value="getText('label.ai.yourinstitution.facilitiesForFisabledPeopleAvailable')"/><span class="required">*</span>:</label>
			</td>
			<td>
				<s:select theme="simple" id="selectFacilitiesForDisabledPeopleAvailable" list="yesNoList" onchange="facilitiesForDisabledPeopleAvailableChanged();" value="accessibilityQuestion"></s:select>
			</td>
			<td colspan="2" class="labelLeft">
				<input type="button" id="buttonAddFutherInformationOnExistingFacilities" value="<s:property value="getText('label.ai.yourinstitution.addFutherInformationOnExistingFacilities')"/>" onclick="yiAddFutherInformationOnExistingFacilities();" />
			</td>
		</tr>

		<tr>
			<td>
				<label for="textReferencetoyourinstitutionsholdingsguide" ><s:property value="getText('label.ai.yourinstitution.referenceToYourInstitutionsHoldingsGuide')"/>:</label>
			</td>
			<td>
				<input type="text" id="textReferencetoyourinstitutionsholdingsguide" onchange="linkToYourHolndingsGuideChanged();" value="${loader.resourceRelationHref}" />
			</td>
			<td class="labelLeft">
				<label for="textYIHoldingsGuideLinkTitle" ><s:property value="getText('label.ai.tabs.commons.linkTitle')"/>:</label>
			</td>
			<td>
				<input type="text" id="textYIHoldingsGuideLinkTitle" onchange="linkToYourHolndingsGuideTitleChanged();" value="${loader.resourceRelationrelationEntry}" />
			</td>
		</tr>

		<tr>
			<td id="tdButtonsYourInstitutionTab" colspan="4">
				<input type="button" id="buttonYourInstitutionTabNext" value="<s:property value='getText("label.ai.tabs.commons.button.nextTab")' />" class="rightButton" onclick="checkAndShowNextTab($(this).parent().parent().parent().parent(), '<s:property value="getText('label.ai.tabs.commons.fieldRequired')" />', '<s:property value="getText('label.ai.tabs.commons.pleaseFillMandatoryFields')" />');" />
				<input type="button" id="buttonYourInstitutionTabCheck" value="<s:property value='getText("label.ai.tabs.commons.button.check")' />" class="rightButton" onclick="clickYourInstitutionAction('<s:property value="getText('label.ai.tabs.commons.fieldRequired')" />');"/>
			</td>
		</tr>
	</table>
</div>