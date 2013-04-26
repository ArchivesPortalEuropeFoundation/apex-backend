<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<div id="relationsTabContent">
	<table id="relationsTable">
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
				<input type="text" id="textWebsiteOfResource" value="<s:property value="#websiteOfResource" />" />
			</td>
			<td id="tdTypeOfRelation">
				<label for="selectTypeOfYourRelation"><s:property value="getText('label.ai.relations.typeOfRelation')" />:</label>
			</td>
			<td>
				<s:select theme="simple" id="selectTypeOfYourRelation" list="typeYourRelationList"></s:select>
			</td>
		</tr>

		<tr>
			<td id="tdTitleOfRelatedMaterial">
				<s:property value="getText('label.ai.relations.titleOfRelatedMaterial')" />:
			</td>
			<td>
				<input type="text" id="textTitleOfRelatedMaterial" value="<s:property value="#titleOfRelatedMaterial" />" />
			</td>
			<td colspan="2">
			</td>
		</tr>

		<tr>
			<td id="tdDescriptionOfRelation">
				<s:property value="getText('label.ai.relations.descriptionOfRelation')" />:
			</td>
			<td>
				<input type="text" id="textDescriptionOfRelation" value="<s:property value="#descriptionOfRelation" />" />
			</td>
			<td id="tdLanguageDescriptionOfRelation">
				<label for="selectLanguageDescriptionOfRelation"><s:property value="getText('label.ai.tabs.commons.selectLanguage')" />:</label>
			</td>
			<td>
				<s:select theme="simple" id="selectLanguageDescriptionOfRelation" list="languageList"></s:select>
			</td>
		</tr>

		<tr>
			<td id="tdAddNewResourceRelation" colspan="2">
				<input type="button" value="<s:property value='getText("label.ai.relations.addNewResourceRelation")' />"/>
			</td>
			<td colspan="2">
			</td>
		</tr>

		<tr>
			<td id="institutionLabel" colspan="2">
				<s:property value="getText('label.ai.relations.institutionRelation')" />
			</td>
			<td id="tdInstitutionTypeOfRelation">
				<label for="selectTypeOftheRelation"><s:property value="getText('label.ai.relations.institutionTypeOfRelation')" />:</label>
			</td>
			<td>
				<s:select theme="simple" id="selectTypeOftheRelation" list="typeTheRelationList"></s:select>
			</td>
		</tr>

		<tr>
			<td id="tdWebsiteOfDescription">
				<s:property value="getText('label.ai.relations.websiteOfDescription')" />:
			</td>
			<td>
				<input type="text" id="textWebsiteOfDescription" value="<s:property value="#websiteOfDescription" />" />
			</td>
			<td colspan="2">
			</td>
		</tr>

		<tr>
			<td id="tdTitleOfRelatedInstitution">
				<s:property value="getText('label.ai.relations.titleOfRelatedInstitution')" />:
			</td>
			<td>
				<input type="text" id="textTitleOfRelatedInstitution" value="<s:property value="#titleOfRelatedInstitution" />" />
			</td>
			<td colspan="2">
			</td>
		</tr>

		<tr>
			<td id="tdInstitutionDescriptionOfRelation">
				<s:property value="getText('label.ai.relations.descriptionOfRelation')" />:
			</td>
			<td>
				<input type="text" id="textInstitutionDescriptionOfRelation" value="<s:property value="#institutionDescriptionOfRelation" />" />
			</td>
			<td id="tdLanguageInstitutionDescriptionOfRelation">
				<label for="selectLanguageInstitutionDescriptionOfRelation"><s:property value="getText('label.ai.tabs.commons.selectLanguage')" />:</label>
			</td>
			<td>
				<s:select theme="simple" id="selectLanguageInstitutionDescriptionOfRelation" list="languageList"></s:select>
			</td>
		</tr>

		<tr>
			<td id="tdAddNewInstitutionRelation" colspan="2">
				<input type="button" value="<s:property value='getText("label.ai.relations.addNewInstitutionRelation")' />"/>
			</td>
			<td colspan="2">
			</td>
		</tr>

		<tr>
			<td id="tdButtonsDescriptionTab" colspan="4">
				<input type="button" id="buttonRelationsTabSave" value="<s:property value='getText("label.ai.tabs.commons.button.save")' />" class="rightButton" />
				<script type="text/javascript">
					//current tab
					$("table#relationsTable input#buttonRelationsTabSave").click(clickRelationsAction);
				</script>
			</td>
		</tr>

	</table>
</div>
