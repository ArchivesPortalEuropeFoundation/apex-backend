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
		<xsl:value-of select="fn:normalize-space(ape:highlight(., 'title'))" disable-output-escaping="yes" />
	</xsl:template>
	<xsl:template match="text()" mode="other">
		<xsl:value-of select="ape:highlight(., 'other')" disable-output-escaping="yes" /><xsl:text> </xsl:text>
	</xsl:template>
	<xsl:template match="text()" mode="otherwithoutwhitespace">
		<xsl:value-of select="ape:highlight(., 'otherwithoutwhitespace')" disable-output-escaping="yes" />
	</xsl:template>
	<xsl:template match="text()" mode="otherwithcoma">
		<xsl:value-of select="fn:normalize-space(ape:highlight(., 'otherwithcoma'))" disable-output-escaping="yes" />
		<!-- Extent option -->
		<xsl:if test="string-length(../@unit)>0">
			<xsl:text> (</xsl:text><xsl:value-of select="../@unit"/><xsl:text>)</xsl:text>
		</xsl:if>	
		<xsl:if test="position() != last()">
		   <xsl:text>, </xsl:text>
		</xsl:if>
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
									<xsl:choose>
										<xsl:when test='$type eq "text" or $type eq "image" or $type eq "sound" or $type eq "video" or $type eq "3d"'>
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
					<xsl:when test="ape:linked($href) = 'indexed-preview'">
						<xsl:variable name="extref.warning" select="ape:resource('error.user.second.display.indexed.preview')"/>
						<div class="linkButton">
							<xsl:choose>
								<xsl:when test="./@xlink:title and text()">
									<xsl:variable name="title" select="./@xlink:title" />
									<xsl:variable name="initTitle" select="ape:resource('seconddisplay.view.fa')" />
									<a href="javascript:void(0)" onclick="window.alert('{$extref.warning}')" title="{$initTitle} '{$title}'">
										<xsl:value-of select="ape:resource('seconddisplay.view.fa')" />
										<xsl:text> '</xsl:text>
										<xsl:value-of select="text()" />
										<xsl:text>' </xsl:text>
										<span class="icon_new_window"><xsl:text> </xsl:text></span>
									</a>
								</xsl:when>
								<xsl:when test="./@xlink:title">
									<a href="javascript:void(0)" onclick="window.alert('{$extref.warning}')">
										<xsl:value-of select="ape:resource('seconddisplay.view.fa')" />
										<xsl:text> '</xsl:text>
										<xsl:value-of select="./@xlink:title" />
										<xsl:text>' </xsl:text>
										<span class="icon_new_window"><xsl:text> </xsl:text></span>
									</a>
								</xsl:when>
								<xsl:when test="text()">
									<a href="javascript:void(0)" onclick="window.alert('{$extref.warning}')">
										<xsl:value-of select="ape:resource('seconddisplay.view.fa')" />
										<xsl:text> '</xsl:text>
										<xsl:value-of select="text()" />
										<xsl:text>' </xsl:text>
										<span class="icon_new_window"><xsl:text> </xsl:text></span>
									</a>
								</xsl:when>
								<xsl:otherwise>
									<a href="javascript:void(0)" onclick="window.alert('{$extref.warning}')">
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
				<th class="thEntry">
					<xsl:apply-templates mode="#current" />
				</th>
			</xsl:when>
			<xsl:otherwise>
				<td class="tdEntry">
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
	  <div class="defaultlayout">	
		<xsl:text> (</xsl:text>
		<xsl:variable name="addressCount" select="count(ead:addressline)"></xsl:variable>
		<xsl:for-each select="ead:addressline">
			<xsl:value-of select="." />
			<xsl:if test="$addressCount > position()">
				<xsl:text>, </xsl:text>
			</xsl:if>
		</xsl:for-each>
		<xsl:text>)</xsl:text>
	 </div>
	</xsl:template>
	<xsl:template name="langmaterial">
		<h2>
			<xsl:value-of select="ape:resource('eadcontent.language')" />
		</h2>
		<div class="ead-content">
		    <xsl:for-each select="ead:langmaterial">
				<xsl:apply-templates mode="other"/>
			</xsl:for-each>
		</div>
	</xsl:template>
	<xsl:template name="repository">
		<h2>
			<xsl:value-of select="ape:resource('eadcontent.repository')" />
		</h2>
		<div class="ead-content">
			<xsl:for-each select="ead:repository">
				<p>
					<xsl:apply-templates  mode="other"/><xsl:text> </xsl:text>
				</p>
			</xsl:for-each>
		</div>
	</xsl:template>
	<xsl:template name="physloc">
		<h2>
			<xsl:value-of select="ape:resource('eadcontent.physloc')" />
		</h2>
		<div class="ead-content">
		  <xsl:for-each select="ead:physloc">
		    <xsl:if test="./text()">
			 	<xsl:if test="./@label" >
				    <xsl:value-of select="./@label" />
				    <xsl:text>: </xsl:text>
			    </xsl:if>
			  <xsl:apply-templates mode="otherwithoutwhitespace"/>
			  <xsl:if test="position() != last() and not(./text()[position()+1=last()])">
			      <xsl:text>, </xsl:text>
			  </xsl:if>
			 </xsl:if> 
		   </xsl:for-each>
		</div>
	</xsl:template>
	<xsl:template name="materialspec">
		<h2>
			<xsl:value-of select="ape:resource('eadcontent.materialspec')" />
		</h2>
		<div class="ead-content">
			<xsl:for-each select="ead:materialspec">
				<xsl:apply-templates  mode="otherwithoutwhitespace"/>
				<xsl:if test="position() != last()">
					<xsl:text>, </xsl:text>
				</xsl:if>
			</xsl:for-each>
		</div>
	</xsl:template>
	<xsl:template name="physfacet">
		<h2>
			<xsl:value-of select="ape:resource('eadcontent.physfacet')" />
		</h2>
		<div class="ead-content">
			<xsl:for-each select="ead:physfacet">
				<xsl:apply-templates  mode="otherwithoutwhitespace" />
				<xsl:if test="position() != last()">
					<xsl:text>, </xsl:text>
				</xsl:if>
			</xsl:for-each>
		</div>
	</xsl:template>
	<!-- Extent option -->
	<xsl:template name="extent">
		<h2>
			<xsl:value-of select="ape:resource('eadcontent.extent')" />
		</h2>
		<div class="ead-content">
		  <xsl:for-each select="ead:extent">
			<xsl:apply-templates mode="otherwithoutwhitespace"/>
			<xsl:if test="./@unit"> 
				<xsl:text> (</xsl:text><xsl:value-of select="./@unit"/><xsl:text>)</xsl:text>
			</xsl:if>
			<xsl:if test="position() != last()">
				<xsl:text>, </xsl:text>
			  </xsl:if>
		   </xsl:for-each>			 
		</div>
	</xsl:template>
	<xsl:template name="genreform">
		<h2>
			<xsl:value-of select="ape:resource('eadcontent.genreform')" />
		</h2>
		<div class="ead-content">
			<xsl:for-each select="ead:genreform">
				<xsl:apply-templates  mode="otherwithoutwhitespace"/>
				<xsl:if test="position() != last()">
					<xsl:text>, </xsl:text>
				</xsl:if>
			</xsl:for-each>
		</div>
	</xsl:template>
	<xsl:template name="dimensions">
		<h2>
			<xsl:value-of select="ape:resource('eadcontent.dimensions')" />
		</h2>
		<div class="ead-content">
			<xsl:for-each select="ead:dimensions">
				<xsl:apply-templates  mode="otherwithoutwhitespace"/>
				<xsl:if test="position() != last()">
					<xsl:text>, </xsl:text>
				</xsl:if>
			</xsl:for-each>
		</div>
	</xsl:template>
    <xsl:template name="physdescText">
		<h2>
			<xsl:value-of select="ape:resource('eadcontent.physdesc')" />
		</h2>
		<div class="ead-content">
		 <xsl:apply-templates mode="otherwithcoma"/>    
		</div>
	</xsl:template>
	<xsl:template name="note">
		<h2>
			<xsl:value-of select="ape:resource('eadcontent.note')" />
		</h2>
		<div class="ead-content">
			<xsl:for-each select="ead:note">
				<xsl:apply-templates  mode="other"/>
			</xsl:for-each>
		</div>
	</xsl:template>
	<xsl:template name="dao" >
		<h2>
			<xsl:value-of select="ape:resource('eadcontent.dao')" />
		</h2>
		<div class="ead-content">
			<ul class="daolist">
			<xsl:for-each select='ead:dao[@xlink:title!="thumbnail" or not(@xlink:title)]'>
				<xsl:variable name="linkPosition" select="position()" />
				<xsl:variable name="thumbnail" select='parent::node()/ead:dao[@xlink:title="thumbnail"]' />
				<xsl:variable name="href" select="./@xlink:href" />
				<li><a href="{$href}" target="_blank">
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
					<!-- if thumbnail exists -->
					<xsl:choose>
						<xsl:when test="$thumbnail">
							<xsl:choose>
								<xsl:when test="count($thumbnail) >= $linkPosition">
									<xsl:variable name="thumbnailHref" select="$thumbnail[$linkPosition]/@xlink:href" />
									<img width="200px" src="{$thumbnailHref}"  alt="{$dao.title}" title="{$dao.title}"/>
								</xsl:when>
								<xsl:otherwise>
									<xsl:variable name="thumbnailHref" select="$thumbnail[1]/@xlink:href" />
									<img width="200px" src="{$thumbnailHref}" alt="{$dao.title}"  title="{$dao.title}"/>
								</xsl:otherwise>
							</xsl:choose>
						</xsl:when>
						<xsl:otherwise>
							<xsl:choose>
								<xsl:when test="./@xlink:role">
									<xsl:variable name="type" select="fn:lower-case(./@xlink:role)"></xsl:variable>
									<xsl:choose>
										<xsl:when test='$type eq "text" or $type eq "image" or $type eq "sound" or $type eq "video" or $type eq "3d"'>
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
								</xsl:when>
								<xsl:otherwise>
									<span class="icon_dao_type_unspecified" title="{$dao.title}">
										<xsl:value-of select="$dao.title" />
									</span>
								</xsl:otherwise>
							</xsl:choose>
						</xsl:otherwise>
					</xsl:choose>
				</a></li>
			</xsl:for-each>
			</ul>
		</div>
	</xsl:template>
	<xsl:template name="origination">
		<h2>
			<xsl:value-of select="ape:resource('eadcontent.origination')" />
		</h2>
	   	<div class="ead-content"> 
			<xsl:if test="ead:origination[@label='pre']">
			 	<br/>
				<p> 
					<b>
						<xsl:value-of select="ape:resource('eadcontent.origination.pre')" />
						:
					</b>
				 	<br />
					<xsl:for-each select="ead:origination[@label='pre']">
						<xsl:apply-templates  mode="other"/>
					</xsl:for-each>
					
				</p>
			</xsl:if>
			<xsl:if test="ead:origination[@label='final']">
			  	<br/>
				<p>
					<b>
						<xsl:value-of select="ape:resource('eadcontent.origination.final')" />
						:
					</b>
				 	<br /> 
					<xsl:for-each select="ead:origination[@label='final']">
						<xsl:apply-templates  mode="other"/>
					</xsl:for-each>
				</p>
			</xsl:if>
			<xsl:if test="ead:origination[@label='organisational unit']">
				<br />
				<p>
					<b>
						<xsl:value-of select="ape:resource('eadcontent.origination.orgunit')" />
						:
					</b>
				    <br /> 
					<xsl:for-each select="ead:origination[@label='organisational unit']">
						<xsl:apply-templates  mode="other"/>
					</xsl:for-each>
				</p>
			</xsl:if>
			<xsl:if
				test="ead:origination[@label!='pre'] and ead:origination[@label!='final'] and ead:origination[@label!='organisational unit']">
				<br />
				<p>
					<xsl:for-each select="ead:origination">
						<xsl:if test="./@label!='pre' and ./@label!='final' and ./@label!='organisational unit'">
							<b>
								<xsl:value-of select="./@label" />
								:
							</b>
							<br />
							<xsl:apply-templates mode="other"/>
						</xsl:if>
					</xsl:for-each>
				</p>
			</xsl:if>
            <xsl:if test="ead:origination[not(@label)]">
                 <br/>
                 <p>  
                  <xsl:for-each select="ead:origination[not(@label)]">
                        <xsl:apply-templates mode="other"/>
                  </xsl:for-each>
                 </p>
            </xsl:if>
	   	</div>
	</xsl:template>

	<xsl:template name="scopecontent">
		<h2>
			<xsl:value-of select="ape:resource('eadcontent.scopecontent')" />
		</h2>
		<div class="ead-content">
			<xsl:for-each select="ead:scopecontent">
				<xsl:apply-templates mode="scopecontent"/>
			</xsl:for-each>
		</div>
	</xsl:template>
	<xsl:template name="bioghist">
		<h2>
			<xsl:value-of select="ape:resource('eadcontent.bioghist')" />
		</h2>
		<div class="ead-content">
			<xsl:for-each select="ead:bioghist">
				<xsl:apply-templates  mode="other"/>
			</xsl:for-each>
		</div>
	</xsl:template>
	<xsl:template name="custodhist">
		<h2>
			<xsl:value-of select="ape:resource('eadcontent.custodhist')" />
		</h2>
		<div class="ead-content">
			<xsl:for-each select="ead:custodhist">
				<xsl:apply-templates  mode="other"/>
			</xsl:for-each>
		</div>
	</xsl:template>
	<xsl:template name="appraisal">
		<h2>
			<xsl:value-of select="ape:resource('eadcontent.appraisal')" />
		</h2>
		<div class="ead-content">
			<xsl:for-each select="ead:appraisal">
				<xsl:apply-templates  mode="other"/>
			</xsl:for-each>
		</div>
	</xsl:template>
	<xsl:template name="processinfo">
		<h2>
			<xsl:value-of select="ape:resource('eadcontent.processinfo')" />
		</h2>
		<div class="ead-content">
			<xsl:for-each select="ead:processinfo">
				<xsl:apply-templates  mode="other"/>
			</xsl:for-each>
		</div>
	</xsl:template>
	<xsl:template name="accruals">
		<h2>
			<xsl:value-of select="ape:resource('eadcontent.accruals')" />
		</h2>
		<div class="ead-content">
			<xsl:for-each select="ead:accruals">
				<xsl:apply-templates  mode="other"/>
			</xsl:for-each>
		</div>
	</xsl:template>
	<xsl:template name="arrangement">
		<h2>
			<xsl:value-of select="ape:resource('eadcontent.arrangement')" />
		</h2>
		<div class="ead-content">
			<xsl:for-each select="ead:arrangement">
				<xsl:apply-templates  mode="other"/>
			</xsl:for-each>
		</div>
	</xsl:template>
	<xsl:template name="fileplan">
		<h2>
			<xsl:value-of select="ape:resource('eadcontent.fileplan')" />
		</h2>
		<div class="ead-content">
			<xsl:for-each select="ead:fileplan">
				<xsl:apply-templates  mode="other"/>
			</xsl:for-each>
		</div>
	</xsl:template>
	<xsl:template name="accessrestrict">
		<h2>
			<xsl:value-of select="ape:resource('eadcontent.accessrestrict')" />
		</h2>
		<div class="ead-content">
			<xsl:for-each select="ead:accessrestrict">
				<xsl:apply-templates  mode="other"/>
			</xsl:for-each>
		</div>
	</xsl:template>
	<xsl:template name="userestrict">
		<h2>
			<xsl:value-of select="ape:resource('eadcontent.userestrict')" />
		</h2>
		<div class="ead-content">
			<xsl:for-each select="ead:userestrict">
				<xsl:apply-templates  mode="other"/>
			</xsl:for-each>
		</div>
	</xsl:template>
	<xsl:template name="phystech">
		<h2>
			<xsl:value-of select="ape:resource('eadcontent.phystech')" />
		</h2>
		<div class="ead-content">
			<xsl:for-each select="ead:phystech">
				<xsl:apply-templates  mode="other"/>
			</xsl:for-each>
		</div>
	</xsl:template>
	<xsl:template name="otherfindaid">
		<h2>
			<xsl:value-of select="ape:resource('eadcontent.otherfindaid')" />
		</h2>
		<div class="ead-content">
			<xsl:for-each select="ead:otherfindaid">
				<xsl:apply-templates  mode="otherfindingaids"/>
			</xsl:for-each>
		</div>
	</xsl:template>
	<xsl:template name="relatedmaterial">
		<h2>
			<xsl:value-of select="ape:resource('eadcontent.relatedmaterial')" />
		</h2>
		<div class="ead-content">
			<xsl:for-each select="ead:relatedmaterial">
				<xsl:apply-templates  mode="other"/>
			</xsl:for-each>
		</div>
	</xsl:template>
	<xsl:template name="separatedmaterial">
		<h2>
			<xsl:value-of select="ape:resource('eadcontent.separatedmaterial')" />
		</h2>
		<div class="ead-content">
			<xsl:for-each select="ead:separatedmaterial">
				<xsl:apply-templates  mode="other"/>
			</xsl:for-each>
		</div>
	</xsl:template>
	<xsl:template name="altformavail">
		<h2>
			<xsl:value-of select="ape:resource('eadcontent.altformavail')" />
		</h2>
		<div class="ead-content">
			<xsl:for-each select="ead:altformavail">
				<xsl:apply-templates  mode="other"/>
			</xsl:for-each>
		</div>
	</xsl:template>
	<xsl:template name="originalsloc">
		<h2>
			<xsl:value-of select="ape:resource('eadcontent.originalsloc')" />
		</h2>
		<div class="ead-content">
			<xsl:for-each select="ead:originalsloc">
				<xsl:apply-templates  mode="other"/>
			</xsl:for-each>
		</div>
	</xsl:template>
	<xsl:template name="bibliography">
		<h2>
			<xsl:value-of select="ape:resource('eadcontent.bibliography')" />
		</h2>
		<div class="ead-content">
			<xsl:for-each select="ead:bibliography">
				<xsl:apply-templates  mode="other"/>
			</xsl:for-each>
		</div>
	</xsl:template>
	<xsl:template name="prefercite">
		<h2>
			<xsl:value-of select="ape:resource('eadcontent.prefercite')" />
		</h2>
		<div class="ead-content">
			<xsl:for-each select="ead:prefercite">
				<xsl:apply-templates  mode="other"/>
			</xsl:for-each>
		</div>
	</xsl:template>
	<xsl:template name="odd">
		<h2>
			<xsl:value-of select="ape:resource('eadcontent.odd')" />
		</h2>
		<div class="ead-content">
			<xsl:for-each select="ead:odd">
				<xsl:apply-templates  mode="other"/>
			</xsl:for-each>
		</div>
	</xsl:template>
	<xsl:template name="acqinfo">
		<h2>
			<xsl:value-of select="ape:resource('eadcontent.acqinfo')" />
		</h2>
		<div class="ead-content">
			<xsl:for-each select="ead:acqinfo">
				<xsl:apply-templates mode="other" />
			</xsl:for-each>
		</div>
	</xsl:template>
	<xsl:template name="controlaccess">
		<h2>
			<xsl:value-of select="ape:resource('eadcontent.controlaccess')" />
		</h2>
		<div class="ead-content">
			<xsl:for-each select="ead:controlaccess">
				<xsl:apply-templates mode="other" />
			</xsl:for-each>
		</div>
	</xsl:template>
	
	<xsl:template name="container">
		<h2>
			<xsl:value-of select="ape:resource('eadcontent.container')" />
		</h2>
		<div class="ead-content">
			<xsl:for-each select="ead:container">
				<xsl:apply-templates mode="other" />
			</xsl:for-each>
		</div>
	</xsl:template>
	
	<!-- Extent option -->
	<xsl:template match="ead:extent">
	    <xsl:value-of select="ape:highlight(., 'other')" disable-output-escaping="yes" />
	    <xsl:if test="./@unit">
	    	<xsl:text> (</xsl:text><xsl:value-of select="./@unit"/><xsl:text>)</xsl:text>
	    </xsl:if>
	</xsl:template>
	
	<xsl:template match="ead:dimensions">
	    <xsl:value-of select="ape:highlight(., 'other')" disable-output-escaping="yes" />
	    <xsl:if test="./@unit">
	    	<xsl:text> (</xsl:text><xsl:value-of select="./@unit"/><xsl:text>)</xsl:text>
	    </xsl:if>
	</xsl:template>
	
    <xsl:template match="ead:physfacet">
        <xsl:apply-templates mode="other" />
    </xsl:template>
    
    <xsl:template match="ead:genreform">
        <xsl:apply-templates mode="other" />
    </xsl:template>
    
	<xsl:template match="ead:bibref" mode="other">
	<xsl:choose>
		<xsl:when test="ead:imprint">	
			<p>
			<xsl:for-each select="ead:imprint">
				<p>
				<xsl:variable name="nameCount" select="count(./parent::node()/ead:name)"></xsl:variable>
				<xsl:for-each select="./parent::node()/ead:name">
					<xsl:value-of select="ape:highlight(., 'other')" disable-output-escaping="yes" /> 
					<xsl:if test="$nameCount > position()">
						<xsl:text>, </xsl:text>
					</xsl:if>
				</xsl:for-each>
				<xsl:text>: </xsl:text>
				<xsl:variable name="titleCount" select="count(./parent::node()/ead:title)"></xsl:variable>
				<xsl:for-each select="./parent::node()/ead:title">
					<i><xsl:value-of select="ape:highlight(., 'other')" disable-output-escaping="yes" /></i>
					<xsl:if test="$titleCount > position()">
						<xsl:text>, </xsl:text>
					</xsl:if>
				</xsl:for-each>
				<xsl:text>. </xsl:text>
				<xsl:variable name="geognameCount" select="count(ead:geogname)"></xsl:variable>
				<xsl:for-each select="ead:geogname">
					<xsl:value-of select="." />
					<xsl:if test="$geognameCount > position()">
						<xsl:text>, </xsl:text>
					</xsl:if>
				</xsl:for-each>
				<xsl:text>: </xsl:text>
				<xsl:variable name="publisherCount" select="count(ead:publisher)"></xsl:variable>
				<xsl:for-each select="ead:publisher">
					<xsl:value-of select="ape:highlight(., 'other')" disable-output-escaping="yes" />
					<xsl:if test="$publisherCount > position()">
						<xsl:text>, </xsl:text>
					</xsl:if>
				</xsl:for-each>
				<xsl:text>, </xsl:text>
				<xsl:variable name="dateCount" select="count(ead:date)"></xsl:variable>
				<xsl:for-each select="ead:date">
					<xsl:value-of select="ape:highlight(., 'other')" disable-output-escaping="yes" />
					<xsl:if test="$dateCount > position()">
						<xsl:text>, </xsl:text>
					</xsl:if>
				</xsl:for-each>
				<xsl:text>. (</xsl:text>		
				<xsl:value-of select = "ape:resource('eadcontent.online')"/><xsl:text>: </xsl:text>
				<xsl:variable name="href" select="./parent::node()/@xlink:href"></xsl:variable>
				<xsl:choose>
					<xsl:when test="./parent::node()/@xlink:title">
						<a href="{$href}" target="_blank"><xsl:value-of select="./parent::node()/@xlink:title"></xsl:value-of></a>
					</xsl:when>
					<xsl:otherwise>
						<a href="{$href}" target="_blank"><xsl:value-of select="$href"></xsl:value-of></a>
					</xsl:otherwise>
				</xsl:choose>
				<xsl:text>).</xsl:text>
				</p>
			</xsl:for-each>
			</p>	
		</xsl:when> <!-- ead:imprint -->
		<xsl:otherwise> 
			<p>
			<xsl:value-of select="." />
			<xsl:text>. (</xsl:text>
			<xsl:value-of select = "ape:resource('eadcontent.online')"/><xsl:text>: </xsl:text>
			<xsl:variable name="href" select="./@xlink:href"></xsl:variable>
			<xsl:choose>
				<xsl:when test="./@xlink:title">
					<a href="{$href}" target="_blank"><xsl:value-of select="./@xlink:title"></xsl:value-of></a>
				</xsl:when>
				<xsl:otherwise>
					<a href="{$href}" target="_blank"><xsl:value-of select="$href"></xsl:value-of></a>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:text>).</xsl:text>	
			</p>
		</xsl:otherwise>
	</xsl:choose>
	</xsl:template>
</xsl:stylesheet>