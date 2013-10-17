package org.oclc.oai.harvester.parser.other.set;

import org.oclc.oai.harvester.parser.other.OaiPmhElements;
import org.oclc.oai.harvester.parser.other.OaiPmhMemoryParser;

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
public class OaiPmhSetParser extends OaiPmhMemoryParser {
    private static final QName SET_SPEC_PREFIX = new QName(OAI_PMH, "setSpec");
    private static final QName LIST_SETS = new QName(OAI_PMH, "ListSets");

    public OaiPmhSetParser() {
        super();
    }

    public OaiPmhElements parse(XMLStreamReader xmlReader) throws XMLStreamException {
        LinkedList<QName> path = new LinkedList<QName>();
        OaiPmhElements elements = new OaiPmhElements();
        boolean foundEndElement = false;
        int event =  xmlReader.next();
        QName lastElement = null;
        String set = "";
        while(!foundEndElement && event != XMLStreamConstants.END_DOCUMENT){
            if (event == XMLStreamConstants.START_ELEMENT) {
                lastElement = xmlReader.getName();
                add(path, lastElement);
            } else if (event == XMLStreamConstants.CHARACTERS) {
                if (SET_SPEC_PREFIX.equals(lastElement)) {
                    set += xmlReader.getText();
                } else if (RESUMPTION_TOKEN.equals(lastElement)) {
                    elements.setResumptionToken(xmlReader.getText());
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                QName elementName = xmlReader.getName();
                if(SET_SPEC_PREFIX.equals(elementName)) {
                    elements.getElements().add(set.trim());
                    set = "";
                }
                if (LIST_SETS.equals(elementName)) {
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
        if (!SET.equals(qName)) {
            path.add(qName);
        }
    }

    private static void removeLast(LinkedList<QName> path, QName qName) {
        if (!SET.equals(qName)) {
            if (!path.isEmpty()) {
                path.removeLast();
            }
        }
    }
}
