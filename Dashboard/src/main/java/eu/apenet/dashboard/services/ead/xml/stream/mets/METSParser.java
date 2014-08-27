package eu.apenet.dashboard.services.ead.xml.stream.mets;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.xml.sax.SAXException;

import eu.apenet.dashboard.services.ead.xml.AbstractParser;
import eu.apenet.dpt.utils.util.APEXmlCatalogResolver;
import eu.archivesportaleurope.xml.ApeXMLConstants;

public class METSParser extends AbstractParser {

	public static List<DaoInfo> parse(File file) throws Exception {
		XMLStreamReader xmlReader = null;
		try {
			validateMETS(file);
			xmlReader = getXMLReader(file);
			QName lastElement = null;

			LinkedList<QName> pathPosition = new LinkedList<QName>();
			METSXpathReader metsXpathReader = new METSXpathReader();
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
			return metsXpathReader.getData();
		}catch (Exception e) {
			throw e;

		}finally {
			if (xmlReader != null){
				xmlReader.close();
			}
		}
	}

	private static void validateMETS(File file) throws SAXException, IOException {
		List<URL> schemaURLs = new ArrayList<URL>();
		schemaURLs.add(METSParser.class.getResource("/apeMETS.xsd"));
		schemaURLs.add(METSParser.class.getResource("/apeMETSRights.xsd"));
		schemaURLs.add(METSParser.class.getResource("/apeMETSxlink.xsd"));
		InputStreamReader reader = null;
		try {
			reader = new InputStreamReader(new FileInputStream(file), ApeXMLConstants.UTF_8);
			StreamSource source = new StreamSource(reader);
			Schema schema = getSchema(schemaURLs);
			Validator validator = schema.newValidator();
			validator.validate(source);
		} finally {
			if (reader != null){
				reader.close();
			}
		}

	}

	private static Schema getSchema(List<URL> schemaURLs) throws SAXException {
		SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		schemaFactory.setResourceResolver(new APEXmlCatalogResolver());
		List<StreamSource> schemaSources = new ArrayList<StreamSource>();
		for (URL schemaURL : schemaURLs) {
			schemaSources.add(new StreamSource(schemaURL.toExternalForm()));
		}
		return schemaFactory.newSchema(schemaSources.toArray(new StreamSource[] {}));
	}

	private static XMLStreamReader getXMLReader(File file) throws FileNotFoundException, XMLStreamException {
		XMLInputFactory inputFactory = (XMLInputFactory) XMLInputFactory.newInstance();
		return (XMLStreamReader) inputFactory.createXMLStreamReader(new FileInputStream(file), ApeXMLConstants.UTF_8);
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
