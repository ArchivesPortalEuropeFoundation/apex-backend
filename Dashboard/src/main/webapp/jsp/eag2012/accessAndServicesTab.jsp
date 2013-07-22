<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<div id="headerContainer">
</div>

<s:if test="%{loader.numberOfRepositories > 0}">
	<div id="accessAndServicesTabContent">
		<s:set var="counter" value="0"/>
		<s:iterator begin="1" end="loader.numberOfRepositories" status="status">
			<table id="accessAndServicesTable_<s:property value="%{#status.index + 1}" />" class="tablePadding">
				<s:if test="%{loader.asOpening.size() > 0}">
					<s:set var="opening" value="loader.asOpening[#counter]"/>
					<s:set var="openingLang" value="loader.asOpeningLang[#counter]"/>
					<s:if test="%{#opening.size() > 0}">
						<s:iterator var="internalCurrent" value="#opening" status="internalStatus">
							<tr id="trASOpeningTimes_<s:property value="%{#internalStatus.index + 1}" />">
								<td id="tdOpeningTimes_<s:property value="%{#internalStatus.index + 1}" />">
									<s:if test="%{#internalStatus.index == 0}">
										<label for="textOpeningTimes_<s:property value="%{#internalStatus.index + 1}" />"><s:property value="getText('label.ai.tabs.commons.openingTimes')" /><span class="required">*</span>:</label>
									</s:if>
									<s:else>
										<label for="textOpeningTimes_<s:property value="%{#internalStatus.index + 1}" />"><s:property value="getText('label.ai.tabs.commons.openingTimes')" />:</label>
									</s:else>
								</td>
								<td>
									<s:if test="%{#counter > 0}">
										<input type="text" id="textOpeningTimes_<s:property value="%{#internalStatus.index + 1}" />" value="<s:property value="#internalCurrent" />" />
									</s:if>
									<s:else>
										<input type="text" id="textOpeningTimes_<s:property value="%{#internalStatus.index + 1}" />" onchange="aSOpeningHoursOfInstitutionChanged($(this));" value="<s:property value="#internalCurrent" />" />
									</s:else>
								</td>
								<td class="labelLeft">
									<label for="selectLanguageOpeningTimes_<s:property value="%{#internalStatus.index + 1}" />" ><s:property value="getText('label.ai.tabs.commons.selectLanguage')" />:</label>
								</td>
								<td>
									<s:if test="%{#counter > 0}">
										<select id="selectLanguageOpeningTimes_<s:property value="%{#internalStatus.index + 1}" />">
									</s:if>
									<s:else>
										<select id="selectLanguageOpeningTimes_<s:property value="%{#internalStatus.index + 1}" />" onchange="aSOpeningHoursOfInstitutionLangChanged($(this));">
									</s:else>
										<s:iterator value="languageList" var="language"> 
											<option value="<s:property value="#language.key" />"<s:if test="%{#language.key == #openingLang[#internalStatus.index]}" > selected=selected </s:if>><s:property value="#language.value" /></option>
										</s:iterator>
									</select>
								</td>
							</tr>
						</s:iterator>
					</s:if>
					<s:else>
						<tr id="trASOpeningTimes_1">
							<td id="tdOpeningTimes_1">
								<label for="textOpeningTimes_1"><s:property value="getText('label.ai.tabs.commons.openingTimes')" /><span class="required">*</span>:</label>
							</td>
							<td>
								<s:if test="%{#counter > 0}">
									<input type="text" id="textOpeningTimes_1" />
								</s:if>
								<s:else>
									<input type="text" id="textOpeningTimes_1" onchange="aSOpeningHoursOfInstitutionChanged($(this));" />
								</s:else>
							</td>
							<td class="labelLeft">
								<label for="selectLanguageOpeningTimes_1" ><s:property value="getText('label.ai.tabs.commons.selectLanguage')" />:</label>
							</td>
							<td>
								<s:if test="%{#counter > 0}">
									<select id="selectLanguageOpeningTimes_1">
								</s:if>
								<s:else>
									<select id="selectLanguageOpeningTimes_1" onchange="aSOpeningHoursOfInstitutionLangChanged($(this));">
								</s:else>
									<s:iterator value="languageList" var="language"> 
										<option value="<s:property value="#language.key" />"><s:property value="#language.value" /></option>
									</s:iterator>
								</select>
							</td>
						</tr>
					</s:else>
				</s:if>
				<s:else>
					<tr id="trASOpeningTimes_1">
						<td id="tdOpeningTimes_1">
							<label for="textOpeningTimes_1"><s:property value="getText('label.ai.tabs.commons.openingTimes')" /><span class="required">*</span>:</label>
						</td>
						<td>
							<s:if test="%{#counter > 0}">
								<input type="text" id="textOpeningTimes_1" />
							</s:if>
							<s:else>
								<input type="text" id="textOpeningTimes_1" onchange="aSOpeningHoursOfInstitutionChanged($(this));" />
							</s:else>
						</td>
						<td class="labelLeft">
							<label for="selectLanguageOpeningTimes_1" ><s:property value="getText('label.ai.tabs.commons.selectLanguage')" />:</label>
						</td>
						<td>
							<s:if test="%{#counter > 0}">
								<select id="selectLanguageOpeningTimes_1">
							</s:if>
							<s:else>
								<select id="selectLanguageOpeningTimes_1" onchange="aSOpeningHoursOfInstitutionLangChanged($(this));">
							</s:else>
								<s:iterator value="languageList" var="language"> 
									<option value="<s:property value="#language.key" />"><s:property value="#language.value" /></option>
								</s:iterator>
							</select>
						</td>
					</tr>
				</s:else>
		
				<tr>
					<td id="tdASAddOpeningTimes" colspan="2">
						<input type="button" id="buttonASAddOpeningTimes"  value="<s:property value='getText("label.ai.accessAndServices.addOpeningTimes")' />" onclick="aSAddOpeningTimes('<s:property value="getText('label.ai.tabs.commons.pleaseFillData')" />');" />
					</td>
					<td colspan="2">
					</td>
				</tr>

				<s:if test="%{loader.asClosing.size() > 0}">
					<s:set var="closing" value="loader.asClosing[#counter]"/>
					<s:set var="closingLang" value="loader.asClosingLang[#counter]"/>
					<s:if test="%{#closing.size() > 0}">
						<s:iterator var="internalCurrent" value="#closing" status="internalStatus">
							<tr id="trASClosingDates_<s:property value="%{#internalStatus.index + 1}" />">
								<td>
									<label for="textClosingDates_<s:property value="%{#internalStatus.index + 1}" />"><s:property value="getText('label.ai.tabs.commons.closingDates')" />:</label>
								</td>
								<td>
									<s:if test="%{#counter > 0}">
										<input type="text" id="textClosingDates_<s:property value="%{#internalStatus.index + 1}" />" value="<s:property value="#internalCurrent" />" />
									</s:if>
									<s:else>
										<input type="text" id="textClosingDates_<s:property value="%{#internalStatus.index + 1}" />" onchange="aSClosingHoursOfInstitutionChanged($(this));" value="<s:property value="#internalCurrent" />" />
									</s:else>
								</td>
								<td class="labelLeft">
									<label for="selectLanguageClosingDates_<s:property value="%{#internalStatus.index + 1}" />" ><s:property value="getText('label.ai.tabs.commons.selectLanguage')" />:</label>
								</td>
								<td>
									<s:if test="%{#counter > 0}">
										<select id="selectLanguageClosingDates_<s:property value="%{#internalStatus.index + 1}" />">
									</s:if>
									<s:else>
										<select id="selectLanguageClosingDates_<s:property value="%{#internalStatus.index + 1}" />" onchange="aSClosingHoursOfInstitutionLangChanged($(this));">
									</s:else>
										<s:iterator value="languageList" var="language"> 
											<option value="<s:property value="#language.key" />"<s:if test="%{#language.key == #closingLang[#internalStatus.index]}" > selected=selected </s:if>><s:property value="#language.value" /></option>
										</s:iterator>
									</select>
								</td>
							</tr>
						</s:iterator>
					</s:if>
					<s:else>
						<tr id="trASClosingDates_1">
							<td>
								<label for="textClosingDates_1"><s:property value="getText('label.ai.tabs.commons.closingDates')" />:</label>
							</td>
							<td>
								<s:if test="%{#counter > 0}">
									<input type="text" id="textClosingDates_1" />
								</s:if>
								<s:else>
									<input type="text" id="textClosingDates_1" onchange="aSClosingHoursOfInstitutionChanged($(this));"/>
								</s:else>
							</td>
							<td class="labelLeft">
								<label for="selectLanguageClosingDates_1" ><s:property value="getText('label.ai.tabs.commons.selectLanguage')" />:</label>
							</td>
							<td>
								<s:if test="%{#counter > 0}">
									<select id="selectLanguageClosingDates_1">
								</s:if>
								<s:else>
									<select id="selectLanguageClosingDates_1" onchange="aSClosingHoursOfInstitutionLangChanged($(this));">
								</s:else>
									<s:iterator value="languageList" var="language"> 
										<option value="<s:property value="#language.key" />"><s:property value="#language.value" /></option>
									</s:iterator>
								</select>
							</td>
						</tr>
					</s:else>
				</s:if>
				<s:else>
					<tr id="trASClosingDates_1">
						<td>
							<label for="textClosingDates_1"><s:property value="getText('label.ai.tabs.commons.closingDates')" />:</label>
						</td>
						<td>
							<s:if test="%{#counter > 0}">
								<input type="text" id="textClosingDates_1" />
							</s:if>
							<s:else>
								<input type="text" id="textClosingDates_1" onchange="aSClosingHoursOfInstitutionChanged($(this));" />
							</s:else>
						</td>
						<td class="labelLeft">
							<label for="selectLanguageClosingDates_1" ><s:property value="getText('label.ai.tabs.commons.selectLanguage')" />:</label>
						</td>
						<td>
							<s:if test="%{#counter > 0}">
								<select id="selectLanguageClosingDates_1">
							</s:if>
							<s:else>
								<select id="selectLanguageClosingDates_1" onchange="aSClosingHoursOfInstitutionLangChanged($(this));">
							</s:else>
								<s:iterator value="languageList" var="language"> 
									<option value="<s:property value="#language.key" />"><s:property value="#language.value" /></option>
								</s:iterator>
							</select>
						</td>
					</tr>
				</s:else>

				<tr>
					<td id="tdASAddClosingDates" colspan="2">
						<input type="button" id="buttonASAddClosingDates"  value="<s:property value='getText("label.ai.accessAndServices.addClosingDates")' />" onclick="aSAddClosingDates('<s:property value="getText('label.ai.tabs.commons.pleaseFillData')" />');" />
					</td>
					<td colspan="2">
					</td>
				</tr>

				<s:if test="%{loader.asNumberOfDirections.size() > 0}">
					<s:set var="numberOfDirections" value="loader.asNumberOfDirections[#counter]"/>
					<s:set var="directions" value="loader.asDirections[#counter]"/>
					<s:set var="directionsLang" value="loader.asDirectionsLang[#counter]"/>
					<s:set var="directionsHref" value="loader.asDirectionsCitationHref[#counter]"/>
					<s:if test="%{#numberOfDirections.size() > 0}">
						<s:set var="internalCounter" value="0"/>
						<s:iterator var="internalCurrent" value="#numberOfDirections" status="internalStatus">
							<tr id="trTravellingDirections_<s:property value="%{#internalStatus.index + 1}" />">
								<td>
									<label for="textTravellingDirections_<s:property value="%{#internalStatus.index + 1}" />"><s:property value="getText('label.ai.accessAndServices.travellingDirections')" />:</label>
								</td>
								<td>
									<textarea id="textTravellingDirections_<s:property value="%{#internalStatus.index + 1}" />"><s:property value="#directions[#internalCounter]" /></textarea>
								</td>
								<td class="labelLeft">
									<label for="selectASATDSelectLanguage_<s:property value="%{#internalStatus.index + 1}" />"><s:property value="getText('label.ai.tabs.commons.selectLanguage')" />:</label>
								</td>
								<td>
									<select id="selectASATDSelectLanguage_<s:property value="%{#internalStatus.index + 1}" />">
										<s:iterator value="languageList" var="language"> 
											<option value="<s:property value="#language.key" />"<s:if test="%{#language.key == #directionsLang[#internalCounter]}" > selected=selected </s:if>><s:property value="#language.value" /></option>
										</s:iterator>
									</select>
								</td>
							</tr>

							<tr id="tr2TravellingDirections_<s:property value="%{#internalStatus.index + 1}" />">
								<td>
									<label for="textTravelLink_<s:property value="%{#internalStatus.index + 1}" />"><s:property value="getText('label.ai.accessAndServices.link')"/>:</label>
								</td>
								<td colspan="2">
									<input type="text" id="textTravelLink_<s:property value="%{#internalStatus.index + 1}" />" value="<s:property value="#directionsHref[#internalCounter]" />" />
								</td>
								<td colspan="2">
								</td>
							</tr>
							<s:set var="internalCounter" value="%{#internalCounter + 1}"/>
						</s:iterator>
					</s:if>
					<s:else>
						<tr id="trTravellingDirections_1">
							<td>
								<label for="textTravellingDirections_1"><s:property value="getText('label.ai.accessAndServices.travellingDirections')" />:</label>
							</td>
							<td>
								<textarea id="textTravellingDirections_1"></textarea>
							</td>
							<td class="labelLeft">
								<label for="selectASATDSelectLanguage_1"><s:property value="getText('label.ai.tabs.commons.selectLanguage')"/>:</label>
							</td>
							<td>
								<select id="selectASATDSelectLanguage_1">
									<s:iterator value="languageList" var="language"> 
										<option value="<s:property value="#language.key" />"><s:property value="#language.value" /></option>
									</s:iterator>
								</select>
							</td>
						</tr>

						<tr id="tr2TravellingDirections_1">
							<td>
								<label for="textTravelLink_1"><s:property value="getText('label.ai.accessAndServices.link')"/>:</label>
							</td>
							<td colspan="2">
								<input type="text" id="textTravelLink_1" />
							</td>
							<td colspan="2">
							</td>
						</tr>
					</s:else>
				</s:if>
				<s:else>
					<tr id="trTravellingDirections_1">
						<td>
							<label for="textTravellingDirections_1"><s:property value="getText('label.ai.accessAndServices.travellingDirections')" />:</label>
						</td>
						<td>
							<textarea id="textTravellingDirections_1"></textarea>
						</td>
						<td class="labelLeft">
							<label for="selectASATDSelectLanguage_1"><s:property value="getText('label.ai.tabs.commons.selectLanguage')"/>:</label>
						</td>
						<td>
							<select id="selectASATDSelectLanguage_1">
								<s:iterator value="languageList" var="language"> 
									<option value="<s:property value="#language.key" />"><s:property value="#language.value" /></option>
								</s:iterator>
							</select>
						</td>
					</tr>

					<tr id="tr2TravellingDirections_1">
						<td>
							<label for="textTravelLink_1"><s:property value="getText('label.ai.accessAndServices.link')"/>:</label>
						</td>
						<td colspan="2">
							<input type="text" id="textTravelLink_1" />
						</td>
						<td colspan="2">
						</td>
					</tr>
				</s:else>

				<tr>
					<td colspan="2">
						<input type="button" id="buttonASAddTravellingDirections" value="<s:property value="getText('label.ai.accessAndServices.addTravellingDirections')"/>" onclick="aSAddTravellingDirections('<s:property value="getText('label.ai.tabs.commons.pleaseFillData')" />');"/>
					</td>
					<td colspan="2">
					</td>
				</tr>

				<s:set var="accesibleToThePublic" value="loader.asAccessQuestion[#counter]"/>
				<tr>
					<td>
						<label for="selectASAccesibleToThePublic"><s:property value="getText('label.ai.accessAndServices.accesibleToThePublic')"/><span class="required">*</span>:</label>
					</td>
					<td>
						<select id="selectASAccesibleToThePublic" onchange="aSAccessibleToThePublicChanged();">
							<s:iterator value="yesNoList" var="yesno"> 
								<option value="<s:property value="#yesno.key" />"<s:if test="%{#yesno.key == #accesibleToThePublic[0]}" > selected=selected </s:if>><s:property value="#yesno.value" /></option>
							</s:iterator>
						</select>
					</td>
					<td colspan="2">
					</td>
				</tr>

				<s:if test="%{loader.asRestaccess.size() > 0}">
					<s:set var="restaccess" value="loader.asRestaccess[#counter]"/>
					<s:set var="restaccessLang" value="loader.asRestaccessLang[#counter]"/>
					<s:if test="%{#restaccess.size() > 0}">
						<s:iterator var="internalCurrent" value="#restaccess" status="internalStatus">
							<tr id="trASAccessRestrictions_<s:property value="%{#internalStatus.index + 1}" />">
								<td>
									<label for="textASAccessRestrictions_<s:property value="%{#internalStatus.index + 1}" />"><s:property value="getText('label.ai.accessAndServices.accessRestrictions')" />:</label>
								</td>
								<td>
									<s:if test="%{#counter > 0}">
										<input type="text" id="textASAccessRestrictions_<s:property value="%{#internalStatus.index + 1}" />" value="<s:property value="#internalCurrent" />" />
									</s:if>
									<s:else>
										<input type="text" id="textASAccessRestrictions_<s:property value="%{#internalStatus.index + 1}" />" onchange="aSFutherAccessInformationChanged($(this));" value="<s:property value="#internalCurrent" />" />
									</s:else>
								</td>
								<td class="labelLeft">
									<label for="selectASARSelectLanguage_<s:property value="%{#internalStatus.index + 1}" />" ><s:property value="getText('label.ai.tabs.commons.selectLanguage')" />:</label>
								</td>
								<td>
									<s:if test="%{#counter > 0}">
										<select id="selectASARSelectLanguage_<s:property value="%{#internalStatus.index + 1}" />">
									</s:if>
									<s:else>
										<select id="selectASARSelectLanguage_<s:property value="%{#internalStatus.index + 1}" />" onchange="aSFutherAccessInformationLangChanged($(this));">
									</s:else>
										<s:iterator value="languageList" var="language"> 
											<option value="<s:property value="#language.key" />"<s:if test="%{#language.key == #restaccessLang[#internalStatus.index]}" > selected=selected </s:if>><s:property value="#language.value" /></option>
										</s:iterator>
									</select>
								</td>
							</tr>
						</s:iterator>
					</s:if>
					<s:else>
						<tr id="trASAccessRestrictions_1">
							<td>
								<label for="textASAccessRestrictions_1"><s:property value="getText('label.ai.accessAndServices.accessRestrictions')" />:</label>
							</td>
							<td>
								<s:if test="%{#counter > 0}">
									<input type="text" id="textASAccessRestrictions_1" />
								</s:if>
								<s:else>
									<input type="text" id="textASAccessRestrictions_1" onchange="aSFutherAccessInformationChanged($(this));" />
								</s:else>
							</td>
							<td class="labelLeft">
								<label for="selectASARSelectLanguage_1"><s:property value="getText('label.ai.tabs.commons.selectLanguage')"/>:</label>
							</td>
							<td>
								<s:if test="%{#counter > 0}">
									<select id="selectASARSelectLanguage_1">
								</s:if>
								<s:else>
									<select id="selectASARSelectLanguage_1" onchange="aSFutherAccessInformationLangChanged($(this));">
								</s:else>
									<s:iterator value="languageList" var="language"> 
										<option value="<s:property value="#language.key" />"><s:property value="#language.value" /></option>
									</s:iterator>
								</select>
							</td>
						</tr>
					</s:else>
				</s:if>
				<s:else>
					<tr id="trASAccessRestrictions_1">
						<td>
							<label for="textASAccessRestrictions_1"><s:property value="getText('label.ai.accessAndServices.accessRestrictions')" />:</label>
						</td>
						<td>
							<s:if test="%{#counter > 0}">
								<input type="text" id="textASAccessRestrictions_1" />
							</s:if>
							<s:else>
								<input type="text" id="textASAccessRestrictions_1" onchange="aSFutherAccessInformationChanged($(this));" />
							</s:else>
						</td>
						<td class="labelLeft">
							<label for="selectASARSelectLanguage_1"><s:property value="getText('label.ai.tabs.commons.selectLanguage')"/>:</label>
						</td>
						<td>
							<s:if test="%{#counter > 0}">
								<select id="selectASARSelectLanguage_1">
							</s:if>
							<s:else>
								<select id="selectASARSelectLanguage_1" onchange="aSFutherAccessInformationLangChanged($(this));">
							</s:else>
								<s:iterator value="languageList" var="language"> 
									<option value="<s:property value="#language.key" />"><s:property value="#language.value" /></option>
								</s:iterator>
							</select>
						</td>
					</tr>
				</s:else>

				<tr>
					<td colspan="2">
						<input type="button" id="buttonAddFutherAccessInformation" value="<s:property value="getText('label.ai.accessAndServices.addFutherAccessInformation')"/>" onclick="addFutherAccessInformation('<s:property value="getText('label.ai.tabs.commons.pleaseFillData')" />');" />
					</td>
					<td colspan="2">
					</td>
				</tr>

				<s:if test="%{loader.asNumberOfTermsOfUse.size() > 0}">
					<s:set var="numberOfTermsOfUse" value="loader.asNumberOfTermsOfUse[#counter]"/>
					<s:set var="termsOfUse" value="loader.asTermsOfUse[#counter]"/>
					<s:set var="termsOfUseLang" value="loader.asTermsOfUseLang[#counter]"/>
					<s:set var="termsOfUseHref" value="loader.asTermsOfUseHref[#counter]"/>
					<s:if test="%{#numberOfTermsOfUse.size() > 0}">
						<s:iterator var="internalCurrent" value="#numberOfTermsOfUse" status="internalStatus">
							<tr id="trASAddFutherTermOfUse_<s:property value="%{#internalStatus.index + 1}" />">
								<td>
									<label for="textASTermOfUse_<s:property value="%{#internalStatus.index + 1}" />"><s:property value="getText('label.ai.accessAndServices.termsOfUse')" />:</label>
								</td>
								<td>
									<textarea id="textASTermOfUse_<s:property value="%{#internalStatus.index + 1}" />"><s:property value="#termsOfUse[#internalStatus.index]" /></textarea>
								</td>
								<td class="labelLeft">
									<label for="selectASAFTOUSelectLanguage_<s:property value="%{#internalStatus.index + 1}" />" ><s:property value="getText('label.ai.tabs.commons.selectLanguage')" />:</label>
								</td>
								<td>
									<select id="selectASAFTOUSelectLanguage_<s:property value="%{#internalStatus.index + 1}" />">
										<s:iterator value="languageList" var="language"> 
											<option value="<s:property value="#language.key" />"<s:if test="%{#language.key == #termsOfUseLang[#internalStatus.index]}" > selected=selected </s:if>><s:property value="#language.value" /></option>
										</s:iterator>
									</select>
								</td>
							</tr>

							<tr id="tr2ASAddFutherTermOfUse_<s:property value="%{#internalStatus.index + 1}" />">
								<td>
									<label for="textASTOULink_<s:property value="%{#internalStatus.index + 1}" />"><s:property value="getText('label.ai.accessAndServices.link')"/>:</label>
								</td>
								<td colspan="2">
									<input type="text" id="textASTOULink_<s:property value="%{#internalStatus.index + 1}" />" value="<s:property value="#termsOfUseHref[#internalStatus.index]" />" />
								</td>
								<td colspan="2">
								</td>
							</tr>
						</s:iterator>
					</s:if>
					<s:else>
						<tr id="trASAddFutherTermOfUse_1">
							<td>
								<label for="textASTermOfUse_1"><s:property value="getText('label.ai.accessAndServices.termsOfUse')" />:</label>
							</td>
							<td>
								<textarea id="textASTermOfUse_1"></textarea>
							</td>
							<td class="labelLeft">
								<label for="selectASAFTOUSelectLanguage_1"><s:property value="getText('label.ai.tabs.commons.selectLanguage')"/>:</label>
							</td>
							<td>
								<select id="selectASAFTOUSelectLanguage_1">
									<s:iterator value="languageList" var="language"> 
										<option value="<s:property value="#language.key" />"><s:property value="#language.value" /></option>
									</s:iterator>
								</select>
							</td>
						</tr>

						<tr id="tr2ASAddFutherTermOfUse_1">
							<td>
								<label for="textASTOULink_1"><s:property value="getText('label.ai.accessAndServices.link')"/>:</label>
							</td>
							<td>
								<input type="text" id="textASTOULink_1" />
							</td>
							<td colspan="2">
							</td>
						</tr>
					</s:else>
				</s:if>
				<s:else>
					<tr id="trASAddFutherTermOfUse_1">
						<td>
							<label for="textASTermOfUse_1"><s:property value="getText('label.ai.accessAndServices.termsOfUse')" />:</label>
						</td>
						<td>
							<textarea id="textASTermOfUse_1"></textarea>
						</td>
						<td class="labelLeft">
							<label for="selectASAFTOUSelectLanguage_1"><s:property value="getText('label.ai.tabs.commons.selectLanguage')"/>:</label>
						</td>
						<td>
							<select id="selectASAFTOUSelectLanguage_1">
								<s:iterator value="languageList" var="language"> 
									<option value="<s:property value="#language.key" />"><s:property value="#language.value" /></option>
								</s:iterator>
							</select>
						</td>
					</tr>

					<tr id="tr2ASAddFutherTermOfUse_1">
						<td>
							<label for="textASTOULink_1"><s:property value="getText('label.ai.accessAndServices.link')"/>:</label>
						</td>
						<td>
							<input type="text" id="textASTOULink_1" />
						</td>
						<td colspan="2">
						</td>
					</tr>
				</s:else>

				<tr>
					<td colspan="2">
						<input type="button" id="buttonASAddFutherTermOfUse" value="<s:property value="getText('label.ai.accessAndServices.addFurtherTermsOfUse')"/>" onclick="aSAddFutherTermOfUse('<s:property value="getText('label.ai.tabs.commons.pleaseFillData')" />');" />
					</td>
					<td colspan="2">
					</td>
				</tr>

				<s:set var="accessibilityToThePublic" value="loader.asAccessibilityQuestion[#counter]"/>
				<tr>
					<td>
						<label for="selectASFacilitiesForDisabledPeopleAvailable"><s:property value="getText('label.ai.accessAndServices.facilitiesForDisabledPeopleAvailable')"/><span class="required">*</span>:</label>
					</td>
					<td>
						<select id="selectASFacilitiesForDisabledPeopleAvailable" onchange="aSFacilitiesForDisabledPeopleAvailableChanged();">
							<s:iterator value="yesNoList" var="language"> 
								<option value="<s:property value="#language.key" />"<s:if test="%{#language.key == #accessibilityToThePublic[0]}" > selected=selected </s:if>><s:property value="#language.value" /></option>
							</s:iterator>
						</select>
					</td>
				</tr>

				<s:if test="%{loader.asAccessibility.size() > 0}">
					<s:set var="accessibility" value="loader.asAccessibility[#counter]"/>
					<s:set var="accessibilityLang" value="loader.asAccessibilityLang[#counter]"/>
					<s:if test="%{#accessibility.size() > 0}">
						<s:iterator var="internalCurrent" value="#accessibility" status="internalStatus">
							<tr id="trAccessibilityInformation_<s:property value="%{#internalStatus.index + 1}" />">
								<td>
									<label for="textASAccessibility_<s:property value="%{#internalStatus.index + 1}" />"><s:property value="getText('label.ai.accessAndServices.accesibility')" />:</label>
								</td>
								<td>
									<s:if test="%{#counter > 0}">
										<input type="text" id="textASAccessibility_<s:property value="%{#internalStatus.index + 1}" />" value="<s:property value="#internalCurrent" />" />
									</s:if>
									<s:else>
										<input type="text" id="textASAccessibility_<s:property value="%{#internalStatus.index + 1}" />" onchange="aSFutherInformationOnExistingFacilitiesChanged($(this));" value="<s:property value="#internalCurrent" />" />
									</s:else>
								</td>
								<td class="labelLeft">
									<label for="selectASASelectLanguage_<s:property value="%{#internalStatus.index + 1}" />" ><s:property value="getText('label.ai.tabs.commons.selectLanguage')" />:</label>
								</td>
								<td>
									<s:if test="%{#counter > 0}">
										<select id="selectASASelectLanguage_<s:property value="%{#internalStatus.index + 1}" />">
									</s:if>
									<s:else>
										<select id="selectASASelectLanguage_<s:property value="%{#internalStatus.index + 1}" />" onchange="aSFutherInformationOnExistingFacilitiesLangChanged($(this));">
									</s:else>
										<s:iterator value="languageList" var="language"> 
											<option value="<s:property value="#language.key" />"<s:if test="%{#language.key == #accessibilityLang[#internalStatus.index]}" > selected=selected </s:if>><s:property value="#language.value" /></option>
										</s:iterator>
									</select>
								</td>
							</tr>
						</s:iterator>
					</s:if>
					<s:else>
						<tr id="trAccessibilityInformation_1">
							<td>
								<label for="textASAccessibility_1"><s:property value="getText('label.ai.accessAndServices.accesibility')" />:</label>
							</td>
							<td>
								<s:if test="%{#counter > 0}">
									<input type="text" id="textASAccessibility_1" />
								</s:if>
								<s:else>
									<input type="text" id="textASAccessibility_1" onchange="aSFutherInformationOnExistingFacilitiesChanged($(this));" />
								</s:else>
							</td>
							<td class="labelLeft">
								<label for="selectASASelectLanguage_1"><s:property value="getText('label.ai.tabs.commons.selectLanguage')" />:</label>
							</td>
							<td>
								<s:if test="%{#counter > 0}">
									<select id="selectASASelectLanguage_1">
								</s:if>
								<s:else>
									<select id="selectASASelectLanguage_1" onchange="aSFutherInformationOnExistingFacilitiesLangChanged($(this));">
								</s:else>
									<s:iterator value="languageList" var="language"> 
										<option value="<s:property value="#language.key" />"><s:property value="#language.value" /></option>
									</s:iterator>
								</select>
							</td>
						</tr>
					</s:else>
				</s:if>
				<s:else>
					<tr id="trAccessibilityInformation_1">
						<td>
							<label for="textASAccessibility_1"><s:property value="getText('label.ai.accessAndServices.accesibility')" />:</label>
						</td>
						<td>
							<s:if test="%{#counter > 0}">
								<input type="text" id="textASAccessibility_1" />
							</s:if>
							<s:else>
								<input type="text" id="textASAccessibility_1" onchange="aSFutherInformationOnExistingFacilitiesChanged($(this));" />
							</s:else>
						</td>
						<td class="labelLeft">
							<label for="selectASASelectLanguage_1"><s:property value="getText('label.ai.tabs.commons.selectLanguage')" />:</label>
						</td>
						<td>
							<s:if test="%{#counter > 0}">
								<select id="selectASASelectLanguage_1">
							</s:if>
							<s:else>
								<select id="selectASASelectLanguage_1" onchange="aSFutherInformationOnExistingFacilitiesLangChanged($(this));">
							</s:else>
								<s:iterator value="languageList" var="language"> 
									<option value="<s:property value="#language.key" />"><s:property value="#language.value" /></option>
								</s:iterator>
							</select>
						</td>
					</tr>
				</s:else>
		
				<tr>
					<td colspan="2">
						<input type="button" id="buttonAddAccessibilityInformation" value="<s:property value="getText('label.ai.accessAndServices.addAccessibilityInformation')"/>" onclick="addAccessibilityInformation('<s:property value="getText('label.ai.tabs.commons.pleaseFillData')" />');" />
					</td>
					<td colspan="2">
					</td>
				</tr>

				<tr>
					<td id="searchroomLabel" colspan="4">
						<span><s:property value="getText('label.ai.accessAndServices.searchroom')" /></span>
					</td>
				</tr>

				<s:if test="%{loader.asSearchRoomTelephone.size() > 0}">
					<s:set var="searchRoomTelephone" value="loader.asSearchRoomTelephone[#counter]"/>
					<s:if test="%{#searchRoomTelephone.size() > 0}">
						<s:iterator var="internalCurrent" value="#searchRoomTelephone" status="internalStatus">
							<tr>
								<td>
									<label for="textASSRTelephone_<s:property value="%{#internalStatus.index + 1}" />"><s:property value="getText('label.ai.tabs.commons.telephone')" />:</label>
								</td>
								<td>
									<input type="text" id="textASSRTelephone_<s:property value="%{#internalStatus.index + 1}" />" value="<s:property value="#internalCurrent" />" />
								</td>
								<td colspan="2">
								</td>
							</tr>
						</s:iterator>
					</s:if>
					<s:else>
						<tr>
							<td>
								<label for="textASSRTelephone_1"><s:property value="getText('label.ai.tabs.commons.telephone')"/>:</label>
							</td>
							<td>
								<input type="text" id="textASSRTelephone_1" />
							</td>
							<td colspan="2">
							</td>
						</tr>
					</s:else>
				</s:if>
				<s:else>
					<tr>
						<td>
							<label for="textASSRTelephone_1"><s:property value="getText('label.ai.tabs.commons.telephone')"/>:</label>
						</td>
						<td>
							<input type="text" id="textASSRTelephone_1"/>
						</td>
						<td colspan="2">
						</td>
					</tr>
				</s:else>

				<s:if test="%{loader.asSearchRoomNumberOfEmail.size() > 0}">
					<s:set var="srNumberOfEmail" value="loader.asSearchRoomNumberOfEmail[#counter]"/>
					<s:set var="srEmailHref" value="loader.asSearchRoomEmailHref[#counter]"/>
					<s:set var="srEmailTitle" value="loader.asSearchRoomEmailTitle[#counter]"/>
					<s:if test="%{#srNumberOfEmail.size() > 0}">
						<s:iterator var="internalCurrent" value="#srNumberOfEmail" status="internalStatus">
							<tr>
								<td>
									<s:if test="%{#internalStatus.index > 0}">
										<label for="textASSREmailAddress_<s:property value="%{#internalStatus.index + 1}" />"><s:property value="getText('label.ai.tabs.commons.emailAddress')" />:</label>
									</s:if>
									<s:else>
										<label for="textASSREmailAddress"><s:property value="getText('label.ai.tabs.commons.emailAddress')" />:</label>
									</s:else>
								</td>
								<td>
									<s:if test="%{#internalStatus.index > 0}">
										<input type="text" id="textASSREmailAddress_<s:property value="%{#internalStatus.index + 1}" />" value="<s:property value="#srEmailHref[#internalStatus.index]" />" />
									</s:if>
									<s:else>
										<input type="text" id="textASSREmailAddress" value="<s:property value="#srEmailHref[#internalStatus.index]" />" />
									</s:else>
								</td>
								<td class="labelLeft">
									<s:if test="%{#internalStatus.index > 0}">
										<label for="textASSREmailLinkTitle_<s:property value="%{#internalStatus.index + 1}" />"><s:property value="getText('label.ai.tabs.commons.linkTitle')" />:</label>
									</s:if>
									<s:else>
										<label for="textASSREmailLinkTitle"><s:property value="getText('label.ai.tabs.commons.linkTitle')"/>:</label>
									</s:else>
								</td>
								<td>
									<s:if test="%{#internalStatus.index > 0}">
										<input type="text" id="textASSREmailLinkTitle_<s:property value="%{#internalStatus.index + 1}" />" value="<s:property value="#srEmailTitle[#internalStatus.index]" />" />
									</s:if>
									<s:else>
										<input type="text" id="textASSREmailLinkTitle" value="<s:property value="#srEmailTitle[#internalStatus.index]" />" />
									</s:else>
								</td>
							</tr>
						</s:iterator>
					</s:if>
					<s:else>
						<tr>
							<td>
								<label for="textASSREmailAddress"><s:property value="getText('label.ai.tabs.commons.emailAddress')"/>:</label>
							</td>
							<td>
								<input type="text" id="textASSREmailAddress" />
							</td>
							<td class="labelLeft">
								<label for="textASSREmailLinkTitle"><s:property value="getText('label.ai.tabs.commons.linkTitle')"/>:</label>
							</td>
							<td>
								<input type="text" id="textASSREmailLinkTitle" />
							</td>
						</tr>
					</s:else>
				</s:if>
				<s:else>
					<tr>
						<td>
							<label for="textASSREmailAddress"><s:property value="getText('label.ai.tabs.commons.emailAddress')"/>:</label>
						</td>
						<td>
							<input type="text" id="textASSREmailAddress" />
						</td>
						<td class="labelLeft">
							<label for="textASSREmailLinkTitle"><s:property value="getText('label.ai.tabs.commons.linkTitle')"/>:</label>
						</td>
						<td>
							<input type="text" id="textASSREmailLinkTitle" />
						</td>
					</tr>
				</s:else>

				<s:if test="%{loader.asSearchRoomNumberOfWebpage.size() > 0}">
					<s:set var="srNumberOfWebpage" value="loader.asSearchRoomNumberOfWebpage[#counter]"/>
					<s:set var="srWebpageHref" value="loader.asSearchRoomWebpageHref[#counter]"/>
					<s:set var="srWebpageTitle" value="loader.asSearchRoomWebpageTitle[#counter]"/>
					<s:if test="%{#srNumberOfWebpage.size() > 0}">
						<s:iterator var="internalCurrent" value="#srNumberOfWebpage" status="internalStatus">
							<tr>
								<td>
									<s:if test="%{#internalStatus.index > 0}">
										<label for="textASSRWebpage_<s:property value="%{#internalStatus.index + 1}" />"><s:property value="getText('label.ai.tabs.commons.webpage')" />:</label>
									</s:if>
									<s:else>
										<label for="textASSRWebpage"><s:property value="getText('label.ai.tabs.commons.webpage')" />:</label>
									</s:else>
								</td>
								<td>
									<s:if test="%{#internalStatus.index > 0}">
										<input type="text" id="textASSRWebpage_<s:property value="%{#internalStatus.index + 1}" />" value="<s:property value="#srWebpageHref[#internalStatus.index]" />" />
									</s:if>
									<s:else>
										<input type="text" id="textASSRWebpage" value="<s:property value="#srWebpageHref[#internalStatus.index]" />" />
									</s:else>
								</td>
								<td class="labelLeft">
									<s:if test="%{#internalStatus.index > 0}">
										<label for="textASSRWebpageLinkTitle_<s:property value="%{#internalStatus.index + 1}" />"><s:property value="getText('label.ai.tabs.commons.linkTitle')" />:</label>
									</s:if>
									<s:else>
										<label for="textASSRWebpageLinkTitle"><s:property value="getText('label.ai.tabs.commons.linkTitle')"/>:</label>
									</s:else>
								</td>
								<td>
									<s:if test="%{#internalStatus.index > 0}">
										<input type="text" id="textASSRWebpageLinkTitle_<s:property value="%{#internalStatus.index + 1}" />" value="<s:property value="#srWebpageTitle[#internalStatus.index]" />" />
									</s:if>
									<s:else>
										<input type="text" id="textASSRWebpageLinkTitle" value="<s:property value="#srWebpageTitle[#internalStatus.index]" />" />
									</s:else>
								</td>
							</tr>
						</s:iterator>
					</s:if>
					<s:else>
						<tr>
							<td>
								<label for="textASSRWebpage"><s:property value="getText('label.ai.tabs.commons.webpage')"/>:</label>
							</td>
							<td>
								<input type="text" id="textASSRWebpage" />
							</td>
							<td class="labelLeft">
								<label for="textASSRWebpageLinkTitle"><s:property value="getText('label.ai.tabs.commons.linkTitle')"/>:</label>
							</td>
							<td>
								<input type="text" id="textASSRWebpageLinkTitle" />
							</td>
						</tr>
					</s:else>
				</s:if>
				<s:else>
					<tr>
						<td>
							<label for="textASSRWebpage"><s:property value="getText('label.ai.tabs.commons.webpage')"/>:</label>
						</td>
						<td>
							<input type="text" id="textASSRWebpage" />
						</td>
						<td class="labelLeft">
							<label for="textASSRWebpageLinkTitle"><s:property value="getText('label.ai.tabs.commons.linkTitle')"/>:</label>
						</td>
						<td>
							<input type="text" id="textASSRWebpageLinkTitle" />
						</td>
					</tr>
				</s:else>

				<s:if test="%{loader.asSearchRoomWorkPlaces.size() > 0}">
					<s:set var="srWorkPlaces" value="loader.asSearchRoomWorkPlaces[#counter]"/>
					<s:if test="%{#srWorkPlaces.size() > 0}">
						<s:iterator var="internalCurrent" value="#srWorkPlaces" status="internalStatus">
							<tr>
								<td>
									<s:if test="%{#internalStatus.index > 0}">
										<label for="textASSRWorkPlaces_<s:property value="%{#internalStatus.index + 1}" />"><s:property value="getText('label.ai.accessAndServices.workPlaces')" />:</label>
									</s:if>
									<s:else>
										<label for="textASSRWorkPlaces"><s:property value="getText('label.ai.accessAndServices.workPlaces')" />:</label>
									</s:else>
								</td>
								<td>
									<s:if test="%{#internalStatus.index > 0}">
										<input type="text" id="textASSRWorkPlaces_<s:property value="%{#internalStatus.index + 1}" />" value="<s:property value="#internalCurrent" />" />
									</s:if>
									<s:else>
										<input type="text" id="textASSRWorkPlaces" value="<s:property value="#internalCurrent" />" />
									</s:else>
								</td>
								<td colspan="2">
								</td>
							</tr>
						</s:iterator>
					</s:if>
					<s:else>
						<tr>
							<td>
								<label for="textASSRWorkPlaces"><s:property value="getText('label.ai.accessAndServices.workPlaces')"/>:</label>
							</td>
							<td>
								<input type="text" id="textASSRWorkPlaces" />
							</td>
							<td colspan="2">
							</td>
						</tr>
					</s:else>
				</s:if>
				<s:else>
					<tr>
						<td>
							<label for="textASSRWorkPlaces"><s:property value="getText('label.ai.accessAndServices.workPlaces')"/>:</label>
						</td>
						<td>
							<input type="text" id="textASSRWorkPlaces" />
						</td>
						<td colspan="2">
						</td>
					</tr>
				</s:else>

				<s:if test="%{loader.asSearchRoomComputerPlaces.size() > 0}">
					<s:set var="srComputerPlaces" value="loader.asSearchRoomComputerPlaces[#counter]"/>
					<s:if test="%{#srComputerPlaces.size() > 0}">
						<s:iterator var="internalCurrent" value="#srComputerPlaces" status="internalStatus">
							<tr>
								<td>
									<s:if test="%{#internalStatus.index > 0}">
										<label for="textASSRComputerPlaces_<s:property value="%{#internalStatus.index + 1}" />"><s:property value="getText('label.ai.accessAndServices.computerPlaces')" />:</label>
									</s:if>
									<s:else>
										<label for="textASSRComputerPlaces"><s:property value="getText('label.ai.accessAndServices.computerPlaces')" />:</label>
									</s:else>
								</td>
								<td>
									<s:if test="%{#internalStatus.index > 0}">
										<input type="text" id="textASSRComputerPlaces_<s:property value="%{#internalStatus.index + 1}" />" value="<s:property value="#internalCurrent" />" />
									</s:if>
									<s:else>
										<input type="text" id="textASSRComputerPlaces" value="<s:property value="#internalCurrent" />" />
									</s:else>
								</td>
								<td colspan="2">
								</td>
							</tr>
						</s:iterator>
					</s:if>
					<s:else>
						<tr>
							<td>
								<label for="textASSRComputerPlaces"><s:property value="getText('label.ai.accessAndServices.computerPlaces')"/>:</label>
							</td>
							<td>
								<input type="text" id="textASSRComputerPlaces" />
							</td>
							<td colspan="2">
							</td>
						</tr>
					</s:else>
				</s:if>
				<s:else>
					<tr>
						<td>
							<label for="textASSRComputerPlaces"><s:property value="getText('label.ai.accessAndServices.computerPlaces')"/>:</label>
						</td>
						<td>
							<input type="text" id="textASSRComputerPlaces" />
						</td>
						<td colspan="2">
						</td>
					</tr>
				</s:else>

				<s:if test="%{loader.asSearchRoomComputerPlacesDescription.size() > 0}">
					<s:set var="srComputerPlacesDescription" value="loader.asSearchRoomComputerPlacesDescription[#counter]"/>
					<s:set var="srComputerPlacesDescriptionLang" value="loader.asSearchRoomComputerPlacesDescriptionLang[#counter]"/>
					<s:if test="%{#srComputerPlacesDescription.size() > 0}">
						<s:iterator var="internalCurrent" value="#srComputerPlacesDescription" status="internalStatus">
							<tr id="trASSRDescriptionOfYourComputerPlaces_<s:property value="%{#internalStatus.index + 1}" />">
								<td id="tdDescriptionOfYourComputerPlaces_<s:property value="%{#internalStatus.index + 1}" />">
									<label for="textDescriptionOfYourComputerPlaces_<s:property value="%{#internalStatus.index + 1}" />"><s:property value="getText('label.ai.accessAndServices.descriptionOfComputerPlaces')" />:</label>
								</td>
								<td>
									<input type="text" id="textDescriptionOfYourComputerPlaces_<s:property value="%{#internalStatus.index + 1}" />" value="<s:property value="#internalCurrent" />" />
								</td>
								<td id="tdSelectDescriptionOfYourComputerPlaces_<s:property value="%{#internalStatus.index + 1}" />" class="labelLeft">
									<label for="selectDescriptionOfYourComputerPlaces_<s:property value="%{#internalStatus.index + 1}" />"><s:property value="getText('label.ai.tabs.commons.selectLanguage')" />:</label>
								</td>
								<td>
									<select id="selectDescriptionOfYourComputerPlaces_<s:property value="%{#internalStatus.index + 1}" />">
										<s:iterator value="languageList" var="language"> 
											<option value="<s:property value="#language.key" />"<s:if test="%{#language.key == #srComputerPlacesDescriptionLang[#internalStatus.index]}" > selected=selected </s:if>><s:property value="#language.value" /></option>
										</s:iterator>
									</select>
								</td>
							</tr>
						</s:iterator>
						<tr id="trASSRAddDescriptionOfYourComputerPlaces">
							<td colspan="3">
								<input type="button" id="buttonASSRAddadescriptionofyourcomputerplaces" value="<s:property value="getText('label.ai.accessAndServices.addDescriptionOfYourComputerPlaces')"/>" onclick="aSSRAddDescriptionOfYourComputerPlaces('<s:property value="getText('label.ai.accessAndServices.descriptionOfComputerPlaces')"/>', '<s:property value="getText('label.ai.tabs.commons.selectLanguage')" />', '<s:property value="getText('label.ai.tabs.commons.pleaseFillData')" />');" />
							</td>
							<td>
							</td>
						</tr>
					</s:if>
					<s:else>
						<tr id="trASSRAddDescriptionOfYourComputerPlaces">
							<td colspan="3">
								<input type="button" id="buttonASSRAddadescriptionofyourcomputerplaces" value="<s:property value="getText('label.ai.accessAndServices.addDescriptionOfYourComputerPlaces')"/>" onclick="aSSRAddDescriptionOfYourComputerPlaces('<s:property value="getText('label.ai.accessAndServices.descriptionOfComputerPlaces')"/>', '<s:property value="getText('label.ai.tabs.commons.selectLanguage')" />', '<s:property value="getText('label.ai.tabs.commons.pleaseFillData')" />');" />
							</td>
							<td>
							</td>
						</tr>
					</s:else>
				</s:if>
				<s:else>
					<tr id="trASSRAddDescriptionOfYourComputerPlaces">
						<td colspan="3">
							<input type="button" id="buttonASSRAddadescriptionofyourcomputerplaces" value="<s:property value="getText('label.ai.accessAndServices.addDescriptionOfYourComputerPlaces')"/>" onclick="aSSRAddDescriptionOfYourComputerPlaces('<s:property value="getText('label.ai.accessAndServices.descriptionOfComputerPlaces')"/>', '<s:property value="getText('label.ai.tabs.commons.selectLanguage')" />', '<s:property value="getText('label.ai.tabs.commons.pleaseFillData')" />');" />
						</td>
						<td>
						</td>
					</tr>
				</s:else>

				<tr>
					<td>
						<label for="textASSRMicrofilmPlaces"><s:property value="getText('label.ai.accessAndServices.microfilmPlaces')"/>:</label>
					</td>
					<td>
						<s:if test="%{loader.asSearchRoomMicrofilmReaders.size() > 0}">
							<s:set var="srMicrofilmReaders" value="loader.asSearchRoomMicrofilmReaders[#counter]"/>
							<s:if test="%{#srMicrofilmReaders.size() > 0}">
								<input type="text" id="textASSRMicrofilmPlaces" value="<s:property value="#srMicrofilmReaders[0]" />" />
							</s:if>
							<s:else>
								<input type="text" id="textASSRMicrofilmPlaces" />
							</s:else>
						</s:if>
						<s:else>
							<input type="text" id="textASSRMicrofilmPlaces" />
						</s:else>
					</td>
					<td class="labelLeft">
						<label for="selectASSRPhotographAllowance"><s:property value="getText('label.ai.accessAndServices.photographAllowance')"/>:</label>
					</td>
					<td>
						<select id="selectASSRPhotographAllowance">
							<s:iterator value="photographList" var="photograph">
								<s:if test="%{loader.asSearchRoomPhotographAllowance.size() > 0}">
									<s:set var="srPhotographAllowance" value="loader.asSearchRoomPhotographAllowance[#counter]"/>
									<s:if test="%{#srPhotographAllowance.size() > 0}">
										<option value="<s:property value="#photograph.key" />"
											<s:if test="%{#photograph.key == #srPhotographAllowance[0]}"> selected=selected </s:if>>
											<s:property value="#photograph.value" />
										</option>
									</s:if>
									<s:else>
										<option value="<s:property value="#photograph.key" />">
											<s:property value="#photograph.value" />
										</option>
									</s:else>
								</s:if>
								<s:else>
									<option value="<s:property value="#photograph.key" />">
										<s:property value="#photograph.value" />
									</option>
								</s:else>
							</s:iterator>
						</select>
					</td>
				</tr>

				<s:if test="%{loader.asSearchRoomNumberOfReadersTicket.size() > 0}">
					<s:set var="numberOfReadersTicket" value="loader.asSearchRoomNumberOfReadersTicket[#counter]"/>
					<s:set var="readersTicketContent" value="loader.asSearchRoomReadersTicketContent[#counter]"/>
					<s:set var="readersTicketLang" value="loader.asSearchRoomReadersTicketLang[#counter]"/>
					<s:set var="readersTicketHref" value="loader.asSearchRoomReadersTicketHref[#counter]"/>
					<s:if test="%{#numberOfReadersTicket.size() > 0}">
						<s:iterator var="internalCurrent" value="#numberOfReadersTicket" status="internalStatus">
							<tr id="trASSRReadersTicket_<s:property value="%{#internalStatus.index + 1}" />">
								<td>
									<label for="textASSRReadersTicket_<s:property value="%{#internalStatus.index + 1}" />"><s:property value="getText('label.ai.accessAndServices.readersTicket')" />:</label>
								</td>
								<td>
									<input type="text" id="textASSRReadersTicket_<s:property value="%{#internalStatus.index + 1}" />" value="<s:property value="#readersTicketContent[#internalStatus.index]" />" />
								</td>
								<td class="labelLeft">
									<label for="selectReadersTickectLanguage_<s:property value="%{#internalStatus.index + 1}" />" ><s:property value="getText('label.ai.tabs.commons.selectLanguage')" />:</label>
								</td>
								<td>
									<select id="selectReadersTickectLanguage_<s:property value="%{#internalStatus.index + 1}" />">
										<s:iterator value="languageList" var="language"> 
											<option value="<s:property value="#language.key" />"<s:if test="%{#language.key == #readersTicketLang[#internalStatus.index]}" > selected=selected </s:if>><s:property value="#language.value" /></option>
										</s:iterator>
									</select>
								</td>
							</tr>

							<tr id="tr2ASSRReadersTicket_<s:property value="%{#internalStatus.index + 1}" />">
								<td>
									<label for="textASSRRTLink_<s:property value="%{#internalStatus.index + 1}" />"><s:property value="getText('label.ai.accessAndServices.link')"/>:</label>
								</td>
								<td>
									<input type="text" id="textASSRRTLink_<s:property value="%{#internalStatus.index + 1}" />" value="<s:property value="#readersTicketHref[#internalStatus.index]" />" />
								</td>
								<td colspan="2">
								</td>
							</tr>
						</s:iterator>
					</s:if>
					<s:else>
						<tr id="trASSRReadersTicket_1">
							<td>
								<label for="textASSRReadersTicket_1"><s:property value="getText('label.ai.accessAndServices.readersTicket')"/>:</label>
							</td>
							<td>
								<input type="text" id="textASSRReadersTicket_1" />
							</td>
							<td class="labelLeft">
								<label for="selectReadersTickectLanguage_1"><s:property value="getText('label.ai.tabs.commons.selectLanguage')"/>:</label>
							</td>
							<td>
								<select id="selectReadersTickectLanguage_1">
									<s:iterator value="languageList" var="language"> 
										<option value="<s:property value="#language.key" />"><s:property value="#language.value" /></option>
									</s:iterator>
								</select>
							</td>
						</tr>

						<tr id="tr2ASSRReadersTicket_1">
							<td>
								<label for="textASSRRTLink_1"><s:property value="getText('label.ai.accessAndServices.link')"/>:</label>
							</td>
							<td>
								<input type="text" id="textASSRRTLink_1" />
							</td>
							<td colspan="2">
							</td>
						</tr>
					</s:else>
				</s:if>
				<s:else>
					<tr id="trASSRReadersTicket_1">
						<td>
							<label for="textASSRReadersTicket_1"><s:property value="getText('label.ai.accessAndServices.readersTicket')"/>:</label>
						</td>
						<td>
							<input type="text" id="textASSRReadersTicket_1" />
						</td>
						<td class="labelLeft">
							<label for="selectReadersTickectLanguage_1"><s:property value="getText('label.ai.tabs.commons.selectLanguage')"/>:</label>
						</td>
						<td>
							<select id="selectReadersTickectLanguage_1">
								<s:iterator value="languageList" var="language"> 
									<option value="<s:property value="#language.key" />"><s:property value="#language.value" /></option>
								</s:iterator>
							</select>
						</td>
					</tr>

					<tr id="tr2ASSRReadersTicket_1">
						<td>
							<label for="textASSRRTLink_1"><s:property value="getText('label.ai.accessAndServices.link')"/>:</label>
						</td>
						<td>
							<input type="text" id="textASSRRTLink_1" />
						</td>
						<td colspan="2">
						</td>
					</tr>
				</s:else>

				<tr>
					<td colspan="2">
						<input type="button" id="buttonASSRAddReadersTicket" value="<s:property value="getText('label.ai.accessAndServices.addReadersTicket')"/>" onclick="aSSRAddReadersTicket('<s:property value="getText('label.ai.tabs.commons.pleaseFillData')" />');"/>
					</td>
					<td colspan="2">
					</td>
				</tr>

				<s:if test="%{loader.asSearchRoomNumberOfAdvancedOrders.size() > 0}">
					<s:set var="numberOfAdvancedOrders" value="loader.asSearchRoomNumberOfAdvancedOrders[#counter]"/>
					<s:set var="advancedOrdersContent" value="loader.asSearchRoomAdvancedOrdersContent[#counter]"/>
					<s:set var="advancedOrdersLang" value="loader.asSearchRoomAdvancedOrdersLang[#counter]"/>
					<s:set var="advancedOrdersHref" value="loader.asSearchRoomAdvancedOrdersHref[#counter]"/>
					<s:if test="%{#numberOfAdvancedOrders.size() > 0}">
						<s:iterator var="internalCurrent" value="#numberOfAdvancedOrders" status="internalStatus">
							<tr id="trASSRAddFurtherOrderInformation_<s:property value="%{#internalStatus.index + 1}" />">
								<td>
									<label for="textASSRAdvancedOrders_<s:property value="%{#internalStatus.index + 1}" />"><s:property value="getText('label.ai.accessAndServices.advancedOrders')" />:</label>
								</td>
								<td>
									<input type="text" id="textASSRAdvancedOrders_<s:property value="%{#internalStatus.index + 1}" />" value="<s:property value="#advancedOrdersContent[#internalStatus.index]" />" />
								</td>
								<td class="labelLeft">
									<label for="selectASSRAFOIUSelectLanguage_<s:property value="%{#internalStatus.index + 1}" />" ><s:property value="getText('label.ai.tabs.commons.selectLanguage')" />:</label>
								</td>
								<td>
									<select id="selectASSRAFOIUSelectLanguage_<s:property value="%{#internalStatus.index + 1}" />">
										<s:iterator value="languageList" var="language"> 
											<option value="<s:property value="#language.key" />"<s:if test="%{#language.key == #advancedOrdersLang[#internalStatus.index]}" > selected=selected </s:if>><s:property value="#language.value" /></option>
										</s:iterator>
									</select>
								</td>
							</tr>

							<tr id="tr2ASSRAddFurtherOrderInformation_<s:property value="%{#internalStatus.index + 1}" />">
								<td>
									<label for="textASSRAOLink_<s:property value="%{#internalStatus.index + 1}" />"><s:property value="getText('label.ai.accessAndServices.link')"/>:</label>
								</td>
								<td>
									<input type="text" id="textASSRAOLink_<s:property value="%{#internalStatus.index + 1}" />" value="<s:property value="#advancedOrdersHref[#internalStatus.index]" />" />
								</td>
								<td colspan="2">
								</td>
							</tr>
						</s:iterator>
					</s:if>
					<s:else>
						<tr id="trASSRAddFurtherOrderInformation_1">
							<td>
								<label for="textASSRAdvancedOrders_1"><s:property value="getText('label.ai.accessAndServices.advancedOrders')" />:</label>
							</td>
							<td>
								<input type="text" id="textASSRAdvancedOrders_1" />
							</td>
							<td class="labelLeft">
								<label for="selectASSRAFOIUSelectLanguage_1"><s:property value="getText('label.ai.tabs.commons.selectLanguage')"/>:</label>
							</td>
							<td>
								<select id="selectASSRAFOIUSelectLanguage_1">
									<s:iterator value="languageList" var="language"> 
										<option value="<s:property value="#language.key" />"><s:property value="#language.value" /></option>
									</s:iterator>
								</select>
							</td>
						</tr>

						<tr id="tr2ASSRAddFurtherOrderInformation_1">
							<td>
								<label for="textASSRAOLink_1"><s:property value="getText('label.ai.accessAndServices.link')" />:</label>
							</td>
							<td>
								<input type="text" id="textASSRAOLink_1" />
							</td>
							<td colspan="2">
							</td>
						</tr>
					</s:else>
				</s:if>
				<s:else>
					<tr id="trASSRAddFurtherOrderInformation_1">
						<td>
							<label for="textASSRAdvancedOrders_1"><s:property value="getText('label.ai.accessAndServices.advancedOrders')" />:</label>
						</td>
						<td>
							<input type="text" id="textASSRAdvancedOrders_1" />
						</td>
						<td class="labelLeft">
							<label for="selectASSRAFOIUSelectLanguage_1"><s:property value="getText('label.ai.tabs.commons.selectLanguage')"/>:</label>
						</td>
						<td>
							<select id="selectASSRAFOIUSelectLanguage_1">
								<s:iterator value="languageList" var="language"> 
									<option value="<s:property value="#language.key" />"><s:property value="#language.value" /></option>
								</s:iterator>
							</select>
						</td>
					</tr>

					<tr id="tr2ASSRAddFurtherOrderInformation_1">
						<td>
							<label for="textASSRAOLink_1"><s:property value="getText('label.ai.accessAndServices.link')" />:</label>
						</td>
						<td>
							<input type="text" id="textASSRAOLink_1" />
						</td>
						<td colspan="2">
						</td>
					</tr>
				</s:else>
		
				<tr>
					<td colspan="2">
						<input type="button" id="buttonASSRAddFurtherOrderInformation" value="<s:property value="getText('label.ai.accessAndServices.addFurtherOrderInformation')" />" onclick="aSSRAddFurtherOrderInformation('<s:property value="getText('label.ai.tabs.commons.pleaseFillData')" />');" />
					</td>
					<td colspan="2">
					</td>
				</tr>

				<s:if test="%{loader.asSearchRoomResearchServicesContent.size() > 0}">
					<s:set var="researchServicesContent" value="loader.asSearchRoomResearchServicesContent[#counter]"/>
					<s:set var="researchServicesLang" value="loader.asSearchRoomResearchServicesLang[#counter]"/>
					<s:if test="%{#researchServicesContent.size() > 0}">
						<s:iterator var="internalCurrent" value="#researchServicesContent" status="internalStatus">
							<tr id="trASSRResearchServices_<s:property value="%{#internalStatus.index + 1}" />">
								<td>
									<label for="textASSRResearchServices_<s:property value="%{#internalStatus.index + 1}" />"><s:property value="getText('label.ai.accessAndServices.researchServices')" />:</label>
								</td>
								<td>
									<input type="text" id="textASSRResearchServices_<s:property value="%{#internalStatus.index + 1}" />" value="<s:property value="#internalCurrent" />" />
								</td>
								<td class="labelLeft">
									<label for="textASSRRSSelectLanguage_<s:property value="%{#internalStatus.index + 1}" />" ><s:property value="getText('label.ai.tabs.commons.selectLanguage')" />:</label>
								</td>
								<td>
									<select id="textASSRRSSelectLanguage_<s:property value="%{#internalStatus.index + 1}" />">
										<s:iterator value="languageList" var="language"> 
											<option value="<s:property value="#language.key" />"<s:if test="%{#language.key == #researchServicesLang[#internalStatus.index]}" > selected=selected </s:if>><s:property value="#language.value" /></option>
										</s:iterator>
									</select>
								</td>
							</tr>
						</s:iterator>
					</s:if>
					<s:else>
						<tr id="trASSRResearchServices_1">
							<td>
								<label for="textASSRResearchServices_1"><s:property value="getText('label.ai.accessAndServices.researchServices')"/>:</label>
							</td>
							<td>
								<input type="text" id="textASSRResearchServices_1" />
							</td>
							<td class="labelLeft">
								<label for="textASSRRSSelectLanguage_1"><s:property value="getText('label.ai.tabs.commons.selectLanguage')"/>:</label>
							</td>
							<td>
								<select id="textASSRRSSelectLanguage_1">
									<s:iterator value="languageList" var="language"> 
										<option value="<s:property value="#language.key" />"><s:property value="#language.value" /></option>
									</s:iterator>
								</select>
							</td>
						</tr>
					</s:else>
				</s:if>
				<s:else>
					<tr id="trASSRResearchServices_1">
						<td>
							<label for="textASSRResearchServices_1"><s:property value="getText('label.ai.accessAndServices.researchServices')"/>:</label>
						</td>
						<td>
							<input type="text" id="textASSRResearchServices_1" />
						</td>
						<td class="labelLeft">
							<label for="textASSRRSSelectLanguage_1"><s:property value="getText('label.ai.tabs.commons.selectLanguage')"/>:</label>
						</td>
						<td>
							<select id="textASSRRSSelectLanguage_1">
								<s:iterator value="languageList" var="language"> 
									<option value="<s:property value="#language.key" />"><s:property value="#language.value" /></option>
								</s:iterator>
							</select>
						</td>
					</tr>
				</s:else>

				<tr>
					<td colspan="2">
						<input type="button" id="buttonASAddResearchServices" value="<s:property value="getText('label.ai.accessAndServices.addResearchServices')"/>" onclick="aSAddResearchServices('<s:property value="getText('label.ai.tabs.commons.pleaseFillData')" />');" />
					</td>
					<td colspan="2">
					</td>
				</tr>

				<tr>
					<td id="libraryLabel">
						<span><s:property value="getText('label.ai.accessAndServices.library')" /></span>
					</td>
					<td>
						<select id="selectASLibrary">
							<s:iterator value="noneYesNoList" var="yesno">
								<s:if test="%{loader.asLibraryQuestion.size() > 0}">
									<s:set var="libraryQuestion" value="loader.asLibraryQuestion[#counter]"/>
									<s:if test="%{#libraryQuestion.size() > 0}">
										<option value="<s:property value="#yesno.key" />"
											<s:if test="%{#yesno.key == #libraryQuestion[0]}"> selected=selected </s:if>>
											<s:property value="#yesno.value" />
										</option>
									</s:if>
									<s:else>
										<option value="<s:property value="#yesno.key" />">
											<s:property value="#yesno.value" />
										</option>
									</s:else>
								</s:if>
								<s:else>
									<option value="<s:property value="#yesno.key" />">
										<s:property value="#yesno.value" />
									</option>
								</s:else>
							</s:iterator>
						</select>
					</td>
					<td colspan="2">
					</td>
				</tr>

				<s:if test="%{loader.asLibraryTelephone.size() > 0}">
					<s:set var="libraryTelephone" value="loader.asLibraryTelephone[#counter]"/>
					<s:if test="%{#libraryTelephone.size() > 0}">
						<s:iterator var="internalCurrent" value="#libraryTelephone" status="internalStatus">
							<tr>
								<td>
									<label for="textASLTelephone_<s:property value="%{#internalStatus.index + 1}" />"><s:property value="getText('label.ai.tabs.commons.telephone')" />:</label>
								</td>
								<td>
									<input type="text" id="textASLTelephone_<s:property value="%{#internalStatus.index + 1}" />" value="<s:property value="#internalCurrent" />" />
								</td>
								<td colspan="2">
								</td>
							</tr>
						</s:iterator>
					</s:if>
					<s:else>
						<tr>
							<td>
								<label for="textASLTelephone_1"><s:property value="getText('label.ai.tabs.commons.telephone')" />:</label>
							</td>
							<td>
								<input type="text" id="textASLTelephone_1" />
							</td>
							<td colspan="2">
							</td>
						</tr>
					</s:else>
				</s:if>
				<s:else>
					<tr>
						<td>
							<label for="textASLTelephone_1"><s:property value="getText('label.ai.tabs.commons.telephone')" />:</label>
						</td>
						<td>
							<input type="text" id="textASLTelephone_1" />
						</td>
						<td colspan="2">
						</td>
					</tr>
				</s:else>

				<s:if test="%{loader.asLibraryNumberOfEmail.size() > 0}">
					<s:set var="libNumberOfEmail" value="loader.asLibraryNumberOfEmail[#counter]"/>
					<s:set var="libEmailHref" value="loader.asLibraryEmailHref[#counter]"/>
					<s:set var="libEmailTitle" value="loader.asLibraryEmailTitle[#counter]"/>
					<s:if test="%{#libNumberOfEmail.size() > 0}">
						<s:iterator var="internalCurrent" value="#libNumberOfEmail" status="internalStatus">
							<tr>
								<td>
									<s:if test="%{#internalStatus.index > 0}">
										<label for="textASLEmailAddress_<s:property value="%{#internalStatus.index + 1}" />"><s:property value="getText('label.ai.tabs.commons.emailAddress')" />:</label>
									</s:if>
									<s:else>
										<label for="textASLEmailAddress"><s:property value="getText('label.ai.tabs.commons.emailAddress')" />:</label>
									</s:else>
								</td>
								<td>
									<s:if test="%{#internalStatus.index > 0}">
										<input type="text" id="textASLEmailAddress_<s:property value="%{#internalStatus.index + 1}" />" value="<s:property value="#libEmailHref[#internalStatus.index]" />" />
									</s:if>
									<s:else>
										<input type="text" id="textASLEmailAddress" value="<s:property value="#libEmailHref[#internalStatus.index]" />" />
									</s:else>
								</td>
								<td class="labelLeft">
									<s:if test="%{#internalStatus.index > 0}">
										<label for="textASLEmailLinkTitle_<s:property value="%{#internalStatus.index + 1}" />"><s:property value="getText('label.ai.tabs.commons.linkTitle')" />:</label>
									</s:if>
									<s:else>
										<label for="textASLEmailLinkTitle"><s:property value="getText('label.ai.tabs.commons.linkTitle')"/>:</label>
									</s:else>
								</td>
								<td>
									<s:if test="%{#internalStatus.index > 0}">
										<input type="text" id="textASLEmailLinkTitle_<s:property value="%{#internalStatus.index + 1}" />" value="<s:property value="#libEmailTitle[#internalStatus.index]" />" />
									</s:if>
									<s:else>
										<input type="text" id="textASLEmailLinkTitle" value="<s:property value="#libEmailTitle[#internalStatus.index]" />" />
									</s:else>
								</td>
							</tr>
						</s:iterator>
					</s:if>
					<s:else>
						<tr>
							<td>
								<label for="textASLEmailAddress"><s:property value="getText('label.ai.tabs.commons.emailAddress')" />:</label>
							</td>
							<td>
								<input type="text" id="textASLEmailAddress" />
							</td>
							<td class="labelLeft">
								<label for="textASLEmailLinkTitle"><s:property value="getText('label.ai.tabs.commons.linkTitle')"/>:</label>
							</td>
							<td>
								<input type="text" id="textASLEmailLinkTitle" />
							</td>
						</tr>
					</s:else>
				</s:if>
				<s:else>
					<tr>
						<td>
							<label for="textASLEmailAddress"><s:property value="getText('label.ai.tabs.commons.emailAddress')" />:</label>
						</td>
						<td>
							<input type="text" id="textASLEmailAddress" />
						</td>
						<td class="labelLeft">
							<label for="textASLEmailLinkTitle"><s:property value="getText('label.ai.tabs.commons.linkTitle')"/>:</label>
						</td>
						<td>
							<input type="text" id="textASLEmailLinkTitle" />
						</td>
					</tr>
				</s:else>

				<s:if test="%{loader.asLibraryNumberOfWebpage.size() > 0}">
					<s:set var="libNumberOfWebpage" value="loader.asLibraryNumberOfWebpage[#counter]"/>
					<s:set var="libWebpageHref" value="loader.asLibraryWebpageHref[#counter]"/>
					<s:set var="libWebpageTitle" value="loader.asLibraryWebpageTitle[#counter]"/>
					<s:if test="%{#libNumberOfWebpage.size() > 0}">
						<s:iterator var="internalCurrent" value="#libNumberOfWebpage" status="internalStatus">
							<tr>
								<td>
									<s:if test="%{#internalStatus.index > 0}">
										<label for="textASLWebpage_<s:property value="%{#internalStatus.index + 1}" />"><s:property value="getText('label.ai.tabs.commons.webpage')" />:</label>
									</s:if>
									<s:else>
										<label for="textASLWebpage"><s:property value="getText('label.ai.tabs.commons.webpage')" />:</label>
									</s:else>
								</td>
								<td>
									<s:if test="%{#internalStatus.index > 0}">
										<input type="text" id="textASLWebpage_<s:property value="%{#internalStatus.index + 1}" />" value="<s:property value="#libWebpageHref[#internalStatus.index]" />" />
									</s:if>
									<s:else>
										<input type="text" id="textASLWebpage" value="<s:property value="#libWebpageHref[#internalStatus.index]" />" />
									</s:else>
								</td>
								<td class="labelLeft">
									<s:if test="%{#internalStatus.index > 0}">
										<label for="textASLWebpageLinkTitle_<s:property value="%{#internalStatus.index + 1}" />"><s:property value="getText('label.ai.tabs.commons.linkTitle')" />:</label>
									</s:if>
									<s:else>
										<label for="textASLWebpageLinkTitle"><s:property value="getText('label.ai.tabs.commons.linkTitle')"/>:</label>
									</s:else>
								</td>
								<td>
									<s:if test="%{#internalStatus.index > 0}">
										<input type="text" id="textASLWebpageLinkTitle_<s:property value="%{#internalStatus.index + 1}" />" value="<s:property value="#libWebpageTitle[#internalStatus.index]" />" />
									</s:if>
									<s:else>
										<input type="text" id="textASLWebpageLinkTitle" value="<s:property value="#libWebpageTitle[#internalStatus.index]" />" />
									</s:else>
								</td>
							</tr>
						</s:iterator>
					</s:if>
					<s:else>
						<tr>
							<td>
								<label for="textASLWebpage"><s:property value="getText('label.ai.tabs.commons.webpage')" />:</label>
							</td>
							<td>
								<input type="text" id="textASLWebpage" />
							</td>
							<td class="labelLeft">
								<label for="textASLWebpageLinkTitle"><s:property value="getText('label.ai.tabs.commons.linkTitle')"/>:</label>
							</td>
							<td>
								<input type="text" id="textASLWebpageLinkTitle" />
							</td>
						</tr>
					</s:else>
				</s:if>
				<s:else>
					<tr>
						<td>
							<label for="textASLWebpage"><s:property value="getText('label.ai.tabs.commons.webpage')" />:</label>
						</td>
						<td>
							<input type="text" id="textASLWebpage" />
						</td>
						<td class="labelLeft">
							<label for="textASLWebpageLinkTitle"><s:property value="getText('label.ai.tabs.commons.linkTitle')"/>:</label>
						</td>
						<td>
							<input type="text" id="textASLWebpageLinkTitle" />
						</td>
					</tr>
				</s:else>

				<tr>
					<td>
						<label for="textASLMonographocPublication"><s:property value="getText('label.ai.accessAndServices.monographicPublication')" />:</label>
					</td>
					<td>
						<s:if test="%{loader.asLibraryMonographPublication.size() > 0}">
							<s:set var="libMonographPublication" value="loader.asLibraryMonographPublication[#counter]"/>
							<s:if test="%{#libMonographPublication.size() > 0}">
								<input type="text" id="textASLMonographocPublication" value="<s:property value="#libMonographPublication[0]" />" />
							</s:if>
							<s:else>
								<input type="text" id="textASLMonographocPublication" />
							</s:else>
						</s:if>
						<s:else>
							<input type="text" id="textASLMonographocPublication" />
						</s:else>
					</td>
					<td class="labelLeft">
						<label for="textASLSerialPublication"><s:property value="getText('label.ai.accessAndServices.serialPublication')"/>:</label>
					</td>
					<td>
						<s:if test="%{loader.asLibrarySerialPublication.size() > 0}">
							<s:set var="libSerialPublication" value="loader.asLibrarySerialPublication[#counter]"/>
							<s:if test="%{#libSerialPublication.size() > 0}">
								<input type="text" id="textASLSerialPublication" value="<s:property value="#libSerialPublication[0]" />" />
							</s:if>
							<s:else>
								<input type="text" id="textASLSerialPublication" />
							</s:else>
						</s:if>
						<s:else>
							<input type="text" id="textASLSerialPublication" />
						</s:else>
					</td>
				</tr>

				<tr>
					<td id="internetAccessLabel">
						<label for="selectASInternetAccess"><span><s:property value="getText('label.ai.accessAndServices.internetAccess')" /></span>:</label>
					</td>
					<td>
						<select id="selectASInternetAccess">
							<s:iterator value="noneYesNoList" var="yesno">
								<s:if test="%{loader.asInternetAccessQuestion.size() > 0}">
									<s:set var="internetAccessQuestion" value="loader.asInternetAccessQuestion[#counter]"/>
									<s:if test="%{#internetAccessQuestion.size() > 0}">
										<option value="<s:property value="#yesno.key" />"
											<s:if test="%{#yesno.key == #internetAccessQuestion[0]}"> selected=selected </s:if>>
											<s:property value="#yesno.value" />
										</option>
									</s:if>
									<s:else>
										<option value="<s:property value="#yesno.key" />">
											<s:property value="#yesno.value" />
										</option>
									</s:else>
								</s:if>
								<s:else>
									<option value="<s:property value="#yesno.key" />">
										<s:property value="#yesno.value" />
									</option>
								</s:else>
							</s:iterator>
						</select>
					</td>
					<td colspan="2">
					</td>
				</tr>

				<s:if test="%{loader.asInternetAccessDescription.size() > 0}">
					<s:set var="internetAccessDescription" value="loader.asInternetAccessDescription[#counter]"/>
					<s:set var="internetAccessDescriptionLang" value="loader.asInternetAccessDescriptionLang[#counter]"/>
					<s:if test="%{#internetAccessDescription.size() > 0}">
						<s:iterator var="internalCurrent" value="#internetAccessDescription" status="internalStatus">
							<tr id="trASPIAAddInternetAccessInformation_<s:property value="%{#internalStatus.index + 1}" />">
								<td>
									<label for="textASDescription_<s:property value="%{#internalStatus.index + 1}" />"><s:property value="getText('label.ai.accessAndServices.description')" />:</label>
								</td>
								<td>
									<input type="text" id="textASDescription_<s:property value="%{#internalStatus.index + 1}" />" value="<s:property value="#internalCurrent" />" />
								</td>
								<td class="labelLeft">
									<label for="selectASDSelectLanguage_<s:property value="%{#internalStatus.index + 1}" />" ><s:property value="getText('label.ai.tabs.commons.selectLanguage')" />:</label>
								</td>
								<td>
									<select id="selectASDSelectLanguage_<s:property value="%{#internalStatus.index + 1}" />">
										<s:iterator value="languageList" var="language"> 
											<option value="<s:property value="#language.key" />"<s:if test="%{#language.key == #internetAccessDescriptionLang[#internalStatus.index]}" > selected=selected </s:if>><s:property value="#language.value" /></option>
										</s:iterator>
									</select>
								</td>
							</tr>
						</s:iterator>
					</s:if>
					<s:else>
						<tr id="trASPIAAddInternetAccessInformation_1">
							<td>
								<label for="textASDescription_1"><span><s:property value="getText('label.ai.accessAndServices.description')" /></span>:</label>
							</td>
							<td>
								<input type="text" id="textASDescription_1" />
							</td>
							<td class="labelLeft">
								<label for="selectASDSelectLanguage_1"><s:property value="getText('label.ai.tabs.commons.selectLanguage')"/>:</label>
							</td>
							<td>
								<select id="selectASDSelectLanguage_1">
									<s:iterator value="languageList" var="language">
										<option value="<s:property value="#language.key" />"><s:property value="#language.value" /></option>
									</s:iterator>
								</select>
							</td>
						</tr>
					</s:else>
				</s:if>
				<s:else>
					<tr id="trASPIAAddInternetAccessInformation_1">
						<td>
							<label for="textASDescription_1"><span><s:property value="getText('label.ai.accessAndServices.description')" /></span>:</label>
						</td>
						<td>
							<input type="text" id="textASDescription_1" />
						</td>
						<td class="labelLeft">
							<label for="selectASDSelectLanguage_1"><s:property value="getText('label.ai.tabs.commons.selectLanguage')"/>:</label>
						</td>
						<td>
							<select id="selectASDSelectLanguage_1">
								<s:iterator value="languageList" var="language">
									<option value="<s:property value="#language.key" />"><s:property value="#language.value" /></option>
								</s:iterator>
							</select>
						</td>
					</tr>
				</s:else>

				<tr>
					<td colspan="3">
						<input type="button" id="buttonASPIAAddInternetAccessInformation" value="<s:property value="getText('label.ai.accessAndServices.addInternetAccessInformation')" />" onclick="aSPIAAddInternetAccessInformation('<s:property value="getText('label.ai.tabs.commons.pleaseFillData')" />');" />
					</td>
					<td>
					</td>
				</tr>

				<tr>
					<td id="technicalServicesLabel" colspan="4">
						<span><s:property value="getText('label.ai.accessAndServices.technicalServices')" /></span>
					</td>
				</tr>

				<tr>
					<td id="restaurationLabLabel">
						<span><label for="selectASTSRestaurationLab"><s:property value="getText('label.ai.accessAndServices.restaurationLab')" />:</label></span>
					</td>
					<td>
						<select id="selectASTSRestaurationLab">
							<s:iterator value="noneYesNoList" var="yesno">
								<s:if test="%{loader.asRestorationlabQuestion.size() > 0}">
									<s:set var="restorationlabQuestion" value="loader.asRestorationlabQuestion[#counter]"/>
									<s:if test="%{#restorationlabQuestion.size() > 0}">
										<option value="<s:property value="#yesno.key" />"
											<s:if test="%{#yesno.key == #restorationlabQuestion[0]}"> selected=selected </s:if>>
											<s:property value="#yesno.value" />
										</option>
									</s:if>
									<s:else>
										<option value="<s:property value="#yesno.key" />">
											<s:property value="#yesno.value" />
										</option>
									</s:else>
								</s:if>
								<s:else>
									<option value="<s:property value="#yesno.key" />">
										<s:property value="#yesno.value" />
									</option>
								</s:else>
							</s:iterator>
						</select>
					</td>
					<td colspan="2">
					</td>
				</tr>

				<s:if test="%{loader.asRestorationlabDescription.size() > 0}">
					<s:set var="restorationlabDescription" value="loader.asRestorationlabDescription[#counter]"/>
					<s:set var="restorationlabDescriptionLang" value="loader.asRestorationlabDescriptionLang[#counter]"/>
					<s:if test="%{#restorationlabDescription.size() > 0}">
						<s:iterator var="internalCurrent" value="#restorationlabDescription" status="internalStatus">
							<tr id="trASTSDescriptionOfRestaurationLab_<s:property value="%{#internalStatus.index + 1}" />">
								<td>
									<label for="textASTSDescriptionOfRestaurationLab_<s:property value="%{#internalStatus.index + 1}" />"><span><s:property value="getText('label.ai.accessAndServices.description')" /></span>:</label>
								</td>
								<td>
									<input type="text" id="textASTSDescriptionOfRestaurationLab_<s:property value="%{#internalStatus.index + 1}" />" value="<s:property value="#internalCurrent" />" />
								</td>
								<td class="labelLeft">
									<label for="selectASTSSelectLanguage_<s:property value="%{#internalStatus.index + 1}" />" ><s:property value="getText('label.ai.tabs.commons.selectLanguage')" />:</label>
								</td>
								<td>
									<select id="selectASTSSelectLanguage_<s:property value="%{#internalStatus.index + 1}" />">
										<s:iterator value="languageList" var="language"> 
											<option value="<s:property value="#language.key" />"<s:if test="%{#language.key == #restorationlabDescriptionLang[#internalStatus.index]}" > selected=selected </s:if>><s:property value="#language.value" /></option>
										</s:iterator>
									</select>
								</td>
							</tr>
						</s:iterator>
					</s:if>
					<s:else>
						<tr id="trASTSDescriptionOfRestaurationLab_1">
							<td>
								<label for="textASTSDescriptionOfRestaurationLab_1"><span><s:property value="getText('label.ai.accessAndServices.description')" /></span>:</label>
							</td>
							<td>
								<input type="text" id="textASTSDescriptionOfRestaurationLab_1" />
							</td>
							<td class="labelLeft">
								<label for="selectASTSSelectLanguage_1"><s:property value="getText('label.ai.tabs.commons.selectLanguage')"/>:</label>
							</td>
							<td>
								<select id="selectASTSSelectLanguage_1">
									<s:iterator value="languageList" var="language"> 
										<option value="<s:property value="#language.key" />"><s:property value="#language.value" /></option>
									</s:iterator>
								</select>
							</td>
						</tr>
					</s:else>
				</s:if>
				<s:else>
					<tr id="trASTSDescriptionOfRestaurationLab_1">
						<td>
							<label for="textASTSDescriptionOfRestaurationLab_1"><span><s:property value="getText('label.ai.accessAndServices.description')" /></span>:</label>
						</td>
						<td>
							<input type="text" id="textASTSDescriptionOfRestaurationLab_1" />
						</td>
						<td class="labelLeft">
							<label for="selectASTSSelectLanguage_1"><s:property value="getText('label.ai.tabs.commons.selectLanguage')"/>:</label>
						</td>
						<td>
							<select id="selectASTSSelectLanguage_1">
								<s:iterator value="languageList" var="language"> 
									<option value="<s:property value="#language.key" />"><s:property value="#language.value" /></option>
								</s:iterator>
							</select>
						</td>
					</tr>
				</s:else>

				<tr>
					<td colspan="3">
						<input type="button" id="buttonAddADescriptionOfYourRestaurationLab" value="<s:property value="getText('label.ai.accessAndServices.addDescriptionOfYourRestaurationLab')"/>" onclick="addADescriptionOfYourRestaurationLab('<s:property value="getText('label.ai.tabs.commons.pleaseFillData')" />');"/>
					</td>
					<td>
					</td>
				</tr>

				<s:if test="%{loader.asRestorationlabTelephone.size() > 0}">
					<s:set var="restorationlabTelephone" value="loader.asRestorationlabTelephone[#counter]"/>
					<s:if test="%{#restorationlabTelephone.size() > 0}">
						<s:iterator var="internalCurrent" value="#restorationlabTelephone" status="internalStatus">
							<tr>
								<td>
									<label for="textASTSTelephone_<s:property value="%{#internalStatus.index + 1}" />"><s:property value="getText('label.ai.tabs.commons.telephone')" />:</label>
								</td>
								<td>
									<input type="text" id="textASTSTelephone_<s:property value="%{#internalStatus.index + 1}" />" value="<s:property value="#internalCurrent" />" />
								</td>
								<td colspan="2">
								</td>
							</tr>
						</s:iterator>
					</s:if>
					<s:else>
						<tr>
							<td>
								<label for="textASTSTelephone_1"><s:property value="getText('label.ai.tabs.commons.telephone')" />:</label>
							</td>
							<td>
								<input type="text" id="textASTSTelephone_1" />
							</td>
							<td colspan="2">
							</td>
						</tr>
					</s:else>
				</s:if>
				<s:else>
					<tr>
						<td>
							<label for="textASTSTelephone_1"><s:property value="getText('label.ai.tabs.commons.telephone')" />:</label>
						</td>
						<td>
							<input type="text" id="textASTSTelephone_1" />
						</td>
						<td colspan="2">
						</td>
					</tr>
				</s:else>

				<s:if test="%{loader.asRestorationlabNumberOfEmail.size() > 0}">
					<s:set var="restorationlabNumberOfEmail" value="loader.asRestorationlabNumberOfEmail[#counter]"/>
					<s:set var="restorationlabEmailHref" value="loader.asRestorationlabEmailHref[#counter]"/>
					<s:set var="restorationlabEmailTitle" value="loader.asRestorationlabEmailTitle[#counter]"/>
					<s:if test="%{#restorationlabNumberOfEmail.size() > 0}">
						<s:iterator var="internalCurrent" value="#restorationlabNumberOfEmail" status="internalStatus">
							<tr>
								<td>
									<s:if test="%{#internalStatus.index > 0}">
										<label for="textASRSEmail_<s:property value="%{#internalStatus.index + 1}" />"><s:property value="getText('label.ai.tabs.commons.emailAddress')" />:</label>
									</s:if>
									<s:else>
										<label for="textASRSEmail"><s:property value="getText('label.ai.tabs.commons.emailAddress')" />:</label>
									</s:else>
								</td>
								<td>
									<s:if test="%{#internalStatus.index > 0}">
										<input type="text" id="textASRSEmail_<s:property value="%{#internalStatus.index + 1}" />" value="<s:property value="#restorationlabEmailHref[#internalStatus.index]" />" />
									</s:if>
									<s:else>
										<input type="text" id="textASRSEmail" value="<s:property value="#restorationlabEmailHref[#internalStatus.index]" />" />
									</s:else>
								</td>
								<td class="labelLeft">
									<s:if test="%{#internalStatus.index > 0}">
										<label for="textASRSEmailLinkTitle_<s:property value="%{#internalStatus.index + 1}" />"><s:property value="getText('label.ai.tabs.commons.linkTitle')" />:</label>
									</s:if>
									<s:else>
										<label for="textASRSEmailLinkTitle"><s:property value="getText('label.ai.tabs.commons.linkTitle')"/>:</label>
									</s:else>
								</td>
								<td>
									<s:if test="%{#internalStatus.index > 0}">
										<input type="text" id="textASRSEmailLinkTitle_<s:property value="%{#internalStatus.index + 1}" />" value="<s:property value="#restorationlabEmailTitle[#internalStatus.index]" />" />
									</s:if>
									<s:else>
										<input type="text" id="textASRSEmailLinkTitle" value="<s:property value="#restorationlabEmailTitle[#internalStatus.index]" />" />
									</s:else>
								</td>
							</tr>
						</s:iterator>
					</s:if>
					<s:else>
						<tr>
							<td>
								<label for="textASRSEmail"><s:property value="getText('label.ai.tabs.commons.emailAddress')" />:</label>
							</td>
							<td>
								<input type="text" id="textASRSEmail" />
							</td>
							<td class="labelLeft">
								<label for="textASRSEmailLinkTitle"><s:property value="getText('label.ai.tabs.commons.linkTitle')"/>:</label>
							</td>
							<td>
								<input type="text" id="textASRSEmailLinkTitle" />
							</td>
						</tr>
					</s:else>
				</s:if>
				<s:else>
					<tr>
						<td>
							<label for="textASRSEmail"><s:property value="getText('label.ai.tabs.commons.emailAddress')" />:</label>
						</td>
						<td>
							<input type="text" id="textASRSEmail" />
						</td>
						<td class="labelLeft">
							<label for="textASRSEmailLinkTitle"><s:property value="getText('label.ai.tabs.commons.linkTitle')"/>:</label>
						</td>
						<td>
							<input type="text" id="textASRSEmailLinkTitle" />
						</td>
					</tr>
				</s:else>

				<s:if test="%{loader.asRestorationlabNumberOfWebpage.size() > 0}">
					<s:set var="restorationlabNumberOfWebpage" value="loader.asRestorationlabNumberOfWebpage[#counter]"/>
					<s:set var="restorationlabWebpageHref" value="loader.asRestorationlabWebpageHref[#counter]"/>
					<s:set var="restorationlabWebpageTitle" value="loader.asRestorationlabWebpageTitle[#counter]"/>
					<s:if test="%{#restorationlabNumberOfWebpage.size() > 0}">
						<s:iterator var="internalCurrent" value="#restorationlabNumberOfWebpage" status="internalStatus">
							<tr>
								<td>
									<s:if test="%{#internalStatus.index > 0}">
										<label for="textASRSWebpage_<s:property value="%{#internalStatus.index + 1}" />"><s:property value="getText('label.ai.tabs.commons.webpage')" />:</label>
									</s:if>
									<s:else>
										<label for="textASRSWebpage"><s:property value="getText('label.ai.tabs.commons.webpage')" />:</label>
									</s:else>
								</td>
								<td>
									<s:if test="%{#internalStatus.index > 0}">
										<input type="text" id="textASRSWebpage_<s:property value="%{#internalStatus.index + 1}" />" value="<s:property value="#restorationlabWebpageHref[#internalStatus.index]" />" />
									</s:if>
									<s:else>
										<input type="text" id="textASRSWebpage" value="<s:property value="#restorationlabWebpageHref[#internalStatus.index]" />" />
									</s:else>
								</td>
								<td class="labelLeft">
									<s:if test="%{#internalStatus.index > 0}">
										<label for="textASRSWebpageLinkTitle_<s:property value="%{#internalStatus.index + 1}" />"><s:property value="getText('label.ai.tabs.commons.linkTitle')" />:</label>
									</s:if>
									<s:else>
										<label for="textASRSWebpageLinkTitle"><s:property value="getText('label.ai.tabs.commons.linkTitle')"/>:</label>
									</s:else>
								</td>
								<td>
									<s:if test="%{#internalStatus.index > 0}">
										<input type="text" id="textASRSWebpageLinkTitle_<s:property value="%{#internalStatus.index + 1}" />" value="<s:property value="#restorationlabWebpageTitle[#internalStatus.index]" />" />
									</s:if>
									<s:else>
										<input type="text" id="textASRSWebpageLinkTitle" value="<s:property value="#restorationlabWebpageTitle[#internalStatus.index]" />" />
									</s:else>
								</td>
							</tr>
						</s:iterator>
					</s:if>
					<s:else>
						<tr>
							<td>
								<label for="textASRSWebpage"><s:property value="getText('label.ai.tabs.commons.webpage')" />:</label>
							</td>
							<td>
								<input type="text" id="textASRSWebpage" />
							</td>
							<td class="labelLeft">
								<label for="textASRSWebpageLinkTitle"><s:property value="getText('label.ai.tabs.commons.linkTitle')"/>:</label>
							</td>
							<td>
								<input type="text" id="textASRSWebpageLinkTitle" />
							</td>
						</tr>
					</s:else>
				</s:if>
				<s:else>
					<tr>
						<td>
							<label for="textASRSWebpage"><s:property value="getText('label.ai.tabs.commons.webpage')" />:</label>
						</td>
						<td>
							<input type="text" id="textASRSWebpage" />
						</td>
						<td class="labelLeft">
							<label for="textASRSWebpageLinkTitle"><s:property value="getText('label.ai.tabs.commons.linkTitle')"/>:</label>
						</td>
						<td>
							<input type="text" id="textASRSWebpageLinkTitle" />
						</td>
					</tr>
				</s:else>

				<tr>
					<td id="reproductionServiceLabel">
						<label for="selectASTSReproductionService"><s:property value="getText('label.ai.accessAndServices.reproductionService')"/>:</label>
					</td>
					<td>
						<select id="selectASTSReproductionService">
							<s:iterator value="noneYesNoList" var="yesno">
								<s:if test="%{loader.asReproductionserQuestion.size() > 0}">
									<s:set var="reproductionserQuestion" value="loader.asReproductionserQuestion[#counter]"/>
									<s:if test="%{#reproductionserQuestion.size() > 0}">
										<option value="<s:property value="#yesno.key" />"
											<s:if test="%{#yesno.key == #reproductionserQuestion[0]}"> selected=selected </s:if>>
											<s:property value="#yesno.value" />
										</option>
									</s:if>
									<s:else>
										<option value="<s:property value="#yesno.key" />">
											<s:property value="#yesno.value" />
										</option>
									</s:else>
								</s:if>
								<s:else>
									<option value="<s:property value="#yesno.key" />">
										<s:property value="#yesno.value" />
									</option>
								</s:else>
							</s:iterator>
						</select>
					</td>
					<td colspan="2">
					</td>
				</tr>

				<s:if test="%{loader.asReproductionserDescription.size() > 0}">
					<s:set var="reproductionserDescription" value="loader.asReproductionserDescription[#counter]"/>
					<s:set var="reproductionserDescriptionLang" value="loader.asReproductionserDescriptionLang[#counter]"/>
					<s:if test="%{#reproductionserDescription.size() > 0}">
						<s:iterator var="internalCurrent" value="#reproductionserDescription" status="internalStatus">
							<tr id="trASTSDescriptionOfReproductionService_<s:property value="%{#internalStatus.index + 1}" />">
								<td>
									<label for="textASTSDescriptionOfReproductionService_<s:property value="%{#internalStatus.index + 1}" />"><span><s:property value="getText('label.ai.accessAndServices.description')" /></span>:</label>
								</td>
								<td>
									<input type="text" id="textASTSDescriptionOfReproductionService_<s:property value="%{#internalStatus.index + 1}" />" value="<s:property value="#internalCurrent" />" />
								</td>
								<td class="labelLeft">
									<label for="selectASTSRSSelectLanguage_<s:property value="%{#internalStatus.index + 1}" />" ><s:property value="getText('label.ai.tabs.commons.selectLanguage')" />:</label>
								</td>
								<td>
									<select id="selectASTSRSSelectLanguage_<s:property value="%{#internalStatus.index + 1}" />">
										<s:iterator value="languageList" var="language"> 
											<option value="<s:property value="#language.key" />"<s:if test="%{#language.key == #reproductionserDescriptionLang[#internalStatus.index]}" > selected=selected </s:if>><s:property value="#language.value" /></option>
										</s:iterator>
									</select>
								</td>
							</tr>
						</s:iterator>
					</s:if>
					<s:else>
						<tr id="trASTSDescriptionOfReproductionService_1">
							<td>
								<label for="textASTSDescriptionOfReproductionService_1"><span><s:property value="getText('label.ai.accessAndServices.description')" /></span>:</label>
							</td>
							<td>
								<input type="text" id="textASTSDescriptionOfReproductionService_1" />
							</td>
							<td class="labelLeft">
								<label for="selectASTSRSSelectLanguage_1"><s:property value="getText('label.ai.tabs.commons.selectLanguage')"/>:</label>
							</td>
							<td>
								<select id="selectASTSRSSelectLanguage_1">
									<s:iterator value="languageList" var="language"> 
										<option value="<s:property value="#language.key" />"><s:property value="#language.value" /></option>
									</s:iterator>
								</select>
							</td>
						</tr>
					</s:else>
				</s:if>
				<s:else>
					<tr id="trASTSDescriptionOfReproductionService_1">
						<td>
							<label for="textASTSDescriptionOfReproductionService_1"><span><s:property value="getText('label.ai.accessAndServices.description')" /></span>:</label>
						</td>
						<td>
							<input type="text" id="textASTSDescriptionOfReproductionService_1" />
						</td>
						<td class="labelLeft">
							<label for="selectASTSRSSelectLanguage_1"><s:property value="getText('label.ai.tabs.commons.selectLanguage')"/>:</label>
						</td>
						<td>
							<select id="selectASTSRSSelectLanguage_1">
								<s:iterator value="languageList" var="language"> 
									<option value="<s:property value="#language.key" />"><s:property value="#language.value" /></option>
								</s:iterator>
							</select>
						</td>
					</tr>
				</s:else>
		
				<tr>
					<td colspan="3">
						<input type="button" id="buttonASAddADescriptionOfYourReproductionService"value="<s:property value="getText('label.ai.accessAndServices.addDescriptionOfYourReproductionService')"/>" onclick="aSAddADescriptionOfYourReproductionService('<s:property value="getText('label.ai.tabs.commons.pleaseFillData')" />');" />
					</td>
					<td>
					</td>
				</tr>

				<s:if test="%{loader.asReproductionserTelephone.size() > 0}">
					<s:set var="reproductionserTelephone" value="loader.asReproductionserTelephone[#counter]"/>
					<s:if test="%{#reproductionserTelephone.size() > 0}">
						<s:iterator var="internalCurrent" value="#reproductionserTelephone" status="internalStatus">
							<tr>
								<td>
									<label for="textASTSRSTelephone_<s:property value="%{#internalStatus.index + 1}" />"><s:property value="getText('label.ai.tabs.commons.telephone')" />:</label>
								</td>
								<td>
									<input type="text" id="textASTSRSTelephone_<s:property value="%{#internalStatus.index + 1}" />" value="<s:property value="#internalCurrent" />" />
								</td>
								<td colspan="2">
								</td>
							</tr>
						</s:iterator>
					</s:if>
					<s:else>
						<tr>
							<td>
								<label for="textASTSRSTelephone_1"><s:property value="getText('label.ai.tabs.commons.telephone')" />:</label>
							</td>
							<td>
								<input type="text" id="textASTSRSTelephone_1" />
							</td>
							<td colspan="2">
							</td>
						</tr>
					</s:else>
				</s:if>
				<s:else>
					<tr>
						<td>
							<label for="textASTSRSTelephone_1"><s:property value="getText('label.ai.tabs.commons.telephone')" />:</label>
						</td>
						<td>
							<input type="text" id="textASTSRSTelephone_1" />
						</td>
						<td colspan="2">
						</td>
					</tr>
				</s:else>

				<s:if test="%{loader.asReproductionserNumberOfEmail.size() > 0}">
					<s:set var="reproductionserNumberOfEmail" value="loader.asReproductionserNumberOfEmail[#counter]"/>
					<s:set var="reproductionserEmailHref" value="loader.asReproductionserEmailHref[#counter]"/>
					<s:set var="reproductionserEmailTitle" value="loader.asReproductionserEmailTitle[#counter]"/>
					<s:if test="%{#reproductionserNumberOfEmail.size() > 0}">
						<s:iterator var="internalCurrent" value="#reproductionserNumberOfEmail" status="internalStatus">
							<tr>
								<td>
									<s:if test="%{#internalStatus.index > 0}">
										<label for="textASTSRSEmailAddress_<s:property value="%{#internalStatus.index + 1}" />"><s:property value="getText('label.ai.tabs.commons.emailAddress')" />:</label>
									</s:if>
									<s:else>
										<label for="textASTSRSEmailAddress"><s:property value="getText('label.ai.tabs.commons.emailAddress')" />:</label>
									</s:else>
								</td>
								<td>
									<s:if test="%{#internalStatus.index > 0}">
										<input type="text" id="textASTSRSEmailAddress_<s:property value="%{#internalStatus.index + 1}" />" value="<s:property value="#reproductionserEmailHref[#internalStatus.index]" />" />
									</s:if>
									<s:else>
										<input type="text" id="textASTSRSEmailAddress" value="<s:property value="#reproductionserEmailHref[#internalStatus.index]" />" />
									</s:else>
								</td>
								<td class="labelLeft">
									<s:if test="%{#internalStatus.index > 0}">
										<label for="textASTSEmailAddressLinkTitle_<s:property value="%{#internalStatus.index + 1}" />"><s:property value="getText('label.ai.tabs.commons.linkTitle')" />:</label>
									</s:if>
									<s:else>
										<label for="textASTSEmailAddressLinkTitle"><s:property value="getText('label.ai.tabs.commons.linkTitle')"/>:</label>
									</s:else>
								</td>
								<td>
									<s:if test="%{#internalStatus.index > 0}">
										<input type="text" id="textASTSEmailAddressLinkTitle_<s:property value="%{#internalStatus.index + 1}" />" value="<s:property value="#reproductionserEmailTitle[#internalStatus.index]" />" />
									</s:if>
									<s:else>
										<input type="text" id="textASTSEmailAddressLinkTitle" value="<s:property value="#reproductionserEmailTitle[#internalStatus.index]" />" />
									</s:else>
								</td>
							</tr>
						</s:iterator>
					</s:if>
					<s:else>
						<tr>
							<td>
								<label for="textASTSRSEmailAddress"><s:property value="getText('label.ai.tabs.commons.emailAddress')" />:</label>
							</td>
							<td>
								<input type="text" id="textASTSRSEmailAddress" />
							</td>
							<td class="labelLeft">
								<label for="textASTSEmailAddressLinkTitle"><s:property value="getText('label.ai.tabs.commons.linkTitle')"/>:</label>
							</td>
							<td>
								<input type="text" id="textASTSEmailAddressLinkTitle" />
							</td>
						</tr>
					</s:else>
				</s:if>
				<s:else>
					<tr>
						<td>
							<label for="textASTSRSEmailAddress"><s:property value="getText('label.ai.tabs.commons.emailAddress')" />:</label>
						</td>
						<td>
							<input type="text" id="textASTSRSEmailAddress" />
						</td>
						<td class="labelLeft">
							<label for="textASTSEmailAddressLinkTitle"><s:property value="getText('label.ai.tabs.commons.linkTitle')"/>:</label>
						</td>
						<td>
							<input type="text" id="textASTSEmailAddressLinkTitle" />
						</td>
					</tr>
				</s:else>

				<s:if test="%{loader.asReproductionserNumberOfWebpage.size() > 0}">
					<s:set var="reproductionserNumberOfWebpage" value="loader.asReproductionserNumberOfWebpage[#counter]"/>
					<s:set var="reproductionserWebpageHref" value="loader.asReproductionserWebpageHref[#counter]"/>
					<s:set var="reproductionserWebpageTitle" value="loader.asReproductionserWebpageTitle[#counter]"/>
					<s:if test="%{#reproductionserNumberOfWebpage.size() > 0}">
						<s:iterator var="internalCurrent" value="#reproductionserNumberOfWebpage" status="internalStatus">
							<tr>
								<td>
									<s:if test="%{#internalStatus.index > 0}">
										<label for="textASTSRSWebpage_<s:property value="%{#internalStatus.index + 1}" />"><s:property value="getText('label.ai.tabs.commons.webpage')" />:</label>
									</s:if>
									<s:else>
										<label for="textASTSRSWebpage"><s:property value="getText('label.ai.tabs.commons.webpage')" />:</label>
									</s:else>
								</td>
								<td>
									<s:if test="%{#internalStatus.index > 0}">
										<input type="text" id="textASTSRSWebpage_<s:property value="%{#internalStatus.index + 1}" />" value="<s:property value="#reproductionserWebpageHref[#internalStatus.index]" />" />
									</s:if>
									<s:else>
										<input type="text" id="textASTSRSWebpage" value="<s:property value="#reproductionserWebpageHref[#internalStatus.index]" />" />
									</s:else>
								</td>
								<td class="labelLeft">
									<s:if test="%{#internalStatus.index > 0}">
										<label for="textASTSRSWebpageLinkTitle_<s:property value="%{#internalStatus.index + 1}" />"><s:property value="getText('label.ai.tabs.commons.linkTitle')" />:</label>
									</s:if>
									<s:else>
										<label for="textASTSRSWebpageLinkTitle"><s:property value="getText('label.ai.tabs.commons.linkTitle')"/>:</label>
									</s:else>
								</td>
								<td>
									<s:if test="%{#internalStatus.index > 0}">
										<input type="text" id="textASTSRSWebpageLinkTitle_<s:property value="%{#internalStatus.index + 1}" />" value="<s:property value="#reproductionserWebpageTitle[#internalStatus.index]" />" />
									</s:if>
									<s:else>
										<input type="text" id="textASTSRSWebpageLinkTitle" value="<s:property value="#reproductionserWebpageTitle[#internalStatus.index]" />" />
									</s:else>
								</td>
							</tr>
						</s:iterator>
					</s:if>
					<s:else>
						<tr>
							<td>
								<label for="textASTSRSWebpage"><s:property value="getText('label.ai.tabs.commons.webpage')" />:</label>
							</td>
							<td>
								<input type="text" id="textASTSRSWebpage" />
							</td>
							<td class="labelLeft">
								<label for="textASTSRSWebpageLinkTitle"><s:property value="getText('label.ai.tabs.commons.linkTitle')"/>:</label>
							</td>
							<td>
								<input type="text" id="textASTSRSWebpageLinkTitle" />
							</td>
						</tr>
					</s:else>
				</s:if>
				<s:else>
					<tr>
						<td>
							<label for="textASTSRSWebpage"><s:property value="getText('label.ai.tabs.commons.webpage')" />:</label>
						</td>
						<td>
							<input type="text" id="textASTSRSWebpage" />
						</td>
						<td class="labelLeft">
							<label for="textASTSRSWebpageLinkTitle"><s:property value="getText('label.ai.tabs.commons.linkTitle')"/>:</label>
						</td>
						<td>
							<input type="text" id="textASTSRSWebpageLinkTitle" />
						</td>
					</tr>
				</s:else>

				<tr>
					<td>
						<label for="selectASTSRSMicroform"><s:property value="getText('label.ai.accessAndServices.microformServices')" />:</label>
					</td>
					<td>
						<select id="selectASTSRSMicroform">
							<s:iterator value="noneYesNoList" var="yesno">
								<s:if test="%{loader.asReproductionserMicrofilmServices.size() > 0}">
									<s:set var="reproductionserMicrofilmServices" value="loader.asReproductionserMicrofilmServices[#counter]"/>
									<s:if test="%{#reproductionserMicrofilmServices.size() > 0}">
										<option value="<s:property value="#yesno.key" />"
											<s:if test="%{#yesno.key == #reproductionserMicrofilmServices[0]}"> selected=selected </s:if>>
											<s:property value="#yesno.value" />
										</option>
									</s:if>
									<s:else>
										<option value="<s:property value="#yesno.key" />">
											<s:property value="#yesno.value" />
										</option>
									</s:else>
								</s:if>
								<s:else>
									<option value="<s:property value="#yesno.key" />">
										<s:property value="#yesno.value" />
									</option>
								</s:else>
							</s:iterator>
						</select>
					</td>
					<td colspan="2">
					</td>
				</tr>

				<tr>
					<td>
						<label for="selectASTSRSPhotographServices"><s:property value="getText('label.ai.accessAndServices.photographServices')" />:</label>
					</td>
					<td>
						<select id="selectASTSRSPhotographServices">
							<s:iterator value="noneYesNoList" var="yesno">
								<s:if test="%{loader.asReproductionserPhotographicServices.size() > 0}">
									<s:set var="reproductionserPhotographicServices" value="loader.asReproductionserPhotographicServices[#counter]"/>
									<s:if test="%{#reproductionserPhotographicServices.size() > 0}">
										<option value="<s:property value="#yesno.key" />"
											<s:if test="%{#yesno.key == #reproductionserPhotographicServices[0]}"> selected=selected </s:if>>
											<s:property value="#yesno.value" />
										</option>
									</s:if>
									<s:else>
										<option value="<s:property value="#yesno.key" />">
											<s:property value="#yesno.value" />
										</option>
									</s:else>
								</s:if>
								<s:else>
									<option value="<s:property value="#yesno.key" />">
										<s:property value="#yesno.value" />
									</option>
								</s:else>
							</s:iterator>
						</select>
					</td>
					<td colspan="2">
					</td>
				</tr>

				<tr>
					<td>
						<label for="selectASTSRSDigitalServices"><s:property value="getText('label.ai.accessAndServices.digitalServices')" />:</label>
					</td>
					<td>
						<select id="selectASTSRSDigitalServices">
							<s:iterator value="noneYesNoList" var="yesno">
								<s:if test="%{loader.asReproductionserDigitisationServices.size() > 0}">
									<s:set var="reproductionserDigitisationServices" value="loader.asReproductionserDigitisationServices[#counter]"/>
									<s:if test="%{#reproductionserDigitisationServices.size() > 0}">
										<option value="<s:property value="#yesno.key" />"
											<s:if test="%{#yesno.key == #reproductionserDigitisationServices[0]}"> selected=selected </s:if>>
											<s:property value="#yesno.value" />
										</option>
									</s:if>
									<s:else>
										<option value="<s:property value="#yesno.key" />">
											<s:property value="#yesno.value" />
										</option>
									</s:else>
								</s:if>
								<s:else>
									<option value="<s:property value="#yesno.key" />">
										<s:property value="#yesno.value" />
									</option>
								</s:else>
							</s:iterator>
						</select>
					</td>
					<td colspan="2">
					</td>
				</tr>

				<tr>
					<td>
						<label for="selectASTSRSPhotocopyServices"><s:property value="getText('label.ai.accessAndServices.photocopyServices')" />:</label>
					</td>
					<td>
						<select id="selectASTSRSPhotocopyServices">
							<s:iterator value="noneYesNoList" var="yesno">
								<s:if test="%{loader.asReproductionserPhotocopyingServices.size() > 0}">
									<s:set var="reproductionserPhotocopyingServices" value="loader.asReproductionserPhotocopyingServices[#counter]"/>
									<s:if test="%{#reproductionserPhotocopyingServices.size() > 0}">
										<option value="<s:property value="#yesno.key" />"
											<s:if test="%{#yesno.key == #reproductionserPhotocopyingServices[0]}"> selected=selected </s:if>>
											<s:property value="#yesno.value" />
										</option>
									</s:if>
									<s:else>
										<option value="<s:property value="#yesno.key" />">
											<s:property value="#yesno.value" />
										</option>
									</s:else>
								</s:if>
								<s:else>
									<option value="<s:property value="#yesno.key" />">
										<s:property value="#yesno.value" />
									</option>
								</s:else>
							</s:iterator>
						</select>
					</td>
					<td colspan="2">
					</td>
				</tr>

				<tr>
					<td id="recreationalServiceLabel" colspan="4">
						<span><s:property value="getText('label.ai.accessAndServices.recreationalServices')"/></span>
					</td>
					
				</tr>

				<s:if test="%{loader.asRecreationalServicesRefreshmentArea.size() > 0}">
					<s:set var="recreationalServicesRefreshmentArea" value="loader.asRecreationalServicesRefreshmentArea[#counter]"/>
					<s:set var="recreationalServicesRefreshmentAreaLang" value="loader.asRecreationalServicesRefreshmentAreaLang[#counter]"/>
					<s:if test="%{#recreationalServicesRefreshmentArea.size() > 0}">
						<s:iterator var="internalCurrent" value="#recreationalServicesRefreshmentArea" status="internalStatus">
							<tr id="trASReSeRefreshment_<s:property value="%{#internalStatus.index + 1}" />">
								<td>
									<label for="textASReSeRefreshment_<s:property value="%{#internalStatus.index + 1}" />"><s:property value="getText('label.ai.accessAndServices.refreshment')" />:</label>
								</td>
								<td>
									<input type="text" id="textASReSeRefreshment_<s:property value="%{#internalStatus.index + 1}" />" value="<s:property value="#internalCurrent" />" />
								</td>
								<td class="labelLeft">
									<label for="selectASReSeRefreshmentSelectLanguage_<s:property value="%{#internalStatus.index + 1}" />" ><s:property value="getText('label.ai.tabs.commons.selectLanguage')" />:</label>
								</td>
								<td>
									<select id="selectASReSeRefreshmentSelectLanguage_<s:property value="%{#internalStatus.index + 1}" />">
										<s:iterator value="languageList" var="language"> 
											<option value="<s:property value="#language.key" />"<s:if test="%{#language.key == #recreationalServicesRefreshmentAreaLang[#internalStatus.index]}" > selected=selected </s:if>><s:property value="#language.value" /></option>
										</s:iterator>
									</select>
								</td>
							</tr>
						</s:iterator>
					</s:if>
					<s:else>
						<tr id="trASReSeRefreshment_1">
							<td>
								<label for="textASReSeRefreshment_1"><s:property value="getText('label.ai.accessAndServices.refreshment')" />:</label>
							</td>
							<td>
								<input type="text" id="textASReSeRefreshment_1" />
							</td>
							<td class="labelLeft">
								<label for="selectASReSeRefreshmentSelectLanguage_1"><s:property value="getText('label.ai.tabs.commons.selectLanguage')"/>:</label>
							</td>
							<td>
								<select id="selectASReSeRefreshmentSelectLanguage_1">
									<s:iterator value="languageList" var="language"> 
										<option value="<s:property value="#language.key" />"><s:property value="#language.value" /></option>
									</s:iterator>
								</select>
							</td>
						</tr>
					</s:else>
				</s:if>
				<s:else>
					<tr id="trASReSeRefreshment_1">
						<td>
							<label for="textASReSeRefreshment_1"><s:property value="getText('label.ai.accessAndServices.refreshment')" />:</label>
						</td>
						<td>
							<input type="text" id="textASReSeRefreshment_1" />
						</td>
						<td class="labelLeft">
							<label for="selectASReSeRefreshmentSelectLanguage_1"><s:property value="getText('label.ai.tabs.commons.selectLanguage')"/>:</label>
						</td>
						<td>
							<select id="selectASReSeRefreshmentSelectLanguage_1">
								<s:iterator value="languageList" var="language"> 
									<option value="<s:property value="#language.key" />"><s:property value="#language.value" /></option>
								</s:iterator>
							</select>
						</td>
					</tr>
				</s:else>

				<tr>
					<td colspan="3">
						<input type="button" id="buttonASReSeAddFurtherRefreshment" value="<s:property value="getText('label.ai.accessAndServices.addFurtherRefreshmentInformation')"/>" onclick="aSReSeAddFurtherRefreshment('<s:property value="getText('label.ai.tabs.commons.pleaseFillData')" />');" />
					</td>
					<td>
					</td>
				</tr>

				<s:if test="%{loader.asRSNumberOfExhibition.size() > 0}">
					<s:set var="numberOfExhibition" value="loader.asRSNumberOfExhibition[#counter]"/>
					<s:set var="rsExhibition" value="loader.asRSExhibition[#counter]"/>
					<s:set var="rsExhibitionLang" value="loader.asRSExhibitionLang[#counter]"/>
					<s:set var="rsExhibitionWebpageHref" value="loader.asRSExhibitionWebpageHref[#counter]"/>
					<s:set var="rsExhibitionWebpageTitle" value="loader.asRSExhibitionWebpageTitle[#counter]"/>
					<s:if test="%{#numberOfExhibition.size() > 0}">
						<s:iterator var="internalCurrent" value="#numberOfExhibition" status="internalStatus">
							<tr id="trASReSeExhibition_<s:property value="%{#internalStatus.index + 1}" />">
								<td>
									<label for="textASReSeExhibition_<s:property value="%{#internalStatus.index + 1}" />"><s:property value="getText('label.ai.accessAndServices.exhibition')" />:</label>
								</td>
								<td>
									<input type="text" id="textASReSeExhibition_<s:property value="%{#internalStatus.index + 1}" />" value="<s:property value="#rsExhibition[#internalStatus.index]" />" />
								</td>
								<td class="labelLeft">
									<label for="selectASReSeExhibitionSelectLanguage_<s:property value="%{#internalStatus.index + 1}" />" ><s:property value="getText('label.ai.tabs.commons.selectLanguage')" />:</label>
								</td>
								<td>
									<select id="selectASReSeExhibitionSelectLanguage_<s:property value="%{#internalStatus.index + 1}" />">
										<s:iterator value="languageList" var="language"> 
											<option value="<s:property value="#language.key" />"<s:if test="%{#language.key == #rsExhibitionLang[#internalStatus.index]}" > selected=selected </s:if>><s:property value="#language.value" /></option>
										</s:iterator>
									</select>
								</td>
							</tr>

							<tr id="tr2ASReSeExhibition_<s:property value="%{#internalStatus.index + 1}" />">
								<td>
									<label for="textASReSeWebpage_<s:property value="%{#internalStatus.index + 1}" />"><s:property value="getText('label.ai.tabs.commons.webpage')"/>:</label>
								</td>
								<td>
									<input type="text" id="textASReSeWebpage_<s:property value="%{#internalStatus.index + 1}" />" value="<s:property value="#rsExhibitionWebpageHref[#internalStatus.index]" />" />
								</td>
								<td class="labelLeft">
									<label for="textASReSeWebpageLinkTitle_<s:property value="%{#internalStatus.index + 1}" />" ><s:property value="getText('label.ai.tabs.commons.linkTitle')" />:</label>
								</td>
								<td>
									<input type="text" id="textASReSeWebpageLinkTitle_<s:property value="%{#internalStatus.index + 1}" />" value="<s:property value="#rsExhibitionWebpageTitle[#internalStatus.index]" />" />
								</td>
							</tr>
						</s:iterator>
					</s:if>
					<s:else>
						<tr id="trASReSeExhibition_1">
							<td>
								<label for="textASReSeExhibition_1"><s:property value="getText('label.ai.accessAndServices.exhibition')" />:</label>
							</td>
							<td>
								<input type="text" id="textASReSeExhibition_1" />
							</td>
							<td class="labelLeft">
								<label for="selectASReSeExhibitionSelectLanguage_1"><s:property value="getText('label.ai.tabs.commons.selectLanguage')"/>:</label>
							</td>
							<td>
								<select id="selectASReSeExhibitionSelectLanguage_1">
									<s:iterator value="languageList" var="language"> 
										<option value="<s:property value="#language.key" />"><s:property value="#language.value" /></option>
									</s:iterator>
								</select>
							</td>
						</tr>

						<tr id="tr2ASReSeExhibition_1">
							<td>
								<label for="textASReSeWebpage_1"><s:property value="getText('label.ai.tabs.commons.webpage')" />:</label>
							</td>
							<td>
								<input type="text" id="textASReSeWebpage_1" />
							</td>
							<td class="labelLeft">
								<label for="textASReSeWebpageLinkTitle_1"><s:property value="getText('label.ai.tabs.commons.linkTitle')" />:</label>
							</td>
							<td>
								<input type="text" id="textASReSeWebpageLinkTitle_1" />
							</td>
						</tr>
					</s:else>
				</s:if>
				<s:else>
					<tr id="trASReSeExhibition_1">
						<td>
							<label for="textASReSeExhibition_1"><s:property value="getText('label.ai.accessAndServices.exhibition')" />:</label>
						</td>
						<td>
							<input type="text" id="textASReSeExhibition_1" />
						</td>
						<td class="labelLeft">
							<label for="selectASReSeExhibitionSelectLanguage_1"><s:property value="getText('label.ai.tabs.commons.selectLanguage')"/>:</label>
						</td>
						<td>
							<select id="selectASReSeExhibitionSelectLanguage_1">
								<s:iterator value="languageList" var="language"> 
									<option value="<s:property value="#language.key" />"><s:property value="#language.value" /></option>
								</s:iterator>
							</select>
						</td>
					</tr>

					<tr id="tr2ASReSeExhibition_1">
						<td>
							<label for="textASReSeWebpage_1"><s:property value="getText('label.ai.tabs.commons.webpage')" />:</label>
						</td>
						<td>
							<input type="text" id="textASReSeWebpage_1" />
						</td>
						<td class="labelLeft">
							<label for="textASReSeWebpageLinkTitle_1"><s:property value="getText('label.ai.tabs.commons.linkTitle')" />:</label>
						</td>
						<td>
							<input type="text" id="textASReSeWebpageLinkTitle_1" />
						</td>
					</tr>
				</s:else>

				<tr>
					<td colspan="2">
						<input type="button" id="buttonASReSeAddExhibition" value="<s:property value="getText('label.ai.accessAndServices.addExhibition')"/>" onclick="aSReSeAddExhibition('<s:property value="getText('label.ai.tabs.commons.pleaseFillData')" />');" />
					</td>
					<td colspan="2">
					</td>
				</tr>

				<s:if test="%{loader.asRSNumberOfToursSessions.size() > 0}">
					<s:set var="numberOfToursSessions" value="loader.asRSNumberOfToursSessions[#counter]"/>
					<s:set var="rsToursSessions" value="loader.asRSToursSessions[#counter]"/>
					<s:set var="rsToursSessionsLang" value="loader.asRSToursSessionsLang[#counter]"/>
					<s:set var="rsToursSessionsWebpageHref" value="loader.asRSToursSessionsWebpageHref[#counter]"/>
					<s:set var="rsToursSessionsWebpageTitle" value="loader.asRSToursSessionsWebpageTitle[#counter]"/>
					<s:if test="%{#numberOfToursSessions.size() > 0}">
						<s:iterator var="internalCurrent" value="#numberOfToursSessions" status="internalStatus">
							<tr id="trASReSeToursAndSessions_<s:property value="%{#internalStatus.index + 1}" />">
								<td>
									<label for="textASReSeToursAndSessions_<s:property value="%{#internalStatus.index + 1}" />"><s:property value="getText('label.ai.accessAndServices.toursAndSessions')" />:</label>
								</td>
								<td>
									<input type="text" id="textASReSeToursAndSessions_<s:property value="%{#internalStatus.index + 1}" />" value="<s:property value="#rsToursSessions[#internalStatus.index]" />" />
								</td>
								<td class="labelLeft">
									<label for="selectASReSeToursAndSessionsSelectLanguage_<s:property value="%{#internalStatus.index + 1}" />" ><s:property value="getText('label.ai.tabs.commons.selectLanguage')" />:</label>
								</td>
								<td>
									<select id="selectASReSeToursAndSessionsSelectLanguage_<s:property value="%{#internalStatus.index + 1}" />">
										<s:iterator value="languageList" var="language"> 
											<option value="<s:property value="#language.key" />"<s:if test="%{#language.key == #rsToursSessionsLang[#internalStatus.index]}" > selected=selected </s:if>><s:property value="#language.value" /></option>
										</s:iterator>
									</select>
								</td>
							</tr>

							<tr id="tr2ASReSeToursAndSessions_<s:property value="%{#internalStatus.index + 1}" />">
								<td>
									<label for="textASReSeTSWebpage_<s:property value="%{#internalStatus.index + 1}" />"><s:property value="getText('label.ai.tabs.commons.webpage')"/>:</label>
								</td>
								<td>
									<input type="text" id="textASReSeTSWebpage_<s:property value="%{#internalStatus.index + 1}" />" value="<s:property value="#rsToursSessionsWebpageHref[#internalStatus.index]" />" />
								</td>
								<td class="labelLeft">
									<label for="textASReSeWebpageTSLinkTitle_<s:property value="%{#internalStatus.index + 1}" />" ><s:property value="getText('label.ai.tabs.commons.linkTitle')" />:</label>
								</td>
								<td>
									<input type="text" id="textASReSeWebpageTSLinkTitle_<s:property value="%{#internalStatus.index + 1}" />" value="<s:property value="#rsToursSessionsWebpageTitle[#internalStatus.index]" />" />
								</td>
							</tr>
						</s:iterator>
					</s:if>
					<s:else>
						<tr id="trASReSeToursAndSessions_1">
							<td>
								<label for="textASReSeToursAndSessions_1" ><s:property value="getText('label.ai.accessAndServices.toursAndSessions')" />:</label>
							</td>
							<td>
								<input type="text" id="textASReSeToursAndSessions_1" />
							</td>
							<td class="labelLeft">
								<label for="selectASReSeToursAndSessionsSelectLanguage_1"><s:property value="getText('label.ai.tabs.commons.selectLanguage')"/>:</label>
							</td>
							<td>
								<select id="selectASReSeToursAndSessionsSelectLanguage_1">
									<s:iterator value="languageList" var="language"> 
										<option value="<s:property value="#language.key" />"><s:property value="#language.value" /></option>
									</s:iterator>
								</select>
							</td>
						</tr>

						<tr id="tr2ASReSeToursAndSessions_1">
							<td>
								<label for="textASReSeTSWebpage_1"><s:property value="getText('label.ai.tabs.commons.webpage')" />:</label>
							</td>
							<td>
								<input type="text" id="textASReSeTSWebpage_1" />
							</td>
							<td class="labelLeft">
								<label for="textASReSeWebpageTSLinkTitle_1"><s:property value="getText('label.ai.tabs.commons.linkTitle')" />:</label>
							</td>
							<td>
								<input type="text" id="textASReSeWebpageTSLinkTitle_1" />
							</td>
						</tr>
					</s:else>
				</s:if>
				<s:else>
					<tr id="trASReSeToursAndSessions_1">
						<td>
							<label for="textASReSeToursAndSessions_1" ><s:property value="getText('label.ai.accessAndServices.toursAndSessions')" />:</label>
						</td>
						<td>
							<input type="text" id="textASReSeToursAndSessions_1" />
						</td>
						<td class="labelLeft">
							<label for="selectASReSeToursAndSessionsSelectLanguage_1"><s:property value="getText('label.ai.tabs.commons.selectLanguage')"/>:</label>
						</td>
						<td>
							<select id="selectASReSeToursAndSessionsSelectLanguage_1">
								<s:iterator value="languageList" var="language"> 
									<option value="<s:property value="#language.key" />"><s:property value="#language.value" /></option>
								</s:iterator>
							</select>
						</td>
					</tr>

					<tr id="tr2ASReSeToursAndSessions_1">
						<td>
							<label for="textASReSeTSWebpage_1"><s:property value="getText('label.ai.tabs.commons.webpage')" />:</label>
						</td>
						<td>
							<input type="text" id="textASReSeTSWebpage_1" />
						</td>
						<td class="labelLeft">
							<label for="textASReSeWebpageTSLinkTitle_1"><s:property value="getText('label.ai.tabs.commons.linkTitle')" />:</label>
						</td>
						<td>
							<input type="text" id="textASReSeWebpageTSLinkTitle_1" />
						</td>
					</tr>
				</s:else>

				<tr>
					<td colspan="2">
						<input type="button" id="buttonASReSeToursAndSessions" value="<s:property value="getText('label.ai.accessAndServices.addToursAndSessions')"/>" onclick="aSReSeToursAndSessions('<s:property value="getText('label.ai.tabs.commons.pleaseFillData')" />');"/>
					</td>
					<td colspan="2">
					</td>
				</tr>

				<s:if test="%{loader.asRSNumberOfOtherServices.size() > 0}">
					<s:set var="numberOfOtherServices" value="loader.asRSNumberOfOtherServices[#counter]"/>
					<s:set var="rsOtherServices" value="loader.asRSOtherServices[#counter]"/>
					<s:set var="rsOtherServicesLang" value="loader.asRSOtherServicesLang[#counter]"/>
					<s:set var="rsOtherServicesWebpageHref" value="loader.asRSOtherServicesWebpageHref[#counter]"/>
					<s:set var="rsOtherServicesWebpageTitle" value="loader.asRSOtherServicesWebpageTitle[#counter]"/>
					<s:if test="%{#numberOfOtherServices.size() > 0}">
						<s:iterator var="internalCurrent" value="#numberOfOtherServices" status="internalStatus">
							<tr id="trASReSeOtherServices_<s:property value="%{#internalStatus.index + 1}" />">
								<td>
									<label for="textASReSeOtherServices_<s:property value="%{#internalStatus.index + 1}" />"><s:property value="getText('label.ai.accessAndServices.otherServices')" />:</label>
								</td>
								<td>
									<input type="text" id="textASReSeOtherServices_<s:property value="%{#internalStatus.index + 1}" />" value="<s:property value="#rsOtherServices[#internalStatus.index]" />" />
								</td>
								<td class="labelLeft">
									<label for="selectASReSeOtherServicesSelectLanguage_<s:property value="%{#internalStatus.index + 1}" />" ><s:property value="getText('label.ai.tabs.commons.selectLanguage')" />:</label>
								</td>
								<td>
									<select id="selectASReSeOtherServicesSelectLanguage_<s:property value="%{#internalStatus.index + 1}" />">
										<s:iterator value="languageList" var="language"> 
											<option value="<s:property value="#language.key" />"<s:if test="%{#language.key == #rsOtherServicesLang[#internalStatus.index]}" > selected=selected </s:if>><s:property value="#language.value" /></option>
										</s:iterator>
									</select>
								</td>
							</tr>

							<tr id="tr2ASReSeOtherServices_<s:property value="%{#internalStatus.index + 1}" />">
								<td>
									<label for="textASReSeOSWebpage_<s:property value="%{#internalStatus.index + 1}" />"><s:property value="getText('label.ai.tabs.commons.webpage')"/>:</label>
								</td>
								<td>
									<input type="text" id="textASReSeOSWebpage_<s:property value="%{#internalStatus.index + 1}" />" value="<s:property value="#rsOtherServicesWebpageHref[#internalStatus.index]" />" />
								</td>
								<td class="labelLeft">
									<label for="textASReSeWebpageOSLinkTitle_<s:property value="%{#internalStatus.index + 1}" />" ><s:property value="getText('label.ai.tabs.commons.linkTitle')" />:</label>
								</td>
								<td>
									<input type="text" id="textASReSeWebpageOSLinkTitle_<s:property value="%{#internalStatus.index + 1}" />" value="<s:property value="#rsOtherServicesWebpageTitle[#internalStatus.index]" />" />
								</td>
							</tr>
						</s:iterator>
					</s:if>
					<s:else>
						<tr id="trASReSeOtherServices_1">
							<td>
								<label for="textASReSeOtherServices_1" ><s:property value="getText('label.ai.accessAndServices.otherServices')" />:</label>
							</td>
							<td>
								<input type="text" id="textASReSeOtherServices_1" />
							</td>
							<td class="labelLeft">
								<label for="selectASReSeOtherServicesSelectLanguage_1"><s:property value="getText('label.ai.tabs.commons.selectLanguage')"/>:</label>
							</td>
							<td>
								<select id="selectASReSeOtherServicesSelectLanguage_1">
									<s:iterator value="languageList" var="language"> 
										<option value="<s:property value="#language.key" />"><s:property value="#language.value" /></option>
									</s:iterator>
								</select>
							</td>
						</tr>

						<tr id="tr2ASReSeOtherServices_1">
							<td>
								<label for="textASReSeOSWebpage_1"><s:property value="getText('label.ai.tabs.commons.webpage')" />:</label>
							</td>
							<td>
								<input type="text" id="textASReSeOSWebpage_1" />
							</td>
							<td class="labelLeft">
								<label for="textASReSeWebpageOSLinkTitle_1"><s:property value="getText('label.ai.tabs.commons.linkTitle')" />:</label>
							</td>
							<td>
								<input type="text" id="textASReSeWebpageOSLinkTitle_1" />
							</td>
						</tr>
					</s:else>
				</s:if>
				<s:else>
					<tr id="trASReSeOtherServices_1">
						<td>
							<label for="textASReSeOtherServices_1" ><s:property value="getText('label.ai.accessAndServices.otherServices')" />:</label>
						</td>
						<td>
							<input type="text" id="textASReSeOtherServices_1" />
						</td>
						<td class="labelLeft">
							<label for="selectASReSeOtherServicesSelectLanguage_1"><s:property value="getText('label.ai.tabs.commons.selectLanguage')"/>:</label>
						</td>
						<td>
							<select id="selectASReSeOtherServicesSelectLanguage_1">
								<s:iterator value="languageList" var="language"> 
									<option value="<s:property value="#language.key" />"><s:property value="#language.value" /></option>
								</s:iterator>
							</select>
						</td>
					</tr>

					<tr id="tr2ASReSeOtherServices_1">
						<td>
							<label for="textASReSeOSWebpage_1"><s:property value="getText('label.ai.tabs.commons.webpage')" />:</label>
						</td>
						<td>
							<input type="text" id="textASReSeOSWebpage_1" />
						</td>
						<td class="labelLeft">
							<label for="textASReSeWebpageOSLinkTitle_1"><s:property value="getText('label.ai.tabs.commons.linkTitle')" />:</label>
						</td>
						<td>
							<input type="text" id="textASReSeWebpageOSLinkTitle_1" />
						</td>
					</tr>
				</s:else>

				<tr>
					<td colspan="2">
						<input type="button" id="buttonASAddServices" value="<s:property value="getText('label.ai.accessAndServices.addSevices')"/>" onclick="aSAddServices('<s:property value="getText('label.ai.tabs.commons.pleaseFillData')" />');"/>
					</td>
					<td colspan="2">
					</td>
				</tr>

				<tr>
					<td id="tdButtonsAccessAndServiceTab" colspan="4">
						<input type="button" id="buttonAccessAndServiceTabNext" value="<s:property value='getText("label.ai.tabs.commons.button.nextTab")' />" class="rightButton" onclick="checkAndShowNextTab($(this).parent().parent().parent().parent(), '<s:property value="getText('label.ai.tabs.commons.fieldRequired')" />', '<s:property value="getText('label.ai.tabs.commons.pleaseFillMandatoryFields')" />','<s:property value="getText('label.eag.eagwithurlwarnings')"/>');" />
						<input type="button" id="buttonAccessAndServiceTabPrevious" value="<s:property value='getText("label.ai.tabs.commons.button.previousTab")' />" class="rightButton" onclick="checkAndShowPreviousTab($(this).parent().parent().parent().parent(), '<s:property value="getText('label.ai.tabs.commons.fieldRequired')" />', '<s:property value="getText('label.ai.tabs.commons.pleaseFillMandatoryFields')" />','<s:property value="getText('label.eag.eagwithurlwarnings')"/>');" />
					</td>
				</tr>
			</table>

			<s:set var="counter" value="%{#counter + 1}"/>
		</s:iterator>
	</div>
</s:if>

<div id="accessAndServicesTabContent" style="display:none;">
	<table id="accessAndServicesTable" class="tablePadding">
		<tr id="trASOpeningTimes_1">
			<td id="tdOpeningTimes_1">
				<label for="textOpeningTimes_1"><s:property value="getText('label.ai.tabs.commons.openingTimes')" /><span class="required">*</span>:</label>
			</td>
			<td>
				<input type="text" id="textOpeningTimes_1" onchange="aSOpeningHoursOfInstitutionChanged($(this));" />
			</td>
			<td class="labelLeft">
				<label for="selectLanguageOpeningTimes_1" ><s:property value="getText('label.ai.tabs.commons.selectLanguage')" />:</label>
			</td>
			<td>
				<select id="selectLanguageOpeningTimes_1" onchange="aSOpeningHoursOfInstitutionLangChanged($(this));">
					<s:iterator value="languageList" var="language"> 
						<option value="<s:property value="#language.key" />"><s:property value="#language.value" /></option>
					</s:iterator>
				</select>
			</td>
		</tr>

		<tr>
			<td id="tdASAddOpeningTimes" colspan="2">
				<input type="button" id="buttonASAddOpeningTimes"  value="<s:property value='getText("label.ai.accessAndServices.addOpeningTimes")' />" onclick="aSAddOpeningTimes('<s:property value="getText('label.ai.tabs.commons.pleaseFillData')" />');" />
			</td>
			<td colspan="2">
			</td>
		</tr>

		<tr id="trASClosingDates_1">
			<td>
				<label for="textClosingDates_1"><s:property value="getText('label.ai.tabs.commons.closingDates')" />:</label>
			</td>
			<td>
				<input type="text" id="textClosingDates_1" onchange="aSClosingHoursOfInstitutionChanged($(this));" />
			</td>
			<td class="labelLeft">
				<label for="selectLanguageClosingDates_1" ><s:property value="getText('label.ai.tabs.commons.selectLanguage')" />:</label>
			</td>
			<td>
				<select id="selectLanguageClosingDates_1" onchange="aSClosingHoursOfInstitutionLangChanged($(this));">
					<s:iterator value="languageList" var="language"> 
						<option value="<s:property value="#language.key" />"><s:property value="#language.value" /></option>
					</s:iterator>
				</select>
			</td>
		</tr>

		<tr>
			<td id="tdASAddClosingDates" colspan="2">
				<input type="button" id="buttonASAddClosingDates"  value="<s:property value='getText("label.ai.accessAndServices.addClosingDates")' />" onclick="aSAddClosingDates('<s:property value="getText('label.ai.tabs.commons.pleaseFillData')" />');" />
			</td>
			<td colspan="2">
			</td>
		</tr>

		<tr id="trTravellingDirections_1">
			<td>
				<label for="textTravellingDirections_1"><s:property value="getText('label.ai.accessAndServices.travellingDirections')" />:</label>
			</td>
			<td>
				<textarea id="textTravellingDirections_1"></textarea>
			</td>
			<td class="labelLeft">
				<label for="selectASATDSelectLanguage_1"><s:property value="getText('label.ai.tabs.commons.selectLanguage')"/>:</label>
			</td>
			<td>
				<select id="selectASATDSelectLanguage_1">
					<s:iterator value="languageList" var="language"> 
						<option value="<s:property value="#language.key" />"><s:property value="#language.value" /></option>
					</s:iterator>
				</select>
			</td>
		</tr>

		<tr id="tr2TravellingDirections_1">
			<td>
				<label for="textTravelLink_1"><s:property value="getText('label.ai.accessAndServices.link')"/>:</label>
			</td>
			<td colspan="2">
				<input type="text" id="textTravelLink_1" />
			</td>
			<td colspan="2">
			</td>
		</tr>

		<tr>
			<td colspan="2">
				<input type="button" id="buttonASAddTravellingDirections" value="<s:property value="getText('label.ai.accessAndServices.addTravellingDirections')"/>" onclick="aSAddTravellingDirections('<s:property value="getText('label.ai.tabs.commons.pleaseFillData')" />');"/>
			</td>
			<td colspan="2">
			</td>
		</tr>

		<tr>
			<td>
				<label for="selectASAccesibleToThePublic"><s:property value="getText('label.ai.accessAndServices.accesibleToThePublic')"/><span class="required">*</span>:</label>
			</td>
			<td>
				<select id="selectASAccesibleToThePublic" onchange="aSAccessibleToThePublicChanged();">
					<s:iterator value="yesNoList" var="yesno"> 
						<option value="<s:property value="#yesno.key" />"><s:property value="#yesno.value" /></option>
					</s:iterator>
				</select>
			</td>
			<td colspan="2">
			</td>
		</tr>

		<tr id="trASAccessRestrictions_1">
			<td>
				<label for="textASAccessRestrictions_1"><s:property value="getText('label.ai.accessAndServices.accessRestrictions')" />:</label>
			</td>
			<td>
				<input type="text" id="textASAccessRestrictions_1" onchange="aSFutherAccessInformationChanged($(this));" />
			</td>
			<td class="labelLeft">
				<label for="selectASARSelectLanguage_1"><s:property value="getText('label.ai.tabs.commons.selectLanguage')"/>:</label>
			</td>
			<td>
				<select id="selectASARSelectLanguage_1" onchange="aSFutherAccessInformationLangChanged($(this));">
					<s:iterator value="languageList" var="language"> 
						<option value="<s:property value="#language.key" />"><s:property value="#language.value" /></option>
					</s:iterator>
				</select>
			</td>
		</tr>

		<tr>
			<td colspan="2">
				<input type="button" id="buttonAddFutherAccessInformation" value="<s:property value="getText('label.ai.accessAndServices.addFutherAccessInformation')"/>" onclick="addFutherAccessInformation('<s:property value="getText('label.ai.tabs.commons.pleaseFillData')" />');" />
			</td>
			<td colspan="2">
			</td>
		</tr>

		<tr id="trASAddFutherTermOfUse_1">
			<td>
				<label for="textASTermOfUse_1"><s:property value="getText('label.ai.accessAndServices.termsOfUse')" />:</label>
			</td>
			<td>
				<textarea id="textASTermOfUse_1"></textarea>
			</td>
			<td class="labelLeft">
				<label for="selectASAFTOUSelectLanguage_1"><s:property value="getText('label.ai.tabs.commons.selectLanguage')"/>:</label>
			</td>
			<td>
				<select id="selectASAFTOUSelectLanguage_1">
					<s:iterator value="languageList" var="language"> 
						<option value="<s:property value="#language.key" />"><s:property value="#language.value" /></option>
					</s:iterator>
				</select>
			</td>
		</tr>

		<tr id="tr2ASAddFutherTermOfUse_1">
			<td>
				<label for="textASTOULink_1"><s:property value="getText('label.ai.accessAndServices.link')"/>:</label>
			</td>
			<td>
				<input type="text" id="textASTOULink_1" />
			</td>
			<td colspan="2">
			</td>
		</tr>

		<tr>
			<td colspan="2">
				<input type="button" id="buttonASAddFutherTermOfUse" value="<s:property value="getText('label.ai.accessAndServices.addFurtherTermsOfUse')"/>" onclick="aSAddFutherTermOfUse('<s:property value="getText('label.ai.tabs.commons.pleaseFillData')" />');" />
			</td>
			<td colspan="2">
			</td>
		</tr>

		<tr>
			<td>
				<label for="selectASFacilitiesForDisabledPeopleAvailable"><s:property value="getText('label.ai.accessAndServices.facilitiesForDisabledPeopleAvailable')"/><span class="required">*</span>:</label>
			</td>
			<td>
				<select id="selectASFacilitiesForDisabledPeopleAvailable" onchange="aSFacilitiesForDisabledPeopleAvailableChanged();">
					<s:iterator value="yesNoList" var="language"> 
						<option value="<s:property value="#language.key" />"><s:property value="#language.value" /></option>
					</s:iterator>
				</select>
			</td>
		</tr>

		<tr id="trAccessibilityInformation_1">
			<td>
				<label for="textASAccessibility_1"><s:property value="getText('label.ai.accessAndServices.accesibility')" />:</label>
			</td>
			<td>
				<input type="text" id="textASAccessibility_1" onchange="aSFutherInformationOnExistingFacilitiesChanged($(this));" />
			</td>
			<td class="labelLeft">
				<label for="selectASASelectLanguage_1"><s:property value="getText('label.ai.tabs.commons.selectLanguage')" />:</label>
			</td>
			<td>
				<select id="selectASASelectLanguage_1" onchange="aSFutherInformationOnExistingFacilitiesLangChanged($(this));">
					<s:iterator value="languageList" var="language"> 
						<option value="<s:property value="#language.key" />"><s:property value="#language.value" /></option>
					</s:iterator>
				</select>
			</td>
		</tr>

		<tr>
			<td colspan="2">
				<input type="button" id="buttonAddAccessibilityInformation" value="<s:property value="getText('label.ai.accessAndServices.addAccessibilityInformation')"/>" onclick="addAccessibilityInformation('<s:property value="getText('label.ai.tabs.commons.pleaseFillData')" />');" />
			</td>
			<td colspan="2">
			</td>
		</tr>

		<tr>
			<td id="searchroomLabel" colspan="4">
				<span><s:property value="getText('label.ai.accessAndServices.searchroom')" /></span>
			</td>
		</tr>

		<tr>
			<td>
				<label for="textASSRTelephone_1"><s:property value="getText('label.ai.tabs.commons.telephone')"/>:</label>
			</td>
			<td>
				<input type="text" id="textASSRTelephone_1" />
			</td>
			<td colspan="2">
			</td>
		</tr>

		<tr>
			<td>
				<label for="textASSREmailAddress"><s:property value="getText('label.ai.tabs.commons.emailAddress')"/>:</label>
			</td>
			<td>
				<input type="text" id="textASSREmailAddress" />
			</td>
			<td class="labelLeft">
				<label for="textASSREmailLinkTitle"><s:property value="getText('label.ai.tabs.commons.linkTitle')"/>:</label>
			</td>
			<td>
				<input type="text" id="textASSREmailLinkTitle" />
			</td>
		</tr>

		<tr>
			<td>
				<label for="textASSRWebpage"><s:property value="getText('label.ai.tabs.commons.webpage')"/>:</label>
			</td>
			<td>
				<input type="text" id="textASSRWebpage" />
			</td>
			<td class="labelLeft">
				<label for="textASSRWebpageLinkTitle"><s:property value="getText('label.ai.tabs.commons.linkTitle')"/>:</label>
			</td>
			<td>
				<input type="text" id="textASSRWebpageLinkTitle" />
			</td>
		</tr>

		<tr>
			<td>
				<label for="textASSRWorkPlaces"><s:property value="getText('label.ai.accessAndServices.workPlaces')"/>:</label>
			</td>
			<td>
				<input type="text" id="textASSRWorkPlaces" />
			</td>
			<td colspan="2">
			</td>
		</tr>

		<tr>
			<td>
				<label for="textASSRComputerPlaces"><s:property value="getText('label.ai.accessAndServices.computerPlaces')"/>:</label>
			</td>
			<td>
				<input type="text" id="textASSRComputerPlaces" />
			</td>
			<td colspan="2">
			</td>
		</tr>

		<tr id="trASSRAddDescriptionOfYourComputerPlaces">
			<td colspan="3">
				<input type="button" id="buttonASSRAddadescriptionofyourcomputerplaces" value="<s:property value="getText('label.ai.accessAndServices.addDescriptionOfYourComputerPlaces')"/>" onclick="aSSRAddDescriptionOfYourComputerPlaces('<s:property value="getText('label.ai.accessAndServices.descriptionOfComputerPlaces')"/>', '<s:property value="getText('label.ai.tabs.commons.selectLanguage')" />', '<s:property value="getText('label.ai.tabs.commons.pleaseFillData')" />');" />
			</td>
			<td>
			</td>
		</tr>

		<tr>
			<td>
				<label for="textASSRMicrofilmPlaces"><s:property value="getText('label.ai.accessAndServices.microfilmPlaces')"/>:</label>
			</td>
			<td>
				<input type="text" id="textASSRMicrofilmPlaces" />
			</td>
			<td class="labelLeft">
				<label for="selectASSRPhotographAllowance"><s:property value="getText('label.ai.accessAndServices.photographAllowance')"/>:</label>
			</td>
			<td>
				<select id="selectASSRPhotographAllowance">
					<s:iterator value="photographList" var="photograph"> 
						<option value="<s:property value="#photograph.key" />"><s:property value="#photograph.value" /></option>
					</s:iterator>
				</select>
			</td>
		</tr>

		<tr id="trASSRReadersTicket_1">
			<td>
				<label for="textASSRReadersTicket_1"><s:property value="getText('label.ai.accessAndServices.readersTicket')"/>:</label>
			</td>
			<td>
				<input type="text" id="textASSRReadersTicket_1" />
			</td>
			<td class="labelLeft">
				<label for="selectReadersTickectLanguage_1"><s:property value="getText('label.ai.tabs.commons.selectLanguage')"/>:</label>
			</td>
			<td>
				<select id="selectReadersTickectLanguage_1">
					<s:iterator value="languageList" var="language"> 
						<option value="<s:property value="#language.key" />"><s:property value="#language.value" /></option>
					</s:iterator>
				</select>
			</td>
		</tr>

		<tr id="tr2ASSRReadersTicket_1">
			<td>
				<label for="textASSRRTLink_1"><s:property value="getText('label.ai.accessAndServices.link')"/>:</label>
			</td>
			<td>
				<input type="text" id="textASSRRTLink_1" />
			</td>
			<td colspan="2">
			</td>
		</tr>

		<tr>
			<td colspan="2">
				<input type="button" id="buttonASSRAddReadersTicket" value="<s:property value="getText('label.ai.accessAndServices.addReadersTicket')"/>" onclick="aSSRAddReadersTicket('<s:property value="getText('label.ai.tabs.commons.pleaseFillData')" />');"/>
			</td>
			<td colspan="2">
			</td>
		</tr>

		<tr id="trASSRAddFurtherOrderInformation_1">
			<td>
				<label for="textASSRAdvancedOrders_1"><s:property value="getText('label.ai.accessAndServices.advancedOrders')" />:</label>
			</td>
			<td>
				<input type="text" id="textASSRAdvancedOrders_1" />
			</td>
			<td class="labelLeft">
				<label for="selectASSRAFOIUSelectLanguage_1"><s:property value="getText('label.ai.tabs.commons.selectLanguage')"/>:</label>
			</td>
			<td>
				<select id="selectASSRAFOIUSelectLanguage_1">
					<s:iterator value="languageList" var="language"> 
						<option value="<s:property value="#language.key" />"><s:property value="#language.value" /></option>
					</s:iterator>
				</select>
			</td>
		</tr>

		<tr id="tr2ASSRAddFurtherOrderInformation_1">
			<td>
				<label for="textASSRAOLink_1"><s:property value="getText('label.ai.accessAndServices.link')" />:</label>
			</td>
			<td>
				<input type="text" id="textASSRAOLink_1" />
			</td>
			<td colspan="2">
			</td>
		</tr>

		<tr>
			<td colspan="2">
				<input type="button" id="buttonASSRAddFurtherOrderInformation" value="<s:property value="getText('label.ai.accessAndServices.addFurtherOrderInformation')" />" onclick="aSSRAddFurtherOrderInformation('<s:property value="getText('label.ai.tabs.commons.pleaseFillData')" />');" />
			</td>
			<td colspan="2">
			</td>
		</tr>

		<tr id="trASSRResearchServices_1">
			<td>
				<label for="textASSRResearchServices_1"><s:property value="getText('label.ai.accessAndServices.researchServices')"/>:</label>
			</td>
			<td>
				<input type="text" id="textASSRResearchServices_1" />
			</td>
			<td class="labelLeft">
				<label for="textASSRRSSelectLanguage_1"><s:property value="getText('label.ai.tabs.commons.selectLanguage')"/>:</label>
			</td>
			<td>
				<select id="textASSRRSSelectLanguage_1">
					<s:iterator value="languageList" var="language"> 
						<option value="<s:property value="#language.key" />"><s:property value="#language.value" /></option>
					</s:iterator>
				</select>
			</td>
		</tr>

		<tr>
			<td colspan="2">
				<input type="button" id="buttonASAddResearchServices" value="<s:property value="getText('label.ai.accessAndServices.addResearchServices')"/>" onclick="aSAddResearchServices('<s:property value="getText('label.ai.tabs.commons.pleaseFillData')" />');" />
			</td>
			<td colspan="2">
			</td>
		</tr>

		<tr>
			<td id="libraryLabel">
				<span><s:property value="getText('label.ai.accessAndServices.library')" /></span>
			</td>
			<td>
				<select id="selectASLibrary">
					<s:iterator value="noneYesNoList" var="yesno"> 
						<option value="<s:property value="#yesno.key" />"><s:property value="#yesno.value" /></option>
					</s:iterator>
				</select>
			</td>
			<td colspan="2">
			</td>
		</tr>

		<tr>
			<td>
				<label for="textASLTelephone_1"><s:property value="getText('label.ai.tabs.commons.telephone')" />:</label>
			</td>
			<td>
				<input type="text" id="textASLTelephone_1" />
			</td>
			<td colspan="2">
			</td>
		</tr>

		<tr>
			<td>
				<label for="textASLEmailAddress"><s:property value="getText('label.ai.tabs.commons.emailAddress')" />:</label>
			</td>
			<td>
				<input type="text" id="textASLEmailAddress" />
			</td>
			<td class="labelLeft">
				<label for="textASLEmailLinkTitle"><s:property value="getText('label.ai.tabs.commons.linkTitle')"/>:</label>
			</td>
			<td>
				<input type="text" id="textASLEmailLinkTitle" />
			</td>
		</tr>

		<tr>
			<td>
				<label for="textASLWebpage"><s:property value="getText('label.ai.tabs.commons.webpage')" />:</label>
			</td>
			<td>
				<input type="text" id="textASLWebpage" />
			</td>
			<td class="labelLeft">
				<label for="textASLWebpageLinkTitle"><s:property value="getText('label.ai.tabs.commons.linkTitle')"/>:</label>
			</td>
			<td>
				<input type="text" id="textASLWebpageLinkTitle" />
			</td>
		</tr>

		<tr>
			<td>
				<label for="textASLMonographocPublication"><s:property value="getText('label.ai.accessAndServices.monographicPublication')" />:</label>
			</td>
			<td>
				<input type="text" id="textASLMonographocPublication" />
			</td>
			<td class="labelLeft">
				<label for="textASLSerialPublication"><s:property value="getText('label.ai.accessAndServices.serialPublication')"/>:</label>
			</td>
			<td>
				<input type="text" id="textASLSerialPublication" />
			</td>
		</tr>

		<tr>
			<td id="internetAccessLabel">
				<label for="selectASInternetAccess"><span><s:property value="getText('label.ai.accessAndServices.internetAccess')" /></span>:</label>
			</td>
			<td>
				<select id="selectASInternetAccess">
					<s:iterator value="noneYesNoList" var="yesno"> 
						<option value="<s:property value="#yesno.key" />"><s:property value="#yesno.value" /></option>
					</s:iterator>
				</select>
			</td>
			<td colspan="2">
			</td>
		</tr>

		<tr id="trASPIAAddInternetAccessInformation_1">
			<td>
				<label for="textASDescription_1"><span><s:property value="getText('label.ai.accessAndServices.description')" /></span>:</label>
			</td>
			<td>
				<input type="text" id="textASDescription_1" />
			</td>
			<td class="labelLeft">
				<label for="selectASDSelectLanguage_1"><s:property value="getText('label.ai.tabs.commons.selectLanguage')"/>:</label>
			</td>
			<td>
				<select id="selectASDSelectLanguage_1">
					<s:iterator value="languageList" var="language">
						<option value="<s:property value="#language.key" />"><s:property value="#language.value" /></option>
					</s:iterator>
				</select>
			</td>
		</tr>

		<tr>
			<td colspan="3">
				<input type="button" id="buttonASPIAAddInternetAccessInformation" value="<s:property value="getText('label.ai.accessAndServices.addInternetAccessInformation')" />" onclick="aSPIAAddInternetAccessInformation('<s:property value="getText('label.ai.tabs.commons.pleaseFillData')" />');" />
			</td>
			<td>
			</td>
		</tr>

		<tr>
			<td id="technicalServicesLabel" colspan="4">
				<span><s:property value="getText('label.ai.accessAndServices.technicalServices')" /></span>
			</td>
		</tr>

		<tr>
			<td id="restaurationLabLabel">
				<span><label for="selectASTSRestaurationLab"><s:property value="getText('label.ai.accessAndServices.restaurationLab')" />:</label></span>
			</td>
			<td>
				<select id="selectASTSRestaurationLab">
					<s:iterator value="noneYesNoList" var="yesno"> 
						<option value="<s:property value="#yesno.key" />"><s:property value="#yesno.value" /></option>
					</s:iterator>
				</select>
			</td>
			<td colspan="2">
			</td>
		</tr>

		<tr id="trASTSDescriptionOfRestaurationLab_1">
			<td>
				<label for="textASTSDescriptionOfRestaurationLab_1"><span><s:property value="getText('label.ai.accessAndServices.description')" /></span>:</label>
			</td>
			<td>
				<input type="text" id="textASTSDescriptionOfRestaurationLab_1" />
			</td>
			<td class="labelLeft">
				<label for="selectASTSSelectLanguage_1"><s:property value="getText('label.ai.tabs.commons.selectLanguage')"/>:</label>
			</td>
			<td>
				<select id="selectASTSSelectLanguage_1">
					<s:iterator value="languageList" var="language"> 
						<option value="<s:property value="#language.key" />"><s:property value="#language.value" /></option>
					</s:iterator>
				</select>
			</td>
		</tr>

		<tr>
			<td colspan="3">
				<input type="button" id="buttonAddADescriptionOfYourRestaurationLab" value="<s:property value="getText('label.ai.accessAndServices.addDescriptionOfYourRestaurationLab')"/>" onclick="addADescriptionOfYourRestaurationLab('<s:property value="getText('label.ai.tabs.commons.pleaseFillData')" />');"/>
			</td>
			<td>
			</td>
		</tr>

		<tr>
			<td>
				<label for="textASTSTelephone_1"><s:property value="getText('label.ai.tabs.commons.telephone')" />:</label>
			</td>
			<td>
				<input type="text" id="textASTSTelephone_1" />
			</td>
			<td colspan="2">
			</td>
		</tr>

		<tr>
			<td>
				<label for="textASRSEmail"><s:property value="getText('label.ai.tabs.commons.emailAddress')" />:</label>
			</td>
			<td>
				<input type="text" id="textASRSEmail" />
			</td>
			<td class="labelLeft">
				<label for="textASRSEmailLinkTitle"><s:property value="getText('label.ai.tabs.commons.linkTitle')"/>:</label>
			</td>
			<td>
				<input type="text" id="textASRSEmailLinkTitle" />
			</td>
		</tr>

		<tr>
			<td>
				<label for="textASRSWebpage"><s:property value="getText('label.ai.tabs.commons.webpage')" />:</label>
			</td>
			<td>
				<input type="text" id="textASRSWebpage" />
			</td>
			<td class="labelLeft">
				<label for="textASRSWebpageLinkTitle"><s:property value="getText('label.ai.tabs.commons.linkTitle')"/>:</label>
			</td>
			<td>
				<input type="text" id="textASRSWebpageLinkTitle" />
			</td>
		</tr>

		<tr>
			<td id="reproductionServiceLabel">
				<label for="selectASTSReproductionService"><s:property value="getText('label.ai.accessAndServices.reproductionService')"/>:</label>
			</td>
			<td>
				<select id="selectASTSReproductionService">
					<s:iterator value="noneYesNoList" var="yesno"> 
						<option value="<s:property value="#yesno.key" />"><s:property value="#yesno.value" /></option>
					</s:iterator>
				</select>
			</td>
			<td colspan="2">
			</td>
		<tr>

		<tr id="trASTSDescriptionOfReproductionService_1">
			<td>
				<label for="textASTSDescriptionOfReproductionService_1"><span><s:property value="getText('label.ai.accessAndServices.description')" /></span>:</label>
			</td>
			<td>
				<input type="text" id="textASTSDescriptionOfReproductionService_1" />
			</td>
			<td class="labelLeft">
				<label for="selectASTSRSSelectLanguage_1"><s:property value="getText('label.ai.tabs.commons.selectLanguage')"/>:</label>
			</td>
			<td>
				<select id="selectASTSRSSelectLanguage_1">
					<s:iterator value="languageList" var="language"> 
						<option value="<s:property value="#language.key" />"><s:property value="#language.value" /></option>
					</s:iterator>
				</select>
			</td>
		</tr>

		<tr>
			<td colspan="3">
				<input type="button" id="buttonASAddADescriptionOfYourReproductionService"value="<s:property value="getText('label.ai.accessAndServices.addDescriptionOfYourReproductionService')"/>" onclick="aSAddADescriptionOfYourReproductionService('<s:property value="getText('label.ai.tabs.commons.pleaseFillData')" />');" />
			</td>
			<td>
			</td>
		</tr>

		<tr>
			<td>
				<label for="textASTSRSTelephone_1"><s:property value="getText('label.ai.tabs.commons.telephone')" />:</label>
			</td>
			<td>
				<input type="text" id="textASTSRSTelephone_1" />
			</td>
			<td colspan="2">
			</td>
		</tr>

		<tr>
			<td>
				<label for="textASTSRSEmailAddress"><s:property value="getText('label.ai.tabs.commons.emailAddress')" />:</label>
			</td>
			<td>
				<input type="text" id="textASTSRSEmailAddress" />
			</td>
			<td class="labelLeft">
				<label for="textASTSEmailAddressLinkTitle"><s:property value="getText('label.ai.tabs.commons.linkTitle')"/>:</label>
			</td>
			<td>
				<input type="text" id="textASTSEmailAddressLinkTitle" />
			</td>
		</tr>

		<tr>
			<td>
				<label for="textASTSRSWebpage"><s:property value="getText('label.ai.tabs.commons.webpage')" />:</label>
			</td>
			<td>
				<input type="text" id="textASTSRSWebpage" />
			</td>
			<td class="labelLeft">
				<label for="textASTSRSWebpageLinkTitle"><s:property value="getText('label.ai.tabs.commons.linkTitle')"/>:</label>
			</td>
			<td>
				<input type="text" id="textASTSRSWebpageLinkTitle" />
			</td>
		</tr>

		<tr>
			<td>
				<label for="selectASTSRSMicroform"><s:property value="getText('label.ai.accessAndServices.microformServices')" />:</label>
			</td>
			<td>
				<select id="selectASTSRSMicroform">
					<s:iterator value="noneYesNoList" var="yesno"> 
						<option value="<s:property value="#yesno.key" />"><s:property value="#yesno.value" /></option>
					</s:iterator>
				</select>
			</td>
			<td colspan="2">
			</td>
		</tr>

		<tr>
			<td>
				<label for="selectASTSRSPhotographServices"><s:property value="getText('label.ai.accessAndServices.photographServices')" />:</label>
			</td>
			<td>
				<select id="selectASTSRSPhotographServices">
					<s:iterator value="noneYesNoList" var="yesno"> 
						<option value="<s:property value="#yesno.key" />"><s:property value="#yesno.value" /></option>
					</s:iterator>
				</select>
			</td>
			<td colspan="2">
			</td>
		</tr>

		<tr>
			<td>
				<label for="selectASTSRSDigitalServices"><s:property value="getText('label.ai.accessAndServices.digitalServices')" />:</label>
			</td>
			<td>
				<select id="selectASTSRSDigitalServices">
					<s:iterator value="noneYesNoList" var="yesno"> 
						<option value="<s:property value="#yesno.key" />"><s:property value="#yesno.value" /></option>
					</s:iterator>
				</select>
			</td>
			<td colspan="2">
			</td>
		</tr>

		<tr>
			<td>
				<label for="selectASTSRSPhotocopyServices"><s:property value="getText('label.ai.accessAndServices.photocopyServices')" />:</label>
			</td>
			<td>
				<select id="selectASTSRSPhotocopyServices">
					<s:iterator value="noneYesNoList" var="yesno"> 
						<option value="<s:property value="#yesno.key" />"><s:property value="#yesno.value" /></option>
					</s:iterator>
				</select>
			</td>
			<td colspan="2">
			</td>
		</tr>

		<tr>
			<td id="recreationalServiceLabel" colspan="4">
				<span><s:property value="getText('label.ai.accessAndServices.recreationalServices')"/></span>
			</td>
			
		</tr>

		<tr id="trASReSeRefreshment_1">
			<td>
				<label for="textASReSeRefreshment_1"><s:property value="getText('label.ai.accessAndServices.refreshment')" />:</label>
			</td>
			<td>
				<input type="text" id="textASReSeRefreshment_1" />
			</td>
			<td class="labelLeft">
				<label for="selectASReSeRefreshmentSelectLanguage_1"><s:property value="getText('label.ai.tabs.commons.selectLanguage')"/>:</label>
			</td>
			<td>
				<select id="selectASReSeRefreshmentSelectLanguage_1">
					<s:iterator value="languageList" var="language"> 
						<option value="<s:property value="#language.key" />"><s:property value="#language.value" /></option>
					</s:iterator>
				</select>
			</td>
		</tr>

		<tr>
			<td colspan="3">
				<input type="button" id="buttonASReSeAddFurtherRefreshment" value="<s:property value="getText('label.ai.accessAndServices.addFurtherRefreshmentInformation')"/>" onclick="aSReSeAddFurtherRefreshment('<s:property value="getText('label.ai.tabs.commons.pleaseFillData')" />');" />
			</td>
			<td>
			</td>
		</tr>

		<tr id="trASReSeExhibition_1">
			<td>
				<label for="textASReSeExhibition_1"><s:property value="getText('label.ai.accessAndServices.exhibition')" />:</label>
			</td>
			<td>
				<input type="text" id="textASReSeExhibition_1" />
			</td>
			<td class="labelLeft">
				<label for="selectASReSeExhibitionSelectLanguage_1"><s:property value="getText('label.ai.tabs.commons.selectLanguage')"/>:</label>
			</td>
			<td>
				<select id="selectASReSeExhibitionSelectLanguage_1">
					<s:iterator value="languageList" var="language"> 
						<option value="<s:property value="#language.key" />"><s:property value="#language.value" /></option>
					</s:iterator>
				</select>
			</td>
		</tr>

		<tr id="tr2ASReSeExhibition_1">
			<td>
				<label for="textASReSeWebpage_1"><s:property value="getText('label.ai.tabs.commons.webpage')" />:</label>
			</td>
			<td>
				<input type="text" id="textASReSeWebpage_1" />
			</td>
			<td class="labelLeft">
				<label for="textASReSeWebpageLinkTitle_1"><s:property value="getText('label.ai.tabs.commons.linkTitle')" />:</label>
			</td>
			<td>
				<input type="text" id="textASReSeWebpageLinkTitle_1" />
			</td>
		</tr>

		<tr>
			<td colspan="2">
				<input type="button" id="buttonASReSeAddExhibition" value="<s:property value="getText('label.ai.accessAndServices.addExhibition')"/>" onclick="aSReSeAddExhibition('<s:property value="getText('label.ai.tabs.commons.pleaseFillData')" />');" />
			</td>
			<td colspan="2">
			</td>
		</tr>

		<tr id="trASReSeToursAndSessions_1">
			<td>
				<label for="textASReSeToursAndSessions_1" ><s:property value="getText('label.ai.accessAndServices.toursAndSessions')" />:</label>
			</td>
			<td>
				<input type="text" id="textASReSeToursAndSessions_1" />
			</td>
			<td class="labelLeft">
				<label for="selectASReSeToursAndSessionsSelectLanguage_1"><s:property value="getText('label.ai.tabs.commons.selectLanguage')"/>:</label>
			</td>
			<td>
				<select id="selectASReSeToursAndSessionsSelectLanguage_1">
					<s:iterator value="languageList" var="language"> 
						<option value="<s:property value="#language.key" />"><s:property value="#language.value" /></option>
					</s:iterator>
				</select>
			</td>
		</tr>

		<tr id="tr2ASReSeToursAndSessions_1">
			<td>
				<label for="textASReSeTSWebpage_1"><s:property value="getText('label.ai.tabs.commons.webpage')" />:</label>
			</td>
			<td>
				<input type="text" id="textASReSeTSWebpage_1" />
			</td>
			<td class="labelLeft">
				<label for="textASReSeWebpageTSLinkTitle_1"><s:property value="getText('label.ai.tabs.commons.linkTitle')" />:</label>
			</td>
			<td>
				<input type="text" id="textASReSeWebpageTSLinkTitle_1" />
			</td>
		</tr>

		<tr>
			<td colspan="2">
				<input type="button" id="buttonASReSeToursAndSessions" value="<s:property value="getText('label.ai.accessAndServices.addToursAndSessions')"/>" onclick="aSReSeToursAndSessions('<s:property value="getText('label.ai.tabs.commons.pleaseFillData')" />');"/>
			</td>
			<td colspan="2">
			</td>
		</tr>

		<tr id="trASReSeOtherServices_1">
			<td>
				<label for="textASReSeOtherServices_1" ><s:property value="getText('label.ai.accessAndServices.otherServices')" />:</label>
			</td>
			<td>
				<input type="text" id="textASReSeOtherServices_1" />
			</td>
			<td class="labelLeft">
				<label for="selectASReSeOtherServicesSelectLanguage_1"><s:property value="getText('label.ai.tabs.commons.selectLanguage')"/>:</label>
			</td>
			<td>
				<select id="selectASReSeOtherServicesSelectLanguage_1">
					<s:iterator value="languageList" var="language"> 
						<option value="<s:property value="#language.key" />"><s:property value="#language.value" /></option>
					</s:iterator>
				</select>
			</td>
		</tr>

		<tr id="tr2ASReSeOtherServices_1">
			<td>
				<label for="textASReSeOSWebpage_1"><s:property value="getText('label.ai.tabs.commons.webpage')" />:</label>
			</td>
			<td>
				<input type="text" id="textASReSeOSWebpage_1" />
			</td>
			<td class="labelLeft">
				<label for="textASReSeWebpageOSLinkTitle_1"><s:property value="getText('label.ai.tabs.commons.linkTitle')" />:</label>
			</td>
			<td>
				<input type="text" id="textASReSeWebpageOSLinkTitle_1" />
			</td>
		</tr>

		<tr>
			<td colspan="2">
				<input type="button" id="buttonASAddServices" value="<s:property value="getText('label.ai.accessAndServices.addSevices')"/>" onclick="aSAddServices('<s:property value="getText('label.ai.tabs.commons.pleaseFillData')" />');"/>
			</td>
			<td colspan="2">
			</td>
		</tr>

		<tr>
			<td id="tdButtonsAccessAndServiceTab" colspan="4">
				<input type="button" id="buttonAccessAndServiceTabNext" value="<s:property value='getText("label.ai.tabs.commons.button.nextTab")' />" class="rightButton" onclick="checkAndShowNextTab($(this).parent().parent().parent().parent(), '<s:property value="getText('label.ai.tabs.commons.fieldRequired')" />', '<s:property value="getText('label.ai.tabs.commons.pleaseFillMandatoryFields')" />','<s:property value="getText('label.eag.eagwithurlwarnings')"/>');" />
				<input type="button" id="buttonAccessAndServiceTabPrevious" value="<s:property value='getText("label.ai.tabs.commons.button.previousTab")' />" class="rightButton" onclick="checkAndShowPreviousTab($(this).parent().parent().parent().parent(), '<s:property value="getText('label.ai.tabs.commons.fieldRequired')" />', '<s:property value="getText('label.ai.tabs.commons.pleaseFillMandatoryFields')" />','<s:property value="getText('label.eag.eagwithurlwarnings')"/>');" />
			</td>
		</tr>
	</table>
</div>