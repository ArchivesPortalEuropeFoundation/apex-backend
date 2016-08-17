package eu.apenet.commons.listener;

import java.io.File;

import javax.servlet.ServletContext;

import org.apache.commons.lang.StringUtils;

import eu.apenet.commons.utils.APEnetUtilities;
import eu.archivesportaleurope.commons.config.ApePortalConfig;

public class ApePortalConfigListener extends ApePortalAndDashboardConfigListener {

    private static final String SOLR_STOPWORDS_URL = "SOLR_STOPWORDS_URL";
    private static final String SOLR_SEARCH_URL = "SOLR_BASE_SEARCH_URL";

    @Override
    public void contextInitializedInternal(ServletContext servletContext) {
        try {
            ApePortalConfig apeConfig = new ApePortalConfig();
            init(servletContext, apeConfig);
            apeConfig.finalizeConfigPhase();
            APEnetUtilities.setConfig(apeConfig);
        } catch (RuntimeException e) {
            log.fatal("Fatal error while initializing: " + e.getMessage(), e);
            throw e;
        }
    }

    protected void init(ServletContext servletContext, ApePortalConfig config) {
        /*
		 * highlighting for second display;
         */
        String solrStopwordsUrl = servletContext.getInitParameter(SOLR_STOPWORDS_URL);

        if (StringUtils.isBlank(solrStopwordsUrl)) {
            log.warn("No "
                    + SOLR_STOPWORDS_URL
                    + " specified. Highlighting in Second Display will be different from highlighting in search results.");
        } else {
            File solrFile = new File(solrStopwordsUrl);
            solrStopwordsUrl = solrFile.getAbsolutePath().replace('\\', '/');
            if (!solrFile.exists()) {
                log.warn("No " + SOLR_STOPWORDS_URL + " exists(" + solrFile.getAbsolutePath()
                        + "). Highlighting in Second Display will be different from highlighting in search results.");
            }
        }
        config.setSolrStopwordsUrl(solrStopwordsUrl);
        String solrSearchUrl = servletContext.getInitParameter(SOLR_SEARCH_URL);
        if (StringUtils.isBlank(solrSearchUrl)) {
            log.warn("No " + SOLR_SEARCH_URL + " specified.");
        } else {
            log.info(SOLR_SEARCH_URL + ": " + solrSearchUrl);
        }
        config.setBaseSolrSearchUrl(solrSearchUrl);
        super.init(servletContext, config);
    }
}
