package eu.apenet.dashboard.manual;

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.stax2.XMLInputFactory2;
import org.codehaus.stax2.XMLOutputFactory2;
import org.codehaus.stax2.XMLStreamReader2;
import org.codehaus.stax2.XMLStreamWriter2;

import eu.apenet.dashboard.actions.ajax.EditEadAction;
import eu.apenet.dashboard.services.ead.xml.AbstractParser;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.CLevel;
import eu.apenet.persistence.vo.EadContent;

/**
 * User: Yoann Moranville
 * Date: 06/05/2011
 *
 * @author Yoann Moranville
 */
public class EditParser extends AbstractParser {
    private static final Logger LOG = Logger.getLogger(EditParser.class);
    public final QName C_ELEMENT = new QName(APENET_EAD, "c");
    public final QName EAD_ELEMENT = new QName(APENET_EAD, "ead");
    private static final String UTF8 = "UTF-8";

    public String xmlToHtml(CLevel cLevel, EadContent eadContent) throws XMLStreamException, IOException {
        int counterDiv = 0;
        String xml = "";
        if (cLevel != null)
            xml = cLevel.getXml();
        else
            xml = eadContent.getXml();

        StringWriter stringWriter = new StringWriter();
        XMLStreamWriter2 xmlWriter = ((XMLOutputFactory2) XMLOutputFactory2.newInstance()).createXMLStreamWriter(stringWriter, UTF8);
        XMLStreamReader2 xmlReader = (XMLStreamReader2) ((XMLInputFactory2) XMLInputFactory2.newInstance()).createXMLStreamReader(IOUtils.toInputStream(xml, UTF8), UTF8);

        String lastElementName = null;
        for (int event = xmlReader.next(); event != XMLStreamConstants.END_DOCUMENT; event = xmlReader.next()) {
            if (event == XMLStreamConstants.START_ELEMENT) {

                lastElementName = xmlReader.getLocalName();

                if (EditEadAction.UndisplayableFields.isDisplayable(lastElementName)) {
                    LOG.debug("Open div for '" + lastElementName + "'");
                    counterDiv++;
                    xmlWriter.writeStartElement("div");
                    xmlWriter.writeAttribute("class", "editionElement");
                    xmlWriter.writeCharacters(xmlReader.getLocalName() + ": ");
                    xmlWriter.writeEmptyElement("br");
                    for (int i = 0; i < xmlReader.getAttributeCount(); i++) {
                        if (EditEadAction.UndisplayableFields.isDisplayable(xmlReader.getAttributeLocalName(i))) {
                            if (EditEadAction.EditableFields.isEditable(lastElementName + "_" + xmlReader.getAttributeLocalName(i))) {
                                xmlWriter.writeStartElement("span");
                                xmlWriter.writeCharacters("@" + xmlReader.getAttributeLocalName(i) + ": ");

                                writeCorrectInput(xmlWriter, lastElementName, xmlReader.getAttributeLocalName(i), xmlReader.getAttributeValue(i), cLevel);

                                xmlWriter.writeEndElement();
                            } else {
                                xmlWriter.writeStartElement("span");
                                xmlWriter.writeCharacters("@" + xmlReader.getAttributeLocalName(i) + ": " + xmlReader.getAttributeValue(i));
                                xmlWriter.writeEndElement();
                            }
                            xmlWriter.writeEmptyElement("br");
                        }
                    }
                }

            } else if (event == XMLStreamConstants.END_ELEMENT) {
                if (counterDiv > 1) {
                    LOG.debug("Close div for '" + lastElementName + "'");
                    counterDiv--;
                    xmlWriter.writeEndElement();
                }
                lastElementName = null;
            } else if (event == XMLStreamConstants.CHARACTERS) {
                if (!StringUtils.isBlank(xmlReader.getText()) && EditEadAction.UndisplayableFields.isDisplayable(lastElementName)) {
                    if (lastElementName != null && EditEadAction.EditableFields.isEditable(lastElementName)) {
                        xmlWriter.writeCharacters("#text: ");
                        xmlWriter.writeEmptyElement("input");
                        xmlWriter.writeAttribute("type", "text");
                        xmlWriter.writeAttribute("name", lastElementName);
                        xmlWriter.writeAttribute("value", xmlReader.getText());
                    } else {
                        xmlWriter.writeCharacters("#text: " + xmlReader.getText());
                    }
                }

            } else if (event == XMLStreamConstants.CDATA) {
                LOG.info("Write cdata");
                xmlWriter.writeCData("#text: " + xmlReader.getText());

            }
        }


        xmlWriter.flush();
        xmlWriter.close();
        xmlReader.close();
        return stringWriter.toString();
    }

    private void writeCorrectInput(XMLStreamWriter2 xmlWriter, String lastElementName, String attrName, String attrValue, CLevel cLevel) throws XMLStreamException {
        if (attrName.equals("level") && lastElementName.equals("c") && cLevel != null) {
            xmlWriter.writeStartElement("select");
            xmlWriter.writeAttribute("name", lastElementName + "_" + attrName);

            List<String> childLevels = DAOFactory.instance().getCLevelDAO().findChildrenLevels(cLevel.getClId());
            childLevels = EditEadAction.CElementLevel.getPossibleLevels(cLevel.getParent(), childLevels);
            for (String childLevel : childLevels) {
                xmlWriter.writeStartElement("option");
                xmlWriter.writeAttribute("value", childLevel);
                if (attrValue.equals(childLevel))
                    xmlWriter.writeAttribute("selected", "selected");
                xmlWriter.writeCharacters(childLevel);
                xmlWriter.writeEndElement();
            }

            xmlWriter.writeEndElement();
        } else {
            xmlWriter.writeEmptyElement("input");
            xmlWriter.writeAttribute("type", "text");
            xmlWriter.writeAttribute("name", lastElementName + "_" + attrName);
            xmlWriter.writeAttribute("value", attrValue);
        }
    }

    public String getNewXmlString(String xml, Map<String, String> formValues) throws XMLStreamException, IOException {

        StringWriter stringWriter = new StringWriter();
        XMLStreamWriter2 xmlWriter = ((XMLOutputFactory2) XMLOutputFactory2.newInstance()).createXMLStreamWriter(stringWriter, UTF8);
        XMLStreamReader2 xmlReader = (XMLStreamReader2) ((XMLInputFactory2) XMLInputFactory2.newInstance()).createXMLStreamReader(IOUtils.toInputStream(xml, UTF8), UTF8);

        String changedItem = null;

        for (int event = xmlReader.next(); event != XMLStreamConstants.END_DOCUMENT; event = xmlReader.next()) {
            if (event == XMLStreamConstants.START_ELEMENT) {

                changedItem = null;

                QName element = xmlReader.getName();
                if (element.equals(C_ELEMENT) || element.equals(EAD_ELEMENT)) {
                    xmlWriter.writeStartElement(element.getPrefix(), element.getLocalPart(), element.getNamespaceURI());
                    xmlWriter.writeDefaultNamespace(APENET_EAD);
                    xmlWriter.writeNamespace("xlink", XLINK);
                    xmlWriter.writeNamespace("xsi", XSI);
                } else {
                    xmlWriter.writeStartElement(element.getPrefix(), element.getLocalPart(), element.getNamespaceURI());
                }
                for (int i = 0; i < xmlReader.getAttributeCount(); i++) {
                    String changedAttr = isElementChanged(xmlReader.getAttributeLocalName(i), formValues);
                    if (changedAttr == null)
                        xmlWriter.writeAttribute(xmlReader.getAttributePrefix(i), xmlReader.getAttributeNamespace(i), xmlReader.getAttributeLocalName(i), xmlReader.getAttributeValue(i));
                    else
                        xmlWriter.writeAttribute(xmlReader.getAttributePrefix(i), xmlReader.getAttributeNamespace(i), xmlReader.getAttributeLocalName(i), changedAttr);
                }

                changedItem = isElementChanged(element.getLocalPart(), formValues);

            } else if (event == XMLStreamConstants.END_ELEMENT) {

                writeEndElement(xmlReader, xmlWriter);

            } else if (event == XMLStreamConstants.CHARACTERS) {

                if (changedItem == null)
                    xmlWriter.writeCharacters(xmlReader.getText());
                else
                    xmlWriter.writeCharacters(changedItem);

                changedItem = null;

            } else if (event == XMLStreamConstants.CDATA) {

                if (changedItem == null)
                    xmlWriter.writeCData(xmlReader.getText());
                else
                    xmlWriter.writeCData(changedItem);

                changedItem = null;

            }
        }


        xmlWriter.flush();
        xmlWriter.close();
        xmlReader.close();
        return stringWriter.toString();
    }

    public String addInLevel(EditEadAction.AddableFields field, String xml, String value) throws XMLStreamException, IOException {
        LOG.info("We are adding '" + value + "' for the key '" + field.getName() + "'");

        StringWriter stringWriter = new StringWriter();
        XMLStreamWriter2 xmlWriter = ((XMLOutputFactory2) XMLOutputFactory2.newInstance()).createXMLStreamWriter(stringWriter, UTF8);

        XMLInputFactory2 xmlif = ((XMLInputFactory2) XMLInputFactory2.newInstance());
        xmlif.setProperty(XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES, Boolean.FALSE);
        xmlif.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, Boolean.FALSE);
        xmlif.setProperty(XMLInputFactory.SUPPORT_DTD, Boolean.FALSE);
        xmlif.setProperty(XMLInputFactory.IS_COALESCING, Boolean.FALSE);
        XMLStreamReader2 xmlReader = (XMLStreamReader2) xmlif.createXMLStreamReader(IOUtils.toInputStream(xml, UTF8), UTF8);

        String[] pathElements = field.getXpath().split("/");
        int lenghtPath = pathElements.length;
        int pointerPath = 0;
        boolean isInsidePath = false;

        for (int event = xmlReader.next(); event != XMLStreamConstants.END_DOCUMENT; event = xmlReader.next()) {
            if (event == XMLStreamConstants.START_ELEMENT) {

                QName element = xmlReader.getName();
                if (element.equals(C_ELEMENT) || element.equals(EAD_ELEMENT)) {
                    xmlWriter.writeStartElement(element.getPrefix(), element.getLocalPart(), element.getNamespaceURI());
                    xmlWriter.writeDefaultNamespace(APENET_EAD);
                    xmlWriter.writeNamespace("xlink", XLINK);
                    xmlWriter.writeNamespace("xsi", XSI);
                } else {
                    xmlWriter.writeStartElement(element.getPrefix(), element.getLocalPart(), element.getNamespaceURI());
                }

                for (int i = 0; i < xmlReader.getAttributeCount(); i++) {
                    xmlWriter.writeAttribute(xmlReader.getAttributePrefix(i), xmlReader.getAttributeNamespace(i), xmlReader.getAttributeLocalName(i), xmlReader.getAttributeValue(i));
                }

                //fewsfesf
                LOG.info(xmlReader.getLocalName() + " - " + pathElements[pointerPath]);
                if (xmlReader.getLocalName().equals(pathElements[pointerPath])) {
                    LOG.info("We arrive in " + xmlReader.getLocalName());
                    isInsidePath = true;
                    LOG.info("pointerPath = " + pointerPath + ", lenghtPath = " + lenghtPath);
                    if (pointerPath == lenghtPath - 1) {
                        LOG.info("We are inside element");
                    }
                    pointerPath++;
                }
                //fesfe


            } else if (event == XMLStreamConstants.END_ELEMENT) {

                if (isInsidePath && xmlReader.getLocalName().equals(pathElements[pointerPath - 1])) {
                    pointerPath--;
                    LOG.info("We leave from " + xmlReader.getLocalName() + ", so it means the element we look for does not exist - we need to create it here, before closing this tag.");

                    int numberOfOpenedTags = 0;
                    while (pointerPath != lenghtPath - 1) {
                        LOG.info("pointerPath = " + pointerPath + ", lenghtPath = " + lenghtPath);
                        numberOfOpenedTags++;
                        pointerPath++;
                        LOG.info("pointerPath++ = " + pointerPath);
                        if (pointerPath == lenghtPath - 1)
                            createTag(xmlWriter, pathElements[pointerPath], value);
                        else
                            createTag(xmlWriter, pathElements[pointerPath], null);
                    }

                    while (numberOfOpenedTags != 0) {
                        numberOfOpenedTags--;
                        xmlWriter.writeEndElement();
                    }

                    LOG.info("pointerPath = " + pointerPath);
                    if (pointerPath == 0) {
                        LOG.info("We left the full path");
                        isInsidePath = false;
                    }
                }

                writeEndElement(xmlReader, xmlWriter);

            } else if (event == XMLStreamConstants.CHARACTERS) {

                xmlWriter.writeCharacters(xmlReader.getText());

            } else if (event == XMLStreamConstants.CDATA) {

                xmlWriter.writeCData(xmlReader.getText());

            }
        }


        xmlWriter.flush();
        xmlWriter.close();
        xmlReader.close();
        return stringWriter.toString();
    }

//    public String cLevelXmlToEadXml(String cLevelXml){
//
//        StringWriter stringWriter = new StringWriter();
//
//        XMLInputFactory2 xmlif = ((XMLInputFactory2) XMLInputFactory2.newInstance());
//        xmlif.setProperty(XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES, Boolean.FALSE);
//        xmlif.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, Boolean.FALSE);
//        xmlif.setProperty(XMLInputFactory.SUPPORT_DTD, Boolean.FALSE);
//        xmlif.setProperty(XMLInputFactory.IS_COALESCING, Boolean.FALSE);
//        try {
//            XMLStreamReader2 xmlReader = (XMLStreamReader2) xmlif.createXMLStreamReader(IOUtils.toInputStream(cLevelXml, UTF8), UTF8);
//            XMLStreamWriter2 xmlWriter = ((XMLOutputFactory2) XMLOutputFactory2.newInstance()).createXMLStreamWriter(stringWriter, UTF8);
//
//            for (int event = xmlReader.next(); event != XMLStreamConstants.END_DOCUMENT; event = xmlReader.next()) {
//                if (event == XMLStreamConstants.START_ELEMENT) {
//                    if(xmlReader.getName().equals(C_ELEMENT)){
//                        writeEAD(xmlWriter);
//                        xmlWriter.writeStartElement("eadheader");
//                        xmlWriter.writeAttribute("countryencoding", "iso3166-1");
//                        xmlWriter.writeAttribute("dateencoding", "iso8601");
//                        xmlWriter.writeAttribute("langencoding", "iso639-2b");
//                        xmlWriter.writeAttribute("repositoryencoding", "iso15511");
//                        xmlWriter.writeAttribute("scriptencoding", "iso15924");
//                        xmlWriter.writeAttribute("relatedencoding", "MARC21");
//                        xmlWriter.writeStartElement("eadid");
//                        xmlWriter.writeAttribute("countrycode", "PT");
//                        xmlWriter.writeAttribute("mainagencycode", "TEST");
//
//                    }
//                } else if (event == XMLStreamConstants.END_ELEMENT) {
//
//                } else if (event == XMLStreamConstants.CHARACTERS) {
//
//                } else if (event == XMLStreamConstants.CDATA) {
//
//                }
//            }
//
//        } catch (Exception e) {
//            LOG.error("Error", e);
//        }
//
//        return null;
//    }

    private void createTag(XMLStreamWriter2 writer, String element, String value) throws XMLStreamException {
        LOG.info("We create element: " + element);
        writer.writeStartElement(null, element, APENET_EAD);
        if (value == null) {
            LOG.info("But value is null, so not last element");
        } else {
            LOG.info("And value is not null, so last element");
            writer.writeCharacters(value);
        }
    }

    /**
     * Checks if an element of the original XML is part of the submitted form
     *
     * @param xmlElementName The name of the element in the original XML
     * @return Either the value of the changed element or null if it does not exist
     */
    public String isElementChanged(String xmlElementName, Map<String, String> formValues) {
        for (String key : formValues.keySet()) {
            String elementName = key;
            if (key.contains("_"))
                elementName = key.split("_")[1];

            if (xmlElementName.equals(elementName))
                return formValues.get(key);
        }
        return null;
    }
}