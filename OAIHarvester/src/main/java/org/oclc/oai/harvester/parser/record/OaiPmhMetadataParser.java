package org.oclc.oai.harvester.parser.record;

import java.io.File;
import java.io.FileOutputStream;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamConstants;
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

}
