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
	<xsl:variable name="language.default" select="'eng'"/>
	<xsl:template match="text()" mode="other">
		<xsl:value-of select="ape:highlight(., 'other')" disable-output-escaping="yes" />
	</xsl:template>
	<xsl:template match="/">
		<!-- <xsl:text>LANGUAGE SELECTED: </xsl:text><xsl:value-of select="$language.selected"/>-->
		<h2 class="blockHeader">
	        <xsl:call-template name="nameEntry"/> 
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
		</h2>
		<div class="eaccpf">
		  <table>
		    <thead>
			</thead>
	    	<tbody>
			   <tr>
			   	<td class="label">
			   		<xsl:if test="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:existDates/eac:dateRange/eac:fromDate/text()">
			   			 <xsl:value-of select="ape:resource('eaccpf.portal.birthDate')"/>
			   		</xsl:if>
			   	</td>
			   	<td>
			   		<xsl:call-template name="birthDate"/>
			   	</td>
			   </tr>
			   <tr>
			   	<td class="label">
			   		<xsl:if test="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:existDates/eac:dateRange/eac:toDate/text()">
			   			 <xsl:value-of select="ape:resource('eaccpf.portal.deathDate')"/>
			   		</xsl:if>
			   	</td>
			   	<td>
			   		<xsl:call-template name="deathDate"/>
			   	</td>
			   </tr>
			   <tr>
			   	<td class="label">
			    	<xsl:if test="./eac:eac-cpf/eac:cpfDescription/eac:identity/eac:nameEntryParallel or ./eac:eac-cpf/eac:cpfDescription/eac:identity/eac:nameEntry"> 
			    		<xsl:value-of select="ape:resource('eaccpf.portal.alternativeForms')"/>		
			    	</xsl:if>
			   	</td>
			   	<td>
			   		<xsl:if test="./eac:eac-cpf/eac:cpfDescription/eac:identity/eac:nameEntryParallel">
			   			<xsl:call-template name="alternativeName">
			   				<xsl:with-param name="listName" select="./eac:eac-cpf/eac:cpfDescription/eac:identity/eac:nameEntryParallel/eac:nameEntry"/>
			   			</xsl:call-template>  
			   		</xsl:if>
			   		<xsl:if test="./eac:eac-cpf/eac:cpfDescription/eac:identity/eac:nameEntry">
			   			<xsl:call-template name="alternativeName">
			   				<xsl:with-param name="listName" select="./eac:eac-cpf/eac:cpfDescription/eac:identity/eac:nameEntry"/>
			   			</xsl:call-template>  
			   		</xsl:if>
			   	</td>	
			   </tr>
			   <tr>
			   	<td class="label">
			   		<xsl:if test="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:biogHist">
			   			<xsl:value-of select="ape:resource('eaccpf.portal.biogHist')"/>
			   		</xsl:if>	
			   	</td>
			   	</tr>
			    <tr>
			    <td colspan="2">
			   		<xsl:if test="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:biogHist">
				   		<xsl:call-template name="multilanguage">
				   			<xsl:with-param name="list" select="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:biogHist/eac:p/text()"/>
				   		</xsl:call-template>
	   				</xsl:if>
			   	</td>	
			   </tr>
		   </tbody>
		  </table> 			
	   </div>
	</xsl:template>	
	
	<!-- Template for name -->
	<xsl:template name="nameEntry">
	   	<xsl:choose>
			<xsl:when test="./eac:eac-cpf/eac:cpfDescription/eac:identity/eac:nameEntryParallel"> 
		       	<xsl:call-template name="multilanguageName">
		       		 <xsl:with-param name="list" select="./eac:eac-cpf/eac:cpfDescription/eac:identity/eac:nameEntryParallel/eac:nameEntry"/>
		       	</xsl:call-template>
		 		</xsl:when>
		    <xsl:when test="./eac:eac-cpf/eac:cpfDescription/eac:identity/eac:nameEntry"> 
		    	
		       	<xsl:call-template name="multilanguageName">
		       		 <xsl:with-param name="list" select="./eac:eac-cpf/eac:cpfDescription/eac:identity/eac:nameEntry"/>
		       	</xsl:call-template> 
		       
		 		</xsl:when>
			<xsl:otherwise>
			</xsl:otherwise>
		</xsl:choose> 	
	</xsl:template>
	
	<!-- Template for alternative forms of name -->
	<xsl:template name="alternativeName">
		<xsl:param name="listName"/>
		<xsl:for-each select="$listName/eac:part">
			<xsl:if test="position()>1"> 
			  	<p>	
				 <xsl:value-of select="."/>
				</p>
				<!-- <xsl:apply-templates mode="other"/> -->	
			</xsl:if>
		</xsl:for-each>
	</xsl:template>
	
	<!-- template for language only one element nameEntry-->
	<xsl:template name="multilanguageName">
		<xsl:param name="list"/>
			<xsl:choose>
				<xsl:when test="count($list) > 1">
					<xsl:choose>
						<xsl:when test="$list/eac:part[@xml:lang = $language.selected] and $list/eac:part[@xml:lang = $language.selected]/text() and $list/eac:part[@xml:lang = $language.selected]/text() != ''">
						<!-- 	<span><xsl:text>selected</xsl:text></span> -->
						    <xsl:for-each select="$list/eac:part[@xml:lang = $language.selected]">
								<xsl:if test="position()=1"> 
											<xsl:value-of select="."/>	
										<!-- 	<xsl:text>case 1</xsl:text> -->
											<!-- <xsl:apply-templates select="." mode="other"/> -->	
										
											<!-- <xsl:call-template name="compositeName">
												<xsl:with-param name="list" select="."/>
											</xsl:call-template> -->	
							 	</xsl:if> 
					 	    </xsl:for-each> 
						</xsl:when>
						<xsl:when test="$list/eac:part[@xml:lang = $language.default] and $list/eac:part[@xml:lang = $language.default]/text() and $list/eac:part[@xml:lang = $language.default]/text() != ''">
						<!--  	<xsl:text>english</xsl:text>-->
						 	<xsl:for-each select="$list/eac:part[@xml:lang = $language.default]"> 
								  <xsl:if test="position()=1">
											<xsl:value-of select="."/>
										<!--  <xsl:text>case 2</xsl:text>-->
											<!-- <xsl:apply-templates select="." mode="other"/> -->
										
										<!-- <xsl:call-template name="compositeName">
												<xsl:with-param name="list" select="."/>
											</xsl:call-template>-->
								 </xsl:if>
						  	</xsl:for-each>
						</xsl:when>
						<xsl:when test="$list/eac:part[not(@xml:lang)]">
						<!--   <xsl:text>without language</xsl:text>-->
						 <!-- <xsl:variable name="currentNode" select="current()"></xsl:variable> -->
						  	<xsl:for-each select="$list/eac:part[not(@xml:lang)]"> 
							 	  <xsl:if test="position()=1"> 
									  	<xsl:value-of select="."/> 
											<!-- <xsl:apply-templates select="." mode="other"/> -->
									 <!--<xsl:call-template name="compositeName">
												<xsl:with-param name="listName" select="current()/parent::node()"/>
										</xsl:call-template> -->
							  	 </xsl:if>
						   	</xsl:for-each> 
						</xsl:when>
						<xsl:otherwise>
						<!-- 	<xsl:text>The first language</xsl:text> -->
						 	<xsl:variable name="language.first" select="$list[1]/eac:part[@xml:lang]"></xsl:variable>
							<xsl:for-each select="$list">
								<xsl:variable name="currentLang" select="current()/eac:part[@xml:lang]"></xsl:variable>
								<xsl:variable name="currentNode" select="current()/eac:part"></xsl:variable>
								<xsl:choose>
									<xsl:when test="not($currentLang)">
										<xsl:if test="position() = 1">
													<xsl:value-of select="./eac:part"/>
											     <!-- <xsl:text>case 3 </xsl:text>-->
													<!-- <xsl:apply-templates select="." mode="other"/> -->	
												
													<!--  <xsl:call-template name="compositeName">
														<xsl:with-param name="list" select="."/>
													</xsl:call-template>-->
									    </xsl:if>
									</xsl:when>
									<xsl:otherwise>
										<xsl:if test="$currentLang = $language.first">
											<xsl:if test="position() = 1">
														<xsl:value-of select="$currentNode"/>	
													<!-- <xsl:text>case 4 </xsl:text>-->
														<!-- <xsl:apply-templates select="$currentNode/eac:part" mode="other"/> -->
													
													<!--  	<xsl:call-template name="compositeName">
															<xsl:with-param name="list" select="$currentNode"/>
														</xsl:call-template>-->
										  	</xsl:if>
										</xsl:if>
									</xsl:otherwise>
								</xsl:choose>
							</xsl:for-each>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:when>
				<xsl:otherwise>
					<xsl:for-each select="$list">
								<xsl:value-of select="./eac:part"/>
							  <!--    <xsl:text>case 5</xsl:text>-->
							<!--  	<xsl:call-template name="compositeName">
									<xsl:with-param name="list" select="."/>
								</xsl:call-template>-->
					</xsl:for-each>
				</xsl:otherwise>
			
			</xsl:choose>
		
	</xsl:template>
	
	<!-- template for patronymic, surname or firstname -->
	<xsl:template name="compositeName">
		<xsl:param name="listName"></xsl:param>
	    <xsl:for-each select="$listName/eac:part">
		  	<xsl:if test= "@localType='firstname'" >
		  	 <!--      <xsl:text> FIRSTNAME</xsl:text>-->
	        <!-- 	<xsl:variable name="firstName" select="."/> -->
	        <!-- 	<xsl:value-of select="$firstName"/>   -->
	    	</xsl:if>
	   <!--   	<xsl:if test="@localType='surname'">
	    	     <xsl:text>  surNAME </xsl:text>
	       		<xsl:variable name="surName" select="@localtype"></xsl:variable>
	     	</xsl:if>
	        <xsl:if test="@localType='patronymic'">
	         <xsl:text>  patronymic </xsl:text>
	       		<xsl:variable name="patronymic" select="@localtype"></xsl:variable>
	        </xsl:if>-->
	   </xsl:for-each>
	</xsl:template> 
	
	<!-- template for language only one element -->
	<xsl:template name="multilanguageOnlyOne">
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
										<xsl:variable name="href" select="$list[@vocabularySource]"/>
										<a href="{$href}" target="_blank"><xsl:value-of select="."/></a>
									</xsl:when>
									<xsl:otherwise>
										<xsl:value-of select="."/>
									<!-- <xsl:apply-templates select="." mode="other"/> -->
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
										<xsl:variable name="href" select="$list[@vocabularySource]"/>
										<a href="{$href}" target="_blank"><xsl:value-of select="."/></a>
									</xsl:when>
									<xsl:otherwise>
										<xsl:value-of select="."/>
									<!-- <xsl:apply-templates select="." mode="other"/> -->
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
							<xsl:choose>
								<xsl:when test="not($currentLang)">
									<xsl:if test="position() = 1">
										<xsl:choose>
											<xsl:when test="($list=$placeEntryBirth or $list=$placeEntryDeath) and $list[@vocabularySource]">
												<xsl:variable name="href" select="$list[@vocabularySource]"/>
												<a href="{$href}" target="_blank"><xsl:value-of select="."/></a>
											</xsl:when>
											<xsl:otherwise>
												<xsl:value-of select="."/>
											<!-- <xsl:apply-templates select="." mode="other"/> -->
											</xsl:otherwise>
										</xsl:choose>
									</xsl:if>
								</xsl:when>
								<xsl:otherwise>
									<xsl:if test="$currentLang = $language.first">
										<xsl:if test="position() = 1">
											<xsl:choose>
												<xsl:when test="($list=$placeEntryBirth or $list=$placeEntryDeath) and $list[@vocabularySource]">
													<xsl:variable name="href" select="$list[@vocabularySource]"/>
													<a href="{$href}" target="_blank"><xsl:value-of select="$currentText"/><!-- <xsl:apply-templates select="$currentText" mode="other"/> --></a>
												</xsl:when>
												<xsl:otherwise>
													<xsl:value-of select="$currentText"/>
												<!-- <xsl:apply-templates select="$currentText" mode="other"/> -->
												</xsl:otherwise>
											</xsl:choose>
										</xsl:if>
									</xsl:if>
								</xsl:otherwise>
							</xsl:choose>
						</xsl:for-each>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
			<xsl:otherwise>
				<xsl:for-each select="$list">
					<xsl:choose>
						<xsl:when test="($list=$placeEntryBirth or $list=$placeEntryDeath) and $list[@vocabularySource]">
							<xsl:variable name="href" select="$list[@vocabularySource]"/>
							<a href="{$href}" target="_blank"><xsl:value-of select="."/></a>
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="."/>
						<!-- <xsl:apply-templates select="." mode="other"/> -->
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
	  		<xsl:call-template name="multilanguageOnlyOne">
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
	  		<xsl:call-template name="multilanguageOnlyOne">
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
	
	<!-- template for language -->
	<xsl:template name="multilanguage">
		<xsl:param name="list"/>
		<xsl:choose>
			<xsl:when test="count($list) > 1">
				<xsl:choose>
					<xsl:when test="$list[@xml:lang = $language.selected] and $list[@xml:lang = $language.selected]/text() and $list[@xml:lang = $language.selected]/text() != ''">
						<xsl:for-each select="$list">
							<xsl:variable name="currentLang" select="current()/@xml:lang"></xsl:variable>
							<xsl:if test="$currentLang = $language.selected or not($currentLang)">
								<p>
									<xsl:value-of select="."/>
									<!-- <xsl:apply-templates mode="other"/> -->
								</p>
							</xsl:if>
						</xsl:for-each>
					</xsl:when>
					<xsl:when test="$list[@xml:lang = $language.default] and $list[@xml:lang = $language.default]/text() and $list[@xml:lang = $language.default]/text() != ''">
						<xsl:for-each select="$list">
							<xsl:variable name="currentLang" select="current()/@xml:lang"></xsl:variable>
							<xsl:if test="not($currentLang) or $currentLang = $language.default">
								<p>
									<xsl:value-of select="."/>
									<!-- <xsl:apply-templates mode="other"/> -->
								</p>
							</xsl:if>
						</xsl:for-each>
					</xsl:when>
					<xsl:otherwise> <!-- first language -->
						<xsl:for-each select="$list">
							<xsl:variable name="currentLang" select="current()/@xml:lang"></xsl:variable>
							<xsl:choose>
								<xsl:when test="not($currentLang)">
									<p class="multilanguageNoLang">
										<xsl:value-of select="."/>
										<!-- <xsl:apply-templates mode="other"/> -->
									</p>
								</xsl:when>
								<xsl:when test="$currentLang">
									  <xsl:variable name="classValue">
						                   <xsl:text>multilanguageLang_</xsl:text><xsl:value-of select="$currentLang"></xsl:value-of>
					                  </xsl:variable>
									  <p class="{$classValue}">
									     <xsl:value-of select="."/>
									     <!-- <xsl:apply-templates mode="other"/> -->
									  </p>
								</xsl:when>	  
							</xsl:choose>
						</xsl:for-each>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
			<xsl:otherwise>
				<xsl:for-each select="$list">
					<p>
						<xsl:value-of select="."/>
						<!-- <xsl:apply-templates mode="other"/> -->
					</p>
				</xsl:for-each>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>	
</xsl:stylesheet>