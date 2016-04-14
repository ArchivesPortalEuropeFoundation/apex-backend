/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.apeapi.response;

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
public class SearchStatResponseTest {
    
    public SearchStatResponseTest() {
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
     * Test of getQueryTime method, of class SearchStatResponse.
     */
    @Test
    public void testGetMethods() {
        System.out.println("getQueryTime");
        SearchStatResponse instance = new SearchStatResponse();
        int expResult = 0;
        int result = instance.getQueryTime();
        assertEquals(expResult, result);
        assertEquals(0, instance.getRows());
        assertEquals("", instance.getQ());
    }
    
    /**
     * Test of getQueryTime method, of class SearchStatResponse.
     */
    @Test
    public void testSetMethods() {
        System.out.println("getQueryTime");
        SearchStatResponse instance = new SearchStatResponse();
        String expQ = "Bla";
        int expT = 5;
        int expR = 10;
        instance.setQ(expQ);
        instance.setQueryTime(expT);
        instance.setRows(expR);
        assertEquals(expT, instance.getQueryTime());
        assertEquals(expR, instance.getRows());
        assertEquals(expQ, instance.getQ());
    }
    
}
