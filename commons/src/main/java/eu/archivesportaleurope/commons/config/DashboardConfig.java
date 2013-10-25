package eu.archivesportaleurope.commons.config;

import javax.xml.xpath.XPathFactory;
import javax.xml.xpath.XPathFactoryConfigurationException;

import eu.apenet.commons.exceptions.BadConfigurationException;
import eu.apenet.commons.utils.APEnetUtilities;


public class DashboardConfig extends ApePortalAndDashboardConfig{
    protected static final String EMAIL_SEPARATOR = ";";
	private String tempDirPath;
    private String domainNameMainServer;
    
	private String europeanaDirPath;
    private String solrIndexUrl;
	private String emailDashboardFeedbackDestiny;   
    private String xslDirPath;
    private XPathFactory xpathFactory;
    private boolean maintenanceMode = false;
    private String maintenanceAction;
    private boolean defaultQueueProcessing = true;
    private boolean defaultHarvestingProcessing = false;

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


	public String getSolrIndexUrl() {
		return solrIndexUrl;
	}

	public void setSolrIndexUrl(String solrIndexUrl) {
		checkConfigured();
		this.solrIndexUrl = solrIndexUrl;
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

	public boolean isMaintenanceMode() {
		return maintenanceMode;
	}

	public void setMaintenanceMode(boolean maintenanceMode) {
		this.maintenanceMode = maintenanceMode;
	}

	public boolean isDefaultQueueProcessing() {
		return defaultQueueProcessing;
	}

	public void setDefaultQueueProcessing(boolean defaultQueueProcessing) {
		checkConfigured();
		this.defaultQueueProcessing = defaultQueueProcessing;
	}

    public boolean isDefaultHarvestingProcessing() {
        return defaultHarvestingProcessing;
    }

    public void setDefaultHarvestingProcessing(boolean defaultHarvestingProcessing) {
        checkConfigured();
        this.defaultHarvestingProcessing = defaultHarvestingProcessing;
    }

    public String getMaintenanceAction() {
		return maintenanceAction;
	}

	public void setMaintenanceAction(String maintenanceAction) {
		checkConfigured();
		this.maintenanceAction = maintenanceAction;
	}
	
}
