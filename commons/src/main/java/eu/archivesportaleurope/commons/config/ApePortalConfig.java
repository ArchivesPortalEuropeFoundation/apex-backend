package eu.archivesportaleurope.commons.config;

public class ApePortalConfig extends ApePortalAndDashboardConfig {

    private String solrStopwordsUrl;
    private String baseSolrSearchUrl;

    public String getBaseSolrSearchUrl() {
        return baseSolrSearchUrl;
    }

    public void setBaseSolrSearchUrl(String baseSolrSearchUrl) {
        checkConfigured();
        this.baseSolrSearchUrl = baseSolrSearchUrl;
    }

    public String getSolrStopwordsUrl() {
        return solrStopwordsUrl;
    }

    public void setSolrStopwordsUrl(String solrStopwordsUrl) {
        checkConfigured();
        this.solrStopwordsUrl = solrStopwordsUrl;
    }

}
