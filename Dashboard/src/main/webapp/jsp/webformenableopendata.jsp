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
        $("#submit").click(function () {
            return confirm("Are you sure!!!");
        });

    });
</script>
<div align="center">
    <p><span style="font-weight: bold;font-size:large;"><s:property value="getText('dashboard.menu.topic.enableapi')" /></span><br/><br/></p>

    <c:if test="${enableOpenData eq false}">
        <s:form method="POST" theme="simple">
            <s:hidden name="ai_id" value="%{ai_id}"></s:hidden>
                <table>
                    <tr>
                        <td colspan="15"><s:property value="getText('dashboard.menu.topic.enableapi')" />: 
                        <s:checkbox name="enableOpenData" id="enableOpenData" fieldValue="true"></s:checkbox>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="2" style="float: right">
                        <s:submit id="submit" key="label.ok" cssClass="mainButton"/>
                        <s:submit key="label.cancel" action="dashboardHome"/>
                    </td>
                </tr>
            </table>
        </s:form>
    </c:if>
    <c:if test="${enableOpenData eq true}">
        You already enabled open data 
    </c:if>
    <s:property value="enableOpenData"/>
    
    <s:property value="aiName"/>:<s:property value="aiId"/>


</div>
