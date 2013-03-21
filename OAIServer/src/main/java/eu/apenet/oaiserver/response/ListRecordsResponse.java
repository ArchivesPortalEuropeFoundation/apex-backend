package eu.apenet.oaiserver.response;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.oaiserver.util.OAIUtils;
import eu.apenet.persistence.vo.Ese;
import eu.apenet.persistence.vo.EseState;
import eu.apenet.persistence.vo.MetadataFormat;
import eu.apenet.persistence.vo.ResumptionToken;

public class ListRecordsResponse extends AbstractResponse {



	private List<Ese> eses;
	private ResumptionToken resumptionToken;

	public ListRecordsResponse(List<Ese> eses, ResumptionToken resumptionToken) {
		this.eses = eses;
		this.resumptionToken = resumptionToken;
	}

	@Override
	protected void generateResponseInternal(XMLStreamWriterHolder writer, Map<String, String> params) throws XMLStreamException, IOException {
		writer.writeStartElement("ListRecords");
		for (Ese ese: eses){
			writer.writeStartElement("record");
			writer.writeStartElement("header");
			if (EseState.REMOVED.equalsIgnoreCase(ese.getEseState().getState())){
				writer.writeAttribute("status", "deleted");
			}
			writer.writeTextElement("identifier" , ese.getOaiIdentifier());
			writer.writeTextElement("datestamp" , OAIUtils.parseDateToISO8601(ese.getModificationDate()));
			writer.writeTextElement("setSpec" , ese.getEset());
			writer.closeElement();
			if (EseState.PUBLISHED.equalsIgnoreCase(ese.getEseState().getState())){
				writer.writeStartElement("metadata");
				writeEseFile(writer, ese);
				writer.closeElement();
			}

			writer.closeElement();
		}
		writeResumptionToken(writer, resumptionToken );
		writer.closeElement();
	}

	private void writeEseFile(XMLStreamWriterHolder writer, Ese ese) throws IOException, XMLStreamException {
		FileInputStream inputStream = getFileInputStream(ese.getPath());
		XMLStreamReader xmlReader = getXMLReader(inputStream);
		for (int event = xmlReader.next(); event != XMLStreamConstants.END_DOCUMENT; event = xmlReader.next()) {
			if (event == XMLStreamConstants.START_ELEMENT) {
				writer.writeStartElement(xmlReader);
				QName elementName = xmlReader.getName();
				if (elementName.getLocalPart().equals("RDF") && MetadataFormat.EDM.equals(ese.getMetadataFormat())){
					writer.writeAttribute(XMLStreamWriterHolder.XMLNS, EDM_NAMESPACE);
					writer.writeAttributeNS(XMLStreamWriterHolder.XMLNS, "ore", "", "http://www.openarchives.org/ore/terms/");
					writer.writeAttributeNS(XMLStreamWriterHolder.XMLNS, "owl", "", "http://www.w3.org/2002/07/owl#");
					writer.writeAttributeNS(XMLStreamWriterHolder.XMLNS, "rdf", "", EDM_NAMESPACE);
					writer.writeAttributeNS(XMLStreamWriterHolder.XMLNS, "skos", "", "http://www.w3.org/2004/02/skos/core#");
					writer.writeAttributeNS(XMLStreamWriterHolder.XMLNS, "wgs84", "", "http://www.w3.org/2003/01/geo/wgs84_pos#");
					writer.writeAttributeNS(XMLStreamWriterHolder.XMLNS, "dc", "", "http://purl.org/dc/elements/1.1/");
					writer.writeAttributeNS(XMLStreamWriterHolder.XMLNS, "dcterms", "", "http://purl.org/dc/terms/");
					writer.writeAttributeNS(XMLStreamWriterHolder.XMLNS, "edm", "", "http://www.europeana.eu/schemas/edm/");
					writer.writeAttributeNS(XMLStreamWriterHolder.XMLNS, "enrichment", "", "http://www.europeana.eu/schemas/edm/enrichment");
					writer.writeAttributeNS(XMLStreamWriterHolder.XMLNS, "europeana", "", "http://www.europeana.eu/schemas/ese/");
					writer.writeAttributeNS(XMLStreamWriterHolder.XSI_PREFIX, XMLStreamWriterHolder.SCHEMA_LOCATION,"", EDM_NAMESPACE + " " + EDM_SCHEMA_LOCATION);
				}
				
				
			} else if (event == XMLStreamConstants.END_ELEMENT) {
				writer.closeElement();
			} else if (event == XMLStreamConstants.CHARACTERS) {
				writer.writeCharacters(xmlReader);
			} else if (event == XMLStreamConstants.CDATA) {
				writer.writeCData(xmlReader);
			}
		}
		xmlReader.close();
		inputStream.close();
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


}
