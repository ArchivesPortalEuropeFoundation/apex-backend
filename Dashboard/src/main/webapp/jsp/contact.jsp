<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="apenet" uri="http://commons.apenet.eu/tags"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@page import="java.util.Locale"%>


<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="apenet" uri="http://commons.apenet.eu/tags"%>
    
<div align="center">  
	<s:form id="contact" method="POST"> <!-- action="/Dashboard/contact.action" name="form" -->
		<br></br>				
		<s:textfield name="email" required="true" key="label.email.contact" size="50" />	
		&nbsp;
		 <%-- WHILE ITERATOR DOESN'T WORKS, THIS IS A TEMPORALLY WAY TO MAKE THE CODE RUNS
		<s:select id="Mail_selected" name="subjectsMenu" list="mails" listKey="key" listValue="value" ></s:select>

		<select id="Mail_selected" name="subjectsMenu">
			<s:iterator value="mails" var="mail">
			 	<option value="<s:property value='#mail.key'/>"> <s:property value="#mail.value"/> </option>
			</s:iterator>		
		</select> -
		--%>	
		<select id="Mail_selected" name="subjectsMenu">
		<option value="bastiaan.verhoef.ape@gmail.com;yoann.moranville@gmail.com;beatriz.gonzalezvi@mecd.es"><s:property value="getText('xmlMail.other')" /></option>
		<option value="kerstin.arnold.ape@gmail.com"><s:property value="getText('xmlMail.europeana')" /></option> 
		<option value="lucile.grand.ape@gmail.com"><s:property value="getText('xmlMail.procesing')" /></option> 
		<option value="bastiaan.verhoef.ape@gmail.com;yoann.moranville@gmail.com"><s:property value="getText('xmlMail.troubles')" /></option> 
		<option value="kerstin.arnold.ape@gmail.com;chris.houwing.ape@gmail.com"><s:property value="getText('xmlMail.suggestions')" /></option>
		<option value="beatriz.gonzalezvi@mecd.es"><s:property value="getText('xmlMail.data')" /></option> 
		</select>
			
		&nbsp;			
		<s:div>
			<br></br>
		</s:div>	
		<s:textarea name="feedbackText" key="label.feedback.comments" required="true" labelposition="left" cssStyle="width:500px; height:180px;vertical-align:middle" />
		<s:actionerror />
		<table>
			<tr>
				<td><s:submit method="execute" key="label.feedback.send" theme="simple"  cssClass="mainButton" />
				</td>
				<!--depending the item selected in the dropdown list, the subject and recipients will be sent-->
				<td><s:submit action="index" value="Cancel" onclick="form.onsubmit=null" theme="simple" /></td>
				<td><s:reset value="Reset" theme="simple" /></td>
			</tr>
		</table>
	</s:form>	
</div>

