<%-- 
    Document   : webformenableopendata
    Created on : Jan 13, 2016, 9:57:16 AM
    Author     : kaisar
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<style>
    .enableCheckbox{
        width: 13px;height: 15px;padding: 0;margin:0;vertical-align: middle;position: relative;top: -1px;*overflow: hidden;
    }
    .disabledButton {
        color: gray !important;
        cursor: default !important;
    }
</style>
<script type="text/javascript">
    $(document).ready(function () {
        $('#enableOpenData').click(function () {
            if ($('#enableOpenData').attr('checked') === 'checked') {
                $('#submit').prop('disabled', false);
                $('#submit').addClass('mainButton');
                $('#submit').removeClass('disabledButton');
            } else {
                $('#submit').removeClass('mainButton');
                $('#submit').addClass('disabledButton');
            }
        });
        $("#submit").click(function () {
            if ($('#enableOpenData').attr('checked') === 'checked') {
                var doIt = confirm("<s:property value="getText('content.message.opendata.comfirm')" />");
                if (doIt) {
                    return true;
                }
                else {
                    location.reload();
                    return false;
                }
            }
            return false;
        });
    });
</script>
<div>
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
    <br><br>
    <s:if test="!hasActionErrors()">
        <s:form method="POST" action="doEnableOpenData" theme="simple">
            <s:hidden name="ai_id" value="%{ai_id}"></s:hidden>
                <table>
                    <tr>
                        <td colspan="15">
                        <c:if test="${enableOpenData eq true}">
                            <div>
                                <span style="display: block; padding-left: 15px;text-indent: -15px;">
                                    <s:checkbox name="checkBoxValue" id="enableOpenData" fieldValue="true" cssClass="enableCheckbox"></s:checkbox>
                                    <span><s:property value="getText('label.ai.enableopendata.disable')" /></span>
                                </span>
                            </div>



                        </c:if>
                        <c:if test="${enableOpenData eq false}"> 
                            <div>
                                <span style="display: block; padding-left: 15px;text-indent: -15px;">
                                    <s:checkbox name="checkBoxValue" id="enableOpenData" fieldValue="true" cssClass="enableCheckbox"></s:checkbox>
                                    <span><s:property value="getText('label.ai.enableopendata.enable')" /></span>
                                </span>
                            </div>
                        </c:if>
                    </td>
                </tr>
                <tr>
                    <td colspan="2" style="float: right">
                        <br>
                    <s:submit id="submit" key="label.submit" cssClass="disabledButton" cssStyle="width:75px; height:25px" disabled="true"/>
                    </td>
                </tr>
            </table>
        </s:form>
    </s:if>

</div>
