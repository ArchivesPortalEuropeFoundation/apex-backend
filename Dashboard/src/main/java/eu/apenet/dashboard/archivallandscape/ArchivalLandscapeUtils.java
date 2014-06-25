package eu.apenet.dashboard.archivallandscape;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.XMLConstants;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.apache.log4j.Logger;

import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.dashboard.security.SecurityContext;
import eu.apenet.persistence.dao.ArchivalInstitutionDAO;
import eu.apenet.persistence.dao.HoldingsGuideDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.AiAlternativeName;
import eu.apenet.persistence.vo.ArchivalInstitution;
import eu.apenet.persistence.vo.CouAlternativeName;
import eu.apenet.persistence.vo.Country;
import eu.apenet.persistence.vo.HoldingsGuide;

public class ArchivalLandscapeUtils {
	
	private static final String AL_XMLNS = "urn:isbn:1-931666-22-9";
	private static final String AL_XMLNS_XLINK = "http://www.w3.org/1999/xlink";
	private static final String AL_XMLNS_XSI = XMLConstants.W3C_XML_SCHEMA_INSTANCE_NS_URI; //"http://www.w3.org/2001/XMLSchema-instance";
	private static final String AL_AUDIENCE = "external";
	private static final String AL_XSI_SCHEMALOCATION = "urn:isbn:1-931666-22-9 ead.xsd";
	private static final String AL_EADHEADER_COUNTRYENCODING = "iso3166-1";
	private static final String AL_EADHEADER_DATAENCODING = "iso8601";
	private static final String AL_EADHEADER_LANGENCODING = "iso639-2b";
	private static final String AL_EADHEADER_RELATEDENCODING = "MARC21";
	private static final String AL_EADHEADER_REPOSITORYENCODING = "iso15511";
	private static final String AL_EADHEADER_SCRIPTENCODING = "iso15924";
	private static final String AL_EADID_IDENTIFIER = "ape_archival_landscape";
	private static final String AL_EADID_MAINAGENCYCODE = "-APEnet";
	private static final String AL_EADID_PREFIX = "AL-";
	private static final String AL_TITLEPROPER_ENCODINGANALOG = "245";
	private static final String AL_TITLEPROPER_TYPE = "eng";
	private static final String AL_TITLEPROPER = "Archives Portal Europe - Archival Landscape";
	private static final String AL_GLOBAL_ENCODINGANALOG = "3.1.4";
	private static final String AL_RELATEDENCODING = "ISAD(G)v2";
	private static final String AL_ARCHDESC_TYPE = "inventory"; //probably could be "archival_landscape" value like other ALs
	private static final String AI_DSC_TYPE = "othertype";
	private static final String COUNTRYCODE = "countrycode";
	
	private static final String AL_GLOBAL_UNITTITLE = "European countries";
	
	private static final String EADID = "eadid";
	protected static final String LEVEL = "level";
	protected static final String FONDS = "fonds";
	protected static final String SERIES = "series";
	protected static final String FILE = "file";
	
	private static Logger log = Logger.getLogger(ArchivalLandscapeUtils.class);
	
	protected static boolean checkIfTwoInstitutionsHasTheSameParents(ArchivalInstitution ingestedInstitution,ArchivalInstitution archivalInstitution) {
		boolean state = false;
		if(ingestedInstitution!=null && archivalInstitution!=null 
				&& ingestedInstitution.getInternalAlId()!=null && archivalInstitution.getInternalAlId()!=null &&   
				ingestedInstitution.getInternalAlId().equals(archivalInstitution.getInternalAlId())){
			ArchivalInstitution parentIngestedInstitution = ingestedInstitution.getParent();
			ArchivalInstitution parentArchivalInstitution = archivalInstitution.getParent();
			if(parentIngestedInstitution!=null && parentArchivalInstitution!=null){
				state = checkIfTwoInstitutionsHasTheSameParents(parentIngestedInstitution,parentArchivalInstitution);
			}else if(parentIngestedInstitution==null && parentArchivalInstitution==null){
				state = true;
			}
		}
		return state;
	}
	
	/**
	 * Seek an institution into ArchivalInstitution structure.
	 * The seek institution comes from an AL file.
	 * 
	 * @param ingestedInstitution
	 * @param archivalInstitutions
	 * @return ArchivalInstitution.TARGET / NULL if not found
	 */
	protected static ArchivalInstitution getInstitutionFromArchivalInstitutionStructure(ArchivalInstitution ingestedInstitution, Collection<ArchivalInstitution> archivalInstitutions) {
		ArchivalInstitution targetInstitution = null;
		if(archivalInstitutions!=null && ingestedInstitution!=null && ingestedInstitution.getInternalAlId()!=null){
			Iterator<ArchivalInstitution> itArchivalInstitutions = archivalInstitutions.iterator();
			while(targetInstitution==null && itArchivalInstitutions.hasNext()){
				ArchivalInstitution fileArchivalInstitution = itArchivalInstitutions.next();
				if(fileArchivalInstitution.getInternalAlId().equals(ingestedInstitution.getInternalAlId())){
					targetInstitution = fileArchivalInstitution;
				}else if(fileArchivalInstitution.isGroup()){
					targetInstitution = getInstitutionFromArchivalInstitutionStructure(ingestedInstitution,fileArchivalInstitution.getChildArchivalInstitutions());
				}
			}
		}
		return targetInstitution;
	}
	
	protected static Collection<ArchivalInstitution> getInstitutionsWithEmptyIdFromArchivalInstitutionStructure(Collection<ArchivalInstitution> archivalInstitutions){
		Collection<ArchivalInstitution> detectedArchivalInstitutions = null;
		if(archivalInstitutions!=null){
			detectedArchivalInstitutions = new ArrayList<ArchivalInstitution>();
			Iterator<ArchivalInstitution> itArchivalInstitutions = archivalInstitutions.iterator();
			while(itArchivalInstitutions.hasNext()){
				ArchivalInstitution archivalInstitution = itArchivalInstitutions.next();
				if(archivalInstitution.getInternalAlId().trim().isEmpty()){
					detectedArchivalInstitutions.add(archivalInstitution); 
				}
				if(archivalInstitution.isGroup()){
					List<ArchivalInstitution> children = archivalInstitution.getChildArchivalInstitutions();
					if(children!=null && children.size()>0){
						detectedArchivalInstitutions.addAll(getInstitutionsWithEmptyIdFromArchivalInstitutionStructure(children));
					}
				}
			}
		}
		return detectedArchivalInstitutions;
	}
	
	protected static Map<String,List<ArchivalInstitution>> getInstitutionsWithSameIdentifierFromArchivalInstitutionStructure(Collection<ArchivalInstitution> archivalInstitutions){
		Map<String,List<ArchivalInstitution>> allRepeatedInstitutions = null;
		Map<String, List<ArchivalInstitution>> allInstitutionsByIdentifier = getInstitutionsWithSameIdentifierFromArchivalInstitutionStructure(archivalInstitutions,null);
		if(allInstitutionsByIdentifier!=null && allInstitutionsByIdentifier.size()>0){
			Set<String> keys = allInstitutionsByIdentifier.keySet();
			allRepeatedInstitutions = new HashMap<String,List<ArchivalInstitution>>();
			if(keys!=null){
				Iterator<String> itKeys = keys.iterator();
				while(itKeys.hasNext()){
					String key = itKeys.next();
					List<ArchivalInstitution> tempList = allInstitutionsByIdentifier.get(key);
					if(tempList!=null && tempList.size()>1){
						allRepeatedInstitutions.put(key,tempList);
					}
				}
			}
		}
		return allRepeatedInstitutions;
	}

	private static Map<String,List<ArchivalInstitution>> getInstitutionsWithSameIdentifierFromArchivalInstitutionStructure(Collection<ArchivalInstitution> archivalInstitutions,Map<String,List<ArchivalInstitution>> allRepeatedInstitutions){
		if(allRepeatedInstitutions==null){
			allRepeatedInstitutions = new HashMap<String,List<ArchivalInstitution>>();
		}
		if(archivalInstitutions!=null){
			Iterator<ArchivalInstitution> itArchivalInstitutions = archivalInstitutions.iterator();
			while(itArchivalInstitutions.hasNext()){
				ArchivalInstitution archivalInstitution = itArchivalInstitutions.next();
				List<ArchivalInstitution> tempList = null;
				if(archivalInstitution.getInternalAlId()!=null){
					tempList = allRepeatedInstitutions.get(archivalInstitution.getInternalAlId());
					if(tempList==null){
						tempList = new ArrayList<ArchivalInstitution>();
					}
					tempList.add(archivalInstitution);
					allRepeatedInstitutions.put(archivalInstitution.getInternalAlId(), tempList);	
				}else{
					tempList = allRepeatedInstitutions.get(""); //empty identifier key
					if(tempList==null){
						tempList = new ArrayList<ArchivalInstitution>();
					}
					tempList.add(archivalInstitution);
					allRepeatedInstitutions.put(archivalInstitution.getInternalAlId(), tempList);
				}
				if(archivalInstitution.isGroup()){
					List<ArchivalInstitution> children = archivalInstitution.getChildArchivalInstitutions();
					if(children!=null){
						allRepeatedInstitutions = getInstitutionsWithSameIdentifierFromArchivalInstitutionStructure(children,allRepeatedInstitutions);
					}
				}
			}
		}
		
		return allRepeatedInstitutions;
	}

	/**
	 * Method to check and returns, the list of institutions that has the same
	 * name in the collection passed.
	 *
	 * @param archivalInstitutions List of institutions and/or groups to check.
	 * @return The list of related institutions.
	 */
	protected static Map<String,List<ArchivalInstitution>> getInstitutionsWithSameNameFromArchivalInstitutionStructure(Collection<ArchivalInstitution> archivalInstitutions) {
		Map<String, List<ArchivalInstitution>> allRepeatedInstitutions = null;
		Map<String, List<ArchivalInstitution>> allInstitutionsByName = getInstitutionsWithSameNameFromArchivalInstitutionStructure(archivalInstitutions, allRepeatedInstitutions);

		if (allInstitutionsByName != null && allInstitutionsByName.size() > 0){
			Set<String> keys = allInstitutionsByName.keySet();
			allRepeatedInstitutions = new HashMap<String, List<ArchivalInstitution>>();
			if (keys != null){
				Iterator<String> itKeys = keys.iterator();
				while (itKeys.hasNext()) {
					String key = itKeys.next();
					List<ArchivalInstitution> tempList = allInstitutionsByName.get(key);
					if (tempList != null && tempList.size() > 1) {
						allRepeatedInstitutions.put(key,tempList);
					}
				}
			}
		}

		return allRepeatedInstitutions;
	}

	/**
	 * Method to obtain the institutions listed under same name.
	 *
	 * @param archivalInstitutions List of institutions and/or groups to check.
	 * @param allRepeatedInstitutions List of institutions and/or groups checked.
	 *
	 * @return Map of institutions with keys the same name.
	 */
	private static Map<String,List<ArchivalInstitution>> getInstitutionsWithSameNameFromArchivalInstitutionStructure(Collection<ArchivalInstitution> archivalInstitutions, Map<String,List<ArchivalInstitution>> allRepeatedInstitutions) {
		if (allRepeatedInstitutions == null) {
			allRepeatedInstitutions = new HashMap<String, List<ArchivalInstitution>>();
		}

		if (archivalInstitutions != null) {
			Iterator<ArchivalInstitution> itArchivalInstitutions = archivalInstitutions.iterator();
			while (itArchivalInstitutions.hasNext()) {
				ArchivalInstitution archivalInstitution = itArchivalInstitutions.next();
				List<ArchivalInstitution> tempList = null;
				if (archivalInstitution.getAiname() != null) {
					tempList = allRepeatedInstitutions.get(archivalInstitution.getAiname());

					if (tempList == null) {
						tempList = new ArrayList<ArchivalInstitution>();
					}

					tempList.add(archivalInstitution);
					allRepeatedInstitutions.put(archivalInstitution.getAiname(), tempList);	
				} else {
					tempList = allRepeatedInstitutions.get(""); //empty identifier key

					if (tempList == null){
						tempList = new ArrayList<ArchivalInstitution>();
					}

					tempList.add(archivalInstitution);
					allRepeatedInstitutions.put(archivalInstitution.getAiname(), tempList);
				}
			}
		}

		return allRepeatedInstitutions;
	}

	/**
	 * Parse an archivalInstitution structure to a plain internal identifiers
	 * @param archivalInstitutions
	 * @return List<String> internalIdentifiers (plain list)
	 */
	protected static List<String> getIdentifiersFromArchivalInstitutionStructure(Collection<ArchivalInstitution> archivalInstitutions) {
		List<String> institutionsIdentifiers = new ArrayList<String>();
		Iterator<ArchivalInstitution> itArchivalInstitutions = archivalInstitutions.iterator();
		while(itArchivalInstitutions.hasNext()){
			ArchivalInstitution targetInstitution = itArchivalInstitutions.next();
			String identifier = targetInstitution.getInternalAlId();
			institutionsIdentifiers.add(identifier);
			if(targetInstitution.isGroup()){
				List<ArchivalInstitution> children = targetInstitution.getChildArchivalInstitutions();
				if(children!=null && children.size()>0){
					institutionsIdentifiers.addAll(getIdentifiersFromArchivalInstitutionStructure(children));
				}
			}
		}
		return institutionsIdentifiers;
	}

	public static ArchivalInstitution getInstitutionByNameFromStructure(String emptyIdentifierNamed,Collection<ArchivalInstitution> archivalInstitutionList) {
		ArchivalInstitution target = null;
		if(archivalInstitutionList!=null){
			Iterator<ArchivalInstitution> itArchivalInstitution = archivalInstitutionList.iterator();
			while(target==null && itArchivalInstitution.hasNext()){
				ArchivalInstitution tempInstitution = itArchivalInstitution.next();
				if(tempInstitution.getAiname().equals(emptyIdentifierNamed)){
					target = tempInstitution;
				}else if(tempInstitution.isGroup()){
					List<ArchivalInstitution> children = tempInstitution.getChildArchivalInstitutions();
					if(children!=null && children.size()>0){
						target = getInstitutionByNameFromStructure(emptyIdentifierNamed, children);
					}
				}
			}
		}
		return target;
	}

	/**
	 * Recovers the list of institutions with same name from the collection
	 * passed.
	 *
	 * @param institutionName Name of the institution to search.
	 * @param archivalInstitutionList Collection of institutions in which the search will be performed.
	 *
	 * @return List of institutions with same name in the collection.
	 */
	public static List<ArchivalInstitution> getInstitutionsByNameFromStructure(String institutionName, Collection<ArchivalInstitution> archivalInstitutionList) {
		List<ArchivalInstitution> target = new ArrayList<ArchivalInstitution>();

		if (archivalInstitutionList != null) {
			Iterator<ArchivalInstitution> itArchivalInstitution = archivalInstitutionList.iterator();

			while (itArchivalInstitution.hasNext()) {
				ArchivalInstitution tempInstitution = itArchivalInstitution.next();

				if (tempInstitution.getAiname().equals(institutionName)) {
					target.add(tempInstitution);
				} else if (tempInstitution.isGroup()) {
					List<ArchivalInstitution> children = tempInstitution.getChildArchivalInstitutions();

					if (children != null && children.size() > 0) {
						target.addAll(getInstitutionsByNameFromStructure(institutionName, children));
					}
				}
			}
		}

		return target;
	}

	/**
	 * Recovers the institution with the internal identifier from the
	 * collection passed.
	 *
	 * @param internalIdentifier Internal identifier of the institution to search.
	 * @param archivalInstitutionList Collection of institutions in which the search will be performed.
	 *
	 * @return Institutions with the internal identifier in the collection.
	 */
	public static ArchivalInstitution getInstitutionsByInternalIdentifierFromStructure(String internalIdentifier, Collection<ArchivalInstitution> archivalInstitutionList) {
		ArchivalInstitution target = null;

		if (archivalInstitutionList != null) {
			Iterator<ArchivalInstitution> itArchivalInstitution = archivalInstitutionList.iterator();

			while (target == null && itArchivalInstitution.hasNext()) {
				ArchivalInstitution tempInstitution = itArchivalInstitution.next();

				if (tempInstitution.getInternalAlId().equals(internalIdentifier)) {
					target = tempInstitution;
				} else if (tempInstitution.isGroup()) {
					List<ArchivalInstitution> children = tempInstitution.getChildArchivalInstitutions();

					if (children != null && children.size() > 0) {
						target = getInstitutionsByInternalIdentifierFromStructure(internalIdentifier, children);
					}
				}
			}
		}

		return target;
	}
	
	/**
	 * Replaces a node from an archivalInstitution List with the new.
	 * Uses oldArchivalInstitution to set a target to be replaced (by newArchivalInstitution).
	 * (Only it's replaced when internal identifier match or if internal identifier of
	 * old institution is empty the ainame match).
	 * 
	 * @param oldInstitution
	 * @param newArchivalInstitution
	 * @param archivalInstitutionList
	 * @return
	 */
	public static List<ArchivalInstitution> replaceAnInstitutionToArchivalInstitutionStructure(ArchivalInstitution oldArchivalInstitution,ArchivalInstitution newArchivalInstitution,Collection<ArchivalInstitution> archivalInstitutionList) {
		List<ArchivalInstitution> institutions = new ArrayList<ArchivalInstitution>();
		Iterator<ArchivalInstitution> itArchivalInstitutions = archivalInstitutionList.iterator();
		while(itArchivalInstitutions.hasNext()){
			ArchivalInstitution targetInstitution = itArchivalInstitutions.next();
			if(targetInstitution.isGroup()){
				List<ArchivalInstitution> children = targetInstitution.getChildArchivalInstitutions();
				if(children!=null && children.size()>0){
					children = replaceAnInstitutionToArchivalInstitutionStructure(oldArchivalInstitution,newArchivalInstitution,children); //search into children structure for replace node
					targetInstitution.setChildArchivalInstitutions(children);
				}
			}
			if(oldArchivalInstitution.getInternalAlId().equals(targetInstitution.getInternalAlId()) ||
				((oldArchivalInstitution.getInternalAlId()==null || oldArchivalInstitution.getInternalAlId().trim().isEmpty()) && oldArchivalInstitution.getAiname().equals(newArchivalInstitution.getAiname()) ) ){
				newArchivalInstitution.setChildArchivalInstitutions(targetInstitution.getChildArchivalInstitutions()); //replace children if they exists
				targetInstitution = newArchivalInstitution;
			}
			institutions.add(targetInstitution);
		}
		return institutions;
	}
	
	/**
	 * Replace an institution by other institution with the same identifier and the same name
	 * into a structure. It's necessary both conditions are true.
	 *  
	 * @param oldInstitution
	 * @param newInstitution
	 * @param archivalInstitutionList
	 * @return
	 */
	public static List<ArchivalInstitution> replaceXMLInstitutionByInstitution(ArchivalInstitution oldInstitution, ArchivalInstitution newInstitution,Collection<ArchivalInstitution> archivalInstitutionList) {
		List<ArchivalInstitution> institutions = new ArrayList<ArchivalInstitution>();
		if(oldInstitution!=null && newInstitution!=null && archivalInstitutionList!=null){
			Iterator<ArchivalInstitution> itArchivalInstitutions = archivalInstitutionList.iterator();
			while(itArchivalInstitutions.hasNext()){
				ArchivalInstitution targetInstitution = itArchivalInstitutions.next();
				if(targetInstitution.isGroup()){
					List<ArchivalInstitution> children = targetInstitution.getChildArchivalInstitutions();
					if(children!=null && children.size()>0){
						children = replaceXMLInstitutionByInstitution(oldInstitution,newInstitution,children);
						targetInstitution.setChildArchivalInstitutions(children);
					}
				}
				if(oldInstitution.getInternalAlId().equals(targetInstitution.getInternalAlId())
						&& oldInstitution.getAiname().equals(targetInstitution.getAiname()) ){
					newInstitution.setChildArchivalInstitutions(targetInstitution.getChildArchivalInstitutions()); //replace children if they exists
					targetInstitution = newInstitution;
				}
				institutions.add(targetInstitution);
			}
		}
		return institutions;
	}
	
	/**
	 * Called into parseCollectionToPlainList method.
	 * Functionality is very similar, but the different is the argument parent.
	 * 
	 * Parent is used to get all children and call to himself by recursive-way if needed.
	 * 
	 * @param parent
	 * @return List<ArchivalInstition> plainArchivalInstitutions
	 */
	private static List<ArchivalInstitution> checkChild(ArchivalInstitution parent){
		List<ArchivalInstitution> archivalInstitutionList = new ArrayList<ArchivalInstitution>();
		log.debug("children check for parent: "+parent.getAiname());
		Set<ArchivalInstitution> children = new LinkedHashSet<ArchivalInstitution>(parent.getChildArchivalInstitutions());
		Iterator<ArchivalInstitution> itChildren = children.iterator();
		while(itChildren.hasNext()){
			ArchivalInstitution institution = itChildren.next();
			if(institution.isGroup()){
				log.debug("Group: "+institution.getAiname()+" parent: "+parent.getAiname());
				if(institution.getChildArchivalInstitutions()!=null && institution.getChildArchivalInstitutions().size()>0){
					archivalInstitutionList.addAll(checkChild(institution)); //recursive call
				}
			}else{
				log.debug("Institution: "+institution.getAiname()+" parent: "+parent.getAiname());
			}
			institution.setParentAiId((institution.getParent()!=null)?institution.getParent().getAiId():null); //parent_ai_id fix for bad hibernate mapping
			archivalInstitutionList.add(institution);
		}
		return archivalInstitutionList;
	}
	
	protected List<ArchivalInstitution> getExcludedInstitutions(Collection<ArchivalInstitution> newArchivalInstitutionStructure,List<String> ingestedIdentifiers) {
		List<ArchivalInstitution> excludedInstitutions = new ArrayList<ArchivalInstitution>();
		if(newArchivalInstitutionStructure!=null){
			Iterator<ArchivalInstitution> it = newArchivalInstitutionStructure.iterator();
			while(it.hasNext()){
				ArchivalInstitution tempAI = it.next();
				String tempInternalIdentifier = tempAI.getInternalAlId();
				if(!ingestedIdentifiers.contains(tempInternalIdentifier)){
					excludedInstitutions.add(tempAI); //new archival institution
				}else{
					if(tempAI.isGroup()){
						List<ArchivalInstitution> children = tempAI.getChildArchivalInstitutions();
						if(children!=null && children.size()>0){ //append excluded children
							excludedInstitutions.addAll(getExcludedInstitutions(children,ingestedIdentifiers));
						}
					}
				}
			}
		}
		return excludedInstitutions;
	}
	
	/**
	 * Checks if some identifier is repeated.
	 * It's used before do anything, and necessary for checks and 
	 * discriminate states with archival institution edition.
	 * 
	 * @param archivalInstitutions
	 * @return state<boolean>
	 */
	protected static Collection<String> checkIdentifiersForArchivalInstitutionStructure(Collection<ArchivalInstitution> archivalInstitutions) {
		Collection<String> internalIdentifiers = null;
		if(archivalInstitutions!=null){
			internalIdentifiers = new HashSet<String>();
			boolean state = true; //at least for this moment
			Iterator<ArchivalInstitution> it = archivalInstitutions.iterator();
			while(it.hasNext() && state){
				ArchivalInstitution tempAI = it.next();
				String tempInternalIdentifier = tempAI.getInternalAlId();
				if(internalIdentifiers.contains(tempInternalIdentifier)){
					state = false;
				}else{
					internalIdentifiers.add(tempInternalIdentifier);
					if(tempAI.isGroup()){
						List<ArchivalInstitution> children = tempAI.getChildArchivalInstitutions();
						if(children!=null && children.size()>0){
							Iterator<ArchivalInstitution> itChildren = children.iterator();
							while(state && itChildren.hasNext()){
								ArchivalInstitution child = itChildren.next();
								Collection<String> childrenToAppend = checkIdentifiersForArchivalInstitutionChild(child,internalIdentifiers);
								if(childrenToAppend!=null){
									internalIdentifiers.addAll(childrenToAppend);
								}else{
									return null;
								}
							}
						}
					}
				}
			}
		}
		return internalIdentifiers;
	}
	
	/**
	 * Recursive method for checkIdentifiersForArchivalInstitutionStructure.
	 * It returns null when repeated identifier is detected.
	 */
	private static Set<String> checkIdentifiersForArchivalInstitutionChild(ArchivalInstitution child,Collection<String> internalIdentifiersParents) {
		Set<String> internalIdentifiers = new HashSet<String>();
		if(child!=null){
			String tempInternalIdentifier = child.getInternalAlId();
			if(internalIdentifiersParents!=null){
				if(!internalIdentifiersParents.contains(tempInternalIdentifier)){
					internalIdentifiersParents.add(tempInternalIdentifier);
					if(child.isGroup()){
						List<ArchivalInstitution> children = child.getChildArchivalInstitutions();
						if(children!=null && children.size()>0){
							Iterator<ArchivalInstitution> it = children.iterator();
							while(it.hasNext()){
								ArchivalInstitution tempInstitution = it.next();
								Collection<String> tempIdentifiersToBeUsedInRecursiveFunction = internalIdentifiersParents;
								tempIdentifiersToBeUsedInRecursiveFunction.addAll(internalIdentifiers);
								Set<String> tempInternalIdentifiers = checkIdentifiersForArchivalInstitutionChild(tempInstitution,tempIdentifiersToBeUsedInRecursiveFunction);
								tempIdentifiersToBeUsedInRecursiveFunction = null;
								if(tempInternalIdentifiers!=null){
									internalIdentifiers.addAll(tempInternalIdentifiers);
								}else{
									return null;
								}
							}
						}
					}
				}else{
					return null;
				}
			}
		}
		return internalIdentifiers;
	}
	
	/**
	 * Method to order the child groups.
	 *
	 * @param archivalInstitution Current parent.
	 * @param initialAIToBeDeleted List of ai to be deleted.
	 * @return List of childs.
	 */
	protected static List<ArchivalInstitution> orderChildsGroups(ArchivalInstitution archivalInstitution, List<ArchivalInstitution> initialAIToBeDeleted) {
		List<ArchivalInstitution> orderedChildList = new ArrayList<ArchivalInstitution>();
		// Checks if institution is group.
		if (archivalInstitution.isGroup()) {
			List<ArchivalInstitution> childAIList = archivalInstitution.getChildArchivalInstitutions();
			if (childAIList != null && !childAIList.isEmpty()) {
				Iterator<ArchivalInstitution> childAIIt = childAIList.iterator();
				while (childAIIt.hasNext()) {
					ArchivalInstitution currentAI = childAIIt.next();
					if (currentAI.isGroup()) {
						orderedChildList.addAll(orderChildsGroups(currentAI, initialAIToBeDeleted));
						if (initialAIToBeDeleted.contains(currentAI)) {
							orderedChildList.add(currentAI);
						}
					}
					
				}
			}
		}
		
		return orderedChildList;
	}
	
	/**
	 * Method to construct the path from the current element to the root level.
	 *
	 * @param parentArchivalInstitution Element to construct the path.
	 * @return Path from the current element to root level.
	 */
	protected static String buildParentsNode(ArchivalInstitution parentArchivalInstitution) {
		StringBuffer parents = new StringBuffer();
		if(parentArchivalInstitution!=null){
			log.debug("Building hierarchy for current element: " + parentArchivalInstitution.getAiname());
			parents.append(parentArchivalInstitution.getInternalAlId());
			while(parentArchivalInstitution.getParentAiId()!=null){
				parentArchivalInstitution = parentArchivalInstitution.getParent();
				parents.append(",");
				parents.append(parentArchivalInstitution.getInternalAlId());
			}
			parents.append(",");
		}
		parents.append(SecurityContext.get().getCountryId());
		return parents.toString();
	}
	
	/**
	 * Parse a Collection<ArchivalInstitutions> currentStructure(Group1,Institution2,Group3,Institution4):
	 * 
	 * 1. - Group1
	 * 1.1 - Group1.1
	 * 1.1.1 - Institution1.1.1
	 * 2. - Institution2
	 * 3. - Group3
	 * 3.1 - Institution3.1
	 * 4. Institution4
	 * 
	 * to a plain List<ArchivalInstitution> allInstitutions:
	 * 
	 * (Group1,Group1.1,Institution1.1.1,Institution2,Group3,Institution3.1,Institution4).
	 * 
	 * @param archivalInstitutions
	 * @return List<ArchivalInstitution>
	 */
	protected static List<ArchivalInstitution> parseCollectionToPlainList(Collection<ArchivalInstitution> archivalInstitutions) {
		List<ArchivalInstitution> archivalInstitutionsPlainList = new ArrayList<ArchivalInstitution>();
		if(archivalInstitutions!=null){
			Iterator<ArchivalInstitution> it = archivalInstitutions.iterator();
			while(it.hasNext()){
				ArchivalInstitution institution = it.next();
				if(institution.isGroup()){
					log.debug("Group: "+institution.getAiname());
					if(institution.getChildArchivalInstitutions()!=null){
						archivalInstitutionsPlainList.addAll(checkChild(institution)); //call to get plain children
					}
				}else{
					log.debug("Institution: "+institution.getAiname());
				}
				institution.setParentAiId((institution.getParent()!=null)?institution.getParent().getAiId():null); //parent_ai_id fix for bad hibernate mapping
				archivalInstitutionsPlainList.add(institution);
			}
		}
		return archivalInstitutionsPlainList;
	}
	
	/**
	 * Open a file, read it and get attribute "countrycode" from
	 * eadid node.
	 * 
	 * @param httpFile
	 * @return countryCode-String
	 */
	protected static String getXMLEadidCountrycode(File httpFile){
		String response = "";
		XMLStreamReader r = null;
		try {
			XMLInputFactory factory = XMLInputFactory.newFactory();
			r = factory.createXMLStreamReader(new FileReader(httpFile));
			//start reading file
			boolean exit = false;
			boolean found = false;
			while(!exit && r.hasNext()){
				int event = r.next();
				switch (event) {
					case XMLStreamConstants.START_ELEMENT:
						//check if tag equals eadid
						if(r.getLocalName().equalsIgnoreCase(EADID)){
							//read attribute countrycode and put it into response
							int count = r.getAttributeCount();
							for(int i=0;!exit && i<count;i++){
								String attributeName = r.getAttributeLocalName(i);
								if(attributeName!=null && attributeName.equalsIgnoreCase(COUNTRYCODE)){
									response = r.getAttributeValue(i);
									exit = true;
								}
							}
						}
						break;
					case XMLStreamConstants.END_ELEMENT:
						if(found){
							exit = true;
						}
						break;
				}
			}
			//end reading file
		} catch (FileNotFoundException e) {
			log.error("File not found reading eadid :: "+httpFile.getAbsolutePath());
		} catch (XMLStreamException e) {
			log.error("Archival Landscape exception reading eadid: " + APEnetUtilities.generateThrowableLog(e));
		} catch (Exception e){
			log.error("Exception reading eadid: " + APEnetUtilities.generateThrowableLog(e));
		} finally {
			if(r!=null){
				try {
					r.close();
				} catch (XMLStreamException e) {
					log.error("Archival Landscape reading exception" + APEnetUtilities.generateThrowableLog(e));
				}
			}
		}
		return response;
	}
	
	protected static ByteArrayOutputStream buildXMlFromStructure(String countryName,Collection<ArchivalInstitution> archivalInstitutionStructure){
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//		EadCreator eadCreator = null;
		try {
//			eadCreator = new EadCreator(outputStream); //TODO, fix EadCreator before use it
			StringBuilder eadContent = new StringBuilder();
			if(countryName!=null){
				eadContent.append(openingXmlHeader());
				eadContent.append(openEadNode());
				
					eadContent.append(openEadHeader());
						eadContent.append(buildEadId());
						eadContent.append(buildFiledesc());
					eadContent.append(closeEadHeader());
					
					eadContent.append(openArchDesc());
						eadContent.append("\n\t\t\t<did>");
							eadContent.append("\n\t\t\t\t<unittitle");
							eadContent.append(" encodinganalog=\""+AL_GLOBAL_ENCODINGANALOG+"\"");
							eadContent.append(" type=\"eng\">");
							eadContent.append(AL_GLOBAL_UNITTITLE);
							eadContent.append("</unittitle>");
						eadContent.append("\n\t\t\t</did>");
						eadContent.append(openDsc());
							eadContent.append(buildCLevel(countryName,0,archivalInstitutionStructure)); //each archival institution is a C level
						eadContent.append(closeDsc());
					eadContent.append(closeArchDesc());
					
				eadContent.append(closeEadNode());
				outputStream.write(eadContent.toString().getBytes());
				eadContent = null;
			}
		} catch (Exception e){
			log.error("Unknown error into buildXMLFromDDBB" + APEnetUtilities.generateThrowableLog(e));
		}finally {
			if(outputStream!=null){
				try {
					outputStream.close();
				} catch (IOException e) {
					log.error("Unknown error into buildXMLFromDDBB" + APEnetUtilities.generateThrowableLog(e));
				}
			}
		}
		return outputStream;
	}
	
	protected static ByteArrayOutputStream buildXMlFromDDBB(String countryName) {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//		EadCreator eadCreator = null;
		try {
//			eadCreator = new EadCreator(outputStream); //TODO, fix EadCreator before use it
			StringBuilder eadContent = new StringBuilder();
			if(countryName!=null){
				eadContent.append(openingXmlHeader());
				eadContent.append(openEadNode());
				
					eadContent.append(openEadHeader());
						eadContent.append(buildEadId());
						eadContent.append(buildFiledesc());
					eadContent.append(closeEadHeader());
					
					eadContent.append(openArchDesc());
						eadContent.append("\n\t\t\t<did>");
							eadContent.append("\n\t\t\t\t<unittitle");
							eadContent.append(" encodinganalog=\""+AL_GLOBAL_ENCODINGANALOG+"\"");
							eadContent.append(" type=\"eng\">");
							eadContent.append(AL_GLOBAL_UNITTITLE);
							eadContent.append("</unittitle>");
						eadContent.append("\n\t\t\t</did>");
						eadContent.append(openDsc());
							eadContent.append(buildCLevel(countryName,0,null)); //each archival institution is a C level
						eadContent.append(closeDsc());
					eadContent.append(closeArchDesc());
					
				eadContent.append(closeEadNode());
				outputStream.write(eadContent.toString().getBytes());
				eadContent = null;
			}
		} catch (Exception e){
			log.error("Unknown error into buildXMLFromDDBB" + APEnetUtilities.generateThrowableLog(e));
		}finally {
			if(outputStream!=null){
				try {
					outputStream.close();
				} catch (IOException e) {
					log.error("Unknown error into buildXMLFromDDBB" + APEnetUtilities.generateThrowableLog(e));
				}
			}
		}
		return outputStream;
	}

	private static String openingXmlHeader() {
		return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
	}

	private static String closeArchDesc() {
		return "\n\t</archdesc>";
	}

	private static StringBuilder openArchDesc() {
		StringBuilder archDesc = new StringBuilder();
		archDesc.append("\n\t");
		archDesc.append("<archdesc");
		archDesc.append(" encodinganalog=\""+AL_GLOBAL_ENCODINGANALOG+"\"");
		archDesc.append(" level=\"fonds\"");
		archDesc.append(" relatedencoding=\""+AL_RELATEDENCODING+"\"");
		archDesc.append(" type=\""+AL_ARCHDESC_TYPE+"\">");
		return archDesc;
	}

	private static StringBuilder buildCLevel(Object parameter, int level,Collection<ArchivalInstitution> listCountryArchivalInstitutions) {
		StringBuilder cLevel = new StringBuilder();
		cLevel.append("\n");
		String tabs = "";
		for(int i=-3;i<level;i++){ //first level has 3 \t, so starts in that place
			tabs += "\t";
		}
		cLevel.append(tabs);
		if(parameter instanceof ArchivalInstitution){ //institution part, normal behavior, next cases
			ArchivalInstitution archivalInstitution = (ArchivalInstitution)parameter;
			if(archivalInstitution!=null){
				cLevel.append(buildInstitutionCLevel(archivalInstitution,tabs,level));
			}
		}else if(parameter instanceof String){ //country part, first case
			cLevel.append(buildFondsCLevel(parameter,tabs,listCountryArchivalInstitutions));
		}
		return cLevel;
	}

	private static StringBuilder buildFondsCLevel(Object parameter, String tabs,Collection<ArchivalInstitution> listCountryArchivalInstitutions) {
		StringBuilder cLevel = new StringBuilder();
		cLevel.append("<c level=\""+FONDS+"\">");
		Country country = DAOFactory.instance().getCountryDAO().getCountryByCname(parameter.toString());
		Set<CouAlternativeName> couAlternativeNames = country.getCouAlternativeNames();
		if(couAlternativeNames!=null){
			Map<String,String> alternativeNames = new HashMap<String,String>();
			Iterator<CouAlternativeName> iteratorCouAlternativeNames = couAlternativeNames.iterator();
			while(iteratorCouAlternativeNames.hasNext()){
				CouAlternativeName couAlternativeName = iteratorCouAlternativeNames.next();
				String lang = couAlternativeName.getLang().getIsoname().toLowerCase(); //TODO, parse to 3_char_isoname
				alternativeNames.put(lang,couAlternativeName.getCouAnName());
			}
			cLevel.append(buildDidNode(null,alternativeNames,tabs));
		}
		ArchivalInstitutionDAO archivalInstitutionDAO = DAOFactory.instance().getArchivalInstitutionDAO();
		if(listCountryArchivalInstitutions==null){
			listCountryArchivalInstitutions = archivalInstitutionDAO.getArchivalInstitutionsByCountryIdForAL(country.getId(),true);
		}
		if(listCountryArchivalInstitutions!=null){
			Iterator<ArchivalInstitution> itArchivalInstitutions = listCountryArchivalInstitutions.iterator();
			while(itArchivalInstitutions.hasNext()){
				cLevel.append(buildCLevel(itArchivalInstitutions.next(),2,null));
			}
		}
		cLevel.append("\n"+tabs);
		cLevel.append("</c>");
		return cLevel;
	}

	private static StringBuilder buildInstitutionCLevel(ArchivalInstitution archivalInstitution,String tabs,Integer level) {
		StringBuilder cLevel = new StringBuilder();
		cLevel.append("<c");
		if(archivalInstitution.getInternalAlId()!=null && !archivalInstitution.getInternalAlId().isEmpty()){
			cLevel.append(" id=\""+archivalInstitution.getInternalAlId()+"\"");
		}
		if(archivalInstitution.isGroup()){
			cLevel.append(" level=\""+SERIES+"\">");
		}else{
			cLevel.append(" level=\""+FILE+"\">");
		}
		Set<AiAlternativeName> aiAlternativeNames = archivalInstitution.getAiAlternativeNames();
		if(aiAlternativeNames!=null){
			Map<String,String> alternativeNames = new HashMap<String,String>();
			Map<String,String> mainAlternativeName = new HashMap<String,String>();
			Iterator<AiAlternativeName> itAlternativeNames = aiAlternativeNames.iterator();
			while(itAlternativeNames.hasNext()){
				AiAlternativeName alternativeName = itAlternativeNames.next();
				if(!alternativeName.getPrimaryName()){
					alternativeNames.put(alternativeName.getLang().getIsoname(),alternativeName.getAiAName());
				}else{
					mainAlternativeName.put(alternativeName.getLang().getIsoname(),alternativeName.getAiAName());
				}
			}
			cLevel.append(buildDidNode(alternativeNames,mainAlternativeName,tabs));
		}
		Set<ArchivalInstitution> children = new LinkedHashSet<ArchivalInstitution>(archivalInstitution.getChildArchivalInstitutions());
		if(children!=null){
			Iterator<ArchivalInstitution> itChildren = children.iterator();
			while(itChildren.hasNext()){
				cLevel.append(buildCLevel(itChildren.next(),level+1,null));
			}
		}
		//put HG link if exists
		HoldingsGuideDAO hgDao = DAOFactory.instance().getHoldingsGuideDAO();
		HoldingsGuide exampleHG = new HoldingsGuide();
		exampleHG.setAiId(archivalInstitution.getAiId());
		List<HoldingsGuide> hgList = hgDao.getHoldingsGuidesByArchivalInstitutionId(archivalInstitution.getAiId());
		if(hgList!=null && hgList.size()>0){
			Iterator<HoldingsGuide> itHg = hgList.iterator();
			while(itHg.hasNext()){
				cLevel.append(buildOtherfindaid(itHg.next(),tabs));
			}
		}
		cLevel.append("\n"+tabs);
		cLevel.append("</c>");
		return cLevel;
	}

	private static StringBuilder buildOtherfindaid(HoldingsGuide holdingsGuide,String tabs) {
		StringBuilder otherfindaid = new StringBuilder();
		if(holdingsGuide!=null){
			otherfindaid.append("\n"+tabs);
			otherfindaid.append("<otherfindaid");
			otherfindaid.append(" encodinganalog=\""+AL_GLOBAL_ENCODINGANALOG+"\"");
			otherfindaid.append(">");
			otherfindaid.append("\n"+tabs+"\t");
			otherfindaid.append("<p>");
			otherfindaid.append("\n"+tabs+"\t\t");
			otherfindaid.append("<extref");
			otherfindaid.append(" xlink:href=\""+holdingsGuide.getEadid()+"\" />");
			otherfindaid.append("\n"+tabs+"\t");
			otherfindaid.append("</p>");
			otherfindaid.append("\n"+tabs);
			otherfindaid.append("</otherfindaid>");
		}
		return otherfindaid;
	}

	private static String closeDsc() {
		return "\n\t\t</dsc>";
	}

	private static StringBuilder openDsc() {
		StringBuilder openedDsc = new StringBuilder();
		openedDsc.append("\n\t\t<dsc");
		openedDsc.append(" type=\""+AI_DSC_TYPE+"\">");
		return openedDsc;
	}

	private static StringBuilder buildDidNode(Map<String, String> alternativeNames, Map<String, String> mainAlternativeName, String tabs) {
		StringBuilder didNode = new StringBuilder();
		//first make main alternative name, which it's the institution name
		if(mainAlternativeName!=null){
			didNode.append("\n"+tabs+"\t");
			didNode.append("<did>");
			Iterator<String> keyIterator = mainAlternativeName.keySet().iterator(); //should be only one
			while(keyIterator.hasNext()){ //this should only have one unique
				String key = keyIterator.next();
				didNode.append("\n"+tabs+"\t\t");
				didNode.append("<unittitle");
				didNode.append(" encodinganalog=\""+AL_GLOBAL_ENCODINGANALOG+"\"");
				didNode.append(" type=\""+key.toLowerCase()+"\">");
				String aName = mainAlternativeName.get(key);
				//related to issue #1079
				if(aName.contains("&amp;")){
					aName = aName.replaceAll("&amp;","&");
				}
				if(aName.contains("&")){
					aName = aName.replaceAll("&","&amp;");
				}
				//escape the character <
				if(aName.contains("<")){
					aName = aName.replaceAll("<", "&lt;");
				}
				didNode.append(aName);
				didNode.append("</unittitle>");
			}
			if(alternativeNames!=null && alternativeNames.size()>0){
//				didNode.append("\n"+tabs+"\t");
//				didNode.append("<did>");
				//next make all alternative names
				Iterator<String> alternativeNamesIterator = alternativeNames.keySet().iterator();
				while(alternativeNamesIterator.hasNext()){
					String key = alternativeNamesIterator.next();
					didNode.append("\n"+tabs+"\t\t");
					didNode.append("<unittitle");
					didNode.append(" encodinganalog=\""+AL_GLOBAL_ENCODINGANALOG+"\"");
					didNode.append(" type=\""+key.toLowerCase()+"\">");
					String aName = alternativeNames.get(key);
					//same case like main alternative name, see comment over here (mainAlternativeName)
					if(aName.contains("&amp;")){
						aName = aName.replaceAll("&amp;","&");
					}
					if(aName.contains("&")){
						aName = aName.replaceAll("&","&amp;");
					}
					//escape the character <
					if(aName.contains("<")){
						aName = aName.replaceAll("<", "&lt;");
					}
					didNode.append(aName);
					didNode.append("</unittitle>");
				}
//				didNode.append("\n"+tabs+"\t");
//				didNode.append("</did>");
			}
			didNode.append("\n"+tabs+"\t");
			didNode.append("</did>");
		}
		return didNode;
	}

	private static String closeEadHeader() {
		return "\n\t</eadheader>";
	}

	private static StringBuilder buildFiledesc() {
		StringBuilder filedesc = new StringBuilder();
		filedesc.append("\n\t\t<filedesc>");
			filedesc.append("\n\t\t\t<titlestmt>");
				filedesc.append("\n\t\t\t\t<titleproper");
				filedesc.append(" encodinganalog=\""+AL_TITLEPROPER_ENCODINGANALOG+"\"");
				filedesc.append(" type=\""+AL_TITLEPROPER_TYPE+"\">");
				filedesc.append(AL_TITLEPROPER);
				filedesc.append("</titleproper>");
			filedesc.append("\n\t\t\t</titlestmt>");
		filedesc.append("\n\t\t</filedesc>");
		return filedesc;
	}

	private static StringBuilder buildEadId() {
		String countryCode = SecurityContext.get().getCountryIsoname();
		StringBuilder eadId = new StringBuilder();
		eadId.append("\n\t\t<eadid");
		eadId.append(" countrycode=\""+countryCode+"\"");
		eadId.append(" identifier=\""+AL_EADID_IDENTIFIER+"\"");
		eadId.append(" mainagencycode=\""+countryCode+AL_EADID_MAINAGENCYCODE+"\"");
		eadId.append(">");
		eadId.append(AL_EADID_PREFIX+countryCode);
		eadId.append("</eadid>");
		return eadId;
	}

	private static StringBuilder openEadHeader() {
		StringBuilder eadHeader = new StringBuilder();
		eadHeader.append("\n\t<eadheader");
		eadHeader.append(" countryencoding=\""+AL_EADHEADER_COUNTRYENCODING+"\"");
		eadHeader.append(" dateencoding=\""+AL_EADHEADER_DATAENCODING+"\"");
		eadHeader.append(" langencoding=\""+AL_EADHEADER_LANGENCODING+"\"");
		eadHeader.append(" relatedencoding=\""+AL_EADHEADER_RELATEDENCODING+"\"");
		eadHeader.append(" repositoryencoding=\""+AL_EADHEADER_REPOSITORYENCODING+"\"");
		eadHeader.append(" scriptencoding=\""+AL_EADHEADER_SCRIPTENCODING+"\"");
		eadHeader.append(">");
		return eadHeader;
	}

	private static String closeEadNode() {
		return "\n</ead>";
	}

	private static StringBuilder openEadNode() {
		StringBuilder eadNode = new StringBuilder();
		eadNode.append("<ead");
		eadNode.append(" xmlns=\""+AL_XMLNS+"\"");
		eadNode.append(" xmlns:xlink=\""+AL_XMLNS_XLINK+"\"");
		eadNode.append(" xmlns:xsi=\""+AL_XMLNS_XSI+"\"");
		eadNode.append(" audience=\""+AL_AUDIENCE+"\"");
		eadNode.append(" xsi:schemaLocation=\""+AL_XSI_SCHEMALOCATION+"\"");
		eadNode.append(">\n");
		return eadNode;
	}
	
}
