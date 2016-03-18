/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.apeapi.resources;

import eu.archivesportaleurope.apeapi.response.common.OverViewResponse;
import eu.archivesportaleurope.apeapi.response.ead.EadResponseSet;
import eu.archivesportaleurope.apeapi.services.CurrentLevelService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
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
@Path("/content")
@Api("/content")
public class ContentResource {

    @Autowired
    CurrentLevelService currentLevelService;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public void setCurrentLevelService(CurrentLevelService currentLevelService) {
        this.currentLevelService = currentLevelService;
    }

   

    @GET
    @Path("/{id}/overview")
    @PreAuthorize("hasRole('ROLE_USER')")
    @ApiOperation(value = "Return overview response of an EAD item",
            response = OverViewResponse.class
    )
    @ApiResponses(value = {
        @ApiResponse(code = 500, message = "Internal server error"),
        @ApiResponse(code = 400, message = "Bad request")
    })
    @Produces(MediaType.APPLICATION_JSON)
    public Response getContentOverview(@PathParam("id") String id) {
        OverViewResponse overViewResponse = currentLevelService.findOverviewByClId(new Long(id.substring(1)));
        return Response.ok().entity(overViewResponse).build();
    }
}
