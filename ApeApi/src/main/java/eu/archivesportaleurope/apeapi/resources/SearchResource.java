/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.apeapi.resources;

import eu.archivesportaleurope.apeapi.common.datatypes.ServerConstants;
import eu.archivesportaleurope.apeapi.request.SearchPageRequestWithUnitId;
import eu.archivesportaleurope.apeapi.request.QueryPageRequest;
import eu.archivesportaleurope.apeapi.request.SearchDocRequest;
import eu.archivesportaleurope.apeapi.request.SearchRequest;
import eu.archivesportaleurope.apeapi.response.eaccpf.EacCpfFacetedResponseSet;
import eu.archivesportaleurope.apeapi.response.ead.EadFactedDocResponseSet;
import eu.archivesportaleurope.apeapi.response.ead.EadFactedResponseSet;
import eu.archivesportaleurope.apeapi.response.ead.EadHierarchyResponseSet;
import eu.archivesportaleurope.apeapi.response.ead.EadResponseSet;
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
import org.apache.solr.client.solrj.response.QueryResponse;
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
@Path("/search")
@Api("/search")
@Produces({ServerConstants.APE_API_V1})
public class SearchResource extends ApiServiceProcessor {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    EadSearchService eadSearch;
    @Autowired
    SearchService eacCpfSearch;
    /*
    public Response process(ApiService service) {
        try {
            return service.serve();
        } catch (WebApplicationException e) {
            logger.debug(ServerConstants.WEB_APP_EXCEPTION, e);
            return e.getResponse();
        } catch (Exception e) {
            logger.debug(ServerConstants.UNKNOWN_EXCEPTION, e);
            AppException errMsg = new InternalErrorException(e.getMessage());
            return errMsg.getResponse();
        }
    }*/
    
    @POST
    @Path("/ead")
    @PreAuthorize("hasRole('ROLE_USER')")
    @ApiOperation(value = "Search for EAD",
            response = EadFactedResponseSet.class
    )
    @ApiResponses(value = {
        @ApiResponse(code = 500, message = "Internal server error"),
        @ApiResponse(code = 400, message = "Bad request"),
        @ApiResponse(code = 401, message = "Unauthorized")
    })
    @Consumes({ServerConstants.APE_API_V1})
    public Response search(
            @ApiParam(value = "Search EAD units\nCount should not be more than 50", required = true) @Valid SearchRequest searchRequest
    ) {
        ApiService apiService = () -> {
            QueryResponse queryResponse = eadSearch.searchOpenData(searchRequest);
            EadFactedResponseSet eadResponseSet = new EadFactedResponseSet(queryResponse);
            return Response.ok().entity(eadResponseSet).build();
        };
        return super.process(apiService);
    }

    @POST
    @Path("/eac-cpf")
    @PreAuthorize("hasRole('ROLE_USER')")
    @ApiOperation(value = "Search for EAC-CPF",
            response = EacCpfFacetedResponseSet.class
    )
    @ApiResponses(value = {
        @ApiResponse(code = 500, message = "Internal server error"),
        @ApiResponse(code = 400, message = "Bad request"),
        @ApiResponse(code = 401, message = "Unauthorized")
    })

    @Consumes({ServerConstants.APE_API_V1})
    public Response eacCpfSearch(@ApiParam(value = "Search EAC units\nCount should not be more than 50", required = true) @Valid SearchRequest searchRequest) {
        ApiService apiService = () -> {
            return Response.ok().entity(new EacCpfFacetedResponseSet(eacCpfSearch
                    .searchOpenData(searchRequest))).build();
        };
        
        return super.process(apiService);
    }

    @POST
    @Path("/ead/docList")
    @PreAuthorize("hasRole('ROLE_USER')")
    @ApiOperation(value = "Search for EAD docList",
            response = EadFactedDocResponseSet.class
    )
    @ApiResponses(value = {
        @ApiResponse(code = 500, message = "Internal server error"),
        @ApiResponse(code = 400, message = "Bad request"),
        @ApiResponse(code = 401, message = "Unauthorized")
    })
    @Consumes({ServerConstants.APE_API_V1})
    public Response searchEadDocs(
            @ApiParam(value = "Search EAD units\nCount should not be more than 50", required = true) @Valid SearchDocRequest searchRequest
    ) {
        ApiService apiService = () -> {
            QueryResponse response = eadSearch.getEadList(searchRequest);
            return Response.ok().entity(new EadFactedDocResponseSet(searchRequest, response)).build();
        };
        return super.process(apiService);
    }

    @POST
    @Path("/ead/{id}/descendants")
    @PreAuthorize("hasRole('ROLE_USER')")
    @ApiOperation(value = "Search the term in all descendants of a given id",
            response = EadFactedResponseSet.class
    )
    @ApiResponses(value = {
        @ApiResponse(code = 500, message = "Internal server error"),
        @ApiResponse(code = 400, message = "Bad request"),
        @ApiResponse(code = 401, message = "Unauthorized")
    })
    @Consumes({ServerConstants.APE_API_V1})
    public Response getDescendants(
            @PathParam("id") String id,
            @ApiParam(value = "Search EAD units\nCount should not be more than 50", required = true) @Valid SearchRequest searchRequest
    ) {
        ApiService apiService = () -> {
            QueryResponse response = eadSearch.getDescendants(id, searchRequest);
            return Response.ok().entity(new EadFactedResponseSet(response)).build();
        };
        
        return super.process(apiService);
    }

    @POST
    @Path("/ead/{id}/descendantsWithAncestors")
    @PreAuthorize("hasRole('ROLE_USER')")
    @ApiOperation(value = "Search all descendants (with the ancestors list) of a given ead id",
            response = EadHierarchyResponseSet.class
    )
    @ApiResponses(value = {
        @ApiResponse(code = 500, message = "Internal server error"),
        @ApiResponse(code = 400, message = "Bad request"),
        @ApiResponse(code = 401, message = "Unauthorized")
    })
    @Consumes({ServerConstants.APE_API_V1})
    public Response getDescendantsWithAncestors(
            @PathParam("id") String id,
            @ApiParam(value = "Search EAD units\nCount should not be more than 50", required = true) @Valid SearchRequest searchRequest
    ) {
        ApiService apiService = () -> {
            EadHierarchyResponseSet response = eadSearch.getDescendantsWithAncestors(id, searchRequest);
            return Response.ok().entity(response).build();
        };
        
        return super.process(apiService);
    }

    @POST
    @Path("/ead/{id}/children")
    @PreAuthorize("hasRole('ROLE_USER')")
    @ApiOperation(value = "Search the term in all children of a given id",
            response = EadResponseSet.class
    )
    @ApiResponses(value = {
        @ApiResponse(code = 500, message = "Internal server error"),
        @ApiResponse(code = 400, message = "Bad request"),
        @ApiResponse(code = 401, message = "Unauthorized")
    })
    @Consumes({ServerConstants.APE_API_V1})
    public Response getChildren(
            @PathParam("id") String id,
            @ApiParam(value = "Search EAD units\nCount should not be more than 50", required = true) @Valid QueryPageRequest searchRequest
    ) {
        ApiService apiService = () -> {
            QueryResponse response = eadSearch.getChildren(id, searchRequest);
            return Response.ok().entity(new EadResponseSet(response)).build();
        };
        
        return super.process(apiService);
    }

    @POST
    @Path("/searchEadFindingAidNo")
    @PreAuthorize("hasRole('ROLE_USER')")
    @ApiOperation(value = "Search Eads with FindingAidNo/FondsUnitId",
            response = EadFactedResponseSet.class
    )
    @ApiResponses(value = {
        @ApiResponse(code = 500, message = "Internal server error"),
        @ApiResponse(code = 400, message = "Bad request"),
        @ApiResponse(code = 401, message = "Unauthorized")
    })
    @Consumes({ServerConstants.APE_API_V1})
    public Response searchEadFindingAidNo (
            @ApiParam(value = "Search EAD units\nCount should not be more than 50", required = true) @Valid SearchPageRequestWithUnitId filteredSortedPageRequest
    ) {
        ApiService apiService = () -> {
            QueryResponse response = eadSearch.getEadsByFondsUnitId(filteredSortedPageRequest);
            return Response.ok().entity(new EadFactedResponseSet(response)).build();
        };
        
        return super.process(apiService);
    }
}
