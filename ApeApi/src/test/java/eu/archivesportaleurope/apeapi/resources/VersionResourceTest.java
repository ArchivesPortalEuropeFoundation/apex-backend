package eu.archivesportaleurope.apeapi.resources;

import eu.archivesportaleurope.apeapi.common.datatypes.ServerConstants;
import javax.ws.rs.core.Response;
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
 * @author kaisar
 */
public class VersionResourceTest extends JerseySpringTest {

    final private transient Logger logger = LoggerFactory.getLogger(this.getClass());

    @Before
    public void setUpTest() {

    }

    @After
    public void tearDownTest() {
        validateMockitoUsage();
    }

    @Test
    public void testCurrentAppVersion() {
        logger.debug("Application current version Version");
        Response response = super.target("version").path("current").request().get(Response.class);
        Assert.assertEquals(response.getStatus(), HttpStatus.OK.value());
        response.bufferEntity();
        String appVersion = "{\"version\":\""+ServerConstants.APE_API_V1+"\"}";
        Assert.assertEquals(appVersion, response.readEntity(String.class));
    }

    @Test
    public void testPreviousAppVersion() {
        logger.debug("Application previous version Version");
        Response response = super.target("version").path("previous").request().get(Response.class);
        String appVersion = "{\"version\":0.0}";
        Assert.assertEquals(appVersion, response.readEntity(String.class));
    }

    @Override
    protected ResourceConfig configure() {
        return new ResourceConfig(VersionResource.class);
    }

}
