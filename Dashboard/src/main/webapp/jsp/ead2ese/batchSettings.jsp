<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<html>
<head>
<script src="${pageContext.request.contextPath}/js/jquery/jquery_1.4.2.min.js" type="text/javascript"></script>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/cssnew.css" type="text/css"/>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/creativecommons.css" type="text/css"/>
</head>
<body>

<script type='text/javascript'>
	$(function() {
  
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
		$('#licensecc0').click(function() {
			$('#hiddenCreativeCommonsLicense').hide();
			$('#hiddenEuropeanaLicense').hide();
		});
		$('#licensecpdm').click(function() {
			$('#hiddenCreativeCommonsLicense').hide();
			$('#hiddenEuropeanaLicense').hide();
		});	
		$('#licensecreativecommons').click(function() {
			$('#hiddenCreativeCommonsLicense').show();
			$('#hiddenEuropeanaLicense').hide();
		});
		$('#licenseeuropeana').click(function() {
			$('#hiddenCreativeCommonsLicense').hide();
			$('#hiddenEuropeanaLicense').show();
		});

	});
</script>
			<s:form  method="POST"  theme="simple">
				<s:hidden name="search"/>
				<s:hidden name="aiId"/>
				<s:hidden name="searchTerms"/>
				<s:hidden name="pageNumber"/>
	<table>
		<tr>
			<td class="inputLabel"><s:label key="ead2ese.label.type" for="selectType" /><span class="required">*</span>:</td>
			<td><s:select id="selectType" name="daoType" list="typeSet" listKey="value" listValue="content" required="true"></s:select> <s:fielderror fieldName="daoType"/>
			</td>
		</tr>
		<tr>
			<td class="inputLabel"><s:label key="ead2ese.label.hierarchy.prefix" for="hierarchyPrefix" />:</td>
			<td><s:textfield id="hierarchyPrefix" name="hierarchyPrefix"></s:textfield>
			</td>
		</tr>
		<tr>
			<td class="inputLabel"><s:label key="ead2ese.label.inherit.parent" for="inheritFileParent" />:</td>
			<td><s:radio name="inheritFileParent" list="yesNoSet" listKey="value" listValue="content" id="inheritFileParent"></s:radio>
			</td>
		</tr>
		<tr>
			<td class="inputLabel"><s:label key="ead2ese.label.inherit.origination" for="inheritOrigination" />:</td>
			<td><s:radio name="inheritOrigination" list="yesNoSet" listKey="value" listValue="content"></s:radio>
			</td>
		</tr>
		<tr>
			<td class="inputLabel"><s:label key="ead2ese.label.inherit.language" for="inheritLanguage" />:</td>
			<td><s:radio name="inheritLanguage" id="inheritLanguage" list="inheritLanguageSet" listKey="value" listValue="content"></s:radio>
			</td>
		</tr>
		<s:if test="inheritLanguage!='provide'">
			<c:set var="languageInvisible" value="style=\"display: none;\""></c:set>
		</s:if>
		<tr id="hiddenLanguage" ${languageInvisible}>
			<td class="inputLabel"><s:label key="ead2ese.label.language.select" for="language" /><span class="required">*</span>:</td>
			<td><s:select name="language" id="language" listKey="value" listValue="content" list="languages" required="true"
					value=""></s:select><s:fielderror fieldName="language"/>
			</td>
		</tr>
		<tr>
			<td class="inputLabel"><s:label key="ead2ese.label.dataprovider" for="dataProvider" /><span
				class="required">*</span>:</td>
			<td>
				<s:textfield id="textDataProvider" name="textDataProvider" required="true"/>
				<s:if test="showDataProviderCheck==true">
					<s:checkbox name="dataProviderCheck" id="dataProviderCheck" value="true"></s:checkbox>
					<s:label key="ead2ese.content.dataprovider.mapping" for="dataProviderCheck"/>
				</s:if>
				<s:fielderror fieldName="textDataProvider"/>
			</td>
		</tr>
		<tr>
			<td class="inputLabel"><s:label key="ead2ese.label.provider" for="provider" /><span
				class="required">*</span>:</td>
			<td><s:textfield id="provider" name="provider" required="true"/>
				<s:fielderror fieldName="provider"/>
			</td>
		</tr>
		<tr>
			<td class="inputLabel"><s:label key="ead2ese.label.license" for="license" />:</td>
			<td><s:radio name="license" id="license" list="licenseSet" listKey="value" listValue="content"></s:radio>(<s:label key="ead2ese.content.license.moreinfo"/><s:a href="docs/Europeana%20Rights%20Guidelines.pdf" ><s:property value="getText('ead2ese.content.license.link')" /></s:a>)
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
		<tr>
			<td colspan="2"><s:submit action="startConvertEseBatch" key="ead2ese.label.next"  cssClass="mainButton"/> 
			</td>
		</tr>
	</table>
			</s:form>
</body>
</html>

			



