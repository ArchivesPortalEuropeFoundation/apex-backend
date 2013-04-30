<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<div id="yourInstitutionTabContent">
	<table id="yourInstitutionTable_1">	
		<tr>
			<td id="yourInstitutionLabel" colspan="4">
				<s:property value="getText('label.ai.tab.yourinstitution.title')" />
			</td>
			<td class="borderContactVisitorAddress"></td>
		</tr>

		<tr>
			<td colspan="2">
				<label  for="textYIPersonInstitutionResposibleForTheDescription"><s:property value="getText('label.ai.yourinstitution.personinstitutionresposibleforthedescription')"/>:</label>
			</td>
			<td colspan="2">
				<input type="text" id="textYIPersonInstitutionResposibleForTheDescription" class="middleText"/>
			</td>
		</tr>

		<tr>
			<td>
				<label for="textYIInstitutionCountryCode"><s:property value="getText('label.ai.tabs.commons.countryCode')"/><span class="required">*</span>:</label>
			</td>
			<td>
				<input type="text" id="textYIInstitutionCountryCode" disabled="disabled"/>
			</td>
			<td colspan="2">
			</td>
		</tr>

		<tr>
			<td>
				<label for="textYIIdentifierOfTheInstitution"><s:property value="getText('label.ai.tabs.commons.identifierOfTheInstitution')"/><span class="required">*</span>:</label>
			</td>
			<td>
				<input type="text" id="textYIIdentifierOfTheInstitution" />
			</td>
			<td class="labelLeft">
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
				<input type="button" id="buttonAddFutherIds" value="<s:property value="getText('label.ai.yourinstitution.addFutherIds')" />" onclick="addFurtherIds('<s:property value="getText('label.ai.yourinstitution.futherId')" />');" />
			</td>
		</tr>
		<tr>
			<td>
				<label for="textYINameOfTheInstitution"><s:property value="getText('label.ai.tabs.commons.nameOfTheInstitution')"/><span class="required">*</span>:</label>
			</td>
			<td>
				<input type="text" id="textYINameOfTheInstitution" />
			</td>
			<td class="labelLeft">
				<label for="selectYINOTISelectLanguage"><s:property value="getText('label.ai.tabs.commons.selectLanguage')"/><span class="required">*</span>:</label>
			</td>
			<td>
				<s:select theme="simple" id="selectYINOTISelectLanguage" list="languageList"></s:select>
			</td>
		</tr>

		<tr>
			<td colspan="3">
			</td>
			<td>
				<input type="button" id="buttonAddRepositories" value="<s:property value="getText('label.ai.yourinstitution.addRepositories')" />" onclick="addRepositories('<s:property value="getText('label.ai.yourinstitution.institution')" />','<s:property value="getText('label.ai.yourinstitution.repository')" />');" />
			</td>
		</tr>
		<tr>
			<td>
				<label for="textYIParallelNameOfTheInstitution"><s:property value="getText('label.ai.tabs.commons.parallelNameOfTheInstitution')"/>:</label>
			</td>
			<td>
				<input type="text" id="textYIParallelNameOfTheInstitution" />
			</td>
			<td class="labelLeft">
				<label for="selectYIPNOTISelectLanguage"><s:property value="getText('label.ai.tabs.commons.selectLanguage')"/>:</label>
			</td>
			<td class="labelLeftselect">
				<s:select theme="simple" id="selectYIPNOTISelectLanguage" list="languageList"></s:select>
			</td>
		</tr>
	</table>

	<table id="yiTableVisitorsAddress_1">
		<tr>
			<td id="visitorAdressLabel" colspan="4">
				<s:property value="getText('label.ai.tabs.commons.visitorAddress')" />
			</td>
			<td class="borderYourInstitution"></td>
		</tr>
		<tr>
			<td>
				<label for="textYIStreet"><s:property value="getText('label.ai.tabs.commons.street')"/><span class="required">*</span>:</label>
			</td>
			<td>
				<input type="text" id="textYIStreet" />
			</td>
			<td>
				<label for="selectYIVASelectLanguage"><s:property value="getText('label.ai.tabs.commons.selectLanguage')"/><span class="required">*</span>:</label>
			</td>
			<td>
				<s:select theme="simple" id="selectYIVASelectLanguage" list="languageList"></s:select>
			</td>
		</tr>

		<tr>
			<td>
				<label for="textYICity"><s:property value="getText('label.ai.tabs.commons.cityTownWithPostalcode')"/><span class="required">*</span>:</label>
			</td>
			<td>
				<input type="text" id="textYICity" />
			</td>
			<td colspan="2">
			</td>
		</tr>

		<tr>
			<td>
				<label for="textYICountry"><s:property value="getText('label.ai.tabs.commons.country')"/><span class="required">*</span>:</label>
			</td>
			<td>
				<input type="text" id="textYICountry" />
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
				<input type="text" id="textYILatitude" />
			</td>
			<td class="labelLeft">
				<label for="textYILongitude"><s:property value="getText('label.ai.tabs.commons.longitude')"/></label>
			</td>
			<td>
				<input type="text" id="textYILongitude" />
			</td>
		</tr>
	</table>

	<table id="yiTableOthers">
		<tr>
			<td colspan="2">
				<input type="button" id="buttonAddVisitorsAddressTranslation" value="<s:property value="getText('label.ai.yourinstitution.addFurtherVisitorsAddress')"/>" onclick="yiAddVisitorsAddressTranslation();" />
			</td>
			<td colspan="2">
			</td>
		</tr>

		<tr>
			<td colspan="4">
				<input type="button" id="buttonAddPostalAddressIfDifferent" value="<s:property value="getText('label.ai.yourinstitution.addPostalAddressIfDifferent')"/>" />
				<script type="text/javascript">
					$("#buttonAddPostalAddressIfDifferent").click(function(){
						$("#buttonAddPostalAddressIfDifferent").hide();

						var property1 = "<s:property value="getText('label.ai.yourinstitution.postalAddress')" />";
						var property2 = "<s:property value="getText('label.ai.tabs.commons.selectLanguage')"/>";
						var property3 = "<s:property value="getText('label.ai.tabs.commons.street')"/>";
						var property4 = "<s:property value="getText('label.ai.tabs.commons.cityTownWithPostalcode')"/>";
						var select = '<select id="selectYIPASelectLanguage">'+$("#selectYIPNOTISelectLanguage").html()+'</select>';
						$("table#yiTableOthers").before('<table id="yiTablePostalAddress_1">'+
							'<tr id="yiPostalAddressLabel">'+
								'<td id="postalAddressLabel" colspan="4">'+property1+
								'</td>'+
							'</tr>'+
							'<tr id="yiPostalAddressStreet">'+
								'<td>'+
									'<label for="textYIPAStreet">'+property3+'<span class="required">*</span>:</label>'+
								'</td>'+
								'<td>'+
									'<input type="text" id="textYIPAStreet" />'+
								'</td>'+
								'<td id="yiPostalAddressLanguage">'+
									'<label for="selectYIPASelectLanguage">'+property2+'<span class="required">*</span>:</label>'+
								'</td>'+
								'<td>'+select+
								'</td>'+
							'</tr>'+
							'<tr id="yiPostalAddressCity">'+
								'<td>'+
									'<label for="textYIPACity">'+property4+'<span class="required">*</span>:</label>'+
								'</td>'+
								'<td>'+
									'<input type="text" id="textYIPACity" />'+
								'</td>'+
							'</tr></table>');

						$("table#yiTableOthers tr#yiPostalAddressTranslation").show();
					});
				</script>
			</td>
		</tr>

		<tr id="yiPostalAddressTranslation" style="display:none;">
			<td colspan="2">
				<input type="button" id="buttonAddPostalAddressTranslation" value="<s:property value="getText('label.ai.yourinstitution.addPostalAddressTranslation')"/>" onclick="yiAddPostalAddressTranslation();" />
			</td>
			<td colspan="2">
			</td>
		</tr>

		<tr>
			<td>
				<label for="selectYIContinent" ><s:property value="getText('label.ai.tabs.commons.continent')"/>:</label>
			</td>
			<td>
				<s:select theme="simple" id="selectYIContinent" list="continentOfTheInstitutionList"></s:select>
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
			<td class="labelLeft">
				<label for="textYIEmailLinkTitle" ><s:property value="getText('label.ai.tabs.commons.linkTitle')"/>:</label>
			</td>
			<td>
				<input type="text" id="textYIEmailLinkTitle" />
			</td>
		</tr>

		<tr>
			<td>
				<label for="textYIWebpage" ><s:property value="getText('label.ai.tabs.commons.webpage')"/>:</label>
			</td>
			<td>
				<input type="text" id="textYIWebpage" />
			</td>
			<td class="labelLeft">
				<label for="textYIWebpageLinkTitle" ><s:property value="getText('label.ai.tabs.commons.linkTitle')"/>:</label>
			</td>
			<td>
				<input type="text" id="textYIWebpageLinkTitle" />
			</td>
		</tr>

		<tr>
			<td>
				<label for="textYIOpeningTimes" ><s:property value="getText('label.ai.yourinstitution.openingTimes')"/><span class="required">*</span>:</label>
			</td>
			<td>
				<input type="text" id="textYIOpeningTimes" />
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
			<td><input type="text" id="yourInstitutionClosingDates" /></td>
		</tr>
		<tr>
			<td>
				<label for="selectAccessibleToThePublic" ><s:property value="getText('label.ai.yourinstitution.accessibleToThePublic')"/><span class="required">*</span>:</label>
			</td>
			<td>
				<s:select theme="simple" id="selectAccessibleToThePublic" list="yesNoList"></s:select>
			</td>
			<td colspan="2">
				<input type="button" id="buttonFutherAccessInformation" value="<s:property value="getText('label.ai.yourinstitution.addFurtherAccessInformation')"/>" onclick="yiFutherAccessInformation();" />
			</td>
		</tr>

		<tr>
			<td>
				<label for="selectFacilitiesForDisabledPeopleAvailable" ><s:property value="getText('label.ai.yourinstitution.facilitiesForFisabledPeopleAvailable')"/><span class="required">*</span>:</label>
			</td>
			<td>
				<s:select theme="simple" id="selectFacilitiesForDisabledPeopleAvailable" list="yesNoList"></s:select>
			</td>
			<td colspan="2">
				<input type="button" id="buttonAddFutherInformationOnExistingFacilities" value="<s:property value="getText('label.ai.yourinstitution.addFutherInformationOnExistingFacilities')"/>" onclick="yiAddFutherInformationOnExistingFacilities();" />
			</td>
		</tr>

		<tr>
			<td>
				<label for="textReferencetoyourinstitutionsholdingsguide" ><s:property value="getText('label.ai.yourinstitution.referenceToYourInstitutionsHoldingsGuide')"/>:</label>
			</td>
			<td>
				<input type="text" id="textReferencetoyourinstitutionsholdingsguide" />
			</td>
			<td class="labelLeft">
				<label for="textYIHoldingsGuideLinkTitle" ><s:property value="getText('label.ai.tabs.commons.linkTitle')"/>:</label>
			</td>
			<td>
				<input type="text" id="textYIHoldingsGuideLinkTitle" />
			</td>
		</tr>

		<tr>
			<td>
				<label for="textYINumberOFWorkingPlaces" ><s:property value="getText('label.ai.yourinstitution.working.places')"/>:</label>
			</td>
			<td>
				<input type="text" id="textYINumberOFWorkingPlaces" />
			</td>
			<td colspan="2">
			</td>
		</tr>

		<tr>
			<td id="tdButtonsYourInstitutionTab" colspan="4">
				<input type="button" id="buttonYourInstitutionTabNext" value="<s:property value='getText("label.ai.tabs.commons.button.nextTab")' />" class="rightButton" />
				<input type="button" id="buttonYourInstitutionTabCheck" value="<s:property value='getText("label.ai.tabs.commons.button.check")' />" class="rightButton" />
				<script type="text/javascript">
					//current tab
					$("table#yiTableOthers input#buttonYourInstitutionTabCheck").click(clickYourInstitutionAction);
				</script>
			</td>
		</tr>
	</table>
</div>