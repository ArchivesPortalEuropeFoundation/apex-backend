/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.apeapi.resources;

import eu.archivesportaleurope.apeapi.exceptions.AppException;
import eu.archivesportaleurope.apeapi.exceptions.InternalErrorException;
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
    @Path("/getInstitute/{page}/{limit}")
    @PreAuthorize("hasRole('ROLE_USER')")
    @ApiOperation(value = "return list of Archival institute", response = ArchivalInstitutesResponse.class)
    @ApiResponses(value = {
        @ApiResponse(code = 500, message = "Internal server error")})
    @Produces({"application/vnd.ape-v1+json"})
    public Response getInsByOpenData(@ApiParam(value = "Page number (Starts form 0)", required = true) @PathParam("page") int page, @ApiParam(value = "limit can't be more than 50", required = true) @PathParam("limit") int limit) {
        if (page < 0) {
            page = 0;
        }
        if (limit < 0 || limit > 50) {
            limit = 50;
        }
        try {
            return Response.ok().entity(aiStatService.getAiWithOpenDataEnabled(page, limit)).build();
        } catch (WebApplicationException e) {
            logger.error("WebApplicationException", e);
            return e.getResponse();
        } catch (Exception e) {
            logger.error("Exception", e);
            AppException errMsg = new InternalErrorException(e.getMessage());
            return errMsg.getResponse();
        }
    }
}
