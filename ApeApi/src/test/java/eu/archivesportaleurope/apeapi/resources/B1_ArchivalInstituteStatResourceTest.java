/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.apeapi.resources;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import eu.archivesportaleurope.apeapi.common.datatypes.ServerConstants;
import eu.archivesportaleurope.apeapi.jersey.JerseySpringWithSecurityTest;
import eu.archivesportaleurope.apeapi.request.PageRequest;
import eu.archivesportaleurope.apeapi.response.ArchivalInstitutesResponse;
import eu.archivesportaleurope.apeapi.response.ead.EadResponseSet;
import eu.archivesportaleurope.apeapi.response.utils.JsonDateDeserializer;
import eu.archivesportaleurope.test.util.EmbeddedSolrManager;
import java.io.IOException;
import java.util.Date;
import java.util.logging.Level;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.glassfish.jersey.server.ResourceConfig;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

/**
 *
 * @author kaisar
 */
//@Ignore
public class B1_ArchivalInstituteStatResourceTest extends JerseySpringWithSecurityTest {

    @Autowired
    public SolrClient ead3SolrServer;

    final private transient Logger logger = LoggerFactory.getLogger(this.getClass());
    private Gson gson;

    public B1_ArchivalInstituteStatResourceTest() {
    }

    @BeforeClass
    public static void setUpClass() {
        try {
            EmbeddedSolrManager.setupData("/EadMockData.json", "ead3s", EadResponseSet.class); //eads
        } catch (IOException | SolrServerException | InterruptedException ex) {
            java.util.logging.Logger.getLogger(B1_ArchivalInstituteStatResourceTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        gson = new GsonBuilder().serializeNulls().registerTypeAdapter(Date.class, new JsonDateDeserializer()).create();
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getInsByOpenData method, of class ArchivalInstituteStatResource.
     */
    @Test
    public void testGetInsByOpenData() {
        logger.debug(":::Test Get Institute that has open data enabled");
        PageRequest request = new PageRequest();
        request.setStartIndex(0);
        request.setCount(2);

        Response response = super.target("institute").path("getInstitutes").request().header("APIkey", "myApiKeyXXXX123456789").post(Entity.entity(request, ServerConstants.APE_API_V1));
        response.bufferEntity();
        String jsonResponse = response.readEntity(String.class);
        logger.debug("Response Json: " + jsonResponse);

        TypeToken<ArchivalInstitutesResponse> token = new TypeToken<ArchivalInstitutesResponse>() {
        };
        ArchivalInstitutesResponse ais = gson.fromJson(jsonResponse, token.getType());
        Assert.assertEquals(2, ais.getInstitutes().size());
        Assert.assertEquals(3, ais.getTotalResults());
    }

    @Test
    public void testWithStartLessThanZero() {
        logger.debug(":::Test Get Institute that has open data enabled with start less than zero");
        PageRequest request = new PageRequest();
        request.setStartIndex(-1);
        request.setCount(2);
        Response response = super.target("institute").path("getInstitutes").request().header("APIkey", "myApiKeyXXXX123456789").post(Entity.entity(request, ServerConstants.APE_API_V1));
        response.bufferEntity();

        String jsonResponse = response.readEntity(String.class);
        logger.debug("Response Json: " + jsonResponse);

        TypeToken<ArchivalInstitutesResponse> token = new TypeToken<ArchivalInstitutesResponse>() {
        };
        ArchivalInstitutesResponse ais = gson.fromJson(jsonResponse, token.getType());
        Assert.assertEquals(2, ais.getInstitutes().size());
        Assert.assertEquals(3, ais.getTotalResults());
    }

    @Test(expected = ProcessingException.class)
    public void testInvalidRequest() {
        logger.debug("Test invalid request with count > 50");
        PageRequest request = new PageRequest();
        request.setStartIndex(1);
        request.setCount(51);

        Response response = super.target("institute").path("getInstitutes").request().header("APIkey", "myApiKeyXXXX123456789").post(Entity.entity(request, ServerConstants.APE_API_V1));
        response.bufferEntity();
        String jsonResponse = response.readEntity(String.class);
        logger.debug("Response Json: " + jsonResponse);

        Assert.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }

    @Test(expected = ProcessingException.class)
    public void testInvalidRequestNeg() {
        logger.debug("Test invalid request with count < 0");
        PageRequest request = new PageRequest();
        request.setStartIndex(1);
        request.setCount(-51);

        Response response = super.target("institute").path("getInstitutes").request().header("APIkey", "myApiKeyXXXX123456789").post(Entity.entity(request, ServerConstants.APE_API_V1));
        response.bufferEntity();
        String jsonResponse = response.readEntity(String.class);
        logger.debug("Response Json: " + jsonResponse);

        Assert.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }

    @Override
    protected ResourceConfig configure() {
        return new ResourceConfig(ArchivalInstituteStatResource.class);
    }

}
