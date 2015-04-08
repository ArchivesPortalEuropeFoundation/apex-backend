package eu.apenet.oaiserver.response;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.oaiserver.config.Configuration;
import eu.apenet.oaiserver.config.vo.MetadataObject;
import eu.apenet.oaiserver.config.vo.ResumptionTokens;
import eu.apenet.oaiserver.request.RequestProcessor;
import eu.apenet.persistence.vo.Ese;
import eu.apenet.persistence.vo.MetadataFormat;
import eu.apenet.persistence.vo.ResumptionToken;

public class ListRecordsResponse extends ListIdentifiersResponse {


	public ListRecordsResponse(List<MetadataObject> metadataObjects, ResumptionTokens resumptionToken) {
		super(metadataObjects, resumptionToken);
	}
	protected ListRecordsResponse(MetadataObject metadataObject) {
		super(metadataObject);
	}

	protected void writeMetadataObjectFile(XMLStreamWriterHolder writer, MetadataObject metadataObject) throws IOException, XMLStreamException {
		writer.writeStartElement("metadata");
		FileInputStream inputStream = getFileInputStream(metadataObject.getXmlPath());
		XMLStreamReader xmlReader = getXMLReader(inputStream);
        boolean isRoot = true;
		for (int event = xmlReader.next(); event != XMLStreamConstants.END_DOCUMENT; event = xmlReader.next()) {
			if (event == XMLStreamConstants.START_ELEMENT) {
				if (isRoot) {
					writer.writeStartDocument(xmlReader);
                    isRoot = false;
				} else {
					writer.writeStartElement(xmlReader);
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
		writer.closeElement();
	}

	private static FileInputStream getFileInputStream(String path) throws FileNotFoundException, XMLStreamException {
		File file = new File(Configuration.XML_DIR_PATH + path);
		return new FileInputStream(file);
	}

	private static XMLStreamReader getXMLReader(FileInputStream fileInputStream) throws FileNotFoundException, XMLStreamException {
		XMLInputFactory inputFactory = (XMLInputFactory) XMLInputFactory.newInstance();
		return (XMLStreamReader) inputFactory.createXMLStreamReader(fileInputStream, UTF_8);
	}

	protected String getVerb(){
		return RequestProcessor.VERB_LIST_RECORDS;
	}
}
