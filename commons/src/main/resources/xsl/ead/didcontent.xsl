<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:fn="http://www.w3.org/2005/xpath-functions" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xmlns='http://www.w3.org/1999/xhtml' xmlns:ead="urn:isbn:1-931666-22-9" xmlns:xlink="http://www.w3.org/1999/xlink"
	xmlns:ape="http://www.archivesportaleurope.eu/xslt/extensions"
	exclude-result-prefixes="xlink xlink xsi ead ape">
	<xsl:param name="eadcontent.extref.prefix"/>
	<xsl:include href="common.xsl" />
	<xsl:output method="html" indent="yes" version="4.0" encoding="UTF-8" />
	<xsl:template match="/">
		<xsl:for-each select="/ead:ead/ead:archdesc/ead:did">
			<div class="eadid">
				<xsl:value-of select="ead:unitid" />
			</div>
			<div class="titleproper">
				<xsl:value-of select="ead:unittitle" />
			</div>
            <div class="subtitle">
                <xsl:apply-templates  select="ead:unitdate" mode="alterdate"/>
            </div>
			<div id="content">
				<div id="expandableContent">
					<xsl:if test="ead:langmaterial">
						<xsl:call-template name="langmaterial" />
					</xsl:if>
					<xsl:if test="ead:origination">
						<xsl:call-template name="origination" />
					</xsl:if>

					<xsl:if test="ead:repository">
						<xsl:call-template name="repository" />
					</xsl:if>
					<xsl:if test="ead:physloc/@label">
						<xsl:call-template name="physloc-label" />
					</xsl:if>
					<xsl:if test="ead:materialspec">
						<xsl:call-template name="materialspec" />
					</xsl:if>
					<xsl:for-each select="ead:physdesc">
						<xsl:if test="ead:physfacet">
							<xsl:call-template name="physfacet" />
						</xsl:if>
						<xsl:if test="ead:extent">
							<xsl:call-template name="extent" />
						</xsl:if>
						<xsl:if test="ead:genreform">
							<xsl:call-template name="genreform" />
						</xsl:if>
						<xsl:if test="ead:dimensions">
							<xsl:call-template name="dimensions" />
						</xsl:if>
					</xsl:for-each>
					<xsl:if test="ead:dao">
						<xsl:call-template name="dao" />
					</xsl:if>

				</div>
			</div>
		</xsl:for-each>
	</xsl:template>



</xsl:stylesheet>