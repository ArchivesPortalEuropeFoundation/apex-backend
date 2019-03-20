/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.apeapi.resources;

import de.staatsbibliothek_berlin.eac.EacCpf;
import eu.archivesportaleurope.apeapi.common.datatypes.ServerConstants;
import eu.archivesportaleurope.apeapi.exceptions.AppException;
import eu.archivesportaleurope.apeapi.exceptions.InternalErrorException;
import eu.archivesportaleurope.apeapi.response.ContentResponseClevel;
import eu.archivesportaleurope.apeapi.response.ContentResponseEacCpf;
import eu.archivesportaleurope.apeapi.response.ContentResponseEad;
import eu.archivesportaleurope.apeapi.response.common.DetailContent;
import eu.archivesportaleurope.apeapi.services.EacCpfContentService;
import eu.archivesportaleurope.apeapi.services.EadContentService;
import gov.loc.ead.C;
import gov.loc.ead.Ead;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import org.eclipse.persistence.jaxb.MarshallerProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

/**
 *
 * @author mahbub
 */
@Component
@Path("/content")
@Api("/content")
@Produces({ServerConstants.APE_API_V1})
public class ContentResource {

    @Autowired
    private EadContentService eadContentService;
    
    @Autowired
    private EacCpfContentService eacCpfContentService;
    
    @Autowired
    private ServletContext servletContext;
    
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final JAXBContext eadContext;
    private final Unmarshaller eadUnmarshaller;
    
    private final JAXBContext eacCpfContext;
    private final Unmarshaller eacCpfUnmarshaller;
    
    private final JAXBContext clevelContext;    
    private final Unmarshaller cUnmarshaller;

    public ContentResource() throws JAXBException {
        this.eadContext = JAXBContext.newInstance(Ead.class);
        this.eadUnmarshaller = eadContext.createUnmarshaller();
        
        this.clevelContext = JAXBContext.newInstance(C.class);
        this.cUnmarshaller = clevelContext.createUnmarshaller();
        
        this.eacCpfContext = JAXBContext.newInstance(EacCpf.class);
        this.eacCpfUnmarshaller = eacCpfContext.createUnmarshaller();
    }

    //*
    @GET
    @Path("/ead/clevel/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    @ApiOperation(value = "Return content response of clevel",
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

            ContentResponseClevel contentResponse = new ContentResponseClevel(detailContent, id);

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
    @Path("/ead/archdesc/{id}")
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
    public Response getEadContent(@PathParam("id") String id) {
        try {
            DetailContent detailContent = eadContentService.findEadContent(id);

            ContentResponseEad contentResponse = new ContentResponseEad(detailContent, id);

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
    @Path("/eac-cpf/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    @ApiOperation(value = "Return content response of an EAC-CPF item",
            response = ContentResponseEacCpf.class
    )
    @ApiResponses(value = {
        @ApiResponse(code = 500, message = "Internal server error"),
        @ApiResponse(code = 400, message = "Bad request"),
        @ApiResponse(code = 401, message = "Unauthorized")
    })
    @Consumes({ServerConstants.APE_API_V1})
    public Response getEacCpfContent(@PathParam("id") String id) {
        try {
            eu.apenet.persistence.vo.EacCpf eacCpf = eacCpfContentService.findEacCpfById(id);
            String repoPath = this.servletContext.getInitParameter(ServerConstants.REPOSITORY_DIR_PATH);
            File file = new File(repoPath + eacCpf.getPath());
            FileInputStream fins = new FileInputStream(file);
            ContentResponseEacCpf contentResponse = new ContentResponseEacCpf(eacCpf);
            
            EacCpf eacCpfJson = (EacCpf) eacCpfUnmarshaller.unmarshal(fins);
            
            contentResponse.setContent(eacCpfJson);
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
}
