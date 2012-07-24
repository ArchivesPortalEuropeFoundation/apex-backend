<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:fn="http://www.w3.org/2005/xpath-functions" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xmlns='http://www.w3.org/1999/xhtml' xmlns:ead="urn:isbn:1-931666-22-9" xmlns:xlink="http://www.w3.org/1999/xlink"
	xmlns:ape="http://www.archivesportaleurope.eu/xslt/extensions"
	exclude-result-prefixes="xlink xlink xsi ead ape">
	<xsl:param name="eadcontent.extref.prefix"/>
	<xsl:include href="common.xsl"/>
	<xsl:output method="html" indent="yes" version="4.0" encoding="UTF-8" />
	<xsl:template match="/">
		<div id="body">
			<div class="eadid">
				<xsl:value-of select="/ead:ead/ead:archdesc/ead:did/ead:unitid" />
			</div>
			<xsl:for-each select="/ead:ead/ead:eadheader/ead:filedesc/ead:titlestmt/ead:titleproper">
				<div class="titleproper">
					<xsl:apply-templates mode="notsearchable"/>
				</div>
			</xsl:for-each>
			<xsl:for-each select="/ead:ead/ead:eadheader/ead:filedesc/ead:titlestmt/ead:subtitle">
				<div class="subtitle">
					<xsl:apply-templates mode="notsearchable"/>
				</div>
			</xsl:for-each>



			<xsl:for-each select="/ead:ead/ead:eadheader/ead:filedesc/ead:titlestmt/ead:author">
				<div class="defaultlayout">
					<xsl:apply-templates mode="notsearchable"/>
				</div>
			</xsl:for-each>
			<xsl:for-each select="/ead:ead/ead:eadheader/ead:filedesc/ead:publicationstmt/ead:date">
				<div class="defaultlayout">
					<xsl:apply-templates mode="notsearchable"/>
				</div>
			</xsl:for-each>
			<xsl:for-each select="/ead:ead/ead:eadheader/ead:filedesc/ead:publicationstmt/ead:publisher">
				<div class="defaultlayout">
					<xsl:apply-templates mode="notsearchable"/>
				</div>
			</xsl:for-each>
			<xsl:if test="/ead:ead/ead:eadheader/ead:filedesc/ead:publicationstmt/ead:address">
				<div class="defaultlayout">
					<xsl:apply-templates select="/ead:ead/ead:eadheader/ead:filedesc/ead:publicationstmt/ead:address" mode="notsearchable"/>
				</div>
			</xsl:if>

			<xsl:for-each select="/ead:ead/ead:eadheader/ead:filedesc/ead:seriesstmt/ead:titleproper">
				<div class="defaultlayout">
					<xsl:apply-templates mode="notsearchable"/>
				</div>
			</xsl:for-each>
			<xsl:for-each select="/ead:ead/ead:eadheader/ead:profiledesc/ead:langusage">
				<div class="defaultlayout">
					<xsl:apply-templates mode="notsearchable"/>
				</div>
			</xsl:for-each>
			<xsl:for-each select="/ead:ead/ead:eadheader/ead:profiledesc/ead:creation">
				<h2 class="creation">
					<xsl:value-of select="ape:resource('eadcontent.frontpage.creation')" />
				</h2>
				<div class="creation-content">
					<xsl:apply-templates mode="notsearchable"/>
				</div>
			</xsl:for-each>
			<div class="eadidurl">
				<xsl:if test="/ead:ead/ead:eadheader/ead:eadid/@url">
					<xsl:variable name="seeInContext" select="/ead:ead/ead:eadheader/ead:eadid/@url" />
					<a href="{$seeInContext}" target="_blank">
						<xsl:value-of select="ape:resource('eadcontent.frontpage.eadid.url')"/>
					</a>
				</xsl:if>
			</div>
		</div>
	</xsl:template>


</xsl:stylesheet>