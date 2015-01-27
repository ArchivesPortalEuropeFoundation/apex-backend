package eu.archivesportaleurope.xml.xpath;

import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamReader;

import eu.archivesportaleurope.xml.xpath.handler.XpathHandler;

public class DummyXmlStreamHandler implements XpathHandler {

	private List<QName> xpathPosition;
	private XMLStreamReader xmlReader;
	
	public List<QName> getXpathPosition() {
		return xpathPosition;
	}

	public XMLStreamReader getXmlReader() {
		return xmlReader;
	}

	@Override
	public void processCharacters(List<QName> xpathPosition, XMLStreamReader xmlReader) throws Exception {
		this.xpathPosition =xpathPosition;
		this.xmlReader =xmlReader;

	}

	@Override
	public void processStartElement(List<QName> xpathPosition, XMLStreamReader xmlReader) throws Exception {
		this.xpathPosition =xpathPosition;
		this.xmlReader =xmlReader;

	}

	@Override
	public void processEndElement(List<QName> xpathPosition, XMLStreamReader xmlReader) throws Exception {
		this.xpathPosition =xpathPosition;
		this.xmlReader =xmlReader;

	}

}
