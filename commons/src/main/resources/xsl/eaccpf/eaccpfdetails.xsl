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
	
		<h1 class="blockHeader">
	      <!--    <xsl:call-template name="nameEntry"/> -->
	      	<xsl:call-template name="multilanguageName">
		       		 <xsl:with-param name="list" select="//eac:nameEntry"/>
		    </xsl:call-template> 
			<xsl:variable name="existDates" select="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:existDates"></xsl:variable>
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
			<!--<xsl:value-of select="./eac:eac-cpf/eac:cpfDescription/eac:identity/eac:entityType"/> -->
				<span>
					<xsl:apply-templates select="./eac:eac-cpf/eac:cpfDescription/eac:identity/eac:entityType" mode="other"/>
				</span>
			</xsl:if>
		</h1>
		<div id="details">	
			<!-- Dates -->
			<xsl:if test="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:existDates/eac:dateRange/eac:fromDate/text()">
				<div class="row">
						<div class="leftcolumn">
							<h2><xsl:value-of select="ape:resource('eaccpf.portal.birthDate')"/></h2>	
						</div>
						<div class="rightcolumn">
							<xsl:call-template name="birthDate"/>
						</div>
				</div>
			</xsl:if>
			<xsl:if test="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:existDates/eac:dateRange/eac:toDate/text()">
				<div class="row">
						<div class="leftcolumn">
					   		<h2><xsl:value-of select="ape:resource('eaccpf.portal.deathDate')"/></h2>
					   	</div>
					   	<div class="rightcolumn">
							<xsl:call-template name="deathDate"/>
						</div>
				</div>	
			</xsl:if> 
			<!-- alternative names -->
			<xsl:if test="count(//eac:nameEntry) > 1">
				<div class="row">
					<div class="leftcolumn">
				   		<h2><xsl:value-of select="ape:resource('eaccpf.portal.alternativeForms')"/></h2>
				   	</div>
				   	<div class="rightcolumn">
				   			<xsl:call-template name="alternativeName">
				   				<xsl:with-param name="list" select="//eac:nameEntry"/>
				   				<xsl:with-param name="clazz" select="'alternativeName'"/>
				   			</xsl:call-template>  
					</div>
				</div>
			</xsl:if>
			<!-- localDescription -->
			<xsl:if test="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:localDescriptions/eac:localDescription">
			    <h2 class="title"><xsl:value-of select="ape:resource('eaccpf.portal.localDescription')"/></h2>
				<xsl:if test="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:localDescriptions/eac:localDescription/eac:term/text()">	 
					<div class="row subrow">
							<div class="leftcolumn">
						   		<h2><xsl:value-of select="ape:resource('eaccpf.portal.localDescription')"/><xsl:text>:</xsl:text></h2>
						   	</div>
						   	<div class="rightcolumn">
								<xsl:call-template name="multilanguage">
					   				<xsl:with-param name="list" select="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:localDescriptions/eac:localDescription/eac:term"/>
					   				<xsl:with-param name="clazz" select="'localDescription'"/>
					   			</xsl:call-template>
							</div>
					</div>
				</xsl:if>
				<!-- placeEntry in localDescription -->
				<xsl:if test="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:localDescriptions/eac:localDescription/eac:placeEntry/text()">	 
					<div class="row subrow">
							<div class="leftcolumn">
						   		<h2><xsl:value-of select="ape:resource('eaccpf.portal.location')"/><xsl:text>:</xsl:text></h2>
						   	</div>
						   	<div class="rightcolumn">
								<xsl:call-template name="multilanguagePlaces">
					   				<xsl:with-param name="list" select="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:localDescriptions/eac:localDescription/eac:placeEntry"/>
					   				<xsl:with-param name="clazz" select="'locationLocalDescription'"/>
					   			</xsl:call-template>
							</div>
					</div>
				</xsl:if>
				<!-- descriptiveNote in localDescription -->
				<xsl:if test="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:localDescriptions/eac:localDescription/eac:descriptiveNote/eac:p/text()">	 
					<div class="row subrow">
							<div class="leftcolumn">
						   		<h2><xsl:value-of select="ape:resource('eaccpf.portal.description')"/><xsl:text>:</xsl:text></h2>
						   	</div>
						   	<div class="rightcolumn">
								<xsl:call-template name="multilanguage">
					   				<xsl:with-param name="list" select="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:localDescriptions/eac:localDescription/eac:descriptiveNote/eac:p"/>
					   				<xsl:with-param name="clazz" select="'descriptiveNoteLocalDescription'"/>
					   			</xsl:call-template>
							</div>
					</div>
				</xsl:if>
				<!-- citation in localDescription -->
				<xsl:if test="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:localDescriptions/eac:localDescription/eac:citation/text()">	 
					<div class="row subrow">
							<div class="leftcolumn">
						   		<h2><xsl:value-of select="ape:resource('eaccpf.portal.citation')"/><xsl:text>:</xsl:text></h2>
						   	</div>
						   	<div class="rightcolumn">
								<xsl:call-template name="multilanguage">
					   				<xsl:with-param name="list" select="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:localDescriptions/eac:localDescription/eac:citation"/>
					   				<xsl:with-param name="clazz" select="'citationLocalDescription'"/>
					   			</xsl:call-template>
							</div>
					</div>
				</xsl:if>
				<!-- dates in localDescription -->
				<xsl:if test="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:localDescriptions/eac:localDescription/eac:date or
				              ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:localDescriptions/eac:localDescription/eac:dateRange or
				              ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:localDescriptions/eac:localDescription/eac:dateSet">
				    <div class="row subrow">
				    	<div class="leftcolumn">
						   		<h2><xsl:value-of select="ape:resource('eaccpf.portal.date')"/><xsl:text>:</xsl:text></h2>
						</div>          
				        <div class="rightcolumn">  
							<xsl:variable name="dateLocalDescription" select="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:localDescriptions/eac:localDescription"></xsl:variable>
							<!-- when there are only 1 dateSet -->
							<xsl:if test="$dateLocalDescription/eac:dateSet and (($dateLocalDescription/eac:dateSet/eac:dateRange and $dateLocalDescription/eac:dateSet/eac:dateRange/eac:fromDate/text() and $dateLocalDescription/eac:dateSet/eac:dateRange/eac:toDate/text()) or ($dateLocalDescription/eac:dateSet/eac:date and $dateLocalDescription/eac:dateSet/eac:date/text()))">
								<xsl:text> (</xsl:text>
								<xsl:apply-templates select="$dateLocalDescription/eac:dateSet"/>
								<xsl:text>)</xsl:text>
							</xsl:if>
							<!-- when there are only 1 dateRange -->
							<xsl:if test="$dateLocalDescription/eac:dateRange and $dateLocalDescription/eac:dateRange/eac:fromDate/text() and $dateLocalDescription/eac:dateRange/eac:toDate/text()">
								<xsl:text> (</xsl:text>
								<xsl:apply-templates select="$dateLocalDescription/eac:dateRange"/>
								<xsl:text>)</xsl:text>
							</xsl:if>
							<!-- when there are only 1 date -->
							<xsl:if test="$dateLocalDescription/eac:date and $dateLocalDescription/eac:date/text()">
								<xsl:text> (</xsl:text>
								<xsl:apply-templates select="$dateLocalDescription/eac:date"/>
								<xsl:text>)</xsl:text>
							</xsl:if>
						</div> 
					</div>
				</xsl:if>
			</xsl:if>	
			
			<!-- function -->
			<xsl:if test="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:functions/eac:function/eac:term/text()">	   
				<div class="row">
						<div class="leftcolumn">
					   		<h2><xsl:value-of select="ape:resource('eaccpf.portal.function')"/></h2>
					   	</div>
					   	<div class="rightcolumn">
							<xsl:call-template name="multilanguage">
				   				<xsl:with-param name="list" select="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:functions/eac:function/eac:term"/>
				   				<xsl:with-param name="clazz" select="'function'"/>
				   			</xsl:call-template>
						</div>
				</div>
			</xsl:if> 
			<!-- occupation --> 
			<xsl:if test="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:occupations/eac:occupation/eac:term/text()">   
				<div class="row">
						<div class="leftcolumn">
					   		<h2><xsl:value-of select="ape:resource('eaccpf.portal.occupation')"/></h2>
					   	</div>
					   	<div class="rightcolumn">
							<xsl:call-template name="multilanguage">
					   			<xsl:with-param name="list" select="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:occupations/eac:occupation/eac:term"/>
					   			<xsl:with-param name="clazz" select="'occupation'"/>
					   		</xsl:call-template>
						</div>
				</div>
			</xsl:if> 
			<!-- mandates -->
			<xsl:if test="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:mandates/eac:mandate/eac:term/text()">   
				<div class="row">
						<div class="leftcolumn">
					   		<h2><xsl:value-of select="ape:resource('eaccpf.portal.mandate')"/></h2>
					   	</div>
					   	<div class="rightcolumn">
							<xsl:call-template name="multilanguage">
					   			<xsl:with-param name="list" select="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:mandates/eac:mandate/eac:term"/>
					   			<xsl:with-param name="clazz" select="'mandates'"/>
					   		</xsl:call-template>
						</div>
				</div>
			</xsl:if> 
			<!--languagesUsed -->
			<xsl:if test="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:languagesUsed/eac:languageUsed/eac:language/text()">   
				<div class="row">
						<div class="leftcolumn">
					   		<h2><xsl:value-of select="ape:resource('eaccpf.portal.languagesUsed')"/></h2>
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
					   		<h2><xsl:value-of select="ape:resource('eaccpf.portal.scriptUsed')"/></h2>
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
						   		<h2><xsl:value-of select="ape:resource('eaccpf.portal.description')"/></h2>
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
					   		<h2><xsl:value-of select="ape:resource('eaccpf.portal.biogHist')"/></h2>
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
					   		<h2><xsl:value-of select="ape:resource('eaccpf.portal.agencyName')"/></h2>
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
			<h2 class="otherInformation"><xsl:value-of select="ape:resource('eaccpf.portal.otherInformation')"/></h2>
			     <!-- last update -->
			<div class="row grey">
				<div class="leftcolumn">
					<h2 class="grey"><xsl:value-of select="ape:resource('eaccpf.portal.eventDate')"/></h2>
				</div>
				<div class="rightcolumn"> 
					<xsl:apply-templates select="./eac:eac-cpf/eac:control/eac:maintenanceHistory/eac:maintenanceEvent/eac:eventDateTime[last()]" mode="other"/>
				</div>
			</div>	
				<!-- identifier-->
			<div class="row grey">
				<div class="leftcolumn">
					<h2 class="grey"><xsl:value-of select="ape:resource('eaccpf.portal.identifier')"/></h2>
				</div>
				<div class="rightcolumn"> 
					<xsl:apply-templates select="./eac:eac-cpf/eac:control/eac:recordId" mode="other"/>
				</div>
			</div>
	</div>
	<!-- BOXES -->
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
						   		<xsl:when test="./not[eac:relationEntry]">
						   			<xsl:variable name="href" select="./@xlink:href"/>
						   			<a href="{$href}" target="_blank">
						   				<xsl:value-of select="ape:resource('eaccpf.portal.goToRelatedResource')"/>
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
		   		<a class="displayLinkShowMore" href="javascript:showMoreRelation('material', 'li');">
					<xsl:value-of select="ape:resource('eaccpf.portal.showmore')"/><xsl:text>...</xsl:text>
				</a>
				<a class="displayLinkShowLess" href="javascript:showLessRelation('material', 'li');">
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
						   		<xsl:when test="./eac:relationEntry[not(@localType='title')]">
						   			<xsl:call-template name="multilanguageRelations">
							   			 	<xsl:with-param name="list" select="./eac:relationEntry[not(@localType)]"/>
							   		</xsl:call-template>
						   		</xsl:when>
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
		   		<a class="displayLinkShowMore" href="javascript:showMoreRelation('persons', 'li');">
					<xsl:value-of select="ape:resource('eaccpf.portal.showmore')"/><xsl:text>...</xsl:text>
				</a>
				<a class="displayLinkShowLess" href="javascript:showLessRelation('persons', 'li');">
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
		   		<a class="displayLinkShowMore" href="javascript:showMoreRelation('archives', 'li');">
					<xsl:value-of select="ape:resource('eaccpf.portal.showmore')"/><xsl:text>...</xsl:text>
				</a>
				<a class="displayLinkShowLess" href="javascript:showLessRelation('archives', 'li');">
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
				<a class="displayLinkShowMore linkShow" href="javascript:showMoreRelation('{$clazz}', 'p');">
					<xsl:value-of select="ape:resource('eaccpf.portal.showmore')"/>
				</a>
				<a class="displayLinkShowLess linkShow" href="javascript:showLessRelation('{$clazz}', 'p');">
					<xsl:value-of select="ape:resource('eaccpf.portal.showless')"/>
				</a>
			</div>	
		</div>
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
	
	<!-- template for places -->
	<xsl:template name="multilanguagePlaces">
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
											<a href="{$href}" target="_blank"><xsl:apply-templates select="." mode="other"/> <!--<xsl:value-of select="."/>--></a>
										</xsl:when>
										<xsl:otherwise>
										<!-- <xsl:value-of select="."/>-->
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
											<a href="{$href}" target="_blank"><xsl:apply-templates select="." mode="other"/> <!-- <xsl:value-of select="."/> --></a>
										</xsl:when>
										<xsl:otherwise>
											<!--<xsl:value-of select="."/>-->
										 <xsl:apply-templates select="." mode="other"/> 
										</xsl:otherwise>
									</xsl:choose>
								</p>
						</xsl:for-each>
					</xsl:when>
					<xsl:when test="$list[not(@xml:lang)] and $list[not(@xml:lang)]/text() and $list[not(@xml:lang)]/text() != ''">
						   <xsl:text>withoutLanguage</xsl:text>
						  	<xsl:for-each select="$list[not(@xml:lang)]"> 
								 	  <p>
										 <xsl:choose>
											<xsl:when test="@vocabularySource">
												<xsl:variable name="href" select="./@vocabularySource"/>
												<a href="{$href}" target="_blank"><xsl:apply-templates select="." mode="other"/> <!-- <xsl:value-of select="."/> --></a>
											</xsl:when>
											<xsl:otherwise>
												<!--<xsl:value-of select="."/>-->
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
													<a href="{$href}" target="_blank"><!--<xsl:value-of select="$currentText"/>--><xsl:apply-templates select="$currentText" mode="other"/></a>
												</xsl:when>
												<xsl:otherwise>
													<!--<xsl:value-of select="$currentText"/>-->
												 <xsl:apply-templates select="$currentText" mode="other"/> 
												</xsl:otherwise>
											</xsl:choose>
										</p>
									</xsl:if>
						</xsl:for-each>
					</xsl:otherwise>
				</xsl:choose>
				<div class="linkMore">
					<a class="displayLinkShowMore linkShow" href="javascript:showMoreRelation('{$clazz}', 'p');">
						<xsl:value-of select="ape:resource('eaccpf.portal.showmore')"/>
					</a>
					<a class="displayLinkShowLess linkShow" href="javascript:showLessRelation('{$clazz}', 'p');">
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
							<a href="{$href}" target="_blank"><xsl:apply-templates select="." mode="other"/> <!--<xsl:value-of select="."/>--></a>
						</xsl:when>
						<xsl:otherwise>
							<!--<xsl:value-of select="."/>-->
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
										<a href="{$href}" target="_blank"><xsl:apply-templates select="." mode="other"/> <!--<xsl:value-of select="."/>--></a>
									</xsl:when>
									<xsl:otherwise>
									<!-- <xsl:value-of select="."/>-->
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
										<a href="{$href}" target="_blank"><xsl:apply-templates select="." mode="other"/> <!-- <xsl:value-of select="."/> --></a>
									</xsl:when>
									<xsl:otherwise>
										<!--<xsl:value-of select="."/>-->
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
											<a href="{$href}" target="_blank"><xsl:apply-templates select="." mode="other"/> <!-- <xsl:value-of select="."/> --></a>
										</xsl:when>
										<xsl:otherwise>
											<!--<xsl:value-of select="."/>-->
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
													<a href="{$href}" target="_blank"><!--<xsl:value-of select="$currentText"/>--><xsl:apply-templates select="$currentText" mode="other"/></a>
												</xsl:when>
												<xsl:otherwise>
													<!--<xsl:value-of select="$currentText"/>-->
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
							<a href="{$href}" target="_blank"><xsl:apply-templates select="." mode="other"/> <!--<xsl:value-of select="."/>--></a>
						</xsl:when>
						<xsl:otherwise>
							<!--<xsl:value-of select="."/>-->
						 <xsl:apply-templates select="." mode="other"/> 
						</xsl:otherwise>
					</xsl:choose>
				</xsl:for-each>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	
	<!-- Template for surname, firstname or patronymic -->
	
	
	<!-- Template for birth date  -->
	<xsl:template name="birthDate">
		<xsl:variable name="birthdate" select="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:existDates/eac:dateRange/eac:fromDate/text()">
	  	</xsl:variable>	
	  	<xsl:choose>
        	<xsl:when test="$birthdate='unknown'">
        		<xsl:text>?</xsl:text>
        	</xsl:when>
        	<xsl:otherwise>
        		<!--  <xsl:value-of select="$birthDate"></xsl:value-of>-->
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
	
	<!-- Template for deathDate -->
	<xsl:template name="deathDate">
		<xsl:variable name="deathDate" select="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:existDates/eac:dateRange/eac:toDate/text()">
	  	</xsl:variable>	
	  	<xsl:choose>
        	<xsl:when test="$deathDate='unknown'">
        		<xsl:text>?</xsl:text>
        	</xsl:when>
        	<xsl:otherwise>
        		<!--  <xsl:value-of select="$birthDate"></xsl:value-of>-->
        		<xsl:apply-templates select="$deathDate" mode="other"/>
        	</xsl:otherwise>
        </xsl:choose> 
		<xsl:if test="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:places/eac:place/eac:placeEntry[@localType='death']">
	  		<xsl:text>, </xsl:text>
	  		<xsl:call-template name="multilanguagePlaceEntry">
	  			<xsl:with-param name="list" select="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:places/eac:place/eac:placeEntry[@localType='death']"/>
	  		</xsl:call-template>		
	  </xsl:if>
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
					<a class="displayLinkShowMore linkShow" href="javascript:showMoreRelation('{$clazz}', 'p');">
						<xsl:value-of select="ape:resource('eaccpf.portal.showmore')"/>
					</a>
					<a class="displayLinkShowLess linkShow" href="javascript:showLessRelation('{$clazz}', 'p');">
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