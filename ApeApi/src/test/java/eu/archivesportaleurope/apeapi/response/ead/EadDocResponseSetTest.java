/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.apeapi.response.ead;

import eu.archivesportaleurope.apeapi.request.SearchDocRequest;
import eu.archivesportaleurope.apeapi.resources.A1_SearchResourceTest;
import eu.archivesportaleurope.test.util.EmbeddedSolrManager;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author mahbub
 */
public class EadDocResponseSetTest {
    
    public EadDocResponseSetTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
        /*try {
            EmbeddedSolrManager.setupData("/EadMockData.json", "ead3s", EadResponseSet.class); //eads
        } catch (IOException | SolrServerException | InterruptedException ex) {
            java.util.logging.Logger.getLogger(EadDocResponseSetTest.class.getName()).log(Level.SEVERE, null, ex);
        }*/
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
     * Test of getEadDocList method, of class EadDocResponseSet.
     */
    @Test
    public void testNullGroup() {
        System.out.println("Null solr response group");
        SearchDocRequest request = new SearchDocRequest();
        request.setCount(5);
        request.setQuery("*");
        request.setStartIndex(0);
        QueryResponse queryResponse = new QueryResponse();
        
        EadDocResponseSet instance = new EadDocResponseSet(request, queryResponse);
        int expResult = 0;
        int result = instance.getEadDocList().size();
        assertEquals(expResult, result);
    }

    /**
     * Test of addEadDoc method, of class EadDocResponseSet.
     */
   // @Test
    public void testAddEadDoc() {
        System.out.println("addEadDoc");
        EadDocResponse docResponse = null;
        EadDocResponseSet instance = new EadDocResponseSet();
        instance.addEadDoc(docResponse);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setEadDocList method, of class EadDocResponseSet.
     */
    //@Test
    public void testSetEadDocList() {
        System.out.println("setEadDocList");
        List<EadDocResponse> eadDocList = null;
        EadDocResponseSet instance = new EadDocResponseSet();
        instance.setEadDocList(eadDocList);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
