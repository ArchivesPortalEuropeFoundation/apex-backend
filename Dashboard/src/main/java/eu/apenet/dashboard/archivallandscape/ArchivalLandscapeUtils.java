package eu.apenet.dashboard.archivallandscape;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.dashboard.security.SecurityContext;
import eu.apenet.dashboard.utils.ContentUtils;
import eu.apenet.persistence.dao.AiAlternativeNameDAO;
import eu.apenet.persistence.dao.ArchivalInstitutionDAO;
import eu.apenet.persistence.dao.ContentSearchOptions;
import eu.apenet.persistence.dao.EadDAO;
import eu.apenet.persistence.dao.HoldingsGuideDAO;
import eu.apenet.persistence.dao.LangDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.AiAlternativeName;
import eu.apenet.persistence.vo.ArchivalInstitution;
import eu.apenet.persistence.vo.CouAlternativeName;
import eu.apenet.persistence.vo.Country;
import eu.apenet.persistence.vo.FindingAid;
import eu.apenet.persistence.vo.HoldingsGuide;
import eu.apenet.persistence.vo.Lang;
import eu.apenet.persistence.vo.SentMailRegister;
import eu.apenet.persistence.vo.SourceGuide;
/**
 * <p>
 * Archival-Landscape utilities class.
 * </p>
 * <p>
 * It contains all final information to build xml-nodes.
 * </p>
 */
public class ArchivalLandscapeUtils {
	//Constants used internally
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
	protected static final String UNITTITLE = "unittitle";
	private static Logger log = Logger.getLogger(ArchivalLandscapeUtils.class);
	
	 //Set for the institutions with special characters
	private static Set<String> institutionsWithSpecialCharacters;
		
	public static Set<String> getInstitutionsWithSpecialCharacters() {
		return institutionsWithSpecialCharacters;
	}

	public static void setInstitutionsWithSpecialCharacters(
			Set<String> institutionsWithSpecialCharacters) {
		ArchivalLandscapeUtils.institutionsWithSpecialCharacters = institutionsWithSpecialCharacters;
	}

	// Variables from old ArchivalLandscape class.
	private Country country;

	public List<String> Structure; //All structure in archival landscape for this partner
	public List<String> Archives; //Archival institutions for this partner

	private ArchivalInstitution ai;
	private ArchivalInstitution ai_parent;
	private ArchivalInstitutionDAO aiDao;
	private List<ArchivalInstitution> archivalInstitutionsToDelete= new ArrayList<ArchivalInstitution>();
	private List<ArchivalInstitution> archivalInstitutionsToInsert= new ArrayList<ArchivalInstitution>();
	private List<AiAlternativeName> archivalInstitutionsNameNotChanged= new ArrayList<AiAlternativeName>();
	private List<AiAlternativeName> archivalInstitutionsNameChanged= new ArrayList<AiAlternativeName>();
	private List<ArchivalInstitution> archivalInstitutionsParentNotChanged= new ArrayList<ArchivalInstitution>();
	private List<ArchivalInstitution> archivalInstitutionsParentChanged= new ArrayList<ArchivalInstitution>();
	private List<SentMailRegister> sentMailRegisterList = new ArrayList<SentMailRegister>();

	// Set for the duplicate identifiers.
	private static Set<String> duplicateIdentifiers;
	// End of variables from old ArchivalLandscape class.

	// Constructor.
	public ArchivalLandscapeUtils () {
		SecurityContext securityContext = SecurityContext.get();
		this.country = DAOFactory.instance().getCountryDAO().findById(securityContext.getCountryId());
	}

	// Setters and getters from old ArchivalLandscape class.
	//Returns the ISO name of the country
	public String getmyCountry() {
		if (this.country == null) {
			SecurityContext securityContext = SecurityContext.get();
			this.country = DAOFactory.instance().getCountryDAO().findById(securityContext.getCountryId());
		}
		return this.country.getIsoname();
	}

	// Returns the ID of the country.
	public int getCountryId() {
		return this.country.getId();
	}

	public List<ArchivalInstitution> getArchivalInstitutionsToDelete() {
		return archivalInstitutionsToDelete;
	}

	public void setArchivalInstitutionsToDelete(
		List<ArchivalInstitution> archivalInstitutionsToDelete) {
		this.archivalInstitutionsToDelete = archivalInstitutionsToDelete;
	}

	public List<ArchivalInstitution> getArchivalInstitutionsToInsert() {
		return archivalInstitutionsToInsert;
	}

	public void setArchivalInstitutionsToInsert(
		List<ArchivalInstitution> archivalInstitutionsToInsert) {
		this.archivalInstitutionsToInsert = archivalInstitutionsToInsert;
	}

	public List<SentMailRegister> getSentMailRegisterList() {
		return sentMailRegisterList;
	}
	
	public void setSentMailRegisterList(List<SentMailRegister> sentMailRegisterList) {
		this.sentMailRegisterList = sentMailRegisterList;
	}

	public List<AiAlternativeName> getArchivalInstitutionsNameNotChanged() {
		return archivalInstitutionsNameNotChanged;
	}

	public void setArchivalInstitutionsNameNotChanged(
		List<AiAlternativeName> archivalInstitutionsNameNotChanged) {
		this.archivalInstitutionsNameNotChanged = archivalInstitutionsNameNotChanged;
	}

	public List<AiAlternativeName> getArchivalInstitutionsNameChanged() {
		return archivalInstitutionsNameChanged;
	}

	public void setArchivalInstitutionsNameChanged(
		List<AiAlternativeName> archivalInstitutionsNameChanged) {
		this.archivalInstitutionsNameChanged = archivalInstitutionsNameChanged;
	}

	public List<ArchivalInstitution> getArchivalInstitutionsParentNotChanged() {
		return archivalInstitutionsParentNotChanged;
	}

	public void setArchivalInstitutionsParentNotChanged(
		List<ArchivalInstitution> archivalInstitutionsParentNotChanged) {
		this.archivalInstitutionsParentNotChanged = archivalInstitutionsParentNotChanged;
	}

	public List<ArchivalInstitution> getArchivalInstitutionsParentChanged() {
		return archivalInstitutionsParentChanged;
	}

	public void setArchivalInstitutionsParentChanged(
		List<ArchivalInstitution> archivalInstitutionsParentChanged) {
		this.archivalInstitutionsParentChanged = archivalInstitutionsParentChanged;
	}

	public static Set<String> getDuplicateIdentifiers() {
		return ArchivalLandscapeUtils.duplicateIdentifiers;
	}

	public static void addDuplicateIdentifiers(String duplicateIdentifier) {
		if (ArchivalLandscapeUtils.getDuplicateIdentifiers() == null) {
			ArchivalLandscapeUtils.duplicateIdentifiers = new LinkedHashSet<String>();
		}

		ArchivalLandscapeUtils.getDuplicateIdentifiers().add(duplicateIdentifier);
	}

	public static void setDuplicateIdentifiers(Set<String> duplicateIdentifiers) {
		ArchivalLandscapeUtils.duplicateIdentifiers = duplicateIdentifiers;
	}
	// END of setters and getters from old ArchivalLandscape class.
	
	/**
	 * <p>
	 * Method which returns the path of the AL of each country.
	 * </p>
	 * @param country - String country
	 * @return String - target path
	 */
	public String getmyPath(String country) {
		String path = APEnetUtilities.getConfig().getRepoDirPath() + File.separatorChar;
		path = path + country + File.separatorChar + "AL" + File.separatorChar;

		File file=new File(path);
		if(!file.exists())
		file.mkdir();

		return path;
	}
	
	/**
	 * <p>
	 * Append to a global Set an institution with special characters (param -> identifier)
	 * </p>
	 * @param institutionsWithSpecialCharacters - String institution
	 */
	public static void addInstitutionsWithSpecialCharacters(String institutionsWithSpecialCharacters) {
		if (ArchivalLandscapeUtils.getInstitutionsWithSpecialCharacters() == null) {
			ArchivalLandscapeUtils.institutionsWithSpecialCharacters = new LinkedHashSet<String>();
		}

		ArchivalLandscapeUtils.getInstitutionsWithSpecialCharacters().add(institutionsWithSpecialCharacters);
	}

	/**
	 * <p>
	 * Method which check if two institution has the same father and returns it.
	 * </p>
	 * @param ingestedInstitution -  {@link ArchivalInstitution} compare parameter 1
	 * @param archivalInstitution -  {@link ArchivalInstitution} compare parameter 2
	 * @return boolean -> same parent
	 */
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
	 * <p>
	 * Seek an institution into {@link ArchivalInstitution} structure.
	 * </p>
	 * <p>
	 * The seek institution comes from an AL file.
	 * </p>
	 * @param ingestedInstitution - {@link ArchivalInstitution} seeker
	 * @param archivalInstitutions - Collection<{@link ArchivalInstitution}> source
	 * @return {@link ArchivalInstitution}.TARGET / null if not found
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
	
	/**
	 * <p>
	 * Method which loop into an structured Collection<{@link ArchivalInstitution}> and returns
	 * in a new Collection<{@link ArchivalInstitution}> institutions which contains an empty 
	 * internal-identifier. 
	 * </p>
	 * @param archivalInstitutions - Collection<{@link ArchivalInstitution}> source
	 * @return Collection<{@link ArchivalInstitution}> -> detectedArchivalInstitutions / null 
	 */
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
	/**
	 * <p>
	 * Method which loop into an structured Collection<{@link ArchivalInstitution}> and returns
	 * in a new Collection<{@link ArchivalInstitution}> institutions which contains the same identifier.
	 * </p>
	 * @param archivalInstitutions - Collection<{@link ArchivalInstitution}> source
	 * @return Map<String,List<{@link ArchivalInstitution}>> -> allRepeatedInstitutions / null
	 */
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

	/**
	 * <p>
	 * Recursive method which loop into an structured Collection<{@link ArchivalInstitution}> and returns
	 * in a new Collection<{@link ArchivalInstitution}> institutions which contains the same identifier.
	 * </p>
	 * @param archivalInstitutions - Collection<{@link ArchivalInstitution}> source
	 * @param allRepeatedInstitutions - Map<String,List<{@link ArchivalInstitution}>> storage
	 * @return Map<String,List<{@link ArchivalInstitution}>> -> allRepeatedInstitutions
	 */
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
	 * <p>
	 * Method to check and returns, the list of institutions that has the same
	 * name in the collection passed.
	 * </p>
	 * @param archivalInstitutions - Collection<{@link ArchivalInstitution}> source to check
	 * @return Map<String, List<{@link ArchivalInstitution}>> -> The list of related institutions with same name / null
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
	 * <p>
	 * Method to obtain the institutions listed under same name.
	 * </p>
	 * @param archivalInstitutions - Collection<{@link ArchivalInstitution}> institutions and/or groups to check (source)
	 * @param allRepeatedInstitutions - Map<String,List<{@link ArchivalInstitution}>> institutions and/or groups checked (storage)
	 *
	 * @return Map<String,List<{@link ArchivalInstitution}>> -> institutions with keys which have the same name, key is aiName
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
	 * <p>
	 * Parse an archivalInstitution structure to a plain internal identifiers
	 * </p>
	 * @param archivalInstitutions - Collection<{@link ArchivalInstitution}> source
	 * @return List<String> internalIdentifiers -> plain identifiers list
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

	/**
	 * <p>
	 * Method which tries to search in an structure (param) containing ArchivalInstitutions 
	 * and returns if it's found an institution with the target name (param).
	 * </p>
	 * @param emptyIdentifierNamed - String name (seeker)
	 * @param archivalInstitutionList - Collection<{@link ArchivalInstitution}> source
	 * @return {@link ArchivalInstitution} -> target extracted institution / null 
	 */
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
	 * <p>
	 * Recovers the list of institutions with same name from the collection
	 * passed.
	 * </p>
	 * @param institutionName - String name of the institution to search (seeker)
	 * @param archivalInstitutionList - Collection<{@link ArchivalInstitution}> of institutions in which the search will be performed.
	 *
	 * @return List<{@link ArchivalInstitution}> -> institutions with same name into the collection
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
	 * <p>
	 * Recovers the institution with the internal identifier from the
	 * collection passed.
	 * </p>
	 * @param internalIdentifier - String internal identifier of the institution to search.
	 * @param archivalInstitutionList - Collection<{@link ArchivalInstitution}> of institutions in which the search will be performed.
	 *
	 * @return {@link ArchivalInstitution} -> Target institution with the internal identifier in the collection.
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
	 * <p>
	 * Replaces a node from an archivalInstitution List with the new.
	 * </p>
	 * <p>
	 * Uses oldArchivalInstitution to set a target to be replaced (by newArchivalInstitution).
	 * </p>
	 * <p>
	 * (Only it's replaced when internal identifier match or if internal identifier of
	 * old institution is empty the ainame match).
	 * </p>
	 * 
	 * @param oldInstitution - {@link ArchivalInstitution} target
	 * @param newArchivalInstitution - {@link ArchivalInstitution} destination
	 * @param archivalInstitutionList - Collection<{@link ArchivalInstitution}> source
	 * @return ArrayList<{@link ArchivalInstitution}> -> Structure with target replacement
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
	 * <p>
	 * Replace an institution by other institution with the same identifier and the same name
	 * into a structure.
	 * </p>
	 * <p>
	 * It's necessary both conditions are true.
	 * </p>
	 *  
	 * @param oldInstitution - {@link ArchivalInstitution} target
	 * @param newInstitution - {@link ArchivalInstitution} destination
	 * @param archivalInstitutionList - Collection<{@link ArchivalInstitution}> source
	 * @return ArrayList<{@link ArchivalInstitution}> -> changed institutions
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
	 * <p>
	 * Called into parseCollectionToPlainList method.
	 * </p>
	 * <p>
	 * Functionality is very similar, but the different is the argument parent.
	 * </p>
	 * <p>
	 * Parent is used to get all children and call to himself by recursive-way if needed.
	 * </p>
	 * 
	 * @param parent - {@link ArchivalInstitution} parent institution
	 * @return List<{@link ArchivalInstitution}> -> plainArchivalInstitutions list
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
	
	/**
	 * <p>
	 * Method which discriminate from an Collection<{@link ArchivalInstitution}> institutions (param) 
	 * which are contained into a List<String> identifiers (param).
	 * </p>
	 * 
	 * @param newArchivalInstitutionStructure - Collection<{@link ArchivalInstitution}> seeker
	 * @param ingestedIdentifiers - List<String> source
	 * @return List<{@link ArchivalInstitution}> -> excludedInstitutions list
	 */
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
	 * <p>
	 * Checks if some identifier is repeated.
	 * </p>
	 * <p>
	 * It's used before do anything, and necessary for checks and 
	 * discriminate states with archival institution edition.
	 * </p>
	 * 
	 * @param archivalInstitutions - Collection<{@link ArchivalInstitution}> archivalInstitutions 
	 * @return HashSet<String> -> internalIdentifiers set
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
	 * <p>
	 * Recursive method for checkIdentifiersForArchivalInstitutionStructure.
	 * </p>
	 * <p>
	 * If archival institution node has archival institution children it call recursively itself. 
	 * </p>
	 * <p>
	 * It returns null when repeated identifier is detected.
	 * </p>
	 * @param child - {@link ArchivalInstitution} child node
	 * @param internalIdentifiersParents - Collection<String> list of parents identifiers
	 * @return HashSet<String> -> internalIdentifiers set / null
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
	 * <p>
	 * Method to order the child groups.
	 * </p>
	 * <p>
	 * If an archival institution has children it call recursively itself.
	 * </p>
	 *
	 * @param archivalInstitution - Current parent.
	 * @param initialAIToBeDeleted - List of ai to be deleted.
	 * @return List<{@link ArchivalInstitution}> -> Children sorted list
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
	 * <p>
	 * Method to construct the path from the current element to the root level.
	 * </p>
	 * <p>
	 * It builds his parent structure using internalALId() of each parent, and write it into StringBuilder
	 * </p>
	 * <p>
	 * Returns the content of StringBuilder converted by StringBuilder.toString().
	 * </p>
	 * @param parentArchivalInstitution Element to construct the path.
	 * @return String -> path from the current element to root level.
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
	 * <p>
	 * Parse a Collection<{@link ArchivalInstitutions}> currentStructure(Group1,Institution2,Group3,Institution4):
	 * </p>
	 * <p>
	 * It differences between group and institutions, using the .isGroup() and .getParent().getAiId() methods.
	 * This last step is necessary for fix a bad configuration of Hibernate annotations (in this case related to parent_ai_id) 
	 * </p>
	 * <p>
	 * The results are built in this kind of structure:
	 * </p>
	 * <p>
	 * Source:
	 * </p>
	 * <ul>
	 * <li>1. - Group1 </li>
	 * <li>1.1 - Group1.1</li>
	 * <li>1.1.1 - Institution1.1.1</li>
	 * <li>2. - Institution2</li>
	 * <li>3. - Group3</li>
	 * <li>3.1 - Institution3.1</li>
	 * <li>4. Institution4</li>
	 * </ul>
	 * <p>
	 * This structure is converted to a plain List<{@link ArchivalInstitution}> (allInstitutions):
	 * </p>
	 * <p>
	 * Results values for this example structure:
	 * </p>
	 * <p>
	 * (Group1,Group1.1,Institution1.1.1,Institution2,Group3,Institution3.1,Institution4).
	 * </p>
	 * @param archivalInstitutions - Collection<{@link ArchivalInstitution}>
	 * @return ArrayList<{@link ArchivalInstitution}> -> archivalInstitutionsPlainList
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
	 * <p>
	 * Open a file, read it and get attribute "countrycode" from
	 * eadid node.
	 * </p>
	 * <p>
	 * It uses a SAX logic to extract from <eadid> element the 
	 * 'countrycode' attribute content.
	 * </p>
	 * <p>
	 * This method don't check syntax of any results, simply 
	 * get the content seek. 
	 * </p>
	 * @param httpFile - File source
	 * @return String -> countryCode extracted from File
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
	
	/**
	 * <p>
	 * Method which is used to build an xml reading a Collection<{@link ArchivalInstitution}>.
	 * </p>
	 * <p>
	 * It returns an OutputStream (ByteArrayOutputStream) with the xml nodes written.
	 * </p>
	 * @param countryName - String name of country
	 * @param archivalInstitutionStructure - Collection<{@link ArchivalInstitution}>
	 * @return ByteArrayOutputStream -> StringBuilder written into ByteArrayOutputStream
	 */
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
	
	/**
	 * <p>
	 * Method which build the entire xml using the countryName (param).
	 * </p>
	 * <p>
	 * It calls to each xml open/close method for each node.
	 * XML file nodes are ead, eadheader, eadid, filedesc, archdesc,
	 * did, unittitle and dsc with all c levels.
	 * </p>
	 * <p>
	 * It returns a ByteArrayOutputStream with a complete xml written.
	 * </p>
	 * 
	 * @param countryName - String formateed country name to be pushed into c levels
	 * @return ByteArrayOutputStream -> StringBuilder written into ByteArrayOutputStream
	 */
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

	/**
	 * <p>
	 * Method which returns the starting-head of a XML (version 1.0)
	 * with encoding "UTF-8".
	 * </p>
	 * 
	 * @return String
	 */
	private static String openingXmlHeader() {
		return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
	}

	/**
	 * <p>
	 * Method which returns the archdesc node close tag
	 * with all indents.
	 * </p>
	 * 
	 * @return String
	 */
	private static String closeArchDesc() {
		return "\n\t</archdesc>";
	}

	/**
	 * <p>
	 * Method which builds the starting tag of archdesc node.
	 * It contains the encodinganalog, level, relatedencoding and type.
	 * </p>
	 * @return StringBuilder -> it has writen the archdesc node data
	 */
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
	
	/**
	 * <p>
	 * This method builds a c-level.
	 * </p>
	 * <p>
	 * This method is used recursively, and depends on the params (Object parameter) 
	 * builds and institutionCLevel (instanceof {@link ArchivalInstitution}) or 
	 * buildsFondsCLevel (instanceof {@link ArchivalInstitution}), it calls to eachs method depends 
	 * on the case.
	 * </p>
	 * <p>
	 * It returns an StringBuilder with the XML content (Archival-Landscape).
	 * </p> 
	 * 
	 * @param parameter - Object which could be instanceof String or {@link ArchivalInstitution}
	 * @param level - integer huge level
	 * @param listCountryArchivalInstitutions - Collection<{@link ArchivalInstitution}> target
	 * @return StringBuilder - c level written into StringBuilder
	 */
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
	/**
	 * <p>
	 * This method builds a c-level fond.
	 * </p>
	 * <p>
	 * It uses an object param - CName of a Country
	 * </p>
	 * <p>
	 * It builds a did node and if necessary a c-level child/children
	 * </p>
	 * @param parameter - Object which could be instanceof String or {@link ArchivalInstitution}
	 * @param tabs - String tab indent by level
	 * @param listCountryArchivalInstitutions - Collection<{@link ArchivalInstitution}>
	 * @return StringBuilder - c level fond written into StringBuilder
	 */
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
	/**
	 * <p>
	 * This method builds into StringBuilder a c-level (only SERIES and FILES)
	 * </p>
	 * @param archivalInstitution - {@link ArchivalInstitution} source
	 * @param tabs - String tab indent by level
	 * @param level - Integer huge level
	 * @return StringBuilder -> c_level written
	 */
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
	/**
	 * <p>
	 * This method builds <otherfindaid> node with chis default values.
	 * </p>
	 * <p>
	 * This node is indent by his level figure.
	 * <p>
	 * @param holdingsGuide  {@link HoldingsGuide}
	 * @param tabs - String tab indent by level
	 * @return StringBuilder -> otherfindaid node written
	 */
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
	/**
	 * <p>
	 * This method returns the close tag for <dsc> node.
	 * </p>
	 * @return String
	 */
	private static String closeDsc() {
		return "\n\t\t</dsc>";
	}
	/**
	 * <p>
	 * This method builds the opening tag for <dsc> node.
	 * </p>
	 * @return StringBuilder -> openedDsc node
	 */
	private static StringBuilder openDsc() {
		StringBuilder openedDsc = new StringBuilder();
		openedDsc.append("\n\t\t<dsc");
		openedDsc.append(" type=\""+AI_DSC_TYPE+"\">");
		return openedDsc;
	}
	/**
	 * <p>
	 * This method builds a did node with default values.
	 * </p>
	 * <p>
	 * Also it uses alternative names to make <unittitle> nodes.
	 * </p>
	 * @param alternativeNames - Map<String, String> source alternative names, this is used depending on the huge level, it could be null
	 * @param mainAlternativeName - Map<String, String> source alternative names, this is used depending on the huge level, it must be not null
	 * @param tabs - String tab indent by level
	 * @return StringBuilder -> did node written
	 */
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
	/**
	 * <p>
	 * This method builds the close tag for <eadheader> node.
	 * </p>
	 * @return String
	 */
	private static String closeEadHeader() {
		return "\n\t</eadheader>";
	}
	/**
	 * <p>
	 * This method builds a <filedesc> node with his defaults values.
	 * </p>
	 * @return StringBuilder -> filedesc node written
	 */
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
	/**
	 * <p>
	 * This method builds a default <eadid> complete 
	 * node with his country code.
	 * </p>
	 * <p>
	 * The current country code is extracted 
	 * from session user information.
	 * </p>
	 * @return StringBuilder -> eadid node written
	 */
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
	/**
	 * <p>
	 * This method builds default <eadheader> node opening tag.
	 * </p>
	 * @return StringBuilder -> eadheader node written
	 */
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
	/**
	 * <p>
	 * This method builds a close <ead> node tag.
	 * </p>
	 * @return String - closing tag </ead> with line break
	 */
	private static String closeEadNode() {
		return "\n</ead>";
	}
	/**
	 * <p>
	 * This method builds an <ead> node tag with default values.
	 * </p>
	 * @return StringBuilder -> ead node written
	 */
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


	/**
	 * <p>
	 * This methods from old ArchivalLandscape class.
	 * </p>
	 * <p>
	 * It checks if an institution has alternatives name
	 * in a node in archival-landscape.
	 * </p>
	 * <p>
	 * Uses special rules to generate corrections into alternative_names.
	 * See
	 * </p>
	 * <p>
	 * If necessary updates an ArchivalInstitution related data into DDBB.
	 * </p>
	 * @param arch_inst - {@link ArchivalInstitution} used into DDBB to extract alternative_nameS
	 * @param archivalLandscapeNode - {@link ArchivalLandscapeNode}, see ArchivalLandscapeStructure for more details
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void checkAlternativeNames(ArchivalInstitution arch_inst, ArchivalLandscapeNode archivalLandscapeNode){
		
		//Check if the languages considered for this item (group or institution) are stored in database
		Boolean found= null;
		AiAlternativeNameDAO ai_alNameDAO = DAOFactory.instance().getAiAlternativeNameDAO(); 
		List<AiAlternativeName> aiNames = ai_alNameDAO.findByAIId(arch_inst); //Alternative names stored in database
		Map<String, String> namesToUpload = new HashMap<String, String>(); 
		Iterator iterator = archivalLandscapeNode.getNames().entrySet().iterator(); //Alternative names in the new AL
		Iterator iterator2 = archivalLandscapeNode.getPrimaryName().entrySet().iterator(); //Alternative names in the new AL with primary info
        while(iterator.hasNext()){
        	found = false;
        	Map.Entry<String, String> temp = (Entry<String, String>) iterator.next();
        	Map.Entry<String, Boolean> primaryNames = null;
        	if (iterator2.hasNext())
        		primaryNames = (Entry<String, Boolean>) iterator2.next();
        	
        	for (int m=0;m<aiNames.size();m++)
        	{
        		//The language is the same
        		if (aiNames.get(m).getLang().getIsoname().toLowerCase().equals(temp.getKey().toLowerCase()))
        		{
        			found = true;
        			//But the name is NOT the same, we update it 
        			if (!aiNames.get(m).getAiAName().equals(temp.getValue()))
        			{
        				if (primaryNames!= null)
        				{
        					Boolean toChange= false;
        					//If it's NOT the primary name (therefore this name is not indexed), the alternative name will be updated 
        					if (!primaryNames.getValue())
     						{
        						toChange = true;        						
     						}
        					//If it's the primary name, we have to check if this item is indexed somewhere 
        					else{
        						// If yes, update the list to inform the user that the name can't be changed
        						boolean containsPublishedFiles = ContentUtils.containsPublishedFiles(arch_inst);
        						if (containsPublishedFiles)
        							this.getArchivalInstitutionsNameNotChanged().add(aiNames.get(m));        						
        						//Change the name in archival institution name too.
        						else
        						{
        							toChange=true;
        							ArchivalInstitutionDAO aiDao = DAOFactory.instance().getArchivalInstitutionDAO();
        							arch_inst.setAiname(temp.getValue());
        							aiDao.updateSimple(arch_inst);
        							this.getArchivalInstitutionsNameChanged().add(aiNames.get(m));
        						}	
        					}
        					if (toChange)
        					{
        						aiNames.get(m).setAiAName(temp.getValue());
                				AiAlternativeName ai_altName = new AiAlternativeName();
                				ai_altName = ai_alNameDAO.findById(aiNames.get(m).getAiAnId());
                				ai_altName.setAiAName(temp.getValue());
                				ai_alNameDAO.updateSimple(ai_altName);
        					}
        				}
        			}
        			aiNames.remove(m);
        		}
        		//The language is not the same
        		else
        		{
        			//But the name is the same, we change the language
        			if (aiNames.get(m).getAiAName().equals(temp.getValue()))
        			{       
        				found = true;
        				List<AiAlternativeName> ai_altName = new ArrayList<AiAlternativeName>();		            	
        	        	ai_altName = ai_alNameDAO.findByAiAName(aiNames.get(m).getAiAName().toString());
        	        	
        	        	for (int j=0;j<ai_altName.size();j++)
        	        	{
        	        		if (ai_altName.get(j).getArchivalInstitution().getAiId() == arch_inst.getAiId())
        	        		{
        	        			LangDAO langDao = DAOFactory.instance().getLangDAO();
                	        	Lang lang = new Lang();		            	
                	        	lang = langDao.getLangByIsoname(temp.getKey());
        	        			ai_altName.get(j).setLang(lang);        	        			
        	        			log.debug("Updating in database the language of the alternative name "+ temp.getValue() +" of the institution "+ arch_inst.getAiname());
        	                	ai_alNameDAO.updateSimple(ai_altName.get(j));
        	        		}
        	        	}
        	        	aiNames.remove(m);
        			}
        		}
        	}
        	if (!found)
        		namesToUpload.put(temp.getKey(), temp.getValue());        	
        }
        iterator = namesToUpload.entrySet().iterator();
        //All the languages that are still in the file list must be stored in database because they are new ones
        while (iterator.hasNext())
        {        	
			Map.Entry<String, String> temp = (Entry<String, String>) iterator.next();
        	        	
        	AiAlternativeName ai_altName = new AiAlternativeName();		            	
        	ai_altName.setAiAName(temp.getValue());		            	
        	ai_altName.setArchivalInstitution(arch_inst);
        	LangDAO langDao = DAOFactory.instance().getLangDAO();
        	Lang lang = new Lang();		            	
        	lang = langDao.getLangByIsoname(temp.getKey());
        	if (lang == null)
        	{
        		log.error("The lang " + temp.getKey() + " does not exist");
        		log.error("It was not possible to upload the alternative name: " + temp.getValue() + " of the institution: " + arch_inst);
        	}
        	else
        	{
        		ai_altName.setLang(lang);
        		//At this time, the new alternative names are not primary ones, so, false.
        		ai_altName.setPrimaryName(false);
        		log.debug("Storing in database the alternative name "+ temp.getValue() +" of the institution "+ arch_inst);
        		ai_alNameDAO.insertSimple(ai_altName);
        	}
        }
       //All the languages that are still in the database list must be deleted in database because they are old ones
        for (int i=0;i<aiNames.size();i++)        	
        {
        	log.debug("Deleting from database the alternative name "+ aiNames.get(i).getAiAName()+" of the institution "+ arch_inst);
        	ai_alNameDAO.deleteSimple(aiNames.get(i));
        }
	}

	/**
	 * <p>
	 * This method checks if a file contains unique identifiers.
	 * </p> 
	 * @param file - File, source file
	 * @return boolean -> changed / null
	 */
	public static Boolean checkIdentifiers(File file){
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(file);
			doc.normalizeDocument();
			doc.normalize();
			NodeList listTemp = doc.getElementsByTagName("c");
			boolean changes = false;
			List<String> identifiers = new ArrayList<String>(); //list of unique identifiers
			// Delete the content of "DuplicateIdentifiers".
			ArchivalLandscapeUtils.setDuplicateIdentifiers(null);
			for(int i=0;i<listTemp.getLength();i++){
				Element cTemp = (Element)listTemp.item(i);
				if(cTemp.getAttributes()!=null && cTemp.getAttributes().getNamedItem("id")==null){
					String identifier = generateNewRandomIdentifier();
					cTemp.setAttribute("id",identifier); //This identifier can be repeated
					changes = true;
				}else if(cTemp.getAttributes()!=null && cTemp.getAttributes().getNamedItem("id")!=null){
					String identifier = cTemp.getAttributes().getNamedItem("id").getNodeValue();
					if(identifiers.contains(identifier)){ //internal identifier is repeated?
						// Add the repeated identifiers to the set.
						ArchivalLandscapeUtils.addDuplicateIdentifiers(identifier);
					}else if(!isValidIdentifier(identifier)){ //is a valid identifier?
						// return a new state for personalized layout
						return null;
					}
					identifiers.add(identifier);
				}
			}

			if (ArchivalLandscapeUtils.duplicateIdentifiers != null
					&& !ArchivalLandscapeUtils.duplicateIdentifiers.isEmpty()) {
				return false;
			}

			if(changes){
				TransformerFactory tf = TransformerFactory.newInstance();
		        Transformer transformer = tf.newTransformer();
		        transformer.transform(new DOMSource(doc), new StreamResult(file));
			}
		} catch (Exception e) {
			log.error("Checking c identifiers in makeTemporal():"+  APEnetUtilities.generateThrowableLog(e));
		}

		return true;
	}
	/**
	 * <p>
	 * This method generates a random identifier for archival_institutions.
	 * </p>
	 * <p>
	 * It always starts with 'A' and uses current time plus '-' 
	 * character and a random float number.
	 * </p>
	 * @return String - patterned random id
	 */
	private static String generateNewRandomIdentifier() {
		return "A"+System.currentTimeMillis()+"-"+(new Float(+Math.random()*1000000).toString());
	}
	/**
	 * <p>
	 * This method checks if identifier is given in the unique 
	 * param could be generated or it's valid.
	 * </p>
	 * <p>
	 * It checks if this identifier has content and 
	 * his content starts with a letter.
	 * </p>
	 * @param identifier - String identifier
	 * @return boolean - valid
	 */
	public static boolean isValidIdentifier(String identifier) {
		if(identifier.length()>0){
			char firstCharacter = identifier.charAt(0);
			if(Character.isLetter(firstCharacter)){
				return true;
			}
		}
		return false;
	}

	/**
	 * <p>
	 * Method which stores in the database the name of the 'new' archival 
	 * institutions uploaded and delete the ones 'removed'.
	 * </p>
	 * <p>
	 * It uses an InputSource (extracted from a Reader) to parse
	 * file data to a DOM document.
	 * </p>
	 * <p>
	 * DOM Document is used to extract c levels information.
	 * </p>
	 * <p>
	 * See {@link ArchivalLandscapeStructure} documentation for more details.
	 * </p>
	 * @param file -> File, used to get stream and a DOM document
	 * @param execute -> boolean, flag to launch a delete action (see ContentUtils.deleteArchivalInstitution() documentation for more details)
	 * @return String -> state control string
	 */
	public String storeArchives(File file, boolean execute) {
		
		String result = "success";
				
		//Get archival institution by this partner
		LangDAO langDao = DAOFactory.instance().getLangDAO();
		Lang lang = null;		
		this.ai_parent = new ArchivalInstitution();
		this.aiDao = DAOFactory.instance().getArchivalInstitutionDAO();
		List <ArchivalInstitution> ais = this.aiDao.getGroupsAndArchivalInstitutionsByCountryId(this.country.getId(), "alorder", false);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder;
        
        log.debug("Storing in database the institutions of the country " + this.getmyCountry() + "...");
           
        try {
    		
			dBuilder = dbFactory.newDocumentBuilder();
			Reader reader = new InputStreamReader(new FileInputStream(file),"UTF-8");
			InputSource sfile = new InputSource(reader);
	        Document doc = dBuilder.parse(sfile);
	        doc.getDocumentElement().normalize();
	        
	        //Get the whole structure of the archival landscape
            NodeList a = doc.getElementsByTagName("c");            
            ArchivalLandscapeStructure al = new ArchivalLandscapeStructure();
            List <ArchivalLandscapeNode> alNodes = al.archivalLandscapeStructure(a);
            
            for (int h = 0; h < alNodes.size(); h++) {
                ArchivalLandscapeNode archivalLandscapeNode = alNodes.get(h);
	            this.ai = new ArchivalInstitution();
		        if (ais.size()==0) { //There's no (or no more) archival institutions for this partner
                    for (Entry<String, Boolean> item : archivalLandscapeNode.getPrimaryName().entrySet()) {
                        if (item.getValue())
                            this.ai.setAiname(archivalLandscapeNode.getNames().get(item.getKey()));
                    }

		            this.ai.setGroup(archivalLandscapeNode.getIs_group());
		            
		            if (archivalLandscapeNode.getParent_name()== null)
		            	this.ai.setParent(null);
		            else {
		            	ai_parent = aiDao.getArchivalInstitutionsByCountryIdandAlIdentifier(this.country.getId(), archivalLandscapeNode.getParent_internal_al_id());
		            	this.ai.setParent(ai_parent);
		            }
		            this.ai.setCountryId(this.country.getId());
		            this.ai.setCountry(this.country);
		            this.ai.setAlorder(archivalLandscapeNode.getNodeId());
		            this.ai.setInternalAlId(archivalLandscapeNode.getInternal_al_id());
		            //StoreAI
		            log.debug("Storing in database the institution "+ this.ai.getAiname());
		            this.aiDao.insertSimple(this.ai);
		            ContentUtils.updateContainsSearchableItemsInAiGroups(this.ai);
		            this.archivalInstitutionsToInsert.add(this.ai);
		            
		            //Store the alternative names of the archival institution
                    for (Entry<String, String> temp : archivalLandscapeNode.getNames().entrySet()) {
                        AiAlternativeNameDAO ai_alNameDAO = DAOFactory.instance().getAiAlternativeNameDAO();
                        AiAlternativeName ai_altName = new AiAlternativeName();
                        ai_altName.setAiAName(temp.getValue());
                        ai_altName.setArchivalInstitution(this.ai);

                        if(lang == null || !lang.getIsoname().equalsIgnoreCase(temp.getKey()))
                            lang = langDao.getLangByIsoname(temp.getKey());

                        ai_altName.setLang(lang);
                        if (archivalLandscapeNode.getPrimaryName().get(temp.getKey().toLowerCase()) == null)
                            ai_altName.setPrimaryName(false);
                        else
                            ai_altName.setPrimaryName(true);
                        log.debug("Storing in database the alternative name " + temp.getValue() + " of the institution " + this.ai.getAiname());
                        ai_alNameDAO.insertSimple(ai_altName);
                    }
		        } else {//There are already archival institutions for this partner
		        	Boolean upload=true;
		        	for (int i= 0; i< ais.size();i++) {
                        ArchivalInstitution archivalInstitution = ais.get(i);
		         		//If the institution already exists, don't store in database
		            	if (archivalLandscapeNode.getInternal_al_id().equals(archivalInstitution.getInternalAlId())) {
		            		this.ai = archivalInstitution;
		            		//If the item stored in database has the same parent in the archival landscape, check the languages
		            		if ((archivalLandscapeNode.getParent_internal_al_id()== null) && (archivalInstitution.getParent() ==null))
		            		{
		            			checkAlternativeNames(archivalInstitution, archivalLandscapeNode);
		            		}
		            		else if  ((archivalLandscapeNode.getParent_internal_al_id()!= null) && (archivalInstitution.getParent() != null))
		            		{
				            	if ((archivalLandscapeNode.getParent_internal_al_id().equals(archivalInstitution.getParent().getInternalAlId())))
				            	{
				            		checkAlternativeNames(archivalInstitution, archivalLandscapeNode);
				            	}
				            	//If not, change the parent of the item for the new one
				            	else 
				            	{
//				            		//Check if the item is in the index
//				            		Long numFilesIndexed = EditArchivalLandscapeLogic.countIndexedContentByInstitutionGroupId(archivalInstitution.getInternalAlId(), archivalInstitution.isGroup());
//	        						if ((numFilesIndexed != null) && (numFilesIndexed > 0))
//	        						{
//	        							this.getArchivalInstitutionsParentNotChanged().add(ais.get(h));
//	        						}
//	        						else{
	        							if (archivalLandscapeNode.getParent_internal_al_id()== null)
				    		            	this.ai.setParent(null);
				    		            else {
				    		            	ai_parent = aiDao.getArchivalInstitutionsByCountryIdandAlIdentifier(this.country.getId() , archivalLandscapeNode.getParent_internal_al_id());
				    		            	this.ai.setParent(ai_parent);
				    		            }
	        							this.getArchivalInstitutionsParentChanged().add(this.ai);
//	        						}
				            	}
		            		}
		            		//Discrepancies in parents to solve (unless the parent is the node of the country=fonds)
		            		else if ((archivalInstitution.getParent() == null) && (archivalLandscapeNode.getParent_internal_al_id()!= null))
		            		{
//		            			//Check if the item is in the index
//			            		Long numFilesIndexed = EditArchivalLandscapeLogic.countIndexedContentByInstitutionGroupId(archivalInstitution.getInternalAlId(), archivalInstitution.isGroup());
//        						if ((numFilesIndexed != null) && (numFilesIndexed > 0)) {
//		            				this.getArchivalInstitutionsParentNotChanged().add(this.ai);
//        						} else {
        							ai_parent = aiDao.getArchivalInstitutionsByCountryIdandAlIdentifier(this.country.getId() , archivalLandscapeNode.getParent_internal_al_id());
		    		            	this.ai.setParent(ai_parent);
		    		            	this.getArchivalInstitutionsParentChanged().add(this.ai);
//        						}
		            		}else if (((archivalInstitution.getParent() != null) && (archivalLandscapeNode.getParent_internal_al_id()== null)))
		            		{
//		            			//Check if the item is in the index
//			            		Long numFilesIndexed = EditArchivalLandscapeLogic.countIndexedContentByInstitutionGroupId(archivalInstitution.getInternalAlId(), archivalInstitution.isGroup());
//        						if ((numFilesIndexed != null) && (numFilesIndexed > 0)) {
//        							this.getArchivalInstitutionsParentNotChanged().add(this.ai);
//        						} else {
        							this.ai.setParent(null);
        							this.getArchivalInstitutionsParentChanged().add(this.ai);
//        						}
		            		}	
					         //Update always the order of the archival landscape
		            		this.ai.setAlorder(archivalLandscapeNode.getNodeId());
		            		this.ai.setInternalAlId(archivalLandscapeNode.getInternal_al_id());
		            		log.debug("Updating the parent in database of the institution "+ this.ai.getAiname());
	            			this.aiDao.updateSimple(this.ai);
	            			ContentUtils.updateContainsSearchableItemsInAiGroups(this.ai);
			            	ais.remove(i);
			            	upload = false;
		            	}
		        	}
			         //New files to upload
			         if (upload) {
                         for (Entry<String, Boolean> item : archivalLandscapeNode.getPrimaryName().entrySet()) {
                             if (item.getValue())
                                 this.ai.setAiname(archivalLandscapeNode.getNames().get(item.getKey()));
                         }
				        	
				         //this.ai.setPartnerId(this.partnerId);		            
				         this.ai.setGroup(archivalLandscapeNode.getIs_group());
				            
				         if (archivalLandscapeNode.getParent_name()== null)
				            this.ai.setParent(null);
				         else {
				        	 ai_parent = aiDao.getArchivalInstitutionsByCountryIdandAlIdentifier(this.country.getId() , archivalLandscapeNode.getParent_internal_al_id());
				             this.ai.setParent(ai_parent);
				         }
				         this.ai.setCountryId(this.country.getId());
				         this.ai.setCountry(this.country);
				         this.ai.setAlorder(archivalLandscapeNode.getNodeId());
				         this.ai.setInternalAlId(archivalLandscapeNode.getInternal_al_id());
				         //StoreAI
				         log.debug("Storing in database the institution "+ this.ai.getAiname());
				         this.aiDao.insertSimple(this.ai);
				         ContentUtils.updateContainsSearchableItemsInAiGroups(this.ai);
				         this.archivalInstitutionsToInsert.add(this.ai);
		         		
				         //Store the alternative names of the archival institution
                         for (Entry<String, String> temp : archivalLandscapeNode.getNames().entrySet()) {
                             AiAlternativeNameDAO ai_alNameDAO = DAOFactory.instance().getAiAlternativeNameDAO();
                             AiAlternativeName ai_altName = new AiAlternativeName();
                             ai_altName.setAiAName(temp.getValue());
                             ai_altName.setArchivalInstitution(this.ai);
                             if (lang != null) {
                                 if (!lang.getIsoname().toLowerCase().equals(temp.getKey().toLowerCase()))
                                     lang = langDao.getLangByIsoname(temp.getKey());
                             } else {
                                 lang = langDao.getLangByIsoname(temp.getKey());
                             }
                             ai_altName.setLang(lang);
                             if (archivalLandscapeNode.getPrimaryName().get(temp.getKey().toLowerCase()) == null)
                                 ai_altName.setPrimaryName(false);
                             else
                                 ai_altName.setPrimaryName(true);
                             log.debug("Storing in database the alternative name " + temp.getValue() + " of the institution " + this.ai.getAiname());
                             ai_alNameDAO.insertSimple(ai_altName);
                         }
			         }
		        }
	        }//End for        
		    //Files to delete 
		    if (ais.size() > 0) {
                for (ArchivalInstitution ai1 : ais) {
                    log.debug("Deleting from database the institution " + ai1.getAiname() + "...");
                    ContentUtils cu = new ContentUtils();
                    String resultRemoveAI = cu.deleteArchivalInstitution(ai1, execute);
                    archivalInstitutionsToDelete.add(ai1);
                    this.sentMailRegisterList.addAll(cu.getSentMailRegisterList());
                    if (resultRemoveAI.equals("error")) {
                        log.error("The institution " + ai1.getAiname() + " could not be removed. ");
                        result = "error";
                    }
                }
		    }
		    alNodes.clear();
		    ais.clear();
        }catch(IOException e)		{
        	log.error("The file " + file.getName() + " could not be read " + APEnetUtilities.generateThrowableLog(e));
        	result = "error";								
 		}catch (Exception e) {
			log.error("Some institutions of the country " + this.getmyCountry() +" could not be stored or removed in database and repository " + APEnetUtilities.generateThrowableLog(e));
			result = "error";
		}
		return result;
	}
	/**
	 * <p>
	 * Method which deletes content from an archival_institution (param)
	 * </p> 
	 * <p>
	 * It uses {@link ContentUtils} class to check if this institution 
	 * contains eacs and remove content.
	 * </p>
	 * <p>
	 * Also it rename internal files to be removed when confirmation success 
	 * process has been detected.
	 * </p>
	 * @param ai -> {@link ArchivalInstitution}, target to be deleted
	 * @return String -> renamed/market_like_deleted path
	 */
	protected static String deleteContent(ArchivalInstitution ai) {
		String path = null;
		ContentUtils cu = new ContentUtils();
		String resultRemoveAI = "";
        try {
        	EadDAO eadDAO = DAOFactory.instance().getEadDAO();
			ContentSearchOptions eadSearchOptions = new ContentSearchOptions();
			eadSearchOptions.setArchivalInstitionId(ai.getAiId());
			eadSearchOptions.setContentClass(FindingAid.class);
			boolean hasEads = eadDAO.existEads(eadSearchOptions);
			if (!hasEads) {
				eadSearchOptions.setContentClass(HoldingsGuide.class);
				hasEads = hasEads || eadDAO.existEads(eadSearchOptions);
			}
			if (!hasEads) {
				eadSearchOptions.setContentClass(SourceGuide.class);
				hasEads = hasEads || eadDAO.existEads(eadSearchOptions);
			}

			boolean hasEacs = ContentUtils.containsEacs(ai);
			if(!hasEads && !hasEacs){
				path = ai.getEagPath();
				resultRemoveAI = cu.deleteArchivalInstitution(ai,true);
				if(path!=null && path.length()>0){ //there are files to be removed
					log.debug("There are somethings to be removed, checking...");
					path = path.substring(0,path.indexOf(File.separatorChar+"EAG"));
					path = path+"_old";
					String subDir = APEnetUtilities.getConfig().getRepoDirPath();
					if(!(resultRemoveAI!=null && resultRemoveAI.equals("ok"))){
						log.debug("Rollback detected, reverting _old to original path...");
						FileUtils.moveDirectory(new File(subDir+path),new File(subDir+path.substring(0,path.length()-"_old".length())));
						log.debug("Revert done!");
					}
				}else{
					log.debug("Nothing to be removed.");
				}
			}
		} catch (Exception e) {
			log.error( APEnetUtilities.generateThrowableLog(e));
		}
        return path;
	}

	// End of methods from old ArchivalLandscape class.

	/**
	 * <p>
	 * Check special characters in the institution's name and alternative's name.
	 * </p>
	 * @param archivalInstitutions - Collection<{@link ArchivalInstitution}> source
	 * @return boolean -> if there are specials characters into the source collection
	 */
	public static Boolean checkSpecialCharacter(Collection<ArchivalInstitution> archivalInstitutions) {
		archivalInstitutions = ArchivalLandscapeUtils.parseCollectionToPlainList(archivalInstitutions); //parse to list
		Iterator<ArchivalInstitution> itListInstitutions = archivalInstitutions.iterator();
		ArchivalLandscapeUtils.setInstitutionsWithSpecialCharacters(null);
		while(itListInstitutions.hasNext()){
			ArchivalInstitution targetInstitution = itListInstitutions.next();
			if (targetInstitution.getAiname()!=null && (targetInstitution.getAiname().contains("<") 
				|| targetInstitution.getAiname().contains(":") || targetInstitution.getAiname().contains("\\")
				|| targetInstitution.getAiname().contains(">") || targetInstitution.getAiname().contains("%"))){
				ArchivalLandscapeUtils.addInstitutionsWithSpecialCharacters(targetInstitution.getAiname());
			}else{
				Set<AiAlternativeName> aiAlternativeNames = targetInstitution.getAiAlternativeNames();
				Iterator<AiAlternativeName> itAlternativeNames = aiAlternativeNames.iterator();
				boolean found = false; 
				while(!found && itAlternativeNames.hasNext()){
					AiAlternativeName alternativeName = itAlternativeNames.next();
					if (alternativeName.getAiAName().contains("<") || alternativeName.getAiAName().contains(">")
						|| targetInstitution.getAiname().contains(":") || targetInstitution.getAiname().contains("\\")
						|| alternativeName.getAiAName().contains("%")){
						
						ArchivalLandscapeUtils.addInstitutionsWithSpecialCharacters(targetInstitution.getAiname());
						found = true;
					}
			    }
			}
		}
		if (ArchivalLandscapeUtils.institutionsWithSpecialCharacters != null
				&& !ArchivalLandscapeUtils.institutionsWithSpecialCharacters.isEmpty()) {
			return true;
		}	
		return false;
	}
}
