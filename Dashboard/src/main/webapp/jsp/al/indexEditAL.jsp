<%@page import="java.util.Iterator"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="com.opensymphony.xwork2.ActionContext"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<script type="text/javascript">
	$(document).ready(function(){
		loadUpPart("${pageContext.request.contextPath}","<s:property value="getText('viewAL.tree')" />","${countryId}");
	});
</script>
<div id="informationDiv"></div>
<div id="archivalLandscapeEditor">
	<div id="archivalLandscapeEditorUp" class="alEUp">
		<img alt="loading..." src="images/waiting.gif">
	</div>
	<div id="archivalLandscapeEditorDown">
		<img style="display:none;" id="loadingImg" alt="loading..." src="images/waiting.gif">
		<div id="divForm">
			<div id="appendInstitutionsGroups">
				<div id="filterSelectContainer" class="filterContainer">
					<div class="firstFilterDiv">
						<label for="textAL"><s:property value="getText('al.message.name')" />:</label>
						<input type="text" name="textAL" id="textAL" class="inputTextBar" />
					</div>
					<div class="secondFilterDiv">
						<label for="element" style="padding-right:4px;"><s:property value="getText('al.message.element')" />:</label>
						<select name="element" id="element">
							<option value="series" ><s:property value="getText('al.message.series')"/></option>
							<option selected="selected" value="file"><s:property value="getText('al.message.file')"/></option>
						</select>
					</div>
					<div class="secondFilterDiv" >
						<label for="selectedLang"><s:property value="getText('al.message.selectlanguage')"/>:</label>
						<select name="selectedLang" id="selectedLang" >
							<s:iterator var="row" value="langList">
								<s:if test='#row.getIsoname().toLowerCase().equals("eng")'>
									<option selected="selected" value="<s:property value="#row.getIsoname().toLowerCase()"/>"><s:property value="#row.getLname()" /></option>
								</s:if>
								<s:else>
									<option value="<s:property value="#row.getIsoname()"/>"><s:property value="#row.getLname()" /></option>
								</s:else>
							</s:iterator>
						</select>
					</div>
					<div id="addDiv" class="divAction" onclick="appendNode();"><s:property value="getText('al.message.addtolist')" /></div>
				</div>
			</div>
		</div>
		<div id="editorActions" class="filterContainer">
			<div id="moveUpDiv" onclick="moveUp();" class="divAction"><s:property value="getText('al.message.up')" /></div>
			<div id="moveDownDiv" onclick="moveDown();" class="divAction"><s:property value="getText('al.message.down')" /></div>
			<div id="deleteDiv" onclick="deleteNode();" class="divAction"><s:property value="getText('al.message.modify')" /></div>
		</div>
		<div class="filterContainer" id="divGroupNodesContainer" >
			<!-- build select here for change_group-select -->
			<div id="changeNodeDiv" onclick="changeGroup();" class="divActionChangeNode"><s:property value="getText('al.message.changeGroup')" /></div>
		</div>
		
		<div id="alternativesNamesDiv" class="filterContainer">
			<div id="showLanguagesDiv" onclick="getAlternativeNames();" class="divAction"><s:property value="getText('al.message.showalternativenames')" /></div>
			<div id="editDiv" onclick="editAlternativeNames();" class="divAction"><s:property value="getText('al.message.edittarget')" /></div>
		</div>
		
		<div id="editLanguagesDiv" class="filterContainer">
			<p class="ALP2">
				<label style="float:left;" for="target"><s:property value="getText('al.message.anwritelanguage')"/>: </label>
				<input type="text" name="target" id="target">
			</p>
			<p class="ALP">
				<label for="selectedLangTranslations" class="leftSpace"><s:property value="getText('al.message.anselectlanguage')"/>: </label>
				<select name="selectedLangTranslations" id="selectedLangTranslations" onchange="recoverAlternativeName();" >
					<s:iterator var="row" value="langList">
						<%-- <s:if test='#row.getIsoname().toLowerCase().equals("eng")'>
							<option selected="selected" value="<s:property value="#row.getIsoname().toLowerCase()"/>"><s:property value="#row.getLname()" /></option>
						</s:if>
						<s:else>--%>
							<option value="<s:property value="#row.getIsoname()"/>" <%-- <s:if test="#row.getIsoname().toLowerCase().equals('eng')">selected="selected"</s:if>--%> ><s:property value="#row.getLname()" /></option>
						<%--</s:else>--%>
					</s:iterator>
				</select>
			</p>
			<div class="divAction" id="editTargetSubmitDiv" onclick="sendAlternativeNames();">
				<s:property value="getText('al.message.editalternativenames')"/>
			</div>
			<div class="divAction" id="deleteTargetSubmitDiv" onclick="deleteAlternativeNames();">
				<s:property value="getText('al.message.deletetarget')"/>
			</div>
			<div class="divAction" id="editTargetCancelDiv" onclick="cancelEditAlternativeNames();">
				<s:property value="getText('al.message.canceledittarget')"/>
			</div>
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
