<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<div style="text-align:left;">
    <p>----- <s:property value="getText('ead.report.header')" /> -----</p>
    <p><s:property value="getText('ead.report.missing.unittitle')" /> (unittitle): <s:property escapeHtml="false" value="%{counterUnittitle}"/></p>
    <p><s:property value="getText('ead.report.missing.unitdate')" /> (unitdate@normal): <s:property escapeHtml="false" value="%{counterUnitdate}"/></p>
    <p><s:property value="getText('ead.report.missing.dao')" /> (dao@xlink:role): <s:property escapeHtml="false" value="%{counterDao}"/></p>
    <p><s:property value="getText('ead.report.missing.href')" /> (dao@xlink:href): <s:property escapeHtml="false" value="%{counterWrongHref}"/></p>
</div>