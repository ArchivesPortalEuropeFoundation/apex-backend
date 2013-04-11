package eu.apenet.dashboard.manual.eag;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import eu.apenet.dashboard.utils.ChangeControl;
import eu.apenet.persistence.dao.ArchivalInstitutionDAO;
import eu.apenet.persistence.vo.ArchivalInstitution;
/**
 * Creates and manages all building proccess related to EAG2012 into Dashboard 
 */
public class Eag2012Creator {
	
    private static final String EAG_XMLNS = "http://www.archivesportaleurope.eu/profiles/APEnet_EAG/";
	private static final String PORTAL_URL = "http://www.archivesportaleurope.net";
	private static final String XML_AUDIENCE = "external";
	private static final String XML_BASE = "http://www.archivesportaleurope.eu/";
    private Eag2012 eag2012;
	private String storagePath;
	private boolean isNew;
	private ArchivalInstitution archivalInstitution;
	private ArchivalInstitutionDAO archivalInstitutionDao;
	private boolean someRepositorguideInformationEmpty;
	private String value;
	private final Logger log = Logger.getLogger(getClass());
	private String eagPath;
	private String id;

	public Eag2012Creator(Eag2012 eag2012) {
		this.eag2012 = eag2012;
	}
	
	public void createEag2012(){
		if(this.eag2012!=null){
			/*NEW DEVELOPMENT - BEGIN TEST */
			Map<String, Object> eagHashMap = buildEagHashMap(); //build EAG structure
	        
	        //check if eagHashMap main node is ok
	        String nodeName = null;
			if(eagHashMap.get("nodeName")!=null && eagHashMap.get("nodeName") instanceof String){
				nodeName = (String)eagHashMap.get("nodeName");
			}
			String nodeValue = null;
			if(eagHashMap.get("nodeValue")!=null && eagHashMap.get("nodeValue") instanceof String){
				nodeValue = (String)eagHashMap.get("nodeValue");
			}
			HashMap<String,String> attributes = null;
			if(eagHashMap.get("attributes")!=null && (eagHashMap.get("attributes") instanceof HashMap)){ //check for hashmap
				attributes = (HashMap<String,String>)eagHashMap.get("attributes");
			}
			List<HashMap<String,?>> eagNodeChildren = null;
			if(eagHashMap.get("children")!=null && eagHashMap.get("children") instanceof List){
				eagNodeChildren = (List<HashMap<String,?>>)eagHashMap.get("children");
			}
			//build EAG-Object-Structure
			Eag2012Node eagNode = buildEagNode(nodeName,nodeValue,attributes,eagNodeChildren);
	        //parse to DOM
	        Document doc = null;
			try {
				doc = generateXMLDocumentFromEagNodes(eagNode);
			} catch (ParserConfigurationException e) {
				log.error("EXCEPTION trying to generate an XML Document from EAGNode",e);
			} 
			//generate XML-DOM Document to be stored from object-eag-structure
	        if(doc!=null){
	        	//Write the XML
	            storeToXML(doc, this.storagePath, this.isNew, this.archivalInstitution, this.archivalInstitutionDao, this.someRepositorguideInformationEmpty, this.value);
	        }else{
	        	log.warn("Couldn't not be stored EAG2012 to XML file, possible reason: is doc null?");
	        }
			/* NEW DEVELOPMENT - END TEST*/
		}
	}
	
	private void storeToXML(Node doc, String storagePath, boolean isNew, ArchivalInstitution archivalInstitution, ArchivalInstitutionDAO archivalInstitutionDao, boolean someRepositorguideInformationEmpty, String value) {
		//Create the new file 
        Source source = new DOMSource(doc);
        Transformer transformer = null;
        try {
        	File eagFile = new File(storagePath);
        	Result result = new StreamResult(eagFile);
            transformer = TransformerFactory.newInstance().newTransformer();
            //tabulation  by levels into XML
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            
            transformer.transform(source, result);
            
            //Finally, the new path, autform and repositorycode are stored in archival_institution table
            if (isNew) {
            	//If the EAG is new, the registration date has to be stored in Data Base
                Date dateNow = new Date();
                archivalInstitution.setRegistrationDate(dateNow);	                	
            }

            archivalInstitution.setEagPath(eagPath);
            archivalInstitution.setAutform(this.eag2012.getAutform());
            archivalInstitution.setRepositorycode(this.getId());
            archivalInstitutionDao.store(archivalInstitution);
            

			log.info("The EAG " + this.eagPath + " has been created and stored in repository");
            ChangeControl.logOperation("Upload eag");
            
            if (someRepositorguideInformationEmpty) {
            	value = "correct_withoutRepositorguideInformation";
            }
            else {
            	value = "correct";
            }
            
        } 
        catch (TransformerConfigurationException e) {
			log.error("Error configuring Transformer during the creation of " + storagePath + " file. " +  e.getMessage());
            value = "error";
        } 
        catch (TransformerFactoryConfigurationError e) {
			log.error("Error configuring Transformer Factory during the creation of " + storagePath + " file. " +  e.getMessage());
            value = "error";
        } 
        catch (TransformerException e) {
			log.error("Transformer error during the creation of " + storagePath + " file. " +  e.getMessage());
            value = "error";
        }
        finally {
        	transformer = null;
        }
	}
	
	private String getId() {
		return this.id;
	}

	/**
	 * Generates a DOM-XML object which is filled with all his node children.
	 * This method doesn't store any file, only returns target filled object. 
	 * 
	 * @param mainEagNode
	 * @return Document (DOM Document)
	 * @throws ParserConfigurationException
	 */
	private Document generateXMLDocumentFromEagNodes(Eag2012Node mainEagNode) throws ParserConfigurationException{
        DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document xmlDocument = documentBuilder.newDocument();

        Element elementNode = xmlDocument.createElement(mainEagNode.getNodeName());
        if(mainEagNode.getAttributes()!=null){
        	HashMap<String, String> attributes = mainEagNode.getAttributes();
        	Iterator<String> keysIterator = attributes.keySet().iterator();
        	while(keysIterator.hasNext()){
        		String key = keysIterator.next();
        		String value = attributes.get(key);
        		elementNode.setAttribute(key, value);
        	}
        }
        if(mainEagNode.getValue()!=null){
        	elementNode.setTextContent(mainEagNode.getValue());
        }
        if(mainEagNode.getChildren()!=null){
        	elementNode = generateChildrenXMLFromEagNode(elementNode,mainEagNode.getChildren(),xmlDocument);
        }
        xmlDocument.appendChild(elementNode);
        return xmlDocument;
	}
	/**
	 * Recursive method used into generateXMLDocumentFromEagNodes to fill father with all of his children.
	 * 
	 * @param elementNode
	 * @param children
	 * @param xmlDocument
	 * @return Element (DOM Node)
	 */
	private Element generateChildrenXMLFromEagNode(Element elementNode,List<Eag2012Node> children, Document xmlDocument) {
		Iterator<Eag2012Node> childrenIterator = children.iterator();
		while(childrenIterator.hasNext()){
			Eag2012Node childEagNode = childrenIterator.next();
			Element childNode = xmlDocument.createElement(childEagNode.getNodeName());
			if(childEagNode.getAttributes()!=null){
	        	HashMap<String, String> attributes = childEagNode.getAttributes();
	        	Iterator<String> keysIterator = attributes.keySet().iterator();
	        	while(keysIterator.hasNext()){
	        		String key = keysIterator.next();
	        		String value = attributes.get(key);
	        		childNode.setAttribute(key, value);
	        	}
	        }
			if(childEagNode.getValue()!=null){
				childNode.setTextContent(childEagNode.getValue());
	        }
			if(childEagNode.getChildren()!=null){
				childNode = generateChildrenXMLFromEagNode(childNode,childEagNode.getChildren(),xmlDocument);
	        }
			elementNode.appendChild(childNode);
		}
		return elementNode;
	}
	
	/**
	 * Method parses plane node elements from xml-form to a structure based on EagNode objects  
	 * 
	 * @param eagNodeName
	 * @param eagNodeValue
	 * @param eagNodeAttributes
	 * @param children
	 * @return EagNode
	 */
	private Eag2012Node buildEagNode(String eagNodeName,String eagNodeValue,HashMap<String,String> eagNodeAttributes,List<HashMap<String,?>> children) {
		Eag2012Node eagNode = new Eag2012Node();
		eagNode.setNodeName(eagNodeName);
		eagNode.setValue(eagNodeValue);
		eagNode.setAttributes(eagNodeAttributes);
		List<Eag2012Node> childrenNode = new ArrayList<Eag2012Node>();
		if(children!=null){
			Iterator<HashMap<String, ?>> childrenIterator = children.iterator();
			while(childrenIterator.hasNext()){
				HashMap<String, ?> child = childrenIterator.next();
				//check what they contain, it's needed in case of 'null' values
				if(child!=null){
					String nodeName = null;
					if(child.get("nodeName")!=null && child.get("nodeName") instanceof String){
						nodeName = (String)child.get("nodeName");
					}
					String nodeValue = null;
					if(child.get("nodeValue")!=null && child.get("nodeValue") instanceof String){
						nodeValue = (String)child.get("nodeValue");
					}
					HashMap<String,String> attributes = null;
					if(child.get("attributes")!=null && (child.get("attributes") instanceof HashMap)){ //check for hashmap
						attributes = (HashMap<String,String>)child.get("attributes");
					}
					List<HashMap<String,?>> eagNodeChildren = null;
					if(child.get("children")!=null && child.get("children") instanceof List){
						eagNodeChildren = (List<HashMap<String,?>>)child.get("children");
					}
					//recurse childNode fill for eagNode
					Eag2012Node childNode = buildEagNode(nodeName,nodeValue,attributes,eagNodeChildren);
					childrenNode.add(childNode);
				}
			}
			eagNode.setChildren(childrenNode);
		}
		return eagNode;
	}
	
	/** EAG2012 creation functions **/
	
	private Map<String,Object> buildEagHashMap() {
		Map<String,Object> eagHashMap = new HashMap<String,Object>();
        
        eagHashMap.put("nodeName","eag");
        HashMap<String, String> eagAttributes = new HashMap<String, String>();
        eagAttributes.put("audience", XML_AUDIENCE);
        eagAttributes.put("xmlns", EAG_XMLNS);
        eagHashMap.put("attributes", eagAttributes);
        eagHashMap.put("nodeValue", null);

        List<HashMap<String,?>> children = new ArrayList<HashMap<String,?>>(); 
        
        children.add(buildControl());       
        children.add(buildArchguide());        
        children.add(buildRelations());
        //end relations
        eagHashMap.put("children", children); //put EAG children nodes
        
        return eagHashMap;
	}

	private HashMap<String, ?> buildRelations() {
		
		HashMap<String, Object> childRelations = new HashMap<String, Object>();
		
		childRelations.put("nodeName", "relations");
		HashMap<String, String> childRelationsAttributes = new HashMap<String, String>();
		childRelationsAttributes.put("xml:base", "http://www.archivesportaleurope.eu/");
		childRelationsAttributes.put("xml:id", "237");
		childRelationsAttributes.put("xml:lang", "mul");
		childRelations.put("attributes", childRelationsAttributes);
		   ArrayList<HashMap<String, ?>> childrenRelations = new ArrayList<HashMap<String,?>>();
		   
		   childrenRelations.add(buildResourceRelation());
		   childrenRelations.add(buildeagRelation());
		  
		
		childRelations.put("children", childrenRelations);
		
		
		return childRelations;
	}

	private HashMap<String, ?> buildResourceRelation() {
		
		HashMap<String, Object> childresourceRelation = new HashMap<String, Object>();
		childresourceRelation.put("nodeName", "resourceRelation");
		HashMap<String, String> childresourceRelationAttributes = new HashMap<String, String>();
		childresourceRelationAttributes.put("resourceRelationType", "creatorOf");
		childresourceRelationAttributes.put("lastDateTimeVerified", "12-10-14");
		childresourceRelationAttributes.put("xml:id", "45");
		childresourceRelationAttributes.put("xml:lang", "spa");
		childresourceRelationAttributes.put("href", "http://censoarchivos.mcu.es/CensoGuía/fondoDetailSession.htm?id&amp;#61;999618");
		
		    //Children of resourceRelation
		    
		    ArrayList<HashMap<String, ?>> childrenresourceRelation = new ArrayList<HashMap<String,?>>();
		
		     childrenresourceRelation.add(buildRelationEntry());
		     childrenresourceRelation.add(buildEagHashMapDescriptiveNote());
		     childrenresourceRelation.add(buildDateSet());
		     childrenresourceRelation.add(buildDate());
		     childrenresourceRelation.add(buildDateRange());
		     childrenresourceRelation.add(buildObjectBinWrap());
		     childrenresourceRelation.add(buildObjectXMLWrap());
		     childrenresourceRelation.add(buildPlaceEntry());
		
		childresourceRelation.put("children", childrenresourceRelation);     
		     
		return childresourceRelation;
	}

	private HashMap<String, ?> buildPlaceEntry() {
		
		HashMap<String, Object> childPlaceEntry = new HashMap<String, Object>();
		childPlaceEntry.put("nodeName", "placeEntry");
		HashMap<String, String> childPlaceEntryAttributes = new HashMap<String,String>();
		childPlaceEntryAttributes.put("accuracy", "specific");
		childPlaceEntryAttributes.put("altitude", "822.782");
		childPlaceEntryAttributes.put("xml:id", "261");
		childPlaceEntryAttributes.put("xml:lang", "eng");
		childPlaceEntryAttributes.put("countryCode", "ES");
		childPlaceEntryAttributes.put("latitude", "42.582234");
		childPlaceEntryAttributes.put("longitude", "-5.565234");
		childPlaceEntryAttributes.put("localType", "localType");
		childPlaceEntryAttributes.put("scriptCode", "Latn");
		childPlaceEntryAttributes.put("transliteration", "http://www.archivesportaleurope.eu/scripts/EAG/");
		childPlaceEntryAttributes.put("vocabularySource", "http://www.archivesportaleurope.eu/vocabularies/EAG/");
		childPlaceEntry.put("attributes", childPlaceEntryAttributes);
		childPlaceEntry.put("nodeValue", "León");
		
		
		return childPlaceEntry;
	}

	private HashMap<String, ?> buildeagRelation() {
		
		HashMap<String, Object> childeagRelation = new HashMap<String, Object>();
		childeagRelation.put("nodeName", "eagRelation");
		HashMap<String, String> childeagRelationAttributes = new HashMap<String,String>();
		childeagRelationAttributes.put("eagRelationType", "associative");
		childeagRelationAttributes.put("href", "http://censoarcivos.mcu.es/CensoGuía/.....");
		childeagRelation.put("attributes", childeagRelationAttributes);
		childeagRelation.put("nodeValue", null);
		   
		    //Children of eagRelation
		    ArrayList<HashMap<String, ?>> childrenRelations = new ArrayList<HashMap<String,?>>();
		    childrenRelations.add(buildRelationEntry());
		    childrenRelations.add(buildEagHashMapDescriptiveNote());
		    
		childeagRelation.put("children", childrenRelations); 
			
		
		return childeagRelation;
	}

	private HashMap<String, ?> buildRelationEntry() {
		
		HashMap<String, Object> childRelation = new HashMap<String, Object>();
		childRelation.put("nodeName", "relationEntry");
		HashMap<String, String> childRelationAttributes = new HashMap<String,String>();
		childRelationAttributes.put("localType", "localType");
		childRelationAttributes.put("scriptCode", "scriptCode");
		childRelationAttributes.put("transliteration", "transliteration");
		childRelationAttributes.put("xml:id", "78");
		childRelationAttributes.put("xml:lang", "eng");
		childRelation.put("attributes", childRelationAttributes);
		childRelation.put("nodeValue", "Consejo supremo de Aragón");
	
		
		return childRelation;
	}

	private HashMap<String, ?> buildControl() {
		HashMap<String, Object> childControl = new HashMap<String, Object>();
        //start control node
        childControl.put("nodeName","control");
        HashMap<String, String> controlAttributes = new HashMap<String,String>();
        controlAttributes.put("xml:base",XML_BASE);
        controlAttributes.put("xml:id",this.eag2012.getControlId());
        controlAttributes.put("xml:lang",this.eag2012.getControlLanguage());
        childControl.put("attributes", controlAttributes);
        
        ArrayList<HashMap<String, ?>> childrenControl = new ArrayList<HashMap<String,?>>();
        
        childrenControl.add(buildRecordId());
        childrenControl.add(buildOtherRecordId());
        childrenControl.add(buildMaintenanceAgency());
        childrenControl.add(buildMaintenanceStatus());
        childrenControl.add(buildMaintenanceHistory());
        childrenControl.add(buildLanguageDeclarations());
        childrenControl.add(buildConventionDeclaration());
        childrenControl.add(buildLocalControl());
        childrenControl.add(buildLocalTypeDeclaration());
        childrenControl.add(buildPublicationStatus());
        childrenControl.add(buildSources());
        
        childControl.put("children",childrenControl);
        return childControl;
	}

	private HashMap<String, ?> buildLocalTypeDeclaration() {
		HashMap<String, Object> childControl = new HashMap<String, Object>();
        childControl.put("nodeName","localTypeDeclaration");
        HashMap<String, String> childControlAttributes = new HashMap<String, String>();
        childControlAttributes.put("xml:id", "44");
        childControlAttributes.put("xml:lang", "eng");
        childControl.put("attributes", childControlAttributes);
        childControl.put("nodeValue", null);
        ArrayList<HashMap<String, Object>> childControlChildren = new ArrayList<HashMap<String,Object>>();
        childControlChildren.add(buildEagHashMapCitation());
        childControlChildren.add(buildEagHashMapAbbreviation());
        childControlChildren.add(buildEagHashMapDescriptiveNote());
        childControl.put("children",childControlChildren);
        return childControl;
	}

	private HashMap<String, ?> buildLocalControl() {
		HashMap<String, Object> childControl = new HashMap<String, Object>();
        childControl.put("nodeName","localControl");
        HashMap<String, String> childControlAttributes = new HashMap<String, String>();
        childControlAttributes.put("localType", "detailLevel");
        childControlAttributes.put("xml:id", "38");
        childControlAttributes.put("xml:lang", "eng");
        childControl.put("attributes", childControlAttributes);
        childControl.put("nodeValue", null);
        
        ArrayList<HashMap<String, Object>> childControlChildren = new ArrayList<HashMap<String,Object>>();
        childControlChildren.add(buildTerm());
        childControlChildren.add(buildDate());
        childControlChildren.add(buildDateRange());
        childControl.put("children",childControlChildren);
        return childControl;
	}

	private HashMap<String, ?> buildConventionDeclaration() {
		HashMap<String, Object> childControl = new HashMap<String, Object>();
        childControl.put("nodeName","conventionDeclaration");
        HashMap<String, String> childControlAttributes = new HashMap<String, String>();
        childControlAttributes.put("xml:id", "33");
        childControlAttributes.put("xml:lang", "eng");
        childControl.put("attributes", childControlAttributes);
        childControl.put("nodeValue", null);

        ArrayList<HashMap<String, Object>> childControlChildren = new ArrayList<HashMap<String,Object>>();
        childControlChildren.add(buildEagHashMapAbbreviation());
        childControlChildren.add(buildEagHashMapCitation());
        childControlChildren.add(buildEagHashMapDescriptiveNote());
        
        childControl.put("children", childControlChildren);
        return childControl;
	}

	private HashMap<String, ?> buildLanguageDeclarations() {
		HashMap<String, Object> childControl = new HashMap<String, Object>();
        childControl.put("nodeName","languageDeclarations");
        childControl.put("attributes", null);
        childControl.put("nodeValue",null);
        
        ArrayList<HashMap<String, Object>> childControlChildren = new ArrayList<HashMap<String,Object>>();
        
	        HashMap<String, Object> childControl1 = new HashMap<String,Object>();
	        childControl1.put("nodeName", "languageDeclaration");
	        HashMap<String, String> childControl1Attributes = new HashMap<String, String>();
	        childControl1Attributes.put("xml:id", "9995");
	        childControl1Attributes.put("xml:lang", "eng");
	        childControl1.put("attributes", childControl1Attributes);
	        childControl1.put("nodeValue", null);
	        
		        ArrayList<HashMap<String, Object>> childControl1Children = new ArrayList<HashMap<String,Object>>();
		        HashMap<String, Object> childControl2 = new HashMap<String, Object>();
		        childControl2.put("nodeName", "language");
		        HashMap<String, String> childControl2Attributes = new HashMap<String, String>();
		        childControl2Attributes.put("languageCode", "spa");
		        childControl2Attributes.put("xml:id", "20");
		        childControl2Attributes.put("xml:lang", "spa");
		        childControl2.put("attributes", childControl2Attributes);
		        childControl2.put("nodeValue", "Español, Castellano");
		        childControl2.put("children", null);
		        
		        childControl1Children.add(childControl2);
		        
		        childControl2 = new HashMap<String, Object>();
		        childControl2.put("nodeName", "script");
		        childControl2Attributes = new HashMap<String, String>();
		        childControl2Attributes.put("scriptCode", "Latn");
		        childControl2Attributes.put("xml:id", "21");
		        childControl2Attributes.put("xml:lang", "spa");
		        childControl2.put("attributes", childControl2Attributes);
		        childControl2.put("nodeValue", "Latina");
		        childControl2.put("children", null);
		        
		        childControl1Children.add(childControl2);
		        
		        childControl2 = buildEagHashMapDescriptiveNote();
		        
		        childControl1Children.add(childControl2);
		        
	        childControl1.put("children", childControl1Children);
        childControlChildren.add(childControl1);
        
        childControl.put("children",childControlChildren);
        return childControl;
	}

	private HashMap<String, ?> buildMaintenanceHistory() {
		HashMap<String, Object> childControl = new HashMap<String, Object>();
        childControl.put("nodeName","maintenanceHistory");
        HashMap<String, String> childControlAttributes = new HashMap<String, String>();
        childControlAttributes.put("xml:id","9");
        childControlAttributes.put("xml:lang","eng");
        childControl.put("attributes",childControlAttributes);
        childControl.put("nodeValue",null);
        ArrayList<HashMap<String, Object>> childControlChildren = new ArrayList<HashMap<String, Object>>();
        HashMap<String, Object> childControlChild = new HashMap<String, Object>();
        childControlChild.put("nodeName", "maintenanceEvent");
        HashMap<String, String> childControlChildAttributes = new HashMap<String, String>();
        childControlChildAttributes.put("xml:id", "10");
        childControlChildAttributes.put("xml:lang", "eng");
        childControlChild.put("attributes", childControlChildAttributes);
        ArrayList<HashMap<String, Object>> childControlChildChildren = new ArrayList<HashMap<String,Object>>();
        HashMap<String, Object> childControlChildChild = new HashMap<String, Object>();
        childControlChildChild.put("nodeName", "agent");
        HashMap<String, String> childControlChildChildAttributes = new HashMap<String, String>();
        childControlChildChildAttributes.put("xml:id", "15");
        childControlChildChildAttributes.put("xml:lang", "eng");
        childControlChildChild.put("attributes", childControlChildChildAttributes);
        childControlChildChild.put("nodeValue", "Automatically created agent");
        childControlChildChild.put("children", null);
        childControlChildChildren.add(childControlChildChild);
        childControlChildChild = new HashMap<String, Object>();
        childControlChildChild.put("nodeName", "agentType");
        childControlChildChildAttributes = new HashMap<String, String>();
        childControlChildChildAttributes.put("xml:id", "17");
        childControlChildChild.put("attributes", childControlChildChildAttributes);
        childControlChildChild.put("nodeValue", "Machine");
        childControlChildChild.put("children", null);
        childControlChildChildren.add(childControlChildChild);
        childControlChildChild = new HashMap<String, Object>();
        childControlChildChild.put("nodeName", "eventDateTime");
        childControlChildChildAttributes = new HashMap<String, String>();
        childControlChildChildAttributes.put("standardDateTime", "2012-06-21T18:17:22");
        childControlChildChildAttributes.put("xml:lang", "eng");
        childControlChildChildAttributes.put("xml:id", "18");
        childControlChildChild.put("attributes", childControlChildChildAttributes);
        childControlChildChild.put("nodeValue", "21.06.2012");
        childControlChildChild.put("children", null);
        childControlChildChildren.add(childControlChildChild);
        childControlChildChild = new HashMap<String, Object>();
        childControlChildChild.put("nodeName", "eventType");
        childControlChildChildAttributes = new HashMap<String, String>();
        childControlChildChildAttributes.put("xml:id", "19");
        childControlChildChild.put("attributes", childControlChildChildAttributes);
        childControlChildChild.put("nodeValue", "revised");
        childControlChildChild.put("children", null);
        childControlChildChildren.add(childControlChildChild);
        childControlChild.put("children", childControlChildChildren);
        childControlChildren.add(childControlChild);
        childControl.put("children", childControlChildren);
        return childControl;
	}

	private HashMap<String, ?> buildMaintenanceStatus() {
		HashMap<String, Object> childControl = new HashMap<String, Object>();
        childControl.put("nodeName","maintenanceStatus");
        HashMap<String, String> childControlAttributes = new HashMap<String, String>();
        childControlAttributes.put("xml:id","8");
        childControl.put("attributes",childControlAttributes);
        childControl.put("nodeValue",/*"revised"*/this.eag2012.getMaintenanceStatus());
        return childControl;
	}

	private HashMap<String, ?> buildMaintenanceAgency() {
		HashMap<String, Object> childControl = new HashMap<String, Object>();
        childControl.put("nodeName","maintenanceAgency");
        HashMap<String, String> childControlAttributes = new HashMap<String, String>();
        childControlAttributes.put("xml:id", "5");
        childControl.put("attributes", childControlAttributes);
        ArrayList<HashMap<String, Object>> childControlChildren = new ArrayList<HashMap<String, Object>>();
        HashMap<String, Object> childControlChild = new HashMap<String, Object>();
        childControlChild.put("nodeName", "agencyCode");
        HashMap<String, String> childControlChildAttributes = new HashMap<String, String>();
        childControlChildAttributes.put("xml:id", "6");
        childControlChild.put("attributes", childControlChildAttributes);
        childControlChild.put("nodeValue", "ES-8019-ACA");
        childControlChild.put("children", null);
        childControlChildren.add(childControlChild);
        childControlChild = new HashMap<String, Object>();
        childControlChild.put("nodeName", "agencyName");
        childControlChildAttributes = new HashMap<String, String>();
        childControlChildAttributes.put("xml:lang", "span");
        childControlChildAttributes.put("xml:id", "7");
        childControlChild.put("attributes",childControlChildAttributes);
        childControlChild.put("nodeValue","Archivo de la corona de Aragón");
        childControlChild.put("children",null);
        childControlChildren.add(childControlChild);
        
        childControlChild = new HashMap<String, Object>();
        childControlChild.put("nodeName", "otherAgencyCode");
        childControlChildAttributes = new HashMap<String, String>();
        childControlChildAttributes.put("localType", "localId");
        childControlChildAttributes.put("xml:id", "5");
        childControlChild.put("attributes",childControlChildAttributes);
        childControlChild.put("nodeValue","ACA");
        childControlChild.put("children",null);
        childControlChildren.add(childControlChild);
        
        childControlChild = buildEagHashMapDescriptiveNote();
        childControlChildren.add(childControlChild);
        
        childControl.put("children",childControlChildren);
        
        return childControl;
	}

	private HashMap<String, ?> buildOtherRecordId() {
		HashMap<String, Object> childControl = new HashMap<String, Object>();
        childControl.put("nodeName","otherRecordId");
        HashMap<String, String> childControlAttributes = new HashMap<String, String>();
        childControlAttributes.put("localType",this.eag2012.getOtherRecordIdLocalType());
        childControlAttributes.put("xml:id",this.eag2012.getOtherRecordIdId());
        childControl.put("attributes",childControlAttributes);
        childControl.put("nodeValue",this.eag2012.getOtherRecordIdValue());
        return childControl;
	}

	private HashMap<String, ?> buildRecordId() {
		HashMap<String, Object> childControl = new HashMap<String, Object>();
        childControl.put("nodeName","recordId");
        
        HashMap<String, String> childControlAttributes = new HashMap<String,String>();
        childControlAttributes.put("xml:id",this.eag2012.getRecordIdId());
        childControl.put("attributes", childControlAttributes);
        childControl.put("children",null);
        childControl.put("nodeValue",this.eag2012.getRecordIdValue());
        return childControl;
	}

	private HashMap<String, ?> buildSources() {
		HashMap<String, Object> childControl = new HashMap<String, Object>();
        childControl.put("nodeName","sources");
       
        HashMap<String, String> childControlAttributes = new HashMap<String, String>();
        childControlAttributes.put("xml:base", XML_BASE);
        childControlAttributes.put("xml:id", this.eag2012.getSourcesId());
        childControlAttributes.put("xml:lang", this.eag2012.getSourcesLang());
        childControl.put("attributes", childControlAttributes);
       
        ArrayList<HashMap<String, Object>> childControlChildren = new ArrayList<HashMap<String,Object>>();
        HashMap<String, Object> childControlChildChild = new HashMap<String, Object>();
        childControlChildChild.put("nodeName", "source");
       
        HashMap<String, String> childControlChildChildAttributes = new HashMap<String, String>();
        childControlChildChildAttributes.put("lastDateTimeVerified", this.eag2012.getSourceLastDateTimeVerified());
        childControlChildChildAttributes.put("xml:id", this.eag2012.getSourceId());
        childControlChildChildAttributes.put("href", this.eag2012.getSourceHref());
        childControlChildChild.put("attributes",childControlChildChildAttributes );
        List<HashMap<String, Object>> childControlChildChildChildren = new ArrayList<HashMap<String,Object>>();
        
        HashMap<String, Object> childControlChildChildChild = new HashMap<String, Object>();
        childControlChildChildChild.put("nodeName","sourceEntry");
        HashMap<String, String> childControlChildChildChildAttributes = new HashMap<String, String>();
        childControlChildChildChildAttributes.put("scriptCode", "Latn");
        childControlChildChildChildAttributes.put("transliteration", "http://www.archivesportaleurope.eu/scripts/EAG/");
        childControlChildChildChildAttributes.put("xml:id","52");
        childControlChildChildChildAttributes.put("xml:lang","eng");
        childControlChildChildChild.put("attributes",childControlChildChildChildAttributes);
        childControlChildChildChild.put("nodeValue","Webpage of the Federal Archives of Germany, viewed 9th July 2012");
        childControlChildChildChildren.add(childControlChildChildChild);
        
        childControlChildChildChildren.add(buildObjectBinWrap());
 
        childControlChildChildChildren.add(buildObjectXMLWrap());
        
        childControlChildChildChildren.add(buildEagHashMapDescriptiveNote());
        
		childControlChildChild.put("children", childControlChildChildChildren);
        childControlChildren.add(childControlChildChild);
        childControl.put("children",childControlChildren);
        return childControl;
	}

	private HashMap<String, Object> buildObjectXMLWrap() {
		
		HashMap<String, Object> childObjectXMLWrap = new HashMap<String, Object>();
        childObjectXMLWrap.put("nodeName","objectXMLWrap");
        HashMap<String, String> childObjectXMLWrapAttributes = new HashMap<String, String>();
        childObjectXMLWrapAttributes.put("xml:id","53879");
        childObjectXMLWrap.put("nodeValue",null);
        childObjectXMLWrap.put("children",null);
        childObjectXMLWrap.put("attributes",childObjectXMLWrapAttributes);
		
		
		return childObjectXMLWrap;
	}

	private HashMap<String, Object> buildObjectBinWrap() {
		
		HashMap<String, Object> childObjectBinWrap = new HashMap<String, Object>();
        childObjectBinWrap.put("nodeName","objectBinWrap");
        HashMap<String, String> childObjectBinWrapAttributes = new HashMap<String, String>();
        childObjectBinWrapAttributes.put("xml:id","53878");
        childObjectBinWrap.put("nodeValue",null);
        childObjectBinWrap.put("children",null);
        childObjectBinWrap.put("attributes",childObjectBinWrapAttributes);
      
	
		return childObjectBinWrap;
	}

	private HashMap<String, ?> buildPublicationStatus() {
		HashMap<String, Object> childControl = new HashMap<String, Object>();
        childControl.put("nodeName","publicationStatus");
        HashMap<String, String> childControlAttributes = new HashMap<String, String>();
        childControlAttributes.put("xml:id", "49");
        childControl.put("attributes", childControlAttributes);
        childControl.put("nodeValue", "approved");
        childControl.put("children",null);
        return childControl;
	}

	private HashMap<String, Object> buildDateRange() {
		HashMap<String, Object> childControlChild = new HashMap<String,Object>();
        childControlChild.put("nodeName", "dateRange");
        HashMap<String, String> childControlChildAttributes = new HashMap<String, String>();
        childControlChildAttributes.put("localType", "localDateType");
        childControlChildAttributes.put("xml:id", "41");
        childControlChildAttributes.put("xml:lang", "eng");
        childControlChild.put("attributes",childControlChildAttributes);
        childControlChild.put("nodeValue", null);
        List<HashMap<String, Object>> childControlChildChildren = new ArrayList<HashMap<String, Object>>();
        HashMap<String, Object> childControlChildChild = new HashMap<String, Object>();
        childControlChildChild.put("nodeName", "fromDate");
        HashMap<String,String> childControlChildChildAttributes = new HashMap<String,String>();
        childControlChildChildAttributes.put("notAfter", "2113");
        childControlChildChildAttributes.put("notBefore", "2013");
        childControlChildChildAttributes.put("standardDate", "1919-09");
        childControlChildChildAttributes.put("xml:id", "6545");
        childControlChildChildAttributes.put("xml:lang", "eng");
        childControlChildChild.put("attributes", childControlChildChildAttributes);
        childControlChildChild.put("nodeValue", "September 1919");
        childControlChildChild.put("children", null);
        childControlChildChildren.add(childControlChildChild);
        
        childControlChildChild = new HashMap<String, Object>();
        childControlChildChild.put("nodeName", "toDate");
        childControlChildChildAttributes = new HashMap<String,String>();
        childControlChildChildAttributes.put("notAfter", "2113");
        childControlChildChildAttributes.put("notBefore", "2013");
        childControlChildChildAttributes.put("standardDate", "1945-04-14");
        childControlChildChildAttributes.put("xml:id", "6543");
        childControlChildChildAttributes.put("xml:lang", "eng");
        childControlChildChild.put("attributes", childControlChildChildAttributes);
        childControlChildChild.put("nodeValue", "14.04.1945");
        childControlChildChild.put("children", null);
        childControlChildChildren.add(childControlChildChild);
        
        childControlChild.put("children", childControlChildChildren);
        return childControlChild;
	}

	private HashMap<String, Object> buildTerm() {
		HashMap<String, Object> childControlChild = new HashMap<String,Object>();
        childControlChild.put("nodeName", "term");
        HashMap<String, String> childControlChildAttributes = new HashMap<String, String>();
        childControlChildAttributes.put("lastDateTimeVerified", "2012-06-29T13:15:27");
        childControlChildAttributes.put("scriptCode", "Latn");
        childControlChildAttributes.put("transliteration", "http://www.archivesportaleurope.eu/scripts/EAG");
        childControlChildAttributes.put("vocabularySource", "http://www.archivesportaleurope.eu/vocabularies/EAG");
        childControlChildAttributes.put("xml:id", "39");
        childControlChildAttributes.put("xml:lang", "eng");
        childControlChild.put("attributes",childControlChildAttributes);
        childControlChild.put("nodeValue", "extended");
        childControlChild.put("children", null);
        return childControlChild;
	}

	private HashMap<String, Object> buildDate() {
		HashMap<String, Object> childControlChild = new HashMap<String,Object>();
        childControlChild.put("nodeName", "date");
        HashMap<String, String> childControlChildAttributes = new HashMap<String, String>();
        childControlChildAttributes.put("localType", "localDateType");
        childControlChildAttributes.put("notAfter", "2115");
        childControlChildAttributes.put("notBefore", "2012");
        childControlChildAttributes.put("standardDate", "2012/2015");
        childControlChildAttributes.put("xml:id", "40");
        childControlChildAttributes.put("xml:lang", "eng");
        childControlChild.put("attributes",childControlChildAttributes);
        childControlChild.put("nodeValue", "August 2012");
        childControlChild.put("children", null);
        return childControlChild;
	}

	private HashMap<String, Object> buildEagHashMapAbbreviation() {
		HashMap<String, Object> childControlChild = new HashMap<String, Object>();
        childControlChild.put("nodeName", "abbreviation");
        HashMap<String, String> childControlChildAttributes = new HashMap<String, String>();
        childControlChildAttributes.put("xml:id", "34");
        childControlChildAttributes.put("xml:lang", "eng");
        childControlChild.put("attributes", childControlChildAttributes);
        childControlChild.put("nodeValue", "ISDIAH");
        childControlChild.put("children", null);
        return childControlChild;
	}
	
	private HashMap<String, Object> buildEagHashMapCitation() {
		HashMap<String, Object> childControlChild = new HashMap<String, Object>();
        childControlChild.put("nodeName", "citation");
        HashMap<String, String> childControlChildAttributes = new HashMap<String, String>();
        childControlChildAttributes.put("xml:id", "35");
        childControlChildAttributes.put("xml:lang", "eng");
        childControlChildAttributes.put("lastDateTimeVerified", "2012-06-29T13:15:27");
        childControlChildAttributes.put("href", "http://www.archivesportaleurope.eu");
        childControlChild.put("attributes", childControlChildAttributes);
        childControlChild.put("nodeValue", "International Standard for Describing Institutions with Archival Holdings");
        childControlChild.put("children", null);
        return childControlChild;
	}

	private HashMap<String, Object> buildEagHashMapDescriptiveNote() {
		HashMap<String, Object> childControlChild = new HashMap<String, Object>();
        childControlChild.put("nodeName", "descriptiveNote");
        HashMap<String, String> childControlChildAttributes = new HashMap<String, String>();
        childControlChildAttributes.put("xml:lang", "ger");
        childControlChildAttributes.put("xml:id", "270");
        childControlChild.put("attributes",childControlChildAttributes);
        List<HashMap<String, Object>> childControlChildChildren = new ArrayList<HashMap<String, Object>>();
        HashMap<String, Object> childControlChildChild = new HashMap<String, Object>();
        childControlChildChild.put("nodeName", "p");
        childControlChildChild.put("children", null);
        HashMap<String,String> childControlChildChildAttributes = new HashMap<String,String>();
        childControlChildChildAttributes.put("xml:id", "271");
        childControlChildChildAttributes.put("xml:lang", "ger");
        childControlChildChild.put("attributes", childControlChildChildAttributes);
        childControlChildChild.put("nodeValue", "In Koblenz wurde das Bundesarchiv 1952 begrüdet");
        childControlChildChildren.add(childControlChildChild);
        childControlChild.put("children", childControlChildChildren);
        return childControlChild;
	}
	
	private HashMap<String, ?> buildArchguide() {
		
		HashMap<String, Object> childArchguide = new HashMap<String, Object>();

		//start archguide node
		
		childArchguide.put("nodeName","archguide");
		childArchguide.put("attributes",null);
		childArchguide.put("nodeValue",null);
		
        ArrayList<HashMap<String, ?>> childrenArchguide = new ArrayList<HashMap<String,?>>();

        childrenArchguide.add(buildIdentity());
        
        childrenArchguide.add(buildDesc());
        
        childArchguide.put("children", childrenArchguide);
        return childArchguide;
        
	}
	
	
	private HashMap<String, ?> buildIdentity() { //TODO, not finished yet
		
		
		    HashMap<String, Object> childArchguide = new HashMap<String, Object>();
	        childArchguide.put("nodeName","identity");
	        childArchguide.put("attributes",null);
			childArchguide.put("nodeValue",null);
			
	       
	        ArrayList<HashMap<String, Object>> childArchguideChildren = new ArrayList<HashMap<String, Object>>();
	        
	        HashMap<String, Object> childArchguide1 = new HashMap<String, Object>();
	       
	        childArchguide1.put("nodeName", "repositorid");
	        HashMap<String, String> childArchguide1Attributes = new HashMap<String, String>();
	        childArchguide1Attributes.put("countrycode", "DE");
	        childArchguide1Attributes.put("repositorycode", "DE-00000000000001");  
	        childArchguide1.put("attributes", childArchguide1Attributes);
	        childArchguide1.put("nodeValue", null);
	        childArchguide1.put("children", null);
	        
	        childArchguideChildren.add(childArchguide1);
	        
	        childArchguide1 = new HashMap<String, Object>();
	        childArchguide1.put("nodeName", "otherRepositorId");
	        childArchguide1.put("nodeValue", "BArch");
	        
	        childArchguideChildren.add(childArchguide1);
	        
	        childArchguide1 = new HashMap<String, Object>();
	        childArchguide1.put("nodeName", "autform");
	        childArchguide1Attributes = new HashMap<String, String>();
	        childArchguide1Attributes.put("xml:lang", "ger");
	        childArchguide1.put("attributes", childArchguide1Attributes);
	        childArchguide1.put("nodeValue", "Bundesarchiv");
	        childArchguide1.put("children", null);
	        
	        childArchguideChildren.add(childArchguide1);
	        
	        childArchguide1 = new HashMap<String, Object>();
	        childArchguide1.put("nodeName", "parform");
	        childArchguide1Attributes = new HashMap<String, String>();
	        childArchguide1Attributes.put("xml:lang", "eng");
	        childArchguide1.put("attributes", childArchguide1Attributes);
	        childArchguide1.put("nodeValue", "Federal Archives of Germany");
	        childArchguide1.put("children", null);
	        
	        childArchguideChildren.add(childArchguide1);
	        
	        childArchguideChildren.add(buildNonpreform());
	        
	        childArchguide1 = new HashMap<String, Object>();
	        childArchguide1.put("nodeName", "repositoryType");
	        childArchguide1.put("attributes", null);
	        childArchguide1.put("nodeValue", "National Archives");
	        childArchguide1.put("children", null);
	         
	        childArchguideChildren.add(childArchguide1);
	        
	        childArchguide.put("children", childArchguideChildren);
	        
	        return childArchguide;
		
		
	}

	private HashMap<String, Object> buildNonpreform(){ 
		
		HashMap<String, Object> childArchguide1 = new HashMap<String, Object>();
        childArchguide1.put("nodeName", "nonpreform");
        HashMap<String, String> childArchguide1Attributes = new HashMap<String, String>();
        childArchguide1Attributes.put("xml:lang", "ger");
        childArchguide1.put("attributes", childArchguide1Attributes);
        childArchguide1.put("nodeValue", "Reichsararchiv");
        
	        List<HashMap<String, Object>> childArchguide1Children = new ArrayList<HashMap<String,Object>>();
	        
	        HashMap<String, Object> childArchguide2 = new HashMap<String, Object>();
	        childArchguide2.put("nodeName", "useDates");
	        HashMap<String, String> childArchguide2Attributes = new HashMap<String, String>();
	        childArchguide2Attributes.put("xml:id", "57");
	        childArchguide2Attributes.put("xml:lang", "mul");
	        childArchguide2.put("attributes", childArchguide2Attributes);
	        childArchguide2.put("nodeValue", null);
	        
	        	List<HashMap<String, Object>> childArchguide2Children = new ArrayList<HashMap<String,Object>>();
	        	childArchguide2Children.add(buildDateSet());
	        
			        
	        childArchguide2.put("children", childArchguide2Children);
	        
	        childArchguide1Children.add(childArchguide2);
	        
        childArchguide1.put("children", childArchguide1Children);
        
		return childArchguide1;
	}

	private HashMap<String, Object> buildDateSet() {
		
		HashMap<String, Object> childArchguide3 = new HashMap<String, Object>();
    	childArchguide3.put("nodeName","dateSet");
    	
    	HashMap<String, String> childArchguide3Attributes = new HashMap<String, String>();

    	childArchguide3Attributes.put("localType", "localDateType");
    	childArchguide3Attributes.put("xml:id", "58");
    	childArchguide3Attributes.put("xml:lang", "mul");
    	
    	childArchguide3.put("attributes",childArchguide3Attributes);
    	childArchguide3.put("nodeValue",null);
    	
        	//dateSet children	        
	        List<HashMap<String, Object>> childArchguide3Children = new ArrayList<HashMap<String,Object>>();
	        childArchguide3Children.add(buildDate());
	        childArchguide3Children.add(buildDateRange());
	        
        childArchguide3.put("children",childArchguide3Children);    
	        
		return childArchguide3;
	}

	private HashMap<String, ?> buildDesc() { // TODO Auto-generated method stub
		
		 HashMap<String, Object> childArchguide = new HashMap<String, Object>();
	        childArchguide.put("nodeName","desc");
	        
	        childArchguide.put("attributes",null);
			childArchguide.put("nodeValue",null);
			
	        ArrayList<HashMap<String, Object>> childArchguideChildren = new ArrayList<HashMap<String, Object>>();
	        childArchguideChildren.add(buildRepositories());
	        

		  childArchguide.put("children", childArchguideChildren);
		
		
		 return childArchguide;
		 
	}

	

	private HashMap<String, Object> buildRepositories() {
		
		HashMap<String, Object> childArchguide1 = new HashMap<String, Object>();
		childArchguide1.put("nodeName", "repositories");
        childArchguide1.put("attributes",null);
		childArchguide1.put("nodeValue",null);
		
		ArrayList<HashMap<String, Object>> childArchguide1Children = new ArrayList<HashMap<String, Object>>();
        
        HashMap<String, Object> childArchguide2 = new HashMap<String, Object>();
        
        childArchguide2.put("nodeName", "repository");
        childArchguide2.put("attributes",null);
		childArchguide2.put("nodeValue",null);
		
        
     		ArrayList<HashMap<String, Object>> childArchguide2Children = new ArrayList<HashMap<String, Object>>();
            
     		HashMap<String, Object> childArchguide3 = new HashMap<String, Object>();
            childArchguide3.put("nodeName", "repositoryName");
            HashMap<String, String> childArchguide3Attributes = new HashMap<String, String>();
        	childArchguide3Attributes.put("xml:lang", "ger");
        	childArchguide3.put("attributes",childArchguide3Attributes);
        	childArchguide3.put("nodeValue", "Bundesarchiv Roblenz");
        	
        	childArchguide2Children.add(childArchguide3);
        	
		    childArchguide3 = new HashMap<String, Object>();
		    childArchguide3.put("nodeName", "repositoryRole");
		    childArchguide3.put("attributes", null);
		    childArchguide3.put("nodeValue", "Headquarter");
		    
		    childArchguide2Children.add(childArchguide3);
		    
		    childArchguide3 = new HashMap<String, Object>();
		    childArchguide3.put("nodeName", "geogarea");
		    childArchguide3Attributes = new HashMap<String, String>();
        	childArchguide3Attributes.put("xml:lang", "eng");
        	childArchguide3.put("attributes",childArchguide3Attributes);
        	childArchguide3.put("nodeValue", "Europe");
        	
        	childArchguide2Children.add(childArchguide3);
        	
        	childArchguide3 = new HashMap<String, Object>();
        	childArchguide3.put("nodeName", "location");
        	childArchguide3Attributes = new HashMap<String, String>();
        	childArchguide3Attributes.put("localType", "visitors address");
        	childArchguide3Attributes.put("latitude", "50.342780");
        	childArchguide3Attributes.put("longitude", "7.572900");
        	childArchguide3.put("attributes",childArchguide3Attributes);
        	childArchguide3.put("nodeValue", null);
        	

        	  
        	  ArrayList<HashMap<String, Object>> childArchguide3Children = new ArrayList<HashMap<String, Object>>();
        	  
        	  HashMap<String, Object> childArchguide4 = new HashMap<String, Object>();
        	  childArchguide4.put("nodeName", "country");
              HashMap<String, String> childArchguide4Attributes = new HashMap<String, String>();
              childArchguide4Attributes.put("xml:lang", "ger");
              childArchguide4.put("attributes", childArchguide4Attributes);
              childArchguide4.put("nodeValue", "Deutschland");
              
              childArchguide3Children.add(childArchguide4);
              
              childArchguide4 = new HashMap<String, Object>();
              childArchguide4.put("nodeName","firstdem");
              childArchguide4Attributes = new HashMap<String, String>();
              childArchguide4Attributes.put("xml:lang", "ger");
              childArchguide4.put("attributes", childArchguide4Attributes);
              childArchguide4.put("nodeValue", "Rheinland-Pfalz");
              
              childArchguide3Children.add(childArchguide4);
              
              childArchguide4 = new HashMap<String, Object>();
              childArchguide4.put("nodeName","secondem");
              childArchguide4Attributes = new HashMap<String, String>();
              childArchguide4Attributes.put("xml:lang", "ger");
              childArchguide4.put("attributes", childArchguide4Attributes);
              childArchguide4.put("nodeValue", "Kreisfreie Stadt Koblenz");
              
              childArchguide3Children.add(childArchguide4);
              
              childArchguide4 = new HashMap<String, Object>();
              childArchguide4.put("nodeName","municipalityPostalcode");
              childArchguide4Attributes = new HashMap<String, String>();
              childArchguide4Attributes.put("xml:lang", "ger");
              childArchguide4.put("attributes", childArchguide4Attributes);
              childArchguide4.put("nodeValue", "56075 Koblenz");
              
              childArchguide3Children.add(childArchguide4);
              
              childArchguide4 = new HashMap<String, Object>();
              childArchguide4.put("nodeName","localentity");
              childArchguide4Attributes = new HashMap<String, String>();
              childArchguide4Attributes.put("xml:lang", "ger");
              childArchguide4.put("attributes", childArchguide4Attributes);
              childArchguide4.put("nodeValue", "Karthause");
              
              childArchguide3Children.add(childArchguide4);
              
              childArchguide4 = new HashMap<String, Object>();
              childArchguide4.put("nodeName","street");
              childArchguide4Attributes = new HashMap<String, String>();
              childArchguide4Attributes.put("xml:lang", "ger");
              childArchguide4.put("attributes", childArchguide4Attributes);
              childArchguide4.put("nodeValue", "Potsdamer Strañe 1");
              
              childArchguide3Children.add(childArchguide4);
              
              childArchguide3.put("children", childArchguide3Children);
           
              childArchguide2Children.add(childArchguide3);
            
           
           childArchguide2Children.add(buildTelephone());     
           childArchguide2Children.add(buildFax());
           childArchguide2Children.add(buildEmail());   
           childArchguide2Children.add(buildWebpage());    
           
           childArchguide3 = new HashMap<String, Object>();
           childArchguide3.put("nodeName", "directions");
           childArchguide3Attributes = new HashMap<String, String>();
       	   childArchguide3Attributes.put("xml:lang", "ger");
           childArchguide3.put("attributes",childArchguide3Attributes);
       	   childArchguide3.put("nodeValue", "Buslinien 2 und 12................");
       	      childArchguide3Children = new ArrayList<HashMap<String, Object>>();
   	  
	          childArchguide3Children.add(buildEagHashMapCitation());
	       
	       childArchguide3.put("children", childArchguide3Children);
       
           childArchguide2Children.add(childArchguide3);   
       	 
       	   
       	   childArchguide3 = new HashMap<String, Object>();
           childArchguide3.put("nodeName", "repositorhist");
           childArchguide3.put("attributes",null);
    	   childArchguide3.put("nodeValue", null);
    	
    	       childArchguide3Children = new ArrayList<HashMap<String, Object>>();
     	        childArchguide3Children.add(buildEagHashMapDescriptiveNote());
     	       
     	   childArchguide3.put("children", childArchguide3Children);   
           childArchguide2Children.add(childArchguide3);  
           
           childArchguide3 = new HashMap<String, Object>();
           childArchguide3.put("nodeName", "repositorfound");
           childArchguide3.put("attributes",null);
    	   childArchguide3.put("nodeValue", null);
    	
    	       childArchguide3Children = new ArrayList<HashMap<String, Object>>();   
    	       
    	         childArchguide3Children.add(buildDate());
                 childArchguide3Children.add(buildRule());    	       
    	      
               childArchguide3.put("children", childArchguide3Children);
                 
               childArchguide2Children.add(childArchguide3);   
             	   
               
            childArchguide3 = new HashMap<String, Object>();
            childArchguide3.put("nodeName", "repositorsup");
            childArchguide3.put("attributes",null);
            childArchguide3.put("nodeValue", null);
        	
        	   childArchguide3Children = new ArrayList<HashMap<String, Object>>();   
        	       
        	      childArchguide3Children.add(buildDate());
                  childArchguide3Children.add(buildRule());    	       
        	      
                childArchguide3.put("children", childArchguide3Children);
                     
                childArchguide2Children.add(childArchguide3);    
		    
		
            childArchguide3 = new HashMap<String, Object>();
                childArchguide3.put("nodeName", "buildinginfo");
                childArchguide3.put("attributes",null);
                childArchguide3.put("nodeValue", null);
            	
            	   childArchguide3Children = new ArrayList<HashMap<String, Object>>();   
            	      
            	       childArchguide4 = new HashMap<String, Object>();
             	       childArchguide4.put("nodeName", "building");
                       childArchguide4.put("attributes", null);
                       childArchguide4.put("nodeValue", null);
                     
                         ArrayList<HashMap<String, Object>> childArchguide4Children = new ArrayList<HashMap<String, Object>>();
                          childArchguide4Children.add(buildEagHashMapDescriptiveNote());
                     
                       childArchguide4.put("children",childArchguide4Children);
                     childArchguide3Children.add(childArchguide4);
                     
               	       childArchguide4 = new HashMap<String, Object>();
               	       childArchguide4.put("nodeName", "repositorarea");
               	       childArchguide4.put("attributes", null);
               	       childArchguide4.put("nodeValue", null);
               	         childArchguide4Children = new ArrayList<HashMap<String, Object>>();
               	         childArchguide4Children.add(buildNum("squaremetre"));
                                        	       
                        childArchguide4.put("children",childArchguide4Children);
                     childArchguide3Children.add(childArchguide4);
                       
                        childArchguide4 = new HashMap<String, Object>();
               	        childArchguide4.put("nodeName", "lengthshelf");
               	        childArchguide4.put("attributes", null);
            	        childArchguide4.put("nodeValue", null);
            	           childArchguide4Children = new ArrayList<HashMap<String, Object>>();
            	           childArchguide4Children.add(buildNum("linearmetre"));
            	           
            	         childArchguide4.put("children",childArchguide4Children);
            	      childArchguide3Children.add(childArchguide4); 
            	       
            	       
            	   childArchguide3.put("children", childArchguide3Children);      
            	   childArchguide2Children.add(childArchguide3);   
            	   
            	   //Children of repository, adminhierarchy
            	   childArchguide3 = new HashMap<String, Object>();
                   childArchguide3.put("nodeName", "adminhierarchy");
                   childArchguide3.put("attributes",null);
                   childArchguide3.put("nodeValue", null); 
                      
                      //Children of adminhierarchy
                      childArchguide3Children = new ArrayList<HashMap<String, Object>>();   
         	      
        	            childArchguide4 = new HashMap<String, Object>();
         	            childArchguide4.put("nodeName", "adminunit");
                        childArchguide4Attributes = new HashMap<String, String>();
                        childArchguide4Attributes.put("xml:lang", "ger");
                        childArchguide4.put("attributes", childArchguide4Attributes);
                        childArchguide4.put("nodeValue", "Parrafada");
                        
                        childArchguide3Children.add(childArchguide4);                        
            	   childArchguide3.put("children", childArchguide3Children);
            	   childArchguide2Children.add(childArchguide3);   
            	   
            	   //Children holdings, children of repository
            	   childArchguide3 = new HashMap<String, Object>();
                   childArchguide3.put("nodeName", "holdings");
                   childArchguide3.put("attributes",null);
                   childArchguide3.put("nodeValue", null); 
                     
                     //Children of holdings
                     childArchguide3Children = new ArrayList<HashMap<String, Object>>();   
            	      childArchguide3Children.add(buildEagHashMapDescriptiveNote());
            	      childArchguide3Children.add(buildDateSet());
            	      childArchguide4 = new HashMap<String, Object>();
            	      childArchguide4.put("nodeName", "extent");
            	      childArchguide4.put("attributes", null);
            	      childArchguide4.put("nodeValue", null);
            	      
            	           //Children of extent
            	           childArchguide4Children = new ArrayList<HashMap<String, Object>>();
       	                   childArchguide4Children.add(buildNum("linearmetre"));
       	           
            	      childArchguide4.put("children", childArchguide4Children);
            	      childArchguide3Children.add(childArchguide4);
            	    childArchguide3.put("children", childArchguide3Children);
            	    childArchguide2Children.add(childArchguide3);   
            	    
            	    //Children of repository, timetable
            	    
            	    childArchguide3 = new HashMap<String, Object>();
                    childArchguide3.put("nodeName", "timetable");
                    childArchguide3.put("attributes",null);
                    childArchguide3.put("nodeValue", null); 
            	         
                       //Children of timetable
                       childArchguide3Children = new ArrayList<HashMap<String, Object>>();   
           	           childArchguide4 = new HashMap<String, Object>();
           	           childArchguide4.put("nodeName", "opening");
           	           childArchguide4.put("attributes", null);
           	           childArchguide4.put("nodeValue", "Mo. -Do.: 08:00 Uhr - 19:00, ......");
            	       
           	           childArchguide3Children.add(childArchguide4);
           	           
           	           childArchguide4 = new HashMap<String, Object>();
        	           childArchguide4.put("nodeName", "closing");
        	           childArchguide4Attributes = new HashMap<String, String>();
        	           childArchguide4Attributes.put("standardDate","16-12-13");
        	           childArchguide4.put("attributes", childArchguide4Attributes);
        	           childArchguide4.put("nodeValue", "an Wochenenden un nationalen...");
         	       
        	           childArchguide3Children.add(childArchguide4);
            	    childArchguide3.put("children", childArchguide3Children);
            	    childArchguide2Children.add(childArchguide3);
            	    
            	    //Children of repository, access
            	    
            	    childArchguide3 = new HashMap<String, Object>();
                    childArchguide3.put("nodeName", "access");
                    childArchguide3Attributes = new HashMap<String, String>();
     	            childArchguide3Attributes.put("question","yes");
     	            childArchguide3.put("attributes", childArchguide3Attributes);
                    childArchguide3.put("nodeValue", null); 
                    
                        //children of access
                        
                       childArchguide3Children = new ArrayList<HashMap<String, Object>>();   
        	           childArchguide4 = new HashMap<String, Object>();
        	           childArchguide4.put("nodeName", "restaccess");
        	           childArchguide4Attributes = new HashMap<String, String>();
        	           childArchguide4Attributes.put("xml:lang","eng");
        	           childArchguide4.put("attributes", childArchguide4Attributes);
        	           childArchguide4.put("nodeValue", "To prepare for your personal use of the reference service......");
         	       
        	           childArchguide3Children.add(childArchguide4);
            	   
        	           childArchguide4 = new HashMap<String, Object>();
        	           childArchguide4.put("nodeName", "termsOfUse");
        	           childArchguide4Attributes = new HashMap<String, String>();
        	           childArchguide4Attributes.put("href","http://www.bundesarchiv.de/bernutzung/voraussetzungen/");
        	           childArchguide4Attributes.put("xml:lang","eng");
        	           childArchguide4.put("attributes", childArchguide4Attributes);
        	           childArchguide4.put("nodeValue", "General information on access and use");
         	       
        	           childArchguide3Children.add(childArchguide4);
        
        	        childArchguide3.put("children", childArchguide3Children);
               	    childArchguide2Children.add(childArchguide3);
               	    
               	    //Children of repository, accessibility
               	    childArchguide3 = new HashMap<String, Object>();
                    childArchguide3.put("nodeName", "accessibility");
                    childArchguide3Attributes = new HashMap<String, String>();
  	                childArchguide3Attributes.put("question","yes"); //mandatory
  	                childArchguide3Attributes.put("xml:lang","eng");
  	                childArchguide3.put("attributes", childArchguide3Attributes);
                    childArchguide3.put("nodeValue", "Allocated parking spaces, lifts for persons with disabilities "); 
                    
                    childArchguide2Children.add(childArchguide3); 
                    
                    //Children of repository, services
                    childArchguide3 = new HashMap<String, Object>();
                    childArchguide3.put("nodeName", "services");
     	            childArchguide3.put("attributes", null);
                    childArchguide3.put("nodeValue", null); 
                        
                           //Children of services
                           childArchguide3Children = new ArrayList<HashMap<String, Object>>();   
     	                   childArchguide4 = new HashMap<String, Object>();
     	                   childArchguide4.put("nodeName", "searchroom");
     	                   childArchguide4.put("attributes", null);
     	                   childArchguide4.put("nodeValue", null);
      	                    
     	                   //Children of searchroom
     	                    childArchguide4Children = new ArrayList<HashMap<String, Object>>(); 
     	                    childArchguide4Children.add(buildContact()); //contact
     	                    
    	                    HashMap<String, Object> childArchguide5 = new HashMap<String, Object>();
    	                    childArchguide5.put("nodeName", "workPlaces");  //workPlaces
    	                    childArchguide5.put("attributes", null);
      	                    childArchguide5.put("nodeValue", null);
      	                      ArrayList<HashMap<String, Object>> childArchguide5Children = new ArrayList<HashMap<String, Object>>(); 
     	                      childArchguide5Children.add(buildNum("site"));
     	                    
     	                      childArchguide5.put("children", childArchguide5Children);
     	                    childArchguide4Children.add(childArchguide5);
     	                   
     	                    childArchguide5 = new HashMap<String, Object>();
  	                        childArchguide5.put("nodeName", "computerPlaces");  //computerPlaces
  	                        childArchguide5.put("attributes", null);
    	                    childArchguide5.put("nodeValue", null);
    	                      childArchguide5Children = new ArrayList<HashMap<String, Object>>(); 
   	                          childArchguide5Children.add(buildNum("site"));
   	                          childArchguide5Children.add(buildEagHashMapDescriptiveNote());
   	                        
   	                        childArchguide5.put("children", childArchguide5Children);
    	                    childArchguide4Children.add(childArchguide5);
    	                    
    	                    childArchguide5 = new HashMap<String, Object>();
  	                        childArchguide5.put("nodeName", "microfilmPlaces");  //microfilmPlaces
  	                        childArchguide5.put("attributes", null);
    	                    childArchguide5.put("nodeValue", null);
    	                      childArchguide5Children = new ArrayList<HashMap<String, Object>>(); 
   	                          childArchguide5Children.add(buildNum("site"));
    	                    
   	                        childArchguide5.put("children", childArchguide5Children);
   	                        childArchguide4Children.add(childArchguide5);
   	                        
   	                        childArchguide4Children.add(buildWebpage());  //webpage
   	                        
   	                        childArchguide5 = new HashMap<String, Object>();
	                        childArchguide5.put("nodeName", "photographAllowance");  //photographAllowance
	                        childArchguide5.put("attributes", null);
 	                        childArchguide5.put("nodeValue", "no");
 	                        childArchguide4Children.add(childArchguide5);
 	                        
 	                        childArchguide5 = new HashMap<String, Object>();
	                        childArchguide5.put("nodeName", "readersTicket");  //readersTicket
	                        HashMap<String, String> childArchguide5Attributes = new HashMap<String, String>();
	                        childArchguide5Attributes.put("href", "http://www.bundesarchiv.de/imperia/md/content/..");
	                        childArchguide5Attributes.put("xml:lang", "eng");
	                        childArchguide5.put("attributes", childArchguide5Attributes);
	                        childArchguide5.put("nodeValue", "Request for Use (German)");
	                        childArchguide4Children.add(childArchguide5);
	                        
	                        childArchguide5 = new HashMap<String, Object>();
	                        childArchguide5.put("nodeName", "advancedOrders");  //advancedOrders
	                        childArchguide5Attributes = new HashMap<String, String>();
	                        childArchguide5Attributes.put("href", "http://www.bundesarchiv.de/recherche/index.html.en");
	                        childArchguide5Attributes.put("xml:lang", "eng");
	                        childArchguide5.put("attributes", childArchguide5Attributes);
	                        childArchguide5.put("nodeValue", "Please use our online research tools in other to prepare.....");
	                        childArchguide4Children.add(childArchguide5);
    	                    
	                        childArchguide5 = new HashMap<String, Object>();
	                        childArchguide5.put("nodeName", "researchservices");  //researchservices
	                        childArchguide5.put("attributes", null);
	                        childArchguide5.put("nodeValue", null);
	                          childArchguide5Children = new ArrayList<HashMap<String, Object>>(); 
 	                          childArchguide5Children.add(buildEagHashMapDescriptiveNote());
  	                    
 	                        childArchguide5.put("children", childArchguide5Children);
	                        childArchguide4Children.add(childArchguide5);
    	                    
    	                 childArchguide4.put("children", childArchguide4Children);  
    	                    
     	                 childArchguide3Children.add(childArchguide4);   //add searchroom
     	               
     	               //Children of services, library
     	               
  	                   childArchguide4 = new HashMap<String, Object>();
  	                   childArchguide4.put("nodeName", "library");
  	                   childArchguide4Attributes = new HashMap<String, String>();
                       childArchguide4Attributes.put("question", "yes");
                       childArchguide4.put("attributes", childArchguide4Attributes);
  	                   childArchguide4.put("nodeValue", null);
   	                    
  	                       //Children of library
  	                       childArchguide4Children = new ArrayList<HashMap<String, Object>>(); 
  	                       childArchguide4Children.add(buildContact()); //contact
  	                       childArchguide4Children.add(buildWebpage()); //webpage
  	                       
 	                       childArchguide5 = new HashMap<String, Object>();
 	                       childArchguide5.put("nodeName", "monographicpub");  //monographicpub
 	                       childArchguide5.put("attributes", null);
   	                       childArchguide5.put("nodeValue", null);
   	                        childArchguide5Children = new ArrayList<HashMap<String, Object>>(); 
  	                        childArchguide5Children.add(buildNum("book"));
  	                    
  	                      childArchguide5.put("children", childArchguide5Children);
  	                    childArchguide4Children.add(childArchguide5);
         	   
  	                     childArchguide5 = new HashMap<String, Object>();
                         childArchguide5.put("nodeName", "serialpub");  //serialpub
                         childArchguide5.put("attributes", null);
	                     childArchguide5.put("nodeValue", null);
	                        childArchguide5Children = new ArrayList<HashMap<String, Object>>(); 
	                        childArchguide5Children.add(buildNum("title"));
	                    
	                      childArchguide5.put("children", childArchguide5Children);
	                    childArchguide4Children.add(childArchguide5);  
	                    
	                    childArchguide4.put("children", childArchguide4Children);
	                    
	                  childArchguide3Children.add(childArchguide4);
	                    
	                  //Children of services, internetAccess
    	               
 	                   childArchguide4 = new HashMap<String, Object>();
 	                   childArchguide4.put("nodeName", "internetAccess");
 	                   childArchguide4Attributes = new HashMap<String, String>();
                       childArchguide4Attributes.put("question", "yes");
                       childArchguide4.put("attributes", childArchguide4Attributes);
 	                   childArchguide4.put("nodeValue", null);
  	                    
 	                       //Children of internetAccess
 	                       childArchguide4Children = new ArrayList<HashMap<String, Object>>(); 
 	                       childArchguide4Children.add(buildEagHashMapDescriptiveNote()); //descriptiveNote
 	     	           
 	                   childArchguide4.put("children", childArchguide4Children);
 		                    
 		               childArchguide3Children.add(childArchguide4);    
 	  	              
 		               //Children of services, techservices
 		               
 		               childArchguide4 = new HashMap<String, Object>();
	                   childArchguide4.put("nodeName", "techservices");
                       childArchguide4.put("attributes", null);
	                   childArchguide4.put("nodeValue", null); 
	                      
	                   //Children of techservices
	                       childArchguide4Children = new ArrayList<HashMap<String, Object>>(); 
	                         childArchguide5 = new HashMap<String, Object>();
 	                         childArchguide5.put("nodeName", "reproductionser");  //reproductionser
 	                         childArchguide5Attributes = new HashMap<String, String>();
 	                         childArchguide5Attributes.put("question", "yes");
 	                         childArchguide5.put("attributes", childArchguide5Attributes);
   	                         childArchguide5.put("nodeValue", null);
	                            
   	                            //Children of reproductionser
   	                             childArchguide5Children = new ArrayList<HashMap<String, Object>>(); 
	                             childArchguide5Children.add(buildEagHashMapDescriptiveNote());
	                             childArchguide5Children.add(buildContact());
	                             childArchguide5Children.add(buildWebpage());
	                             HashMap<String, Object> childArchguide6 = new HashMap<String, Object>();
	                             childArchguide6.put("nodeName", "microformser");
	                             HashMap<String, String> childArchguide6Attributes = new HashMap<String, String>();
	                             childArchguide6Attributes.put("question", "yes");
	                             childArchguide6.put("attributes", childArchguide6Attributes);
	       	                     childArchguide6.put("nodeValue", null);
	       	                     
	       	                     childArchguide5Children.add(childArchguide6);
	       	                     
	       	                     childArchguide6 = new HashMap<String, Object>();
	                             childArchguide6.put("nodeName", "photographser");
	                             childArchguide6Attributes = new HashMap<String, String>();
	                             childArchguide6Attributes.put("question", "yes");
	                             childArchguide6.put("attributes", childArchguide6Attributes);
	       	                     childArchguide6.put("nodeValue", null);
	       	                     
	       	                     childArchguide5Children.add(childArchguide6);
	       	                     
	       	                     childArchguide6 = new HashMap<String, Object>();
	                             childArchguide6.put("nodeName", "digitalser");
	                             childArchguide6Attributes = new HashMap<String, String>();
	                             childArchguide6Attributes.put("question", "yes");
	                             childArchguide6.put("attributes", childArchguide6Attributes);
	       	                     childArchguide6.put("nodeValue", null);
	       	                     
	       	                     childArchguide5Children.add(childArchguide6);
	       	                     
	       	                     childArchguide6 = new HashMap<String, Object>();
	                             childArchguide6.put("nodeName", "photocopyser");
	                             childArchguide6Attributes = new HashMap<String, String>();
	                             childArchguide6Attributes.put("question", "yes");
	                             childArchguide6.put("attributes", childArchguide6Attributes);
	       	                     childArchguide6.put("nodeValue", null);
	       	                     
	       	                     childArchguide5Children.add(childArchguide6);
	       	                      
	                      childArchguide5.put("children", childArchguide5Children);
	                     childArchguide4Children.add(childArchguide5);
	                     
	                     //Children restorationlab
	                     childArchguide5 = new HashMap<String, Object>();
	                     childArchguide5.put("nodeName", "restorationlab");  //restorationLab
	                     childArchguide5Attributes = new HashMap<String, String>();
	                     childArchguide5Attributes.put("question", "yes");
	                     childArchguide5.put("attributes", childArchguide5Attributes);
	                     childArchguide5.put("nodeValue", null);
	                     
	                            //Children
	                            childArchguide5Children = new ArrayList<HashMap<String, Object>>(); 
                                childArchguide5Children.add(buildEagHashMapDescriptiveNote());
                                childArchguide5Children.add(buildContact());
                                childArchguide5Children.add(buildWebpage());
	                     
                          childArchguide5.put("children", childArchguide5Children);
       	                  childArchguide4Children.add(childArchguide5);
	                     
	                    childArchguide4.put("children",  childArchguide4Children); 
	                     
	                    childArchguide3Children.add(childArchguide4);      
	                    
	                    //Children of services, recreationalServices
	                    childArchguide4 = new HashMap<String, Object>();
		                childArchguide4.put("nodeName", "recreationalServices");
	                    childArchguide4.put("attributes", null);
		                childArchguide4.put("nodeValue", null); 
		                      
		                   //Children of recreationalServices
		                       childArchguide4Children = new ArrayList<HashMap<String, Object>>(); 
		                         childArchguide5 = new HashMap<String, Object>();
	 	                         childArchguide5.put("nodeName", "refreshment");  //refreshment
	 	                         childArchguide5.put("attributes", null);
	   	                         childArchguide5.put("nodeValue", null); 
                                 childArchguide5Children = new 	ArrayList<HashMap<String, Object>>(); 
                                 childArchguide5Children.add(buildEagHashMapDescriptiveNote());
                                 
                                 childArchguide5.put("children", childArchguide5Children);
              	                 childArchguide4Children.add(childArchguide5);
                                 
              	                 childArchguide5 = new HashMap<String, Object>();
	 	                         childArchguide5.put("nodeName", "exhibition");  //exhibition
	 	                         childArchguide5.put("attributes", null);
	   	                         childArchguide5.put("nodeValue", null); 
                                 childArchguide5Children = new 	ArrayList<HashMap<String, Object>>(); 
                                 childArchguide5Children.add(buildEagHashMapDescriptiveNote());
                                 childArchguide5Children.add(buildWebpage());
                            
                                 childArchguide5.put("children", childArchguide5Children);
            	                 childArchguide4Children.add(childArchguide5);
                                 
            	                 childArchguide5 = new HashMap<String, Object>();
	 	                         childArchguide5.put("nodeName", "tourSessions");  //tourSessions
	 	                         childArchguide5.put("attributes", null);
	   	                         childArchguide5.put("nodeValue", null); 
                                 childArchguide5Children = new 	ArrayList<HashMap<String, Object>>(); 
                                 childArchguide5Children.add(buildEagHashMapDescriptiveNote());
                                 childArchguide5Children.add(buildWebpage());
                            
                                 childArchguide5.put("children", childArchguide5Children);
            	                 childArchguide4Children.add(childArchguide5);
                                 
            	                 childArchguide5 = new HashMap<String, Object>();
	 	                         childArchguide5.put("nodeName", "otherServices");  //otherServices
	 	                         childArchguide5.put("attributes", null);
	   	                         childArchguide5.put("nodeValue", null); 
                                 childArchguide5Children = new 	ArrayList<HashMap<String, Object>>(); 
                                 childArchguide5Children.add(buildEagHashMapDescriptiveNote());
                                 childArchguide5Children.add(buildWebpage());
                            
                                 childArchguide5.put("children", childArchguide5Children);
            	                 childArchguide4Children.add(childArchguide5);
                                 
                                 childArchguide4.put("children",childArchguide4Children);
                             
                             childArchguide3Children.add(childArchguide4); 
                             childArchguide3.put("children", childArchguide3Children);
                             
                            childArchguide2Children.add(childArchguide3);  // add services       
                                 
        childArchguide2.put("children", childArchguide2Children);
        childArchguide1Children.add(childArchguide2);
        childArchguide1.put("children",childArchguide1Children);
        
        
		// TODO Auto-generated method stub
		return childArchguide1;
	}

	private HashMap<String, Object> buildContact() {
		
		 HashMap<String, Object>  childArchguidechild = new HashMap<String, Object>(); 
         childArchguidechild.put("nodeName", "contact");
         childArchguidechild.put("attributes",null);
         childArchguidechild.put("nodeValue", null); 
         		
         ArrayList<HashMap<String, Object>> childArchguideChildren = new ArrayList<HashMap<String, Object>>();
         childArchguideChildren.add(buildTelephone());
         childArchguideChildren.add(buildEmail());
         
         childArchguidechild.put("children", childArchguideChildren);
		
		
		return childArchguidechild;
	}

	private HashMap<String, Object> buildNum(String name) {
		
		HashMap<String, Object> childArchguidechild = new HashMap<String, Object>();
		
		childArchguidechild.put("nodeName", "num");
		HashMap<String, String> childArchguidechildAttributes = new HashMap<String, String>();
        childArchguidechildAttributes.put("unit", name);
        childArchguidechild.put("attributes", childArchguidechildAttributes);
        childArchguidechild.put("nodeValue", "15000");	
		
        
		return childArchguidechild;
	}

	private HashMap<String, Object> buildRule() {
		
		HashMap<String, Object> childArchguide4 = new HashMap<String, Object>();
		
  	    childArchguide4.put("nodeName", "rule");
        HashMap<String, String> childArchguide4Attributes = new HashMap<String, String>();
        childArchguide4Attributes.put("xml:lang", "ger");
        childArchguide4.put("attributes", childArchguide4Attributes);
        childArchguide4.put("nodeValue", "Die Bundesregierung...............");
        
		return childArchguide4;
	}

	private HashMap<String, Object> buildWebpage() {
		
		HashMap<String, Object> childArchguide3 = new HashMap<String, Object>();
		childArchguide3 = new HashMap<String, Object>();
        childArchguide3.put("nodeName","webpage");
        HashMap<String, String> childArchguide3Attributes = new HashMap<String, String>();
        childArchguide3Attributes.put("href", "www.bundesarchiv.de");
        childArchguide3.put("attributes", childArchguide3Attributes);
        childArchguide3.put("nodeValue", "Go to the website");
		

		return childArchguide3;
	}

	private HashMap<String, Object> buildEmail() {
		
		HashMap<String, Object> childArchguide3 = new HashMap<String, Object>();
		childArchguide3 = new HashMap<String, Object>();
        childArchguide3.put("nodeName","email");
        HashMap<String, String> childArchguide3Attributes = new HashMap<String, String>();
        childArchguide3Attributes.put("href", "koblenz@bundesarchiv.de");
        childArchguide3.put("attributes", childArchguide3Attributes);
        childArchguide3.put("nodeValue", "Send an e-mail");
		
		
		return childArchguide3;
	}

	private HashMap<String, Object> buildFax() {
		
		HashMap<String, Object> childArchguide3 = new HashMap<String, Object>();
		childArchguide3 = new HashMap<String, Object>();
        childArchguide3.put("nodeName","fax");
        childArchguide3.put("attributes", null);
        childArchguide3.put("nodeValue", "+49 261 505 226");
		
		return childArchguide3;
	}

	private HashMap<String, Object> buildTelephone() {
		
		HashMap<String, Object> childArchguide3 = new HashMap<String, Object>();
        childArchguide3.put("nodeName","telephone");
        childArchguide3.put("attributes", null);
        childArchguide3.put("nodeValue", "+49 261 505 0");
		
		return childArchguide3;
	}

}
