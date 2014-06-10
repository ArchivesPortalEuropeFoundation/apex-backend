package eu.archivesportaleurope.xml.xpath;

import java.util.LinkedList;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamReader;

public interface XmlStreamHandler {
	public void processCharacters(LinkedList<QName> xpathPosition, XMLStreamReader xmlReader) throws Exception;
	public void processStartElement(LinkedList<QName> xpathPosition, XMLStreamReader xmlReader) throws Exception;
	public void processEndElement(LinkedList<QName> xpathPosition, XMLStreamReader xmlReader) throws Exception;
}
