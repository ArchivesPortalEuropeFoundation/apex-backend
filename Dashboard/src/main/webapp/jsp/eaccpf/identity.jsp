<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="/struts-tags" prefix="s" %>

<div id="headerContainer">
</div>

<div id="identityTabContent">
    <input type="hidden" name="useMode" value='<s:property value="useMode" />' />
    <input type="hidden" name="cpfType" value='<s:property value="cpfType" />' />
    <h2 class="tablePadding"><s:property value="cpfTypeDescriptionText" /></h2>
    <s:if test="%{loader.nameEntries.size() > 0}">
        <s:iterator var="current" value="loader.nameEntries" status="status">
            <table id="identityPersonName_<s:property value="#status.index + 1" />" class="tablePadding">
                <tr>
                    <th class="sectionHeader" colspan="4"><s:property value="getText('eaccpf.identity.name')" /></th>
                </tr>
                <s:iterator var="currentPart" value="#current.parts" status="statusPart">
                    <tr id="trNamePart_<s:property value="#statusPart.index + 1" />">
                        <td><label for="textPersonName"><s:if test="#statusPart.index == 0"><b></s:if><s:property value="getText('eaccpf.identity.name.full')" />:<s:if test="#statusPart.index == 0">*</b></s:if></label></td>
                        <td><input type="text" id="textPersonName" name="identityPersonName_<s:property value="#status.index + 1" />_part_<s:property value="#statusPart.index + 1" />" <s:if test="#statusPart.index == 0">required="required"</s:if> value="<s:property value="#currentPart.content" />" /></td>
                        <td><label for="identityComponentOfName"><s:property value="getText('eaccpf.identity.name.component')" />:</label></td>
                        <td>
                            <select id="identityComponentOfName" name="identityPersonName_<s:property value="#status.index + 1" />_comp_<s:property value="#statusPart.index + 1" />" <s:if test="#statusComponent.index == 0">required="required"</s:if>>
                                <s:iterator value="componentNameList" var="compNames">
                                    <option value='<s:property value="#compNames.key"/>' <s:if test='%{#compNames.key==#currentPart.localType}'>selected="selected"</s:if>><s:property value="#compNames.value"/></option>
                                </s:iterator>
                            </select>
                        </td>
                    </tr>
                </s:iterator>
                <tr>
                    <td><input type="button" value="<s:property value="getText('eaccpf.identity.add.part')" />" id="addAddressComponentButton" onclick="addPartName($(this).parent().parent().parent().parent().attr('id'), '<s:property value="getText('eaccpf.identity.error.empty.name')" />');" /></td>
                    <td colspan="3"></td>
                </tr>
                <tr id="trNameForm">
                    <td><label for="identityFormOfName"><s:property value="getText('eaccpf.identity.name.form')" />:</label></td>
                    <td>
                        <select id="identityFormOfName" name="identityFormOfName_<s:property value="#status.index + 1" />" required="required">
                            <s:iterator value="formNameList" var="formNames">
                                <option value='<s:property value="#formNames.key"/>' <s:if test='%{#formNames.key==#current.form}'>selected="selected"</s:if>><s:property value="#formNames.value"/></option>
                            </s:iterator>
                        </select>
                    </td>
                    <td><label for="identityNameLanguage"><s:property value="getText('eaccpf.commons.select.language')" />:</label></td>
                    <td>
                        <select id="identityNameLanguage" name="identityNameLanguage_<s:property value="#status.index + 1" />">
                            <s:iterator value="languages" var="language">
                                <option value='<s:property value="#language.key"/>' <s:if test='%{#language.key==#current.language}'>selected="selected"</s:if>><s:property value="#language.value"/></option>
                            </s:iterator>
                        </select>
                    </td>
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
                                <input type="text" id="date_1" name="identityPersonName_<s:property value="#status.index + 1" />_date_1_<s:property value="#status2.index + 1" />" value="<s:property value="#currentDateRow.dateContent1" />" <s:if test='%{#currentDateRow.radioValue1 != "known"}'>disabled="disabled"</s:if> onchange="parseDateToISO($(this).attr('value'), $(this).parent().parent().parent().parent().attr('id'), $(this).parent().parent().attr('id'), $(this).attr('id'));"><br />
                                </td>
                            <s:if test="#currentDateRow.dateContent2 != null or #currentDateRow.standardDate2 != null">
                                <td><s:property value="getText('eaccpf.commons.to.date')" />:</td>
                                <td>
                                    <input type="text" id="date_2" name="identityPersonName_<s:property value="#status.index + 1" />_date_2_<s:property value="#status2.index + 1" />" value="<s:property value="#currentDateRow.dateContent2" />" <s:if test='%{#currentDateRow.radioValue2 != "known"}'>disabled="disabled"</s:if> onchange="parseDateToISO($(this).attr('value'), $(this).parent().parent().parent().parent().attr('id'), $(this).parent().parent().attr('id'), $(this).attr('id'));"><br />
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
                                <input type="radio" name="identityPersonName_<s:property value="#status.index + 1" />_date_1_radio_<s:property value="#status2.index + 1" />" value="known" <s:if test='%{#currentDateRow.radioValue1 == "known"}'>checked="checked"</s:if> onchange="toggleDateTextfields($(this));"><s:property value="getText('eaccpf.commons.date.known')"/>&nbsp;
                                <input type="radio" name="identityPersonName_<s:property value="#status.index + 1" />_date_1_radio_<s:property value="#status2.index + 1" />" value="unknown" <s:if test='%{#currentDateRow.radioValue1 == "unknown"}'>checked="checked"</s:if> onchange="toggleDateTextfields($(this));"><s:property value="getText('eaccpf.commons.unknown.date')"/>&nbsp;
                                <s:if test="#currentDateRow.dateContent2 != null or #currentDateRow.standardDate2 != null">
                                </td>
                                <td></td>
                                <td>
                                    <input type="radio" name="identityPersonName_<s:property value="#status.index + 1" />_date_2_radio_<s:property value="#status2.index + 1" />" value="known" <s:if test='%{#currentDateRow.radioValue2 == "known"}'>checked="checked"</s:if> onchange="toggleDateTextfields($(this));"><s:property value="getText('eaccpf.commons.date.known')"/>&nbsp;
                                    <input type="radio" name="identityPersonName_<s:property value="#status.index + 1" />_date_2_radio_<s:property value="#status2.index + 1" />" value="unknown" <s:if test='%{#currentDateRow.radioValue2 == "unknown"}'>checked="checked"</s:if> onchange="toggleDateTextfields($(this));"><s:property value="getText('eaccpf.commons.unknown.date')"/>&nbsp;
                                    <input type="radio" name="identityPersonName_<s:property value="#status.index + 1" />_date_2_radio_<s:property value="#status2.index + 1" />" value="open" <s:if test='%{#currentDateRow.radioValue2 == "open"}'>checked="checked"</s:if> onchange="toggleDateTextfields($(this));"><s:property value="openLabel"/>
                                    </td>
                            </s:if>
                            <s:else>
                            <input type="radio" name="identityPersonName_<s:property value="#status.index + 1" />_date_1_radio_<s:property value="#status2.index + 1" />" value="open" <s:if test='%{#currentDateRow.radioValue1 == "open"}'>checked="checked"</s:if> onchange="toggleDateTextfields($(this));"><s:property value="openLabel"/>&nbsp;
                                </td>
                                <td></td>
                                <td></td>
                        </s:else>
                        <tr id="trDate_iso_<s:property value="#status2.index + 1" />">
                            <td><s:property value="getText('eaccpf.commons.iso.date')" />:</td>
                            <td>
                                <table style="width: 50%;">
                                    <tr>
                                        <td style="padding: 0px;"><input type="text" title="YYYY" id="date_1_Year" name="identityPersonName_<s:property value="#status.index + 1" />_date_1_Year_<s:property value="#status2.index + 1" />" <s:if test='%{#currentDateRow.radioValue1 != "known"}'>disabled="disabled"</s:if> <s:if test="%{#currentDateRow.standardDate1.year != 0}">value="<s:property value="#currentDateRow.standardDate1.year" />"</s:if> size="4" maxlength="4" onchange="validateIsoDates($(this), '<s:property value="getText('eaccpf.commons.error.no.standard.date')" />', '<s:property value="getText('eaccpf.commons.error.no.standard.dateRange')" />');" /></td>
                                            <td style="padding: 0px;">&ndash;</td>
                                                <td style="padding: 0px;"><input type="text" title="MM" id="date_1_Month" name="identityPersonName_<s:property value="#status.index + 1" />_date_1_Month_<s:property value="#status2.index + 1" />" <s:if test='%{#currentDateRow.radioValue1 != "known"}'>disabled="disabled"</s:if> <s:if test="%{#currentDateRow.standardDate1.month != 0}">value="<s:if test="%{#currentDateRow.standardDate1.month < 10}">0</s:if><s:property value="#currentDateRow.standardDate1.month" />"</s:if> size="2" maxlength="2" onchange="validateIsoDates($(this), '<s:property value="getText('eaccpf.commons.error.no.standard.date')" />', '<s:property value="getText('eaccpf.commons.error.no.standard.dateRange')" />');" /></td>
                                            <td style="padding: 0px;">&ndash;</td>
                                                <td style="padding: 0px;"><input type="text" title="DD" id="date_1_Day" name="identityPersonName_<s:property value="#status.index + 1" />_date_1_Day_<s:property value="#status2.index + 1" />" <s:if test='%{#currentDateRow.radioValue1 != "known"}'>disabled="disabled"</s:if> <s:if test="%{#currentDateRow.standardDate1.day != 0}">value="<s:if test="%{#currentDateRow.standardDate1.day < 10}">0</s:if><s:property value="#currentDateRow.standardDate1.day" />"</s:if> size="2" maxlength="2" onchange="validateIsoDates($(this), '<s:property value="getText('eaccpf.commons.error.no.standard.date')" />', '<s:property value="getText('eaccpf.commons.error.no.standard.dateRange')" />');" /></td>
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
                                            <td style="padding: 0px;"><input type="text" title="YYYY" id="date_2_Year" name="identityPersonName_<s:property value="#status.index + 1" />_date_2_Year_<s:property value="#status2.index + 1" />" <s:if test='%{#currentDateRow.radioValue2 != "known"}'>disabled="disabled"</s:if> <s:if test="%{#currentDateRow.standardDate2.year != 0}">value="<s:property value="#currentDateRow.standardDate2.year" />"</s:if> size="4" maxlength="4" onchange="validateIsoDates($(this), '<s:property value="getText('eaccpf.commons.error.no.standard.date')" />', '<s:property value="getText('eaccpf.commons.error.no.standard.dateRange')" />');" /></td>
                                                <td style="padding: 0px;">&ndash;</td>
                                                    <td style="padding: 0px;"><input type="text" title="MM" id="date_2_Month" name="identityPersonName_<s:property value="#status.index + 1" />_date_2_Month_<s:property value="#status2.index + 1" />" <s:if test='%{#currentDateRow.radioValue2 != "known"}'>disabled="disabled"</s:if> <s:if test="%{#currentDateRow.standardDate2.month != 0}">value="<s:if test="%{#currentDateRow.standardDate2.month < 10}">0</s:if><s:property value="#currentDateRow.standardDate2.month" />"</s:if>size="2" maxlength="2" onchange="validateIsoDates($(this), '<s:property value="getText('eaccpf.commons.error.no.standard.date')" />', '<s:property value="getText('eaccpf.commons.error.no.standard.dateRange')" />');" /></td>
                                                <td style="padding: 0px;">&ndash;</td>
                                                    <td style="padding: 0px;"><input type="text" title="DD" id="date_2_Day" name="identityPersonName_<s:property value="#status.index + 1" />_date_2_Day_<s:property value="#status2.index + 1" />" <s:if test='%{#currentDateRow.radioValue2 != "known"}'>disabled="disabled"</s:if> <s:if test="%{#currentDateRow.standardDate2.day != 0}">value="<s:if test="%{#currentDateRow.standardDate2.day < 10}">0</s:if><s:property value="#currentDateRow.standardDate2.day" />"</s:if>size="2" maxlength="2" onchange="validateIsoDates($(this), '<s:property value="getText('eaccpf.commons.error.no.standard.date')" />', '<s:property value="getText('eaccpf.commons.error.no.standard.dateRange')" />');" /></td>
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
                    <td><label><s:property value="getText('eaccpf.identity.dates.use')" />:</label></td>
                    <td><input type="button" value="<s:property value="getText('eaccpf.commons.add.single.date')" />" id="addNameDate" onclick="addDateOrDateRangeName(this.id, $(this).parent().parent().parent().parent().attr('id'), '<s:property value="getText('eaccpf.commons.date')" />', '<s:property value="getText('eaccpf.commons.from.date')" />', '<s:property value="getText('eaccpf.commons.to.date')" />', '<s:property value="getText('eaccpf.commons.date.type')" />', '<s:property value="getText('eaccpf.commons.date.known')" />', '<s:property value="getText('eaccpf.commons.unknown.date')" />', '<s:property value="openLabel" />', '<s:property value="getText('eaccpf.commons.iso.date')" />', '<s:property value="getText('eaccpf.commons.error.no.standard.date')" />', '<s:property value="getText('eaccpf.commons.error.no.standard.dateRange')" />')" />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                        <input type="button" value="<s:property value="getText('eaccpf.commons.add.range.date')" />" id="addNameDateRange" onclick="addDateOrDateRangeName(this.id, $(this).parent().parent().parent().parent().attr('id'), '<s:property value="getText('eaccpf.commons.date')" />', '<s:property value="getText('eaccpf.commons.from.date')" />', '<s:property value="getText('eaccpf.commons.to.date')" />', '<s:property value="getText('eaccpf.commons.date.type')" />', '<s:property value="getText('eaccpf.commons.date.known')" />', '<s:property value="getText('eaccpf.commons.unknown.date')" />', '<s:property value="openLabel" />', '<s:property value="getText('eaccpf.commons.iso.date')" />', '<s:property value="getText('eaccpf.commons.error.no.standard.date')" />', '<s:property value="getText('eaccpf.commons.error.no.standard.dateRange')" />')" /></td>
                        <s:if test="%{#current.dates.size() > 0}">
                        <td><input type="hidden" id="identityPersonName_<s:property value="#status.index + 1" />_rows" name="identityPersonName_<s:property value="#status.index + 1" />_rows" value="<s:property value="#current.dates.size()" />" /></td>
                        </s:if>
                        <s:else>
                        <td><input type="hidden" id="identityPersonName_<s:property value="#status.index + 1" />_rows" name="identityPersonName_<s:property value="#status.index + 1" />_rows" value="0" /></td>
                        </s:else>
                    <td></td>
                </tr>
            </table>
        </s:iterator>
    </s:if>
    <s:else>
        <table id="identityPersonName_1" class="tablePadding">
            <tr>
                <th class="sectionHeader" colspan="4"><s:property value="getText('eaccpf.identity.name')" /></th>
            </tr>
            <tr id="trNamePart_1">
                <td><label for="textPersonName"><b><s:property value="getText('eaccpf.identity.name.full')" />:*</b></label></td>
                <td><input type="text" id="textPersonName" name="identityPersonName_1_part_1" /></td>
                <td><label for="identityComponentOfName"><s:property value="getText('eaccpf.identity.name.component')" />:</label></td>
                <td>
                    <select id="identityComponentOfName" name="identityPersonName_1_comp_1">
                        <s:iterator value="componentNameList" var="compNames">
                            <option value='<s:property value="#compNames.key"/>' <s:if test='%{#compNames.key=="persname"}'>selected="selected"</s:if>><s:property value="#compNames.value"/></option>
                        </s:iterator>
                    </select>
                </td>
            </tr>
            <tr>
                <td><input type="button" value="<s:property value="getText('eaccpf.identity.add.part')" />" id="addAddressComponentButton" onclick="addPartName($(this).parent().parent().parent().parent().attr('id'), '<s:property value="getText('eaccpf.identity.error.empty.name')" />');" /></td>
                <td colspan="3"></td>
            </tr>
            <tr id="trNameForm">
                <td><label for="identityFormOfName"><s:property value="getText('eaccpf.identity.name.form')" />:</label></td>
                <td>
                    <select id="identityFormOfName" name="identityFormOfName_1">
                        <s:iterator value="formNameList" var="formNames">
                            <option value='<s:property value="#formNames.key"/>'><s:property value="#formNames.value"/></option>
                        </s:iterator>
                    </select>
                </td>
                <td><label for="identityNameLanguage"><s:property value="getText('eaccpf.commons.select.language')" />:</label></td>
                <td>
                    <select id="identityNameLanguage" name="identityNameLanguage_1">
                        <s:iterator value="languages" var="language">
                            <option value='<s:property value="#language.key"/>' <s:if test='%{#language.key==defaultLanguage}'>selected="selected"</s:if>><s:property value="#language.value"/></option>
                        </s:iterator>
                    </select>
                </td>
            </tr>
            <tr>
                <td><label><s:property value="getText('eaccpf.identity.dates.use')" />:</label></td>
                <td><input type="button" value="<s:property value="getText('eaccpf.commons.add.single.date')" />" id="addNameDate" onclick="addDateOrDateRangeName(this.id, $(this).parent().parent().parent().parent().attr('id'), '<s:property value="getText('eaccpf.commons.date')" />', '<s:property value="getText('eaccpf.commons.from.date')" />', '<s:property value="getText('eaccpf.commons.to.date')" />', '<s:property value="getText('eaccpf.commons.date.type')" />', '<s:property value="getText('eaccpf.commons.date.known')" />', '<s:property value="getText('eaccpf.commons.unknown.date')" />', '<s:property value="openLabel" />', '<s:property value="getText('eaccpf.commons.iso.date')" />', '<s:property value="getText('eaccpf.commons.error.no.standard.date')" />', '<s:property value="getText('eaccpf.commons.error.no.standard.dateRange')" />')" />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <input type="button" value="<s:property value="getText('eaccpf.commons.add.range.date')" />" id="addNameDateRange" onclick="addDateOrDateRangeName(this.id, $(this).parent().parent().parent().parent().attr('id'), '<s:property value="getText('eaccpf.commons.date')" />', '<s:property value="getText('eaccpf.commons.from.date')" />', '<s:property value="getText('eaccpf.commons.to.date')" />', '<s:property value="getText('eaccpf.commons.date.type')" />', '<s:property value="getText('eaccpf.commons.date.known')" />', '<s:property value="getText('eaccpf.commons.unknown.date')" />', '<s:property value="openLabel" />', '<s:property value="getText('eaccpf.commons.iso.date')" />', '<s:property value="getText('eaccpf.commons.error.no.standard.date')" />', '<s:property value="getText('eaccpf.commons.error.no.standard.dateRange')" />')" /></td>
                <td><input type="hidden" id="identityPersonName_1_rows" name="identityPersonName_1_rows" value="0" /></td>
                <td></td>
            </tr>
        </table>
    </s:else>
    <table id="addNameButtonPanel" class="tablePadding">
        <tr>
            <td>
                <input type="button" value="<s:property value="getText('eaccpf.identity.add.form')" />" id="addNameButton" onclick="addNameForm('<s:property value="getText('eaccpf.identity.error.empty.name')" />', '<s:property value="defaultLanguage"/>');" />
            </td>
        </tr>
    </table>
    <s:if test="%{loader.identifiers.size() > 0}">
        <s:iterator var="current" value="loader.identifiers" status="status">
            <table id="identityPersonId_<s:property value="#status.index + 1" />" class="tablePadding">
                <tr>
                    <th class="sectionHeader" colspan="4"><s:property value="getText('eaccpf.identity.identifier')" /></th>
                </tr>
                <tr id="trPersonId">
                    <td><s:property value="cpfTypeIdentifierText" /></td>
                    <td><input type="text" id="textPersonId" name="textPersonId_<s:property value="#status.index + 1" />" value="<s:property value="#current.identifier" />"/></td>
                    <td><s:property value="getText('eaccpf.commons.identifier.type')" /></td>
                    <td><input type="text" id="textPersonTypeId" name="textPersonTypeId_<s:property value="#status.index + 1" />" value="<s:property value="#current.identifierType" />"/></td>
                </tr>
            </table>
        </s:iterator>
    </s:if>
    <s:else>
        <table id="identityPersonId_1" class="tablePadding">
            <tr>
                <th class="sectionHeader" colspan="4"><s:property value="getText('eaccpf.identity.identifier')" /></th>
            </tr>
            <tr id="trPersonId">
                <td><s:property value="cpfTypeIdentifierText" />:</td>
                <td><input type="text" id="textPersonId" name="textPersonId_1"/></td>
                <td><s:property value="getText('eaccpf.identity.identifier.agency')" />:</td>
                <td><input type="text" id="textPersonTypeId" name="textPersonTypeId_1"/></td>
            </tr>
        </table>
    </s:else>
    <table id="addNameIdentifierButtonPanel" class="tablePadding">
        <tr>
            <td>
                <input type="button" value="<s:property value="getText('eaccpf.identity.add.identifier')" />" id="addPersonIdButton" onclick="addPersonId('<s:property value="getText('eaccpf.identity.error.empty.identifier')" />');" />
            </td>
        </tr>
    </table>
    <table id="dateExistenceTable" class="tablePadding">
        <tr id="trDateExistenceTableHeader">
            <th class="sectionHeader" colspan="4"><s:property value="getText('eaccpf.identity.dates.existence')" />*</th>
        </tr>
        <s:if test="%{loader.existDates.size() > 0}">
            <s:iterator var="currentDateRow" value="loader.existDates" status="status">
                <tr id="trDate_text_<s:property value="#status.index + 1" />">
                    <td>
                        <s:if test="#currentDateRow.dateContent2 != null">
                            <s:property value="getText('eaccpf.commons.from.date')" />:
                        </s:if>
                        <s:else>
                            <s:property value="getText('eaccpf.commons.date')" />:
                        </s:else>
                    </td>
                    <td>
                        <input type="text" id="date_1" name="dateExistenceTable_date_1_<s:property value="#status.index + 1" />" value="<s:property value="#currentDateRow.dateContent1" />" <s:if test='%{#currentDateRow.radioValue1 != "known"}'>disabled="disabled"</s:if> onchange="parseDateToISO($(this).attr('value'), $(this).parent().parent().parent().parent().attr('id'), $(this).parent().parent().attr('id'), $(this).attr('id'));"><br />
                        </td>
                    <s:if test="#currentDateRow.dateContent2 != null or #currentDateRow.standardDate2 != null">
                        <td><s:property value="getText('eaccpf.commons.to.date')" />:</td>
                        <td>
                            <input type="text" id="date_2" name="dateExistenceTable_date_2_<s:property value="#status.index + 1" />" value="<s:property value="#currentDateRow.dateContent2" />" <s:if test='%{#currentDateRow.radioValue2 != "known"}'>disabled="disabled"</s:if> onchange="parseDateToISO($(this).attr('value'), $(this).parent().parent().parent().parent().attr('id'), $(this).parent().parent().attr('id'), $(this).attr('id'));"><br />
                            </td>
                    </s:if>
                    <s:else>
                        <td></td>
                        <td></td>
                    </s:else>
                </tr>
                <tr id="trDate_radio_<s:property value="#status.index + 1" />">
                    <td>
                        <s:property value="getText('eaccpf.commons.date.type')"/>:
                    </td>
                    <td>
                        <input type="radio" name="dateExistenceTable_date_1_radio_<s:property value="#status.index + 1" />" value="known" <s:if test='%{#currentDateRow.radioValue1 == "known"}'>checked="checked"</s:if> onchange="toggleDateTextfields($(this));"><s:property value="getText('eaccpf.commons.date.known')"/>&nbsp;
                        <input type="radio" name="dateExistenceTable_date_1_radio_<s:property value="#status.index + 1" />" value="unknown" <s:if test='%{#currentDateRow.radioValue1 == "unknown"}'>checked="checked"</s:if> onchange="toggleDateTextfields($(this));"><s:property value="getText('eaccpf.commons.unknown.date')"/>&nbsp;
                        <s:if test="#currentDateRow.dateContent2 != null or #currentDateRow.standardDate2 != null">
                        </td>
                        <td></td>
                        <td>
                            <input type="radio" name="dateExistenceTable_date_2_radio_<s:property value="#status.index + 1" />" value="known" <s:if test='%{#currentDateRow.radioValue2 == "known"}'>checked="checked"</s:if> onchange="toggleDateTextfields($(this));"><s:property value="getText('eaccpf.commons.date.known')"/>&nbsp;
                            <input type="radio" name="dateExistenceTable_date_2_radio_<s:property value="#status.index + 1" />" value="unknown" <s:if test='%{#currentDateRow.radioValue2 == "unknown"}'>checked="checked"</s:if> onchange="toggleDateTextfields($(this));"><s:property value="getText('eaccpf.commons.unknown.date')"/>&nbsp;
                            <input type="radio" name="dateExistenceTable_date_2_radio_<s:property value="#status.index + 1" />" value="open" <s:if test='%{#currentDateRow.radioValue2 == "open"}'>checked="checked"</s:if> onchange="toggleDateTextfields($(this));"><s:property value="openLabel"/>
                            </td>
                    </s:if>
                    <s:else>
                    <input type="radio" name="dateExistenceTable_date_1_radio_<s:property value="#status.index + 1" />" value="open" <s:if test='%{#currentDateRow.radioValue1 == "open"}'>checked="checked"</s:if> onchange="toggleDateTextfields($(this));"><s:property value="openLabel"/>&nbsp;
                        </td>
                        <td></td>
                        <td></td>
                </s:else>
                </tr>
                <tr id="trDate_iso_<s:property value="#status.index + 1" />">
                    <td><s:property value="getText('eaccpf.commons.iso.date')" />:</td>
                    <td>
                        <table style="width: 50%;">
                            <tr>
                                <td style="padding: 0px;"><input type="text" title="YYYY" id="date_1_Year" name="dateExistenceTable_date_1_Year_<s:property value="#status.index + 1" />" <s:if test='%{#currentDateRow.radioValue1 != "known"}'>disabled="disabled"</s:if> <s:if test="%{#currentDateRow.standardDate1.year != 0}">value="<s:property value="#currentDateRow.standardDate1.year" />"</s:if> size="4" maxlength="4" onchange="validateIsoDates($(this), '<s:property value="getText('eaccpf.commons.error.no.standard.date')" />', '<s:property value="getText('eaccpf.commons.error.no.standard.dateRange')" />');" /></td>
                                    <td style="padding: 0px;">&ndash;</td>
                                        <td style="padding: 0px;"><input type="text" title="MM" id="date_1_Month" name="dateExistenceTable_date_1_Month_<s:property value="#status.index + 1" />" <s:if test='%{#currentDateRow.radioValue1 != "known"}'>disabled="disabled"</s:if> <s:if test="%{#currentDateRow.standardDate1.month != 0}">value="<s:if test="%{#currentDateRow.standardDate1.month < 10}">0</s:if><s:property value="#currentDateRow.standardDate1.month" />"</s:if> size="2" maxlength="2" onchange="validateIsoDates($(this), '<s:property value="getText('eaccpf.commons.error.no.standard.date')" />', '<s:property value="getText('eaccpf.commons.error.no.standard.dateRange')" />');" /></td>
                                    <td style="padding: 0px;">&ndash;</td>
                                        <td style="padding: 0px;"><input type="text" title="DD" id="date_1_Day" name="dateExistenceTable_date_1_Day_<s:property value="#status.index + 1" />" <s:if test='%{#currentDateRow.radioValue1 != "known"}'>disabled="disabled"</s:if> <s:if test="%{#currentDateRow.standardDate1.day != 0}">value="<s:if test="%{#currentDateRow.standardDate1.day < 10}">0</s:if><s:property value="#currentDateRow.standardDate1.day" />"</s:if> size="2" maxlength="2" onchange="validateIsoDates($(this), '<s:property value="getText('eaccpf.commons.error.no.standard.date')" />', '<s:property value="getText('eaccpf.commons.error.no.standard.dateRange')" />');" /></td>
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
                                    <td style="padding: 0px;"><input type="text" title="YYYY" id="date_2_Year" name="dateExistenceTable_date_2_Year_<s:property value="#status.index + 1" />" <s:if test='%{#currentDateRow.radioValue2 != "known"}'>disabled="disabled"</s:if> <s:if test="%{#currentDateRow.standardDate2.year != 0}">value="<s:property value="#currentDateRow.standardDate2.year" />"</s:if> size="4" maxlength="4" onchange="validateIsoDates($(this), '<s:property value="getText('eaccpf.commons.error.no.standard.date')" />', '<s:property value="getText('eaccpf.commons.error.no.standard.dateRange')" />');" /></td>
                                        <td style="padding: 0px;">&ndash;</td>
                                            <td style="padding: 0px;"><input type="text" title="MM" id="date_2_Month" name="dateExistenceTable_date_2_Month_<s:property value="#status.index + 1" />" <s:if test='%{#currentDateRow.radioValue2 != "known"}'>disabled="disabled"</s:if> <s:if test="%{#currentDateRow.standardDate2.month != 0}">value="<s:if test="%{#currentDateRow.standardDate2.month < 10}">0</s:if><s:property value="#currentDateRow.standardDate2.month" />"</s:if> size="2" maxlength="2" onchange="validateIsoDates($(this), '<s:property value="getText('eaccpf.commons.error.no.standard.date')" />', '<s:property value="getText('eaccpf.commons.error.no.standard.dateRange')" />');" /></td>
                                        <td style="padding: 0px;">&ndash;</td>
                                            <td style="padding: 0px;"><input type="text" title="DD" id="date_2_Day" name="dateExistenceTable_date_2_Day_<s:property value="#status.index + 1" />" <s:if test='%{#currentDateRow.radioValue2 != "known"}'>disabled="disabled"</s:if> <s:if test="%{#currentDateRow.standardDate2.day != 0}">value="<s:if test="%{#currentDateRow.standardDate2.day < 10}">0</s:if><s:property value="#currentDateRow.standardDate2.day" />"</s:if> size="2" maxlength="2" onchange="validateIsoDates($(this), '<s:property value="getText('eaccpf.commons.error.no.standard.date')" />', '<s:property value="getText('eaccpf.commons.error.no.standard.dateRange')" />');" /></td>
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
        <s:else>
            <tr id="trDate_text_1">
                <td><b><s:property value="getText('eaccpf.commons.from.date')" />:*</b></td>
                <td>
                    <input type="text" id="date_1" name="dateExistenceTable_date_1_1" onchange="parseDateToISO($(this).attr('value'), $(this).parent().parent().parent().parent().attr('id'), $(this).parent().parent().attr('id'), $(this).attr('id'), '<s:property value="getText('eaccpf.commons.error.no.standard.date')" />', '<s:property value="getText('eaccpf.commons.error.no.standard.dateRange')" />');"><br />
                </td>
                <td><b><s:property value="getText('eaccpf.commons.to.date')" />:*</b></td>
                <td>
                    <input type="text" id="date_2" name="dateExistenceTable_date_2_1" onchange="parseDateToISO($(this).attr('value'), $(this).parent().parent().parent().parent().attr('id'), $(this).parent().parent().attr('id'), $(this).attr('id'), '<s:property value="getText('eaccpf.commons.error.no.standard.date')" />', '<s:property value="getText('eaccpf.commons.error.no.standard.dateRange')" />');"><br />
                </td>
            </tr>
            <tr id="trDate_radio_1">
                <td>
                    <s:property value="getText('eaccpf.commons.date.type')"/>:
                </td>
                <td>
                    <input type="radio" name="dateExistenceTable_date_1_radio_1" value="known" checked="checked" onchange="toggleDateTextfields($(this));"><s:property value="getText('eaccpf.commons.date.known')"/>&nbsp;
                    <input type="radio" name="dateExistenceTable_date_1_radio_1" value="unknown" onchange="toggleDateTextfields($(this));"><s:property value="getText('eaccpf.commons.unknown.date')"/>&nbsp;
                </td>
                <td></td>
                <td>
                    <input type="radio" name="dateExistenceTable_date_2_radio_1" value="known" checked="checked" onchange="toggleDateTextfields($(this));"><s:property value="getText('eaccpf.commons.date.known')"/>&nbsp;
                    <input type="radio" name="dateExistenceTable_date_2_radio_1" value="unknown" onchange="toggleDateTextfields($(this));"><s:property value="getText('eaccpf.commons.unknown.date')"/>&nbsp;
                    <input type="radio" name="dateExistenceTable_date_2_radio_1" value="open" onchange="toggleDateTextfields($(this));"><s:property value="openLabel"/>
                </td>
            </tr>
            <tr id="trDate_iso_1">
                <td><s:property value="getText('eaccpf.commons.iso.date')" />:</td>
                <td>
                    <table style="width: 50%;">
                        <tr>
                            <td style="padding: 0px;"><input type="text" title="YYYY" id="date_1_Year" name="dateExistenceTable_date_1_Year_1" size="4" maxlength="4" onchange="validateIsoDates($(this), '<s:property value="getText('eaccpf.commons.error.no.standard.date')" />', '<s:property value="getText('eaccpf.commons.error.no.standard.dateRange')" />');" /></td>
                            <td style="padding: 0px;">&ndash;</td>
                            <td style="padding: 0px;"><input type="text" title="MM" id="date_1_Month" name="dateExistenceTable_date_1_Month_1" size="2" maxlength="2" onchange="validateIsoDates($(this), '<s:property value="getText('eaccpf.commons.error.no.standard.date')" />', '<s:property value="getText('eaccpf.commons.error.no.standard.dateRange')" />');" /></td>
                            <td style="padding: 0px;">&ndash;</td>
                            <td style="padding: 0px;"><input type="text" title="DD" id="date_1_Day" name="dateExistenceTable_date_1_Day_1" size="2" maxlength="2" onchange="validateIsoDates($(this), '<s:property value="getText('eaccpf.commons.error.no.standard.date')" />', '<s:property value="getText('eaccpf.commons.error.no.standard.dateRange')" />');" /></td>
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
                <td>
                    <table style="width: 50%;">
                        <tr>
                            <td style="padding: 0px;"><input type="text" title="YYYY" id="date_2_Year" name="dateExistenceTable_date_2_Year_1" size="4" maxlength="4" onchange="validateIsoDates($(this), '<s:property value="getText('eaccpf.commons.error.no.standard.date')" />', '<s:property value="getText('eaccpf.commons.error.no.standard.dateRange')" />');" /></td>
                            <td style="padding: 0px;">&ndash;</td>
                            <td style="padding: 0px;"><input type="text" title="MM" id="date_2_Month" name="dateExistenceTable_date_2_Month_1" size="2" maxlength="2" onchange="validateIsoDates($(this), '<s:property value="getText('eaccpf.commons.error.no.standard.date')" />', '<s:property value="getText('eaccpf.commons.error.no.standard.dateRange')" />');" /></td>
                            <td style="padding: 0px;">&ndash;</td>
                            <td style="padding: 0px;"><input type="text" title="DD" id="date_2_Day" name="dateExistenceTable_date_2_Day_1" size="2" maxlength="2" onchange="validateIsoDates($(this), '<s:property value="getText('eaccpf.commons.error.no.standard.date')" />', '<s:property value="getText('eaccpf.commons.error.no.standard.dateRange')" />');" /></td>
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
            </tr>
        </s:else>
    </table>
    <table id="dateExistenceButtonPanel" class="tablePadding">
        <tr>
            <td><input type="button" value="<s:property value="getText('eaccpf.commons.add.single.date')" />" id="addExistDate" onclick="addDateOrDateRangeExistence(this.id, '<s:property value="getText('eaccpf.commons.date')" />', '<s:property value="getText('eaccpf.commons.from.date')" />', '<s:property value="getText('eaccpf.commons.to.date')" />', '<s:property value="getText('eaccpf.commons.date.type')" />', '<s:property value="getText('eaccpf.commons.date.known')" />', '<s:property value="getText('eaccpf.commons.unknown.date')" />', '<s:property value="openLabel" />', '<s:property value="getText('eaccpf.commons.iso.date')" />')" />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                <input type="button" value="<s:property value="getText('eaccpf.commons.add.range.date')" />" id="addExistDateRange" onclick="addDateOrDateRangeExistence(this.id, '<s:property value="getText('eaccpf.commons.date')" />', '<s:property value="getText('eaccpf.commons.from.date')" />', '<s:property value="getText('eaccpf.commons.to.date')" />', '<s:property value="getText('eaccpf.commons.date.type')" />', '<s:property value="getText('eaccpf.commons.date.known')" />', '<s:property value="getText('eaccpf.commons.unknown.date')" />', '<s:property value="openLabel" />', '<s:property value="getText('eaccpf.commons.iso.date')" />')" /></td>
                <s:if test="%{loader.existDates.size() > 0}">
                <td><input type="hidden" id="dateExistenceTable_rows" name="dateExistenceTable_rows" value="<s:property value="loader.existDates.size()" />" /></td>
                </s:if>
                <s:else>
                <td><input type="hidden" id="dateExistenceTable_rows" name="dateExistenceTable_rows" value="1" /></td>
                </s:else>
            <td></td>
        </tr>
    </table>
</div>