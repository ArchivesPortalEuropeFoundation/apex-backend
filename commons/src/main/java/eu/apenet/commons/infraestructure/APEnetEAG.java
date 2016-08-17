package eu.apenet.commons.infraestructure;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.XMLEvent;

import org.apache.log4j.Logger;

import eu.apenet.commons.ResourceBundleSource;
import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.commons.utils.XMLUtils;
import eu.apenet.persistence.dao.ArchivalInstitutionDAO;
import eu.apenet.persistence.dao.ContentSearchOptions;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.ArchivalInstitution;
import eu.apenet.persistence.vo.Ead;
import eu.apenet.persistence.vo.HoldingsGuide;

/**
 * This class and its methods has been imported to the Portal from Dashboard
 * done by Eloy
 *
 * User: Eloy García Date: Sep 23d, 2010
 */
/**
 * This class is in charge of managing each EAG file uploaded or edited by a
 * partner. This class gathers all the operations a partner can do within the
 * Dashboard Content Provider Information option
 */
public class APEnetEAG {

    //Constants
    protected static final String REPOSITORGUIDE_LOCAL_RESOURCE = "2";
    protected static final String REPOSITORGUIDE_EXTERNAL_RESOURCE = "1";
    protected static final String PORTAL_CONTEXT = "/Portal";
    protected static final String SECOND_DISPLAY_PREVIEW_ACTION = "/SecondDisplay.action";
    protected static final String NO_URL = "No URL";
    protected static final String SEPARATOR = ";";
    protected static final String START_ATTRIBUTE = "[";
    protected static final String END_ATTRIBUTE = "]";
    protected static final String HTTP = "http://";
    protected static final String HTTPS = "https://";

    protected static final String ROOT = "eag";
    protected static final String AUTFORM_TAG = "archguide/identity/autform";
    protected static final String PARFORM_TAG = "archguide/identity/parform";
    protected static final String EADID_TAG = "eagheader/eagid";
    protected static final String SURNAMES_TAG = "eagheader/mainhist/mainevent/respevent/surnames";
    protected static final String FIRSTNAME_TAG = "eagheader/mainhist/mainevent/respevent/firstname";
    protected static final String COUNTRY_TAG = "archguide/desc/country";
    protected static final String MUNICIPALITY_TAG = "archguide/desc/municipality";
    protected static final String POSTALCODE_TAG = "archguide/desc/postalcode";
    protected static final String STREET_TAG = "archguide/desc/street";
    protected static final String TELEPHONE_TAG = "archguide/desc/telephone";
    protected static final String EMAIL_TAG = "archguide/desc/email";
    protected static final String WEBPAGE_TAG = "archguide/desc/webpage";
    protected static final String ACCESS_TAG = "archguide/desc/access";
    protected static final String HANDICAPPED_TAG = "archguide/desc/buildinginfo/handicapped";
    protected static final String SEARCHROOM_NUM_TAG = "archguide/desc/buildinginfo/searchroom/num";
    protected static final String LIBRARY_TAG = "archguide/desc/techservices/library";
    protected static final String RESTORATIONLAB_TAG = "archguide/desc/techservices/restorationlab";
    protected static final String REPRODUCTIONSER_TAG = "archguide/desc/techservices/reproductionser";
    protected static final String AUTOMATION_TAG = "archguide/desc/automation";
    protected static final String EXTENT_NUM_TAG = "archguide/desc/extent/num";
    protected static final String DATE_TAG = "eagheader/mainhist/mainevent/date";
    protected static final String MAINEVENT_TAG = "eagheader/mainhist/mainevent";

    protected static final String HREF_ATTRIBUTE = "href";
    protected static final String QUESTION_ATTRIBUTE = "question";
    protected static final String NORMAL_ATTRIBUTE = "normal";
    protected static final String MAINTYPE_ATTRIBUTE = "maintype";
    protected static final String NULL_ATTRIBUTE = "";

    //Attributes
    protected final Logger log = Logger.getLogger(getClass());

    protected Integer aiId;
    private String eagPath;
    protected String name;
    protected Boolean hasChangedName;
    protected String englishName;
    protected Boolean hasChangedEnglishName;
    private String id;
    protected Boolean hasChangedId;
    protected String responsiblePersonSurname;
    protected String responsiblePersonName;
    protected String country;
    protected Boolean hasChangedCountry;
    protected String cityTown;
    protected Boolean hasChangedCityTown;
    protected String postalCode;
    protected Boolean hasChangedPostalCode;
    protected String street;
    protected Boolean hasChangedStreet;
    protected String telephone;
    protected Boolean hasChangedTelephone;
    protected String emailAddress;
    protected Boolean hasChangedEmailAddress;
    protected String webPage;
    protected Boolean hasChangedWebPage;
    protected String fax;
    protected String firstdem; //Region data
    protected String secondem; //Region data
    protected String localentity;
    protected String opening;
    protected String openingNum;
    protected String closing;
    protected String access; //Access data
    protected Boolean hasChangedAccess;
    protected String restaccess; //Access data
    protected String accesibility;
    protected String readingRoom;
    protected String repositoryguide;
    protected String repositoryguideLink;
    protected String library;
    protected Boolean hasChangedLibrary;
    protected String libraryMonographic;
    protected String librarySerie;
    protected String restorationlab;
    protected String reproductionser;
    protected String photocopyser;
    protected String microformser;
    protected String photographser;
    protected String digitalser;
    protected String automation;
    protected Boolean hasChangedAutomation;
    protected String autusermanag;
    protected String autdescription;
    protected String indexvoc;
    protected String odautomationP;
    protected String repositorhistP;
    protected String date;
    protected String rule;
    protected String firstnameResp;
    protected String surnamesResp;
    protected String chargeResp;
    protected String adminunit;
    protected String building;
    protected String repositorareaNum;
    protected String lengthshelfNum;
    protected String extentNum;
    protected String notes;
    protected String dateCreated;
    protected String dateUpdated;
    protected List<String> warnings_ead;
    protected List<Ead> holdingsGuideIndexed;
    protected List<String> repositorguideInformation;
    protected List<String> repositorguideURL;
    protected List<String> repositorguideResource;
    protected List<String> repositorguidePossibleHGTitle;
    protected List<String> localHGSelected;
    protected Boolean eagEmptyInformation;
    private ResourceBundleSource resourceBundleSource;
    // Getters & Setters

    public List<String> getWarnings_ead() {
        return warnings_ead;
    }

    public void setWarnings_ead(List<String> warnings_ead) {
        this.warnings_ead = warnings_ead;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getDateUpdated() {
        return dateUpdated;
    }

    public void setDateUpdated(String dateUpdated) {
        this.dateUpdated = dateUpdated;
    }

    public String getExtentNum() {
        return extentNum;
    }

    public void setExtentNum(String extentNum) {
        this.extentNum = extentNum;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public String getRepositorareaNum() {
        return repositorareaNum;
    }

    public void setRepositorareaNum(String repositorareaNum) {
        this.repositorareaNum = repositorareaNum;
    }

    public String getLengthshelfNum() {
        return lengthshelfNum;
    }

    public void setLengthshelfNum(String lengthshelfNum) {
        this.lengthshelfNum = lengthshelfNum;
    }

    public String getAdminunit() {
        return adminunit;
    }

    public void setAdminunit(String adminunit) {
        this.adminunit = adminunit;
    }

    public String getFirstnameResp() {
        return firstnameResp;
    }

    public void setFirstnameResp(String firstnameResp) {
        this.firstnameResp = firstnameResp;
    }

    public String getSurnamesResp() {
        return surnamesResp;
    }

    public void setSurnamesResp(String surnamesResp) {
        this.surnamesResp = surnamesResp;
    }

    public String getChargeResp() {
        return chargeResp;
    }

    public void setChargeResp(String chargeResp) {
        this.chargeResp = chargeResp;
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getRepositorhistP() {
        return repositorhistP;
    }

    public void setRepositorhistP(String repositorhistP) {
        this.repositorhistP = repositorhistP;
    }

    public String getAutusermanag() {
        return autusermanag;
    }

    public void setAutusermanag(String autusermanag) {
        this.autusermanag = autusermanag;
    }

    public String getAutdescription() {
        return autdescription;
    }

    public void setAutdescription(String autdescription) {
        this.autdescription = autdescription;
    }

    public String getIndexvoc() {
        return indexvoc;
    }

    public void setIndexvoc(String indexvoc) {
        this.indexvoc = indexvoc;
    }

    public String getOdautomationP() {
        return odautomationP;
    }

    public void setOdautomationP(String odautomationP) {
        this.odautomationP = odautomationP;
    }

    public String getAutomation() {
        return automation;
    }

    public void setAutomation(String automation) {

        if (!this.automation.equals(automation)) {
            this.hasChangedAutomation = true;
        }

        this.automation = automation;
    }

    public String getPhotocopyser() {
        return photocopyser;
    }

    public void setPhotocopyser(String photocopyser) {
        this.photocopyser = photocopyser;
    }

    public String getMicroformser() {
        return microformser;
    }

    public void setMicroformser(String microformser) {
        this.microformser = microformser;
    }

    public String getPhotographser() {
        return photographser;
    }

    public void setPhotographser(String photographser) {
        this.photographser = photographser;
    }

    public String getDigitalser() {
        return digitalser;
    }

    public void setDigitalser(String digitalser) {
        this.digitalser = digitalser;
    }

    public String getReproductionser() {
        return reproductionser;
    }

    public void setReproductionser(String reproductionser) {
        this.reproductionser = reproductionser;
    }

    public String getLibraryMonographic() {
        return libraryMonographic;
    }

    public void setLibraryMonographic(String libraryMonographic) {
        this.libraryMonographic = libraryMonographic;
    }

    public String getLibrarySerie() {
        return librarySerie;
    }

    public void setLibrarySerie(String librarySerie) {
        this.librarySerie = librarySerie;
    }

    public String getRestorationlab() {
        return restorationlab;
    }

    public void setRestorationlab(String restorationlab) {
        this.restorationlab = restorationlab;
    }

    public String getLibrary() {
        return library;
    }

    public void setLibrary(String library) {

        if (!this.library.equals(library)) {
            this.hasChangedLibrary = true;
        }

        this.library = library;
    }

    public String getRepositoryguideLink() {
        return repositoryguideLink;
    }

    public void setRepositoryguideLink(String repositoryguideLink) {
        this.repositoryguideLink = repositoryguideLink;
    }

    public String getRepositoryguide() {
        return repositoryguide;
    }

    public void setRepositoryguide(String repositoryguide) {
        this.repositoryguide = repositoryguide;
    }

    public String getReadingRoom() {
        return readingRoom;
    }

    public void setReadingRoom(String readingRoom) {
        this.readingRoom = readingRoom;
    }

    public String getAccesibility() {
        return accesibility;
    }

    public void setAccesibility(String accesibility) {
        this.accesibility = accesibility;
    }

    public String getAccess() {
        return access;
    }

    public void setAccess(String access) {

        if (!this.access.equals(access)) {
            this.hasChangedAccess = true;
        }

        this.access = access;
    }

    public String getRestaccess() {
        return restaccess;
    }

    public void setRestaccess(String restaccess) {
        this.restaccess = restaccess;
    }

    public String getClosing() {
        return closing;
    }

    public void setClosing(String closing) {
        this.closing = closing;
    }

    public String getOpening() {
        return opening;
    }

    public void setOpening(String opening) {
        this.opening = opening;
    }

    public String getOpeningNum() {
        return openingNum;
    }

    public void setOpeningNum(String openingNum) {
        this.openingNum = openingNum;
    }

    public String getLocalentity() {
        return localentity;
    }

    public void setLocalentity(String localentity) {
        this.localentity = localentity;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getFirstdem() {
        return firstdem;
    }

    public void setFirstdem(String firstdem) {
        this.firstdem = firstdem;
    }

    public String getSecondem() {
        return secondem;
    }

    public void setSecondem(String secondem) {
        this.secondem = secondem;
    }

    public Integer getAiId() {
        return aiId;
    }

    public void setAiId(Integer aiId) {
        this.aiId = aiId;
    }

    public String getEagPath() {
        return eagPath;
    }

    public void setEagPath(String eagPath) {
        this.eagPath = eagPath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {

        if (!this.name.equals(name)) {
            this.hasChangedName = true;
        }

        this.name = name;
    }

    public String getEnglishName() {
        return englishName;
    }

    public void setEnglishName(String englishName) {

        if (!this.englishName.equals(englishName)) {
            this.hasChangedEnglishName = true;
        }

        this.englishName = englishName;
    }

    public String getId() {
        return id;
    }

    public String getFilename() {
        return APEnetUtilities.convertToFilename(this.getId()) + ".xml";
    }

    public void setId(String id) {

        if (!this.id.equals(id)) {
            this.hasChangedId = true;
        }

        this.id = id;
    }

    public String getResponsiblePersonSurname() {
        return responsiblePersonSurname;
    }

    public void setResponsiblePersonSurname(String responsiblePersonSurname) {
        this.responsiblePersonSurname = responsiblePersonSurname;
    }

    public String getResponsiblePersonName() {
        return responsiblePersonName;
    }

    public void setResponsiblePersonName(String responsiblePersonName) {
        this.responsiblePersonName = responsiblePersonName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {

        if (!this.country.equals(country)) {
            this.hasChangedCountry = true;
        }

        this.country = country;
    }

    public String getCityTown() {
        return cityTown;
    }

    public void setCityTown(String cityTown) {

        if (!this.cityTown.equals(cityTown)) {
            this.hasChangedCityTown = true;
        }

        this.cityTown = cityTown;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {

        if (!this.postalCode.equals(postalCode)) {
            this.hasChangedPostalCode = true;
        }

        this.postalCode = postalCode;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {

        if (!this.street.equals(street)) {
            this.hasChangedStreet = true;
        }

        this.street = street;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {

        if (!this.telephone.equals(telephone)) {
            this.hasChangedTelephone = true;
        }

        this.telephone = telephone;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {

        if (!this.emailAddress.equals(emailAddress)) {
            this.hasChangedEmailAddress = true;
        }

        this.emailAddress = emailAddress;
    }

    public String getWebPage() {
        return webPage;
    }

    public void setWebPage(String webPage) {

        if (!this.webPage.equals(webPage)) {
            this.hasChangedWebPage = true;
        }

        this.webPage = webPage;
    }

    public Logger getLog() {
        return log;
    }

    public List<Ead> getHoldingsGuideIndexed() {
        return holdingsGuideIndexed;
    }

    public void setHoldingsGuideIndexed(List<Ead> holdingsGuideIndexed) {
        this.holdingsGuideIndexed = holdingsGuideIndexed;
    }

    public void setRepositorguideInformation(
            List<String> repositorguideInformation) {
        this.repositorguideInformation = repositorguideInformation;
    }

    public List<String> getRepositorguideInformation() {
        return repositorguideInformation;
    }

    public void setRepositorguideURL(List<String> repositorguideURL) {
        this.repositorguideURL = repositorguideURL;
    }

    public List<String> getRepositorguideURL() {
        return repositorguideURL;
    }

    public void setRepositorguideResource(List<String> repositorguideResource) {
        this.repositorguideResource = repositorguideResource;
    }

    public List<String> getRepositorguideResource() {
        return repositorguideResource;
    }

    public void setRepositorguidePossibleHGTitle(
            List<String> repositorguidePossibleHGTitle) {
        this.repositorguidePossibleHGTitle = repositorguidePossibleHGTitle;
    }

    public List<String> getRepositorguidePossibleHGTitle() {
        return repositorguidePossibleHGTitle;
    }

    public List<String> getLocalHGSelected() {
        return localHGSelected;
    }

    public void setLocalHGSelected(List<String> localHGSelected) {
        this.localHGSelected = localHGSelected;
    }

    public Boolean getEagEmptyInformation() {
        return eagEmptyInformation;
    }

    public void setEagEmptyInformation(Boolean eagEmptyInformation) {
        this.eagEmptyInformation = eagEmptyInformation;
    }

    //Constructor
    public APEnetEAG(ResourceBundleSource resourceBundleSource, Integer aiId, String tempEagPath) {

        this.resourceBundleSource = resourceBundleSource;
        ArchivalInstitutionDAO archivalInstitutionDao = DAOFactory.instance().getArchivalInstitutionDAO();
        ArchivalInstitution archivalInstitution = archivalInstitutionDao.findById(aiId);
        this.repositorguideInformation = new ArrayList<String>();
        this.repositorguideURL = new ArrayList<String>();
        this.repositorguideResource = new ArrayList<String>();
        this.repositorguidePossibleHGTitle = new ArrayList<String>();
        this.localHGSelected = new ArrayList<String>();
        List<Integer> localHGSelectedIds = new ArrayList<Integer>();
        HashMap<String, String> tagsAndAttributesToRetrieve = new HashMap<String, String>();

        this.eagPath = archivalInstitution.getEagPath();

        this.aiId = aiId;

        if (this.eagPath == null) {
            if (tempEagPath != null) {
                this.eagPath = tempEagPath;
            }
            this.name = "";
            if (archivalInstitution != null) {
                this.name = archivalInstitution.getAiname().toString();
            }
            this.englishName = "";
            this.id = "";
            this.responsiblePersonSurname = "";
            this.responsiblePersonName = "";
            this.country = "";
            this.cityTown = "";
            this.postalCode = "";
            this.street = "";
            this.telephone = "";
            this.emailAddress = "";
            this.webPage = "";
            this.firstdem = "";
            this.secondem = "";
            this.fax = "";
            this.localentity = "";
            this.opening = "";
            this.openingNum = "";
            this.closing = "";
            this.access = "";
            this.restaccess = "";
            this.accesibility = "";
            this.readingRoom = "";
            this.repositoryguide = "";
            this.library = "";
            this.restorationlab = "";
            this.reproductionser = "";
            this.photocopyser = "";
            this.microformser = "";
            this.photographser = "";
            this.digitalser = "";
            this.automation = "";
            this.autusermanag = "";
            this.autdescription = "";
            this.indexvoc = "";
            this.odautomationP = "";
            this.repositorhistP = "";
            this.date = "";
            this.rule = "";
            this.firstnameResp = "";
            this.surnamesResp = "";
            this.chargeResp = "";
            this.adminunit = "";
            this.building = "";
            this.repositorareaNum = "";
            this.lengthshelfNum = "";
            this.extentNum = "";
            this.notes = "";
            this.dateCreated = "";
            this.dateUpdated = "";
            this.holdingsGuideIndexed = null;
            /////////// LAST MODIFICATIONS REGARDING TICKET #652 -- Begin
            /////////////////////////////////////////////////////////////
            // Remove this line and uncomment the other one if 'Link to holdings guide' default text is wanted again within EAG web form
            this.repositorguideInformation.add("");
            //this.repositorguideInformation.add(actionSupport.getText("label.ai.hg.information.content.default"));
            /////////// LAST MODIFICATIONS REGARDING TICKET #652 -- End
            /////////////////////////////////////////////////////////////
            this.repositorguideURL.add("");
            this.repositorguideResource.add(REPOSITORGUIDE_EXTERNAL_RESOURCE);
            this.repositorguidePossibleHGTitle.add("");
            this.localHGSelected.add("");
            this.eagEmptyInformation = false;
        } else {
            this.log.debug("Extracting files from the EAG of " + this.aiId);

            if (tempEagPath != null) {
                this.eagPath = tempEagPath;
            } else {
                this.eagPath = APEnetUtilities.getConfig().getRepoDirPath() + this.eagPath;
            }

            this.createMapTagsAndAttributesFromEAGToExtract(tagsAndAttributesToRetrieve);
            Map<String, String> valuesExtractedFromEAG = XMLUtils.extractTagsAndAttributesFromXML(tagsAndAttributesToRetrieve, this.eagPath);
            this.fillEAGValues(valuesExtractedFromEAG);

            ContentSearchOptions eadSearchOptions = new ContentSearchOptions();
            eadSearchOptions.setContentClass(HoldingsGuide.class);
            eadSearchOptions.setArchivalInstitionId(aiId);
            eadSearchOptions.setPublished(true);
            this.holdingsGuideIndexed = DAOFactory.instance().getEadDAO().getEads(eadSearchOptions);

            boolean eadidFound = false;
            int j = 0;

            if (this.holdingsGuideIndexed.size() == 0) {
                this.repositorguidePossibleHGTitle.add("");
                this.localHGSelected.add("");
            } else {
                for (int i = 0; i < this.holdingsGuideIndexed.size(); i++) {
                    this.repositorguidePossibleHGTitle.add(this.holdingsGuideIndexed.get(i).getTitle());
                    localHGSelectedIds.add(this.holdingsGuideIndexed.get(i).getId());
                }
            }

            this.eagEmptyInformation = false;
            List<List<String>> repositorguideList = extractRepositorguideFromEag();
            for (int i = 0; i < repositorguideList.size(); i++) {
                /////////// LAST MODIFICATIONS REGARDING TICKET #652 -- Begin
                /////////////////////////////////////////////////////////////
                // Remove this block and uncomment the other one if 'Link to holdings guide' default text is wanted again within EAG web form
                if (repositorguideList.get(i).get(0).isEmpty() && !repositorguideList.get(i).get(1).equals(NO_URL) && !repositorguideList.get(i).get(1).equals("")) {
                    this.repositorguideInformation.add(resourceBundleSource.getString("label.ai.hg.information.content.default"));
                    this.eagEmptyInformation = true;
                } else {
                    this.repositorguideInformation.add(repositorguideList.get(i).get(0));
                }
                //this.repositorguideInformation.add(repositorguideList.get(i).get(0));
                /////////// LAST MODIFICATIONS REGARDING TICKET #652 -- End
                /////////////////////////////////////////////////////////////
                if (repositorguideList.get(i).get(1).equals(NO_URL) || repositorguideList.get(i).get(1).equals("")) {
                    this.repositorguideURL.add("");
                } else {
                    String url = repositorguideList.get(i).get(1);
                    if (!url.contains(PORTAL_CONTEXT + SECOND_DISPLAY_PREVIEW_ACTION)) {
                        if (!url.startsWith(HTTP) && !(url.startsWith(HTTPS))) {
                            url = HTTP + url;
                        }
                    }
                    this.repositorguideURL.add(url);
                }
            }

            if (this.repositorguideURL.size() != 0) {
                for (int i = 0; i < this.repositorguideURL.size(); i++) {
                    if (this.repositorguideURL.get(i).contains(PORTAL_CONTEXT + SECOND_DISPLAY_PREVIEW_ACTION)) {
                        // Then, the URL typed is a local URL
                        this.repositorguideResource.add(REPOSITORGUIDE_LOCAL_RESOURCE);

                        // It is necessary to search among the holdings guide indexed found in order to obtain its title
                        while (!eadidFound && j < localHGSelectedIds.size()) {

                            if (this.repositorguideURL.get(i).contains("id=" + localHGSelectedIds.get(j).toString() + "&")) {
                                this.localHGSelected.add(this.holdingsGuideIndexed.get(j).getTitle());
                                eadidFound = true;
                            }

                            j = j + 1;

                        }

                        if (!eadidFound) {
                            // 1: The URL was modified locally and the eadid is not correct
                            // 2: The HG is not indexed in the system anymore
                            this.repositorguidePossibleHGTitle.add(resourceBundleSource.getString("label.eag.error.hgindexed"));
                            this.localHGSelected.add(resourceBundleSource.getString("label.eag.error.hgindexed"));
                        }

                        j = 0;
                        eadidFound = false;

                    } else {
                        // Then, the URL typed is an external URL

                        this.repositorguideResource.add(REPOSITORGUIDE_EXTERNAL_RESOURCE);
                        this.localHGSelected.add("");
                    }
                }
            }
        }

        this.hasChangedName = false;
        this.hasChangedEnglishName = false;
        this.hasChangedId = false;
        this.hasChangedCountry = false;
        this.hasChangedCityTown = false;
        this.hasChangedPostalCode = false;
        this.hasChangedStreet = false;
        this.hasChangedTelephone = false;
        this.hasChangedEmailAddress = false;
        this.hasChangedWebPage = false;
        this.hasChangedAccess = false;
        this.hasChangedLibrary = false;
        this.hasChangedAutomation = false;

        tagsAndAttributesToRetrieve = null;
        archivalInstitutionDao = null;
        archivalInstitution = null;
        localHGSelectedIds = null;

    }

    //This method extracts the content within a tag inside an EAG (the first one) or
    //the content within an attribute for the tag
    public String extractAttributeFromEag(String element, String attribute, boolean isReturningFirstInstance) {
        final String CONVERTED_FLAG = "Converted_APEnet_EAG_version_";
        XMLStreamReader input = null;
        InputStream sfile = null;
        XMLInputFactory xmlif = (XMLInputFactory) XMLInputFactory.newInstance();
        xmlif.setProperty(XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES, Boolean.FALSE);
        xmlif.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, Boolean.FALSE);
        xmlif.setProperty(XMLInputFactory.SUPPORT_DTD, Boolean.FALSE);
        xmlif.setProperty(XMLInputFactory.IS_COALESCING, Boolean.FALSE);

        try {
            sfile = new FileInputStream(this.eagPath);
            input = (XMLStreamReader) xmlif.createXMLStreamReader(sfile);

            boolean abort = false;
            boolean isInsideElement = false;
            boolean isInsidePath = false;
            boolean wasInsidePath = false;
            String importantData = "";

            String[] pathElements = element.split("/");
            int lenghtPath = pathElements.length;
            int pointerPath = 0;

            log.debug("Checking EAG file, looking for element " + element + ", and attribute " + ((attribute == null) ? "null" : attribute) + ", path begins with " + pathElements[0]);
            while (!abort && input.hasNext()) {
                switch (input.getEventType()) {
                    case XMLEvent.START_DOCUMENT:
                        break;
                    case XMLEvent.START_ELEMENT:
                        if (input.getLocalName().equals(pathElements[pointerPath])) {
                            isInsidePath = true;
                            wasInsidePath = true;
                            if (pointerPath == lenghtPath - 1) {
                                isInsideElement = true;
                                if (attribute != null) {
                                    for (int attributeNb = 0; attributeNb < input.getAttributeCount(); attributeNb++) {
                                        if (input.getAttributeLocalName(attributeNb).equals(attribute)) {
                                            log.debug("Returning " + input.getAttributeValue(attributeNb));
                                            return input.getAttributeValue(attributeNb);
                                        }
                                    }
                                    log.debug("Returning error");
                                    return "error";
                                }
                            }
                            pointerPath++;
                        }
                        break;
                    case XMLEvent.CHARACTERS:
                        if (isInsideElement) {
                            importantData = input.getText();
                            if (importantData.startsWith(CONVERTED_FLAG)) {
                                return "true";
                            } else if (isReturningFirstInstance) {
                                return importantData;
                            }
                        }
                        break;
                    case XMLEvent.CDATA:
                        break;
                    case XMLEvent.END_ELEMENT:
                        if (isInsidePath && input.getLocalName().equals(pathElements[pointerPath - 1])) {
                            pointerPath--;
                            isInsideElement = false;
                            if (pointerPath == 0) {
                                isInsidePath = false;
                            }
                        }
                        if (!isInsidePath && wasInsidePath) {
                            abort = true;
                        }
                        break;
                }
                if (input.hasNext()) {
                    input.next();
                }
            }
        } catch (Exception e) {
            log.error("Error parsing StAX for file " + this.eagPath, e);
        } finally {
            try {
                input.close();
                sfile.close();
            } catch (Exception e) {
                log.error("Error closing streams" + e.getMessage(), e);
            }
        }
        log.debug("Returning error");
        return "error";
    }

    //This method returns all the same elements of an EAG xml file found
    public String lookingForward(String element, String attribute, String value) {
        XMLStreamReader input = null;
        InputStream sfile = null;
        XMLInputFactory xmlif = (XMLInputFactory) XMLInputFactory.newInstance();
        xmlif.setProperty(XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES, Boolean.FALSE);
        xmlif.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, Boolean.FALSE);
        xmlif.setProperty(XMLInputFactory.SUPPORT_DTD, Boolean.FALSE);
        xmlif.setProperty(XMLInputFactory.IS_COALESCING, Boolean.FALSE);
        try {
            sfile = new FileInputStream(this.eagPath);
            input = (XMLStreamReader) xmlif.createXMLStreamReader(sfile);
            String[] pathElements = element.split("/");
            while (input.hasNext()) {
                switch (input.getEventType()) {
                    case XMLEvent.START_ELEMENT:
                        for (int i = 0; i < pathElements.length; i++) {
                            if (input.getLocalName().equals(pathElements[i])) { //***********//
                                for (int x = 0; x < input.getAttributeCount(); x++) {
                                    if (input.getAttributeLocalName(x).equals(attribute) && input.getAttributeValue(x).equals(value)) {
                                        input.close();
                                        sfile.close();
                                        return value;
                                    }
                                }

                            }
                        }
                        break;
                }
                if (input.hasNext()) {
                    input.next();
                }
            }
            return "error"; //The element is not found
        } catch (Exception e) {
            log.error("Error parsing StAX for file " + this.eagPath, e);
        } finally {
            try {
                input.close();
                sfile.close();
            } catch (Exception e) {
                log.error("Error closing streams" + e.getMessage(), e);
            }
        }
        log.debug("Returning error");
        return "error"; //Error
    }

    public String extractUpdates(String element, String attributeName, String value) {
        XMLStreamReader input = null;
        InputStream sfile = null;
        XMLInputFactory xmlif = (XMLInputFactory) XMLInputFactory.newInstance();
        xmlif.setProperty(XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES, Boolean.FALSE);
        xmlif.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, Boolean.FALSE);
        xmlif.setProperty(XMLInputFactory.SUPPORT_DTD, Boolean.FALSE);
        xmlif.setProperty(XMLInputFactory.IS_COALESCING, Boolean.FALSE);
        try {
            sfile = new FileInputStream(this.eagPath);
            input = (XMLStreamReader) xmlif.createXMLStreamReader(sfile);
            String[] pathElements = element.split("/");
            int exit = -1;
            String lastAttributeValue = "";
            boolean found = false;
            while (input.hasNext()) {
                switch (input.getEventType()) {
                    case XMLEvent.START_ELEMENT:
                        for (int i = 0; i < pathElements.length; i++) {
                            if (input.getLocalName().equals(pathElements[i])) {
                                for (int x = 0; x < input.getAttributeCount(); x++) {
                                    if (input.getAttributeLocalName(x).equals(attributeName)) {
                                        if (lastAttributeValue.length() == 0) {
                                            lastAttributeValue = " ";
                                            found = false;
                                        } else if (found) {
                                            /*if(lastAttributeValue.length()>1){
												lastAttributeValue+=", ";
											}*/
                                            lastAttributeValue = input.getAttributeValue(x);
                                            exit = i;
                                            found = false;
                                        }
                                        break;
                                    }
                                }

                            } else if (input.getLocalName().trim().equals(pathElements[pathElements.length - 2])) {
                                found = true;
                            }
                        }
                        break;
                    case XMLEvent.END_ELEMENT:
                        if (exit > -1 && input.getLocalName().equals(pathElements[exit])) {
                            exit = -1;
                        }
                        break;
                }
                if (input.hasNext()) {
                    input.next();
                }
            }
            return lastAttributeValue;
        } catch (Exception e) {
            log.error("Error parsing StAX for file " + this.eagPath, e);
        } finally {
            try {
                input.close();
                sfile.close();
            } catch (Exception e) {
                log.error("Error closing streams" + e.getMessage(), e);
            }
        }
        log.debug("Returning error");
        return "error";
    }

    // This method returns all the same elements of an EAG xml file found
    // or the values for a specific element and attribute that can be repeateable
    public List<String> extractSameAttributesFromEag(String element, String attribute) {

        XMLStreamReader input = null;
        InputStream sfile = null;
        boolean isInsideElement = false;
        boolean isInsidePath = false;
        List<String> listResults = new ArrayList<String>();

        XMLInputFactory xmlif = (XMLInputFactory) XMLInputFactory.newInstance();
        xmlif.setProperty(XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES, Boolean.FALSE);
        xmlif.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, Boolean.FALSE);
        xmlif.setProperty(XMLInputFactory.SUPPORT_DTD, Boolean.FALSE);
        xmlif.setProperty(XMLInputFactory.IS_COALESCING, Boolean.FALSE);

        try {

            sfile = new FileInputStream(this.eagPath);
            input = (XMLStreamReader) xmlif.createXMLStreamReader(sfile);
            String[] pathElements = element.split("/");

            int lenghtPath = pathElements.length;
            int pointerPath = 0;

            while (input.hasNext()) {
                switch (input.getEventType()) {
                    case XMLEvent.START_ELEMENT:
                        for (int i = 0; i < lenghtPath; i++) {
                            if (input.getLocalName().equals(pathElements[i])) {
                                isInsidePath = true;
                                if (pointerPath == lenghtPath - 1) {
                                    // The reader is located in the element searched
                                    isInsideElement = true;
                                    if (attribute != null) {

                                        for (int x = 0; x < input.getAttributeCount(); x++) {

                                            if (input.getAttributeLocalName(x).equals(attribute)) {
                                                listResults.add(input.getAttributeValue(x));
                                            }

                                        }

                                    }
                                }

                                pointerPath = pointerPath + 1;

                            }
                        }
                        break;

                    case XMLEvent.CHARACTERS:
                        if (attribute == null) {
                            // We are only searching for Text inside the element (between init tag and end tag)
                            if (isInsideElement) {
                                listResults.add(input.getText());
                            }
                        }
                        break;

                    case XMLEvent.END_ELEMENT:

                        if (isInsidePath && input.getLocalName().equals(pathElements[pointerPath - 1])) {
                            pointerPath = pointerPath - 1;
                            isInsideElement = false;

                            if (pointerPath == 0) {
                                isInsidePath = false;
                            }
                        }

                        break;

                }
                if (input.hasNext()) {
                    input.next();
                }
            }

        } catch (Exception e) {

            log.error("Error parsing StAX for file " + this.eagPath, e);

        } finally {

            try {

                input.close();
                sfile.close();

            } catch (Exception e) {

                log.error("Error closing streams" + e.getMessage(), e);
            }
        }

        return listResults;
    }

    // This method returns all the repositorguide texts and href attributes
    // within an EAG file
    public List<List<String>> extractRepositorguideFromEag() {

        String attribute = "href";
        String element = "archguide/desc/repositorguides/repositorguide";
        XMLStreamReader input = null;
        InputStream sfile = null;
        boolean isInsideElement = false;
        boolean isInsidePath = false;
        int oneOfTwoFound = 0;
        boolean shouldHaveText = false;
        String hrefContent = "";
        String repositorguideContent = "";
        List<List<String>> listResults = new ArrayList<List<String>>();
        List<String> listItem = new ArrayList<String>();

        XMLInputFactory xmlif = (XMLInputFactory) XMLInputFactory.newInstance();
        xmlif.setProperty(XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES, Boolean.FALSE);
        xmlif.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, Boolean.FALSE);
        xmlif.setProperty(XMLInputFactory.SUPPORT_DTD, Boolean.FALSE);
        xmlif.setProperty(XMLInputFactory.IS_COALESCING, Boolean.FALSE);

        try {

            sfile = new FileInputStream(this.eagPath);
            input = (XMLStreamReader) xmlif.createXMLStreamReader(sfile);
            String[] pathElements = element.split("/");

            int lenghtPath = pathElements.length;
            int pointerPath = 0;

            while (input.hasNext()) {
                switch (input.getEventType()) {
                    case XMLEvent.START_ELEMENT:
                        for (int i = 0; i < lenghtPath; i++) {
                            if (input.getLocalName().equals(pathElements[i])) {
                                isInsidePath = true;
                                if (pointerPath == lenghtPath - 1) {
                                    // The reader is located in the element searched (repositorguide)
                                    isInsideElement = true;

                                    if (oneOfTwoFound == 0) {
                                        //It has been found the first repositorguide of the pair
                                        oneOfTwoFound = 1;

                                        for (int x = 0; x < input.getAttributeCount(); x++) {

                                            if (input.getAttributeLocalName(x).equals(attribute)) {
                                                oneOfTwoFound = 2;
                                                shouldHaveText = true;
                                                hrefContent = input.getAttributeValue(x);
                                                //listItem.add(input.getAttributeValue(x));
                                            }

                                        }

                                    } else if (oneOfTwoFound == 1) {
                                        // It was found a repositorguide without href before and it is needed to find
                                        // a repositorguide with href attribute
                                        for (int x = 0; x < input.getAttributeCount(); x++) {

                                            if (input.getAttributeLocalName(x).equals(attribute)) {
                                                oneOfTwoFound = 2;
                                                shouldHaveText = false;
                                                hrefContent = input.getAttributeValue(x);
                                                //listItem.add(input.getAttributeValue(x));
                                            }

                                        }
                                    }

                                }

                                pointerPath = pointerPath + 1;

                            }
                        }
                        break;

                    case XMLEvent.CHARACTERS:
                        if (oneOfTwoFound == 1) {
                            // It has been found a repositorguide tag without href attribute
                            if (isInsideElement) {
                                repositorguideContent = input.getText();
                                listItem.add(repositorguideContent);
                                listItem.add(NO_URL);
                                listResults.add(listItem);
                                listItem = new ArrayList<String>();
                            }
                        } else if (oneOfTwoFound == 2) {
                            // It has been found a repositorguide tag with href attribute
                            if (isInsideElement) {
                                if (shouldHaveText) {
                                    if (input.hasText()) {
                                        listItem.add(input.getText());
                                    } else {
                                        /////////// LAST MODIFICATIONS REGARDING TICKET #652 -- Begin
                                        /////////////////////////////////////////////////////////////
                                        // Remove this line and uncomment the other one if 'Link to holdings guide' default text is wanted again within EAG web form
                                        listItem.add("");
                                        //listItem.add(actionSupport.getText("label.ai.hg.information.content.default"));
                                        /////////// LAST MODIFICATIONS REGARDING TICKET #652 -- End
                                        /////////////////////////////////////////////////////////////
                                    }
                                    listItem.add(hrefContent);
                                    listResults.add(listItem);

                                } else // The repositorguide should't have text
                                 if (input.hasText()) {
                                        // It means that we have found a different repositorguide with 
                                        // href attribute and text
                                        // It is necessary to remove the last listItem
                                        // and add two different items
                                        listResults.remove(listResults.size() - 1);
                                        listItem.add(repositorguideContent);
                                        listItem.add(NO_URL);
                                        listResults.add(listItem);
                                        listItem = new ArrayList<String>();
                                        listItem.add(input.getText());
                                        listItem.add(hrefContent);
                                        listResults.add(listItem);

                                    }

                                listItem = new ArrayList<String>();
                                repositorguideContent = "";
                                hrefContent = "";
                                oneOfTwoFound = 0;
                            }
                        }
                        break;

                    case XMLEvent.END_ELEMENT:

                        if (isInsidePath && input.getLocalName().equals(pathElements[pointerPath - 1])) {

                            if (oneOfTwoFound == 2) {
                                if (!shouldHaveText) {
                                    listResults.remove(listResults.size() - 1);
                                    listItem.add(repositorguideContent);
                                    listItem.add(hrefContent);
                                    listResults.add(listItem);
                                } else {
                                    /////////// LAST MODIFICATIONS REGARDING TICKET #652 -- Begin
                                    /////////////////////////////////////////////////////////////
                                    // Remove this line and uncomment the other one if 'Link to holdings guide' default text is wanted again within EAG web form
                                    listItem.add("");
                                    //listItem.add(actionSupport.getText("label.ai.hg.information.content.default"));
                                    /////////// LAST MODIFICATIONS REGARDING TICKET #652 -- End
                                    /////////////////////////////////////////////////////////////
                                    listItem.add(hrefContent);
                                    listResults.add(listItem);
                                }

                                listItem = new ArrayList<String>();
                                repositorguideContent = "";
                                hrefContent = "";
                                oneOfTwoFound = 0;
                            }

                            pointerPath = pointerPath - 1;
                            isInsideElement = false;

                            if (pointerPath == 0) {
                                isInsidePath = false;
                            }
                        }

                        break;

                }
                if (input.hasNext()) {
                    input.next();
                }
            }

        } catch (Exception e) {

            log.error("Error parsing StAX for file " + this.eagPath, e);

        } finally {

            try {

                input.close();
                sfile.close();

            } catch (Exception e) {

                log.error("Error closing streams" + e.getMessage(), e);
            }
        }

        return listResults;
    }

    private void createMapTagsAndAttributesFromEAGToExtract(HashMap<String, String> tagsAndAttributesToRetrieved) {

        // In order to include a tag within the elements we want to search is necessary to follow this rules
        // 1- The input will be a HashMap(String, String)
        // 2- Every key will represent a tag
        // 3- Every value will represent an attribute or a set of attributes for this tag
        // 4- If the value "" is included for a key, that will mean that it is expected to be retrieved the text within the tag itself
        // 5- If the value is a set of attributes f.i.: "classcode; level", it will mean that it will be necessary to retrieve
        //    different values (one per attribute defined)
        // Example: INPUT -> [("eag/archguide/desc/organization/descunit","classcode;level") ; 
        //                    ("eag/archguide/identity/autform",""); 
        //                    ("eag/eagheader/mainhist/mainevent/date","normal")]
        tagsAndAttributesToRetrieved.put(ROOT + APEnetUtilities.FILESEPARATOR + AUTFORM_TAG, NULL_ATTRIBUTE);
        tagsAndAttributesToRetrieved.put(ROOT + APEnetUtilities.FILESEPARATOR + PARFORM_TAG, NULL_ATTRIBUTE);
        tagsAndAttributesToRetrieved.put(ROOT + APEnetUtilities.FILESEPARATOR + EADID_TAG, NULL_ATTRIBUTE);
        //tagsAndAttributesToRetrieved.put(ROOT + APEnetUtilities.FILESEPARATOR + SURNAMES_TAG, NULL_ATTRIBUTE);
        //tagsAndAttributesToRetrieved.put(ROOT + APEnetUtilities.FILESEPARATOR + FIRSTNAME_TAG, NULL_ATTRIBUTE);
        tagsAndAttributesToRetrieved.put(ROOT + APEnetUtilities.FILESEPARATOR + COUNTRY_TAG, NULL_ATTRIBUTE);
        tagsAndAttributesToRetrieved.put(ROOT + APEnetUtilities.FILESEPARATOR + MUNICIPALITY_TAG, NULL_ATTRIBUTE);
        tagsAndAttributesToRetrieved.put(ROOT + APEnetUtilities.FILESEPARATOR + POSTALCODE_TAG, NULL_ATTRIBUTE);
        tagsAndAttributesToRetrieved.put(ROOT + APEnetUtilities.FILESEPARATOR + STREET_TAG, NULL_ATTRIBUTE);
        tagsAndAttributesToRetrieved.put(ROOT + APEnetUtilities.FILESEPARATOR + TELEPHONE_TAG, NULL_ATTRIBUTE);
        tagsAndAttributesToRetrieved.put(ROOT + APEnetUtilities.FILESEPARATOR + EMAIL_TAG, HREF_ATTRIBUTE);
        tagsAndAttributesToRetrieved.put(ROOT + APEnetUtilities.FILESEPARATOR + WEBPAGE_TAG, HREF_ATTRIBUTE);
        tagsAndAttributesToRetrieved.put(ROOT + APEnetUtilities.FILESEPARATOR + ACCESS_TAG, QUESTION_ATTRIBUTE);
        tagsAndAttributesToRetrieved.put(ROOT + APEnetUtilities.FILESEPARATOR + HANDICAPPED_TAG, QUESTION_ATTRIBUTE);
        tagsAndAttributesToRetrieved.put(ROOT + APEnetUtilities.FILESEPARATOR + SEARCHROOM_NUM_TAG, NULL_ATTRIBUTE);
        tagsAndAttributesToRetrieved.put(ROOT + APEnetUtilities.FILESEPARATOR + LIBRARY_TAG, QUESTION_ATTRIBUTE);
        tagsAndAttributesToRetrieved.put(ROOT + APEnetUtilities.FILESEPARATOR + RESTORATIONLAB_TAG, QUESTION_ATTRIBUTE);
        tagsAndAttributesToRetrieved.put(ROOT + APEnetUtilities.FILESEPARATOR + REPRODUCTIONSER_TAG, QUESTION_ATTRIBUTE);
        tagsAndAttributesToRetrieved.put(ROOT + APEnetUtilities.FILESEPARATOR + AUTOMATION_TAG, QUESTION_ATTRIBUTE);
        tagsAndAttributesToRetrieved.put(ROOT + APEnetUtilities.FILESEPARATOR + EXTENT_NUM_TAG, NULL_ATTRIBUTE);
        tagsAndAttributesToRetrieved.put(ROOT + APEnetUtilities.FILESEPARATOR + DATE_TAG, NORMAL_ATTRIBUTE);
        tagsAndAttributesToRetrieved.put(ROOT + APEnetUtilities.FILESEPARATOR + MAINEVENT_TAG, MAINTYPE_ATTRIBUTE);

    }

    private void fillEAGValues(Map<String, String> valuesExtractedFromEAG) {

        String mainevent = null;
        String dates = null;
        List<String> maineventValues = null;
        List<String> dateValues = null;

        this.name = valuesExtractedFromEAG.get(ROOT + APEnetUtilities.FILESEPARATOR + AUTFORM_TAG);
        this.englishName = valuesExtractedFromEAG.get(ROOT + APEnetUtilities.FILESEPARATOR + PARFORM_TAG);
        this.id = valuesExtractedFromEAG.get(ROOT + APEnetUtilities.FILESEPARATOR + EADID_TAG);
        //this.responsiblePersonSurname = valuesExtractedFromEAG.get(ROOT + APEnetUtilities.FILESEPARATOR + SURNAMES_TAG);
        //this.responsiblePersonName = valuesExtractedFromEAG.get(ROOT + APEnetUtilities.FILESEPARATOR + FIRSTNAME_TAG);
        /*this.country = valuesExtractedFromEAG.get(ROOT + APEnetUtilities.FILESEPARATOR + COUNTRY_TAG);
		this.cityTown = valuesExtractedFromEAG.get(ROOT + APEnetUtilities.FILESEPARATOR + MUNICIPALITY_TAG);
		this.postalCode = valuesExtractedFromEAG.get(ROOT + APEnetUtilities.FILESEPARATOR + POSTALCODE_TAG);
		this.street = valuesExtractedFromEAG.get(ROOT + APEnetUtilities.FILESEPARATOR + STREET_TAG);
		this.telephone = valuesExtractedFromEAG.get(ROOT + APEnetUtilities.FILESEPARATOR + TELEPHONE_TAG);
		this.emailAddress = valuesExtractedFromEAG.get(ROOT + APEnetUtilities.FILESEPARATOR + EMAIL_TAG + START_ATTRIBUTE + HREF_ATTRIBUTE + END_ATTRIBUTE);
		this.webPage = valuesExtractedFromEAG.get(ROOT + APEnetUtilities.FILESEPARATOR + WEBPAGE_TAG + START_ATTRIBUTE + HREF_ATTRIBUTE + END_ATTRIBUTE);
		if (this.webPage!=null && !this.webPage.startsWith(HTTP) && !(this.webPage.startsWith(HTTPS))) {
			this.webPage = HTTP + this.webPage;
		}
		this.access = valuesExtractedFromEAG.get(ROOT + APEnetUtilities.FILESEPARATOR + ACCESS_TAG + START_ATTRIBUTE + QUESTION_ATTRIBUTE + END_ATTRIBUTE);
		this.accesibility = valuesExtractedFromEAG.get(ROOT + APEnetUtilities.FILESEPARATOR + HANDICAPPED_TAG + START_ATTRIBUTE + QUESTION_ATTRIBUTE + END_ATTRIBUTE);
		this.readingRoom = valuesExtractedFromEAG.get(ROOT + APEnetUtilities.FILESEPARATOR + SEARCHROOM_NUM_TAG);
		this.library = valuesExtractedFromEAG.get(ROOT + APEnetUtilities.FILESEPARATOR + LIBRARY_TAG + START_ATTRIBUTE + QUESTION_ATTRIBUTE + END_ATTRIBUTE);
		this.restorationlab = valuesExtractedFromEAG.get(ROOT + APEnetUtilities.FILESEPARATOR + RESTORATIONLAB_TAG + START_ATTRIBUTE + QUESTION_ATTRIBUTE + END_ATTRIBUTE);
		this.reproductionser = valuesExtractedFromEAG.get(ROOT + APEnetUtilities.FILESEPARATOR + REPRODUCTIONSER_TAG + START_ATTRIBUTE + QUESTION_ATTRIBUTE + END_ATTRIBUTE);			
		this.automation = valuesExtractedFromEAG.get(ROOT + APEnetUtilities.FILESEPARATOR + AUTOMATION_TAG + START_ATTRIBUTE + QUESTION_ATTRIBUTE + END_ATTRIBUTE);
		this.extentNum = valuesExtractedFromEAG.get(ROOT + APEnetUtilities.FILESEPARATOR + EXTENT_NUM_TAG);
		
		// Extracting the dates (creation and modification)
		// mainevent can be "create" or "create;update"
		mainevent = valuesExtractedFromEAG.get(ROOT + APEnetUtilities.FILESEPARATOR + MAINEVENT_TAG + START_ATTRIBUTE + MAINTYPE_ATTRIBUTE + END_ATTRIBUTE); 
		dates = valuesExtractedFromEAG.get(ROOT + APEnetUtilities.FILESEPARATOR + DATE_TAG + START_ATTRIBUTE + NORMAL_ATTRIBUTE + END_ATTRIBUTE);
		maineventValues = Arrays.asList(mainevent.split(SEPARATOR));
		dateValues = Arrays.asList(dates.split(SEPARATOR));
		
		// The dates are sorted. This way, the first date will be the creation date
		// and the other ones will be the modification dates
		Collections.sort(dateValues);
		
		this.dateCreated = "";
		
		if (maineventValues.contains("create")) {
			
			// There is a creation date
			if (dateValues.size() > 0) {
				// Because of the dates are sorted, the first one will be the creation date
				this.dateCreated = dateValues.get(0);
			}
		}
		
		this.dateUpdated = "";
		
		if (maineventValues.contains("update")) {
			
			// There is one or several modification dates
			for (int i = 1 ; i < dateValues.size() ; i ++) {
				this.dateUpdated = this.dateUpdated + dateValues.get(i);
				if (i + 1 < dateValues.size() ) {
					this.dateUpdated = this.dateUpdated + SEPARATOR;
				}
			}
		}
         */
    }

    public String lookingForwardElementContent(String element) {
        String text = null;
        XMLStreamReader input = null;
        InputStream sfile = null;
        XMLInputFactory xmlif = (XMLInputFactory) XMLInputFactory.newInstance();
        xmlif.setProperty(XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES, Boolean.FALSE);
        xmlif.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, Boolean.FALSE);
        xmlif.setProperty(XMLInputFactory.SUPPORT_DTD, Boolean.FALSE);
        xmlif.setProperty(XMLInputFactory.IS_COALESCING, Boolean.FALSE);

        try {
            sfile = new FileInputStream(this.eagPath);
            input = xmlif.createXMLStreamReader(sfile);

            boolean exit = false;
            boolean found = true;
            String[] pathElements = null;
            if (input != null && element != null) {
                if (element.contains("/")) { //check input parameters
                    if (element.startsWith("/")) {
                        element = element.substring(1);
                    }
                    if (element.endsWith("/") && element.length() > 2) {
                        element = element.substring(0, element.length() - 2);
                    }
                    pathElements = element.split("/");
                } else {
                    pathElements = new String[1];
                    pathElements[0] = element;
                }
                List<String> currentElement = new ArrayList<String>();
                log.debug("Checking EAG file, looking for element " + element + ", path begins with " + pathElements[0]);
                while (!exit && input.hasNext()) {
                    switch (input.getEventType()) {
                        case XMLEvent.START_ELEMENT:
                            currentElement.add(input.getLocalName().toString());
                            if (currentElement.size() == pathElements.length) {
                                found = true;
                                for (int i = 0; i < pathElements.length && found; i++) {
                                    found = (pathElements[i].trim().equals(currentElement.get(i).trim()));
                                }
                                text = "";
                            }
                            break;
                        case XMLEvent.CHARACTERS:
                        case XMLEvent.CDATA:
                            if (found) {
                                text += input.getText();
                            }
                            break;
                        case XMLEvent.END_ELEMENT:
                            currentElement.remove(currentElement.size() - 1);
                            if (found) {
                                exit = true;
                            }
                            break;
                    }
                    if (input.hasNext()) {
                        input.next();
                    }
                }
            }
        } catch (Exception e) {
            log.error("Exception getting " + element, e);
        }
        return text;
    }
}
