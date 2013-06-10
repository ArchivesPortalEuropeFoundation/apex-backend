<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<div id="headerContainer">
</div>

<div id="relationsTabContent">
	<table id="resourceRelationTable_1" class="tablePadding">
		<tr>
			<td id="resourceLabel" colspan="4">
				<s:property value="getText('label.ai.relations.resourceRelations')" />
			</td>
			
		</tr>

		<tr>
			<td id="tdWebsiteOfResource">
				<s:property value="getText('label.ai.relations.websiteOfResource')" />:
			</td>
			<td>
				<input type="text" id="textWebsiteOfResource" value="${loader.resourceRelationHref}" onchange="relationsLinkToYourHolndingsGuideChanged();" />
			</td>
			<td id="tdTypeOfRelation" class="labelLeft">
				<label for="selectTypeOfYourRelation"><s:property value="getText('label.ai.relations.typeOfRelation')" />:</label>
			</td>
			<td>
				<select id="selectTypeOfYourRelation" >
					<s:iterator value="typeYourRelationList" var="relation"> 
						<option value="<s:property value="#relation.key" />"<s:if test="%{#relation.key == loader.resourceRelationType}" > selected=selected </s:if>><s:property value="#relation.value" /></option>
					</s:iterator>
				</select>
			</td>
		</tr>

		<tr>
			<td id="tdTitleOfRelatedMaterial">
				<s:property value="getText('label.ai.relations.titleOfRelatedMaterial')" />:
			</td>
			<td>
				<input type="text" id="textTitleOfRelatedMaterial" value="${loader.resourceRelationrelationEntry}" onchange="relationsLinkToYourHolndingsGuideTitleChanged();" />
			</td>
			<td colspan="2">
			</td>
		</tr>

		<tr id="trRelationsDescriptionOfRelation">
			<td id="tdDescriptionOfRelation">
				<s:property value="getText('label.ai.relations.descriptionOfRelation')" />:
			</td>
			<td>
				<input type="text" id="textDescriptionOfRelation" value="${loader.eagRelationrelationEntryDescription}" />
			</td>
			<td id="tdLanguageDescriptionOfRelation" class="labelLeft">
				<label for="selectLanguageDescriptionOfRelation"><s:property value="getText('label.ai.tabs.commons.selectLanguage')" />:</label>
			</td>
			<td>
				<select id="selectLanguageDescriptionOfRelation" >
					<s:iterator value="languageList" var="language"> 
						<option value="<s:property value="#language.key" />"<s:if test="%{#language.key == loader.eagRelationrelationEntryDescriptionLang}" > selected=selected </s:if>><s:property value="#language.value" /></option>
					</s:iterator>
				</select>
			</td>
		</tr>
	</table>

	<table id="addResourceRelationTable" class="tablePadding">
		<tr>
			<td id="tdAddNewResourceRelation" colspan="2">
				<input type="button" id="buttonRelationAddNewResourceRelation" onclick="relationAddNewResourceRelation('<s:property value="getText('label.ai.tabs.commons.pleaseFillData')" />');" value="<s:property value='getText("label.ai.relations.addNewResourceRelation")' />"/>
			</td>
			<td colspan="2">
			</td>
		</tr>
	</table>

	<table id="institutionRelationTable_1" class="tablePadding">
		<tr>
			<td id="institutionLabel" colspan="4">
				<s:property value="getText('label.ai.relations.institutionRelation')" />
			</td>
			
		</tr>

		<tr>
			<td id="tdWebsiteOfDescription">
				<s:property value="getText('label.ai.relations.websiteOfDescription')" />:
			</td>
			<td>
				<input type="text" id="textWebsiteOfDescription" value="${loader.eagRelationHref}" />
			</td>
			<td id="tdInstitutionTypeOfRelation" class="labelLeft">
				<label for="selectTypeOftheRelation"><s:property value="getText('label.ai.relations.institutionTypeOfRelation')" />:</label>
			</td>
			<td>
				<select id="selectTypeOftheRelation" >
					<s:iterator value="typeTheRelationList" var="relation"> 
						<option value="<s:property value="#relation.key" />" <s:if test="loader.resourceRelationType == #relation.value">selected="selected"</s:if>><s:property value="#relation.value" /></option>
					</s:iterator>
				</select>
			</td>
			<td colspan="2">
			</td>
			
		</tr>

		<tr>
			<td id="tdTitleOfRelatedInstitution">
				<s:property value="getText('label.ai.relations.titleOfRelatedInstitution')" />:
			</td>
			<td>
				<input type="text" id="textTitleOfRelatedInstitution" value="${loader.eagRelationrelationEntry}" />
			</td>
			<td colspan="2">
			</td>
		</tr>

		<tr id="trRelationsInstitutionDescriptionOfRelation">
			<td id="tdInstitutionDescriptionOfRelation">
				<s:property value="getText('label.ai.relations.descriptionOfRelation')" />:
			</td>
			<td>
				<input type="text" id="textInstitutionDescriptionOfRelation" value="${loader.eagRelationrelationEntryDescription}" />
			</td>
			<td id="tdLanguageInstitutionDescriptionOfRelation" class="labelLeft">
				<label for="selectLanguageInstitutionDescriptionOfRelation"><s:property value="getText('label.ai.tabs.commons.selectLanguage')" />:</label>
			</td>
			<td>
				<select id="selectLanguageInstitutionDescriptionOfRelation" >
					<s:iterator value="languageList" var="language"> 
						<option value="<s:property value="#language.key" />"<s:if test="%{#language.key == loader.eagRelationrelationEntryDescriptionLang}" > selected=selected </s:if>><s:property value="#language.value" /></option>
					</s:iterator>
				</select>
			</td>
		</tr>
	</table>

	<table id="relationsOtherTable" class="tablePadding">
		<tr>
			<td id="tdAddNewInstitutionRelation" colspan="2">
				<input type="button" id="buttonAddNewInstitutionRelation" onclick="relationAddNewInstitutionRelation('<s:property value="getText('label.ai.tabs.commons.pleaseFillData')" />');" value="<s:property value='getText("label.ai.relations.addNewInstitutionRelation")' />"/>
			</td>
			<td colspan="2">
			</td>
		</tr>

		<tr>
			<td id="tdButtonsDescriptionTab" colspan="4">
				<input type="button" id="buttonRelationsTabCheck" value="<s:property value='getText("label.ai.tabs.commons.button.check")' />" class="rightButton" onclick="clickRelationsAction('<s:property value="getText('label.ai.tabs.commons.fieldRequired')" />');"/>
			</td>
		</tr>

	</table>
</div>