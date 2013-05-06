<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<div id="contactTabContent">
	<table id="contactTable">
		<tr id="trVisitorsAddressLabel"><td colspan="4"><table id="contactTableVisitorsAddress_1">
			<tr>
				<td id="visitorAdressLabel" colspan="4">
					<s:property value="getText('label.ai.tabs.commons.visitorAddress')" />
				</td>
				
			</tr>

			<tr>
				<td id="tdStreetOfTheInstitution">
					<label for="textContactStreetOfTheInstitution"><s:property value="getText('label.ai.tabs.commons.street')" /><span class="required">*</span>:</label>
				</td>
				<td>
					<input type="text" id="textContactStreetOfTheInstitution" value="<s:property value="#streetOfTheInstitution" />" disabled="disabled" />
				</td>
				<td id="tdLanguageVisitorAddress" class="labelLeft">
					<label for="selectLanguageVisitorAddress" ><s:property value="getText('label.ai.tabs.commons.selectLanguage')" />:</label>
				</td>
				<td>
					<s:select theme="simple" id="selectLanguageVisitorAddress" list="languageList" disabled="true"></s:select>
				</td>
			</tr>

			<tr>
				<td id="tdCityOfTheInstitution">
					<label for="textContactCityOfTheInstitution"><s:property value="getText('label.ai.tabs.commons.cityTownWithPostalcode')" /><span class="required">*</span>:</label>
				</td>
				<td>
					<input type="text" id="textContactCityOfTheInstitution" value="<s:property value="#cityOfTheInstitution" />" disabled="disabled" />
				</td>
				<td colspan="2">
				</td>
			</tr>

			<tr>
				<td id="tdDistrictOfTheInstitution">
					<label for="textContactCityOfTheInstitution"><s:property value="getText('label.ai.contact.districtOfTheInstitution')" />:</label>
				</td>
				<td>
					<input type="text" id="textContactDistrictOfTheInstitution" value="<s:property value="#districtOfTheInstitution" />" />
				</td>
				<td colspan="2">
				</td>
			</tr>

			<tr>
				<td id="tdCountyOfTheInstitution">
					<label for="textContactCountyOfTheInstitution"><s:property value="getText('label.ai.contact.countyOfTheInstitution')" />:</label>
				</td>
				<td>
					<input type="text" id="textContactCountyOfTheInstitution" value="<s:property value="#countyOfTheInstitution" />" />
				</td>
				<td colspan="2">
				</td>
			</tr>

			<tr>
				<td id="tdRegionOfTheInstitution">
					<label for="textContactRegionOfTheInstitution"><s:property value="getText('label.ai.contact.regionOfTheInstitution')" />:</label>
				</td>
				<td>
					<input type="text" id="textContactRegionOfTheInstitution" value="<s:property value="#regionOfTheInstitution" />" />
				</td>
				<td colspan="2">
				</td>
			</tr>

			<tr>
				<td id="tdCountryOfTheInstitution">
					<label for="textContactCountryOfTheInstitution"><s:property value="getText('label.ai.tabs.commons.country')" /><span class="required">*</span>:</label>
				</td>
				<td>
					<input type="text" id="textContactCountryOfTheInstitution" value="<s:property value="#countryOfTheInstitution" />" disabled="disabled" />
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
					<label for="textContactLatitudeOfTheInstitution"><s:property value="getText('label.ai.tabs.commons.latitude')" /></label>
				</td>
				<td>
					<input type="text" id="textContactLatitudeOfTheInstitution" value="<s:property value="#latitudeOfTheInstitution" />" disabled="disabled" />
				</td>
				<td class="labelLeft">
					<label for="textContactLongitudeOfTheInstitution"><s:property value="getText('label.ai.tabs.commons.longitude')" /></label>
				</td>
				<td>
					<input type="text" id="textContactLongitudeOfTheInstitution" value="<s:property value="#longitudeOfTheInstitution" />" disabled="disabled" />
				</td>
			</tr>
		</table></td></tr>

		<tr>
			<td id="tdContactAddVisitorsAddressTranslation" colspan="2">
				<input type="button" id="buttonContactAddVisitorsAddressTranslation"  value="<s:property value='getText("label.ai.tabs.commons.addFurtherVisitorsAddress")' />" onclick="contactAddVisitorsAddressTranslation();" />
			</td>
		</tr>

		<tr id="trButtonContactAddPostalAddressIfDifferent">
			<td colspan="4">
				<input type="button" id="buttonContactAddPostalAddressIfDifferent" value="<s:property value="getText('label.ai.tabs.commons.addPostalAddressIfDifferent')"/>" onclick="contactAddPostalAddressIfDifferent('<s:property value="getText('label.ai.contact.postalAddress')" />','<s:property value="getText('label.ai.tabs.commons.selectLanguage')"/>','<s:property value="getText('label.ai.tabs.commons.street')"/>','<s:property value="getText('label.ai.tabs.commons.cityTownWithPostalcode')"/>');" />
			</td>
		</tr>

		<tr id="trButtonContacPostalAddressTranslation" style="display:none;">
			<td colspan="2">
				<input type="button" id="buttonContacPostalAddressTranslation" value="<s:property value="getText('label.ai.yourinstitution.addPostalAddressTranslation')"/>" onclick="contactAddPostalAddressTranslation();" />
			</td>
			<td colspan="2">
			</td>
		</tr>

		<tr>
			<td id="tdContinentOfTheInstitution">
				<label for="selectContinentOfTheInstitution"><s:property value="getText('label.ai.tabs.commons.continent')" /><span class="required">*</span>:</label>
				
			</td>
			<td>
				<s:select theme="simple" id="selectContinentOfTheInstitution" list="continentOfTheInstitutionList" disabled="true"></s:select>
			</td>
			<td colspan="2" ></td>
		</tr>

		<tr id="trTelephoneOfTheInstitution">
			<td id="tdTelephoneOfTheInstitution">
				<label for="textContactTelephoneOfTheInstitution"><s:property value="getText('label.ai.tabs.commons.telephone')" /></label>:
			</td>
			<td>
				<input type="text" id="textContactTelephoneOfTheInstitution" value="<s:property value="#telephoneOfTheInstitution" />" disabled="disabled" />
			</td>
			<td id="tdAddFurtherTelephoneOfTheInstitution" colspan="2">
				<input id="buttonAddFurtherTelephoneOfTheInstitution" type="button" value="<s:property value='getText("label.ai.contact.addFurtherTelephoneNumbers")' />" onclick="addFurtherTelephoneOfTheInstitution();"/>
				<script type="text/javascript">
					$("table#contactTable_"+$("table[id^='contactTable_']").length+" input#buttonAddFurtherTelephoneOfTheInstitution").click();
				</script>
			</td>
		</tr>
		<tr id="trFaxOfTheInstitution">
			<td id="tdFaxOfTheInstitution">
				<label for="textContactFaxOfTheInstitution"><s:property value="getText('label.ai.contact.faxOfTheInstitution')" /></label>:
			</td>
			<td>
				<input type="text" id="textContactFaxOfTheInstitution" value="<s:property value="#faxOfTheInstitution" />" />
			</td>
			<td id="tdAddFurtherFaxOfTheInstitution" colspan="2">
				<input id="buttonAddFurtherFaxOfTheInstitution" type="button" value="<s:property value='getText("label.ai.contact.addFurtherFaxOfTheInstitution")' />" onclick="addFurtherFaxOfTheInstitution();"/>
			</td>
		</tr>
		<tr id="trEmailOfTheInstitution">
			<td>
				<label for="textContactEmailOfTheInstitution"><s:property value="getText('label.ai.tabs.commons.emailAddress')" /></label>:
			</td>
			<td>
				<input type="text" id="textContactEmailOfTheInstitution" value="<s:property value="#emailOfTheInstitution" />" disabled="disabled" />
			</td>
			<td class="labelLeft">
				<label for="textContactLinkTitleForEmailOfTheInstitution"><s:property value="getText('label.ai.tabs.commons.linkTitle')" /></label>:
			</td>
			<td>
				<input type="text" id="textContactLinkTitleForEmailOfTheInstitution" value="<s:property value="#linkTitleForEmailOfTheInstitution" />" />
			</td>
		</tr>

		<tr>
			<td id="tdAddFurtherEmailsOfTheInstitution" colspan="2">
				<input id="buttonAddFurtherEmailsOfTheInstitution" type="button" value="<s:property value='getText("label.ai.contact.addFurtherEmailsAddresses")' />" onclick="addFurtherEmailsOfTheInstitution();" />
			</td>
		</tr>

		<tr id="trWebOfTheInstitution">
			<td>
				<label for="textContactWebOfTheInstitution"><s:property value="getText('label.ai.tabs.commons.webpage')" /><span class="required">*</span>:</label>
			</td>
			<td>
				<input type="text" id="textContactWebOfTheInstitution" value="<s:property value="#webOfTheInstitution" />" disabled="disabled" />
			</td>
			<td class="labelLeft">
				<label for="textContactLinkTitleForWebOfTheInstitution"><s:property value="getText('label.ai.tabs.commons.linkTitle')" /></label>:
			</td>
			<td>
				<input type="text" id="textContactLinkTitleForWebOfTheInstitution" value="<s:property value="#linkTitleForWebOfTheInstitution" />" />
			</td>
		</tr>

		<tr>
			<td id="tdAddFurtherWebsOfTheInstitution" colspan="2">
				<input id="buttonAddFurtherWebsOfTheInstitution" type="button" value="<s:property value='getText("label.ai.contact.addFurtherWebpages")' />" onclick="addFurtherWebsOfTheInstitution();" />
			</td>
		</tr>

		<tr>
			<td id="tdButtonsContactTab" colspan="4">
				<input type="button" id="buttonContactTabNext" value="<s:property value='getText("label.ai.tabs.commons.button.nextTab")' />" class="rightButton" />
				<input type="button" id="buttonContactTabCheck" value="<s:property value='getText("label.ai.tabs.commons.button.check")' />" class="rightButton" onclick="clickContactAction();" />
			</td>
		</tr>
	</table>
</div>