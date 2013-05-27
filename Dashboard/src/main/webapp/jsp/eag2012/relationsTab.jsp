<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<div id="relationsTabContent">
	<table id="resourceRelationTable_1">
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
			<td id="tdTypeOfRelation">
				<label for="selectTypeOfYourRelation"><s:property value="getText('label.ai.relations.typeOfRelation')" />:</label>
			</td>
			<td>
				<s:select theme="simple" id="selectTypeOfYourRelation" list="typeYourRelationList" value="resourceRelationType"></s:select>
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
				<input type="text" id="textDescriptionOfRelation" value="${loader.resourceRelationrelationEntryDescription}" />
			</td>
			<td id="tdLanguageDescriptionOfRelation">
				<label for="selectLanguageDescriptionOfRelation"><s:property value="getText('label.ai.tabs.commons.selectLanguage')" />:</label>
			</td>
			<td>
				<s:select theme="simple" id="selectLanguageDescriptionOfRelation" list="languageList" value="resourceRelationrelationEntryDescriptionLang"></s:select>
			</td>
		</tr>
	</table>

	<table id="addResourceRelationTable">
		<tr>
			<td id="tdAddNewResourceRelation" colspan="2">
				<input type="button" id="buttonRelationAddNewResourceRelation" onclick="relationAddNewResourceRelation('<s:property value="getText('label.ai.tabs.commons.pleaseFillData')" />');" value="<s:property value='getText("label.ai.relations.addNewResourceRelation")' />"/>
			</td>
			<td colspan="2">
			</td>
		</tr>
	</table>

	<table id="institutionRelationTable_1">
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
			<td id="tdInstitutionTypeOfRelation">
				<label for="selectTypeOftheRelation"><s:property value="getText('label.ai.relations.institutionTypeOfRelation')" />:</label>
			</td>
			<td>
				<s:select theme="simple" id="selectTypeOftheRelation" list="typeTheRelationList" value="eagRelationType"></s:select>
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
			<td id="tdLanguageInstitutionDescriptionOfRelation">
				<label for="selectLanguageInstitutionDescriptionOfRelation"><s:property value="getText('label.ai.tabs.commons.selectLanguage')" />:</label>
			</td>
			<td>
				<s:select theme="simple" id="selectLanguageInstitutionDescriptionOfRelation" list="languageList" value="eagRelationrelationEntryDescriptionLang"></s:select>
			</td>
		</tr>
	</table>

	<table id="relationsOtherTable">
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