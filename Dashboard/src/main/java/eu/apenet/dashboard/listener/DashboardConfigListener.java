package eu.apenet.dashboard.listener;

import java.util.Locale;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import org.apache.commons.lang.StringUtils;

import eu.apenet.commons.listener.ApePortalAndDashboardConfigListener;
import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.dashboard.utils.PropertiesUtil;
import eu.archivesportaleurope.commons.config.DashboardConfig;

public class DashboardConfigListener extends ApePortalAndDashboardConfigListener {

    private static final String CONFIG_PROPERTIES_PATH = "CONFIG_PROPERTIES_PATH";
    private static final String SOLR_BASE_INDEX_URL = "SOLR_BASE_INDEX_URL";
    private static final String XSL_DIR_PATH = "XSL_DIR_PATH";
    private static final String XSL_DIR_PATH_DEFAULT = "/ape/data/xsl/";
    private static final String TMP_DIR_PATH = "TMP_DIR_PATH";
    private static final String TMP_DIR_PATH_DEFAULT = "/ape/data/tmp/";
    private static final String DEFAULT_QUEUE_PROCESSING = "DEFAULT_QUEUE_PROCESSING";
    private static final String DEFAULT_HARVESTING_PROCESSING = "DEFAULT_HARVESTING_PROCESSING";
    private static final String MAINTENANCE_MODE = "MAINTENANCE_MODE";
    private static final String MAINTENANCE_ACTION = "MAINTENANCE_ACTION";

    @Override
    public void contextInitializedInternal(ServletContext servletContext) {
        try {
            Locale.setDefault(Locale.UK);
            DashboardConfig apeConfig = new DashboardConfig();
            init(servletContext, apeConfig);
            apeConfig.finalizeConfigPhase();
            APEnetUtilities.setConfig(apeConfig);
            if (StringUtils.isNotBlank(apeConfig.getMaintenanceAction())) {
                MaintenanceTask maintenanceTask = new MaintenanceTask();
                maintenanceTask.start();
            }
            MaintenanceDaemon.start();
            if (apeConfig.isDefaultQueueProcessing()) {
                QueueDaemon.start();
            }
            if (apeConfig.isDefaultHarvestingProcessing()) {
                HarvesterDaemon.start(true);
            } else {
                HarvesterDaemon.start(false);
            }

        } catch (RuntimeException e) {
            log.fatal("Fatal error while initializing: " + e.getMessage(), e);
            throw e;
        }
    }

    protected void init(ServletContext servletContext, DashboardConfig config) {

        String configProperties = servletContext.getInitParameter(CONFIG_PROPERTIES_PATH);
        if (StringUtils.isBlank(configProperties)) {
            config.setConfigPropertiesPath("/ape/liferay/tomcat-base/conf/dashboard.properties");
        } else {
            config.setConfigPropertiesPath(configProperties);
        }
        PropertiesUtil.reload(config);

        /*
		 * solr indexing
         */
        String solrIndexUrl = servletContext.getInitParameter(SOLR_BASE_INDEX_URL);
        if (StringUtils.isBlank(solrIndexUrl)) {
            log.warn("No " + SOLR_BASE_INDEX_URL + " specified.");
        } else {
            log.info(SOLR_BASE_INDEX_URL + ": " + solrIndexUrl);
        }
        config.setBaseSolrIndexUrl(solrIndexUrl);

        String xslDirPath = servletContext.getInitParameter(XSL_DIR_PATH);
        if (StringUtils.isBlank(xslDirPath)) {
            log.info("No " + XSL_DIR_PATH + " specified. Using the default: " + XSL_DIR_PATH_DEFAULT);
            xslDirPath = XSL_DIR_PATH_DEFAULT;
        }
        xslDirPath = checkPath(XSL_DIR_PATH, xslDirPath);
        config.setXslDirPath(xslDirPath);
        String tmpDirPath = servletContext.getInitParameter(TMP_DIR_PATH);
        if (StringUtils.isBlank(tmpDirPath)) {
            log.warn("No " + TMP_DIR_PATH + " specified. Using the default: " + TMP_DIR_PATH_DEFAULT);
            tmpDirPath = TMP_DIR_PATH_DEFAULT;
        }
        tmpDirPath = checkPath(TMP_DIR_PATH, tmpDirPath);
        config.setTempDirPath(tmpDirPath);
        String defaultQueueProcessingString = servletContext.getInitParameter(DEFAULT_QUEUE_PROCESSING);
        if (StringUtils.isNotBlank(defaultQueueProcessingString)) {
            config.setDefaultQueueProcessing(Boolean.parseBoolean(defaultQueueProcessingString));
        }

        String defaultHarvestingProcessingString = servletContext.getInitParameter(DEFAULT_HARVESTING_PROCESSING);
        if (StringUtils.isNotBlank(defaultHarvestingProcessingString)) {
            config.setDefaultHarvestingProcessing(Boolean.parseBoolean(defaultHarvestingProcessingString));
        }

        String maintenanceMode = servletContext.getInitParameter(MAINTENANCE_MODE);
        if (StringUtils.isNotBlank(maintenanceMode)) {
            config.setMaintenanceMode(Boolean.parseBoolean(maintenanceMode));
        }
        String maintenanceAction = servletContext.getInitParameter(MAINTENANCE_ACTION);
        if (StringUtils.isNotBlank(maintenanceAction)) {
            config.setDefaultQueueProcessing(false);
            config.setMaintenanceMode(true);
            config.setMaintenanceAction(maintenanceAction);
            log.info("Maintenance action specified: " + maintenanceAction);

        }
        super.init(servletContext, config);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        QueueDaemon.stop();
        HarvesterDaemon.stop(true);
        MaintenanceDaemon.stop();
    }

}
