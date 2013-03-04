<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns="urn:isbn:1-931666-22-9"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:fo="http://www.w3.org/1999/XSL/Format"
                xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                xmlns:xs="http://www.w3.org/2001/XMLSchema"
                xmlns:xlink="http://www.w3.org/1999/xlink"
                xsi:schemaLocation="urn:isbn:1-931666-22-9 http://www.loc.gov/ead/ead.xsd"
                xpath-default-namespace="urn:isbn:1-931666-22-9"
                exclude-result-prefixes="xsl fo xs">

        <xsl:output indent="yes" method="xml" />
        <xsl:strip-space elements="*"/>

        <xsl:param name="mainagencycode" select="'PT-DGARQ'"/>

        <xsl:template match="/">
            <xsl:variable name="mainIdentifier"><xsl:value-of select="normalize-space(//*:unitid/text())" /></xsl:variable>

            <ead xmlns="urn:isbn:1-931666-22-9" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="urn:isbn:1-931666-22-9 http://www.archivesportaleurope.eu/profiles/APEnet_EAD.xsd http://www.w3.org/1999/xlink http://www.loc.gov/standards/xlink/xlink.xsd" audience="external">
                <eadheader countryencoding="iso3166-1" dateencoding="iso8601" langencoding="iso639-2b" repositoryencoding="iso15511" scriptencoding="iso15924" relatedencoding="MARC21">
                    <eadid countrycode="PT">
                        <xsl:attribute name="mainagencycode" select="$mainagencycode"/>
                        <xsl:attribute name="identifier"><xsl:value-of select="concat($mainagencycode, concat('-', $mainIdentifier))"/></xsl:attribute>
                        <xsl:value-of select="$mainIdentifier"/>
                    </eadid>
                    <filedesc>
                        <titlestmt>
                            <titleproper encodinganalog="245">
                                <xsl:value-of select="//*:unittitle"/>
                            </titleproper>
                        </titlestmt>
                        <publicationstmt>
                            <publisher>
                                <xsl:value-of select="//*:publisher"/>
                            </publisher>
                        </publicationstmt>
                    </filedesc>
	  	        </eadheader>
                <archdesc level="fonds" type="inventory" encodinganalog="3.1.4" relatedencoding="ISAD(G)v2">
                    <did>
                        <unittitle encodinganalog="3.1.2">
                            <xsl:value-of select="//*:unittitle"/>
                        </unittitle>
                        <unitid type="call number" encodinganalog="3.1.1">
                            <xsl:if test="//*:unitid/*:extptr">
                                <extptr>
                                    <xsl:attribute name="xlink:href">
                                        <xsl:value-of select="//*:unitid/*:extptr/@xlink:href"/>
                                    </xsl:attribute>
                                </extptr>
                            </xsl:if>
                            <xsl:value-of select="$mainIdentifier"/>
                        </unitid>
                        <xsl:for-each select="//*:unitdate">
                            <unitdate encodinganalog="3.1.3" era="ce" calendar="gregorian">
                                <xsl:value-of select="text()"/>
                            </unitdate>
                        </xsl:for-each>
                    </did>
                    <scopecontent encodinganalog="summary">
                        <p>
                            <xsl:value-of select="//*:scopecontent"/>
                        </p>
                    </scopecontent>
                    <dsc type="othertype">
                    </dsc>
                </archdesc>
            </ead>
        </xsl:template>

        <xsl:template match="node()">

        </xsl:template>

        <xsl:template match="text()|@*" priority="2">
            <xsl:copy>
                <xsl:apply-templates select="node()|@*"/>
            </xsl:copy>
        </xsl:template>

        <xsl:template match="comment()" priority="3" />

</xsl:stylesheet>
