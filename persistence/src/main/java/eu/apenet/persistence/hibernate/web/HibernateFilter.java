package eu.apenet.persistence.hibernate.web;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.log4j.Logger;

import eu.apenet.persistence.hibernate.HibernateUtil;

/**
 * Filter for closing and committing transactions
 * 
 * @author bverhoef
 *
 */
public class HibernateFilter implements Filter {

    private static final Logger log = Logger.getLogger(HibernateFilter.class);
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
                HibernateUtil.commitDatabaseTransaction();
            } catch (Exception de) {
            	logError(de);
            }

        } catch (Exception e) {
        	logError(e);
            // If an exception occurs and there is an open transaction, roll back the transaction.
            try {
            	
                HibernateUtil.rollbackDatabaseTransaction();
            }

            catch (Exception de) {
            	logError(de);
            }

        } 
        finally {

            // No matter what happens, close the Session.
            try {
                HibernateUtil.closeDatabaseSession();
            }

            catch (Exception de) {
            	logError(de);
            }

        }
    }

    private void logError(Throwable e){
    	if (logStackTraces){
    		log.error(e.getMessage(), e);
    	}else {
    		log.error(e.getMessage());
    	}
    }
    /**
     * This method is used to get a nice formatted string into a email.
     * @param e
     * @return
     */
    private String stackTraceToString(Throwable e) {
        String result = e.getMessage() + ":\n";
        result += e.getClass().toString() + "\n";
        for (int i = 0; i < e.getStackTrace().length; i++) {
            result += "\t" + (e.getStackTrace())[i].toString() + "\n";
        }
        return result;
    }

    public void destroy() {
    }

}