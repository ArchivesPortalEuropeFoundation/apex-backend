<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="apenet" uri="http://commons.apenet.eu/tags"%>
<%@ taglib prefix="ape" uri="http://commons.archivesportaleurope.eu/tags"%>
<%@ taglib prefix="dashboard" uri="http://dashboard.archivesportaleurope.eu/tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link href="${pageContext.request.contextPath}/css/eadcontent/eadcontent.css" type="text/css" rel="stylesheet" />
<link href="${pageContext.request.contextPath}/css/eadcontent/expand.css" type="text/css" rel="stylesheet" />
<script src="${pageContext.request.contextPath}/js/jquery/jquery_1.4.2.min.js" type="text/javascript"></script>
<title>${c.unittitle}</title>

</head>
<body>
	<c:set var="element"><c:out value="${param['element']}" /></c:set>
	<c:set var="print">
		<c:out value="${param['print']}" />
	</c:set>
	<c:choose>
		<c:when test="${print=='true'}">
			<script>
				$(document).ready(function() {
					self.print();
				});
			</script>
		</c:when>
		<c:otherwise>
			<script>
				$(document).ready(function() {
					$("#print").click(function() {
						self.print();
					});
				});
			</script>
			<div id="print">
				<img id="print" align="right" src="${pageContext.request.contextPath}/images/print-icon.png" alt="print"
					title="<s:text name="label.print" />" />
			</div>
		</c:otherwise>
	</c:choose>
	<div id="header">
		<div id="leftPart">
			<img src="${pageContext.request.contextPath}/images/second_display/${countryImageName}" alt="header_dyn" />
		</div>
		<div id="rightPart">
			<img src="${pageContext.request.contextPath}/images/second_display/2nd_displ_right.png" alt="header_fix" />
		</div>
	</div>
	<c:set var="term">
		<c:out value="${param['term']}" />
	</c:set>
	<div id="eadDisplayPortlet">
		<div class="contextInformation">
			<apenet:context clevel="${c}" country="${localizedCountryName}" />
		</div>
		<dashboard:ead type="cdetails" xml="${c.xml}"  secondDisplayUrl="${pageContext.request.contextPath}/preview.action?holding=false&aiId=${archivalInstitution.aiId}" aiId="${archivalInstitution.aiId}" />
		<c:if test="${not c.leaf}">

		<div id="children" class="box">
			<div class="boxtitle">
					<div class="numberOfPages">
						<ape:pageDescription numberOfItems="${totalNumberOfChildren}" pageSize="${pageSize}" pageNumber="${pageNumber}" />
					</div>

					<c:set var="contentUrl"
						value="${pageContext.request.contextPath}/displayCContent.action?id=${c.clId}&term=${term}" />
					<div id="child-paging" class="paging">
						<ape:paging numberOfItems="${totalNumberOfChildren}" pageSize="${pageSize}" pageNumber="${pageNumber}"
							refreshUrl="${contentUrl}" pageNumberId="pageNumber"  />
					</div>
				</div>
				<dashboard:ead type="cdetails-child" xml="${childXml}" />
			</div>
		</c:if>
	</div>

</body>
</html>