<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<div id="headerContainer">
</div>

<div id="relationsTabContent">
	<s:if test="%{loader.relationsResourceRelationType.size() > 0}">
	 <s:iterator var="current" value="loader.relationsResourceRelationType" status="status">
		<table id="resourceRelationTable_<s:property value="%{#status.index + 1}"/>"class="tablePadding">
			<tr>
				<td id="resourceLabel" colspan="4">
					<label for="resourceLabel"><s:property value="getText('eag2012.relations.resourceRelations')" />:</label>
				</td>
				
			</tr>
	
			<tr>
				<td id="tdWebsiteOfResource">
					<label for="textWebsiteOfResource"><s:property value="getText('eag2012.relations.linkToResourceRelation')" />:</label>
				</td>
				<td>
					<textarea id="textWebsiteOfResource" onchange="relationsLinkToYourHolndingsGuideChanged($(this).parent().parent().parent().parent());"><s:property value="loader.relationsResourceRelationHref[#status.index]" /></textarea>
				</td>
				<td id="tdTypeOfRelation" class="labelLeft">
					<label for="selectTypeOfYourRelation"><s:property value="getText('eag2012.relations.typeOfRelation')" />:</label>
				</td>
				<td>
					<select id="selectTypeOfYourRelation" >
						<s:iterator value="typeYourRelationList" var="relation"> 
							<option value="<s:property value="#relation.key" />"<s:if test="%{#relation.key == #current}" > selected=selected </s:if>><s:property value="#relation.value" /></option>
						</s:iterator>
					</select>
				</td>
			</tr>
			<tr>
				<td id="tdTitleOfRelatedMaterial">
					<label for="textTitleOfRelatedMaterial"><s:property value="getText('eag2012.relations.titleOfRelatedMaterial')" />:</label>
				</td>
				<td>
					<textarea id="textTitleOfRelatedMaterial" onchange="relationsLinkToYourHolndingsGuideTitleChanged($(this).parent().parent().parent().parent());"><s:property value="loader.relationsResourceRelationEntry[#status.index]" /></textarea>
				</td>
				<td class="labelLeft">
					<label for="selectTitleOfRelatedMaterialLang"><s:property value="getText('eag2012.commons.selectLanguage')" />:</label>
				</td>
				<td>
					<select id="selectTitleOfRelatedMaterialLang" onchange="selectTitleRelatedLangChange($(this).parent().parent().parent().parent());" >
						<s:iterator value="languageList" var="language"> 
							<option value="<s:property value="#language.key" />"<s:if test="%{#language.key == loader.relationsResourceRelationEntryLang[#status.index]}" > selected=selected </s:if>><s:property value="#language.value" /></option>
						</s:iterator>
					</select>
				</td>
			</tr>
	
			<tr id="trRelationsDescriptionOfRelation">
				<td id="tdDescriptionOfRelation">
					<label for="textDescriptionOfRelation"><s:property value="getText('eag2012.relations.descriptionOfRelation')" />:</label>
				</td>
				<td>
					<textarea id="textDescriptionOfRelation"><s:property value="loader.relationsResourceRelationEntryDescription[#status.index]" /></textarea>
				</td>
				<td id="tdLanguageDescriptionOfRelation" class="labelLeft">
					<label for="selectLanguageDescriptionOfRelation"><s:property value="getText('eag2012.commons.selectLanguage')" />:</label>
				</td>
				<td>
					<select id="selectLanguageDescriptionOfRelation">
						<s:iterator value="languageList" var="language"> 
							<option value="<s:property value="#language.key" />"<s:if test="%{#language.key == loader.relationsResourceRelationEntryDescriptionLang[#status.index]}" > selected=selected </s:if>><s:property value="#language.value" /></option>
						</s:iterator>
					</select>
				</td>
			</tr>
		</table>
	 </s:iterator>	
	</s:if>
	<s:else>
		<table id="resourceRelationTable_1" class="tablePadding">
			<tr>
				<td id="resourceLabel" colspan="4">
					<label for="resourceLabel"><s:property value="getText('eag2012.relations.resourceRelations')" />:</label>
				</td>
				
			</tr>
	
			<tr>
				<td id="tdWebsiteOfResource">
					<label for="tdWebsiteOfResource"><s:property value="getText('eag2012.relations.linkToResourceRelation')" />:</label>
				</td>
				<td>
					<textarea id="textWebsiteOfResource" onchange="relationsLinkToYourHolndingsGuideChanged($(this).parent().parent().parent().parent());"></textarea>
				</td>
				<td id="tdTypeOfRelation" class="labelLeft">
					<label for="selectTypeOfYourRelation"><s:property value="getText('eag2012.relations.typeOfRelation')" />:</label>
				</td>
				<td>
					<select id="selectTypeOfYourRelation" >
						<s:iterator value="typeYourRelationList" var="relation"> 
							<option value="<s:property value="#relation.key" />"><s:property value="#relation.value" /></option>
						</s:iterator>
					</select>
				</td>
			</tr>
	
			<tr>
				<td id="tdTitleOfRelatedMaterial">
					<label for="tdTitleOfRelatedMaterial"><s:property value="getText('eag2012.relations.titleOfRelatedMaterial')" />:</label>
				</td>
				<td>
					<textarea id="textTitleOfRelatedMaterial" onchange="relationsLinkToYourHolndingsGuideTitleChanged($(this).parent().parent().parent().parent());"></textarea>
				</td>
				<td class="labelLeft">
					<label for="selectTitleOfRelatedMaterialLang"><s:property value="getText('eag2012.commons.selectLanguage')" />:</label>
				</td>
				<td>
					<select id="selectTitleOfRelatedMaterialLang" onchange="selectTitleRelatedLangChange($(this).parent().parent().parent().parent());">
						<s:iterator value="languageList" var="language"> 
							<option value="<s:property value="#language.key" />" ><s:property value="#language.value" /></option>
						</s:iterator>
					</select>
				</td>
			</tr>
	
			<tr id="trRelationsDescriptionOfRelation">
				<td id="tdDescriptionOfRelation">
					<label for="tdDescriptionOfRelation"><s:property value="getText('eag2012.relations.descriptionOfRelation')" />:</label>
				</td>
				<td>
					<textarea id="textDescriptionOfRelation"></textarea>
				</td>
				<td id="tdLanguageDescriptionOfRelation" class="labelLeft">
					<label for="selectLanguageDescriptionOfRelation"><s:property value="getText('eag2012.commons.selectLanguage')" />:</label>
				</td>
				<td>
					<select id="selectLanguageDescriptionOfRelation" >
						<s:iterator value="languageList" var="language"> 
							<option value="<s:property value="#language.key"/>" ><s:property value="#language.value" /></option>
						</s:iterator>
					</select>
				</td>
			</tr>
		</table>
	</s:else>
 
	<table id="addResourceRelationTable" class="tablePadding">
		<tr>
			<td id="tdAddNewResourceRelation" colspan="2">
				<input type="button" id="buttonRelationAddNewResourceRelation" onclick="relationAddNewResourceRelation('<s:property value="getText('eag2012.commons.pleaseFillData')" />');" value="<s:property value='getText("eag2012.commons.addNewResourceRelation")' />"/>
			</td>
			<td colspan="2">
			</td>
		</tr>
	</table>

	<s:if test="%{loader.relationsEagRelationType.size() > 0}">
	  <s:iterator var="current" value="loader.relationsNumberOfEagRelations" status="status">
		<table id="institutionRelationTable_<s:property value="%{#status.index + 1}"/>" class="tablePadding">
			<tr>
				<td id="institutionLabel" colspan="4">
					<label for="institutionLabel"><s:property value="getText('eag2012.relations.descriptionOfRelation')" />:</label>
				</td>
				
			</tr>
	
			<tr>
				<td id="tdWebsiteOfDescription">
					<label for="textWebsiteOfDescription"><s:property value="getText('eag2012.relations.websiteOfDescription')" />:</label>
				</td>
				<td>
					<textarea id="textWebsiteOfDescription"><s:property value="loader.relationsEagRelationHref[#status.index]" /></textarea>
				</td>
				<td id="tdInstitutionTypeOfRelation" class="labelLeft">
					<label for="selectTypeOftheRelation"><s:property value="getText('eag2012.relations.institutionTypeOfRelation')" />:</label>
				</td>
				<td>
					<select id="selectTypeOftheRelation" >
						<s:iterator value="typeTheRelationList" var="relation"> 
							<option value="<s:property value="#relation.key" />" <s:if test="loader. relationsEagRelationType[#status.index] == #relation.key">selected="selected"</s:if>><s:property value="#relation.value" /></option>
						</s:iterator>
					</select>
				</td>
				<td colspan="2">
				</td>
				
			</tr>
	
			<tr>
				<td id="tdTitleOfRelatedInstitution">
					<label for="textTitleOfRelatedInstitution"><s:property value="getText('eag2012.relations.titleOfRelatedInstitution')" />:</label>
				</td>
				<td>
					<textarea id="textTitleOfRelatedInstitution"><s:property value="loader.relationsEagRelationEntry[#status.index]" /></textarea>
				</td>
				<td class="labelLeft">
					<label for="selectTitleOfRelatedInstitutionLang"><s:property value="getText('eag2012.commons.selectLanguage')" />:</label>
				</td>
				<td>
					<select id="selectTitleOfRelatedInstitutionLang" >
						<s:iterator value="languageList" var="language"> 
							<option value="<s:property value="#language.key" />"<s:if test="%{#language.key == loader.relationsEagRelationEntryLang[#status.index]}" > selected=selected </s:if>><s:property value="#language.value" /></option>
						</s:iterator>
					</select>
				</td>
			</tr>
	
			<tr id="trRelationsInstitutionDescriptionOfRelation">
				<td id="tdInstitutionDescriptionOfRelation">
					<label for="textInstitutionDescriptionOfRelation"><s:property value="getText('eag2012.relations.descriptionOfRelation')" />:</label>
				</td>
				<td>
					<textarea id="textInstitutionDescriptionOfRelation"><s:property value="loader.relationsEagRelationEntryDescription[#status.index]" /></textarea>
				</td>
				<td id="tdLanguageInstitutionDescriptionOfRelation" class="labelLeft">
					<label for="selectLanguageInstitutionDescriptionOfRelation"><s:property value="getText('eag2012.commons.selectLanguage')" />:</label>
				</td>
				<td>
					<select id="selectLanguageInstitutionDescriptionOfRelation" >
						<s:iterator value="languageList" var="language"> 
							<option value="<s:property value="#language.key" />"<s:if test="%{#language.key == loader.relationsEagRelationEntryDescriptionLang[#status.index]}" > selected=selected </s:if>><s:property value="#language.value" /></option>
						</s:iterator>
					</select>
				</td>
			</tr>
		</table>
	 </s:iterator>
	</s:if>
	<s:else>
	   <table id="institutionRelationTable_1" class="tablePadding">
			<tr>
				<td id="institutionLabel" colspan="4">
					<label for="institutionLabel"><s:property value="getText('eag2012.relations.descriptionOfRelation')" />:</label>
				</td>
				
			</tr>
	
			<tr>
				<td id="tdWebsiteOfDescription">
					<label for="textWebsiteOfDescription"><s:property value="getText('eag2012.relations.websiteOfDescription')" />:</label>
				</td>
				<td>
					<textarea id="textWebsiteOfDescription"></textarea>
				</td>
				<td id="tdInstitutionTypeOfRelation" class="labelLeft">
					<label for="selectTypeOftheRelation"><s:property value="getText('eag2012.relations.institutionTypeOfRelation')" />:</label>
				</td>
				<td>
					<select id="selectTypeOftheRelation" >
						<s:iterator value="typeTheRelationList" var="relation"> 
							<option value="<s:property value="#relation.key" />"><s:property value="#relation.value" /></option>
						</s:iterator>
					</select>
				</td>
				<td colspan="2">
				</td>
				
			</tr>
	
			<tr>
				<td id="tdTitleOfRelatedInstitution">
					<label for="textTitleOfRelatedInstitution"><s:property value="getText('eag2012.relations.titleOfRelatedInstitution')" />:</label>
				</td>
				<td>
					<textarea id="textTitleOfRelatedInstitution"></textarea>
				</td>
				<td class="labelLeft">
					<label for="selectTitleOfRelatedInstitutionLang"><s:property value="getText('eag2012.commons.selectLanguage')" />:</label>
				</td>
				<td>
					<select id="selectTitleOfRelatedInstitutionLang">
						<s:iterator value="languageList" var="language"> 
							<option value="<s:property value="#language.key" />" ><s:property value="#language.value" /></option>
						</s:iterator>
					</select>
				</td>
			</tr>
	
			<tr id="trRelationsInstitutionDescriptionOfRelation">
				<td id="tdInstitutionDescriptionOfRelation">
					<label for="textInstitutionDescriptionOfRelation"><s:property value="getText('eag2012.relations.descriptionOfRelation')" />:</label>
				</td>
				<td>
					<textarea id="textInstitutionDescriptionOfRelation"></textarea>
				</td>
				<td id="tdLanguageInstitutionDescriptionOfRelation" class="labelLeft">
					<label for="selectLanguageInstitutionDescriptionOfRelation"><s:property value="getText('eag2012.commons.selectLanguage')" />:</label>
				</td>
				<td>
					<select id="selectLanguageInstitutionDescriptionOfRelation" >
						<s:iterator value="languageList" var="language"> 
							<option value="<s:property value="#language.key" />"><s:property value="#language.value" /></option>
						</s:iterator>
					</select>
				</td>
			</tr>
		</table>
	</s:else>
	<table id="relationsOtherTable" class="tablePadding">
		<tr>
			<td id="tdAddNewInstitutionRelation" colspan="2">
				<input type="button" id="buttonAddNewInstitutionRelation" onclick="relationAddNewInstitutionRelation('<s:property value="getText('eag2012.commons.pleaseFillData')" />');" value="<s:property value='getText("eag2012.relations.addNewInstitutionRelation")' />"/>
			</td>
			<td colspan="2">
			</td>
		</tr>

		<tr>
			<td id="tdButtonsDescriptionTab" colspan="4">
			    <input type="button" id="buttonRelationsTabPrevious" value="<s:property value='getText("eag2012.commons.previousTab")' />" class="rightButton" onclick="checkAndShowPreviousTab($(this).parent().parent().parent().parent(),'<s:property value="getText('eag2012.errors.fieldRequired')" />','<s:property value="getText('eag2012.commons.pleaseFillMandatoryFields')" />','<s:property value="getText('eag2012.commons.eagwithurlwarnings')"/>','<s:property value="getText('eag2012.commons.eagwithurlwarnings')"/>');" />
			</td>
		</tr>

	</table>
</div>