package eu.archivesportaleurope.harvester.oaipmh.parser.record;

import java.io.File;
import java.util.Calendar;
import java.util.LinkedList;

import javax.xml.bind.DatatypeConverter;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;

public class OaiPmhRecordParser extends AbstractOaiPmhParser{
	
	
	protected static final QName IDENTIFIER = new QName(OAI_PMH, "identifier");
	protected static final QName DATESTAMP = new QName(OAI_PMH, "datestamp");
	protected static final QName STATUS = new QName(OAI_PMH, "status");
	public OaiPmhRecordParser(File outputDirectory) {
		super(outputDirectory);
	}
	public OaiPmhRecord parse(XMLStreamReader xmlReader, QName rootElement, Calendar fromCalendar, Calendar untilCalendar)
			throws Exception {
		LinkedList<QName> path = new LinkedList<QName>();
		OaiPmhRecord record = new OaiPmhRecord();
		QName lastElement = null;
		if (HEADER.equals(rootElement)){
			record.setStatus(xmlReader.getAttributeValue(null, STATUS.getLocalPart()));	
			add(path, rootElement);
		}
		boolean foundEndElement = false;
		int event =  xmlReader.next();
		while(!foundEndElement && event != XMLStreamConstants.END_DOCUMENT){
			if (event == XMLStreamConstants.START_ELEMENT) {
				lastElement = xmlReader.getName();
				if (HEADER.equals(lastElement)) {
					record.setStatus(xmlReader.getAttributeValue(
							null, STATUS.getLocalPart()));
				}
				else if (METADATA.equals(lastElement)) {
					if (!record.isDropped()){
						OaiPmhMetadataParser parser = new OaiPmhMetadataParser(getOutputDirectory());
						record.setFilename(parser.parse(xmlReader,record.getFilenameFromIdentifier()));
					}
				}
				add(path, lastElement);
			}else if (event == XMLStreamConstants.CHARACTERS) {
                if (IDENTIFIER.equals(lastElement)) {
                    boolean match = path.size() == 2
                            && HEADER.equals(path.get(0)) && IDENTIFIER.equals(path.get(1));
                    if (match) {
                        record.setIdentifier(xmlReader.getText());
                    }
                } else if (DATESTAMP.equals(lastElement)) {
                    boolean match = path.size() == 2
                            && HEADER.equals(path.get(0)) && DATESTAMP.equals(path.get(1));
                    if (match) {
                        try {
                        	Calendar current = DatatypeConverter.parseDateTime(xmlReader.getText());
                            record.setTimestamp(current.getTime());
                            record.setDropped(shouldDropped(current, fromCalendar, untilCalendar));
                        } catch (Exception e) {}
                    }
                }
			}
			else if (event == XMLStreamConstants.END_ELEMENT) {
				QName elementName = xmlReader.getName();
				if (rootElement.equals(elementName)) {
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
	private boolean shouldDropped(Calendar current, Calendar fromCalendar, Calendar untilCalendar){
		boolean withinTimespan = true;
		withinTimespan = withinTimespan && (fromCalendar == null || current.compareTo(fromCalendar) >= 0);
		withinTimespan = withinTimespan && (untilCalendar == null || current.compareTo(untilCalendar) <= 0);
		return !withinTimespan;
		
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
