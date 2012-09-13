package eu.archivesportaleurope.commons.config;

import javax.xml.xpath.XPathFactory;
import javax.xml.xpath.XPathFactoryConfigurationException;

import eu.apenet.commons.exceptions.BadConfigurationException;
import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.commons.utils.SecurityLevel;


public class DashboardConfig extends ApePortalAndDashboardConfig{
    protected static final String EMAIL_SEPARATOR = ";";
	private String tempDirPath;
    private String domainNameMainServer;
    
	private String europeanaDirPath;
    private String solrIndexUrl;
	private String emailDashboardFeedbackDestiny;   
  	private boolean directIndexing;
    private String xslDirPath;
    private XPathFactory xpathFactory;
    private boolean batchProcessingEnabled = true;

	public String getEuropeanaDirPath() {
		return europeanaDirPath;
	}

	public void setEuropeanaDirPath(String europeanaDirPath) {
		checkConfigured();	
		this.europeanaDirPath = europeanaDirPath;
	}
	public String getEmailDashboardFeedbackDestiny() {
		return emailDashboardFeedbackDestiny;
	}

	public void setEmailDashboardFeedbackDestiny(
			String emailDashboardFeedbackDestiny) {
		checkConfigured();
		this.emailDashboardFeedbackDestiny = emailDashboardFeedbackDestiny;
	
	}
	public void setDirectIndexing(boolean directIndexing) {
		checkConfigured();
		this.directIndexing = directIndexing;
	}

	public String getSolrIndexUrl() {
		return solrIndexUrl;
	}

	public void setSolrIndexUrl(String solrIndexUrl) {
		checkConfigured();
		this.solrIndexUrl = solrIndexUrl;
	}
	public boolean isDirectIndexing() {
		return directIndexing;
	}
    public String getXslDirPath(){
        return xslDirPath;
    }
    public String getSystemXslDirPath(){
        return xslDirPath + APEnetUtilities.FILESEPARATOR + "system";
    }
	public void setXslDirPath(String xslDirPath) {
		checkConfigured();
		this.xslDirPath = xslDirPath;
	}

	public XPathFactory getXpathFactory() {
		return xpathFactory;
	}

	@Override
	protected void initBeforeFinalize() {
        try {
       		xpathFactory = XPathFactory.newInstance(XPathFactory.DEFAULT_OBJECT_MODEL_URI,"org.apache.xpath.jaxp.XPathFactoryImpl", this.getClass().getClassLoader());
		} catch (XPathFactoryConfigurationException e) {
			throw new BadConfigurationException(e);
		}
		super.initBeforeFinalize();
	}


	public void setDomainNameMainServer(String domainNameMainServer) {
		checkConfigured();
		this.domainNameMainServer = domainNameMainServer;
	}
    public String getDomainNameMainServer() {
		return domainNameMainServer;
	}
    
	//todo: Change this name back to getTempDirPath when everything is working fine
	public String getTempAndUpDirPath(){
        return tempDirPath;
    }
	public void setTempDirPath(String tempDirPath) {
		checkConfigured();
		this.tempDirPath = tempDirPath;
	}


	public boolean isBatchProcessingEnabled() {
		return batchProcessingEnabled;
	}

	public void setBatchProcessingEnabled(boolean batchProcessingEnabled) {
		checkConfigured();
		this.batchProcessingEnabled = batchProcessingEnabled;
	}


	
}
