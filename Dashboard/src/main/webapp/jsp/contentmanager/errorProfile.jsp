<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="ape" uri="http://commons.archivesportaleurope.eu/tags"%>

<link rel="stylesheet" href="${pageContext.request.contextPath}/css/ingestionprofile.css" type="text/css"/>

<s:form method="POST" theme="simple">
	<table>
		<tr>
			<td colspan="2">
                <s:actionerror />
                <br/>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<s:submit action="contentmanager" key="dashboard.editead.btn.cancelandquit" />
			</td>
		</tr>
	</table>

	<s:hidden id ="batchItems" name="batchItems" />
	<s:hidden name="id" />
	<s:hidden name="xmlTypeId" />
	<s:hidden name="selectedProfile" id="selectedProfile"/>
</s:form>