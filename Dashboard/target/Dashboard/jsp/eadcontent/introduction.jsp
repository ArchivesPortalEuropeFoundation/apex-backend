<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="apenet" uri="http://commons.apenet.eu/tags"%>
<%@ taglib prefix="dashboard" uri="http://dashboard.archivesportaleurope.eu/tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<c:set var="xmlTypeId"><c:out value="${param['xmlTypeId']}" /></c:set>
<c:set var="fileId"><c:out value="${param['fileId']}" /></c:set>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<link href="${pageContext.request.contextPath}/css/eadcontent/eadcontent.css" type="text/css" rel="stylesheet" />
<script src="${pageContext.request.contextPath}/js/jquery/jquery_1.4.2.min.js" type="text/javascript"></script>
<title>${eadContent.titleproper}</title>
<c:set var="expandall">
	<s:property value="getText('eadcontent.expandall')" escapeHtml="false"/>
</c:set>
<c:set var="collapseall">
	<s:property value="getText('eadcontent.collapseall')" escapeHtml="false"/>
</c:set>

</head>
<body>

<c:set var="print"><c:out value="${param['print']}" /></c:set>
<c:choose>
<c:when test="${print=='true'}">
	<script>
		
	    $(document).ready(function() {
			self.print(); 	
	    });
	</script>
    <div id="header">
        <div id="leftPart">
            <img src="${pageContext.request.contextPath}/images/second_display/${countryImageName}" alt="header_dyn"/>
        </div>
        <div id="rightPart">
            <img src="${pageContext.request.contextPath}/images/second_display/2nd_displ_right.png" alt="header_fix"/>
        </div>
    </div>
	<div id="contextHierarchy">
	<apenet:context eadContent="${eadContent}" onlyArchives="true" country="${localizedCountryName}"/>
</div>
</c:when>
<c:otherwise>
<c:set var="printUrl" value="${pageContext.request.contextPath}/displayEadIntroduction.action?fileId=${fileId}&xmlTypeId=${xmlTypeId}&print=true"/>
<script>
    $(document).ready(function() {
        $("#print").click(function() {
        	var preview =window.open('${printUrl}','printpreview', 'width=1000,height=600,left=10,top=10,menubar=0,toolbar=0,status=0,location=0,scrollbars=1,resizable=1');  
        	preview.focus();
        });    	
    });
</script>
<div id="print"><img  id="print" align="right" src="${pageContext.request.contextPath}/images/print-icon.png" alt="print" title="<s:text name="label.print" />" /></div>	
</c:otherwise>
</c:choose>
<div id="eadDisplayPortlet">
<dashboard:ead type="introduction" xml="${eadContent.xml}" />
</div>
</body>
</html>