/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.apeapi.transaction.repository.util;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 *
 * @author mahbub
 */
public class OffsetBasedPageRequestTest {
    
    public OffsetBasedPageRequestTest() {
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
     * Test of Constructor exception, of class OffsetBasedPageRequest.
     */
    @Test (expected = IllegalArgumentException.class)
    public void testConstructor() {
        System.out.println("Constructor");
        OffsetBasedPageRequest instance = new OffsetBasedPageRequest(0, 0);
    }
    
    /**
     * Test of Constructor exception, of class OffsetBasedPageRequest.
     */
    @Test (expected = IllegalArgumentException.class)
    public void testConstructorOffsetLessZero() {
        System.out.println("Constructor");
        OffsetBasedPageRequest instance = new OffsetBasedPageRequest(-1, 10);
    }
    
    /**
     * Test of getPageNumber method, of class OffsetBasedPageRequest.
     */
    @Test
    public void testGetPageNumber() {
        System.out.println("getPageNumber");
        OffsetBasedPageRequest instance = new OffsetBasedPageRequest(15, 7);
        int expResult = 2;
        int result = instance.getPageNumber();
        assertEquals(expResult, result);
    }

    /**
     * Test of getPageSize method, of class OffsetBasedPageRequest.
     */
    @Test
    public void testGetPageSize() {
        System.out.println("getPageSize");
        OffsetBasedPageRequest instance = new OffsetBasedPageRequest(15, 7);
        int expResult = 7;
        int result = instance.getPageSize();
        assertEquals(expResult, result);
    }

    /**
     * Test of getOffset method, of class OffsetBasedPageRequest.
     */
    @Test
    public void testGetOffset() {
        System.out.println("getOffset");
        OffsetBasedPageRequest instance = new OffsetBasedPageRequest(15, 7);
        int expResult = 15;
        int result = instance.getOffset();
        assertEquals(expResult, result);
    }

    /**
     * Test of getSort method, of class OffsetBasedPageRequest.
     */
    @Test
    public void testGetSort() {
        System.out.println("getSort");
        Sort sort = new Sort(new Sort.Order("whatever"));
        OffsetBasedPageRequest instance = new OffsetBasedPageRequest(15, 7, sort);
        Sort expResult = sort;
        Sort result = instance.getSort();
        assertEquals(expResult, result);
    }

    /**
     * Test of next method, of class OffsetBasedPageRequest.
     */
    @Test
    public void testNext() {
        System.out.println("next");
        OffsetBasedPageRequest instance = new OffsetBasedPageRequest(15, 7);
        Pageable expResult = new OffsetBasedPageRequest(15+7, 7);
        Pageable result = instance.next();
        assertEquals(expResult, result);
    }

    /**
     * Test of previous method, of class OffsetBasedPageRequest.
     */
    @Test
    public void testPrevious() {
        System.out.println("previous");
        OffsetBasedPageRequest instance = new OffsetBasedPageRequest(15, 7);
        OffsetBasedPageRequest expResult = new OffsetBasedPageRequest(15-7, 7);
        OffsetBasedPageRequest result = instance.previous();
        assertEquals(expResult, result);
    }

    /**
     * Test of previousOrFirst method, of class OffsetBasedPageRequest.
     */
    @Test
    public void testPreviousOrFirst() {
        System.out.println("previousOrFirst");
        OffsetBasedPageRequest instance = new OffsetBasedPageRequest(0, 7);
        Pageable expResult = new OffsetBasedPageRequest(0, 7);
        Pageable result = instance.previousOrFirst();
        assertEquals(expResult, result);
    }

    /**
     * Test of first method, of class OffsetBasedPageRequest.
     */
    @Test
    public void testFirst() {
        System.out.println("first");
        OffsetBasedPageRequest instance = new OffsetBasedPageRequest(15, 7);
        Pageable expResult = new OffsetBasedPageRequest(0, 7);
        Pageable result = instance.first();
        assertEquals(expResult, result);
    }

    /**
     * Test of hasPrevious method, of class OffsetBasedPageRequest.
     */
    @Test
    public void testHasPrevious() {
        System.out.println("hasPrevious");
        OffsetBasedPageRequest instance = new OffsetBasedPageRequest(15, 7);
        boolean expResult = true;
        boolean result = instance.hasPrevious();
        assertEquals(expResult, result);
    }

    /**
     * Test of equals method, of class OffsetBasedPageRequest.
     */
    @Test
    public void testEquals() {
        System.out.println("equals");
        Object o = new OffsetBasedPageRequest(15, 7);
        OffsetBasedPageRequest instance = new OffsetBasedPageRequest(15, 7);
        boolean expResult = true;
        boolean result = instance.equals(o);
        assertEquals(expResult, result);
    }
    
    /**
     * Test of equals method, of class OffsetBasedPageRequest.
     */
    @Test
    public void testEqualsTrue() {
        System.out.println("equals");
        Object o;
        OffsetBasedPageRequest instance = new OffsetBasedPageRequest(15, 7);
        o=instance;
        boolean expResult = true;
        boolean result = instance.equals(o);
        assertEquals(expResult, result);
    }
    
    /**
     * Test of equals method, of class OffsetBasedPageRequest.
     */
    @Test
    public void testEqualsFalse() {
        System.out.println("equals");
        Object o = new Double("1.0");
        OffsetBasedPageRequest instance = new OffsetBasedPageRequest(15, 7);
        boolean expResult = false;
        boolean result = instance.equals(o);
        assertEquals(expResult, result);
    }
}
