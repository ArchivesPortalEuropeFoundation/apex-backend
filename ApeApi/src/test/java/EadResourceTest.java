
import com.google.gson.Gson;
import eu.archivesportaleurope.apeapi.request.SearchRequest;
import eu.archivesportaleurope.apeapi.resources.SearchResource;
import eu.archivesportaleurope.apeapi.response.ead.EadResponseSet;
import eu.archivesportaleurope.apeapi.services.SearchService;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URISyntaxException;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
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

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author kaisar
 */
public class EadResourceTest extends JerseySpringTest {

    @Autowired
    SearchService eadSearch;

    final private transient Logger logger = LoggerFactory.getLogger(this.getClass());

    @Before
    public void setUpTest() {
        Mockito.reset(eadSearch);
    }

    @After
    public void tearDownTest() {
        validateMockitoUsage();
    }

    @Test
    public void testSearch() throws FileNotFoundException, SolrServerException, URISyntaxException {
        logger.debug("Test Search");
        BufferedReader br;
        br = new BufferedReader(new FileReader(new File(this.getClass().getResource("response.json").toURI())));
        EadResponseSet eads = new Gson().fromJson(br, EadResponseSet.class);
        Mockito.when(eadSearch.search(Matchers.<SearchRequest>any())).thenReturn(eads);
        SearchRequest request = new SearchRequest();
        request.setCount(10);
        request.setQuery("nepal");
        request.setStart(0);
        Response response = super.target("search").path("ead").request().post(Entity.entity(request, MediaType.APPLICATION_JSON));
        response.bufferEntity();
        EadResponseSet responseEad = response.readEntity(EadResponseSet.class);
        Assert.assertEquals(response.getStatus(), HttpStatus.OK.value());
        Assert.assertEquals(eads.getTotalResults(), responseEad.getTotalResults());
    }

    @Override
    protected ResourceConfig configure() {
        return new ResourceConfig(SearchResource.class);
    }

}
