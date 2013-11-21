/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.apenet.dashboard.manual;

import eu.apenet.commons.types.XmlType;
import eu.apenet.commons.view.jsp.SelectItem;
import eu.apenet.dashboard.AbstractInstitutionAction;
import eu.apenet.persistence.dao.IngestionprofileDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.Ingestionprofile;
import eu.apenet.persistence.vo.IngestionprofileDefaultDaoType;
import eu.apenet.persistence.vo.IngestionprofileDefaultExistingFileAction;
import eu.apenet.persistence.vo.IngestionprofileDefaultNoEadidAction;
import eu.apenet.persistence.vo.IngestionprofileDefaultUploadAction;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TreeSet;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 *
 * @author papp
 */
public class IngestionprofilesAction extends AbstractInstitutionAction {

    private static final String CREATIVECOMMONS_CPDM = "cpdm";
    private static final String CREATIVECOMMONS_CC0 = "cc0";
    private static final String CREATIVECOMMONS = "creativecommons";
    private static final String EUROPEANA = "europeana";
    private static final String INHERITLANGUAGE_PROVIDE = "provide";

    //Collections for basic tab
    private Set<SelectItem> ingestionprofiles = new LinkedHashSet<SelectItem>();
    private Set<SelectItem> associatedFiletypes = new LinkedHashSet<SelectItem>();
    private Set<SelectItem> uploadedFileActions = new LinkedHashSet<SelectItem>();
    private Set<SelectItem> existingFileActions = new LinkedHashSet<SelectItem>();
    private Set<SelectItem> noEadidActions = new LinkedHashSet<SelectItem>();
    private Set<SelectItem> daoTypes = new LinkedHashSet<SelectItem>();

    //Collections for Europeana tab
    private Set<SelectItem> typeSet = new LinkedHashSet<SelectItem>();
    private Set<SelectItem> yesNoSet = new LinkedHashSet<SelectItem>();
    private Set<SelectItem> inheritLanguageSet = new TreeSet<SelectItem>();
    private Set<SelectItem> languages = new TreeSet<SelectItem>();
    private Set<SelectItem> licenseSet = new LinkedHashSet<SelectItem>();
    private Set<SelectItem> europeanaLicenseSet = new LinkedHashSet<SelectItem>();

    //fields for basic tab components
    private String profilelist;
    private String profileName;
    private String associatedFiletype;
    private String uploadedFileAction;
    private String existingFileAction;
    private String noEadidAction;
    private String daoType;
    private String daoTypeCheck;

    //fields for Europeana tab components
    private String textDataProvider;
    private String dataProviderCheck;
    private String europeanaDaoType;
    private String europeanaDaoTypeCheck;
    private List<String> languageSelection = new ArrayList<String>();
    private String languageCheck;
    private String license;
    private String europeanaLicense;
    private String cc_js_result_uri;
    private String licenseAdditionalInformation;
    private String hierarchyPrefix;
    private String inheritFileParent;
    private String inheritOrigination;

    //other fields
    private static final Logger LOG = Logger.getLogger(IngestionprofilesAction.class);

    @Override
    public void validate() {
        if (this.getProfileName() == null || this.getProfileName().equals("")){
            addFieldError("profileName", "Please provide a profile name!");
        }
        if (this.getEuropeanaDaoType().equals("")){
            addFieldError("europeanaDaoType", "Please provide a DAO type for Europeana!");
        }
        if (this.getLanguageSelection().isEmpty()){
            addFieldError("languageSelection", "Please provide at least one language for Europeana!");
        }
    }

    @Override
    public String input() {
        setUp();

        IngestionprofileDAO profileDAO = DAOFactory.instance().getIngestionprofileDAO();
        List<Ingestionprofile> queryResult = profileDAO.getIngestionprofiles(getAiId());
        if (queryResult != null && !queryResult.isEmpty()) {
            for (Ingestionprofile entry : queryResult) {
                ingestionprofiles.add(new SelectItem(Long.toString(entry.getId()), entry.getNameProfile()));
            }
            if (profilelist == null) {
                profilelist = ingestionprofiles.iterator().next().getValue();
            }
        }

        if (StringUtils.isNotBlank(profilelist)) {
            Long profilelistLong = Long.parseLong(profilelist);
            Ingestionprofile ingestionprofile = profileDAO.findById(profilelistLong);
            profileName = ingestionprofile.getNameProfile();
            associatedFiletype = ingestionprofile.getFileType().toString();
            uploadedFileAction = Integer.toString(ingestionprofile.getUploadAction().getId());
            existingFileAction = Integer.toString(ingestionprofile.getExistAction().getId());
            noEadidAction = Integer.toString(ingestionprofile.getNoeadidAction().getId());
            daoType = Integer.toString(ingestionprofile.getDaoType().getId());
            daoTypeCheck = Boolean.toString(ingestionprofile.getDaoTypeFromFile());

            textDataProvider = ingestionprofile.getEuropeanaDataProvider();
            dataProviderCheck = Boolean.toString(ingestionprofile.getEuropeanaDataProviderFromFile());
            europeanaDaoType = Integer.toString(ingestionprofile.getEuropeanaDaoType());
            europeanaDaoTypeCheck = Boolean.toString(ingestionprofile.getEuropeanaDaoTypeFromFile());
            String[] tempLang = ingestionprofile.getEuropeanaLanguages().split(" ");
            languageSelection.addAll(Arrays.asList(tempLang));
            languageCheck = Boolean.toString(ingestionprofile.getEuropeanaLanguagesFromFile());
            license = ingestionprofile.getEuropeanaLicense();
            if (license.equals(EUROPEANA)) {
                europeanaLicense = ingestionprofile.getEuropeanaLicenseDetails();
            }
            if (license.equals(CREATIVECOMMONS)) {
                cc_js_result_uri = ingestionprofile.getEuropeanaLicenseDetails();
            }
            licenseAdditionalInformation = ingestionprofile.getEuropeanaAddRights();
            hierarchyPrefix = ingestionprofile.getEuropeanaHierarchyPrefix();
            inheritFileParent = Boolean.toString(ingestionprofile.getEuropeanaInheritElements());
            inheritOrigination = Boolean.toString(ingestionprofile.getEuropeanaInheritOrigin());
        }
        return SUCCESS;
    }

    @Override
    public String execute() throws Exception {
        IngestionprofileDAO profileDAO = DAOFactory.instance().getIngestionprofileDAO();
        Ingestionprofile profile;
        if (profilelist.equals("-1")) {
            profile = new Ingestionprofile(getAiId(), "", 1);
        } else {
            profile = profileDAO.findById(Long.parseLong(profilelist));
        }

        profile.setNameProfile(profileName);
        profile.setFileType(Integer.parseInt(associatedFiletype));
        profile.setUploadAction(IngestionprofileDefaultUploadAction.getUploadAction(uploadedFileAction));
        profile.setExistAction(IngestionprofileDefaultExistingFileAction.getExistingFileAction(existingFileAction));
        profile.setNoeadidAction(IngestionprofileDefaultNoEadidAction.getExistingFileAction(noEadidAction));
        profile.setDaoType(IngestionprofileDefaultDaoType.getDaoType(daoType));
        profile.setDaoTypeFromFile(Boolean.parseBoolean(daoTypeCheck));

        profile.setEuropeanaDataProvider(textDataProvider);
        profile.setEuropeanaDataProviderFromFile(Boolean.parseBoolean(dataProviderCheck));
        profile.setEuropeanaDaoType(Integer.parseInt(europeanaDaoType));
        profile.setEuropeanaDaoTypeFromFile(Boolean.parseBoolean(europeanaDaoTypeCheck));
        StringBuilder langTemp = new StringBuilder();
        for (int i=0; i < languageSelection.size(); i++) {
            langTemp.append(languageSelection.get(i));
            if (i < languageSelection.size() - 1) {
                langTemp.append(" ");
            }
        }
        profile.setEuropeanaLanguages(langTemp.toString());
        profile.setEuropeanaLanguagesFromFile(Boolean.parseBoolean(languageCheck));
        profile.setEuropeanaLicense(license);
        if (license.equals(EUROPEANA)) {
            profile.setEuropeanaLicenseDetails(europeanaLicense);
        }
        if (license.equals(CREATIVECOMMONS)) {
            profile.setEuropeanaLicenseDetails(cc_js_result_uri);
        }
        profile.setEuropeanaAddRights(licenseAdditionalInformation);
        profile.setEuropeanaHierarchyPrefix(hierarchyPrefix);
        profile.setEuropeanaInheritElements(Boolean.parseBoolean(inheritFileParent));
        profile.setEuropeanaInheritOrigin(Boolean.parseBoolean(inheritOrigination));
        if (profilelist.equals("-1")) {
            profileDAO.store(profile);
        } else {
            profileDAO.update(profile);
        }
        return SUCCESS;
    }

    public String addIngestionprofile() throws Exception {
        setUp();

        IngestionprofileDAO profileDAO = DAOFactory.instance().getIngestionprofileDAO();
        List<Ingestionprofile> queryResult = profileDAO.getIngestionprofiles(getAiId());
        if (queryResult != null && !queryResult.isEmpty()) {
            for (Ingestionprofile entry : queryResult) {
                ingestionprofiles.add(new SelectItem(Long.toString(entry.getId()), entry.getNameProfile()));
            }
        }
        ingestionprofiles.add(new SelectItem("-1", ""));
        profilelist = "-1";
        profileName = "";
        associatedFiletype = XmlType.EAD_FA.getIdentifier() + "";
        uploadedFileAction = "1";
        existingFileAction = "1";
        noEadidAction = "0";
        daoType = "0";
        daoTypeCheck = Boolean.toString(true);

        textDataProvider = getAiname();
        dataProviderCheck = Boolean.toString(true);
        europeanaDaoType = "";
        europeanaDaoTypeCheck = Boolean.toString(true);
        languageSelection = new ArrayList<String>();
        languageCheck = Boolean.toString(true);
        license = EUROPEANA;
        europeanaLicense = "";
        licenseAdditionalInformation = "";
        hierarchyPrefix = getText("ead2ese.content.hierarchy.prefix");
        inheritFileParent = Boolean.toString(false);
        inheritOrigination = Boolean.toString(false);

        return SUCCESS;
    }

    public String cancel() throws Exception {
        return SUCCESS;
    }

    private void setUp() {
        //basic preferences
        associatedFiletypes.add(new SelectItem(XmlType.EAD_FA.getIdentifier(), getText("content.message.fa")));
        associatedFiletypes.add(new SelectItem(XmlType.EAD_HG.getIdentifier(), getText("content.message.hg")));
        associatedFiletypes.add(new SelectItem(XmlType.EAD_SG.getIdentifier(), getText("content.message.sg")));
        uploadedFileActions.add(new SelectItem("1", getText("ingestionprofiles.upload.convertValidatePublish")));
        uploadedFileActions.add(new SelectItem("2", getText("ingestionprofiles.upload.convertValidatePublishEuropeana")));
        uploadedFileActions.add(new SelectItem("3", getText("ingestionprofiles.upload.convert")));
        uploadedFileActions.add(new SelectItem("4", getText("ingestionprofiles.upload.validate")));
        uploadedFileActions.add(new SelectItem("0", getText("ingestionprofiles.upload.nothing")));
        existingFileActions.add(new SelectItem("1", getText("ingestionprofiles.existing.overwrite")));
        existingFileActions.add(new SelectItem("0", getText("ingestionprofiles.existing.keep")));
        noEadidActions.add(new SelectItem("0", getText("ingestionprofiles.noeadid.remove")));
        noEadidActions.add(new SelectItem("1", getText("ingestionprofiles.noeadid.addLater")));
        daoTypes.add(new SelectItem("1", getText("ingestionprofiles.dao.text")));
        daoTypes.add(new SelectItem("2", getText("ingestionprofiles.dao.image")));
        daoTypes.add(new SelectItem("3", getText("ingestionprofiles.dao.sound")));
        daoTypes.add(new SelectItem("4", getText("ingestionprofiles.dao.video")));
        daoTypes.add(new SelectItem("5", getText("ingestionprofiles.dao.3D")));
        daoTypes.add(new SelectItem("0", getText("ingestionprofiles.dao.unspecified")));

        //Europeana preferences
        String[] isoLanguages = Locale.getISOLanguages();
        for (String language : isoLanguages) {
            String languageDescription = new Locale(language).getDisplayLanguage(Locale.ENGLISH);
            languages.add(new SelectItem(language, languageDescription));
        }
        typeSet.add(new SelectItem("", getText("ead2ese.content.selectone")));
        typeSet.add(new SelectItem("1", getText("ingestionprofiles.dao.text")));
        typeSet.add(new SelectItem("2", getText("ingestionprofiles.dao.image")));
        typeSet.add(new SelectItem("3", getText("ingestionprofiles.dao.sound")));
        typeSet.add(new SelectItem("4", getText("ingestionprofiles.dao.video")));
        typeSet.add(new SelectItem("5", getText("ingestionprofiles.dao.3D")));
        yesNoSet.add(new SelectItem("true", getText("ead2ese.content.yes")));
        yesNoSet.add(new SelectItem("false", getText("ead2ese.content.no")));
        inheritLanguageSet.add(new SelectItem("1", getText("ead2ese.content.yes")));
        inheritLanguageSet.add(new SelectItem("0", getText("ead2ese.content.no")));
        inheritLanguageSet.add(new SelectItem("2", getText("ead2ese.label.language.select")));
        licenseSet.add(new SelectItem(EUROPEANA, getText("ead2ese.content.license.europeana")));
        licenseSet.add(new SelectItem(CREATIVECOMMONS, getText("ead2ese.content.license.creativecommons")));
        licenseSet.add(new SelectItem(CREATIVECOMMONS_CC0, getText("ead2ese.content.license.creativecommons.cc0")));
        licenseSet.add(new SelectItem(CREATIVECOMMONS_CPDM, getText("ead2ese.content.license.creativecommons.publicdomain")));
        europeanaLicenseSet.add(new SelectItem("", getText("ead2ese.content.selectone")));
        europeanaLicenseSet.add(new SelectItem("http://www.europeana.eu/rights/rr-p/", getText("ead2ese.content.license.europeana.access.paid")));
        europeanaLicenseSet.add(new SelectItem("http://www.europeana.eu/rights/rr-f/", getText("ead2ese.content.license.europeana.access.free")));
        europeanaLicenseSet.add(new SelectItem("http://www.europeana.eu/rights/rr-r/", getText("ead2ese.content.license.europeana.access.restricted")));
        europeanaLicenseSet.add(new SelectItem("http://www.europeana.eu/rights/unknown/", getText("ead2ese.content.license.europeana.access.unknown")));
    }

    public Set<SelectItem> getIngestionprofiles() {
        return ingestionprofiles;
    }

    public void setIngestionprofiles(Set<SelectItem> ingestionprofiles) {
        this.ingestionprofiles = ingestionprofiles;
    }

    public String getProfilelist() {
        return profilelist;
    }

    public void setProfilelist(String profilelist) {
        this.profilelist = profilelist;
    }

    public Set<SelectItem> getAssociatedFiletypes() {
        return associatedFiletypes;
    }

    public void setAssociatedFiletypes(Set<SelectItem> associatedFiletypes) {
        this.associatedFiletypes = associatedFiletypes;
    }

    public Set<SelectItem> getUploadedFileActions() {
        return uploadedFileActions;
    }

    public void setUploadedFileActions(Set<SelectItem> uploadedFileActions) {
        this.uploadedFileActions = uploadedFileActions;
    }

    public Set<SelectItem> getExistingFileActions() {
        return existingFileActions;
    }

    public void setExistingFileActions(Set<SelectItem> existingFileActions) {
        this.existingFileActions = existingFileActions;
    }

    public Set<SelectItem> getNoEadidActions() {
        return noEadidActions;
    }

    public void setNoEadidActions(Set<SelectItem> noEadidActions) {
        this.noEadidActions = noEadidActions;
    }

    public Set<SelectItem> getDaoTypes() {
        return daoTypes;
    }

    public void setDaoTypes(Set<SelectItem> daoTypes) {
        this.daoTypes = daoTypes;
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

    public Set<SelectItem> getLanguages() {
        return languages;
    }

    public void setLanguages(Set<SelectItem> languages) {
        this.languages = languages;
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

    public String getHierarchyPrefix() {
        return hierarchyPrefix;
    }

    public void setHierarchyPrefix(String hierarchyPrefix) {
        this.hierarchyPrefix = hierarchyPrefix;
    }

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public String getAssociatedFiletype() {
        return associatedFiletype;
    }

    public void setAssociatedFiletype(String associatedFiletype) {
        this.associatedFiletype = associatedFiletype;
    }

    public String getUploadedFileAction() {
        return uploadedFileAction;
    }

    public void setUploadedFileAction(String uploadedFileAction) {
        this.uploadedFileAction = uploadedFileAction;
    }

    public String getExistingFileAction() {
        return existingFileAction;
    }

    public void setExistingFileAction(String existingFileAction) {
        this.existingFileAction = existingFileAction;
    }

    public String getNoEadidAction() {
        return noEadidAction;
    }

    public void setNoEadidAction(String noEadidAction) {
        this.noEadidAction = noEadidAction;
    }

    public String getDaoType() {
        return daoType;
    }

    public void setDaoType(String daoType) {
        this.daoType = daoType;
    }

    public String getDaoTypeCheck() {
        return daoTypeCheck;
    }

    public void setDaoTypeCheck(String daoTypeCheck) {
        this.daoTypeCheck = daoTypeCheck;
    }

    public String getTextDataProvider() {
        return textDataProvider;
    }

    public void setTextDataProvider(String textDataProvider) {
        this.textDataProvider = textDataProvider;
    }

    public String getDataProviderCheck() {
        return dataProviderCheck;
    }

    public void setDataProviderCheck(String dataProviderCheck) {
        this.dataProviderCheck = dataProviderCheck;
    }

    public String getEuropeanaDaoType() {
        return europeanaDaoType;
    }

    public void setEuropeanaDaoType(String europeanaDaoType) {
        this.europeanaDaoType = europeanaDaoType;
    }

    public String getEuropeanaDaoTypeCheck() {
        return europeanaDaoTypeCheck;
    }

    public void setEuropeanaDaoTypeCheck(String europeanaDaoTypeCheck) {
        this.europeanaDaoTypeCheck = europeanaDaoTypeCheck;
    }

    public List<String> getLanguageSelection() {
        return languageSelection;
    }

    public void setLanguageSelection(List<String> languageSelection) {
        this.languageSelection = languageSelection;
    }

    public String getLanguageCheck() {
        return languageCheck;
    }

    public void setLanguageCheck(String languageCheck) {
        this.languageCheck = languageCheck;
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

    public String getInheritFileParent() {
        return inheritFileParent;
    }

    public void setInheritFileParent(String inheritFileParent) {
        this.inheritFileParent = inheritFileParent;
    }

    public String getInheritOrigination() {
        return inheritOrigination;
    }

    public void setInheritOrigination(String inheritOrigination) {
        this.inheritOrigination = inheritOrigination;
    }

}
