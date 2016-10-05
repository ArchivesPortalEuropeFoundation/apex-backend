package eu.archivesportaleurope.harvester.oaipmh.parser.record;

import java.io.File;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

public abstract class AbstractOaiPmhParser {

    protected static final String OAI_PMH = "http://www.openarchives.org/OAI/2.0/";
    protected static final QName RECORD = new QName(OAI_PMH, "record");
    protected static final QName LIST_IDENTIFIERS = new QName(OAI_PMH, "ListIdentifiers");
    protected static final QName METADATA = new QName(OAI_PMH, "metadata");
    protected static final String UTF8 = "UTF-8";
    protected static final QName RESUMPTION_TOKEN = new QName(OAI_PMH, "resumptionToken");
    protected static final QName ERROR = new QName(OAI_PMH, "error");
    protected static final QName HEADER = new QName(OAI_PMH, "header");
    private File outputDirectory;

    public AbstractOaiPmhParser(File outputDirectory) {
        this.outputDirectory = outputDirectory;
    }

    protected File getOutputDirectory() {
        return outputDirectory;
    }

    protected static void writeEndElement(XMLStreamReader xmlReader, XMLStreamWriter xmlWriter) throws XMLStreamException {
        if (xmlWriter != null) {
            xmlWriter.writeEndElement();
        }
    }

    protected static void writeStartElement(XMLStreamReader xmlReader, XMLStreamWriter xmlWriter) throws XMLStreamException {
        if (xmlWriter != null) {
            QName element = xmlReader.getName();
            xmlWriter.writeStartElement(element.getPrefix(), element.getLocalPart(), element.getNamespaceURI());
            for (int i = 0; i < xmlReader.getAttributeCount(); i++) {
                String prefix = xmlReader.getAttributePrefix(i);
                if (prefix == null || prefix.isEmpty()) {
                    xmlWriter.writeAttribute(xmlReader.getAttributeLocalName(i), xmlReader.getAttributeValue(i));
                } else {
                    xmlWriter.writeAttribute(xmlReader.getAttributePrefix(i), xmlReader.getAttributeNamespace(i), xmlReader.getAttributeLocalName(i), xmlReader.getAttributeValue(i));
                }
            }
            for (int i = 0; i < xmlReader.getNamespaceCount(); i++) {
                String prefix = xmlReader.getNamespacePrefix(i);
                String namespaceURI = xmlReader.getNamespaceURI(i);

                if (prefix == null || prefix.isEmpty()) {
                    xmlWriter.writeDefaultNamespace(namespaceURI);
                } else {
                    xmlWriter.writeNamespace(prefix, namespaceURI);
                }
            }
        }
    }

    protected static void writeCharacters(XMLStreamReader xmlReader, XMLStreamWriter xmlWriter) throws XMLStreamException {
        if (xmlWriter != null) {
            xmlWriter.writeCharacters(xmlReader.getText());
        }
    }

    protected static void writeCData(XMLStreamReader xmlReader, XMLStreamWriter xmlWriter) throws XMLStreamException {
        if (xmlWriter != null) {
            xmlWriter.writeCData(xmlReader.getText());
        }
    }
}
