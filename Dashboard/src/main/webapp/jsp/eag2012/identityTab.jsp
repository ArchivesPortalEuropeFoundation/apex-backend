<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<div id="identityTabContent" style="float:left;width:100%;">
	<table id="identityTable">
		<tr>
			<td id="countryCodeFieldColumn">
				<s:property value="getText('label.ai.tabs.commons.countryCode')" />
				<span class="required">*</span>:
			</td>
			<td>
				<input type="text" id="textIdentityCountryCodeOfTheInstitution" value="<s:property value="#countryCode" />" disabled="disabled" />
				<s:fielderror fieldName="textIdentityCountryCodeOfTheInstitution"/>
			</td>
		</tr>

		<tr>
			<td id="tdIdentifierOfTheInstitution">
				<s:property value="getText('label.ai.tabs.commons.identifierOfTheInstitution')" />
				<span class="required">*</span>:
			</td>
			<td>
				<input type="text" id="textIdentityIdentifierOfTheInstitution" value="<s:property value="#identifierOfTheInstitution" />" disabled="disabled" />
				<s:fielderror fieldName="textIdentityIdentifierOfTheInstitution"/>
			</td>
			<td id="tdIdUsedInAPE">
				<s:property value="getText('label.ai.tabs.commons.idUsedInAPE')" />:
			</td>
			<td>
				<input type="text" id="textIdentityIdUsedInAPE" value="<s:property value="#idUsedInAPE" />" disabled="disabled" />
			</td>
		</tr>

		<tr id="trNameOfTheInstitution">
			<td id="tdNameOfTheInstitution">
				<s:property value="getText('label.ai.tabs.commons.nameOfTheInstitution')" />
				<span class="required">*</span>:
			</td>
			<td>
				<input type="text" id="textIdentityIdUsedInAPE" value="<s:property value="#nameOfTheInstitution" />" disabled="disabled" />
				<s:fielderror fieldName="nameOfTheInstitution"/>
			</td>
			<td id="tdNameOfTheInstitutionLanguage">
				<label for="noti_languageList"><s:property value="getText('label.ai.tabs.commons.selectLanguage')" /></label>
				<span class="required">*</span>:
			</td>
			<td>
				<s:select theme="simple" id="noti_languageList" list="languageList"></s:select>
				<s:fielderror fieldName="noti_languageList"/>
			</td>
		</tr>

		<tr>
			<td colspan="2"><input id="buttonAddAnotherFormOfTheAuthorizedName" type="button" value="<s:property value='getText("label.ai.identity.addAnotherFormOfTheAuthorizedName")' />" class="longButton" /></td>
			<script type="text/javascript">
				$("table#identityTable input#buttonAddAnotherFormOfTheAuthorizedName").click(function(){
					var count = $("table#identityTable tr[id^='trNameOfTheInstitution']").length;
					var newId = "trNameOfTheInstitution_"+(count+1);
					var trHtml = "<tr id=\""+newId+"\">"+$("table#identityTable tr[id^='trNameOfTheInstitution']").clone().html()+"</tr>";
					var lastId = "table#identityTable tr#trNameOfTheInstitution";
					if(count>1){
						lastId+="_"+(count);
					}
					$(lastId).after(trHtml);
					//update last content
					$("#"+newId+" input#textIdentityIdUsedInAPE").attr("id","textIdentityIdUsedInAPE_"+(count+1));
					$("#"+newId+" td#tdNameOfTheInstitution").attr("id","tdNameOfTheInstitution_"+(count+1));
					$("#"+newId+" input#textIdentityIdUsedInAPE_"+(count+1)).attr("value","");
					$("#"+newId+" input#textIdentityIdUsedInAPE_"+(count+1)).removeAttr("disabled");
					$("#"+newId+" td#tdNameOfTheInstitutionLanguage").attr("id","tdNameOfTheInstitutionLanguage_"+(count+1));
					$("#"+newId+" label[for='noti_languageList']").attr("for","noti_languageList_"+(count+1));
					$("#"+newId+" select#noti_languageList").attr("id","noti_languageList_"+(count+1));
				});
			</script>
		</tr>

		<tr class="marginTop">
			<td>
				<label for="textParallelNameOfTheInstitution"><s:property value="getText('label.ai.tabs.commons.parallelNameOfTheInstitution')" />:</label>
			</td>
			<td>
				<input type="text" id="textParallelNameOfTheInstitution" value=""/>
			</td>
			<td id="tdNameOfTheInstitutionLanguage">
				<label for="pnoti_languageList"><s:property value="getText('label.ai.tabs.commons.selectLanguage')" />:</label>
			</td>
			<td>
				<s:select theme="simple" id="pnoti_languageList" list="languageList"></s:select>
			</td>
		</tr>

		<tr>
			<td colspan="2">
				<input type="button" value="<s:property value='getText("label.ai.identity.addAnotherParallelNameOfTheInstitution")' />" class="longButton" />
			</td>
			<td colspan="2">
			</td>
		</tr>

		<tr class="marginTop">
			<td>
				<label for="textFormerlyUsedName"><s:property value="getText('label.ai.identity.formerlyUsedName')" />:</label>
			</td>
			<td>
				<input type="text" id="textFormerlyUsedName" value=""/>
			</td>
			<td id="tdTextFormerlyUsedName">
				<label for="tfun_languageList"><s:property value="getText('label.ai.tabs.commons.selectLanguage')" />:</label>
			</td>
			<td>
				<s:select theme="simple" id="tfun_languageList" list="languageList"></s:select>
			</td>
		</tr>

		<tr>
			<td colspan="4">
				<label for="textDatesWhenThisNameWasUsed"><s:property value="getText('label.ai.identity.datesWhenThisNameWasUsed')" />:</label>
			</td>
		</tr>

		<tr>
			<td>
				<label for="textDatesWhenThisNameWasUsedFrom"><s:property value="getText('label.ai.tabs.commons.textFrom')" />:</label>
			</td>
			<td>
				<input type="text" id="textDatesWhenThisNameWasUsedFrom" value=""/>
			</td>
			<td>
				<label for="textDatesWhenThisNameWasUsedTo"><s:property value="getText('label.ai.tabs.commons.textTo')" />:</label>
			</td>
			<td>
				<input type="text" id="textDatesWhenThisNameWasUsedTo" value=""/>
			</td>
		</tr>

		<tr>
			<td>
				<input type="button" id="buttonAddMoreDates" value="<s:property value='getText("label.ai.identity.addMoreDates")' />" class="longButton" />
			</td>
			<td colspan="3">
			</td>
		</tr>

		<tr>
			<td colspan="2">
				<input type="button" id="buttonAddMoreAnotherFormerlyUsedName" value="<s:property value='getText("label.ai.identity.addAnotherFormerlyUsedName")' />" class="longButton" />
			</td>
			<td colspan="2">
			</td>
		</tr>

		<tr>
			<td>
				<label for="textSelectTypeOfTheInstitution"><s:property value="getText('label.ai.identity.selectTypeOfTheInstitution')" />:</label>
			</td>
			<td>
				<s:select theme="simple" id="selectTypeOfTheInstitution" list="typeOfInstitutionList" cssStyle="max-width:300px;" ></s:select>
			</td>
			<td colspan="2">
			</td>
		</tr>

		<tr>
			<td id="tdButtonsContactTab" colspan="4">
				<input type="button" id="buttonIdentityTabNext" value="<s:property value='getText("label.ai.tabs.commons.button.nextTab")' />" class="rightButton" />
				<input type="button" id="buttonIdentityTabExit" value="<s:property value='getText("label.ai.tabs.commons.button.exit")' />" class="rightButton" />
				<input type="button" id="buttonIdentityTabSave" value="<s:property value='getText("label.ai.tabs.commons.button.save")' />" class="rightButton" />
				<script type="text/javascript">
					//current tab
					$("table#identityTable input#buttonIdentityTabSave").click(clickIdentityAction);
				</script>
			</td>
		</tr>
	</table>
</div>