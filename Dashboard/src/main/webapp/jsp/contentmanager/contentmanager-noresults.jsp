<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:if test="${xmlTypeId == '1'}">
    <div class="left">
        <a href="hgTreeCreation.action"><s:text name="dashboard.hgcreation.title"/></a>
    </div>
</c:if>
<div id="noresults"><s:text name="content.message.noresults" /></div>

<script type="text/javascript">
    $(document).ready(function() {
    	initContentManager(${xmlTypeId});
    	clearFilesFromSession();
	});
</script>