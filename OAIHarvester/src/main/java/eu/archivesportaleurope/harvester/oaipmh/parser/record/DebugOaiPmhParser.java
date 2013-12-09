package eu.archivesportaleurope.harvester.oaipmh.parser.record;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

import org.codehaus.stax2.XMLOutputFactory2;

public class DebugOaiPmhParser extends OaiPmhParser {


	public DebugOaiPmhParser(File outputDirectory) {
		super(outputDirectory);
	}

	@Override
	public ResultInfo parse(InputStream inputStream, int numberOfRequests) throws Exception {
		XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
		XMLStreamReader xmlStreamReader = xmlInputFactory.createXMLStreamReader(inputStream, UTF8);
		File file = new File(getOutputDirectory(),"oai-pmh-request-" + numberOfRequests+".xml");
		if (file.exists()){
			file.delete();
		}
		FileOutputStream fileOutputStream = new FileOutputStream(file);
		XMLStreamWriter xmlWriter =  XMLOutputFactory2.newInstance().createXMLStreamWriter(fileOutputStream, UTF8);
		ResultInfo resultInfo = new ResultInfo();
		QName lastElement = null;
		for (int event = xmlStreamReader.next(); event != XMLStreamConstants.END_DOCUMENT; event = xmlStreamReader
				.next()) {
			if (event == XMLStreamConstants.START_ELEMENT) {
				lastElement = xmlStreamReader.getName();
				writeStartElement(xmlStreamReader, xmlWriter);
			} else if (event == XMLStreamConstants.ATTRIBUTE) {
				if (ERROR.equals(lastElement)) {
					for (int i = 0; i < xmlStreamReader.getAttributeCount(); i++) {
						resultInfo.getErrors().add(
								xmlStreamReader.getAttributeLocalName(i) + ": " + xmlStreamReader.getAttributeValue(i)
										+ " - ");
					}
				}
			} else if (event == XMLStreamConstants.CHARACTERS || event == XMLStreamConstants.CDATA) {
				if (ERROR.equals(lastElement)) {
					resultInfo.getErrors().add(xmlStreamReader.getText());
				} else if (RESUMPTION_TOKEN.equals(lastElement)) {
					if (resultInfo.getNewResumptionToken() == null) {
                        resultInfo.setNewResumptionToken(xmlStreamReader.getText());
					}else {
                        resultInfo.setNewResumptionToken(resultInfo.getNewResumptionToken() + xmlStreamReader.getText());
					}
					
				}
				if (event == XMLStreamConstants.CHARACTERS) {
	                writeCharacters(xmlStreamReader, xmlWriter);
	            } else if (event == XMLStreamConstants.CDATA) {
	                writeCData(xmlStreamReader, xmlWriter);
	            }
			}else if (event == XMLStreamConstants.END_ELEMENT) {
				writeEndElement(xmlStreamReader, xmlWriter);
                if(RESUMPTION_TOKEN.equals(lastElement)) {
                    lastElement = null;
                }
			}
		}

		xmlWriter.writeEndDocument();
		xmlWriter.flush();
		xmlWriter.close();
		return resultInfo;
	}


}
