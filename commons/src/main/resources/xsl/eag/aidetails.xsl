<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xmlns='http://www.w3.org/1999/xhtml' xmlns:eag="http://www.archivesportaleurope.net/Portal/profiles/eag_2012/" xmlns:xlink="http://www.w3.org/1999/xlink"
	xmlns:ape="http://www.archivesportaleurope.eu/xslt/extensions" exclude-result-prefixes="xlink xlink xsi eag ape">

	<xsl:output method="html" indent="yes" version="4.0" encoding="UTF-8" />
	<xsl:param name="language.selected"/>
	<xsl:variable name="language.default" select="'eng'"/>
	<xsl:template match="/">
		<h2 class="blockHeader">
			<xsl:value-of select="./eag:eag/eag:archguide/eag:identity/eag:autform"></xsl:value-of>
		</h2>
	
		
		<!-- CONTACT -->
	<!-- starts loop -->
	
	<xsl:for-each select="./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository">
	   <xsl:variable name="id" select="position()"/>
	   <xsl:if test="count(current()/parent::node()/eag:repository)> 1">
		<h3 class="repositoryName" id="repositoryName_{$id}">
			<xsl:value-of select="./eag:repositoryName"></xsl:value-of>
		</h3>	
		</xsl:if>
		<div class="repositoryInfo" id="repositoryName_{$id}">
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
					
			     <xsl:if test="eag:location[not(@localType) or @localType='postal address']">
			      <tr class="longDisplay">
			        <td class="header">
			         <xsl:value-of select="ape:resource('directory.text.postaladdress')"></xsl:value-of>
			        </td>
					 <td id="address">
					  <xsl:if test="eag:location[not(@localType) or @localType='postal address']/eag:street/text()">
				         <xsl:value-of select="eag:location[not(@localType) or @localType='postal address']/eag:street" /> 
				         <xsl:if test="eag:location[not(@localType) or @localType='postal address']/eag:municipalityPostalcode/text()">
						    <xsl:text> , </xsl:text>
					     </xsl:if>
					  </xsl:if>	 
					  <xsl:if test="eag:location[not(@localType) or @localType='postal address']/eag:municipalityPostalcode/text()">	 		
				 	     <xsl:value-of select="eag:location[not(@localType) or @localType='postal address']/eag:municipalityPostalcode" />  
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
				 <xsl:call-template name="email">
					 	<xsl:with-param name="class" select="'header'"/>
				 </xsl:call-template>
				 <xsl:call-template name="webpage">
				 	<xsl:with-param name="class" select="'header'"/>
				 </xsl:call-template>
				 <xsl:call-template name="telephone">
				 	<xsl:with-param name="class" select="'header'"/>
				 </xsl:call-template>			

				  
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
				  
				  <xsl:if test="/eag:eag/eag:archguide/eag:identity/eag:repositoryType/text()">
				  <tr >
				    <td class="header">
				      <xsl:value-of select="ape:resource('directory.text.typeofarchive')" />	
				    </td>
                    <td>
                      <xsl:value-of select="/eag:eag/eag:archguide/eag:identity/eag:repositoryType" />
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
				 	<xsl:call-template name="multilanguage" >
				 	<xsl:with-param name="list" select="eag:access/eag:restaccess"></xsl:with-param>
				 	</xsl:call-template>
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
			      <td class="header subInfoHeader" colspan="2">
			       <xsl:value-of select="ape:resource('directory.text.searchroomcontact')" />
			      </td>
			     </tr>
				 <xsl:call-template name="email">
				 	<xsl:with-param name="parent" select="eag:services/eag:searchroom/eag:contact"/>
				 	<xsl:with-param name="class" select="'subHeader'"/>
				 	<xsl:with-param name="trClass" select="'longDisplay'"/>
				 </xsl:call-template>
				 <xsl:call-template name="webpage">
				 	<xsl:with-param name="parent" select="eag:services/eag:searchroom"/>
				 	<xsl:with-param name="class" select="'subHeader'"/>
				 	<xsl:with-param name="trClass" select="'longDisplay'"/>
				 </xsl:call-template>
				 <xsl:call-template name="telephone">
				 	<xsl:with-param name="parent" select="eag:services/eag:searchroom/eag:contact"/>
				 	<xsl:with-param name="class" select="'subHeader'"/>
				 	<xsl:with-param name="trClass" select="'longDisplay'"/>
				 </xsl:call-template>	

			    </xsl:if>

				
				      <!-- researchServices -->
				
			    <xsl:if test="eag:services/eag:searchroom/eag:researchServices/eag:descriptiveNote/eag:p/text()">
			      <tr class="longDisplay">
			        <td class="header">
			          <xsl:value-of select="ape:resource('directory.text.archivesresearchservice')" />
			        </td>
			        <td>
			      <xsl:call-template name="multilanguage">
				  	<xsl:with-param name="list" select="eag:services/eag:searchroom/eag:researchServices/eag:descriptiveNote/eag:p"/>
				  </xsl:call-template>
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
			      <xsl:call-template name="multilanguage">
				  	<xsl:with-param name="list" select="eag:services/eag:searchroom/eag:computerPlaces/eag:descriptiveNote/eag:p"/>
				  </xsl:call-template>
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
			         <td>
			         	<xsl:call-template name="multilanguage">
			         		<xsl:with-param name="list" select="eag:services/eag:internetAccess[@question='yes']/eag:descriptiveNote/eag:p"/>
			         	</xsl:call-template>
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
			    <xsl:if test="eag:services/eag:library/eag:contact" >
			     <tr class="longDisplay">
			      <td class="header subInfoHeader" colspan="2">
			       <xsl:value-of select="ape:resource('directory.text.librarycontact')" />
			      </td>
			     </tr>
				 <xsl:call-template name="email">
				 	<xsl:with-param name="parent" select="eag:services/eag:library/eag:contact"/>
				 	<xsl:with-param name="class" select="'subHeader'"/>
				 	<xsl:with-param name="trClass" select="'longDisplay'"/>
				 </xsl:call-template>
				 <xsl:call-template name="webpage">
				 	<xsl:with-param name="parent" select="eag:services/eag:library"/>
				 	<xsl:with-param name="class" select="'subHeader'"/>
				 	<xsl:with-param name="trClass" select="'longDisplay'"/>
				 </xsl:call-template>
				 <xsl:call-template name="telephone">
				 	<xsl:with-param name="parent" select="eag:services/eag:library/eag:contact"/>
				 	<xsl:with-param name="class" select="'subHeader'"/>
				 	<xsl:with-param name="trClass" select="'longDisplay'"/>
				 </xsl:call-template>	

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
						  	<xsl:call-template name="multilanguage">
				         		<xsl:with-param name="list" select="eag:services/eag:techservices/eag:reproductionser/eag:descriptiveNote/eag:p"/>
				         	</xsl:call-template>
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
			    <xsl:if test="eag:services/eag:techservices/eag:reproductionser/eag:contact" >
			     <tr class="longDisplay">
			      <td class="header subInfoHeader" colspan="2">
			       <xsl:value-of select="ape:resource('directory.text.reproductionsservicecontact')" />
			      </td>
			     </tr>
				 <xsl:call-template name="email">
				 	<xsl:with-param name="parent" select="eag:services/eag:techservices/eag:reproductionser/eag:contact"/>
				 	<xsl:with-param name="class" select="'subHeader'"/>
				 	<xsl:with-param name="trClass" select="'longDisplay'"/>
				 </xsl:call-template>
				 <xsl:call-template name="webpage">
				 	<xsl:with-param name="parent" select="eag:services/eag:techservices/eag:reproductionser"/>
				 	<xsl:with-param name="class" select="'subHeader'"/>
				 	<xsl:with-param name="trClass" select="'longDisplay'"/>
				 </xsl:call-template>
				 <xsl:call-template name="telephone">
				 	<xsl:with-param name="parent" select="eag:services/eag:techservices/eag:reproductionser/eag:contact"/>
				 	<xsl:with-param name="class" select="'subHeader'"/>
				 	<xsl:with-param name="trClass" select="'longDisplay'"/>
				 </xsl:call-template>	

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
							<xsl:call-template name="multilanguage">
				         		<xsl:with-param name="list" select="eag:services/eag:techservices/eag:restorationlab/eag:descriptiveNote/eag:p"/>
				         	</xsl:call-template>
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
			       <td class="header subInfoHeader" colspan="2" >
			        <xsl:value-of select="ape:resource('directory.text.restorationlabcontact')" />
			       </td>
			      </tr>
				 <xsl:call-template name="email">
				 	<xsl:with-param name="parent" select="eag:services/eag:techservices/eag:restorationlab/eag:contact"/>
				 	<xsl:with-param name="class" select="'subHeader'"/>
				 	<xsl:with-param name="trClass" select="'longDisplay'"/>
				 </xsl:call-template>
				 <xsl:call-template name="webpage">
				 	<xsl:with-param name="parent" select="eag:services/eag:techservices/eag:restorationlab"/>
				 	<xsl:with-param name="class" select="'subHeader'"/>
				 	<xsl:with-param name="trClass" select="'longDisplay'"/>
				 </xsl:call-template>
				 <xsl:call-template name="telephone">
				 	<xsl:with-param name="parent" select="eag:services/eag:techservices/eag:restorationlab/eag:contact"/>
				 	<xsl:with-param name="class" select="'subHeader'"/>
				 	<xsl:with-param name="trClass" select="'longDisplay'"/>
				 </xsl:call-template>				     

				</xsl:if>
				         <!-- refreshment --> 
				
				 <xsl:if test="eag:services/eag:recreationalServices/eag:refreshment">
				   <tr class="longDisplay">
				     <td class="header"><xsl:value-of select="ape:resource('directory.text.refreshmentarea')" /></td>
				     <td>
				       <xsl:call-template name="multilanguage">
			         		<xsl:with-param name="list" select="eag:services/eag:recreationalServices/eag:refreshment/eag:descriptiveNote/eag:p"/>
			         	</xsl:call-template>
				     </td>  
				   </tr>
				 </xsl:if>
				
				            <!-- exhibition -->
				
				 <xsl:if test= "eag:services/eag:recreationalServices/eag:exhibition">
				   <tr class="longDisplay">
				     <td class="header"><xsl:value-of select="ape:resource('directory.text.exhibition')" /></td>
				     <td>
				     	<xsl:call-template name="multilanguage">
			         		<xsl:with-param name="list" select="eag:services/eag:recreationalServices/eag:exhibition/eag:descriptiveNote/eag:p"/>
			         	</xsl:call-template>
					     <xsl:for-each select="eag:services/eag:recreationalServices/eag:toursSessions/eag:webpage">
							<xsl:variable name="webpage" select="@href"/>
							<div><a href="{$webpage}"  target="_blank" ><xsl:value-of select="." /></a></div>
						 </xsl:for-each>				         	
				     </td>
				   </tr> 

			     </xsl:if>
			     
				       <!-- toursSession -->
				 
			     <xsl:if test= "eag:services/eag:recreationalservices/eag:toursSessions">
				   <tr class="longDisplay">
				     <td class="header"><xsl:value-of select="ape:resource('directory.text.guidedtour')" /></td>
				     <td>
				     	<xsl:call-template name="multilanguage">
			         		<xsl:with-param name="list" select="eag:services/eag:recreationalServices/eag:toursSessions/eag:descriptiveNote/eag:p"/>
			         	</xsl:call-template>
			         	<xsl:for-each select="eag:services/eag:recreationalServices/eag:toursSessions/eag:webpage">
							<xsl:variable name="webpage" select="@href"/>
							<div><a href="{$webpage}"  target="_blank" ><xsl:value-of select="." /></a></div>
						 </xsl:for-each>
				     </td>  
				   </tr> 

			    </xsl:if>	
			    
				       <!-- otherServices -->
				 
			     <xsl:if test= "eag:services/eag:recreationalServices/eag:otherServices">
				   <tr class="longDisplay">
				     <td class="header"><xsl:value-of select="ape:resource('directory.text.otherservices')" /></td>
				     <td>
				     	<xsl:call-template name="multilanguage">
			         		<xsl:with-param name="list" select="eag:services/eag:recreationalServices/eag:otherServices/eag:descriptiveNote/eag:p"/>
			         	</xsl:call-template>
			         	<xsl:for-each select="eag:services/eag:recreationalServices/eag:otherServices/eag:webpage">
							<xsl:variable name="webpage" select="@href"/>
							<div><a href="{$webpage}"  target="_blank" ><xsl:value-of select="." /></a></div>
						 </xsl:for-each>
				     </td>  
				   </tr> 

			    </xsl:if>	
	   			<xsl:if test="count(current()/parent::node()/eag:repository)> 1">
				 <tr>
				  <td class="header"><xsl:value-of select="ape:resource('directory.text.associatedrepositories')" />
				  </td>
				  <td>
		            <xsl:for-each select="current()/parent::node()/eag:repository">
		               <xsl:variable name="otherRepositoryId" select="position()"/>
		               <xsl:if test="$id != $otherRepositoryId">
			    	   <div><a href= "javascript:displayRepository('{$otherRepositoryId}');"> <xsl:value-of select="./eag:repositoryName"/><xsl:text> (</xsl:text><xsl:value-of select="./eag:repositoryRole"/><xsl:text>)</xsl:text></a></div>
				        </xsl:if>    
	   		        </xsl:for-each>
	   		         
	   		      </td> 
	   		   </tr>	
   		   		</xsl:if>		     
			</tbody>
		</table>   
	<!-- END LOOP -->
		</div>
	</xsl:for-each>
	<div id="afterRepositories">
	</div>
	
			        <!-- ARCHIVES & HOLDINGS DESCRIPTION -->
			        
			 <table class="aiSection" id="archivesDisplay">
			  <thead>
				<tr>
					<th colspan="2">
						<xsl:value-of select="ape:resource('directory.text.archivesandholdings')" /><xsl:text> (</xsl:text><a class="displayLinkSeeMore" href="javascript:seeMore('archivesDisplay');" ><xsl:value-of select="ape:resource('directory.text.seemore')"/></a><a class="displayLinkSeeLess" href="javascript:seeLess('archivesDisplay');" ><xsl:value-of select="ape:resource('directory.text.seeless')"/></a><xsl:text>)</xsl:text>
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
			   <tr class="longDisplay" >
			    <td class="header"><xsl:value-of select="ape:resource('directory.text.holdings')" /></td>
			    <td>
			    		<xsl:call-template name="multilanguage">
			         		<xsl:with-param name="list" select="./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:holdings/eag:descriptiveNote/eag:p"/>
			         	</xsl:call-template>
			    </td>
			   </tr>
			 </xsl:if>
			 
			       <!-- extent -->
			 
			 <xsl:if test="./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:holdings/eag:extent/eag:num/text()">
			   <tr >
			    <td class="header"><xsl:value-of select="ape:resource('directory.text.extentholdings')" /></td>
			    <td><xsl:value-of select= "./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:holdings/eag:extent/eag:num" /><xsl:text> </xsl:text><xsl:value-of select="ape:resource('directory.text.extentholdings.unit')" /></td>
			   </tr>
			 </xsl:if>
			 
			       <!-- dates of holdings -->
			 
			 <xsl:if test="./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:holdings/eag:dateSet">
			   <tr >
			    <td class="header"><xsl:value-of select="ape:resource('directory.text.datesholdings')" /></td>
			    <td>
			     <xsl:for-each select= "./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:holdings/eag:dateSet/eag:date">
			      <xsl:value-of select= "." />
			      <xsl:text>, </xsl:text>
			     </xsl:for-each>
			     <xsl:for-each select= "./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:holdings/eag:dateSet/eag:dateRange">
			       <xsl:value-of select= "./eag:fromDate" /> 
			       <xsl:variable name="variable" select="./eag:toDate"></xsl:variable>
			       <xsl:choose>
			       		<xsl:when test="string(number(substring($variable,1,2)))!='NaN'">
			       			<xsl:text> - </xsl:text>
			       		</xsl:when>
			       		<xsl:otherwise><xsl:text> </xsl:text></xsl:otherwise>
			       </xsl:choose>
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
			   <tr class="longDisplay">
			    <td class="header"><xsl:value-of select="ape:resource('directory.text.historyofthearchives')" /></td>
			    <td>
			    	<xsl:call-template name="multilanguage">
			    		<xsl:with-param name="list" select="./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:repositorhist/eag:descriptiveNote/eag:p"></xsl:with-param>
			    	</xsl:call-template>
			    </td>
			   </tr>
			 </xsl:if>
			 
			     <!-- nonpreform  and useDates-->
			 
			 <xsl:if test="./eag:eag/eag:archguide/eag:identity/eag:nonpreform">
			  
			   <tr class="longDisplay">
			    <td class="subHeader"><xsl:value-of select="ape:resource('directory.text.alternative')" /></td>
			    <td>
			    	<xsl:call-template name="multilanguageNoperform">
			    		<xsl:with-param name="list" select="./eag:eag/eag:archguide/eag:identity/eag:nonpreform"/>
			    	</xsl:call-template>
			    </td>
			   </tr>
		
			 </xsl:if>
				
			     <!-- date of repositorfound -->
			 
			 <xsl:if test="./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:repositorfound/eag:date/text()">
			   <tr class="longDisplay">
			    <td class="subHeader"><xsl:value-of select="ape:resource('directory.text.daterepositorfound')" /></td>
			    <td><xsl:value-of select= "./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:repositorfound/eag:date" />
			    </td>
			   </tr>
			 </xsl:if>
			 
			   <!-- rule of repositorfound -->
			 
			 <xsl:if test="./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:repositorfound/eag:rule/text()">
			   <tr class="longDisplay">
			    <td class="subHeader"><xsl:value-of select="ape:resource('directory.text.rulerepositorfound')" /></td>
			    <td><xsl:value-of select= "./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:repositorfound/eag:rule" />
			    </td>
			   </tr>
			 </xsl:if>
			 
			    <!-- date of repositorsup -->
			 
			 <xsl:if test="./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:repositorsup/eag:date/text()">
			   <tr class="longDisplay">
			    <td class="subHeader"><xsl:value-of select="ape:resource('directory.text.daterepositorsup')" /></td>
			    <td><xsl:value-of select= "./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:repositorsup/eag:date" />
			    </td>
			   </tr>
			 </xsl:if>
			 
			     <!-- rule of repositorsup -->
			 
			 <xsl:if test="./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:repositorsup/eag:rule/text()">
			   <tr class="longDisplay" >
			    <td class="subHeader"><xsl:value-of select="ape:resource('directory.text.rulerepositorsup')" /></td>
			    <td><xsl:value-of select= "./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:repositorsup/eag:rule" />
			    </td>
			   </tr>
			 </xsl:if>
			 
			     <!-- adminunit --> 
			 
			  <xsl:if test="./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:adminhierarchy/eag:adminunit/text()">
			   <tr class="longDisplay">
			    <td class="header"><xsl:value-of select="ape:resource('directory.text.archivedepartment')" /></td>
			    <td>
			    		<xsl:call-template name="multilanguage">
			         		<xsl:with-param name="list" select="./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:adminhierarchy/eag:adminunit"/>
			         	</xsl:call-template>
			    </td>
			   </tr>
			 </xsl:if>
			 
			       <!-- building -->
			 
			 <xsl:if test="./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:buildinginfo/eag:building/eag:descriptiveNote/eag:p/text()">
			   <tr class="longDisplay">
			    <td class="header"><xsl:value-of select="ape:resource('directory.text.archivebuilding')" /></td>
			    <td>
			    		<xsl:call-template name="multilanguage">
			         		<xsl:with-param name="list" select="./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:buildinginfo/eag:building/eag:descriptiveNote/eag:p"/>
			         	</xsl:call-template>
			    </td>
			   </tr>
			 </xsl:if>
			 
			       <!-- repositorarea -->
			 
			  <xsl:if test="./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:buildinginfo/eag:repositorarea/eag:num/text()">
			   <tr class="longDisplay">
			    <td class="header"><xsl:value-of select="ape:resource('directory.text.buildingarea')" /></td>
			    <td><xsl:value-of select= "./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:buildinginfo/eag:repositorarea/eag:num" /><xsl:text> m</xsl:text><sup><xsl:text>2</xsl:text></sup>
			    </td>
			   </tr>
			 </xsl:if>
			 
			    <!-- lengthshelf -->
			 
			 <xsl:if test="./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:buildinginfo/eag:lengthshelf/eag:num/text()">
			   <tr class="longDisplay">
			    <td class="header"><xsl:value-of select="ape:resource('directory.text.lengthshelfavailable')" /></td>
			    <td><xsl:value-of select= "./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:buildinginfo/eag:lengthshelf/eag:num" /><xsl:text> linear metre</xsl:text>
			    </td>
			   </tr>
			 </xsl:if>
			 
			   <!-- lastupdate -->
			   
			<xsl:if test="./eag:eag/eag:control/eag:maintenanceHistory/eag:maintenanceEvent/eag:eventDateTime/text()">  
			 <tr>
			   <td class="header"><xsl:value-of select="ape:resource('directory.text.lastupdate')"/> </td>
			   <td>
			   		<xsl:variable name="numberOfMaintenanceEvent" select="count(./eag:eag/eag:control/eag:maintenanceHistory/eag:maintenanceEvent)"/>
			   		<xsl:value-of select= "./eag:eag/eag:control/eag:maintenanceHistory/eag:maintenanceEvent[$numberOfMaintenanceEvent]/eag:eventDateTime"/>
			   	</td>
			 </tr>
			 </xsl:if>
			 </tbody>
		</table>
		<input id="print" type="button" value="Print" onClick="javascript:printEAG();"/>
  </xsl:template>
	<xsl:template name="email">
		<xsl:param name="parent" select="current()"/>
		<xsl:param name="class"/>
			<xsl:param name="trClass" select="''"/>
			 <xsl:if test="$parent/eag:email/@href">		  
			  <tr class="{$trClass}">
				<td class="{$class}"><xsl:value-of select="ape:resource('directory.text.email')" /></td>
				<td>
				  <xsl:for-each select="$parent/eag:email">
					<xsl:variable name="email" select="@href"/>
					<div><a href="mailto:{$email}"  target="_blank"><xsl:value-of select="." /></a></div>
				</xsl:for-each>  
				</td>
			  </tr>
			</xsl:if>
	</xsl:template>
	<xsl:template name="webpage">
		<xsl:param name="parent" select="current()"/>
		<xsl:param name="class"/>
		<xsl:param name="trClass" select="''"/>
			 <xsl:if test="$parent/eag:webpage/@href">		  
			  <tr class="{$trClass}">
				<td class="{$class}"><xsl:value-of select="ape:resource('directory.text.webpage')" /></td>
					<td>
						 <xsl:for-each select="$parent/eag:webpage">
						 <xsl:variable name="webpage" select="@href"/>
						<div><a href="{$webpage}"  target="_blank" ><xsl:value-of select="." /></a></div>
						 </xsl:for-each>
					</td>
			  </tr>		
			</xsl:if>
	</xsl:template>
	<xsl:template name="telephone">
		<xsl:param name="parent" select="current()"/>
		<xsl:param name="class"/>
		<xsl:param name="trClass" select="''"/>
			 <xsl:if test="$parent/eag:telephone">		  
			  <tr class="{$trClass}">
				<td class="{$class}"><xsl:value-of select="ape:resource('directory.text.tel')" /></td>
				<td>
				  <xsl:for-each select="$parent/eag:telephone">
				  	
					<xsl:variable name="email" select="@href"/>
					<div><xsl:value-of select="." /></div>
				</xsl:for-each>  
				</td>
			  </tr>
			</xsl:if>
	</xsl:template>
	<xsl:template name="multilanguage">
		<xsl:param name="list"/>
		<xsl:choose>
			<xsl:when test="count($list) > 1">
				<xsl:choose>
					<xsl:when test="$list[@xml:lang = $language.selected]">
						<xsl:for-each select="$list[@xml:lang = $language.selected]">
							<p><xsl:value-of select="." /></p>
						</xsl:for-each>
					</xsl:when>	
					<xsl:when test="$list[@xml:lang = $language.default]">
						<xsl:for-each select="$list[@xml:lang = $language.default]">
							<p><xsl:value-of select="." /></p>
						</xsl:for-each>
					</xsl:when>
					<xsl:otherwise>
						<xsl:variable name="language.first" select="$list[1]/@xml:lang"></xsl:variable>
						<xsl:for-each select="$list[@xml:lang = $language.first]">
							<p><xsl:value-of select="." /></p>
						</xsl:for-each>
					</xsl:otherwise>			
				</xsl:choose>
			</xsl:when>
			<xsl:otherwise>
				<xsl:for-each select="$list">
					<p><xsl:value-of select="." /></p>
				</xsl:for-each>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<xsl:template name="multilanguageNoperform">
		<xsl:param name="list"/>
		<xsl:choose>
			<xsl:when test="count($list) > 1">
				<xsl:choose>
					<xsl:when test="$list[@xml:lang = $language.selected]">
						<xsl:for-each select="$list[@xml:lang = $language.selected]">
							<p><xsl:apply-templates select="."/></p>
						</xsl:for-each>
					</xsl:when>	
					<xsl:when test="$list[@xml:lang = $language.default]">
						<xsl:for-each select="$list[@xml:lang = $language.default]">
							<p><xsl:apply-templates select="."/></p>
						</xsl:for-each>
					</xsl:when>
					<xsl:otherwise>
						<xsl:variable name="language.first" select="$list[1]/@xml:lang"></xsl:variable>
						<xsl:for-each select="$list[@xml:lang = $language.first]">
							<p><xsl:apply-templates select="."/></p>
						</xsl:for-each>
					</xsl:otherwise>			
				</xsl:choose>
			</xsl:when>
			<xsl:otherwise>
				<xsl:for-each select="$list">
					<p><xsl:apply-templates select="."/></p>
				</xsl:for-each>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<xsl:template match="eag:nonpreform">
			<xsl:value-of select= "./text()" />
				  <xsl:if test="./eag:useDates/eag:dateSet/eag:dateRange or ./eag:useDates/eag:dateSet/eag:date">
				     <xsl:text> (</xsl:text>
				     <xsl:for-each select= "./eag:useDates/eag:dateSet/eag:date">
			            <xsl:value-of select= "." />
			            <xsl:text>, </xsl:text>
			         </xsl:for-each>
				     <xsl:for-each select= "./eag:useDates/eag:dateSet/eag:dateRange">
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
	</xsl:template>
</xsl:stylesheet>
