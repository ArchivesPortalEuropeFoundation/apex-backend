package org.oclc.oai.harvester.parser.other.metadata;

import org.oclc.oai.harvester.parser.other.OaiPmhMemoryParser;
import org.oclc.oai.harvester.parser.other.OaiPmhElements;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;
import java.util.LinkedList;

/**
 * User: Yoann Moranville
 * Date: 16/10/2013
 *
 * @author Yoann Moranville
 */
public class OaiPmhMetadataFormatParser extends OaiPmhMemoryParser {
    public static final QName METADATA_PREFIX = new QName(OAI_PMH, "metadataPrefix");

    public OaiPmhMetadataFormatParser() {
        super();
    }

    public OaiPmhElements parse(XMLStreamReader xmlReader) throws Exception {
        LinkedList<QName> path = new LinkedList<QName>();
        OaiPmhElements elements = new OaiPmhElements();
        boolean foundEndElement = false;
        int event =  xmlReader.next();
        QName lastElement = null;
        while(!foundEndElement && event != XMLStreamConstants.END_DOCUMENT){
            if (event == XMLStreamConstants.START_ELEMENT) {
                lastElement = xmlReader.getName();
                add(path, lastElement);
            } else if (event == XMLStreamConstants.CHARACTERS) {
                if (METADATA_PREFIX.equals(lastElement)) {
                    elements.getMetadataFormats().add(xmlReader.getText());
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                QName elementName = xmlReader.getName();
                if (METADATA_PREFIX.equals(elementName)) {
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
