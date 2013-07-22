<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>


<div align="center"> 
	<s:form action="view" method="post">
	<br></br>
	<s:actionerror />
	<!--<s:fielderror />  namespace="/" -->
	<br></br>
		<s:textfield readonly="true" name="firstName" required="true" key="label.firstname" size="25" />
		<s:textfield readonly="true" name="lastName" required="true" key="label.lastname" size="25" />
		<s:textfield readonly="true" name="email_address" key="label.email_address"> </s:textfield>
		<s:textfield readonly="true" name="secretQuestion" key="label.secretquestion" size="50"> </s:textfield>
		<s:textfield readonly="true" name="secretAnswer" required="true" key="label.secretanswer" size="50"> </s:textfield>
		
		<table>
		<br></br>
		<tr>
			<td><s:submit action="edit" method="execute" key="label.edit" theme="simple"  cssClass="mainButton" />
			</td>
			<td><s:submit action="showHome" value="Cancel" onclick="form.onsubmit=null" theme="simple" /></td>
			</tr>
		</table>
		
	</s:form>
</div>	

