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
		<h2 class="blockHeader">
			<xsl:value-of select="./eag:eag/eag:archguide/eag:identity/eag:autform"></xsl:value-of>
		</h2>

		<!-- CONTACT -->
		<!-- starts loop -->
		<xsl:for-each select="./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository">
			<xsl:variable name="id" select="position()"/>
			<div id="repository_{$id}">
				<xsl:if test="count(current()/parent::node()/eag:repository)> 1">
					<h3 class="repositoryName">
						<xsl:call-template name="multilanguageOnlyOne">
							<xsl:with-param name="list" select="./eag:repositoryName"></xsl:with-param>
						</xsl:call-template>
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
					</h3>
				</xsl:if>
				<div class="repositoryInfo">
					<table class="aiSection contactDisplay">
						<thead>
							<tr>
								<th colspan="2">
									<xsl:value-of select="ape:resource('eag2012.portal.contact')"/>
									<!-- Check if occult parts exists -->
									<xsl:if test="eag:location[not(@localType) or @localType='visitors address']/eag:localentity/text() or eag:location[not(@localType) or @localType='visitors address']/eag:secondem/text() or eag:location/eag:firstdem/text() or eag:location[@localType='postal address'] or eag:fax and eag:fax/text() != ''">
										<xsl:text> (</xsl:text>
										<a class="displayLinkSeeMore" href="javascript:seeMore('contactDisplay','{$id}');">
											<xsl:value-of select="ape:resource('eag2012.portal.seemore')"/>
										</a>
										<a class="displayLinkSeeLess" href="javascript:seeLess('contactDisplay','{$id}');">
											<xsl:value-of select="ape:resource('eag2012.portal.seeless')"/>
										</a>
										<xsl:text>)</xsl:text>
									</xsl:if>
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

							<!-- local entity only shown if there are values-->
							<xsl:if test="eag:location[not(@localType) or @localType='visitors address']/eag:localentity/text()">
								<tr class="longDisplay">
									<td class="header">
										<xsl:value-of select="ape:resource('eag2012.portal.district')"/><xsl:text>:</xsl:text>
									</td>
									<td>
										<xsl:call-template name="multilanguage">
											<xsl:with-param name="list" select="eag:location[not(@localType) or @localType='visitors address']/eag:localentity"></xsl:with-param>
										</xsl:call-template>
									</td>
								</tr>
							</xsl:if>

							<!-- secondem only shown if there are values-->
							<xsl:if test="eag:location[not(@localType) or @localType='visitors address']/eag:secondem/text()">
								<tr class="longDisplay">
									<td class="header">
										<xsl:value-of select="ape:resource('eag2012.portal.countrylocalauthority')"/><xsl:text>:</xsl:text>
									</td>
									<td>
										<xsl:call-template name="multilanguage">
											<xsl:with-param name="list" select="eag:location[not(@localType) or @localType='visitors address']/eag:secondem"></xsl:with-param>
										</xsl:call-template>
									</td>
								</tr>
							</xsl:if>

							<!-- firstdem only shown if there are values-->
							<xsl:if test="eag:location[not(@localType) or @localType='visitors address']/eag:firstdem/text()">
								<tr class="longDisplay">
									<td class="header">
										<xsl:value-of select="ape:resource('eag2012.portal.regionautonomousauthority')"/><xsl:text>:</xsl:text>
									</td>
									<td>
										<xsl:call-template name="multilanguage">
											<xsl:with-param name="list" select="eag:location[not(@localType) or @localType='visitors address']/eag:firstdem"></xsl:with-param>
										</xsl:call-template>
									</td>
								</tr>
							</xsl:if>

							<!-- postal address only shown if there are values-->
							<xsl:if test="eag:location[@localType='postal address'] and eag:location[@localType='postal address']/eag:street/text() and eag:location[@localType='postal address']/eag:municipalityPostalcode/text() and eag:location[@localType='postal address']/eag:municipalityPostalcode/text()">
								<tr class="longDisplay">
									<td class="header">
										<xsl:value-of select="ape:resource('eag2012.portal.postaladdress')"></xsl:value-of><xsl:text>:</xsl:text>
									</td>
									<td class="postalAddress">
										<xsl:call-template name="multilanguageAddress">
											<xsl:with-param name="list" select="eag:location[@localType='visitors address']"></xsl:with-param>
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

							<!-- fax only shown if there are values-->
							<xsl:if test="eag:fax and eag:fax/text() != ''">
								<tr class="longDisplay">
									<td class="header">
										<xsl:value-of select="ape:resource('eag2012.portal.fax')"/><xsl:text>:</xsl:text>
									</td>
									<td>
										<xsl:for-each select="eag:fax">
											<div>
												<xsl:value-of select="."/>
											</div>
										</xsl:for-each>
									</td>
								</tr>
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
									
					<!-- ACCESS INFORMATION -->
					<table class="aiSection accessDisplay">
						<thead>
							<tr>
								<th colspan="2">
									<xsl:value-of select="ape:resource('eag2012.portal.accessandserviceinfo')"/>
									<xsl:text> (</xsl:text>
									<a class="displayLinkSeeMore" href="javascript:seeMore('accessDisplay','{$id}');">
										<xsl:value-of select="ape:resource('eag2012.portal.seemore')"/>
									</a>
									<a class="displayLinkSeeLess" href="javascript:seeLess('accessDisplay','{$id}');">
										<xsl:value-of select="ape:resource('eag2012.portal.seeless')"/>
									</a>
									<xsl:text>)</xsl:text>
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
										<xsl:call-template name="multilanguage">
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
										<xsl:call-template name="multilanguage">
											<xsl:with-param name="list" select="eag:timetable/eag:closing"></xsl:with-param>
										</xsl:call-template>
									</td>
								</tr>
							</xsl:if>

							<!-- directions and citations goes to template-->
							<xsl:if test="eag:directions and eag:directions/text() or (eag:directions/eag:citation and eag:directions/eag:citation/@href and eag:directions/eag:citation/@href != '') ">
								<xsl:call-template name="multilanguageWithChilds">
									<xsl:with-param name="title">
										<xsl:value-of select="ape:resource('eag2012.portal.directions')"/><xsl:text>:</xsl:text>
									</xsl:with-param>
									<xsl:with-param name="trClass" select="'longDisplay'"/>
									<xsl:with-param name="list" select="eag:directions"/>
								</xsl:call-template>
							</xsl:if>


							<!-- accessconditions only shown if there are values-->
							<xsl:if test="eag:access">
								<tr>
									<td class="header">
										<xsl:value-of select="ape:resource('eag2012.portal.accessconditions')"/><xsl:text>:</xsl:text>
									</td>
									<td>
										<xsl:choose>
											<xsl:when test="eag:access[@question='yes']">
												<xsl:value-of select="ape:resource('eag2012.portal.accesspublic')"/>
											</xsl:when>
											<xsl:otherwise>
												<xsl:value-of select="ape:resource('eag2012.portal.accesspermission')"/>
											</xsl:otherwise>
										</xsl:choose>
									</td>
								</tr>
							</xsl:if>

							<!-- restaccess only shown if there are values-->
							<xsl:if test="eag:access/eag:restaccess/text()">
								<tr>
									<td class="header">
										<xsl:value-of select="ape:resource('eag2012.portal.accessinformation')"/><xsl:text>:</xsl:text>
									</td>
									<td>
										<xsl:call-template name="multilanguage">
											<xsl:with-param name="list" select="eag:access/eag:restaccess"></xsl:with-param>
										</xsl:call-template>
									</td>
								</tr>
							</xsl:if>

							<!-- accessibility only shown if there are values-->
							<xsl:if test="eag:accessibility">
								<tr class="longDisplay">
									<td class="header">
										<xsl:value-of select="ape:resource('eag2012.portal.disabledaccess')"/><xsl:text>:</xsl:text>
									</td>
									<td>
										<xsl:choose>
											<xsl:when test="eag:accessibility[@question='yes']">
												<xsl:value-of select="ape:resource('eag2012.portal.facilities')"/>
											</xsl:when>
											<xsl:otherwise>
												<xsl:value-of select="ape:resource('eag2012.portal.nofacilities')"/>
											</xsl:otherwise>
										</xsl:choose>
									</td>
								</tr>
							</xsl:if>

							<!-- accessibility text only shown if there are values-->
							<xsl:if test="eag:accessibility/text()">
								<tr title="repository">
									<td class="header">
										<xsl:value-of select="ape:resource('eag2012.portal.facilitiesfordisabledpersons')"/><xsl:text>:</xsl:text>
									</td>
									<td>
										<xsl:call-template name="multilanguage">
											<xsl:with-param name="list" select="eag:accessibility"></xsl:with-param>
										</xsl:call-template>
									</td>
								</tr>
							</xsl:if>

							<!-- termsOfUse only shown if there are values-->
							<xsl:if test="eag:access/eag:termsOfUse and eag:access/eag:termsOfUse/text()">
								<xsl:call-template name="multilanguageWithChilds">
									<xsl:with-param name="title">
										<xsl:value-of select="ape:resource('eag2012.portal.termsofuse')"/><xsl:text>:</xsl:text>
									</xsl:with-param>
									<xsl:with-param name="list" select="eag:access/eag:termsOfUse"/>
									<xsl:with-param name="trClass" select="'longDisplay'"/>
								</xsl:call-template>
							</xsl:if>

							<!-- readersTicket shown template-->
							<xsl:if test="eag:services/eag:searchroom/eag:readersTicket">
								<xsl:call-template name="multilanguageWithChilds">
									<xsl:with-param name="title">
										<xsl:value-of select="ape:resource('eag2012.portal.readersticket')"/><xsl:text>:</xsl:text>
									</xsl:with-param>
									<xsl:with-param name="list" select="eag:services/eag:searchroom/eag:readersTicket"/>
								</xsl:call-template>
							</xsl:if>

							<!-- searchroom only shown if there are values-->
							<xsl:if test="eag:services/eag:searchroom/eag:workPlaces/eag:num/text()">
								<tr>
									<td class="header">
										<xsl:value-of select="ape:resource('eag2012.portal.searchroom')"/><xsl:text>:</xsl:text>
									</td>
									<td>
										<xsl:apply-templates select="eag:services/eag:searchroom/eag:workPlaces/eag:num"/>
									</td>
								</tr>
							</xsl:if>

							<!-- advancedOrders only shown if there are values-->
							<xsl:if test="eag:services/eag:searchroom/eag:advancedOrders">
								<xsl:call-template name="multilanguageWithChilds">
									<xsl:with-param name="title">
										<xsl:value-of select="ape:resource('eag2012.portal.orderingdocuments')"/><xsl:text>:</xsl:text>
									</xsl:with-param>
									<xsl:with-param name="list" select="eag:services/eag:searchroom/eag:advancedOrders"/>
								</xsl:call-template>
							</xsl:if>

							<!-- contact searchroom only shown if there are values-->
							<xsl:if test="eag:services/eag:searchroom/eag:contact and (eag:services/eag:searchroom/eag:contact/eag:telephone/text() or eag:services/eag:searchroom/eag:contact/eag:email/@href != '' or eag:services/eag:searchroom/eag:webpage/@href != '')">
								<tr class="longDisplay">
									<td class="header subInfoHeader" colspan="2">
										<xsl:value-of select="ape:resource('eag2012.portal.searchroomcontact')"/>
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

							<!-- researchServices only shown if there are values-->
							<xsl:if test="eag:services/eag:searchroom/eag:researchServices/eag:descriptiveNote/eag:p/text()">
								<tr class="longDisplay">
									<td class="header">
										<xsl:value-of select="ape:resource('eag2012.portal.archivesresearchservice')"/><xsl:text>:</xsl:text>
									</td>
									<td>
										<xsl:call-template name="multilanguage">
											<xsl:with-param name="list" select="eag:services/eag:searchroom/eag:researchServices/eag:descriptiveNote/eag:p"/>
										</xsl:call-template>
									</td>
								</tr>
							</xsl:if>

							<!-- computerPlaces only shown if there are values-->
							<xsl:if test="eag:services/eag:searchroom/eag:computerPlaces and ((eag:services/eag:searchroom/eag:computerPlaces/eag:num and eag:services/eag:searchroom/eag:computerPlaces/eag:num/text() and eag:services/eag:searchroom/eag:computerPlaces/eag:num/text() != '') or (eag:services/eag:searchroom/eag:computerPlaces/eag:descriptiveNote/eag:p and eag:services/eag:searchroom/eag:computerPlaces/eag:descriptiveNote/eag:p/text() and eag:services/eag:searchroom/eag:computerPlaces/eag:descriptiveNote/eag:p/text() != ''))">
								<tr class="longDisplay">
									<td class="header">
										<xsl:value-of select="ape:resource('eag2012.portal.computerplaces')"/><xsl:text>:</xsl:text>
									</td>
									<td>
										<xsl:if test="eag:services/eag:searchroom/eag:computerPlaces/eag:num and eag:services/eag:searchroom/eag:computerPlaces/eag:num/text() and eag:services/eag:searchroom/eag:computerPlaces/eag:num/text() != ''">
											<xsl:apply-templates select="eag:services/eag:searchroom/eag:computerPlaces/eag:num"/>
										</xsl:if>
										<xsl:if test="eag:services/eag:searchroom/eag:computerPlaces/eag:descriptiveNote/eag:p and eag:services/eag:searchroom/eag:computerPlaces/eag:descriptiveNote/eag:p/text() and eag:services/eag:searchroom/eag:computerPlaces/eag:descriptiveNote/eag:p/text() != ''">
											<xsl:call-template name="multilanguage">
												<xsl:with-param name="list" select="eag:services/eag:searchroom/eag:computerPlaces/eag:descriptiveNote/eag:p"/>
											</xsl:call-template>
										</xsl:if>
									</td>
								</tr>
							</xsl:if>

							<!-- microfilmPlaces only shown if there are values-->
							<xsl:if test="eag:services/eag:searchroom/eag:microfilmPlaces/eag:num/text() and eag:services/eag:searchroom/eag:microfilmPlaces/eag:num/text() != ''">
								<tr class="longDisplay">
									<td class="header">
										<xsl:value-of select="ape:resource('eag2012.portal.microfilmplaces')"/><xsl:text>:</xsl:text>
									</td>
									<td>
										<xsl:apply-templates select="eag:services/eag:searchroom/eag:microfilmPlaces/eag:num"/>
									</td>
								</tr>
							</xsl:if>

							<!-- photographAllowance only shown if there are values-->
							<xsl:if test="eag:services/eag:searchroom/eag:photographAllowance/text()">
								<tr class="longDisplay">
									<td class="header">
										<xsl:value-of select="ape:resource('eag2012.portal.photographAllowance')"/><xsl:text>:</xsl:text>
									</td>
									<td>
										<xsl:variable name="photo"  select="eag:services/eag:searchroom/eag:photographAllowance/text()"></xsl:variable>
											<xsl:for-each select="$photo">
										<div>
											<xsl:choose>
												<xsl:when test="$photo = 'no'">
													<xsl:value-of select = "ape:resource('eag2012.commons.no')"/>
												</xsl:when>
												<xsl:when test="$photo = 'yes'">
													<xsl:value-of select = "ape:resource('eag2012.commons.yes')"/>
												</xsl:when>
												<xsl:when test="$photo = 'depending on the material'">
													<xsl:value-of select = "ape:resource('eag2012.options.photograph.depending')"/>
												</xsl:when>
												<xsl:when test="$photo = 'Yes (without flash)'">
													<xsl:value-of select = "ape:resource('eag2012.options.photograph.without')"/>
												</xsl:when>
												<xsl:otherwise>
													<xsl:text></xsl:text>
												</xsl:otherwise>
											</xsl:choose>
										</div>
									</xsl:for-each>
									</td>
								</tr>
							</xsl:if>
							
							<!-- internetAccess only shown if there are values-->
							<xsl:if test="eag:services/eag:internetAccess">
								<tr>
									<td class="header">
										<xsl:value-of select="ape:resource('eag2012.portal.publicinternetAccess')"/><xsl:text>:</xsl:text>
									</td>
									<td>
										<xsl:choose>
											<xsl:when test="eag:services/eag:internetAccess[@question='yes']">
												<xsl:value-of select="ape:resource('eag2012.portal.available')"/>
											</xsl:when>
											<xsl:otherwise>
												<xsl:value-of select="ape:resource('eag2012.portal.notavailable')"/>
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

							<!-- library only shown if there are values-->
							<xsl:if test="eag:services/eag:library and (eag:services/eag:library[@question='yes']/eag:monographicpub/eag:num/text() != '' or eag:services/eag:library[@question='yes']/eag:serialpub/eag:num/text() != '')">
								<tr>
									<td class="header">
										<xsl:value-of select="ape:resource('eag2012.portal.library')"/><xsl:text>:</xsl:text>
									</td>
									<td>
										<xsl:if test="eag:services/eag:library[@question='yes']/eag:monographicpub/eag:num/text()">
											<xsl:apply-templates select="eag:services/eag:library[@question='yes']/eag:monographicpub/eag:num"/>
										</xsl:if>
										<xsl:if test="eag:services/eag:library[@question='yes']/eag:serialpub/eag:num/text()">
											<xsl:if test="eag:services/eag:library[@question='yes']/eag:monographicpub/eag:num/text()">
												<xsl:text>, </xsl:text>
											</xsl:if>
											<xsl:apply-templates select="eag:services/eag:library[@question='yes']/eag:serialpub/eag:num"/>
										</xsl:if>
										<xsl:if test="eag:services/eag:library[@question='no']">
											<xsl:value-of select="ape:resource('eag2012.portal.nolibrary')"/>
										</xsl:if>
									</td>
								</tr>

								<!-- contact library shown only if there is library-->
								<xsl:if test="eag:services/eag:library/eag:contact and (eag:services/eag:library/eag:contact/eag:telephone/text() or eag:services/eag:library/eag:contact/eag:email/@href != '' or eag:services/eag:library/eag:webpage/@href != '')">
									<tr class="longDisplay">
										<td class="header subInfoHeader" colspan="2">
											<xsl:value-of select="ape:resource('eag2012.portal.librarycontact')"/>
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

							<!-- reproductionser only shown if there are values-->
							<xsl:if test="eag:services/eag:techservices/eag:reproductionser">
								<tr class="longDisplay">
									<td class="header">
										<xsl:value-of select="ape:resource('eag2012.portal.reproductionsservices')"/><xsl:text>:</xsl:text>
									</td>
									<td>
										<xsl:choose>
											<xsl:when test="eag:services/eag:techservices/eag:reproductionser[@question='yes'] and eag:services/eag:techservices/eag:reproductionser[@question='yes']/eag:descriptiveNote/eag:p/text()">
												<xsl:call-template name="multilanguage">
													<xsl:with-param name="list" select="eag:services/eag:techservices/eag:reproductionser/eag:descriptiveNote/eag:p"/>
												</xsl:call-template>
											</xsl:when>
											<xsl:when test="eag:services/eag:techservices/eag:reproductionser[@question='yes']">
												<xsl:value-of select="ape:resource('eag2012.portal.yesreproductionser')"/>
											</xsl:when>
											<xsl:otherwise>
												<xsl:value-of select="ape:resource('eag2012.portal.noreproductionser')"/>
											</xsl:otherwise>
										</xsl:choose>
									</td>
								</tr>
							</xsl:if>

							<!-- digitalser only shown if there are values-->
							<xsl:if test="eag:services/eag:techservices/eag:reproductionser/eag:digitalser">
								<tr class="longDisplay">
									<td class="header">
										<xsl:value-of select="ape:resource('eag2012.portal.digitisationservice')"/><xsl:text>:</xsl:text>
									</td>
									<td>
										<xsl:choose>
											<xsl:when test="eag:services/eag:techservices/eag:reproductionser/eag:digitalser[@question='yes']">
												<xsl:value-of select="ape:resource('eag2012.portal.available')"/>
											</xsl:when>
											<xsl:otherwise>
												<xsl:value-of select="ape:resource('eag2012.portal.notavailable')"/>
											</xsl:otherwise>
										</xsl:choose>
									</td>
								</tr>
							</xsl:if>

							<!-- photocopyser only shown if there are values-->
							<xsl:if test="eag:services/eag:techservices/eag:reproductionser/eag:photocopyser">
								<tr class="longDisplay">
									<td class="header">
										<xsl:value-of select="ape:resource('eag2012.portal.photocopyingservice')"/><xsl:text>:</xsl:text>
									</td>
									<td>
										<xsl:choose>
											<xsl:when test="eag:services/eag:techservices//eag:reproductionser/eag:photocopyser[@question='yes']">
												<xsl:value-of select="ape:resource('eag2012.portal.available')"/>
											</xsl:when>
											<xsl:otherwise>
												<xsl:value-of select="ape:resource('eag2012.portal.notavailable')"/>
											</xsl:otherwise>
										</xsl:choose>
									</td>
								</tr>
							</xsl:if>

							<!-- photographser only shown if there are values-->
							<xsl:if test="eag:services/eag:techservices/eag:reproductionser/eag:photographser">
								<tr class="longDisplay">
									<td class="header">
										<xsl:value-of select="ape:resource('eag2012.portal.photographicservice')"/><xsl:text>:</xsl:text>
									</td>
									<td>
										<xsl:choose>
											<xsl:when test="eag:services/eag:techservices/eag:reproductionser/eag:photographser[@question='yes']">
												<xsl:value-of select="ape:resource('eag2012.portal.available')"/>
											</xsl:when>
											<xsl:otherwise>
												<xsl:value-of select="ape:resource('eag2012.portal.notavailable')"/>
											</xsl:otherwise>
										</xsl:choose>
									</td>
								</tr>
							</xsl:if>

							<!-- microformser only shown if there are values-->
							<xsl:if test="eag:services/eag:techservices/eag:reproductionser/eag:microformser">
								<tr class="longDisplay">
									<td class="header">
										<xsl:value-of select="ape:resource('eag2012.portal.microfilmservice')"/><xsl:text>:</xsl:text>
									</td>
									<td>
										<xsl:choose>
											<xsl:when test="eag:services/eag:techservices/eag:reproductionser/eag:microformser[@question='yes']">
												<xsl:value-of select="ape:resource('eag2012.portal.available')"/>
											</xsl:when>
											<xsl:otherwise>
												<xsl:value-of select="ape:resource('eag2012.portal.notavailable')"/>
											</xsl:otherwise>
										</xsl:choose>
									</td>
								</tr>
							</xsl:if>

							<!-- contact reproductionser only shown if there are values-->
							<xsl:if test="eag:services/eag:techservices/eag:reproductionser/eag:contact and (eag:services/eag:techservices/eag:reproductionser/eag:contact/eag:telephone/text() or eag:services/eag:techservices/eag:reproductionser/eag:contact/eag:email/@href != '' or eag:services/eag:techservices/eag:reproductionser/eag:webpage/@href != '')">
								<tr class="longDisplay">
									<td class="header subInfoHeader" colspan="2">
										<xsl:value-of select="ape:resource('eag2012.portal.reproductionsservicecontact')"/>
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

							<!-- restorationlab only shown if there are values-->
							<xsl:if test="eag:services/eag:techservices/eag:restorationlab">
								<tr class="longDisplay">
									<td class="header">
										<xsl:value-of select="ape:resource('eag2012.portal.conservationlaboratory')"/><xsl:text>:</xsl:text>
									</td>
									<td>
										<xsl:choose>
											<xsl:when test="eag:services/eag:techservices/eag:restorationlab[@question='yes'] and eag:services/eag:techservices/eag:restorationlab[@question='yes']/eag:descriptiveNote/eag:p/text()">
												<xsl:call-template name="multilanguage">
													<xsl:with-param name="list" select="eag:services/eag:techservices/eag:restorationlab/eag:descriptiveNote/eag:p"/>
												</xsl:call-template>
											</xsl:when>
											<xsl:when test="eag:services/eag:techservices/eag:restorationlab[@question='yes']">
												<xsl:value-of select="ape:resource('eag2012.portal.yesrestorationlab')"/>
											</xsl:when>
											<xsl:otherwise>
												<xsl:value-of select="ape:resource('eag2012.portal.norestorationlab')"/>
											</xsl:otherwise>
										</xsl:choose>
									</td>
								</tr>
							</xsl:if>

							<!-- contact restorationlab only shown if there are values-->
							<xsl:if test="eag:services/eag:techservices/eag:restorationlab/eag:contact and eag:services/eag:techservices/eag:restorationlab/eag:contact/eag:telephone/text() and eag:services/eag:techservices/eag:restorationlab/eag:contact/eag:email/@href != '' and eag:services/eag:techservices/eag:restorationlab/eag:webpage/@href != ''">
								<tr class="longDisplay">
									<td class="header subInfoHeader" colspan="2">
										<xsl:value-of select="ape:resource('eag2012.portal.restorationlabcontact')"/>
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

							<!-- refreshment only shown if there are values-->
							<xsl:if test="eag:services/eag:recreationalServices/eag:refreshment/eag:descriptiveNote/eag:p/text()">
								<tr class="longDisplay">
									<td class="header">
										<xsl:value-of select="ape:resource('eag2012.portal.refreshmentarea')"/><xsl:text>:</xsl:text>
									</td>
									<td>
										<xsl:call-template name="multilanguage">
											<xsl:with-param name="list" select="eag:services/eag:recreationalServices/eag:refreshment/eag:descriptiveNote/eag:p"/>
										</xsl:call-template>
									</td>
								</tr>
							</xsl:if>

							<!-- exhibition only shown if there are values-->
							<xsl:if test="eag:services/eag:recreationalServices/eag:exhibition and (eag:services/eag:recreationalServices/eag:exhibition/eag:webpage/@href != '' or eag:services/eag:recreationalServices/eag:exhibition/eag:descriptiveNote/eag:p/text())">
								<tr class="longDisplay">
									<td class="header">
										<xsl:value-of select="ape:resource('eag2012.portal.exhibition')"/><xsl:text>:</xsl:text>
									</td>
									<td>
										<xsl:call-template name="multilanguageRecreationalServices">
											<xsl:with-param name="list" select="eag:services/eag:recreationalServices/eag:exhibition"/>
										</xsl:call-template>
									</td>
								</tr>
							</xsl:if>

							<!-- toursSession only shown if there are values-->
							<xsl:if test="eag:services/eag:recreationalServices/eag:toursSessions and (eag:services/eag:recreationalServices/eag:toursSessions/eag:webpage/@href != '' or eag:services/eag:recreationalServices/eag:toursSessions/eag:descriptiveNote/eag:p/text())">
								<tr class="longDisplay">
									<td class="header">
										<xsl:value-of select="ape:resource('eag2012.portal.guidedtour')"/><xsl:text>:</xsl:text>
									</td>
									<td>
										<xsl:call-template name="multilanguageRecreationalServices">
											<xsl:with-param name="list" select="eag:services/eag:recreationalServices/eag:toursSessions"/>
										</xsl:call-template>
									</td>
								</tr>
							</xsl:if>

							<!-- otherServices only shown if there are values-->
							<xsl:if test="eag:services/eag:recreationalServices/eag:otherServices and (eag:services/eag:recreationalServices/eag:otherServices/eag:webpage/@href != '' or eag:services/eag:recreationalServices/eag:otherServices/eag:descriptiveNote/eag:p/text())">
								<tr class="longDisplay">
									<td class="header">
										<xsl:value-of select="ape:resource('eag2012.portal.otherservices')"/><xsl:text>:</xsl:text>
									</td>
									<td>
										<xsl:call-template name="multilanguageRecreationalServices">
											<xsl:with-param name="list" select="eag:services/eag:recreationalServices/eag:otherServices"/>
										</xsl:call-template>
									</td>
								</tr>
							</xsl:if>
						</tbody>
					</table>

					<xsl:if test="eag:holdings or eag:holdings/eag:descriptiveNote/eag:p/text() or eag:holdings/eag:extent/eag:num/text() or eag:holdings/eag:date/text() or (eag:holdings/eag:dateRange/eag:fromDate/text() and eag:holdings/eag:dateRange/eag:toDate/text()) or eag:holdings/eag:dateSet or eag:holdings/eag:dateSet/eag:date/text() or (eag:holdings/eag:dateSet/eag:dateRange/eag:fromDate/text() and eag:holdings/eag:dateSet/eag:dateRange/eag:toDate/text()) or eag:repositorhist/eag:descriptiveNote/eag:p/text() or eag:repositorfound/eag:date/text() or eag:repositorfound/eag:rule/text() or eag:repositorsup/eag:date/text() or eag:repositorsup/eag:rule/text() or eag:adminhierarchy/eag:adminunit/text() or eag:buildinginfo/eag:building/eag:descriptiveNote/eag:p/text() or eag:buildinginfo/eag:repositorarea/eag:num/text() or eag:buildinginfo/eag:lengthshelf/eag:num/text()">
						<table class="aiSection archivesDisplay">
							<thead>
								<tr>
									<th colspan="2">
										<xsl:value-of select="ape:resource('eag2012.portal.archivesandholdings')"/>
										<!-- Check if occult parts exists -->
										<xsl:if test="eag:holdings/eag:descriptiveNote/eag:p/text() or eag:repositorhist/eag:descriptiveNote/eag:p/text() or eag:repositorfound/eag:date/text() or eag:repositorfound/eag:rule/text() or eag:repositorsup/eag:date/text() or eag:repositorsup/eag:rule/text() or eag:adminhierarchy/eag:adminunit/text() or eag:buildinginfo/eag:building/eag:descriptiveNote/eag:p/text() or eag:buildinginfo/eag:repositorarea/eag:num/text() or eag:buildinginfo/eag:lengthshelf/eag:num/text()">
											<xsl:text> (</xsl:text>
											<a class="displayLinkSeeMore" href="javascript:seeMore('archivesDisplay','{$id}');">
												<xsl:value-of select="ape:resource('eag2012.portal.seemore')"/>
											</a>
											<a class="displayLinkSeeLess" href="javascript:seeLess('archivesDisplay','{$id}');">
												<xsl:value-of select="ape:resource('eag2012.portal.seeless')"/>
											</a>
											<xsl:text>)</xsl:text>
										</xsl:if>
									</th>
								</tr>
							</thead>
							<tbody>
	
								<!-- holdings only shown if there are values-->
								<xsl:if test="eag:holdings/eag:descriptiveNote/eag:p/text()">
									<tr class="longDisplay">
										<td class="header">
											<xsl:value-of select="ape:resource('eag2012.portal.holdings')"/><xsl:text>:</xsl:text>
										</td>
										<td>
											<xsl:call-template name="multilanguage">
												<xsl:with-param name="list" select="eag:holdings/eag:descriptiveNote/eag:p"/>
											</xsl:call-template>
										</td>
									</tr>
								</xsl:if>
	
								<!-- extent only shown if there are values-->
								<xsl:if test="eag:holdings/eag:extent/eag:num/text()">
									<tr>
										<td class="header">
											<xsl:value-of select="ape:resource('eag2012.portal.extentholdings')"/><xsl:text>:</xsl:text>
										</td>
										<td>
											<xsl:apply-templates select="eag:holdings/eag:extent/eag:num"/>
										</td>
									</tr>
								</xsl:if>
	
								<!-- dates of holdings only shown if there are values-->
								<xsl:if test="eag:holdings/eag:date/text() or (eag:holdings/eag:dateRange/eag:fromDate/text() and eag:holdings/eag:dateRange/eag:toDate/text()) or (eag:holdings/eag:dateSet and (eag:holdings/eag:dateSet/eag:date/text() or (eag:holdings/eag:dateSet/eag:dateRange/eag:fromDate/text() and eag:holdings/eag:dateSet/eag:dateRange/eag:toDate/text())))">
									<tr>
										<td class="header">
											<xsl:value-of select="ape:resource('eag2012.portal.datesholdings')"/><xsl:text>:</xsl:text>
										</td>
										<td>
											<!-- when there are only 1 dateSet -->
											<xsl:if test="eag:holdings/eag:dateSet and ((eag:holdings/eag:dateSet/eag:dateRange and eag:holdings/eag:dateSet/eag:dateRange/eag:fromDate/text() and eag:holdings/eag:dateSet/eag:dateRange/eag:toDate/text()) or (eag:holdings/eag:dateSet/eag:date and eag:holdings/eag:dateSet/eag:date/text()))">
												<xsl:apply-templates select="eag:holdings/eag:dateSet"/>
											</xsl:if>
											<!-- when there are only 1 dateRange -->
											<xsl:if test="eag:holdings/eag:dateRange and eag:holdings/eag:dateRange/eag:fromDate/text() and eag:holdings/eag:dateRange/eag:toDate/text()">
												<xsl:apply-templates select="eag:holdings/eag:dateRange"/>
											</xsl:if>
											<!-- when there are only 1 date -->
											<xsl:if test="eag:holdings/eag:date and eag:holdings/eag:date/text()">
												<xsl:apply-templates select="eag:holdings/eag:date"/>
											</xsl:if>
											
										</td>
									</tr>
								</xsl:if>
	
								<!-- repositorhist only shown if there are values-->
								<xsl:if test="eag:repositorhist/eag:descriptiveNote/eag:p/text()">
									<tr class="longDisplay">
										<td class="header">
											<xsl:value-of select="ape:resource('eag2012.portal.historyofthearchives')"/><xsl:text>:</xsl:text>
										</td>
										<td>
											<xsl:call-template name="multilanguage">
												<xsl:with-param name="list" select="eag:repositorhist/eag:descriptiveNote/eag:p"></xsl:with-param>
											</xsl:call-template>
										</td>
									</tr>
								</xsl:if>
	
								<!-- date of repositorfound only shown if there are values-->
								<xsl:if test="eag:repositorfound/eag:date/text()">
									<tr class="longDisplay">
										<td class="subHeader">
											<xsl:value-of select="ape:resource('eag2012.portal.daterepositorfound')"/><xsl:text>:</xsl:text>
										</td>
										<td>
											<xsl:value-of select="eag:repositorfound/eag:date"/>
										</td>
									</tr>
								</xsl:if>
	
								<!-- rule of repositorfound only shown if there are values-->
								<xsl:if test="eag:repositorfound/eag:rule/text()">
									<tr class="longDisplay">
										<td class="subHeader">
											<xsl:value-of select="ape:resource('eag2012.portal.rulerepositorfound')"/><xsl:text>:</xsl:text>
										</td>
										<td>
											<xsl:call-template name="multilanguage">
												<xsl:with-param name="list" select="eag:repositorfound/eag:rule"></xsl:with-param>
											</xsl:call-template>
										</td>
									</tr>
								</xsl:if>
	
								<!-- date of repositorsup only shown if there are values-->
								<xsl:if test="eag:repositorsup/eag:date/text()">
									<tr class="longDisplay">
										<td class="subHeader">
											<xsl:value-of select="ape:resource('eag2012.portal.daterepositorsup')"/><xsl:text>:</xsl:text>
										</td>
										<td>
											<xsl:value-of select="eag:repositorsup/eag:date"/>
										</td>
									</tr>
								</xsl:if>
	
								<!-- rule of repositorsup only shown if there are values-->
								<xsl:if test="eag:repositorsup/eag:rule/text()">
									<tr class="longDisplay">
										<td class="subHeader">
											<xsl:value-of select="ape:resource('eag2012.portal.rulerepositorsup')"/><xsl:text>:</xsl:text>
										</td>
										<td>
											<xsl:call-template name="multilanguage">
												<xsl:with-param name="list" select="eag:repositorsup/eag:rule"></xsl:with-param>
											</xsl:call-template>
										</td>
									</tr>
								</xsl:if>
	
								<!-- adminunit only shown if there are values-->
								<xsl:if test="eag:adminhierarchy/eag:adminunit/text()">
									<tr class="longDisplay">
										<td class="header">
											<xsl:value-of select="ape:resource('eag2012.portal.archivedepartment')"/><xsl:text>:</xsl:text>
										</td>
										<td>
											<xsl:call-template name="multilanguage">
												<xsl:with-param name="list" select="eag:adminhierarchy/eag:adminunit"/>
											</xsl:call-template>
										</td>
									</tr>
								</xsl:if>
	
								<!-- building only shown if there are values-->
								<xsl:if test="eag:buildinginfo/eag:building/eag:descriptiveNote/eag:p/text()">
									<tr class="longDisplay">
										<td class="header">
											<xsl:value-of select="ape:resource('eag2012.portal.archivebuilding')"/><xsl:text>:</xsl:text>
										</td>
										<td>
											<xsl:call-template name="multilanguage">
												<xsl:with-param name="list" select="eag:buildinginfo/eag:building/eag:descriptiveNote/eag:p"/>
											</xsl:call-template>
										</td>
									</tr>
								</xsl:if>
	
								<!-- repositorarea only shown if there are values-->
								<xsl:if test="eag:buildinginfo/eag:repositorarea/eag:num/text()">
									<tr class="longDisplay">
										<td class="header">
											<xsl:value-of select="ape:resource('eag2012.portal.buildingarea')"/><xsl:text>:</xsl:text>
										</td>
										<td>
											<xsl:apply-templates select="eag:buildinginfo/eag:repositorarea/eag:num"/>
										</td>
									</tr>
								</xsl:if>
	
								<!-- lengthshelf only shown if there are values-->
								<xsl:if test="eag:buildinginfo/eag:lengthshelf/eag:num/text()">
									<tr class="longDisplay">
										<td class="header">
											<xsl:value-of select="ape:resource('eag2012.portal.lengthshelfavailable')"/><xsl:text>:</xsl:text>
										</td>
										<td>
											<xsl:apply-templates select="eag:buildinginfo/eag:lengthshelf/eag:num"/>
										</td>
									</tr>
								</xsl:if>
							</tbody>
						</table>
					</xsl:if>
					<!-- END LOOP -->
				</div>
			</div>
		</xsl:for-each>
		<div id="afterRepositories">
		</div>

		<table class="aiSection otherDisplay">
			<thead>
				<tr>
					<th colspan="2">
						<xsl:value-of select="ape:resource('eag2012.portal.other')"/>
						<!-- Check if occult parts exists -->
						<xsl:if test="./eag:eag/eag:archguide/eag:identity/eag:nonpreform and ./eag:eag/eag:archguide/eag:identity/eag:nonpreform/text() and ./eag:eag/eag:archguide/eag:identity/eag:nonpreform/eag:useDates and ((eag:eag/eag:archguide/eag:identity/eag:nonpreform/eag:useDates/eag:date and eag:eag/eag:archguide/eag:identity/eag:nonpreform/eag:useDates/eag:date/text()) or (eag:eag/eag:archguide/eag:identity/eag:nonpreform/eag:useDates/eag:dateRange and eag:eag/eag:archguide/eag:identity/eag:nonpreform/eag:useDates/eag:dateRange/eag:fromDate/text() and eag:eag/eag:archguide/eag:identity/eag:nonpreform/eag:useDates/eag:dateRange/eag:toDate/text()) or (eag:eag/eag:archguide/eag:identity/eag:nonpreform/eag:useDates/eag:dateSet/eag:date and eag:eag/eag:archguide/eag:identity/eag:nonpreform/eag:useDates/eag:dateSet/eag:date/text()) or (eag:eag/eag:archguide/eag:identity/eag:nonpreform/eag:useDates/eag:dateSet/eag:dateRange and eag:eag/eag:archguide/eag:identity/eag:nonpreform/eag:useDates/eag:dateSet/eag:dateRange/eag:fromDate/text() and eag:eag/eag:archguide/eag:identity/eag:nonpreform/eag:useDates/eag:dateSet/eag:dateRange/eag:toDate/text()))">
							<xsl:text> (</xsl:text>
							<a class="displayLinkSeeMore" href="javascript:seeMore('otherDisplay');">
								<xsl:value-of select="ape:resource('eag2012.portal.seemore')"/>
							</a>
							<a class="displayLinkSeeLess" href="javascript:seeLess('otherDisplay');">
								<xsl:value-of select="ape:resource('eag2012.portal.seeless')"/>
							</a>
							<xsl:text>)</xsl:text>
						</xsl:if>
					</th>
				</tr>
			</thead>
			<tbody>

				<!-- relationEntry and links to related resources-->
				<xsl:choose>
					<xsl:when test="(./eag:eag/eag:relations/eag:resourceRelation/@href and ./eag:eag/eag:relations/eag:resourceRelation/@href != '') or (./eag:eag/eag:relations/eag:resourceRelation/eag:relationEntry/text() and ./eag:eag/eag:relations/eag:resourceRelation/eag:relationEntry/text() != '')">
						<tr>
							<td class="header">
								<xsl:value-of select="ape:resource('eag2012.portal.relatedresource')"/>:
							</td>
							<td>
								<xsl:call-template name="multilanguageWithLink">
									<xsl:with-param name="list" select="./eag:eag/eag:relations/eag:resourceRelation/eag:relationEntry"></xsl:with-param>
								</xsl:call-template>
							</td>
						</tr>
					</xsl:when>
					<xsl:otherwise>
						<xsl:if test="./eag:eag/eag:relations/eag:resourceRelation/eag:relationEntry/text()">
							<tr>
								<td class="header">
									<xsl:value-of select="ape:resource('eag2012.portal.relatedresource')"/>
								</td>
								<td>
									<xsl:value-of select="./eag:eag/eag:relations/eag:resourceRelation/eag:relationEntry"/>
								</td>
							</tr>
						</xsl:if>
					</xsl:otherwise>
				</xsl:choose>

				<!-- associatedrepositories only shown if there are values-->
				<xsl:choose>
					<xsl:when test="(./eag:eag/eag:relations/eag:eagRelation/@href and ./eag:eag/eag:relations/eag:eagRelation/@href != '') or (./eag:eag/eag:relations/eag:eagRelation/eag:relationEntry/text() and ./eag:eag/eag:relations/eag:eagRelation/eag:relationEntry/text() != '')">
						<tr>
							<td class="header">
								<xsl:value-of select="ape:resource('eag2012.portal.associatedrepositories')"/>:
							</td>
							<td>
								<xsl:call-template name="multilanguageWithLink">
									<xsl:with-param name="list" select="./eag:eag/eag:relations/eag:eagRelation/eag:relationEntry"></xsl:with-param>
								</xsl:call-template>
							</td>
						</tr>
					</xsl:when>
					<xsl:otherwise>
						<xsl:if test="./eag:eag/eag:relations/eag:eagRelation/eag:relationEntry/text()">
							<tr>
								<td class="header">
									<xsl:value-of select="ape:resource('eag2012.portal.associatedrepositories')"/>:
								</td>
								<td>
									<xsl:value-of select="./eag:eag/eag:relations/eag:eagRelation/eag:relationEntry"/>
								</td>
							</tr>
						</xsl:if>
					</xsl:otherwise>
				</xsl:choose>

				<!-- nonpreform and useDates only shown if there are values-->
				<xsl:if test="./eag:eag/eag:archguide/eag:identity/eag:nonpreform and ./eag:eag/eag:archguide/eag:identity/eag:nonpreform/text() and ./eag:eag/eag:archguide/eag:identity/eag:nonpreform/eag:useDates and ((eag:eag/eag:archguide/eag:identity/eag:nonpreform/eag:useDates/eag:date and eag:eag/eag:archguide/eag:identity/eag:nonpreform/eag:useDates/eag:date/text()) or (eag:eag/eag:archguide/eag:identity/eag:nonpreform/eag:useDates/eag:dateRange and eag:eag/eag:archguide/eag:identity/eag:nonpreform/eag:useDates/eag:dateRange/eag:fromDate/text() and eag:eag/eag:archguide/eag:identity/eag:nonpreform/eag:useDates/eag:dateRange/eag:toDate/text()) or (eag:eag/eag:archguide/eag:identity/eag:nonpreform/eag:useDates/eag:dateSet/eag:date and eag:eag/eag:archguide/eag:identity/eag:nonpreform/eag:useDates/eag:dateSet/eag:date/text()) or (eag:eag/eag:archguide/eag:identity/eag:nonpreform/eag:useDates/eag:dateSet/eag:dateRange and eag:eag/eag:archguide/eag:identity/eag:nonpreform/eag:useDates/eag:dateSet/eag:dateRange/eag:fromDate/text() and eag:eag/eag:archguide/eag:identity/eag:nonpreform/eag:useDates/eag:dateSet/eag:dateRange/eag:toDate/text()))">
					<tr class="longDisplay">
						<td class="header">
							<xsl:value-of select="ape:resource('eag2012.portal.alternative')"/><xsl:text>:</xsl:text>
						</td>
						<td>
							<xsl:call-template name="multilanguageNoperform">
								<xsl:with-param name="list" select="./eag:eag/eag:archguide/eag:identity/eag:nonpreform"/>
							</xsl:call-template>
						</td>
					</tr>
				</xsl:if>

				<!-- repositoryType only shown if there are values-->			
				<xsl:if test="./eag:eag/eag:archguide/eag:identity/eag:repositoryType/text() and (./eag:eag/eag:archguide/eag:identity/eag:repositoryType/text() = 'Business archives' or ./eag:eag/eag:archguide/eag:identity/eag:repositoryType/text() = 'Church and religious archives' or ./eag:eag/eag:archguide/eag:identity/eag:repositoryType/text() = 'County/local authority archives' or ./eag:eag/eag:archguide/eag:identity/eag:repositoryType/text() = 'Specialised non-governmental archives and archives of other cultural (heritage) institutions' or ./eag:eag/eag:archguide/eag:identity/eag:repositoryType/text() = 'Media archives' or ./eag:eag/eag:archguide/eag:identity/eag:repositoryType/text() = 'Municipal archives' or ./eag:eag/eag:archguide/eag:identity/eag:repositoryType/text() = 'National archives' or ./eag:eag/eag:archguide/eag:identity/eag:repositoryType/text() = 'Archives of political parties, of popular/labour movement and other non-governmental organisations, associations, agencies and foundations' or ./eag:eag/eag:archguide/eag:identity/eag:repositoryType/text() = 'Private persons and family archives' or ./eag:eag/eag:archguide/eag:identity/eag:repositoryType/text() = 'Regional archives' or ./eag:eag/eag:archguide/eag:identity/eag:repositoryType/text() = 'Specialised government archives' or ./eag:eag/eag:archguide/eag:identity/eag:repositoryType/text() = 'University and research archives')">
					<tr>
						<td class="header">
							<xsl:value-of select="ape:resource('eag2012.portal.typeofarchive')"/><xsl:text>:</xsl:text>
						</td>
						<td>
							<!-- if there are one or more -->
							<xsl:for-each select="./eag:eag/eag:archguide/eag:identity/eag:repositoryType">
<!-- 								<xsl:if test="not(position() = last())">
									<xsl:text>, </xsl:text>
								</xsl:if> --> 
								<xsl:choose>
									<xsl:when test="current()/text() = 'Business archives'">
										<p>
											<xsl:value-of select = "ape:resource('eag2012.options.institutionType.businessArchives')"/>
										</p>
									</xsl:when>
									<xsl:when test="current()/text() = 'Church and religious archives'">
										<p>
											<xsl:value-of select = "ape:resource('eag2012.options.institutionType.churchArchives')"/>
										</p>
									</xsl:when>
									<xsl:when test="current()/text() = 'County/local authority archives'">
										<p>
											<xsl:value-of select = "ape:resource('eag2012.options.institutionType.countyArchives')"/>
										</p>
									</xsl:when>
									<xsl:when test="current()/text() = 'Specialised non-governmental archives and archives of other cultural (heritage) institutions'">
										<p>
											<xsl:value-of select = "ape:resource('eag2012.options.institutionType.culturalArchives')"/>
										</p>
									</xsl:when>
									<xsl:when test="current()/text() = 'Media archives'">
										<p>	
											<xsl:value-of select = "ape:resource('eag2012.options.institutionType.mediaArchives')"/>
										</p>
									</xsl:when>
									<xsl:when test="current()/text() = 'Municipal archives'">
										<p>	
											<xsl:value-of select = "ape:resource('eag2012.options.institutionType.municipalArchives')"/>
										</p>
									</xsl:when>
									<xsl:when test="current()/text() = 'National archives'">
										<p>	
											<xsl:value-of select = "ape:resource('eag2012.options.institutionType.nationalArchives')"/>
										</p>
									</xsl:when>
									<xsl:when test="current()/text() = 'Archives of political parties, of popular/labour movement and other non-governmental organisations, associations, agencies and foundations'">
										<p>	
											<xsl:value-of select = "ape:resource('eag2012.options.institutionType.politicalArchives')"/>
										</p>
									</xsl:when>
									<xsl:when test="current()/text() = 'Private persons and family archives'">
										<p>	
											<xsl:value-of select = "ape:resource('eag2012.options.institutionType.privateArchives')"/>
										</p>
									</xsl:when>
									<xsl:when test="current()/text() = 'Regional archives'">
										<p>	
											<xsl:value-of select = "ape:resource('eag2012.options.institutionType.regionalArchives')"/>
										</p>
									</xsl:when>
									<xsl:when test="current()/text() = 'Specialised government archives'">
										<p>	
											<xsl:value-of select = "ape:resource('eag2012.options.institutionType.specialisedArchives')"/>
										</p>
									</xsl:when>
									<xsl:when test="current()/text() = 'University and research archives'">
										<p>	
											<xsl:value-of select = "ape:resource('eag2012.options.institutionType.universityArchives')"/>
										</p>
									</xsl:when>
									<xsl:otherwise>
										<xsl:text> </xsl:text>
									</xsl:otherwise>
								</xsl:choose>
							 </xsl:for-each> 
						</td>
					</tr>
				</xsl:if>

				<!-- lastupdate only shown if there are values-->
				<xsl:variable name="numberOfMaintenanceEvent" select="count(./eag:eag/eag:control/eag:maintenanceHistory/eag:maintenanceEvent)"/>
				<xsl:if test="./eag:eag/eag:control/eag:maintenanceHistory/eag:maintenanceEvent[$numberOfMaintenanceEvent]/eag:eventDateTime/text()">
					<tr>
						<td class="header">
							<xsl:value-of select="ape:resource('eag2012.portal.lastupdate')"/><xsl:text>:</xsl:text>
						</td>
						<td>
							<xsl:value-of select="./eag:eag/eag:control/eag:maintenanceHistory/eag:maintenanceEvent[$numberOfMaintenanceEvent]/eag:eventDateTime"/>
						</td>
					</tr>
				</xsl:if>
			</tbody>
		</table>
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
								<xsl:when test="$language.selected = $language.default">
									<xsl:for-each select="$list">
										<xsl:variable name="email" select="current()/@href"></xsl:variable>
										<xsl:variable name="text" select="current()/text()"></xsl:variable>
										<xsl:variable name="currentLang" select="current()/@xml:lang"></xsl:variable>
										<xsl:if test="(not($currentLang) or $currentLang = $language.default) and ($email or $text)">
											<div>
												<xsl:choose>
													<xsl:when test="$email and $email != '' and $text and $text != ''">
														<a href="mailto:{$email}" target="_blank">
															<xsl:choose>
																<xsl:when test="$currentLang = $language.selected or $currentLang = $language.default">
																	<xsl:value-of select="$text"/>
																</xsl:when>
																<xsl:otherwise>
																	<xsl:value-of select="ape:resource('eag2012.portal.contactUsByEmail')"/>
																</xsl:otherwise>
															</xsl:choose>
														</a>
													</xsl:when>
													<xsl:when test="$email and $email != ''">
														<a href="mailto:{$email}" target="_blank">
															<xsl:value-of select="ape:resource('eag2012.portal.contactUsByEmail')"/>
														</a>
													</xsl:when>
													<xsl:when test="$text and $text != ''">
															<xsl:choose>
																<xsl:when test="$currentLang = $language.selected or $currentLang = $language.default">
																	<xsl:value-of select="$text"/>
																</xsl:when>
																<xsl:otherwise>
																	<xsl:value-of select="ape:resource('eag2012.portal.contactUsByEmail')"/>
																</xsl:otherwise>
															</xsl:choose>
													</xsl:when>
												</xsl:choose>
											</div>
										</xsl:if>
									</xsl:for-each>
								</xsl:when>
								<xsl:when test="$list[@xml:lang = $language.selected] and $list[@xml:lang = $language.selected]/text() and $list[@xml:lang = $language.selected]/text() != ''">
									<xsl:for-each select="$list">
										<xsl:variable name="email" select="current()/@href"></xsl:variable>
										<xsl:variable name="text" select="current()/text()"></xsl:variable>
										<xsl:variable name="currentLang" select="current()/@xml:lang"></xsl:variable>
										<xsl:if test="(not($currentLang) or $currentLang = $language.selected) and ($email or $text)">
											<div>
												<xsl:choose>
													<xsl:when test="$email and $email != '' and $text and $text != ''">
														<a href="mailto:{$email}" target="_blank">
															<xsl:choose>
																<xsl:when test="$currentLang = $language.selected or $currentLang = $language.default">
																	<xsl:value-of select="$text"/>
																</xsl:when>
																<xsl:otherwise>
																	<xsl:value-of select="ape:resource('eag2012.portal.contactUsByEmail')"/>
																</xsl:otherwise>
															</xsl:choose>
														</a>
													</xsl:when>
													<xsl:when test="$email and $email != ''">
														<a href="mailto:{$email}" target="_blank">
															<xsl:value-of select="ape:resource('eag2012.portal.contactUsByEmail')"/>
														</a>
													</xsl:when>
													<xsl:when test="$text and $text != ''">
															<xsl:choose>
																<xsl:when test="$currentLang = $language.selected or $currentLang = $language.default">
																	<xsl:value-of select="$text"/>
																</xsl:when>
																<xsl:otherwise>
																	<xsl:value-of select="ape:resource('eag2012.portal.contactUsByEmail')"/>
																</xsl:otherwise>
															</xsl:choose>
													</xsl:when>
												</xsl:choose>
											</div>
										</xsl:if>
									</xsl:for-each>
								</xsl:when>
								<xsl:when test="$list[@xml:lang = $language.default] and $list[@xml:lang = $language.default]/text() and $list[@xml:lang = $language.default]/text() != ''">
									<xsl:for-each select="$list">
										<xsl:variable name="email" select="current()/@href"></xsl:variable>
										<xsl:variable name="text" select="current()/text()"></xsl:variable>
										<xsl:variable name="currentLang" select="current()/@xml:lang"></xsl:variable>
										<xsl:if test="(not($currentLang) or $currentLang = $language.default) and ($email or $text)">
											<div>
												<xsl:choose>
													<xsl:when test="$email and $email != '' and $text and $text != ''">
														<a href="mailto:{$email}" target="_blank">
															<xsl:choose>
																<xsl:when test="$currentLang = $language.selected or $currentLang = $language.default">
																	<xsl:value-of select="$text"/>
																</xsl:when>
																<xsl:otherwise>
																	<xsl:value-of select="ape:resource('eag2012.portal.contactUsByEmail')"/>
																</xsl:otherwise>
															</xsl:choose>
														</a>
													</xsl:when>
													<xsl:when test="$email and $email != ''">
														<a href="mailto:{$email}" target="_blank">
															<xsl:value-of select="ape:resource('eag2012.portal.contactUsByEmail')"/>
														</a>
													</xsl:when>
													<xsl:when test="$text and $text != ''">
															<xsl:choose>
																<xsl:when test="$currentLang = $language.selected or $currentLang = $language.default">
																	<xsl:value-of select="$text"/>
																</xsl:when>
																<xsl:otherwise>
																	<xsl:value-of select="ape:resource('eag2012.portal.contactUsByEmail')"/>
																</xsl:otherwise>
															</xsl:choose>
													</xsl:when>
												</xsl:choose>
											</div>
										</xsl:if>
									</xsl:for-each>
								</xsl:when>
								<xsl:otherwise>
									<xsl:variable name="language.first" select="$list[1]/@xml:lang"></xsl:variable>
									<xsl:for-each select="$list">
										<xsl:variable name="email" select="current()/@href"></xsl:variable>
										<xsl:variable name="text" select="current()/text()"></xsl:variable>
										<xsl:variable name="currentLang" select="current()/@xml:lang"></xsl:variable>
										<xsl:if test="(not($currentLang) or $currentLang = $language.first) and ($email or $text)">
											<div>
												<xsl:choose>
													<xsl:when test="$email and $email != '' and $text and $text != ''">
														<a href="mailto:{$email}" target="_blank">
															<xsl:choose>
																<xsl:when test="$currentLang = $language.first or $currentLang = $language.default">
																	<xsl:value-of select="$text"/>
																</xsl:when>
																<xsl:otherwise>
																	<xsl:value-of select="ape:resource('eag2012.portal.contactUsByEmail')"/>
																</xsl:otherwise>
															</xsl:choose>
														</a>
													</xsl:when>
													<xsl:when test="$email and $email != ''">
														<a href="mailto:{$email}" target="_blank">
															<xsl:value-of select="ape:resource('eag2012.portal.contactUsByEmail')"/>
														</a>
													</xsl:when>
													<xsl:when test="$text and $text != ''">
															<xsl:choose>
																<xsl:when test="$currentLang = $language.first or $currentLang = $language.default">
																	<xsl:value-of select="$text"/>
																</xsl:when>
																<xsl:otherwise>
																	<xsl:value-of select="ape:resource('eag2012.portal.contactUsByEmail')"/>
																</xsl:otherwise>
															</xsl:choose>
													</xsl:when>
												</xsl:choose>
											</div>
										</xsl:if>
									</xsl:for-each>
								</xsl:otherwise>
							</xsl:choose>
						</xsl:when>
						<xsl:otherwise>
							<xsl:for-each select="$list">
								<xsl:variable name="email" select="current()/@href"></xsl:variable>
								<xsl:variable name="text" select="current()/text()"></xsl:variable>
								<xsl:variable name="currentLang" select="current()/@xml:lang"></xsl:variable>
								<div>
									<xsl:choose>
										<xsl:when test="$email and $email != '' and $text and $text != ''">
											<a href="mailto:{$email}" target="_blank">
												<xsl:choose>
													<xsl:when test="$currentLang = $language.default">
														<xsl:value-of select="$text"/>
													</xsl:when>
													<xsl:otherwise>
														<xsl:value-of select="ape:resource('eag2012.portal.contactUsByEmail')"/>
													</xsl:otherwise>
												</xsl:choose>
											</a>
										</xsl:when>
										<xsl:when test="$email and $email != ''">
											<a href="mailto:{$email}" target="_blank">
												<xsl:value-of select="ape:resource('eag2012.portal.contactUsByEmail')"/>
											</a>
										</xsl:when>
										<xsl:when test="$text and $text != ''">
												<xsl:choose>
													<xsl:when test="$currentLang = $language.default">
														<xsl:value-of select="$text"/>
													</xsl:when>
													<xsl:otherwise>
														<xsl:value-of select="ape:resource('eag2012.portal.contactUsByEmail')"/>
													</xsl:otherwise>
												</xsl:choose>
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
		<xsl:if test="$parent/eag:webpage/@href and $parent/eag:webpage/@href != ''">
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
		</xsl:if>
	</xsl:template>

	<!-- template for multilanguage for webpage -->
	<xsl:template name="multilaguageWebpage">
		<xsl:param name="list"/>
		<xsl:choose>
			<xsl:when test="count($list) > 1">
				<xsl:choose>
					<xsl:when test="$language.selected = $language.default">
						<xsl:for-each select="$list">
							<xsl:variable name="link" select="current()/@href"></xsl:variable>
							<xsl:variable name="text" select="current()/text()"></xsl:variable>
							<xsl:variable name="currentLang" select="current()/@xml:lang"></xsl:variable>
							<xsl:if test="(not($currentLang) or $currentLang = $language.default) and ($link or $text)">
								<div>
									<xsl:choose>
										<xsl:when test="$link and $link != '' and $text and $text != ''">
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
										</xsl:when>
										<xsl:when test="$link and $link != ''">
											<a href="{$link}" target="_blank">
												<xsl:value-of select="ape:resource('eag2012.portal.linktorelatedresource')"/>
											</a>
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
								</div>
							</xsl:if>
						</xsl:for-each>
					</xsl:when>
					<xsl:when test="$list[@xml:lang = $language.selected] and $list[@xml:lang = $language.selected]/text() and $list[@xml:lang = $language.selected]/text() != ''">
						<xsl:for-each select="$list">
							<xsl:variable name="link" select="current()/@href"></xsl:variable>
							<xsl:variable name="text" select="current()/text()"></xsl:variable>
							<xsl:variable name="currentLang" select="current()/@xml:lang"></xsl:variable>
							<xsl:if test="(not($currentLang) or $currentLang = $language.selected) and ($link or $text)">
								<div>
									<xsl:choose>
										<xsl:when test="$link and $link != '' and $text and $text != ''">
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
										</xsl:when>
										<xsl:when test="$link and $link != ''">
											<a href="{$link}" target="_blank">
												<xsl:value-of select="ape:resource('eag2012.portal.linktorelatedresource')"/>
											</a>
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
								</div>
							</xsl:if>
						</xsl:for-each>
					</xsl:when>
					<xsl:when test="$list[@xml:lang = $language.default] and $list[@xml:lang = $language.default]/text() and $list[@xml:lang = $language.default]/text() != ''">
						<xsl:for-each select="$list">
							<xsl:variable name="link" select="current()/@href"></xsl:variable>
							<xsl:variable name="text" select="current()/text()"></xsl:variable>
							<xsl:variable name="currentLang" select="current()/@xml:lang"></xsl:variable>
							<xsl:if test="(not($currentLang) or $currentLang = $language.default) and ($link or $text)">
								<div>
									<xsl:choose>
										<xsl:when test="$link and $link != '' and $text and $text != ''">
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
										</xsl:when>
										<xsl:when test="$link and $link != ''">
											<a href="{$link}" target="_blank">
												<xsl:value-of select="ape:resource('eag2012.portal.linktorelatedresource')"/>
											</a>
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
								</div>
							</xsl:if>
						</xsl:for-each>
					</xsl:when>
					<xsl:otherwise>
						<xsl:variable name="language.first" select="$list[1]/@xml:lang"></xsl:variable>
						<xsl:for-each select="$list">
							<xsl:variable name="link" select="current()/@href"></xsl:variable>
							<xsl:variable name="text" select="current()/text()"></xsl:variable>
							<xsl:variable name="currentLang" select="current()/@xml:lang"></xsl:variable>
							<xsl:if test="(not($currentLang) or $currentLang = $language.first) and ($link or $text)">
								<div>
									<xsl:choose>
										<xsl:when test="$link and $link != '' and $text and $text != ''">
											<a href="{$link}" target="_blank">
												<xsl:choose>
													<xsl:when test="$currentLang = $language.first or $currentLang = $language.default">
														<xsl:value-of select="$text"/>
													</xsl:when>
													<xsl:otherwise>
														<xsl:value-of select="ape:resource('eag2012.portal.linktorelatedresource')"/>
													</xsl:otherwise>
												</xsl:choose>
											</a>
										</xsl:when>
										<xsl:when test="$link and $link != ''">
											<a href="{$link}" target="_blank">
												<xsl:value-of select="ape:resource('eag2012.portal.linktorelatedresource')"/>
											</a>
										</xsl:when>
										<xsl:when test="$text and $text != ''">
												<xsl:choose>
													<xsl:when test="$currentLang = $language.first or $currentLang = $language.default">
														<xsl:value-of select="$text"/>
													</xsl:when>
													<xsl:otherwise>
														<xsl:value-of select="ape:resource('eag2012.portal.linktorelatedresource')"/>
													</xsl:otherwise>
												</xsl:choose>
										</xsl:when>
									</xsl:choose>
								</div>
							</xsl:if>
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
									<xsl:choose>
										<xsl:when test="$currentLang = $language.default">
											<xsl:value-of select="$text"/>
										</xsl:when>
										<xsl:otherwise>
											<xsl:value-of select="ape:resource('eag2012.portal.linktorelatedresource')"/>
										</xsl:otherwise>
									</xsl:choose>
								</a>
							</xsl:when>
							<xsl:when test="$link and $link != ''">
								<a href="{$link}" target="_blank">
									<xsl:value-of select="ape:resource('eag2012.portal.linktorelatedresource')"/>
								</a>
							</xsl:when>
							<xsl:when test="$text and $text != ''">
									<xsl:choose>
										<xsl:when test="$currentLang = $language.default">
											<xsl:value-of select="$text"/>
										</xsl:when>
										<xsl:otherwise>
											<xsl:value-of select="ape:resource('eag2012.portal.linktorelatedresource')"/>
										</xsl:otherwise>
									</xsl:choose>
							</xsl:when>
						</xsl:choose>
					</div>
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

	<!-- template for language for visitors and postal address. -->
	<xsl:template name="multilanguageAddress">
		<xsl:param name="list"/>
		<xsl:choose>
			<xsl:when test="count($list) > 1">
				<xsl:choose>
					<xsl:when test="$language.selected = $language.default">
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
					<xsl:otherwise>
						<xsl:variable name="language.first" select="$list[1]/eag:street/@xml:lang"></xsl:variable>
						<xsl:for-each select="$list">
							<xsl:variable name="currentLang" select="current()/eag:street/@xml:lang"></xsl:variable>
							<xsl:variable name="currentStreetText" select="current()/eag:street/text()"></xsl:variable>
							<xsl:variable name="currentMunicipalityText" select="current()/eag:municipalityPostalcode/text()"></xsl:variable>
							<xsl:if test="(not($currentLang) or $currentLang = $language.first) and ($currentStreetText or $currentMunicipalityText)">
								<p>
									<xsl:choose>
										<xsl:when test="not($currentLang)">
											<xsl:if test="current()/eag:street/text()">
												<xsl:value-of select="current()/eag:street/text()"/>
												<xsl:if test="current()/eag:municipalityPostalcode/text()">
													<xsl:text>, </xsl:text>
												</xsl:if>
											</xsl:if>
											<xsl:if test="current()/eag:municipalityPostalcode/text()">
												<xsl:value-of select="current()/eag:municipalityPostalcode/text()"/>
											</xsl:if>
										</xsl:when>
										<xsl:otherwise>
											<xsl:if test="$currentLang = $language.first">
												<xsl:if test="current()/eag:street/text()">
													<xsl:value-of select="current()/eag:street/text()"/>
													<xsl:if test="current()/eag:municipalityPostalcode/text()">
														<xsl:text>, </xsl:text>
													</xsl:if>
												</xsl:if>
												<xsl:if test="current()/eag:municipalityPostalcode/text()">
													<xsl:value-of select="current()/eag:municipalityPostalcode/text()"/>
												</xsl:if>
											</xsl:if>
										</xsl:otherwise>
									</xsl:choose>
								</p>
							</xsl:if>
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
					<xsl:when test="$language.selected = $language.default">
						<xsl:for-each select="$list">
							<xsl:variable name="currentLang" select="current()/@xml:lang"></xsl:variable>
							<xsl:if test="(not($currentLang) or $currentLang = $language.default) and current()/text() and current()/text() != ''">
								<p>
									<xsl:value-of select="."/>
								</p>
							</xsl:if>
						</xsl:for-each>
					</xsl:when>
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
					<xsl:otherwise>
						<xsl:variable name="language.first" select="$list[1]/@xml:lang"></xsl:variable>
						<xsl:for-each select="$list">
							<xsl:variable name="currentLang" select="current()/@xml:lang"></xsl:variable>
							<xsl:choose>
								<xsl:when test="not($currentLang)">
									<p>
										<xsl:value-of select="."/>
									</p>
								</xsl:when>
								<xsl:otherwise>
									<xsl:if test="$currentLang = $language.first">
										<p>
											<xsl:value-of select="."/>
										</p>
									</xsl:if>
								</xsl:otherwise>
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

				<xsl:call-template name="multilanguage">
					<xsl:with-param name="list" select="current()/parent::node()/eag:descriptiveNote/eag:p"></xsl:with-param>
				</xsl:call-template>
			</div>
		</xsl:for-each>
	</xsl:template>

	<!-- template for language nonpreform-->
	<xsl:template name="multilanguageNoperform">
		<xsl:param name="list"/>
			<xsl:choose>
				<xsl:when test="count($list) > 1">
					<xsl:choose>
						<xsl:when test="$language.selected = $language.default">
							<xsl:for-each select="$list">
								<xsl:variable name="currentLang" select="current()/@xml:lang"></xsl:variable>
								<xsl:if test="(not($currentLang) or $currentLang = $language.default) and current()/text() and current()/text() != ''">
									<p>
										<xsl:apply-templates select="."/>
									</p>
								</xsl:if>
							</xsl:for-each>
						</xsl:when>
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
						<xsl:otherwise>
							<xsl:variable name="language.first" select="$list[1]/@xml:lang"></xsl:variable>
							<xsl:for-each select="$list">
								<xsl:variable name="currentLang" select="current()/@xml:lang"></xsl:variable>
								<xsl:choose>
									<xsl:when test="not($currentLang)">
										<p>
											<xsl:apply-templates select="."/>
										</p>
									</xsl:when>
									<xsl:otherwise>
										<xsl:if test="$currentLang = $language.first">
											<p>
												<xsl:apply-templates select="."/>
											</p>
										</xsl:if>
									</xsl:otherwise>
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
								<xsl:when test="$language.selected = $language.default">
									<xsl:for-each select="$list">
										<xsl:variable name="currentLang" select="current()/@xml:lang"></xsl:variable>
										<xsl:if test="(not($currentLang) or $currentLang = $language.default)">
											<p>
												<xsl:apply-templates select="."/>
											</p>
										</xsl:if>
									</xsl:for-each>
								</xsl:when>
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
								<xsl:otherwise>
									<xsl:variable name="language.first" select="$list[1]/@xml:lang"></xsl:variable>
									<xsl:for-each select="$list">
										<xsl:variable name="currentLang" select="current()/@xml:lang"></xsl:variable>
										<xsl:choose>
											<xsl:when test="not($currentLang)">
												<p>
													<xsl:apply-templates select="."/>
												</p>
											</xsl:when>
											<xsl:otherwise>
												<xsl:if test="$currentLang = $language.first">
													<p>
														<xsl:apply-templates select="."/>
													</p>
												</xsl:if>
											</xsl:otherwise>
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

	<!-- template for language exhibition, toursSessions and otherServices -->
	<xsl:template name="multilanguageRecreationalServices">
		<xsl:param name="list"/>
		<xsl:for-each select="$list">
			<div>
				<xsl:variable name="descriptionList" select="current()/eag:descriptiveNote/eag:p"></xsl:variable>
				<xsl:variable name="webList" select="current()/eag:webpage"></xsl:variable>
				<xsl:call-template name="multilanguage">
					<xsl:with-param name="list" select="$descriptionList"/>
				</xsl:call-template>
				<xsl:call-template name="multilaguageWebpage">
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
		<xsl:value-of select="text()"/>
		<xsl:for-each select="eag:citation">
			<p>
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
