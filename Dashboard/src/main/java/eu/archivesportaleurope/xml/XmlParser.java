package eu.archivesportaleurope.xml;

import java.io.InputStream;
import java.util.LinkedList;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;

import eu.archivesportaleurope.harvester.util.StreamUtil;
import eu.archivesportaleurope.xml.xpath.XpathReader;

/**
 * XML parser based on StAX. This parser supports a programmatically way of
 * xpath queries.
 * 
 * @author Bastiaan Verhoef
 *
 */
public class XmlParser {

	/**
	 * Parse xml file based on StAX.
	 * 
	 * @param inputStream
	 *            Stream that contains the xml file
	 * @param xpathReader
	 *            Xpath reader that is able to read xpath.
	 * @throws Exception
	 */
	public static final void parse(InputStream inputStream, XpathReader<?> xpathReader) throws Exception {
		if (xpathReader == null) {
			throw new IllegalArgumentException("No xpath reader specified");
		} else if (inputStream == null) {
			throw new IllegalArgumentException("No inputStream specified");
		}
		XMLStreamReader xmlReader = null;
		try {
			xmlReader = StreamUtil.getXMLStreamReader(inputStream);
			QName lastElement = null;
			/*
			 * init xpath reader
			 */
			xpathReader.init();
			LinkedList<QName> pathPosition = new LinkedList<QName>();
			for (int event = xmlReader.next(); event != XMLStreamConstants.END_DOCUMENT; event = xmlReader.next()) {
				/*
				 * iterate through the whole xml file.
				 */
				if (event == XMLStreamConstants.START_ELEMENT) {
					lastElement = xmlReader.getName();
					/*
					 * add qname to xpath position
					 */
					pathPosition.add(lastElement);
					xpathReader.processStartElement(pathPosition, xmlReader);
				} else if (event == XMLStreamConstants.END_ELEMENT) {
					xpathReader.processEndElement(pathPosition, xmlReader);
					/*
					 * remove qname from xpath position
					 */
					if (!pathPosition.isEmpty()) {
						pathPosition.removeLast();
					}
				} else if (event == XMLStreamConstants.CHARACTERS) {
					xpathReader.processCharacters(pathPosition, xmlReader);
				}
			}
		} catch (Exception e) {
			throw new XmlException(e);

		} finally {
			if (xmlReader != null) {
				xmlReader.close();
			}
			inputStream.close();
		}
	}

}
