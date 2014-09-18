<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="/struts-tags" prefix="s" %>

<div id="headerContainer">
</div>

<div id="identityTabContent">
    <input type="hidden" name="useMode" value='<s:property value="useMode" />' />
    <input type="hidden" name="cpfType" value='<s:property value="cpfType" />' />
    <h2 class="tablePadding"><s:property value="cpfTypeDescriptionText" /></h2>
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
    <table id="addNameButtonPanel" class="tablePadding">
        <tr>
            <td>
                <input type="button" value="<s:property value="getText('eaccpf.identity.add.form')" />" id="addNameButton" onclick="addNameForm('<s:property value="getText('eaccpf.identity.error.empty.name')" />', '<s:property value="defaultLanguage"/>');" />
            </td>
        </tr>
    </table>
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
    </table>
    <table id="dateExistenceButtonPanel" class="tablePadding">
        <tr>
            <td><input type="button" value="<s:property value="getText('eaccpf.commons.add.single.date')" />" id="addExistDate" onclick="addDateOrDateRangeExistence(this.id, '<s:property value="getText('eaccpf.commons.date')" />', '<s:property value="getText('eaccpf.commons.from.date')" />', '<s:property value="getText('eaccpf.commons.to.date')" />', '<s:property value="getText('eaccpf.commons.date.type')" />', '<s:property value="getText('eaccpf.commons.date.known')" />', '<s:property value="getText('eaccpf.commons.unknown.date')" />', '<s:property value="openLabel" />', '<s:property value="getText('eaccpf.commons.iso.date')" />', '<s:property value="getText('eaccpf.commons.error.no.standard.date')" />', '<s:property value="getText('eaccpf.commons.error.no.standard.dateRange')" />')" />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                <input type="button" value="<s:property value="getText('eaccpf.commons.add.range.date')" />" id="addExistDateRange" onclick="addDateOrDateRangeExistence(this.id, '<s:property value="getText('eaccpf.commons.date')" />', '<s:property value="getText('eaccpf.commons.from.date')" />', '<s:property value="getText('eaccpf.commons.to.date')" />', '<s:property value="getText('eaccpf.commons.date.type')" />', '<s:property value="getText('eaccpf.commons.date.known')" />', '<s:property value="getText('eaccpf.commons.unknown.date')" />', '<s:property value="openLabel" />', '<s:property value="getText('eaccpf.commons.iso.date')" />', '<s:property value="getText('eaccpf.commons.error.no.standard.date')" />', '<s:property value="getText('eaccpf.commons.error.no.standard.dateRange')" />')" /></td>
            <td><input type="hidden" id="dateExistenceTable_rows" name="dateExistenceTable_rows" value="1" /></td>
            <td></td>
        </tr>
    </table>
</div>