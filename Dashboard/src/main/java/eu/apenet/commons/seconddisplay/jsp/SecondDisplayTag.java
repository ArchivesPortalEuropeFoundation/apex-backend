package eu.apenet.commons.seconddisplay.jsp;

import java.util.List;

import org.apache.log4j.Logger;

import eu.apenet.commons.ResourceBundleSource;
import eu.apenet.commons.StrutsResourceBundleSource;
import eu.apenet.commons.solr.SolrField;
import eu.apenet.commons.xslt.tags.AbstractEadTag;
@Deprecated
public class SecondDisplayTag extends AbstractEadTag {

	private String xslUrl;
	private final static Logger LOG = Logger.getLogger(SecondDisplayTag.class);
	private static final List<SolrField> DEFAULT_HIGHLIGHT_FIELDS = SolrField.getDefaults();
	

	public String getXslUrl() {
		return xslUrl;
	}

	public void setXslUrl(String xslUrl) {
		this.xslUrl = xslUrl;
	}



	@Override
	public String getType() {
		return getXslUrl().replaceAll("xsl/seconddisplay/", "").replaceAll(".xsl", "");
	}



	@Override
	protected ResourceBundleSource getResourceBundleSource() {
		return new StrutsResourceBundleSource();
	}



	@Override
	protected String getSolrStopwordsUrl() {
		String solrStopwordsUrl = null;
		return null;
	}




}
