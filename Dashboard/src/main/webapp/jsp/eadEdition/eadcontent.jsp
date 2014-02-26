<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="fileId" value="${param['id']}"/>
<c:set var="xmlTypeId" value="${param['xmlTypeId']}"/>
<script type="text/javascript">
	var messageSpecialChars = "<s:property value="getText('content.message.EadidWithSpecialCharacter')" />";
	var messageEmptyEADID = "<s:property value="getText('content.message.EadidEmpty')" />";
	var messageEmptyWhenSave = "<s:property value="getText('dashboard.editead.errorEmptyMandatoryField')" />";
	var messageNormalWithSpecialChars = "<s:property value="getText('dashboard.editead.errorNormalWithSpecialCharacter')" />";
	var messageNormalCorrecVal = "<s:property value="getText('dashboard.editead.errorCorrectValues')" />";
	var messageEmptyNormal = "<s:property value="getText('dashboard.editead.errorEmptyNormal')" />";
	var messageNotCorrectDate = "<s:property value="getText('dashboard.editead.errorInDate')" />";
	var messageEmptyTitleproper = "<s:property value="getText('dashboard.editead.errorEmptyTitleproper')" />";
	var messageEmptyPreviousLang = "<s:property value="getText('dashboard.editead.errorPreviousLanguageEmpty')" />";

	$(document).ready(function() {
		initEadTree("${fileId}", "${xmlTypeId}", messageSpecialChars, messageEmptyEADID, messageEmptyWhenSave, messageNormalWithSpecialChars, messageNormalCorrecVal, messageEmptyNormal, messageNotCorrectDate, messageEmptyTitleproper, messageEmptyPreviousLang);
	});
</script>
<div id="eadEdition">
	<div id="eadcontent">
		<div id="left-pane" class="pane">
			<div id="eadTree"></div>
		</div>
		<div id="splitter" class="pane"></div>
		<div id="right-pane" class="pane">
		
		            <p id="editionFormContainer">
                    </p>
                    <s:form method="POST" theme="simple" id="controls" cssClass="controls">
               			<input id="saveEADButton" type="button" name="saveEADButton" value="<s:property value="getText('dashboard.hgcreation.sbm.btn.save')" />" />
               			<s:submit action="contentmanager" key="dashboard.editead.btn.cancel" id="cancelEADButton" name="cancelEADButton" />
                    </s:form>
		</div>
	</div>
</div>