package eu.archivesportaleurope.xml.xpath.handler;

import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamReader;

/**
 * Interface for handlers that process data based on the xpath position.
 * 
 * @author Bastiaan Verhoef
 *
 */
public interface XpathHandler {
	/**
	 * This method is called when the xml parser reads characters.
	 * 
	 * @param xpathPosition
	 *            Xpath position
	 * @param xmlReader
	 *            XMLStreamReader where the data is available
	 * @throws Exception
	 */
	public void processCharacters(List<QName> xpathPosition, XMLStreamReader xmlReader) throws Exception;

	/**
	 * This method is called when the xml parser reads a start element.
	 * 
	 * @param xpathPosition
	 *            Xpath position
	 * @param xmlReader
	 *            XMLStreamReader where the data is available
	 * @throws Exception
	 */
	public void processStartElement(List<QName> xpathPosition, XMLStreamReader xmlReader) throws Exception;

	/**
	 * This method is called when the xml parser reads an end element.
	 * 
	 * @param xpathPosition
	 *            Xpath position
	 * @param xmlReader
	 *            XMLStreamReader where the data is available
	 * @throws Exception
	 */
	public void processEndElement(List<QName> xpathPosition, XMLStreamReader xmlReader) throws Exception;
}
