<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<div style="text-align:left;">
    <p>----- <s:property value="getText('edm.report.header')" /> -----<br/><s:property value="getText('edm.report.note')" /></p>
    <p><s:property value="getText('edm.report.identifier.missing')" />: <s:property escapeHtml="false" value="%{noUnitidNumber}"/></p>
    <p><s:property value="getText('edm.report.identifier.duplicated.number')" />: <s:property escapeHtml="false" value="%{duplicateUnitidsNumber}"/></p>
    <p><s:property value="getText('edm.report.identifier.duplicated.values')" />: <s:property escapeHtml="false" value="%{duplicateUnitids}"/></p>
</div>