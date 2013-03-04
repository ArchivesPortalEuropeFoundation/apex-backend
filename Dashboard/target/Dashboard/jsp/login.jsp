<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="apenet" uri="http://commons.apenet.eu/tags"%>

<div id ="body" align="center">
	<div id="dashboard-login-advantages">
		<s:text name="user.info.advantages"> </s:text>
	</div>

	<s:form action="login" method="post">
	<br></br>
	<s:actionerror />
	<s:actionmessage/>
		<br></br>
		
		<s:textfield name="username" key="label.email_address"/>
		<s:password name="password" key="label.password" cssClass="small"/>
		<s:checkbox name="dropOtherSession" key="label.login.dropothersession"/>
		<table>
			<br></br>
			<br></br>
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
