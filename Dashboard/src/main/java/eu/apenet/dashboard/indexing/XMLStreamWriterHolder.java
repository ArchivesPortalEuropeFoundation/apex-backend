package eu.apenet.dashboard.indexing;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

import org.apache.log4j.Logger;

public class XMLStreamWriterHolder {
	private static final Logger LOGGER = Logger.getLogger(XMLStreamWriterHolder.class);
	private XMLStreamWriter xmlWriter;
	private int numberOfOpenedElements = 0;

	public XMLStreamWriterHolder(XMLStreamWriter xmlWriter) {
		this.xmlWriter = xmlWriter;
	}

	public void writeEAD() throws XMLStreamException {
		if (numberOfOpenedElements == 0) {
			xmlWriter.writeStartElement(AbstractParser.EAD_ELEMENT.getPrefix(),
					AbstractParser.EAD_ELEMENT.getLocalPart(), AbstractParser.EAD_ELEMENT.getNamespaceURI());
			xmlWriter.writeDefaultNamespace(AbstractParser.APENET_EAD);
			xmlWriter.writeNamespace("xlink", AbstractParser.XLINK);
			xmlWriter.writeNamespace("xsi", AbstractParser.XSI);
			numberOfOpenedElements++;
		}
	}

	public void writeCElement(XMLStreamReader xmlReader) throws XMLStreamException {
		if (numberOfOpenedElements == 0) {
			QName name = xmlReader.getName();
			xmlWriter.writeStartElement(name.getPrefix(), name.getLocalPart(), name.getNamespaceURI());
			xmlWriter.writeDefaultNamespace(AbstractParser.APENET_EAD);
			xmlWriter.writeNamespace("xlink", AbstractParser.XLINK);
			xmlWriter.writeNamespace("xsi", AbstractParser.XSI);
			for (int i = 0; i < xmlReader.getAttributeCount(); i++) {
				xmlWriter.writeAttribute(xmlReader.getAttributePrefix(i), xmlReader.getAttributeNamespace(i),
						xmlReader.getAttributeLocalName(i), xmlReader.getAttributeValue(i));
			}
		}
	}

	public void closeElement() throws XMLStreamException {
		if (numberOfOpenedElements > 0) {
			xmlWriter.writeEndElement();
			numberOfOpenedElements--;
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

	protected void close() throws XMLStreamException {
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
