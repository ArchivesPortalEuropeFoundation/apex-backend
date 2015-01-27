package eu.archivesportaleurope.xml.xpath.handler;

import java.io.InputStream;

import org.junit.Assert;
import org.junit.Test;

import eu.archivesportaleurope.xml.AbstractTest;
import eu.archivesportaleurope.xml.TestConstants;
import eu.archivesportaleurope.xml.XmlParser;
import eu.archivesportaleurope.xml.xpath.AbstractXpathReader;

public class CountXpathHandlerTest extends AbstractTest{

	/**
	 * Test full existing xpath 'count(/ead/c/did/unitid)'
	 * @throws Exception
	 */
	@Test
	public void countFullXpath() throws Exception {
		InputStream inputStream = getInputStream("/eu/archivesportaleurope/xml/xpath/handler/test-file.xml");
		CountXpathHandler countHandler = new CountXpathHandler(TestConstants.TEST_NAMESPACE, new String[] {TestConstants.EAD.getLocalPart(), 
				TestConstants.C.getLocalPart(), TestConstants.DID.getLocalPart(),TestConstants.UNITID.getLocalPart()});
		TestCountXpathReader xpathReader = new TestCountXpathReader(countHandler);
		XmlParser.parse(inputStream, xpathReader);
		Assert.assertEquals(5, countHandler.getCount());
		
	}
	
	/**
	 * Test full not existing xpath 'count(/did/unitid)'
	 * @throws Exception
	 */
	@Test
	public void countPartialXpathNotRelative() throws Exception {
		InputStream inputStream = getInputStream("/eu/archivesportaleurope/xml/xpath/handler/test-file.xml");
		CountXpathHandler countHandler = new CountXpathHandler(TestConstants.TEST_NAMESPACE, new String[] {TestConstants.DID.getLocalPart(),TestConstants.UNITID.getLocalPart()});
		TestCountXpathReader xpathReader = new TestCountXpathReader(countHandler);
		XmlParser.parse(inputStream, xpathReader);
		Assert.assertEquals(0, countHandler.getCount());

	}
	
	/**
	 * Test relative existing xpath 'count(//did/unitid)'
	 * @throws Exception
	 */
	@Test
	public void countRelativeXpath() throws Exception {
		InputStream inputStream = getInputStream("/eu/archivesportaleurope/xml/xpath/handler/test-file.xml");
		CountXpathHandler countHandler = new CountXpathHandler(TestConstants.TEST_NAMESPACE, new String[] {TestConstants.DID.getLocalPart(),TestConstants.UNITID.getLocalPart()});
		/*
		 * set relative
		 */
		countHandler.setRelative(true);
		TestCountXpathReader xpathReader = new TestCountXpathReader(countHandler);
		XmlParser.parse(inputStream, xpathReader);
		Assert.assertEquals(5, countHandler.getCount());
	}
	
	/**
	 * Test relative existing xpath 'count(//did/unitid[1])'
	 * @throws Exception
	 */
	@Test
	public void countRelativeXpathOnlyFirst() throws Exception {
		InputStream inputStream = getInputStream("/eu/archivesportaleurope/xml/xpath/handler/test-file.xml");
		CountXpathHandler countHandler = new CountXpathHandler(TestConstants.TEST_NAMESPACE, new String[] {TestConstants.DID.getLocalPart(),TestConstants.UNITID.getLocalPart()});
		/*
		 * set relative
		 */
		countHandler.setRelative(true);
		/*
		 * count first
		 */
		countHandler.setOnlyFirst(true);
		TestCountXpathReader xpathReader = new TestCountXpathReader(countHandler);
		XmlParser.parse(inputStream, xpathReader);
		Assert.assertEquals(1, countHandler.getCount());
	}
	
	/**
	 * Test relative existing xpath 'count(//did/unitid[@type='call number'])'
	 * @throws Exception
	 */
	@Test
	public void countRelativeXpathWithAttribute() throws Exception {
		InputStream inputStream = getInputStream("/eu/archivesportaleurope/xml/xpath/handler/test-file.xml");
		CountXpathHandler countHandler = new CountXpathHandler(TestConstants.TEST_NAMESPACE, new String[] {TestConstants.DID.getLocalPart(),TestConstants.UNITID.getLocalPart()});
		/*
		 * set relative
		 */
		countHandler.setRelative(true);
		/*
		 * with attribute @type='call number'
		 */
		countHandler.setAttribute("type", "call number", false);
		TestCountXpathReader xpathReader = new TestCountXpathReader(countHandler);
		XmlParser.parse(inputStream, xpathReader);
		Assert.assertEquals(2, countHandler.getCount());
	}
	
	/**
	 * Test relative existing xpath 'count(//did/unitid[not(@type='call number')])'
	 * @throws Exception
	 */
	@Test
	public void countRelativeXpathWithAttributeNot() throws Exception {
		InputStream inputStream = getInputStream("/eu/archivesportaleurope/xml/xpath/handler/test-file.xml");
		CountXpathHandler countHandler = new CountXpathHandler(TestConstants.TEST_NAMESPACE, new String[] {TestConstants.DID.getLocalPart(),TestConstants.UNITID.getLocalPart()});
		/*
		 * set relative
		 */
		countHandler.setRelative(true);
		/*
		 * with attribute not @type='call number'
		 */
		countHandler.setAttribute("type", "call number", true);
		TestCountXpathReader xpathReader = new TestCountXpathReader(countHandler);
		XmlParser.parse(inputStream, xpathReader);
		Assert.assertEquals(3, countHandler.getCount());
	}
	
	/**
	 * test null values with attributes and attribute values
	 * @throws Exception
	 */
	@Test
	public void countRelativeXpathWithNullAttribute() throws Exception {
		InputStream inputStream = getInputStream("/eu/archivesportaleurope/xml/xpath/handler/test-file.xml");
		CountXpathHandler countHandler = new CountXpathHandler(TestConstants.TEST_NAMESPACE, new String[] {TestConstants.DID.getLocalPart(),TestConstants.UNITID.getLocalPart()});
		/*
		 * set relative
		 */
		countHandler.setRelative(true);
		/*
		 * with no attribute specified
		 */
		countHandler.setAttribute(null, null, false);
		TestCountXpathReader xpathReader = new TestCountXpathReader(countHandler);
		XmlParser.parse(inputStream, xpathReader);
		Assert.assertEquals(5, countHandler.getCount());
	}
	/**
	 * test null values with attributes values
	 * @throws Exception
	 */
	@Test
	public void countRelativeXpathWithNullAttributeValue() throws Exception {
		InputStream inputStream = getInputStream("/eu/archivesportaleurope/xml/xpath/handler/test-file.xml");
		CountXpathHandler countHandler = new CountXpathHandler(TestConstants.TEST_NAMESPACE, new String[] {TestConstants.DID.getLocalPart(),TestConstants.UNITID.getLocalPart()});
		/*
		 * set relative
		 */
		countHandler.setRelative(true);
		/*
		 * with no attribute specified
		 */
		countHandler.setAttribute("test", null, false);
		TestCountXpathReader xpathReader = new TestCountXpathReader(countHandler);
		XmlParser.parse(inputStream, xpathReader);
		Assert.assertEquals(5, countHandler.getCount());
	}
	
	/**
	 * Test relative existing xpath 'count(//did/unitid[not(@xlink:href='test')])'
	 * @throws Exception
	 */
	@Test
	public void countRelativeXpathWithAttributeWithNamespace() throws Exception {
		InputStream inputStream = getInputStream("/eu/archivesportaleurope/xml/xpath/handler/test-file.xml");
		CountXpathHandler countHandler = new CountXpathHandler(TestConstants.TEST_NAMESPACE, new String[] {TestConstants.DID.getLocalPart(),TestConstants.UNITID.getLocalPart()});
		/*
		 * set relative
		 */
		countHandler.setRelative(true);
		/*
		 * with no attribute specified
		 */
		countHandler.setAttribute(TestConstants.XLINK_NAMESPACE, "href", "test", false);
		TestCountXpathReader xpathReader = new TestCountXpathReader(countHandler);
		XmlParser.parse(inputStream, xpathReader);
		Assert.assertEquals(1, countHandler.getCount());
	}
	private static class TestCountXpathReader extends AbstractXpathReader<Integer> {
		private CountXpathHandler countHandler;

		public TestCountXpathReader(CountXpathHandler countHandler){
			this.countHandler = countHandler;
		}
		@Override
		protected void internalInit() throws Exception {
			getXpathHandlers().clear();
			getXpathHandlers().add(countHandler);
		}

		@Override
		public Integer getData() {
			return countHandler.getCount();
		}


	}
}
