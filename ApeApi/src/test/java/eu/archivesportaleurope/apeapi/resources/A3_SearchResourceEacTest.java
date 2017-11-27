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
import eu.archivesportaleurope.apeapi.request.SearchRequest;
import eu.archivesportaleurope.apeapi.response.eaccpf.EacCpfResponse;
import eu.archivesportaleurope.apeapi.response.eaccpf.EacCpfResponseSet;
import eu.archivesportaleurope.apeapi.response.utils.JsonDateDeserializer;
import eu.archivesportaleurope.test.util.EmbeddedSolrManager;
import java.io.IOException;
import java.util.Date;
import java.util.logging.Level;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import org.apache.solr.client.solrj.SolrServerException;
import org.glassfish.jersey.server.ResourceConfig;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

/**
 *
 * @author kaisar
 */
public class A3_SearchResourceEacTest extends JerseySpringWithSecurityTest {

    final private transient Logger logger = LoggerFactory.getLogger(this.getClass());
    private Gson gson;

    @BeforeClass
    public static void setUpClass() {
        try {
            EmbeddedSolrManager.setupData("/EacMockData.json", "eac-cpfs", EacCpfResponseSet.class); //eac-cpfs
        } catch (IOException | SolrServerException | InterruptedException ex) {
            java.util.logging.Logger.getLogger(A3_SearchResourceEacTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Before
    public void setUpTest() throws SolrServerException, IOException, InterruptedException {
        gson = new GsonBuilder().serializeNulls().registerTypeAdapter(Date.class, new JsonDateDeserializer()).create();
    }

    @Test
    public void testSearchAll() {
        logger.debug("Search All");
        SearchRequest request = new SearchRequest();
        request.setCount(0);
        request.setStartIndex(0);
        request.setQuery("*");

        Response response = super.target("search").path("eac-cpf").request().header("APIkey", "myApiKeyXXXX123456789").post(Entity.entity(request, ServerConstants.APE_API_V1));
        response.bufferEntity();
        String jsonResponse = response.readEntity(String.class);
        logger.debug("Response Json : " + jsonResponse);
        TypeToken<EacCpfResponseSet> token = new TypeToken<EacCpfResponseSet>() {
        };
        EacCpfResponseSet responseEac = gson.fromJson(jsonResponse, token.getType());
        Assert.assertEquals(HttpStatus.OK.value(), response.getStatus());
        Assert.assertTrue(responseEac.getTotalResults() > 0);
    }

    @Test
    public void testSearchWithName() {
        logger.debug("Search with name field");
        SearchRequest request = new SearchRequest();
        request.setCount(0);
        request.setStartIndex(0);
        request.setQuery("Deutsche");

        Response response = super.target("search").path("eac-cpf").request().header("APIkey", "myApiKeyXXXX123456789").post(Entity.entity(request, ServerConstants.APE_API_V1));
        response.bufferEntity();
        String jsonResponse = response.readEntity(String.class);
        logger.debug("Response Json : " + jsonResponse);
        TypeToken<EacCpfResponseSet> token = new TypeToken<EacCpfResponseSet>() {
        };
        EacCpfResponseSet responseEac = gson.fromJson(jsonResponse, token.getType());
        Assert.assertEquals(HttpStatus.OK.value(), response.getStatus());
        Assert.assertEquals(2, responseEac.getTotalResults());
    }

    @Test
    public void testSearchSingleResult() {
        logger.debug("Search single result");
        SearchRequest request = new SearchRequest();
        request.setCount(0);
        request.setStartIndex(0);
        request.setQuery("Federal Chancellery of Germany");

        Response response = super.target("search").path("eac-cpf").request().header("APIkey", "myApiKeyXXXX123456789").post(Entity.entity(request, ServerConstants.APE_API_V1));
        response.bufferEntity();
        String jsonResponse = response.readEntity(String.class);
        logger.debug("Response Json : " + jsonResponse);
        TypeToken<EacCpfResponseSet> token = new TypeToken<EacCpfResponseSet>() {
        };
        EacCpfResponseSet responseEac = gson.fromJson(jsonResponse, token.getType());
        Assert.assertEquals(HttpStatus.OK.value(), response.getStatus());
        Assert.assertEquals(1, responseEac.getTotalResults());
        EacCpfResponse eac = responseEac.getEacSearchResults().get(0);
        Assert.assertEquals("2", eac.getId());
        Assert.assertEquals("140967287", eac.getRecordId());
        Assert.assertEquals("corporatebody", eac.getEntityType());
        Assert.assertEquals("1949 - open", eac.getExistDates());
        Assert.assertEquals("", eac.getDescription());
        Assert.assertEquals(" Ministerium des Innern 140930344 Bundesarchiv DE-1958 Deutsche Verwaltung des Innern (1940-1945) 1946-1949 (1950-1957) DO-1-28279 Bundesarchiv DE-1958 Weisungen der Deutschen Verwaltung des Innern (1946-1949) do1_dvdiweis Bundesarchiv DE-1958", eac.getOther());
        Assert.assertEquals("Nationaal Archief", eac.getRepository());
        Assert.assertEquals("NL-HaNA", eac.getRepositoryCode());
        Assert.assertEquals("NETHERLANDS", eac.getCountry());
        Assert.assertEquals("7", eac.getCountryId());
        Assert.assertEquals("15", eac.getNumberOfArchivalMaterialRelations());
        Assert.assertEquals("9", eac.getNumberOfNameRelations());
    }

    @Override
    protected ResourceConfig configure() {
        return new ResourceConfig(SearchResource.class);
    }

}
