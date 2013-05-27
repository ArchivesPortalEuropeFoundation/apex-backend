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
						<xsl:value-of select="./eag:repositoryName"></xsl:value-of>
					</h3>
				</xsl:if>
				<div class="repositoryInfo">
					<table class="aiSection contactDisplay">
						<thead>
							<tr>
								<th colspan="2">
									<xsl:value-of select="ape:resource('eagcontent.contact')"/>
									<!-- Check if occult parts exists -->
									<xsl:if test="eag:location[not(@localType) or @localType='visitors address']/eag:localentity/text() or eag:location[not(@localType) or @localType='visitors address']/eag:secondem/text() or eag:location/eag:firstdem/text() or eag:location[@localType='postal address'] or eag:fax and eag:fax/text() != ''">
										<xsl:text> (</xsl:text>
										<a class="displayLinkSeeMore" href="javascript:seeMore('contactDisplay','{$id}');">
											<xsl:value-of select="ape:resource('eagcontent.seemore')"/>
										</a>
										<a class="displayLinkSeeLess" href="javascript:seeLess('contactDisplay','{$id}');">
											<xsl:value-of select="ape:resource('eagcontent.seeless')"/>
										</a>
										<xsl:text>)</xsl:text>
									</xsl:if>
								</th>
							</tr>
						</thead>
						<tbody>

							<!-- visitors Address -->
							<xsl:if
								test="eag:location[not(@localType) or @localType='visitors address']">
								<tr>
									<td class="header">
										<xsl:value-of select="ape:resource('eagcontent.visitorsaddress')"/>
									</td>
									<td class="address">
										<xsl:if test="eag:location[not(@localType) or @localType='visitors address']/eag:street/text()">
											<xsl:value-of select="eag:location[not(@localType) or @localType='visitors address']/eag:street"/>
											<xsl:if test="eag:location[not(@localType) or @localType='visitors address']/eag:municipalityPostalcode/text()">
												<xsl:text>, </xsl:text>
											</xsl:if>
										</xsl:if>
										<xsl:if test="eag:location[not(@localType) or @localType='visitors address']/eag:municipalityPostalcode/text()">
											<xsl:value-of select="eag:location[not(@localType) or @localType='visitors address']/eag:municipalityPostalcode"/>
										</xsl:if>
									</td>
								</tr>
							</xsl:if>

							<!-- localentity -->
							<xsl:if test="eag:location[not(@localType) or @localType='visitors address']/eag:localentity/text()">
								<tr class="longDisplay">
									<td class="header">
										<xsl:value-of select="ape:resource('eagcontent.district')"/>
									</td>
									<td>
										<xsl:value-of select="eag:location[not(@localType) or @localType='visitors address']/eag:localentity"/>
									</td>
								</tr>
							</xsl:if>

							<!-- secondem -->
							<xsl:if test="eag:location[not(@localType) or @localType='visitors address']/eag:secondem/text()">
								<tr class="longDisplay">
									<td class="header">
										<xsl:value-of select="ape:resource('eagcontent.countrylocalauthority')"/>
									</td>
									<td>
										<xsl:value-of select="eag:location[not(@localType) or @localType='visitors address']/eag:secondem"></xsl:value-of>
									</td>
								</tr>
							</xsl:if>

							<!-- firstdem -->
							<xsl:if test="eag:location/eag:firstdem/text()">
								<tr class="longDisplay">
									<td class="header">
										<xsl:value-of select="ape:resource('eagcontent.regionautonomousauthority')"/>
									</td>
									<td>
										<xsl:value-of select="eag:location/eag:firstdem"></xsl:value-of>
									</td>
								</tr>
							</xsl:if>

							<!-- postal address -->
							<xsl:if test="eag:location[@localType='postal address']">
								<tr class="longDisplay">
									<td class="header">
										<xsl:value-of select="ape:resource('eagcontent.postaladdress')"></xsl:value-of>
									</td>
									<td class="postalAddress">
										<xsl:if test="eag:location[@localType='postal address']/eag:street/text()">
											<xsl:value-of select="eag:location[@localType='postal address']/eag:street"/>
											<xsl:if test="eag:location[@localType='postal address']/eag:municipalityPostalcode/text()">
												<xsl:text>, </xsl:text>
											</xsl:if>
										</xsl:if>
										<xsl:if test="eag:location[@localType='postal address']/eag:municipalityPostalcode/text()">
											<xsl:value-of select="eag:location[@localType='postal address']/eag:municipalityPostalcode"/>
										</xsl:if>
									</td>
								</tr>
							</xsl:if>

							<!-- country -->
							<xsl:if test="eag:location[not(@localType) or @localType='visitors address']/eag:country/text()">
								<tr>
									<td class="header">
										<xsl:value-of select="ape:resource('eagcontent.country')"/>
									</td>
									<td>
										<xsl:value-of select="eag:location[not(@localType) or @localType='visitors address']/eag:country"/>
									</td>
								</tr>
							</xsl:if>
							<xsl:call-template name="email">
								<xsl:with-param name="class" select="'header'"/>
							</xsl:call-template>
							<xsl:call-template name="webpage">
								<xsl:with-param name="class" select="'header'"/>
							</xsl:call-template>
							<xsl:call-template name="telephone">
								<xsl:with-param name="class" select="'header'"/>
							</xsl:call-template>

							<!-- fax -->
							<xsl:if test="eag:fax and eag:fax/text() != ''">
								<tr class="longDisplay">
									<td class="header">
										<xsl:value-of select="ape:resource('eagcontent.fax')"/>
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

							<!-- repositoryType -->
							<xsl:if test="/eag:eag/eag:archguide/eag:identity/eag:repositoryType/text()">
								<tr>
									<td class="header">
										<xsl:value-of select="ape:resource('eagcontent.typeofarchive')"/>
									</td>
									<td>
										<xsl:for-each select="/eag:eag/eag:archguide/eag:identity/eag:repositoryType">
											<div>
												<xsl:value-of select="."/>
											</div>
										</xsl:for-each>
									</td>
								</tr>
							</xsl:if>

							<!-- roleofthearchive -->
							<!-- <xsl:if test= "$repository = 'Head quarter'"> -->
							<xsl:if test="eag:repositoryRole and eag:repositoryRole/text() != ''">
								<tr>
									<td class="header">
										<xsl:value-of select="ape:resource('eagcontent.roleofthearchive')"/>
									</td>
									<td>
										<xsl:value-of select="eag:repositoryRole"/>
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
									<xsl:value-of select="ape:resource('eagcontent.accessandserviceinfo')"/>
									<xsl:text> (</xsl:text>
									<a class="displayLinkSeeMore" href="javascript:seeMore('accessDisplay','{$id}');">
										<xsl:value-of select="ape:resource('eagcontent.seemore')"/>
									</a>
									<a class="displayLinkSeeLess" href="javascript:seeLess('accessDisplay','{$id}');">
										<xsl:value-of select="ape:resource('eagcontent.seeless')"/>
									</a>
									<xsl:text>)</xsl:text>
								</th>
							</tr>
						</thead>
						<tbody>

							<!-- opening -->
							<xsl:if test="eag:timetable/eag:opening/text()">
								<tr>
									<td class="header">
										<xsl:value-of select="ape:resource('eagcontent.openinghours')"></xsl:value-of>
									</td>
									<td>
										<xsl:value-of select="eag:timetable/eag:opening"/>
									</td>
								</tr>
							</xsl:if>

							<!-- closing -->
							<xsl:if test="eag:timetable/eag:closing/text()">
								<tr>
									<td class="header">
										<xsl:value-of select="ape:resource('eagcontent.closingdates')"></xsl:value-of>
									</td>
									<td>
										<xsl:value-of select="eag:timetable/eag:closing"/>
									</td>
								</tr>
							</xsl:if>

							<!-- directions and citations -->
							<xsl:if test="eag:directions">
								<xsl:call-template name="multilanguageWithChilds">
									<xsl:with-param name="title">
										<xsl:value-of select="ape:resource('eagcontent.directions')"/>
									</xsl:with-param>
									<xsl:with-param name="trClass" select="'longDisplay'"/>
									<xsl:with-param name="list" select="eag:directions"/>
								</xsl:call-template>
							</xsl:if>


							<!-- accessconditions -->
							<xsl:if test="eag:access">
								<tr>
									<td class="header">
										<xsl:value-of select="ape:resource('eagcontent.accessconditions')"/>
									</td>
									<td>
										<xsl:choose>
											<xsl:when test="eag:access[@question='yes']">
												<xsl:value-of select="ape:resource('eagcontent.accesspublic')"/>
											</xsl:when>
											<xsl:otherwise>
												<xsl:value-of select="ape:resource('eagcontent.accesspermission')"/>
											</xsl:otherwise>
										</xsl:choose>
									</td>
								</tr>
							</xsl:if>

							<!-- restaccess -->
							<xsl:if test="eag:access/eag:restaccess/text()">
								<tr>
									<td class="header">
										<xsl:value-of select="ape:resource('eagcontent.accessinformation')"/>
									</td>
									<td>
										<xsl:call-template name="multilanguage">
											<xsl:with-param name="list" select="eag:access/eag:restaccess"></xsl:with-param>
										</xsl:call-template>
									</td>
								</tr>
							</xsl:if>

							<!-- accessibility -->
							<xsl:if test="eag:accessibility">
								<tr class="longDisplay">
									<td class="header">
										<xsl:value-of select="ape:resource('eagcontent.disabledaccess')"/>
									</td>
									<td>
										<xsl:choose>
											<xsl:when test="eag:accessibility[@question='yes']">
												<xsl:value-of select="ape:resource('eagcontent.facilities')"/>
											</xsl:when>
											<xsl:otherwise>
												<xsl:value-of select="ape:resource('eagcontent.nofacilities')"/>
											</xsl:otherwise>
										</xsl:choose>
									</td>
								</tr>
							</xsl:if>
							<xsl:if test="eag:accessibility/text()">
								<tr title="repository">
									<td class="header">
										<xsl:value-of
											select="ape:resource('eagcontent.facilitiesfordisabledpersons')"/>
									</td>
									<td>
										<xsl:value-of select="eag:accessibility"/>
									</td>
								</tr>
							</xsl:if>

							<!-- termsOfUse -->
							<xsl:if test="eag:access/eag:termsOfUse">
								<xsl:call-template name="multilanguageWithChilds">
									<xsl:with-param name="title">
										<xsl:value-of select="ape:resource('eagcontent.termsofuse')"/><xsl:text>:</xsl:text>
									</xsl:with-param>
									<xsl:with-param name="list" select="eag:access/eag:termsOfUse"/>
									<xsl:with-param name="trClass" select="'longDisplay'"/>
								</xsl:call-template>
							</xsl:if>

							<!-- readersTicket -->
							<xsl:if test="eag:services/eag:searchroom/eag:readersTicket">
								<xsl:call-template name="multilanguageWithChilds">
									<xsl:with-param name="title">
										<xsl:value-of select="ape:resource('eagcontent.readersticket')"/>
									</xsl:with-param>
									<xsl:with-param name="list" select="eag:services/eag:searchroom/eag:readersTicket"/>
								</xsl:call-template>
							</xsl:if>

							<!-- searchroom -->
							<xsl:if
								test="eag:services/eag:searchroom/eag:workPlaces/eag:num/text()">
								<tr>
									<td class="header">
										<xsl:value-of select="ape:resource('eagcontent.searchroom')"/>
									</td>
									<td>
										<xsl:apply-templates select="eag:services/eag:searchroom/eag:workPlaces/eag:num"/>
									</td>
								</tr>
							</xsl:if>

							<!-- advancedOrders -->
							<xsl:if test="eag:services/eag:searchroom/eag:advancedOrders">
								<xsl:call-template name="multilanguageWithChilds">
									<xsl:with-param name="title">
										<xsl:value-of select="ape:resource('eagcontent.orderingdocuments')"/>
									</xsl:with-param>
									<xsl:with-param name="list" select="eag:services/eag:searchroom/eag:advancedOrders"/>
								</xsl:call-template>
							</xsl:if>

							<!-- contact searchroom -->
							<xsl:if test="eag:services/eag:searchroom/eag:contact">
								<tr class="longDisplay">
									<td class="header subInfoHeader" colspan="2">
										<xsl:value-of select="ape:resource('eagcontent.searchroomcontact')"/>
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

							<!-- researchServices -->
							<xsl:if
								test="eag:services/eag:searchroom/eag:researchServices/eag:descriptiveNote/eag:p/text()">
								<tr class="longDisplay">
									<td class="header">
										<xsl:value-of select="ape:resource('eagcontent.archivesresearchservice')"/>
									</td>
									<td>
										<xsl:call-template name="multilanguage">
											<xsl:with-param name="list" select="eag:services/eag:searchroom/eag:researchServices/eag:descriptiveNote/eag:p"/>
										</xsl:call-template>
									</td>
								</tr>
							</xsl:if>

							<!-- computerPlaces -->
							<xsl:if test="eag:services/eag:searchroom/eag:computerPlaces">
								<tr class="longDisplay">
									<td class="header">
										<xsl:value-of select="ape:resource('eagcontent.computerplaces')"/>
									</td>
									<td>
										<xsl:apply-templates select="eag:services/eag:searchroom/eag:computerPlaces/eag:num"/>
										<xsl:call-template name="multilanguage">
											<xsl:with-param name="list" select="eag:services/eag:searchroom/eag:computerPlaces/eag:descriptiveNote/eag:p"/>
										</xsl:call-template>
									</td>
								</tr>
							</xsl:if>

							<!-- microfilmPlaces -->
							<xsl:if
								test="eag:services/eag:searchroom/eag:microfilmPlaces/eag:num/text()">
								<tr class="longDisplay">
									<td class="header">
										<xsl:value-of select="ape:resource('eagcontent.microfilmplaces')"/>
									</td>
									<td>
										<xsl:apply-templates select="eag:services/eag:searchroom/eag:microfilmPlaces/eag:num"/>
									</td>
								</tr>
							</xsl:if>

							<!-- photographAllowance -->
							<xsl:if
								test="eag:services/eag:searchroom/eag:photographAllowance/text()">
								<tr class="longDisplay">
									<td class="header">
										<xsl:value-of select="ape:resource('eagcontent.photographAllowance')"/>
									</td>
									<td>
										<xsl:value-of select="eag:services/eag:searchroom/eag:photographAllowance"/>
									</td>
								</tr>
							</xsl:if>

							<!-- internetAccess -->
							<xsl:if test="eag:services/eag:internetAccess">
								<tr>
									<td class="header">
										<xsl:value-of select="ape:resource('eagcontent.publicinternetAccess')"/>
									</td>
									<td>
										<xsl:choose>
											<xsl:when test="eag:services/eag:internetAccess[@question='yes']">
												<xsl:value-of select="ape:resource('eagcontent.available')"/>
											</xsl:when>
											<xsl:otherwise>
												<xsl:value-of select="ape:resource('eagcontent.notavailable')"/>
											</xsl:otherwise>
										</xsl:choose>
									</td>
								</tr>

								<xsl:if
									test="eag:services/eag:internetAccess[@question='yes']/eag:descriptiveNote/eag:p/text()">
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

							<!-- library -->
							<xsl:if test="eag:services/eag:library and (eag:services/eag:library[@question='yes']/eag:monographicpub/eag:num/text() != '' or eag:services/eag:library[@question='yes']/eag:serialpub/eag:num/text() != '')">
								<tr>
									<td class="header">
										<xsl:value-of select="ape:resource('eagcontent.library')"/>
									</td>
									<td>
										<xsl:apply-templates select="eag:services/eag:library[@question='yes']/eag:monographicpub/eag:num"/>
										<xsl:if test="eag:services/eag:library[@question='yes']/eag:serialpub/eag:num/text()">
											<xsl:if test="eag:services/eag:library[@question='yes']/eag:monographicpub/eag:num/text()">
												<xsl:text>, </xsl:text>
											</xsl:if>
											<xsl:apply-templates select="eag:services/eag:library[@question='yes']/eag:serialpub/eag:num"/>
										</xsl:if>
										<xsl:if test="eag:services/eag:library[@question='no']">
											<xsl:value-of select="ape:resource('eagcontent.nolibrary')"/>
										</xsl:if>
									</td>
								</tr>

								<!-- contact library -->
								<xsl:if test="eag:services/eag:library/eag:contact">
									<tr class="longDisplay">
										<td class="header subInfoHeader" colspan="2">
											<xsl:value-of select="ape:resource('eagcontent.librarycontact')"/>
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

							<!-- reproductionser -->
							<xsl:if test="eag:services/eag:techservices/eag:reproductionser">
								<tr class="longDisplay">
									<td class="header">
										<xsl:value-of select="ape:resource('eagcontent.reproductionsservices')"/>
									</td>
									<td>
										<xsl:choose>
											<xsl:when test="eag:services/eag:techservices/eag:reproductionser[@question='yes']">
												<xsl:call-template name="multilanguage">
													<xsl:with-param name="list" select="eag:services/eag:techservices/eag:reproductionser/eag:descriptiveNote/eag:p"/>
												</xsl:call-template>
											</xsl:when>
											<xsl:otherwise>
												<xsl:value-of select="ape:resource('eagcontent.noreproductionser')"/>
											</xsl:otherwise>
										</xsl:choose>
									</td>
								</tr>
							</xsl:if>

							<!-- digitalser -->
							<xsl:if
								test="eag:services/eag:techservices/eag:reproductionser/eag:digitalser">
								<tr class="longDisplay">
									<td class="header">
										<xsl:value-of select="ape:resource('eagcontent.digitisationservice')"/>
									</td>
									<td>
										<xsl:choose>
											<xsl:when test="eag:services/eag:techservices/eag:reproductionser/eag:digitalser[@question='yes']">
												<xsl:value-of select="ape:resource('eagcontent.available')"/>
											</xsl:when>
											<xsl:otherwise>
												<xsl:value-of select="ape:resource('eagcontent.notavailable')"/>
											</xsl:otherwise>
										</xsl:choose>
									</td>
								</tr>
							</xsl:if>

							<!-- photocopyser -->
							<xsl:if
								test="eag:services/eag:techservices/eag:reproductionser/eag:photocopyser">
								<tr class="longDisplay">
									<td class="header">
										<xsl:value-of select="ape:resource('eagcontent.photocopyingservice')"/>
									</td>
									<td>
										<xsl:choose>
											<xsl:when test="eag:services/eag:techservices//eag:reproductionser/eag:photocopyser[@question='yes']">
												<xsl:value-of select="ape:resource('eagcontent.available')"/>
											</xsl:when>
											<xsl:otherwise>
												<xsl:value-of select="ape:resource('eagcontent.notavailable')"/>
											</xsl:otherwise>
										</xsl:choose>
									</td>
								</tr>
							</xsl:if>

							<!-- photographser -->
							<xsl:if
								test="eag:services/eag:techservices/eag:reproductionser/eag:photographser">
								<tr class="longDisplay">
									<td class="header">
										<xsl:value-of select="ape:resource('eagcontent.photographicservice')"/>
									</td>
									<td>
										<xsl:choose>
											<xsl:when test="eag:services/eag:techservices/eag:reproductionser/eag:photographser[@question='yes']">
												<xsl:value-of select="ape:resource('eagcontent.available')"/>
											</xsl:when>
											<xsl:otherwise>
												<xsl:value-of select="ape:resource('eagcontent.notavailable')"/>
											</xsl:otherwise>
										</xsl:choose>
									</td>
								</tr>
							</xsl:if>

							<!-- microformser -->
							<xsl:if
								test="eag:services/eag:techservices/eag:reproductionser/eag:microformser">
								<tr class="longDisplay">
									<td class="header">
										<xsl:value-of select="ape:resource('eagcontent.microfilmservice')"/>
									</td>
									<td>
										<xsl:choose>
											<xsl:when test="eag:services/eag:techservices/eag:reproductionser/eag:microformser[@question='yes']">
												<xsl:value-of select="ape:resource('eagcontent.available')"/>
											</xsl:when>
											<xsl:otherwise>
												<xsl:value-of select="ape:resource('eagcontent.notavailable')"/>
											</xsl:otherwise>
										</xsl:choose>
									</td>
								</tr>
							</xsl:if>

							<!-- contact reproductionser -->
							<xsl:if
								test="eag:services/eag:techservices/eag:reproductionser/eag:contact">
								<tr class="longDisplay">
									<td class="header subInfoHeader" colspan="2">
										<xsl:value-of select="ape:resource('eagcontent.reproductionsservicecontact')"/>
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

							<!-- restorationlab -->
							<xsl:if test="eag:services/eag:techservices/eag:restorationlab">
								<tr class="longDisplay">
									<td class="header">
										<xsl:value-of select="ape:resource('eagcontent.conservationlaboratory')"/>
									</td>
									<td>
										<xsl:choose>
											<xsl:when test="eag:services/eag:techservices/eag:restorationlab[@question='yes']">
												<xsl:call-template name="multilanguage">
													<xsl:with-param name="list" select="eag:services/eag:techservices/eag:restorationlab/eag:descriptiveNote/eag:p"/>
												</xsl:call-template>
											</xsl:when>
											<xsl:otherwise>
												<xsl:value-of select="ape:resource('eagcontent.norestorationlab')"/>
											</xsl:otherwise>
										</xsl:choose>
									</td>
								</tr>
							</xsl:if>

							<!-- contact restorationlab -->
							<xsl:if
								test="eag:services/eag:techservices/eag:restorationlab/eag:contact">
								<tr class="longDisplay">
									<td class="header subInfoHeader" colspan="2">
										<xsl:value-of select="ape:resource('eagcontent.restorationlabcontact')"/>
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

							<!-- refreshment -->
							<xsl:if test="eag:services/eag:recreationalServices/eag:refreshment">
								<tr class="longDisplay">
									<td class="header">
										<xsl:value-of select="ape:resource('eagcontent.refreshmentarea')"/>
									</td>
									<td>
										<xsl:call-template name="multilanguage">
											<xsl:with-param name="list" select="eag:services/eag:recreationalServices/eag:refreshment/eag:descriptiveNote/eag:p"/>
										</xsl:call-template>
									</td>
								</tr>
							</xsl:if>

							<!-- exhibition -->
							<xsl:if test="eag:services/eag:recreationalServices/eag:exhibition">
								<tr class="longDisplay">
									<td class="header">
										<xsl:value-of select="ape:resource('eagcontent.exhibition')"/>
									</td>
									<td>
										<xsl:call-template name="multilanguage">
											<xsl:with-param name="list" select="eag:services/eag:recreationalServices/eag:exhibition/eag:descriptiveNote/eag:p"/>
										</xsl:call-template>
										<xsl:for-each select="eag:services/eag:recreationalServices/eag:toursSessions/eag:webpage">
											<xsl:variable name="webpage" select="@href"/>
											<div>
												<a href="{$webpage}" target="_blank">
													<xsl:value-of select="."/>
												</a>
											</div>
										</xsl:for-each>
									</td>
								</tr>
							</xsl:if>

							<!-- toursSession -->
							<xsl:if test="eag:services/eag:recreationalservices/eag:toursSessions">
								<tr class="longDisplay">
									<td class="header">
										<xsl:value-of select="ape:resource('eagcontent.guidedtour')"/>
									</td>
									<td>
										<xsl:call-template name="multilanguage">
											<xsl:with-param name="list" select="eag:services/eag:recreationalServices/eag:toursSessions/eag:descriptiveNote/eag:p"/>
										</xsl:call-template>
										<xsl:for-each select="eag:services/eag:recreationalServices/eag:toursSessions/eag:webpage">
											<xsl:variable name="webpage" select="@href"/>
											<div>
												<a href="{$webpage}" target="_blank">
													<xsl:value-of select="."/>
												</a>
											</div>
										</xsl:for-each>
									</td>
								</tr>
							</xsl:if>

							<!-- otherServices -->
							<xsl:if test="eag:services/eag:recreationalServices/eag:otherServices">
								<tr class="longDisplay">
									<td class="header">
										<xsl:value-of select="ape:resource('eagcontent.otherservices')"/>
									</td>
									<td>
										<xsl:call-template name="multilanguage">
											<xsl:with-param name="list" select="eag:services/eag:recreationalServices/eag:otherServices/eag:descriptiveNote/eag:p"/>
										</xsl:call-template>
										<xsl:for-each select="eag:services/eag:recreationalServices/eag:otherServices/eag:webpage">
											<xsl:variable name="webpage" select="@href"/>
											<div>
												<a href="{$webpage}" target="_blank">
													<xsl:value-of select="."/>
												</a>
											</div>
										</xsl:for-each>
									</td>
								</tr>
							</xsl:if>
							<xsl:if test="count(current()/parent::node()/eag:repository)> 1">
								<tr>
									<td class="header">
										<xsl:value-of select="ape:resource('eagcontent.associatedrepositories')"/>
									</td>
									<td>
										<xsl:for-each select="current()/parent::node()/eag:repository">
											<xsl:variable name="otherRepositoryId" select="position()"/>
											<xsl:if test="$id != $otherRepositoryId">
												<div>
													<a href="javascript:displayRepository('{$otherRepositoryId}');">
														<xsl:value-of select="./eag:repositoryName"/>
														<xsl:text> (</xsl:text>
														<xsl:value-of select="./eag:repositoryRole"/>
														<xsl:text>)</xsl:text>
													</a>
												</div>
											</xsl:if>
										</xsl:for-each>
									</td>
								</tr>
							</xsl:if>
						</tbody>
					</table>

					<table class="aiSection archivesDisplay">
						<thead>
							<tr>
								<th colspan="2">
									<xsl:value-of select="ape:resource('eagcontent.archivesandholdings')"/>
									<!-- Check if occult parts exists -->
									<xsl:if test="eag:holdings/eag:descriptiveNote/eag:p/text() or eag:repositorhist/eag:descriptiveNote/eag:p/text() or eag:repositorfound/eag:date/text() or eag:repositorfound/eag:rule/text() or ./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:repositorsup/eag:date/text() or eag:repositorsup/eag:rule/text() or eag:adminhierarchy/eag:adminunit/text() or eag:buildinginfo/eag:building/eag:descriptiveNote/eag:p/text() or eag:buildinginfo/eag:repositorarea/eag:num/text() or eag:buildinginfo/eag:lengthshelf/eag:num/text()">
										<xsl:text> (</xsl:text>
										<a class="displayLinkSeeMore" href="javascript:seeMore('archivesDisplay','{$id}');">
											<xsl:value-of select="ape:resource('eagcontent.seemore')"/>
										</a>
										<a class="displayLinkSeeLess" href="javascript:seeLess('archivesDisplay','{$id}');">
											<xsl:value-of select="ape:resource('eagcontent.seeless')"/>
										</a>
										<xsl:text>)</xsl:text>
									</xsl:if>
								</th>
							</tr>
						</thead>
						<tbody>

							<!-- holdings -->
							<xsl:if test="eag:holdings/eag:descriptiveNote/eag:p/text()">
								<tr class="longDisplay">
									<td class="header">
										<xsl:value-of select="ape:resource('eagcontent.holdings')"/>
									</td>
									<td>
										<xsl:call-template name="multilanguage">
											<xsl:with-param name="list" select="eag:holdings/eag:descriptiveNote/eag:p"/>
										</xsl:call-template>
									</td>
								</tr>
							</xsl:if>

							<!-- extent -->
							<xsl:if test="eag:holdings/eag:extent/eag:num/text()">
								<tr>
									<td class="header">
										<xsl:value-of select="ape:resource('eagcontent.extentholdings')"/>
									</td>
									<td>
										<xsl:apply-templates select="eag:holdings/eag:extent/eag:num"/>
									</td>
								</tr>
							</xsl:if>

							<!-- dates of holdings -->
							<xsl:if test="eag:holdings/eag:dateSet">
								<tr>
									<td class="header">
										<xsl:value-of select="ape:resource('eagcontent.datesholdings')"/>
									</td>
									<td>
										<xsl:apply-templates select="eag:holdings/eag:dateSet"/>
									</td>
								</tr>
							</xsl:if>

							<!-- repositorhist -->
							<xsl:if test="eag:repositorhist/eag:descriptiveNote/eag:p/text()">
								<tr class="longDisplay">
									<td class="header">
										<xsl:value-of select="ape:resource('eagcontent.historyofthearchives')"/>
									</td>
									<td>
										<xsl:call-template name="multilanguage">
											<xsl:with-param name="list" select="eag:repositorhist/eag:descriptiveNote/eag:p"></xsl:with-param>
										</xsl:call-template>
									</td>
								</tr>
							</xsl:if>

							<!-- date of repositorfound -->
							<xsl:if test="eag:repositorfound/eag:date/text()">
								<tr class="longDisplay">
									<td class="subHeader">
										<xsl:value-of select="ape:resource('eagcontent.daterepositorfound')"/>
									</td>
									<td>
										<xsl:value-of select="eag:repositorfound/eag:date"/>
									</td>
								</tr>
							</xsl:if>

							<!-- rule of repositorfound -->
							<xsl:if test="eag:repositorfound/eag:rule/text()">
								<tr class="longDisplay">
									<td class="subHeader">
										<xsl:value-of select="ape:resource('eagcontent.rulerepositorfound')"/>
									</td>
									<td>
										<xsl:value-of select="eag:repositorfound/eag:rule"/>
									</td>
								</tr>
							</xsl:if>

							<!-- date of repositorsup -->
							<xsl:if
								test="./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:repositorsup/eag:date/text()">
								<tr class="longDisplay">
									<td class="subHeader">
										<xsl:value-of select="ape:resource('eagcontent.daterepositorsup')"/>
									</td>
									<td>
										<xsl:value-of
											select="./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository/eag:repositorsup/eag:date"/>
									</td>
								</tr>
							</xsl:if>

							<!-- rule of repositorsup -->
							<xsl:if test="eag:repositorsup/eag:rule/text()">
								<tr class="longDisplay">
									<td class="subHeader">
										<xsl:value-of select="ape:resource('eagcontent.rulerepositorsup')"/>
									</td>
									<td>
										<xsl:value-of select="eag:repositorsup/eag:rule"/>
									</td>
								</tr>
							</xsl:if>

							<!-- adminunit -->
							<xsl:if test="eag:adminhierarchy/eag:adminunit/text()">
								<tr class="longDisplay">
									<td class="header">
										<xsl:value-of select="ape:resource('eagcontent.archivedepartment')"/>
									</td>
									<td>
										<xsl:call-template name="multilanguage">
											<xsl:with-param name="list" select="eag:adminhierarchy/eag:adminunit"/>
										</xsl:call-template>
									</td>
								</tr>
							</xsl:if>

							<!-- building -->
							<xsl:if
								test="eag:buildinginfo/eag:building/eag:descriptiveNote/eag:p/text()">
								<tr class="longDisplay">
									<td class="header">
										<xsl:value-of select="ape:resource('eagcontent.archivebuilding')"/>
									</td>
									<td>
										<xsl:call-template name="multilanguage">
											<xsl:with-param name="list" select="eag:buildinginfo/eag:building/eag:descriptiveNote/eag:p"/>
										</xsl:call-template>
									</td>
								</tr>
							</xsl:if>

							<!-- repositorarea -->
							<xsl:if test="eag:buildinginfo/eag:repositorarea/eag:num/text()">
								<tr class="longDisplay">
									<td class="header">
										<xsl:value-of select="ape:resource('eagcontent.buildingarea')"/>
									</td>
									<td>
										<xsl:apply-templates select="eag:buildinginfo/eag:repositorarea/eag:num"/>
									</td>
								</tr>
							</xsl:if>

							<!-- lengthshelf -->
							<xsl:if test="eag:buildinginfo/eag:lengthshelf/eag:num/text()">
								<tr class="longDisplay">
									<td class="header">
										<xsl:value-of select="ape:resource('eagcontent.lengthshelfavailable')"/>
									</td>
									<td>
										<xsl:apply-templates select="eag:buildinginfo/eag:lengthshelf/eag:num"/>
									</td>
								</tr>
							</xsl:if>
						</tbody>
					</table>
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
						<xsl:value-of select="ape:resource('eagcontent.other')"/>
						<!-- Check if occult parts exists -->
						<xsl:if test="./eag:eag/eag:archguide/eag:identity/eag:nonpreform">
							<xsl:text> (</xsl:text>
							<a class="displayLinkSeeMore" href="javascript:seeMore('otherDisplay');">
								<xsl:value-of select="ape:resource('eagcontent.seemore')"/>
							</a>
							<a class="displayLinkSeeLess" href="javascript:seeLess('otherDisplay');">
								<xsl:value-of select="ape:resource('eagcontent.seeless')"/>
							</a>
							<xsl:text>)</xsl:text>
						</xsl:if>
					</th>
				</tr>
			</thead>
			<tbody>

				<!-- relationEntry -->
				<xsl:choose>
					<xsl:when test="./eag:eag/eag:relations/eag:resourceRelation/@href">
						<tr>
							<td class="header">
								<xsl:value-of select="ape:resource('eagcontent.relatedresource')"/>:
							</td>
							<td>
								<xsl:for-each select="./eag:eag/eag:relations/eag:resourceRelation/eag:relationEntry">
									<xsl:variable name="link" select="current()/parent::node()/@href"></xsl:variable>
										<xsl:choose>
											<xsl:when test="$link and $link != '' and text()">
												<div>
													<a href="{$link}" target="_blank">
														<xsl:value-of select="text()"/>
													</a>
												</div>
											</xsl:when>
											<xsl:when test="$link and $link != ''">
												<div>
													<a href="{$link}" target="_blank">
														<xsl:value-of select="ape:resource('eagcontent.linktorelatedresource')"/>
													</a>
												</div>
											</xsl:when>
											<xsl:when test="text()">
												<div>
													<xsl:value-of select="text()"/>
												</div>
											</xsl:when>
										</xsl:choose>
								</xsl:for-each>
							</td>
						</tr>
					</xsl:when>
					<xsl:otherwise>
						<xsl:if test="./eag:eag/eag:relations/eag:resourceRelation/eag:relationEntry/text()">
							<tr>
								<td class="header">
									<xsl:value-of select="ape:resource('eagcontent.relatedresource')"/>
								</td>
								<td>
									<xsl:value-of select="./eag:eag/eag:relations/eag:resourceRelation/eag:relationEntry"/>
								</td>
							</tr>
						</xsl:if>
					</xsl:otherwise>
				</xsl:choose>

				<!-- nonpreform and useDates -->
				<xsl:if test="./eag:eag/eag:archguide/eag:identity/eag:nonpreform">
					<tr class="longDisplay">
						<td class="header">
							<xsl:value-of select="ape:resource('eagcontent.alternative')"/>
						</td>
						<td>
							<xsl:call-template name="multilanguageNoperform">
								<xsl:with-param name="list" select="./eag:eag/eag:archguide/eag:identity/eag:nonpreform"/>
							</xsl:call-template>
						</td>
					</tr>
				</xsl:if>

				<!-- lastupdate -->
				<xsl:if test="./eag:eag/eag:control/eag:maintenanceHistory/eag:maintenanceEvent/eag:eventDateTime/text()">
					<tr>
						<td class="header">
							<xsl:value-of select="ape:resource('eagcontent.lastupdate')"/>:
						</td>
						<td>
							<xsl:variable name="numberOfMaintenanceEvent" select="count(./eag:eag/eag:control/eag:maintenanceHistory/eag:maintenanceEvent)"/>
							<xsl:value-of select="./eag:eag/eag:control/eag:maintenanceHistory/eag:maintenanceEvent[$numberOfMaintenanceEvent]/eag:eventDateTime"/>
						</td>
					</tr>
				</xsl:if>
			</tbody>
		</table>
	</xsl:template>

	<xsl:template name="email">
		<xsl:param name="parent" select="current()"/>
		<xsl:param name="class"/>
		<xsl:param name="trClass" select="''"/>
		<xsl:if test="$parent/eag:email/@href">
			<tr class="{$trClass}">
				<td class="{$class}">
					<xsl:value-of select="ape:resource('eagcontent.email')"/>
				</td>
				<td>
					<xsl:for-each select="$parent/eag:email">
						<xsl:variable name="email" select="@href"/>
						<div>
							<a href="mailto:{$email}" target="_blank">
								<xsl:choose>
									<xsl:when test="text()">
										<xsl:value-of select="."/>
									</xsl:when>
									<xsl:otherwise>
										<xsl:value-of select="$email"/>
									</xsl:otherwise>
								</xsl:choose>
							</a>
						</div>
					</xsl:for-each>
				</td>
			</tr>
		</xsl:if>
	</xsl:template>

	<xsl:template name="webpage">
		<xsl:param name="parent" select="current()"/>
		<xsl:param name="class"/>
		<xsl:param name="trClass" select="''"/>
		<xsl:if test="$parent/eag:webpage/@href">
			<tr class="{$trClass}">
				<td class="{$class}">
					<xsl:value-of select="ape:resource('eagcontent.webpage')"/>
				</td>
				<td>
					<xsl:for-each select="$parent/eag:webpage">
						<xsl:variable name="webpage" select="@href"/>
						<div>
							<a href="{$webpage}" target="_blank">
								<xsl:choose>
									<xsl:when test="text()">
										<xsl:value-of select="."/>
									</xsl:when>
									<xsl:otherwise>
										<xsl:value-of select="$webpage"/>
									</xsl:otherwise>
								</xsl:choose>
							</a>
						</div>
					</xsl:for-each>
				</td>
			</tr>
		</xsl:if>
	</xsl:template>

	<xsl:template name="telephone">
		<xsl:param name="parent" select="current()"/>
		<xsl:param name="class"/>
		<xsl:param name="trClass" select="''"/>
		<xsl:if test="$parent/eag:telephone">
			<tr class="{$trClass}">
				<td class="{$class}">
					<xsl:value-of select="ape:resource('eagcontent.tel')"/>
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

	<xsl:template name="multilanguage">
		<xsl:param name="list"/>
		<xsl:choose>
			<xsl:when test="count($list) > 1">
				<xsl:choose>
					<xsl:when test="$list[@xml:lang = $language.selected]">
						<xsl:for-each select="$list[@xml:lang = $language.selected]">
							<p>
								<xsl:value-of select="."/>
							</p>
						</xsl:for-each>
					</xsl:when>
					<xsl:when test="$list[@xml:lang = $language.default]">
						<xsl:for-each select="$list[@xml:lang = $language.default]">
							<p>
								<xsl:value-of select="."/>
							</p>
						</xsl:for-each>
					</xsl:when>
					<xsl:otherwise>
						<xsl:variable name="language.first" select="$list[1]/@xml:lang"></xsl:variable>
						<xsl:for-each select="$list[@xml:lang = $language.first]">
							<p>
								<xsl:value-of select="."/>
							</p>
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

	<xsl:template name="multilanguageNoperform">
		<xsl:param name="list"/>
		<xsl:choose>
			<xsl:when test="count($list) > 1">
				<xsl:choose>
					<xsl:when test="$list[@xml:lang = $language.selected]">
						<xsl:for-each select="$list[@xml:lang = $language.selected]">
							<p>
								<xsl:apply-templates select="."/>
							</p>
						</xsl:for-each>
					</xsl:when>
					<xsl:when test="$list[@xml:lang = $language.default]">
						<xsl:for-each select="$list[@xml:lang = $language.default]">
							<p>
								<xsl:apply-templates select="."/>
							</p>
						</xsl:for-each>
					</xsl:when>
					<xsl:otherwise>
						<xsl:variable name="language.first" select="$list[1]/@xml:lang"></xsl:variable>
						<xsl:for-each select="$list[@xml:lang = $language.first]">
							<p>
								<xsl:apply-templates select="."/>
							</p>
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

	<xsl:template match="eag:nonpreform">
		<xsl:value-of select="text()"/>
		<!-- when there are only 1 dateSet -->
		<xsl:if test="./eag:useDates/eag:dateSet">
			<xsl:text> (</xsl:text>
			<xsl:apply-templates select="./eag:useDates/eag:dateSet"/>
			<xsl:text>)</xsl:text>
		</xsl:if>
		<!-- when there are only 1 dateRange -->
		<xsl:if test="./eag:useDates/eag:dateRange">
			<xsl:text> (</xsl:text>
			<xsl:apply-templates select="./eag:useDates/eag:dateRange"/>
			<xsl:text>)</xsl:text>
		</xsl:if>
		<!-- when there are only 1 date -->
		<xsl:if test="./eag:useDates/eag:date">
			<xsl:text> (</xsl:text>
			<xsl:apply-templates select="./eag:useDates/eag:date"/>
			<xsl:text>)-</xsl:text>
		</xsl:if>
		
	</xsl:template>

	<!-- template for dateSet -->
	<xsl:template match="eag:dateSet">
		<xsl:if test="eag:dateRange or eag:date">
			<xsl:for-each select="eag:date">
				<xsl:value-of select="."/>
				<xsl:if test="position() != last()">
					<xsl:text>, </xsl:text>
				</xsl:if>
			</xsl:for-each>
			<xsl:if test="eag:dateRange and eag:date">
				<xsl:text>, </xsl:text>
			</xsl:if>
			<xsl:for-each select="eag:dateRange">
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
			</xsl:for-each>
		</xsl:if>
	</xsl:template>
	
		<!-- template for date -->
	<xsl:template match="eag:date">
		<xsl:for-each select="eag:date">
			<xsl:value-of select="."/>
			<xsl:if test="position() != last()">
				<xsl:text>, </xsl:text>
			</xsl:if>
			<xsl:value-of select="./eag:toDate"/>
		</xsl:for-each>
	</xsl:template>
	
		<!-- template for dateRange -->
	<xsl:template match="eag:dateRange">
		<xsl:if test="position() != 1">
			<xsl:text>, </xsl:text>
		</xsl:if>
		<xsl:value-of select="./eag:fromDate"/><xsl:text> - </xsl:text>
		<xsl:value-of select="./eag:toDate"/>
		<xsl:for-each select="eag:dateRange">
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
			</xsl:choose>
		</xsl:for-each>
	</xsl:template>

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
									<xsl:for-each select="$list[@xml:lang = $language.selected]">
										<p>
											<xsl:apply-templates select="."/>
										</p>
									</xsl:for-each>
								</xsl:when>
								<xsl:when test="$list[@xml:lang = $language.default]">
									<xsl:for-each select="$list[@xml:lang = $language.default]">
										<p>
											<xsl:apply-templates select="."/>
										</p>
									</xsl:for-each>
								</xsl:when>
								<xsl:otherwise>
									<xsl:variable name="language.first" select="$list[1]/@xml:lang"></xsl:variable>
									<xsl:for-each select="$list[@xml:lang = $language.first]">
										<p>
											<xsl:apply-templates select="."/>
										</p>
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

	<xsl:template match="eag:advancedOrders | eag:readersTicket | eag:termsOfUse | eag:citation">
		<xsl:choose>
			<xsl:when test="./@href">
				<xsl:variable name="href" select="./@href"/>
				<a href="{$href}" target="_blank">
					<xsl:choose>
						<xsl:when test="text()">
							<xsl:value-of select="."/>
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="$href"/>
						</xsl:otherwise>
					</xsl:choose>
				</a>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="."/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<xsl:template match="eag:directions">
		<xsl:value-of select="text()"/>
		<xsl:for-each select="eag:citation">
			<p>
				<xsl:apply-templates select="."/>
			</p>
		</xsl:for-each>
	</xsl:template>

	<xsl:template match="eag:num">
		<xsl:variable name="unit" select="concat('eagcontent.num.unit.', ./@unit)"/>
		<xsl:value-of select="text()"/>
		<xsl:text> </xsl:text>
		<xsl:value-of select="ape:resource($unit)" disable-output-escaping="yes"/>
	</xsl:template>
</xsl:stylesheet>
