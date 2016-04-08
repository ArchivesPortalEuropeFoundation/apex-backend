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
import eu.archivesportaleurope.apeapi.request.SearchRequest;
import eu.archivesportaleurope.apeapi.response.ead.EadResponse;
import eu.archivesportaleurope.apeapi.response.ead.EadResponseSet;
import eu.archivesportaleurope.apeapi.response.utils.JsonDateDeserializer;
import eu.archivesportaleurope.apeapi.response.utils.PropertiesUtil;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import org.apache.solr.client.solrj.SolrServerException;
import org.glassfish.jersey.server.ResourceConfig;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.validateMockitoUsage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

/**
 *
 * @author Mahbub
 */
public class SearchResourceTest extends JerseySpringTest {

    final private transient Logger logger = LoggerFactory.getLogger(this.getClass());
    private Gson gson;
    Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Before
    public void setUpTest() {
//        Mockito.reset(eadSearchUtil);
        gson = new GsonBuilder().serializeNulls().registerTypeAdapter(Date.class, new JsonDateDeserializer()).create();
    }

    @After
    public void tearDownTest() {
        validateMockitoUsage();
        //server.shutdown();
    }

    @Test
    public void testSearch_ead_TotalResultCount() throws FileNotFoundException, SolrServerException, URISyntaxException {
        logger.debug("Test Search TotalResultCount");
        SearchRequest request = new SearchRequest();
        request.setCount(10);
        request.setQuery("Heerlijkheid");
        request.setStart(0);

        Response response = super.target("search").path("ead").request().header("APIkey", "myApiKeyXXXX123456789").post(Entity.entity(request, ServerConstants.APE_API_V1));
        response.bufferEntity();

        EadResponseSet responseEad = response.readEntity(EadResponseSet.class);

        Assert.assertEquals(HttpStatus.OK.value(), response.getStatus());
        Assert.assertEquals(1, responseEad.getTotalResults());
    }

    @Test
    public void testSearch_ead_Title() throws FileNotFoundException, SolrServerException, URISyntaxException {
        logger.debug("Test Search Title");
        SearchRequest request = new SearchRequest();
        request.setCount(10);
        request.setQuery("Heerlijkheid");
        request.setStart(0);

        Response response = super.target("search").path("ead").request().post(Entity.entity(request, ServerConstants.APE_API_V1));
        response.bufferEntity();

        //No idea why directly asking for EadResponseSet.class does not works
        String jsonResponse = response.readEntity(String.class); //.replaceAll("[\n]+", "");
        logger.debug("Response Json: " + jsonResponse);

        TypeToken<EadResponseSet> token = new TypeToken<EadResponseSet>() {
        };
        EadResponseSet responseEad = gson.fromJson(jsonResponse, token.getType());

        Assert.assertEquals(HttpStatus.OK.value(), response.getStatus());

        EadResponse doc = responseEad.getEadSearchResults().get(0);
        Assert.assertEquals("Heerlijkheid Alblasserdam - Kaarten", doc.getUnitTitle());
        logger.debug("Title: " + doc.getUnitTitle());
    }

    @Test
    public void testWithDefaultCount() {
        logger.debug("Test Search with default count");
        SearchRequest request = new SearchRequest();
        request.setCount(0);
        request.setQuery("Heerlijkheid");
        request.setStart(0);

        Response response = super.target("search").path("ead").request().post(Entity.entity(request, ServerConstants.APE_API_V1));
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

    @Test(expected = ProcessingException.class)
    public void testInvalidRequest() {
        logger.debug("Test invalid request with count > 50");
        SearchRequest request = new SearchRequest();
        request.setCount(51);
        request.setQuery("Anything");
        request.setStart(0);
        Set<ConstraintViolation<SearchRequest>> constraintViolations = validator.validate(request);
        ConstraintViolation<SearchRequest> constraintViolation = constraintViolations.iterator().next();

        Assert.assertEquals(1, constraintViolations.size());
        Assert.assertEquals("Count must not be more than 50", constraintViolation.getMessage());
        Assert.assertEquals("count", constraintViolation.getPropertyPath().toString());
        super.target("search").path("ead").request().post(Entity.entity(request, ServerConstants.APE_API_V1));
    }

    @Override
    protected ResourceConfig configure() {
        return new ResourceConfig(SearchResource.class);
    }

}
