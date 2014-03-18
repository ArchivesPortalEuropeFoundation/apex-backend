<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<c:set var="element">
	<c:out value="${param['element']}" />
</c:set>
<c:set var="xmlTypeId" value="${xmlTypeId}" />
<c:set var="fileId" value="${ead.id}" />
<c:set var="countryImageName">${countryImageName}</c:set>
<c:set var="solrId">
	<c:out value="${param['solrid']}" />
</c:set>
<c:set var="term">
	<c:out value="${param['term']}" />
</c:set>
<c:choose>
	<c:when test="${not empty solrId}">
		<c:set var="contentUrl"
			value="${pageContext.request.contextPath}/displayCContent.action?id=${solrId}&term=${term}&element=${element}&aiId=${archivalInstitution.aiId}" />
	</c:when>
	<c:otherwise>
		<c:set var="contentUrl"
			value="${pageContext.request.contextPath}/displayEadFrontPage.action?fileId=${fileId}&xmlTypeId=${xmlTypeId}" />
	</c:otherwise>
</c:choose>
<head>
<title><s:property value="%{title}" /></title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link href="${pageContext.request.contextPath}/css/jquery/jquery-ui-1.8.14.custom.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery/jquery_1.4.2.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery/jquery-ui-1.8.14.custom.min.js"></script>
<script src='${pageContext.request.contextPath}/js/jquery/jquery.cookie.js' type='text/javascript'></script>
<script src='${pageContext.request.contextPath}/js/dynatree/jquery.dynatree.js' type='text/javascript'></script>
<script src='${pageContext.request.contextPath}/js/browsers.js' type='text/javascript'></script>
<link href='${pageContext.request.contextPath}/js/dynatree/skin/ui.dynatree.css' rel='stylesheet' type='text/css' />
<link href='${pageContext.request.contextPath}/css/secondDisplay.css' rel='stylesheet' type="text/css" />
<link href='${pageContext.request.contextPath}/css/global.css' rel='stylesheet' type="text/css" />
<link href='${pageContext.request.contextPath}/css/cssnew.css' rel='stylesheet' type="text/css" />
<script type='text/javascript'>
	$(function() {
		// --- Initialize sample trees
		$("#tree")
				.dynatree(
						{
							title : "Bestandsdelen",
							rootVisible : false,
							autoFocus : false,
							fx : {
								height : "toggle",
								duration : 200
							},
							// In real life we would call a URL on the server like this:
							initAjax : {
								url : "${pageContext.request.contextPath}/generateTreeJSON.action",
								data : {
									fileId : "${fileId}",
									xmlTypeId : "${xmlTypeId}",
									solrId : "${solrId}"
								}
							},

							onActivate : function(dtnode) {
								// Use our custom attribute to load the new target content:
								if (dtnode.data.url)
									parent
											.$("[name=contentFrame]")
											.attr(
													"src",
													dtnode.data.url
															+ "&aiId=${archivalInstitution.aiId}");
							},

							onLazyRead : function(dtnode) {
								if (dtnode.data.more == "after") {
									dtnode.parent
											.appendAjaxWithoutRemove({
												url : "${pageContext.request.contextPath}/generateTreeJSON.action",
												data : {
													parentId : dtnode.data.parentId,
													orderId : dtnode.data.orderId,
													ecId : dtnode.data.ecId,
													more : dtnode.data.more
												}
											});
									var parent = dtnode.parent;
									dtnode.remove();
									var children = parent.getChildren();
									var index = children.length - 1;
									var lastChild = children[index];
									var relativeTop = $('#tree').scrollTop()
											+ $(lastChild.span).offset().top
											- $("#tree").offset().top - 40;
									$('#tree').animate({
										scrollTop : relativeTop
									}, 500);
								} else if (dtnode.data.more == "before") {
									dtnode.parent
											.insertBeforeAjaxWithoutRemove(
													{
														url : "${pageContext.request.contextPath}/generateTreeJSON.action",
														data : {
															parentId : dtnode.data.parentId,
															orderId : dtnode.data.orderId,
															ecId : dtnode.data.ecId,
															more : dtnode.data.more,
															max : dtnode.data.max
														}
													}, dtnode);
									dtnode.remove();
								}

								else {
									dtnode
											.appendAjax({
												url : "${pageContext.request.contextPath}/generateTreeJSON.action",
												data : {
													parentId : dtnode.data.id
												}

											});
								}
							},
							minExpandLevel : 2
						});
		$(window).resize(function() {
			resizePage();
		});

		resizePage();
	});
	function resizePage() {
		$("#frames").height($(window).height() - $("#header").height());
	}
	function redirect(country){
	    var hostname = window.location.hostname;	
	    var finalHref;
        if (hostname == "contentchecker.archivesportaleurope.net"){ 
           finalHref = "http://contentchecker.archivesportaleurope.net/"+ country + "/home";	
        }else{
			finalHref = "http://www.archivesportaleurope.net/" + country + "/home";
		}
       location.href = finalHref;
	}
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
		<div class="contextInformation">${localizedCountryName} &gt; ${archivalInstitution.ainame}</div>
		<div id="frames">
			<div id="left">
				<div id="tree"></div>
			</div>
			<div id="right">
				<iframe src="${contentUrl}" name="contentFrame" width="100%" height="100%" scrolling="auto" marginheight="0"
					marginwidth="0" frameborder="0">
					<p>Your browser does not support iframes</p>
				</iframe>
			</div>
		</div>
	</div>
	<script type="text/javascript">
		var msg = "<s:property value="getText('header.browser')" />";
		$(document).ready(checkBrowser(msg));
	</script>
</body>
</html>