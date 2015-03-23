package eu.archivesportaleurope.xml.xpath.handler;

import java.util.LinkedList;

import javax.xml.namespace.QName;

import junit.framework.Assert;

import org.junit.Test;

import eu.archivesportaleurope.xml.TestConstants;

public class XpathQueryTest {

	@Test
	public void testXpathQueryWithOneItem() {
		String[] xpathQueryArray = new String[] {TestConstants.EAD.getLocalPart()};
		XpathQuery xpathQuery = new XpathQuery(TestConstants.TEST_NAMESPACE, xpathQueryArray);
		Assert.assertEquals(1, xpathQuery.getXpathQuerySize());
		Assert.assertEquals(TestConstants.EAD,xpathQuery.getFirst());
		/*
		 * test with empty list
		 */
		LinkedList<QName> xpathPosition = new LinkedList<QName>();
		Assert.assertFalse(xpathQuery.match(xpathPosition));
		xpathPosition.add(TestConstants.ARCHDESC);
		/*
		 * test with wrong list
		 */
		Assert.assertFalse(xpathQuery.match(xpathPosition));
		/*
		 * test with correct list
		 */
		xpathPosition.clear();
		xpathPosition.add(TestConstants.EAD);
		Assert.assertTrue(xpathQuery.match(xpathPosition));
		/*
		 * test with wrong list with 2 items
		 */
		xpathPosition.add(TestConstants.ARCHDESC);
		Assert.assertFalse(xpathQuery.match(xpathPosition));
	}

	
	@Test
	public void testXpathQueryWithTwoItem() {
		String[] xpathQueryArray = new String[] {TestConstants.EAD.getLocalPart(), TestConstants.ARCHDESC.getLocalPart()};
		XpathQuery xpathQuery = new XpathQuery(TestConstants.TEST_NAMESPACE, xpathQueryArray);
		Assert.assertEquals(2, xpathQuery.getXpathQuerySize());
		Assert.assertEquals(TestConstants.EAD,xpathQuery.getFirst());
		/*
		 * test with empty list
		 */
		LinkedList<QName> xpathPosition = new LinkedList<QName>();
		Assert.assertFalse(xpathQuery.match(xpathPosition));
		xpathPosition.add(TestConstants.ARCHDESC);
		/*
		 * test with wrong list
		 */
		Assert.assertFalse(xpathQuery.match(xpathPosition));
		/*
		 * test with wrong list
		 */
		xpathPosition.clear();
		xpathPosition.add(TestConstants.EAD);
		Assert.assertFalse(xpathQuery.match(xpathPosition));
		/*
		 * test with correct list with 2 items
		 */
		xpathPosition.add(TestConstants.ARCHDESC);
		Assert.assertTrue(xpathQuery.match(xpathPosition));
	}
	@Test
	public void testXpathQueryWithTwoItemAndNot() {
		String[] xpathQueryArray = new String[] {TestConstants.EAD.getLocalPart(), "not(" + TestConstants.ARCHDESC.getLocalPart() +")"};
		XpathQuery xpathQuery = new XpathQuery(TestConstants.TEST_NAMESPACE, xpathQueryArray);
		Assert.assertEquals(2, xpathQuery.getXpathQuerySize());
		Assert.assertEquals(TestConstants.EAD,xpathQuery.getFirst());
		/*
		 * test with empty list
		 */
		LinkedList<QName> xpathPosition = new LinkedList<QName>();
		Assert.assertFalse(xpathQuery.match(xpathPosition));
		xpathPosition.add(TestConstants.ARCHDESC);
		/*
		 * test with wrong list
		 */
		Assert.assertFalse(xpathQuery.match(xpathPosition));
		/*
		 * test with wrong list
		 */
		xpathPosition.clear();
		xpathPosition.add(TestConstants.EAD);
		Assert.assertFalse(xpathQuery.match(xpathPosition));
		/*
		 * test with wrong list with 2 items
		 */
		xpathPosition.add(TestConstants.ARCHDESC);
		Assert.assertFalse(xpathQuery.match(xpathPosition));
		/*
		 * test with correct list with 2 items
		 */
		xpathPosition.clear();
		xpathPosition.add(TestConstants.EAD);
		xpathPosition.add(TestConstants.C);
		Assert.assertTrue(xpathQuery.match(xpathPosition));
	}
	
	@Test
	public void testXpathQueryWithTwoItemAndOr() {
		String[] xpathQueryArray = new String[] {TestConstants.EAD.getLocalPart(), TestConstants.ARCHDESC.getLocalPart() + "|" + TestConstants.C.getLocalPart()};
		XpathQuery xpathQuery = new XpathQuery(TestConstants.TEST_NAMESPACE, xpathQueryArray);
		Assert.assertEquals(2, xpathQuery.getXpathQuerySize());
		Assert.assertEquals(TestConstants.EAD,xpathQuery.getFirst());
		/*
		 * test with empty list
		 */
		LinkedList<QName> xpathPosition = new LinkedList<QName>();
		Assert.assertFalse(xpathQuery.match(xpathPosition));
		xpathPosition.add(TestConstants.ARCHDESC);
		/*
		 * test with wrong list
		 */
		Assert.assertFalse(xpathQuery.match(xpathPosition));
		/*
		 * test with wrong list
		 */
		xpathPosition.clear();
		xpathPosition.add(TestConstants.EAD);
		Assert.assertFalse(xpathQuery.match(xpathPosition));
		/*
		 * test with wrong list with 2 items
		 */
		xpathPosition.add(TestConstants.ARCHDESC);
		Assert.assertTrue(xpathQuery.match(xpathPosition));
		/*
		 * test with correct list with 2 items
		 */
		xpathPosition.clear();
		xpathPosition.add(TestConstants.EAD);
		xpathPosition.add(TestConstants.C);
		Assert.assertTrue(xpathQuery.match(xpathPosition));
	}
	
	@Test
	public void testXpathQueryWithTwoItemAndOrAndNot() {
		String[] xpathQueryArray = new String[] {TestConstants.EAD.getLocalPart(), "not(" + TestConstants.ARCHDESC.getLocalPart() + "|" +"" +  TestConstants.C.getLocalPart() + ")"};
		XpathQuery xpathQuery = new XpathQuery(TestConstants.TEST_NAMESPACE, xpathQueryArray);
		Assert.assertEquals(2, xpathQuery.getXpathQuerySize());
		Assert.assertEquals(TestConstants.EAD,xpathQuery.getFirst());
		/*
		 * test with empty list
		 */
		LinkedList<QName> xpathPosition = new LinkedList<QName>();
		Assert.assertFalse(xpathQuery.match(xpathPosition));
		xpathPosition.add(TestConstants.ARCHDESC);
		/*
		 * test with wrong list
		 */
		Assert.assertFalse(xpathQuery.match(xpathPosition));
		/*
		 * test with wrong list
		 */
		xpathPosition.clear();
		xpathPosition.add(TestConstants.EAD);
		Assert.assertFalse(xpathQuery.match(xpathPosition));
		/*
		 * test with wrong list with 2 items
		 */
		xpathPosition.add(TestConstants.ARCHDESC);
		Assert.assertFalse(xpathQuery.match(xpathPosition));
		/*
		 * test with correct list with 2 items
		 */
		xpathPosition.clear();
		xpathPosition.add(TestConstants.EAD);
		xpathPosition.add(TestConstants.EAD);
		Assert.assertTrue(xpathQuery.match(xpathPosition));
	}
}
