<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<div align="middle">
	<s:form>
		<table>
		<tr>
		<td><h2><s:property value="getText('site.error.message.maintenance')" /></h2></td>
		</tr>
		<tr>
		<td> <s:property value="getText('site.error.message.try')" /> <td>
		</tr>
		</table>
			
		<br></br>	
	<s:submit action="index" value="Back to the Home"/>
	</s:form>
</div>
	