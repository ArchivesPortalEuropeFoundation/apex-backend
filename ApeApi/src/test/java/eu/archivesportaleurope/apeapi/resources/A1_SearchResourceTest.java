/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.apeapi.resources;

import eu.archivesportaleurope.apeapi.jersey.JerseySpringWithSecurityTest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import eu.archivesportaleurope.apeapi.common.datatypes.ServerConstants;
import eu.archivesportaleurope.apeapi.request.QueryPageRequest;
import eu.archivesportaleurope.apeapi.request.SearchPageRequestWithUnitId;
import eu.archivesportaleurope.apeapi.request.SearchRequest;
import eu.archivesportaleurope.apeapi.response.ead.EadFactedResponseSet;
import eu.archivesportaleurope.apeapi.response.ead.EadResponse;
import eu.archivesportaleurope.apeapi.response.ead.EadResponseSet;
import eu.archivesportaleurope.apeapi.response.utils.JsonDateDeserializer;
import eu.archivesportaleurope.apeapi.response.utils.PropertiesUtil;
import eu.archivesportaleurope.test.util.EmbeddedSolrManager;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.logging.Level;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.glassfish.jersey.server.ResourceConfig;
import org.junit.After;
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
 * @author Mahbub
 */
public class A1_SearchResourceTest extends JerseySpringWithSecurityTest {

    @Autowired
    public SolrClient ead3SolrServer;

    final private transient Logger logger = LoggerFactory.getLogger(this.getClass());
    private Gson gson;

    @BeforeClass
    public static void setUpClass() {
        try {
            EmbeddedSolrManager.setupData("/EadMockData.json", "ead3s", EadResponseSet.class); //eads
        } catch (IOException | SolrServerException | InterruptedException ex) {
            java.util.logging.Logger.getLogger(A1_SearchResourceTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Before
    public void setUpTest() throws SolrServerException, IOException, InterruptedException {
        gson = new GsonBuilder().serializeNulls().registerTypeAdapter(Date.class, new JsonDateDeserializer()).create();
    }

    @After
    public void tearDownTest() {
    }

    @Test
    public void testSearchWithFondsUnitId() {
        logger.debug("Test search with fonds unit id");
        SearchPageRequestWithUnitId request = new SearchPageRequestWithUnitId();
        request.setFindingAidNo("1.01.01.10");
        request.setCount(50);
        request.setStartIndex(0);
        Response response = super.target("search").path("searchEadFindingAidNo")
                .request().header("APIkey", "myApiKeyXXXX123456789").post(Entity.entity(request, ServerConstants.APE_API_V1));
        response.bufferEntity();
        Assert.assertEquals(HttpStatus.OK.value(), response.getStatus());
        String jsonResponse = response.readEntity(String.class);
        logger.debug("Response Json: " + jsonResponse);

        TypeToken<EadFactedResponseSet> token = new TypeToken<EadFactedResponseSet>() {
        };

        EadFactedResponseSet factedResponseSet = gson.fromJson(jsonResponse, token.getType());
        Assert.assertEquals(1, factedResponseSet.getTotalResults());
        Assert.assertEquals("F13716", factedResponseSet.getEadSearchResults().get(0).getId());
        Assert.assertEquals("1.01.01.10", factedResponseSet.getEadSearchResults().get(0).getFindingAidNo());
        Assert.assertEquals("1.01.01.10", factedResponseSet.getEadSearchResults().get(0).getUnitId());
    }

    @Test
    public void testSearchWithFondsUnitIdAndUnitId() {
        logger.debug("Test search with fonds unit id in combination with unit id");
        SearchPageRequestWithUnitId request = new SearchPageRequestWithUnitId();
        request.setFindingAidNo("1.01.01.10");
        request.setUnitId("1");
        request.setCount(2);
        request.setStartIndex(0);
        Response response = super.target("search").path("searchEadFindingAidNo")
                .request().header("APIkey", "myApiKeyXXXX123456789").post(Entity.entity(request, ServerConstants.APE_API_V1));
        response.bufferEntity();
        Assert.assertEquals(HttpStatus.OK.value(), response.getStatus());
        String jsonResponse = response.readEntity(String.class);
        logger.debug("Response Json: " + jsonResponse);

        TypeToken<EadFactedResponseSet> token = new TypeToken<EadFactedResponseSet>() {
        };

        EadFactedResponseSet factedResponseSet = gson.fromJson(jsonResponse, token.getType());
        Assert.assertEquals(1, factedResponseSet.getTotalResults());
        Assert.assertEquals("C13716", factedResponseSet.getEadSearchResults().get(0).getId());
        Assert.assertEquals("1.01.01.10", factedResponseSet.getEadSearchResults().get(0).getFindingAidNo());
        Assert.assertEquals("1.01.01.10 - 1", factedResponseSet.getEadSearchResults().get(0).getUnitId());
    }

    @Test
    public void testSearchWithFondsUnitIdAndUnitId2() {
        logger.debug("Test 2 search with fonds unit id in combination with unit id ");
        SearchPageRequestWithUnitId request = new SearchPageRequestWithUnitId();
        request.setFindingAidNo("1.01.01.10");
        request.setUnitId("11");
        request.setCount(2);
        request.setStartIndex(0);
        Response response = super.target("search").path("searchEadFindingAidNo")
                .request().header("APIkey", "myApiKeyXXXX123456789").post(Entity.entity(request, ServerConstants.APE_API_V1));
        response.bufferEntity();
        Assert.assertEquals(HttpStatus.OK.value(), response.getStatus());
        String jsonResponse = response.readEntity(String.class);
        logger.debug("Response Json: " + jsonResponse);

        TypeToken<EadFactedResponseSet> token = new TypeToken<EadFactedResponseSet>() {
        };

        EadFactedResponseSet factedResponseSet = gson.fromJson(jsonResponse, token.getType());
        Assert.assertEquals(1, factedResponseSet.getTotalResults());
        Assert.assertEquals("C13717", factedResponseSet.getEadSearchResults().get(0).getId());
        Assert.assertEquals("1.01.01.10", factedResponseSet.getEadSearchResults().get(0).getFindingAidNo());
        Assert.assertEquals("1.01.01.10 - 11", factedResponseSet.getEadSearchResults().get(0).getUnitId());
    }

    @Test
    public void testSearch_ead_TotalResultCount() throws FileNotFoundException, SolrServerException, URISyntaxException {
        logger.debug("Test Search TotalResultCount");
        SearchRequest request = new SearchRequest();
        request.setCount(10);
        request.setQuery("*");
        request.setStartIndex(0);

        Response response = super.target("search").path("ead").request().header("APIkey", "myApiKeyXXXX123456789").post(Entity.entity(request, ServerConstants.APE_API_V1));
        response.bufferEntity();

        //No idea why directly asking for EadResponseSet.class does not works
        String jsonResponse = response.readEntity(String.class); //.replaceAll("[\n]+", "");
        logger.debug("Response Json: " + jsonResponse);

        TypeToken<EadResponseSet> token = new TypeToken<EadResponseSet>() {
        };
        EadResponseSet responseEad = gson.fromJson(jsonResponse, token.getType());

        Assert.assertEquals(HttpStatus.OK.value(), response.getStatus());
        Assert.assertTrue(responseEad.getTotalResults() > 0);
    }

    @Test
    public void testSearch_ead_Title() throws FileNotFoundException, SolrServerException, URISyntaxException {
        logger.debug("Test Search Title");
        SearchRequest request = new SearchRequest();
        request.setCount(10);
        request.setQuery("Nepal");
        request.setStartIndex(0);

        Response response = super.target("search").path("ead").request().header("APIkey", "myApiKeyXXXX123456789").post(Entity.entity(request, ServerConstants.APE_API_V1));
        response.bufferEntity();

        //No idea why directly asking for EadResponseSet.class does not works
        String jsonResponse = response.readEntity(String.class); //.replaceAll("[\n]+", "");
        logger.debug("Response Json: " + jsonResponse);

        TypeToken<EadResponseSet> token = new TypeToken<EadResponseSet>() {
        };
        EadResponseSet responseEad = gson.fromJson(jsonResponse, token.getType());

        Assert.assertEquals(HttpStatus.OK.value(), response.getStatus());

        EadResponse doc = responseEad.getEadSearchResults().get(0);
        Assert.assertEquals("Nepal. 1967 - 1973", doc.getUnitTitle());
        logger.debug("Title: " + doc.getUnitTitle());
    }

    @Test
    public void testWithDefaultCount() {
        logger.debug("Test Search with default count");
        SearchRequest request = new SearchRequest();
        request.setCount(0);
        request.setQuery("Nepal");
        request.setStartIndex(0);

        Response response = super.target("search").path("ead").request().header("APIkey", "myApiKeyXXXX123456789").post(Entity.entity(request, ServerConstants.APE_API_V1));
        response.bufferEntity();

        //No idea why directly asking for EadResponseSet.class does not works
        String jsonResponse = response.readEntity(String.class); //.replaceAll("[\n]+", "");
        logger.debug("Response Json: " + jsonResponse);

        TypeToken<EadResponseSet> token = new TypeToken<EadResponseSet>() {
        };
        EadResponseSet responseEad = gson.fromJson(jsonResponse, token.getType());

        Assert.assertEquals(HttpStatus.OK.value(), response.getStatus());
        logger.info(new PropertiesUtil("resource.properties").getValueFromKey("search.request.default.count"));
        Assert.assertTrue(Integer.parseInt(new PropertiesUtil("resource.properties").getValueFromKey("search.request.default.count")) >= responseEad.getEadSearchResults().size());
    }

    //@Test(expected = ProcessingException.class)
    public void testInvalidRequest() {
        logger.debug("Test invalid request with count > 50");
        SearchRequest request = new SearchRequest();
        request.setCount(51);
        request.setQuery("Anything");
        request.setStartIndex(0);
        Response response = super.target("search").path("ead").request().post(Entity.entity(request, ServerConstants.APE_API_V1));
        response.bufferEntity();
        String jsonResponse = response.readEntity(String.class); //.replaceAll("[\n]+", "");
        logger.debug("Response Json: " + jsonResponse);
        Assert.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }

    @Test
    public void testNullAPIkeyRequest() {
        logger.debug("Test Null apikey");
        SearchRequest request = new SearchRequest();
        request.setCount(5);
        request.setQuery("Anything");
        request.setStartIndex(0);
        Response response = super.target("search").path("ead").request().post(Entity.entity(request, ServerConstants.APE_API_V1));

        Assert.assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatus());
    }

    @Test
    public void testInvalidAPIkeyRequest() {
        logger.debug("Test Null apikey");
        SearchRequest request = new SearchRequest();
        request.setCount(5);
        request.setQuery("Anything");
        request.setStartIndex(0);
        Response response = super.target("search").path("ead").request().header("APIkey", "Blabal").post(Entity.entity(request, ServerConstants.APE_API_V1));

        Assert.assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatus());
    }
    
    @Test
    public void testGetChildrenHasNoChild() {
        QueryPageRequest request = new QueryPageRequest();
        request.setCount(10);
        request.setQuery("*");
        request.setStartIndex(0);

        Response response = super.target("search").path("ead").path("F9732").path("children").request().header("APIkey", "myApiKeyXXXX123456789").post(Entity.entity(request, ServerConstants.APE_API_V1));
        response.bufferEntity();

        //No idea why directly asking for EadResponseSet.class does not works
        String jsonResponse = response.readEntity(String.class); //.replaceAll("[\n]+", "");
        logger.debug("Response Json: " + jsonResponse);

        TypeToken<EadResponseSet> token = new TypeToken<EadResponseSet>() {
        };
        EadResponseSet responseEad = gson.fromJson(jsonResponse, token.getType());

        Assert.assertEquals(HttpStatus.OK.value(), response.getStatus());
        Assert.assertTrue(responseEad.getTotalResults() == 0);
    }
    
    @Test
    public void testGetChildrenHasSomeChild() {
        QueryPageRequest request = new QueryPageRequest();
        request.setCount(10);
        request.setQuery("*");
        request.setStartIndex(0);

        Response response = super.target("search").path("ead").path("F13716").path("children").request().header("APIkey", "myApiKeyXXXX123456789").post(Entity.entity(request, ServerConstants.APE_API_V1));
        response.bufferEntity();

        //No idea why directly asking for EadResponseSet.class does not works
        String jsonResponse = response.readEntity(String.class); //.replaceAll("[\n]+", "");
        logger.debug("Response Json: " + jsonResponse);

        TypeToken<EadResponseSet> token = new TypeToken<EadResponseSet>() {
        };
        EadResponseSet responseEad = gson.fromJson(jsonResponse, token.getType());

        Assert.assertEquals(HttpStatus.OK.value(), response.getStatus());
        Assert.assertTrue(responseEad.getTotalResults() > 0);
    }

    @Override
    protected ResourceConfig configure() {
        return new ResourceConfig(SearchResource.class);
    }

}
