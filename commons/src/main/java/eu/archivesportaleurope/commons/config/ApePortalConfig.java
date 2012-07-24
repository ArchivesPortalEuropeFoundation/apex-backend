package eu.archivesportaleurope.commons.config;

public class ApePortalConfig extends ApePortalAndDashboardConfig{
	private String solrStopwordsUrl;

	public String getSolrStopwordsUrl() {
		return solrStopwordsUrl;
	}

	public void setSolrStopwordsUrl(String solrStopwordsUrl) {
		checkConfigured();
		this.solrStopwordsUrl = solrStopwordsUrl;
	}
	
}
