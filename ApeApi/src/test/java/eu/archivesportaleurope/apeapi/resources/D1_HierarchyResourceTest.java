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
import eu.archivesportaleurope.apeapi.request.QueryPageRequest;
import eu.archivesportaleurope.apeapi.request.SearchDocRequest;
import eu.archivesportaleurope.apeapi.response.ead.EadDocResponse;
import eu.archivesportaleurope.apeapi.response.ead.EadFactedDocResponseSet;
import eu.archivesportaleurope.apeapi.response.ead.EadResponse;
import eu.archivesportaleurope.apeapi.response.ead.EadResponseSet;
import eu.archivesportaleurope.apeapi.response.hierarchy.HierarchyResponseSet;
import eu.archivesportaleurope.apeapi.response.utils.JsonDateDeserializer;
import eu.archivesportaleurope.test.util.EmbeddedSolrManager;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Date;
import java.util.logging.Level;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import org.apache.commons.lang.StringUtils;
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
public class D1_HierarchyResourceTest extends JerseySpringWithSecurityTest {

    @Autowired
    public SolrClient ead3SolrServer;
    final private transient Logger logger = LoggerFactory.getLogger(this.getClass());
    private Gson gson;

    public D1_HierarchyResourceTest() {
    }

    @BeforeClass
    public static void setUpClass() {
        try {
            EmbeddedSolrManager.setupData("/HierarchyData.json", "ead3s"); //eads
        } catch (SolrServerException | IOException ex) {
            java.util.logging.Logger.getLogger(D1_HierarchyResourceTest.class.getName()).log(Level.SEVERE, null, ex);
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

    @Test
    public void testGetAncestorsMultiple() {
        logger.debug("Test Ancestors multiple");
        String id = "C97901";
        Response response = super.target("hierarchy").path("ead").path(id).path("ancestors").request().header("APIkey", "myApiKeyXXXX123456789").post(Entity.entity(null, ServerConstants.APE_API_V1));
        response.bufferEntity();
        String jsonResponse = response.readEntity(String.class);
        logger.info("Response Json: " + jsonResponse);
        TypeToken<HierarchyResponseSet> token = new TypeToken<HierarchyResponseSet>() {
        };
        HierarchyResponseSet responseSet = gson.fromJson(jsonResponse, token.getType());
        Assert.assertEquals(HttpStatus.OK.value(), response.getStatus());
        Assert.assertEquals(3, responseSet.getTotalResults());

        String[] ids = new String[]{"F158", "C97892", "C97893"};
        int[] siblingPositions = new int[]{0, 3, 0};
        int[] ancestorLevels = new int[]{0, 1, 2};

        for (int i = 0; i < responseSet.getTotalResults(); i++) {
            Assert.assertEquals(ids[i], responseSet.getResults().get(i).getId());
            Assert.assertEquals(siblingPositions[i], responseSet.getResults().get(i).getSiblingPosition());
            Assert.assertEquals(ancestorLevels[i], responseSet.getResults().get(i).getAncestorLevel());
        }
    }

    @Test
    public void testSearch_ead_Doc_List() throws FileNotFoundException, SolrServerException, URISyntaxException {
        logger.debug("Test Search Doc list");
        SearchDocRequest request = new SearchDocRequest();
        request.setCount(10);
        request.setQuery("*");
        request.setStartIndex(0);
        request.setDocType("fa");

        Response response = super.target("search").path("ead").path("docList").request().header("APIkey", "myApiKeyXXXX123456789").post(Entity.entity(request, ServerConstants.APE_API_V1));
//        Response response = super.target("search").path("ead/F13716/children").request().header("APIkey", "myApiKeyXXXX123456789").post(Entity.entity(request, ServerConstants.APE_API_V1));
        response.bufferEntity();

        //No idea why directly asking for EadResponseSet.class does not works
        String jsonResponse = response.readEntity(String.class); //.replaceAll("[\n]+", "");
        logger.debug("*****Response Json: " + jsonResponse);

        TypeToken<EadFactedDocResponseSet> token = new TypeToken<EadFactedDocResponseSet>() {
        };
        EadFactedDocResponseSet responseEad = gson.fromJson(jsonResponse, token.getType());
        Assert.assertEquals(HttpStatus.OK.value(), response.getStatus());
        Assert.assertEquals(1, responseEad.getTotalDocs());
        Assert.assertEquals(247, responseEad.getTotalResults());
        EadDocResponse doc = responseEad.getEadDocList().get(0);
        Assert.assertTrue(StringUtils.contains(doc.getFindingAidTitle(), "Inventaris van de verzameling Ernsting"));
        logger.debug("Title: " + doc.getRepositoryCode());
    }

    @Test
    public void testSearch_ead_get_children() throws FileNotFoundException, SolrServerException, URISyntaxException {
        logger.debug("Test Search Title");
        QueryPageRequest request = new QueryPageRequest();
        request.setCount(10);
        request.setQuery("*");
        request.setStartIndex(0);
        String[] children = new String[]{"C97742", "C97805", "C97892", "C97973"};

        Response response = super.target("search").path("ead").path("F158").path("children").request().header("APIkey", "myApiKeyXXXX123456789").post(Entity.entity(request, ServerConstants.APE_API_V1));
        response.bufferEntity();

        //No idea why directly asking for EadResponseSet.class does not works
        String jsonResponse = response.readEntity(String.class); //.replaceAll("[\n]+", "");
        logger.debug("Response Json: " + jsonResponse);

        TypeToken<EadResponseSet> token = new TypeToken<EadResponseSet>() {
        };
        EadResponseSet responseEad = gson.fromJson(jsonResponse, token.getType());
//
        Assert.assertEquals(HttpStatus.OK.value(), response.getStatus());
        Assert.assertEquals(4, responseEad.getTotalResults());
        for (EadResponse ead : responseEad.getEadSearchResults()) {
            Assert.assertTrue(Arrays.asList(children).contains(ead.getId()));
            logger.debug("Title: " + ead.getUnitTitle());
        }
    }

    @Test
    public void testGetAncestors() {
        logger.debug("Test Ancestors single");
        String id = "C97892";
        Response response = super.target("hierarchy").path("ead").path(id).path("ancestors").request().header("APIkey", "myApiKeyXXXX123456789").post(Entity.entity(null, ServerConstants.APE_API_V1));
        response.bufferEntity();
        String jsonResponse = response.readEntity(String.class);
        logger.info("Response Json: " + jsonResponse);
        TypeToken<HierarchyResponseSet> token = new TypeToken<HierarchyResponseSet>() {
        };
        HierarchyResponseSet responseSet = gson.fromJson(jsonResponse, token.getType());
        Assert.assertEquals(HttpStatus.OK.value(), response.getStatus());
        Assert.assertEquals(1, responseSet.getTotalResults());
        Assert.assertEquals("F158", responseSet.getResults().get(0).getId());
        Assert.assertEquals(0, responseSet.getResults().get(0).getSiblingPosition());
        Assert.assertEquals(0, responseSet.getResults().get(0).getAncestorLevel());
    }

    @Test
    public void testGetAncestorsZero() {
        logger.debug("Test Ancestors zero");
        String id = "F158";
        Response response = super.target("hierarchy").path("ead").path(id).path("ancestors").request().header("APIkey", "myApiKeyXXXX123456789").post(Entity.entity(null, ServerConstants.APE_API_V1));
        response.bufferEntity();
        String jsonResponse = response.readEntity(String.class);
        logger.info("Response Json: " + jsonResponse);
        TypeToken<HierarchyResponseSet> token = new TypeToken<HierarchyResponseSet>() {
        };
        HierarchyResponseSet responseSet = gson.fromJson(jsonResponse, token.getType());
        Assert.assertEquals(HttpStatus.OK.value(), response.getStatus());
        Assert.assertEquals(0, responseSet.getTotalResults());
    }

    @Test
    public void testGetAncestorsInvalid() {
        logger.debug("Test Ancestors multiple");
        String id = "Invalid";
        Response response = super.target("hierarchy").path("ead").path(id).path("ancestors").request().header("APIkey", "myApiKeyXXXX123456789").post(Entity.entity(null, ServerConstants.APE_API_V1));
        response.bufferEntity();
        String jsonResponse = response.readEntity(String.class);
        logger.info("Response Json: " + jsonResponse);
        Assert.assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
    }

    @Test
    public void testGetChildrenFromFA() {
        logger.debug("Test Children multiple from FA");
        String id = "F158";
        PageRequest request = new PageRequest();
        request.setCount(5);
        request.setStartIndex(0);
        Response response = super.target("hierarchy").path("ead").path(id).path("children").request().header("APIkey", "myApiKeyXXXX123456789").post(Entity.entity(request, ServerConstants.APE_API_V1));
        response.bufferEntity();
        String jsonResponse = response.readEntity(String.class);
        logger.info("Response Json: " + jsonResponse);
        TypeToken<HierarchyResponseSet> token = new TypeToken<HierarchyResponseSet>() {
        };
        HierarchyResponseSet responseSet = gson.fromJson(jsonResponse, token.getType());
        Assert.assertEquals(HttpStatus.OK.value(), response.getStatus());
        Assert.assertEquals(4, responseSet.getTotalResults());

        String[] ids = new String[]{"C97742", "C97805", "C97892", "C97973"};
        int[] siblingPositions = new int[]{1, 2, 3, 4};
        int[] ancestorLevels = new int[]{1, 1, 1, 1};

        for (int i = 0; i < responseSet.getTotalResults(); i++) {
            Assert.assertEquals(ids[i], responseSet.getResults().get(i).getId());
            Assert.assertEquals(siblingPositions[i], responseSet.getResults().get(i).getSiblingPosition());
            Assert.assertEquals(ancestorLevels[i], responseSet.getResults().get(i).getAncestorLevel());
        }
    }

    @Test
    public void testGetChildrenFromCL() {
        logger.debug("Test Children multiple with C id");
        String id = "C97742";
        PageRequest request = new PageRequest();
        request.setCount(5);
        request.setStartIndex(0);
        Response response = super.target("hierarchy").path("ead").path(id).path("children").request().header("APIkey", "myApiKeyXXXX123456789").post(Entity.entity(request, ServerConstants.APE_API_V1));
        response.bufferEntity();
        String jsonResponse = response.readEntity(String.class);
        logger.info("Response Json: " + jsonResponse);
        TypeToken<HierarchyResponseSet> token = new TypeToken<HierarchyResponseSet>() {
        };
        HierarchyResponseSet responseSet = gson.fromJson(jsonResponse, token.getType());
        Assert.assertEquals(HttpStatus.OK.value(), response.getStatus());
        Assert.assertEquals(2, responseSet.getTotalResults());

        String[] ids = new String[]{"C97743", "C97759"};
        int[] siblingPositions = new int[]{0, 1};
        int[] ancestorLevels = new int[]{2, 2};

        for (int i = 0; i < responseSet.getTotalResults(); i++) {
            Assert.assertEquals(ids[i], responseSet.getResults().get(i).getId());
            Assert.assertEquals(siblingPositions[i], responseSet.getResults().get(i).getSiblingPosition());
            Assert.assertEquals(ancestorLevels[i], responseSet.getResults().get(i).getAncestorLevel());
        }
    }

    @Test
    public void testGetChildrenFromCLGreaterThanPageSize() {
        logger.debug("Test Children multiple with C id grater than page size");
        String id = "C97759";
        PageRequest request = new PageRequest();
        request.setCount(2);
        request.setStartIndex(0);
        Response response = super.target("hierarchy").path("ead").path(id).path("children").request().header("APIkey", "myApiKeyXXXX123456789").post(Entity.entity(request, ServerConstants.APE_API_V1));
        response.bufferEntity();
        String jsonResponse = response.readEntity(String.class);
        logger.info("Response Json: " + jsonResponse);
        TypeToken<HierarchyResponseSet> token = new TypeToken<HierarchyResponseSet>() {
        };
        HierarchyResponseSet responseSet = gson.fromJson(jsonResponse, token.getType());
        Assert.assertEquals(HttpStatus.OK.value(), response.getStatus());
        Assert.assertEquals(10, responseSet.getTotalResults());

        String[] ids = new String[]{"C97760", "C97763"};
        int[] siblingPositions = new int[]{0, 1};
        int[] ancestorLevels = new int[]{3, 3};

        for (int i = 0; i < responseSet.getResults().size(); i++) {
            Assert.assertEquals(ids[i], responseSet.getResults().get(i).getId());
            Assert.assertEquals(siblingPositions[i], responseSet.getResults().get(i).getSiblingPosition());
            Assert.assertEquals(ancestorLevels[i], responseSet.getResults().get(i).getAncestorLevel());
        }
    }

    @Test
    public void testGetChildrenFromCLZero() {
        logger.debug("Test Children with blank");
        String id = "C97761";
        PageRequest request = new PageRequest();
        request.setCount(5);
        request.setStartIndex(0);
        Response response = super.target("hierarchy").path("ead").path(id).path("children").request().header("APIkey", "myApiKeyXXXX123456789").post(Entity.entity(request, ServerConstants.APE_API_V1));
        response.bufferEntity();
        String jsonResponse = response.readEntity(String.class);
        logger.info("Response Json: " + jsonResponse);
        TypeToken<HierarchyResponseSet> token = new TypeToken<HierarchyResponseSet>() {
        };
        HierarchyResponseSet responseSet = gson.fromJson(jsonResponse, token.getType());
        Assert.assertEquals(HttpStatus.OK.value(), response.getStatus());
        Assert.assertEquals(0, responseSet.getTotalResults());
    }

    @Test
    public void testGetChildrenInvalid() {
        logger.debug("Test Children with blank");
        String id = "Invalid";
        PageRequest request = new PageRequest();
        request.setCount(5);
        request.setStartIndex(0);
        Response response = super.target("hierarchy").path("ead").path(id).path("children").request().header("APIkey", "myApiKeyXXXX123456789").post(Entity.entity(request, ServerConstants.APE_API_V1));
        response.bufferEntity();
        String jsonResponse = response.readEntity(String.class);
        logger.info("Response Json: " + jsonResponse);
        Assert.assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
    }

    @Override
    protected ResourceConfig configure() {
        return new ResourceConfig(HierarchyResource.class);
    }

}
