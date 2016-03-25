/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.apeapi.resources;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.util.StatusPrinter;
import eu.archivesportaleurope.apeapi.common.datatypes.ServerConstants;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 *
 * @author sowrov
 */
@Component
@Path("/version")
//@Produces({"application/vnd.roz-v1+json"})
@Api(value = "/version")
public class VersionResource {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @GET
    @Path("current")
//    @Consumes({"application/vnd.roz-v1+json"})
    @ApiOperation(value = "Return current app version")
    @ApiResponses(value = {
        @ApiResponse(code = 500, message = "Internal server error")
    })
    public Response appVersion() {
        String appVersion = "{\"version\":\""+ServerConstants.APE_API_V1+"\"}";
        //Logback config test
//        // assume SLF4J is bound to logback in the current environment
//        LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
//        // print logback's internal status
//        StatusPrinter.print(lc);
        logger.debug("System name: " + System.getProperty("os.name"));
        logger.debug("My current version is " + appVersion);
        return Response.ok(appVersion).build();
    }

    @GET
    @Path("previous")
//    @Consumes({"application/vnd.roz-v2+json"})
    @ApiOperation(value = "Return previous app version")
    @ApiResponses(value = {
        @ApiResponse(code = 500, message = "Internal server error")
    })
    public Response appVersionPre() {
        String appVersion = "{\"version\":0.0}";
        logger.debug("My current version is " + appVersion);
        return Response.ok(appVersion).build();
    }
}
