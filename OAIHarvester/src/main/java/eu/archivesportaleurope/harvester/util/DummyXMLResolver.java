package eu.archivesportaleurope.harvester.util;

import java.io.StringReader;

import javax.xml.stream.XMLResolver;
import javax.xml.stream.XMLStreamException;
import javax.xml.transform.stream.StreamSource;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;



public class DummyXMLResolver implements XMLResolver {
 
	
	@Override
	public Object resolveEntity(String publicID, String systemID, String baseURI, String namespace)
			throws XMLStreamException {
		return new StreamSource(new StringReader(""));
	}

	public InputSource resolveEntity(String publicID, String systemID)
        throws SAXException {
        
        return new InputSource(new StringReader(""));
    }
}
