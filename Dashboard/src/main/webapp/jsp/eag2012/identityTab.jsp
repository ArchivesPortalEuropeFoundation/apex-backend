<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<div id="identityTabContent" style="float:left;width:100%;">
	<table id="identityTable">
		<tr>
			<td id="countryCodeFieldColumn">
				<label  for="textIdentityCountryCodeOfTheInstitution"><s:property value="getText('label.ai.tabs.commons.countryCode')" /><span class="required">*</span>:</label>
			</td>
			<td>
				<input type="text" id="textIdentityCountryCodeOfTheInstitution" value="${countryCode}" disabled="disabled" />
			</td>
		</tr>

		<tr>
			<td id="tdIdentifierOfTheInstitution">
				<label  for="textIdentityIdentifierOfTheInstitution"><s:property value="getText('label.ai.tabs.commons.identifierOfTheInstitution')" /><span class="required">*</span>:</label>
			</td>
			<td>
				<input type="text" id="textIdentityIdentifierOfTheInstitution" value="${idOfInstitution}" disabled="disabled" />
			</td>
			<td id="tdIdUsedInAPE" class="labelLeft">
				<label  for="textIdentityIdUsedInAPE"><s:property value="getText('label.ai.tabs.commons.idUsedInAPE')" />:</label>
			</td>
			<td>
				<input type="text" id="textIdentityIdUsedInAPE" value="${idUsedInAPE}" disabled="disabled" />
			</td>
		</tr>
	</table>

	<table id="identityTableNameOfTheInstitution_1">
		<tr id="trNameOfTheInstitution">
			<td id="tdNameOfTheInstitution">
				<label for="textNameOfTheInstitution"><s:property value="getText('label.ai.tabs.commons.nameOfTheInstitution')" /><span class="required">*</span>:</label>
			</td>
			<td>
				<input type="text" id="textNameOfTheInstitution" value="${nameOfInstitution}" disabled="disabled" />
			</td>
			<td id="tdNameOfTheInstitutionLanguage" class="labelLeft">
				<label for="noti_languageList"><s:property value="getText('label.ai.tabs.commons.selectLanguage')" /><span class="required">*</span>:</label>
			</td>
			<td>
				<s:select theme="simple" id="noti_languageList" list="languageList" disabled="true"></s:select>
			</td>
		</tr>
	</table>

	<table id="identityButtonAddNames">
		<tr>
			<td colspan="2"><input id="buttonAddAnotherFormOfTheAuthorizedName" type="button" value="<s:property value='getText("label.ai.identity.addAnotherFormOfTheAuthorizedName")' />" onclick="addAnotherFormOfTheAuthorizedName();"/></td>
		</tr>
	</table>

	<table id="identityTableParallelNameOfTheInstitution_1">
		<tr class="marginTop" id="trParallelNameOfTheInstitution" >
			<td>
				<label for="textParallelNameOfTheInstitution"><s:property value="getText('label.ai.tabs.commons.parallelNameOfTheInstitution')" />:</label>
			</td>
			<td>
				<input type="text" id="textParallelNameOfTheInstitution" value="${parallelNameOfInstitution}" disabled="disabled" />
			</td>
			<td id="tdNameOfTheInstitutionLanguage" class="labelLeft">
				<label for="pnoti_languageList"><s:property value="getText('label.ai.tabs.commons.selectLanguage')" />:</label>
			</td>
			<td>
				<s:select theme="simple" id="pnoti_languageList" list="languageList" disabled="true"></s:select>
			</td>
		</tr>
	</table>

	<table id="identityButtonAddParallelNames">
		<tr>
			<td colspan="2">
				<input type="button" id="buttonAddParallelNameOfTheInstitution" value="<s:property value='getText("label.ai.identity.addAnotherParallelNameOfTheInstitution")' />" onclick="addParallelNameOfTheInstitution();"/>
			</td>
		</tr>
	</table>

	<table id="identityButtonAddFormerlyUsedName">
		<tr id="trAddMoreAnotherFormerlyUsedName">
			<td colspan="2">
				<input type="button" id="buttonAddMoreAnotherFormerlyUsedName" value="<s:property value='getText("label.ai.identity.addAnotherFormerlyUsedName")' />" />
				<script type="text/javascript">
					$("#buttonAddMoreAnotherFormerlyUsedName").click(function(){


						var counter = $("table[id^='identityTableFormerlyUsedName_']").length;
						var property1 = "<s:property value="getText('label.ai.identity.formerlyUsedName')" />";
						var property2 = "<s:property value="getText('label.ai.tabs.commons.selectLanguage')" />";
						var property3 = "<s:property value="getText('label.ai.identity.datesWhenThisNameWasUsed')" />";
						var property4 = "<s:property value="getText('label.ai.tabs.commons.year')" />";
						var property5 = "<s:property value="getText('label.ai.tabs.commons.addSingleYear')" />";
						var property6 = "<s:property value="getText('label.ai.tabs.commons.addYearRange')" />";
						var select = '<select id="tfun_languageList">'+$("#pnoti_languageList").html()+'</select>';
						var text1 = "<s:property value="getText('label.ai.tabs.commons.yearFrom')" />";
						var text2 = "<s:property value="getText('label.ai.tabs.commons.textTo')" />";


						if (counter == 0) {
							$("table#identityButtonAddFormerlyUsedName").before('<table id="identityTableFormerlyUsedName_1">'+
								'<tr id="trTextFormerlyUsedName" class="marginTop">'+
									'<td>'+
										'<label for="textFormerlyUsedName">'+property1+':</label>'+
									'</td>'+
									'<td>'+
										'<input type="text" id="textFormerlyUsedName" value=""/>'+
									'</td>'+
									'<td class="labelLeft">'+
										'<label for="tfun_languageList">'+property2+':</label>'+
									'</td>'+
									'<td>'+select+
									'</td>'+
								'</tr>'+
								'<tr id="trLabelDatesWhenThisNameWasUsed">'+
									'<td colspan="4">'+
										'<label for="textDatesWhenThisNameWasUsed">'+property3+':</label>'+
									'</td>'+
								'</tr>'+
								'<tr id="trYearWhenThisNameWasUsed_1">'+
									'<td>'+
										'<label for="textYearWhenThisNameWasUsed_1">'+property4+':</label>'+
									'</td>'+
									'<td>'+
										'<input type="text" id="textYearWhenThisNameWasUsed_1" value=""/>'+
									'</td>'+
									'<td colspan="2">'+
									'</td>'+
								'</tr>'+
								'<tr>'+
									'<td>'+
										'<input type="button" id="buttonAddSingleYear" value="'+property5+'" onclick="addSingleYear($(this).parent().parent().parent().parent());" />'+
									'</td>'+
									'<td>'+
									'<input type="button" id="buttonAddYearRange" value="'+property6+'" onclick="addRangeYear($(this).parent().parent().parent().parent(), \''+text1+'\', \''+text2+'\');" />'+
								'</td>'+
									'<td colspan="2">'+
									'</td>'+
								'</tr></table>');
						} else {
							$("table#identityButtonAddFormerlyUsedName").before('<table id="identityTableFormerlyUsedName_'+(counter+1)+'">'+
								'<tr id="trTextFormerlyUsedName" class="marginTop">'+
									'<td>'+
										'<label for="textFormerlyUsedName">'+property1+':</label>'+
									'</td>'+
									'<td>'+
										'<input type="text" id="textFormerlyUsedName" value=""/>'+
									'</td>'+
									'<td class="labelLeft">'+
										'<label for="tfun_languageList">'+property2+':</label>'+
									'</td>'+
									'<td>'+select+
									'</td>'+
								'</tr>'+
								'<tr id="trLabelDatesWhenThisNameWasUsed">'+
									'<td colspan="4">'+
										'<label for="textDatesWhenThisNameWasUsed">'+property3+':</label>'+
									'</td>'+
								'</tr>'+
								'<tr id="trYearWhenThisNameWasUsed_1">'+
									'<td>'+
										'<label for="textYearWhenThisNameWasUsed_1">'+property4+':</label>'+
									'</td>'+
									'<td>'+
										'<input type="text" id="textYearWhenThisNameWasUsed_1" value=""/>'+
									'</td>'+
									'<td colspan="2">'+
									'</td>'+
								'</tr>'+
								'<tr>'+
									'<td>'+
										'<input type="button" id="buttonAddSingleYear" value="'+property5+'" onclick="addSingleYear($(this).parent().parent().parent().parent());" />'+
									'</td>'+
									'<td>'+
									'<input type="button" id="buttonAddYearRange" value="'+property6+'" onclick="addRangeYear($(this).parent().parent().parent().parent(), \''+text1+'\', \''+text2+'\');" />'+
								'</td>'+
									'<td colspan="2">'+
									'</td>'+
								'</tr></table>');
						}
					});
				</script>
			</td>
			<td colspan="2">
			</td>
		</tr>
	</table>

	<table id="identitySelectTypeOfTheInstitution">
		<tr>
			<td>
				<label for="textSelectTypeOfTheInstitution"><s:property value="getText('label.ai.identity.selectTypeOfTheInstitution')" />:</label>
			</td>
			<td>
				<s:select theme="simple" id="selectTypeOfTheInstitution" list="typeOfInstitutionList" ></s:select>
			</td>
			<td colspan="2">
			</td>
		</tr>

		<tr>
			<td id="tdButtonsContactTab" colspan="4">
				<input type="button" id="buttonIdentityTabNext" value="<s:property value='getText("label.ai.tabs.commons.button.nextTab")' />" class="rightButton" />
				<input type="button" id="buttonIdentityTabCheck" value="<s:property value='getText("label.ai.tabs.commons.button.check")' />" class="rightButton" onclick="clickIdentityAction('<s:property value="getText('label.ai.tabs.commons.fieldRequired')" />');" />
			</td>
		</tr>
	</table>
</div>