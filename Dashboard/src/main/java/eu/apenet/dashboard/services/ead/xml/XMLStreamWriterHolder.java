package eu.apenet.dashboard.services.ead.xml;

import eu.archivesportaleurope.xml.ApeXMLConstants;
import org.apache.commons.lang.StringUtils;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

public class XMLStreamWriterHolder {
	private XMLStreamWriter xmlWriter;
	private int numberOfOpenedElements = 0;

	public XMLStreamWriterHolder(XMLStreamWriter xmlWriter) {
		this.xmlWriter = xmlWriter;
	}

	public void writeEAD(QName name) throws XMLStreamException {
		if (numberOfOpenedElements == 0) {
			xmlWriter.writeStartElement(name.getPrefix(), name.getLocalPart(), name.getNamespaceURI());
            if(StringUtils.isNotEmpty(name.getPrefix())) {
                xmlWriter.writeNamespace(name.getPrefix(), ApeXMLConstants.APE_EAD_NAMESPACE);
            } else {
                xmlWriter.writeDefaultNamespace(ApeXMLConstants.APE_EAD_NAMESPACE);
            }
			xmlWriter.writeNamespace(ApeXMLConstants.XLINK_PREFIX, ApeXMLConstants.XLINK_NAMESPACE);
			xmlWriter.writeNamespace(ApeXMLConstants.XSI_PREFIX, ApeXMLConstants.XSI_NAMESPACE);
			numberOfOpenedElements++;
		}
	}

	public void writeCElement(XMLStreamReader xmlReader) throws XMLStreamException {
		if (numberOfOpenedElements == 0) {
			QName name = xmlReader.getName();
			xmlWriter.writeStartElement(name.getPrefix(), name.getLocalPart(), name.getNamespaceURI());
            if(StringUtils.isNotEmpty(name.getPrefix())) {
                xmlWriter.writeNamespace(name.getPrefix(), ApeXMLConstants.APE_EAD_NAMESPACE);
            } else {
                xmlWriter.writeDefaultNamespace(ApeXMLConstants.APE_EAD_NAMESPACE);
            }
            xmlWriter.writeNamespace(ApeXMLConstants.XLINK_PREFIX, ApeXMLConstants.XLINK_NAMESPACE);
            xmlWriter.writeNamespace(ApeXMLConstants.XSI_PREFIX, ApeXMLConstants.XSI_NAMESPACE);
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

	public void writeStartElement(XMLStreamReader xmlReader) throws XMLStreamException {
		QName element = xmlReader.getName();
		if (xmlWriter != null) {
			xmlWriter.writeStartElement(element.getPrefix(), element.getLocalPart(), element.getNamespaceURI());
			for (int i = 0; i < xmlReader.getAttributeCount(); i++) {
				xmlWriter.writeAttribute(xmlReader.getAttributePrefix(i), xmlReader.getAttributeNamespace(i),
						xmlReader.getAttributeLocalName(i), xmlReader.getAttributeValue(i));
			}
			numberOfOpenedElements++;
		}
	}

	public void writeCharacters(XMLStreamReader xmlReader) throws XMLStreamException {
		if (xmlWriter != null) {
			xmlWriter.writeCharacters(xmlReader.getText());
		}
	}

	public void writeCData(XMLStreamReader xmlReader) throws XMLStreamException {
		if (xmlWriter != null) {
			xmlWriter.writeCData(xmlReader.getText());
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
