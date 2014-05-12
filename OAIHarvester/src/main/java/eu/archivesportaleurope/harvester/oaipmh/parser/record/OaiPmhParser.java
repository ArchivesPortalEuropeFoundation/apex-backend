package eu.archivesportaleurope.harvester.oaipmh.parser.record;

import java.io.File;
import java.io.InputStream;
import java.util.Calendar;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;

import org.apache.log4j.Logger;

import eu.archivesportaleurope.harvester.oaipmh.HarvestObject;
import eu.archivesportaleurope.harvester.oaipmh.OaiPmhHarvester;
import eu.archivesportaleurope.harvester.util.StreamUtil;

public class OaiPmhParser extends AbstractOaiPmhParser {
	private static final Logger LOGGER = Logger.getLogger(OaiPmhHarvester.LOGGER_STRING);
	private Integer maxNumberOfRecords;
	public OaiPmhParser(File outputDirectory, Integer maxNumberOfRecords) {
		super(outputDirectory);
		this.maxNumberOfRecords = maxNumberOfRecords;
	}
	public OaiPmhParser(File outputDirectory) {
		super(outputDirectory);
	}
	public ResultInfo parse(HarvestObject harvestObject, InputStream inputStream, int numberOfRequests, Calendar fromCalendar, Calendar untilCalendar) throws Exception {
		XMLStreamReader xmlStreamReader = StreamUtil.getXMLStreamReader(inputStream);
		OaiPmhRecordParser oaiPmhRecordParser = new OaiPmhRecordParser(getOutputDirectory());
		ResultInfo resultInfo = new ResultInfo();
		QName lastElement = null;
		boolean noRecordsMatch = false;
		for (int event = xmlStreamReader.next(); event != XMLStreamConstants.END_DOCUMENT; event = xmlStreamReader
				.next()) {
			if (event == XMLStreamConstants.START_ELEMENT) {
				lastElement = xmlStreamReader.getName();
				if (RECORD.equals(lastElement)) {
					addOrUpdateRecord(harvestObject, oaiPmhRecordParser.parse(xmlStreamReader,RECORD, fromCalendar, untilCalendar));
				}else if (HEADER.equals(lastElement)) {
					addOrUpdateRecord(harvestObject, oaiPmhRecordParser.parse(xmlStreamReader,HEADER, fromCalendar, untilCalendar));
				}else if (ERROR.equals(lastElement)) {
					for (int i = 0; i < xmlStreamReader.getAttributeCount(); i++) {
						if ("noRecordsMatch".equalsIgnoreCase(xmlStreamReader.getAttributeValue(i)) && "code".equalsIgnoreCase(xmlStreamReader.getAttributeLocalName(i))){
							noRecordsMatch = true;
						}
					}
				}
			} else if (event == XMLStreamConstants.ATTRIBUTE) {
				if (!noRecordsMatch && ERROR.equals(lastElement)) {
					for (int i = 0; i < xmlStreamReader.getAttributeCount(); i++) {
						resultInfo.getErrors().add(
								xmlStreamReader.getAttributeLocalName(i) + ": " + xmlStreamReader.getAttributeValue(i)
										+ " - ");
					}
				} else if (RESUMPTION_TOKEN.equals(lastElement)) {
				}
			} else if (event == XMLStreamConstants.CHARACTERS || event == XMLStreamConstants.CDATA) {
				if (!noRecordsMatch && ERROR.equals(lastElement)) {
					resultInfo.getErrors().add(xmlStreamReader.getText());
				} else if (RESUMPTION_TOKEN.equals(lastElement)) {
					if (resultInfo.getNewResumptionToken() == null){
                        resultInfo.setNewResumptionToken(xmlStreamReader.getText());
					}else {
                        resultInfo.setNewResumptionToken(resultInfo.getNewResumptionToken() + xmlStreamReader.getText());
					}
				}
			} else if (event == XMLStreamConstants.END_ELEMENT) {
                if (RESUMPTION_TOKEN.equals(lastElement)) {
                    lastElement = null;
                }
            }
		}
		xmlStreamReader.close();
		return resultInfo;
	}
	public Integer getMaxNumberOfRecords() {
		return maxNumberOfRecords;
	}
	public void addOrUpdateRecord(HarvestObject harvestObject, OaiPmhRecord record){
		if (!harvestObject.isGetRecordPhase()){
			harvestObject.increaseNumberOfRecords();
			harvestObject.setLatestRecordId(record.getIdentifier());
			String action = null;
			if (record.isDropped()){
				action = "i";
			}else if (record.isDeleted()){
				action = "d";
			}else {
				action = "u";
			}
			if (record.getFilename() == null){
				LOGGER.info("("+harvestObject.getNumberOfRequests()+"," + harvestObject.getNumberOfRecords() +"): LI(" + action + "): " + record );
			}else {
				LOGGER.info("("+harvestObject.getNumberOfRequests()+"," + harvestObject.getNumberOfRecords() +"): LR(" + action + "): " + record );
			}
			harvestObject.add(record);			
		}
	}
}
