/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.apeapi.resources;

import eu.archivesportaleurope.apeapi.common.datatypes.ServerConstants;
import eu.archivesportaleurope.apeapi.exceptions.AppException;
import eu.archivesportaleurope.apeapi.exceptions.InternalErrorException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author mahbub
 */
public class ApiServiceProcessor {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    
    protected Response process(ApiService service) {
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
    }
}
