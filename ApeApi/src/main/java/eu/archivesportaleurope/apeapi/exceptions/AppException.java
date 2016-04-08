/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.apeapi.exceptions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.archivesportaleurope.apeapi.response.ErrorMessageResponse;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class to map application related exceptions
 *
 * @author amacoder
 *
 */
public class AppException extends WebApplicationException {

    private final transient Logger logger = LoggerFactory.getLogger(this.getClass());
    private static final long serialVersionUID = -8999932578270387947L;

    /**
     * contains redundantly the HTTP status of the response sent back to the
     * client in case of error, so that the developer does not have to look into
     * the response headers. If null a default
     */
    private final Integer status;

    /**
     * application specific error code
     */
    private final int code;

    /**
     * link documenting the exception
     */
    private final String link;

    private final String message;

    /**
     * detailed error description for developers
     */
    private final String developerMessage;

    /**
     *
     * @param status
     * @param code
     * @param message
     * @param developerMessage
     * @param link
     */
    public AppException(int status, int code, String message,
            String developerMessage, String link) {
        this.status = status;
        this.code = code;
        this.message = message;
        this.developerMessage = developerMessage;
        this.link = link;
    }

    public int getStatus() {
        return status;
    }

    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return this.message;
    }

    public String getDeveloperMessage() {
        return developerMessage;
    }

    public String getLink() {
        return link;
    }

    @Override
    public Response getResponse() {
        return Response.status(status).type(MediaType.APPLICATION_JSON_TYPE)
                .entity(getErrorResponse()).build();
    }

    public ErrorMessageResponse getErrorResponse() {
        ErrorMessageResponse response = new ErrorMessageResponse();
        response.setStatus(this.status);
        response.setCode(this.code);
        response.setDeveloperMessage(developerMessage);
        response.setMessage(this.message);
        return response;
    }

    @Override
    public String toString() {
        String reqStr = "";
        try {
            ObjectMapper mapper = new ObjectMapper();
            reqStr = mapper.writeValueAsString(getErrorResponse());
        } catch (JsonProcessingException e) {
            logger.debug(e.getMessage(), e);
        }

        return reqStr;
    }
}
