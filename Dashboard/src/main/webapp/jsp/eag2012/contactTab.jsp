<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<div id="headerContainer">
</div>

<s:if test="%{loader.numberOfRepositories > 0}">
	<div id="contactTabContent">
		<s:set var="counter" value="0"/>
		<s:iterator begin="1" end="loader.numberOfRepositories" status="status">
			<table id="contactTable_<s:property value="%{#status.index + 1}" />" class="tablePadding">
				<s:if test="%{#status.index != 0}">
					<s:set var="repositoryName" value="loader.repositoryName[#counter]"/>
					<s:set var="repositoryRole" value="loader.repositoryRole[#counter]"/>
					<tr>
						<td id="tdNameOfRepository">
							<label for="textNameOfRepository"><s:property value="getText('label.ai.contact.nameOfRepository')" /><span class="required">*</span>:</label>
						</td>
						<td>
							<textarea id="textNameOfRepository" onchange="contactChangeTabName('<s:property value="getText('label.ai.yourinstitution.repository')" />');"><s:property value="#repositoryName[0]" /></textarea>
						<td>
							<label for="selectRoleOfRepository"><s:property value="getText('label.ai.contact.roleOfRepository')" /><span class="required">*</span>:</label>
						</td>
						<td>
							<select id="selectRoleOfRepository">
								<s:iterator value="repositoryRoleList" var="role">
									<option value="<s:property value="#role.key" />"
										<s:if test="%{#role.key == #repositoryRole[0]}" > selected=selected </s:if>>
										<s:property value="#role.value" />
									</option>
								</s:iterator>
							</select>
						</td>
					</tr>
				</s:if>

				<s:set var="numberOfVisitorsAddress" value="loader.contactNumberOfVisitorsAddress[#counter]"/>
				<s:if test="%{#numberOfVisitorsAddress.size() > 0}">
					<s:set var="internalCounter" value="0"/>
					<s:set var="latitude" value="loader.contactLatitude[#counter]"/>
					<s:set var="longitude" value="loader.contactLongitude[#counter]"/>
					<s:set var="country" value="loader.contactCountry[#counter]"/>
					<s:set var="firstdem" value="loader.contactFirstdem[#counter]"/>
					<s:set var="secondem" value="loader.contactSecondem[#counter]"/>
					<s:set var="municipality" value="loader.contactMunicipality[#counter]"/>
					<s:set var="localentity" value="loader.contactLocalentity[#counter]"/>
					<s:set var="street" value="loader.contactStreet[#counter]"/>
					<s:set var="streetLang" value="loader.contactStreetLang[#counter]"/>
					<s:iterator var="internalCurrent" value="#numberOfVisitorsAddress" status="internalStatus">
						<tr id="trVisitorsAddressLabel">
							<td colspan="4">
								<table id="contactTableVisitorsAddress_<s:property value="%{#internalStatus.index + 1}" />">
									<tr>
										<td id="visitorAdressLabel" colspan="4">
											<s:property value="getText('label.ai.tabs.commons.visitorAddress')" />
										</td>
									</tr>

									<tr>
										<td id="tdStreetOfTheInstitution">
											<s:if test="%{#internalStatus.index == 0}">
												<label for="textContactStreetOfTheInstitution"><s:property value="getText('label.ai.tabs.commons.street')" /><span class="required">*</span>:</label>
											</s:if>
											<s:else>
												<label for="textContactStreetOfTheInstitution"><s:property value="getText('label.ai.tabs.commons.street')" />:</label>
											</s:else>
										</td>
										<td>
											<s:if test="%{#counter > 0}">
												<textarea id="textContactStreetOfTheInstitution"><s:property value="#street[#internalCounter]" /></textarea>
											</s:if>
											<s:elseif test="%{#internalStatus.index == 0}">
												<textarea id="textContactStreetOfTheInstitution" onchange="contactStreetOfInstitutionChanged($(this).parent().parent().parent().parent());" disabled="disabled"><s:property value="#street[#internalCounter]" /></textarea>
											</s:elseif>
											<s:else>
												<textarea id="textContactStreetOfTheInstitution" onchange="contactStreetOfInstitutionChanged($(this).parent().parent().parent().parent());"><s:property value="#street[#internalCounter]" /></textarea>
											</s:else>
										</td>
										<td id="tdLanguageVisitorAddress" class="labelLeft">
											<label for="selectLanguageVisitorAddress" ><s:property value="getText('label.ai.tabs.commons.selectLanguage')" />:</label>
										</td>
										<td>
											<s:if test="%{#counter > 0}">
												<select id="selectLanguageVisitorAddress">
											</s:if>
											<s:else>
												<select id="selectLanguageVisitorAddress" onchange="contactStreetLanguageChanged($(this).parent().parent().parent().parent());">
											</s:else>
												<s:iterator value="languageList" var="language"> 
													<option value="<s:property value="#language.key" />"<s:if test="%{#language.key == #streetLang[#internalCounter]}" > selected=selected </s:if>><s:property value="#language.value" /></option>
												</s:iterator>
											</select>
										</td>
									</tr>

									<tr>
										<td id="tdCityOfTheInstitution">
											<s:if test="%{#internalStatus.index == 0}">
												<label for="textContactCityOfTheInstitution"><s:property value="getText('label.ai.tabs.commons.cityTownWithPostalcode')" /><span class="required">*</span>:</label>
											</s:if>
											<s:else>
												<label for="textContactCityOfTheInstitution"><s:property value="getText('label.ai.tabs.commons.cityTownWithPostalcode')" />:</label>
											</s:else>
										</td>
										<td>
											<s:if test="%{#counter > 0}">
												<textarea id="textContactCityOfTheInstitution"><s:property value="#municipality[#internalCounter]" /></textarea>
											</s:if>
											<s:elseif test="%{#internalStatus.index == 0}">
												<textarea id="textContactCityOfTheInstitution" onchange="contactCityOfInstitutionChanged($(this).parent().parent().parent().parent());" disabled="disabled"><s:property value="#municipality[#internalCounter]" /></textarea>
											</s:elseif>
											<s:else>
												<textarea id="textContactCityOfTheInstitution" onchange="contactCityOfInstitutionChanged($(this).parent().parent().parent().parent());"><s:property value="#municipality[#internalCounter]" /></textarea>
											</s:else>
										</td>
										<td colspan="2">
										</td>
									</tr>

									<tr>
										<td id="tdDistrictOfTheInstitution">
											<label for="textContactDistrictOfTheInstitution"><s:property value="getText('label.ai.contact.districtOfTheInstitution')" />:</label>
										</td>
										<td>
											<textarea id="textContactDistrictOfTheInstitution"><s:property value="#localentity[#internalCounter]" /></textarea>
										</td>
										<td colspan="2">
										</td>
									</tr>

									<tr>
										<td id="tdCountyOfTheInstitution">
											<label for="textContactCountyOfTheInstitution"><s:property value="getText('label.ai.contact.countyOfTheInstitution')" />:</label>
										</td>
										<td>
											<textarea id="textContactCountyOfTheInstitution"><s:property value="#secondem[#internalCounter]" /></textarea>
										</td>
										<td colspan="2">
										</td>
									</tr>

									<tr>
										<td id="tdRegionOfTheInstitution">
											<label for="textContactRegionOfTheInstitution"><s:property value="getText('label.ai.contact.regionOfTheInstitution')" />:</label>
										</td>
										<td>
											<textarea id="textContactRegionOfTheInstitution"><s:property value="#firstdem[#internalCounter]" /></textarea>
										</td>
										<td colspan="2">
										</td>
									</tr>

									<tr>
										<td id="tdCountryOfTheInstitution">
											<s:if test="%{#internalStatus.index == 0}">
												<label for="textContactCountryOfTheInstitution"><s:property value="getText('label.ai.tabs.commons.country')" /><span class="required">*</span>:</label>
											</s:if>
											<s:else>
												<label for="textContactCountryOfTheInstitution"><s:property value="getText('label.ai.tabs.commons.country')" />:</label>
											</s:else>
										</td>
										<td>
											<s:if test="%{#counter > 0}">
												<textarea id="textContactCountryOfTheInstitution"><s:property value="#country[#internalCounter]" /></textarea>
											</s:if>
											<s:elseif test="%{#internalStatus.index == 0}">
												<textarea id="textContactCountryOfTheInstitution" onchange="contactCountryOfInstitutionChanged($(this).parent().parent().parent().parent());" disabled="disabled"><s:property value="#country[#internalCounter]" /></textarea>
											</s:elseif>
											<s:else>
												<textarea id="textContactCountryOfTheInstitution" onchange="contactCountryOfInstitutionChanged($(this).parent().parent().parent().parent());"><s:property value="#country[#internalCounter]" /></textarea>
											</s:else>
										</td>
										<td colspan="2">
										</td>
									</tr>

									<tr>
										<td id="coordinatesLabel" colspan="4">
											<a href="http://itouchmap.com/latlong.html" target="_blank"><s:property value="getText('label.ai.tabs.commons.coordinates')" /></a>
										</td>
									</tr>

									<tr>
										<td>
											<label for="textContactLatitudeOfTheInstitution"><s:property value="getText('label.ai.tabs.commons.latitude')" /></label>
										</td>
										<td>
											<s:if test="%{#counter > 0}">
												<input type="text" id="textContactLatitudeOfTheInstitution" value="<s:property value="#latitude[#internalCounter]" />" />
											</s:if>
											<s:else>
												<input type="text" id="textContactLatitudeOfTheInstitution" value="<s:property value="#latitude[#internalCounter]" />" onchange="contactLatitudeChanged($(this).parent().parent().parent().parent());"/>
											</s:else>
										</td>
										<td class="labelLeft">
											<label for="textContactLongitudeOfTheInstitution"><s:property value="getText('label.ai.tabs.commons.longitude')" /></label>
										</td>
										<td>
											<s:if test="%{#counter > 0}">
													<input type="text" id="textContactLongitudeOfTheInstitution" value="<s:property value="#longitude[#internalCounter]" />" />
											</s:if>
											<s:else>
												<input type="text" id="textContactLongitudeOfTheInstitution" value="<s:property value="#longitude[#internalCounter]" />" onchange="contactLongitudeChanged($(this).parent().parent().parent().parent());"/>
											</s:else>
										</td>
									</tr>
								</table>
							</td>
						</tr>
						<s:set var="internalCounter" value="%{#internalCounter + 1}"/>
					</s:iterator>
				</s:if>

				<tr>
					<td id="tdContactAddVisitorsAddressTranslation" colspan="2">
						<input type="button" id="buttonContactAddVisitorsAddressTranslation"  value="<s:property value='getText("label.ai.tabs.commons.addFurtherVisitorsAddress")' />" onclick="contactAddVisitorsAddressTranslation('<s:property value="getText('label.ai.tabs.commons.pleaseFillData')" />');" />
					</td>
				</tr>

				<s:set var="numberOfPostalAddress" value="loader.contactNumberOfPostalAddress[#counter]"/>
				<s:if test="%{#numberOfPostalAddress.size() > 0}">
					<s:set var="internalCounter" value="0"/>
					<s:set var="postalMunicipality" value="loader.contactPostalMunicipality[#counter]"/>
					<s:set var="postalStreet" value="loader.contactPostalStreet[#counter]"/>
					<s:set var="postalStreetLang" value="loader.contactPostalStreetLang[#counter]"/>
					<s:iterator var="internalCurrent" value="#numberOfPostalAddress" status="status">
						<tr>
							<td colspan="4">
								<table id="contactTablePostalAddress_<s:property value="%{#status.index + 1}" />">
									<tr id="trContactPostalAddressLabel">
										<td id="postalAddressLabel" colspan="4">
											<s:property value="getText('label.ai.contact.postalAddress')" />
										</td>
									</tr>

									<tr id="contactPostalAddressStreet">
										<td>
											<s:if test="%{#status.index == 0}">
												<label for="textContactPAStreet"><s:property value="getText('label.ai.tabs.commons.street')" /><span class="required">*</span>:</label>
											</s:if>
											<s:else>
												<label for="textContactPAStreet"><s:property value="getText('label.ai.tabs.commons.street')" />:</label>
											</s:else>
										</td>
										<td>
											<s:if test="%{#counter > 0}">
												<textarea id="textContactPAStreet"><s:property value="#postalStreet[#internalCounter]" /></textarea>
											</s:if>
											<s:else>
												<textarea id="textContactPAStreet" onchange="contactAddressStreetChanged($(this).parent().parent().parent().parent());"><s:property value="#postalStreet[#internalCounter]" /></textarea>
											</s:else>
										</td>
										<td id="contactPostalAddressLanguage" class="labelLeft">
											<label for="selectContactLanguagePostalAddress"><s:property value="getText('label.ai.tabs.commons.selectLanguage')" />:</label>
										</td>
										<td>
											<s:if test="%{#counter > 0}">
												<select id="selectContactLanguagePostalAddress">
											</s:if>
											<s:else>
												<select id="selectContactLanguagePostalAddress" onchange="contactAddressLanguageChanged($(this).parent().parent().parent().parent());">
											</s:else>
												<s:iterator value="languageList" var="language"> 
													<option value="<s:property value="#language.key" />"
														<s:if test="%{#language.key == #postalStreetLang[#internalCounter]}" > selected=selected </s:if>>
														<s:property value="#language.value" />
													</option>
												</s:iterator>	
											</select>
										</td>
									</tr>

									<tr id="contactPostalAddressCity">
										<td>
											<s:if test="%{#status.index == 0}">
												<label for="textContactPACity"><s:property value="getText('label.ai.tabs.commons.cityTownWithPostalcode')" /><span class="required">*</span>:</label>
											</s:if>
											<s:else>
												<label for="textContactPACity"><s:property value="getText('label.ai.tabs.commons.cityTownWithPostalcode')" />:</label>
											</s:else>
										</td>
										<td>
											<s:if test="%{#counter > 0}">
												<textarea id="textContactPACity"><s:property value="#postalMunicipality[#internalCounter]" /></textarea>
											</s:if>
											<s:else>
												<textarea id="textContactPACity" onchange="contactAddressCityChanged($(this).parent().parent().parent().parent());"><s:property value="#postalMunicipality[#internalCounter]" /></textarea>
											</s:else>
										</td>
										<td colspan="2">
										</td>
									</tr>
								</table>
							</td>
						</tr>
						<s:set var="internalCounter" value="%{#internalCounter + 1}"/>
					</s:iterator>

					<tr id="trButtonContacPostalAddressTranslation">
						<td colspan="2">
							<input type="button" id="buttonContacPostalAddressTranslation" value="<s:property value="getText('label.ai.yourinstitution.addPostalAddressTranslation')"/>" onclick="contactAddPostalAddressTranslation('<s:property value="getText('label.ai.tabs.commons.pleaseFillData')" />');" />
						</td>
						<td colspan="2">
						</td>
					</tr>
				</s:if>
				<s:else>
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
				</s:else>

				<tr>
					<td id="tdContinentOfTheInstitution">
						<label for="selectContinentOfTheInstitution"><s:property value="getText('label.ai.tabs.commons.continent')" /><span class="required">*</span>:</label>
					</td>
					<td>
						<s:set var="continentLoaded" value="loader.contactContinent[#counter]"/>
						<select id="selectContinentOfTheInstitution" disabled="disabled">
							<s:iterator value="continentOfTheInstitutionList" var="continent"> 
								<option value="<s:property value="#continent.key" />"<s:if test="%{#continent.key == #continentLoaded[0]}" > selected=selected </s:if>><s:property value="#continent.value" /></option>
							</s:iterator>
						</select>
					</td>
					<td colspan="2" ></td>
				</tr>

				<s:if test="%{loader.contactTelephone.size() > 0}">
					<s:set var="telephone" value="loader.contactTelephone[#counter]"/>
					<s:if test="%{#telephone.size() > 0}">
						<s:iterator var="internalCurrent" value="#telephone" status="internalStatus">
							<tr id="trTelephoneOfTheInstitution_<s:property value="%{#internalStatus.index + 1}" />">
								<td id="tdTelephoneOfTheInstitution_<s:property value="%{#internalStatus.index + 1}" />">
									<label for="textContactTelephoneOfTheInstitution_<s:property value="%{#internalStatus.index + 1}" />"><s:property value="getText('label.ai.tabs.commons.telephone')" />:</label>
								</td>
								<td>
									<s:if test="%{#counter > 0}">
										<input type="text" id="textContactTelephoneOfTheInstitution_<s:property value="%{#internalStatus.index + 1}" />" value="<s:property value="#internalCurrent" />" />
									</s:if>
									<s:else>
										<input type="text" id="textContactTelephoneOfTheInstitution_<s:property value="%{#internalStatus.index + 1}" />" value="<s:property value="#internalCurrent" />" onchange="contactTelephoneChanged($(this));" />
									</s:else>
								</td>
									<s:if test="%{#internalStatus.index == 0}">
										<td id="tdAddFurtherTelephoneOfTheInstitution" colspan="2">
											<input id="buttonAddFurtherTelephoneOfTheInstitution" type="button" value="<s:property value='getText("label.ai.contact.addFurtherTelephoneNumbers")' />" onclick="addFurtherTelephoneOfTheInstitution('<s:property value="getText('label.ai.tabs.commons.pleaseFillData')" />');"/>
										</td>
									</s:if>
									<s:else>
										<td colspan="2">
										</td>
									</s:else>
							</tr>
						</s:iterator>
					</s:if>
					<s:else>
						<tr id="trTelephoneOfTheInstitution_1">
							<td id="tdTelephoneOfTheInstitution_1">
								<label for="textContactTelephoneOfTheInstitution_1"><s:property value="getText('label.ai.tabs.commons.telephone')" />:</label>
							</td>
							<td>
								<s:if test="%{#counter > 0}">
									<input type="text" id="textContactTelephoneOfTheInstitution_1" />
								</s:if>
								<s:else>
									<input type="text" id="textContactTelephoneOfTheInstitution_1" onchange="contactTelephoneChanged($(this));" />
								</s:else>
							</td>
							<td id="tdAddFurtherTelephoneOfTheInstitution" colspan="2">
								<input id="buttonAddFurtherTelephoneOfTheInstitution" type="button" value="<s:property value='getText("label.ai.contact.addFurtherTelephoneNumbers")' />" onclick="addFurtherTelephoneOfTheInstitution('<s:property value="getText('label.ai.tabs.commons.pleaseFillData')" />');"/>
							</td>
						</tr>
					</s:else>
				</s:if>
				<s:else>
					<tr id="trTelephoneOfTheInstitution_1">
						<td id="tdTelephoneOfTheInstitution_1">
							<label for="textContactTelephoneOfTheInstitution_1"><s:property value="getText('label.ai.tabs.commons.telephone')" />:</label>
						</td>
						<td>
							<s:if test="%{#counter > 0}">
								<input type="text" id="textContactTelephoneOfTheInstitution_1" />
							</s:if>
							<s:else>
								<input type="text" id="textContactTelephoneOfTheInstitution_1" onchange="contactTelephoneChanged($(this));" />
							</s:else>
						</td>
						<td id="tdAddFurtherTelephoneOfTheInstitution" colspan="2">
							<input id="buttonAddFurtherTelephoneOfTheInstitution" type="button" value="<s:property value='getText("label.ai.contact.addFurtherTelephoneNumbers")' />" onclick="addFurtherTelephoneOfTheInstitution('<s:property value="getText('label.ai.tabs.commons.pleaseFillData')" />');"/>
						</td>
					</tr>
				</s:else>

				<s:if test="%{loader.contactFax.size() > 0}">
					<s:set var="fax" value="loader.contactFax[#counter]"/>
					<s:if test="%{#fax.size() > 0}">
						<s:iterator var="internalCurrent" value="#fax" status="internalStatus">
							<tr id="trFaxOfTheInstitution_<s:property value="%{#internalStatus.index + 1}" />">
								<td id="tdFaxOfTheInstitution_<s:property value="%{#internalStatus.index + 1}" />">
									<label for="textContactFaxOfTheInstitution_<s:property value="%{#internalStatus.index + 1}" />"><s:property value="getText('label.ai.contact.faxOfTheInstitution')" />:</label>
								</td>
								<td>
									<input type="text" id="textContactFaxOfTheInstitution_<s:property value="%{#internalStatus.index + 1}" />" value="<s:property value="#internalCurrent" />" />
								</td>
								<s:if test="%{#internalStatus.index == 0}">
									<td id="tdAddFurtherFaxOfTheInstitution" colspan="2">
										<input id="buttonAddFurtherFaxOfTheInstitution" type="button" value="<s:property value='getText("label.ai.contact.addFurtherFaxOfTheInstitution")' />" onclick="addFurtherFaxOfTheInstitution('<s:property value="getText('label.ai.tabs.commons.pleaseFillData')" />');"/>
									</td>
								</s:if>
								<s:else>
									<td colspan="2">
									</td>
								</s:else>
							</tr>
						</s:iterator>
					</s:if>
					<s:else>
						<tr id="trFaxOfTheInstitution_1">
							<td id="tdFaxOfTheInstitution_1">
								<label for="textContactFaxOfTheInstitution_1"><s:property value="getText('label.ai.contact.faxOfTheInstitution')" />:</label>
							</td>
							<td>
								<input type="text" id="textContactFaxOfTheInstitution_1"/>
							</td>
							<td id="tdAddFurtherFaxOfTheInstitution" colspan="2">
								<input id="buttonAddFurtherFaxOfTheInstitution" type="button" value="<s:property value='getText("label.ai.contact.addFurtherFaxOfTheInstitution")' />" onclick="addFurtherFaxOfTheInstitution('<s:property value="getText('label.ai.tabs.commons.pleaseFillData')" />');"/>
							</td>
						</tr>
					</s:else>
				</s:if>
				<s:else>
					<tr id="trFaxOfTheInstitution_1">
						<td id="tdFaxOfTheInstitution_1">
							<label for="textContactFaxOfTheInstitution_1"><s:property value="getText('label.ai.contact.faxOfTheInstitution')" />:</label>
						</td>
						<td>
							<input type="text" id="textContactFaxOfTheInstitution_1"/>
						</td>
						<td id="tdAddFurtherFaxOfTheInstitution" colspan="2">
							<input id="buttonAddFurtherFaxOfTheInstitution" type="button" value="<s:property value='getText("label.ai.contact.addFurtherFaxOfTheInstitution")' />" onclick="addFurtherFaxOfTheInstitution('<s:property value="getText('label.ai.tabs.commons.pleaseFillData')" />');"/>
						</td>
					</tr>
				</s:else>

				<s:set var="numberOfEmailAddress" value="loader.contactNumberOfEmailAddress[#counter]"/>
				<s:if test="%{#numberOfEmailAddress.size() > 0}">
					<s:set var="internalCounter" value="0"/>
					<s:set var="emailHref" value="loader.contactEmailHref[#counter]"/>
					<s:set var="emailTitle" value="loader.contactEmailTitle[#counter]"/>
					<s:set var="emailLang" value="loader.contactEmailLang[#counter]"/>
					<s:iterator var="internalCurrent" value="#numberOfEmailAddress" status="internalStatus">
						<s:if test="%{#internalStatus.index == 0}">
							<tr id="trEmailOfTheInstitution">
						</s:if>
						<s:else>
							<tr id="trEmailOfTheInstitution_<s:property value="%{#internalStatus.index + 1}" />">
						</s:else>
							<td>
								<label for="textContactEmailOfTheInstitution_<s:property value="%{#internalStatus.index + 1}" />"><s:property value="getText('label.ai.tabs.commons.emailAddress')" />:</label>
							</td>
							<td>
								<textarea id="textContactEmailOfTheInstitution_<s:property value="%{#internalStatus.index + 1}" />" onchange="contactEmailChanged($(this));"><s:property value="#emailHref[#internalCounter]" /></textarea>
							</td>
							<td class="labelLeft">
								<label for="selectEmailLanguageOfTheInstitution_<s:property value="%{#internalStatus.index + 1}" />"><s:property value="getText('label.ai.tabs.commons.selectLanguage')"/>:</label>
							</td>
							<td>
								<select id="selectEmailLanguageOfTheInstitution_<s:property value="%{#internalStatus.index + 1}" />" onchange="contactEmailLanguageChanged($(this));" >
									<s:iterator value="languageList" var="language"> 
										<option value="<s:property value="#language.key" />"
											<s:if test="%{#language.key == #emailLang[#internalCounter]}" > selected=selected </s:if>>
											<s:property value="#language.value" />
										</option>
									</s:iterator>
								</select>
							</td>
						</tr>

				        <s:if test="%{#internalStatus.index == 0}">
					       <tr id="trLanguageEmailOfTheInstitution">
				        </s:if>
				        <s:else>
					       <tr id="trLanguageEmailOfTheInstitution_<s:property value="%{#internalStatus.index + 1}" />">
				        </s:else>
							<td>
								<label for="textContactLinkTitleForEmailOfTheInstitution_<s:property value="%{#internalStatus.index + 1}" />"><s:property value="getText('label.ai.tabs.commons.linkTitle')" />:</label>
							</td>
							<td>
								<textarea id="textContactLinkTitleForEmailOfTheInstitution_<s:property value="%{#internalStatus.index + 1}" />" onchange="contactEmailOfInstitutionLinkChanged($(this));"><s:property value="#emailTitle[#internalCounter]" /></textarea>
							</td>
						    <td colspan="2">
						    </td>
				    	</tr>
						<s:set var="internalCounter" value="%{#internalCounter + 1}"/>
					</s:iterator>

					<tr>
						<td id="tdAddFurtherEmailsOfTheInstitution" colspan="2">
							<input id="buttonAddFurtherEmailsOfTheInstitution" type="button" value="<s:property value='getText("label.ai.contact.addFurtherEmailsAddresses")' />" onclick="addFurtherEmailsOfTheInstitution('<s:property value="getText('label.ai.tabs.commons.pleaseFillData')" />');" />
						</td>
					</tr>
				</s:if>
				<s:else>
					<tr id="trEmailOfTheInstitution">
						<td>
							<label for="textContactEmailOfTheInstitution_1"><s:property value="getText('label.ai.tabs.commons.emailAddress')" />:</label>
						</td>
						<td>
							<textarea id="textContactEmailOfTheInstitution_1" onchange="contactEmailChanged($(this));"></textarea>
						</td>
						<td class="labelLeft">
							<label for="selectEmailLanguageOfTheInstitution_1"><s:property value="getText('label.ai.tabs.commons.selectLanguage')"/>:</label>
						</td>
						<td>
							<select id="selectEmailLanguageOfTheInstitution_1" onchange="contactEmailLanguageChanged($(this));">
								<s:iterator value="languageList" var="language"> 
									<option value="<s:property value="#language.key" />"><s:property value="#language.value" /></option>
								</s:iterator>
							</select>
						</td>
					</tr>
					<tr id="trLanguageEmailOfTheInstitution" >
						<td>
							<label for="textContactLinkTitleForEmailOfTheInstitution_1"><s:property value="getText('label.ai.tabs.commons.linkTitle')" />:</label>
						</td>
						<td>
							<textarea id="textContactLinkTitleForEmailOfTheInstitution_1" onchange="contactEmailOfInstitutionLinkChanged($(this));"></textarea>
						</td>
						<td colspan="2">
						</td>
					</tr>

					<tr>
						<td id="tdAddFurtherEmailsOfTheInstitution" colspan="2">
							<input id="buttonAddFurtherEmailsOfTheInstitution" type="button" value="<s:property value='getText("label.ai.contact.addFurtherEmailsAddresses")' />" onclick="addFurtherEmailsOfTheInstitution('<s:property value="getText('label.ai.tabs.commons.pleaseFillData')" />');" />
						</td>
					</tr>
				</s:else>

				<s:set var="numberOfWebpageAddress" value="loader.contactNumberOfWebpageAddress[#counter]"/>
				<s:if test="%{#numberOfWebpageAddress.size() > 0}">
					<s:set var="internalCounter" value="0"/>
					<s:set var="webpageHref" value="loader.contactWebpageHref[#counter]"/>
					<s:set var="webpageTitle" value="loader.contactWebpageTitle[#counter]"/>
					<s:set var="webpageLang" value="loader.contactWebpageLang[#counter]"/>
					<s:iterator var="internalCurrent" value="#numberOfWebpageAddress" status="internalStatus">
						<s:if test="%{#internalStatus.index == 0}">
							<tr id="trWebOfTheInstitution">
						</s:if>
						<s:else>
							<tr id="trWebOfTheInstitution_<s:property value="%{#internalStatus.index + 1}" />">
						</s:else>
							<td>
								<label for="textContactWebOfTheInstitution_<s:property value="%{#internalStatus.index + 1}" />"><s:property value="getText('label.ai.tabs.commons.webpage')" />:</label>
							</td>
							<td>
								<textarea id="textContactWebOfTheInstitution_<s:property value="%{#internalStatus.index + 1}" />" onchange="contactWebpageChanged($(this));"><s:property value="#webpageHref[#internalCounter]" /></textarea>
							</td>
							<td class="labelLeft">
								<label for="selectWebpageLanguageOfTheInstitution_<s:property value="%{#internalStatus.index + 1}" />"><s:property value="getText('label.ai.tabs.commons.selectLanguage')"/>:</label>
							</td>
							<td>
								<select id="selectWebpageLanguageOfTheInstitution_<s:property value="%{#internalStatus.index + 1}" />" onchange="contactWebpageLangChanged($(this));">
									<s:iterator value="languageList" var="language"> 
										<option value="<s:property value="#language.key" />"
											<s:if test="%{#language.key == #webpageLang[#internalCounter]}" > selected=selected </s:if>>
											<s:property value="#language.value" /></option>
									</s:iterator>
								</select>
							</td>
						</tr>

						<s:if test="%{#internalStatus.index == 0}">
							<tr id="trLanguageWebpageOfTheInstitution">
						</s:if>
						<s:else>
							<tr id="trLanguageWebpageOfTheInstitution_<s:property value="%{#internalStatus.index + 1}" />">
						</s:else>
							<td>
								<label for="textContactLinkTitleForWebOfTheInstitution_<s:property value="%{#internalStatus.index + 1}" />"><s:property value="getText('label.ai.tabs.commons.linkTitle')" />:</label>
							</td>
							<td>
								<textarea id="textContactLinkTitleForWebOfTheInstitution_<s:property value="%{#internalStatus.index + 1}" />" onchange="contactWebOfInstitutionLinkChanged($(this));"><s:property value="#webpageTitle[#internalCounter]" /></textarea>
							</td>
							<td colspan="2">
							</td>
				    	</tr>
						<s:set var="internalCounter" value="%{#internalCounter + 1}"/>
					</s:iterator>

					<tr>
						<td id="tdAddFurtherWebsOfTheInstitution" colspan="2">
							<input id="buttonAddFurtherWebsOfTheInstitution" type="button" value="<s:property value='getText("label.ai.contact.addFurtherWebpages")' />" onclick="addFurtherWebsOfTheInstitution('<s:property value="getText('label.ai.tabs.commons.pleaseFillData')" />');" />
						</td>
					</tr>
				</s:if>
				<s:else>
					<tr id="trWebOfTheInstitution">
						<td id="tdLabelTextContactWebOfTheInstitution_1">
							<label for="textContactWebOfTheInstitution_1"><s:property value="getText('label.ai.tabs.commons.webpage')" />:</label>
						</td>
						<td>
							<textarea id="textContactWebOfTheInstitution_1" onchange="contactWebpageChanged($(this));"></textarea>
						</td>
						<td class="labelLeft">
							<label for="selectWebpageLanguageOfTheInstitution_1"><s:property value="getText('label.ai.tabs.commons.selectLanguage')"/>:</label>
						</td>
						<td>
							<select id="selectWebpageLanguageOfTheInstitution_1" onchange="contactWebpageLangChanged($(this));">
								<s:iterator value="languageList" var="language"> 
									<option value="<s:property value="#language.key" />"><s:property value="#language.value" /></option>
								</s:iterator>
							</select>
						</td>
					</tr>

                    <tr id="trLanguageWebpageOfTheInstitution" >
						<td>
							<label for="textContactLinkTitleForWebOfTheInstitution_1"><s:property value="getText('label.ai.tabs.commons.linkTitle')" />:</label>
						</td>
						<td>
							<textarea id="textContactLinkTitleForWebOfTheInstitution_1" onchange="contactWebOfInstitutionLinkChanged($(this));"></textarea>
						</td>
						<td colspan="2">
						</td>
				    </tr>

					<tr>
						<td id="tdAddFurtherWebsOfTheInstitution" colspan="2">
							<input id="buttonAddFurtherWebsOfTheInstitution" type="button" value="<s:property value='getText("label.ai.contact.addFurtherWebpages")' />" onclick="addFurtherWebsOfTheInstitution('<s:property value="getText('label.ai.tabs.commons.pleaseFillData')" />');" />
						</td>
					</tr>
				</s:else>

				<tr>
					<td id="tdButtonsContactTab" colspan="4">
						<input type="button" id="buttonContactTabNext" value="<s:property value='getText("label.ai.tabs.commons.button.nextTab")' />" class="rightButton" onclick="checkAndShowNextTab($(this).parent().parent().parent().parent(), '<s:property value="getText('label.ai.tabs.commons.fieldRequired')" />', '<s:property value="getText('label.ai.tabs.commons.pleaseFillMandatoryFields')" />','<s:property value="getText('label.eag.eagwithurlwarnings')"/>');" />
						<s:if test="%{#counter == 0}">
							<input type="button" id="buttonContactTabPrevious" value="<s:property value='getText("label.ai.tabs.commons.button.previousTab")' />" class="rightButton" onclick="checkAndShowPreviousTab($(this).parent().parent().parent().parent(),'<s:property value="getText('label.ai.tabs.commons.fieldRequired')" />','<s:property value="getText('label.ai.tabs.commons.pleaseFillMandatoryFields')" />','<s:property value="getText('label.eag.eagwithurlwarnings')"/>');" />
						</s:if>
					</td>
				</tr>
			</table>

			<s:set var="counter" value="%{#counter + 1}"/>
		</s:iterator>
	</div>
</s:if>

<div id="contactTabContent" style="display:none;">
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
					<textarea id="textContactStreetOfTheInstitution" onchange="contactStreetOfInstitutionChanged($(this).parent().parent().parent().parent());" disabled="disabled"></textarea>
				</td>
				<td id="tdLanguageVisitorAddress" class="labelLeft">
					<label for="selectLanguageVisitorAddress" ><s:property value="getText('label.ai.tabs.commons.selectLanguage')" />:</label>
				</td>
				<td>
					<select id="selectLanguageVisitorAddress" onchange="contactStreetLanguageChanged($(this).parent().parent().parent().parent());">
						<s:iterator value="languageList" var="language"> 
							<option value="<s:property value="#language.key" />"><s:property value="#language.value" /></option>
						</s:iterator>
					</select>
				</td>
			</tr>

			<tr>
				<td id="tdCityOfTheInstitution">
					<label for="textContactCityOfTheInstitution"><s:property value="getText('label.ai.tabs.commons.cityTownWithPostalcode')" /><span class="required">*</span>:</label>
				</td>
				<td>
					<textarea id="textContactCityOfTheInstitution" disabled="disabled"></textarea>
				</td>
				<td colspan="2">
				</td>
			</tr>

			<tr>
				<td id="tdDistrictOfTheInstitution">
					<label for="textContactDistrictOfTheInstitution"><s:property value="getText('label.ai.contact.districtOfTheInstitution')" />:</label>
				</td>
				<td>
					<textarea id="textContactDistrictOfTheInstitution"></textarea>
				</td>
				<td colspan="2">
				</td>
			</tr>

			<tr>
				<td id="tdCountyOfTheInstitution">
					<label for="textContactCountyOfTheInstitution"><s:property value="getText('label.ai.contact.countyOfTheInstitution')" />:</label>
				</td>
				<td>
					<textarea id="textContactCountyOfTheInstitution"></textarea>
				</td>
				<td colspan="2">
				</td>
			</tr>

			<tr>
				<td id="tdRegionOfTheInstitution">
					<label for="textContactRegionOfTheInstitution"><s:property value="getText('label.ai.contact.regionOfTheInstitution')" />:</label>
				</td>
				<td>
					<textarea id="textContactRegionOfTheInstitution"></textarea>
				</td>
				<td colspan="2">
				</td>
			</tr>

			<tr>
				<td id="tdCountryOfTheInstitution">
					<label for="textContactCountryOfTheInstitution"><s:property value="getText('label.ai.tabs.commons.country')" /><span class="required">*</span>:</label>
				</td>
				<td>
					<textarea id="textContactCountryOfTheInstitution" onchange="contactCountryOfInstitutionChanged($(this).parent().parent().parent().parent());" disabled="disabled"></textarea>
				</td>
				<td colspan="2">
				</td>
			</tr>

			<tr>
				<td id="coordinatesLabel" colspan="4">
					<a href="http://itouchmap.com/latlong.html" target="_blank"><s:property value="getText('label.ai.tabs.commons.coordinates')" /></a>
				</td>
			</tr>

			<tr>
				<td>
					<label for="textContactLatitudeOfTheInstitution"><s:property value="getText('label.ai.tabs.commons.latitude')" /></label>
				</td>
				<td>
					<input type="text" id="textContactLatitudeOfTheInstitution" onchange="contactLatitudeChanged($(this).parent().parent().parent().parent());"/>
				</td>
				<td class="labelLeft">
					<label for="textContactLongitudeOfTheInstitution"><s:property value="getText('label.ai.tabs.commons.longitude')" /></label>
				</td>
				<td>
					<input type="text" id="textContactLongitudeOfTheInstitution" onchange="contactLongitudeChanged($(this).parent().parent().parent().parent());"/>
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
						<option value="<s:property value="#continent.key" />"><s:property value="#continent.value" /></option>
					</s:iterator>
				</select>
			</td>
			<td colspan="2" ></td>
		</tr>

		<tr id="trTelephoneOfTheInstitution_1">
			<td id="tdTelephoneOfTheInstitution_1">
				<label for="textContactTelephoneOfTheInstitution_1"><s:property value="getText('label.ai.tabs.commons.telephone')" />:</label>
			</td>
			<td>
				<input type="text" id="textContactTelephoneOfTheInstitution_1" onchange="contactTelephoneChanged($(this))" />
			</td>
			<td id="tdAddFurtherTelephoneOfTheInstitution" colspan="2">
				<input id="buttonAddFurtherTelephoneOfTheInstitution" type="button" value="<s:property value='getText("label.ai.contact.addFurtherTelephoneNumbers")' />" onclick="addFurtherTelephoneOfTheInstitution('<s:property value="getText('label.ai.tabs.commons.pleaseFillData')" />');"/>
			</td>
		</tr>
		<tr id="trFaxOfTheInstitution_1">
			<td id="tdFaxOfTheInstitution_1">
				<label for="textContactFaxOfTheInstitution_1"><s:property value="getText('label.ai.contact.faxOfTheInstitution')" />:</label>
			</td>
			<td>
				<input type="text" id="textContactFaxOfTheInstitution_1" />
			</td>
			<td id="tdAddFurtherFaxOfTheInstitution" colspan="2">
				<input id="buttonAddFurtherFaxOfTheInstitution" type="button" value="<s:property value='getText("label.ai.contact.addFurtherFaxOfTheInstitution")' />" onclick="addFurtherFaxOfTheInstitution('<s:property value="getText('label.ai.tabs.commons.pleaseFillData')" />');"/>
			</td>
		</tr>
		<tr id="trEmailOfTheInstitution">
			<td>
				<label for="textContactEmailOfTheInstitution_1"><s:property value="getText('label.ai.tabs.commons.emailAddress')" />:</label>
			</td>
			<td>
				<textarea id="textContactEmailOfTheInstitution_1" onchange="contactEmailChanged($(this));"></textarea>
			</td>
			<td class="labelLeft">
				<label for="selectEmailLanguageOfTheInstitution"><s:property value="getText('label.ai.tabs.commons.selectLanguage')"/>:</label>
			</td>
			<td>
				<select id="selectEmailLanguageOfTheInstitution" onchange="contactEmailLanguageChanged($(this));">
					<s:iterator value="languageList" var="language"> 
						<option value="<s:property value="#language.key" />"><s:property value="#language.value" /></option>
					</s:iterator>
				</select>
			</td>
		</tr>
		<tr id="trLanguageEmailOfTheInstitution" >
				<td>
					<label for="textContactLinkTitleForEmailOfTheInstitution_1"><s:property value="getText('label.ai.tabs.commons.linkTitle')" />:</label>
				</td>
				<td>
					<textarea id="textContactLinkTitleForEmailOfTheInstitution_1" onchange="contactEmailOfInstitutionLinkChanged($(this));"></textarea>
				</td>
				<td colspan="2"></td>
		</tr>
		<tr>
			<td id="tdAddFurtherEmailsOfTheInstitution" colspan="2">
				<input id="buttonAddFurtherEmailsOfTheInstitution" type="button" value="<s:property value='getText("label.ai.contact.addFurtherEmailsAddresses")' />" onclick="addFurtherEmailsOfTheInstitution('<s:property value="getText('label.ai.tabs.commons.pleaseFillData')" />');" />
			</td>
		</tr>

		<tr id="trWebOfTheInstitution">
			<td id="tdLabelTextContactWebOfTheInstitution_1">
				<label for="textContactWebOfTheInstitution_1"><s:property value="getText('label.ai.tabs.commons.webpage')" />:</label>
			</td>
			<td>
				<textarea id="textContactWebOfTheInstitution_1" onchange="contactWebpageChanged($(this));"></textarea>
			</td>
			<td class="labelLeft">
				<label for="selectWebpageLanguageOfTheInstitution_1"><s:property value="getText('label.ai.tabs.commons.selectLanguage')"/>:</label>
			</td>
			<td>
				<select id="selectWebpageLanguageOfTheInstitution_1" onchange="contactWebpageLangChanged($(this));">
					<s:iterator value="languageList" var="language"> 
						<option value="<s:property value="#language.key" />"><s:property value="#language.value" /></option>
					</s:iterator>
				</select>
			</td>
		</tr>
        <tr id="trLanguageWebpageOfTheInstitution" >
			<td>
				<label for="textContactLinkTitleForWebOfTheInstitution_1"><s:property value="getText('label.ai.tabs.commons.linkTitle')" />:</label>
			</td>
			<td>
				<textarea id="textContactLinkTitleForWebOfTheInstitution_1" onchange="contactWebOfInstitutionLinkChanged($(this));"></textarea>
			</td>
			<td colspan="2">
			</td>
		</tr>
		<tr>
			<td id="tdAddFurtherWebsOfTheInstitution" colspan="2">
				<input id="buttonAddFurtherWebsOfTheInstitution" type="button" value="<s:property value='getText("label.ai.contact.addFurtherWebpages")' />" onclick="addFurtherWebsOfTheInstitution('<s:property value="getText('label.ai.tabs.commons.pleaseFillData')" />');" />
			</td>
		</tr>

		<tr>
			<td id="tdButtonsContactTab" colspan="4">
				<input type="button" id="buttonContactTabNext" value="<s:property value='getText("label.ai.tabs.commons.button.nextTab")' />" class="rightButton" onclick="checkAndShowNextTab($(this).parent().parent().parent().parent(), '<s:property value="getText('label.ai.tabs.commons.fieldRequired')" />', '<s:property value="getText('label.ai.tabs.commons.pleaseFillMandatoryFields')" />','<s:property value="getText('label.eag.eagwithurlwarnings')"/>');" />
				<input type="button" id="buttonContactTabPrevious" value="<s:property value='getText("label.ai.tabs.commons.button.previousTab")' />" class="rightButton" onclick="checkAndShowPreviousTab($(this).parent().parent().parent().parent(),'<s:property value="getText('label.ai.tabs.commons.fieldRequired')" />','<s:property value="getText('label.ai.tabs.commons.pleaseFillMandatoryFields')" />','<s:property value="getText('label.eag.eagwithurlwarnings')"/>');" />
			</td>
		</tr>
	</table>
</div>