package eu.archivesportaleurope.xml;

import static org.junit.Assert.fail;

import java.io.InputStream;

import junit.framework.Assert;

import org.junit.Test;

import eu.archivesportaleurope.xml.xpath.DummyXpathReader;

public class XmlParserTest extends AbstractTest{

	@Test
	public void testNoInputStreamArgument() throws Exception {
		InputStream inputStream = getInputStream("/eu/archivesportaleurope/xml-notexist");
		try {
			XmlParser.parse(inputStream, new DummyXpathReader());
			fail ("No illegal argument exception");
		}catch (IllegalArgumentException e){
			
		}
	}
	
	@Test
	public void testNoXpathReaderArgument() throws Exception {
		InputStream inputStream = getInputStream("/eu/archivesportaleurope/xml/valid.xml");
		try {
			XmlParser.parse(inputStream, null);
			fail ("No illegal argument exception");
		}catch (IllegalArgumentException e){
			
		}
	}
	
	@Test
	public void testValidXml() throws Exception {
		InputStream inputStream = getInputStream("/eu/archivesportaleurope/xml/valid.xml");
		XmlParser.parse(inputStream, new DummyXpathReader());
		
	}
	@Test
	public void testInvalidXml() throws Exception {
		InputStream inputStream = getInputStream("/eu/archivesportaleurope/xml/invalid.xml");
		try {
			XmlParser.parse(inputStream, new DummyXpathReader());
			fail("No exception thrown");
		}catch (XmlException e){
			
		}
		
	}

	@Test
	public void testInvalidDtd() throws Exception {
		InputStream inputStream = getInputStream("/eu/archivesportaleurope/xml/invalid-dtd.xml");
		XmlParser.parse(inputStream, new DummyXpathReader());
	}
	
	@Test
	public void testCorrectNumberOfElementsAndCharactersXml() throws Exception {
		InputStream inputStream = getInputStream("/eu/archivesportaleurope/xml/valid-counting-elements-and-characters.xml");
		DummyXpathReader reader = new DummyXpathReader();
		XmlParser.parse(inputStream, reader);
		Assert.assertTrue(reader.isInited());
		Assert.assertEquals(2, reader.getNumberOfStartElements());
		Assert.assertEquals(2, reader.getNumberOfEndElements());
		Assert.assertEquals(3, reader.getNumberOfCharacters());
	}
	
}
