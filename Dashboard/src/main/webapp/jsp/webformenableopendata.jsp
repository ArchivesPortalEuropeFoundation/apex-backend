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
        var checked = false;
        $('#submit').hide();
        $('#enableOpenData').click(function () {
            if ($('#enableOpenData').attr('checked') === 'checked') {
                checked = true;
            }
            else {
                checked = false;
            }
            if (!(checked ^${enableOpenData})) {
                $('#submit').hide();
            }
            else {
                $('#submit').show();
            }
        });
        $("#submit").click(function () {
            if ($('#enableOpenData').attr('checked') === 'checked')
                checked = true;
            var doIt = confirm("Are you sure!!!");
            if (doIt && (checked ^${enableOpenData})) {
                $('#submit').hide();
                $('#cancel').hide();
                $('#enableOpenData').hide();
                window.location.href = '/Dashboard/enableOpenData.action';
                return true;
            }
            else if (doIt === false || (doIt === true && (checked ^${enableOpenData} === false))) {
                location.reload();
                return false;
            }
            else {
                window.location.href = '/Dashboard/dashboardHome.action';
                return false;
            }
            return doIt;
        });
    });
</script>
<div align="center">
    <p><span style="font-weight: bold;font-size:large;"><s:property value="getText('dashboard.menu.topic.enableapi')" /></span><br/><br/></p>

    <c:if test="${enableOpenData eq true}">
        <s:property value="getText('label.ai.enableopendata.enabled')" />
    </c:if>
    <s:form method="POST" action="doEnableOpenData" theme="simple">
        <s:hidden name="ai_id" value="%{ai_id}"></s:hidden>
            <table>
                <tr>
                    <td colspan="15"><s:property value="getText('dashboard.menu.topic.enableapi')" />: 
                    <c:if test="${enableOpenData eq true}">
                        <s:checkbox name="enableOpenData" id="enableOpenData" fieldValue="true" value="true"></s:checkbox>
                    </c:if>
                    <c:if test="${enableOpenData eq false}">
                        <s:checkbox name="enableOpenData" id="enableOpenData" fieldValue="true"></s:checkbox>
                    </c:if>
                </td>
            </tr>
            <tr>
                <td colspan="2" style="float: right">
                    <s:submit id="submit" key="label.ok" cssClass="mainButton"/>
                    <s:submit id="cancel" key="label.cancel" action="dashboardHome" name="Cancel" onclick="form.onsubmit=null"/>
                </td>
            </tr>
        </table>
    </s:form>
</div>
