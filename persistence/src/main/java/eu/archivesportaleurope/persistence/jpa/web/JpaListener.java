package eu.archivesportaleurope.persistence.jpa.web;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;

import eu.archivesportaleurope.persistence.jpa.JpaUtil;

/**
 * Servlet Context Listener to initialize the sessionFactory
 * 
 * @author bverhoef
 * 
 */
public class JpaListener implements ServletContextListener {
	private static final Logger log = Logger.getLogger(JpaListener.class);

	// private static final String CONFIG_PATH_DEFAULT =
	// "/hibernatePortal.cfg.xml";

	public void contextInitialized(ServletContextEvent ce) {
		try {
			JpaUtil.init();
		} catch (RuntimeException e) {
			log.fatal("Fatal error while initializing: " + e.getMessage(), e);
			throw e;
		}
	}

	public void contextDestroyed(ServletContextEvent ce) {
		JpaUtil.closeEntityManagerFactory();
	}
}