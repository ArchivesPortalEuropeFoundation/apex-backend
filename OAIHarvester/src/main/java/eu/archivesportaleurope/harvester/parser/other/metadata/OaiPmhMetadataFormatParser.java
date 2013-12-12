package eu.archivesportaleurope.harvester.parser.other.metadata;

import eu.archivesportaleurope.harvester.parser.other.OaiPmhElement;
import eu.archivesportaleurope.harvester.parser.other.OaiPmhElements;
import eu.archivesportaleurope.harvester.parser.other.OaiPmhMemoryParser;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import java.util.LinkedList;

/**
 * User: Yoann Moranville
 * Date: 16/10/2013
 *
 * @author Yoann Moranville
 */
public class OaiPmhMetadataFormatParser extends OaiPmhMemoryParser {
    private static final QName METADATA_PREFIX = new QName(OAI_PMH, "metadataPrefix");
    private static final QName LIST_METADATA_FORMATS = new QName(OAI_PMH, "ListMetadataFormats");

    public OaiPmhMetadataFormatParser() {
        super();
    }

    public OaiPmhElements parse(XMLStreamReader xmlReader) throws XMLStreamException {
        LinkedList<QName> path = new LinkedList<QName>();
        OaiPmhElements elements = new OaiPmhElements();
        boolean foundEndElement = false;
        int event =  xmlReader.next();
        QName lastElement = null;
        String metadata = "";
        while(!foundEndElement && event != XMLStreamConstants.END_DOCUMENT){
            if (event == XMLStreamConstants.START_ELEMENT) {
                lastElement = xmlReader.getName();
                add(path, lastElement);
            } else if (event == XMLStreamConstants.CHARACTERS) {
                if (METADATA_PREFIX.equals(lastElement)) {
                    metadata += xmlReader.getText();
                } else if (RESUMPTION_TOKEN.equals(lastElement)) {
                    elements.setResumptionToken(xmlReader.getText());
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                QName elementName = xmlReader.getName();
                if (METADATA_PREFIX.equals(elementName)) {
                    elements.getElements().add(new OaiPmhElement(metadata.trim()));
                    metadata = "";
                }
                if (LIST_METADATA_FORMATS.equals(elementName)) {
                    foundEndElement = true;
                } else {
                    removeLast(path, elementName);
                }
            }
            if (!foundEndElement){
                event = xmlReader.next();
            }
        }
        return elements;
    }

    private static void add(LinkedList<QName> path, QName qName) {
        if (!METADATA_FORMAT.equals(qName)) {
            path.add(qName);
        }
    }

    private static void removeLast(LinkedList<QName> path, QName qName) {
        if (!METADATA_FORMAT.equals(qName)) {
            if (!path.isEmpty()) {
                path.removeLast();
            }
        }
    }
}
