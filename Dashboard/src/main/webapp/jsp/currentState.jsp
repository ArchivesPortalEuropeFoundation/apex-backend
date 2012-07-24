<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
	<head>
		<title>Archives Portal Europe - Dashboard - Current State</title>
	</head>
<body>
	<div >
		<s:form>
		<s:actionerror />
		<s:actionmessage />
		<br></br>
		<s:hidden name= "currentStateDDBB"></s:hidden>
	   	<s:hidden name="currentStateNFS"></s:hidden>
	   	<s:hidden name="currentStateSOLR"></s:hidden>
	   	 <s:hidden name="currentStateTOMCAT"></s:hidden>
		<s:submit action="index" value="Back to the Home"/>		
		</s:form>
	</div>
</body>	
</html>

