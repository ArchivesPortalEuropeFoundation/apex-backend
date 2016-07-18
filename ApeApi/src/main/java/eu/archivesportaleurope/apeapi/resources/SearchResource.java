/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.apeapi.resources;

import eu.archivesportaleurope.apeapi.common.datatypes.ServerConstants;
import eu.archivesportaleurope.apeapi.exceptions.AppException;
import eu.archivesportaleurope.apeapi.exceptions.InternalErrorException;
import eu.archivesportaleurope.apeapi.request.SearchDocRequest;
import eu.archivesportaleurope.apeapi.request.SearchRequest;
import eu.archivesportaleurope.apeapi.response.ead.EadDocResponseSet;
import eu.archivesportaleurope.apeapi.response.eaccpf.EacCpfFacetedResponseSet;
import eu.archivesportaleurope.apeapi.response.ead.EadFactedResponseSet;
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
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
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
public class SearchResource {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    SearchService eadSearch;
    @Autowired
    SearchService eacCpfSearch;

    @POST
    @Path("/ead")
    @PreAuthorize("hasRole('ROLE_USER')")
    @ApiOperation(value = "Return search results based on query",
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
        try {
            QueryResponse queryResponse = eadSearch.searchOpenData(searchRequest);
            EadFactedResponseSet eadResponseSet = new EadFactedResponseSet(queryResponse);
            return Response.ok().entity(eadResponseSet).build();
        } catch (WebApplicationException e) {
            logger.debug(ServerConstants.WEB_APP_EXCEPTION, e);
            return e.getResponse();
        } catch (Exception e) {
            logger.debug(ServerConstants.UNKNOWN_EXCEPTION, e);
            AppException errMsg = new InternalErrorException(e.getMessage());
            return errMsg.getResponse();
        }
    }

    @POST
    @Path("/eac-cpf")
    @PreAuthorize("hasRole('ROLE_USER')")
    @ApiOperation(value = "Return eac-cpf search results based on query",
            response = EacCpfFacetedResponseSet.class
    )
    @ApiResponses(value = {
        @ApiResponse(code = 500, message = "Internal server error"),
        @ApiResponse(code = 400, message = "Bad request"),
        @ApiResponse(code = 401, message = "Unauthorized")
    })

    @Consumes({ServerConstants.APE_API_V1})
    public Response eacCpfSearch(@ApiParam(value = "Search EAC units\nCount should not be more than 50", required = true) @Valid SearchRequest searchRequest) {
        try {
            return Response.ok().entity(new EacCpfFacetedResponseSet(eacCpfSearch
                    .searchOpenData(searchRequest))).build();
        } catch (WebApplicationException e) {
            logger.debug(ServerConstants.WEB_APP_EXCEPTION, e);
            return e.getResponse();
        } catch (Exception e) {
            logger.debug(ServerConstants.UNKNOWN_EXCEPTION, e);
            AppException errMsg = new InternalErrorException(e.getMessage());
            return errMsg.getResponse();
        }
    }

    @POST
    @Path("/ead/docList")
    @PreAuthorize("hasRole('ROLE_USER')")
    @ApiOperation(value = "Return search results based on query",
            response = EadDocResponseSet.class
    )
    @ApiResponses(value = {
        @ApiResponse(code = 500, message = "Internal server error"),
        @ApiResponse(code = 400, message = "Bad request"),
        @ApiResponse(code = 401, message = "Unauthorized")
    })
    @Consumes({ServerConstants.APE_API_V1})
    public Response context(
            @ApiParam(value = "Search EAD units\nCount should not be more than 50", required = true) @Valid SearchDocRequest searchRequest
    ) {
        try {
            return Response.ok().entity(eadSearch.getEadList(searchRequest)).build();
        } catch (WebApplicationException e) {
            logger.debug(ServerConstants.WEB_APP_EXCEPTION, e);
            return e.getResponse();
        } catch (Exception e) {
            logger.debug(ServerConstants.UNKNOWN_EXCEPTION, e);
            AppException errMsg = new InternalErrorException(e.getMessage());
            return errMsg.getResponse();
        }
    }

}
