<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="/struts-tags" prefix="s" %>
<div id="headerContainer">
</div>

<div id="descriptionTabContent">
    <h2 class="tablePadding"><s:property value="cpfTypeDescriptionText" /></h2>
    <table id="placeTable_1" class="tablePadding">
        <tr>
            <th class="sectionHeader" colspan="4"><s:property value="getText('eaccpf.description.place')" /></th>
        </tr>
        <tr>
            <td><label for="place"><s:property value="getText('eaccpf.description.place.name')" /></label></td>
            <td><input type="text" id="place" name="place_1" oninput="togglePlaceFields($(this));" /></td>
            <td><label for="placeLanguage"><s:property value="getText('eaccpf.description.selectlanguage')" /></label></td>
            <td>
                <select id="placeLanguage" name="placeLanguage_1" >
                    <s:iterator value="languages" var="language">
                        <option value='<s:property value="#language.key"/>'<s:if test='%{#language.key == defaultLanguage}'>selected="selected"</s:if>><s:property value="#language.value"/></option>
                    </s:iterator>
                </select>
            </td>
        </tr>
        <tr>
            <td><label for="linkPlaceVocab"><s:property value="getText('eaccpf.description.linkvocabularyplaces')" /></label></td>
            <td><input type="text" id="linkPlaceVocab" name="linkPlaceVocab_1" disabled="disabled" /></td>
            <td colspan="2"></td>
        </tr>
        <tr>
            <td><label for="placeCountry"><s:property value="getText('eaccpf.description.country')" /></label></td>
            <td>
                <select id="placeCountry" name="placeCountry_1" disabled="disabled">
                    <s:iterator value="countryList" var="list">
                        <option value='<s:property value="#list.key"/>'><s:property value="#list.value"/></option>
                    </s:iterator>
                </select>
            </td>
            <td colspan="2"></td>
        </tr>
        <tr id="trAddressComponent_1">
            <td><label for="addressDetails"><s:property value="getText('eaccpf.description.addressdetails')" /></label></td>
            <td><input type="text" id="addressDetails" name="placeTable_1_addressDetails_1"  disabled="disabled"/></td>
            <td><label for="addressComponent"><s:property value="getText('eaccpf.description.component')" /></label></td>
            <td>
                <select id="addressComponent" name="placeTable_1_addressComponent_1" disabled="disabled">
                    <s:iterator value="addressComponentTypeList" var="list">
                        <option value='<s:property value="#list.key"/>'><s:property value="#list.value"/></option>
                    </s:iterator>
                </select>
            </td>
        </tr>
        <tr>
            <td><input type="button" value="<s:property value="getText('eaccpf.description.button.addaddressdetails')" />" id="addAddressComponentButton" onclick="addAddressComponent($(this).parent().parent().parent().parent().attr('id'), '<s:property value="getText('eaccpf.commons.error.empty.place')" />');" disabled="disabled" /></td>
            <td colspan="3"></td>
        </tr>
        <tr id="trPlaceRole">
            <td><label for="placeRole_1"><s:property value="getText('eaccpf.description.roleplace')" /></label></td>
            <td>
                <select id="placeRole_1" name="placeRole_1" disabled="disabled">
                    <s:iterator value="placeEntryList" var="list">
                        <option value='<s:property value="#list.key"/>'><s:property value="#list.value"/></option>
                    </s:iterator>
                </select>
            </td>
            <td colspan="2"></td>
        </tr>
        <tr>
            <td><label><s:property value="getText('eaccpf.description.adddatesofusefortheplace')" /></label></td>
            <td><input type="button" value="<s:property value="getText('eaccpf.commons.add.single.date')" />" id="addPlaceDate" onclick="addDateOrDateRangePlace(this.id, $(this).parent().parent().parent().parent().attr('id'), '<s:property value="getText('eaccpf.commons.date')" />', '<s:property value="getText('eaccpf.commons.from.date')" />', '<s:property value="getText('eaccpf.commons.to.date')" />', '<s:property value="getText('eaccpf.commons.date.type')" />', '<s:property value="getText('eaccpf.commons.date.known')" />', '<s:property value="getText('eaccpf.commons.unknown.date')" />', '<s:property value="openLabel" />', '<s:property value="getText('eaccpf.commons.iso.date')" />', '<s:property value="getText('eaccpf.commons.error.no.standard.date')" />', '<s:property value="getText('eaccpf.commons.error.no.standard.dateRange')" />')" disabled="disabled"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                <input type="button" value="<s:property value="getText('eaccpf.commons.add.range.date')" />" id="addPlaceDateRange" onclick="addDateOrDateRangePlace(this.id, $(this).parent().parent().parent().parent().attr('id'), '<s:property value="getText('eaccpf.commons.date')" />', '<s:property value="getText('eaccpf.commons.from.date')" />', '<s:property value="getText('eaccpf.commons.to.date')" />', '<s:property value="getText('eaccpf.commons.date.type')" />', '<s:property value="getText('eaccpf.commons.date.known')" />', '<s:property value="getText('eaccpf.commons.unknown.date')" />', '<s:property value="openLabel" />', '<s:property value="getText('eaccpf.commons.iso.date')" />', '<s:property value="getText('eaccpf.commons.error.no.standard.date')" />', '<s:property value="getText('eaccpf.commons.error.no.standard.dateRange')" />')" disabled="disabled"/></td>
                <s:if test="%{#current.dates.size() > 0}">
                <td><input type="hidden" id="placeTable_1_rows" name="placeTable_1_rows" value="<s:property value="#current.dates.size()" />" /></td>
                </s:if>
                <s:else>
                <td><input type="hidden" id="placeTable_1_rows" name="placeTable_1_rows" value="0" /></td>
                </s:else>
            <td></td>
        </tr>
    </table>
    <table id="addPlaceButtonPanel" class="tablePadding">
        <tr>
            <td><input type="button" value="<s:property value="getText('eaccpf.description.button.addFurtherPlace')" />" id="addPlaceButton" onclick="addPlace('<s:property value="defaultLanguage" />', '<s:property value="getText('eaccpf.commons.error.empty.place')" />');" /></td>
        </tr>
    </table>
    <table id="functionTable_1" class="tablePadding">
        <tr>
            <th class="sectionHeader" colspan="4"><s:property value="getText('eaccpf.description.function')" /></th>
        </tr>
        <tr>
            <td><label for="function"><s:property value="getText('eaccpf.description.function')" /></label></td>
            <td><input type="text" id="function" name="function_1" /></td>
            <td><label for="functionLanguage"><s:property value="getText('eaccpf.description.selectlanguage')" /></label></td>
            <td>
                <select id="functionLanguage" name="functionLanguage_1" >
                    <s:iterator value="languages" var="language">
                        <option value='<s:property value="#language.key"/>'<s:if test='%{#language.key == defaultLanguage}'>selected="selected"</s:if>><s:property value="#language.value"/></option>
                    </s:iterator>
                </select>
            </td>
        </tr>
        <tr>
            <td><label for="linkFunctionVocab"><s:property value="getText('eaccpf.description.linktovocabulary')" /></label></td>
            <td><input type="text" id="linkFunctionVocab" name="linkFunctionVocab_1" /></td>
            <td colspan="2"></td>
        </tr>
        <tr>
            <td><label for="functionDescription"><s:property value="getText('eaccpf.description.descriptionfunction')" /></label></td>
            <td colspan="3"><textarea id="functionDescription" name="functionDescription_1"></textarea></td>
        </tr>
        <tr id="trPlaceFunction_1">
            <td><label for="textPlaceFunction"><s:property value="getText('eaccpf.description.place.name')" /></label></td>
            <td><input type="text" id="textPlaceFunction" name="functionTable_1_place_1" /></td>
            <td><label for="functionCountry"><s:property value="getText('eaccpf.description.country')" /></label></td>
            <td>
                <select id="functionCountry"  name="functionTable_1_country_1" >
                    <s:iterator value="countryList" var="list">
                        <option value='<s:property value="#list.key"/>'><s:property value="#list.value"/></option>
                    </s:iterator>
                </select>
            </td>
        </tr>
        <tr id="trPlaceFunctionButton">
            <td><input type="button" value="<s:property value="getText('eaccpf.description.button.addFurtherPlaceFunction')" />" id="addPlaceFunctionButton" onclick="addPlaceFunction($(this).parent().parent().parent().parent().attr('id'), '<s:property value="getText('eaccpf.commons.error.empty.place')" />');" /></td>
            <td colspan="3"></td>
        <tr>
            <td><label><s:property value="getText('eaccpf.description.adddatesofuseforthefunction')" /></label></td>
            <td><input type="button" value="<s:property value="getText('eaccpf.commons.add.single.date')" />" id="addFunctionDate" onclick="addDateOrDateRangeFunction(this.id, $(this).parent().parent().parent().parent().attr('id'), '<s:property value="getText('eaccpf.commons.date')" />', '<s:property value="getText('eaccpf.commons.from.date')" />', '<s:property value="getText('eaccpf.commons.to.date')" />', '<s:property value="getText('eaccpf.commons.date.type')" />', '<s:property value="getText('eaccpf.commons.date.known')" />', '<s:property value="getText('eaccpf.commons.unknown.date')" />', '<s:property value="openLabel" />', '<s:property value="getText('eaccpf.commons.iso.date')" />', '<s:property value="getText('eaccpf.commons.error.no.standard.date')" />', '<s:property value="getText('eaccpf.commons.error.no.standard.dateRange')" />')" />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                <input type="button" value="<s:property value="getText('eaccpf.commons.add.range.date')" />" id="addFunctionDateRange" onclick="addDateOrDateRangeFunction(this.id, $(this).parent().parent().parent().parent().attr('id'), '<s:property value="getText('eaccpf.commons.date')" />', '<s:property value="getText('eaccpf.commons.from.date')" />', '<s:property value="getText('eaccpf.commons.to.date')" />', '<s:property value="getText('eaccpf.commons.date.type')" />', '<s:property value="getText('eaccpf.commons.date.known')" />', '<s:property value="getText('eaccpf.commons.unknown.date')" />', '<s:property value="openLabel" />', '<s:property value="getText('eaccpf.commons.iso.date')" />', '<s:property value="getText('eaccpf.commons.error.no.standard.date')" />', '<s:property value="getText('eaccpf.commons.error.no.standard.dateRange')" />')" /></td>
            <td><input type="hidden" id="functionTable_1_rows" name="functionTable_1_rows" value="0" /></td>
            <td></td>
        </tr>
    </table>
    <table id="addFunctionButtonPanel" class="tablePadding">
        <tr>
            <td><input type="button" value="<s:property value="getText('eaccpf.description.button.addFurtherFunction')" />" id="addFunctionButton" onclick="addFunction('<s:property value="defaultLanguage" />', '<s:property value="getText('eaccpf.commons.error.empty.function')" />');" /></td>
        </tr>
    </table>
    <table id="occupationTable_1" class="tablePadding">
        <tr>
            <th class="sectionHeader" colspan="4"><s:property value="getText('eaccpf.description.ocupation')" /></th>
        </tr>
        <tr>
            <td><label for="occupation"><s:property value="getText('eaccpf.description.ocupation')" /></label></td>
            <td><input type="text" id="occupation" name="occupation_1" /></td>
            <td><label for="occupationLanguage"><s:property value="getText('eaccpf.description.selectlanguage')" /></label></td>
            <td>
                <select id="occupationLanguage" name="occupationLanguage_1" >
                    <s:iterator value="languages" var="language">
                        <option value='<s:property value="#language.key"/>'<s:if test='%{#language.key == defaultLanguage}'>selected="selected"</s:if>><s:property value="#language.value"/></option>
                    </s:iterator>
                </select>
            </td>
        </tr>
        <tr>
            <td><label for="linkOccupationVocab"><s:property value="getText('eaccpf.description.linktocontrolledocupations')" /></label></td>
            <td><input type="text" id="linkOccupationVocab" name="linkOccupationVocab_1" /></td>
            <td colspan="2"></td>
        </tr>
        <tr>
            <td><label for="occupationDescription"><s:property value="getText('eaccpf.description.descriptionocupations')" /></label></td>
            <td colspan="3"><textarea id="occupationDescription" name="occupationDescription_1"></textarea></td>
        </tr>
        <tr id="trPlaceOccupation_1">
            <td><label for="textPlaceOccupation"><s:property value="getText('eaccpf.description.place.name')" /></label></td>
            <td><input type="text" id="textPlaceOccupation" name="occupationTable_1_place_1" /></td>
            <td><label for="occupationCountry"><s:property value="getText('eaccpf.description.country')" /></label></td>
            <td>
                <select id="occupationCountry" name="occupationTable_1_country_1" >
                    <s:iterator value="countryList" var="list">
                        <option value='<s:property value="#list.key"/>'><s:property value="#list.value"/></option>
                    </s:iterator>
                </select>
            </td>
        </tr>
        <tr id="trPlaceOccupationButton">
            <td><input type="button" value="<s:property value="getText('eaccpf.description.button.addFurtherPlaceOcupation')" />" id="addPlaceOccupationButton" onclick="addPlaceOccupation($(this).parent().parent().parent().parent().attr('id'), '<s:property value="getText('eaccpf.commons.error.empty.place')" />');" /></td>
            <td colspan="3"></td>
        </tr>
        <tr>
            <td><label><s:property value="getText('eaccpf.description.adddatesofusefortheoccupation')" /></label></td>
            <td><input type="button" value="<s:property value="getText('eaccpf.commons.add.single.date')" />" id="addOccupationDate" onclick="addDateOrDateRangeOccupation(this.id, $(this).parent().parent().parent().parent().attr('id'), '<s:property value="getText('eaccpf.commons.date')" />', '<s:property value="getText('eaccpf.commons.from.date')" />', '<s:property value="getText('eaccpf.commons.to.date')" />', '<s:property value="getText('eaccpf.commons.date.type')" />', '<s:property value="getText('eaccpf.commons.date.known')" />', '<s:property value="getText('eaccpf.commons.unknown.date')" />', '<s:property value="openLabel" />', '<s:property value="getText('eaccpf.commons.iso.date')" />', '<s:property value="getText('eaccpf.commons.error.no.standard.date')" />', '<s:property value="getText('eaccpf.commons.error.no.standard.dateRange')" />')" />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                <input type="button" value="<s:property value="getText('eaccpf.commons.add.range.date')" />" id="addOccupationDateRange" onclick="addDateOrDateRangeOccupation(this.id, $(this).parent().parent().parent().parent().attr('id'), '<s:property value="getText('eaccpf.commons.date')" />', '<s:property value="getText('eaccpf.commons.from.date')" />', '<s:property value="getText('eaccpf.commons.to.date')" />', '<s:property value="getText('eaccpf.commons.date.type')" />', '<s:property value="getText('eaccpf.commons.date.known')" />', '<s:property value="getText('eaccpf.commons.unknown.date')" />', '<s:property value="openLabel" />', '<s:property value="getText('eaccpf.commons.iso.date')" />', '<s:property value="getText('eaccpf.commons.error.no.standard.date')" />', '<s:property value="getText('eaccpf.commons.error.no.standard.dateRange')" />')" /></td>
            <td><input type="hidden" id="occupationTable_1_rows" name="occupationTable_1_rows" value="0" /></td>
            <td></td>
        </tr>
    </table>
    <table id="addOccupationButtonPanel" class="tablePadding">
        <tr>
            <td><input type="button" value="<s:property value="getText('eaccpf.description.button.addFurtherOcupation')" />" id="addOccupationButton" onclick="addOccupation('<s:property value="defaultLanguage" />', '<s:property value="getText('eaccpf.commons.error.empty.occupation')" />');" /></td>
        </tr>
    </table>
    <table class="tablePadding">
        <tr>
            <th class="sectionHeader"><s:property value="getText('eaccpf.description.genealogy')" /></th>
        </tr>
    </table>
    <table id="genealogyContent_1" class="tablePadding">
        <tr>
            <td><label for="genealogyDescription"><s:property value="getText('eaccpf.description.description')" /></label></td>
            <td colspan="3"><textarea id="genealogyDescription" name="genealogyDescription_1"></textarea></td>
        </tr>
        <tr>
            <td><label for="genealogyLanguage"><s:property value="getText('eaccpf.description.selectlanguage')" /></label></td>
            <td>
                <select id="genealogyLanguage" name="genealogyLanguage_1" >
                    <s:iterator value="languages" var="language">
                        <option value='<s:property value="#language.key"/>'<s:if test='%{#language.key == defaultLanguage}'>selected="selected"</s:if>><s:property value="#language.value"/></option>
                    </s:iterator>
                </select>
            </td>
            <td></td>
            <td></td>
        </tr>
    </table>
    <table id="addGenealogyButtonPanel" class="tablePadding">
        <tr>
            <td><input type="button" value="<s:property value="getText('eaccpf.description.button.addfurthergenealogydescription')" />" id="addGenealogyButton" onclick="addGenealogy('<s:property value="getText('eaccpf.commons.error.empty.genealogy')" />');" /></td>
        </tr>
    </table>
    <table class="tablePadding">
        <tr>
            <th class="sectionHeader"><s:property value="getText('eaccpf.description.biography')" /></th>
        </tr>
    </table>
    <table id="biographyContent_1" class="tablePadding">
        <tr>
            <td><label for="biographyDescription"><s:property value="getText('eaccpf.description.description')" /></label></td>
            <td colspan="3"><textarea id="biographyDescription" name="biographyDescription_1"></textarea></td>
        </tr>
        <tr>
            <td><label for="biographyLanguage"><s:property value="getText('eaccpf.description.selectlanguage')" /></label></td>
            <td>
                <select id="biographyLanguage" name="biographyLanguage_1" >
                    <s:iterator value="languages" var="language">
                        <option value='<s:property value="#language.key"/>'<s:if test='%{#language.key == defaultLanguage}'>selected="selected"</s:if>><s:property value="#language.value"/></option>
                    </s:iterator>
                </select>
            </td>
            <td></td>
            <td></td>
        </tr>
    </table>
    <table id="addBiographyButtonPanel" class="tablePadding">
        <tr>
            <td><input type="button" value="<s:property value="getText('eaccpf.description.button.addfurtherbiographydescription')" />" id="addBiographyButton" onclick="addBiography('<s:property value="getText('eaccpf.commons.error.empty.biohist')" />');" /></td>
        </tr>
    </table>
</div>