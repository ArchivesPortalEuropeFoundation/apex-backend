package eu.apenet.oaiserver.response;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

import org.apache.log4j.Logger;

public class XMLStreamWriterHolder {
    public static final String SCHEMA_LOCATION = "schemaLocation";
	public static final String XMLNS = "xmlns";
    public static final String XSI_NAMESPACE = "http://www.w3.org/2001/XMLSchema-instance";
    public static final String XSI_PREFIX = "xsi";
    public static final String OAI_PMH_NAMESPACE = "http://www.openarchives.org/OAI/2.0/";
	private static final Logger LOGGER = Logger.getLogger(XMLStreamWriterHolder.class);
	private XMLStreamWriter xmlWriter;
	private int numberOfOpenedElements = 0;

	public XMLStreamWriterHolder(XMLStreamWriter xmlWriter) {
		this.xmlWriter = xmlWriter;
	}

	public void writeOaiPmh() throws XMLStreamException {
		if (numberOfOpenedElements == 0) {
			xmlWriter.writeStartDocument("UTF-8", "1.0");
			xmlWriter.writeStartElement("",
					"OAI-PMH", OAI_PMH_NAMESPACE);
			xmlWriter.writeDefaultNamespace(OAI_PMH_NAMESPACE);
			//
			xmlWriter.writeNamespace(XSI_PREFIX, XSI_NAMESPACE);
			xmlWriter.writeAttribute(XSI_NAMESPACE, SCHEMA_LOCATION, OAI_PMH_NAMESPACE + " http://www.openarchives.org/OAI/2.0/OAI-PMH.xsd");
			numberOfOpenedElements++;
		}
	}


	public void closeElement() throws XMLStreamException {
		if (numberOfOpenedElements > 0) {
			xmlWriter.writeEndElement();
			numberOfOpenedElements--;
		}
	}
	protected void writeTextElement(String localName, String value) throws XMLStreamException {
		if (xmlWriter == null) {
			LOGGER.error("Try to write element: "  + localName + ", but writer is already closed.");
		} else {
			xmlWriter.writeStartElement(localName);
			numberOfOpenedElements++;
			xmlWriter.writeCharacters(value);
			closeElement();
		}
	}
	protected void writeTextElementNS(String prefix, String localName, String namespaceURI, String value) throws XMLStreamException {
		if (xmlWriter == null) {
			LOGGER.error("Try to write element: "  + localName + ", but writer is already closed.");
		} else {
			xmlWriter.writeStartElement(prefix, localName, namespaceURI);
			numberOfOpenedElements++;
			xmlWriter.writeCharacters(value);
			closeElement();
		}
	}
	protected void writeTextElementWithAttribute(String localName, String value, String localAttribute, String attributeValue) throws XMLStreamException {
		if (xmlWriter == null) {
			LOGGER.error("Try to write element: "  + localName + ", but writer is already closed.");
		} else {
			xmlWriter.writeStartElement(localName);
			numberOfOpenedElements++;
			xmlWriter.writeAttribute(localAttribute, attributeValue);
			xmlWriter.writeCharacters(value);
			closeElement();
		}
	}
	protected void writeStartElement(String localName) throws XMLStreamException {
		if (xmlWriter == null) {
			LOGGER.error("Try to write element: "  + localName + ", but writer is already closed.");
		} else {
			xmlWriter.writeStartElement(localName);
			numberOfOpenedElements++;
		}
	}
	
	protected void writeStartElementWithNewDefaultNamespace(String localName, String namespace, String schemaLocation) throws XMLStreamException {
		if (xmlWriter == null) {
			LOGGER.error("Try to write element: "  + localName + ", but writer is already closed.");
		} else {
			xmlWriter.writeStartElement(localName);
			xmlWriter.writeAttribute(XMLNS, namespace);
			xmlWriter.writeAttribute(XSI_NAMESPACE, SCHEMA_LOCATION, namespace + " "  + schemaLocation);
			numberOfOpenedElements++;
		}
	}
	protected void writeStartElementNS(String prefix, String localName, String namespaceURI) throws XMLStreamException {
		if (xmlWriter == null) {
			LOGGER.error("Try to write element: "  + localName + ", but writer is already closed.");
		} else {
			xmlWriter.writeStartElement(prefix, localName, namespaceURI);
			numberOfOpenedElements++;
		}
	}

	protected void writeAttribute(String localName, String value) throws XMLStreamException {
		if (xmlWriter == null) {
			LOGGER.error("Try to write attribute: "  + localName + ", but writer is already closed.");
		} else {
			xmlWriter.writeAttribute(localName, value);
		}
	}
	protected void writeAttributeNS(String prefix, String localName, String namespaceURI, String value) throws XMLStreamException {
		if (xmlWriter == null) {
			LOGGER.error("Try to write attribute: "  + localName + ", but writer is already closed.");
		} else {
			xmlWriter.writeAttribute(prefix, namespaceURI, localName, value);
		}
	}
	protected void writeStartElement(XMLStreamReader xmlReader) throws XMLStreamException {
		QName element = xmlReader.getName();
		if (xmlWriter == null) {
			LOGGER.error("Try to write element: "  + element.getPrefix() + ":" + element.getLocalPart() + ", but writer is already closed.");
		} else {
			xmlWriter.writeStartElement(element.getPrefix(), element.getLocalPart(), element.getNamespaceURI());
			for (int i = 0; i < xmlReader.getAttributeCount(); i++) {
				xmlWriter.writeAttribute(xmlReader.getAttributePrefix(i), xmlReader.getAttributeNamespace(i),
						xmlReader.getAttributeLocalName(i), xmlReader.getAttributeValue(i));
			}
			numberOfOpenedElements++;
		}
	}

	protected void writeCharacters(XMLStreamReader xmlReader) throws XMLStreamException {
		if (xmlWriter != null) {
			xmlWriter.writeCharacters(xmlReader.getText());
		}
	}

	protected void writeCData(XMLStreamReader xmlReader) throws XMLStreamException {
		if (xmlWriter != null) {
			xmlWriter.writeCData(xmlReader.getText());
		}
	}

	protected void writeCharacters(String text) throws XMLStreamException {
		if (xmlWriter != null) {
			xmlWriter.writeCharacters(text);
		}
	}

	protected void writeCData(String text) throws XMLStreamException {
		if (xmlWriter != null) {
			xmlWriter.writeCData(text);
		}
	}

	public void close() throws XMLStreamException {
		if (xmlWriter != null) {
			while (numberOfOpenedElements > 0) {
				closeElement();
			}
			xmlWriter.flush();
			xmlWriter.close();
			xmlWriter = null;
		}
	}

	@Override
	public String toString() {
		if (xmlWriter == null){
			return "XMLStreamWriterHolder [xmlWriter=null]";
		}else {
			return "XMLStreamWriterHolder [xmlWriter=" + xmlWriter.getClass() + "]";
		}
		
	}
	
}
