<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<div align="middle"> 
	<s:form action="changeForgetPwd" method="post">
	<br></br>
	<s:actionerror />

		<s:password name="password" required="true" key="label.password" size="50"> </s:password>
		<s:password name="repassword" required="true" key="label.repassword" size="50"> </s:password>
	
		<s:hidden name="validation_link" value="%{validation_link}"> </s:hidden>
	
		<table>
		<br></br>
		<tr>
			<td><s:submit method="execute" key="label.ChangeForgetPwd" theme="simple" />
			</td>
			<td><s:submit action="index" value="Cancel" onclick="form.onsubmit=null" theme="simple" /></td>
			<td><s:reset value="Reset" theme="simple" /></td>
			</tr>
		</table>
			
	</s:form>
</div>	
