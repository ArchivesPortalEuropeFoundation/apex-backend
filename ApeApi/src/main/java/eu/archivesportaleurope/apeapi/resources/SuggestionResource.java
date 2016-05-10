/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.apeapi.resources;

import com.google.gson.Gson;
import eu.apenet.persistence.dao.EacCpfDAO;
import eu.apenet.persistence.dao.EadDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.archivesportaleurope.apeapi.exceptions.AppException;
import eu.archivesportaleurope.apeapi.exceptions.InternalErrorException;
import eu.archivesportaleurope.apeapi.response.common.AutocompletionResponseSet;
import eu.archivesportaleurope.apeapi.response.common.SuggestionResponseSet;
import eu.archivesportaleurope.apeapi.services.SuggestionService;
import eu.archivesportaleurope.persistence.jpa.JpaUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
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
            logger.error("WebApplicationException", e);
            return  e.getResponse();
        } catch (Exception e) {
            logger.error("Exception", e);
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
            logger.error("WebApplicationException", e);
            return  e.getResponse();
        } catch (Exception e) {
            logger.error("Exception", e);
            AppException errMsg = new InternalErrorException(e.getMessage());
            return errMsg.getResponse();
        }
    }

    //@POST
    //@Path("/test")
    @ApiOperation(value = "Return DB", response = String.class)
    @ApiResponses(value = {
        @ApiResponse(code = 500, message = "Internal server error")})
    @Produces(MediaType.TEXT_PLAIN)
    public Response dBTest() {
        try {
            JpaUtil.init();
            EadDAO eadDAO = DAOFactory.instance().getEadDAO();
            EacCpfDAO eca = DAOFactory.instance().getEacCpfDAO();
            return Response.ok().entity(eadDAO.getTotalCountOfUnits() + " " + eca.findAll().size()).build();
        } catch (WebApplicationException e) {
            logger.error("WebApplicationException", e);
            return  e.getResponse();
        } catch (Exception e) {
            logger.error("Exception", e);
            AppException errMsg = new InternalErrorException(e.getMessage());
            return errMsg.getResponse();
        }
    }
}
