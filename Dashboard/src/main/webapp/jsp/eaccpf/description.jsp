<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="/struts-tags" prefix="s" %>
<div id="headerContainer">
</div>

<div id="descriptionTabContent">
    <h2 class="tablePadding"><s:property value="cpfTypeDescriptionText" /></h2>
    <s:if test="%{loader.places.size() > 0}">
        <s:iterator var="current" value="loader.places" status="status">
            <table id="placeTable_<s:property value="#status.index + 1" />" class="tablePadding">
                <tr>
                    <th class="sectionHeader" colspan="4"><s:property value="getText('eaccpf.description.place')" /></th>
                </tr>
                <tr>
                    <td><label for="place"><s:property value="getText('eaccpf.description.place')" /></label></td>
                    <td><input type="text" id="place" name="place_<s:property value="#status.index + 1" />" value="<s:property value="#current.placeName" />" /></td>
                    <td><label for="placeLanguage"><s:property value="getText('eaccpf.description.selectlanguage')" /></label></td>
                    <td>
                        <select id="placeLanguage" name="placeLanguage_<s:property value="#status.index + 1" />" >
                            <s:iterator value="languages" var="language">
                                <option value='<s:property value="#language.key"/>' <s:if test='%{#language.key == #current.languageCode}'>selected="selected"</s:if>><s:property value="#language.value"/></option>
                            </s:iterator>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td><label for="linkPlaceVocab"><s:property value="getText('eaccpf.description.linkvocabularyplaces')" /></label></td>
                    <td><input type="text" id="linkPlaceVocab" name="linkPlaceVocab_<s:property value="#status.index + 1" />" value="<s:property value="#current.vocabLink" />" /></td>
                    <td colspan="2"></td>
                </tr>
                <tr>
                    <td><label for="placeCountry"><s:property value="getText('eaccpf.description.country')" /></label></td>
                    <td>
                        <select id="placeCountry" name="placeCountry_<s:property value="#status.index + 1" />" >
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
                                    <td><label for="addressDetails"><s:property value="getText('eaccpf.description.addressdetails')" /></label></td>
                                    <td><input type="text" id="addressDetails" name="placeTable_<s:property value="#status.index + 1" />_addressDetails_<s:property value="#status2.index + 1" />" value="<s:property value="#currentAddressDetail" />" /></td>
                                    <td><label for="addressComponent"><s:property value="getText('eaccpf.description.component')" /></label></td>
                                    <td>
                                        <select id="addressComponent" name="placeTable_<s:property value="#status.index + 1" />_addressComponent_<s:property value="#status2.index + 1" />" >
                                            <s:iterator value="addressComponentTypeList" var="list">
                                                <option value='<s:property value="#list.key"/>' <s:if test='%{#list.key == #currentAddressComponent}'>selected="selected"</s:if>><s:property value="#list.value"/></option>
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
                        <td><label for="addressDetails"><s:property value="getText('eaccpf.description.addressdetails')" /></label></td>
                        <td><input type="text" id="addressDetails" name="placeTable_<s:property value="#status.index + 1" />_addressDetails_1" /></td>
                        <td><label for="addressComponent"><s:property value="getText('eaccpf.description.component')" /></label></td>
                        <td>
                            <select id="addressComponent" name="placeTable_<s:property value="#status.index + 1" />_addressComponent_1">
                                <s:iterator value="addressComponentTypeList" var="list">
                                    <option value='<s:property value="#list.key"/>'><s:property value="#list.value"/></option>
                                </s:iterator>
                            </select>
                        </td>
                    </tr>
                </s:else>
                <tr>
                    <td><input type="button" value="<s:property value="getText('eaccpf.description.button.addaddressdetails')" />" id="addAddressComponentButton" onclick="addAddressComponent($(this).parent().parent().parent().parent().attr('id'), '<s:property value="getText('eaccpf.commons.error.empty.place')" />');" /></td>
                    <td colspan="3"></td>
                </tr>
                <tr id="trPlaceRole">
                    <td><label for="placeRole_<s:property value="#status.index + 1" />"><s:property value="getText('eaccpf.description.roleplace')" /></label></td>
                    <td>
                        <select id="placeRole_<s:property value="#status.index + 1" />" name="placeRole_<s:property value="#status.index + 1" />" value="<s:property value="#current.role" />">
                            <s:iterator value="placeEntryList" var="list">
                                <option value='<s:property value="#list.key"/>' <s:if test='%{#list.key == #current.role}'>selected="selected"</s:if>><s:property value="#list.value"/></option>
                            </s:iterator>
                        </select>
                    </td>
                    <td colspan="2"></td>
                </tr>
                <s:if test="%{#current.dates.size() > 0}">
                    <s:iterator var="currentDateRow" value="#current.dates" status="status2">
                        <tr id="trDate_text_<s:property value="#status2.index + 1" />">
                            <td>
                                <s:if test="#currentDateRow.dateContent2 != null">
                                    <s:property value="getText('eaccpf.commons.from.date')" />:
                                </s:if>
                                <s:else>
                                    <s:property value="getText('eaccpf.commons.date')" />:
                                </s:else>
                            </td>
                            <td>
                                <input type="text" id="date_1" name="placeTable_<s:property value="#status.index + 1" />_date_1_<s:property value="#status2.index + 1" />" value="<s:property value="#currentDateRow.dateContent1" />" <s:if test='%{#currentDateRow.radioValue1 != "known"}'>disabled="disabled"</s:if> onchange="parseDateToISO($(this).attr('value'), $(this).parent().parent().parent().parent().attr('id'), $(this).parent().parent().attr('id'), $(this).attr('id'));"><br />
                                </td>
                            <s:if test="#currentDateRow.dateContent2 != null or #currentDateRow.standardDate2 != null">
                                <td><s:property value="getText('eaccpf.commons.to.date')" />:</td>
                                <td>
                                    <input type="text" id="date_2" name="placeTable_<s:property value="#status.index + 1" />_date_2_<s:property value="#status2.index + 1" />" value="<s:property value="#currentDateRow.dateContent2" />" <s:if test='%{#currentDateRow.radioValue2 != "known"}'>disabled="disabled"</s:if> onchange="parseDateToISO($(this).attr('value'), $(this).parent().parent().parent().parent().attr('id'), $(this).parent().parent().attr('id'), $(this).attr('id'));"><br />
                                    </td>
                            </s:if>
                            <s:else>
                                <td></td>
                                <td></td>
                            </s:else>
                        </tr>
                        <tr id="trDate_radio_<s:property value="#status2.index + 1" />">
                            <td>
                                <s:property value="getText('eaccpf.commons.date.type')"/>:
                            </td>
                            <td>
                                <input type="radio" name="placeTable_<s:property value="#status.index + 1" />_date_1_radio_<s:property value="#status2.index + 1" />" value="known" <s:if test='%{#currentDateRow.radioValue1 == "known"}'>checked="checked"</s:if> onchange="toggleDateTextfields($(this));"><s:property value="getText('eaccpf.commons.date.known')"/>&nbsp;
                                <input type="radio" name="placeTable_<s:property value="#status.index + 1" />_date_1_radio_<s:property value="#status2.index + 1" />" value="unknown" <s:if test='%{#currentDateRow.radioValue1 == "unknown"}'>checked="checked"</s:if> onchange="toggleDateTextfields($(this));"><s:property value="getText('eaccpf.commons.unknown.date')"/>&nbsp;
                                <s:if test="#currentDateRow.dateContent2 != null or #currentDateRow.standardDate2 != null">
                                </td>
                                <td></td>
                                <td>
                                    <input type="radio" name="placeTable_<s:property value="#status.index + 1" />_date_2_radio_<s:property value="#status2.index + 1" />" value="known" <s:if test='%{#currentDateRow.radioValue2 == "known"}'>checked="checked"</s:if> onchange="toggleDateTextfields($(this));"><s:property value="getText('eaccpf.commons.date.known')"/>&nbsp;
                                    <input type="radio" name="placeTable_<s:property value="#status.index + 1" />_date_2_radio_<s:property value="#status2.index + 1" />" value="unknown" <s:if test='%{#currentDateRow.radioValue2 == "unknown"}'>checked="checked"</s:if> onchange="toggleDateTextfields($(this));"><s:property value="getText('eaccpf.commons.unknown.date')"/>&nbsp;
                                    <input type="radio" name="placeTable_<s:property value="#status.index + 1" />_date_2_radio_<s:property value="#status2.index + 1" />" value="open" <s:if test='%{#currentDateRow.radioValue2 == "open"}'>checked="checked"</s:if> onchange="toggleDateTextfields($(this));"><s:property value="openLabel"/>
                                    </td>
                            </s:if>
                            <s:else>
                            <input type="radio" name="placeTable_<s:property value="#status.index + 1" />_date_1_radio_<s:property value="#status2.index + 1" />" value="open" <s:if test='%{#currentDateRow.radioValue1 == "open"}'>checked="checked"</s:if> onchange="toggleDateTextfields($(this));"><s:property value="openLabel"/>&nbsp;
                                </td>
                                <td></td>
                                <td></td>
                        </s:else><tr id="trDate_iso_<s:property value="#status2.index + 1" />">
                            <td><s:property value="getText('eaccpf.commons.iso.date')" />:</td>
                            <td>
                                <table style="width: 50%;">
                                    <tr>
                                        <td style="padding: 0px;"><input type="text" title="YYYY" id="date_1_Year" name="placeTable_<s:property value="#status.index + 1" />_date_1_Year_<s:property value="#status2.index + 1" />" <s:if test='%{#currentDateRow.radioValue1 != "known"}'>disabled="disabled"</s:if> <s:if test="%{#currentDateRow.standardDate1.year != 0}">value="<s:property value="#currentDateRow.standardDate1.year" />"</s:if> size="4" maxlength="4" onchange="validateIsoDates($(this), '<s:property value="getText('eaccpf.commons.error.no.standard.date')" />', '<s:property value="getText('eaccpf.commons.error.no.standard.dateRange')" />');" /></td>
                                            <td style="padding: 0px;">&ndash;</td>
                                                <td style="padding: 0px;"><input type="text" title="MM" id="date_1_Month" name="placeTable_<s:property value="#status.index + 1" />_date_1_Month_<s:property value="#status2.index + 1" />" <s:if test='%{#currentDateRow.radioValue1 != "known"}'>disabled="disabled"</s:if> <s:if test="%{#currentDateRow.standardDate1.month != 0}">value="<s:if test="%{#currentDateRow.standardDate1.month < 10}">0</s:if><s:property value="#currentDateRow.standardDate1.month" />"</s:if> size="2" maxlength="2" onchange="validateIsoDates($(this), '<s:property value="getText('eaccpf.commons.error.no.standard.date')" />', '<s:property value="getText('eaccpf.commons.error.no.standard.dateRange')" />');" /></td>
                                            <td style="padding: 0px;">&ndash;</td>
                                                <td style="padding: 0px;"><input type="text" title="DD" id="date_1_Day" name="placeTable_<s:property value="#status.index + 1" />_date_1_Day_<s:property value="#status2.index + 1" />" <s:if test='%{#currentDateRow.radioValue1 != "known"}'>disabled="disabled"</s:if> <s:if test="%{#currentDateRow.standardDate1.day != 0}">value="<s:if test="%{#currentDateRow.standardDate1.day < 10}">0</s:if><s:property value="#currentDateRow.standardDate1.day" />"</s:if> size="2" maxlength="2" onchange="validateIsoDates($(this), '<s:property value="getText('eaccpf.commons.error.no.standard.date')" />', '<s:property value="getText('eaccpf.commons.error.no.standard.dateRange')" />');" /></td>
                                        </tr>
                                        <tr>
                                            <td style="padding: 0px;">YYYY</td>
                                            <td style="padding: 0px;">&ndash;</td>
                                            <td style="padding: 0px;">MM</td>
                                            <td style="padding: 0px;">&ndash;</td>
                                            <td style="padding: 0px;">DD</td>
                                        </tr>
                                    </table>
                                </td>
                                <td></td>
                            <s:if test="#currentDateRow.dateContent2 != null or #currentDateRow.standardDate2 != null">
                                <td>
                                    <table style="width: 50%;">
                                        <tr>
                                            <td style="padding: 0px;"><input type="text" title="YYYY" id="date_2_Year" name="placeTable_<s:property value="#status.index + 1" />_date_2_Year_<s:property value="#status2.index + 1" />" <s:if test='%{#currentDateRow.radioValue2 != "known"}'>disabled="disabled"</s:if> <s:if test="%{#currentDateRow.standardDate2.year != 0}">value="<s:property value="#currentDateRow.standardDate2.year" />"</s:if> size="4" maxlength="4" onchange="validateIsoDates($(this), '<s:property value="getText('eaccpf.commons.error.no.standard.date')" />', '<s:property value="getText('eaccpf.commons.error.no.standard.dateRange')" />');" /></td>
                                                <td style="padding: 0px;">&ndash;</td>
                                                    <td style="padding: 0px;"><input type="text" title="MM" id="date_2_Month" name="placeTable_<s:property value="#status.index + 1" />_date_2_Month_<s:property value="#status2.index + 1" />" <s:if test='%{#currentDateRow.radioValue2 != "known"}'>disabled="disabled"</s:if> <s:if test="%{#currentDateRow.standardDate2.month != 0}">value="<s:if test="%{#currentDateRow.standardDate2.month < 10}">0</s:if><s:property value="#currentDateRow.standardDate2.month" />"</s:if>size="2" maxlength="2" onchange="validateIsoDates($(this), '<s:property value="getText('eaccpf.commons.error.no.standard.date')" />', '<s:property value="getText('eaccpf.commons.error.no.standard.dateRange')" />');" /></td>
                                                <td style="padding: 0px;">&ndash;</td>
                                                    <td style="padding: 0px;"><input type="text" title="DD" id="date_2_Day" name="placeTable_<s:property value="#status.index + 1" />_date_2_Day_<s:property value="#status2.index + 1" />" <s:if test='%{#currentDateRow.radioValue2 != "known"}'>disabled="disabled"</s:if> <s:if test="%{#currentDateRow.standardDate2.day != 0}">value="<s:if test="%{#currentDateRow.standardDate2.day < 10}">0</s:if><s:property value="#currentDateRow.standardDate2.day" />"</s:if>size="2" maxlength="2" onchange="validateIsoDates($(this), '<s:property value="getText('eaccpf.commons.error.no.standard.date')" />', '<s:property value="getText('eaccpf.commons.error.no.standard.dateRange')" />');" /></td>
                                            </tr>
                                            <tr>
                                                <td style="padding: 0px;">YYYY</td>
                                                <td style="padding: 0px;">&ndash;</td>
                                                <td style="padding: 0px;">MM</td>
                                                <td style="padding: 0px;">&ndash;</td>
                                                <td style="padding: 0px;">DD</td>
                                            </tr>
                                        </table>
                                    </td>
                            </s:if>
                            <s:else><td></td></s:else>
                            </tr>
                    </s:iterator>
                </s:if>
                <tr>
                    <td><label><s:property value="getText('eaccpf.description.adddatesofusefortheplace')" /></label></td>
                    <td><input type="button" value="<s:property value="getText('eaccpf.commons.add.single.date')" />" id="addPlaceDate" onclick="addDateOrDateRangePlace(this.id, $(this).parent().parent().parent().parent().attr('id'), '<s:property value="getText('eaccpf.commons.date')" />', '<s:property value="getText('eaccpf.commons.from.date')" />', '<s:property value="getText('eaccpf.commons.to.date')" />', '<s:property value="getText('eaccpf.commons.date.type')" />', '<s:property value="getText('eaccpf.commons.date.known')" />', '<s:property value="getText('eaccpf.commons.unknown.date')" />', '<s:property value="openLabel" />', '<s:property value="getText('eaccpf.commons.iso.date')" />')" />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                        <input type="button" value="<s:property value="getText('eaccpf.commons.add.range.date')" />" id="addPlaceDateRange" onclick="addDateOrDateRangePlace(this.id, $(this).parent().parent().parent().parent().attr('id'), '<s:property value="getText('eaccpf.commons.date')" />', '<s:property value="getText('eaccpf.commons.from.date')" />', '<s:property value="getText('eaccpf.commons.to.date')" />', '<s:property value="getText('eaccpf.commons.date.type')" />', '<s:property value="getText('eaccpf.commons.date.known')" />', '<s:property value="getText('eaccpf.commons.unknown.date')" />', '<s:property value="openLabel" />', '<s:property value="getText('eaccpf.commons.iso.date')" />')" /></td>
                    <td><input type="hidden" id="placeTable_<s:property value="#status.index + 1" />_rows" name="placeTable_<s:property value="#status.index + 1" />_rows" value="<s:property value="#current.dates.size()" />" /></td>
                    <td></td>
                </tr>
            </table>
        </s:iterator>
    </s:if>
    <s:else>
        <table id="placeTable_1" class="tablePadding">
            <tr>
                <th class="sectionHeader" colspan="4"><s:property value="getText('eaccpf.description.place')" /></th>
            </tr>
            <tr>
                <td><label for="place"><s:property value="getText('eaccpf.description.place.name')" />:</label></td>
                <td><input type="text" id="place" name="place_1" oninput="togglePlaceFields($(this));" /></td>
                <td><label for="placeLanguage"><s:property value="getText('eaccpf.description.selectlanguage')" />:</label></td>
                <td>
                    <select id="placeLanguage" name="placeLanguage_1" >
                        <s:iterator value="languages" var="language">
                            <option value='<s:property value="#language.key"/>'<s:if test='%{#language.key == defaultLanguage}'>selected="selected"</s:if>><s:property value="#language.value"/></option>
                        </s:iterator>
                    </select>
                </td>
            </tr>
            <tr>
                <td><label for="linkPlaceVocab"><s:property value="getText('eaccpf.description.linkvocabularyplaces')" />:</label></td>
                <td><input type="text" id="linkPlaceVocab" name="linkPlaceVocab_1" disabled="disabled" /></td>
                <td colspan="2"></td>
            </tr>
            <tr>
                <td><label for="placeCountry"><s:property value="getText('eaccpf.description.country')" />:</label></td>
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
                <td><label for="addressDetails"><s:property value="getText('eaccpf.description.addressdetails')" />:</label></td>
                <td><input type="text" id="addressDetails" name="placeTable_1_addressDetails_1"  disabled="disabled"/></td>
                <td><label for="addressComponent"><s:property value="getText('eaccpf.description.component')" />:</label></td>
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
                <td><label for="placeRole_1"><s:property value="getText('eaccpf.description.roleplace')" />:</label></td>
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
                <td><label><s:property value="getText('eaccpf.description.adddatesofusefortheplace')" />:</label></td>
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
    </s:else>
    <table id="addPlaceButtonPanel" class="tablePadding">
        <tr>
            <td><input type="button" value="<s:property value="getText('eaccpf.description.button.addFurtherPlace')" />" id="addPlaceButton" onclick="addPlace('<s:property value="defaultLanguage" />', '<s:property value="getText('eaccpf.commons.error.empty.place')" />');" /></td>
        </tr>
    </table>
    <s:if test="%{loader.functions.size() > 0}">
        <s:iterator var="current" value="loader.functions" status="status">
            <table id="functionTable_<s:property value="#status.index + 1" />" class="tablePadding">
                <tr>
                    <th class="sectionHeader" colspan="4"><s:property value="getText('eaccpf.description.function')" /></th>
                </tr>
                <tr>
                    <td><label for="function"><s:property value="getText('eaccpf.description.function')" /></label></td>
                    <td><input type="text" id="function" name="function_<s:property value="#status.index + 1" />" value="<s:property value="#current.functionName" />" /></td>
                    <td><label for="functionLanguage"><s:property value="getText('eaccpf.description.selectlanguage')" /></label></td>
                    <td>
                        <select id="functionLanguage" name="functionLanguage_<s:property value="#status.index + 1" />" >
                            <s:iterator value="languages" var="language">
                                <option value='<s:property value="#language.key"/>' <s:if test='%{#language.key == #current.languageCode}'>selected="selected"</s:if>><s:property value="#language.value"/></option>
                            </s:iterator>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td><label for="linkFunctionVocab"><s:property value="getText('eaccpf.description.linktovocabulary')" /></label></td>
                    <td><input type="text" id="linkFunctionVocab" name="linkFunctionVocab_<s:property value="#status.index + 1" />" value="<s:property value="#current.vocabLink" />" /></td>
                    <td colspan="2"></td>
                </tr>
                <tr>
                    <td><label for="functionDescription"><s:property value="getText('eaccpf.description.descriptionfunction')" /></label></td>
                    <td colspan="3"><textarea id="functionDescription" name="functionDescription_<s:property value="#status.index + 1" />"><s:property value="#current.description" /></textarea></td>
                </tr>
                <s:if test="%{#current.places.size() > 0}">
                    <s:iterator var="currentPlace" value="#current.places" status="status2">
                        <s:iterator var="currentCountryCode" value="#current.countryCodes" status="status3">
                            <s:if test="%{#status2.index == #status3.index}">
                                <tr id="trPlaceFunction_<s:property value="#status.index + 1" />">
                                    <td><label for="textPlaceFunction"><s:property value="getText('eaccpf.description.place')" /></label></td>
                                    <td><input type="text" id="textPlaceFunction" name="functionTable_<s:property value="#status.index + 1" />_place_<s:property value="#status2.index + 1" />" value="<s:property value="#currentPlace" />" /></td>
                                    <td><label for="functionCountry"><s:property value="getText('eaccpf.description.country')" /></label></td>
                                    <td>
                                        <select id="functionCountry"  name="functionTable_<s:property value="#status.index + 1" />_country_<s:property value="#status2.index + 1" />" >
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
                        <td><label for="textPlaceFunction"><s:property value="getText('eaccpf.description.place')" /></label></td>
                        <td><input type="text" id="textPlaceFunction" name="functionTable_<s:property value="#status.index + 1" />_place_1" /></td>
                        <td><label for="functionCountry"><s:property value="getText('eaccpf.description.country')" /></label></td>
                        <td>
                            <select id="functionCountry"  name="functionTable_<s:property value="#status.index + 1" />_country_1" >
                                <s:iterator value="countryList" var="list">
                                    <option value='<s:property value="#list.key"/>'><s:property value="#list.value"/></option>
                                </s:iterator>
                            </select>
                        </td>
                    </tr>
                </s:else>
                <tr id="trPlaceFunctionButton">
                    <td><input type="button" value="<s:property value="getText('eaccpf.description.button.addFurtherPlaceFunction')" />" id="addPlaceFunctionButton" onclick="addPlaceFunction($(this).parent().parent().parent().parent().attr('id'), '<s:property value="getText('eaccpf.commons.error.empty.place')" />');" /></td>
                    <td colspan="3"></td>
                    <s:if test="%{#current.dates.size() > 0}">
                        <s:iterator var="currentDateRow" value="#current.dates" status="status2">
                        <tr id="trDate_text_<s:property value="#status2.index + 1" />">
                            <td>
                                <s:if test="#currentDateRow.dateContent2 != null">
                                    <s:property value="getText('eaccpf.commons.from.date')" />:
                                </s:if>
                                <s:else>
                                    <s:property value="getText('eaccpf.commons.date')" />:
                                </s:else>
                            </td>
                            <td>
                                <input type="text" id="date_1" name="functionTable_<s:property value="#status.index + 1" />_date_1_<s:property value="#status2.index + 1" />" value="<s:property value="#currentDateRow.dateContent1" />" <s:if test='%{#currentDateRow.radioValue1 != "known"}'>disabled="disabled"</s:if> onchange="parseDateToISO($(this).attr('value'), $(this).parent().parent().parent().parent().attr('id'), $(this).parent().parent().attr('id'), $(this).attr('id'));"><br />
                                </td>
                            <s:if test="#currentDateRow.dateContent2 != null or #currentDateRow.standardDate2 != null">
                                <td><s:property value="getText('eaccpf.commons.to.date')" />:</td>
                                <td>
                                    <input type="text" id="date_2" name="functionTable_<s:property value="#status.index + 1" />_date_2_<s:property value="#status2.index + 1" />" value="<s:property value="#currentDateRow.dateContent2" />" <s:if test='%{#currentDateRow.radioValue2 != "known"}'>disabled="disabled"</s:if> onchange="parseDateToISO($(this).attr('value'), $(this).parent().parent().parent().parent().attr('id'), $(this).parent().parent().attr('id'), $(this).attr('id'));"><br />
                                    </td>
                            </s:if>
                            <s:else>
                                <td></td>
                                <td></td>
                            </s:else>
                        </tr>
                        <tr id="trDate_radio_<s:property value="#status2.index + 1" />">
                            <td>
                                <s:property value="getText('eaccpf.commons.date.type')"/>:
                            </td>
                            <td>
                                <input type="radio" name="functionTable_<s:property value="#status.index + 1" />_date_1_radio_<s:property value="#status2.index + 1" />" value="known" <s:if test='%{#currentDateRow.radioValue1 == "known"}'>checked="checked"</s:if> onchange="toggleDateTextfields($(this));"><s:property value="getText('eaccpf.commons.date.known')"/>&nbsp;
                                <input type="radio" name="functionTable_<s:property value="#status.index + 1" />_date_1_radio_<s:property value="#status2.index + 1" />" value="unknown" <s:if test='%{#currentDateRow.radioValue1 == "unknown"}'>checked="checked"</s:if> onchange="toggleDateTextfields($(this));"><s:property value="getText('eaccpf.commons.unknown.date')"/>&nbsp;
                                <s:if test="#currentDateRow.dateContent2 != null or #currentDateRow.standardDate2 != null">
                                </td>
                                <td></td>
                                <td>
                                    <input type="radio" name="functionTable_<s:property value="#status.index + 1" />_date_2_radio_<s:property value="#status2.index + 1" />" value="known" <s:if test='%{#currentDateRow.radioValue2 == "known"}'>checked="checked"</s:if> onchange="toggleDateTextfields($(this));"><s:property value="getText('eaccpf.commons.date.known')"/>&nbsp;
                                    <input type="radio" name="functionTable_<s:property value="#status.index + 1" />_date_2_radio_<s:property value="#status2.index + 1" />" value="unknown" <s:if test='%{#currentDateRow.radioValue2 == "unknown"}'>checked="checked"</s:if> onchange="toggleDateTextfields($(this));"><s:property value="getText('eaccpf.commons.unknown.date')"/>&nbsp;
                                    <input type="radio" name="functionTable_<s:property value="#status.index + 1" />_date_2_radio_<s:property value="#status2.index + 1" />" value="open" <s:if test='%{#currentDateRow.radioValue2 == "open"}'>checked="checked"</s:if> onchange="toggleDateTextfields($(this));"><s:property value="openLabel"/>
                                    </td>
                            </s:if>
                            <s:else>
                            <input type="radio" name="functionTable_<s:property value="#status.index + 1" />_date_1_radio_<s:property value="#status2.index + 1" />" value="open" <s:if test='%{#currentDateRow.radioValue1 == "open"}'>checked="checked"</s:if> onchange="toggleDateTextfields($(this));"><s:property value="openLabel"/>&nbsp;
                                </td>
                                <td></td>
                                <td></td>
                        </s:else>
                        <tr id="trDate_iso_<s:property value="#status2.index + 1" />">
                            <td><s:property value="getText('eaccpf.commons.iso.date')" />:</td>
                            <td>
                                <table style="width: 50%;">
                                    <tr>
                                        <td style="padding: 0px;"><input type="text" title="YYYY" id="date_1_Year" name="functionTable_<s:property value="#status.index + 1" />_date_1_Year_<s:property value="#status2.index + 1" />" <s:if test='%{#currentDateRow.radioValue1 != "known"}'>disabled="disabled"</s:if> <s:if test="%{#currentDateRow.standardDate1.year != 0}">value="<s:property value="#currentDateRow.standardDate1.year" />"</s:if> size="4" maxlength="4" onchange="validateIsoDates($(this), '<s:property value="getText('eaccpf.commons.error.no.standard.date')" />', '<s:property value="getText('eaccpf.commons.error.no.standard.dateRange')" />');" /></td>
                                            <td style="padding: 0px;">&ndash;</td>
                                                <td style="padding: 0px;"><input type="text" title="MM" id="date_1_Month" name="functionTable_<s:property value="#status.index + 1" />_date_1_Month_<s:property value="#status2.index + 1" />" <s:if test='%{#currentDateRow.radioValue1 != "known"}'>disabled="disabled"</s:if> <s:if test="%{#currentDateRow.standardDate1.month != 0}">value="<s:if test="%{#currentDateRow.standardDate1.month < 10}">0</s:if><s:property value="#currentDateRow.standardDate1.month" />"</s:if> size="2" maxlength="2" onchange="validateIsoDates($(this), '<s:property value="getText('eaccpf.commons.error.no.standard.date')" />', '<s:property value="getText('eaccpf.commons.error.no.standard.dateRange')" />');" /></td>
                                            <td style="padding: 0px;">&ndash;</td>
                                                <td style="padding: 0px;"><input type="text" title="DD" id="date_1_Day" name="functionTable_<s:property value="#status.index + 1" />_date_1_Day_<s:property value="#status2.index + 1" />" <s:if test='%{#currentDateRow.radioValue1 != "known"}'>disabled="disabled"</s:if> <s:if test="%{#currentDateRow.standardDate1.day != 0}">value="<s:if test="%{#currentDateRow.standardDate1.day < 10}">0</s:if><s:property value="#currentDateRow.standardDate1.day" />"</s:if> size="2" maxlength="2" onchange="validateIsoDates($(this), '<s:property value="getText('eaccpf.commons.error.no.standard.date')" />', '<s:property value="getText('eaccpf.commons.error.no.standard.dateRange')" />');" /></td>
                                        </tr>
                                        <tr>
                                            <td style="padding: 0px;">YYYY</td>
                                            <td style="padding: 0px;">&ndash;</td>
                                            <td style="padding: 0px;">MM</td>
                                            <td style="padding: 0px;">&ndash;</td>
                                            <td style="padding: 0px;">DD</td>
                                        </tr>
                                    </table>
                                </td>
                                <td></td>
                            <s:if test="#currentDateRow.dateContent2 != null or #currentDateRow.standardDate2 != null">
                                <td>
                                    <table style="width: 50%;">
                                        <tr>
                                            <td style="padding: 0px;"><input type="text" title="YYYY" id="date_2_Year" name="functionTable_<s:property value="#status.index + 1" />_date_2_Year_<s:property value="#status2.index + 1" />" <s:if test='%{#currentDateRow.radioValue2 != "known"}'>disabled="disabled"</s:if> <s:if test="%{#currentDateRow.standardDate2.year != 0}">value="<s:property value="#currentDateRow.standardDate2.year" />"</s:if> size="4" maxlength="4" onchange="validateIsoDates($(this), '<s:property value="getText('eaccpf.commons.error.no.standard.date')" />', '<s:property value="getText('eaccpf.commons.error.no.standard.dateRange')" />');" /></td>
                                                <td style="padding: 0px;">&ndash;</td>
                                                    <td style="padding: 0px;"><input type="text" title="MM" id="date_2_Month" name="functionTable_<s:property value="#status.index + 1" />_date_2_Month_<s:property value="#status2.index + 1" />" <s:if test='%{#currentDateRow.radioValue2 != "known"}'>disabled="disabled"</s:if> <s:if test="%{#currentDateRow.standardDate2.month != 0}">value="<s:if test="%{#currentDateRow.standardDate2.month < 10}">0</s:if><s:property value="#currentDateRow.standardDate2.month" />"</s:if>size="2" maxlength="2" onchange="validateIsoDates($(this), '<s:property value="getText('eaccpf.commons.error.no.standard.date')" />', '<s:property value="getText('eaccpf.commons.error.no.standard.dateRange')" />');" /></td>
                                                <td style="padding: 0px;">&ndash;</td>
                                                    <td style="padding: 0px;"><input type="text" title="DD" id="date_2_Day" name="functionTable_<s:property value="#status.index + 1" />_date_2_Day_<s:property value="#status2.index + 1" />" <s:if test='%{#currentDateRow.radioValue2 != "known"}'>disabled="disabled"</s:if> <s:if test="%{#currentDateRow.standardDate2.day != 0}">value="<s:if test="%{#currentDateRow.standardDate2.day < 10}">0</s:if><s:property value="#currentDateRow.standardDate2.day" />"</s:if>size="2" maxlength="2" onchange="validateIsoDates($(this), '<s:property value="getText('eaccpf.commons.error.no.standard.date')" />', '<s:property value="getText('eaccpf.commons.error.no.standard.dateRange')" />');" /></td>
                                            </tr>
                                            <tr>
                                                <td style="padding: 0px;">YYYY</td>
                                                <td style="padding: 0px;">&ndash;</td>
                                                <td style="padding: 0px;">MM</td>
                                                <td style="padding: 0px;">&ndash;</td>
                                                <td style="padding: 0px;">DD</td>
                                            </tr>
                                        </table>
                                    </td>
                            </s:if>
                            <s:else><td></td></s:else>
                            </tr>
                    </s:iterator>
                </s:if>
                <tr>
                    <td><label><s:property value="getText('eaccpf.description.adddatesofuseforthefunction')" /></label></td>
                    <td><input type="button" value="<s:property value="getText('eaccpf.commons.add.single.date')" />" id="addFunctionDate" onclick="addDateOrDateRangeFunction(this.id, $(this).parent().parent().parent().parent().attr('id'), '<s:property value="getText('eaccpf.commons.date')" />', '<s:property value="getText('eaccpf.commons.from.date')" />', '<s:property value="getText('eaccpf.commons.to.date')" />', '<s:property value="getText('eaccpf.commons.unknown.date')" />', '<s:property value="getText('eaccpf.commons.iso.date')" />')" />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                        <input type="button" value="<s:property value="getText('eaccpf.commons.add.range.date')" />" id="addFunctionDateRange" onclick="addDateOrDateRangeFunction(this.id, $(this).parent().parent().parent().parent().attr('id'), '<s:property value="getText('eaccpf.commons.date')" />', '<s:property value="getText('eaccpf.commons.from.date')" />', '<s:property value="getText('eaccpf.commons.to.date')" />', '<s:property value="getText('eaccpf.commons.unknown.date')" />', '<s:property value="getText('eaccpf.commons.iso.date')" />')" /></td>
                        <s:if test="%{#current.dates.size() > 0}">
                        <td><input type="hidden" id="functionTable_<s:property value="#status.index + 1" />_rows" name="functionTable_<s:property value="#status.index + 1" />_rows" value="<s:property value="#current.dates.size()" />" /></td>
                        </s:if>
                        <s:else>
                        <td><input type="hidden" id="functionTable_<s:property value="#status.index + 1" />_rows" name="functionTable_<s:property value="#status.index + 1" />_rows" value="0" /></td>
                        </s:else>
                    <td></td>
                </tr>
            </table>
        </s:iterator>
    </s:if>
    <s:else>
        <table id="functionTable_1" class="tablePadding">
            <tr>
                <th class="sectionHeader" colspan="4"><s:property value="getText('eaccpf.description.function')" /></th>
            </tr>
            <tr>
                <td><label for="function"><s:property value="getText('eaccpf.description.function')" />:</label></td>
                <td><input type="text" id="function" name="function_1" /></td>
                <td><label for="functionLanguage"><s:property value="getText('eaccpf.description.selectlanguage')" />:</label></td>
                <td>
                    <select id="functionLanguage" name="functionLanguage_1" >
                        <s:iterator value="languages" var="language">
                            <option value='<s:property value="#language.key"/>'<s:if test='%{#language.key == defaultLanguage}'>selected="selected"</s:if>><s:property value="#language.value"/></option>
                        </s:iterator>
                    </select>
                </td>
            </tr>
            <tr>
                <td><label for="linkFunctionVocab"><s:property value="getText('eaccpf.description.linktovocabulary')" />:</label></td>
                <td><input type="text" id="linkFunctionVocab" name="linkFunctionVocab_1" /></td>
                <td colspan="2"></td>
            </tr>
            <tr>
                <td><label for="functionDescription"><s:property value="getText('eaccpf.description.descriptionfunction')" />:</label></td>
                <td colspan="3"><textarea id="functionDescription" name="functionDescription_1"></textarea></td>
            </tr>
            <tr id="trPlaceFunction_1">
                <td><label for="textPlaceFunction"><s:property value="getText('eaccpf.description.place.name')" />:</label></td>
                <td><input type="text" id="textPlaceFunction" name="functionTable_1_place_1" /></td>
                <td><label for="functionCountry"><s:property value="getText('eaccpf.description.country')" />:</label></td>
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
                <td><label><s:property value="getText('eaccpf.description.adddatesofuseforthefunction')" />:</label></td>
                <td><input type="button" value="<s:property value="getText('eaccpf.commons.add.single.date')" />" id="addFunctionDate" onclick="addDateOrDateRangeFunction(this.id, $(this).parent().parent().parent().parent().attr('id'), '<s:property value="getText('eaccpf.commons.date')" />', '<s:property value="getText('eaccpf.commons.from.date')" />', '<s:property value="getText('eaccpf.commons.to.date')" />', '<s:property value="getText('eaccpf.commons.date.type')" />', '<s:property value="getText('eaccpf.commons.date.known')" />', '<s:property value="getText('eaccpf.commons.unknown.date')" />', '<s:property value="openLabel" />', '<s:property value="getText('eaccpf.commons.iso.date')" />', '<s:property value="getText('eaccpf.commons.error.no.standard.date')" />', '<s:property value="getText('eaccpf.commons.error.no.standard.dateRange')" />')" />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <input type="button" value="<s:property value="getText('eaccpf.commons.add.range.date')" />" id="addFunctionDateRange" onclick="addDateOrDateRangeFunction(this.id, $(this).parent().parent().parent().parent().attr('id'), '<s:property value="getText('eaccpf.commons.date')" />', '<s:property value="getText('eaccpf.commons.from.date')" />', '<s:property value="getText('eaccpf.commons.to.date')" />', '<s:property value="getText('eaccpf.commons.date.type')" />', '<s:property value="getText('eaccpf.commons.date.known')" />', '<s:property value="getText('eaccpf.commons.unknown.date')" />', '<s:property value="openLabel" />', '<s:property value="getText('eaccpf.commons.iso.date')" />', '<s:property value="getText('eaccpf.commons.error.no.standard.date')" />', '<s:property value="getText('eaccpf.commons.error.no.standard.dateRange')" />')" /></td>
                <td><input type="hidden" id="functionTable_1_rows" name="functionTable_1_rows" value="0" /></td>
                <td></td>
            </tr>
        </table>
    </s:else>
    <table id="addFunctionButtonPanel" class="tablePadding">
        <tr>
            <td><input type="button" value="<s:property value="getText('eaccpf.description.button.addFurtherFunction')" />" id="addFunctionButton" onclick="addFunction('<s:property value="defaultLanguage" />', '<s:property value="getText('eaccpf.commons.error.empty.function')" />');" /></td>
        </tr>
    </table>
    <s:if test="%{loader.occupations.size() > 0}">
        <s:iterator var="current" value="loader.occupations" status="status">
            <table id="occupationTable_<s:property value="#status.index + 1" />" class="tablePadding">
                <tr>
                    <th class="sectionHeader" colspan="4"><s:property value="getText('eaccpf.description.ocupation')" /></th>
                </tr>
                <tr>
                    <td><label for="occupation"><s:property value="getText('eaccpf.description.ocupation')" /></label></td>
                    <td><input type="text" id="occupation" name="occupation_<s:property value="#status.index + 1" />" value="<s:property value="#current.occupationName" />" /></td>
                    <td><label for="occupationLanguage"><s:property value="getText('eaccpf.description.selectlanguage')" /></label></td>
                    <td>
                        <select id="occupationLanguage" name="occupationLanguage_<s:property value="#status.index + 1" />" >
                            <s:iterator value="languages" var="language">
                                <option value='<s:property value="#language.key"/>'<s:if test='%{#language.key == #current.languageCode}'>selected="selected"</s:if>><s:property value="#language.value"/></option>
                            </s:iterator>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td><label for="linkOccupationVocab"><s:property value="getText('eaccpf.description.linktocontrolledocupations')" /></label></td>
                    <td><input type="text" id="linkOccupationVocab" name="linkOccupationVocab_<s:property value="#status.index + 1" />" value="<s:property value="#current.vocabLink" />" /></td>
                    <td colspan="2"></td>
                </tr>
                <tr>
                    <td><label for="occupationDescription"><s:property value="getText('eaccpf.description.descriptionocupations')" /></label></td>
                    <td colspan="3"><textarea id="occupationDescription" name="occupationDescription_<s:property value="#status.index + 1" />"><s:property value="#current.description" /></textarea></td>
                </tr>
                <s:if test="%{#current.places.size() > 0}">
                    <s:iterator var="currentPlace" value="#current.places" status="status2">
                        <s:iterator var="currentCountryCode" value="#current.countryCodes" status="status3">
                            <s:if test="%{#status2.index == #status3.index}">
                                <tr id="trPlaceOccupation_<s:property value="#status.index + 1" />">
                                    <td><label for="textPlaceOccupation"><s:property value="getText('eaccpf.description.place')" /></label></td>
                                    <td><input type="text" id="textPlaceOccupation" name="occupationTable_<s:property value="#status.index + 1" />_place_<s:property value="#status2.index + 1" />" value="<s:property value="#currentPlace" />" /></td>
                                    <td><label for="occupationCountry"><s:property value="getText('eaccpf.description.country')" /></label></td>
                                    <td>
                                        <select id="occupationCountry" name="occupationTable_<s:property value="#status.index + 1" />_country_<s:property value="#status2.index + 1" />" >
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
                        <td><label for="textPlaceOccupation"><s:property value="getText('eaccpf.description.place')" /></label></td>
                        <td><input type="text" id="textPlaceOccupation" name="occupationTable_<s:property value="#status.index + 1" />_place_1" /></td>
                        <td><label for="occupationCountry"><s:property value="getText('eaccpf.description.country')" /></label></td>
                        <td>
                            <select id="occupationCountry" name="occupationTable_<s:property value="#status.index + 1" />_country_1" >
                                <s:iterator value="countryList" var="list">
                                    <option value='<s:property value="#list.key"/>'><s:property value="#list.value"/></option>
                                </s:iterator>
                            </select>
                        </td>
                    </tr>
                </s:else>
                <tr id="trPlaceOccupationButton">
                    <td><input type="button" value="<s:property value="getText('eaccpf.description.button.addFurtherPlaceOcupation')" />" id="addPlaceOccupationButton" onclick="addPlaceOccupation($(this).parent().parent().parent().parent().attr('id'), '<s:property value="getText('eaccpf.commons.error.empty.place')" />');" /></td>
                    <td colspan="3"></td>
                </tr>
                <s:if test="%{#current.dates.size() > 0}">
                    <s:iterator var="currentDateRow" value="#current.dates" status="status2">
                        <tr id="trDate_text_<s:property value="#status2.index + 1" />">
                            <td>
                                <s:if test="#currentDateRow.dateContent2 != null">
                                    <s:property value="getText('eaccpf.commons.from.date')" />:
                                </s:if>
                                <s:else>
                                    <s:property value="getText('eaccpf.commons.date')" />:
                                </s:else>
                            </td>
                            <td>
                                <input type="text" id="date_1" name="occupationTable_<s:property value="#status.index + 1" />_date_1_<s:property value="#status2.index + 1" />" value="<s:property value="#currentDateRow.dateContent1" />" <s:if test='%{#currentDateRow.radioValue1 != "known"}'>disabled="disabled"</s:if> onchange="parseDateToISO($(this).attr('value'), $(this).parent().parent().parent().parent().attr('id'), $(this).parent().parent().attr('id'), $(this).attr('id'));"><br />
                                </td>
                            <s:if test="#currentDateRow.dateContent2 != null or #currentDateRow.standardDate2 != null">
                                <td><s:property value="getText('eaccpf.commons.to.date')" />:</td>
                                <td>
                                    <input type="text" id="date_2" name="occupationTable_<s:property value="#status.index + 1" />_date_2_<s:property value="#status2.index + 1" />" value="<s:property value="#currentDateRow.dateContent2" />" <s:if test='%{#currentDateRow.radioValue2 != "known"}'>disabled="disabled"</s:if> onchange="parseDateToISO($(this).attr('value'), $(this).parent().parent().parent().parent().attr('id'), $(this).parent().parent().attr('id'), $(this).attr('id'));"><br />
                                    </td>
                            </s:if>
                            <s:else>
                                <td></td>
                                <td></td>
                            </s:else>
                        </tr>
                        <tr id="trDate_radio_<s:property value="#status2.index + 1" />">
                            <td>
                                <s:property value="getText('eaccpf.commons.date.type')"/>:
                            </td>
                            <td>
                                <input type="radio" name="occupationTable_<s:property value="#status.index + 1" />_date_1_radio_<s:property value="#status2.index + 1" />" value="known" <s:if test='%{#currentDateRow.radioValue1 == "known"}'>checked="checked"</s:if> onchange="toggleDateTextfields($(this));"><s:property value="getText('eaccpf.commons.date.known')"/>&nbsp;
                                <input type="radio" name="occupationTable_<s:property value="#status.index + 1" />_date_1_radio_<s:property value="#status2.index + 1" />" value="unknown" <s:if test='%{#currentDateRow.radioValue1 == "unknown"}'>checked="checked"</s:if> onchange="toggleDateTextfields($(this));"><s:property value="getText('eaccpf.commons.unknown.date')"/>&nbsp;
                                <s:if test="#currentDateRow.dateContent2 != null or #currentDateRow.standardDate2 != null">
                                </td>
                                <td></td>
                                <td>
                                    <input type="radio" name="occupationTable_<s:property value="#status.index + 1" />_date_2_radio_<s:property value="#status2.index + 1" />" value="known" <s:if test='%{#currentDateRow.radioValue2 == "known"}'>checked="checked"</s:if> onchange="toggleDateTextfields($(this));"><s:property value="getText('eaccpf.commons.date.known')"/>&nbsp;
                                    <input type="radio" name="occupationTable_<s:property value="#status.index + 1" />_date_2_radio_<s:property value="#status2.index + 1" />" value="unknown" <s:if test='%{#currentDateRow.radioValue2 == "unknown"}'>checked="checked"</s:if> onchange="toggleDateTextfields($(this));"><s:property value="getText('eaccpf.commons.unknown.date')"/>&nbsp;
                                    <input type="radio" name="occupationTable_<s:property value="#status.index + 1" />_date_2_radio_<s:property value="#status2.index + 1" />" value="open" <s:if test='%{#currentDateRow.radioValue2 == "open"}'>checked="checked"</s:if> onchange="toggleDateTextfields($(this));"><s:property value="openLabel"/>
                                    </td>
                            </s:if>
                            <s:else>
                            <input type="radio" name="occupationTable_<s:property value="#status.index + 1" />_date_1_radio_<s:property value="#status2.index + 1" />" value="open" <s:if test='%{#currentDateRow.radioValue1 == "open"}'>checked="checked"</s:if> onchange="toggleDateTextfields($(this));"><s:property value="openLabel"/>&nbsp;
                                </td>
                                <td></td>
                                <td></td>
                        </s:else>
                        <tr id="trDate_iso_<s:property value="#status2.index + 1" />">
                            <td><s:property value="getText('eaccpf.commons.iso.date')" />:</td>
                            <td>
                                <table style="width: 50%;">
                                    <tr>
                                        <td style="padding: 0px;"><input type="text" title="YYYY" id="date_1_Year" name="occupationTable_<s:property value="#status.index + 1" />_date_1_Year_<s:property value="#status2.index + 1" />" <s:if test='%{#currentDateRow.radioValue1 != "known"}'>disabled="disabled"</s:if> <s:if test="%{#currentDateRow.standardDate1.year != 0}">value="<s:property value="#currentDateRow.standardDate1.year" />"</s:if> size="4" maxlength="4" onchange="validateIsoDates($(this), '<s:property value="getText('eaccpf.commons.error.no.standard.date')" />', '<s:property value="getText('eaccpf.commons.error.no.standard.dateRange')" />');" /></td>
                                            <td style="padding: 0px;">&ndash;</td>
                                                <td style="padding: 0px;"><input type="text" title="MM" id="date_1_Month" name="occupationTable_<s:property value="#status.index + 1" />_date_1_Month_<s:property value="#status2.index + 1" />" <s:if test='%{#currentDateRow.radioValue1 != "known"}'>disabled="disabled"</s:if> <s:if test="%{#currentDateRow.standardDate1.month != 0}">value="<s:if test="%{#currentDateRow.standardDate1.month < 10}">0</s:if><s:property value="#currentDateRow.standardDate1.month" />"</s:if> size="2" maxlength="2" onchange="validateIsoDates($(this), '<s:property value="getText('eaccpf.commons.error.no.standard.date')" />', '<s:property value="getText('eaccpf.commons.error.no.standard.dateRange')" />');" /></td>
                                            <td style="padding: 0px;">&ndash;</td>
                                                <td style="padding: 0px;"><input type="text" title="DD" id="date_1_Day" name="occupationTable_<s:property value="#status.index + 1" />_date_1_Day_<s:property value="#status2.index + 1" />" <s:if test='%{#currentDateRow.radioValue1 != "known"}'>disabled="disabled"</s:if> <s:if test="%{#currentDateRow.standardDate1.day != 0}">value="<s:if test="%{#currentDateRow.standardDate1.day < 10}">0</s:if><s:property value="#currentDateRow.standardDate1.day" />"</s:if> size="2" maxlength="2" onchange="validateIsoDates($(this), '<s:property value="getText('eaccpf.commons.error.no.standard.date')" />', '<s:property value="getText('eaccpf.commons.error.no.standard.dateRange')" />');" /></td>
                                        </tr>
                                        <tr>
                                            <td style="padding: 0px;">YYYY</td>
                                            <td style="padding: 0px;">&ndash;</td>
                                            <td style="padding: 0px;">MM</td>
                                            <td style="padding: 0px;">&ndash;</td>
                                            <td style="padding: 0px;">DD</td>
                                        </tr>
                                    </table>
                                </td>
                                <td></td>
                            <s:if test="#currentDateRow.dateContent2 != null or #currentDateRow.standardDate2 != null">
                                <td>
                                    <table style="width: 50%;">
                                        <tr>
                                            <td style="padding: 0px;"><input type="text" title="YYYY" id="date_2_Year" name="occupationTable_<s:property value="#status.index + 1" />_date_2_Year_<s:property value="#status2.index + 1" />" <s:if test='%{#currentDateRow.radioValue2 != "known"}'>disabled="disabled"</s:if> <s:if test="%{#currentDateRow.standardDate2.year != 0}">value="<s:property value="#currentDateRow.standardDate2.year" />"</s:if> size="4" maxlength="4" onchange="validateIsoDates($(this), '<s:property value="getText('eaccpf.commons.error.no.standard.date')" />', '<s:property value="getText('eaccpf.commons.error.no.standard.dateRange')" />');" /></td>
                                                <td style="padding: 0px;">&ndash;</td>
                                                    <td style="padding: 0px;"><input type="text" title="MM" id="date_2_Month" name="occupationTable_<s:property value="#status.index + 1" />_date_2_Month_<s:property value="#status2.index + 1" />" <s:if test='%{#currentDateRow.radioValue2 != "known"}'>disabled="disabled"</s:if> <s:if test="%{#currentDateRow.standardDate2.month != 0}">value="<s:if test="%{#currentDateRow.standardDate2.month < 10}">0</s:if><s:property value="#currentDateRow.standardDate2.month" />"</s:if>size="2" maxlength="2" onchange="validateIsoDates($(this), '<s:property value="getText('eaccpf.commons.error.no.standard.date')" />', '<s:property value="getText('eaccpf.commons.error.no.standard.dateRange')" />');" /></td>
                                                <td style="padding: 0px;">&ndash;</td>
                                                    <td style="padding: 0px;"><input type="text" title="DD" id="date_2_Day" name="occupationTable_<s:property value="#status.index + 1" />_date_2_Day_<s:property value="#status2.index + 1" />" <s:if test='%{#currentDateRow.radioValue2 != "known"}'>disabled="disabled"</s:if> <s:if test="%{#currentDateRow.standardDate2.day != 0}">value="<s:if test="%{#currentDateRow.standardDate2.day < 10}">0</s:if><s:property value="#currentDateRow.standardDate2.day" />"</s:if>size="2" maxlength="2" onchange="validateIsoDates($(this), '<s:property value="getText('eaccpf.commons.error.no.standard.date')" />', '<s:property value="getText('eaccpf.commons.error.no.standard.dateRange')" />');" /></td>
                                            </tr>
                                            <tr>
                                                <td style="padding: 0px;">YYYY</td>
                                                <td style="padding: 0px;">&ndash;</td>
                                                <td style="padding: 0px;">MM</td>
                                                <td style="padding: 0px;">&ndash;</td>
                                                <td style="padding: 0px;">DD</td>
                                            </tr>
                                        </table>
                                    </td>
                            </s:if>
                            <s:else><td></td></s:else>
                            </tr>
                    </s:iterator>
                </s:if>
                <tr>
                    <td><label><s:property value="getText('eaccpf.description.adddatesofusefortheoccupation')" /></label></td>
                    <td><input type="button" value="<s:property value="getText('eaccpf.commons.add.single.date')" />" id="addOccupationDate" onclick="addDateOrDateRangeOccupation(this.id, $(this).parent().parent().parent().parent().attr('id'), '<s:property value="getText('eaccpf.commons.date')" />', '<s:property value="getText('eaccpf.commons.from.date')" />', '<s:property value="getText('eaccpf.commons.to.date')" />', '<s:property value="getText('eaccpf.commons.unknown.date')" />', '<s:property value="getText('eaccpf.commons.iso.date')" />')" />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                        <input type="button" value="<s:property value="getText('eaccpf.commons.add.range.date')" />" id="addOccupationDateRange" onclick="addDateOrDateRangeOccupation(this.id, $(this).parent().parent().parent().parent().attr('id'), '<s:property value="getText('eaccpf.commons.date')" />', '<s:property value="getText('eaccpf.commons.from.date')" />', '<s:property value="getText('eaccpf.commons.to.date')" />', '<s:property value="getText('eaccpf.commons.unknown.date')" />', '<s:property value="getText('eaccpf.commons.iso.date')" />')" /></td>
                        <s:if test="%{#current.dates.size() > 0}">
                        <td><input type="hidden" id="occupationTable_<s:property value="#status.index + 1" />_rows" name="occupationTable_<s:property value="#status.index + 1" />_rows" value="<s:property value="#current.dates.size()" />" /></td>
                        </s:if>
                        <s:else>
                        <td><input type="hidden" id="occupationTable_<s:property value="#status.index + 1" />_rows" name="occupationTable_<s:property value="#status.index + 1" />_rows" value="0" /></td>
                        </s:else>
                    <td></td>
                </tr>
            </table>
        </s:iterator>
    </s:if>
    <s:else>
        <table id="occupationTable_1" class="tablePadding">
            <tr>
                <th class="sectionHeader" colspan="4"><s:property value="getText('eaccpf.description.ocupation')" /></th>
            </tr>
            <tr>
                <td><label for="occupation"><s:property value="getText('eaccpf.description.ocupation')" />:</label></td>
                <td><input type="text" id="occupation" name="occupation_1" /></td>
                <td><label for="occupationLanguage"><s:property value="getText('eaccpf.description.selectlanguage')" />:</label></td>
                <td>
                    <select id="occupationLanguage" name="occupationLanguage_1" >
                        <s:iterator value="languages" var="language">
                            <option value='<s:property value="#language.key"/>'<s:if test='%{#language.key == defaultLanguage}'>selected="selected"</s:if>><s:property value="#language.value"/></option>
                        </s:iterator>
                    </select>
                </td>
            </tr>
            <tr>
                <td><label for="linkOccupationVocab"><s:property value="getText('eaccpf.description.linktocontrolledocupations')" />:</label></td>
                <td><input type="text" id="linkOccupationVocab" name="linkOccupationVocab_1" /></td>
                <td colspan="2"></td>
            </tr>
            <tr>
                <td><label for="occupationDescription"><s:property value="getText('eaccpf.description.descriptionocupations')" />:</label></td>
                <td colspan="3"><textarea id="occupationDescription" name="occupationDescription_1"></textarea></td>
            </tr>
            <tr id="trPlaceOccupation_1">
                <td><label for="textPlaceOccupation"><s:property value="getText('eaccpf.description.place.name')" />:</label></td>
                <td><input type="text" id="textPlaceOccupation" name="occupationTable_1_place_1" /></td>
                <td><label for="occupationCountry"><s:property value="getText('eaccpf.description.country')" />:</label></td>
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
                <td><label><s:property value="getText('eaccpf.description.adddatesofusefortheoccupation')" />:</label></td>
                <td><input type="button" value="<s:property value="getText('eaccpf.commons.add.single.date')" />" id="addOccupationDate" onclick="addDateOrDateRangeOccupation(this.id, $(this).parent().parent().parent().parent().attr('id'), '<s:property value="getText('eaccpf.commons.date')" />', '<s:property value="getText('eaccpf.commons.from.date')" />', '<s:property value="getText('eaccpf.commons.to.date')" />', '<s:property value="getText('eaccpf.commons.date.type')" />', '<s:property value="getText('eaccpf.commons.date.known')" />', '<s:property value="getText('eaccpf.commons.unknown.date')" />', '<s:property value="openLabel" />', '<s:property value="getText('eaccpf.commons.iso.date')" />', '<s:property value="getText('eaccpf.commons.error.no.standard.date')" />', '<s:property value="getText('eaccpf.commons.error.no.standard.dateRange')" />')" />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <input type="button" value="<s:property value="getText('eaccpf.commons.add.range.date')" />" id="addOccupationDateRange" onclick="addDateOrDateRangeOccupation(this.id, $(this).parent().parent().parent().parent().attr('id'), '<s:property value="getText('eaccpf.commons.date')" />', '<s:property value="getText('eaccpf.commons.from.date')" />', '<s:property value="getText('eaccpf.commons.to.date')" />', '<s:property value="getText('eaccpf.commons.date.type')" />', '<s:property value="getText('eaccpf.commons.date.known')" />', '<s:property value="getText('eaccpf.commons.unknown.date')" />', '<s:property value="openLabel" />', '<s:property value="getText('eaccpf.commons.iso.date')" />', '<s:property value="getText('eaccpf.commons.error.no.standard.date')" />', '<s:property value="getText('eaccpf.commons.error.no.standard.dateRange')" />')" /></td>
                <td><input type="hidden" id="occupationTable_1_rows" name="occupationTable_1_rows" value="0" /></td>
                <td></td>
            </tr>
        </table>
    </s:else>
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
    <s:if test="%{loader.genealogies.size() > 0}">
        <s:iterator var="current" value="loader.genealogies" status="status">
            <table id="genealogyContent_<s:property value="#status.index + 1" />" class="tablePadding">
                <tr>
                    <td><label for="genealogyDescription"><s:property value="getText('eaccpf.description.description')" />:</label></td>
                    <td rowspan="3"><textarea id="genealogyDescription" name="genealogyDescription_<s:property value="#status.index + 1" />"><s:property value="#current.paragraph" /></textarea></td>
                    <td><label for="genealogyLanguage"><s:property value="getText('eaccpf.description.selectlanguage')" />:</label></td>
                    <td>
                        <select id="genealogyLanguage" name="genealogyLanguage_<s:property value="#status.index + 1" />" >
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
                <td><label for="genealogyDescription"><s:property value="getText('eaccpf.description.description')" />:</label></td>
                <td rowspan="3"><textarea id="genealogyDescription" name="genealogyDescription_1"></textarea></td>
                <td><label for="genealogyLanguage"><s:property value="getText('eaccpf.description.selectlanguage')" />:</label></td>
                <td>
                    <select id="genealogyLanguage" name="genealogyLanguage_1" >
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
            <td><input type="button" value="<s:property value="getText('eaccpf.description.button.addfurthergenealogydescription')" />" id="addGenealogyButton" onclick="addGenealogy('<s:property value="getText('eaccpf.commons.error.empty.genealogy')" />');" /></td>
        </tr>
    </table>
    <table class="tablePadding">
        <tr>
            <th class="sectionHeader"><s:property value="getText('eaccpf.description.biography')" /></th>
        </tr>
    </table>
    <s:if test="%{loader.biographies.size() > 0}">
        <s:iterator var="current" value="loader.biographies" status="status">
            <table id="biographyContent_<s:property value="#status.index + 1" />" class="tablePadding">
                <tr>
                    <td><label for="biographyDescription"><s:property value="getText('eaccpf.description.description')" />:</label></td>
                    <td rowspan="3"><textarea id="biographyDescription" name="biographyDescription_<s:property value="#status.index + 1" />"><s:property value="#current.paragraph" /></textarea></td>
                    <td><label for="biographyLanguage"><s:property value="getText('eaccpf.description.selectlanguage')" />:</label></td>
                    <td>
                        <select id="biographyLanguage" name="biographyLanguage_<s:property value="#status.index + 1" />" >
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
                <td><label for="biographyDescription"><s:property value="getText('eaccpf.description.description')" />:</label></td>
                <td rowspan="3"><textarea id="biographyDescription" name="biographyDescription_1"></textarea></td>
                <td><label for="biographyLanguage"><s:property value="getText('eaccpf.description.selectlanguage')" />:</label></td>
                <td>
                    <select id="biographyLanguage" name="biographyLanguage_1" >
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
            <td><input type="button" value="<s:property value="getText('eaccpf.description.button.addfurtherbiographydescription')" />" id="addBiographyButton" onclick="addBiography('<s:property value="getText('eaccpf.commons.error.empty.biohist')" />');" /></td>
        </tr>
    </table>
</div>