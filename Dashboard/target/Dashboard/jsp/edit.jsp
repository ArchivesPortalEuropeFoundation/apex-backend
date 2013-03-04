<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<div align="center" > 

	<s:form action="edit" method="post" theme="simple">
	<br></br>
	<s:actionerror />
	<s:actionmessage/>
	<s:fielderror />
	
	<h2 align="left"><s:property value="getText('label.edit.user.details')"/></h2>
	<table>
	
	<tr>
	
	</tr>
	</table>
	<br/>
	
	<table>
		<s:textfield name="firstName" required="true" key="label.firstname" size="25" theme="xhtml"/>
		<s:textfield name="lastName" required="true" key="label.lastname" size="25" theme="xhtml"/>
		<s:textfield name="email" required="true" key="label.email_address" size="50" theme="xhtml">   </s:textfield>
		<s:textfield name="secretQuestion" required="true" key="label.secretquestion" size="50" theme="xhtml"> </s:textfield>
		<s:textfield name="secretAnswer" required="true" key="label.secretanswer" size="50" theme="xhtml"> </s:textfield>

	</table>

	<br/>
	<br/>
	<br/>
	<br/>

	<h2 align="left"><s:property value="getText('label.edit.pwd.change')"/></h2>
	<table>
	<tr>
	</tr>
	<br/>
	<br/>
	
	</table>
	
	<br/>
		
	<table>
	<s:password name="currentPassword" required="true" key="label.currentpassword" size="25" theme="xhtml"/>
	<s:password name="newPassword" required="true" key="label.newPassword" size="25" theme="xhtml" />
	<s:password name="rePassword" required="true" key="label.verifypassword" size="25" theme="xhtml"/>
	</table>
		
	<table>
		<br></br>
		<tr>
			<td><s:submit method="execute" key="label.save" theme="simple"  cssClass="mainButton"/>
			</td>
			<!-- <td><s:submit action="showHome" value="Cancel" onclick="form.onsubmit=null" theme="simple" /></td> -->
			<td><s:reset value="Reset" theme="simple" /></td>
			</tr>
	</table>
		
	</s:form>

</div>