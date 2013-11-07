<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<link rel="stylesheet" href="${pageContext.request.contextPath}/css/userprofile.css" type="text/css"/>

<script type='text/javascript'>
    $(function() {
        $('#licensecreativecommons').click(function() {
            $('#hiddenCreativeCommonsLicense').show();
            $('#hiddenEuropeanaLicense').hide();
        });
        $('#licensecc0').click(function() {
            $('#hiddenCreativeCommonsLicense').hide();
            $('#hiddenEuropeanaLicense').hide();
        });
        $('#licensecpdm').click(function() {
            $('#hiddenCreativeCommonsLicense').hide();
            $('#hiddenEuropeanaLicense').hide();
        });
        $('#licenseeuropeana').click(function() {
            $('#hiddenCreativeCommonsLicense').hide();
            $('#hiddenEuropeanaLicense').show();
        });
    });
</script>

<div id="headerContainer">
</div>

<div id="europeanaTabContent">
    <table>
        <tr>
            <td class="inputLabel"><s:label key="ead2ese.label.dataprovider" for="dataProvider" />:</td>
            <td>
                <s:textfield id="textDataProvider" name="textDataProvider" />
                <s:checkbox name="dataProviderCheck" id="dataProviderCheck" value="true" />
                <s:label key="ead2ese.content.dataprovider.file" for="dataProviderCheck"/>
            </td>
        </tr>
        <tr id="trSelectType">
            <td class="inputLabel"><s:label key="ead2ese.label.type" for="selectType" />:</td>
            <td><s:select id="selectType" name="europeanaDaoType" list="typeSet" listKey="value" listValue="content" />
                <s:checkbox name="europeanaDaoTypeCheck" id="daoTypeCheck" value="true" />
                <s:label key="ead2ese.label.type.file" for="europeanaDaoType"/>
            </td>
        </tr>
        <tr id="trLanguage">
            <td class="inputLabel">
                <s:label key="ead2ese.label.language.material" for="language" />:
            </td>
            <td class="tdVertical">
                <s:select name="languageSelection" id="languageSelection" listKey="value" listValue="content" list="languages" multiple="true"/>
                <s:checkbox name="languageCheck" id="languageOfTheMaterialCheck" value="true" />
                <s:label key="ead2ese.label.language.file" for="languageOfTheMaterialCheck"/>
            </td>
        </tr>
        <tr>
            <td class="inputLabel"><s:label key="ead2ese.label.license" for="license" />:</td>
            <td><s:radio name="license" id="license" list="licenseSet" listKey="value" listValue="content" />
                <br/>(<s:label key="ead2ese.content.license.moreinfo"/><s:a target="_blank" href="docs/Europeana%20Rights%20Guidelines.pdf" ><s:property value="getText('ead2ese.content.license.link')" /></s:a>)
                </td>
            </tr>
        <s:if test="license=='europeana'">
            <c:set var="creativeCommonsInvisible" value="style=\"display: none;\""></c:set>
        </s:if>
        <s:else>
            <c:set var="europeanaInvisible" value="style=\"display: none;\""></c:set>
        </s:else>
        <tr id="hiddenEuropeanaLicense"  ${europeanaInvisible}>
            <td class="inputLabel"><s:label key="ead2ese.label.license.europeana" for="europeanaLicense" />:</td>
            <td><s:select id="europeanaLicense" name="europeanaLicense" list="europeanaLicenseSet" listKey="value" listValue="content"/><s:fielderror fieldName="europeanaLicense"/>
            </td>
        </tr>
        <tr id="hiddenCreativeCommonsLicense" ${creativeCommonsInvisible}>
            <td colspan="2">
                <div id="cc_js_widget_container">
                    <script type="text/javascript" src="https://api.creativecommons.org/jswidget/tags/0.97/complete.js?locale=en_US&amp;want_a_license=definitely"></script>
                </div>
            </td>
        </tr>
        <tr>
            <td class="inputLabel"><s:label key="ead2ese.label.license.additional" for="licenseAdditionalInformation" />:</td>
            <td><s:textarea id="licenseAdditionalInformation" name="licenseAdditionalInformation"/>
            </td>
        </tr>
        <tr>
            <td class="inputLabel">
                <s:label key="ead2ese.label.hierarchy.prefix" for="hierarchyPrefix" />:
            </td>
            <td><s:textfield id="hierarchyPrefix" name="hierarchyPrefix"></s:textfield></td>
            </tr>
            <tr id="hiddenInheritFileParent">
                <td class="inputLabel">
                <s:label key="ead2ese.label.inherit.parent" for="inheritFileParent" />:
            </td>
            <td><s:radio name="inheritFileParent" list="yesNoSet" listKey="value" listValue="content" id="inheritFileParent"></s:radio></td>
            </tr>
            <tr id="hiddenInheritOrigination">
                <td class="inputLabel">
                <s:label key="ead2ese.label.inherit.origination" for="inheritOrigination" />:
            </td>
            <td><s:radio name="inheritOrigination" list="yesNoSet" listKey="value" listValue="content"></s:radio>
            </td>
        </tr>
    </table>
</div>