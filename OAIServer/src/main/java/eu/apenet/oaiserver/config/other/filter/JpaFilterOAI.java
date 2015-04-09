package eu.apenet.oaiserver.config.other.filter;

import eu.archivesportaleurope.persistence.jpa.JpaUtil;
import org.apache.log4j.Logger;

import javax.servlet.*;
import java.io.IOException;

/**
 * Created by yoannmoranville on 09/04/15.
 */
public class JpaFilterOAI implements Filter {
    private static final Logger log = Logger.getLogger(JpaFilterOAI.class);
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
        log.error(e.getMessage(), e);
    }

    public void destroy() {
    }
}
