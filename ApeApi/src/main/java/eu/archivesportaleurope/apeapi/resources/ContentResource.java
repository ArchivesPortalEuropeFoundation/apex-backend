/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.apeapi.resources;

import eu.archivesportaleurope.apeapi.exceptions.AppException;
import eu.archivesportaleurope.apeapi.exceptions.InternalErrorException;
import eu.archivesportaleurope.apeapi.response.common.OverViewFrontPageContent;
import eu.archivesportaleurope.apeapi.response.OverViewResponse;
import eu.archivesportaleurope.apeapi.services.EadContentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
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
    EadContentService eadContentService;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

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
        try {
            OverViewFrontPageContent frontPageResponse = eadContentService.findEadContent(id);
            OverViewResponse overViewResponse = new OverViewResponse();
            overViewResponse.setId(id);
            overViewResponse.setXmlType(frontPageResponse.getXmlType());
            overViewResponse.setAiId(frontPageResponse.getAiId());
            if (frontPageResponse.getEadContent() != null) {
                overViewResponse.setOverViewXml(frontPageResponse.getEadContent().getXml());
                overViewResponse.setUnitId(frontPageResponse.getEadContent().getEadid());
                overViewResponse.setUnitTitle(frontPageResponse.getEadContent().getUnittitle());
            } else {
                overViewResponse.setUnitId(frontPageResponse.getCurrentLevel().getUnitid());
                overViewResponse.setOverViewXml(frontPageResponse.getCurrentLevel().getXml());
                overViewResponse.setUnitTitle(frontPageResponse.getCurrentLevel().getUnittitle());
            }
            return Response.ok().entity(overViewResponse).build();
        } catch (WebApplicationException e) {
            logger.error("WebApplicationException", e.getCause());
            return ((WebApplicationException) e).getResponse();
        } catch (Exception e) {
            logger.error("Exception", e);
            AppException errMsg = new InternalErrorException(e.getMessage());
            return ((WebApplicationException) errMsg).getResponse();
        }
    }
}
