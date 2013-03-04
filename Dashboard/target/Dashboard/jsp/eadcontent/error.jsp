<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title><s:property value="%{title}" /></title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link href='${pageContext.request.contextPath}/css/secondDisplay.css' rel='stylesheet' type="text/css" />
<title>Error</title>
</head>
<body>
	<div id="wrapper">
		<div id="header">
			<div id="leftPart">
				<img src="${pageContext.request.contextPath}/images/second_display/header_default.png" alt="header_dyn" />
			</div>
			<div id="rightPart">
				<img src="${pageContext.request.contextPath}/images/second_display/2nd_displ_right.png" alt="header_fix" />
			</div>
		</div>
		<div class="error">
			<s:form>
				<s:actionmessage />
				<s:actionerror />
			</s:form>
		</div>
	</div>
</body>
</html>