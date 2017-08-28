<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<link rel="stylesheet" href="${pageContext.request.contextPath}/css/ead2edm/ead2edm.css" type="text/css"/>

<script type='text/javascript'>
    $(function () {

        $("#conversionTypefull").click(function () {
            $('#hiddenInheritFileParent').show();
            enableInheritFileParentCheckState();
            $('#hiddenInheritOrigination').show();
            enableInheritOriginationCheckState();
            $('#hiddenInheritUnittitle').show();
            enableInheritUnittitleCheckState();
        });

        $("#conversionTypeminimal").click(function () {
            $('#hiddenInheritFileParent').hide();
            disableInheritFileParentCheckState();
            $('#hiddenInheritOrigination').hide();
            disableInheritOriginationCheckState();
            $('#hiddenInheritUnittitle').hide();
            disableInheritUnittitleCheckState();
        });

        $('#inheritLanguageprovide').click(function () {
            $('#hiddenLanguage').show();
        });
        $('#inheritLanguageyes').click(function () {
            $('#hiddenLanguage').hide();
        });
        $('#inheritLanguageno').click(function () {
            $('#hiddenLanguage').hide();
        });

        $('#dataProvidercustom').click(function () {
            $('#hiddenCustomDataProvider').show();
        });
        $('#dataProvidermapping').click(function () {
            $('#hiddenCustomDataProvider').hide();
        });


        $('#providercustom').click(function () {
            $('#hiddenCustomProvider').show();
        });
        $('#providerapenet').click(function () {
            $('#hiddenCustomProvider').hide();
        });
        $('#providernothing').click(function () {
            $('#hiddenCustomProvider').hide();
        });

        $('#licensecreativecommons').click(function () {
            $('#hiddenCreativeCommonsLicense').show();
            $('#hiddenEuropeanaLicense').hide();
        });
        $('#licensecc0').click(function () {
            $('#hiddenCreativeCommonsLicense').hide();
            $('#hiddenEuropeanaLicense').hide();
        });
        $('#licensecpdm').click(function () {
            $('#hiddenCreativeCommonsLicense').hide();
            $('#hiddenEuropeanaLicense').hide();
        });
        $('#licenseeuropeana').click(function () {
            $('#hiddenCreativeCommonsLicense').hide();
            $('#hiddenEuropeanaLicense').show();
        });
        $('#licenseoutofcopyright').click(function () {
            $('#hiddenCreativeCommonsLicense').hide();
            $('#hiddenEuropeanaLicense').hide();
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
        $("input[id^='inheritFileParent']").each(function () {
            $(this).removeAttr('disabled');
        });
    }

    function disableInheritFileParentCheckState() {
        $("input[id^='inheritFileParent']").each(function () {
            if ($(this).attr("id") != "inheritFileParentCheck") {
                $(this).attr('disabled', 'disabled');
                if ($(this).val() == "no") {
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
        $("input[name^='inheritOrigination']").each(function () {
            $(this).removeAttr('disabled');
        });
    }

    function disableInheritOriginationCheckState() {
        $("input[name^='inheritOrigination']").each(function () {
            if ($(this).attr("name") != "inheritOriginationCheck") {
                $(this).attr('disabled', 'disabled');
                if ($(this).val() == "no") {
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
        $("input[name^='inheritUnittitle']").each(function () {
            $(this).removeAttr('disabled');
        });
    }

    function disableInheritUnittitleCheckState() {
        $("input[name^='inheritUnittitle']").each(function () {
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

    function setMandatoryField() {
        $("input[name^='inheritLanguage']").each(function () {
            if ($(this).attr("checked") == "checked") {
                var text = $("tr#trLanguageOfTheMaterial label[for='languageOfTheMaterial']").text();

                if ($(this).val() == "provide") {
                    text += "*";
                } else {
                    text = text.replace("*", "");
                }

                $("tr#trLanguageOfTheMaterial label[for='languageOfTheMaterial']").text(text);
            }
        });
    }
</script>

<s:form method="POST" theme="simple">
    <table id="conversionToEDMForm">
        <tr>
            <td colspan="2" class="titleLabel">
                <s:label key="ead2ese.label.general.settings" />
            </td>
        </tr>
        <tr>
            <td class="inputLabel">
                <s:label key="ead2ese.label.choose.conversion.type"  for="conversionType" />:
            </td>
            <td>
                <s:iterator value="conversionTypeSet">
                    <s:radio name="conversionType" id="conversionType" list="top" listKey="value" listValue="content"></s:radio><br/>
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
                <s:if test="hasArchdescUnittitle or hasTitlestmtTitleproper">
                    <s:iterator value="sourceOfFondsTitleSet">
                        <s:radio name="sourceOfFondsTitle" id="sourceOfFondsTitle" list="top" listKey="value" listValue="content"></s:radio><br/>
                    </s:iterator>
                    <s:fielderror fieldName="sourceOfFondsTitle"/>
                </s:if>
                <s:else>
                    <s:text name="ead2edm.message.fondsTitle.noSourceAvailable"/>
                </s:else>
            </td>
        </tr>
        <tr>
            <td colspan="2" class="titleLabel">
                <s:label key="ead2ese.label.specific.settings" />
            </td>
        </tr>
        <tr>
            <td class="inputLabel"><s:label key="ead2ese.label.dataprovider" for="dataProvider" /><span
                    class="required">*</span>:</td>
            <td>
                <s:textfield id="textDataProvider" name="textDataProvider" required="true"/>
                <s:if test="dataProviderCheck==true">
                    <s:checkbox name="dataProviderCheck" id="dataProviderCheck" value="true"></s:checkbox>
                </s:if>
                <s:else>
                    <s:checkbox name="dataProviderCheck" id="dataProviderCheck" value="false"></s:checkbox>
                </s:else>
                <s:label key="ead2ese.content.dataprovider.file" for="dataProviderCheck"/>
                <s:fielderror fieldName="textDataProvider"/>
            </td>
        </tr>
        <tr id="trSelectType">
            <td class="inputLabel"><s:label key="ead2ese.label.type" for="selectType" /><span class="required">*</span>:</td>
            <td><s:select id="selectType" name="daoType" list="typeSet" listKey="value" listValue="content" required="true"></s:select>
                <s:checkbox name="daoTypeCheck" id="daoTypeCheck" value="true"></s:checkbox>
                <s:label key="ead2ese.label.type.file" for="daoType"/>
                <s:fielderror fieldName="daoType"/>
            </td>
        </tr>

        <s:if test="batchConversion==false">
            <tr id="trInheritLanguage">
                <td class="inputLabel">
                    <s:label key="ead2ese.label.inherit.language" for="inheritLanguage" /><span class="required">*</span>:
                </td>
                <td>
                    <s:iterator value="inheritLanguageSet">
                        <s:radio name="inheritLanguage" id="inheritLanguage" list="top" listKey="value" listValue="content"></s:radio><br/>
                    </s:iterator>
                    <s:fielderror fieldName="inheritLanguage"/>
                </td>
            </tr>
        </s:if>

        <tr id="trLanguageOfTheMaterial">
            <td class="inputLabel">
                <s:if test="batchConversion==true">
                    <s:label key="ead2ese.label.language.material" for="languageOfTheMaterial" /><span class="required">*</span>:
                </s:if>
                <s:else>
                    <s:label key="ead2ese.label.language.material" for="languageOfTheMaterial" />:
                </s:else>
            </td>
            <td class="tdVertical">
                <s:select name="languageSelection" id="languageSelection" listKey="value" listValue="content" list="languages" required="true"
                          value="" multiple="true" size="4"></s:select>

                <s:if test="languageOfTheMaterialCheck==true">
                    <s:checkbox name="languageOfTheMaterialCheck" id="languageOfTheMaterialCheck" value="true"></s:checkbox>
                </s:if>
                <s:else>
                    <s:checkbox name="languageOfTheMaterialCheck" id="languageOfTheMaterialCheck" value="false"></s:checkbox>
                </s:else>
                <s:label key="ead2ese.label.language.file" for="languageOfTheMaterialCheck"/>
                <s:fielderror fieldName="languageSelection"/>
                <s:fielderror fieldName="languageOfTheMaterialCheck"/>
            </td>
        </tr>

        <tr>
            <td class="inputLabel"><s:label key="ead2ese.label.license" for="license" />:</td>
            <td>
                <s:if test="licenseCheck==true">
                    <s:checkbox name="licenseCheck" id="licenseCheck" value="true"></s:checkbox>
                </s:if>
                <s:else>
                    <s:checkbox name="licenseCheck" id="licenseCheck" value="false"></s:checkbox>
                </s:else>
                <s:label key="ead2ese.label.license.file" for="licenseCheck"/>
            </td>
        </tr>
        <tr>
            <td></td>
            <td>
                <s:iterator value="licenseSet">
                    <s:radio name="license" id="license" list="top" listKey="value" listValue="content"></s:radio><br/>
                </s:iterator><s:fielderror fieldName="license"/>
                (<s:label key="ead2ese.content.license.moreinfo"/>: <s:a target="_blank" href="http://pro.europeana.eu/share-your-data/rights-statement-guidelines/available-rights-statements" ><s:property value="getText('ead2ese.content.license.link')" /></s:a>)
                </td>
            </tr>
        <s:if test="license==null || license=='cc0' || license=='cpdm' || license=='europeana'">
            <c:set var="creativeCommonsInvisible" value="style=\"display: none;\""></c:set>
        </s:if>
        <s:if test="license==null || license=='cc0' || license=='cpdm' || license=='creativecommons'">
            <c:set var="europeanaInvisible" value="style=\"display: none;\""></c:set>
        </s:if>
        <tr id="hiddenEuropeanaLicense"  ${europeanaInvisible}>
            <td class="inputLabel"><s:label key="ead2ese.label.license.europeana" for="europeanaLicense" /><span
                    class="required">*</span>:</td>
            <td><s:select id="europeanaLicense" name="europeanaLicense" list="europeanaLicenseSet" listKey="value" listValue="content" required="true"/><s:fielderror fieldName="europeanaLicense"/>
            </td>
        </tr>				
        <tr id="hiddenCreativeCommonsLicense" ${creativeCommonsInvisible}>
            <td colspan="2">     <div id="cc_js_widget_container">
                    <script type="text/javascript" src="https://api.creativecommons.org/jswidget/tags/0.97/complete.js?locale=en_US&amp;want_a_license=definitely"></script>
                </div>
            </td>
        </tr>
        <tr>
            <td class="inputLabel"><s:label key="ead2ese.label.license.additional" for="licenseAdditionalInformation" />:</td>
            <td><s:textarea id="licenseAdditionalInformation" name="licenseAdditionalInformation"/>
                <s:fielderror fieldName="licenseAdditionalInformationCheck"/>
            </td>
        </tr>
        <s:if test="conversionType=='minimal'">
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
        <tr>
            <td colspan="2">
                <s:submit id="processEDMConvert" action="processEdmConvert" key="ead2ese.label.next" cssClass="mainButton"/>
                <s:submit id="cancelEDMConvert" action="contentmanager" key="ead2ese.label.cancel" />
            </td>
        </tr>
    </table>

    <s:hidden name="batchItems" />
    <s:hidden name="batchConversion" />
    <s:hidden name="id" />
    <s:hidden name="noLanguageOnClevel"/>
    <s:hidden name="noLanguageOnParents" />
    <s:hidden name="hasArchdescUnittitle" />
    <s:hidden name="hasTitlestmtTitleproper" />
</s:form>