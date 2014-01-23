package eu.archivesportaleurope.harvester.oaipmh.parser.record;

import java.io.File;
import java.io.InputStream;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;

public class OaiPmhParser extends AbstractOaiPmhParser {

	private Integer maxNumberOfRequests;
	public OaiPmhParser(File outputDirectory, Integer maxNumberOfRequests) {
		super(outputDirectory);
		this.maxNumberOfRequests = maxNumberOfRequests;
	}
	public OaiPmhParser(File outputDirectory) {
		super(outputDirectory);
	}
	public ResultInfo parse(InputStream inputStream, int numberOfRequests) throws Exception {
		XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
		XMLStreamReader xmlStreamReader = xmlInputFactory.createXMLStreamReader(inputStream, UTF8);
		OaiPmhRecordParser oaiPmhRecordParser = new OaiPmhRecordParser(getOutputDirectory());
		ResultInfo resultInfo = new ResultInfo();
		QName lastElement = null;
		boolean noRecordsMatch = false;
		for (int event = xmlStreamReader.next(); event != XMLStreamConstants.END_DOCUMENT; event = xmlStreamReader
				.next()) {
			if (event == XMLStreamConstants.START_ELEMENT) {
				lastElement = xmlStreamReader.getName();
				if (RECORD.equals(lastElement)) {
					resultInfo.getRecords().add(oaiPmhRecordParser.parse(xmlStreamReader));
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
					// for(int i = 0; i < xmlStreamReader.getAttributeCount();
					// i++) {
					// if(xmlStreamReader.getAttributeLocalName(i).equals("completeListSize"))
					// {
					// listSize = xmlStreamReader.getAttributeValue(i);
					// }
					// }
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
	public Integer getMaxNumberOfRequests() {
		return maxNumberOfRequests;
	}


}
