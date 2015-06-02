<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="apenet" uri="http://commons.apenet.eu/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div id ="body" align="center">
	<div id="dashboard-login-advantages">
		<s:text name="user.info.advantages"> </s:text>
	</div>
	<c:if test="${maintenanceMode}">
		<h1 id="maintenanceMode"><s:text name="user.maintenancemode"/></h1>

	</c:if>
	<c:if test="${queueClosed}">
		<h2 id="queueClosedMode"><s:text name="user.queueclosed"/></h2>
	</c:if>
	<s:form id="login" action="login" method="post"  focusElement="username">
	<br></br>
	<s:actionerror />
	<s:actionmessage/>
		<br></br>
		
		<s:textfield name="username" key="label.email_address" id="username" />
		<s:password name="password" key="label.password"/>
		<s:checkbox name="dropOtherSession" key="label.login.dropothersession"/>
					<br/>
			<br/>
		<table>

			<tr>
				<td><s:submit method="execute" key="label.login" tooltip="Login execution" theme="simple" cssClass="mainButton"/> </td>
				<td><s:submit action="index" key="label.cancel" onclick="form.onsubmit=null" theme="simple" /></td>
				<td><s:reset key="label.reset" theme="simple" /></td>
			</tr>
		</table>
		
	</s:form>
		
	<div id="dashboard-login-forgetpwd">
			<s:text name="user.forgetPwd"> </s:text>
			<s:a action="forgetPwd"> <s:text name="user.here"> </s:text> </s:a>
	</div>
</div>
