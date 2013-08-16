<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<script type='text/javascript'>
	$(function() {

		$("#conversionTypefull").click(function() {
			$('#hiddenHierarchyPrefix').show();
			enableHierarchyPrefixState();
			$('#hiddenInheritFileParent').show();
			enableInheritFileParentCheckState();
			$('#hiddenInheritOrigination').show();
			enableInheritOriginationCheckState();
			$('#hiddeInheritLanguage').show();
			enableInheritLanguageCheckState();
		});

		$("#conversionTypeminimal").click(function() {
			$('#hiddenHierarchyPrefix').hide();
			disableHierarchyPrefixState($('input#hierarchyPrefixInitialValue').val());
			$('#hiddenInheritFileParent').hide();
			disableInheritFileParentCheckState();
			$('#hiddenInheritOrigination').hide();
			disableInheritOriginationCheckState();
			$('#hiddeInheritLanguage').hide();
			disableInheritLanguageCheckState();
		});

		$('#inheritLanguageprovide').click(function() {
			$('#hiddenLanguage').show();
		});
		$('#inheritLanguageyes').click(function() {
			$('#hiddenLanguage').hide();
		});
		$('#inheritLanguageno').click(function() {
			$('#hiddenLanguage').hide();
		});

		$('#dataProvidercustom').click(function() {
			$('#hiddenCustomDataProvider').show();
		});
		$('#dataProvidermapping').click(function() {
			$('#hiddenCustomDataProvider').hide();
		});

		
		$('#providercustom').click(function() {
			$('#hiddenCustomProvider').show();
		});
		$('#providerapenet').click(function() {
			$('#hiddenCustomProvider').hide();
		});
		$('#providernothing').click(function() {
			$('#hiddenCustomProvider').hide();
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

	function changeHierarchyPrefixState(value) {
		if ($("#hierarchyPrefixCheck").attr('checked')) {
			enableHierarchyPrefixState();
		} else {
			disableHierarchyPrefixState(value);
		}
	}

	function enableHierarchyPrefixState() {
		$("#hierarchyPrefix").removeAttr('disabled');
	}

	function disableHierarchyPrefixState(value) {
		$("#hierarchyPrefix").attr('disabled', 'disabled');
		$("#hierarchyPrefix").val(value);
	}

	function changeInheritFileParentCheckState() {
		if ($("#inheritFileParentCheck").attr('checked')) {
			enableInheritFileParentCheckState();
		} else {
			disableInheritFileParentCheckState();
		}
	}

	function enableInheritFileParentCheckState() {
		$("input[id^='inheritFileParent']").each(function(){
			$(this).removeAttr('disabled');
		});
	}

	function disableInheritFileParentCheckState() {
		$("input[id^='inheritFileParent']").each(function(){
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
		$("input[name^='inheritOrigination']").each(function(){
			$(this).removeAttr('disabled');
		});
	}

	function disableInheritOriginationCheckState() {
		$("input[name^='inheritOrigination']").each(function(){
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

	function changeInheritLanguageCheckState() {
		if ($("#inheritLanguageCheck").attr('checked')) {
			enableInheritLanguageCheckState();
		} else {
			disableInheritLanguageCheckState();
		}
	}

	function enableInheritLanguageCheckState() {
		$("input[id^='inheritLanguage']").each(function(){
			$(this).removeAttr('disabled');
		});
	}

	function disableInheritLanguageCheckState() {
		$("input[id^='inheritLanguage']").each(function(){
			if ($(this).attr("id") != "inheritLanguageCheck") {
				$(this).attr('disabled', 'disabled');
				if ($(this).val() == "no") {
					$(this).attr('checked', 'checked');
				} else {
					$(this).removeAttr('checked');
				}
			}
		});
		$('#hiddenLanguage').hide();
	}
</script>

<s:form method="POST" theme="simple">
	<table>
		<tr>
			<td colspan="2" class="inputLabel" style="text-align: center;">
				<s:label key="ead2ese.label.choose.conversion.type" />:
				<s:radio name="conversionType" id="conversionType" list="conversionTypeSet" listKey="value" listValue="content" required="true"></s:radio>
				<br/><br/>
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
		<tr>
			<td class="inputLabel"><s:label key="ead2ese.label.type" for="selectType" /><span class="required">*</span>:</td>
			<td><s:select id="selectType" name="daoType" list="typeSet" listKey="value" listValue="content" required="true"></s:select> <s:fielderror fieldName="daoType"/>
				<s:checkbox name="daoTypeCheck" id="daoTypeCheck" value="true"></s:checkbox>
				<s:label key="ead2ese.label.type.file" for="daoType"/>
			</td>
		</tr>
		<tr>
			<td class="inputLabel"><s:label key="ead2ese.label.license" for="license" />:</td>
			<td><s:radio name="license" id="license" list="licenseSet" listKey="value" listValue="content"></s:radio>
			<br/>(<s:label key="ead2ese.content.license.moreinfo"/><s:a href="docs/Europeana%20Rights%20Guidelines.pdf" ><s:property value="getText('ead2ese.content.license.link')" /></s:a>)
			</td>
		</tr>
		<s:if test="license=='europeana'">
			<c:set var="creativeCommonsInvisible" value="style=\"display: none;\""></c:set>
		</s:if>
		<s:else>
			<c:set var="europeanaInvisible" value="style=\"display: none;\""></c:set>
		</s:else>
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
			</td>
		</tr>
		<s:if test="conversionType=='minimal'">
			<c:set var="showMinimal" value="style=\"display: none;\""></c:set>
		</s:if>
		<tr id="hiddenHierarchyPrefix" ${showMinimal}>
			<td class="inputLabel">
				<s:hidden id="hierarchyPrefixInitialValue" value="%{hierarchyPrefix}"></s:hidden>
				<s:checkbox name="hierarchyPrefixCheck" id="hierarchyPrefixCheck" value="true" onchange="changeHierarchyPrefixState('%{hierarchyPrefix}');"></s:checkbox>
				<s:label key="ead2ese.label.hierarchy.prefix" for="hierarchyPrefix" />:
			</td>
			<td><s:textfield id="hierarchyPrefix" name="hierarchyPrefix"></s:textfield></td>
		</tr>
		<tr id="hiddenInheritFileParent" ${showMinimal}>
			<td class="inputLabel">
				<s:checkbox name="inheritFileParentCheck" id="inheritFileParentCheck" value="true" onchange="changeInheritFileParentCheckState();"></s:checkbox>
				<s:label key="ead2ese.label.inherit.parent" for="inheritFileParent" />:
			</td>
			<td><s:radio name="inheritFileParent" list="yesNoSet" listKey="value" listValue="content" id="inheritFileParent"></s:radio>
			</td>
		</tr>
		<tr id="hiddenInheritOrigination" ${showMinimal}>
			<td class="inputLabel">
				<s:checkbox name="inheritOriginationCheck" id="inheritOriginationCheck" value="true" onchange="changeInheritOriginationCheckState();"></s:checkbox>
				<s:label key="ead2ese.label.inherit.origination" for="inheritOrigination" />:
			</td>
			<td><s:radio name="inheritOrigination" list="yesNoSet" listKey="value" listValue="content"></s:radio>
			</td>
		</tr>
		<tr id="hiddeInheritLanguage" ${showMinimal}>
			<td class="inputLabel">
				<s:checkbox name="inheritLanguageCheck" id="inheritLanguageCheck" value="true" onchange="changeInheritLanguageCheckState();"></s:checkbox>
				<s:label key="ead2ese.label.inherit.language" for="inheritLanguage" /><span class="required">*</span>:
			</td>
			<td><s:radio name="inheritLanguage" id="inheritLanguage" list="inheritLanguageSet" listKey="value" listValue="content" required="true">
				</s:radio><s:fielderror fieldName="inheritLanguage"/>
			</td>
		</tr>
		<s:if test="inheritLanguage!='provide'">
			<c:set var="languageInvisible" value="style=\"display: none;\""></c:set>
		</s:if>
		<tr id="hiddenLanguage" ${languageInvisible}>
			<td class="inputLabel"><s:label key="ead2ese.label.language.select" for="language" /><span class="required">*</span>:</td>
			<td><s:select name="language" id="language" listKey="value" listValue="content" list="languages" required="true"
					value="" multiple="true" size="4"></s:select><s:fielderror fieldName="language"/>
			</td>
		</tr>
		<tr>
			<td colspan="2"><s:submit action="processEseConvert" key="ead2ese.label.next"  cssClass="mainButton"/> <s:submit action="contentmanager"
					key="ead2ese.label.cancel" />
			</td>
		</tr>
	</table>
	<br></br>
	<br></br>
	<s:hidden name="batchItems" />
	<s:hidden name="id" />
	<s:hidden name="noLanguageOnClevel"/>
	<s:hidden name="noLanguageOnParents" />
</s:form>