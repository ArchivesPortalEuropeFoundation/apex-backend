<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="apenet" uri="http://commons.apenet.eu/tags"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div >
	<s:form action="countryState" method="POST" >
		<s:actionmessage />
  			<br></br>
			<div id="country_list" style="height:10%;">
				 <s:select  id="countrySelected" name="countrySelected" list="countriesList" listKey="id" 
	  			listValue="cname" headerKey = "-1" headerValue="%{getText('admin.menu.countryselection')}" theme="simple" >
				</s:select> 
				
				<s:submit id="ok" key="admin.menu.label.ok" theme="simple"/>
				&nbsp;  &nbsp; 
				<br></br>
			</div>
			<div id="countrystate" style="width:350px;" >
				<s:if test="aiNodes.size()>0">
					<table border="1" cellpadding="1500">
					<thead align="center">
					<tr style="background:#ECE9EA; font-weight: bold; ">
						<td ><s:property value="getText('admin.menu.institution')"/></td>
						<td><s:property value="getText('admin.menu.EAG')"/></td>
						<td><s:property value="getText('admin.menu.FA')"/></td>
						<td><s:property value="getText('admin.menu.HG')"/></td>
						<td><s:property value="getText('admin.menu.SG')"/></td>
					</tr>
					</thead>
					<tbody>						
						<s:iterator value="aiNodes" var="row" status="stat">
							<tr>
								<td><s:property value="#row.ai.ainame" /></td>
								<td>
									<s:if test="#row.ai.eagPath!=null">
										<span class="eagLink" id="eagLink_${row.ai.aiId}"><s:property value="getText('admin.menu.label.EAGuploaded')"/></span>
									</s:if>
									<s:else>
										<s:property value="getText('admin.menu.label.EAGnotuploaded')"/>
									</s:else>					
								</td>
								<td>
									<s:property value="#row.numFindingAids" />							
								</td>
								<td>
									<s:property value="#row.numHoldingGuides" />										
								</td>
								<td>
									<s:property value="#row.numSourceGuides" />
								</td>
							</tr>	
						</s:iterator>
					</tbody>
					</table>
				</s:if>
				<s:elseif test="countrySelected!=null"> 
					<s:property value="getText('a.message.noaldefined')" />					
				</s:elseif>
			</div>
		</s:form>	
		<br></br>
		<br></br>	       
</div>
<script type="text/javascript">
    $(document).ready(function() {
        checkEagLink();
    });
</script>