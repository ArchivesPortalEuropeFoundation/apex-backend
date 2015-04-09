package eu.apenet.oaiserver.config.other.listener;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.xml.DOMConfigurator;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.File;

/**
 * Created by yoannmoranville on 09/04/15.
 */
public class Log4jListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        initLog4j();
    }

    private void initLog4j(){
        String log4jLocation = "none/log4j.xml";
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

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
    }
}
