package eu.apenet.commons.xslt.tags;

import java.io.BufferedReader;
import java.io.FileReader;
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
import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.commons.xslt.EacXslt;


/**
 * Display eac-cpf file.
 * 
 */
public abstract class AbstractEacTag extends SimpleTagSupport {
	public static final String EACCPFDETAILS_XSLT = "eaccpfdetails";
	private String xml;
	private String searchTerms;
	private String searchFieldsSelection;
	private String secondDisplayUrl;
	private String aiId;
	private String type;
	private String databaseId;
	private String eacUrl;
	private String repositoryCode;
	private String eaccpfIdentifier;

	// Variables for the relations.
	private String aiCodeUrl;
	private String eacUrlBase;
	private String eadUrl;

	private final static Logger LOG = Logger.getLogger(AbstractEacTag.class);
	private static final List<SolrField> DEFAULT_HIGHLIGHT_FIELDS = SolrField.getDefaults();

	private final static Map<String, String> xsltUrls = new HashMap<String,String>();
	static {
		xsltUrls.put(EACCPFDETAILS_XSLT, "xsl/eaccpf/eaccpfdetails.xsl");	
	}
	
	public final void doTag() throws JspException, IOException {
		FileReader eacFile = new FileReader(APEnetUtilities.getApePortalAndDashboardConfig().getRepoDirPath() + this.getEacUrl());
		Source xmlSource = new StreamSource(new StringReader(this.readFile(eacFile)));
		List<SolrField> highlightFields = SolrField.getSolrFieldsByIdString(searchFieldsSelection);
		if (highlightFields.size() == 0) {
			highlightFields = DEFAULT_HIGHLIGHT_FIELDS;
		}
		try {
			Integer aiIdInt = null;
			if (StringUtils.isNotBlank(aiId)) {
				aiIdInt = Integer.parseInt(aiId);
			}
			String xslLocation = xsltUrls.get(getType());
			if (xslLocation == null){
				LOG.warn("EAC-CPF xsl type does not exist: " + getType());
			}else {
				EacXslt.convertEacToHtml(xslLocation, this.getJspContext().getOut(), xmlSource, searchTerms,
					highlightFields, getResourceBundleSource(), secondDisplayUrl, aiIdInt, isPreview(),
					getSolrStopwordsUrl(), this.getAiCodeUrl(), this.getEacUrlBase(), this.getEadUrl());
			}
		} catch (Exception e) {
			LOG.error(e.getMessage());
		}

	}

	private String readFile(FileReader file) throws IOException {
		BufferedReader bufferedReader = new BufferedReader(file);
		String line = null;
		StringBuilder stringBuilder = new StringBuilder();
		String lineSeparator = System.getProperty("line.separator");

		while ((line = bufferedReader.readLine()) != null) {
			stringBuilder.append(line);
			stringBuilder.append(lineSeparator);
		}

		return stringBuilder.toString();
	}

	protected abstract ResourceBundleSource getResourceBundleSource();

	protected abstract String getSolrStopwordsUrl();
	
	protected abstract boolean isPreview();

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

	public String getSearchFieldsSelection() {
		return searchFieldsSelection;
	}

	public void setSearchFieldsSelection(String searchFieldsSelection) {
		this.searchFieldsSelection = searchFieldsSelection;
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

	public String getDatabaseId() {
		return databaseId;
	}

	public void setDatabaseId(String databaseId) {
		this.databaseId = databaseId;
	}

	public String getEacUrl() {
		return eacUrl;
	}

	public void setEacUrl(String eacUrl) {
		this.eacUrl = eacUrl;
	}

	/**
	 * @return the repositoryCode
	 */
	public String getRepositoryCode() {
		return this.repositoryCode;
	}

	/**
	 * @param repositoryCode the repositoryCode to set
	 */
	public void setRepositoryCode(String repositoryCode) {
		this.repositoryCode = repositoryCode;
	}

	/**
	 * @return the eaccpfIdentifier
	 */
	public String getEaccpfIdentifier() {
		return this.eaccpfIdentifier;
	}

	/**
	 * @param eaccpfIdentifier the eaccpfIdentifier to set
	 */
	public void setEaccpfIdentifier(String eaccpfIdentifier) {
		this.eaccpfIdentifier = eaccpfIdentifier;
	}

	/**
	 * @return the aiCodeUrl
	 */
	public String getAiCodeUrl() {
		return this.aiCodeUrl;
	}

	/**
	 * @param aiCodeUrl the aiCodeUrl to set
	 */
	public void setAiCodeUrl(String aiCodeUrl) {
		this.aiCodeUrl = aiCodeUrl;
	}

	/**
	 * @return the eacUrlBase
	 */
	public String getEacUrlBase() {
		return this.eacUrlBase;
	}

	/**
	 * @param eacUrlBase the eacUrlBase to set
	 */
	public void setEacUrlBase(String eacUrlBase) {
		this.eacUrlBase = eacUrlBase;
	}

	/**
	 * @return the eadUrl
	 */
	public String getEadUrl() {
		return this.eadUrl;
	}

	/**
	 * @param eadUrl the eadUrl to set
	 */
	public void setEadUrl(String eadUrl) {
		this.eadUrl = eadUrl;
	}

}
