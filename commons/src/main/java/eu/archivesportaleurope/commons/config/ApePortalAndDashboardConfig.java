package eu.archivesportaleurope.commons.config;


public class ApePortalAndDashboardConfig extends ApeConfig{
    private String solrSearchUrl;
    private String archivalLandscapeDirPath;
    private boolean newSolrConfiguration = false;
    public String getSolrSearchUrl(){
        return solrSearchUrl;
    }

	public void setSolrSearchUrl(String solrSearchUrl) {
		checkConfigured();
		this.solrSearchUrl = solrSearchUrl;
	}

	public String getArchivalLandscapeDirPath() {
		return archivalLandscapeDirPath;
	}

	public void setArchivalLandscapeDirPath(String archivalLandscapeDirPath) {
		checkConfigured();
		this.archivalLandscapeDirPath = archivalLandscapeDirPath;
	}

	public boolean isNewSolrConfiguration() {
		return newSolrConfiguration;
	}

	public void setNewSolrConfiguration(boolean newSolrConfiguration) {
		checkConfigured();
		this.newSolrConfiguration = newSolrConfiguration;
	}
	
}
