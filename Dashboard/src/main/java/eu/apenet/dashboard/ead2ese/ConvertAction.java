package eu.apenet.dashboard.ead2ese;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

import eu.apenet.commons.types.XmlType;
import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.commons.view.jsp.SelectItem;
import eu.apenet.dashboard.AbstractInstitutionAction;
import eu.apenet.dashboard.actions.ajax.AjaxControllerAbstractAction;
import eu.apenet.dashboard.actions.content.BatchEadActions;
import eu.apenet.dashboard.actions.content.ContentManagerAction;
import eu.apenet.dashboard.services.ead.EadService;
import eu.apenet.dpt.utils.ead2ese.EseConfig;
import eu.apenet.dpt.utils.ead2ese.EseFileUtils;
import eu.apenet.dpt.utils.util.Ead2EseInformation;
import eu.apenet.persistence.dao.EadSearchOptions;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.Ead;
import eu.apenet.persistence.vo.FindingAid;
import eu.apenet.persistence.vo.QueueAction;

public class ConvertAction extends AbstractInstitutionAction {

	private static final String CREATIVECOMMONS_CPDM = "cpdm";
	private static final String CREATIVECOMMONS_CC0 = "cc0";
	private static final String CREATIVECOMMONS = "creativecommons";
	private static final String INHERITLANGUAGE_PROVIDE = "provide";
	private static final String EUROPEANA = "europeana";
	private static final String TYPE_TEXT = "TEXT";	// Constant for type "text".
	private static final String OPTION_YES = "yes";	// Constant for value "yes".
	private static final String OPTION_NO = "no";		// Constant for value "no".

	protected final Logger log = Logger.getLogger(getClass());
	/**
	 * 
	 */
	private static final long serialVersionUID = -304486360468003677L;
	private String id;
	private String batchItems;
	private String provider = "Archives Portal Europe";
	private String daoType;
    private Set<SelectItem> typeSet = new TreeSet<SelectItem>();
    private Set<SelectItem> yesNoSet = new TreeSet<SelectItem>();
    private Set<SelectItem> inheritLanguageSet = new TreeSet<SelectItem>();
    private Set<SelectItem> providerSet = new TreeSet<SelectItem>();
    private Set<SelectItem> licenseSet = new TreeSet<SelectItem>();
    private Set<SelectItem> europeanaLicenseSet = new TreeSet<SelectItem>();
    private String license;
    private String europeanaLicense;
    private String cc_js_result_uri;
    private String language;
    private String licenseAdditionalInformation;
    private Map<String, String> dateMappings;
    private String filename;
    private String validateLinks = ConvertAction.OPTION_NO;
    private String inheritLanguage = ConvertAction.OPTION_NO;
    //private List<LabelValueBean> languages = new ArrayList<SelectItem>();
    private String hierarchyPrefix;
    private String inheritOrigination= ConvertAction.OPTION_NO;
    private String inheritFileParent= ConvertAction.OPTION_NO;
    private String customDataProvider;
    private String mappingsFileFileName; 		//The uploaded file name
    private File mappingsFile;					//The uploaded file
    private String mappingsFileContentType;		//The content type of the file uploaded
    private String textDataProvider;			//Text for the data provider from element "<repository>".
    private boolean dataProviderCheck;			//Select or not the check for the data provider
    private boolean daoTypeCheck = true;
    private boolean noLanguageOnClevel = true;
    private boolean noLanguageOnParents;
    private Set<SelectItem> languages = new TreeSet<SelectItem>();
	
	@Override
	public void validate() {
		if (INHERITLANGUAGE_PROVIDE.equals(inheritLanguage)){
			if (StringUtils.isBlank(language)){
				addFieldError("language", getText("errors.required"));
			}
		} else  if (ConvertAction.OPTION_NO.equals(inheritLanguage)) {
			if (ConvertAction.TYPE_TEXT.equals(daoType)) {
				if (noLanguageOnClevel) {
					addFieldError("inheritLanguage", getText("errors.required")
						+ getText("errors.clevel.without.langmaterial"));
				}
			}
		} else if (ConvertAction.OPTION_YES.equals(inheritLanguage)) {
			if (ConvertAction.TYPE_TEXT.equals(daoType)) {
				if (this.isNoLanguageOnParents()) {
					addFieldError("inheritLanguage", getText("errors.required")
						+ getText("errors.fa.without.langmaterial"));
				}
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
		if (provider.isEmpty()) {
			addFieldError("provider", getText("errors.required"));
		}

		if (textDataProvider.isEmpty()) {
			addFieldError("textDataProvider", getText("errors.required"));
		}
	}



	@Override
	protected void buildBreadcrumbs() {
		addBreadcrumb("content.action", getText("breadcrumb.section.contentmanager"));
		addBreadcrumb(getText("breadcrumb.section.convertToEse"));
	}


    @Override
	public void prepare() throws Exception {
		super.prepare();

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
		typeSet.add(new SelectItem("3D", getText("ead2ese.content.type.3D")));
		typeSet.add(new SelectItem("IMAGE", getText("ead2ese.content.type.image")));
		typeSet.add(new SelectItem(ConvertAction.TYPE_TEXT, getText("ead2ese.content.type.text")));
		typeSet.add(new SelectItem("SOUND", getText("ead2ese.content.type.sound")));
		typeSet.add(new SelectItem("VIDEO", getText("ead2ese.content.type.video")));
		yesNoSet.add(new SelectItem(ConvertAction.OPTION_YES, getText("ead2ese.content.yes")));
		yesNoSet.add(new SelectItem(ConvertAction.OPTION_NO, getText("ead2ese.content.no")));
		inheritLanguageSet.add(new SelectItem(ConvertAction.OPTION_YES, getText("ead2ese.content.yes")));
		inheritLanguageSet.add(new SelectItem(ConvertAction.OPTION_NO, getText("ead2ese.content.no")));
		inheritLanguageSet.add(new SelectItem(INHERITLANGUAGE_PROVIDE, getText("ead2ese.label.language.select")));
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

	public String input() throws IOException, SAXException, ParserConfigurationException{
		if (StringUtils.isNotBlank(id)){
			Ead ead = DAOFactory.instance().getEadDAO().findById(Integer.parseInt(id), FindingAid.class);
			File file = EseFileUtils.getRepoFile(APEnetUtilities.getConfig().getRepoDirPath(),
					ead.getPathApenetead());
			Ead2EseInformation ead2EseInformation = new Ead2EseInformation(file, "", getAiname());
			textDataProvider = ead2EseInformation.getRepository();
			daoType = ead2EseInformation.getRoleType();
			if (StringUtils.isBlank(textDataProvider)){
				Ead2EseInformation ead2EseInformationParent = new Ead2EseInformation(file, "", null);
				if (ead2EseInformationParent.getArchdescRepository() != null
						&& !ead2EseInformationParent.getArchdescRepository().isEmpty()) {
					textDataProvider = ead2EseInformationParent.getArchdescRepository();
				}
				this.setDataProviderCheck(true);
			}
			if (StringUtils.isNotBlank(ead2EseInformation.getLanguageCode())){
				noLanguageOnClevel = false;			
			}
			if (ead2EseInformation.getAlternativeLanguages() != null
					&& !ead2EseInformation.getAlternativeLanguages().isEmpty()){
				this.setNoLanguageOnParents(false);			
			} else {
				this.setNoLanguageOnParents(true);
			}
			
		}else {		
			this.setDataProviderCheck(true);			
		}
		return SUCCESS;
	}

	
    public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}

	@SuppressWarnings("unchecked")
	public String execute() throws Exception{		
		EseConfig config = fillConfig();
		if (StringUtils.isBlank(batchItems)){
			EadService.convertToEseEdm(Integer.parseInt(id), config.getProperties());
		}else {
			if (BatchEadActions.SELECTED_ITEMS.equals(batchItems)) {

				List<Integer> ids = (List<Integer>) getServletRequest().getSession().getAttribute(
						AjaxControllerAbstractAction.LIST_IDS);
				if (ids != null) {
					EadService.addBatchToQueue(ids, getAiId(), XmlType.EAD_FA,QueueAction.CONVERT_TO_ESE_EDM,config.getProperties());
					return SUCCESS;
				} else {
					return ERROR;
				}

			} else if (BatchEadActions.SEARCHED_ITEMS.equals(batchItems)) {
				EadSearchOptions eadSearchOptions = (EadSearchOptions)getServletRequest().getSession()
						.getAttribute(ContentManagerAction.EAD_SEARCH_OPTIONS);
				EadService.addBatchToQueue(eadSearchOptions, QueueAction.CONVERT_TO_ESE_EDM,config.getProperties());
				return SUCCESS;
			} else {
				EadService.addBatchToQueue(null, getAiId(),XmlType.EAD_FA, QueueAction.CONVERT_TO_ESE_EDM,config.getProperties());
				return SUCCESS;
			}
		}
   		return SUCCESS;
    }
	protected EseConfig fillConfig(){
    	EseConfig config = new EseConfig();  	
    	config.setContextInformationPrefix(hierarchyPrefix);
    	config.setInheritElementsFromFileLevel(ConvertAction.OPTION_YES.equals(inheritFileParent));
    	config.setInheritOrigination(ConvertAction.OPTION_YES.equals(inheritOrigination));
    	config.setInheritLanguage(ConvertAction.OPTION_YES.equals(inheritLanguage));
    	if (INHERITLANGUAGE_PROVIDE.equals(inheritLanguage)){
    		config.setLanguage(language);
    	}
    	config.setType(daoType);
    	config.setProvider(provider);
    	if (customDataProvider != null && !customDataProvider.isEmpty()) {
    		config.setDataProvider(customDataProvider);
    	} else if (textDataProvider != null && !textDataProvider.isEmpty()) {
    		config.setDataProvider(textDataProvider);
    	}
    	if (EUROPEANA.equals(license)){
    		config.setRights(europeanaLicense);
    	}else if(CREATIVECOMMONS_CC0.equals(license)){
    		config.setRights("http://creativecommons.org/publicdomain/zero/1.0/");
    	}else if(CREATIVECOMMONS_CPDM.equals(license)){
    		config.setRights("http://creativecommons.org/publicdomain/mark/1.0/");
    	}else {
    		config.setRights(cc_js_result_uri);
    	}
    	config.setUseExistingDaoRole(daoTypeCheck);
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
	public String getBatchItems() {
		return batchItems;
	}
	public void setBatchItems(String batchItems) {
		this.batchItems = batchItems;
	}

	public String getTextDataProvider() {
		return textDataProvider;
	}

	public void setTextDataProvider(String textDataProvider) {
		this.textDataProvider = textDataProvider;
	}

	public boolean isDataProviderCheck() {
		return dataProviderCheck;
	}

	public void setDataProviderCheck(boolean dataProviderCheck) {
		this.dataProviderCheck = dataProviderCheck;
	}

	public boolean isDaoTypeCheck() {
		return daoTypeCheck;
	}

	public void setDaoTypeCheck(boolean daoTypeCheck) {
		this.daoTypeCheck = daoTypeCheck;
	}

	public boolean isNoLanguageOnClevel() {
		return noLanguageOnClevel;
	}

	public void setNoLanguageOnClevel(boolean noLanguageOnClevel) {
		this.noLanguageOnClevel = noLanguageOnClevel;
	}

	public boolean isNoLanguageOnParents() {
		return this.noLanguageOnParents;
	}

	public void setNoLanguageOnParents(boolean noLanguageOnParents) {
		this.noLanguageOnParents = noLanguageOnParents;
	}
	
}
