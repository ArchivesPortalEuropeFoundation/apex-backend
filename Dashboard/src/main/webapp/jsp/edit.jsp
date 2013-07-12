<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<script type='text/javascript'>
function settings(text)
{	
	if ($("#firstName").val()=="" || $("#lastName").val()=="" || $("#email").val()=="" 
		|| $("#secretQuestion").val()=="" || $("#secretAnswer").val()==""){
		alert(text);
		return;
	}

	if ($("table#passwordTable").is(':visible')) {
		checkSave(text);
		return;	
	}
	$("#changePasswd").hide();
	$('#okButton').hide();
	$('#resetButton').hide();
	$('#MyPass').show();
	$('#passwordTable').hide();
	
}

function checkSave(text) {
	if($("table#passwordTable").is(':visible')){
		if ($("#currentPassword").val()=="" || $("#newPassword").val()=="" || $("#rePassword").val()==""){
			alert(text);
			return;
		}	
	}

	$("#form").submit();
}

function ChangePassword(){
	$("#passwordTable").show();
	$("#changePasswd").hide();
}
</script>
<div align="center" > 

	<s:form id="form" action="edit" method="post" theme="simple" >
	<br></br>
	<s:actionerror />
	<s:actionmessage/>
	<s:if test="%{messageError=='false'}">
		<script type='text/javascript'>
			$(document).ready(function(){
				$("#passwordTable").show();
				$("#changePasswd").hide();
			});
		</script>
	</s:if>
	
	<table class="userTable">
		<thead>
			<tr>
				<td id="passWordChange" colspan="4">
					<h2 align="left"><s:property value="getText('label.edit.user.details')"/></h2>
					<br></br>
				</td>
			</tr>
		</thead>
		<tbody>
			<tr>
				<s:textfield id="firstName" name="firstName" required="true" key="label.firstname" size="25" theme="xhtml"/>
				<s:textfield id ="lastName" name="lastName" required="true" key="label.lastname" size="25" theme="xhtml"/>
				<s:textfield id ="email" name="email" required="true" key="label.email_address" size="50" theme="xhtml">   </s:textfield>
				<s:textfield id ="secretQuestion" name="secretQuestion" required="true" key="label.secretquestion" size="50" theme="xhtml"> </s:textfield>
				<s:textfield id ="secretAnswer" name="secretAnswer" required="true" key="label.secretanswer" size="50" theme="xhtml"> </s:textfield>
			</tr>
		</tbody>
	</table>
	<table id="passwordTable" style="display:none" class="userTable">
		<thead>
			<tr>
				<td id="passWordChange" colspan="4">
					<h2 align="left"><s:property value="getText('label.edit.pwd.change')"/></h2>
					<br></br>
				</td>
			</tr>
		</thead>
		<tbody>
		    <tr>
				<s:password id="currentPassword" name="currentPassword" required="true" key="label.currentpassword" size="25" theme="xhtml"/>
				<s:password id="newPassword" name="newPassword" required="true" key="label.newPassword" size="25" theme="xhtml" />
				<s:password id="rePassword" name="rePassword" required="true" key="label.verifypassword" size="25" theme="xhtml"/>
			</tr>
		</tbody>
	</table>
		
	<table>

		<tr>
		     <td>
		     	<br></br>
		     	<input type="button" id="changePasswd" value="<s:property value="getText('label.change.passWord')"/>" onclick="ChangePassword();"/>
			 </td>
			 <td>
				  <br></br>
				  <input type="button" id="okButton" value="<s:property value="getText('label.save')"/>"  onclick="settings('<s:property value="getText('label.fillData')" />')"/>
				  <div id="MyPass" style="display:none;" >
					<label for="confirmPassword"><s:property value='getText("label.messageConfirmPassword")' /></label>
					<p></p>
					<input type="password" id="confirmPassword" name="confirmPassword" required="true" size="25" theme="simple"/>
					<p></p>
					<input type="submit" value="<s:property value="getText('label.verifypassword')" />" class="checkPass" />				
				  </div>
			 </td>
			 <td>
				  <br></br>
				  <input type="reset" id="resetButton" value="<s:property value="getText('label.reset')" />"  />
			 </td>
			</tr>
	</table>
		
	</s:form>

</div>