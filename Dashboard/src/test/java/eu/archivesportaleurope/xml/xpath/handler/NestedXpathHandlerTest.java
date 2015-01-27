package eu.archivesportaleurope.xml.xpath.handler;

import java.io.InputStream;

import org.junit.Assert;
import org.junit.Test;

import eu.archivesportaleurope.xml.AbstractTest;
import eu.archivesportaleurope.xml.TestConstants;
import eu.archivesportaleurope.xml.XmlParser;
import eu.archivesportaleurope.xml.xpath.AbstractXpathReader;

public class NestedXpathHandlerTest extends AbstractTest {

	/**
	 * Test xpath '/ead/c' together with ./did/unitid and ./did/unittitle
	 * @throws Exception
	 */
	@Test
	public void fullXpath() throws Exception {
		InputStream inputStream = getInputStream("/eu/archivesportaleurope/xml/xpath/handler/nested-test-file.xml");
		NestedXpathHandler nestedXpathHandler = new NestedXpathHandler(TestConstants.TEST_NAMESPACE, new String[] {TestConstants.EAD.getLocalPart(), 
				TestConstants.C.getLocalPart()});
		CountXpathHandler countUnitidHandler = new CountXpathHandler(TestConstants.TEST_NAMESPACE, new String[] {TestConstants.DID.getLocalPart(),TestConstants.UNITID.getLocalPart()});
		CountXpathHandler countUnittitleHandler = new CountXpathHandler(TestConstants.TEST_NAMESPACE, new String[] {TestConstants.DID.getLocalPart(),TestConstants.UNITTITLE.getLocalPart()});
		nestedXpathHandler.getHandlers().add(countUnittitleHandler);
		nestedXpathHandler.getHandlers().add(countUnitidHandler);
		TestNestedXpathReader xpathReader = new TestNestedXpathReader(nestedXpathHandler);
		XmlParser.parse(inputStream, xpathReader);
		Assert.assertEquals(5, countUnitidHandler.getCount());
		Assert.assertEquals(2, countUnittitleHandler.getCount());
	}
	
	/**
	 * Test xpath '/ead/c[1]' together with ./did/unitid  and ./did/unittitle
	 * @throws Exception
	 */
	@Test
	public void fullXpathOnlyFirst() throws Exception {
		InputStream inputStream = getInputStream("/eu/archivesportaleurope/xml/xpath/handler/nested-test-file.xml");
		NestedXpathHandler nestedXpathHandler = new NestedXpathHandler(TestConstants.TEST_NAMESPACE, new String[] {TestConstants.EAD.getLocalPart(), 
				TestConstants.C.getLocalPart()});
		nestedXpathHandler.setOnlyFirst(true);
		CountXpathHandler countUnitidHandler = new CountXpathHandler(TestConstants.TEST_NAMESPACE, new String[] {TestConstants.DID.getLocalPart(),TestConstants.UNITID.getLocalPart()});
		CountXpathHandler countUnittitleHandler = new CountXpathHandler(TestConstants.TEST_NAMESPACE, new String[] {TestConstants.DID.getLocalPart(),TestConstants.UNITTITLE.getLocalPart()});
		nestedXpathHandler.getHandlers().add(countUnittitleHandler);
		nestedXpathHandler.getHandlers().add(countUnitidHandler);
		TestNestedXpathReader xpathReader = new TestNestedXpathReader(nestedXpathHandler);
		XmlParser.parse(inputStream, xpathReader);
		Assert.assertEquals(2, countUnitidHandler.getCount());
		Assert.assertEquals(1, countUnittitleHandler.getCount());
	}
	
	/**
	 * Test xpath '//c' together with ./did/unitid and ./did/unittitle
	 * @throws Exception
	 */
	@Test
	public void relativeXpath() throws Exception {
		InputStream inputStream = getInputStream("/eu/archivesportaleurope/xml/xpath/handler/nested-test-file.xml");
		NestedXpathHandler nestedXpathHandler = new NestedXpathHandler(TestConstants.TEST_NAMESPACE, new String[] {TestConstants.C.getLocalPart()});
		nestedXpathHandler.setRelative(true);
		CountXpathHandler countUnitidHandler = new CountXpathHandler(TestConstants.TEST_NAMESPACE, new String[] {TestConstants.DID.getLocalPart(),TestConstants.UNITID.getLocalPart()});
		CountXpathHandler countUnittitleHandler = new CountXpathHandler(TestConstants.TEST_NAMESPACE, new String[] {TestConstants.DID.getLocalPart(),TestConstants.UNITTITLE.getLocalPart()});
		nestedXpathHandler.getHandlers().add(countUnittitleHandler);
		nestedXpathHandler.getHandlers().add(countUnitidHandler);
		TestNestedXpathReader xpathReader = new TestNestedXpathReader(nestedXpathHandler);
		XmlParser.parse(inputStream, xpathReader);
		Assert.assertEquals(5, countUnitidHandler.getCount());
		Assert.assertEquals(2, countUnittitleHandler.getCount());
	}

	private static class TestNestedXpathReader extends AbstractXpathReader<Object> {
		private NestedXpathHandler nestedXpathHandler;

		public TestNestedXpathReader(NestedXpathHandler nestedXpathHandler){
			this.nestedXpathHandler = nestedXpathHandler;
		}
		@Override
		protected void internalInit() throws Exception {
			getXpathHandlers().clear();
			getXpathHandlers().add(nestedXpathHandler);
		}

		@Override
		public Object getData() {
			return null;
		}


	}
}
