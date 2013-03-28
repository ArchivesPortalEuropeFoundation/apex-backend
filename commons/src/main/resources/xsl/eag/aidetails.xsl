<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xmlns='http://www.w3.org/1999/xhtml' xmlns:eag="http://www.archivesportaleurope.net/Portal/profiles/eag_2012/" xmlns:xlink="http://www.w3.org/1999/xlink"
	xmlns:ape="http://www.archivesportaleurope.eu/xslt/extensions" exclude-result-prefixes="xlink xlink xsi eag ape">

	<xsl:output method="html" indent="yes" version="4.0" encoding="UTF-8" />
	<xsl:template match="/">
		<h2 id="archivalInstitutionName" class="blockHeader">
			<xsl:value-of select="./eag:eag/eag:archguide/eag:identity/eag:autform"></xsl:value-of>
		</h2>
	
		
		<!-- CONTACT -->
	
		<table class="aiSection">
			<thead>
				<tr>
					<th colspan="2">
						<xsl:value-of select="ape:resource('directory.text.contact')" />
					</th>
				</tr>
			</thead>
			<tbody>
			<tr>
			   <td class="header">
				    <xsl:value-of select="ape:resource('directory.text.visitorsaddress')" />
				   </td>
				   <td id="address">
				      <xsl:value-of select="./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:location[@localType='visitors address']/eag:street" /> 
				      <xsl:text> , </xsl:text>
				 	  <xsl:value-of select="./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:location[@localType='visitors address']/eag:municipalityPostalcode" /> 
					 
				   </td>
				  </tr> 
				 <xsl:if test="./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:location/eag:localentity/text()">
				 <tr class="longDisplay" >
				   <td class="header">
				     <xsl:value-of select="ape:resource('directory.text.district')"/>
				    </td>
				    <td>
				      <xsl:value-of select="./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:location/eag:localentity"/>    
				    </td>  
				  </tr>
				  </xsl:if>
				  <xsl:if test="./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:location/eag:secondem/text()">
				  <tr class="longDisplay">
				    <td class="header">
				      <xsl:value-of select="ape:resource('directory.text.countrylocalauthority')"/>
				    </td>    
	                <td>
	                  <xsl:value-of select="./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:location/eag:secondem"></xsl:value-of>
	                </td>		
			      </tr>
			      </xsl:if>
			      <xsl:if test="./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:location/eag:firstdem/text()">
				  <tr class="longDisplay">
				    <td class="header">
				      <xsl:value-of select="ape:resource('directory.text.regionautonomousauthority')"/>
				    </td>    
	                <td>
	                  <xsl:value-of select="./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:location/eag:firstdem"></xsl:value-of>
	                </td>		
			      </tr>
			      </xsl:if>
			      <tr class="longDisplay">
			        <td class="header">
			         <xsl:value-of select="ape:resource('directory.text.postaladdress')"></xsl:value-of>
			        </td>
			        <td id="postalAddress">
                        <xsl:value-of select="./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:location[not(@localType) or @localType='postal address']/eag:street" />
                        <xsl:text> , </xsl:text>
			            <xsl:value-of select="./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:location[not(@localType) or @localType='postal address']/eag:municipalityPostalcode"></xsl:value-of>
			        </td>
			      </tr>
			       <tr>
					<td class="header">
						<xsl:value-of select="ape:resource('directory.text.country')" />
					</td>
					<td>
						<xsl:value-of select="./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:location/eag:country" />
					</td>
				  </tr>
			   <xsl:for-each select="./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:email/@href">
				  <tr>
				  
					<td class="header"><xsl:value-of select="ape:resource('directory.text.email')" /></td>
					<td>
					  
						<xsl:variable name="email" select="."/>
						<a href="mailto:{$email}"  target="_blank" ><xsl:value-of select="$email" /></a>
					
					</td>
				  </tr>
				  
				  </xsl:for-each>  
				  <xsl:for-each select="./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:webpage/@href">
				  <tr>
					<td class="header"><xsl:value-of select="ape:resource('directory.text.webpage')" /></td>
					<td>
						<xsl:variable name="webpage" select="."/>
						<a href="{$webpage}"  target="_blank" ><xsl:value-of select="$webpage" /></a>
					</td>
				  </tr>
				  </xsl:for-each>
				  <xsl:for-each select="./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:telephone">
				  <tr>
					<td class="header">
						<xsl:value-of select="ape:resource('directory.text.tel')" />
					</td>
					<td>
						<xsl:value-of select="." />
					</td>
				  </tr>
				  </xsl:for-each>
				  <xsl:for-each select="./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:fax">
				  <tr class="longDisplay">
				   <td class="header">
				     <xsl:value-of select="ape:resource('directory.text.fax')" />
				   </td>
				   <td>
						<xsl:value-of select="." />
				   </td>
				  </tr>
				  </xsl:for-each>
				  <xsl:if test="./eag:eag/eag:archguide/eag:identity/eag:repositoryType/text()">
				  <tr>
				    <td class="header">
				      <xsl:value-of select="ape:resource('directory.text.typeofarchive')" />	
				    </td>
                    <td>
                      <xsl:value-of select="./eag:eag/eag:archguide/eag:identity/eag:repositoryType" />
                    </td>			  
   				  </tr>
				  </xsl:if>
				  <xsl:if test="./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:repositoryRole/text()">
				  <tr>
				    <td class="header">
				      <xsl:value-of select="ape:resource('directory.text.roleofthearchive')" />	
				    </td>
                    <td>
                      <xsl:value-of select="./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:repositoryRole" />
                    </td>			  
   				  </tr>
				  </xsl:if>
			  </tbody>
			  </table>
			  
			  <!-- ACCESS INFORMATION -->
			  
			  <table class="aiSection">
			  <thead>
				<tr>
					<th colspan="2">
						<xsl:value-of select="ape:resource('directory.text.accessandserviceinfo')" />
					</th>
				</tr>
			  </thead>
			  <tbody>
			    <tr>
			     <td class="header">
			       <xsl:value-of select="ape:resource('directory.text.openinghours')"></xsl:value-of>
			     </td> 
			     <td>
			      <xsl:value-of select="./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:timetable/eag:opening" /> 
			     </td>
			    </tr>
			    <xsl:if test="./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:timetable/eag:closing/text()">	
			    <tr>
			     <td class="header">
			       <xsl:value-of select="ape:resource('directory.text.closingdates')"></xsl:value-of>
			     </td> 
			     <td>
			      <xsl:value-of select="./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:timetable/eag:closing" /> 
			     </td>
			    </tr>
			    </xsl:if>
			    <xsl:if test="./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:directions/text()">
			     <tr class="longDisplay">
			       <td class="header">
			         <xsl:value-of select="ape:resource('directory.text.directions')"></xsl:value-of>
			       </td>
			       <td>
			         <xsl:value-of select="./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:directions" />
			       </td>
			     </tr>
			    </xsl:if>	
			    <xsl:if test="./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:directions/eag:citation/text()">
			     <tr class="longDisplay">
			       <td class="header">
			         <xsl:variable name="citation" select="./eag:eag/eag:desc/eag:repositories/eag:repository/eag:directions/eag:citation/@href"/>
			         <a href="{$citation}" target="_blank"> <xsl:value-of select="ape:resource('directory.text.directionscitation')"/></a>
			       </td>
	
			     </tr>
			    </xsl:if>	
			    <tr>
			     <td class="header">
			       <xsl:value-of select="ape:resource('directory.text.accessconditions')" />
			     </td>
			     <td>
			         <xsl:choose>
						<xsl:when test="./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:access[@question='yes']">
							<xsl:value-of select="ape:resource('directory.text.accesspublic')" />
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="ape:resource('directory.text.accesspermission')" />
						</xsl:otherwise>
					</xsl:choose>
			     </td>
			    </tr>
			    <xsl:if test="./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:access/eag:restaccess/text()" >
			    <tr>
			     <td class="header"><xsl:value-of select="ape:resource('directory.text.accessinformation')" /></td>
				 <td>
				   <xsl:for-each select="./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:access/eag:restaccess">
					<xsl:value-of select="." />
					</xsl:for-each>
				 </td> 
			    </tr>
			    </xsl:if>  
			    <tr class="longDisplay">
			      <td class="header">
			       <xsl:value-of select="ape:resource('directory.text.disabledaccess')" />
			      </td>
			        <td>
			         <xsl:choose>
						<xsl:when test="./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:accessibility[@question='yes']">
							<xsl:value-of select="ape:resource('directory.text.facilities')" />
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="ape:resource('directory.text.nofacilities')" />
						</xsl:otherwise>
					</xsl:choose>
			     </td>    
			    </tr>
			    <tr>
			     <td class="header"><xsl:value-of select="ape:resource('directory.text.facilitiesfordisabledpersons')" /></td>
				 <td>
					<xsl:value-of select="./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:accessibility" />
				 </td> 
			    </tr>
			    <xsl:if test="./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:access/eag:termsOfUse/text()" >
			      <tr class="longDisplay">
			        <td class="header">
			          <xsl:value-of select="ape:resource('directory.text.termsofuse')" />
			        </td>
			        <td>
			        <xsl:for-each select="./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:access/eag:termsOfUse">
			          <xsl:value-of select="." />
			          </xsl:for-each>
			        </td>
			      </tr>
			      <tr>
			        <td class="header">
			         <xsl:variable name="term" select="./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:access/eag:termsOfUse/@href"/>
			         <a href="{$term}" target="_blank"> <xsl:value-of select="ape:resource('directory.text.termsofuselink')"/></a>
			       </td>
			      </tr>
			    </xsl:if>
			    <xsl:if test="./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:services/eag:searchroom/eag:readersTicket/text()" >
			    <xsl:for-each select="./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:services/eag:searchroom/eag:readersTicket">
			    <tr>
			     <td class="header"><xsl:value-of select="ape:resource('directory.text.readersticket')" />  
			     </td>
			     <td >
			      <xsl:value-of select="." />
			     </td>
			    </tr>
			    </xsl:for-each>
			    <tr>
			     <td class="header">
			         <xsl:variable name="readers" select="./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:services/eag:searchroom/eag:readersTicket/@href"/>
			         <a href="{$readers}" target="_blank"> <xsl:value-of select="ape:resource('directory.text.readersticketlink')"/></a>
			       </td>
			    </tr>
			    </xsl:if>
			    <tr>
			     <td class="header">
			      <xsl:value-of select="ape:resource('directory.text.searchroom')" />
			     </td>
			     <td>
			      <xsl:value-of select="./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:services/eag:searchroom/eag:workPlaces/eag:num" /><xsl:text> seats </xsl:text>
			     </td>  
			    </tr>
			    <xsl:if test="./eag:eag/eag:desc/eag:repositories/eag:repository/eag:services/eag:searchroom/eag:advancedOrders/text()" >
			     <tr>
			      <td class="header">
			          <xsl:value-of select="ape:resource('directory.text.orderingdocuments')" />
			      </td>
			      <td>
			       <xsl:value-of select="./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:services/eag:searchroom/eag:advancedOrders" />
			      </td>
			     </tr>
			     <tr>
			     <td class="header">
			         <xsl:variable name="advanced" select="./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:services/eag:searchroom/eag:advancedOrders/@href"/>
			         <a href="{$advanced}" target="_blank"> <xsl:value-of select="ape:resource('directory.text.advancedorderslink')"/></a>
			       </td>
			    </tr>
			    </xsl:if>
			    <xsl:if test="./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:services/eag:searchroom/eag:contact" >
			     <tr class="longDisplay">
			      <td class="header">
			       <xsl:value-of select="ape:resource('directory.text.searchroomcontact')" />
			      </td>
			     </tr>
			     <xsl:if test="./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:services/eag:searchroom/eag:contact/eag:email">
			     <xsl:for-each select="./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:services/eag:searchroom/eag:contact/eag:email/@href">
			     <tr class="longDisplay">
			      <td class="subHeader">
			       <xsl:value-of select="ape:resource('directory.text.email')" />
			      </td>
			      <td>
					<xsl:variable name="email" select="."/>
					<a href="mailto:{$email}"  target="_blank" ><xsl:value-of select="$email" /></a>			      
			      </td>
			     </tr>
			     </xsl:for-each>
			     </xsl:if>
			     </xsl:if>
			     <xsl:if test="./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:services/eag:searchroom/eag:webpage/text()">
			     <xsl:for-each select="./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:services/eag:searchroom/eag:webpage/@href">
			     <tr class="longDisplay">
			       <td class="subHeader"><xsl:value-of select="ape:resource('directory.text.webpage')" /></td>
					<td>
						<xsl:variable name="webpage" select="."/>
						<a href="{$webpage}"  target="_blank" ><xsl:value-of select="$webpage" /></a>
					</td>
				 </tr>
				 </xsl:for-each>
			     </xsl:if>
			     <xsl:if test="./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:services/eag:searchroom/eag:contact/eag:telephone/text()">
			     <xsl:for-each select="./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:services/eag:searchroom/eag:contact/eag:telephone" >
			      <tr class="longDisplay">
					<td class="subHeader">
						<xsl:value-of select="ape:resource('directory.text.tel')" />
					</td>
					<td>
						<xsl:value-of select="." />
					</td>
				  </tr>
				  </xsl:for-each>
			    </xsl:if>
			    <xsl:if test="./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:services/eag:searchroom/eag:researchServices/eag:descriptiveNote/eag:p/text()">
			      <tr class="longDisplay">
			        <td class="header">
			          <xsl:value-of select="ape:resource('directory.text.archivesresearchservice')" />
			        </td>
			        <td>
			        <xsl:for-each select= "./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:services/eag:searchroom/eag:researchServices/eag:descriptiveNote/eag:p">
			          <xsl:value-of select= "." />
			         </xsl:for-each>
			        </td>
			      </tr>
			    </xsl:if>
			    <xsl:if test="./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:services/eag:searchroom/eag:computerPlaces">
			     <tr class="longDisplay">
			      <td class="header">
			       <xsl:value-of select="ape:resource('directory.text.computerplaces')" />
			      </td>
			      <td>
			       <xsl:value-of select= "./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:services/eag:searchroom/eag:computerPlaces/eag:num" />
			      </td>
			     </tr>
			    </xsl:if>
			    <xsl:if test="./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:services/eag:searchroom/eag:computerPlaces/eag:descriptiveNote/eag:p/text()">
			    <tr class="longDisplay">
			      <td class="header"></td>
			      <td>
			      <xsl:for-each select=  "./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:services/eag:searchroom/eag:computerPlaces/eag:descriptiveNote/eag:p">
			       <xsl:value-of select=  "." />
			      </xsl:for-each>
			      </td>
			     
			    </tr>
			    </xsl:if>
			    <xsl:if test="./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:services/eag:searchroom/eag:microfilmPlaces">
			     <tr class="longDisplay">
			      <td class="header">
			       <xsl:value-of select="ape:resource('directory.text.microfilmplaces')" />
			      </td>
			      <td>
			       <xsl:value-of select= "./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:services/eag:searchroom/eag:microfilmPlaces/eag:num" />
			      </td>
			     </tr>
			    </xsl:if>
			    <xsl:if test="./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:services/eag:searchroom/eag:photographAllowance/text()">
			     <tr class="longDisplay">
			      <td class="header">
			       <xsl:value-of select="ape:resource('directory.text.photographAllowance')" />
			      </td>
			      <td>
			       <xsl:value-of select= "./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:services/eag:searchroom/eag:photographAllowance" />
			      </td>
			     </tr>
			    </xsl:if>
			    <xsl:if test="./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:services/eag:internetAccess">
			    <tr>
			     <td class="header">
			      <xsl:value-of select="ape:resource('directory.text.publicinternetAccess')" />
			     </td>
			     <td>
			         <xsl:choose>
						<xsl:when test="./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:services/eag:internetAccess[@question='yes']">
							<xsl:value-of select="ape:resource('directory.text.publicinternet')" />
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="ape:resource('directory.text.nopublicinternet')" />
						</xsl:otherwise>
					</xsl:choose>
			     </td> 
			     </tr>
			     <tr>
			     <xsl:if test="./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:services/eag:internetAccess[@question='yes']">
			        <td class="header"></td>  
			        <td> <xsl:value-of select= "./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:services/eag:internetAccess/eag:descriptiveNote/eag:p" /></td>
			     </xsl:if> 
			     </tr>   
			    </xsl:if>
			    <xsl:if test="./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:services/eag:library">
			     <tr>
			      <td class="header">
			         <xsl:value-of select="ape:resource('directory.text.library')" />
			      </td>
			      <td>
			         <xsl:choose>
						<xsl:when test="./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:services/eag:library[@question='yes']">
							<xsl:value-of select= "./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:services/eag:library/eag:monographicpub/eag:num" /> <xsl:text> books,</xsl:text>
			                <xsl:value-of select= "./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:services/eag:library/eag:serialpub/eag:num" /> <xsl:text> periodicals</xsl:text>
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="ape:resource('directory.text.nolibrary')" />
						</xsl:otherwise>
					</xsl:choose>
			     </td> 
			     
			     </tr>
			     <xsl:if test="./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:services/eag:library/eag:contact">
			     <tr class="longDisplay">
			       <td class="header">
			        <xsl:value-of select="ape:resource('directory.text.librarycontact')" />
			       </td>
			     </tr>
			     <xsl:if test="./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:services/eag:library/eag:contact/eag:email">
			     
			     <xsl:for-each select= "./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:services/eag:library/eag:contact/eag:email/@href">
			     <tr class="longDisplay">
			      <td class="subHeader">
			       <xsl:value-of select="ape:resource('directory.text.email')" />
			      </td>
			      
			      <td>
					<xsl:variable name="email" select= "."/>
					<a href="mailto:{$email}"  target="_blank" ><xsl:value-of select="$email" /></a>			      
			      </td>
			     </tr>
			     </xsl:for-each>
			     </xsl:if>
			     </xsl:if>
			     <xsl:if test= "./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:services/eag:library/eag:webpage/text()">
			     <xsl:for-each select="./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:services/eag:library/eag:webpage/@href">
			     <tr class="longDisplay">
			       <td class="subHeader"><xsl:value-of select="ape:resource('directory.text.webpage')" /></td>
					<td>
						<xsl:variable name="webpage" select="."/>
						<a href="{$webpage}"  target="_blank" ><xsl:value-of select="$webpage" /></a>
					</td>
				 </tr>
				 </xsl:for-each>
			     </xsl:if>
			     <xsl:if test= "./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:services/eag:library/eag:contact/eag:telephone/text()">
			      <xsl:for-each select="./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:services/eag:library/eag:contact/eag:telephone">
			      <tr class="longDisplay">
					<td class="subHeader">
						<xsl:value-of select="ape:resource('directory.text.tel')" />
					</td>
					<td>
						<xsl:value-of select="." />
					</td>
				  </tr>
				  </xsl:for-each>
			    </xsl:if>
			    </xsl:if>
			    
			    <tr class="longDisplay">
			     <td class="header">
			      <xsl:value-of select="ape:resource('directory.text.reproductionsservices')" />
			     </td>
			      <td>
			         <xsl:choose>
						<xsl:when test="./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:services/eag:techservices/eag:reproductionser[@question='yes']">
						  <xsl:for-each select= "./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:services/eag:techservices/eag:reproductionser/eag:descriptiveNote/eag:p">
							<xsl:value-of select= "." />
						</xsl:for-each>
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="ape:resource('directory.text.noreproductionser')" />
						</xsl:otherwise>
					</xsl:choose>
			     </td> 
			    </tr>
			   
			    <xsl:if test="./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:services/eag:techservices/eag:reproductionser/eag:digitalser">
			    <tr class="longDisplay">
			     <td class="header">
			      <xsl:value-of select="ape:resource('directory.text.digitisationservice')" />
			     </td>
			      <td>
			         <xsl:choose>
						<xsl:when test="./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:services/eag:techservices/eag:reproductionser/eag:digitalser[@question='yes']">
							<xsl:value-of select= "ape:resource('directory.text.digitalser')" />
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="ape:resource('directory.text.nodigitalser')" />
						</xsl:otherwise>
					</xsl:choose>
			     </td> 
			    </tr>
			    </xsl:if>
			    
			    <xsl:if test="./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:services/eag:techservices/eag:reproductionser/eag:photocopyser">
			    <tr class="longDisplay">
			     <td class="header">
			      <xsl:value-of select="ape:resource('directory.text.photocopyingservice')" />
			     </td>
			      <td>
			         <xsl:choose>
						<xsl:when test="./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:services/eag:techservices//eag:reproductionser/eag:photocopyser[@question='yes']">
							<xsl:value-of select= "ape:resource('directory.text.photocopyser')" />
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="ape:resource('directory.text.nophotocopyser')" />
						</xsl:otherwise>
					</xsl:choose>
			     </td> 
			    </tr>
			    </xsl:if>
			    
			    <xsl:if test="./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:services/eag:techservices/eag:reproductionser/eag:photographser">
			    <tr class="longDisplay">
			     <td class="header">
			      <xsl:value-of select="ape:resource('directory.text.photographicservice')" />
			     </td>
			      <td>
			         <xsl:choose>
						<xsl:when test="./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:services/eag:techservices/eag:reproductionser/eag:photographser[@question='yes']">
							<xsl:value-of select= "ape:resource('directory.text.photographser')" />
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="ape:resource('directory.text.nophotographser')" />
						</xsl:otherwise>
					</xsl:choose>
			     </td> 
			    </tr>
			    </xsl:if>
			    
			    <xsl:if test="./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:services/eag:techservices/eag:reproductionser/eag:microformser">
			    <tr class="longDisplay">
			     <td class="header">
			      <xsl:value-of select="ape:resource('directory.text.microfilmservice')" />
			     </td>
			      <td>
			         <xsl:choose>
						<xsl:when test="./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:services/eag:techservices/eag.reproductionser/eag:microformser[@question='yes']">
							<xsl:value-of select= "ape:resource('directory.text.microfilmser')" />
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="ape:resource('directory.text.nomicrofilmser')" />
						</xsl:otherwise>
					</xsl:choose>
			     </td> 
			    </tr>
			    </xsl:if>
			    
			    <xsl:if test="./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:services/eag:techservices/eag:reproductionser/eag:contact">
			    <tr class="longDisplay">
			       <td class="header">
			        <xsl:value-of select="ape:resource('directory.text.reproductionsservicecontact')" />
			       </td>
			     </tr>
			     <xsl:if test="./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:services/eag:techservices/eag:reproductionser/eag:contact/eag:email">
			     <xsl:for-each select= "./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:services/eag:techservices/eag:reproductionser/eag:contact/eag:email/@href">
			     <tr class="longDisplay">
			      <td class="subHeader">
			       <xsl:value-of select="ape:resource('directory.text.email')" />
			      </td>
			      <td>
					<xsl:variable name="email" select= "."/>
					<a href="mailto:{$email}"  target="_blank" ><xsl:value-of select="$email" /></a>			      
			      </td>
			     </tr>
			     </xsl:for-each>
			     </xsl:if>
			     </xsl:if>
			     <xsl:if test="./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:services/eag:techservices/eag:reproductionser/eag:webpage/text()">
			     <xsl:for-each select="./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:services/eag:techservices/eag:reproductionser/eag:webpage/@href">
			     <tr class="longDisplay">
			       <td class="subHeader"><xsl:value-of select="ape:resource('directory.text.webpage')" /></td>
					<td>
						<xsl:variable name="webpage" select="."/>
						<a href="{$webpage}"  target="_blank" ><xsl:value-of select="$webpage" /></a>
					</td>
				 </tr>
				 </xsl:for-each>
			     </xsl:if>
			     <xsl:if test="./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:services/eag:techservices/eag:reproductionser/eag:contact/eag:telephone/text()">
			     <xsl:for-each select="./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:services/eag:techservices/eag:reproductionser/eag:contact/eag:telephone">
			      <tr class="longDisplay">
					<td class="subHeader">
						<xsl:value-of select="ape:resource('directory.text.tel')" />
					</td>
					<td>
						<xsl:value-of select="." />
					</td>
				  </tr>
				  </xsl:for-each>
				  </xsl:if>
				  
				<xsl:if test="./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:services/eag:techservices/eag:restorationlab">
				  <tr class="longDisplay">
			        <td class="header">
			          <xsl:value-of select="ape:resource('directory.text.conservationlaboratory')" />
			        </td>
			        <td>
			         <xsl:choose>
						<xsl:when test="./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:services/eag:techservices/eag:restorationlab[@question='yes']">
						   <xsl:for-each select= "./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:services/eag:techservices/eag:restorationlab/eag:descriptiveNote/eag:p">
							<xsl:value-of select= "." />
						</xsl:for-each>
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="ape:resource('directory.text.norestorationlab')" />
						</xsl:otherwise>
					 </xsl:choose>
			       </td> 
			     </tr>
				</xsl:if>
				
				<xsl:if test="./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:services/eag:techservices/eag:restorationlab/eag:contact">
				  <tr class="longDisplay">
			       <td class="header">
			        <xsl:value-of select="ape:resource('directory.text.restorationlabcontact')" />
			       </td>
			      </tr>
			        <xsl:if test="./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:services/eag:techservices/eag:restorationlab/eag:contact/eag:email">
			         
			        <xsl:for-each select= "./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:services/eag:techservices/eag:restorationlab/eag:contact/eag:email/@href"> 
			         <tr class="longDisplay">
			           <td class="subHeader">
			             <xsl:value-of select="ape:resource('directory.text.email')" />
			           </td>
			           <td>
					    <xsl:variable name="email" select= "."/>
					    <a href="mailto:{$email}"  target="_blank" ><xsl:value-of select="$email" /></a>			      
			           </td>
			         </tr>
			         </xsl:for-each>
			        </xsl:if>
			     </xsl:if>
			     <xsl:if test="./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:services/eag:techservices/eag:restorationlab/eag:webpage/text()">
			      
			      <xsl:for-each select="./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:services/eag:techservices/eag:restorationlab/eag:webpage/@href">
			      <tr class="longDisplay">
			        <td class="subHeader"><xsl:value-of select="ape:resource('directory.text.webpage')" /></td>
					<td>
						<xsl:variable name="webpage" select="."/>
						<a href="{$webpage}"  target="_blank" ><xsl:value-of select="$webpage" /></a>
					</td>
				  </tr>
				  </xsl:for-each>
			     </xsl:if>
			     <xsl:if test="./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:services/eag:techservices/eag:restorationlab/eag:contact/eag:telephone/text()">
			      
			      <xsl:for-each select="./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:services/eag:techservices/eag:restorationlab/eag:contact/eag:telephone">
			      <tr class="longDisplay">
					<td class="subHeader">
						<xsl:value-of select="ape:resource('directory.text.tel')" />
					</td>
					<td>
						<xsl:value-of select="." />
					</td>
				  </tr>
				  </xsl:for-each>
				  </xsl:if>
				
				 <xsl:if test="./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:services/eag:recreationalServices/eag:refreshment">
				   <tr class="longDisplay">
				     <td class="header"><xsl:value-of select="ape:resource('directory.text.refreshmentarea')" /></td>
				     <td><xsl:value-of select="./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:services/eag:recreationalServices/eag:refreshment/eag:descriptiveNote/eag:p" /></td>  
				   </tr>
				 </xsl:if>
				
				 <xsl:if test= "./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:services/eag:recreationalServices/eag:exhibition">
				   <tr class="longDisplay">
				     <td class="header"><xsl:value-of select="ape:resource('directory.text.exhibition')" /></td>
				     <td><xsl:for-each select="./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:services/eag:recreationalServices/eag:exhibition/eag:descriptiveNote/eag:p">
				       <xsl:value-of select="." /> 
				     </xsl:for-each> 
				     </td>
				   </tr> 
                 <xsl:if test= "./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:services/eag:recreationalServices/eag:exhibition/eag:webpage/text()">
			    
			    <xsl:for-each select="./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:services/eag:recreationalServices/eag:exhibition/eag:webpage/@href">
			     <tr class="longDisplay">
			       <td class="header">
					
						<xsl:variable name="webpage" select="."/>
						<a href="{$webpage}"  target="_blank" ><xsl:value-of select="ape:resource('directory.text.webpageexhibition')" /></a>
					</td>
				 </tr>
				 </xsl:for-each>
			     </xsl:if>	
			     </xsl:if>
			     
			     <xsl:if test= "./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:services/eag:recreationalservices/eag:toursSessions">
				   <tr class="longDisplay">
				     <td class="header"><xsl:value-of select="ape:resource('directory.text.guidedtour')" /></td>
				     <td>
				     <xsl:for-each select="./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:services/eag:recreationalServices/eag:toursSessions/eag:descriptiveNote/eag:p">
				       <xsl:value-of select="." />
				     </xsl:for-each>
				     </td>  
				   </tr> 
                 <xsl:if test= "./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:services/eag:recreationalServices/eag:toursSessions/eag:webpage/text()">
			     <xsl:for-each select="./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:services/eag:recreationalServices/eag:toursSession/eag:webpage/@href"> 
			     <tr class="longDisplay">
			       <td class="header">
					
						<xsl:variable name="webpage" select="."/>
						<a href="{$webpage}"  target="_blank" ><xsl:value-of select="ape:resource('directory.text.gotothewebsite')" /></a>
					</td>
				 </tr>
				 </xsl:for-each>
			     </xsl:if>	
			     </xsl:if>	
			     
			     <xsl:if test= "./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:services/eag:recreationalServices/eag:otherServices">
				   <tr class="longDisplay">
				     <td class="header"><xsl:value-of select="ape:resource('directory.text.otherservices')" /></td>
				     <td><xsl:value-of select="./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:services/eag:recreationalServices/eag:otherServices/eag:descriptiveNote/eag:p" /></td>  
				   </tr> 
                 <xsl:if test= "./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:services/eag:recreationalServices/eag:otherServices/eag:webpage/text()">
			    
			    <xsl:for-each select="./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:services/eag:recreationalServices/eag:otherServices/eag:webpage/@href">
			     <tr class="longDisplay">
			       <td class="header">
					
						<xsl:variable name="webpage" select="."/>
						<a href="{$webpage}"  target="_blank" ><xsl:value-of select="ape:resource('directory.text.gotothewebsite')" /></a>
					</td>
				 </tr>
				 </xsl:for-each>
			     </xsl:if>	
			     </xsl:if>	
			     
			</tbody>
			</table>   
			
			        <!-- ARCHIVES & HOLDINGS DESCRIPTION -->
			        
			 <table class="aiSection">
			  <thead>
				<tr>
					<th colspan="2">
						<xsl:value-of select="ape:resource('directory.text.archivesandholdings')" />
					</th>
				</tr>
			 </thead>
			 
			 <tbody>
			 
			 <xsl:if test="./eag:eag/eag:relations/eag:resourceRelation/eag:relationEntry/text()">
			  <tr>
			   <td class="header"><xsl:value-of select="ape:resource('directory.text.relatedresource')" /></td>
			   <td><xsl:value-of select= "./eag:eag/eag:relations/eag:resourceRelation/eag:relationEntry" /></td>
			  </tr>
			 
			  <tr>
			   <td class="header">
			    <xsl:variable name="webpage" select="./eag:eag/eag:relations/eag:resourceRelation/@href"/>
				<a href="{$webpage}"  target="_blank" ><xsl:value-of select="ape:resource('directory.text.link')" /></a>
			   </td>
			  </tr>
			 
			 </xsl:if>
			 
			 <xsl:if test="./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:holdings/eag:descriptiveNote/eag:p/text()">
			   <tr class="longDisplay">
			    <td class="header"><xsl:value-of select="ape:resource('directory.text.holdings')" /></td>
			    <td>
			     <xsl:for-each select="./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:holdings/eag:descriptiveNote/eag:p">
			        <xsl:value-of select= "." />
			    </xsl:for-each>
			    </td>
			   </tr>
			 </xsl:if>
			 
			 <xsl:if test="./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:holdings/eag:extent">
			   <tr>
			    <td class="header"><xsl:value-of select="ape:resource('directory.text.extentholdings')" /></td>
			    <td><xsl:value-of select= "./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:holdings/eag:extent/eag:num" /><xsl:text>linear metre</xsl:text> </td>
			   </tr>
			 </xsl:if>
			 
			 <xsl:if test="./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:holdings/eag:dateSet">
			   <tr>
			    <td class="header"><xsl:value-of select="ape:resource('directory.text.datesholdings')" /></td>
			    <td><xsl:value-of select= "./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:holdings/eag:dateSet/eag:date" />
			      
			      <xsl:value-of select= "./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:holdings/eag:dateSet/eag:dateRange/eag:fromDate" /> 
			      <xsl:text>-</xsl:text>
			      <xsl:value-of select= "./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:holdings/eag:dateSet/eag:dateRange/eag:toDate" /> 
			    </td>
			   </tr>
			 </xsl:if>
			 
			 <xsl:if test="./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:repositorhist">
			   <tr class="longDisplay">
			    <td class="header"><xsl:value-of select="ape:resource('directory.text.historyofthearchives')" /></td>
			    <td>
			     <xsl:for-each select="./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:repositorhist/eag:descriptiveNote/eag:p">
			     <xsl:value-of select= "." />
			    
			     </xsl:for-each>
			    </td>
			   </tr>
			 </xsl:if>
			 
			  <xsl:if test="./eag:eag/eag:archguide/eag:identity/eag:nonpreform">
			  <xsl:for-each select="./eag:eag/eag:archguide/eag:identity/eag:nonpreform">
			   <tr class="longDisplay">
			    <td class="subHeader"><xsl:value-of select="ape:resource('directory.text.alternative')" /></td>
			    <td><xsl:value-of select= "." />
			    </td>
			   </tr>
			  
			 
			 
			 <xsl:if test="./eag:eag/eag:archguide/eag:identity/eag:nonpreform/eag:useDates">
			   <tr class="longDisplay">
			    <td class="subHeader"><xsl:value-of select="ape:resource('directory.text.used')" /></td>
			    <td><!-- <xsl:value-of select= "./eag:eag/eag:archguide/eag:identity/eag:nonpreform/eag:useDates/eag:dateSet/eag:date" /> -->
			      <xsl:value-of select= "./eag:eag/eag:archguide/eag:identity/eag:nonpreform/eag:useDates/eag:dateSet/eag:dateRange/eag:fromDate" /> 
			      <xsl:text>-</xsl:text>
			      <xsl:value-of select= "./eag:eag/eag:archguide/eag:identity/eag:nonpreform/eag:useDates/eag:dateSet/eag:dateRange/eag:toDate" /> 
			    </td>
			   </tr>
			 </xsl:if>
			 </xsl:for-each>
			 </xsl:if>
			 <xsl:if test="./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:repositorfound/eag:date/text()">
			   <tr class="longDisplay">
			    <td class="subHeader"><xsl:value-of select="ape:resource('directory.text.daterepositorfound')" /></td>
			    <td><xsl:value-of select= "./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:repositorfound/eag:date" />
			    </td>
			   </tr>
			 </xsl:if>
			 
			 <xsl:if test="./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:repositorfound/eag:rule/text()">
			   <tr class="longDisplay">
			    <td class="subHeader"><xsl:value-of select="ape:resource('directory.text.rulerepositorfound')" /></td>
			    <td><xsl:value-of select= "./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:repositorfound/eag:rule" />
			    </td>
			   </tr>
			 </xsl:if>
			 
			 <xsl:if test="./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:repositorsup/eag:date/text()">
			   <tr class="longDisplay">
			    <td class="subHeader"><xsl:value-of select="ape:resource('directory.text.daterepositorsup')" /></td>
			    <td><xsl:value-of select= "./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:repositorsup/eag:date" />
			    </td>
			   </tr>
			 </xsl:if>
			 
			 <xsl:if test="./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:repositorsup/eag:rule/text()">
			   <tr class="longDisplay">
			    <td class="header"><xsl:value-of select="ape:resource('directory.text.rulerepositorsup')" /></td>
			    <td><xsl:value-of select= "./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:repositorsup/eag:rule" />
			    </td>
			   </tr>
			 </xsl:if>
			 
			  <xsl:if test="./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:adminhierarchy/eag:adminunit/text()">
			  <xsl:for-each select="./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:adminhierarchy/eag:adminunit">
			   <tr class="longDisplay">
			    <td class="header"><xsl:value-of select="ape:resource('directory.text.archivedepartment')" /></td>
			    <td><xsl:value-of select= "." />
			    </td>
			   </tr>
			   </xsl:for-each>
			 </xsl:if>
			 
			 <xsl:if test="./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:buildinginfo/eag:building">
			   <tr class="longDisplay">
			    <td class="header"><xsl:value-of select="ape:resource('directory.text.archivebuilding')" /></td>
			    <td>
			    <xsl:for-each select= "./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:buildinginfo/eag:building/eag:descriptiveNote/eag:p">
			      <xsl:value-of select= "." />
			    </xsl:for-each>
			    </td>
			   </tr>
			 </xsl:if>
			 
			  <xsl:if test="./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:buildinginfo/eag:repositorarea">
			   <tr class="longDisplay">
			    <td class="header"><xsl:value-of select="ape:resource('directory.text.buildingarea')" /></td>
			    <td><xsl:value-of select= "./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:buildinginfo/eag:repositorarea/eag:num" /><xsl:text>m</xsl:text><sup><xsl:text>2</xsl:text></sup>
			    </td>
			   </tr>
			 </xsl:if>
			 
			 <xsl:if test="./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:buildinginfo/eag:lengthshelf">
			   <tr class="longDisplay">
			    <td class="header"><xsl:value-of select="ape:resource('directory.text.extentholdings')" /></td>
			    <td><xsl:value-of select= "./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:buildinginfo/eag:lengthshelf/eag:num" /><xsl:text> linear metre</xsl:text>
			    </td>
			   </tr>
			 </xsl:if>
			 
			 <tr>
			   <td class="header"><xsl:value-of select="ape:resource('directory.text.lastupdate')"/> </td>
			   <td><xsl:value-of select= "./eag:eag/eag:control/eag:maintenanceHistory/eag:maintenanceEvent/eag:eventDateTime"/></td>
			 </tr>
			 </tbody>
			 <tfoot>
				<tr>
					<td>
					</td>
					<td>
						<a id="displayLink" href="javascript:display();" ><xsl:value-of select="ape:resource('directory.text.hideshow')"/></a>
					</td>
				</tr>
			</tfoot>
		</table>
		<xsl:apply-templates select="./eag:eag/eag:archguide/eag:desc"></xsl:apply-templates>

	</xsl:template>
	<xsl:template match="eag:desc">
	    
	           
			        	
				<!--  
				
					<td class="header">
						<xsl:value-of select="ape:resource('directory.text.address')" />
					</td>
					<td id="address">
						<xsl:value-of select="./eag:street" />
						<xsl:text> - </xsl:text>
						<xsl:value-of select="./eag:postalcode" />
						<xsl:text> - </xsl:text>
						<xsl:value-of select="./eag:municipality" />
					</td>
				</tr>
				<tr>
					<td class="header">
						<xsl:value-of select="ape:resource('directory.text.country')" />
					</td>
					<td>
						<xsl:value-of select="./eag:country" />
					</td>
				</tr>
 				<tr>
					<td class="header">
						<xsl:value-of select="ape:resource('directory.text.tel')" />
					</td>
					<td>
						<xsl:value-of select="./eag:telephone" />
					</td>
				</tr>
				<tr>
					<td class="header"><xsl:value-of select="ape:resource('directory.text.email')" /></td>
					<td>
						<xsl:variable name="email" select="./eag:email/@href"/>
						<a href="mailto:{$email}"  target="_blank" ><xsl:value-of select="$email" /></a>
					</td>
				</tr>
				<tr>
					<td class="header"><xsl:value-of select="ape:resource('directory.text.webpage')" /></td>
					<td>
						<xsl:variable name="webpage" select="./eag:webpage/@href"/>
						<a href="{$webpage}"  target="_blank" ><xsl:value-of select="$webpage" /></a>
					</td>
				</tr>
				<xsl:if test="../eag:identity/eag:parform/text()">				
				<tr>
					<td class="header"><xsl:value-of select="ape:resource('directory.text.parallelformname')" /></td>
					<td><xsl:value-of select="../eag:identity/eag:parform" /></td>
				</tr>				
				</xsl:if>
			</tbody>
		</table>
		<table class="aiSection">
			<thead>
				<tr>
					<th colspan="2">
						<xsl:value-of select="ape:resource('directory.text.accessinfo')" />
					</th>
				</tr>
			</thead>
			<tbody>
				<tr>
					<td class="header"><xsl:value-of select="ape:resource('directory.text.accessanduse')" /></td>
					<td>
					<xsl:choose>
						<xsl:when test="./eag:access[@question='yes']">
							<xsl:value-of select="ape:resource('directory.text.accesspublic')" />
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="ape:resource('directory.text.accesspermission')" />
						</xsl:otherwise>
					</xsl:choose>
					</td>
				</tr>
				<tr>
					<td class="header"><xsl:value-of select="ape:resource('directory.text.access')" /></td>
					<td>
					<xsl:choose>
						<xsl:when test="./eag:buildinginfo/eag:handicapped[@question='yes']">
							<xsl:value-of select="ape:resource('directory.text.facilities')" />
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="ape:resource('directory.text.nofacilities')" />
						</xsl:otherwise>
					</xsl:choose>
					</td>
				</tr>
			</tbody>
		</table>
		<table class="aiSection">
			<thead>
				<tr>
					<th colspan="2">
						<xsl:value-of select="ape:resource('directory.text.service')" />
					</th>
				</tr>
			</thead>
			<tbody>
				<xsl:if test="./eag:buildinginfo/eag:searchroom/eag:num/text()">
				<tr>
					<td class="header"><xsl:value-of select="ape:resource('directory.text.readingroom')" /></td>
					<td><xsl:value-of select="./eag:buildinginfo/eag:searchroom/eag:num" /></td>
				</tr>
				</xsl:if>
				<tr>
					<td class="header"><xsl:value-of select="ape:resource('directory.text.libraryassets')" /></td>
					<td>
					<xsl:choose>
						<xsl:when test="./eag:techservices/eag:library[@question='yes']">
							<xsl:value-of select="ape:resource('directory.text.library')" />
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="ape:resource('directory.text.nolibrary')" />
						</xsl:otherwise>
					</xsl:choose>
					</td>
				</tr>	
				<tr>
					<td class="header"><xsl:value-of select="ape:resource('directory.text.restoration')" /></td>
					<td>
					<xsl:choose>
						<xsl:when test="./eag:techservices/eag:restorationlab[@question='yes']">
							<xsl:value-of select="ape:resource('directory.text.available')" />
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="ape:resource('directory.text.notavailable')" />
						</xsl:otherwise>
					</xsl:choose>
					</td>
				</tr>	
				<tr>
					<td class="header"><xsl:value-of select="ape:resource('directory.text.repro')" /></td>
					<td>
					<xsl:choose>
						<xsl:when test="./eag:techservices/eag:reproductionser[@question='yes']">
							<xsl:value-of select="ape:resource('directory.text.available')" />
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="ape:resource('directory.text.notavailable')" />
						</xsl:otherwise>
					</xsl:choose>
					</td>
				</tr>		
				<tr>
					<td class="header"><xsl:value-of select="ape:resource('directory.text.information')" /></td>
					<td>
					<xsl:choose>
						<xsl:when test="./eag:automation[@question='yes']">
							<xsl:value-of select="ape:resource('directory.text.available')" />
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="ape:resource('directory.text.notavailable')" />
						</xsl:otherwise>
					</xsl:choose>
					</td>
				</tr>
				<xsl:if test="./eag:extent/eag:num/text()">		
				<tr>
					<td class="header"><xsl:value-of select="ape:resource('directory.text.extent')" /></td>
					<td><xsl:value-of select="./eag:extent/eag:num" /></td>
				</tr>								
				</xsl:if>	
				<tr>
					<td class="header"><xsl:value-of select="ape:resource('directory.text.fileupdated')" /></td>
					<td>
					<xsl:choose>
						<xsl:when test="/eag:eag/eag:eagheader/eag:mainhist/eag:mainevent[@maintype='update']">
							<xsl:value-of select="/eag:eag/eag:eagheader/eag:mainhist/eag:mainevent[@maintype='update']/eag:date/@normal" />
						</xsl:when>
						<xsl:when test="/eag:eag/eag:eagheader/eag:mainhist/eag:mainevent[@maintype='create']">
							<xsl:value-of select="/eag:eag/eag:eagheader/eag:mainhist/eag:mainevent[@maintype='create']/eag:date/@normal" />
						</xsl:when>
					</xsl:choose>
					</td>
				</tr>		
				<xsl:if test="./eag:repositorguides/eag:repositorguide">		
				<tr>
					<td class="header"><xsl:value-of select="ape:resource('directory.text.hg')" /></td>
					<td><ul>
						<xsl:for-each select="./eag:repositorguides/eag:repositorguide">
							<xsl:variable name="href" select="./@href"/>
							<li><a href="{$href}"  target="_blank" ><xsl:value-of select="text()" /></a></li>									
						</xsl:for-each>
						</ul>
					</td>
				</tr>								
				</xsl:if>																				
			</tbody>
		</table>	-->	
		
	</xsl:template>
</xsl:stylesheet>
