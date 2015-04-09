package eu.apenet.oaiserver.config.other.listener;

import eu.apenet.oaiserver.config.other.jpa.JpaUtil;
import org.apache.log4j.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Created by yoannmoranville on 09/04/15.
 */
public class JpaListenerOAI implements ServletContextListener {
    private static final Logger log = Logger.getLogger(JpaListenerOAI.class);

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