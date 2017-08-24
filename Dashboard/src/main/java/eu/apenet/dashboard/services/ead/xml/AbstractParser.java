package eu.apenet.dashboard.services.ead.xml;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

import eu.archivesportaleurope.xml.ApeXMLConstants;

public abstract class AbstractParser  {


	@Deprecated
	public static final String APENET_EAD = ApeXMLConstants.APE_EAD_NAMESPACE;
	@Deprecated
	public static final String XLINK = ApeXMLConstants.XLINK_NAMESPACE;
	@Deprecated
	public static final String XSI = ApeXMLConstants.XSI_NAMESPACE;

    protected static final QName EAD_ELEMENT = new QName(ApeXMLConstants.APE_EAD_NAMESPACE, "ead");
    protected static final QName C_ELEMENT = new QName(ApeXMLConstants.APE_EAD_NAMESPACE, "c");


	protected static void writeEndElement(XMLStreamReader xmlReader, XMLStreamWriter xmlWriter) throws XMLStreamException{
		if (xmlWriter != null){
			xmlWriter.writeEndElement();
		}
	}

	protected static void writeStartElement(XMLStreamReader xmlReader, XMLStreamWriter xmlWriter) throws XMLStreamException{
		if (xmlWriter != null){
			QName element = xmlReader.getName();
			xmlWriter.writeStartElement(element.getPrefix(), element.getLocalPart(), element.getNamespaceURI());
			for (int i=0; i < xmlReader.getAttributeCount(); i++){
				xmlWriter.writeAttribute(xmlReader.getAttributePrefix(i), xmlReader.getAttributeNamespace(i), xmlReader.getAttributeLocalName(i), xmlReader.getAttributeValue(i));
			}
			for (int i = 0; i < xmlReader.getNamespaceCount(); i++){
				String prefix = xmlReader.getNamespacePrefix(i);
				String namespaceURI = xmlReader.getNamespaceURI(i);
				
				if ( null != prefix && prefix.isEmpty()){
					xmlWriter.writeDefaultNamespace(namespaceURI);
				}else {
					xmlWriter.writeNamespace(prefix, namespaceURI);
				}
			}
		}		
	}	

	protected static void writeCharacters(XMLStreamReader xmlReader, XMLStreamWriter xmlWriter) throws XMLStreamException{
		if (xmlWriter != null){
			xmlWriter.writeCharacters(xmlReader.getText());
		}
	}

	protected static void writeCData(XMLStreamReader xmlReader, XMLStreamWriter xmlWriter) throws XMLStreamException{
		if (xmlWriter != null){
			xmlWriter.writeCData(xmlReader.getText());
		}
	}
	protected static String limitTitle(String string){
		String temp = removeUnusedCharacters(string);
		if (temp != null && temp.length() > 255){
			return temp.substring(0,252) + "...";
		}else {
			return temp;
		}

	}

	protected static String removeUnusedCharacters(String input){
		if (input != null){
			String result = input.replaceAll("[\t ]+", " ");
			result = result.replaceAll("[\n\r]+", "");
			return result.trim();
		}else {
			return null;
		}

	}

    protected final void writeEAD(XMLStreamWriter xmlWriter) throws XMLStreamException {
		if (xmlWriter != null) {
			xmlWriter.writeStartElement(EAD_ELEMENT.getPrefix(), EAD_ELEMENT.getLocalPart(), EAD_ELEMENT.getNamespaceURI());
			xmlWriter.writeDefaultNamespace(ApeXMLConstants.APE_EAD_NAMESPACE);
			xmlWriter.writeNamespace(ApeXMLConstants.XLINK_PREFIX, ApeXMLConstants.XLINK_NAMESPACE);
			xmlWriter.writeNamespace(ApeXMLConstants.XSI_PREFIX, ApeXMLConstants.XSI_NAMESPACE);
            xmlWriter.writeAttribute(ApeXMLConstants.XSI_NAMESPACE, ApeXMLConstants.SCHEMA_LOCATION, ApeXMLConstants.APE_EAD_NAMESPACE + " " + ApeXMLConstants.APE_EAD_LOCATION + " " +   ApeXMLConstants.XLINK_NAMESPACE + " " +  ApeXMLConstants.XLINK_LOCATION);
            xmlWriter.writeAttribute("audience", "external");
		}
	}

}
