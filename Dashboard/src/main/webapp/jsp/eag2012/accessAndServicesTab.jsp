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
			<td>
				<label for="textTravelLink"><s:property value="getText('label.ai.accessAndServices.link')"/>:</label>
			</td>
			<td colspan="2">
				<input type="text" id="textTravelLink" />
			</td>
		</tr>
		<tr id="tr2TravellingDirections">
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
		<script type="text/javascript">
			$("table#accessAndServicesTable input#buttonASAddTravellingDirections").click(function(){
				var count = $("table#accessAndServicesTable tr[id^='trTravellingDirections']").length;
				var target1 = "trTravellingDirections_"+(count+1);
				var target2 = "tr2TravellingDirections_"+(count+1);
				var lastId = "table#accessAndServicesTable tr#tr2TravellingDirections";
				if(count>1){
					lastId+="_"+(count);
				}
				var tr2HTML = "<tr id=\""+target1+"\">";
				tr2HTML += $("table#accessAndServicesTable tr#trTravellingDirections").clone().html();
				tr2HTML += "</tr>\n";
				if(count==1){
					tr2HTML += "<tr id=\""+target2+"\">"+$("table#accessAndServicesTable tr#tr2TravellingDirections").clone().html();
				}else{
					tr2HTML += "<tr id=\""+target2+"\">"+$("table#accessAndServicesTable tr#tr2TravellingDirections_"+(count)).clone().html();
				}
				tr2HTML += "</tr>";
				$(lastId).after(tr2HTML);
				//update with new elements
				//put .click event to only new visible button
				$("table#accessAndServicesTable tr#"+target2+" input#buttonASAddTravellingDirections").click($._data($(lastId+" input#buttonASAddTravellingDirections")[0],"events")["click"][0].handler);
				if(count==1){
					$("table#accessAndServicesTable tr#tr2TravellingDirections input#buttonASAddTravellingDirections").remove();
				}else{
					$("table#accessAndServicesTable tr#tr2TravellingDirections_"+(count)+" input#buttonASAddTravellingDirections").remove();
				}
				//update rest of new elements
				$("table#accessAndServicesTable tr#"+target1+" label[for='textTravellingDirections']").attr("for","textTravellingDirections_"+(count+1));
				$("table#accessAndServicesTable tr#"+target1+" textarea#textTravellingDirections").attr("id","textTravellingDirections_"+(count+1));
				$("table#accessAndServicesTable tr#"+target1+" label[for='textTravelLink']").attr("for","textTravelLink_"+(count+1));
				$("table#accessAndServicesTable tr#"+target1+" input#textTravelLink").attr("id","textTravelLink_"+(count+1));
				$("table#accessAndServicesTable tr#"+target2+" label[for='selectASATDSelectLanguage']").attr("for","selectASATDSelectLanguage_"+(count+1));
				$("table#accessAndServicesTable tr#"+target2+" select#selectASATDSelectLanguage").attr("id","selectASATDSelectLanguage_"+(count+1));
			});
		</script>
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
				<script type="text/javascript">
					$("table#accessAndServicesTable input#buttonAddFutherAccessInformation").click(function(){
						var count = $("table#accessAndServicesTable tr[id^='trASAccessRestrictions']").length;
						var newId = "trASAccessRestrictions_"+(count+1);
						var trHtml = "<tr id=\""+newId+"\">"+$("table#accessAndServicesTable tr[id='trASAccessRestrictions']").clone().html()+"</tr>";
						var lastId = "table#accessAndServicesTable tr#trASAccessRestrictions";
						if(count>1){
							lastId+="_"+(count);
						}
						$(lastId).after(trHtml);
						//update last content
						$("table#accessAndServicesTable tr#"+newId+" label[for='textASAccessRestrictions']").attr("for","textASAccessRestrictions_"+(count+1));
						$("table#accessAndServicesTable tr#"+newId+" input#textASAccessRestrictions").attr("id","textASAccessRestrictions_"+(count+1));
						$("table#accessAndServicesTable tr#"+newId+" label[for='selectASARSelectLanguage']").attr("for","selectASARSelectLanguage_"+(count+1));
						$("table#accessAndServicesTable tr#"+newId+" select#selectASARSelectLanguage").attr("id","selectASARSelectLanguage_"+(count+1));
					});
				</script>
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
			<td>
				<label for="textASTOULink"><s:property value="getText('label.ai.accessAndServices.link')"/>:</label>
			</td>
			<td colspan="2">
				<input type="text" id="textASTOULink" />
			</td>
		</tr>

		<tr id="tr2ASAddFutherTermOfUse">
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
		<script type="text/javascript">
			$("table#accessAndServicesTable input#buttonASAddFutherTermOfUse").click(function(){
				var count = $("table#accessAndServicesTable tr[id^='trASAddFutherTermOfUse']").length;
				var target1 = "trASAddFutherTermOfUse_"+(count+1);
				var target2 = "tr2ASAddFutherTermOfUse_"+(count+1);
				var lastId = "table#accessAndServicesTable tr#tr2ASAddFutherTermOfUse";
				if(count>1){
					lastId+="_"+(count);
				}
				var tr2HTML = "<tr id=\""+target1+"\">";
				tr2HTML += $("table#accessAndServicesTable tr#trASAddFutherTermOfUse").clone().html();
				tr2HTML += "</tr>\n";
				if(count==1){
					tr2HTML += "<tr id=\""+target2+"\">"+$("table#accessAndServicesTable tr#tr2ASAddFutherTermOfUse").clone().html();
				}else{
					tr2HTML += "<tr id=\""+target2+"\">"+$("table#accessAndServicesTable tr#tr2ASAddFutherTermOfUse_"+(count)).clone().html();
				}
				tr2HTML += "</tr>";
				$(lastId).after(tr2HTML);
				//update with new elements
				//put .click event to only new visible button
				$("table#accessAndServicesTable tr#"+target2+" input#buttonASAddFutherTermOfUse").click($._data($(lastId+" input#buttonASAddFutherTermOfUse")[0],"events")["click"][0].handler);
				if(count==1){
					$("table#accessAndServicesTable tr#tr2ASAddFutherTermOfUse input#buttonASAddFutherTermOfUse").remove();
				}else{
					$("table#accessAndServicesTable tr#tr2ASAddFutherTermOfUse_"+(count)+" input#buttonASAddFutherTermOfUse").remove();
				}
				//update rest of new elements
				$("table#accessAndServicesTable tr#"+target1+" label[for='textASTermOfUse']").attr("for","textASTermOfUse_"+(count+1));
				$("table#accessAndServicesTable tr#"+target1+" textarea#textASTermOfUse").attr("id","textASTermOfUse_"+(count+1));
				$("table#accessAndServicesTable tr#"+target1+" label[for='textTravelLink']").attr("for","textTravelLink_"+(count+1));
				$("table#accessAndServicesTable tr#"+target1+" input#textASTOULink").attr("id","textASTOULink_"+(count+1));
				$("table#accessAndServicesTable tr#"+target2+" label[for='textASTOULink']").attr("for","textASTOULink_"+(count+1));
				$("table#accessAndServicesTable tr#"+target2+" select#selectASAFTOUSelectLanguage").attr("id","selectASAFTOUSelectLanguage_"+(count+1));
			});
		</script>
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
				<script type="text/javascript">
					$("table#accessAndServicesTable input#buttonAddAccessibilityInformation").click(function(){
						var count = $("table#accessAndServicesTable tr[id^='trAccessibilityInformation']").length;
						var newId = "trAccessibilityInformation_"+(count+1);
						var trHtml = "<tr id=\""+newId+"\">"+$("table#accessAndServicesTable tr[id='trAccessibilityInformation']").clone().html()+"</tr>";
						var lastId = "table#accessAndServicesTable tr#trAccessibilityInformation";
						if(count>1){
							lastId+="_"+(count);
						}
						$(lastId).after(trHtml);
						//update last content
						$("table#accessAndServicesTable tr#"+newId+" label[for='textASAccessibility']").attr("for","textASAccessibility_"+(count+1));
						$("table#accessAndServicesTable tr#"+newId+" input#textASAccessibility").attr("id","textASAccessibility_"+(count+1));
						$("table#accessAndServicesTable tr#"+newId+" label[for='selectASASelectLanguage']").attr("for","selectASASelectLanguage_"+(count+1));
						$("table#accessAndServicesTable tr#"+newId+" select#selectASASelectLanguage").attr("id","selectASASelectLanguage_"+(count+1));
					});
				</script>
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

		<tr id="trASSRAddadescriptionofyourcomputerplaces">
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
		<script type="text/javascript">
			$("table#accessAndServicesTable input#buttonASSRAddadescriptionofyourcomputerplaces").click(function(){
				var count = $("table#accessAndServicesTable tr[id^='trASSRAddadescriptionofyourcomputerplaces']").length;
				var newId = "trASSRAddadescriptionofyourcomputerplaces_"+(count+1);
				var trHtml = "<tr id=\""+newId+"\">"+$("table#accessAndServicesTable tr[id='trASSRAddadescriptionofyourcomputerplaces']").clone().html()+"</tr>";
				var lastId = "table#accessAndServicesTable tr#trASSRAddadescriptionofyourcomputerplaces";
				if(count>1){
					lastId+="_"+(count);
				}
				$(lastId).after(trHtml);
				//update last content
				$("table#accessAndServicesTable tr#"+newId+" label[for='textASSRComputerPlaces']").attr("for","textASAccessibility_"+(count+1));
				$("table#accessAndServicesTable tr#"+newId+" input#textASSRComputerPlaces").attr("id","textASAccessibility_"+(count+1));
				$("table#accessAndServicesTable tr#"+newId+" input#buttonASSRAddadescriptionofyourcomputerplaces").parent().remove();
			});
		</script>
		<tr>
			<td>
				<label for="textASSRMicrofilmPlaces"><s:property value="getText('label.ai.accessAndServices.microfilmPlaces')"/>:</label>
			</td>
			<td>
				<input type="text" id="textASSRMicrofilmPlaces" />
			</td>
			<td>
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
				<script type="text/javascript">
					$("table#accessAndServicesTable input#buttonASSRAddReadersTicket").click(function(){
						var count = $("table#accessAndServicesTable tr[id^='trASSRReadersTicket']").length;
						var newId = "trASSRReadersTicket_"+(count+1);
						var trHtml = "<tr id=\""+newId+"\">"+$("table#accessAndServicesTable tr[id='trASSRReadersTicket']").clone().html()+"</tr>";
						var lastId = "table#accessAndServicesTable tr#trASSRReadersTicket";
						if(count>1){
							lastId+="_"+(count);
						}
						$(lastId).after(trHtml);
						//update last content
						$("table#accessAndServicesTable tr#"+newId+" label[for='textASSRReadersTicket']").attr("for","textASSRReadersTicket_"+(count+1));
						$("table#accessAndServicesTable tr#"+newId+" input#textASSRReadersTicket").attr("id","textASSRReadersTicket_"+(count+1));
						$("table#accessAndServicesTable tr#"+newId+" label[for='textASSRRTLink']").attr("for","textASSRRTLink_"+(count+1));
						$("table#accessAndServicesTable tr#"+newId+" input#textASSRRTLink").attr("id","textASSRRTLink_"+(count+1));
					});
				</script>
			</td>
			<td>
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
			<td>
				<label for="textASSRAOLink"><s:property value="getText('label.ai.accessAndServices.link')" />:</label>
			</td>
			<td colspan="2">
				<input type="text" id="textASSRAOLink" />
			</td>
		</tr>

		<tr>
			<td colspan="2">
				<input type="button" id="buttonASSRAddFurtherOrderInformation" value="<s:property value="getText('label.ai.accessAndServices.addFurtherOrderInformation')" />" />
				<script type="text/javascript">
					$("table#accessAndServicesTable input#buttonASSRAddFurtherOrderInformation").click(function(){
						var count = $("table#accessAndServicesTable tr[id^='trASSRAddFurtherOrderInformation']").length;
						var newId = "trASSRAddFurtherOrderInformation_"+(count+1);
						var trHtml = "<tr id=\""+newId+"\">"+$("table#accessAndServicesTable tr[id='trASSRAddFurtherOrderInformation']").clone().html()+"</tr>";
						var lastId = "table#accessAndServicesTable tr#trASSRAddFurtherOrderInformation";
						if(count>1){
							lastId+="_"+(count);
						}
						$(lastId).after(trHtml);
						//update last content
						$("table#accessAndServicesTable tr#"+newId+" label[for='textASSRAdvancedOrders']").attr("for","textASSRAdvancedOrders_"+(count+1));
						$("table#accessAndServicesTable tr#"+newId+" input#textASSRAdvancedOrders").attr("id","textASSRAdvancedOrders_"+(count+1));
						$("table#accessAndServicesTable tr#"+newId+" label[for='textASSRAOLink']").attr("for","textASSRAOLink_"+(count+1));
						$("table#accessAndServicesTable tr#"+newId+" input#textASSRAOLink").attr("id","textASSRAOLink_"+(count+1));
					});
				</script>
			</td>
			<td>
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
				<script type="text/javascript">
					$("table#accessAndServicesTable input#buttonASAddResearchServices").click(function(){
						var count = $("table#accessAndServicesTable tr[id^='trASSRResearchServices']").length;
						var newId = "trASSRResearchServices_"+(count+1);
						var trHtml = "<tr id=\""+newId+"\">"+$("table#accessAndServicesTable tr[id='trASSRResearchServices']").clone().html()+"</tr>";
						var lastId = "table#accessAndServicesTable tr#trASSRResearchServices";
						if(count>1){
							lastId+="_"+(count);
						}
						$(lastId).after(trHtml);
						//update last content
						$("table#accessAndServicesTable tr#"+newId+" label[for='textASSRResearchServices']").attr("for","textASSRResearchServices_"+(count+1));
						$("table#accessAndServicesTable tr#"+newId+" input#textASSRResearchServices").attr("id","textASSRResearchServices_"+(count+1));
						$("table#accessAndServicesTable tr#"+newId+" label[for='textASSRRSSelectLanguage']").attr("for","textASSRRSSelectLanguage_"+(count+1));
						$("table#accessAndServicesTable tr#"+newId+" select#textASSRRSSelectLanguage").attr("id","textASSRRSSelectLanguage_"+(count+1));
					});
				</script>
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
				<span><label for="selectASTSRestaurationLab"><s:property value="getText('label.ai.accessAndServices.restaurationLab')" />:</label></span>
			</td>
			<td>
				<s:select theme="simple" id="selectASTSRestaurationLab" list="yesNoList"></s:select>
			</td>
			<td colspan="2">
				<input type="button" id="buttonAddADescriptionOfYourRestaurationLab" value="<s:property value="getText('label.ai.accessAndServices.addDescriptionOfYourRestaurationLab')"/>"/>
				<script type="text/javascript">
					$("table#accessAndServicesTable input#buttonAddADescriptionOfYourRestaurationLab").click(function(){
						$(this).before("<input type=\"text\" id=\"textDescriptionOfYourRestaurationLab\" />");
						$(this).remove();
					});
				</script>
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
				<label for="selectASTSReproductionService"><s:property value="getText('label.ai.accessAndServices.reproductionService')" /></label>
				<span class="required">*</span>:
			</td>
			<td>
				<s:select theme="simple" id="selectASTSReproductionService" list="yesNoList"></s:select>
				<s:fielderror fieldName="yesNotextASTSReproductionService"/>
			</td>
			<td colspan="2">
				<input type="button" id="buttonASAddADescriptionOfYourReproductionService"value="<s:property value="getText('label.ai.accessAndServices.addDescriptionOfYourReproductionService')"/>"/>
				<script type="text/javascript">
					$("table#accessAndServicesTable input#buttonASAddADescriptionOfYourReproductionService").click(function(){
						$(this).before("<input type=\"text\" id=\"textASADescriptionOfYourReproductionService\" />");
						$(this).remove();
					});
				</script>
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
				<s:select theme="simple" id="selectASTSRSMicroform" list="yesNoList"></s:select>
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
				<s:select theme="simple" id="selectASTSRSPhotographServices" list="yesNoList"></s:select>
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
				<s:select theme="simple" id="selectASTSRSDigitalServices" list="yesNoList"></s:select>
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
				<s:select theme="simple" id="selectASTSRSPhotocopyServices" list="yesNoList"></s:select>
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

		<tr id="trASReSeExhibition">
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
				<script type="text/javascript">
					$("table#accessAndServicesTable input#buttonASReSeAddExhibition").click(function(){
						var count = $("table#accessAndServicesTable tr[id^='trASReSeExhibition']").length;
						var newId = "trASReSeExhibition_"+(count+1);
						var trHtml = "<tr id=\""+newId+"\">"+$("table#accessAndServicesTable tr[id='trASReSeExhibition']").clone().html()+"</tr>";
						var lastId = "table#accessAndServicesTable tr#trASReSeExhibition";
						if(count>1){
							lastId+="_"+(count);
						}
						$(lastId).after(trHtml);
						//update last content
						$("table#accessAndServicesTable tr#"+newId+" label[for='textASReSeExhibition']").attr("for","textASReSeExhibition_"+(count+1));
						$("table#accessAndServicesTable tr#"+newId+" input#textASReSeExhibition").attr("id","textASReSeExhibition_"+(count+1));
						$("table#accessAndServicesTable tr#"+newId+" label[for='selectASReSeExhibitionSelectLanguage']").attr("for","selectASReSeExhibitionSelectLanguage_"+(count+1));
						$("table#accessAndServicesTable tr#"+newId+" select#selectASReSeExhibitionSelectLanguage").attr("id","selectASReSeExhibitionSelectLanguage_"+(count+1));
					});
				</script>
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

		<tr id="trASReSeToursAndSessions">
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
				<script type="text/javascript">
					$("table#accessAndServicesTable input#buttonASReSeToursAndSessions").click(function(){
						var count = $("table#accessAndServicesTable tr[id^='trASReSeToursAndSessions']").length;
						var newId = "trASReSeToursAndSessions_"+(count+1);
						var trHtml = "<tr id=\""+newId+"\">"+$("table#accessAndServicesTable tr[id='trASReSeToursAndSessions']").clone().html()+"</tr>";
						var lastId = "table#accessAndServicesTable tr#trASReSeToursAndSessions";
						if(count>1){
							lastId+="_"+(count);
						}
						$(lastId).after(trHtml);
						//update last content
						$("table#accessAndServicesTable tr#"+newId+" label[for='textASReSeToursAndSessions']").attr("for","textASReSeToursAndSessions_"+(count+1));
						$("table#accessAndServicesTable tr#"+newId+" input#textASReSeToursAndSessions").attr("id","textASReSeToursAndSessions_"+(count+1));
						$("table#accessAndServicesTable tr#"+newId+" label[for='selectASReSeToursAndSessionsSelectLanguage']").attr("for","selectASReSeToursAndSessionsSelectLanguage_"+(count+1));
						$("table#accessAndServicesTable tr#"+newId+" select#selectASReSeToursAndSessionsSelectLanguage").attr("id","selectASReSeToursAndSessionsSelectLanguage_"+(count+1));
					});
				</script>
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

		<tr id="trASReSeOtherServices">
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
				<script type="text/javascript">
					$("table#accessAndServicesTable input#buttonASAddServices").click(function(){
						var count = $("table#accessAndServicesTable tr[id^='trASReSeOtherServices']").length;
						var newId = "trASReSeOtherServices_"+(count+1);
						var trHtml = "<tr id=\""+newId+"\">"+$("table#accessAndServicesTable tr[id='trASReSeOtherServices']").clone().html()+"</tr>";
						var lastId = "table#accessAndServicesTable tr#trASReSeOtherServices";
						if(count>1){
							lastId+="_"+(count);
						}
						$(lastId).after(trHtml);
						//update last content
						$("table#accessAndServicesTable tr#"+newId+" label[for='textASReSeOtherServices']").attr("for","textASReSeOtherServices_"+(count+1));
						$("table#accessAndServicesTable tr#"+newId+" input#textASReSeOtherServices").attr("id","textASReSeOtherServices_"+(count+1));
						$("table#accessAndServicesTable tr#"+newId+" label[for='selectASReSeOtherServicesSelectLanguage']").attr("for","selectASReSeOtherServicesSelectLanguage_"+(count+1));
						$("table#accessAndServicesTable tr#"+newId+" select#selectASReSeOtherServicesSelectLanguage").attr("id","selectASReSeOtherServicesSelectLanguage_"+(count+1));
					});
				</script>
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
				<input type="button" id="buttonAccessAndServicelTabSave" value="<s:property value='getText("label.ai.tabs.commons.button.save")' />" class="rightButton" />
				<script type="text/javascript">
					//current tab
					$("table#accessAndServicesTable input#buttonAccessAndServicelTabSave").click(clickAccessAndServicesAction);
				</script>
			</td>
		</tr>
	</table>
</div>