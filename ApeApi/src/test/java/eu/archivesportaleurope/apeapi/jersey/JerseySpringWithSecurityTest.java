package eu.archivesportaleurope.apeapi.jersey;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import eu.archivesportaleurope.apeapi.utils.CustormDelegatingFilterProxy;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Application;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;
import org.glassfish.jersey.test.DeploymentContext;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.ServletDeploymentContext;
import org.glassfish.jersey.test.TestProperties;
import org.glassfish.jersey.test.grizzly.GrizzlyWebTestContainerFactory;
import org.glassfish.jersey.test.spi.TestContainerFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.context.ContextLoaderListener;

/**
 *
 * @author M.Mozadded
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:META-INF/applicationContext-test.xml", "classpath:META-INF/security-context.xml"})
@ActiveProfiles("unit-test")
@WebAppConfiguration
public abstract class JerseySpringWithSecurityTest {

    private JerseyTest _jerseyTest;

    public final WebTarget target(final String path) {
        return _jerseyTest.target(path);
    }

    @Before
    public void setupJerseyTest() throws Exception {
        _jerseyTest.setUp();
    }

    @After
    public void tearDownJerseyTest() throws Exception {
        _jerseyTest.tearDown();
    }

    @Autowired
    public void setApplicationContext(final ApplicationContext context) {
        _jerseyTest = new JerseyTest() {
             @Override
            public TestContainerFactory getTestContainerFactory() {
                return new GrizzlyWebTestContainerFactory();
            }
            
            @Override
            public DeploymentContext configureDeployment() {
                super.configureDeployment();
                Application config = configure();
                return ServletDeploymentContext
                    .forServlet(new ServletContainer((ResourceConfig) config))
                    .addFilter(CustormDelegatingFilterProxy.class, "springSecurityFilterChain")
                        .addListener(ContextLoaderListener.class)
                        .contextParam("spring.profiles.active", "unit-test")
                        .contextParam("contextConfigLocation", "classpath:META-INF/applicationContext-test.xml,classpath:META-INF/security-context.xml")
                    .build();
            }

            @Override
            protected Application configure() {
                enable(TestProperties.LOG_TRAFFIC);
                enable(TestProperties.DUMP_ENTITY);
                ResourceConfig application = JerseySpringWithSecurityTest.this.configure();
                return application;
            }

        };
    }

    protected abstract ResourceConfig configure();
}
