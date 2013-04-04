<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="apenet" uri="http://commons.apenet.eu/tags"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@page import="java.util.Locale"%>


<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="apenet" uri="http://commons.apenet.eu/tags"%>

<div align="center">  
	
	<s:form action="contact" method="post">
		<br></br>
		<!--<s:fielderror />-->
				
		<s:textfield name="email" required="true" key="label.email.contact" size="50" />
		
		&nbsp;
		

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
				<td><s:submit action="index" value="Cancel"
					onclick="form.onsubmit=null" theme="simple" /></td>
				<td><s:reset value="Reset" theme="simple" /></td>
			</tr>
		</table>
	</s:form>	
</div>

