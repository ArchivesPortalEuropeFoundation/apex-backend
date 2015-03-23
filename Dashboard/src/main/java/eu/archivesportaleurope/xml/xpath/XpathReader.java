package eu.archivesportaleurope.xml.xpath;

import java.util.LinkedList;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamReader;
/**
 * Interfaces for xpath and xml parsing.
 * 
 * @author Bastiaan Verhoef
 *
 * @param <T>
 */
public interface XpathReader<T> {
	/**
	 * This method is called when the xml parser reads characters.
	 * 
	 * @param xpathPosition
	 *            Xpath position
	 * @param xmlReader
	 *            XMLStreamReader where the data is available
	 * @throws Exception
	 */
	public void processCharacters(LinkedList<QName> xpathPosition, XMLStreamReader xmlReader) throws Exception;

	/**
	 * This method is called when the xml parser reads a start element.
	 * 
	 * @param xpathPosition
	 *            Xpath position
	 * @param xmlReader
	 *            XMLStreamReader where the data is available
	 * @throws Exception
	 */
	public void processStartElement(LinkedList<QName> xpathPosition, XMLStreamReader xmlReader) throws Exception;

	/**
	 * This method is called when the xml parser reads an end element.
	 * 
	 * @param xpathPosition
	 *            Xpath position
	 * @param xmlReader
	 *            XMLStreamReader where the data is available
	 * @throws Exception
	 */
	public void processEndElement(LinkedList<QName> xpathPosition, XMLStreamReader xmlReader) throws Exception;
	
	/**
	 * Initialize the Xpath Reader.
	 * 
	 * @throws Exception
	 */
	public void init() throws Exception;
	
	/**
	 * Returns the data parsed, based on xpath queries
	 * @return Parsed data
	 */
	public T getData() throws Exception;
}
