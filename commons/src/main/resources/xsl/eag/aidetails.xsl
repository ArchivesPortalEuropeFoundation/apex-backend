<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                xmlns='http://www.w3.org/1999/xhtml' xmlns:eag="http://www.archivesportaleurope.net/Portal/profiles/eag_2012/"
                xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:ape="http://www.archivesportaleurope.eu/xslt/extensions" 
                xmlns:xd="http://www.pnp-software.com/XSLTdoc"
                exclude-result-prefixes="xlink xlink xsi eag ape  xd">
    <xsl:import href="commonsEag.xsl"/>
    <xsl:import href="contact.xsl"/>
    <xsl:import href="archivesHolding.xsl"/>
    <xsl:import href="accessService.xsl"/>
    <xsl:import href="otherInformation.xsl"/>
    <xd:doc type="stylesheet">
        <xd:short>Page for display the data from the archival institution chosen.</xd:short>
        <xd:detail>
            With the following data:
            <ul>
                <li>
                    <b>CONTACT DETAILS</b>:
                    <ul>
                        <br/>
                        <li>Visitors Address.</li>
                        <li>District (Only "see more").</li>
                        <li>County/Local authority (Only "see more").</li>
                        <li>Region/Autonomous authority (Only "see more").</li>
                        <li>Postal address (Only "see more").</li>
                        <li>Country.</li>
                        <li>E-mail address.</li>
                        <li>Webpage.</li>
                        <li>Telephone.</li>
                        <li>Fax (Only "see more").</li>
                        <li>Role of the archive.</li>

                    </ul>
                </li>
                <br/>
                <li>
                    <b>ACCESS AND SERVICE INFORMATION</b>:
                    <ul>
                        <br/>
                        <li>Opening hours.</li>
                        <li>Closing dates.</li>
                        <li>Directions (Only "see more").</li>
                        <li>Access conditions.</li>
                        <li>Access information.</li>
                        <li>Disabled access (Only "see more").</li>
                        <li>Facilities for disabled persons.</li>
                        <li>Terms of use (Only "see more").</li>
                        <li>Reader's ticket.</li>
                        <li>Searchroom.</li>
                        <li>Ordering documents in advance.</li>
                        <li>Searchroom contact: (Only "see more").</li>
                        <ul>
                            <li>E-mail address.</li>
                            <li>Webpage.</li>
                            <li>Telephone.</li>
                        </ul>
                        <li>Archives research service (Only "see more").</li>
                        <li>Computer places (Only "see more").</li>
                        <li>Microfilm/fiche readers (Only "see more").</li>
                        <li>Photograph allowance (Only "see more").</li>
                        <li>Public internet access.</li>
                        <li>Library.</li>
                        <li>Library contact: (Only "see more").</li>
                        <ul>
                            <li>E-mail address.</li>
                            <li>Webpage.</li>
                            <li>Telephone.</li>
                        </ul>
                        <li>Reproductions services (Only "see more").</li>
                        <li>Digitisation service (Only "see more").</li>
                        <li>Photocopying service (Only "see more").</li>
                        <li>Photographic service (Only "see more").</li>
                        <li>Microfilming service (Only "see more").</li>
                        <li>Reproductions service contact: (Only "see more").</li>
                        <ul>
                            <li>E-mail address.</li>
                            <li>Webpage.</li>
                            <li>Telephone.</li>
                        </ul>
                        <li>Conservation laboratory (Only "see more").</li>
                        <li>Refreshment area (Only "see more").</li>
                        <li>Exhibition (Only "see more").</li>
                        <li>Guided tours (Only "see more").</li>
                        <li>Other services (Only "see more").</li>
					

                    </ul>
                </li>
                <br/>
                <li>
                    <b>ARCHIVES AND HOLDINGS DESCRIPTION</b>:
                    <ul>
                        <br/>
                        <li>Archival and other holdings (Only "see more").</li>
                        <li>Extent of holdings.</li>
                        <li>Date(s) of the holdings.</li>
                        <li>History of the Archive (Only "see more").</li>
                        <li>Date of the archives foundation (Only "see more").</li>
                        <li>Archives act (Only "see more").</li>
                        <li>Archive departments (Only "see more").</li>
                        <li>Archive building (Only "see more").</li>
                        <li>Lengthshelf available (Only "see more").</li>
                    </ul>
                </li>
            </ul>
            <br/>
            Section Other Information always shown is common to all repositories. With the following data:
            <ul>
                <li>
                    <b>OTHER INFORMATION</b>:
                    <ul>	
                        <br/>				
                        <li>Related resource.</li>
                        <li>Associated repositories.</li>
                        <li>Name of the archive (in other languages)(Only "see more").</li>
                        <li>Alternative / previous name(s) of the archive (Only "see more").</li>
                        <li>Type of archive.</li>
                        <li>Last update.</li>
                    </ul>
                </li>
            </ul>
        </xd:detail>  
    </xd:doc>
    <xd:doc type="import">
        <xd:short>Import the XML: <a href="commonsEag.xsl.xd.html">
                <code>commonsEag.xml</code>
            </a>, <a href="contact.xsl.xd.html">
                <code>contact.xsl</code>
            </a>, <a href="archivesHolding.xsl.xd.html">
                <code>archivesHolding.xml</code>
            </a>, <a href="accessService.xsl.xd.html">
                <code>accessService.xml</code>
            </a> and <a href="otherInformation.xsl.xd.html">
                <code>otherInformation.xml</code>
            </a>.
        </xd:short>
        <xd:detail>
            CommonsEag.xml common templates for displaying data.<br/>
            Contact.xml templates for displaying data part of Contact Details.<br/>
            ArchivesHolding.xml templates for displaying data part of Access and Service Information.<br/>
            AccessService.xml templates for displaying data part of Archives and Holdings Description.<br/>
            OtherInformation.xml templates for displaying data part of Other Information.
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
	
    <!--  <xsl:include href="commonsEag.xsl"/>--> 	
    <xd:doc>
        <xd:short>Template <code>main</code>.</xd:short>
        <xd:detail>
            Template main that runs around the tree root, to display: <code>Contact details</code>, <code>Access and service information</code>, <code>Archives and holdings description</code>  and <code>Other information</code>.
        </xd:detail>
    </xd:doc>
    <xsl:template match="/">
        <h2 class="blockHeader">
            <xsl:value-of select="./eag:eag/eag:archguide/eag:identity/eag:autform"></xsl:value-of>
        </h2>
        <!-- Hidden div to store the value of the selected language. -->
        <div id="languageSelected" class="hidden">
            <span id="languageSelected">
                <xsl:value-of select="$language.selected"/>
            </span>
        </div>
        <!-- CONTACT -->
        <!-- starts loop -->
        <xsl:for-each select="./eag:eag/eag:archguide/eag:desc/eag:repositories/eag:repository">
            <xsl:variable name="id" select="position()"/>
            <div id="repository_{$id}">
                <xsl:if test="count(current()/parent::node()/eag:repository)> 1">
                    <h3 class="repositoryName">
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
                    </h3>
                </xsl:if>
                <div class="repositoryInfo">				
                    <xsl:call-template name="contact">
                        <xsl:with-param name="id" select="$id"/>
                    </xsl:call-template>					
                    <xsl:call-template name="accessAndService">
                        <xsl:with-param name="id" select="$id"/>
                    </xsl:call-template>
                    <xsl:if test="eag:holdings or eag:holdings/eag:descriptiveNote/eag:p/text() or eag:holdings/eag:extent/eag:num/text() or eag:holdings/eag:date/text() or (eag:holdings/eag:dateRange/eag:fromDate/text() and eag:holdings/eag:dateRange/eag:toDate/text()) or eag:holdings/eag:dateSet or eag:holdings/eag:dateSet/eag:date/text() or (eag:holdings/eag:dateSet/eag:dateRange/eag:fromDate/text() and eag:holdings/eag:dateSet/eag:dateRange/eag:toDate/text()) or eag:repositorhist/eag:descriptiveNote/eag:p/text() or eag:repositorfound/eag:date/text() or eag:repositorfound/eag:rule/text() or eag:repositorsup/eag:date/text() or eag:repositorsup/eag:rule/text() or eag:adminhierarchy/eag:adminunit/text() or eag:buildinginfo/eag:building/eag:descriptiveNote/eag:p/text() or eag:buildinginfo/eag:repositorarea/eag:num/text() or eag:buildinginfo/eag:lengthshelf/eag:num/text()">
                        <xsl:call-template name="holdings">
                            <xsl:with-param name="id" select="$id"/>
                        </xsl:call-template>
                    </xsl:if>
                    <!-- END LOOP -->
                </div>
            </div>
        </xsl:for-each>
        <div id="afterRepositories">
        </div>
        <xsl:call-template name="otherInformation"/> 		
    </xsl:template>
</xsl:stylesheet>