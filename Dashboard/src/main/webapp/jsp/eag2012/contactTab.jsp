<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<div id="contactTabContent">
	<table id="contactTable">
		<tr>
			<td id="visitorAdressLabel" colspan="4">
				<s:property value="getText('label.ai.contact.visitorAddress')" />
			</td>
		</tr>

		<tr>
			<td id="tdStreetOfTheInstitution">
				<s:property value="getText('label.ai.contact.streetOfTheInstitution')" />
				<span class="required">*</span>:
			</td>
			<td>
				<s:property value="#streetOfTheInstitution" />
				<s:fielderror fieldName="streetOfTheInstitution"/>
			</td>
			<td id="tdLanguageStreetOfTheInstitution">
				<s:property value="getText('label.ai.common.select.language')" />
			</td>
			<td>
				<s:label>TODO: Language list</s:label>
				<%-- <s:select id="noti_languageList" list="languageList" />--%>
			</td>
		</tr>

		<tr>
			<td id="tdCityOfTheInstitution">
				<s:property value="getText('label.ai.contact.cityOfTheInstitution')" />
				<span class="required">*</span>:
			</td>
			<td>
				<s:property value="#cityOfTheInstitution" />
				<s:fielderror fieldName="cityOfTheInstitution"/>
			</td>
			<td id="tdLanguageCityOfTheInstitution">
				<s:property value="getText('label.ai.common.select.language')" />
			</td>
			<td>
				<s:label>TODO: Language list</s:label>
				<%-- <s:select id="noti_languageList" list="languageList" />--%>
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
				<s:property value="getText('label.ai.common.select.language')" />
			</td>
			<td>
				<s:label>TODO: Language list</s:label>
				<%-- <s:select id="noti_languageList" list="languageList" />--%>
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
				<s:property value="getText('label.ai.common.select.language')" />
			</td>
			<td>
				<s:label>TODO: Language list</s:label>
				<%-- <s:select id="noti_languageList" list="languageList" />--%>
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
				<s:property value="getText('label.ai.common.select.language')" />
			</td>
			<td>
				<s:label>TODO: Language list</s:label>
				<%-- <s:select id="noti_languageList" list="languageList" />--%>
			</td>
		</tr>

		<tr>
			<td id="tdCountryOfTheInstitution">
				<s:property value="getText('label.ai.contact.countryOfTheInstitution')" />
				<span class="required">*</span>:
			</td>
			<td>
				<s:property value="#countryOfTheInstitution" />
				<s:fielderror fieldName="countryOfTheInstitution"/>
			</td>
			<td id="tdLanguageCountryOfTheInstitution">
				<s:property value="getText('label.ai.common.select.language')" />
			</td>
			<td>
				<s:label>TODO: Language list</s:label>
				<%-- <s:select id="noti_languageList" list="languageList" />--%>
			</td>
		</tr>

		<tr>
			<td id="coordinatesLabel" colspan="4">
				<s:property value="getText('label.ai.contact.coordinates')" />
			</td>
		</tr>

		<tr>
			<td>
				<s:property value="getText('label.ai.contact.latitudeOfTheInstitution')" />
			</td>
			<td>
				<s:property value="#latitudeOfTheInstitution" />
			</td>
			<td>
				<s:property value="getText('label.ai.contact.longitudeOfTheInstitution')" />
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
				<s:property value="getText('label.ai.contact.postalStreetOfTheInstitution')" />:
			</td>
			<td>
				<s:property value="#postalStreetOfTheInstitution" />
			</td>
			<td id="tdLanguagePostalStreetOfTheInstitution">
				<s:property value="getText('label.ai.common.select.language')" />
			</td>
			<td>
				<s:label>TODO: Language list</s:label>
				<%-- <s:select id="noti_languageList" list="languageList" />--%>
			</td>
		</tr>

		<tr>
			<td id="tdPostalCityOfTheInstitution">
				<s:property value="getText('label.ai.contact.postalCityOfTheInstitution')" />:
			</td>
			<td>
				<s:property value="#postalCityOfTheInstitution" />
			</td>
			<td id="tdLanguagePostalCityOfTheInstitution">
				<s:property value="getText('label.ai.common.select.language')" />
			</td>
			<td>
				<s:label>TODO: Language list</s:label>
				<%-- <s:select id="noti_languageList" list="languageList" />--%>
			</td>
		</tr>

		<tr>
			<td id="tdContinentOfTheInstitution">
				<s:property value="getText('label.ai.contact.continentOfTheInstitution')" />
				<span class="required">*</span>:
			</td>
			<td>
				<s:property value="#continentOfTheInstitution" />
				<s:fielderror fieldName="continentOfTheInstitution" />
			</td>
			<td colspan="2" ></td>
		</tr>

		<tr>
			<td id="tdTelephoneOfTheInstitution">
				<s:property value="getText('label.ai.contact.telephoneOfTheInstitution')" />:
			</td>
			<td>
				<s:property value="#telephoneOfTheInstitution" />
			</td>
			<td id="tdAddFurtherTelephoneOfTheInstitution" colspan="2">
				<input type="button" value="<s:property value='getText("label.ai.contact.addFurtherTelephoneOfTheInstitution")' />" class="longButton" />
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
				<s:property value="getText('label.ai.contact.emailOfTheInstitution')" />:
			</td>
			<td>
				<s:property value="#emailOfTheInstitution" />
			</td>
			<td id="tdLinkTitleForEmailOfTheInstitution">
				<s:property value="getText('label.ai.contact.LinkTitleForEmailOfTheInstitution')" />
			</td>
			<td>
				<s:property value="#linkTitleForEmailOfTheInstitution" />
			</td>
		</tr>

		<tr>
			<td id="tdAddFurtherEmailsOfTheInstitution" colspan="4">
				<input type="button" value="<s:property value='getText("label.ai.contact.addFurtherEmailsOfTheInstitution")' />" class="longButton" />
			</td>
		</tr>

		<tr>
			<td id="tdWebOfTheInstitution">
				<s:property value="getText('label.ai.contact.webOfTheInstitution')" />
				<span class="required">*</span>:
			</td>
			<td>
				<s:property value="#webOfTheInstitution" />
				<s:fielderror fieldName="webOfTheInstitution"/>
			</td>
			<td id="tdLinkTitleForWebOfTheInstitution">
				<s:property value="getText('label.ai.contact.LinkTitleForWebOfTheInstitution')" />
			</td>
			<td>
				<s:property value="#linkTitleForWebOfTheInstitution" />
			</td>
		</tr>

		<tr>
			<td id="tdAddFurtherWebsOfTheInstitution" colspan="4">
				<input type="button" value="<s:property value='getText("label.ai.contact.addFurtherWebsOfTheInstitution")' />" class="longButton" />
			</td>
		</tr>

		<tr>
			<td id="tdButtonsContactTab" colspan="4">
				<input type="button" id="buttonContactTabSave" value="<s:property value='getText("label.ai.common.save")' />" class="rightButton" />
				<input type="button" id="buttonContactTabExit" value="<s:property value='getText("label.ai.common.exit")' />" class="rightButton" />
				<input type="button" id="buttonContactTabNext" value="<s:property value='getText("label.ai.common.nextTab")' />" class="rightButton" />
			</td>
		</tr>
	</table>
</div>