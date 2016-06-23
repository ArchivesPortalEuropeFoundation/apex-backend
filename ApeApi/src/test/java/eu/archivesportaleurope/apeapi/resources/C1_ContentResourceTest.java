/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.apeapi.resources;

import eu.archivesportaleurope.apeapi.jersey.JerseySpringWithSecurityTest;
import org.glassfish.jersey.server.ResourceConfig;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author mahbub
 */
public class C1_ContentResourceTest  extends JerseySpringWithSecurityTest {
    
    public C1_ContentResourceTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getClevelContent method, of class ContentResource.
     */
    @Test
    public void testGetClevelContent() {
        System.out.println("getClevelContent");
        
    }

    /**
     * Test of getContent method, of class ContentResource.
     */
    //@Test
    public void testGetContent() {
        System.out.println("getContent");
        
    }

    @Override
    protected ResourceConfig configure() {
        return new ResourceConfig(ContentResource.class);
    }
    
}
