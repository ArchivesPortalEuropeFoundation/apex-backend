<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="apenet" uri="http://commons.apenet.eu/tags"%>
<%@ taglib prefix="dashboard" uri="http://dashboard.archivesportaleurope.eu/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title><s:property value="%{title}" /></title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link href="${pageContext.request.contextPath}/css/jquery/jquery-ui-1.8.14.custom.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery/jquery_1.4.2.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery/jquery-ui-1.8.14.custom.min.js"></script>
<script src='${pageContext.request.contextPath}/js/jquery/jquery.cookie.js' type='text/javascript'></script>
<script src='${pageContext.request.contextPath}/js/eaccpf/eaccpf_common.js' type='text/javascript'></script>
<script src='${pageContext.request.contextPath}/js/eaccpf/eaccpf.js' type='text/javascript'></script>
<link href='${pageContext.request.contextPath}/css/global.css' rel='stylesheet' type="text/css" /> 
<link href='${pageContext.request.contextPath}/css/cssnew.css' rel='stylesheet' type="text/css" />
<link href='${pageContext.request.contextPath}/css/eaccpf/eaccpfdisplay.css' rel='stylesheet' type="text/css" />
<script type='text/javascript'>
	$(document).ready(function() {
		initPrint();
	});	
</script>
</head>
<body>
	<div id="wrapper" class="wrapperEaccpfDisplay">
		<div id="header">
			<div id="logo"></div>
			<div class="left-header"></div>
			<div class="right-header"></div>
		</div>
		<div>&nbsp;</div>
		<div id="eacCpfDisplayPortlet">
			<div id="printPreview">
				<h3 id="contextInformation">
					${localizedCountryName}
					&gt; <a href="${aiCodeUrl}/${archivalInstitution.encodedRepositorycode}">${archivalInstitution.ainame}</a>
				</h3>
			 	<div id="eaccpfcontent">
				    <dashboard:eac type="eaccpfdetails" eacUrl="${eac.path}" repositoryCode="${repositoryCode}" eaccpfIdentifier="${eaccpfIdentifier}" langNavigator="${langNavigator}" translationLanguage="${translationLanguage}" />
		   		</div>
		   	</div>
	   </div> 
	</div>
</body>
</html>