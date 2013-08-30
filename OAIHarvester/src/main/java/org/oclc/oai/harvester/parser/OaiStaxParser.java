package org.oclc.oai.harvester.parser;

import org.apache.commons.io.IOUtils;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * User: Yoann Moranville
 * Date: 06/08/2013
 *
 * @author Yoann Moranville
 */
public class OaiStaxParser {
    private XMLStreamReader xmlStreamReader;

    private List<String> errors;
    private boolean inError = false;
    private String listSize;
    private String resumptionToken = "";
    private boolean inResumptionToken;

    public OaiStaxParser(InputStream inputStream) throws XMLStreamException {
        XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
        xmlStreamReader = xmlInputFactory.createXMLStreamReader(inputStream);

        parse();
    }

    private void parse() throws XMLStreamException {
        int event;
        while (xmlStreamReader.hasNext()) {
            event = xmlStreamReader.next();
            if (event == XMLStreamConstants.START_ELEMENT) {
                QName elementName = xmlStreamReader.getName();
                if(elementName.getLocalPart().equals("error")) {
                    inError = true;
                    if(errors == null)
                        errors = new ArrayList<String>();
                } else if (elementName.getLocalPart().equals("resumptionToken")) {
                    inResumptionToken = true;
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                QName elementName = xmlStreamReader.getName();
                if(inError && elementName.getLocalPart().equals("error")) {
                    inError = false;
                } else if (inResumptionToken && elementName.getLocalPart().equals("resumptionToken")) {
                    inResumptionToken = false;
                }
            } else if (event == XMLStreamConstants.ATTRIBUTE) {
                if(inError) {
                    for(int i = 0; i < xmlStreamReader.getAttributeCount(); i++) {
                        errors.add(xmlStreamReader.getAttributeLocalName(i) + ": " + xmlStreamReader.getAttributeValue(i) + " - ");
                    }
                } else if (inResumptionToken) {
                    for(int i = 0; i < xmlStreamReader.getAttributeCount(); i++) {
                        if(xmlStreamReader.getAttributeLocalName(i).equals("completeListSize")) {
                            listSize = xmlStreamReader.getAttributeValue(i);
                        }
                    }
                }
            } else if (event == XMLStreamConstants.CHARACTERS || event == XMLStreamConstants.CDATA) {
                if(inError) {
                    errors.add(xmlStreamReader.getText());
                } else if (inResumptionToken) {
                    resumptionToken += xmlStreamReader.getText();
                }
            }
        }
    }

    public List<String> getErrors() {
        return errors;
    }

    public String getResumptionToken() {
        return resumptionToken;
    }
}
