<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:fn="http://www.w3.org/2005/xpath-functions"
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
	
	<xsl:variable name="smallcase" select="'abcdefghijklmnopqrstuvwxyz'" />
	<xsl:variable name="uppercase" select="'ABCDEFGHIJKLMNOPQRSTUVWXYZ'" />

	<xsl:template match="text()" mode="other">
		<xsl:value-of select="fn:normalize-space(ape:highlight(., 'other'))" disable-output-escaping="yes" />
	</xsl:template>
	<xsl:template match="/">
		<xsl:variable name="existDates" select="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:existDates"/>
		<xsl:variable name="entityType" select="./eac:eac-cpf/eac:cpfDescription/eac:identity/eac:entityType"/>
		<h1 class="titleproper">
		    <!-- nameEntry -->
	      	<xsl:call-template name="namePriorisation">
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
			<!-- dateRange fromDate -->
			<xsl:if test="$existDates/eac:dateRange/eac:fromDate/text() or $existDates/eac:dateSet/eac:dateRange/eac:fromDate/text() or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:places/eac:place/eac:placeEntry[@localType='birth']/text()
			              or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:places/eac:place/eac:placeEntry[@localType='foundation']/text()">
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
								<xsl:if test="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:places/eac:place/eac:placeEntry[@localType='birth']/text()">
							  		<xsl:text>, </xsl:text>
							  		<xsl:call-template name="multilanguagePlaceEntry">
							  			<xsl:with-param name="list" select="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:places/eac:place/eac:placeEntry[@localType='birth']"/>
							  		</xsl:call-template>		
							    </xsl:if>
							</xsl:if>
							<xsl:if test="$entityType = 'corporateBody'">
								<xsl:if test="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:places/eac:place/eac:placeEntry[@localType='foundation']/text()">
							  		<xsl:text>, </xsl:text>
							  		<xsl:call-template name="multilanguagePlaceEntry">
							  			<xsl:with-param name="list" select="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:places/eac:place/eac:placeEntry[@localType='foundation']"/>
							  		</xsl:call-template>		
							    </xsl:if>
							</xsl:if>
						</div>
				</div>
			</xsl:if>
			<!-- dateRange toDate -->
			<xsl:if test="($existDates/eac:dateRange/eac:toDate/text() or $existDates/eac:dateSet/eac:dateRange/eac:toDate/text()
						  or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:places/eac:place/eac:placeEntry[@localType='death']/text()
			              or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:places/eac:place/eac:placeEntry[@localType='suppression']/text())
						  and ($existDates/eac:dateRange[1][@localType!='open'] or $existDates/eac:dateSet/eac:dateRange[1][@localType!='open']
						  	or $existDates/eac:dateRange[1][not(@localType)] or $existDates/eac:dateSet/eac:dateRange[1][not(@localType)])">
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
									<xsl:if test="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:places/eac:place/eac:placeEntry[@localType='death']/text()">
								  		<xsl:text>, </xsl:text>
								  		<xsl:call-template name="multilanguagePlaceEntry">
								  			<xsl:with-param name="list" select="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:places/eac:place/eac:placeEntry[@localType='death']"/>
								  		</xsl:call-template>		
								    </xsl:if>
								</xsl:if>
								<xsl:if test="$entityType = 'corporateBody'">
									<xsl:if test="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:places/eac:place/eac:placeEntry[@localType='suppression']/text()">
								  		<xsl:text>, </xsl:text>
								  		<xsl:call-template name="multilanguagePlaceEntry">
								  			<xsl:with-param name="list" select="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:places/eac:place/eac:placeEntry[@localType='suppression']"/>
								  		</xsl:call-template>		
								    </xsl:if>
								</xsl:if>
							</div>
					</div>
			</xsl:if>
			<!-- Date -->
			<xsl:if test="($existDates/eac:date/text() or $existDates/eac:dateSet/eac:date/text())
						and (not($existDates/eac:dateRange/eac:fromDate/text()) and not($existDates/eac:dateSet/eac:dateRange/eac:fromDate/text()) and not($existDates/eac:dateRange/eac:toDate/text()) and not($existDates/eac:dateSet/eac:dateRange/eac:toDate/text()))">
					<div class="row">
							<div class="leftcolumn">
						   		<h2>
									<xsl:value-of select="ape:resource('eaccpf.portal.date')"/>
						   			<xsl:text>:</xsl:text>
						   		</h2>
						   	</div>
						   	<div class="rightcolumn">
						   		<xsl:if test="$existDates/eac:date/text()">
									<xsl:apply-templates select="$existDates/eac:date"/>
								</xsl:if>
						   		<xsl:if test="$existDates/eac:dateSet/eac:date/text()">
									<xsl:apply-templates select="$existDates/eac:dateSet/eac:date[1]"/>
								</xsl:if>
							</div>
					</div>
			</xsl:if>
			<!-- descriptiveNote of the existDates element. -->
			<xsl:if test="$existDates/eac:descriptiveNote/eac:p/text()">   
				<div class="row">
						<div class="leftcolumn">
					   		<h2><xsl:value-of select="ape:resource('eaccpf.portal.note')"/><xsl:text>:</xsl:text></h2>
					   	</div>
					   	<div class="rightcolumn">
							<xsl:call-template name="multilanguage">
					   			<xsl:with-param name="list" select="$existDates/eac:descriptiveNote/eac:p"/>
					   			<xsl:with-param name="clazz" select="'language'"/>
					   		</xsl:call-template>
						</div>
				</div>
			</xsl:if>

			<!-- alternative names -->
			<xsl:if test="count(//eac:nameEntry) > 1">
				<div class="row" id="titleAlternativeName">
					<div class="leftcolumn">
				   		<h2><xsl:value-of select="ape:resource('eaccpf.portal.alternativeForms')"/><xsl:text>:</xsl:text></h2>
				   	</div>
				   	<div class="rightcolumn">
				   			<xsl:call-template name="alternativeNamePriorisation">
				   				<xsl:with-param name="list" select="//eac:nameEntry"/>
				   				<xsl:with-param name="clazz" select="'alternativeName'"/>
				   			</xsl:call-template>  
					</div>
				</div>
			</xsl:if>
			<!--descriptive note inside identity -->
			<xsl:if test="./eac:eac-cpf/eac:cpfDescription/eac:identity/eac:descriptiveNote/eac:p/text()">   
				<div class="row">
						<div class="leftcolumn">
					   		<h2><xsl:value-of select="ape:resource('eaccpf.portal.note')"/><xsl:text>:</xsl:text></h2>
					   	</div>
					   	<div class="rightcolumn">
							<xsl:call-template name="multilanguage">
					   			<xsl:with-param name="list" select="./eac:eac-cpf/eac:cpfDescription/eac:identity/eac:descriptiveNote/eac:p"/>
					   			<xsl:with-param name="clazz" select="'language'"/>
					   		</xsl:call-template>
						</div>
				</div>
			</xsl:if>
			
			<!-- location -->
			<xsl:if test="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:places/eac:place">
				<h2 class="title"><xsl:value-of select="ape:resource('eaccpf.portal.location')"/></h2>
				<xsl:for-each select="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:places"> 
					<div class="blockPlural">
						<xsl:variable name="posParent" select="position()"/>
						<xsl:for-each select="./eac:place">
							<div class="blockSingular">
								<xsl:variable name="posChild" select="position()"/>
							    <!-- placeEntry in localDescription -->
								<xsl:call-template name="places">
						    		<xsl:with-param name="list" select="./eac:placeEntry"/>
						    		<xsl:with-param name="clazz" select="'locationPlace_'"/>
						    		<xsl:with-param name="posParent" select="$posParent"/>
							    	<xsl:with-param name="posChild" select="$posChild"/>
						    	    <xsl:with-param name="title" select="'eaccpf.portal.location'"/> 
						    	</xsl:call-template>
						       <!-- placeRole -->
						       <xsl:if test="./eac:placeRole/text() and ./eac:placeEntry/text()">
							        <div class="row">
									    <div class="leftcolumn">
								   			<h2 class="subrow"><xsl:value-of select="ape:resource('eaccpf.portal.roleOfLocation')"/><xsl:text>:</xsl:text></h2>
								   	    </div>
								     	<div class="rightcolumn">
								     		<xsl:call-template name="multilanguageWithVocabularySource">
				   								<xsl:with-param name="list" select="./eac:placeRole"/>
								   				<xsl:with-param name="clazz" select="'placeRole_'"/>
								   				<xsl:with-param name="posParent" select="$posParent"/>
								   				<xsl:with-param name="posChild" select="$posChild"/>
								   			</xsl:call-template>
									    </div>
							        </div>
							    </xsl:if>
						    	<xsl:if test="./eac:address/eac:addressLine[@localType='street']/text()">
							    	<xsl:call-template name="commonChild">
							   			<xsl:with-param name="list" select="./eac:address/eac:addressLine[@localType='street']"/>
							   			<xsl:with-param name="clazz" select="'language'"/>
							   			<xsl:with-param name="posParent" select="$posParent"/>
				    					<xsl:with-param name="posChild" select="$posChild"/>
							    		<xsl:with-param name="title" select="'eaccpf.description.combo.address.component.street'"/>
							    	</xsl:call-template>
								</xsl:if>
								<xsl:if test="./eac:address/eac:addressLine[@localType='postalcode']/text()">
						    		<xsl:call-template name="commonChild">
							   			<xsl:with-param name="list" select="./eac:address/eac:addressLine[@localType='postalcode']"/>
							   			<xsl:with-param name="clazz" select="'language'"/>
							   			<xsl:with-param name="posParent" select="$posParent"/>
				    					<xsl:with-param name="posChild" select="$posChild"/>
						    			<xsl:with-param name="title" select="'eaccpf.description.combo.address.component.postalcode'"/>
					    			</xsl:call-template>
							    </xsl:if>
							    <xsl:if test="./eac:address/eac:addressLine[@localType='localentity']/text()">
						    		<xsl:call-template name="commonChild">
							   			<xsl:with-param name="list" select="./eac:address/eac:addressLine[@localType='localentity']"/>
							   			<xsl:with-param name="clazz" select="'language'"/>
							   			<xsl:with-param name="posParent" select="$posParent"/>
				    					<xsl:with-param name="posChild" select="$posChild"/>
						    			<xsl:with-param name="title" select="'eaccpf.description.combo.address.component.localentity'"/>
						    		</xsl:call-template>
								</xsl:if>
								<xsl:if test="./eac:address/eac:addressLine[@localType='secondem']/text()">
						    		<xsl:call-template name="commonChild">
							   			<xsl:with-param name="list" select="./eac:address/eac:addressLine[@localType='secondem']"/>
							   			<xsl:with-param name="clazz" select="'language'"/>
							   			<xsl:with-param name="posParent" select="$posParent"/>
				    					<xsl:with-param name="posChild" select="$posChild"/>
						    			<xsl:with-param name="title" select="'eaccpf.description.combo.address.component.secondem'"/>
						    		</xsl:call-template>
								</xsl:if>
								<xsl:if test="./eac:address/eac:addressLine[@localType='firstdem']/text()">
						    		<xsl:call-template name="commonChild">
							   			<xsl:with-param name="list" select="./eac:address/eac:addressLine[@localType='firstdem']"/>
							   			<xsl:with-param name="clazz" select="'language'"/>
							   			<xsl:with-param name="posParent" select="$posParent"/>
				    					<xsl:with-param name="posChild" select="$posChild"/>
						    			<xsl:with-param name="title" select="'eaccpf.description.combo.address.component.firstdem'"/>
						    		</xsl:call-template>
								</xsl:if>
								<xsl:if test="./eac:address/eac:addressLine[@localType='country']/text()">
						    		<xsl:call-template name="commonChild">
							   			<xsl:with-param name="list" select="./eac:address/eac:addressLine[@localType='country']"/>
							   			<xsl:with-param name="clazz" select="'language'"/>
							   			<xsl:with-param name="posParent" select="$posParent"/>
				    					<xsl:with-param name="posChild" select="$posChild"/>
						    			<xsl:with-param name="title" select="'eaccpf.description.combo.address.component.country'"/>
						    		</xsl:call-template>
								</xsl:if>
								<xsl:if test="./eac:address/eac:addressLine[@localType='other']/text()">
						    		<xsl:call-template name="commonChild">
							   			<xsl:with-param name="list" select="./eac:address/eac:addressLine[@localType='other']"/>
							   			<xsl:with-param name="clazz" select="'language'"/>
							   			<xsl:with-param name="posParent" select="$posParent"/>
				    					<xsl:with-param name="posChild" select="$posChild"/>
						    			<xsl:with-param name="title" select="'eaccpf.description.combo.address.component.other'"/>
						    		</xsl:call-template>
								</xsl:if>
								<!--descriptive note place-->
								<xsl:if test="./eac:descriptiveNote/eac:p/text()">   
						    		<xsl:call-template name="commonChild">
							    		<xsl:with-param name="list" select="./eac:descriptiveNote/eac:p"/>
							    		<xsl:with-param name="clazz" select="'descriptiveNotelocationPlace_'"/>					   			
						   				<xsl:with-param name="posParent" select="$posParent"/>
				    					<xsl:with-param name="posChild" select="$posChild"/>
						    			<xsl:with-param name="title" select="'eaccpf.portal.note'"/>
					    			</xsl:call-template>
							    </xsl:if>
							    <!-- citation in place -->
							  	<xsl:call-template name="commonChild">
						    		<xsl:with-param name="list" select="./eac:citation"/>
						    		<xsl:with-param name="clazz" select="'citationLocalDescription_'"/>
						    		<xsl:with-param name="posParent" select="$posParent"/>
						    		<xsl:with-param name="posChild" select="$posChild"/>
						    		<xsl:with-param name="title" select="'eaccpf.portal.citation'"/>
						    	</xsl:call-template>
						    </div>	
					     </xsl:for-each>
				     	<!-- descriptiveNote places-->
				     	<xsl:if test="./eac:descriptiveNote/eac:p/text()">
							 <xsl:call-template name="commonChild">
					    		<xsl:with-param name="list" select="./eac:descriptiveNote/eac:p"/>
					    		<xsl:with-param name="clazz" select="'descriptiveNotelocationPlaces_'"/>
					   			<xsl:with-param name="posParent" select="$posParent"/>
			    				<xsl:with-param name="posChild" select="''"/>
					    		<xsl:with-param name="title" select="'eaccpf.portal.note'"/>
					    	 </xsl:call-template>
				    	 </xsl:if>
				    </div>	 
			    </xsl:for-each>
			</xsl:if>
			<!-- localDescription -->
			<xsl:if test="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:localDescriptions/eac:localDescription">
			    <h2 class="title"><xsl:value-of select="translate(ape:resource('eaccpf.portal.localDescription'), $smallcase, $uppercase)"/></h2>
				<xsl:for-each select="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:localDescriptions"> 
					<div class="blockPlural">
						<xsl:variable name="posParent" select="position()"/>
					    <xsl:for-each select="./eac:localDescription">
				    		<div class="blockSingular">
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
						    </div>	
						</xsl:for-each>
							<!-- descriptiveNote in localDescriptions -->
						 	<xsl:call-template name="commonChild">
					    		<xsl:with-param name="list" select="./eac:descriptiveNote/eac:p"/>
					    		<xsl:with-param name="clazz" select="'descriptiveNoteLocalDescriptions_'"/>
					    		<xsl:with-param name="posParent" select="$posParent"/>
					    		<xsl:with-param name="posChild" select="''"/>
					    		<xsl:with-param name="title" select="'eaccpf.portal.note'"/>
				    	 	</xsl:call-template>
					</div> 	 	
				</xsl:for-each>		
		   </xsl:if>
		   <!-- legalStatus -->
		   <xsl:if test="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:legalStatuses/eac:legalStatus">
			    <h2 class="title"><xsl:value-of select="translate(ape:resource('eaccpf.portal.legalStatus'), $smallcase, $uppercase)"/></h2>
				<xsl:for-each select="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:legalStatuses">
					<div class="blockPlural">
						<xsl:variable name="posParent" select="position()"/> 
					    <xsl:for-each select="./eac:legalStatus">
				    		<div class="blockSingular">
					    		<xsl:variable name="posChild" select="position()"/>
					    		<!-- term legalStatus -->
						    	<xsl:call-template name="term">
						    		<xsl:with-param name="list" select="./eac:term"/>
						    		<xsl:with-param name="clazz" select="'legalStatus_'"/>
						    		<xsl:with-param name="posParent" select="$posParent"/>
						    		<xsl:with-param name="posChild" select="$posChild"/>
						    		<xsl:with-param name="title" select="'eaccpf.portal.legalStatus'"/>
						    	</xsl:call-template>
								<!-- placeEntry in legalStatus -->
								<xsl:call-template name="commonChild">
						    		<xsl:with-param name="list" select="./eac:placeEntry"/>
						    		<xsl:with-param name="clazz" select="'locationLegalStatus_'"/>
						    		<xsl:with-param name="posParent" select="$posParent"/>
						    		<xsl:with-param name="posChild" select="$posChild"/>
						    		<xsl:with-param name="title" select="'eaccpf.portal.location'"/>
						    	</xsl:call-template>
								<!-- citation in legalStatus -->
							  	<xsl:call-template name="commonChild">
						    		<xsl:with-param name="list" select="./eac:citation"/>
						    		<xsl:with-param name="clazz" select="'citationLegalStatus_'"/>
						    		<xsl:with-param name="posParent" select="$posParent"/>
						    		<xsl:with-param name="posChild" select="$posChild"/>
						    		<xsl:with-param name="title" select="'eaccpf.portal.citation'"/>
						    	</xsl:call-template>
								<!-- dates in legalStatus -->
								<xsl:call-template name="commonDates">
						    		<xsl:with-param name="date" select="./eac:date"/>
						    		<xsl:with-param name="dateRange" select="./eac:dateRange"/>
						    		<xsl:with-param name="dateSet" select="./eac:dateSet"/>
						    	</xsl:call-template>
								<!-- descriptiveNote in legalStatus -->
								<xsl:call-template name="commonChild">
						    		<xsl:with-param name="list" select="./eac:descriptiveNote/eac:p"/>
						    		<xsl:with-param name="clazz" select="'descriptiveNoteLegalStatus_'"/>
						    		<xsl:with-param name="posParent" select="$posParent"/>
						    		<xsl:with-param name="posChild" select="$posChild"/>
						    		<xsl:with-param name="title" select="'eaccpf.portal.note'"/>
						    	</xsl:call-template>
						    </div>	
						</xsl:for-each>
							<!-- descriptiveNote in legalStatus -->
							 <xsl:call-template name="commonChild">
					    		<xsl:with-param name="list" select="./eac:descriptiveNote/eac:p"/>
					    		<xsl:with-param name="clazz" select="'descriptiveNoteLegalStatuses_'"/>
					    		<xsl:with-param name="posParent" select="$posParent"/>
					    		<xsl:with-param name="posChild" select="''"/>
					    		<xsl:with-param name="title" select="'eaccpf.portal.note'"/>
					    	 </xsl:call-template>
			    	 </div>
				</xsl:for-each>		
		   </xsl:if>
		   <!-- function -->
		   <xsl:if test="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:functions/eac:function">
			    <h2 class="title"><xsl:value-of select="translate(ape:resource('eaccpf.portal.function'), $smallcase, $uppercase)"/></h2>
				<xsl:for-each select="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:functions">
					<div class="blockPlural">
						<xsl:variable name="posParent" select="position()"/> 
					    <xsl:for-each select="./eac:function">
					    	<div class="blockSingular">	
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
					    	</div>
						</xsl:for-each>
							<!-- descriptiveNote in functions -->
							 <xsl:call-template name="commonChild">
					    		<xsl:with-param name="list" select="./eac:descriptiveNote/eac:p"/>
					    		<xsl:with-param name="clazz" select="'descriptiveNoteFunctions_'"/>
					    		<xsl:with-param name="posParent" select="$posParent"/>
					    		<xsl:with-param name="posChild" select="''"/>
					    		<xsl:with-param name="title" select="'eaccpf.portal.note'"/>
					    	 </xsl:call-template>
					</div>	    	 
				</xsl:for-each>		
		    </xsl:if>
		    <!-- occupation -->
		    <xsl:if test="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:occupations/eac:occupation">
			    <h2 class="title"><xsl:value-of select="translate(ape:resource('eaccpf.portal.occupation'), $smallcase, $uppercase)"/></h2>
				<xsl:for-each select="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:occupations"> 
					<div class="blockPlural">
						<xsl:variable name="posParent" select="position()"/> 
					    <xsl:for-each select="./eac:occupation">
				    		<div class="blockSingular">
					    		<xsl:variable name="posChild" select="position()"/> 
					    		<!-- term occupation -->
						    	<xsl:call-template name="term">
						    		<xsl:with-param name="list" select="./eac:term"/>
						    		<xsl:with-param name="clazz" select="'occupation_'"/>
						    		<xsl:with-param name="posParent" select="$posParent"/>
						    		<xsl:with-param name="posChild" select="$posChild"/>
						    		<xsl:with-param name="title" select="'eaccpf.portal.occupation'"/>
						    	</xsl:call-template>
								<!-- placeEntry in occupation-->
								<xsl:call-template name="commonChild">
						    		<xsl:with-param name="list" select="./eac:placeEntry"/>
						    		<xsl:with-param name="clazz" select="'locationOccupation_'"/>
						    		<xsl:with-param name="posParent" select="$posParent"/>
						    		<xsl:with-param name="posChild" select="$posChild"/>
						    		<xsl:with-param name="title" select="'eaccpf.portal.location'"/>
						    	</xsl:call-template>
								<!-- citation in occupation -->
							  	<xsl:call-template name="commonChild">
						    		<xsl:with-param name="list" select="./eac:citation"/>
						    		<xsl:with-param name="clazz" select="'citationOccupation_'"/>
						    		<xsl:with-param name="posParent" select="$posParent"/>
						    		<xsl:with-param name="posChild" select="$posChild"/>
						    		<xsl:with-param name="title" select="'eaccpf.portal.citation'"/>
						    	</xsl:call-template>
								<!-- dates in occupation-->
								<xsl:call-template name="commonDates">
						    		<xsl:with-param name="date" select="./eac:date"/>
						    		<xsl:with-param name="dateRange" select="./eac:dateRange"/>
						    		<xsl:with-param name="dateSet" select="./eac:dateSet"/>
						    	</xsl:call-template>
								<!-- descriptiveNote in occupation-->
								<xsl:call-template name="commonChild">
						    		<xsl:with-param name="list" select="./eac:descriptiveNote/eac:p"/>
						    		<xsl:with-param name="clazz" select="'descriptiveNoteOccupation_'"/>
						    		<xsl:with-param name="posParent" select="$posParent"/>
						    		<xsl:with-param name="posChild" select="$posChild"/>
						    		<xsl:with-param name="title" select="'eaccpf.portal.note'"/>
						    	</xsl:call-template>
						    </div>	
						</xsl:for-each>
						<!-- descriptiveNote in occupations -->
						 <xsl:call-template name="commonChild">
				    		<xsl:with-param name="list" select="./eac:descriptiveNote/eac:p"/>
				    		<xsl:with-param name="clazz" select="'descriptiveNoteOccupations_'"/>
				    		<xsl:with-param name="posParent" select="$posParent"/>
				    		<xsl:with-param name="posChild" select="''"/>
				    		<xsl:with-param name="title" select="'eaccpf.portal.note'"/>
				    	 </xsl:call-template>
			    	 </div>
				</xsl:for-each>		
		    </xsl:if>
		    <!--mandates-->
		    <xsl:if test="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:mandates/eac:mandate">
			    <h2 class="title"><xsl:value-of select="translate(ape:resource('eaccpf.portal.mandate'), $smallcase, $uppercase)"/></h2>
				<xsl:for-each select="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:mandates">
					<div class="blockPlural">
						<xsl:variable name="posParent" select="position()"/>  
					    <xsl:for-each select="./eac:mandate">
				    		<div class="blockSingular">
					    		<xsl:variable name="posChild" select="position()"/> 
					    		<!-- term mandate -->
						    	<xsl:call-template name="term">
						    		<xsl:with-param name="list" select="./eac:term"/>
						    		<xsl:with-param name="clazz" select="'mandate_'"/>
						    		<xsl:with-param name="posParent" select="$posParent"/>
						    		<xsl:with-param name="posChild" select="$posChild"/>
						    		<xsl:with-param name="title" select="'eaccpf.portal.mandate'"/>
						    	</xsl:call-template>
								<!-- placeEntry in mandate-->
								<xsl:call-template name="commonChild">
						    		<xsl:with-param name="list" select="./eac:placeEntry"/>
						    		<xsl:with-param name="clazz" select="'locationMandate_'"/>
						    		<xsl:with-param name="posParent" select="$posParent"/>
						    		<xsl:with-param name="posChild" select="$posChild"/>
						    		<xsl:with-param name="title" select="'eaccpf.portal.location'"/>
						    	</xsl:call-template>
								<!-- citation in mandate -->
							  	<xsl:call-template name="commonChild">
						    		<xsl:with-param name="list" select="./eac:citation"/>
						    		<xsl:with-param name="clazz" select="'citationMandate_'"/>
						    		<xsl:with-param name="posParent" select="$posParent"/>
						    		<xsl:with-param name="posChild" select="$posChild"/>
						    		<xsl:with-param name="title" select="'eaccpf.portal.citation'"/>
						    	</xsl:call-template>
								<!-- dates in mandate-->
								<xsl:call-template name="commonDates">
						    		<xsl:with-param name="date" select="./eac:date"/>
						    		<xsl:with-param name="dateRange" select="./eac:dateRange"/>
						    		<xsl:with-param name="dateSet" select="./eac:dateSet"/>
						    	</xsl:call-template>
								<!-- descriptiveNote in mandate-->
								<xsl:call-template name="commonChild">
						    		<xsl:with-param name="list" select="./eac:descriptiveNote/eac:p"/>
						    		<xsl:with-param name="clazz" select="'descriptiveNoteMandate_'"/>
						    		<xsl:with-param name="posParent" select="$posParent"/>
						    		<xsl:with-param name="posChild" select="$posChild"/>
						    		<xsl:with-param name="title" select="'eaccpf.portal.note'"/>
						    	</xsl:call-template>
						    </div>	
						</xsl:for-each>
						<!-- descriptiveNote in mandates -->
						 <xsl:call-template name="commonChild">
				    		<xsl:with-param name="list" select="./eac:descriptiveNote/eac:p"/>
				    		<xsl:with-param name="clazz" select="'descriptiveNoteMandates_'"/>
				    		<xsl:with-param name="posParent" select="$posParent"/>
				    		<xsl:with-param name="posChild" select="''"/>
				    		<xsl:with-param name="title" select="'eaccpf.portal.note'"/>
				    	 </xsl:call-template>
			    	 </div>
				</xsl:for-each>		
		    </xsl:if>
			<!--language -->
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
			<!--descriptive note inside languagesUsed -->
			<xsl:if test="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:languagesUsed/eac:descriptiveNote/eac:p/text()">   
				<div class="row">
						<div class="leftcolumn">
					   		<h2><xsl:value-of select="ape:resource('eaccpf.portal.note')"/><xsl:text>:</xsl:text></h2>
					   	</div>
					   	<div class="rightcolumn">
							<xsl:call-template name="multilanguage">
					   			<xsl:with-param name="list" select="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:languagesUsed/eac:descriptiveNote/eac:p"/>
					   			<xsl:with-param name="clazz" select="'language'"/>
<!-- 				    			<xsl:with-param name="title" select="'eaccpf.portal.note'"/> -->
					   		</xsl:call-template>
						</div>
				</div>
			</xsl:if>
			<!-- structureOrGenealogy -->
			<xsl:if test="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:structureOrGenealogy/eac:p/text() or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:structureOrGenealogy/eac:outline/eac:level/eac:item/text()">
				<h2 class="title"><xsl:value-of select="ape:resource('eaccpf.portal.structureOrGenealogy')"/></h2>
				<xsl:variable name="firstchild" select="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:structureOrGenealogy/*[1]"/>
			  	<xsl:if test="name($firstchild)='outline'">
					   <xsl:call-template name="outline">
					   	 <xsl:with-param name="list" select="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:structureOrGenealogy"/>
					   	 <xsl:with-param name="clazz" select="'structureOrGenealogy'"/>	
					  	 <xsl:with-param name="title" select="'eaccpf.portal.structureOrGenealogy'"/> 	
					   </xsl:call-template>
					   <xsl:call-template name="p">
					   	 <xsl:with-param name="list" select="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:structureOrGenealogy"/>
					  	 <xsl:with-param name="clazz" select="'structureOrGenealogyNote'"/>	
					   </xsl:call-template>
				</xsl:if>
				<xsl:if test="name($firstchild)='p'">    
					    <xsl:call-template name="p">
					   	  <xsl:with-param name="list" select="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:structureOrGenealogy"/>
					   	  <xsl:with-param name="clazz" select="'structureOrGenealogyNote'"/>	
					    </xsl:call-template>
					   <xsl:call-template name="outline">
					   	  <xsl:with-param name="list" select="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:structureOrGenealogy"/>
					   	  <xsl:with-param name="clazz" select="'structureOrGenealogy'"/>	
					  	  <xsl:with-param name="title" select="'eaccpf.portal.structureOrGenealogy'"/> 	
					   </xsl:call-template>
				</xsl:if>
			</xsl:if> 
			<!-- generalContext -->
			<xsl:if test="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:generalContext/eac:p/text() or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:generalContext/eac:outline/eac:level/eac:item/text()">
			  	<h2 class="title"><xsl:value-of select="ape:resource('eaccpf.portal.generalContext')"/></h2> 
				<xsl:variable name="firstchild" select="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:generalContext/*[1]"/>
			  	<xsl:if test="name($firstchild)='outline'">
					   <xsl:call-template name="outline">
					   	 <xsl:with-param name="list" select="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:generalContext"/>
					   	 <xsl:with-param name="clazz" select="'generalContext'"/>	
					  	 <xsl:with-param name="title" select="'eaccpf.portal.generalContext'"/> 
					   </xsl:call-template>
					   <xsl:call-template name="p">
					   	 <xsl:with-param name="list" select="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:generalContext"/>
					  	 <xsl:with-param name="clazz" select="'generalContextNote'"/>	
					   </xsl:call-template>
				</xsl:if>
				<xsl:if test="name($firstchild)='p'">    
					    <xsl:call-template name="p">
					   	  <xsl:with-param name="list" select="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:generalContext"/>
					   	  <xsl:with-param name="clazz" select="'generalContextNote'"/>	
					    </xsl:call-template>
					   <xsl:call-template name="outline">
					   	  <xsl:with-param name="list" select="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:generalContext"/>
					   	  <xsl:with-param name="clazz" select="'generalContext'"/>	
					  	  <xsl:with-param name="title" select="'eaccpf.portal.generalContext'"/>	
					   </xsl:call-template>
				</xsl:if>
			</xsl:if> 
			<xsl:if test="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:biogHist">   
				<xsl:call-template name="bioHistMultilanguage">
					<xsl:with-param name="bioHist" select="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:biogHist"></xsl:with-param>
					<xsl:with-param name="entityType" select="$entityType"></xsl:with-param>
				</xsl:call-template>
			</xsl:if>
			
			<!-- provided by -->
		<!--  	<xsl:if test="./eac:eac-cpf/eac:control/eac:maintenanceAgency/eac:agencyName/text()">
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
			</xsl:if>-->
	</div>
	</xsl:template>
	
	<xsl:template name="bioHistMultilanguage" >
		<xsl:param name="bioHist"></xsl:param>
		<xsl:param name="entityType"></xsl:param>
		
		<!-- bioHist section title -->
		<xsl:if test="$bioHist/eac:p/text() or $bioHist/eac:citation/text() or $bioHist/eac:chronList/eac:chronItem">
			<h2 id="chronListTitle" class="title row" ><xsl:value-of select="ape:resource('eaccpf.portal.biogHist')"/></h2>
		</xsl:if>
		<!-- biogHist p --> 
		<xsl:if test="$bioHist/eac:p/text()">   
			<div class="row">
				<div class="leftcolumn">
			   		<h2 class="subrow">
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
		<!-- biogHist citation --> 
		<xsl:if test="$bioHist/eac:citation/text()">   
			<div class="row">
				<div class="leftcolumn">
			   		<h2 class="subrow">
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
					<xsl:for-each select="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:biogHist"> 
						<xsl:variable name="posParent" select="position()"/>
						<xsl:for-each select="./eac:citation">
							<xsl:variable name="posChild" select="position()"/>								
							<p>
								<xsl:call-template name="citationHref">
						   			<xsl:with-param name="link" select="./@xlink:href"/>
									<xsl:with-param name="title" select="./text()"/>
								</xsl:call-template>
							</p>
					     </xsl:for-each>
				    </xsl:for-each> 	
				</div>
			</div>
		</xsl:if>
		<!-- biogHist chronList -->
		<xsl:if test="$bioHist/eac:chronList/eac:chronItem">
			<xsl:call-template name="chronListMultilanguage">
				<xsl:with-param name="list" select="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:biogHist/eac:chronList"></xsl:with-param>
			</xsl:call-template>
		</xsl:if>
	</xsl:template>
	
	<xsl:template name="chronListMultilanguage">
		<xsl:param name="list"></xsl:param>
		<xsl:if test="$list/eac:chronItem or $list/eac:placeEntry or $list/eac:event">
			<xsl:for-each select="$list" >
				<div class="blockPlural">
				<xsl:for-each select="./eac:chronItem" >
					<div class="blockSingular">
					    <xsl:if test="./eac:date or ./eac:dateRange or ./eac:dateSet">
					    <div id="chronListItemContent" >
							<xsl:call-template name="commonDates">
					    		<xsl:with-param name="date" select="./eac:date"/>
					    		<xsl:with-param name="dateRange" select="./eac:dateRange"/>
					    		<xsl:with-param name="dateSet" select="./eac:dateSet"/>
					    	</xsl:call-template>
					    </div>
					    </xsl:if>
						<xsl:if test="./eac:placeEntry">
						<div id="chronListItemContent" class="row">
							<div class="leftcolumn">
						   		<h2 class="subrow"><xsl:value-of select="ape:resource('eaccpf.description.place')"/></h2>
						   	</div> 
						    <div class="rightcolumn"> 
								<xsl:call-template name="multilanguagePlaceEntry">
						  			<xsl:with-param name="list" select="./eac:placeEntry"/>
						  		</xsl:call-template>
					  		</div>
					  	</div>		
						</xsl:if>
						<xsl:if test="./eac:event">
							<div id="chronListItemContent" class="row">
								<div class="leftcolumn">
							   		<h2 class="subrow"><xsl:value-of select="ape:resource('eaccpf.portal.event')"/></h2>
							   	</div> 
							    <div class="rightcolumn"> 
									<xsl:call-template name="multilanguageNoP"> <!-- multilanguageEvent -->
							  			<xsl:with-param name="list" select="./eac:event"/>
							  			<xsl:with-param name="clazz" select="'language'"/>
							  		</xsl:call-template>
						  		</div>
					  		</div>
						</xsl:if>
					</div>
				</xsl:for-each>
				</div>
			</xsl:for-each>
		</xsl:if>
	</xsl:template>
	
	<!-- Template for select the correct order to display the alternative names
		 based in the value of the attribute "@localType". -->
	<xsl:template name="alternativeNamePriorisation">
		<xsl:param name="list"/>
		<xsl:param name="clazz"/>

		<div id="{$clazz}" class= "moreDisplay">
			<xsl:choose>
				<xsl:when test="count($list) > 1">
					<!-- Checks the attribute "@localType". -->
					<!-- localType = preferred -->
					<xsl:if test="$list[@localType = 'preferred']">
						<xsl:call-template name="alternativeName">
							<xsl:with-param name="list" select="$list[@localType = 'preferred']"/>
						</xsl:call-template> 
					</xsl:if>
					<!-- localType = authorized -->
					<xsl:if test="$list[@localType = 'authorized']">
						<xsl:call-template name="alternativeName">
							<xsl:with-param name="list" select="$list[@localType = 'authorized']"/>
						</xsl:call-template> 
					</xsl:if>
					<!-- not @localType -->
					<xsl:if test="$list[not(@localType)]">
						<xsl:call-template name="alternativeName">
							<xsl:with-param name="list" select="$list[not(@localType)]"/>
						</xsl:call-template> 
					</xsl:if>
					<!-- localType = alternative -->
					<xsl:if test="$list[@localType = 'alternative']">
						<xsl:call-template name="alternativeName">
							<xsl:with-param name="list" select="$list[@localType = 'alternative']"/>
						</xsl:call-template> 
					</xsl:if>
					<!-- localType = abbreviation -->
					<xsl:if test="$list[@localType = 'abbreviation']">
						<xsl:call-template name="alternativeName">
							<xsl:with-param name="list" select="$list[@localType = 'abbreviation']"/>
						</xsl:call-template> 
					</xsl:if>
					<!-- localType = other -->
					<xsl:if test="$list[@localType = 'other']">
						<xsl:call-template name="alternativeName">
							<xsl:with-param name="list" select="$list[@localType = 'other']"/>
						</xsl:call-template> 
					</xsl:if>
					<!-- In any other case checks the whole list. -->
					<xsl:if test="$list[not(@localType = 'preferred') and not(@localType = 'authorized') and not(@localType = 'alternative') and not(@localType = 'abbreviation') and not(@localType = 'other') and @localType]">
<!-- 					<xsl:if test="$list[not(@localType = 'preferred') and not(@localType = 'authorized') and not(@localType != 'alternative') and not(@localType = 'abbreviation') and not(@localType = 'other')] and $list[@localType]"> -->
<!-- 					<xsl:if test="$list[@localType != 'preferred' and @localType != 'authorized' and @localType != 'alternative' and @localType != 'abbreviation' and @localType != 'other'] and $list[not(@localType)]"> -->
						<xsl:call-template name="alternativeName">
							<xsl:with-param name="list" select="$list[not(@localType = 'preferred') and not(@localType = 'authorized') and not(@localType = 'alternative') and not(@localType = 'abbreviation') and not(@localType = 'other') and @localType]"/>
						</xsl:call-template> 
					</xsl:if>
				</xsl:when>

				<xsl:otherwise>
					<xsl:call-template name="alternativeName">
						<xsl:with-param name="list" select="$list"/>
					</xsl:call-template> 
				</xsl:otherwise>
			</xsl:choose>
		</div>
	</xsl:template>
	
	<!-- Template for alternative forms of name -->
	<xsl:template name="alternativeName">
		<xsl:param name="list"/>

		<xsl:for-each select="$list">
			<p>
				<xsl:call-template name="compositeName">
					<xsl:with-param name="listName" select="current()"/>
					<xsl:with-param name="isHeader" select="'false'"/>
				</xsl:call-template> 
		    </p>
		</xsl:for-each>
	</xsl:template>
	<!-- template places -->
	<xsl:template name="places">
		<xsl:param name="list"/>
	    <xsl:param name="clazz"/>
	    <xsl:param name="posParent"/>
	    <xsl:param name="posChild"/>
	    <xsl:param name="title"/>
		<xsl:if test="($list[@localType != 'foundation']/text() and $list[@localType != 'suppression']/text() and $list[@localType != 'birth']/text() and $list[@localType != 'death']/text()) or $list[not(@localType)]/text()">
<!-- 		<xsl:if test="($list[@localType != 'foundation' and @localType != 'suppression' and @localType != 'birth' and @localType != 'death']/text()) or $list[not(@localType)]/text()"> -->
			<div class="row locationPlace">
				 	<div class="leftcolumn">
				   		<h2 class="subrow"><xsl:value-of select="ape:resource($title)"/><xsl:text>:</xsl:text></h2>
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
		<xsl:if test="$list/text() or name($list[1])='citation'">	 
			<div class="row">
					<div class="leftcolumn">
				   		<h2 class="subrow"><xsl:value-of select="ape:resource($title)"/><xsl:text>:</xsl:text></h2>
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
			<xsl:if test="@href">
				<xsl:variable name="href" select="./@href"/>
				<a href="{$href}" target="_blank"><xsl:apply-templates select="." mode="other"/></a>
			</xsl:if>
		</xsl:if>
	</xsl:template>
	
	<!-- template for commons dates -->
	<xsl:template name="commonDates">
		<xsl:param name="date"/>
		<xsl:param name="dateRange"/>
		<xsl:param name="dateSet"/>
		<xsl:if test="$date or $dateRange or $dateSet">
		    <div class="row">
		    	<div class="leftcolumn">
				   		<h2 class="subrow"><xsl:value-of select="ape:resource('eaccpf.portal.date')"/><xsl:text>:</xsl:text></h2>
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

	<!-- Template for select the correct name to display as title based in the
		 value of the attribute "@localType". -->
	<xsl:template name="namePriorisation">
		<xsl:param name="list"/>

		<xsl:choose>
			<xsl:when test="count($list) > 1">
				<!-- Checks the attribute "@localType". -->
				<xsl:choose>
					<!-- localType = preferred -->
					<xsl:when test="$list[@localType = 'preferred']">
						<xsl:call-template name="multilanguageName">
							<xsl:with-param name="list" select="$list[@localType = 'preferred']"/>
						</xsl:call-template> 
					</xsl:when>
					<!-- localType = authorized -->
					<xsl:when test="$list[@localType = 'authorized']">
						<xsl:call-template name="multilanguageName">
							<xsl:with-param name="list" select="$list[@localType = 'authorized']"/>
						</xsl:call-template> 
					</xsl:when>
					<!-- not @localType -->
					<xsl:when test="$list[not(@localType)]">
						<xsl:call-template name="multilanguageName">
							<xsl:with-param name="list" select="$list[not(@localType)]"/>
						</xsl:call-template> 
					</xsl:when>
					<!-- localType = alternative -->
					<xsl:when test="$list[@localType = 'alternative']">
						<xsl:call-template name="multilanguageName">
							<xsl:with-param name="list" select="$list[@localType = 'alternative']"/>
						</xsl:call-template> 
					</xsl:when>
					<!-- localType = abbreviation -->
					<xsl:when test="$list[@localType = 'abbreviation']">
						<xsl:call-template name="multilanguageName">
							<xsl:with-param name="list" select="$list[@localType = 'abbreviation']"/>
						</xsl:call-template> 
					</xsl:when>
					<!-- localType = other -->
					<xsl:when test="$list[@localType = 'other']">
						<xsl:call-template name="multilanguageName">
							<xsl:with-param name="list" select="$list[@localType = 'other']"/>
						</xsl:call-template> 
					</xsl:when>
					<!-- In any other case checks the whole list. -->
					<xsl:otherwise>
						<xsl:call-template name="multilanguageName">
							<xsl:with-param name="list" select="$list"/>
						</xsl:call-template> 
					</xsl:otherwise>
				</xsl:choose>
			</xsl:when>

			<xsl:otherwise>
				<xsl:call-template name="multilanguageName">
					<xsl:with-param name="list" select="$list"/>
				</xsl:call-template> 
			</xsl:otherwise>
		</xsl:choose>
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
	    <xsl:call-template name="nameEntry">
	    	<xsl:with-param name="listName" select="$listName"/>
	    </xsl:call-template>
	    <xsl:if test="$isHeader = 'true'"> <!-- to display the alternative names -->
  			<span id="nameTitle" class="hidden">
  				<xsl:call-template name="nameEntry">
  					<xsl:with-param name="listName" select="$listName"/>
  				</xsl:call-template>
  			</span>
  		</xsl:if>	
	</xsl:template> 
	
	<!-- template nameEntry -->
	<xsl:template name="nameEntry">
		<xsl:param name="listName"/>
    	<xsl:variable name="firstName" select="$listName/eac:part[@localType='firstname']"/>
		<xsl:variable name="surName" select="$listName/eac:part[@localType='surname']"/>
		<xsl:variable name="patronymic" select="$listName/eac:part[@localType='patronymic']"/>
	    <xsl:variable name="prefix" select="$listName/eac:part[@localType='prefix']"/>
	    <xsl:variable name="suffix" select="$listName/eac:part[@localType='suffix']"/>
		<xsl:variable name="alias" select="$listName/eac:part[@localType='alias']"/>
		<xsl:variable name="title" select="$listName/eac:part[@localType='title']"/>
		<xsl:variable name="birthname" select="$listName/eac:part[@localType='birthname']"/>
		<xsl:variable name="legalform" select="$listName/eac:part[@localType='legalform']"/>
		<xsl:variable name="corpname" select="$listName/eac:part[@localType='corpname']"/>
		<xsl:variable name="famname" select="$listName/eac:part[@localType='famname']"/>
		<xsl:variable name="persname" select="$listName/eac:part[@localType='persname']"/>
    	<xsl:choose>
	    	<xsl:when test="not($corpname) and not($famname) and not($persname) and not($legalform) and not($listName/eac:part[not(@localType) or @localType=''])"> 
	    		<xsl:if test="$surName">
	    		<xsl:for-each select="$surName">
	    			<xsl:apply-templates select="." mode="other"/>
	    			<xsl:if test="position()!=last()">
	    				<xsl:text> </xsl:text>
	    			</xsl:if>
	    		</xsl:for-each>	
	    			<xsl:if test="$birthname">
	    				<xsl:text> </xsl:text>
	    			</xsl:if>
	    		</xsl:if>
	    		<xsl:if test="$birthname">
	    			<xsl:text>(</xsl:text>
	    		    <xsl:for-each select="$birthname"> 	
	    				<xsl:apply-templates select="." mode="other"/>
	    				<xsl:if test="position()!=last()">
	    					<xsl:text> </xsl:text>
	    				</xsl:if>
	    		  	</xsl:for-each>	  
	    			<xsl:text>)</xsl:text>
	    		</xsl:if>
	    		<xsl:if test="$prefix">
	    			<xsl:if test="$surName or $birthname">
	    				<xsl:text>, </xsl:text>
	    			</xsl:if>
	    			<xsl:for-each select="$prefix"> 	
	    				<xsl:apply-templates select="." mode="other"/>
	    				<xsl:if test="position()!=last()">
	    					<xsl:text> </xsl:text>
	    			    </xsl:if>
	    			</xsl:for-each>	  
	    		</xsl:if>
	    		<xsl:if test="$firstName">
	    			<xsl:if test="$surName or $birthname or $prefix">
	    				<xsl:text>, </xsl:text>
	    			</xsl:if>
	    			<xsl:for-each select="$firstName"> 	
	    				<xsl:apply-templates select="." mode="other"/>
	    				<xsl:if test="position()!=last()">
	    					<xsl:text> </xsl:text>
	    			    </xsl:if>
	    			</xsl:for-each>	
	    		</xsl:if>
	    		<xsl:if test="$patronymic">
	    			<xsl:choose>
	    				<xsl:when test="$firstName">
	    					<xsl:text> </xsl:text>
	    				</xsl:when>
	    				<xsl:otherwise>
	    					<xsl:if test="$surName or $birthname or $prefix">
			    				<xsl:text>, </xsl:text>
			    			</xsl:if>
	    				</xsl:otherwise>
	    			</xsl:choose>
	    			<xsl:for-each select="$patronymic"> 	
	    				<xsl:apply-templates select="." mode="other"/>
	    				<xsl:if test="position()!=last()">
	    					<xsl:text> </xsl:text>
	    			    </xsl:if>
	    			</xsl:for-each>	
	    		</xsl:if>
	    		<xsl:if test="$suffix">
	    			<xsl:if test="$surName or $birthname or $prefix or $firstName or $patronymic">
	    				<xsl:text>, </xsl:text>
	    			</xsl:if>
	    			<xsl:for-each select="$suffix"> 	
	    				<xsl:apply-templates select="." mode="other"/>
	    				<xsl:if test="position()!=last()">
	    					<xsl:text> </xsl:text>
	    			    </xsl:if>
	    			</xsl:for-each>	
	    		</xsl:if>
	    		<xsl:if test="$title">
	    			<xsl:if test="$surName or $birthname or $prefix or $firstName or $patronymic or $suffix">
	    				<xsl:text>, </xsl:text>
	    			</xsl:if>
	    			<xsl:for-each select="$title"> 	
	    				<xsl:apply-templates select="." mode="other"/>
	    				<xsl:if test="position()!=last()">
	    					<xsl:text> </xsl:text>
	    			    </xsl:if>
	    			</xsl:for-each>	
	    		</xsl:if>
	    	</xsl:when>
	    	<xsl:otherwise>
	    		<xsl:choose>
	    			<xsl:when test="$corpname and $legalform">
	    				<xsl:apply-templates select="$corpname" mode="other"/>
	    				<xsl:text> </xsl:text>
	    				<xsl:apply-templates select="$legalform" mode="other"/>
	    			</xsl:when>
	    			<xsl:otherwise>
	    				<xsl:apply-templates select="$listName/eac:part[1]" mode="other"/>
	    			</xsl:otherwise>
	    		</xsl:choose>
	    	</xsl:otherwise>
	    </xsl:choose>
	    <xsl:if test="$alias">
	    	<xsl:if test="$surName or $birthname or $prefix or $firstName or $patronymic or $suffix or $title or $corpname or $famname or $persname">
   				<xsl:text> </xsl:text> 
   			</xsl:if>
	   		<xsl:text>(</xsl:text><xsl:value-of select="ape:resource('eaccpf.portal.alias')"/><xsl:text>: </xsl:text>
	   		<xsl:for-each select="$alias"> 	
   				<xsl:apply-templates select="." mode="other"/>
   				<xsl:if test="position()!=last()">
   					<xsl:text> </xsl:text>
   			    </xsl:if>
   			</xsl:for-each>	
	   		<xsl:text>)</xsl:text>
	    </xsl:if>
	</xsl:template>
	
	<!-- template vocabularySource -->
	<xsl:template name="vocabularySource">
		<xsl:param name="node"/>
		<xsl:variable name="link" select="$node/@vocabularySource"/>
		<xsl:if test="not(starts-with($link, 'http')) and not(starts-with($link, 'https')) and not(starts-with($link, 'ftp')) and not(starts-with($link, 'www'))">
			<xsl:variable name="aiCodeEac" select="ape:aiFromEac($link, '')"/>
			<xsl:variable name="aiCodeEad" select="ape:aiFromEad($link, '')"/>
			<xsl:variable name="aiCodeEag" select="ape:checkAICode($link, '', 'true')" />
			<xsl:choose>
				<xsl:when test="$aiCodeEac != 'ERROR' and $aiCodeEac != ''">
					<xsl:variable name="encodedAiCode" select="ape:encodeSpecialCharacters($aiCodeEac)" />
					<xsl:variable name="encodedlink" select="ape:encodeSpecialCharacters($link)" />
					<a href="{$eacUrlBase}/aicode/{$encodedAiCode}/type/ec/id/{$encodedlink}" target="_blank">
						<xsl:apply-templates select="$node" mode="other"/>
					</a>
				</xsl:when>
				<xsl:when test="$aiCodeEad != 'ERROR' and $aiCodeEad != ''">
					<a href="{$eadUrl}/{$aiCodeEad}" target="_blank">
						<xsl:apply-templates select="$node" mode="other"/>
					</a>
				</xsl:when>
				<xsl:when test="$aiCodeEag != 'ERROR' and $aiCodeEag != ''">
					<xsl:variable name="encodedAiCode" select="ape:encodeSpecialCharacters($aiCodeEag)" />
					<a href="{$aiCodeUrl}/{$encodedAiCode}" target="_blank">
						<xsl:apply-templates select="$node" mode="other"/>
					</a>
				</xsl:when>
				<xsl:otherwise>
					<xsl:apply-templates select="$node" mode="other"/>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:if> 
		<xsl:if test="starts-with($link, 'http') or starts-with($link, 'https') or starts-with($link, 'ftp') or starts-with($link, 'www')">
			<a href="{$link}" target="_blank">
				<xsl:apply-templates select="$node" mode="other"/>
			</a>
		</xsl:if>	
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
										<xsl:call-template name="vocabularySource">
											<xsl:with-param name="node" select="."/>
										</xsl:call-template>
									</xsl:when>
									<xsl:when test="name(current()) = 'citation'">
										<xsl:call-template name="citationHref">
											<xsl:with-param name="link" select="./@xlink:href"/>
											<xsl:with-param name="title" select="./text()"/>
										</xsl:call-template>
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
										<xsl:call-template name="vocabularySource">
											<xsl:with-param name="node" select="."/>
										</xsl:call-template>
									</xsl:when>
									<xsl:when test="name(current()) = 'citation'">
										<xsl:call-template name="citationHref">
											<xsl:with-param name="link" select="./@xlink:href"/>
											<xsl:with-param name="title" select="./text()"/>
										</xsl:call-template>
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
										<xsl:call-template name="vocabularySource">
											<xsl:with-param name="node" select="."/>
										</xsl:call-template>
									</xsl:when>
									<xsl:when test="name(current()) = 'citation'">
										<xsl:call-template name="citationHref">
											<xsl:with-param name="link" select="./@xlink:href"/>
											<xsl:with-param name="title" select="./text()"/>
										</xsl:call-template>
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
												    <xsl:call-template name="vocabularySource">
														<xsl:with-param name="node" select="."/>
													</xsl:call-template>
												</xsl:when>
												<xsl:when test="name(current()) = 'citation'">
													<xsl:call-template name="citationHref">
														<xsl:with-param name="link" select="./@xlink:href"/>
														<xsl:with-param name="title" select="./text()"/>
													</xsl:call-template>
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
			</div>
			</xsl:when>
			<xsl:otherwise>
				<xsl:for-each select="$list">
					<xsl:choose>
						<xsl:when test="@vocabularySource">
							<xsl:call-template name="vocabularySource">
								<xsl:with-param name="node" select="."/>
							</xsl:call-template>
						</xsl:when>
						<xsl:when test="name(current()) = 'citation'">
							<xsl:call-template name="citationHref">
								<xsl:with-param name="link" select="./@xlink:href"/>
								<xsl:with-param name="title" select="./text()"/>
							</xsl:call-template>
						</xsl:when>
						<xsl:otherwise>
						 	<xsl:apply-templates select="." mode="other"/> 
						</xsl:otherwise>
					</xsl:choose>
				</xsl:for-each>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

		<!-- template for citation with/without @href -->
	<xsl:template name="citationHref">
		<xsl:param name="link"/>
		<xsl:param name="title"/>
		<xsl:choose>
			<!--link ok -->
			<xsl:when test="starts-with($link, 'http') or starts-with($link, 'https') or starts-with($link, 'ftp') or starts-with($link, 'www')">
				<a href="{$link}" target="_blank">
				<xsl:choose>
					<xsl:when test="$title != ''">
						<xsl:value-of select="$title"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="ape:resource('eaccpf.portal.seeCitation')"/>
					</xsl:otherwise>
				</xsl:choose>
				</a>
			</xsl:when>
			<!-- internal links -->
			<xsl:when test="not(starts-with($link, 'http')) and 
							not(starts-with($link, 'https')) and 
							not(starts-with($link, 'ftp')) and 
							not(starts-with($link, 'www'))">
				<xsl:variable name="aiCodeEac" select="ape:aiFromEac($link, '')"/>
				<xsl:variable name="aiCodeEad" select="ape:aiFromEad($link, '')"/>
				<xsl:variable name="aiCodeEag" select="ape:checkAICode($link, '', 'true')" />
				<xsl:choose>
					<xsl:when test="$aiCodeEac != 'ERROR' and $aiCodeEac != ''">
						<xsl:variable name="encodedAiCode" select="ape:encodeSpecialCharacters($aiCodeEac)" />
						<xsl:variable name="encodedlink" select="ape:encodeSpecialCharacters($link)" />
						<a href="{$eacUrlBase}/aicode/{$encodedAiCode}/type/ec/id/{$encodedlink}" target="_blank">
							<xsl:value-of select="$title"/>
						</a>
					</xsl:when>
					<xsl:when test="$aiCodeEad != 'ERROR' and $aiCodeEad != ''">
						<a href="{$eadUrl}/{$aiCodeEad}" target="_blank">
							<xsl:value-of select="$title"/>
						</a>
					</xsl:when>
					<xsl:when test="$aiCodeEag != 'ERROR' and $aiCodeEag != ''">
						<xsl:variable name="encodedAiCode" select="ape:encodeSpecialCharacters($aiCodeEag)" />
						<a href="{$aiCodeUrl}/{$encodedAiCode}" target="_blank">
							<xsl:value-of select="$title"/>
						</a>
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="$title"/>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
			<!--link ko -->
			<xsl:otherwise>
				<xsl:choose>
					<xsl:when test="$title != ''">
						<xsl:value-of select="$title"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="ape:resource('eaccpf.portal.seeCitation')"/>
					</xsl:otherwise>
				</xsl:choose>
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
									<xsl:when test="@vocabularySource">
										<xsl:call-template name="vocabularySource">
											<xsl:with-param name="node" select="."/>
										</xsl:call-template>
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
									<xsl:when test="@vocabularySource">
										<xsl:call-template name="vocabularySource">
											<xsl:with-param name="node" select="."/>
										</xsl:call-template>
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
										<xsl:when test="@vocabularySource">
											<xsl:call-template name="vocabularySource">
												<xsl:with-param name="node" select="."/>
										    </xsl:call-template>
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
												<xsl:when test="@vocabularySource">
													<xsl:call-template name="vocabularySource">
														<xsl:with-param name="node" select="."/>
										   		    </xsl:call-template>
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
							<xsl:call-template name="vocabularySource">
								<xsl:with-param name="node" select="."/>
				   		    </xsl:call-template>
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
										<xsl:call-template name="vocabularySource">
											<xsl:with-param name="node" select="."/>
				   		   			    </xsl:call-template>
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
										<xsl:call-template name="vocabularySource">
											<xsl:with-param name="node" select="."/>
				   		   			    </xsl:call-template>
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
											<xsl:call-template name="vocabularySource">
												<xsl:with-param name="node" select="."/>
				   		   			        </xsl:call-template>
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
											<xsl:call-template name="vocabularySource">
												<xsl:with-param name="node" select="."/>
				   		   			        </xsl:call-template>
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
			</div>
			</xsl:when>
			<xsl:otherwise>
				<xsl:for-each select="$list">
					<p>
						<xsl:choose>
							<xsl:when test="@vocabularySource">
								<xsl:call-template name="vocabularySource">
									<xsl:with-param name="node" select="."/>
			   			        </xsl:call-template>
							</xsl:when>
							<xsl:otherwise>
							    <xsl:apply-templates select="." mode="other"/> 
							</xsl:otherwise>
						</xsl:choose>
					</p>
				</xsl:for-each>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<!-- Template for toDate or fromDate to detect the unknow value-->
	<xsl:template name="dateUnknow"> 
		<!-- dateUnknow gets fromDate or toDate -->
		<xsl:param name="dateUnknow"/>
	  	<xsl:choose>
	  		<!-- when it is void or it does not exist -->
	  		<xsl:when test="$dateUnknow='' or not($dateUnknow)">
        		<xsl:value-of select="ape:resource('eaccpf.commons.dateUnknow')"/>
        	</xsl:when>
        	<xsl:when test="$dateUnknow/parent::node()[@localType='open'] and $dateUnknow/text() = 'open'">
			</xsl:when>
        	<!-- unknownStart, unknownEnd and both -->
        	<xsl:otherwise>
        		<xsl:choose>
		        	<xsl:when test="name($dateUnknow) = 'fromDate'">
		        		<xsl:choose>
							<xsl:when test="$dateUnknow/parent::node()[@localType='unknown' or @localType='unknownStart']">
		        				<xsl:value-of select="ape:resource('eaccpf.commons.dateUnknow')"/>
							</xsl:when>
							<xsl:when test="$dateUnknow/parent::node()[@localType='open'] and $dateUnknow/text() = 'open'">
		        				<xsl:text> </xsl:text>
							</xsl:when>
							<xsl:otherwise>
								<xsl:apply-templates select="$dateUnknow" mode="other"/> 
							</xsl:otherwise>
						</xsl:choose>	
		        	</xsl:when>
	        		<xsl:when test="name($dateUnknow) = 'toDate'">
		        		<xsl:choose>
							<xsl:when test="$dateUnknow/parent::node()[@localType='unknown' or @localType='unknownEnd']">
	        					<xsl:value-of select="ape:resource('eaccpf.commons.dateUnknow')"/>
							</xsl:when>
							<xsl:when test="$dateUnknow/parent::node()[@localType='open'] and $dateUnknow/text() = 'open'">
		        				<xsl:text> </xsl:text>
							</xsl:when>
							<xsl:otherwise>
								<xsl:apply-templates select="$dateUnknow" mode="other"/> 
							</xsl:otherwise>
						</xsl:choose>
        			</xsl:when>
					<xsl:otherwise>
	        			<xsl:apply-templates select="$dateUnknow" mode="other"/>
		        	</xsl:otherwise>
	        	</xsl:choose>
        	</xsl:otherwise>
		</xsl:choose> 
	</xsl:template>

	<!-- template for dateSet -->
	<xsl:template match="eac:dateSet">
		<xsl:if test="eac:dateRange or eac:date">
			<xsl:call-template name="multilanguageDate">
				<xsl:with-param name="list" select="eac:date"/>
			</xsl:call-template>
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
		    			       	<xsl:choose>
									<xsl:when test="current()[@localType='unknown' or @localType='unknownStart' or @localType='unknownEnd']">
										<xsl:value-of select="ape:resource('eaccpf.commons.dateUnknow')"/>
									</xsl:when>
									<xsl:when test="current()[@localType='open']">
									</xsl:when>
									<xsl:otherwise>
										<xsl:apply-templates select="." mode="other"/> 
									</xsl:otherwise>
								</xsl:choose>
							    <xsl:if test="position() != last() and current()[not(@localType ='open')]">
									<xsl:text>, </xsl:text>
								</xsl:if>
							</xsl:if>
						</xsl:for-each>
					</xsl:when>
					<xsl:when test="$list[@xml:lang = $language.default] and $list[@xml:lang = $language.default]/text() and $list[@xml:lang = $language.default]/text() != ''">
						<xsl:for-each select="$list[@xml:lang = $language.default]">
							<xsl:if test="current()/text()">
   		    			       	<xsl:choose>
									<xsl:when test="current()[@localType='unknown' or @localType='unknownStart' or @localType='unknownEnd']">
										<xsl:value-of select="ape:resource('eaccpf.commons.dateUnknow')"/>
									</xsl:when>
									<xsl:when test="current()[@localType='open']">
									</xsl:when>
									<xsl:otherwise>
										<xsl:apply-templates select="." mode="other"/> 
									</xsl:otherwise>
								</xsl:choose>
							    <xsl:if test="position() != last() and current()[not(@localType ='open')]">
									<xsl:text>, </xsl:text>
								</xsl:if>
							</xsl:if>
						</xsl:for-each>
					</xsl:when>
					<xsl:when test="$list[not(@xml:lang)] and $list[not(@xml:lang)]/text() and $list[not(@xml:lang)]/text() != ''">
						  	<xsl:for-each select="$list[not(@xml:lang)]"> 
								<xsl:if test="current()/text()">
	   		    			       	<xsl:choose>
										<xsl:when test="current()[@localType='unknown' or @localType='unknownStart' or @localType='unknownEnd']">
											<xsl:value-of select="ape:resource('eaccpf.commons.dateUnknow')"/>
										</xsl:when>
										<xsl:when test="current()[@localType='open']">
										</xsl:when>
										<xsl:otherwise>
											<xsl:apply-templates select="." mode="other"/> 
										</xsl:otherwise>
									</xsl:choose>
								    <xsl:if test="position() != last() and current()[not(@localType ='open')]">
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
    		    			       	<xsl:choose>
										<xsl:when test="current()[@localType='unknown' or @localType='unknownStart' or @localType='unknownEnd']">
											<xsl:value-of select="ape:resource('eaccpf.commons.dateUnknow')"/>
										</xsl:when>
										<xsl:when test="current()[@localType='open']">
										</xsl:when>
										<xsl:otherwise>
											<xsl:apply-templates select="." mode="other"/> 
										</xsl:otherwise>
									</xsl:choose>
								    <xsl:if test="position() != last() and current()[not(@localType ='open')]">
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
			       	<xsl:choose>
						<xsl:when test="current()[@localType='unknown' or @localType='unknownStart' or @localType='unknownEnd']">
							<xsl:value-of select="ape:resource('eaccpf.commons.dateUnknow')"/>
						</xsl:when>
						<xsl:when test="current()[@localType='open']">
						</xsl:when>
						<xsl:otherwise>
							<xsl:apply-templates select="." mode="other"/> 
						</xsl:otherwise>
					</xsl:choose>
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
			<xsl:when test="$dateRange[@localType='unknown' or @localType='unknownStart']">
				<xsl:value-of select="ape:resource('eaccpf.commons.dateUnknow')"/>
			</xsl:when>
			<xsl:when test="$dateRange[@localType='open'] and $dateRange/eac:fromDate/text() = 'open'">
			</xsl:when>
			<xsl:otherwise>
				<xsl:apply-templates select="$dateRange/eac:fromDate" mode="other"/> 
			</xsl:otherwise>
		</xsl:choose>

		<xsl:choose>
			<xsl:when test="($dateRange[@localType!='unknown']
							or $dateRange[not(@localType)])
							and ($dateRange/eac:toDate
								or $dateRange[@localType='unknownEnd']
				                or not($dateRange/eac:toDate) 
				                or not($dateRange/eac:fromDate)
				                or $dateRange[@localType='open'] 
				                or $dateRange/eac:fromDate/text() = 'open')">
				<xsl:text> - </xsl:text>
			</xsl:when>
			<xsl:otherwise>
				<xsl:choose>
					<xsl:when test="$dateRange[@localType='unknown']">
					</xsl:when>
					<xsl:when test="$dateRange[@localType='open'] and $dateRange/eac:fromDate/text() = 'open'">
					</xsl:when>
					<xsl:otherwise>
						<xsl:text> </xsl:text>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:otherwise>
		</xsl:choose>

		<xsl:choose>	
			<xsl:when test="$dateRange[@localType='unknownEnd']">
				<xsl:value-of select="ape:resource('eaccpf.commons.dateUnknow')"/>
			</xsl:when>
			<xsl:when test="$dateRange[@localType='open'] and $dateRange/eac:toDate/text() = 'open'">
			</xsl:when>
		    <xsl:otherwise>
		    	<xsl:if  test="$dateRange[@localType='unknownStart' or @localType=''] or $dateRange[not(@localType)]"> 
		    		<xsl:apply-templates select="$dateRange/eac:toDate" mode="other"/> 
		   		</xsl:if>
		   		<xsl:if  test="$dateRange[@localType='open'] and $dateRange/eac:fromDate/text() = 'open'"> 
		    		<xsl:apply-templates select="$dateRange/eac:toDate" mode="other"/> 
		   		</xsl:if>
		    </xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<!-- template for date -->
	<xsl:template match="eac:date">
		<xsl:if test="./text()">
			<xsl:if test="position() != last() and current()[not(@localType ='open')]">
				<xsl:text>, </xsl:text>
			</xsl:if>
			<xsl:choose>
				<xsl:when test="current()[@localType='unknown' or @localType='unknownStart' or @localType='unknownEnd']">
					<xsl:value-of select="ape:resource('eaccpf.commons.dateUnknow')"/>
				</xsl:when>
				<xsl:when test="current()[@localType='open']">
				</xsl:when>
				<xsl:otherwise>
					<xsl:apply-templates mode="other"/>
				</xsl:otherwise>
			</xsl:choose>
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
			   						<xsl:if test="name(current()) = 'language'">
										<xsl:if test="./parent::node()/eac:descriptiveNote/eac:p/text() ">
											<xsl:text> (</xsl:text>
											<xsl:call-template  name="multilanguageNoP">
									   			<xsl:with-param name="list" select="./parent::node()/eac:descriptiveNote/eac:p"/>
									   			<xsl:with-param name="clazz" select="'language'"/>
											</xsl:call-template>
											<xsl:text>)</xsl:text>
										</xsl:if>
									</xsl:if>
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
			   						<xsl:if test="name(current()) = 'language'">
										<xsl:if test="./parent::node()/eac:descriptiveNote/eac:p/text() ">
											<xsl:text> (</xsl:text>
											<xsl:call-template  name="multilanguageNoP">
									   			<xsl:with-param name="list" select="./parent::node()/eac:descriptiveNote/eac:p"/>
									   			<xsl:with-param name="clazz" select="'language'"/>
											</xsl:call-template>
											<xsl:text>)</xsl:text>
										</xsl:if>
									</xsl:if>
								</p>
							</xsl:if>
						</xsl:for-each>
					</xsl:when>
					<xsl:when test="$list[not(@xml:lang)] and $list[not(@xml:lang)]/text() and $list[not(@xml:lang)]/text() != ''">
						  	<xsl:for-each select="$list[not(@xml:lang)]"> 
								<p>
								  	<xsl:apply-templates select="." mode="other"/>
		   							<xsl:if test="name(current()) = 'language'">
									<xsl:if test="./parent::node()/eac:descriptiveNote/eac:p/text() ">
										<xsl:text> (</xsl:text>
										<xsl:call-template  name="multilanguageNoP">
								   			<xsl:with-param name="list" select="./parent::node()/eac:descriptiveNote/eac:p"/>
								   			<xsl:with-param name="clazz" select="'language'"/>
										</xsl:call-template>
										<xsl:text>)</xsl:text>
									</xsl:if>
								</xsl:if>
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
			   							<xsl:if test="name(current()) = 'language'">
										<xsl:if test="./parent::node()/eac:descriptiveNote/eac:p/text() ">
											<xsl:text> (</xsl:text>
											<xsl:call-template  name="multilanguageNoP">
									   			<xsl:with-param name="list" select="./parent::node()/eac:descriptiveNote/eac:p"/>
									   			<xsl:with-param name="clazz" select="'language'"/>
											</xsl:call-template>
											<xsl:text>)</xsl:text>
										</xsl:if>
									</xsl:if>	 
								</p>			
							</xsl:if>
						</xsl:for-each>
					</xsl:otherwise>
				</xsl:choose>
			</div>
			</xsl:when>
			<xsl:otherwise>
				<xsl:for-each select="$list">
					<p>
						<xsl:apply-templates mode="other"/>
						<xsl:if test="name(current()) = 'language'">
							<xsl:if test="./parent::node()/eac:descriptiveNote/eac:p/text() ">
								<xsl:text> (</xsl:text>
								<xsl:call-template  name="multilanguageNoP">
						   			<xsl:with-param name="list" select="./parent::node()/eac:descriptiveNote/eac:p"/>
						   			<xsl:with-param name="clazz" select="'language'"/>
								</xsl:call-template>
								<xsl:text>)</xsl:text>
							</xsl:if>
						</xsl:if>
					</p>
				</xsl:for-each>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<!-- template for multilanguage wit no <p> -->
	<xsl:template name="multilanguageNoP">
		<xsl:param name="list"/>
		<xsl:param name="clazz"/>	
		<xsl:choose>
			<!--  -->
			<xsl:when test="count($list) > 1">
				<xsl:choose>
					
					<!-- lang equals selected language -->
					<xsl:when test="$list[@xml:lang = $language.selected] and $list[@xml:lang = $language.selected]/text() and $list[@xml:lang = $language.selected]/text() != ''">
						<xsl:variable name="listLangSelected" select="$list[@xml:lang = $language.selected]"/>
						<xsl:for-each select="$listLangSelected">
							<xsl:if test="position() > 1">
								<xsl:text> </xsl:text>
							</xsl:if>
						    <xsl:apply-templates mode="other"/> 
						</xsl:for-each>
					</xsl:when>
					
					<!-- default language -->
					<xsl:when test="$list[@xml:lang = $language.default] and $list[@xml:lang = $language.default]/text() and $list[@xml:lang = $language.default]/text() != ''">
						<xsl:variable name="listLangDefault" select="$list[@xml:lang = $language.default]"/>
						<xsl:for-each select="$listLangDefault">
							<xsl:if test="position() > 1">
								<xsl:text> </xsl:text>
							</xsl:if>
						    <xsl:apply-templates mode="other"/> 
						</xsl:for-each>
					</xsl:when>
				  	
				  	<!-- no lang -->
					<xsl:when test="$list[not(@xml:lang)] and $list[not(@xml:lang)]/text() and $list[not(@xml:lang)]/text() != ''">
					  	<xsl:variable name="listNoLang" select="$list[not(@xml:lang)]"/>
					  	<xsl:for-each select="$listNoLang"> 
							<xsl:if test="position() > 1">
								<xsl:text> </xsl:text>
							</xsl:if>
						  	<xsl:apply-templates select="." mode="other"/>
					   	</xsl:for-each> 
					</xsl:when>
					
					<!-- first language -->
					<xsl:otherwise> 
						<xsl:variable name="language.first" select="$list[1]/@xml:lang"></xsl:variable>
						<xsl:variable name="listFirstLang" select="$list[@xml:lang = $language.first]"/>
						<xsl:for-each select="$listFirstLang">
							<xsl:if test="position() > 1">
								<xsl:text> </xsl:text>
							</xsl:if>
							<xsl:apply-templates select="." mode="other"/> 
						</xsl:for-each>
					</xsl:otherwise>
					
				</xsl:choose>
			</xsl:when>
			<!-- 1 element -->
			<xsl:otherwise>
				<xsl:for-each select="$list">
					<xsl:apply-templates mode="other"/>
				</xsl:for-each>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	
	<!--template for outline -->
	<xsl:template name="outline">
		<xsl:param name="list"/>
		<xsl:param name="clazz"/>
		<xsl:param name="title"/>
		<xsl:if test="$list/eac:outline/eac:level/eac:item/text()"> 
		   	<div class="row">
				<div class="leftcolumn">
			   		<h2 class="subrow"><xsl:value-of select="ape:resource($title)"/><xsl:text>:</xsl:text></h2>
			   	</div>
			   	<div class="rightcolumn moreDisplay" id="{$clazz}">
			   		<xsl:call-template name="multilanguageOutline">
			   			<xsl:with-param name="list" select="$list/eac:outline/eac:level"/>
			   		</xsl:call-template>
				</div>
		  </div>
		</xsl:if>
	</xsl:template>
	
	<!-- template for <p> in structureOrGenealogy or generalContext-->
	<xsl:template name="p">
		<xsl:param name="list"/>
		<xsl:param name="clazz"/>
		<xsl:if test="$list/eac:p/text()">
	    	<div class="row">
					<div class="leftcolumn">
				   		<h2 class="subrow"><xsl:value-of select="ape:resource('eaccpf.portal.note')"/><xsl:text>:</xsl:text></h2>
				   	</div>
				   	<div class="rightcolumn">
						<xsl:call-template name="multilanguage">
				   			<xsl:with-param name="list" select="$list/eac:p"/>
				   			<xsl:with-param name="clazz" select="$clazz"/>
				   		</xsl:call-template>
					</div>
			</div>
	    </xsl:if>
	</xsl:template>
	
	<!-- template for outline -->
	<xsl:template name="multilanguageOutline">
		<xsl:param name="list"/><!-- outline/level -->
		<xsl:choose>
		    <!-- selected language -->
			<xsl:when test="$list/descendant-or-self::node()/eac:item[@xml:lang = $language.selected] and $list/descendant-or-self::node()/eac:item[@xml:lang = $language.selected]/text() and $list/descendant-or-self::node()/eac:item[@xml:lang = $language.selected]/text() != ''">
				<xsl:call-template name="multilanguageOutlineRecursive">
					<xsl:with-param name="list" select="$list"/>
					<xsl:with-param name="count" select="1"/> <!-- count the number of white spaces to display in the tree -->
					<xsl:with-param name="language" select="$language.selected"/>
				</xsl:call-template>
			</xsl:when> 
			<!-- default language (english) -->
			<xsl:when test="$list/descendant-or-self::node()/eac:item[@xml:lang = $language.default] and $list/descendant-or-self::node()/eac:item[@xml:lang = $language.default]/text() and $list/descendant-or-self::node()/eac:item[@xml:lang = $language.default]/text() != ''">
				<xsl:call-template name="multilanguageOutlineRecursive">
					<xsl:with-param name="list" select="$list"/>
					<xsl:with-param name="count" select="1"/> <!-- count the number of white spaces to display in the tree -->
					<xsl:with-param name="language" select="$language.default"/>
				</xsl:call-template>
			</xsl:when>
			<!-- not lang -->
			<xsl:when test="$list/descendant-or-self::node()/eac:item[not(@xml:lang)] and $list/descendant-or-self::node()/eac:item[not(@xml:lang)]/text() and $list/descendant-or-self::node()/eac:item[not(@xml:lang)]/text() != ''">
				<xsl:call-template name="multilanguageOutlineRecursive">
					<xsl:with-param name="list" select="$list"/>
					<xsl:with-param name="count" select="1"/> <!-- count the number of white spaces to display in the tree -->
					<xsl:with-param name="language" select="'notLang'"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise> <!-- First language -->
				<xsl:variable name="language.first" select="$list[1]/eac:item/@xml:lang"/>
				<xsl:call-template name="multilanguageOutlineRecursive">
					<xsl:with-param name="list" select="$list"/>
					<xsl:with-param name="count" select="1"/> <!-- count the number of white spaces to display in the tree -->
					<xsl:with-param name="language" select="$language.first"/>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>	
	</xsl:template>
	
	<!-- template for outline recursive -->
	<xsl:template name="multilanguageOutlineRecursive">
		<xsl:param name="list"/>
		<xsl:param name="count"/>
		<xsl:param name="language"/>
			 <xsl:for-each select="$list">
				 	<xsl:if test="./descendant-or-self::node()/eac:item[@xml:lang = $language] or ($language='notLang' and ./descendant-or-self::node()/eac:item[not(@xml:lang)])"> 
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
						<xsl:call-template name="multilanguageOutlineRecursive">
							<xsl:with-param name="list" select="."/>
							<xsl:with-param name="count" select="$count+1"/>
							<xsl:with-param name="language" select="$language"/>
						</xsl:call-template>
					</xsl:for-each> 
				</xsl:for-each> 
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