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
import eu.archivesportaleurope.apeapi.exceptions.ViolationException;
import eu.archivesportaleurope.apeapi.jersey.JerseySpringWithSecurityTest;
import eu.archivesportaleurope.apeapi.response.ArchivalInstitutesResponse;
import eu.archivesportaleurope.apeapi.response.utils.JsonDateDeserializer;
import eu.archivesportaleurope.apeapi.services.AiStatService;
import java.util.Date;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import org.glassfish.jersey.server.ResourceConfig;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

/**
 *
 * @author kaisar
 */
public class ArchivalInstituteStatResourceTest extends JerseySpringWithSecurityTest {

    final private transient Logger logger = LoggerFactory.getLogger(this.getClass());
    private Gson gson;
    @Autowired
    private AiStatService aiStatService;

    public ArchivalInstituteStatResourceTest() {
    }

    @BeforeClass
    public static void setUpClass() {
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
        int start = 0;
        int count = 2;

        Response response = super.target("institute").path("getInstitute").path(String.valueOf(start))
                .path(String.valueOf(count)).request().header("APIkey", "myApiKeyXXXX123456789").header("Content-Type", ServerConstants.APE_API_V1).accept(ServerConstants.APE_API_V1).get();
        response.bufferEntity();
        String jsonResponse = response.readEntity(String.class);
        logger.debug("Response Json: " + jsonResponse);

        TypeToken<ArchivalInstitutesResponse> token = new TypeToken<ArchivalInstitutesResponse>() {
        };
        ArchivalInstitutesResponse ais = gson.fromJson(jsonResponse, token.getType());
        Assert.assertEquals(2, ais.getInstitutes().size());
        Assert.assertEquals(3, ais.getTotal());
    }

    @Test
    public void testWithStartLessThanZero() {
        logger.debug(":::Test Get Institute that has open data enabled with start less than zero");
        int start = -1;
        int count = 2;
        Response response = super.target("institute").path("getInstitute").path(String.valueOf(start))
                .path(String.valueOf(count)).request().header("APIkey", "myApiKeyXXXX123456789").header("Content-Type", ServerConstants.APE_API_V1).accept(ServerConstants.APE_API_V1).get();
        response.bufferEntity();
        String jsonResponse = response.readEntity(String.class);
        logger.debug("Response Json: " + jsonResponse);

        TypeToken<ArchivalInstitutesResponse> token = new TypeToken<ArchivalInstitutesResponse>() {
        };
        ArchivalInstitutesResponse ais = gson.fromJson(jsonResponse, token.getType());
        Assert.assertEquals(2, ais.getInstitutes().size());
        Assert.assertEquals(3, ais.getTotal());
    }

    @Test
    public void testInvalidRequest() {
        logger.debug("Test invalid request with count > 50");
        int start = 1;
        int count = 51;
        Response response = super.target("institute").path("getInstitute").path(String.valueOf(start))
                .path(String.valueOf(count)).request().header("APIkey", "myApiKeyXXXX123456789").header("Content-Type", ServerConstants.APE_API_V1).accept(ServerConstants.APE_API_V1).get();
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
