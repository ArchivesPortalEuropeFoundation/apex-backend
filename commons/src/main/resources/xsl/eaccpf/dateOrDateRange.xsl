<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xmlns='http://www.w3.org/1999/xhtml' xmlns:eac="urn:isbn:1-931666-33-4"
	xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:ape="http://www.archivesportaleurope.eu/xslt/extensions"
	exclude-result-prefixes="xlink xlink xsi eac ape">

	<xsl:output method="html" indent="yes" version="4.0" encoding="UTF-8" />

	<!-- template for commons dates -->
	<xsl:template name="commonDates">
		<xsl:param name="date"/>
		<xsl:param name="dateRange"/>
		<xsl:param name="dateSet"/>
		<xsl:param name="mode"/>

		<xsl:if test="(($mode = 'default' or $mode = 'showAll') and ($date or $dateRange or $dateSet))
					or ($mode = 'other' and ($date[@xml:lang = $translationLanguage]/text()
						or $dateRange[eac:fromDate[@xml:lang = $translationLanguage]]/text()
						or $dateRange[eac:toDate[@xml:lang = $translationLanguage]]/text()
						or $dateSet[eac:date[@xml:lang = $translationLanguage]]/text()
						or $dateSet[eac:dateRange[eac:fromDate[@xml:lang = $translationLanguage]]]/text()
						or $dateSet[eac:dateRange[eac:toDate[@xml:lang = $translationLanguage]]]/text()))">
		    <div class="row">
		    	<div class="leftcolumn">
				   		<h2><xsl:value-of select="ape:resource('eaccpf.portal.date')"/><xsl:text>:</xsl:text></h2>
				</div>          
		        <div class="rightcolumn">
		        	<span class="nameEtryDates">
						<!-- when there are only 1 dateSet -->
						<xsl:if test="$dateSet and (($dateSet/eac:dateRange/eac:fromDate or $dateSet/eac:dateRange/eac:toDate) or ($dateSet/eac:date and $dateSet/eac:date/text()))">
							<xsl:apply-templates select="$dateSet">
				   				<xsl:with-param name="mode" select="$mode"/>
							</xsl:apply-templates>
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

	<!-- Template for toDate or fromDate to detect the unknown value-->
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
		<xsl:param name="mode"/>
		<xsl:if test="eac:dateRange or eac:date">
			<!-- Checks if exists any content in the translation language. -->
			<xsl:if test="$mode = 'other' and (eac:date[@xml:lang = $translationLanguage] or eac:dateRange[eac:fromDate[@xml:lang = $translationLanguage] or eac:toDate[@xml:lang = $translationLanguage]])">
				<xsl:if test="name(eac:date/parent::node()) = 'existDates' or name(eac:date/parent::node()/parent::node()) = 'existDates'
							or name(eac:dateRange/parent::node()) = 'existDates' or name(eac:dateRange/parent::node()/parent::node()) = 'existDates'">
					<xsl:text> (</xsl:text>
				</xsl:if>
					<span class="nameEtryDates">
						<xsl:if test="eac:date[@xml:lang = $translationLanguage] and eac:date[@xml:lang = $translationLanguage]/text() and eac:date[@xml:lang = $translationLanguage]/text() != ''">
							<xsl:call-template name="multilanguageDate">
								<xsl:with-param name="list" select="eac:date[@xml:lang = $translationLanguage]"/>
							</xsl:call-template>
						</xsl:if>
						<xsl:if test="eac:dateRange[eac:fromDate[@xml:lang = $translationLanguage] or eac:toDate[@xml:lang = $translationLanguage]]
									and eac:dateRange[eac:fromDate[@xml:lang = $translationLanguage] or eac:toDate[@xml:lang = $translationLanguage]]/text()
									and eac:dateRange[eac:fromDate[@xml:lang = $translationLanguage] or eac:toDate[@xml:lang = $translationLanguage]]/text() != ''">
							<xsl:call-template name="multilanguageDateRange">
								<xsl:with-param name="list" select="eac:dateRange[eac:fromDate[@xml:lang = $translationLanguage] or eac:toDate[@xml:lang = $translationLanguage]]"/>	
							</xsl:call-template>
						</xsl:if>
					</span>
				<xsl:if test="name(eac:date/parent::node()) = 'existDates' or name(eac:date/parent::node()/parent::node()) = 'existDates'
							or name(eac:dateRange/parent::node()) = 'existDates' or name(eac:dateRange/parent::node()/parent::node()) = 'existDates'">
					<xsl:text>)</xsl:text>
				</xsl:if>
			</xsl:if>
			<xsl:if test="$mode = 'showAll'">
				<xsl:if test="name(eac:date/parent::node()) = 'existDates' or name(eac:date/parent::node()/parent::node()) = 'existDates'
							or name(eac:dateRange/parent::node()) = 'existDates' or name(eac:dateRange/parent::node()/parent::node()) = 'existDates'">
					<xsl:text> (</xsl:text>
				</xsl:if>
					<span class="nameEtryDates">
						<xsl:call-template name="showAllDates">
							<xsl:with-param name="list" select="eac:date"/>
						</xsl:call-template>
						<xsl:if test="eac:dateRange/eac:fromDate or eac:dateRange/eac:toDate">
							<xsl:call-template name="showAllDateRanges">
								<xsl:with-param name="list" select="eac:dateRange"/>	
							</xsl:call-template>
						</xsl:if>
					</span>
				<xsl:if test="name(eac:date/parent::node()) = 'existDates' or name(eac:date/parent::node()/parent::node()) = 'existDates'
							or name(eac:dateRange/parent::node()) = 'existDates' or name(eac:dateRange/parent::node()/parent::node()) = 'existDates'">
					<xsl:text>)</xsl:text>
				</xsl:if>
			</xsl:if>
			<xsl:if test="$mode = 'default'">
				<xsl:if test="name(eac:date/parent::node()) = 'existDates' or name(eac:date/parent::node()/parent::node()) = 'existDates'
							or name(eac:dateRange/parent::node()) = 'existDates' or name(eac:dateRange/parent::node()/parent::node()) = 'existDates'">
					<xsl:text> (</xsl:text>
				</xsl:if>
					<span class="nameEtryDates">
						<xsl:call-template name="multilanguageDate">
							<xsl:with-param name="list" select="eac:date"/>
						</xsl:call-template>
						<xsl:if test="eac:dateRange/eac:fromDate or eac:dateRange/eac:toDate">
							<xsl:call-template name="multilanguageDateRange">
								<xsl:with-param name="list" select="eac:dateRange"/>	
							</xsl:call-template>
						</xsl:if>
					</span>
				<xsl:if test="name(eac:date/parent::node()) = 'existDates' or name(eac:date/parent::node()/parent::node()) = 'existDates'
							or name(eac:dateRange/parent::node()) = 'existDates' or name(eac:dateRange/parent::node()/parent::node()) = 'existDates'">
					<xsl:text>)</xsl:text>
				</xsl:if>
			</xsl:if>
		</xsl:if>
	</xsl:template>

	<!-- Template that will show all the dates in the passed list
		 independent of its language. -->
	<xsl:template name="showAllDates">
		<xsl:param name="list"/>

		<xsl:for-each select="$list">
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
	</xsl:template>

	<!-- Template that will show all the date ranges in the passed list
		 independent of its language. -->
	<xsl:template name="showAllDateRanges">
		<xsl:param name="list"/>

		<xsl:for-each select="$list">
			<xsl:choose>
				<xsl:when test="./eac:fromDate">
					<xsl:if test="position() > 1 or (position() = 1 and ./parent::node()/eac:date/text())">
						<xsl:text>, </xsl:text>
					</xsl:if>
					<xsl:call-template name="fromToDate">
						<xsl:with-param name="dateRange" select="."/>
					</xsl:call-template>
				</xsl:when>
				<xsl:when test="./eac:toDate">
					<xsl:if test="position() > 1  or (position() = 1 and ./parent::node()/eac:date/text())">
						<xsl:text>, </xsl:text>
					</xsl:if>
					<xsl:call-template name="fromToDate">
						<xsl:with-param name="dateRange" select="."/>
					</xsl:call-template>
				</xsl:when>
				<xsl:otherwise/>
			</xsl:choose>
		</xsl:for-each>
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
					<xsl:when test="$list[@xml:lang = $lang.navigator] and $list[@xml:lang = $lang.navigator]/text() and $list[@xml:lang = $lang.navigator]/text() != ''">
						<xsl:for-each select="$list[@xml:lang = $lang.navigator]">
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
					<xsl:when test="($list/eac:fromDate[@xml:lang = $lang.navigator] and $list/eac:fromDate[@xml:lang = $lang.navigator]/text() and $list/eac:fromDate[@xml:lang = $lang.navigator]/text() != '')
					                 or ($list/eac:toDate[@xml:lang = $lang.navigator] and $list/eac:toDate[@xml:lang = $lang.navigator]/text() and $list/eac:toDate[@xml:lang = $lang.navigator]/text() != '')">
						<xsl:for-each select="$list">
						    <xsl:variable name="currentLangFrom" select="current()/eac:fromDate/@xml:lang"/>
						    <xsl:variable name="currentLangTo" select="current()/eac:toDate/@xml:lang"/>
						    <xsl:choose>
						    	<xsl:when test="./eac:fromDate">
						    		<xsl:if test="($currentLangFrom = $lang.navigator)">
						    			<xsl:if test="position() > 1 or (position() = 1 and ./parent::node()/eac:date/text())">
											<xsl:text>, </xsl:text>
										</xsl:if>
										<xsl:call-template name="fromToDate">
				          	 				<xsl:with-param name="dateRange" select="."/>
			                			</xsl:call-template>
									</xsl:if>
						    	</xsl:when>
						    	<xsl:when test="./eac:toDate">
						    		<xsl:if test="($currentLangTo = $lang.navigator)">
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
						<xsl:when test="$list[@xml:lang = $lang.navigator] and $list[@xml:lang = $lang.navigator]/text() and $list[@xml:lang = $lang.navigator]/text() != ''">
						    <xsl:for-each select="$list[@xml:lang = $lang.navigator]">
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

	<!-- Template to display the date of the entity in case the translation
		 selected will be a specific language.  -->
	<xsl:template name="dateInitialDefault" >
		<xsl:param name="dateElement"/>
		<xsl:param name="entityType"/>

		<div class="row">
			<div class="leftcolumn">
				<h2>
					<xsl:value-of select="ape:resource('eaccpf.portal.date')"/>
		   			<xsl:text>:</xsl:text>
				</h2>	
			</div>
			<div class="rightcolumn">
				<xsl:if test="name($dateElement) = 'date'">
					<xsl:apply-templates select="$dateElement"/>
				</xsl:if> 
				<xsl:if test="name($dateElement) = 'dateSet' and $dateElement/eac:date">
					<xsl:variable name="datesList" select="$dateElement/eac:date[@xml:lang = $translationLanguage]"/>
					<xsl:apply-templates select="$datesList[1]"/>
				</xsl:if>
			</div>
		</div>
	</xsl:template>

	<!-- Template to display the date of the entity in case the translation
		 selected will be "Translations" or "Show all".  -->
	<xsl:template name="dateInitial" >
		<xsl:param name="existDates"/>
		<xsl:param name="entityType"/>

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
	</xsl:template>
</xsl:stylesheet>