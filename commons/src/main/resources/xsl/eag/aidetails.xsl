<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xmlns='http://www.w3.org/1999/xhtml' xmlns:eag="http://www.archivesportaleurope.eu/profiles/APEnet_EAG/" xmlns:xlink="http://www.w3.org/1999/xlink"
	xmlns:ape="http://www.archivesportaleurope.eu/xslt/extensions" exclude-result-prefixes="xlink xlink xsi eag ape">

	<xsl:output method="html" indent="yes" version="4.0" encoding="UTF-8" />
	<xsl:template match="/">
		<div class="aiTitle">
			<xsl:value-of select="./eag:eag/eag:archguide/eag:identity/eag:autform"></xsl:value-of>
		</div>
		<xsl:apply-templates select="./eag:eag/eag:archguide/eag:desc"></xsl:apply-templates>

	</xsl:template>
	<xsl:template match="eag:desc">
		<table class="aiSection">
			<thead>
				<tr>
					<th colspan="2">
						<xsl:value-of select="ape:resource('directory.text.contact')" />
					</th>
				</tr>
			</thead>
			<tbody>
				<tr>
					<td class="header">
						<xsl:value-of select="ape:resource('directory.text.address')" />
					</td>
					<td id="address">
						<xsl:value-of select="./eag:street" />
						<xsl:text> - </xsl:text>
						<xsl:value-of select="./eag:postalcode" />
						<xsl:text> - </xsl:text>
						<xsl:value-of select="./eag:municipality" />
					</td>
				</tr>
				<tr>
					<td class="header">
						<xsl:value-of select="ape:resource('directory.text.country')" />
					</td>
					<td>
						<xsl:value-of select="./eag:country" />
					</td>
				</tr>
 				<tr>
					<td class="header">
						<xsl:value-of select="ape:resource('directory.text.tel')" />
					</td>
					<td>
						<xsl:value-of select="./eag:telephone" />
					</td>
				</tr>
				<tr>
					<td class="header"><xsl:value-of select="ape:resource('directory.text.email')" /></td>
					<td>
						<xsl:variable name="email" select="./eag:email/@href"/>
						<a href="mailto:{$email}"  target="_blank" ><xsl:value-of select="$email" /></a>
					</td>
				</tr>
				<tr>
					<td class="header"><xsl:value-of select="ape:resource('directory.text.webpage')" /></td>
					<td>
						<xsl:variable name="webpage" select="./eag:webpage/@href"/>
						<a href="{$webpage}"  target="_blank" ><xsl:value-of select="$webpage" /></a>
					</td>
				</tr>
				<xsl:if test="../eag:identity/eag:parform/text()">				
				<tr>
					<td class="header"><xsl:value-of select="ape:resource('directory.text.parallelformname')" /></td>
					<td><xsl:value-of select="../eag:identity/eag:parform" /></td>
				</tr>				
				</xsl:if>
			</tbody>
		</table>
		<table class="aiSection">
			<thead>
				<tr>
					<th colspan="2">
						<xsl:value-of select="ape:resource('directory.text.accessinfo')" />
					</th>
				</tr>
			</thead>
			<tbody>
				<tr>
					<td class="header"><xsl:value-of select="ape:resource('directory.text.accessanduse')" /></td>
					<td>
					<xsl:choose>
						<xsl:when test="./eag:access[@question='yes']">
							<xsl:value-of select="ape:resource('directory.text.accesspublic')" />
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="ape:resource('directory.text.accesspermission')" />
						</xsl:otherwise>
					</xsl:choose>
					</td>
				</tr>
				<tr>
					<td class="header"><xsl:value-of select="ape:resource('directory.text.access')" /></td>
					<td>
					<xsl:choose>
						<xsl:when test="./eag:buildinginfo/eag:handicapped[@question='yes']">
							<xsl:value-of select="ape:resource('directory.text.facilities')" />
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="ape:resource('directory.text.nofacilities')" />
						</xsl:otherwise>
					</xsl:choose>
					</td>
				</tr>
			</tbody>
		</table>
		<table class="aiSection">
			<thead>
				<tr>
					<th colspan="2">
						<xsl:value-of select="ape:resource('directory.text.service')" />
					</th>
				</tr>
			</thead>
			<tbody>
				<xsl:if test="./eag:buildinginfo/eag:searchroom/eag:num/text()">
				<tr>
					<td class="header"><xsl:value-of select="ape:resource('directory.text.readingroom')" /></td>
					<td><xsl:value-of select="./eag:buildinginfo/eag:searchroom/eag:num" /></td>
				</tr>
				</xsl:if>
				<tr>
					<td class="header"><xsl:value-of select="ape:resource('directory.text.libraryassets')" /></td>
					<td>
					<xsl:choose>
						<xsl:when test="./eag:techservices/eag:library[@question='yes']">
							<xsl:value-of select="ape:resource('directory.text.library')" />
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="ape:resource('directory.text.nolibrary')" />
						</xsl:otherwise>
					</xsl:choose>
					</td>
				</tr>	
				<tr>
					<td class="header"><xsl:value-of select="ape:resource('directory.text.restoration')" /></td>
					<td>
					<xsl:choose>
						<xsl:when test="./eag:techservices/eag:restorationlab[@question='yes']">
							<xsl:value-of select="ape:resource('directory.text.available')" />
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="ape:resource('directory.text.notavailable')" />
						</xsl:otherwise>
					</xsl:choose>
					</td>
				</tr>	
				<tr>
					<td class="header"><xsl:value-of select="ape:resource('directory.text.repro')" /></td>
					<td>
					<xsl:choose>
						<xsl:when test="./eag:techservices/eag:reproductionser[@question='yes']">
							<xsl:value-of select="ape:resource('directory.text.available')" />
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="ape:resource('directory.text.notavailable')" />
						</xsl:otherwise>
					</xsl:choose>
					</td>
				</tr>		
				<tr>
					<td class="header"><xsl:value-of select="ape:resource('directory.text.information')" /></td>
					<td>
					<xsl:choose>
						<xsl:when test="./eag:techservices/eag:automation[@question='yes']">
							<xsl:value-of select="ape:resource('directory.text.available')" />
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="ape:resource('directory.text.notavailable')" />
						</xsl:otherwise>
					</xsl:choose>
					</td>
				</tr>
				<xsl:if test="./eag:extent/eag:num/text()">		
				<tr>
					<td class="header"><xsl:value-of select="ape:resource('directory.text.extent')" /></td>
					<td><xsl:value-of select="./eag:extent/eag:num" /></td>
				</tr>								
				</xsl:if>	
				<tr>
					<td class="header"><xsl:value-of select="ape:resource('directory.text.fileupdated')" /></td>
					<td>
					<xsl:choose>
						<xsl:when test="/eag:eag/eag:eagheader/eag:mainhist/eag:mainevent[@maintype='update']">
							<xsl:value-of select="/eag:eag/eag:eagheader/eag:mainhist/eag:mainevent[@maintype='update']/eag:date/@normal" />
						</xsl:when>
						<xsl:when test="/eag:eag/eag:eagheader/eag:mainhist/eag:mainevent[@maintype='create']">
							<xsl:value-of select="/eag:eag/eag:eagheader/eag:mainhist/eag:mainevent[@maintype='create']/eag:date/@normal" />
						</xsl:when>
					</xsl:choose>
					</td>
				</tr>		
				<xsl:if test="./eag:repositorguides/eag:repositorguide">		
				<tr>
					<td class="header"><xsl:value-of select="ape:resource('text.list.holdings.guide')" /></td>
					<td><ul>
						<xsl:for-each select="./eag:repositorguides/eag:repositorguide">
							<xsl:variable name="href" select="./@href"/>
							<li><a href="{$href}"  target="_blank" ><xsl:value-of select="text()" /></a></li>									
						</xsl:for-each>
						</ul>
					</td>
				</tr>								
				</xsl:if>																				
			</tbody>
		</table>		
		
	</xsl:template>
</xsl:stylesheet>
