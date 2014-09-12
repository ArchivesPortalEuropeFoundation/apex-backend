<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="/struts-tags" prefix="s" %>
<div id="headerContainer">
</div>

<div id="relationsTabContent">
    <table id="cpfRelationsTable_1" class="tablePadding">
        <tr>
            <th class="sectionHeader" colspan="4"><s:property value="getText('eaccpf.relations.cpf.relation')" /></th>
        </tr>
        <tr>
            <td><label><s:property value="getText('eaccpf.relations.cpf.relation.name')" />:</label></td>
            <td><input type="text" id="textCpfRelationName" name="textCpfRelationName_1" /></td>
            <td><label><s:property value="getText('eaccpf.commons.select.language')" />:</label></td>
            <td>
                <select id="cpfRelationLanguage" name="cpfRelationLanguage_1" onchange="">
                    <s:iterator value="languages" var="language">
                        <option value='<s:property value="#language.key"/>'<s:if test='%{#language.key == defaultLanguage}'>selected="selected"</s:if>><s:property value="#language.value"/></option>
                    </s:iterator>
                </select>
            </td>
        </tr>
        <tr>
            <td><label><s:property value="getText('eaccpf.relations.cpf.relation.identifier')" />:</label></td>
            <td><input type="text" id="textCpfRelationId" name="textCpfRelationId_1" /></td>
            <td colspan="2"></td>
        </tr>
        <tr>
            <td><label><s:property value="getText('eaccpf.relations.link')" />:</label></td>
            <td><input type="text" id="textCpfRelationLink" name="textCpfRelationLink_1" /></td>
            <td><label><b><s:property value="getText('eaccpf.relations.relation.type')" />*</b>:</label></td>
            <td><select id="cpfRelationType" name="cpfRelationType_1" onchange="">
                    <s:iterator value="cpfRelationTypeList" var="relationType">
                        <option value='<s:property value="#relationType.key"/>'><s:property value="#relationType.value"/></option>
                    </s:iterator>
                </select>
            </td>
        </tr>
        <tr>
            <td colspan="4"><s:property value="getText('eaccpf.relations.cpf.relation.organisation')" /></td>
        </tr>
        <tr id="trCpfRelationRespOrg_1">
            <td><label><s:property value="getText('eaccpf.relations.name')" />:</label></td>
            <td><input type="text" id="textCpfRelRespOrgPerson" name="cpfRelationsTable_1_textCpfRelRespOrgPerson_1" /></td>
            <td><label><s:property value="getText('eaccpf.relations.identifier')" />:</label></td>
            <td><input type="text" id="textCpfRelRespOrgId" name="cpfRelationsTable_1_textCpfRelRespOrgId_1" /></td>
        </tr>
        <tr>
            <td><label><s:property value="getText('eaccpf.relations.description')" />:</label></td>
            <td colspan="3"><textarea id="textareaCpfRelationDescription" name="textareaCpfRelationDescription_1"></textarea></td>
        </tr>
    </table>
    <table class="tablePadding">
        <tr>
            <td><input type="button" value="<s:property value="getText('eaccpf.relations.add.further.cpf')" />" id="addCpfRelationButton" onClick="addCpfRelation('<s:property value="defaultLanguage" />', '<s:property value="getText('eaccpf.relations.error.popup')" />');" /></td>
        </tr>
    </table>
    <table id="resRelationsTable_1" class="tablePadding">
        <tr>
            <th class="sectionHeader" colspan="4"><s:property value="getText('eaccpf.relations.resources.relation')" /></th>
        </tr>
        <tr>
            <td><label><s:property value="getText('eaccpf.relations.resource.relation.name')" />:</label></td>
            <td><input type="text" id="textResRelationName" name="textResRelationName_1" /></td>
            <td><label><s:property value="getText('eaccpf.commons.select.language')" />:</label></td>
            <td>
                <select id="resRelationLanguage" name="resRelationLanguage_1" onchange="">
                    <s:iterator value="languages" var="language">
                        <option value='<s:property value="#language.key"/>'<s:if test='%{#language.key == defaultLanguage}'>selected="selected"</s:if>><s:property value="#language.value"/></option>
                    </s:iterator>
                </select>
            </td>
        </tr>
        <tr>
            <td><label><s:property value="getText('eaccpf.relations.resource.relation.identifier')" />:</label></td>
            <td><input type="text" id="textResRelationId" name="textResRelationId_1" /></td>
            <td colspan="2"></td>
        </tr>
        <tr>
            <td><label><s:property value="getText('eaccpf.relations.link')" />:</label></td>
            <td><input type="text" id="textResRelationLink" name="textResRelationLink_1" /></td>
            <td><label><b><s:property value="getText('eaccpf.relations.relation.type')" />*</b>:</label></td>
            <td><select id="resRelationType" name="resRelationType_1" onchange="">
                    <s:iterator value="resRelationTypeList" var="relationType">
                        <option value='<s:property value="#relationType.key"/>'><s:property value="#relationType.value"/></option>
                    </s:iterator>
                </select>
            </td>
        </tr>
        <tr>
            <td colspan="4"><s:property value="getText('eaccpf.relations.resource.relation.organisation')" /></td>
        </tr>
        <tr id="trResRelationRespOrg_1">
            <td><label><s:property value="getText('eaccpf.relations.name')" />:</label></td>
            <td><input type="text" id="textResRelRespOrgPerson" name="resRelationsTable_1_textResRelRespOrgPerson_1" /></td>
            <td><label><s:property value="getText('eaccpf.relations.identifier')" />:</label></td>
            <td><input type="text" id="textResRelRespOrgId" name="resRelationsTable_1_textResRelRespOrgId_1" /></td>
        </tr>
        <tr>
            <td><label><s:property value="getText('eaccpf.relations.description')" />:</label></td>
            <td colspan="3"><textarea id="textareaResRelationDescription" name="textareaResRelationDescription_1"></textarea></td>
        </tr>
    </table>
    <table class="tablePadding">
        <tr>
            <td><input type="button" value="<s:property value="getText('eaccpf.relations.add.further.resource')" />" id="addResRelationButton" onClick="addResRelation('<s:property value="defaultLanguage" />', '<s:property value="getText('eaccpf.relations.error.popup')" />');" /></td>
        </tr>
    </table>
    <table id="fncRelationsTable_1" class="tablePadding">
        <tr>
            <th class="sectionHeader" colspan="4"><s:property value="getText('eaccpf.relations.functions.relation')" /></th>
        </tr>
        <tr>
            <td><label><s:property value="getText('eaccpf.relations.function.relation.name')" />:</label></td>
            <td><input type="text" id="textFncRelationName" name="textFncRelationName_1" /></td>
            <td><label><s:property value="getText('eaccpf.commons.select.language')" />:</label></td>
            <td>
                <select id="fncRelationLanguage" name="fncRelationLanguage_1" onchange="">
                    <s:iterator value="languages" var="language">
                        <option value='<s:property value="#language.key"/>'<s:if test='%{#language.key == defaultLanguage}'>selected="selected"</s:if>><s:property value="#language.value"/></option>
                    </s:iterator>
                </select>
            </td>
        </tr>
        <tr>
            <td><label><s:property value="getText('eaccpf.relations.function.relation.identifier')" />:</label></td>
            <td><input type="text" id="textFncRelationId" name="textFncRelationId_1" /></td>
            <td colspan="2"></td>
        </tr>
        <tr>
            <td><label><s:property value="getText('eaccpf.relations.link')" />:</label></td>
            <td><input type="text" id="textFncRelationLink" name="textFncRelationLink_1" /></td>
            <td><label><b><s:property value="getText('eaccpf.relations.relation.type')" />*</b>:</label></td>
            <td><select id="fncRelationType" name="fncRelationType_1" onchange="">
                    <s:iterator value="fncRelationTypeList" var="relationType">
                        <option value='<s:property value="#relationType.key"/>'><s:property value="#relationType.value"/></option>
                    </s:iterator>
                </select>
            </td>
        </tr>
        <tr>
            <td colspan="4"><s:property value="getText('eaccpf.relations.function.relation.organisation')" /></td>
        </tr>
        <tr id="trFncRelationRespOrg_1">
            <td><label><s:property value="getText('eaccpf.relations.name')" />:</label></td>
            <td><input type="text" id="textFncRelRespOrgPerson" name="fncRelationsTable_1_textFncRelRespOrgPerson_1" /></td>
            <td><label><s:property value="getText('eaccpf.relations.identifier')" />:</label></td>
            <td><input type="text" id="textFncRelRespOrgId" name="fncRelationsTable_1_textFncRelRespOrgId_1" /></td>
        </tr>
        <tr>
            <td><label><s:property value="getText('eaccpf.relations.description')" />:</label></td>
            <td colspan="3"><textarea id="textareaFncRelationDescription" name="textareaFncRelationDescription_1"></textarea></td>
        </tr>
    </table>
    <table class="tablePadding">
        <tr>
            <td><input type="button" value="<s:property value="getText('eaccpf.relations.add.further.function')" />" id="addFncRelationButton" onClick="addFncRelation('<s:property value="defaultLanguage" />', '<s:property value="getText('eaccpf.relations.error.popup')" />');" /></td>
        </tr>
    </table>
</div>
