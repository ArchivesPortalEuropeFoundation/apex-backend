<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="ape" uri="http://commons.archivesportaleurope.eu/tags"%>
<%@ taglib prefix="dashboard" uri="http://dashboard.archivesportaleurope.eu/tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link href="${pageContext.request.contextPath}/css/eadcontent/eadcontent.css" type="text/css" rel="stylesheet" />
<script src="${pageContext.request.contextPath}/js/jquery/jquery_1.4.2.min.js" type="text/javascript"></script>
<title>${c.unittitle}</title>


</head>
<body>
	<c:set var="aiId"><c:out value="${param['aiId']}" /></c:set>
	<c:set var="printUrl"
		value="${pageContext.request.contextPath}/displayCContentPreview.action?id=${c.clId}&pageNumber=${pageNumber}&print=true" />
	<script>
		$(document)
				.ready(
						function() {
							$("#print")
									.click(
											function() {
												var preview = window
														.open(
																'${printUrl}',
																'printpreview',
																'width=1000,height=600,left=10,top=10,menubar=0,toolbar=0,status=0,location=0,scrollbars=1,resizable=1');
												preview.focus();
											});
						});
	</script>
	<div id="print">
		<img id="print" align="right" src="${pageContext.request.contextPath}/images/print-icon.png" alt="print"
			title="<s:text name="label.print" />" />
	</div>
	<div id="eadDisplayPortlet">
		<dashboard:ead type="cdetails" xml="${c.xml}" secondDisplayUrl="${pageContext.request.contextPath}/preview.action?xmlTypeId=0&aiId=${aiId}" aiId="${aiId}" />
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