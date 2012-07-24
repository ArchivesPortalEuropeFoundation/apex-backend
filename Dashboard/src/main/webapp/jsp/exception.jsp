<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<div align="center">
	<s:form>
	<h2> <s:text name="exception.occured"/> </h2>
	<br></br>
	<s:actionerror />
	<s:actionmessage />
	<br></br>
	<s:submit action="index" value="Back to the Home"/>
	</s:form>
</div>
	