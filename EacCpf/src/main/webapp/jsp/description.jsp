<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="/struts-tags" prefix="s" %>
<div id="headerContainer">
</div>

<div id="descriptionTabContent">
    <table id="placeTable_1" class="tablePadding">
        <tr>
            <th id="thPlaceTableHeader" colspan="4">Place 1</th>
        </tr>
        <tr>
            <td>Place</td>
            <td><input type="text" id="place" name="place_1" /></td>
            <td>Select a language</td>
            <td>
                <select id="placeLanguage" name="placeLanguage_1" onchange="">
                    <s:iterator value="languages" var="language">
                        <option value='<s:property value="#language.key"/>'<s:if test='%{#language.key == defaultLanguage}'>selected="selected"</s:if>><s:property value="#language.value"/></option>
                    </s:iterator>
                </select>
            </td>
        </tr>
        <tr>
            <td>Link to controlled vocabulary used for places</td>
            <td><input type="text" id="linkPlaceVocab" name="linkPlaceVocab_1" /></td>
            <td colspan="2"></td>
        </tr>
        <tr>
            <td>Country</td>
            <td>
                <select id="placeCountry" name="placeCountry_1" onchange="">
                    <s:iterator value="countryList" var="list">
                        <option value='<s:property value="#list.key"/>'><s:property value="#list.value"/></option>
                    </s:iterator>
                </select>
            </td>
            <td colspan="2"></td>
        </tr>
        <tr id="trAddressComponent_1">
            <td>Address details</td>
            <td><input type="text" id="addressDetails" name="placeTable_1_addressDetails_1" /></td>
            <td>Component</td>
            <td>
                <select id="addressComponent" name="placeTable_1_addressComponent_1" onchange="">
                    <s:iterator value="addressComponentTypeList" var="list">
                        <option value='<s:property value="#list"/>'><s:property value="#list"/></option>
                    </s:iterator>
                </select>
            </td>
        </tr>
        <tr>
            <td><input type="button" value="Add further address details" id="addAddressComponentButton" onclick="addAddressComponent($(this).parent().parent().parent().parent().attr('id'));" /></td>
            <td colspan="3"></td>
        </tr>
        <tr id="trPlaceRole">
            <td>Role of the place</td>
            <td><input type="text" id="placeRole" name="placeRole_1" /></td>
            <td colspan="2"></td>
        </tr>
        <tr>
            <td>Add</td>
            <td>
                <s:iterator value="dateOrDateRange" var="dateType">
                    <input type="radio" name="dateOrDateRangePlace" value='<s:property value="#dateType.key"/>' onclick="addDateOrDateRangePlace($(this).parent().parent().parent().parent().attr('id'))"><s:property value="#dateType.value"/>&nbsp;&nbsp;&nbsp;
                </s:iterator>
            </td>
            <td><input type="hidden" id="placeTable_1_rows" name="placeTable_1_rows" value="0" /></td>
            <td></td>
        </tr>
    </table>
    <table id="addPlaceButtonPanel" class="tablePadding">
        <tr>
            <td><input type="button" value="Add further place" id="addPlaceButton" onclick="addPlace('<s:property value="defaultLanguage" />');" /></td>
        </tr>
    </table>
    <table id="functionTable_1" class="tablePadding">
        <tr>
            <th id="thFunctionTableHeader" colspan="4">Function 1</th>
        </tr>
        <tr>
            <td>Function</td>
            <td><input type="text" id="function" name="function_1" /></td>
            <td>Select a language</td>
            <td>
                <select id="functionLanguage" name="functionLanguage_1" onchange="">
                    <s:iterator value="languages" var="language">
                        <option value='<s:property value="#language.key"/>'<s:if test='%{#language.key == defaultLanguage}'>selected="selected"</s:if>><s:property value="#language.value"/></option>
                    </s:iterator>
                </select>
            </td>
        </tr>
        <tr>
            <td>Link to controlled vocabulary used for functions</td>
            <td><input type="text" id="linkFunctionVocab" name="linkFunctionVocab_1" /></td>
            <td colspan="2"></td>
        </tr>
        <tr>
            <td>Description of the function</td>
            <td colspan="3"><textarea id="functionDescription" name="functionDescription_1"></textarea></td>
        </tr>
        <tr id="trPlaceFunction_1">
            <td>Place</td>
            <td><input type="text" id="textPlaceFunction" name="functionTable_1_place_1" /></td>
            <td>Country</td>
            <td>
                <select id="functionCountry"  name="functionTable_1_country_1" onchange="">
                    <s:iterator value="countryList" var="list">
                        <option value='<s:property value="#list.key"/>'><s:property value="#list.value"/></option>
                    </s:iterator>
                </select>
            </td>
        </tr>
        <tr id="trPlaceFunctionButton">
            <td><input type="button" value="Add further place for function" id="addPlaceFunctionButton" onclick="addPlaceFunction($(this).parent().parent().parent().parent().attr('id'));" /></td>
            <td colspan="3"></td>
        <tr>
            <td>Add</td>
            <td>
                <s:iterator value="dateOrDateRange" var="dateType">
                    <input type="radio" name="dateOrDateRangeFunction" value='<s:property value="#dateType.key"/>' onclick="addDateOrDateRangeFunction($(this).parent().parent().parent().parent().attr('id'));"><s:property value="#dateType.value"/>&nbsp;&nbsp;&nbsp;
                </s:iterator>
            </td>
            <td><input type="hidden" id="functionTable_1_rows" name="functionTable_1_rows" value="0" /></td>
            <td></td>
        </tr>
    </table>
    <table id="addFunctionButtonPanel" class="tablePadding">
        <tr>
            <td><input type="button" value="Add further function" id="addFunctionButton" onclick="addFunction('<s:property value="defaultLanguage" />');" /></td>
        </tr>
    </table>
    <table id="occupationTable_1" class="tablePadding">
        <tr>
            <th id="thOccupationTableHeader" colspan="4">Occupation 1</th>
        </tr>
        <tr>
            <td>Occupation</td>
            <td><input type="text" id="occupation" name="occupation_1" /></td>
            <td>Select a language</td>
            <td>
                <select id="occupationLanguage" name="occupationLanguage_1" onchange="">
                    <s:iterator value="languages" var="language">
                        <option value='<s:property value="#language.key"/>'<s:if test='%{#language.key == defaultLanguage}'>selected="selected"</s:if>><s:property value="#language.value"/></option>
                    </s:iterator>
                </select>
            </td>
        </tr>
        <tr>
            <td>Link to controlled vocabulary used for occupations</td>
            <td><input type="text" id="linkOccupationVocab" name="linkOccupationVocab_1" /></td>
            <td colspan="2"></td>
        </tr>
        <tr>
            <td>Description of the occupation</td>
            <td colspan="3"><textarea id="occupationDescription" name="occupationDescription_1"></textarea></td>
        </tr>
        <tr id="trPlaceOccupation_1">
            <td>Place</td>
            <td><input type="text" id="textPlaceOccupation" name="occupationTable_1_place_1" /></td>
            <td>Country</td>
            <td>
                <select id="occupationCountry" name="occupationTable_1_country_1" onchange="">
                    <s:iterator value="countryList" var="list">
                        <option value='<s:property value="#list.key"/>'><s:property value="#list.value"/></option>
                    </s:iterator>
                </select>
            </td>
        </tr>
        <tr id="trPlaceOccupationButton">
            <td><input type="button" value="Add further place for occupation" id="addPlaceOccupationButton" onclick="addPlaceOccupation($(this).parent().parent().parent().parent().attr('id'));" /></td>
            <td colspan="3"></td>
        </tr>
        <tr>
            <td>Add</td>
            <td>
                <s:iterator value="dateOrDateRange" var="dateType">
                    <input type="radio" name="dateOrDateRangeOccupation" value='<s:property value="#dateType.key"/>' onclick="addDateOrDateRangeOccupation($(this).parent().parent().parent().parent().attr('id'));"><s:property value="#dateType.value"/>&nbsp;&nbsp;&nbsp;
                </s:iterator>
            </td>
            <td><input type="hidden" id="occupationTable_1_rows" name="occupationTable_1_rows" value="0" /></td>
            <td></td>
        </tr>
    </table>
    <table id="addOccupationButtonPanel" class="tablePadding">
        <tr>
            <td><input type="button" value="Add further occupation" id="addOccupationButton" onclick="addOccupation('<s:property value="defaultLanguage" />');" /></td>
        </tr>
    </table>
    <table class="tablePadding">
        <tr>
            <th id="thGenealogyHeader">Genealogy</th>
        </tr>
    </table>
    <table id="genealogyContent_1" class="tablePadding">
        <tr>
            <td>Description</td>
            <td rowspan="3"><textarea id="genealogyDescription" name="genealogyDescription_1"></textarea></td>
            <td>Select a language°</td>
            <td>
                <select id="genealogyLanguage" name="genealogyLanguage_1" onchange="">
                    <s:iterator value="languages" var="language">
                        <option value='<s:property value="#language.key"/>'<s:if test='%{#language.key == defaultLanguage}'>selected="selected"</s:if>><s:property value="#language.value"/></option>
                    </s:iterator>
                </select>
            </td>
        </tr>
    </table>
    <table id="addGenealogyButtonPanel" class="tablePadding">
        <tr>
            <td><input type="button" value="Add further genealogy description" id="addGenealogyButton" onclick="addGenealogy();" /></td>
        </tr>
    </table>
    <table class="tablePadding">
        <tr>
            <th id="thBiographyHeader">Biography</th>
        </tr>
    </table>
    <table id="biographyContent_1" class="tablePadding">
        <tr>
            <td>Description</td>
            <td rowspan="3"><textarea id="biographyDescription" name="biographyDescription_1"></textarea></td>
            <td>Select a language°</td>
            <td>
                <select id="biographyLanguage" name="biographyLanguage_1" onchange="">
                    <s:iterator value="languages" var="language">
                        <option value='<s:property value="#language.key"/>'<s:if test='%{#language.key == defaultLanguage}'>selected="selected"</s:if>><s:property value="#language.value"/></option>
                    </s:iterator>
                </select>
            </td>
        </tr>
    </table>
    <table id="addBiographyButtonPanel" class="tablePadding">
        <tr>
            <td><input type="button" value="Add further biography description" id="addBiographyButton" onclick="addBiography();" /></td>
        </tr>
    </table>
</div>