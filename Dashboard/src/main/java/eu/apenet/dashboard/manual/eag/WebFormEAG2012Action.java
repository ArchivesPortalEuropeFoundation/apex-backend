package eu.apenet.dashboard.manual.eag;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

import com.opensymphony.xwork2.ActionSupport;

/**
 * Action used to manage and store the new EAG2012.
 */
public class WebFormEAG2012Action extends ActionSupport {

	/**
	 * Serializable.
	 */
	private static final long serialVersionUID = 732801399037503323L;

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

    private Map<String,String> yesNoMap = new HashMap<String,String>();
    private Map<String,String> typeOfInstitutionMap = new LinkedHashMap<String,String>();
    private Map<String,String> continentOfInstitutionMap = new LinkedHashMap<String,String>();

	// Attributes.
	// Common to various tabs.
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
		Locale[] locales = Locale.getAvailableLocales();
		Map<String,String> languages = new LinkedHashMap<String,String>();
		languages.put(WebFormEAG2012Action.OPTION_NONE, "---");
		for (int i = 0; i < locales.length; i++) {
			languages.put(locales[i].getCountry(), locales[i].getDisplayLanguage(Locale.ENGLISH));
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
}
