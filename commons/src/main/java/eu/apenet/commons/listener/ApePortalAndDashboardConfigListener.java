package eu.apenet.commons.listener;

import javax.servlet.ServletContext;

import org.apache.commons.lang.StringUtils;

import eu.apenet.commons.exceptions.BadConfigurationException;
import eu.apenet.commons.utils.APEnetUtilities;
import eu.archivesportaleurope.commons.config.ApePortalAndDashboardConfig;

public class ApePortalAndDashboardConfigListener extends APEnetConfigListener {
	private static final String SOLR_DATA_DIR_PATH = "SOLR_DATA_DIR_PATH";
	private static final String SOLR_CONTEXT_PATH = "/solr";
	
	private static final String AL_DIR_PATH = "AL_DIR_PATH";
	private static final String AL_DIR_PATH_DEFAULT = "/ape/data/al/";

	
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
			if (solrDataDirPath == null)
				throw new BadConfigurationException(SOLR_DATA_DIR_PATH + " is not configured");
			System.setProperty("solr.data.dir", solrDataDirPath);
			return;
		}
		String archivalLandscapeDirPath = servletContext.getInitParameter(AL_DIR_PATH);
		if (StringUtils.isBlank(archivalLandscapeDirPath)) {
			log.info("No " + AL_DIR_PATH + " specified. Using the default: " + AL_DIR_PATH_DEFAULT);
			archivalLandscapeDirPath = AL_DIR_PATH_DEFAULT;
		}
		archivalLandscapeDirPath = checkPath(AL_DIR_PATH, archivalLandscapeDirPath);
		config.setArchivalLandscapeDirPath(archivalLandscapeDirPath);

		super.init(servletContext, config);
	}
}
