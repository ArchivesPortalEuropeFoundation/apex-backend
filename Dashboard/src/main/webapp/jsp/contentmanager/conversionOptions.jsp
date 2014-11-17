<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="ape" uri="http://commons.archivesportaleurope.eu/tags"%>
<%@ taglib prefix="apenet" uri="http://commons.apenet.eu/tags"%>

<s:form id="conversionOptForm" theme="simple" action="">
	<table id="optionsWizard">
		<tr id="trDefaultDaoType" >
			<td id="tdLabelDefaultDaoType" class="optionsWizardLabels">
				<s:label key="ingestionprofiles.defaultDao" for="daoType"/>:
			</td>
			<td id="tdSelectDefaultDaoType" class="optionsWizardContents">
				<s:select id="daoType" name="daoType" list="daoTypes" listKey="value" listValue="content" />
				<s:checkbox id="daoTypeCheck" name="daoTypeCheck" />
				<s:label key="ead2ese.label.type.file" for="daoTypeCheck"/>
			</td>
		</tr>
		<tr id="trRightForDigitalObject" >
			<td id="tdLabelRightForDigitalObject" class="optionsWizardLabels">
				<s:label key="content.message.default.rights.digital.objects" for="rightDigitalObjects"/>:
			</td>
			<td id="tdSelectRightForDigitalObject" class="optionsWizardContents">
				<s:select id="rightDigitalObjects" name="rightDigitalObjects" list="rightsDigitalObjects" listKey="value" listValue="content" onchange="deleteMessage($(this));" />
			</td>
		</tr>
		<tr id="trDescriptionRightForDigitalObject" >
			<td id="tdLabelDescriptionRightForDigitalObject" class="optionsWizardSubLabels">
				<s:label key="dashboard.hgcreation.label.description" for="descriptionRightForDigitalObject" />:
			</td>
			<td id="tdTextDescriptionRightForDigitalObject" class="optionsWizardContents">
				<s:textarea id="descriptionRightForDigitalObject" name="descriptionRightForDigitalObject" />
			</td>
		</tr>
		<tr id="trHolderRightForDigitalObject" >
			<td id="tdLabelHolderRightForDigitalObject" class="optionsWizardSubLabels">
				<s:label key="content.message.rights.holder" for="textHolderRightForDigitalObject" />:
			</td>
			<td id="tdTextHolderRightForDigitalObject" class="optionsWizardContents">
				<s:textfield id="textHolderRightForDigitalObject" name="textHolderRightForDigitalObject" />
			</td>
		</tr>
		<tr id="trRightForEADData" >
			<td id="tdLabelRightForEADData" class="optionsWizardLabels">
				<s:label key="content.message.default.rights.ead.data" for="rightEadData"/>:
			</td>
			<td id="tdSelectRightForEADData" class="optionsWizardContents">
				<s:select id="rightEadData" name="rightEadData" list="rightsEadData" listKey="value" listValue="content" onchange="deleteMessage($(this));" />
			</td>
		</tr>
		<tr id="trDescriptionRightForEADData" >
			<td id="tdLabelDescriptionRightForEADData" class="optionsWizardSubLabels">
				<s:label key="dashboard.hgcreation.label.description" for="descriptionRightForEADData" />:
			</td>
			<td id="tdTextDescriptionRightForEADData" class="optionsWizardContents">
				<s:textarea id="descriptionRightForEADData" name="descriptionRightForEADData" />
			</td>
		</tr>
		<tr id="trHolderRightForEADData" >
			<td id="tdLabelHolderRightForEADData" class="optionsWizardSubLabels">
				<s:label key="content.message.rights.holder" for="textHolderRightForEADData" />:
			</td>
			<td id="tdTextHolderRightForEADData" class="optionsWizardContents">
				<s:textfield id="textHolderRightForEADData" name="textHolderRightForEADData" />
			</td>
		</tr>
	</table>
	
	<div id="buttonsDiv">
		<input type="button" id="submitBtnRoleType" value="Submit" class="mainButton"/>
		<input type="button" id="cancelBtnRoleType" value="Cancel" class="otherButton"/>
	</div>
</s:form>
