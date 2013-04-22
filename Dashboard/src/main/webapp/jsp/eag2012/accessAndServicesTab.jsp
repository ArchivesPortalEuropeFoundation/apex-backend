<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<div id="accessAndServicesTabContent">
	<table id="accessAndServicesTable">
		<tr>
			<td>
				<label for="textOpeningTimes"><s:property value="getText('ai.label.accessandservices.openingtimes')" /></label>
				<span class="required">*</span>:
			</td>
			<td>
				<input type="text" id="textOpeningTimes" />
				<s:fielderror fieldName="textOpeningTimes"/>
			</td>
			<td>
				<label for="textClosingDates"><s:property value="getText('ai.label.accessandservices.closingDates')" />:</label>
			</td>
			<td>
				<input type="text" id="textClosingDates" />
			</td>
		</tr>

		<tr>
			<td>
				<label for="textTravellingDirections"><s:property value="getText('ai.label.accessandservices.travellingdirections')" />:</label>
			</td>
			<td rowspan="2">
				<textarea id="textTravellingDirections"></textarea>
			</td>
			<td>
				<label for="textTravelLink"><s:property value="getText('ai.label.accessandservices.link')"/>:</label>
			</td>
			<td colspan="2">
				<input type="text" id="textTravelLink" />
			</td>
		</tr>

		<tr>
			<td>
				<input type="button" id="buttonASAddTravellingDirections" value="<s:property value="getText('ai.label.accessandservices.addTravellingDirections')"/>" />
			</td>
			<td>
				<label for="selectASATDSelectLanguage"><s:property value="getText('ai.label.accessandservices.selectlanguage')"/>:</label>
			</td>
			<td>
				<s:label>TODO: Language list</s:label>
				<%-- <s:select id="selectASATDSelectLanguage" list="languageList" />--%>
			</td>
		</tr>

		<tr>
			<td>
				<label for="selectASAccesibleToThePublic"><s:property value="getText('ai.label.accessandservices.accesibletothepublic')"/></label>
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
				<label for="textASAccessRestrictions"><s:property value="getText('ai.label.accessandservices.accessrestrictions')" />:</label>
			</td>
			<td>
				<input type="text" id="textASAccessRestrictions" />
			</td>
			<td>
				<label for="selectASARSelectLanguage"><s:property value="getText('ai.label.accessandservices.selectlanguage')"/>:</label>
			</td>
			<td>
				<s:label>TODO: Language list</s:label>
				<%-- <s:select id="selectASARSelectLanguage" list="laguageList" />--%>
			</td>
		</tr>

		<tr>
			<td colspan="4">
				<input type="button" id="buttonAddFutherAccessInformation" value="<s:property value="getText('ai.label.accessandservices.addfutheraccessinformation')"/>" />
			</td>
		</tr>

		<tr>
			<td>
				<label for="textASTermOfUse"><s:property value="getText('ai.label.accessandservices.termofuse')" />:</label>
			</td>
			<td rowspan="2">
				<textarea id="textASTermOfUse"></textarea>
			</td>
			<td>
				<label for="textASTOULink"><s:property value="getText('ai.label.accessandservices.link')"/>:</label>
			</td>
			<td colspan="2">
				<input type="text" id="textASTOULink" />
			</td>
		</tr>

		<tr>
			<td>
				<input type="button" id="buttonASAddFutherTermOfUse" value="<s:property value="getText('ai.label.accessandservices.addFurtherTermsOfUse')"/>" />
			</td>
			<td>
				<label for="selectASAFTOUSelectLanguage"><s:property value="getText('ai.label.accessandservices.selectlanguage')"/>:</label>
			</td>
			<td>
				<s:label>TODO: Language list</s:label>
				<%-- <s:select id="selectASAFTOUSelectLanguage" list="languageList" />--%>
			</td>
		</tr>

		<tr>
			<td>
				<label for="selectASFacilitiesForDisabledPeopleAvailable"><s:property value="getText('ai.label.accessandservices.accesibletothepublic')"/></label>
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
				<label for="textASAccessibility"><s:property value="getText('ai.label.accessandservices.accesibility')" />:</label>
			</td>
			<td>
				<input type="text" id="textASAccessibility" />
			</td>
			<td>
				<label for="selectASASelectLanguage"><s:property value="getText('ai.label.accessandservices.selectlanguage')"/>:</label>
			</td>
			<td>
				<s:label>TODO: Language list</s:label>
				<%-- <s:select id="selectASASelectLanguage" list="laguageList" />--%>
			</td>
		</tr>

		<tr>
			<td colspan="4">
				<input type="button" id="buttonAddAccessibilityInformation" value="<s:property value="getText('ai.label.accessandservices.addaccessibilityinformation')"/>" />
			</td>
		</tr>

		<tr>
			<td id="searchroomLabel" colspan="4">
				<span><s:property value="getText('ai.label.accessandservices.searchroom')" /></span>
			</td>
		</tr>

		<tr>
			<td>
				<label for="textASSRTelephone"><s:property value="getText('ai.label.accessandservices.telephone')"/>:</label>
			</td>
			<td>
				<input type="text" id="textASSRTelephone" />
			</td>
			<td colspan="2">
			</td>
		</tr>

		<tr>
			<td>
				<label for="textASSREmailAddress"><s:property value="getText('ai.label.accessandservices.emailaddress')"/>:</label>
			</td>
			<td>
				<input type="text" id="textASSREmailAddress" />
			</td>
			<td>
				<label for="textASSREmailLinkTitle"><s:property value="getText('ai.label.accessandservices.linktitle')"/>:</label>
			</td>
			<td>
				<input type="text" id="textASSREmailLinkTitle" />
			</td>
		</tr>

		<tr>
			<td>
				<label for="textASSRWebpage"><s:property value="getText('ai.label.accessandservices.webpage')"/>:</label>
			</td>
			<td>
				<input type="text" id="textASSRWebpage" />
			</td>
			<td>
				<label for="textASSRWebpageLinkTitle"><s:property value="getText('ai.label.accessandservices.linktitle')"/>:</label>
			</td>
			<td>
				<input type="text" id="textASSRWebpageLinkTitle" />
			</td>
		</tr>

		<tr>
			<td>
				<label for="textASSRWorkPlaces"><s:property value="getText('ai.label.accessandservices.workplaces')"/></label>
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
				<label for="textASSRComputerPlaces"><s:property value="getText('ai.label.accessandservices.computerplaces')"/>:</label>
			</td>
			<td>
				<input type="text" id="textASSRComputerPlaces" />
			</td>
			<td colspan="2">
				<input type="button" id="buttonASSRAddadescriptionofyourcomputerplaces" value="<s:property value="getText('ai.label.accessandservices.addadescriptionofyourcomputerplaces')"/>" />
			</td>
		</tr>

		<tr>
			<td>
				<label for="textASSRMicrofilmPlaces"><s:property value="getText('ai.label.accessandservices.microfilmplaces')"/>:</label>
			</td>
			<td>
				<input type="text" id="textASSRMicrofilmPlaces" />
			</td>
			<td>
				<label for="textASSRPhotographAllowance"><s:property value="getText('ai.label.accessandservices.photographallowance')"/></label>
			</td>
			<td>
				<s:label>TODO: Microfilm photograph allowance</s:label>
				<%-- <s:select id="textASSRPhotographAllowance" list="yesnoWithoutFlashlist" /> --%>
			</td>
		</tr>

		<tr>
			<td>
				<label for="textASSRReadersTicket"><s:property value="getText('ai.label.accessandservices.readersticket')"/>:</label>
			</td>
			<td>
				<input type="text" id="textASSRReadersTicket" />
			</td>
			<td>
				<label for="textASSRRTLink"><s:property value="getText('ai.label.accessandservices.link')"/>:</label>
			</td>
			<td>
				<input type="text" id="textASSRRTLink" />
			</td>
		</tr>

	<%-- TODO: rest of tab --%>

	</table>
</div>