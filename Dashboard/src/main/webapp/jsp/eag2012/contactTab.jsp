<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<div id="contactTabContent">
	<table id="contactTable">
		<tr>
			<td id="visitorAdressLabel" colspan="4">
				<s:property value="getText('label.ai.tabs.commons.visitorAddress')" />
			</td>
		</tr>

		<tr>
			<td id="tdStreetOfTheInstitution">
				<s:property value="getText('label.ai.tabs.commons.street')" />
				<span class="required">*</span>:
			</td>
			<td>
				<input type="text" id="textContactStreetOfTheInstitution" value="<s:property value="#streetOfTheInstitution" />" />
				<s:fielderror fieldName="textContactStreetOfTheInstitution"/>
			</td>
			<td id="tdLanguageStreetOfTheInstitution">
				<label for="selectLanguageStreetOfTheInstitution" ><s:property value="getText('label.ai.tabs.commons.selectLanguage')" />:</label>
			</td>
			<td>
				<s:select theme="simple" id="selectLanguageStreetOfTheInstitution" list="languageList"></s:select>
			</td>
		</tr>

		<tr>
			<td id="tdCityOfTheInstitution">
				<s:property value="getText('label.ai.tabs.commons.cityTownWithPostalcode')" />
				<span class="required">*</span>:
			</td>
			<td>
				<input type="text" id="textContactCityOfTheInstitution" value="<s:property value="#cityOfTheInstitution" />" />
				<s:fielderror fieldName="textContactCityOfTheInstitution"/>
			</td>
			<td id="tdLanguageCityOfTheInstitution">
				<label for="selectLanguageCityOfTheInstitution"><s:property value="getText('label.ai.tabs.commons.selectLanguage')" />:</label>
			</td>
			<td>
				<s:select theme="simple" id="selectLanguageCityOfTheInstitution" list="languageList"></s:select>
			</td>
		</tr>

		<tr>
			<td id="tdDistrictOfTheInstitution">
				<s:property value="getText('label.ai.contact.districtOfTheInstitution')" />:
			</td>
			<td>
				<input type="text" id="textContactDistrictOfTheInstitution" value="<s:property value="#districtOfTheInstitution" />" />
			</td>
			<td id="tdLanguageDistrictOfTheInstitution">
				<label for="selectLanguageDistrictOfTheInstitution"><s:property value="getText('label.ai.tabs.commons.selectLanguage')" />:</label>
			</td>
			<td>
				<s:select theme="simple" id="selectLanguageDistrictOfTheInstitution" list="languageList"></s:select>
			</td>
		</tr>

		<tr>
			<td id="tdCountyOfTheInstitution">
				<s:property value="getText('label.ai.contact.countyOfTheInstitution')" />:
			</td>
			<td>
				<input type="text" id="textContactCountyOfTheInstitution" value="<s:property value="#countyOfTheInstitution" />" />
			</td>
			<td id="tdLanguageCountyOfTheInstitution">
				<label for="selectLanguageCountyOfTheInstitution"><s:property value="getText('label.ai.tabs.commons.selectLanguage')" />:</label>
			</td>
			<td>
				<s:select theme="simple" id="selectLanguageCountyOfTheInstitution" list="languageList"></s:select>
			</td>
		</tr>

		<tr>
			<td id="tdRegionOfTheInstitution">
				<s:property value="getText('label.ai.contact.regionOfTheInstitution')" />:
			</td>
			<td>
				<input type="text" id="textContactRegionOfTheInstitution" value="<s:property value="#regionOfTheInstitution" />" />
			</td>
			<td id="tdLanguageRegionOfTheInstitution">
				<label for="selectLanguageRegionOfTheInstitution"><s:property value="getText('label.ai.tabs.commons.selectLanguage')" />:</label>
			</td>
			<td>
				<s:select theme="simple" id="selectLanguageRegionOfTheInstitution" list="languageList"></s:select>
			</td>
		</tr>

		<tr>
			<td id="tdCountryOfTheInstitution">
				<s:property value="getText('label.ai.tabs.commons.country')" />
				<span class="required">*</span>:
			</td>
			<td>
				<input type="text" id="textContactCountryOfTheInstitution" value="<s:property value="#countryOfTheInstitution" />" />
				<s:fielderror fieldName="textContactCountryOfTheInstitution"/>
			</td>
			<td id="tdLanguageCountryOfTheInstitution">
				<label for="selectLanguageCountryOfTheInstitution"><s:property value="getText('label.ai.tabs.commons.selectLanguage')" />:</label>
			</td>
			<td>
				<s:select theme="simple" id="selectLanguageCountryOfTheInstitution" list="languageList"></s:select>
			</td>
		</tr>

		<tr>
			<td id="coordinatesLabel" colspan="4">
				<s:property value="getText('label.ai.tabs.commons.coordinates')" />
			</td>
		</tr>

		<tr>
			<td>
				<s:property value="getText('label.ai.tabs.commons.latitude')" />
			</td>
			<td>
				<input type="text" id="textContactLatitudeOfTheInstitution" value="<s:property value="#latitudeOfTheInstitution" />" />
			</td>
			<td>
				<s:property value="getText('label.ai.tabs.commons.longitude')" />
			</td>
			<td>
				<input type="text" id="textContactLongitudeOfTheInstitution" value="<s:property value="#longitudeOfTheInstitution" />" />
			</td>
		</tr>

		<tr>
			<td id="postalAddressLabel" colspan="4">
				<s:property value="getText('label.ai.contact.postalAddress')" />
			</td>
		</tr>

		<tr>
			<td id="tdPostalStreetOfTheInstitution">
				<s:property value="getText('label.ai.tabs.commons.street')" />:
			</td>
			<td>
				<input type="text" id="textContactPostalStreetOfTheInstitution" value="<s:property value="#postalStreetOfTheInstitution" />" />
			</td>
			<td id="tdLanguagePostalStreetOfTheInstitution">
				<label for="selectLanguagePostalStreetOfTheInstitution"><s:property value="getText('label.ai.tabs.commons.selectLanguage')" />:</label>
			</td>
			<td>
				<s:select theme="simple" id="selectLanguagePostalStreetOfTheInstitution" list="languageList"></s:select>
			</td>
		</tr>

		<tr>
			<td id="tdPostalCityOfTheInstitution">
				<s:property value="getText('label.ai.tabs.commons.cityTownWithPostalcode')" />:
			</td>
			<td>
				<input type="text" id="textContactPostalCityOfTheInstitution" value="<s:property value="#postalCityOfTheInstitution" />" />
			</td>
			<td id="tdLanguagePostalCityOfTheInstitution">
				<label for="selectLanguagePostalCityOfTheInstitution"><s:property value="getText('label.ai.tabs.commons.selectLanguage')" />:</label>
			</td>
			<td>
				<s:select theme="simple" id="selectLanguagePostalCityOfTheInstitution" list="languageList"></s:select>
			</td>
		</tr>

		<tr>
			<td id="tdContinentOfTheInstitution">
				<s:property value="getText('label.ai.tabs.commons.continent')" />
				<span class="required">*</span>:
			</td>
			<td>
				<s:select theme="simple" id="selectContinentOfTheInstitution" list="continentOfTheInstitutionList" ></s:select>
				<s:fielderror fieldName="continentOfTheInstitution" />
			</td>
			<td colspan="2" ></td>
		</tr>

		<tr id="trTelephoneOfTheInstitution">
			<td id="tdTelephoneOfTheInstitution">
				<label for="textContactTelephoneOfTheInstitution"><s:property value="getText('label.ai.tabs.commons.telephone')" /></label>:
			</td>
			<td>
				<input type="text" id="textContactTelephoneOfTheInstitution" value="<s:property value="#telephoneOfTheInstitution" />" />
			</td>
			<td id="tdAddFurtherTelephoneOfTheInstitution" colspan="2">
				<input id="buttonAddFurtherTelephoneOfTheInstitution" type="button" value="<s:property value='getText("label.ai.contact.addFurtherTelephoneNumbers")' />" class="longButton" />
			</td>
		</tr>
		<script type="text/javascript">
			$("table#contactTable input#buttonAddFurtherTelephoneOfTheInstitution").click(function(){
				var count = $("table#contactTable tr[id^='trTelephoneOfTheInstitution']").length;
				var newId = "trTelephoneOfTheInstitution_"+(count+1);
				var trHtml = "<tr id=\""+newId+"\">"+$("table#contactTable tr[id^='trTelephoneOfTheInstitution']").clone().html()+"</tr>";
				var lastId = "table#contactTable tr#trTelephoneOfTheInstitution";
				if(count>1){
					lastId+="_"+(count);
				}
				$(lastId).after(trHtml);
				//delete cloned button
				$("table#contactTable tr#"+newId+" td#tdAddFurtherTelephoneOfTheInstitution").remove();
				//update last content
				$("table#contactTable tr#"+newId+" td#tdTelephoneOfTheInstitution").attr("id","tdTelephoneOfTheInstitution_"+(count+1));
				$("table#contactTable tr#"+newId+" label[for='textContactTelephoneOfTheInstitution']").attr("for","textContactTelephoneOfTheInstitution_"+(count+1));
				$("table#contactTable tr#"+newId+" input#textContactTelephoneOfTheInstitution").attr("id","textContactTelephoneOfTheInstitution_"+(count+1));
			});
		</script>
		<tr id="trFaxOfTheInstitution">
			<td id="tdFaxOfTheInstitution">
				<label for="textContactFaxOfTheInstitution"><s:property value="getText('label.ai.contact.faxOfTheInstitution')" /></label>:
			</td>
			<td>
				<input type="text" id="textContactFaxOfTheInstitution" value="<s:property value="#faxOfTheInstitution" />" />
			</td>
			<td id="tdAddFurtherFaxOfTheInstitution" colspan="2">
				<input id="buttonAddFurtherFaxOfTheInstitution" type="button" value="<s:property value='getText("label.ai.contact.addFurtherFaxOfTheInstitution")' />" class="longButton" />
			</td>
		</tr>
		<script type="text/javascript">
			$("table#contactTable input#buttonAddFurtherFaxOfTheInstitution").click(function(){
				var count = $("table#contactTable tr[id^='trFaxOfTheInstitution']").length;
				var newId = "trFaxOfTheInstitution_"+(count+1);
				var trHtml = "<tr id=\""+newId+"\">"+$("table#contactTable tr[id^='trFaxOfTheInstitution']").clone().html()+"</tr>";
				var lastId = "table#contactTable tr#trFaxOfTheInstitution";
				if(count>1){
					lastId+="_"+(count);
				}
				$(lastId).after(trHtml);
				//delete cloned button
				$("table#contactTable tr#"+newId+" td#tdAddFurtherFaxOfTheInstitution").remove();
				//update last content
				$("table#contactTable tr#"+newId+" td#tdFaxOfTheInstitution").attr("id","tdFaxOfTheInstitution_"+(count+1));
				$("table#contactTable tr#"+newId+" label[for='textContactFaxOfTheInstitution']").attr("for","textContactFaxOfTheInstitution_"+(count+1));
				$("table#contactTable tr#"+newId+" input#textContactFaxOfTheInstitution").attr("id","textContactFaxOfTheInstitution_"+(count+1));
			});
		</script>
		<tr id="trEmailOfTheInstitution">
			<td>
				<label for="textContactEmailOfTheInstitution"><s:property value="getText('label.ai.tabs.commons.emailAddress')" /></label>:
			</td>
			<td>
				<input type="text" id="textContactEmailOfTheInstitution" value="<s:property value="#emailOfTheInstitution" />" />
			</td>
			<td>
				<label for="textContactLinkTitleForEmailOfTheInstitution"><s:property value="getText('label.ai.tabs.commons.linkTitle')" /></label>:
			</td>
			<td>
				<input type="text" id="textContactLinkTitleForEmailOfTheInstitution" value="<s:property value="#linkTitleForEmailOfTheInstitution" />" />
			</td>
		</tr>

		<tr>
			<td id="tdAddFurtherEmailsOfTheInstitution" colspan="2">
				<input id="buttonAddFurtherEmailsOfTheInstitution" type="button" value="<s:property value='getText("label.ai.contact.addFurtherEmailsAddresses")' />" class="longButton" />
			</td>
			<script type="text/javascript">
				$("table#contactTable input#buttonAddFurtherEmailsOfTheInstitution").click(function(){
					var count = $("table#contactTable tr[id^='trEmailOfTheInstitution']").length;
					var newId = "trEmailOfTheInstitution_"+(count+1);
					var trHtml = "<tr id=\""+newId+"\">"+$("table#contactTable tr[id^='trEmailOfTheInstitution']").clone().html()+"</tr>";
					var lastId = "table#contactTable tr#trEmailOfTheInstitution";
					if(count>1){
						lastId+="_"+(count);
					}
					$(lastId).after(trHtml);
					//update last content
					$("table#contactTable tr#"+newId+" tr#trEmailOfTheInstitution").attr("id","trEmailOfTheInstitution_"+(count+1));
					$("table#contactTable tr#"+newId+" label[for='textContactEmailOfTheInstitution']").attr("for","textContactEmailOfTheInstitution_"+(count+1));
					$("table#contactTable tr#"+newId+" input#textContactEmailOfTheInstitution").attr("id","textContactEmailOfTheInstitution_"+(count+1));
					$("table#contactTable tr#"+newId+" label[for='textContactLinkTitleForEmailOfTheInstitution']").attr("for","textContactLinkTitleForEmailOfTheInstitution_"+(count+1));
					$("table#contactTable tr#"+newId+" input#textContactLinkTitleForEmailOfTheInstitution").attr("id","textContactLinkTitleForEmailOfTheInstitution_"+(count+1));
				});
			</script>
		</tr>

		<tr id="trWebOfTheInstitution">
			<td>
				<label for="textContactWebOfTheInstitution"><s:property value="getText('label.ai.tabs.commons.webpage')" /></label>
				<span class="required">*</span>:
			</td>
			<td>
				<input type="text" id="textContactWebOfTheInstitution" value="<s:property value="#webOfTheInstitution" />" />
				<s:fielderror fieldName="webOfTheInstitution"/>
			</td>
			<td>
				<label for="textContactLinkTitleForWebOfTheInstitution"><s:property value="getText('label.ai.tabs.commons.linkTitle')" /></label>:
			</td>
			<td>
				<input type="text" id="textContactLinkTitleForWebOfTheInstitution" value="<s:property value="#linkTitleForWebOfTheInstitution" />" />
			</td>
		</tr>

		<tr>
			<td id="tdAddFurtherWebsOfTheInstitution" colspan="2">
				<input id="buttonAddFurtherWebsOfTheInstitution" type="button" value="<s:property value='getText("label.ai.contact.addFurtherWebpages")' />" class="longButton" />
			</td>
			<script type="text/javascript">
				$("table#contactTable input#buttonAddFurtherWebsOfTheInstitution").click(function(){
					var count = $("table#contactTable tr[id^='trWebOfTheInstitution']").length;
					var newId = "trWebOfTheInstitution_"+(count+1);
					var trHtml = "<tr id=\""+newId+"\">"+$("table#contactTable tr[id^='trWebOfTheInstitution']").clone().html()+"</tr>";
					var lastId = "table#contactTable tr#trWebOfTheInstitution";
					if(count>1){
						lastId+="_"+(count);
					}
					$(lastId).after(trHtml);
					//update last content
					$("table#contactTable tr#"+newId+" tr#trWebOfTheInstitution").attr("id","trWebOfTheInstitution_"+(count+1));
					$("table#contactTable tr#"+newId+" label[for='textContactWebOfTheInstitution']").attr("for","textContactWebOfTheInstitution_"+(count+1));
					$("table#contactTable tr#"+newId+" input#textContactWebOfTheInstitution").attr("id","textContactWebOfTheInstitution_"+(count+1));
					$("table#contactTable tr#"+newId+" label[for='textContactLinkTitleForWebOfTheInstitution']").attr("for","textContactLinkTitleForWebOfTheInstitution_"+(count+1));
					$("table#contactTable tr#"+newId+" input#textContactLinkTitleForWebOfTheInstitution").attr("id","textContactLinkTitleForWebOfTheInstitution_"+(count+1));
				});
			</script>
		</tr>

		<tr>
			<td id="tdButtonsContactTab" colspan="4">
				<input type="button" id="buttonContactTabNext" value="<s:property value='getText("label.ai.tabs.commons.button.nextTab")' />" class="rightButton" />
				<input type="button" id="buttonContactTabSave" value="<s:property value='getText("label.ai.tabs.commons.button.save")' />" class="rightButton" />
				<script type="text/javascript">
					//current tab
					$("table#contactTable input#buttonContactTabSave").click(clickContactAction);
				</script>
			</td>
		</tr>
	</table>
</div>