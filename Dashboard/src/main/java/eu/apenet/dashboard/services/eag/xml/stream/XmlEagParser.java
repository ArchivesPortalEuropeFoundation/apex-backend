package eu.apenet.dashboard.services.eag.xml.stream;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.LinkedList;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.apache.log4j.Logger;

import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.dashboard.services.ead.xml.AbstractParser;
import eu.apenet.dashboard.services.eag.xml.stream.publish.EagPublishData;
import eu.apenet.dashboard.services.eag.xml.stream.publish.EagSolrPublisher;
import eu.apenet.persistence.vo.ArchivalInstitution;


public class XmlEagParser extends AbstractParser {

	private static Logger LOG = Logger.getLogger(XmlEagParser.class);
	public static final String UTF_8 = "utf-8";


    public static long parseAndPublish(ArchivalInstitution archivalInstitution) throws Exception {
    	EagSolrPublisher solrPublisher = new EagSolrPublisher();

		FileInputStream fileInputStream =  getFileInputStream(archivalInstitution.getEagPath());

		XMLStreamReader xmlReader = getXMLReader(fileInputStream);
		QName lastElement = null;

		LinkedList<QName> pathPosition = new LinkedList<QName>();
		EagPublishDataFiller eagParser = new EagPublishDataFiller();
		try {
			for (int event = xmlReader.next(); event != XMLStreamConstants.END_DOCUMENT; event = xmlReader.next()) {
				if (event == XMLStreamConstants.START_ELEMENT) {
					lastElement = xmlReader.getName();
					add(pathPosition, lastElement);
					eagParser.processStartElement(pathPosition, xmlReader);
				} else if (event == XMLStreamConstants.END_ELEMENT) {
					eagParser.processEndElement(pathPosition, xmlReader);
					QName elementName = xmlReader.getName();
					removeLast(pathPosition, elementName);
				} else if (event == XMLStreamConstants.CHARACTERS) {
					eagParser.processCharacters(pathPosition, xmlReader);
				} else if (event == XMLStreamConstants.CDATA) {
					eagParser.processCharacters(pathPosition, xmlReader);
				}
			}
			xmlReader.close();
			fileInputStream.close();
			EagPublishData publishData = new EagPublishData();
			eagParser.fillData(publishData, archivalInstitution);
			solrPublisher.publish(archivalInstitution, publishData);
			solrPublisher.commitSolrDocuments();
		
		} catch (Exception de) {
			if (solrPublisher != null) {
				LOG.error(archivalInstitution + ": rollback:", de);
				solrPublisher.unpublish(archivalInstitution);
			}
			throw de;
		}
		return solrPublisher.getSolrTime();
	}

	private static FileInputStream getFileInputStream(String path) throws FileNotFoundException, XMLStreamException {
		File file = new File(APEnetUtilities.getConfig().getRepoDirPath() + path);
		return new FileInputStream(file);
	}

	private static XMLStreamReader getXMLReader(FileInputStream fileInputStream) throws FileNotFoundException,
			XMLStreamException {
		XMLInputFactory inputFactory = (XMLInputFactory) XMLInputFactory.newInstance();
		return (XMLStreamReader) inputFactory.createXMLStreamReader(fileInputStream, UTF_8);
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
