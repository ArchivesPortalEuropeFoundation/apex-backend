<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<div id="yourInstitutionTabContent">
	<table id="yourInstitutionTable">	
		<tr>
			<td id="yourInstitutionLabel" colspan="4">
				<s:property value="getText('label.ai.tab.yourinstitution.title')" />
			</td>
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
	</table>
	<table id="yourInstitutionTabContent_1">
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

		<tr><td colspan="4"><table id="yiTableVisitosAddress_1">
			<tr>
				<td id="visitorAdressLabel" colspan="4">
					<s:property value="getText('label.ai.tabs.commons.visitorAddress')" />
				</td>
			</tr>

			<tr>
				<td>
					<label for="selectYIVASelectLanguage"><s:property value="getText('label.ai.tabs.commons.selectLanguage')"/><span class="required">*</span>:</label>
				</td>
				<td>
					<s:select theme="simple" id="selectYIVASelectLanguage" list="languageList"></s:select>
				</td>
				<td colspan="2">
				</td>
			</tr>

			<tr>
				<td>
					<label for="textYIStreet"><s:property value="getText('label.ai.tabs.commons.street')"/><span class="required">*</span>:</label>
				</td>
				<td>
					<input type="text" id="textYIStreet" />
				</td>
				<td colspan="2">
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
		</table></td></tr>

		<tr><td colspan="4"><table id="yiTablePostalAddress_1">
			<tr id="yiPostalAddressLabel" style="display:none;">
				<td id="postalAddressLabel" colspan="4">
					<s:property value="getText('label.ai.yourinstitution.postalAddress')" />
				</td>
			</tr>
	
			<tr id="yiPostalAddressLanguage" style="display:none;">
				<td>
					<label for="selectYIPASelectLanguage"><s:property value="getText('label.ai.tabs.commons.selectLanguage')"/><span class="required">*</span>:</label>
				</td>
				<td>
					<s:select theme="simple" id="selectYIPASelectLanguage" list="languageList"></s:select>
				</td>
				<td colspan="2">
				</td>
			</tr>
	
			<tr id="yiPostalAddressStreet" style="display:none;">
				<td>
					<label for="textYIPAStreet"><s:property value="getText('label.ai.tabs.commons.street')"/><span class="required">*</span>:</label>
				</td>
				<td>
					<input type="text" id="textYIPAStreet" />
				</td>
				<td colspan="2">
				</td>
			</tr>
	
			<tr id="yiPostalAddressCity" style="display:none;">
				<td>
					<label for="textYIPACity"><s:property value="getText('label.ai.tabs.commons.cityTownWithPostalcode')"/><span class="required">*</span>:</label>
				</td>
				<td>
					<input type="text" id="textYIPACity" />
				</td>
				<td colspan="2">
				</td>
			</tr>
		</table></td></tr>

		<tr>
			<td colspan="2">
				<input type="button" id="buttonAddVisitorsAddressTranslation" value="<s:property value="getText('label.ai.yourinstitution.addFurtherVisitorsAddress')"/>" />
				<script type="text/javascript">
					$("#buttonAddVisitorsAddressTranslation").click(function(){
						var counter = $("table[id^='yiTableVisitosAddress_']").length;
						var clone = $("table[id^='yiTableVisitosAddress_"+counter+"']").clone();
						clone = "<table id='"+("yiTableVisitosAddress_"+(counter+1))+"'>"+clone.html()+"</table>";
						$("table[id^='yiTableVisitosAddress_"+counter+"']").after(clone);
						// Reset parametters.
						$("table#yiTableVisitosAddress_"+(counter+1)+" input[type='text']").each(function(){
							$(this).val(""); // Clean all input_text.
						});
					});
				</script>
			</td>
			<td colspan="2">
			</td>
		</tr>

		<tr>
			<td colspan="2">
				<input type="button" id="buttonAddPostalAddressIfDifferent" value="<s:property value="getText('label.ai.yourinstitution.addPostalAddressIfDifferent')"/>" />
			</td>
			<td colspan="2">
			</td>
		</tr>

		<tr id="yiPostalAddressTranslation" style="display:none;">
			<td colspan="2">
				<input type="button" id="buttonAddPostalAddressTranslation" value="<s:property value="getText('label.ai.yourinstitution.addPostalAddressTranslation')"/>" />
				<script type="text/javascript">
					$("#buttonAddPostalAddressTranslation").click(function(){
						var counter = $("table[id^='yiTablePostalAddress_']").length;
						var clone = $("table[id^='yiTablePostalAddress_"+counter+"']").clone();
						clone = "<table id='"+("yiTablePostalAddress_"+(counter+1))+"'>"+clone.html()+"</table>";
						$("table[id^='yiTablePostalAddress_"+counter+"']").after(clone);
						// Reset parametters.
						$("table#yiTablePostalAddress_"+(counter+1)+" input[type='text']").each(function(){
							$(this).val(""); // Clean all input_text.
						});
					});
				</script>
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
				<input type="button" id="buttonAddClosingDates" value="<s:property value="getText('label.ai.tabs.commons.closingDates')"/>" />
			</td>
			<td colspan="3">
			</td>
		</tr>
		<tr id="fieldClosingDates" style="display:none;">
			<td><label for="yourInstitutionClosingDates"><s:property value="getText('label.ai.yourinstitution.closingDates')"/></label></td>
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
				<input type="button" id="buttonFutherAccessInformation" value="<s:property value="getText('label.ai.yourinstitution.addFurtherAccessInformation')"/>" />
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
				<input type="button" id="buttonAddFutherInformationOnExistingFacilities" value="<s:property value="getText('label.ai.yourinstitution.addFutherInformationOnExistingFacilities')"/>" />
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
				<input type="button" id="buttonYourInstitutionTabSave" value="<s:property value='getText("label.ai.tabs.commons.button.save")' />" class="rightButton" />
			</td>
		</tr>
	</table>
</div>