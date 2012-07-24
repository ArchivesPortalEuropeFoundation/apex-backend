<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:fn="http://www.w3.org/2005/xpath-functions" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xmlns='http://www.w3.org/1999/xhtml' xmlns:ead="urn:isbn:1-931666-22-9" xmlns:xlink="http://www.w3.org/1999/xlink"
	xmlns:ape="http://www.archivesportaleurope.eu/xslt/extensions"
	exclude-result-prefixes="xlink xlink xsi ead ape">
	<xsl:param name="eadcontent.extref.prefix"/>
	<xsl:include href="did-common.xsl" />
	<xsl:output method="html" indent="yes" version="4.0" encoding="UTF-8" />
	<xsl:template match="/">
		<div class="eadid">
			<xsl:value-of select="/ead:ead/ead:eadheader/ead:eadid" />
		</div>
		<xsl:for-each select="/ead:ead/ead:eadheader/ead:filedesc/ead:titlestmt/ead:titleproper">
			<div class="titleproper">
				<xsl:apply-templates  mode="notsearchable"/>
			</div>
		</xsl:for-each>
		<div class="subtitle">
			<xsl:value-of select="ape:resource('eadcontent.introduction')" />
		</div>
		<div id="content">
			<div class="expandContent">
				<xsl:apply-templates select="/ead:ead/ead:archdesc" mode="notsearchable"/>

			</div>
		</div>
	</xsl:template>
	<xsl:template match="ead:archdesc" mode="#all">
		<xsl:if test="ead:scopecontent">
			<xsl:call-template name="scopecontent" />
		</xsl:if>
		<xsl:if test="ead:bioghist">
			<xsl:call-template name="bioghist" />			
		</xsl:if>
		<xsl:if test="ead:custodhist">
			<xsl:call-template name="custodhist" />
		</xsl:if>
		<xsl:if test="ead:appraisal">
			<xsl:call-template name="appraisal" />
		</xsl:if>
		<xsl:if test="ead:processinfo">
			<xsl:call-template name="processinfo" />
		</xsl:if>
		<xsl:if test="ead:accruals">
			<xsl:call-template name="accruals" />
		</xsl:if>
		<xsl:if test="ead:arrangement">
			<xsl:call-template name="arrangement" />
		</xsl:if>
		<xsl:if test="ead:fileplan">
			<xsl:call-template name="fileplan" />
		</xsl:if>
		<xsl:if test="ead:accessrestrict">
			<xsl:call-template name="accessrestrict" />
		</xsl:if>
		<xsl:if test="ead:userestrict">
			<xsl:call-template name="userestrict" />
		</xsl:if>
		<xsl:if test="ead:phystech">
			<xsl:call-template name="phystech" />
		</xsl:if>
		<xsl:if test="ead:otherfindaid">
			<xsl:call-template name="otherfindaid" />
		</xsl:if>
		<xsl:if test="ead:relatedmaterial">
			<xsl:call-template name="relatedmaterial" />
		</xsl:if>
		<xsl:if test="ead:separatedmaterial">
			<xsl:call-template name="separatedmaterial" />
		</xsl:if>
		<xsl:if test="ead:altformavail">
			<xsl:call-template name="altformavail" />
		</xsl:if>
		<xsl:if test="ead:originalsloc">
			<xsl:call-template name="originalsloc" />
		</xsl:if>
		<xsl:if test="ead:bibliography">
			<xsl:call-template name="bibliography" />
		</xsl:if>
		<xsl:if test="ead:prefercite">
			<xsl:call-template name="prefercite" />
		</xsl:if>
		<xsl:apply-templates select="./ead:did"/>

		<xsl:if test="ead:odd">
			<xsl:call-template name="odd" />
		</xsl:if>
		<xsl:if test="ead:controlaccess">
			<xsl:call-template name="controlaccess" />
		</xsl:if>
	</xsl:template>
	<xsl:template match="ead:did">
		<xsl:if test="ead:note">
			<xsl:call-template name="note" />
		</xsl:if>
	</xsl:template>
</xsl:stylesheet>