<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:fn="http://www.w3.org/2005/xpath-functions"  xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xmlns='http://www.w3.org/1999/xhtml' xmlns:ead="urn:isbn:1-931666-22-9" xmlns:xlink="http://www.w3.org/1999/xlink"
	xmlns:ape="http://www.archivesportaleurope.eu/xslt/extensions" 	exclude-result-prefixes="xlink xlink xsi ead ape fn #default">

	<xsl:output method="html" indent="yes" version="4.0" encoding="UTF-8" />
	<xsl:template match="ead:head" mode="#all" />
	<xsl:template match="ead:p" mode="#all">
		<p>
			<xsl:apply-templates mode="#current" />
		</p>
	</xsl:template>
	<xsl:template match="text()" mode="scopecontent">
		<xsl:value-of select="ape:highlight(., 'scopecontent')" disable-output-escaping="yes" />
	</xsl:template>
	<xsl:template match="text()" mode="title">
		<xsl:value-of select="ape:highlight(., 'title')" disable-output-escaping="yes" />
	</xsl:template>
	<xsl:template match="text()" mode="other">
		<xsl:value-of select="ape:highlight(., 'other')" disable-output-escaping="yes" />
	</xsl:template>
	<xsl:template match="text()" mode="alterdate">
		<xsl:value-of select="ape:highlight(., 'alterdate')" disable-output-escaping="yes" />
	</xsl:template>	
	<xsl:template match="text()" mode="notsearchable">
		<xsl:value-of select="." />
	</xsl:template>	
	<xsl:template match="ead:unitdate" mode="#all">
		<xsl:apply-templates mode="#current" />
	</xsl:template>	
	<xsl:template match="ead:lb" mode="#all">
		<xsl:text disable-output-escaping="yes">&lt;br&gt;</xsl:text>
	</xsl:template>
	<xsl:template match="ead:emph[@render='bold']" mode="#all">
		<b>
			<xsl:apply-templates mode="#current" />
		</b>
	</xsl:template>
	<xsl:template match="ead:emph[@render='italic']" mode="#all">
		<i>
			<xsl:apply-templates mode="#current" />
		</i>
	</xsl:template>
	<xsl:template match="ead:list[@type='ordered']" mode="#all">
		<p>
			<ol>
				<xsl:apply-templates mode="#current" />
			</ol>
		</p>
	</xsl:template>
	<xsl:template match="ead:list[@type='marked'] | ead:list[not(@type)]" mode="#all">
		<p>
			<ul>
				<xsl:apply-templates mode="#current" />
			</ul>
		</p>
	</xsl:template>
	<xsl:template match="ead:item" mode="#all">
		<li>
			<xsl:apply-templates mode="#current" />
		</li>
	</xsl:template>
	<xsl:template match="ead:dao"  mode="#all">
		<xsl:variable name="href" select="./@xlink:href" />
				<a href="{$href}" target="_blank">
				<xsl:variable name="dao.title">
					<xsl:choose>
						<xsl:when test="./@xlink:title">
							<xsl:value-of select="./@xlink:title" />
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="ape:resource('eadcontent.dao.notitle')" />
						</xsl:otherwise>
					</xsl:choose>
				</xsl:variable>
					<xsl:choose>
						<xsl:when test="./@xlink:role">
							<xsl:variable name="type" select="fn:lower-case(./@xlink:role)"/>
							<span class="icon_dao_type_{$type}" title="{$dao.title}">
								{$dao.title}
							</span>
						</xsl:when>
						<xsl:otherwise>
							<span class="icon_dao_type_unspecified" title="{$dao.title}">
								{$dao.title}
							</span>
						</xsl:otherwise>
					</xsl:choose>
				</a>
	</xsl:template>

	<xsl:template match="ead:dao" mode="thumbnail">
		<xsl:variable name="href" select="./@xlink:href" />
		<img width="200px" src="{$href}" />
	</xsl:template>

	<xsl:template match="ead:extref" mode="#all">
		<xsl:variable name="href" select="./@xlink:href" />
		<xsl:variable name="prefix" select="$eadcontent.extref.prefix" />
		<xsl:choose>
			<xsl:when test="starts-with($href,'http')">
				<a href="{$href}" target="_blank">
					<xsl:choose>
						<xsl:when test="./@xlink:title">
							<xsl:value-of select="./@xlink:title" />
						</xsl:when>
						<xsl:otherwise>
							<xsl:choose>
								<xsl:when test="text()">
									<xsl:value-of select="text()" />
									<xsl:text> </xsl:text>
								</xsl:when>
								<xsl:otherwise>
									<xsl:variable name="extref.notitle" select="ape:resource('eadcontent.extref.notitle')" />
									<span class="icon_notitle" title="{$extref.notitle}">
										{$extref.notitle}
									</span>
								</xsl:otherwise>
							</xsl:choose>
						</xsl:otherwise>
					</xsl:choose>
				</a>
			</xsl:when>
			<xsl:otherwise>
				<xsl:choose>
					<xsl:when test="ape:linked($href) = 'indexed'">
						<a href="{$prefix}&amp;eadid={$href}" target="{$href}">
							<xsl:choose>
								<xsl:when test="./@xlink:title">
									<xsl:value-of select="./@xlink:title" />
								</xsl:when>
								<xsl:when test="text()">
									<xsl:value-of select="text()" />
								</xsl:when>
								<xsl:otherwise>
									<xsl:variable name="extref.notitle" select="ape:resource('eadcontent.extref.notitle')" />
									<span class="icon_notitle"  title="{$extref.notitle}">
										{$extref.notitle}
									</span>
								</xsl:otherwise>
							</xsl:choose>
						</a>
					</xsl:when>
					<xsl:when test="ape:linked($href) = 'notindexed' ">
						<xsl:variable name="extref.warning" select="ape:resource('error.user.second.display.notindexed')"/>
						<a href="javascript:void(0)" onclick="window.alert('{$extref.warning}')">
							<xsl:choose>
								<xsl:when test="./@xlink:title">
									<xsl:value-of select="./@xlink:title" />
								</xsl:when>
								<xsl:when test="text()">
									<xsl:value-of select="text()" />
								</xsl:when>
								<xsl:otherwise>
									<xsl:variable name="extref.notitle" select="ape:resource('eadcontent.extref.notitle')" />
									<span class="icon_notitle" title="{$extref.notitle}">
										{$extref.notitle}
									</span>
								</xsl:otherwise>
							</xsl:choose>
						</a>
					</xsl:when>
					<xsl:when test="ape:linked($href) = 'notavailable'">
							<xsl:choose>
								<xsl:when test="./@xlink:title">
									<xsl:value-of select="./@xlink:title" />
								</xsl:when>
								<xsl:when test="text()">
									<xsl:value-of select="text()" />
								</xsl:when>
								<xsl:otherwise>
									<xsl:variable name="extref.notitle" select="ape:resource('eadcontent.extref.notitle')" />
									<span class="icon_notitle" title="{$extref.notitle}">
										{$extref.notitle}
									</span>
								</xsl:otherwise>
							</xsl:choose>
					</xsl:when>
					<xsl:otherwise>
						<xsl:if test="text()">
							<xsl:value-of select="text()" />
						</xsl:if>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:otherwise>
		</xsl:choose>

	</xsl:template>

	<xsl:template match="ead:table" mode="#all">
		<table>
			<xsl:if test="./ead:head">
				<caption>
					<i>
						<xsl:apply-templates select="./ead:head/child::node()" mode="#current" />
					</i>
				</caption>
			</xsl:if>
			<xsl:apply-templates mode="#current" />
		</table>
	</xsl:template>
	<xsl:template match="ead:tgroup" mode="#all">
		<xsl:apply-templates mode="#current" />
	</xsl:template>
	<xsl:template match="ead:thead" mode="#all">
		<thead>
			<xsl:apply-templates mode="#current">
				<xsl:with-param name="type" select="'head'" />
			</xsl:apply-templates>
		</thead>
	</xsl:template>
	<xsl:template match="ead:tbody" mode="#all">
		<tbody>
			<xsl:apply-templates mode="#current">
				<xsl:with-param name="type" select="'body'" />
			</xsl:apply-templates>
		</tbody>
	</xsl:template>
	<xsl:template match="ead:row" mode="#all">
		<xsl:param name="type" />
		<tr>
			<xsl:apply-templates mode="#current">
				<xsl:with-param name="type" select="$type" />
			</xsl:apply-templates>
		</tr>
	</xsl:template>
	<xsl:template match="ead:entry" mode="#all">
		<xsl:param name="type" />
		<xsl:choose>
			<xsl:when test="$type = 'head'">
				<th>
					<xsl:apply-templates mode="#current" />
				</th>
			</xsl:when>
			<xsl:otherwise>
				<td>
					<xsl:apply-templates mode="#current" />
				</td>
			</xsl:otherwise>
		</xsl:choose>

	</xsl:template>
	<xsl:template match="ead:corpname" mode="#all">
		<p>
			<xsl:apply-templates mode="#current" />
		</p>
	</xsl:template>
	<xsl:template match="ead:persname" mode="#all">
		<p>
			<xsl:apply-templates mode="#current" />
		</p>
	</xsl:template>
	<xsl:template match="ead:famname" mode="#all">
		<p>
			<xsl:apply-templates mode="#current" />
		</p>
	</xsl:template>
	<xsl:template match="ead:name" mode="#all">
		<p>
			<xsl:apply-templates mode="#current" />
		</p>
	</xsl:template>
	<xsl:template match="ead:geogname" mode="#all">
		<p>
			<xsl:apply-templates mode="#current" />
		</p>
	</xsl:template>
	<xsl:template match="ead:function" mode="#all">
		<p>
			<xsl:apply-templates mode="#current" />
		</p>
	</xsl:template>
	<xsl:template match="ead:title" mode="#all">
		<p>
			<xsl:apply-templates mode="#current" />
		</p>
	</xsl:template>
	<xsl:template match="ead:subject" mode="#all">
		<p>
			<xsl:apply-templates mode="#current" />
		</p>
	</xsl:template>
	<xsl:template match="ead:occupation" mode="#all">
		<p>
			<xsl:apply-templates mode="#current" />
		</p>
	</xsl:template>
	<xsl:template match="ead:address" mode="#all">
		<xsl:text> (</xsl:text>
		<xsl:variable name="addressCount" select="count(ead:addressline)"></xsl:variable>
		<xsl:for-each select="ead:addressline">
			<xsl:value-of select="." />
			<xsl:if test="$addressCount > position()">
				<xsl:text>, </xsl:text>
			</xsl:if>
		</xsl:for-each>
		<xsl:text>)</xsl:text>
	</xsl:template>
</xsl:stylesheet>