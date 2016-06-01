/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.apeapi.resources;

import eu.archivesportaleurope.apeapi.common.datatypes.ServerConstants;
import eu.archivesportaleurope.apeapi.exceptions.AppException;
import eu.archivesportaleurope.apeapi.exceptions.InternalErrorException;
import eu.archivesportaleurope.apeapi.request.SearchRequest;
import eu.archivesportaleurope.apeapi.response.ead.EadResponseSet;
import eu.archivesportaleurope.apeapi.services.SearchService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.text.ParseException;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import org.apache.solr.client.solrj.SolrServerException;
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
    
    @POST
    @Path("/ead")
    @PreAuthorize("hasRole('ROLE_USER')")
    @ApiOperation(value = "Return search results based on query",
            response = EadResponseSet.class
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
            EadResponseSet eadResponseSet = eadSearch.searchOpenData(searchRequest);
            return Response.ok().entity(eadResponseSet).build();
        } catch (WebApplicationException e) {
            logger.debug("WebApplicationException", e);
            return  e.getResponse();
        } catch (SolrServerException | ParseException e) {
            logger.debug("Exception", e);
            AppException errMsg = new InternalErrorException(e.getMessage());
            return errMsg.getResponse();
        }
    }
}
