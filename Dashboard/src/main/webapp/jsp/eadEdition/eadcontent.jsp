<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="fileId" value="${param['id']}"/>
<c:set var="xmlTypeId" value="${param['xmlTypeId']}"/>
<script type="text/javascript">
	var messageEmptyEADID = "<s:property value="getText('content.message.EadidEmpty')" />";
	var messageEmptyWhenSave = "<s:property value="getText('dashboard.editead.errorEmptyMandatoryField')" />";
	var messageNormalCorrecVal = "<s:property value="getText('dashboard.editead.errorCorrectValues')" />";
	var messageEmptyNormal = "<s:property value="getText('dashboard.editead.errorEmptyNormal')" />";
	var messageEmptyTitleproper = "<s:property value="getText('dashboard.editead.errorEmptyTitleproper')" />";
	var messageEmptyPreviousLang = "<s:property value="getText('dashboard.editead.errorPreviousLanguageEmpty')" />";
	var messageInvalidCountrycode = "<s:property value="getText('dashboard.editead.errorInvalidCountrycode')" />";
	var messageEmptyCountrycode = "<s:property value="getText('dashboard.editead.errorEmptyCountrycode')" />";
	var messageEmptyMainagencycode = "<s:property value="getText('dashboard.editead.errorEmptyMainagencycode')" />";
	var messagePleaseSaveChanges = "<s:property value="getText('dashboard.editead.errorPleaseSaveChanges')" />";

	$(document).ready(function() {
		initEadTree("${fileId}", "${xmlTypeId}", messageEmptyEADID, messageEmptyWhenSave, messageInvalidCountrycode, messageEmptyCountrycode, messageEmptyMainagencycode, messageNormalCorrecVal, messageEmptyNormal, messageEmptyTitleproper, messageEmptyPreviousLang, messagePleaseSaveChanges);
	});
</script>
<div id="eadEdition">
	<div id="eadcontent">
		<div id="left-pane" class="pane">
			<div id="eadTree"></div>
		</div>
		<div id="splitter" class="pane"></div>
		<div id="right-pane" class="pane">
			<div id="informationDiv"></div>
			<input type="hidden" id="changed" value="false" />

			<p id="editionFormContainer">
			</p>

			<s:form method="POST" theme="simple" id="controls" cssClass="controls">
				<input id="saveEADButton" type="button" name="saveEADButton" value="<s:property value="getText('dashboard.hgcreation.sbm.btn.save')" />" />
				<s:submit action="contentmanager" key="dashboard.editead.btn.cancel" id="cancelEADButton" name="cancelEADButton" />
			</s:form>
		</div>
	</div>

	<div class="hidden">
		<div id="processingInfoDiv">
			<table id="processingInfoTable">
				<tr>
					<td>
						<img id="processingInfoImg" src="images/colorbox/loading.gif" />
					</td>
					<td>
						<label class="bold" id="processingInfoLabel" for="processingInfoImg">
							<s:text name="al.message.processing" />
						</label>
					</td>
				</tr>
			</table>
		</div>
	</div>
</div>