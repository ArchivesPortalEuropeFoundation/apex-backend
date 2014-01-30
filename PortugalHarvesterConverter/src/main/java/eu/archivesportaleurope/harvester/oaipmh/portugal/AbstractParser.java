package eu.archivesportaleurope.harvester.oaipmh.portugal;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

public abstract class AbstractParser {
    public static final String APENET_EAD = "urn:isbn:1-931666-22-9";
    public static final String XLINK = "http://www.w3.org/1999/xlink";
    public static final String XSI = "http://www.w3.org/2001/XMLSchema-instance";

    protected static final QName EAD_ELEMENT = new QName(APENET_EAD, "ead");
    protected static final QName C_ELEMENT = new QName(APENET_EAD, "c");


    protected static void writeEndElement(XMLStreamReader xmlReader, XMLStreamWriter xmlWriter) throws XMLStreamException{
        if (xmlWriter != null){
            xmlWriter.writeEndElement();
        }
    }

    protected static void writeStartElement(XMLStreamReader xmlReader, XMLStreamWriter xmlWriter) throws XMLStreamException{
        if (xmlWriter != null){
            QName element = xmlReader.getName();
            xmlWriter.writeStartElement(element.getPrefix(), element.getLocalPart(), element.getNamespaceURI());
            for (int i=0; i < xmlReader.getAttributeCount(); i++){
                xmlWriter.writeAttribute(xmlReader.getAttributePrefix(i), xmlReader.getAttributeNamespace(i), xmlReader.getAttributeLocalName(i), xmlReader.getAttributeValue(i));
            }
        }
    }


    protected static void writeCharacters(XMLStreamReader xmlReader, XMLStreamWriter xmlWriter) throws XMLStreamException{
        if (xmlWriter != null){
            xmlWriter.writeCharacters(xmlReader.getText());
        }
    }

    protected static void writeCData(XMLStreamReader xmlReader, XMLStreamWriter xmlWriter) throws XMLStreamException{
        if (xmlWriter != null){
            xmlWriter.writeCData(xmlReader.getText());
        }
    }
    protected static String limitTitle(String string){
        String temp = removeUnusedCharacters(string);
        if (temp != null && temp.length() > 255){
            return temp.substring(0,252) + "...";
        }else {
            return temp;
        }

    }

    protected static String removeUnusedCharacters(String input){
        if (input != null){
            String result = input.replaceAll("[\t ]+", " ");
            result = result.replaceAll("[\n\r]+", "");
            return result.trim();
        }else {
            return null;
        }

    }

    protected void writeEAD(XMLStreamWriter xmlWriter) throws XMLStreamException {
        if (xmlWriter != null) {
            xmlWriter.writeStartElement(EAD_ELEMENT.getPrefix(), EAD_ELEMENT.getLocalPart(), EAD_ELEMENT.getNamespaceURI());
            xmlWriter.writeDefaultNamespace(APENET_EAD);
            xmlWriter.writeNamespace("xlink", XLINK);
            xmlWriter.writeNamespace("xsi", XSI);
            xmlWriter.writeAttribute(XSI, "schemaLocation", "urn:isbn:1-931666-22-9 http://schemas.archivesportaleurope.net/profiles/apeEAD_XSD1.0.xsd http://www.w3.org/1999/xlink http://www.loc.gov/standards/xlink/xlink.xsd");
            xmlWriter.writeAttribute("audience", "external");
        }
    }

}
