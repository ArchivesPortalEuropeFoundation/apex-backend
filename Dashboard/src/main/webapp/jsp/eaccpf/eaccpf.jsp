<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="apenet" uri="http://commons.apenet.eu/tags"%>
<%@ taglib prefix="dashboard" uri="http://dashboard.archivesportaleurope.eu/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<c:set var="element">
	<c:out value="${param['element']}" />
</c:set>
<c:set var="xmlTypeId" value="${xmlTypeId}" />
<c:set var="fileId" value="${eac.id}" />
<c:set var="term">
	<c:out value="${param['term']}" />
</c:set>
<c:set var="printEacDetailsUrl" value="${pageContext.request.contextPath}/printEacCpfDetails.action?id=${fileId}&xmlTypeId=${xmlTypeId}&print=true"/>
<head>
<title><s:property value="%{title}" /></title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link href="${pageContext.request.contextPath}/css/jquery/jquery-ui-1.8.14.custom.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery/jquery_1.4.2.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery/jquery-ui-1.8.14.custom.min.js"></script>
<script src='${pageContext.request.contextPath}/js/jquery/jquery.cookie.js' type='text/javascript'></script>
<script src='${pageContext.request.contextPath}/js/eaccpf/eaccpf.js' type='text/javascript'></script>
<script src='${pageContext.request.contextPath}/js/browsers.js' type='text/javascript'></script>
<link href='${pageContext.request.contextPath}/css/global.css' rel='stylesheet' type="text/css" /> 
<link href='${pageContext.request.contextPath}/css/cssnew.css' rel='stylesheet' type="text/css" />
<link href='${pageContext.request.contextPath}/css/eaccpf/eaccpfdisplay.css' rel='stylesheet' type="text/css" />
<script type='text/javascript'>
	$(document).ready(function() {
		init();	
		makeRelationsCollapsible();
	});	
</script>
</head>
<body>
	<div id="wrapper">
		<div id="browser"></div>
		<div id="header">
			<a href="#" onclick="redirect('${locale}')"><div id="logo"></div></a>
			<div class="left-header"></div>
			<div class="right-header"></div>
		</div>
		<div>&nbsp;</div>
		<div id="eacCpfDisplayPortlet">
			<div id="printEacDetails">
				<a href="javascript:printEacDetails('${printEacDetailsUrl}')">
					<img  id="printEacDetails" align="right" src="${pageContext.request.contextPath}/images/print-icon.png" alt="print" title="<s:text name="label.print" />" />
				</a>
			</div>	
		 	<div id="eaccpfcontent">
			    <dashboard:eac type="eaccpfdetails" eacUrl="${eac.path}" repositoryCode="${repositoryCode}" eaccpfIdentifier="${eaccpfIdentifier}" />
	   		</div>
	   </div> 
	</div>
	<script type="text/javascript">
			var msg = "<s:property value="getText('header.browser')" />";
			$(document).ready(checkBrowser(msg));
	</script>
</body>
</html>