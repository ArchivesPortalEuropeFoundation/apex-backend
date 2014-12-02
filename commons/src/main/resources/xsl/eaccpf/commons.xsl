<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xmlns='http://www.w3.org/1999/xhtml' xmlns:eac="urn:isbn:1-931666-33-4"
	xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:ape="http://www.archivesportaleurope.eu/xslt/extensions"
	exclude-result-prefixes="xlink xlink xsi eac ape">

	<xsl:output method="html" indent="yes" version="4.0" encoding="UTF-8" />

	<!-- Template for multilanguage resourceRelations with @localType='title'. -->
	<xsl:template name="multilanguageRelationsTitle">
		<xsl:param name="list"/>
		<xsl:choose>
			<xsl:when test="count($list) > 1">
				<xsl:choose>
					<xsl:when test="$list[@xml:lang = $language.selected] and $list[@xml:lang = $language.selected]/text() and $list[@xml:lang = $language.selected]/text() != ''">
				 	 	<xsl:variable name="link" select="$list/parent::node()/@xlink:href"/>
				 	 	<xsl:variable name="first" select="$list[1]"/>
				 	 	<xsl:choose>
				 	 		<xsl:when test="$list/parent::node()/@xlink:href">
				 	 			<xsl:if test="starts-with($link, 'http') or starts-with($link, 'https') or starts-with($link, 'ftp') or starts-with($link, 'www')">
									<a href="{$link}" target="_blank">
										<xsl:for-each select="$list[@xml:lang = $language.selected]">
											<xsl:if test="@localType='title'">
												<xsl:if test="(position() > 1 and $first[@localType='title'] ) or (position() > 2 and $first[@localType='id'])">
													<xsl:text>. </xsl:text>
												</xsl:if>
												<xsl:apply-templates select="." mode="other"/>
											</xsl:if>
									 	</xsl:for-each>
									</a>
						 	   </xsl:if>
						 	   <xsl:if test="not(starts-with($link, 'http')) and not(starts-with($link, 'https')) and not(starts-with($link, 'ftp')) and not(starts-with($link, 'www'))">
									<xsl:choose>
										<xsl:when test="./parent::node()[eac:relationEntry[@localType='agencyCode']]">
											<xsl:variable name="href" select="./parent::node()/eac:relationEntry[@localType='agencyCode']"/>
									  		<xsl:if test="not(starts-with($href, 'http')) and not(starts-with($href, 'https')) and not(starts-with($href, 'ftp')) and not(starts-with($href, 'www'))">
												<xsl:variable name="aiCode" select="ape:aiFromEad($link, $href)"/>
												<xsl:choose>
													<xsl:when test="$aiCode != 'ERROR' and $aiCode != ''">
														<a href="{$eadUrl}/{$aiCode}" target="_blank">
															<xsl:for-each select="$list[@xml:lang = $language.selected]">
																<xsl:if test="position() > 1">
																	<xsl:text>. </xsl:text>
																</xsl:if>
																<xsl:apply-templates select="." mode="other"/>
														 	</xsl:for-each>
														</a>
													</xsl:when>
													<xsl:otherwise>
														<xsl:for-each select="$list[@xml:lang = $language.selected]">
															<xsl:if test="position() > 1">
																<xsl:text>. </xsl:text>
															</xsl:if>
															<xsl:apply-templates select="." mode="other"/>
													 	</xsl:for-each>
													</xsl:otherwise>
												</xsl:choose>
											</xsl:if>
										</xsl:when>
									</xsl:choose>
						 	   </xsl:if>
				 	 		</xsl:when>
				 	 		<xsl:otherwise>
								<xsl:call-template name="resourceRelationListId">
									<xsl:with-param name="list" select="$list"/>
								</xsl:call-template>
				 	 		</xsl:otherwise>
			 	 	</xsl:choose>
				 	 	<xsl:call-template name="relationType">
							<xsl:with-param name="current" select="$list/parent::node()"/>
						</xsl:call-template> 
					</xsl:when>
					<xsl:when test="$list[@xml:lang = $lang.navigator] and $list[@xml:lang = $lang.navigator]/text() and $list[@xml:lang = $lang.navigator]/text() != ''">
					 	 	<xsl:variable name="link" select="$list/parent::node()/@xlink:href"/>
					 	 	<xsl:choose>
					 	 		<xsl:when test="$list/parent::node()/@xlink:href">
					 	 			<xsl:if test="starts-with($link, 'http') or starts-with($link, 'https') or starts-with($link, 'ftp') or starts-with($link, 'www')">
										<a href="{$link}" target="_blank">
											<xsl:for-each select="$list[@xml:lang = $lang.navigator]">
												<xsl:if test="position() > 1">
													<xsl:text>. </xsl:text>
												</xsl:if>
												<xsl:apply-templates select="." mode="other"/>
										 	</xsl:for-each>
										</a>
							 	   </xsl:if>
							 	   <xsl:if test="not(starts-with($link, 'http')) and not(starts-with($link, 'https')) and not(starts-with($link, 'ftp')) and not(starts-with($link, 'www'))">
										<xsl:choose>
											<xsl:when test="./parent::node()[eac:relationEntry[@localType='agencyCode']]">
												<xsl:variable name="href" select="./parent::node()/eac:relationEntry[@localType='agencyCode']"/>
										  		<xsl:if test="not(starts-with($href, 'http')) and not(starts-with($href, 'https')) and not(starts-with($href, 'ftp')) and not(starts-with($href, 'www'))">
													<xsl:variable name="aiCode" select="ape:aiFromEad($link, $href)"/>
													<xsl:choose>
														<xsl:when test="$aiCode != 'ERROR' and $aiCode != ''">
															<a href="{$eadUrl}/{$aiCode}" target="_blank">
																<xsl:for-each select="$list[@xml:lang = $lang.navigator]">
																	<xsl:if test="position() > 1">
																		<xsl:text>. </xsl:text>
																	</xsl:if>
																	<xsl:apply-templates select="." mode="other"/>
															 	</xsl:for-each>
															</a>
														</xsl:when>
														<xsl:otherwise>
															<xsl:for-each select="$list[@xml:lang = $lang.navigator]">
																<xsl:if test="position() > 1">
																	<xsl:text>. </xsl:text>
																</xsl:if>
																<xsl:apply-templates select="." mode="other"/>
														 	</xsl:for-each>
														</xsl:otherwise>
													</xsl:choose>
												</xsl:if>
											</xsl:when>
											<xsl:otherwise>
												<xsl:variable name="aiCode" select="ape:aiFromEad($link, '')"/>
												<xsl:choose>
													<xsl:when test="$aiCode != 'ERROR' and $aiCode != ''">
														<a href="{$eadUrl}/{$aiCode}" target="_blank">
															<xsl:for-each select="$list[@xml:lang = $lang.navigator]">
																<xsl:if test="position() > 1">
																	<xsl:text>. </xsl:text>
																</xsl:if>
																<xsl:apply-templates select="." mode="other"/>
														 	</xsl:for-each>
														</a>
													</xsl:when>
													<xsl:otherwise>
														<xsl:for-each select="$list[@xml:lang = $lang.navigator]">
															<xsl:if test="position() > 1">
																<xsl:text>. </xsl:text>
															</xsl:if>
															<xsl:apply-templates select="." mode="other"/>
													 	</xsl:for-each>
													</xsl:otherwise>
												</xsl:choose>
											</xsl:otherwise>
										</xsl:choose>
							 	   </xsl:if>
					 	 		</xsl:when>
					 	 		<xsl:otherwise>
									<xsl:call-template name="resourceRelationListId">
										<xsl:with-param name="list" select="$list"/>
									</xsl:call-template>
					 	 		</xsl:otherwise>
					 	 	</xsl:choose>
					 	 	<xsl:call-template name="relationType">
								<xsl:with-param name="current" select="$list/parent::node()"/>
							</xsl:call-template> 
					</xsl:when>				
					<xsl:when test="$list[@xml:lang = $language.default] and $list[@xml:lang = $language.default]/text() and $list[@xml:lang = $language.default]/text() != ''">
						<xsl:variable name="link" select="$list/parent::node()/@xlink:href"/>
					 	 	<xsl:choose>
					 	 		<xsl:when test="$list/parent::node()/@xlink:href">
					 	 			<xsl:if test="starts-with($link, 'http') or starts-with($link, 'https') or starts-with($link, 'ftp') or starts-with($link, 'www')">
										<a href="{$link}" target="_blank">
											<xsl:for-each select="$list[@xml:lang = $language.default]">
												<xsl:if test="position() > 1">
													<xsl:text>. </xsl:text>
												</xsl:if>
												<xsl:apply-templates select="." mode="other"/>
										 	</xsl:for-each>
										</a>
							 	   </xsl:if>
							 	   <xsl:if test="not(starts-with($link, 'http')) and not(starts-with($link, 'https')) and not(starts-with($link, 'ftp')) and not(starts-with($link, 'www'))">
										<xsl:choose>
											<xsl:when test="./parent::node()[eac:relationEntry[@localType='agencyCode']]">
												<xsl:variable name="href" select="./parent::node()/eac:relationEntry[@localType='agencyCode']"/>
										  		<xsl:if test="not(starts-with($href, 'http')) and not(starts-with($href, 'https')) and not(starts-with($href, 'ftp')) and not(starts-with($href, 'www'))">
													<xsl:variable name="aiCode" select="ape:aiFromEad($link, $href)"/>
													<xsl:choose>
														<xsl:when test="$aiCode != 'ERROR' and $aiCode != ''">
															<a href="{$eadUrl}/{$aiCode}" target="_blank">
																<xsl:for-each select="$list[@xml:lang = $language.default]">
																	<xsl:if test="position() > 1">
																		<xsl:text>. </xsl:text>
																	</xsl:if>
																	<xsl:apply-templates select="." mode="other"/>
															 	</xsl:for-each>
															</a>
														</xsl:when>
														<xsl:otherwise>
															<xsl:for-each select="$list[@xml:lang = $language.default]">
																<xsl:if test="position() > 1">
																	<xsl:text>. </xsl:text>
																</xsl:if>
																<xsl:apply-templates select="." mode="other"/>
														 	</xsl:for-each>
														</xsl:otherwise>
													</xsl:choose>
												</xsl:if>
											</xsl:when>
											<xsl:otherwise>
												<xsl:variable name="aiCode" select="ape:aiFromEad($link, '')"/>
												<xsl:choose>
													<xsl:when test="$aiCode != 'ERROR' and $aiCode != ''">
														<a href="{$eadUrl}/{$aiCode}" target="_blank">
															<xsl:for-each select="$list[@xml:lang = $language.default]">
																<xsl:if test="position() > 1">
																	<xsl:text>. </xsl:text>
																</xsl:if>
																<xsl:apply-templates select="." mode="other"/>
														 	</xsl:for-each>
														</a>
													</xsl:when>
													<xsl:otherwise>
														<xsl:for-each select="$list[@xml:lang = $language.default]">
															<xsl:if test="position() > 1">
																<xsl:text>. </xsl:text>
															</xsl:if>
															<xsl:apply-templates select="." mode="other"/>
													 	</xsl:for-each>
													</xsl:otherwise>
												</xsl:choose>
											</xsl:otherwise>
										</xsl:choose>
							 	   </xsl:if>
					 	 		</xsl:when>
					 	 		<xsl:otherwise>
					 	 			<xsl:for-each select="$list[@xml:lang = $language.default]">
										<xsl:if test="position() > 1">
											<xsl:text>. </xsl:text>
										</xsl:if>
										<xsl:apply-templates select="." mode="other"/>
								 	</xsl:for-each>
					 	 		</xsl:otherwise>
					 	 	</xsl:choose>
					 	 	<xsl:call-template name="relationType">
								<xsl:with-param name="current" select="$list/parent::node()"/>
							</xsl:call-template> 
					</xsl:when>
					<xsl:when test="$list[not(@xml:lang)] and $list[not(@xml:lang)]/text() and $list[not(@xml:lang)]/text() != ''">
						  	<xsl:variable name="link" select="$list/parent::node()/@xlink:href"/>
					 	 	<xsl:choose>
					 	 		<xsl:when test="$list/parent::node()/@xlink:href">
					 	 			<xsl:if test="starts-with($link, 'http') or starts-with($link, 'https') or starts-with($link, 'ftp') or starts-with($link, 'www')">
										<a href="{$link}" target="_blank">
											<xsl:choose>
												<xsl:when test="$list[@localType='title']">
													<xsl:for-each select="$list[@localType='title']">
														<xsl:if test="position() > 1">
															<xsl:text>. </xsl:text>
														</xsl:if>
														<xsl:apply-templates select="." mode="other"/>
												 	</xsl:for-each>
												</xsl:when>
												<xsl:when test="$list[not(@localType)]">
													<xsl:for-each select="$list[not(@localType)]">
														<xsl:if test="position() > 1">
															<xsl:text>. </xsl:text>
														</xsl:if>
														<xsl:apply-templates select="." mode="other"/>
												 	</xsl:for-each>
												</xsl:when>
												<xsl:otherwise>
													<xsl:for-each select="$list[not(@xml:lang)]">
														<xsl:if test="position() > 1">
															<xsl:text>. </xsl:text>
														</xsl:if>
														<xsl:apply-templates select="." mode="other"/>
												 	</xsl:for-each>
												</xsl:otherwise>
											</xsl:choose>
										</a>
							 	   </xsl:if>
							 	   <xsl:if test="not(starts-with($link, 'http')) and not(starts-with($link, 'https')) and not(starts-with($link, 'ftp')) and not(starts-with($link, 'www'))">
										<xsl:choose>
											<xsl:when test="./parent::node()[eac:relationEntry[@localType='agencyCode']]">
												<xsl:variable name="href" select="./parent::node()/eac:relationEntry[@localType='agencyCode']"/>
										  		<xsl:if test="not(starts-with($href, 'http')) and not(starts-with($href, 'https')) and not(starts-with($href, 'ftp')) and not(starts-with($href, 'www'))">
													<xsl:variable name="aiCode" select="ape:aiFromEad($link, $href)"/>
													<xsl:choose>
														<xsl:when test="$aiCode != 'ERROR' and $aiCode != ''">
															<a href="{$eadUrl}/{$aiCode}" target="_blank">
																<xsl:for-each select="$list[not(@xml:lang)]">
																	<xsl:if test="position() > 1">
																		<xsl:text>. </xsl:text>
																	</xsl:if>
																	<xsl:apply-templates select="." mode="other"/>
															 	</xsl:for-each>
															</a>
														</xsl:when>
														<xsl:otherwise>
															<xsl:for-each select="$list[not(@xml:lang)]">
																<xsl:if test="position() > 1">
																	<xsl:text>. </xsl:text>
																</xsl:if>
																<xsl:apply-templates select="." mode="other"/>
														 	</xsl:for-each>
														</xsl:otherwise>
													</xsl:choose>
												</xsl:if>
											</xsl:when>
											<xsl:otherwise>
												<xsl:variable name="aiCode" select="ape:aiFromEad($link, '')"/>
												<xsl:choose>
													<xsl:when test="$aiCode != 'ERROR' and $aiCode != ''">
														<a href="{$eadUrl}/{$aiCode}" target="_blank">
															<xsl:for-each select="$list[not(@xml:lang)]">
																<xsl:if test="position() > 1">
																	<xsl:text>. </xsl:text>
																</xsl:if>
																<xsl:apply-templates select="." mode="other"/>
														 	</xsl:for-each>
														</a>
													</xsl:when>
													<xsl:otherwise>
														<xsl:for-each select="$list[not(@xml:lang)]">
															<xsl:if test="position() > 1">
																<xsl:text>. </xsl:text>
															</xsl:if>
															<xsl:apply-templates select="." mode="other"/>
													 	</xsl:for-each>
													</xsl:otherwise>
												</xsl:choose>
											</xsl:otherwise>
										</xsl:choose>
							 	   </xsl:if>
					 	 		</xsl:when>
					 	 		<xsl:otherwise>
								 	<xsl:call-template name="resourceRelationListId">
								 		<xsl:with-param name="list" select="$list" />
								 	</xsl:call-template>
					 	 		</xsl:otherwise>
					 	 	</xsl:choose>
					 	 	<xsl:call-template name="relationType">
								<xsl:with-param name="current" select="$list/parent::node()"/>
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
													<xsl:if test="position() > 1">
														<xsl:text>. </xsl:text>
													</xsl:if>
													<xsl:apply-templates select="." mode="other"/>
												</xsl:if>	
										 	</xsl:for-each>
										</a>
							 	   </xsl:if>
							 	   <xsl:if test="not(starts-with($link, 'http')) and not(starts-with($link, 'https')) and not(starts-with($link, 'ftp')) and not(starts-with($link, 'www'))">
										<xsl:choose>
											<xsl:when test="./parent::node()[eac:relationEntry[@localType='agencyCode']]">
												<xsl:variable name="href" select="./parent::node()/eac:relationEntry[@localType='agencyCode']"/>
										  		<xsl:if test="not(starts-with($href, 'http')) and not(starts-with($href, 'https')) and not(starts-with($href, 'ftp')) and not(starts-with($href, 'www'))">
													<xsl:variable name="aiCode" select="ape:aiFromEad($link, $href)"/>
													<xsl:choose>
														<xsl:when test="$aiCode != 'ERROR' and $aiCode != ''">
															<a href="{$eadUrl}/{$aiCode}" target="_blank">
																<xsl:for-each select="$list">
																	<xsl:variable name="currentLang" select="current()/@xml:lang"></xsl:variable>
																	<xsl:if test="$currentLang = $language.first">
																		<xsl:if test="position() > 1">
																			<xsl:text>. </xsl:text>
																		</xsl:if>
																		<xsl:apply-templates select="." mode="other"/>
																	</xsl:if>
															 	</xsl:for-each>
															</a>
														</xsl:when>
														<xsl:otherwise>
															<xsl:for-each select="$list">
																<xsl:variable name="currentLang" select="current()/@xml:lang"></xsl:variable>
																<xsl:if test="$currentLang = $language.first">
																	<xsl:if test="position() > 1">
																		<xsl:text>. </xsl:text>
																	</xsl:if>
																	<xsl:apply-templates select="." mode="other"/>
																</xsl:if>
														 	</xsl:for-each>
														</xsl:otherwise>
													</xsl:choose>
												</xsl:if>
											</xsl:when>
											<xsl:otherwise>
												<xsl:variable name="aiCode" select="ape:aiFromEad($link, '')"/>
												<xsl:choose>
													<xsl:when test="$aiCode != 'ERROR' and $aiCode != ''">
														<a href="{$eadUrl}/{$aiCode}" target="_blank">
															<xsl:for-each select="$list">
																<xsl:variable name="currentLang" select="current()/@xml:lang"></xsl:variable>
																<xsl:if test="$currentLang = $language.first">
																	<xsl:if test="position() > 1">
																		<xsl:text>. </xsl:text>
																	</xsl:if>
																	<xsl:apply-templates select="." mode="other"/>
																</xsl:if>
														 	</xsl:for-each>
														</a>
													</xsl:when>
													<xsl:otherwise>
														<xsl:for-each select="$list">
															<xsl:variable name="currentLang" select="current()/@xml:lang"></xsl:variable>
															<xsl:if test="$currentLang = $language.first">
																<xsl:if test="position() > 1">
																	<xsl:text>. </xsl:text>
																</xsl:if>
																<xsl:apply-templates select="." mode="other"/>
															</xsl:if>
													 	</xsl:for-each>
													</xsl:otherwise>
												</xsl:choose>
											</xsl:otherwise>
										</xsl:choose>
							 	   </xsl:if>
					 	 		</xsl:when>
					 	 		<xsl:otherwise>
						   			<xsl:choose>
							   			<xsl:when test="./@xlink:href != ''">
											<xsl:variable name="link" select="./@xlink:href"/>
											<xsl:choose>
												<xsl:when test="starts-with($link, 'http') or starts-with($link, 'https') or starts-with($link, 'ftp') or starts-with($link, 'www')">
													<a href="{$link}" target="_blank">
								   						<xsl:value-of select="ape:resource('eaccpf.portal.goToRelatedResource')"/>
								   					</a>
												</xsl:when>
												<xsl:when test="./eac:relationEntry[@localType='agencyCode']">
													<xsl:variable name="href" select="./eac:relationEntry[@localType='agencyCode']"/>
											  		<xsl:if test="not(starts-with($href, 'http')) and not(starts-with($href, 'https')) and not(starts-with($href, 'ftp')) and not(starts-with($href, 'www'))">
														<xsl:variable name="aiCode" select="ape:aiFromEad($link, $href)"/>
														<xsl:choose>
															<xsl:when test="$aiCode != 'ERROR' and $aiCode != ''">
																<a href="{$eadUrl}/{$aiCode}" target="_blank">
																	<xsl:value-of select="ape:resource('eaccpf.portal.goToRelatedResource')"/>
																</a>
															</xsl:when>
															<xsl:otherwise>
																<xsl:value-of select="ape:resource('eaccpf.portal.goToRelatedResource')"/>
															</xsl:otherwise>
														</xsl:choose>
													</xsl:if>
												</xsl:when>
												<xsl:otherwise>
													<xsl:call-template name="resourceRelationListId">
														<xsl:with-param name="list" select="$list"></xsl:with-param>
													</xsl:call-template>
												</xsl:otherwise>
											</xsl:choose>
			
											<xsl:if test="./@xlink:href != ''">
									   			<xsl:call-template name="relationType">
													<xsl:with-param name="current" select="."/>
												</xsl:call-template>
											</xsl:if>
										</xsl:when>
										<xsl:otherwise> <!-- when ./@xlink:href = '' -->
											<!-- search an <relationEntry @localType="id"> -->
											<xsl:choose>
												<xsl:when test="./eac:relationEntry[@localType='id']"> <!-- id is here -->
													<xsl:variable name="href" select="./eac:relationEntry[@localType='id']"/>
													<xsl:value-of select="href"></xsl:value-of> <!-- TODO, delete -->
													<xsl:variable name="aiCode" select="ape:aiFromEad($href,'')"/>
													<xsl:choose> <!-- generate at the same way that up -->
														<xsl:when test="$aiCode != 'ERROR' and $aiCode != ''">
															<a href="{$eadUrl}/{$aiCode}" target="_blank">
																<xsl:value-of select="ape:resource('eaccpf.portal.goToRelatedResource')"/>
															</a>
														</xsl:when>
														<xsl:otherwise>
															<xsl:value-of select="ape:resource('eaccpf.portal.goToRelatedResource')"/>
														</xsl:otherwise>
													</xsl:choose>
												</xsl:when>
												<xsl:when test="./eac:relationEntry[@localType='title']">
													<xsl:variable name="href" select="./eac:relationEntry[@localType='title']"/>
													<xsl:variable name="aiCode" select="ape:titleFromEad($href,'')"/>
													<xsl:value-of select="aiCode"></xsl:value-of>
												</xsl:when>
											</xsl:choose>
										</xsl:otherwise>
									</xsl:choose>
						   		</xsl:otherwise>
					 	 	</xsl:choose>
					 	 	<xsl:call-template name="relationType">
								<xsl:with-param name="current" select="$list/parent::node()"/>
							</xsl:call-template> 
					</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
			<xsl:otherwise>
		 	 	<xsl:choose>
		 	 		<xsl:when test="$list/parent::node()/@xlink:href">
		 	 			<xsl:variable name="link" select="$list/parent::node()/@xlink:href"/>
		 	 			<xsl:choose>
			 	 			<xsl:when test="starts-with($link, 'http') or starts-with($link, 'https') or starts-with($link, 'ftp') or starts-with($link, 'www')">
								<xsl:call-template name="resourceRelationListId">
									<xsl:with-param name="list" select="$list"/>
								</xsl:call-template> 
								<xsl:call-template name="relationType">
									<xsl:with-param name="current" select="$list/parent::node()"/>
								</xsl:call-template> 
					 	   </xsl:when>
					 	   <xsl:when test="not(starts-with($link, 'http')) and not(starts-with($link, 'https')) and not(starts-with($link, 'ftp')) and not(starts-with($link, 'www'))">
								<xsl:choose>
									<xsl:when test="./parent::node()[eac:relationEntry[@localType='agencyCode']]">
										<xsl:variable name="href" select="./parent::node()/eac:relationEntry[@localType='agencyCode']"/>
								  		<xsl:if test="not(starts-with($href, 'http')) and not(starts-with($href, 'https')) and not(starts-with($href, 'ftp')) and not(starts-with($href, 'www'))">
											<xsl:variable name="aiCode" select="ape:aiFromEad($link, $href)"/>
											<xsl:choose>
												<xsl:when test="$aiCode != 'ERROR' and $aiCode != ''">
													<a href="{$eadUrl}/{$aiCode}" target="_blank">
														<xsl:for-each select="$list">
															<xsl:apply-templates select="." mode="other"/>
													 	</xsl:for-each>
													</a>
												</xsl:when>
												<xsl:otherwise>
													<xsl:choose>
														<xsl:when test="./parent::node()/eac:relationEntry[@localType='title']">
															<xsl:for-each select="$list[@localType='title']">
																<xsl:apply-templates select="." mode="other"/>
														 	</xsl:for-each>
														</xsl:when>
														<xsl:when test="./parent::node()/eac:relationEntry[not(@localType)]">
															<xsl:apply-templates select="$list[not(@localType)][1]" mode="other"/>
														</xsl:when>
														<xsl:otherwise>
															<xsl:value-of select="ape:resource('eaccpf.portal.goToRelatedResource')"/>
														</xsl:otherwise>
													</xsl:choose>
												</xsl:otherwise>
											</xsl:choose>
										</xsl:if>
									</xsl:when>
									<xsl:otherwise>
										<xsl:variable name="aiCode" select="ape:aiFromEad($link, '')"/>
										<xsl:choose>
											<xsl:when test="$aiCode != 'ERROR' and $aiCode != ''">
												<a href="{$eadUrl}/{$aiCode}" target="_blank">
													<xsl:for-each select="$list[@localType='title']">
														<xsl:apply-templates select="." mode="other"/>
												 	</xsl:for-each>
												</a>
											</xsl:when>
											<xsl:otherwise>
												<xsl:choose>
													<xsl:when test="$list/parent::node()/eac:relationEntry[@localType='title']">
														<xsl:for-each select="$list[@localType='title']">
															<xsl:apply-templates select="." mode="other"/>
													 	</xsl:for-each>
													</xsl:when>
													<xsl:when test="$list/parent::node()/eac:relationEntry[not(@localType)]">
														<xsl:apply-templates select="$list/parent::node()/eac:relationEntry[not(@localType)][1]" mode="other"/>
													</xsl:when>
													<xsl:otherwise>
														<xsl:value-of select="ape:resource('eaccpf.portal.goToRelatedResource')"/>
													</xsl:otherwise>
												</xsl:choose>
											</xsl:otherwise>
										</xsl:choose>
									</xsl:otherwise>
								</xsl:choose>
					 	   </xsl:when>
					 	   <xsl:otherwise>
									<xsl:variable name="aiCode" select="ape:aiFromEad($link, '')"/>
									<xsl:choose>
										<xsl:when test="$aiCode != 'ERROR' and $aiCode != ''">
											<a href="{$eadUrl}/{$aiCode}" target="_blank">
												<xsl:for-each select="$list">
													<xsl:apply-templates select="." mode="other"/>
											 	</xsl:for-each>
											</a>
										</xsl:when>
										<xsl:otherwise>
											<xsl:for-each select="$list">
												<xsl:apply-templates select="." mode="other"/>
										 	</xsl:for-each>
										</xsl:otherwise>
									</xsl:choose>
								</xsl:otherwise>
						</xsl:choose>
		 	 		</xsl:when>
		 	 		<xsl:when test="$list[@localType='id']"> <!-- id is here -->
						<xsl:variable name="localTypeId" select="$list[@localType='id']/text()"/>
						<xsl:variable name="href" select="ape:aiFromEad($localTypeId,'')"/>
						<xsl:choose> <!-- generate at the same way that up -->
							<xsl:when test="$list[@localType='title']">
								<xsl:choose>
									<xsl:when test="$href != ''">
										<a href="{$eadUrl}/{$href}" target="_blank">
											<xsl:for-each select="$list[@localType='title']">
												<xsl:if test="position() > 1">
													<xsl:text>. </xsl:text>
												</xsl:if>
												<xsl:apply-templates select="." mode="other"/>
											</xsl:for-each>
										</a>
									</xsl:when>
									<xsl:otherwise>
										<xsl:for-each select="$list[@localType='title']">
											<xsl:if test="position() > 1">
												<xsl:text>. </xsl:text>
											</xsl:if>
											<xsl:apply-templates select="." mode="other"/>
										</xsl:for-each>
									</xsl:otherwise>
								</xsl:choose>
							</xsl:when>
							<xsl:otherwise>
								<xsl:variable name="eadTitle" select="ape:titleFromEad($localTypeId,'')"/>
								<xsl:choose>
									<xsl:when test="$eadTitle != ''">
										<xsl:choose>
											<xsl:when test="$href != ''">
												<a href="{$eadUrl}/{$href}" target="_blank"><xsl:value-of select="$eadTitle"></xsl:value-of></a>
											</xsl:when>
											<xsl:otherwise>
												<xsl:value-of select="$eadTitle"></xsl:value-of>
											</xsl:otherwise>
										</xsl:choose>
									</xsl:when>
									<xsl:otherwise>
										<xsl:choose>
											<xsl:when test="$list[not(@localType)]">
												<xsl:variable name="title" select="$list[not(@localType)][1]/text()"/>
												<xsl:choose>
													<xsl:when test="$href != '' and $title =''">
														<a href="{$eadUrl}/{$href}" target="_blank"><xsl:value-of select="ape:resource('eaccpf.portal.goToRelatedResource')"/></a>
													</xsl:when>
													<xsl:when test="$href != '' and $title !=''">
														<a href="{$eadUrl}/{$href}" target="_blank"><xsl:value-of select="$title"/></a>
													</xsl:when>
													<xsl:otherwise>
														<xsl:value-of select="$title"/>
													</xsl:otherwise>
												</xsl:choose>
											</xsl:when>
											<xsl:otherwise>
												<xsl:choose>
													<xsl:when test="$href != ''">
														<a href="{$eadUrl}/{$href}" target="_blank"><xsl:value-of select="ape:resource('eaccpf.portal.goToRelatedResource')"/></a>
													</xsl:when>
													<xsl:otherwise>
														<xsl:value-of select="ape:resource('eaccpf.portal.goToRelatedResource')"/>
													</xsl:otherwise>
												</xsl:choose>
											</xsl:otherwise>
										</xsl:choose>
									</xsl:otherwise>
								</xsl:choose>
							</xsl:otherwise>
						</xsl:choose>
					</xsl:when>
					<xsl:when test="$list[@localType='title']">
						<xsl:for-each select="$list[@localType='title']">
							<xsl:if test="position() > 1">
								<xsl:text>. </xsl:text>
							</xsl:if>
							<xsl:apply-templates select="." mode="other"/>
						</xsl:for-each>
					</xsl:when>
		 	 		<xsl:otherwise>
		 	 			<xsl:value-of select="ape:resource('eaccpf.portal.goToRelatedResource')"/> <!--  TODO, default text, review this case -->
		 	 		</xsl:otherwise>
		 	 	</xsl:choose>
		 	 	<xsl:call-template name="relationType">
					<xsl:with-param name="current" select="$list/parent::node()"/>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	
	<xsl:template name="resourceRelationListId">
		<xsl:param name="list"/>
		<xsl:variable name="first" select="$list[1]" />
		<xsl:variable name="link" select="$first/parent::node()/@xlink:href"/>
		<xsl:variable name="id" select="$list[@localType='id']/text()"/>
		<xsl:variable name="title" select="$list[@localType='title']/text()"/>
		<xsl:choose>
			<xsl:when test="$link != ''">
				<xsl:variable name="aiCode" select="ape:aiFromEad($id,'')"/>
				<xsl:choose>
					<xsl:when test="$title!=''">
						<a href="{$eadUrl}/{$aiCode}" target="_blank">
							<xsl:for-each select="$list[@xml:lang = $language.selected]">
								<xsl:if test="@localType='title'">
									<xsl:if test="(position() > 1 and $first[@localType='title'] ) or (position() > 2 and $first[@localType='id'])">
										<xsl:text>. </xsl:text>
									</xsl:if>
									<xsl:apply-templates select="." mode="other"/>
								</xsl:if>
						 	</xsl:for-each>
						</a>
					</xsl:when>
					<xsl:when test="$title = '' and $aiCode != 'ERROR' and $aiCode != ''">
						<a href="{$aiCode}" target="_blank">
							<xsl:value-of select="ape:resource('eaccpf.portal.goToRelatedResource')"/>
						</a>
					</xsl:when>
					<xsl:otherwise>
						<xsl:variable name="title" select="ape:titleFromEad($id,'')"/>
						<xsl:choose>
							<xsl:when test="$title != ''">
								<xsl:choose>
									<xsl:when test="$aiCode != ''">
										<a href="{$aiCode}" target="_blank">
											<xsl:value-of select="$title"/>
										</a>
									</xsl:when>
									<xsl:otherwise>
										<xsl:value-of select="$title"/>
									</xsl:otherwise>
								</xsl:choose>
							</xsl:when>
							<xsl:otherwise>
								<xsl:choose>
									<xsl:when test="$aiCode != ''">
										<a href="{$aiCode}" target="_blank">
											<xsl:value-of select="ape:resource('eaccpf.portal.goToRelatedResource')"/>
										</a>
									</xsl:when>
									<xsl:otherwise>
										<xsl:value-of select="ape:resource('eaccpf.portal.goToRelatedResource')"/>
									</xsl:otherwise>
								</xsl:choose>
							</xsl:otherwise>
						</xsl:choose>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
			<xsl:otherwise>
				<xsl:variable name="aiCode" select="ape:aiFromEad($id,'')"/>
				<xsl:variable name="href" select="$list[@localType='id']"/>
				<xsl:choose>
					<xsl:when test="$title != ''">
						<xsl:choose>
							<xsl:when test="$aiCode != '' and $aiCode != 'ERROR'">
								<a href="{$aiCode}" target="_blank">
									<xsl:value-of select="$title"></xsl:value-of>
								</a>
							</xsl:when>
							<xsl:otherwise>
								<xsl:value-of select="$title"></xsl:value-of>
							</xsl:otherwise>
						</xsl:choose>
					</xsl:when>
					<xsl:when test="$aiCode != ''"> <!-- id is here -->
						<xsl:variable name="eadTitle" select="ape:titleFromEad($href,'')"/>
						<xsl:choose> <!-- generate at the same way that up -->
							<xsl:when test="$eadTitle != 'ERROR' and $eadTitle != ''">
								<a href="{$aiCode}" target="_blank">
									<xsl:value-of select="$eadTitle"/>
								</a>
							</xsl:when>
							<xsl:otherwise>
								<a href="{$aiCode}" target="_blank">
									<xsl:value-of select="ape:resource('eaccpf.portal.goToRelatedResource')"/>
								</a>
							</xsl:otherwise>
						</xsl:choose>
					</xsl:when>
					<xsl:when test="$list[not(@localType)]">
						<xsl:value-of select="$list[not(@localType)][1]"></xsl:value-of>
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="ape:resource('eaccpf.portal.goToRelatedResource')"/>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<!-- Template for multilanguage relations. -->
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
									</xsl:otherwise>	
								</xsl:choose>
								<xsl:call-template name="relationType">
									<xsl:with-param name="current" select="./parent::node()"/>
								</xsl:call-template>
							</xsl:if>
					 	</xsl:for-each> 
					</xsl:when>
					<xsl:when test="$list[@xml:lang = $lang.navigator] and $list[@xml:lang = $lang.navigator]/text() and $list[@xml:lang = $lang.navigator]/text() != ''">
					 	<xsl:for-each select="$list[@xml:lang = $lang.navigator]">
						   <xsl:if test="position()=1">
								<xsl:choose>
									<xsl:when test="./parent::node()/@xlink:href">
										<xsl:call-template name="linkToFile">
											<xsl:with-param name="link" select="./parent::node()/@xlink:href"/>
										</xsl:call-template>
									</xsl:when>
									<xsl:otherwise>
										<xsl:apply-templates select="." mode="other"/>
									</xsl:otherwise>	
								</xsl:choose>
								<xsl:call-template name="relationType">
									<xsl:with-param name="current" select="./parent::node()"/>
								</xsl:call-template>
							</xsl:if>
					 	</xsl:for-each> 
					</xsl:when>
					<xsl:when test="$list[@xml:lang = $language.default] and $list[@xml:lang = $language.default]/text() and $list[@xml:lang = $language.default]/text() != ''">
						<xsl:for-each select="$list[@xml:lang = $language.default]">
							<xsl:if test="position()=1">
								<xsl:choose>
									<xsl:when test="./parent::node()/@xlink:href">
										<xsl:call-template name="linkToFile">
											<xsl:with-param name="link" select="./parent::node()/@xlink:href"/>
										</xsl:call-template>
									</xsl:when>
									<xsl:otherwise>
										<xsl:apply-templates select="." mode="other"/>
									</xsl:otherwise>	
								</xsl:choose>
								<xsl:call-template name="relationType">
									<xsl:with-param name="current" select="./parent::node()"/>
								</xsl:call-template>
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
									</xsl:otherwise>	
								</xsl:choose>
								<xsl:call-template name="relationType">
									<xsl:with-param name="current" select="./parent::node()"/>
								</xsl:call-template>
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
										</xsl:otherwise>	
									</xsl:choose>
									<xsl:call-template name="relationType">
										<xsl:with-param name="current" select="./parent::node()"/>
									</xsl:call-template>
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
							</xsl:otherwise>	
						</xsl:choose>
							<xsl:call-template name="relationType">
								<xsl:with-param name="current" select="./parent::node()"/>
							</xsl:call-template>
					 </xsl:if>
				</xsl:for-each>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<!-- Template for links to file type. -->
	<xsl:template name="linkToFile">
		<xsl:param name="link"/>
	   	<xsl:if test="starts-with($link, 'http') or starts-with($link, 'https') or starts-with($link, 'ftp') or starts-with($link, 'www')">
			<a href="{$link}" target="_blank"><xsl:apply-templates select="current()" mode="other"/></a>
		</xsl:if>
		<xsl:if test="not(starts-with($link, 'http')) and not(starts-with($link, 'https')) and not(starts-with($link, 'ftp')) and not(starts-with($link, 'www'))">
			<xsl:choose>
				<xsl:when test="name(./parent::node()) = 'cpfRelation'">
					<xsl:choose>
						<xsl:when test="./parent::node()[eac:relationEntry[@localType='agencyCode']]">
							<xsl:variable name="href" select="./parent::node()/eac:relationEntry[@localType='agencyCode']"/>
					  		<xsl:if test="not(starts-with($href, 'http')) and not(starts-with($href, 'https')) and not(starts-with($href, 'ftp')) and not(starts-with($href, 'www'))">
								<xsl:variable name="encodedHref" select="ape:encodeSpecialCharacters($href)" />
								<xsl:variable name="encodedlink" select="ape:encodeSpecialCharacters($link)" />
								<a href="{$eacUrlBase}/aicode/{$encodedHref}/type/ec/id/{$encodedlink}" target="_blank">
									<xsl:apply-templates select="." mode="other"/>
								</a>
							</xsl:if>
						</xsl:when>
						<xsl:otherwise>
							<xsl:variable name="aiCode" select="ape:aiFromEac($link, '')"/>
							<xsl:choose>
								<xsl:when test="$aiCode != 'ERROR' and $aiCode != ''">
								<xsl:variable name="encodedlink" select="ape:encodeSpecialCharacters($link)" />
									<a href="{$eacUrlBase}/aicode/{$aiCode}/type/ec/id/{$encodedlink}" target="_blank">
										<xsl:apply-templates select="." mode="other"/>
									</a>
								</xsl:when>
								<xsl:otherwise>
									<xsl:apply-templates select="." mode="other"/>
								</xsl:otherwise>
							</xsl:choose>
						</xsl:otherwise>	
					</xsl:choose>
				</xsl:when>
				<xsl:when test="name(./parent::node()) = 'resourceRelation'">
					<xsl:choose>
						<xsl:when test="./parent::node()[eac:relationEntry[@localType='agencyCode']]">
							<xsl:variable name="href" select="./parent::node()/eac:relationEntry[@localType='agencyCode']"/>
					  		<xsl:if test="not(starts-with($href, 'http')) and not(starts-with($href, 'https')) and not(starts-with($href, 'ftp')) and not(starts-with($href, 'www'))">
							<xsl:variable name="aiCode" select="ape:aiFromEad($link, $href)"/>
							<xsl:choose>
								<xsl:when test="$aiCode != 'ERROR' and $aiCode != ''">
									<a href="{$eadUrl}/{$aiCode}" target="_blank">
										<xsl:apply-templates select="." mode="other"/>
									</a>
								</xsl:when>
								<xsl:otherwise>
									<xsl:apply-templates select="." mode="other"/>
								</xsl:otherwise>
							</xsl:choose>
							</xsl:if>
						</xsl:when>
						<xsl:otherwise>
							<xsl:variable name="aiCode" select="ape:aiFromEad($link, '')"/>
							<xsl:choose>
								<xsl:when test="$aiCode != 'ERROR' and $aiCode != ''">
									<a href="{$eadUrl}/{$aiCode}" target="_blank">
										<xsl:apply-templates select="." mode="other"/>
									</a>
								</xsl:when>
								<xsl:otherwise>
									<xsl:apply-templates select="." mode="other"/>
								</xsl:otherwise>
							</xsl:choose>
						</xsl:otherwise>	
					</xsl:choose>
				</xsl:when>
				<xsl:when test="name(./parent::node()) != 'alternativeSet'">
					<xsl:choose>
						<xsl:when test="./parent::node()[eac:componentEntry[@localType='agencyCode']]">
							<xsl:variable name="href" select="./parent::node()/eac:componentEntry[@localType='agencyCode']"/>
					  		<xsl:if test="not(starts-with($href, 'http')) and not(starts-with($href, 'https')) and not(starts-with($href, 'ftp')) and not(starts-with($href, 'www'))">
								<xsl:variable name="encodedHref" select="ape:encodeSpecialCharacters($href)" />
								<xsl:variable name="encodedlink" select="ape:encodeSpecialCharacters($link)" />
								<a href="{$eacUrlBase}/aicode/{$encodedHref}/type/ec/id/{$encodedlink}" target="_blank">
									<xsl:apply-templates select="." mode="other"/>
								</a>
							</xsl:if>
						</xsl:when>
						<xsl:otherwise>
							<xsl:variable name="aiCode" select="ape:aiFromEac($link, '')"/>
							<xsl:choose>
								<xsl:when test="$aiCode != 'ERROR' and $aiCode != ''">
								<xsl:variable name="encodedlink" select="ape:encodeSpecialCharacters($link)" />
									<a href="{$eacUrlBase}/aicode/{$aiCode}/type/ec/id/{$encodedlink}" target="_blank">
										<xsl:apply-templates select="." mode="other"/>
									</a>
								</xsl:when>
								<xsl:otherwise>
									<xsl:apply-templates select="." mode="other"/>
								</xsl:otherwise>
							</xsl:choose>
						</xsl:otherwise>	
					</xsl:choose>
				</xsl:when>
				<xsl:otherwise>
					<!-- TODO: functionRelation -->
					<a href="#">
						<xsl:apply-templates select="." mode="other"/>
					</a>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:if>
	</xsl:template>

	<!-- Template for attribute @cpfRelationType or @resourceRelationType. -->
	<xsl:template name="relationType">
		<xsl:param name="current"/>
		<xsl:if test="name($current)='cpfRelation' and $current[@cpfRelationType]"> 
			<xsl:text> (</xsl:text>
			<xsl:choose>
				<xsl:when test="$current[@cpfRelationType='associative']"> 
					<xsl:value-of select="ape:resource('eaccpf.portal.associative')"/>
				</xsl:when>
				<xsl:when test="$current[@cpfRelationType='identity']"> 
					<xsl:value-of select="ape:resource('eaccpf.portal.identity')"/>
				</xsl:when>
				<xsl:when test="$current[@cpfRelationType='hierarchical']"> 
					<xsl:value-of select="ape:resource('eaccpf.portal.hierarchical')"/>
				</xsl:when>
				<xsl:when test="$current[@cpfRelationType='hierarchical-parent']"> 
					<xsl:value-of select="ape:resource('eaccpf.portal.hierarchicalParent')"/>
				</xsl:when>
				<xsl:when test="$current[@cpfRelationType='hierarchical-child']"> 
					<xsl:value-of select="ape:resource('eaccpf.portal.hierarchicalChild')"/>
				</xsl:when>
				<xsl:when test="$current[@cpfRelationType='temporal']"> 
					<xsl:value-of select="ape:resource('eaccpf.portal.temporal')"/>
				</xsl:when>
				<xsl:when test="$current[@cpfRelationType='temporal-earlier']"> 
					<xsl:value-of select="ape:resource('eaccpf.portal.temporalEarlier')"/>
				</xsl:when>
				<xsl:when test="$current[@cpfRelationType='temporal-later']"> 
					<xsl:value-of select="ape:resource('eaccpf.portal.temporalLater')"/>
				</xsl:when>
				<xsl:when test="$current[@cpfRelationType='family']"> 
					<xsl:value-of select="ape:resource('eaccpf.portal.family')"/>
				</xsl:when>
				<xsl:otherwise/>
			</xsl:choose>
			<xsl:text>)</xsl:text>	
		</xsl:if>
		<xsl:if test="name($current)='resourceRelation' and $current[@resourceRelationType]"> 
			<xsl:text> (</xsl:text>
			<xsl:choose>
				<xsl:when test="$current[@resourceRelationType='creatorOf']">
					<xsl:value-of select="ape:resource('eaccpf.portal.creatorOf')"/>
				</xsl:when>
				<xsl:when test="$current[@resourceRelationType='subjectOf']">
					<xsl:value-of select="ape:resource('eaccpf.portal.subjectOf')"/>
				</xsl:when>
				<xsl:when test="$current[@resourceRelationType='other']">
					<xsl:value-of select="ape:resource('eaccpf.portal.other')"/>
				</xsl:when>
				<xsl:otherwise/>
			</xsl:choose>
			<xsl:text>)</xsl:text>	
		</xsl:if>
	</xsl:template>
</xsl:stylesheet>