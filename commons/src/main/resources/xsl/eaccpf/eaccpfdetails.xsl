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
	<xsl:param name="eac.repository.code"/>
	<xsl:variable name="language.default" select="'eng'"/>
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
			
			<!-- when there are only 1 dateSet -->
			<xsl:if test="$existDates/eac:dateSet and (($existDates/eac:dateSet/eac:dateRange and $existDates/eac:dateSet/eac:dateRange/eac:fromDate/text() and $existDates/eac:dateSet/eac:dateRange/eac:toDate/text()) or ($existDates/eac:dateSet/eac:date and $existDates/eac:dateSet/eac:date/text()))">
				<xsl:text> (</xsl:text>
				<xsl:apply-templates select="$existDates/eac:dateSet"/>
				<xsl:text>)</xsl:text>
			</xsl:if>
			<!-- when there are only 1 dateRange -->
			<xsl:if test="$existDates/eac:dateRange and $existDates/eac:dateRange/eac:fromDate/text() and $existDates/eac:dateRange/eac:toDate/text()">
				<xsl:text> (</xsl:text>
				<xsl:apply-templates select="$existDates/eac:dateRange"/>
				<xsl:text>)</xsl:text>
			</xsl:if>
			<!-- when there are only 1 date -->
			<xsl:if test="$existDates/eac:date and $existDates/eac:date/text()">
				<xsl:text> (</xsl:text>
				<xsl:apply-templates select="$existDates/eac:date"/>
				<xsl:text>)</xsl:text>
			</xsl:if>
			
			<xsl:if test="./eac:eac-cpf/eac:cpfDescription/eac:identity/eac:entityType/text()">
				<xsl:text> </xsl:text>
				<span>
					<xsl:apply-templates select="$entityType" mode="other"/>
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
								<xsl:if test="$existDates/eac:dateSet/eac:dateRange">
									<xsl:call-template name="dateUnknow">
										<xsl:with-param name="dateUnknow" select="$existDates/eac:dateSet/eac:dateRange/eac:fromDate"/>
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
								<xsl:if test="$existDates/eac:dateSet/eac:dateRange">
									<xsl:call-template name="dateUnknow">
										<xsl:with-param name="dateUnknow" select="$existDates/eac:dateSet/eac:dateRange/eac:toDate"/>
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
			<xsl:if test="$entityType='corporateBody'">
				<xsl:if test="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:places/eac:place">
					<h2 class="title"><xsl:value-of select="ape:resource('eaccpf.portal.location')"/></h2>
					<xsl:for-each select="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:places/eac:place">
					    <!-- placeEntry in localDescription -->
						<xsl:call-template name="commonChild">
				    		<xsl:with-param name="list" select="./eac:placeEntry"/>
				    		<xsl:with-param name="clazz" select="'locationPlace'"/>
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
				     </xsl:for-each>	   
				</xsl:if>
			</xsl:if>
			<!-- localDescription -->
			<xsl:if test="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:localDescriptions/eac:localDescription">
			    <h2 class="title"><xsl:value-of select="ape:resource('eaccpf.portal.localDescription')"/></h2>
				<xsl:for-each select="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:localDescriptions"> 
				    <xsl:for-each select="./eac:localDescription">
				    		<!-- term localDescription -->
					    	<xsl:call-template name="term">
					    		<xsl:with-param name="list" select="./eac:term"/>
					    		<xsl:with-param name="clazz" select="'localDescription'"/>
					    		<xsl:with-param name="title" select="'eaccpf.portal.localDescription'"/>
					    	</xsl:call-template>
							<!-- placeEntry in localDescription -->
							<xsl:call-template name="commonChild">
					    		<xsl:with-param name="list" select="./eac:placeEntry"/>
					    		<xsl:with-param name="clazz" select="'locationLocalDescription'"/>
					    		<xsl:with-param name="title" select="'eaccpf.portal.location'"/>
					    	</xsl:call-template>
							<!-- citation in localDescription -->
						  	<xsl:call-template name="commonChild">
					    		<xsl:with-param name="list" select="./eac:citation"/>
					    		<xsl:with-param name="clazz" select="'citationLocalDescription'"/>
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
					    		<xsl:with-param name="clazz" select="'descriptiveNoteLocalDescription'"/>
					    		<xsl:with-param name="title" select="'eaccpf.portal.note'"/>
					    	</xsl:call-template>
					</xsl:for-each>
						<!-- descriptiveNote in localDescriptions -->
						 <xsl:call-template name="commonChild">
				    		<xsl:with-param name="list" select="./eac:descriptiveNote/eac:p"/>
				    		<xsl:with-param name="clazz" select="'descriptiveNoteLocalDescriptions'"/>
				    		<xsl:with-param name="title" select="'eaccpf.portal.note'"/>
				    	 </xsl:call-template>
				</xsl:for-each>		
		   </xsl:if>
		   <!-- legalStatus -->
		   <xsl:if test="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:legalStatuses/eac:legalStatus">
			    <h2 class="title"><xsl:value-of select="ape:resource('eaccpf.portal.legalStatus')"/></h2>
				<xsl:for-each select="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:legalStatuses"> 
				    <xsl:for-each select="./eac:legalStatus">
				    		<!-- term localDescription -->
					    	<xsl:call-template name="term">
					    		<xsl:with-param name="list" select="./eac:term"/>
					    		<xsl:with-param name="clazz" select="'legalStatus'"/>
					    		<xsl:with-param name="title" select="'eaccpf.portal.legalStatus'"/>
					    	</xsl:call-template>
							<!-- placeEntry in localDescription -->
							<xsl:call-template name="commonChild">
					    		<xsl:with-param name="list" select="./eac:placeEntry"/>
					    		<xsl:with-param name="clazz" select="'locationLegalStatus'"/>
					    		<xsl:with-param name="title" select="'eaccpf.portal.location'"/>
					    	</xsl:call-template>
							<!-- citation in localDescription -->
						  	<xsl:call-template name="commonChild">
					    		<xsl:with-param name="list" select="./eac:citation"/>
					    		<xsl:with-param name="clazz" select="'citationLegalStatus'"/>
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
					    		<xsl:with-param name="clazz" select="'descriptiveNoteLegalStatus'"/>
					    		<xsl:with-param name="title" select="'eaccpf.portal.note'"/>
					    	</xsl:call-template>
					</xsl:for-each>
						<!-- descriptiveNote in localDescriptions -->
						 <xsl:call-template name="commonChild">
				    		<xsl:with-param name="list" select="./eac:descriptiveNote/eac:p"/>
				    		<xsl:with-param name="clazz" select="'descriptiveNoteLegalStatuses'"/>
				    		<xsl:with-param name="title" select="'eaccpf.portal.note'"/>
				    	 </xsl:call-template>
				</xsl:for-each>		
		   </xsl:if>
		   <!-- function -->
		   <xsl:if test="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:functions/eac:function">
			    <h2 class="title"><xsl:value-of select="ape:resource('eaccpf.portal.function')"/></h2>
				<xsl:for-each select="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:functions"> 
				    <xsl:for-each select="./eac:function">
				    		<!-- term function -->
					    	<xsl:call-template name="term">
					    		<xsl:with-param name="list" select="./eac:term"/>
					    		<xsl:with-param name="clazz" select="'function'"/>
					    		<xsl:with-param name="title" select="'eaccpf.portal.function'"/>
					    	</xsl:call-template>
							<!-- placeEntry in function-->
							<xsl:call-template name="commonChild">
					    		<xsl:with-param name="list" select="./eac:placeEntry"/>
					    		<xsl:with-param name="clazz" select="'locationFunction'"/>
					    		<xsl:with-param name="title" select="'eaccpf.portal.location'"/>
					    	</xsl:call-template>
							<!-- citation in function -->
						  	<xsl:call-template name="commonChild">
					    		<xsl:with-param name="list" select="./eac:citation"/>
					    		<xsl:with-param name="clazz" select="'citationFunction'"/>
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
					    		<xsl:with-param name="clazz" select="'descriptiveNoteFunction'"/>
					    		<xsl:with-param name="title" select="'eaccpf.portal.note'"/>
					    	</xsl:call-template>
					</xsl:for-each>
						<!-- descriptiveNote in functions -->
						 <xsl:call-template name="commonChild">
				    		<xsl:with-param name="list" select="./eac:descriptiveNote/eac:p"/>
				    		<xsl:with-param name="clazz" select="'descriptiveNoteFunctions'"/>
				    		<xsl:with-param name="title" select="'eaccpf.portal.note'"/>
				    	 </xsl:call-template>
				</xsl:for-each>		
		    </xsl:if>
		    <!-- occupation -->
		    <xsl:if test="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:occupations/eac:occupation">
			    <h2 class="title"><xsl:value-of select="ape:resource('eaccpf.portal.occupation')"/></h2>
				<xsl:for-each select="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:occupations"> 
				    <xsl:for-each select="./eac:occupation">
				    		<!-- term function -->
					    	<xsl:call-template name="term">
					    		<xsl:with-param name="list" select="./eac:term"/>
					    		<xsl:with-param name="clazz" select="'occupation'"/>
					    		<xsl:with-param name="title" select="'eaccpf.portal.occupation'"/>
					    	</xsl:call-template>
							<!-- placeEntry in function-->
							<xsl:call-template name="commonChild">
					    		<xsl:with-param name="list" select="./eac:placeEntry"/>
					    		<xsl:with-param name="clazz" select="'locationOccupation'"/>
					    		<xsl:with-param name="title" select="'eaccpf.portal.location'"/>
					    	</xsl:call-template>
							<!-- citation in function -->
						  	<xsl:call-template name="commonChild">
					    		<xsl:with-param name="list" select="./eac:citation"/>
					    		<xsl:with-param name="clazz" select="'citationOccupation'"/>
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
					    		<xsl:with-param name="clazz" select="'descriptiveNoteOccupation'"/>
					    		<xsl:with-param name="title" select="'eaccpf.portal.note'"/>
					    	</xsl:call-template>
					</xsl:for-each>
						<!-- descriptiveNote in functions -->
						 <xsl:call-template name="commonChild">
				    		<xsl:with-param name="list" select="./eac:descriptiveNote/eac:p"/>
				    		<xsl:with-param name="clazz" select="'descriptiveNoteOccupations'"/>
				    		<xsl:with-param name="title" select="'eaccpf.portal.note'"/>
				    	 </xsl:call-template>
				</xsl:for-each>		
		    </xsl:if>
		    <!--mandates-->
		    <xsl:if test="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:mandates/eac:mandate">
			    <h2 class="title"><xsl:value-of select="ape:resource('eaccpf.portal.mandate')"/></h2>
				<xsl:for-each select="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:mandates"> 
				    <xsl:for-each select="./eac:mandate">
				    		<!-- term function -->
					    	<xsl:call-template name="term">
					    		<xsl:with-param name="list" select="./eac:term"/>
					    		<xsl:with-param name="clazz" select="'mandate'"/>
					    		<xsl:with-param name="title" select="'eaccpf.portal.mandate'"/>
					    	</xsl:call-template>
							<!-- placeEntry in function-->
							<xsl:call-template name="commonChild">
					    		<xsl:with-param name="list" select="./eac:placeEntry"/>
					    		<xsl:with-param name="clazz" select="'locationMandate'"/>
					    		<xsl:with-param name="title" select="'eaccpf.portal.location'"/>
					    	</xsl:call-template>
							<!-- citation in function -->
						  	<xsl:call-template name="commonChild">
					    		<xsl:with-param name="list" select="./eac:citation"/>
					    		<xsl:with-param name="clazz" select="'citationMandate'"/>
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
					    		<xsl:with-param name="clazz" select="'descriptiveNoteMandate'"/>
					    		<xsl:with-param name="title" select="'eaccpf.portal.note'"/>
					    	</xsl:call-template>
					</xsl:for-each>
						<!-- descriptiveNote in functions -->
						 <xsl:call-template name="commonChild">
				    		<xsl:with-param name="list" select="./eac:descriptiveNote/eac:p"/>
				    		<xsl:with-param name="clazz" select="'descriptiveNoteMandates'"/>
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
			
			<!-- generalContext -->
			<xsl:if test="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:generalContext">
			<!--  	<h2 class="title"><xsl:value-of select="ape:resource('eaccpf.portal.generalContext')"/></h2> -->
				<xsl:if test="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:generalContext/eac:p/text()">
					<div class="row">
							<div class="leftcolumn">
						   		<h2><xsl:value-of select="ape:resource('eaccpf.portal.generalContext')"/><xsl:text>:</xsl:text></h2>
						   	</div>
						   	<div class="rightcolumn">
								<xsl:call-template name="multilanguage">
						   			<xsl:with-param name="list" select="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:generalContext/eac:p"/>
						   			<xsl:with-param name="clazz" select="'generalContext'"/>
						   		</xsl:call-template>
							</div>
					</div>
				</xsl:if>
			</xsl:if> 
			<!-- biogHist --> 
			<xsl:if test="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:biogHist/eac:p/text()">   
				<div class="row">
						<div class="leftcolumn">
					   		<h2><xsl:value-of select="ape:resource('eaccpf.portal.biogHist')"/><xsl:text>:</xsl:text></h2>
					   	</div>
					   	<div class="rightcolumn leftSide">
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
								<a href="{$href}" target="_blank"><xsl:apply-templates select="./eac:eac-cpf/eac:control/eac:maintenanceAgency/eac:agencyName" mode="other"/> <!--<xsl:value-of select="."/>--></a>
							</xsl:when>
							<xsl:otherwise>
								<!--<xsl:value-of select="."/>-->
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
		<xsl:if test="./eac:eac-cpf/eac:cpfDescription/eac:relations/eac:resourceRelation/eac:relationEntry/text()">
			<div id="material" class="box">
				<div class="boxtitle">
					<span class="collapsibleIcon expanded"> </span>
					<span class="text"><xsl:value-of select="ape:resource('eaccpf.portal.archivalMaterial')"/>
						<xsl:text> (</xsl:text>
						<xsl:value-of select="count(./eac:eac-cpf/eac:cpfDescription/eac:relations/eac:resourceRelation)"/>
						<xsl:text>)</xsl:text>
					</span>
				</div>
				<ul>
					<xsl:for-each select="./eac:eac-cpf/eac:cpfDescription/eac:relations/eac:resourceRelation">
						<li>
							<xsl:choose>
								<xsl:when test="./eac:relationEntry[@localType='title']">
									<xsl:call-template name="multilanguageRelationsTitle">
							   			 	<xsl:with-param name="list" select="./eac:relationEntry[@localType='title']"/>
							   		</xsl:call-template>
						   		</xsl:when>
						   		<xsl:when test="./eac:relationEntry[not(@localType)]">
						   			<xsl:call-template name="multilanguageRelations">
							   			 	<xsl:with-param name="list" select="./eac:relationEntry[not(@localType)]"/>
							   		</xsl:call-template>
						   		</xsl:when>
						   		<!-- TODO -->
						   		<xsl:otherwise>
						   			<xsl:variable name="href" select="./@xlink:href"/>
						   			<a href="{$href}" target="_blank">
						   				<xsl:value-of select="ape:resource('eaccpf.portal.goToRelatedResource')"/>
										<xsl:text> (</xsl:text>
										<xsl:call-template name="relationType">
										     <xsl:with-param name="current" select="."/>
										</xsl:call-template>
										<xsl:text>)</xsl:text>
						   			</a>
						   		</xsl:otherwise>
					   		</xsl:choose>
				   		</li>
			   		</xsl:for-each>
		   		</ul>
		   		<div class="whitespace">
		   		<!--   &nbsp;--> 
		   		</div>
		   		<a class="displayLinkShowMore" href="javascript:showMore('material', 'li');">
					<xsl:value-of select="ape:resource('eaccpf.portal.showmore')"/><xsl:text>...</xsl:text>
				</a>
				<a class="displayLinkShowLess" href="javascript:showLess('material', 'li');">
					<xsl:value-of select="ape:resource('eaccpf.portal.showless')"/><xsl:text>...</xsl:text>
				</a>
			</div>
		</xsl:if>
		<xsl:if test="./eac:eac-cpf/eac:cpfDescription/eac:relations/eac:cpfRelation/eac:relationEntry/text()">
			<div id="persons" class="box">
				<div class="boxtitle">
					<span class="collapsibleIcon expanded"> </span>
					<span class="text"><xsl:value-of select="ape:resource('eaccpf.portal.relatedName')"/>
						<xsl:text> (</xsl:text>
						<xsl:value-of select="count(./eac:eac-cpf/eac:cpfDescription/eac:relations/eac:cpfRelation)"/>
						<xsl:text>)</xsl:text>
					</span>
				</div>
				<ul>
					<xsl:for-each select="./eac:eac-cpf/eac:cpfDescription/eac:relations/eac:cpfRelation">
						<li>
							<xsl:choose>
								<xsl:when test="./eac:relationEntry[@localType='title']">
									<xsl:call-template name="multilanguageRelationsTitle">
							   			 	<xsl:with-param name="list" select="./eac:relationEntry[@localType='title']"/>
							   		</xsl:call-template>
						   		</xsl:when>
						   		<!-- TODO: -->
						   		<xsl:when test="./eac:relationEntry[not(@localType='title')]">
						   			<xsl:call-template name="multilanguageRelations">
							   			 	<xsl:with-param name="list" select="./eac:relationEntry[not(@localType)]"/>
							   		</xsl:call-template>
						   		</xsl:when>
						   		<!-- TODO: -->
						   		<xsl:when test="./not[eac:relationEntry]">
						   			<xsl:variable name="href" select="./@xlink:href"/>
						   			<a href="{$href}" target="_blank">
						   				<xsl:value-of select="ape:resource('eaccpf.portal.goToRelatedName')"/>
						   			</a>	
						   		</xsl:when>
						   		<xsl:otherwise/>
					   		</xsl:choose>
				   		</li>
			   		</xsl:for-each>
		   		</ul>
		   		<div class="whitespace">
		   		 <!--   &nbsp;--> 
		   		</div>
		   		<a class="displayLinkShowMore" href="javascript:showMore('persons', 'li');">
					<xsl:value-of select="ape:resource('eaccpf.portal.showmore')"/><xsl:text>...</xsl:text>
				</a>
				<a class="displayLinkShowLess" href="javascript:showLess('persons', 'li');">
					<xsl:value-of select="ape:resource('eaccpf.portal.showless')"/><xsl:text>...</xsl:text>
				</a>
			</div>
		</xsl:if>
		<xsl:if test="./eac:eac-cpf/eac:cpfDescription/eac:relations/eac:resourceRelation/eac:relationEntry/@localType='agencyName'">	
			<div id="archives" class="box">
				<div class="boxtitle">
					<span class="collapsibleIcon expanded"> </span>
					<span class="text"><xsl:value-of select="ape:resource('eaccpf.portal.archives')"/>
					    <xsl:text> (</xsl:text>
						<xsl:value-of select="count(./eac:eac-cpf/eac:cpfDescription/eac:relations/eac:resourceRelation[eac:relationEntry[@localType='agencyName']])"/>
						<xsl:text>)</xsl:text>
					</span>
				</div>
				<ul>
					<xsl:for-each select="./eac:eac-cpf/eac:cpfDescription/eac:relations/eac:resourceRelation">
							<xsl:choose>
								<xsl:when test="./eac:relationEntry[@localType='agencyName']">
									<li>
										<xsl:call-template name="multilanguageArchives">
						   					<xsl:with-param name="list" select="./eac:relationEntry[@localType='agencyName']"/>
						   				</xsl:call-template>
					   				</li>
								</xsl:when>
								<xsl:when test="./eac:relationEntry[not(@localType='agencyName')]"> 
								</xsl:when>
								<xsl:otherwise/>
							</xsl:choose>
							
		   		    </xsl:for-each>
		   		</ul>
		   		<div class="whitespace">
		   		 <!--   &nbsp;--> 
		   		</div>
		   		<a class="displayLinkShowMore" href="javascript:showMore('archives', 'li');">
					<xsl:value-of select="ape:resource('eaccpf.portal.showmore')"/><xsl:text>...</xsl:text>
				</a>
				<a class="displayLinkShowLess" href="javascript:showLess('archives', 'li');">
					<xsl:value-of select="ape:resource('eaccpf.portal.showless')"/><xsl:text>...</xsl:text>
				</a>
			</div>
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
	
	<!-- template term -->
	<xsl:template name="term">
		<xsl:param name="list"/>
		<xsl:param name="clazz"/>
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
			   			</xsl:call-template>
					</div>
			</div>
		</xsl:if>
	</xsl:template>
	
	<!--template for common childs -->
	<xsl:template name="commonChild">
		<xsl:param name="list"/>
	    <xsl:param name="clazz"/>
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
					<!-- when there are only 1 dateSet -->
					<xsl:if test="$dateSet and (($dateSet/eac:dateRange and $dateSet/eac:dateRange/eac:fromDate/text() and $dateSet/eac:dateRange/eac:toDate/text()) or ($dateSet/eac:date and $dateSet/eac:date/text()))">
						<xsl:text> (</xsl:text>
						<xsl:apply-templates select="$dateSet"/>
						<xsl:text>)</xsl:text>
					</xsl:if>
					<!-- when there are only 1 dateRange -->
					<xsl:if test="$dateRange and $dateRange/eac:fromDate/text() and $dateRange/eac:toDate/text()">
						<xsl:text> (</xsl:text>
						<xsl:apply-templates select="$dateRange"/>
						<xsl:text>)</xsl:text>
					</xsl:if>
					<!-- when there are only 1 date -->
					<xsl:if test="$date and $date/text()">
						<xsl:text> (</xsl:text>
						<xsl:apply-templates select="$date"/>
						<xsl:text>)</xsl:text>
					</xsl:if>
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
	
	<!-- template for nodes with attribute @vocabularySource -->
	<xsl:template name="multilanguageWithVocabularySource">
		<xsl:param name="list"/>
		<xsl:param name="clazz"/>
		<xsl:choose>
			<xsl:when test="count($list) > 1">
			<div id="{$clazz}" class= "moreDisplay">
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
		<xsl:variable name="placeEntryBirth" select="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:places/eac:place/eac:placeEntry[@localType='birth']"/>
		<xsl:variable name="placeEntryDeath" select="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:places/eac:place/eac:placeEntry[@localType='death']"/>
		<xsl:choose>
			<xsl:when test="count($list) > 1">
				<xsl:choose>
					<xsl:when test="$list[@xml:lang = $language.selected] and $list[@xml:lang = $language.selected]/text() and $list[@xml:lang = $language.selected]/text() != ''">
						<xsl:for-each select="$list[@xml:lang = $language.selected]">
							<xsl:if test="position() = 1">
								<xsl:choose>
									<xsl:when test="($list=$placeEntryBirth or $list=$placeEntryDeath) and $list[@vocabularySource]">
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
									<xsl:when test="($list=$placeEntryBirth or $list=$placeEntryDeath) and $list[@vocabularySource]">
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
						   <xsl:text>withoutLanguage</xsl:text>
						  	<xsl:for-each select="$list[not(@xml:lang)]"> 
							 	  <xsl:if test="position()=1"> 
									 <xsl:choose>
										<xsl:when test="($list=$placeEntryBirth or $list=$placeEntryDeath) and $list[@vocabularySource]">
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
												<xsl:when test="($list=$placeEntryBirth or $list=$placeEntryDeath) and $list[@vocabularySource]">
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
						<xsl:when test="($list=$placeEntryBirth or $list=$placeEntryDeath) and $list[@vocabularySource]">
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
	
	<!-- Template for birth date  -->
	<xsl:template name="birthDate">
		<xsl:variable name="birthdate" select="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:existDates/eac:dateRange/eac:fromDate/text()">
	  	</xsl:variable>	
	  	<xsl:choose>
        	<xsl:when test="$birthdate='unknown'">
        		<xsl:text>?</xsl:text>
        	</xsl:when>
        	<xsl:otherwise>
        		<xsl:apply-templates select="$birthdate" mode="other"/>
        	</xsl:otherwise>
        </xsl:choose> 
		<xsl:if test="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:places/eac:place/eac:placeEntry[@localType='birth']">
	  		<xsl:text>, </xsl:text>
	  		<xsl:call-template name="multilanguagePlaceEntry">
	  			<xsl:with-param name="list" select="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:places/eac:place/eac:placeEntry[@localType='birth']"/>
	  		</xsl:call-template>		
	  </xsl:if>
	</xsl:template>
	
	<!-- Template for toDate -->
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
			<xsl:for-each select="eac:date">
				<xsl:if test="current()/text()">
					<xsl:value-of select="."/>
					<xsl:if test="position() != last()">
						<xsl:text>, </xsl:text>
					</xsl:if>
				</xsl:if>
			</xsl:for-each>
			<xsl:if test="eac:dateRange and eac:dateRange/eac:fromDate/text() and eac:dateRange/eac:toDate/text() and eac:date/text()">
				<xsl:text>, </xsl:text>
			</xsl:if>
			<xsl:for-each select="eac:dateRange">
				<xsl:if test="./eac:fromDate/text() and ./eac:toDate/text()">
					<xsl:variable name="var" select="./eac:toDate"></xsl:variable>
					<xsl:choose>	
						<xsl:when test="./eac:fromDate/text()='unknown'">
							<xsl:text>?</xsl:text>
						</xsl:when>
					    <xsl:otherwise>
					    	<xsl:value-of select="./eac:fromDate"/>
					    </xsl:otherwise>
					</xsl:choose>
				<!--  	<xsl:variable name="var" select="./eac:toDate"></xsl:variable>-->
					<xsl:choose>
						<xsl:when test="string(number(substring($var,1,2)))!='NaN' or ($var/text() = 'unknown' and ./eac:fromDate/text() !='unknown')">
							<xsl:text> - </xsl:text>
						</xsl:when>
						<xsl:otherwise>
							<xsl:text> </xsl:text>
						</xsl:otherwise>
					</xsl:choose>
					<xsl:choose>	
						<xsl:when test="./eac:toDate/text()='unknown' and ./eac:fromDate/text() != 'unknown'">
							<xsl:text>?</xsl:text>
						</xsl:when>
					    <xsl:otherwise>
					    	<xsl:value-of select="./eac:toDate"/>
					    </xsl:otherwise>
					</xsl:choose>
					<xsl:if test="position() != last()">
						<xsl:text>, </xsl:text>
					</xsl:if>
				</xsl:if>
			</xsl:for-each>
		</xsl:if>
	</xsl:template>
	
	<!-- template for date -->
	<xsl:template match="eac:date">
		<xsl:if test="./text()">
			<xsl:if test="position() != 1">
				<xsl:text>, </xsl:text>
			</xsl:if>
			<xsl:value-of select="text()"/>
			<xsl:for-each select="eac:date">
				<xsl:if test="current()/text()">
					<xsl:value-of select="."/>
					<xsl:if test="position() != last()">
						<xsl:text>, </xsl:text>
					</xsl:if>
					<xsl:value-of select="current()"/>
				</xsl:if>
			</xsl:for-each>
		</xsl:if>
	</xsl:template>
	
	<!-- template for dateRange -->
	<xsl:template match="eac:dateRange">
		<xsl:if test="./eac:fromDate/text() and ./eac:toDate/text()">
			<xsl:if test="position() != 1">
				<xsl:text>, </xsl:text>
			</xsl:if>
			<xsl:value-of select="./eac:fromDate"/>
			<xsl:variable name="var" select="./eac:toDate"></xsl:variable>
			<xsl:choose>
				<xsl:when test="string(number(substring($var,1,2)))!='NaN'">
					<xsl:text> - </xsl:text>
				</xsl:when>
				<xsl:otherwise>
					<xsl:text> </xsl:text>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:value-of select="./eac:toDate"/>
			<xsl:for-each select="eac:dateRange">
				<xsl:if test="current()/eac:fromDate/text() and current()/eac:toDate/text()">
					<xsl:value-of select="./eac:fromDate"/>
					<xsl:variable name="var" select="./eac:toDate"></xsl:variable>
					<xsl:value-of select="./eac:toDate"/>
					<xsl:if test="position() != last()">
						<xsl:text>, </xsl:text>
					</xsl:if>
					<xsl:choose>
						<xsl:when test="string(number(substring($var,1,2)))!='NaN'">
							<xsl:text> - </xsl:text>
						</xsl:when>
						<xsl:otherwise>
							<xsl:text> </xsl:text>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:if>
			</xsl:for-each>
		</xsl:if>
	</xsl:template>
	
	<!-- template for links to file type -->
	<xsl:template name="linkToFile">
		<xsl:param name="link"/>
	   	<xsl:if test="starts-with($link, 'http') or starts-with($link, 'https') or starts-with($link, 'ftp') or starts-with($link, 'www')">
			<a href="{$link}" target="_blank"><xsl:apply-templates select="current()" mode="other"/></a>
			<xsl:call-template name="relationType">
			   	 <xsl:with-param name="current" select="current()"/>
			</xsl:call-template>
 	   </xsl:if>
 	   <xsl:if test="not(starts-with($link, 'http')) and not(starts-with($link, 'https')) and not(starts-with($link, 'ftp')) and not(starts-with($link, 'www'))">
		<a href="#">
          <!--   <xsl:attribute name="onclick"><script>recoverRelatedInstitution('<xsl:value-of select="ape:related(current()/parent::node()/@xlink:href)"></xsl:value-of>');</script></xsl:attribute>-->
		   <xsl:apply-templates select="current()" mode="other"/></a>
		<xsl:call-template name="relationType">
		     <xsl:with-param name="current" select="current()"/>
		</xsl:call-template>
 	   </xsl:if>
	</xsl:template>
	
	<!-- template for attribute @cpfRelationType or @resourceRelationType -->
	<xsl:template name="relationType">
		<xsl:param name="current"/>
		<xsl:if test="name($current/parent::node())='cpfRelation' and $current/parent::node()[@cpfRelationType]"> 
			<xsl:text> (</xsl:text>
			<xsl:choose>
				<xsl:when test="$current/parent::node()[@cpfRelationType='associative']"> 
					<xsl:value-of select="ape:resource('eaccpf.portal.associative')"/>
				</xsl:when>
				<xsl:when test="$current/parent::node()[@cpfRelationType='identity']"> 
					<xsl:value-of select="ape:resource('eaccpf.portal.identity')"/>
				</xsl:when>
				<xsl:when test="$current/parent::node()[@cpfRelationType='hierarchical']"> 
					<xsl:value-of select="ape:resource('eaccpf.portal.hierarchical')"/>
				</xsl:when>
				<xsl:when test="$current/parent::node()[@cpfRelationType='hierarchical-parent']"> 
					<xsl:value-of select="ape:resource('eaccpf.portal.hierarchicalParent')"/>
				</xsl:when>
				<xsl:when test="$current/parent::node()[@cpfRelationType='hierarchical-child']"> 
					<xsl:value-of select="ape:resource('eaccpf.portal.hierarchicalChild')"/>
				</xsl:when>
				<xsl:when test="$current/parent::node()[@cpfRelationType='temporal']"> 
					<xsl:value-of select="ape:resource('eaccpf.portal.temporal')"/>
				</xsl:when>
				<xsl:when test="$current/parent::node()[@cpfRelationType='temporal-earlier']"> 
					<xsl:value-of select="ape:resource('eaccpf.portal.temporalEarlier')"/>
				</xsl:when>
				<xsl:when test="$current/parent::node()[@cpfRelationType='temporal-later']"> 
					<xsl:value-of select="ape:resource('eaccpf.portal.temporalLater')"/>
				</xsl:when>
				<xsl:when test="$current/parent::node()[@cpfRelationType='family']"> 
					<xsl:value-of select="ape:resource('eaccpf.portal.family')"/>
				</xsl:when>
				<xsl:otherwise/>
			</xsl:choose>
			<xsl:text>)</xsl:text>	
		</xsl:if>
		<xsl:if test="name($current/parent::node())='resourceRelation' and $current/parent::node()[@resourceRelationType]"> 
			<xsl:text> (</xsl:text>
			<xsl:choose>
				<xsl:when test="$current/parent::node()[@resourceRelationType='creatorOf']">
					<xsl:value-of select="ape:resource('eaccpf.portal.creatorOf')"/>
				</xsl:when>
				<xsl:when test="$current/parent::node()[@resourceRelationType='subjectOf']">
					<xsl:value-of select="ape:resource('eaccpf.portal.subjectOf')"/>
				</xsl:when>
				<xsl:when test="$current/parent::node()[@resourceRelationType='other']">
					<xsl:value-of select="ape:resource('eaccpf.portal.other')"/>
				</xsl:when>
				<xsl:otherwise/>
			</xsl:choose>
			<xsl:text>)</xsl:text>	
		</xsl:if>
	</xsl:template>
	
	<!-- template for multilanguage relations -->
	<xsl:template name="multilanguageRelations">
		<xsl:param name="list"/>
			<xsl:choose>
				<xsl:when test="count($list) > 1">
					<xsl:choose>
						<xsl:when test="$list[@xml:lang = $language.selected] and $list[@xml:lang = $language.selected]/text() and $list[@xml:lang = $language.selected]/text() != ''">
						 	<xsl:for-each select="$list[@xml:lang = $language.selected]">
							   <xsl:if test="position()=1">
									<xsl:choose>
										<xsl:when test="./parent::node()/@xlink:href">
											<xsl:call-template name="linkToFile">
												<xsl:with-param name="link" select="./parent::node()/@xlink:href"/>
											</xsl:call-template>
										</xsl:when>
										<xsl:otherwise>
											<xsl:apply-templates select="." mode="other"/>
											<xsl:call-template name="relationType">
												<xsl:with-param name="current" select="current()"/>
											</xsl:call-template>
										</xsl:otherwise>	
									</xsl:choose>
								</xsl:if>	
								
						 	</xsl:for-each> 
						</xsl:when>
						<xsl:when test="$list[@xml:lang = $language.default] and $list[@xml:lang = $language.default]/text() and $list[@xml:lang = $language.default]/text() != ''">
							<xsl:for-each select="$list[@xml:lang = $language.default]">
								<xsl:if test="position()=1"><xsl:choose>
									<xsl:when test="./parent::node()/@xlink:href">
										<xsl:call-template name="linkToFile">
											<xsl:with-param name="link" select="./parent::node()/@xlink:href"/>
										</xsl:call-template>
									</xsl:when>
									<xsl:otherwise>
										<xsl:apply-templates select="." mode="other"/>
										<xsl:call-template name="relationType">
											<xsl:with-param name="current" select="current()"/>
										</xsl:call-template>
									</xsl:otherwise>	
								</xsl:choose>
								</xsl:if>
							</xsl:for-each>
						</xsl:when>
						<xsl:when test="$list[not(@xml:lang)] and $list[not(@xml:lang)]/text() and $list[not(@xml:lang)]/text() != ''">
							  	<xsl:for-each select="$list[not(@xml:lang)]"> 
									<xsl:if test="position()=1">
										<xsl:choose>
											<xsl:when test="./parent::node()/@xlink:href">
												<xsl:call-template name="linkToFile">
													<xsl:with-param name="link" select="./parent::node()/@xlink:href"/>
												</xsl:call-template>
											</xsl:when>
											<xsl:otherwise>
												<xsl:apply-templates select="." mode="other"/>
												<xsl:call-template name="relationType">
													<xsl:with-param name="current" select="current()"/>
												</xsl:call-template>
											</xsl:otherwise>	
										</xsl:choose>
									</xsl:if>
							   	</xsl:for-each> 
						</xsl:when>
						<xsl:otherwise> <!-- first language -->
							<xsl:variable name="language.first" select="$list[1]/@xml:lang"></xsl:variable>
							<xsl:for-each select="$list">
								<xsl:variable name="currentLang" select="current()/@xml:lang"></xsl:variable>
								<xsl:if test="$currentLang = $language.first">
									<xsl:if test="position()=1">
										<xsl:choose>
											<xsl:when test="./parent::node()/@xlink:href">
												<xsl:call-template name="linkToFile">
													<xsl:with-param name="link" select="./parent::node()/@xlink:href"/>
												</xsl:call-template>
											</xsl:when>
											<xsl:otherwise>
												<xsl:apply-templates select="." mode="other"/>
												<xsl:call-template name="relationType">
													<xsl:with-param name="current" select="current()"/>
												</xsl:call-template>
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
						 <xsl:if test="position()=1">
							<xsl:choose>
								<xsl:when test="./parent::node()/@xlink:href">
									<xsl:call-template name="linkToFile">
										<xsl:with-param name="link" select="./parent::node()/@xlink:href"/>
									</xsl:call-template>
								</xsl:when>
								<xsl:otherwise>
									<xsl:apply-templates select="." mode="other"/>
									<xsl:call-template name="relationType">
										<xsl:with-param name="current" select="current()"/>
									</xsl:call-template>
								</xsl:otherwise>	
							</xsl:choose>
						 </xsl:if>
					</xsl:for-each>
				</xsl:otherwise>
			</xsl:choose>
	</xsl:template>
	
	<!--template for multilanguage resourceRelations with @localType='title'-->
	<xsl:template name="multilanguageRelationsTitle">
		<xsl:param name="list"/>
			<xsl:choose>
				<xsl:when test="count($list) > 1">
					<xsl:choose>
						<xsl:when test="$list[@xml:lang = $language.selected] and $list[@xml:lang = $language.selected]/text() and $list[@xml:lang = $language.selected]/text() != ''">
						 	 	<xsl:variable name="link" select="$list/parent::node()/@xlink:href"/>
						 	 	<xsl:choose>
						 	 		<xsl:when test="$list/parent::node()/@xlink:href">
						 	 			<xsl:if test="starts-with($link, 'http') or starts-with($link, 'https') or starts-with($link, 'ftp') or starts-with($link, 'www')">
											<a href="{$link}" target="_blank">
												<xsl:for-each select="$list[@xml:lang = $language.selected]">
													<xsl:apply-templates select="." mode="other"/>
													<xsl:text>. </xsl:text>
											 	</xsl:for-each>
											</a>
								 	   </xsl:if>
								 	   <xsl:if test="not(starts-with($link, 'http')) and not(starts-with($link, 'https')) and not(starts-with($link, 'ftp')) and not(starts-with($link, 'www'))">
										<a href="#">
								          <!--   <xsl:attribute name="onclick"><script>recoverRelatedInstitution('<xsl:value-of select="ape:related(current()/parent::node()/@xlink:href)"></xsl:value-of>');</script></xsl:attribute>-->
										    <xsl:for-each select="$list[@xml:lang = $language.selected]">
												<xsl:apply-templates select="." mode="other"/>
												<xsl:text>. </xsl:text>
										 	</xsl:for-each>
										</a>
								 	   </xsl:if>
						 	 		</xsl:when>
						 	 		<xsl:otherwise>
						 	 			<xsl:for-each select="$list[@xml:lang = $language.selected]">
											<xsl:apply-templates select="." mode="other"/>
											<xsl:text>. </xsl:text>
									 	</xsl:for-each>
						 	 		</xsl:otherwise>
						 	 	</xsl:choose>
						 	 	<xsl:call-template name="relationType">
									<xsl:with-param name="current" select="$list"/>
								</xsl:call-template> 
						</xsl:when>		
						<xsl:when test="$list[@xml:lang = $language.default] and $list[@xml:lang = $language.default]/text() and $list[@xml:lang = $language.default]/text() != ''">
							<xsl:variable name="link" select="$list/parent::node()/@xlink:href"/>
						 	 	<xsl:choose>
						 	 		<xsl:when test="$list/parent::node()/@xlink:href">
						 	 			<xsl:if test="starts-with($link, 'http') or starts-with($link, 'https') or starts-with($link, 'ftp') or starts-with($link, 'www')">
											<a href="{$link}" target="_blank">
												<xsl:for-each select="$list[@xml:lang = $language.default]">
													<xsl:apply-templates select="." mode="other"/>
													<xsl:text>. </xsl:text>
											 	</xsl:for-each>
											</a>
								 	   </xsl:if>
								 	   <xsl:if test="not(starts-with($link, 'http')) and not(starts-with($link, 'https')) and not(starts-with($link, 'ftp')) and not(starts-with($link, 'www'))">
										<a href="#">
								          <!--   <xsl:attribute name="onclick"><script>recoverRelatedInstitution('<xsl:value-of select="ape:related(current()/parent::node()/@xlink:href)"></xsl:value-of>');</script></xsl:attribute>-->
										    <xsl:for-each select="$list[@xml:lang = $language.default]">
												<xsl:apply-templates select="." mode="other"/>
												<xsl:text>. </xsl:text>
										 	</xsl:for-each>
										</a>
								 	   </xsl:if>
						 	 		</xsl:when>
						 	 		<xsl:otherwise>
						 	 			<xsl:for-each select="$list[@xml:lang = $language.default]">
											<xsl:apply-templates select="." mode="other"/>
											<xsl:text>. </xsl:text>
									 	</xsl:for-each>
						 	 		</xsl:otherwise>
						 	 	</xsl:choose>
						 	 	<xsl:call-template name="relationType">
									<xsl:with-param name="current" select="$list"/>
								</xsl:call-template> 
						</xsl:when>
						<xsl:when test="$list[not(@xml:lang)] and $list[not(@xml:lang)]/text() and $list[not(@xml:lang)]/text() != ''">
							  	<xsl:variable name="link" select="$list/parent::node()/@xlink:href"/>
						 	 	<xsl:choose>
						 	 		<xsl:when test="$list/parent::node()/@xlink:href">
						 	 			<xsl:if test="starts-with($link, 'http') or starts-with($link, 'https') or starts-with($link, 'ftp') or starts-with($link, 'www')">
											<a href="{$link}" target="_blank">
												<xsl:for-each select="$list[not(@xml:lang)]">
													<xsl:apply-templates select="." mode="other"/>
													<xsl:text>. </xsl:text>
											 	</xsl:for-each>
											</a>
								 	   </xsl:if>
								 	   <xsl:if test="not(starts-with($link, 'http')) and not(starts-with($link, 'https')) and not(starts-with($link, 'ftp')) and not(starts-with($link, 'www'))">
										<a href="#">
								          <!--   <xsl:attribute name="onclick"><script>recoverRelatedInstitution('<xsl:value-of select="ape:related(current()/parent::node()/@xlink:href)"></xsl:value-of>');</script></xsl:attribute>-->
										    <xsl:for-each select="$list[not(@xml:lang)]">
												<xsl:apply-templates select="." mode="other"/>
												<xsl:text>. </xsl:text>
										 	</xsl:for-each>
										</a>
								 	   </xsl:if>
						 	 		</xsl:when>
						 	 		<xsl:otherwise>
						 	 			<xsl:for-each select="$list[not(@xml:lang)]">
											<xsl:apply-templates select="." mode="other"/>
											<xsl:text>. </xsl:text>
									 	</xsl:for-each>
						 	 		</xsl:otherwise>
						 	 	</xsl:choose>
						 	 	<xsl:call-template name="relationType">
									<xsl:with-param name="current" select="$list"/>
								</xsl:call-template> 
						</xsl:when>
						<xsl:otherwise> <!-- first language -->
							<xsl:variable name="language.first" select="$list[1]/@xml:lang"></xsl:variable>
							<xsl:variable name="link" select="$list/parent::node()/@xlink:href"/>
						 	 	<xsl:choose>
						 	 		<xsl:when test="$list/parent::node()/@xlink:href">
						 	 			<xsl:if test="starts-with($link, 'http') or starts-with($link, 'https') or starts-with($link, 'ftp') or starts-with($link, 'www')">
											<a href="{$link}" target="_blank">
												<xsl:for-each select="$list">
													<xsl:variable name="currentLang" select="current()/@xml:lang"></xsl:variable>
													<xsl:if test="$currentLang = $language.first">
														<xsl:apply-templates select="." mode="other"/>
														<xsl:text>. </xsl:text>
													</xsl:if>	
											 	</xsl:for-each>
											</a>
								 	   </xsl:if>
								 	   <xsl:if test="not(starts-with($link, 'http')) and not(starts-with($link, 'https')) and not(starts-with($link, 'ftp')) and not(starts-with($link, 'www'))">
										<a href="#">
								          <!--   <xsl:attribute name="onclick"><script>recoverRelatedInstitution('<xsl:value-of select="ape:related(current()/parent::node()/@xlink:href)"></xsl:value-of>');</script></xsl:attribute>-->
										   <xsl:for-each select="$list">
												<xsl:variable name="currentLang" select="current()/@xml:lang"></xsl:variable>
												<xsl:if test="$currentLang = $language.first">
													<xsl:apply-templates select="." mode="other"/>
													<xsl:text>. </xsl:text>
												</xsl:if>	
										 	</xsl:for-each>
										</a>
								 	   </xsl:if>
						 	 		</xsl:when>
						 	 		<xsl:otherwise>
						 	 			<xsl:for-each select="$list">
											<xsl:variable name="currentLang" select="current()/@xml:lang"></xsl:variable>
											<xsl:if test="$currentLang = $language.first">
												<xsl:apply-templates select="." mode="other"/>
												<xsl:text>. </xsl:text>
											</xsl:if>	
									 	</xsl:for-each>
						 	 		</xsl:otherwise>
						 	 	</xsl:choose>
						 	 	<xsl:call-template name="relationType">
									<xsl:with-param name="current" select="$list"/>
								</xsl:call-template> 
						</xsl:otherwise>
					</xsl:choose>
				</xsl:when>
				<xsl:otherwise>
					<xsl:variable name="link" select="$list/parent::node()/@xlink:href"/>
			 	 	<xsl:choose>
			 	 		<xsl:when test="$list/parent::node()/@xlink:href">
			 	 			<xsl:if test="starts-with($link, 'http') or starts-with($link, 'https') or starts-with($link, 'ftp') or starts-with($link, 'www')">
								<a href="{$link}" target="_blank">
									<xsl:for-each select="$list">
										<xsl:apply-templates select="." mode="other"/>
								 	</xsl:for-each>
								</a>
					 	   </xsl:if>
					 	   <xsl:if test="not(starts-with($link, 'http')) and not(starts-with($link, 'https')) and not(starts-with($link, 'ftp')) and not(starts-with($link, 'www'))">
							<a href="#">
					          <!--   <xsl:attribute name="onclick"><script>recoverRelatedInstitution('<xsl:value-of select="ape:related(current()/parent::node()/@xlink:href)"></xsl:value-of>');</script></xsl:attribute>-->
							    <xsl:for-each select="$list">
									<xsl:apply-templates select="." mode="other"/>
							 	</xsl:for-each>
							</a>
					 	   </xsl:if>
			 	 		</xsl:when>
			 	 		<xsl:otherwise>
			 	 			<xsl:for-each select="$list">
								<xsl:apply-templates select="." mode="other"/>
						 	</xsl:for-each>
			 	 		</xsl:otherwise>
			 	 	</xsl:choose>
			 	 	<xsl:call-template name="relationType">
						<xsl:with-param name="current" select="$list"/>
					</xsl:call-template>
				</xsl:otherwise>
			</xsl:choose>
	</xsl:template>
	
	<!-- template for multilanguage archives -->
	<xsl:template name="multilanguageArchives">
		<xsl:param name="list"/>
			<xsl:choose>
				<xsl:when test="count($list) > 1">
					<xsl:choose>
						<xsl:when test="$list[@xml:lang = $language.selected] and $list[@xml:lang = $language.selected]/text() and $list[@xml:lang = $language.selected]/text() != ''">
						 	<xsl:for-each select="$list[@xml:lang = $language.selected]"> 
							   <xsl:if test="position()=1">
										<xsl:choose>
											<xsl:when test="./parent::node()/@localType='agencyCode'">
												<xsl:variable name="href" select="./parent::node()/@localType='agencyCode'"/>
											  		<xsl:if test="starts-with($href, 'http') or starts-with($href, 'https') or starts-with($href, 'ftp') or starts-with($href, 'www')">
														<a href="{$href}" target="_blank"><xsl:apply-templates select="." mode="other"/></a>
											  		</xsl:if>
											  		<xsl:if test="not(starts-with($href, 'http')) and not(starts-with($href, 'https')) and not(starts-with($href, 'ftp')) and not(starts-with($href, 'www'))">
														<a href="#">
	          											<!--   <xsl:attribute name="onclick"><script>recoverRelatedInstitution('<xsl:value-of select="ape:related(current()/parent::node()/@xlink:href)"></xsl:value-of>');</script></xsl:attribute>-->
															<xsl:apply-templates select="." mode="other"/>
														</a>
													</xsl:if>
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
								<xsl:if test="position()=1">
										<xsl:variable name="href" select="./parent::node()/@localType='agencyCode'"/>
										<xsl:if test="starts-with($href, 'http') or starts-with($href, 'https') or starts-with($href, 'ftp') or starts-with($href, 'www')">
											<a href="{$href}" target="_blank"><xsl:apply-templates select="." mode="other"/></a>
								  		</xsl:if>
								  		<xsl:if test="not(starts-with($href, 'http')) and not(starts-with($href, 'https')) and not(starts-with($href, 'ftp')) and not(starts-with($href, 'www'))">
											<a href="#">
	       											<!--   <xsl:attribute name="onclick"><script>recoverRelatedInstitution('<xsl:value-of select="ape:related(current()/parent::node()/@xlink:href)"></xsl:value-of>');</script></xsl:attribute>-->
	       											<xsl:apply-templates select="." mode="other"/>
											</a>
										</xsl:if>
								</xsl:if>
							</xsl:for-each>
						</xsl:when>
						<xsl:when test="$list[not(@xml:lang)] and $list[not(@xml:lang)]/text() and $list[not(@xml:lang)]/text() != ''">
							  	<xsl:for-each select="$list[not(@xml:lang)]"> 
									<xsl:if test="position()=1">
										<xsl:variable name="href" select="./parent::node()/@localType='agencyCode'"/>
										<xsl:if test="starts-with($href, 'http') or starts-with($href, 'https') or starts-with($href, 'ftp') or starts-with($href, 'www')">
											<a href="{$href}" target="_blank"><xsl:apply-templates select="." mode="other"/></a>
								  		</xsl:if>
								  		<xsl:if test="not(starts-with($href, 'http')) and not(starts-with($href, 'https')) and not(starts-with($href, 'ftp')) and not(starts-with($href, 'www'))">
											<a href="${aiCodeUrl}/${repositoryCode}">
												<!--   <xsl:attribute name="onclick"><script>recoverRelatedInstitution('<xsl:value-of select="ape:related(current()/parent::node()/@xlink:href)"></xsl:value-of>');</script></xsl:attribute>-->
												<xsl:apply-templates select="." mode="other"/>
											</a>
										</xsl:if>
									</xsl:if>
							   	</xsl:for-each> 
						</xsl:when>
						<xsl:otherwise> <!-- first language -->
							<xsl:variable name="language.first" select="$list[1]/@xml:lang"></xsl:variable>
							<xsl:for-each select="$list">
								<xsl:variable name="currentLang" select="current()/@xml:lang"></xsl:variable>
								<xsl:if test="$currentLang = $language.first">
									<xsl:if test="position()=1">
										<xsl:variable name="href" select="./parent::node()/@localType='agencyCode'"/>
										<xsl:if test="starts-with($href, 'http') or starts-with($href, 'https') or starts-with($href, 'ftp') or starts-with($href, 'www')">
											<a href="{$href}" target="_blank"><xsl:apply-templates select="." mode="other"/></a>
								  		</xsl:if>
								  		<xsl:if test="not(starts-with($href, 'http')) and not(starts-with($href, 'https')) and not(starts-with($href, 'ftp')) and not(starts-with($href, 'www'))">
											<a href="#">
        											<!--   <xsl:attribute name="onclick"><script>recoverRelatedInstitution('<xsl:value-of select="ape:related(current()/parent::node()/@xlink:href)"></xsl:value-of>');</script></xsl:attribute>-->
												<xsl:apply-templates select="." mode="other"/>
											</a>
										</xsl:if>
									</xsl:if>			
								</xsl:if>
							</xsl:for-each>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:when>
				<xsl:otherwise>
					<xsl:for-each select="$list">
						 <xsl:if test="position()=1">
							<xsl:variable name="href" select="./parent::node()/@localType='agencyCode'"/>
							<xsl:if test="starts-with($href, 'http') or starts-with($href, 'https') or starts-with($href, 'ftp') or starts-with($href, 'www')">
								<a href="{$href}" target="_blank"><xsl:apply-templates select="." mode="other"/></a>
					  		</xsl:if>
					  		<xsl:if test="not(starts-with($href, 'http')) and not(starts-with($href, 'https')) and not(starts-with($href, 'ftp')) and not(starts-with($href, 'www'))">
								<a href="${aiCodeUrl}/${repositoryCode}">
     											<!--   <xsl:attribute name="onclick"><script>recoverRelatedInstitution('<xsl:value-of select="ape:related(current()/parent::node()/@xlink:href)"></xsl:value-of>');</script></xsl:attribute>-->
									<xsl:apply-templates select="." mode="other"/>
								</a>
							</xsl:if>
						 </xsl:if>
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
</xsl:stylesheet>