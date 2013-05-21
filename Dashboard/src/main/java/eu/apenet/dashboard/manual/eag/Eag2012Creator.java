package eu.apenet.dashboard.manual.eag;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.dashboard.utils.ChangeControl;
import eu.apenet.persistence.dao.ArchivalInstitutionDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.ArchivalInstitution;

// TODO: This class will be removed.

/**
 * Creates and manages all building process related to EAG2012 into Dashboard 
 */
public class Eag2012Creator {
	
	public static final String TAB_YOUR_INSTITUTION = "your_institution";
	public static final String TAB_IDENTITY = "identity";
	public static final String TAB_CONTACT = "contact";
	public static final String TAB_ACCESS_AND_SERVICES = "access_and_services";
	public static final String TAB_DESCRIPTION = "description";
	public static final String TAB_CONTROL = "control";
	public static final String TAB_RELATION = "relation";
	
	private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
	
    private static final String EAG_XMLNS = "http://www.archivesportaleurope.net/profiles/APEnet_EAG/";
	private static final String XML_AUDIENCE = "external";
	private static final String XML_BASE = "http://www.archivesportaleurope.net/";
	
	private static final String VISITORS_ADDRESS = "visitors address";
	private static final String POSTAL_ADDRESS = "postal address";
	//agencyType values
	private static final String AGENT_TYPE_MACHINE = "machine";
	private static final String AGENT_TYPE_HUMAN = "human";
	//eventType values
	private static final String EVENTTYPE_CREATED = "created";
	private static final String EVENTTYPE_DELETED = "revised";
	//section indexes
	public static final String ROOT = "root";
	public static final String LANGUAGE_DECLARATIONS = "languageDeclaration";
	public static final String RESOURCE_RELATION = "resourceRelation";
	public static final String EAG_RELATION = "eagRelation";
	public static final String LOCAL_TYPE_DECLARATION = "localTypeDeclaration";
	public static final String CONVENTION_DECLARATION = "conventionDeclaration";
	public static final String MAINTENANCE_AGENCY = "maintenanceAgency";
	public static final String SOURCE = "source";
	public static final String REPOSITORY = "repository";
	public static final String OTHER_SERVICES = "otherServices";
	public static final String TOURS_SESSIONS = "toursSessions";
	public static final String EXHIBITION = "exhibition";
	public static final String REFRESHMENT = "refreshment";
	public static final String RESTORATION_LAB = "restorationlab";
	public static final String REPRODUCTIONSER = "reproductionser";
	public static final String INTERNET_ACCESS = "internetAccess";
	public static final String RESEARCH_SERVICES = "researchServices";
	public static final String HOLDINGS = "holdings";
	public static final String BUILDING = "building";
	public static final String REPOSITORHIST = "repositorhist";
	public static final String LIBRARY = "library";
	public static final String SEARCHROOM = "searchroom";
	//subsection indexes
	public static final String WORKING_PLACES = "workingPlaces";
	public static final String COMPUTER_PLACES = "computerPlaces";
	public static final String MICROFILM = "microfilm";
	public static final String MONOGRAPHIC_PUBLICATION ="monographicPublication";
    public static final String SERIAL_PUBLICATION ="serialPublication";
	public static final String REPOSITOR_FOUND = "repositorfound";
	public static final String REPOSITOR_SUP = "repositorsup";
	public static final String ROOT_SUBSECTION = "root_section";
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
	private Integer aiId;

	public Eag2012Creator(Integer archivalInstitutionId,Eag2012 eag2012,String storagePath) {
		this.aiId = archivalInstitutionId;
		this.eag2012 = eag2012;
		this.storagePath=storagePath;
		checkAndFillParametters();
	}
	
	public void createEag2012(){
		
		if(this.eag2012!=null && this.aiId!=null){
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
//	        Document doc = null;
			StringBuilder docStringBuilder = null;
//			try {
//				doc = generateXMLDocumentFromEagNodes(eagNode);
				docStringBuilder = generateStringFromEagNodes(eagNode);
//			} catch (ParserConfigurationException e) {
//				log.error("EXCEPTION trying to generate an XML Document from EAGNode",e);
//			} 
			//generate XML-DOM Document to be stored from object-eag-structure
//	        if(doc!=null){
			if(docStringBuilder!=null){
	        	//Write the XML
//	            storeToXML(doc);
				try {
					storeToXML(docStringBuilder);
				} catch (IOException e) {
					log.error("ERROR trying to store eag2012 into '"+storagePath+"'.",e);
				}
	            //last checks 
	            try {
					if(Eag2012.checkAndFixRepositorId(this.aiId, this.storagePath)){
						log.debug("EAG2012 changed (from <otherRepositorId> tag) - repositorId@repositorycode");
					}
				} catch (Exception e){
					log.error("EXCEPTION trying to check and fix/fill repositorycode",e);
				}
	        }else{
	        	log.warn("Couldn't not be stored EAG2012 to XML file, possible reason: is doc null?");
	        }
			/* NEW DEVELOPMENT - END TEST*/
		}
	}
	
	private StringBuilder generateStringFromEagNodes(Eag2012Node eagNode) {
		StringBuilder eag2012StringBuilder = new StringBuilder();
		eag2012StringBuilder.append(generateRecursiveStringFromEagNodes(eagNode,0));
		return eag2012StringBuilder;
	}
	
	private StringBuilder generateRecursiveStringFromEagNodes(Eag2012Node eagNode,Integer level){
		StringBuilder stringBuilderNode = new StringBuilder();
		stringBuilderNode.append("\n");
		for(int i=0;i<level;i++){
    		stringBuilderNode.append("\t");
    	}
		stringBuilderNode.append("<"+eagNode.getNodeName());
        if(eagNode.getAttributes()!=null){
        	HashMap<String, String> attributes = eagNode.getAttributes();
        	Iterator<String> keysIterator = attributes.keySet().iterator();
        	while(keysIterator.hasNext()){
        		String key = keysIterator.next();
        		String value = attributes.get(key);
        		if(value!=null && !value.isEmpty()){
        			stringBuilderNode.append(" "+key+"=\""+value+"\"");
        		}
        	}
        }
        stringBuilderNode.append(">");
        if(eagNode.getValue()!=null){
        	stringBuilderNode.append(eagNode.getValue());
        }
        if(eagNode.getChildren()!=null){
        	Iterator<Eag2012Node> eag2012nodesIterator = eagNode.getChildren().iterator();
        	while(eag2012nodesIterator.hasNext()){
        		stringBuilderNode.append(generateRecursiveStringFromEagNodes(eag2012nodesIterator.next(),level+1));
        	}
        	stringBuilderNode.append("\n");
            for(int i=0;i<level;i++){
        		stringBuilderNode.append("\t");
        	}
        }
        stringBuilderNode.append("</"+eagNode.getNodeName()+">");
        return stringBuilderNode;
	}

	
	private void checkAndFillParametters() {
		if(this.archivalInstitutionDao==null){
			this.archivalInstitutionDao = DAOFactory.instance().getArchivalInstitutionDAO();
		}
		if(this.archivalInstitution==null){
			this.archivalInstitution = this.archivalInstitutionDao.getArchivalInstitution(this.aiId);
			this.isNew = true;
		}else{
			this.isNew = false;
		}
		if(this.storagePath==null){
			this.storagePath = APEnetUtilities.getConfig().getRepoDirPath()+this.archivalInstitution.getEagPath();
		}
        if(this.eag2012!=null){
        	SimpleDateFormat df = new SimpleDateFormat();
            df.applyPattern(DATE_FORMAT);
        	this.eag2012.setEventDateTimeStandardDateTime(df.format(new GregorianCalendar().getTime()));
        	this.eag2012.setEventTypeValue((this.isNew)?EVENTTYPE_CREATED:EVENTTYPE_DELETED);
        }
	}
	/**
	 * Stores an StringBuilder which contains eag2012 data.
	 * 
	 * @param StringBuiler
	 * @throws IOException
	 */
	private void storeToXML(StringBuilder docSB) throws IOException {
		//Create the new file 
    	BufferedWriter writer = new BufferedWriter(new FileWriter(new File(storagePath)));
    	writer.write(docSB.toString());
    	writer.close();
    	
        //Finally, the new path, autform and repositorycode are stored in archival_institution table
        if (this.isNew) {
        	//If the EAG is new, the registration date has to be stored in Data Base
            Date dateNow = new Date();
            this.archivalInstitution.setRegistrationDate(dateNow);	                	
        }

        this.archivalInstitution.setEagPath(this.eagPath);
        this.archivalInstitution.setAutform(this.eag2012.getAutform());
        this.archivalInstitution.setRepositorycode(this.getId());
        this.archivalInstitutionDao.store(this.archivalInstitution);
        

		log.info("The EAG " + this.eagPath + " has been created and stored in repository");
        ChangeControl.logOperation("Upload eag");
        
        if (someRepositorguideInformationEmpty) {
        	value = "correct_withoutRepositorguideInformation";
        }
        else {
        	value = "correct";
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
        		if(value!=null && !value.isEmpty()){
        			elementNode.setAttribute(key, value);
        		}
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
	        		if(value!=null && !value.isEmpty()){
	        			childNode.setAttribute(key, value);
	        		}
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
        
        eagHashMap.put("nodeName","eag"); //mandatory
        HashMap<String, String> eagAttributes = new HashMap<String, String>();
        eagAttributes.put("audience", XML_AUDIENCE);
        eagAttributes.put("xmlns", EAG_XMLNS);
        eagHashMap.put("attributes", eagAttributes);
        eagHashMap.put("nodeValue", null);

        List<HashMap<String,?>> children = new ArrayList<HashMap<String,?>>(); 
        
        children.add(buildControl(this.eag2012.getControlId(),this.eag2012.getControlLanguage())); //mandatory, non-repeatable       
        children.add(buildArchguide()); //mandatory no repeatable       
        children.add(buildRelations(this.eag2012.getRelationsId(),this.eag2012.getRelationsLang())); //no mandatory no repeatable
        
        eagHashMap.put("children", children); //put EAG children nodes
        
        return eagHashMap;
	}

	private HashMap<String, ?> buildRelations(String relationsId, String relationsLang) {
		
		HashMap<String, Object> childRelations = new HashMap<String, Object>();
		
		childRelations.put("nodeName", "relations");
		HashMap<String, String> childRelationsAttributes = new HashMap<String, String>();
		childRelationsAttributes.put("xml:base", XML_BASE);
		childRelationsAttributes.put("xml:id",relationsId);
		childRelationsAttributes.put("xml:lang", relationsLang);
		childRelations.put("attributes", childRelationsAttributes);
		childRelations.put("nodeValue", null);
		   ArrayList<HashMap<String, ?>> childrenRelations = new ArrayList<HashMap<String,?>>();
		   for(int i=0;this.eag2012.getResourceRelationResourceRelationType()!=null && i<this.eag2012.getResourceRelationResourceRelationType().size();i++){ //no mandatory repeatable
			   childrenRelations.add(buildResourceRelation(this.eag2012.getResourceRelationResourceRelationType().get(i),this.eag2012.getResourceRelationLastDateTimeVerified().get(i),this.eag2012.getResourceRelationId().get(i),this.eag2012.getResourceRelationLang().get(i),this.eag2012.getResourceRelationHref().get(i)));
		   }
		   for(int i=0;this.eag2012.getEagRelationEagRelationType()!=null && i<this.eag2012.getEagRelationEagRelationType().size();i++){ //no mandatory repeatable
			   childrenRelations.add(buildEagRelation(this.eag2012.getEagRelationEagRelationType().get(i),this.eag2012.getEagRelationHref().get(i)));
		   }
		
		childRelations.put("children", childrenRelations);
		
		
		return childRelations;
	}

	private HashMap<String, ?> buildResourceRelation(String resourceRelationType,String resourceRelationLastDateTimeVerified,String resourceRelationId,String resourceRelationLang,String resourceRelationHref) {
		
		HashMap<String, Object> childresourceRelation = new HashMap<String, Object>();
		 childresourceRelation.put("nodeName", "resourceRelation");
		HashMap<String, String> childresourceRelationAttributes = new HashMap<String, String>();
		if(resourceRelationType!=null){
	     childresourceRelationAttributes.put("resourceRelationType",resourceRelationType);
		 childresourceRelationAttributes.put("lastDateTimeVerified", resourceRelationLastDateTimeVerified);
		 childresourceRelationAttributes.put("xml:id", resourceRelationId);
		 childresourceRelationAttributes.put("xml:lang",resourceRelationLang);
		 childresourceRelationAttributes.put("href", resourceRelationHref);
		 childresourceRelation.put("attributes",childresourceRelationAttributes);
		}
		childresourceRelation.put("nodeValue",null);
		    //Children of resourceRelation
		    
		    ArrayList<HashMap<String, ?>> childrenresourceRelation = new ArrayList<HashMap<String,?>>();
		
		    for(int i=0;this.eag2012.getRelationEntryValue()!=null && i<this.eag2012.getRelationEntryValue().size();i++){//no mandatory repeatable
		    	childrenresourceRelation.add(buildRelationEntry(this.eag2012.getRelationEntryScriptCode().get(i),this.eag2012.getRelationEntryId().get(i),this.eag2012.getRelationEntryLang().get(i),this.eag2012.getRelationEntryValue().get(i)));
		    } 
		     childrenresourceRelation.add(buildDescriptiveNote(0,TAB_RELATION,RESOURCE_RELATION,0)); //mandatory no repeatable
		     
		     for(int i=0;this.eag2012.getDateSetLang()!=null && i<this.eag2012.getDateSetLang().size();i++){//no mandatory repeatable
		    	 childrenresourceRelation.add(buildDateSet(0,TAB_RELATION,i));
		     }
		     for(int i=0;this.eag2012.getDateLang()!=null && i<this.eag2012.getDateLang().size();i++){ //no mandatory repeatable
		    	 childrenresourceRelation.add(buildDate(0,TAB_RELATION,i));
		     }
		     for(int i=0;this.eag2012.getDateRangeLang()!=null && i<this.eag2012.getDateRangeLang().size();i++){ //no mandatory repeatable
		    	 childrenresourceRelation.add(buildDateRange(0,TAB_RELATION,i));
		     }
		     childrenresourceRelation.add(buildObjectBinWrap(this.eag2012.getObjectBinWrapId())); //no mandatory no repeatable
		     
		     for(int i=0;this.eag2012.getObjectXMLWrapId()!=null && i<this.eag2012.getObjectXMLWrapId().size();i++){//no mandatory repeatable
		    	 childrenresourceRelation.add(buildObjectXMLWrap(this.eag2012.getObjectXMLWrapId().get(i)));
		     }
		     
		     for(int i=0;this.eag2012.getPlaceEntryValue()!=null && i<this.eag2012.getPlaceEntryValue().size();i++){//no mandatory repeatable
		    	 childrenresourceRelation.add(buildPlaceEntry(this.eag2012.getPlaceEntryAccuracy().get(i),this.eag2012.getPlaceEntryAltitude().get(i),this.eag2012.getPlaceEntryId().get(i),this.eag2012.getPlaceEntryLang().get(i),this.eag2012.getPlaceEntryCountryCode().get(i),this.eag2012.getPlaceEntryLatitude().get(i),this.eag2012.getPlaceEntryLongitude().get(i),this.eag2012.getPlaceEntryScriptCode().get(i),this.eag2012.getPlaceEntryValue().get(i)));
		     }
		childresourceRelation.put("children", childrenresourceRelation);     
		     
		return childresourceRelation;
	}

	private HashMap<String, ?> buildPlaceEntry(String accuracy,String altitude,String placeEntryId,String lang,String countryCode,String latitude,String longitude,String scriptCode,String value ) {
		
		HashMap<String, Object> childPlaceEntry = new HashMap<String, Object>();
		childPlaceEntry.put("nodeName", "placeEntry");
		HashMap<String, String> childPlaceEntryAttributes = new HashMap<String,String>();
		childPlaceEntryAttributes.put("accuracy", /*"specific"*/accuracy);
		childPlaceEntryAttributes.put("altitude", altitude);
		childPlaceEntryAttributes.put("xml:id", placeEntryId);
		childPlaceEntryAttributes.put("xml:lang", lang);
		childPlaceEntryAttributes.put("countryCode", countryCode);
		childPlaceEntryAttributes.put("latitude", latitude);
		childPlaceEntryAttributes.put("longitude", longitude);
		childPlaceEntryAttributes.put("localType", "localType");
		childPlaceEntryAttributes.put("scriptCode", scriptCode);
		childPlaceEntryAttributes.put("transliteration", "http://www.archivesportaleurope.net/scripts/EAG/");
		childPlaceEntryAttributes.put("vocabularySource", "http://www.archivesportaleurope.net/vocabularies/EAG/");
		childPlaceEntry.put("attributes", childPlaceEntryAttributes);
		childPlaceEntry.put("nodeValue", value);
		
		
		return childPlaceEntry;
	}

	private HashMap<String, ?> buildEagRelation(String eagRelationType,String eagRelationHref) {
		
		HashMap<String, Object> childeagRelation = new HashMap<String, Object>();
		childeagRelation.put("nodeName", "eagRelation");
		HashMap<String, String> childeagRelationAttributes = new HashMap<String,String>();
		if(eagRelationType!=null){
		childeagRelationAttributes.put("eagRelationType", eagRelationType);
		childeagRelationAttributes.put("href", eagRelationHref);
		childeagRelation.put("attributes", childeagRelationAttributes);
		}
		childeagRelation.put("nodeValue", null);
		   
		    //Children of eagRelation
		    ArrayList<HashMap<String, ?>> childrenRelations = new ArrayList<HashMap<String,?>>();
		    for(int i=0;this.eag2012.getRelationEntryValue()!=null && i<this.eag2012.getRelationEntryValue().size();i++){//no mandatory repeatable
		    	childrenRelations.add(buildRelationEntry(this.eag2012.getRelationEntryScriptCode().get(i),this.eag2012.getRelationEntryId().get(i),this.eag2012.getRelationEntryLang().get(i),this.eag2012.getRelationEntryValue().get(i)));
		    }
		    childrenRelations.add(buildDescriptiveNote(0,TAB_RELATION,EAG_RELATION,0)); //mandatory no repeatable
		    
		childeagRelation.put("children", childrenRelations); 
			
		
		return childeagRelation;
	}

	private HashMap<String, ?> buildRelationEntry(String relationEntryScriptCode,String relationEntryId, String relationEntryLang,String relationEntryValue) {
		
		HashMap<String, Object> childRelation = new HashMap<String, Object>();
		childRelation.put("nodeName", "relationEntry");
		HashMap<String, String> childRelationAttributes = new HashMap<String,String>();
		childRelationAttributes.put("localType", "localType");
		childRelationAttributes.put("scriptCode", /*"scriptCode"*/relationEntryScriptCode);
		childRelationAttributes.put("transliteration", "http://www.archivesportaleurope.net/scripts/EAG/");
		childRelationAttributes.put("xml:id", relationEntryId);
		childRelationAttributes.put("xml:lang", relationEntryLang);
		childRelation.put("attributes", childRelationAttributes);
		childRelation.put("nodeValue", relationEntryValue);
		
		return childRelation;
	}

	private HashMap<String, ?> buildControl(String controlId, String controlLanguage) {
		HashMap<String, Object> childControl = new HashMap<String, Object>();
        //start control node
        childControl.put("nodeName","control");
        HashMap<String, String> controlAttributes = new HashMap<String,String>();
        controlAttributes.put("xml:base",XML_BASE);
        controlAttributes.put("xml:id",controlId);
        controlAttributes.put("xml:lang",controlLanguage);
        childControl.put("attributes", controlAttributes);
        childControl.put("nodeValue", null);
        
        ArrayList<HashMap<String, ?>> childrenControl = new ArrayList<HashMap<String,?>>();
        
        childrenControl.add(buildRecordId(this.eag2012.getRecordIdId(),this.eag2012.getRecordIdValue())); //mandatory, no-repeatable
        
        for(int i=0;this.eag2012.getOtherRecordIdValue()!=null && i<this.eag2012.getOtherRecordIdValue().size(); i++){ //no mandatory, repeatable
        	childrenControl.add(buildOtherRecordId((this.eag2012.getOtherRecordIdId()!=null && i<this.eag2012.getOtherRecordIdId().size())?this.eag2012.getOtherRecordIdId().get(i):null, this.eag2012.getOtherRecordIdValue().get(i)));
        }
        
        childrenControl.add(buildMaintenanceAgency(0,TAB_CONTROL)); //mandatory, no-repeatable
        childrenControl.add(buildMaintenanceStatus(this.eag2012.getMaintenanceStatusId(),this.eag2012.getMaintenanceStatusValue())); //mandatory, no-repeatable
        childrenControl.add(buildMaintenanceHistory(this.eag2012.getMaintenanceHistoryId(),this.eag2012.getMaintenanceHistoryLang())); //mandatory, no-repeatable
        childrenControl.add(buildLanguageDeclarations()); //no-mandatory,no-repeatable
        
        for(int i=0;this.eag2012.getConventionDeclarationLang()!=null && i<this.eag2012.getConventionDeclarationLang().size();i++){ //no mandatory, repeatable
        	childrenControl.add(buildConventionDeclaration(this.eag2012.getConventionDeclarationId().get(i),this.eag2012.getConventionDeclarationLang().get(i)));
        }
        for(int i=0;this.eag2012.getLocalControlLang()!=null && i<this.eag2012.getLocalControlLang().size();i++){ //no mandatory repeatable
        	childrenControl.add(buildLocalControl(this.eag2012.getLocalControlId().get(i),this.eag2012.getLocalControlLang().get(i)));
        }
        for(int i=0;this.eag2012.getLocalTypeDeclarationLang()!=null && i<this.eag2012.getLocalTypeDeclarationLang().size();i++){ //no mandatory repeatable
        	childrenControl.add(buildLocalTypeDeclaration(this.eag2012.getLocalTypeDeclarationId().get(i),this.eag2012.getLocalTypeDeclarationLang().get(i)));
        }
        childrenControl.add(buildPublicationStatus(this.eag2012.getPublicationStatusId(),this.eag2012.getPublicationStatusValue())); //no mandatory no repeatable
        childrenControl.add(buildSources(this.eag2012.getSourcesId(),this.eag2012.getSourcesLang())); //no mandatory no repeatable
        
        childControl.put("children",childrenControl);
        return childControl;
	}

	private HashMap<String, ?> buildLocalTypeDeclaration(String localTypeDeclarationId, String localTypeDeclarationLang) {
		HashMap<String, Object> childControl = new HashMap<String, Object>();
        childControl.put("nodeName","localTypeDeclaration");
        HashMap<String, String> childControlAttributes = new HashMap<String, String>();
        childControlAttributes.put("xml:id", localTypeDeclarationId);
        childControlAttributes.put("xml:lang",localTypeDeclarationLang);
        childControl.put("attributes", childControlAttributes);
        childControl.put("nodeValue", null);
       
        ArrayList<HashMap<String, Object>> childControlChildren = new ArrayList<HashMap<String,Object>>();
        for(int i=0;this.eag2012.getCitationValue()!=null && i<this.eag2012.getCitationValue().size();i++){ // citation mandatory, repeatable
          childControlChildren.add(buildCitation(this.eag2012.getCitationId().get(0).get(i),this.eag2012.getCitationLang().get(0).get(i),this.eag2012.getCitationLastDateTimeVerified().get(0).get(i),this.eag2012.getCitationHref().get(0).get(i),this.eag2012.getCitationValue().get(0).get(i)));
        }
        childControlChildren.add(buildAbbreviation(this.eag2012.getAbbreviationId(),this.eag2012.getAbbreviationLang(),this.eag2012.getAbbreviationValue())); //no-mandatory, no-repeatable
        childControlChildren.add(buildDescriptiveNote(0,TAB_CONTROL,LOCAL_TYPE_DECLARATION,0)); //no-mandatory,no-repeatable 
        childControl.put("children",childControlChildren);
        return childControl;
	}

	private HashMap<String, ?> buildLocalControl(String localControlId, String localControlLang) {
		HashMap<String, Object> childControl = new HashMap<String, Object>();
        childControl.put("nodeName","localControl");
        HashMap<String, String> childControlAttributes = new HashMap<String, String>();
        childControlAttributes.put("localType", "detailLevel");
        childControlAttributes.put("xml:id", localControlId);
        childControlAttributes.put("xml:lang", localControlLang);
        childControl.put("attributes", childControlAttributes);
        childControl.put("nodeValue", null);
        
        ArrayList<HashMap<String, Object>> childControlChildren = new ArrayList<HashMap<String,Object>>();
        
        for(int i=0;i<this.eag2012.getTermLang().size();i++){ //no mandatory, repeatable
        	childControlChildren.add(buildTerm(this.eag2012.getTermLastDateTimeVerified().get(i),this.eag2012.getTermScriptCode().get(i),this.eag2012.getTermId().get(i),this.eag2012.getTermLang().get(i),this.eag2012.getTermValue().get(i)));
        }
        for(int i=0;i<this.eag2012.getDateLang().size();i++){//no mandatory, repeatable
        	childControlChildren.add(buildDate(0,TAB_CONTROL,i));
        }
        for(int i=0;this.eag2012.getFromDateStandardDate()!=null && this.eag2012.getFromDateStandardDate().size()>0 && this.eag2012.getFromDateStandardDate().get(0).get(TAB_CONTROL)!=null && i<this.eag2012.getFromDateStandardDate().get(0).get(TAB_CONTROL).size();i++){ //no mandatory, repeatable
        	childControlChildren.add(buildDateRange(0,TAB_CONTROL,i));
        }
        childControl.put("children",childControlChildren);
        return childControl;
	}

	private HashMap<String, ?> buildConventionDeclaration(String conventionDeclarationId,String conventionDeclarationLang) {
		HashMap<String, Object> childControl = new HashMap<String, Object>();
        childControl.put("nodeName","conventionDeclaration");
        HashMap<String, String> childControlAttributes = new HashMap<String, String>();
        childControlAttributes.put("xml:id",conventionDeclarationId);
        childControlAttributes.put("xml:lang", conventionDeclarationLang);
        childControl.put("attributes", childControlAttributes);
        childControl.put("nodeValue", null);

        ArrayList<HashMap<String, Object>> childControlChildren = new ArrayList<HashMap<String,Object>>();
        childControlChildren.add(buildAbbreviation(this.eag2012.getAbbreviationId(),this.eag2012.getAbbreviationLang(),this.eag2012.getAbbreviationValue())); //no-mandatory,no-repeatable
        for(int i=0;this.eag2012.getCitationValue()!=null && i<this.eag2012.getCitationValue().size();i++){ //mandatory, repeatable
        	childControlChildren.add(buildCitation(this.eag2012.getCitationId().get(0).get(i),this.eag2012.getCitationLang().get(0).get(i),this.eag2012.getCitationLastDateTimeVerified().get(0).get(i),this.eag2012.getCitationHref().get(0).get(i),this.eag2012.getCitationValue().get(0).get(i))); //get(0) is reserve for citation into TAB_CONTROL
        }
        childControlChildren.add(buildDescriptiveNote(0,TAB_CONTROL,CONVENTION_DECLARATION,0)); //no-mandatory, no-repeatable
        
        childControl.put("children", childControlChildren);
        return childControl;
	}

	private HashMap<String, ?> buildLanguageDeclarations() {
		HashMap<String, Object> childControl = new HashMap<String, Object>();
        childControl.put("nodeName","languageDeclarations");
        childControl.put("attributes", null);
        childControl.put("nodeValue",null);
        
        ArrayList<HashMap<String, Object>> childControlChildren = new ArrayList<HashMap<String,Object>>();
        
	    for(int i=0;this.eag2012.getLanguageDeclarationLang()!=null && i<this.eag2012.getLanguageDeclarationLang().size();i++){  //no mandatory, repeatable
	    	childControlChildren.add(buildLanguageDeclaration(this.eag2012.getLanguageDeclarationId().get(i),this.eag2012.getLanguageDeclarationLang().get(i),i));
	    }
        childControl.put("children",childControlChildren);
        return childControl;
	}

	private HashMap<String, Object> buildLanguageDeclaration(String languageDeclarationId, String languageDeclarationLang,Integer languageDeclarationIndex) {

		  HashMap<String, Object> childControl1 = new HashMap<String,Object>();
	        childControl1.put("nodeName", "languageDeclaration");
	        HashMap<String, String> childControl1Attributes = new HashMap<String, String>();
	        childControl1Attributes.put("xml:id", languageDeclarationId);
	        childControl1Attributes.put("xml:lang",languageDeclarationLang);
	        childControl1.put("attributes", childControl1Attributes);
	        childControl1.put("nodeValue", null);
	        
		        ArrayList<HashMap<String, Object>> childControl1Children = new ArrayList<HashMap<String,Object>>();
		        
		        childControl1Children.add(buildLanguage(this.eag2012.getLanguageLanguageCode(),this.eag2012.getLanguageId(),this.eag2012.getLanguageLang(),this.eag2012.getLanguageValue())); //mandatory, non-repeatable 
		        childControl1Children.add(buildScript(this.eag2012.getScriptScriptCode(),this.eag2012.getScriptId(),this.eag2012.getScriptLang(),this.eag2012.getScriptValue())); //mandatory, non-repeatable
		        childControl1Children.add(buildDescriptiveNote(0,TAB_CONTROL,LANGUAGE_DECLARATIONS,languageDeclarationIndex));
		        
	        childControl1.put("children", childControl1Children);
		
		
		return childControl1;
	}

	private HashMap<String, Object> buildScript(String scriptScriptCode, String scriptId, String scriptLang, String scriptValue) {

		HashMap<String,Object> childControl2 = new HashMap<String, Object>();
	        childControl2.put("nodeName", "script");
	        HashMap<String,String>childControl2Attributes = new HashMap<String, String>();
	        childControl2Attributes.put("scriptCode", scriptScriptCode);
	        childControl2Attributes.put("xml:id", scriptId);
	        childControl2Attributes.put("xml:lang",scriptLang);
	        childControl2.put("attributes", childControl2Attributes);
	        childControl2.put("nodeValue", scriptValue);
	        childControl2.put("children", null);	
		
		return childControl2;
	}

	private HashMap<String, Object> buildLanguage(String languageLanguageCode, String languageId, String languageLang, String languageValue) {

		 HashMap<String, Object> childControl2 = new HashMap<String, Object>();
	        childControl2.put("nodeName", "language");
	        HashMap<String, String> childControl2Attributes = new HashMap<String, String>();
	        childControl2Attributes.put("languageCode", languageLanguageCode);
	        childControl2Attributes.put("xml:id", languageId);
	        childControl2Attributes.put("xml:lang", /*"spa"*/languageLang);
	        childControl2.put("attributes", childControl2Attributes);
	        childControl2.put("nodeValue",languageValue);
	        childControl2.put("children", null);
		
		
		return childControl2;
	}

	private HashMap<String, ?> buildMaintenanceHistory(String maintenanceHistoryId, String maintenanceHistoryLang) {
		HashMap<String, Object> childControl = new HashMap<String, Object>();
        childControl.put("nodeName","maintenanceHistory");
        HashMap<String, String> childControlAttributes = new HashMap<String, String>();
        childControlAttributes.put("xml:id",maintenanceHistoryId);
        childControlAttributes.put("xml:lang",maintenanceHistoryLang);
        childControl.put("attributes",childControlAttributes);
        childControl.put("nodeValue",null);
        
        ArrayList<HashMap<String, Object>> childControlChildren = new ArrayList<HashMap<String, Object>>();
        //for(int i=0;this.eag2012.getMaintenanceEventLang()!=null && i<this.eag2012.getMaintenanceEventLang().size();i++){ //mandatory, repeatable
        //for(int i=0;(this.eag2012.getAgentValue()!=null && this.eag2012.getAgentValue())|| this.eag2012.getAgentTypeValue() || this.eag2012.getEventDateTimeValue() || this.eag2012.getEventTypeValue();i++){ //mandatory, repeatable (why this tag is repeatable if the target agent tag is unique?)
        	//childControlChildren.add(buildMaintenanceEvent((this.eag2012.getMaintenanceEventId()!=null && this.eag2012.getMaintenanceEventId().size()>i)?this.eag2012.getMaintenanceEventId().get(i):null,(this.eag2012.getMaintenanceEventLang()!=null && this.eag2012.getMaintenanceEventLang().size()>i)?this.eag2012.getMaintenanceEventLang().get(i):null));
        	childControlChildren.add(buildMaintenanceEvent(null,null));
        //}
        childControl.put("children", childControlChildren);
        return childControl;
	}

	private HashMap<String, Object> buildMaintenanceEvent(String maintenanceEventId,String maintenanceEventLang) {

		 HashMap<String, Object> childControl1 = new HashMap<String, Object>();
	        childControl1.put("nodeName", "maintenanceEvent");
	        HashMap<String, String> childControl1Attributes = new HashMap<String, String>();
	        childControl1Attributes.put("xml:id", maintenanceEventId);
	        childControl1Attributes.put("xml:lang", maintenanceEventLang);
	        childControl1.put("attributes", childControl1Attributes);
	        childControl1.put("nodeValue", null);
	        
	        ArrayList<HashMap<String, Object>> childControl1Children = new ArrayList<HashMap<String,Object>>();
	        
	        childControl1Children.add(buildAgent(this.eag2012.getAgentId(),this.eag2012.getAgentLang(),this.eag2012.getAgentValue())); //mandatory, non-repeatable
	       
	        childControl1Children.add(buildAgentType(this.eag2012.getAgentTypeId(),this.eag2012.getAgentValue())); //mandatory, non-repeatable
	       
	        childControl1Children.add(buildEventDateTime(this.eag2012.getEventDateTimeStandardDateTime(),this.eag2012.getEventDateTimeLang(),this.eag2012.getEventDateTimeId(),this.eag2012.getEventDateTimeValue())); //mandatory, non-repeatable
	       
	        childControl1Children.add(buildEventType(this.eag2012.getEventTypeId(),this.eag2012.getEventTypeValue())); //mandatory, non-repeatable
	        childControl1.put("children", childControl1Children);
		
		return childControl1;
	}

	private HashMap<String, Object> buildEventType(String eventTypeId,String eventTypeValue) {

		HashMap<String,Object> childControl2 = new HashMap<String, Object>();
	       childControl2.put("nodeName", "eventType");
	       HashMap<String,String> childControl2Attributes = new HashMap<String, String>();
	        childControl2Attributes.put("xml:id", eventTypeId);
	        childControl2.put("attributes", childControl2Attributes);
	        childControl2.put("nodeValue", /*"revised"*/eventTypeValue);
	        childControl2.put("children", null);
		
		return childControl2;
	}

	private HashMap<String, Object> buildEventDateTime(String eventDateTimeStandardDateTime,String eventDateTimeLang,String eventDateTimeId, String  eventDateTimeValue) {
		 
		HashMap<String, Object>childControl2 = new HashMap<String, Object>();
	        childControl2.put("nodeName", "eventDateTime");
	       HashMap<String,String> childControl2Attributes = new HashMap<String, String>();
	        childControl2Attributes.put("standardDateTime", eventDateTimeStandardDateTime);
	        childControl2Attributes.put("xml:lang", eventDateTimeLang);
	        childControl2Attributes.put("xml:id", eventDateTimeId);
	        childControl2.put("attributes", childControl2Attributes);
	        childControl2.put("nodeValue", eventDateTimeValue);
	        childControl2.put("children", null);      
		
		return childControl2;
	}

	private HashMap<String, Object> buildAgentType(String agentTypeId, String agentTypeValue) {
		 
		HashMap<String,Object> childControl2 = new HashMap<String, Object>();
	        childControl2.put("nodeName", "agentType");
	        HashMap<String, String>  childControl2Attributes = new HashMap<String, String>();
	        childControl2Attributes.put("xml:id", agentTypeId);
	        childControl2.put("attributes", childControl2Attributes);
	        childControl2.put("nodeValue", (agentTypeValue==null || agentTypeValue.isEmpty())?AGENT_TYPE_MACHINE:AGENT_TYPE_HUMAN);
	        childControl2.put("children", null);
		
		return childControl2;
	}

	private HashMap<String, Object> buildAgent(String agentId,String agentLang, String agentValue) {

		HashMap<String, Object> childControl2 = new HashMap<String, Object>();
        childControl2.put("nodeName", "agent");
        HashMap<String, String> childControl2Attributes = new HashMap<String, String>();
        childControl2Attributes.put("xml:id", agentId);
        childControl2Attributes.put("xml:lang", agentLang);
        childControl2.put("attributes", childControl2Attributes);
        childControl2.put("nodeValue", agentValue);
        childControl2.put("children", null);
		
		return childControl2;
	}

	private HashMap<String, ?> buildMaintenanceStatus(String maintenanceStatusId, String maintenanceStatusValue) {
		HashMap<String, Object> childControl = new HashMap<String, Object>();
        childControl.put("nodeName","maintenanceStatus");
        HashMap<String, String> childControlAttributes = new HashMap<String, String>();
        childControlAttributes.put("xml:id",maintenanceStatusId);
        childControl.put("attributes",childControlAttributes);
        childControl.put("nodeValue",maintenanceStatusValue);
        return childControl;
	}

	private HashMap<String, ?> buildMaintenanceAgency(Integer indexRepo,String indexTab) {
		HashMap<String, Object> childControl = new HashMap<String, Object>();
        childControl.put("nodeName","maintenanceAgency");
        
        HashMap<String, String> childControlAttributes = new HashMap<String, String>();
        childControlAttributes.put("xml:id",this.eag2012.getMaintenanceAgencyId());
        childControl.put("attributes", childControlAttributes);
        childControl.put("nodeValue",null);
        ArrayList<HashMap<String, Object>> childControlChildren = new ArrayList<HashMap<String, Object>>();
         childControlChildren.add(buildAgencyCode(this.eag2012.getAgencyCodeId(),this.eag2012.getAgencyCodeValue())); //mandatory, no-repeatable
         childControlChildren.add(buildAgencyName(this.eag2012.getAgencyNameLang(),this.eag2012.getAgencyNameId(),this.eag2012.getAgencyNameValue())); //mandatory, non-repeatable
         
         for(int i=0;this.eag2012.getOtherAgencyCodeValue()!=null && i<this.eag2012.getOtherAgencyCodeValue().size();i++){ //no-mandatory, repeatable
           childControlChildren.add(buildOtherAgencyCode(this.eag2012.getOtherAgencyCodeId().get(i),this.eag2012.getOtherAgencyCodeValue().get(i)));
         }
        //childControlChildren.add(buildDescriptiveNote(this.eag2012.getDescriptiveNoteLang(),this.eag2012.getDescriptiveNoteId())); //no-mandatory, no-repatable
        childControlChildren.add(buildDescriptiveNote(indexRepo,indexTab,MAINTENANCE_AGENCY,0)); //no-mandatory, no-repeatable
        childControl.put("children",childControlChildren);
        
        return childControl;
	}

	private HashMap<String, Object> buildAgencyName(String agencyNameLang, String agencyNameId, String agencyNameValue) {

		 HashMap<String, Object> childControl1 = new HashMap<String, Object>();
	        childControl1.put("nodeName", "agencyName");
	        HashMap<String, String> childControlChildAttributes = new HashMap<String, String>();
	        childControlChildAttributes.put("xml:lang", agencyNameLang);
	        childControlChildAttributes.put("xml:id", agencyNameId);
	        childControl1.put("attributes",childControlChildAttributes);
	        childControl1.put("nodeValue",agencyNameValue);
	        childControl1.put("children",null);
		
		return  childControl1;
	}

	private HashMap<String, Object> buildAgencyCode(String agencyCodeId, String agencyCodeValue) {

		HashMap<String, Object> childControl1 = new HashMap<String, Object>();
        childControl1.put("nodeName", "agencyCode");
        HashMap<String, String> childControlChildAttributes = new HashMap<String, String>();
        childControlChildAttributes.put("xml:id", agencyCodeId);
        childControl1.put("attributes", childControlChildAttributes);
        childControl1.put("nodeValue", agencyCodeValue);
        childControl1.put("children", null);
		
		return childControl1;
	}

	private HashMap<String, Object> buildOtherAgencyCode(String otherAgencyCodeId,String otherAgencyCodeValue) {

		HashMap<String, Object> childControl1 = new HashMap<String, Object>();
	    childControl1.put("nodeName", "otherAgencyCode");
	    HashMap<String, String> childControlChildAttributes = new HashMap<String, String>();
	    childControlChildAttributes.put("localType", "localId");
	    childControlChildAttributes.put("xml:id",otherAgencyCodeId);
	    childControl1.put("attributes",childControlChildAttributes);
	    childControl1.put("nodeValue",otherAgencyCodeValue);
	    childControl1.put("children",null);
		
		
		return childControl1;
	}

	private HashMap<String, ?> buildOtherRecordId(String otherRecordId, String otherRecordIdValue) {
		HashMap<String, Object> childControl = new HashMap<String, Object>();
        childControl.put("nodeName","otherRecordId");
        HashMap<String, String> childControlAttributes = new HashMap<String, String>();
        childControlAttributes.put("localType","localId");
        childControlAttributes.put("xml:id",otherRecordId);
        childControl.put("attributes",childControlAttributes);
        childControl.put("nodeValue", otherRecordIdValue);
        childControl.put("children", null);
        return childControl;
	}

	private HashMap<String, ?> buildRecordId(String recordIdId,String recordIdValue) {
		HashMap<String, Object> childControl = new HashMap<String, Object>();
        childControl.put("nodeName","recordId");
        
        HashMap<String, String> childControlAttributes = new HashMap<String,String>();
        childControlAttributes.put("xml:id",recordIdId);
        childControl.put("attributes", childControlAttributes);
        childControl.put("children",null);
        childControl.put("nodeValue",recordIdValue);
        return childControl;
	}

	private HashMap<String, ?> buildSources(String sourcesId, String sourcesLang) {
		HashMap<String, Object> childControl = new HashMap<String, Object>();
        childControl.put("nodeName","sources");
       
        HashMap<String, String> childControlAttributes = new HashMap<String, String>();
        childControlAttributes.put("xml:base", XML_BASE);
        childControlAttributes.put("xml:id", sourcesId);
        childControlAttributes.put("xml:lang", sourcesLang);
        childControl.put("attributes", childControlAttributes);
        childControl.put("nodeValue", null);
        ArrayList<HashMap<String, Object>> childControlChildren = new ArrayList<HashMap<String,Object>>();
        
        for(int i=0;this.eag2012.getSourceLastDateTimeVerified()!=null && i<this.eag2012.getSourceLastDateTimeVerified().size();i++){ //mandatory, repeatable
        	childControlChildren.add(buildSource(this.eag2012.getSourceLastDateTimeVerified().get(i),this.eag2012.getSourceId().get(i),this.eag2012.getSourceHref().get(i)));
        }
        childControl.put("children",childControlChildren);
        return childControl;
	}

	private HashMap<String, Object> buildSource(String sourceLastDateTimeVerified, String sourceId, String sourceHref) {

		HashMap<String, Object> childControl2 = new HashMap<String, Object>();
        childControl2.put("nodeName", "source");
       
        HashMap<String, String> childControl2Attributes = new HashMap<String, String>();
        childControl2Attributes.put("lastDateTimeVerified", sourceLastDateTimeVerified);
        childControl2Attributes.put("xml:id", sourceId);
        childControl2Attributes.put("href", sourceHref);
        childControl2.put("attributes",childControl2Attributes);
        childControl2.put("nodeValue", null);
        List<HashMap<String, Object>> childControl2Children = new ArrayList<HashMap<String,Object>>();
        childControl2Children.add(buildSourceEntry(this.eag2012.getSourceEntryScriptCode(),this.eag2012.getSourceEntryId(),this.eag2012.getSourceEntryLang(),this.eag2012.getSourceEntryValue()));//no-mandatory,no-repeatable
        childControl2Children.add(buildObjectBinWrap(this.eag2012.getObjectBinWrapId())); //no-mandatory, no-repeatable 
        
        for(int i=0;this.eag2012.getObjectXMLWrapId()!=null && i<this.eag2012.getObjectXMLWrapId().size();i++){ //no mandatory, repeatable
        	childControl2Children.add(buildObjectXMLWrap(this.eag2012.getObjectXMLWrapId().get(i)));
	    }
        childControl2Children.add(buildDescriptiveNote(0,TAB_CONTROL,SOURCE,0));//no-mandatory,no-repeatable
		childControl2.put("children", childControl2Children);
		
		return childControl2;
	}

	private HashMap<String, Object> buildSourceEntry(String sourceEntryScriptCode, String sourceEntryId, String sourceEntryLang, String sourceEntryValue ) {

		HashMap<String, Object> childControl3 = new HashMap<String, Object>();
        childControl3.put("nodeName","sourceEntry");
        HashMap<String, String> childControl3Attributes = new HashMap<String, String>();
        childControl3Attributes.put("scriptCode", sourceEntryScriptCode);
        childControl3Attributes.put("transliteration","http://www.archivesportaleurope.net/scripts/EAG/");
        childControl3Attributes.put("xml:id",sourceEntryId);
        childControl3Attributes.put("xml:lang",sourceEntryLang);
        childControl3.put("attributes",childControl3Attributes);
        childControl3.put("nodeValue",sourceEntryValue);
		
		return childControl3;
	}

	private HashMap<String, Object> buildObjectXMLWrap(String objectXMLWrapId) {
		
		HashMap<String, Object> childObjectXMLWrap = new HashMap<String, Object>();
        childObjectXMLWrap.put("nodeName","objectXMLWrap");
        HashMap<String, String> childObjectXMLWrapAttributes = new HashMap<String, String>();
        childObjectXMLWrapAttributes.put("xml:id",objectXMLWrapId);
        childObjectXMLWrap.put("nodeValue",null);
        childObjectXMLWrap.put("children",null);
        childObjectXMLWrap.put("attributes",childObjectXMLWrapAttributes);
		
		return childObjectXMLWrap;
	}

	private HashMap<String, Object> buildObjectBinWrap(String objectBinWrapId) {
		
		HashMap<String, Object> childObjectBinWrap = new HashMap<String, Object>();
        childObjectBinWrap.put("nodeName","objectBinWrap");
        HashMap<String, String> childObjectBinWrapAttributes = new HashMap<String, String>();
        childObjectBinWrapAttributes.put("xml:id",objectBinWrapId);
        childObjectBinWrap.put("nodeValue",null);
        childObjectBinWrap.put("children",null);
        childObjectBinWrap.put("attributes",childObjectBinWrapAttributes);
      
		return childObjectBinWrap;
	}

	private HashMap<String, ?> buildPublicationStatus(String publicationStatusId, String publicationStatusValue) {
		HashMap<String, Object> childControl = new HashMap<String, Object>();
        childControl.put("nodeName","publicationStatus");
        HashMap<String, String> childControlAttributes = new HashMap<String, String>();
        childControlAttributes.put("xml:id", publicationStatusId);
        childControl.put("attributes", childControlAttributes);
        childControl.put("nodeValue", /*"approved"*/publicationStatusValue);
        childControl.put("children",null);
        return childControl;
	}

	private HashMap<String, Object> buildDateRange(Integer indexRepo,String indexTab,Integer indexList) {
		HashMap<String, Object> childControl1 = new HashMap<String,Object>();
        childControl1.put("nodeName", "dateRange");
        HashMap<String, String> childControl1Attributes = new HashMap<String, String>();
        childControl1Attributes.put("localType", "localDateType");
//        childControl1Attributes.put("xml:id", this.eag2012.getDateRangeId().get(indexRepo).get(indexTab).get(indexList));
//        childControl1Attributes.put("xml:lang", this.eag2012.getDateRangeLang().get(indexRepo).get(indexTab).get(indexList));
        childControl1.put("attributes",childControl1Attributes);
        childControl1.put("nodeValue", null);
        List<HashMap<String, Object>> childControl1Children = new ArrayList<HashMap<String, Object>>();
        
        childControl1Children.add(buildFromDate(
        		/*this.eag2012.getFromDateNotAfter().get(indexRepo).get(indexTab).get(indexList)*/null,
        		/*this.eag2012.getFromDateNotBefore().get(indexRepo).get(indexTab).get(indexList)*/null,
        		/*this.eag2012.getFromDateStandardDate().get(indexRepo).get(indexTab).get(indexList)*/null,
        		/*this.eag2012.getFromDateId().get(indexRepo).get(indexTab).get(indexList)*/null,
        		/*this.eag2012.getFromDateLang().get(indexRepo).get(indexTab).get(indexList)*/null,
        		/*this.eag2012.getFromDateValue().get(indexRepo).get(indexTab).get(indexList))*//*this.eag2012.getFromDateStandardDate().get(indexRepo).get(indexTab).get(indexList)));*/null));
        childControl1Children.add(buildToDate(
        		/*this.eag2012.getToDateNotAfter().get(indexRepo).get(indexTab).get(indexList)*/null,
        		/*this.eag2012.getToDateNotBefore().get(indexRepo).get(indexTab).get(indexList)*/null,
        		/*this.eag2012.getToDateStandardDate().get(indexRepo).get(indexTab).get(indexList)*/null,
        		/*this.eag2012.getToDateId().get(indexRepo).get(indexTab).get(indexList)*/null,
        		/*this.eag2012.getToDateLang().get(indexRepo).get(indexTab).get(indexList)*/null,
        		/*this.eag2012.getToDateValue().get(indexRepo).get(indexTab).get(indexList)*//*this.eag2012.getToDateStandardDate().get(indexRepo).get(indexTab).get(indexList)));*/null));
        
        childControl1.put("children", childControl1Children);
        return childControl1;
	}

	private HashMap<String, Object> buildToDate(String toDateNotAfter, String toDateNotBefore, String toDateStandardDate,String toDateId, String toDateLang, String toDateValue) {

		    HashMap<String, Object> childControl2 = new HashMap<String, Object>();
	        childControl2.put("nodeName", "toDate");
	        HashMap<String, String> childControl2Attributes = new HashMap<String,String>();
	        childControl2Attributes.put("notAfter", toDateNotAfter);
	        childControl2Attributes.put("notBefore", toDateNotBefore);
	        childControl2Attributes.put("standardDate",toDateStandardDate);
	        childControl2Attributes.put("xml:id", toDateId);
	        childControl2Attributes.put("xml:lang", toDateLang);
	        childControl2.put("attributes", childControl2Attributes);
	        childControl2.put("nodeValue", toDateValue);
	        childControl2.put("children", null);
		
		return  childControl2;
	}

	private HashMap<String, Object> buildFromDate(String fromDateNotAfter, String fromDateNotBefore, String fromDateStandardDate, String fromDateId, String fromDateLang, String fromDateValue ) {

		    HashMap<String, Object> childControl2 = new HashMap<String, Object>();
	        childControl2.put("nodeName", "fromDate");
	        HashMap<String,String> childControl2Attributes = new HashMap<String,String>();
	        childControl2Attributes.put("notAfter", fromDateNotAfter);
	        childControl2Attributes.put("notBefore",fromDateNotBefore);
	        childControl2Attributes.put("standardDate", fromDateStandardDate);
	        childControl2Attributes.put("xml:id", fromDateId);
	        childControl2Attributes.put("xml:lang", fromDateLang);
	        childControl2.put("attributes", childControl2Attributes);
	        childControl2.put("nodeValue", fromDateValue);
	        childControl2.put("children", null);
		
		return childControl2;
	}

	private HashMap<String, Object> buildTerm(String termLastDateTimeVerified, String termScriptCode,String termId, String termLang, String termValue) {
		HashMap<String, Object> childControlchild = new HashMap<String,Object>();
        childControlchild.put("nodeName", "term");
        HashMap<String, String> childControlChildAttributes = new HashMap<String, String>();
        childControlChildAttributes.put("lastDateTimeVerified", termLastDateTimeVerified);
        childControlChildAttributes.put("scriptCode", termScriptCode);
        childControlChildAttributes.put("transliteration","http://www.archivesportaleurope.net/scripts/EAG/");
        childControlChildAttributes.put("vocabularySource", "http://www.archivesportaleurope.net/vocabularies/EAG/");
        childControlChildAttributes.put("xml:id", termId);
        childControlChildAttributes.put("xml:lang", termLang);
        childControlchild.put("attributes",childControlChildAttributes);
        childControlchild.put("nodeValue",termValue);
        childControlchild.put("children", null);
        return childControlchild;
	}

	private HashMap<String, Object> buildDate(Integer indexRepo,String indexTab,Integer indexList) {
		HashMap<String, Object> childControlChild = new HashMap<String,Object>();
        childControlChild.put("nodeName", "date");
        HashMap<String, String> childControlChildAttributes = new HashMap<String, String>();
        childControlChildAttributes.put("localType", "localDateType");
//        childControlChildAttributes.put("notAfter", this.eag2012.getDateNotAfter().get(indexRepo).get(indexTab).get(indexList));
//        childControlChildAttributes.put("notBefore", this.eag2012.getDateNotBefore().get(indexRepo).get(indexTab).get(indexList));
 //        childControlChildAttributes.put("standardDate", this.eag2012.getDateStandardDate().get(indexRepo).get(indexTab).get(indexList));
//        childControlChildAttributes.put("xml:id", this.eag2012.getDateId().get(indexRepo).get(indexTab).get(indexList));
//        childControlChildAttributes.put("xml:lang", this.eag2012.getDateLang().get(indexRepo).get(indexTab).get(indexList));
        childControlChild.put("attributes",childControlChildAttributes);
        childControlChild.put("nodeValue", (this.eag2012.getDateValue()!=null && this.eag2012.getDateValue().size()>0 && this.eag2012.getDateValue().get(indexRepo)!=null && this.eag2012.getDateValue().get(indexRepo).get(indexTab)!=null && this.eag2012.getDateValue().get(indexRepo).get(indexTab)!=null)?this.eag2012.getDateValue().get(indexRepo).get(indexTab).get(indexList):null);
        childControlChild.put("children", null);
        return childControlChild;
	}

	private HashMap<String, Object> buildAbbreviation(String abbreviationId, String abbreviationLang, String  abbreviationValue) {
		HashMap<String, Object> childControlChild = new HashMap<String, Object>();
        childControlChild.put("nodeName", "abbreviation");
        HashMap<String, String> childControlChildAttributes = new HashMap<String, String>();
        childControlChildAttributes.put("xml:id", abbreviationId);
        childControlChildAttributes.put("xml:lang", abbreviationLang);
        childControlChild.put("attributes", childControlChildAttributes);
        childControlChild.put("nodeValue", abbreviationValue);
        childControlChild.put("children", null);
        return childControlChild;
	}
	
	private HashMap<String, Object> buildCitation(String citationId,String citationLang, String citationLastDateTimeVerified, String citationHref, String citationValue) {
		HashMap<String, Object> childControlChild = new HashMap<String, Object>();
        childControlChild.put("nodeName", "citation");
        HashMap<String, String> childControlChildAttributes = new HashMap<String, String>();
        childControlChildAttributes.put("xml:id", citationId);
        childControlChildAttributes.put("xml:lang", citationLang);
        childControlChildAttributes.put("lastDateTimeVerified", citationLastDateTimeVerified);
        childControlChildAttributes.put("href", citationHref);
        childControlChild.put("attributes", childControlChildAttributes);
        childControlChild.put("nodeValue", citationValue);
        childControlChild.put("children", null);
        return childControlChild;
	}

	private HashMap<String, Object> buildDescriptiveNote(Integer indexRepo,String tabKey,String sectionKey,Integer indexList) {
	
		HashMap<String, Object> childControl1 = new HashMap<String, Object>();
        childControl1.put("nodeName", "descriptiveNote");
        HashMap<String, String> childControl1Attributes = new HashMap<String, String>();
        if(this.eag2012.getDescriptiveNoteLang()!=null){
        	childControl1Attributes.put("xml:lang", (this.eag2012.getDescriptiveNoteLang() !=null && this.eag2012.getDescriptiveNoteLang().size()>indexRepo && this.eag2012.getDescriptiveNoteLang().get(indexRepo)!=null)?this.eag2012.getDescriptiveNoteLang().get(indexRepo).get(tabKey):null);
        }
        if(this.eag2012.getDescriptiveNoteId()!=null){
        	childControl1Attributes.put("xml:id",(this.eag2012.getDescriptiveNoteId()!=null && this.eag2012.getDescriptiveNoteId().size()>indexRepo && this.eag2012.getDescriptiveNoteId().get(indexRepo)!=null && this.eag2012.getDescriptiveNoteId().get(indexRepo).size()>0)?this.eag2012.getDescriptiveNoteId().get(indexRepo).get(tabKey):null);
        }
        childControl1.put("attributes",childControl1Attributes);
        childControl1.put("nodeValue", null);
        List<HashMap<String, Object>> childControl1Children = new ArrayList<HashMap<String, Object>>();
        for(int i=0; this.eag2012.getDescriptiveNotePValue()!=null && i<this.eag2012.getDescriptiveNotePValue().size();i++){ //mandatory, repeatable
        	childControl1Children.add(buildP(indexRepo,tabKey,sectionKey,indexList,i));
        }
        childControl1.put("children", childControl1Children);
        return childControl1;
	}
	
	private HashMap<String, Object> buildP(Integer indexRepo,String tabKey,String section,Integer indexList,Integer indexP) {

		HashMap<String, Object> childControl2 = new HashMap<String, Object>();
        childControl2.put("nodeName", "p");
        childControl2.put("children", null);
        HashMap<String,String> childControlChildChildAttributes = new HashMap<String,String>();
//        childControlChildChildAttributes.put("xml:id",(this.eag2012.getDescriptiveNotePId()!=null && this.eag2012.getDescriptiveNotePId().size()>indexRepo && this.eag2012.getDescriptiveNotePId().get(indexRepo)!=null && this.eag2012.getDescriptiveNotePId().get(indexRepo).size()>0)?this.eag2012.getDescriptiveNotePId().get(indexRepo).get(tabKey).get(indexP):null);
//        childControlChildChildAttributes.put("xml:lang",(this.eag2012.getDescriptiveNotePLang()!=null && this.eag2012.getDescriptiveNotePLang().size()>indexRepo && this.eag2012.getDescriptiveNotePLang().get(indexRepo).size()>0)?this.eag2012.getDescriptiveNotePLang().get(indexRepo).get(tabKey).get(indexP):null);
        childControl2.put("attributes", childControlChildChildAttributes);
        childControl2.put("nodeValue", (this.eag2012.getDescriptiveNotePValue()!=null && 
        		this.eag2012.getDescriptiveNotePValue().size()>indexRepo && 
        		this.eag2012.getDescriptiveNotePValue().get(indexRepo)!=null && 
        		this.eag2012.getDescriptiveNotePValue().get(indexRepo).size()>indexList && 
        		this.eag2012.getDescriptiveNotePValue().get(indexRepo).get(tabKey)!=null &&
				this.eag2012.getDescriptiveNotePValue().get(indexRepo).get(tabKey).size()>indexList &&
				this.eag2012.getDescriptiveNotePValue().get(indexRepo).get(tabKey).get(indexP)!=null &&
				this.eag2012.getDescriptiveNotePValue().get(indexRepo).get(tabKey).get(indexP).size()>indexP
        		)?this.eag2012.getDescriptiveNotePValue().get(indexRepo).get(tabKey).get(indexP):null);
			
		return childControl2;
	}

	private HashMap<String, ?> buildArchguide() {
		
		HashMap<String, Object> childArchguide = new HashMap<String, Object>();

		//start archguide node
		
		childArchguide.put("nodeName","archguide");
		childArchguide.put("attributes",null);
		childArchguide.put("nodeValue",null);
		
        ArrayList<HashMap<String, ?>> childrenArchguide = new ArrayList<HashMap<String,?>>();

        childrenArchguide.add(buildIdentity()); //mandatory no repeatable
        
        childrenArchguide.add(buildDesc()); //mandatory no repeatable
        
        childArchguide.put("children", childrenArchguide);
        return childArchguide;
        
	}
	
	
	private HashMap<String, ?> buildIdentity() { 
		HashMap<String, Object> childArchguide = new HashMap<String, Object>();
		childArchguide.put("nodeName","identity");
		childArchguide.put("attributes",null);
		childArchguide.put("nodeValue",null);
			
		   
		    ArrayList<HashMap<String, Object>> childArchguideChildren = new ArrayList<HashMap<String, Object>>();
		    
		    childArchguideChildren.add(buildRepositorid(this.eag2012.getRepositoridCountrycode(),this.eag2012.getRepositoridRepositorycode())); //mandatory not repeatable
		if(this.eag2012.getOtherRepositorIdValue()==null || this.eag2012.getOtherRepositorIdValue().isEmpty()){
			childArchguideChildren.add(buildOtherRepositorId(this.eag2012.getOtherRepositorIdValue())); //no mandatory not repeatable
		}
		for(int i=0;this.eag2012.getAutformLang()!=null && i<this.eag2012.getAutformLang().size();i++){ // mandatory repeatable
			if(this.eag2012.getAutformValue().get(i)!=null && !this.eag2012.getAutformValue().get(i).isEmpty()){
				childArchguideChildren.add(buildAutform(this.eag2012.getAutformLang().get(i),this.eag2012.getAutformValue().get(i)));
			}
		}
		for(int i=0;this.eag2012.getParformLang()!=null && i<this.eag2012.getParformLang().size();i++){ //no mandatory repeatable
			if(this.eag2012.getParformValue().get(i)!=null && !this.eag2012.getParformValue().get(i).isEmpty()){
				childArchguideChildren.add(buildParform(this.eag2012.getParformLang().get(i),this.eag2012.getParformValue().get(i)));
			}
		}
		for(int i=0;this.eag2012.getNonpreformLang()!=null && i<this.eag2012.getNonpreformLang().size();i++){ //no mandatory repeatable
			childArchguideChildren.add(buildNonpreform(this.eag2012.getNonpreformLang().get(i),this.eag2012.getNonpreformValue().get(i)));
		}
		for(int i=0;this.eag2012.getRepositoryTypeValue()!=null && i<this.eag2012.getRepositoryTypeValue().size();i++){//no mandatory repeatable
			childArchguideChildren.add(buildRepositoryType(this.eag2012.getRepositoryTypeValue().get(i)));
		}
		childArchguide.put("children", childArchguideChildren);
		
		return childArchguide;
	}

	private HashMap<String, Object> buildOtherRepositorId(String otherRepositorIdValue) {

		HashMap<String, Object> childArchguide1 = new HashMap<String, Object>();
        childArchguide1.put("nodeName", "otherRepositorId");
        childArchguide1.put("nodeValue", (otherRepositorIdValue==null || otherRepositorIdValue.isEmpty())?null:otherRepositorIdValue);
		
		return  childArchguide1;
	}

	private HashMap<String, Object> buildRepositorid(String repositoridCountrycode, String repositoridRepositorycode) {

		 HashMap<String, Object> childArchguide1 = new HashMap<String, Object>();
	       
	        childArchguide1.put("nodeName", "repositorid");
	        HashMap<String, String> childArchguide1Attributes = new HashMap<String, String>();
	        childArchguide1Attributes.put("countrycode",repositoridCountrycode);
	        childArchguide1Attributes.put("repositorycode",repositoridRepositorycode);  
	        childArchguide1.put("attributes", childArchguide1Attributes);
	        childArchguide1.put("nodeValue", null);
	        childArchguide1.put("children", null);
		
		return childArchguide1;
	}

	private HashMap<String, Object> buildAutform(String autformLang, String autformValue) {
        
		HashMap<String, Object> childArchguide1 = new HashMap<String, Object>();
	        childArchguide1.put("nodeName", "autform");
	        HashMap<String, String> childArchguide1Attributes = new HashMap<String, String>();
	        if(autformLang!=null){
	        	childArchguide1Attributes.put("xml:lang", autformLang);
	        	childArchguide1.put("attributes", childArchguide1Attributes);
	        }
	        childArchguide1.put("nodeValue",(autformValue==null || autformValue.isEmpty())?null:autformValue);
	        childArchguide1.put("children", null);
		
		return childArchguide1;
	}

	private HashMap<String, Object> buildParform(String parformLang,String parformValue) {

		HashMap<String, Object> childArchguide1 = new HashMap<String, Object>();
	        childArchguide1.put("nodeName", "parform");
	        HashMap<String, String>  childArchguide1Attributes = new HashMap<String, String>();
	        if(parformLang!=null){
	           childArchguide1Attributes.put("xml:lang",parformLang);
	           childArchguide1.put("attributes", childArchguide1Attributes);
	        }
	        childArchguide1.put("nodeValue", (parformValue==null || parformValue.isEmpty())?null:parformValue);
	        childArchguide1.put("children", null);
		
		
		return childArchguide1;
	}

	private HashMap<String, Object> buildRepositoryType(String repositoryTypeValue) {

		HashMap<String, Object>   childArchguide1 = new HashMap<String, Object>();
	    childArchguide1.put("nodeName", "repositoryType");
	    childArchguide1.put("attributes", null);
	    childArchguide1.put("nodeValue", repositoryTypeValue);
	    childArchguide1.put("children", null);
	
		return childArchguide1;
	}

	private HashMap<String, Object> buildNonpreform(String nonpreformLang,String nonpreformValue){ 
		
		HashMap<String, Object> childArchguide1 = new HashMap<String, Object>();
        childArchguide1.put("nodeName", "nonpreform");
        HashMap<String, String> childArchguide1Attributes = new HashMap<String, String>();
        if(nonpreformLang!=null){
          childArchguide1Attributes.put("xml:lang",nonpreformLang);
          childArchguide1.put("attributes", childArchguide1Attributes);
        }
        childArchguide1.put("nodeValue", nonpreformValue);
        
	        List<HashMap<String, Object>> childArchguide1Children = new ArrayList<HashMap<String,Object>>();
	          childArchguide1Children.add(buildUsesDates(this.eag2012.getUseDatesId(),this.eag2012.getUseDatesLang())); //no mandatory not repeatable
	        
        childArchguide1.put("children", childArchguide1Children);
        
		return childArchguide1;
	}

	private HashMap<String, Object> buildUsesDates(String useDatesId, String useDatesLang) {

	    HashMap<String, Object> childArchguide2 = new HashMap<String, Object>();
	        childArchguide2.put("nodeName", "useDates");
	        HashMap<String, String> childArchguide2Attributes = new HashMap<String, String>();
	        childArchguide2Attributes.put("xml:id",useDatesId);
	        childArchguide2Attributes.put("xml:lang",useDatesLang);
	        childArchguide2.put("attributes", childArchguide2Attributes);
	        childArchguide2.put("nodeValue", null);
	        
	        List<HashMap<String, Object>> childArchguide2Children = new ArrayList<HashMap<String,Object>>();
	        
	          for(int i=0;
	        		  (this.eag2012.getDateStandardDate()!=null && this.eag2012.getDateStandardDate().size()>0 && 
	        				  this.eag2012.getDateStandardDate().get(0).get(TAB_IDENTITY)!=null && 
	        				  this.eag2012.getDateStandardDate().get(0).get(TAB_IDENTITY).size()>i) &&
	        		  (this.eag2012.getFromDateStandardDate()!=null && this.eag2012.getFromDateStandardDate().size()>0 && 
	        		  		this.eag2012.getFromDateStandardDate().get(0).get(TAB_IDENTITY)!=null && 
	        		  		this.eag2012.getFromDateStandardDate().get(0).get(TAB_IDENTITY).size()>i)
	        		  ;i++){//no mandatory repeatable
	        	childArchguide2Children.add(buildDateSet(0,TAB_IDENTITY,i));
	        }      
	        childArchguide2.put("children", childArchguide2Children);
		
		return childArchguide2;
	}

	private HashMap<String, Object> buildDateSet(Integer indexRepo,String indexTab,Integer indexList) {
		
		HashMap<String, Object> childArchguide3 = new HashMap<String, Object>();
    	childArchguide3.put("nodeName","dateSet");
    	
    	HashMap<String, String> childArchguide3Attributes = new HashMap<String, String>();

    	childArchguide3Attributes.put("localType", "localDateType");
    	//childArchguide3Attributes.put("xml:id", this.eag2012.getDateSetId().get(indexRepo).get(indexTab).get(indexList));
    	//childArchguide3Attributes.put("xml:lang", this.eag2012.getDateSetLang().get(indexRepo).get(indexTab).get(indexList));
    	
    	childArchguide3.put("attributes",childArchguide3Attributes);
    	childArchguide3.put("nodeValue",null);
    	
        	//dateSet children	        
	        List<HashMap<String, Object>> childArchguide3Children = new ArrayList<HashMap<String,Object>>();
	        
	        for(int i=0;this.eag2012.getDateStandardDate()!=null && this.eag2012.getDateStandardDate().size()>indexRepo && this.eag2012.getDateStandardDate().get(indexRepo)!=null && this.eag2012.getDateStandardDate().get(indexRepo).get(indexTab)!=null && i<this.eag2012.getDateStandardDate().get(indexRepo).get(indexTab).size();i++){//no mandatory repeatable
	        	childArchguide3Children.add(buildDate(indexRepo,indexTab,i));
	        }
	        for(int i=0;this.eag2012.getFromDateStandardDate()!=null && this.eag2012.getFromDateStandardDate().size()>indexRepo && this.eag2012.getFromDateStandardDate().get(indexRepo)!=null && this.eag2012.getFromDateStandardDate()!=null && this.eag2012.getFromDateStandardDate().size()>0 && this.eag2012.getFromDateStandardDate().get(indexRepo).get(indexTab)!=null && i<this.eag2012.getFromDateStandardDate().get(indexRepo).get(indexTab).size();i++){  //no mandatory repeatable
	        	childArchguide3Children.add(buildDateRange(indexRepo,indexTab,i));
	        }
        childArchguide3.put("children",childArchguide3Children);    
	        
		return childArchguide3;
	}

	private HashMap<String, ?> buildDesc() { 
		
		 HashMap<String, Object> childArchguide = new HashMap<String, Object>();
	        childArchguide.put("nodeName","desc");
	        
	        childArchguide.put("attributes",null);
			childArchguide.put("nodeValue",null);
			
	        ArrayList<HashMap<String, Object>> childArchguideChildren = new ArrayList<HashMap<String, Object>>();
	        childArchguideChildren.add(buildRepositories()); //mandatory not repeatable
	        

		  childArchguide.put("children", childArchguideChildren);
		
		
		 return childArchguide;
		 
	}

	private HashMap<String, Object> buildRepositories() {
		
		HashMap<String, Object> childArchguide1 = new HashMap<String, Object>();
		childArchguide1.put("nodeName", "repositories");
        childArchguide1.put("attributes",null);
		childArchguide1.put("nodeValue",null);
		
		ArrayList<HashMap<String, Object>> childArchguide1Children = new ArrayList<HashMap<String, Object>>();
        //geogearea is unique for all repositories and mandatory, so it's used into looper into for-control-condition
		int counter = (this.eag2012.getGeogareaValue()!=null && !this.eag2012.getGeogareaValue().isEmpty())?1:0;
		counter += (this.eag2012.getRepositoryNameValue()!=null && this.eag2012.getRepositoryNameValue().size()>0)?this.eag2012.getRepositoryNameValue().size():0;
        for(int indexRepo=0;indexRepo<counter;indexRepo++){ //location-localtype is mandatory so there are the same number of repositories and localtypes  
        	childArchguide1Children.add(buildRepository(indexRepo)); //no mandatory repeatable
        }
        childArchguide1.put("children",childArchguide1Children);
        
		return childArchguide1;
	}

	private HashMap<String, Object> buildRepository(Integer indexRepo) {

       HashMap<String, Object> childArchguide2 = new HashMap<String, Object>();
        
        childArchguide2.put("nodeName", "repository");
        childArchguide2.put("attributes",null);
		childArchguide2.put("nodeValue",null);
		
        
     		ArrayList<HashMap<String, Object>> childArchguide2Children = new ArrayList<HashMap<String, Object>>();
     		if(indexRepo>0){
	     		childArchguide2Children.add(buildRepositoryName((this.eag2012.getRepositoryNameLang()!=null && this.eag2012.getRepositoryNameLang().size()>indexRepo)?this.eag2012.getRepositoryNameLang().get(indexRepo):null,(this.eag2012.getRepositoryNameValue()!=null && this.eag2012.getRepositoryNameValue().size()>indexRepo)?this.eag2012.getRepositoryNameValue().get(indexRepo):null));
	        	childArchguide2Children.add(buildRepositoryRole((this.eag2012.getRepositoryRoleValue()!=null && this.eag2012.getRepositoryRoleValue().size()>indexRepo)?this.eag2012.getRepositoryRoleValue().get(indexRepo):null)); //no mandatory not repeatable
     		}else{
     			childArchguide2Children.add(buildRepositoryRole(Eag2012.REPOSITORY_ROLE_HEAD_QUARTER)); //no mandatory not repeatable
     		}
        	childArchguide2Children.add(buildGeogarea(indexRepo)); //mandatory not repeatable
//        	if(indexRepo>0){
        		for(int i=0;this.eag2012.getStreetValue()!=null && this.eag2012.getStreetValue().size()>indexRepo && i<this.eag2012.getStreetValue().get(indexRepo).size();i++){ //first part <-> mandatory repeatable
            		childArchguide2Children.add(buildLocation(VISITORS_ADDRESS,indexRepo,i));
            	}
        		for(int i=0;this.eag2012.getPostalStreetValue()!=null && this.eag2012.getPostalStreetValue().size()>indexRepo && i<this.eag2012.getPostalStreetValue().get(indexRepo).size();i++){ //first part <-> mandatory repeatable
        			childArchguide2Children.add(buildLocation2(POSTAL_ADDRESS,indexRepo,i)); //postal address
        		}
//        	}
        	if(indexRepo>0){
	            for(int i=0;this.eag2012.getTelephoneValue()!=null && this.eag2012.getTelephoneValue().size()>indexRepo && this.eag2012.getTelephoneValue().get(indexRepo)!=null && i<this.eag2012.getTelephoneValue().get(indexRepo).size();i++){ //no mandatory repeatable
/* TODO	            	childArchguide2Children.add(buildTelephone(this.eag2012.getTelephoneValue().get(indexRepo).get(TAB_CONTACT).get(i))); */     
	            }
        	}else if(this.eag2012.getTelephoneValue()!=null && this.eag2012.getTelephoneValue().size()>0 && this.eag2012.getTelephoneValue().get(indexRepo)!=null && this.eag2012.getTelephoneValue().get(indexRepo).size()>0 && this.eag2012.getTelephoneValue().get(indexRepo).get(TAB_YOUR_INSTITUTION)!=null && this.eag2012.getTelephoneValue().get(indexRepo).get(TAB_YOUR_INSTITUTION).size()>0){
/* TODO	            	childArchguide2Children.add(buildTelephone(this.eag2012.getTelephoneValue().get(indexRepo).get(TAB_YOUR_INSTITUTION).get(0))); */     
        	}
            if(indexRepo>0){
            	for(int i=0;this.eag2012.getFaxValue()!=null && indexRepo<this.eag2012.getFaxValue().size() && this.eag2012.getFaxValue().get(indexRepo)!=null && this.eag2012.getFaxValue().get(indexRepo).get(TAB_CONTACT)!=null && this.eag2012.getFaxValue().get(indexRepo).get(TAB_CONTACT).size()>i;i++){//no mandatory repeatable
                	childArchguide2Children.add(buildFax(this.eag2012.getFaxValue().get(indexRepo).get(TAB_CONTACT).get(i)));
                }
            }
            if(indexRepo>0){
            	for(int i=0;this.eag2012.getEmailHref()!=null && this.eag2012.getEmailHref().size()>indexRepo && this.eag2012.getEmailHref().get(indexRepo)!=null && this.eag2012.getEmailHref().get(indexRepo).get(TAB_CONTACT)!=null && this.eag2012.getEmailHref().get(indexRepo).get(TAB_CONTACT).size()>i;i++){//no mandatory repeatable
                	childArchguide2Children.add(buildEmail(this.eag2012.getEmailHref().get(indexRepo).get(TAB_CONTACT).get(ROOT).get(i),this.eag2012.getEmailValue().get(indexRepo).get(TAB_CONTACT).get(ROOT).get(i)));   
                }
                for(int i=0;this.eag2012.getWebpageHref()!=null && this.eag2012.getWebpageHref().size()>indexRepo && this.eag2012.getWebpageHref().get(indexRepo)!=null && this.eag2012.getWebpageHref().get(indexRepo).get(TAB_CONTACT)!=null && this.eag2012.getWebpageHref().get(indexRepo).get(TAB_CONTACT).size()>i;i++){ //no mandatory repeatable
                	childArchguide2Children.add(buildWebpage(this.eag2012.getWebpageHref().get(indexRepo).get(TAB_CONTACT).get(ROOT).get(i),this.eag2012.getWebpageValue().get(indexRepo).get(TAB_CONTACT).get(ROOT).get(i)));  
                }
            }else{
            	if(this.eag2012.getEmailHref()!=null && this.eag2012.getEmailHref().size()>0 && this.eag2012.getEmailHref().get(indexRepo)!=null && this.eag2012.getEmailHref().get(indexRepo).size()>0 && this.eag2012.getEmailHref().get(indexRepo).get(TAB_YOUR_INSTITUTION)!=null && this.eag2012.getEmailHref().get(indexRepo).get(TAB_YOUR_INSTITUTION).size()>0){
            		childArchguide2Children.add(buildEmail(this.eag2012.getEmailHref().get(indexRepo).get(TAB_YOUR_INSTITUTION).get(ROOT).get(0),this.eag2012.getEmailValue().get(indexRepo).get(TAB_YOUR_INSTITUTION).get(ROOT).get(0)));
            	}
            	if(this.eag2012.getWebpageHref()!=null && this.eag2012.getWebpageHref().size()>0 && this.eag2012.getWebpageHref().get(indexRepo)!=null && this.eag2012.getWebpageHref().get(indexRepo).size()>0 && this.eag2012.getWebpageHref().get(indexRepo).get(TAB_YOUR_INSTITUTION)!=null && this.eag2012.getWebpageHref().get(indexRepo).get(TAB_YOUR_INSTITUTION).size()>0){
            		childArchguide2Children.add(buildWebpage(this.eag2012.getWebpageHref().get(indexRepo).get(TAB_YOUR_INSTITUTION).get(ROOT).get(0),this.eag2012.getWebpageValue().get(indexRepo).get(TAB_YOUR_INSTITUTION).get(ROOT).get(0)));
            	}
            }
            /* TODO
             * if(indexRepo>0){
            	for(int i=0;this.eag2012.getDirectionsValue()!=null && indexRepo<this.eag2012.getDirectionsValue().size() && this.eag2012.getDirectionsValue().get(indexRepo)!=null && this.eag2012.getDirectionsValue().get(indexRepo).get(TAB_CONTACT)!=null && this.eag2012.getDirectionsValue().get(indexRepo).get(TAB_CONTACT).size()>i;i++){ //no mandatory repeatable
                    childArchguide2Children.add(buildDirections(this.eag2012.getDirectionsLang().get(indexRepo).get(TAB_CONTACT).get(i),this.eag2012.getDirectionsValue().get(indexRepo).get(TAB_CONTACT).get(i),indexRepo));
                 }
            	childArchguide2Children.add(buildRepositorHist(indexRepo));    //no mandatory not repeatable                
                childArchguide2Children.add(buildRepositorfound(indexRepo));   //no mandatory not repeatable 
                childArchguide2Children.add(buildRepositorsup(indexRepo));     //no mandatory not repeatable    
                childArchguide2Children.add(buildBuildinginfo(indexRepo));     //no mandatory not repeatable 
                childArchguide2Children.add(buildAdminhierarchy(indexRepo));   //no mandatory not repeatable 
                childArchguide2Children.add(buildHoldings(indexRepo));         //no mandatory not repeatable 
            }*/

            childArchguide2Children.add(buildTimetable(indexRepo));        //mandatory not repeatable
            childArchguide2Children.add(buildAccess(indexRepo));   //mandatory not repeatable
            
            for(int i=0;this.eag2012.getAccessibilityQuestion()!=null && indexRepo<this.eag2012.getAccessibilityQuestion().size() && this.eag2012.getAccessibilityQuestion().get(indexRepo)!=null && this.eag2012.getAccessibilityQuestion().get(indexRepo).size()>i;i++){//mandatory repeatable
            	childArchguide2Children.add(buildAccessibility(indexRepo,i));              
            }
            
            childArchguide2Children.add(buildServices(indexRepo));  //no mandatory not repeatable      
            childArchguide2Children.add(buildDescriptiveNote(indexRepo,TAB_ACCESS_AND_SERVICES,REPOSITORY,0)); //no mandatory not repeatable
            
            
            
        childArchguide2.put("children", childArchguide2Children);
		
		return childArchguide2;
	}

	private HashMap<String, Object> buildServices(Integer indexRepo) {

		   //Children of repository, services
        HashMap<String, Object> childArchguide3 = new HashMap<String, Object>();
        childArchguide3.put("nodeName", "services");
        childArchguide3.put("attributes", null);
        childArchguide3.put("nodeValue", null); 
            
               //Children of services
               ArrayList<HashMap<String, Object>> childArchguide3Children = new ArrayList<HashMap<String, Object>>();   
                 childArchguide3Children.add(buildSearchroom(indexRepo));   //mandatory not repeatable
                 childArchguide3Children.add(buildLibrary(indexRepo));  //no mandatory not repeatable     
                 childArchguide3Children.add(buildInternetAccess(indexRepo));  //no mandatory not repeatable   
                 childArchguide3Children.add(buildTechservices(indexRepo));    //no mandatory not repeatable   
                 childArchguide3Children.add(buildRecreationalServices(indexRepo)); //no mandatory not repeatable 
               childArchguide3.put("children", childArchguide3Children);
		
		return childArchguide3;
	}

	private HashMap<String, Object> buildRecreationalServices(Integer indexRepo) {

	      //Children of services, recreationalServices
        HashMap<String, Object> childArchguide4 = new HashMap<String, Object>();
        childArchguide4.put("nodeName", "recreationalServices");
        childArchguide4.put("attributes", null);
        childArchguide4.put("nodeValue", null); 
              
           //Children of recreationalServices
               ArrayList<HashMap<String, Object>> childArchguide4Children = new ArrayList<HashMap<String, Object>>(); 
                
	              childArchguide4Children.add(buildRefreshment(indexRepo)); //not mandatory not repeatable
                  childArchguide4Children.add(buildExhibition(indexRepo));  //not mandatory repeatable
                  childArchguide4Children.add(buildTourSessions(indexRepo)); //not mandatory repeatable
                  childArchguide4Children.add(buildOtherServices(indexRepo)); //not mandatory repeatable
                 
                 childArchguide4.put("children",childArchguide4Children);
		
		return childArchguide4;
	}

	private HashMap<String, Object> buildOtherServices(Integer indexRepo) {

		 HashMap<String, Object> childArchguide5 = new HashMap<String, Object>();
         childArchguide5.put("nodeName", "otherServices");  //otherServices
         childArchguide5.put("attributes", null);
         childArchguide5.put("nodeValue", null); 
         ArrayList<HashMap<String, Object>> childArchguide5Children = new ArrayList<HashMap<String, Object>>(); 
            childArchguide5Children.add(buildDescriptiveNote(indexRepo,TAB_ACCESS_AND_SERVICES,OTHER_SERVICES,0)); //no mandatory not repeatable, can appear in other nodes
            	for(int i=0;this.eag2012.getWebpageHref()!=null && indexRepo<this.eag2012.getWebpageHref().size() && this.eag2012.getWebpageHref().get(indexRepo)!=null && this.eag2012.getWebpageHref().get(indexRepo).get(TAB_ACCESS_AND_SERVICES)!=null && this.eag2012.getWebpageHref().get(indexRepo).get(TAB_ACCESS_AND_SERVICES).size()>0 && this.eag2012.getWebpageHref().get(indexRepo).get(TAB_ACCESS_AND_SERVICES).get(ROOT)!=null && this.eag2012.getWebpageHref().get(indexRepo).get(TAB_ACCESS_AND_SERVICES).get(OTHER_SERVICES).size()>i;i++){ //no mandatory repeatable
             	   childArchguide5Children.add(buildWebpage(this.eag2012.getWebpageHref().get(indexRepo).get(TAB_ACCESS_AND_SERVICES).get(OTHER_SERVICES).get(i),this.eag2012.getWebpageValue().get(indexRepo).get(TAB_ACCESS_AND_SERVICES).get(OTHER_SERVICES).get(i)));
                }
        childArchguide5.put("children", childArchguide5Children);
		
		return childArchguide5;
	}

	private HashMap<String, Object> buildTourSessions(Integer indexRepo) {

		HashMap<String, Object> childArchguide5 = new HashMap<String, Object>();
        childArchguide5.put("nodeName", "toursSessions");  //toursSessions
        childArchguide5.put("attributes", null);
        childArchguide5.put("nodeValue", null); 
        ArrayList<HashMap<String, Object>> childArchguide5Children = new 	ArrayList<HashMap<String, Object>>(); 
          childArchguide5Children.add(buildDescriptiveNote(indexRepo,TAB_ACCESS_AND_SERVICES,TOURS_SESSIONS,0)); //no mandatory not repeatable, can appear in other nodes
        	  for(int i=0;this.eag2012.getWebpageHref()!=null && indexRepo<this.eag2012.getWebpageHref().size() && this.eag2012.getWebpageHref().get(indexRepo)!=null && this.eag2012.getWebpageHref().get(indexRepo).get(TAB_ACCESS_AND_SERVICES)!=null && this.eag2012.getWebpageHref().get(indexRepo).get(TAB_ACCESS_AND_SERVICES).size()>0 && this.eag2012.getWebpageHref().get(indexRepo).get(TAB_ACCESS_AND_SERVICES).get(TOURS_SESSIONS)!=null && this.eag2012.getWebpageHref().get(indexRepo).get(TAB_ACCESS_AND_SERVICES).get(TOURS_SESSIONS).size()>i;i++){ //no mandatory repeatable
            	  childArchguide5Children.add(buildWebpage(this.eag2012.getWebpageHref().get(indexRepo).get(TAB_ACCESS_AND_SERVICES).get(TOURS_SESSIONS).get(i),this.eag2012.getWebpageValue().get(indexRepo).get(TAB_ACCESS_AND_SERVICES).get(TOURS_SESSIONS).get(i)));
              }
       childArchguide5.put("children", childArchguide5Children);
		
		return childArchguide5;
	}

	private HashMap<String, Object> buildExhibition(Integer indexRepo) {

		 HashMap<String, Object> childArchguide5 = new HashMap<String, Object>();
         childArchguide5.put("nodeName", "exhibition");  //exhibition
         childArchguide5.put("attributes", null);
         childArchguide5.put("nodeValue", null); 
         ArrayList<HashMap<String, Object>> childArchguide5Children = new ArrayList<HashMap<String, Object>>(); 
          childArchguide5Children.add(buildDescriptiveNote(indexRepo,TAB_ACCESS_AND_SERVICES,EXHIBITION,0)); //no mandatory not repeatable, can appear in other nodes 
        	  for(int i=0;this.eag2012.getWebpageHref()!=null && indexRepo<this.eag2012.getWebpageHref().size() && this.eag2012.getWebpageHref().get(indexRepo)!=null && this.eag2012.getWebpageHref().get(indexRepo).get(TAB_ACCESS_AND_SERVICES)!=null && this.eag2012.getWebpageHref().get(indexRepo).get(TAB_ACCESS_AND_SERVICES).size()>0 && this.eag2012.getWebpageHref().get(indexRepo).get(TAB_ACCESS_AND_SERVICES).get(EXHIBITION)!=null && this.eag2012.getWebpageHref().get(indexRepo).get(TAB_ACCESS_AND_SERVICES).get(EXHIBITION).size()>i;i++){ //no mandatory repeatable  
            	  childArchguide5Children.add(buildWebpage(this.eag2012.getWebpageHref().get(indexRepo).get(TAB_ACCESS_AND_SERVICES).get(EXHIBITION).get(i),this.eag2012.getWebpageValue().get(indexRepo).get(TAB_ACCESS_AND_SERVICES).get(EXHIBITION).get(i)));
              } 
         childArchguide5.put("children", childArchguide5Children);
		
		return  childArchguide5;
	}

	private HashMap<String, Object> buildRefreshment(Integer indexRepo) {

		 HashMap<String, Object> childArchguide5 = new HashMap<String, Object>();
         childArchguide5.put("nodeName", "refreshment");  //refreshment
         childArchguide5.put("attributes", null);
         childArchguide5.put("nodeValue", null); 
         ArrayList<HashMap<String, Object>> childArchguide5Children = new 	ArrayList<HashMap<String, Object>>(); 
         	childArchguide5Children.add(buildDescriptiveNote(indexRepo,TAB_ACCESS_AND_SERVICES,REFRESHMENT,0));//mandatory not repeatable, can appear in other nodes
        
        childArchguide5.put("children", childArchguide5Children);
		
		return childArchguide5;
	}

	private HashMap<String, Object> buildTechservices(Integer indexRepo) {

		 //Children of services, techservices
        
        HashMap<String, Object> childArchguide4 = new HashMap<String, Object>();
        childArchguide4.put("nodeName", "techservices");
        childArchguide4.put("attributes", null);
        childArchguide4.put("nodeValue", null); 
          
       //Children of techservices
           ArrayList<HashMap<String, Object>> childArchguide4Children = new ArrayList<HashMap<String, Object>>(); 
    
              childArchguide4Children.add(buildReproductionser(indexRepo)); //mandatory not repeatable
              childArchguide4Children.add(buildRestorationLab(indexRepo));  //no mandatory not repeatable
         
        childArchguide4.put("children",  childArchguide4Children); 
		
        return childArchguide4;
	}

	private HashMap<String, Object> buildRestorationLab(Integer indexRepo) {//TODO, check if it's a missing tag

		//Children restorationlab
        HashMap<String, Object> childArchguide5 = new HashMap<String, Object>();
        childArchguide5.put("nodeName", "restorationlab");  //restorationLab
        HashMap<String, String> childArchguide5Attributes = new HashMap<String, String>();
        childArchguide5Attributes.put("question", (this.eag2012.getRestorationlabQuestion()!=null && this.eag2012.getRestorationlabQuestion().size()>indexRepo)?this.eag2012.getRestorationlabQuestion().get(indexRepo):null); //check
        childArchguide5.put("attributes", childArchguide5Attributes);
        childArchguide5.put("nodeValue", null);
        
               //Children
               ArrayList<HashMap<String, Object>> childArchguide5Children = new ArrayList<HashMap<String, Object>>(); 
               childArchguide5Children.add(buildDescriptiveNote(indexRepo,TAB_ACCESS_AND_SERVICES,RESTORATION_LAB,0)); //no mandatory not repeatable, can appear in other nodes
               childArchguide5Children.add(buildContact(indexRepo)); //not mandatory can appear in other nodes
            	   for(int i=0;this.eag2012.getWebpageHref()!=null && indexRepo<this.eag2012.getWebpageHref().size() && this.eag2012.getWebpageHref().get(indexRepo)!=null && this.eag2012.getWebpageHref().get(indexRepo).get(TAB_ACCESS_AND_SERVICES)!=null && this.eag2012.getWebpageHref().get(indexRepo).get(TAB_ACCESS_AND_SERVICES).size()>0 && this.eag2012.getWebpageHref().get(indexRepo).get(TAB_ACCESS_AND_SERVICES).get(RESTORATION_LAB)!=null && this.eag2012.getWebpageHref().get(indexRepo).get(TAB_ACCESS_AND_SERVICES).get(RESTORATION_LAB).size()>i;i++){ //no mandatory repeatable
                	   childArchguide5Children.add(buildWebpage(this.eag2012.getWebpageHref().get(indexRepo).get(TAB_ACCESS_AND_SERVICES).get(RESTORATION_LAB).get(i),this.eag2012.getWebpageValue().get(indexRepo).get(TAB_ACCESS_AND_SERVICES).get(RESTORATION_LAB).get(i)));
                   } 
         childArchguide5.put("children", childArchguide5Children);
		
		
		return  childArchguide5;
	}

	private HashMap<String, Object> buildReproductionser(Integer indexRepo) {

        HashMap<String, Object> childArchguide5 = new HashMap<String, Object>();
        childArchguide5.put("nodeName", "reproductionser");  //reproductionser
        HashMap<String, String> childArchguide5Attributes = new HashMap<String, String>();
        childArchguide5Attributes.put("question",(this.eag2012.getReproductionserQuestion()!=null && this.eag2012.getReproductionserQuestion().size()>indexRepo)?this.eag2012.getReproductionserQuestion().get(indexRepo):null);
        childArchguide5.put("attributes", childArchguide5Attributes);
        childArchguide5.put("nodeValue", null);
          
             //Children of reproductionser
        ArrayList<HashMap<String, Object>> childArchguide5Children = new ArrayList<HashMap<String, Object>>(); 
          
        childArchguide5Children.add(buildDescriptiveNote(indexRepo,TAB_ACCESS_AND_SERVICES,REPRODUCTIONSER,0)); //no mandatory no repeatable
        childArchguide5Children.add(buildContact(indexRepo)); //not mandatory can appear in other nodes
        	for(int i=0;this.eag2012.getWebpageHref()!=null && indexRepo<this.eag2012.getWebpageHref().size() && this.eag2012.getWebpageHref().get(indexRepo)!=null && this.eag2012.getWebpageHref().get(indexRepo).get(TAB_ACCESS_AND_SERVICES)!=null && this.eag2012.getWebpageHref().get(indexRepo).get(TAB_ACCESS_AND_SERVICES).size()>0 && this.eag2012.getWebpageHref().get(indexRepo).get(TAB_ACCESS_AND_SERVICES).get(REPRODUCTIONSER)!=null && this.eag2012.getWebpageHref().get(indexRepo).get(TAB_ACCESS_AND_SERVICES).get(REPRODUCTIONSER).size()>i;i++){ //no mandatory
                childArchguide5Children.add(buildWebpage(this.eag2012.getWebpageHref().get(indexRepo).get(TAB_ACCESS_AND_SERVICES).get(REPRODUCTIONSER).get(i),this.eag2012.getWebpageValue().get(indexRepo).get(TAB_ACCESS_AND_SERVICES).get(REPRODUCTIONSER).get(i)));
              }
        if(this.eag2012.getMicroformserQuestion()!=null && this.eag2012.getMicroformserQuestion().size()>indexRepo){
        	childArchguide5Children.add(buildMicroformser(this.eag2012.getMicroformserQuestion().get(indexRepo)));  //no mandatory no repeatable
        }
        if(this.eag2012.getPhotographserQuestion()!=null && this.eag2012.getPhotographserQuestion().size()>indexRepo){
        	childArchguide5Children.add(buildPhotographser(this.eag2012.getPhotographserQuestion().get(indexRepo)));  //no mandatory no repeatable
        }
        if(this.eag2012.getDigitalserQuestion()!=null && this.eag2012.getDigitalserQuestion().size()>indexRepo){
        	childArchguide5Children.add(buildDigitalser(this.eag2012.getDigitalserQuestion().get(indexRepo)));       //no mandatory no repeatable
        }
        if(this.eag2012.getPhotocopyserQuestion()!=null && this.eag2012.getPhotocopyserQuestion().size()>indexRepo){
        	childArchguide5Children.add(buildPhotocopyser(this.eag2012.getPhotocopyserQuestion().get(indexRepo)));   //no mandatory no repeatable
        }
       childArchguide5.put("children", childArchguide5Children);
		
		return childArchguide5;
	}

	private HashMap<String, Object> buildPhotocopyser(String photocopyserQuestion) {

		 HashMap<String, Object> childArchguide6 = new HashMap<String, Object>();
         childArchguide6.put("nodeName", "photocopyser");
         HashMap<String, String> childArchguide6Attributes = new HashMap<String, String>();
         childArchguide6Attributes.put("question", photocopyserQuestion);
         childArchguide6.put("attributes", childArchguide6Attributes);
         childArchguide6.put("nodeValue", null);
		
		return childArchguide6;
	}

	private HashMap<String, Object> buildDigitalser(String digitalserQuestion) {
		
		HashMap<String, Object> childArchguide6 = new HashMap<String, Object>();
         childArchguide6.put("nodeName", "digitalser");
         HashMap<String, String> childArchguide6Attributes = new HashMap<String, String>();
         childArchguide6Attributes.put("question",digitalserQuestion);
         childArchguide6.put("attributes", childArchguide6Attributes);
         childArchguide6.put("nodeValue", null);
	
		return childArchguide6;
	}

	private HashMap<String, Object> buildPhotographser(String  photographserQuestion) {

		  HashMap<String, Object> childArchguide6 = new HashMap<String, Object>();
          childArchguide6.put("nodeName", "photographser");
          HashMap<String, String> childArchguide6Attributes = new HashMap<String, String>();
          childArchguide6Attributes.put("question", photographserQuestion);
          childArchguide6.put("attributes", childArchguide6Attributes);
          childArchguide6.put("nodeValue", null);
		
		return childArchguide6;
	}

	private HashMap<String, Object> buildMicroformser(String microformserQuestion) {

		  HashMap<String, Object> childArchguide6 = new HashMap<String, Object>();
          childArchguide6.put("nodeName", "microformser");
          HashMap<String, String> childArchguide6Attributes = new HashMap<String, String>();
          childArchguide6Attributes.put("question", microformserQuestion);
          childArchguide6.put("attributes", childArchguide6Attributes);
          childArchguide6.put("nodeValue", null);
		
		return childArchguide6;
	}

	private HashMap<String, Object> buildInternetAccess(Integer indexRepo) {

		 //Children of services, internetAccess
        
        HashMap<String, Object> childArchguide4 = new HashMap<String, Object>();
        childArchguide4.put("nodeName", "internetAccess");
        HashMap<String, String> childArchguide4Attributes = new HashMap<String, String>();
        childArchguide4Attributes.put("question", (this.eag2012.getInternetAccessQuestion()!=null && this.eag2012.getInternetAccessQuestion().size()>indexRepo)?this.eag2012.getInternetAccessQuestion().get(indexRepo):null);
        childArchguide4.put("attributes", childArchguide4Attributes);
        childArchguide4.put("nodeValue", null);
          
            //Children of internetAccess
            ArrayList<HashMap<String, Object>> childArchguide4Children = new ArrayList<HashMap<String, Object>>(); 
            childArchguide4Children.add(buildDescriptiveNote(indexRepo,TAB_ACCESS_AND_SERVICES,INTERNET_ACCESS,0)); //no mandatory no repeatable
           
        childArchguide4.put("children", childArchguide4Children);
		
		return  childArchguide4;
	}

	private HashMap<String, Object> buildLibrary(Integer indexRepo) {

		//Children of services, library
        
        HashMap<String, Object> childArchguide4 = new HashMap<String, Object>();
        childArchguide4.put("nodeName", "library");
        HashMap<String, String> childArchguide4Attributes = new HashMap<String, String>();
        childArchguide4Attributes.put("question",(this.eag2012.getLibraryQuestion()!=null && this.eag2012.getLibraryQuestion().size()>indexRepo)?this.eag2012.getLibraryQuestion().get(indexRepo):null);
        childArchguide4.put("attributes", childArchguide4Attributes);
        childArchguide4.put("nodeValue", null);
          
            //Children of library
            ArrayList<HashMap<String, Object>> childArchguide4Children = new ArrayList<HashMap<String, Object>>(); 
              childArchguide4Children.add(buildContact(indexRepo)); //contact
            	  for(int i=0;this.eag2012.getWebpageHref()!=null && indexRepo<this.eag2012.getWebpageHref().size() && this.eag2012.getWebpageHref().get(indexRepo)!=null && this.eag2012.getWebpageHref().get(indexRepo).get(TAB_ACCESS_AND_SERVICES)!=null && this.eag2012.getWebpageHref().get(indexRepo).get(TAB_ACCESS_AND_SERVICES).size()>0 && this.eag2012.getWebpageHref().get(indexRepo).get(TAB_ACCESS_AND_SERVICES).get(LIBRARY)!=null && this.eag2012.getWebpageHref().get(indexRepo).get(TAB_ACCESS_AND_SERVICES).get(LIBRARY).size()>i;i++){ //no mandatory
                	  childArchguide4Children.add(buildWebpage(this.eag2012.getWebpageHref().get(indexRepo).get(TAB_ACCESS_AND_SERVICES).get(LIBRARY).get(i),this.eag2012.getWebpageValue().get(indexRepo).get(TAB_ACCESS_AND_SERVICES).get(LIBRARY).get(i))); //webpage
                  } 
              childArchguide4Children.add(buildMonographicpub(indexRepo));
              childArchguide4Children.add(buildSerialpub(indexRepo));  
       
       childArchguide4.put("children", childArchguide4Children);
		
		return childArchguide4;
	}

	private HashMap<String, Object> buildSerialpub(Integer indexRepo) {

		 HashMap<String, Object> childArchguide5 = new HashMap<String, Object>();
	     childArchguide5.put("nodeName", "serialpub");  //serialpub
	     childArchguide5.put("attributes", null);
	     childArchguide5.put("nodeValue", null);
	     ArrayList<HashMap<String, Object>> childArchguide5Children = new ArrayList<HashMap<String, Object>>(); 
/* TODO	        childArchguide5Children.add(buildNum("title",(this.eag2012.getNumValue()!=null && this.eag2012.getNumValue().size()>indexRepo)?this.eag2012.getNumValue().get(indexRepo):null)); */
	       
	     childArchguide5.put("children", childArchguide5Children);
		
		return childArchguide5;
	}

	private HashMap<String, Object> buildMonographicpub(Integer indexRepo) {

		 
        HashMap<String, Object> childArchguide5 = new HashMap<String, Object>();
        childArchguide5.put("nodeName", "monographicpub");  //monographicpub
        childArchguide5.put("attributes", null);
        childArchguide5.put("nodeValue", null);
        ArrayList<HashMap<String, Object>> childArchguide5Children = new ArrayList<HashMap<String, Object>>(); 
/* TODO          childArchguide5Children.add(buildNum("book",(this.eag2012.getNumValue()!=null)?this.eag2012.getNumValue().get(indexRepo):null)); */
      
        childArchguide5.put("children", childArchguide5Children);
		
		return childArchguide5;
	}

	private HashMap<String, Object> buildSearchroom(Integer indexRepo) {

        HashMap<String, Object> childArchguide4 = new HashMap<String, Object>();
        childArchguide4.put("nodeName", "searchroom");
        childArchguide4.put("attributes", null);
        childArchguide4.put("nodeValue", null);
          
        //Children of searchroom
         ArrayList<HashMap<String, Object>> childArchguide4Children = new ArrayList<HashMap<String, Object>>(); 
           childArchguide4Children.add(buildContact(indexRepo)); //contact
           childArchguide4Children.add(buildWorkPlaces(indexRepo));
           childArchguide4Children.add(buildComputerPlaces(indexRepo));
           childArchguide4Children.add(buildMicrofilmPlaces(indexRepo));
        	   for(int i=0;this.eag2012.getWebpageHref()!=null && indexRepo<this.eag2012.getWebpageHref().size() && this.eag2012.getWebpageHref().get(indexRepo)!=null && this.eag2012.getWebpageHref().get(indexRepo).get(TAB_ACCESS_AND_SERVICES)!=null && this.eag2012.getWebpageHref().get(indexRepo).get(TAB_ACCESS_AND_SERVICES).size()>0 && this.eag2012.getWebpageHref().get(indexRepo).get(TAB_ACCESS_AND_SERVICES).get(SEARCHROOM)!=null && this.eag2012.getWebpageHref().get(indexRepo).get(TAB_ACCESS_AND_SERVICES).get(SEARCHROOM).size()>i;i++){ //no mandatory
            	   childArchguide4Children.add(buildWebpage(this.eag2012.getWebpageHref().get(indexRepo).get(TAB_ACCESS_AND_SERVICES).get(SEARCHROOM).get(i),this.eag2012.getWebpageValue().get(indexRepo).get(TAB_ACCESS_AND_SERVICES).get(SEARCHROOM).get(i)));  //webpage
               }
/* TODO           childArchguide4Children.add(buildPhotographAllowance(this.eag2012.getPhotographAllowanceValue())); */
           
/* TODO           for(int i=0;this.eag2012.getReadersTicketLang()!=null && i<this.eag2012.getReadersTicketLang().size();i++){//no mandatory
        	   childArchguide4Children.add(buildReadersTicket(this.eag2012.getReadersTicketHref().get(indexRepo).get(i),this.eag2012.getReadersTicketLang().get(indexRepo).get(i),this.eag2012.getReadersTicketValue().get(indexRepo).get(i)));
           }*/
           for(int i=0;this.eag2012.getAdvancedOrdersLang()!=null && i<this.eag2012.getAdvancedOrdersLang().size();i++){//no mandatory
        	   childArchguide4Children.add(buildAdvancedOrders(this.eag2012.getAdvancedOrdersHref().get(indexRepo).get(i),this.eag2012.getAdvancedOrdersLang().get(indexRepo).get(i),this.eag2012.getAdvancedOrdersValue().get(indexRepo).get(i)));
           }
           
             childArchguide4Children.add(buildResearchServices(indexRepo));
           
     childArchguide4.put("children", childArchguide4Children);  
		
		
		return childArchguide4;
	}

	private HashMap<String, Object> buildResearchServices(Integer indexRepo) {

	  HashMap<String, Object> childArchguide5 = new HashMap<String, Object>();
	   childArchguide5.put("nodeName", "researchServices");  //researchservices
	   childArchguide5.put("attributes", null);
	   childArchguide5.put("nodeValue", null);
	   ArrayList<HashMap<String, Object>> childArchguide5Children = new ArrayList<HashMap<String, Object>>(); 
	      childArchguide5Children.add(buildDescriptiveNote(indexRepo,TAB_ACCESS_AND_SERVICES,RESEARCH_SERVICES,0));
	      
	    childArchguide5.put("children", childArchguide5Children);
		
		return  childArchguide5;
	}

	private HashMap<String, Object> buildAdvancedOrders(String advancedOrdersHref, String advancedOrdersLang, String advancedOrdersValue) {

		 HashMap<String, Object> childArchguide5 = new HashMap<String, Object>();
	     childArchguide5.put("nodeName", "advancedOrders");  //advancedOrders
	     HashMap<String, String> childArchguide5Attributes = new HashMap<String, String>();
	     childArchguide5Attributes.put("href", advancedOrdersHref);
	     childArchguide5Attributes.put("xml:lang", advancedOrdersLang);
	     childArchguide5.put("attributes", childArchguide5Attributes);
	     childArchguide5.put("nodeValue", advancedOrdersValue);
		
		return  childArchguide5;
	}

	private HashMap<String, Object> buildReadersTicket(String readersTicketHref, String readersTicketLang, String readersTicketValue) {
		
		HashMap<String, Object> childArchguide5 = new HashMap<String, Object>();
        childArchguide5.put("nodeName", "readersTicket");  //readersTicket
        HashMap<String, String> childArchguide5Attributes = new HashMap<String, String>();
        childArchguide5Attributes.put("href",readersTicketHref);
        childArchguide5Attributes.put("xml:lang",readersTicketLang);
        childArchguide5.put("attributes", childArchguide5Attributes);
        childArchguide5.put("nodeValue", readersTicketValue);
		
		return childArchguide5;
	}

	private HashMap<String, Object> buildPhotographAllowance(String photographAllowanceValue) {

		 HashMap<String, Object> childArchguide5 = new HashMap<String, Object>();
	        childArchguide5.put("nodeName", "photographAllowance");  //photographAllowance
	        childArchguide5.put("attributes", null);
	        childArchguide5.put("nodeValue", photographAllowanceValue);
		
		return childArchguide5;
	}

	private HashMap<String, Object> buildMicrofilmPlaces(Integer indexRepo) {

		 HashMap<String, Object> childArchguide5 = new HashMap<String, Object>();
         childArchguide5.put("nodeName", "microfilmPlaces");  //microfilmPlaces
         childArchguide5.put("attributes", null);
         childArchguide5.put("nodeValue", null);
         ArrayList<HashMap<String, Object>> childArchguide5Children = new ArrayList<HashMap<String, Object>>(); 
/* TODO            childArchguide5Children.add(buildNum("site",(this.eag2012.getNumValue()!=null && this.eag2012.getNumValue().size()>indexRepo)?this.eag2012.getNumValue().get(indexRepo):null)); */
         childArchguide5.put("children", childArchguide5Children);
		
		return childArchguide5;
	}

	private HashMap<String, Object> buildComputerPlaces(Integer indexRepo) {

		 HashMap<String, Object> childArchguide5 = new HashMap<String, Object>();
         childArchguide5.put("nodeName", "computerPlaces");  //computerPlaces
         childArchguide5.put("attributes", null);
         childArchguide5.put("nodeValue", null);
         ArrayList<HashMap<String, Object>> childArchguide5Children = new ArrayList<HashMap<String, Object>>(); 
/* TODO            childArchguide5Children.add(buildNum("site",(this.eag2012.getNumValue()!=null && this.eag2012.getNumValue().size()>indexRepo)?this.eag2012.getNumValue().get(indexRepo):null)); */
            childArchguide5Children.add(buildDescriptiveNote(indexRepo,TAB_ACCESS_AND_SERVICES,COMPUTER_PLACES,0));
          
          childArchguide5.put("children", childArchguide5Children);
		
		
		return childArchguide5;
	}

	private HashMap<String, Object> buildWorkPlaces(Integer indexRepo) {

		 HashMap<String, Object> childArchguide5 = new HashMap<String, Object>();
	     childArchguide5.put("nodeName", "workPlaces");  //workPlaces
	     childArchguide5.put("attributes", null);
	     childArchguide5.put("nodeValue", null);
	     ArrayList<HashMap<String, Object>> childArchguide5Children = new ArrayList<HashMap<String, Object>>(); 
/* TODO	        childArchguide5Children.add(buildNum("site",(this.eag2012.getNumValue()!=null && this.eag2012.getNumValue().size()>indexRepo)?this.eag2012.getNumValue().get(indexRepo):null)); */
	         
	     childArchguide5.put("children", childArchguide5Children);
			
		return  childArchguide5;
	}

	private HashMap<String, Object> buildAccessibility(Integer indexRepo,Integer indexList) {

		 //Children of repository, accessibility
   	    HashMap<String, Object> childArchguide3 = new HashMap<String, Object>();
        childArchguide3.put("nodeName", "accessibility");
        HashMap<String, String> childArchguide3Attributes = new HashMap<String, String>();
        //i),this.eag2012.getAccessibilityLang().get(indexRepo).get(i),this.eag2012.getAccessibilityValue().get(indexRepo).get(i)
          childArchguide3Attributes.put("question",(this.eag2012.getAccessibilityQuestion()!=null && this.eag2012.getAccessibilityQuestion().size()>indexRepo && this.eag2012.getAccessibilityQuestion().get(indexRepo).size()>indexList)?this.eag2012.getAccessibilityQuestion().get(indexRepo).get(indexList):null); //mandatory
/* TODO          childArchguide3Attributes.put("xml:lang",(this.eag2012.getAccessibilityLang()!=null && this.eag2012.getAccessibilityLang().size()>indexRepo && this.eag2012.getAccessibilityLang().get(indexRepo)!=null && this.eag2012.getAccessibilityLang().get(indexRepo).size()>indexList)?this.eag2012.getAccessibilityLang().get(indexRepo).get(indexList):null); */ 
          childArchguide3.put("attributes", childArchguide3Attributes);
        childArchguide3.put("nodeValue", (this.eag2012.getAccessibilityValue()!=null && this.eag2012.getAccessibilityValue().size()>indexRepo && this.eag2012.getAccessibilityValue().get(indexRepo).size()>indexList)?this.eag2012.getAccessibilityValue().get(indexRepo).get(indexList):null); 
		
		return childArchguide3;
	}

	private HashMap<String, Object> buildAccess(Integer indexRepo) {

		 //Children of repository, access
	    
	    HashMap<String, Object> childArchguide3 = new HashMap<String, Object>();
        childArchguide3.put("nodeName", "access");
        HashMap<String, String> childArchguide3Attributes = new HashMap<String, String>();
/* TODO         childArchguide3Attributes.put("question",this.eag2012.getAccessQuestion()); */
         childArchguide3.put("attributes", childArchguide3Attributes);
        childArchguide3.put("nodeValue", null); 
        
            //children of access
            
           ArrayList<HashMap<String, Object>> childArchguide3Children = new ArrayList<HashMap<String, Object>>();   
           
             for(int i=0;this.eag2012.getRestaccessValue()!=null && 
            		 indexRepo<this.eag2012.getRestaccessValue().size() &&
            		 this.eag2012.getRestaccessValue().get(indexRepo)!=null &&
    				 this.eag2012.getRestaccessValue().get(indexRepo).size()>i
            		 ;i++){//no mandatory repeatable
              childArchguide3Children.add(buildRestaccess(indexRepo,i));
             }
             for(int i=0;this.eag2012.getTermsOfUseLang()!=null && i<this.eag2012.getTermsOfUseLang().size();i++){//no mandatory repeatable
               childArchguide3Children.add(buildTermsOfUse(this.eag2012.getTermsOfUseHref().get(indexRepo).get(i),this.eag2012.getTermsOfUseLang().get(indexRepo).get(i),this.eag2012.getTermsOfUseValue().get(indexRepo).get(i)));
             }
        childArchguide3.put("children", childArchguide3Children);
		
		return  childArchguide3;
	}

	private HashMap<String, Object> buildTermsOfUse(String termsOfUseHref, String termsOfUseLang, String termsOfUseValue) {

		 HashMap<String, Object> childArchguide4 = new HashMap<String, Object>();
         childArchguide4.put("nodeName", "termsOfUse");
         HashMap<String, String> childArchguide4Attributes = new HashMap<String, String>();
         childArchguide4Attributes.put("href",termsOfUseHref);
         childArchguide4Attributes.put("xml:lang",termsOfUseLang);
         childArchguide4.put("attributes", childArchguide4Attributes);
         childArchguide4.put("nodeValue", termsOfUseValue);
		
		return childArchguide4;
	}

	private HashMap<String, Object> buildRestaccess(Integer indexRepo,Integer indexList) {
		HashMap<String, Object> childArchguide4 = new HashMap<String, Object>();
        childArchguide4.put("nodeName", "restaccess");
        HashMap<String, String> childArchguide4Attributes = new HashMap<String, String>();
/* TODO       childArchguide4Attributes.put("xml:lang",(this.eag2012.getRestaccessLang()!=null && this.eag2012.getRestaccessLang().size()>indexRepo && this.eag2012.getRestaccessLang().get(indexRepo)!=null && this.eag2012.getRestaccessLang().get(indexRepo).size()>indexList)?this.eag2012.getRestaccessLang().get(indexRepo).get(indexList):null); */
        childArchguide4.put("attributes", childArchguide4Attributes);
        childArchguide4.put("nodeValue", (this.eag2012.getRestaccessValue()!=null && this.eag2012.getRestaccessValue().size()>indexRepo && this.eag2012.getRestaccessValue().get(indexRepo)!=null && this.eag2012.getRestaccessValue().get(indexRepo).size()>indexList)?this.eag2012.getRestaccessValue().get(indexRepo).get(indexList):null);
		return childArchguide4;
	}

	private HashMap<String, Object> buildTimetable(Integer indexRepo) {

		 //Children of repository, timetable
	    
	    HashMap<String, Object> childArchguide3 = new HashMap<String, Object>();
        childArchguide3.put("nodeName", "timetable");
        childArchguide3.put("attributes",null);
        childArchguide3.put("nodeValue", null); 
	         
           //Children of timetable
           ArrayList<HashMap<String, Object>> childArchguide3Children = new ArrayList<HashMap<String, Object>>();   
           if(indexRepo>0){
/* TODO        	   childArchguide3Children.add(buildOpening((this.eag2012.getOpeningValue()!=null && this.eag2012.getOpeningValue().size()>indexRepo && this.eag2012.getOpeningValue().get(indexRepo)!=null && this.eag2012.getOpeningValue().get(indexRepo).get(TAB_ACCESS_AND_SERVICES)!=null)?this.eag2012.getOpeningValue().get(indexRepo).get(TAB_ACCESS_AND_SERVICES):null)); //mandatory not repeatable */
        	   if(this.eag2012.getClosingStandardDate()!=null && this.eag2012.getClosingStandardDate().size()>indexRepo && this.eag2012.getClosingStandardDate().get(indexRepo)!=null && this.eag2012.getClosingStandardDate().get(indexRepo).get(TAB_ACCESS_AND_SERVICES)!=null){
/* TODO        		   childArchguide3Children.add(buildClosing((this.eag2012.getClosingStandardDate()!=null && this.eag2012.getClosingStandardDate().size()>indexRepo && this.eag2012.getClosingStandardDate().get(indexRepo)!=null && this.eag2012.getClosingStandardDate().get(indexRepo).get(TAB_ACCESS_AND_SERVICES)!=null)?this.eag2012.getClosingStandardDate().get(indexRepo).get(TAB_ACCESS_AND_SERVICES):null,(this.eag2012.getClosingValue()!=null && this.eag2012.getClosingValue().size()>0 && this.eag2012.getClosingValue().get(indexRepo)!=null && this.eag2012.getClosingValue().get(indexRepo).get(TAB_ACCESS_AND_SERVICES)!=null)?this.eag2012.getClosingValue().get(indexRepo).get(TAB_ACCESS_AND_SERVICES):null)); //not mandatory not repeatable */
        	   }
           }else{
/* TODO        	   childArchguide3Children.add(buildOpening((this.eag2012.getOpeningValue()!=null && this.eag2012.getOpeningValue().size()>indexRepo && this.eag2012.getOpeningValue().get(indexRepo)!=null && this.eag2012.getOpeningValue().get(indexRepo).get(TAB_YOUR_INSTITUTION)!=null)?this.eag2012.getOpeningValue().get(indexRepo).get(TAB_YOUR_INSTITUTION):null)); //mandatory not repeatable */
        	   if(this.eag2012.getClosingStandardDate()!=null && this.eag2012.getClosingStandardDate().size()>indexRepo && this.eag2012.getClosingStandardDate().get(indexRepo)!=null && this.eag2012.getClosingStandardDate().get(indexRepo).get(TAB_YOUR_INSTITUTION)!=null){
/* TODO        		   childArchguide3Children.add(buildClosing((this.eag2012.getClosingStandardDate()!=null && this.eag2012.getClosingStandardDate().size()>indexRepo && this.eag2012.getClosingStandardDate().get(indexRepo)!=null && this.eag2012.getClosingStandardDate().get(indexRepo).get(TAB_YOUR_INSTITUTION)!=null)?this.eag2012.getClosingStandardDate().get(indexRepo).get(TAB_YOUR_INSTITUTION):null,(this.eag2012.getClosingValue()!=null && this.eag2012.getClosingValue().size()>0 && this.eag2012.getClosingValue().get(indexRepo)!=null && this.eag2012.getClosingValue().get(indexRepo).get(TAB_YOUR_INSTITUTION)!=null)?this.eag2012.getClosingValue().get(indexRepo).get(TAB_YOUR_INSTITUTION):null)); //not mandatory not repeatable */
        	   }
           }
	    
             childArchguide3.put("children", childArchguide3Children);
		
		return childArchguide3;
	}

	private HashMap<String, Object> buildClosing(String closingStandardDate,String closingValue ) {

		 HashMap<String, Object> childArchguide4 = new HashMap<String, Object>();
         childArchguide4.put("nodeName", "closing");
         HashMap<String, String> childArchguide4Attributes = new HashMap<String, String>();
         childArchguide4Attributes.put("standardDate",closingStandardDate);
         childArchguide4.put("attributes", childArchguide4Attributes);
         childArchguide4.put("nodeValue", closingValue);
		
		return childArchguide4;
	}

	private HashMap<String, Object> buildOpening(String openingValue) {

		 HashMap<String, Object> childArchguide4 = new HashMap<String, Object>();
         childArchguide4.put("nodeName", "opening");
         childArchguide4.put("attributes", null);
         childArchguide4.put("nodeValue", openingValue);
     
		return childArchguide4;
	}

	private HashMap<String, Object> buildHoldings(Integer indexRepo) {

		 //Children holdings, children of repository
 	   HashMap<String, Object> childArchguide3 = new HashMap<String, Object>();
        childArchguide3.put("nodeName", "holdings");
        childArchguide3.put("attributes",null);
        childArchguide3.put("nodeValue", null); 
          
          //Children of holdings
          ArrayList<HashMap<String, Object>> childArchguide3Children = new ArrayList<HashMap<String, Object>>();   
 	        childArchguide3Children.add(buildDescriptiveNote(indexRepo,TAB_CONTACT,HOLDINGS,0)); //no mandatory not repeatable, can appear in other nodes
 	       
 	        for(int i=0;this.eag2012.getDateSetLang()!=null && indexRepo<this.eag2012.getDateSetLang().size() && this.eag2012.getDateSetLang().get(indexRepo)!=null && this.eag2012.getDateSetLang().get(indexRepo).get(TAB_CONTACT)!=null && this.eag2012.getDateSetLang().get(indexRepo).get(TAB_CONTACT).size()>i;i++){ //no mandatory repeatable
 	        	childArchguide3Children.add(buildDateSet(indexRepo,TAB_CONTACT,i));
 	        }
 	        childArchguide3Children.add(buildExtent(indexRepo)); //no mandatory not repeatable
 	    
 	        childArchguide3.put("children", childArchguide3Children);
		
		
		return childArchguide3;
	}

	private HashMap<String, Object> buildExtent(Integer indexRepo) {

		 HashMap<String, Object> childArchguide4 = new HashMap<String, Object>();
	      childArchguide4.put("nodeName", "extent");
	      childArchguide4.put("attributes", null);
	      childArchguide4.put("nodeValue", null);
	      
	           //Children of extent
	           ArrayList<HashMap<String, Object>> childArchguide4Children = new ArrayList<HashMap<String, Object>>();
/* TODO                  childArchguide4Children.add(buildNum("linearmetre",(this.eag2012.getNumValue()!=null && this.eag2012.getNumValue().size()>indexRepo)?this.eag2012.getNumValue().get(indexRepo):null)); */
          
	      childArchguide4.put("children", childArchguide4Children);
		
		return childArchguide4;
	}

	private HashMap<String, Object> buildAdminhierarchy(Integer indexRepo) {

		  //Children of repository, adminhierarchy
 	    HashMap<String, Object> childArchguide3 = new HashMap<String, Object>();
        childArchguide3.put("nodeName", "adminhierarchy");
        childArchguide3.put("attributes",null);
        childArchguide3.put("nodeValue", null); 
           
           //Children of adminhierarchy
           ArrayList<HashMap<String, Object>> childArchguide3Children = new ArrayList<HashMap<String, Object>>();
           
           for(int i=0;this.eag2012.getAdminunitValue()!=null && indexRepo<this.eag2012.getAdminunitValue().size() && this.eag2012.getAdminunitValue().get(indexRepo)!=null && this.eag2012.getAdminunitValue().get(indexRepo).size()>i;i++){//mandatory repeatable
        	   childArchguide3Children.add(buildAdminunit(this.eag2012.getAdminunitLang().get(indexRepo).get(i),this.eag2012.getAdminunitValue().get(indexRepo).get(i)));                        
           }
           
          childArchguide3.put("children", childArchguide3Children);
		
		return childArchguide3;
	}

	private HashMap<String, Object> buildAdminunit(String adminunitLang, String adminunitValue ) {

		 HashMap<String, Object> childArchguide4 = new HashMap<String, Object>();
         childArchguide4.put("nodeName", "adminunit");
         HashMap<String, String> childArchguide4Attributes = new HashMap<String, String>();
         childArchguide4Attributes.put("xml:lang", adminunitLang);
         childArchguide4.put("attributes", childArchguide4Attributes);
         childArchguide4.put("nodeValue", adminunitValue);
		
		return childArchguide4;
	}

	private HashMap<String, Object> buildBuildinginfo(Integer indexRepo) {

		HashMap<String, Object> childArchguide3 = new HashMap<String, Object>();
         childArchguide3.put("nodeName", "buildinginfo");
         childArchguide3.put("attributes",null);
         childArchguide3.put("nodeValue", null);
     	
          ArrayList<HashMap<String, Object>> childArchguide3Children = new ArrayList<HashMap<String, Object>>();   
     	        
              childArchguide3Children.add(buildBuilding(indexRepo)); //no mandatory not repeatable
              childArchguide3Children.add(buildRepositorarea(indexRepo));   //no mandatory not repeatable  
     	      childArchguide3Children.add(buildLengthshelf(indexRepo));  //no mandatory not repeatable
     	       
     	childArchguide3.put("children", childArchguide3Children);  	
		
		return childArchguide3;
	}

	
	private HashMap<String, Object> buildLengthshelf(Integer indexRepo) {

		HashMap<String, Object> childArchguide4 = new HashMap<String, Object>();
        childArchguide4.put("nodeName", "lengthshelf");
        childArchguide4.put("attributes", null);
        childArchguide4.put("nodeValue", null);
        ArrayList<HashMap<String, Object>> childArchguide4Children = new ArrayList<HashMap<String, Object>>();
/* TODO          childArchguide4Children.add(buildNum("linearmetre",(this.eag2012.getNumValue()!=null && this.eag2012.getNumValue().size()>indexRepo)?this.eag2012.getNumValue().get(indexRepo):null)); //mandatory not repeatable, can appear in other nodes */
        
        childArchguide4.put("children",childArchguide4Children);
		
		return childArchguide4;
	}

	private HashMap<String, Object> buildRepositorarea(Integer indexRepo){
		HashMap<String, Object> childArchguide4 = new HashMap<String, Object>();
	       childArchguide4.put("nodeName", "repositorarea");
	       childArchguide4.put("attributes", null);
	       childArchguide4.put("nodeValue", null);
	       ArrayList<HashMap<String, Object>> childArchguide4Children = new ArrayList<HashMap<String, Object>>();
/* TODO	         childArchguide4Children.add(buildNum("linearmetre",(this.eag2012.getNumValue()!=null && this.eag2012.getNumValue().size()>indexRepo)?this.eag2012.getNumValue().get(indexRepo):null)); //mandatory not repeatable, can appear in other nodes */ 
                       	       
           childArchguide4.put("children",childArchguide4Children);	
		
		return childArchguide4;
		
	}
	private HashMap<String, Object> buildBuilding(Integer indexRepo){
		
		HashMap<String, Object>childArchguide4 = new HashMap<String, Object>();
	      childArchguide4.put("nodeName", "building");
          childArchguide4.put("attributes", null);
          childArchguide4.put("nodeValue", null);
        
          ArrayList<HashMap<String, Object>> childArchguide4Children = new ArrayList<HashMap<String, Object>>();
             childArchguide4Children.add(buildDescriptiveNote(indexRepo,TAB_CONTACT,BUILDING,0));
        
          childArchguide4.put("children",childArchguide4Children);	
		
	return	childArchguide4;
	}
	
	private HashMap<String, Object> buildRepositorsup(Integer indexRepo) {

		HashMap<String,Object> childArchguide3 = new HashMap<String, Object>();
         childArchguide3.put("nodeName", "repositorsup");
         childArchguide3.put("attributes",null);
         childArchguide3.put("nodeValue", null);
     	
         ArrayList<HashMap<String, Object>>  childArchguide3Children = new ArrayList<HashMap<String, Object>>();   
     	   
         for(int i=0;this.eag2012.getDateLang()!=null && indexRepo<this.eag2012.getDateLang().size() && this.eag2012.getDateLang().get(indexRepo)!=null && this.eag2012.getDateLang().get(indexRepo).get(TAB_CONTACT)!=null && this.eag2012.getDateLang().get(indexRepo).get(TAB_CONTACT).size()>i;i++){ //no mandatory repeatable
        	 childArchguide3Children.add(buildDate(indexRepo,TAB_CONTACT,i));
         } 
         for(int i=0; this.eag2012.getRuleValue()!=null && indexRepo<this.eag2012.getRuleValue().size() && this.eag2012.getRuleValue().get(indexRepo)!=null && this.eag2012.getRuleValue().get(indexRepo).size()>i;i++){//no mandatory repeatable
        	 childArchguide3Children.add(buildRule(indexRepo,i));    	       
         }  
         childArchguide3.put("children", childArchguide3Children);
		
		return childArchguide3;
	}

	private HashMap<String, Object> buildRepositorfound(Integer indexRepo) {

		 HashMap<String, Object> childArchguide3 = new HashMap<String, Object>();
          childArchguide3.put("nodeName", "repositorfound");
          childArchguide3.put("attributes",null);
   	      childArchguide3.put("nodeValue", null);
   	
   	      ArrayList<HashMap<String, Object>> childArchguide3Children = new ArrayList<HashMap<String, Object>>();   
   	       
   	      for(int i=0;this.eag2012.getDateLang()!=null && indexRepo<this.eag2012.getDateLang().size() && this.eag2012.getDateLang().get(indexRepo)!=null && this.eag2012.getDateLang().get(indexRepo).get(TAB_CONTACT)!=null && this.eag2012.getDateLang().get(indexRepo).get(TAB_CONTACT).size()>i;i++){ //no mandatory repeatable
   	         childArchguide3Children.add(buildDate(indexRepo,TAB_CONTACT,i));
   	      }
   	      for(int i=0; this.eag2012.getRuleValue()!=null && indexRepo<this.eag2012.getRuleValue().size() && this.eag2012.getRuleValue().get(indexRepo)!=null && this.eag2012.getRuleValue().get(indexRepo).size()>i;i++){//no mandatory repeatable
   	         childArchguide3Children.add(buildRule(indexRepo,i));    	       
   	      }
             childArchguide3.put("children", childArchguide3Children);
		
		return childArchguide3;
	}

	private HashMap<String, Object> buildRepositorHist(Integer indexRepo) {

		HashMap<String, Object> childArchguide3 = new HashMap<String, Object>();
         childArchguide3.put("nodeName", "repositorhist");
         childArchguide3.put("attributes",null);
  	     childArchguide3.put("nodeValue", null);
  	     ArrayList<HashMap<String, Object>>childArchguide3Children = new ArrayList<HashMap<String, Object>>();
   	       childArchguide3Children.add(buildDescriptiveNote(indexRepo,TAB_CONTACT,REPOSITORHIST,0)); //mandatory not repeatable
   	       
   	     childArchguide3.put("children", childArchguide3Children);  
			
		return childArchguide3;
	}

	private HashMap<String, Object> buildDirections(String directionsLang, String directionsValue,Integer indexRepo) {

		 HashMap<String, Object>childArchguide3 = new HashMap<String, Object>();
         childArchguide3.put("nodeName", "directions");
         HashMap<String, String>childArchguide3Attributes = new HashMap<String, String>();
     	 childArchguide3Attributes.put("xml:lang", directionsLang);
         childArchguide3.put("attributes",childArchguide3Attributes);
     	 childArchguide3.put("nodeValue", directionsValue);
     	 ArrayList<HashMap<String, Object>> childArchguide3Children = new ArrayList<HashMap<String, Object>>();
 	        for(int i=0;this.eag2012.getCitationHref()!=null && this.eag2012.getCitationHref().size()>indexRepo && this.eag2012.getCitationHref().get(indexRepo)!=null && i<this.eag2012.getCitationHref().get(indexRepo).size();i++){ //no mandatory
 	        	childArchguide3Children.add(buildCitation(this.eag2012.getCitationId().get(indexRepo).get(i),this.eag2012.getCitationLang().get(indexRepo).get(i),this.eag2012.getCitationLastDateTimeVerified().get(indexRepo).get(i),this.eag2012.getCitationHref().get(indexRepo).get(i),this.eag2012.getCitationValue().get(indexRepo).get(i)));
 	        }
	       childArchguide3.put("children", childArchguide3Children);
		
		return childArchguide3;
	}

	private HashMap<String, Object> buildLocation2(String locationLocalType,Integer indexRepo,Integer indexList) {

		   HashMap<String,Object> childArchguide3 = new HashMap<String, Object>();
	    	childArchguide3.put("nodeName", "location");
	    	HashMap<String, String>childArchguide3Attributes = new HashMap<String, String>();
	    	childArchguide3Attributes.put("localType", locationLocalType);
	    	childArchguide3.put("attributes",childArchguide3Attributes);
	    	childArchguide3.put("nodeValue", null);
	    	  
	    	  ArrayList<HashMap<String, Object>> childArchguide3Children = new ArrayList<HashMap<String, Object>>();
	    	  
	          childArchguide3Children.add(buildMunicipalityPostalCode((this.eag2012.getMunicipalityPostalcodeLang()!=null && this.eag2012.getMunicipalityPostalcodeLang().size()>indexRepo && this.eag2012.getMunicipalityPostalcodeLang().get(indexRepo)!=null && this.eag2012.getMunicipalityPostalcodeLang().get(indexRepo).size()>indexList)?this.eag2012.getMunicipalityPostalcodeLang().get(indexRepo).get(indexList):null,(this.eag2012.getMunicipalityPostalcodeValue()!=null && this.eag2012.getMunicipalityPostalcodeValue().size()>indexRepo && this.eag2012.getMunicipalityPostalcodeValue().get(indexRepo)!=null && this.eag2012.getMunicipalityPostalcodeValue().get(indexRepo).size()>indexList)?this.eag2012.getMunicipalityPostalcodeValue().get(indexRepo).get(indexList):null));
	          childArchguide3Children.add(buildStreet((this.eag2012.getPostalStreetLang()!=null && this.eag2012.getPostalStreetLang().size()>indexRepo && this.eag2012.getPostalStreetLang().get(indexRepo)!=null && this.eag2012.getPostalStreetLang().get(indexRepo).size()>indexList)?this.eag2012.getPostalStreetLang().get(indexRepo).get(indexList):null,(this.eag2012.getPostalStreetValue()!=null && this.eag2012.getPostalStreetValue().size()>indexRepo && this.eag2012.getPostalStreetValue().get(indexRepo)!=null && this.eag2012.getPostalStreetValue().get(indexRepo).size()>indexList)?this.eag2012.getPostalStreetValue().get(indexRepo).get(indexList):null));
	          
	          childArchguide3.put("children", childArchguide3Children);
			
			return childArchguide3;
		}
	
	private HashMap<String, Object> buildLocation(String locationLocalType, Integer indexRepo,Integer indexList) {

	   HashMap<String,Object> childArchguide3 = new HashMap<String, Object>();
    	childArchguide3.put("nodeName", "location");
    	HashMap<String, String>childArchguide3Attributes = new HashMap<String, String>();
    	childArchguide3Attributes.put("localType", locationLocalType);
/* TODO    	childArchguide3Attributes.put("latitude", (this.eag2012.getLocationLongitude()!=null && this.eag2012.getLocationLongitude().size()>indexRepo && this.eag2012.getLocationLongitude().get(indexRepo)!=null && this.eag2012.getLocationLongitude().get(indexRepo).size()>indexList)?this.eag2012.getLocationLongitude().get(indexRepo).get(indexList):null); */
/* TODO		childArchguide3Attributes.put("longitude", (this.eag2012.getLocationLatitude()!=null && this.eag2012.getLocationLatitude().size()>indexRepo && this.eag2012.getLocationLatitude().get(indexRepo)!=null && this.eag2012.getLocationLatitude().get(indexRepo).size()>indexList)?this.eag2012.getLocationLatitude().get(indexRepo).get(indexList):null); */
    	childArchguide3.put("attributes",childArchguide3Attributes);
    	childArchguide3.put("nodeValue", null);
    	  
    	  ArrayList<HashMap<String, Object>> childArchguide3Children = new ArrayList<HashMap<String, Object>>();
    	  
/* TODO          childArchguide3Children.add(buildCountry((this.eag2012.getCountryLang()!=null && this.eag2012.getCountryLang().size()>indexRepo && this.eag2012.getCountryLang().get(indexRepo)!=null && this.eag2012.getCountryLang().get(indexRepo).size()>indexList)?this.eag2012.getCountryLang().get(indexRepo).get(indexList):null,(this.eag2012.getCountryValue()!=null && this.eag2012.getCountryValue().size()>indexRepo && this.eag2012.getCountryValue().get(indexRepo)!=null && this.eag2012.getCountryValue().get(indexRepo).size()>indexList)?this.eag2012.getCountryValue().get(indexRepo).get(indexList):null)); */
          childArchguide3Children.add(buildFirstdem((this.eag2012.getFirstdemLang()!=null && this.eag2012.getFirstdemLang().size()>indexRepo && this.eag2012.getFirstdemLang().get(indexRepo)!=null && this.eag2012.getFirstdemLang().get(indexRepo).size()>indexList)?this.eag2012.getFirstdemLang().get(indexRepo).get(indexList):null,(this.eag2012.getFirstdemValue()!=null && this.eag2012.getFirstdemValue().size()>indexRepo && this.eag2012.getFirstdemValue().get(indexRepo)!=null && this.eag2012.getFirstdemValue().get(indexRepo).size()>indexList)?this.eag2012.getFirstdemValue().get(indexRepo).get(indexList):null));
          childArchguide3Children.add(buildSecondem((this.eag2012.getSecondemLang()!=null && this.eag2012.getSecondemLang().size()>indexRepo && this.eag2012.getSecondemLang().get(indexRepo)!=null && this.eag2012.getSecondemLang().get(indexRepo).size()>indexList)?this.eag2012.getSecondemLang().get(indexRepo).get(indexList):null,(this.eag2012.getSecondemValue()!=null && this.eag2012.getSecondemValue().size()>indexRepo && this.eag2012.getSecondemValue().get(indexRepo)!=null && this.eag2012.getSecondemValue().get(indexRepo).size()>indexList)?this.eag2012.getSecondemValue().get(indexRepo).get(indexList):null));
/* TODO          childArchguide3Children.add(buildMunicipalityPostalCode((this.eag2012.getCitiesLang()!=null && this.eag2012.getCitiesLang().size()>0 && this.eag2012.getCitiesLang().get(indexRepo)!=null && this.eag2012.getCitiesLang().get(indexRepo).size()>indexList)?this.eag2012.getCitiesLang().get(indexRepo).get(indexList):null,(this.eag2012.getCitiesValue()!=null && this.eag2012.getCitiesValue().size()>0 && this.eag2012.getCitiesValue().get(indexRepo)!=null && this.eag2012.getCitiesValue().get(indexRepo).size()>indexList)?this.eag2012.getCitiesValue().get(indexRepo).get(indexList):null)); */
          childArchguide3Children.add(buildLocalentity((this.eag2012.getLocalentityLang()!=null && this.eag2012.getLocalentityLang().size()>indexRepo)?this.eag2012.getLocalentityLang().get(indexRepo):null,(this.eag2012.getLocalentityValue()!=null && this.eag2012.getLocalentityValue().size()>indexRepo)?this.eag2012.getLocalentityValue().get(indexRepo):null));
/* TODO          childArchguide3Children.add(buildStreet((this.eag2012.getStreetLang()!=null && this.eag2012.getStreetLang().size()>0 && this.eag2012.getStreetLang().get(indexRepo)!=null && this.eag2012.getStreetLang().get(indexRepo).size()>indexList)?this.eag2012.getStreetLang().get(indexRepo).get(indexList):null,(this.eag2012.getStreetValue()!=null && this.eag2012.getStreetValue().size()>0 && this.eag2012.getStreetValue().get(indexRepo)!=null && this.eag2012.getStreetValue().get(indexRepo).size()>indexList)?this.eag2012.getStreetValue().get(indexRepo).get(indexList):null)); */
          
          childArchguide3.put("children", childArchguide3Children);
		
		return childArchguide3;
	}

	private HashMap<String, Object> buildStreet(String streetLang, String streetValue) {

		HashMap<String, Object> childArchguide4 = new HashMap<String, Object>();
        childArchguide4.put("nodeName","street");
        HashMap<String, String> childArchguide4Attributes = new HashMap<String, String>();
        childArchguide4Attributes.put("xml:lang",streetLang);
        childArchguide4.put("attributes", childArchguide4Attributes);
        childArchguide4.put("nodeValue", streetValue);
        
		return  childArchguide4;
	}

	private HashMap<String, Object> buildLocalentity(String localentityLang, String localentityValue ) {

		HashMap<String, Object> childArchguide4 = new HashMap<String, Object>();
        childArchguide4.put("nodeName","localentity");
        HashMap<String, String> childArchguide4Attributes = new HashMap<String, String>();
        childArchguide4Attributes.put("xml:lang", localentityLang);
        childArchguide4.put("attributes", childArchguide4Attributes);
        childArchguide4.put("nodeValue", localentityValue);
		
		return childArchguide4;
	}

	private HashMap<String, Object> buildMunicipalityPostalCode(String municipalityPostalcodeLang, String municipalityPostalcodeValue) {

		 HashMap<String, Object> childArchguide4 = new HashMap<String, Object>();
         childArchguide4.put("nodeName","municipalityPostalcode");
         HashMap<String, String> childArchguide4Attributes = new HashMap<String, String>();
         childArchguide4Attributes.put("xml:lang", municipalityPostalcodeLang);
         childArchguide4.put("attributes", childArchguide4Attributes);
         childArchguide4.put("nodeValue",municipalityPostalcodeValue);
		
		
		return  childArchguide4;
	}

	private HashMap<String, Object> buildSecondem(String secondemLang, String secondemValue) {

		 HashMap<String, Object> childArchguide4 = new HashMap<String, Object>();
         childArchguide4.put("nodeName","secondem");
         HashMap<String, String> childArchguide4Attributes = new HashMap<String, String>();
         childArchguide4Attributes.put("xml:lang", secondemLang);
         childArchguide4.put("attributes", childArchguide4Attributes);
         childArchguide4.put("nodeValue", secondemValue);
		
		return childArchguide4;
	}

	private HashMap<String, Object> buildFirstdem(String firstdemLang, String firstdemValue) {

		HashMap<String, Object> childArchguide4 = new HashMap<String, Object>();
        childArchguide4.put("nodeName","firstdem");
        HashMap<String, String> childArchguide4Attributes = new HashMap<String, String>();
        childArchguide4Attributes.put("xml:lang", firstdemLang);
        childArchguide4.put("attributes", childArchguide4Attributes);
        childArchguide4.put("nodeValue", firstdemValue);
        
		return childArchguide4;
	}

	private HashMap<String, Object> buildCountry(String countryLang, String countryValue) {

		HashMap<String, Object> childArchguide4 = new HashMap<String, Object>();
  	    childArchguide4.put("nodeName", "country");
        HashMap<String, String> childArchguide4Attributes = new HashMap<String, String>();
        childArchguide4Attributes.put("xml:lang",countryLang);
        childArchguide4.put("attributes", childArchguide4Attributes);
        childArchguide4.put("nodeValue", countryValue);
        
		return childArchguide4;
	}

	private HashMap<String, Object> buildGeogarea(Integer indexRepo) {

		 HashMap<String, Object> childArchguide3 = new HashMap<String, Object>();
		    childArchguide3.put("nodeName", "geogarea");
		    HashMap<String, String>childArchguide3Attributes = new HashMap<String, String>();
      	    childArchguide3Attributes.put("xml:lang", this.eag2012.getGeogareaLang()); 
      	    childArchguide3.put("attributes",childArchguide3Attributes);
      	    childArchguide3.put("nodeValue", this.eag2012.getGeogareaValue() );
		
		return childArchguide3;
	}

	private HashMap<String, Object> buildRepositoryRole(String repositoryRoleValue) {

		  HashMap<String, Object> childArchguide3 = new HashMap<String, Object>();
		    childArchguide3.put("nodeName", "repositoryRole");
		    childArchguide3.put("attributes", null);
		    childArchguide3.put("nodeValue", repositoryRoleValue);
			
		return childArchguide3;
	}

	private HashMap<String, Object> buildRepositoryName(String repositoryNameLang, String repositoryNameValue) {

		HashMap<String, Object> childArchguide3 = new HashMap<String, Object>();
        childArchguide3.put("nodeName", "repositoryName");
        HashMap<String, String> childArchguide3Attributes = new HashMap<String, String>();
    	childArchguide3Attributes.put("xml:lang", repositoryNameLang);
    	childArchguide3.put("attributes",childArchguide3Attributes);
    	childArchguide3.put("nodeValue", repositoryNameValue);
    	
		return childArchguide3;
	}

	private HashMap<String, Object> buildContact(Integer indexRepo) {
		
		 HashMap<String, Object>  childArchguidechild = new HashMap<String, Object>(); 
         childArchguidechild.put("nodeName", "contact");
         childArchguidechild.put("attributes",null);
         childArchguidechild.put("nodeValue", null); 
         		
         ArrayList<HashMap<String, Object>> childArchguideChildren = new ArrayList<HashMap<String, Object>>();
        if(indexRepo>0){
        	for(int i=0;this.eag2012.getTelephoneValue()!=null && indexRepo<this.eag2012.getTelephoneValue().size() && this.eag2012.getTelephoneValue().get(indexRepo)!=null && this.eag2012.getTelephoneValue().get(indexRepo).get(TAB_CONTACT)!=null && this.eag2012.getTelephoneValue().get(indexRepo).get(TAB_CONTACT).size()>i;i++){ //no mandatory
/* TODO        		childArchguideChildren.add(buildTelephone(this.eag2012.getTelephoneValue().get(indexRepo).get(TAB_CONTACT).get(i))); */
            }
            for(int i=0;this.eag2012.getEmailHref()!=null && indexRepo<this.eag2012.getEmailHref().size() && this.eag2012.getEmailHref().get(indexRepo)!=null && this.eag2012.getEmailHref().get(indexRepo).get(TAB_CONTACT)!=null && this.eag2012.getEmailHref().get(indexRepo).get(TAB_CONTACT).size()>0 && this.eag2012.getEmailHref().get(indexRepo).get(TAB_CONTACT).get(ROOT)!=null && this.eag2012.getEmailHref().get(indexRepo).get(TAB_CONTACT).get(ROOT).size()>i;i++){//no mandatory
            	childArchguideChildren.add(buildEmail(this.eag2012.getEmailHref().get(indexRepo).get(TAB_CONTACT).get(ROOT).get(i),this.eag2012.getEmailValue().get(indexRepo).get(TAB_CONTACT).get(ROOT).get(i)));
            }
        }else{
        	if(this.eag2012.getTelephoneValue()!=null && this.eag2012.getTelephoneValue().size()>0 && this.eag2012.getTelephoneValue().get(0).get(TAB_YOUR_INSTITUTION)!=null && this.eag2012.getTelephoneValue().get(0).get(TAB_YOUR_INSTITUTION).size()>0){
/* TODO        		childArchguideChildren.add(buildTelephone(this.eag2012.getTelephoneValue().get(indexRepo).get(TAB_YOUR_INSTITUTION).get(0))); */
        	}if(this.eag2012.getEmailHref()!=null && this.eag2012.getEmailHref().size()>0 && this.eag2012.getEmailHref().get(0)!=null && this.eag2012.getEmailHref().get(0).get(TAB_YOUR_INSTITUTION)!=null && this.eag2012.getEmailHref().get(0).get(TAB_YOUR_INSTITUTION).size()>0 && this.eag2012.getEmailHref().get(0).get(TAB_YOUR_INSTITUTION).get(ROOT)!=null && this.eag2012.getEmailHref().get(0).get(TAB_YOUR_INSTITUTION).get(ROOT).size()>0){
        		childArchguideChildren.add(buildEmail(this.eag2012.getEmailHref().get(indexRepo).get(TAB_YOUR_INSTITUTION).get(ROOT).get(0),this.eag2012.getEmailValue().get(indexRepo).get(TAB_YOUR_INSTITUTION).get(ROOT).get(0)));
        	}
        }
         childArchguidechild.put("children", childArchguideChildren);
		
		
		return childArchguidechild;
	}

	private HashMap<String, Object> buildNum(String numunit, String numValue) {
		
		HashMap<String, Object> childArchguidechild = new HashMap<String, Object>();
		
		childArchguidechild.put("nodeName", "num");
		HashMap<String, String> childArchguidechildAttributes = new HashMap<String, String>();
        childArchguidechildAttributes.put("unit", numunit);
        childArchguidechild.put("attributes", childArchguidechildAttributes);
        childArchguidechild.put("nodeValue", numValue);	
		
		return childArchguidechild;
	}

	private HashMap<String, Object> buildRule(Integer indexRepo,Integer indexList) {
		
		HashMap<String, Object> childArchguide4 = new HashMap<String, Object>();
		
  	    childArchguide4.put("nodeName", "rule");
        HashMap<String, String> childArchguide4Attributes = new HashMap<String, String>();
/* TODO        childArchguide4Attributes.put("xml:lang",(this.eag2012.getRuleLang()!=null && this.eag2012.getRuleLang().size()>indexRepo && this.eag2012.getRuleLang().get(indexRepo)!=null && this.eag2012.getRuleLang().get(indexRepo).size()>indexList)? this.eag2012.getRuleLang().get(indexRepo).get(indexList):null); */
        childArchguide4.put("attributes", childArchguide4Attributes);
        childArchguide4.put("nodeValue",(this.eag2012.getRuleValue()!=null && this.eag2012.getRuleValue().size()>indexRepo && this.eag2012.getRuleValue().get(indexRepo)!=null && this.eag2012.getRuleValue().get(indexRepo).size()>indexList)?this.eag2012.getRuleValue().get(indexRepo).get(indexList):null);
        
		return childArchguide4;
	}

	private HashMap<String, Object> buildWebpage(String webpageHref, String webpageValue) {
		
		HashMap<String, Object> childArchguide3 = new HashMap<String, Object>();
		childArchguide3 = new HashMap<String, Object>();
        childArchguide3.put("nodeName","webpage");
        HashMap<String, String> childArchguide3Attributes = new HashMap<String, String>();
       if(webpageHref!=null){
        childArchguide3Attributes.put("href", webpageHref);
        childArchguide3.put("attributes", childArchguide3Attributes);
       }
        childArchguide3.put("nodeValue", webpageValue);
		

		return childArchguide3;
	}

	private HashMap<String, Object> buildEmail(String emailHref,String emailValue) {
		
		HashMap<String, Object> childArchguide3 = new HashMap<String, Object>();
		childArchguide3 = new HashMap<String, Object>();
        childArchguide3.put("nodeName","email");
        HashMap<String, String> childArchguide3Attributes = new HashMap<String, String>();
        childArchguide3Attributes.put("href", emailHref);
        childArchguide3.put("attributes", childArchguide3Attributes);
        childArchguide3.put("nodeValue", emailValue);
		
		
		return childArchguide3;
	}

	private HashMap<String, Object> buildFax(String faxValue) {
		
		HashMap<String, Object> childArchguide3 = new HashMap<String, Object>();
		childArchguide3 = new HashMap<String, Object>();
        childArchguide3.put("nodeName","fax");
        childArchguide3.put("attributes", null);
        childArchguide3.put("nodeValue", faxValue);
		
		return childArchguide3;
	}

	private HashMap<String, Object> buildTelephone(String telephoneValue) {
		
		HashMap<String, Object> childArchguide3 = new HashMap<String, Object>();
        childArchguide3.put("nodeName","telephone");
        childArchguide3.put("attributes", null);
        childArchguide3.put("nodeValue", telephoneValue);
		
		return childArchguide3;
	}

}