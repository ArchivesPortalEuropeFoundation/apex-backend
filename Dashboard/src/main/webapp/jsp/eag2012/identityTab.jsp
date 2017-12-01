<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%-- <script type="text/javascript">
	$(document).ready(function(){
		var  text = '<s:property value="getText('error.specialCharacters')" escape="false" />';
	//	checkAutforms(textSpecialCharacters);
		$("table#identityTableNameOfTheInstitution_").each(function(){
			var id = $(this).attr("id");
			$(id + " tr#trNameOfTheInstitution textarea#textNameOfTheInstitution").on("input", function(){
				checkName(text, $(this));
			});
		});
	});
</script>	 --%>	
<div id="headerContainer">
</div>

<div id="identityTabContent" style="float:left;width:100%;">
  	<table>
  		<tbody>
  			<tr>
  				<td colspan="4">
					<table id="identityTable" class="tablePadding">
						<tr>
							<td id="countryCodeFieldColumn">
								<label  for="textIdentityCountryCodeOfTheInstitution"><s:property value="getText('eag2012.commons.countryCode')" /><span class="required">*</span>:</label>
							</td>
							<td>
								<input type="text" id="textIdentityCountryCodeOfTheInstitution" value="${loader.countryCode}" disabled="disabled" />
							</td>
							<td colspan="2">
							</td>
						</tr>
				
						<tr>
							<td id="tdIdentifierOfTheInstitution">
								<label  for="textIdentityIdentifierOfTheInstitution"><s:property value="getText('eag2012.commons.identifierOfTheInstitution')" /><span class="required">*</span>:</label>
							</td>
							<td>
								<textarea id="textIdentityIdentifierOfTheInstitution" disabled="disabled">${loader.otherRepositorId}</textarea>
							</td>
							<td id="tdIdUsedInAPE" class="labelLeft">
								<label  for="textIdentityIdUsedInAPE"><s:property value="getText('eag2012.commons.idUsedInApe')" />:</label>
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
											<label for="textNameOfTheInstitution"><s:property value="getText('eag2012.commons.nameOfInstitution')" /><span class="required">*</span>:</label>
										</s:if>
										<s:else>
											<label for="textNameOfTheInstitution"><s:property value="getText('eag2012.commons.nameOfInstitution')" />:</label>
										</s:else>
									</td>
									<td colspan="2" class="textContact">
										<s:if test="%{#status.index == 0}">
											<textarea id="textNameOfTheInstitution" disabled="disabled" onchange="nameOfInstitutionChanged('<s:property value="getText('eag2012.commons.errorOnChangeNameOfInstitution')" />', '${loader.initialAutformEscaped}');"><s:property value="#current" /></textarea>
										</s:if>
										<s:else>
											<textarea id="textNameOfTheInstitution" onchange="nameOfInstitutionChanged('<s:property value="getText('eag2012.commons.errorOnChangeNameOfInstitution')" />', '${loader.initialAutformEscaped}');"><s:property value="#current" /></textarea>
										</s:else>
									</td>
									<td id="tdNameOfTheInstitutionLanguage">
										<label class="language" for="noti_languageList"><s:property value="getText('eag2012.commons.selectLanguage')" />:</label>
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
									<label for="textNameOfTheInstitution"><s:property value="getText('eag2012.commons.nameOfInstitution')" /><span class="required">*</span>:</label>
								</td>
								<td colspan="2" class="textContact">
									<textarea id="textNameOfTheInstitution" disabled="disabled" onchange="nameOfInstitutionChanged('<s:property value="getText('eag2012.commons.errorOnChangeNameOfInstitution')" />', '${loader.initialAutformEscaped}');"></textarea>
								</td>
								<td id="tdNameOfTheInstitutionLanguage">
									<label class="language" for="noti_languageList"><s:property value="getText('eag2012.commons.selectLanguage')" />:</label>
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
								<input id="buttonAddAnotherFormOfTheAuthorizedName" type="button" value="<s:property value='getText("eag2012.identity.addTranslationOfName")' />" onclick="addAnotherFormOfTheAuthorizedName('<s:property value="getText('eag2012.commons.pleaseFillData')" />', '<s:property value="getText('error.specialCharacters')" escape="false" />');"/>
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
										<label for="textParallelNameOfTheInstitution"><s:property value="getText('eag2012.commons.alternativeCurrentNameOfInstitution')" />:</label>
									</td>
									<td colspan="2" class="textContact">
										<s:if test="%{#status.index == 0}">
											<textarea id="textParallelNameOfTheInstitution" disabled="disabled"><s:property value="#current" /></textarea>
										</s:if>
										<s:else>
											<textarea id="textParallelNameOfTheInstitution"><s:property value="#current" /></textarea>
										</s:else>
									</td>
									<td id="tdNameOfTheInstitutionLanguage">
										<label class="language" for="pnoti_languageList"><s:property value="getText('eag2012.commons.selectLanguage')" />:</label>
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
									<label for=" "><s:property value="getText('eag2012.commons.alternativeCurrentNameOfInstitution')" />:</label>
								</td>
								<td colspan="2" class="textContact">
									<textarea id="textParallelNameOfTheInstitution" disabled="disabled"></textarea>
								</td>
								<td id="tdNameOfTheInstitutionLanguage">
									<label class="language" for="pnoti_languageList"><s:property value="getText('eag2012.commons.selectLanguage')" />:</label>
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
								<input type="button" id="buttonAddParallelNameOfTheInstitution" value="<s:property value='getText("eag2012.identity.addTranslationOfAlternativeCurrentName")' />" onclick="addParallelNameOfTheInstitution('<s:property value="getText('eag2012.commons.pleaseFillData')" />');"/>
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
											<label for="textFormerlyUsedName"><s:property value="getText('eag2012.identity.previousNameOfInstitution')" /></label>
										</td>
										<td colspan="2">
											<textarea id="textFormerlyUsedName"><s:property value="#current" /></textarea>
										</td>
										<td class="labelLeft">
											<label class="language" for="tfun_languageList"><s:property value="getText('eag2012.commons.selectLanguage')" /></label>
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
											<label for="textDatesWhenThisNameWasUsed"><s:property value="getText('eag2012.identity.yearsOfUsedName')" /></label>
										</td>
									</tr>
				
									<s:if test="%{loader.nonpreformDate.size() > 0 && loader.nonpreformDate[#counter].size() > 0}">
										<s:iterator var="internalCurrent" value="loader.nonpreformDate[#counter]" status="internalStatus">
											<tr id="trYearWhenThisNameWasUsed_<s:property value="%{#internalStatus.index + 1}" />">
												<td>
													<label for="textYearWhenThisNameWasUsed_<s:property value="%{#internalStatus.index + 1}" />"><s:property value="getText('eag2012.commons.year')" /></label>
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
												<label for="textYearWhenThisNameWasUsed_1"><s:property value="getText('eag2012.commons.year')" /></label>
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
													<label for="textIdentityYearFrom_<s:property value="%{#internalStatus.index + 1}" />"><s:property value="getText('eag2012.commons.yearFrom')" /></label>
												</td>
												<td>
													<input type="text" id="textYearWhenThisNameWasUsedFrom_<s:property value="%{#internalStatus.index + 1}" />" value="<s:property value="#internalCurrent" />" />
												</td>
												<td class="labelLeft">
													<label for="textYearWhenThisNameWasUsedTo_<s:property value="%{#internalStatus.index + 1}" />"><s:property value="getText('eag2012.commons.to')" /></label>
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
											<input type="button" id="buttonAddSingleYear" value="<s:property value="getText('eag2012.commons.addYearButton')" />" onclick="addSingleYear($(this).parent().parent().parent().parent(), '<s:property value="getText('eag2012.commons.pleaseFillData')" />');" />
										</td>
										<td>
											<input type="button" id="buttonAddYearRange" value="<s:property value="getText('eag2012.commons.addYearRangeButton')" />" onclick="addRangeYear($(this).parent().parent().parent().parent(), '<s:property value="getText('eag2012.commons.yearFrom')" />', '<s:property value="getText('eag2012.commons.to')" />', '<s:property value="getText('eag2012.commons.pleaseFillData')" />');" />
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
								<input type="button" id="buttonAddMoreAnotherFormerlyUsedName" value="<s:property value='getText("eag2012.identity.addPreviouslyUsedName")' />" onclick="addMoreAnotherFormerlyUsedName('<s:property value="getText('eag2012.identity.previousNameOfInstitution')" />', '<s:property value="getText('eag2012.commons.selectLanguage')" />', '<s:property value="getText('eag2012.identity.yearsOfUsedName')" />', '<s:property value="getText('eag2012.commons.year')" />', '<s:property value="getText('eag2012.commons.addYearButton')" />', '<s:property value="getText('eag2012.commons.addYearRangeButton')" />', '<s:property value="getText('eag2012.commons.yearFrom')" />', '<s:property value="getText('eag2012.commons.to')" />', '<s:property value="getText('eag2012.commons.pleaseFillData')" />');" />
							</td>
							<td colspan="2">
							</td>
						</tr>
					</table>
				
					<table id="identitySelectTypeOfTheInstitution" class="tablePadding">
						<tr>
							<td>
								<label for="textSelectTypeOfTheInstitution"><s:property value="getText('eag2012.identity.selectType')" />:</label>
							</td>
							<td colspan="2" class="textContact">
								<select id="selectTypeOfTheInstitution" multiple="multiple" size="4" onclick="selectTypeOfInstitutionOptionsIntoYITab();">
									<s:iterator value="typeOfInstitutionList" var="type"> 
										<option value="<s:property value="#type.key" />"
											<s:set var="isSelected" value="false"/>
											<s:iterator var="current" value="loader.repositoryType" status="status">
												<s:if test="#type.key == #current"> selected="selected"</s:if>
											</s:iterator> ><s:property value="#type.value" /></option>
									</s:iterator>
								</select>
							</td>
							<td>
							</td>
						</tr>
				
						<tr>
							<td id="tdButtonsContactTab" colspan="4">
								<input type="button" id="buttonIdentityTabNext" value="<s:property value='getText("eag2012.commons.nextTab")' />" class="rightButton" onclick="checkAndShowNextTab($(this).parent().parent().parent().parent(), '<s:property value="getText('eag2012.errors.fieldRequired')" />', '<s:property value="getText('eag2012.commons.pleaseFillMandatoryFields')" />','<s:property value="getText('eag2012.commons.eagwithurlwarnings')"/>');" />
							    <input type="button" id="buttonIdentityTabPrevious" value="<s:property value='getText("eag2012.commons.previousTab")' />" class="rightButton" onclick="checkAndShowPreviousTab($(this).parent().parent().parent().parent(),'<s:property value="getText('eag2012.errors.fieldRequired')" />','<s:property value="getText('eag2012.commons.pleaseFillMandatoryFields')" />','<s:property value="getText('eag2012.commons.eagwithurlwarnings')"/>');" />
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</tbody>
	</table>
</div>