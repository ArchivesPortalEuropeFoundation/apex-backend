package eu.apenet.oaiserver.config.other.filter;

import org.apache.log4j.Logger;

import javax.servlet.*;
import java.io.IOException;

/**
 * Created by yoannmoranville on 09/04/15.
 */
public class UTF8ResponseFilterOAI implements Filter {
    private Logger log = Logger.getLogger(getClass());
    private static final String UTF8 = "UTF-8";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        servletResponse.setContentType("text/html; charset=" + UTF8);
        servletRequest.setCharacterEncoding(UTF8);
        servletResponse.setCharacterEncoding(UTF8);
        log.debug("Response character encoding: " + servletResponse.getCharacterEncoding());
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
    }
}
