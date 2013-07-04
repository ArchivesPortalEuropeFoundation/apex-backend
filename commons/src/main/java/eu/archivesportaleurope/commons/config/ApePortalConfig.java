package eu.archivesportaleurope.commons.config;

public class ApePortalConfig extends ApePortalAndDashboardConfig{
	private String solrStopwordsUrl;
    private String solrSearchUrl;
    public String getSolrSearchUrl(){
        return solrSearchUrl;
    }

	public void setSolrSearchUrl(String solrSearchUrl) {
		checkConfigured();
		this.solrSearchUrl = solrSearchUrl;
	}

	public String getSolrStopwordsUrl() {
		return solrStopwordsUrl;
	}

	public void setSolrStopwordsUrl(String solrStopwordsUrl) {
		checkConfigured();
		this.solrStopwordsUrl = solrStopwordsUrl;
	}
	
}
