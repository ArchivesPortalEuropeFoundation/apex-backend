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
				<s:property value="#streetOfTheInstitution" />
				<s:fielderror fieldName="streetOfTheInstitution"/>
			</td>
			<td id="tdLanguageStreetOfTheInstitution">
				<label for="selectLanguageStreetOfTheInstitution" ><s:property value="getText('label.ai.tabs.commons.selectLanguage')" />:</label>
			</td>
			<td>
				<s:select theme="simple" id="selectLanguageStreetOfTheInstitution" list="languageList"></s:select>
			</td>
		</tr>

		<tr>
			<td id="tdCityOfTheInstitution">
				<s:property value="getText('label.ai.tabs.commons.cityTownWithPostalcode')" />
				<span class="required">*</span>:
			</td>
			<td>
				<s:property value="#cityOfTheInstitution" />
				<s:fielderror fieldName="cityOfTheInstitution"/>
			</td>
			<td id="tdLanguageCityOfTheInstitution">
				<label for="selectLanguageCityOfTheInstitution"><s:property value="getText('label.ai.tabs.commons.selectLanguage')" />:</label>
			</td>
			<td>
				<s:select theme="simple" id="selectLanguageCityOfTheInstitution" list="languageList"></s:select>
			</td>
		</tr>

		<tr>
			<td id="tdDistrictOfTheInstitution">
				<s:property value="getText('label.ai.contact.districtOfTheInstitution')" />:
			</td>
			<td>
				<s:property value="#districtOfTheInstitution" />
			</td>
			<td id="tdLanguageDistrictOfTheInstitution">
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
				<s:property value="#countyOfTheInstitution" />
			</td>
			<td id="tdLanguageCountyOfTheInstitution">
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
				<s:property value="#regionOfTheInstitution" />
			</td>
			<td id="tdLanguageRegionOfTheInstitution">
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
				<s:property value="#countryOfTheInstitution" />
				<s:fielderror fieldName="countryOfTheInstitution"/>
			</td>
			<td id="tdLanguageCountryOfTheInstitution">
				<label for="selectLanguageCountryOfTheInstitution"><s:property value="getText('label.ai.tabs.commons.selectLanguage')" />:</label>
			</td>
			<td>
				<s:select theme="simple" id="selectLanguageCountryOfTheInstitution" list="languageList"></s:select>
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
				<s:property value="#latitudeOfTheInstitution" />
			</td>
			<td>
				<s:property value="getText('label.ai.tabs.commons.longitude')" />
			</td>
			<td>
				<s:property value="#longitudeOfTheInstitution" />
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
				<s:property value="#postalStreetOfTheInstitution" />
			</td>
			<td id="tdLanguagePostalStreetOfTheInstitution">
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
				<s:property value="#postalCityOfTheInstitution" />
			</td>
			<td id="tdLanguagePostalCityOfTheInstitution">
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
				<s:select theme="simple" id="selectContinentOfTheInstitution" list="continentOfTheInstitutionList" ></s:select>
				<s:fielderror fieldName="continentOfTheInstitution" />
			</td>
			<td colspan="2" ></td>
		</tr>

		<tr>
			<td id="tdTelephoneOfTheInstitution">
				<s:property value="getText('label.ai.tabs.commons.telephone')" />:
			</td>
			<td>
				<s:property value="#telephoneOfTheInstitution" />
			</td>
			<td id="tdAddFurtherTelephoneOfTheInstitution" colspan="2">
				<input type="button" value="<s:property value='getText("label.ai.contact.addFurtherTelephoneNumbers")' />" class="longButton" />
			</td>
		</tr>

		<tr>
			<td id="tdFaxOfTheInstitution">
				<s:property value="getText('label.ai.contact.faxOfTheInstitution')" />:
			</td>
			<td>
				<s:property value="#faxOfTheInstitution" />
			</td>
			<td id="tdAddFurtherFaxOfTheInstitution" colspan="2">
				<input type="button" value="<s:property value='getText("label.ai.contact.addFurtherFaxOfTheInstitution")' />" class="longButton" />
			</td>
		</tr>

		<tr>
			<td id="tdEmailOfTheInstitution">
				<s:property value="getText('label.ai.tabs.commons.emailAddress')" />:
			</td>
			<td>
				<s:property value="#emailOfTheInstitution" />
			</td>
			<td id="tdLinkTitleForEmailOfTheInstitution">
				<s:property value="getText('label.ai.tabs.commons.linkTitle')" />:
			</td>
			<td>
				<s:property value="#linkTitleForEmailOfTheInstitution" />
			</td>
		</tr>

		<tr>
			<td id="tdAddFurtherEmailsOfTheInstitution" colspan="2">
				<input type="button" value="<s:property value='getText("label.ai.contact.addFurtherEmailsAddresses")' />" class="longButton" />
			</td>
			<td colspan="2">
			</td>
		</tr>

		<tr>
			<td id="tdWebOfTheInstitution">
				<s:property value="getText('label.ai.tabs.commons.webpage')" />
				<span class="required">*</span>:
			</td>
			<td>
				<s:property value="#webOfTheInstitution" />
				<s:fielderror fieldName="webOfTheInstitution"/>
			</td>
			<td id="tdLinkTitleForWebOfTheInstitution">
				<s:property value="getText('label.ai.tabs.commons.linkTitle')" />:
			</td>
			<td>
				<s:property value="#linkTitleForWebOfTheInstitution" />
			</td>
		</tr>

		<tr>
			<td id="tdAddFurtherWebsOfTheInstitution" colspan="2">
				<input type="button" value="<s:property value='getText("label.ai.contact.addFurtherWebpages")' />" class="longButton" />
			</td>
			<td colspan="2">
			</td>			
		</tr>

		<tr>
			<td id="tdButtonsContactTab" colspan="4">
				<input type="button" id="buttonContactTabNext" value="<s:property value='getText("label.ai.tabs.commons.button.nextTab")' />" class="rightButton" />
				<input type="button" id="buttonContactTabExit" value="<s:property value='getText("label.ai.tabs.commons.button.exit")' />" class="rightButton" />
				<input type="button" id="buttonContactTabSave" value="<s:property value='getText("label.ai.tabs.commons.button.save")' />" class="rightButton" />
			</td>
		</tr>
	</table>
</div>