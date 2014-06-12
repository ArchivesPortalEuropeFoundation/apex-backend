<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xmlns='http://www.w3.org/1999/xhtml' xmlns:eag="http://www.archivesportaleurope.net/Portal/profiles/eag_2012/"
	xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:ape="http://www.archivesportaleurope.eu/xslt/extensions"
	exclude-result-prefixes="xlink xlink xsi eag ape">

	<xsl:output method="html" indent="yes" version="4.0"
		encoding="UTF-8"/>
	<xsl:param name="language.selected"/>
	<xsl:variable name="language.default" select="'eng'"/>
	<xsl:template match="/">
		<h1>
			<xsl:value-of select="./eag:eag/eag:archguide/eag:identity/eag:autform"></xsl:value-of>
		</h1>

		<!-- CONTACT -->
		<!-- starts loop -->
		<xsl:for-each select="./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository">
			<xsl:variable name="id" select="position()"/>
			<div id="repository_{$id}">
				<xsl:if test="count(current()/parent::node()/eag:repository)> 1">
					<h2 class="repositoryName">
						<xsl:choose>
							<xsl:when test="./eag:repositoryName">
								<xsl:call-template name="multilanguageOnlyOne">
									<xsl:with-param name="list" select="./eag:repositoryName" ></xsl:with-param>
								</xsl:call-template>
							</xsl:when>
							<xsl:otherwise>
								<xsl:value-of select="/eag:eag/eag:archguide/eag:identity/eag:autform"/>
							</xsl:otherwise>
						</xsl:choose>
						<xsl:variable name="role" select="./eag:repositoryRole/text()"></xsl:variable>
						<xsl:if test="$role and ($role = 'Branch' or $role = 'Head quarter' or $role = 'Interim archive')">
							<xsl:text> (</xsl:text>
							<xsl:choose>
								<xsl:when test="$role = 'Branch'">
									<xsl:value-of select = "ape:resource('eag2012.options.role.branch')"/>
								</xsl:when>
								<xsl:when test="$role = 'Head quarter'">
									<xsl:value-of select = "ape:resource('eag2012.options.role.headquarters')"/>
								</xsl:when>
								<xsl:when test="$role = 'Interim archive'">
									<xsl:value-of select = "ape:resource('eag2012.options.role.interimArchive')"/>
								</xsl:when>	
								<xsl:otherwise>
									<xsl:text></xsl:text>
								</xsl:otherwise>
							</xsl:choose>
							<xsl:text>)</xsl:text>
						</xsl:if>
					</h2>
				</xsl:if>
				<xsl:if test="$id = 1">
				<div class="repositoryInfo">
					<table class="aiSection contactDisplay">
						<thead>
							<tr>
								<th colspan="2">
									<xsl:value-of select="ape:resource('eag2012.portal.contact')"/>
								</th>
							</tr>
						</thead>
						<tbody>

							<!-- visitors Address only shown if there are values -->
							<xsl:if test="eag:location[not(@localType) or @localType='visitors address'] and eag:location[not(@localType) or @localType='visitors address']/eag:street/text() and eag:location[not(@localType) or @localType='visitors address']/eag:municipalityPostalcode/text()">
								<tr>
									<td class="header">
										<xsl:value-of select="ape:resource('eag2012.portal.visitorsaddress')"/><xsl:text>:</xsl:text>
									</td>
									<td class="address">
										<xsl:call-template name="multilanguageAddress">
											<xsl:with-param name="list" select="eag:location[not(@localType) or @localType='visitors address']"></xsl:with-param>
										</xsl:call-template>
									</td>
								</tr>
							</xsl:if>







							<!-- country only shown if there are values-->
							<xsl:if test="eag:location[not(@localType) or @localType='visitors address']/eag:country and eag:location[not(@localType) or @localType='visitors address']/eag:country/text() != ''">
								<tr>
									<td class="header">
										<xsl:value-of select="ape:resource('eag2012.portal.country')"/><xsl:text>:</xsl:text>
									</td>
									<td>
										<xsl:call-template name="multilanguage">
											<xsl:with-param name="list" select="eag:location[not(@localType) or @localType='visitors address']/eag:country"></xsl:with-param>
										</xsl:call-template>
									</td>
								</tr>
							
								<xsl:call-template name="email">
									<xsl:with-param name="class" select="'header'"/>
								</xsl:call-template>
								<xsl:call-template name="webpage">
									<xsl:with-param name="class" select="'header'"/>
								</xsl:call-template>
								<xsl:call-template name="telephone">
									<xsl:with-param name="class" select="'header'"/>
								</xsl:call-template>
							
							</xsl:if>

							
							<!-- roleofthearchive only shown if there are values-->
							<xsl:if test="current()/eag:repositoryRole and current()/eag:repositoryRole/text() != ''  and (current()/eag:repositoryRole/text() = 'Branch' or current()/eag:repositoryRole/text() = 'Head quarter' or current()/eag:repositoryRole/text() = 'Interim archive')">
								<tr>
									<td class="header">
										<xsl:value-of select="ape:resource('eag2012.portal.roleofthearchive')"/><xsl:text>:</xsl:text>
									</td>
									<td>
										<xsl:variable name="role" select="current()/eag:repositoryRole/text()"></xsl:variable>
										<div>
											<xsl:choose>
												<xsl:when test="$role = 'Branch'">
													<xsl:value-of select = "ape:resource('eag2012.options.role.branch')"/>
												</xsl:when>
												<xsl:when test="$role = 'Head quarter'">
													<xsl:value-of select = "ape:resource('eag2012.options.role.headquarters')"/>
												</xsl:when>
												<xsl:when test="$role = 'Interim archive'">
													<xsl:value-of select = "ape:resource('eag2012.options.role.interimArchive')"/>
												</xsl:when>	
												<xsl:otherwise>
													<xsl:text></xsl:text>
												</xsl:otherwise>
											</xsl:choose>
										</div>
									</td>
								</tr>
							</xsl:if>
						</tbody>
					</table>
					<xsl:if test="eag:timetable/eag:opening/text() or eag:timetable/eag:closing/text()">				
					<!-- ACCESS INFORMATION -->
					<table class="aiSection accessDisplay">
						<thead>
							<tr>
								<th colspan="2">
									<xsl:value-of select="ape:resource('eag2012.portal.accessandserviceinfo')"/>
								</th>
							</tr>
						</thead>
						<tbody>

							<!-- opening only shown if there are values-->
							<xsl:if test="eag:timetable/eag:opening/text()">
								<tr>
									<td class="header">
										<xsl:value-of select="ape:resource('eag2012.portal.openinghours')"></xsl:value-of><xsl:text>:</xsl:text>
									</td>
									<td>
										<xsl:call-template name="multilanguageOpening">
											<xsl:with-param name="list" select="eag:timetable/eag:opening"></xsl:with-param>
										</xsl:call-template>
									</td>
								</tr>
							</xsl:if>

							<!-- closing only shown if there are values-->
							<xsl:if test="eag:timetable/eag:closing/text()">
								<tr>
									<td class="header">
										<xsl:value-of select="ape:resource('eag2012.portal.closingdates')"></xsl:value-of><xsl:text>:</xsl:text>
									</td>
									<td>
										<xsl:call-template name="multilanguageClosing">
											<xsl:with-param name="list" select="eag:timetable/eag:closing"></xsl:with-param>
										</xsl:call-template>
									</td>
								</tr>
							</xsl:if>

						</tbody>
					</table>
					</xsl:if>

				</div>
				</xsl:if>
			</div>
		</xsl:for-each>

	</xsl:template>

<!-- template for email-->
	<xsl:template name="email">
		<xsl:param name="parent" select="current()"/>
		<xsl:param name="class"/>
		<xsl:param name="trClass" select="''"/>
		<xsl:if test="$parent/eag:email/@href and $parent/eag:email/@href != ''">
			<tr class="{$trClass}">
				<td class="{$class}">
					<xsl:value-of select="ape:resource('eag2012.portal.email')"/><xsl:text>:</xsl:text>
				</td>
				<td>
					<xsl:variable name="list" select="$parent/eag:email"></xsl:variable>
					<xsl:choose>
						<xsl:when test="count($list) > 1">
							<xsl:choose>
								
								<xsl:when test="$list[@xml:lang = $language.selected]">
									<xsl:for-each select="$list">
										<xsl:variable name="email" select="current()/@href"></xsl:variable>
										<xsl:variable name="text" select="current()/text()"></xsl:variable>
										<xsl:variable name="currentLang" select="current()/@xml:lang"></xsl:variable>
										<xsl:if test="(not($currentLang) or $currentLang = $language.selected) and ($email or $text)">
											<div>
												<xsl:choose>
													<xsl:when test="$email and $email != '' and $text and $text != ''">
														<a href="mailto:{$email}" target="_blank">
														   <xsl:value-of select="$text"/>
														</a>
													</xsl:when>
													<xsl:when test="$email and $email != ''">
														<a href="mailto:{$email}" target="_blank">
															<xsl:value-of select="ape:resource('eag2012.portal.contactUsByEmail')"/>
														</a>
													</xsl:when>
													<xsl:when test="$text and $text != ''">
													      <xsl:value-of select="$text"/>
													</xsl:when>
												</xsl:choose>
											</div>
										</xsl:if>
									</xsl:for-each>
								</xsl:when>
								<xsl:when test="$list[@xml:lang = $language.default]">
									<xsl:for-each select="$list">
										<xsl:variable name="email" select="current()/@href"></xsl:variable>
										<xsl:variable name="text" select="current()/text()"></xsl:variable>
										<xsl:variable name="currentLang" select="current()/@xml:lang"></xsl:variable>
										<xsl:if test="(not($currentLang) or $currentLang = $language.default) and ($email or $text)">
											<div>
												<xsl:choose>
													<xsl:when test="$email and $email != '' and $text and $text != ''">
														<a href="mailto:{$email}" target="_blank">
														    <xsl:value-of select="$text"/>
														</a>
													</xsl:when>
													<xsl:when test="$email and $email != ''">
														<a href="mailto:{$email}" target="_blank">
															<xsl:value-of select="ape:resource('eag2012.portal.contactUsByEmail')"/>
														</a>
													</xsl:when>
													<xsl:when test="$text and $text != ''">
													      <xsl:value-of select="$text"/>
													</xsl:when>
												</xsl:choose>
											</div>
										</xsl:if>
									</xsl:for-each>
								</xsl:when>
								<xsl:otherwise> <!-- first language -->
									<xsl:for-each select="$list">
										<xsl:variable name="email" select="current()/@href"></xsl:variable>
										<xsl:variable name="text" select="current()/text()"></xsl:variable>
										<xsl:variable name="currentLang" select="current()/@xml:lang"></xsl:variable>
										<xsl:choose>
											<xsl:when test="not($currentLang)">
												<div class="emailNoLang">
													<xsl:choose>
														<xsl:when test="$email and $email != '' and $text and $text != ''">
															<a href="mailto:{$email}" target="_blank">
															    <xsl:value-of select="$text"/>
															</a>
														</xsl:when>
														<xsl:when test="$email and $email != ''">
															<a href="mailto:{$email}" target="_blank">
																<xsl:value-of select="ape:resource('eag2012.portal.contactUsByEmail')"/>
															</a>
														</xsl:when>
														<xsl:when test="$text and $text != ''">
														    	<xsl:value-of select="$text"/>
														</xsl:when>
													</xsl:choose>
												</div>
											</xsl:when>
											<xsl:when test="$currentLang">
												<xsl:variable name="classValue">
													<xsl:text>emailLang_</xsl:text><xsl:value-of select="$currentLang"></xsl:value-of>
												</xsl:variable>
												<div class="{$classValue}">
													<xsl:choose>
														<xsl:when test="$email and $email != '' and $text and $text != ''">
															<a href="mailto:{$email}" target="_blank">
															    <xsl:value-of select="$text"/>
															</a>
														</xsl:when>
														<xsl:when test="$email and $email != ''">
															<a href="mailto:{$email}" target="_blank">
																<xsl:value-of select="ape:resource('eag2012.portal.contactUsByEmail')"/>
															</a>
														</xsl:when>
														<xsl:when test="$text and $text != ''">
														    	<xsl:value-of select="$text"/>
														</xsl:when>
													</xsl:choose>
												</div>
											</xsl:when>
										</xsl:choose>
									</xsl:for-each>
								</xsl:otherwise>
							</xsl:choose>
						</xsl:when>
						<xsl:otherwise>  <!-- There is only one element in the list -->
							<xsl:for-each select="$list">
								<xsl:variable name="email" select="current()/@href"></xsl:variable>
								<xsl:variable name="text" select="current()/text()"></xsl:variable>
								<xsl:variable name="currentLang" select="current()/@xml:lang"></xsl:variable>
								<div>
									<xsl:choose>
										<xsl:when test="$email and $email != '' and $text and $text != ''">
											<a href="mailto:{$email}" target="_blank">
											   <xsl:value-of select="$text"/>
											</a>
										</xsl:when>
										<xsl:when test="$email and $email != ''">
											<a href="mailto:{$email}" target="_blank">
												<xsl:value-of select="ape:resource('eag2012.portal.contactUsByEmail')"/>
											</a>
										</xsl:when>
										<xsl:when test="$text and $text != ''">
										    <xsl:value-of select="$text"/>
										</xsl:when>
									</xsl:choose>
								</div>
							</xsl:for-each>
						</xsl:otherwise>
					</xsl:choose>
				</td>
			</tr>
		</xsl:if>
	</xsl:template>


	<!-- template for webpage-->
	<xsl:template name="webpage">
		<xsl:param name="parent" select="current()"/>
		<xsl:param name="class"/>
		<xsl:param name="trClass" select="''"/>
		<xsl:choose>
			<xsl:when test="$parent/eag:webpage/@href and $parent/eag:webpage/@href != ''">
				<tr class="{$trClass}">
					<td class="{$class}">
						<xsl:value-of select="ape:resource('eag2012.portal.webpage')"/><xsl:text>:</xsl:text>
					</td>
					<td>
						<xsl:call-template name="multilaguageWebpage">
							<xsl:with-param name="list" select="$parent/eag:webpage"/>
						</xsl:call-template>
					</td>
				</tr>
			</xsl:when>
		</xsl:choose>
	</xsl:template>

	<!-- template for multilanguage for webpage -->
	<xsl:template name="multilaguageWebpage">
		<xsl:param name="list"/>
		<xsl:choose>
			<xsl:when test="count($list) > 1">
				<xsl:choose>
					<xsl:when test="$list[@xml:lang = $language.selected]">
						<xsl:for-each select="$list">
							<xsl:variable name="link" select="current()/@href"></xsl:variable>
							<xsl:variable name="text" select="current()/text()"></xsl:variable>
							<xsl:variable name="currentLang" select="current()/@xml:lang"></xsl:variable>
							<xsl:if test="(not($currentLang) or $currentLang = $language.selected) and ($link or $text)">
								<div>
									<xsl:choose>
										<xsl:when test="$link and $link != '' and $text and $text != ''">
											<a href="{$link}" target="_blank">
											   <xsl:value-of select="$text"/>
											</a>
										</xsl:when>
										<xsl:when test="$link and $link != ''">
											<a href="{$link}" target="_blank">
												<xsl:value-of select="ape:resource('eag2012.portal.gotothewebsite')"/>
											</a>
										</xsl:when>
										<xsl:when test="$text and $text != ''">
										     <xsl:value-of select="$text"/>
										</xsl:when>
									</xsl:choose>
								</div>
							</xsl:if>
						</xsl:for-each>
					</xsl:when>
					<xsl:when test="$list[@xml:lang = $language.default]">
						<xsl:for-each select="$list">
							<xsl:variable name="link" select="current()/@href"></xsl:variable>
							<xsl:variable name="text" select="current()/text()"></xsl:variable>
							<xsl:variable name="currentLang" select="current()/@xml:lang"></xsl:variable>
							<xsl:if test="(not($currentLang) or $currentLang = $language.default) and ($link or $text)">
								<div>
									<xsl:choose>
										<xsl:when test="$link and $link != '' and $text and $text != ''">
											<a href="{$link}" target="_blank">
											   <xsl:value-of select="$text"/>
											</a>
										</xsl:when>
										<xsl:when test="$link and $link != ''">
											<a href="{$link}" target="_blank">
												<xsl:value-of select="ape:resource('eag2012.portal.gotothewebsite')"/>
											</a>
										</xsl:when>
										<xsl:when test="$text and $text != ''">
										    <xsl:value-of select="$text"/>
										</xsl:when>
									</xsl:choose>
								</div>
							</xsl:if>
						</xsl:for-each>
					</xsl:when>
					<xsl:otherwise> <!-- first language -->
						<xsl:for-each select="$list">
							<xsl:variable name="link" select="current()/@href"></xsl:variable>
							<xsl:variable name="text" select="current()/text()"></xsl:variable>
							<xsl:variable name="currentLang" select="current()/@xml:lang"></xsl:variable>
							<xsl:choose>
								<xsl:when test="not($currentLang)">
										<div class="webpageNoLang">
											<xsl:choose>
												<xsl:when test="$link and $link != '' and $text and $text != ''">
													<a href="{$link}" target="_blank">
													    <xsl:value-of select="$text"/>
													</a>
												</xsl:when>
												<xsl:when test="$link and $link != ''">
													<a href="{$link}" target="_blank">
														<xsl:value-of select="ape:resource('eag2012.portal.gotothewebsite')"/>
													</a>
												</xsl:when>
												<xsl:when test="$text and $text != ''">
												    	<xsl:value-of select="$text"/>
												</xsl:when>
											</xsl:choose>
										</div>
									</xsl:when>
									<xsl:when test="$currentLang">
										<xsl:variable name="classValue">
											<xsl:text>webpageLang_</xsl:text><xsl:value-of select="$currentLang"></xsl:value-of>
										</xsl:variable>
										<div class="{$classValue}">
											<xsl:choose>
												<xsl:when test="$link and $link != '' and $text and $text != ''">
													<a href="{$link}" target="_blank">
													    <xsl:value-of select="$text"/>
													</a>
												</xsl:when>
												<xsl:when test="$link and $link != ''">
													<a href="{$link}" target="_blank">
														<xsl:value-of select="ape:resource('eag2012.portal.gotothewebsite')"/>
													</a>
												</xsl:when>
												<xsl:when test="$text and $text != ''">
												    	<xsl:value-of select="$text"/>
												</xsl:when>
											</xsl:choose>
										</div>
									</xsl:when>
							</xsl:choose>
						</xsl:for-each>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
			<xsl:otherwise>
				<xsl:for-each select="$list">
					<xsl:variable name="link" select="current()/@href"></xsl:variable>
					<xsl:variable name="text" select="current()/text()"></xsl:variable>
					<xsl:variable name="currentLang" select="current()/@xml:lang"></xsl:variable>
					<div>
						<xsl:choose>
							<xsl:when test="$link and $link != '' and $text and $text != ''">
								<a href="{$link}" target="_blank">
								   <xsl:value-of select="$text"/>
								</a>
							</xsl:when>
							<xsl:when test="$link and $link != ''">
								<a href="{$link}" target="_blank">
									<xsl:value-of select="ape:resource('eag2012.portal.gotothewebsite')"/>
								</a>
							</xsl:when>
							<xsl:when test="$text and $text != ''">
							     <xsl:value-of select="$text"/>
							</xsl:when>
						</xsl:choose>
					</div>
				</xsl:for-each>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

<!-- template for multilanguage for webpage Exhibition-->
	<xsl:template name="multilanguageWebpageExhibition">
		<xsl:param name="list"/>
		<xsl:choose>
			<xsl:when test="count($list) > 1">
				<xsl:choose>
					<xsl:when test="$list[@xml:lang = $language.selected]">
						<xsl:for-each select="$list">
							<xsl:variable name="link" select="current()/@href"></xsl:variable>
							<xsl:variable name="text" select="current()/text()"></xsl:variable>
							<xsl:variable name="currentLang" select="current()/@xml:lang"></xsl:variable>
							<xsl:if test="(not($currentLang) or $currentLang = $language.selected) and ($link or $text)">
								<div>
									<xsl:choose>
										<xsl:when test="$link and $link != '' and $text and $text != ''">
											<a href="{$link}" target="_blank">
											   <xsl:value-of select="$text"/>
											</a>
										</xsl:when>
										<xsl:when test="$link and $link != ''">
											<a href="{$link}" target="_blank">
												<xsl:value-of select="ape:resource('eag2012.portal.gotothewebsite')"/>
											</a>
										</xsl:when>
										<xsl:when test="$text and $text != ''">
										     <xsl:value-of select="$text"/>
										</xsl:when>
									</xsl:choose>
								</div>
							</xsl:if>
						</xsl:for-each>
					</xsl:when>
					<xsl:when test="$list[@xml:lang = $language.default]">
						<xsl:for-each select="$list">
							<xsl:variable name="link" select="current()/@href"></xsl:variable>
							<xsl:variable name="text" select="current()/text()"></xsl:variable>
							<xsl:variable name="currentLang" select="current()/@xml:lang"></xsl:variable>
							<xsl:if test="(not($currentLang) or $currentLang = $language.default) and ($link or $text)">
								<div>
									<xsl:choose>
										<xsl:when test="$link and $link != '' and $text and $text != ''">
											<a href="{$link}" target="_blank">
											   <xsl:value-of select="$text"/>
											</a>
										</xsl:when>
										<xsl:when test="$link and $link != ''">
											<a href="{$link}" target="_blank">
												<xsl:value-of select="ape:resource('eag2012.portal.gotothewebsite')"/>
											</a>
										</xsl:when>
										<xsl:when test="$text and $text != ''">
										    <xsl:value-of select="$text"/>
										</xsl:when>
									</xsl:choose>
								</div>
							</xsl:if>
						</xsl:for-each>
					</xsl:when>
					<xsl:otherwise> <!-- first language -->
						<xsl:for-each select="$list">
							<xsl:variable name="link" select="current()/@href"></xsl:variable>
							<xsl:variable name="text" select="current()/text()"></xsl:variable>
							<xsl:variable name="currentLang" select="current()/@xml:lang"></xsl:variable>
							<xsl:choose>
								<xsl:when test="not($currentLang)">
										<div class="webpageExhibitionNoLang">
											<xsl:choose>
												<xsl:when test="$link and $link != '' and $text and $text != ''">
													<a href="{$link}" target="_blank">
													    <xsl:value-of select="$text"/>
													</a>
												</xsl:when>
												<xsl:when test="$link and $link != ''">
													<a href="{$link}" target="_blank">
														<xsl:value-of select="ape:resource('eag2012.portal.gotothewebsite')"/>
													</a>
												</xsl:when>
												<xsl:when test="$text and $text != ''">
												    	<xsl:value-of select="$text"/>
												</xsl:when>
											</xsl:choose>
										</div>
									</xsl:when>
									<xsl:when test="$currentLang">
										<xsl:variable name="classValue">
											<xsl:text>webpageExhibitionLang_</xsl:text><xsl:value-of select="$currentLang"></xsl:value-of>
										</xsl:variable>
										<div class="{$classValue}">
											<xsl:choose>
												<xsl:when test="$link and $link != '' and $text and $text != ''">
													<a href="{$link}" target="_blank">
													    <xsl:value-of select="$text"/>
													</a>
												</xsl:when>
												<xsl:when test="$link and $link != ''">
													<a href="{$link}" target="_blank">
														<xsl:value-of select="ape:resource('eag2012.portal.gotothewebsite')"/>
													</a>
												</xsl:when>
												<xsl:when test="$text and $text != ''">
												    	<xsl:value-of select="$text"/>
												</xsl:when>
											</xsl:choose>
										</div>
									</xsl:when>
							</xsl:choose>
						</xsl:for-each>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
			<xsl:otherwise>
				<xsl:for-each select="$list">
					<xsl:variable name="link" select="current()/@href"></xsl:variable>
					<xsl:variable name="text" select="current()/text()"></xsl:variable>
					<xsl:variable name="currentLang" select="current()/@xml:lang"></xsl:variable>
					<div>
						<xsl:choose>
							<xsl:when test="$link and $link != '' and $text and $text != ''">
								<a href="{$link}" target="_blank">
								   <xsl:value-of select="$text"/>
								</a>
							</xsl:when>
							<xsl:when test="$link and $link != ''">
								<a href="{$link}" target="_blank">
									<xsl:value-of select="ape:resource('eag2012.portal.gotothewebsite')"/>
								</a>
							</xsl:when>
							<xsl:when test="$text and $text != ''">
							     <xsl:value-of select="$text"/>
							</xsl:when>
						</xsl:choose>
					</div>
				</xsl:for-each>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

<!-- template for language Exhibition-->
	<xsl:template name="multilanguageExhibition">
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
								</p>
							</xsl:if>
						</xsl:for-each>
					</xsl:when>
					<xsl:otherwise> <!-- first language -->
						<xsl:for-each select="$list">
							<xsl:variable name="currentLang" select="current()/@xml:lang"></xsl:variable>
							<xsl:choose>
								<xsl:when test="not($currentLang)">
									<p class="multilanguageExhibitionNoLang">
										<xsl:value-of select="."/>
									</p>
								</xsl:when>
								<xsl:when test="$currentLang">
									  <xsl:variable name="classValue">
						                   <xsl:text>multilanguageExhibitionLang_</xsl:text><xsl:value-of select="$currentLang"></xsl:value-of>
					                  </xsl:variable>
									  <p class="{$classValue}">
									     <xsl:value-of select="."/>
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
					</p>
				</xsl:for-each>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	
	<!-- template for multilanguage for webpage ToursSessions-->
	<xsl:template name="multilanguageWebpageToursSessions">
		<xsl:param name="list"/>
		<xsl:choose>
			<xsl:when test="count($list) > 1">
				<xsl:choose>
					<xsl:when test="$list[@xml:lang = $language.selected]">
						<xsl:for-each select="$list">
							<xsl:variable name="link" select="current()/@href"></xsl:variable>
							<xsl:variable name="text" select="current()/text()"></xsl:variable>
							<xsl:variable name="currentLang" select="current()/@xml:lang"></xsl:variable>
							<xsl:if test="(not($currentLang) or $currentLang = $language.selected) and ($link or $text)">
								<div>
									<xsl:choose>
										<xsl:when test="$link and $link != '' and $text and $text != ''">
											<a href="{$link}" target="_blank">
											   <xsl:value-of select="$text"/>
											</a>
										</xsl:when>
										<xsl:when test="$link and $link != ''">
											<a href="{$link}" target="_blank">
												<xsl:value-of select="ape:resource('eag2012.portal.gotothewebsite')"/>
											</a>
										</xsl:when>
										<xsl:when test="$text and $text != ''">
										     <xsl:value-of select="$text"/>
										</xsl:when>
									</xsl:choose>
								</div>
							</xsl:if>
						</xsl:for-each>
					</xsl:when>
					<xsl:when test="$list[@xml:lang = $language.default]">
						<xsl:for-each select="$list">
							<xsl:variable name="link" select="current()/@href"></xsl:variable>
							<xsl:variable name="text" select="current()/text()"></xsl:variable>
							<xsl:variable name="currentLang" select="current()/@xml:lang"></xsl:variable>
							<xsl:if test="(not($currentLang) or $currentLang = $language.default) and ($link or $text)">
								<div>
									<xsl:choose>
										<xsl:when test="$link and $link != '' and $text and $text != ''">
											<a href="{$link}" target="_blank">
											   <xsl:value-of select="$text"/>
											</a>
										</xsl:when>
										<xsl:when test="$link and $link != ''">
											<a href="{$link}" target="_blank">
												<xsl:value-of select="ape:resource('eag2012.portal.gotothewebsite')"/>
											</a>
										</xsl:when>
										<xsl:when test="$text and $text != ''">
										    <xsl:value-of select="$text"/>
										</xsl:when>
									</xsl:choose>
								</div>
							</xsl:if>
						</xsl:for-each>
					</xsl:when>
					<xsl:otherwise> <!-- first language -->
						<xsl:for-each select="$list">
							<xsl:variable name="link" select="current()/@href"></xsl:variable>
							<xsl:variable name="text" select="current()/text()"></xsl:variable>
							<xsl:variable name="currentLang" select="current()/@xml:lang"></xsl:variable>
							<xsl:choose>
								<xsl:when test="not($currentLang)">
										<div class="webpageToursSessionsNoLang">
											<xsl:choose>
												<xsl:when test="$link and $link != '' and $text and $text != ''">
													<a href="{$link}" target="_blank">
													    <xsl:value-of select="$text"/>
													</a>
												</xsl:when>
												<xsl:when test="$link and $link != ''">
													<a href="{$link}" target="_blank">
														<xsl:value-of select="ape:resource('eag2012.portal.gotothewebsite')"/>
													</a>
												</xsl:when>
												<xsl:when test="$text and $text != ''">
												    	<xsl:value-of select="$text"/>
												</xsl:when>
											</xsl:choose>
										</div>
									</xsl:when>
									<xsl:when test="$currentLang">
										<xsl:variable name="classValue">
											<xsl:text>webpageToursSessionsLang_</xsl:text><xsl:value-of select="$currentLang"></xsl:value-of>
										</xsl:variable>
										<div class="{$classValue}">
											<xsl:choose>
												<xsl:when test="$link and $link != '' and $text and $text != ''">
													<a href="{$link}" target="_blank">
													    <xsl:value-of select="$text"/>
													</a>
												</xsl:when>
												<xsl:when test="$link and $link != ''">
													<a href="{$link}" target="_blank">
														<xsl:value-of select="ape:resource('eag2012.portal.gotothewebsite')"/>
													</a>
												</xsl:when>
												<xsl:when test="$text and $text != ''">
												    	<xsl:value-of select="$text"/>
												</xsl:when>
											</xsl:choose>
										</div>
									</xsl:when>
							</xsl:choose>
						</xsl:for-each>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
			<xsl:otherwise>
				<xsl:for-each select="$list">
					<xsl:variable name="link" select="current()/@href"></xsl:variable>
					<xsl:variable name="text" select="current()/text()"></xsl:variable>
					<xsl:variable name="currentLang" select="current()/@xml:lang"></xsl:variable>
					<div>
						<xsl:choose>
							<xsl:when test="$link and $link != '' and $text and $text != ''">
								<a href="{$link}" target="_blank">
								   <xsl:value-of select="$text"/>
								</a>
							</xsl:when>
							<xsl:when test="$link and $link != ''">
								<a href="{$link}" target="_blank">
									<xsl:value-of select="ape:resource('eag2012.portal.gotothewebsite')"/>
								</a>
							</xsl:when>
							<xsl:when test="$text and $text != ''">
							     <xsl:value-of select="$text"/>
							</xsl:when>
						</xsl:choose>
					</div>
				</xsl:for-each>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

<!-- template for language ToursSessions-->
	<xsl:template name="multilanguageToursSessions">
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
								</p>
							</xsl:if>
						</xsl:for-each>
					</xsl:when>
					<xsl:otherwise> <!-- first language -->
						<xsl:for-each select="$list">
							<xsl:variable name="currentLang" select="current()/@xml:lang"></xsl:variable>
							<xsl:choose>
								<xsl:when test="not($currentLang)">
									<p class="multilanguageToursSessionsNoLang">
										<xsl:value-of select="."/>
									</p>
								</xsl:when>
								<xsl:when test="$currentLang">
									  <xsl:variable name="classValue">
						                   <xsl:text>multilanguageToursSessionsLang_</xsl:text><xsl:value-of select="$currentLang"></xsl:value-of>
					                  </xsl:variable>
									  <p class="{$classValue}">
									     <xsl:value-of select="."/>
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
					</p>
				</xsl:for-each>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	
	<!-- template for multilanguage for webpage Other Services-->
	<xsl:template name="multilanguageWebpageOtherServices">
		<xsl:param name="list"/>
		<xsl:choose>
			<xsl:when test="count($list) > 1">
				<xsl:choose>
					<xsl:when test="$list[@xml:lang = $language.selected]">
						<xsl:for-each select="$list">
							<xsl:variable name="link" select="current()/@href"></xsl:variable>
							<xsl:variable name="text" select="current()/text()"></xsl:variable>
							<xsl:variable name="currentLang" select="current()/@xml:lang"></xsl:variable>
							<xsl:if test="(not($currentLang) or $currentLang = $language.selected) and ($link or $text)">
								<div>
									<xsl:choose>
										<xsl:when test="$link and $link != '' and $text and $text != ''">
											<a href="{$link}" target="_blank">
											   <xsl:value-of select="$text"/>
											</a>
										</xsl:when>
										<xsl:when test="$link and $link != ''">
											<a href="{$link}" target="_blank">
												<xsl:value-of select="ape:resource('eag2012.portal.gotothewebsite')"/>
											</a>
										</xsl:when>
										<xsl:when test="$text and $text != ''">
										     <xsl:value-of select="$text"/>
										</xsl:when>
									</xsl:choose>
								</div>
							</xsl:if>
						</xsl:for-each>
					</xsl:when>
					<xsl:when test="$list[@xml:lang = $language.default]">
						<xsl:for-each select="$list">
							<xsl:variable name="link" select="current()/@href"></xsl:variable>
							<xsl:variable name="text" select="current()/text()"></xsl:variable>
							<xsl:variable name="currentLang" select="current()/@xml:lang"></xsl:variable>
							<xsl:if test="(not($currentLang) or $currentLang = $language.default) and ($link or $text)">
								<div>
									<xsl:choose>
										<xsl:when test="$link and $link != '' and $text and $text != ''">
											<a href="{$link}" target="_blank">
											   <xsl:value-of select="$text"/>
											</a>
										</xsl:when>
										<xsl:when test="$link and $link != ''">
											<a href="{$link}" target="_blank">
												<xsl:value-of select="ape:resource('eag2012.portal.gotothewebsite')"/>
											</a>
										</xsl:when>
										<xsl:when test="$text and $text != ''">
										    <xsl:value-of select="$text"/>
										</xsl:when>
									</xsl:choose>
								</div>
							</xsl:if>
						</xsl:for-each>
					</xsl:when>
					<xsl:otherwise> <!-- first language -->
						<xsl:for-each select="$list">
							<xsl:variable name="link" select="current()/@href"></xsl:variable>
							<xsl:variable name="text" select="current()/text()"></xsl:variable>
							<xsl:variable name="currentLang" select="current()/@xml:lang"></xsl:variable>
							<xsl:choose>
								<xsl:when test="not($currentLang)">
										<div class="webpageOtherServicesNoLang">
											<xsl:choose>
												<xsl:when test="$link and $link != '' and $text and $text != ''">
													<a href="{$link}" target="_blank">
													    <xsl:value-of select="$text"/>
													</a>
												</xsl:when>
												<xsl:when test="$link and $link != ''">
													<a href="{$link}" target="_blank">
														<xsl:value-of select="ape:resource('eag2012.portal.gotothewebsite')"/>
													</a>
												</xsl:when>
												<xsl:when test="$text and $text != ''">
												    	<xsl:value-of select="$text"/>
												</xsl:when>
											</xsl:choose>
										</div>
									</xsl:when>
									<xsl:when test="$currentLang">
										<xsl:variable name="classValue">
											<xsl:text>webpageOtherServicesLang_</xsl:text><xsl:value-of select="$currentLang"></xsl:value-of>
										</xsl:variable>
										<div class="{$classValue}">
											<xsl:choose>
												<xsl:when test="$link and $link != '' and $text and $text != ''">
													<a href="{$link}" target="_blank">
													    <xsl:value-of select="$text"/>
													</a>
												</xsl:when>
												<xsl:when test="$link and $link != ''">
													<a href="{$link}" target="_blank">
														<xsl:value-of select="ape:resource('eag2012.portal.gotothewebsite')"/>
													</a>
												</xsl:when>
												<xsl:when test="$text and $text != ''">
												    	<xsl:value-of select="$text"/>
												</xsl:when>
											</xsl:choose>
										</div>
									</xsl:when>
							</xsl:choose>
						</xsl:for-each>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
			<xsl:otherwise>
				<xsl:for-each select="$list">
					<xsl:variable name="link" select="current()/@href"></xsl:variable>
					<xsl:variable name="text" select="current()/text()"></xsl:variable>
					<xsl:variable name="currentLang" select="current()/@xml:lang"></xsl:variable>
					<div>
						<xsl:choose>
							<xsl:when test="$link and $link != '' and $text and $text != ''">
								<a href="{$link}" target="_blank">
								   <xsl:value-of select="$text"/>
								</a>
							</xsl:when>
							<xsl:when test="$link and $link != ''">
								<a href="{$link}" target="_blank">
									<xsl:value-of select="ape:resource('eag2012.portal.gotothewebsite')"/>
								</a>
							</xsl:when>
							<xsl:when test="$text and $text != ''">
							     <xsl:value-of select="$text"/>
							</xsl:when>
						</xsl:choose>
					</div>
				</xsl:for-each>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

<!-- template for language OtherServices-->
	<xsl:template name="multilanguageOtherServices">
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
								</p>
							</xsl:if>
						</xsl:for-each>
					</xsl:when>
					<xsl:otherwise> <!-- first language -->
						<xsl:for-each select="$list">
							<xsl:variable name="currentLang" select="current()/@xml:lang"></xsl:variable>
							<xsl:choose>
								<xsl:when test="not($currentLang)">
									<p class="multilanguageOtherServicesNoLang">
										<xsl:value-of select="."/>
									</p>
								</xsl:when>
								<xsl:when test="$currentLang">
									  <xsl:variable name="classValue">
						                   <xsl:text>multilanguageOtherServicesLang_</xsl:text><xsl:value-of select="$currentLang"></xsl:value-of>
					                  </xsl:variable>
									  <p class="{$classValue}">
									     <xsl:value-of select="."/>
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
					</p>
				</xsl:for-each>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	
	<!-- template for telephone-->
	<xsl:template name="telephone">
		<xsl:param name="parent" select="current()"/>
		<xsl:param name="class"/>
		<xsl:param name="trClass" select="''"/>
		<xsl:if test="$parent/eag:telephone and $parent/eag:telephone/text()">
			<tr class="{$trClass}">
				<td class="{$class}">
					<xsl:value-of select="ape:resource('eag2012.portal.tel')"/><xsl:text>:</xsl:text>
				</td>
				<td>
					<xsl:for-each select="$parent/eag:telephone">
						<div>
							<xsl:value-of select="."/>
						</div>
					</xsl:for-each>
				</td>
			</tr>
		</xsl:if>
	</xsl:template>

	<!-- template for language only one element -->
	<xsl:template name="multilanguageOnlyOne">
		<xsl:param name="list"/>
		<xsl:choose>
			<xsl:when test="count($list) > 1">
				<xsl:choose>
					<xsl:when test="$list[@xml:lang = $language.selected] and $list[@xml:lang = $language.selected]/text() and $list[@xml:lang = $language.selected]/text() != ''">
						<xsl:for-each select="$list[@xml:lang = $language.selected]">
							<xsl:if test="position() = 1">
								<xsl:value-of select="."/>
							</xsl:if>
						</xsl:for-each>
					</xsl:when>
					<xsl:when test="$list[@xml:lang = $language.default] and $list[@xml:lang = $language.default]/text() and $list[@xml:lang = $language.default]/text() != ''">
						<xsl:for-each select="$list[@xml:lang = $language.default]">
							<xsl:if test="position() = 1">
								<xsl:value-of select="."/>
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
										<xsl:value-of select="$currentText"/>
									</xsl:if>
								</xsl:when>
								<xsl:otherwise>
									<xsl:if test="$currentLang = $language.first">
										<xsl:if test="position() = 1">
											<xsl:value-of select="$currentText"/>
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
					<xsl:value-of select="."/>
				</xsl:for-each>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<!-- template for language for visitors address. -->
	<xsl:template name="multilanguageAddress">
		<xsl:param name="list"/>
		<xsl:choose>
			<xsl:when test="count($list) > 1">
				<xsl:choose>
					<xsl:when test="$list/eag:street[@xml:lang = $language.selected] and $list/eag:street[@xml:lang = $language.selected]/text() and $list/eag:street[@xml:lang = $language.selected]/text() != ''">
						<xsl:for-each select="$list">
							<xsl:variable name="currentLang" select="current()/eag:street/@xml:lang"></xsl:variable>
							<xsl:variable name="currentStreetText" select="current()/eag:street/text()"></xsl:variable>
							<xsl:variable name="currentMunicipalityText" select="current()/eag:municipalityPostalcode/text()"></xsl:variable>
							<xsl:if test="(not($currentLang) or $currentLang = $language.selected) and ($currentStreetText or $currentMunicipalityText)">
								<p>
									<xsl:if test="$currentStreetText and $currentStreetText != ''">
										<xsl:value-of select="$currentStreetText"/>
										<xsl:if test="$currentMunicipalityText">
											<xsl:text>, </xsl:text>
										</xsl:if>
									</xsl:if>
									<xsl:if test="$currentMunicipalityText and $currentMunicipalityText != ''">
										<xsl:value-of select="$currentMunicipalityText"/>
									</xsl:if>
								</p>
							</xsl:if>
						</xsl:for-each>
					</xsl:when>
					<xsl:when test="$list/eag:street[@xml:lang = $language.default] and $list/eag:street[@xml:lang = $language.default]/text() and $list/eag:street[@xml:lang = $language.default]/text() != ''">
						<xsl:for-each select="$list">
							<xsl:variable name="currentLang" select="current()/eag:street/@xml:lang"></xsl:variable>
							<xsl:variable name="currentStreetText" select="current()/eag:street/text()"></xsl:variable>
							<xsl:variable name="currentMunicipalityText" select="current()/eag:municipalityPostalcode/text()"></xsl:variable>
							<xsl:if test="(not($currentLang) or $currentLang = $language.default) and ($currentStreetText or $currentMunicipalityText)">
								<p>
									<xsl:if test="$currentStreetText and $currentStreetText != ''">
										<xsl:value-of select="$currentStreetText"/>
										<xsl:if test="$currentMunicipalityText">
											<xsl:text>, </xsl:text>
										</xsl:if>
									</xsl:if>
									<xsl:if test="$currentMunicipalityText and $currentMunicipalityText != ''">
										<xsl:value-of select="$currentMunicipalityText"/>
									</xsl:if>
								</p>
							</xsl:if>
						</xsl:for-each>
					</xsl:when>
					<xsl:otherwise> <!-- first language -->
						<xsl:for-each select="$list">
							<xsl:variable name="currentLang" select="current()/eag:street/@xml:lang"></xsl:variable>
							<xsl:variable name="currentStreetText" select="current()/eag:street/text()"></xsl:variable>
							<xsl:variable name="currentMunicipalityText" select="current()/eag:municipalityPostalcode/text()"></xsl:variable>
							<xsl:choose>
						      <xsl:when test="not($currentLang)">
									<p class="addressNoLang">
									   <xsl:if test="current()/eag:street/text()">
											<xsl:value-of select="current()/eag:street/text()"/>
											<xsl:if test="current()/eag:municipalityPostalcode/text()">
												<xsl:text>, </xsl:text>
											</xsl:if>
										</xsl:if>
										<xsl:if test="current()/eag:municipalityPostalcode/text()">
											<xsl:value-of select="current()/eag:municipalityPostalcode/text()"/>
										</xsl:if>
									</p>	
								</xsl:when>
								<xsl:when test="$currentLang">
									  <xsl:variable name="classValue">
							             <xsl:text>addressLang_</xsl:text><xsl:value-of select="$currentLang"></xsl:value-of>
						              </xsl:variable>
									  <p class="{$classValue}">
									     <xsl:if test="current()/eag:street/text()">
											<xsl:value-of select="current()/eag:street/text()"/>
											<xsl:if test="current()/eag:municipalityPostalcode/text()">
												<xsl:text>, </xsl:text>
											</xsl:if>
										 </xsl:if>
										 <xsl:if test="current()/eag:municipalityPostalcode/text()">
											<xsl:value-of select="current()/eag:municipalityPostalcode/text()"/>
										 </xsl:if>
									  </p> 
								</xsl:when>
							</xsl:choose>
						</xsl:for-each>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
			<xsl:otherwise>
				<xsl:for-each select="$list">
					<xsl:if test="current()/eag:street/text()">
						<xsl:value-of select="current()/eag:street/text()"/>
						<xsl:if test="current()/eag:municipalityPostalcode/text()">
							<xsl:text>, </xsl:text>
						</xsl:if>
					</xsl:if>
					<xsl:if test="current()/eag:municipalityPostalcode/text()">
						<xsl:value-of select="current()/eag:municipalityPostalcode/text()"/>
					</xsl:if>
				</xsl:for-each>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	
	<!-- template for language postal address. -->
	<xsl:template name="multilanguagePostalAddress">
		<xsl:param name="list"/>
		<xsl:choose>
			<xsl:when test="count($list) > 1">
				<xsl:choose>
					<xsl:when test="$list/eag:street[@xml:lang = $language.selected] and $list/eag:street[@xml:lang = $language.selected]/text() and $list/eag:street[@xml:lang = $language.selected]/text() != ''">
						<xsl:for-each select="$list">
							<xsl:variable name="currentLang" select="current()/eag:street/@xml:lang"></xsl:variable>
							<xsl:variable name="currentStreetText" select="current()/eag:street/text()"></xsl:variable>
							<xsl:variable name="currentMunicipalityText" select="current()/eag:municipalityPostalcode/text()"></xsl:variable>
							<xsl:if test="(not($currentLang) or $currentLang = $language.selected) and ($currentStreetText or $currentMunicipalityText)">
								<p>
									<xsl:if test="$currentStreetText and $currentStreetText != ''">
										<xsl:value-of select="$currentStreetText"/>
										<xsl:if test="$currentMunicipalityText">
											<xsl:text>, </xsl:text>
										</xsl:if>
									</xsl:if>
									<xsl:if test="$currentMunicipalityText and $currentMunicipalityText != ''">
										<xsl:value-of select="$currentMunicipalityText"/>
									</xsl:if>
								</p>
							</xsl:if>
						</xsl:for-each>
					</xsl:when>
					<xsl:when test="$list/eag:street[@xml:lang = $language.default] and $list/eag:street[@xml:lang = $language.default]/text() and $list/eag:street[@xml:lang = $language.default]/text() != ''">
						<xsl:for-each select="$list">
							<xsl:variable name="currentLang" select="current()/eag:street/@xml:lang"></xsl:variable>
							<xsl:variable name="currentStreetText" select="current()/eag:street/text()"></xsl:variable>
							<xsl:variable name="currentMunicipalityText" select="current()/eag:municipalityPostalcode/text()"></xsl:variable>
							<xsl:if test="(not($currentLang) or $currentLang = $language.default) and ($currentStreetText or $currentMunicipalityText)">
								<p>
									<xsl:if test="$currentStreetText and $currentStreetText != ''">
										<xsl:value-of select="$currentStreetText"/>
										<xsl:if test="$currentMunicipalityText">
											<xsl:text>, </xsl:text>
										</xsl:if>
									</xsl:if>
									<xsl:if test="$currentMunicipalityText and $currentMunicipalityText != ''">
										<xsl:value-of select="$currentMunicipalityText"/>
									</xsl:if>
								</p>
							</xsl:if>
						</xsl:for-each>
					</xsl:when>
					<xsl:otherwise> <!-- first language -->
						<xsl:for-each select="$list">
							<xsl:variable name="currentLang" select="current()/eag:street/@xml:lang"></xsl:variable>
							<xsl:variable name="currentStreetText" select="current()/eag:street/text()"></xsl:variable>
							<xsl:variable name="currentMunicipalityText" select="current()/eag:municipalityPostalcode/text()"></xsl:variable>
							<xsl:choose>
						      <xsl:when test="not($currentLang)">
									<p class="postalAddressNoLang">
									   <xsl:if test="current()/eag:street/text()">
											<xsl:value-of select="current()/eag:street/text()"/>
											<xsl:if test="current()/eag:municipalityPostalcode/text()">
												<xsl:text>, </xsl:text>
											</xsl:if>
										</xsl:if>
										<xsl:if test="current()/eag:municipalityPostalcode/text()">
											<xsl:value-of select="current()/eag:municipalityPostalcode/text()"/>
										</xsl:if>
									</p>	
								</xsl:when>
								<xsl:when test="$currentLang">
									  <xsl:variable name="classValue">
							             <xsl:text>postalAddressLang_</xsl:text><xsl:value-of select="$currentLang"></xsl:value-of>
						              </xsl:variable>
									  <p class="{$classValue}">
									     <xsl:if test="current()/eag:street/text()">
											<xsl:value-of select="current()/eag:street/text()"/>
											<xsl:if test="current()/eag:municipalityPostalcode/text()">
												<xsl:text>, </xsl:text>
											</xsl:if>
										 </xsl:if>
										 <xsl:if test="current()/eag:municipalityPostalcode/text()">
											<xsl:value-of select="current()/eag:municipalityPostalcode/text()"/>
										 </xsl:if>
									  </p> 
								</xsl:when>
							</xsl:choose>
						</xsl:for-each>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
			<xsl:otherwise>
				<xsl:for-each select="$list">
					<xsl:if test="current()/eag:street/text()">
						<xsl:value-of select="current()/eag:street/text()"/>
						<xsl:if test="current()/eag:municipalityPostalcode/text()">
							<xsl:text>, </xsl:text>
						</xsl:if>
					</xsl:if>
					<xsl:if test="current()/eag:municipalityPostalcode/text()">
						<xsl:value-of select="current()/eag:municipalityPostalcode/text()"/>
					</xsl:if>
				</xsl:for-each>
			</xsl:otherwise>
		</xsl:choose>
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
									</p>
								</xsl:when>
								<xsl:when test="$currentLang">
									  <xsl:variable name="classValue">
						                   <xsl:text>multilanguageLang_</xsl:text><xsl:value-of select="$currentLang"></xsl:value-of>
					                  </xsl:variable>
									  <p class="{$classValue}">
									     <xsl:value-of select="."/>
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
					</p>
				</xsl:for-each>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	
<!-- template for language Accessibility -->
	<xsl:template name="multilanguageAccessibility">
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
								</p>
							</xsl:if>
						</xsl:for-each>
					</xsl:when>
					<xsl:otherwise> <!-- first language -->
						<xsl:for-each select="$list">
							<xsl:variable name="currentLang" select="current()/@xml:lang"></xsl:variable>
							<xsl:choose>
								<xsl:when test="not($currentLang)">
									<p class="multilanguageAccessibilityNoLang">
										<xsl:value-of select="."/>
									</p>
								</xsl:when>
								<xsl:when test="$currentLang">
									  <xsl:variable name="classValue">
						                   <xsl:text>multilanguageAccessibilityLang_</xsl:text><xsl:value-of select="$currentLang"></xsl:value-of>
					                  </xsl:variable>
									  <p class="{$classValue}">
									     <xsl:value-of select="."/>
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
					</p>
				</xsl:for-each>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	
	<!-- template for language restaccess -->
	<xsl:template name="multilanguageRestaccess">
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
								</p>
							</xsl:if>
						</xsl:for-each>
					</xsl:when>
					<xsl:otherwise> <!-- first language -->
						<xsl:for-each select="$list">
							<xsl:variable name="currentLang" select="current()/@xml:lang"></xsl:variable>
							<xsl:choose>
								<xsl:when test="not($currentLang)">
									<p class="multilanguageRestaccessNoLang">
										<xsl:value-of select="."/>
									</p>
								</xsl:when>
								<xsl:when test="$currentLang">
									  <xsl:variable name="classValue">
						                   <xsl:text>multilanguageRestaccessLang_</xsl:text><xsl:value-of select="$currentLang"></xsl:value-of>
					                  </xsl:variable>
									  <p class="{$classValue}">
									     <xsl:value-of select="."/>
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
					</p>
				</xsl:for-each>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	
	<!-- template for language opening hours -->
	<xsl:template name="multilanguageOpening">
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
								</p>
							</xsl:if>
						</xsl:for-each>
					</xsl:when>
					<xsl:otherwise> <!-- first language -->
						<xsl:for-each select="$list">
							<xsl:variable name="currentLang" select="current()/@xml:lang"></xsl:variable>
							<xsl:choose>
								<xsl:when test="not($currentLang)">
									<p class="multilanguageOpeningNoLang">
										<xsl:value-of select="."/>
									</p>
								</xsl:when>
								<xsl:when test="$currentLang">
									  <xsl:variable name="classValue">
						                   <xsl:text>multilanguageOpeningLang_</xsl:text><xsl:value-of select="$currentLang"></xsl:value-of>
					                  </xsl:variable>
									  <p class="{$classValue}">
									     <xsl:value-of select="."/>
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
					</p>
				</xsl:for-each>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	
	<!-- template for language closing hours -->
	<xsl:template name="multilanguageClosing">
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
								</p>
							</xsl:if>
						</xsl:for-each>
					</xsl:when>
					<xsl:otherwise> <!-- first language -->
						<xsl:for-each select="$list">
							<xsl:variable name="currentLang" select="current()/@xml:lang"></xsl:variable>
							<xsl:choose>
								<xsl:when test="not($currentLang)">
									<p class="multilanguageClosingNoLang">
										<xsl:value-of select="."/>
									</p>
								</xsl:when>
								<xsl:when test="$currentLang">
									  <xsl:variable name="classValue">
						                   <xsl:text>multilanguageClosingLang_</xsl:text><xsl:value-of select="$currentLang"></xsl:value-of>
					                  </xsl:variable>
									  <p class="{$classValue}">
									     <xsl:value-of select="."/>
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
					</p>
				</xsl:for-each>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	
<!-- template for language computerPlaces -->
	<xsl:template name="multilanguageComputerPlaces">
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
								</p>
							</xsl:if>
						</xsl:for-each>
					</xsl:when>
					<xsl:otherwise> <!-- first language -->
						<xsl:for-each select="$list">
							<xsl:variable name="currentLang" select="current()/@xml:lang"></xsl:variable>
							<xsl:choose>
								<xsl:when test="not($currentLang)">
									<p class="multilanguageComputerPlacesNoLang">
										<xsl:value-of select="."/>
									</p>
								</xsl:when>
								<xsl:when test="$currentLang">
									  <xsl:variable name="classValue">
						                   <xsl:text>multilanguageComputerPlacesLang_</xsl:text><xsl:value-of select="$currentLang"></xsl:value-of>
					                  </xsl:variable>
									  <p class="{$classValue}">
									     <xsl:value-of select="."/>
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
					</p>
				</xsl:for-each>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

<!-- template for language refreshment -->
	<xsl:template name="multilanguageRefreshment">
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
								</p>
							</xsl:if>
						</xsl:for-each>
					</xsl:when>
					<xsl:otherwise> <!-- first language -->
						<xsl:for-each select="$list">
							<xsl:variable name="currentLang" select="current()/@xml:lang"></xsl:variable>
							<xsl:choose>
								<xsl:when test="not($currentLang)">
									<p class="multilanguageRefreshmentNoLang">
										<xsl:value-of select="."/>
									</p>
								</xsl:when>
								<xsl:when test="$currentLang">
									  <xsl:variable name="classValue">
						                   <xsl:text>multilanguageRefreshmentLang_</xsl:text><xsl:value-of select="$currentLang"></xsl:value-of>
					                  </xsl:variable>
									  <p class="{$classValue}">
									     <xsl:value-of select="."/>
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
					</p>
				</xsl:for-each>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

<!-- template for language researchServices -->
	<xsl:template name="multilanguageResearchServices">
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
								</p>
							</xsl:if>
						</xsl:for-each>
					</xsl:when>
					<xsl:otherwise> <!-- first language -->
						<xsl:for-each select="$list">
							<xsl:variable name="currentLang" select="current()/@xml:lang"></xsl:variable>
							<xsl:choose>
								<xsl:when test="not($currentLang)">
									<p class="multilanguageResearchServicesNoLang">
										<xsl:value-of select="."/>
									</p>
								</xsl:when>
								<xsl:when test="$currentLang">
									  <xsl:variable name="classValue">
						                   <xsl:text>multilanguageResearchServicesLang_</xsl:text><xsl:value-of select="$currentLang"></xsl:value-of>
					                  </xsl:variable>
									  <p class="{$classValue}">
									     <xsl:value-of select="."/>
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
					</p>
				</xsl:for-each>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

<!-- template for language reproductionser -->
	<xsl:template name="multilanguageReproductionser">
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
								</p>
							</xsl:if>
						</xsl:for-each>
					</xsl:when>
					<xsl:otherwise> <!-- first language -->
						<xsl:for-each select="$list">
							<xsl:variable name="currentLang" select="current()/@xml:lang"></xsl:variable>
							<xsl:choose>
								<xsl:when test="not($currentLang)">
									<p class="multilanguageReproductionserNoLang">
										<xsl:value-of select="."/>
									</p>
								</xsl:when>
								<xsl:when test="$currentLang">
									  <xsl:variable name="classValue">
						                   <xsl:text>multilanguageReproductionserLang_</xsl:text><xsl:value-of select="$currentLang"></xsl:value-of>
					                  </xsl:variable>
									  <p class="{$classValue}">
									     <xsl:value-of select="."/>
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
					</p>
				</xsl:for-each>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	
	<!-- template for language description internet access -->
	<xsl:template name="multilanguageInternetaccess">
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
								</p>
							</xsl:if>
						</xsl:for-each>
					</xsl:when>
					<xsl:otherwise> <!-- first language -->
						<xsl:for-each select="$list">
							<xsl:variable name="currentLang" select="current()/@xml:lang"></xsl:variable>
							<xsl:choose>
								<xsl:when test="not($currentLang)">
									<p class="multilanguageInternetaccessNoLang">
										<xsl:value-of select="."/>
									</p>
								</xsl:when>
								<xsl:when test="$currentLang">
									  <xsl:variable name="classValue">
						                   <xsl:text>multilanguageInternetaccessLang_</xsl:text><xsl:value-of select="$currentLang"></xsl:value-of>
					                  </xsl:variable>
									  <p class="{$classValue}">
									     <xsl:value-of select="."/>
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
					</p>
				</xsl:for-each>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	
	<!-- template for language description conservationLaboratory -->
	<xsl:template name="multilanguageConservationLaboratory">
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
								</p>
							</xsl:if>
						</xsl:for-each>
					</xsl:when>
					<xsl:otherwise> <!-- first language -->
						<xsl:for-each select="$list">
							<xsl:variable name="currentLang" select="current()/@xml:lang"></xsl:variable>
							<xsl:choose>
								<xsl:when test="not($currentLang)">
									<p class="multilanguageConservationLaboratoryNoLang">
										<xsl:value-of select="."/>
									</p>
								</xsl:when>
								<xsl:when test="$currentLang">
									  <xsl:variable name="classValue">
						                   <xsl:text>multilanguageConservationLaboratoryLang_</xsl:text><xsl:value-of select="$currentLang"></xsl:value-of>
					                  </xsl:variable>
									  <p class="{$classValue}">
									     <xsl:value-of select="."/>
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
					</p>
				</xsl:for-each>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	
	<!-- template for language description holdings -->
	<xsl:template name="multilanguageHoldings">
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
								</p>
							</xsl:if>
						</xsl:for-each>
					</xsl:when>
					<xsl:otherwise> <!-- first language -->
						<xsl:for-each select="$list">
							<xsl:variable name="currentLang" select="current()/@xml:lang"></xsl:variable>
							<xsl:choose>
								<xsl:when test="not($currentLang)">
									<p class="multilanguageHoldingsNoLang">
										<xsl:value-of select="."/>
									</p>
								</xsl:when>
								<xsl:when test="$currentLang">
									  <xsl:variable name="classValue">
						                   <xsl:text>multilanguageHoldingsLang_</xsl:text><xsl:value-of select="$currentLang"></xsl:value-of>
					                  </xsl:variable>
									  <p class="{$classValue}">
									     <xsl:value-of select="."/>
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
					</p>
				</xsl:for-each>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	
	<!-- template for language repositorhist -->
	<xsl:template name="multilanguageRepositorhist">
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
								</p>
							</xsl:if>
						</xsl:for-each>
					</xsl:when>
					<xsl:otherwise> <!-- first language -->
						<xsl:for-each select="$list">
							<xsl:variable name="currentLang" select="current()/@xml:lang"></xsl:variable>
							<xsl:choose>
								<xsl:when test="not($currentLang)">
									<p class="multilanguageRepositorhistNoLang">
										<xsl:value-of select="."/>
									</p>
								</xsl:when>
								<xsl:when test="$currentLang">
									  <xsl:variable name="classValue">
						                   <xsl:text>multilanguageRepositorhistLang_</xsl:text><xsl:value-of select="$currentLang"></xsl:value-of>
					                  </xsl:variable>
									  <p class="{$classValue}">
									     <xsl:value-of select="."/>
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
					</p>
				</xsl:for-each>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	
	<!-- template for language repositorfound -->
	<xsl:template name="multilanguageRepositorfound">
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
								</p>
							</xsl:if>
						</xsl:for-each>
					</xsl:when>
					<xsl:otherwise> <!-- first language -->
						<xsl:for-each select="$list">
							<xsl:variable name="currentLang" select="current()/@xml:lang"></xsl:variable>
							<xsl:choose>
								<xsl:when test="not($currentLang)">
									<p class="multilanguageRepositorfoundNoLang">
										<xsl:value-of select="."/>
									</p>
								</xsl:when>
								<xsl:when test="$currentLang">
									  <xsl:variable name="classValue">
						                   <xsl:text>multilanguageRepositorfoundLang_</xsl:text><xsl:value-of select="$currentLang"></xsl:value-of>
					                  </xsl:variable>
									  <p class="{$classValue}">
									     <xsl:value-of select="."/>
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
					</p>
				</xsl:for-each>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

<!-- template for language repositorsup -->
	<xsl:template name="multilanguageRepositorsup">
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
								</p>
							</xsl:if>
						</xsl:for-each>
					</xsl:when>
					<xsl:otherwise> <!-- first language -->
						<xsl:for-each select="$list">
							<xsl:variable name="currentLang" select="current()/@xml:lang"></xsl:variable>
							<xsl:choose>
								<xsl:when test="not($currentLang)">
									<p class="multilanguageRepositorsupNoLang">
										<xsl:value-of select="."/>
									</p>
								</xsl:when>
								<xsl:when test="$currentLang">
									  <xsl:variable name="classValue">
						                   <xsl:text>multilanguageRepositorsupLang_</xsl:text><xsl:value-of select="$currentLang"></xsl:value-of>
					                  </xsl:variable>
									  <p class="{$classValue}">
									     <xsl:value-of select="."/>
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
					</p>
				</xsl:for-each>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	
	<!-- template for language adminunit -->
	<xsl:template name="multilanguageAdminunit">
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
								</p>
							</xsl:if>
						</xsl:for-each>
					</xsl:when>
					<xsl:otherwise> <!-- first language -->
						<xsl:for-each select="$list">
							<xsl:variable name="currentLang" select="current()/@xml:lang"></xsl:variable>
							<xsl:choose>
								<xsl:when test="not($currentLang)">
									<p class="multilanguageAdminunitNoLang">
										<xsl:value-of select="."/>
									</p>
								</xsl:when>
								<xsl:when test="$currentLang">
									  <xsl:variable name="classValue">
						                   <xsl:text>multilanguageAdminunitLang_</xsl:text><xsl:value-of select="$currentLang"></xsl:value-of>
					                  </xsl:variable>
									  <p class="{$classValue}">
									     <xsl:value-of select="."/>
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
					</p>
				</xsl:for-each>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

<!-- template for language building -->
	<xsl:template name="multilanguageBuilding">
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
								</p>
							</xsl:if>
						</xsl:for-each>
					</xsl:when>
					<xsl:otherwise> <!-- first language -->
						<xsl:for-each select="$list">
							<xsl:variable name="currentLang" select="current()/@xml:lang"></xsl:variable>
							<xsl:choose>
								<xsl:when test="not($currentLang)">
									<p class="multilanguageBuildingNoLang">
										<xsl:value-of select="."/>
									</p>
								</xsl:when>
								<xsl:when test="$currentLang">
									  <xsl:variable name="classValue">
						                   <xsl:text>multilanguageBuildingLang_</xsl:text><xsl:value-of select="$currentLang"></xsl:value-of>
					                  </xsl:variable>
									  <p class="{$classValue}">
									     <xsl:value-of select="."/>
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
					</p>
				</xsl:for-each>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	
	<!-- template for language parform -->
	<xsl:template name="multilanguageParform">
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
								</p>
							</xsl:if>
						</xsl:for-each>
					</xsl:when>
					<xsl:otherwise> <!-- first language -->
						<xsl:for-each select="$list">
							<xsl:variable name="currentLang" select="current()/@xml:lang"></xsl:variable>
							<xsl:choose>
								<xsl:when test="not($currentLang)">
									<p class="multilanguageParformNoLang">
										<xsl:value-of select="."/>
									</p>
								</xsl:when>
								<xsl:when test="$currentLang">
									  <xsl:variable name="classValue">
						                   <xsl:text>multilanguageParformLang_</xsl:text><xsl:value-of select="$currentLang"></xsl:value-of>
					                  </xsl:variable>
									  <p class="{$classValue}">
									     <xsl:value-of select="."/>
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
					</p>
				</xsl:for-each>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	
	<!-- template for language plus link-->
	<xsl:template name="multilanguageWithLink">
		<xsl:param name="list"/>
		<xsl:for-each select="$list">
			<xsl:variable name="link" select="current()/parent::node()/@href"></xsl:variable>
			<xsl:variable name="text" select="current()/text()"></xsl:variable>
			<xsl:variable name="currentLang" select="current()/@xml:lang"></xsl:variable>
			<div>
				<xsl:choose>
					<xsl:when test="$link and $link != '' and $text and $text != ''">
							<xsl:if test="starts-with($link, 'http') or starts-with($link, 'https') or starts-with($link, 'ftp') or starts-with($link, 'www')">
								<a href="{$link}" target="_blank">
									<xsl:choose>
										<xsl:when test="$currentLang = $language.selected or $currentLang = $language.default">
											<xsl:value-of select="$text"/>
										</xsl:when>
										<xsl:otherwise>
											<xsl:value-of select="ape:resource('eag2012.portal.linktorelatedresource')"/>
										</xsl:otherwise>
									</xsl:choose>
								</a>
							</xsl:if>
							<xsl:if test="not(starts-with($link, 'http')) and not(starts-with($link, 'https')) and not(starts-with($link, 'ftp')) and not(starts-with($link, 'www'))">
								<a href="#">
									<xsl:attribute name="onclick"><script>recoverRelatedInstitution('<xsl:value-of select="ape:related(current()/parent::node()/@href)"></xsl:value-of>');</script></xsl:attribute>
									<xsl:choose>
										<xsl:when test="$currentLang = $language.selected or $currentLang = $language.default">
											<xsl:value-of select="$text"/>
										</xsl:when>
										<xsl:otherwise>
											<xsl:value-of select="ape:resource('eag2012.portal.linktorelatedresource')"/>
										</xsl:otherwise>
									</xsl:choose>
								</a>
							</xsl:if>
					</xsl:when>
					<xsl:when test="$link and $link != ''">
							<xsl:if test="starts-with($link, 'http') or starts-with($link, 'https') or starts-with($link, 'ftp') or starts-with($link, 'www')">
								<a href="{$link}" target="_blank">
									<xsl:value-of select="ape:resource('eag2012.portal.linktorelatedresource')"/>
								</a>
							</xsl:if>
							<xsl:if test="not(starts-with($link, 'http')) and not(starts-with($link, 'https')) and not(starts-with($link, 'ftp')) and not(starts-with($link, 'www'))">
								<a href="#">
									<xsl:attribute name="onclick"><script>recoverRelatedInstitution('<xsl:value-of select="ape:related(current()/parent::node()/@href)"></xsl:value-of>');</script></xsl:attribute>
									<xsl:value-of select="ape:resource('eag2012.portal.linktorelatedresource')"/>
								</a>
							</xsl:if>
					</xsl:when>
					<xsl:when test="$text and $text != ''">
							<xsl:choose>
								<xsl:when test="$currentLang = $language.selected or $currentLang = $language.default">
									<xsl:value-of select="$text"/>
								</xsl:when>
								<xsl:otherwise>
									<xsl:value-of select="ape:resource('eag2012.portal.linktorelatedresource')"/>
								</xsl:otherwise>
							</xsl:choose>
					</xsl:when>
				</xsl:choose>

				<xsl:call-template name="multilanguageAssociatedrepositories">
					<xsl:with-param name="list" select="current()/parent::node()/eag:descriptiveNote/eag:p"></xsl:with-param>
				</xsl:call-template>
			</div>
		</xsl:for-each>
	</xsl:template>
	
	<!-- template for language related resources-->
	<xsl:template name="multilanguageAssociatedrepositories">
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
								</p>
							</xsl:if>
						</xsl:for-each>
					</xsl:when>
					<xsl:otherwise> <!-- first language -->
						<xsl:for-each select="$list">
							<xsl:variable name="currentLang" select="current()/@xml:lang"></xsl:variable>
							<xsl:choose>
								<xsl:when test="not($currentLang)">
									<p class="multilanguageAssociatedrepositoriesNoLang">
										<xsl:value-of select="."/>
									</p>
								</xsl:when>
								<xsl:when test="$currentLang">
									  <xsl:variable name="classValue">
						                   <xsl:text>multilanguageAssociatedrepositoriesLang_</xsl:text><xsl:value-of select="$currentLang"></xsl:value-of>
					                  </xsl:variable>
									  <p class="{$classValue}">
									     <xsl:value-of select="."/>
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
					</p>
				</xsl:for-each>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	
	<!-- template for language plus link-->
	<xsl:template name="multilanguageWithLinkRelatedResources">
		<xsl:param name="list"/>
		<xsl:for-each select="$list">
			<xsl:variable name="link" select="current()/parent::node()/@href"></xsl:variable>
			<xsl:variable name="text" select="current()/text()"></xsl:variable>
			<xsl:variable name="currentLang" select="current()/@xml:lang"></xsl:variable>
			<div>
				<xsl:choose>
					<xsl:when test="$link and $link != '' and $text and $text != ''">
							<xsl:if test="starts-with($link, 'http') or starts-with($link, 'https') or starts-with($link, 'ftp') or starts-with($link, 'www')">
								<a href="{$link}" target="_blank">
									<xsl:choose>
										<xsl:when test="$currentLang = $language.selected or $currentLang = $language.default">
											<xsl:value-of select="$text"/>
										</xsl:when>
										<xsl:otherwise>
											<xsl:value-of select="ape:resource('eag2012.portal.linktorelatedresource')"/>
										</xsl:otherwise>
									</xsl:choose>
								</a>
							</xsl:if>
							<xsl:if test="not(starts-with($link, 'http')) and not(starts-with($link, 'https')) and not(starts-with($link, 'ftp')) and not(starts-with($link, 'www'))">
								<a href="#">
									<xsl:attribute name="onclick"><script>recoverRelatedInstitution('<xsl:value-of select="ape:related(current()/parent::node()/@href)"></xsl:value-of>');</script></xsl:attribute>
									<xsl:choose>
										<xsl:when test="$currentLang = $language.selected or $currentLang = $language.default">
											<xsl:value-of select="$text"/>
										</xsl:when>
										<xsl:otherwise>
											<xsl:value-of select="ape:resource('eag2012.portal.linktorelatedresource')"/>
										</xsl:otherwise>
									</xsl:choose>
								</a>
							</xsl:if>
					</xsl:when>
					<xsl:when test="$link and $link != ''">
							<xsl:if test="starts-with($link, 'http') or starts-with($link, 'https') or starts-with($link, 'ftp') or starts-with($link, 'www')">
								<a href="{$link}" target="_blank">
									<xsl:value-of select="ape:resource('eag2012.portal.linktorelatedresource')"/>
								</a>
							</xsl:if>
							<xsl:if test="not(starts-with($link, 'http')) and not(starts-with($link, 'https')) and not(starts-with($link, 'ftp')) and not(starts-with($link, 'www'))">
								<a href="#">
									<xsl:attribute name="onclick"><script>recoverRelatedInstitution('<xsl:value-of select="ape:related(current()/parent::node()/@href)"></xsl:value-of>');</script></xsl:attribute>
									<xsl:value-of select="ape:resource('eag2012.portal.linktorelatedresource')"/>
								</a>
							</xsl:if>
					</xsl:when>
					<xsl:when test="$text and $text != ''">
							<xsl:choose>
								<xsl:when test="$currentLang = $language.selected or $currentLang = $language.default">
									<xsl:value-of select="$text"/>
								</xsl:when>
								<xsl:otherwise>
									<xsl:value-of select="ape:resource('eag2012.portal.linktorelatedresource')"/>
								</xsl:otherwise>
							</xsl:choose>
					</xsl:when>
				</xsl:choose>

				<xsl:call-template name="multilanguageRelatedResources">
					<xsl:with-param name="list" select="current()/parent::node()/eag:descriptiveNote/eag:p"></xsl:with-param>
				</xsl:call-template>
			</div>
		</xsl:for-each>
	</xsl:template>

<!-- template for language related resources-->
	<xsl:template name="multilanguageRelatedResources">
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
								</p>
							</xsl:if>
						</xsl:for-each>
					</xsl:when>
					<xsl:otherwise> <!-- first language -->
						<xsl:for-each select="$list">
							<xsl:variable name="currentLang" select="current()/@xml:lang"></xsl:variable>
							<xsl:choose>
								<xsl:when test="not($currentLang)">
									<p class="multilanguageRelatedResourcesNoLang">
										<xsl:value-of select="."/>
									</p>
								</xsl:when>
								<xsl:when test="$currentLang">
									  <xsl:variable name="classValue">
						                   <xsl:text>multilanguageRelatedResourcesLang_</xsl:text><xsl:value-of select="$currentLang"></xsl:value-of>
					                  </xsl:variable>
									  <p class="{$classValue}">
									     <xsl:value-of select="."/>
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
					</p>
				</xsl:for-each>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<!-- template for language nonpreform-->
	<xsl:template name="multilanguageNoperform">
		<xsl:param name="list"/>
			<xsl:choose>
				<xsl:when test="count($list) > 1">
					<xsl:choose>
						<xsl:when test="$list[@xml:lang = $language.selected] and $list[@xml:lang = $language.selected]/text() and $list[@xml:lang = $language.selected]/text() != ''">
							<xsl:for-each select="$list">
								<xsl:variable name="currentLang" select="current()/@xml:lang"></xsl:variable>
								<xsl:if test="$currentLang = $language.selected or not($currentLang)">
									<p>
										<xsl:apply-templates select="."/>
									</p>
								</xsl:if>
							</xsl:for-each>
						</xsl:when>
						<xsl:when test="$list[@xml:lang = $language.default] and $list[@xml:lang = $language.default]/text() and $list[@xml:lang = $language.default]/text() != ''">
							<xsl:for-each select="$list">
								<xsl:variable name="currentLang" select="current()/@xml:lang"></xsl:variable>
								<xsl:if test="not($currentLang) or $currentLang = $language.default">
									<p>
										<xsl:apply-templates select="."/>
									</p>
								</xsl:if>
							</xsl:for-each>
						</xsl:when>
						<xsl:otherwise>  <!-- first language -->
							<xsl:for-each select="$list">
								<xsl:variable name="currentLang" select="current()/@xml:lang"></xsl:variable>
								<xsl:choose>
									<xsl:when test="not($currentLang)">
										<p class="nonpreformNoLang">
											<xsl:apply-templates select="."/>
										</p>
									</xsl:when>
									<xsl:when test="$currentLang">
									  <xsl:variable name="classValue">
						                   <xsl:text>nonpreformLang_</xsl:text><xsl:value-of select="$currentLang"></xsl:value-of>
					                  </xsl:variable>
									  <p class="{$classValue}">
										<xsl:apply-templates select="."/>
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
							<xsl:apply-templates select="."/>
						</p>
					</xsl:for-each>
				</xsl:otherwise>
			</xsl:choose>
	</xsl:template>

	<!-- template for nonpreform-->
	<xsl:template match="eag:nonpreform">
		<xsl:if test="./text() and ./text() != ''">
			<xsl:value-of select="text()"/>
			<!-- when there are only 1 dateSet -->
			<xsl:if test="./eag:useDates/eag:dateSet and ((./eag:useDates/eag:dateSet/eag:dateRange and ./eag:useDates/eag:dateSet/eag:dateRange/eag:fromDate/text() and ./eag:useDates/eag:dateSet/eag:dateRange/eag:toDate/text()) or (./eag:useDates/eag:dateSet/eag:date and ./eag:useDates/eag:dateSet/eag:date/text()))">
				<xsl:text> (</xsl:text>
				<xsl:apply-templates select="./eag:useDates/eag:dateSet"/>
				<xsl:text>)</xsl:text>
			</xsl:if>
			<!-- when there are only 1 dateRange -->
			<xsl:if test="./eag:useDates/eag:dateRange and ./eag:useDates/eag:dateRange/eag:fromDate/text() and ./eag:useDates/eag:dateRange/eag:toDate/text()">
				<xsl:text> (</xsl:text>
				<xsl:apply-templates select="./eag:useDates/eag:dateRange"/>
				<xsl:text>)</xsl:text>
			</xsl:if>
			<!-- when there are only 1 date -->
			<xsl:if test="./eag:useDates/eag:date and ./eag:useDates/eag:date/text()">
				<xsl:text> (</xsl:text>
				<xsl:apply-templates select="./eag:useDates/eag:date"/>
				<xsl:text>)</xsl:text>
			</xsl:if>
		</xsl:if>
	</xsl:template>

	<!-- template for dateSet -->
	<xsl:template match="eag:dateSet">
		<xsl:if test="eag:dateRange or eag:date">
			<xsl:for-each select="eag:date">
				<xsl:if test="current()/text()">
					<xsl:value-of select="."/>
					<xsl:if test="position() != last()">
						<xsl:text>, </xsl:text>
					</xsl:if>
				</xsl:if>
			</xsl:for-each>
			<xsl:if test="eag:dateRange and eag:dateRange/eag:fromDate/text() and eag:dateRange/eag:toDate/text() and eag:date/text()">
				<xsl:text>, </xsl:text>
			</xsl:if>
			<xsl:for-each select="eag:dateRange">
				<xsl:if test="./eag:fromDate/text() and ./eag:toDate/text()">
					<xsl:value-of select="./eag:fromDate"/>
					<xsl:variable name="var" select="./eag:toDate"></xsl:variable>
					<xsl:choose>
						<xsl:when test="string(number(substring($var,1,2)))!='NaN'">
							<xsl:text> - </xsl:text>
						</xsl:when>
						<xsl:otherwise>
							<xsl:text> </xsl:text>
						</xsl:otherwise>
					</xsl:choose>
					<xsl:value-of select="./eag:toDate"/>
					<xsl:if test="position() != last()">
						<xsl:text>, </xsl:text>
					</xsl:if>
				</xsl:if>
			</xsl:for-each>
		</xsl:if>
	</xsl:template>
	
	<xsl:template match="eag:date">
		<xsl:if test="./text()">
			<xsl:if test="position() != 1">
				<xsl:text>, </xsl:text>
			</xsl:if>
			<xsl:value-of select="text()"/>
			<xsl:for-each select="eag:date">
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
	<xsl:template match="eag:dateRange">
		<xsl:if test="./eag:fromDate/text() and ./eag:toDate/text()">
			<xsl:if test="position() != 1">
				<xsl:text>, </xsl:text>
			</xsl:if>
			<xsl:value-of select="./eag:fromDate"/>
			<xsl:variable name="var" select="./eag:toDate"></xsl:variable>
			<xsl:choose>
				<xsl:when test="string(number(substring($var,1,2)))!='NaN'">
					<xsl:text> - </xsl:text>
				</xsl:when>
				<xsl:otherwise>
					<xsl:text> </xsl:text>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:value-of select="./eag:toDate"/>
			<xsl:for-each select="eag:dateRange">
				<xsl:if test="current()/eag:fromDate/text() and current()/eag:toDate/text()">
					<xsl:value-of select="./eag:fromDate"/>
					<xsl:variable name="var" select="./eag:toDate"></xsl:variable>
					<xsl:value-of select="./eag:toDate"/>
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

	<!-- template for multilanguage with childs -->
	<xsl:template name="multilanguageWithChilds">
		<xsl:param name="title"/>
		<xsl:param name="class" select="'header'"/>
		<xsl:param name="trClass" select="''"/>
		<xsl:param name="list"/>
		<xsl:if test="$list">
			<tr class="{$trClass}">
				<td class="{$class}">
					<xsl:value-of select="$title"/>
				</td>
				<td>
					<xsl:choose>
						<xsl:when test="count($list) > 1">
							<xsl:choose>
								<xsl:when test="$list[@xml:lang = $language.selected]">
									<xsl:for-each select="$list">
										<xsl:variable name="currentLang" select="current()/@xml:lang"></xsl:variable>
										<xsl:if test="$currentLang = $language.selected or not($currentLang)">
											<p>
												<xsl:apply-templates select="."/>
											</p>
										</xsl:if>
									</xsl:for-each>
								</xsl:when>
								<xsl:when test="$list[@xml:lang = $language.default]">
									<xsl:for-each select="$list">
										<xsl:variable name="currentLang" select="current()/@xml:lang"></xsl:variable>
										<xsl:if test="not($currentLang) or $currentLang = $language.default">
											<p>
												<xsl:apply-templates select="."/>
											</p>
										</xsl:if>
									</xsl:for-each>
								</xsl:when>
								<xsl:otherwise> <!-- first language -->
									<xsl:for-each select="$list">
										<xsl:variable name="currentLang" select="current()/@xml:lang"></xsl:variable>
										<xsl:choose>
											<xsl:when test="not($currentLang)">
												<p class="multilanguageWithChildsNoLang">
													<xsl:apply-templates select="."/>
												</p>
											</xsl:when>
											<xsl:when test="$currentLang">
											  <xsl:variable name="classValue">
								                   <xsl:text>multilanguageWithChildsLang_</xsl:text><xsl:value-of select="$currentLang"></xsl:value-of>
							                  </xsl:variable>
											  <p class="{$classValue}">
												<xsl:apply-templates select=".">
												  <xsl:with-param name="directions" select="$classValue"/>
												</xsl:apply-templates>
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
									<xsl:apply-templates select="."/>
								</p>
							</xsl:for-each>
						</xsl:otherwise>
					</xsl:choose>
				</td>
			</tr>
		</xsl:if>
	</xsl:template>
	
		<!-- template for multilanguage terms of use -->
	<xsl:template name="multilanguageTermsofuse">
		<xsl:param name="title"/>
		<xsl:param name="class" select="'header'"/>
		<xsl:param name="trClass" select="''"/>
		<xsl:param name="list"/>
		<xsl:if test="$list">
			<tr class="{$trClass}">
				<td class="{$class}">
					<xsl:value-of select="$title"/>
				</td>
				<td>
					<xsl:choose>
						<xsl:when test="count($list) > 1">
							<xsl:choose>
								<xsl:when test="$list[@xml:lang = $language.selected]">
									<xsl:for-each select="$list">
										<xsl:variable name="currentLang" select="current()/@xml:lang"></xsl:variable>
										<xsl:if test="$currentLang = $language.selected or not($currentLang)">
											<p>
												<xsl:apply-templates select="."/>
											</p>
										</xsl:if>
									</xsl:for-each>
								</xsl:when>
								<xsl:when test="$list[@xml:lang = $language.default]">
									<xsl:for-each select="$list">
										<xsl:variable name="currentLang" select="current()/@xml:lang"></xsl:variable>
										<xsl:if test="not($currentLang) or $currentLang = $language.default">
											<p>
												<xsl:apply-templates select="."/>
											</p>
										</xsl:if>
									</xsl:for-each>
								</xsl:when>
								<xsl:otherwise> <!-- first language -->
									<xsl:for-each select="$list">
										<xsl:variable name="currentLang" select="current()/@xml:lang"></xsl:variable>
										<xsl:choose>
											<xsl:when test="not($currentLang)">
												<p class="multilanguageTermsofuseNoLang">
													<xsl:apply-templates select="."/>
												</p>
											</xsl:when>
											<xsl:when test="$currentLang">
											  <xsl:variable name="classValue">
								                   <xsl:text>multilanguageTermsofuseLang_</xsl:text><xsl:value-of select="$currentLang"></xsl:value-of>
							                  </xsl:variable>
											  <p class="{$classValue}">
												<xsl:apply-templates select="."/>
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
									<xsl:apply-templates select="."/>
								</p>
							</xsl:for-each>
						</xsl:otherwise>
					</xsl:choose>
				</td>
			</tr>
		</xsl:if>
	</xsl:template>

	<!-- template for multilanguage readersTicket -->
	<xsl:template name="multilanguageReadersTicket">
		<xsl:param name="title"/>
		<xsl:param name="class" select="'header'"/>
		<xsl:param name="trClass" select="''"/>
		<xsl:param name="list"/>
		<xsl:if test="$list">
			<tr class="{$trClass}">
				<td class="{$class}">
					<xsl:value-of select="$title"/>
				</td>
				<td>
					<xsl:choose>
						<xsl:when test="count($list) > 1">
							<xsl:choose>
								<xsl:when test="$list[@xml:lang = $language.selected]">
									<xsl:for-each select="$list">
										<xsl:variable name="currentLang" select="current()/@xml:lang"></xsl:variable>
										<xsl:if test="$currentLang = $language.selected or not($currentLang)">
											<p>
												<xsl:apply-templates select="."/>
											</p>
										</xsl:if>
									</xsl:for-each>
								</xsl:when>
								<xsl:when test="$list[@xml:lang = $language.default]">
									<xsl:for-each select="$list">
										<xsl:variable name="currentLang" select="current()/@xml:lang"></xsl:variable>
										<xsl:if test="not($currentLang) or $currentLang = $language.default">
											<p>
												<xsl:apply-templates select="."/>
											</p>
										</xsl:if>
									</xsl:for-each>
								</xsl:when>
								<xsl:otherwise> <!-- first language -->
									<xsl:for-each select="$list">
										<xsl:variable name="currentLang" select="current()/@xml:lang"></xsl:variable>
										<xsl:choose>
											<xsl:when test="not($currentLang)">
												<p class="multilanguageReadersTicketNoLang">
													<xsl:apply-templates select="."/>
												</p>
											</xsl:when>
											<xsl:when test="$currentLang">
											  <xsl:variable name="classValue">
								                   <xsl:text>multilanguageReadersTicketLang_</xsl:text><xsl:value-of select="$currentLang"></xsl:value-of>
							                  </xsl:variable>
											  <p class="{$classValue}">
												<xsl:apply-templates select="."/>
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
									<xsl:apply-templates select="."/>
								</p>
							</xsl:for-each>
						</xsl:otherwise>
					</xsl:choose>
				</td>
			</tr>
		</xsl:if>
	</xsl:template>

	<!-- template for multilanguage advancedOrders -->
	<xsl:template name="multilanguageAdvancedOrders">
		<xsl:param name="title"/>
		<xsl:param name="class" select="'header'"/>
		<xsl:param name="trClass" select="''"/>
		<xsl:param name="list"/>
		<xsl:if test="$list">
			<tr class="{$trClass}">
				<td class="{$class}">
					<xsl:value-of select="$title"/>
				</td>
				<td>
					<xsl:choose>
						<xsl:when test="count($list) > 1">
							<xsl:choose>
								<xsl:when test="$list[@xml:lang = $language.selected]">
									<xsl:for-each select="$list">
										<xsl:variable name="currentLang" select="current()/@xml:lang"></xsl:variable>
										<xsl:if test="$currentLang = $language.selected or not($currentLang)">
											<p>
												<xsl:apply-templates select="."/>
											</p>
										</xsl:if>
									</xsl:for-each>
								</xsl:when>
								<xsl:when test="$list[@xml:lang = $language.default]">
									<xsl:for-each select="$list">
										<xsl:variable name="currentLang" select="current()/@xml:lang"></xsl:variable>
										<xsl:if test="not($currentLang) or $currentLang = $language.default">
											<p>
												<xsl:apply-templates select="."/>
											</p>
										</xsl:if>
									</xsl:for-each>
								</xsl:when>
								<xsl:otherwise> <!-- first language -->
									<xsl:for-each select="$list">
										<xsl:variable name="currentLang" select="current()/@xml:lang"></xsl:variable>
										<xsl:choose>
											<xsl:when test="not($currentLang)">
												<p class="multilanguageAdvancedOrdersNoLang">
													<xsl:apply-templates select="."/>
												</p>
											</xsl:when>
											<xsl:when test="$currentLang">
											  <xsl:variable name="classValue">
								                   <xsl:text>multilanguageAdvancedOrdersLang_</xsl:text><xsl:value-of select="$currentLang"></xsl:value-of>
							                  </xsl:variable>
											  <p class="{$classValue}">
												<xsl:apply-templates select="."/>
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
									<xsl:apply-templates select="."/>
								</p>
							</xsl:for-each>
						</xsl:otherwise>
					</xsl:choose>
				</td>
			</tr>
		</xsl:if>
	</xsl:template>

	<!-- template for language exhibition -->
	<xsl:template name="multilanguageRecreationalServicesExhibition">
		<xsl:param name="list"/>
	    <xsl:for-each select="$list">
			<div>
				<xsl:variable name="descriptionList" select="current()/eag:descriptiveNote/eag:p"></xsl:variable>
				<xsl:variable name="webList" select="current()/eag:webpage"></xsl:variable>
				<xsl:call-template name="multilanguageExhibition">
					<xsl:with-param name="list" select="$descriptionList"/>
				</xsl:call-template>
				<xsl:call-template name="multilanguageWebpageExhibition">
					<xsl:with-param name="list" select="$webList"/>
				</xsl:call-template>
			</div>
	   </xsl:for-each> 
	</xsl:template>

	<!-- template for language toursSessions -->
	<xsl:template name="multilanguageRecreationalServicesToursSessions">
		<xsl:param name="list"/>
		<xsl:for-each select="$list">
			<div>
				<xsl:variable name="descriptionList" select="current()/eag:descriptiveNote/eag:p"></xsl:variable>
				<xsl:variable name="webList" select="current()/eag:webpage"></xsl:variable>
				<xsl:call-template name="multilanguageToursSessions">
					<xsl:with-param name="list" select="$descriptionList"/>
				</xsl:call-template>
				<xsl:call-template name="multilanguageWebpageToursSessions">
					<xsl:with-param name="list" select="$webList"/>
				</xsl:call-template>
			</div>
		</xsl:for-each>
	</xsl:template>

	<!-- template for language otherServices -->
	<xsl:template name="multilanguageRecreationalServicesOtherServices">
		<xsl:param name="list"/>
		<xsl:for-each select="$list">
			<div>
				<xsl:variable name="descriptionList" select="current()/eag:descriptiveNote/eag:p"></xsl:variable>
				<xsl:variable name="webList" select="current()/eag:webpage"></xsl:variable>
				<xsl:call-template name="multilanguageOtherServices">
					<xsl:with-param name="list" select="$descriptionList"/>
				</xsl:call-template>
				<xsl:call-template name="multilanguageWebpageOtherServices">
					<xsl:with-param name="list" select="$webList"/>
				</xsl:call-template>
			</div>
		</xsl:for-each>
	</xsl:template>

	<!-- template for advancedOrders -->
	<xsl:template match="eag:advancedOrders">
		<xsl:choose>
			<xsl:when test="./@href and ./@href != ''">
				<xsl:variable name="href" select="./@href"/>
				<a href="{$href}" target="_blank">
					<xsl:choose>
						<xsl:when test="./text() and ./text() != ''">
							<xsl:value-of select="."/>
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="ape:resource('eag2012.portal.advancedorderslink')"/>
						</xsl:otherwise>
					</xsl:choose>
				</a>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="."/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<!-- template for readersTicket -->
	<xsl:template match="eag:readersTicket">
		<xsl:choose>
			<xsl:when test="./@href and ./@href != ''">
				<xsl:variable name="href" select="./@href"/>
				<a href="{$href}" target="_blank">
					<xsl:choose>
						<xsl:when test="./text() and ./text() != ''">
							<xsl:value-of select="."/>
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="ape:resource('eag2012.portal.readersticketlink')"/>
						</xsl:otherwise>
					</xsl:choose>
				</a>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="."/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<!-- template for termsOfUse -->
	<xsl:template match="eag:termsOfUse">
		<xsl:choose>
			<xsl:when test="./@href and ./@href != ''">
				<xsl:variable name="href" select="./@href"/>
				<a href="{$href}" target="_blank">
					<xsl:choose>
						<xsl:when test="text() and text() != ''">
							<xsl:value-of select="."/>
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="ape:resource('eag2012.portal.termsofuselink')"/>
						</xsl:otherwise>
					</xsl:choose>
				</a>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="."/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<!-- template for citation -->
	<xsl:template match="eag:citation">
		<xsl:choose>
			<xsl:when test="./@href and ./@href != ''">
				<xsl:variable name="href" select="./@href"/>
				<a href="{$href}" target="_blank">
					<xsl:value-of select="ape:resource('eag2012.portal.directionscitation')"/>
				</a>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="."/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<!-- template for citation-->
  	<xsl:template match="eag:directions">
  	    <xsl:param name="directions"/>
		<xsl:value-of select="text()"/>
		<xsl:for-each select="eag:citation">
			 <p class="{$directions}">
				<xsl:apply-templates select="."/>
		     </p>
		</xsl:for-each>
	</xsl:template>  

	<!-- template for numeric values-->
	<xsl:template match="eag:num">
		<xsl:variable name="unit" select="concat('eag2012.portal.num.unit.', ./@unit)"/>
		<xsl:value-of select="text()"/>
		<xsl:text> </xsl:text>
		<xsl:value-of select="ape:resource($unit)" disable-output-escaping="yes"/>
	</xsl:template>
</xsl:stylesheet>
