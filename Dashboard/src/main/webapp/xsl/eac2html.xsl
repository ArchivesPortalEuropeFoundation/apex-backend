<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xmlns='http://www.w3.org/1999/xhtml' xmlns:cpf="urn:isbn:1-931666-33-4" xmlns:xlink="http://www.w3.org/1999/xlink"
	exclude-result-prefixes="xlink xsl xsi cpf">
	
    <xsl:output method="html" indent="yes" version="4.0" encoding="UTF-8" />
	
    <xsl:template match="/">
		<div id="body">
			<div>
                recordId: <xsl:value-of select="/cpf:eac-cpf/cpf:control/cpf:recordId" />
			</div>
            <div>
				language: <xsl:value-of select="/cpf:eac-cpf/cpf:control/cpf:languageDeclaration/cpf:language" />
			</div>
            <div>
				maintenanceHistory:
                <ul>
                    <li>eventType: <xsl:value-of select="/cpf:eac-cpf/cpf:control/cpf:maintenanceHistory/cpf:maintenanceEvent/cpf:eventType" /></li>
                    <li>eventDateTime: <xsl:value-of select="/cpf:eac-cpf/cpf:control/cpf:maintenanceHistory/cpf:maintenanceEvent/cpf:eventDateTime" /></li>
                    <li>agentType: <xsl:value-of select="/cpf:eac-cpf/cpf:control/cpf:maintenanceHistory/cpf:maintenanceEvent/cpf:agentType" /></li>
                    <li>agent: <xsl:value-of select="/cpf:eac-cpf/cpf:control/cpf:maintenanceHistory/cpf:maintenanceEvent/cpf:agent" /></li>
                </ul>
			</div>
            <div>
                cpfDescription:
                <ul>
                    <li>entityType: <xsl:value-of select="/cpf:eac-cpf/cpf:cpfDescription/cpf:identity/cpf:entityType" /></li>
                    <li>nameEntry: <xsl:value-of select="/cpf:eac-cpf/cpf:cpfDescription/cpf:identity/cpf:nameEntry/cpf:part" /></li>
                    <li>description:
                        <ul>
                            <li>existsDate: <xsl:value-of select="/cpf:eac-cpf/cpf:cpfDescription/cpf:description/cpf:existDates/cpf:date" /></li>
                            <li>place: <xsl:value-of select="/cpf:eac-cpf/cpf:cpfDescription/cpf:description/cpf:place/cpf:placeEntry" /></li>
                            <li>occupation: <xsl:value-of select="/cpf:eac-cpf/cpf:cpfDescription/cpf:description/cpf:occupation/cpf:term" /></li>
                            <li>bioghist: <xsl:value-of select="/cpf:eac-cpf/cpf:cpfDescription/cpf:description/cpf:biogHist" /></li>
                        </ul>
                    </li>
                    <li>relations:
                        <ul>
                            <xsl:for-each select="/cpf:eac-cpf/cpf:cpfDescription/cpf:relations/cpf:resourceRelation">
                                <li>relationEntry: <xsl:value-of select="cpf:relationEntry" />
                                    <xsl:if test="@xlink:href">, link:
                                        <a>
                                            <xsl:attribute name="href">
                                                <xsl:value-of select="@xlink:href"/>
                                            </xsl:attribute>
                                            <xsl:value-of select="@xlink:href"/>
                                        </a>
                                    </xsl:if>
                                </li>
                            </xsl:for-each>
                        </ul>
                    </li>
                </ul>
            </div>
            

		</div>
	</xsl:template>


</xsl:stylesheet>