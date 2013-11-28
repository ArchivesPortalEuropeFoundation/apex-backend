package org.oclc.oai.harvester.parser.record;

import java.io.File;
import java.io.InputStream;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;

public class OaiPmhParser extends AbstractOaiPmhParser {



	public OaiPmhParser(File outputDirectory) {
		super(outputDirectory);
	}

	public ResultInfo parse(InputStream inputStream, int numberOfRequests) throws Exception {
		XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
		XMLStreamReader xmlStreamReader = xmlInputFactory.createXMLStreamReader(inputStream, UTF8);
		OaiPmhRecordParser oaiPmhRecordParser = new OaiPmhRecordParser(getOutputDirectory());
		ResultInfo resultInfo = new ResultInfo();
		QName lastElement = null;
		for (int event = xmlStreamReader.next(); event != XMLStreamConstants.END_DOCUMENT; event = xmlStreamReader
				.next()) {
			if (event == XMLStreamConstants.START_ELEMENT) {
				lastElement = xmlStreamReader.getName();
				if (RECORD.equals(lastElement)) {
					resultInfo.getRecords().add(oaiPmhRecordParser.parse(xmlStreamReader));
				}
			} else if (event == XMLStreamConstants.ATTRIBUTE) {
				if (ERROR.equals(lastElement)) {
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
				if (ERROR.equals(lastElement)) {
					resultInfo.getErrors().add(xmlStreamReader.getText());
				} else if (RESUMPTION_TOKEN.equals(lastElement)) {
					if (resultInfo.getNewResumptionToken() == null){
                        if(xmlStreamReader.getText().trim().length() > 0)
						    resultInfo.setNewResumptionToken(xmlStreamReader.getText());
					}else {
                        if(xmlStreamReader.getText().trim().length() > 0)
						    resultInfo.setNewResumptionToken(resultInfo.getNewResumptionToken() + xmlStreamReader.getText());
					}
				}
			}
		}
		return resultInfo;
	}


}
