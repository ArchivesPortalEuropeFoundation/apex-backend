package eu.apenet.commons.listener;

import java.io.File;
import java.util.TimeZone;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import eu.apenet.commons.exceptions.BadConfigurationException;
import eu.apenet.commons.utils.APEnetUtilities;
import eu.archivesportaleurope.commons.config.ApeConfig;

/**
 * User: Yoann Moranville Date: Sep 28, 2010
 *
 * @author Yoann Moranville
 */
public class APEnetConfigListener implements ServletContextListener {

    private static final String LOG4J_XML_LOCATION = "LOG4J_XML_LOCATION";

    protected final Logger log = Logger.getLogger(this.getClass());

    private static final String REPOSITORY_DIR_PATH = "REPOSITORY_DIR_PATH";
    private static final String REPOSITORY_DIR_PATH_DEFAULT = "/ape/data/repo/";
    private static final String EMAIL_PREFIX = "EMAIL_PREFIX";
    private static final String EMAIL_PREFIX_DEFAULT = "[APE] ";

    @Override
    public final void contextInitialized(ServletContextEvent servletContextEvent) {
        try {
            initLog4j(servletContextEvent.getServletContext());
            contextInitializedInternal(servletContextEvent.getServletContext());
        } catch (RuntimeException e) {
            log.fatal("Fatal error while initializing: " + e.getMessage(), e);
            throw e;
        }
    }

    public void contextInitializedInternal(ServletContext servletContext) {
        ApeConfig apeConfig = new ApeConfig();
        init(servletContext, apeConfig);
        apeConfig.finalizeConfigPhase();
        APEnetUtilities.setConfig(apeConfig);
    }

    protected void init(ServletContext servletContext, ApeConfig config) {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        String repositoryDirPath = servletContext.getInitParameter(REPOSITORY_DIR_PATH);
        repositoryDirPath = checkPath(REPOSITORY_DIR_PATH, repositoryDirPath);
        if (StringUtils.isBlank(repositoryDirPath)) {
            log.info("No " + REPOSITORY_DIR_PATH + " specified. Using the default: " + REPOSITORY_DIR_PATH_DEFAULT);
            repositoryDirPath = REPOSITORY_DIR_PATH_DEFAULT;
        }
        config.setRepoDirPath(repositoryDirPath);
        String emailPrefix = servletContext.getInitParameter(EMAIL_PREFIX);
        if (StringUtils.isBlank(emailPrefix)) {
            log.warn("No " + EMAIL_PREFIX + " specified. Using the default: " + EMAIL_PREFIX_DEFAULT);
            emailPrefix = EMAIL_PREFIX_DEFAULT;
        }
        config.setEmailPrefix(emailPrefix);
    }

    private void initLog4j(ServletContext servletContext) {
        String log4jLocation = servletContext.getInitParameter(LOG4J_XML_LOCATION);
        if (log4jLocation != null) {
            File log4jXmlFile = new File(log4jLocation);
            if (log4jXmlFile.exists()) {
                System.out.println("Initializing log4j with: " + log4jXmlFile);
                DOMConfigurator.configure(log4jXmlFile.getAbsolutePath());
            } else {
                System.err.println("*** " + log4jXmlFile + " file not found, so initializing log4j with BasicConfigurator");
                BasicConfigurator.configure();
            }
        }
    }

    protected String checkPath(String propertyName, String dirPath) {
        File dir = new File(dirPath);
        String absolutePath = dir.getAbsolutePath().replace('\\', '/');
        if (dir.exists()) {

            if (dir.isDirectory()) {
                if (dir.canRead() && dir.canWrite()) {
                    log.info("Successfull initialized " + propertyName + "(" + absolutePath + ")");
                } else {
                    throw new BadConfigurationException(propertyName + "(" + absolutePath
                            + "): no read and/or write access");
                }
            } else {
                throw new BadConfigurationException(propertyName + "(" + absolutePath
                        + "): already exists, but is no directory");
            }
        } else if (dir.mkdirs()) {
            log.info("Successfull created " + propertyName + "(" + absolutePath + ")");
        } else {
            throw new BadConfigurationException("Could not create " + propertyName + "(" + absolutePath + ")");
        }
        return absolutePath;
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
    }
}
