package eu.apenet.commons.xslt.tags;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import eu.apenet.commons.ResourceBundleSource;
import eu.apenet.commons.solr.SolrField;
import eu.apenet.commons.xslt.EadXslt;

/**
 * Display eag file.
 * 
 * @author bastiaan
 *
 */
public abstract class AbstractEadTag extends SimpleTagSupport {
	public static final String CDETAILS_CHILD_XSLT = "cdetails-child";
	public static final String FRONTPAGE_XSLT = "frontpage";
	public static final String CDETAILS_XSLT = "cdetails";
	private String xml;
	private String searchTerms;
	private String searchFieldsSelectionId;
	private String secondDisplayUrl;
	private String aiId;
	private String type;
	private final static Logger LOG = Logger.getLogger(AbstractEadTag.class);
	private static final List<SolrField> DEFAULT_HIGHLIGHT_FIELDS = SolrField.getDefaults();

	private final static Map<String, String> xsltUrls = new HashMap<String,String>();
	static {
		xsltUrls.put(CDETAILS_XSLT, "xsl/ead/cdetails.xsl");
		xsltUrls.put(CDETAILS_CHILD_XSLT, "xsl/ead/cdetails-child.xsl"); 		
		xsltUrls.put(FRONTPAGE_XSLT, "xsl/ead/frontpage.xsl");	
		
	}
	
	public final void doTag() throws JspException, IOException {
		Source xmlSource = new StreamSource(new StringReader(xml));
		List<SolrField> highlightFields = SolrField.getSolrFieldsByIdString(searchFieldsSelectionId);
		if (highlightFields.size() == 0) {
			highlightFields = DEFAULT_HIGHLIGHT_FIELDS;
		}
		try {
			Integer aiIdInt = null;
			if (StringUtils.isNotBlank(aiId)) {
				aiIdInt = Integer.parseInt(aiId);
			}
			EadXslt.convertEadToHtml(xsltUrls.get(getType()), this.getJspContext().getOut(), xmlSource, searchTerms,
					highlightFields, getResourceBundleSource(), secondDisplayUrl, aiIdInt, getSolrStopwordsUrl());
		} catch (Exception e) {
			LOG.error(e.getMessage());
		}

	}

	protected abstract ResourceBundleSource getResourceBundleSource();

	protected abstract String getSolrStopwordsUrl();

	public String getXml() {
		return xml;
	}

	public void setXml(String xml) {
		this.xml = xml;
	}

	public String getSearchTerms() {
		return searchTerms;
	}

	public void setSearchTerms(String searchTerms) {
		this.searchTerms = searchTerms;
	}

	public String getSearchFieldsSelectionId() {
		return searchFieldsSelectionId;
	}

	public void setSearchFieldsSelectionId(String searchFieldsSelectionId) {
		this.searchFieldsSelectionId = searchFieldsSelectionId;
	}

	public String getSecondDisplayUrl() {
		return secondDisplayUrl;
	}

	public void setSecondDisplayUrl(String secondDisplayUrl) {
		this.secondDisplayUrl = secondDisplayUrl;
	}

	public String getAiId() {
		return aiId;
	}

	public void setAiId(String aiId) {
		this.aiId = aiId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
