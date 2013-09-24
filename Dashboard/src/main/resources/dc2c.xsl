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

    <xsl:template match="/">
        <c>
            <xsl:call-template name="level">
                <xsl:with-param name="level" select="dc/type"/>
            </xsl:call-template>
            <did>
                <xsl:call-template name="did"/>
            </did>
            <xsl:call-template name="notdid"/>
        </c>
    </xsl:template>

    <xsl:template name="level">
        <xsl:param name="level"/>
        <xsl:attribute name="level">
            <xsl:choose>
                <xsl:when test="$level eq 'Fundo'">
                    <xsl:value-of select="'fonds'"/>
                </xsl:when>
                <xsl:when test="$level eq 'Série' or $level eq 'Subfundo' or $level eq 'Secção' or $level eq 'Subsérie' or $level eq 'Subsecção'">
                    <xsl:value-of select="'series'"/>
                </xsl:when>
                <xsl:when test="$level eq 'Documento composto' or $level eq 'Unidade de instalação'">
                    <xsl:value-of select="'file'"/>
                </xsl:when>
                <xsl:when test="$level eq 'Documento simples'">
                    <xsl:value-of select="'item'"/>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:value-of select="'series'"/>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:attribute>
    </xsl:template>

    <xsl:template name="did">
        <xsl:for-each select="dc/identifier">
            <xsl:if test="not(starts-with(text(), 'http'))">
                <unitid type="call number" encodinganalog="3.1.1">
                    <xsl:for-each select="parent::*/*[local-name()='identifier']">
                        <xsl:if test="starts-with(text(), 'http')">
                            <extptr>
                                <xsl:attribute name="xlink:href">
                                    <xsl:value-of select="text()" />
                                </xsl:attribute>
                            </extptr>
                        </xsl:if>
                    </xsl:for-each>
                    <xsl:apply-templates select="text()" />
                </unitid>
            </xsl:if>
        </xsl:for-each>
        <xsl:if test="dc/title">
            <unittitle encodinganalog="3.1.2">
                <xsl:value-of select="dc/title/text()"/>
            </unittitle>
        </xsl:if>
        <xsl:if test="dc/publisher">
            <origination>
                <xsl:value-of select="dc/publisher"/>
            </origination>
        </xsl:if>
        <xsl:for-each select="dc/date">
            <unitdate encodinganalog="3.1.3" era="ce" calendar="gregorian">
                <xsl:value-of select="text()"/>
            </unitdate>
        </xsl:for-each>
        <xsl:for-each select="dc/format">
            <physdesc encodinganalog="3.1.5">
                <extent>
                    <xsl:value-of select="text()"/>
                </extent>
            </physdesc>
        </xsl:for-each>
        <xsl:for-each select="dc/relation">
            <dao>
                <xsl:attribute name="xlink:title" select="'thumbnail'"/>
                <xsl:attribute name="xlink:href" select="replace(text(), 'vault/?id=', 'thumb/?f=')"/>
            </dao>
            <xsl:for-each select="parent::*/*[local-name()='identifier']">
                <xsl:if test="starts-with(text(), 'http')">
                    <dao>
                        <xsl:attribute name="xlink:href" select="replace(text(), 'details?', 'viewer?')"/>
                    </dao>
                </xsl:if>
            </xsl:for-each>
        </xsl:for-each>
    </xsl:template>

    <xsl:template name="notdid">
        <xsl:if test="dc/subject">
            <scopecontent encodinganalog="summary">
                <p>
                    <xsl:value-of select="dc/subject"/>
                </p>
            </scopecontent>
        </xsl:if>
    </xsl:template>

    <xsl:template match="text()|@*" priority="2">
        <xsl:copy>
            <xsl:apply-templates select="node()|@*"/>
        </xsl:copy>
    </xsl:template>

    <xsl:template match="comment()" priority="3" />

</xsl:stylesheet>
