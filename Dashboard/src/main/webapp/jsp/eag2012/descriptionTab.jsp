<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<div id="headerContainer">
</div>

<s:if test="%{loader.numberOfRepositories > 0}">
	<div id=descriptionTabContent>
		<s:set var="counter" value="0"/>
		<s:iterator begin="1" end="loader.numberOfRepositories" status="status">
			<table id="descriptionTable_<s:property value="%{#status.index + 1}" />" class="tablePadding">
				<tr>
					<td id="repositoryLabel" colspan="4">
						<s:property value="getText('eag2012.description.repositoryDescription')" />
					</td>
					
				</tr>

				<s:if test="%{loader.descRepositorhist.size() > 0}">
					<s:set var="repositorhist" value="loader.descRepositorhist[#counter]"/>
					<s:set var="repositorhistLang" value="loader.descRepositorhistLang[#counter]"/>
					<s:if test="%{#repositorhist.size() > 0}">
						<s:iterator var="internalCurrent" value="#repositorhist" status="internalStatus">
							<tr id="trRepositoryHistory_<s:property value="%{#internalStatus.index + 1}" />">
								<td id="tdRepositoryHistory_<s:property value="%{#internalStatus.index + 1}" />">
									<label for="textRepositoryHistory_<s:property value="%{#internalStatus.index + 1}" />"><s:property value="getText('eag2012.description.repositoryHistory')" />:</label>
								</td>
								<td>
									<textarea id="textRepositoryHistory_<s:property value="%{#internalStatus.index + 1}" />" ><s:property value="#internalCurrent" /></textarea>
								</td>
								<td id="tdLanguageRepositoryHistory_<s:property value="%{#internalStatus.index + 1}" />" class="labelLeft">
									<label for="selectLanguageRepositoryHistory_<s:property value="%{#internalStatus.index + 1}" />" ><s:property value="getText('eag2012.commons.selectLanguage')" />:</label>
								</td>
								<td>
									<select id="selectLanguageRepositoryHistory_<s:property value="%{#internalStatus.index + 1}" />">
										<s:iterator value="languageList" var="language"> 
											<option value="<s:property value="#language.key" />"<s:if test="%{#language.key == #repositorhistLang[#internalStatus.index]}" > selected=selected </s:if>><s:property value="#language.value" /></option>
										</s:iterator>
									</select>
								</td>
							</tr>
						</s:iterator>
					</s:if>
					<s:else>
						<tr id="trRepositoryHistory_1">
							<td id="tdRepositoryHistory_1">
								<label for="textRepositoryHistory_1"><s:property value="getText('eag2012.description.repositoryHistory')" />:</label>
							</td>
							<td>
								<textarea id="textRepositoryHistory_1" ></textarea>
							</td>
							<td id="tdLanguageRepositoryHistory_1" class="labelLeft">
								<label for="selectLanguageRepositoryHistory_1"><s:property value="getText('eag2012.commons.selectLanguage')" />:</label>
							</td>
							<td>
								<select id="selectLanguageRepositoryHistory_1">
									<s:iterator value="languageList" var="language"> 
										<option value="<s:property value="#language.key" />"><s:property value="#language.value" /></option>
									</s:iterator>
								</select>
							</td>
						</tr>
					</s:else>
				</s:if>
				<s:else>
					<tr id="trRepositoryHistory_1">
						<td id="tdRepositoryHistory_1">
							<label for="textRepositoryHistory_1"><s:property value="getText('eag2012.description.repositoryHistory')" />:</label>
						</td>
						<td>
							<textarea id="textRepositoryHistory_1" ></textarea>
						</td>
						<td id="tdLanguageRepositoryHistory_1" class="labelLeft">
							<label for="selectLanguageRepositoryHistory_1"><s:property value="getText('eag2012.commons.selectLanguage')" />:</label>
						</td>
						<td>
							<select id="selectLanguageRepositoryHistory_1">
								<s:iterator value="languageList" var="language"> 
									<option value="<s:property value="#language.key" />"><s:property value="#language.value" /></option>
								</s:iterator>
							</select>
						</td>
					</tr>
				</s:else>

				<tr id="trAddHistoryDescription">
					<td id="tdAddHistoryDescription" colspan="2">
						<input type="button" id="buttonAddHistoryDescription" onclick="descriptionAddHistoryDescription('<s:property value="getText('eag2012.commons.pleaseFillData')" />');" value="<s:property value='getText("eag2012.isil.addHistoryDescription")' />"/>
					</td>
					<td colspan="2">
					</td>
				</tr>

				<tr>
					<td id="tdDateOfRepositoryFoundation">
						<label for="textDateOfRepositoryFoundation"><s:property value="getText('eag2012.description.foundationDate')" />:</label>
					</td>
					<td>
						<s:if test="%{loader.descRepositorFoundDate.size() > 0}">
							<s:set var="descRepoFoundDate" value="loader.descRepositorFoundDate[#counter]"/>
							<s:if test="%{#descRepoFoundDate.size() > 0}">
								<input type="text" id="textDateOfRepositoryFoundation" value="<s:property value="#descRepoFoundDate[0]" />" />
							</s:if>
							<s:else>
								<input type="text" id="textDateOfRepositoryFoundation" />
							</s:else>
						</s:if>
						<s:else>
							<input type="text" id="textDateOfRepositoryFoundation" />
						</s:else>
					</td>
					<td colspan="2"></td>
				</tr>

				<s:if test="%{loader.descRepositorFoundRule.size() > 0}">
					<s:set var="repositorFoundRule" value="loader.descRepositorFoundRule[#counter]"/>
					<s:set var="repositorFoundRuleLang" value="loader.descRepositorFoundRuleLang[#counter]"/>
					<s:if test="%{#repositorFoundRule.size() > 0}">
						<s:iterator var="internalCurrent" value="#repositorFoundRule" status="internalStatus">
							<tr id="trRuleOfRepositoryFoundation_<s:property value="%{#internalStatus.index + 1}" />">
								<td id="tdRuleOfRepositoryFoundation_<s:property value="%{#internalStatus.index + 1}" />">
									<label for="textRuleOfRepositoryFoundation_<s:property value="%{#internalStatus.index + 1}" />"><s:property value="getText('eag2012.description.ruleOfRepositoryFoundation')" />:</label>
								</td>
								<td>
									<textarea id="textRuleOfRepositoryFoundation_<s:property value="%{#internalStatus.index + 1}" />"><s:property value="#internalCurrent" /></textarea>
								</td>
								<td id="tdLanguageRuleOfRepositoryFoundation_<s:property value="%{#internalStatus.index + 1}" />" class="labelLeft">
									<label for="selectLanguageRuleOfRepositoryFoundation_<s:property value="%{#internalStatus.index + 1}" />" ><s:property value="getText('eag2012.commons.selectLanguage')" />:</label>
								</td>
								<td>
									<select id="selectLanguageRuleOfRepositoryFoundation_<s:property value="%{#internalStatus.index + 1}" />">
										<s:iterator value="languageList" var="language"> 
											<option value="<s:property value="#language.key" />"<s:if test="%{#language.key == #repositorFoundRuleLang[#internalStatus.index]}" > selected=selected </s:if>><s:property value="#language.value" /></option>
										</s:iterator>
									</select>
								</td>
							</tr>
						</s:iterator>
					</s:if>
					<s:else>
						<tr id="trRuleOfRepositoryFoundation_1">
							<td id="tdRuleOfRepositoryFoundation_1">
								<label for="textRuleOfRepositoryFoundation_1"><s:property value="getText('eag2012.description.ruleOfRepositoryFoundation')" />:</label>
							</td>
							<td>
								<textarea id="textRuleOfRepositoryFoundation_1"></textarea>
							</td>
							<td id="tdLanguageRuleOfRepositoryFoundation_1" class="labelLeft">
								<label for="selectLanguageRuleOfRepositoryFoundation_1"><s:property value="getText('eag2012.commons.selectLanguage')" />:</label>
							</td>
							<td>
								<select id="selectLanguageRuleOfRepositoryFoundation_1">
									<s:iterator value="languageList" var="language"> 
										<option value="<s:property value="#language.key" />"><s:property value="#language.value" /></option>
									</s:iterator>
								</select>
							</td>
						</tr>
					</s:else>
				</s:if>
				<s:else>
					<tr id="trRuleOfRepositoryFoundation_1">
						<td id="tdRuleOfRepositoryFoundation_1">
							<label for="textRuleOfRepositoryFoundation_1"><s:property value="getText('eag2012.description.ruleOfRepositoryFoundation')" />:</label>
						</td>
						<td>
							<textarea id="textRuleOfRepositoryFoundation_1"></textarea>
						</td>
						<td id="tdLanguageRuleOfRepositoryFoundation_1" class="labelLeft">
							<label for="selectLanguageRuleOfRepositoryFoundation_1"><s:property value="getText('eag2012.commons.selectLanguage')" />:</label>
						</td>
						<td>
							<select id="selectLanguageRuleOfRepositoryFoundation_1">
								<s:iterator value="languageList" var="language"> 
									<option value="<s:property value="#language.key" />"><s:property value="#language.value" /></option>
								</s:iterator>
							</select>
						</td>
					</tr>
				</s:else>

				<tr>
					<td id="tdAddFoundationInformation" colspan="2">
						<input type="button" id="buttonDescriptionAddFoundationInformation" value="<s:property value='getText("eag2012.description.addRule")' />" onclick="descriptionAddFoundationInformation('<s:property value="getText('eag2012.commons.pleaseFillData')" />');"/>
					</td>
					<td colspan="2">
					</td>
				</tr>

				<tr>
					<td id="tdDateOfRepositorySuppression">
						<label for="textDateOfRepositorySuppression"><s:property value="getText('eag2012.description.dateArchiveClosure')" />:</label>
					</td>
					<td>
						<s:if test="%{loader.descRepositorSupDate.size() > 0}">
							<s:set var="descRepoSupDate" value="loader.descRepositorSupDate[#counter]"/>
							<s:if test="%{#descRepoSupDate.size() > 0}">
								<input type="text" id="textDateOfRepositorySuppression" value="<s:property value="#descRepoSupDate[0]" />" />
							</s:if>
							<s:else>
								<input type="text" id="textDateOfRepositorySuppression" />
							</s:else>
						</s:if>
						<s:else>
							<input type="text" id="textDateOfRepositorySuppression" />
						</s:else>
					</td>
					<td colspan="2"></td>
				</tr>

				<s:if test="%{loader.descRepositorSupRule.size() > 0}">
					<s:set var="repositorSupRule" value="loader.descRepositorSupRule[#counter]"/>
					<s:set var="repositorSupRuleLang" value="loader.descRepositorSupRuleLang[#counter]"/>
					<s:if test="%{#repositorSupRule.size() > 0}">
						<s:iterator var="internalCurrent" value="#repositorSupRule" status="internalStatus">
							<tr id="trDescriptionAddSuppressionInformation_<s:property value="%{#internalStatus.index + 1}" />">
								<td id="tdRuleOfRepositorySuppression_<s:property value="%{#internalStatus.index + 1}" />">
									<label for="textRuleOfRepositorySuppression_<s:property value="%{#internalStatus.index + 1}" />"><s:property value="getText('eag2012.description.ruleOfRepositorySuppression')" />:</label>
								</td>
								<td>
									<textarea id="textRuleOfRepositorySuppression_<s:property value="%{#internalStatus.index + 1}" />"><s:property value="#internalCurrent" /></textarea>
								</td>
								<td id="tdLanguageRuleOfRepositorySuppression_<s:property value="%{#internalStatus.index + 1}" />" class="labelLeft">
									<label for="selectLanguageRuleOfRepositorySuppression_<s:property value="%{#internalStatus.index + 1}" />" ><s:property value="getText('eag2012.commons.selectLanguage')" />:</label>
								</td>
								<td>
									<select id="selectLanguageRuleOfRepositorySuppression_<s:property value="%{#internalStatus.index + 1}" />">
										<s:iterator value="languageList" var="language"> 
											<option value="<s:property value="#language.key" />"<s:if test="%{#language.key == #repositorSupRuleLang[#internalStatus.index]}" > selected=selected </s:if>><s:property value="#language.value" /></option>
										</s:iterator>
									</select>
								</td>
							</tr>
						</s:iterator>
					</s:if>
					<s:else>
						<tr id="trDescriptionAddSuppressionInformation_1">
							<td id="tdRuleOfRepositorySuppression_1">
								<label for="textRuleOfRepositorySuppression_1"><s:property value="getText('eag2012.description.ruleOfRepositorySuppression')" />:</label>
							</td>
							<td>
								<textarea id="textRuleOfRepositorySuppression_1"></textarea>
							</td>
							<td id="tdLanguageRuleOfRepositorySuppression_1" class="labelLeft">
								<label for="selectLanguageRuleOfRepositorySuppression_1"><s:property value="getText('eag2012.commons.selectLanguage')" />:</label>
							</td>
							<td>
								<select id="selectLanguageRuleOfRepositorySuppression_1">
									<s:iterator value="languageList" var="language"> 
										<option value="<s:property value="#language.key" />"><s:property value="#language.value" /></option>
									</s:iterator>
								</select>
							</td>
						</tr>
					</s:else>
				</s:if>
				<s:else>
					<tr id="trDescriptionAddSuppressionInformation_1">
						<td id="tdRuleOfRepositorySuppression_1">
							<label for="textRuleOfRepositorySuppression_1"><s:property value="getText('eag2012.description.ruleOfRepositorySuppression')" />:</label>
						</td>
						<td>
							<textarea id="textRuleOfRepositorySuppression_1"></textarea>
						</td>
						<td id="tdLanguageRuleOfRepositorySuppression_1" class="labelLeft">
							<label for="selectLanguageRuleOfRepositorySuppression_1"><s:property value="getText('eag2012.commons.selectLanguage')" />:</label>
						</td>
						<td>
							<select id="selectLanguageRuleOfRepositorySuppression_1">
								<s:iterator value="languageList" var="language"> 
									<option value="<s:property value="#language.key" />"><s:property value="#language.value" /></option>
								</s:iterator>
							</select>
						</td>
					</tr>
				</s:else>

				<tr>
					<td id="tdAddSuppressionInformation" colspan="2">
						<input type="button" id="buttonDescriptionAddSuppressionInformation" onclick="descriptionAddSuppressionInformation('<s:property value="getText('eag2012.commons.pleaseFillData')" />');" value="<s:property value='getText("eag2012.description.addRule")' />"/>
					</td>
					<td colspan="2">
					</td>
				</tr>

				<tr>
					<td id="administrativeLabel" colspan="4">
						<s:property value="getText('eag2012.description.administrativeStructure')" />
					</td>
					
				</tr>

				<s:if test="%{loader.descAdminunit.size() > 0}">
					<s:set var="adminunit" value="loader.descAdminunit[#counter]"/>
					<s:set var="adminunitLang" value="loader.descAdminunitLang[#counter]"/>
					<s:if test="%{#adminunit.size() > 0}">
						<s:iterator var="internalCurrent" value="#adminunit" status="internalStatus">
							<tr id="trDescriptionAddAdministrationUnits_<s:property value="%{#internalStatus.index + 1}" />">
								<td id="tdUnitOfAdministrativeStructure_<s:property value="%{#internalStatus.index + 1}" />">
									<label for="textUnitOfAdministrativeStructure_<s:property value="%{#internalStatus.index + 1}" />"><s:property value="getText('eag2012.description.unitOfAdministrativeStructure')" />:</label>
								</td>
								<td>
									<textarea id="textUnitOfAdministrativeStructure_<s:property value="%{#internalStatus.index + 1}" />"><s:property value="#internalCurrent" /></textarea>
								</td>
								<td id="tdLanguageUnitOfAdministrativeStructure_<s:property value="%{#internalStatus.index + 1}" />" class="labelLeft">
									<label for="selectLanguageUnitOfAdministrativeStructure_<s:property value="%{#internalStatus.index + 1}" />" ><s:property value="getText('eag2012.commons.selectLanguage')" />:</label>
								</td>
								<td>
									<select id="selectLanguageUnitOfAdministrativeStructure_<s:property value="%{#internalStatus.index + 1}" />">
										<s:iterator value="languageList" var="language"> 
											<option value="<s:property value="#language.key" />"<s:if test="%{#language.key == #adminunitLang[#internalStatus.index]}" > selected=selected </s:if>><s:property value="#language.value" /></option>
										</s:iterator>
									</select>
								</td>
							</tr>
						</s:iterator>
					</s:if>
					<s:else>
						<tr id="trDescriptionAddAdministrationUnits_1">
							<td id="tdUnitOfAdministrativeStructure_1">
								<label for="textUnitOfAdministrativeStructure_1"><s:property value="getText('eag2012.description.unitOfAdministrativeStructure')" />:</label>
							</td>
							<td>
								<textarea id="textUnitOfAdministrativeStructure_1"></textarea>
							</td>
							<td id="tdLanguageUnitOfAdministrativeStructure_1" class="labelLeft">
								<label for="selectLanguageUnitOfAdministrativeStructure_1"><s:property value="getText('eag2012.commons.selectLanguage')" />:</label>
							</td>
							<td>
								<select id="selectLanguageUnitOfAdministrativeStructure_1">
									<s:iterator value="languageList" var="language"> 
										<option value="<s:property value="#language.key" />"><s:property value="#language.value" /></option>
									</s:iterator>
								</select>
							</td>
						</tr>
					</s:else>
				</s:if>
				<s:else>
					<tr id="trDescriptionAddAdministrationUnits_1">
						<td id="tdUnitOfAdministrativeStructure_1">
							<label for="textUnitOfAdministrativeStructure_1"><s:property value="getText('eag2012.description.unitOfAdministrativeStructure')" />:</label>
						</td>
						<td>
							<textarea id="textUnitOfAdministrativeStructure_1"></textarea>
						</td>
						<td id="tdLanguageUnitOfAdministrativeStructure_1" class="labelLeft">
							<label for="selectLanguageUnitOfAdministrativeStructure_1"><s:property value="getText('eag2012.commons.selectLanguage')" />:</label>
						</td>
						<td>
							<select id="selectLanguageUnitOfAdministrativeStructure_1">
								<s:iterator value="languageList" var="language"> 
									<option value="<s:property value="#language.key" />"><s:property value="#language.value" /></option>
								</s:iterator>
							</select>
						</td>
					</tr>
				</s:else>

				<tr>
					<td id="tdAddAdministrationUnits" colspan="2">
						<input type="button" id="buttonDescriptionAddAdministrationUnits" onclick="descriptionAddAdministrationUnits('<s:property value="getText('eag2012.commons.pleaseFillData')" />');" value="<s:property value='getText("eag2012.description.addAdministrationUnits")' />" />
					</td>
					<td colspan="2">
					</td>
				</tr>

				<tr>
					<td id="buildingLabel" colspan="4">
						<s:property value="getText('eag2012.description.buildingDescription')" />
					</td>
					
				</tr>

				<s:if test="%{loader.descBuilding.size() > 0}">
					<s:set var="building" value="loader.descBuilding[#counter]"/>
					<s:set var="buildingLang" value="loader.descBuildingLang[#counter]"/>
					<s:if test="%{#building.size() > 0}">
						<s:iterator var="internalCurrent" value="#building" status="internalStatus">
							<tr id="trBuildingDescription_<s:property value="%{#internalStatus.index + 1}" />">
								<td id="tdBuilding_<s:property value="%{#internalStatus.index + 1}" />">
									<label for="textBuilding_<s:property value="%{#internalStatus.index + 1}" />"><s:property value="getText('eag2012.description.building')" />:</label>
								</td>
								<td>
									<textarea id="textBuilding_<s:property value="%{#internalStatus.index + 1}" />"><s:property value="#internalCurrent" /></textarea>
								</td>
								<td id="tdLanguageBuilding_<s:property value="%{#internalStatus.index + 1}" />" class="labelLeft">
									<label for="selectLanguageBuilding_<s:property value="%{#internalStatus.index + 1}" />" ><s:property value="getText('eag2012.commons.selectLanguage')" />:</label>
								</td>
								<td>
									<select id="selectLanguageBuilding_<s:property value="%{#internalStatus.index + 1}" />">
										<s:iterator value="languageList" var="language"> 
											<option value="<s:property value="#language.key" />"<s:if test="%{#language.key == #buildingLang[#internalStatus.index]}" > selected=selected </s:if>><s:property value="#language.value" /></option>
										</s:iterator>
									</select>
								</td>
							</tr>
						</s:iterator>
					</s:if>
					<s:else>
						<tr id="trBuildingDescription_1">
							<td id="tdBuilding_1">
								<label for="textBuilding_1"><s:property value="getText('eag2012.description.building')" />:</label>
							</td>
							<td>
								<textarea id="textBuilding_1"></textarea>
							</td>
							<td id="tdLanguageBuilding_1" class="labelLeft">
								<label for="selectLanguageBuilding_1"><s:property value="getText('eag2012.commons.selectLanguage')" />:</label>
							</td>
							<td>
								<select id="selectLanguageBuilding_1">
									<s:iterator value="languageList" var="language"> 
										<option value="<s:property value="#language.key" />"><s:property value="#language.value" /></option>
									</s:iterator>
								</select>
							</td>
						</tr>
					</s:else>
				</s:if>
				<s:else>
					<tr id="trBuildingDescription_1">
						<td id="tdBuilding_1">
							<label for="textBuilding_1"><s:property value="getText('eag2012.description.building')" />:</label>
						</td>
						<td>
							<textarea id="textBuilding_1"></textarea>
						</td>
						<td id="tdLanguageBuilding_1" class="labelLeft">
							<label for="selectLanguageBuilding_1"><s:property value="getText('eag2012.commons.selectLanguage')" />:</label>
						</td>
						<td>
							<select id="selectLanguageBuilding_1">
								<s:iterator value="languageList" var="language"> 
									<option value="<s:property value="#language.key" />"><s:property value="#language.value" /></option>
								</s:iterator>
							</select>
						</td>
					</tr>
				</s:else>

				<tr>
					<td id="tdAddBuildingDescription" colspan="2">
						<input type="button" id="buttonDescriptionBuildingDescription" onclick="descriptionAddBuildingDescription('<s:property value="getText('eag2012.commons.pleaseFillData')" />');" value="<s:property value='getText("eag2012.isil.addBuildingDescription")' />" />
					</td>
					<td colspan="2">
					</td>
				</tr>

				<tr>
					<td id="tdRepositoryArea">
						<label for="textRepositoryArea"><s:property value="getText('eag2012.description.repositoryArea')" />:</label>
					</td>
					<td>
						<s:if test="%{loader.descRepositorarea.size() > 0}">
							<s:set var="repositorarea" value="loader.descRepositorarea[#counter]"/>
							<s:if test="%{#repositorarea.size() > 0}">
								<input type="text" id="textRepositoryArea" value="<s:property value="#repositorarea[0]" />" />
							</s:if>
							<s:else>
								<input type="text" id="textRepositoryArea" />
							</s:else>
						</s:if>
						<s:else>
							<input type="text" id="textRepositoryArea" />
						</s:else>
					</td>
					<td colspan="2"></td>
				</tr>

				<tr>
					<td id="tdLengthOfShelf">
						<label for="textLengthOfShelf"><s:property value="getText('eag2012.description.lengthShelf')" />:</label>
					</td>
					<td>
						<s:if test="%{loader.descLengthshelf.size() > 0}">
							<s:set var="lengthshelf" value="loader.descLengthshelf[#counter]"/>
							<s:if test="%{#lengthshelf.size() > 0}">
								<input type="text" id="textLengthOfShelf" value="<s:property value="#lengthshelf[0]" />" />
							</s:if>
							<s:else>
								<input type="text" id="textLengthOfShelf" />
							</s:else>
						</s:if>
						<s:else>
							<input type="text" id="textLengthOfShelf" />
						</s:else>
					</td>
					<td colspan="2"></td>
				</tr>

				<tr>
					<td id="holdingLabel" colspan="4">
						<s:property value="getText('eag2012.description.holdingDescription')" />
					</td>
					
				</tr>

				<s:if test="%{loader.descHoldings.size() > 0}">
					<s:set var="holdings" value="loader.descHoldings[#counter]"/>
					<s:set var="holdingsLang" value="loader.descHoldingsLang[#counter]"/>
					<s:if test="%{#holdings.size() > 0}">
						<s:iterator var="internalCurrent" value="#holdings" status="internalStatus">
							<tr id="trArchivalAndOtherHoldings_<s:property value="%{#internalStatus.index + 1}" />">
								<td id="tdArchivalAndOtherHoldings_<s:property value="%{#internalStatus.index + 1}" />">
									<label for="textArchivalAndOtherHoldings_<s:property value="%{#internalStatus.index + 1}" />"><s:property value="getText('eag2012.description.archivalAndOtherHoldings')" />:</label>
								</td>
								<td>
									<textarea id="textArchivalAndOtherHoldings_<s:property value="%{#internalStatus.index + 1}" />"><s:property value="#internalCurrent" /></textarea>
								</td>
								<td id="tdLanguageArchivalAndOtherHoldings_<s:property value="%{#internalStatus.index + 1}" />" class="labelLeft">
									<label for="selectLanguageArchivalAndOtherHoldings_<s:property value="%{#internalStatus.index + 1}" />" ><s:property value="getText('eag2012.commons.selectLanguage')" />:</label>
								</td>
								<td>
									<select id="selectLanguageArchivalAndOtherHoldings_<s:property value="%{#internalStatus.index + 1}" />">
										<s:iterator value="languageList" var="language"> 
											<option value="<s:property value="#language.key" />"<s:if test="%{#language.key == #holdingsLang[#internalStatus.index]}" > selected=selected </s:if>><s:property value="#language.value" /></option>
										</s:iterator>
									</select>
								</td>
							</tr>
						</s:iterator>
					</s:if>
					<s:else>
						<tr id="trArchivalAndOtherHoldings_1">
							<td id="tdArchivalAndOtherHoldings_1">
								<label for="textArchivalAndOtherHoldings_1"><s:property value="getText('eag2012.description.archivalAndOtherHoldings')" />:</label>
							</td>
							<td>
								<textarea id="textArchivalAndOtherHoldings_1"></textarea>
							</td>
							<td id="tdLanguageArchivalAndOtherHoldings_1" class="labelLeft">
								<label for="selectLanguageArchivalAndOtherHoldings_1"><s:property value="getText('eag2012.commons.selectLanguage')" />:</label>
							</td>
							<td>
								<select id="selectLanguageArchivalAndOtherHoldings_1">
									<s:iterator value="languageList" var="language"> 
										<option value="<s:property value="#language.key" />"><s:property value="#language.value" /></option>
									</s:iterator>
								</select>
							</td>
						</tr>
					</s:else>
				</s:if>
				<s:else>
					<tr id="trArchivalAndOtherHoldings_1">
						<td id="tdArchivalAndOtherHoldings_1">
							<label for="textArchivalAndOtherHoldings_1"><s:property value="getText('eag2012.description.archivalAndOtherHoldings')" />:</label>
						</td>
						<td>
							<textarea id="textArchivalAndOtherHoldings_1"></textarea>
						</td>
						<td id="tdLanguageArchivalAndOtherHoldings_1" class="labelLeft">
							<label for="selectLanguageArchivalAndOtherHoldings_1"><s:property value="getText('eag2012.commons.selectLanguage')" />:</label>
						</td>
						<td>
							<select id="selectLanguageArchivalAndOtherHoldings_1">
								<s:iterator value="languageList" var="language"> 
									<option value="<s:property value="#language.key" />"><s:property value="#language.value" /></option>
								</s:iterator>
							</select>
						</td>
					</tr>
				</s:else>

				<tr>
					<td id="tdAddAnotherArchivalDescription" colspan="2">
						<input type="button" id="buttonDescriptionAddAnotherArchivalDescription" onclick="descriptionAddAnotherArchivalDescription('<s:property value="getText('eag2012.commons.pleaseFillData')" />');" value="<s:property value='getText("eag2012.isil.addArchivalAndOtherHoldingsDescription")' />" />
					</td>
					<td colspan="2">
					</td>
				</tr>

				<tr>
					<td colspan="4">
						<s:property value="getText('eag2012.description.yearsOfTheHoldings')" />
					</td>
				</tr>

				<s:if test="%{loader.descHoldingsDate.size() > 0}">
					<s:set var="dateOfHoldings" value="loader.descHoldingsDate[#counter]"/>
					<s:if test="%{#dateOfHoldings.size() > 0}">
						<s:iterator var="internalCurrent" value="#dateOfHoldings" status="internalStatus">
							<tr id="trYearWhenThisNameWasUsed_<s:property value="%{#internalStatus.index + 1}" />">
								<td>
									<label for="textYearWhenThisNameWasUsed_<s:property value="%{#internalStatus.index + 1}" />"><s:property value="getText('eag2012.commons.year')" />:</label>
								</td>
								<td>
									<input type="text" id="textYearWhenThisNameWasUsed_<s:property value="%{#internalStatus.index + 1}" />" value="<s:property value="#internalCurrent" />" />
								</td>
								<td colspan="2">
								</td>
							</tr>
						</s:iterator>
					</s:if>
					<s:else>
						<tr id="trYearWhenThisNameWasUsed_1">
							<td>
								<label for="textYearWhenThisNameWasUsed_1"><s:property value="getText('eag2012.commons.year')" />:</label>
							</td>
							<td>
								<input type="text" id="textYearWhenThisNameWasUsed_1" />
							</td>
							<td colspan="2">
							</td>
						</tr>
					</s:else>
				</s:if>
				<s:else>
					<tr id="trYearWhenThisNameWasUsed_1">
						<td>
							<label for="textYearWhenThisNameWasUsed_1"><s:property value="getText('eag2012.commons.year')" />:</label>
						</td>
						<td>
							<input type="text" id="textYearWhenThisNameWasUsed_1" />
						</td>
						<td colspan="2">
						</td>
					</tr>
				</s:else>

				<s:if test="%{loader.descNumberOfHoldingsDateRange.size() > 0}">
					<s:set var="holdingsDateRange" value="loader.descNumberOfHoldingsDateRange[#counter]"/>
					<s:set var="holdingsFromDate" value="loader.descHoldingsDateRangeFromDate[#counter]"/>
					<s:set var="holdingsToDate" value="loader.descHoldingsDateRangeToDate[#counter]"/>
					<s:if test="%{#holdingsDateRange.size() > 0}">
						<s:iterator var="internalCurrent" value="#holdingsDateRange" status="internalStatus">
							<tr id="trYearRangeWhenThisNameWasUsed_<s:property value="%{#internalStatus.index + 1}" />">
								<td>
									<label for="textIdentityYearFrom_<s:property value="%{#internalStatus.index + 1}" />"><s:property value="getText('eag2012.commons.yearFrom')" />:</label>
								</td>
								<td>
									<input type="text" id="textYearWhenThisNameWasUsedFrom_<s:property value="%{#internalStatus.index + 1}" />" value="<s:property value="#holdingsFromDate[#internalStatus.index]" />" />
								</td>
								<td class="labelLeft">
									<label for="textYearWhenThisNameWasUsedTo_<s:property value="%{#internalStatus.index + 1}" />" ><s:property value="getText('eag2012.commons.to')" />:</label>
								</td>
								<td>
									<input type="text" id="textYearWhenThisNameWasUsedTo_<s:property value="%{#internalStatus.index + 1}" />" value="<s:property value="#holdingsToDate[#internalStatus.index]" />" />
								</td>
							</tr>
						</s:iterator>
					</s:if>
				</s:if>

				<tr>
					<td>
						<input type="button" id="buttonDescriptionAddSingleYear" value="<s:property value="getText('eag2012.commons.addYearButton')" />" onclick="addSingleYear($(this).parent().parent().parent().parent(), '<s:property value="getText('eag2012.commons.pleaseFillData')" />');" />
					</td>
					<td>
						<input type="button" id="buttonDescriptionAddYearRange" value="<s:property value="getText('eag2012.commons.addYearRangeButton')" />" onclick="addRangeYear($(this).parent().parent().parent().parent(), '<s:property value="getText('eag2012.commons.yearFrom')" />', '<s:property value="getText('eag2012.commons.to')" />', '<s:property value="getText('eag2012.commons.pleaseFillData')" />');" />
					</td>
					<td colspan="2">
					</td>
				</tr>

				<tr>
					<td id="tdExtent">
						<label for="textExtent"><s:property value="getText('eag2012.description.extent')" />:</label>
					</td>
					<td>
						<s:if test="%{loader.descExtent.size() > 0}">
							<s:set var="extent" value="loader.descExtent[#counter]"/>
							<s:if test="%{#extent.size() > 0}">
								<input type="text" id="textExtent" value="<s:property value="#extent[0]" />" />
							</s:if>
							<s:else>
								<input type="text" id="textExtent" />
							</s:else>
						</s:if>
						<s:else>
							<input type="text" id="textExtent" />
						</s:else>
					</td>
					<td colspan="2"></td>
				</tr>

				<tr>
					<td id="tdButtonsDescriptionTab" colspan="4">
						<s:if test="%{#counter == 0}">
							<input type="button" id="buttonDescriptionTabNext" value="<s:property value='getText("eag2012.commons.nextTab")' />" class="rightButton" onclick="checkAndShowNextTab($(this).parent().parent().parent().parent(), '<s:property value="getText('eag2012.errors.fieldRequired')" />', '<s:property value="getText('eag2012.commons.pleaseFillMandatoryFields')" />','<s:property value="getText('eag2012.commons.eagwithurlwarnings')"/>');" />
						</s:if>
						<input type="button" id="buttonDescriptionTabPrevious" value="<s:property value='getText("eag2012.commons.previousTab")' />" class="rightButton" onclick="checkAndShowPreviousTab($(this).parent().parent().parent().parent(), '<s:property value="getText('eag2012.errors.fieldRequired')" />', '<s:property value="getText('eag2012.commons.pleaseFillMandatoryFields')" />','<s:property value="getText('eag2012.commons.eagwithurlwarnings')"/>');" />
					</td>
				</tr>
			</table>

			<s:set var="counter" value="%{#counter + 1}"/>
		</s:iterator>
	</div>
</s:if>

<div id=descriptionTabContent style="display:none;">
	<table id="descriptionTable" class="tablePadding">
		<tr>
			<td id="repositoryLabel" colspan="4">
				<s:property value="getText('eag2012.description.repositoryDescription')" />
			</td>
			
		</tr>

		<tr id="trRepositoryHistory_1">
			<td id="tdRepositoryHistory_1">
				<label for="textRepositoryHistory_1"><s:property value="getText('eag2012.description.repositoryHistory')" />:</label>
			</td>
			<td>
				<textarea id="textRepositoryHistory_1" ></textarea>
			</td>
			<td id="tdLanguageRepositoryHistory_1" class="labelLeft">
				<label for="selectLanguageRepositoryHistory_1"><s:property value="getText('eag2012.commons.selectLanguage')" />:</label>
			</td>
			<td>
				<select id="selectLanguageRepositoryHistory_1">
					<s:iterator value="languageList" var="language"> 
						<option value="<s:property value="#language.key" />"><s:property value="#language.value" /></option>
					</s:iterator>
				</select>
			</td>
		</tr>

		<tr id="trAddHistoryDescription">
			<td id="tdAddHistoryDescription" colspan="2">
				<input type="button" id="buttonAddHistoryDescription" onclick="descriptionAddHistoryDescription('<s:property value="getText('eag2012.commons.pleaseFillData')" />');" value="<s:property value='getText("eag2012.isil.addHistoryDescription")' />"/>
			</td>
			<td colspan="2">
			</td>
		</tr>

		<tr>
			<td id="tdDateOfRepositoryFoundation">
				<label for="textDateOfRepositoryFoundation"><s:property value="getText('eag2012.description.foundationDate')" />:</label>
			</td>
			<td>
				<input type="text" id="textDateOfRepositoryFoundation" />
			</td>
			<td colspan="2"></td>
		</tr>

		<tr id="trRuleOfRepositoryFoundation_1">
			<td id="tdRuleOfRepositoryFoundation_1">
				<label for="textRuleOfRepositoryFoundation_1"><s:property value="getText('eag2012.description.ruleOfRepositoryFoundation')" />:</label>
			</td>
			<td>
				<textarea id="textRuleOfRepositoryFoundation_1"></textarea>
			</td>
			<td id="tdLanguageRuleOfRepositoryFoundation_1" class="labelLeft">
				<label for="selectLanguageRuleOfRepositoryFoundation_1"><s:property value="getText('eag2012.commons.selectLanguage')" />:</label>
			</td>
			<td>
				<select id="selectLanguageRuleOfRepositoryFoundation_1">
					<s:iterator value="languageList" var="language"> 
						<option value="<s:property value="#language.key" />"><s:property value="#language.value" /></option>
					</s:iterator>
				</select>
			</td>
		</tr>

		<tr>
			<td id="tdAddFoundationInformation" colspan="2">
				<input type="button" id="buttonDescriptionAddFoundationInformation" value="<s:property value='getText("eag2012.description.addRule")' />" onclick="descriptionAddFoundationInformation('<s:property value="getText('eag2012.commons.pleaseFillData')" />');"/>
			</td>
			<td colspan="2">
			</td>
		</tr>

		<tr>
			<td id="tdDateOfRepositorySuppression">
				<label for="textDateOfRepositorySuppression"><s:property value="getText('eag2012.description.dateArchiveClosure')" />:</label>
			</td>
			<td>
				<input type="text" id="textDateOfRepositorySuppression" />
			</td>
			<td colspan="2"></td>
		</tr>

		<tr id="trDescriptionAddSuppressionInformation_1">
			<td id="tdRuleOfRepositorySuppression_1">
				<label for="textRuleOfRepositorySuppression_1"><s:property value="getText('eag2012.description.ruleOfRepositorySuppression')" />:</label>
			</td>
			<td>
				<textarea id="textRuleOfRepositorySuppression_1"></textarea>
			</td>
			<td id="tdLanguageRuleOfRepositorySuppression_1" class="labelLeft">
				<label for="selectLanguageRuleOfRepositorySuppression_1"><s:property value="getText('eag2012.commons.selectLanguage')" />:</label>
			</td>
			<td>
				<select id="selectLanguageRuleOfRepositorySuppression_1">
					<s:iterator value="languageList" var="language"> 
						<option value="<s:property value="#language.key" />"><s:property value="#language.value" /></option>
					</s:iterator>
				</select>
			</td>
		</tr>

		<tr>
			<td id="tdAddSuppressionInformation" colspan="2">
				<input type="button" id="buttonDescriptionAddSuppressionInformation" onclick="descriptionAddSuppressionInformation('<s:property value="getText('eag2012.commons.pleaseFillData')" />');" value="<s:property value='getText("eag2012.description.addRule")' />"/>
			</td>
			<td colspan="2">
			</td>
		</tr>

		<tr>
			<td id="administrativeLabel" colspan="4">
				<s:property value="getText('eag2012.description.administrativeStructure')" />
			</td>
			
		</tr>

		<tr id="trDescriptionAddAdministrationUnits_1">
			<td id="tdUnitOfAdministrativeStructure_1">
				<label for="textUnitOfAdministrativeStructure_1"><s:property value="getText('eag2012.description.unitOfAdministrativeStructure')" />:</label>
			</td>
			<td>
				<textarea id="textUnitOfAdministrativeStructure_1"></textarea>
			</td>
			<td id="tdLanguageUnitOfAdministrativeStructure_1" class="labelLeft">
				<label for="selectLanguageUnitOfAdministrativeStructure_1"><s:property value="getText('eag2012.commons.selectLanguage')" />:</label>
			</td>
			<td>
				<select id="selectLanguageUnitOfAdministrativeStructure_1">
					<s:iterator value="languageList" var="language"> 
						<option value="<s:property value="#language.key" />"><s:property value="#language.value" /></option>
					</s:iterator>
				</select>
			</td>
		</tr>

		<tr>
			<td id="tdAddAdministrationUnits" colspan="2">
				<input type="button" id="buttonDescriptionAddAdministrationUnits" onclick="descriptionAddAdministrationUnits('<s:property value="getText('eag2012.commons.pleaseFillData')" />');" value="<s:property value='getText("eag2012.description.addAdministrationUnits")' />" />
			</td>
			<td colspan="2">
			</td>
		</tr>

		<tr>
			<td id="buildingLabel" colspan="4">
				<s:property value="getText('eag2012.description.buildingDescription')" />
			</td>
			
		</tr>

		<tr id="trBuildingDescription_1">
			<td id="tdBuilding_1">
				<label for="textBuilding_1"><s:property value="getText('eag2012.description.building')" />:</label>
			</td>
			<td>
				<textarea id="textBuilding_1"></textarea>
			</td>
			<td id="tdLanguageBuilding_1" class="labelLeft">
				<label for="selectLanguageBuilding_1"><s:property value="getText('eag2012.commons.selectLanguage')" />:</label>
			</td>
			<td>
				<select id="selectLanguageBuilding_1">
					<s:iterator value="languageList" var="language"> 
						<option value="<s:property value="#language.key" />"><s:property value="#language.value" /></option>
					</s:iterator>
				</select>
			</td>
		</tr>

		<tr>
			<td id="tdAddBuildingDescription" colspan="2">
				<input type="button" id="buttonDescriptionBuildingDescription" onclick="descriptionAddBuildingDescription('<s:property value="getText('eag2012.commons.pleaseFillData')" />');" value="<s:property value='getText("eag2012.isil.addBuildingDescription")' />" />
			</td>
			<td colspan="2">
			</td>
		</tr>

		<tr>
			<td id="tdRepositoryArea">
				<label for="textRepositoryArea"><s:property value="getText('eag2012.description.repositoryArea')" />:</label>
			</td>
			<td>
				<input type="text" id="textRepositoryArea" />
			</td>
			<td colspan="2"></td>
		</tr>

		<tr>
			<td id="tdLengthOfShelf">
				<label for="textLengthOfShelf"><s:property value="getText('eag2012.description.lengthShelf')" />:</label>
			</td>
			<td>
				<input type="text" id="textLengthOfShelf" />
			</td>
			<td colspan="2"></td>
		</tr>

		<tr>
			<td id="holdingLabel" colspan="4">
				<s:property value="getText('eag2012.description.holdingDescription')" />
			</td>
			
		</tr>

		<tr id="trArchivalAndOtherHoldings_1">
			<td id="tdArchivalAndOtherHoldings_1">
				<label for="textArchivalAndOtherHoldings_1"><s:property value="getText('eag2012.description.archivalAndOtherHoldings')" />:</label>
			</td>
			<td>
				<textarea id="textArchivalAndOtherHoldings_1"></textarea>
			</td>
			<td id="tdLanguageArchivalAndOtherHoldings_1" class="labelLeft">
				<label for="selectLanguageArchivalAndOtherHoldings_1"><s:property value="getText('eag2012.commons.selectLanguage')" />:</label>
			</td>
			<td>
				<select id="selectLanguageArchivalAndOtherHoldings_1">
					<s:iterator value="languageList" var="language"> 
						<option value="<s:property value="#language.key" />"><s:property value="#language.value" /></option>
					</s:iterator>
				</select>
			</td>
		</tr>

		<tr>
			<td id="tdAddAnotherArchivalDescription" colspan="2">
				<input type="button" id="buttonDescriptionAddAnotherArchivalDescription" onclick="descriptionAddAnotherArchivalDescription('<s:property value="getText('eag2012.commons.pleaseFillData')" />');" value="<s:property value='getText("eag2012.isil.addArchivalAndOtherHoldingsDescription")' />" />
			</td>
			<td colspan="2">
			</td>
		</tr>

		<tr>
			<td colspan="4">
				<s:property value="getText('eag2012.description.yearsOfTheHoldings')" />
			</td>
		</tr>

		<tr id="trYearWhenThisNameWasUsed_1">
			<td>
				<label for="textYearWhenThisNameWasUsed_1"><s:property value="getText('eag2012.commons.year')" />:</label>
			</td>
			<td>
				<input type="text" id="textYearWhenThisNameWasUsed_1" />
			</td>
			<td colspan="2">
			</td>
		</tr>

		<tr>
			<td>
				<input type="button" id="buttonDescriptionAddSingleYear" value="<s:property value="getText('eag2012.commons.addYearButton')" />" onclick="addSingleYear($(this).parent().parent().parent().parent(), '<s:property value="getText('eag2012.commons.pleaseFillData')" />');" />
			</td>
			<td>
				<input type="button" id="buttonDescriptionAddYearRange" value="<s:property value="getText('eag2012.commons.addYearRangeButton')" />" onclick="addRangeYear($(this).parent().parent().parent().parent(), '<s:property value="getText('eag2012.commons.yearFrom')" />', '<s:property value="getText('eag2012.commons.to')" />', '<s:property value="getText('eag2012.commons.pleaseFillData')" />');" />
			</td>
			<td colspan="2">
			</td>
		</tr>

		<tr>
			<td id="tdExtent">
				<label for="textExtent"><s:property value="getText('eag2012.description.extent')" />:</label>
			</td>
			<td>
				<input type="text" id="textExtent" />
			</td>
			<td colspan="2"></td>
		</tr>

		<tr>
			<td id="tdButtonsDescriptionTab" colspan="4">
				<input type="button" id="buttonDescriptionTabNext" value="<s:property value='getText("eag2012.commons.nextTab")' />" class="rightButton" onclick="checkAndShowNextTab($(this).parent().parent().parent().parent(), '<s:property value="getText('eag2012.errors.fieldRequired')" />', '<s:property value="getText('eag2012.commons.pleaseFillMandatoryFields')" />','<s:property value="getText('eag2012.commons.eagwithurlwarnings')"/>');" />
				<input type="button" id="buttonDescriptionTabPrevious" value="<s:property value='getText("eag2012.commons.previousTab")' />" class="rightButton" onclick="checkAndShowPreviousTab($(this).parent().parent().parent().parent(), '<s:property value="getText('eag2012.errors.fieldRequired')" />', '<s:property value="getText('eag2012.commons.pleaseFillMandatoryFields')" />','<s:property value="getText('eag2012.commons.eagwithurlwarnings')"/>');" />
			</td>
		</tr>
	</table>
</div>
