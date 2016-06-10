/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.apeapi.resources;

import eu.archivesportaleurope.apeapi.common.datatypes.ServerConstants;
import eu.archivesportaleurope.apeapi.exceptions.AppException;
import eu.archivesportaleurope.apeapi.exceptions.InternalErrorException;
import eu.archivesportaleurope.apeapi.response.ContentResponseClevel;
import eu.archivesportaleurope.apeapi.response.ContentResponseEad;
import eu.archivesportaleurope.apeapi.response.common.DetailContent;
import eu.archivesportaleurope.apeapi.services.EadContentService;
import gov.loc.ead.C;
import gov.loc.ead.Ead;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ResourceContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.Provider;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

/**
 *
 * @author mahbub
 */
@Provider
@Path("/content")
@Api("/content")
@Produces({ServerConstants.APE_API_V1})
public class ContentResource {

    @Autowired
    private EadContentService eadContentService;
    
    @Context
    private ServletContext servletContext;
    
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final JAXBContext eadContext;
    private final JAXBContext clevelContext;
    private final Unmarshaller eadUnmarshaller;
    private final Unmarshaller cUnmarshaller;

    public ContentResource() throws JAXBException {
        this.eadContext = JAXBContext.newInstance(Ead.class);
        this.clevelContext = JAXBContext.newInstance(C.class);
        this.eadUnmarshaller = eadContext.createUnmarshaller();
        this.cUnmarshaller = clevelContext.createUnmarshaller();
    }

    //*
    @GET
    @Path("/descriptiveUnit/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    @ApiOperation(value = "Return content response of descriptive unit",
            response = ContentResponseClevel.class
    )
    @ApiResponses(value = {
        @ApiResponse(code = 500, message = "Internal server error"),
        @ApiResponse(code = 400, message = "Bad request"),
        @ApiResponse(code = 401, message = "Unauthorized")
    })
    @Consumes({ServerConstants.APE_API_V1})
    public Response getClevelContent(@PathParam("id") String id) {
        try {
            DetailContent detailContent = eadContentService.findClevelContent(id);

            ContentResponseClevel contentResponse = new ContentResponseClevel();

            contentResponse.setId(id);
            contentResponse.setRepositoryId(detailContent.getAiId());
            contentResponse.setRepository(detailContent.getAiRepoName());
            contentResponse.setUnitId(detailContent.getUnitId());
            contentResponse.setUnitTitle(detailContent.getUnitTitle());

            InputStream stream = new ByteArrayInputStream(detailContent.getXml().getBytes());
            C clevel = (C) cUnmarshaller.unmarshal(stream);
            contentResponse.setContent(clevel);
            return Response.ok().entity(contentResponse).build();

        } catch (WebApplicationException e) {
            logger.debug(ServerConstants.WEB_APP_EXCEPTION, e);
            return  e.getResponse();
        } catch (Exception e) {
            logger.debug(ServerConstants.UNKNOWN_EXCEPTION, e);
            AppException errMsg = new InternalErrorException(e.getMessage());
            return errMsg.getResponse();
        }
    }
//*/
    //*

    @GET
    @Path("/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    @ApiOperation(value = "Return content response of an EAD item",
            response = ContentResponseEad.class
    )
    @ApiResponses(value = {
        @ApiResponse(code = 500, message = "Internal server error"),
        @ApiResponse(code = 400, message = "Bad request"),
        @ApiResponse(code = 401, message = "Unauthorized")
    })
    @Consumes({ServerConstants.APE_API_V1})
    public Response getContent(@PathParam("id") String id) {
        try {
            DetailContent detailContent = eadContentService.findEadContent(id);

            ContentResponseEad contentResponse = new ContentResponseEad();

            contentResponse.setId(id);
            contentResponse.setRepositoryId(detailContent.getAiId());
            contentResponse.setRepository(detailContent.getAiRepoName());
            contentResponse.setUnitId(detailContent.getUnitId());
            contentResponse.setUnitTitle(detailContent.getUnitTitle());

            InputStream stream = new ByteArrayInputStream(detailContent.getXml().getBytes());
            Ead ead = (Ead) eadUnmarshaller.unmarshal(stream);
            contentResponse.setContent(ead);
            return Response.ok().entity(contentResponse).build();

        } catch (WebApplicationException e) {
            logger.debug(ServerConstants.WEB_APP_EXCEPTION, e);
            return  e.getResponse();
        } catch (Exception e) {
            logger.debug(ServerConstants.UNKNOWN_EXCEPTION, e);
            AppException errMsg = new InternalErrorException(e.getMessage());
            return errMsg.getResponse();
        }
    }
//*/
    //*

    @GET
    @Path("download/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    @ApiOperation(value = "Download raw xml of an EAD item",
            response = String.class
    )
    @ApiResponses(value = {
        @ApiResponse(code = 500, message = "Internal server error"),
        @ApiResponse(code = 400, message = "Bad request"),
        @ApiResponse(code = 401, message = "Unauthorized")
    })
    @Consumes({ServerConstants.APE_API_V1})
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response downloadXmlContent(@PathParam("id") String id) {
        try {
            eu.apenet.persistence.vo.Ead ead = eadContentService.findEadById(id);
            String repoPath = this.servletContext.getInitParameter(ServerConstants.REPOSITORY_DIR_PATH);
            File file = new File(repoPath + ead.getPath());
            ResponseBuilder response = Response.ok((Object) file);
            response.header("Content-Disposition", "attachment; filename="+id+".xml");
            return response.build();
        } catch (WebApplicationException e) {
            logger.debug(ServerConstants.WEB_APP_EXCEPTION, e);
            return  e.getResponse();
        } catch (Exception e) {
            logger.debug(ServerConstants.UNKNOWN_EXCEPTION, e);
            AppException errMsg = new InternalErrorException(e.getMessage());
            return errMsg.getResponse();
        }
    }
//*/
}
