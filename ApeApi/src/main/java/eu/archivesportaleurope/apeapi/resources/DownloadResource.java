/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.apeapi.resources;

import eu.apenet.persistence.vo.EacCpf;
import eu.archivesportaleurope.apeapi.common.datatypes.ServerConstants;
import eu.archivesportaleurope.apeapi.services.EacCpfContentService;
import eu.archivesportaleurope.apeapi.services.EadContentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.io.File;
import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
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
@Path("/download")
@Api("/download")
@Produces({ServerConstants.APE_API_V1})
public class DownloadResource extends ApiServiceProcessor {
    @Autowired
    private EadContentService eadContentService;
    
    @Autowired
    private EacCpfContentService eacCpfContentService;
    
    @Autowired
    private ServletContext servletContext;
    
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    //*

    @GET
    @Path("ead/{id}")
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
    public Response downloadXmlEad(@PathParam("id") String id) {
        ApiService apiService = () -> {
            eu.apenet.persistence.vo.Ead ead = eadContentService.findEadById(id);
            String repoPath = this.servletContext.getInitParameter(ServerConstants.REPOSITORY_DIR_PATH);
            File file = new File(repoPath + ead.getPath());
            Response.ResponseBuilder response = Response.ok((Object) file);
            response.header("Content-Disposition", "attachment; filename="+id+".xml");
            return response.build();
        };
        
        return super.process(apiService);
    }
//*/
    
    //*

    @GET
    @Path("eac-cpf/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    @ApiOperation(value = "Download raw xml of an EAC-CPF item",
            response = String.class
    )
    @ApiResponses(value = {
        @ApiResponse(code = 500, message = "Internal server error"),
        @ApiResponse(code = 400, message = "Bad request"),
        @ApiResponse(code = 401, message = "Unauthorized")
    })
    @Consumes({ServerConstants.APE_API_V1})
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response downloadXmlEacCpf(@PathParam("id") String id) {
        ApiService apiService = () -> {
            EacCpf eacCpf = eacCpfContentService.findEacCpfById(id);
            String repoPath = this.servletContext.getInitParameter(ServerConstants.REPOSITORY_DIR_PATH);
            File file = new File(repoPath + eacCpf.getPath());
            Response.ResponseBuilder response = Response.ok((Object) file);
            response.header("Content-Disposition", "attachment; filename="+id+".xml");
            return response.build();
        };
        
        return super.process(apiService);
    }
//*/
}
