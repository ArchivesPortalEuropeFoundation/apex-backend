<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<div id="contactTabContent">
	<table id="contactTable">
		<tr>
			<td id="visitorAdressLabel" colspan="4">
				<s:property value="getText('label.ai.tabs.commons.visitorAddress')" />
			</td>
		</tr>

		<tr>
			<td id="tdStreetOfTheInstitution">
				<s:property value="getText('label.ai.tabs.commons.street')" />
				<span class="required">*</span>:
			</td>
			<td>
				<input type="text" id="textContactStreetOfTheInstitution" value="<s:property value="#streetOfTheInstitution" />" disabled="disabled" />
				<s:fielderror fieldName="textContactStreetOfTheInstitution"/>
			</td>
			<td id="tdLanguageStreetOfTheInstitution" class="labelLeft">
				<label for="selectLanguageStreetOfTheInstitution" ><s:property value="getText('label.ai.tabs.commons.selectLanguage')" />:</label>
			</td>
			<td>
				<s:select theme="simple" id="selectLanguageStreetOfTheInstitution" list="languageList" disabled="true"></s:select>
			</td>
		</tr>

		<tr>
			<td id="tdCityOfTheInstitution">
				<s:property value="getText('label.ai.tabs.commons.cityTownWithPostalcode')" />
				<span class="required">*</span>:
			</td>
			<td>
				<input type="text" id="textContactCityOfTheInstitution" value="<s:property value="#cityOfTheInstitution" />" disabled="disabled" />
				<s:fielderror fieldName="textContactCityOfTheInstitution"/>
			</td>
			<td id="tdLanguageCityOfTheInstitution" class="labelLeft">
				<label for="selectLanguageCityOfTheInstitution"><s:property value="getText('label.ai.tabs.commons.selectLanguage')" />:</label>
			</td>
			<td>
				<s:select theme="simple" id="selectLanguageCityOfTheInstitution" list="languageList" disabled="true"></s:select>
			</td>
		</tr>

		<tr>
			<td id="tdDistrictOfTheInstitution">
				<s:property value="getText('label.ai.contact.districtOfTheInstitution')" />:
			</td>
			<td>
				<input type="text" id="textContactDistrictOfTheInstitution" value="<s:property value="#districtOfTheInstitution" />" />
			</td>
			<td id="tdLanguageDistrictOfTheInstitution" class="labelLeft">
				<label for="selectLanguageDistrictOfTheInstitution"><s:property value="getText('label.ai.tabs.commons.selectLanguage')" />:</label>
			</td>
			<td>
				<s:select theme="simple" id="selectLanguageDistrictOfTheInstitution" list="languageList"></s:select>
			</td>
		</tr>

		<tr>
			<td id="tdCountyOfTheInstitution">
				<s:property value="getText('label.ai.contact.countyOfTheInstitution')" />:
			</td>
			<td>
				<input type="text" id="textContactCountyOfTheInstitution" value="<s:property value="#countyOfTheInstitution" />" />
			</td>
			<td id="tdLanguageCountyOfTheInstitution" class="labelLeft">
				<label for="selectLanguageCountyOfTheInstitution"><s:property value="getText('label.ai.tabs.commons.selectLanguage')" />:</label>
			</td>
			<td>
				<s:select theme="simple" id="selectLanguageCountyOfTheInstitution" list="languageList"></s:select>
			</td>
		</tr>

		<tr>
			<td id="tdRegionOfTheInstitution">
				<s:property value="getText('label.ai.contact.regionOfTheInstitution')" />:
			</td>
			<td>
				<input type="text" id="textContactRegionOfTheInstitution" value="<s:property value="#regionOfTheInstitution" />" />
			</td>
			<td id="tdLanguageRegionOfTheInstitution" class="labelLeft">
				<label for="selectLanguageRegionOfTheInstitution"><s:property value="getText('label.ai.tabs.commons.selectLanguage')" />:</label>
			</td>
			<td>
				<s:select theme="simple" id="selectLanguageRegionOfTheInstitution" list="languageList"></s:select>
			</td>
		</tr>

		<tr>
			<td id="tdCountryOfTheInstitution">
				<s:property value="getText('label.ai.tabs.commons.country')" />
				<span class="required">*</span>:
			</td>
			<td>
				<input type="text" id="textContactCountryOfTheInstitution" value="<s:property value="#countryOfTheInstitution" />" disabled="disabled" />
				<s:fielderror fieldName="textContactCountryOfTheInstitution"/>
			</td>
			<td id="tdLanguageCountryOfTheInstitution" class="labelLeft">
				<label for="selectLanguageCountryOfTheInstitution"><s:property value="getText('label.ai.tabs.commons.selectLanguage')" />:</label>
			</td>
			<td>
				<s:select theme="simple" id="selectLanguageCountryOfTheInstitution" list="languageList" disabled="true"></s:select>
			</td>
		</tr>

		<tr>
			<td id="coordinatesLabel" colspan="4">
				<s:property value="getText('label.ai.tabs.commons.coordinates')" />
			</td>
		</tr>

		<tr>
			<td>
				<s:property value="getText('label.ai.tabs.commons.latitude')" />
			</td>
			<td>
				<input type="text" id="textContactLatitudeOfTheInstitution" value="<s:property value="#latitudeOfTheInstitution" />" disabled="disabled" />
			</td>
			<td class="labelLeft">
				<s:property value="getText('label.ai.tabs.commons.longitude')" />
			</td>
			<td>
				<input type="text" id="textContactLongitudeOfTheInstitution" value="<s:property value="#longitudeOfTheInstitution" />" disabled="disabled" />
			</td>
		</tr>

		<tr>
			<td id="postalAddressLabel" colspan="4">
				<s:property value="getText('label.ai.contact.postalAddress')" />
			</td>
		</tr>

		<tr>
			<td id="tdPostalStreetOfTheInstitution">
				<s:property value="getText('label.ai.tabs.commons.street')" />:
			</td>
			<td>
				<input type="text" id="textContactPostalStreetOfTheInstitution" value="<s:property value="#postalStreetOfTheInstitution" />" />
			</td>
			<td id="tdLanguagePostalStreetOfTheInstitution" class="labelLeft">
				<label for="selectLanguagePostalStreetOfTheInstitution"><s:property value="getText('label.ai.tabs.commons.selectLanguage')" />:</label>
			</td>
			<td>
				<s:select theme="simple" id="selectLanguagePostalStreetOfTheInstitution" list="languageList"></s:select>
			</td>
		</tr>

		<tr>
			<td id="tdPostalCityOfTheInstitution">
				<s:property value="getText('label.ai.tabs.commons.cityTownWithPostalcode')" />:
			</td>
			<td>
				<input type="text" id="textContactPostalCityOfTheInstitution" value="<s:property value="#postalCityOfTheInstitution" />" />
			</td>
			<td id="tdLanguagePostalCityOfTheInstitution" class="labelLeft">
				<label for="selectLanguagePostalCityOfTheInstitution"><s:property value="getText('label.ai.tabs.commons.selectLanguage')" />:</label>
			</td>
			<td>
				<s:select theme="simple" id="selectLanguagePostalCityOfTheInstitution" list="languageList"></s:select>
			</td>
		</tr>

		<tr>
			<td id="tdContinentOfTheInstitution">
				<s:property value="getText('label.ai.tabs.commons.continent')" />
				<span class="required">*</span>:
			</td>
			<td>
				<s:select theme="simple" id="selectContinentOfTheInstitution" list="continentOfTheInstitutionList" disabled="true"></s:select>
				<s:fielderror fieldName="continentOfTheInstitution" />
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
			</td>
		</tr>
		<script type="text/javascript">
			$("table#contactTable input#buttonAddFurtherTelephoneOfTheInstitution").click();
		</script>
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
				<label for="textContactWebOfTheInstitution"><s:property value="getText('label.ai.tabs.commons.webpage')" /></label>
				<span class="required">*</span>:
			</td>
			<td>
				<input type="text" id="textContactWebOfTheInstitution" value="<s:property value="#webOfTheInstitution" />" disabled="disabled" />
				<s:fielderror fieldName="webOfTheInstitution"/>
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
				<input type="button" id="buttonContactTabSave" value="<s:property value='getText("label.ai.tabs.commons.button.save")' />" class="rightButton" />
				<script type="text/javascript">
					//current tab
					$("table#contactTable_"+$("table[id^='contactTable_']").length+" input#buttonContactTabSave").click(clickContactAction);
				</script>
			</td>
		</tr>
	</table>
</div>