/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.apeapi.utils;

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
public class CommonUtilsTest {
    
    public CommonUtilsTest() {
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
     * Test of splitByColon method, of class CommonUtils.
     */
    @Test
    public void testSplitByColon() {
        System.out.println("splitByColon");
        String str = "Test:0";
        int number = 0;
        String expResult = "Test";
        String result = CommonUtils.splitByColon(str, number);
        assertEquals(expResult, result);
    }
    
    @Test
    public void testSplitByColonOutOfBound() {
        System.out.println("splitByColonOutOfBound");
        String str = "Test:0";
        int number = 2;
        String expResult = "";
        String result = CommonUtils.splitByColon(str, number);
        assertEquals(expResult, result);
    }
    
    @Test
    public void testSplitByColonNull() {
        System.out.println("splitByColonOutOfBound");
        String str = null;
        int number = 0;
        String expResult = "";
        String result = CommonUtils.splitByColon(str, number);
        assertEquals(expResult, result);
    }

    /**
     * Test of objectToString method, of class CommonUtils.
     */
    @Test
    public void testObjectToString() {
        System.out.println("objectToString");
        String test = "Test";
        Object o = test;
        String expResult = "Test";
        String result = CommonUtils.objectToString(o);
        assertEquals(expResult, result);
    }

    /**
     * Test of objectToInt method, of class CommonUtils.
     */
    @Test
    public void testObjectToInt() {
        System.out.println("objectToInt");
        Integer i = 10;
        Object o = i;
        int expResult = 10;
        int result = CommonUtils.objectToInt(o);
        assertEquals(expResult, result);
    }
    
    /**
     * Test of objectToInt method, of class CommonUtils.
     */
    @Test
    public void testObjectToIntNonInt() {
        System.out.println("objectToInt");
        String str = "10";
        Object o = str;
        int expResult = 10;
        int result = CommonUtils.objectToInt(o);
        assertEquals(expResult, result);
    }
    
    /**
     * Test of objectToInt method, of class CommonUtils.
     */
    @Test
    public void testObjectToIntNonConvertable() {
        System.out.println("objectToInt");
        String str = "Test";
        Object o = str;
        int expResult = 0;
        int result = CommonUtils.objectToInt(o);
        assertEquals(expResult, result);
    }
    
}
