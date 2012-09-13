package eu.apenet.commons.listener;

import javax.servlet.ServletContext;

import org.apache.commons.lang.StringUtils;

import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.commons.utils.SecurityLevel;
import eu.archivesportaleurope.commons.config.DashboardConfig;

public class DashboardConfigListener extends ApePortalAndDashboardConfigListener {
	private static final String TRUE = "true";
	private static final String EMAIL_DASHBOARD_FEEDBACK_DESTINY = "EMAIL_DASHBOARD_FEEDBACK_DESTINY";
	private static final String EUROPEANA_DIR_PATH = "EUROPEANA_DIR_PATH";
	private static final String EUROPEANA_DIR_PATH_DEFAULT = "/mnt/europeana/";
	private static final String SOLR_INDEX_URL = "SOLR_INDEX_URL";
	private static final String DIRECT_INDEXING = "DIRECT_INDEXING";
	private static final String XSL_DIR_PATH = "XSL_DIR_PATH";
	private static final String XSL_DIR_PATH_DEFAULT = "/mnt/xsl/";
	private static final String TMP_DIR_PATH = "TMP_DIR_PATH";
	private static final String TMP_DIR_PATH_DEFAULT = "/mnt/tmp/";
	private static final String BATCH_PROCESSING_ENABLED = "BATCH_PROCESSING_ENABLED";

	private static final String DOMAIN_NAME_MAIN_SERVER = "DOMAIN_NAME_MAIN_SERVER";
	private static final String DOMAIN_NAME_MAIN_SERVER_DEFAULT = "localhost:8443";
	
	@Override
	public void contextInitializedInternal(ServletContext servletContext) {
		try {
			DashboardConfig apeConfig = new DashboardConfig();
			init(servletContext, apeConfig);
			apeConfig.finalizeConfigPhase();
			APEnetUtilities.setConfig(apeConfig);
		} catch (RuntimeException e) {
			log.fatal("Fatal error while initializing: " + e.getMessage(), e);
			throw e;
		}
	}

	protected void init(ServletContext servletContext, DashboardConfig config) {
		String europeanaDirPath = servletContext.getInitParameter(EUROPEANA_DIR_PATH);
		if (StringUtils.isBlank(europeanaDirPath)) {
			log.warn("No " + EUROPEANA_DIR_PATH + " specified. Using the default: " + EUROPEANA_DIR_PATH_DEFAULT);
			europeanaDirPath = EUROPEANA_DIR_PATH_DEFAULT;
		}
		europeanaDirPath = checkPath(EUROPEANA_DIR_PATH, europeanaDirPath);
		config.setEuropeanaDirPath(europeanaDirPath);
		String emailDashboardFeedbackDestiny = servletContext.getInitParameter(EMAIL_DASHBOARD_FEEDBACK_DESTINY);

		if (StringUtils.isBlank(emailDashboardFeedbackDestiny)) {
			log.warn("No " + EMAIL_DASHBOARD_FEEDBACK_DESTINY + " specified.");
		} else {
			log.info(EMAIL_DASHBOARD_FEEDBACK_DESTINY + ": " + emailDashboardFeedbackDestiny);
		}

		config.setEmailDashboardFeedbackDestiny(emailDashboardFeedbackDestiny);

		/*
		 * solr indexing
		 */
		String solrIndexUrl = servletContext.getInitParameter(SOLR_INDEX_URL);
		if (StringUtils.isBlank(solrIndexUrl)) {
			log.warn("No " + SOLR_INDEX_URL + " specified.");
		} else {
			log.info(SOLR_INDEX_URL + ": " + solrIndexUrl);
		}
		config.setSolrIndexUrl(solrIndexUrl);
		/*
		 * direct indexing
		 */
		boolean directIndexing = TRUE.equalsIgnoreCase(servletContext.getInitParameter(DIRECT_INDEXING));
		config.setDirectIndexing(directIndexing);
		String batchProcessingEnabled = servletContext.getInitParameter(BATCH_PROCESSING_ENABLED);
		if (!StringUtils.isBlank(batchProcessingEnabled)) {
			config.setBatchProcessingEnabled(TRUE.equalsIgnoreCase(batchProcessingEnabled));
		}

		
		String xslDirPath = servletContext.getInitParameter(XSL_DIR_PATH);
		if (StringUtils.isBlank(xslDirPath)) {
			log.warn("No " + XSL_DIR_PATH + " specified. Using the default: " + XSL_DIR_PATH_DEFAULT);
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

	
	
		String domainNameMainServer = servletContext.getInitParameter(DOMAIN_NAME_MAIN_SERVER);
		if (StringUtils.isBlank(domainNameMainServer)) {
			log.warn("No " + DOMAIN_NAME_MAIN_SERVER + " specified. Using the default: "
					+ DOMAIN_NAME_MAIN_SERVER_DEFAULT);
			domainNameMainServer = DOMAIN_NAME_MAIN_SERVER_DEFAULT;
		}
		config.setDomainNameMainServer(domainNameMainServer);

		super.init(servletContext, config);
	}

}
