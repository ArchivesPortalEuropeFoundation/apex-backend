package eu.apenet.dashboard.services.ead.xml.stream.mets;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.xml.sax.SAXException;

import eu.apenet.dashboard.services.ead.xml.AbstractParser;
import eu.apenet.dpt.utils.util.APEXmlCatalogResolver;
import eu.archivesportaleurope.xml.ApeXMLConstants;
import eu.archivesportaleurope.xml.XmlParser;

public class METSParser extends AbstractParser {

	public static MetsInfo parse(File file) throws Exception {
		try {
			validateMETS(file);
			METSXpathReader metsXpathReader = new METSXpathReader();
			FileInputStream inputStream = new FileInputStream(file);
			XmlParser.parse(inputStream, metsXpathReader);
			inputStream.close();
			return metsXpathReader.getData();
		}catch (Exception e) {
			throw e;

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



}
