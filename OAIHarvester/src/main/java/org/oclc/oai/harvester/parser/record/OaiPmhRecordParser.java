package org.oclc.oai.harvester.parser.record;

import java.io.File;
import java.util.LinkedList;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;

public class OaiPmhRecordParser extends AbstractOaiPmhParser{
	
	public static final QName HEADER = new QName(OAI_PMH, "header");
	public static final QName IDENTIFIER = new QName(OAI_PMH, "identifier");
	public static final QName STATUS = new QName(OAI_PMH, "status");
	public OaiPmhRecordParser(File outputDirectory) {
		super(outputDirectory);
	}
	public OaiPmhRecord parse(XMLStreamReader xmlReader)
			throws Exception {
		LinkedList<QName> path = new LinkedList<QName>();
		OaiPmhRecord record = new OaiPmhRecord();
		boolean foundEndElement = false;
		int event =  xmlReader.next();
		QName lastElement = null;
		while(!foundEndElement && event != XMLStreamConstants.END_DOCUMENT){
			if (event == XMLStreamConstants.START_ELEMENT) {
				lastElement = xmlReader.getName();
				if (HEADER.equals(lastElement)) {
					record.setStatus(xmlReader.getAttributeValue(
							null, STATUS.getLocalPart()));
				}
				else if (METADATA.equals(lastElement)) {
					OaiPmhMetadataParser parser = new OaiPmhMetadataParser(getOutputDirectory());
					record.setFilename(parser.parse(xmlReader,record.getIdentifier()));
				}
				add(path, lastElement);
			}else if (event == XMLStreamConstants.CHARACTERS) {
				if (IDENTIFIER.equals(lastElement)) {
					boolean match = path.size() == 2
							&& HEADER.equals(path.get(0)) && IDENTIFIER.equals(path.get(1));
					if (match) {
						record.setIdentifier(xmlReader.getText());
					}
				}
			}
			else if (event == XMLStreamConstants.END_ELEMENT) {
				QName elementName = xmlReader.getName();
				if (RECORD.equals(elementName)) {
					foundEndElement = true;

				} else {
					removeLast(path, elementName);
					
				}
			}
			if (!foundEndElement){
				event = xmlReader.next();
			}
		}
		return record;
	}
	private static void add(LinkedList<QName> path, QName qName) {
		if (!RECORD.equals(qName)) {
			path.add(qName);
		}
	}

	private static void removeLast(LinkedList<QName> path, QName qName) {
		if (!RECORD.equals(qName)) {
			if (!path.isEmpty()) {
				path.removeLast();
			}
		}

	}
}
