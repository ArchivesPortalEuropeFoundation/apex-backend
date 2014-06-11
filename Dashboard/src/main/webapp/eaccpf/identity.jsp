<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="/struts-tags" prefix="s" %>

<div id="headerContainer">
</div>

<div id="identityTabContent">
    <input type="hidden" name="cpfType" value='<s:property value="cpfType" />' />
    <h2 class="tablePadding"><s:property value="cpfTypeDescriptionText" /></h2>
    <s:if test="%{loader.nameEntries.size() > 0}">
        <s:iterator var="current" value="loader.nameEntries" status="status">
            <table id="identityPersonName_<s:property value="#status.index + 1" />" class="tablePadding">
                <tr>
                    <th class="sectionHeader" colspan="4">Name</th>
                </tr>
                <tr id="trNamePart_1">
                    <td><label for="textPersonName">Full name or part of name:*</label></td>
                    <td><input type="text" id="textPersonName" name="identityPersonName_<s:property value="#status.index + 1" />_part_1" required="required" value="<s:property value="#current.name" />" /></td>
                    <td><label for="identityComponentOfName">Component of the name:</label></td>
                    <td>
                        <select id="identityComponentOfName" name="identityPersonName_<s:property value="#status.index + 1" />_comp_1" required="required">
                            <s:iterator value="componentNameList" var="compNames">
                                <option value='<s:property value="#compNames.key"/>' <s:if test='%{#compNames.key==#current.component}'>selected="selected"</s:if>><s:property value="#compNames.value"/></option>
                            </s:iterator>
                        </select>
                    </td>
                </tr>
                <tr id="trNameForm">
                    <td><label for="identityFormOfName">Form of the name:</label></td>
                    <td>
                        <select id="identityFormOfName" name="identityFormOfName_<s:property value="#status.index + 1" />" required="required">
                            <s:iterator value="formNameList" var="formNames">
                                <option value='<s:property value="#formNames.key"/>' <s:if test='%{#formNames.key==#current.form}'>selected="selected"</s:if>><s:property value="#formNames.value"/></option>
                            </s:iterator>
                        </select>
                    </td>
                    <td><label for="identityNameLanguage">Select a language:</label></td>
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
                            <td>Date<s:if test="#currentDateRow.dateContent2 != null"> range from</s:if></td>
                                <td>
                                    <input type="text" id="date_1" name="identityPersonName_<s:property value="#status.index + 1" />_date_1_<s:property value="#status2.index + 1" />" value="<s:property value="#currentDateRow.dateContent1" />" <s:if test="%{#currentDateRow.standardDate1.year == 0000}">disabled="disabled" </s:if>onblur="parseDateToISO($(this).attr('value'), $(this).parent().parent().parent().parent().attr('id'), $(this).parent().parent().attr('id'), $(this).attr('id'));"><br />
                                <input type="checkbox" id="date_unknown_1" name="identityPersonName_<s:property value="#status.index + 1" />_date_unknown_1_<s:property value="#status2.index + 1" />" <s:if test="%{#currentDateRow.standardDate1.year == 0000}">checked="checked" </s:if>onchange="toggleDateTextfields($(this));" /><label for="date_unknown_1">unknown</label>
                                </td>
                            <s:if test="#currentDateRow.dateContent2 != null or #currentDateRow.standardDate2 != null">
                                <td>to</td>
                                <td>
                                    <input type="text" id="date_2" name="identityPersonName_<s:property value="#status.index + 1" />_date_2_<s:property value="#status2.index + 1" />" value="<s:property value="#currentDateRow.dateContent2" />" <s:if test="%{#currentDateRow.standardDate2.year == 2999}">disabled="disabled" </s:if>onblur="parseDateToISO($(this).attr('value'), $(this).parent().parent().parent().parent().attr('id'), $(this).parent().parent().attr('id'), $(this).attr('id'));"><br />
                                    <input type="checkbox" id="date_unknown_2" name="identityPersonName_<s:property value="#status.index + 1" />_date_unknown_2_<s:property value="#status2.index + 1" />" <s:if test="%{#currentDateRow.standardDate2.year == 2999}">checked="checked" </s:if>onchange="toggleDateTextfields($(this));" /><label for="date_unknown_2">unknown</label>
                                    </td>
                            </s:if>
                            <s:else>
                                <td></td>
                                <td></td>
                            </s:else>
                        </tr>
                        <tr id="trDate_iso_<s:property value="#status2.index + 1" />">
                            <td>(ISO date<s:if test="#currentDateRow.standardDate2 != null">s</s:if>; optional)</td>
                            <td><input type="text" title="YYYY" id="date_1_Year" name="identityPersonName_<s:property value="#status.index + 1" />_date_1_Year_<s:property value="#status2.index + 1" />" size="4" maxlength="4" <s:if test="%{#currentDateRow.standardDate1.year == 0000}">disabled="disabled" </s:if> value="<s:property value="#currentDateRow.standardDate1.year" />" /> &ndash;
                                <input type="text" title="MM" id="date_1_Month" name="identityPersonName_<s:property value="#status.index + 1" />_date_1_Month_<s:property value="#status2.index + 1" />" size="2" maxlength="2" <s:if test="%{#currentDateRow.standardDate1.year == 0000}">disabled="disabled" </s:if> <s:if test="%{#currentDateRow.standardDate1.month != 0}">value="<s:property value="#currentDateRow.standardDate1.month" />"</s:if> /> &ndash;
                                <input type="text" title="DD" id="date_1_Day" name="identityPersonName_<s:property value="#status.index + 1" />_date_1_Day_<s:property value="#status2.index + 1" />" size="2" maxlength="2" <s:if test="%{#currentDateRow.standardDate1.year == 0000}">disabled="disabled" </s:if> <s:if test="%{#currentDateRow.standardDate1.day != 0}">value="<s:property value="#currentDateRow.standardDate1.day" />"</s:if> /></td>
                                <s:if test="#currentDateRow.standardDate2 != null">
                                <td></td>
                                <td><input type="text" title="YYYY" id="date_2_Year" name="identityPersonName_<s:property value="#status.index + 1" />_date_2_Year_<s:property value="#status2.index + 1" />" size="4" maxlength="4" <s:if test="%{#currentDateRow.standardDate2.year == 2999}">disabled="disabled" </s:if> value="<s:property value="#currentDateRow.standardDate2.year" />" /> &ndash;
                                    <input type="text" title="MM" id="date_2_Month" name="identityPersonName_<s:property value="#status.index + 1" />_date_2_Month_<s:property value="#status2.index + 1" />" size="2" maxlength="2" <s:if test="%{#currentDateRow.standardDate2.year == 2999}">disabled="disabled" </s:if> <s:if test="%{#currentDateRow.standardDate2.month != 0}">value="<s:property value="#currentDateRow.standardDate2.month" />"</s:if> /> &ndash;
                                    <input type="text" title="DD" id="date_2_Day" name="identityPersonName_<s:property value="#status.index + 1" />_date_2_Day_<s:property value="#status2.index + 1" />" size="2" maxlength="2" <s:if test="%{#currentDateRow.standardDate2.year == 2999}">disabled="disabled" </s:if> <s:if test="%{#currentDateRow.standardDate2.day != 0}">value="<s:property value="#currentDateRow.standardDate2.day" />"</s:if> /></td>
                                </s:if>
                                <s:else>
                                <td></td>
                                <td></td>
                            </s:else>
                        </tr>
                    </s:iterator>
                </s:if>
                <tr>
                    <td>Add dates of use for the name:</td>
                    <td><input type="button" value="Add single date" id="addNameDate" onclick="addDateOrDateRangeName(this.id, $(this).parent().parent().parent().parent().attr('id'))" />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                        <input type="button" value="Add date range" id="addNameDateRange" onclick="addDateOrDateRangeName(this.id, $(this).parent().parent().parent().parent().attr('id'))" /></td>
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
                <th class="sectionHeader" colspan="4">Name</th>
            </tr>
            <tr id="trNamePart_1">
                <td><label for="textPersonName">Full name or part of name:*</label></td>
                <td><input type="text" id="textPersonName" name="identityPersonName_1_part_1" /></td>
                <td><label for="identityComponentOfName">Component of the name:</label></td>
                <td>
                    <select id="identityComponentOfName" name="identityPersonName_1_comp_1">
                        <s:iterator value="componentNameList" var="compNames">
                            <option value='<s:property value="#compNames.key"/>' <s:if test='%{#compNames.key=="persname"}'>selected="selected"</s:if>><s:property value="#compNames.value"/></option>
                        </s:iterator>
                    </select>
                </td>
            </tr>
            <tr>
                <td><input type="button" value="Add another part" id="addAddressComponentButton" onclick="addPartName($(this).parent().parent().parent().parent().attr('id'));" /></td>
                <td colspan="3"></td>
            </tr>
            <tr id="trNameForm">
                <td><label for="identityFormOfName">Form of the name:</label></td>
                <td>
                    <select id="identityFormOfName" name="identityFormOfName_1">
                        <s:iterator value="formNameList" var="formNames">
                            <option value='<s:property value="#formNames.key"/>' <s:if test='%{#formNames.key=="authorized"}'>selected="selected"</s:if>><s:property value="#formNames.value"/></option>
                        </s:iterator>
                    </select>
                </td>
                <td><label for="identityNameLanguage">Select a language:</label></td>
                <td>
                    <select id="identityNameLanguage" name="identityNameLanguage_1">
                        <s:iterator value="languages" var="language">
                            <option value='<s:property value="#language.key"/>' <s:if test='%{#language.key==defaultLanguage}'>selected="selected"</s:if>><s:property value="#language.value"/></option>
                        </s:iterator>
                    </select>
                </td>
            </tr>
            <tr>
                <td><label>Add dates of use for the name:</label></td>
                <td><input type="button" value="Add single date" id="addNameDate" onclick="addDateOrDateRangeName(this.id, $(this).parent().parent().parent().parent().attr('id'))" />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <input type="button" value="Add date range" id="addNameDateRange" onclick="addDateOrDateRangeName(this.id, $(this).parent().parent().parent().parent().attr('id'))" /></td>
                <td><input type="hidden" id="identityPersonName_1_rows" name="identityPersonName_1_rows" value="0" /></td>
                <td></td>
            </tr>
        </table>
    </s:else>
    <table id="addNameButtonPanel" class="tablePadding">
        <tr>
            <td>
                <input type="button" value="Add another form of the name" id="addNameButton" onclick="addNameForm('Please fill in the existing fields first', '<s:property value="defaultLanguage"/>');" />
            </td>
        </tr>
    </table>
    <s:if test="%{loader.identifiers.size() > 0}">
        <s:iterator var="current" value="loader.identifiers" status="status">
            <table id="identityPersonId_<s:property value="#status.index + 1" />" class="tablePadding">
                <tr>
                    <th class="sectionHeader" colspan="4">Identifier</th>
                </tr>
                <tr id="trPersonId">
                    <td><s:property value="cpfTypeIdentifierText" />:</td>
                    <td><input type="text" id="textPersonId" name="textPersonId_<s:property value="#status.index + 1" />" value="<s:property value="#current.identifier" />"/></td>
                    <td>Type of the identifier:</td>
                    <td><input type="text" id="textPersonTypeId" name="textPersonTypeId_<s:property value="#status.index + 1" />" value="<s:property value="#current.identifierType" />"/></td>
                </tr>
            </table>
        </s:iterator>
    </s:if>
    <s:else>
        <table id="identityPersonId_1" class="tablePadding">
            <tr>
                <th class="sectionHeader" colspan="4">Identifier</th>
            </tr>
            <tr id="trPersonId">
                <td><s:property value="cpfTypeIdentifierText" />:</td>
                <td><input type="text" id="textPersonId" name="textPersonId_1"/></td>
                <td>Type of the identifier:</td>
                <td><input type="text" id="textPersonTypeId" name="textPersonTypeId_1"/></td>
            </tr>
        </table>
    </s:else>
    <table id="addNameIdentifierButtonPanel" class="tablePadding">
        <tr>
            <td>
                <input type="button" value="Add identifier" id="addPersonIdButton" onclick="addPersonId();" />
            </td>
        </tr>
    </table>
    <table id="dateExistenceTable" class="tablePadding">
        <tr id="trDateExistenceTableHeader">
            <th class="sectionHeader" colspan="4">Date(s) of existence*</th>
        </tr>
        <s:if test="%{loader.existDates.size() > 0}">
            <s:iterator var="current" value="loader.existDates" status="status">
                <tr id="trDate_text_<s:property value="#status.index + 1" />">
                    <td>Date<s:if test="#current.dateContent2 != null"> range from</s:if></td>
                        <td>
                            <input type="text" id="date_1" name="dateExistenceTable_date_1_<s:property value="#status.index + 1" />" value="<s:property value="#current.dateContent1" />" <s:if test="%{#current.standardDate1.year == 0000}">disabled="disabled" </s:if>onblur="parseDateToISO($(this).attr('value'), $(this).parent().parent().parent().parent().attr('id'), $(this).parent().parent().attr('id'), $(this).attr('id'));"><br />
                        <input type="checkbox" id="date_unknown_1" name="dateExistenceTable_date_unknown_1_<s:property value="#status.index + 1" />" <s:if test="%{#current.standardDate1.year == 0000}">checked="checked" </s:if>onchange="toggleDateTextfields($(this));" /><label for="date_unknown_1">unknown</label>
                        </td>
                    <s:if test="#current.dateContent2 != null or #current.standardDate2 != null">
                        <td>to</td>
                        <td>
                            <input type="text" id="date_2" name="dateExistenceTable_date_2_<s:property value="#status.index + 1" />" value="<s:property value="#current.dateContent2" />" <s:if test="%{#current.standardDate2.year == 2999}">disabled="disabled" </s:if>onblur="parseDateToISO($(this).attr('value'), $(this).parent().parent().parent().parent().attr('id'), $(this).parent().parent().attr('id'), $(this).attr('id'));"><br />
                            <input type="checkbox" id="date_unknown_2" name="dateExistenceTable_date_unknown_2_<s:property value="#status.index + 1" />" <s:if test="%{#current.standardDate2.year == 2999}">checked="checked" </s:if>onchange="toggleDateTextfields($(this));" /><label for="date_unknown_2">unknown</label>
                            </td>
                    </s:if>
                    <s:else>
                        <td></td>
                        <td></td>
                    </s:else>
                </tr>
                <tr id="trDate_iso_<s:property value="#status.index + 1" />">
                    <td>(ISO date<s:if test="#current.standardDate2 != null">s</s:if>; optional)</td>
                    <td><input type="text" title="YYYY" id="date_1_Year" name="dateExistenceTable_date_1_Year_<s:property value="#status.index + 1" />" size="4" maxlength="4" <s:if test="%{#current.standardDate1.year == 0000}">disabled="disabled" </s:if> value="<s:property value="#current.standardDate1.year" />" /> &ndash;
                        <input type="text" title="MM" id="date_1_Month" name="dateExistenceTable_date_1_Month_<s:property value="#status.index + 1" />" size="2" maxlength="2" <s:if test="%{#current.standardDate1.year == 0000}">disabled="disabled" </s:if> <s:if test="%{#current.standardDate1.month != 0}">value="<s:property value="#current.standardDate1.month" />"</s:if> /> &ndash;
                        <input type="text" title="DD" id="date_1_Day" name="dateExistenceTable_date_1_Day_<s:property value="#status.index + 1" />" size="2" maxlength="2" <s:if test="%{#current.standardDate1.year == 0000}">disabled="disabled" </s:if> <s:if test="%{#current.standardDate1.day != 0}">value="<s:property value="#current.standardDate1.day" />"</s:if> /></td>
                        <s:if test="#current.standardDate2 != null">
                        <td></td>
                        <td><input type="text" title="YYYY" id="date_2_Year" name="dateExistenceTable_date_2_Year_<s:property value="#status.index + 1" />" size="4" maxlength="4" <s:if test="%{#current.standardDate2.year == 2999}">disabled="disabled" </s:if> value="<s:property value="#current.standardDate2.year" />" /> &ndash;
                            <input type="text" title="MM" id="date_2_Month" name="dateExistenceTable_date_2_Month_<s:property value="#status.index + 1" />" size="2" maxlength="2" <s:if test="%{#current.standardDate2.year == 2999}">disabled="disabled" </s:if> <s:if test="%{#current.standardDate2.month != 0}">value="<s:property value="#current.standardDate2.month" />"</s:if> /> &ndash;
                            <input type="text" title="DD" id="date_2_Day" name="dateExistenceTable_date_2_Day_<s:property value="#status.index + 1" />" size="2" maxlength="2" <s:if test="%{#current.standardDate2.year == 2999}">disabled="disabled" </s:if> <s:if test="%{#current.standardDate2.day != 0}">value="<s:property value="#current.standardDate2.day" />"</s:if> /></td>
                        </s:if>
                        <s:else>
                        <td></td>
                        <td></td>
                    </s:else>
                </tr>
            </s:iterator>
        </s:if>
        <s:else>
            <tr id="trDate_text_1">
                <td>Date range from</td>
                <td>
                    <input type="text" id="date_1" name="dateExistenceTable_date_1_1" onblur="parseDateToISO($(this).attr('value'), $(this).parent().parent().parent().parent().attr('id'), $(this).parent().parent().attr('id'), $(this).attr('id'));"><br />
                    <input type="checkbox" id="date_unknown_1" name="dateExistenceTable_date_unknown_1_1" onchange="toggleDateTextfields($(this));" /><label for="date_unknown_1">unknown</label>
                </td>
                <td>to</td>
                <td>
                    <input type="text" id="date_2" name="dateExistenceTable_date_2_1" onblur="parseDateToISO($(this).attr('value'), $(this).parent().parent().parent().parent().attr('id'), $(this).parent().parent().attr('id'), $(this).attr('id'));"><br />
                    <input type="checkbox" id="date_unknown_2" name="dateExistenceTable_date_unknown_2_1" onchange="toggleDateTextfields($(this));" /><label for="date_unknown_2">unknown</label>
                </td>
            </tr>
            <tr id="trDate_iso_1">
                <td>(ISO dates; optional)</td>
                <td>
                    <input type="text" title="YYYY" id="date_1_Year" name="dateExistenceTable_date_1_Year_1" size="4" maxlength="4" /> &ndash;
                    <input type="text" title="MM" id="date_1_Month" name="dateExistenceTable_date_1_Month_1" size="2" maxlength="2" /> &ndash;
                    <input type="text" title="DD" id="date_1_Day" name="dateExistenceTable_date_1_Day_1" size="2" maxlength="2" />
                </td>
                <td></td>
                <td>
                    <input type="text" title="YYYY" id="date_2_Year" name="dateExistenceTable_date_2_Year_1" size="4" maxlength="4" /> &ndash;
                    <input type="text" title="MM" id="date_2_Month" name="dateExistenceTable_date_2_Month_1" size="2" maxlength="2" /> &ndash;
                    <input type="text" title="DD" id="date_2_Day" name="dateExistenceTable_date_2_Day_1" size="2" maxlength="2" />
                </td>
            </tr>
        </s:else>
    </table>
    <table id="dateExistenceButtonPanel" class="tablePadding">
        <tr>
            <td><input type="button" value="Add single date" id="addExistDate" onclick="addDateOrDateRangeExistence(this.id)" />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                <input type="button" value="Add date range" id="addExistDateRange" onclick="addDateOrDateRangeExistence(this.id)" /></td>
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