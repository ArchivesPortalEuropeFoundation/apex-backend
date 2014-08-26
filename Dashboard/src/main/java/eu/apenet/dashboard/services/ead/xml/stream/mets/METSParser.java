package eu.apenet.dashboard.services.ead.xml.stream.mets;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.apache.log4j.Logger;

import eu.apenet.dashboard.services.ead.xml.AbstractParser;
import eu.archivesportaleurope.xml.ApeXMLConstants;


public class METSParser extends AbstractParser {

	private static Logger LOGGER = Logger.getLogger(METSParser.class);

    public static List<DaoInfo> parse(InputStream inputStream) throws Exception {
		XMLStreamReader xmlReader = getXMLReader(inputStream);
		QName lastElement = null;

		LinkedList<QName> pathPosition = new LinkedList<QName>();
		METSXpathReader metsXpathReader = new METSXpathReader();
		try {
			for (int event = xmlReader.next(); event != XMLStreamConstants.END_DOCUMENT; event = xmlReader.next()) {
				if (event == XMLStreamConstants.START_ELEMENT) {
					lastElement = xmlReader.getName();
					add(pathPosition, lastElement);
					metsXpathReader.processStartElement(pathPosition, xmlReader);
				} else if (event == XMLStreamConstants.END_ELEMENT) {
					metsXpathReader.processEndElement(pathPosition, xmlReader);
					QName elementName = xmlReader.getName();
					removeLast(pathPosition, elementName);
				} else if (event == XMLStreamConstants.CHARACTERS) {
					metsXpathReader.processCharacters(pathPosition, xmlReader);
				} else if (event == XMLStreamConstants.CDATA) {
					metsXpathReader.processCharacters(pathPosition, xmlReader);
				}
			}
			xmlReader.close();
			inputStream.close();
			return metsXpathReader.getData();
		
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
		return null;
	}

//	private static FileInputStream getFileInputStream(String path) throws FileNotFoundException, XMLStreamException {
//		File file = new File(APEnetUtilities.getConfig().getRepoDirPath() + path);
//		return new FileInputStream(file);
//	}

	private static XMLStreamReader getXMLReader(InputStream inputStream) throws FileNotFoundException,
			XMLStreamException {
		XMLInputFactory inputFactory = (XMLInputFactory) XMLInputFactory.newInstance();
		return (XMLStreamReader) inputFactory.createXMLStreamReader(inputStream, ApeXMLConstants.UTF_8);
	}


	protected static class IndexData {
		private long startIndex = -1;
		private long endIndex = -1;
		private long currentIndex = -1;

		public IndexData(long startIndex, long endIndex) {
			this.startIndex = startIndex;
			this.endIndex = endIndex;

		}

		public long getCurrentIndex() {
			if (currentIndex == -1) {
				this.currentIndex = startIndex;
			} else if (currentIndex < (endIndex - 1)) {
				currentIndex++;
			}
			return currentIndex;
		}

	}
	private static void add(LinkedList<QName> path, QName qName) {
		path.add(qName);
	}

	private static void removeLast(LinkedList<QName> path, QName qName) {
		if (!path.isEmpty()) {
			path.removeLast();
		}
	}
}
