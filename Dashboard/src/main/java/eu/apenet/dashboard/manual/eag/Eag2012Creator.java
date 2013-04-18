package eu.apenet.dashboard.manual.eag;

import java.io.File;
import java.io.IOException;
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
import org.xml.sax.SAXException;

import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.dashboard.archivallandscape.ArchivalLandscape;
import eu.apenet.dashboard.utils.ChangeControl;
import eu.apenet.persistence.dao.ArchivalInstitutionDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.ArchivalInstitution;
import eu.archivesportaleurope.commons.config.ApeConfig;
/**
 * Creates and manages all building proccess related to EAG2012 into Dashboard 
 */
public class Eag2012Creator {
	
    private static final String EAG_XMLNS = "http://www.archivesportaleurope.eu/profiles/APEnet_EAG/";
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
	        Document doc = null;
			try {
				doc = generateXMLDocumentFromEagNodes(eagNode);
			} catch (ParserConfigurationException e) {
				log.error("EXCEPTION trying to generate an XML Document from EAGNode",e);
			} 
			//generate XML-DOM Document to be stored from object-eag-structure
	        if(doc!=null){
	        	//Write the XML
	            storeToXML(doc);
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
	}

	private void storeToXML(Node doc) {
		//Create the new file 
        Source source = new DOMSource(doc);
        Transformer transformer = null;
        try {
        	File eagFile = new File(storagePath);
        	Result result = new StreamResult(eagFile);
            transformer = TransformerFactory.newInstance().newTransformer();
            //tabulation by levels into XML
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            
            transformer.transform(source, result);
            
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
        
        children.add(buildControl(this.eag2012.getControlId(),this.eag2012.getControlLanguage()));       
        children.add(buildArchguide());        
        children.add(buildRelations(this.eag2012.getRelationsId(),this.eag2012.getRelationsLang()));
        //end relations
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
		   
		   childrenRelations.add(buildResourceRelation(this.eag2012.getResourceRelationResourceRelationType(),this.eag2012.getResourceRelationLastDateTimeVerified(),this.eag2012.getResourceRelationId(),this.eag2012.getResourceRelationLang(),this.eag2012.getResourceRelationHref()));
		   childrenRelations.add(buildeagRelation(this.eag2012.getEagRelationEagRelationType(),this.eag2012.getEagRelationHref()));
		  
		
		childRelations.put("children", childrenRelations);
		
		
		return childRelations;
	}

	private HashMap<String, ?> buildResourceRelation(String resourceRelationType,String resourceRelationLastDateTimeVerified,String resourceRelationId,String resourceRelationLang,String resourceRelationHref) {
		
		HashMap<String, Object> childresourceRelation = new HashMap<String, Object>();
		childresourceRelation.put("nodeName", "resourceRelation");
		HashMap<String, String> childresourceRelationAttributes = new HashMap<String, String>();
		childresourceRelationAttributes.put("resourceRelationType", /*"creatorOf"*/resourceRelationType);
		childresourceRelationAttributes.put("lastDateTimeVerified", resourceRelationLastDateTimeVerified);
		childresourceRelationAttributes.put("xml:id", resourceRelationId);
		childresourceRelationAttributes.put("xml:lang",resourceRelationLang);
		childresourceRelationAttributes.put("href", resourceRelationHref);
		childresourceRelation.put("attributes",childresourceRelationAttributes);
		childresourceRelation.put("nodeValue",null);
		    //Children of resourceRelation
		    
		    ArrayList<HashMap<String, ?>> childrenresourceRelation = new ArrayList<HashMap<String,?>>();
		
		     childrenresourceRelation.add(buildRelationEntry(this.eag2012.getRelationEntryScriptCode(),this.eag2012.getRelationEntryId(),this.eag2012.getRelationEntryLang(),this.eag2012.getRelationEntryValue()));
		     childrenresourceRelation.add(buildDescriptiveNote(this.eag2012.getDescriptiveNoteLang(),this.eag2012.getDescriptiveNoteId()));
		     childrenresourceRelation.add(buildDateSet(this.eag2012.getDateSetId(), this.eag2012.getDateSetLang()));
		     childrenresourceRelation.add(buildDate(this.eag2012.getDateNotAfter(),this.eag2012.getDateNotBefore(),this.eag2012.getDateStandardDate(),this.eag2012.getDateId(),this.eag2012.getDateLang(),this.eag2012.getDateValue()));
		     childrenresourceRelation.add(buildDateRange(this.eag2012.getDateRangeId(),this.eag2012.getDateRangeLang()));
		     childrenresourceRelation.add(buildObjectBinWrap(this.eag2012.getObjectBinWrapId()));
		     childrenresourceRelation.add(buildObjectXMLWrap(this.eag2012.getObjectXMLWrapId()));
		     childrenresourceRelation.add(buildPlaceEntry(this.eag2012.getPlaceEntryAccuracy(),this.eag2012.getPlaceEntryAltitude(),this.eag2012.getPlaceEntryId(),this.eag2012.getPlaceEntryLang(),this.eag2012.getPlaceEntryCountryCode(),this.eag2012.getPlaceEntryLatitude(),this.eag2012.getPlaceEntryLongitude(),this.eag2012.getPlaceEntryScriptCode(),this.eag2012.getPlaceEntryValue()));
		
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
		childPlaceEntryAttributes.put("transliteration", "http://www.archivesportaleurope.eu/scripts/EAG/");
		childPlaceEntryAttributes.put("vocabularySource", "http://www.archivesportaleurope.eu/vocabularies/EAG/");
		childPlaceEntry.put("attributes", childPlaceEntryAttributes);
		childPlaceEntry.put("nodeValue", value);
		
		
		return childPlaceEntry;
	}

	private HashMap<String, ?> buildeagRelation(String eagRelationType,String eagRelationHref) {
		
		HashMap<String, Object> childeagRelation = new HashMap<String, Object>();
		childeagRelation.put("nodeName", "eagRelation");
		HashMap<String, String> childeagRelationAttributes = new HashMap<String,String>();
		childeagRelationAttributes.put("eagRelationType", eagRelationType);
		childeagRelationAttributes.put("href", eagRelationHref);
		childeagRelation.put("attributes", childeagRelationAttributes);
		childeagRelation.put("nodeValue", null);
		   
		    //Children of eagRelation
		    ArrayList<HashMap<String, ?>> childrenRelations = new ArrayList<HashMap<String,?>>();
		    childrenRelations.add(buildRelationEntry(this.eag2012.getRelationEntryScriptCode(),this.eag2012.getRelationEntryId(),this.eag2012.getRelationEntryLang(),this.eag2012.getRelationEntryValue()));
		    childrenRelations.add(buildDescriptiveNote(this.eag2012.getDescriptiveNoteLang(),this.eag2012.getDescriptiveNoteId()));
		    
		childeagRelation.put("children", childrenRelations); 
			
		
		return childeagRelation;
	}

	private HashMap<String, ?> buildRelationEntry(String relationEntryScriptCode,String relationEntryId, String relationEntryLang,String relationEntryValue) {
		
		HashMap<String, Object> childRelation = new HashMap<String, Object>();
		childRelation.put("nodeName", "relationEntry");
		HashMap<String, String> childRelationAttributes = new HashMap<String,String>();
		childRelationAttributes.put("localType", "localType");
		childRelationAttributes.put("scriptCode", /*"scriptCode"*/relationEntryScriptCode);
		childRelationAttributes.put("transliteration", "http://www.archivesportaleurope.eu/scripts/EAG/");
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
        
        childrenControl.add(buildRecordId(this.eag2012.getRecordIdId(),this.eag2012.getRecordIdValue()));
        childrenControl.add(buildOtherRecordId(this.eag2012.getOtherRecordIdId(), this.eag2012.getOtherRecordIdValue()));
        childrenControl.add(buildMaintenanceAgency(this.eag2012.getMaintenanceAgencyId()));
        childrenControl.add(buildMaintenanceStatus(this.eag2012.getMaintenanceStatusId(),this.eag2012.getMaintenanceStatusValue()));
        childrenControl.add(buildMaintenanceHistory(this.eag2012.getMaintenanceHistoryId(),this.eag2012.getMaintenanceHistoryLang()));
        childrenControl.add(buildLanguageDeclarations());
        childrenControl.add(buildConventionDeclaration(this.eag2012.getConventionDeclarationId(),this.eag2012.getConvetionDeclarationLang()));
        childrenControl.add(buildLocalControl(this.eag2012.getLocalControlId(),this.eag2012.getLocalControlLang()));
        childrenControl.add(buildLocalTypeDeclaration(this.eag2012.getLocalTypeDeclarationId(),this.eag2012.getLocalTypeDeclarationLang()));
        childrenControl.add(buildPublicationStatus(this.eag2012.getPublicationStatusId(),this.eag2012.getPublicationStatusValue()));
        childrenControl.add(buildSources(this.eag2012.getSourcesId(),this.eag2012.getSourcesLang()));
        
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
        childControlChildren.add(buildCitation(this.eag2012.getCitationId(),this.eag2012.getCitationLang(),this.eag2012.getCitationLastDateTimeVerified(),this.eag2012.getCitationHref(),this.eag2012.getCitationValue()));
        childControlChildren.add(buildAbbreviation(this.eag2012.getAbbreviationId(),this.eag2012.getAbbreviationLang(),this.eag2012.getAbbreviationValue()));
        childControlChildren.add(buildDescriptiveNote(this.eag2012.getDescriptiveNoteLang(),this.eag2012.getDescriptiveNoteId()));
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
        childControlChildren.add(buildTerm(this.eag2012.getTermLastDateTimeVerified(),this.eag2012.getTermScriptCode(),this.eag2012.getTermId(),this.eag2012.getTermLang(),this.eag2012.getTermValue()));
        childControlChildren.add(buildDate(this.eag2012.getDateNotAfter(),this.eag2012.getDateNotBefore(),this.eag2012.getDateStandardDate(),this.eag2012.getDateId(),this.eag2012.getDateLang(),this.eag2012.getDateValue()));
        childControlChildren.add(buildDateRange(this.eag2012.getDateRangeId(),this.eag2012.getDateRangeLang()));
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
        childControlChildren.add(buildAbbreviation(this.eag2012.getAbbreviationId(),this.eag2012.getAbbreviationLang(),this.eag2012.getAbbreviationValue()));
        childControlChildren.add(buildCitation(this.eag2012.getCitationId(),this.eag2012.getCitationLang(),this.eag2012.getCitationLastDateTimeVerified(),this.eag2012.getCitationHref(),this.eag2012.getCitationValue()));
        childControlChildren.add(buildDescriptiveNote(this.eag2012.getDescriptiveNoteLang(),this.eag2012.getDescriptiveNoteId()));
        
        childControl.put("children", childControlChildren);
        return childControl;
	}

	private HashMap<String, ?> buildLanguageDeclarations() {
		HashMap<String, Object> childControl = new HashMap<String, Object>();
        childControl.put("nodeName","languageDeclarations");
        childControl.put("attributes", null);
        childControl.put("nodeValue",null);
        
        ArrayList<HashMap<String, Object>> childControlChildren = new ArrayList<HashMap<String,Object>>();
        
	      
        childControlChildren.add(buildLanguageDeclaration(this.eag2012.getLanguageDeclarationId(),this.eag2012.getLanguageDeclarationLang()));
        
        childControl.put("children",childControlChildren);
        return childControl;
	}

	private HashMap<String, Object> buildLanguageDeclaration(String languageDeclarationId, String languageDeclarationLang) {

		  HashMap<String, Object> childControl1 = new HashMap<String,Object>();
	        childControl1.put("nodeName", "languageDeclaration");
	        HashMap<String, String> childControl1Attributes = new HashMap<String, String>();
	        childControl1Attributes.put("xml:id", languageDeclarationId);
	        childControl1Attributes.put("xml:lang",languageDeclarationLang);
	        childControl1.put("attributes", childControl1Attributes);
	        childControl1.put("nodeValue", null);
	        
		        ArrayList<HashMap<String, Object>> childControl1Children = new ArrayList<HashMap<String,Object>>();
		        
		        childControl1Children.add(buildLanguage(this.eag2012.getLanguageLanguageCode(),this.eag2012.getLanguageId(),this.eag2012.getLanguageLang(),this.eag2012.getLanguageValue())); 
		        childControl1Children.add(buildScript(this.eag2012.getScriptScriptCode(),this.eag2012.getScriptId(),this.eag2012.getScriptLang(),this.eag2012.getScriptValue()));
		        childControl1Children.add(buildDescriptiveNote(this.eag2012.getDescriptiveNoteLang(),this.eag2012.getDescriptiveNoteId()));
		        
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
       
        childControlChildren.add(buildMaintenanceEvent(this.eag2012.getMaintenanceEventId(),this.eag2012.getMaintenanceEventLang()));
        
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
	        
	        childControl1Children.add(buildAgent(this.eag2012.getAgentId(),this.eag2012.getAgentLang(),this.eag2012.getAgentValue()));
	       
	        childControl1Children.add(buildAgentType(this.eag2012.getAgentTypeId(),this.eag2012.getAgentTypeValue()));
	       
	        childControl1Children.add(buildEventDateTime(this.eag2012.getEventDateTimeStandardDateTime(),this.eag2012.getEventDateTimeLang(),this.eag2012.getEventDateTimeId(),this.eag2012.getEventDateTimeValue()));
	       
	        childControl1Children.add(buildEventType(this.eag2012.getEventTypeId(),this.eag2012.getEventTypeValue()));
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
	        childControl2.put("nodeValue", agentTypeValue);
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
        childControl.put("nodeValue",/*"revised"*/maintenanceStatusValue);
        return childControl;
	}

	private HashMap<String, ?> buildMaintenanceAgency(String maintenanceAgencyId) {
		HashMap<String, Object> childControl = new HashMap<String, Object>();
        childControl.put("nodeName","maintenanceAgency");
        
        HashMap<String, String> childControlAttributes = new HashMap<String, String>();
        childControlAttributes.put("xml:id",maintenanceAgencyId);
        childControl.put("attributes", childControlAttributes);
        childControl.put("nodeValue",null);
        ArrayList<HashMap<String, Object>> childControlChildren = new ArrayList<HashMap<String, Object>>();
         childControlChildren.add(buildAgencyCode(this.eag2012.getAgencyCodeId(),this.eag2012.getAgencyCodeValue()));
         childControlChildren.add(buildAgencyName(this.eag2012.getAgencyNameLang(),this.eag2012.getAgencyNameId(),this.eag2012.getAgencyNameValue()));
         childControlChildren.add(buildOtherAgencyCode(this.eag2012.getOtherAgencyCodeId(),this.eag2012.getOtherAgencyCodeValue()));
        
        childControlChildren.add(buildDescriptiveNote(this.eag2012.getDescriptiveNoteLang(),this.eag2012.getDescriptiveNoteId()));
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
        
        childControlChildren.add(buildSource(this.eag2012.getSourceLastDateTimeVerified(),this.eag2012.getSourceId(),this.eag2012.getSourceHref()));
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
        childControl2Children.add(buildSourceEntry(this.eag2012.getSourceEntryScriptCode(),this.eag2012.getSourceEntryId(),this.eag2012.getSourceEntryLang(),this.eag2012.getSourceEntryValue()));
        childControl2Children.add(buildObjectBinWrap(this.eag2012.getObjectBinWrapId())); 
        childControl2Children.add(buildObjectXMLWrap(this.eag2012.getObjectXMLWrapId()));
        childControl2Children.add(buildDescriptiveNote(this.eag2012.getDescriptiveNoteLang(),this.eag2012.getDescriptiveNoteId()));
		childControl2.put("children", childControl2Children);
		
		return childControl2;
	}

	private HashMap<String, Object> buildSourceEntry(String sourceEntryScriptCode, String sourceEntryId, String sourceEntryLang, String sourceEntryValue ) {

		HashMap<String, Object> childControl3 = new HashMap<String, Object>();
        childControl3.put("nodeName","sourceEntry");
        HashMap<String, String> childControl3Attributes = new HashMap<String, String>();
        childControl3Attributes.put("scriptCode", sourceEntryScriptCode);
        childControl3Attributes.put("transliteration","http://www.archivesportaleurope.eu/scripts/EAG/");
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

	private HashMap<String, Object> buildDateRange(String dateRangeId,String dateRangeLang) {
		HashMap<String, Object> childControl1 = new HashMap<String,Object>();
        childControl1.put("nodeName", "dateRange");
        HashMap<String, String> childControl1Attributes = new HashMap<String, String>();
        childControl1Attributes.put("localType", "localDateType");
        childControl1Attributes.put("xml:id", dateRangeId);
        childControl1Attributes.put("xml:lang", dateRangeLang);
        childControl1.put("attributes",childControl1Attributes);
        childControl1.put("nodeValue", null);
        List<HashMap<String, Object>> childControl1Children = new ArrayList<HashMap<String, Object>>();
       
        childControl1Children.add(buildFromDate(this.eag2012.getFromDateNotAfter(),this.eag2012.getFromDateNotBefore(),this.eag2012.getFromDateStandardDate(),this.eag2012.getFromDateId(),this.eag2012.getFromDateLang(),this.eag2012.getFromDateValue()));
        childControl1Children.add(buildToDate(this.eag2012.getToDateNotAfter(),this.eag2012.getToDateNotBefore(),this.eag2012.getToDateStandardDate(),this.eag2012.getToDateId(),this.eag2012.getToDateLang(),this.eag2012.getToDateValue()));
        
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
        childControlChildAttributes.put("transliteration","http://www.archivesportaleurope.eu/scripts/EAG/");
        childControlChildAttributes.put("vocabularySource", "http://www.archivesportaleurope.eu/vocabularies/EAG/");
        childControlChildAttributes.put("xml:id", termId);
        childControlChildAttributes.put("xml:lang", termLang);
        childControlchild.put("attributes",childControlChildAttributes);
        childControlchild.put("nodeValue", /*"extended"*/termValue);
        childControlchild.put("children", null);
        return childControlchild;
	}

	private HashMap<String, Object> buildDate(String dateNotAfter,String dateNotBefore,String dateStandardDate,String dateId,String dateLang,String dateValue) {
		HashMap<String, Object> childControlChild = new HashMap<String,Object>();
        childControlChild.put("nodeName", "date");
        HashMap<String, String> childControlChildAttributes = new HashMap<String, String>();
        childControlChildAttributes.put("localType", "localDateType");
        childControlChildAttributes.put("notAfter", dateNotAfter);
        childControlChildAttributes.put("notBefore", dateNotBefore);
        childControlChildAttributes.put("standardDate", dateStandardDate);
        childControlChildAttributes.put("xml:id", dateId);
        childControlChildAttributes.put("xml:lang", dateLang);
        childControlChild.put("attributes",childControlChildAttributes);
        childControlChild.put("nodeValue", dateValue);
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

	private HashMap<String, Object> buildDescriptiveNote(String descriptiveNoteLang,String descriptiveNoteId) {
	
		HashMap<String, Object> childControl1 = new HashMap<String, Object>();
        childControl1.put("nodeName", "descriptiveNote");
        HashMap<String, String> childControl1Attributes = new HashMap<String, String>();
        childControl1Attributes.put("xml:lang", descriptiveNoteLang);
        childControl1Attributes.put("xml:id",descriptiveNoteId);
        childControl1.put("attributes",childControl1Attributes);
        childControl1.put("nodeValue", null);
        List<HashMap<String, Object>> childControl1Children = new ArrayList<HashMap<String, Object>>();
       
        childControl1Children.add(buildP(this.eag2012.getDescriptiveNotePId(),this.eag2012.getDescriptiveNotePLang(),this.eag2012.getDescriptiveNotePValue()));
        childControl1.put("children", childControl1Children);
        return childControl1;
	}
	
	private HashMap<String, Object> buildP(String descriptiveNotePId, String descriptiveNotePLang, String descriptiveNotePValue) {

		HashMap<String, Object> childControl2 = new HashMap<String, Object>();
        childControl2.put("nodeName", "p");
        childControl2.put("children", null);
        HashMap<String,String> childControlChildChildAttributes = new HashMap<String,String>();
        childControlChildChildAttributes.put("xml:id",descriptiveNotePId);
        childControlChildChildAttributes.put("xml:lang", descriptiveNotePLang);
        childControl2.put("attributes", childControlChildChildAttributes);
        childControl2.put("nodeValue", descriptiveNotePValue);
			
		return childControl2;
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
	        
	        childArchguideChildren.add(buildRepositorid(this.eag2012.getRepositoridCountrycode(),this.eag2012.getRepositoridRepositorycode()));
	        childArchguideChildren.add(buildOtherRepositorId(this.eag2012.getOtherRepositorIdValue()));
	        
	        childArchguideChildren.add(buildAutform(this.eag2012.getAutformLang(),this.eag2012.getAutformValue()));
	        childArchguideChildren.add(buildParform(this.eag2012.getParformLang(),this.eag2012.getParformValue()));	        
	        childArchguideChildren.add(buildNonpreform(this.eag2012.getNonpreformLang(),this.eag2012.getNonpreformValue()));
	        childArchguideChildren.add(buildRepositoryType(this.eag2012.getRepositoryTypeValue()));
	        
	        childArchguide.put("children", childArchguideChildren);
	        
	        return childArchguide;
		
		
	}

	private HashMap<String, Object> buildOtherRepositorId(String otherRepositorIdValue) {

		HashMap<String, Object> childArchguide1 = new HashMap<String, Object>();
        childArchguide1.put("nodeName", "otherRepositorId");
        childArchguide1.put("nodeValue", otherRepositorIdValue);
		
		return  childArchguide1;
	}

	private HashMap<String, Object> buildRepositorid(String repositoridCountrycode, String repositoridRepositorycode) {

		 HashMap<String, Object> childArchguide1 = new HashMap<String, Object>();
	       
	        childArchguide1.put("nodeName", "repositorid");
	        HashMap<String, String> childArchguide1Attributes = new HashMap<String, String>();
	        childArchguide1Attributes.put("countrycode",/* "DE"*/repositoridCountrycode);
	        childArchguide1Attributes.put("repositorycode", /*"DE-00000000000001"*/repositoridRepositorycode);  
	        childArchguide1.put("attributes", childArchguide1Attributes);
	        childArchguide1.put("nodeValue", null);
	        childArchguide1.put("children", null);
		
		return childArchguide1;
	}

	private HashMap<String, Object> buildAutform(String autformLang, String autformValue) {
        
		HashMap<String, Object> childArchguide1 = new HashMap<String, Object>();
	        childArchguide1.put("nodeName", "autform");
	        HashMap<String, String> childArchguide1Attributes = new HashMap<String, String>();
	        childArchguide1Attributes.put("xml:lang", autformLang);
	        childArchguide1.put("attributes", childArchguide1Attributes);
	        childArchguide1.put("nodeValue",autformValue);
	        childArchguide1.put("children", null);
		
		return childArchguide1;
	}

	private HashMap<String, Object> buildParform(String parformLang,String parformValue) {

		HashMap<String, Object> childArchguide1 = new HashMap<String, Object>();
	        childArchguide1.put("nodeName", "parform");
	        HashMap<String, String>  childArchguide1Attributes = new HashMap<String, String>();
	        childArchguide1Attributes.put("xml:lang",parformLang);
	        childArchguide1.put("attributes", childArchguide1Attributes);
	        childArchguide1.put("nodeValue", parformValue);
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
        childArchguide1Attributes.put("xml:lang",nonpreformLang);
        childArchguide1.put("attributes", childArchguide1Attributes);
        childArchguide1.put("nodeValue", nonpreformValue);
        
	        List<HashMap<String, Object>> childArchguide1Children = new ArrayList<HashMap<String,Object>>();
	          childArchguide1Children.add(buildUsesDates(this.eag2012.getUseDatesId(),this.eag2012.getUseDatesLang()));
	        
        childArchguide1.put("children", childArchguide1Children);
        
		return childArchguide1;
	}

	private HashMap<String, Object> buildUsesDates(String useDatesId, String useDatesLang) {

	    HashMap<String, Object> childArchguide2 = new HashMap<String, Object>();
	        childArchguide2.put("nodeName", "useDates");
	        HashMap<String, String> childArchguide2Attributes = new HashMap<String, String>();
	        childArchguide2Attributes.put("xml:id",useDatesId);
	        childArchguide2Attributes.put("xml:lang", /*"mul"*/useDatesLang);
	        childArchguide2.put("attributes", childArchguide2Attributes);
	        childArchguide2.put("nodeValue", null);
	        
	        List<HashMap<String, Object>> childArchguide2Children = new ArrayList<HashMap<String,Object>>();
	        	childArchguide2Children.add(buildDateSet(this.eag2012.getDateSetId(), this.eag2012.getDateSetLang()));
	                
	        childArchguide2.put("children", childArchguide2Children);
		
		return childArchguide2;
	}

	private HashMap<String, Object> buildDateSet(String dateSetId, String dateSetLang) {
		
		HashMap<String, Object> childArchguide3 = new HashMap<String, Object>();
    	childArchguide3.put("nodeName","dateSet");
    	
    	HashMap<String, String> childArchguide3Attributes = new HashMap<String, String>();

    	childArchguide3Attributes.put("localType", "localDateType");
    	childArchguide3Attributes.put("xml:id", dateSetId);
    	childArchguide3Attributes.put("xml:lang", /*"mul"*/dateSetLang);
    	
    	childArchguide3.put("attributes",childArchguide3Attributes);
    	childArchguide3.put("nodeValue",null);
    	
        	//dateSet children	        
	        List<HashMap<String, Object>> childArchguide3Children = new ArrayList<HashMap<String,Object>>();
	        childArchguide3Children.add(buildDate(this.eag2012.getDateNotAfter(),this.eag2012.getDateNotBefore(),this.eag2012.getDateStandardDate(),this.eag2012.getDateId(),this.eag2012.getDateLang(),this.eag2012.getDateValue()));
	        childArchguide3Children.add(buildDateRange(this.eag2012.getDateRangeId(),this.eag2012.getDateRangeLang()));
	        
        childArchguide3.put("children",childArchguide3Children);    
	        
		return childArchguide3;
	}

	private HashMap<String, ?> buildDesc() { 
		
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
        
        
        childArchguide1Children.add(buildRepository());
        childArchguide1.put("children",childArchguide1Children);
        
        
		// TODO Auto-generated method stub
		return childArchguide1;
	}

	private HashMap<String, Object> buildRepository() {

       HashMap<String, Object> childArchguide2 = new HashMap<String, Object>();
        
        childArchguide2.put("nodeName", "repository");
        childArchguide2.put("attributes",null);
		childArchguide2.put("nodeValue",null);
		
        
     		ArrayList<HashMap<String, Object>> childArchguide2Children = new ArrayList<HashMap<String, Object>>();
            
        	childArchguide2Children.add(buildRepositoryName(this.eag2012.getRepositoryNameLang(),this.eag2012.getRepositoryNameValue()));
		    childArchguide2Children.add(buildRepositoryRole(this.eag2012.getRepositoryRoleValue()));
        	childArchguide2Children.add(buildGeogarea(this.eag2012.getGeogareaLang(),this.eag2012.getGeogareaValue()));
            childArchguide2Children.add(buildLocation(this.eag2012.getLocationLocalType(),this.eag2012.getLocationLongitude(),this.eag2012.getLocationLatitude()));
            childArchguide2Children.add(buildTelephone(this.eag2012.getTelephoneValue()));     
            childArchguide2Children.add(buildFax(this.eag2012.getFaxValue()));
            childArchguide2Children.add(buildEmail(this.eag2012.getEmailHref(),this.eag2012.getEmailValue()));   
            childArchguide2Children.add(buildWebpage(this.eag2012.getWebpageHref(),this.eag2012.getWebpageValue()));   
            childArchguide2Children.add(buildDirections(this.eag2012.getDirectionsLang(),this.eag2012.getDirectionsValue()));    
            childArchguide2Children.add(buildRepositorHist());                   
            childArchguide2Children.add(buildRepositorfound());     
            childArchguide2Children.add(buildRepositorsup());        
            childArchguide2Children.add(buildBuildinginfo());   
            childArchguide2Children.add(buildAdminhierarchy());   
            childArchguide2Children.add(buildHoldings());   
            childArchguide2Children.add(buildTimetable());
            childArchguide2Children.add(buildAccess(this.eag2012.getAccessQuestion()));
            childArchguide2Children.add(buildAccessibility(this.eag2012.getAccessibilityQuestion(),this.eag2012.getAccessibilityLang(),this.eag2012.getAccessibilityValue()));              
            childArchguide2Children.add(buildServices());  // add services       
            childArchguide2Children.add(buildDescriptiveNote(this.eag2012.getDescriptiveNoteLang(),this.eag2012.getDescriptiveNoteId()));                     
        childArchguide2.put("children", childArchguide2Children);
		
		return childArchguide2;
	}

	private HashMap<String, Object> buildServices() {

		   //Children of repository, services
        HashMap<String, Object> childArchguide3 = new HashMap<String, Object>();
        childArchguide3.put("nodeName", "services");
        childArchguide3.put("attributes", null);
        childArchguide3.put("nodeValue", null); 
            
               //Children of services
               ArrayList<HashMap<String, Object>> childArchguide3Children = new ArrayList<HashMap<String, Object>>();   
                 childArchguide3Children.add(buildSearchroom());   //add searchroom
                 childArchguide3Children.add(buildLibrary(this.eag2012.getLibraryQuestion()));       
                 childArchguide3Children.add(buildInternetAccess(this.eag2012.getInternetAccessQuestion()));    
                 childArchguide3Children.add(buildTechservices());      
                 childArchguide3Children.add(buildRecreationalServices()); 
               childArchguide3.put("children", childArchguide3Children);
		
		return childArchguide3;
	}

	private HashMap<String, Object> buildRecreationalServices() {

	      //Children of services, recreationalServices
        HashMap<String, Object> childArchguide4 = new HashMap<String, Object>();
        childArchguide4.put("nodeName", "recreationalServices");
        childArchguide4.put("attributes", null);
        childArchguide4.put("nodeValue", null); 
              
           //Children of recreationalServices
               ArrayList<HashMap<String, Object>> childArchguide4Children = new ArrayList<HashMap<String, Object>>(); 
                
	              childArchguide4Children.add(buildRefreshment());
                  childArchguide4Children.add(buildExhibition());
                  childArchguide4Children.add(buildTourSessions());
                  childArchguide4Children.add(buildOtherServices());
                 
                 childArchguide4.put("children",childArchguide4Children);
		
		return childArchguide4;
	}

	private HashMap<String, Object> buildOtherServices() {

		 HashMap<String, Object> childArchguide5 = new HashMap<String, Object>();
         childArchguide5.put("nodeName", "otherServices");  //otherServices
         childArchguide5.put("attributes", null);
         childArchguide5.put("nodeValue", null); 
         ArrayList<HashMap<String, Object>> childArchguide5Children = new ArrayList<HashMap<String, Object>>(); 
            childArchguide5Children.add(buildDescriptiveNote(this.eag2012.getDescriptiveNoteLang(),this.eag2012.getDescriptiveNoteId()));
            childArchguide5Children.add(buildWebpage(this.eag2012.getWebpageHref(),this.eag2012.getWebpageValue()));
   
        childArchguide5.put("children", childArchguide5Children);
		
		return childArchguide5;
	}

	private HashMap<String, Object> buildTourSessions() {

		HashMap<String, Object> childArchguide5 = new HashMap<String, Object>();
        childArchguide5.put("nodeName", "tourSessions");  //tourSessions
        childArchguide5.put("attributes", null);
        childArchguide5.put("nodeValue", null); 
        ArrayList<HashMap<String, Object>> childArchguide5Children = new 	ArrayList<HashMap<String, Object>>(); 
          childArchguide5Children.add(buildDescriptiveNote(this.eag2012.getDescriptiveNoteLang(),this.eag2012.getDescriptiveNoteId()));
          childArchguide5Children.add(buildWebpage(this.eag2012.getWebpageHref(),this.eag2012.getWebpageValue()));
  
       childArchguide5.put("children", childArchguide5Children);
		
		return childArchguide5;
	}

	private HashMap<String, Object> buildExhibition() {

		 HashMap<String, Object> childArchguide5 = new HashMap<String, Object>();
         childArchguide5.put("nodeName", "exhibition");  //exhibition
         childArchguide5.put("attributes", null);
         childArchguide5.put("nodeValue", null); 
         ArrayList<HashMap<String, Object>> childArchguide5Children = new ArrayList<HashMap<String, Object>>(); 
          childArchguide5Children.add(buildDescriptiveNote(this.eag2012.getDescriptiveNoteLang(),this.eag2012.getDescriptiveNoteId()));
          childArchguide5Children.add(buildWebpage(this.eag2012.getWebpageHref(),this.eag2012.getWebpageValue()));
   
         childArchguide5.put("children", childArchguide5Children);
		
		return  childArchguide5;
	}

	private HashMap<String, Object> buildRefreshment() {

		 HashMap<String, Object> childArchguide5 = new HashMap<String, Object>();
         childArchguide5.put("nodeName", "refreshment");  //refreshment
         childArchguide5.put("attributes", null);
         childArchguide5.put("nodeValue", null); 
         ArrayList<HashMap<String, Object>> childArchguide5Children = new 	ArrayList<HashMap<String, Object>>(); 
          childArchguide5Children.add(buildDescriptiveNote(this.eag2012.getDescriptiveNoteLang(),this.eag2012.getDescriptiveNoteId()));
        
        childArchguide5.put("children", childArchguide5Children);
		
		return childArchguide5;
	}

	private HashMap<String, Object> buildTechservices() {

		 //Children of services, techservices
        
        HashMap<String, Object> childArchguide4 = new HashMap<String, Object>();
        childArchguide4.put("nodeName", "techservices");
        childArchguide4.put("attributes", null);
        childArchguide4.put("nodeValue", null); 
          
       //Children of techservices
           ArrayList<HashMap<String, Object>> childArchguide4Children = new ArrayList<HashMap<String, Object>>(); 
    
              childArchguide4Children.add(buildReproductionser(this.eag2012.getReproductionserQuestion()));
              childArchguide4Children.add(buildRestorationLab(this.eag2012.getRestorationlabQuestion()));
         
        childArchguide4.put("children",  childArchguide4Children); 
		
        return childArchguide4;
	}

	private HashMap<String, Object> buildRestorationLab(String restorationlabQuestion) {

		//Children restorationlab
        HashMap<String, Object> childArchguide5 = new HashMap<String, Object>();
        childArchguide5.put("nodeName", "restorationlab");  //restorationLab
        HashMap<String, String> childArchguide5Attributes = new HashMap<String, String>();
        childArchguide5Attributes.put("question", restorationlabQuestion);
        childArchguide5.put("attributes", childArchguide5Attributes);
        childArchguide5.put("nodeValue", null);
        
               //Children
               ArrayList<HashMap<String, Object>> childArchguide5Children = new ArrayList<HashMap<String, Object>>(); 
               childArchguide5Children.add(buildDescriptiveNote(this.eag2012.getDescriptiveNoteLang(),this.eag2012.getDescriptiveNoteId()));
               childArchguide5Children.add(buildContact());
               childArchguide5Children.add(buildWebpage(this.eag2012.getWebpageHref(),this.eag2012.getWebpageValue()));
        
         childArchguide5.put("children", childArchguide5Children);
		
		
		return  childArchguide5;
	}

	private HashMap<String, Object> buildReproductionser(String reproductionserQuestion) {

        HashMap<String, Object> childArchguide5 = new HashMap<String, Object>();
        childArchguide5.put("nodeName", "reproductionser");  //reproductionser
        HashMap<String, String> childArchguide5Attributes = new HashMap<String, String>();
        childArchguide5Attributes.put("question",reproductionserQuestion);
        childArchguide5.put("attributes", childArchguide5Attributes);
        childArchguide5.put("nodeValue", null);
          
             //Children of reproductionser
        ArrayList<HashMap<String, Object>> childArchguide5Children = new ArrayList<HashMap<String, Object>>(); 
          
        childArchguide5Children.add(buildDescriptiveNote(this.eag2012.getDescriptiveNoteLang(),this.eag2012.getDescriptiveNoteId()));
        childArchguide5Children.add(buildContact());
        childArchguide5Children.add(buildWebpage(this.eag2012.getWebpageHref(),this.eag2012.getWebpageValue()));
        childArchguide5Children.add(buildMicroformser(this.eag2012.getMicroformserQuestion()));       
        childArchguide5Children.add(buildPhotographser(this.eag2012.getPhotographserQuestion()));                
        childArchguide5Children.add(buildDigitalser(this.eag2012.getDigitalserQuestion())); 
        childArchguide5Children.add(buildPhotocopyser(this.eag2012.getPhotocopyserQuestion()));
                
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

	private HashMap<String, Object> buildInternetAccess(String internetAccessQuestion) {

		 //Children of services, internetAccess
        
        HashMap<String, Object> childArchguide4 = new HashMap<String, Object>();
        childArchguide4.put("nodeName", "internetAccess");
        HashMap<String, String> childArchguide4Attributes = new HashMap<String, String>();
        childArchguide4Attributes.put("question", internetAccessQuestion);
        childArchguide4.put("attributes", childArchguide4Attributes);
        childArchguide4.put("nodeValue", null);
          
            //Children of internetAccess
            ArrayList<HashMap<String, Object>> childArchguide4Children = new ArrayList<HashMap<String, Object>>(); 
            childArchguide4Children.add(buildDescriptiveNote(this.eag2012.getDescriptiveNoteLang(),this.eag2012.getDescriptiveNoteId())); //descriptiveNote
           
        childArchguide4.put("children", childArchguide4Children);
		
		return  childArchguide4;
	}

	private HashMap<String, Object> buildLibrary(String libraryQuestion) {

		//Children of services, library
        
        HashMap<String, Object> childArchguide4 = new HashMap<String, Object>();
        childArchguide4.put("nodeName", "library");
        HashMap<String, String> childArchguide4Attributes = new HashMap<String, String>();
        childArchguide4Attributes.put("question", libraryQuestion);
        childArchguide4.put("attributes", childArchguide4Attributes);
        childArchguide4.put("nodeValue", null);
          
            //Children of library
            ArrayList<HashMap<String, Object>> childArchguide4Children = new ArrayList<HashMap<String, Object>>(); 
              childArchguide4Children.add(buildContact()); //contact
              childArchguide4Children.add(buildWebpage(this.eag2012.getWebpageHref(),this.eag2012.getWebpageValue())); //webpage
              childArchguide4Children.add(buildMonographicpub());
              childArchguide4Children.add(buildSerialpub());  
       
       childArchguide4.put("children", childArchguide4Children);
		
		return childArchguide4;
	}

	private HashMap<String, Object> buildSerialpub() {

		 HashMap<String, Object> childArchguide5 = new HashMap<String, Object>();
	     childArchguide5.put("nodeName", "serialpub");  //serialpub
	     childArchguide5.put("attributes", null);
	     childArchguide5.put("nodeValue", null);
	     ArrayList<HashMap<String, Object>> childArchguide5Children = new ArrayList<HashMap<String, Object>>(); 
	        childArchguide5Children.add(buildNum("title",this.eag2012.getNumValue()));
	       
	     childArchguide5.put("children", childArchguide5Children);
		
		return childArchguide5;
	}

	private HashMap<String, Object> buildMonographicpub() {

		 
        HashMap<String, Object> childArchguide5 = new HashMap<String, Object>();
        childArchguide5.put("nodeName", "monographicpub");  //monographicpub
        childArchguide5.put("attributes", null);
        childArchguide5.put("nodeValue", null);
        ArrayList<HashMap<String, Object>> childArchguide5Children = new ArrayList<HashMap<String, Object>>(); 
          childArchguide5Children.add(buildNum("book",this.eag2012.getNumValue()));
      
        childArchguide5.put("children", childArchguide5Children);
		
		return childArchguide5;
	}

	private HashMap<String, Object> buildSearchroom() {

        HashMap<String, Object> childArchguide4 = new HashMap<String, Object>();
        childArchguide4.put("nodeName", "searchroom");
        childArchguide4.put("attributes", null);
        childArchguide4.put("nodeValue", null);
          
        //Children of searchroom
         ArrayList<HashMap<String, Object>> childArchguide4Children = new ArrayList<HashMap<String, Object>>(); 
           childArchguide4Children.add(buildContact()); //contact
           childArchguide4Children.add(buildWorkPlaces());
           childArchguide4Children.add(buildComputerPlaces());
           childArchguide4Children.add(buildMicrofilmPlaces());
           childArchguide4Children.add(buildWebpage(this.eag2012.getWebpageHref(),this.eag2012.getWebpageValue()));  //webpage
           childArchguide4Children.add(buildPhotographAllowance(this.eag2012.getPhotographAllowanceValue()));
           childArchguide4Children.add(buildReadersTicket(this.eag2012.getReadersTicketHref(),this.eag2012.getReadersTicketLang(),this.eag2012.getReadersTicketValue()));
           childArchguide4Children.add(buildAdvancedOrders(this.eag2012.getAdvancedOrdersHref(),this.eag2012.getAdvancedOrdersLang(),this.eag2012.getAdvancedOrdersValue()));
           childArchguide4Children.add(buildResearchservices());
        
     childArchguide4.put("children", childArchguide4Children);  
		
		
		return childArchguide4;
	}

	private HashMap<String, Object> buildResearchservices() {

	  HashMap<String, Object> childArchguide5 = new HashMap<String, Object>();
	   childArchguide5.put("nodeName", "researchservices");  //researchservices
	   childArchguide5.put("attributes", null);
	   childArchguide5.put("nodeValue", null);
	   ArrayList<HashMap<String, Object>> childArchguide5Children = new ArrayList<HashMap<String, Object>>(); 
	      childArchguide5Children.add(buildDescriptiveNote(this.eag2012.getDescriptiveNoteLang(),this.eag2012.getDescriptiveNoteId()));
	      
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

	private HashMap<String, Object> buildMicrofilmPlaces() {

		 HashMap<String, Object> childArchguide5 = new HashMap<String, Object>();
         childArchguide5.put("nodeName", "microfilmPlaces");  //microfilmPlaces
         childArchguide5.put("attributes", null);
         childArchguide5.put("nodeValue", null);
         ArrayList<HashMap<String, Object>> childArchguide5Children = new ArrayList<HashMap<String, Object>>(); 
            childArchguide5Children.add(buildNum("site",this.eag2012.getNumValue()));
         childArchguide5.put("children", childArchguide5Children);
		
		return childArchguide5;
	}

	private HashMap<String, Object> buildComputerPlaces() {

		 HashMap<String, Object> childArchguide5 = new HashMap<String, Object>();
         childArchguide5.put("nodeName", "computerPlaces");  //computerPlaces
         childArchguide5.put("attributes", null);
         childArchguide5.put("nodeValue", null);
         ArrayList<HashMap<String, Object>> childArchguide5Children = new ArrayList<HashMap<String, Object>>(); 
            childArchguide5Children.add(buildNum("site",this.eag2012.getNumValue()));
            childArchguide5Children.add(buildDescriptiveNote(this.eag2012.getDescriptiveNoteLang(),this.eag2012.getDescriptiveNoteId()));
          
          childArchguide5.put("children", childArchguide5Children);
		
		
		return childArchguide5;
	}

	private HashMap<String, Object> buildWorkPlaces() {

		 HashMap<String, Object> childArchguide5 = new HashMap<String, Object>();
	     childArchguide5.put("nodeName", "workPlaces");  //workPlaces
	     childArchguide5.put("attributes", null);
	     childArchguide5.put("nodeValue", null);
	     ArrayList<HashMap<String, Object>> childArchguide5Children = new ArrayList<HashMap<String, Object>>(); 
	        childArchguide5Children.add(buildNum("site",this.eag2012.getNumValue()));
	         
	     childArchguide5.put("children", childArchguide5Children);
			
		return  childArchguide5;
	}

	private HashMap<String, Object> buildAccessibility(String accessibilityQuestion, String accessibilityLang, String accessibilityValue) {

		 //Children of repository, accessibility
   	    HashMap<String, Object> childArchguide3 = new HashMap<String, Object>();
        childArchguide3.put("nodeName", "accessibility");
        HashMap<String, String> childArchguide3Attributes = new HashMap<String, String>();
          childArchguide3Attributes.put("question",accessibilityQuestion); //mandatory
          childArchguide3Attributes.put("xml:lang",accessibilityLang);
          childArchguide3.put("attributes", childArchguide3Attributes);
        childArchguide3.put("nodeValue", accessibilityValue); 
		
		return childArchguide3;
	}

	private HashMap<String, Object> buildAccess(String accessQuestion) {

		 //Children of repository, access
	    
	    HashMap<String, Object> childArchguide3 = new HashMap<String, Object>();
        childArchguide3.put("nodeName", "access");
        HashMap<String, String> childArchguide3Attributes = new HashMap<String, String>();
         childArchguide3Attributes.put("question",accessQuestion);
         childArchguide3.put("attributes", childArchguide3Attributes);
        childArchguide3.put("nodeValue", null); 
        
            //children of access
            
           ArrayList<HashMap<String, Object>> childArchguide3Children = new ArrayList<HashMap<String, Object>>();   
           
             childArchguide3Children.add(buildRestaccess(this.eag2012.getRestaccessLang(),this.eag2012.getRestaccessValue()));
             childArchguide3Children.add(buildTermsOfUse(this.eag2012.getTermsOfUseHref(),this.eag2012.getTermsOfUseLang(),this.eag2012.getTermsOfUseValue()));

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

	private HashMap<String, Object> buildRestaccess(String restaccessLang, String restaccessValue) {

		HashMap<String, Object> childArchguide4 = new HashMap<String, Object>();
        childArchguide4.put("nodeName", "restaccess");
        HashMap<String, String> childArchguide4Attributes = new HashMap<String, String>();
        childArchguide4Attributes.put("xml:lang",restaccessLang);
        childArchguide4.put("attributes", childArchguide4Attributes);
        childArchguide4.put("nodeValue", restaccessValue);
		
		return childArchguide4;
	}

	private HashMap<String, Object> buildTimetable() {

		 //Children of repository, timetable
	    
	    HashMap<String, Object> childArchguide3 = new HashMap<String, Object>();
        childArchguide3.put("nodeName", "timetable");
        childArchguide3.put("attributes",null);
        childArchguide3.put("nodeValue", null); 
	         
           //Children of timetable
           ArrayList<HashMap<String, Object>> childArchguide3Children = new ArrayList<HashMap<String, Object>>();   
	          
	           childArchguide3Children.add(buildOpening(this.eag2012.getOpeningValue()));
               childArchguide3Children.add(buildClosing(this.eag2012.getClosingStandardDate(),this.eag2012.getClosingValue()));
	    
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

	private HashMap<String, Object> buildHoldings() {

		 //Children holdings, children of repository
 	   HashMap<String, Object> childArchguide3 = new HashMap<String, Object>();
        childArchguide3.put("nodeName", "holdings");
        childArchguide3.put("attributes",null);
        childArchguide3.put("nodeValue", null); 
          
          //Children of holdings
          ArrayList<HashMap<String, Object>> childArchguide3Children = new ArrayList<HashMap<String, Object>>();   
 	        childArchguide3Children.add(buildDescriptiveNote(this.eag2012.getDescriptiveNoteLang(),this.eag2012.getDescriptiveNoteId()));
 	        childArchguide3Children.add(buildDateSet(this.eag2012.getDateSetId(), this.eag2012.getDateSetLang()));
 	        childArchguide3Children.add(buildExtent());
 	    
 	        childArchguide3.put("children", childArchguide3Children);
		
		
		return childArchguide3;
	}

	private HashMap<String, Object> buildExtent() {

		 HashMap<String, Object> childArchguide4 = new HashMap<String, Object>();
	      childArchguide4.put("nodeName", "extent");
	      childArchguide4.put("attributes", null);
	      childArchguide4.put("nodeValue", null);
	      
	           //Children of extent
	           ArrayList<HashMap<String, Object>> childArchguide4Children = new ArrayList<HashMap<String, Object>>();
                  childArchguide4Children.add(buildNum("linearmetre",this.eag2012.getNumValue()));
          
	      childArchguide4.put("children", childArchguide4Children);
		
		return childArchguide4;
	}

	private HashMap<String, Object> buildAdminhierarchy() {

		  //Children of repository, adminhierarchy
 	    HashMap<String, Object> childArchguide3 = new HashMap<String, Object>();
        childArchguide3.put("nodeName", "adminhierarchy");
        childArchguide3.put("attributes",null);
        childArchguide3.put("nodeValue", null); 
           
           //Children of adminhierarchy
           ArrayList<HashMap<String, Object>> childArchguide3Children = new ArrayList<HashMap<String, Object>>();  
             childArchguide3Children.add(buildAdminunit(this.eag2012.getAdminunitLang(),this.eag2012.getAdminunitValue()));                        
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

	private HashMap<String, Object> buildBuildinginfo() {

		HashMap<String, Object> childArchguide3 = new HashMap<String, Object>();
         childArchguide3.put("nodeName", "buildinginfo");
         childArchguide3.put("attributes",null);
         childArchguide3.put("nodeValue", null);
     	
          ArrayList<HashMap<String, Object>> childArchguide3Children = new ArrayList<HashMap<String, Object>>();   
     	        
              childArchguide3Children.add(buildBuilding());
              childArchguide3Children.add(buildRepositorarea());     
     	      childArchguide3Children.add(buildLengthshelf()); 
     	       
     	childArchguide3.put("children", childArchguide3Children);  	
		
		return childArchguide3;
	}

	
	private HashMap<String, Object> buildLengthshelf() {

		HashMap<String, Object> childArchguide4 = new HashMap<String, Object>();
        childArchguide4.put("nodeName", "lengthshelf");
        childArchguide4.put("attributes", null);
        childArchguide4.put("nodeValue", null);
        ArrayList<HashMap<String, Object>> childArchguide4Children = new ArrayList<HashMap<String, Object>>();
          childArchguide4Children.add(buildNum("linearmetre",this.eag2012.getNumValue()));
        
        childArchguide4.put("children",childArchguide4Children);
		
		return childArchguide4;
	}

	private HashMap<String, Object> buildRepositorarea(){
		HashMap<String, Object> childArchguide4 = new HashMap<String, Object>();
	       childArchguide4.put("nodeName", "repositorarea");
	       childArchguide4.put("attributes", null);
	       childArchguide4.put("nodeValue", null);
	       ArrayList<HashMap<String, Object>> childArchguide4Children = new ArrayList<HashMap<String, Object>>();
	         childArchguide4Children.add(buildNum("squaremetre",this.eag2012.getNumValue()));
                       	       
           childArchguide4.put("children",childArchguide4Children);	
		
		return childArchguide4;
		
	}
	private HashMap<String, Object> buildBuilding(){
		
		HashMap<String, Object>childArchguide4 = new HashMap<String, Object>();
	      childArchguide4.put("nodeName", "building");
          childArchguide4.put("attributes", null);
          childArchguide4.put("nodeValue", null);
        
          ArrayList<HashMap<String, Object>> childArchguide4Children = new ArrayList<HashMap<String, Object>>();
             childArchguide4Children.add(buildDescriptiveNote(this.eag2012.getDescriptiveNoteLang(),this.eag2012.getDescriptiveNoteId()));
        
          childArchguide4.put("children",childArchguide4Children);	
		
	return	childArchguide4;
	}
	
	private HashMap<String, Object> buildRepositorsup() {

		HashMap<String,Object> childArchguide3 = new HashMap<String, Object>();
         childArchguide3.put("nodeName", "repositorsup");
         childArchguide3.put("attributes",null);
         childArchguide3.put("nodeValue", null);
     	
         ArrayList<HashMap<String, Object>>  childArchguide3Children = new ArrayList<HashMap<String, Object>>();   
     	       
     	   childArchguide3Children.add(buildDate(this.eag2012.getDateNotAfter(),this.eag2012.getDateNotBefore(),this.eag2012.getDateStandardDate(),this.eag2012.getDateId(),this.eag2012.getDateLang(),this.eag2012.getDateValue()));
           childArchguide3Children.add(buildRule(this.eag2012.getRuleLang(),this.eag2012.getRuleValue()));    	       
     	      
           childArchguide3.put("children", childArchguide3Children);
		
		return childArchguide3;
	}

	private HashMap<String, Object> buildRepositorfound() {

		 HashMap<String, Object> childArchguide3 = new HashMap<String, Object>();
          childArchguide3.put("nodeName", "repositorfound");
          childArchguide3.put("attributes",null);
   	      childArchguide3.put("nodeValue", null);
   	
   	      ArrayList<HashMap<String, Object>> childArchguide3Children = new ArrayList<HashMap<String, Object>>();   
   	       
   	         childArchguide3Children.add(buildDate(this.eag2012.getDateNotAfter(),this.eag2012.getDateNotBefore(),this.eag2012.getDateStandardDate(),this.eag2012.getDateId(),this.eag2012.getDateLang(),this.eag2012.getDateValue()));
             childArchguide3Children.add(buildRule(this.eag2012.getRuleLang(),this.eag2012.getRuleValue()));    	       
   	      
             childArchguide3.put("children", childArchguide3Children);
		
		return childArchguide3;
	}

	private HashMap<String, Object> buildRepositorHist() {

		HashMap<String, Object> childArchguide3 = new HashMap<String, Object>();
         childArchguide3.put("nodeName", "repositorhist");
         childArchguide3.put("attributes",null);
  	     childArchguide3.put("nodeValue", null);
  	     ArrayList<HashMap<String, Object>>childArchguide3Children = new ArrayList<HashMap<String, Object>>();
   	       childArchguide3Children.add(buildDescriptiveNote(this.eag2012.getDescriptiveNoteLang(),this.eag2012.getDescriptiveNoteId()));
   	       
   	     childArchguide3.put("children", childArchguide3Children);  
			
		return childArchguide3;
	}

	private HashMap<String, Object> buildDirections(String directionsLang, String directionsValue ) {

		 HashMap<String, Object>childArchguide3 = new HashMap<String, Object>();
         childArchguide3.put("nodeName", "directions");
         HashMap<String, String>childArchguide3Attributes = new HashMap<String, String>();
     	 childArchguide3Attributes.put("xml:lang", directionsLang);
         childArchguide3.put("attributes",childArchguide3Attributes);
     	 childArchguide3.put("nodeValue", directionsValue);
     	 ArrayList<HashMap<String, Object>> childArchguide3Children = new ArrayList<HashMap<String, Object>>();
 	  
	          childArchguide3Children.add(buildCitation(this.eag2012.getCitationId(),this.eag2012.getCitationLang(),this.eag2012.getCitationLastDateTimeVerified(),this.eag2012.getCitationHref(),this.eag2012.getCitationValue()));
	       
	       childArchguide3.put("children", childArchguide3Children);
		
		return childArchguide3;
	}

	private HashMap<String, Object> buildLocation(String locationLocalType, String locationLatitude, String locationLongitude) {

	   HashMap<String,Object> childArchguide3 = new HashMap<String, Object>();
    	childArchguide3.put("nodeName", "location");
    	HashMap<String, String>childArchguide3Attributes = new HashMap<String, String>();
    	childArchguide3Attributes.put("localType", locationLocalType);
    	childArchguide3Attributes.put("latitude", locationLatitude);
    	childArchguide3Attributes.put("longitude", locationLongitude);
    	childArchguide3.put("attributes",childArchguide3Attributes);
    	childArchguide3.put("nodeValue", null);
    	  
    	  ArrayList<HashMap<String, Object>> childArchguide3Children = new ArrayList<HashMap<String, Object>>();
    	  
          childArchguide3Children.add(buildCountry(this.eag2012.getCountryLang(),this.eag2012.getCountryValue()));
          childArchguide3Children.add(buildFirstdem(this.eag2012.getFirstdemLang(),this.eag2012.getFirstdemValue()));
          childArchguide3Children.add(buildSecondem(this.eag2012.getSecondemLang(),this.eag2012.getSecondemValue()));
          childArchguide3Children.add(buildMunicipalityPostalCode(this.eag2012.getMunicipalityPostalcodeLang(),this.eag2012.getMunicipalityPostalcodeValue()));
          childArchguide3Children.add(buildLocalentity(this.eag2012.getLocalentityLang(),this.eag2012.getLocalentityValue()));
          childArchguide3Children.add(buildStreet(this.eag2012.getStreetLang(),this.eag2012.getStreetValue()));
          
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

	private HashMap<String, Object> buildGeogarea(String geogareaLang, String geogareaValue) {

		 HashMap<String, Object> childArchguide3 = new HashMap<String, Object>();
		    childArchguide3.put("nodeName", "geogarea");
		    HashMap<String, String>childArchguide3Attributes = new HashMap<String, String>();
      	    childArchguide3Attributes.put("xml:lang", geogareaLang);
      	    childArchguide3.put("attributes",childArchguide3Attributes);
      	    childArchguide3.put("nodeValue", /*"Europe"*/geogareaValue);
		
		return childArchguide3;
	}

	private HashMap<String, Object> buildRepositoryRole(String repositoryRoleValue) {

		  HashMap<String, Object> childArchguide3 = new HashMap<String, Object>();
		    childArchguide3.put("nodeName", "repositoryRole");
		    childArchguide3.put("attributes", null);
		    childArchguide3.put("nodeValue", /*"Headquarter"*/repositoryRoleValue);
			
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

	private HashMap<String, Object> buildContact() {
		
		 HashMap<String, Object>  childArchguidechild = new HashMap<String, Object>(); 
         childArchguidechild.put("nodeName", "contact");
         childArchguidechild.put("attributes",null);
         childArchguidechild.put("nodeValue", null); 
         		
         ArrayList<HashMap<String, Object>> childArchguideChildren = new ArrayList<HashMap<String, Object>>();
         childArchguideChildren.add(buildTelephone(this.eag2012.getTelephoneValue()));
         childArchguideChildren.add(buildEmail(this.eag2012.getEmailHref(),this.eag2012.getEmailValue()));
         
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

	private HashMap<String, Object> buildRule(String ruleLang,String ruleValue) {
		
		HashMap<String, Object> childArchguide4 = new HashMap<String, Object>();
		
  	    childArchguide4.put("nodeName", "rule");
        HashMap<String, String> childArchguide4Attributes = new HashMap<String, String>();
        childArchguide4Attributes.put("xml:lang", ruleLang);
        childArchguide4.put("attributes", childArchguide4Attributes);
        childArchguide4.put("nodeValue", ruleValue);
        
		return childArchguide4;
	}

	private HashMap<String, Object> buildWebpage(String webpageHref, String webpageValue) {
		
		HashMap<String, Object> childArchguide3 = new HashMap<String, Object>();
		childArchguide3 = new HashMap<String, Object>();
        childArchguide3.put("nodeName","webpage");
        HashMap<String, String> childArchguide3Attributes = new HashMap<String, String>();
        childArchguide3Attributes.put("href", webpageHref);
        childArchguide3.put("attributes", childArchguide3Attributes);
        childArchguide3.put("nodeValue", /*"Go to the website"*/webpageValue);
		

		return childArchguide3;
	}

	private HashMap<String, Object> buildEmail(String emailHref,String emailValue) {
		
		HashMap<String, Object> childArchguide3 = new HashMap<String, Object>();
		childArchguide3 = new HashMap<String, Object>();
        childArchguide3.put("nodeName","email");
        HashMap<String, String> childArchguide3Attributes = new HashMap<String, String>();
        childArchguide3Attributes.put("href", emailHref);
        childArchguide3.put("attributes", childArchguide3Attributes);
        childArchguide3.put("nodeValue", /*"Send an e-mail"*/emailValue);
		
		
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
