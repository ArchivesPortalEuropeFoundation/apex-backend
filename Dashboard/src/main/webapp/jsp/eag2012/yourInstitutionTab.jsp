<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<div id="headerContainer">
</div>

<div id="yourInstitutionTabContent">
	<table id="yourInstitutionTable_1" class="tablePadding">	
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
				<input type="text" id="textYIIdentifierOfTheInstitution" value="${loader.otherRepositorId}" onKeyup="idOfInstitutionChanged('<s:property value="getText('label.ai.tabs.commons.repeatISIL')"/>','<s:property value="getText('label.ai.tabs.commons.errorISIL')"/>');"/>
			</td>
			<td class="labelLeft">
			  <label for="textYICodeISL"><s:property value="getText('label.ai.tabs.commons.codeISL')"/></label>
			</td>
			<td>
			  <select id="selectYICodeISIL" onclick="codeISILChanged('<s:property value="getText('label.ai.tabs.commons.errorISIL')"/>');">
			  	<s:iterator value="yesNoList" var="list">
			  		<option value='<s:property value="#list.key"/>' <s:if test='%{newEag && #list.key=="no"}'>selected=selected</s:if><s:if test='%{#list.key == loader.recordIdISIL}'>selected=selected</s:if>><s:property value="#list.value"/></option>
			  	</s:iterator>
			  </select>
			</td>
		</tr>

		<s:if test="%{loader.otherRecordId.size() > 0}">
			<s:iterator var="current" value="loader.otherRecordId" status="status">
				<tr>
					<td>
						<label for="otherRepositorId_<s:property value="#status.index" />"> <s:property value="getText('label.ai.yourinstitution.futherId')" />:</label>
					</td>
					<td>
						<input type="text" id="otherRepositorId_<s:property value="#status.index" />" value="<s:property value="#current" />" onclick="idOfInstitutionChanged('<s:property value="getText('label.ai.tabs.commons.repeatISIL')"/>','<s:property value="getText('label.ai.tabs.commons.errorISIL')"/>','<s:property value="#status.index" />');" onkeyup="idOfInstitutionChanged('<s:property value="getText('label.ai.tabs.commons.repeatISIL')"/>','<s:property value="getText('label.ai.tabs.commons.errorISIL')"/>','<s:property value="#status.index" />');" />
					</td>
					<td class="labelLeft">
						<label for="selectOtherRepositorIdCodeISIL_<s:property value="#status.index" />"> <s:property value="getText('label.ai.tabs.commons.codeISL')" />:</label>
					</td>
					<td>
						<select id="selectOtherRepositorIdCodeISIL_<s:property value="#status.index" />" onclick="codeISILChanged('<s:property value="getText('label.ai.tabs.commons.errorISIL')"/>','<s:property value="#status.index" />');">
						  	<s:iterator value="yesNoList" var="list">
						  		<option value='<s:property value="#list.key"/>'
						  			<s:if test='%{#list.key == "no" && #current != loader.recordId}'>selected=selected</s:if>>
						  			<s:property value="#list.value"/>
						  		</option>
						  	</s:iterator>
						</select>
					</td>
				</tr>
			</s:iterator>
		</s:if>

		<tr>
			<td colspan="2">
				<input type="button" id="buttonAddFutherIds" value="<s:property value="getText('label.ai.yourinstitution.addFutherIds')" />" onclick="addFurtherIds('<s:property value="getText('label.ai.yourinstitution.futherId')" />','<s:property value="getText('label.ai.tabs.commons.pleaseFillData')" />','<s:property value="getText('label.ai.tabs.commons.codeISL')" />','<s:property value="getText('label.ai.tabs.commons.repeatISIL')" />','<s:property value="getText('label.ai.tabs.commons.errorISIL')" />');" />
			</td>
			<td class="labelLeft">
				<label for="textYIIdUsedInAPE"><s:property value="getText('label.ai.tabs.commons.idUsedInAPE')"/>:</label>
			</td>
			<td>
			    <%-- <input type="hidden" id="selfrecordId" value="${loader.selfRecordId}"/> --%>
				<input type="hidden" id="recordIdHidden" value="${loader.selfRecordId}"/>
				<input type="text" id="textYIIdUsedInAPE" value="<s:if test="%{!newEag}">${loader.recordId}</s:if>" disabled="disabled" />
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
				<label for="selectYINOTISelectLanguage"><s:property value="getText('label.ai.tabs.commons.selectLanguage')"/>:</label>
			</td>
			<td>
				<select id="selectYINOTISelectLanguage" onchange="nameOfInstitutionLanguageChanged();" >
					<s:iterator value="languageList" var="language"> 
						<option value="<s:property value="#language.key" />"<s:if test="%{#language.key == loader.autformLang}" > selected=selected </s:if>><s:property value="#language.value" /></option>
					</s:iterator>
				</select>
			</td>
		</tr>

		<tr>
			<td colspan="3">
			</td>
			<td>
				<input type="button" id="buttonAddRepositories" value="<s:property value="getText('label.ai.yourinstitution.addRepositories')" />"  onclick="addRepositories('<s:property value="getText('label.ai.yourinstitution.institution')" />','<s:property value="getText('label.ai.yourinstitution.repository')" />','<s:property value="getText('label.ai.contact.nameOfRepository')" />','<s:property value="getText('label.ai.contact.roleOfRepository')" />','<s:property value="getText('label.ai.tabs.commons.option.role.headquarters')" />','<s:property value="getText('label.ai.tabs.commons.option.role.branch')" />','<s:property value="getText('label.ai.tabs.commons.option.role.interimArchive')" />');" />
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
			<td>
				<select id="selectYIPNOTISelectLanguage" onchange="parallelNameOfInstitutionLanguageChanged();" >
					<s:iterator value="languageList" var="language"> 
						<option value="<s:property value="#language.key" />"<s:if test="%{#language.key == loader.parformLang}" > selected=selected </s:if>><s:property value="#language.value" /></option>
					</s:iterator>
				</select>
			</td>
		</tr>
	</table>

	<s:if test="%{loader.yiNumberOfVisitorsAddress.size() > 0}">
		<s:set var="counter" value="0"/>
		<s:iterator var="current" value="loader.yiNumberOfVisitorsAddress" status="status">
			<table id="yiTableVisitorsAddress_<s:property value="%{#status.index + 1}" />" class="tablePadding">
				<tr>
					<td id="visitorAdressLabel" colspan="4">
						<s:property value="getText('label.ai.tabs.commons.visitorAddress')" />
					</td>
				</tr>
	
				<tr>
					<td>
						<s:if test="%{#status.index == 0}">
							<label for="textYIStreet"><s:property value="getText('label.ai.tabs.commons.street')"/><span class="required">*</span>:</label>
						</s:if>
						<s:else>
							<label for="textYIStreet"><s:property value="getText('label.ai.tabs.commons.street')"/>:</label>
						</s:else>
					</td>
					<td>
						<input type="text" id="textYIStreet" onchange="streetOfInstitutionChanged($(this).parent().parent().parent().parent());" value="<s:property value="loader.yiStreet[#counter]" />" />
					</td>
					<td class="labelLeft">
						<label for="selectYIVASelectLanguage"><s:property value="getText('label.ai.tabs.commons.selectLanguage')"/>:</label>
					</td>
					<td>
						<select id="selectYIVASelectLanguage" onchange="streetOfInstitutionLanguageChanged($(this).parent().parent().parent().parent());" >
							<s:iterator value="languageList" var="language"> 
								<option value="<s:property value="#language.key" />"
									<s:if test="%{#language.key == loader.yiStreetLang[#counter]}" > selected=selected </s:if>>
									<s:property value="#language.value" />
								</option>
							</s:iterator>
						</select>
					</td>
				</tr>
	
				<tr>
					<td>
						<s:if test="%{#status.index == 0}">
							<label for="textYICity"><s:property value="getText('label.ai.tabs.commons.cityTownWithPostalcode')"/><span class="required">*</span>:</label>
						</s:if>
						<s:else>
							<label for="textYICity"><s:property value="getText('label.ai.tabs.commons.cityTownWithPostalcode')"/>:</label>
						</s:else>
					</td>
					<td>
						<input type="text" id="textYICity" onchange="cityOfInstitutionChanged($(this).parent().parent().parent().parent());" value="<s:property value="loader.yiMunicipalityPostalcode[#counter]" />" />
					</td>
					<td colspan="2">
					</td>
				</tr>

				<tr>
					<td>
						<s:if test="%{#status.index == 0}">
							<label for="textYICountry"><s:property value="getText('label.ai.tabs.commons.country')"/><span class="required">*</span>:</label>
						</s:if>
						<s:else>
							<label for="textYICountry"><s:property value="getText('label.ai.tabs.commons.country')"/>:</label>
						</s:else>
					</td>
					<td>
						<input type="text" id="textYICountry" onchange="countryOfInstitutionChanged($(this).parent().parent().parent().parent());" value="<s:property value="loader.yiCountry[#counter]" />" />
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
						<label for="textYILatitude"><s:property value="getText('label.ai.tabs.commons.latitude')"/></label>
					</td>
					<td>
						<input type="text" id="textYILatitude" onchange="latitudeOfInstitutionChanged($(this).parent().parent().parent().parent());" value="<s:property value="loader.yiLatitude[#counter]" />" />
					</td>
					<td class="labelLeft">
						<label for="textYILongitude"><s:property value="getText('label.ai.tabs.commons.longitude')"/></label>
					</td>
					<td>
						<input type="text" id="textYILongitude" onchange="longitudeOfInstitutionChanged($(this).parent().parent().parent().parent());" value="<s:property value="loader.yiLongitude[#counter]" />" />
					</td>
				</tr>
			</table>
			<s:set var="counter" value="%{#counter + 1}"/>
		</s:iterator>
	</s:if>
	<s:else>
		<table id="yiTableVisitorsAddress_1" class="tablePadding">
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
					<input type="text" id="textYIStreet" onchange="streetOfInstitutionChanged($(this).parent().parent().parent().parent());" />
				</td>
				<td class="labelLeft">
					<label for="selectYIVASelectLanguage"><s:property value="getText('label.ai.tabs.commons.selectLanguage')"/>:</label>
				</td>
				<td>
					<select id="selectYIVASelectLanguage" onchange="streetOfInstitutionLanguageChanged($(this).parent().parent().parent().parent());" >
						<s:iterator value="languageList" var="language"> 
							<option value="<s:property value="#language.key" />"><s:property value="#language.value" /></option>
						</s:iterator>
					</select>
				</td>
			</tr>

			<tr>
				<td>
					<label for="textYICity"><s:property value="getText('label.ai.tabs.commons.cityTownWithPostalcode')"/><span class="required">*</span>:</label>
				</td>
				<td>
					<input type="text" id="textYICity" onchange="cityOfInstitutionChanged($(this).parent().parent().parent().parent());" />
				</td>
				<td colspan="2">
				</td>
			</tr>

			<tr>
				<td>
					<label for="textYICountry"><s:property value="getText('label.ai.tabs.commons.country')"/><span class="required">*</span>:</label>
				</td>
				<td>
					<input type="text" id="textYICountry" onchange="countryOfInstitutionChanged($(this).parent().parent().parent().parent());" />
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
					<label for="textYILatitude"><s:property value="getText('label.ai.tabs.commons.latitude')"/></label>
				</td>
				<td>
					<input type="text" id="textYILatitude" onchange="latitudeOfInstitutionChanged($(this).parent().parent().parent().parent());" />
				</td>
				<td class="labelLeft">
					<label for="textYILongitude"><s:property value="getText('label.ai.tabs.commons.longitude')"/></label>
				</td>
				<td>
					<input type="text" id="textYILongitude" onchange="longitudeOfInstitutionChanged($(this).parent().parent().parent().parent());" />
				</td>
			</tr>
		</table>
	</s:else>

	<table id="yiTableButtonAddVisitorsAddress" class="tablePadding">
		<tr>
			<td colspan="2">
				<input type="button" id="buttonAddVisitorsAddressTranslation" value="<s:property value="getText('label.ai.tabs.commons.addFurtherVisitorsAddress')"/>" onclick="yiAddVisitorsAddressTranslation('<s:property value="getText('label.ai.tabs.commons.pleaseFillData')" />');" />
			</td>
			<td colspan="2">
			</td>
		</tr>
	</table>

	<s:if test="%{loader.yiNumberOfPostalAddress.size() > 0}">
		<s:set var="counter" value="0"/>
		<s:iterator var="current" value="loader.yiNumberOfPostalAddress" status="status">
			<table id="yiTablePostalAddress_<s:property value="%{#status.index + 1}" />" class="tablePadding">
				<tr id="yiPostalAddressLabel">
					<td id="postalAddressLabel" colspan="4">
						<s:property value="getText('label.ai.yourinstitution.postalAddress')" />
					</td>
				</tr>
	
				<tr id="yiPostalAddressStreet">
					<td>
						<s:if test="%{#status.index == 0}">
							<label for="textYIPAStreet"><s:property value="getText('label.ai.tabs.commons.street')" /><span class="required">*</span>:</label>
						</s:if>
						<s:else>
							<label for="textYIPAStreet"><s:property value="getText('label.ai.tabs.commons.street')" />:</label>
						</s:else>
					</td>
					<td>
						<input type="text" id="textYIPAStreet" onchange="postalAddressStreetChanged($(this).parent().parent().parent().parent());" value="<s:property value="loader.yiStreetPostal[#counter]" />" />
					</td>
					<td id="yiPostalAddressLanguage" class="labelLeft">
						<label for="selectYIPASelectLanguage"><s:property value="getText('label.ai.tabs.commons.selectLanguage')" />:</label>
					</td>
					<td>
						<select id="selectYIPASelectLanguage" onchange="postalAddressLanguageChanged($(this).parent().parent().parent().parent());">
							<s:iterator value="languageList" var="language"> 
								<option value="<s:property value="#language.key" />"
									<s:if test="%{#language.key == loader.yiStreetPostalLang[#counter]}" > selected=selected </s:if>>
									<s:property value="#language.value" />
								</option>
							</s:iterator>	
						</select>
					</td>
				</tr>
	
				<tr id="yiPostalAddressCity">
					<td>
						<s:if test="%{#status.index == 0}">
							<label for="textYIPACity"><s:property value="getText('label.ai.tabs.commons.cityTownWithPostalcode')" /><span class="required">*</span>:</label>
						</s:if>
						<s:else>
							<label for="textYIPACity"><s:property value="getText('label.ai.tabs.commons.cityTownWithPostalcode')" />:</label>
						</s:else>
					</td>
					<td>
						<input type="text" id="textYIPACity" onchange="postalAddressCityChanged($(this).parent().parent().parent().parent());" value="<s:property value="loader.yiMunicipalityPostalcodePostal[#counter]" />" />
					</td>
					<td colspan="2">
					</td>
				</tr>
			</table>
			<s:set var="counter" value="%{#counter + 1}"/>
		</s:iterator>
	</s:if>

	<table id="yiTableOthers" class="tablePadding">
		<tr>
			<td colspan="4">
				<s:if test="%{loader.yiNumberOfPostalAddress.size() > 0}">
					<input type="button" id="buttonAddPostalAddressIfDifferent" value="<s:property value="getText('label.ai.tabs.commons.addPostalAddressIfDifferent')"/>" onclick="yiAddPostalAddressIfDifferent('<s:property value="getText('label.ai.yourinstitution.postalAddress')" />', '<s:property value="getText('label.ai.tabs.commons.selectLanguage')" />', '<s:property value="getText('label.ai.tabs.commons.street')" />', '<s:property value="getText('label.ai.tabs.commons.cityTownWithPostalcode')" />')" style="display:none;" />
				</s:if>
				<s:else>
					<input type="button" id="buttonAddPostalAddressIfDifferent" value="<s:property value="getText('label.ai.tabs.commons.addPostalAddressIfDifferent')"/>" onclick="yiAddPostalAddressIfDifferent('<s:property value="getText('label.ai.yourinstitution.postalAddress')" />', '<s:property value="getText('label.ai.tabs.commons.selectLanguage')" />', '<s:property value="getText('label.ai.tabs.commons.street')" />', '<s:property value="getText('label.ai.tabs.commons.cityTownWithPostalcode')" />')" />
				</s:else>
			</td>
		</tr>

		<s:if test="%{loader.yiNumberOfPostalAddress.size() > 0}">
			<tr id="yiPostalAddressTranslation">
		</s:if>
		<s:else>
			<tr id="yiPostalAddressTranslation" style="display:none;">
		</s:else>
			<td colspan="2">
				<input type="button" id="buttonAddPostalAddressTranslation" value="<s:property value="getText('label.ai.yourinstitution.addPostalAddressTranslation')"/>" onclick="yiAddPostalAddressTranslation('<s:property value="getText('label.ai.tabs.commons.pleaseFillData')" />');" />
			</td>
			<td colspan="2">
			</td>
		</tr>

		<tr>
			<td>
				<label for="selectYIContinent" ><s:property value="getText('label.ai.tabs.commons.continent')"/><span class="required">*</span>:</label>
			</td>
			<td>
				<select id="selectYIContinent" onchange="continentOfInstitutionChanged();" >
					<s:iterator value="continentOfTheInstitutionList" var="continent"> 
						<option value="<s:property value="#continent.key" />"<s:if test="%{#continent.key == loader.geogarea}" > selected=selected </s:if>><s:property value="#continent.value" /></option>
					</s:iterator>
				</select>
			</td>
		</tr>

		<tr>
			<td>
				<label for="textYITelephone" ><s:property value="getText('label.ai.tabs.commons.telephone')"/>:</label>
			</td>
			<td>
				<input type="text" id="textYITelephone" onchange="telephoneOfInstitutionChanged($(this));" value="${loader.telephone}" />
			</td>
		</tr>

		<s:if test="%{loader.yiNumberOfEmailAddress.size() > 0}">
			<s:set var="counter" value="0"/>
			<s:iterator var="current" value="loader.yiNumberOfEmailAddress" status="status">
				<s:if test="%{#status.index == 0}">
					<tr id="trTextYIEmail">
				</s:if>
				<s:else>
					<tr id="trTextYIEmail_<s:property value="%{#status.index + 1}" />">
				</s:else>
					<td>
						<s:if test="%{#status.index == 0}">
							<label for="textYIEmailAddress" ><s:property value="getText('label.ai.tabs.commons.emailAddress')"/>:</label>
						</s:if>
						<s:else>
							<label for="textYIEmailAddress_<s:property value="%{#status.index + 1}" />" ><s:property value="getText('label.ai.tabs.commons.emailAddress')"/>:</label>
						</s:else>
					</td>
					<td>
						<s:if test="%{#status.index == 0}">
							<input type="text" id="textYIEmailAddress" onchange="emailOfInstitutionChanged($(this));" value="<s:property value="loader.yiEmail[#counter]" />" />
						</s:if>
						<s:else>
							<input type="text" id="textYIEmailAddress_<s:property value="%{#status.index + 1}" />" onchange="emailOfInstitutionChanged($(this));" value="<s:property value="loader.yiEmail[#counter]" />" />
						</s:else>
					</td>
					<td class="labelLeft">
						<s:if test="%{#status.index == 0}">
							<label for="selectTextYILangEmail"><s:property value="getText('label.ai.tabs.commons.selectLanguage')"/>:</label>
						</s:if>
						<s:else>
							<label for="selectTextYILangEmail_<s:property value="%{#status.index + 1}" />"><s:property value="getText('label.ai.tabs.commons.selectLanguage')"/>:</label>
						</s:else>
					</td>
					<td>
						<s:if test="%{#status.index == 0}">
							<select id="selectTextYILangEmail">
						</s:if>
						<s:else>
							<select id="selectTextYILangEmail_<s:property value="%{#status.index + 1}" />" >
						</s:else>
							<s:iterator value="languageList" var="language"> 
								<option value="<s:property value="#language.key" />"
									<s:if test="%{#language.key == loader.yiEmailLang[#counter]}" > selected=selected </s:if>>
									<s:property value="#language.value" />
								</option>
							</s:iterator>
						</select>
					</td>
				</tr>

				<s:if test="%{#status.index == 0}">
					<tr id="trTextYILangEmail">
				</s:if>
				<s:else>
					<tr id="trTextYILangEmail_<s:property value="%{#status.index + 1}" />">
				</s:else>
					<td>
						<s:if test="%{#status.index == 0}">
							<label for="textYIEmailLinkTitle"><s:property value="getText('label.ai.tabs.commons.linkTitle')"/>:</label>
						</s:if>
						<s:else>
							<label for="textYIEmailLinkTitle_<s:property value="%{#status.index + 1}" />"><s:property value="getText('label.ai.tabs.commons.linkTitle')"/>:</label>
						</s:else>
					</td>
					<td>
						<s:if test="%{#status.index == 0}">
							<input type="text" id="textYIEmailLinkTitle" onchange="emailOfInstitutionLinkChanged($(this));" value="<s:property value="loader.yiEmailTitle[#counter]" />" />
						</s:if>
						<s:else>
							<input type="text" id="textYIEmailLinkTitle_<s:property value="%{#status.index + 1}" />" onchange="emailOfInstitutionLinkChanged($(this));" value="<s:property value="loader.yiEmailTitle[#counter]" />" />
						</s:else>
					</td>
					<td colspan="2">
					</td>
				</tr>
				<s:set var="counter" value="%{#counter + 1}"/>
			</s:iterator>

			<tr id="trButtonAFEOTInstitution">
				<td colspan="2">
					<input id="buttonAddFurtherEmailsOfTheInstitution" type="button" value="<s:property value='getText("label.ai.contact.addFurtherEmailsAddresses")' />" onclick="addYIFurtherEmailsOfTheInstitution('<s:property value="getText('label.ai.tabs.commons.pleaseFillData')" />');" />
				</td>
				<td colspan="2">
				</td>
			</tr>
		</s:if>
		<s:else>
			<tr id="trTextYIEmail">
				<td>
					<label for="textYIEmailAddress" ><s:property value="getText('label.ai.tabs.commons.emailAddress')"/>:</label>
				</td>
				<td>
					<input type="text" id="textYIEmailAddress" onchange="emailOfInstitutionChanged($(this));" />
				</td>
				<td class="labelLeft">
					<label for="selectTextYILangEmail"><s:property value="getText('label.ai.tabs.commons.selectLanguage')"/>:</label>
				</td>
				<td>
					<select id="selectTextYILangEmail" >
						<s:iterator value="languageList" var="language"> 
							<option value="<s:property value="#language.key" />"><s:property value="#language.value" /></option>
						</s:iterator>
					</select>
				</td>
			</tr>

			<tr id="trTextYILangEmail">
				<td>
					<label for="textYIEmailLinkTitle" ><s:property value="getText('label.ai.tabs.commons.linkTitle')"/>:</label>
				</td>
				<td>
					<input type="text" id="textYIEmailLinkTitle" onchange="emailOfInstitutionLinkChanged($(this));" />
				</td>
			</tr>

			<tr id="trButtonAFEOTInstitution">
				<td colspan="2">
					<input id="buttonAddFurtherEmailsOfTheInstitution" type="button" value="<s:property value='getText("label.ai.contact.addFurtherEmailsAddresses")' />" onclick="addYIFurtherEmailsOfTheInstitution('<s:property value="getText('label.ai.tabs.commons.pleaseFillData')" />');" />
				</td>
				<td colspan="2">
				</td>
			</tr>
		</s:else>

		<s:if test="%{loader.yiNumberOfWebpageAddress.size() > 0}">
			<s:set var="counter" value="0"/>
			<s:iterator var="current" value="loader.yiNumberOfWebpageAddress" status="status">
				<s:if test="%{#status.index == 0}">
					<tr id="trButtonYIWebpage">
				</s:if>
				<s:else>
					<tr id="trButtonYIWebpage_<s:property value="%{#status.index + 1}" />">
				</s:else>
					<td>
						<s:if test="%{#status.index == 0}">
							<label for="textYIWebpage" ><s:property value="getText('label.ai.tabs.commons.webpage')"/>:</label>
						</s:if>
						<s:else>
							<label for="textYIWebpage_<s:property value="%{#status.index + 1}" />" ><s:property value="getText('label.ai.tabs.commons.webpage')"/>:</label>
						</s:else>
					</td>
					<td>
						<s:if test="%{#status.index == 0}">
							<input type="text" id="textYIWebpage" onchange="webOfInstitutionChanged($(this));" value="<s:property value="loader.yiWebpage[#counter]" />" />
						</s:if>
						<s:else>
							<input type="text" id="textYIWebpage_<s:property value="%{#status.index + 1}" />" onchange="webOfInstitutionChanged($(this));" value="<s:property value="loader.yiWebpage[#counter]" />" />
						</s:else>
					</td>
					<td class="labelLeft">
						<s:if test="%{#status.index == 0}">
							<label for="selectTextYILangWebpage"><s:property value="getText('label.ai.tabs.commons.selectLanguage')"/>:</label>
						</s:if>
						<s:else>
							<label for="selectTextYILangWebpage_<s:property value="%{#status.index + 1}" />"><s:property value="getText('label.ai.tabs.commons.selectLanguage')"/>:</label>
						</s:else>
					</td>
					<td>
						<s:if test="%{#status.index == 0}">
							<select id="selectTextYILangWebpage">
						</s:if>
						<s:else>
							<select id="selectTextYILangWebpage_<s:property value="%{#status.index + 1}" />" >
						</s:else>
							<s:iterator value="languageList" var="language"> 
								<option value="<s:property value="#language.key" />"
									<s:if test="%{#language.key == loader.yiWebpageLang[#counter]}" > selected=selected </s:if>>
									<s:property value="#language.value" />
								</option>
							</s:iterator>
						</select>
					</td>
				</tr>

				<s:if test="%{#status.index == 0}">
					<tr id="trButtonYILangWebpage">
				</s:if>
				<s:else>
					<tr id="trButtonYILangWebpage_<s:property value="%{#status.index + 1}" />">
				</s:else>
					<td>
						<s:if test="%{#status.index == 0}">
							<label for="textYIWebpageLinkTitle"><s:property value="getText('label.ai.tabs.commons.linkTitle')"/>:</label>
						</s:if>
						<s:else>
							<label for="textYIWebpageLinkTitle_<s:property value="%{#status.index + 1}" />"><s:property value="getText('label.ai.tabs.commons.linkTitle')"/>:</label>
						</s:else>
					</td>
					<td>
						<s:if test="%{#status.index == 0}">
							<input type="text" id="textYIWebpageLinkTitle" onchange="webOfInstitutionLinkChanged($(this));" value="<s:property value="loader.yiWebpageTitle[#counter]" />" />
						</s:if>
						<s:else>
							<input type="text" id="textYIWebpageLinkTitle_<s:property value="%{#status.index + 1}" />" onchange="webOfInstitutionLinkChanged($(this));" value="<s:property value="loader.yiWebpageTitle[#counter]" />" />
						</s:else>
					</td>
					<td colspan="2">
					</td>
				</tr>
				<s:set var="counter" value="%{#counter + 1}"/>
			</s:iterator>

			<tr id="trButtonAFWOTInstitution">
				<td id="tdAddFurtherWebsOfTheInstitution" colspan="2">
					<input id="buttonAddFurtherWebsOfTheInstitution" type="button" value="<s:property value='getText("label.ai.contact.addFurtherWebpages")' />" onclick="addYIFurtherWebsOfTheInstitution('<s:property value="getText('label.ai.tabs.commons.pleaseFillData')" />');" />
				</td>
			</tr>
		</s:if>
		<s:else>
			<tr id="trButtonYIWebpage">
				<td>
					<label for="textYIWebpage" ><s:property value="getText('label.ai.tabs.commons.webpage')"/>:</label>
				</td>
				<td>
					<input type="text" id="textYIWebpage" onchange="webOfInstitutionChanged($(this));" />
				</td>
				<td class="labelLeft">
					<label for="selectTextYILangWebpage"><s:property value="getText('label.ai.tabs.commons.selectLanguage')"/>:</label>
				</td>
				<td>
					<select id="selectTextYILangWebpage" >
						<s:iterator value="languageList" var="language"> 
							<option value="<s:property value="#language.key" />"><s:property value="#language.value" /></option>
						</s:iterator>
					</select>
				</td>
			</tr>

			<tr id="trButtonYILangWebpage">
				<td>
					<label for="textYIWebpageLinkTitle" ><s:property value="getText('label.ai.tabs.commons.linkTitle')"/>:</label>
				</td>
				<td>
					<input type="text" id="textYIWebpageLinkTitle" onchange="webOfInstitutionLinkChanged($(this));" />
				</td>
			</tr>

			<tr id="trButtonAFWOTInstitution">
				<td id="tdAddFurtherWebsOfTheInstitution" colspan="2">
					<input id="buttonAddFurtherWebsOfTheInstitution" type="button" value="<s:property value='getText("label.ai.contact.addFurtherWebpages")' />" onclick="addYIFurtherWebsOfTheInstitution('<s:property value="getText('label.ai.tabs.commons.pleaseFillData')" />');" />
				</td>
			</tr>
		</s:else>

		<s:if test="%{loader.yiOpening.size() > 0}">
			<s:set var="counter" value="0"/>
			<s:iterator var="current" value="loader.yiOpening" status="status">
				<s:if test="%{#status.index == 0}">
					<tr id="trTextYIOpeningTimes">
				</s:if>
				<s:else>
					<tr id="trTextYIOpeningTimes_<s:property value="%{#status.index + 1}" />">
				</s:else>
					<td>
						<s:if test="%{#status.index == 0}">
							<label for="textYIOpeningTimes" ><s:property value="getText('label.ai.tabs.commons.openingTimes')"/><span class="required">*</span>:</label>
						</s:if>
						<s:else>
							<label for="textYIOpeningTimes_<s:property value="%{#status.index + 1}" />" ><s:property value="getText('label.ai.tabs.commons.openingTimes')"/>:</label>
						</s:else>
					</td>
					<td>
						<s:if test="%{#status.index == 0}">
							<input type="text" id="textYIOpeningTimes" onchange="openingHoursOfInstitutionChanged($(this));" value="<s:property value="#current" />" />
						</s:if>
						<s:else>
							<input type="text" id="textYIOpeningTimes_<s:property value="%{#status.index + 1}" />" onchange="openingHoursOfInstitutionChanged($(this));" value="<s:property value="#current" />" />
						</s:else>
					</td>
					<td class="labelLeft">
						<s:if test="%{#status.index == 0}">
							<label for="selectTextYIOpeningTimes"><s:property value="getText('label.ai.tabs.commons.selectLanguage')"/>:</label>
						</s:if>
						<s:else>
							<label for="selectTextYIOpeningTimes_<s:property value="%{#status.index + 1}" />"><s:property value="getText('label.ai.tabs.commons.selectLanguage')"/>:</label>
						</s:else>
					</td>
					<td>
						<s:if test="%{#status.index == 0}">
							<select id="selectTextYIOpeningTimes" onchange="duplicateOpeningTimesLanguage($(this));">
						</s:if>
						<s:else>
							<select id="selectTextYIOpeningTimes_<s:property value="%{#status.index + 1}" />" onchange="duplicateOpeningTimesLanguage($(this));">
						</s:else>
							<s:iterator value="languageList" var="language"> 
								<option value="<s:property value="#language.key" />"
									<s:if test="%{#language.key == loader.yiOpeningLang[#counter]}" > selected=selected </s:if>>
									<s:property value="#language.value" />
								</option>
							</s:iterator>
						</select>
					</td>
				</tr>
				<s:set var="counter" value="%{#counter + 1}"/>
			</s:iterator>
		</s:if>
		<s:else>
			<tr id="trTextYIOpeningTimes">
				<td>
					<label for="textYIOpeningTimes" ><s:property value="getText('label.ai.tabs.commons.openingTimes')"/><span class="required">*</span>:</label>
				</td>
				<td>
					<input type="text" id="textYIOpeningTimes" onchange="openingHoursOfInstitutionChanged($(this));" value="${loader.opening}" />
				</td>
				<td class="labelLeft">
					<label for="selectTextYIOpeningTimes"><s:property value="getText('label.ai.tabs.commons.selectLanguage')"/>:</label>
				</td>
				<td>
					<select id="selectTextYIOpeningTimes" onchange="duplicateOpeningTimesLanguage($(this));" >
						<s:iterator value="languageList" var="language"> 
							<option value="<s:property value="#language.key" />"<s:if test="%{#language.key == loader.openingLang}" > selected=selected </s:if>><s:property value="#language.value" /></option>
						</s:iterator>
					</select>
				</td>
			</tr>
		</s:else>

		<tr>
			<td id="tdASAddOpeningTimes" colspan="2">
				<input type="button" id="buttonASAddOpeningTimes"  value="<s:property value='getText("label.ai.accessAndServices.addOpeningTimes")' />" onclick="yIAddOpeningTimes('<s:property value="getText('label.ai.tabs.commons.pleaseFillData')" />');" />
			</td>
		</tr>

		<s:if test="%{loader.yiClosing.size() > 0}">
			<s:set var="counter" value="0"/>
			<s:iterator var="current" value="loader.yiClosing" status="status">
				<s:if test="%{#status.index == 0}">
					<tr id="fieldClosingDates">
				</s:if>
				<s:else>
					<tr id="fieldClosingDates_<s:property value="%{#status.index + 1}" />">
				</s:else>
					<td>
						<s:if test="%{#status.index == 0}">
							<label for="yourInstitutionClosingDates" ><s:property value="getText('label.ai.tabs.commons.closingDates')"/>:</label>
						</s:if>
						<s:else>
							<label for="yourInstitutionClosingDates_<s:property value="%{#status.index + 1}" />" ><s:property value="getText('label.ai.tabs.commons.closingDates')"/>:</label>
						</s:else>
					</td>
					<td>
						<s:if test="%{#status.index == 0}">
							<input type="text" id="yourInstitutionClosingDates" onchange="closingHoursOfInstitutionChanged($(this));" value="<s:property value="#current" />" />
						</s:if>
						<s:else>
							<input type="text" id="yourInstitutionClosingDates_<s:property value="%{#status.index + 1}" />" onchange="closingHoursOfInstitutionChanged($(this));" value="<s:property value="#current" />" />
						</s:else>
					</td>
					<td class="labelLeft">
						<s:if test="%{#status.index == 0}">
							<label for="selectTextYIClosingTimes"><s:property value="getText('label.ai.tabs.commons.selectLanguage')"/>:</label>
						</s:if>
						<s:else>
							<label for="selectTextYIClosingTimes_<s:property value="%{#status.index + 1}" />"><s:property value="getText('label.ai.tabs.commons.selectLanguage')"/>:</label>
						</s:else>
					</td>
					<td>
						<s:if test="%{#status.index == 0}">
							<select id="selectTextYIClosingTimes" onchange="duplicateClosingTimesLanguage($(this));" >
						</s:if>
						<s:else>
							<select id="selectTextYIClosingTimes_<s:property value="%{#status.index + 1}" />" onchange="duplicateClosingTimesLanguage($(this));">
						</s:else>
							<s:iterator value="languageList" var="language"> 
								<option value="<s:property value="#language.key" />"
									<s:if test="%{#language.key == loader.yiClosingLang[#counter]}" > selected=selected </s:if>>
									<s:property value="#language.value" />
								</option>
							</s:iterator>
						</select>
					</td>
				</tr>
				<s:set var="counter" value="%{#counter + 1}"/>
			</s:iterator>

			<tr id="trYIbuttonAddClosingDates">
				<td id="tdASAddClosingDates" colspan="2">
					<input type="button" id="buttonASAddClosingDates"  value="<s:property value='getText("label.ai.accessAndServices.addClosingDates")' />" onclick="yIAddClosingDates2('<s:property value="getText('label.ai.tabs.commons.pleaseFillData')" />');" />
				</td>
				<td colspan="2">
				</td>
			</tr>
		</s:if>
		<s:else>
			<tr id="fieldClosingDates">
				<td>
					<label for="yourInstitutionClosingDates"><s:property value="getText('label.ai.yourinstitution.closingDates')"/>:</label>
				</td>
				<td>
					<input type="text" id="yourInstitutionClosingDates" onchange="closingHoursOfInstitutionChanged($(this));" />
				</td>
				<td class="labelLeft">
					<label for="selectTextYIClosingTimes"><s:property value="getText('label.ai.tabs.commons.selectLanguage')"/>:</label>
				</td>
				<td>
					<select id="selectTextYIClosingTimes" onchange="duplicateClosingTimesLanguage($(this));">
						<s:iterator value="languageList" var="language"> 
							<option value="<s:property value="#language.key" />"><s:property value="#language.value" /></option>
						</s:iterator>
					</select>
				</td>
			</tr>

			<tr id="trYIbuttonAddClosingDates">
				<td id="tdASAddClosingDates" colspan="2">
					<input type="button" id="buttonASAddClosingDates"  value="<s:property value='getText("label.ai.accessAndServices.addClosingDates")' />" onclick="yIAddClosingDates2('<s:property value="getText('label.ai.tabs.commons.pleaseFillData')" />');" />
				</td>
				<td colspan="2">
				</td>
			</tr>
		</s:else>

		<s:if test="%{loader.yiRestaccess.size() > 0}">
			<tr>
				<td>
					<label for="selectAccessibleToThePublic" ><s:property value="getText('label.ai.yourinstitution.accessibleToThePublic')"/><span class="required">*</span>:</label>
				</td>
				<td>
					<select id="selectAccessibleToThePublic" onchange="accessibleToThePublicChanged();" >
						<s:iterator value="yesNoList" var="yesno"> 
							<option value="<s:property value="#yesno.key" />"<s:if test="%{#yesno.key == loader.accessQuestion}" > selected=selected </s:if>><s:property value="#yesno.value" /></option>
						</s:iterator>
					</select>
				</td>
				<td colspan="2">
				</td>
			</tr>

			<s:set var="counter" value="0"/>
			<s:iterator var="current" value="loader.yiRestaccess" status="status">
				<s:if test="%{#status.index == 0}">
					<tr id="trYIButtonFutherAccessInformation">
				</s:if>
				<s:else>
					<tr id="trYIButtonFutherAccessInformation_<s:property value="%{#status.index + 1}" />">
				</s:else>
					<td>
						<s:if test="%{#status.index == 0}">
							<label for="futherAccessInformation" ><s:property value="getText('label.ai.accessAndServices.accessRestrictions')"/><span class="required">*</span>:</label>
						</s:if>
						<s:else>
							<label for="futherAccessInformation_<s:property value="%{#status.index + 1}" />" ><s:property value="getText('label.ai.accessAndServices.accessRestrictions')"/>:</label>
						</s:else>
					</td>
					<td>
						<s:if test="%{#status.index == 0}">
							<input type="text" id="futherAccessInformation" onchange="futherAccessInformationChanged($(this));" value="<s:property value="#current" />" />
						</s:if>
						<s:else>
							<input type="text" id="futherAccessInformation_<s:property value="%{#status.index + 1}" />" onchange="futherAccessInformationChanged($(this));" value="<s:property value="#current" />" />
						</s:else>
					</td>
					<td class="labelLeft">
						<s:if test="%{#status.index == 0}">
							<label for="selectFutherAccessInformation"><s:property value="getText('label.ai.tabs.commons.selectLanguage')"/>:</label>
						</s:if>
						<s:else>
							<label for="selectFutherAccessInformation_<s:property value="%{#status.index + 1}" />"><s:property value="getText('label.ai.tabs.commons.selectLanguage')"/>:</label>
						</s:else>
					</td>
					<td>
						<s:if test="%{#status.index == 0}">
							<select id="selectFutherAccessInformation" onchange="duplicateAccessInformation($(this));" >
						</s:if>
						<s:else>
							<select id="selectFutherAccessInformation_<s:property value="%{#status.index + 1}" />" onchange="duplicateAccessInformation($(this));" >
						</s:else>
							<s:iterator value="languageList" var="language"> 
								<option value="<s:property value="#language.key" />"
									<s:if test="%{#language.key == loader.yiRestaccessLang[#counter]}" > selected=selected </s:if>>
									<s:property value="#language.value" />
								</option>
							</s:iterator>
						</select>
					</td>
				</tr>
				<s:set var="counter" value="%{#counter + 1}"/>
			</s:iterator>

			<tr>
				<td colspan="2">
				</td>
				<td colspan="2">
					<input type="button" id="buttonFutherAccessInformation2" value="<s:property value="getText('label.ai.yourinstitution.addFurtherAccessInformation')"/>" onclick="yiFutherAccessInformation2('<s:property value="getText('label.ai.tabs.commons.pleaseFillData')" />');" />
				</td>
			</tr>
		</s:if>
		<s:else>
			<tr>
				<td>
					<label for="selectAccessibleToThePublic" ><s:property value="getText('label.ai.yourinstitution.accessibleToThePublic')"/><span class="required">*</span>:</label>
				</td>
				<td>
					<select id="selectAccessibleToThePublic" onchange="accessibleToThePublicChanged();" >
						<s:iterator value="yesNoList" var="yesno"> 
							<option value="<s:property value="#yesno.key" />"<s:if test="%{#yesno.key == loader.accessQuestion}" > selected=selected </s:if>><s:property value="#yesno.value" /></option>
						</s:iterator>
					</select>
				</td>
				<td colspan="2" class="labelLeft">
					<input type="button" id="buttonFutherAccessInformation" value="<s:property value="getText('label.ai.yourinstitution.addFurtherAccessInformation')"/>" onclick="yiFutherAccessInformation();" />
				</td>
			</tr>

			<tr id="trYIButtonFutherAccessInformation" style="display:none;">
				<td>
					<label for="futherAccessInformation"><s:property value="getText('label.ai.accessAndServices.accessRestrictions')" />:</label>
				</td>
				<td>
					<input type="text" id="futherAccessInformation" onchange="futherAccessInformationChanged($(this));"/>
				</td>
				<td class="labelLeft">
					<label for="selectFutherAccessInformation"><s:property value="getText('label.ai.tabs.commons.selectLanguage')"/>:</label>
				</td>
				<td>
					<select id="selectFutherAccessInformation" onchange="duplicateAccessInformation($(this));">
						<s:iterator value="languageList" var="language">
							<option value="<s:property value="#language.key" />"><s:property value="#language.value" /></option>
						</s:iterator>
					</select>
				</td>
			</tr>
			
			<tr>
				<td colspan="2">
				</td>
				<td colspan="2" class="labelLeft">
					<input type="button" id="buttonFutherAccessInformation2" style="display:none;" value="<s:property value="getText('label.ai.yourinstitution.addFurtherAccessInformation')"/>" onclick="yiFutherAccessInformation2('<s:property value="getText('label.ai.tabs.commons.pleaseFillData')" />');" />
				</td>
			</tr>
		</s:else>

		<s:if test="%{loader.yiAccessibility.size() > 0}">
			<tr>
				<td>
					<label for="selectFacilitiesForDisabledPeopleAvailable" ><s:property value="getText('label.ai.yourinstitution.facilitiesForFisabledPeopleAvailable')"/><span class="required">*</span>:</label>
				</td>
				<td>
					<select id="selectFacilitiesForDisabledPeopleAvailable" onchange="facilitiesForDisabledPeopleAvailableChanged();" >
						<s:iterator value="yesNoList" var="yesno"> 
							<option value="<s:property value="#yesno.key" />"<s:if test="%{#yesno.key == loader.accessibilityQuestion}" > selected=selected </s:if>><s:property value="#yesno.value" /></option>
						</s:iterator>
					</select>
				</td>
				<td colspan="2">
				</td>
			</tr>

			<s:set var="counter" value="0"/>
			<s:iterator var="current" value="loader.yiAccessibility" status="status">
				<s:if test="%{#status.index == 0}">
					<tr id="trButtonAddFutherInformationOnExistingFacilities">
				</s:if>
				<s:else>
					<tr id="trButtonAddFutherInformationOnExistingFacilities_<s:property value="%{#status.index + 1}" />">
				</s:else>
					<td>
						<s:if test="%{#status.index == 0}">
							<label for="futherInformationOnExistingFacilities" ><s:property value="getText('label.ai.accessAndServices.accesibility')"/>:</label>
						</s:if>
						<s:else>
							<label for="futherInformationOnExistingFacilities_<s:property value="%{#status.index + 1}" />" ><s:property value="getText('label.ai.accessAndServices.accesibility')"/>:</label>
						</s:else>
					</td>
					<td>
						<s:if test="%{#status.index == 0}">
							<input type="text" id="futherInformationOnExistingFacilities" onchange="futherInformationOnExistingFacilitiesChanged($(this));" value="<s:property value="#current" />" />
						</s:if>
						<s:else>
							<input type="text" id="futherInformationOnExistingFacilities_<s:property value="%{#status.index + 1}" />" onchange="futherInformationOnExistingFacilitiesChanged($(this));" value="<s:property value="#current" />" />
						</s:else>
					</td>
					<td class="labelLeft">
						<s:if test="%{#status.index == 0}">
							<label for="selectFutherAccessInformationOnExistingFacilities"><s:property value="getText('label.ai.tabs.commons.selectLanguage')"/>:</label>
						</s:if>
						<s:else>
							<label for="selectFutherAccessInformationOnExistingFacilities_<s:property value="%{#status.index + 1}" />"><s:property value="getText('label.ai.tabs.commons.selectLanguage')"/>:</label>
						</s:else>
					</td>
					<td>
						<s:if test="%{#status.index == 0}">
							<select id="selectFutherAccessInformationOnExistingFacilities" onchange="duplicateFutherAccessInformationOnExistingFacilitiesLanguage($(this));" >
						</s:if>
						<s:else>
							<select id="selectFutherAccessInformationOnExistingFacilities_<s:property value="%{#status.index + 1}" />" onchange="duplicateFutherAccessInformationOnExistingFacilitiesLanguage($(this));">
						</s:else>
							<s:iterator value="languageList" var="language"> 
								<option value="<s:property value="#language.key" />"
									<s:if test="%{#language.key == loader.yiAccessibilityLang[#counter]}" > selected=selected </s:if>>
									<s:property value="#language.value" />
								</option>
							</s:iterator>
						</select>
					</td>
				</tr>
				<s:set var="counter" value="%{#counter + 1}"/>
			</s:iterator>

			<tr>
				<td colspan="2">
				</td>
				<td colspan="2" class="labelLeft">
					<input type="button" id="buttonAddFutherInformationOnExistingFacilities2" value="<s:property value="getText('label.ai.yourinstitution.addFutherInformationOnExistingFacilities')"/>" onclick="yiAddFutherInformationOnExistingFacilities2('<s:property value="getText('label.ai.tabs.commons.pleaseFillData')" />');" />
				</td>
			</tr>
		</s:if>
		<s:else>
			<tr>
				<td>
					<label for="selectFacilitiesForDisabledPeopleAvailable" ><s:property value="getText('label.ai.yourinstitution.facilitiesForFisabledPeopleAvailable')"/><span class="required">*</span>:</label>
				</td>
				<td>
					<select id="selectFacilitiesForDisabledPeopleAvailable" onchange="facilitiesForDisabledPeopleAvailableChanged();" >
						<s:iterator value="yesNoList" var="yesno"> 
							<option value="<s:property value="#yesno.key" />"<s:if test="%{#yesno.key == loader.accessibilityQuestion}" > selected=selected </s:if>><s:property value="#yesno.value" /></option>
						</s:iterator>
					</select>
				</td>
				<td colspan="2" class="labelLeft">
					<input type="button" id="buttonAddFutherInformationOnExistingFacilities" value="<s:property value="getText('label.ai.yourinstitution.addFutherInformationOnExistingFacilities')"/>" onclick="yiAddFutherInformationOnExistingFacilities();" />
				</td>
			</tr>

			<tr id="trButtonAddFutherInformationOnExistingFacilities" style="display:none;">
				<td>
					<label for="futherInformationOnExistingFacilities"><s:property value="getText('label.ai.accessAndServices.accesibility')" />:</label>
				</td>
				<td>
					<input type="text" id="futherInformationOnExistingFacilities" onchange="futherInformationOnExistingFacilitiesChanged($(this));" />		
				</td>
				<td class="labelLeft">
					<label for="selectFutherAccessInformationOnExistingFacilities"><s:property value="getText('label.ai.tabs.commons.selectLanguage')"/>:</label>
				</td>
				<td>
					<select id="selectFutherAccessInformationOnExistingFacilities" onchange="duplicateFutherAccessInformationOnExistingFacilitiesLanguage($(this));">
						<s:iterator value="languageList" var="language"> 
							<option value="<s:property value="#language.key" />"><s:property value="#language.value" /></option>
						</s:iterator>
					</select>
				</td>
			</tr>

			<tr>
				<td colspan="2"> </td>
				<td colspan="2" class="labelLeft">
					<input type="button" id="buttonAddFutherInformationOnExistingFacilities2" style="display:none;" value="<s:property value="getText('label.ai.yourinstitution.addFutherInformationOnExistingFacilities')"/>" onclick="yiAddFutherInformationOnExistingFacilities2('<s:property value="getText('label.ai.tabs.commons.pleaseFillData')" />');" />
				</td>
			</tr>
		</s:else>

		<s:if test="%{loader.yiResourceRelationHref.size() > 0}">
			<s:set var="counter" value="0"/>
			<s:iterator var="current" value="loader.yiResourceRelationHref" status="status">
				<s:if test="%{#status.index == 0}">
					<tr id="trYIReferencetoHoldingsguide">
				</s:if>
				<s:else>
					<tr id="trYIReferencetoHoldingsguide_<s:property value="%{#status.index + 1}" />">
				</s:else>
					<td>
						<s:if test="%{#status.index == 0}">
							<label for="textReferencetoyourinstitutionsholdingsguide" ><s:property value="getText('label.ai.yourinstitution.referenceToYourInstitutionsHoldingsGuide')"/>:</label>
						</s:if>
						<s:else>
							<label for="textReferencetoyourinstitutionsholdingsguide_<s:property value="%{#status.index + 1}" />" ><s:property value="getText('label.ai.yourinstitution.referenceToYourInstitutionsHoldingsGuide')"/>:</label>
						</s:else>
					</td>
					<td>
						<s:if test="%{#status.index == 0}">
							<input type="text" id="textReferencetoyourinstitutionsholdingsguide" onchange="linkToYourHolndingsGuideChanged();" value="<s:property value="#current" />" />
						</s:if>
						<s:else>
							<input type="text" id="textReferencetoyourinstitutionsholdingsguide_<s:property value="%{#status.index + 1}" />" value="<s:property value="#current" />" />
						</s:else>
					</td>
					<td class="labelLeft">
						<s:if test="%{#status.index == 0}">
							<label for="textYIHoldingsGuideLinkTitle"><s:property value="getText('label.ai.tabs.commons.linkTitle')"/>:</label>
						</s:if>
						<s:else>
							<label for="textYIHoldingsGuideLinkTitle_<s:property value="%{#status.index + 1}" />"><s:property value="getText('label.ai.tabs.commons.linkTitle')"/>:</label>
						</s:else>
					</td>
					<td>
						<s:if test="%{#status.index == 0}">
							<input type="text" id="textYIHoldingsGuideLinkTitle" onchange="linkToYourHolndingsGuideTitleChanged();" value="<s:property value="loader.yiResourceRelationrelationEntry[#counter]" />" />
						</s:if>
						<s:else>
							<input type="text" id="textYIHoldingsGuideLinkTitle_<s:property value="%{#status.index + 1}" />" value="<s:property value="loader.yiResourceRelationrelationEntry[#counter]" />" />
						</s:else>
					</td>
				</tr>

				<s:if test="%{#status.index == 0}">
					<tr id="trYIReferenceto2Holdingsguide"><%-- waiting for ticket decision #543 --%>
				</s:if>
				<s:else>
					<tr id="trYIReferenceto2Holdingsguide_<s:property value="%{#status.index + 1}" />"><%-- waiting for ticket decision #543 --%>
				</s:else>
					<td colspan="2">
					</td>
					<td class="labelLeft">
						<s:if test="%{#status.index == 0}">
							<label for="selectYIReferencetoHoldingsguide"><s:property value="getText('label.ai.tabs.commons.selectLanguage')"/>:</label>
						</s:if>
						<s:else>
							<label for="selectYIReferencetoHoldingsguide_<s:property value="%{#status.index + 1}" />"><s:property value="getText('label.ai.tabs.commons.selectLanguage')"/>:</label>
						</s:else>
					</td>
					<td>
						<s:if test="%{#status.index == 0}">
							<select id="selectYIReferencetoHoldingsguide">
						</s:if>
						<s:else>
							<select id="selectYIReferencetoHoldingsguide_<s:property value="%{#status.index + 1}" />" >
						</s:else>
							<s:iterator value="languageList" var="language"> 
								<option value="<s:property value="#language.key" />"
									<s:if test="%{#language.key == loader.yiResourceRelationLang[#counter]}" > selected=selected </s:if>>
									<s:property value="#language.value" />
								</option>
							</s:iterator>
						</select>
					</td>
				</tr><%-- waiting for ticket decision #543 --%>
				<s:set var="counter" value="%{#counter + 1}"/>
			</s:iterator>

			<tr>
				<td colspan="2">
				</td>
				<td colspan="2" class="labelLeft">
					<input type="button" id="buttonAddReferencetoyourinstitutionsholdingsguide" value="<s:property value="getText('label.ai.relations.addNewResourceRelation')"/>" onclick="yiAddReferencetoyourinstitutionsholdingsguide('<s:property value="getText('label.ai.tabs.commons.pleaseFillData')" />');" />
				</td>
			</tr>
		</s:if>
		<s:else>
			<tr id="trYIReferencetoHoldingsguide">
				<td>
					<label for="textReferencetoyourinstitutionsholdingsguide" ><s:property value="getText('label.ai.yourinstitution.referenceToYourInstitutionsHoldingsGuide')"/>:</label>
				</td>
				<td>
					<input type="text" id="textReferencetoyourinstitutionsholdingsguide" onchange="linkToYourHolndingsGuideChanged();" />
				</td>
				<td class="labelLeft">
					<label for="textYIHoldingsGuideLinkTitle" ><s:property value="getText('label.ai.tabs.commons.linkTitle')"/>:</label>
				</td>
				<td>
					<input type="text" id="textYIHoldingsGuideLinkTitle" onchange="linkToYourHolndingsGuideTitleChanged();" />
				</td>
			</tr>

			<tr id="trYIReferenceto2Holdingsguide" ><%-- waiting for ticket decision #543 --%>
				<td colspan="2"></td>
				<td class="labelLeft">
					<label for="selectYIReferencetoHoldingsguide"><s:property value="getText('label.ai.tabs.commons.selectLanguage')"/>:</label>
				</td>
				<td>
					<select id="selectYIReferencetoHoldingsguide" >
						<s:iterator value="languageList" var="language"> 
							<option value="<s:property value="#language.key" />"><s:property value="#language.value" /></option>
						</s:iterator>
					</select>
				</td>
			</tr>

			<tr><%-- waiting for ticket decision #543 --%>
				<td colspan="2">
				</td>
				<td colspan="2" class="labelLeft">
					<input type="button" id="buttonAddReferencetoyourinstitutionsholdingsguide" value="<s:property value="getText('label.ai.relations.addNewResourceRelation')"/>" onclick="yiAddReferencetoyourinstitutionsholdingsguide('<s:property value="getText('label.ai.tabs.commons.pleaseFillData')" />');" />
				</td>
			</tr>
		</s:else>

		<tr>
			<td id="tdButtonsYourInstitutionTab" colspan="4">
				<input type="button" id="buttonYourInstitutionTabNext" value="<s:property value='getText("label.ai.tabs.commons.button.nextTab")' />" class="rightButton" onclick="checkAndShowNextTab($(this).parent().parent().parent().parent(), '<s:property value="getText('label.ai.tabs.commons.fieldRequired')" />', '<s:property value="getText('label.ai.tabs.commons.pleaseFillMandatoryFields')" />','<s:property value="getText('label.eag.eagwithurlwarnings')"/>');" />
			</td>
		</tr>
	</table>
</div>
