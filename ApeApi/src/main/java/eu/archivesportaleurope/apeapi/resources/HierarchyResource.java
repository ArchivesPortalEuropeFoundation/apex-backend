/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.apeapi.resources;

import eu.archivesportaleurope.apeapi.common.datatypes.ServerConstants;
import eu.archivesportaleurope.apeapi.request.PageRequest;
import eu.archivesportaleurope.apeapi.response.hierarchy.HierarchyResponseSet;
import eu.archivesportaleurope.apeapi.services.EadSearchService;
import eu.archivesportaleurope.apeapi.services.SearchService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;

/**
 *
 * @author kaisar
 */
@Component
@Path("/hierarchy")
@Api("/hierarchy")
@Produces({ServerConstants.APE_API_V1})
public class HierarchyResource extends ApiServiceProcessor {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    EadSearchService eadSearch;
    @Autowired
    SearchService eacCpfSearch;

    @POST
    @Path("/ead/{id}/ancestors")
    @PreAuthorize("hasRole('ROLE_USER')")
    @ApiOperation(value = "Get all ancestors of a given id",
            response = HierarchyResponseSet.class
    )
    @ApiResponses(value = {
        @ApiResponse(code = 500, message = "Internal server error"),
        @ApiResponse(code = 400, message = "Bad request"),
        @ApiResponse(code = 401, message = "Unauthorized")
    })
    @Consumes({ServerConstants.APE_API_V1})
    public Response getAncestors(@PathParam("id") String id) {
        ApiService apiService = () -> {
            HierarchyResponseSet response = eadSearch.getAncestors(id);
            return Response.ok().entity(response).build();
        };
        
        return super.process(apiService);
    }

    @POST
    @Path("/ead/{id}/children")
    @PreAuthorize("hasRole('ROLE_USER')")
    @ApiOperation(value = "Get all children of a given id",
            response = HierarchyResponseSet.class
    )
    @ApiResponses(value = {
        @ApiResponse(code = 500, message = "Internal server error"),
        @ApiResponse(code = 400, message = "Bad request"),
        @ApiResponse(code = 401, message = "Unauthorized")
    })
    @Consumes({ServerConstants.APE_API_V1})
    public Response getChildren(
            @PathParam("id") String id,
            @ApiParam(value = "Search EAD units\nCount should not be more than 50", required = true) @Valid PageRequest searchRequest
    ) {
        ApiService apiService = () -> {
            HierarchyResponseSet response = eadSearch.getChildren(id, searchRequest);
            return Response.ok().entity(response).build();
        };
        
        return super.process(apiService);
    }

}
