/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.apeapi.authentication;

import eu.archivesportaleurope.apeapi.common.datatypes.ServerConstants;
import javax.servlet.http.HttpServletRequest;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;

/**
 *
 * @author M.Mozadded
 */
public class UrlParametersAuthenticationFilter extends
        AbstractPreAuthenticatedProcessingFilter {
    @Override
    protected Object getPreAuthenticatedPrincipal(HttpServletRequest request) {
        return true;
    }

    @Override
    protected Object getPreAuthenticatedCredentials(HttpServletRequest request) {
        String authSessionId = request.getHeader(ServerConstants.HEADER_API_KEY);
        if (authSessionId==null) {
            return null;
        }
        String pathInfo = request.getPathInfo();
        String[] credentials = new String[2];
        credentials[0] = authSessionId;
        credentials[1] = pathInfo;
        //we can add more!
        return credentials;
    }
}
