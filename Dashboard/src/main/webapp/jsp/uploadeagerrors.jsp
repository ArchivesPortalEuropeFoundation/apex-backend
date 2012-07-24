<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
        <div id="fileList">
        <s:property value="getText('label.eag.validationerror')" /><br /><br /><br />
		<s:actionmessage /><br />
		<!-- 
        - <s:property value="getText('label.eag.uploadingerror.one')" /><br><br>
        - <s:property value="getText('label.eag.uploadingerror.two')" /> <br>
		- <s:property value="getText('label.eag.uploadingerror.three')" /> <br>
		 -->
            <s:if test="warnings_eag != null">
                <br /><br />
                <b><s:property value="getText('label.eag.validationerror')" /></b>
                <br />
                <s:iterator value="warnings_eag" status="warn_id">
                    <div class="warnId" id="warnId<s:property value="#warn_id.index"/>">
                        <s:property escapeHtml="false" />
                    </div>
                </s:iterator>
            </s:if>
        </div>