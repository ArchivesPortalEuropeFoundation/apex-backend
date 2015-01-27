package eu.archivesportaleurope.xml.xpath.handler;

import java.io.InputStream;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import eu.archivesportaleurope.xml.AbstractTest;
import eu.archivesportaleurope.xml.ApeXmlUtil;
import eu.archivesportaleurope.xml.TestConstants;
import eu.archivesportaleurope.xml.XmlParser;
import eu.archivesportaleurope.xml.xpath.AbstractXpathReader;

public class TextXpathHandlerTest extends AbstractTest {

	/**
	 * Test full existing xpath '/ead/c/did/unitid/text()'
	 * @throws Exception
	 */
	@Test
	public void fullXpath() throws Exception {
		InputStream inputStream = getInputStream("/eu/archivesportaleurope/xml/xpath/handler/test-file.xml");
		TextXpathHandler textHandler = new TextXpathHandler(TestConstants.TEST_NAMESPACE, new String[] {TestConstants.EAD.getLocalPart(), 
				TestConstants.C.getLocalPart(), TestConstants.DID.getLocalPart(),TestConstants.UNITID.getLocalPart()});
		TestTextXpathReader xpathReader = new TestTextXpathReader(textHandler);
		XmlParser.parse(inputStream, xpathReader);
		Assert.assertEquals("unitid1-old", textHandler.getFirstResult());
		Assert.assertEquals(5, textHandler.getResult().size());
		Assert.assertEquals(5, textHandler.getResultSet().size());
		List<String> result = textHandler.getResult();
		Assert.assertEquals("unitid1-old", result.get(0));
		Assert.assertEquals("unitid1", result.get(1));
		Assert.assertEquals("unitid2-old", result.get(2));
		Assert.assertEquals("unitid2", result.get(3));
		Assert.assertEquals("unitid2-old1", result.get(4));
	
	}
	
	/**
	 * Test full existing xpath '/ead/c/did[not(unitid)]'
	 * @throws Exception
	 */
	@Test
	public void notFullXpath() throws Exception {
		InputStream inputStream = getInputStream("/eu/archivesportaleurope/xml/xpath/handler/test-file.xml");
		TextXpathHandler textHandler = new TextXpathHandler(TestConstants.TEST_NAMESPACE, new String[] {TestConstants.EAD.getLocalPart(), 
				TestConstants.C.getLocalPart(), TestConstants.DID.getLocalPart(),"not(" + TestConstants.UNITID.getLocalPart() + ")" });
		TestTextXpathReader xpathReader = new TestTextXpathReader(textHandler);
		XmlParser.parse(inputStream, xpathReader);
		Assert.assertEquals("Test item 1", textHandler.getFirstResult());
		Assert.assertEquals(3, textHandler.getResult().size());
		Assert.assertEquals(3, textHandler.getResultSet().size());
		List<String> result = textHandler.getResult();
		Assert.assertEquals("Test item 1", result.get(0));
		Assert.assertEquals("Test item 2", result.get(1));
		Assert.assertEquals("Other info", result.get(2));
	}
	/**
	 * Test full existing xpath '/ead/c/did[not(unitid | unittitle)]'
	 * @throws Exception
	 */
	@Test
	public void notFullXpathWithOr() throws Exception {
		InputStream inputStream = getInputStream("/eu/archivesportaleurope/xml/xpath/handler/test-file.xml");
		TextXpathHandler textHandler = new TextXpathHandler(TestConstants.TEST_NAMESPACE, new String[] {TestConstants.EAD.getLocalPart(), 
				TestConstants.C.getLocalPart(), TestConstants.DID.getLocalPart(),"not(" + TestConstants.UNITID.getLocalPart() + " | " + TestConstants.UNITTITLE.getLocalPart() + ")" });
		TestTextXpathReader xpathReader = new TestTextXpathReader(textHandler);
		XmlParser.parse(inputStream, xpathReader);
		Assert.assertEquals("Other info", textHandler.getFirstResult());
		Assert.assertEquals(1, textHandler.getResult().size());
		Assert.assertEquals(1, textHandler.getResultSet().size());
		List<String> result = textHandler.getResult();
		Assert.assertEquals("Other info", result.get(0));
	}
	
	/**
	 * Test full existing xpath '/ead/c/did[unitid | unittitle]'
	 * @throws Exception
	 */
	@Test
	public void fullXpathWithOr() throws Exception {
		InputStream inputStream = getInputStream("/eu/archivesportaleurope/xml/xpath/handler/test-file.xml");
		TextXpathHandler textHandler = new TextXpathHandler(TestConstants.TEST_NAMESPACE, new String[] {TestConstants.EAD.getLocalPart(), 
				TestConstants.C.getLocalPart(), TestConstants.DID.getLocalPart(),TestConstants.OTHER.getLocalPart() + " | " + TestConstants.UNITTITLE.getLocalPart()  });
		TestTextXpathReader xpathReader = new TestTextXpathReader(textHandler);
		XmlParser.parse(inputStream, xpathReader);
		Assert.assertEquals("Test item 1", textHandler.getFirstResult());
		Assert.assertEquals(3, textHandler.getResult().size());
		Assert.assertEquals(3, textHandler.getResultSet().size());
		List<String> result = textHandler.getResult();
		Assert.assertEquals("Test item 1", result.get(0));
		Assert.assertEquals("Test item 2", result.get(1));
		Assert.assertEquals("Other info", result.get(2));
	}
	/**
	 * Test full existing xpath '/ead/c/did/unitid//text()'
	 * @throws Exception
	 */
	@Test
	public void allTextBelowXpath() throws Exception {
		InputStream inputStream = getInputStream("/eu/archivesportaleurope/xml/xpath/handler/test-file.xml");
		TextXpathHandler textHandler = new TextXpathHandler(TestConstants.TEST_NAMESPACE, new String[] {TestConstants.EAD.getLocalPart(), 
				TestConstants.C.getLocalPart(), TestConstants.DID.getLocalPart(),TestConstants.UNITID.getLocalPart()});
		/*
		 * all text below
		 */
		textHandler.setAllTextBelow(true);
		TestTextXpathReader xpathReader = new TestTextXpathReader(textHandler);
		XmlParser.parse(inputStream, xpathReader);
		Assert.assertEquals("unitid1-old", textHandler.getFirstResult());
		Assert.assertEquals(5, textHandler.getResult().size());
		Assert.assertEquals(5, textHandler.getResultSet().size());
		List<String> result = textHandler.getResult();
		Assert.assertEquals("unitid1-old", result.get(0));
		Assert.assertEquals("unitid1", result.get(1));
		Assert.assertEquals("unitid2-old", result.get(2));
		Assert.assertEquals("unitid2", result.get(3));
		Assert.assertEquals("unitid2hellotester-old1", result.get(4));
	
	}
	/**
	 * Test full existing xpath '/ead/c/did/unitid//text()'
	 * @throws Exception
	 */
	@Test
	public void allTextBelowXpathWithWhitespaces() throws Exception {
		InputStream inputStream = getInputStream("/eu/archivesportaleurope/xml/xpath/handler/test-file.xml");
		TextXpathHandler textHandler = new TextXpathHandler(TestConstants.TEST_NAMESPACE, new String[] {TestConstants.EAD.getLocalPart(), 
				TestConstants.C.getLocalPart(), TestConstants.DID.getLocalPart(),TestConstants.UNITID.getLocalPart()}, true);
		/*
		 * all text below
		 */
		textHandler.setAllTextBelow(true);
		TestTextXpathReader xpathReader = new TestTextXpathReader(textHandler);
		XmlParser.parse(inputStream, xpathReader);
		Assert.assertEquals("unitid1-old", textHandler.getFirstResult());
		Assert.assertEquals(5, textHandler.getResult().size());
		Assert.assertEquals(5, textHandler.getResultSet().size());
		List<String> result = textHandler.getResult();
		Assert.assertEquals("unitid1-old ", result.get(0));
		Assert.assertEquals("unitid1 ", result.get(1));
		Assert.assertEquals("unitid2-old ", result.get(2));
		Assert.assertEquals("unitid2 ", result.get(3));
		Assert.assertEquals("unitid2 hello tester -old1 ", result.get(4));
	
	}
	/**
	 * Test full not-existing xpath '/did/unitid'
	 * @throws Exception
	 */
	@Test
	public void notExistingXpath() throws Exception {
		InputStream inputStream = getInputStream("/eu/archivesportaleurope/xml/xpath/handler/test-file.xml");
		TextXpathHandler textHandler = new TextXpathHandler(TestConstants.TEST_NAMESPACE, new String[] {TestConstants.DID.getLocalPart(),TestConstants.UNITID.getLocalPart()});
		TestTextXpathReader xpathReader = new TestTextXpathReader(textHandler);
		XmlParser.parse(inputStream, xpathReader);
		Assert.assertNull(textHandler.getFirstResult());
		Assert.assertEquals(0, textHandler.getResult().size());
		Assert.assertEquals(0, textHandler.getResultSet().size());
		
	}
	

	/**
	 * Test relative existing xpath '//did/unitid'
	 * @throws Exception
	 */
	@Test
	public void relativeXpath() throws Exception {
		InputStream inputStream = getInputStream("/eu/archivesportaleurope/xml/xpath/handler/test-file.xml");
		TextXpathHandler textHandler = new TextXpathHandler(TestConstants.TEST_NAMESPACE, new String[] {TestConstants.DID.getLocalPart(),TestConstants.UNITID.getLocalPart()});
		/*
		 * set relative
		 */
		textHandler.setRelative(true);
		TestTextXpathReader xpathReader = new TestTextXpathReader(textHandler);
		XmlParser.parse(inputStream, xpathReader);
		Assert.assertEquals("unitid1-old", textHandler.getFirstResult());
		Assert.assertEquals(5, textHandler.getResult().size());
		Assert.assertEquals(5, textHandler.getResultSet().size());
		List<String> result = textHandler.getResult();
		Assert.assertEquals("unitid1-old", result.get(0));
		Assert.assertEquals("unitid1", result.get(1));
		Assert.assertEquals("unitid2-old", result.get(2));
		Assert.assertEquals("unitid2", result.get(3));
		Assert.assertEquals("unitid2-old1", result.get(4));
	}
	
	/**
	 * Test relative existing xpath '//did/unitid[1]'
	 * @throws Exception
	 */
	@Test
	public void relativeXpathOnlyFirst() throws Exception {
		InputStream inputStream = getInputStream("/eu/archivesportaleurope/xml/xpath/handler/test-file.xml");
		TextXpathHandler textHandler = new TextXpathHandler(TestConstants.TEST_NAMESPACE, new String[] {TestConstants.DID.getLocalPart(),TestConstants.UNITID.getLocalPart()});
		/*
		 * set relative
		 */
		textHandler.setRelative(true);
		/*
		 * count first
		 */
		textHandler.setOnlyFirst(true);
		TestTextXpathReader xpathReader = new TestTextXpathReader(textHandler);
		XmlParser.parse(inputStream, xpathReader);
		Assert.assertEquals("unitid1-old", textHandler.getFirstResult());
		Assert.assertEquals(1, textHandler.getResult().size());
		Assert.assertEquals(1, textHandler.getResultSet().size());
		List<String> result = textHandler.getResult();
		Assert.assertEquals("unitid1-old", result.get(0));
	}
	
	/**
	 * Test relative existing xpath '//did/unitid[@type='call number']'
	 * @throws Exception
	 */
	@Test
	public void relativeXpathWithAttribute() throws Exception {
		InputStream inputStream = getInputStream("/eu/archivesportaleurope/xml/xpath/handler/test-file.xml");
		TextXpathHandler textHandler = new TextXpathHandler(TestConstants.TEST_NAMESPACE, new String[] {TestConstants.DID.getLocalPart(),TestConstants.UNITID.getLocalPart()});
		/*
		 * set relative
		 */
		textHandler.setRelative(true);
		/*
		 * with attribute @type='call number'
		 */
		textHandler.setAttribute("type", "call number", false);
		TestTextXpathReader xpathReader = new TestTextXpathReader(textHandler);
		XmlParser.parse(inputStream, xpathReader);
		Assert.assertEquals("unitid1", textHandler.getFirstResult());
		Assert.assertEquals(2, textHandler.getResult().size());
		Assert.assertEquals(2, textHandler.getResultSet().size());
		List<String> result = textHandler.getResult();
		Assert.assertEquals("unitid1", result.get(0));
		Assert.assertEquals("unitid2", result.get(1));
	}
	
	/**
	 * Test relative existing xpath 'count(//did/unitid[not(@type='call number')])'
	 * @throws Exception
	 */
	@Test
	public void relativeXpathWithAttributeNot() throws Exception {
		InputStream inputStream = getInputStream("/eu/archivesportaleurope/xml/xpath/handler/test-file.xml");
		TextXpathHandler textHandler = new TextXpathHandler(TestConstants.TEST_NAMESPACE, new String[] {TestConstants.DID.getLocalPart(),TestConstants.UNITID.getLocalPart()});
		/*
		 * set relative
		 */
		textHandler.setRelative(true);
		/*
		 * with attribute not @type='call number'
		 */
		textHandler.setAttribute("type", "call number", true);
		TestTextXpathReader xpathReader = new TestTextXpathReader(textHandler);
		XmlParser.parse(inputStream, xpathReader);
		Assert.assertEquals("unitid1-old", textHandler.getFirstResult());
		Assert.assertEquals(3, textHandler.getResult().size());
		Assert.assertEquals(3, textHandler.getResultSet().size());
		List<String> result = textHandler.getResult();
		Assert.assertEquals("unitid1-old", result.get(0));
		Assert.assertEquals("unitid2-old", result.get(1));
		Assert.assertEquals("unitid2-old1", result.get(2));
	}
	
	/**
	 * test null values with attributes and attribute values
	 * @throws Exception
	 */
	@Test
	public void relativeXpathWithNullAttribute() throws Exception {
		InputStream inputStream = getInputStream("/eu/archivesportaleurope/xml/xpath/handler/test-file.xml");
		TextXpathHandler textHandler = new TextXpathHandler(TestConstants.TEST_NAMESPACE, new String[] {TestConstants.DID.getLocalPart(),TestConstants.UNITID.getLocalPart()});
		/*
		 * set relative
		 */
		textHandler.setRelative(true);
		/*
		 * with no attribute specified
		 */
		textHandler.setAttribute(null, null, false);
		TestTextXpathReader xpathReader = new TestTextXpathReader(textHandler);
		XmlParser.parse(inputStream, xpathReader);
		Assert.assertEquals("unitid1-old", textHandler.getFirstResult());
		Assert.assertEquals(5, textHandler.getResult().size());
		Assert.assertEquals(5, textHandler.getResultSet().size());
		List<String> result = textHandler.getResult();
		Assert.assertEquals("unitid1-old", result.get(0));
		Assert.assertEquals("unitid1", result.get(1));
		Assert.assertEquals("unitid2-old", result.get(2));
		Assert.assertEquals("unitid2", result.get(3));
		Assert.assertEquals("unitid2-old1", result.get(4));
	}
	/**
	 * test null values with attributes values
	 * @throws Exception
	 */
	@Test
	public void relativeXpathWithNullAttributeValue() throws Exception {
		InputStream inputStream = getInputStream("/eu/archivesportaleurope/xml/xpath/handler/test-file.xml");
		TextXpathHandler textHandler = new TextXpathHandler(TestConstants.TEST_NAMESPACE, new String[] {TestConstants.DID.getLocalPart(),TestConstants.UNITID.getLocalPart()});
		/*
		 * set relative
		 */
		textHandler.setRelative(true);
		/*
		 * with no attribute specified
		 */
		textHandler.setAttribute("test", null, false);
		TestTextXpathReader xpathReader = new TestTextXpathReader(textHandler);
		XmlParser.parse(inputStream, xpathReader);
		Assert.assertEquals("unitid1-old", textHandler.getFirstResult());
		Assert.assertEquals(5, textHandler.getResult().size());
		Assert.assertEquals(5, textHandler.getResultSet().size());
		List<String> result = textHandler.getResult();
		Assert.assertEquals("unitid1-old", result.get(0));
		Assert.assertEquals("unitid1", result.get(1));
		Assert.assertEquals("unitid2-old", result.get(2));
		Assert.assertEquals("unitid2", result.get(3));
		Assert.assertEquals("unitid2-old1", result.get(4));
	}
	
	/**
	 * Test relative existing xpath '//did/unitid[not(@xlink:href='test')]'
	 * @throws Exception
	 */
	@Test
	public void countRelativeXpathWithAttributeWithNamespace() throws Exception {
		InputStream inputStream = getInputStream("/eu/archivesportaleurope/xml/xpath/handler/test-file.xml");
		TextXpathHandler textHandler = new TextXpathHandler(TestConstants.TEST_NAMESPACE, new String[] {TestConstants.DID.getLocalPart(),TestConstants.UNITID.getLocalPart()});
		/*
		 * set relative
		 */
		textHandler.setRelative(true);
		/*
		 * with no attribute specified
		 */
		textHandler.setAttribute(TestConstants.XLINK_NAMESPACE, "href", "test", false);
		TestTextXpathReader xpathReader = new TestTextXpathReader(textHandler);
		XmlParser.parse(inputStream, xpathReader);
		Assert.assertEquals("unitid2-old1", textHandler.getFirstResult());
		Assert.assertEquals(1, textHandler.getResult().size());
		Assert.assertEquals(1, textHandler.getResultSet().size());
		List<String> result = textHandler.getResult();
		Assert.assertEquals("unitid2-old1", result.get(0));
	}
	private static class TestTextXpathReader extends AbstractXpathReader<String> {
		private TextXpathHandler textHandler;

		public TestTextXpathReader(TextXpathHandler textHandler){
			this.textHandler = textHandler;
		}
		@Override
		protected void internalInit() throws Exception {
			getXpathHandlers().clear();
			getXpathHandlers().add(textHandler);
		}

		@Override
		public String getData() {
			return ApeXmlUtil.convertToString(textHandler.getResult(), 0, ApeXmlUtil.WHITE_SPACE);
		}


	}
}
