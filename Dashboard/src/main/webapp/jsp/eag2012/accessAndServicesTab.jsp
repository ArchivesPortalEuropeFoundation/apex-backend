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
			<td>
				<label for="textClosingDates"><s:property value="getText('label.ai.accessAndServices.closingDates')" />:</label>
			</td>
			<td>
				<input type="text" id="textClosingDates" />
			</td>
		</tr>

		<tr>
			<td>
				<label for="textTravellingDirections"><s:property value="getText('label.ai.accessAndServices.travellingDirections')" />:</label>
			</td>
			<td rowspan="2">
				<textarea id="textTravellingDirections"></textarea>
			</td>
			<td>
				<label for="textTravelLink"><s:property value="getText('label.ai.accessAndServices.link')"/>:</label>
			</td>
			<td colspan="2">
				<input type="text" id="textTravelLink" />
			</td>
		</tr>

		<tr>
			<td>
				<input type="button" id="buttonASAddTravellingDirections" value="<s:property value="getText('label.ai.accessAndServices.addTravellingDirections')"/>" />
			</td>
			<td>
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
				<s:label>TODO: Yes/No list</s:label>
				<%-- <s:select id="selectASAccesibleToThePublic" list="yesnoList" />--%>
				<s:fielderror fieldName="yesNoSelectASAccesibleToThePublic"/>
			</td>
			<td colspan="2">
			</td>
		</tr>

		<tr>
			<td>
				<label for="textASAccessRestrictions"><s:property value="getText('label.ai.accessAndServices.accessRestrictions')" />:</label>
			</td>
			<td>
				<input type="text" id="textASAccessRestrictions" />
			</td>
			<td>
				<label for="selectASARSelectLanguage"><s:property value="getText('label.ai.tabs.commons.selectLanguage')"/>:</label>
			</td>
			<td>
				<s:select theme="simple" id="selectASARSelectLanguage" list="languageList"></s:select>
			</td>
		</tr>

		<tr>
			<td colspan="2">
				<input type="button" id="buttonAddFutherAccessInformation" value="<s:property value="getText('label.ai.accessAndServices.addFutherAccessInformation')"/>" />
			</td>
			<td colspan="2">
			</td>
		</tr>

		<tr>
			<td>
				<label for="textASTermOfUse"><s:property value="getText('label.ai.accessAndServices.termsOfUse')" />:</label>
			</td>
			<td rowspan="2">
				<textarea id="textASTermOfUse"></textarea>
			</td>
			<td>
				<label for="textASTOULink"><s:property value="getText('label.ai.accessAndServices.link')"/>:</label>
			</td>
			<td colspan="2">
				<input type="text" id="textASTOULink" />
			</td>
		</tr>

		<tr>
			<td>
				<input type="button" id="buttonASAddFutherTermOfUse" value="<s:property value="getText('label.ai.accessAndServices.addFurtherTermsOfUse')"/>" />
			</td>
			<td>
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
				<s:label>TODO: Yes/No list</s:label>
				<%-- <s:select id="selectASFacilitiesForDisabledPeopleAvailable" list="yesnoList" />--%>
				<s:fielderror fieldName="yesNoSelectASFacilitiesForDisabledPeopleAvailable"/>
			</td>
		</tr>

		<tr>
			<td>
				<label for="textASAccessibility"><s:property value="getText('label.ai.accessAndServices.accesibility')" />:</label>
			</td>
			<td>
				<input type="text" id="textASAccessibility" />
			</td>
			<td>
				<label for="selectASASelectLanguage"><s:property value="getText('label.ai.tabs.commons.selectLanguage')"/>:</label>
			</td>
			<td>
				<s:select theme="simple" id="selectASASelectLanguage" list="languageList"></s:select>
			</td>
		</tr>

		<tr>
			<td colspan="2">
				<input type="button" id="buttonAddAccessibilityInformation" value="<s:property value="getText('label.ai.accessAndServices.addAccessibilityInformation')"/>" />
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
			<td>
				<label for="textASSREmailAddress"><s:property value="getText('label.ai.tabs.commons.emailAddress')"/>:</label>
			</td>
			<td>
				<input type="text" id="textASSREmailAddress" />
			</td>
			<td>
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
			<td>
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

		<tr>
			<td>
				<label for="textASSRComputerPlaces"><s:property value="getText('label.ai.accessAndServices.computerPlaces')"/>:</label>
			</td>
			<td>
				<input type="text" id="textASSRComputerPlaces" />
			</td>
			<td colspan="2">
				<input type="button" id="buttonASSRAddadescriptionofyourcomputerplaces" value="<s:property value="getText('label.ai.accessAndServices.addDescriptionOfYourComputerPlaces')"/>" />
			</td>
		</tr>

		<tr>
			<td>
				<label for="textASSRMicrofilmPlaces"><s:property value="getText('label.ai.accessAndServices.microfilmPlaces')"/>:</label>
			</td>
			<td>
				<input type="text" id="textASSRMicrofilmPlaces" />
			</td>
			<td>
				<label for="textASSRPhotographAllowance"><s:property value="getText('label.ai.accessAndServices.photographAllowance')"/>:</label>
			</td>
			<td>
				<s:label>TODO: Microfilm photograph allowance</s:label>
				<%-- <s:select id="textASSRPhotographAllowance" list="yesnoWithoutFlashlist" /> --%>
			</td>
		</tr>

		<tr>
			<td>
				<label for="textASSRReadersTicket"><s:property value="getText('label.ai.accessAndServices.readersTicket')"/>:</label>
			</td>
			<td>
				<input type="text" id="textASSRReadersTicket" />
			</td>
			<td>
				<label for="textASSRRTLink"><s:property value="getText('label.ai.accessAndServices.link')"/>:</label>
			</td>
			<td>
				<input type="text" id="textASSRRTLink" />
			</td>
		</tr>

		<tr>
			<td colspan="2">
				<input type="button" id="buttonASSRAddReadersTicket" value="<s:property value="getText('label.ai.accessAndServices.addReadersTicket')"/>" />
			</td>
			<td>
				<label for="selectReadersTickectLanguage"><s:property value="getText('label.ai.tabs.commons.selectLanguage')"/>:</label>
			</td>
			<td>
				<s:select theme="simple" id="selectReadersTickectLanguage" list="languageList"></s:select>
			</td>
		</tr>

		<tr>
			<td>
				<label for="textASSRAdvancedOrders"><s:property value="getText('label.ai.accessAndServices.advancedOrders')" />:</label>
			</td>
			<td>
				<input type="text" id="textASSRAdvancedOrders" />
			</td>
			<td>
				<label for="textASSRAOLink"><s:property value="getText('label.ai.accessAndServices.link')" />:</label>
			</td>
			<td colspan="2">
				<input type="text" id="textASSRAOLink" />
			</td>
		</tr>

		<tr>
			<td>
				<input type="button" id="buttonASSRAddFurtherOrderInformation" value="<s:property value="getText('label.ai.accessAndServices.addFurtherOrderInformation')" />" />
			</td>
			<td>
			</td>
			<td>
				<label for="selectASSRAFOIUSelectLanguage"><s:property value="getText('label.ai.tabs.commons.selectLanguage')"/>:</label>
			</td>
			<td>
				<s:select theme="simple" id="selectASSRAFOIUSelectLanguage" list="languageList"></s:select>
			</td>
		</tr>

		<tr>
			<td>
				<label for="textASSRResearchServices"><s:property value="getText('label.ai.accessAndServices.researchServices')"/>:</label>
			</td>
			<td>
				<input type="text" id="textASSRResearchServices" />
			</td>
			<td>
				<label for="textASSRRSSelectLanguage"><s:property value="getText('label.ai.tabs.commons.selectLanguage')"/>:</label>
			</td>
			<td>
				<s:select theme="simple" id="textASSRRSSelectLanguage" list="languageList"></s:select>
			</td>
		</tr>

		<tr>
			<td colspan="2">
				<input type="button" id="buttonASAddResearchServices" value="<s:property value="getText('label.ai.accessAndServices.addResearchServices')"/>" />
			</td>
			<td colspan="2">
			</td>
		</tr>

		<tr>
			<td id="libraryLabel">
				<span><s:property value="getText('label.ai.accessAndServices.library')" /></span>
			</td>
			<td>
				<s:label>TODO: YES/No list</s:label>
				<%-- <s:select id="selectASLibrary" list="yesnoList" />--%>
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
			<td>
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
			<td>
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
			<td>
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
				<s:label>TODO: YES/No list</s:label>
				<%-- <s:select id="selectASInternetAccess" list="yesnoList" />--%>
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
			<td>
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
				<span><label for="textASTSRestaurationLab"><s:property value="getText('label.ai.accessAndServices.restaurationLab')" />:</label></span>
			</td>
			<td>
				<s:label>TODO: YES/No list</s:label>
				<%-- <s:select id="textASTSRestaurationLab" list="yesnoList" />--%>
			</td>
			<td colspan="2">
				<input type="button" id="buttonAddADescriptionOfYourRestaurationLab" value="<s:property value="getText('label.ai.accessAndServices.addDescriptionOfYourRestaurationLab')"/>"/>
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
			<td>
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
			<td>
				<label for="textASRSWebpageLinkTitle"><s:property value="getText('label.ai.tabs.commons.linkTitle')"/>:</label>
			</td>
			<td>
				<input type="text" id="textASRSLinkTitle" />
			</td>
		</tr>

		<tr>
			<td id="reproductionServiceLabel">
				<label for="textASTSReproductionService"><s:property value="getText('label.ai.accessAndServices.reproductionService')" /></label>
				<span class="required">*</span>:
			</td>
			<td>
				<s:label>TODO: Yes/No list</s:label>
				<%-- <s:select id="textASTSReproductionService" list="yesnoList" />--%>
				<s:fielderror fieldName="yesNotextASTSReproductionService"/>
			</td>
			<td colspan="2">
				<input type="text" id="buttonASAddADescriptionOfYourReproductionService"value="<s:property value="getText('label.ai.accessAndServices.addDescriptionOfYourReproductionService')"/>"/>
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
			<td>
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
			<td>
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
				<s:label>TODO: Yes/No list</s:label>
				<%-- <s:select id="selectASTSRSMicroform" list="yesnoList" />--%>
			</td>
			<td>
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
				<s:label>TODO: Yes/No list</s:label>
				<%-- <s:select id="selectASTSRSPhotographServices" list="yesnoList" />--%>
			</td>
			<td>
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
				<s:label>TODO: Yes/No list</s:label>
				<%-- <s:select id="selectASTSRSDigitalServices" list="yesnoList" />--%>
			</td>
			<td>
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
				<s:label>TODO: Yes/No list</s:label>
				<%-- <s:select id="selectASTSRSPhotocopyServices" list="yesnoList" />--%>
			</td>
			<td>
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
			<td>
				<label for="selectASReSeRefreshmentSelectLanguage"><s:property value="getText('label.ai.tabs.commons.selectLanguage')"/>:</label>
			</td>
			<td>
				<s:select theme="simple" id="selectASReSeRefreshmentSelectLanguage" list="languageList"></s:select>
			</td>
		</tr>

		<tr>
			<td>
				<label for="textASReSeExhibition"><s:property value="getText('label.ai.accessAndServices.exhibition')" />:</label>
			</td>
			<td>
				<input type="text" id="textASReSeExhibition" />
			</td>
			<td>
				<label for="selectASReSeExhibitionSelectLanguage"><s:property value="getText('label.ai.tabs.commons.selectLanguage')"/>:</label>
			</td>
			<td>
				<s:select theme="simple" id="selectASReSeExhibitionSelectLanguage" list="languageList"></s:select>
			</td>
		</tr>

		<tr>
			<td colspan="2">
				<input type="button" id="buttonASReSeAddExhibition" value="<s:property value="getText('label.ai.accessAndServices.addExhibition')"/>" />
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
			<td>
				<label for="textASReSeWebpageLinkTitle"><s:property value="getText('label.ai.tabs.commons.linkTitle')" />:</label>
			</td>
			<td>
				<input type="text" id="textASReSeWebpageLinkTitle" />
			</td>
		</tr>

		<tr>
			<td>
				<label for="textASReSeToursAndSessions" ><s:property value="getText('label.ai.accessAndServices.toursAndSessions')" /></label>
			</td>
			<td>
				<input type="text" id="textASReSeToursAndSessions" />
			</td>
			<td>
				<label for="selectASReSeToursAndSessionsSelectLanguage"><s:property value="getText('label.ai.tabs.commons.selectLanguage')"/></label>
			</td>
			<td>
				<s:select theme="simple" id="selectASReSeToursAndSessionsSelectLanguage" list="languageList"></s:select>
			</td>
		</tr>

		<tr>
			<td colspan="2">
				<input type="button" id="buttonASReSeToursAndSessions" value="<s:property value="getText('label.ai.accessAndServices.addToursAndSessions')"/>" />
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
			<td>
				<label for="textASReSeWebpageTSLinkTitle"><s:property value="getText('label.ai.tabs.commons.linkTitle')" />:</label>
			</td>
			<td>
				<input type="text" id="textASReSeWebpageTSLinkTitle" />
			</td>
		</tr>

		<tr>
			<td>
				<label for="textASReSeOtherServices" ><s:property value="getText('label.ai.accessAndServices.otherServices')" />:</label>
			</td>
			<td>
				<input type="text" id="textASReSeOtherServices" />
			</td>
			<td>
				<label for="selectASReSeOtherServicesSelectLanguage"><s:property value="getText('label.ai.tabs.commons.selectLanguage')"/>:</label>
			</td>
			<td>
				<s:select theme="simple" id="selectASReSeOtherServicesSelectLanguage" list="languageList"></s:select>
			</td>
		</tr>

		<tr>
			<td colspan="2">
				<input type="button" id="buttonASAddServices" value="<s:property value="getText('label.ai.accessAndServices.addSevices')"/>" />
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
			<td>
				<label for="textASReSeWebpageOSLinkTitle"><s:property value="getText('label.ai.tabs.commons.linkTitle')" />:</label>
			</td>
			<td>
				<input type="text" id="textASReSeWebpageOSLinkTitle" />
			</td>
		</tr>

		<tr>
			<td id="tdButtonsAccessAndServiceTab" colspan="4">
				<input type="button" id="buttonAccessAndServiceTabNext" value="<s:property value='getText("label.ai.tabs.commons.button.nextTab")' />" class="rightButton" />
				<input type="button" id="buttonAccessAndServiceTabExit" value="<s:property value='getText("label.ai.tabs.commons.button.exit")' />" class="rightButton" />
				<input type="button" id="buttonAccessAndServicelTabSave" value="<s:property value='getText("label.ai.tabs.commons.button.save")' />" class="rightButton" />
			</td>
		</tr>
	</table>
</div>