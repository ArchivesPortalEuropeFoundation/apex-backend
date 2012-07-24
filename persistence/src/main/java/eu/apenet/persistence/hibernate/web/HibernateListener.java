package eu.apenet.persistence.hibernate.web;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import eu.apenet.persistence.hibernate.HibernateUtil;

/**
 * Servlet Context Listener to initialize the sessionFactory
 * 
 * @author bverhoef
 * 
 */
public class HibernateListener implements ServletContextListener {
	private static final Logger log = Logger.getLogger(HibernateListener.class);
	private static final String CONFIG_PATH = "/hibernate.cfg.xml";

	private static final String APENET_DATASOURCE_VALUE = "APENET_DATASOURCE_VALUE";
	private static final String APENET_DATASOURCE_VALUE_DEFAULT = "jdbc/APEnetDatabaseDashboard";

	// private static final String CONFIG_PATH_DEFAULT =
	// "/hibernatePortal.cfg.xml";

	public void contextInitialized(ServletContextEvent ce) {
		try {
			String datasourceValue = ce.getServletContext().getInitParameter(APENET_DATASOURCE_VALUE);
			if (StringUtils.isBlank(datasourceValue)) {
				log.warn("No " + APENET_DATASOURCE_VALUE + " specified. Using the default: "
						+ APENET_DATASOURCE_VALUE_DEFAULT);
				datasourceValue = APENET_DATASOURCE_VALUE_DEFAULT;
			}
			/*
			 * If an exception occurred, no catch is made, because it is useless
			 * to start the application
			 */
			HibernateUtil.init(CONFIG_PATH, datasourceValue);
			log.info("Using the hibernate config file : " + CONFIG_PATH);
			log.info("Using the datasource : " + datasourceValue);
		} catch (RuntimeException e) {
			log.fatal("Fatal error while initializing: " + e.getMessage(), e);
			throw e;
		}
	}

	public void contextDestroyed(ServletContextEvent ce) {
		HibernateUtil.closeSessionFactory();
	}
}