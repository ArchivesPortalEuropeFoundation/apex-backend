<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:choose>
<c:when test="${xmlTypeId == '1'}">
    <div id="HGnoResults" class="left">
        <a href="hgTreeCreation.action"><s:text name="dashboard.hgcreation.title"/></a>
    </div>
</c:when>
<c:when test="${xmlTypeId == '3'}">
    <div id="HGnoResults" class="left">
        <a href="sgTreeCreation.action"><s:text name="dashboard.sgcreation.title"/></a>
    </div>
</c:when>
</c:choose>

<div id="noresults"><s:text name="content.message.noresults" /></div>

<script type="text/javascript">
    $(document).ready(function() {
    	initContentManager(${xmlTypeId});
    	clearFilesFromSession();
	});
</script>