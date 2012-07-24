package eu.apenet.commons.listener;

import java.io.File;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import org.apache.commons.lang.StringUtils;

import eu.apenet.commons.utils.APEnetUtilities;
import eu.archivesportaleurope.commons.config.ApePortalConfig;

public class ApePortalConfigListener extends ApePortalAndDashboardConfigListener {

	private static final String SOLR_STOPWORDS_URL = "SOLR_STOPWORDS_URL";

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
		super.init(servletContext, config);
	}
}
