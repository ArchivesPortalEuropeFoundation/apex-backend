<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="/struts-tags" prefix="s" %>
<div id="headerContainer">
</div>

<div id="descriptionTabContent">
    <s:if test="%{loader.places.size() > 0}">
        <s:iterator var="current" value="loader.places" status="status">
            <table id="placeTable_<s:property value="#status.index + 1" />" class="tablePadding">
                <tr>
                    <th id="thPlaceTableHeader" colspan="4">Place <s:property value="#status.index + 1" /></th>
                </tr>
                <tr>
                    <td>Place</td>
                    <td><input type="text" id="place" name="place_<s:property value="#status.index + 1" />" value="<s:property value="#current.placeName" />" /></td>
                    <td>Select a language</td>
                    <td>
                        <select id="placeLanguage" name="placeLanguage_<s:property value="#status.index + 1" />" onchange="">
                            <s:iterator value="languages" var="language">
                                <option value='<s:property value="#language.key"/>' <s:if test='%{#language.key == #current.languageCode}'>selected="selected"</s:if>><s:property value="#language.value"/></option>
                            </s:iterator>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td>Link to controlled vocabulary used for places</td>
                    <td><input type="text" id="linkPlaceVocab" name="linkPlaceVocab_<s:property value="#status.index + 1" />" value="<s:property value="#current.vocabLink" />" /></td>
                    <td colspan="2"></td>
                </tr>
                <tr>
                    <td>Country</td>
                    <td>
                        <select id="placeCountry" name="placeCountry_<s:property value="#status.index + 1" />" onchange="">
                            <s:iterator value="countryList" var="list">
                                <option value='<s:property value="#list.key"/>' <s:if test='%{#list.key == #current.countryCode}'>selected="selected"</s:if>><s:property value="#list.value"/></option>
                            </s:iterator>
                        </select>
                    </td>
                    <td colspan="2"></td>
                </tr>
                <s:if test="%{#current.addressDetails.size() > 0}">
                    <s:iterator var="currentAddressDetail" value="#current.addressDetails" status="status2">
                        <s:iterator var="currentAddressComponent" value="#current.addressComponents" status="status3">
                            <s:if test="%{#status2.index == #status3.index}">
                                <tr id="trAddressComponent_<s:property value="#status.index + 1" />">
                                    <td>Address details</td>
                                    <td><input type="text" id="addressDetails" name="placeTable_<s:property value="#status.index + 1" />_addressDetails_<s:property value="#status2.index + 1" />" value="<s:property value="#currentAddressDetail" />" /></td>
                                    <td>Component</td>
                                    <td>
                                        <select id="addressComponent" name="placeTable_<s:property value="#status.index + 1" />_addressComponent_<s:property value="#status2.index + 1" />" onchange="">
                                            <s:iterator value="addressComponentTypeList" var="list">
                                                <option value='<s:property value="#list"/>' <s:if test='%{#list == #currentAddressComponent}'>selected="selected"</s:if>><s:property value="#list"/></option>
                                            </s:iterator>
                                        </select>
                                    </td>
                                </tr>
                            </s:if>
                        </s:iterator>
                    </s:iterator>
                </s:if>
                <s:else>
                    <tr id="trAddressComponent_<s:property value="#status.index + 1" />">
                        <td>Address details</td>
                        <td><input type="text" id="addressDetails" name="placeTable_<s:property value="#status.index + 1" />_addressDetails_1" /></td>
                        <td>Component</td>
                        <td>
                            <select id="addressComponent" name="placeTable_<s:property value="#status.index + 1" />_addressComponent_1" onchange="">
                                <s:iterator value="addressComponentTypeList" var="list">
                                    <option value='<s:property value="#list"/>'><s:property value="#list"/></option>
                                </s:iterator>
                            </select>
                        </td>
                    </tr>
                </s:else>
                <tr>
                    <td><input type="button" value="Add further address details" id="addAddressComponentButton" onclick="addAddressComponent($(this).parent().parent().parent().parent().attr('id'));" /></td>
                    <td colspan="3"></td>
                </tr>
                <tr id="trPlaceRole">
                    <td>Role of the place</td>
                    <td><input type="text" id="placeRole" name="placeRole_<s:property value="#status.index + 1" />" value="<s:property value="#current.role" />" /></td>
                    <td colspan="2"></td>
                </tr>
                <s:if test="%{#current.dates.size() > 0}">
                    <s:iterator var="currentDateRow" value="#current.dates" status="status">
                        <tr id="trDate_text_<s:property value="#status.index + 1" />">
                            <td>Date<s:if test="#currentDateRow.dateContent2 != null"> range from</s:if></td>
                                <td>
                                    <input type="text" id="date_1" name="dateExistenceTable_date_1_<s:property value="#status.index + 1" />" value="<s:property value="#currentDateRow.dateContent1" />" <s:if test="%{#currentDateRow.standardDate1.year == 0000}">disabled="disabled" </s:if>onblur="parseDateToISO($(this).attr('value'), $(this).parent().parent().parent().parent().attr('id'), $(this).parent().parent().attr('id'), $(this).attr('id'));"><br />
                                <input type="checkbox" id="date_unknown_1" name="dateExistenceTable_date_unknown_1_<s:property value="#status.index + 1" />" <s:if test="%{#currentDateRow.standardDate1.year == 0000}">checked="checked" </s:if>onchange="toggleDateTextfields($(this));" /><label for="date_unknown_1">unknown</label>
                                </td>
                            <s:if test="#currentDateRow.dateContent2 != null or #currentDateRow.standardDate2 != null">
                                <td>to</td>
                                <td>
                                    <input type="text" id="date_2" name="dateExistenceTable_date_2_<s:property value="#status.index + 1" />" value="<s:property value="#currentDateRow.dateContent2" />" <s:if test="%{#currentDateRow.standardDate2.year == 2999}">disabled="disabled" </s:if>onblur="parseDateToISO($(this).attr('value'), $(this).parent().parent().parent().parent().attr('id'), $(this).parent().parent().attr('id'), $(this).attr('id'));"><br />
                                    <input type="checkbox" id="date_unknown_2" name="dateExistenceTable_date_unknown_2_<s:property value="#status.index + 1" />" <s:if test="%{#currentDateRow.standardDate2.year == 2999}">checked="checked" </s:if>onchange="toggleDateTextfields($(this));" /><label for="date_unknown_2">unknown</label>
                                    </td>
                            </s:if>
                            <s:else>
                                <td></td>
                                <td></td>
                            </s:else>
                        </tr>
                        <tr id="trDate_iso_<s:property value="#status.index + 1" />">
                            <td>(ISO date<s:if test="#currentDateRow.standardDate2 != null">s</s:if>; optional)</td>
                            <td><input type="text" title="YYYY" id="date_1_Year" name="dateExistenceTable_date_1_Year_<s:property value="#status.index + 1" />" size="4" maxlength="4" <s:if test="%{#currentDateRow.standardDate1.year == 0000}">disabled="disabled" </s:if> value="<s:property value="#currentDateRow.standardDate1.year" />" /> &ndash;
                                <input type="text" title="MM" id="date_1_Month" name="dateExistenceTable_date_1_Month_<s:property value="#status.index + 1" />" size="2" maxlength="2" <s:if test="%{#currentDateRow.standardDate1.year == 0000}">disabled="disabled" </s:if> <s:if test="%{#currentDateRow.standardDate1.month != 0}">value="<s:property value="#currentDateRow.standardDate1.month" />"</s:if> /> &ndash;
                                <input type="text" title="DD" id="date_1_Day" name="dateExistenceTable_date_1_Day_<s:property value="#status.index + 1" />" size="2" maxlength="2" <s:if test="%{#currentDateRow.standardDate1.year == 0000}">disabled="disabled" </s:if> <s:if test="%{#currentDateRow.standardDate1.day != 0}">value="<s:property value="#currentDateRow.standardDate1.day" />"</s:if> /></td>
                                <s:if test="#currentDateRow.standardDate2 != null">
                                <td></td>
                                <td><input type="text" title="YYYY" id="date_2_Year" name="dateExistenceTable_date_2_Year_<s:property value="#status.index + 1" />" size="4" maxlength="4" <s:if test="%{#currentDateRow.standardDate2.year == 2999}">disabled="disabled" </s:if> value="<s:property value="#currentDateRow.standardDate2.year" />" /> &ndash;
                                    <input type="text" title="MM" id="date_2_Month" name="dateExistenceTable_date_2_Month_<s:property value="#status.index + 1" />" size="2" maxlength="2" <s:if test="%{#currentDateRow.standardDate2.year == 2999}">disabled="disabled" </s:if> <s:if test="%{#currentDateRow.standardDate2.month != 0}">value="<s:property value="#currentDateRow.standardDate2.month" />"</s:if> /> &ndash;
                                    <input type="text" title="DD" id="date_2_Day" name="dateExistenceTable_date_2_Day_<s:property value="#status.index + 1" />" size="2" maxlength="2" <s:if test="%{#currentDateRow.standardDate2.year == 2999}">disabled="disabled" </s:if> <s:if test="%{#currentDateRow.standardDate2.day != 0}">value="<s:property value="#currentDateRow.standardDate2.day" />"</s:if> /></td>
                                </s:if>
                                <s:else>
                                <td></td>
                                <td></td>
                            </s:else>
                        </tr>
                    </s:iterator>
                </s:if>
                <tr>
                    <td>Add</td>
                    <td>
                        <s:iterator value="dateOrDateRange" var="dateType">
                            <input type="radio" name="dateOrDateRangePlace" value='<s:property value="#dateType.key"/>' onclick="addDateOrDateRangePlace($(this).parent().parent().parent().parent().attr('id'))"><s:property value="#dateType.value"/>&nbsp;&nbsp;&nbsp;
                        </s:iterator>
                    </td>
                    <td><input type="hidden" id="placeTable_<s:property value="#status.index + 1" />_rows" name="placeTable_<s:property value="#status.index + 1" />_rows" value="0" /></td>
                    <td></td>
                </tr>
            </table>
        </s:iterator>
    </s:if>
    <s:else>
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
    </s:else>
    <table id="addPlaceButtonPanel" class="tablePadding">
        <tr>
            <td><input type="button" value="Add further place" id="addPlaceButton" onclick="addPlace('<s:property value="defaultLanguage" />');" /></td>
        </tr>
    </table>
    <s:if test="%{loader.functions.size() > 0}">
        <s:iterator var="current" value="loader.functions" status="status">
            <table id="functionTable_<s:property value="#status.index + 1" />" class="tablePadding">
                <tr>
                    <th id="thFunctionTableHeader" colspan="4">Function <s:property value="#status.index + 1" /></th>
                </tr>
                <tr>
                    <td>Function</td>
                    <td><input type="text" id="function" name="function_<s:property value="#status.index + 1" />" value="<s:property value="#current.functionName" />" /></td>
                    <td>Select a language</td>
                    <td>
                        <select id="functionLanguage" name="functionLanguage_<s:property value="#status.index + 1" />" onchange="">
                            <s:iterator value="languages" var="language">
                                <option value='<s:property value="#language.key"/>' <s:if test='%{#language.key == #current.languageCode}'>selected="selected"</s:if>><s:property value="#language.value"/></option>
                            </s:iterator>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td>Link to controlled vocabulary used for functions</td>
                    <td><input type="text" id="linkFunctionVocab" name="linkFunctionVocab_<s:property value="#status.index + 1" />" value="<s:property value="#current.vocabLink" />" /></td>
                    <td colspan="2"></td>
                </tr>
                <tr>
                    <td>Description of the function</td>
                    <td colspan="3"><textarea id="functionDescription" name="functionDescription_<s:property value="#status.index + 1" />"><s:property value="#current.description" /></textarea></td>
                </tr>
                <s:if test="%{#current.places.size() > 0}">
                    <s:iterator var="currentPlace" value="#current.places" status="status2">
                        <s:iterator var="currentCountryCode" value="#current.countryCodes" status="status3">
                            <s:if test="%{#status2.index == #status3.index}">
                                <tr id="trPlaceFunction_<s:property value="#status.index + 1" />">
                                    <td>Place</td>
                                    <td><input type="text" id="textPlaceFunction" name="functionTable_<s:property value="#status.index + 1" />_place_<s:property value="#status2.index + 1" />" value="<s:property value="#currentPlace" />" /></td>
                                    <td>Country</td>
                                    <td>
                                        <select id="functionCountry"  name="functionTable_<s:property value="#status.index + 1" />_country_<s:property value="#status2.index + 1" />" onchange="">
                                            <s:iterator value="countryList" var="list">
                                                <option value='<s:property value="#list.key"/>' <s:if test="%{#list.key == #currentCountryCode}">selected="selected"</s:if>><s:property value="#list.value"/></option>
                                            </s:iterator>
                                        </select>
                                    </td>
                                </tr>
                            </s:if>
                        </s:iterator>
                    </s:iterator>
                </s:if>
                <s:else>
                    <tr id="trPlaceFunction_<s:property value="#status.index + 1" />">
                        <td>Place</td>
                        <td><input type="text" id="textPlaceFunction" name="functionTable_<s:property value="#status.index + 1" />_place_1" /></td>
                        <td>Country</td>
                        <td>
                            <select id="functionCountry"  name="functionTable_<s:property value="#status.index + 1" />_country_1" onchange="">
                                <s:iterator value="countryList" var="list">
                                    <option value='<s:property value="#list.key"/>'><s:property value="#list.value"/></option>
                                </s:iterator>
                            </select>
                        </td>
                    </tr>
                </s:else>
                <tr id="trPlaceFunctionButton">
                    <td><input type="button" value="Add further place for function" id="addPlaceFunctionButton" onclick="addPlaceFunction($(this).parent().parent().parent().parent().attr('id'));" /></td>
                    <td colspan="3"></td>
                    <s:if test="%{#current.dates.size() > 0}">
                        <s:iterator var="currentDateRow" value="#current.dates" status="status">
                        <tr id="trDate_text_<s:property value="#status.index + 1" />">
                            <td>Date<s:if test="#currentDateRow.dateContent2 != null"> range from</s:if></td>
                                <td>
                                    <input type="text" id="date_1" name="dateExistenceTable_date_1_<s:property value="#status.index + 1" />" value="<s:property value="#currentDateRow.dateContent1" />" <s:if test="%{#currentDateRow.standardDate1.year == 0000}">disabled="disabled" </s:if>onblur="parseDateToISO($(this).attr('value'), $(this).parent().parent().parent().parent().attr('id'), $(this).parent().parent().attr('id'), $(this).attr('id'));"><br />
                                <input type="checkbox" id="date_unknown_1" name="dateExistenceTable_date_unknown_1_<s:property value="#status.index + 1" />" <s:if test="%{#currentDateRow.standardDate1.year == 0000}">checked="checked" </s:if>onchange="toggleDateTextfields($(this));" /><label for="date_unknown_1">unknown</label>
                                </td>
                            <s:if test="#currentDateRow.dateContent2 != null or #currentDateRow.standardDate2 != null">
                                <td>to</td>
                                <td>
                                    <input type="text" id="date_2" name="dateExistenceTable_date_2_<s:property value="#status.index + 1" />" value="<s:property value="#currentDateRow.dateContent2" />" <s:if test="%{#currentDateRow.standardDate2.year == 2999}">disabled="disabled" </s:if>onblur="parseDateToISO($(this).attr('value'), $(this).parent().parent().parent().parent().attr('id'), $(this).parent().parent().attr('id'), $(this).attr('id'));"><br />
                                    <input type="checkbox" id="date_unknown_2" name="dateExistenceTable_date_unknown_2_<s:property value="#status.index + 1" />" <s:if test="%{#currentDateRow.standardDate2.year == 2999}">checked="checked" </s:if>onchange="toggleDateTextfields($(this));" /><label for="date_unknown_2">unknown</label>
                                    </td>
                            </s:if>
                            <s:else>
                                <td></td>
                                <td></td>
                            </s:else>
                        </tr>
                        <tr id="trDate_iso_<s:property value="#status.index + 1" />">
                            <td>(ISO date<s:if test="#currentDateRow.standardDate2 != null">s</s:if>; optional)</td>
                            <td><input type="text" title="YYYY" id="date_1_Year" name="dateExistenceTable_date_1_Year_<s:property value="#status.index + 1" />" size="4" maxlength="4" <s:if test="%{#currentDateRow.standardDate1.year == 0000}">disabled="disabled" </s:if> value="<s:property value="#currentDateRow.standardDate1.year" />" /> &ndash;
                                <input type="text" title="MM" id="date_1_Month" name="dateExistenceTable_date_1_Month_<s:property value="#status.index + 1" />" size="2" maxlength="2" <s:if test="%{#currentDateRow.standardDate1.year == 0000}">disabled="disabled" </s:if> <s:if test="%{#currentDateRow.standardDate1.month != 0}">value="<s:property value="#currentDateRow.standardDate1.month" />"</s:if> /> &ndash;
                                <input type="text" title="DD" id="date_1_Day" name="dateExistenceTable_date_1_Day_<s:property value="#status.index + 1" />" size="2" maxlength="2" <s:if test="%{#currentDateRow.standardDate1.year == 0000}">disabled="disabled" </s:if> <s:if test="%{#currentDateRow.standardDate1.day != 0}">value="<s:property value="#currentDateRow.standardDate1.day" />"</s:if> /></td>
                                <s:if test="#currentDateRow.standardDate2 != null">
                                <td></td>
                                <td><input type="text" title="YYYY" id="date_2_Year" name="dateExistenceTable_date_2_Year_<s:property value="#status.index + 1" />" size="4" maxlength="4" <s:if test="%{#currentDateRow.standardDate2.year == 2999}">disabled="disabled" </s:if> value="<s:property value="#currentDateRow.standardDate2.year" />" /> &ndash;
                                    <input type="text" title="MM" id="date_2_Month" name="dateExistenceTable_date_2_Month_<s:property value="#status.index + 1" />" size="2" maxlength="2" <s:if test="%{#currentDateRow.standardDate2.year == 2999}">disabled="disabled" </s:if> <s:if test="%{#currentDateRow.standardDate2.month != 0}">value="<s:property value="#currentDateRow.standardDate2.month" />"</s:if> /> &ndash;
                                    <input type="text" title="DD" id="date_2_Day" name="dateExistenceTable_date_2_Day_<s:property value="#status.index + 1" />" size="2" maxlength="2" <s:if test="%{#currentDateRow.standardDate2.year == 2999}">disabled="disabled" </s:if> <s:if test="%{#currentDateRow.standardDate2.day != 0}">value="<s:property value="#currentDateRow.standardDate2.day" />"</s:if> /></td>
                                </s:if>
                                <s:else>
                                <td></td>
                                <td></td>
                            </s:else>
                        </tr>
                    </s:iterator>
                </s:if>
                <tr>
                    <td>Add</td>
                    <td>
                        <s:iterator value="dateOrDateRange" var="dateType">
                            <input type="radio" name="dateOrDateRangeFunction" value='<s:property value="#dateType.key"/>' onclick="addDateOrDateRangeFunction($(this).parent().parent().parent().parent().attr('id'));"><s:property value="#dateType.value"/>&nbsp;&nbsp;&nbsp;
                        </s:iterator>
                    </td>
                    <td><input type="hidden" id="functionTable_<s:property value="#status.index + 1" />_rows" name="functionTable_<s:property value="#status.index + 1" />_rows" value="0" /></td>
                    <td></td>
                </tr>
            </table>
        </s:iterator>
    </s:if>
    <s:else>
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
    </s:else>
    <table id="addFunctionButtonPanel" class="tablePadding">
        <tr>
            <td><input type="button" value="Add further function" id="addFunctionButton" onclick="addFunction('<s:property value="defaultLanguage" />');" /></td>
        </tr>
    </table>
    <s:if test="%{loader.occupations.size() > 0}">
        <s:iterator var="current" value="loader.occupations" status="status">
            <table id="occupationTable_<s:property value="#status.index + 1" />" class="tablePadding">
                <tr>
                    <th id="thOccupationTableHeader" colspan="4">Occupation <s:property value="#status.index + 1" /></th>
                </tr>
                <tr>
                    <td>Occupation</td>
                    <td><input type="text" id="occupation" name="occupation_<s:property value="#status.index + 1" />" value="<s:property value="#current.occupationName" />" /></td>
                    <td>Select a language</td>
                    <td>
                        <select id="occupationLanguage" name="occupationLanguage_<s:property value="#status.index + 1" />" onchange="">
                            <s:iterator value="languages" var="language">
                                <option value='<s:property value="#language.key"/>'<s:if test='%{#language.key == #current.languageCode}'>selected="selected"</s:if>><s:property value="#language.value"/></option>
                            </s:iterator>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td>Link to controlled vocabulary used for occupations</td>
                    <td><input type="text" id="linkOccupationVocab" name="linkOccupationVocab_<s:property value="#status.index + 1" />" value="<s:property value="#current.vocabLink" />" /></td>
                    <td colspan="2"></td>
                </tr>
                <tr>
                    <td>Description of the occupation</td>
                    <td colspan="3"><textarea id="occupationDescription" name="occupationDescription_<s:property value="#status.index + 1" />"><s:property value="#current.description" /></textarea></td>
                </tr>
                <s:if test="%{#current.places.size() > 0}">
                    <s:iterator var="currentPlace" value="#current.places" status="status2">
                        <s:iterator var="currentCountryCode" value="#current.countryCodes" status="status3">
                            <s:if test="%{#status2.index == #status3.index}">
                                <tr id="trPlaceOccupation_<s:property value="#status.index + 1" />">
                                    <td>Place</td>
                                    <td><input type="text" id="textPlaceOccupation" name="occupationTable_<s:property value="#status.index + 1" />_place_<s:property value="#status2.index + 1" />" value="<s:property value="#currentPlace" />" /></td>
                                    <td>Country</td>
                                    <td>
                                        <select id="occupationCountry" name="occupationTable_<s:property value="#status.index + 1" />_country_<s:property value="#status2.index + 1" />" onchange="">
                                            <s:iterator value="countryList" var="list">
                                                <option value='<s:property value="#list.key"/>' <s:if test="%{#list.key == #currentCountryCode}">selected="selected"</s:if>><s:property value="#list.value"/></option>
                                            </s:iterator>
                                        </select>
                                    </td>
                                </tr>
                            </s:if>
                        </s:iterator>
                    </s:iterator>
                </s:if>
                <s:else>
                    <tr id="trPlaceOccupation_<s:property value="#status.index + 1" />">
                        <td>Place</td>
                        <td><input type="text" id="textPlaceOccupation" name="occupationTable_<s:property value="#status.index + 1" />_place_1" /></td>
                        <td>Country</td>
                        <td>
                            <select id="occupationCountry" name="occupationTable_<s:property value="#status.index + 1" />_country_1" onchange="">
                                <s:iterator value="countryList" var="list">
                                    <option value='<s:property value="#list.key"/>'><s:property value="#list.value"/></option>
                                </s:iterator>
                            </select>
                        </td>
                    </tr>
                </s:else>
                <tr id="trPlaceOccupationButton">
                    <td><input type="button" value="Add further place for occupation" id="addPlaceOccupationButton" onclick="addPlaceOccupation($(this).parent().parent().parent().parent().attr('id'));" /></td>
                    <td colspan="3"></td>
                </tr>
                <s:if test="%{#current.dates.size() > 0}">
                    <s:iterator var="currentDateRow" value="#current.dates" status="status">
                        <tr id="trDate_text_<s:property value="#status.index + 1" />">
                            <td>Date<s:if test="#currentDateRow.dateContent2 != null"> range from</s:if></td>
                                <td>
                                    <input type="text" id="date_1" name="dateExistenceTable_date_1_<s:property value="#status.index + 1" />" value="<s:property value="#currentDateRow.dateContent1" />" <s:if test="%{#currentDateRow.standardDate1.year == 0000}">disabled="disabled" </s:if>onblur="parseDateToISO($(this).attr('value'), $(this).parent().parent().parent().parent().attr('id'), $(this).parent().parent().attr('id'), $(this).attr('id'));"><br />
                                <input type="checkbox" id="date_unknown_1" name="dateExistenceTable_date_unknown_1_<s:property value="#status.index + 1" />" <s:if test="%{#currentDateRow.standardDate1.year == 0000}">checked="checked" </s:if>onchange="toggleDateTextfields($(this));" /><label for="date_unknown_1">unknown</label>
                                </td>
                            <s:if test="#currentDateRow.dateContent2 != null or #currentDateRow.standardDate2 != null">
                                <td>to</td>
                                <td>
                                    <input type="text" id="date_2" name="dateExistenceTable_date_2_<s:property value="#status.index + 1" />" value="<s:property value="#currentDateRow.dateContent2" />" <s:if test="%{#currentDateRow.standardDate2.year == 2999}">disabled="disabled" </s:if>onblur="parseDateToISO($(this).attr('value'), $(this).parent().parent().parent().parent().attr('id'), $(this).parent().parent().attr('id'), $(this).attr('id'));"><br />
                                    <input type="checkbox" id="date_unknown_2" name="dateExistenceTable_date_unknown_2_<s:property value="#status.index + 1" />" <s:if test="%{#currentDateRow.standardDate2.year == 2999}">checked="checked" </s:if>onchange="toggleDateTextfields($(this));" /><label for="date_unknown_2">unknown</label>
                                    </td>
                            </s:if>
                            <s:else>
                                <td></td>
                                <td></td>
                            </s:else>
                        </tr>
                        <tr id="trDate_iso_<s:property value="#status.index + 1" />">
                            <td>(ISO date<s:if test="#currentDateRow.standardDate2 != null">s</s:if>; optional)</td>
                            <td><input type="text" title="YYYY" id="date_1_Year" name="dateExistenceTable_date_1_Year_<s:property value="#status.index + 1" />" size="4" maxlength="4" <s:if test="%{#currentDateRow.standardDate1.year == 0000}">disabled="disabled" </s:if> value="<s:property value="#currentDateRow.standardDate1.year" />" /> &ndash;
                                <input type="text" title="MM" id="date_1_Month" name="dateExistenceTable_date_1_Month_<s:property value="#status.index + 1" />" size="2" maxlength="2" <s:if test="%{#currentDateRow.standardDate1.year == 0000}">disabled="disabled" </s:if> <s:if test="%{#currentDateRow.standardDate1.month != 0}">value="<s:property value="#currentDateRow.standardDate1.month" />"</s:if> /> &ndash;
                                <input type="text" title="DD" id="date_1_Day" name="dateExistenceTable_date_1_Day_<s:property value="#status.index + 1" />" size="2" maxlength="2" <s:if test="%{#currentDateRow.standardDate1.year == 0000}">disabled="disabled" </s:if> <s:if test="%{#currentDateRow.standardDate1.day != 0}">value="<s:property value="#currentDateRow.standardDate1.day" />"</s:if> /></td>
                                <s:if test="#currentDateRow.standardDate2 != null">
                                <td></td>
                                <td><input type="text" title="YYYY" id="date_2_Year" name="dateExistenceTable_date_2_Year_<s:property value="#status.index + 1" />" size="4" maxlength="4" <s:if test="%{#currentDateRow.standardDate2.year == 2999}">disabled="disabled" </s:if> value="<s:property value="#currentDateRow.standardDate2.year" />" /> &ndash;
                                    <input type="text" title="MM" id="date_2_Month" name="dateExistenceTable_date_2_Month_<s:property value="#status.index + 1" />" size="2" maxlength="2" <s:if test="%{#currentDateRow.standardDate2.year == 2999}">disabled="disabled" </s:if> <s:if test="%{#currentDateRow.standardDate2.month != 0}">value="<s:property value="#currentDateRow.standardDate2.month" />"</s:if> /> &ndash;
                                    <input type="text" title="DD" id="date_2_Day" name="dateExistenceTable_date_2_Day_<s:property value="#status.index + 1" />" size="2" maxlength="2" <s:if test="%{#currentDateRow.standardDate2.year == 2999}">disabled="disabled" </s:if> <s:if test="%{#currentDateRow.standardDate2.day != 0}">value="<s:property value="#currentDateRow.standardDate2.day" />"</s:if> /></td>
                                </s:if>
                                <s:else>
                                <td></td>
                                <td></td>
                            </s:else>
                        </tr>
                    </s:iterator>
                </s:if>
                <tr>
                    <td>Add</td>
                    <td>
                        <s:iterator value="dateOrDateRange" var="dateType">
                            <input type="radio" name="dateOrDateRangeOccupation" value='<s:property value="#dateType.key"/>' onclick="addDateOrDateRangeOccupation($(this).parent().parent().parent().parent().attr('id'));"><s:property value="#dateType.value"/>&nbsp;&nbsp;&nbsp;
                        </s:iterator>
                    </td>
                    <td><input type="hidden" id="occupationTable_<s:property value="#status.index + 1" />_rows" name="occupationTable_<s:property value="#status.index + 1" />_rows" value="0" /></td>
                    <td></td>
                </tr>
            </table>
        </s:iterator>
    </s:if>
    <s:else>
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
    </s:else>
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
    <s:if test="%{loader.genealogies.size() > 0}">
        <s:iterator var="current" value="loader.genealogies" status="status">
            <table id="genealogyContent_<s:property value="#status.index + 1" />" class="tablePadding">
                <tr>
                    <td>Description</td>
                    <td rowspan="3"><textarea id="genealogyDescription" name="genealogyDescription_<s:property value="#status.index + 1" />"><s:property value="#current.paragraph" /></textarea></td>
                    <td>Select a language°</td>
                    <td>
                        <select id="genealogyLanguage" name="genealogyLanguage_<s:property value="#status.index + 1" />" onchange="">
                            <s:iterator value="languages" var="language">
                                <option value='<s:property value="#language.key"/>'<s:if test='%{#language.key == #current.languageCode}'>selected="selected"</s:if>><s:property value="#language.value"/></option>
                            </s:iterator>
                        </select>
                    </td>
                </tr>
            </table>
        </s:iterator>
    </s:if>
    <s:else>
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
    </s:else>
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
    <s:if test="%{loader.biographies.size() > 0}">
        <s:iterator var="current" value="loader.biographies" status="status">
            <table id="biographyContent_<s:property value="#status.index + 1" />" class="tablePadding">
                <tr>
                    <td>Description</td>
                    <td rowspan="3"><textarea id="biographyDescription" name="biographyDescription_<s:property value="#status.index + 1" />"><s:property value="#current.paragraph" /></textarea></td>
                    <td>Select a language°</td>
                    <td>
                        <select id="biographyLanguage" name="biographyLanguage_<s:property value="#status.index + 1" />" onchange="">
                            <s:iterator value="languages" var="language">
                                <option value='<s:property value="#language.key"/>'<s:if test='%{#language.key == #current.languageCode}'>selected="selected"</s:if>><s:property value="#language.value"/></option>
                            </s:iterator>
                        </select>
                    </td>
                </tr>
            </table>
        </s:iterator>
    </s:if>
    <s:else>
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
    </s:else>
    <table id="addBiographyButtonPanel" class="tablePadding">
        <tr>
            <td><input type="button" value="Add further biography description" id="addBiographyButton" onclick="addBiography();" /></td>
        </tr>
    </table>
</div>