<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<div align="center"> 
	<s:div>
		<li>
		<s:text name="user.info.tips.forgetPwd"> </s:text>
		</li>
	</s:div>
	<s:form action="forgetPwd" method="post">
	<br></br>
	<s:actionerror />
	
	<s:if test="#session['forgetIn'] == 'true'"> 
		<!--<s:textfield name="username" required="true" key="label.username" size="25"> </s:textfield>--> 
		<s:textfield readonly="true" name="email" key="label.email" size="50" /> 
		<s:textfield readonly="true" name="secretQuestion" key="label.secretquestion" size="50"> </s:textfield>
		<s:textfield name="secretAnswer" required="true" key="label.secretanswer" size="50"> </s:textfield>
	</s:if>
	
	<s:else>
	<!--<s:fielderror />-->
		<!--<s:textfield name="username" required="true" key="label.username" size="25"> </s:textfield>--> 
		<s:textfield name="email" required="true" key="label.email" size="50" />
	</s:else>

	<table>
		<br></br>
		<tr>
			<td><s:submit method="execute" key="label.forgetPwd" theme="simple"  cssClass="mainButton"/>
			</td>
			<td><s:submit action="index" value="Cancel" onclick="form.onsubmit=null" theme="simple" /></td>
			<td><s:reset value="Reset" theme="simple" /></td>
			</tr>
	</table>	
	</s:form>
</div>	
