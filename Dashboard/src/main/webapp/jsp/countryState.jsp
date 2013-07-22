<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="apenet" uri="http://commons.apenet.eu/tags"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div >
	<s:form action="countryState" method="POST" theme="simple">
		<s:actionmessage />
			<div id="country_list">
				 <s:select  id="countrySelected" name="countrySelected" list="countriesList" listKey="id" 
	  			listValue="cname" headerKey = "-1" headerValue="%{getText('admin.menu.countryselection')}" theme="simple" >
				</s:select> 
				
				<s:submit id="ok" key="admin.menu.label.ok" theme="simple"/>
			</div>
			<div>
				<s:if test="aiNodes.size()>0">
					<table class="defaultlayout">
					<tr>
						<th ><s:property value="getText('admin.menu.institution')"/></th>
						<th><s:property value="getText('admin.menu.EAG')"/></th>
						<th><s:property value="getText('admin.menu.FA')"/></th>
						<th><s:property value="getText('admin.menu.HG')"/></th>
						<th><s:property value="getText('admin.menu.SG')"/></th>
					</tr>
				
						<s:iterator value="aiNodes" var="row" status="stat">
							<tr>
								<td><s:property value="#row.ai.ainame" /></td>
								<td>
									<s:if test="#row.ai.eagPath!=null">
										<s:property value="getText('admin.menu.label.EAGuploaded')"/>
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
					</table>
				</s:if>
				<s:elseif test="countrySelected!=null"> 
					<s:property value="getText('a.message.noaldefined')" />					
				</s:elseif>
			</div>
		</s:form>	
     
</div>
