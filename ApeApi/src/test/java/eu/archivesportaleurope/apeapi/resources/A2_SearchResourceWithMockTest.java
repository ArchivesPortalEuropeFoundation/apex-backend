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
import eu.archivesportaleurope.apeapi.request.SearchRequest;
import eu.archivesportaleurope.apeapi.response.utils.JsonDateDeserializer;
import eu.archivesportaleurope.apeapi.services.SearchService;
import java.text.ParseException;
import java.util.Date;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import org.apache.solr.client.solrj.SolrServerException;
import org.glassfish.jersey.server.ResourceConfig;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;
import static org.mockito.Mockito.validateMockitoUsage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

/**
 *
 * @author mahbub
 */
public class A2_SearchResourceWithMockTest extends JerseySpringTest {
    final private transient Logger logger = LoggerFactory.getLogger(this.getClass());
    private Gson gson;
    Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
    
    @Autowired
    SearchService eadSearch;
    
    @Before
    public void setUpTest() {
        Mockito.reset(eadSearch);
        gson = new GsonBuilder().serializeNulls().registerTypeAdapter(Date.class, new JsonDateDeserializer()).create();
    }

    @After
    public void tearDownTest() {
        validateMockitoUsage();
        //server.shutdown();
    }
    
    @Test
    public void testInternalServerException() throws SolrServerException, ParseException {
        logger.debug("Test exception");
        SearchRequest request = new SearchRequest();
        request.setCount(0);
        request.setQuery("Heerlijkheid");
        request.setStartIndex(0);

        Mockito.doThrow(SolrServerException.class).when(eadSearch).searchOpenData(Matchers.<SearchRequest>any());
        
        Response response = super.target("search").path("ead").request().header("APIkey", "myApiKeyXXXX123456789").post(Entity.entity(request, ServerConstants.APE_API_V1));
        
        Assert.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getStatus());
    }
    
    @Test
    public void testWebAppException() throws SolrServerException, ParseException {
        logger.debug("Test exception");
        SearchRequest request = new SearchRequest();
        request.setCount(0);
        request.setQuery("Heerlijkheid");
        request.setStartIndex(0);
        
        Mockito.doThrow(new WebApplicationException()).when(eadSearch).searchOpenData(Matchers.<SearchRequest>any());
        
        Response response = super.target("search").path("ead").request().header("APIkey", "myApiKeyXXXX123456789").post(Entity.entity(request, ServerConstants.APE_API_V1));
        
        Assert.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getStatus());
    }
    
    @Test(expected = ProcessingException.class)
    public void testInvalidRequest() {
        logger.debug("Test invalid request with count > 50");
        SearchRequest request = new SearchRequest();
        request.setCount(51);
        request.setQuery("Something");
        Set<ConstraintViolation<SearchRequest>> constraintViolations = validator.validate(request);
        ConstraintViolation<SearchRequest> constraintViolation = constraintViolations.iterator().next();

        //1 rules violated
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
