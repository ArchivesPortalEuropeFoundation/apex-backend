/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.apeapi.resources;

import eu.archivesportaleurope.apeapi.request.ApiKeyRequest;
import eu.archivesportaleurope.apeapi.response.ApiKeyResponse;
import eu.archivesportaleurope.apeapi.transaction.repository.ApiKeyRepo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import javax.validation.Valid;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author Mahbub
 */
@Component
@Path("/apikey")
@Api("/apikey")
public class ApiKeyResource {
//    @Autowired
//    ApiKeyRepo apiKeyRepo;
    
    @POST
    @Path("/")
    @ApiOperation(value = "Return apikey besed on passkey",
            response = ApiKeyResponse.class
    )
    @ApiResponses(value = {
        @ApiResponse(code = 500, message = "Internal server error"),
        @ApiResponse(code = 400, message = "Bad request")
    })
    @Produces(MediaType.APPLICATION_JSON)
    public Response getKey(
            @ApiParam(value = "", required = true) @Valid ApiKeyRequest apiKeyRequest
    ) {
        return null;
    }
}
