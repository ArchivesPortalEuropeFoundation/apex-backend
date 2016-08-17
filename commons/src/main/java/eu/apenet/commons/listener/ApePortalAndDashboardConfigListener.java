package eu.apenet.commons.listener;

import javax.servlet.ServletContext;

import eu.apenet.commons.exceptions.BadConfigurationException;
import eu.apenet.commons.utils.APEnetUtilities;
import eu.archivesportaleurope.commons.config.ApePortalAndDashboardConfig;

public class ApePortalAndDashboardConfigListener extends APEnetConfigListener {

    private static final String SOLR_DATA_DIR_PATH = "SOLR_DATA_DIR_PATH";
    private static final String SOLR_CONTEXT_PATH = "/solr";

    @Override
    public void contextInitializedInternal(ServletContext servletContext) {
        try {
            ApePortalAndDashboardConfig apeConfig = new ApePortalAndDashboardConfig();
            init(servletContext, apeConfig);
            apeConfig.finalizeConfigPhase();
            APEnetUtilities.setConfig(apeConfig);
        } catch (RuntimeException e) {
            log.fatal("Fatal error while initializing: " + e.getMessage(), e);
            throw e;
        }
    }

    protected void init(ServletContext servletContext, ApePortalAndDashboardConfig config) {
        if (servletContext.getContextPath().equals(SOLR_CONTEXT_PATH)) {
            String solrDataDirPath = servletContext.getInitParameter(SOLR_DATA_DIR_PATH);
            if (solrDataDirPath == null) {
                throw new BadConfigurationException(SOLR_DATA_DIR_PATH + " is not configured");
            }
            System.setProperty("solr.data.dir", solrDataDirPath);
            return;
        }

        super.init(servletContext, config);
    }
}
