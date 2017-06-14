<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<link rel="stylesheet" href="${pageContext.request.contextPath}/css/ingestionprofile.css" type="text/css"/>

<script type='text/javascript'>
    $(function() {
        $("#radioConversiontypefalse").click(function() {
            $('#hiddenInheritFileParent').show();
            enableInheritFileParentCheckState();
            $('#hiddenInheritOrigination').show();
            enableInheritOriginationCheckState();
            $('#hiddenInheritUnittitle').show();
            enableInheritUnittitleCheckState();
        });

        $("#radioConversiontypetrue").click(function() {
            $('#hiddenInheritFileParent').hide();
            disableInheritFileParentCheckState();
            $('#hiddenInheritOrigination').hide();
            disableInheritOriginationCheckState();
            $('#hiddenInheritUnittitle').hide();
            disableInheritUnittitleCheckState();
        });

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

    function changeInheritFileParentCheckState() {
        if ($("#inheritFileParentCheck").attr('checked')) {
            enableInheritFileParentCheckState();
        } else {
            disableInheritFileParentCheckState();
        }
    }

    function enableInheritFileParentCheckState() {
        $("input[id^='inheritFileParent']").each(function() {
            $(this).removeAttr('disabled');
        });
    }

    function disableInheritFileParentCheckState() {
        $("input[id^='inheritFileParent']").each(function() {
            if ($(this).attr("id") != "inheritFileParentCheck") {
                $(this).attr('disabled', 'disabled');
                if ($(this).val() == "false") {
                    $(this).attr('checked', 'checked');
                } else {
                    $(this).removeAttr('checked');
                }
            }
        });
    }

    function changeInheritOriginationCheckState() {
        if ($("#inheritOriginationCheck").attr('checked')) {
            enableInheritOriginationCheckState();
        } else {
            disableInheritOriginationCheckState();
        }
    }

    function enableInheritOriginationCheckState() {
        $("input[name^='inheritOrigination']").each(function() {
            $(this).removeAttr('disabled');
        });
    }

    function disableInheritOriginationCheckState() {
        $("input[name^='inheritOrigination']").each(function() {
            if ($(this).attr("name") != "inheritOriginationCheck") {
                $(this).attr('disabled', 'disabled');
                if ($(this).val() == "false") {
                    $(this).attr('checked', 'checked');
                } else {
                    $(this).removeAttr('checked');
                }
            }
        });
    }

    function changeInheritUnittitleCheckState() {
        if ($("#inheritUnittitleCheck").attr('checked')) {
            enableInheritUnittitleCheckState();
        } else {
            disableInheritUnittitleCheckState();
        }
    }

    function enableInheritUnittitleCheckState() {
        $("input[name^='inheritUnittitle']").each(function() {
            $(this).removeAttr('disabled');
        });
    }

    function disableInheritUnittitleCheckState() {
        $("input[name^='inheritUnittitle']").each(function() {
            if ($(this).attr("name") != "inheritUnittitleCheck") {
                $(this).attr('disabled', 'disabled');
                if ($(this).val() == "no") {
                    $(this).attr('checked', 'checked');
                } else {
                    $(this).removeAttr('checked');
                }
            }
        });
    }
</script>

<div id="headerContainer">
</div>

<div id="europeanaTabContent">
    <table id="conversionToEDMForm">
        <tr>
            <td colspan="2" class="titleLabel">
                <s:label key="ead2ese.label.general.settings" />
            </td>
        </tr>
        <tr id="conversionMode">
            <td class="inputLabel"><s:label key="ead2ese.label.choose.conversion.type" for="conversionType" />:</td>
            <td>
                <s:iterator value="conversiontypeSet">
                    <s:radio name="conversiontype" id="radioConversiontype" list="top" listKey="value" listValue="content"></s:radio><br/>
                </s:iterator>
            </td>
        </tr>
        <tr>
            <td class="inputLabel">
                <s:label key="ead2ese.label.specify.idsource"  for="sourceOfIdentifiers" />:
            </td>
            <td>
                <s:iterator value="sourceOfIdentifiersSet">
                    <s:radio name="sourceOfIdentifiers" id="sourceOfIdentifiers" list="top" listKey="value" listValue="content"></s:radio><br/>
                </s:iterator>
            </td>
        </tr>
        <tr>
            <td class="inputLabel">
                <s:label key="ead2ese.label.specify.sourceOfFondsTitle"  for="sourceOfFondsTitle" />:
            </td>
            <td>
                <s:iterator value="sourceOfFondsTitleSet">
                    <s:radio name="sourceOfFondsTitle" id="sourceOfFondsTitle" list="top" listKey="value" listValue="content"></s:radio><br/>
                </s:iterator>
            </td>
        </tr>
        <tr>
            <td colspan="2" class="titleLabel">
                <s:label key="ead2ese.label.specific.settings" />
            </td>
        </tr>
        <tr id="trDataProvider">
            <td class="inputLabel"><s:label key="ead2ese.label.dataprovider" for="textDataProvider" />*:</td>
            <td>
                <s:textfield id="textDataProvider" name="textDataProvider" maxLength="100"/>
                <s:checkbox name="dataProviderCheck" id="dataProviderCheck" />
                <s:label key="ead2ese.content.dataprovider.file" for="dataProviderCheck"/>
            </td>
        </tr>
        <tr id="trSelectType">
            <td class="inputLabel"><s:label key="ead2ese.label.type" for="edmDaoType" />*:</td>
            <td><s:select id="edmDaoType" name="europeanaDaoType" list="typeSet" listKey="value" listValue="content" required="true" />
                <s:checkbox name="europeanaDaoTypeCheck" id="daoTypeCheck" />
                <s:label key="ead2ese.label.type.file" for="europeanaDaoType"/>
            </td>
        </tr>
        <tr id="trLanguage">
            <td class="inputLabel">
                <s:label key="ead2ese.label.language.material" for="languageSelection" />*:
            </td>
            <td class="tdVertical">
                <s:select name="languageSelection" id="languageselection" listKey="value" listValue="content" list="languages" multiple="true" required="true" />
                <s:checkbox name="languageCheck" id="languageOfTheMaterialCheck" />
                <s:label key="ead2ese.label.language.file" for="languageOfTheMaterialCheck"/>
            </td>
        </tr>
        <tr>
            <td class="inputLabel"><s:label key="ead2ese.label.license" for="license" />:</td>
            <td>
                <s:checkbox name="licenseCheck" id="licenseCheck" />
                <s:label key="ead2ese.label.license.file" for="licenseCheck"/>
            </td>
        </tr>
        <tr>
            <td></td>
            <td>
                <s:iterator value="licenseSet">
                    <s:radio name="license" id="license" list="top" listKey="value" listValue="content"></s:radio><br/>
                </s:iterator>
                (<s:label key="ead2ese.content.license.moreinfo"/> <s:a target="_blank" href="docs/Europeana%20Rights%20Guidelines.pdf" ><s:property value="getText('ead2ese.content.license.link')" /></s:a>)
                </td>
            </tr>
        <s:if test="license=='cc0' || license=='cpdm' || license=='europeana'">
            <c:set var="creativeCommonsInvisible" value="style=\"display: none;\""></c:set>
        </s:if>
        <s:if test="license=='creativecommons' || license=='cc0' || license=='cpdm'">
            <c:set var="europeanaInvisible" value="style=\"display: none;\""></c:set>
        </s:if>
        <tr id="hiddenEuropeanaLicense"  ${europeanaInvisible}>
            <td class="inputLabel"><s:label key="ead2ese.label.license.europeana" for="europeanaLicense" />*:</td>
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
            <td><s:textarea id="licenseAdditionalInformation" name="licenseAdditionalInformation" />
            </td>
        </tr>
        <s:if test="conversiontype=='true'">
            <c:set var="showMinimal" value="style=\"display: none;\""></c:set>
        </s:if>
        <tr id="hiddenInheritFileParent" ${showMinimal}>
            <td class="inputLabel">
                <s:checkbox name="inheritFileParentCheck" id="inheritFileParentCheck" value="true" onchange="changeInheritFileParentCheckState();"></s:checkbox>
                <s:label key="ead2ese.label.inherit.parent" for="inheritFileParent" />:
            </td>
            <td>
                <s:iterator value="yesNoSet">
                    <s:radio name="inheritFileParent" id="inheritFileParent" list="top" listKey="value" listValue="content"></s:radio><br/>
                </s:iterator>
            </td>
        </tr>
        <tr id="hiddenInheritOrigination" ${showMinimal}>
            <td class="inputLabel">
                <s:checkbox name="inheritOriginationCheck" id="inheritOriginationCheck" value="true" onchange="changeInheritOriginationCheckState();"></s:checkbox>
                <s:label key="ead2ese.label.inherit.origination" for="inheritOrigination" />:
            </td>
            <td>
                <s:iterator value="yesNoSet">
                    <s:radio name="inheritOrigination" id="inheritOrigination" list="top" listKey="value" listValue="content"></s:radio><br/>
                </s:iterator>
            </td>
        </tr>
        <tr id="hiddenInheritUnittitle" ${showMinimal}>
            <td class="inputLabel">
                <s:checkbox name="inheritUnittitleCheck" id="inheritUnittitleCheck" value="true" onchange="changeInheritUnittitleCheckState();"></s:checkbox>
                <s:label key="ead2ese.label.inherit.unittitle" for="inheritUnittitle" />:
            </td>
            <td>
                <s:iterator value="yesNoSet">
                    <s:radio name="inheritUnittitle" id="inheritUnittitle" list="top" listKey="value" listValue="content"></s:radio><br/>
                </s:iterator>
            </td>
        </tr>
    </table>
</div>