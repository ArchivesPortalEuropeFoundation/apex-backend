package eu.apenet.dashboard.manual;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.util.TextProviderHelper;
import org.codehaus.stax2.XMLInputFactory2;
import org.codehaus.stax2.XMLOutputFactory2;
import org.codehaus.stax2.XMLStreamReader2;
import org.codehaus.stax2.XMLStreamWriter2;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.util.ValueStack;

import eu.apenet.dashboard.actions.ajax.EditEadAction;
import eu.apenet.dashboard.services.ead.xml.AbstractParser;
import eu.apenet.dpt.utils.util.LanguageIsoList;
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
    /**
	 * Serializable.
	 */

	private static final Logger LOG = Logger.getLogger(EditParser.class);
    public final QName C_ELEMENT = new QName(APENET_EAD, "c");
    public final QName EAD_ELEMENT = new QName(APENET_EAD, "ead");
    public final QName LANGUAGE_ELEMENT = new QName(APENET_EAD, "language");
    private static final String UTF8 = "UTF-8";
    // Counters to be able to parse all the values.
    // General counter.
    private int counter = 1;
    // Counter for attibute "@normal" in element <unitdate>.
    private int counterUnitdate = 1;
    // Counter for element <titleproper>.
    private int counterTitleproper = 1;
    // Counter for element <language>.
    private int counterLanguage = 1;
    // Counter for attibute "@langcode" in element <language>.
    private int counterLangcode = 1;

    // Name of the elements and attributes.
    // Name of element <titlestmt>.
	private static final String TITLESTMT = "titlestmt";
    // Name of element "<titleproper>".
	private static final String TITLEPROPER = "titleproper";
    // Name of attibute "@normal" in element <unitdate>.
	private static final String NORMAL = "normal";
    // Name of element <unitdate>.
	private static final String UNITDATE = "unitdate";
	// Name of element <langmaterial>.
	private static final String LANGMATERIAL = "langmaterial";
	// Name of element <langusage>.
	private static final String LANGUSAGE = "langusage";
	// Name of element <language>.
	private static final String LANGUAGE = "language";
    // Name of attibute "@langcode" in element <language>.
	private static final String LANGCODE = "langcode";
    // Name of attibute "@scriptcode" in element <language>.
	private static final String SCRIPTCODE = "scriptcode";
    // Name of attibute "@level" in element <c>.
	private static final String LEVEL = "level";
    // Name of id for attibute "@normal" in element <unitdate>.
	private static final String UNITDATE_NORMAL = "unitdate_normal";
    // Name of id for attibute "@langcode" in element <language>.
	private static final String LANGUAGE_LANGCODE = "language_langcode";

	// Default value of attribute @scriptcode.
	private static final String SCRIPTCODE_VALUE = "Latn";
	// Default value of namespaceURI for element <language>.
	private static final String LANGUAGE_NAMESPACE_URI = "urn:isbn:1-931666-22-9";
	// Default value of attribue @langcode.
	private static final String LANGCODE_VALUE = "none";

	// Variables to check if exists (or no) the necessary elements and attributes. 
	// Element <unitdate>.
	private boolean unitdateLocated = false;
	// Attribute @normal in element <unitdate>.
	private boolean normalLocated = false;
	// Element <titlestmt>.
	private boolean titlestmtLocated = false;
	// Element <titleproper>.
	private boolean titleproperLocated = false;
	// Element <langmaterial> or <langusage>.
	private boolean languageSectionLocated = false;
	// Element <language>.
	private boolean languageLocated = false;
	// Attriute @lagcode in element <language>.
	private boolean langcodeLocated = false;

	// Variables to check if the value is added.
	// Element <titleproper>.
	private boolean titleproperValueAdded = false;

	// Map with the values.
	private Map<String, String> formValues;

	// The text value of the element.
	private String changedItem;

    /**
	 * @return the counter
	 */
	public int getCounter() {
		return this.counter;
	}

	/**
	 * @param counter the counter to set
	 */
	public void setCounter(int counter) {
		this.counter = counter;
	}

    /**
	 * @return the counterUnitdate
	 */
	public int getCounterUnitdate() {
		return this.counterUnitdate;
	}

	/**
	 * @param counterUnitdate the counterUnitdate to set
	 */
	public void setCounterUnitdate(int counterUnitdate) {
		this.counterUnitdate = counterUnitdate;
	}

    /**
	 * @return the counterTitleproper
	 */
	public int getCounterTitleproper() {
		return this.counterTitleproper;
	}

	/**
	 * @param counterTitleproper the counterTitleproper to set
	 */
	public void setCounterTitleproper(int counterTitleproper) {
		this.counterTitleproper = counterTitleproper;
	}

	/**
	 * @return the counterLanguage
	 */
	public int getCounterLanguage() {
		return this.counterLanguage;
	}

	/**
	 * @param counterLanguage the counterLanguage to set
	 */
	public void setCounterLanguage(int counterLanguage) {
		this.counterLanguage = counterLanguage;
	}

	/**
	 * @return the counterLangcode
	 */
	public int getCounterLangcode() {
		return this.counterLangcode;
	}

	/**
	 * @param counterLangcode the counterLangcode to set
	 */
	public void setCounterLangcode(int counterLangcode) {
		this.counterLangcode = counterLangcode;
	}

	/**
	 * @return the unitdateLocated
	 */
	public boolean isUnitdateLocated() {
		return this.unitdateLocated;
	}

	/**
	 * @param unitdateLocated the unitdateLocated to set
	 */
	public void setUnitdateLocated(boolean unitdateLocated) {
		this.unitdateLocated = unitdateLocated;
	}

	/**
	 * @return the normalLocated
	 */
	public boolean isNormalLocated() {
		return this.normalLocated;
	}

	/**
	 * @param normalLocated the normalLocated to set
	 */
	public void setNormalLocated(boolean normalLocated) {
		this.normalLocated = normalLocated;
	}

	/**
	 * @return the titlestmtLocated
	 */
	public boolean isTitlestmtLocated() {
		return this.titlestmtLocated;
	}

	/**
	 * @param titlestmtLocated the titlestmtLocated to set
	 */
	public void setTitlestmtLocated(boolean titlestmtLocated) {
		this.titlestmtLocated = titlestmtLocated;
	}

	/**
	 * @return the titleproperLocated
	 */
	public boolean isTitleproperLocated() {
		return this.titleproperLocated;
	}

	/**
	 * @param titleproperLocated the titleproperLocated to set
	 */
	public void setTitleproperLocated(boolean titleproperLocated) {
		this.titleproperLocated = titleproperLocated;
	}

	/**
	 * @return the titleproperValueAdded
	 */
	public boolean isTitleproperValueAdded() {
		return this.titleproperValueAdded;
	}

	/**
	 * @param titleproperValueAdded the titleproperValueAdded to set
	 */
	public void setTitleproperValueAdded(boolean titleproperValueAdded) {
		this.titleproperValueAdded = titleproperValueAdded;
	}

	/**
	 * @return the languageSectionLocated
	 */
	public boolean isLanguageSectionLocated() {
		return this.languageSectionLocated;
	}

	/**
	 * @param languageSectionLocated the languageSectionLocated to set
	 */
	public void setLanguageSectionLocated(boolean languageSectionLocated) {
		this.languageSectionLocated = languageSectionLocated;
	}

	/**
	 * @return the languageLocated
	 */
	public boolean isLanguageLocated() {
		return this.languageLocated;
	}

	/**
	 * @param languageLocated the languageLocated to set
	 */
	public void setLanguageLocated(boolean languageLocated) {
		this.languageLocated = languageLocated;
	}

	/**
	 * @return the langcodeLocated
	 */
	public boolean isLangcodeLocated() {
		return this.langcodeLocated;
	}

	/**
	 * @param langcodeLocated the langcodeLocated to set
	 */
	public void setLangcodeLocated(boolean langcodeLocated) {
		this.langcodeLocated = langcodeLocated;
	}

	/**
	 * @return the formValues
	 */
	public Map<String, String> getFormValues() {
		return this.formValues;
	}

	/**
	 * @param formValues the formValues to set
	 */
	public void setFormValues(Map<String, String> formValues) {
		this.formValues = formValues;
	}

	/**
	 * @return the changedItem
	 */
	public String getChangedItem() {
		return this.changedItem;
	}

	/**
	 * @param changedItem the changedItem to set
	 */
	public void setChangedItem(String changedItem) {
		this.changedItem = changedItem;
	}

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

        // Fill the parent div which contains the language combo to clone and
        // the rest of the EAD data.
        LOG.debug("Open parent div which contains the 'language_langcode_hidden' combo and the ead data.");
        counterDiv++;
        xmlWriter.writeStartElement("div");
        xmlWriter.writeAttribute("class", "editionElement");
        // Get the language combo to clone.
        this.writeHiddenLanguageBox(xmlWriter);

        // Get the EAD data.
        String lastElementName = null;
        for (int event = xmlReader.next(); event != XMLStreamConstants.END_DOCUMENT; event = xmlReader.next()) {
            if (event == XMLStreamConstants.START_ELEMENT) {

                lastElementName = xmlReader.getLocalName();
            	this.checkAttributes(lastElementName, null);

                if (EditEadAction.UndisplayableFields.isDisplayable(lastElementName)) {
                    LOG.debug("Open div for '" + lastElementName + "'");
                    counterDiv++;
                    xmlWriter.writeStartElement("div");
                    xmlWriter.writeAttribute("class", "editionElement");
                    xmlWriter.writeCharacters(xmlReader.getLocalName() + ": ");
                    xmlWriter.writeEmptyElement("br");
                    for (int i = 0; i < xmlReader.getAttributeCount(); i++) {
                        if (EditEadAction.UndisplayableFields.isDisplayable(xmlReader.getAttributeLocalName(i))) {
                        	this.checkAttributes(lastElementName, xmlReader.getAttributeLocalName(i));
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

                    // Checks the element to close.
                    if (lastElementName != null) {
                    	this.checkEndElement(lastElementName, xmlWriter);
                    } else  if (xmlReader.getLocalName() != null) {
                    	this.checkEndElement(xmlReader.getLocalName(), xmlWriter);
                    }

                    xmlWriter.writeEndElement();

                    // Reset the located elements.
                    if (lastElementName != null) {
                    	this.resetElementsLocated(lastElementName);
                    } else  if (xmlReader.getLocalName() != null) {
                    	this.resetElementsLocated(xmlReader.getLocalName());
                    }
                }
                lastElementName = null;
            } else if (event == XMLStreamConstants.CHARACTERS) {
                if (!StringUtils.isBlank(xmlReader.getText()) && EditEadAction.UndisplayableFields.isDisplayable(lastElementName)) {
                    if (lastElementName != null && EditEadAction.EditableFields.isEditable(lastElementName)) {
                        xmlWriter.writeCharacters("#text: ");
                        xmlWriter.writeEmptyElement("input");
                        xmlWriter.writeAttribute("type", "text");
                        xmlWriter.writeAttribute("value", xmlReader.getText());
                        if (this.isUnitdateLocated()) {
	                        xmlWriter.writeAttribute("name", lastElementName + "_" + this.getCounterUnitdate());
	                        this.setCounterUnitdate(this.getCounterUnitdate() + 1);
                        } else if (this.isTitleproperLocated()) {
	                        xmlWriter.writeAttribute("name", lastElementName + "_" + this.getCounterTitleproper());
	                        this.setCounterTitleproper(this.getCounterTitleproper() + 1);
                        } else if (this.isLanguageLocated()) {
	                        xmlWriter.writeAttribute("name", lastElementName + "_" + this.getCounterLanguage() + "_" + this.getCounterLangcode());
                        } else {
	                        xmlWriter.writeAttribute("name", lastElementName + "_" + this.getCounter());
	                        this.setCounter(this.getCounter() + 1);
                        }
                    } else {
                        xmlWriter.writeCharacters("#text: " + xmlReader.getText());
                    }

                    // Set the value of the element "titleproper" as set.
                    if (EditParser.TITLEPROPER.equalsIgnoreCase(lastElementName)) {
                    	this.setTitleproperValueAdded(true);
                    }
                }

            } else if (event == XMLStreamConstants.CDATA) {
                LOG.info("Write cdata");
                xmlWriter.writeCData("#text: " + xmlReader.getText());

            }
        }

        // Close the parent div which contains the language combo to clone and
        // the rest of the EAD data. 
        xmlWriter.writeEndElement();
        LOG.debug("Close div which contains the 'language_langcode_hidden' combo and the ead data.");
        counterDiv--;

        xmlWriter.flush();
        xmlWriter.close();
        xmlReader.close();
        return stringWriter.toString();
    }

    private void writeCorrectInput(XMLStreamWriter2 xmlWriter, String lastElementName, String attrName, String attrValue, CLevel cLevel) throws XMLStreamException {
        if (EditParser.LANGCODE.equalsIgnoreCase(attrName)
        		&& EditParser.LANGUAGE.equalsIgnoreCase(lastElementName)) {
        	// Write the select for the possible language codes.
            xmlWriter.writeStartElement("select");
            xmlWriter.writeAttribute("name", lastElementName + "_" + attrName + "_" + this.getCounterLanguage() + "_" + this.getCounterLangcode());

            // Add empty value to the language select.
            xmlWriter.writeStartElement("option");
            xmlWriter.writeAttribute("value", EditParser.LANGCODE_VALUE);
            xmlWriter.writeCharacters("---");
            xmlWriter.writeEndElement();

            List<String> languagesList = LanguageIsoList.getLanguageIsoList();
            for (String language : languagesList) {
                xmlWriter.writeStartElement("option");
                String langCode = LanguageIsoList.getIsoCode(language);
                xmlWriter.writeAttribute("value", langCode);
                // Checks if the current element is the selected one.
                if (attrValue.equals(langCode)) {
                	xmlWriter.writeAttribute("selected", "selected");
                }
                xmlWriter.writeCharacters(LanguageIsoList.getIsoCode(language));
                xmlWriter.writeEndElement();
            }
            xmlWriter.writeEndElement();
        } else {
            xmlWriter.writeEmptyElement("input");
            xmlWriter.writeAttribute("type", "text");
            xmlWriter.writeAttribute("value", attrValue);
            if (this.isUnitdateLocated()) {
            	xmlWriter.writeAttribute("name", lastElementName + "_" + attrName + "_" + this.getCounterUnitdate());
                this.setCounterUnitdate(this.getCounterUnitdate() + 1);
            } else if (this.isTitleproperLocated()) {
                	xmlWriter.writeAttribute("name", lastElementName + "_" + attrName + "_" + this.getCounterTitleproper());
                    this.setCounterTitleproper(this.getCounterTitleproper() + 1);
            } else if (this.isLanguageLocated()) {
            	xmlWriter.writeAttribute("name", lastElementName + "_" + attrName + "_" + this.getCounterLanguage() + "_" + this.getCounterLangcode());
            } else {
            	xmlWriter.writeAttribute("name", lastElementName + "_" + attrName + "_" + this.getCounter());
                this.setCounter(this.getCounter() + 1);
            }
        }
    }

    private void writeHiddenLanguageBox(XMLStreamWriter2 xmlWriter) throws XMLStreamException {
    	// Write the select for the possible language codes.
        xmlWriter.writeStartElement("select");
        xmlWriter.writeAttribute("name", "language_langcode_hidden");
        xmlWriter.writeAttribute("class", "hidden");

        // Add empty value to the language select.
        xmlWriter.writeStartElement("option");
        xmlWriter.writeAttribute("value", EditParser.LANGCODE_VALUE);
    	xmlWriter.writeAttribute("selected", "selected");
        xmlWriter.writeCharacters("---");
        xmlWriter.writeEndElement();

        List<String> languagesList = LanguageIsoList.getLanguageIsoList();
        for (String language : languagesList) {
            xmlWriter.writeStartElement("option");
            String langCode = LanguageIsoList.getIsoCode(language);
            xmlWriter.writeAttribute("value", langCode);
            xmlWriter.writeCharacters(LanguageIsoList.getIsoCode(language));
            xmlWriter.writeEndElement();
        }
        xmlWriter.writeEndElement();
    }

    public String getNewXmlString(String xml, Map<String, String> formValues) throws XMLStreamException, IOException {
    	Map<String, String> formValuesCopy = new LinkedHashMap<String, String>(formValues);

    	// Set the copy to the global map.
    	this.setFormValues(formValuesCopy);

        StringWriter stringWriter = new StringWriter();
        XMLStreamWriter2 xmlWriter = ((XMLOutputFactory2) XMLOutputFactory2.newInstance()).createXMLStreamWriter(stringWriter, UTF8);
        XMLStreamReader2 xmlReader = (XMLStreamReader2) ((XMLInputFactory2) XMLInputFactory2.newInstance()).createXMLStreamReader(IOUtils.toInputStream(xml, UTF8), UTF8);

        this.setChangedItem(null);
        boolean isLanguage = true;

        for (int event = xmlReader.next(); event != XMLStreamConstants.END_DOCUMENT; event = xmlReader.next()) {
            if (event == XMLStreamConstants.START_ELEMENT) {

            	this.setChangedItem(null);
                isLanguage = true;

                QName element = xmlReader.getName();

                this.checkAttributes(xmlReader.getLocalName(), null);

                if (element.equals(C_ELEMENT) || element.equals(EAD_ELEMENT)) {
                    xmlWriter.writeStartElement(element.getPrefix(), element.getLocalPart(), element.getNamespaceURI());
                    xmlWriter.writeDefaultNamespace(APENET_EAD);
                    xmlWriter.writeNamespace("xlink", XLINK);
                    xmlWriter.writeNamespace("xsi", XSI);
                    this.addContent(xmlReader, xmlWriter, element);
                } else  if (element.equals(this.LANGUAGE_ELEMENT)) {
                	isLanguage = this.checkCurrentLanguage(xmlWriter, xmlReader, element);
                } else {
                    xmlWriter.writeStartElement(element.getPrefix(), element.getLocalPart(), element.getNamespaceURI());
                    this.addContent(xmlReader, xmlWriter, element);
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {

                if (EditParser.LANGMATERIAL.equalsIgnoreCase(xmlReader.getLocalName())
                		|| EditParser.LANGUSAGE.equalsIgnoreCase(xmlReader.getLocalName())) {
                	// Checks if its necessary add new values.
                	if (this.getFormValues() != null) {
                		this.checkAddedLanguages(xmlWriter, xmlReader);
                	}
                }
            	// Reset the located elements.
                this.resetElementsLocated(xmlReader.getLocalName());

                if (isLanguage) {
                	writeEndElement(xmlReader, xmlWriter);
                }
                isLanguage = true;
            } else if (event == XMLStreamConstants.CHARACTERS) {
            	if (isLanguage) {
	                if (this.getChangedItem() == null)
	                    xmlWriter.writeCharacters(xmlReader.getText());
	                else
	                    xmlWriter.writeCharacters(this.getChangedItem());
            	}

            	this.setChangedItem(null);
            } else if (event == XMLStreamConstants.CDATA) {
            	if (isLanguage) {
	                if (this.getChangedItem() == null)
	                    xmlWriter.writeCData(xmlReader.getText());
	                else
	                    xmlWriter.writeCData(this.getChangedItem());
            	}

            	this.setChangedItem(null);
            }
        }

        xmlWriter.flush();
        xmlWriter.close();
        xmlReader.close();
        return stringWriter.toString();
    }

    private void addContent(XMLStreamReader2 xmlReader,
			XMLStreamWriter2 xmlWriter, QName element) throws XMLStreamException {
        for (int i = 0; i < xmlReader.getAttributeCount(); i++) {
        	this.checkAttributes(xmlReader.getLocalName(), xmlReader.getAttributeLocalName(i));
            String changedAttr = isElementChanged(element.getLocalPart(), xmlReader.getAttributeLocalName(i));
            if (changedAttr == null)
                xmlWriter.writeAttribute(xmlReader.getAttributePrefix(i), xmlReader.getAttributeNamespace(i), xmlReader.getAttributeLocalName(i), xmlReader.getAttributeValue(i));
            else
                xmlWriter.writeAttribute(xmlReader.getAttributePrefix(i), xmlReader.getAttributeNamespace(i), xmlReader.getAttributeLocalName(i), changedAttr);
        }

        // Checks if added "normal" attribute in element "unitdate".
        if (this.isUnitdateLocated() && !this.isNormalLocated()) {
        	String newAttribute = this.isElementChanged(element.getLocalPart(), EditParser.NORMAL);
        	if (newAttribute != null) {
        		xmlWriter.writeAttribute("", "", EditParser.NORMAL, newAttribute);
        	}
        }

        this.setChangedItem(isElementChanged(element.getLocalPart(), null));
	}

	/**
     * Method to check if the current language element should be maintained,
     * changed or removed.
     *
     * @param xmlWriter
     * @param xmlReader
     * @param element
     * @return
     * @throws XMLStreamException
     */
    private boolean checkCurrentLanguage(XMLStreamWriter2 xmlWriter,
			XMLStreamReader2 xmlReader, QName element) throws XMLStreamException {
    	boolean result = false;
		// Checks the value of the element.
    	String elementValue = isElementChanged(element.getLocalPart(), null);
		if (elementValue != null
				&& !elementValue.trim().isEmpty()) {
				xmlWriter.writeStartElement(element.getPrefix(), element.getLocalPart(), element.getNamespaceURI());
	        for (int i = 0; i < xmlReader.getAttributeCount(); i++) {
	        	this.checkAttributes(xmlReader.getLocalName(), xmlReader.getAttributeLocalName(i));
	            String changedAttr = isElementChanged(element.getLocalPart(), xmlReader.getAttributeLocalName(i));
	            if (changedAttr == null)
	                xmlWriter.writeAttribute(xmlReader.getAttributePrefix(i), xmlReader.getAttributeNamespace(i), xmlReader.getAttributeLocalName(i), xmlReader.getAttributeValue(i));
	            else
	                xmlWriter.writeAttribute(xmlReader.getAttributePrefix(i), xmlReader.getAttributeNamespace(i), xmlReader.getAttributeLocalName(i), changedAttr);
	        }
	        this.setChangedItem(elementValue);
	        result = true;
		}
		return result;
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
     * @param xmlAttributeName The name of the attribute in the original XML
     * @return Either the value of the changed element or null if it does not exist
     */
    public String isElementChanged(String xmlElementName, String xmlAttributeName) {
    	// Recover the list of the keys for the current element.
    	Set<String> keyList= new LinkedHashSet<String>();
    	if (this.getFormValues() != null) {
    		Set<String> keySet = this.getFormValues().keySet();
    		if (keySet != null) {
    			Iterator<String> keysIt = keySet.iterator();
    			while (keysIt.hasNext()) {
    				String key = keysIt.next();
    				String xmlKey = xmlElementName + "_";

    				if (xmlAttributeName != null) {
    					if (xmlAttributeName.equalsIgnoreCase(EditParser.LEVEL)) {
    						xmlKey = xmlKey + xmlAttributeName;
    					} else {
    						xmlKey = xmlKey + xmlAttributeName + "_";
    					}
    				}
    				
    				if (key.startsWith(xmlKey)) {
    					// Checks if the current element is "unitdate" and has attribute "normal".
    					if (EditParser.UNITDATE.equalsIgnoreCase(xmlElementName)) {
    						if (xmlAttributeName != null
    								&& EditParser.NORMAL.equalsIgnoreCase(xmlAttributeName)) {
    							// recover the proper key for the current unitdate element.
    							if (key.endsWith("_" + this.getCounterUnitdate())) {
        							keyList.add(key);
    							}
    						}
    					} else if (EditParser.TITLEPROPER.equalsIgnoreCase(xmlElementName)) {
    						// Check if the current element is "titleproper" and recover the
    						// proper key for the current titleproper element.
    							if (key.endsWith("_" + this.getCounterTitleproper())) {
        							keyList.add(key);
    							}
						} else if (EditParser.LANGUAGE.equalsIgnoreCase(xmlElementName)) {
							// Checks if the current element is "language" and has attribute "langcode".
							if (xmlAttributeName != null
    								&& EditParser.LANGCODE.equalsIgnoreCase(xmlAttributeName)) {
    							// recover the proper key for the current langcode attribute.
    							if (key.endsWith("_" + this.getCounterLanguage() + "_" + this.getCounterLangcode())) {
        							keyList.add(key);
    							}
							} else if (xmlAttributeName == null
									&& key.endsWith("_" + this.getCounterLanguage() + "_" + this.getCounterLangcode())
									&& !key.startsWith(EditParser.LANGUAGE_LANGCODE)) {
								keyList.add(key);
							}
    					} else {
    						keyList.add(key);
    					}
    				}
    			}
    		}
    	}

    	// Updates counterUnitdate if necessary.
    	if (EditParser.UNITDATE.equalsIgnoreCase(xmlElementName)) {
			if (xmlAttributeName != null
					&& EditParser.NORMAL.equalsIgnoreCase(xmlAttributeName)) {
				this.setCounterUnitdate(this.getCounterUnitdate() + 1);
			}
    	}

    	// Updates counterTitleproper if necessary.
    	if (EditParser.TITLEPROPER.equalsIgnoreCase(xmlElementName)
    			&& xmlAttributeName == null) {
			this.setCounterTitleproper(this.getCounterTitleproper() + 1);
    	}

    	// Check if exists values in the list.
    	if (keyList != null && !keyList.isEmpty()) {
    		String key = keyList.iterator().next();
    		String value = this.getFormValues().get(key);
    		this.getFormValues().remove(key);
    		// Unescape char '.
    		if (value.contains("%27")) { 
    			value = value.replaceAll("%27", "'");
    		}
    		// Unescape char <.
    		if (value.contains("%3C")) { 
    			value = value.replaceAll("%3C", "<");
    		}
    		// Unescape char >.
    		if (value.contains("%3E")) { 
    			value = value.replaceAll("%3E", ">");
    		}
    		return value;
    	}

        return null;
    }

    /**
     * Method to check the attribute name to enable or disable the "add attribute" button.
     *
     * @param elementName Name of the current element.
     * @param attributeName Name of the current attribute for the element.
     */
    private void checkAttributes(String elementName, String attributeName) {
    	LOG.debug("Check attribute " + attributeName + ", for element " + elementName);
    	// Check if the current element is "unitdate".
    	if (EditParser.UNITDATE.equalsIgnoreCase(elementName)) {
    		LOG.debug("Located element " + elementName);
    		this.setUnitdateLocated(true);
    	}

    	// Check if the current element is "unitdate" and contains attribute "normal".
    	if (EditParser.UNITDATE.equalsIgnoreCase(elementName)
    			&& EditParser.NORMAL.equalsIgnoreCase(attributeName)) {
    		LOG.debug("Located attribute " + attributeName + ", for element " + elementName);
    		this.setNormalLocated(true);
    	}

    	// Check if the current element is "titlestmt".
    	if (EditParser.TITLESTMT.equalsIgnoreCase(elementName)) {
    		LOG.debug("Located element " + elementName);
    		this.setTitlestmtLocated(true);
    	}

    	// Check if the current element is "titleproper".
    	if (EditParser.TITLEPROPER.equalsIgnoreCase(elementName)) {
    		LOG.debug("Located element " + elementName);
    		this.setTitleproperLocated(true);
    	}

    	// Check if current element is "langmaterial" or "langusage".
    	if (EditParser.LANGMATERIAL.equalsIgnoreCase(elementName)
    			|| EditParser.LANGUSAGE.equalsIgnoreCase(elementName)) {
    		LOG.debug("Located element " + elementName);
    		this.setLanguageSectionLocated(true);
    	}

    	// Check if the current element is "language".
    	if (EditParser.LANGUAGE.equalsIgnoreCase(elementName)) {
    		LOG.debug("Located element " + elementName);
    		this.setLanguageLocated(true);
    	}

    	// Check if the current element is "language" and contains attribute "langcode".
    	if (EditParser.LANGUAGE.equalsIgnoreCase(elementName)
    			&& EditParser.LANGCODE.equalsIgnoreCase(attributeName)) {
    		LOG.debug("Located attribute " + attributeName + ", for element " + elementName);
    		this.setLangcodeLocated(true);
    	}
    }

    /**
     * Method to reset the located elements when necessary.
     *
     * @param elementName Name of the current element.
     */
    private void resetElementsLocated(String elementName) {
    	// Reset located element <unitdate>.
    	if (EditParser.UNITDATE.equalsIgnoreCase(elementName)) {
        	LOG.debug("Reset located element: " + elementName);
    		this.setUnitdateLocated(false);
    	}
    	// Reset located attribute @normal inside element <unitdate>.
    	if (this.isUnitdateLocated()
    			&& EditParser.NORMAL.equalsIgnoreCase(elementName)) {
        	LOG.debug("Reset located element: " + elementName);
    		this.setNormalLocated(false);
    	}
    	// Reset located element <titlestmt>.
    	if (EditParser.TITLESTMT.equalsIgnoreCase(elementName)) {
        	LOG.debug("Reset located element: " + elementName);
    		this.setTitlestmtLocated(false);
    	}
    	// Reset located element <titleproper>.
    	if (EditParser.TITLEPROPER.equalsIgnoreCase(elementName)) {
        	LOG.debug("Reset located element: " + elementName);
    		this.setTitleproperLocated(false);
    	}
    	// Reset located element <langmaterial> and <langusage>.
    	if (EditParser.LANGMATERIAL.equalsIgnoreCase(elementName)
    			|| EditParser.LANGUSAGE.equalsIgnoreCase(elementName)) {
        	LOG.debug("Reset located element: " + elementName);
    		this.setLanguageSectionLocated(false);
    		this.setCounterLanguage(this.getCounterLanguage() + 1);
    		this.setCounterLangcode(1);
    	}
    	// Reset located attribute @langcode inside element <language>.
    	if (this.isLanguageLocated()
    			&& EditParser.LANGCODE.equalsIgnoreCase(elementName)) {
        	LOG.debug("Reset located element: " + elementName);
    		this.setLangcodeLocated(false);
    	}
    	// Reset located element <language>.
    	if (EditParser.LANGUAGE.equalsIgnoreCase(elementName)) {
        	LOG.debug("Reset located element: " + elementName);
    		this.setLanguageLocated(false);
    		this.setCounterLangcode(this.getCounterLangcode() + 1);
    	}
    }

    /**
     * Method to check the close tags and add the appropriate buttons when necessary.
     *
     * @param elementName Name of the current close tag.
     * @throws XMLStreamException Exception processing XML.
     */
    private void checkEndElement(String elementName, XMLStreamWriter2 xmlWriter) throws XMLStreamException {
    	// Checks if the close element is "titleproper".
    	if (EditParser.TITLEPROPER.equalsIgnoreCase(elementName)) {
    		// Check if it is necessary to add input text for "titleproper" element.
    		if (!this.titleproperValueAdded) {
    			LOG.debug("Element to add is " + EditParser.TITLEPROPER + ".");
    			xmlWriter.writeCharacters("#text: ");
    			xmlWriter.writeEmptyElement("input");
    			xmlWriter.writeAttribute("type", "text");
    			xmlWriter.writeAttribute("name", EditParser.TITLEPROPER + "_" + this.getCounterTitleproper());
    			xmlWriter.writeAttribute("value", "");
    			this.setCounterTitleproper(this.getCounterTitleproper() + 1);
    		}
			// Put the button to add more "titleproper" elements.
//			LOG.debug("Element to close is " + EditParser.TITLEPROPER + ".");
//
//			LOG.debug("Element to add is " + EditParser.TITLEPROPER + ".");
//            xmlWriter.writeEmptyElement("br");
//			xmlWriter.writeEmptyElement("input");
//			xmlWriter.writeAttribute("type", "button");
//			xmlWriter.writeAttribute("name", "btn_" + EditParser.TITLEPROPER + "_" + this.getCounterTitleproper());
//			xmlWriter.writeAttribute("value", getText("dashboard.editead.btn.addElement") + " " + getText("dashboard.editead.btn.elementTitleproper"));
//			this.setCounterTitleproper(this.getCounterTitleproper() + 1);

//			this.writeCorrectButton(elementName, xmlWriter);
    	}
    	// Checks if the close element is "unitdate".
    	if (EditParser.UNITDATE.equalsIgnoreCase(elementName)
    			&& !this.isNormalLocated()) {
	    	// Element to close is "unitdate".
			// Check if it is necessary to add button for "normal" attribute to
    		// "unitdate" element.
			LOG.debug("Element to close is " + EditParser.UNITDATE + ".");

			LOG.debug("Attribute to add is " + EditParser.NORMAL + " in element " + EditParser.UNITDATE + ".");
            xmlWriter.writeEmptyElement("br");
			xmlWriter.writeEmptyElement("input");
			xmlWriter.writeAttribute("type", "button");
			xmlWriter.writeAttribute("name", "btn_" + EditParser.UNITDATE_NORMAL + "_" + this.getCounterUnitdate());
			xmlWriter.writeAttribute("value", getText("dashboard.editead.btn.addAttribute") + " " + getText("dashboard.editead.btn.attributeNormal"));
			this.setCounterUnitdate(this.getCounterUnitdate() + 1);
    	}
    	// Checks if the close element is "langmaterial" or "langusage".
    	if (EditParser.LANGMATERIAL.equalsIgnoreCase(elementName)
    			|| EditParser.LANGUSAGE.equalsIgnoreCase(elementName)) {
	    	// Element to close is "langmaterial" or "langusage". Add button to add more "language" elements
			LOG.debug("Element to close is " +elementName + ".");
			xmlWriter.writeEmptyElement("input");
			xmlWriter.writeAttribute("type", "button");
			xmlWriter.writeAttribute("name", "btn_" + EditParser.LANGUAGE + "_" + this.getCounterLanguage() + "_" + this.getCounterLangcode());
			xmlWriter.writeAttribute("value", getText("dashboard.editead.btn.addElement") + " " + getText("dashboard.editead.btn.elementLanguage"));
			this.setCounterLangcode(this.getCounterLangcode() + 1);
    	}
    }

    /**
     * Method to check if there is new language added for the current section.
     *
     * @param xmlWriter
     * @param xmlReader
     * @throws XMLStreamException 
     */
    private void checkAddedLanguages(XMLStreamWriter2 xmlWriter, XMLStreamReader2 xmlReader) throws XMLStreamException {
    	List<String> keysList = new LinkedList<String>();
    	// Recover the existing keys in the map.
    	Set<String> keySet = this.getFormValues().keySet();
		if (keySet != null) {
			Iterator<String> keysIt = keySet.iterator();
			while (keysIt.hasNext()) {
				String key = keysIt.next();
				String langKey = EditParser.LANGUAGE + "_" + this.getCounterLanguage();

				if (key.startsWith(langKey)) {
					keysList.add(key);
				}
			}
		}

		if (keysList != null && !keysList.isEmpty()) {
			// Is needed to add new language.
			for (int i = 0; i < keysList.size(); i++) {
				String languageKey = keysList.get(i);
				String languageValue = this.getFormValues().get(languageKey);
				String langcodeKey = languageKey.substring(0, (languageKey.indexOf("_") + 1)) + EditParser.LANGCODE + languageKey.substring(languageKey.indexOf("_"));
				String langcodeValue = this.getFormValues().get(langcodeKey);

				if (languageValue != null && !languageValue.isEmpty()
						&& langcodeValue != null && !langcodeValue.isEmpty()
						&& !langcodeValue.equalsIgnoreCase(EditParser.LANGCODE_VALUE)) {
					// Add the element.
					xmlWriter.writeStartElement("", EditParser.LANGUAGE, EditParser.LANGUAGE_NAMESPACE_URI);
					xmlWriter.writeAttribute("", "", EditParser.LANGCODE, langcodeValue);
					xmlWriter.writeAttribute("", "", EditParser.SCRIPTCODE, SCRIPTCODE_VALUE);
					xmlWriter.writeCharacters(languageValue);
					writeEndElement(xmlReader, xmlWriter);
				}

				// Finally removes the entry in the map.
				this.getFormValues().remove(languageKey);
				this.getFormValues().remove(langcodeValue);
			}
		}
    }
    private String getText(String code){
		ValueStack valueStack = ActionContext.getContext().getValueStack();
		return TextProviderHelper.getText(code, code, valueStack);
    }
}
