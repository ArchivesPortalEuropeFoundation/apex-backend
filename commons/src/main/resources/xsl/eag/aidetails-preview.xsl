<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                xmlns='http://www.w3.org/1999/xhtml' xmlns:eag="http://www.archivesportaleurope.net/Portal/profiles/eag_2012/"
                xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:ape="http://www.archivesportaleurope.eu/xslt/extensions" 
                xmlns:xd="http://www.pnp-software.com/XSLTdoc"
                exclude-result-prefixes="xlink xlink xsi eag ape xd" >
    <xsl:import href="commonsEag.xsl"/>
    <xd:doc type="stylesheet">
        <xd:short>Page for display the preview data from the archival institution chosen.</xd:short>
        <xd:detail>
            Page addetails-preview.xml for display the preview data from the archival institution chosen(only the information of the first repository is shown, other repositories name only). With the following data:
            <ul>
                <li>
                    <b>CONTACT DETAILS</b>:
                    <ul>
                        <li>Visitors Address.</li>
                        <li>Country.</li>
                        <li>Webpage.</li>
                        <li>Telephone.</li>		
                    </ul>
                </li>
                <li>
                    <b>ACCESS AND SERVICE INFORMATION</b>:
                    <ul>
                        <li>Opening hours.</li>
                        <li>Closing dates.</li>
                    </ul>
                </li>
            </ul>
        </xd:detail>  
    </xd:doc>
    <xd:doc type="import">
        <xd:short>Import the XML: <a href="commonsEag.xsl.xd.html">
                <code>commonsEag.xml</code>
            </a>.
        </xd:short>
        <xd:detail>
            CommonsEag.xml common templates for displaying data.
        </xd:detail>	    
    </xd:doc>
    <xsl:output method="html" indent="yes" version="4.0" encoding="UTF-8"/>
    <xd:doc>
        <xd:short>The param that controls the selected language.</xd:short>    
    </xd:doc>
    <xsl:param name="language.selected"/>
    <xd:doc>
        <xd:short>The variable that controls the default language.</xd:short>    
    </xd:doc>
    <xsl:variable name="language.default" select="'eng'"/>
    <xd:doc>
        <xd:short>Template <code>main</code>.</xd:short>
        <xd:detail>
            Template main that runs around the tree root, to display: <code>contact details<code> and </code>access and service information</code>.
        </xd:detail>
    </xd:doc>
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
                                            <xsl:value-of select="ape:resource('eag2012.portal.visitorsaddress')"/>
                                            <xsl:text>:</xsl:text>
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
                                            <xsl:value-of select="ape:resource('eag2012.portal.country')"/>
                                            <xsl:text>:</xsl:text>
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
                                                <xsl:value-of select="ape:resource('eag2012.portal.openinghours')"></xsl:value-of>
                                                <xsl:text>:</xsl:text>
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
                                                <xsl:value-of select="ape:resource('eag2012.portal.closingdates')"></xsl:value-of>
                                                <xsl:text>:</xsl:text>
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
</xsl:stylesheet>
