package org.oclc.oai.harvester.parser;

import java.io.File;
import java.io.FileOutputStream;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

import org.codehaus.stax2.XMLOutputFactory2;

public class OaiPmhMetadataParser extends AbstractOaiPmhParser{
	
	public OaiPmhMetadataParser(File outputDirectory) {
		super(outputDirectory);
	}
	public String parse(XMLStreamReader xmlReader, String identifier)
			throws Exception {
		String filename = identifier.replaceAll("[^a-zA-Z0-9\\-\\.]", "_") +".xml";
		File file = new File(getOutputDirectory(),filename);
		if (file.exists()){
			file.delete();
		}
		FileOutputStream fileOutputStream = new FileOutputStream(file);
		XMLStreamWriter xmlWriter =  XMLOutputFactory2.newInstance().createXMLStreamWriter(fileOutputStream, UTF8);
		boolean foundEndElement = false;
		int event =  xmlReader.next();
		boolean firstElement = true;
		while(!foundEndElement && event != XMLStreamConstants.END_DOCUMENT){
			if (event == XMLStreamConstants.START_ELEMENT) {
				writeStartElement(xmlReader, xmlWriter);
				if (firstElement){

					firstElement = false;
				}
			} else if (event == XMLStreamConstants.CHARACTERS) {
                writeCharacters(xmlReader, xmlWriter);
            } else if (event == XMLStreamConstants.CDATA) {
                writeCData(xmlReader, xmlWriter);
            }else if (event == XMLStreamConstants.END_ELEMENT) {
				QName elementName = xmlReader.getName();
				if (METADATA.equals(elementName)) {
					foundEndElement = true;
				} else {
					writeEndElement(xmlReader, xmlWriter);
				}
			}
			if (!foundEndElement){
				event = xmlReader.next();
			}
		}
		xmlWriter.writeEndDocument();
		xmlWriter.flush();
		xmlWriter.close();
		return filename;
	}
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
			for (int i = 0; i < xmlReader.getNamespaceCount(); i++){
				String prefix = xmlReader.getNamespacePrefix(i);
				String namespaceURI = xmlReader.getNamespaceURI(i);
				xmlWriter.writeNamespace(prefix, namespaceURI);
				if (prefix.isEmpty()){
					xmlWriter.writeDefaultNamespace(namespaceURI);
				}
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
}
