package eu.apenet.dashboard.ead2ese;

import java.io.File;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang.StringUtils;

import eu.apenet.commons.view.jsp.SelectItem;
import eu.apenet.dashboard.AbstractInstitutionAction;
import eu.apenet.dpt.utils.ead2ese.Config;

public class ConvertAction extends AbstractInstitutionAction{

	private static final String CREATIVECOMMONS_CPDM = "cpdm";
	private static final String CREATIVECOMMONS_CC0 = "cc0";
	private static final String CREATIVECOMMONS = "creativecommons";
	private static final String DATA_PROVIDER_CUSTOM = "custom";
	private static final String INHERITLANGUAGE_PROVIDE = "provide";
	private static final String EUROPEANA = "europeana";
	private static final String SEARCH = "success_search";
	/**
	 * 
	 */
	private static final long serialVersionUID = -304486360468003677L;
	private String id;
    private String xmlTypeId;
	private String pageNumber;
	private String provider = "Archives Portal Europe";
	private String daoType;
    private Set<SelectItem> typeSet = new TreeSet<SelectItem>();
    private Set<SelectItem> yesNoSet = new TreeSet<SelectItem>();
    private Set<SelectItem> inheritLanguageSet = new TreeSet<SelectItem>();
    private Set<SelectItem> providerSet = new TreeSet<SelectItem>();
    private Set<SelectItem> dataProviderSet = new TreeSet<SelectItem>();
    private Set<SelectItem> licenseSet = new TreeSet<SelectItem>();
    private Set<SelectItem> europeanaLicenseSet = new TreeSet<SelectItem>();
    private String license;
    private String europeanaLicense;
    private String cc_js_result_uri;
    private String language;
    private String licenseAdditionalInformation;
    private Map<String, String> dateMappings;
    private String filename;
    private String validateLinks = "no";
    private String inheritLanguage = "no";
    //private List<LabelValueBean> languages = new ArrayList<SelectItem>();
    private String hierarchyPrefix;
    private String inheritOrigination= "no";
    private String inheritFileParent= "no";
    private String dataProvider = "mapping";
    private String customDataProvider;
    private String mappingsFileFileName; 		//The uploaded file name
    private File mappingsFile;					//The uploaded file
    private String mappingsFileContentType;		//The content type of the file uploaded
    private String searchTerms;
    private String orderBy;
    
    private Set<SelectItem> languages = new TreeSet<SelectItem>();

	
	@Override
	public void validate() {
		if (INHERITLANGUAGE_PROVIDE.equals(inheritLanguage)){
			if (StringUtils.isBlank(language)){
				addFieldError("language", getText("errors.required"));
			}
		}
		if (DATA_PROVIDER_CUSTOM.equals(dataProvider)){
			if (StringUtils.isBlank(customDataProvider)){
				addFieldError("customDataProvider",getText("errors.required"));
			}
		}
		if (EUROPEANA.equals(license)){
			if (StringUtils.isBlank(europeanaLicense)){
				addFieldError("europeanaLicense", getText("errors.required"));
			}
		}	
		if (StringUtils.isBlank(daoType)){
			addFieldError("daoType",getText("errors.required"));
		} 
	}
	@Override
	protected void buildBreadcrumbs() {
		addBreadcrumb("content.action", getText("breadcrumb.section.contentmanager"));
		addBreadcrumb(getText("breadcrumb.section.convertToEse"));
	}


    public ConvertAction() {
		String[] isoLanguages = Locale.getISOLanguages();
		languages.add(new SelectItem("", getText("ead2ese.content.selectone")));
		for (String language : isoLanguages) {
			String languageDescription = new Locale(language).getDisplayLanguage(Locale.ENGLISH);
			//String label = language + " (" +  languageDescription + ")"; 
			languages.add(new SelectItem(language, languageDescription));
			
		}  
		//list="#{'IMAGE':'Image', 'TEXT':'Text', 'SOUND':'Sound', 'VIDEO':'Video'}"
		typeSet.add(new SelectItem("", getText("ead2ese.content.selectone")));
		typeSet.add(new SelectItem("IMAGE", getText("ead2ese.content.type.image")));
		typeSet.add(new SelectItem("TEXT", getText("ead2ese.content.type.text")));
		typeSet.add(new SelectItem("SOUND", getText("ead2ese.content.type.sound")));
		typeSet.add(new SelectItem("VIDEO", getText("ead2ese.content.type.video")));
		yesNoSet.add(new SelectItem("yes", getText("ead2ese.content.yes")));
		yesNoSet.add(new SelectItem("no", getText("ead2ese.content.no")));
		inheritLanguageSet.add(new SelectItem("yes", getText("ead2ese.content.yes")));
		inheritLanguageSet.add(new SelectItem("no", getText("ead2ese.content.no")));
		inheritLanguageSet.add(new SelectItem(INHERITLANGUAGE_PROVIDE, getText("ead2ese.label.language.select")));	
		dataProviderSet.add(new SelectItem(DATA_PROVIDER_CUSTOM, getText("ead2ese.content.dataprovider.custom")));
		dataProviderSet.add(new SelectItem("mapping", getText("ead2ese.content.dataprovider.mapping")));
		licenseSet.add(new SelectItem(EUROPEANA, getText("ead2ese.content.license.europeana")));
		licenseSet.add(new SelectItem(CREATIVECOMMONS, getText("ead2ese.content.license.creativecommons")));
		licenseSet.add(new SelectItem(CREATIVECOMMONS_CC0, getText("ead2ese.content.license.creativecommons.cc0")));
		licenseSet.add(new SelectItem(CREATIVECOMMONS_CPDM, getText("ead2ese.content.license.creativecommons.publicdomain")));
		license = EUROPEANA;
		europeanaLicenseSet.add(new SelectItem("", getText("ead2ese.content.selectone")));
		europeanaLicenseSet.add(new SelectItem("http://www.europeana.eu/rights/rr-p/", getText("ead2ese.content.license.europeana.access.paid")));
		europeanaLicenseSet.add(new SelectItem("http://www.europeana.eu/rights/rr-f/", getText("ead2ese.content.license.europeana.access.free")));
		europeanaLicenseSet.add(new SelectItem("http://www.europeana.eu/rights/rr-r/", getText("ead2ese.content.license.europeana.access.restricted")));
		europeanaLicenseSet.add(new SelectItem("http://www.europeana.eu/rights/unknown/", getText("ead2ese.content.license.europeana.access.unknown")));
		this.setHierarchyPrefix(getText("ead2ese.content.hierarchy.prefix" ));

    }
	public String input(){
		return SUCCESS;
	}
	
    public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}

    public String getXmlTypeId() {
        return xmlTypeId;
    }

    public void setXmlTypeId(String xmlTypeId) {
        this.xmlTypeId = xmlTypeId;
    }

    public String getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(String pageNumber) {
		this.pageNumber = pageNumber;
	}


	public String cancel(){
		if(this.searchTerms!=null && !this.searchTerms.isEmpty()){
			return SEARCH; 
    	}
   		return SUCCESS;
	}
	public String execute() throws Exception{
		Config config = fillConfig();
    	EAD2ESEConverter.convertEAD2ESE(new Integer(id) , config);
   		return SUCCESS;
    }
	protected Config fillConfig(){
    	Config config = new Config();  	
    	config.setContextInformationPrefix(hierarchyPrefix);
    	config.setInheritElementsFromFileLevel("yes".equals(inheritFileParent));
    	config.setInheritOrigination("yes".equals(inheritOrigination));
    	config.setInheritLanguage("yes".equals(inheritLanguage));
    	if (INHERITLANGUAGE_PROVIDE.equals(inheritLanguage)){
    		config.setLanguage(language);
    	}
    	config.setType(daoType);
    	config.setProvider(provider);
    	config.setDataProvider(customDataProvider);
    	if (EUROPEANA.equals(license)){
    		config.setRights(europeanaLicense);
    	}else if(CREATIVECOMMONS_CC0.equals(license)){
    		config.setRights("http://creativecommons.org/publicdomain/zero/1.0/");
    	}else if(CREATIVECOMMONS_CPDM.equals(license)){
    		config.setRights("http://creativecommons.org/publicdomain/mark/1.0/");
    	}else {
    		config.setRights(cc_js_result_uri);
    	}
    	config.setRightsAdditionalInformation(licenseAdditionalInformation);
    	return config;
	}
	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}

	public String getDaoType() {
		return daoType;
	}

	public void setDaoType(String daoType) {
		this.daoType = daoType;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public Map<String, String> getDateMappings() {
		return dateMappings;
	}

	public void setDateMappings(Map<String, String> dateMappings) {
		this.dateMappings = dateMappings;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getValidateLinks() {
		return validateLinks;
	}

	public void setValidateLinks(String validateLinks) {
		this.validateLinks = validateLinks;
	}

	public String getInheritLanguage() {
		return inheritLanguage;
	}

	public void setInheritLanguage(String inheritLanguage) {
		this.inheritLanguage = inheritLanguage;
	}

	public String getHierarchyPrefix() {
		return hierarchyPrefix;
	}

	public void setHierarchyPrefix(String hierarchyPrefix) {
		this.hierarchyPrefix = hierarchyPrefix;
	}





	public String getInheritOrigination() {
		return inheritOrigination;
	}

	public void setInheritOrigination(String inheritOrigination) {
		this.inheritOrigination = inheritOrigination;
	}

	public String getInheritFileParent() {
		return inheritFileParent;
	}

	public void setInheritFileParent(String inheritFileParent) {
		this.inheritFileParent = inheritFileParent;
	}

	
	public Set<SelectItem> getLanguages() {
		return languages;
	}
	public void setLanguages(Set<SelectItem> languages) {
		this.languages = languages;
	}
	
	
	public String getMappingsFileFileName() {
		return mappingsFileFileName;
	}
	public void setMappingsFileFileName(String mappingsFileFileName) {
		this.mappingsFileFileName = mappingsFileFileName;
	}
	public File getMappingsFile() {
		return mappingsFile;
	}
	public void setMappingsFile(File mappingsFile) {
		this.mappingsFile = mappingsFile;
	}
	public String getMappingsFileContentType() {
		return mappingsFileContentType;
	}
	public void setMappingsFileContentType(String mappingsFileContentType) {
		this.mappingsFileContentType = mappingsFileContentType;
	}

	public String getCustomDataProvider() {
		return customDataProvider;
	}

	public void setCustomDataProvider(String customDataProvider) {
		this.customDataProvider = customDataProvider;
	}


	public Set<SelectItem> getTypeSet() {
		return typeSet;
	}

	public void setTypeSet(Set<SelectItem> typeSet) {
		this.typeSet = typeSet;
	}

	public Set<SelectItem> getYesNoSet() {
		return yesNoSet;
	}

	public void setYesNoSet(Set<SelectItem> yesNoSet) {
		this.yesNoSet = yesNoSet;
	}

	public Set<SelectItem> getInheritLanguageSet() {
		return inheritLanguageSet;
	}

	public void setInheritLanguageSet(Set<SelectItem> inheritLanguageSet) {
		this.inheritLanguageSet = inheritLanguageSet;
	}

	public Set<SelectItem> getProviderSet() {
		return providerSet;
	}

	public void setProviderSet(Set<SelectItem> providerSet) {
		this.providerSet = providerSet;
	}

	public Set<SelectItem> getDataProviderSet() {
		return dataProviderSet;
	}

	public void setDataProviderSet(Set<SelectItem> dataProviderSet) {
		this.dataProviderSet = dataProviderSet;
	}

	public String getDataProvider() {
		return dataProvider;
	}

	public void setDataProvider(String dataProvider) {
		this.dataProvider = dataProvider;
	}

	public Set<SelectItem> getLicenseSet() {
		return licenseSet;
	}

	public void setLicenseSet(Set<SelectItem> licenseSet) {
		this.licenseSet = licenseSet;
	}



	public Set<SelectItem> getEuropeanaLicenseSet() {
		return europeanaLicenseSet;
	}

	public void setEuropeanaLicenseSet(Set<SelectItem> europeanaLicenseSet) {
		this.europeanaLicenseSet = europeanaLicenseSet;
	}

	public String getLicense() {
		return license;
	}

	public void setLicense(String license) {
		this.license = license;
	}

	public String getEuropeanaLicense() {
		return europeanaLicense;
	}

	public void setEuropeanaLicense(String europeanaLicense) {
		this.europeanaLicense = europeanaLicense;
	}

	public String getCc_js_result_uri() {
		return cc_js_result_uri;
	}

	public void setCc_js_result_uri(String cc_js_result_uri) {
		this.cc_js_result_uri = cc_js_result_uri;
	}

	public String getLicenseAdditionalInformation() {
		return licenseAdditionalInformation;
	}

	public void setLicenseAdditionalInformation(String licenseAdditionalInformation) {
		this.licenseAdditionalInformation = licenseAdditionalInformation;
	}

	public void setSearchTerms(String searchTerms) {
		this.searchTerms = searchTerms;
	}

	public String getSearchTerms() {
		return searchTerms;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	public String getOrderBy() {
		return orderBy;
	}
	
}
