/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.apeapi.resources;

import eu.archivesportaleurope.apeapi.exceptions.AppException;
import eu.archivesportaleurope.apeapi.exceptions.InternalErrorException;
import eu.archivesportaleurope.apeapi.exceptions.ViolationException;
import eu.archivesportaleurope.apeapi.response.ArchivalInstitutesResponse;
import eu.archivesportaleurope.apeapi.services.AiStatService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

/**
 *
 * @author kaisar
 */
@Component
@Path("/institute")
@Api("/institute")
public class ArchivalInstituteStatResource {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    AiStatService aiStatService;

    @GET
    @Path("/getInstitute/{startIndex}/{count}")
    @PreAuthorize("hasRole('ROLE_USER')")
    @ApiOperation(value = "return list of Archival institute", response = ArchivalInstitutesResponse.class)
    @ApiResponses(value = {
        @ApiResponse(code = 500, message = "Internal server error"),
        @ApiResponse(code = 400, message = "Bad request"),
        @ApiResponse(code = 401, message = "Unauthorized")
    })
    @Produces({"application/vnd.ape-v1+json"})
    public Response getInsByOpenData(@ApiParam(value = "Start Index (Starts form 0)", required = true) @PathParam("startIndex") int startIndex, 
            @ApiParam(value = "Count can't be more than 50", required = true) @PathParam("count") int count) {
        if (startIndex < 0) {
            startIndex = 0;
        }
        if (count < 1 || count > 50) {
            throw new ViolationException("Count can not be less than one and greater than 50", "Count was: "+count);
        }
        try {
            return Response.ok().entity(aiStatService.getAiWithOpenDataEnabled(startIndex, count)).build();
        } catch (WebApplicationException e) {
            logger.debug("WebApplicationException", e);
            return e.getResponse();
        } catch (Exception e) {
            logger.debug("Exception", e);
            AppException errMsg = new InternalErrorException(e.getMessage());
            return errMsg.getResponse();
        }
    }
}
