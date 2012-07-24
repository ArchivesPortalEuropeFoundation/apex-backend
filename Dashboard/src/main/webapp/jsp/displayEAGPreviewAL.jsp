<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%> 
<link href="${pageContext.request.contextPath}/css/cssnew.css" rel="stylesheet" type="text/css" />

<s:if test="pathEAG!=null&&pathEAG!=''">
        <p>&nbsp;</p>
		<div class="apnetTitle"><h2><s:property value="name"></s:property></h2></div>
        <p>&nbsp;</p>
         <hr/> 
 		<p>&nbsp;</p> 	
	
		<div class="apnetsubTitle"><h2><s:property value="getText('previewal.text.contact')"/></h2></div>
		<p>&nbsp;</p> 	
		<div class="tableDirectory"><table width="100%">		
		<tr height="40px">
		<th></th>			
			<td class="tdleft"><span class="apnetTagTitle"><br/><s:property value="getText('previewal.text.address')"/></span></td>
			<td class="tdright"><span class="apnetTextContent"><br/><s:property value="street"></s:property>&nbsp; - &nbsp;<s:property value="postalCode"></s:property>&nbsp; - &nbsp;<s:property value="cityTown"></s:property>&nbsp; - &nbsp;<s:property value="localentity"></s:property> </span></td>
		</tr>		
		<s:if test="firstdem!=''">
			<tr height="40px">
				<th></th>
				<td class="tdleft"><span class="apnetTagTitle"><br/><s:property value="getText('previewal.text.region')"/></span></td>
				<td class="tdright"><span class="apnetTextContent"><br/><s:property value="firstdem"></s:property>&nbsp; - &nbsp;<s:property value="secondem"></s:property></span></td>
			</tr>	
		</s:if>
		<tr height="40px;">
		<th></th>
			<td class="tdleft"><span class="apnetTagTitle"><br/><s:property value="getText('previewal.text.country')"/></span></td>
			<td class="tdright"><span class="apnetTextContent"><br/><s:property value="country"></s:property></span></td>
		</tr>
		
		<tr height="40px;">
		<th></th>
			<td class="tdleft"><span class="apnetTagTitle"><br/><s:property value="getText('previewal.text.tel')"/></span></td>
			<td class="tdright"><span class="apnetTextContent"><br/><s:property value="telephone"></s:property></span></td>
		</tr>
		<s:if test="fax!=''">
		<tr height="40px;">
		<th></th>
			<td class="tdleft"><span class="apnetTagTitle"><br/><s:property value="getText('previewal.text.fax')"/></span></td>
			<td class="tdright"><span class="apnetTextContent"><br/><s:property value="fax"></s:property></span></td>
		</tr>
		</s:if>		
		<tr height="40px;">
		<th></th>
			<td class="tdleft" ><span class="apnetTagTitle"><br/><s:property value="getText('previewal.text.email')"/></span></td>
			<td class="tdright"><span class="apnetTextContent"><br/><s:property value="emailAddress"></s:property></span></td>
		</tr>
		<tr height="40px;">
		<th></th>
			<td class="tdleft"><span class="apnetTagTitle"><br/><s:property value="getText('previewal.text.webpage')"/></span></td>
			<td class="tdright"><span class="apnetTextContent"><br/><a href="<s:property value="webPage"/>" class="textslink" target="_blank" ><s:property value="webPage" /></a></span></td>
		</tr>
		<tr height="40px;">
		<th></th>
			<td class="tdleft"><span class="apnetTagTitle"><br/><s:property value="getText('previewal.text.parallelformname')"/></span></td>
			<td class="tdright"><span class="apnetTextContent"><br/><s:property value="englishName"></s:property></span></td>
		</tr>		
		</table>		
		</div>
		
		<p>&nbsp;</p>
		<div class="apnetsubTitle"><h2><s:property value="getText('previewal.text.accessinfo')"/></h2></div>		
		<p>&nbsp;</p> 	
		<div class="tableDirectory">
		<table width="100%">
			<s:if test="opening!=''">		
				<tr height="40px;">
				<th></th>
					<td class="tdleft"><span class="apnetTagTitle"><br/><s:property value="getText('previewal.text.open')"/></span></td>
					<td class="tdright"><span class="apnetTextContent"><br/><s:property value="opening"></s:property> &nbsp; <s:property value="openingNum"></s:property></span></td>
				</tr>
			</s:if>
			<s:if test="closing!=''">
				<tr height="40px;">
				<th></th>
					<td class="tdleft"><span class="apnetTagTitle"><br/><s:property value="getText('previewal.text.closed')"/></span></td>
					<td class="tdright"><span class="apnetTextContent"><br/><s:property value="closing"></s:property></span></td>
				</tr>	
			</s:if>
			<s:if test="access=='yes'">
				<tr height="40px;">
				<th></th>
					<td class="tdleft"><span class="apnetTagTitle"><br/><s:property value="getText('previewal.text.accessanduse')"/></span></td>
					<td class="tdright"><span class="apnetTextContent"><br/><s:property value="getText('previewal.text.accesspublic')"/></span></td>
				</tr>	
			</s:if>
			<s:elseif test="access=='no'">
				<tr height="40px;">
				<th></th>
					<td class="tdleft"><span class="apnetTagTitle"><br/><s:property value="getText('previewal.text.accessanduse')"/></span></td>
					<td class="tdright"><span class="apnetTextContent"><br/><s:property value="getText('previewal.text.accesspermission')"/></span></td>
					<s:if test="restaccess!=''">
						<td class="tdright""><span class="apnetTextContent"><s:property value="restaccess"></s:property></span></td>
					</s:if>
				</tr>
			</s:elseif>
			<s:if test="accesibility=='yes'">
				<tr height="40px;">
				<th></th>
					<td class="tdleft"><span class="apnetTagTitle"><br/><s:property value="getText('previewal.text.access')"/></span></td>
					<td class="tdright"><span class="apnetTextContent"><br/><s:property value="getText('previewal.text.facilities')"/></span></td>
				</tr>	
			</s:if>
			<s:elseif test="access=='no'">
				<tr height="40px;">
				<th></th>
					<td class="tdleft"><span class="apnetTagTitle"><br/><s:property value="getText('previewal.text.access')"/></span></td>
					<td class="tdright"><span class="apnetTextContent"><br/><s:property value="getText('previewal.text.nofacilities')"/></span></td>
				</tr>
			</s:elseif>
		</table>
		
		<p>&nbsp;</p>
		<div class="apnetsubTitle"><h2><s:property value="getText('previewal.text.service')"/></h2></div>		
		<p>&nbsp;</p> 	
		<div class="tableDirectory">
		<table width="100%">
			<s:if test="readingRoom!=''">
				<tr height="40px;">
				<th></th>
					<td class="tdleft"><span class="apnetTagTitle"><br/><s:property value="getText('previewal.text.readingroom')"/></span></td>
					<td class="tdright"><span class="apnetTextContent"><br/><s:property value="readingRoom"></s:property></span></td>
				</tr>	
			</s:if>
			<s:if test="repositoryguideLink!=''">
				<tr height="40px;">
				<th></th>
					<td class="tdleft"><span class="apnetTagTitle"><br/><s:property value="getText('previewal.text.hg')"/></span></td>
					<td class="tdright"><span class="apnetTextContent"><br/><a href="<s:property value="repositoryguideLink"/>" class="textslink" target="_blank" >
					<s:if test="repositoryguide!=''">
						<s:property value="repositoryguide" />
					</s:if>
					<s:else>
						<s:property value="repositoryguideLink" />
					</s:else>						
					</a></span></td>
				</tr>	
			</s:if>
			<s:if test="library=='yes'">
				<tr height="40px;">
				<th></th>
					<td class="tdleft"><span class="apnetTagTitle"><br/><s:property value="getText('previewal.text.libraryassets')"/></span></td>
					<td class="tdright"><span class="apnetTextContent"><br/><s:property value="getText('previewal.text.library')"/></span></td>
				</tr>	
			</s:if>
			<s:elseif test="library=='no'">
			<tr height="40px;">
				<th></th>
					<td class="tdleft"><span class="apnetTagTitle"><br/><s:property value="getText('previewal.text.libraryassets')"/></span></td>
					<td class="tdright"><span class="apnetTextContent"><br/><s:property value="getText('previewal.text.nolibrary')"/></span></td>
				</tr>	
			</s:elseif>
			<s:if test="restorationlab=='yes'">
				<tr height="40px;">
				<th></th>
					<td class="tdleft"><span class="apnetTagTitle"><br/><s:property value="getText('previewal.text.restoration')"/></span></td>
					<td class="tdright"><span class="apnetTextContent"><br/><s:property value="getText('previewal.text.available')"/></span></td>
				</tr>	
			</s:if>
			<s:elseif test="restorationlab=='no'">
			<tr height="40px;">
				<th></th>
					<td class="tdleft"><span class="apnetTagTitle"><br/><s:property value="getText('previewal.text.restoration')"/></span></td>
					<td class="tdright"><span class="apnetTextContent"><br/><s:property value="getText('previewal.text.notavailable')"/></span></td>
				</tr>	
			</s:elseif>
			<s:if test="reproductionser=='yes'">
				<tr height="40px;">
				<th></th>
					<td class="tdleft"><span class="apnetTagTitle"><br/><s:property value="getText('previewal.text.repro')"/></span></td>					
					<td class="tdright"><span class="apnetTextContent"><br/><s:property value="getText('previewal.text.available')"/><br/><br/>
					<s:if test="photocopyser=='yes'">
						 &nbsp;&nbsp;&nbsp; <s:property value="getText('previewal.text.photocopies')"/>	<br/>					
					</s:if>
					<s:if test="microformser=='yes'">
						 &nbsp;&nbsp;&nbsp; <s:property value="getText('previewal.text.microforms')"/> <br/>					
					</s:if>
					<s:if test="photographser=='yes'">
						 &nbsp;&nbsp;&nbsp; <s:property value="getText('previewal.text.photographies')"/>	<br/>				
					</s:if>
					<s:if test="digitalser=='yes'">
						&nbsp;&nbsp;&nbsp; <s:property value="getText('previewal.text.digitalrepros')"/> <br/>					
					</s:if>					
					</span></td>
				</tr>	
			</s:if>
			<s:elseif test="reproductionser=='no'">
			<tr height="40px;">
				<th></th>
					<td class="tdleft"><span class="apnetTagTitle"><br/><s:property value="getText('previewal.text.repro')"/></span></td>
					<td class="tdright"><span class="apnetTextContent"><br/><s:property value="getText('previewal.text.notavailable')"/></span></td>
				</tr>	
			</s:elseif>
			<s:if test="automation=='yes'">
				<tr height="40px;">
				<th></th>
					<td class="tdleft"><span class="apnetTagTitle"><br/><s:property value="getText('previewal.text.information')"/></span></td>					
					<td class="tdright"><span class="apnetTextContent"><br/><s:property value="getText('previewal.text.available')"/> <br/><br/>
					<s:if test="autusermanag=='yes'">
						 &nbsp;&nbsp;&nbsp; <s:property value="getText('previewal.text.useradmin')"/>	<br/>					
					</s:if>
					<s:if test="autdescription=='yes'">
						 &nbsp;&nbsp;&nbsp; <s:property value="getText('previewal.text.descriptions')"/> <br/>			
					</s:if>
					<s:if test="indexvoc=='yes'">
						  &nbsp;&nbsp;&nbsp; <s:property value="getText('previewal.text.indices')"/> <br/>				
					</s:if>
					<s:if test="odautomationP!=''">
						&nbsp;&nbsp;&nbsp;<s:property value="odautomationP" /><br/>
					</s:if> 						
					</span></td>
				</tr>	
			</s:if>
			<s:elseif test="automation=='no'">
			<tr height="40px;">
				<th></th>
					<td class="tdleft"><span class="apnetTagTitle"><br/><s:property value="getText('previewal.text.information')"/></span></td>
					<td class="tdright"><span class="apnetTextContent"><br/><s:property value="getText('previewal.text.notavailable')"/></span></td>
				</tr>	
			</s:elseif>
			<s:if test="extentNum!=''">
		<tr height="40px;">
			<th></th>
			<td class="tdleft"><span class="apnetTagTitle"><br/><s:property value="getText('previewal.text.extent')"/></span></td>
			<td class="tdright"><span class="apnetTextContent"><br/><s:property value="extentNum"></s:property></span></td>
		</tr>
		</s:if>
		<s:if test="dateUpdated!=''">
		<tr height="40px;">
			<th></th>
			<td class="tdleft"><span class="apnetTagTitle"><br/><s:property value="getText('previewal.text.fileupdated')"/></span></td>
			<td class="tdright"><span class="apnetTextContent"><br/><s:property value="dateUpdated"></s:property></span></td>
		</tr>
		</s:if>
		<s:elseif test="dateCreated!=''">
		<tr height="40px;">
			<th></th>
			<td class="tdleft"><span class="apnetTagTitle"><br/><s:property value="getText('previewal.text.filecreated')"/></span></td>
			<td class="tdright"><span class="apnetTextContent"><br/><s:property value="dateCreated"></s:property></span></td>
		</tr>
		</s:elseif>
		</table>
		</div>
<!--		<p>&nbsp;</p>
 		<div class="apnetsubTitle"><h2><s:property value="getText('directory.text.archivalinstitution')"/></h2></div>

		<p>&nbsp;</p> 	
		-->
		<div class="tableDirectory">
		<table width="100%">
		<s:if test="repositorhistP!=''">
		<tr height="40px;">
			<th></th>
			<td class="tdleft"><span class="apnetTagTitle"><br/><s:property value="getText('previewal.text.history')"/></span></td>
			<td class="tdright"><span class="apnetTextContent"><br/><s:property value="repositorhistP"></s:property></span></td>
		</tr>
		</s:if>
		<s:if test="date!=''">	
		<tr height="40px;">
			<th></th>
			<td class="tdleft"><span class="apnetTagTitle"><br/><s:property value="getText('previewal.text.foundation')"/></span></td>
			<td class="tdright"><span class="apnetTextContent"><br/><s:property value="date"></s:property>&nbsp; - &nbsp;<s:property value="rule"></s:property></span></td>
		</tr>	
		</s:if>
		<s:if test="firstnameResp!=''">	
		<tr height="40px;">
			<th></th>
			<td class="tdleft"><span class="apnetTagTitle"><br/><s:property value="getText('previewal.text.director')"/></span></td>
			<td class="tdright"><span class="apnetTextContent"><br/><s:property value="firstnameResp"></s:property>&nbsp; <s:property value="surnamesResp"></s:property>&nbsp; - &nbsp;<s:property value="chargeResp"></s:property></span></td>
		</tr>
		</s:if>
		<s:if test="adminunit!=''">	
		<tr height="40px;">
			<th></th>
			<td class="tdleft"><span class="apnetTagTitle"><br/><s:property value="getText('previewal.text.structure')"/></span></td>
			<td class="tdright"><span class="apnetTextContent"><br/><s:property value="adminunit"></s:property></span></td>
		</tr>
		</s:if>
		<s:if test="building!=''">
		<tr height="40px;">
			<th></th>
			<td class="tdleft"><span class="apnetTagTitle"><br/><s:property value="getText('previewal.text.archivebuilding')"/></span></td>
			<td class="tdright"><span class="apnetTextContent"><br/><s:property value="building"></s:property>&nbsp; <s:property value="repositorareaNum"></s:property>&nbsp; - &nbsp;<s:property value="lengthshelfNum"></s:property></span></td>
		</tr>
		</s:if>		
		
		<s:if test="notes!=''">
		<tr height="40px;">
			<th></th>
			<td class="tdleft"><span class="apnetTagTitle"><br/><s:property value="getText('previewal.text.additionalnotes')"/></span></td>
			<td class="tdright"><span class="apnetTextContent"><br/><s:property value="notes"></s:property></span></td>
		</tr>
		</s:if>
		</table>
		</div>
		<p>&nbsp;</p>
		<!--<p>&nbsp;</p> 
		 <div class="apnetsubTitle"><h2><s:property value="getText('directory.text.technicaldata')"/></h2></div>-->		
			
		<!-- <div class="tableDirectory">
		<table width="100%">
		 <tr height="40px;">
		<th></th>
			<td class="tdleft"><span class="apnetTagTitle"><br/><s:property value="getText('directory.text.contactperson')"/></span></td>
			<td class="tdright"><span class="apnetTextContent"><br/><s:property value="responsiblePersonSurname"></s:property>, &nbsp;<s:property value="responsiblePersonName"></s:property></span></td>
		</tr>
		<s:if test="dateUpdated!=''">
		<tr height="40px;">
			<th></th>
			<td class="tdleft"><span class="apnetTagTitle"><br/><s:property value="getText('directory.text.fileupdated')"/></span></td>
			<td class="tdright"><span class="apnetTextContent"><br/><s:property value="dateUpdated"></s:property></span></td>
		</tr>
		</s:if>
		<s:elseif test="dateCreated!=''">
		<tr height="40px;">
			<th></th>
			<td class="tdleft"><span class="apnetTagTitle"><br/><s:property value="getText('directory.text.filecreated')"/></span></td>
			<td class="tdright"><span class="apnetTextContent"><br/><s:property value="dateCreated"></s:property></span></td>
		</tr>
		</s:elseif>
		</table>
		</div>-->
		</div>
</s:if>		
	  