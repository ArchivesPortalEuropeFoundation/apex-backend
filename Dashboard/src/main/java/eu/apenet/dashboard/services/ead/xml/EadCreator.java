package eu.apenet.dashboard.services.ead.xml;

import java.io.IOException;
import java.io.OutputStream;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

/**
 * User: Yoann Moranville
 * Date: 3/24/11
 *
 * @author Yoann Moranville
 */
public class EadCreator extends AbstractParser {
	private static final String UTF8 = "UTF-8";
    private static final QName DSC_ELEMENT = new QName(APENET_EAD, "dsc");
    private static final QName ARCHDESC_ELEMENT = new QName(APENET_EAD, "archdesc");
    private static final Logger LOG = Logger.getLogger(EadCreator.class);

    private XMLStreamWriter xmlWriter;
    private XMLInputFactory inputFactory;

    public EadCreator(OutputStream outputStream) throws XMLStreamException {
        xmlWriter = XMLOutputFactory.newInstance().createXMLStreamWriter(outputStream, UTF8);
        inputFactory = XMLInputFactory.newInstance();
    }

    public void writeEadContent(String xml) throws IOException, XMLStreamException {
        writeEadContent(xml, null);
    }

    public void writeEadContent(String xml, String level) throws IOException, XMLStreamException {
//        LOG.info("XML: " + xml);
        XMLStreamReader xmlReader = inputFactory.createXMLStreamReader(IOUtils.toInputStream(xml, UTF8), UTF8);

        for (int event = xmlReader.next(); event != XMLStreamConstants.END_DOCUMENT; event = xmlReader.next()) {
            if (event == XMLStreamConstants.START_ELEMENT) {
                if(xmlReader.getName().equals(EAD_ELEMENT)) {
                    writeEAD(xmlWriter);
                } else {
                    writeStartElement(xmlReader, xmlWriter);
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                QName name = xmlReader.getName();
                if(!name.equals(EAD_ELEMENT) && !name.equals(ARCHDESC_ELEMENT) && !name.equals(DSC_ELEMENT) && !name.equals(C_ELEMENT))// && !name.equals(C_ELEMENT_NO_NS))
                    writeEndElement(xmlReader, xmlWriter);
            } else if (event == XMLStreamConstants.CHARACTERS) {
                writeCharacters(xmlReader, xmlWriter);
            } else if (event == XMLStreamConstants.CDATA) {
                writeCData(xmlReader, xmlWriter);
            }
        }
    }

    public void closeEndTags() throws XMLStreamException {
        if(xmlWriter != null) {
            xmlWriter.writeEndElement(); //DSC_ELEMENT
            xmlWriter.writeEndElement(); //ARCHDESC_ELEMENT
            // In some cases, only is needed to close two elements, so we try
            // to close the third (with the corresponding catch).
            try {
            	xmlWriter.writeEndElement(); //EAD_ELEMENT
            } catch (XMLStreamException xmlse) {
            	if (!xmlse.getMessage().startsWith("No open start element")) {
            		LOG.error("Error while closing elements.");
            		throw xmlse;
            	}
            }
        }
    }

    public void closeCTag() throws XMLStreamException {
        if(xmlWriter != null)
            xmlWriter.writeEndElement(); //C_ELEMENT
    }

    public void closeWriter() throws XMLStreamException {
        if(xmlWriter != null){
            xmlWriter.flush();
            xmlWriter.close();
        }
    }

}
