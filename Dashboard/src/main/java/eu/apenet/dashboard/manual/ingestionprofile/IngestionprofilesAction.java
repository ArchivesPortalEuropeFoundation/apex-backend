/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.apenet.dashboard.manual.ingestionprofile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TreeSet;

import eu.apenet.persistence.vo.*;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.opensymphony.xwork2.Action;

import eu.apenet.commons.types.XmlType;
import eu.apenet.commons.view.jsp.SelectItem;
import eu.apenet.dashboard.AbstractInstitutionAction;
import eu.apenet.dashboard.actions.ajax.AjaxConversionOptionsConstants;
import eu.apenet.persistence.dao.IngestionprofileDAO;
import eu.apenet.persistence.factory.DAOFactory;

/**
 *
 * @author papp
 */
public class IngestionprofilesAction extends AbstractInstitutionAction {

    /**
     * Serializable.
     */
    private static final long serialVersionUID = 292033613637062110L;
    private static final String CREATIVECOMMONS_CPDM = "cpdm";
    private static final String CREATIVECOMMONS_CC0 = "cc0";
    private static final String CREATIVECOMMONS = "creativecommons";
    private static final String EUROPEANA = "europeana";
    private static final String INHERITLANGUAGE_PROVIDE = "provide";
    private static final String OUT_OF_COPYRIGHT = "outofcopyright";
    // Source of identifiers.
    public static final String OPTION_UNITID = "unitid";
    private static final String OPTION_CID = "cid";
    // Source of title for fonds.
    private static final String OPTION_ARCHDESC_UNITTITLE = "archdescUnittitle";
    private static final String OPTION_TITLESTMT_TITLEPROPER = "titlestmtTitleproper";

    //Collections for basic tab
    private Set<SelectItem> ingestionprofiles = new LinkedHashSet<SelectItem>();
    private Set<SelectItem> associatedFiletypes = new LinkedHashSet<SelectItem>();
    private Set<SelectItem> uploadedFileActions = new LinkedHashSet<SelectItem>();
    private Set<SelectItem> existingFileActions = new LinkedHashSet<SelectItem>();
    private Set<SelectItem> noEadidActions = new LinkedHashSet<SelectItem>();
    private Set<SelectItem> daoTypes = new LinkedHashSet<SelectItem>();
    private Set<SelectItem> rightsDigitalObjects = new LinkedHashSet<SelectItem>(); // Rights for digital objects.
    private Set<SelectItem> rightsEadData = new LinkedHashSet<SelectItem>(); // Rights for EAD data.
    private Set<SelectItem> xslFiles = new LinkedHashSet<SelectItem>();

    //Collections for Europeana tab
    private Set<SelectItem> conversiontypeSet = new LinkedHashSet<SelectItem>();
    private Set<SelectItem> sourceOfIdentifiersSet = new TreeSet<SelectItem>();
    private Set<SelectItem> sourceOfFondsTitleSet = new TreeSet<SelectItem>();
    private Set<SelectItem> typeSet = new TreeSet<SelectItem>();
    private Set<SelectItem> yesNoSet = new TreeSet<SelectItem>();
    private Set<SelectItem> inheritLanguageSet = new TreeSet<SelectItem>();
    private Set<SelectItem> languages = new TreeSet<SelectItem>();
    private Set<SelectItem> licenseSet = new TreeSet<SelectItem>();
    private Set<SelectItem> europeanaLicenseSet = new TreeSet<SelectItem>();

    //fields for basic tab components
    private String profilelist;
    private String profileName;
    private String associatedFiletype;
    private String uploadedFileAction;
    private String existingFileAction;
    private String noEadidAction;
    private String daoType;
    private String daoTypeCheck;
    private String rightDigitalObjects;
    private String rightDigitalDescription;
    private String rightDigitalHolder;
    private String rightEadData;
    private String rightEadDescription;
    private String rightEadHolder;
    private String defaultXslFile;

    //fields for Europeana tab components
    private String conversiontype;
    private String sourceOfIdentifiers;
    private String sourceOfFondsTitle;
    private String textDataProvider;
    private String dataProviderCheck;
    private String europeanaDaoType;
    private String europeanaDaoTypeCheck;
    private List<String> languageSelection = new ArrayList<String>();
    private String languageCheck;
    private String licenseCheck;
    private String license;
    private String europeanaLicense;
    private String cc_js_result_uri;
    private String licenseAdditionalInformation;
    private String inheritFileParentCheck;
    private String inheritFileParent;
    private String inheritOriginationCheck;
    private String inheritOrigination;
    private String inheritUnittitleCheck;
    private String inheritUnittitle;

    //other fields
    private static final Logger LOG = Logger.getLogger(IngestionprofilesAction.class);

    private String lastSelection; //See #1718 description

    @Override
    public String input() {
        IngestionprofileDAO profileDAO = DAOFactory.instance().getIngestionprofileDAO();
        List<Ingestionprofile> queryResult = profileDAO.getIngestionprofiles(getAiId());
        if (queryResult != null && !queryResult.isEmpty()) {
            for (Ingestionprofile entry : queryResult) {
                ingestionprofiles.add(new SelectItem(Long.toString(entry.getId()), entry.getNameProfile()));
            }
            if (profilelist == null) {
                if (this.lastSelection != null) {
                    this.profilelist = this.lastSelection;
                } else {
                    profilelist = ingestionprofiles.iterator().next().getValue();
                }
            } else {
                this.lastSelection = this.profilelist;
            }
        }

        if (StringUtils.isNotBlank(profilelist)) {

            Long profilelistLong = Long.parseLong(profilelist);
            Ingestionprofile ingestionprofile = profileDAO.findById(profilelistLong);
            if (ingestionprofile != null) {
                profileName = ingestionprofile.getNameProfile();
                associatedFiletype = ingestionprofile.getFileType().toString();
                if (!convertToString(XmlType.EAD_FA.getIdentifier()).equals(associatedFiletype)
                        && ingestionprofile.getUploadAction().getId() == 2) {
                    uploadedFileAction = "1";
                } else {
                    uploadedFileAction = Integer.toString(ingestionprofile.getUploadAction().getId());
                }
                existingFileAction = Integer.toString(ingestionprofile.getExistAction().getId());
                noEadidAction = Integer.toString(ingestionprofile.getNoeadidAction().getId());
                daoType = Integer.toString(ingestionprofile.getDaoType().getId());
                daoTypeCheck = Boolean.toString(ingestionprofile.getDaoTypeFromFile());
                // Rights for digital objects.
                this.setRightDigitalObjects(ingestionprofile.getRightsOfDigitalObjects());
                this.setRightDigitalDescription(ingestionprofile.getRightsOfDigitalDescription());
                this.setRightDigitalHolder(ingestionprofile.getRightsOfDigitalHolder());
                // Rights for EAD data.
                this.setRightEadData(ingestionprofile.getRightsOfEADData());
                this.setRightEadDescription(ingestionprofile.getRightsOfEADDescription());
                this.setRightEadHolder(ingestionprofile.getRightsOfEADHolder());

                conversiontype = Boolean.toString(ingestionprofile.getEuropeanaConversionType());
                if (conversiontype == null || conversiontype.isEmpty()) {
                    conversiontype = "true";
                }
                sourceOfIdentifiers = ingestionprofile.getSourceOfIdentifiers();
                if (sourceOfIdentifiers == null || sourceOfIdentifiers.isEmpty()) {
                    sourceOfIdentifiers = IngestionprofilesAction.OPTION_UNITID;
                }
                if (ingestionprofile.getUseArchdescUnittitle() == false) {
                    sourceOfFondsTitle = IngestionprofilesAction.OPTION_TITLESTMT_TITLEPROPER;
                } else {
                    sourceOfFondsTitle = IngestionprofilesAction.OPTION_ARCHDESC_UNITTITLE;
                }

                textDataProvider = ingestionprofile.getEuropeanaDataProvider();
                dataProviderCheck = Boolean.toString(ingestionprofile.getEuropeanaDataProviderFromFile());
                europeanaDaoType = Integer.toString(ingestionprofile.getEuropeanaDaoType());
                europeanaDaoTypeCheck = Boolean.toString(ingestionprofile.getEuropeanaDaoTypeFromFile());
                String[] tempLang = ingestionprofile.getEuropeanaLanguages().split(" ");
                languageSelection.addAll(Arrays.asList(tempLang));
                languageCheck = Boolean.toString(ingestionprofile.getEuropeanaLanguagesFromFile());
                licenseCheck = Boolean.toString(ingestionprofile.getEuropeanaLicenseFromFile());
                license = ingestionprofile.getEuropeanaLicense();
                if (license.equals(EUROPEANA)) {
                    europeanaLicense = ingestionprofile.getEuropeanaLicenseDetails();
                }
                if (license.equals(CREATIVECOMMONS)) {
                    cc_js_result_uri = ingestionprofile.getEuropeanaLicenseDetails();
                }
                licenseAdditionalInformation = ingestionprofile.getEuropeanaAddRights();
                inheritFileParentCheck = Boolean.toString(ingestionprofile.getEuropeanaInheritElementsCheck());
                inheritFileParent = Boolean.toString(ingestionprofile.getEuropeanaInheritElements());
                inheritOriginationCheck = Boolean.toString(ingestionprofile.getEuropeanaInheritOriginCheck());
                inheritOrigination = Boolean.toString(ingestionprofile.getEuropeanaInheritOrigin());
                inheritUnittitleCheck = Boolean.toString(ingestionprofile.getEuropeanaInheritUnittitleCheck());
                inheritUnittitle = Boolean.toString(ingestionprofile.getEuropeanaInheritUnittitle());
                if (ingestionprofile.getXslUploadId() != null) {
                    setDefaultXslFile(DAOFactory.instance().getXslUploadDAO().findById(ingestionprofile.getXslUploadId()).getId() + "");
                }
            }
        }
        setUp();
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
        // Only adds the rights options if file type is an EAD.
        if (profile.getFileType() != 2) {
            // Rights for digital objects.
            if (this.getRightDigitalObjects() != null
                    && !this.getRightDigitalObjects().isEmpty()
                    && !this.getRightDigitalObjects().equalsIgnoreCase(AjaxConversionOptionsConstants.NO_SELECTED)) {
                profile.setRightsOfDigitalObjects(this.getRightDigitalObjects());
                profile.setRightsOfDigitalObjectsText(this.recoverRightsStatementText(this.getRightDigitalObjects()));
                profile.setRightsOfDigitalDescription(this.getRightDigitalDescription());
                profile.setRightsOfDigitalHolder(this.getRightDigitalHolder());
            } else {
                profile.setRightsOfDigitalObjects("");
                profile.setRightsOfDigitalObjectsText("");
                profile.setRightsOfDigitalDescription("");
                profile.setRightsOfDigitalHolder("");
            }

            // Rights for EAD data.
            if (this.getRightEadData() != null
                    && !this.getRightEadData().isEmpty()
                    && !this.getRightEadData().equalsIgnoreCase(AjaxConversionOptionsConstants.NO_SELECTED)) {
                profile.setRightsOfEADData(this.getRightEadData());
                profile.setRightsOfEADDataText(this.recoverRightsStatementText(this.getRightEadData()));
                profile.setRightsOfEADDescription(this.getRightEadDescription());
                profile.setRightsOfEADHolder(this.getRightEadHolder());
            } else {
                profile.setRightsOfEADData("");
                profile.setRightsOfEADDataText("");
                profile.setRightsOfEADDescription("");
                profile.setRightsOfEADHolder("");
            }

            if (getDefaultXslFile() != null && !getDefaultXslFile().equals("-1")) {
                profile.setXslUploadId(Long.parseLong(getDefaultXslFile()));
            }
        } else {
            // Rights for digital objects.
            profile.setRightsOfDigitalObjects("");
            profile.setRightsOfDigitalObjectsText("");
            profile.setRightsOfDigitalDescription("");
            profile.setRightsOfDigitalHolder("");
            // Rights for EAD data.
            profile.setRightsOfEADData("");
            profile.setRightsOfEADDataText("");
            profile.setRightsOfEADDescription("");
            profile.setRightsOfEADHolder("");
        }

        profile.setEuropeanaConversionType(Boolean.parseBoolean(conversiontype));
        profile.setSourceOfIdentifiers(sourceOfIdentifiers);
        if (sourceOfFondsTitle.equals(IngestionprofilesAction.OPTION_TITLESTMT_TITLEPROPER)) {
            profile.setUseArchdescUnittitle(Boolean.FALSE);
        } else {
            profile.setUseArchdescUnittitle(Boolean.TRUE);
        }
        profile.setEuropeanaDataProvider(textDataProvider);
        profile.setEuropeanaDataProviderFromFile(Boolean.parseBoolean(dataProviderCheck));
        if (europeanaDaoType == null || europeanaDaoType.isEmpty()) {
            profile.setEuropeanaDaoType(0);
        } else {
            profile.setEuropeanaDaoType(Integer.parseInt(europeanaDaoType));
        }
        profile.setEuropeanaDaoTypeFromFile(Boolean.parseBoolean(europeanaDaoTypeCheck));
        StringBuilder langTemp = new StringBuilder("");
        for (int i = 0; i < languageSelection.size(); i++) {
            langTemp.append(languageSelection.get(i));
            if (i < languageSelection.size() - 1) {
                langTemp.append(" ");
            }
        }
        profile.setEuropeanaLanguages(langTemp.toString());
        profile.setEuropeanaLanguagesFromFile(Boolean.parseBoolean(languageCheck));
        profile.setEuropeanaLicenseFromFile(Boolean.parseBoolean(licenseCheck));
        profile.setEuropeanaLicense(license);
        if (IngestionprofilesAction.EUROPEANA.equals(this.license)) {
            profile.setEuropeanaLicenseDetails(europeanaLicense);
        } else if (IngestionprofilesAction.CREATIVECOMMONS.equals(this.license)) {
            profile.setEuropeanaLicenseDetails(cc_js_result_uri);
        } else if (IngestionprofilesAction.CREATIVECOMMONS_CC0.equals(this.license)) {
            profile.setEuropeanaLicenseDetails("http://creativecommons.org/publicdomain/zero/1.0/");
        } else if (IngestionprofilesAction.CREATIVECOMMONS_CPDM.equals(this.license)) {
            profile.setEuropeanaLicenseDetails("http://creativecommons.org/publicdomain/mark/1.0/");
        } else {
            profile.setEuropeanaLicenseDetails("http://www.europeana.eu/rights/out-of-copyright-non-commercial/");
        }
        profile.setEuropeanaAddRights(licenseAdditionalInformation);
        profile.setEuropeanaInheritElementsCheck(Boolean.parseBoolean(inheritFileParentCheck));
        profile.setEuropeanaInheritElements(Boolean.parseBoolean(inheritFileParent));
        profile.setEuropeanaInheritOriginCheck(Boolean.parseBoolean(inheritOriginationCheck));
        profile.setEuropeanaInheritOrigin(Boolean.parseBoolean(inheritOrigination));
        profile.setEuropeanaInheritUnittitleCheck(Boolean.parseBoolean(inheritUnittitleCheck));
        profile.setEuropeanaInheritUnittitle(Boolean.parseBoolean(inheritUnittitle));

        if (profilelist.equals("-1")) {
            profile = profileDAO.store(profile);
            this.lastSelection = profile.getId().toString();
            addActionMessage(getText("ingestionprofiles.profilessaved"));
        } else {
            profileDAO.update(profile);
            this.lastSelection = this.profilelist;
            addActionMessage(getText("ingestionprofiles.profilesupdated"));
        }
        return SUCCESS;
    }

    /**
     * Method to recover the rights statement text from the rights statement URL
     * selected.
     *
     * @param option_default_rights rights statement URL selected.
     *
     * @return Rights statement text.
     */
    private String recoverRightsStatementText(String option_default_rights) {
        String option_default_rights_text = null;

        // Check the URL selected
        if (AjaxConversionOptionsConstants.CREATIVECOMMONS_ATTRIBUTION.equalsIgnoreCase(option_default_rights)) {
            // Creative Commons Attribution.
            option_default_rights_text = getText("content.message.rights.creative.attribution");
        } else if (AjaxConversionOptionsConstants.CREATIVECOMMONS_ATTRIBUTION_NO_DERIVATES.equalsIgnoreCase(option_default_rights)) {
            // Creative Commons Attribution, No Derivatives.
            option_default_rights_text = getText("content.message.rights.creative.attribution.no.derivates");
        } else if (AjaxConversionOptionsConstants.CREATIVECOMMONS_ATTRIBUTION_NON_COMERCIAL.equalsIgnoreCase(option_default_rights)) {
            // Creative Commons Attribution, Non-Commercial.
            option_default_rights_text = getText("content.message.rights.creative.attribution.non.commercial");
        } else if (AjaxConversionOptionsConstants.CREATIVECOMMONS_ATTRIBUTION_NC_NO_DERIVATES.equalsIgnoreCase(option_default_rights)) {
            // Creative Commons Attribution, Non-Commercial, No Derivatives.
            option_default_rights_text = getText("content.message.rights.creative.attribution.non.commercial.no.derivates");
        } else if (AjaxConversionOptionsConstants.CREATIVECOMMONS_ATTRIBUTION_NC_SHARE.equalsIgnoreCase(option_default_rights)) {
            // Creative Commons Attribution, Non-Commercial, ShareAlike.
            option_default_rights_text = getText("content.message.rights.creative.attribution.non.commercial.sharealike");
        } else if (AjaxConversionOptionsConstants.CREATIVECOMMONS_ATTRIBUTION_SHARE.equalsIgnoreCase(option_default_rights)) {
            // Creative Commons Attribution, ShareAlike .
            option_default_rights_text = getText("content.message.rights.creative.attribution.sharealike");
        } else if (AjaxConversionOptionsConstants.CREATIVECOMMONS_CC0_PUBLIC.equalsIgnoreCase(option_default_rights)) {
            //  Creative Commons CC0 Public Domain Dedication .
            option_default_rights_text = getText("content.message.rights.creative.public.domain");
        } else if (AjaxConversionOptionsConstants.FREE_ACCESS_NO_REUSE.equalsIgnoreCase(option_default_rights)) {
            // Free access – no re-use.
            option_default_rights_text = getText("ead2ese.content.license.europeana.access.free");
        } else if (AjaxConversionOptionsConstants.ORPHAN_WORKS.equalsIgnoreCase(option_default_rights)) {
            // Orphan works.
            option_default_rights_text = getText("ead2ese.content.license.europeana.access.orphan");
        } else if (AjaxConversionOptionsConstants.OUT_OF_COPYRIGHT.equalsIgnoreCase(option_default_rights)) {
            // Out of copyright - no commercial re-use.
            option_default_rights_text = getText("ead2ese.content.license.out.of.copyright");
        } else if (AjaxConversionOptionsConstants.PAID_ACCESS_NO_REUSE.equalsIgnoreCase(option_default_rights)) {
            // Paid access – no re-use.
            option_default_rights_text = getText("ead2ese.content.license.europeana.access.paid");
        } else if (AjaxConversionOptionsConstants.PUBLIC_DOMAIN_MARK.equalsIgnoreCase(option_default_rights)) {
            // Public Domain Mark.
            option_default_rights_text = getText("content.message.rights.public.domain");
        } else {
            // Unknown.
            option_default_rights_text = getText("content.message.rights.unknown");
        }

        return option_default_rights_text;
    }

    public String addIngestionprofile() throws Exception {
        setUp();

        IngestionprofileDAO profileDAO = DAOFactory.instance().getIngestionprofileDAO();
        List<Ingestionprofile> queryResult = profileDAO.getIngestionprofiles(getAiId());
        if (queryResult != null && !queryResult.isEmpty()) {
            for (Ingestionprofile entry : queryResult) {
                ingestionprofiles.add(new SelectItem(Long.toString(entry.getId()), entry.getNameProfile()));
            }
        } else {
            uploadedFileActions.clear();
            uploadedFileActions.add(new SelectItem("1", getText("ingestionprofiles.upload.convertValidatePublish")));
            uploadedFileActions.add(new SelectItem("2", getText("ingestionprofiles.upload.convertValidatePublishEuropeana")));
            uploadedFileActions.add(new SelectItem("3", getText("ingestionprofiles.upload.convert")));
            uploadedFileActions.add(new SelectItem("4", getText("ingestionprofiles.upload.validate")));
            uploadedFileActions.add(new SelectItem("0", getText("ingestionprofiles.upload.nothing")));

        }
        ingestionprofiles.add(new SelectItem("-1", ""));

        profilelist = "-1";
        profileName = "";
        associatedFiletype = convertToString(XmlType.EAD_FA.getIdentifier());
        uploadedFileAction = "1";
        existingFileAction = "1";
        noEadidAction = "0";
        daoType = "0";
        daoTypeCheck = Boolean.toString(true);
        // Rights for digital objects.
        this.setRightDigitalObjects(AjaxConversionOptionsConstants.NO_SELECTED);
        this.setRightDigitalDescription("");
        this.setRightDigitalHolder("");
        // Rights for EAD data.
        this.setRightEadData(AjaxConversionOptionsConstants.NO_SELECTED);
        this.setRightEadDescription("");
        this.setRightEadHolder("");

        conversiontype = "true";
        sourceOfIdentifiers = IngestionprofilesAction.OPTION_UNITID;
        sourceOfFondsTitle = IngestionprofilesAction.OPTION_ARCHDESC_UNITTITLE;
        textDataProvider = getAiname();
        dataProviderCheck = Boolean.toString(true);
        europeanaDaoType = "";
        europeanaDaoTypeCheck = Boolean.toString(true);
        languageSelection = new ArrayList<String>();
        languageCheck = Boolean.toString(true);
        licenseCheck = Boolean.toString(true);
        license = EUROPEANA;
        europeanaLicense = "";
        licenseAdditionalInformation = "";
        inheritFileParentCheck = Boolean.toString(true);
        inheritFileParent = Boolean.toString(false);
        inheritOriginationCheck = Boolean.toString(true);
        inheritOrigination = Boolean.toString(false);
        inheritUnittitleCheck = Boolean.toString(true);
        inheritUnittitle = Boolean.toString(false);

        this.lastSelection = this.profilelist;

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
        associatedFiletypes.add(new SelectItem(XmlType.EAC_CPF.getIdentifier(), getText("content.message.ec")));
//        Hide Ead3
        associatedFiletypes.add(new SelectItem(XmlType.EAD_3.getIdentifier(), getText("content.message.ead3")));
        uploadedFileActions.add(new SelectItem("1", getText("ingestionprofiles.upload.convertValidatePublish")));
        if (convertToString(XmlType.EAD_FA.getIdentifier()).equals(associatedFiletype)) {
            uploadedFileActions.add(new SelectItem("2", getText("ingestionprofiles.upload.convertValidatePublishEuropeana")));
        }
        uploadedFileActions.add(new SelectItem("3", getText("ingestionprofiles.upload.convert")));
        uploadedFileActions.add(new SelectItem("4", getText("ingestionprofiles.upload.validate")));
        uploadedFileActions.add(new SelectItem("0", getText("ingestionprofiles.upload.nothing")));
        existingFileActions.add(new SelectItem("1", getText("ingestionprofiles.existing.overwrite")));
        existingFileActions.add(new SelectItem("0", getText("ingestionprofiles.existing.keep")));
        existingFileActions.add(new SelectItem("2", getText("ingestionprofiles.existing.ask")));
        noEadidActions.add(new SelectItem("0", getText("ingestionprofiles.noeadid.remove")));
        noEadidActions.add(new SelectItem("1", getText("ingestionprofiles.noeadid.addLater")));
        daoTypes.add(new SelectItem("1", getText("ingestionprofiles.dao.text")));
        daoTypes.add(new SelectItem("2", getText("ingestionprofiles.dao.image")));
        daoTypes.add(new SelectItem("3", getText("ingestionprofiles.dao.sound")));
        daoTypes.add(new SelectItem("4", getText("ingestionprofiles.dao.video")));
        daoTypes.add(new SelectItem("5", getText("ingestionprofiles.dao.3D")));
        daoTypes.add(new SelectItem("0", getText("ingestionprofiles.dao.unspecified")));

        // Set of available permissions.
        Set<SelectItem> rightsSet = this.addRightsOptions();
        // Rights for digital objects.
        this.getRightsDigitalObjects().addAll(rightsSet);
        // Rights for EAD data.
        this.getRightsEadData().addAll(rightsSet);

        //Europeana preferences
        conversiontypeSet.add(new SelectItem("true", this.getText("ead2ese.label.minimal.conversion")));
        conversiontypeSet.add(new SelectItem("false", this.getText("ead2ese.label.full.conversion")));
        sourceOfIdentifiersSet.add(new SelectItem(IngestionprofilesAction.OPTION_UNITID, this.getText("ead2ese.label.id.unitid").replaceAll(">", "&#62;").replaceAll("<", "&#60;")));
        sourceOfIdentifiersSet.add(new SelectItem(IngestionprofilesAction.OPTION_CID, this.getText("ead2ese.label.id.c").replaceAll(">", "&#62;").replaceAll("<", "&#60;")));
        sourceOfFondsTitleSet.add(new SelectItem(IngestionprofilesAction.OPTION_ARCHDESC_UNITTITLE, this.getText("ead2ese.label.fondstitle.archdescUnittitle").replaceAll(">", "&#62;").replaceAll("<", "&#60;")));
        sourceOfFondsTitleSet.add(new SelectItem(IngestionprofilesAction.OPTION_TITLESTMT_TITLEPROPER, this.getText("ead2ese.label.fondstitle.titlestmtTitleproper").replaceAll(">", "&#62;").replaceAll("<", "&#60;")));
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
        europeanaLicenseSet.add(new SelectItem("http://rightsstatements.org/vocab/InC/1.0/", getText("ead2ese.content.license.europeana.incopyright")));
        europeanaLicenseSet.add(new SelectItem("http://rightsstatements.org/vocab/InC-EDU/1.0/", getText("ead2ese.content.license.europeana.incopyright.eduuse")));
        europeanaLicenseSet.add(new SelectItem("http://rightsstatements.org/vocab/InC-OW-EU/1.0/", getText("ead2ese.content.license.europeana.incopyright.euorphan")));
        europeanaLicenseSet.add(new SelectItem("http://rightsstatements.org/vocab/NoC-NC/1.0/", getText("ead2ese.content.license.europeana.nocopyright.noncommercial")));
        europeanaLicenseSet.add(new SelectItem("http://rightsstatements.org/vocab/NoC-OKLR/1.0/", getText("ead2ese.content.license.europeana.nocopyright.otherlegal")));
        europeanaLicenseSet.add(new SelectItem("http://rightsstatements.org/vocab/CNE/1.0/", getText("ead2ese.content.license.europeana.copyrightnotevaluated")));

        if (DAOFactory.instance().getXslUploadDAO().hasXslUpload(getAiId())) {
            List<XslUpload> xslUploads = DAOFactory.instance().getXslUploadDAO().getXslUploads(getAiId());
            getXslFiles().add(new SelectItem("-1", "DEFAULT"));
            for (XslUpload xslUpload : xslUploads) {
                getXslFiles().add(new SelectItem(xslUpload.getId(), xslUpload.getReadableName()));
            }
        }
    }

    /**
     * Method to create the set with all the available rights statements for EAD
     * data.
     *
     * @return Set with all the available rights statements for EAD data.
     */
    private Set<SelectItem> addRightsOptions() {
        Set<SelectItem> rightsSet = new LinkedHashSet<SelectItem>();
        rightsSet.add(new SelectItem(AjaxConversionOptionsConstants.NO_SELECTED, "---"));
        rightsSet.add(new SelectItem(AjaxConversionOptionsConstants.PUBLIC_DOMAIN_MARK, getText("content.message.rights.public.domain")));
        rightsSet.add(new SelectItem(AjaxConversionOptionsConstants.OUT_OF_COPYRIGHT, getText("ead2ese.content.license.out.of.copyright")));
        rightsSet.add(new SelectItem(AjaxConversionOptionsConstants.CREATIVECOMMONS_CC0_PUBLIC, getText("content.message.rights.creative.public.domain")));
        rightsSet.add(new SelectItem(AjaxConversionOptionsConstants.CREATIVECOMMONS_ATTRIBUTION, getText("content.message.rights.creative.attribution")));
        rightsSet.add(new SelectItem(AjaxConversionOptionsConstants.CREATIVECOMMONS_ATTRIBUTION_SHARE, getText("content.message.rights.creative.attribution.sharealike")));
        rightsSet.add(new SelectItem(AjaxConversionOptionsConstants.CREATIVECOMMONS_ATTRIBUTION_NO_DERIVATES, getText("content.message.rights.creative.attribution.no.derivates")));
        rightsSet.add(new SelectItem(AjaxConversionOptionsConstants.CREATIVECOMMONS_ATTRIBUTION_NON_COMERCIAL, getText("content.message.rights.creative.attribution.non.commercial")));
        rightsSet.add(new SelectItem(AjaxConversionOptionsConstants.CREATIVECOMMONS_ATTRIBUTION_NC_SHARE, getText("content.message.rights.creative.attribution.non.commercial.sharealike")));
        rightsSet.add(new SelectItem(AjaxConversionOptionsConstants.CREATIVECOMMONS_ATTRIBUTION_NC_NO_DERIVATES, getText("content.message.rights.creative.attribution.non.commercial.no.derivates")));
        rightsSet.add(new SelectItem(AjaxConversionOptionsConstants.FREE_ACCESS_NO_REUSE, getText("ead2ese.content.license.europeana.access.free")));
        rightsSet.add(new SelectItem(AjaxConversionOptionsConstants.PAID_ACCESS_NO_REUSE, getText("ead2ese.content.license.europeana.access.paid")));
        rightsSet.add(new SelectItem(AjaxConversionOptionsConstants.ORPHAN_WORKS, getText("ead2ese.content.license.europeana.access.orphan")));
        rightsSet.add(new SelectItem(AjaxConversionOptionsConstants.UNKNOWN, getText("content.message.rights.unknown")));

        return rightsSet;
    }

    public String showIngestionProfiles() {
        return Action.SUCCESS;
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

    /**
     * @return the rightsDigitalObjects
     */
    public Set<SelectItem> getRightsDigitalObjects() {
        return this.rightsDigitalObjects;
    }

    /**
     * @param rightsDigitalObjects the rightsDigitalObjects to set
     */
    public void setRightsDigitalObjects(Set<SelectItem> rightsDigitalObjects) {
        this.rightsDigitalObjects = rightsDigitalObjects;
    }

    /**
     * @return the rightsEadData
     */
    public Set<SelectItem> getRightsEadData() {
        return this.rightsEadData;
    }

    /**
     * @param rightsEadData the rightsEadData to set
     */
    public void setRightsEadData(Set<SelectItem> rightsEadData) {
        this.rightsEadData = rightsEadData;
    }

    public Set<SelectItem> getConversiontypeSet() {
        return conversiontypeSet;
    }

    public void setConversiontypeSet(Set<SelectItem> conversiontypeSet) {
        this.conversiontypeSet = conversiontypeSet;
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

    /**
     * @return the rightDigitalObjects
     */
    public String getRightDigitalObjects() {
        return this.rightDigitalObjects;
    }

    /**
     * @param rightDigitalObjects the rightDigitalObjects to set
     */
    public void setRightDigitalObjects(String rightDigitalObjects) {
        this.rightDigitalObjects = rightDigitalObjects;
    }

    /**
     * @return the rightDigitalDescription
     */
    public String getRightDigitalDescription() {
        return this.rightDigitalDescription;
    }

    /**
     * @param rightDigitalDescription the rightDigitalDescription to set
     */
    public void setRightDigitalDescription(String rightDigitalDescription) {
        this.rightDigitalDescription = rightDigitalDescription;
    }

    /**
     * @return the rightDigitalHolder
     */
    public String getRightDigitalHolder() {
        return this.rightDigitalHolder;
    }

    /**
     * @param rightDigitalHolder the rightDigitalHolder to set
     */
    public void setRightDigitalHolder(String rightDigitalHolder) {
        this.rightDigitalHolder = rightDigitalHolder;
    }

    /**
     * @return the rightEadData
     */
    public String getRightEadData() {
        return this.rightEadData;
    }

    /**
     * @param rightEadData the rightEadData to set
     */
    public void setRightEadData(String rightEadData) {
        this.rightEadData = rightEadData;
    }

    /**
     * @return the rightEadDescription
     */
    public String getRightEadDescription() {
        return this.rightEadDescription;
    }

    /**
     * @param rightEadDescription the rightEadDescription to set
     */
    public void setRightEadDescription(String rightEadDescription) {
        this.rightEadDescription = rightEadDescription;
    }

    /**
     * @return the rightEadHolder
     */
    public String getRightEadHolder() {
        return this.rightEadHolder;
    }

    /**
     * @param rightEadHolder the rightEadHolder to set
     */
    public void setRightEadHolder(String rightEadHolder) {
        this.rightEadHolder = rightEadHolder;
    }

    public String getConversiontype() {
        return conversiontype;
    }

    public void setConversiontype(String conversiontype) {
        this.conversiontype = conversiontype;
    }

    /**
     * @return the sourceOfIdentifiers
     */
    public String getSourceOfIdentifiers() {
        return this.sourceOfIdentifiers;
    }

    /**
     * @param sourceOfIdentifiers the sourceOfIdentifiers to set
     */
    public void setSourceOfIdentifiers(String sourceOfIdentifiers) {
        this.sourceOfIdentifiers = sourceOfIdentifiers;
    }

    public String getSourceOfFondsTitle() {
        return sourceOfFondsTitle;
    }

    public void setSourceOfFondsTitle(String sourceOfFondsTitle) {
        this.sourceOfFondsTitle = sourceOfFondsTitle;
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

    public String getLicenseCheck() {
        return licenseCheck;
    }

    public void setLicenseCheck(String licenseCheck) {
        this.licenseCheck = licenseCheck;
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

    public String getInheritFileParentCheck() {
        return inheritFileParentCheck;
    }

    public void setInheritFileParentCheck(String inheritFileParentCheck) {
        this.inheritFileParentCheck = inheritFileParentCheck;
    }

    public String getInheritFileParent() {
        return inheritFileParent;
    }

    public void setInheritFileParent(String inheritFileParent) {
        this.inheritFileParent = inheritFileParent;
    }

    public String getInheritOriginationCheck() {
        return inheritOriginationCheck;
    }

    public void setInheritOriginationCheck(String inheritOriginationCheck) {
        this.inheritOriginationCheck = inheritOriginationCheck;
    }

    public String getInheritOrigination() {
        return inheritOrigination;
    }

    public void setInheritOrigination(String inheritOrigination) {
        this.inheritOrigination = inheritOrigination;
    }

    public String getInheritUnittitleCheck() {
        return inheritUnittitleCheck;
    }

    public void setInheritUnittitleCheck(String inheritUnittitleCheck) {
        this.inheritUnittitleCheck = inheritUnittitleCheck;
    }

    public String getInheritUnittitle() {
        return inheritUnittitle;
    }

    public void setInheritUnittitle(String inheritUnittitle) {
        this.inheritUnittitle = inheritUnittitle;
    }

    public String getLastSelection() {
        return lastSelection;
    }

    public void setLastSelection(String lastSelection) {
        this.lastSelection = lastSelection;
    }

    public Set<SelectItem> getXslFiles() {
        return xslFiles;
    }

    public void setXslFiles(Set<SelectItem> xslFiles) {
        this.xslFiles = xslFiles;
    }

    public String getDefaultXslFile() {
        return defaultXslFile;
    }

    public void setDefaultXslFile(String defaultXslFile) {
        this.defaultXslFile = defaultXslFile;
    }

    private String convertToString(int identifier) {
        return identifier + "";
    }
}
