package eu.apenet.dashboard.manual.eag;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.dashboard.AbstractInstitutionAction;
import eu.apenet.dashboard.archivallandscape.ArchivalLandscape;
import eu.apenet.dpt.utils.util.LanguageIsoList;
import eu.apenet.persistence.dao.ArchivalInstitutionDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.ArchivalInstitution;

/**
 * Action used to manage and store the new EAG2012.
 */
public class WebFormEAG2012Action extends AbstractInstitutionAction {

	/**
	 * Serializable.
	 */
	private static final long serialVersionUID = 732801399037503323L;

	private final Logger log = Logger.getLogger(getClass());
	
	/**
	 * This action is used to render the EAG2012 webform.
	 */

	private static final String OPTION_YES = "yes";	// Constant for value "yes".
	private static final String OPTION_NO = "no";	// Constant for value "no".

	private static final String OPTION_NONE = "none";				// Constant for value "none".
	private static final String OPTION_NATIONAL = "national";		// Constant for value "national".
	private static final String OPTION_REGIONAL = "regional";		// Constant for value "regional".
	private static final String OPTION_COUNTY = "county";			// Constant for value "county".
	private static final String OPTION_MUNICIPAL = "municipal";		// Constant for value "municipal".
	private static final String OPTION_SPECIALISED = "specialised";	// Constant for value "specialised".
	private static final String OPTION_PRIVATE = "private";			// Constant for value "private".
	private static final String OPTION_CHURCH = "church";			// Constant for value "church".
	private static final String OPTION_BUSINESS = "business";		// Constant for value "business".
	private static final String OPTION_UNIVERSITY = "university";	// Constant for value "university".
	private static final String OPTION_MEDIA = "media";				// Constant for value "media".
	private static final String OPTION_POLITICAL = "political";		// Constant for value "political".
	private static final String OPTION_CULTURAL = "cultural";		// Constant for value "cultural".

	private static final String OPTION_EUROPE = "europe";			// Constant for value "europe".
	private static final String OPTION_AFRICA = "africa";			// Constant for value "africa".
	private static final String OPTION_AMERICA = "america";			// Constant for value "america".
	private static final String OPTION_ANTARCTICA = "antarctica";	// Constant for value "antarctica".
	private static final String OPTION_ASIA = "asia";				// Constant for value "asia".
	private static final String OPTION_AUSTRALIA = "australia";		// Constant for value "australia".

	private static final String OPTION_DEPENDING = "depending";		// Constant for value "depending".
	private static final String OPTION_WITHOUT = "without";			// Constant for value "without".

	private static final String OPTION_CREATOR = "creator";			// Constant for value "creator".
	private static final String OPTION_SUBJECT = "subject";			// Constant for value "subject".
	private static final String OPTION_OTHER = "other";				// Constant for value "other".

	private static final String OPTION_CHILD = "child";				// Constant for value "child".
	private static final String OPTION_PARENT = "parent";			// Constant for value "parent".
	private static final String OPTION_EARLIER = "earlier";			// Constant for value "earlier".
	private static final String OPTION_LATER = "later";				// Constant for value "later".
	private static final String OPTION_ASSOCIATIVE = "associative";	// Constant for value "associative".

	private static final String OPTION_SCRIPT = "Latn";			// Constant for value "Latn".
	private static final String OPTION_SCRIPT_TEXT = "Latin";	// Constant for value "Latin".

	private static final String EAG_PATH = "EAG";

    private Map<String,String> yesNoMap = new HashMap<String,String>();
    private Map<String,String> typeOfInstitutionMap = new LinkedHashMap<String,String>();
    private Map<String,String> continentOfInstitutionMap = new LinkedHashMap<String,String>();
    private Map<String,String> photographMap = new LinkedHashMap<String,String>();
    private Map<String,String> typeYourRelationMap = new LinkedHashMap<String,String>();
    private Map<String,String> typeTheRelationMap = new LinkedHashMap<String,String>();
    private Map<String,String> languageISOMap = new LinkedHashMap<String,String>();
    private Map<String,String> scriptMap = new LinkedHashMap<String,String>();

	// Attributes.
	// Common to various tabs.
    private String personResponsibleForDescription;
    private String idOfInstitution;
    private String nameOfInstitution;
    private String nameOfInstitutionLanguage;
    private String parallelNameOfInstitution;
    private String parallelNameOfInstitutionLanguage;


	private String repositoridCountryCode;
	private String otherRepositorIdValue;
	private String recordIdValue;
	private String autformValue;			// Will be an ordered list. 
	private String autformLang;				// Will be an ordered list.
	private String parformValue;			// Will be an ordered list.
	private String parformLang;				// Will be an ordered list.

	// Your institution tab.

	// Identity tab.
	private String nonpreformValue;			// Will be an ordered list.
	private String nonpreformLang;			// Will be an ordered list.
	private String fromDateStandardDate;
	private String toDateStandardDate;
	private String repositoryTypeValue;		// Will be an ordered list.

	// Contact.

	// Access and Services.

	// Description.

	// Control.

	// Relations.

	/**
	 * Empty constructor.
	 */
	public WebFormEAG2012Action() {
		super();
	}

	public Map<String,String> getLanguageList() {
		String[] isoLanguages = Locale.getISOLanguages();
		Map<String,String> languages = new LinkedHashMap<String,String>();
		languages.put(WebFormEAG2012Action.OPTION_NONE, "---");
		for (String language : isoLanguages) {
			String languageDescription = new Locale(language).getDisplayLanguage(Locale.ENGLISH);
			languages.put(language, languageDescription);
		}
		return languages;
	}

	public Map<String,String> getYesNoList() {
		this.getYesNoMap().put(WebFormEAG2012Action.OPTION_YES, getText("label.ai.tabs.commons.option.yes"));
		this.getYesNoMap().put(WebFormEAG2012Action.OPTION_NO, getText("label.ai.tabs.commons.option.no"));

		return this.getYesNoMap();
	}

	public Map<String,String> getTypeOfInstitutionList() {
		this.getTypeOfInstitutionMap().put(WebFormEAG2012Action.OPTION_NONE, "---");
		this.getTypeOfInstitutionMap().put(WebFormEAG2012Action.OPTION_NATIONAL,
				getText("label.ai.tabs.commons.option.institutionType.nationalArchives"));
		this.getTypeOfInstitutionMap().put(WebFormEAG2012Action.OPTION_REGIONAL,
				getText("label.ai.tabs.commons.option.institutionType.regionalArchives"));
		this.getTypeOfInstitutionMap().put(WebFormEAG2012Action.OPTION_COUNTY,
				getText("label.ai.tabs.commons.option.institutionType.countyArchives"));
		this.getTypeOfInstitutionMap().put(WebFormEAG2012Action.OPTION_MUNICIPAL,
				getText("label.ai.tabs.commons.option.institutionType.municipalArchives"));
		this.getTypeOfInstitutionMap().put(WebFormEAG2012Action.OPTION_SPECIALISED,
				getText("label.ai.tabs.commons.option.institutionType.specialisedArchives"));
		this.getTypeOfInstitutionMap().put(WebFormEAG2012Action.OPTION_PRIVATE,
				getText("label.ai.tabs.commons.option.institutionType.privateArchives"));
		this.getTypeOfInstitutionMap().put(WebFormEAG2012Action.OPTION_CHURCH,
				getText("label.ai.tabs.commons.option.institutionType.churchArchives"));
		this.getTypeOfInstitutionMap().put(WebFormEAG2012Action.OPTION_BUSINESS,
				getText("label.ai.tabs.commons.option.institutionType.businessArchives"));
		this.getTypeOfInstitutionMap().put(WebFormEAG2012Action.OPTION_UNIVERSITY,
				getText("label.ai.tabs.commons.option.institutionType.universityArchives"));
		this.getTypeOfInstitutionMap().put(WebFormEAG2012Action.OPTION_MEDIA,
				getText("label.ai.tabs.commons.option.institutionType.mediaArchives"));
		this.getTypeOfInstitutionMap().put(WebFormEAG2012Action.OPTION_POLITICAL,
				getText("label.ai.tabs.commons.option.institutionType.politicalArchives"));
		this.getTypeOfInstitutionMap().put(WebFormEAG2012Action.OPTION_CULTURAL,
				getText("label.ai.tabs.commons.option.institutionType.culturalArchives"));

		return this.getTypeOfInstitutionMap();
	}

	public Map<String,String> getContinentOfTheInstitutionList() {
		this.getContinentOfInstitutionMap().put(WebFormEAG2012Action.OPTION_EUROPE,
				getText("label.ai.tabs.commons.option.continent.europe"));
		this.getContinentOfInstitutionMap().put(WebFormEAG2012Action.OPTION_AFRICA,
				getText("label.ai.tabs.commons.option.continent.africa"));
		this.getContinentOfInstitutionMap().put(WebFormEAG2012Action.OPTION_AMERICA,
				getText("label.ai.tabs.commons.option.continent.america"));
		this.getContinentOfInstitutionMap().put(WebFormEAG2012Action.OPTION_ANTARCTICA,
				getText("label.ai.tabs.commons.option.continent.antarctica"));
		this.getContinentOfInstitutionMap().put(WebFormEAG2012Action.OPTION_ASIA,
				getText("label.ai.tabs.commons.option.continent.asia"));
		this.getContinentOfInstitutionMap().put(WebFormEAG2012Action.OPTION_AUSTRALIA,
				getText("label.ai.tabs.commons.option.continent.australia"));

		return this.getContinentOfInstitutionMap();
	}

	public Map<String,String> getPhotographList() {
		this.getPhotographMap().put(WebFormEAG2012Action.OPTION_NONE, "---");
		this.getPhotographMap().put(WebFormEAG2012Action.OPTION_DEPENDING,
				getText("label.ai.tabs.commons.option.photograph.depending"));
		this.getPhotographMap().put(WebFormEAG2012Action.OPTION_YES, getText("label.ai.tabs.commons.option.yes"));
		this.getPhotographMap().put(WebFormEAG2012Action.OPTION_WITHOUT,
				getText("label.ai.tabs.commons.option.photograph.without"));
		this.getPhotographMap().put(WebFormEAG2012Action.OPTION_NO, getText("label.ai.tabs.commons.option.no"));

		return this.getPhotographMap();
	}

	public Map<String,String> getTypeYourRelationList() {
		this.getTypeYourRelationMap().put(WebFormEAG2012Action.OPTION_CREATOR,
				getText("label.ai.tabs.commons.option.typeYourRelation.creator"));
		this.getTypeYourRelationMap().put(WebFormEAG2012Action.OPTION_SUBJECT,
				getText("label.ai.tabs.commons.option.typeYourRelation.subject"));
		this.getTypeYourRelationMap().put(WebFormEAG2012Action.OPTION_OTHER,
				getText("label.ai.tabs.commons.option.typeYourRelation.other"));

		return this.getTypeYourRelationMap();
	}

	public Map<String,String> getTypeTheRelationList() {
		this.getTypeTheRelationMap().put(WebFormEAG2012Action.OPTION_NONE, "---");
		this.getTypeTheRelationMap().put(WebFormEAG2012Action.OPTION_CHILD,
				getText("label.ai.tabs.commons.option.typeTheRelation.child"));
		this.getTypeTheRelationMap().put(WebFormEAG2012Action.OPTION_PARENT,
				getText("label.ai.tabs.commons.option.typeTheRelation.parent"));
		this.getTypeTheRelationMap().put(WebFormEAG2012Action.OPTION_EARLIER,
				getText("label.ai.tabs.commons.option.typeTheRelation.earlier"));
		this.getTypeTheRelationMap().put(WebFormEAG2012Action.OPTION_LATER,
				getText("label.ai.tabs.commons.option.typeTheRelation.later"));
		this.getTypeTheRelationMap().put(WebFormEAG2012Action.OPTION_ASSOCIATIVE,
				getText("label.ai.tabs.commons.option.typeTheRelation.associative"));

		return this.getTypeTheRelationMap();
	}

	public Map<String,String> getLanguageISOList() {
		this.getLanguageISOMap().put(WebFormEAG2012Action.OPTION_NONE, "---");

		List<String> languageList = LanguageIsoList.getLanguageIsoList();

		for (int i = 0; i < languageList.size(); i++) {
			Locale locale = new Locale(languageList.get(i));
			this.getLanguageISOMap().put(locale.getISO3Language(), locale.getDisplayLanguage());
		}

		return this.getLanguageISOMap();
	}

	public Map<String,String> getScriptList() {
		this.getScriptMap().put(WebFormEAG2012Action.OPTION_NONE, "---");

		this.getScriptMap().put(WebFormEAG2012Action.OPTION_SCRIPT, WebFormEAG2012Action.OPTION_SCRIPT_TEXT);

		return this.getScriptMap();
	}

	// Getters and setters.
	/**
	 * @return the yesNoMap
	 */
	public Map<String,String> getYesNoMap() {
		return this.yesNoMap;
	}

	/**
	 * @param yesNoSet the yesNoMap to set
	 */
	public void setYesNoMap(Map<String,String> yesNoMap) {
		this.yesNoMap = yesNoMap;
	}

	/**
	 * @return the typeOfInstitutionMap
	 */
	public Map<String, String> getTypeOfInstitutionMap() {
		return this.typeOfInstitutionMap;
	}

	/**
	 * @param typeOfInstitutionMap the typeOfInstitutionMap to set
	 */
	public void setTypeOfInstitutionMap(Map<String, String> typeOfInstitutionMap) {
		this.typeOfInstitutionMap = typeOfInstitutionMap;
	}

	/**
	 * @return the continentOfInstitutionMap
	 */
	public Map<String, String> getContinentOfInstitutionMap() {
		return this.continentOfInstitutionMap;
	}

	/**
	 * @param continentOfInstitutionMap the continentOfInstitutionMap to set
	 */
	public void setContinentOfInstitutionMap(
			Map<String, String> continentOfInstitutionMap) {
		this.continentOfInstitutionMap = continentOfInstitutionMap;
	}

	/**
	 * @return the photographMap
	 */
	public Map<String, String> getPhotographMap() {
		return this.photographMap;
	}

	/**
	 * @param photographMap the photographMap to set
	 */
	public void setPhotographMap(Map<String, String> photographMap) {
		this.photographMap = photographMap;
	}

	/**
	 * @return the typeYourRelationMap
	 */
	public Map<String, String> getTypeYourRelationMap() {
		return this.typeYourRelationMap;
	}

	/**
	 * @param typeYourRelationMap the typeYourRelationMap to set
	 */
	public void setTypeYourRelationMap(Map<String, String> typeYourRelationMap) {
		this.typeYourRelationMap = typeYourRelationMap;
	}

	/**
	 * @return the typetheRelationMap
	 */
	public Map<String, String> getTypeTheRelationMap() {
		return this.typeTheRelationMap;
	}

	/**
	 * @param typetheRelationMap the typeTheRelationMap to set
	 */
	public void setTypeTheRelationMap(Map<String, String> typeTheRelationMap) {
		this.typeTheRelationMap = typeTheRelationMap;
	}

	/**
	 * @return the languageISOMap
	 */
	public Map<String, String> getLanguageISOMap() {
		return this.languageISOMap;
	}

	/**
	 * @param languageISOMap the languageISOMap to set
	 */
	public void setLanguageISOMap(Map<String, String> languageISOMap) {
		this.languageISOMap = languageISOMap;
	}

	/**
	 * @return the scriptMap
	 */
	public Map<String, String> getScriptMap() {
		return this.scriptMap;
	}

	/**
	 * @param scriptMap the scriptMap to set
	 */
	public void setScriptMap(Map<String, String> scriptMap) {
		this.scriptMap = scriptMap;
	}

	/**
	 * @return the personResponsibleForDescription
	 */
	public String getPersonResponsibleForDescription() {
		return this.personResponsibleForDescription;
	}

	/**
	 * @param personResponsibleForDescription the personResponsibleForDescription to set
	 */
	public void setPersonResponsibleForDescription(
			String personResponsibleForDescription) {
		this.personResponsibleForDescription = personResponsibleForDescription;
	}

	/**
	 * @return the idOfInstitution
	 */
	public String getIdOfInstitution() {
		return this.idOfInstitution;
	}

	/**
	 * @param idOfInstitution the idOfInstitution to set
	 */
	public void setIdOfInstitution(String idOfInstitution) {
		this.idOfInstitution = idOfInstitution;
	}

	/**
	 * @return the nameOfInstitution
	 */
	public String getNameOfInstitution() {
		return this.nameOfInstitution;
	}

	/**
	 * @param nameOfInstitution the nameOfInstitution to set
	 */
	public void setNameOfInstitution(String nameOfInstitution) {
		this.nameOfInstitution = nameOfInstitution;
	}

	/**
	 * @return the nameOfInstitutionLanguage
	 */
	public String getNameOfInstitutionLanguage() {
		return this.nameOfInstitutionLanguage;
	}

	/**
	 * @param nameOfInstitutionLanguage the nameOfInstitutionLanguage to set
	 */
	public void setNameOfInstitutionLanguage(String nameOfInstitutionLanguage) {
		this.nameOfInstitutionLanguage = nameOfInstitutionLanguage;
	}

	/**
	 * @return the parallelNameOfInstitution
	 */
	public String getParallelNameOfInstitution() {
		return this.parallelNameOfInstitution;
	}

	/**
	 * @param parallelNameOfInstitution the parallelNameOfInstitution to set
	 */
	public void setParallelNameOfInstitution(String parallelNameOfInstitution) {
		this.parallelNameOfInstitution = parallelNameOfInstitution;
	}

	/**
	 * @return the parallelNameOfInstitutionLanguage
	 */
	public String getParallelNameOfInstitutionLanguage() {
		return this.parallelNameOfInstitutionLanguage;
	}

	/**
	 * @param parallelNameOfInstitutionLanguage the parallelNameOfInstitutionLanguage to set
	 */
	public void setParallelNameOfInstitutionLanguage(
			String parallelNameOfInstitutionLanguage) {
		this.parallelNameOfInstitutionLanguage = parallelNameOfInstitutionLanguage;
	}

	/**
	 * @return the repositoridCountryCode
	 */
	public String getRepositoridCountryCode() {
		return this.repositoridCountryCode;
	}

	/**
	 * @param repositoridCountryCode the repositoridCountryCode to set
	 */
	public void setRepositoridCountryCode(String repositoridCountryCode) {
		this.repositoridCountryCode = repositoridCountryCode;
	}

	/**
	 * @return the otherRepositorIdValue
	 */
	public String getOtherRepositorIdValue() {
		return this.otherRepositorIdValue;
	}

	/**
	 * @param otherRepositorIdValue the otherRepositorIdValue to set
	 */
	public void setOtherRepositorIdValue(String otherRepositorIdValue) {
		this.otherRepositorIdValue = otherRepositorIdValue;
	}

	/**
	 * @return the recordIdValue
	 */
	public String getRecordIdValue() {
		return this.recordIdValue;
	}

	/**
	 * @param recordIdValue the recordIdValue to set
	 */
	public void setRecordIdValue(String recordIdValue) {
		this.recordIdValue = recordIdValue;
	}

	/**
	 * @return the autformValue
	 */
	public String getAutformValue() {
		return this.autformValue;
	}

	/**
	 * @param autformValue the autformValue to set
	 */
	public void setAutformValue(String autformValue) {
		this.autformValue = autformValue;
	}

	/**
	 * @return the autformLang
	 */
	public String getAutformLang() {
		return this.autformLang;
	}

	/**
	 * @param autformLang the autformLang to set
	 */
	public void setAutformLang(String autformLang) {
		this.autformLang = autformLang;
	}

	/**
	 * @return the parformValue
	 */
	public String getParformValue() {
		return this.parformValue;
	}

	/**
	 * @param parformValue the parformValue to set
	 */
	public void setParformValue(String parformValue) {
		this.parformValue = parformValue;
	}

	/**
	 * @return the parformLang
	 */
	public String getParformLang() {
		return this.parformLang;
	}

	/**
	 * @param parformLang the parformLang to set
	 */
	public void setParformLang(String parformLang) {
		this.parformLang = parformLang;
	}

	/**
	 * @return the nonpreformValue
	 */
	public String getNonpreformValue() {
		return this.nonpreformValue;
	}

	/**
	 * @param nonpreformValue the nonpreformValue to set
	 */
	public void setNonpreformValue(String nonpreformValue) {
		this.nonpreformValue = nonpreformValue;
	}

	/**
	 * @return the nonpreformLang
	 */
	public String getNonpreformLang() {
		return this.nonpreformLang;
	}

	/**
	 * @param nonpreformLang the nonpreformLang to set
	 */
	public void setNonpreformLang(String nonpreformLang) {
		this.nonpreformLang = nonpreformLang;
	}

	/**
	 * @return the fromDateStandardDate
	 */
	public String getFromDateStandardDate() {
		return this.fromDateStandardDate;
	}

	/**
	 * @param fromDateStandardDate the fromDateStandardDate to set
	 */
	public void setFromDateStandardDate(String fromDateStandardDate) {
		this.fromDateStandardDate = fromDateStandardDate;
	}

	/**
	 * @return the toDateStandardDate
	 */
	public String getToDateStandardDate() {
		return this.toDateStandardDate;
	}

	/**
	 * @param toDateStandardDate the toDateStandardDate to set
	 */
	public void setToDateStandardDate(String toDateStandardDate) {
		this.toDateStandardDate = toDateStandardDate;
	}

	/**
	 * @return the repositoryTypeValue
	 */
	public String getRepositoryTypeValue() {
		return this.repositoryTypeValue;
	}

	/**
	 * @param repositoryTypeValue the repositoryTypeValue to set
	 */
	public void setRepositoryTypeValue(String repositoryTypeValue) {
		this.repositoryTypeValue = repositoryTypeValue;
	}

	@Override
	public String execute() throws Exception {
		return SUCCESS;
	}
	
	/**
	 * Returns the into ISO_2Characteres.
	 * @return CC 
	 */
	public String getCountryCode(){
		return new ArchivalLandscape().getmyCountry();
	}
	
	/**
	 * Generates unique isocode for ID used in APE.
	 * 
	 * @return ISO_CODE
	 */
	public String getIdUsedInAPE(){
		return Eag2012.generatesISOCode(getAiId());
	}
	
	private String form;
	
	public void setForm(String form){
		this.form = form;
	}
	
	public String getForm(){
		return this.form;
	}
	
	public String createEAG2012(){
		Eag2012 eag2012 = null;
		try{
			eag2012 = getAndFillEag2012Object();
		}catch (JSONException e) {
			log.error(e.getMessage());
		}
		if(eag2012!=null){
			String path = File.separatorChar+getCountryCode()+File.separatorChar+getAiId()+File.separatorChar+EAG_PATH+File.separatorChar+"eag2012.xml";
			
			//create file
			Eag2012Creator creator = new Eag2012Creator(this.getAiId(), eag2012,APEnetUtilities.getConfig().getRepoDirPath()+path);
			creator.createEag2012(); 
			
			//store ddbb path
			ArchivalInstitutionDAO archivalInstitutionDao = DAOFactory.instance().getArchivalInstitutionDAO();
			ArchivalInstitution archivalInstitution = archivalInstitutionDao.getArchivalInstitution(getAiId());
			if(archivalInstitution!=null){
				archivalInstitution.setEagPath(path);
				archivalInstitutionDao.store(archivalInstitution);
				log.info("EAG2012 stored to "+path);
			}else{
				log.error("Could not be stored EAG2012 path, reason: null archival institution");
			}
			
		}
		return SUCCESS;
	}

	private Eag2012 getAndFillEag2012Object() throws JSONException {
		Eag2012 eag2012 = null;
		if(this.form!=null && !this.form.isEmpty()){
			eag2012 = new Eag2012();
			JSONObject jsonObj = new JSONObject(this.form);
			if(jsonObj.has("yourInstitution")){ //first_tab -> 'yourInstitution' : array[{},..,{}]
				eag2012 = parseYourInstitutionJsonObjToEag2012(eag2012,jsonObj);				
			}
			if(jsonObj.has("identity")){
				eag2012 = parseIdentityJsonObjToEag2012(eag2012,jsonObj);
			}
	        if(jsonObj.has("contact")){
	        	eag2012 = parseContactJsonObjToEag2012(eag2012, jsonObj);
	        }
		}
		
		return eag2012;
	}
	
	private Eag2012 parseContactJsonObjToEag2012(Eag2012 eag2012,JSONObject jsonObj) throws JSONException {
		JSONObject contact = jsonObj.getJSONObject("contact");
		if(contact!=null){
			int x = 0;
			while(contact.has("contactTable_"+(++x))){ //each child of contact is the container of visitor address and all attributes
				
				JSONObject contactTable = contact.getJSONObject("contactTable_"+(x));
				//Contact visitorsAddress
				
				if(contactTable.has("textNameOfRepository")){
					String nameOfRepository = contactTable.getString("textNameOfRepository");
					List<String> repositorNames = eag2012.getRepositoryNameValue();
					if(repositorNames==null){
						repositorNames = new ArrayList<String>();
					}
					repositorNames.add(nameOfRepository);
					eag2012.setRepositoryNameValue(repositorNames);
				}
				if(contactTable.has("selectRoleOfRepository")){
					String roleOfRepository = contactTable.getString("selectRoleOfRepository");
					List<String> rolesOfRepository = eag2012.getRepositoryRoleValue();
					if(rolesOfRepository==null){
						rolesOfRepository = new ArrayList<String>();
					}
					rolesOfRepository.add(roleOfRepository);
					eag2012.setRepositoryRoleValue(rolesOfRepository);
				}
				
				JSONObject visitorAddress = contactTable.getJSONObject("visitorsAddress");
				if(visitorAddress!=null){
					List<String> listStreet = new ArrayList<String>();
					List<String> listCities = new ArrayList<String>();
					List<String> listDistrict = new ArrayList<String>();
					List<String> listLocalAuthority = new ArrayList<String>();
					List<String> listAutonomus = new ArrayList<String>();
					List<String> listCountries = new ArrayList<String>();
					List<String> listLatitudes = new ArrayList<String>();
					List<String> listLongitudes = new ArrayList<String>();
					List<String> listStreetLanguage = new ArrayList<String>();
					List<List<String>> tempListList = new ArrayList<List<String>>();  
					
					
					
					int i=1;
					if(visitorAddress.has("contactTableVisitorsAddress_"+i)){
				    	
				      do{
				    	    JSONObject visitorAddressTable = visitorAddress.getJSONObject("contactTableVisitorsAddress_"+i);
							listStreet.add(visitorAddressTable.getString("textContactStreetOfTheInstitution"));
							listStreetLanguage.add(visitorAddressTable.getString("selectLanguageVisitorAddress")); 
				            listCities.add(visitorAddressTable.getString("textContactCityOfTheInstitution"));
				            listDistrict.add(visitorAddressTable.getString("textContactDistrictOfTheInstitution"));
				            listLocalAuthority.add(visitorAddressTable.getString("textContactCountyOfTheInstitution"));
				            listAutonomus.add(visitorAddressTable.getString("textContactRegionOfTheInstitution"));
				            listCountries.add(visitorAddressTable.getString("textContactCountryOfTheInstitution"));
				            listLatitudes.add(visitorAddressTable.getString("textContactLatitudeOfTheInstitution"));
				            listLongitudes.add(visitorAddressTable.getString("textContactLongitudeOfTheInstitution"));
				            
				      
				      }while(visitorAddress.has("contactTableVisitorsAddress_"+(++i)));
				      
				     
						tempListList.add(listStreet);
						eag2012.setStreetValue(tempListList);
						tempListList = new ArrayList<List<String>>();
						tempListList.add(listStreetLanguage);
						eag2012.setStreetLang(tempListList);
						tempListList = new ArrayList<List<String>>();
						tempListList.add(listCities);
						eag2012.setCitiesValue(tempListList);
						eag2012.setLocalentityValue(listDistrict);
						tempListList = new ArrayList<List<String>>();
						tempListList.add(listLocalAuthority);
						eag2012.setSecondemValue(tempListList);
						tempListList = new ArrayList<List<String>>();
						tempListList.add(listAutonomus);
						eag2012.setFirstdemValue(tempListList);
						tempListList = new ArrayList<List<String>>();
						tempListList.add(listCountries);
						eag2012.setCountryValue(tempListList);
						tempListList = new ArrayList<List<String>>();
						tempListList.add(listLatitudes);
						eag2012.setLocationLatitude(tempListList);
						tempListList = new ArrayList<List<String>>();
						tempListList.add(listLongitudes);
						eag2012.setLocationLongitude(tempListList);
				      
				      }
				}
				
				JSONObject postalAddress = contactTable.getJSONObject("postalAddress");
				if(postalAddress!=null){
					List<String> listStreet = new ArrayList<String>();
					List<String> listStreetLanguage = new ArrayList<String>();
					List<String> listCities = new ArrayList<String>();
					List<List<String>> tempListList = new ArrayList<List<String>>();
					int i=1;
					if(postalAddress.has("contactTablePostalAddress_"+i)){
						
						do{
							JSONObject postalAddressTable = postalAddress.getJSONObject("contactTablePostalAddress_"+i);
							listStreet.add(postalAddressTable.getString("textContactPAStreet"));
							listStreetLanguage.add(postalAddressTable.getString("selectContactLanguagePostalAddress"));
							listCities.add(postalAddressTable.getString("textContactPACity"));
							
						}while(postalAddress.has("contactTablePostalAddress_"+(++i)));
						
						tempListList.add(listStreet);
						eag2012.setPostalStreetValue(tempListList);
						tempListList = new ArrayList<List<String>>();
						tempListList.add(listStreetLanguage);
						eag2012.setPostalStreetLang(tempListList);
						tempListList = new ArrayList<List<String>>();
						tempListList.add(listCities);
						eag2012.setMunicipalityPostalcodeValue(tempListList);
					}
					
				}
				int i=1;
				while(contactTable.has("textContactTelephoneOfTheInstitution_"+(++i))){
					Map<String, List<String>> telephones = new HashMap<String,List<String>>();
					ArrayList<String> listTelephones = new ArrayList<String>();
					listTelephones.add(contactTable.getString("textContactTelephoneOfTheInstitution_"+i));
					telephones.put(Eag2012Creator.TAB_CONTACT,listTelephones);
					List<Map<String, List<String>>> listMapTelephonesList = new ArrayList<Map<String, List<String>>>(); 
					listMapTelephonesList.add(telephones);
					eag2012.setTelephoneValue(listMapTelephonesList); 	
				}
				
			    i=0;
				while(contactTable.has("textContactFaxOfTheInstitution_"+(++i))){
					Map<String, List<String>> fax = new HashMap<String,List<String>>();
					ArrayList<String> listFax = new ArrayList<String>();
					listFax.add(contactTable.getString("textContactFaxOfTheInstitution_"+i));
					fax.put(Eag2012Creator.TAB_CONTACT,listFax);
					List<Map<String, List<String>>> listMapFaxList = new ArrayList<Map<String, List<String>>>(); 
					listMapFaxList.add(fax);
					eag2012.setFaxValue(listMapFaxList); 	
				}
				
				 i=(x==1)?1:0;
				 while(contactTable.has("textContactEmailOfTheInstitution_"+(++i))){
					 Map<String, List<String>> email = new HashMap<String,List<String>>();
					 ArrayList<String> listEmail = new ArrayList<String>();
					 listEmail.add(contactTable.getString("textContactEmailOfTheInstitution_"+i));
					 email.put(Eag2012Creator.TAB_CONTACT,listEmail);
					 List<Map<String, List<String>>> listMapEmailList = new ArrayList<Map<String, List<String>>>(); 
					 listMapEmailList.add(email);
					 eag2012.setEmailHref(listMapEmailList); 	
					}
				 i=(x==1)?1:0;
				 while(contactTable.has("textContactLinkTitleForEmailOfTheInstitution_"+(++i))){
					 Map<String, List<String>> email = new HashMap<String,List<String>>();
					 ArrayList<String> listEmail = new ArrayList<String>();
					 listEmail.add(contactTable.getString("textContactLinkTitleForEmailOfTheInstitution_"+i));
					 email.put(Eag2012Creator.TAB_CONTACT,listEmail);
					 List<Map<String, List<String>>> listMapEmailList = new ArrayList<Map<String, List<String>>>(); 
					 listMapEmailList.add(email);
					 eag2012.setEmailValue(listMapEmailList); 	
					}
				 i=(x==1)?1:0;
				 while(contactTable.has("textContactWebOfTheInstitution_"+(++i))){
					 Map<String, List<String>> web = new HashMap<String,List<String>>();
					 ArrayList<String> listWeb = new ArrayList<String>();
					 listWeb.add(contactTable.getString("textContactWebOfTheInstitution_"+i));
					 web.put(Eag2012Creator.TAB_CONTACT,listWeb);
					 List<Map<String, List<String>>> listMapWebList = new ArrayList<Map<String, List<String>>>(); 
					 listMapWebList.add(web);
					 eag2012.setWebpageHref(listMapWebList); 	
					}
			
				 i=(x==1)?1:0;
				 while(contactTable.has("textContactLinkTitleForWebOfTheInstitution_"+(++i))){
					 Map<String, List<String>> web = new HashMap<String,List<String>>();
					 ArrayList<String> listWeb = new ArrayList<String>();
					 listWeb.add(contactTable.getString("textContactLinkTitleForWebOfTheInstitution_"+i));
					 web.put(Eag2012Creator.TAB_CONTACT,listWeb);
					 List<Map<String, List<String>>> listMapWebList = new ArrayList<Map<String, List<String>>>(); 
					 listMapWebList.add(web);
					 eag2012.setWebpageValue(listMapWebList); 	
					}
			}
		}
		return eag2012;	
	}
	
	private Eag2012 parseIdentityJsonObjToEag2012(Eag2012 eag2012,JSONObject jsonObj) throws JSONException {
		JSONObject identity = jsonObj.getJSONObject("identity");
		if(identity!=null){
			if(identity.has("institutionNames")){
				JSONObject institutionNames = identity.getJSONObject("institutionNames");
				//Name of the institution
				int i=1;
				while(institutionNames.has("identityTableNameOfTheInstitution_"+(++i))){
					JSONObject nameOfTheInstitutionTable = institutionNames.getJSONObject("identityTableNameOfTheInstitution_"+i);
					if(nameOfTheInstitutionTable.has("textNameOfTheInstitution")){
						List<String> listAutforms = eag2012.getAutformValue();
						if(listAutforms==null){
							listAutforms = new ArrayList<String>();
						}
						listAutforms.add(nameOfTheInstitutionTable.getString("textNameOfTheInstitution"));
						eag2012.setAutformValue(listAutforms);
					}
					if(nameOfTheInstitutionTable.has("noti_languageList")){
						List<String> listAutformLangs = eag2012.getAutformLang();
						if(listAutformLangs==null){
							listAutformLangs = new ArrayList<String>();
						}
						listAutformLangs.add(nameOfTheInstitutionTable.getString("noti_languageList"));
						eag2012.setAutformLang(listAutformLangs);
					}
				}
			}
			if(identity.has("parallelNames")){
				JSONObject parallelName = identity.getJSONObject("parallelNames");
				int i=1;
				//Parallel name of the institution
				while(parallelName.has("identityTableParallelNameOfTheInstitution_"+(++i))){
					JSONObject parallelNameOfTheInstitution = parallelName.getJSONObject("identityTableParallelNameOfTheInstitution_"+i);
					if(parallelNameOfTheInstitution.has("textParallelNameOfTheInstitution")){
						List<String> listParforms = eag2012.getParformValue();
						if(listParforms==null){
							listParforms = new ArrayList<String>();
						}
						listParforms.add(parallelNameOfTheInstitution.getString("textParallelNameOfTheInstitution"));
						eag2012.setParformValue(listParforms);
					}
					if(parallelNameOfTheInstitution.has("pnoti_languageList")){
						List<String> listParformLangs = eag2012.getParformLang();
						if(listParformLangs==null){
							listParformLangs = new ArrayList<String>();
						}
						listParformLangs.add(parallelNameOfTheInstitution.getString("pnoti_languageList"));
						eag2012.setParformLang(listParformLangs);
					}
				}
			}
			if(identity.has("formerlyNames")){
				JSONObject formerlyName = identity.getJSONObject("formerlyNames");
				int i=0;
				while(formerlyName.has("identityTableFormerlyUsedName_"+(++i))){
					//Formerly used name
					JSONObject previousNameOfTheArchive = formerlyName.getJSONObject("identityTableFormerlyUsedName_"+i);
					if(previousNameOfTheArchive.has("textFormerlyUsedName")){
						List<String> listNonpreform = eag2012.getNonpreformValue();
						if(listNonpreform==null){
							listNonpreform = new ArrayList<String>();
						}
						listNonpreform.add(previousNameOfTheArchive.getString("textFormerlyUsedName"));
						eag2012.setNonpreformValue(listNonpreform);
					}
					if(previousNameOfTheArchive.has("tfun_languageList")){
						List<String> listNonpreformLangs = eag2012.getNonpreformLang();
						if(listNonpreformLangs==null){
							listNonpreformLangs = new ArrayList<String>();
						}
						listNonpreformLangs.add(previousNameOfTheArchive.getString("tfun_languageList"));
						eag2012.setNonpreformLang(listNonpreformLangs);
					}
					//Identity Single Year
					int j=0;
					while(previousNameOfTheArchive.has("textYearWhenThisNameWasUsed_"+(++j))){
						Map<String, List<String>> years = new HashMap<String,List<String>>();
						ArrayList<String> listYears = new ArrayList<String>();
						listYears.add(previousNameOfTheArchive.getString("textYearWhenThisNameWasUsed_"+j));
						years.put(Eag2012Creator.TAB_IDENTITY,listYears);
						List<Map<String, List<String>>> listMapYearsList = eag2012.getDateStandardDate();
						if(listMapYearsList==null){
							listMapYearsList = new ArrayList<Map<String, List<String>>>();
						}
						listMapYearsList.add(years);
						eag2012.setDateStandardDate(listMapYearsList);
					}
					//Identity Range Year From
					j=0;
					while(previousNameOfTheArchive.has("textYearWhenThisNameWasUsedFrom_"+(++j))){
						Map<String, List<String>> years = new HashMap<String,List<String>>();
						ArrayList<String> listYears = new ArrayList<String>();
						listYears.add(previousNameOfTheArchive.getString("textYearWhenThisNameWasUsedFrom_"+j));
						years.put(Eag2012Creator.TAB_IDENTITY,listYears);
						List<Map<String, List<String>>> listMapYearsList = eag2012.getDateStandardDate();
						if(listMapYearsList==null){
							listMapYearsList = new ArrayList<Map<String, List<String>>>();
						}
						listMapYearsList.add(years);
						eag2012.setFromDateStandardDate(listMapYearsList);
					}
					//Identity Range Year To
					j=0;
					while(previousNameOfTheArchive.has("textYearWhenThisNameWasUsedTo_"+(++j))){
						Map<String, List<String>> years = new HashMap<String,List<String>>();
						ArrayList<String> listYears = new ArrayList<String>();
						listYears.add(previousNameOfTheArchive.getString("textYearWhenThisNameWasUsedTo_"+j));
						years.put(Eag2012Creator.TAB_IDENTITY,listYears);
						List<Map<String, List<String>>> listMapYearsList = eag2012.getDateStandardDate();
						if(listMapYearsList==null){
							listMapYearsList = new ArrayList<Map<String, List<String>>>();
						}
						listMapYearsList.add(years);
						eag2012.setToDateStandardDate(listMapYearsList);
					}
				}
			}
			//Identity Type of the Institution
			if (identity.has("selectTypeOfTheInstitution")){
				List<String> listRepositoryType = new ArrayList<String>(); 
				listRepositoryType.add(identity.getString("selectTypeOfTheInstitution"));
				eag2012.setRepositoryTypeValue(listRepositoryType);
			}
		}
		return eag2012;
	}

	/**
	 * Fill a eag2012 object got from params, fill it with the information provided into 
	 * JSONObject (got from params) and returns the target EAG2012 object. 
	 * 
	 * @param Eag2012
	 * @param JSONObj
	 * @return Eag2012
	 * @throws JSONException
	 */
	private Eag2012 parseYourInstitutionJsonObjToEag2012(Eag2012 eag2012,JSONObject jsonObj) throws JSONException {
		JSONObject yourInstitution = jsonObj.getJSONObject("yourInstitution");
		if(yourInstitution!=null){
			//your institution - your institution
			if(yourInstitution.has("textYIPersonInstitutionResposibleForTheDescription")){
				eag2012.setAgentValue(yourInstitution.getString("textYIPersonInstitutionResposibleForTheDescription"));
			}
			eag2012.setRepositoridCountrycode(yourInstitution.getString("textYIInstitutionCountryCode"));
			//used afterwards
			//eag2012.setCountryValue(yourInstitution.get("textYIInstitutionCountryCode").toString()); //this tag is used into each repository. TODO, needs to be parsed to ISO3_Characters
			//eag2012.setRepositoridRepositorycode(yourInstitution.getString("textYIIdentifierOfTheInstitution"));
			eag2012.setOtherRepositorId(yourInstitution.getString("textYIIdentifierOfTheInstitution"));
			eag2012.setRepositoridRepositorycode(eag2012.getOtherRepositorId());
			eag2012.setRecordIdValue(yourInstitution.getString("textYIIdUsedInAPE"));
			//looper
			List<String> otherRepositorIds = new ArrayList<String>();
			for(int i=0;yourInstitution.has("otherRepositorId_"+(i));i++){
				otherRepositorIds.add(yourInstitution.getString("otherRepositorId_"+(i)));
			}
			if(otherRepositorIds.size()>0){
				eag2012.setOtherRecordIdValue(otherRepositorIds);
			}
			List<String> tempList = new ArrayList<String>();
			if(yourInstitution.has("textYINameOfTheInstitution")){
				tempList.add(yourInstitution.getString("textYINameOfTheInstitution"));
				eag2012.setAutformValue(tempList);
			}
			if(yourInstitution.has("selectYINOTISelectLanguage")){
				tempList = new ArrayList<String>();
				tempList.add(yourInstitution.getString("selectYINOTISelectLanguage"));
				eag2012.setAutformLang(tempList);
			}
			if(yourInstitution.has("textYIParallelNameOfTheInstitution")){
				tempList = new ArrayList<String>();
				tempList.add(yourInstitution.getString("textYIParallelNameOfTheInstitution"));
				eag2012.setParformValue(tempList);
			}
			if(yourInstitution.has("selectYIPNOTISelectLanguage")){
				tempList = new ArrayList<String>();
				tempList.add(yourInstitution.getString("selectYIPNOTISelectLanguage"));
				eag2012.setParformLang(tempList);
			}
			//your institution - visitor address
			JSONObject visitorAddress = yourInstitution.getJSONObject("visitorsAddress");
			if(visitorAddress!=null){
				List<String> listStreets = new ArrayList<String>();
				List<String> listLangStreets = new ArrayList<String>();
				List<String> listCities = new ArrayList<String>();
				List<String> listCountries = new ArrayList<String>();
				List<String> listLatitudes = new ArrayList<String>();
				List<String> listLongitudes = new ArrayList<String>();
				List<String> listStreetLanguage = new ArrayList<String>();
				if(visitorAddress.length()>0){
					for(int i=0;i<visitorAddress.length();i++){
						JSONObject visitorAddressTable = visitorAddress.getJSONObject("yiTableVisitorsAddress_"+(i+1));
						listStreets.add(visitorAddressTable.getString("textYIStreet"));
						listLangStreets.add(visitorAddressTable.getString("selectYIVASelectLanguage"));
						listCities.add(visitorAddressTable.getString("textYICity"));
						listCountries.add(visitorAddressTable.getString("textYICountry"));
						listLatitudes.add(visitorAddressTable.getString("textYILatitude"));
						listLongitudes.add(visitorAddressTable.getString("textYILongitude"));
						listStreetLanguage.add(visitorAddressTable.getString("selectYIVASelectLanguage"));
					}//"yiTableVisitorsAddress_"+(++i)
					
					List<List<String>> tempListList = new ArrayList<List<String>>(); //at first time list must be in 0 position for first <location> tag into <repository> and 
					tempListList.add(listStreets);
					eag2012.setStreetValue(tempListList);
					tempListList = new ArrayList<List<String>>();
					tempListList.add(listLangStreets);
					eag2012.setStreetLang(tempListList);
					tempListList = new ArrayList<List<String>>();
					tempListList.add(listCities);
					eag2012.setMunicipalityPostalcodeValue(tempListList);
					tempListList = new ArrayList<List<String>>();
					tempListList.add(listCountries);
					eag2012.setCountryValue(tempListList);
					tempListList = new ArrayList<List<String>>();
					tempListList.add(listStreetLanguage);
					eag2012.setCountryLang(tempListList);
					List<List<String>> locationsTemp = new ArrayList<List<String>>();
					locationsTemp.add(listLatitudes);//repo0
					eag2012.setLocationLatitude(locationsTemp);
					locationsTemp = new ArrayList<List<String>>();
					locationsTemp.add(listLongitudes);
					eag2012.setLocationLongitude(locationsTemp);
				}
			}
			JSONObject postalAddress = yourInstitution.getJSONObject("postalAddress");
			if(postalAddress!=null){
				List<String> listStreets = new ArrayList<String>();
				List<String> listLangStreets = new ArrayList<String>();
				List<String> listCities = new ArrayList<String>();
				for(int i=0;i<postalAddress.length();i++){
					JSONObject postalAddressTable = postalAddress.getJSONObject("yiTablePostalAddress_"+(i+1));
					listStreets.add(postalAddressTable.getString("textYIPAStreet"));
					listLangStreets.add(postalAddressTable.getString("selectYIPASelectLanguage"));
					listCities.add(postalAddressTable.getString("textYIPACity"));
				}
				List<List<String>> streetValueList = eag2012.getStreetValue();
				streetValueList.add(listStreets);
				List<List<String>> streetLangList = eag2012.getStreetLang();
				streetLangList.add(listLangStreets);
				List<List<String>> municipalityPostalcodeValueList = eag2012.getMunicipalityPostalcodeValue();
				municipalityPostalcodeValueList.add(listCities);
				//update lists
				eag2012.setStreetValue(streetValueList);
				eag2012.setStreetLang(streetLangList);
				eag2012.setMunicipalityPostalcodeValue(municipalityPostalcodeValueList);
			}
			//your institution - last part
			
			if(yourInstitution.has("textYITelephone")){
				Map<String, List<String>> telephones = new HashMap<String,List<String>>();
				ArrayList<String> listTelephones = new ArrayList<String>();
				listTelephones.add(yourInstitution.getString("textYITelephone"));
				telephones.put(Eag2012Creator.TAB_YOUR_INSTITUTION,listTelephones);
				List<Map<String, List<String>>> listMapTelephonesList = new ArrayList<Map<String, List<String>>>(); 
				listMapTelephonesList.add(telephones);
				eag2012.setTelephoneValue(listMapTelephonesList); //first repo (index_0) (your_institution), first tab (index_TAB_YOUR_INSTITUTION), unique element {list.set(telephone) }
			}
			if(yourInstitution.has("textYIEmailAddress")){
				Map<String, List<String>> mails = new HashMap<String,List<String>>();
				ArrayList<String> listMails = new ArrayList<String>();
				listMails.add(yourInstitution.getString("textYIEmailAddress"));
				mails.put(Eag2012Creator.TAB_YOUR_INSTITUTION,listMails);
				List<Map<String, List<String>>> listMapMailsList = new ArrayList<Map<String, List<String>>>();
				listMapMailsList.add(mails);
				eag2012.setEmailHref(listMapMailsList); //first repo (index_0) (your_institution), first tab (index_TAB_YOUR_INSTITUTION), unique element {list.set(mailsHref) }
			}
			if(yourInstitution.has("textYIEmailLinkTitle")){
				Map<String, List<String>> mailsTitle = new HashMap<String,List<String>>();
				ArrayList<String> listMailsTitle = new ArrayList<String>();
				listMailsTitle.add(yourInstitution.getString("textYIEmailLinkTitle"));
				mailsTitle.put(Eag2012Creator.TAB_YOUR_INSTITUTION,listMailsTitle);
				List<Map<String, List<String>>> listMailsTitleList = new ArrayList<Map<String, List<String>>>();
				listMailsTitleList.add(mailsTitle);
				eag2012.setEmailValue(listMailsTitleList); //first repo (index_0) (your_institution), first tab (index_TAB_YOUR_INSTITUTION), unique element {list.set(mailsTitle) }
			}
			if(yourInstitution.has("textYIWebpage")){
				Map<String, List<String>> webpageLinkHref = new HashMap<String,List<String>>();
				ArrayList<String> listMailsWebpageHref = new ArrayList<String>();
				listMailsWebpageHref.add(yourInstitution.getString("textYIWebpage"));
				webpageLinkHref.put(Eag2012Creator.TAB_YOUR_INSTITUTION,listMailsWebpageHref);
				List<Map<String, List<String>>> listWebpageLinkHref = new ArrayList<Map<String, List<String>>>(); 
				eag2012.setWebpageHref(listWebpageLinkHref); //first repo (index_0) (your_institution), first tab (index_TAB_YOUR_INSTITUTION), unique element {list.set(webpageHref) }
			}
			if(yourInstitution.has("textYIWebpageLinkTitle")){
				Map<String, List<String>> webpageValue = new HashMap<String,List<String>>();
				ArrayList<String> listWebpage = new ArrayList<String>();
				listWebpage.add(yourInstitution.getString("textYIWebpageLinkTitle"));
				webpageValue.put(Eag2012Creator.TAB_YOUR_INSTITUTION,listWebpage);
				List<Map<String, List<String>>> listWebpageValue = new ArrayList<Map<String, List<String>>>();
				listWebpageValue.add(webpageValue);
				eag2012.setWebpageValue(listWebpageValue); //first repo (index_0) (your_institution), first tab (index_TAB_YOUR_INSTITUTION), unique element {list.set(webpageTitle) }
			}
			if(yourInstitution.has("textYIOpeningTimes")){
				Map<String, String> openingMap = new HashMap<String,String>();
				openingMap.put(Eag2012Creator.TAB_YOUR_INSTITUTION, yourInstitution.getString("textYIOpeningTimes"));
				List<Map<String, String>> openingValues = new ArrayList<Map<String,String>>();
				openingValues.add(openingMap);
				eag2012.setOpeningValue(openingValues); //first repo (index_0) (your_institution), first tab (index_TAB_YOUR_INSTITUTION), unique element {opening}
			}
			if(yourInstitution.has("yourInstitutionClosingDates")){
				Map<String, String> closingMap = new HashMap<String,String>();
				closingMap.put(Eag2012Creator.TAB_YOUR_INSTITUTION, yourInstitution.getString("yourInstitutionClosingDates"));
				List<Map<String, String>> closingValues = new ArrayList<Map<String,String>>();
				closingValues.add(closingMap);
				eag2012.setClosingValue(closingValues); //first repo (index_0) (your_institution), first tab (index_TAB_YOUR_INSTITUTION), unique element {opening}
				eag2012.setClosingStandardDate(closingValues); //first repo (index_0) (your_institution), first tab (index_TAB_YOUR_INSTITUTION), unique element {closing}
			}
			if(yourInstitution.has("selectAccessibleToThePublic")){
				eag2012.setAccessQuestion(yourInstitution.getString("selectAccessibleToThePublic"));
			}
			if(yourInstitution.has("futherAccessInformation")){
				List<List<String>> furtherAccessInformationListList = new ArrayList<List<String>>();
				List<String> furtherAccessInformationList = new ArrayList<String>();
				furtherAccessInformationList.add(yourInstitution.getString("futherAccessInformation"));
				furtherAccessInformationListList.add(furtherAccessInformationList);
				eag2012.setRestaccessValue(furtherAccessInformationListList); //first repo (index_0) (your_institution), unique element {list.furtherAccessInformation}
			}
			if(yourInstitution.has("futherInformationOnExistingFacilities")){
				List<List<String>> futherInformationOnExistingFacilitiesListList = new ArrayList<List<String>>();
				List<String> futherInformationOnExistingFacilitiesList = new ArrayList<String>();
				futherInformationOnExistingFacilitiesList.add(yourInstitution.getString("futherInformationOnExistingFacilities"));
				futherInformationOnExistingFacilitiesListList.add(futherInformationOnExistingFacilitiesList); 
				eag2012.setAccessibilityValue(futherInformationOnExistingFacilitiesListList);//first repo (index_0) (your_institution), unique element {list.futherInformationOnExistingFacilities}
			}
			if(yourInstitution.has("textReferencetoyourinstitutionsholdingsguide")){
				String resourceRelationHref = yourInstitution.getString("textReferencetoyourinstitutionsholdingsguide");
				List<String> resourceRelationHrefList = new ArrayList<String>();
				resourceRelationHrefList.add(resourceRelationHref);
				eag2012.setResourceRelationHref(resourceRelationHrefList);
			}
			if(yourInstitution.has("textYIHoldingsGuideLinkTitle")){
				String relationEntry = yourInstitution.getString("textYIHoldingsGuideLinkTitle");
				List<String> relationEntryList = new ArrayList<String>();
				relationEntryList.add(relationEntry);
				eag2012.setRelationEntryValue(relationEntryList);
			}
			if(yourInstitution.has("selectYIContinent")){
				String continent = yourInstitution.getString("selectYIContinent");
				eag2012.setGeogareaValue(continent);
			}
			if(yourInstitution.has("selectFacilitiesForDisabledPeopleAvailable")){
				List<List<String>> accessibilityQuestionListList = new ArrayList<List<String>>();
				List<String> accessibilityQuestionList = new ArrayList<String>();
				accessibilityQuestionList.add(yourInstitution.getString("selectFacilitiesForDisabledPeopleAvailable"));
				accessibilityQuestionListList.add(accessibilityQuestionList);
				eag2012.setAccessibilityQuestion(accessibilityQuestionListList);
			}
		}
		return eag2012;
	}
}
