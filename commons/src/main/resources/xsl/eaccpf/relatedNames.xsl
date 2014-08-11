<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xmlns='http://www.w3.org/1999/xhtml' xmlns:eac="urn:isbn:1-931666-33-4"
	xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:ape="http://www.archivesportaleurope.eu/xslt/extensions"
	exclude-result-prefixes="xlink xlink xsi eac ape">

	<xsl:output method="html" indent="yes" version="4.0" encoding="UTF-8" />

	<!-- Template for related names. -->
	<xsl:template name="relatedNames">
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
					   		<xsl:when test="./eac:relationEntry[not(@localType)]">
					   			<xsl:call-template name="multilanguageRelations">
						   			 	<xsl:with-param name="list" select="./eac:relationEntry[not(@localType)]"/>
						   		</xsl:call-template>
					   		</xsl:when>
					   		<xsl:otherwise>
					   			<xsl:variable name="link" select="./@xlink:href"/>
								<xsl:choose>
									<xsl:when test="starts-with($link, 'http') or starts-with($link, 'https') or starts-with($link, 'ftp') or starts-with($link, 'www')">
										<a href="{$link}" target="_blank">
					   						<xsl:value-of select="ape:resource('eaccpf.portal.goToRelatedName')"/>
					   					</a>

							   			<xsl:call-template name="relationType">
											<xsl:with-param name="current" select="."/>
										</xsl:call-template>
									</xsl:when>
									<xsl:when test="./eac:relationEntry[@localType='agencyCode'] and $link != ''">
										<xsl:variable name="href" select="./eac:relationEntry[@localType='agencyCode']"/>
										<xsl:if test="not(starts-with($href, 'http')) and not(starts-with($href, 'https')) and not(starts-with($href, 'ftp')) and not(starts-with($href, 'www'))">
											<xsl:variable name="aiCode" select="ape:checkAICode($href, '', 'true')"/>
											<xsl:choose>
												<xsl:when test="$aiCode != 'ERROR' and $aiCode != ''">
													<xsl:variable name="encodedAiCode" select="ape:encodeSpecialCharacters($aiCode)" />
													<xsl:variable name="encodedlink" select="ape:encodeSpecialCharacters($link)" />
													<a href="{$eacUrlBase}/aicode/{$encodedAiCode}/type/ec/id/{$encodedlink}" target="_blank">
														<xsl:value-of select="ape:resource('eaccpf.portal.goToRelatedName')"/>
													</a>
												</xsl:when>
												<xsl:otherwise>
													<xsl:value-of select="ape:resource('eaccpf.portal.goToRelatedName')"/>
												</xsl:otherwise>
											</xsl:choose>

								   			<xsl:call-template name="relationType">
												<xsl:with-param name="current" select="."/>
											</xsl:call-template>
										</xsl:if>
									</xsl:when>
									<xsl:otherwise>
										<xsl:if test="$link != ''">
											<xsl:variable name="aiCode" select="ape:aiFromEac($link, '')"/>
											<xsl:choose>
												<xsl:when test="$aiCode != 'ERROR' and $aiCode != ''">
													<xsl:variable name="encodedAiCode" select="ape:encodeSpecialCharacters($aiCode)" />
													<xsl:variable name="encodedlink" select="ape:encodeSpecialCharacters($link)" />
													<a href="{$eacUrlBase}/aicode/{$encodedAiCode}/type/ec/id/{$encodedlink}" target="_blank">
														<xsl:value-of select="ape:resource('eaccpf.portal.goToRelatedName')"/>
													</a>
												</xsl:when>
												<xsl:otherwise>
													<xsl:value-of select="ape:resource('eaccpf.portal.goToRelatedName')"/>
												</xsl:otherwise>
											</xsl:choose>

								   			<xsl:call-template name="relationType">
												<xsl:with-param name="current" select="."/>
											</xsl:call-template>
										</xsl:if>

										<xsl:if test="$link = ''">
											<xsl:value-of select="ape:resource('eaccpf.portal.goToRelatedName')"/>

								   			<xsl:call-template name="relationType">
												<xsl:with-param name="current" select="."/>
											</xsl:call-template>
										</xsl:if>
									</xsl:otherwise>
								</xsl:choose>
					   		</xsl:otherwise>
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
	</xsl:template>
</xsl:stylesheet>