<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<div id="accessAndServicesTabContent">
	<table id="accessAndServicesTable">
		<tr>
			<td>
				<label for="textOpeningTimes"><s:property value="getText('label.ai.accessAndServices.openingTimes')" /></label>
				<span class="required">*</span>:
			</td>
			<td>
				<input type="text" id="textOpeningTimes" />
				<s:fielderror fieldName="textOpeningTimes"/>
			</td>
			<td class="labelLeft">
				<label for="textClosingDates"><s:property value="getText('label.ai.tabs.commons.closingDates')" />:</label>
			</td>
			<td>
				<input type="text" id="textClosingDates" />
			</td>
		</tr>

		<tr id="trTravellingDirections">
			<td>
				<label for="textTravellingDirections"><s:property value="getText('label.ai.accessAndServices.travellingDirections')" />:</label>
			</td>
			<td rowspan="2">
				<textarea id="textTravellingDirections"></textarea>
			</td>
			<td class="labelLeft">
				<label for="textTravelLink"><s:property value="getText('label.ai.accessAndServices.link')"/>:</label>
			</td>
			<td colspan="2">
				<input type="text" id="textTravelLink" />
			</td>
		</tr>
		<tr id="tr2TravellingDirections">
			<td>
				<input type="button" id="buttonASAddTravellingDirections" value="<s:property value="getText('label.ai.accessAndServices.addTravellingDirections')"/>" onclick="aSAddTravellingDirections();"/>
			</td>
			<td class="labelLeft">
				<label for="selectASATDSelectLanguage"><s:property value="getText('label.ai.tabs.commons.selectLanguage')"/>:</label>
			</td>
			<td>
				<s:select theme="simple" id="selectASATDSelectLanguage" list="languageList"></s:select>
			</td>
		</tr>
		<tr>
			<td>
				<label for="selectASAccesibleToThePublic"><s:property value="getText('label.ai.accessAndServices.accesibleToThePublic')"/></label>
				<span class="required">*</span>:
			</td>
			<td>
				<s:select theme="simple" id="selectASAccesibleToThePublic" list="yesNoList"></s:select>
				<s:fielderror fieldName="yesNoSelectASAccesibleToThePublic"/>
			</td>
			<td colspan="2">
			</td>
		</tr>

		<tr id="trASAccessRestrictions">
			<td>
				<label for="textASAccessRestrictions"><s:property value="getText('label.ai.accessAndServices.accessRestrictions')" />:</label>
			</td>
			<td>
				<input type="text" id="textASAccessRestrictions" />
			</td>
			<td class="labelLeft">
				<label for="selectASARSelectLanguage"><s:property value="getText('label.ai.tabs.commons.selectLanguage')"/>:</label>
			</td>
			<td>
				<s:select theme="simple" id="selectASARSelectLanguage" list="languageList"></s:select>
			</td>
		</tr>

		<tr>
			<td colspan="2">
				<input type="button" id="buttonAddFutherAccessInformation" value="<s:property value="getText('label.ai.accessAndServices.addFutherAccessInformation')"/>" onclick="addFutherAccessInformation();" />
			</td>
			<td colspan="2">
			</td>
		</tr>

		<tr id="trASAddFutherTermOfUse">
			<td>
				<label for="textASTermOfUse"><s:property value="getText('label.ai.accessAndServices.termsOfUse')" />:</label>
			</td>
			<td rowspan="2">
				<textarea id="textASTermOfUse"></textarea>
			</td>
			<td class="labelLeft">
				<label for="textASTOULink"><s:property value="getText('label.ai.accessAndServices.link')"/>:</label>
			</td>
			<td colspan="2">
				<input type="text" id="textASTOULink" />
			</td>
		</tr>

		<tr id="tr2ASAddFutherTermOfUse">
			<td>
				<input type="button" id="buttonASAddFutherTermOfUse" value="<s:property value="getText('label.ai.accessAndServices.addFurtherTermsOfUse')"/>" onclick="aSAddFutherTermOfUse();" />
			</td>
			<td class="labelLeft">
				<label for="selectASAFTOUSelectLanguage"><s:property value="getText('label.ai.tabs.commons.selectLanguage')"/>:</label>
			</td>
			<td>
				<s:select theme="simple" id="selectASAFTOUSelectLanguage" list="languageList"></s:select>
			</td>
		</tr>
		<tr>
			<td>
				<label for="selectASFacilitiesForDisabledPeopleAvailable"><s:property value="getText('label.ai.accessAndServices.facilitiesForDisabledPeopleAvailable')"/></label>
				<span class="required">*</span>:
			</td>
			<td>
				<s:select theme="simple" id="selectASFacilitiesForDisabledPeopleAvailable" list="yesNoList"></s:select>
				<s:fielderror fieldName="yesNoSelectASFacilitiesForDisabledPeopleAvailable"/>
			</td>
		</tr>

		<tr id="trAccessibilityInformation">
			<td>
				<label for="textASAccessibility"><s:property value="getText('label.ai.accessAndServices.accesibility')" />:</label>
			</td>
			<td>
				<input type="text" id="textASAccessibility" />
			</td>
			<td class="labelLeft">
				<label for="selectASASelectLanguage"><s:property value="getText('label.ai.tabs.commons.selectLanguage')"/>:</label>
			</td>
			<td>
				<s:select theme="simple" id="selectASASelectLanguage" list="languageList"></s:select>
			</td>
		</tr>

		<tr>
			<td colspan="2">
				<input type="button" id="buttonAddAccessibilityInformation" value="<s:property value="getText('label.ai.accessAndServices.addAccessibilityInformation')"/>" onclick="addAccessibilityInformation();" />
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
				<label for="textASSRTelephone"><s:property value="getText('label.ai.tabs.commons.telephone')"/>:</label>
			</td>
			<td>
				<input type="text" id="textASSRTelephone" />
			</td>
			<td colspan="2">
			</td>
		</tr>

		<tr>
			<td class="labelLeft">
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
				<label for="textASSRWorkPlaces"><s:property value="getText('label.ai.accessAndServices.workPlaces')"/></label>
				<span class="required">*</span>:
			</td>
			<td>
				<input type="text" id="textASSRWorkPlaces" />
				<s:fielderror fieldName="textASSRWorkPlaces"/>
			</td>
			<td colspan="2">
			</td>
		</tr>

		<tr id="trASSRAddadescriptionofyourcomputerplaces">
			<td>
				<label for="textASSRComputerPlaces"><s:property value="getText('label.ai.accessAndServices.computerPlaces')"/>:</label>
			</td>
			<td>
				<input type="text" id="textASSRComputerPlaces" />
			</td>
			<td colspan="2">
				<input type="button" id="buttonASSRAddadescriptionofyourcomputerplaces" value="<s:property value="getText('label.ai.accessAndServices.addDescriptionOfYourComputerPlaces')"/>" onclick="aSSRAddadescriptionofyourcomputerplaces();" />
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
				<s:select theme="simple" id="selectASSRPhotographAllowance" list="photographList"></s:select>
			</td>
		</tr>

		<tr id="trASSRReadersTicket">
			<td>
				<label for="textASSRReadersTicket"><s:property value="getText('label.ai.accessAndServices.readersTicket')"/>:</label>
			</td>
			<td>
				<input type="text" id="textASSRReadersTicket" />
			</td>
			<td class="labelLeft">
				<label for="textASSRRTLink"><s:property value="getText('label.ai.accessAndServices.link')"/>:</label>
			</td>
			<td>
				<input type="text" id="textASSRRTLink" />
			</td>
		</tr>

		<tr>
			<td colspan="2">
				<input type="button" id="buttonASSRAddReadersTicket" value="<s:property value="getText('label.ai.accessAndServices.addReadersTicket')"/>" onclick="aSSRAddReadersTicket();"/>
			</td>
			<td class="labelLeft">
				<label for="selectReadersTickectLanguage"><s:property value="getText('label.ai.tabs.commons.selectLanguage')"/>:</label>
			</td>
			<td>
				<s:select theme="simple" id="selectReadersTickectLanguage" list="languageList"></s:select>
			</td>
		</tr>

		<tr id="trASSRAddFurtherOrderInformation">
			<td>
				<label for="textASSRAdvancedOrders"><s:property value="getText('label.ai.accessAndServices.advancedOrders')" />:</label>
			</td>
			<td>
				<input type="text" id="textASSRAdvancedOrders" />
			</td>
			<td class="labelLeft">
				<label for="textASSRAOLink"><s:property value="getText('label.ai.accessAndServices.link')" />:</label>
			</td>
			<td colspan="2">
				<input type="text" id="textASSRAOLink" />
			</td>
		</tr>

		<tr>
			<td colspan="2">
				<input type="button" id="buttonASSRAddFurtherOrderInformation" value="<s:property value="getText('label.ai.accessAndServices.addFurtherOrderInformation')" />" onclick="aSSRAddFurtherOrderInformation();" />
			</td>
			<td class="labelLeft">
				<label for="selectASSRAFOIUSelectLanguage"><s:property value="getText('label.ai.tabs.commons.selectLanguage')"/>:</label>
			</td>
			<td>
				<s:select theme="simple" id="selectASSRAFOIUSelectLanguage" list="languageList"></s:select>
			</td>
		</tr>

		<tr id="trASSRResearchServices">
			<td>
				<label for="textASSRResearchServices"><s:property value="getText('label.ai.accessAndServices.researchServices')"/>:</label>
			</td>
			<td>
				<input type="text" id="textASSRResearchServices" />
			</td>
			<td class="labelLeft">
				<label for="textASSRRSSelectLanguage"><s:property value="getText('label.ai.tabs.commons.selectLanguage')"/>:</label>
			</td>
			<td>
				<s:select theme="simple" id="textASSRRSSelectLanguage" list="languageList"></s:select>
			</td>
		</tr>

		<tr>
			<td colspan="2">
				<input type="button" id="buttonASAddResearchServices" value="<s:property value="getText('label.ai.accessAndServices.addResearchServices')"/>" onclick="aSAddResearchServices();" />
			</td>
			<td colspan="2">
			</td>
		</tr>

		<tr>
			<td id="libraryLabel">
				<span><s:property value="getText('label.ai.accessAndServices.library')" /></span>
			</td>
			<td>
				<s:select theme="simple" id="selectASLibrary" list="yesNoList"></s:select>
			</td>
			<td colspan="2">
			</td>
		</tr>

		<tr>
			<td>
				<label for="textASLTelephone"><s:property value="getText('label.ai.tabs.commons.telephone')" />:</label>
			</td>
			<td>
				<input type="text" id="textASLTelephone" />
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
				<s:select theme="simple" id="selectASInternetAccess" list="yesNoList"></s:select>
			</td>
			<td colspan="2">
			</td>
		</tr>

		<tr>
			<td>
				<label for="textASDescription"><span><s:property value="getText('label.ai.accessAndServices.description')" /></span>:</label>
			</td>
			<td>
				<input type="text" id="textASDescription" />
			</td>
			<td class="labelLeft">
				<label for="selectASDSelectLanguage"><s:property value="getText('label.ai.tabs.commons.selectLanguage')"/>:</label>
			</td>
			<td>
				<s:select theme="simple" id="selectASDSelectLanguage" list="languageList"></s:select>
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
				<s:select theme="simple" id="selectASTSRestaurationLab" list="yesNoList"></s:select>
			</td>
			<td colspan="2">
				<input type="button" id="buttonAddADescriptionOfYourRestaurationLab" value="<s:property value="getText('label.ai.accessAndServices.addDescriptionOfYourRestaurationLab')"/>" onclick="addADescriptionOfYourRestaurationLab();"/>
			</td>
		</tr>

		<tr>
			<td>
				<label for="textASTSTelephone"><s:property value="getText('label.ai.tabs.commons.telephone')" />:</label>
			</td>
			<td>
				<input type="text" id="textASTSTelephone" />
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
				<input type="text" id="textASRSLinkTitle" />
			</td>
		</tr>

		<tr>
			<td id="reproductionServiceLabel">
				<label for="selectASTSReproductionService"><s:property value="getText('label.ai.accessAndServices.reproductionService')" /></label>
				<span class="required">*</span>:
			</td>
			<td>
				<s:select theme="simple" id="selectASTSReproductionService" list="yesNoList"></s:select>
				<s:fielderror fieldName="yesNotextASTSReproductionService"/>
			</td>
			<td colspan="2">
				<input type="button" id="buttonASAddADescriptionOfYourReproductionService"value="<s:property value="getText('label.ai.accessAndServices.addDescriptionOfYourReproductionService')"/>" onclick="aSAddADescriptionOfYourReproductionService();" />
			</td>
		<tr>

		<tr>
			<td>
				<label for="textASTSRSTelephone"><s:property value="getText('label.ai.tabs.commons.telephone')" />:</label>
			</td>
			<td>
				<input type="text" id="textASTSRSTelephone" />
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
				<label for="textASRSWebpage"><s:property value="getText('label.ai.tabs.commons.webpage')" />:</label>
			</td>
			<td>
				<input type="text" id="textASRSWebpage" />
			</td>
			<td class="labelLeft">
				<label for="textASRSWebpageLinkTitle"><s:property value="getText('label.ai.tabs.commons.linkTitle')"/>:</label>
			</td>
			<td>
				<input type="text" id="textASRSLinkTitle" />
			</td>
		</tr>

		<tr>
			<td>
				<label for="selectASTSRSMicroform"><s:property value="getText('label.ai.accessAndServices.microformServices')" />:</label>
			</td>
			<td>
				<s:select theme="simple" id="selectASTSRSMicroform" list="yesNoList"></s:select>
			</td>
			<td class="labelLeft">
				<label for="textASTSRSMicroformDescription"><s:property value="getText('label.ai.accessAndServices.description')" />:</label>
			</td>
			<td>
				<input type="text" id="textASTSRSMicroformDescription" />
			</td>
		</tr>

		<tr>
			<td>
				<label for="selectASTSRSPhotographServices"><s:property value="getText('label.ai.accessAndServices.photographServices')" />:</label>
			</td>
			<td>
				<s:select theme="simple" id="selectASTSRSPhotographServices" list="yesNoList"></s:select>
			</td>
			<td class="labelLeft">
				<label for="textASTSRSPhotographServicesDescription"><s:property value="getText('label.ai.accessAndServices.description')" />:</label>
			</td>
			<td>
				<input type="text" id="textASTSRSPhotographServicesDescription" />
			</td>
		</tr>

		<tr>
			<td>
				<label for="selectASTSRSDigitalServices"><s:property value="getText('label.ai.accessAndServices.digitalServices')" />:</label>
			</td>
			<td>
				<s:select theme="simple" id="selectASTSRSDigitalServices" list="yesNoList"></s:select>
			</td>
			<td class="labelLeft">
				<label for="textASTSRSDigitalServicesDescription"><s:property value="getText('label.ai.accessAndServices.description')" />:</label>
			</td>
			<td>
				<input type="text" id="textASTSRSDigitalServicesDescription" />
			</td>
		</tr>

		<tr>
			<td>
				<label for="selectASTSRSPhotocopyServices"><s:property value="getText('label.ai.accessAndServices.photocopyServices')" />:</label>
			</td>
			<td>
				<s:select theme="simple" id="selectASTSRSPhotocopyServices" list="yesNoList"></s:select>
			</td>
			<td class="labelLeft">
				<label for="textASTSRSPhotocopyServicesDescription"><s:property value="getText('label.ai.accessAndServices.description')" />:</label>
			</td>
			<td>
				<input type="text" id="textASTSRSPhotocopyServicesDescription" />
			</td>
		</tr>

		<tr>
			<td id="recreationalServiceLabel" colspan="4">
				<span><s:property value="getText('label.ai.accessAndServices.recreationalServices')"/></span>
			</td>
			
		</tr>

		<tr>
			<td>
				<label for="textASReSeRefreshment"><s:property value="getText('label.ai.accessAndServices.refreshment')" />:</label>
			</td>
			<td>
				<input type="text" id="textASReSeRefreshment" />
			</td>
			<td class="labelLeft">
				<label for="selectASReSeRefreshmentSelectLanguage"><s:property value="getText('label.ai.tabs.commons.selectLanguage')"/>:</label>
			</td>
			<td>
				<s:select theme="simple" id="selectASReSeRefreshmentSelectLanguage" list="languageList"></s:select>
			</td>
		</tr>

		<tr id="trASReSeExhibition">
			<td>
				<label for="textASReSeExhibition"><s:property value="getText('label.ai.accessAndServices.exhibition')" />:</label>
			</td>
			<td>
				<input type="text" id="textASReSeExhibition" />
			</td>
			<td class="labelLeft">
				<label for="selectASReSeExhibitionSelectLanguage"><s:property value="getText('label.ai.tabs.commons.selectLanguage')"/>:</label>
			</td>
			<td>
				<s:select theme="simple" id="selectASReSeExhibitionSelectLanguage" list="languageList"></s:select>
			</td>
		</tr>

		<tr>
			<td colspan="2">
				<input type="button" id="buttonASReSeAddExhibition" value="<s:property value="getText('label.ai.accessAndServices.addExhibition')"/>" onclick="aSReSeAddExhibition();" />
			</td>
			<td colspan="2">
			</td>
		</tr>

		<tr>
			<td>
				<label for="textASReSeWebpage"><s:property value="getText('label.ai.tabs.commons.webpage')" />:</label>
			</td>
			<td>
				<input type="text" id="textASReSeWebpage" />
			</td>
			<td class="labelLeft">
				<label for="textASReSeWebpageLinkTitle"><s:property value="getText('label.ai.tabs.commons.linkTitle')" />:</label>
			</td>
			<td>
				<input type="text" id="textASReSeWebpageLinkTitle" />
			</td>
		</tr>

		<tr id="trASReSeToursAndSessions">
			<td>
				<label for="textASReSeToursAndSessions" ><s:property value="getText('label.ai.accessAndServices.toursAndSessions')" /></label>
			</td>
			<td>
				<input type="text" id="textASReSeToursAndSessions" />
			</td>
			<td class="labelLeft">
				<label for="selectASReSeToursAndSessionsSelectLanguage"><s:property value="getText('label.ai.tabs.commons.selectLanguage')"/></label>
			</td>
			<td>
				<s:select theme="simple" id="selectASReSeToursAndSessionsSelectLanguage" list="languageList"></s:select>
			</td>
		</tr>

		<tr>
			<td colspan="2">
				<input type="button" id="buttonASReSeToursAndSessions" value="<s:property value="getText('label.ai.accessAndServices.addToursAndSessions')"/>" onclick="aSReSeToursAndSessions();"/>
			</td>
			<td colspan="2">
			</td>
		</tr>

		<tr>
			<td>
				<label for="textASReSeTSWebpage"><s:property value="getText('label.ai.tabs.commons.webpage')" />:</label>
			</td>
			<td>
				<input type="text" id="textASReSeTSWebpage" />
			</td>
			<td class="labelLeft">
				<label for="textASReSeWebpageTSLinkTitle"><s:property value="getText('label.ai.tabs.commons.linkTitle')" />:</label>
			</td>
			<td>
				<input type="text" id="textASReSeWebpageTSLinkTitle" />
			</td>
		</tr>

		<tr id="trASReSeOtherServices">
			<td>
				<label for="textASReSeOtherServices" ><s:property value="getText('label.ai.accessAndServices.otherServices')" />:</label>
			</td>
			<td>
				<input type="text" id="textASReSeOtherServices" />
			</td>
			<td class="labelLeft">
				<label for="selectASReSeOtherServicesSelectLanguage"><s:property value="getText('label.ai.tabs.commons.selectLanguage')"/>:</label>
			</td>
			<td>
				<s:select theme="simple" id="selectASReSeOtherServicesSelectLanguage" list="languageList"></s:select>
			</td>
		</tr>

		<tr>
			<td colspan="2">
				<input type="button" id="buttonASAddServices" value="<s:property value="getText('label.ai.accessAndServices.addSevices')"/>" onclick="aSAddServices();"/>
			</td>
			<td colspan="2">
			</td>
		</tr>

		<tr>
			<td>
				<label for="textASReSeOSWebpage"><s:property value="getText('label.ai.tabs.commons.webpage')" />:</label>
			</td>
			<td>
				<input type="text" id="textASReSeOSWebpage" />
			</td>
			<td class="labelLeft">
				<label for="textASReSeWebpageOSLinkTitle"><s:property value="getText('label.ai.tabs.commons.linkTitle')" />:</label>
			</td>
			<td>
				<input type="text" id="textASReSeWebpageOSLinkTitle" />
			</td>
		</tr>

		<tr>
			<td id="tdButtonsAccessAndServiceTab" colspan="4">
				<input type="button" id="buttonAccessAndServiceTabNext" value="<s:property value='getText("label.ai.tabs.commons.button.nextTab")' />" class="rightButton" />
				<input type="button" id="buttonAccessAndServicelTabCheck" value="<s:property value='getText("label.ai.tabs.commons.button.check")' />" class="rightButton" />
				<script type="text/javascript">
					//current tab
					$("table#accessAndServicesTable input#buttonAccessAndServicelTabCheck").click(clickAccessAndServicesAction);
				</script>
			</td>
		</tr>
	</table>
</div>