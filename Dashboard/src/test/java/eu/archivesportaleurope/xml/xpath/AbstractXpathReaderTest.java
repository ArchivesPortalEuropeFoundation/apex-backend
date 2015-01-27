package eu.archivesportaleurope.xml.xpath;

import java.util.LinkedList;

import javax.xml.namespace.QName;

import junit.framework.Assert;

import org.junit.Test;

import eu.archivesportaleurope.xml.AbstractTest;

public class AbstractXpathReaderTest extends AbstractTest {

	@Test
	public void testInit() throws Exception {
		TestXpathReader testXpathReader = new TestXpathReader();
		Assert.assertNull(testXpathReader.getDummy1Handler());
		Assert.assertNull(testXpathReader.getDummy2Handler());
		testXpathReader.init();
		Assert.assertNotNull(testXpathReader.getDummy1Handler());
		Assert.assertNotNull(testXpathReader.getDummy2Handler());
	}

	@Test
	public void testNotInit() throws Exception {
		TestXpathReader testXpathReader = new TestXpathReader();
		Assert.assertNull(testXpathReader.getDummy1Handler());
		Assert.assertNull(testXpathReader.getDummy2Handler());
		testXpathReader.processStartElement(null, null);
		testXpathReader.processStartElement(new LinkedList<QName>(), null);
		testXpathReader.processEndElement(null, null);
		testXpathReader.processEndElement(new LinkedList<QName>(), null);
		testXpathReader.processCharacters(null, null);
		testXpathReader.processCharacters(new LinkedList<QName>(), null);
	}

	
	@Test
	public void testStartElement() throws Exception {
		TestXpathReader testXpathReader = new TestXpathReader();
		testXpathReader.init();
		LinkedList<QName> xpathPosition = new LinkedList<QName>();
		testXpathReader.processStartElement(xpathPosition, null);
		Assert.assertEquals(testXpathReader.getDummy1Handler().getXpathPosition(), xpathPosition);
		Assert.assertEquals(testXpathReader.getDummy2Handler().getXpathPosition(), xpathPosition);
	}
	
	@Test
	public void testEndElement() throws Exception {
		TestXpathReader testXpathReader = new TestXpathReader();
		testXpathReader.init();
		LinkedList<QName> xpathPosition = new LinkedList<QName>();
		testXpathReader.processEndElement(xpathPosition, null);
		Assert.assertEquals(testXpathReader.getDummy1Handler().getXpathPosition(), xpathPosition);
		Assert.assertEquals(testXpathReader.getDummy2Handler().getXpathPosition(), xpathPosition);
	}
	
	@Test
	public void testCharacters() throws Exception {
		TestXpathReader testXpathReader = new TestXpathReader();
		testXpathReader.init();
		LinkedList<QName> xpathPosition = new LinkedList<QName>();
		testXpathReader.processCharacters(xpathPosition, null);
		Assert.assertEquals(testXpathReader.getDummy1Handler().getXpathPosition(), xpathPosition);
		Assert.assertEquals(testXpathReader.getDummy2Handler().getXpathPosition(), xpathPosition);
	}
	
	private static class TestXpathReader extends AbstractXpathReader<Object> {
		private DummyXmlStreamHandler dummy1Handler;
		private DummyXmlStreamHandler dummy2Handler;

		@Override
		protected void internalInit() throws Exception {
			dummy1Handler = new DummyXmlStreamHandler();
			dummy2Handler = new DummyXmlStreamHandler();
			getXpathHandlers().add(dummy1Handler);
			getXpathHandlers().add(dummy2Handler);
		}

		@Override
		public Object getData() {
			// TODO Auto-generated method stub
			return null;
		}

		public DummyXmlStreamHandler getDummy1Handler() {
			return dummy1Handler;
		}

		public DummyXmlStreamHandler getDummy2Handler() {
			return dummy2Handler;
		}

	}
}
