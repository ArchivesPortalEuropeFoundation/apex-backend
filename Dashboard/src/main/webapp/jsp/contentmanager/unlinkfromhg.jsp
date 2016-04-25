<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="ape" uri="http://commons.archivesportaleurope.eu/tags"%>
<script type='text/javascript'>
    $(document).ready(function () {
        initPage();
    });

</script>

<s:form method="POST">
    <s:text name="content.message.unlinkfromhg.choosehg"/>
    <s:checkboxlist name="hgList" list="linkedUnpublishedHgs" />
    <s:submit action="unlinkFromHg" key="label.ok" cssClass="mainButton" />
    <s:submit action="contentmanager" key="label.cancel" />
    <s:hidden id ="batchItems" name="batchItems" />
    <s:hidden name="id" />
</s:form>