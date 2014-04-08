package eu.archivesportaleurope.persistence.jpa.web;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.log4j.Logger;

import eu.archivesportaleurope.persistence.jpa.JpaUtil;

/**
 * Filter for closing and committing transactions
 * 
 * @author bverhoef
 *
 */
public class JpaFilter implements Filter {

    private static final Logger log = Logger.getLogger(JpaFilter.class);
    private static final String LOG_STACKTRACES = "LOG_STACKTRACES";
    private boolean logStackTraces = false;
    public void init(FilterConfig filterConfig){
    	String logStackTracesString = filterConfig.getServletContext().getInitParameter(LOG_STACKTRACES);
    	logStackTraces = "true".equals(logStackTracesString);
        log.info("Hibernate Filter started. Log stacktraces=" + logStackTraces);
    }

    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException {

        // There is actually no explicit "opening" of a Session, the
        // first call to HibernateUtil.beginTransaction() will get
        // a fresh Session.
        try {
            chain.doFilter(request, response);
            Exception exception = (Exception) request.getAttribute("javax.servlet.error.exception");
            if (exception != null){
            	throw exception;
            }
            // Commit any pending database transaction.
            try {
                JpaUtil.commitDatabaseTransaction();
            } catch (Exception de) {
            	logError(de);
            }

        } catch (Exception e) {
        	logError(e);
            // If an exception occurs and there is an open transaction, roll back the transaction.
            try {
            	
            	JpaUtil.rollbackDatabaseTransaction();
            }

            catch (Exception de) {
            	logError(de);
            }

        } 
        finally {

            // No matter what happens, close the Session.
            try {
            	JpaUtil.closeDatabaseSession();
            }

            catch (Exception de) {
            	logError(de);
            }

        }
    }

    private void logError(Throwable e){
    	if (logStackTraces){
    		log.error(e.getMessage(),e);
    	}else {
    		log.error(JpaUtil.generateThrowableLog(e));
    	}
    }

    public void destroy() {
    }

}