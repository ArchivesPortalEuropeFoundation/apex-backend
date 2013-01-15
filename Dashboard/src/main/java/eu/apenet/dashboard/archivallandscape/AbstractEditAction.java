package eu.apenet.dashboard.archivallandscape;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import eu.apenet.dashboard.security.SecurityContext;
import eu.apenet.persistence.vo.ArchivalInstitution;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.dashboard.AbstractAction;
import eu.apenet.dashboard.Breadcrumb;
import eu.apenet.dashboard.utils.ContentUtils;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.Lang;

public abstract class AbstractEditAction extends AbstractAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2423469704910846783L;
	private String selectedLang;
	private String ALElement;
	private String textAL;
	private String element;
	private List<Institution> list;
	protected Logger log = Logger.getLogger(getClass());
	private String country;
	private String countryCIdentifier;
	private List<Institution> AL;
	private List<Institution> groupList;
	private String father;
	private List<Lang> langList;
	private boolean hasElementChanged = false;

	private boolean edit;
    private Integer countryIdentifier;
    private SecurityContext securityContext;

	@Override
	public void validate() {
		if (this.AL == null) {
			this.AL = new ArrayList<Institution>();
		}
		if (this.groupList == null) {
			this.groupList = new ArrayList<Institution>();
		}
		this.edit = false;
		getLanguagesList(); // It's called all the time
        setCountryIdentifier();
	}

    public void setCountryIdentifier() {
        if(countryIdentifier == null) {
            if(securityContext == null) {
                securityContext = SecurityContext.get();
            }
            countryIdentifier = securityContext.getCountryId();
        }
    }

	@Override
	protected void buildBreadcrumbs() {
		super.buildBreadcrumbs();
		addBreadcrumb(getText("breadcrumb.section.editArchivalLandscape"));
	}

	protected void setList(List<Institution> list) {
		this.list = list;
	}

	protected List<Institution> getList() {
		return this.list;
	}

	public boolean getEdit() {
		return this.edit;
	}

	protected void setEdit(boolean edit) {
		this.edit = edit;
	}

	public String getFather() {
		return father;
	}

	public List<Lang> getLangList() {
		return langList;
	}

	public void setFather(String father) {
		this.father = father;
	}

	public void setGroupList(List<Institution> groupList) {
		this.groupList = groupList;
	}

	public List<Institution> getGroupList() {
		return this.groupList;
	}

	public List<Institution> getAL() {
		return AL;
	}

	protected void setAL(List<Institution> AL) {
		this.AL = AL;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getElement() {
		return element;
	}

	public void setElement(String element) {
		this.element = element;
	}

	public String getALElement() {
		return ALElement;
	}

	public void setALElement(String aLElement) {
		ALElement = aLElement;
	}

	public String getSelectedLang() {
		return selectedLang;
	}

	public void setSelectedLang(String selectedLang) {
		this.selectedLang = selectedLang;
	}

	public void setTextAL(String textAL) {
		this.textAL = textAL;
	}

	public String getTextAL() {
		return textAL;
	}

	public boolean isHasElementChanged() {
		return hasElementChanged;
	}

	public void setHasElementChanged(boolean hasElementChanged) {
		this.hasElementChanged = hasElementChanged;
	}

	/**
	 * Replace the old node with the info written into a institution.
	 * 
	 * @param oldNode
	 * @param insti
	 * @return Node
	 */
	protected Node replaceNode(Node oldNode, Institution insti) {
		NamedNodeMap list = oldNode.getAttributes(); // Should be c attributes
		boolean exit = false;
		for (int i = 0; i < list.getLength() && !exit; i++) {
			if (list.item(i).getNodeName().trim().equals("id")
					&& list.item(i).getNodeValue().trim().equals(insti.getId())) {
				list.item(i).setNodeValue(insti.getId());
				if (insti.getInstitutions() != null) {
					for (int k = 0; k < oldNode.getChildNodes().getLength() && !exit; k++) {
						if (k + 1 == oldNode.getChildNodes().getLength()) { // append
							if (appendNewElement(insti, oldNode) == null) {
								addActionMessage(getText("al.message.someErrors"));
							}
							exit = true;
						}
					}
				} else { // There should not be a child here if
							// getInstitutions() returns null
					for (int k = 0; k < oldNode.getChildNodes().getLength(); k++) {
						if (oldNode.getChildNodes().item(k).getNodeName().equals("c")) {
							oldNode.removeChild(oldNode.getChildNodes().item(k));
						}
					}
				}
			}
		}
		return oldNode; // Changed node
	}

	/**
	 * Navigate by a node looking forward a string. If it matches and returns
	 * state.
	 * 
	 * @param oldNode
	 * @param insti
	 * @return Boolean (found-true or not_found-false)
	 */
	protected Boolean searchInNode(Node oldNode, Institution insti, Document doc) {
		NodeList childNodes = oldNode.getChildNodes();
		for (int g = 0; g < childNodes.getLength(); g++) {
			if (childNodes.item(g).getNodeName() != null) {
				if (childNodes.item(g).getNodeName().equals("did") && childNodes.item(g).getTextContent() != null) {// If
																													// the
																													// system
																													// is
																													// out
																													// of
																													// the
																													// c
																													// levels
																													// children
																													// and
																													// has
																													// content
					if (oldNode.getAttributes() != null && insti != null
							&& oldNode.getAttributes().getNamedItem("id") != null
							&& oldNode.getAttributes().getNamedItem("id").getNodeValue().equals(insti.getId())) {
						replaceNode(oldNode, insti);
						return true;// It's not needed search more, return found
					}
				} else if (childNodes.item(g).getNodeName().equals("c")) { // See
																			// if
																			// there
																			// are
																			// c
																			// child
																			// nodes
																			// into
																			// did
																			// element
					Boolean found = searchInNode(childNodes.item(g), insti, doc);
					if (found != null && found) {
						return true; // found
					}
				}
			}
		}
		return false; // found but doesn't changed
	}

	/**
	 * Appends a new node to an old node according to the Institution insti
	 * parameter. It writes the encodinganalog and type node attributes too but
	 * it's prepared to replaces old nodes. Needs the parent node.
	 * 
	 * @param insti
	 * @param oldNode
	 */
	private Boolean appendNewElement(Institution insti, Node oldNode) {
		Boolean changed = false;
		// First build the node and appends new element, it's control by boolean
		// changed attribute
		Element element = oldNode.getOwnerDocument().createElement("c");
		element.setAttribute("level", insti.getLevel());
		element.setAttribute("id", insti.getId());
		Element didElement = oldNode.getOwnerDocument().createElement("did");
		Element unit = oldNode.getOwnerDocument().createElement("unittitle");
		unit.setAttribute("encodinganalog", "3.1.2");
		unit.setAttribute("type", getSelectedLanguage());
		unit.setTextContent(insti.getName());
		didElement.appendChild(unit);
		element.appendChild(didElement);
		if (insti.getInstitutions() != null) { // Search sublevels
			for (int x = 0; x < insti.getInstitutions().size(); x++) {
				changed = appendNewElement(insti.getInstitutions().get(x), oldNode);
			}
		}
		boolean flag = findIdentifierIntoANode(oldNode, insti.getId());
		if (changed != null && !changed && !flag) {
			if (oldNode.getAttributes() != null && oldNode.getAttributes().getNamedItem("level") != null
					&& !oldNode.getAttributes().getNamedItem("level").getNodeValue().equals("file")) {
				oldNode.appendChild(element);
				changed = true;
			} else {
				changed = null;
			}
		}
		return changed; // returns state
	}

	private boolean findIdentifierIntoANode(Node node, String identifier) {
		boolean flag = false;
		NodeList children = node.getChildNodes();
		for (int i = 0; !flag && i < children.getLength(); i++) {
			NamedNodeMap attributes = children.item(i).getAttributes();
			if (children.item(i).getNodeName().equals("c") && attributes != null
					&& attributes.getNamedItem("id") != null
					&& attributes.getNamedItem("id").getNodeValue().equals(identifier)) {
				flag = true;
			} else if (children.item(i).getNodeName().equals("c")) { // TODO:
																		// check
																		// this
																		// part
				flag = findIdentifierIntoANode(children.item(i), identifier);
			}
		}
		return flag;
	}

	/**
	 * Get the parameter selectedLang and check if it's right, next query to
	 * ddbb which is the language encoded value and returns it. If it's not
	 * right, it returns default value (eng).
	 * 
	 * @return String
	 */
	private String getSelectedLanguage() {
		if (this.selectedLang != null) { // Check if language value is right
			List<Lang> tempList = DAOFactory.instance().getLangDAO().getLanguages(this.selectedLang.toLowerCase());
			if (tempList != null && tempList.size() > 0) {
				return this.selectedLang.toLowerCase();
			}
		}
		return "eng"; // default value
	}

	/**
	 * Creates a new Element from a Document parent and returns the new element.
	 * It writes the node attributes encodinganalog and type.
	 * 
	 * @param insti
	 * @param doc
	 * @return Element
	 */
	protected Element getNewElementDoc(Institution insti, Document doc) {
		Element element = doc.createElement("c");
		element.setAttribute("id", insti.getId());
		element.setAttribute("level", insti.getLevel());
		Element didElement = doc.createElement("did");
		Element unit = doc.createElement("unittitle");
		unit.setAttribute("encodinganalog", "3.1.2");
		unit.setAttribute("type", getSelectedLanguage());
		unit.setTextContent(insti.getName());
		didElement.appendChild(unit);
		element.appendChild(didElement);
		if (insti.institutions != null) {
			for (int x = 0; x < insti.institutions.size(); x++) {
				element.appendChild(getNewElementDoc(insti.getInstitutions().get(x), doc));
			}
		}
		return element;
	}

	/**
	 * Remove a node that match by id.
	 * 
	 * @param alElement
	 * @param list
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 * @throws TransformerException
	 */
	protected boolean remove(String alElement, List<Institution> list) throws ParserConfigurationException,
			SAXException, IOException, TransformerException {
		DocumentBuilder db = EditArchivalLandscapeLogic.buildDocument();
		ArchivalLandscape a = new ArchivalLandscape();
		String path = a.getmyPath(a.getmyCountry()) + "tmp" + APEnetUtilities.FILESEPARATOR + a.getmyCountry()
				+ "AL.xml";
		Document doc = db.parse(path);
		doc.normalizeDocument();
		doc.normalize();
		NodeList cList = doc.getElementsByTagName("c");
		Node nodeTemp = null;// doc.getElementById(alElement);
		for (int i = 0; i < cList.getLength() && nodeTemp == null; i++) {
			if (cList.item(i).hasAttributes() && cList.item(i).getAttributes().getNamedItem("id") != null
					&& cList.item(i).getAttributes().getNamedItem("id").getNodeValue().equals(this.ALElement)) {
				nodeTemp = cList.item(i);
				nodeTemp.getParentNode().removeChild(nodeTemp);// remove node
			}
		}
		if (nodeTemp != null) {
			TransformerFactory tf = TransformerFactory.newInstance(); // Save
																		// the
																		// document
																		// with
																		// changes
			Transformer transformer = tf.newTransformer();
			transformer.transform(new DOMSource(doc), new StreamResult(new File(path)));
			return true;
		}
		return false;
	}

	/**
	 * This method is called by action which controls the up or down of the
	 * institution list.
	 * 
	 * @return String
	 */
	protected String moveAnInstitution(boolean flag) {
		if (this.textAL == null || this.element == null) {
			this.list = EditArchivalLandscapeLogic.navigate(true);
		} else {
			this.list = EditArchivalLandscapeLogic.navigate(false);
		}
		String cadena = null;
		if (this.ALElement != null) {
			cadena = this.ALElement.toString();
		}
		// Looking forward the institution in the list
		Institution insti = null;
		Iterator<Institution> it = this.list.iterator();
		boolean found = false;
		while (!found && it.hasNext()) {
			insti = EditArchivalLandscapeLogic.getInstiById(it.next(), cadena);
			if (insti != null) {
				found = true;
			}
		}

		try {
			Document doc = null;
			ArchivalLandscape a = new ArchivalLandscape();
			String path = a.getmyPath(a.getmyCountry()) + "tmp" + APEnetUtilities.FILESEPARATOR + a.getmyCountry()
					+ "AL.xml";
			this.setCountry(a.getmyCountryName());
			DocumentBuilder db = fillCountryCIdentifier(path);
			if (insti != null) {
				doc = EditArchivalLandscapeLogic.moveNode(path, db, insti, flag);
				log.debug("Starting saving in the temp file...");
				TransformerFactory tf = TransformerFactory.newInstance();
				Transformer transformer = tf.newTransformer();
				transformer.transform(new DOMSource(doc), new StreamResult(new File(path)));
				log.debug("Saved in the temp file");
				this.list = EditArchivalLandscapeLogic.navigate(false);
				parseList(this.list, null);
			} else {
				db = null;
				this.list = EditArchivalLandscapeLogic.navigate(false);
				parseList(this.list, null);
				addActionMessage(getText("al.message.noElementSelectedToMove"));
				return SUCCESS;
			}
		} catch (Exception e) {
			log.error("Moving a Node - " + e.getMessage());
		}
		return SUCCESS;
	}

	protected DocumentBuilder fillCountryCIdentifier(String path) throws ParserConfigurationException, SAXException,
			IOException {
		// in this case is needed load countryCIdentifier before the action
		DocumentBuilder docBuilder = EditArchivalLandscapeLogic.buildDocument();
		Document tempDoc = docBuilder.parse(path);
		tempDoc.normalize();
		tempDoc.normalizeDocument();
		NodeList nodeList = tempDoc.getElementsByTagName("c");
		if (nodeList.getLength() > 0 && (this.countryCIdentifier == null || this.countryCIdentifier.isEmpty())) {
			fillCountryCIdentifier(nodeList.item(0));
		}
		nodeList = null;
		tempDoc = null;
		// end for load countryCIdentifier
		return docBuilder;
	}

	private void fillCountryCIdentifier(Node cElement) {
		NamedNodeMap attributes = cElement.getAttributes();
		for (int i = 0; i < cElement.getAttributes().getLength(); i++) {
			if (attributes.item(i).getNodeName().equals("id")) {
				this.countryCIdentifier = attributes.item(i).getNodeValue();
				break;
			}
		}
	}

	/**
	 * This method parse an institution lists to elements to be showed in the
	 * edition of Archival Landscape
	 */
	protected void parseList(List<Institution> list, String level) {
        for (Integer i = 1, counter = i; list != null && i <= list.size(); i++) {
			Institution insti = list.get(i - 1);
			if (insti != null) {
				Institution institutionTemp = new Institution();
				Integer levels = null;
				if (level != null) {
					if (level.contains(".")) {
						String[] array = (level.split(" ")[0]).split("\\.");
						levels = array.length + 1;
					} else {
						levels = 2;
					}
				} else {
					levels = 1;
				}
				institutionTemp.setDepth(levels);
				if (insti.getLevel().equals("series")) {
					if (level != null) {
						if (insti.getId() != null && !insti.getId().isEmpty()) {
							institutionTemp.setId(insti.getId());
						} else {
							institutionTemp.setId("A"
									+ (System.currentTimeMillis() + "-" + Math.random() * 1000000).toString());
						}
						institutionTemp.setName(level + "." + (counter++) + " " + insti.getName());
						institutionTemp.setGroup(true);
						institutionTemp.setContainsEads(ContentUtils.containsEads(institutionTemp.getId(), countryIdentifier));
						this.AL.add(institutionTemp);
					} else { // Tab should be ""
						if (insti.getId() != null && !insti.getId().isEmpty()) {
							institutionTemp.setId(insti.getId());
						} else {
							institutionTemp.setId(insti.getId());
						}
						institutionTemp.setName((counter++) + " " + insti.getName());
						institutionTemp.setContainsEads(ContentUtils.containsEads(institutionTemp.getId(), countryIdentifier));
						institutionTemp.setGroup(true);
						this.AL.add(institutionTemp);
					}
					if (this.groupList.isEmpty()) {
						Institution temp = new Institution();
						temp.setName(this.country);
						temp.setId(this.countryCIdentifier);
						this.groupList.add(temp);
					}
					this.groupList.add(insti);
					if (insti.getInstitutions() != null) {
						if (level != null) {
							parseList(insti.getInstitutions(), level + "." + (counter - 1));

						} else {
							parseList(insti.getInstitutions(), new Integer(counter - 1).toString());
						}
						institutionTemp.setGroup(true);
					}
				} else {
					if (insti.getId() != null && !insti.getId().isEmpty()) {
						institutionTemp.setId(insti.getId());
					} else {
						institutionTemp.setId(insti.getId());
					}
					institutionTemp.setName(insti.getName());

					institutionTemp.setGroup(false);
					institutionTemp.setContainsEads(ContentUtils.containsEads(institutionTemp.getId(), countryIdentifier));
					this.AL.add(institutionTemp);
				}
			}
		}
	}

	protected Document changeNodeFather(String path, DocumentBuilder docBuilder, Institution insti)
			throws SAXException, IOException {
		Document doc = null;
		try {
			doc = fillCountryCIdentifier(path).parse(path);
			EditArchivalLandscapeLogic.removeWhiteSpaceInXML(doc);
		} catch (Exception e) {
			log.error("Could not be removed the whiteSpaceNodes in temporal AL.xml:" + e.getCause());
		}
		doc.normalize();
		doc.normalizeDocument();
		NodeList nodeList = doc.getElementsByTagName("c");
		Node node = null;
		boolean done = false;
		for (int i = 0; !done && i < nodeList.getLength(); i++) { // First
																	// search
																	// the
																	// reference
																	// node
																	// depends
																	// on
																	// boolean
																	// parameter.
			node = nodeList.item(i);
			NamedNodeMap attributes = node.getAttributes();
			boolean found = false;
			if (attributes.getLength() > 0) {
				for (int j = 0; !found && j < attributes.getLength(); j++) { // looking
																				// forward
																				// c
																				// identifier
					if (i == 0 && attributes.item(j).getNodeName().equals("id")) { // Country
																					// fond
																					// case
						this.countryCIdentifier = attributes.item(j).getNodeValue();
					}
					if (attributes.item(j).getNodeName().equals("id")
							&& attributes.item(j).getNodeValue().equals(insti.getId())) {
						found = true; // It's the C seek
					}
				}
			}
			if (found) { // Search for father and move 'node'
				Node target = null;
				for (int count = 0; !done && count < nodeList.getLength(); count++) {
					target = nodeList.item(count);
					attributes = target.getAttributes();
					if (attributes.getLength() > 0) {
						for (int j = 0; !done && j < attributes.getLength(); j++) { // looking
																					// forward
																					// c
																					// identifier
							if (attributes.item(j).getNodeName().equals("id")
									&& attributes.item(j).getNodeValue().equals(this.father)) {
								target.appendChild(node);
							}
						}
					}
				}
			}
		}
		return doc;
	}

	/**
	 * This method get all c levels and search where is the institution which
	 * will be replaced and return a document edited to be saved. It calls to
	 * searchInNode method.
	 * 
	 * @param route
	 * @param docBuilder
	 * @param insti
	 * @return Document
	 * @throws SAXException
	 * @throws IOException
	 */
	protected Document replace(String route, DocumentBuilder docBuilder, Institution insti) throws SAXException,
			IOException {
		Document doc = docBuilder.parse(route);
		doc.normalize();
		doc.normalizeDocument();
		NodeList nodeList = doc.getElementsByTagName("c"); // get all C levels
		int count = 0;
		Boolean flag = null;
		do {
			flag = null; // flag indicates if the node was found
			for (int j = 0; j < nodeList.getLength() && (flag == null || flag == false); j++) {
				Node node = nodeList.item(j);
				if (node.hasChildNodes()) {
					flag = searchInNode(node, insti, doc);// Navigate
					if (flag != null && flag) { // Check if has been found and
												// if not continue
						break;
					}
				}
			}
		} while (insti.getInstitutions() != null && insti.getInstitutions().size() > count++ && (flag == null || !flag));
		return doc;
	}

	/**
	 * Talk with ddbb and fill the action languages list to be selected in the
	 * edition form.
	 */
	protected void getLanguagesList() {
		this.langList = DAOFactory.instance().getLangDAO().findAll();
		Collections.sort(this.langList);
	}

	/**
	 * Write a list in a DOM document and returns it.
	 * 
	 * @param list
	 * @param route
	 * @throws ParserConfigurationException
	 * @throws IOException
	 * @throws SAXException
	 */
	protected Document write(List<Institution> list, String route) throws ParserConfigurationException, SAXException,
			IOException {
		Document doc = null;
		if ((list != null && list.size() > 0) && this.ALElement != null) {
			// Extract institutions
			Iterator<Institution> i = list.iterator();
			DocumentBuilder docBuilder = EditArchivalLandscapeLogic.buildDocument();
			Institution insti = null;
			while (insti == null && i.hasNext()) {
				insti = EditArchivalLandscapeLogic.getInstiById(i.next(), this.ALElement); // Changed
			}
			// Get the document which it's used to get/set information
			InputStream sfile = new FileInputStream(route);
			doc = docBuilder.parse(sfile);
			doc.normalizeDocument();
			doc.normalize();
			sfile.close();
			NodeList listNodes = doc.getElementsByTagName("c");
			// Access to c node which has level="fonds"
			Node good = null;
			boolean exit = false;
			for (int ii = 0; !exit && ii < listNodes.getLength(); ii++) {
				if (listNodes.item(ii).hasAttributes()) {
					NamedNodeMap listAttributes = listNodes.item(ii).getAttributes();
					for (int j = 0; !exit && j < listAttributes.getLength(); j++) {
						if (listAttributes.item(j).getNodeName().trim().equals("level")
								&& listAttributes.item(j).getNodeValue().trim().equals("fonds")) {
							good = listNodes.item(ii); // It's the node matched
							// good = listAttributes.item(j);
							exit = true;
						}
					}
				}
			}
			// Looking for the level which is equals to insti
			Boolean result = null;
			if (exit) {
				result = searchInNode(good, insti, doc);
			}
			// if not exist create a new one and place the information
			if (result == null || !result) {// IF DOESN'T EXISTS...
				// good is C fond level
				Institution insti2 = new Institution();
				insti2.setName(this.textAL);
				insti2.setLevel(this.element);
				insti2.setId("A" + (System.currentTimeMillis() + "-" + Math.random() * 1000000).toString());
				insti2.setInstitutions(null);
				if (insti.getInstitutions() != null) {
					List<Institution> listInsti = insti.getInstitutions();
					if (listInsti == null) {
						listInsti = new ArrayList<Institution>();
					}
					listInsti.add(insti2);
					insti.setInstitutions(listInsti);
				}
				// Insti is father of new insti2
				doc = replace(route, docBuilder, insti);
			}
		} else {// create a new one (because there isn't a fatherNode)
			Institution insti = new Institution();
			insti.setName(this.textAL);
			insti.setLevel(this.element);
			insti.setId("A" + (System.currentTimeMillis() + "-" + Math.random() * 1000000).toString());
			insti.setInstitutions(null);
			DocumentBuilder docBuilder = EditArchivalLandscapeLogic.buildDocument();
			File file = new File(route);
			InputStream sfile = new FileInputStream(file);
			doc = docBuilder.parse(sfile);
			sfile.close();
			doc = docBuilder.parse(route);
			boolean exit = false;
			NodeList listChilds = doc.getElementsByTagName("c");
			for (int i = 0; !exit && i < listChilds.getLength(); i++) {
				if (listChilds.item(i).hasAttributes()) {
					NamedNodeMap listAttributes = listChilds.item(i).getAttributes();
					for (int j = 0; !exit && j < listAttributes.getLength(); j++) {
						if (listAttributes.item(j).getNodeName().trim().equals("level")
								&& listAttributes.item(j).getNodeValue().trim().equals("fonds")) {
							listChilds.item(i).appendChild(getNewElementDoc(insti, doc));
							// listChilds.item(i).insertBefore(getNewElementDoc(insti,
							// doc), listChilds.item(i).getLastChild());
							exit = true;
						}
					}
				}
			}

		}
		return doc;
	}

	/**
	 * This method calls to write function and places ArrayList<Institution> in
	 * a doc Document and places this document in the file situated on a route.
	 * 
	 * @param list
	 * @param route
	 * @throws XMLStreamException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws TransformerException
	 */
	protected void writeList(List<Institution> list, String route) throws XMLStreamException, IOException,
			ParserConfigurationException, SAXException, TransformerException {
		if (this.textAL != null && this.textAL.length() > 0) {
			Document doc = write(list, route);
			log.debug("OK - Starting saving in a file...");
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer transformer = tf.newTransformer();
			transformer.transform(new DOMSource(doc), new StreamResult(new File(route)));
			log.debug("OK - Saved in a file!");
		}
	}

	/**
	 * It navigates on the list and upload the content adding the new element
	 * 
	 * @return Boolean
	 */
	protected Boolean navigateToUploadList(List<Institution> list) {
		for (int i = 0; list != null && i < list.size(); i++) {
			if (list.get(i).getId().equals(this.ALElement)) {
				if (this.element != null && (this.element.equals("file") || this.element.equals("series"))) {
					Institution insti = new Institution();
					insti.setName(this.textAL);
					insti.setLevel(this.element);
					insti.setId("A" + (System.currentTimeMillis() + "-" + Math.random() * 1000000).toString());
					insti.setInstitutions(null);
					List<Institution> institutions = null;
					if (list.get(i).getInstitutions() != null) {
						institutions = list.get(i).getInstitutions();
					} else {
						institutions = new ArrayList<Institution>();
					}
					institutions.add(insti);
					list.get(i).setInstitutions(institutions);
					return true;
				}
				return false;
			} else if (list.get(i).getInstitutions() != null) {
				Boolean state = navigateToUploadList(list.get(i).getInstitutions());
				if (state != null && state != false) {
					return state;
				}
			}
		}
		return null;
	}

	/**
	 * Make a temp file into tmp folder of an AL, this file will be edited and
	 * in the end will be copied to override original AL.xml.
	 * 
	 * @throws IOException
	 */
	protected void makeTemporal() throws IOException {
		ArchivalLandscape a = new ArchivalLandscape();
		File tempDir = new File(a.getmyPath(a.getmyCountry()) + "tmp");
		if (!tempDir.exists()) {
			tempDir.mkdir();
		} else {
			File[] files = tempDir.listFiles();
			for (int i = 0; i < files.length; i++) {
				// files[i].delete();
				FileUtils.forceDelete(files[i]);
			}
		}
		String path = a.getmyPath(a.getmyCountry()) + "tmp" + APEnetUtilities.FILESEPARATOR + a.getmyCountry()
				+ "AL.xml";
		Reader in = new InputStreamReader(new FileInputStream(a.getmyPath(a.getmyCountry()) + a.getmyCountry()
				+ "AL.xml"), "UTF-8");
		// FileWriter out = new FileWriter(new File(path));
		OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(new File(path), true), "UTF-8");
		int c;
		while ((c = in.read()) != -1) {
			out.write(c);
		}
		in.close();
		out.close();
		// Check if all c elements have an identifier
		try {
			Document doc = fillCountryCIdentifier(path).parse(path);
			doc.normalizeDocument();
			doc.normalize();
			NodeList listTemp = doc.getElementsByTagName("c");
			boolean changes = false;
			for (int i = 0; i < listTemp.getLength(); i++) {
				Element cTemp = (Element) listTemp.item(i);
				if (cTemp.getAttributes() != null
						&& cTemp.getAttributes().getNamedItem("id") == null
						&& cTemp.getAttributes().getNamedItem("level") != null
						&& (cTemp.getAttributes().getNamedItem("level").getTextContent().equals("series") || cTemp
								.getAttributes().getNamedItem("level").getTextContent().equals("file"))) {
					cTemp.setAttribute("id", "A" + System.currentTimeMillis() + "-"
							+ (new Float(Math.random() * 1000000).toString()));
					changes = true;
				}
			}
			if (changes) {
				TransformerFactory tf = TransformerFactory.newInstance();
				Transformer transformer = tf.newTransformer();
				transformer.transform(new DOMSource(doc), new StreamResult(new File(path)));
			}
		} catch (Exception e) {
			log.error("Checking c identifiers in makeTemporal():" + e.getCause(), e);
		}
	}
}
