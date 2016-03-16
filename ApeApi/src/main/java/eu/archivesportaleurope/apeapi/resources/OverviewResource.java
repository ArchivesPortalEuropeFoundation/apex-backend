/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.apeapi.resources;

import eu.apenet.commons.types.XmlType;
import eu.apenet.persistence.dao.CLevelDAO;
import eu.apenet.persistence.vo.ArchivalInstitution;
import eu.apenet.persistence.vo.CLevel;
import eu.apenet.persistence.vo.Ead;
import eu.archivesportaleurope.apeapi.response.common.OverViewResponse;
import eu.archivesportaleurope.apeapi.transaction.repository.CLevelRepo;
import io.swagger.annotations.Api;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
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
@Path("/overview")
@Api("/overview")
public class OverviewResource {

    @Autowired
    CLevelRepo cLevelRepo;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public void setcLevelRepo(CLevelRepo cLevelRepo) {
        this.cLevelRepo = cLevelRepo;
    }

   

    @GET
    @Path("/simple/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSimpleOverview(@PathParam("id") String id) {
        CLevel currentLevel = cLevelRepo.findById(new Long(id.substring(1)));
        Ead ead = currentLevel.getEadContent().getEad();
        ArchivalInstitution archivalInstitution = ead.getArchivalInstitution();
        XmlType xmlType = XmlType.getContentType(ead);
        OverViewResponse overViewResponse = new OverViewResponse();
        overViewResponse.setXmlType(xmlType);
        overViewResponse.setCurrentLevel(currentLevel);
        overViewResponse.setAiId(archivalInstitution.getAiId());
        overViewResponse.setAiRepoCode(archivalInstitution.getEncodedRepositorycode());
        overViewResponse.setEadId(ead.getEncodedIdentifier());
        return Response.ok().entity(overViewResponse).build();
    }
}
