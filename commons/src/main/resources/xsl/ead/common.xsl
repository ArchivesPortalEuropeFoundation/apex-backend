<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:fn="http://www.w3.org/2005/xpath-functions"  xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xmlns='http://www.w3.org/1999/xhtml' xmlns:ead="urn:isbn:1-931666-22-9" xmlns:xlink="http://www.w3.org/1999/xlink"
	xmlns:ape="http://www.archivesportaleurope.eu/xslt/extensions" 	exclude-result-prefixes="xlink xlink xsi ead ape fn #default">

	<xsl:output method="html" indent="yes" version="4.0" encoding="UTF-8" />
	<xsl:template match="ead:head" mode="#all">
		<h4>
			<xsl:apply-templates mode="#current" />
		</h4>
	</xsl:template>
	<xsl:template match="ead:p" mode="#all">
		<p>
			<xsl:apply-templates mode="#current" />
		</p>
	</xsl:template>
	<xsl:template match="text()" mode="scopecontent">
		<xsl:value-of select="ape:highlight(., 'scopecontent')" disable-output-escaping="yes" /><xsl:text> </xsl:text>
	</xsl:template>
	<xsl:template match="text()" mode="title">
		<xsl:value-of select="ape:highlight(., 'title')" disable-output-escaping="yes" />
	</xsl:template>
	<xsl:template match="text()" mode="other">
		<xsl:value-of select="ape:highlight(., 'other')" disable-output-escaping="yes" /><xsl:text> </xsl:text>
	</xsl:template>
	<xsl:template match="text()" mode="alterdate">
		<xsl:value-of select="ape:highlight(., 'alterdate')" disable-output-escaping="yes" />
	</xsl:template>	
	<xsl:template match="text()" mode="notsearchable">
		<xsl:value-of select="." /><xsl:text> </xsl:text>
	</xsl:template>	
	<xsl:template match="ead:unitdate" mode="#all">
		<xsl:apply-templates mode="#current" /><xsl:text> </xsl:text>
	</xsl:template>	
	<xsl:template match="ead:lb" mode="#all">
		<xsl:text disable-output-escaping="yes">&lt;br&gt;</xsl:text>
	</xsl:template>
	<xsl:template match="ead:emph[@render='bold']" mode="#all">
		<b>
			<xsl:apply-templates mode="#current" />
		</b><xsl:text> </xsl:text>
	</xsl:template>
	<xsl:template match="ead:emph[@render='italic']" mode="#all">
		<i>
			<xsl:apply-templates mode="#current" />
		</i><xsl:text> </xsl:text>
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
								<xsl:value-of select="$dao.title" />
							</span>
						</xsl:when>
						<xsl:otherwise>
							<span class="icon_dao_type_unspecified" title="{$dao.title}">
								<xsl:value-of select="$dao.title" />
							</span>
						</xsl:otherwise>
					</xsl:choose>
				</a><xsl:text> </xsl:text>
	</xsl:template>

	<xsl:template match="ead:dao" mode="thumbnail">
		<xsl:variable name="href" select="./@xlink:href" />
		<img width="200px" src="{$href}" />
	</xsl:template>

	<xsl:template match="ead:extref" mode="otherfindingaids">
		<xsl:variable name="href" select="./@xlink:href" />
		<xsl:variable name="prefix" select="$eadcontent.extref.prefix" />
		<xsl:choose>
			<xsl:when test="starts-with($href,'http')">
				<div class="linkButton">
					<xsl:choose>
						<xsl:when test="./@xlink:title and text()">
							<xsl:variable name="title" select="./@xlink:title" />
							<xsl:variable name="initTitle" select="ape:resource('seconddisplay.view.fa')" />
							<a href="{$href}" target="_blank" title="{$initTitle} '{$title}'">
								<xsl:value-of select="ape:resource('seconddisplay.view.fa')" />
								<xsl:text> '</xsl:text>
								<xsl:value-of select="text()" />
								<xsl:text>' </xsl:text>
								<span class="icon_new_window"><xsl:text> </xsl:text></span>
							</a>
						</xsl:when>
						<xsl:when test="./@xlink:title">
							<a href="{$href}" target="_blank">
								<xsl:value-of select="ape:resource('seconddisplay.view.fa')" />
								<xsl:text> '</xsl:text>
								<xsl:value-of select="./@xlink:title" />
								<xsl:text>' </xsl:text>
								<span class="icon_new_window"><xsl:text> </xsl:text></span>
							</a>
						</xsl:when>
						<xsl:when test="text()">
							<a href="{$href}" target="_blank">
								<xsl:value-of select="ape:resource('seconddisplay.view.fa')" />
								<xsl:text> '</xsl:text>
								<xsl:value-of select="text()" />
								<xsl:text>' </xsl:text>
								<span class="icon_new_window"><xsl:text> </xsl:text></span>
							</a>
						</xsl:when>
						<xsl:otherwise>
							<a href="{$href}" target="_blank">
								<xsl:value-of select="ape:resource('seconddisplay.view.fa')" />
								<span class="icon_new_window"><xsl:text> </xsl:text></span>
							</a>
						</xsl:otherwise>
					</xsl:choose>
				</div>
			</xsl:when>
			<xsl:otherwise>
				<xsl:choose>
					<xsl:when test="ape:linked($href) = 'indexed'">
						<div class="linkButton">
							<xsl:choose>
								<xsl:when test="./@xlink:title and text()">
									<xsl:variable name="title" select="./@xlink:title" />
									<xsl:variable name="initTitle" select="ape:resource('seconddisplay.view.fa')" />
									<a href="{$prefix}/{$href}" target="{$href}" title="{$initTitle} '{$title}'">
										<xsl:value-of select="ape:resource('seconddisplay.view.fa')" />
										<xsl:text> '</xsl:text>
										<xsl:value-of select="text()" />
										<xsl:text>' </xsl:text>
										<span class="icon_new_window"><xsl:text> </xsl:text></span>
									</a>
								</xsl:when>
								<xsl:when test="./@xlink:title">
									<a href="{$prefix}/{$href}" target="{$href}">
										<xsl:value-of select="ape:resource('seconddisplay.view.fa')" />
										<xsl:text> '</xsl:text>
										<xsl:value-of select="./@xlink:title" />
										<xsl:text>' </xsl:text>
										<span class="icon_new_window"><xsl:text> </xsl:text></span>
									</a>
								</xsl:when>
								<xsl:when test="text()">
									<a href="{$prefix}/{$href}" target="{$href}">
										<xsl:value-of select="ape:resource('seconddisplay.view.fa')" />
										<xsl:text> '</xsl:text>
										<xsl:value-of select="text()" />
										<xsl:text>' </xsl:text>
										<span class="icon_new_window"><xsl:text> </xsl:text></span>
									</a>
								</xsl:when>
								<xsl:otherwise>
									<a href="{$prefix}/{$href}" target="{$href}">
										<xsl:value-of select="ape:resource('seconddisplay.view.fa')" />
										<span class="icon_new_window"><xsl:text> </xsl:text></span>
									</a>
								</xsl:otherwise>
							</xsl:choose>
						</div>
					</xsl:when>
					<xsl:when test="ape:linked($href) = 'notindexed' ">
						<xsl:variable name="extref.warning" select="ape:resource('error.user.second.display.notindexed')"/>
						<div class="linkButton">
							<xsl:choose>
								<xsl:when test="./@xlink:title and text()">
									<xsl:variable name="title" select="./@xlink:title" />
									<xsl:variable name="initTitle" select="ape:resource('seconddisplay.view.fa')" />
									<a href="javascript:void(0)" onclick="window.alert('{$extref.warning}')" class="notIndexed" title="{$initTitle} '{$title}'">
										<xsl:value-of select="ape:resource('seconddisplay.view.fa')" />
										<xsl:text> '</xsl:text>
										<xsl:value-of select="text()" />
										<xsl:text>' </xsl:text>
										<span class="icon_new_window"><xsl:text> </xsl:text></span>
									</a>
								</xsl:when>
								<xsl:when test="./@xlink:title">
									<a href="javascript:void(0)" onclick="window.alert('{$extref.warning}')" class="notIndexed">
										<xsl:value-of select="ape:resource('seconddisplay.view.fa')" />
										<xsl:text> '</xsl:text>
										<xsl:value-of select="./@xlink:title" />
										<xsl:text>' </xsl:text>
										<span class="icon_new_window"><xsl:text> </xsl:text></span>
									</a>
								</xsl:when>
								<xsl:when test="text()">
									<a href="javascript:void(0)" onclick="window.alert('{$extref.warning}')" class="notIndexed">
										<xsl:value-of select="ape:resource('seconddisplay.view.fa')" />
										<xsl:text> '</xsl:text>
										<xsl:value-of select="text()" />
										<xsl:text>' </xsl:text>
										<span class="icon_new_window"><xsl:text> </xsl:text></span>
									</a>
								</xsl:when>
								<xsl:otherwise>
									<a href="javascript:void(0)" onclick="window.alert('{$extref.warning}')" class="notIndexed">
										<xsl:value-of select="ape:resource('seconddisplay.view.fa')" />
										<span class="icon_new_window"><xsl:text> </xsl:text></span>
									</a>
								</xsl:otherwise>
							</xsl:choose>
						</div>
					</xsl:when>
					<xsl:otherwise>
						<div class="linkButton">
							<xsl:choose>
								<xsl:when test="./@xlink:title and text()">
									<xsl:variable name="title" select="./@xlink:title" />
									<xsl:variable name="initTitle" select="ape:resource('seconddisplay.view.fa')" />
									<div class="nolink">
										<xsl:value-of select="ape:resource('seconddisplay.view.fa')" />
										<xsl:text> '</xsl:text>
										<xsl:value-of select="text()" />
										<xsl:text>' </xsl:text>
									</div>
								</xsl:when>
								<xsl:when test="./@xlink:title">
									<div class="nolink">
										<xsl:value-of select="ape:resource('seconddisplay.view.fa')" />
										<xsl:text> '</xsl:text>
										<xsl:value-of select="./@xlink:title" />
										<xsl:text>' </xsl:text>
									</div>
								</xsl:when>
								<xsl:when test="text()">
									<div class="nolink">
										<xsl:value-of select="ape:resource('seconddisplay.view.fa')" />
										<xsl:text> '</xsl:text>
										<xsl:value-of select="text()" />
										<xsl:text>' </xsl:text>
									</div>
								</xsl:when>
								<xsl:otherwise>
									<div class="nolink">
										<xsl:value-of select="ape:resource('seconddisplay.view.fa')" />
									</div>
								</xsl:otherwise>
							</xsl:choose>
						</div>			
					</xsl:otherwise>
				</xsl:choose>
			</xsl:otherwise>
		</xsl:choose><xsl:text> </xsl:text>
	</xsl:template>

	<xsl:template match="ead:extref" mode="other notsearchable scopecontent">
		<xsl:variable name="href" select="./@xlink:href" />
		<xsl:variable name="prefix" select="$eadcontent.extref.prefix" />
		<xsl:choose>
			<xsl:when test="starts-with($href,'http')">
				<xsl:choose>
					<xsl:when test="./@xlink:title and text()">
						<xsl:variable name="title" select="./@xlink:title" />
						<a href="{$href}" target="_blank" title="{$title}">
							<xsl:value-of select="text()" />
						</a>
					</xsl:when>
					<xsl:when test="./@xlink:title">
						<a href="{$href}" target="_blank">
							<xsl:value-of select="./@xlink:title" />
						</a>
					</xsl:when>
					<xsl:when test="text()">
						<a href="{$href}" target="_blank">
							<xsl:value-of select="text()" />
						</a>
					</xsl:when>
					<xsl:otherwise>
						<a href="{$href}" target="_blank">
							<xsl:variable name="extref.notitle" select="ape:resource('eadcontent.extref.notitle')" />
							<span class="icon_notitle" title="{$extref.notitle}">
								<xsl:value-of select="$extref.notitle" />
							</span>
						</a>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
			<xsl:otherwise>
				<xsl:choose>
					<xsl:when test="ape:linked($href) = 'indexed'">
						<xsl:choose>
							<xsl:when test="./@xlink:title and text()">
								<xsl:variable name="title" select="./@xlink:title" />
								<a href="{$prefix}&amp;eadid={$href}" target="{$href}" title="{$title}">
									<xsl:value-of select="text()" />
								</a>
							</xsl:when>
							<xsl:when test="./@xlink:title">
								<a href="{$prefix}&amp;eadid={$href}" target="{$href}">
									<xsl:value-of select="./@xlink:title" />
								</a>
							</xsl:when>
							<xsl:when test="text()">
								<a href="{$prefix}&amp;eadid={$href}" target="{$href}">
									<xsl:value-of select="text()" />
								</a>
							</xsl:when>
							<xsl:otherwise>
								<a href="{$prefix}&amp;eadid={$href}" target="{$href}">
									<xsl:variable name="extref.notitle" select="ape:resource('eadcontent.extref.notitle')" />
									<span class="icon_notitle" title="{$extref.notitle}">
										<xsl:value-of select="$extref.notitle" />
									</span>
								</a>
							</xsl:otherwise>
						</xsl:choose>
					</xsl:when>
					<xsl:when test="ape:linked($href) = 'notindexed' ">
						<xsl:variable name="extref.warning" select="ape:resource('error.user.second.display.notindexed')"/>
						<xsl:choose>
							<xsl:when test="./@xlink:title and text()">
								<xsl:variable name="title" select="./@xlink:title" />
								<a href="javascript:void(0)" onclick="window.alert('{$extref.warning}')" title="{$title}">
									<xsl:value-of select="text()" />
								</a>
							</xsl:when>
							<xsl:when test="./@xlink:title">
								<a href="javascript:void(0)" onclick="window.alert('{$extref.warning}')">
									<xsl:value-of select="./@xlink:title" />
								</a>
							</xsl:when>
							<xsl:when test="text()">
								<a href="javascript:void(0)" onclick="window.alert('{$extref.warning}')">
									<xsl:value-of select="text()" />
								</a>
							</xsl:when>
							<xsl:otherwise>
								<a href="javascript:void(0)" onclick="window.alert('{$extref.warning}')">
									<xsl:variable name="extref.notitle" select="ape:resource('eadcontent.extref.notitle')" />
									<span class="icon_notitle" title="{$extref.notitle}">
										<xsl:value-of select="$extref.notitle" />
									</span>
								</a>
							</xsl:otherwise>
						</xsl:choose>
					</xsl:when>
					<xsl:when test="ape:linked($href) = 'notavailable'">
						<xsl:choose>
							<xsl:when test="./@xlink:title and text()">
								<xsl:value-of select="text()" />
							</xsl:when>
							<xsl:when test="./@xlink:title">
								<xsl:value-of select="./@xlink:title" />
							</xsl:when>
							<xsl:when test="text()">
								<xsl:value-of select="text()" />
							</xsl:when>
							<xsl:otherwise>
								<xsl:variable name="extref.notitle" select="ape:resource('eadcontent.extref.notitle')" />
								<span class="icon_notitle" title="{$extref.notitle}">
									<xsl:value-of select="$extref.notitle" />
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
		</xsl:choose><xsl:text> </xsl:text>
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