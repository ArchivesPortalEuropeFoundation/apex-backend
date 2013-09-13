package org.oclc.oai.harvester.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;

public class OaiPmhParser extends AbstractOaiPmhParser {

	private static final QName RESUMPTION_TOKEN = new QName(OAI_PMH, "resumptionToken");
	private static final QName ERROR = new QName(OAI_PMH, "error");

	public OaiPmhParser(File outputDirectory) {
		super(outputDirectory);
	}

	public ResultInfo parse(InputStream inputStream) throws Exception {
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
					resultInfo.setNewResumptionToken(xmlStreamReader.getText());
				}
			}
		}
		return resultInfo;
	}


}
