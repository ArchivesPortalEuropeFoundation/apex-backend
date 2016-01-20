<%-- 
    Document   : webformenableopendata
    Created on : Jan 13, 2016, 9:57:16 AM
    Author     : kaisar
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<script type="text/javascript">
    $(document).ready(function () {
        $('#submit').prop('disabled', true);
        $('#enableOpenData').click(function () {
            if ($('#enableOpenData').attr('checked') === 'checked') {
                $('#submit').prop('disabled', false);
            }
        });
        $("#submit").click(function () {
            if ($('#enableOpenData').attr('checked') === 'checked') {
                var doIt = confirm("<s:property value="getText('content.message.opendata.comfirm')" />");
                if (doIt) {
                    return true;
                }
                else {
                    return false;
                }
            }
            return false;
        });
    });
</script>
<div align="center">
    <p><span style="font-weight: bold;font-size:large;"><s:property value="getText('dashboard.menu.topic.enableapi')" /></span><br/><br/></p>

    <s:if test="hasActionErrors()">
        <div class="errors">
            <s:actionerror/>
        </div>
    </s:if>
    <c:if test="${enableOpenData eq true}">
        <s:property value="getText('label.ai.enableopendata.enabled')" />
    </c:if>
    <c:if test="${enableOpenData eq false}">
        <s:property value="getText('label.ai.enableopendata.disabled')" />
    </c:if>
    <s:form method="POST" action="doEnableOpenData" theme="simple">
        <s:hidden name="ai_id" value="%{ai_id}"></s:hidden>
            <table>
                <tr>
                    <td colspan="15">
                    <c:if test="${enableOpenData eq true}">
                        <s:property value="getText('label.ai.enableopendata.disable')" />: 
                        <s:checkbox name="checkBoxValue" id="enableOpenData" fieldValue="true"></s:checkbox>
                    </c:if>
                    <c:if test="${enableOpenData eq false}">
                        <s:property value="getText('label.ai.enableopendata.enable')" />: 
                        <s:checkbox name="checkBoxValue" id="enableOpenData" fieldValue="true"></s:checkbox>
                    </c:if>
                </td>
            </tr>
            <tr>
                <td colspan="2" style="float: right">
                    <br>
                <s:submit id="submit" key="label.submit" cssClass="mainButton" cssStyle="width:75px; height:25px"/>
                </td>
            </tr>
        </table>
    </s:form>
</div>
