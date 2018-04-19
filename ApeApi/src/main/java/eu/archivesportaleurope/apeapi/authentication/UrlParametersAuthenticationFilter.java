/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.apeapi.authentication;

import eu.archivesportaleurope.apeapi.common.datatypes.ServerConstants;
import eu.archivesportaleurope.apeapi.services.HttpRequestLogger;
import java.util.Enumeration;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;

/**
 *
 * @author M.Mozadded
 */
public class UrlParametersAuthenticationFilter extends
        AbstractPreAuthenticatedProcessingFilter {

    private final Logger localLogger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private HttpRequestLogger requestLogger;

    @Override
    protected Object getPreAuthenticatedPrincipal(HttpServletRequest request) {
        return true;
    }

    @Override
    protected Object getPreAuthenticatedCredentials(HttpServletRequest request) {
        String heardLog="", tmp;
        Enumeration<String> headerNames = request.getHeaderNames();
        while(headerNames.hasMoreElements()) {
            tmp = headerNames.nextElement();
            heardLog+=tmp+": "+request.getHeader(tmp)+"\n";
        }
        localLogger.debug("PreAuth Http header list: "+heardLog);
        String apiKey = request.getHeader(ServerConstants.HEADER_API_KEY);
        requestLogger.log(request);

        if (apiKey == null) {
            localLogger.debug("No apikey was provided, so reject the request");
            return null;
        }

        String pathInfo = request.getPathInfo();
        String[] credentials = new String[2];
        credentials[0] = apiKey;
        credentials[1] = pathInfo;
        return credentials;
    }
}
