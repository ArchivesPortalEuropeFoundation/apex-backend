/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.apeapi.response.utils;

import eu.archivesportaleurope.apeapi.exceptions.UnauthorizeUserException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import org.springframework.security.access.AccessDeniedException;

/**
 *
 * @author M.Mozadded
 */
@Provider
public class AccessDeniedMapper implements ExceptionMapper<AccessDeniedException> {
    @Override
    public Response toResponse(AccessDeniedException e) {
        WebApplicationException unAuthExp = new UnauthorizeUserException("You are not authorized to use this service.", "" );
        return unAuthExp.getResponse();
    }
}