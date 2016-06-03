/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.apeapi.resources;

import com.google.gson.Gson;
import eu.archivesportaleurope.apeapi.common.datatypes.ServerConstants;
import eu.archivesportaleurope.apeapi.exceptions.AppException;
import eu.archivesportaleurope.apeapi.exceptions.InternalErrorException;
import eu.archivesportaleurope.apeapi.response.common.AutocompletionResponseSet;
import eu.archivesportaleurope.apeapi.response.common.SuggestionResponseSet;
import eu.archivesportaleurope.apeapi.services.SuggestionService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author kaisar
 */
@Component
//@Path("/suggestion")
//@Api("/suggestion")
public class SuggestionResource {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final Gson gson = new Gson();

    @Autowired
    SuggestionService suggestionService;

    //@POST
    //@Path("/autocompletion")
    @ApiOperation(value = "Return autocompletion", response = AutocompletionResponseSet.class)
    @ApiResponses(value = {
        @ApiResponse(code = 500, message = "Internal server error")})
    @Produces(MediaType.APPLICATION_JSON)
    public Response autocomplete(@ApiParam(value = "Search Term", required = true) String term) {
        try {
            AutocompletionResponseSet responseSet;
            if (term.length() > 2) {
                responseSet = suggestionService.autoComplete(term);
            } else {
                responseSet = new AutocompletionResponseSet();
            }
            return Response.ok().entity(gson.toJson(responseSet)).build();
        } catch (WebApplicationException e) {
            logger.debug(ServerConstants.WEB_APP_EXCEPTION, e);
            return  e.getResponse();
        } catch (Exception e) {
            logger.debug(ServerConstants.UNKNOWN_EXCEPTION, e);
            AppException errMsg = new InternalErrorException(e.getMessage());
            return errMsg.getResponse();
        }
    }

    //@POST
    //@Path("/spelling")
    @ApiOperation(value = "Return suggestion", response = SuggestionResponseSet.class)
    @ApiResponses(value = {
        @ApiResponse(code = 500, message = "Internal server error")})
    @Produces(MediaType.APPLICATION_JSON)
    public Response suggestion(@ApiParam(value = "Search Term", required = true) String term) {
        try {
            SuggestionResponseSet suggestionResponseSet = suggestionService.suggest(term);
            return Response.ok().entity(suggestionResponseSet).build();
        } catch (WebApplicationException e) {
            logger.debug(ServerConstants.WEB_APP_EXCEPTION, e);
            return  e.getResponse();
        } catch (Exception e) {
            logger.debug(ServerConstants.UNKNOWN_EXCEPTION, e);
            AppException errMsg = new InternalErrorException(e.getMessage());
            return errMsg.getResponse();
        }
    }
}
