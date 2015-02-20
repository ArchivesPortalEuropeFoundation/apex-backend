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
import eu.apenet.dashboard.actions.ajax.EditEadAction.AddableFields;
import eu.apenet.dashboard.actions.ajax.EditEadAction.EditableFields;
import eu.apenet.dashboard.actions.ajax.EditEadAction.UndisplayableFields;
import eu.apenet.dashboard.services.ead.xml.AbstractParser;
import eu.apenet.dpt.utils.util.LanguageIsoList;
import eu.apenet.persistence.vo.CLevel;
import eu.apenet.persistence.vo.EadContent;

/**
 * <p>
 * Support class used to process the information of the XML file and convert it
 * in an HTML representation and viceversa.
 * </p>
 * <p>
 * In this HTML representation it is automatically included the needed changes
 * to let the user edit and/or add more information about the elements which
 * are marked as editable and/or appendable.
 * </p>
 * <p>
 * Once the user end its changes and wants to save them, this class have the
 * necessary methods to performs the needed task of recover the information
 * from the client and store all the edited/added elements, and/or attributes,
 * as the XML of the file which is currently edited.
 * </p><br/>
 * <p>
 * User: Yoann Moranville
 * <p>
 * Date: 06/05/2011
 * </p><br/>
 *
 * @author Yoann Moranville
 */
public class EditParser extends AbstractParser {
	/**
	 * <p>
	 * Constant for the class to use for the log.
	 * </p>
	 */
	private static final Logger LOG = Logger.getLogger(EditParser.class);
	/**
	 * <p>
	 * Constant for the qualified name of the element {@code <c>}.
	 * </p>
	 */
    public final QName C_ELEMENT = new QName(APENET_EAD, "c");
	/**
	 * <p>
	 * Constant for the qualified name of the element {@code <ead>}.
	 * </p>
	 */
    public final QName EAD_ELEMENT = new QName(APENET_EAD, "ead");
	/**
	 * <p>
	 * Constant for the qualified name of the element {@code <language>}.
	 * </p>
	 */
    public final QName LANGUAGE_ELEMENT = new QName(APENET_EAD, "language");
	/**
	 * <p>
	 * Constant to define the desired encoding for the response.
	 * </p>
	 */
    private static final String UTF8 = "UTF-8";
    // Counters to be able to parse all the values.
    // General counter.
    /**
     * <p>
     * Variable to store the general number of elements.
     * </p>
     */
    private int counter = 1;
    // Counter for attribute "@normal" in element <unitdate>.
    /**
     * <p>
     * Variable to store the number of attributes <b>{@code @normal}</b> in
     * element <i>{@code <unitdate>}</i>.
     * </p>
     */
    private int counterUnitdate = 1;
    // Counter for element <titleproper>.
    /**
     * <p>
     * Variable to store the number of elements <b>{@code <titleproper>}</b>.
     * </p>
     */
    private int counterTitleproper = 1;
    // Counter for element <language>.
    /**
     * <p>
     * Variable to store the number of elements <b>{@code <language>}</b>.
     * </p>
     */
    private int counterLanguage = 1;
    // Counter for attribute "@langcode" in element <language>.
    /**
     * <p>
     * Variable to store the number of attributes <b>{@code @langcode}</b> in
     * element <i>{@code <language>}</i>.
     * </p>
     */
    private int counterLangcode = 1;

    // Name of the elements and attributes.
    // Name of element <titlestmt>.
    /**
	 * <p>
     * Constant for the name of the element <b>{@code <titlestmt>}</b>.
	 * </p>
     */
	private static final String TITLESTMT = "titlestmt";
    // Name of element "<titleproper>".
    /**
	 * <p>
     * Constant for the name of the element <b>{@code <titleproper>}</b>.
	 * </p>
     */
	private static final String TITLEPROPER = "titleproper";
    // Name of attribute "@normal" in element <unitdate>.
    /**
	 * <p>
     * Constant for the name of the attribute <b>{@code @normal}</b> of the
     * element <i>{@code <unitdate>}</i>.
	 * </p>
     */
	private static final String NORMAL = "normal";
    // Name of element <unitdate>.
    /**
	 * <p>
     * Constant for the name of the element <b>{@code <unitdate>}</b>.
	 * </p>
     */
	private static final String UNITDATE = "unitdate";
	// Name of element <langmaterial>.
    /**
	 * <p>
     * Constant for the name of the element <b>{@code <langmaterial>}</b>.
	 * </p>
     */
	private static final String LANGMATERIAL = "langmaterial";
	// Name of element <langusage>.
    /**
	 * <p>
     * Constant for the name of the element <b>{@code <langusage>}</b>.
	 * </p>
     */
	private static final String LANGUSAGE = "langusage";
	// Name of element <language>.
    /**
	 * <p>
     * Constant for the name of the element <b>{@code <language>}</b>.
	 * </p>
     */
	private static final String LANGUAGE = "language";
    // Name of attribute "@langcode" in element <language>.
    /**
	 * <p>
     * Constant for the name of the attribute <b>{@code @langcode}</b> of the
     * element <i>{@code <language>}</i>.
	 * </p>
     */
	private static final String LANGCODE = "langcode";
    // Name of attribute "@scriptcode" in element <language>.
    /**
	 * <p>
     * Constant for the name of the attribute <b>{@code @scriptcode}</b> of the
     * element <i>{@code <language>}</i>.
	 * </p>
     */
	private static final String SCRIPTCODE = "scriptcode";
    // Name of attribute "@level" in element <c>.
    /**
	 * <p>
     * Constant for the name of the attribute <b>{@code @level}</b> of the
     * element <i>{@code <c>}</i>.
	 * </p>
     */
	private static final String LEVEL = "level";
    // Name of id for attribute "@normal" in element <unitdate>.
    /**
	 * <p>
     * Constant for the name of the editable and appendable field of the
     * attribute <b>{@code @normal}</b> of the element
     * <i>{@code <unitdate>}</i>.
	 * </p>
     */
	private static final String UNITDATE_NORMAL = "unitdate_normal";
    // Name of id for attribute "@langcode" in element <language>.
    /**
	 * <p>
     * Constant for the name of the editable and appendable field of the
     * attribute <b>{@code @langcode}</b> of the element
     * <i>{@code <language>}</i>.
	 * </p>
     */
	private static final String LANGUAGE_LANGCODE = "language_langcode";

	// Default value of attribute @scriptcode.
	/**
	 * <p>
     * Constant for the default value of the attribute <b>{@code @scriptcode}</b>
     * of the element <i>{@code <language>}</i>.
	 * </p>
	 */
	private static final String SCRIPTCODE_VALUE = "Latn";
	// Default value of namespaceURI for element <language>.
	/**
	 * <p>
     * Constant for the default value of the <b>namespaceURI</b> of the element
     * <i>{@code <language>}</i>.
	 * </p>
	 */
	private static final String LANGUAGE_NAMESPACE_URI = "urn:isbn:1-931666-22-9";
	// Default value of attribute @langcode.
	/**
	 * <p>
     * Constant for the default value of the attribute <b>{@code @langcode}</b>
     * of the element <i>{@code <language>}</i>.
	 * </p>
	 */
	private static final String LANGCODE_VALUE = "none";

	// Variables to check if exists (or no) the necessary elements and attributes. 
	// Element <unitdate>.
	/**
	 * <p>
	 * Variable to specify if the element <b>{@code <unitdate>}</b> is located.
	 * </p>
	 */
	private boolean unitdateLocated = false;
	// Attribute @normal in element <unitdate>.
	/**
	 * <p>
	 * Variable to specify if the attribute <b>{@code @normal}</b> of the
	 * element <i>{@code <unitdate>}</i> is located.
	 * </p>
	 */
	private boolean normalLocated = false;
	// Element <titlestmt>.
	/**
	 * <p>
	 * Variable to specify if the element <b>{@code <titlestmt>}</b> is located.
	 * </p>
	 */
	private boolean titlestmtLocated = false;
	// Element <titleproper>.
	/**
	 * <p>
	 * Variable to specify if the element <b>{@code <titleproper>}</b> is
	 * located.
	 * </p>
	 */
	private boolean titleproperLocated = false;
	// Element <langmaterial> or <langusage>.
	/**
	 * <p>
	 * Variable to specify if the element <b>{@code <langmaterial>}</b> or the
	 * element <b>{@code <langusage>}</b> is located.
	 * </p>
	 */
	private boolean languageSectionLocated = false;
	// Element <language>.
	/**
	 * <p>
	 * Variable to specify if the element <b>{@code <language>}</b> is located.
	 * </p>
	 */
	private boolean languageLocated = false;
	// Attribute @lagcode in element <language>.
	/**
	 * <p>
	 * Variable to specify if the attribute <b>{@code @lagcode}</b> of the
	 * element <i>{@code <language>}</i> is located.
	 * </p>
	 */
	private boolean langcodeLocated = false;

	// Variables to check if the value is added.
	// Element <titleproper>.
	/**
	 * <p>
	 * Variable to specify if the value of the element <b>{@code <titleproper>}</b>
	 * has been added.
	 * </p>
	 */
	private boolean titleproperValueAdded = false;

	// Map with the values.
	/**
	 * <p>
	 * Variable to store all the edited, added and non-modified
	 * information in the client side.
	 * </p>
	 */
	private Map<String, String> formValues;

	// The text value of the element.
	/**
	 * <p>
	 * Variable to store the text value of the item which is currently being
	 * processed.
	 * </p>
	 */
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

	/**
	 * <p>
	 * This method is called from <i>"{@link EditEadAction#getXmlData()}"</i>.
	 * </p>
	 * <p>
	 * It is used to built a {@link String} which will have the representation
	 * of the XML, of the passed object, in an HTML format.
	 * </p>
	 * <p>
	 * This representation includes the needed information to display elements
	 * of type <i>{@code <input type="text">}</i> for those fields which are
	 * editable. Also include elements of type <i>{@code <input type="button">}</i>
	 * for those fields which are appendable.
	 * </p>
	 * <p>
	 * <b>NOTE</b>: Take in mind that not all the available elements/attributes
	 * from the XML are parsed to its HTML display. Only are shown does
	 * elements which are not in the enumeration {@link UndisplayableFields}.
	 * </p>
	 *
	 * @param cLevel {@link CLevel} specifying the level which information
	 * should be displayed.
	 * @param eadContent {@link EadContent} specifying the file which root
	 * information should be displayed.
	 *
	 * @return {@link String} which contains the HTML representation of the XML
	 * from the passed object.
	 *
	 * @throws XMLStreamException Exception while reading the XML information
	 * and generating the HTML.
	 * @throws IOException Exception when accessing the file.
	 *
	 * @see EditParser#writeHiddenLanguageBox(XMLStreamWriter2)
	 * @see EditParser#checkAttributes(String, String)
	 * @see UndisplayableFields#isDisplayable(String)
	 * @see EditableFields#isEditable(String)
	 * @see EditParser#writeCorrectInput(XMLStreamWriter2, String, String, String, CLevel)
	 * @see EditParser#checkEndElement(String, XMLStreamWriter2)
	 * @see EditParser#resetElementsLocated(String)
	 */
	public String xmlToHtml(CLevel cLevel, EadContent eadContent) throws XMLStreamException, IOException {
		LOG.debug("Entering method \"xmlToHtml\".");

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

		LOG.debug("Leaving method \"xmlToHtml\".");

        return stringWriter.toString();
    }

	/**
	 * <p>
	 * This method is called from <i>"{@link EditParser#xmlToHtml(CLevel, EadContent)}"</i>.
	 * </p>
	 * <p>
	 * It is used to process the current element/attribute and create the
	 * needed structure in the HTML in order to enable the edition of the
	 * passed element/attribute.
	 * </p>
	 *
	 * @param xmlWriter {@link XMLStreamWriter2} containing the HTML
	 * representation of the XML.
	 * @param lastElementName {@link String} specifying the name of the current
	 * element of the XML which is under process.
	 * @param attrName {@link String} specifying the name of the current
	 * attribute for the passed element of the XML which is under process.
	 * @param attrValue {@link String} specifying the value of the current
	 * element of the XML which is under process.
	 * @param cLevel {@link CLevel} specifying the level which information
	 * should be displayed.
	 *
	 * @throws XMLStreamException Exception while reading the XML information
	 * and generating the HTML.
	 */
    private void writeCorrectInput(XMLStreamWriter2 xmlWriter, String lastElementName, String attrName, String attrValue, CLevel cLevel) throws XMLStreamException {
		LOG.debug("Entering method \"writeCorrectInput\".");

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

        LOG.debug("Leaving method \"writeCorrectInput\".");
    }

    /**
	 * <p>
	 * This method is called from <i>"{@link EditParser#xmlToHtml(CLevel, EadContent)}"</i>.
	 * </p>
	 * <p>
	 * It is used to create a hidden HTML {@code <select>} element which should
	 * be used as a base to create the duplications when a new <b>language</b>
	 * block will be added.
	 * </p>
     *
     * @param xmlWriter {@link XMLStreamWriter2} containing the HTML
	 * representation of the XML.
     *
     * @throws XMLStreamException Exception while reading the XML information
	 * and generating the HTML.
     */
    private void writeHiddenLanguageBox(XMLStreamWriter2 xmlWriter) throws XMLStreamException {
    	LOG.debug("Entering method \"writeHiddenLanguageBox\".");

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

    	LOG.debug("Leaving method \"writeHiddenLanguageBox\".");
    }

    /**
	 * <p>
	 * This method is called from <i>"{@link EditEadAction#saveXmlData()}"</i>.
	 * </p>
     * <p>
     * It is used to save the <i>edited</i> and/or <i>added</i> elements and
     * its contents in XML of the current part.
     * </p>
     * <p>
     * Once the save process ends, a {@link String} is returned containing a
     * correct XML and, in most of the cases, a valid XML against apeEAD schema.
     * </p>
     * <p>
     * If any error occurs during the process an appropriate exception is
     * thrown.
     * </p>
     *
     * @param xml {@link String} representing the XML which should be updated
     * after the edition.
     * @param formValues {@link Map}{@code <}{@link String}, {@link String}{@code >}
     * containing the values for the editable and appendable fields.
     *
     * @return {@link String} representing the XML in which all the edited and
     * appended elements have been saved.
     *
     * @throws XMLStreamException Exception while reading the XML information
	 * and processing one edited/added by the user.
	 * @throws IOException Exception when trying to read the XML information.
	 *
	 * @see EditParser#checkAttributes(String, String)
	 * @see EditParser#addContent(XMLStreamReader2, XMLStreamWriter2, QName)
	 * @see EditParser#checkCurrentLanguage(XMLStreamWriter2, XMLStreamReader2, QName)
	 * @see EditParser#checkAddedLanguages(XMLStreamWriter2, XMLStreamReader2)
	 * @see EditParser#resetElementsLocated(String)
     */
    public String getNewXmlString(String xml, Map<String, String> formValues) throws XMLStreamException, IOException {
    	LOG.debug("Entering method \"getNewXmlString\".");

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

    	LOG.debug("Leaving method \"getNewXmlString\".");

        return stringWriter.toString();
    }

    /**
	 * <p>
	 * This method is called from <i>"{@link EditParser#getNewXmlString(String, Map)}"</i>.
	 * </p>
     * <p>
     * It is used to process the <b>current</b> <i>element</i> and its
     * <i>attributes</i> checking if its needed to replace the current value in
     * the XML for the new one, edited by the user, or should maintain the
     * current one. 
     * </p>
     * <p>
     * In the case that it is detected that a <b>new</b> <i>element</i> and its
     * <i>attributes</i> has been added by the user, also are processed and
     * added to the new XML in the right location.
     * </p>
     *
     * @param xmlReader {@link XMLStreamReader2} containing the old information
     * for the XML.
     * @param xmlWriter {@link XMLStreamWriter2} containing the new information
     * for the XML.
     * @param element {@link QName} specifying the qualified name of the
     * element which is currently processed.
     *
     * @throws XMLStreamException Exception while modifying the XML information.
     *
     * @see EditParser#checkAttributes(String, String)
     * @see EditParser#isElementChanged(String, String)
     */
    private void addContent(XMLStreamReader2 xmlReader,
			XMLStreamWriter2 xmlWriter, QName element) throws XMLStreamException {
    	LOG.debug("Entering method \"addContent\".");

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

    	LOG.debug("Leaving method \"addContent\".");
	}

    /**
	 * <p>
	 * This method is called from <i>"{@link EditParser#getNewXmlString(String, Map)}"</i>.
	 * </p>
     * <p>
     * It is used to check if the current <i>element</i>, which is the type
     * {@code <language>}, should be maintained without any change, maintained
     * but its value, or the value of any of its attributes, changed or fully
     * removed for the final XML.
     * </p>
     *
     * @param xmlWriter {@link XMLStreamWriter2} containing the new information
     * for the XML.
     * @param xmlReader {@link XMLStreamReader2} containing the old information
     * for the XML.
     * @param element {@link QName} specifying the qualified name of the
     * element which is currently processed.
     *
     * @return {@code boolean} specifying if the value of the element, of any
     * its attributes has been changed or not.
     *
     * @throws XMLStreamException Exception while modifying the XML information.
     *
     * @see EditParser#isElementChanged(String, String)
     * @see EditParser#checkAttributes(String, String)
     */
    private boolean checkCurrentLanguage(XMLStreamWriter2 xmlWriter,
			XMLStreamReader2 xmlReader, QName element) throws XMLStreamException {
    	LOG.debug("Entering method \"checkCurrentLanguage\".");

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

    	LOG.debug("Leaving method \"checkCurrentLanguage\".");

		return result;
	}

    /**
	 * <p>
	 * This method is called from <i>"{@link EditEadAction#addFieldEntry()}"</i>
	 * and from <i>"{@link EditEadAction#writeNewCLevelXmlAndChildren(AddableFields, CLevel, String, boolean)}"</i>.
	 * </p>
     * <p>
     * It is used for modifying the value of the element passed for the new one
     * when needed.
     * </p>
     * <p>
     * <b>Note: This method is currently unused.</b>
     * </p>
     *
     * @param field {@link AddableFields} specifying an element which is marked
     * as appendable.
     * @param xml {@link String} representing the XML which should be changed.
     * @param value {@link String} specifying the new value for the current
     * element.
     *
     * @return {@link String} representing the XML in which all the edited and
     * appended elements have been saved.
     *
     * @throws XMLStreamException Exception while modifying the XML information.
     * @throws IOException Exception when trying to read the XML information.
     *
     * @see EditParser#createTag(XMLStreamWriter2, String, String)
     * @see EditParser#writeEndElement(javax.xml.stream.XMLStreamReader, javax.xml.stream.XMLStreamWriter)
     */
	public String addInLevel(EditEadAction.AddableFields field, String xml, String value) throws XMLStreamException, IOException {
    	LOG.debug("Entering method \"addInLevel\".");

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

    	LOG.debug("Leaving method \"addInLevel\".");

        return stringWriter.toString();
    }

	/**
	 * <p>
	 * This method is called from <i>"{@link EditParser#addInLevel(AddableFields, String, String)}"</i>.
	 * </p>
     * <p>
     * It is used to create the tag for the element passes or the value of the
     * element passed. 
     * </p>
     * <p>
     * <b>Note: This method is currently unused.</b>
     * </p>
	 *
	 * @param writer {@link XMLStreamWriter2} containing the new information
     * for the XML.
	 * @param element {@link String} specifying an element which is currently
	 * processed.
	 * @param value {@link String} specifying the new value for the current
     * element.
	 *
	 * @throws XMLStreamException Exception while modifying the XML information.
	 */
    private void createTag(XMLStreamWriter2 writer, String element, String value) throws XMLStreamException {
    	LOG.debug("Entering method \"createTag\".");

        LOG.info("We create element: " + element);
        writer.writeStartElement(null, element, APENET_EAD);
        if (value == null) {
            LOG.info("But value is null, so not last element");
        } else {
            LOG.info("And value is not null, so last element");
            writer.writeCharacters(value);
        }

    	LOG.debug("Leaving method \"createTag\".");
    }

    /**
	 * <p>
	 * This method is called from <i>"{@link EditParser#addContent(XMLStreamReader2, XMLStreamWriter2, QName)}"</i>
	 * and from <i>"{@link EditParser#checkCurrentLanguage(XMLStreamWriter2, XMLStreamReader2, QName)}"</i>.
	 * </p>
     * <p>
     * It is used to check if the value of the <i>element</i>, or the
     * <i>attribute</i>, passed is in the list of the fields edited/added by
     * the user.
     * </p>
     * <p>
     * If the value is found, before returns it, it is performed the action of
     * unescape the characters which previously has been escaped, before has
     * been sent from client to server.
     * </p>
     * <p>
     * Those characters are: <b>{@code '}</b>, <b>{@code <}</b> and <b>{@code >}</b>.
     * </p>
     *
     * @param xmlElementName {@link String} specifying the name of the element
     * in the original XML.
     * @param xmlAttributeName {@link String} specifying the name of the
     * attribute in the original XML.
     *
     * @return
     * <p>
     * {@link String} which represent the result of the process.
     * </p>
     * <p>
     * Could be one of the follows:
     * </p>
     * <p>
     *  <ul>
     *   <li><b>null</b> - If the value of the element is not changed or the
     *   	element does not exists.</li>
     *   <li><b>Any value</b> - The new value of the element when it is
     *   	changed.</li>
     *  </ul>
     * </p>
     */
    public String isElementChanged(String xmlElementName, String xmlAttributeName) {
    	LOG.debug("Entering method \"isElementChanged\".");

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

        	LOG.debug("Leaving method \"isElementChanged\" when the value of the element is changed.");

    		return value;
    	}

    	LOG.debug("Leaving method \"isElementChanged\" when the value of the element is not changed.");

        return null;
    }

    /**
	 * <p>
	 * This method is called from <i>"{@link EditParser#addContent(XMLStreamReader2, XMLStreamWriter2, QName)}"</i>,
	 * from <i>"{@link EditParser#checkCurrentLanguage(XMLStreamWriter2, XMLStreamReader2, QName)}"</i>,
	 * from <i>"{@link EditParser#getNewXmlString(String, Map)}"</i>
	 * and from <i>"{@link EditParser#xmlToHtml(CLevel, EadContent)}"</i>.
	 * </p>
     * <p>
     * It is used to check the <i>name of the element</i> and the <i>name of
     * the attribute</i> in order to enable or disable the "add attribute"
     * button, thought the enable of the variables for the element located.
     * </p>
     *
     * @param elementName {@link String} specifying the name of the element
     * in the original XML.
     * @param attributeName {@link String} specifying the name of the
     * attribute in the original XML.
     */
    private void checkAttributes(String elementName, String attributeName) {
    	LOG.debug("Entering method \"checkAttributes\".");

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

    	LOG.debug("Leaving method \"checkAttributes\".");
    }

    /**
	 * <p>
	 * This method is called from <i>"{@link EditParser#getNewXmlString(String, Map)}"</i>
	 * and from <i>"{@link EditParser#xmlToHtml(CLevel, EadContent)}"</i>.
	 * </p>
	 * <p>
     * It is used to check the <i>name of the element/attribute</i> in order to
     * disable the variables for the element located when necessary.
	 * </p>
     *
     * @param elementName {@link String} specifying the name of the element, or
     * the name of the attribute, in the original XML.
     */
    private void resetElementsLocated(String elementName) {
    	LOG.debug("Entering method \"resetElementsLocated\".");

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

    	LOG.debug("Leaving method \"resetElementsLocated\".");
    }

    /**
	 * <p>
	 * This method is called from <i>"{@link EditParser#xmlToHtml(CLevel, EadContent)}"</i>.
	 * </p>
	 * <p>
	 * It is used to check if its needed to include a button to add more
	 * elements from the same type of the currently processed element before
	 * close the current parent.
	 * </p>
     *
     * @param elementName {@link String} specifying the name of the current
	 * element of the XML which is under process.
     * @param xmlWriter {@link XMLStreamWriter2} containing the HTML
	 * representation of the XML.
     *
     * @throws XMLStreamException Exception while modifying the XML information.
     */
    private void checkEndElement(String elementName, XMLStreamWriter2 xmlWriter) throws XMLStreamException {
    	LOG.debug("Entering method \"checkEndElement\".");

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

    	LOG.debug("Leaving method \"checkEndElement\".");
    }

    /**
	 * <p>
	 * This method is called from <i>"{@link EditParser#getNewXmlString(String, Map)}"</i>.
	 * </p>
     * <p>
     * It is used to check if there is new language added for the current
     * section and built the information needed, including the element part and
     * value plus the attribute and its value.
     * </p>
     *
     * @param xmlWriter {@link XMLStreamWriter2} containing the new information
     * for the XML.
     * @param xmlReader {@link XMLStreamReader2} containing the old information
     * for the XML.
     *
     * @throws XMLStreamException Exception while modifying the XML information.
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

    /**
	 * <p>
	 * This method is called from <i>"{@link EditParser#checkEndElement(String, XMLStreamWriter2)}"</i>.
	 * </p>
     * <p>
     * It is used to check recover the internationalized text, in the current
     * selected language, for the key which is passed as parameter.
     * </p>
     *
     * @param code {@link String} representing the key of the porperty which
     * should be obtained.
     *
     * @return {@link String} which contains the internationalized text for the
     * passed key.
     *
     * @see TextProviderHelper#getText(String, String, ValueStack)
     */
    private String getText(String code){
		ValueStack valueStack = ActionContext.getContext().getValueStack();
		return TextProviderHelper.getText(code, code, valueStack);
    }
}
