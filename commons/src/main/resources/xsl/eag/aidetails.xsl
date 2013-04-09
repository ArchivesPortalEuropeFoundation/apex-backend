<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xmlns='http://www.w3.org/1999/xhtml' xmlns:eag="http://www.archivesportaleurope.net/Portal/profiles/eag_2012/" xmlns:xlink="http://www.w3.org/1999/xlink"
	xmlns:ape="http://www.archivesportaleurope.eu/xslt/extensions" exclude-result-prefixes="xlink xlink xsi eag ape">

	<xsl:output method="html" indent="yes" version="4.0" encoding="UTF-8" />
	<xsl:template match="/">
		<h2 class="blockHeader">
			<xsl:value-of select="./eag:eag/eag:archguide/eag:identity/eag:autform"></xsl:value-of>
		</h2>
	
		
		<!-- CONTACT -->
	<!-- starts loop -->
	
	<xsl:for-each select="./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository">
	   <xsl:variable name="id" select="position()"/>
	   <xsl:if test="count(current()/parent::node()/eag:repository)> 1">
		<h3 class="repositoryName">
			<xsl:value-of select="./eag:repositoryName"></xsl:value-of>
		</h3>	
		</xsl:if>
		<div class="repositoryInfo">
		<table class="aiSection" id="contactDisplay_{$id}">
			<thead>
				<tr>
					<th colspan="2">
						<xsl:value-of select="ape:resource('directory.text.contact')" /><xsl:text> (</xsl:text><a class="displayLinkSeeMore" href="javascript:seeMore('contactDisplay_{$id}');" ><xsl:value-of select="ape:resource('directory.text.seemore')"/></a><a class="displayLinkSeeLess" href="javascript:seeLess('contactDisplay_{$id}');" ><xsl:value-of select="ape:resource('directory.text.seeless')"/></a><xsl:text>)</xsl:text>
					</th>
				</tr>
			</thead>
			<tbody>
			
			
			
	   
			   
			   <!-- visitors Address -->
			  <xsl:if test="eag:location[@localType='visitors address']"> 
			  <tr>
			      <td class="header">
				    <xsl:value-of select="ape:resource('directory.text.visitorsaddress')" />
				   </td>
				   <td id="address">
					  <xsl:if test="eag:location[@localType='visitors address']/eag:street/text()">
				         <xsl:value-of select="eag:location[@localType='visitors address']/eag:street" /> 
				         <xsl:if test="eag:location[@localType='visitors address']/eag:municipalityPostalcode/text()">
						    <xsl:text> , </xsl:text>
					     </xsl:if>
					  </xsl:if>	 
					  <xsl:if test="eag:location[@localType='visitors address']/eag:municipalityPostalcode/text()">	 		
				 	     <xsl:value-of select="eag:location[@localType='visitors address']/eag:municipalityPostalcode" />  
				      </xsl:if>
					</td>
			      </tr>
				 </xsl:if>
			  
			      <!-- localentity -->
			  
				 <xsl:if test="eag:location[@localType='visitors address']/eag:localentity/text()">
				 <tr class="longDisplay">
				   <td class="header">
				     <xsl:value-of select="ape:resource('directory.text.district')"/>
				    </td>
				    <td>
				      <xsl:value-of select="eag:location[@localType='visitors address']/eag:localentity"/>    
				    </td>  
				  </tr>
				  </xsl:if>
				  
				      <!-- secondem -->
				  
				  <xsl:if test="eag:location[@localType='visitors address']/eag:secondem/text()">
				  <tr class="longDisplay">
				    <td class="header">
				      <xsl:value-of select="ape:resource('directory.text.countrylocalauthority')"/>
				    </td>    
	                <td>
	                  <xsl:value-of select="eag:location[@localType='visitors address']/eag:secondem"></xsl:value-of>
	                </td>		
			      </tr>
			      </xsl:if>
				  
				    <!-- firstdem -->
				  
			      <xsl:if test="eag:location/eag:firstdem/text()">
				  <tr class="longDisplay">
				    <td class="header">
				      <xsl:value-of select="ape:resource('directory.text.regionautonomousauthority')"/>
				    </td>    
	                <td>
	                  <xsl:value-of select="eag:location/eag:firstdem"></xsl:value-of>
	                </td>		
			      </tr>
			      </xsl:if>
				  
				    <!-- postal address -->
					
			     <xsl:if test="eag:location[@localType='postal address']">
			      <tr class="longDisplay">
			        <td class="header">
			         <xsl:value-of select="ape:resource('directory.text.postaladdress')"></xsl:value-of>
			        </td>
					 <td id="address">
					  <xsl:if test="eag:location[@localType='postal address']/eag:street/text()">
				         <xsl:value-of select="eag:location[@localType='postal address']/eag:street" /> 
				         <xsl:if test="eag:location[@localType='postal address']/eag:municipalityPostalcode/text()">
						    <xsl:text> , </xsl:text>
					     </xsl:if>
					  </xsl:if>	 
					  <xsl:if test="eag:location[@localType='postal address']/eag:municipalityPostalcode/text()">	 		
				 	     <xsl:value-of select="eag:location[@localType='postal address']/eag:municipalityPostalcode" />  
				      </xsl:if>
					 </td>
			      </tr>
				 </xsl:if>
				  
				      <!-- country -->
					  
				  <xsl:if test="eag:location[@localType='visitors address']/eag:country/text()"> 
			       <tr>
					<td class="header">
						<xsl:value-of select="ape:resource('directory.text.country')" />
					</td>
					<td>
						<xsl:value-of select="eag:location[@localType='visitors address']/eag:country" />
					</td>
				  </tr>
				 </xsl:if> 
				  
				  <!-- email -->
				 <xsl:if test="eag:email/@href">		  
				  <tr>
					<td class="header"><xsl:value-of select="ape:resource('directory.text.email')" /></td>
					<td>
					  <xsl:for-each select="eag:email/@href">
						<xsl:variable name="email" select="."/>
						<div><a href="mailto:{$email}"  target="_blank" ><xsl:value-of select="$email" /></a></div>
					</xsl:for-each>  
					</td>
				  </tr>
				  
				  
				  </xsl:if>
				    <!-- webpage -->
				   <xsl:if test="eag:webpage/@href">		 
				  <tr>
					<td class="header"><xsl:value-of select="ape:resource('directory.text.webpage')" /></td>
					<td>
						 <xsl:for-each select="eag:webpage/@href">
						<xsl:variable name="webpage" select="."/>
						<div><a href="{$webpage}"  target="_blank" ><xsl:value-of select="$webpage" /></a></div>
						 </xsl:for-each>
					</td>
				  </tr>
				 
				  </xsl:if>
				  
				    <!-- telephone -->
				  <xsl:if test="eag:telephone">
				  <tr>
					<td class="header">
						<xsl:value-of select="ape:resource('directory.text.tel')" />
					</td>
					<td>
						<xsl:for-each select="eag:telephone">
						<div><xsl:value-of select="." /></div>
						</xsl:for-each>
					</td>
				  </tr>
				  </xsl:if>
				  
				      <!-- fax -->
				  <xsl:if test="eag:fax">
				  
				  <tr class="longDisplay">
				   <td class="header">
				     <xsl:value-of select="ape:resource('directory.text.fax')" />
				   </td>
				   <td>	<xsl:for-each select="eag:fax">
						<div><xsl:value-of select="." /></div>
						</xsl:for-each>
				   </td>
				  </tr>
				  </xsl:if>
				     <!-- repositoryType -->
				  
				  <xsl:if test="./eag:eag/eag:archguide/eag:identity/eag:repositoryType/text()">
				  <tr >
				    <td class="header">
				      <xsl:value-of select="ape:resource('directory.text.typeofarchive')" />	
				    </td>
                    <td>
                      <xsl:value-of select="./eag:eag/eag:archguide/eag:identity/eag:repositoryType" />
                    </td>			  
   				  </tr>
				  </xsl:if>
				
				     <!-- roleofthearchive -->
				
   				   <!--   <xsl:if test= "$repository = 'Head quarter'">-->
   				       <tr >
				        <td class="header">
				         <xsl:value-of select="ape:resource('directory.text.roleofthearchive')" />	
				        </td>
                        <td>
                         <xsl:value-of select="eag:repositoryRole" />
                        </td>			  
   				       </tr>
   				   
			 
			  </tbody>
			  
			  
			  </table>
			  
			  <!-- ACCESS INFORMATION -->
			  
			  <table class="aiSection" id="accessDisplay_{$id}">
			  <thead>
				<tr>
					<th colspan="2">
						<xsl:value-of select="ape:resource('directory.text.accessandserviceinfo')" /><xsl:text> (</xsl:text><a class="displayLinkSeeMore" href="javascript:seeMore('accessDisplay_{$id}');" ><xsl:value-of select="ape:resource('directory.text.seemore')"/></a><a class="displayLinkSeeLess" href="javascript:seeLess('accessDisplay_{$id}');" ><xsl:value-of select="ape:resource('directory.text.seeless')"/></a><xsl:text>)</xsl:text>
					</th>
				</tr>
			  </thead>
			  <tbody>
			     
			       <!-- opening -->
			  
			    <xsl:if test="eag:timetable/eag:opening/text()">	
			     <tr>
			      <td class="header">
			        <xsl:value-of select="ape:resource('directory.text.openinghours')"></xsl:value-of>
			      </td> 
			      <td>
			       <xsl:value-of select="eag:timetable/eag:opening" /> 
			      </td>
			     </tr>
			    </xsl:if>
				
				    <!-- closing -->
				
			    <xsl:if test="eag:timetable/eag:closing/text()">	
			    <tr>
			     <td class="header">
			       <xsl:value-of select="ape:resource('directory.text.closingdates')"></xsl:value-of>
			     </td> 
			     <td>
			      <xsl:value-of select="eag:timetable/eag:closing" /> 
			     </td>
			    </tr>
			    </xsl:if>
				 
				   <!-- directions and citations-->
				
			    
				  <xsl:for-each select="eag:directions">
				    <tr class="longDisplay">
			         <td class="header">
			           <xsl:value-of select="ape:resource('directory.text.directions')"></xsl:value-of>
			         </td>
			         <td>
			          <xsl:value-of select="eag:directions" />
			         </td>
			        </tr>
				    <xsl:for-each select="./eag:eag/eag:desc/eag:repositories/eag:repository/eag:directions/eag:citation">
				      <xsl:if test="./eag:eag/eag:desc/eag:repositories/eag:repository/eag:directions/eag:citation/@href">
			            <tr class="longDisplay">
			              <td class="header"></td>
					      <td>
			               <xsl:variable name="citation" select="./eag:eag/eag:desc/eag:repositories/eag:repository/eag:directions/eag:citation/@href"/>
			               <a href="{$citation}" target="_blank"> <xsl:value-of select="$citation"/></a>
			               </td>
			            </tr>
					  </xsl:if>
				   </xsl:for-each>	 
			     </xsl:for-each>	
			  
				
				    <!-- accessconditions -->
					
				<xsl:if test="eag:access">	
			      <tr >
			        <td class="header">
			           <xsl:value-of select="ape:resource('directory.text.accessconditions')" />
			        </td>
			        <td>
			         <xsl:choose>
						<xsl:when test="eag:access[@question='yes']">
							<xsl:value-of select="ape:resource('directory.text.accesspublic')" />
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="ape:resource('directory.text.accesspermission')" />
						</xsl:otherwise>
					</xsl:choose>
			     </td>
			    </tr>
			   </xsl:if>	
				    
					<!-- restaccess -->
				
			    <xsl:if test="eag:access/eag:restaccess/text()" >
			    <tr>
			     <td class="header"><xsl:value-of select="ape:resource('directory.text.accessinformation')" /></td>
				 <td>
				   <xsl:for-each select="eag:access/eag:restaccess">
					<p><xsl:value-of select="." /></p>
					
					</xsl:for-each>
				 </td> 
			    </tr> 
			    </xsl:if> 
				
				      <!-- accessibility-->
				 
			    <xsl:if test="eag:accessibility">
			    <tr class="longDisplay">
			      <td class="header">
			       <xsl:value-of select="ape:resource('directory.text.disabledaccess')" />
			      </td>
			        <td>
			         <xsl:choose>
						<xsl:when test="eag:accessibility[@question='yes']">
							<xsl:value-of select="ape:resource('directory.text.facilities')" />
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="ape:resource('directory.text.nofacilities')" />
						</xsl:otherwise>
					</xsl:choose>
			     </td>    
			    </tr>
			    </xsl:if>
			    <xsl:if test="eag:accessibility/text()">
			     <tr title="repository">
			      <td class="header"><xsl:value-of select="ape:resource('directory.text.facilitiesfordisabledpersons')" /></td>
				  <td>
					<xsl:value-of select="eag:accessibility" />
				  </td> 
			     </tr>
			    </xsl:if>
			    
				
				           <!-- termsOfUse-->
				<xsl:if test="eag:access/eag:termsOfUse">		   
				
				    <tr class="longDisplay">
				      <td class="header"><xsl:value-of select="ape:resource('directory.text.termsofuse')" />  
			          </td>
			          <td>
			          <xsl:for-each select="eag:access/eag:termsOfUse">
				          <div><xsl:choose>
				          	<xsl:when test="./@href">
				          	<xsl:variable name="term" select="./@href"/>
				              <a href="{$term}" target="_blank"><xsl:value-of select="." /></a>
				          	</xsl:when>
				          	<xsl:otherwise>
				          	<xsl:value-of select="." />
				          	</xsl:otherwise>
				          </xsl:choose></div>
			           </xsl:for-each>
					  </td>
					 </tr>
				</xsl:if>
				            <!-- readersTicket-->
				<xsl:if test="eag:services/eag:searchroom/eag:readersTicket">		
			   
				    <tr>
				      <td class="header"><xsl:value-of select="ape:resource('directory.text.readersticket')" />  
			          </td>
			          <td>
			           <xsl:for-each select="eag:services/eag:searchroom/eag:readersTicket">
				          <div><xsl:choose>
				          	<xsl:when test="./@href">
				          	<xsl:variable name="term" select="./@href"/>
				              <a href="{$term}" target="_blank"><xsl:value-of select="." /></a>
				          	</xsl:when>
				          	<xsl:otherwise>
				          	<xsl:value-of select="." />
				          	</xsl:otherwise>
				          </xsl:choose></div>
			           </xsl:for-each>
					  </td>
					 </tr>
				</xsl:if>
				       <!-- searchroom-->
				
				<xsl:if test="eag:services/eag:searchroom/eag:workPlaces/eag:num/text()">
			      <tr>
			        <td class="header">
			          <xsl:value-of select="ape:resource('directory.text.searchroom')" />
			        </td>
			        <td>
			         <xsl:value-of select="eag:services/eag:searchroom/eag:workPlaces/eag:num" /><xsl:text> seats </xsl:text>
			        </td>  
			      </tr>
				</xsl:if>  
				
				       <!-- advancedOrders-->
					   
			    <xsl:for-each select="eag:services/eag:searchroom/eag:advancedOrders">
				    <tr>
				      <td class="header"><xsl:value-of select="ape:resource('directory.text.orderingdocuments')" />  
			          </td>
			          <td>
			           <xsl:value-of select="." />
					  </td>
					 </tr>
					 <xsl:if test="./@href">
					   <tr title="repository">
					     <td class="header"></td>
						 <td> 
					      <xsl:variable name="advanced" select="./@href"/>
			              <a href="{$advanced}" target="_blank"> <xsl:value-of select="ape:resource('directory.text.advancedorderslink')"/></a>
			             </td>	
				       </tr>
					 </xsl:if> 
			    </xsl:for-each>
				
				      <!-- contact searchroom --> 
				
			    <xsl:if test="eag:services/eag:searchroom/eag:contact" >
			     <tr class="longDisplay">
			      <td class="header">
			       <xsl:value-of select="ape:resource('directory.text.searchroomcontact')" />
			      </td>
			     </tr>
			     <xsl:if test="eag:services/eag:searchroom/eag:contact/eag:email">
			      <xsl:for-each select="eag:services/eag:searchroom/eag:contact/eag:email/@href">
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
				
			    <xsl:if test="eag:services/eag:searchroom/eag:webpage/text()">
			     <xsl:for-each select="eag:services/eag:searchroom/eag:webpage/@href">
			       <tr class="longDisplay">
			         <td class="subHeader"><xsl:value-of select="ape:resource('directory.text.webpage')" /></td>
					 <td>
						<xsl:variable name="webpage" select="."/>
						<a href="{$webpage}"  target="_blank" ><xsl:value-of select="$webpage" /></a>
					</td>
				   </tr>
				 </xsl:for-each>
			    </xsl:if>
				
			    <xsl:if test="eag:services/eag:searchroom/eag:contact/eag:telephone/text()">
			     <xsl:for-each select="eag:services/eag:searchroom/eag:contact/eag:telephone" >
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
				
				      <!-- researchServices -->
				
			    <xsl:if test="eag:services/eag:searchroom/eag:researchServices/eag:descriptiveNote/eag:p/text()">
			      <tr class="longDisplay">
			        <td class="header">
			          <xsl:value-of select="ape:resource('directory.text.archivesresearchservice')" />
			        </td>
			        <td>
			        <xsl:for-each select= "eag:services/eag:searchroom/eag:researchServices/eag:descriptiveNote/eag:p">
			          <p><xsl:value-of select= "." /></p>
			         </xsl:for-each>
			        </td>
			      </tr>
			    </xsl:if>
				
				       <!-- computerPlaces -->    
				
			    <xsl:if test="eag:services/eag:searchroom/eag:computerPlaces">
			     <tr class="longDisplay">
			      <td class="header">
			       <xsl:value-of select="ape:resource('directory.text.computerplaces')" />
			      </td>
			      <td>
			       <xsl:value-of select= "eag:services/eag:searchroom/eag:computerPlaces/eag:num" />
			      </td>
			     </tr>
			    </xsl:if>
				
			    <xsl:if test="eag:services/eag:searchroom/eag:computerPlaces/eag:descriptiveNote/eag:p/text()">
			    <tr class="longDisplay">
			      <td class="header"></td>
			      <td>
			      <xsl:for-each select=  "eag:services/eag:searchroom/eag:computerPlaces/eag:descriptiveNote/eag:p">
			       <p><xsl:value-of select=  "." /></p>
			      </xsl:for-each>
			      </td> 
			    </tr>
			    </xsl:if>
				
				      <!-- microfilmPlaces -->
				
			    <xsl:if test="eag:services/eag:searchroom/eag:microfilmPlaces/eag:num/text()">
			     <tr class="longDisplay">
			      <td class="header">
			       <xsl:value-of select="ape:resource('directory.text.microfilmplaces')" />
			      </td>
			      <td>
			       <xsl:value-of select= "eag:services/eag:searchroom/eag:microfilmPlaces/eag:num" />
			      </td>
			     </tr>
			    </xsl:if>
				 
				     <!-- photographAllowance -->
				
			    <xsl:if test="eag:services/eag:searchroom/eag:photographAllowance/text()">
			     <tr class="longDisplay">
			      <td class="header">
			       <xsl:value-of select="ape:resource('directory.text.photographAllowance')" />
			      </td>
			      <td>
			       <xsl:value-of select= "eag:services/eag:searchroom/eag:photographAllowance" />
			      </td>
			     </tr>
			    </xsl:if>
				
				     <!-- internetAccess -->
				
			    <xsl:if test="eag:services/eag:internetAccess">
			     <tr >
			       <td class="header">
			          <xsl:value-of select="ape:resource('directory.text.publicinternetAccess')" />
			       </td>
			       <td>
			         <xsl:choose>
						<xsl:when test="eag:services/eag:internetAccess[@question='yes']">
							<xsl:value-of select="ape:resource('directory.text.publicinternet')" />
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="ape:resource('directory.text.nopublicinternet')" />
						</xsl:otherwise>
					 </xsl:choose>
			      </td> 
			     </tr>
				
				 <xsl:if test="eag:services/eag:internetAccess[@question='yes']/eag:descriptiveNote/eag:p/text()">  
			       <tr>
			         <td class="header"></td>  
			         <td> <xsl:for-each select= "eag:services/eag:internetAccess[@question='yes']/eag:descriptiveNote/eag:p">
			              <p><xsl:value-of select="."/></p>
			             </xsl:for-each> 
			         </td>
			       </tr>   
				 </xsl:if>
			    </xsl:if>
				
				   <!-- library -->
				
			    <xsl:if test="eag:services/eag:library">
			     <tr>
			     
			      <td class="header">
			         <xsl:value-of select="ape:resource('directory.text.library')" />
			      </td>
			      <td>
				    <xsl:if test="eag:services/eag:library[@question='yes']/eag:monographicpub/eag:num/text()">
						<xsl:value-of select= "eag:services/eag:library/eag:monographicpub/eag:num" /> <xsl:text> books</xsl:text>
					</xsl:if>
					<xsl:if test="eag:services/eag:library[@question='yes']/eag:serialpub/eag:num/text()">	
					   <xsl:if test="eag:services/eag:library[@question='yes']/eag:monographicpub/eag:num/text()">
					       <xsl:text>, </xsl:text>
					   </xsl:if>   
			            <xsl:value-of select= "eag:services/eag:library/eag:serialpub/eag:num" /> <xsl:text> periodicals</xsl:text>
					</xsl:if>	
					<xsl:if test ="eag:services/eag:library[@question='no']">
					 		<xsl:value-of select="ape:resource('directory.text.nolibrary')" />
					</xsl:if>	
			     </td>  
			     </tr>
				 	
				     <!-- contact library -->
				 
			     <xsl:if test="eag:services/eag:library/eag:contact">
			       <tr class="longDisplay">
			         <td class="header">
			           <xsl:value-of select="ape:resource('directory.text.librarycontact')" />
			         </td>
			       </tr>
				   
			      <xsl:if test="eag:services/eag:library/eag:contact/eag:email">	     
			       <xsl:for-each select= "eag:services/eag:library/eag:contact/eag:email/@href">
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
				 
			     <xsl:if test= "eag:services/eag:library/eag:webpage/text()">
			      <xsl:for-each select="eag:services/eag:library/eag:webpage/@href">
			       <tr class="longDisplay">
			         <td class="subHeader"><xsl:value-of select="ape:resource('directory.text.webpage')" /></td>
					 <td>
						<xsl:variable name="webpage" select="."/>
						<a href="{$webpage}"  target="_blank" ><xsl:value-of select="$webpage" /></a>
					</td>
				   </tr>
				  </xsl:for-each>
			     </xsl:if>
				 
			     <xsl:if test= "eag:services/eag:library/eag:contact/eag:telephone/text()">
			      <xsl:for-each select="eag:services/eag:library/eag:contact/eag:telephone">
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
				
				     <!-- reproductionser -->
				
			   <xsl:if test="eag:services/eag:techservices/eag:reproductionser">
			    <tr class="longDisplay">
			     <td class="header">
			      <xsl:value-of select="ape:resource('directory.text.reproductionsservices')" />
			     </td>
			      <td>
			         <xsl:choose>
						<xsl:when test="eag:services/eag:techservices/eag:reproductionser[@question='yes']">
						  <xsl:for-each select= "eag:services/eag:techservices/eag:reproductionser/eag:descriptiveNote/eag:p">
							<p><xsl:value-of select= "." /></p>
						  </xsl:for-each>
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="ape:resource('directory.text.noreproductionser')" />
						</xsl:otherwise>
					</xsl:choose>
			     </td> 
			    </tr>
			   </xsl:if>
			   
			          <!-- digitalser -->
			   
			   <xsl:if test="eag:services/eag:techservices/eag:reproductionser/eag:digitalser">
			    <tr class="longDisplay">
			     <td class="header">
			      <xsl:value-of select="ape:resource('directory.text.digitisationservice')" />
			     </td>
			      <td>
			         <xsl:choose>
						<xsl:when test="eag:services/eag:techservices/eag:reproductionser/eag:digitalser[@question='yes']">
							<xsl:value-of select= "ape:resource('directory.text.digitalser')" />
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="ape:resource('directory.text.nodigitalser')" />
						</xsl:otherwise>
					</xsl:choose>
			     </td> 
			    </tr>
			    </xsl:if>
			    
				       <!-- photocopyser -->
				
			    <xsl:if test="eag:services/eag:techservices/eag:reproductionser/eag:photocopyser">
			    <tr class="longDisplay">
			     <td class="header">
			      <xsl:value-of select="ape:resource('directory.text.photocopyingservice')" />
			     </td>
			      <td>
			         <xsl:choose>
						<xsl:when test="eag:services/eag:techservices//eag:reproductionser/eag:photocopyser[@question='yes']">
							<xsl:value-of select= "ape:resource('directory.text.photocopyser')" />
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="ape:resource('directory.text.nophotocopyser')" />
						</xsl:otherwise>
					</xsl:choose>
			     </td> 
			    </tr>
			    </xsl:if>
			    
				        <!-- photographser -->
				
			    <xsl:if test="eag:services/eag:techservices/eag:reproductionser/eag:photographser">
			    <tr class="longDisplay">
			     <td class="header">
			      <xsl:value-of select="ape:resource('directory.text.photographicservice')" />
			     </td>
			      <td>
			         <xsl:choose>
						<xsl:when test="eag:services/eag:techservices/eag:reproductionser/eag:photographser[@question='yes']">
							<xsl:value-of select= "ape:resource('directory.text.photographser')" />
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="ape:resource('directory.text.nophotographser')" />
						</xsl:otherwise>
					</xsl:choose>
			     </td> 
			    </tr>
			    </xsl:if>
			    
				        <!-- microformser -->
				
			    <xsl:if test="eag:services/eag:techservices/eag:reproductionser/eag:microformser">
			    <tr class="longDisplay">
			     <td class="header">
			      <xsl:value-of select="ape:resource('directory.text.microfilmservice')" />
			     </td>
			      <td>
			         <xsl:choose>
						<xsl:when test="eag:services/eag:techservices/eag.reproductionser/eag:microformser[@question='yes']">
							<xsl:value-of select= "ape:resource('directory.text.microfilmser')" />
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="ape:resource('directory.text.nomicrofilmser')" />
						</xsl:otherwise>
					</xsl:choose>
			     </td> 
			    </tr>
			    </xsl:if>
			    
				      <!-- contact reproductionser -->
				
			    <xsl:if test="eag:services/eag:techservices/eag:reproductionser/eag:contact">
			    <tr class="longDisplay">
			       <td class="header">
			        <xsl:value-of select="ape:resource('directory.text.reproductionsservicecontact')" />
			       </td>
			     </tr>
			     <xsl:if test="eag:services/eag:techservices/eag:reproductionser/eag:contact/eag:email">
			     <xsl:for-each select= "eag:services/eag:techservices/eag:reproductionser/eag:contact/eag:email/@href">
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
			     <xsl:if test="eag:services/eag:techservices/eag:reproductionser/eag:webpage/text()">
			     <xsl:for-each select="eag:services/eag:techservices/eag:reproductionser/eag:webpage/@href">
			     <tr class="longDisplay">
			       <td class="subHeader"><xsl:value-of select="ape:resource('directory.text.webpage')" /></td>
					<td>
						<xsl:variable name="webpage" select="."/>
						<a href="{$webpage}"  target="_blank" ><xsl:value-of select="$webpage" /></a>
					</td>
				 </tr>
				 </xsl:for-each>
			     </xsl:if>
			     <xsl:if test="eag:services/eag:techservices/eag:reproductionser/eag:contact/eag:telephone/text()">
			     <xsl:for-each select="eag:services/eag:techservices/eag:reproductionser/eag:contact/eag:telephone">
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
				  
				        <!-- restorationlab -->
				  
				<xsl:if test="eag:services/eag:techservices/eag:restorationlab">
				  <tr class="longDisplay">
			        <td class="header">
			          <xsl:value-of select="ape:resource('directory.text.conservationlaboratory')" />
			        </td>
			        <td>
			         <xsl:choose>
						<xsl:when test="eag:services/eag:techservices/eag:restorationlab[@question='yes']">
						   <xsl:for-each select= "eag:services/eag:techservices/eag:restorationlab/eag:descriptiveNote/eag:p">
							<p><xsl:value-of select= "." /></p>
						</xsl:for-each>
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="ape:resource('directory.text.norestorationlab')" />
						</xsl:otherwise>
					 </xsl:choose>
			       </td> 
			     </tr>
				</xsl:if>
				
				       <!-- contact restorationlab -->
				
				<xsl:if test="eag:services/eag:techservices/eag:restorationlab/eag:contact">
				  <tr class="longDisplay">
			       <td class="header">
			        <xsl:value-of select="ape:resource('directory.text.restorationlabcontact')" />
			       </td>
			      </tr>
			        <xsl:if test="eag:services/eag:techservices/eag:restorationlab/eag:contact/eag:email">
			         
			        <xsl:for-each select= "eag:services/eag:techservices/eag:restorationlab/eag:contact/eag:email/@href"> 
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
			     <xsl:if test="eag:services/eag:techservices/eag:restorationlab/eag:webpage/text()">
			      
			      <xsl:for-each select="eag:services/eag:techservices/eag:restorationlab/eag:webpage/@href">
			      <tr class="longDisplay">
			        <td class="subHeader"><xsl:value-of select="ape:resource('directory.text.webpage')" /></td>
					<td>
						<xsl:variable name="webpage" select="."/>
						<a href="{$webpage}"  target="_blank" ><xsl:value-of select="$webpage" /></a>
					</td>
				  </tr>
				  </xsl:for-each>
			     </xsl:if>
			     <xsl:if test="eag:services/eag:techservices/eag:restorationlab/eag:contact/eag:telephone/text()">
			      
			      <xsl:for-each select="eag:services/eag:techservices/eag:restorationlab/eag:contact/eag:telephone">
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
				
				         <!-- refreshment --> 
				
				 <xsl:if test="eag:services/eag:recreationalServices/eag:refreshment">
				   <tr class="longDisplay">
				     <td class="header"><xsl:value-of select="ape:resource('directory.text.refreshmentarea')" /></td>
				     <td><xsl:for-each select="eag:services/eag:recreationalServices/eag:refreshment/eag:descriptiveNote/eag:p">
				           <p><xsl:value-of select="."/></p> 
				          </xsl:for-each>
				     </td>  
				   </tr>
				 </xsl:if>
				
				            <!-- exhibition -->
				
				 <xsl:if test= "eag:services/eag:recreationalServices/eag:exhibition">
				   <tr class="longDisplay">
				     <td class="header"><xsl:value-of select="ape:resource('directory.text.exhibition')" /></td>
				     <td><xsl:for-each select="eag:services/eag:recreationalServices/eag:exhibition/eag:descriptiveNote/eag:p">
				       <p><xsl:value-of select="." /></p> 
				     </xsl:for-each> 
				     </td>
				   </tr> 
                   <xsl:if test= "eag:services/eag:recreationalServices/eag:exhibition/eag:webpage/text()">
			         <xsl:for-each select="eag:services/eag:recreationalServices/eag:exhibition/eag:webpage/@href">
			            <tr class="longDisplay">
			               <td class="header"></td>
					       <td>
						      <xsl:variable name="webpage" select="."/>
						      <a href="{$webpage}"  target="_blank" ><xsl:value-of select="ape:resource('directory.text.webpageexhibition')" /></a>
					       </td>
				        </tr>
				    </xsl:for-each>
			       </xsl:if>	
			     </xsl:if>
			     
				       <!-- toursSession -->
				 
			     <xsl:if test= "eag:services/eag:recreationalservices/eag:toursSessions">
				   <tr class="longDisplay">
				     <td class="header"><xsl:value-of select="ape:resource('directory.text.guidedtour')" /></td>
				     <td>
				     <xsl:for-each select="eag:services/eag:recreationalServices/eag:toursSessions/eag:descriptiveNote/eag:p">
				       <p><xsl:value-of select="." /></p>
				     </xsl:for-each>
				     </td>  
				   </tr> 
                 <xsl:if test= "eag:services/eag:recreationalServices/eag:toursSessions/eag:webpage/text()">
			     <xsl:for-each select="eag:services/eag:recreationalServices/eag:toursSession/eag:webpage/@href"> 
			       <tr class="longDisplay">
			          <td class="header"></td>
			          <td>
						<xsl:variable name="webpage" select="."/>
						<a href="{$webpage}"  target="_blank" ><xsl:value-of select="ape:resource('directory.text.gotothewebsite')" /></a>
					  </td>
				   </tr>
				 </xsl:for-each>
			     </xsl:if>	
			    </xsl:if>	
			    
				       <!-- otherServices -->
				 
			     <xsl:if test= "eag:services/eag:recreationalServices/eag:otherServices">
				   <tr class="longDisplay">
				     <td class="header"><xsl:value-of select="ape:resource('directory.text.otherservices')" /></td>
				     <td><xsl:for-each select="eag:services/eag:recreationalServices/eag:otherServices/eag:descriptiveNote/eag:p">
				            <p><xsl:value-of select="."/></p>
				          </xsl:for-each>
				     </td>  
				   </tr> 
				   
                 <xsl:if test= "eag:services/eag:recreationalServices/eag:otherServices/eag:webpage/text()">
			      <xsl:for-each select="eag:services/eag:recreationalServices/eag:otherServices/eag:webpage/@href">
			        <tr class="longDisplay">
			          <td class="header"></td>
			          <td>
						<xsl:variable name="webpage" select="."/>
						<a href="{$webpage}"  target="_blank" ><xsl:value-of select="ape:resource('directory.text.gotothewebsite')" /></a>
					  </td>
				    </tr>
				 </xsl:for-each>
			     </xsl:if>	
			    </xsl:if>	
			     
			</tbody>
		</table>   
	<!-- END LOOP -->
		</div>
	</xsl:for-each>
	<!-- STARTS LOOP_2 FOR LINKS : Associated repository -->
	<!-- ENDS LOOP_2 FOR LINKS : Associated repository -->
	<h3>
		<xsl:value-of select="ape:resource('directory.text.other')" />
	</h3>	
	  <table class="aiSection">
			  
			 <tbody>

			 <tr>
			  <td class="header"><xsl:value-of select="ape:resource('directory.text.associatedrepositories')" />
			  </td>
			  <td>
	            <xsl:for-each select="./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository">
	            
	               <xsl:variable name="id" select="position()"/>
		    	   <a href= "javascript:displayrepository('{$id}');"> <xsl:value-of select="./eag:repositoryName"/></a><br />
			            
   		        </xsl:for-each>
   		        
   		      </td> 
   		   </tr>
   		   </tbody>
   	 </table>
	
			        <!-- ARCHIVES & HOLDINGS DESCRIPTION -->
			        
			 <table class="aiSection" id="archivesDisplay">
			  <thead>
				<tr>
					<th colspan="2">
						<xsl:value-of select="ape:resource('directory.text.archivesandholdings')" />
					</th>
				</tr>
			 </thead>	 
			 <tbody>
			 
			         <!-- relationEntry -->
			
				<xsl:choose>
				  <xsl:when test="./eag:eag/eag:relations/eag:resourceRelation/@href">
				     <xsl:variable name="link" select="./eag:eag/eag:relations/eag:resourceRelation/@href"/>
				     <tr>
					   <td class="header"><xsl:value-of select="ape:resource('directory.text.relatedresource')" /></td>
				       <xsl:choose>
					     <xsl:when test="./eag:eag/eag:relations/eag:resourceRelation/eag:relationEntry/text()">    
			                  <td>
				                 <a href="{$link}"  target="_blank" ><xsl:value-of select="./eag:eag/eag:relations/eag:resourceRelation/eag:relationEntry" /></a>    
			                  </td>
						  </xsl:when>
						  <xsl:otherwise>
						    <td>
							   <xsl:text>There is no content in relationEntry. 
							   You have the posibility to add text (either locally, followed by a new upload or-later on- in the EAG 2012 web form on dashboard).
							   If you want to proceed, please click here: 
							   </xsl:text>     
				               <a href="{$link}"  target="_blank" ><xsl:value-of select="ape:resource('directory.text.linktorelatedresource')" /></a> 
							</td>	   
						  </xsl:otherwise>
						</xsl:choose>
					  </tr>	
					</xsl:when>
					<xsl:otherwise>
					  <xsl:if test="./eag:eag/eag:relations/eag:resourceRelation/eag:relationEntry/text()">	  		   
					    <tr>
						  <td class="header"><xsl:value-of select="ape:resource('directory.text.relatedresource')" /></td>
						  <td><xsl:value-of select= "./eag:eag/eag:relations/eag:resourceRelation/eag:relationEntry" /></td>
						</tr>
					  </xsl:if>
					</xsl:otherwise>
				  </xsl:choose>
				  	  	 			   
			        <!-- holdings -->
			 
			 <xsl:if test="./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:holdings/eag:descriptiveNote/eag:p/text()">
			   <tr class="longDisplay" title="repository">
			    <td class="header"><xsl:value-of select="ape:resource('directory.text.holdings')" /></td>
			    <td>
			     <xsl:for-each select="./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:holdings/eag:descriptiveNote/eag:p">
			        <p><xsl:value-of select= "." /></p>
			    </xsl:for-each>
			    </td>
			   </tr>
			 </xsl:if>
			 
			       <!-- extent -->
			 
			 <xsl:if test="./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:holdings/eag:extent/eag:num/text()">
			   <tr title="repository">
			    <td class="header"><xsl:value-of select="ape:resource('directory.text.extentholdings')" /></td>
			    <td><xsl:value-of select= "./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:holdings/eag:extent/eag:num" /><xsl:text> linear metre</xsl:text> </td>
			   </tr>
			 </xsl:if>
			 
			       <!-- dates of holdings -->
			 
			 <xsl:if test="./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:holdings/eag:dateSet">
			   <tr title="repository">
			    <td class="header"><xsl:value-of select="ape:resource('directory.text.datesholdings')" /></td>
			    <td>
			     <xsl:for-each select= "./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:holdings/eag:dateSet/eag:date">
			      <xsl:value-of select= "." />
			      <xsl:text>, </xsl:text>
			     </xsl:for-each>
			     <xsl:for-each select= "./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:holdings/eag:dateSet/eag:dateRange">
			       <xsl:value-of select= "./eag:fromDate" /> 
			       <xsl:variable name="variable" select="./eag:toDate"></xsl:variable>
			       <xsl:if test="string(number(substring($variable,1,2)))!='NaN'">
			         <xsl:text> - </xsl:text>
			       </xsl:if>
			       <xsl:value-of select= "./eag:toDate" /> 
				<!--    <xsl:if test="./eag:toDate[position()]<last()"> -->
				      <xsl:text>, </xsl:text>
				  <!--  </xsl:if>   -->
			     </xsl:for-each>
			    </td>
			   </tr>
			 </xsl:if>
			 
			        <!-- repositorhist -->
			 
			 <xsl:if test="./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:repositorhist/eag:descriptiveNote/eag:p/text()">
			   <tr class="longDisplay" title="repository">
			    <td class="header"><xsl:value-of select="ape:resource('directory.text.historyofthearchives')" /></td>
			    <td>
			     <xsl:for-each select="./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:repositorhist/eag:descriptiveNote/eag:p">
			       <p><xsl:value-of select= "." /></p>
			    
			     </xsl:for-each>
			    </td>
			   </tr>
			 </xsl:if>
			 
			     <!-- nonpreform  and useDates-->
			 
			 <xsl:if test="./eag:eag/eag:archguide/eag:identity/eag:nonpreform">
			  <xsl:for-each select="./eag:eag/eag:archguide/eag:identity/eag:nonpreform">
			   <tr class="longDisplay">
			    <td class="subHeader"><xsl:value-of select="ape:resource('directory.text.alternative')" /></td>
			    <td>
				  <xsl:value-of select= "." />
				  <xsl:if test="./eag:eag/eag:archguide/eag:identity/eag:nonpreform/eag:useDates">
				     <xsl:text> (</xsl:text>
				     <xsl:for-each select= "./eag:eag/eag:archguide/eag:identity/eag:nonpreform/eag:useDates/eag:dateSet/eag:date">
			            <xsl:value-of select= "." />
			            <xsl:text>, </xsl:text>
			         </xsl:for-each>
				     <xsl:for-each select= "./eag:eag/eag:archguide/eag:identity/eag:nonpreform/eag:useDates/eag:dateSet/eag:dateRange">
			           <xsl:value-of select= "./eag:fromDate" /> 
			           <xsl:variable name="variable" select="./eag:toDate"></xsl:variable>
			           <xsl:if test="string(number(substring($variable,1,2)))!='NaN'">
			              <xsl:text> - </xsl:text>
			           </xsl:if>
			          <xsl:value-of select= "./eag:toDate" /> 
				   <!--    <xsl:if test="./eag:toDate[position()]<last()">--> 
				         <xsl:text>, </xsl:text>
				   <!--    </xsl:if>  --> 
			        </xsl:for-each>
					<xsl:text>)</xsl:text>
				 </xsl:if>	
			    </td>
			   </tr>
			  </xsl:for-each>
			 </xsl:if>
				
			     <!-- date of repositorfound -->
			 
			 <xsl:if test="./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:repositorfound/eag:date/text()">
			   <tr class="longDisplay" title="repository">
			    <td class="subHeader"><xsl:value-of select="ape:resource('directory.text.daterepositorfound')" /></td>
			    <td><xsl:value-of select= "./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:repositorfound/eag:date" />
			    </td>
			   </tr>
			 </xsl:if>
			 
			   <!-- rule of repositorfound -->
			 
			 <xsl:if test="./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:repositorfound/eag:rule/text()">
			   <tr class="longDisplay" title="repository">
			    <td class="subHeader"><xsl:value-of select="ape:resource('directory.text.rulerepositorfound')" /></td>
			    <td><xsl:value-of select= "./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:repositorfound/eag:rule" />
			    </td>
			   </tr>
			 </xsl:if>
			 
			    <!-- date of repositorsup -->
			 
			 <xsl:if test="./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:repositorsup/eag:date/text()">
			   <tr class="longDisplay" title="repository">
			    <td class="subHeader"><xsl:value-of select="ape:resource('directory.text.daterepositorsup')" /></td>
			    <td><xsl:value-of select= "./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:repositorsup/eag:date" />
			    </td>
			   </tr>
			 </xsl:if>
			 
			     <!-- rule of repositorsup -->
			 
			 <xsl:if test="./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:repositorsup/eag:rule/text()">
			   <tr class="longDisplay" title="repository">
			    <td class="subHeader"><xsl:value-of select="ape:resource('directory.text.rulerepositorsup')" /></td>
			    <td><xsl:value-of select= "./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:repositorsup/eag:rule" />
			    </td>
			   </tr>
			 </xsl:if>
			 
			     <!-- adminunit --> 
			 
			  <xsl:if test="./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:adminhierarchy/eag:adminunit/text()">
			  <xsl:for-each select="./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:adminhierarchy/eag:adminunit">
			   <tr class="longDisplay" title="repository">
			    <td class="header"><xsl:value-of select="ape:resource('directory.text.archivedepartment')" /></td>
			    <td><xsl:value-of select= "." />
			    </td>
			   </tr>
			   </xsl:for-each>
			 </xsl:if>
			 
			       <!-- building -->
			 
			 <xsl:if test="./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:buildinginfo/eag:building/eag:descriptiveNote/eag:p/text()">
			   <tr class="longDisplay" title="repository">
			    <td class="header"><xsl:value-of select="ape:resource('directory.text.archivebuilding')" /></td>
			    <td>
			    <xsl:for-each select= "./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:buildinginfo/eag:building/eag:descriptiveNote/eag:p">
			      <p><xsl:value-of select= "." /></p>
			    </xsl:for-each>
			    </td>
			   </tr>
			 </xsl:if>
			 
			       <!-- repositorarea -->
			 
			  <xsl:if test="./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:buildinginfo/eag:repositorarea/eag:num/text()">
			   <tr class="longDisplay" title="repository">
			    <td class="header"><xsl:value-of select="ape:resource('directory.text.buildingarea')" /></td>
			    <td><xsl:value-of select= "./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:buildinginfo/eag:repositorarea/eag:num" /><xsl:text> m</xsl:text><sup><xsl:text>2</xsl:text></sup>
			    </td>
			   </tr>
			 </xsl:if>
			 
			    <!-- lengthshelf -->
			 
			 <xsl:if test="./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:buildinginfo/eag:lengthshelf/eag:num/text()">
			   <tr class="longDisplay" title="repository">
			    <td class="header"><xsl:value-of select="ape:resource('directory.text.lengthshelfavailable')" /></td>
			    <td><xsl:value-of select= "./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:buildinginfo/eag:lengthshelf/eag:num" /><xsl:text> linear metre</xsl:text>
			    </td>
			   </tr>
			 </xsl:if>
			 
			   <!-- lastupdate -->
			   
			<xsl:if test="./eag:eag/eag:control/eag:maintenanceHistory/eag:maintenanceEvent/eag:eventDateTime/text()">  
			 <tr>
			   <td class="header"><xsl:value-of select="ape:resource('directory.text.lastupdate')"/> </td>
			   <td><xsl:value-of select= "./eag:eag/eag:control/eag:maintenanceHistory/eag:maintenanceEvent/eag:eventDateTime"/></td>
			 </tr>
			 </xsl:if>
			 </tbody>
			 <tfoot>
				<tr>
					<td>
					</td>
					<td>
						<a id="displayLink" href="javascript:display('archivesDisplay');" ><xsl:value-of select="ape:resource('directory.text.hideshow')"/></a>
					</td>
				</tr>
			</tfoot>
		</table>
		<input id="print" type="button" value="Print" onClick="javascript:printEAG();"/>
   

  </xsl:template>
</xsl:stylesheet>