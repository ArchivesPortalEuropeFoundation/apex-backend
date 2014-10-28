<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div>
		<s:actionerror />
	<h2><s:text name="dashboard.setfeedbackemail.title"/></h2>
	<s:form action="setFeedbackEmailaddress" method="post" theme="simple">
	<table>
		<tr>
			<td class="inputLabel"><s:text name="dashboard.setfeedbackemail.current"/>:</td>
			<td><b><s:property value="currentFeedbackEmail"/></b></td>
		</tr>	
		<tr>
			<td class="inputLabel"><s:label key="dashboard.setfeedbackemail.new" for="feedbackEmail" /><br/>(<s:text name="dashboard.setfeedbackemail.reset"/>):</td>
			<td><s:textfield id="feedbackEmail" maxlength="255" name="feedbackEmail"/><s:fielderror fieldName="feedbackEmail"/></td>
		</tr>
	</table>
	<table>
		<tr>
			<td colspan="2"><s:submit key="label.ok" cssClass="mainButton" name="okButton" /><s:submit method="cancel" key="label.cancel" name="cancelButton" onclick="form.onsubmit=null"/>
			</td>
		</tr>
	</table>			

		
		
	</s:form>
</div>
