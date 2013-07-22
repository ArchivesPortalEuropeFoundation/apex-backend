<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<div>
	<s:form action="createCountryManager" method="post">
		<s:actionerror />
		<s:hidden name="countryId"/>
		<s:textfield name="firstName" required="true" key="label.firstname"  />
		<s:textfield name="lastName" required="true" key="label.lastname" />
		<s:textfield name="email" required="true" key="label.email" />
		<s:submit key="label.ok" cssClass="mainButton" name="okButton" />
		<s:submit method="cancel" key="label.cancel" name="cancelButton"/>
	</s:form>
</div>
