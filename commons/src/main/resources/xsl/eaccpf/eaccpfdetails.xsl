<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xmlns='http://www.w3.org/1999/xhtml' xmlns:eac="urn:isbn:1-931666-33-4"
	xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:ape="http://www.archivesportaleurope.eu/xslt/extensions"
	exclude-result-prefixes="xlink xlink xsi eac ape">
	
	<xsl:output method="html" indent="yes" version="4.0"
		encoding="UTF-8"/>
	<xsl:param name="eaccontent.extref.prefix"/>
	<xsl:param name="language.selected"/>
	<xsl:param name="aiCodeUrl"/>
	<xsl:param name="eacUrlBase"/>
	<xsl:param name="eadUrl"/>
	<xsl:variable name="language.default" select="'eng'"/>
	<xsl:include href="commons.xsl"/>
	<xsl:include href="alternativeDescriptions.xsl"/>
	<xsl:include href="archivalMaterials.xsl"/>
	<xsl:include href="relatedNames.xsl"/>
	<xsl:include href="archives.xsl"/>
	<xsl:template match="text()" mode="other">
		<xsl:value-of select="ape:highlight(., 'other')" disable-output-escaping="yes" />
	</xsl:template>
	<xsl:template match="/">
		<xsl:variable name="existDates" select="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:existDates"/>
		<xsl:variable name="entityType" select="./eac:eac-cpf/eac:cpfDescription/eac:identity/eac:entityType"/>
		<h1 class="blockHeader">
		    <!-- nameEntry -->
	      	<xsl:call-template name="multilanguageName">
		       		 <xsl:with-param name="list" select="//eac:nameEntry"/>
		    </xsl:call-template> 
			<!-- dates -->
			<xsl:if test="$existDates/eac:date/text() or $existDates/eac:dateRange/eac:fromDate or $existDates/eac:dateRange/eac:toDate or $existDates/eac:dateSet/eac:date/text() or $existDates/eac:dateSet/eac:dateRange/eac:fromDate or $existDates/eac:dateSet/eac:dateRange/eac:toDate">
				<xsl:text> (</xsl:text>
				<span class="nameEtryDates">
					<!-- when there are only 1 dateSet -->
					<xsl:if test="$existDates/eac:dateSet and (($existDates/eac:dateSet/eac:dateRange/eac:fromDate or $existDates/eac:dateSet/eac:dateRange/eac:toDate) or ($existDates/eac:dateSet/eac:date and $existDates/eac:dateSet/eac:date/text()))">
						<xsl:apply-templates select="$existDates/eac:dateSet"/>
					</xsl:if>
					<!-- when there are only 1 dateRange -->
					<xsl:if test="$existDates/eac:dateRange and ($existDates/eac:dateRange/eac:fromDate or $existDates/eac:dateRange/eac:toDate)">
						<xsl:apply-templates select="$existDates/eac:dateRange"/>
					</xsl:if>
					<!-- when there are only 1 date -->
					<xsl:if test="$existDates/eac:date and $existDates/eac:date/text()">
						<xsl:apply-templates select="$existDates/eac:date"/>
					</xsl:if>
				</span>
				<xsl:text>)</xsl:text>
			</xsl:if>
			<xsl:if test="./eac:eac-cpf/eac:cpfDescription/eac:identity/eac:entityType/text()">
				<xsl:text> </xsl:text>
				<span id="entityType">
					<xsl:if test="$entityType='person'">
					   <xsl:value-of select="ape:resource('eaccpf.portal.person')"/>
					</xsl:if>
					<xsl:if test="$entityType='corporateBody'">
					   <xsl:value-of select="ape:resource('eaccpf.portal.corporateBody')"/>
					</xsl:if>
					<xsl:if test="$entityType='family'">
					   <xsl:value-of select="ape:resource('eaccpf.portal.family')"/>
					</xsl:if>
				</span>
			</xsl:if>
		</h1>
		<div id="details">	
			<!-- Dates -->
			<xsl:if test="$existDates/eac:dateRange/eac:fromDate/text() or $existDates/eac:dateSet/eac:dateRange/eac:fromDate/text()">
				<div class="row">
						<div class="leftcolumn">
							<h2>
								<xsl:if test="$entityType = 'person' or $entityType = 'family'">
									<xsl:value-of select="ape:resource('eaccpf.portal.birthDate')"/>
								</xsl:if>
								<xsl:if test="$entityType = 'corporateBody'">
									<xsl:value-of select="ape:resource('eaccpf.portal.foundationDate')"/>
								</xsl:if>	
								<xsl:text>:</xsl:text>
							</h2>	
						</div>
						<div class="rightcolumn">
							<xsl:if test="$existDates/eac:dateRange">
									<xsl:call-template name="dateUnknow">
										<xsl:with-param name="dateUnknow" select="$existDates/eac:dateRange/eac:fromDate"/>
									</xsl:call-template>
								</xsl:if> 
								<xsl:if test="$existDates/eac:dateSet/eac:dateRange/eac:fromDate">
									<xsl:call-template name="multilanguageOneDate">
										<xsl:with-param name="list" select="$existDates/eac:dateSet/eac:dateRange/eac:fromDate"/>
									</xsl:call-template>	
							</xsl:if>
							<xsl:if test="$entityType = 'person' or $entityType = 'family'">
								<xsl:if test="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:places/eac:place/eac:placeEntry[@localType='birth']">
							  		<xsl:text>, </xsl:text>
							  		<xsl:call-template name="multilanguagePlaceEntry">
							  			<xsl:with-param name="list" select="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:places/eac:place/eac:placeEntry[@localType='birth']"/>
							  		</xsl:call-template>		
							    </xsl:if>
							</xsl:if>
							<xsl:if test="$entityType = 'corporateBody'">
								<xsl:if test="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:places/eac:place/eac:placeEntry[@localType='foundation']">
							  		<xsl:text>, </xsl:text>
							  		<xsl:call-template name="multilanguagePlaceEntry">
							  			<xsl:with-param name="list" select="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:places/eac:place/eac:placeEntry[@localType='foundation']"/>
							  		</xsl:call-template>		
							    </xsl:if>
							</xsl:if>
						</div>
				</div>
			</xsl:if>
			<xsl:if test="$existDates/eac:dateRange/eac:toDate/text() or $existDates/eac:dateSet/eac:dateRange/eac:toDate/text()">
					<div class="row">
							<div class="leftcolumn">
						   		<h2>
						   		<xsl:if test="$entityType = 'person' or $entityType = 'family'">
									<xsl:value-of select="ape:resource('eaccpf.portal.deathDate')"/>
								</xsl:if>
								<xsl:if test="$entityType = 'corporateBody'">
									<xsl:value-of select="ape:resource('eaccpf.portal.closingDate')"/>
								</xsl:if>
						   		<xsl:text>:</xsl:text>
						   		</h2>
						   	</div>
						   	<div class="rightcolumn">
						   	    <xsl:if test="$existDates/eac:dateRange">
									<xsl:call-template name="dateUnknow">
										<xsl:with-param name="dateUnknow" select="$existDates/eac:dateRange/eac:toDate"/>
									</xsl:call-template>	
								</xsl:if> 
								<xsl:if test="$existDates/eac:dateSet/eac:dateRange/eac:toDate">
									<xsl:call-template name="multilanguageOneDate">
										<xsl:with-param name="list" select="$existDates/eac:dateSet/eac:dateRange/eac:toDate"/>
									</xsl:call-template>		
								</xsl:if>	
							    <xsl:if test="$entityType = 'person' or $entityType = 'family'">
									<xsl:if test="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:places/eac:place/eac:placeEntry[@localType='death']">
								  		<xsl:text>, </xsl:text>
								  		<xsl:call-template name="multilanguagePlaceEntry">
								  			<xsl:with-param name="list" select="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:places/eac:place/eac:placeEntry[@localType='death']"/>
								  		</xsl:call-template>		
								    </xsl:if>
								</xsl:if>
								<xsl:if test="$entityType = 'corporateBody'">
									<xsl:if test="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:places/eac:place/eac:placeEntry[@localType='suppression']">
								  		<xsl:text>, </xsl:text>
								  		<xsl:call-template name="multilanguagePlaceEntry">
								  			<xsl:with-param name="list" select="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:places/eac:place/eac:placeEntry[@localType='suppression']"/>
								  		</xsl:call-template>		
								    </xsl:if>
								</xsl:if>
							</div>
					</div>
			</xsl:if> 
			<!-- alternative names -->
			<xsl:if test="count(//eac:nameEntry) > 1">
				<div class="row">
					<div class="leftcolumn">
				   		<h2><xsl:value-of select="ape:resource('eaccpf.portal.alternativeForms')"/><xsl:text>:</xsl:text></h2>
				   	</div>
				   	<div class="rightcolumn">
				   			<xsl:call-template name="alternativeName">
				   				<xsl:with-param name="list" select="//eac:nameEntry"/>
				   				<xsl:with-param name="clazz" select="'alternativeName'"/>
				   			</xsl:call-template>  
					</div>
				</div>
			</xsl:if>
			<!-- location -->
				<xsl:if test="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:places/eac:place">
					<h2 class="title"><xsl:value-of select="ape:resource('eaccpf.portal.location')"/></h2>
					<xsl:for-each select="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:places/eac:place">
						<xsl:variable name="posChild" select="position()"/>
					    <!-- placeEntry in localDescription -->
						<xsl:call-template name="places">
				    		<xsl:with-param name="list" select="./eac:placeEntry"/>
				    		<xsl:with-param name="clazz" select="'locationPlace_'"/>
				    		<xsl:with-param name="posParent" select="''"/>
					    	<xsl:with-param name="posChild" select="$posChild"/>
				    	    <xsl:with-param name="title" select="'eaccpf.portal.location'"/> 
				    	</xsl:call-template>
				       <!-- placeRole -->
				       <xsl:if test="./eac:placeRole/text()">
					       	<div class="row subrow">
							    <div class="leftcolumn">
						   			<h2><xsl:value-of select="ape:resource('eaccpf.portal.roleOfLocation')"/><xsl:text>:</xsl:text></h2>
						   	    </div>
						     	<div class="rightcolumn">
						     		<xsl:apply-templates select="./eac:placeRole" mode="other"/>
							    </div>
					        </div>
					    </xsl:if>
				    	<xsl:if test="./eac:address/eac:addressLine[@localType='street']/text()">
					    	<xsl:call-template name="commonChild">
					   			<xsl:with-param name="list" select="./eac:address/eac:addressLine[@localType='street']"/>
					   			<xsl:with-param name="clazz" select="'language'"/>
					   			<xsl:with-param name="posParent" select="''"/>
		    					<xsl:with-param name="posChild" select="$posChild"/>
					    		<xsl:with-param name="title" select="'eaccpf.description.combo.address.component.street'"/>
					    	</xsl:call-template>
						</xsl:if>
						<xsl:if test="./eac:address/eac:addressLine[@localType='postalcode']/text()">
				    		<xsl:call-template name="commonChild">
					   			<xsl:with-param name="list" select="./eac:address/eac:addressLine[@localType='postalcode']"/>
					   			<xsl:with-param name="clazz" select="'language'"/>
					   			<xsl:with-param name="posParent" select="''"/>
		    					<xsl:with-param name="posChild" select="$posChild"/>
				    			<xsl:with-param name="title" select="'eaccpf.description.combo.address.component.postalcode'"/>
			    			</xsl:call-template>
					    </xsl:if>
					    <xsl:if test="./eac:address/eac:addressLine[@localType='localentity']/text()">
				    		<xsl:call-template name="commonChild">
					   			<xsl:with-param name="list" select="./eac:address/eac:addressLine[@localType='localentity']"/>
					   			<xsl:with-param name="clazz" select="'language'"/>
					   			<xsl:with-param name="posParent" select="''"/>
		    					<xsl:with-param name="posChild" select="$posChild"/>
				    			<xsl:with-param name="title" select="'eaccpf.description.combo.address.component.localentity'"/>
				    		</xsl:call-template>
						</xsl:if>
						<xsl:if test="./eac:address/eac:addressLine[@localType='secondem']/text()">
				    		<xsl:call-template name="commonChild">
					   			<xsl:with-param name="list" select="./eac:address/eac:addressLine[@localType='secondem']"/>
					   			<xsl:with-param name="clazz" select="'language'"/>
					   			<xsl:with-param name="posParent" select="''"/>
		    					<xsl:with-param name="posChild" select="$posChild"/>
				    			<xsl:with-param name="title" select="'eaccpf.description.combo.address.component.secondem'"/>
				    		</xsl:call-template>
						</xsl:if>
						<xsl:if test="./eac:address/eac:addressLine[@localType='firstdem']/text()">
				    		<xsl:call-template name="commonChild">
					   			<xsl:with-param name="list" select="./eac:address/eac:addressLine[@localType='firstdem']"/>
					   			<xsl:with-param name="clazz" select="'language'"/>
					   			<xsl:with-param name="posParent" select="''"/>
		    					<xsl:with-param name="posChild" select="$posChild"/>
				    			<xsl:with-param name="title" select="'eaccpf.description.combo.address.component.firstdem'"/>
				    		</xsl:call-template>
						</xsl:if>
						<xsl:if test="./eac:address/eac:addressLine[@localType='other']/text()">
				    		<xsl:call-template name="commonChild">
					   			<xsl:with-param name="list" select="./eac:address/eac:addressLine[@localType='other']"/>
					   			<xsl:with-param name="clazz" select="'language'"/>
					   			<xsl:with-param name="posParent" select="''"/>
		    					<xsl:with-param name="posChild" select="$posChild"/>
				    			<xsl:with-param name="title" select="'eaccpf.description.combo.address.component.other'"/>
				    		</xsl:call-template>
						</xsl:if>
				     </xsl:for-each>
				</xsl:if>
			<!-- localDescription -->
			<xsl:if test="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:localDescriptions/eac:localDescription">
			    <h2 class="title"><xsl:value-of select="ape:resource('eaccpf.portal.localDescription')"/></h2>
				<xsl:for-each select="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:localDescriptions"> 
					<xsl:variable name="posParent" select="position()"/>
				    <xsl:for-each select="./eac:localDescription">
				    		<!-- term localDescription -->
				    		<xsl:variable name="posChild" select="position()"/>
					    	<xsl:call-template name="term">
					    		<xsl:with-param name="list" select="./eac:term"/>
					    		<xsl:with-param name="clazz" select="'localDescription_'"/>
					    		<xsl:with-param name="posParent" select="$posParent"/>
					    		<xsl:with-param name="posChild" select="$posChild"/>
					    		<xsl:with-param name="title" select="'eaccpf.portal.localDescription'"/>
					    	</xsl:call-template>
							<!-- placeEntry in localDescription -->
							<xsl:call-template name="commonChild">
					    		<xsl:with-param name="list" select="./eac:placeEntry"/>
					    		<xsl:with-param name="clazz" select="'locationLocalDescription_'"/>
					    		<xsl:with-param name="posParent" select="$posParent"/>
					    		<xsl:with-param name="posChild" select="$posChild"/>
					    		<xsl:with-param name="title" select="'eaccpf.portal.location'"/>
					    	</xsl:call-template>
							<!-- citation in localDescription -->
						  	<xsl:call-template name="commonChild">
					    		<xsl:with-param name="list" select="./eac:citation"/>
					    		<xsl:with-param name="clazz" select="'citationLocalDescription_'"/>
					    		<xsl:with-param name="posParent" select="$posParent"/>
					    		<xsl:with-param name="posChild" select="$posChild"/>
					    		<xsl:with-param name="title" select="'eaccpf.portal.citation'"/>
					    	</xsl:call-template>
							<!-- dates in localDescription -->
							<xsl:call-template name="commonDates">
					    		<xsl:with-param name="date" select="./eac:date"/>
					    		<xsl:with-param name="dateRange" select="./eac:dateRange"/>
					    		<xsl:with-param name="dateSet" select="./eac:dateSet"/>
					    	</xsl:call-template>
							<!-- descriptiveNote in localDescription -->
							<xsl:call-template name="commonChild">
					    		<xsl:with-param name="list" select="./eac:descriptiveNote/eac:p"/>
					    		<xsl:with-param name="clazz" select="'descriptiveNoteLocalDescription_'"/>
					    		<xsl:with-param name="posParent" select="$posParent"/>
					    		<xsl:with-param name="posChild" select="$posChild"/>
					    		<xsl:with-param name="title" select="'eaccpf.portal.note'"/>
					    	</xsl:call-template>
					</xsl:for-each>
						<!-- descriptiveNote in localDescriptions -->
						 <xsl:call-template name="commonChild">
				    		<xsl:with-param name="list" select="./eac:descriptiveNote/eac:p"/>
				    		<xsl:with-param name="clazz" select="'descriptiveNoteLocalDescriptions_'"/>
				    		<xsl:with-param name="posParent" select="$posParent"/>
				    		<xsl:with-param name="posChild" select="''"/>
				    		<xsl:with-param name="title" select="'eaccpf.portal.note'"/>
				    	 </xsl:call-template>
				</xsl:for-each>		
		   </xsl:if>
		   <!-- legalStatus -->
		   <xsl:if test="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:legalStatuses/eac:legalStatus">
			    <h2 class="title"><xsl:value-of select="ape:resource('eaccpf.portal.legalStatus')"/></h2>
				<xsl:for-each select="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:legalStatuses">
					<xsl:variable name="posParent" select="position()"/> 
				    <xsl:for-each select="./eac:legalStatus">
				    		<xsl:variable name="posChild" select="position()"/>
				    		<!-- term localDescription -->
					    	<xsl:call-template name="term">
					    		<xsl:with-param name="list" select="./eac:term"/>
					    		<xsl:with-param name="clazz" select="'legalStatus_'"/>
					    		<xsl:with-param name="posParent" select="$posParent"/>
					    		<xsl:with-param name="posChild" select="$posChild"/>
					    		<xsl:with-param name="title" select="'eaccpf.portal.legalStatus'"/>
					    	</xsl:call-template>
							<!-- placeEntry in localDescription -->
							<xsl:call-template name="commonChild">
					    		<xsl:with-param name="list" select="./eac:placeEntry"/>
					    		<xsl:with-param name="clazz" select="'locationLegalStatus_'"/>
					    		<xsl:with-param name="posParent" select="$posParent"/>
					    		<xsl:with-param name="posChild" select="$posChild"/>
					    		<xsl:with-param name="title" select="'eaccpf.portal.location'"/>
					    	</xsl:call-template>
							<!-- citation in localDescription -->
						  	<xsl:call-template name="commonChild">
					    		<xsl:with-param name="list" select="./eac:citation"/>
					    		<xsl:with-param name="clazz" select="'citationLegalStatus_'"/>
					    		<xsl:with-param name="posParent" select="$posParent"/>
					    		<xsl:with-param name="posChild" select="$posChild"/>
					    		<xsl:with-param name="title" select="'eaccpf.portal.citation'"/>
					    	</xsl:call-template>
							<!-- dates in localDescription -->
							<xsl:call-template name="commonDates">
					    		<xsl:with-param name="date" select="./eac:date"/>
					    		<xsl:with-param name="dateRange" select="./eac:dateRange"/>
					    		<xsl:with-param name="dateSet" select="./eac:dateSet"/>
					    	</xsl:call-template>
							<!-- descriptiveNote in localDescription -->
							<xsl:call-template name="commonChild">
					    		<xsl:with-param name="list" select="./eac:descriptiveNote/eac:p"/>
					    		<xsl:with-param name="clazz" select="'descriptiveNoteLegalStatus_'"/>
					    		<xsl:with-param name="posParent" select="$posParent"/>
					    		<xsl:with-param name="posChild" select="$posChild"/>
					    		<xsl:with-param name="title" select="'eaccpf.portal.note'"/>
					    	</xsl:call-template>
					</xsl:for-each>
						<!-- descriptiveNote in localDescriptions -->
						 <xsl:call-template name="commonChild">
				    		<xsl:with-param name="list" select="./eac:descriptiveNote/eac:p"/>
				    		<xsl:with-param name="clazz" select="'descriptiveNoteLegalStatuses_'"/>
				    		<xsl:with-param name="posParent" select="$posParent"/>
				    		<xsl:with-param name="posChild" select="''"/>
				    		<xsl:with-param name="title" select="'eaccpf.portal.note'"/>
				    	 </xsl:call-template>
				</xsl:for-each>		
		   </xsl:if>
		   <!-- function -->
		   <xsl:if test="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:functions/eac:function">
			    <h2 class="title"><xsl:value-of select="ape:resource('eaccpf.portal.function')"/></h2>
				<xsl:for-each select="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:functions">
					<xsl:variable name="posParent" select="position()"/> 
				    <xsl:for-each select="./eac:function">
				    		<xsl:variable name="posChild" select="position()"/>
				    		<!-- term function -->
					    	<xsl:call-template name="term">
					    		<xsl:with-param name="list" select="./eac:term"/>
					    		<xsl:with-param name="clazz" select="'function_'"/>
					    		<xsl:with-param name="posParent" select="$posParent"/>
					    		<xsl:with-param name="posChild" select="$posChild"/>
					    		<xsl:with-param name="title" select="'eaccpf.portal.function'"/>
					    	</xsl:call-template>
							<!-- placeEntry in function-->
							<xsl:call-template name="commonChild">
					    		<xsl:with-param name="list" select="./eac:placeEntry"/>
					    		<xsl:with-param name="clazz" select="'locationFunction_'"/>
					    		<xsl:with-param name="posParent" select="$posParent"/>
					    		<xsl:with-param name="posChild" select="$posChild"/>
					    		<xsl:with-param name="title" select="'eaccpf.portal.location'"/>
					    	</xsl:call-template>
							<!-- citation in function -->
						  	<xsl:call-template name="commonChild">
					    		<xsl:with-param name="list" select="./eac:citation"/>
					    		<xsl:with-param name="clazz" select="'citationFunction_'"/>
					    		<xsl:with-param name="posParent" select="$posParent"/>
					    		<xsl:with-param name="posChild" select="$posChild"/>
					    		<xsl:with-param name="title" select="'eaccpf.portal.citation'"/>
					    	</xsl:call-template>
							<!-- dates in function-->
							<xsl:call-template name="commonDates">
					    		<xsl:with-param name="date" select="./eac:date"/>
					    		<xsl:with-param name="dateRange" select="./eac:dateRange"/>
					    		<xsl:with-param name="dateSet" select="./eac:dateSet"/>
					    	</xsl:call-template>
							<!-- descriptiveNote in function-->
							<xsl:call-template name="commonChild">
					    		<xsl:with-param name="list" select="./eac:descriptiveNote/eac:p"/>
					    		<xsl:with-param name="clazz" select="'descriptiveNoteFunction_'"/>
					    		<xsl:with-param name="posParent" select="$posParent"/>
					    		<xsl:with-param name="posChild" select="$posChild"/>
					    		<xsl:with-param name="title" select="'eaccpf.portal.note'"/>
					    	</xsl:call-template>
					</xsl:for-each>
						<!-- descriptiveNote in functions -->
						 <xsl:call-template name="commonChild">
				    		<xsl:with-param name="list" select="./eac:descriptiveNote/eac:p"/>
				    		<xsl:with-param name="clazz" select="'descriptiveNoteFunctions_'"/>
				    		<xsl:with-param name="posParent" select="$posParent"/>
				    		<xsl:with-param name="posChild" select="''"/>
				    		<xsl:with-param name="title" select="'eaccpf.portal.note'"/>
				    	 </xsl:call-template>
				</xsl:for-each>		
		    </xsl:if>
		    <!-- occupation -->
		    <xsl:if test="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:occupations/eac:occupation">
			    <h2 class="title"><xsl:value-of select="ape:resource('eaccpf.portal.occupation')"/></h2>
				<xsl:for-each select="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:occupations"> 
					<xsl:variable name="posParent" select="position()"/> 
				    <xsl:for-each select="./eac:occupation">
				    		<xsl:variable name="posChild" select="position()"/> 
				    		<!-- term function -->
					    	<xsl:call-template name="term">
					    		<xsl:with-param name="list" select="./eac:term"/>
					    		<xsl:with-param name="clazz" select="'occupation_'"/>
					    		<xsl:with-param name="posParent" select="$posParent"/>
					    		<xsl:with-param name="posChild" select="$posChild"/>
					    		<xsl:with-param name="title" select="'eaccpf.portal.occupation'"/>
					    	</xsl:call-template>
							<!-- placeEntry in function-->
							<xsl:call-template name="commonChild">
					    		<xsl:with-param name="list" select="./eac:placeEntry"/>
					    		<xsl:with-param name="clazz" select="'locationOccupation_'"/>
					    		<xsl:with-param name="posParent" select="$posParent"/>
					    		<xsl:with-param name="posChild" select="$posChild"/>
					    		<xsl:with-param name="title" select="'eaccpf.portal.location'"/>
					    	</xsl:call-template>
							<!-- citation in function -->
						  	<xsl:call-template name="commonChild">
					    		<xsl:with-param name="list" select="./eac:citation"/>
					    		<xsl:with-param name="clazz" select="'citationOccupation_'"/>
					    		<xsl:with-param name="posParent" select="$posParent"/>
					    		<xsl:with-param name="posChild" select="$posChild"/>
					    		<xsl:with-param name="title" select="'eaccpf.portal.citation'"/>
					    	</xsl:call-template>
							<!-- dates in function-->
							<xsl:call-template name="commonDates">
					    		<xsl:with-param name="date" select="./eac:date"/>
					    		<xsl:with-param name="dateRange" select="./eac:dateRange"/>
					    		<xsl:with-param name="dateSet" select="./eac:dateSet"/>
					    	</xsl:call-template>
							<!-- descriptiveNote in function-->
							<xsl:call-template name="commonChild">
					    		<xsl:with-param name="list" select="./eac:descriptiveNote/eac:p"/>
					    		<xsl:with-param name="clazz" select="'descriptiveNoteOccupation_'"/>
					    		<xsl:with-param name="posParent" select="$posParent"/>
					    		<xsl:with-param name="posChild" select="$posChild"/>
					    		<xsl:with-param name="title" select="'eaccpf.portal.note'"/>
					    	</xsl:call-template>
					</xsl:for-each>
						<!-- descriptiveNote in functions -->
						 <xsl:call-template name="commonChild">
				    		<xsl:with-param name="list" select="./eac:descriptiveNote/eac:p"/>
				    		<xsl:with-param name="clazz" select="'descriptiveNoteOccupations_'"/>
				    		<xsl:with-param name="posParent" select="$posParent"/>
				    		<xsl:with-param name="posChild" select="''"/>
				    		<xsl:with-param name="title" select="'eaccpf.portal.note'"/>
				    	 </xsl:call-template>
				</xsl:for-each>		
		    </xsl:if>
		    <!--mandates-->
		    <xsl:if test="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:mandates/eac:mandate">
			    <h2 class="title"><xsl:value-of select="ape:resource('eaccpf.portal.mandate')"/></h2>
				<xsl:for-each select="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:mandates">
					<xsl:variable name="posParent" select="position()"/>  
				    <xsl:for-each select="./eac:mandate">
				    		<xsl:variable name="posChild" select="position()"/> 
				    		<!-- term function -->
					    	<xsl:call-template name="term">
					    		<xsl:with-param name="list" select="./eac:term"/>
					    		<xsl:with-param name="clazz" select="'mandate_'"/>
					    		<xsl:with-param name="posParent" select="$posParent"/>
					    		<xsl:with-param name="posChild" select="$posChild"/>
					    		<xsl:with-param name="title" select="'eaccpf.portal.mandate'"/>
					    	</xsl:call-template>
							<!-- placeEntry in function-->
							<xsl:call-template name="commonChild">
					    		<xsl:with-param name="list" select="./eac:placeEntry"/>
					    		<xsl:with-param name="clazz" select="'locationMandate_'"/>
					    		<xsl:with-param name="posParent" select="$posParent"/>
					    		<xsl:with-param name="posChild" select="$posChild"/>
					    		<xsl:with-param name="title" select="'eaccpf.portal.location'"/>
					    	</xsl:call-template>
							<!-- citation in function -->
						  	<xsl:call-template name="commonChild">
					    		<xsl:with-param name="list" select="./eac:citation"/>
					    		<xsl:with-param name="clazz" select="'citationMandate_'"/>
					    		<xsl:with-param name="posParent" select="$posParent"/>
					    		<xsl:with-param name="posChild" select="$posChild"/>
					    		<xsl:with-param name="title" select="'eaccpf.portal.citation'"/>
					    	</xsl:call-template>
							<!-- dates in function-->
							<xsl:call-template name="commonDates">
					    		<xsl:with-param name="date" select="./eac:date"/>
					    		<xsl:with-param name="dateRange" select="./eac:dateRange"/>
					    		<xsl:with-param name="dateSet" select="./eac:dateSet"/>
					    	</xsl:call-template>
							<!-- descriptiveNote in function-->
							<xsl:call-template name="commonChild">
					    		<xsl:with-param name="list" select="./eac:descriptiveNote/eac:p"/>
					    		<xsl:with-param name="clazz" select="'descriptiveNoteMandate_'"/>
					    		<xsl:with-param name="posParent" select="$posParent"/>
					    		<xsl:with-param name="posChild" select="$posChild"/>
					    		<xsl:with-param name="title" select="'eaccpf.portal.note'"/>
					    	</xsl:call-template>
					</xsl:for-each>
						<!-- descriptiveNote in functions -->
						 <xsl:call-template name="commonChild">
				    		<xsl:with-param name="list" select="./eac:descriptiveNote/eac:p"/>
				    		<xsl:with-param name="clazz" select="'descriptiveNoteMandates_'"/>
				    		<xsl:with-param name="posParent" select="$posParent"/>
				    		<xsl:with-param name="posChild" select="''"/>
				    		<xsl:with-param name="title" select="'eaccpf.portal.note'"/>
				    	 </xsl:call-template>
				</xsl:for-each>		
		    </xsl:if>
			<!--languagesUsed -->
			<xsl:if test="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:languagesUsed/eac:languageUsed/eac:language/text()">   
				<div class="row">
						<div class="leftcolumn">
					   		<h2><xsl:value-of select="ape:resource('eaccpf.portal.languagesUsed')"/><xsl:text>:</xsl:text></h2>
					   	</div>
					   	<div class="rightcolumn">
							<xsl:call-template name="multilanguage">
					   			<xsl:with-param name="list" select="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:languagesUsed/eac:languageUsed/eac:language"/>
					   			<xsl:with-param name="clazz" select="'language'"/>
					   		</xsl:call-template>
						</div>
				</div>
			</xsl:if> 
			<!-- script -->
			<xsl:if test="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:languagesUsed/eac:languageUsed/eac:script/text()">   
				<div class="row">
						<div class="leftcolumn">
					   		<h2><xsl:value-of select="ape:resource('eaccpf.portal.scriptUsed')"/><xsl:text>:</xsl:text></h2>
					   	</div>
					   	<div class="rightcolumn">
							<xsl:call-template name="multilanguage">
					   			<xsl:with-param name="list" select="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:languagesUsed/eac:languageUsed/eac:script"/>
					   			<xsl:with-param name="clazz" select="'script'"/>
					   		</xsl:call-template>
						</div>
				</div>
			</xsl:if> 
			<!-- structureOrGenealogy -->
			<xsl:if test="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:structureOrGenealogy">
				<h2 class="title"><xsl:value-of select="ape:resource('eaccpf.portal.structureOrGenealogy')"/></h2>
			    <xsl:if test="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:structureOrGenealogy/eac:outline/eac:level/eac:item/text()"> 
			     	<div class="row subrow">
						<div class="leftcolumn">
					   		<h2><xsl:value-of select="ape:resource('eaccpf.portal.structureOrGenealogy')"/><xsl:text>:</xsl:text></h2>
					   	</div>
					   	<div class="rightcolumn moreDisplay" id="structureOrGenealogy">
					   		<xsl:call-template name="multilanguageOutline">
					   			<xsl:with-param name="list" select="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:structureOrGenealogy/eac:outline/eac:level"/>
					   		  	<xsl:with-param name="clazz" select="'structureOrGenealogy'"/>
					   		  	<xsl:with-param name="count" select="1"/>  <!-- count the number of white spaces to display in the tree -->
					   		</xsl:call-template>
					   		<div class="linkMore">
								<a class="displayLinkShowMore linkShow" href="javascript:showMore('structureOrGenealogy', 'pre');">
									<xsl:value-of select="ape:resource('eaccpf.portal.showmore')"/>
								</a>
								<a class="displayLinkShowLess linkShow" href="javascript:showLess('structureOrGenealogy', 'pre');">
									<xsl:value-of select="ape:resource('eaccpf.portal.showless')"/>
								</a>
							</div>
						</div>
					</div>
			    </xsl:if>
			    <xsl:if test="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:structureOrGenealogy/eac:p/text()">
			    	<div class="row subrow">
							<div class="leftcolumn">
						   		<h2><xsl:value-of select="ape:resource('eaccpf.portal.note')"/><xsl:text>:</xsl:text></h2>
						   	</div>
						   	<div class="rightcolumn">
								<xsl:call-template name="multilanguage">
						   			<xsl:with-param name="list" select="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:structureOrGenealogy/eac:p"/>
						   			<xsl:with-param name="clazz" select="'structureOrGenealogyNote'"/>
						   		</xsl:call-template>
							</div>
					</div>
			    </xsl:if>
			</xsl:if> 
			<!-- generalContext -->
			<xsl:if test="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:generalContext">
			  	<h2 class="title"><xsl:value-of select="ape:resource('eaccpf.portal.generalContext')"/></h2> 
			  	<xsl:if test="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:generalContext/eac:outline/eac:level/eac:item/text()">
			  		<div class="row subrow">
						<div class="leftcolumn">
					   		<h2><xsl:value-of select="ape:resource('eaccpf.portal.generalContext')"/><xsl:text>:</xsl:text></h2>
					   	</div>
					   	<div class="rightcolumn moreDisplay" id="structureOrGenealogy">
					   		<xsl:call-template name="multilanguageOutline">
					   			<xsl:with-param name="list" select="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:generalContext/eac:outline/eac:level"/>
					   		  	<xsl:with-param name="clazz" select="'generalContext'"/>
					   		  	<xsl:with-param name="count" select="1"/> 
					   		</xsl:call-template>
					   		<div class="linkMore">
								<a class="displayLinkShowMore linkShow" href="javascript:showMore('structureOrGenealogy', 'pre');">
									<xsl:value-of select="ape:resource('eaccpf.portal.showmore')"/>
								</a>
								<a class="displayLinkShowLess linkShow" href="javascript:showLess('structureOrGenealogy', 'pre');">
									<xsl:value-of select="ape:resource('eaccpf.portal.showless')"/>
								</a>
							</div>
						</div>
					</div>
			  	</xsl:if>
				<xsl:if test="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:generalContext/eac:p/text()">
					<div class="row subrow">
							<div class="leftcolumn">
						   		<h2><xsl:value-of select="ape:resource('eaccpf.portal.note')"/><xsl:text>:</xsl:text></h2>
						   	</div>
						   	<div class="rightcolumn">
								<xsl:call-template name="multilanguage">
						   			<xsl:with-param name="list" select="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:generalContext/eac:p"/>
						   			<xsl:with-param name="clazz" select="'generalContextNote'"/>
						   		</xsl:call-template>
							</div>
					</div>
				</xsl:if>
			</xsl:if> 
			<!-- biogHist --> 
			<xsl:if test="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:biogHist/eac:p/text()">   
				<div class="row">
						<div class="leftcolumn">
					   		<h2>
					   			<xsl:if test="$entityType='corporateBody'">
					   				<xsl:value-of select="ape:resource('eaccpf.portal.historicalNote')"/>
					   			</xsl:if>
					   			<xsl:if test="$entityType='person' or $entityType='family'">
					   				<xsl:value-of select="ape:resource('eaccpf.portal.biogHist')"/>
					   			</xsl:if>
					   			<xsl:text>:</xsl:text>
					   		</h2>
					   	</div>
					   	<div class="rightcolumn">
							<xsl:call-template name="multilanguage">
					   			<xsl:with-param name="list" select="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:biogHist/eac:p"/>
					   			<xsl:with-param name="clazz" select="'biogist'"/>
					   		</xsl:call-template>
						</div>
				</div>
			</xsl:if>
			<!-- provided by -->
			<xsl:if test="./eac:eac-cpf/eac:control/eac:maintenanceAgency/eac:agencyName/text()">
				<div class="row">
						<div class="leftcolumn">
					   		<h2><xsl:value-of select="ape:resource('eaccpf.portal.agencyName')"/><xsl:text>:</xsl:text></h2>
					   	</div>
					   	<div class="rightcolumn">
							<xsl:choose>
								<xsl:when test="./eac:eac-cpf/eac:control/eac:maintenanceAgency/eac:agencyCode/text()">
									<xsl:variable name="href" select="./eac:eac-cpf/eac:control/eac:maintenanceAgency/eac:agencyCode"/>
									<xsl:if test="starts-with($href, 'http') or starts-with($href, 'https') or starts-with($href, 'ftp') or starts-with($href, 'www')">
										<a href="{$href}" target="_blank">
											<xsl:apply-templates select="./eac:eac-cpf/eac:control/eac:maintenanceAgency/eac:agencyName" mode="other"/>
										</a>
							  		</xsl:if>
							  		<xsl:if test="not(starts-with($href, 'http')) and not(starts-with($href, 'https')) and not(starts-with($href, 'ftp')) and not(starts-with($href, 'www'))">
							  			<xsl:variable name="aiCode" select="ape:checkAICode($href, '', 'true')" />
							  			<xsl:choose>
							  				<xsl:when test="$aiCode != 'ERROR' and $aiCode != ''">
												<xsl:variable name="encodedAiCode" select="ape:encodeSpecialCharacters($aiCode)" />
												<a href="{$aiCodeUrl}/{$encodedAiCode}" target="_blank">
													<xsl:apply-templates select="./eac:eac-cpf/eac:control/eac:maintenanceAgency/eac:agencyName" mode="other"/>
												</a>
							  				</xsl:when>
							  				<xsl:otherwise>
							  					<xsl:apply-templates select="./eac:eac-cpf/eac:control/eac:maintenanceAgency/eac:agencyName" mode="other"/>
							  				</xsl:otherwise>
							  			</xsl:choose>
									</xsl:if>
								</xsl:when>
								<xsl:otherwise>
									<xsl:apply-templates select="./eac:eac-cpf/eac:control/eac:maintenanceAgency/eac:agencyName" mode="other"/>
								</xsl:otherwise>
							</xsl:choose>
						</div>
				</div>
			</xsl:if>
			<!-- other information -->
			<div id="footer">
				<h2 class="otherInformation"><xsl:value-of select="ape:resource('eaccpf.portal.otherInformation')"/></h2>
					<!-- identifier-->
				<xsl:if test="./eac:eac-cpf/eac:control/eac:recordId/text()">  	
					<div class="row grey">
						<div class="leftcolumn">
							<h2 class="grey"><xsl:value-of select="ape:resource('eaccpf.portal.identifier')"/><xsl:text>:</xsl:text></h2>
						</div>
						<div class="rightcolumn"> 
							<xsl:apply-templates select="./eac:eac-cpf/eac:control/eac:recordId" mode="other"/>
						</div>
					</div>
				</xsl:if>
				<!-- other identifier -->
				<xsl:if test="./eac:eac-cpf/eac:cpfDescription/eac:identity/eac:entityId/text()">  
					<div class="row grey">
						<div class="leftcolumn">
							<h2 class="grey"><xsl:value-of select="ape:resource('eaccpf.portal.otherIdentifier')"/><xsl:text>:</xsl:text></h2>
						</div>
						<div class="rightcolumn moreDisplay" id="otherIdentifier"> 
							<xsl:for-each select="./eac:eac-cpf/eac:cpfDescription/eac:identity/eac:entityId">
								<xsl:if test="./text()">
									   	<xsl:choose>
											<xsl:when test="@localType!=''">
												<p>
													<xsl:variable name="href" select="./@localType"/>
													<a href="{$href}" target="_blank"><xsl:apply-templates select="." mode="other"/></a>
												</p>
											</xsl:when>
											<xsl:otherwise>
												<p>
													<xsl:apply-templates select="." mode="other"/> 
												</p>
											</xsl:otherwise>
										</xsl:choose>
								   
								 </xsl:if>  
							</xsl:for-each>
							<div class="linkMore">
								<a class="displayLinkShowMore linkShow" href="javascript:showMore('otherIdentifier', 'p');">
									<xsl:value-of select="ape:resource('eaccpf.portal.showmore')"/>
								</a>
								<a class="displayLinkShowLess linkShow" href="javascript:showLess('otherIdentifier', 'p');">
									<xsl:value-of select="ape:resource('eaccpf.portal.showless')"/>
								</a>
							</div>
						</div>
					</div>
				</xsl:if>
				<!-- last update -->
				<xsl:if test="./eac:eac-cpf/eac:control/eac:maintenanceHistory/eac:maintenanceEvent/eac:eventDateTime/text()">     
					<div class="row grey">
						<div class="leftcolumn">
							<h2 class="grey"><xsl:value-of select="ape:resource('eaccpf.portal.eventDate')"/><xsl:text>:</xsl:text></h2>
						</div>
						<div class="rightcolumn"> 
							<xsl:variable name="lastMaintenance" select="./eac:eac-cpf/eac:control/eac:maintenanceHistory/eac:maintenanceEvent[last()]"/>		
							<xsl:apply-templates select="$lastMaintenance/eac:eventDateTime" mode="other"/>
						</div>
					</div>	
				</xsl:if>
			</div>
	</div>
	<!-- Relations boxes-->
	<div id="relations">
		<!-- Alternative descriptions. -->
		<xsl:if test="./eac:eac-cpf/eac:cpfDescription/eac:alternativeSet/eac:setComponent/eac:componentEntry/text()">
			<xsl:call-template name="alternativeDescriptions"/>
		</xsl:if>

		<!-- Archival materials. -->
		<xsl:if test="./eac:eac-cpf/eac:cpfDescription/eac:relations/eac:resourceRelation/eac:relationEntry/text()">
			<xsl:call-template name="archivalMaterials"/>
		</xsl:if>

		<!-- Related names. -->
		<xsl:if test="./eac:eac-cpf/eac:cpfDescription/eac:relations/eac:cpfRelation/eac:relationEntry/text()">
			<xsl:call-template name="relatedNames"/>
		</xsl:if>

		<!-- Archives. -->
		<xsl:if test="./eac:eac-cpf/eac:cpfDescription/eac:relations/eac:resourceRelation/eac:relationEntry/@localType='agencyName'">
			<xsl:call-template name="archivesRelations"/>
		</xsl:if>	
	</div>
	</xsl:template>
	
	<!-- Template for alternative forms of name -->
	<xsl:template name="alternativeName">
		<xsl:param name="list"/>
		<xsl:param name="clazz"/>
		<div id="{$clazz}" class= "moreDisplay">
			<xsl:for-each select="$list">
				<p>
					<xsl:call-template name="compositeName">
						<xsl:with-param name="listName" select="current()"/>
						<xsl:with-param name="isHeader" select="'false'"/>
					</xsl:call-template> 
			    </p>
			</xsl:for-each>
			<div class="linkMore">
				<a class="displayLinkShowMore linkShow" href="javascript:showMore('{$clazz}', 'p');">
					<xsl:value-of select="ape:resource('eaccpf.portal.showmore')"/>
				</a>
				<a class="displayLinkShowLess linkShow" href="javascript:showLess('{$clazz}', 'p');">
					<xsl:value-of select="ape:resource('eaccpf.portal.showless')"/>
				</a>
			</div>	
		</div>
	</xsl:template>
	<!-- template places -->
	<xsl:template name="places">
		<xsl:param name="list"/>
	    <xsl:param name="clazz"/>
	    <xsl:param name="posParent"/>
	    <xsl:param name="posChild"/>
	    <xsl:param name="title"/> 
		<xsl:if test="($list[@localType != 'foundation']/text() and $list[@localType != 'suppression']/text() and $list[@localType != 'birth']/text() and $list[@localType != 'death']/text()) or $list[not(@localType)]/text()">
			<div class="row subrow locationPlace">
				 	<div class="leftcolumn">
				   		<h2><xsl:value-of select="ape:resource($title)"/><xsl:text>:</xsl:text></h2>
				   	</div> 
				    <div class="rightcolumn"> 
						<xsl:call-template name="multilanguagePlaceEntrySeveral">
			   				<xsl:with-param name="list" select="$list"/>
			   				<xsl:with-param name="clazz" select="$clazz"/>
			   				<xsl:with-param name="posParent" select="$posParent"/>
			   				<xsl:with-param name="posChild" select="$posChild"/>
			   			</xsl:call-template>
				 	</div> 
			</div>
		</xsl:if>
	</xsl:template>
	<!-- template term -->
	<xsl:template name="term">
		<xsl:param name="list"/>
		<xsl:param name="clazz"/>
		<xsl:param name="posParent"/>
		<xsl:param name="posChild"/>
		<xsl:param name="title"/>
		<xsl:if test="$list/text()">
			<div class="row subrow">
					<div class="leftcolumn">
				   		<h2><xsl:value-of select="ape:resource($title)"/>
				   			<xsl:if test="$list/parent::node()/@localType">
					   			<xsl:text> (</xsl:text>
					   			<xsl:value-of select="$list/parent::node()/@localType"/>
					   			<xsl:text>)</xsl:text>
					   		</xsl:if>	
				   			<xsl:text>:</xsl:text>
				   		</h2>
				   	</div>
				   	<div class="rightcolumn">
						<xsl:call-template name="multilanguageWithVocabularySource">
			   				<xsl:with-param name="list" select="$list"/>
			   				<xsl:with-param name="clazz" select="$clazz"/>
			   				<xsl:with-param name="posParent" select="$posParent"/>
			   				<xsl:with-param name="posChild" select="$posChild"/>
			   			</xsl:call-template>
					</div>
			</div>
		</xsl:if>
	</xsl:template>
	
	<!--template for common childs -->
	<xsl:template name="commonChild">
		<xsl:param name="list"/>
	    <xsl:param name="clazz"/>
	    <xsl:param name="posParent"/>
	    <xsl:param name="posChild"/>
	    <xsl:param name="title"/>
		<xsl:if test="$list/text()">	 
			<div class="row subrow">
					<div class="leftcolumn">
				   		<h2><xsl:value-of select="ape:resource($title)"/><xsl:text>:</xsl:text></h2>
				   	</div>
				   	<div class="rightcolumn">
						<xsl:call-template name="multilanguageWithVocabularySource">
			   				<xsl:with-param name="list" select="$list"/>
			   				<xsl:with-param name="clazz" select="$clazz"/>
			   				<xsl:with-param name="posParent" select="$posParent"/>
			   				<xsl:with-param name="posChild" select="$posChild"/>
			   			</xsl:call-template>
					</div>
			</div>
		</xsl:if>
	</xsl:template>
	
	<!-- template for commons dates -->
	<xsl:template name="commonDates">
		<xsl:param name="date"/>
		<xsl:param name="dateRange"/>
		<xsl:param name="dateSet"/>
		<xsl:if test="$date or $dateRange or $dateSet">
		    <div class="row subrow">
		    	<div class="leftcolumn">
				   		<h2><xsl:value-of select="ape:resource('eaccpf.portal.date')"/><xsl:text>:</xsl:text></h2>
				</div>          
		        <div class="rightcolumn">
		        	<span class="nameEtryDates">
						<!-- when there are only 1 dateSet -->
						<xsl:if test="$dateSet and (($dateSet/eac:dateRange/eac:fromDate or $dateSet/eac:dateRange/eac:toDate) or ($dateSet/eac:date and $dateSet/eac:date/text()))">
							<xsl:apply-templates select="$dateSet"/>
						</xsl:if>
						<!-- when there are only 1 dateRange -->
						<xsl:if test="$dateRange and ($dateRange/eac:fromDate or $dateRange/eac:toDate)">
							<xsl:apply-templates select="$dateRange"/>
						</xsl:if>
						<!-- when there are only 1 date -->
						<xsl:if test="$date and $date/text()">
							<xsl:apply-templates select="$date"/>
						</xsl:if>
					</span>
				</div> 
			</div>
		</xsl:if>
	</xsl:template>

	<!-- template for language nameEntry -->
	<xsl:template name="multilanguageName">
		<xsl:param name="list"/>
			<xsl:choose>
				<xsl:when test="count($list) > 1">
					<xsl:choose>
						<xsl:when test="$list/eac:part[@xml:lang = $language.selected] and $list/eac:part[@xml:lang = $language.selected]/text() and $list/eac:part[@xml:lang = $language.selected]/text() != ''">
						    <xsl:for-each select="$list/eac:part[@xml:lang = $language.selected]">
								<xsl:if test="position()=1"> 
										<xsl:call-template name="compositeName">
											<xsl:with-param name="listName" select="current()/parent::node()"/>
											<xsl:with-param name="isHeader" select="'true'"/>
										</xsl:call-template> 
							 	</xsl:if> 
					 	    </xsl:for-each> 
						</xsl:when>
						<xsl:when test="$list/eac:part[@xml:lang = $language.default] and $list/eac:part[@xml:lang = $language.default]/text() and $list/eac:part[@xml:lang = $language.default]/text() != ''">
						 	<xsl:for-each select="$list/eac:part[@xml:lang = $language.default]"> 
								  <xsl:if test="position()=1">
										<xsl:call-template name="compositeName"> 
											<xsl:with-param name="listName" select="current()/parent::node()"/>
											<xsl:with-param name="isHeader" select="'true'"/>
										</xsl:call-template> 
								 </xsl:if>
						  	</xsl:for-each>
						</xsl:when>
						<xsl:when test="$list/eac:part[not(@xml:lang)] and $list/eac:part[not(@xml:lang)]/text() and $list/eac:part[not(@xml:lang)]/text() != ''">
						  	<xsl:for-each select="$list/eac:part[not(@xml:lang)]"> 
							 	  <xsl:if test="position()=1"> 
									 <xsl:call-template name="compositeName">
												<xsl:with-param name="listName" select="current()/parent::node()"/>
												<xsl:with-param name="isHeader" select="'true'"/>
									 </xsl:call-template> 
							  	 </xsl:if>
						   	</xsl:for-each> 
						</xsl:when>
						<xsl:otherwise> 
						 	<xsl:variable name="language.first" select="$list[1]/eac:part[@xml:lang]"></xsl:variable>
							<xsl:for-each select="$list">
								<xsl:variable name="currentLang" select="current()/eac:part[@xml:lang]"></xsl:variable>
								<xsl:variable name="currentNode" select="current()/eac:part"></xsl:variable>
										<xsl:if test="$currentLang = $language.first">
											<xsl:if test="position() = 1">
												<xsl:call-template name="compositeName">
													<xsl:with-param name="listName" select="$currentNode/parent::node()"/>
													<xsl:with-param name="isHeader" select="'true'"/>
										   		</xsl:call-template> 
										  	</xsl:if>
										</xsl:if>
							</xsl:for-each>
					   </xsl:otherwise>
					</xsl:choose>
				</xsl:when>
				<xsl:otherwise>
					<xsl:for-each select="$list">
							<xsl:call-template name="compositeName">
								<xsl:with-param name="listName" select="current()"/>
								<xsl:with-param name="isHeader" select="'true'"/>
							</xsl:call-template> 
					</xsl:for-each>
				</xsl:otherwise>
			</xsl:choose>
	</xsl:template>

	<!-- template for compose the name -->
	<xsl:template name="compositeName">
		<xsl:param name="listName"/>
		<xsl:param name="isHeader"/>
		<xsl:variable name="firstName" select="$listName/eac:part[@localType='firstname']"/>
		<xsl:variable name="surName" select="$listName/eac:part[@localType='surname']"/>
		<xsl:variable name="patronymic" select="$listName/eac:part[@localType='patronymic']"/>
	<!--  	<xsl:variable name="prefix" select="$listName/eac:part[@localType='prefix']"/>-->
	    <xsl:choose>
	    	<xsl:when test="$surName and $firstName and $patronymic">
	    		<xsl:value-of select="$surName"/>
	    		<xsl:text>, </xsl:text>
	    		<xsl:value-of select="$firstName"/>
	    		<xsl:text> </xsl:text>
	    		<xsl:value-of select="$patronymic"/>
	    		<xsl:if test="$isHeader = 'true'">
	    			<!-- <input id="NameTitle" type="hidden" value="$surName, $firstName $patronymic"/> -->
	    			<span id="nameTitle" hidden="">
			    		<xsl:value-of select="$surName"/>
			    		<xsl:text>, </xsl:text>
			    		<xsl:value-of select="$firstName"/>
			    		<xsl:text> </xsl:text>
			    		<xsl:value-of select="$patronymic"/>
	    			</span>
	    		</xsl:if>
	    	</xsl:when>
	    	<xsl:when test="$surName and $firstName">
	    		<xsl:value-of select="$surName"/>
	    		<xsl:text>, </xsl:text>
	    		<xsl:value-of select="$firstName"/>
	    		<xsl:if test="$isHeader = 'true'">
	    			<!-- <input id="NameTitle" type="hidden" value="surName, $firstName"/> -->
	    			<span id="nameTitle" hidden="">
			    		<xsl:value-of select="$surName"/>
			    		<xsl:text>, </xsl:text>
			    		<xsl:value-of select="$firstName"/>
			    	</span>
	    		</xsl:if>
	    	</xsl:when>
	    	<xsl:when test="$surName and $patronymic">
	    		<xsl:value-of select="$surName"/>
	    		<xsl:text> </xsl:text>
	    		<xsl:value-of select="$patronymic"/>
	    		<xsl:if test="$isHeader = 'true'">
	    			<!-- <input id="NameTitle" type="hidden" value="$surName $patronymic"/> -->
	    			<span id="nameTitle" hidden="">
			    		<xsl:value-of select="$surName"/>
			    		<xsl:text> </xsl:text>
			    		<xsl:value-of select="$patronymic"/>
	    			</span>
	    		</xsl:if>
	    	</xsl:when>
	    	<xsl:when test="$firstName and $patronymic">
	    		<xsl:value-of select="$firstName"/>
	    		<xsl:text> </xsl:text>
	    		<xsl:value-of select="$patronymic"/>
	    		<xsl:if test="$isHeader = 'true'">
	    			<!-- <input id="NameTitle" type="hidden" value="$firstName $patronymic"/> -->
	    			<span id="nameTitle" hidden="">
			    		<xsl:value-of select="$firstName"/>
			    		<xsl:text> </xsl:text>
			    		<xsl:value-of select="$patronymic"/>
			    	</span>
	    		</xsl:if>
	    	</xsl:when>
	    	<xsl:otherwise>
	    		<xsl:value-of select="$listName/eac:part"/>
	    		<xsl:if test="$isHeader = 'true'">
	    			<!-- <input id="NameTitle" type="hidden" value="$listName/eac:part"/> -->
	    			<span id="nameTitle" hidden=""><xsl:value-of select="$listName/eac:part"/></span>
	    		</xsl:if>
	    	</xsl:otherwise>
	    </xsl:choose>
	</xsl:template> 

	<!-- template for nodes with attribute @vocabularySource or not and several elements-->
	<xsl:template name="multilanguageWithVocabularySource">
		<xsl:param name="list"/>
		<xsl:param name="clazz"/>
		<xsl:param name="posParent"/>
		<xsl:param name="posChild"/>
		<xsl:choose>
			<xsl:when test="count($list) > 1">
			<div id="{$clazz}{$posParent}{$posChild}" class= "moreDisplay">
				<xsl:choose>
					<xsl:when test="$list[@xml:lang = $language.selected] and $list[@xml:lang = $language.selected]/text() and $list[@xml:lang = $language.selected]/text() != ''">
						<xsl:for-each select="$list[@xml:lang = $language.selected]">
							<p>
								<xsl:choose>
									<xsl:when test="@vocabularySource">
										<xsl:variable name="href" select="./@vocabularySource"/>
										<a href="{$href}" target="_blank"><xsl:apply-templates select="." mode="other"/></a>
									</xsl:when>
									<xsl:otherwise>
										<xsl:apply-templates select="." mode="other"/> 
									</xsl:otherwise>
								</xsl:choose>
							</p>	
						</xsl:for-each>
					</xsl:when>
					<xsl:when test="$list[@xml:lang = $language.default] and $list[@xml:lang = $language.default]/text() and $list[@xml:lang = $language.default]/text() != ''">
						<xsl:for-each select="$list[@xml:lang = $language.default]">
							<p>
								<xsl:choose>
									<xsl:when test="@vocabularySource">
										<xsl:variable name="href" select="./@vocabularySource"/>
										<a href="{$href}" target="_blank"><xsl:apply-templates select="." mode="other"/></a>
									</xsl:when>
									<xsl:otherwise>
									 <xsl:apply-templates select="." mode="other"/> 
									</xsl:otherwise>
								</xsl:choose>
							</p>	
						</xsl:for-each>
					</xsl:when>
					<xsl:when test="$list[not(@xml:lang)] and $list[not(@xml:lang)]/text() and $list[not(@xml:lang)]/text() != ''">
					  	<xsl:for-each select="$list[not(@xml:lang)]">
						 	  <p>
								 <xsl:choose>
									<xsl:when test="@vocabularySource">
										<xsl:variable name="href" select="./@vocabularySource"/>
										<a href="{$href}" target="_blank"><xsl:apply-templates select="." mode="other"/></a>
									</xsl:when>
									<xsl:otherwise>
									 <xsl:apply-templates select="." mode="other"/> 
									</xsl:otherwise>
								</xsl:choose>
							  </p>   
					   	</xsl:for-each> 
					</xsl:when>
					<xsl:otherwise>
						<xsl:variable name="language.first" select="$list[1]/@xml:lang"></xsl:variable>
						<xsl:for-each select="$list">
							<xsl:variable name="currentLang" select="current()/@xml:lang"></xsl:variable>
							<xsl:variable name="currentText" select="current()/text()"></xsl:variable>
									<xsl:if test="$currentLang = $language.first">
										<p>
											<xsl:choose>
												<xsl:when test="@vocabularySource">
													<xsl:variable name="href" select="./@vocabularySource"/>
													<a href="{$href}" target="_blank"><xsl:apply-templates select="$currentText" mode="other"/></a>
												</xsl:when>
												<xsl:otherwise>
												 	<xsl:apply-templates select="$currentText" mode="other"/> 
												</xsl:otherwise>
											</xsl:choose>
										</p>
									</xsl:if>
						</xsl:for-each>
					</xsl:otherwise>
				</xsl:choose>
				<div class="linkMore">
					<a class="displayLinkShowMore linkShow" href="javascript:showMore('{$clazz}{$posParent}{$posChild}', 'p');">
						<xsl:value-of select="ape:resource('eaccpf.portal.showmore')"/>
					</a>
					<a class="displayLinkShowLess linkShow" href="javascript:showLess('{$clazz}{$posParent}{$posChild}', 'p');">
						<xsl:value-of select="ape:resource('eaccpf.portal.showless')"/>
					</a>
				</div>
			</div>
			</xsl:when>
			<xsl:otherwise>
				<xsl:for-each select="$list">
					<xsl:choose>
						<xsl:when test="@vocabularySource">
							<xsl:variable name="href" select="./@vocabularySource"/>
							<a href="{$href}" target="_blank"><xsl:apply-templates select="." mode="other"/></a>
						</xsl:when>
						<xsl:otherwise>
						 	<xsl:apply-templates select="." mode="other"/> 
						</xsl:otherwise>
					</xsl:choose>
				</xsl:for-each>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<!-- template for language only one element -->
	<xsl:template name="multilanguagePlaceEntry">
		<xsl:param name="list"/>
		<xsl:choose>
			<xsl:when test="count($list) > 1">
				<xsl:choose>
					<xsl:when test="$list[@xml:lang = $language.selected] and $list[@xml:lang = $language.selected]/text() and $list[@xml:lang = $language.selected]/text() != ''">
						<xsl:for-each select="$list[@xml:lang = $language.selected]">
							<xsl:if test="position() = 1">
								<xsl:choose>
									<xsl:when test="$list[@vocabularySource]">
										<xsl:variable name="href" select="$list/@vocabularySource"/>
										<a href="{$href}" target="_blank"><xsl:apply-templates select="." mode="other"/></a>
									</xsl:when>
									<xsl:otherwise>
										<xsl:apply-templates select="." mode="other"/> 
									</xsl:otherwise>
								</xsl:choose>	
							</xsl:if>
						</xsl:for-each>
					</xsl:when>
					<xsl:when test="$list[@xml:lang = $language.default] and $list[@xml:lang = $language.default]/text() and $list[@xml:lang = $language.default]/text() != ''">
						<xsl:for-each select="$list[@xml:lang = $language.default]">
							<xsl:if test="position() = 1">
								<xsl:choose>
									<xsl:when test="$list[@vocabularySource]">
										<xsl:variable name="href" select="$list/@vocabularySource"/>
										<a href="{$href}" target="_blank"><xsl:apply-templates select="." mode="other"/></a>
									</xsl:when>
									<xsl:otherwise>
									 <xsl:apply-templates select="." mode="other"/> 
									</xsl:otherwise>
								</xsl:choose>
							</xsl:if>
						</xsl:for-each>
					</xsl:when>
					<xsl:when test="$list[not(@xml:lang)] and $list[not(@xml:lang)]/text() and $list[not(@xml:lang)]/text() != ''">
						  	<xsl:for-each select="$list[not(@xml:lang)]"> 
							 	  <xsl:if test="position()=1"> 
									 <xsl:choose>
										<xsl:when test="$list[@vocabularySource]">
											<xsl:variable name="href" select="$list/@vocabularySource"/>
											<a href="{$href}" target="_blank"><xsl:apply-templates select="." mode="other"/></a>
										</xsl:when>
										<xsl:otherwise>
										 <xsl:apply-templates select="." mode="other"/> 
										</xsl:otherwise>
									</xsl:choose>
							  	 </xsl:if>
						   	</xsl:for-each> 
						</xsl:when>
					<xsl:otherwise>
						<xsl:variable name="language.first" select="$list[1]/@xml:lang"></xsl:variable>
						<xsl:for-each select="$list">
							<xsl:variable name="currentLang" select="current()/@xml:lang"></xsl:variable>
							<xsl:variable name="currentText" select="current()/text()"></xsl:variable>
									<xsl:if test="$currentLang = $language.first">
										<xsl:if test="position() = 1">
											<xsl:choose>
												<xsl:when test="$list[@vocabularySource]">
													<xsl:variable name="href" select="$list/@vocabularySource"/>
													<a href="{$href}" target="_blank"><xsl:apply-templates select="$currentText" mode="other"/></a>
												</xsl:when>
												<xsl:otherwise>
												 <xsl:apply-templates select="$currentText" mode="other"/> 
												</xsl:otherwise>
											</xsl:choose>
										</xsl:if>
									</xsl:if>
						</xsl:for-each>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
			<xsl:otherwise>
				<xsl:for-each select="$list">
					<xsl:choose>
						<xsl:when test="$list[@vocabularySource]">
							<xsl:variable name="href" select="$list/@vocabularySource"/>
							<a href="{$href}" target="_blank"><xsl:apply-templates select="." mode="other"/></a>
						</xsl:when>
						<xsl:otherwise>
						 <xsl:apply-templates select="." mode="other"/> 
						</xsl:otherwise>
					</xsl:choose>
				</xsl:for-each>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	
	<!-- template for several elements placeEntry in places/place/placeEntry-->
	<xsl:template name="multilanguagePlaceEntrySeveral">
		<xsl:param name="list"/>
		<xsl:param name="clazz"/>
		<xsl:param name="posParent"/>
		<xsl:param name="posChild"/>
		<xsl:choose>
			<xsl:when test="count($list) > 1">
			<div id="{$clazz}{$posParent}{$posChild}" class= "moreDisplay">
				<xsl:choose>
					<xsl:when test="$list[@xml:lang = $language.selected] and $list[@xml:lang = $language.selected]/text() and $list[@xml:lang = $language.selected]/text() != ''">
						<xsl:for-each select="$list[@xml:lang = $language.selected and (not(@localType) or (@localType != 'birth' and @localType != 'death' and @localType != 'foundation' and @localType != 'suppression'))]">
							<p>
								<xsl:choose>
									<xsl:when test="@vocabularySource">
										<xsl:variable name="href" select="./@vocabularySource"/>
										<a href="{$href}" target="_blank"><xsl:apply-templates select="." mode="other"/></a>
									</xsl:when>
									<xsl:otherwise>
										<xsl:apply-templates select="." mode="other"/> 
									</xsl:otherwise>
								</xsl:choose>
							</p>
						</xsl:for-each>
					</xsl:when>
					<xsl:when test="($list[@xml:lang = $language.default] and $list[@xml:lang = $language.default]/text() and $list[@xml:lang = $language.default]/text() != '')">
						<xsl:for-each select="$list[@xml:lang = $language.default and (not(@localType) or (@localType != 'birth' and @localType != 'death' and @localType != 'foundation' and @localType != 'suppression'))]">
							<p>
								<xsl:choose>
									<xsl:when test="@vocabularySource">
										<xsl:variable name="href" select="./@vocabularySource"/>
										<a href="{$href}" target="_blank"><xsl:apply-templates select="." mode="other"/></a>
									</xsl:when>
									<xsl:otherwise>
									 <xsl:apply-templates select="." mode="other"/> 
									</xsl:otherwise>
								</xsl:choose>
							</p>
						</xsl:for-each>
					</xsl:when>
					<xsl:when test="$list[not(@xml:lang)] and $list[not(@xml:lang)]/text() and $list[not(@xml:lang)]/text() != ''">
						  	<xsl:for-each select="$list[not(@xml:lang) and (not(@localType) or (@localType != 'birth' and @localType != 'death' and @localType != 'foundation' and @localType != 'suppression'))]">
							 	  <p>
									 <xsl:choose>
										<xsl:when test="@vocabularySource">
											<xsl:variable name="href" select="./@vocabularySource"/>
											<a href="{$href}" target="_blank"><xsl:apply-templates select="." mode="other"/></a>
										</xsl:when>
										<xsl:otherwise>
										 <xsl:apply-templates select="." mode="other"/> 
										</xsl:otherwise>
									</xsl:choose>
								  </p>
						   	</xsl:for-each> 
						</xsl:when>
					<xsl:otherwise>
						<xsl:variable name="language.first" select="$list[1]/@xml:lang"></xsl:variable>
						<xsl:for-each select="$list[not(@localType) or (@localType != 'birth' and @localType != 'death' and @localType != 'foundation' and @localType != 'suppression')]">
							<xsl:variable name="currentLang" select="current()/@xml:lang"></xsl:variable>
							<xsl:variable name="currentText" select="current()/text()"></xsl:variable>
							<xsl:if test="$currentLang = $language.first">
								<p>
									<xsl:choose>
										<xsl:when test="@vocabularySource">
											<xsl:variable name="href" select="./@vocabularySource"/>
											<a href="{$href}" target="_blank"><xsl:apply-templates select="$currentText" mode="other"/></a>
										</xsl:when>
										<xsl:otherwise>
										 <xsl:apply-templates select="$currentText" mode="other"/> 
										</xsl:otherwise>
									</xsl:choose>
								</p>
							</xsl:if>
						</xsl:for-each>
					</xsl:otherwise>
				</xsl:choose>
				<div class="linkMore">
					<a class="displayLinkShowMore linkShow" href="javascript:showMore('{$clazz}{$posParent}{$posChild}', 'p');">
						<xsl:value-of select="ape:resource('eaccpf.portal.showmore')"/>
					</a>
					<a class="displayLinkShowLess linkShow" href="javascript:showLess('{$clazz}{$posParent}{$posChild}', 'p');">
						<xsl:value-of select="ape:resource('eaccpf.portal.showless')"/>
					</a>
				</div>
			</div>
			</xsl:when>
			<xsl:otherwise>
				<xsl:choose>
					<xsl:when test="$list[@vocabularySource]">
						<xsl:variable name="href" select="$list/@vocabularySource"/>
						<a href="{$href}" target="_blank"><xsl:apply-templates select="$list" mode="other"/></a>
					</xsl:when>
					<xsl:otherwise>
					    <xsl:apply-templates select="$list" mode="other"/> 
					</xsl:otherwise>
				</xsl:choose>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<!-- Template for toDate or fromDate to detect the unknow value-->
	<xsl:template name="dateUnknow">
		<xsl:param name="dateUnknow"/>
	  	<xsl:choose>
        	<xsl:when test="$dateUnknow='unknown'">
        		<xsl:text>?</xsl:text>
        	</xsl:when>
        	<xsl:otherwise>
        		<xsl:apply-templates select="$dateUnknow" mode="other"/>
        	</xsl:otherwise>
        </xsl:choose> 
	</xsl:template>

	<!-- template for dateSet -->
	<xsl:template match="eac:dateSet">
		<xsl:if test="eac:dateRange or eac:date">
			<xsl:call-template name="multilanguageDate">
				<xsl:with-param name="list" select="eac:date"/>
			</xsl:call-template>
		<!--  	<xsl:if test="(eac:dateRange/eac:fromDate or eac:dateRange/eac:toDate) and eac:date/text()">
				<xsl:text>, </xsl:text>
			</xsl:if>-->
			<xsl:if test="eac:dateRange/eac:fromDate or eac:dateRange/eac:toDate">
				<xsl:call-template name="multilanguageDateRange">
					<xsl:with-param name="list" select="eac:dateRange"/>	
				</xsl:call-template>
			</xsl:if>
		</xsl:if>
	</xsl:template>

	<!-- template for multilanguageDate -->
	<xsl:template name="multilanguageDate">
		<xsl:param name="list"/>
		<xsl:choose>
			<xsl:when test="count($list) > 1">
				<xsl:choose>
					<xsl:when test="$list[@xml:lang = $language.selected] and $list[@xml:lang = $language.selected]/text() and $list[@xml:lang = $language.selected]/text() != ''">
						<xsl:for-each select="$list[@xml:lang = $language.selected]">
							<xsl:if test="current()/text()">
							    <xsl:apply-templates select="." mode="other"/> 
							    <xsl:if test="position() != last()">
									<xsl:text>, </xsl:text>
								</xsl:if>
							</xsl:if>
						</xsl:for-each>
					</xsl:when>
					<xsl:when test="$list[@xml:lang = $language.default] and $list[@xml:lang = $language.default]/text() and $list[@xml:lang = $language.default]/text() != ''">
						<xsl:for-each select="$list[@xml:lang = $language.default]">
							<xsl:if test="current()/text()">
							    <xsl:apply-templates select="." mode="other"/> 
							    <xsl:if test="position() != last()">
									<xsl:text>, </xsl:text>
								</xsl:if>
							</xsl:if>
						</xsl:for-each>
					</xsl:when>
					<xsl:when test="$list[not(@xml:lang)] and $list[not(@xml:lang)]/text() and $list[not(@xml:lang)]/text() != ''">
						  	<xsl:for-each select="$list[not(@xml:lang)]"> 
								<xsl:if test="current()/text()">
								    <xsl:apply-templates select="." mode="other"/> 
								    <xsl:if test="position() != last()">
										<xsl:text>, </xsl:text>
									</xsl:if>
							    </xsl:if>
						   	</xsl:for-each> 
					</xsl:when>
					<xsl:otherwise> <!-- first language -->
						<xsl:variable name="language.first" select="$list[1]/@xml:lang"></xsl:variable>
						<xsl:for-each select="$list">
							<xsl:if test="current()/text()">
								<xsl:variable name="currentLang" select="current()/@xml:lang"></xsl:variable>
								<xsl:if test="$currentLang = $language.first">
								    <xsl:apply-templates select="." mode="other"/> 
								    <xsl:if test="position() != last()">
										<xsl:text>, </xsl:text>
									</xsl:if>
								</xsl:if>			
							</xsl:if>
						</xsl:for-each>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
			<xsl:otherwise>
				<xsl:for-each select="$list">
					<xsl:apply-templates select="." mode="other"/> 
				</xsl:for-each>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>	

	<!-- template for multilanguageDateRange -->
	<xsl:template name="multilanguageDateRange">
		<xsl:param name="list"/>
		<xsl:choose>
			<xsl:when test="count($list) > 1">
				<xsl:choose>
					<xsl:when test="($list/eac:fromDate[@xml:lang = $language.selected] and $list/eac:fromDate[@xml:lang = $language.selected]/text() and $list/eac:fromDate[@xml:lang = $language.selected]/text() != '')
					                 or ($list/eac:toDate[@xml:lang = $language.selected] and $list/eac:toDate[@xml:lang = $language.selected]/text() and $list/eac:toDate[@xml:lang = $language.selected]/text() != '')">
						<xsl:for-each select="$list">
						    <xsl:variable name="currentLangFrom" select="current()/eac:fromDate/@xml:lang"/>
						    <xsl:variable name="currentLangTo" select="current()/eac:toDate/@xml:lang"/>
						    <xsl:choose>
						    	<xsl:when test="./eac:fromDate">
						    		<xsl:if test="($currentLangFrom = $language.selected)">
						    			<xsl:if test="position() > 1 or (position() = 1 and ./parent::node()/eac:date/text())">
											<xsl:text>, </xsl:text>
										</xsl:if>
										<xsl:call-template name="fromToDate">
				          	 				<xsl:with-param name="dateRange" select="."/>
			                			</xsl:call-template>
									</xsl:if>
						    	</xsl:when>
						    	<xsl:when test="./eac:toDate">
						    		<xsl:if test="($currentLangTo = $language.selected)">
						    			<xsl:if test="position() > 1  or (position() = 1 and ./parent::node()/eac:date/text())">
											<xsl:text>, </xsl:text>
										</xsl:if>
										<xsl:call-template name="fromToDate">
				          	 				<xsl:with-param name="dateRange" select="."/>
			                			</xsl:call-template>
									</xsl:if>
						    	</xsl:when>
						    	<xsl:otherwise/>
						    </xsl:choose>
						</xsl:for-each>
					</xsl:when>
					<xsl:when test="($list/eac:fromDate[@xml:lang = $language.default] and $list/eac:fromDate[@xml:lang = $language.default]/text() and $list/eac:fromDate[@xml:lang = $language.default]/text() != '') 
					                or ($list/eac:toDate[@xml:lang = $language.default] and $list/eac:toDate[@xml:lang = $language.default]/text() and $list/eac:toDate[@xml:lang = $language.default]/text() != '')">
						<xsl:for-each select="$list">
						    <xsl:variable name="currentLangFrom" select="current()/eac:fromDate/@xml:lang"/>
						    <xsl:variable name="currentLangTo" select="current()/eac:toDate/@xml:lang"/>
							<xsl:choose>
						    	<xsl:when test="./eac:fromDate">
						    		<xsl:if test="($currentLangFrom = $language.default)">
						    			<xsl:if test="position() > 1 or (position() = 1 and ./parent::node()/eac:date/text())">
											<xsl:text>, </xsl:text>
										</xsl:if>
										<xsl:call-template name="fromToDate">
				          	 				<xsl:with-param name="dateRange" select="."/>
			                			</xsl:call-template>
									</xsl:if>
						    	</xsl:when>
						    	<xsl:when test="./eac:toDate">
						    		<xsl:if test="($currentLangTo = $language.default)">
						    			<xsl:if test="position() > 1 or (position() = 1 and ./parent::node()/eac:date/text())">
											<xsl:text>, </xsl:text>
										</xsl:if>
										<xsl:call-template name="fromToDate">
				          	 				<xsl:with-param name="dateRange" select="."/>
			                			</xsl:call-template>
									</xsl:if>
						    	</xsl:when>
						    	<xsl:otherwise/>
						    </xsl:choose>
						</xsl:for-each> 
					</xsl:when>
					<xsl:when test="($list/eac:fromDate[not(@xml:lang)] and $list/eac:fromDate[not(@xml:lang)]/text() and $list/eac:fromDate[not(@xml:lang)]/text() != '')
					                or ($list/eac:toDate[not(@xml:lang)] and $list/eac:toDate[not(@xml:lang)]/text() and $list/eac:toDate[not(@xml:lang)]/text() != '')">
							  	<xsl:for-each select="$list">
									<xsl:choose>
								    	<xsl:when test="./eac:fromDate">
								    		<xsl:if test="./eac:fromDate[not(@xml:lang)]">
								    			<xsl:if test="position() > 1 or (position() = 1 and ./parent::node()/eac:date/text())">
													<xsl:text>, </xsl:text>
												</xsl:if>
												<xsl:call-template name="fromToDate">
						          	 				<xsl:with-param name="dateRange" select="."/>
					                			</xsl:call-template>
											</xsl:if>
								    	</xsl:when>
								    	<xsl:when test="./eac:toDate">
								    		<xsl:if test="./eac:toDate[not(@xml:lang)]">
								    			<xsl:if test="position() > 1 or (position() = 1 and ./parent::node()/eac:date/text())">
													<xsl:text>, </xsl:text>
												</xsl:if>
												<xsl:call-template name="fromToDate">
						          	 				<xsl:with-param name="dateRange" select="."/>
					                			</xsl:call-template>
											</xsl:if>
								    	</xsl:when>
							    	<xsl:otherwise/>
							    </xsl:choose>
						     </xsl:for-each>
					</xsl:when>
					<xsl:otherwise> <!-- first language -->
						<xsl:variable name="language.first" select="$list[1]/eac:fromDate/@xml:lang"/>
						<xsl:variable name="languageTo.first" select="$list[1]/eac:toDate/@xml:lang"/>
						<xsl:for-each select="$list">
								<xsl:variable name="currentLang" select="current()/eac:fromDate/@xml:lang"/>
								<xsl:variable name="currentToLang" select="current()/eac:toDate/@xml:lang"/>
								<xsl:choose>
									<xsl:when test="$language.first">
										<xsl:choose>
										    	<xsl:when test="./eac:fromDate">
										    		<xsl:if test="$currentLang=$language.first">
										    			<xsl:if test="position() > 1 or (position() = 1 and ./parent::node()/eac:date/text())">
															<xsl:text>, </xsl:text>
														</xsl:if>
														<xsl:call-template name="fromToDate">
								          	 				<xsl:with-param name="dateRange" select="."/>
							                			</xsl:call-template>
													</xsl:if>
										    	</xsl:when>
										    	<xsl:when test="./eac:toDate">
										    		<xsl:if test="$currentToLang=$language.first">
										    			<xsl:if test="position() > 1 or (position() = 1 and ./parent::node()/eac:date/text())">
															<xsl:text>, </xsl:text>
														</xsl:if>
														<xsl:call-template name="fromToDate">
								          	 				<xsl:with-param name="dateRange" select="."/>
							                			</xsl:call-template>
													</xsl:if>
										    	</xsl:when>
									    	<xsl:otherwise/>
									    </xsl:choose>
									 </xsl:when>
									 <xsl:otherwise>
									 	<xsl:choose>
										    	<xsl:when test="./eac:fromDate">
										    		<xsl:if test="$currentLang=$languageTo.first">
										    			<xsl:if test="position() > 1">
															<xsl:text>, </xsl:text>
														</xsl:if>
														<xsl:call-template name="fromToDate">
								          	 				<xsl:with-param name="dateRange" select="."/>
							                			</xsl:call-template>
													</xsl:if>
										    	</xsl:when>
										    	<xsl:when test="./eac:toDate">
										    		<xsl:if test="$currentToLang=$languageTo.first">
										    			<xsl:if test="position() > 1 or (position() = 1 and ./parent::node()/eac:date/text())">
															<xsl:text>, </xsl:text>
														</xsl:if>
														<xsl:call-template name="fromToDate">
								          	 				<xsl:with-param name="dateRange" select="."/>
							                			</xsl:call-template>
													</xsl:if>
										    	</xsl:when>
									    	<xsl:otherwise/>
									    </xsl:choose>
									 </xsl:otherwise> 
							    </xsl:choose>
						</xsl:for-each>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
			<xsl:otherwise> <!--only one dateRange-->
				<xsl:if test ="$list/parent::node()/eac:date/text()">
					<xsl:text>, </xsl:text>
				</xsl:if>
				<xsl:call-template name="fromToDate">
					<xsl:with-param name="dateRange" select="$list"/>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>	

	<!--template fromDate toDate-->
	<xsl:template name="fromToDate">
		<xsl:param name="dateRange"/>
		<xsl:choose>
			<xsl:when test="$dateRange/eac:fromDate/text()='unknown'">
				<xsl:text>?</xsl:text>
			</xsl:when>
			<xsl:otherwise>
				<xsl:apply-templates select="$dateRange/eac:fromDate" mode="other"/>
		    </xsl:otherwise>
		</xsl:choose>
		<xsl:choose>
			<xsl:when test="string(number(substring($dateRange/eac:toDate,1,2)))!='NaN' or ($dateRange/eac:toDate/text() = 'unknown' and $dateRange/eac:fromDate/text() !='unknown')
			                or not($dateRange/eac:toDate) or not($dateRange/eac:fromDate)">
				<xsl:text> - </xsl:text>
			</xsl:when>
			<xsl:otherwise>
				<xsl:text> </xsl:text>
			</xsl:otherwise>
		</xsl:choose>
		<xsl:choose>	
			<xsl:when test="$dateRange/eac:toDate/text()='unknown' and $dateRange/eac:fromDate/text() != 'unknown'">
				<xsl:text>?</xsl:text>
			</xsl:when>
		    <xsl:otherwise>
		    	<xsl:if test="$dateRange/eac:toDate/text()!='unknown'"> 
		    		<xsl:apply-templates select="$dateRange/eac:toDate" mode="other"/> 
		   		</xsl:if>
		    </xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<!-- template for date -->
	<xsl:template match="eac:date">
		<xsl:if test="./text()">
			<xsl:if test="position() != 1">
				<xsl:text>, </xsl:text>
			</xsl:if>
	        <xsl:apply-templates mode="other"/>
			<xsl:for-each select="eac:date">
				<xsl:if test="current()/text()">
					<xsl:if test="position() != last()">
						<xsl:text>, </xsl:text>
					</xsl:if>
					<xsl:apply-templates select="current()" mode="other"/>
				</xsl:if>
			</xsl:for-each>
		</xsl:if>
	</xsl:template>

	<!-- template for dateRange -->
	<xsl:template match="eac:dateRange">
			<xsl:call-template name="fromToDate">
				<xsl:with-param name="dateRange" select="."/>
			</xsl:call-template>
	</xsl:template>

	<!-- template for multilanguage only one element date in birth, death, foundation or closing-->
	<xsl:template name="multilanguageOneDate">
		<xsl:param name="list"/>
			<xsl:choose>
				<xsl:when test="count($list) > 1">
					<xsl:choose>
						<xsl:when test="$list[@xml:lang = $language.selected] and $list[@xml:lang = $language.selected]/text() and $list[@xml:lang = $language.selected]/text() != ''">
						    <xsl:for-each select="$list[@xml:lang = $language.selected]">
								<xsl:if test="position()=1"> 
									<xsl:call-template name="dateUnknow">
										<xsl:with-param name="dateUnknow" select="."/>
									</xsl:call-template>
							 	</xsl:if> 
					 	    </xsl:for-each> 
						</xsl:when>
						<xsl:when test="$list[@xml:lang = $language.default] and $list[@xml:lang = $language.default]/text() and $list[@xml:lang = $language.default]/text() != ''">
						 	<xsl:for-each select="$list[@xml:lang = $language.default]"> 
								  <xsl:if test="position()=1">
									<xsl:call-template name="dateUnknow">
										<xsl:with-param name="dateUnknow" select="."/>
									</xsl:call-template>
								 </xsl:if>
						  	</xsl:for-each>
						</xsl:when>
						<xsl:when test="$list[not(@xml:lang)] and $list[not(@xml:lang)]/text() and $list[not(@xml:lang)]/text() != ''">
						  	<xsl:for-each select="$list[not(@xml:lang)]"> 
							 	  <xsl:if test="position()=1"> 
									 <xsl:call-template name="dateUnknow">
										<xsl:with-param name="dateUnknow" select="."/>
									</xsl:call-template>
							  	 </xsl:if>
						   	</xsl:for-each> 
						</xsl:when>
						<xsl:otherwise> 
						 	<xsl:variable name="language.first" select="$list[1]/@xml:lang"></xsl:variable>
							<xsl:for-each select="$list">
								<xsl:variable name="currentLang" select="current()/@xml:lang"></xsl:variable>
										<xsl:if test="$currentLang = $language.first">
											<xsl:if test="position() = 1">
												<xsl:call-template name="dateUnknow">
										           <xsl:with-param name="dateUnknow" select="."/>
												</xsl:call-template>
										  	</xsl:if>
										</xsl:if>
							</xsl:for-each>
					   </xsl:otherwise>
					</xsl:choose>
				</xsl:when>
				<xsl:otherwise>
					<xsl:for-each select="$list">
							<xsl:call-template name="dateUnknow">
								<xsl:with-param name="dateUnknow" select="."/>
							</xsl:call-template>
					</xsl:for-each>
				</xsl:otherwise>
			</xsl:choose>
	</xsl:template>

	<!-- template for multilanguage -->
	<xsl:template name="multilanguage">
		<xsl:param name="list"/>
		<xsl:param name="clazz"/>
		<xsl:choose>
			<xsl:when test="count($list) > 1">
			<div id="{$clazz}" class= "moreDisplay">
				<xsl:choose>
					<xsl:when test="$list[@xml:lang = $language.selected] and $list[@xml:lang = $language.selected]/text() and $list[@xml:lang = $language.selected]/text() != ''">
						<xsl:for-each select="$list">
							<xsl:variable name="currentLang" select="current()/@xml:lang"></xsl:variable>
							<xsl:if test="$currentLang = $language.selected">
								<p>
								    <xsl:apply-templates mode="other"/> 
								</p>
							</xsl:if>
						</xsl:for-each>
					</xsl:when>
					<xsl:when test="$list[@xml:lang = $language.default] and $list[@xml:lang = $language.default]/text() and $list[@xml:lang = $language.default]/text() != ''">
						<xsl:for-each select="$list">
							<xsl:variable name="currentLang" select="current()/@xml:lang"></xsl:variable>
							<xsl:if test="$currentLang = $language.default">
								<p>
									<xsl:apply-templates mode="other"/> 
								</p>
							</xsl:if>
						</xsl:for-each>
					</xsl:when>
					<xsl:when test="$list[not(@xml:lang)] and $list[not(@xml:lang)]/text() and $list[not(@xml:lang)]/text() != ''">
						  	<xsl:for-each select="$list[not(@xml:lang)]"> 
								<p>
									  <xsl:apply-templates select="." mode="other"/>
						   		</p>
						   	</xsl:for-each> 
					</xsl:when>
					<xsl:otherwise> <!-- first language -->
						<xsl:variable name="language.first" select="$list[1]/@xml:lang"></xsl:variable>
						<xsl:for-each select="$list">
							<xsl:variable name="currentLang" select="current()/@xml:lang"></xsl:variable>
							<xsl:if test="$currentLang = $language.first">
								<p>
									 <xsl:apply-templates select="." mode="other"/> 			 
								</p>			
							</xsl:if>
						</xsl:for-each>
					</xsl:otherwise>
				</xsl:choose>
				<div class="linkMore">
					<a class="displayLinkShowMore linkShow" href="javascript:showMore('{$clazz}', 'p');">
						<xsl:value-of select="ape:resource('eaccpf.portal.showmore')"/>
					</a>
					<a class="displayLinkShowLess linkShow" href="javascript:showLess('{$clazz}', 'p');">
						<xsl:value-of select="ape:resource('eaccpf.portal.showless')"/>
					</a>
				</div>
			</div>
			</xsl:when>
			<xsl:otherwise>
				<xsl:for-each select="$list">
					<p>
						<xsl:apply-templates mode="other"/> 
					</p>
				</xsl:for-each>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	
	<!-- template for outline -->
	<xsl:template name="multilanguageOutline">
		<xsl:param name="list"/><!-- outline/level -->
		<xsl:param name="clazz"/>
		<xsl:param name="count"/>
		<xsl:choose>
			<xsl:when test="$list/descendant-or-self::node()/eac:item[@xml:lang = $language.selected] and $list/descendant-or-self::node()/eac:item[@xml:lang = $language.selected]/text() and $list/descendant-or-self::node()/eac:item[@xml:lang = $language.selected]/text() != ''">
			 <xsl:for-each select="$list">
					<xsl:if test="./descendant-or-self::node()/eac:item[@xml:lang = $language.selected]">
						<pre class="outline">
							<xsl:if test="name(./parent::node()) != 'outline'">
								<xsl:call-template name="repeat">
									<xsl:with-param name="count" select="$count"/>
								</xsl:call-template>
							</xsl:if>
							<xsl:text>* </xsl:text>
							<xsl:apply-templates select="./eac:item" mode="other"/>
						</pre>
					</xsl:if>
					<xsl:for-each select="./eac:level">
								<xsl:call-template name="multilanguageOutline">
									<xsl:with-param name="list" select="."/>
									<xsl:with-param name="clazz" select="$clazz"/>
									<xsl:with-param name="count" select="$count+1"/>
								</xsl:call-template>
					</xsl:for-each> 
				</xsl:for-each> 
			</xsl:when>
			<!-- TODO: other when to detect the language of the browser -->
			<xsl:otherwise/>
		</xsl:choose>
	</xsl:template>
	
	<!-- template to tabulate the elements outline -->
	<xsl:template name="repeat">
	  <xsl:param name="count" />
	  <xsl:text>  </xsl:text>
	  <xsl:if test="$count &gt; 1">
	    <xsl:call-template name="repeat"> 
	      <xsl:with-param name="count" select="$count - 1" />
	    </xsl:call-template>
	  </xsl:if>
	</xsl:template>
</xsl:stylesheet>