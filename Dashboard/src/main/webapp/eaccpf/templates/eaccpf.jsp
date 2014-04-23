<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<tiles:useAttribute id="cssInternal" name="cssfilesInternal" classname="java.util.List" />
<tiles:useAttribute id="jsInternal" name="jsfilesInternal" classname="java.util.List" />
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <meta content="text/html; charset=UTF-8;" http-equiv="content-type" />
        <meta name="Description" content="APENET" />
        <meta name="Keywords" content="archives portal europe, apenet, europeana, eac-cpf" />
        <title><tiles:getAsString name="title"/></title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}<tiles:getAsString name="maincss"/>" type="text/css"/>
        <c:forEach var="item" items="${jsInternal}">
            <script src="${pageContext.request.contextPath}${item}" type="text/javascript"></script>
        </c:forEach>
        <c:forEach var="item" items="${cssInternal}">
            <link rel="stylesheet" href="${pageContext.request.contextPath}${item}" type="text/css" />
        </c:forEach>
    </head>
    <body>
        <div id="principal">
            <div id="wrap">			    
                <div id="main" class="main">
                    <div id="body" class="body"><tiles:insertAttribute name="body"/></div>
                </div>
            </div>
        </div>
    </body>
</html>