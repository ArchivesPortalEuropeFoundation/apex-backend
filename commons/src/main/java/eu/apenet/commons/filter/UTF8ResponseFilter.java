package eu.apenet.commons.filter;

import org.apache.log4j.Logger;

import javax.servlet.*;
import java.io.IOException;

/**
 * User: Yoann Moranville Date: Nov 4, 2010
 *
 * @author Yoann Moranville
 */
public class UTF8ResponseFilter implements Filter {

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
