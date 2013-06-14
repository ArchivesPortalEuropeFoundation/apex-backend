<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<div id="headerContainer">
</div>

<div id="identityTabContent" style="float:left;width:100%;">
	<table id="identityTable" class="tablePadding">
		<tr>
			<td id="countryCodeFieldColumn">
				<label  for="textIdentityCountryCodeOfTheInstitution"><s:property value="getText('label.ai.tabs.commons.countryCode')" /><span class="required">*</span>:</label>
			</td>
			<td>
				<input type="text" id="textIdentityCountryCodeOfTheInstitution" value="${loader.countryCode}" disabled="disabled" />
			</td>
			<td colspan="2">
			</td>
		</tr>

		<tr>
			<td id="tdIdentifierOfTheInstitution">
				<label  for="textIdentityIdentifierOfTheInstitution"><s:property value="getText('label.ai.tabs.commons.identifierOfTheInstitution')" /><span class="required">*</span>:</label>
			</td>
			<td>
				<input type="text" id="textIdentityIdentifierOfTheInstitution" value="${loader.otherRepositorId}" disabled="disabled" />
			</td>
			<td id="tdIdUsedInAPE" class="labelLeft">
				<label  for="textIdentityIdUsedInAPE"><s:property value="getText('label.ai.tabs.commons.idUsedInAPE')" />:</label>
			</td>
			<td>
				<input type="text" id="textIdentityIdUsedInAPE" value="<s:if test="%{!newEag}">${loader.recordId}</s:if>" disabled="disabled" />
			</td>
		</tr>
	</table>

	<s:if test="%{loader.idAutform.size() > 0}">
		<s:set var="counter" value="0"/>
		<s:iterator var="current" value="loader.idAutform" status="status">
			<table id="identityTableNameOfTheInstitution_<s:property value="%{#status.index + 1}" />" class="tablePadding">
				<tr id="trNameOfTheInstitution">
					<td id="tdNameOfTheInstitution">
						<s:if test="%{#status.index == 0}">
							<label for="textNameOfTheInstitution"><s:property value="getText('label.ai.tabs.commons.nameOfTheInstitution')" /><span class="required">*</span>:</label>
						</s:if>
						<s:else>
							<label for="textNameOfTheInstitution"><s:property value="getText('label.ai.tabs.commons.nameOfTheInstitution')" />:</label>
						</s:else>
					</td>
					<td>
						<s:if test="%{#status.index == 0}">
							<input type="text" id="textNameOfTheInstitution" value="<s:property value="#current" />" disabled="disabled" />
						</s:if>
						<s:else>
							<input type="text" id="textNameOfTheInstitution" value="<s:property value="#current" />" />
						</s:else>
					</td>
					<td id="tdNameOfTheInstitutionLanguage" class="labelLeft">
						<label for="noti_languageList"><s:property value="getText('label.ai.tabs.commons.selectLanguage')" />:</label>
					</td>
					<td>
						<s:if test="%{#status.index == 0}">
							<select id="noti_languageList" disabled="disabled" >
						</s:if>
						<s:else>
							<select id="noti_languageList">
						</s:else>
							<s:iterator value="languageList" var="language"> 
								<option value="<s:property value="#language.key" />"<s:if test="%{#language.key == loader.idAutformLang[#counter]}" > selected=selected </s:if>><s:property value="#language.value" /></option>
							</s:iterator>
						</select>
					</td>
				</tr>
			</table>
			<s:set var="counter" value="%{#counter + 1}"/>
		</s:iterator>
	</s:if>
	<s:else>
		<table id="identityTableNameOfTheInstitution_1" class="tablePadding">
			<tr id="trNameOfTheInstitution">
				<td id="tdNameOfTheInstitution">
					<label for="textNameOfTheInstitution"><s:property value="getText('label.ai.tabs.commons.nameOfTheInstitution')" /><span class="required">*</span>:</label>
				</td>
				<td>
					<input type="text" id="textNameOfTheInstitution" disabled="disabled" />
				</td>
				<td id="tdNameOfTheInstitutionLanguage" class="labelLeft">
					<label for="noti_languageList"><s:property value="getText('label.ai.tabs.commons.selectLanguage')" />:</label>
				</td>
				<td>
					<select id="noti_languageList" disabled="disabled" >
						<s:iterator value="languageList" var="language"> 
							<option value="<s:property value="#language.key" />"><s:property value="#language.value" /></option>
						</s:iterator>
					</select>
				</td>
			</tr>
		</table>
	</s:else>

	<table id="identityButtonAddNames" class="tablePadding">
		<tr>
			<td colspan="3">
				<input id="buttonAddAnotherFormOfTheAuthorizedName" type="button" value="<s:property value='getText("label.ai.identity.addAnotherFormOfTheAuthorizedName")' />" onclick="addAnotherFormOfTheAuthorizedName('<s:property value="getText('label.ai.tabs.commons.pleaseFillData')" />');"/>
			</td>
			<td>
			</td>
		</tr>
	</table>


	<s:if test="%{loader.idParform.size() > 0}">
		<s:set var="counter" value="0"/>
		<s:iterator var="current" value="loader.idParform" status="status">
			<table id="identityTableParallelNameOfTheInstitution_<s:property value="%{#status.index + 1}" />" class="tablePadding">
				<tr class="marginTop" id="trParallelNameOfTheInstitution" >
					<td>
						<label for="textParallelNameOfTheInstitution"><s:property value="getText('label.ai.tabs.commons.parallelNameOfTheInstitution')" />:</label>
					</td>
					<td>
						<s:if test="%{#status.index == 0}">
							<input type="text" id="textParallelNameOfTheInstitution" value="<s:property value="#current" />" disabled="disabled" />
						</s:if>
						<s:else>
							<input type="text" id="textParallelNameOfTheInstitution" value="<s:property value="#current" />" />
						</s:else>
					</td>
					<td id="tdNameOfTheInstitutionLanguage" class="labelLeft">
						<label for="pnoti_languageList"><s:property value="getText('label.ai.tabs.commons.selectLanguage')" />:</label>
					</td>
					<td>
						<s:if test="%{#status.index == 0}">
							<select id="pnoti_languageList" disabled="disabled" >
						</s:if>
						<s:else>
							<select id="pnoti_languageList">
						</s:else>
							<s:iterator value="languageList" var="language"> 
								<option value="<s:property value="#language.key" />"<s:if test="%{#language.key == loader.idParformLang[#counter]}" > selected=selected </s:if>><s:property value="#language.value" /></option>
							</s:iterator>
						</select>
					</td>
				</tr>
			</table>
			<s:set var="counter" value="%{#counter + 1}"/>
		</s:iterator>
	</s:if>
	<s:else>
		<table id="identityTableParallelNameOfTheInstitution_1" class="tablePadding">
			<tr class="marginTop" id="trParallelNameOfTheInstitution" >
				<td>
					<label for="textParallelNameOfTheInstitution"><s:property value="getText('label.ai.tabs.commons.parallelNameOfTheInstitution')" />:</label>
				</td>
				<td>
					<input type="text" id="textParallelNameOfTheInstitution" disabled="disabled" />
				</td>
				<td id="tdNameOfTheInstitutionLanguage" class="labelLeft">
					<label for="pnoti_languageList"><s:property value="getText('label.ai.tabs.commons.selectLanguage')" />:</label>
				</td>
				<td>
					<select id="pnoti_languageList" disabled="disabled" >
						<s:iterator value="languageList" var="language"> 
							<option value="<s:property value="#language.key" />"><s:property value="#language.value" /></option>
						</s:iterator>
					</select>
				</td>
			</tr>
		</table>
	</s:else>

	<table id="identityButtonAddParallelNames" class="tablePadding">
		<tr>
			<td colspan="2">
				<input type="button" id="buttonAddParallelNameOfTheInstitution" value="<s:property value='getText("label.ai.identity.addAnotherParallelNameOfTheInstitution")' />" onclick="addParallelNameOfTheInstitution('<s:property value="getText('label.ai.tabs.commons.pleaseFillData')" />');"/>
			</td>
			<td colspan="2">
			</td>
		</tr>
	</table>

	<s:if test="%{loader.nonpreform.size() > 0}">
			<s:set var="counter" value="0"/>
			<s:iterator var="current" value="loader.nonpreform" status="status">
				<table id="identityTableFormerlyUsedName_<s:property value="%{#status.index + 1}" />" class="tablePadding">
					<tr id="trTextFormerlyUsedName" class="marginTop">
						<td>
							<label for="textFormerlyUsedName"><s:property value="getText('label.ai.identity.formerlyUsedName')" /></label>
						</td>
						<td>
							<input type="text" id="textFormerlyUsedName" value="<s:property value="#current" />" />
						</td>
						<td class="labelLeft">
							<label for="tfun_languageList"><s:property value="getText('label.ai.tabs.commons.selectLanguage')" /></label>
						</td>
						<td>
							<select id="tfun_languageList">
								<s:iterator value="languageList" var="language"> 
									<option value="<s:property value="#language.key" />"
										<s:if test="%{#language.key == loader.nonpreformLang[#counter]}" > selected=selected </s:if>>
										<s:property value="#language.value" />
									</option>
								</s:iterator>
							</select>
						</td>
					</tr>

					<tr id="trLabelDatesWhenThisNameWasUsed">
						<td colspan="4">
							<label for="textDatesWhenThisNameWasUsed"><s:property value="getText('label.ai.identity.datesWhenThisNameWasUsed')" /></label>
						</td>
					</tr>

					<s:if test="%{loader.nonpreformDate.size() > 0 && loader.nonpreformDate[#counter].size() > 0}">
						<s:iterator var="internalCurrent" value="loader.nonpreformDate[#counter]" status="internalStatus">
							<tr id="trYearWhenThisNameWasUsed_<s:property value="%{#internalStatus.index + 1}" />">
								<td>
									<label for="textYearWhenThisNameWasUsed_<s:property value="%{#internalStatus.index + 1}" />"><s:property value="getText('label.ai.tabs.commons.year')" /></label>
								</td>
								<td>
									<input type="text" id="textYearWhenThisNameWasUsed_<s:property value="%{#internalStatus.index + 1}" />" value="<s:property value="#internalCurrent" />"/>
								</td>
								<td colspan="2">
								</td>
							</tr>
						</s:iterator>
					</s:if>
					<s:else>
						<tr id="trYearWhenThisNameWasUsed_1">
							<td>
								<label for="textYearWhenThisNameWasUsed_1"><s:property value="getText('label.ai.tabs.commons.year')" /></label>
							</td>
							<td>
								<input type="text" id="textYearWhenThisNameWasUsed_1"/>
							</td>
							<td colspan="2">
							</td>
						</tr>
					</s:else>

					<s:if test="%{loader.nonpreformDateFrom.size() > 0 && loader.nonpreformDateFrom[#counter].size() > 0 && loader.nonpreformDateTo.size() > 0 && loader.nonpreformDateTo[#counter].size() > 0 && loader.nonpreformDateFrom[#counter].size() == loader.nonpreformDateTo[#counter].size()}">
						<s:set var="internalCounter" value="0"/>
						<s:set var="internalValue" value="loader.nonpreformDateTo[#counter]"/>
						<s:iterator var="internalCurrent" value="loader.nonpreformDateFrom[#counter]" status="internalStatus">
							<tr id="trYearRangeWhenThisNameWasUsed_<s:property value="%{#internalStatus.index + 1}" />">
								<td>
									<label for="textIdentityYearFrom_<s:property value="%{#internalStatus.index + 1}" />"><s:property value="getText('label.ai.tabs.commons.yearFrom')" /></label>
								</td>
								<td>
									<input type="text" id="textYearWhenThisNameWasUsedFrom_<s:property value="%{#internalStatus.index + 1}" />" value="<s:property value="#internalCurrent" />" />
								</td>
								<td class="labelLeft">
									<label for="textYearWhenThisNameWasUsedTo_<s:property value="%{#internalStatus.index + 1}" />"><s:property value="getText('label.ai.tabs.commons.textTo')" /></label>
								</td>
								<td>
									<input type="text" id="textYearWhenThisNameWasUsedTo_<s:property value="%{#internalStatus.index + 1}" />" value="<s:property value="#internalValue[#internalCounter]" />" />
								</td>
							</tr>
							<s:set var="internalCounter" value="%{#internalCounter + 1}"/>
						</s:iterator>
					</s:if>

					<tr>
						<td>
							<input type="button" id="buttonAddSingleYear" value="<s:property value="getText('label.ai.tabs.commons.addSingleYear')" />" onclick="addSingleYear($(this).parent().parent().parent().parent(), '<s:property value="getText('label.ai.tabs.commons.pleaseFillData')" />');" />
						</td>
						<td>
							<input type="button" id="buttonAddYearRange" value="<s:property value="getText('label.ai.tabs.commons.addYearRange')" />" onclick="addRangeYear($(this).parent().parent().parent().parent(), '<s:property value="getText('label.ai.tabs.commons.yearFrom')" />', '<s:property value="getText('label.ai.tabs.commons.textTo')" />', '<s:property value="getText('label.ai.tabs.commons.pleaseFillData')" />');" />
						</td>
						<td colspan="2">
						</td>
					</tr>
				</table>
			<s:set var="counter" value="%{#counter + 1}"/>
		</s:iterator>
	</s:if>

	<table id="identityButtonAddFormerlyUsedName" class="tablePadding">
		<tr id="trAddMoreAnotherFormerlyUsedName">
			<td colspan="2">
				<input type="button" id="buttonAddMoreAnotherFormerlyUsedName" value="<s:property value='getText("label.ai.identity.addAnotherFormerlyUsedName")' />" onclick="addMoreAnotherFormerlyUsedName('<s:property value="getText('label.ai.identity.formerlyUsedName')" />', '<s:property value="getText('label.ai.tabs.commons.selectLanguage')" />', '<s:property value="getText('label.ai.identity.datesWhenThisNameWasUsed')" />', '<s:property value="getText('label.ai.tabs.commons.year')" />', '<s:property value="getText('label.ai.tabs.commons.addSingleYear')" />', '<s:property value="getText('label.ai.tabs.commons.addYearRange')" />', '<s:property value="getText('label.ai.tabs.commons.yearFrom')" />', '<s:property value="getText('label.ai.tabs.commons.textTo')" />', '<s:property value="getText('label.ai.tabs.commons.pleaseFillData')" />');" />
			</td>
			<td colspan="2">
			</td>
		</tr>
	</table>

	<table id="identitySelectTypeOfTheInstitution" class="tablePadding">
		<tr>
			<td>
				<label for="textSelectTypeOfTheInstitution"><s:property value="getText('label.ai.identity.selectTypeOfTheInstitution')" />:</label>
			</td>
			<td>
				<select id="selectTypeOfTheInstitution" multiple="multiple" size="4">
					<s:iterator value="typeOfInstitutionList" var="type"> 
						<option value="<s:property value="#type.key" />"
							<s:set var="isSelected" value="false"/>
							<s:iterator var="current" value="loader.repositoryType" status="status">
								<s:if test="%{#type.key == #current}">
									<s:set var="isSelected" value="true"/>
								</s:if>
							</s:iterator>
							<s:if test="%{#isSelected}" > selected=selected </s:if>>
							<s:property value="#type.value" />
						</option>
					</s:iterator>
				</select>
			</td>
			<td colspan="2">
			</td>
		</tr>

		<tr>
			<td id="tdButtonsContactTab" colspan="4">
				<input type="button" id="buttonIdentityTabNext" value="<s:property value='getText("label.ai.tabs.commons.button.nextTab")' />" class="rightButton" onclick="checkAndShowNextTab($(this).parent().parent().parent().parent(), '<s:property value="getText('label.ai.tabs.commons.fieldRequired')" />', '<s:property value="getText('label.ai.tabs.commons.pleaseFillMandatoryFields')" />');" />
				<input type="button" id="buttonIdentityTabCheck" value="<s:property value='getText("label.ai.tabs.commons.button.check")' />" class="rightButton" onclick="clickIdentityAction('<s:property value="getText('label.ai.tabs.commons.fieldRequired')" />');" />
			</td>
		</tr>
	</table>
</div>