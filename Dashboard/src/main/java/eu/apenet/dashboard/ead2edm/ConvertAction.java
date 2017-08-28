package eu.apenet.dashboard.ead2edm;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashSet;
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
import eu.apenet.dashboard.actions.content.ContentManagerAction;
import eu.apenet.dashboard.actions.content.ead.BatchEadActions;
import eu.apenet.dashboard.services.ead.EadService;
import eu.apenet.dashboard.utils.PropertiesKeys;
import eu.apenet.dashboard.utils.PropertiesUtil;
import eu.apenet.dpt.utils.ead2edm.EdmConfig;
import eu.apenet.dpt.utils.ead2edm.EdmFileUtils;
import eu.apenet.dpt.utils.util.Ead2EdmInformation;
import eu.apenet.persistence.dao.ContentSearchOptions;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.Ead;
import eu.apenet.persistence.vo.FindingAid;
import eu.apenet.persistence.vo.QueueAction;

public class ConvertAction extends AbstractInstitutionAction {

    // Licence types.
    private static final String CREATIVECOMMONS_CPDM = "cpdm";
    private static final String CREATIVECOMMONS_CC0 = "cc0";
    private static final String CREATIVECOMMONS = "creativecommons";
    private static final String EUROPEANA = "europeana";
    // DAO role.
    private static final String TYPE_3D = "3D"; // Constant for type "3D".
    private static final String TYPE_IMAGE = "IMAGE"; // Constant for type "image".
    private static final String TYPE_SOUND = "TEXT"; // Constant for type "sound".
    private static final String TYPE_TEXT = "TEXT"; // Constant for type "text".
    private static final String TYPE_VIDEO = "VIDEO"; // Constant for type "video".
    // Inherit options.
    private static final String INHERIT_PROVIDE = "provide";
    // Options.
    private static final String OPTION_YES = "yes"; // Constant for value "yes".
    private static final String OPTION_NO = "no"; // Constant for value "no".
    // Conversion type.
    private static final String OPTION_MINIMAL = "minimal"; // Constant for value "minimal".
    private static final String OPTION_FULL = "full"; // Constant for value "full".
    // Source of identifiers.
    private static final String OPTION_UNITID = "unitid";
    private static final String OPTION_CID = "cid";
    // Source of title for fonds.
    private static final String OPTION_ARCHDESC_UNITTITLE = "archdescUnittitle";
    private static final String OPTION_TITLESTMT_TITLEPROPER = "titlestmtTitleproper";
    // Europeana licences.
    private static final String EUROPEANA_INCOPY = "http://rightsstatements.org/vocab/InC/1.0/";
    private static final String EUROPEANA_INCOPY_EDUUSE = "http://rightsstatements.org/vocab/InC-EDU/1.0/";
    private static final String EUROPEANA_INCOPY_EUORPHAN = "http://rightsstatements.org/vocab/InC-OW-EU/1.0/";
    private static final String EUROPEANA_NOCOPY_NONCOMMERCIAL = "http://rightsstatements.org/vocab/NoC-NC/1.0/";
    private static final String EUROPEANA_NOCOPY_OTHERLEGAL = "http://rightsstatements.org/vocab/NoC-OKLR/1.0/";
    private static final String EUROPEANA_COPYRIGHT_NOT_EVALUATED = "http://rightsstatements.org/vocab/CNE/1.0/";

    protected final Logger log = Logger.getLogger(getClass());
    /**
     * Serializable.
     */
    private static final long serialVersionUID = -304486360468003677L;
    private String id;
    private String batchItems;
    private String provider = "Archives Portal Europe";
    private String daoType;
    private Set<SelectItem> conversionTypeSet = new LinkedHashSet<SelectItem>();
    private Set<SelectItem> sourceOfIdentifiersSet = new TreeSet<SelectItem>();
    private Set<SelectItem> sourceOfFondsTitleSet = new TreeSet<SelectItem>();
    private Set<SelectItem> typeSet = new TreeSet<SelectItem>();
    private Set<SelectItem> yesNoSet = new TreeSet<SelectItem>();
    private Set<SelectItem> inheritLanguageSet = new TreeSet<SelectItem>();
//    private Set<SelectItem> inheritRightsInfoSet = new TreeSet<SelectItem>();
    private Set<SelectItem> providerSet = new TreeSet<SelectItem>();
    private Set<SelectItem> licenseSet = new TreeSet<SelectItem>();
    private Set<SelectItem> europeanaLicenseSet = new TreeSet<SelectItem>();
    private String license;
    private String europeanaLicense;
    private String cc_js_result_uri;
    private String languageSelection;
    private String licenseAdditionalInformation;
    private Map<String, String> dateMappings;
    private String filename;
    private String conversionType = ConvertAction.OPTION_MINIMAL;
    private String sourceOfIdentifiers = ConvertAction.OPTION_UNITID;
    private String sourceOfFondsTitle = ConvertAction.OPTION_ARCHDESC_UNITTITLE;
    private String validateLinks = ConvertAction.OPTION_NO;
    private String inheritLanguage = ConvertAction.OPTION_NO;
    private String inheritRightsInfo = ConvertAction.OPTION_NO;
    //private List<LabelValueBean> languages = new ArrayList<SelectItem>();
    private String inheritOrigination = ConvertAction.OPTION_NO;
    private String inheritFileParent = ConvertAction.OPTION_NO;
    private String inheritUnittitle = ConvertAction.OPTION_NO;
    private String customDataProvider;
    private String mappingsFileFileName; 		//The uploaded file name
    private File mappingsFile;					//The uploaded file
    private String mappingsFileContentType;		//The content type of the file uploaded
    private String textDataProvider;			//Text for the data provider from element "<repository>".
    private boolean batchConversion;
    private boolean dataProviderCheck = true;			//Select or not the check for the data provider
    private boolean daoTypeCheck = true;
    private boolean inheritFileParentCheck;
    private boolean inheritOriginationCheck;
    private boolean inheritUnittitleCheck;
    private boolean inheritLanguageCheck = true;
    private boolean languageOfTheMaterialCheck = true;
    private boolean noLanguageOnClevel = true;
    private boolean noLanguageOnParents;
    private boolean noLicenceOnClevel = true;
    private boolean noLicenceOnParents;
    private boolean hasArchdescUnittitle = true;
    private boolean hasTitlestmtTitleproper = true;
    private Set<SelectItem> languages = new TreeSet<SelectItem>();
    private boolean licenseCheck = true;

    @Override
    public void validate() {
        if (this.isBatchConversion()) {
            if (StringUtils.isBlank(this.getLanguageSelection())) {
//				this.addFieldError("languageOfTheMaterialCheck", getText("errors.required"));
                this.addFieldError("languageSelection", getText("errors.required"));
            }
        } else {
            if (ConvertAction.INHERIT_PROVIDE.equals(this.getInheritLanguage())) {
                if (StringUtils.isBlank(this.getLanguageSelection())) {
                    addFieldError("languageSelection", getText("errors.required"));
                }
            } else if (ConvertAction.OPTION_NO.equals(this.getInheritLanguage())) {
                if (this.isNoLanguageOnClevel()) {
                    addFieldError("inheritLanguage", getText("errors.required")
                            + ". " + getText("errors.clevel.without.langmaterial"));
                } else if (!this.isLanguageOfTheMaterialCheck()) {
                    addFieldError("languageOfTheMaterialCheck", getText("errors.required")
                            + ". " + getText("errors.provide.language"));
                }
            } else if (ConvertAction.OPTION_YES.equals(this.getInheritLanguage())) {
                if (this.isNoLanguageOnParents()) {
                    addFieldError("inheritLanguage", getText("errors.required")
                            + ". " + getText("errors.fa.without.langmaterial"));
                } else if (!this.isLanguageOfTheMaterialCheck()) {
                    addFieldError("languageOfTheMaterialCheck", getText("errors.required")
                            + ". " + getText("errors.provide.language"));
                }
            }
        }

        if (!this.isBatchConversion() && (this.isHasArchdescUnittitle() || this.isHasTitlestmtTitleproper())) {
            if (ConvertAction.OPTION_ARCHDESC_UNITTITLE.equals(this.getSourceOfFondsTitle())) {
                if (!this.isHasArchdescUnittitle()) {
                    addFieldError("sourceOfFondsTitle", getText("ead2edm.errors.fondsTitle.useAlternativeSource"));
                }
            }
            if (ConvertAction.OPTION_TITLESTMT_TITLEPROPER.equals(this.getSourceOfFondsTitle())) {
                if (!this.isHasTitlestmtTitleproper()) {
                    addFieldError("sourceOfFondsTitle", getText("ead2edm.errors.fondsTitle.useAlternativeSource"));
                }
            }
        }

        if (this.getLicense() == null) {
            addFieldError("license", getText("errors.required"));
        }
        if (ConvertAction.EUROPEANA.equals(this.getLicense())) {
            if (StringUtils.isBlank(this.getEuropeanaLicense())) {
                addFieldError("europeanaLicense", getText("errors.required"));
            }
            if (ConvertAction.EUROPEANA_NOCOPY_OTHERLEGAL.equals(this.getEuropeanaLicense())) {
                if (StringUtils.isBlank(this.getLicenseAdditionalInformation())) {
                    addFieldError("licenseAdditionalInformationCheck", getText("ead2edm.errors.europeana.otherLegalRestrictions.provideAdditionalInfo"));
                }
            }
        }
        if (StringUtils.isBlank(this.getDaoType())) {
            addFieldError("daoType", getText("errors.required"));
        }

        if (this.getTextDataProvider().isEmpty()) {
            addFieldError("textDataProvider", getText("errors.required"));
        }
    }

    @Override
    protected void buildBreadcrumbs() {
        super.buildBreadcrumbs();
        addBreadcrumb("contentmanager.action", getText("breadcrumb.section.contentmanager"));
        addBreadcrumb(getText("breadcrumb.section.convertToEse"));
    }

    @Override
    public void prepare() throws Exception {
        super.prepare();
        String[] isoLanguages = Locale.getISOLanguages();
        for (String language : isoLanguages) {
            String languageDescription = new Locale(language).getDisplayLanguage(Locale.ENGLISH);
            //String label = language + " (" +  languageDescription + ")";
            this.languages.add(new SelectItem(language, languageDescription));
        }

        this.conversionTypeSet.add(new SelectItem(ConvertAction.OPTION_MINIMAL, this.getText("ead2ese.label.minimal.conversion")));
        this.conversionTypeSet.add(new SelectItem(ConvertAction.OPTION_FULL, this.getText("ead2ese.label.full.conversion")));
        this.sourceOfIdentifiersSet.add(new SelectItem(ConvertAction.OPTION_UNITID, this.getText("ead2ese.label.id.unitid").replaceAll(">", "&#62;").replaceAll("<", "&#60;")));
        this.sourceOfIdentifiersSet.add(new SelectItem(ConvertAction.OPTION_CID, this.getText("ead2ese.label.id.c").replaceAll(">", "&#62;").replaceAll("<", "&#60;")));
        this.sourceOfFondsTitleSet.add(new SelectItem(ConvertAction.OPTION_ARCHDESC_UNITTITLE, this.getText("ead2ese.label.fondstitle.archdescUnittitle").replaceAll(">", "&#62;").replaceAll("<", "&#60;")));
        this.sourceOfFondsTitleSet.add(new SelectItem(ConvertAction.OPTION_TITLESTMT_TITLEPROPER, this.getText("ead2ese.label.fondstitle.titlestmtTitleproper").replaceAll(">", "&#62;").replaceAll("<", "&#60;")));
        //list="#{'IMAGE':'Image', 'TEXT':'Text', 'SOUND':'Sound', 'VIDEO':'Video', '3D':'3D'}"
        this.typeSet.add(new SelectItem("", this.getText("ead2ese.content.selectone")));
        this.typeSet.add(new SelectItem(ConvertAction.TYPE_3D, this.getText("ead2ese.content.type.3D")));
        this.typeSet.add(new SelectItem(ConvertAction.TYPE_IMAGE, this.getText("ead2ese.content.type.image")));
        this.typeSet.add(new SelectItem(ConvertAction.TYPE_SOUND, this.getText("ead2ese.content.type.sound")));
        this.typeSet.add(new SelectItem(ConvertAction.TYPE_TEXT, this.getText("ead2ese.content.type.text")));
        this.typeSet.add(new SelectItem(ConvertAction.TYPE_VIDEO, this.getText("ead2ese.content.type.video")));
        this.yesNoSet.add(new SelectItem(ConvertAction.OPTION_YES, this.getText("ead2ese.content.yes")));
        this.yesNoSet.add(new SelectItem(ConvertAction.OPTION_NO, this.getText("ead2ese.content.no")));
        this.inheritLanguageSet.add(new SelectItem(ConvertAction.OPTION_YES, this.getText("ead2ese.content.yes")));
        this.inheritLanguageSet.add(new SelectItem(ConvertAction.OPTION_NO, this.getText("ead2ese.content.no")));
        this.inheritLanguageSet.add(new SelectItem(ConvertAction.INHERIT_PROVIDE, this.getText("ead2ese.label.language.select")));
        this.licenseSet.add(new SelectItem(ConvertAction.EUROPEANA, this.getText("ead2ese.content.license.europeana")));
        this.licenseSet.add(new SelectItem(ConvertAction.CREATIVECOMMONS, this.getText("ead2ese.content.license.creativecommons")));
        this.licenseSet.add(new SelectItem(ConvertAction.CREATIVECOMMONS_CC0, this.getText("ead2ese.content.license.creativecommons.cc0")));
        this.licenseSet.add(new SelectItem(ConvertAction.CREATIVECOMMONS_CPDM, this.getText("ead2ese.content.license.creativecommons.publicdomain")));
        //this.license = EUROPEANA;
        this.europeanaLicenseSet.add(new SelectItem("", this.getText("ead2ese.content.selectone")));
        this.europeanaLicenseSet.add(new SelectItem(ConvertAction.EUROPEANA_INCOPY, this.getText("ead2ese.content.license.europeana.incopyright")));
        this.europeanaLicenseSet.add(new SelectItem(ConvertAction.EUROPEANA_INCOPY_EDUUSE, this.getText("ead2ese.content.license.europeana.incopyright.eduuse")));
        this.europeanaLicenseSet.add(new SelectItem(ConvertAction.EUROPEANA_INCOPY_EUORPHAN, this.getText("ead2ese.content.license.europeana.incopyright.euorphan")));
        this.europeanaLicenseSet.add(new SelectItem(ConvertAction.EUROPEANA_NOCOPY_NONCOMMERCIAL, this.getText("ead2ese.content.license.europeana.nocopyright.noncommercial")));
        this.europeanaLicenseSet.add(new SelectItem(ConvertAction.EUROPEANA_NOCOPY_OTHERLEGAL, this.getText("ead2ese.content.license.europeana.nocopyright.otherlegal")));
        this.europeanaLicenseSet.add(new SelectItem(ConvertAction.EUROPEANA_COPYRIGHT_NOT_EVALUATED, this.getText("ead2ese.content.license.europeana.copyrightnotevaluated")));
    }

    @Override
    public String input() throws IOException, SAXException, ParserConfigurationException {
        if (StringUtils.isNotBlank(id)) {
            Ead ead = DAOFactory.instance().getEadDAO().findById(Integer.parseInt(id), FindingAid.class);
            File file = EdmFileUtils.getRepoFile(APEnetUtilities.getConfig().getRepoDirPath(),
                    ead.getPathApenetead());
            Ead2EdmInformation ead2EdmInformation = new Ead2EdmInformation(file, "", getAiname());
            textDataProvider = ead2EdmInformation.getArchdescRepository();
            daoType = ead2EdmInformation.getRoleType();
            if (StringUtils.isBlank(textDataProvider)) {
                Ead2EdmInformation ead2EseInformationParent = new Ead2EdmInformation(file, "", null);
                if (ead2EseInformationParent.getArchdescRepository() != null
                        && !ead2EseInformationParent.getArchdescRepository().isEmpty()) {
                    textDataProvider = ead2EseInformationParent.getArchdescRepository();
                }
                this.setDataProviderCheck(true);
            }
            this.setNoLanguageOnClevel(!ead2EdmInformation.isLanguagesOnAllCLevels());
            this.setNoLanguageOnParents(!ead2EdmInformation.isLanguagesOnParent());
            this.setNoLicenceOnClevel(!ead2EdmInformation.isLicensesOnAllCLevels());
            this.setNoLicenceOnParents(!ead2EdmInformation.isLicensesOnParent());
            this.setHasArchdescUnittitle(StringUtils.isNotBlank(ead2EdmInformation.getArchdescUnittitle()));
            this.setHasTitlestmtTitleproper(StringUtils.isNotBlank(ead2EdmInformation.getTitlestmtTitleproper()));
            if (StringUtils.isNotBlank(ead2EdmInformation.getArchdescLicenceType())) {
                licenseCheck = true;
            }
            this.setBatchConversion(false);
        } else {
            this.setDataProviderCheck(true);
            this.setBatchConversion(true);
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
    @Override
    public String execute() throws Exception {
        EdmConfig config = fillConfig();
        if (StringUtils.isBlank(batchItems)) {
            EadService.convertToEseEdm(Integer.parseInt(id), config.getProperties());
        } else {
            if (BatchEadActions.SELECTED_ITEMS.equals(batchItems)) {

                List<Integer> ids = (List<Integer>) getServletRequest().getSession().getAttribute(
                        AjaxControllerAbstractAction.LIST_IDS);
                if (ids != null) {
                    EadService.addBatchToQueue(ids, getAiId(), XmlType.EAD_FA, QueueAction.CONVERT_TO_ESE_EDM, config.getProperties());
                    return SUCCESS;
                } else {
                    return ERROR;
                }

            } else if (BatchEadActions.SEARCHED_ITEMS.equals(batchItems)) {
                ContentSearchOptions eadSearchOptions = (ContentSearchOptions) getServletRequest().getSession()
                        .getAttribute(ContentManagerAction.EAD_SEARCH_OPTIONS);
                EadService.addBatchToQueue(eadSearchOptions, QueueAction.CONVERT_TO_ESE_EDM, config.getProperties());
                return SUCCESS;
            } else {
                EadService.addBatchToQueue(null, getAiId(), XmlType.EAD_FA, QueueAction.CONVERT_TO_ESE_EDM, config.getProperties());
                return SUCCESS;
            }
        }
        return SUCCESS;
    }

    protected EdmConfig fillConfig() {
        EdmConfig config = new EdmConfig();
        config.setInheritElementsFromFileLevel(ConvertAction.OPTION_YES.equals(this.getInheritFileParent()));
        config.setInheritOrigination(ConvertAction.OPTION_YES.equals(this.getInheritOrigination()));
        config.setInheritUnittitle(ConvertAction.OPTION_YES.equals(this.getInheritUnittitle()));

        config.setInheritLanguage(true);
        if (this.isBatchConversion()) {
            if (!this.isLanguageOfTheMaterialCheck()) {
                config.setInheritLanguage(false);
            }
            String parseLanguages = this.getLanguageSelection().replaceAll(",", "");
            config.setLanguage(parseLanguages);
        } else {
            config.setInheritLanguage(!ConvertAction.OPTION_NO.equals(this.getInheritLanguage()));
            if (ConvertAction.INHERIT_PROVIDE.equals(this.getInheritLanguage())) {
                if (!this.isLanguageOfTheMaterialCheck()) {
                    config.setInheritLanguage(false);
                }
                String parseLanguages = this.getLanguageSelection().replaceAll(",", "");
                config.setLanguage(parseLanguages);
            }
        }

        config.setUseExistingLanguage(this.isLanguageOfTheMaterialCheck());
        config.setUseExistingRepository(this.isDataProviderCheck());

        config.setType(this.getDaoType());
        config.setProvider(this.getProvider());
        if (this.getCustomDataProvider() != null && !this.getCustomDataProvider().isEmpty()) {
            config.setDataProvider(this.getCustomDataProvider());
        } else if (this.getTextDataProvider() != null && !this.getTextDataProvider().isEmpty()) {
            config.setDataProvider(this.getTextDataProvider());
        }

        config.setInheritRightsInfo(true);
        config.setUseExistingRightsInfo(this.isLicenseCheck());
        if (ConvertAction.EUROPEANA.equals(this.getLicense())) {
            config.setRights(this.getEuropeanaLicense());
        } else if (ConvertAction.CREATIVECOMMONS_CC0.equals(license)) {
            config.setRights("http://creativecommons.org/publicdomain/zero/1.0/");
        } else if (ConvertAction.CREATIVECOMMONS_CPDM.equals(license)) {
            config.setRights("http://creativecommons.org/publicdomain/mark/1.0/");
        } else {
            config.setRights(this.getCc_js_result_uri());
        }
        config.setUseExistingDaoRole(this.isDaoTypeCheck());
        config.setRightsAdditionalInformation(this.getLicenseAdditionalInformation());
        if (ConvertAction.OPTION_MINIMAL.equalsIgnoreCase(this.getConversionType())) {
            config.setMinimalConversion(true);
        } else {
            config.setMinimalConversion(false);
        }

        // Set the source of identifiers.
        if (ConvertAction.OPTION_UNITID.equalsIgnoreCase(this.getSourceOfIdentifiers())) {
            config.setIdSource(ConvertAction.OPTION_UNITID);
        } else {
            config.setIdSource(ConvertAction.OPTION_CID);
        }

        // Set the source of the title of the fonds.
        if (ConvertAction.OPTION_ARCHDESC_UNITTITLE.equalsIgnoreCase(this.getSourceOfFondsTitle())) {
            config.setUseArchUnittitle(true);
        } else {
            config.setUseArchUnittitle(false);
        }

        //if id is not empty, oaiIdentifier and repositoryCode will be filled here; otherwise this is done in EadService
        //immediately before the respective file is added to the queue; see also
        //EadService.addBatchToQueue(ContentSearchOptions eadSearchOptions, QueueAction queueAction, Properties preferences)
        if (id != null && !id.isEmpty()) {
            Ead ead = DAOFactory.instance().getEadDAO().findById(Integer.parseInt(id), FindingAid.class);
            String oaiIdentifier = ead.getArchivalInstitution().getRepositorycode()
                    + APEnetUtilities.FILESEPARATOR + "fa"
                    + APEnetUtilities.FILESEPARATOR + ead.getEadid();
            config.setEdmIdentifier(oaiIdentifier);
            config.setRepositoryCode(ead.getArchivalInstitution().getRepositorycode());
        }
        config.setHost(PropertiesUtil.get(PropertiesKeys.APE_PORTAL_DOMAIN));
        config.setXmlTypeName("fa");

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

    public String getLanguageSelection() {
        return languageSelection;
    }

    public void setLanguageSelection(String languageSelection) {
        this.languageSelection = languageSelection;
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

    public String getInheritRightsInfo() {
        return inheritRightsInfo;
    }

    public void setInheritRightsInfo(String inheritRightsInfo) {
        this.inheritRightsInfo = inheritRightsInfo;
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

    public String getInheritUnittitle() {
        return inheritUnittitle;
    }

    public void setInheritUnittitle(String inheritUnittitle) {
        this.inheritUnittitle = inheritUnittitle;
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

    public boolean isNoLicenceOnClevel() {
        return noLicenceOnClevel;
    }

    public void setNoLicenceOnClevel(boolean noLicenceOnClevel) {
        this.noLicenceOnClevel = noLicenceOnClevel;
    }

    public boolean isNoLicenceOnParents() {
        return noLicenceOnParents;
    }

    public void setNoLicenceOnParents(boolean noLicenceOnParents) {
        this.noLicenceOnParents = noLicenceOnParents;
    }

    public boolean isHasArchdescUnittitle() {
        return hasArchdescUnittitle;
    }

    public void setHasArchdescUnittitle(boolean hasArchdescUnittitle) {
        this.hasArchdescUnittitle = hasArchdescUnittitle;
    }

    public boolean isHasTitlestmtTitleproper() {
        return hasTitlestmtTitleproper;
    }

    public void setHasTitlestmtTitleproper(boolean hasTitlestmtTitleproper) {
        this.hasTitlestmtTitleproper = hasTitlestmtTitleproper;
    }

    /**
     * @return the conversionTypeSet
     */
    public Set<SelectItem> getConversionTypeSet() {
        return this.conversionTypeSet;
    }

    /**
     * @param conversionTypeSet the conversionTypeSet to set
     */
    public void setConversionTypeSet(Set<SelectItem> conversionTypeSet) {
        this.conversionTypeSet = conversionTypeSet;
    }

    /**
     * @return the sourceOfIdentifiersSet
     */
    public Set<SelectItem> getSourceOfIdentifiersSet() {
        return this.sourceOfIdentifiersSet;
    }

    /**
     * @param sourceOfIdentifiersSet the sourceOfIdentifiersSet to set
     */
    public void setSourceOfIdentifiersSet(Set<SelectItem> sourceOfIdentifiersSet) {
        this.sourceOfIdentifiersSet = sourceOfIdentifiersSet;
    }

    public Set<SelectItem> getSourceOfFondsTitleSet() {
        return sourceOfFondsTitleSet;
    }

    public void setSourceOfFondsTitleSet(Set<SelectItem> sourceOfFondsTitleSet) {
        this.sourceOfFondsTitleSet = sourceOfFondsTitleSet;
    }

    /**
     * @return the conversionType
     */
    public String getConversionType() {
        return this.conversionType;
    }

    /**
     * @param conversionType the conversionType to set
     */
    public void setConversionType(String conversionType) {
        this.conversionType = conversionType;
    }

    /**
     * @param sourceOfIdentifiers the sourceOfIdentifiers to set
     */
    public void setSourceOfIdentifiers(String sourceOfIdentifiers) {
        this.sourceOfIdentifiers = sourceOfIdentifiers;
    }

    /**
     * @return the sourceOfIdentifiers
     */
    public String getSourceOfIdentifiers() {
        return this.sourceOfIdentifiers;
    }

    public String getSourceOfFondsTitle() {
        return sourceOfFondsTitle;
    }

    public void setSourceOfFondsTitle(String sourceOfFondsTitle) {
        this.sourceOfFondsTitle = sourceOfFondsTitle;
    }

    /**
     * @return the inheritFileParentCheck
     */
    public boolean isInheritFileParentCheck() {
        return this.inheritFileParentCheck;
    }

    /**
     * @param inheritFileParentCheck the inheritFileParentCheck to set
     */
    public void setInheritFileParentCheck(boolean inheritFileParentCheck) {
        this.inheritFileParentCheck = inheritFileParentCheck;
    }

    /**
     * @return the inheritOriginationCheck
     */
    public boolean isInheritOriginationCheck() {
        return this.inheritOriginationCheck;
    }

    /**
     * @param inheritOriginationCheck the inheritOriginationCheck to set
     */
    public void setInheritOriginationCheck(boolean inheritOriginationCheck) {
        this.inheritOriginationCheck = inheritOriginationCheck;
    }

    public boolean isInheritUnittitleCheck() {
        return inheritUnittitleCheck;
    }

    /**
     * @param inheritUnittitleCheck
     */
    public void setInheritUnittitleCheck(boolean inheritUnittitleCheck) {
        this.inheritUnittitleCheck = inheritUnittitleCheck;
    }

    public boolean isInheritLanguageCheck() {
        return this.inheritLanguageCheck;
    }

    /**
     * @param inheritLanguageCheck the inheritLanguageCheck to set
     */
    public void setInheritLanguageCheck(boolean inheritLanguageCheck) {
        this.inheritLanguageCheck = inheritLanguageCheck;
    }

    /**
     * @return the languageOfTheMaterialCheck
     */
    public boolean isLanguageOfTheMaterialCheck() {
        return this.languageOfTheMaterialCheck;
    }

    /**
     * @param languageOfTheMaterialCheck the languageOfTheMaterialCheck to set
     */
    public void setLanguageOfTheMaterialCheck(boolean languageOfTheMaterialCheck) {
        this.languageOfTheMaterialCheck = languageOfTheMaterialCheck;
    }

    /**
     * @return the batchConversion
     */
    public boolean isBatchConversion() {
        return this.batchConversion;
    }

    /**
     * @param batchConversion the batchConversion to set
     */
    public void setBatchConversion(boolean batchConversion) {
        this.batchConversion = batchConversion;
    }

    public boolean isLicenseCheck() {
        return licenseCheck;
    }

    public void setLicenseCheck(boolean licenseCheck) {
        this.licenseCheck = licenseCheck;
    }

}
