/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.apeapi.response.utils;

import eu.archivesportaleurope.apeapi.exceptions.ViolationException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 *
 * @author kaisar
 */
@Provider
public class BadRequestMapper implements ExceptionMapper<ConstraintViolationException> {

    @Override
    public Response toResponse(ConstraintViolationException exception) {
        String msg = "";
        for (ConstraintViolation<?> violation : exception.getConstraintViolations()) {
            msg += violation.getMessage() + "|";
        }

        WebApplicationException applicationException = new ViolationException(msg, exception.getMessage());
        return applicationException.getResponse();
    }

}
