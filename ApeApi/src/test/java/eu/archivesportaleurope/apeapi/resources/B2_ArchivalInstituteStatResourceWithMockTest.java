/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.apeapi.resources;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import eu.archivesportaleurope.apeapi.common.datatypes.ServerConstants;
import eu.archivesportaleurope.apeapi.jersey.JerseySpringTest;
import eu.archivesportaleurope.apeapi.response.utils.JsonDateDeserializer;
import eu.archivesportaleurope.apeapi.services.AiStatService;
import java.util.Date;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import org.glassfish.jersey.server.ResourceConfig;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.validateMockitoUsage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author kaisar
 */
public class B2_ArchivalInstituteStatResourceWithMockTest extends JerseySpringTest {

    final private transient Logger logger = LoggerFactory.getLogger(this.getClass());
    private Gson gson;
    @Autowired
    private AiStatService aiStatService;

    public B2_ArchivalInstituteStatResourceWithMockTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
        validateMockitoUsage();
    }

    @Before
    public void setUp() {
        Mockito.reset(aiStatService);
        gson = new GsonBuilder().serializeNulls().registerTypeAdapter(Date.class, new JsonDateDeserializer()).create();
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getInsByOpenData method, of class ArchivalInstituteStatResource.
     */
    @Test
    public void testWebApplicationException() {
        logger.debug("Test invalid request with Web application exception");
        int startIndex = 1;
        int count = 5;
        Mockito.when(aiStatService.getAiWithOpenDataEnabled(startIndex, count)).thenThrow(new WebApplicationException("Exception throw success"));
        Response response = super.target("institute").path("getInstitute").path(String.valueOf(startIndex))
                .path(String.valueOf(count)).request().header("APIkey", "myApiKeyXXXX123456789").header("Content-Type", ServerConstants.APE_API_V1).accept(ServerConstants.APE_API_V1).get();
        response.bufferEntity();
        String jsonResponse = response.readEntity(String.class);
        logger.debug("Response Json: " + jsonResponse);
    }

    @Test
    public void testException() throws InterruptedException {
        logger.debug("Test invalid request with  exception");
        int startIndex = 1;
        int count = 5;
        Mockito.when(aiStatService.getAiWithOpenDataEnabled(startIndex, count)).thenThrow(new NullPointerException("Intentional Exception"));
        Response response = super.target("institute").path("getInstitute").path(String.valueOf(startIndex))
                .path(String.valueOf(count)).request().header("APIkey", "myApiKeyXXXX123456789").header("Content-Type", ServerConstants.APE_API_V1).accept(ServerConstants.APE_API_V1).get();
        response.bufferEntity();
        String jsonResponse = response.readEntity(String.class);
        logger.debug("Response Json: " + jsonResponse);
    }

    @Override()
    protected ResourceConfig configure() {
        return new ResourceConfig(ArchivalInstituteStatResource.class);
    }
}
