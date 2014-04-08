package eu.archivesportaleurope.harvester.util;

import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

public class StreamUtil {
	protected static final String UTF8 = "UTF-8";
	public static XMLStreamReader getXMLStreamReader(InputStream inputStream) throws XMLStreamException{
		XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
		xmlInputFactory.setXMLResolver(new DummyXMLResolver());
		return xmlInputFactory.createXMLStreamReader(inputStream, UTF8);
	}
	public static XMLStreamWriter getXMLStreamWriter(OutputStream outputStream) throws XMLStreamException, FactoryConfigurationError{
		return  XMLOutputFactory.newInstance().createXMLStreamWriter(outputStream, UTF8);
	}
}
