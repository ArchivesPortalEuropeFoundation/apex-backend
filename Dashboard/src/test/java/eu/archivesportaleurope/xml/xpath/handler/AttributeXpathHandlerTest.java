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

public class AttributeXpathHandlerTest extends AbstractTest {

	/**
	 * Test full existing xpath '/ead/c/did/unitid@type'
	 * @throws Exception
	 */
	@Test
	public void fullXpath() throws Exception {
		InputStream inputStream = getInputStream("/eu/archivesportaleurope/xml/xpath/handler/test-file.xml");
		AttributeXpathHandler textHandler = new AttributeXpathHandler(TestConstants.TEST_NAMESPACE, new String[] {TestConstants.EAD.getLocalPart(), 
				TestConstants.C.getLocalPart(), TestConstants.DID.getLocalPart(),TestConstants.UNITID.getLocalPart()}, "type");
		TestAttributeXpathReader xpathReader = new TestAttributeXpathReader(textHandler);
		XmlParser.parse(inputStream, xpathReader);
		Assert.assertEquals("former call number", textHandler.getFirstResult());
		Assert.assertEquals(4, textHandler.getResult().size());
		Assert.assertEquals(2, textHandler.getResultSet().size());
		List<String> result = textHandler.getResult();
		Assert.assertEquals("former call number", result.get(0));
		Assert.assertEquals("call number", result.get(1));
		Assert.assertEquals("former call number", result.get(2));
		Assert.assertEquals("call number", result.get(3));
	}
	
	/**
	 * Test full existing xpath '/ead/c/did/unitid@type'
	 * @throws Exception
	 */
	@Test
	public void fullXpathWithNamespace() throws Exception {
		InputStream inputStream = getInputStream("/eu/archivesportaleurope/xml/xpath/handler/test-file.xml");
		AttributeXpathHandler textHandler = new AttributeXpathHandler(TestConstants.TEST_NAMESPACE, new String[] {TestConstants.EAD.getLocalPart(), 
				TestConstants.C.getLocalPart(), TestConstants.DID.getLocalPart(),TestConstants.UNITID.getLocalPart()}, TestConstants.XLINK_NAMESPACE, "href");
		TestAttributeXpathReader xpathReader = new TestAttributeXpathReader(textHandler);
		XmlParser.parse(inputStream, xpathReader);
		Assert.assertEquals("test", textHandler.getFirstResult());
		Assert.assertEquals(1, textHandler.getResult().size());
		Assert.assertEquals(1, textHandler.getResultSet().size());
		List<String> result = textHandler.getResult();
		Assert.assertEquals("test", result.get(0));
	}

	private static class TestAttributeXpathReader extends AbstractXpathReader<String> {
		private AttributeXpathHandler attributeHandler;

		public TestAttributeXpathReader(AttributeXpathHandler attributeHandler){
			this.attributeHandler = attributeHandler;
		}
		@Override
		protected void internalInit() throws Exception {
			getXpathHandlers().clear();
			getXpathHandlers().add(attributeHandler);
		}

		@Override
		public String getData() {
			return  ApeXmlUtil.convertToString(attributeHandler.getResult(), 0, ApeXmlUtil.WHITE_SPACE);
		}


	}
}
