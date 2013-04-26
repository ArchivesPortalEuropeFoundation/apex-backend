<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<div id="yourInstitutionTabContent">
	<table id="yourInstitutionTable">
		<tr>
			<td colspan="2">
				<label class="middleText" for="textYIPersonInstitutionResposibleForTheDescription"><s:property value="getText('label.ai.yourinstitution.personinstitutionresposibleforthedescription')"/>:</label>
			</td>
			<td colspan="2">
				<input type="text" id="textYIPersonInstitutionResposibleForTheDescription" class="middleText"/>
			</td>
		</tr>

		<tr>
			<td>
				<label for="textYIInstitutionCountryCode"><s:property value="getText('label.ai.tabs.commons.countryCode')"/></label>
				<span class="required">*</span>:
			</td>
			<td>
				<input type="text" id="textYIInstitutionCountryCode" />
				<s:fielderror fieldName="yourInstitutionCountryCode"/>
			</td>
			<td colspan="2">
			</td>
		</tr>

		<tr>
			<td>
				<label for="textYIIdentifierOfTheInstitution"><s:property value="getText('label.ai.tabs.commons.identifierOfTheInstitution')"/></label>
				<span class="required">*</span>:
			</td>
			<td>
				<input type="text" id="textYIIdentifierOfTheInstitution" />
				<s:fielderror fieldName="yourInstitutionIdentifierOfTheInstitution"/>
			</td>
			<td>
				<label for="textYIIdUsedInAPE"><s:property value="getText('label.ai.tabs.commons.idUsedInAPE')"/>:</label>
			</td>
			<td>
				<input type="text" id="textYIIdUsedInAPE" disabled="disabled"/>
			</td>
		</tr>

		<tr>
			<td colspan="3">
			</td>
			<td>
				<input type="button" id="buttonAddFutherIds" value="<s:property value="getText('label.ai.yourinstitution.addFutherIds')" />" />
				<script type="text/javascript">
					$("#buttonAddFutherIds").click(function(){
						$(this).parent().parent().before("<tr><td colspan=\"2\"></td><td><label for=\"otherRepositorId_"+($("input[id^='otherRepositorId']").length)+"\"> <s:property value="getText('label.ai.yourinstitution.futherId')" />:</label></td><td><input type=\"text\" id=\"otherRepositorId_"+($("input[id^='otherRepositorId']").length)+"\" /></td></tr>");
					});
				</script>
			</td>
		</tr>
		<tr>
			<td>
				<label for="textYINameOfTheInstitution"><s:property value="getText('label.ai.tabs.commons.nameOfTheInstitution')"/></label>
				<span class="required">*</span>:
			</td>
			<td>
				<input type="text" id="textYINameOfTheInstitution" />
				<s:fielderror fieldName="yourInstitutionNameOfTheInstitution"/>
			</td>
			<td>
				<label for="selectYINOTISelectLanguage"><s:property value="getText('label.ai.tabs.commons.selectLanguage')"/></label>
				<span class="required">*</span>:
			</td>
			<td>
				<s:select theme="simple" id="selectYINOTISelectLanguage" list="languageList"></s:select>
				<s:fielderror fieldName="yourInstitutionLanguageNameOfTheInstitution"/>
			</td>
		</tr>

		<tr>
			<td colspan="3">
			</td>
			<td>
				<input type="button" id="buttonAddRepositories" value="<s:property value="getText('label.ai.yourinstitution.addRepositories')" />" />
				<script type="text/javascript">
					$("#buttonAddRepositories").click(function(){
						var counter = $("table[id^='yourInstitutionTabContent_']").length;
						var clone = $("table[id^='yourInstitutionTabContent_"+counter+"']").clone();
						clone = "<table id='"+("yourInstitutionTabContent_"+(counter+1))+"'>"+clone.html()+"</table>";
						$("table[id^='yourInstitutionTabContent_"+counter+"']").after(clone);
						//reset parametters
						$("table#yourInstitutionTabContent_"+(counter+1)+" input[type='text']").each(function(){
							$(this).val(""); //clean all input_text
						});
						$("table#yourInstitutionTabContent_"+(counter+1)+" tr#YILatitudeLongitude").each(function(){
							$(this).show();
						});
						$("table#yourInstitutionTabContent_"+(counter+1)+" tr#YIPostalAddress").each(function(){
							$(this).hide();
						});
						$("table#yourInstitutionTabContent_"+(counter+1)+" input#buttonAddPostalAddressIfDifferent").each(function(){
							$(this).show();
						});
						$("table#yourInstitutionTabContent_"+(counter+1)+" input#buttonAddPostalAddressIfDifferent").click(function(){
							$(this).hide();
							$("table#yourInstitutionTabContent_"+($("table[id^='yourInstitutionTabContent_']").length)+" tr#YILatitudeLongitude").hide();
							$("table#yourInstitutionTabContent_"+($("table[id^='yourInstitutionTabContent_']").length)+" tr#YIPostalAddress").show();
						});
						$("table#yourInstitutionTabContent_"+(counter+1)+" input#buttonAddClosingDates").click(function(){
							$(this).hide();
							$("table#yourInstitutionTabContent_"+(counter+1)+" tr#fieldClosingDates").show();
						});
						$("table[id^='yourInstitutionTabContent_']").hide();
						var localId = "";
						if(counter==1){
							localId = "yourInstitutionTabContent_"+counter;
							$("#eag2012tabs_institution_tabs").append("<li><a id=\"tab_"+localId+"\" href=\"#repositories\" ><s:property value="getText('label.ai.yourinstitution.institution')" /></a></li>");
						}
						$("table#yourInstitutionTabContent_"+(counter+1)+" input#buttonFutherAccessInformation").click(function(){
							$(this).after('<input type="text" id="futherAccessInformation" />');
							$(this).hide();
						});
						$("table#yourInstitutionTabContent_"+(counter+1)+" input#buttonFutherAccessInformation").each(function(){
							$(this).show();
						});
						$("table#yourInstitutionTabContent_"+(counter+1)+" input#buttonAddFutherInformationOnExistingFacilities").each(function(){
							$(this).show();
						});
						//futherAccessInformation
						$("table#yourInstitutionTabContent_"+(counter+1)+" input#futherAccessInformation").each(function(){
							$(this).remove();
						});
						//futherInformationOnExistingFacilities
						$("table#yourInstitutionTabContent_"+(counter+1)+" input#futherInformationOnExistingFacilities").each(function(){
							$(this).remove();
						});
						$("table#yourInstitutionTabContent_"+(counter+1)+" input#buttonAddFutherInformationOnExistingFacilities").click(function(){
							$(this).after('<input type="text" id="futherInformationOnExistingFacilities" />');
							$(this).hide();
						});
						localId = "yourInstitutionTabContent_"+(counter+1);
						$("#eag2012tabs_institution_tabs").append("<li><a id=\"tab_"+localId+"\" href=\"#repositories\" ><s:property value="getText('label.ai.yourinstitution.repository')" /> "+(counter)+"</a></li>");
						//clone and put the 3 new tables
						$("div#tab-contact").append("<table id=\"contactTable_"+(counter+1)+"\">"+$("table#contactTable").clone().html()+"</table>");
						$("div#tab-accessAndServices").append("<table id=\"accessAndServicesTable_"+(counter+1)+"\">"+$("table#accessAndServicesTable").clone().html()+"</table>");
						$("div#tab-description").append("<table id=\"descriptionTable_"+(counter+1)+"\">"+$("table#descriptionTable").clone().html()+"</table>");
						//fill values with the current "your institution" values provided by user
						
						//contact tab
						var selectedIndex = document.getElementById('selectYIContinent').selectedIndex;
						var latitude = $("#textYILatitude").val();
						var longitude = $("#textYILongitude").val();
						var country = $("#textYICountry").val();
						var city = $("#textYICity").val();
						var street = $("#textYIStreet").val();
						var telephone = $("#textYITelephone").val();
						var email = $("#textYIEmailAddress").val();
						var web = $("#textYIEmailAddress").val();
						//contact table
						$("table#contactTable_"+(counter+1)+" #selectContinentOfTheInstitution option").eq(selectedIndex).prop("selected",true);
						$("table#contactTable_"+(counter+1)+" #textcontactLatitudeOfTheInstitution").attr("value",latitude);
						$("table#contactTable_"+(counter+1)+" #textContactLongitudeOfTheInstitution").attr("value",longitude);
						$("table#contactTable_"+(counter+1)+" #textContactCountryOfTheInstitution").attr("value",country);
						$("table#contactTable_"+(counter+1)+" #textContactCityOfTheInstitution").attr("value",city);
						$("table#contactTable_"+(counter+1)+" #textContactStreetOfTheInstitution").attr("value",street);
						$("table#contactTable_"+(counter+1)+" #textContactTelephoneOfTheInstitution").attr("value",telephone);
						$("table#contactTable_"+(counter+1)+" #textContactEmailOfTheInstitution").attr("value",email);
						$("table#contactTable_"+(counter+1)+" #textContactWebOfTheInstitution").attr("value",web);
						//access and services
						var opening = $("#textYIOpeningTimes").val();
						var accessPublic = document.getElementById('selectAccessibleToThePublic').selectedIndex;
						var access = $("#selectFacilitiesForDisabledPeopleAvailable").val();
						$("table#contactTable_"+(counter+1)+" #textContactWebOfTheInstitution").attr("value",opening);
						$("table#contactTable_"+(counter+1)+" #selectASFacilitiesForDisabledPeopleAvailable option").eq(accessPublic).prop("selected",true);
						$("table#contactTable_"+(counter+1)+" #textASAccessibility").attr("value",access);
						
						$("table#"+localId).show();
						$("a[id^='tab_']").click(function(){
							$("a[id^='tab_']").each(function(){
								$(this).removeClass("eag2012currenttab");
							});
							$(this).addClass("eag2012currenttab");
							var localId = $(this).attr("id");
							if(localId.indexOf("tab_")==0){
								localId = localId.substring("tab_".length);
							}
							$("table[id^='yourInstitutionTabContent_']").hide();
							$("table#"+localId).show();
							//show/hide right tabs and content
							if(localId.indexOf("_1")>-1){
								$("ul#eag2012TabsContainer li a").each(function(){
									var id = $(this).parent().attr("id");
									if(id.indexOf("tab-yourInstitution")>-1){
										$(this).removeClass("eag2012disabled");
									}else if(id.indexOf("tab-identity")>-1){
										$(this).removeClass("eag2012disabled");
									}else if(id.indexOf("tab-control")>-1){
										$(this).removeClass("eag2012disabled");
									}else if(id.indexOf("tab-relations")>-1){
										$(this).removeClass("eag2012disabled");
									}
								});
							}else{
								$("ul#eag2012TabsContainer li a").each(function(){
									var id = $(this).parent().attr("id");
									if(id.indexOf("tab-yourInstitution")>-1 || id.indexOf("tab-identity")>-1 || id.indexOf("tab-control")>-1 || id.indexOf("tab-relations")>-1){
										$(this).addClass("eag2012disabled");
										$("ul#eag2012TabsContainer a[href='#tab-contact']").trigger('click');
									}
								});
//								if($("ul#eag2012TabsContainer a .eag2012currenttab").hasClass("eag2012disabled")){
//									$("ul#eag2012TabsContainer a[href='#tab-contact']").trigger('click');
//								}
							}
						});
						$("a#tab_"+localId).trigger('click');
						$("a#tab-contact").parent().trigger('click');
						//current tab
						$("table#yourInstitutionTabContent_"+(counter+1)+" input#buttonYourInstitutionTabSave").click(clickYourInstitutionAction);
					});
				</script>
			</td>
		</tr>
		</table>
		<table id="yourInstitutionTabContent_1">
		<tr>
			<td>
				<label for="textYIParallelNameOfTheInstitution"><s:property value="getText('label.ai.tabs.commons.parallelNameOfTheInstitution')"/>:</label>
			</td>
			<td>
				<input type="text" id="textYIParallelNameOfTheInstitution" />
			</td>
			<td>
				<label for="selectYIPNOTISelectLanguage"><s:property value="getText('label.ai.tabs.commons.selectLanguage')"/>:</label>
			</td>
			<td>
				<s:select theme="simple" id="selectYIPNOTISelectLanguage" list="languageList"></s:select>
			</td>
		</tr>
	
		<tr>
			<td id="visitorAdressLabel" colspan="4">
				<s:property value="getText('label.ai.tabs.commons.visitorAddress')" />
			</td>
		</tr>

		<tr>
			<td>
				<label for="textYIStreet"><s:property value="getText('label.ai.tabs.commons.street')"/></label>
				<span class="required">*</span>:
			</td>
			<td>
				<input type="text" id="textYIStreet" />
				<s:fielderror fieldName="yourInstitutionStreetOfTheInstitution"/>
			</td>
			<td>
				<label for="selectYISSelectLanguage"><s:property value="getText('label.ai.tabs.commons.selectLanguage')"/>:</label>
			</td>
			<td>
				<s:select theme="simple" id="selectYISSelectLanguage" list="languageList"></s:select>
			</td>
		</tr>

		<tr>
			<td>
				<label for="textYICity"><s:property value="getText('label.ai.tabs.commons.cityTownWithPostalcode')"/></label>
				<span class="required">*</span>:
			</td>
			<td>
				<input type="text" id="textYICity" />
				<s:fielderror fieldName="yourInstitutionCityOfTheInstitution"/>
			</td>
			<td>
				<label for="selectYICSelectLanguage"><s:property value="getText('label.ai.tabs.commons.selectLanguage')"/>:</label>
			</td>
			<td>
				<s:select theme="simple" id="selectYICSelectLanguage" list="languageList"></s:select>
			</td>
		</tr>

		<tr>
			<td>
				<label for="textYICountry"><s:property value="getText('label.ai.tabs.commons.country')"/></label>
				<span class="required">*</span>:
			</td>
			<td>
				<input type="text" id="textYICountry" />
				<s:fielderror fieldName="yourInstitutionCountryOfTheInstitution"/>
			</td>
			<td>
				<label for="selectYICoSelectLanguage"><s:property value="getText('label.ai.tabs.commons.selectLanguage')"/>:</label>
			</td>
			<td>
				<s:select theme="simple" id="selectYICoSelectLanguage" list="languageList"></s:select>
			</td>
		</tr>

		<tr>
			<td id="coordinatesLabel" colspan="4">
				<s:property value="getText('label.ai.tabs.commons.coordinates')" />
			</td>
		</tr>

		<tr id="YILatitudeLongitude">
			<td>
				<label for="textYILatitude"><s:property value="getText('label.ai.tabs.commons.latitude')"/></label>
			</td>
			<td>
				<input type="text" id="textYILatitude" />
			</td>
			<td>
				<label for="textYILongitude"><s:property value="getText('label.ai.tabs.commons.longitude')"/></label>
			</td>
			<td>
				<input type="text" id="textYILongitude" />
			</td>
		</tr>
		<tr id="YIPostalAddress" style="display:none;">
			<td>
				<label for="textYIPostalAddress"><s:property value="getText('label.ai.yourinstitution.postaladdress')"/></label>
			</td>
			<td>
				<input type="text" id="textYIPostalAddress" />
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<input type="button" id="buttonAddPostalAddressIfDifferent" value="<s:property value="getText('label.ai.yourinstitution.addPostalAddressIfDifferent')"/>" />
			</td>
			<td colspan="2">
			</td>
		</tr>

		<tr>
			<td>
				<label for="selectYIContinent" ><s:property value="getText('label.ai.tabs.commons.continent')"/>:</label>
			</td>
			<td>
				<s:select theme="simple" id="selectYIContinent" list="languageList"></s:select>
			</td>
		</tr>

		<tr>
			<td>
				<label for="textYITelephone" ><s:property value="getText('label.ai.tabs.commons.telephone')"/>:</label>
			</td>
			<td>
				<input type="text" id="textYITelephone" />
			</td>
		</tr>

		<tr>
			<td>
				<label for="textYIEmailAddress" ><s:property value="getText('label.ai.tabs.commons.emailAddress')"/>:</label>
			</td>
			<td>
				<input type="text" id="textYIEmailAddress" />
			</td>
			<td>
				<label for="textYIEmailLinkTitle" ><s:property value="getText('label.ai.tabs.commons.linkTitle')"/>:</label>
			</td>
			<td>
				<input type="text" id="textYIEmailLinkTitle" />
			</td>
		</tr>

		<tr>
			<td>
				<label for="textYIWebpage" ><s:property value="getText('label.ai.tabs.commons.webpage')"/></label>
				<span class="required">*</span>:
			</td>
			<td>
				<input type="text" id="textYIWebpage" />
				<s:fielderror fieldName="yourInstitutionWebPageOfTheInstitution"/>
			</td>
			<td>
				<label for="textYIWebpageLinkTitle" ><s:property value="getText('label.ai.tabs.commons.linkTitle')"/>:</label>
			</td>
			<td>
				<input type="text" id="textYIWebpageLinkTitle" />
			</td>
		</tr>

		<tr>
			<td>
				<label for="textYIOpeningTimes" ><s:property value="getText('label.ai.yourinstitution.openingTimes')"/></label>
				<span class="required">*</span>:
			</td>
			<td>
				<input type="text" id="textYIOpeningTimes" />
				<s:fielderror fieldName="yourInstitutionOpeningimes"/>
			</td>
			<td colspan="2">
			</td>
		</tr>

		<tr>
			<td>
				<input type="button" id="buttonAddClosingDates" value="<s:property value="getText('label.ai.tabs.commons.closingDates')"/>" />
			</td>
			<td colspan="3">
			</td>
		</tr>
		<tr id="fieldClosingDates" style="display:none;">
			<td><label for="yourInstitutionClosingDates"><s:property value="getText('label.ai.yourinstitution.closingDates')"/></label></td>
			<td><input type="text" id="yourInstitutionClosingDates" /></td>
		</tr>
		<tr>
			<td>
				<label for="selectAccessibleToThePublic" ><s:property value="getText('label.ai.yourinstitution.accessibleToThePublic')"/></label>
				<span class="required">*</span>:
			</td>
			<td>
				<s:select theme="simple" id="selectAccessibleToThePublic" list="yesNoList"></s:select>
				<s:fielderror fieldName="yourInstitutionAccesibleToPublic"/>
			</td>
			<td colspan="2">
				<input type="button" id="buttonFutherAccessInformation" value="<s:property value="getText('label.ai.yourinstitution.addFurtherAccessInformation')"/>" />
			</td>
		</tr>

		<tr>
			<td>
				<label for="selectFacilitiesForDisabledPeopleAvailable" ><s:property value="getText('label.ai.yourinstitution.facilitiesForFisabledPeopleAvailable')"/></label>
				<span class="required">*</span>:
			</td>
			<td>
				<s:select theme="simple" id="selectFacilitiesForDisabledPeopleAvailable" list="yesNoList"></s:select>
				<s:fielderror fieldName="yourInstitutionFacilitiesForDisabledPeople"/>
			</td>
			<td colspan="2">
				<input type="button" id="buttonAddFutherInformationOnExistingFacilities" value="<s:property value="getText('label.ai.yourinstitution.addFutherInformationOnExistingFacilities')"/>" />
			</td>
		</tr>

		<tr>
			<td colspan="2">
				<label for="textReferencetoyourinstitutionsholdingsguide" ><s:property value="getText('label.ai.yourinstitution.referenceToYourInstitutionsHoldingsGuide')"/>:</label>
			</td>
			<td colspan="2">
				<input type="text" id="textReferencetoyourinstitutionsholdingsguide" />
			</td>
		</tr>

		<tr>
			<td>
				<label for="textYIHoldingsGuideLinkTitle" ><s:property value="getText('label.ai.tabs.commons.linkTitle')"/>:</label>
			</td>
			<td>
				<input type="text" id="textYIHoldingsGuideLinkTitle" />
			</td>
			<td colspan="2">
			</td>
		</tr>

		<tr>
			<td id="tdButtonsYourInstitutionTab" colspan="4">
				<input type="button" id="buttonYourInstitutionTabNext" value="<s:property value='getText("label.ai.tabs.commons.button.nextTab")' />" class="rightButton" />
				<input type="button" id="buttonYourInstitutionTabSave" value="<s:property value='getText("label.ai.tabs.commons.button.save")' />" class="rightButton" />
			</td>
		</tr>
	</table>
</div>