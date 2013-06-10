<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<div id="headerContainer">
</div>

<div id="contactTabContent">
	<table id="contactTable" class="tablePadding">
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
					<input type="text" id="textContactStreetOfTheInstitution" value="${loader.street}" disabled="disabled" />
				</td>
				<td id="tdLanguageVisitorAddress" class="labelLeft">
					<label for="selectLanguageVisitorAddress" ><s:property value="getText('label.ai.tabs.commons.selectLanguage')" />:</label>
				</td>
				<td>
					<select id="selectLanguageVisitorAddress" onchange="contactStreetLanguageChanged();">
						<s:iterator value="languageList" var="language"> 
							<option value="<s:property value="#language.key" />"<s:if test="%{#language.key == loader.streetLang}" > selected=selected </s:if>><s:property value="#language.value" /></option>
						</s:iterator>
					</select>
				</td>
			</tr>

			<tr>
				<td id="tdCityOfTheInstitution">
					<label for="textContactCityOfTheInstitution"><s:property value="getText('label.ai.tabs.commons.cityTownWithPostalcode')" /><span class="required">*</span>:</label>
				</td>
				<td>
					<input type="text" id="textContactCityOfTheInstitution" value="${loader.municipalityPostalcode}" disabled="disabled" />
				</td>
				<td colspan="2">
				</td>
			</tr>

			<tr>
				<td id="tdDistrictOfTheInstitution">
					<label for="textContactDistrictOfTheInstitution"><s:property value="getText('label.ai.contact.districtOfTheInstitution')" />:</label>
				</td>
				<td>
					<input type="text" id="textContactDistrictOfTheInstitution" value="${loader.localentity}" />
				</td>
				<td colspan="2">
				</td>
			</tr>

			<tr>
				<td id="tdCountyOfTheInstitution">
					<label for="textContactCountyOfTheInstitution"><s:property value="getText('label.ai.contact.countyOfTheInstitution')" />:</label>
				</td>
				<td>
					<input type="text" id="textContactCountyOfTheInstitution" value="${loader.secondem}" />
				</td>
				<td colspan="2">
				</td>
			</tr>

			<tr>
				<td id="tdRegionOfTheInstitution">
					<label for="textContactRegionOfTheInstitution"><s:property value="getText('label.ai.contact.regionOfTheInstitution')" />:</label>
				</td>
				<td>
					<input type="text" id="textContactRegionOfTheInstitution" value="${loader.firstdem}" />
				</td>
				<td colspan="2">
				</td>
			</tr>

			<tr>
				<td id="tdCountryOfTheInstitution">
					<label for="textContactCountryOfTheInstitution"><s:property value="getText('label.ai.tabs.commons.country')" /><span class="required">*</span>:</label>
				</td>
				<td>
					<input type="text" id="textContactCountryOfTheInstitution" value="${loader.country}" disabled="disabled" />
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
					<input type="text" id="textContactLatitudeOfTheInstitution" value="${loader.latitude}" onchange="contactLatitudeChanged();" <s:if test="%{loader.latitude!=null && loader.latitude.length()>0}"> disabled=disabled </s:if>/>
				</td>
				<td class="labelLeft">
					<label for="textContactLongitudeOfTheInstitution"><s:property value="getText('label.ai.tabs.commons.longitude')" /></label>
				</td>
				<td>
					<input type="text" id="textContactLongitudeOfTheInstitution" value="${loader.longitude}" onchange="contactLongitudeChanged();" <s:if test="%{loader.longitude!=null && loader.longitude.length()>0}"> disabled=disabled </s:if>/>
				</td>
			</tr>
		</table></td></tr>

		<tr>
			<td id="tdContactAddVisitorsAddressTranslation" colspan="2">
				<input type="button" id="buttonContactAddVisitorsAddressTranslation"  value="<s:property value='getText("label.ai.tabs.commons.addFurtherVisitorsAddress")' />" onclick="contactAddVisitorsAddressTranslation('<s:property value="getText('label.ai.tabs.commons.pleaseFillData')" />');" />
			</td>
		</tr>

		<tr id="trButtonContactAddPostalAddressIfDifferent">
			<td colspan="4">
				<input type="button" id="buttonContactAddPostalAddressIfDifferent" value="<s:property value="getText('label.ai.tabs.commons.addPostalAddressIfDifferent')"/>" onclick="contactAddPostalAddressIfDifferent('<s:property value="getText('label.ai.contact.postalAddress')" />','<s:property value="getText('label.ai.tabs.commons.selectLanguage')"/>','<s:property value="getText('label.ai.tabs.commons.street')"/>','<s:property value="getText('label.ai.tabs.commons.cityTownWithPostalcode')"/>');" />
			</td>
		</tr>

		<tr id="trButtonContacPostalAddressTranslation" style="display:none;">
			<td colspan="2">
				<input type="button" id="buttonContacPostalAddressTranslation" value="<s:property value="getText('label.ai.yourinstitution.addPostalAddressTranslation')"/>" onclick="contactAddPostalAddressTranslation('<s:property value="getText('label.ai.tabs.commons.pleaseFillData')" />');" />
			</td>
			<td colspan="2">
			</td>
		</tr>

		<tr>
			<td id="tdContinentOfTheInstitution">
				<label for="selectContinentOfTheInstitution"><s:property value="getText('label.ai.tabs.commons.continent')" /><span class="required">*</span>:</label>
				
			</td>
			<td>
				<select id="selectContinentOfTheInstitution" disabled="disabled">
					<s:iterator value="continentOfTheInstitutionList" var="continent"> 
						<option value="<s:property value="#continent.key" />"<s:if test="%{#continent.key == loader.geogarea}" > selected=selected </s:if>><s:property value="#continent.value" /></option>
					</s:iterator>
				</select>
			</td>
			<td colspan="2" ></td>
		</tr>

		<tr id="trTelephoneOfTheInstitution">
			<td id="tdTelephoneOfTheInstitution">
				<label for="textContactTelephoneOfTheInstitution"><s:property value="getText('label.ai.tabs.commons.telephone')" /></label>:
			</td>
			<td>
				<input type="text" id="textContactTelephoneOfTheInstitution" value="${loader.telephone}"<s:if test="%{loader.telephone!=null && loader.telephone.length()>0}"> disabled=disabled </s:if> onchange="contactTelephoneChanged();" />
			</td>
			<td id="tdAddFurtherTelephoneOfTheInstitution" colspan="2">
				<input id="buttonAddFurtherTelephoneOfTheInstitution" type="button" value="<s:property value='getText("label.ai.contact.addFurtherTelephoneNumbers")' />" onclick="addFurtherTelephoneOfTheInstitution('<s:property value="getText('label.ai.tabs.commons.pleaseFillData')" />');"/>
			</td>
		</tr>
		<tr id="trFaxOfTheInstitution">
			<td id="tdFaxOfTheInstitution">
				<label for="textContactFaxOfTheInstitution"><s:property value="getText('label.ai.contact.faxOfTheInstitution')" /></label>:
			</td>
			<td>
				<input type="text" id="textContactFaxOfTheInstitution" value="${loader.fax}" />
			</td>
			<td id="tdAddFurtherFaxOfTheInstitution" colspan="2">
				<input id="buttonAddFurtherFaxOfTheInstitution" type="button" value="<s:property value='getText("label.ai.contact.addFurtherFaxOfTheInstitution")' />" onclick="addFurtherFaxOfTheInstitution('<s:property value="getText('label.ai.tabs.commons.pleaseFillData')" />');"/>
			</td>
		</tr>
		<tr id="trEmailOfTheInstitution">
			<td>
				<label for="textContactEmailOfTheInstitution"><s:property value="getText('label.ai.tabs.commons.emailAddress')" /></label>:
			</td>
			<td>
				<input type="text" id="textContactEmailOfTheInstitution" value="${loader.email}" onchange="contactEmailChanged();" <s:if test="%{loader.email!=null && loader.email.length()>0}"> disabled=disabled </s:if>/>
			</td>
			<td class="labelLeft">
				<label for="textContactLinkTitleForEmailOfTheInstitution"><s:property value="getText('label.ai.tabs.commons.linkTitle')" /></label>:
			</td>
			<td>
				<input type="text" id="textContactLinkTitleForEmailOfTheInstitution" value="${loader.emailTitle}" onchange="contactEmailOfInstitutionLinkChanged();" />
			</td>
		</tr>

		<tr>
			<td id="tdAddFurtherEmailsOfTheInstitution" colspan="2">
				<input id="buttonAddFurtherEmailsOfTheInstitution" type="button" value="<s:property value='getText("label.ai.contact.addFurtherEmailsAddresses")' />" onclick="addFurtherEmailsOfTheInstitution('<s:property value="getText('label.ai.tabs.commons.pleaseFillData')" />');" />
			</td>
		</tr>

		<tr id="trWebOfTheInstitution">
			<td id="tdLabelTextContactWebOfTheInstitution">
				<label for="textContactWebOfTheInstitution"><s:property value="getText('label.ai.tabs.commons.webpage')" />:</label>
			</td>
			<td>
				<input type="text" id="textContactWebOfTheInstitution" value="${loader.webpage}" onchange="contactWebpageChanged();" <s:if test="%{loader.webpage!=null && loader.webpage.length()>0}"> disabled=disabled </s:if> />
			</td>
			<td class="labelLeft">
				<label for="textContactLinkTitleForWebOfTheInstitution"><s:property value="getText('label.ai.tabs.commons.linkTitle')" /></label>:
			</td>
			<td>
				<input type="text" id="textContactLinkTitleForWebOfTheInstitution" value="${loader.webpageTitle}" onchange="contactWebOfInstitutionLinkChanged();" />
			</td>
		</tr>

		<tr>
			<td id="tdAddFurtherWebsOfTheInstitution" colspan="2">
				<input id="buttonAddFurtherWebsOfTheInstitution" type="button" value="<s:property value='getText("label.ai.contact.addFurtherWebpages")' />" onclick="addFurtherWebsOfTheInstitution('<s:property value="getText('label.ai.tabs.commons.pleaseFillData')" />');" />
			</td>
		</tr>

		<tr>
			<td id="tdButtonsContactTab" colspan="4">
				<input type="button" id="buttonContactTabNext" value="<s:property value='getText("label.ai.tabs.commons.button.nextTab")' />" class="rightButton" onclick="checkAndShowNextTab($(this).parent().parent().parent().parent(), '<s:property value="getText('label.ai.tabs.commons.fieldRequired')" />', '<s:property value="getText('label.ai.tabs.commons.pleaseFillMandatoryFields')" />');" />
				<input type="button" id="buttonContactTabCheck" value="<s:property value='getText("label.ai.tabs.commons.button.check")' />" class="rightButton" onclick="clickContactAction('<s:property value="getText('label.ai.tabs.commons.fieldRequired')" />');" />
			</td>
		</tr>
	</table>
</div>