package eu.apenet.dashboard.manual.eag;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.SAXException;

import eu.apenet.commons.exceptions.APEnetException;
import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.dashboard.AbstractInstitutionAction;
import eu.apenet.dashboard.archivallandscape.ArchivalLandscape;
import eu.apenet.dashboard.manual.APEnetEAGDashboard;
import eu.apenet.dashboard.manual.eag.utils.CreateEAG2012;
import eu.apenet.dashboard.manual.eag.utils.EAG2012Loader;
import eu.apenet.dpt.utils.eag2012.Eag;
import eu.apenet.dpt.utils.eag2012.namespace.EagNamespaceMapper;
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

    private Map<String,String> yesNoMap = new HashMap<String,String>();
    private Map<String,String> typeOfInstitutionMap = new LinkedHashMap<String,String>();
    private Map<String,String> continentOfInstitutionMap = new LinkedHashMap<String,String>();
    private Map<String,String> photographMap = new LinkedHashMap<String,String>();
    private Map<String,String> typeYourRelationMap = new LinkedHashMap<String,String>();
    private Map<String,String> typeTheRelationMap = new LinkedHashMap<String,String>();
    private Map<String,String> languageISOMap = new LinkedHashMap<String,String>();
    private Map<String,String> scriptMap = new LinkedHashMap<String,String>();

    private List<String> warnings = new ArrayList<String>();

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

	private String eagPath;
	
	public String getEagPath(){
		return this.eagPath;
	}

	public Map<String,String> getLanguageList() {
		Map<String,String> languages = new LinkedHashMap<String,String>();
		languages.put(Eag2012.OPTION_NONE, "---");

		List<String> languagesList = LanguageIsoList.getLanguageIsoList();
		String[] isoLanguages = languagesList.toArray(new String[]{});
		for (String language : isoLanguages) {
			String languageDescription = LanguageIsoList.getIsoCode(language);
			languages.put(languageDescription, language);
		}

		return languages;
	}

	public Map<String,String> getYesNoList() {
		this.getYesNoMap().put(Eag2012.OPTION_YES, getText("label.ai.tabs.commons.option.yes"));
		this.getYesNoMap().put(Eag2012.OPTION_NO, getText("label.ai.tabs.commons.option.no"));
		return this.getYesNoMap();
	}

	public Map<String,String> getTypeOfInstitutionList() {
		this.getTypeOfInstitutionMap().put(Eag2012.OPTION_NATIONAL,getText("label.ai.tabs.commons.option.institutionType.nationalArchives"));
		this.getTypeOfInstitutionMap().put(Eag2012.OPTION_REGIONAL,getText("label.ai.tabs.commons.option.institutionType.regionalArchives"));
		this.getTypeOfInstitutionMap().put(Eag2012.OPTION_COUNTY,getText("label.ai.tabs.commons.option.institutionType.countyArchives"));
		this.getTypeOfInstitutionMap().put(Eag2012.OPTION_MUNICIPAL,getText("label.ai.tabs.commons.option.institutionType.municipalArchives"));
		this.getTypeOfInstitutionMap().put(Eag2012.OPTION_SPECIALISED,getText("label.ai.tabs.commons.option.institutionType.specialisedArchives"));
		this.getTypeOfInstitutionMap().put(Eag2012.OPTION_PRIVATE,getText("label.ai.tabs.commons.option.institutionType.privateArchives"));
		this.getTypeOfInstitutionMap().put(Eag2012.OPTION_CHURCH,getText("label.ai.tabs.commons.option.institutionType.churchArchives"));
		this.getTypeOfInstitutionMap().put(Eag2012.OPTION_BUSINESS,getText("label.ai.tabs.commons.option.institutionType.businessArchives"));
		this.getTypeOfInstitutionMap().put(Eag2012.OPTION_UNIVERSITY,getText("label.ai.tabs.commons.option.institutionType.universityArchives"));
		this.getTypeOfInstitutionMap().put(Eag2012.OPTION_MEDIA,getText("label.ai.tabs.commons.option.institutionType.mediaArchives"));
		this.getTypeOfInstitutionMap().put(Eag2012.OPTION_POLITICAL,getText("label.ai.tabs.commons.option.institutionType.politicalArchives"));
		this.getTypeOfInstitutionMap().put(Eag2012.OPTION_CULTURAL,getText("label.ai.tabs.commons.option.institutionType.culturalArchives"));
		return this.getTypeOfInstitutionMap();
	}

	public Map<String,String> getContinentOfTheInstitutionList() {
		this.getContinentOfInstitutionMap().put(Eag2012.OPTION_EUROPE,getText("label.ai.tabs.commons.option.continent.europe"));
		this.getContinentOfInstitutionMap().put(Eag2012.OPTION_AFRICA,getText("label.ai.tabs.commons.option.continent.africa"));
		this.getContinentOfInstitutionMap().put(Eag2012.OPTION_ANTARCTICA,getText("label.ai.tabs.commons.option.continent.antarctica"));
		this.getContinentOfInstitutionMap().put(Eag2012.OPTION_ASIA,getText("label.ai.tabs.commons.option.continent.asia"));
		this.getContinentOfInstitutionMap().put(Eag2012.OPTION_AUSTRALIA,getText("label.ai.tabs.commons.option.continent.australia"));
		this.getContinentOfInstitutionMap().put(Eag2012.OPTION_NORTH_AMERICA,getText("label.ai.tabs.commons.option.continent.northAmerica"));
		this.getContinentOfInstitutionMap().put(Eag2012.OPTION_SOUTH_AMERICA,getText("label.ai.tabs.commons.option.continent.southAmerica"));

		return this.getContinentOfInstitutionMap();
	}

	public Map<String,String> getPhotographList() {
		this.getPhotographMap().put(Eag2012.OPTION_NONE, "---");
		this.getPhotographMap().put(Eag2012.OPTION_DEPENDING,getText("label.ai.tabs.commons.option.photograph.depending"));
		this.getPhotographMap().put(Eag2012.OPTION_YES, getText("label.ai.tabs.commons.option.yes"));
		this.getPhotographMap().put(Eag2012.OPTION_WITHOUT,getText("label.ai.tabs.commons.option.photograph.without"));
		this.getPhotographMap().put(Eag2012.OPTION_NO, getText("label.ai.tabs.commons.option.no"));

		return this.getPhotographMap();
	}

	public Map<String,String> getTypeYourRelationList() {
		this.getTypeYourRelationMap().put(Eag2012.OPTION_CREATOR,getText("label.ai.tabs.commons.option.typeYourRelation.creator"));
		this.getTypeYourRelationMap().put(Eag2012.OPTION_SUBJECT,getText("label.ai.tabs.commons.option.typeYourRelation.subject"));
		this.getTypeYourRelationMap().put(Eag2012.OPTION_OTHER,getText("label.ai.tabs.commons.option.typeYourRelation.other"));

		return this.getTypeYourRelationMap();
	}

	public Map<String,String> getTypeTheRelationList() {
		this.getTypeTheRelationMap().put(Eag2012.OPTION_NONE, "---");
		this.getTypeTheRelationMap().put(Eag2012.OPTION_CHILD, getText("label.ai.tabs.commons.option.typeTheRelation.child"));
		this.getTypeTheRelationMap().put(Eag2012.OPTION_PARENT, getText("label.ai.tabs.commons.option.typeTheRelation.parent"));
		this.getTypeTheRelationMap().put(Eag2012.OPTION_EARLIER,getText("label.ai.tabs.commons.option.typeTheRelation.earlier"));
		this.getTypeTheRelationMap().put(Eag2012.OPTION_LATER,getText("label.ai.tabs.commons.option.typeTheRelation.later"));
		this.getTypeTheRelationMap().put(Eag2012.OPTION_ASSOCIATIVE,getText("label.ai.tabs.commons.option.typeTheRelation.associative"));
		return this.getTypeTheRelationMap();
	}

	public Map<String,String> getScriptList() {
		this.getScriptMap().put(Eag2012.OPTION_NONE, "---");

		this.getScriptMap().put(Eag2012.OPTION_SCRIPT_ARAB, Eag2012.OPTION_SCRIPT_TEXT_ARAB);
		this.getScriptMap().put(Eag2012.OPTION_SCRIPT_ARMN, Eag2012.OPTION_SCRIPT_TEXT_ARMN);
		this.getScriptMap().put(Eag2012.OPTION_SCRIPT_CPRT, Eag2012.OPTION_SCRIPT_TEXT_CPRT);
		this.getScriptMap().put(Eag2012.OPTION_SCRIPT_CYRL, Eag2012.OPTION_SCRIPT_TEXT_CYRL);
		this.getScriptMap().put(Eag2012.OPTION_SCRIPT_GEOR, Eag2012.OPTION_SCRIPT_TEXT_GEOR);
		this.getScriptMap().put(Eag2012.OPTION_SCRIPT_GREK, Eag2012.OPTION_SCRIPT_TEXT_GREK);
		this.getScriptMap().put(Eag2012.OPTION_SCRIPT_HEBR, Eag2012.OPTION_SCRIPT_TEXT_HEBR);
		this.getScriptMap().put(Eag2012.OPTION_SCRIPT_LATN, Eag2012.OPTION_SCRIPT_TEXT_LATN);

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

	/**
	 * @return the warnings
	 */
	public List<String> getWarnings() {
		return this.warnings;
	}

	/**
	 * @param warnings the warnings to set
	 */
	public void setWarnings(List<String> warnings) {
		this.warnings = warnings;
	}

	@Override
	public String execute() throws Exception {
		ArchivalInstitution archivalInstitution = null;
		String state = SUCCESS;
		try {
			archivalInstitution = DAOFactory.instance().getArchivalInstitutionDAO().getArchivalInstitution(getAiId());
			if(archivalInstitution!=null){
				eagPath = archivalInstitution.getEagPath();
				if(eagPath!=null && !eagPath.isEmpty()){
					state = INPUT;
				}
				String basePath = APEnetUtilities.FILESEPARATOR + this.getCountryCode() + APEnetUtilities.FILESEPARATOR +
						this.getAiId() + APEnetUtilities.FILESEPARATOR + Eag2012.EAG_PATH + APEnetUtilities.FILESEPARATOR;
				String tempPath = basePath + Eag2012.EAG_TEMP_FILE_NAME;
				if (new File(APEnetUtilities.getConfig().getRepoDirPath() + tempPath).exists()) {
					this.loader = new EAG2012Loader(getAiId());
					this.loader.fillEag2012();
					log.info("Loader: "+this.loader.toString()+" has been charged.");

					state = INPUT;
				}
			}
		} catch (Exception e) {
			log.error("Show/Edit EAG2012 Exception: "+e.getMessage());
		}
		if(state.equals(SUCCESS)){
			fillDefaultLoaderValues();
		}else{
			newEag = false;
		}
		return state;
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

	private EAG2012Loader loader;

	private boolean newEag;
	
	public Boolean isNewEag(){
		return this.newEag;
	}
	
	public EAG2012Loader getLoader(){
		return this.loader;
	}
	
	public void setForm(String form){
		this.form = form;
	}
	
	public String getForm(){
		return this.form;
	}

	public String editWebFormEAG2012(){
		String basePath = APEnetUtilities.FILESEPARATOR + this.getCountryCode() + APEnetUtilities.FILESEPARATOR +
				this.getAiId() + APEnetUtilities.FILESEPARATOR + Eag2012.EAG_PATH + APEnetUtilities.FILESEPARATOR;
		ArchivalInstitution archivalInstitution = DAOFactory.instance().getArchivalInstitutionDAO().getArchivalInstitution(getAiId());
		String path = archivalInstitution.getEagPath();
		String tempPath = basePath + Eag2012.EAG_TEMP_FILE_NAME;
		String state = INPUT;
		if (new File(APEnetUtilities.getConfig().getRepoDirPath() + tempPath).exists()){
			newEag = false;
			this.loader = new EAG2012Loader(getAiId());
			this.loader.fillEag2012();
			log.info("Loader: "+this.loader.toString()+" has been charged.");

			try {
				// Check if messages already added.
				if (getActionMessages() == null || getActionMessages().isEmpty()) {
					log.debug("Beginning EAG validation");
					APEnetEAGDashboard apEnetEAGDashboard = new APEnetEAGDashboard(this.getAiId(), new File(APEnetUtilities.getConfig().getRepoDirPath() + tempPath).getAbsolutePath());
					if (!apEnetEAGDashboard.validate()) {
						this.setWarnings(apEnetEAGDashboard.showWarnings());
						//The EAG has been neither validated nor converted.
						log.warn("The file " + new File(APEnetUtilities.getConfig().getRepoDirPath() + tempPath).getName() + " is not valid");
						for (int i = 0; i < this.getWarnings().size(); i++) {
							String warning = this.getWarnings().get(i).replace("<br/>", "");
							log.debug(warning);
							if (!warning.contains("for type 'recordId'")) {
								addActionMessage(warning);
							}
							if (warning.contains("of element 'recordId' is not valid")) {
								this.loader.setRecordId(this.getIdUsedInAPE());
								this.loader.setRecordIdISIL(Eag2012.OPTION_NO);
								addActionMessage(this.getText("label.ai.error.defaultIdUsedInAPE") + " ("+ this.getIdUsedInAPE() +")");
							}
							if (warning.contains("recordId already used")) {
								this.loader.setRecordId(this.getIdUsedInAPE());
								this.loader.setRecordIdISIL(Eag2012.OPTION_NO);
								addActionMessage(this.getText("label.ai.error.defaultIdUsedInAPE") + " ("+ this.getIdUsedInAPE() +")");
							}
						}
					}
				} else {
					Iterator<String> warningsIt = this.getActionMessages().iterator();
					while (warningsIt.hasNext()) {
						String warning = warningsIt.next();
						if (warning.contains("of element 'recordId' is not valid")
								|| warning.contains("recordId already used")) {
							this.loader.setRecordId(this.getIdUsedInAPE());
							this.loader.setRecordIdISIL(Eag2012.OPTION_NO);
						}
					}
				}
			} catch (SAXException saxE) {
				log.error(saxE.getMessage(),saxE);
			} catch (APEnetException apeE) {
				log.error(apeE.getMessage(),apeE);
			}
		} else if(new File(APEnetUtilities.getConfig().getRepoDirPath() + path).exists()){
			newEag = false;
			this.loader = new EAG2012Loader(getAiId());
			this.loader.fillEag2012();
			log.info("Loader: "+this.loader.toString()+" has been charged.");
		}else{
			newEag = true;
			try {
				execute(); //default action
			} catch (Exception e) {
				log.error("ERROR trying to launch default action (execute): "+e.getCause());
			}
			state = SUCCESS;
		}
		return state;
	}
	
	public String createEAG2012() {
		Eag2012 eag2012 = null;
		try{
			eag2012 = getAndFillEag2012Object();
		}catch (JSONException e) {
			log.error(e.getMessage());
		}
		if(eag2012!=null){
			String basePath = APEnetUtilities.FILESEPARATOR + this.getCountryCode() + APEnetUtilities.FILESEPARATOR +
					this.getAiId() + APEnetUtilities.FILESEPARATOR + Eag2012.EAG_PATH + APEnetUtilities.FILESEPARATOR;
			String tempPath = basePath + Eag2012.EAG_TEMP_FILE_NAME;

			// Load XML.
			Eag eag = null;
			ArchivalInstitution archivalInstitution = DAOFactory.instance().getArchivalInstitutionDAO().getArchivalInstitution(getAiId());
			String loadPath = archivalInstitution.getEagPath();
			File eagFile = new File((APEnetUtilities.getConfig().getRepoDirPath() + tempPath));

			if (!eagFile.exists() && loadPath != null && !loadPath.isEmpty()) {
				eagFile = new File((APEnetUtilities.getConfig().getRepoDirPath() + loadPath));
			}

			if (eagFile.exists()) {
				try {
					InputStream eagStream = FileUtils.openInputStream(eagFile);

					JAXBContext jaxbContext = JAXBContext.newInstance(Eag.class);
					Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		            eag = (Eag) jaxbUnmarshaller.unmarshal(eagStream);

		            eagStream.close();		
				} catch (JAXBException jaxbe) {
					log.info(jaxbe.getMessage());
				} catch (IOException ioe) {
					log.info(ioe.getMessage());
				}
			}

			// Fill EAG2012 JAXB object.
			CreateEAG2012 createEAG2012 = new CreateEAG2012(eag2012, eag);
			eag = createEAG2012.fillEAG2012();

			// Save XML.
			try {
				String path = basePath + eag.getControl().getRecordId().getValue().replaceAll("[^a-zA-Z0-9\\-\\.]", "_") + ".xml";

				JAXBContext jaxbContext = JAXBContext.newInstance(Eag.class);
				Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
				jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
				jaxbMarshaller.setProperty("com.sun.xml.bind.namespacePrefixMapper", new EagNamespaceMapper());
				// Save in a temporal file.
				File eagTempFile = new File((APEnetUtilities.getConfig().getRepoDirPath() + tempPath));

				if (!eagTempFile.exists()) {
					FileUtils.writeStringToFile(eagTempFile, "new file");
				}

				jaxbMarshaller.marshal(eag, eagTempFile);

				// It is necessary to validate the file against APEnet EAG schema.
				log.debug("Beginning EAG validation");
				APEnetEAGDashboard apEnetEAGDashboard = new APEnetEAGDashboard(this.getAiId(), eagTempFile.getAbsolutePath());
				if (apEnetEAGDashboard.validate()) {
					log.info("EAG is valid");

					// Move temp file to final file.
					File eagFinalFile = new File((APEnetUtilities.getConfig().getRepoDirPath() + path));
					try {
						FileUtils.forceDelete(eagFinalFile);
					} catch (IOException e) {
						log.error(e.getMessage(),e);
					}
					FileUtils.moveFile(eagTempFile, eagFinalFile);

					//store ddbb path
					ArchivalInstitutionDAO archivalInstitutionDao = DAOFactory.instance().getArchivalInstitutionDAO();
					archivalInstitution = archivalInstitutionDao.getArchivalInstitution(getAiId());
					if(archivalInstitution!=null){
						archivalInstitution.setEagPath(path);
						archivalInstitution.setRepositorycode(eag2012.getRecordIdValue());
						archivalInstitutionDao.store(archivalInstitution);
						log.info("EAG2012 stored to "+path);
					}else{
						log.error("Could not be stored EAG2012 path, reason: null archival institution");
					}
				} else {
					this.setWarnings(apEnetEAGDashboard.showWarnings());
					//The EAG has been neither validated nor converted.
					log.warn("The file " + eagFile.getName() + " is not valid");
					for (int i = 0; i < this.getWarnings().size(); i++) {
						String warning = this.getWarnings().get(i).replace("<br/>", "");
						log.debug(warning);
						if (!warning.contains("for type 'recordId'")) {
							addActionMessage(warning);
						}
						if (warning.contains("of element 'recordId' is not valid")) {
							addActionMessage(this.getText("label.ai.error.defaultIdUsedInAPE") + " ("+ this.getIdUsedInAPE() +")");
						}
						if (warning.contains("recordId already used")) {
							addActionMessage(this.getText("label.ai.error.defaultIdUsedInAPE") + " ("+ this.getIdUsedInAPE() +")");
						}
					}
				}
			} catch (JAXBException jaxbe) {
				log.info(jaxbe.getMessage());
			} catch (APEnetException e) {
				log.error(e.getMessage(),e);
			} catch (SAXException e) {
				log.error(e.getMessage());
			} catch (IOException e) {
				log.error(e.getMessage(),e);
			}
		}
		return this.editWebFormEAG2012();
	}

	private void fillDefaultLoaderValues() { //TODO, now only works with main repository
		newEag = true;
		loader = new EAG2012Loader();
		//your institution
		loader.setAgent(getPersonResponsibleForDescription());
		loader.setCountryCode(getCountryCode());
		loader.setOtherRepositorId(getIdOfInstitution());
		loader.setRecordId(getIdUsedInAPE());
		loader.setAutform(getNameOfInstitution());
		loader.setParform(getParallelNameOfInstitution());
		//identity
//		loader.setCountryCode(getCountryCode());
//		loader.setOtherRepositorId(getIdOfInstitution());
//		loader.setRecordId(getIdUsedInAPE());
//		loader.setAutform(getNameOfInstitution());
//		loader.setParform(getParallelNameOfInstitution());
		//contact
//		loader.setStreet(getStreetOfTheInstitution());
//		loader.setMunicipalityPostalcode(getCityOfTheInstitution());
//		loader.setLocalentity(getDistrictOfTheInstitution());
//		loader.setSecondem(getCountyOfTheInstitution());
//		loader.setFirstdem(getRegionOfTheInstitution());
//		loader.setCountry(getCountryOfTheInstitution());
//		loader.setLatitude(getLatitudeOfTheInstitution());
//		loader.setLongitude(getLongitudeOfTheInstitution());
//		loader.setTelephone(getTelephoneOfTheInstitution());
//		loader.setFax(getFaxOfTheInstitution());
//		loader.setEmail(getEmailOfTheInstitution());
//		loader.setEmailTitle(getLinkTitleForEmailOfTheInstitution());
//		loader.setWebpage(getWebOfTheInstitution());
//		loader.setWebpageTitle(getLinkTitleForWebOfTheInstitution());
		//access and services
		//description
		//control
//		loader.setRecordId(getIdUsedInAPE());
//		loader.setAgent(getPersonResponsibleForDescription());
//		loader.setAbbreviation(getContactAbbreviation());
//		loader.setCitation(getContactFullName());
		//relations
//		loader.setResourceRelationHref(getWebsiteOfResource());
//		loader.setResourceRelationrelationEntry(getTitleOfRelatedMaterial());
//		loader.setResourceRelationrelationEntryDescription(getDescriptionOfRelation());
//		loader.setEagRelationHref(getWebsiteOfDescription());
//		loader.setEagRelationrelationEntry(getTitleOfRelatedInstitution());
//		loader.setEagRelationrelationEntryDescription(getInstitutionDescriptionOfRelation());
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
		
	        if(jsonObj.has("accessAndServices")){
	        	eag2012 = parseAccesAndServicesJsonObjToEag2012(eag2012, jsonObj);
	        } 		
		    if(jsonObj.has("description")){
		    	eag2012 = parseDescriptionJsonObjToEag2012(eag2012, jsonObj);
		    }
		    if(jsonObj.has("control")){
		    	eag2012 = parseControlJsonObjToEag2012(eag2012, jsonObj);
		    }
		    if(jsonObj.has("relations")){
		    	eag2012 = parseRelationJsonObjToEag2012(eag2012, jsonObj);
		    }
		}
		
		return eag2012;
	}
	private Eag2012 parseRelationJsonObjToEag2012(Eag2012 eag2012, JSONObject jsonObj) throws JSONException{
		JSONObject relation = jsonObj.getJSONObject("relations");
		if(relation!=null){
			//resourceRelation section
			if(relation.has("resourceRelations")){
				JSONObject resourceRelations = relation.getJSONObject("resourceRelations");
				int i=0;
				while(resourceRelations.has("resourceRelationTable_"+(++i))){
					JSONObject resourceRelationTable = resourceRelations.getJSONObject("resourceRelationTable_"+i);
					if(resourceRelationTable.has("textWebsiteOfResource")){
						List<String> website = eag2012.getResourceRelationHref();
						if(website==null){
							website = new ArrayList<String>();
						}
						website.add(resourceRelationTable.getString("textWebsiteOfResource"));
						eag2012.setResourceRelationHref(website);
					}
					if(resourceRelationTable.has("selectTypeOfYourRelation")){
						List<String> typeOfYourRelation = eag2012.getResourceRelationResourceRelationType();
						if(typeOfYourRelation==null){
							typeOfYourRelation = new ArrayList<String>();
						}
						typeOfYourRelation.add(resourceRelationTable.getString("selectTypeOfYourRelation"));
						eag2012.setResourceRelationResourceRelationType(typeOfYourRelation);
					}
					if(resourceRelationTable.has("textTitleOfRelatedMaterial")){
						List<Map<String, List<String>>> listRelationEnrtyMap = eag2012.getRelationEntryValue();
						if(listRelationEnrtyMap==null){
							listRelationEnrtyMap= new ArrayList<Map<String,List<String>>>();
						}
						Map<String,List<String>> relationEntryMap = null;
						if(listRelationEnrtyMap.size()>0 && listRelationEnrtyMap.get(0)!=null){
							relationEntryMap = listRelationEnrtyMap.get(0);
						}else{
							relationEntryMap = new HashMap<String,List<String>>();
						}
						List<String> relationEntryList =null;
//						if(relationEntryMap.size()>i && relationEntryMap.get(Eag2012.RESOURCE_RELATION)!=null){
//							relationEntryList= relationEntryMap.get(Eag2012.RESOURCE_RELATION);
						if(relationEntryMap.size()>0 && relationEntryMap.get(Eag2012.RESOURCE_RELATION)!=null){
							relationEntryList= relationEntryMap.get(Eag2012.RESOURCE_RELATION);
						}else{
							relationEntryList = new ArrayList<String>();
						}
						relationEntryList.add(resourceRelationTable.getString("textTitleOfRelatedMaterial"));
						relationEntryMap.put(Eag2012.RESOURCE_RELATION, relationEntryList);
					    if(listRelationEnrtyMap.size()>0){
					    	listRelationEnrtyMap.set(0, relationEntryMap);
					    }else{
					    	listRelationEnrtyMap.add(relationEntryMap);
					    }
					    eag2012.setRelationEntryValue(listRelationEnrtyMap);
					}
					if(resourceRelationTable.has("textDescriptionOfRelation")){
						List<Map<String, Map<String, List<String>>>> descriptiveNotePValue = eag2012.getDescriptiveNotePValue();
						 if(descriptiveNotePValue==null){
							 descriptiveNotePValue = new ArrayList<Map<String, Map<String, List<String>>>>();
						 }
						 Map<String, Map<String, List<String>>> descriptiveNoteMapMapList = null;
						 if(descriptiveNotePValue.size()>0 && descriptiveNotePValue.get(0)!=null){
							 descriptiveNoteMapMapList = descriptiveNotePValue.get(0);
						 }else{
							 descriptiveNoteMapMapList = new HashMap<String,Map<String,List<String>>>();
						 }
						 Map<String, List<String>> descriptiveNoteMapList = null;
						 if(descriptiveNoteMapMapList.size()>0 && descriptiveNoteMapMapList.get(Eag2012.TAB_RELATION)!=null){
							 descriptiveNoteMapList = descriptiveNoteMapMapList.get(Eag2012.TAB_RELATION);
						 }else{
							 descriptiveNoteMapList = new HashMap<String, List<String>>();
						 }
						 List<String> descriptiveNoteList = null;
						 if(descriptiveNoteMapList.size()>0 && descriptiveNoteMapList.get(Eag2012.RESOURCE_RELATION)!=null){
							 descriptiveNoteList = descriptiveNoteMapList.get(Eag2012.RESOURCE_RELATION);
						 }else{
							 descriptiveNoteList = new ArrayList<String>();
						 }
						 descriptiveNoteList.add(resourceRelationTable.getString("textDescriptionOfRelation"));
						 descriptiveNoteMapList.put(Eag2012.RESOURCE_RELATION,descriptiveNoteList);
						 descriptiveNoteMapMapList.put(Eag2012.TAB_RELATION,descriptiveNoteMapList);
						 if(descriptiveNotePValue.size()>0){
							 descriptiveNotePValue.set(0,descriptiveNoteMapMapList);
						 }else{
							 descriptiveNotePValue.add(descriptiveNoteMapMapList);
						 }
						 eag2012.setDescriptiveNotePValue(descriptiveNotePValue);
					 }
					if(resourceRelationTable.has("selectLanguageDescriptionOfRelation")){
						List<Map<String, Map<String, List<String>>>> descriptiveNotePValue = eag2012.getDescriptiveNotePLang();
						 if(descriptiveNotePValue==null){
							 descriptiveNotePValue = new ArrayList<Map<String, Map<String, List<String>>>>();
						 }
						 Map<String, Map<String, List<String>>> descriptiveNoteMapMapList = null;
						 if(descriptiveNotePValue.size()>0 && descriptiveNotePValue.get(0)!=null){
							 descriptiveNoteMapMapList = descriptiveNotePValue.get(0);
						 }else{
							 descriptiveNoteMapMapList = new HashMap<String,Map<String,List<String>>>();
						 }
						 Map<String, List<String>> descriptiveNoteMapList = null;
						 if(descriptiveNoteMapMapList.size()>0 && descriptiveNoteMapMapList.get(Eag2012.TAB_RELATION)!=null){
							 descriptiveNoteMapList = descriptiveNoteMapMapList.get(Eag2012.TAB_RELATION);
						 }else{
							 descriptiveNoteMapList = new HashMap<String, List<String>>();
						 }
						 List<String> descriptiveNoteList = null;
						 if(descriptiveNoteMapList.size()>0 && descriptiveNoteMapList.get(Eag2012.RESOURCE_RELATION)!=null){
							 descriptiveNoteList = descriptiveNoteMapList.get(Eag2012.RESOURCE_RELATION);
						 }else{
							 descriptiveNoteList = new ArrayList<String>();
						 }
						 descriptiveNoteList.add(resourceRelationTable.getString("selectLanguageDescriptionOfRelation"));
						 descriptiveNoteMapList.put(Eag2012.RESOURCE_RELATION,descriptiveNoteList);
						 descriptiveNoteMapMapList.put(Eag2012.TAB_RELATION,descriptiveNoteMapList);
						 if(descriptiveNotePValue.size()>0){
							 descriptiveNotePValue.set(0,descriptiveNoteMapMapList);
						 }else{
							 descriptiveNotePValue.add(descriptiveNoteMapMapList);
						 }
						 eag2012.setDescriptiveNotePLang(descriptiveNotePValue);
					 }	
				}//end while resourceRelations
			}//end if resourceRelation
			
			//institution Repository section
			if(relation.has("institutionRelations")){
				JSONObject institutionRelations = relation.getJSONObject("institutionRelations");
				int i=0;
				while(institutionRelations.has("institutionRelationTable_"+(++i))){
					JSONObject institutionRelationTable = institutionRelations.getJSONObject("institutionRelationTable_"+i);
					if(institutionRelationTable.has("textWebsiteOfDescription")){
						List<String> website = eag2012.getEagRelationHref();
						if(website==null){
							website = new ArrayList<String>();
						}
						website.add(institutionRelationTable.getString("textWebsiteOfDescription"));
						eag2012.setEagRelationHref(website);
					}
					if(institutionRelationTable.has("selectTypeOftheRelation")){
						List<String> typeOftheRelation = eag2012.getEagRelationEagRelationType();
						if(typeOftheRelation==null){
							typeOftheRelation = new ArrayList<String>();
						}
						typeOftheRelation.add(institutionRelationTable.getString("selectTypeOftheRelation"));
						eag2012.setEagRelationEagRelationType(typeOftheRelation);
					}
					if(institutionRelationTable.has("textTitleOfRelatedInstitution")){
						List<Map<String, List<String>>> listRelationEnrtyMap = eag2012.getRelationEntryValue();
						if(listRelationEnrtyMap==null){
							listRelationEnrtyMap= new ArrayList<Map<String,List<String>>>();
						}
						Map<String,List<String>> relationEntryMap = null;
						if(listRelationEnrtyMap.size()>0 && listRelationEnrtyMap.get(0)!=null){
							relationEntryMap = listRelationEnrtyMap.get(0);
						}else{
							relationEntryMap = new HashMap<String,List<String>>();
						}
						List<String> relationEntryList =null;
						if(relationEntryMap.size()>0 && relationEntryMap.get(Eag2012.INSTITUTION_RELATIONS)!=null){
							relationEntryList= relationEntryMap.get(Eag2012.INSTITUTION_RELATIONS);
						}else{
							relationEntryList = new ArrayList<String>();
						}
						relationEntryList.add(institutionRelationTable.getString("textTitleOfRelatedInstitution"));
						relationEntryMap.put(Eag2012.INSTITUTION_RELATIONS, relationEntryList);
					    if(listRelationEnrtyMap.size()>0){
					    	listRelationEnrtyMap.set(0, relationEntryMap);
					    }else{
					    	listRelationEnrtyMap.add(relationEntryMap);
					    }
					    eag2012.setRelationEntryValue(listRelationEnrtyMap);
					}
					if(institutionRelationTable.has("textInstitutionDescriptionOfRelation")){
						List<Map<String, Map<String, List<String>>>> descriptiveNotePValue = eag2012.getDescriptiveNotePValue();
						 if(descriptiveNotePValue==null){
							 descriptiveNotePValue = new ArrayList<Map<String, Map<String, List<String>>>>();
						 }
						 Map<String, Map<String, List<String>>> descriptiveNoteMapMapList = null;
						 if(descriptiveNotePValue.size()>0 && descriptiveNotePValue.get(0)!=null){
							 descriptiveNoteMapMapList = descriptiveNotePValue.get(0);
						 }else{
							 descriptiveNoteMapMapList = new HashMap<String,Map<String,List<String>>>();
						 }
						 Map<String, List<String>> descriptiveNoteMapList = null;
						 if(descriptiveNoteMapMapList.size()>0 && descriptiveNoteMapMapList.get(Eag2012.TAB_RELATION)!=null){
							 descriptiveNoteMapList = descriptiveNoteMapMapList.get(Eag2012.TAB_RELATION);
						 }else{
							 descriptiveNoteMapList = new HashMap<String, List<String>>();
						 }
						 List<String> descriptiveNoteList = null;
						 if(descriptiveNoteMapList.size()>0 && descriptiveNoteMapList.get(Eag2012.INSTITUTION_RELATIONS)!=null){
							 descriptiveNoteList = descriptiveNoteMapList.get(Eag2012.INSTITUTION_RELATIONS);
						 }else{
							 descriptiveNoteList = new ArrayList<String>();
						 }
						 descriptiveNoteList.add(institutionRelationTable.getString("textInstitutionDescriptionOfRelation"));
						 descriptiveNoteMapList.put(Eag2012.INSTITUTION_RELATIONS,descriptiveNoteList);
						 descriptiveNoteMapMapList.put(Eag2012.TAB_RELATION,descriptiveNoteMapList);
						 if(descriptiveNotePValue.size()>0){
							 descriptiveNotePValue.set(0,descriptiveNoteMapMapList);
						 }else{
							 descriptiveNotePValue.add(descriptiveNoteMapMapList);
						 }
						 eag2012.setDescriptiveNotePValue(descriptiveNotePValue);
					 }
					if(institutionRelationTable.has("selectLanguageInstitutionDescriptionOfRelation")){
						List<Map<String, Map<String, List<String>>>> descriptiveNotePValue = eag2012.getDescriptiveNotePLang();
						 if(descriptiveNotePValue==null){
							 descriptiveNotePValue = new ArrayList<Map<String, Map<String, List<String>>>>();
						 }
						 Map<String, Map<String, List<String>>> descriptiveNoteMapMapList = null;
						 if(descriptiveNotePValue.size()>0 && descriptiveNotePValue.get(0)!=null){
							 descriptiveNoteMapMapList = descriptiveNotePValue.get(0);
						 }else{
							 descriptiveNoteMapMapList = new HashMap<String,Map<String,List<String>>>();
						 }
						 Map<String, List<String>> descriptiveNoteMapList = null;
						 if(descriptiveNoteMapMapList.size()>0 && descriptiveNoteMapMapList.get(Eag2012.TAB_RELATION)!=null){
							 descriptiveNoteMapList = descriptiveNoteMapMapList.get(Eag2012.TAB_RELATION);
						 }else{
							 descriptiveNoteMapList = new HashMap<String, List<String>>();
						 }
						 List<String> descriptiveNoteList = null;
						 if(descriptiveNoteMapList.size()>0 && descriptiveNoteMapList.get(Eag2012.INSTITUTION_RELATIONS)!=null){
							 descriptiveNoteList = descriptiveNoteMapList.get(Eag2012.INSTITUTION_RELATIONS);
						 }else{
							 descriptiveNoteList = new ArrayList<String>();
						 }
						 descriptiveNoteList.add(institutionRelationTable.getString("selectLanguageInstitutionDescriptionOfRelation"));
						 descriptiveNoteMapList.put(Eag2012.INSTITUTION_RELATIONS,descriptiveNoteList);
						 descriptiveNoteMapMapList.put(Eag2012.TAB_RELATION,descriptiveNoteMapList);
						 if(descriptiveNotePValue.size()>0){
							 descriptiveNotePValue.set(0,descriptiveNoteMapMapList);
						 }else{
							 descriptiveNotePValue.add(descriptiveNoteMapMapList);
						 }
						 eag2012.setDescriptiveNotePLang(descriptiveNotePValue);
					 }
				}//end while institution
			}//end if institution
			
		}//end if relation
		
		return eag2012;
	}
	private Eag2012 parseControlJsonObjToEag2012(Eag2012 eag2012, JSONObject jsonObj) throws JSONException{
	    JSONObject control = jsonObj.getJSONObject("control");
		if(control!=null){
			if(control.has("textDescriptionIdentifier")){
				String recordId = eag2012.getRecordIdValue();
				if(recordId==null){
					recordId = control.getString("textDescriptionIdentifier");
					eag2012.setRecordIdValue(recordId);
				}
			}
			if(control.has("selectLanguagePesonresponsible")){
				String agentLang = eag2012.getAgentLang();
				if(agentLang==null){
					agentLang = control.getString("selectLanguagePesonresponsible");
					eag2012.setAgentLang(agentLang);
				}
			}
			if(control.has("textPesonResponsible")){
				String agentValue=control.getString("textPesonResponsible");
				eag2012.setAgentValue(agentValue);
			}
			int i = 1;
			while(control.has("selectDescriptionLanguage_"+i) || (control.has("selectDescriptionScript_"+i))){
				List<String> listLanguageCode = eag2012.getLanguageLanguageCode();
				if(listLanguageCode==null){
					listLanguageCode = new ArrayList<String>();
				}

				if(control.has("selectDescriptionLanguage_"+i)){
					listLanguageCode.add(control.getString("selectDescriptionLanguage_"+i));
					eag2012.setLanguageLanguageCode(listLanguageCode);
				}

				List<String> listScript = eag2012.getScriptScriptCode();
				if(listScript==null){
					listScript = new ArrayList<String>();
				}
				if(control.has("selectDescriptionScript_"+i)){
					listScript.add(control.getString("selectDescriptionScript_"+i));
					eag2012.setScriptScriptCode(listScript);
				}

				if (listLanguageCode.size() > listScript.size()) {
					listScript.add("");
				} else if (listLanguageCode.size() < listScript.size()) {
					listLanguageCode.add("");
				}

				i++;
			}
		  //convention declaration	
		  i=0;
		  while(control.has("textContactAbbreviation_"+(++i)) && (control.has("textContactFullName_"+i))){
			if(control.has("textContactAbbreviation_"+i)){
			  List<String> abbreviation = eag2012.getAbbreviationValue();
			  if(abbreviation == null){
				  abbreviation = new ArrayList<String>(); 
			  }
			  abbreviation.add(control.getString("textContactAbbreviation_"+i));	  
              eag2012.setAbbreviationValue(abbreviation);			
			}
			if(control.has("textContactFullName_"+i)){
				List<Map<String, List<String>>> listCitationMap = eag2012.getCitationValue();
				if(listCitationMap==null){
					listCitationMap= new ArrayList<Map<String,List<String>>>();
				}
				Map<String,List<String>> citationMap = null;
				if(listCitationMap.size()>0 && listCitationMap.get(0)!=null){
					citationMap = listCitationMap.get(0);
				}else{
					citationMap = new HashMap<String,List<String>>();
				}
				List<String> citationList =null;
				if(citationMap.size()>0 && citationMap.get(Eag2012.TAB_CONTROL)!=null){
					citationList= citationMap.get(Eag2012.TAB_CONTROL);
				}else{
					citationList = new ArrayList<String>();
				}
				citationList.add(control.getString("textContactFullName_"+i));
				citationMap.put(Eag2012.TAB_CONTROL, citationList);
			    if(listCitationMap.size()>0){
			    	listCitationMap.set(0, citationMap);
			    }else{
			    	listCitationMap.add(citationMap);
			    }
			    eag2012.setCitationValue(listCitationMap);
			}
			
		 }//end while
		}//end first if
		return eag2012;
	}
	
	private Eag2012 parseDescriptionJsonObjToEag2012(Eag2012 eag2012, JSONObject jsonObj) throws JSONException {
		JSONObject description = jsonObj.getJSONObject("description");
		if(description!=null){
			int i=0;
			while(description.has("descriptionTable_"+(i+1))){
				JSONObject descriptionTable = description.getJSONObject("descriptionTable_"+(i+1)); 
			    
				//repository description section
				
				int j=0;
				while (descriptionTable.has("textRepositoryHistory_"+(++j)) && (descriptionTable.has("selectLanguageRepositoryHistory_"+(j)))){
					if(descriptionTable.has("textRepositoryHistory_"+j)){
						List<Map<String, Map<String, List<String>>>> descriptiveNotePValue = eag2012.getDescriptiveNotePValue();
						 if(descriptiveNotePValue==null){
							 descriptiveNotePValue = new ArrayList<Map<String, Map<String, List<String>>>>();
						 }
						 Map<String, Map<String, List<String>>> descriptiveNoteMapMapList = null;
						 if(descriptiveNotePValue.size()>i && descriptiveNotePValue.get(i)!=null){
							 descriptiveNoteMapMapList = descriptiveNotePValue.get(i);
						 }else{
							 descriptiveNoteMapMapList = new HashMap<String,Map<String,List<String>>>();
						 }
						 Map<String, List<String>> descriptiveNoteMapList = null;
						 if(descriptiveNoteMapMapList.size()>0 && descriptiveNoteMapMapList.get(Eag2012.TAB_DESCRIPTION)!=null){
							 descriptiveNoteMapList = descriptiveNoteMapMapList.get(Eag2012.TAB_DESCRIPTION);
						 }else{
							 descriptiveNoteMapList = new HashMap<String, List<String>>();
						 }
						 List<String> descriptiveNoteList = null;
						 if(descriptiveNoteMapList.size()>0 && descriptiveNoteMapList.get(Eag2012.REPOSITORHIST)!=null){
							 descriptiveNoteList = descriptiveNoteMapList.get(Eag2012.REPOSITORHIST);
						 }else{
							 descriptiveNoteList = new ArrayList<String>();
						 }
						 descriptiveNoteList.add(descriptionTable.getString("textRepositoryHistory_"+j));
						 descriptiveNoteMapList.put(Eag2012.REPOSITORHIST,descriptiveNoteList);
						 descriptiveNoteMapMapList.put(Eag2012.TAB_DESCRIPTION,descriptiveNoteMapList);
						 if(descriptiveNotePValue.size()>i){
							 descriptiveNotePValue.set(i,descriptiveNoteMapMapList);
						 }else{
							 descriptiveNotePValue.add(descriptiveNoteMapMapList);
						 }
						 eag2012.setDescriptiveNotePValue(descriptiveNotePValue);
					 }
					if(descriptionTable.has("selectLanguageRepositoryHistory_"+j)){
						List<Map<String, Map<String, List<String>>>> descriptiveNotePValue = eag2012.getDescriptiveNotePLang();
						 if(descriptiveNotePValue==null){
							 descriptiveNotePValue = new ArrayList<Map<String, Map<String, List<String>>>>();
						 }
						 Map<String, Map<String, List<String>>> descriptiveNoteMapMapList = null;
						 if(descriptiveNotePValue.size()>i && descriptiveNotePValue.get(i)!=null){
							 descriptiveNoteMapMapList = descriptiveNotePValue.get(i);
						 }else{
							 descriptiveNoteMapMapList = new HashMap<String,Map<String,List<String>>>();
						 }
						 Map<String, List<String>> descriptiveNoteMapList = null;
						 if(descriptiveNoteMapMapList.size()>0 && descriptiveNoteMapMapList.get(Eag2012.TAB_DESCRIPTION)!=null){
							 descriptiveNoteMapList = descriptiveNoteMapMapList.get(Eag2012.TAB_DESCRIPTION);
						 }else{
							 descriptiveNoteMapList = new HashMap<String, List<String>>();
						 }
						 List<String> descriptiveNoteList = null;
						 if(descriptiveNoteMapList.size()>0 && descriptiveNoteMapList.get(Eag2012.REPOSITORHIST)!=null){
							 descriptiveNoteList = descriptiveNoteMapList.get(Eag2012.REPOSITORHIST);
						 }else{
							 descriptiveNoteList = new ArrayList<String>();
						 }
						 descriptiveNoteList.add(descriptionTable.getString("selectLanguageRepositoryHistory_"+j));
						 descriptiveNoteMapList.put(Eag2012.REPOSITORHIST,descriptiveNoteList);
						 descriptiveNoteMapMapList.put(Eag2012.TAB_DESCRIPTION,descriptiveNoteMapList);
						 if(descriptiveNotePValue.size()>i){
							 descriptiveNotePValue.set(i,descriptiveNoteMapMapList);
						 }else{
							 descriptiveNotePValue.add(descriptiveNoteMapMapList);
						 }
						 eag2012.setDescriptiveNotePLang(descriptiveNotePValue);
					 }	
				}
				//date y rule of repositorfound
				if(descriptionTable.has("textDateOfRepositoryFoundation")){  //date
					 List<Map<String,Map<String,Map<String,List<List<String>>>>>> dateValue = eag2012.getDateStandardDate();
					 if(dateValue==null){
						 dateValue = new ArrayList<Map<String,Map<String,Map<String,List<List<String>>>>>>();
					 }
					 Map<String, Map<String, Map<String, List<List<String>>>>> dateMapMapMap = null;
					 if(dateValue.size()>i && dateValue.get(i)!=null){
						 dateMapMapMap = dateValue.get(i);
					 }else{
						 dateMapMapMap = new HashMap<String, Map<String, Map<String, List<List<String>>>>>(); 
					 }
					 Map<String, Map<String, List<List<String>>>> dateMapMap = null;
					 if(dateMapMapMap.size()>0 && dateMapMapMap.get(Eag2012.TAB_DESCRIPTION)!=null){
						 dateMapMap= dateMapMapMap.get(Eag2012.TAB_DESCRIPTION);
					 }else{
						 dateMapMap = new HashMap<String, Map<String, List<List<String>>>>();
					 }
					 Map<String, List<List<String>>> datesMap = null;
					 if(dateMapMap.size()>0 && dateMapMap.get(Eag2012.REPOSITORHIST)!=null){
						 datesMap = dateMapMap.get(Eag2012.REPOSITORHIST);
					 }else{
						 datesMap = new HashMap<String, List<List<String>>>();
					 }
					 List<List<String>> datesList = null;
					 if(datesMap.size()>0 && datesMap.get(Eag2012.REPOSITOR_FOUND)!=null){
						 datesList= datesMap.get(Eag2012.REPOSITOR_FOUND);
					 }else{
						 datesList = new ArrayList<List<String>>();
					 }
					 List<String> dates = null;
					 if(datesList.size()>0 && datesList.get(0)!=null){
						 dates = datesList.get(0);
					 }else{
						 dates = new ArrayList<String>();
					 }
					 dates.add(descriptionTable.getString("textDateOfRepositoryFoundation"));
					 if(datesList.size()>0){
						 datesList.set(0, dates);
					 }else{
						 datesList.add(dates);
					 }
					 datesMap.put(Eag2012.REPOSITOR_FOUND,datesList);
					 dateMapMap.put(Eag2012.REPOSITORHIST, datesMap);
					 dateMapMapMap.put(Eag2012.TAB_DESCRIPTION, dateMapMap);
					 if(dateValue.size()>i){
						 dateValue.set(i, dateMapMapMap);
					 }else{
						 dateValue.add(dateMapMapMap);
					 }
					 eag2012.setDateStandardDate(dateValue);
				 }
				//rule of repositorfound
				j=0;
				while(descriptionTable.has("textRuleOfRepositoryFoundation_"+(++j)) && descriptionTable.has("selectLanguageRuleOfRepositoryFoundation_"+(j))) {
				 if(descriptionTable.has("textRuleOfRepositoryFoundation_"+j)){	
					List<Map<String, List<String>>> listRulesMap = eag2012.getRuleValue();
					if(listRulesMap==null){
						listRulesMap= new ArrayList<Map<String,List<String>>>();
					}
					Map<String,List<String>> rulesMap = null;
					if(listRulesMap.size()>i && listRulesMap.get(i)!=null){
						rulesMap = listRulesMap.get(i);
					}else{
						rulesMap = new HashMap<String,List<String>>();
					}
					List<String> rulesList =null;
					if(rulesMap.size()>i && rulesMap.get(Eag2012.REPOSITOR_FOUND)!=null){
						rulesList= rulesMap.get(Eag2012.REPOSITOR_FOUND);
					}else{
						rulesList = new ArrayList<String>();
					}
					rulesList.add(descriptionTable.getString("textRuleOfRepositoryFoundation_"+j));
				    rulesMap.put(Eag2012.REPOSITOR_FOUND, rulesList);
				    if(listRulesMap.size()>i){
				    	listRulesMap.set(i, rulesMap);
				    }else{
				    	listRulesMap.add(rulesMap);
				    }
				    eag2012.setRuleValue(listRulesMap);
				 }	
				 if(descriptionTable.has("selectLanguageRuleOfRepositoryFoundation_"+j)){
					 List<Map<String,List<String>>> listRulesLang =eag2012.getRuleLang();
					 if(listRulesLang==null){
						 listRulesLang = new ArrayList<Map<String,List<String>>>();
					 }
					 Map<String,List<String>> rulesLangMap = null;
					 if(listRulesLang.size()>i && listRulesLang.get(i)!=null){
						 rulesLangMap = listRulesLang.get(i);
					 }else{
						 rulesLangMap = new HashMap<String,List<String>>();
					 }
					 List<String> rulesLangList = null;
					 if(rulesLangMap.size()>i && rulesLangMap.get(Eag2012.REPOSITOR_FOUND)!=null){
						rulesLangList=rulesLangMap.get(Eag2012.REPOSITOR_FOUND); 
					 }else{
						 rulesLangList = new ArrayList<String>();
					 }
					 rulesLangList.add(descriptionTable.getString("selectLanguageRuleOfRepositoryFoundation_"+j));
					 rulesLangMap.put(Eag2012.REPOSITOR_FOUND, rulesLangList);
					 if(listRulesLang.size()>i){
						 listRulesLang.set(i,rulesLangMap);
					 }else{
						 listRulesLang.add(rulesLangMap);
					 }
				    eag2012.setRuleLang(listRulesLang);
				 }	
				}
				
				//date of repositorsup
				
				if(descriptionTable.has("textDateOfRepositorySuppression")){  
					 List<Map<String, Map<String, Map<String, List<List<String>>>>>> dateValue = eag2012.getDateStandardDate();
					 if(dateValue==null){
						 dateValue = new ArrayList<Map<String, Map<String, Map<String, List<List<String>>>>>>();
					 }
					 Map<String, Map<String, Map<String, List<List<String>>>>> dateMapMapMap = null;
					 if(dateValue.size()>i && dateValue.get(i)!=null){
						 dateMapMapMap = dateValue.get(i);
					 }else{
						 dateMapMapMap = new HashMap<String, Map<String, Map<String, List<List<String>>>>>(); 
					 }
					 Map<String, Map<String, List<List<String>>>> dateMapMap = null;
					 if(dateMapMapMap.size()>0 && dateMapMapMap.get(Eag2012.TAB_DESCRIPTION)!=null){
						 dateMapMap= dateMapMapMap.get(Eag2012.TAB_DESCRIPTION);
					 }else{
						 dateMapMap = new HashMap<String, Map<String, List<List<String>>>>();
					 }
					 Map<String, List<List<String>>> datesMap = null;
					 if(dateMapMap.size()>0 && dateMapMap.get(Eag2012.REPOSITORHIST)!=null){
						 datesMap = dateMapMap.get(Eag2012.REPOSITORHIST);
					 }else{
						 datesMap = new HashMap<String, List<List<String>>>();
					 }
					 List<List<String>> listDates = null;
					 if(datesMap.size()>0 && datesMap.get(Eag2012.REPOSITOR_SUP)!=null){
						 listDates = datesMap.get(Eag2012.REPOSITOR_SUP);
					 }else{
						 listDates = new ArrayList<List<String>>();
					 }
					 List<String> dates = null;
					 if(listDates.size()>0){
						 dates = listDates.get(0);
					 }else{
						 dates = new ArrayList<String>();
					 }
					 dates.add(descriptionTable.getString("textDateOfRepositorySuppression"));
					 listDates.add(dates);
					 datesMap.put(Eag2012.REPOSITOR_SUP, listDates);
					 dateMapMap.put(Eag2012.REPOSITORHIST, datesMap);
					 dateMapMapMap.put(Eag2012.TAB_DESCRIPTION, dateMapMap);
					 if(dateValue.size()>i){
						 dateValue.set(i,dateMapMapMap);
					 }else{
						 dateValue.add(dateMapMapMap);
					 }
					 eag2012.setDateStandardDate(dateValue);
				 }				
				//rule repositorsup
				
				j=0;
				while(descriptionTable.has("textRuleOfRepositorySuppression_"+(++j)) && descriptionTable.has("selectLanguageRuleOfRepositorySuppression_"+(j))) {
				 if(descriptionTable.has("textRuleOfRepositorySuppression_"+j)){	
					List<Map<String, List<String>>> listRulesMap = eag2012.getRuleValue();
					if(listRulesMap==null){
						listRulesMap= new ArrayList<Map<String,List<String>>>();
					}
					Map<String,List<String>> rulesMap = null;
					if(listRulesMap.size()>i && listRulesMap.get(i)!=null){
						rulesMap = listRulesMap.get(i);
					}else{
						rulesMap = new HashMap<String,List<String>>();
					}
					List<String> rulesList =null;
					if(rulesMap.size()>i && rulesMap.get(Eag2012.REPOSITOR_SUP)!=null){
						rulesList= rulesMap.get(Eag2012.REPOSITOR_SUP);
					}else{
						rulesList = new ArrayList<String>();
					}
					rulesList.add(descriptionTable.getString("textRuleOfRepositorySuppression_"+j));
				    rulesMap.put(Eag2012.REPOSITOR_SUP, rulesList);
				    if(listRulesMap.size()>i){
				    	listRulesMap.set(i, rulesMap);
				    }else{
				    	listRulesMap.add(rulesMap);
				    }
				    eag2012.setRuleValue(listRulesMap);
				 }	
				 if(descriptionTable.has("selectLanguageRuleOfRepositorySuppression_"+j)){
					 List<Map<String,List<String>>> listRulesLang =eag2012.getRuleLang();
					 if(listRulesLang==null){
						 listRulesLang = new ArrayList<Map<String,List<String>>>();
					 }
					 Map<String,List<String>> rulesLangMap = null;
					 if(listRulesLang.size()>i && listRulesLang.get(i)!=null){
						 rulesLangMap = listRulesLang.get(i);
					 }else{
						 rulesLangMap = new HashMap<String,List<String>>();
					 }
					 List<String> rulesLangList = null;
					 if(rulesLangMap.size()>i && rulesLangMap.get(Eag2012.REPOSITOR_SUP)!=null){
						rulesLangList=rulesLangMap.get(Eag2012.REPOSITOR_SUP); 
					 }else{
						 rulesLangList = new ArrayList<String>();
					 }
					 rulesLangList.add(descriptionTable.getString("selectLanguageRuleOfRepositorySuppression_"+j));
					 rulesLangMap.put(Eag2012.REPOSITOR_SUP, rulesLangList);
					 if(listRulesLang.size()>i){
						 listRulesLang.set(i,rulesLangMap);
					 }else{
						 listRulesLang.add(rulesLangMap);
					 }
				    eag2012.setRuleLang(listRulesLang);
				 }	
				}
				
				 //Administrative structure
				
				 String target1 = "textUnitOfAdministrativeStructure";
				 String target2 = "selectLanguageArchivalAndOtherHoldings";
				 int targetNumber = 1;
				 do{
					 target1 = ((target1.indexOf("_")!=-1)?target1.substring(0,target1.indexOf("_")):target1)+"_"+targetNumber;
					 target2 = ((target2.indexOf("_")!=-1)?target2.substring(0,target2.indexOf("_")):target2)+"_"+targetNumber;
					 targetNumber++;
					 if(descriptionTable.has(target1)){
						 List<List<String>> adminUnit = eag2012.getAdminunitValue();
						 if(adminUnit==null){
							 adminUnit = new ArrayList<List<String>>();
						 }
						 List<String> adminUnitList = null;
						 if(adminUnit.size()>i){
							 adminUnitList = adminUnit.get(i);
						 }else{
							 adminUnitList = new ArrayList<String>();
						 }
						 adminUnitList.add(descriptionTable.getString(target1));
						 if(adminUnit.size()>i){
							 adminUnit.set(i, adminUnitList);
						 }else{
							 adminUnit.add(adminUnitList);
						 }
						 eag2012.setAdminunitValue(adminUnit);
					 }
					 if(descriptionTable.has(target2)){
						 List<List<String>> adminUnitLang = eag2012.getAdminunitLang();
						 if(adminUnitLang==null){
							 adminUnitLang = new ArrayList<List<String>>();
						 }
						 List<String> adminUnitLangList = null;
						 if(adminUnitLang.size()>i){
							 adminUnitLangList = adminUnitLang.get(i);
						 }else{
							 adminUnitLangList = new ArrayList<String>();
						 }
						 adminUnitLangList.add(descriptionTable.getString(target2));
						 if(adminUnitLang.size()>i){
							 adminUnitLang.set(i, adminUnitLangList);
						 }else{
							 adminUnitLang.add(adminUnitLangList);
						 }
						 eag2012.setAdminunitLang(adminUnitLang);
					 }
				 }while(descriptionTable.has(target1) && descriptionTable.has(target2));
				 
				 //Building description
				 target1 = "textBuilding";
				 target2 = "selectLanguageBuilding";
				 targetNumber = 1;
				 do{
					 target1 = ((target1.indexOf("_")!=-1)?target1.substring(0,target1.indexOf("_")):target1)+"_"+targetNumber;
					 target2 = ((target2.indexOf("_")!=-1)?target2.substring(0,target2.indexOf("_")):target2)+"_"+targetNumber;
					 targetNumber++;
					 if(descriptionTable.has(target1)){
						 List<Map<String, Map<String, List<String>>>> descriptiveNotePs = eag2012.getDescriptiveNotePValue();
						 if(descriptiveNotePs==null){
							 descriptiveNotePs = new ArrayList<Map<String, Map<String, List<String>>>>();
						 }
						 Map<String, Map<String, List<String>>> descriptiveNotePMap = null;
						 if(descriptiveNotePs.size()>i){
							 descriptiveNotePMap = descriptiveNotePs.get(i);
						 }else{
							 descriptiveNotePMap = new HashMap<String, Map<String, List<String>>>();
						 }
						 Map<String, List<String>> descriptiveNotePMapMap = null;
						 if(descriptiveNotePMap.size()>0 && descriptiveNotePMap.get(Eag2012.TAB_DESCRIPTION)!=null){
							 descriptiveNotePMapMap = descriptiveNotePMap.get(Eag2012.TAB_DESCRIPTION);
						 }else{
							 descriptiveNotePMapMap = new HashMap<String, List<String>>(); 
						 }
						 List<String> descriptiveNotePMapMapList = null;
						 if(descriptiveNotePMapMap.size()>0 && descriptiveNotePMapMap.get(Eag2012.BUILDING)!=null){
							 descriptiveNotePMapMapList = descriptiveNotePMapMap.get(Eag2012.BUILDING);
						 }else{
							 descriptiveNotePMapMapList = new ArrayList<String>();
						 }
						 descriptiveNotePMapMapList.add(descriptionTable.getString(target1));
						 descriptiveNotePMapMap.put(Eag2012.BUILDING, descriptiveNotePMapMapList);
						 descriptiveNotePMap.put(Eag2012.TAB_DESCRIPTION, descriptiveNotePMapMap);
						 if(descriptiveNotePs.size()>i){
							 descriptiveNotePs.set(i,descriptiveNotePMap);
						 }else{
							 descriptiveNotePs.add(descriptiveNotePMap);
						 }
						 eag2012.setDescriptiveNotePValue(descriptiveNotePs);
					 }
					 if(descriptionTable.has(target2)){
						 List<Map<String, Map<String, List<String>>>> descriptiveNotePs = eag2012.getDescriptiveNotePLang();
						 if(descriptiveNotePs==null){
							 descriptiveNotePs = new ArrayList<Map<String, Map<String, List<String>>>>();
						 }
						 Map<String, Map<String, List<String>>> descriptiveNotePMap = null;
						 if(descriptiveNotePs.size()>i){
							 descriptiveNotePMap = descriptiveNotePs.get(i);
						 }else{
							 descriptiveNotePMap = new HashMap<String, Map<String, List<String>>>();
						 }
						 Map<String, List<String>> descriptiveNotePMapMap = null;
						 if(descriptiveNotePMap.size()>0 && descriptiveNotePMap.get(Eag2012.TAB_DESCRIPTION)!=null){
							 descriptiveNotePMapMap = descriptiveNotePMap.get(Eag2012.TAB_DESCRIPTION);
						 }else{
							 descriptiveNotePMapMap = new HashMap<String, List<String>>(); 
						 }
						 List<String> descriptiveNotePMapMapList = null;
						 if(descriptiveNotePMapMap.size()>0 && descriptiveNotePMapMap.get(Eag2012.BUILDING)!=null){
							 descriptiveNotePMapMapList = descriptiveNotePMapMap.get(Eag2012.BUILDING);
						 }else{
							 descriptiveNotePMapMapList = new ArrayList<String>();
						 }
						 descriptiveNotePMapMapList.add(descriptionTable.getString(target2));
						 descriptiveNotePMapMap.put(Eag2012.BUILDING, descriptiveNotePMapMapList);
						 descriptiveNotePMap.put(Eag2012.TAB_DESCRIPTION, descriptiveNotePMapMap);
						 if(descriptiveNotePs.size()>i){
							 descriptiveNotePs.set(i,descriptiveNotePMap);
						 }else{
							 descriptiveNotePs.add(descriptiveNotePMap);
						 }
						 eag2012.setDescriptiveNotePLang(descriptiveNotePs);
					 }
				 }while(descriptionTable.has(target1) && descriptionTable.has(target2));
				 
				 if(descriptionTable.getString("textRepositoryArea")!=null){
					 List<Map<String,Map<String,Map<String,List<String>>>>> numValue = eag2012.getNumValue();
					 if(numValue==null){
						 numValue = new ArrayList<Map<String,Map<String,Map<String,List<String>>>>>();
					 }
					 Map<String, Map<String, Map<String, List<String>>>> numMapMapMap = null;
					 if(numValue.size()>i && numValue.get(i)!=null){
						 numMapMapMap = numValue.get(i);
					 }else{
						 numMapMapMap = new HashMap<String, Map<String, Map<String, List<String>>>>(); 
					 }
					 Map<String, Map<String, List<String>>> numMapMap = null;
					 if(numMapMapMap.size()>0 && numMapMapMap.get(Eag2012.TAB_DESCRIPTION)!=null){
						 numMapMap = numMapMapMap.get(Eag2012.TAB_DESCRIPTION);
					 }else{
						 numMapMap = new HashMap<String, Map<String, List<String>>>();
					 }
					 Map<String, List<String>> numsMap = null;
					 if(numMapMap.size()>0 && numMapMap.get(Eag2012.BUILDING)!=null){
						 numsMap = numMapMap.get(Eag2012.BUILDING);
					 }else{
						 numsMap = new HashMap<String, List<String>>();
					 }
					 List<String> nums = null;
					 if(numsMap.size()>0 && numsMap.get(Eag2012.BUILDING_AREA)!=null){
						 nums = numsMap.get(Eag2012.BUILDING_AREA);
					 }else{
						 nums = new ArrayList<String>();
					 }
					 nums.add(descriptionTable.getString("textRepositoryArea"));
					 numsMap.put(Eag2012.BUILDING_AREA,nums);
					 numMapMap.put(Eag2012.BUILDING,numsMap);
					 numMapMapMap.put(Eag2012.TAB_DESCRIPTION,numMapMap);
					 if(numValue.size()>i){
						 numValue.set(i,numMapMapMap);
					 }else{
						 numValue.add(numMapMapMap);
					 }
					 eag2012.setNumValue(numValue);
				 }
				 if(descriptionTable.getString("textLengthOfShelf")!=null){
					 List<Map<String,Map<String,Map<String,List<String>>>>> numValue = eag2012.getNumValue();
					 if(numValue==null){
						 numValue = new ArrayList<Map<String,Map<String,Map<String,List<String>>>>>();
					 }
					 Map<String, Map<String, Map<String, List<String>>>> numMapMapMap = null;
					 if(numValue.size()>i && numValue.get(i)!=null){
						 numMapMapMap = numValue.get(i);
					 }else{
						 numMapMapMap = new HashMap<String, Map<String, Map<String, List<String>>>>(); 
					 }
					 Map<String, Map<String, List<String>>> numMapMap = null;
					 if(numMapMapMap.size()>0 && numMapMapMap.get(Eag2012.TAB_DESCRIPTION)!=null){
						 numMapMap = numMapMapMap.get(Eag2012.TAB_DESCRIPTION);
					 }else{
						 numMapMap = new HashMap<String, Map<String, List<String>>>();
					 }
					 Map<String, List<String>> numsMap = null;
					 if(numMapMap.size()>0 && numMapMap.get(Eag2012.BUILDING)!=null){
						 numsMap = numMapMap.get(Eag2012.BUILDING);
					 }else{
						 numsMap = new HashMap<String, List<String>>();
					 }
					 List<String> nums = null;
					 if(numsMap.size()>0 && numsMap.get(Eag2012.BUILDING_LENGTH)!=null){
						 nums = numsMap.get(Eag2012.BUILDING_LENGTH);
					 }else{
						 nums = new ArrayList<String>();
					 }
					 nums.add(descriptionTable.getString("textLengthOfShelf"));
					 numsMap.put(Eag2012.BUILDING_LENGTH,nums);
					 numMapMap.put(Eag2012.BUILDING,numsMap);
					 numMapMapMap.put(Eag2012.TAB_DESCRIPTION,numMapMap);
					 if(numValue.size()>i){
						 numValue.set(i,numMapMapMap);
					 }else{
						 numValue.add(numMapMapMap);
					 }
					 eag2012.setNumValue(numValue);
				 }
               
				 //holding description
				   j=0;
					while (descriptionTable.has("textArchivalAndOtherHoldings_"+(++j)) && (descriptionTable.has("selectLanguageArchivalAndOtherHoldings_"+(j)))){
						if(descriptionTable.has("textArchivalAndOtherHoldings_"+j)){
							List<Map<String, Map<String, List<String>>>> descriptiveNotePValue = eag2012.getDescriptiveNotePValue();
							 if(descriptiveNotePValue==null){
								 descriptiveNotePValue = new ArrayList<Map<String, Map<String, List<String>>>>();
							 }
							 Map<String, Map<String, List<String>>> descriptiveNoteMapMapList = null;
							 if(descriptiveNotePValue.size()>i && descriptiveNotePValue.get(i)!=null){
								 descriptiveNoteMapMapList = descriptiveNotePValue.get(i);
							 }else{
								 descriptiveNoteMapMapList = new HashMap<String,Map<String,List<String>>>();
							 }
							 Map<String, List<String>> descriptiveNoteMapList = null;
							 if(descriptiveNoteMapMapList.size()>0 && descriptiveNoteMapMapList.get(Eag2012.TAB_DESCRIPTION)!=null){
								 descriptiveNoteMapList = descriptiveNoteMapMapList.get(Eag2012.TAB_DESCRIPTION);
							 }else{
								 descriptiveNoteMapList = new HashMap<String, List<String>>();
							 }
							 List<String> descriptiveNoteList = null;
							 if(descriptiveNoteMapList.size()>0 && descriptiveNoteMapList.get(Eag2012.HOLDINGS)!=null){
								 descriptiveNoteList = descriptiveNoteMapList.get(Eag2012.HOLDINGS);
							 }else{
								 descriptiveNoteList = new ArrayList<String>();
							 }
							 descriptiveNoteList.add(descriptionTable.getString("textArchivalAndOtherHoldings_"+j));
							 descriptiveNoteMapList.put(Eag2012.HOLDINGS,descriptiveNoteList);
							 descriptiveNoteMapMapList.put(Eag2012.TAB_DESCRIPTION,descriptiveNoteMapList);
							 if(descriptiveNotePValue.size()>i){
								 descriptiveNotePValue.set(i,descriptiveNoteMapMapList);
							 }else{
								 descriptiveNotePValue.add(descriptiveNoteMapMapList);
							 }
							 eag2012.setDescriptiveNotePValue(descriptiveNotePValue);
						 }
						if(descriptionTable.has("selectLanguageArchivalAndOtherHoldings_"+j)){
							List<Map<String, Map<String, List<String>>>> descriptiveNotePValue = eag2012.getDescriptiveNotePLang();
							 if(descriptiveNotePValue==null){
								 descriptiveNotePValue = new ArrayList<Map<String, Map<String, List<String>>>>();
							 }
							 Map<String, Map<String, List<String>>> descriptiveNoteMapMapList = null;
							 if(descriptiveNotePValue.size()>i && descriptiveNotePValue.get(i)!=null){
								 descriptiveNoteMapMapList = descriptiveNotePValue.get(i);
							 }else{
								 descriptiveNoteMapMapList = new HashMap<String,Map<String,List<String>>>();
							 }
							 Map<String, List<String>> descriptiveNoteMapList = null;
							 if(descriptiveNoteMapMapList.size()>0 && descriptiveNoteMapMapList.get(Eag2012.TAB_DESCRIPTION)!=null){
								 descriptiveNoteMapList = descriptiveNoteMapMapList.get(Eag2012.TAB_DESCRIPTION);
							 }else{
								 descriptiveNoteMapList = new HashMap<String, List<String>>();
							 }
							 List<String> descriptiveNoteList = null;
							 if(descriptiveNoteMapList.size()>0 && descriptiveNoteMapList.get(Eag2012.HOLDINGS)!=null){
								 descriptiveNoteList = descriptiveNoteMapList.get(Eag2012.HOLDINGS);
							 }else{
								 descriptiveNoteList = new ArrayList<String>();
							 }
							 descriptiveNoteList.add(descriptionTable.getString("selectLanguageArchivalAndOtherHoldings_"+j));
							 descriptiveNoteMapList.put(Eag2012.HOLDINGS,descriptiveNoteList);
							 descriptiveNoteMapMapList.put(Eag2012.TAB_DESCRIPTION,descriptiveNoteMapList);
							 if(descriptiveNotePValue.size()>i){
								 descriptiveNotePValue.set(i,descriptiveNoteMapMapList);
							 }else{
								 descriptiveNotePValue.add(descriptiveNoteMapMapList);
							 }
							 eag2012.setDescriptiveNotePLang(descriptiveNotePValue);
						 }	
					}
			 	 //date of holdings
					
				  j=0;
				  while(descriptionTable.has("textYearWhenThisNameWasUsed_"+(++j))){
					if(descriptionTable.has("textYearWhenThisNameWasUsed_"+j)){  
					  List<Map<String, Map<String, Map<String, List<List<String>>>>>> dateValue = eag2012.getDateStandardDate();
					  if(dateValue==null){
						dateValue = new ArrayList<Map<String, Map<String, Map<String, List<List<String>>>>>>();
					   }
					   Map<String, Map<String, Map<String, List<List<String>>>>> dateMapMapMap = null;
					   if(dateValue.size()>i && dateValue.get(i)!=null){
						 dateMapMapMap = dateValue.get(i);
					   }else{
						  dateMapMapMap = new HashMap<String, Map<String, Map<String, List<List<String>>>>>(); 
					   }
					   Map<String, Map<String, List<List<String>>>> dateMapMap = null;
					   if(dateMapMapMap.size()>0 && dateMapMapMap.get(Eag2012.TAB_DESCRIPTION)!=null){
						 dateMapMap= dateMapMapMap.get(Eag2012.TAB_DESCRIPTION);
					   }else{
						 dateMapMap = new HashMap<String, Map<String, List<List<String>>>>();
					   }
					   Map<String, List<List<String>>> datesMap = null;
					   if(dateMapMap.size()>0 && dateMapMap.get(Eag2012.HOLDINGS)!=null){
						 datesMap = dateMapMap.get(Eag2012.HOLDINGS);
					   }else{
						 datesMap = new HashMap<String, List<List<String>>>();
					   }
					   List<List<String>> listDates = null;
					   if(datesMap.size()>0 && datesMap.get(Eag2012.HOLDING_SUBSECTION)!=null){
						 listDates = datesMap.get(Eag2012.HOLDING_SUBSECTION);
					   }else{
						 listDates = new ArrayList<List<String>>();
					   }
					   List<String> dates = null;
					   if(listDates.size()>0){ 
						   dates = listDates.get(0);
					   }else{
						   dates = new ArrayList<String>();
					   }
					   dates.add(descriptionTable.getString("textYearWhenThisNameWasUsed_"+j));
					   if(listDates.size()>0){
						   listDates.set(0,dates);  
					   }else{
						   listDates.add(dates);
					   }
					   datesMap.put(Eag2012.HOLDING_SUBSECTION, listDates);
					   dateMapMap.put(Eag2012.HOLDINGS,datesMap);
					   dateMapMapMap.put(Eag2012.TAB_DESCRIPTION,dateMapMap);
					   if( dateValue.size()>i){
						 dateValue.set(i,dateMapMapMap);
					   }else{
						 dateValue.add(dateMapMapMap);
					   }
					   eag2012.setDateStandardDate(dateValue);
					}	  
				  }
			  	 //date range of holdings
				  j=0;
				  while(descriptionTable.has("textYearWhenThisNameWasUsedFrom_"+(++j)) && descriptionTable.has("textYearWhenThisNameWasUsedTo_"+j)){
					  if(descriptionTable.has("textYearWhenThisNameWasUsedFrom_"+j)){  
						  List<Map<String, Map<String, Map<String, List<List<String>>>>>> dateValue = eag2012.getFromDateStandardDate();
						  if(dateValue==null){
							dateValue = new ArrayList<Map<String,Map<String,Map<String,List<List<String>>>>>>();
						   }
						   Map<String, Map<String, Map<String, List<List<String>>>>> dateMapMapMap = null;
						   if(dateValue.size()>i && dateValue.get(i)!=null){
							 dateMapMapMap = dateValue.get(i);
						   }else{
							  dateMapMapMap = new HashMap<String, Map<String, Map<String, List<List<String>>>>>(); 
						   }
						   Map<String, Map<String, List<List<String>>>> dateMapMap = null;
						   if(dateMapMapMap.size()>0 && dateMapMapMap.get(Eag2012.TAB_DESCRIPTION)!=null){
							 dateMapMap= dateMapMapMap.get(Eag2012.TAB_DESCRIPTION);
						   }else{
							 dateMapMap = new HashMap<String, Map<String, List<List<String>>>>();
						   }
						   Map<String, List<List<String>>> datesMap = null;
						   if(dateMapMap.size()>0 && dateMapMap.get(Eag2012.HOLDINGS)!=null){
							 datesMap = dateMapMap.get(Eag2012.HOLDINGS);
						   }else{
							 datesMap = new HashMap<String, List<List<String>>>();
						   }
						   List<List<String>> listDates = null;
						   if(datesMap.size()>0 && datesMap.get(Eag2012.HOLDING_SUBSECTION)!=null){
							   listDates = datesMap.get(Eag2012.HOLDING_SUBSECTION);
						   }else{
							   listDates = new ArrayList<List<String>>();
						   }
						   List<String> dates = null;
						   if(listDates.size()>0){
							   dates = listDates.get(0);
						   }else{
							   dates = new ArrayList<String>();
						   }
						   dates.add(descriptionTable.getString("textYearWhenThisNameWasUsedFrom_"+j));
						   if(listDates.size()>0){
							   listDates.set(0, dates);  
						   }else{
							   listDates.add(dates);
						   }
						   datesMap.put(Eag2012.HOLDING_SUBSECTION, listDates);
						   dateMapMap.put(Eag2012.HOLDINGS,datesMap);
						   dateMapMapMap.put(Eag2012.TAB_DESCRIPTION,dateMapMap);
						   if( dateValue.size()>i){
							 dateValue.set(i,dateMapMapMap);
						   }else{
							 dateValue.add(dateMapMapMap);
						   }
						   eag2012.setFromDateStandardDate(dateValue);
						}	  
					  if(descriptionTable.has("textYearWhenThisNameWasUsedTo_"+j)){  
						  List<Map<String, Map<String, Map<String, List<List<String>>>>>> dateValue = eag2012.getToDateStandardDate();
						  if(dateValue==null){
							dateValue = new ArrayList<Map<String, Map<String, Map<String, List<List<String>>>>>>();
						   }
						   Map<String, Map<String, Map<String, List<List<String>>>>> dateMapMapMap = null;
						   if(dateValue.size()>i && dateValue.get(i)!=null){
							 dateMapMapMap = dateValue.get(i);
						   }else{
							  dateMapMapMap = new HashMap<String, Map<String, Map<String, List<List<String>>>>>(); 
						   }
						   Map<String, Map<String, List<List<String>>>> dateMapMap = null;
						   if(dateMapMapMap.size()>0 && dateMapMapMap.get(Eag2012.TAB_DESCRIPTION)!=null){
							 dateMapMap= dateMapMapMap.get(Eag2012.TAB_DESCRIPTION);
						   }else{
							 dateMapMap = new HashMap<String, Map<String, List<List<String>>>>();
						   }
						   Map<String, List<List<String>>> datesMap = null;
						   if(dateMapMap.size()>0 && dateMapMap.get(Eag2012.HOLDINGS)!=null){
							 datesMap = dateMapMap.get(Eag2012.HOLDINGS);
						   }else{
							 datesMap = new HashMap<String, List<List<String>>>();
						   }
						   List<List<String>> listDates = null;
						   if(datesMap.size()>0 && datesMap.get(Eag2012.HOLDING_SUBSECTION)!=null){
							 listDates = datesMap.get(Eag2012.HOLDING_SUBSECTION);
						   }else{
							 listDates = new ArrayList<List<String>>();
						   }
						   List<String> dates = null;
						   if(listDates.size()>0){
							   dates = listDates.get(0);
						   }else{
							   dates = new ArrayList<String>();
						   }
						   dates.add(descriptionTable.getString("textYearWhenThisNameWasUsedTo_"+j));
						   if(listDates.size()>0){
							   listDates.set(0, dates);  
						   }else{
							   listDates.add(dates);
						   }
						   datesMap.put(Eag2012.HOLDING_SUBSECTION, listDates);
						   dateMapMap.put(Eag2012.HOLDINGS,datesMap);
						   dateMapMapMap.put(Eag2012.TAB_DESCRIPTION,dateMapMap);
						   if( dateValue.size()>i){
							 dateValue.set(i,dateMapMapMap);
						   }else{
							 dateValue.add(dateMapMapMap);
						   }
						   eag2012.setToDateStandardDate(dateValue);
						}	  
				  }  
				  //extent
				  if(descriptionTable.has("textExtent")){
					  List<Map<String,Map<String,Map<String,List<String>>>>> numValue = eag2012.getNumValue();
						 if(numValue==null){
							 numValue = new ArrayList<Map<String,Map<String,Map<String,List<String>>>>>();
						 }
						 Map<String, Map<String, Map<String, List<String>>>> numMapMapMap = null;
						 if(numValue.size()>i && numValue.get(i)!=null){
							 numMapMapMap = numValue.get(i);
						 }else{
							 numMapMapMap = new HashMap<String, Map<String, Map<String, List<String>>>>(); 
						 }
						 Map<String, Map<String, List<String>>> numMapMap = null;
						 if(numMapMapMap.size()>0 && numMapMapMap.get(Eag2012.TAB_DESCRIPTION)!=null){
							 numMapMap = numMapMapMap.get(Eag2012.TAB_DESCRIPTION);
						 }else{
							 numMapMap = new HashMap<String, Map<String, List<String>>>();
						 }
						 Map<String, List<String>> numsMap = null;
						 if(numMapMap.size()>0 && numMapMap.get(Eag2012.HOLDINGS)!=null){
							 numsMap = numMapMap.get(Eag2012.HOLDINGS);
						 }else{
							 numsMap = new HashMap<String, List<String>>();
						 }
						 List<String> nums = null;
						 if(numsMap.size()>0 && numsMap.get(Eag2012.HOLDING_EXTENT)!=null){
							 nums = numsMap.get(Eag2012.HOLDING_EXTENT);
						 }else{
							 nums = new ArrayList<String>();
						 }
						 nums.add(descriptionTable.getString("textExtent"));
						 numsMap.put(Eag2012.HOLDING_EXTENT,nums);
						 numMapMap.put(Eag2012.HOLDINGS,numsMap);
						 numMapMapMap.put(Eag2012.TAB_DESCRIPTION,numMapMap);
						 if(numValue.size()>i){
							 numValue.set(i,numMapMapMap);
						 }else{
							 numValue.add(numMapMapMap);
						 }
						 eag2012.setNumValue(numValue);  
				  }
				  
			 i++;
			}//end while description
			
		}
		
	return eag2012;	
	}
	
	private Eag2012 parseAccesAndServicesJsonObjToEag2012(Eag2012 eag2012,JSONObject jsonObj) throws JSONException {
		JSONObject accessAndServices = jsonObj.getJSONObject("accessAndServices");
		if(accessAndServices!=null){
			int i=0;
			while(accessAndServices.has("accessAndServicesTable_"+(i+1))){
				JSONObject accessTable = accessAndServices.getJSONObject("accessAndServicesTable_"+(i+1));
             
              //opening times and opening langs 
				
				int j=0;			
				while(accessTable.has("textOpeningTimes_"+(++j)) && (accessTable.has("selectLanguageOpeningTimes_"+(j)))){
				  if(accessTable.has("textOpeningTimes_"+j)){
				     List<Map<String,List<String>>> openingValues = eag2012.getOpeningValue();
					 if(openingValues == null){
					   openingValues = new ArrayList<Map<String,List<String>>>();  
					 }
					 Map<String, List<String>> openingMap = null;
					 if((openingValues.size() > i) && (openingValues.get(i)!=null)){
					   openingMap = openingValues.get(i);	  
					 }else{
						 openingMap = new HashMap<String,List<String>>();
					 }
					List<String> openingList = null;
					if((openingMap.size()>i) && (openingMap.get(Eag2012.TAB_ACCESS_AND_SERVICES)!=null)){
						openingList = openingMap.get(Eag2012.TAB_ACCESS_AND_SERVICES);
					}else{
						openingList = new ArrayList<String>();
					}
					openingList.add(accessTable.getString("textOpeningTimes_"+j));
					openingMap.put(Eag2012.TAB_ACCESS_AND_SERVICES, openingList);
				    if(openingValues.size() > i){
				    	openingValues.set(i,openingMap);
				    }else{
				    	openingValues.add(openingMap);
				    }
				    eag2012.setOpeningValue(openingValues);
				  }
				  if(accessTable.has("selectLanguageOpeningTimes_"+j)){
					     List<Map<String,List<String>>> openingLangs = eag2012.getOpeningLang();
						 if(openingLangs == null){
						   openingLangs = new ArrayList<Map<String,List<String>>>();  
						 }
						 Map<String, List<String>> openingMap = null;
						 if((openingLangs.size() > i) && (openingLangs.get(i)!=null)){
						   openingMap = openingLangs.get(i);	  
						 }else{
							 openingMap = new HashMap<String,List<String>>();
						 }
						List<String> openingList = null;
						if((openingMap.size()>i) && (openingMap.get(Eag2012.TAB_ACCESS_AND_SERVICES)!=null)){
							openingList = openingMap.get(Eag2012.TAB_ACCESS_AND_SERVICES);
						}else{
							openingList = new ArrayList<String>();
						}
						openingList.add(accessTable.getString("selectLanguageOpeningTimes_"+j));
						openingMap.put(Eag2012.TAB_ACCESS_AND_SERVICES, openingList);
					    if(openingLangs.size() > i){
					    	openingLangs.set(i,openingMap);
					    }else{
					    	openingLangs.add(openingMap);
					    }
					    eag2012.setOpeningLang(openingLangs);
				 }
				  
				}//end while opening times
				
				//closing times and langs closing
	            
				j=0;			
				while(accessTable.has("textClosingDates_"+(++j)) && (accessTable.has("selectLanguageClosingDates_"+(j)))){
				  if(accessTable.has("textClosingDates_"+j)){
				     List<Map<String,List<String>>> closingValues = eag2012.getClosingStandardDate();
					 if(closingValues == null){
						 closingValues = new ArrayList<Map<String,List<String>>>();  
					 }
					 Map<String, List<String>> closingMap = null;
					 if((closingValues.size() > i) && (closingValues.get(i)!=null)){
						 closingMap = closingValues.get(i);	  
					 }else{
						 closingMap = new HashMap<String,List<String>>();
					 }
					List<String> closingList = null;
					if((closingMap.size()>0) && (closingMap.get(Eag2012.TAB_ACCESS_AND_SERVICES)!=null)){
						closingList = closingMap.get(Eag2012.TAB_ACCESS_AND_SERVICES);
					}else{
						closingList = new ArrayList<String>();
					}
					closingList.add(accessTable.getString("textClosingDates_"+j));
					closingMap.put(Eag2012.TAB_ACCESS_AND_SERVICES, closingList);
				    if(closingValues.size() > i){
				    	closingValues.set(i,closingMap);
				    }else{
				    	closingValues.add(closingMap);
				    }
				    eag2012.setClosingStandardDate(closingValues);
				  }
				  if(accessTable.has("selectLanguageClosingDates_"+j)){
					     List<Map<String,List<String>>> closingLangs = eag2012.getClosingLang();
						 if(closingLangs == null){
							 closingLangs = new ArrayList<Map<String,List<String>>>();  
						 }
						 Map<String, List<String>> closingMap = null;
						 if((closingLangs.size() > i) && (closingLangs.get(i)!=null)){
							 closingMap = closingLangs.get(i);	  
						 }else{
							 closingMap = new HashMap<String,List<String>>();
						 }
						List<String> closingList = null;
						if((closingMap.size()>0) && (closingMap.get(Eag2012.TAB_ACCESS_AND_SERVICES)!=null)){
							closingList = closingMap.get(Eag2012.TAB_ACCESS_AND_SERVICES);
						}else{
							closingList = new ArrayList<String>();
						}
						closingList.add(accessTable.getString("selectLanguageClosingDates_"+j));
						closingMap.put(Eag2012.TAB_ACCESS_AND_SERVICES, closingList);
					    if(closingLangs.size() > i){
					    	closingLangs.set(i,closingMap);
					    }else{
					    	closingLangs.add(closingMap);
					    }
					    eag2012.setClosingLang(closingLangs);
				 }
				  
				}//end while closing times
				
				//directions
				j=0;
				while((accessTable.has("textTravellingDirections_"+(++j))) && (accessTable.has("selectASATDSelectLanguage_"+(j))) && (accessTable.has("textTravelLink_"+(j)))){
				  	if(accessTable.has("textTravellingDirections_"+j)){
				  		List<List<String>> listDirectionsList = eag2012.getDirectionsValue();
				  		if(listDirectionsList == null){
				  			listDirectionsList = new ArrayList<List<String>>();
				  		}
				  		List<String> directionList = null;
				  		if((listDirectionsList.size()>i) && (listDirectionsList.get(i)!=null)){
				  		  directionList=listDirectionsList.get(i);	
				  		}else{
				  			directionList = new ArrayList<String>();
				  		}
				  		directionList.add(accessTable.getString("textTravellingDirections_"+j));
				  		if(listDirectionsList.size()>i){
				  			listDirectionsList.set(i, directionList);
				  		}else{
				  			listDirectionsList.add(directionList);
				  		}
				  		eag2012.setDirectionsValue(listDirectionsList);
				  	}
				  	if(accessTable.has("selectASATDSelectLanguage_"+j)){
				  		List<List<String>> listDirectionsLangList =eag2012.getDirectionsLang();
				  		if(listDirectionsLangList == null){
				  			listDirectionsLangList = new ArrayList<List<String>>();
				  		}
				  		List<String> directionsLangsList = null;
				  		if(listDirectionsLangList.size()>i && (listDirectionsLangList.get(i)!=null)){
				  			directionsLangsList=listDirectionsLangList.get(i);
				  		}else{
				  			directionsLangsList = new ArrayList<String>();
				  		}
				  		directionsLangsList.add(accessTable.getString("selectASATDSelectLanguage_"+j));
				  		if(listDirectionsLangList.size()>i){
				  			listDirectionsLangList.set(i, directionsLangsList);
				  		}else{
				  			listDirectionsLangList.add(directionsLangsList);
				  		}
				  	  eag2012.setDirectionsLang(listDirectionsLangList);
				  	}
				  	if(accessTable.has("textTravelLink_"+j)){
				  		List<Map<String, List<String>>> listCitationMap = eag2012.getCitationHref();
						if(listCitationMap==null){
							listCitationMap= new ArrayList<Map<String,List<String>>>();
						}
						Map<String,List<String>> citationMap = null;
						if(listCitationMap.size()>i && listCitationMap.get(i)!=null){
							citationMap = listCitationMap.get(i);
						}else{
							citationMap = new HashMap<String,List<String>>();
						}
						List<String> citationList =null;
						if(citationMap.size()>0 && citationMap.get(Eag2012.TAB_ACCESS_AND_SERVICES)!=null){
							citationList= citationMap.get(Eag2012.TAB_ACCESS_AND_SERVICES);
						}else{
							citationList = new ArrayList<String>();
						}
						citationList.add(accessTable.getString("textTravelLink_"+j));
						citationMap.put(Eag2012.TAB_ACCESS_AND_SERVICES, citationList);
					    if(listCitationMap.size()>i){
					    	listCitationMap.set(i, citationMap);
					    }else{
					    	listCitationMap.add(citationMap);
					    }
					    eag2012.setCitationHref(listCitationMap);
				  	}
				
				}//end while directions
				
                //access yes or no, it appears in tab yourinstitution, in tab accessAndServices and for each repository
				
				if(accessTable.has("selectASAccesibleToThePublic")){
					List<Map<String, String>> accessQuestions = eag2012.getAccessQuestion();
					if(accessQuestions == null){
						accessQuestions = new ArrayList<Map<String, String>>(); 
					}
					Map<String, String> accessQuestionRepo = null;
					if(accessQuestions.size()>i && accessQuestions.get(i)!=null){
						accessQuestionRepo = accessQuestions.get(i);
					}else{
						accessQuestionRepo = new HashMap<String, String>();
					}
					accessQuestionRepo.put(Eag2012.TAB_ACCESS_AND_SERVICES,accessTable.getString("selectASAccesibleToThePublic"));
					if(accessQuestions.size()>i){
						accessQuestions.set(i,accessQuestionRepo);
					}else{
						accessQuestions.add(accessQuestionRepo);
					}
					eag2012.setAccessQuestion(accessQuestions);
				}
				
			     //access information and access langs
			     
			     j=0;
				 while((accessTable.has("textASAccessRestrictions_"+(++j))) && (accessTable.has("selectASARSelectLanguage_"+(j)))){
					if(accessTable.has("textASAccessRestrictions_"+j)){
						List<Map<String,List<String>>> listMapRestaccessList = eag2012.getRestaccessValue();
						if(listMapRestaccessList==null){
						  listMapRestaccessList = new ArrayList<Map<String, List<String>>>();	
						}
						 Map<String, List<String>> restaccess = null;
						 if((listMapRestaccessList.size()>i) && (listMapRestaccessList.get(i)!=null)){
							restaccess=listMapRestaccessList.get(i); 
						 }else{
							 restaccess=new HashMap<String,List<String>>();
						 }
						 List<String> listRestaccess = null;
						 if((restaccess.size()>i) && (restaccess.get(Eag2012.TAB_ACCESS_AND_SERVICES)!=null)){
							 listRestaccess=restaccess.get(Eag2012.TAB_ACCESS_AND_SERVICES);
						 }else{
							 listRestaccess=new ArrayList<String>();
						 }
					     listRestaccess.add(accessTable.getString("textASAccessRestrictions_"+j));
					     restaccess.put(Eag2012.TAB_ACCESS_AND_SERVICES, listRestaccess);
					     if(listMapRestaccessList.size()>i){
					    	 listMapRestaccessList.set(i, restaccess);
					     }else{
					    	 listMapRestaccessList.add(restaccess);
					     }
					    eag2012.setRestaccessValue(listMapRestaccessList);
					}
					if(accessTable.has("selectASARSelectLanguage_"+j)) {   //access information lang
						List<Map<String,List<String>>> listMapRestaccessList = eag2012.getRestaccessLang();
						if(listMapRestaccessList==null){
						  listMapRestaccessList = new ArrayList<Map<String, List<String>>>();	
						}
						 Map<String, List<String>> restaccessLang = null;
						 if((listMapRestaccessList.size()>i) && (listMapRestaccessList.get(i)!=null)){
							 restaccessLang=listMapRestaccessList.get(i); 
						 }else{
							 restaccessLang=new HashMap<String,List<String>>();
						 }
						 List<String> listRestaccess = null;
						 if((restaccessLang.size()>i) && (restaccessLang.get(Eag2012.TAB_ACCESS_AND_SERVICES)!=null)){
							 listRestaccess=restaccessLang.get(Eag2012.TAB_ACCESS_AND_SERVICES);
						 }else{
							 listRestaccess=new ArrayList<String>();
						 }
					     listRestaccess.add(accessTable.getString("selectASARSelectLanguage_"+j));
					     restaccessLang.put(Eag2012.TAB_ACCESS_AND_SERVICES, listRestaccess);
					     if(listMapRestaccessList.size()>i){
					    	 listMapRestaccessList.set(i, restaccessLang);
					     }else{
					    	 listMapRestaccessList.add(restaccessLang);
					     }
					    eag2012.setRestaccessLang(listMapRestaccessList);		
					}
				}// end while access information
				
				//terms of use
				 j=0;
				 while((accessTable.has("textASTermOfUse_"+(++j))) && (accessTable.has("selectASAFTOUSelectLanguage_"+(j))) && (accessTable.has("textASTOULink_"+(j)))){
					  	if(accessTable.has("textASTermOfUse_"+j)){
					  		List<List<String>> listTermsList = eag2012.getTermsOfUseValue();
					  		if(listTermsList == null){
					  			listTermsList = new ArrayList<List<String>>();
					  		}
					  		List<String> termsList = null;
					  		if((listTermsList.size()>i) && (listTermsList.get(i)!=null)){
					  			termsList=listTermsList.get(i);	
					  		}else{
					  			termsList = new ArrayList<String>();
					  		}
					  		termsList.add(accessTable.getString("textASTermOfUse_"+j));
					  		if(listTermsList.size()>i){
					  			listTermsList.set(i, termsList);
					  		}else{
					  			listTermsList.add(termsList);
					  		}
					  		eag2012.setTermsOfUseValue(listTermsList);
					  	}
					  	if(accessTable.has("selectASAFTOUSelectLanguage_"+j)){
					  		List<List<String>> listTermsLangList =eag2012.getTermsOfUseLang();
					  		if(listTermsLangList == null){
					  			listTermsLangList = new ArrayList<List<String>>();
					  		}
					  		List<String> termsLangsList = null;
					  		if(listTermsLangList.size()>i && (listTermsLangList.get(i)!=null)){
					  			termsLangsList=listTermsLangList.get(i);
					  		}else{
					  			termsLangsList = new ArrayList<String>();
					  		}
					  		termsLangsList.add(accessTable.getString("selectASAFTOUSelectLanguage_"+j));
					  		if(listTermsLangList.size()>i){
					  			listTermsLangList.set(i, termsLangsList);
					  		}else{
					  			listTermsLangList.add(termsLangsList);
					  		}
					  	  eag2012.setTermsOfUseLang(listTermsLangList);
					  	}
					  	if(accessTable.has("textASTOULink_"+j)){
					  		List<List<String>> listTermsHrefList = eag2012.getTermsOfUseHref();
					  		if(listTermsHrefList==null){
					  			listTermsHrefList=new ArrayList<List<String>>();
					  		}
					  		List<String> termsHrefList = null;	
					  		if(listTermsHrefList.size()>i && (listTermsHrefList.get(i)!=null)){
					  			termsHrefList=listTermsHrefList.get(i);
					  		}else{
					  			termsHrefList = new ArrayList<String>();
					  		}
					  		termsHrefList.add(accessTable.getString("textASTOULink_"+j));
					  		if(listTermsHrefList.size()>i){
					  			listTermsHrefList.set(i, termsHrefList);
					  		}else{
					  			listTermsHrefList.add(termsHrefList);
					  		}
					  	  eag2012.setTermsOfUseHref(listTermsHrefList);
					  	}
					
					}//end while terms of use
				 
				//accessibility yes or no, it appears in tab yourinstitution, in tab accessAndServices and for each repository
				 
				  if(accessTable.has("selectASFacilitiesForDisabledPeopleAvailable")){
					List<Map<String, String>> listAccessibilityMap = eag2012.getAccessibilityQuestion();
					if(listAccessibilityMap == null){
						listAccessibilityMap = new ArrayList<Map<String, String>>(); 
					}
					Map<String, String> accessibilityMap = null;
					if(listAccessibilityMap.size()>i && listAccessibilityMap.get(i)!=null){
						accessibilityMap = listAccessibilityMap.get(i);
					}else{
						accessibilityMap = new HashMap<String, String>();
					}
					accessibilityMap.put(Eag2012.TAB_ACCESS_AND_SERVICES,accessTable.getString("selectASFacilitiesForDisabledPeopleAvailable"));
					if(listAccessibilityMap.size()>i){
						listAccessibilityMap.set(i,accessibilityMap);
					}else{
						listAccessibilityMap.add(accessibilityMap);
					}
					eag2012.setAccessibilityQuestion(listAccessibilityMap);
				  }
				  
				   j=0;
				   while((accessTable.has("textASAccessibility_"+(++j))) && (accessTable.has("selectASASelectLanguage_"+(j)))){
						if(accessTable.has("textASAccessibility_"+j)){
							List<Map<String,List<String>>> listMapAccessibilityList = eag2012.getAccessibilityValue();
							if(listMapAccessibilityList==null){
								listMapAccessibilityList = new ArrayList<Map<String, List<String>>>();	
							}
							 Map<String, List<String>> accesibility = null;
							 if((listMapAccessibilityList.size()>i) && (listMapAccessibilityList.get(i)!=null)){
								 accesibility=listMapAccessibilityList.get(i); 
							 }else{
								 accesibility=new HashMap<String,List<String>>();
							 }
							 List<String> listAccessibility = null;
							 if((accesibility.size()>i) && (accesibility.get(Eag2012.TAB_ACCESS_AND_SERVICES)!=null)){
								 listAccessibility=accesibility.get(Eag2012.TAB_ACCESS_AND_SERVICES);
							 }else{
								 listAccessibility=new ArrayList<String>();
							 }
							 listAccessibility.add(accessTable.getString("textASAccessibility_"+j));
						     accesibility.put(Eag2012.TAB_ACCESS_AND_SERVICES, listAccessibility);
						     if(listMapAccessibilityList.size()>i){
						    	 listMapAccessibilityList.set(i, accesibility);
						     }else{
						    	 listMapAccessibilityList.add(accesibility);
						     }
						    eag2012.setAccessibilityValue(listMapAccessibilityList);
						}
						if(accessTable.has("selectASASelectLanguage_"+j)) {   //accessibility information lang
							List<Map<String,List<String>>> listMapAccessibilityList = eag2012.getAccessibilityLang();
							if(listMapAccessibilityList==null){
								listMapAccessibilityList = new ArrayList<Map<String, List<String>>>();	
							}
							 Map<String, List<String>> accessibilityLang = null;
							 if((listMapAccessibilityList.size()>i) && (listMapAccessibilityList.get(i)!=null)){
								 accessibilityLang=listMapAccessibilityList.get(i); 
							 }else{
								 accessibilityLang=new HashMap<String,List<String>>();
							 }
							 List<String> listAccessibility = null;
							 if(( accessibilityLang.size()>i) && ( accessibilityLang.get(Eag2012.TAB_ACCESS_AND_SERVICES)!=null)){
								 listAccessibility= accessibilityLang.get(Eag2012.TAB_ACCESS_AND_SERVICES);
							 }else{
								 listAccessibility=new ArrayList<String>();
							 }
							 listAccessibility.add(accessTable.getString("selectASASelectLanguage_"+j));
						     accessibilityLang.put(Eag2012.TAB_ACCESS_AND_SERVICES, listAccessibility);
						     if(listMapAccessibilityList.size()>i){
						    	 listMapAccessibilityList.set(i,  accessibilityLang);
						     }else{
						    	 listMapAccessibilityList.add( accessibilityLang);
						     }
						    eag2012.setAccessibilityLang(listMapAccessibilityList);		
						}
					}// end while accessibility
				
				 //search_room section
				 if(accessTable.has("textASSRTelephone")){
					 List<Map<String, Map<String, List<String>>>> telephones = eag2012.getTelephoneValue();
					 if(telephones==null){
						 telephones = new ArrayList<Map<String, Map<String, List<String>>>>();
					 }
					 Map<String, Map<String, List<String>>> telephonesMap = null;
					 if(telephones.size()>i && telephones.get(i)!=null){ //repo
						 telephonesMap = telephones.get(i);
					 }else{
						 telephonesMap = new HashMap<String, Map<String, List<String>>>(); 
					 }
					 Map<String, List<String>> telephonesMapList = null;
					 if(telephonesMap.size()>0 && telephonesMap.get(Eag2012.TAB_ACCESS_AND_SERVICES)!=null){
						 telephonesMapList = telephonesMap.get(Eag2012.TAB_ACCESS_AND_SERVICES);
					 }else{
						 telephonesMapList = new HashMap<String, List<String>>();
					 }
					 List<String> telephonesList = null;
					 if(telephonesMapList.size()>0 && telephonesMapList.get(Eag2012.SEARCHROOM)!=null){
						 telephonesList = telephonesMapList.get(Eag2012.SEARCHROOM);
					 }else{
						 telephonesList = new ArrayList<String>();
					 }
					 telephonesList.add(accessTable.getString("textASSRTelephone"));
					 telephonesMapList.put(Eag2012.SEARCHROOM, telephonesList);
					 telephonesMap.put(Eag2012.TAB_ACCESS_AND_SERVICES,telephonesMapList);
					 if(telephones.size()>i){
						 telephones.set(i, telephonesMap);
					 }else{
						 telephones.add(telephonesMap);
					 }
					 eag2012.setTelephoneValue(telephones);
				 }
				 if(accessTable.has("textASSREmailAddress")){
					 List<Map<String, Map<String, List<String>>>> listMapEmailValueList = eag2012.getEmailHref();
					 if(listMapEmailValueList==null){
						 listMapEmailValueList = new ArrayList<Map<String, Map<String, List<String>>>>();
					 }
					 Map<String, Map<String, List<String>>> email = null;
					 if(listMapEmailValueList.size()>i && listMapEmailValueList.get(i)!=null){
						 email = listMapEmailValueList.get(i);
					 }else{
						 email = new HashMap<String, Map<String, List<String>>>();
					 }
					 List<String> listEmail = null;
					 Map<String,List<String>> emailMap = null;
					 if(email.size()>0 && email.get(Eag2012.TAB_ACCESS_AND_SERVICES)!=null){
						 emailMap = email.get(Eag2012.TAB_ACCESS_AND_SERVICES);
					 }else{
						 emailMap = new HashMap<String,List<String>>();
					 }
					 if(emailMap.size()>0 && emailMap.get(Eag2012.SEARCHROOM)!=null){
						 listEmail = emailMap.get(Eag2012.SEARCHROOM);
					 }else{
						 listEmail = new ArrayList<String>();
					 }
					 listEmail.add(accessTable.getString("textASSREmailAddress"));
					 emailMap.put(Eag2012.SEARCHROOM,listEmail); 
					 email.put(Eag2012.TAB_ACCESS_AND_SERVICES, emailMap);
					 if(listMapEmailValueList.size()>i){
						 listMapEmailValueList.set(i,email);
					 }else{
						 listMapEmailValueList.add(email);
					 }
					 eag2012.setEmailHref(listMapEmailValueList);
				 }
				 if(accessTable.has("textASSREmailLinkTitle")){
					 List<Map<String, Map<String, List<String>>>> listMapEmailList = eag2012.getEmailValue();
					 if(listMapEmailList==null){
						 listMapEmailList = new ArrayList<Map<String, Map<String, List<String>>>>();
					 }
					 Map<String, Map<String, List<String>>> email = null;
					 if(listMapEmailList.size()>i && listMapEmailList.get(i)!=null){
						 email = listMapEmailList.get(i);
					 }else{
						 email = new HashMap<String, Map<String, List<String>>>();
					 }
					 List<String> listEmail = null;
					 Map<String,List<String>> emailMap = null;
					 if(email.size()>0 && email.get(Eag2012.TAB_ACCESS_AND_SERVICES)!=null){
						 emailMap = email.get(Eag2012.TAB_ACCESS_AND_SERVICES);
					 }else{
						 emailMap = new HashMap<String,List<String>>();
					 }
					 if(emailMap.size()>0 && emailMap.get(Eag2012.SEARCHROOM)!=null){
						 listEmail = emailMap.get(Eag2012.SEARCHROOM);
					 }else{
						 listEmail = new ArrayList<String>();
					 }
					 listEmail.add(accessTable.getString("textASSREmailLinkTitle"));
					 emailMap.put(Eag2012.SEARCHROOM,listEmail); 
					 email.put(Eag2012.TAB_ACCESS_AND_SERVICES, emailMap);
					 if(listMapEmailList.size()>i){
						 listMapEmailList.set(i,email);
					 }else{
						 listMapEmailList.add(email);
					 }
					 eag2012.setEmailValue(listMapEmailList);
				 }
				 if(accessTable.has("textASSRWebpage")){
					 List<Map<String, Map<String, List<String>>>> listMapWebpagelList = eag2012.getWebpageHref();
					 if(listMapWebpagelList==null){
						 listMapWebpagelList = new ArrayList<Map<String, Map<String, List<String>>>>();
					 }
					 Map<String, Map<String, List<String>>> webpage = null;
					 if(listMapWebpagelList.size()>i && listMapWebpagelList.get(i)!=null){
						 webpage = listMapWebpagelList.get(i);
					 }else{
						 webpage = new HashMap<String, Map<String, List<String>>>();
					 }
					 List<String> listWebpage = null;
					 Map<String,List<String>> webpageMap = null;
					 if(webpage.size()>0 && webpage.get(Eag2012.TAB_ACCESS_AND_SERVICES)!=null){
						 webpageMap = webpage.get(Eag2012.TAB_ACCESS_AND_SERVICES);
					 }else{
						 webpageMap = new HashMap<String,List<String>>();
					 }
					 if(webpageMap.size()>0 && webpageMap.get(Eag2012.SEARCHROOM)!=null){
						 listWebpage = webpageMap.get(Eag2012.SEARCHROOM);
					 }else{
						 listWebpage = new ArrayList<String>();
					 }
					 listWebpage.add(accessTable.getString("textASSRWebpage"));
					 webpageMap.put(Eag2012.SEARCHROOM,listWebpage); //root section, here there is only one mails list
					 webpage.put(Eag2012.TAB_ACCESS_AND_SERVICES, webpageMap);
					 if(listMapWebpagelList.size()>i){
						 listMapWebpagelList.set(i,webpage);
					 }else{
						 listMapWebpagelList.add(webpage);
					 }
					 eag2012.setWebpageHref(listMapWebpagelList);
				 }
				 if(accessTable.has("textASSRWebpageLinkTitle")){
					 List<Map<String, Map<String, List<String>>>> listMapWebpageValueList = eag2012.getWebpageValue();
					 if(listMapWebpageValueList==null){
						 listMapWebpageValueList = new ArrayList<Map<String, Map<String, List<String>>>>();
					 }
					 Map<String, Map<String, List<String>>> webpage = null;
					 if(listMapWebpageValueList.size()>i && listMapWebpageValueList.get(i)!=null){
						 webpage = listMapWebpageValueList.get(i);
					 }else{
						 webpage = new HashMap<String, Map<String, List<String>>>();
					 }
					 List<String> listWebpage = null;
					 Map<String,List<String>> webpageMap = null;
					 if(webpage.size()>0 && webpage.get(Eag2012.TAB_ACCESS_AND_SERVICES)!=null){
						 webpageMap = webpage.get(Eag2012.TAB_ACCESS_AND_SERVICES);
					 }else{
						 webpageMap = new HashMap<String,List<String>>();
					 }
					 if(webpageMap.size()>0 && webpageMap.get(Eag2012.SEARCHROOM)!=null){
						 listWebpage = webpageMap.get(Eag2012.SEARCHROOM);
					 }else{
						 listWebpage = new ArrayList<String>();
					 }
					 listWebpage.add(accessTable.getString("textASSRWebpageLinkTitle"));
					 webpageMap.put(Eag2012.SEARCHROOM,listWebpage); //root section, here there is only one mails list
					 webpage.put(Eag2012.TAB_ACCESS_AND_SERVICES, webpageMap);
					 if(listMapWebpageValueList.size()>i){
						 listMapWebpageValueList.set(i,webpage);
					 }else{
						 listMapWebpageValueList.add(webpage);
					 }
					 eag2012.setWebpageValue(listMapWebpageValueList);
				 }
				 if(accessTable.has("textASSRWorkPlaces")){
					 List<Map<String,Map<String,Map<String,List<String>>>>> numValue = eag2012.getNumValue();
					 if(numValue==null){
						 numValue = new ArrayList<Map<String,Map<String,Map<String,List<String>>>>>();
					 }
					 Map<String, Map<String, Map<String, List<String>>>> numMapMapMap = null;
					 if(numValue.size()>i && numValue.get(i)!=null){
						 numMapMapMap = numValue.get(i);
					 }else{
						 numMapMapMap = new HashMap<String, Map<String, Map<String, List<String>>>>(); 
					 }
					 Map<String, Map<String, List<String>>> numMapMap = null;
					 if(numMapMapMap.size()>0 && numMapMapMap.get(Eag2012.TAB_ACCESS_AND_SERVICES)!=null){
						 numMapMap = numMapMapMap.get(Eag2012.TAB_ACCESS_AND_SERVICES);
					 }else{
						 numMapMap = new HashMap<String, Map<String, List<String>>>();
					 }
					 Map<String, List<String>> numsMap = null;
					 if(numMapMap.size()>0 && numMapMap.get(Eag2012.SEARCHROOM)!=null){
						 numsMap = numMapMap.get(Eag2012.SEARCHROOM);
					 }else{
						 numsMap = new HashMap<String, List<String>>();
					 }
					 List<String> nums = null;
					 if(numsMap.size()>0 && numsMap.get(Eag2012.WORKING_PLACES)!=null){
						 nums = numsMap.get(Eag2012.WORKING_PLACES);
					 }else{
						 nums = new ArrayList<String>();
					 }
					 nums.add(accessTable.getString("textASSRWorkPlaces"));
					 numsMap.put(Eag2012.WORKING_PLACES, nums);
					 numMapMap.put(Eag2012.SEARCHROOM,numsMap);
					 numMapMapMap.put(Eag2012.TAB_ACCESS_AND_SERVICES,numMapMap);
					 if(numValue.size()>i){
						 numValue.set(i,numMapMapMap);
					 }else{
						 numValue.add(numMapMapMap);
					 }
					 eag2012.setNumValue(numValue);
				 }
				 if(accessTable.has("textASSRComputerPlaces")){
					 List<Map<String,Map<String,Map<String,List<String>>>>> numValue = eag2012.getNumValue();
					 if(numValue==null){
						 numValue = new ArrayList<Map<String,Map<String,Map<String,List<String>>>>>();
					 }
					 Map<String, Map<String, Map<String, List<String>>>> numMapMapMap = null;
					 if(numValue.size()>i && numValue.get(i)!=null){
						 numMapMapMap = numValue.get(i);
					 }else{
						 numMapMapMap = new HashMap<String, Map<String, Map<String, List<String>>>>(); 
					 }
					 Map<String, Map<String, List<String>>> numMapMap = null;
					 if(numMapMapMap.size()>0 && numMapMapMap.get(Eag2012.TAB_ACCESS_AND_SERVICES)!=null){
						 numMapMap = numMapMapMap.get(Eag2012.TAB_ACCESS_AND_SERVICES);
					 }else{
						 numMapMap = new HashMap<String, Map<String, List<String>>>();
					 }
					 Map<String, List<String>> numsMap = null;
					 if(numMapMap.size()>0 && numMapMap.get(Eag2012.SEARCHROOM)!=null){
						 numsMap = numMapMap.get(Eag2012.SEARCHROOM);
					 }else{
						 numsMap = new HashMap<String, List<String>>();
					 }
					 List<String> nums = null;
					 if(numsMap.size()>0 && numsMap.get(Eag2012.COMPUTER_PLACES)!=null){
						 nums = numsMap.get(Eag2012.COMPUTER_PLACES);
					 }else{
						 nums = new ArrayList<String>();
					 }
					 nums.add(accessTable.getString("textASSRComputerPlaces"));
					 numsMap.put(Eag2012.COMPUTER_PLACES, nums);
					 numMapMap.put(Eag2012.SEARCHROOM,numsMap);
					 numMapMapMap.put(Eag2012.TAB_ACCESS_AND_SERVICES,numMapMap);
					 if(numValue.size()>i){
						 numValue.set(i,numMapMapMap);
					 }else{
						 numValue.add(numMapMapMap);
					 }
					 eag2012.setNumValue(numValue);
				 }
				 String target1 = "textDescriptionOfYourComputerPlaces";
				 String target2 = "selectDescriptionOfYourComputerPlaces";
				 int targetNumber = 1;
				 do{
					 target1 = ((target1.indexOf("_")!=-1)?target1.substring(0,target1.indexOf("_")):target1)+"_"+targetNumber;
					 target2 = ((target2.indexOf("_")!=-1)?target2.substring(0,target2.indexOf("_")):target2)+"_"+targetNumber;
					 targetNumber++;
					 if(accessTable.has(target1)){
						 List<Map<String, Map<String, List<String>>>> descriptiveNotePValue = eag2012.getDescriptiveNotePValue();
						 if(descriptiveNotePValue==null){
							 descriptiveNotePValue = new ArrayList<Map<String, Map<String, List<String>>>>();
						 }
						 Map<String, Map<String, List<String>>> descriptiveNoteMapMapList = null;
						 if(descriptiveNotePValue.size()>i && descriptiveNotePValue.get(i)!=null){
							 descriptiveNoteMapMapList = descriptiveNotePValue.get(i);
						 }else{
							 descriptiveNoteMapMapList = new HashMap<String,Map<String,List<String>>>();
						 }
						 Map<String, List<String>> descriptiveNoteMapList = null;
						 if(descriptiveNoteMapMapList.size()>0 && descriptiveNoteMapMapList.get(Eag2012.TAB_ACCESS_AND_SERVICES)!=null){
							 descriptiveNoteMapList = descriptiveNoteMapMapList.get(Eag2012.TAB_ACCESS_AND_SERVICES);
						 }else{
							 descriptiveNoteMapList = new HashMap<String, List<String>>();
						 }
						 List<String> descriptiveNoteList = null;
						 if(descriptiveNoteMapList.size()>0 && descriptiveNoteMapList.get(Eag2012.SEARCHROOM)!=null){
							 descriptiveNoteList = descriptiveNoteMapList.get(Eag2012.SEARCHROOM);
						 }else{
							 descriptiveNoteList = new ArrayList<String>();
						 }
						 descriptiveNoteList.add(accessTable.getString(target1));
						 descriptiveNoteMapList.put(Eag2012.SEARCHROOM,descriptiveNoteList);
						 descriptiveNoteMapMapList.put(Eag2012.TAB_ACCESS_AND_SERVICES,descriptiveNoteMapList);
						 if(descriptiveNotePValue.size()>i){
							 descriptiveNotePValue.set(i,descriptiveNoteMapMapList);
						 }else{
							 descriptiveNotePValue.add(descriptiveNoteMapMapList);
						 }
						 eag2012.setDescriptiveNotePValue(descriptiveNotePValue);
					 }
					 if(accessTable.has(target2)){
						 List<Map<String, Map<String, List<String>>>> descriptiveNotePValue = eag2012.getDescriptiveNotePLang();
						 if(descriptiveNotePValue==null){
							 descriptiveNotePValue = new ArrayList<Map<String, Map<String, List<String>>>>();
						 }
						 Map<String, Map<String, List<String>>> descriptiveNoteMapMapList = null;
						 if(descriptiveNotePValue.size()>i && descriptiveNotePValue.get(i)!=null){
							 descriptiveNoteMapMapList = descriptiveNotePValue.get(i);
						 }else{
							 descriptiveNoteMapMapList = new HashMap<String,Map<String,List<String>>>();
						 }
						 Map<String, List<String>> descriptiveNoteMapList = null;
						 if(descriptiveNoteMapMapList.size()>0 && descriptiveNoteMapMapList.get(Eag2012.TAB_ACCESS_AND_SERVICES)!=null){
							 descriptiveNoteMapList = descriptiveNoteMapMapList.get(Eag2012.TAB_ACCESS_AND_SERVICES);
						 }else{
							 descriptiveNoteMapList = new HashMap<String, List<String>>();
						 }
						 List<String> descriptiveNoteList = null;
						 if(descriptiveNoteMapList.size()>0 && descriptiveNoteMapList.get(Eag2012.SEARCHROOM)!=null){
							 descriptiveNoteList = descriptiveNoteMapList.get(Eag2012.SEARCHROOM);
						 }else{
							 descriptiveNoteList = new ArrayList<String>();
						 }
						 descriptiveNoteList.add(accessTable.getString(target2));
						 descriptiveNoteMapList.put(Eag2012.SEARCHROOM,descriptiveNoteList);
						 descriptiveNoteMapMapList.put(Eag2012.TAB_ACCESS_AND_SERVICES,descriptiveNoteMapList);
						 if(descriptiveNotePValue.size()>i){
							 descriptiveNotePValue.set(i,descriptiveNoteMapMapList);
						 }else{
							 descriptiveNotePValue.add(descriptiveNoteMapMapList);
						 }
						 eag2012.setDescriptiveNotePLang(descriptiveNotePValue);
					 }
				 }while(accessTable.has(target1) && accessTable.has(target2));
				 target1 = null;
				 target2 = null;
				 
				 if(accessTable.has("textASSRMicrofilmPlaces")){
					 List<Map<String, Map<String, Map<String, List<String>>>>> nums = eag2012.getNumValue();
					 if(nums==null){
						 nums = new ArrayList<Map<String, Map<String, Map<String, List<String>>>>>();
					 }
					 Map<String, Map<String, Map<String, List<String>>>> numsMapMapMapList = null;
					 if(nums.size()>i && nums.get(i)!=null){
						 numsMapMapMapList = nums.get(i);
					 }else{
						 numsMapMapMapList = new HashMap<String, Map<String, Map<String, List<String>>>>();
					 }
					 Map<String, Map<String, List<String>>> numsMapMapList = null;
					 if(numsMapMapMapList.size()>0 && numsMapMapMapList.get(Eag2012.TAB_ACCESS_AND_SERVICES)!=null){
						 numsMapMapList = numsMapMapMapList.get(Eag2012.TAB_ACCESS_AND_SERVICES);
					 }else{
						 numsMapMapList = new HashMap<String, Map<String, List<String>>>();
					 }
					 Map<String, List<String>> numsMapList = null;
					 if(numsMapMapList.size()>0 && numsMapMapList.get(Eag2012.SEARCHROOM)!=null){
						 numsMapList = numsMapMapList.get(Eag2012.SEARCHROOM);
					 }else{
						 numsMapList = new HashMap<String, List<String>>(); 
					 }
					 List<String> numsList = null;
					 if(numsMapList.size()>0 && numsMapList.get(Eag2012.MICROFILM)!=null){
						 numsList = numsMapList.get(Eag2012.MICROFILM);
					 }else{
						 numsList = new ArrayList<String>();
					 }
					 numsList.add(accessTable.getString("textASSRMicrofilmPlaces"));
					 numsMapList.put(Eag2012.MICROFILM,numsList);
					 numsMapMapList.put(Eag2012.SEARCHROOM,numsMapList);
					 numsMapMapMapList.put(Eag2012.TAB_ACCESS_AND_SERVICES,numsMapMapList);
					 if(nums.size()>i){
						 nums.set(i,numsMapMapMapList);
					 }else{
						 nums.add(numsMapMapMapList);
					 }
					 eag2012.setNumValue(nums);
				 }
				 if(accessTable.has("selectASSRPhotographAllowance")){
					 List<String> photographAllowance = eag2012.getPhotographAllowanceValue();
					 if(photographAllowance==null){
						 photographAllowance = new ArrayList<String>();
					 }
					 if(photographAllowance.size()>i){
						 photographAllowance.set(i, accessTable.getString("selectASSRPhotographAllowance"));
					 }else{
						 photographAllowance.add(accessTable.getString("selectASSRPhotographAllowance"));
					 }
					 eag2012.setPhotographAllowanceValue(photographAllowance);
				 }
				 target1 = "textASSRReadersTicket";
				 target2 = "selectReadersTickectLanguage";
				 String target3 = "textASSRRTLink";
				 targetNumber = 1;
				 do{
					 target1=(target1.indexOf("_")!=-1?target1.substring(0,target1.indexOf("_")):target1)+"_"+targetNumber;
					 target2=(target2.indexOf("_")!=-1?target2.substring(0,target2.indexOf("_")):target2)+"_"+targetNumber;
					 target3=(target3.indexOf("_")!=-1?target3.substring(0,target3.indexOf("_")):target3)+"_"+targetNumber;
					 targetNumber++;
					 if(accessTable.has(target1)){
						 List<List<String>> readersTicket = eag2012.getReadersTicketValue();
						 if(readersTicket==null){
							 readersTicket = new ArrayList<List<String>>();
						 }
						 List<String> readerTicket = null;
						 if(readersTicket.size()>i){
							 readerTicket = readersTicket.get(i);
						 }else{
							 readerTicket = new ArrayList<String>();
						 }
						 readerTicket.add(accessTable.getString(target1));
						 if(readersTicket.size()>i){
							 readersTicket.set(i,readerTicket);
						 }else{
							 readersTicket.add(readerTicket);
						 }
						 eag2012.setReadersTicketValue(readersTicket);
					 }
					 if(accessTable.has(target2)){
						 List<List<String>> readersTicketLang = eag2012.getReadersTicketLang();
						 if(readersTicketLang==null){
							 readersTicketLang = new ArrayList<List<String>>();
						 }
						 List<String> readerTicketLang = null;
						 if(readersTicketLang.size()>i){
							 readerTicketLang = readersTicketLang.get(i);
						 }else{
							 readerTicketLang = new ArrayList<String>();
						 }
						 readerTicketLang.add(accessTable.getString(target2));
						 if(readersTicketLang.size()>i){
							 readersTicketLang.set(i,readerTicketLang);
						 }else{
							 readersTicketLang.add(readerTicketLang);
						 }
						 eag2012.setReadersTicketLang(readersTicketLang);
					 }
					 if(accessTable.has(target3)){
						 List<List<String>> readersTicketHref = eag2012.getReadersTicketHref();
						 if(readersTicketHref==null){
							 readersTicketHref = new ArrayList<List<String>>();
						 }
						 List<String> readerTicketHref = null;
						 if(readersTicketHref.size()>i){
							 readerTicketHref = readersTicketHref.get(i);
						 }else{
							 readerTicketHref = new ArrayList<String>();
						 }
						 readerTicketHref.add(accessTable.getString(target3));
						 if(readersTicketHref.size()>i){
							 readersTicketHref.set(i,readerTicketHref);
						 }else{
							 readersTicketHref.add(readerTicketHref);
						 }
						 eag2012.setReadersTicketHref(readersTicketHref);
					 }
				 }while(accessTable.has(target1) && accessTable.has(target2) && accessTable.has(target3));
				 
				 target1 = "textASSRAdvancedOrders";
				 target2 = "textASSRAOLink";
				 target3 = "selectASSRAFOIUSelectLanguage";
				 targetNumber = 1;
				 do{
					 target1=(target1.indexOf("_")!=-1?target1.substring(0,target1.indexOf("_")):target1)+"_"+targetNumber;
					 target2=(target2.indexOf("_")!=-1?target2.substring(0,target2.indexOf("_")):target2)+"_"+targetNumber;
					 target3=(target3.indexOf("_")!=-1?target3.substring(0,target3.indexOf("_")):target3)+"_"+targetNumber;
					 targetNumber++;
					 if(accessTable.has(target1)){
						 List<List<String>> advancedOrders = eag2012.getAdvancedOrdersValue();
						 if(advancedOrders==null){
							 advancedOrders = new ArrayList<List<String>>();
						 }
						 List<String> advanceOrder = null;
						 if(advancedOrders.size()>i && advancedOrders.get(i)!=null){
							 advanceOrder = advancedOrders.get(i);
						 }else{
							 advanceOrder = new ArrayList<String>();
						 }
						 advanceOrder.add(accessTable.getString(target1));
						 if(advancedOrders.size()>i){
							 advancedOrders.set(i,advanceOrder);
						 }else{
							 advancedOrders.add(advanceOrder);
						 }
						 eag2012.setAdvancedOrdersValue(advancedOrders);
					 }
					 if(accessTable.has(target2)){
						 List<List<String>> advancedOrdersHref = eag2012.getAdvancedOrdersHref();
						 if(advancedOrdersHref==null){
							 advancedOrdersHref = new ArrayList<List<String>>();
						 }
						 List<String> advanceOrderHref = null;
						 if(advancedOrdersHref.size()>i && advancedOrdersHref.get(i)!=null){
							 advanceOrderHref = advancedOrdersHref.get(i);
						 }else{
							 advanceOrderHref = new ArrayList<String>();
						 }
						 advanceOrderHref.add(accessTable.getString(target2));
						 if(advancedOrdersHref.size()>i){
							 advancedOrdersHref.set(i,advanceOrderHref);
						 }else{
							 advancedOrdersHref.add(advanceOrderHref);
						 }
						 eag2012.setAdvancedOrdersHref(advancedOrdersHref);
					 }
					 if(accessTable.has(target3)){
						 List<List<String>> advancedOrdersLang = eag2012.getAdvancedOrdersLang();
						 if(advancedOrdersLang==null){
							 advancedOrdersLang = new ArrayList<List<String>>();
						 }
						 List<String> advanceOrderLang = null;
						 if(advancedOrdersLang.size()>i && advancedOrdersLang.get(i)!=null){
							 advanceOrderLang = advancedOrdersLang.get(i);
						 }else{
							 advanceOrderLang = new ArrayList<String>();
						 }
						 advanceOrderLang.add(accessTable.getString(target3));
						 if(advancedOrdersLang.size()>i){
							 advancedOrdersLang.set(i,advanceOrderLang);
						 }else{
							 advancedOrdersLang.add(advanceOrderLang);
						 }
						 eag2012.setAdvancedOrdersLang(advancedOrdersLang);
					 }
				 }while(accessTable.has(target1) && accessTable.has(target2) && accessTable.has(target3));
				 
				 target1 = "textASSRResearchServices";
				 target2 = "textASSRRSSelectLanguage";
				 targetNumber=1;
				 do{
					 target1=(target1.indexOf("_")!=-1?target1.substring(0,target1.indexOf("_")):target1)+"_"+targetNumber;
					 target2=(target2.indexOf("_")!=-1?target2.substring(0,target2.indexOf("_")):target2)+"_"+targetNumber;
					 targetNumber++;
					 if(accessTable.has(target1)){
						 List<Map<String, Map<String, List<String>>>> descriptiveNotesPValues = eag2012.getDescriptiveNotePValue();
						 if(descriptiveNotesPValues==null){
							 descriptiveNotesPValues = new ArrayList<Map<String, Map<String, List<String>>>>();
						 }
						 Map<String, Map<String, List<String>>> descriptiveNotePValue = null;
						 if(descriptiveNotesPValues.size()>i && descriptiveNotesPValues.get(i)!=null){
							 descriptiveNotePValue = descriptiveNotesPValues.get(i);
						 }else{
							 descriptiveNotePValue = new HashMap<String,Map<String,List<String>>>();
						 }
						 Map<String, List<String>> descriptiveNoteMapList = null;
						 if(descriptiveNotePValue.size()>0 && descriptiveNotePValue.get(Eag2012.TAB_ACCESS_AND_SERVICES)!=null){
							 descriptiveNoteMapList = descriptiveNotePValue.get(Eag2012.TAB_ACCESS_AND_SERVICES);
						 }else{
							 descriptiveNoteMapList = new HashMap<String, List<String>>(); 
						 }
						 List<String> descriptiveNoteList = null;
						 if(descriptiveNoteMapList.size()>0 && descriptiveNoteMapList.get(Eag2012.RESEARCH_SERVICES)!=null){
							 descriptiveNoteList = descriptiveNoteMapList.get(Eag2012.RESEARCH_SERVICES);
						 }else{
							 descriptiveNoteList = new ArrayList<String>();
						 }
						 descriptiveNoteList.add(accessTable.getString(target1));
						 descriptiveNoteMapList.put(Eag2012.RESEARCH_SERVICES,descriptiveNoteList);
						 descriptiveNotePValue.put(Eag2012.TAB_ACCESS_AND_SERVICES,descriptiveNoteMapList);
						 if(descriptiveNotesPValues.size()>i){
							 descriptiveNotesPValues.set(i,descriptiveNotePValue);
						 }else{
							 descriptiveNotesPValues.add(descriptiveNotePValue);
						 }
						 eag2012.setDescriptiveNotePValue(descriptiveNotesPValues);
					 }
					 if(accessTable.has(target2)){
						 List<Map<String, Map<String, List<String>>>> descriptiveNotesPLang = eag2012.getDescriptiveNotePLang();
						 if(descriptiveNotesPLang==null){
							 descriptiveNotesPLang = new ArrayList<Map<String, Map<String, List<String>>>>();
						 }
						 Map<String, Map<String, List<String>>> descriptiveNotePLang = null;
						 if(descriptiveNotesPLang.size()>i && descriptiveNotesPLang.get(i)!=null){
							 descriptiveNotePLang = descriptiveNotesPLang.get(i);
						 }else{
							 descriptiveNotePLang = new HashMap<String,Map<String,List<String>>>();
						 }
						 Map<String, List<String>> descriptiveNoteMapList = null;
						 if(descriptiveNotePLang.size()>0 && descriptiveNotePLang.get(Eag2012.TAB_ACCESS_AND_SERVICES)!=null){
							 descriptiveNoteMapList = descriptiveNotePLang.get(Eag2012.TAB_ACCESS_AND_SERVICES);
						 }else{
							 descriptiveNoteMapList = new HashMap<String, List<String>>(); 
						 }
						 List<String> descriptiveNoteList = null;
						 if(descriptiveNoteMapList.size()>0 && descriptiveNoteMapList.get(Eag2012.RESEARCH_SERVICES)!=null){
							 descriptiveNoteList = descriptiveNoteMapList.get(Eag2012.RESEARCH_SERVICES);
						 }else{
							 descriptiveNoteList = new ArrayList<String>();
						 }
						 descriptiveNoteList.add(accessTable.getString(target2));
						 descriptiveNoteMapList.put(Eag2012.RESEARCH_SERVICES,descriptiveNoteList);
						 descriptiveNotePLang.put(Eag2012.TAB_ACCESS_AND_SERVICES,descriptiveNoteMapList);
						 if(descriptiveNotesPLang.size()>i){
							 descriptiveNotesPLang.set(i,descriptiveNotePLang);
						 }else{
							 descriptiveNotesPLang.add(descriptiveNotePLang);
						 }
						 eag2012.setDescriptiveNotePLang(descriptiveNotesPLang);
					 }
				 }while(accessTable.has(target1) && accessTable.has(target2));
					
				  //library section
					 if(accessTable.has("selectASLibrary")){
						 String library = accessTable.getString("selectASLibrary");
						 List<String> libraryList = eag2012.getLibraryQuestion();
						 if(libraryList == null){
						    libraryList = new ArrayList<String>();
						 }
						 if(libraryList.size()>i){
							 libraryList.set(i,library);
						 }else{
						    libraryList.add(library);
						 }
						 eag2012.setLibraryQuestion(libraryList);
					
					 }
					 if(accessTable.has("textASLTelephone")){
						 List<Map<String, Map<String, List<String>>>> telephones = eag2012.getTelephoneValue();
						 if(telephones==null){
							 telephones = new ArrayList<Map<String, Map<String, List<String>>>>();
						 }
						 Map<String, Map<String, List<String>>> telephonesMap = null;
						 if(telephones.size()>i && telephones.get(i)!=null){ //repo
							 telephonesMap = telephones.get(i);
						 }else{
							 telephonesMap = new HashMap<String, Map<String, List<String>>>(); 
						 }
						 Map<String, List<String>> telephonesMapList = null;
						 if(telephonesMap.size()>0 && telephonesMap.get(Eag2012.TAB_ACCESS_AND_SERVICES)!=null){
							 telephonesMapList = telephonesMap.get(Eag2012.TAB_ACCESS_AND_SERVICES);
						 }else{
							 telephonesMapList = new HashMap<String, List<String>>();
						 }
						 List<String> telephonesList = null;
						 if(telephonesMapList.size()>0 && telephonesMapList.get(Eag2012.LIBRARY)!=null){
							 telephonesList = telephonesMapList.get(Eag2012.LIBRARY);
						 }else{
							 telephonesList = new ArrayList<String>();
						 }
						 telephonesList.add(accessTable.getString("textASLTelephone"));
						 telephonesMapList.put(Eag2012.LIBRARY, telephonesList);
						 telephonesMap.put(Eag2012.TAB_ACCESS_AND_SERVICES,telephonesMapList);
						 if(telephones.size()>i){
							 telephones.set(i, telephonesMap);
						 }else{
							 telephones.add(telephonesMap);
						 }
						 eag2012.setTelephoneValue(telephones);
					 }
					
					 if(accessTable.has("textASLEmailAddress")){
						 List<Map<String, Map<String, List<String>>>> listMapEmailValueList = eag2012.getEmailHref();
						 if(listMapEmailValueList==null){
							 listMapEmailValueList = new ArrayList<Map<String, Map<String, List<String>>>>();
						 }
						 Map<String, Map<String, List<String>>> email = null;
						 if(listMapEmailValueList.size()>i && listMapEmailValueList.get(i)!=null){
							 email = listMapEmailValueList.get(i);
						 }else{
							 email = new HashMap<String, Map<String, List<String>>>();
						 }
						 List<String> listEmail = null;
						 Map<String,List<String>> emailMap = null;
						 if(email.size()>0 && email.get(Eag2012.TAB_ACCESS_AND_SERVICES)!=null){
							 emailMap = email.get(Eag2012.TAB_ACCESS_AND_SERVICES);
						 }else{
							 emailMap = new HashMap<String,List<String>>();
						 }
						 if(emailMap.size()>0 && emailMap.get(Eag2012.LIBRARY)!=null){
							 listEmail = emailMap.get(Eag2012.LIBRARY);
						 }else{
							 listEmail = new ArrayList<String>();
						 }
						 listEmail.add(accessTable.getString("textASLEmailAddress"));
						 emailMap.put(Eag2012.LIBRARY,listEmail); 
						 email.put(Eag2012.TAB_ACCESS_AND_SERVICES, emailMap);
						 if(listMapEmailValueList.size()>i){
							 listMapEmailValueList.set(i,email);
						 }else{
							 listMapEmailValueList.add(email);
						 }
						 eag2012.setEmailHref(listMapEmailValueList);
					 }
					
					 if(accessTable.has("textASLEmailLinkTitle")){
						 List<Map<String, Map<String, List<String>>>> listMapEmailList = eag2012.getEmailValue();
						 if(listMapEmailList==null){
							 listMapEmailList = new ArrayList<Map<String, Map<String, List<String>>>>();
						 }
						 Map<String, Map<String, List<String>>> email = null;
						 if(listMapEmailList.size()>i && listMapEmailList.get(i)!=null){
							 email = listMapEmailList.get(i);
						 }else{
							 email = new HashMap<String, Map<String, List<String>>>();
						 }
						 List<String> listEmail = null;
						 Map<String,List<String>> emailMap = null;
						 if(email.size()>0 && email.get(Eag2012.TAB_ACCESS_AND_SERVICES)!=null){
							 emailMap = email.get(Eag2012.TAB_ACCESS_AND_SERVICES);
						 }else{
							 emailMap = new HashMap<String,List<String>>();
						 }
						 if(emailMap.size()>0 && emailMap.get(Eag2012.LIBRARY)!=null){
							 listEmail = emailMap.get(Eag2012.LIBRARY);
						 }else{
							 listEmail = new ArrayList<String>();
						 }
						 listEmail.add(accessTable.getString("textASLEmailLinkTitle"));
						 emailMap.put(Eag2012.LIBRARY,listEmail); 
						 email.put(Eag2012.TAB_ACCESS_AND_SERVICES, emailMap);
						 if(listMapEmailList.size()>i){
							 listMapEmailList.set(i,email);
						 }else{
							 listMapEmailList.add(email);
						 }
						 eag2012.setEmailValue(listMapEmailList);
					 }
		             
					 if(accessTable.has("textASLWebpage")){
						 List<Map<String, Map<String, List<String>>>> listMapWebpagelList = eag2012.getWebpageHref();
						 if(listMapWebpagelList==null){
							 listMapWebpagelList = new ArrayList<Map<String, Map<String, List<String>>>>();
						 }
						 Map<String, Map<String, List<String>>> webpage = null;
						 if(listMapWebpagelList.size()>i && listMapWebpagelList.get(i)!=null){
							 webpage = listMapWebpagelList.get(i);
						 }else{
							 webpage = new HashMap<String, Map<String, List<String>>>();
						 }
						 List<String> listWebpage = null;
						 Map<String,List<String>> webpageMap = null;
						 if(webpage.size()>0 && webpage.get(Eag2012.TAB_ACCESS_AND_SERVICES)!=null){
							 webpageMap = webpage.get(Eag2012.TAB_ACCESS_AND_SERVICES);
						 }else{
							 webpageMap = new HashMap<String,List<String>>();
						 }
						 if(webpageMap.size()>0 && webpageMap.get(Eag2012.LIBRARY)!=null){
							 listWebpage = webpageMap.get(Eag2012.LIBRARY);
						 }else{
							 listWebpage = new ArrayList<String>();
						 }
						 listWebpage.add(accessTable.getString("textASLWebpage"));
						 webpageMap.put(Eag2012.LIBRARY,listWebpage); //root section, here there is only one mails list
						 webpage.put(Eag2012.TAB_ACCESS_AND_SERVICES, webpageMap);
						 if(listMapWebpagelList.size()>i){
							 listMapWebpagelList.set(i,webpage);
						 }else{
							 listMapWebpagelList.add(webpage);
						 }
						 eag2012.setWebpageHref(listMapWebpagelList);
					 }
					 if(accessTable.has("textASLWebpageLinkTitle")){
						 List<Map<String, Map<String, List<String>>>> listMapWebpageValueList = eag2012.getWebpageValue();
						 if(listMapWebpageValueList==null){
							 listMapWebpageValueList = new ArrayList<Map<String, Map<String, List<String>>>>();
						 }
						 Map<String, Map<String, List<String>>> webpage = null;
						 if(listMapWebpageValueList.size()>i && listMapWebpageValueList.get(i)!=null){
							 webpage = listMapWebpageValueList.get(i);
						 }else{
							 webpage = new HashMap<String, Map<String, List<String>>>();
						 }
						 List<String> listWebpage = null;
						 Map<String,List<String>> webpageMap = null;
						 if(webpage.size()>0 && webpage.get(Eag2012.TAB_ACCESS_AND_SERVICES)!=null){
							 webpageMap = webpage.get(Eag2012.TAB_ACCESS_AND_SERVICES);
						 }else{
							 webpageMap = new HashMap<String,List<String>>();
						 }
						 if(webpageMap.size()>0 && webpageMap.get(Eag2012.LIBRARY)!=null){
							 listWebpage = webpageMap.get(Eag2012.LIBRARY);
						 }else{
							 listWebpage = new ArrayList<String>();
						 }
						 listWebpage.add(accessTable.getString("textASLWebpageLinkTitle"));
						 webpageMap.put(Eag2012.LIBRARY,listWebpage); //root section, here there is only one mails list
						 webpage.put(Eag2012.TAB_ACCESS_AND_SERVICES, webpageMap);
						 if(listMapWebpageValueList.size()>i){
							 listMapWebpageValueList.set(i,webpage);
						 }else{
							 listMapWebpageValueList.add(webpage);
						 }
						 eag2012.setWebpageValue(listMapWebpageValueList);
					 }
					 if(accessTable.has("textASLMonographocPublication")){
						 List<Map<String,Map<String,Map<String,List<String>>>>> numValue = eag2012.getNumValue();
						 if(numValue==null){
							 numValue = new ArrayList<Map<String,Map<String,Map<String,List<String>>>>>();
						 }
						 Map<String, Map<String, Map<String, List<String>>>> numMapMapMap = null;
						 if(numValue.size()>i && numValue.get(i)!=null){
							 numMapMapMap = numValue.get(i);
						 }else{
							 numMapMapMap = new HashMap<String, Map<String, Map<String, List<String>>>>(); 
						 }
						 Map<String, Map<String, List<String>>> numMapMap = null;
						 if(numMapMapMap.size()>0 && numMapMapMap.get(Eag2012.TAB_ACCESS_AND_SERVICES)!=null){
							 numMapMap = numMapMapMap.get(Eag2012.TAB_ACCESS_AND_SERVICES);
						 }else{
							 numMapMap = new HashMap<String, Map<String, List<String>>>();
						 }
						 Map<String, List<String>> numsMap = null;
						 if(numMapMap.size()>0 && numMapMap.get(Eag2012.LIBRARY)!=null){
							 numsMap = numMapMap.get(Eag2012.LIBRARY);
						 }else{
							 numsMap = new HashMap<String, List<String>>();
						 }
						 List<String> nums = null;
						 if(numsMap.size()>0 && numsMap.get(Eag2012.MONOGRAPHIC_PUBLICATION)!=null){
							 nums = numsMap.get(Eag2012.MONOGRAPHIC_PUBLICATION);
						 }else{
							 nums = new ArrayList<String>();
						 }
						 nums.add(accessTable.getString("textASLMonographocPublication"));
						 numsMap.put(Eag2012.MONOGRAPHIC_PUBLICATION, nums);
						 numMapMap.put(Eag2012.LIBRARY,numsMap);
						 numMapMapMap.put(Eag2012.TAB_ACCESS_AND_SERVICES,numMapMap);
						 if(numValue.size()>i){
							 numValue.set(i,numMapMapMap);
						 }else{
							 numValue.add(numMapMapMap);
						 }
						 eag2012.setNumValue(numValue);
					 }
					 
					 if(accessTable.has("textASLSerialPublication")){
						 List<Map<String,Map<String,Map<String,List<String>>>>> numValue = eag2012.getNumValue();
						 if(numValue==null){
							 numValue = new ArrayList<Map<String,Map<String,Map<String,List<String>>>>>();
						 }
						 Map<String, Map<String, Map<String, List<String>>>> numMapMapMap = null;
						 if(numValue.size()>i && numValue.get(i)!=null){
							 numMapMapMap = numValue.get(i);
						 }else{
							 numMapMapMap = new HashMap<String, Map<String, Map<String, List<String>>>>(); 
						 }
						 Map<String, Map<String, List<String>>> numMapMap = null;
						 if(numMapMapMap.size()>0 && numMapMapMap.get(Eag2012.TAB_ACCESS_AND_SERVICES)!=null){
							 numMapMap = numMapMapMap.get(Eag2012.TAB_ACCESS_AND_SERVICES);
						 }else{
							 numMapMap = new HashMap<String, Map<String, List<String>>>();
						 }
						 Map<String, List<String>> numsMap = null;
						 if(numMapMap.size()>0 && numMapMap.get(Eag2012.LIBRARY)!=null){
							 numsMap = numMapMap.get(Eag2012.LIBRARY);
						 }else{
							 numsMap = new HashMap<String, List<String>>();
						 }
						 List<String> nums = null;
						 if(numsMap.size()>0 && numsMap.get(Eag2012.SERIAL_PUBLICATION)!=null){
							 nums = numsMap.get(Eag2012.SERIAL_PUBLICATION);
						 }else{
							 nums = new ArrayList<String>();
						 }
						 nums.add(accessTable.getString("textASLSerialPublication"));
						 numsMap.put(Eag2012.SERIAL_PUBLICATION, nums);
						 numMapMap.put(Eag2012.LIBRARY,numsMap);
						 numMapMapMap.put(Eag2012.TAB_ACCESS_AND_SERVICES,numMapMap);
						 if(numValue.size()>i){
							 numValue.set(i,numMapMapMap);
						 }else{
							 numValue.add(numMapMapMap);
						 }
						 eag2012.setNumValue(numValue);
					 }
					 
					 //internet access
					 
					 if(accessTable.has("selectASInternetAccess")){
						 String internet = accessTable.getString("selectASInternetAccess");
						 List<String> internetList = eag2012.getInternetAccessQuestion();
						 if(internetList == null){
						    internetList = new ArrayList<String>();
						 }
						 if(internetList.size()>i){
							 internetList.set(i,internet);
						 }else{
						   internetList.add(internet);
						 } 
						 eag2012.setInternetAccessQuestion(internetList);
					 }
					 
					 target1 = "textASDescription";
					 target2 = "selectASDSelectLanguage";
					 targetNumber = 1;
					 do{
						 target1 = ((target1.indexOf("_")!=-1)?target1.substring(0,target1.indexOf("_")):target1)+"_"+targetNumber;
						 target2 = ((target2.indexOf("_")!=-1)?target2.substring(0,target2.indexOf("_")):target2)+"_"+targetNumber;
						 targetNumber++;
						 if(accessTable.has(target1)){
							 List<Map<String, Map<String, List<String>>>> descriptiveNotePValue = eag2012.getDescriptiveNotePValue();
							 if(descriptiveNotePValue==null){
								 descriptiveNotePValue = new ArrayList<Map<String, Map<String, List<String>>>>();
							 }
							 Map<String, Map<String, List<String>>> descriptiveNoteMapMapList = null;
							 if(descriptiveNotePValue.size()>i && descriptiveNotePValue.get(i)!=null){
								 descriptiveNoteMapMapList = descriptiveNotePValue.get(i);
							 }else{
								 descriptiveNoteMapMapList = new HashMap<String,Map<String,List<String>>>();
							 }
							 Map<String, List<String>> descriptiveNoteMapList = null;
							 if(descriptiveNoteMapMapList.size()>0 && descriptiveNoteMapMapList.get(Eag2012.TAB_ACCESS_AND_SERVICES)!=null){
								 descriptiveNoteMapList = descriptiveNoteMapMapList.get(Eag2012.TAB_ACCESS_AND_SERVICES);
							 }else{
								 descriptiveNoteMapList = new HashMap<String, List<String>>();
							 }
							 List<String> descriptiveNoteList = null;
							 if(descriptiveNoteMapList.size()>0 && descriptiveNoteMapList.get(Eag2012.INTERNET_ACCESS)!=null){
								 descriptiveNoteList = descriptiveNoteMapList.get(Eag2012.INTERNET_ACCESS);
							 }else{
								 descriptiveNoteList = new ArrayList<String>();
							 }
							 descriptiveNoteList.add(accessTable.getString(target1));
							 descriptiveNoteMapList.put(Eag2012.INTERNET_ACCESS,descriptiveNoteList);
							 descriptiveNoteMapMapList.put(Eag2012.TAB_ACCESS_AND_SERVICES,descriptiveNoteMapList);
							 if(descriptiveNotePValue.size()>i){
								 descriptiveNotePValue.set(i,descriptiveNoteMapMapList);
							 }else{
								 descriptiveNotePValue.add(descriptiveNoteMapMapList);
							 }
							 eag2012.setDescriptiveNotePValue(descriptiveNotePValue);
						 }
						 if(accessTable.has(target2)){
							
							 List<Map<String, Map<String, List<String>>>> descriptiveNotePLang = eag2012.getDescriptiveNotePLang();
							 if(descriptiveNotePLang==null){
								 descriptiveNotePLang = new ArrayList<Map<String, Map<String, List<String>>>>();
							 }
							 Map<String, Map<String, List<String>>> descriptiveNoteMapMapList = null;
							 if(descriptiveNotePLang.size()>i && descriptiveNotePLang.get(i)!=null){
								 descriptiveNoteMapMapList = descriptiveNotePLang.get(i);
							 }else{
								 descriptiveNoteMapMapList = new HashMap<String,Map<String,List<String>>>();
							 }
							 Map<String, List<String>> descriptiveNoteMapList = null;
							 if(descriptiveNoteMapMapList.size()>0 && descriptiveNoteMapMapList.get(Eag2012.TAB_ACCESS_AND_SERVICES)!=null){
								 descriptiveNoteMapList = descriptiveNoteMapMapList.get(Eag2012.TAB_ACCESS_AND_SERVICES);
							 }else{
								 descriptiveNoteMapList = new HashMap<String, List<String>>();
							 }
							 List<String> descriptiveNoteList = null;
							 if(descriptiveNoteMapList.size()>0 && descriptiveNoteMapList.get(Eag2012.INTERNET_ACCESS)!=null){
								 descriptiveNoteList = descriptiveNoteMapList.get(Eag2012.INTERNET_ACCESS);
							 }else{
								 descriptiveNoteList = new ArrayList<String>();
							 }
							 descriptiveNoteList.add(accessTable.getString(target2));
							 descriptiveNoteMapList.put(Eag2012.INTERNET_ACCESS,descriptiveNoteList);
							 descriptiveNoteMapMapList.put(Eag2012.TAB_ACCESS_AND_SERVICES,descriptiveNoteMapList);
							 if(descriptiveNotePLang.size()>i){
								 descriptiveNotePLang.set(i,descriptiveNoteMapMapList);
							 }else{
								 descriptiveNotePLang.add(descriptiveNoteMapMapList);
							 }
							 eag2012.setDescriptiveNotePLang(descriptiveNotePLang);
							 	 
						 }
					 }while(accessTable.has(target1) && accessTable.has(target2));
					 
					 //technical services section Restoration Lab
					 
					 if(accessTable.has("selectASTSRestaurationLab")){
						 String restoration = accessTable.getString("selectASTSRestaurationLab");
						 List<String> restorationList = eag2012.getRestorationlabQuestion();
						 if(restorationList == null){
						    restorationList = new ArrayList<String>();
						 }
						 if(restorationList.size()>i){
							 restorationList.set(i,restoration);
						 }else{
						   restorationList.add(restoration);
						 }
						 eag2012.setRestorationlabQuestion(restorationList);
					 }
					 
					 target1 = "textASTSDescriptionOfRestaurationLab";
					 target2 = "selectASTSSelectLanguage";
					 targetNumber = 1;
					 do{
						 target1 = ((target1.indexOf("_")!=-1)?target1.substring(0,target1.indexOf("_")):target1)+"_"+targetNumber;
						 target2 = ((target2.indexOf("_")!=-1)?target2.substring(0,target2.indexOf("_")):target2)+"_"+targetNumber;
						 targetNumber++;
						 if(accessTable.has(target1)){
							 List<Map<String, Map<String, List<String>>>> descriptiveNotePValue = eag2012.getDescriptiveNotePValue();
							 if(descriptiveNotePValue==null){
								 descriptiveNotePValue = new ArrayList<Map<String, Map<String, List<String>>>>();
							 }
							 Map<String, Map<String, List<String>>> descriptiveNoteMapMapList = null;
							 if(descriptiveNotePValue.size()>i && descriptiveNotePValue.get(i)!=null){
								 descriptiveNoteMapMapList = descriptiveNotePValue.get(i);
							 }else{
								 descriptiveNoteMapMapList = new HashMap<String,Map<String,List<String>>>();
							 }
							 Map<String, List<String>> descriptiveNoteMapList = null;
							 if(descriptiveNoteMapMapList.size()>0 && descriptiveNoteMapMapList.get(Eag2012.TAB_ACCESS_AND_SERVICES)!=null){
								 descriptiveNoteMapList = descriptiveNoteMapMapList.get(Eag2012.TAB_ACCESS_AND_SERVICES);
							 }else{
								 descriptiveNoteMapList = new HashMap<String, List<String>>();
							 }
							 List<String> descriptiveNoteList = null;
							 if(descriptiveNoteMapList.size()>0 && descriptiveNoteMapList.get(Eag2012.RESTORATION_LAB)!=null){
								 descriptiveNoteList = descriptiveNoteMapList.get(Eag2012.RESTORATION_LAB);
							 }else{
								 descriptiveNoteList = new ArrayList<String>();
							 }
							 descriptiveNoteList.add(accessTable.getString(target1));
							 descriptiveNoteMapList.put(Eag2012.RESTORATION_LAB,descriptiveNoteList);
							 descriptiveNoteMapMapList.put(Eag2012.TAB_ACCESS_AND_SERVICES,descriptiveNoteMapList);
							 if(descriptiveNotePValue.size()>i){
								 descriptiveNotePValue.set(i,descriptiveNoteMapMapList);
							 }else{
								 descriptiveNotePValue.add(descriptiveNoteMapMapList);
							 }
							 eag2012.setDescriptiveNotePValue(descriptiveNotePValue);
						 }
						 if(accessTable.has(target2)){
							
							 List<Map<String, Map<String, List<String>>>> descriptiveNotePLang = eag2012.getDescriptiveNotePLang();
							 if(descriptiveNotePLang==null){
								 descriptiveNotePLang = new ArrayList<Map<String, Map<String, List<String>>>>();
							 }
							 Map<String, Map<String, List<String>>> descriptiveNoteMapMapList = null;
							 if(descriptiveNotePLang.size()>i && descriptiveNotePLang.get(i)!=null){
								 descriptiveNoteMapMapList = descriptiveNotePLang.get(i);
							 }else{
								 descriptiveNoteMapMapList = new HashMap<String,Map<String,List<String>>>();
							 }
							 Map<String, List<String>> descriptiveNoteMapList = null;
							 if(descriptiveNoteMapMapList.size()>0 && descriptiveNoteMapMapList.get(Eag2012.TAB_ACCESS_AND_SERVICES)!=null){
								 descriptiveNoteMapList = descriptiveNoteMapMapList.get(Eag2012.TAB_ACCESS_AND_SERVICES);
							 }else{
								 descriptiveNoteMapList = new HashMap<String, List<String>>();
							 }
							 List<String> descriptiveNoteList = null;
							 if(descriptiveNoteMapList.size()>0 && descriptiveNoteMapList.get(Eag2012.RESTORATION_LAB)!=null){
								 descriptiveNoteList = descriptiveNoteMapList.get(Eag2012.RESTORATION_LAB);
							 }else{
								 descriptiveNoteList = new ArrayList<String>();
							 }
							 descriptiveNoteList.add(accessTable.getString(target2));
							 descriptiveNoteMapList.put(Eag2012.RESTORATION_LAB,descriptiveNoteList);
							 descriptiveNoteMapMapList.put(Eag2012.TAB_ACCESS_AND_SERVICES,descriptiveNoteMapList);
							 if(descriptiveNotePLang.size()>i){
								 descriptiveNotePLang.set(i,descriptiveNoteMapMapList);
							 }else{
								 descriptiveNotePLang.add(descriptiveNoteMapMapList);
							 }
							 eag2012.setDescriptiveNotePLang(descriptiveNotePLang); 
							 
							 
						 }
					 }while(accessTable.has(target1) && accessTable.has(target2));
				     
					 if(accessTable.has("textASTSTelephone")){
						 List<Map<String, Map<String, List<String>>>> telephones = eag2012.getTelephoneValue();
						 if(telephones==null){
							 telephones = new ArrayList<Map<String, Map<String, List<String>>>>();
						 }
						 Map<String, Map<String, List<String>>> telephonesMap = null;
						 if(telephones.size()>i && telephones.get(i)!=null){ //repo
							 telephonesMap = telephones.get(i);
						 }else{
							 telephonesMap = new HashMap<String, Map<String, List<String>>>(); 
						 }
						 Map<String, List<String>> telephonesMapList = null;
						 if(telephonesMap.size()>0 && telephonesMap.get(Eag2012.TAB_ACCESS_AND_SERVICES)!=null){
							 telephonesMapList = telephonesMap.get(Eag2012.TAB_ACCESS_AND_SERVICES);
						 }else{
							 telephonesMapList = new HashMap<String, List<String>>();
						 }
						 List<String> telephonesList = null;
						 if(telephonesMapList.size()>0 && telephonesMapList.get(Eag2012.RESTORATION_LAB)!=null){
							 telephonesList = telephonesMapList.get(Eag2012.RESTORATION_LAB);
						 }else{
							 telephonesList = new ArrayList<String>();
						 }
						 telephonesList.add(accessTable.getString("textASTSTelephone"));
						 telephonesMapList.put(Eag2012.RESTORATION_LAB, telephonesList);
						 telephonesMap.put(Eag2012.TAB_ACCESS_AND_SERVICES,telephonesMapList);
						 if(telephones.size()>i){
							 telephones.set(i, telephonesMap);
						 }else{
							 telephones.add(telephonesMap);
						 }
						 eag2012.setTelephoneValue(telephones);
					 }
				
					 if(accessTable.has("textASRSEmail")){
						 List<Map<String, Map<String, List<String>>>> listMapEmailValueList = eag2012.getEmailHref();
						 if(listMapEmailValueList==null){
							 listMapEmailValueList = new ArrayList<Map<String, Map<String, List<String>>>>();
						 }
						 Map<String, Map<String, List<String>>> email = null;
						 if(listMapEmailValueList.size()>i && listMapEmailValueList.get(i)!=null){
							 email = listMapEmailValueList.get(i);
						 }else{
							 email = new HashMap<String, Map<String, List<String>>>();
						 }
						 List<String> listEmail = null;
						 Map<String,List<String>> emailMap = null;
						 if(email.size()>0 && email.get(Eag2012.TAB_ACCESS_AND_SERVICES)!=null){
							 emailMap = email.get(Eag2012.TAB_ACCESS_AND_SERVICES);
						 }else{
							 emailMap = new HashMap<String,List<String>>();
						 }
						 if(emailMap.size()>0 && emailMap.get(Eag2012.RESTORATION_LAB)!=null){
							 listEmail = emailMap.get(Eag2012.RESTORATION_LAB);
						 }else{
							 listEmail = new ArrayList<String>();
						 }
						 listEmail.add(accessTable.getString("textASRSEmail"));
						 emailMap.put(Eag2012.RESTORATION_LAB,listEmail); 
						 email.put(Eag2012.TAB_ACCESS_AND_SERVICES, emailMap);
						 if(listMapEmailValueList.size()>i){
							 listMapEmailValueList.set(i,email);
						 }else{
							 listMapEmailValueList.add(email);
						 }
						 eag2012.setEmailHref(listMapEmailValueList);
					 }
	                 
					 if(accessTable.has("textASRSEmailLinkTitle")){
						 List<Map<String, Map<String, List<String>>>> listMapEmailList = eag2012.getEmailValue();
						 if(listMapEmailList==null){
							 listMapEmailList = new ArrayList<Map<String, Map<String, List<String>>>>();
						 }
						 Map<String, Map<String, List<String>>> email = null;
						 if(listMapEmailList.size()>i && listMapEmailList.get(i)!=null){
							 email = listMapEmailList.get(i);
						 }else{
							 email = new HashMap<String, Map<String, List<String>>>();
						 }
						 List<String> listEmail = null;
						 Map<String,List<String>> emailMap = null;
						 if(email.size()>0 && email.get(Eag2012.TAB_ACCESS_AND_SERVICES)!=null){
							 emailMap = email.get(Eag2012.TAB_ACCESS_AND_SERVICES);
						 }else{
							 emailMap = new HashMap<String,List<String>>();
						 }
						 if(emailMap.size()>0 && emailMap.get(Eag2012.RESTORATION_LAB)!=null){
							 listEmail = emailMap.get(Eag2012.RESTORATION_LAB);
						 }else{
							 listEmail = new ArrayList<String>();
						 }
						 listEmail.add(accessTable.getString("textASRSEmailLinkTitle"));
						 emailMap.put(Eag2012.RESTORATION_LAB,listEmail); 
						 email.put(Eag2012.TAB_ACCESS_AND_SERVICES, emailMap);
						 if(listMapEmailList.size()>i){
							 listMapEmailList.set(i,email);
						 }else{
							 listMapEmailList.add(email);
						 }
						 eag2012.setEmailValue(listMapEmailList);
					 }
	                
					 if(accessTable.has("textASRSWebpage")){
						 List<Map<String, Map<String, List<String>>>> listMapWebpagelList = eag2012.getWebpageHref();
						 if(listMapWebpagelList==null){
							 listMapWebpagelList = new ArrayList<Map<String, Map<String, List<String>>>>();
						 }
						 Map<String, Map<String, List<String>>> webpage = null;
						 if(listMapWebpagelList.size()>i && listMapWebpagelList.get(i)!=null){
							 webpage = listMapWebpagelList.get(i);
						 }else{
							 webpage = new HashMap<String, Map<String, List<String>>>();
						 }
						 List<String> listWebpage = null;
						 Map<String,List<String>> webpageMap = null;
						 if(webpage.size()>0 && webpage.get(Eag2012.TAB_ACCESS_AND_SERVICES)!=null){
							 webpageMap = webpage.get(Eag2012.TAB_ACCESS_AND_SERVICES);
						 }else{
							 webpageMap = new HashMap<String,List<String>>();
						 }
						 if(webpageMap.size()>0 && webpageMap.get(Eag2012.RESTORATION_LAB)!=null){
							 listWebpage = webpageMap.get(Eag2012.RESTORATION_LAB);
						 }else{
							 listWebpage = new ArrayList<String>();
						 }
						 listWebpage.add(accessTable.getString("textASRSWebpage"));
						 webpageMap.put(Eag2012.RESTORATION_LAB,listWebpage); //root section, here there is only one mails list
						 webpage.put(Eag2012.TAB_ACCESS_AND_SERVICES, webpageMap);
						 if(listMapWebpagelList.size()>i){
							 listMapWebpagelList.set(i,webpage);
						 }else{
							 listMapWebpagelList.add(webpage);
						 }
						 eag2012.setWebpageHref(listMapWebpagelList);
					 }
					 if(accessTable.has("textASRSWebpageLinkTitle")){
						 List<Map<String, Map<String, List<String>>>> listMapWebpageValueList = eag2012.getWebpageValue();
						 if(listMapWebpageValueList==null){
							 listMapWebpageValueList = new ArrayList<Map<String, Map<String, List<String>>>>();
						 }
						 Map<String, Map<String, List<String>>> webpage = null;
						 if(listMapWebpageValueList.size()>i && listMapWebpageValueList.get(i)!=null){
							 webpage = listMapWebpageValueList.get(i);
						 }else{
							 webpage = new HashMap<String, Map<String, List<String>>>();
						 }
						 List<String> listWebpage = null;
						 Map<String,List<String>> webpageMap = null;
						 if(webpage.size()>0 && webpage.get(Eag2012.TAB_ACCESS_AND_SERVICES)!=null){
							 webpageMap = webpage.get(Eag2012.TAB_ACCESS_AND_SERVICES);
						 }else{
							 webpageMap = new HashMap<String,List<String>>();
						 }
						 if(webpageMap.size()>0 && webpageMap.get(Eag2012.RESTORATION_LAB)!=null){
							 listWebpage = webpageMap.get(Eag2012.RESTORATION_LAB);
						 }else{
							 listWebpage = new ArrayList<String>();
						 }
						 listWebpage.add(accessTable.getString("textASRSWebpageLinkTitle"));
						 webpageMap.put(Eag2012.RESTORATION_LAB,listWebpage); //root section, here there is only one mails list
						 webpage.put(Eag2012.TAB_ACCESS_AND_SERVICES, webpageMap);
						 if(listMapWebpageValueList.size()>i){
							 listMapWebpageValueList.set(i,webpage);
						 }else{
							 listMapWebpageValueList.add(webpage);
						 }
						 eag2012.setWebpageValue(listMapWebpageValueList);
					 }
					 
				    //Reproductions services section
					 
					 if(accessTable.has("selectASTSReproductionService")){
						 String reproductionser = accessTable.getString("selectASTSReproductionService");
						 List<String> reproductionserList = eag2012.getReproductionserQuestion();
						 if(reproductionserList == null){
						    reproductionserList = new ArrayList<String>();
						 }
						 if(reproductionserList.size()>i){
							 reproductionserList.set(i,reproductionser);
						 }else{
						   reproductionserList.add(reproductionser);
						 } 
						 eag2012.setReproductionserQuestion(reproductionserList);
					 }
					 
					 target1 = "textASTSDescriptionOfReproductionService";
					 target2 = "selectASTSRSSelectLanguage";
					 targetNumber = 1;
					 do{
						 target1 = ((target1.indexOf("_")!=-1)?target1.substring(0,target1.indexOf("_")):target1)+"_"+targetNumber;
						 target2 = ((target2.indexOf("_")!=-1)?target2.substring(0,target2.indexOf("_")):target2)+"_"+targetNumber;
						 targetNumber++;
						 if(accessTable.has(target1)){
							 List<Map<String, Map<String, List<String>>>> descriptiveNotePValue = eag2012.getDescriptiveNotePValue();
							 if(descriptiveNotePValue==null){
								 descriptiveNotePValue = new ArrayList<Map<String, Map<String, List<String>>>>();
							 }
							 Map<String, Map<String, List<String>>> descriptiveNoteMapMapList = null;
							 if(descriptiveNotePValue.size()>i && descriptiveNotePValue.get(i)!=null){
								 descriptiveNoteMapMapList = descriptiveNotePValue.get(i);
							 }else{
								 descriptiveNoteMapMapList = new HashMap<String,Map<String,List<String>>>();
							 }
							 Map<String, List<String>> descriptiveNoteMapList = null;
							 if(descriptiveNoteMapMapList.size()>0 && descriptiveNoteMapMapList.get(Eag2012.TAB_ACCESS_AND_SERVICES)!=null){
								 descriptiveNoteMapList = descriptiveNoteMapMapList.get(Eag2012.TAB_ACCESS_AND_SERVICES);
							 }else{
								 descriptiveNoteMapList = new HashMap<String, List<String>>();
							 }
							 List<String> descriptiveNoteList = null;
							 if(descriptiveNoteMapList.size()>0 && descriptiveNoteMapList.get(Eag2012.REPRODUCTIONSER)!=null){
								 descriptiveNoteList = descriptiveNoteMapList.get(Eag2012.REPRODUCTIONSER);
							 }else{
								 descriptiveNoteList = new ArrayList<String>();
							 }
							 descriptiveNoteList.add(accessTable.getString(target1));
							 descriptiveNoteMapList.put(Eag2012.REPRODUCTIONSER,descriptiveNoteList);
							 descriptiveNoteMapMapList.put(Eag2012.TAB_ACCESS_AND_SERVICES,descriptiveNoteMapList);
							 if(descriptiveNotePValue.size()>i){
								 descriptiveNotePValue.set(i,descriptiveNoteMapMapList);
							 }else{
								 descriptiveNotePValue.add(descriptiveNoteMapMapList);
							 }
							 eag2012.setDescriptiveNotePValue(descriptiveNotePValue);
						 }
						 if(accessTable.has(target2)){
							
							 List<Map<String, Map<String, List<String>>>> descriptiveNotePLang = eag2012.getDescriptiveNotePLang();
							 if(descriptiveNotePLang==null){
								 descriptiveNotePLang = new ArrayList<Map<String, Map<String, List<String>>>>();
							 }
							 Map<String, Map<String, List<String>>> descriptiveNoteMapMapList = null;
							 if(descriptiveNotePLang.size()>i && descriptiveNotePLang.get(i)!=null){
								 descriptiveNoteMapMapList = descriptiveNotePLang.get(i);
							 }else{
								 descriptiveNoteMapMapList = new HashMap<String,Map<String,List<String>>>();
							 }
							 Map<String, List<String>> descriptiveNoteMapList = null;
							 if(descriptiveNoteMapMapList.size()>0 && descriptiveNoteMapMapList.get(Eag2012.TAB_ACCESS_AND_SERVICES)!=null){
								 descriptiveNoteMapList = descriptiveNoteMapMapList.get(Eag2012.TAB_ACCESS_AND_SERVICES);
							 }else{
								 descriptiveNoteMapList = new HashMap<String, List<String>>();
							 }
							 List<String> descriptiveNoteList = null;
							 if(descriptiveNoteMapList.size()>0 && descriptiveNoteMapList.get(Eag2012.REPRODUCTIONSER)!=null){
								 descriptiveNoteList = descriptiveNoteMapList.get(Eag2012.REPRODUCTIONSER);
							 }else{
								 descriptiveNoteList = new ArrayList<String>();
							 }
							 descriptiveNoteList.add(accessTable.getString(target2));
							 descriptiveNoteMapList.put(Eag2012.REPRODUCTIONSER,descriptiveNoteList);
							 descriptiveNoteMapMapList.put(Eag2012.TAB_ACCESS_AND_SERVICES,descriptiveNoteMapList);
							 if(descriptiveNotePLang.size()>i){
								 descriptiveNotePLang.set(i,descriptiveNoteMapMapList);
							 }else{
								 descriptiveNotePLang.add(descriptiveNoteMapMapList);
							 }
							 eag2012.setDescriptiveNotePLang(descriptiveNotePLang); 
							 
							 
						 }
					 }while(accessTable.has(target1) && accessTable.has(target2));
				     
					 if(accessTable.has("textASTSRSTelephone")){
						 List<Map<String, Map<String, List<String>>>> telephones = eag2012.getTelephoneValue();
						 if(telephones==null){
							 telephones = new ArrayList<Map<String, Map<String, List<String>>>>();
						 }
						 Map<String, Map<String, List<String>>> telephonesMap = null;
						 if(telephones.size()>i && telephones.get(i)!=null){ //repo
							 telephonesMap = telephones.get(i);
						 }else{
							 telephonesMap = new HashMap<String, Map<String, List<String>>>(); 
						 }
						 Map<String, List<String>> telephonesMapList = null;
						 if(telephonesMap.size()>0 && telephonesMap.get(Eag2012.TAB_ACCESS_AND_SERVICES)!=null){
							 telephonesMapList = telephonesMap.get(Eag2012.TAB_ACCESS_AND_SERVICES);
						 }else{
							 telephonesMapList = new HashMap<String, List<String>>();
						 }
						 List<String> telephonesList = null;
						 if(telephonesMapList.size()>0 && telephonesMapList.get(Eag2012.REPRODUCTIONSER)!=null){
							 telephonesList = telephonesMapList.get(Eag2012.REPRODUCTIONSER);
						 }else{
							 telephonesList = new ArrayList<String>();
						 }
						 telephonesList.add(accessTable.getString("textASTSRSTelephone"));
						 telephonesMapList.put(Eag2012.REPRODUCTIONSER, telephonesList);
						 telephonesMap.put(Eag2012.TAB_ACCESS_AND_SERVICES,telephonesMapList);
						 if(telephones.size()>i){
							 telephones.set(i, telephonesMap);
						 }else{
							 telephones.add(telephonesMap);
						 }
						 eag2012.setTelephoneValue(telephones);
					 }
				
					 if(accessTable.has("textASTSRSEmailAddress")){
						 List<Map<String, Map<String, List<String>>>> listMapEmailValueList = eag2012.getEmailHref();
						 if(listMapEmailValueList==null){
							 listMapEmailValueList = new ArrayList<Map<String, Map<String, List<String>>>>();
						 }
						 Map<String, Map<String, List<String>>> email = null;
						 if(listMapEmailValueList.size()>i && listMapEmailValueList.get(i)!=null){
							 email = listMapEmailValueList.get(i);
						 }else{
							 email = new HashMap<String, Map<String, List<String>>>();
						 }
						 List<String> listEmail = null;
						 Map<String,List<String>> emailMap = null;
						 if(email.size()>0 && email.get(Eag2012.TAB_ACCESS_AND_SERVICES)!=null){
							 emailMap = email.get(Eag2012.TAB_ACCESS_AND_SERVICES);
						 }else{
							 emailMap = new HashMap<String,List<String>>();
						 }
						 if(emailMap.size()>0 && emailMap.get(Eag2012.REPRODUCTIONSER)!=null){
							 listEmail = emailMap.get(Eag2012.REPRODUCTIONSER);
						 }else{
							 listEmail = new ArrayList<String>();
						 }
						 listEmail.add(accessTable.getString("textASTSRSEmailAddress"));
						 emailMap.put(Eag2012.REPRODUCTIONSER,listEmail); 
						 email.put(Eag2012.TAB_ACCESS_AND_SERVICES, emailMap);
						 if(listMapEmailValueList.size()>i){
							 listMapEmailValueList.set(i,email);
						 }else{
							 listMapEmailValueList.add(email);
						 }
						 eag2012.setEmailHref(listMapEmailValueList);
					 }
	                 
					 if(accessTable.has("textASTSEmailAddressLinkTitle")){
						 List<Map<String, Map<String, List<String>>>> listMapEmailList = eag2012.getEmailValue();
						 if(listMapEmailList==null){
							 listMapEmailList = new ArrayList<Map<String, Map<String, List<String>>>>();
						 }
						 Map<String, Map<String, List<String>>> email = null;
						 if(listMapEmailList.size()>i && listMapEmailList.get(i)!=null){
							 email = listMapEmailList.get(i);
						 }else{
							 email = new HashMap<String, Map<String, List<String>>>();
						 }
						 List<String> listEmail = null;
						 Map<String,List<String>> emailMap = null;
						 if(email.size()>0 && email.get(Eag2012.TAB_ACCESS_AND_SERVICES)!=null){
							 emailMap = email.get(Eag2012.TAB_ACCESS_AND_SERVICES);
						 }else{
							 emailMap = new HashMap<String,List<String>>();
						 }
						 if(emailMap.size()>0 && emailMap.get(Eag2012.REPRODUCTIONSER)!=null){
							 listEmail = emailMap.get(Eag2012.REPRODUCTIONSER);
						 }else{
							 listEmail = new ArrayList<String>();
						 }
						 listEmail.add(accessTable.getString("textASTSEmailAddressLinkTitle"));
						 emailMap.put(Eag2012.REPRODUCTIONSER,listEmail); 
						 email.put(Eag2012.TAB_ACCESS_AND_SERVICES, emailMap);
						 if(listMapEmailList.size()>i){
							 listMapEmailList.set(i,email);
						 }else{
							 listMapEmailList.add(email);
						 }
						 eag2012.setEmailValue(listMapEmailList);
					 }
	                
					 if(accessTable.has("textASTSRSWebpage")){
						 List<Map<String, Map<String, List<String>>>> listMapWebpagelList = eag2012.getWebpageHref();
						 if(listMapWebpagelList==null){
							 listMapWebpagelList = new ArrayList<Map<String, Map<String, List<String>>>>();
						 }
						 Map<String, Map<String, List<String>>> webpage = null;
						 if(listMapWebpagelList.size()>i && listMapWebpagelList.get(i)!=null){
							 webpage = listMapWebpagelList.get(i);
						 }else{
							 webpage = new HashMap<String, Map<String, List<String>>>();
						 }
						 List<String> listWebpage = null;
						 Map<String,List<String>> webpageMap = null;
						 if(webpage.size()>0 && webpage.get(Eag2012.TAB_ACCESS_AND_SERVICES)!=null){
							 webpageMap = webpage.get(Eag2012.TAB_ACCESS_AND_SERVICES);
						 }else{
							 webpageMap = new HashMap<String,List<String>>();
						 }
						 if(webpageMap.size()>0 && webpageMap.get(Eag2012.REPRODUCTIONSER)!=null){
							 listWebpage = webpageMap.get(Eag2012.REPRODUCTIONSER);
						 }else{
							 listWebpage = new ArrayList<String>();
						 }
						 listWebpage.add(accessTable.getString("textASTSRSWebpage"));
						 webpageMap.put(Eag2012.REPRODUCTIONSER,listWebpage); //root section, here there is only one mails list
						 webpage.put(Eag2012.TAB_ACCESS_AND_SERVICES, webpageMap);
						 if(listMapWebpagelList.size()>i){
							 listMapWebpagelList.set(i,webpage);
						 }else{
							 listMapWebpagelList.add(webpage);
						 }
						 eag2012.setWebpageHref(listMapWebpagelList);
					 }
					 if(accessTable.has("textASTSRSWebpageLinkTitle")){
						 List<Map<String, Map<String, List<String>>>> listMapWebpageValueList = eag2012.getWebpageValue();
						 if(listMapWebpageValueList==null){
							 listMapWebpageValueList = new ArrayList<Map<String, Map<String, List<String>>>>();
						 }
						 Map<String, Map<String, List<String>>> webpage = null;
						 if(listMapWebpageValueList.size()>i && listMapWebpageValueList.get(i)!=null){
							 webpage = listMapWebpageValueList.get(i);
						 }else{
							 webpage = new HashMap<String, Map<String, List<String>>>();
						 }
						 List<String> listWebpage = null;
						 Map<String,List<String>> webpageMap = null;
						 if(webpage.size()>0 && webpage.get(Eag2012.TAB_ACCESS_AND_SERVICES)!=null){
							 webpageMap = webpage.get(Eag2012.TAB_ACCESS_AND_SERVICES);
						 }else{
							 webpageMap = new HashMap<String,List<String>>();
						 }
						 if(webpageMap.size()>0 && webpageMap.get(Eag2012.REPRODUCTIONSER)!=null){
							 listWebpage = webpageMap.get(Eag2012.REPRODUCTIONSER);
						 }else{
							 listWebpage = new ArrayList<String>();
						 }
						 listWebpage.add(accessTable.getString("textASTSRSWebpageLinkTitle"));
						 webpageMap.put(Eag2012.REPRODUCTIONSER,listWebpage); //root section, here there is only one mails list
						 webpage.put(Eag2012.TAB_ACCESS_AND_SERVICES, webpageMap);
						 if(listMapWebpageValueList.size()>i){
							 listMapWebpageValueList.set(i,webpage);
						 }else{
							 listMapWebpageValueList.add(webpage);
						 }
						 eag2012.setWebpageValue(listMapWebpageValueList);
					 }
                     
					 //microform
					 if(accessTable.has("selectASTSRSMicroform")){
						 String microform = accessTable.getString("selectASTSRSMicroform");
						 List<String> microformList = eag2012.getMicroformserQuestion();
						 if(microformList == null){
						    microformList = new ArrayList<String>();
						 }
						 if(microformList.size()>i){
							 microformList.set(i,microform);
						 }else{
						   microformList.add(microform);
						 } 
						 eag2012.setMicroformserQuestion(microformList);
					 }
					 
					 
					 //photograph
					 if(accessTable.has("selectASTSRSPhotographServices")){
						 String photograph = accessTable.getString("selectASTSRSPhotographServices");
						 List<String> photographList = eag2012.getMicroformserQuestion();
						 if(photographList == null){
							 photographList = new ArrayList<String>();
						 }
						 if(photographList.size()>i){
							 photographList.set(i,photograph);
						 }else{
							 photographList.add(photograph);
						 } 
						 eag2012.setPhotographserQuestion(photographList);
					 }
					 
					 
					 //digitalser
					 if(accessTable.has("selectASTSRSDigitalServices")){
						 String digitalser = accessTable.getString("selectASTSRSDigitalServices");
						 List<String> digitalserList = eag2012.getDigitalserQuestion();
						 if(digitalserList  == null){
							 digitalserList  = new ArrayList<String>();
						 }
						 if(digitalserList.size()>i){
							 digitalserList.set(i,digitalser);
						 }else{
							 digitalserList.add(digitalser);
						 } 
						 eag2012.setDigitalserQuestion(digitalserList);
					 }
					 
					 //photocopyser
					 if(accessTable.has("selectASTSRSPhotocopyServices")){
						 String photocopyser = accessTable.getString("selectASTSRSPhotocopyServices");
						 List<String> photocopyserList = eag2012.getPhotocopyserQuestion();
						 if(photocopyserList  == null){
							 photocopyserList  = new ArrayList<String>();
						 }
						 if(photocopyserList.size()>i){
							 photocopyserList.set(i,photocopyser);
						 }else{
							 photocopyserList.add(photocopyser);
						 } 
						 eag2012.setPhotocopyserQuestion(photocopyserList);
					 }
					
					//Recreational services
				 	// Refreshment area and refreshment area lang
					j = 0;
					while((accessTable.has("textASReSeRefreshment_"+(++j))) && (accessTable.has("selectASReSeRefreshmentSelectLanguage_"+(j)))){
						if(accessTable.has("textASReSeRefreshment_"+j)){
							List<Map<String, Map<String, List<String>>>> descriptiveNotePValue = eag2012.getDescriptiveNotePValue();
							if(descriptiveNotePValue==null){
								descriptiveNotePValue = new ArrayList<Map<String, Map<String, List<String>>>>();
							}
							Map<String, Map<String, List<String>>> descriptiveNoteMapMapList = null;
							if(descriptiveNotePValue.size()>i && descriptiveNotePValue.get(i)!=null){
								descriptiveNoteMapMapList = descriptiveNotePValue.get(i);
							}else{
								descriptiveNoteMapMapList = new HashMap<String,Map<String,List<String>>>();
							}
							Map<String, List<String>> descriptiveNoteMapList = null;
							if(descriptiveNoteMapMapList.size()>0 && descriptiveNoteMapMapList.get(Eag2012.TAB_ACCESS_AND_SERVICES)!=null){
								descriptiveNoteMapList = descriptiveNoteMapMapList.get(Eag2012.TAB_ACCESS_AND_SERVICES);
							}else{
								descriptiveNoteMapList = new HashMap<String, List<String>>();
							}
							List<String> descriptiveNoteList = null;
							if(descriptiveNoteMapList.size()>0 && descriptiveNoteMapList.get(Eag2012.REFRESHMENT)!=null){
								descriptiveNoteList = descriptiveNoteMapList.get(Eag2012.REFRESHMENT);
							}else{
								descriptiveNoteList = new ArrayList<String>();
							}
							descriptiveNoteList.add(accessTable.getString("textASReSeRefreshment_"+j));
							descriptiveNoteMapList.put(Eag2012.REFRESHMENT,descriptiveNoteList);
							descriptiveNoteMapMapList.put(Eag2012.TAB_ACCESS_AND_SERVICES,descriptiveNoteMapList);
							if(descriptiveNotePValue.size()>i){
								descriptiveNotePValue.set(i,descriptiveNoteMapMapList);
							}else{
								descriptiveNotePValue.add(descriptiveNoteMapMapList);
							}
							eag2012.setDescriptiveNotePValue(descriptiveNotePValue);
						}
						if(accessTable.has("selectASReSeRefreshmentSelectLanguage_"+j)){
							List<Map<String, Map<String, List<String>>>> descriptiveNotePValue = eag2012.getDescriptiveNotePLang();
							if(descriptiveNotePValue==null){
								descriptiveNotePValue = new ArrayList<Map<String, Map<String, List<String>>>>();
							}
							Map<String, Map<String, List<String>>> descriptiveNoteMapMapList = null;
							if(descriptiveNotePValue.size()>i && descriptiveNotePValue.get(i)!=null){
								descriptiveNoteMapMapList = descriptiveNotePValue.get(i);
							}else{
								descriptiveNoteMapMapList = new HashMap<String,Map<String,List<String>>>();
							}
							Map<String, List<String>> descriptiveNoteMapList = null;
							if(descriptiveNoteMapMapList.size()>0 && descriptiveNoteMapMapList.get(Eag2012.TAB_ACCESS_AND_SERVICES)!=null){
								descriptiveNoteMapList = descriptiveNoteMapMapList.get(Eag2012.TAB_ACCESS_AND_SERVICES);
							}else{
								descriptiveNoteMapList = new HashMap<String, List<String>>();
							}
							List<String> descriptiveNoteList = null;
							if(descriptiveNoteMapList.size()>0 && descriptiveNoteMapList.get(Eag2012.REFRESHMENT)!=null){
								descriptiveNoteList = descriptiveNoteMapList.get(Eag2012.REFRESHMENT);
							}else{
								descriptiveNoteList = new ArrayList<String>();
							}
							descriptiveNoteList.add(accessTable.getString("selectASReSeRefreshmentSelectLanguage_"+j));
							descriptiveNoteMapList.put(Eag2012.REFRESHMENT,descriptiveNoteList);
							descriptiveNoteMapMapList.put(Eag2012.TAB_ACCESS_AND_SERVICES,descriptiveNoteMapList);
							if(descriptiveNotePValue.size()>i){
								descriptiveNotePValue.set(i,descriptiveNoteMapMapList);
							}else{
								descriptiveNotePValue.add(descriptiveNoteMapMapList);
							}
							eag2012.setDescriptiveNotePLang(descriptiveNotePValue);
						}
					}

						target1 = "textASReSeExhibition";
						target2 = "textASReSeWebpage";
						target3 = "textASReSeWebpageLinkTitle";
						String target4= "selectASReSeExhibitionSelectLanguage";
						targetNumber = 1;
						do{
							target1 = ((target1.indexOf("_")!=-1)?target1.substring(0,target1.indexOf("_")):target1)+"_"+targetNumber;
							target2 = ((target2.indexOf("_")!=-1)?target2.substring(0,target2.indexOf("_")):target2)+"_"+targetNumber;
							target3 = ((target3.indexOf("_")!=-1)?target3.substring(0,target3.indexOf("_")):target3)+"_"+targetNumber;
							target4 = ((target4.indexOf("_")!=-1)?target4.substring(0,target4.indexOf("_")):target4)+"_"+targetNumber;
							targetNumber++;
							if(accessTable.has(target1)){
								List<Map<String, Map<String, List<String>>>> descriptiveNotePValue = eag2012.getDescriptiveNotePValue();
								if(descriptiveNotePValue==null){
									descriptiveNotePValue = new ArrayList<Map<String, Map<String, List<String>>>>();
								}
								Map<String, Map<String, List<String>>> descriptiveNoteMapMapList = null;
								if(descriptiveNotePValue.size()>i && descriptiveNotePValue.get(i)!=null){
									descriptiveNoteMapMapList = descriptiveNotePValue.get(i);
								}else{
									descriptiveNoteMapMapList = new HashMap<String,Map<String,List<String>>>();
								}
								Map<String, List<String>> descriptiveNoteMapList = null;
								if(descriptiveNoteMapMapList.size()>0 && descriptiveNoteMapMapList.get(Eag2012.TAB_ACCESS_AND_SERVICES)!=null){
									descriptiveNoteMapList = descriptiveNoteMapMapList.get(Eag2012.TAB_ACCESS_AND_SERVICES);
								}else{
									descriptiveNoteMapList = new HashMap<String, List<String>>();
								}
								List<String> descriptiveNoteList = null;
								if(descriptiveNoteMapList.size()>0 && descriptiveNoteMapList.get(Eag2012.EXHIBITION)!=null){
									descriptiveNoteList = descriptiveNoteMapList.get(Eag2012.EXHIBITION);
								}else{
									descriptiveNoteList = new ArrayList<String>();
								}
								descriptiveNoteList.add(accessTable.getString(target1));
								descriptiveNoteMapList.put(Eag2012.EXHIBITION,descriptiveNoteList);
								descriptiveNoteMapMapList.put(Eag2012.TAB_ACCESS_AND_SERVICES,descriptiveNoteMapList);
								if(descriptiveNotePValue.size()>i){
									descriptiveNotePValue.set(i,descriptiveNoteMapMapList);
								}else{
									descriptiveNotePValue.add(descriptiveNoteMapMapList);
								}
								eag2012.setDescriptiveNotePValue(descriptiveNotePValue);
							}
							if(accessTable.has(target2)){
								List<Map<String, Map<String, List<String>>>> listMapWebpageHrefList = eag2012.getWebpageHref();
								 if(listMapWebpageHrefList==null){
									 listMapWebpageHrefList = new ArrayList<Map<String, Map<String, List<String>>>>();
								 }
								 Map<String, Map<String, List<String>>> mapMapListWeb = null;
								 if(listMapWebpageHrefList.size()>i && listMapWebpageHrefList.get(i)!=null){
									 mapMapListWeb = listMapWebpageHrefList.get(i);
								 }else{
									 mapMapListWeb = new HashMap<String, Map<String, List<String>>>();
								 }
								 List<String> listWeb = null;
								 Map<String,List<String>> webMap = null;
								 if(mapMapListWeb.size()>0 && mapMapListWeb.get(Eag2012.TAB_ACCESS_AND_SERVICES)!=null){
									 webMap = mapMapListWeb.get(Eag2012.TAB_ACCESS_AND_SERVICES);
								 }else{
									 webMap = new HashMap<String,List<String>>();
								 }
								 if(webMap.size()>0 && webMap.get(Eag2012.EXHIBITION)!=null){
									 listWeb = webMap.get(Eag2012.EXHIBITION);
								 }else{
									 listWeb = new ArrayList<String>();
								 }
								 listWeb.add(accessTable.getString(target2));
								 webMap.put(Eag2012.EXHIBITION,listWeb);
								 mapMapListWeb.put(Eag2012.TAB_ACCESS_AND_SERVICES,webMap);
								 if(listMapWebpageHrefList.size()>i){
									 listMapWebpageHrefList.set(i,mapMapListWeb);
								 }else{
									 listMapWebpageHrefList.add(mapMapListWeb);
								 }
								 eag2012.setWebpageHref(listMapWebpageHrefList);
							}
							if(accessTable.has(target3)){
								List<Map<String, Map<String, List<String>>>> listMapWebList = eag2012.getWebpageValue();
								if(listMapWebList==null){
									listMapWebList = new ArrayList<Map<String, Map<String, List<String>>>>();
								}
								Map<String, Map<String, List<String>>> mapMapListWeb = null;
								if(listMapWebList.size()>i && listMapWebList.get(i)!=null){
									mapMapListWeb = listMapWebList.get(i);
								}else{
									mapMapListWeb = new HashMap<String, Map<String, List<String>>>();
								}
								List<String> listWeb = null;
								Map<String,List<String>> webMap = null;
								if(mapMapListWeb.size()>0 && mapMapListWeb.get(Eag2012.TAB_ACCESS_AND_SERVICES)!=null){
									webMap = mapMapListWeb.get(Eag2012.TAB_ACCESS_AND_SERVICES);
								}else{
									webMap = new HashMap<String,List<String>>();
								}
								if(webMap.size()>0 && webMap.get(Eag2012.EXHIBITION)!=null){
									listWeb = webMap.get(Eag2012.EXHIBITION);
								}else{
									listWeb = new ArrayList<String>();
								}
								listWeb.add(accessTable.getString(target3));
								webMap.put(Eag2012.EXHIBITION,listWeb);
								mapMapListWeb.put(Eag2012.TAB_ACCESS_AND_SERVICES,webMap);
								if(listMapWebList.size()>i){
									listMapWebList.set(i,mapMapListWeb);
								}else{
									listMapWebList.add(mapMapListWeb);
								}
								eag2012.setWebpageValue(listMapWebList); 
							}
							if(accessTable.has(target4)){
								List<Map<String, Map<String, List<String>>>> descriptiveNotePValue = eag2012.getDescriptiveNotePLang();
								if(descriptiveNotePValue==null){
									descriptiveNotePValue = new ArrayList<Map<String, Map<String, List<String>>>>();
								}
								Map<String, Map<String, List<String>>> descriptiveNoteMapMapList = null;
								if(descriptiveNotePValue.size()>i && descriptiveNotePValue.get(i)!=null){
									descriptiveNoteMapMapList = descriptiveNotePValue.get(i);
								}else{
									descriptiveNoteMapMapList = new HashMap<String,Map<String,List<String>>>();
								}
								Map<String, List<String>> descriptiveNoteMapList = null;
								if(descriptiveNoteMapMapList.size()>0 && descriptiveNoteMapMapList.get(Eag2012.TAB_ACCESS_AND_SERVICES)!=null){
									descriptiveNoteMapList = descriptiveNoteMapMapList.get(Eag2012.TAB_ACCESS_AND_SERVICES);
								}else{
									descriptiveNoteMapList = new HashMap<String, List<String>>();
								}
								List<String> descriptiveNoteList = null;
								if(descriptiveNoteMapList.size()>0 && descriptiveNoteMapList.get(Eag2012.EXHIBITION)!=null){
									descriptiveNoteList = descriptiveNoteMapList.get(Eag2012.EXHIBITION);
								}else{
									descriptiveNoteList = new ArrayList<String>();
								}
								descriptiveNoteList.add(accessTable.getString(target4));
								descriptiveNoteMapList.put(Eag2012.EXHIBITION,descriptiveNoteList);
								descriptiveNoteMapMapList.put(Eag2012.TAB_ACCESS_AND_SERVICES,descriptiveNoteMapList);
								if(descriptiveNotePValue.size()>i){
									descriptiveNotePValue.set(i,descriptiveNoteMapMapList);
								}else{
									descriptiveNotePValue.add(descriptiveNoteMapMapList);
								}
								eag2012.setDescriptiveNotePLang(descriptiveNotePValue);
							}
						}while(accessTable.has(target1) && accessTable.has(target2) && accessTable.has(target3) && accessTable.has(target4));
						
						target1 = "textASReSeToursAndSessions";
						target2 = "textASReSeTSWebpage";
						target3 = "textASReSeWebpageTSLinkTitle";
						target4 = "selectASReSeToursAndSessionsSelectLanguage";
						targetNumber = 1;
						do{
							target1 = ((target1.indexOf("_")!=-1)?target1.substring(0,target1.indexOf("_")):target1)+"_"+targetNumber;
							target2 = ((target2.indexOf("_")!=-1)?target2.substring(0,target2.indexOf("_")):target2)+"_"+targetNumber;
							target3 = ((target3.indexOf("_")!=-1)?target3.substring(0,target3.indexOf("_")):target3)+"_"+targetNumber;
							target4 = ((target4.indexOf("_")!=-1)?target4.substring(0,target4.indexOf("_")):target4)+"_"+targetNumber;
							targetNumber++;
							if(accessTable.has(target1)){
								List<Map<String, Map<String, List<String>>>> descriptiveNotePValue = eag2012.getDescriptiveNotePValue();
								if(descriptiveNotePValue==null){
									descriptiveNotePValue = new ArrayList<Map<String, Map<String, List<String>>>>();
								}
								Map<String, Map<String, List<String>>> descriptiveNoteMapMapList = null;
								if(descriptiveNotePValue.size()>i && descriptiveNotePValue.get(i)!=null){
									descriptiveNoteMapMapList = descriptiveNotePValue.get(i);
								}else{
									descriptiveNoteMapMapList = new HashMap<String,Map<String,List<String>>>();
								}
								Map<String, List<String>> descriptiveNoteMapList = null;
								if(descriptiveNoteMapMapList.size()>0 && descriptiveNoteMapMapList.get(Eag2012.TAB_ACCESS_AND_SERVICES)!=null){
									descriptiveNoteMapList = descriptiveNoteMapMapList.get(Eag2012.TAB_ACCESS_AND_SERVICES);
								}else{
									descriptiveNoteMapList = new HashMap<String, List<String>>();
								}
								List<String> descriptiveNoteList = null;
								if(descriptiveNoteMapList.size()>0 && descriptiveNoteMapList.get(Eag2012.TOURS_SESSIONS)!=null){
									descriptiveNoteList = descriptiveNoteMapList.get(Eag2012.TOURS_SESSIONS);
								}else{
									descriptiveNoteList = new ArrayList<String>();
								}
								descriptiveNoteList.add(accessTable.getString(target1));
								descriptiveNoteMapList.put(Eag2012.TOURS_SESSIONS,descriptiveNoteList);
								descriptiveNoteMapMapList.put(Eag2012.TAB_ACCESS_AND_SERVICES,descriptiveNoteMapList);
								if(descriptiveNotePValue.size()>i){
									descriptiveNotePValue.set(i,descriptiveNoteMapMapList);
								}else{
									descriptiveNotePValue.add(descriptiveNoteMapMapList);
								}
								eag2012.setDescriptiveNotePValue(descriptiveNotePValue);
							}
							if(accessTable.has(target2)){
								List<Map<String, Map<String, List<String>>>> listMapWebpageHrefList = eag2012.getWebpageHref();
								 if(listMapWebpageHrefList==null){
									 listMapWebpageHrefList = new ArrayList<Map<String, Map<String, List<String>>>>();
								 }
								 Map<String, Map<String, List<String>>> mapMapListWeb = null;
								 if(listMapWebpageHrefList.size()>i && listMapWebpageHrefList.get(i)!=null){
									 mapMapListWeb = listMapWebpageHrefList.get(i);
								 }else{
									 mapMapListWeb = new HashMap<String, Map<String, List<String>>>();
								 }
								 List<String> listWeb = null;
								 Map<String,List<String>> webMap = null;
								 if(mapMapListWeb.size()>0 && mapMapListWeb.get(Eag2012.TAB_ACCESS_AND_SERVICES)!=null){
									 webMap = mapMapListWeb.get(Eag2012.TAB_ACCESS_AND_SERVICES);
								 }else{
									 webMap = new HashMap<String,List<String>>();
								 }
								 if(webMap.size()>0 && webMap.get(Eag2012.TOURS_SESSIONS)!=null){
									 listWeb = webMap.get(Eag2012.TOURS_SESSIONS);
								 }else{
									 listWeb = new ArrayList<String>();
								 }
								 listWeb.add(accessTable.getString(target2));
								 webMap.put(Eag2012.TOURS_SESSIONS,listWeb);
								 mapMapListWeb.put(Eag2012.TAB_ACCESS_AND_SERVICES,webMap);
								 if(listMapWebpageHrefList.size()>i){
									 listMapWebpageHrefList.set(i,mapMapListWeb);
								 }else{
									 listMapWebpageHrefList.add(mapMapListWeb);
								 }
								 eag2012.setWebpageHref(listMapWebpageHrefList);
							}
							if(accessTable.has(target3)){
								List<Map<String, Map<String, List<String>>>> listMapWebList = eag2012.getWebpageValue();
								if(listMapWebList==null){
									listMapWebList = new ArrayList<Map<String, Map<String, List<String>>>>();
								}
								Map<String, Map<String, List<String>>> mapMapListWeb = null;
								if(listMapWebList.size()>i && listMapWebList.get(i)!=null){
									mapMapListWeb = listMapWebList.get(i);
								}else{
									mapMapListWeb = new HashMap<String, Map<String, List<String>>>();
								}
								List<String> listWeb = null;
								Map<String,List<String>> webMap = null;
								if(mapMapListWeb.size()>0 && mapMapListWeb.get(Eag2012.TAB_ACCESS_AND_SERVICES)!=null){
									webMap = mapMapListWeb.get(Eag2012.TAB_ACCESS_AND_SERVICES);
								}else{
									webMap = new HashMap<String,List<String>>();
								}
								if(webMap.size()>0 && webMap.get(Eag2012.TOURS_SESSIONS)!=null){
									listWeb = webMap.get(Eag2012.TOURS_SESSIONS);
								}else{
									listWeb = new ArrayList<String>();
								}
								listWeb.add(accessTable.getString(target3));
								webMap.put(Eag2012.TOURS_SESSIONS,listWeb);
								mapMapListWeb.put(Eag2012.TAB_ACCESS_AND_SERVICES,webMap);
								if(listMapWebList.size()>i){
									listMapWebList.set(i,mapMapListWeb);
								}else{
									listMapWebList.add(mapMapListWeb);
								}
								eag2012.setWebpageValue(listMapWebList); 
							}
							if(accessTable.has(target4)){
								List<Map<String, Map<String, List<String>>>> descriptiveNotePValue = eag2012.getDescriptiveNotePLang();
								if(descriptiveNotePValue==null){
									descriptiveNotePValue = new ArrayList<Map<String, Map<String, List<String>>>>();
								}
								Map<String, Map<String, List<String>>> descriptiveNoteMapMapList = null;
								if(descriptiveNotePValue.size()>i && descriptiveNotePValue.get(i)!=null){
									descriptiveNoteMapMapList = descriptiveNotePValue.get(i);
								}else{
									descriptiveNoteMapMapList = new HashMap<String,Map<String,List<String>>>();
								}
								Map<String, List<String>> descriptiveNoteMapList = null;
								if(descriptiveNoteMapMapList.size()>0 && descriptiveNoteMapMapList.get(Eag2012.TAB_ACCESS_AND_SERVICES)!=null){
									descriptiveNoteMapList = descriptiveNoteMapMapList.get(Eag2012.TAB_ACCESS_AND_SERVICES);
								}else{
									descriptiveNoteMapList = new HashMap<String, List<String>>();
								}
								List<String> descriptiveNoteList = null;
								if(descriptiveNoteMapList.size()>0 && descriptiveNoteMapList.get(Eag2012.TOURS_SESSIONS)!=null){
									descriptiveNoteList = descriptiveNoteMapList.get(Eag2012.TOURS_SESSIONS);
								}else{
									descriptiveNoteList = new ArrayList<String>();
								}
								descriptiveNoteList.add(accessTable.getString(target4));
								descriptiveNoteMapList.put(Eag2012.TOURS_SESSIONS,descriptiveNoteList);
								descriptiveNoteMapMapList.put(Eag2012.TAB_ACCESS_AND_SERVICES,descriptiveNoteMapList);
								if(descriptiveNotePValue.size()>i){
									descriptiveNotePValue.set(i,descriptiveNoteMapMapList);
								}else{
									descriptiveNotePValue.add(descriptiveNoteMapMapList);
								}
								eag2012.setDescriptiveNotePLang(descriptiveNotePValue);
							}
						}while(accessTable.has(target1) && accessTable.has(target2) && accessTable.has(target3) && accessTable.has(target4));
						
						target1 = "textASReSeOtherServices";
						target2 = "textASReSeOSWebpage";
						target3 = "textASReSeWebpageOSLinkTitle";
						target4 = "selectASReSeOtherServicesSelectLanguage";
						targetNumber = 1;
						do{
							target1 = ((target1.indexOf("_")!=-1)?target1.substring(0,target1.indexOf("_")):target1)+"_"+targetNumber;
							target2 = ((target2.indexOf("_")!=-1)?target2.substring(0,target2.indexOf("_")):target2)+"_"+targetNumber;
							target3 = ((target3.indexOf("_")!=-1)?target3.substring(0,target3.indexOf("_")):target3)+"_"+targetNumber;
							target4 = ((target4.indexOf("_")!=-1)?target4.substring(0,target4.indexOf("_")):target4)+"_"+targetNumber;
							targetNumber++;
							if(accessTable.has(target1)){
								List<Map<String, Map<String, List<String>>>> descriptiveNotePValue = eag2012.getDescriptiveNotePValue();
								if(descriptiveNotePValue==null){
									descriptiveNotePValue = new ArrayList<Map<String, Map<String, List<String>>>>();
								}
								Map<String, Map<String, List<String>>> descriptiveNoteMapMapList = null;
								if(descriptiveNotePValue.size()>i && descriptiveNotePValue.get(i)!=null){
									descriptiveNoteMapMapList = descriptiveNotePValue.get(i);
								}else{
									descriptiveNoteMapMapList = new HashMap<String,Map<String,List<String>>>();
								}
								Map<String, List<String>> descriptiveNoteMapList = null;
								if(descriptiveNoteMapMapList.size()>0 && descriptiveNoteMapMapList.get(Eag2012.TAB_ACCESS_AND_SERVICES)!=null){
									descriptiveNoteMapList = descriptiveNoteMapMapList.get(Eag2012.TAB_ACCESS_AND_SERVICES);
								}else{
									descriptiveNoteMapList = new HashMap<String, List<String>>();
								}
								List<String> descriptiveNoteList = null;
								if(descriptiveNoteMapList.size()>0 && descriptiveNoteMapList.get(Eag2012.OTHER_SERVICES)!=null){
									descriptiveNoteList = descriptiveNoteMapList.get(Eag2012.OTHER_SERVICES);
								}else{
									descriptiveNoteList = new ArrayList<String>();
								}
								descriptiveNoteList.add(accessTable.getString(target1));
								descriptiveNoteMapList.put(Eag2012.OTHER_SERVICES,descriptiveNoteList);
								descriptiveNoteMapMapList.put(Eag2012.TAB_ACCESS_AND_SERVICES,descriptiveNoteMapList);
								if(descriptiveNotePValue.size()>i){
									descriptiveNotePValue.set(i,descriptiveNoteMapMapList);
								}else{
									descriptiveNotePValue.add(descriptiveNoteMapMapList);
								}
								eag2012.setDescriptiveNotePValue(descriptiveNotePValue);
							}
							if(accessTable.has(target2)){
								List<Map<String, Map<String, List<String>>>> listMapWebpageHrefList = eag2012.getWebpageHref();
								 if(listMapWebpageHrefList==null){
									 listMapWebpageHrefList = new ArrayList<Map<String, Map<String, List<String>>>>();
								 }
								 Map<String, Map<String, List<String>>> mapMapListWeb = null;
								 if(listMapWebpageHrefList.size()>i && listMapWebpageHrefList.get(i)!=null){
									 mapMapListWeb = listMapWebpageHrefList.get(i);
								 }else{
									 mapMapListWeb = new HashMap<String, Map<String, List<String>>>();
								 }
								 List<String> listWeb = null;
								 Map<String,List<String>> webMap = null;
								 if(mapMapListWeb.size()>0 && mapMapListWeb.get(Eag2012.TAB_ACCESS_AND_SERVICES)!=null){
									 webMap = mapMapListWeb.get(Eag2012.TAB_ACCESS_AND_SERVICES);
								 }else{
									 webMap = new HashMap<String,List<String>>();
								 }
								 if(webMap.size()>0 && webMap.get(Eag2012.OTHER_SERVICES)!=null){
									 listWeb = webMap.get(Eag2012.OTHER_SERVICES);
								 }else{
									 listWeb = new ArrayList<String>();
								 }
								 listWeb.add(accessTable.getString(target2));
								 webMap.put(Eag2012.OTHER_SERVICES,listWeb);
								 mapMapListWeb.put(Eag2012.TAB_ACCESS_AND_SERVICES,webMap);
								 if(listMapWebpageHrefList.size()>i){
									 listMapWebpageHrefList.set(i,mapMapListWeb);
								 }else{
									 listMapWebpageHrefList.add(mapMapListWeb);
								 }
								 eag2012.setWebpageHref(listMapWebpageHrefList);
							}
							if(accessTable.has(target3)){
								List<Map<String, Map<String, List<String>>>> listMapWebList = eag2012.getWebpageValue();
								if(listMapWebList==null){
									listMapWebList = new ArrayList<Map<String, Map<String, List<String>>>>();
								}
								Map<String, Map<String, List<String>>> mapMapListWeb = null;
								if(listMapWebList.size()>i && listMapWebList.get(i)!=null){
									mapMapListWeb = listMapWebList.get(i);
								}else{
									mapMapListWeb = new HashMap<String, Map<String, List<String>>>();
								}
								List<String> listWeb = null;
								Map<String,List<String>> webMap = null;
								if(mapMapListWeb.size()>0 && mapMapListWeb.get(Eag2012.TAB_ACCESS_AND_SERVICES)!=null){
									webMap = mapMapListWeb.get(Eag2012.TAB_ACCESS_AND_SERVICES);
								}else{
									webMap = new HashMap<String,List<String>>();
								}
								if(webMap.size()>0 && webMap.get(Eag2012.OTHER_SERVICES)!=null){
									listWeb = webMap.get(Eag2012.OTHER_SERVICES);
								}else{
									listWeb = new ArrayList<String>();
								}
								listWeb.add(accessTable.getString(target3));
								webMap.put(Eag2012.OTHER_SERVICES,listWeb);
								mapMapListWeb.put(Eag2012.TAB_ACCESS_AND_SERVICES,webMap);
								if(listMapWebList.size()>i){
									listMapWebList.set(i,mapMapListWeb);
								}else{
									listMapWebList.add(mapMapListWeb);
								}
								eag2012.setWebpageValue(listMapWebList); 
							}
							if(accessTable.has(target4)){
								List<Map<String, Map<String, List<String>>>> descriptiveNotePValue = eag2012.getDescriptiveNotePLang();
								if(descriptiveNotePValue==null){
									descriptiveNotePValue = new ArrayList<Map<String, Map<String, List<String>>>>();
								}
								Map<String, Map<String, List<String>>> descriptiveNoteMapMapList = null;
								if(descriptiveNotePValue.size()>i && descriptiveNotePValue.get(i)!=null){
									descriptiveNoteMapMapList = descriptiveNotePValue.get(i);
								}else{
									descriptiveNoteMapMapList = new HashMap<String,Map<String,List<String>>>();
								}
								Map<String, List<String>> descriptiveNoteMapList = null;
								if(descriptiveNoteMapMapList.size()>0 && descriptiveNoteMapMapList.get(Eag2012.TAB_ACCESS_AND_SERVICES)!=null){
									descriptiveNoteMapList = descriptiveNoteMapMapList.get(Eag2012.TAB_ACCESS_AND_SERVICES);
								}else{
									descriptiveNoteMapList = new HashMap<String, List<String>>();
								}
								List<String> descriptiveNoteList = null;
								if(descriptiveNoteMapList.size()>0 && descriptiveNoteMapList.get(Eag2012.OTHER_SERVICES)!=null){
									descriptiveNoteList = descriptiveNoteMapList.get(Eag2012.OTHER_SERVICES);
								}else{
									descriptiveNoteList = new ArrayList<String>();
								}
								descriptiveNoteList.add(accessTable.getString(target4));
								descriptiveNoteMapList.put(Eag2012.OTHER_SERVICES,descriptiveNoteList);
								descriptiveNoteMapMapList.put(Eag2012.TAB_ACCESS_AND_SERVICES,descriptiveNoteMapList);
								if(descriptiveNotePValue.size()>i){
									descriptiveNotePValue.set(i,descriptiveNoteMapMapList);
								}else{
									descriptiveNotePValue.add(descriptiveNoteMapMapList);
								}
								eag2012.setDescriptiveNotePLang(descriptiveNotePValue);
							}
						}while(accessTable.has(target1) && accessTable.has(target2) && accessTable.has(target3) && accessTable.has(target4));
						++i;
				}
			}
			return eag2012;	
		}
		
		private Eag2012 parseContactJsonObjToEag2012(Eag2012 eag2012,JSONObject jsonObj) throws JSONException {
			JSONObject contact = jsonObj.getJSONObject("contact");
			if(contact!=null){
				int x = 0;
				while(contact.has("contactTable_"+(x+1))){ //each child of contact is the container of visitor address and all attributes
					
					JSONObject contactTable = contact.getJSONObject("contactTable_"+(x+1));
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
				        
				        List<Map<String, List<String>>> tempListList = null;
				        Map<String, List<String>> tempListMap = null;
				        if(eag2012.getStreetValue()==null){
				        	tempListList = new ArrayList<Map<String, List<String>>>();
				        }else{
				        	tempListList = eag2012.getStreetValue();
				        }
				        if(tempListList.size()>x){
				        	tempListMap = tempListList.get(x);
				        }else{
				        	tempListMap = new HashMap<String, List<String>>();
				        }
				        tempListMap.put(Eag2012.TAB_CONTACT,listStreet);
				        if(tempListList.size()>x){
				        	tempListList.set(x,tempListMap);
				        }else{
				        	tempListList.add(tempListMap);
				        }
						eag2012.setStreetValue(tempListList);
						tempListList = null;
						if(eag2012.getStreetLang()==null){
							tempListList = new ArrayList<Map<String, List<String>>>();
						}else{
							tempListList = eag2012.getStreetLang();
						}
						if(tempListList.size()>x){
				        	tempListMap = tempListList.get(x);
				        }else{
				        	tempListMap = new HashMap<String, List<String>>();
				        }
				        tempListMap.put(Eag2012.TAB_CONTACT,listStreetLanguage);
				        if(tempListList.size()>x){
				        	tempListList.set(x,tempListMap);
				        }else{
				        	tempListList.add(tempListMap);
				        }
						eag2012.setStreetLang(tempListList);
						tempListList = null;
						if(eag2012.getCitiesValue()==null){
							tempListList = new ArrayList<Map<String, List<String>>>();
						}else{
							tempListList = eag2012.getCitiesValue();
						}
						if(tempListList.size()>x){
				        	tempListMap = tempListList.get(x);
				        }else{
				        	tempListMap = new HashMap<String, List<String>>();
				        }
				        tempListMap.put(Eag2012.TAB_CONTACT,listCities);
				        if(tempListList.size()>x){
				        	tempListList.set(x,tempListMap);
				        }else{
				        	tempListList.add(tempListMap);
				        }
						eag2012.setCitiesValue(tempListList);
						//begin listDistrict
						List<List<String>> localEntityValue = null;
						if(eag2012.getLocalentityValue()==null){
							localEntityValue = new ArrayList<List<String>>();
						}else{
							localEntityValue = eag2012.getLocalentityValue();
						}
						if(localEntityValue.size()>x){
							localEntityValue.set(x,listDistrict);
						}else{
							localEntityValue.add(listDistrict);
						}
						eag2012.setLocalentityValue(localEntityValue);
						//end listDistrict
						List<List<String>> tempListList2 = null;
						if(eag2012.getSecondemValue()==null){
							tempListList2 = new ArrayList<List<String>>();
						}else{
							tempListList2 = eag2012.getSecondemValue();
						}
						tempListList2.add(listLocalAuthority);
						eag2012.setSecondemValue(tempListList2);
						tempListList2 = null;
						if(eag2012.getFirstdemValue()==null){
							tempListList2 = new ArrayList<List<String>>();
						}else{
							tempListList2 = eag2012.getFirstdemValue();
						}
						tempListList2.add(listAutonomus);
						eag2012.setFirstdemValue(tempListList2);
						tempListList = null;
						if(eag2012.getCountryValue()==null){
							tempListList = new ArrayList<Map<String, List<String>>>();
						}else{
							tempListList = eag2012.getCountryValue();
						}
						if(tempListList.size()>x){
				        	tempListMap = tempListList.get(x);
				        }else{
				        	tempListMap = new HashMap<String, List<String>>();
				        }
				        tempListMap.put(Eag2012.TAB_CONTACT,listCountries);
				        if(tempListList.size()>x){
				        	tempListList.set(x,tempListMap);
				        }else{
				        	tempListList.add(tempListMap);
				        }
						eag2012.setCountryValue(tempListList);
						tempListList = null;
						if(eag2012.getLocationLatitude()==null){
							tempListList = new ArrayList<Map<String, List<String>>>();
						}else{
							tempListList = eag2012.getLocationLatitude();
						}
						if(tempListList.size()>x){
				        	tempListMap = tempListList.get(x);
				        }else{
				        	tempListMap = new HashMap<String, List<String>>();
				        }
				        tempListMap.put(Eag2012.TAB_CONTACT,listLatitudes);
				        if(tempListList.size()>x){
				        	tempListList.set(x,tempListMap);
				        }else{
				        	tempListList.add(tempListMap);
				        }
						eag2012.setLocationLatitude(tempListList);
						tempListList = null;
						if(eag2012.getLocationLongitude()==null){
							tempListList = new ArrayList<Map<String, List<String>>>();
						}else{
							tempListList = eag2012.getLocationLongitude();
						}
						if(tempListList.size()>x){
				        	tempListMap = tempListList.get(x);
				        }else{
				        	tempListMap = new HashMap<String, List<String>>();
				        }
				        tempListMap.put(Eag2012.TAB_CONTACT,listLongitudes);
				        if(tempListList.size()>x){
				        	tempListList.set(x,tempListMap);
				        }else{
				        	tempListList.add(tempListMap);
				        }
						eag2012.setLocationLongitude(tempListList);
				      
				      }
				}
				
				JSONObject postalAddress = contactTable.getJSONObject("postalAddress");
				if(postalAddress!=null){
					List<String> listStreet = new ArrayList<String>();
					List<String> listStreetLanguage = new ArrayList<String>();
					List<String> listCities = new ArrayList<String>();
					int i=1;
					if(postalAddress.has("contactTablePostalAddress_"+i)){
						
						do{
							JSONObject postalAddressTable = postalAddress.getJSONObject("contactTablePostalAddress_"+i);
							listStreet.add(postalAddressTable.getString("textContactPAStreet"));
							listStreetLanguage.add(postalAddressTable.getString("selectContactLanguagePostalAddress"));
							listCities.add(postalAddressTable.getString("textContactPACity"));
							
						}while(postalAddress.has("contactTablePostalAddress_"+(++i)));
						
						//begin listStreet
						List<Map<String, List<String>>> tempListList2 = null;
						if(eag2012.getPostalStreetValue()==null){
							tempListList2 = new ArrayList<Map<String, List<String>>>();
						}else{
							tempListList2 = eag2012.getPostalStreetValue();
						}
						Map<String, List<String>> tempListMap2 = null;
						if(tempListList2.size()>x){
				        	tempListMap2 = tempListList2.get(x);
				        }else{
				        	tempListMap2 = new HashMap<String, List<String>>();
				        }
				        tempListMap2.put(Eag2012.TAB_CONTACT,listStreet);
				        if(tempListList2.size()>x){
				        	tempListList2.set(x,tempListMap2);
				        }else{
				        	tempListList2.add(tempListMap2);
				        }
						eag2012.setPostalStreetValue(tempListList2);
						//end listStreet
						//begin listStreetLanguage
						tempListList2 = null;
						if(eag2012.getPostalStreetLang()==null){
							tempListList2 = new ArrayList<Map<String, List<String>>>();
						}else{
							tempListList2 = eag2012.getPostalStreetLang();
						}
						tempListMap2 = null;
						if(tempListList2.size()>x){
				        	tempListMap2 = tempListList2.get(x);
				        }else{
				        	tempListMap2 = new HashMap<String, List<String>>();
				        }
				        tempListMap2.put(Eag2012.TAB_CONTACT,listStreetLanguage);
				        if(tempListList2.size()>x){
				        	tempListList2.set(x,tempListMap2);
				        }else{
				        	tempListList2.add(tempListMap2);
				        }
						eag2012.setPostalStreetLang(tempListList2);
						//end listStreetLanguage
						//begin listCities
						tempListList2 = null;
						if(eag2012.getMunicipalityPostalcodeValue()==null){
							tempListList2 = new ArrayList<Map<String, List<String>>>();
						}else{
							tempListList2 = eag2012.getMunicipalityPostalcodeValue();
						}
						tempListMap2 = null;
						if(tempListList2.size()>x){
				        	tempListMap2 = tempListList2.get(x);
				        }else{
				        	tempListMap2 = new HashMap<String, List<String>>();
				        }
				        tempListMap2.put(Eag2012.TAB_CONTACT,listCities);
				        if(tempListList2.size()>x){
				        	tempListList2.set(x,tempListMap2);
				        }else{
				        	tempListList2.add(tempListMap2);
				        }
						eag2012.setMunicipalityPostalcodeValue(tempListList2);
						//end listCities
					}
					
				}
				int i=1;
				while(contactTable.has("textContactTelephoneOfTheInstitution_"+(++i))){
					List<Map<String, Map<String, List<String>>>> telephones = eag2012.getTelephoneValue();
					if(telephones==null){
						telephones = new ArrayList<Map<String, Map<String, List<String>>>>();
					}
					Map<String, Map<String, List<String>>> telephonesMap = null;
					if(telephones.size()>x && telephones.get(x)!=null){ //repo
						telephonesMap = telephones.get(x);
					}else{
						telephonesMap = new HashMap<String, Map<String, List<String>>>(); 
					}
					Map<String, List<String>> telephonesMapList = null;
					if(telephonesMap.size()>0 && telephonesMap.get(Eag2012.TAB_CONTACT)!=null){
						telephonesMapList = telephonesMap.get(Eag2012.TAB_CONTACT);
					}else{
						telephonesMapList = new HashMap<String, List<String>>();
					}
					List<String> telephonesList = null;
					if(telephonesMapList.size()>0 && telephonesMapList.get(Eag2012.ROOT)!=null){
						telephonesList = telephonesMapList.get(Eag2012.ROOT);
					}else{
						telephonesList = new ArrayList<String>();
					}
					telephonesList.add(contactTable.getString("textContactTelephoneOfTheInstitution_"+i));
					telephonesMapList.put(Eag2012.ROOT, telephonesList);
					telephonesMap.put(Eag2012.TAB_CONTACT,telephonesMapList);
					if(telephones.size()>x){
						telephones.set(x, telephonesMap);
					}else{
						telephones.add(telephonesMap);
					}
					eag2012.setTelephoneValue(telephones);
				}
				
			    i=0;
				while(contactTable.has("textContactFaxOfTheInstitution_"+(++i))){
					List<Map<String, List<String>>> listMapFaxList = null;
					if(eag2012.getFaxValue()!=null){
						listMapFaxList = eag2012.getFaxValue();
					}else{
						listMapFaxList = new ArrayList<Map<String,List<String>>>();
					}
					Map<String, List<String>> fax = null;
					if(listMapFaxList.size()>x){
						fax = listMapFaxList.get(x);
					}else{
						fax = new HashMap<String,List<String>>();
					}
					List<String> listFax = null;
					if(fax.size()>0 && fax.get(Eag2012.TAB_CONTACT)!=null){
						listFax = fax.get(Eag2012.TAB_CONTACT);
					}else{
						listFax = new ArrayList<String>();
					}
					listFax.add(contactTable.getString("textContactFaxOfTheInstitution_"+i));
					fax.put(Eag2012.TAB_CONTACT,listFax);
					if(listMapFaxList.size()>x){
						listMapFaxList.set(x,fax);
					}else{
						listMapFaxList.add(fax);
					}
					eag2012.setFaxValue(listMapFaxList); 	
				}
				
				 i=1;
				 while(contactTable.has("textContactEmailOfTheInstitution_"+(++i))){
					 List<Map<String, Map<String, List<String>>>> listMapEmailList = eag2012.getEmailHref();
					 if(listMapEmailList==null){
						 listMapEmailList = new ArrayList<Map<String, Map<String, List<String>>>>();
					 }
					 Map<String, Map<String, List<String>>> email = null;
					 if(listMapEmailList.size()>x && listMapEmailList.get(x)!=null){
						 email = listMapEmailList.get(x);
					 }else{
						 email = new HashMap<String, Map<String, List<String>>>();
					 }
					 List<String> listEmail = null;
					 Map<String,List<String>> emailMap = null;
					 if(email.size()>0 && email.get(Eag2012.TAB_CONTACT)!=null){
						 emailMap = email.get(Eag2012.TAB_CONTACT);
					 }else{
						 emailMap = new HashMap<String,List<String>>();
					 }
					 if(emailMap.size()>0 && emailMap.get(Eag2012.ROOT)!=null){
						 listEmail = emailMap.get(Eag2012.ROOT);
					 }else{
						 listEmail = new ArrayList<String>();
					 }
					 listEmail.add(contactTable.getString("textContactEmailOfTheInstitution_"+i));
					 emailMap.put(Eag2012.ROOT,listEmail); //root section, here there is only one mails list
					 email.put(Eag2012.TAB_CONTACT, emailMap);
					 if(listMapEmailList.size()>x){
						 listMapEmailList.set(x,email);
					 }else{
						 listMapEmailList.add(email);
					 }
					 eag2012.setEmailHref(listMapEmailList);
				 }
				 i=1;
				 while(contactTable.has("textContactLinkTitleForEmailOfTheInstitution_"+(++i))){
					 List<Map<String, Map<String, List<String>>>> listMapEmailValueList = eag2012.getEmailValue();
					 if(listMapEmailValueList==null){
						 listMapEmailValueList = new ArrayList<Map<String, Map<String, List<String>>>>();
					 }
					 Map<String, Map<String, List<String>>> email = null;
					 if(listMapEmailValueList.size()>x && listMapEmailValueList.get(x)!=null){
						 email = listMapEmailValueList.get(x);
					 }else{
						 email = new HashMap<String, Map<String, List<String>>>();
					 }
					 List<String> listEmail = null;
					 Map<String,List<String>> emailMap = null;
					 if(email.size()>0 && email.get(Eag2012.TAB_CONTACT)!=null){
						 emailMap = email.get(Eag2012.TAB_CONTACT);
					 }else{
						 emailMap = new HashMap<String,List<String>>();
					 }
					 if(emailMap.size()>0 && emailMap.get(Eag2012.ROOT)!=null){
						 listEmail = emailMap.get(Eag2012.ROOT);
					 }else{
						 listEmail = new ArrayList<String>();
					 }
					 listEmail.add(contactTable.getString("textContactLinkTitleForEmailOfTheInstitution_"+i));
					  
					 emailMap.put(Eag2012.ROOT,listEmail); //root section, here there is only one mails list
					 email.put(Eag2012.TAB_CONTACT, emailMap);
					 if(listMapEmailValueList.size()>x){
						 listMapEmailValueList.set(x,email);
					 }else{
						 listMapEmailValueList.add(email);
					 }
					 eag2012.setEmailValue(listMapEmailValueList); 	
				 }
				 i=1;
				 while(contactTable.has("textContactWebOfTheInstitution_"+(++i))){
					 List<Map<String, Map<String, List<String>>>> listMapWebpageHrefList = eag2012.getWebpageHref();
					 if(listMapWebpageHrefList==null){
						 listMapWebpageHrefList = new ArrayList<Map<String, Map<String, List<String>>>>();
					 }
					 Map<String, Map<String, List<String>>> mapMapListWeb = null;
					 if(listMapWebpageHrefList.size()>x && listMapWebpageHrefList.get(x)!=null){
						 mapMapListWeb = listMapWebpageHrefList.get(x);
					 }else{
						 mapMapListWeb = new HashMap<String, Map<String, List<String>>>();
					 }
					 List<String> listWeb = null;
					 Map<String,List<String>> webMap = null;
					 if(mapMapListWeb.size()>0 && mapMapListWeb.get(Eag2012.TAB_CONTACT)!=null){
						 webMap = mapMapListWeb.get(Eag2012.TAB_CONTACT);
					 }else{
						 webMap = new HashMap<String,List<String>>();
					 }
					 if(webMap.size()>0 && webMap.get(Eag2012.ROOT)!=null){
						 listWeb = webMap.get(Eag2012.ROOT);
					 }else{
						 listWeb = new ArrayList<String>();
					 }
					 listWeb.add(contactTable.getString("textContactWebOfTheInstitution_"+i));
					 webMap.put(Eag2012.ROOT,listWeb);
					 mapMapListWeb.put(Eag2012.TAB_CONTACT,webMap);
					 if(listMapWebpageHrefList.size()>x){
						 listMapWebpageHrefList.set(x,mapMapListWeb);
					 }else{
						 listMapWebpageHrefList.add(mapMapListWeb);
					 }
					 eag2012.setWebpageHref(listMapWebpageHrefList); 	
				 }
			
				 i=1;
				 while(contactTable.has("textContactLinkTitleForWebOfTheInstitution_"+(++i))){
					 List<Map<String, Map<String, List<String>>>> listMapWebList = eag2012.getWebpageValue();
					 if(listMapWebList==null){
						 listMapWebList = new ArrayList<Map<String, Map<String, List<String>>>>();
					 }
					 Map<String, Map<String, List<String>>> mapMapListWeb = null;
					 if(listMapWebList.size()>x && listMapWebList.get(x)!=null){
						 mapMapListWeb = listMapWebList.get(x);
					 }else{
						 mapMapListWeb = new HashMap<String, Map<String, List<String>>>();
					 }
					 List<String> listWeb = null;
					 Map<String,List<String>> webMap = null;
					 if(mapMapListWeb.size()>0 && mapMapListWeb.get(Eag2012.TAB_CONTACT)!=null){
						 webMap = mapMapListWeb.get(Eag2012.TAB_CONTACT);
					 }else{
						 webMap = new HashMap<String,List<String>>();
					 }
					 if(webMap.size()>0 && webMap.get(Eag2012.ROOT)!=null){
						 listWeb = webMap.get(Eag2012.ROOT);
					 }else{
						 listWeb = new ArrayList<String>();
					 }
					 listWeb.add(contactTable.getString("textContactLinkTitleForWebOfTheInstitution_"+i));
					 webMap.put(Eag2012.ROOT,listWeb);
					 mapMapListWeb.put(Eag2012.TAB_CONTACT,webMap);
					 if(listMapWebList.size()>x){
						 listMapWebList.set(x,mapMapListWeb);
					 }else{
						 listMapWebList.add(mapMapListWeb);
					 }
					 eag2012.setWebpageValue(listMapWebList); 	
				}
				x++;
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
						List<Map<String, Map<String, Map<String, List<List<String>>>>>> yearValue = eag2012.getDateStandardDate();
						if(yearValue==null){
							yearValue = new ArrayList<Map<String, Map<String, Map<String, List<List<String>>>>>>();
						}
						Map<String, Map<String, Map<String, List<List<String>>>>> yearMapMapMap = null;
						if(yearValue.size()>0 && yearValue.get(0)!=null){
							yearMapMapMap = yearValue.get(0);
						}else{
							yearMapMapMap = new HashMap<String, Map<String, Map<String, List<List<String>>>>>();
						}
						Map<String, Map<String, List<List<String>>>> yearMapMap = null;
						if(yearMapMapMap.size()>0 && yearMapMapMap.get(Eag2012.TAB_IDENTITY)!=null){
							 yearMapMap = yearMapMapMap.get(Eag2012.TAB_IDENTITY);
						}else{
							 yearMapMap = new HashMap<String, Map<String, List<List<String>>>>();
						}
						Map<String, List<List<String>>> yearMap = null;
						if(yearMapMap.size()>0 &&  yearMapMap.get(Eag2012.ROOT)!=null){
							yearMap =  yearMapMap.get(Eag2012.ROOT);
						}else{
							yearMap = new HashMap<String, List<List<String>>>();
						}
						List<List<String>> yearList = null;
						if(yearMap.size()>0 && yearMap.get(Eag2012.ROOT_SUBSECTION)!=null){
							yearList = yearMap.get(Eag2012.ROOT_SUBSECTION);
						}else{
							yearList = new ArrayList<List<String>>();
						}
						List<String> year = null;
						if(yearList.size()>0 && yearList.size()>(i-1)){
							year = yearList.get(i-1);
						}else{
							 year = new ArrayList<String>();
						}
						 year.add(previousNameOfTheArchive.getString("textYearWhenThisNameWasUsed_"+j));
						if(yearList.size()>0 && yearList.size()>(i-1)){
							yearList.set((i-1),year);
						}else{
							yearList.add(year);
						}
						yearMap.put(Eag2012.ROOT_SUBSECTION,yearList);
						yearMapMap.put(Eag2012.ROOT, yearMap);
						yearMapMapMap.put(Eag2012.TAB_IDENTITY,yearMapMap);
						if(yearValue.size()>0){
							yearValue.set(0,yearMapMapMap);
						}else{
							yearValue.add(yearMapMapMap);
						}
						eag2012.setDateStandardDate(yearValue);
					}
					//Identity Range Year From
					j=0;
					while(previousNameOfTheArchive.has("textYearWhenThisNameWasUsedFrom_"+(++j)) && previousNameOfTheArchive.has("textYearWhenThisNameWasUsedTo_"+j)){
					 if(previousNameOfTheArchive.has("textYearWhenThisNameWasUsedFrom_"+j)){
						List<Map<String, Map<String, Map<String, List<List<String>>>>>> yearValue = eag2012.getFromDateStandardDate();
						if(yearValue==null){
							yearValue = new ArrayList<Map<String, Map<String, Map<String, List<List<String>>>>>>();
						}
						Map<String, Map<String, Map<String, List<List<String>>>>> yearMapMapMap = null;
						if(yearValue.size()>0 && yearValue.get(0)!=null){
							yearMapMapMap = yearValue.get(0);
						}else{
							yearMapMapMap = new HashMap<String, Map<String, Map<String, List<List<String>>>>>();
						}
						Map<String, Map<String, List<List<String>>>> yearMapMap = null;
						if(yearMapMapMap.size()>0 && yearMapMapMap.get(Eag2012.TAB_IDENTITY)!=null){
							 yearMapMap = yearMapMapMap.get(Eag2012.TAB_IDENTITY);
						}else{
							 yearMapMap = new HashMap<String, Map<String, List<List<String>>>>();
						}
						Map<String, List<List<String>>> yearMap = null;
						if(yearMapMap.size()>0 &&  yearMapMap.get(Eag2012.ROOT)!=null){
							yearMap =  yearMapMap.get(Eag2012.ROOT);
						}else{
							yearMap = new HashMap<String, List<List<String>>>();
						}
						List<List<String>> yearList = null;
						if(yearMap.size()>0 && yearMap.get(Eag2012.ROOT_SUBSECTION)!=null){
							yearList = yearMap.get(Eag2012.ROOT_SUBSECTION);
						}else{
							yearList = new ArrayList<List<String>>();
						}
						List<String> year = null;
						if(yearList.size()>0 && yearList.size()>(i-1)){
							year = yearList.get(i-1);
						}else{
							 year = new ArrayList<String>();
						}
						 year.add(previousNameOfTheArchive.getString("textYearWhenThisNameWasUsedFrom_"+j));
						if(yearList.size()>0 && yearList.size()>(i-1)){
							yearList.set((i-1),year);
						}else{
							yearList.add(year);
						}
						yearMap.put(Eag2012.ROOT_SUBSECTION,yearList);
						yearMapMap.put(Eag2012.ROOT, yearMap);
						yearMapMapMap.put(Eag2012.TAB_IDENTITY,yearMapMap);
						if(yearValue.size()>0){
							yearValue.set(0,yearMapMapMap);
						}else{
							yearValue.add(yearMapMapMap);
						}
						eag2012.setFromDateStandardDate(yearValue);
					
					}
					if(previousNameOfTheArchive.has("textYearWhenThisNameWasUsedTo_"+j)){ 
					//Identity Range Year To
						List<Map<String, Map<String, Map<String, List<List<String>>>>>> yearValue = eag2012.getToDateStandardDate();
						if(yearValue==null){
							yearValue = new ArrayList<Map<String, Map<String, Map<String, List<List<String>>>>>>();
						}
						Map<String, Map<String, Map<String, List<List<String>>>>> yearMapMapMap = null;
						if(yearValue.size()>0 && yearValue.get(0)!=null){
							yearMapMapMap = yearValue.get(0);
						}else{
							yearMapMapMap = new HashMap<String, Map<String, Map<String, List<List<String>>>>>();
						}
						Map<String, Map<String, List<List<String>>>> yearMapMap = null;
						if(yearMapMapMap.size()>0 && yearMapMapMap.get(Eag2012.TAB_IDENTITY)!=null){
							 yearMapMap = yearMapMapMap.get(Eag2012.TAB_IDENTITY);
						}else{
							 yearMapMap = new HashMap<String, Map<String, List<List<String>>>>();
						}
						Map<String, List<List<String>>> yearMap = null;
						if(yearMapMap.size()>0 &&  yearMapMap.get(Eag2012.ROOT)!=null){
							yearMap =  yearMapMap.get(Eag2012.ROOT);
						}else{
							yearMap = new HashMap<String, List<List<String>>>();
						}
						List<List<String>> yearList = null;
						if(yearMap.size()>0 && yearMap.get(Eag2012.ROOT_SUBSECTION)!=null){
							yearList = yearMap.get(Eag2012.ROOT_SUBSECTION);
						}else{
							yearList = new ArrayList<List<String>>();
						}
						List<String> year = null;
						if(yearList.size()>0 && yearList.size()>(i-1)){
							year = yearList.get(i-1);
						}else{
							 year = new ArrayList<String>();
						}
						 year.add(previousNameOfTheArchive.getString("textYearWhenThisNameWasUsedTo_"+j));
						if(yearList.size()>0 && yearList.size()>(i-1)){
							yearList.set((i-1),year);
						}else{
							yearList.add(year);
						}
						yearMap.put(Eag2012.ROOT_SUBSECTION,yearList);
						yearMapMap.put(Eag2012.ROOT, yearMap);
						yearMapMapMap.put(Eag2012.TAB_IDENTITY,yearMapMap);
						if(yearValue.size()>0){
							yearValue.set(0,yearMapMapMap);
						}else{
							yearValue.add(yearMapMapMap);
						}
						eag2012.setToDateStandardDate(yearValue);
					}	//end if toDate			
				}//end While rangeDates
			  }//end while 
			}//end if formerly name
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
			
			
		//	  eag2012.setOtherRepositorId(yourInstitution.getString("textYIIdentifierOfTheInstitution"));
			
		//	eag2012.setRepositoridRepositorycode(eag2012.getOtherRepositorId());
			eag2012.setRecordIdValue(yourInstitution.getString("textYIIdUsedInAPE"));
			//looper
			List<String> otherRecordIds = new ArrayList<String>();
			List<String> localtypeOtherRecordIds =new ArrayList<String>();
			String localTypeNo = yourInstitution.getString("selectYICodeISIL");
			otherRecordIds.add(yourInstitution.getString("textYIIdentifierOfTheInstitution"));
			localtypeOtherRecordIds.add(yourInstitution.getString("selectYICodeISIL"));
	
			if(Eag2012.OPTION_NO.equalsIgnoreCase(localTypeNo)) {
				eag2012.setOtherRepositorId(yourInstitution.getString("textYIIdentifierOfTheInstitution"));
			 
			}
			eag2012.setRepositoridRepositorycode(yourInstitution.getString("textYIIdUsedInAPE"));
			
			for(int i=0;yourInstitution.has("otherRepositorId_"+(i)) && yourInstitution.has("selectOtherRepositorIdCodeISIL_"+i);i++){
				localTypeNo = yourInstitution.getString("selectOtherRepositorIdCodeISIL_"+(i));
				if((Eag2012.OPTION_NO.equalsIgnoreCase(localTypeNo)) && eag2012.getOtherRepositorId()==null){
				  eag2012.setOtherRepositorId(yourInstitution.getString("otherRepositorId_"+(i)));	
				}
				otherRecordIds.add(yourInstitution.getString("otherRepositorId_"+(i)));
				localtypeOtherRecordIds.add(yourInstitution.getString("selectOtherRepositorIdCodeISIL_"+i));
			}
			if(otherRecordIds.size()>0){
				eag2012.setOtherRecordIdValue(otherRecordIds);
			}
			if(localtypeOtherRecordIds.size()>0){
				eag2012.setOtherRecordIdLocalType(localtypeOtherRecordIds);
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
					
					List<Map<String, List<String>>> tempListList = new ArrayList<Map<String, List<String>>>();//at first time list must be in 0 position for first <location> tag into <repository> and
					HashMap<String, List<String>> listTempMap = new HashMap<String, List<String>>();
					listTempMap.put(Eag2012.TAB_YOUR_INSTITUTION,listStreets);
					tempListList.add(listTempMap);
					eag2012.setStreetValue(tempListList);
					tempListList = new ArrayList<Map<String, List<String>>>();
					listTempMap = new HashMap<String, List<String>>();
					listTempMap.put(Eag2012.TAB_YOUR_INSTITUTION,listLangStreets);
					tempListList.add(listTempMap);
					eag2012.setStreetLang(tempListList);
					tempListList = new ArrayList<Map<String, List<String>>>();
					listTempMap = new HashMap<String, List<String>>();
					listTempMap.put(Eag2012.TAB_YOUR_INSTITUTION,listCities);
					tempListList.add(listTempMap);
					eag2012.setCitiesValue(tempListList);
					tempListList = new ArrayList<Map<String, List<String>>>();
					listTempMap = new HashMap<String, List<String>>();
					listTempMap.put(Eag2012.TAB_YOUR_INSTITUTION,listCountries);
					tempListList.add(listTempMap);
					eag2012.setCountryValue(tempListList);
					tempListList = new ArrayList<Map<String, List<String>>>();
					listTempMap = new HashMap<String, List<String>>();
					listTempMap.put(Eag2012.TAB_YOUR_INSTITUTION,listStreetLanguage);
					tempListList.add(listTempMap);
					List<List<String>> tempList2 = new ArrayList<List<String>>();
					List<String> tempList2List = new ArrayList<String>();
					tempList2List.addAll(listLangStreets);
					tempList2.add(tempList2List);
					eag2012.setCountryLang(tempList2);
					tempListList = new ArrayList<Map<String, List<String>>>();
					listTempMap = new HashMap<String, List<String>>();
					listTempMap.put(Eag2012.TAB_YOUR_INSTITUTION,listLatitudes);
					tempListList.add(listTempMap);
					eag2012.setLocationLatitude(tempListList);
					tempListList = new ArrayList<Map<String, List<String>>>();
					listTempMap = new HashMap<String, List<String>>();
					listTempMap.put(Eag2012.TAB_YOUR_INSTITUTION,listLongitudes);
					tempListList.add(listTempMap);
					eag2012.setLocationLongitude(tempListList);
				}
			}
			JSONObject postalAddress = yourInstitution.getJSONObject("postalAddress");
			if(postalAddress!=null){
				List<String> listStreets = new ArrayList<String>();
				List<String> listLangStreets = new ArrayList<String>();
				List<String> listMunicipalities = new ArrayList<String>();
				for(int i=0;i<postalAddress.length();i++){
					JSONObject postalAddressTable = postalAddress.getJSONObject("yiTablePostalAddress_"+(i+1));
					listStreets.add(postalAddressTable.getString("textYIPAStreet"));
					listLangStreets.add(postalAddressTable.getString("selectYIPASelectLanguage"));
					listMunicipalities.add(postalAddressTable.getString("textYIPACity"));
				}
				//begin listStreets
				List<Map<String, List<String>>> tempListList2 = null;
				if(eag2012.getPostalStreetValue()==null){
					tempListList2 = new ArrayList<Map<String, List<String>>>();
				}else{
					tempListList2 = eag2012.getPostalStreetValue();
				}
				Map<String, List<String>> tempListMap2 = null;
				if(tempListList2.size()>0){
		        	tempListMap2 = tempListList2.get(0);
		        }else{
		        	tempListMap2 = new HashMap<String, List<String>>();
		        }
		        tempListMap2.put(Eag2012.TAB_YOUR_INSTITUTION,listStreets);
		        if(tempListList2.size()>0){
		        	tempListList2.set(0,tempListMap2);
		        }else{
		        	tempListList2.add(tempListMap2);
		        }
				eag2012.setPostalStreetValue(tempListList2);
				//end listStreets
				//begin listLangStreets
				tempListList2 = null;
				if(eag2012.getPostalStreetLang()==null){
					tempListList2 = new ArrayList<Map<String, List<String>>>();
				}else{
					tempListList2 = eag2012.getPostalStreetLang();
				}
				tempListMap2 = null;
				if(tempListList2.size()>0){
		        	tempListMap2 = tempListList2.get(0);
		        }else{
		        	tempListMap2 = new HashMap<String, List<String>>();
		        }
		        tempListMap2.put(Eag2012.TAB_YOUR_INSTITUTION,listLangStreets);
		        if(tempListList2.size()>0){
		        	tempListList2.set(0,tempListMap2);
		        }else{
		        	tempListList2.add(tempListMap2);
		        }
				eag2012.setPostalStreetLang(tempListList2);
				//end listLangStreets
				//begin listMunicipalities
				tempListList2 = null;
				if(eag2012.getMunicipalityPostalcodeValue()==null){
					tempListList2 = new ArrayList<Map<String, List<String>>>();
				}else{
					tempListList2 = eag2012.getMunicipalityPostalcodeValue();
				}
				tempListMap2 = null;
				if(tempListList2.size()>0){
		        	tempListMap2 = tempListList2.get(0);
		        }else{
		        	tempListMap2 = new HashMap<String, List<String>>();
		        }
		        tempListMap2.put(Eag2012.TAB_YOUR_INSTITUTION,listMunicipalities);
		        if(tempListList2.size()>0){
		        	tempListList2.set(0,tempListMap2);
		        }else{
		        	tempListList2.add(tempListMap2);
		        }
				eag2012.setMunicipalityPostalcodeValue(tempListList2);
				//end listMunicipalities
			}
			//your institution - last part
			
			if(yourInstitution.has("textYITelephone")){
				List<Map<String, Map<String, List<String>>>> telephones = eag2012.getTelephoneValue();
				if(telephones==null){
					telephones = new ArrayList<Map<String, Map<String, List<String>>>>();
				}
				Map<String, Map<String, List<String>>> telephonesMap = null;
				if(telephones.size()>0 && telephones.get(0)!=null){ //repo
					telephonesMap = telephones.get(0);
				}else{
					telephonesMap = new HashMap<String, Map<String, List<String>>>(); 
				}
				Map<String, List<String>> telephonesMapList = null;
				if(telephonesMap.size()>0 && telephonesMap.get(Eag2012.TAB_YOUR_INSTITUTION)!=null){
					telephonesMapList = telephonesMap.get(Eag2012.TAB_YOUR_INSTITUTION);
				}else{
					telephonesMapList = new HashMap<String, List<String>>();
				}
				List<String> telephonesList = null;
				if(telephonesMapList.size()>0 && telephonesMapList.get(Eag2012.ROOT)!=null){
					telephonesList = telephonesMapList.get(Eag2012.ROOT);
				}else{
					telephonesList = new ArrayList<String>();
				}
				telephonesList.add(yourInstitution.getString("textYITelephone"));
				telephonesMapList.put(Eag2012.ROOT, telephonesList);
				telephonesMap.put(Eag2012.TAB_YOUR_INSTITUTION,telephonesMapList);
				telephones.add(telephonesMap);
				eag2012.setTelephoneValue(telephones);
			}
			if(yourInstitution.has("textYIEmailAddress")){
				List<Map<String, Map<String, List<String>>>> listMapEmailList = eag2012.getEmailHref();
				if(listMapEmailList==null){
					listMapEmailList = new ArrayList<Map<String, Map<String, List<String>>>>();
				}
				Map<String, Map<String, List<String>>> email = null;
				if(listMapEmailList.size()>0 && listMapEmailList.get(0)!=null){
					email = listMapEmailList.get(0);
				}else{
					email = new HashMap<String, Map<String, List<String>>>();
				}
				List<String> listEmail = null;
				Map<String,List<String>> emailMap = null;
				if(email.size()>0 && email.get(Eag2012.TAB_YOUR_INSTITUTION)!=null){
					emailMap = email.get(Eag2012.TAB_YOUR_INSTITUTION);
				}else{
					emailMap = new HashMap<String,List<String>>();
				}
				if(emailMap.size()>0 && emailMap.get(Eag2012.ROOT)!=null){
					listEmail = emailMap.get(Eag2012.ROOT);
				}else{
					listEmail = new ArrayList<String>();
				}
				listEmail.add(yourInstitution.getString("textYIEmailAddress"));
				emailMap.put(Eag2012.ROOT,listEmail); //root section, here there is only one mails list
				email.put(Eag2012.TAB_YOUR_INSTITUTION, emailMap);
				if(listMapEmailList.size()>0){
					listMapEmailList.set(0,email);
				}else{
					listMapEmailList.add(email);
				}
				eag2012.setEmailHref(listMapEmailList); //first repo (index_0) (your_institution), first tab (index_TAB_YOUR_INSTITUTION), unique element {list.set(mailsHref) }
			}
			if(yourInstitution.has("textYIEmailLinkTitle")){
				List<Map<String, Map<String, List<String>>>> listMapEmailList = eag2012.getEmailValue();
				if(listMapEmailList==null){
					listMapEmailList = new ArrayList<Map<String, Map<String, List<String>>>>();
				}
				Map<String, Map<String, List<String>>> email = null;
				if(listMapEmailList.size()>0 && listMapEmailList.get(0)!=null){
					email = listMapEmailList.get(0);
				}else{
					email = new HashMap<String, Map<String, List<String>>>();
				}
				List<String> listEmail = null;
				Map<String,List<String>> emailMap = null;
				if(email.size()>0 && email.get(Eag2012.TAB_YOUR_INSTITUTION)!=null){
					emailMap = email.get(Eag2012.TAB_YOUR_INSTITUTION);
				}else{
					emailMap = new HashMap<String,List<String>>();
				}
				if(emailMap.size()>0 && emailMap.get(Eag2012.ROOT)!=null){
					listEmail = emailMap.get(Eag2012.ROOT);
				}else{
					listEmail = new ArrayList<String>();
				}
				listEmail.add(yourInstitution.getString("textYIEmailLinkTitle"));
				emailMap.put(Eag2012.ROOT,listEmail); //root section, here there is only one mails list
				email.put(Eag2012.TAB_YOUR_INSTITUTION, emailMap);
				if(listMapEmailList.size()>0){
					listMapEmailList.set(0,email);
				}else{
					listMapEmailList.add(email);
				}
				eag2012.setEmailValue(listMapEmailList); //first repo (index_0) (your_institution), first tab (index_TAB_YOUR_INSTITUTION), unique element {list.set(mailsTitle) }
			}
			if(yourInstitution.has("textYIWebpage")){
				List<Map<String, Map<String, List<String>>>> listMapEmailList = eag2012.getWebpageHref();
				if(listMapEmailList==null){
					listMapEmailList = new ArrayList<Map<String, Map<String, List<String>>>>();
				}
				Map<String, Map<String, List<String>>> email = null;
				if(listMapEmailList.size()>0 && listMapEmailList.get(0)!=null){
					email = listMapEmailList.get(0);
				}else{
					email = new HashMap<String, Map<String, List<String>>>();
				}
				List<String> listEmail = null;
				Map<String,List<String>> emailMap = null;
				if(email.size()>0 && email.get(Eag2012.TAB_YOUR_INSTITUTION)!=null){
					emailMap = email.get(Eag2012.TAB_YOUR_INSTITUTION);
				}else{
					emailMap = new HashMap<String,List<String>>();
				}
				if(emailMap.size()>0 && emailMap.get(Eag2012.ROOT)!=null){
					listEmail = emailMap.get(Eag2012.ROOT);
				}else{
					listEmail = new ArrayList<String>();
				}
				listEmail.add(yourInstitution.getString("textYIWebpage"));
				emailMap.put(Eag2012.ROOT,listEmail); //root section, here there is only one mails list
				email.put(Eag2012.TAB_YOUR_INSTITUTION, emailMap);
				if(listMapEmailList.size()>0){
					listMapEmailList.set(0,email);
				}else{
					listMapEmailList.add(email);
				}
				eag2012.setWebpageHref(listMapEmailList); //first repo (index_0) (your_institution), first tab (index_TAB_YOUR_INSTITUTION), unique element {list.set(webpageHref) }
			}
			if(yourInstitution.has("textYIWebpageLinkTitle")){
				List<Map<String, Map<String, List<String>>>> listMapWebpagelList = eag2012.getWebpageValue();
				if(listMapWebpagelList==null){
					listMapWebpagelList = new ArrayList<Map<String, Map<String, List<String>>>>();
				}
				Map<String, Map<String, List<String>>> email = null;
				if(listMapWebpagelList.size()>0 && listMapWebpagelList.get(0)!=null){
					email = listMapWebpagelList.get(0);
				}else{
					email = new HashMap<String, Map<String, List<String>>>();
				}
				List<String> listEmail = null;
				Map<String,List<String>> emailMap = null;
				if(email.size()>0 && email.get(Eag2012.TAB_YOUR_INSTITUTION)!=null){
					emailMap = email.get(Eag2012.TAB_YOUR_INSTITUTION);
				}else{
					emailMap = new HashMap<String,List<String>>();
				}
				if(emailMap.size()>0 && emailMap.get(Eag2012.ROOT)!=null){
					listEmail = emailMap.get(Eag2012.ROOT);
				}else{
					listEmail = new ArrayList<String>();
				}
				listEmail.add(yourInstitution.getString("textYIWebpageLinkTitle"));
				emailMap.put(Eag2012.ROOT,listEmail); //root section, here there is only one mails list
				email.put(Eag2012.TAB_YOUR_INSTITUTION, emailMap);
				if(listMapWebpagelList.size()>0){
					listMapWebpagelList.set(0,email);
				}else{
					listMapWebpagelList.add(email);
				}
				eag2012.setWebpageValue(listMapWebpagelList); //first repo (index_0) (your_institution), first tab (index_TAB_YOUR_INSTITUTION), unique element {list.set(webpageTitle) }
			}
			if(yourInstitution.has("textYIOpeningTimes")){
				List<Map<String,List<String>>> openingValues = eag2012.getOpeningValue();
				 if(openingValues == null){
				   openingValues = new ArrayList<Map<String,List<String>>>();  
				 }
				 Map<String, List<String>> openingMap = null;
				 if((openingValues.size() > 0) && (openingValues.get(0)!=null)){
				   openingMap = openingValues.get(0);	  
				 }else{
					 openingMap = new HashMap<String,List<String>>();
				 }
				List<String> openingList = null;
				if((openingMap.size()>0) && (openingMap.get(Eag2012.TAB_YOUR_INSTITUTION)!=null)){
					openingList = openingMap.get(Eag2012.TAB_YOUR_INSTITUTION);
				}else{
					openingList = new ArrayList<String>();
				}
				openingList.add(yourInstitution.getString("textYIOpeningTimes"));
				openingMap.put(Eag2012.TAB_YOUR_INSTITUTION, openingList);
			    if(openingValues.size() > 0){
				   openingValues.set(0,openingMap);
			    }else{
			    	openingValues.add(openingMap);
			    }
			    eag2012.setOpeningValue(openingValues);
			}
			if(yourInstitution.has("yourInstitutionClosingDates")){
				List<Map<String,List<String>>> closingValues = eag2012.getClosingStandardDate();
				 if(closingValues == null){
					 closingValues = new ArrayList<Map<String,List<String>>>();  
				 }
				 Map<String, List<String>> closingMap = null;
				 if((closingValues.size() > 0) && (closingValues.get(0)!=null)){
					 closingMap = closingValues.get(0);	  
				 }else{
					 closingMap = new HashMap<String,List<String>>();
				 }
				List<String> closingList = null;
				if((closingMap.size()>0) && (closingMap.get(Eag2012.TAB_YOUR_INSTITUTION)!=null)){
					closingList = closingMap.get(Eag2012.TAB_YOUR_INSTITUTION);
				}else{
					closingList = new ArrayList<String>();
				}
				closingList.add(yourInstitution.getString("yourInstitutionClosingDates"));
				closingMap.put(Eag2012.TAB_YOUR_INSTITUTION, closingList);
			    if(closingValues.size() > 0){
			    	closingValues.set(0,closingMap);
			    }else{
			    	closingValues.add(closingMap);
			    }
				eag2012.setClosingValue(closingValues);
			}
			
			if(yourInstitution.has("selectAccessibleToThePublic")){
				List<Map<String, String>> accessQuestions = eag2012.getAccessQuestion();
				if(accessQuestions == null){
					accessQuestions = new ArrayList<Map<String, String>>(); 
				}
				Map<String, String> accessQuestionRepo = null;
				if(accessQuestions.size()>0 && accessQuestions.get(0)!=null){
					accessQuestionRepo = accessQuestions.get(0);
				}else{
					accessQuestionRepo = new HashMap<String, String>();
				}
				accessQuestionRepo.put(Eag2012.TAB_YOUR_INSTITUTION,yourInstitution.getString("selectAccessibleToThePublic"));
				if(accessQuestions.size()>0){
					accessQuestions.set(0,accessQuestionRepo);
				}else{
					accessQuestions.add(accessQuestionRepo);
				}
				eag2012.setAccessQuestion(accessQuestions);
			}
			if(yourInstitution.has("futherAccessInformation")){
				List<Map<String, List<String>>> restAccessValue = eag2012.getRestaccessValue();
				if(restAccessValue == null){
					restAccessValue = new ArrayList<Map<String, List<String>>>(); 
				}
				Map<String, List<String>> restAccessMap = null;
				if(restAccessValue.size()>0 && restAccessValue.get(0)!=null){
					restAccessMap = restAccessValue.get(0);
				}else{
					restAccessMap = new HashMap<String, List<String>>(); 
				}
				List<String> restAccessList = null;
				if(restAccessMap.size()>0 && restAccessMap.get(Eag2012.TAB_YOUR_INSTITUTION)!=null){
					restAccessList = restAccessMap.get(Eag2012.TAB_YOUR_INSTITUTION);
				}else{
					restAccessList = new ArrayList<String>();
				}
				restAccessList.add(yourInstitution.getString("futherAccessInformation"));
				restAccessMap.put(Eag2012.TAB_YOUR_INSTITUTION, restAccessList);
				if(restAccessValue.size()>0){
					restAccessValue.set(0,restAccessMap);
				}else{
					restAccessValue.add(restAccessMap);
				}
				eag2012.setRestaccessValue(restAccessValue); 
			}
			if(yourInstitution.has("futherInformationOnExistingFacilities")){
				List<Map<String, List<String>>> accessibilityValue =eag2012.getAccessibilityValue();
				if(accessibilityValue == null){
					accessibilityValue = new ArrayList<Map<String, List<String>>>();	
				}
				Map<String, List<String>> accessibilityMap = null;
				if(accessibilityValue.size()>0 && accessibilityValue.get(0)!= null){
				  accessibilityMap = accessibilityValue.get(0);	
				}else{
					accessibilityMap = new HashMap<String, List<String>>();
				}
				List<String> accessibilityList = null;
				if(accessibilityMap.size()>0 && accessibilityMap.get(Eag2012.TAB_YOUR_INSTITUTION) != null){
					accessibilityList = accessibilityMap.get(Eag2012.TAB_YOUR_INSTITUTION);
				}else{
					accessibilityList = new ArrayList<String>();
				}
				accessibilityList.add(yourInstitution.getString("futherInformationOnExistingFacilities"));
				accessibilityMap.put(Eag2012.TAB_YOUR_INSTITUTION, accessibilityList);
				if(accessibilityValue.size()>0){
					accessibilityValue.set(0,accessibilityMap);
				}else{
					accessibilityValue.add(accessibilityMap);
				}
				eag2012.setAccessibilityValue(accessibilityValue); 	
			}
			if(yourInstitution.has("textReferencetoyourinstitutionsholdingsguide")){
				String resourceRelationHref = yourInstitution.getString("textReferencetoyourinstitutionsholdingsguide");
				List<String> resourceRelationHrefList = new ArrayList<String>();
				resourceRelationHrefList.add(resourceRelationHref);
				eag2012.setResourceRelationHref(resourceRelationHrefList);
			}
			if(yourInstitution.has("textYIHoldingsGuideLinkTitle")){
				List<Map<String, List<String>>> listRelationEnrtyMap = eag2012.getRelationEntryValue();
				if(listRelationEnrtyMap==null){
					listRelationEnrtyMap= new ArrayList<Map<String,List<String>>>();
				}
				Map<String,List<String>> relationEntryMap = null;
				if(listRelationEnrtyMap.size()>0 && listRelationEnrtyMap.get(0)!=null){
					relationEntryMap = listRelationEnrtyMap.get(0);
				}else{
					relationEntryMap = new HashMap<String,List<String>>();
				}
				List<String> relationEntryList =null;
				if(relationEntryMap.size()>0 && relationEntryMap.get(Eag2012.TAB_YOUR_INSTITUTION)!=null){
					relationEntryList= relationEntryMap.get(Eag2012.TAB_YOUR_INSTITUTION);
				}else{
					relationEntryList = new ArrayList<String>();
				}
				relationEntryList.add(yourInstitution.getString("textYIHoldingsGuideLinkTitle"));
				relationEntryMap.put(Eag2012.TAB_YOUR_INSTITUTION, relationEntryList);
			    if(listRelationEnrtyMap.size()>0){
			    	listRelationEnrtyMap.set(0, relationEntryMap);
			    }else{
			    	listRelationEnrtyMap.add(relationEntryMap);
			    }
			    eag2012.setRelationEntryValue(listRelationEnrtyMap);
			}
			if(yourInstitution.has("selectYIContinent")){
				String continent = yourInstitution.getString("selectYIContinent");
				eag2012.setGeogareaValue(continent);
			}
			if(yourInstitution.has("selectFacilitiesForDisabledPeopleAvailable")){
				List<Map<String, String>> accessibilityQuestions = eag2012.getAccessibilityQuestion();
				if(accessibilityQuestions == null){
					accessibilityQuestions = new ArrayList<Map<String, String>>(); 
				}
				Map<String, String> accessibilityQuestionRepo = null;
				if(accessibilityQuestions.size()>0 && accessibilityQuestions.get(0)!=null){
					accessibilityQuestionRepo = accessibilityQuestions.get(0);
				}else{
					accessibilityQuestionRepo = new HashMap<String, String>();
				}
				accessibilityQuestionRepo.put(Eag2012.TAB_YOUR_INSTITUTION,yourInstitution.getString("selectFacilitiesForDisabledPeopleAvailable"));
				if(accessibilityQuestions.size()>0){
					accessibilityQuestions.set(0,accessibilityQuestionRepo);
				}else{
					accessibilityQuestions.add(accessibilityQuestionRepo);
				}
				eag2012.setAccessibilityQuestion(accessibilityQuestions);	
			}
		}
		return eag2012;
	}
}
