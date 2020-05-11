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
import eu.apenet.oaiserver.request.RequestProcessor;
import eu.apenet.persistence.vo.Ese;
import eu.apenet.persistence.vo.MetadataFormat;
import eu.apenet.persistence.vo.ResumptionToken;

public class ListRecordsResponse extends ListIdentifiersResponse {

    public ListRecordsResponse(List<Ese> eses, ResumptionToken resumptionToken) {
        super(eses, resumptionToken);
    }

    protected ListRecordsResponse(Ese ese) {
        super(ese);
    }

    @Override
    protected void writeEseFile(XMLStreamWriterHolder writer, Ese ese) throws IOException, XMLStreamException {
        writer.writeStartElement("metadata");
        try (FileInputStream inputStream = getFileInputStream(ese.getPath())) {
            XMLStreamReader xmlReader = getXMLReader(inputStream);
            for (int event = xmlReader.next(); event != XMLStreamConstants.END_DOCUMENT; event = xmlReader.next()) {
                switch (event) {
                    case XMLStreamConstants.START_ELEMENT:
                        QName elementName = xmlReader.getName();
                        if (elementName.getLocalPart().equals("RDF") && MetadataFormat.EDM.equals(ese.getMetadataFormat())) {
                            writer.writeStartDocument(xmlReader);
                        } else {
                            writer.writeStartElement(xmlReader);
                        }
                        break;
                    case XMLStreamConstants.END_ELEMENT:
                        writer.closeElement();
                        break;
                    case XMLStreamConstants.CHARACTERS:
                        writer.writeCharacters(xmlReader);
                        break;
                    case XMLStreamConstants.CDATA:
                        writer.writeCData(xmlReader);
                        break;
                    default:
                        break;
                }
            }
            xmlReader.close();
        }
        writer.closeElement();
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

    @Override
    protected String getVerb() {
        return RequestProcessor.VERB_LIST_RECORDS;
    }
}
