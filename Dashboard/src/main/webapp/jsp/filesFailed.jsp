<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div > 	
			<br></br>
			<div id="" style="width:650px;" >				
					<table border="1" cellpadding="1500">
					<thead align="center">
					<tr style="background:#ECE9EA; font-weight: bold; ">						
						<td><s:property value ="getText('label.eadid')"/></td>
						<td> <s:property value ="getText('content.message.queuePosition')"/></td>
						<td><s:property value ="getText('dashboard.errorpage.title')"/></td>
					</tr>
					</thead>
					<tbody>						
						<s:iterator value="filesFailed" var="row" status="stat">
							<tr>
								<td><s:property value="#row.findingAid.eadid" /><s:property value="#row.holdingsGuide.eadid" /></td>
								<td><s:property value="#row.position"/></td>	
								<td><s:property value="#row.errors"/></td>								
								<!-- <td><s:property value="#row.errors.substring(0, 90)"/> <a style="cursor:pointer;" name="" onclick="var message = '<s:property value="#row.errors" />'; alert(message);" >... -->								
								</a></td>
							</tr>	
						</s:iterator>
					</tbody>
					</table>
			</div>

	<br></br>
	<br></br> 	       
</div>
