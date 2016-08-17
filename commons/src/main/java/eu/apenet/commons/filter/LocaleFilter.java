package eu.apenet.commons.filter;

import org.apache.log4j.Logger;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;
import java.util.Locale;

public class LocaleFilter implements Filter {

    private final static Logger LOG = Logger.getLogger(LocaleFilter.class);
    private final static String[] languagesAllowed = {"de", "el", "en", "es", "fi", "fr", "ga", "lv", "mt", "nl", "pl", "pt", "sl", "sv", "xx", "tag"};

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        if (((HttpServletRequest) servletRequest).getSession().getAttribute("WW_TRANS_I18N_LOCALE") == null) {
            ((HttpServletRequest) servletRequest).getSession().setAttribute("WW_TRANS_I18N_LOCALE", servletRequest.getLocale());
            ((HttpServletRequest) servletRequest).getSession().setAttribute("org.apache.tiles.LOCALE", servletRequest.getLocale());
        } else {
            ((HttpServletRequest) servletRequest).getSession().setAttribute("org.apache.tiles.LOCALE", ((HttpServletRequest) servletRequest).getSession().getAttribute("WW_TRANS_I18N_LOCALE"));
        }
        String loc = ((HttpServletRequest) servletRequest).getSession().getAttribute("WW_TRANS_I18N_LOCALE").toString();
        if (!Arrays.asList(languagesAllowed).contains(loc.toLowerCase().substring(0, 2))) {
            ((HttpServletRequest) servletRequest).getSession().setAttribute("org.apache.tiles.LOCALE", new Locale("en"));
            ((HttpServletRequest) servletRequest).getSession().setAttribute("WW_TRANS_I18N_LOCALE", new Locale("en"));
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
    }
}
