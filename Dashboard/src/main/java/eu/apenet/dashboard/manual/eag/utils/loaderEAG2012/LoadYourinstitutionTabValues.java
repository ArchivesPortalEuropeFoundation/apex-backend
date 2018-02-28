package eu.apenet.dashboard.manual.eag.utils.loaderEAG2012;

import org.apache.log4j.Logger;

import eu.apenet.dashboard.archivallandscape.ArchivalLandscapeUtils;
import eu.apenet.dashboard.manual.eag.Eag2012;
import eu.apenet.dashboard.manual.eag.utils.EAG2012Loader;
import eu.apenet.dpt.utils.eag2012.Autform;
import eu.apenet.dpt.utils.eag2012.Eag;
import eu.apenet.dpt.utils.eag2012.Location;
import eu.apenet.dpt.utils.eag2012.OtherRecordId;
import eu.apenet.dpt.utils.eag2012.Parform;
import eu.apenet.dpt.utils.eag2012.Repository;
import eu.apenet.dpt.utils.eag2012.ResourceRelation;
import eu.apenet.dpt.utils.eag2012.Timetable;

/**
 * Class of load yourintitution tab values LoadYourinstitutionTabValues.java
 */
public class LoadYourinstitutionTabValues implements LoaderEAG2012 {

    private final Logger log = Logger.getLogger(getClass());
    private EAG2012Loader loader;

    /**
     * @param yiNumberOfVisitorsAddress {@link String} the
     * yiNumberOfVisitorsAddress to add
     */
    public void addYiNumberOfVisitorsAddress(String yiNumberOfVisitorsAddress) {
        this.loader.getYiNumberOfVisitorsAddress().add(yiNumberOfVisitorsAddress);
    }

    /**
     * @param yiStreet {@link String} the yiStreet to add
     */
    public void addYiStreet(String yiStreet) {
        this.loader.getYiStreet().add(yiStreet);
    }

    /**
     * @param yiStreetLang {@link String} the yiStreetLang to add
     */
    public void addYiStreetLang(String yiStreetLang) {
        this.loader.getYiStreetLang().add(yiStreetLang);
    }

    /**
     * @param yiMunicipalityPostalcode {@link String} the
     * yiMunicipalityPostalcode to add
     */
    public void addYiMunicipalityPostalcode(String yiMunicipalityPostalcode) {
        this.loader.getYiMunicipalityPostalcode().add(yiMunicipalityPostalcode);
    }

    /**
     * @param yiMunicipalityPostalcodeLang {@link String} the
     * yiMunicipalityPostalcodeLang to add
     */
    public void addYiMunicipalityPostalcodeLang(String yiMunicipalityPostalcodeLang) {
        this.loader.getYiMunicipalityPostalcodeLang().add(yiMunicipalityPostalcodeLang);
    }

    /**
     * @param yiCountry {@link String} the yiCountry to add
     */
    public void addYiCountry(String yiCountry) {
        this.loader.getYiCountry().add(yiCountry);
    }

    /**
     * @param yiCountryLang {@link String} the yiCountryLang to add
     */
    public void addYiCountryLang(String yiCountryLang) {
        this.loader.getYiCountryLang().add(yiCountryLang);
    }

    /**
     * @param yiLatitude {@link String} the yiLatitude to add
     */
    public void addYiLatitude(String yiLatitude) {
        this.loader.getYiLatitude().add(yiLatitude);
    }

    /**
     * @param yiLongitude {@link String} the yiLongitude to add
     */
    public void addYiLongitude(String yiLongitude) {
        this.loader.getYiLongitude().add(yiLongitude);
    }

    /**
     * @param yiNumberOfPostalAddress {@link String} the yiNumberOfPostalAddress
     * to add
     */
    public void addYiNumberOfPostalAddress(String yiNumberOfPostalAddress) {
        this.loader.getYiNumberOfPostalAddress().add(yiNumberOfPostalAddress);
    }

    /**
     * @param yiStreetPostal {@link String} the yiStreetPostal to add
     */
    public void addYiStreetPostal(String yiStreetPostal) {
        this.loader.getYiStreetPostal().add(yiStreetPostal);
    }

    /**
     * @param yiStreetPostalLang {@link String} the yiStreetPostalLang to add
     */
    public void addYiStreetPostalLang(String yiStreetPostalLang) {
        this.loader.getYiStreetPostalLang().add(yiStreetPostalLang);
    }

    /**
     * @param yiMunicipalityPostalcodePostal {@link String} the
     * yiMunicipalityPostalcodePostal to add
     */
    public void addYiMunicipalityPostalcodePostal(String yiMunicipalityPostalcodePostal) {
        this.loader.getYiMunicipalityPostalcodePostal().add(yiMunicipalityPostalcodePostal);
    }

    /**
     * @param yiMunicipalityPostalcodePostalLang {@link String} the
     * yiMunicipalityPostalcodePostalLang to add
     */
    public void addYiMunicipalityPostalcodePostalLang(String yiMunicipalityPostalcodePostalLang) {
        this.loader.getYiMunicipalityPostalcodePostalLang().add(yiMunicipalityPostalcodePostalLang);
    }

    /**
     * @param yiNumberOfEmailAddress {@link String} the yiNumberOfEmailAddress
     * to add
     */
    public void addYiNumberOfEmailAddress(String yiNumberOfEmailAddress) {
        this.loader.getYiNumberOfEmailAddress().add(yiNumberOfEmailAddress);
    }

    /**
     * @param yiNumberOfWebpageAddress {@link String} the
     * yiNumberOfWebpageAddress to add
     */
    public void addYiNumberOfWebpageAddress(String yiNumberOfWebpageAddress) {
        this.loader.getYiNumberOfWebpageAddress().add(yiNumberOfWebpageAddress);
    }

    /**
     * @param yiEmail {@link String} the yiEmail to add
     */
    public void addYiEmail(String yiEmail) {
        this.loader.getYiEmail().add(yiEmail);
    }

    /**
     * @param yiEmailTitle {@link String} the yiEmailTitle to add
     */
    public void addYiEmailTitle(String yiEmailTitle) {
        this.loader.getYiEmailTitle().add(yiEmailTitle);
    }

    /**
     * @param yiEmailLang {@link String} the yiEmailLang to add
     */
    public void addYiEmailLang(String yiEmailLang) {
        this.loader.getYiEmailLang().add(yiEmailLang);
    }

    /**
     * @param yiWebpage {@link String} the yiWebpage to add
     */
    public void addYiWebpage(String yiWebpage) {
        this.loader.getYiWebpage().add(yiWebpage);
    }

    /**
     * @param yiWebpageTitle {@link String} the yiWebpageTitle to add
     */
    public void addYiWebpageTitle(String yiWebpageTitle) {
        this.loader.getYiWebpageTitle().add(yiWebpageTitle);
    }

    /**
     * @param yiWebpageLang {@link String} the yiWebpageLang to add
     */
    public void addYiWebpageLang(String yiWebpageLang) {
        this.loader.getYiWebpageLang().add(yiWebpageLang);
    }

    public void addYiNumberOfOpening(String yiNumberOfOpening) {
        this.loader.getYiNumberOfOpening().add(yiNumberOfOpening);
    }
    
    /**
     * @param yiOpeningContent {@link String} the yiOpeningContent to add
     */
    public void addYiOpeningContent(String yiOpeningContent) {
        this.loader.getYiOpeningContent().add(yiOpeningContent);
    }

    /**
     * @param yiOpeningLang {@link String} the yiOpeningLang to add
     */
    public void addYiOpeningLang(String yiOpeningLang) {
        this.loader.getYiOpeningLang().add(yiOpeningLang);
    }

    /**
     * @param yiOpeningHref {@link String} the yiOpeningHref to add
     */
    public void addYiOpeningHref(String yiOpeningHref) {
        this.loader.getYiOpeningHref().add(yiOpeningHref);
    }

    /**
     * @param yiClosing {@link String} the yiClosing to add
     */
    public void addYiClosing(String yiClosing) {
        this.loader.getYiClosing().add(yiClosing);
    }

    /**
     * @param yiClosingLang {@link String} the yiClosingLang to add
     */
    public void addYiClosingLang(String yiClosingLang) {
        this.loader.getYiClosingLang().add(yiClosingLang);
    }

    /**
     * @param yiRestaccess {@link String} the yiRestaccess to add
     */
    public void addYiRestaccess(String yiRestaccess) {
        this.loader.getYiRestaccess().add(yiRestaccess);
    }

    /**
     * @param yiRestaccessLang {@link String} the yiRestaccessLang to add
     */
    public void addYiRestaccessLang(String yiRestaccessLang) {
        this.loader.getYiRestaccessLang().add(yiRestaccessLang);
    }

    /**
     * @param yiAccessibilityQuestion {@link String} the yiAccessibilityQuestion
     * to add
     */
    public void addYiAccessibilityQuestion(String yiAccessibilityQuestion) {
        this.loader.getYiAccessibilityQuestion().add(yiAccessibilityQuestion);
    }

    /**
     * @param yiAccessibility {@link String} the yiAccessibility to add
     */
    public void addYiAccessibility(String yiAccessibility) {
        this.loader.getYiAccessibility().add(yiAccessibility);
    }

    /**
     * @param yiAccessibilityLang {@link String} the yiAccessibilityLang to add
     */
    public void addYiAccessibilityLang(String yiAccessibilityLang) {
        this.loader.getYiAccessibilityLang().add(yiAccessibilityLang);
    }

    /**
     * @param otherRecordIdLocalType {@link String} the otherRecordIdLocalType
     * to add
     */
    public void addOtherRecordIdLocalType(String otherRecordIdLocalType) {
        this.loader.getOtherRecordIdLocalType().add(otherRecordIdLocalType);
    }

    /**
     * @param otherRecordId {@link String} the otherRecordId to add
     */
    public void addOtherRecordId(String otherRecordId) {
        this.loader.getOtherRecordId().add(otherRecordId);
    }

    /**
     * @param idAutform {@link String} the idAutform to add
     */
    public void addIdAutform(String idAutform) {
        this.loader.getIdAutform().add(idAutform);
    }

    /**
     * @param idAutformLang {@link String} the idAutformLang to add
     */
    public void addIdAutformLang(String idAutformLang) {
        this.loader.getIdAutformLang().add(idAutformLang);
    }

    /**
     * @param idParform {@link String} the idParform to add
     */
    public void addIdParform(String idParform) {
        this.loader.getIdParform().add(idParform);
    }

    /**
     * @param idParformLang {@link String} the idParformLang to add
     */
    public void addIdParformLang(String idParformLang) {
        this.loader.getIdParformLang().add(idParformLang);
    }

    /**
     * @param yiResourceRelationHref {@link String} the yiResourceRelationHref
     * to add
     */
    public void addYiResourceRelationHref(String yiResourceRelationHref) {
        this.loader.getYiResourceRelationHref().add(yiResourceRelationHref);
    }

    /**
     * @param yiResourceRelationrelationEntry {@link String} the
     * yiResourceRelationrelationEntry to add
     */
    public void addYiResourceRelationrelationEntry(String yiResourceRelationrelationEntry) {
        this.loader.getYiResourceRelationrelationEntry().add(yiResourceRelationrelationEntry);
    }

    /**
     * @param yiResourceRelationLang {@link String} the yiResourceRelationLang
     * to add
     */
    public void addYiResourceRelationLang(String yiResourceRelationLang) {
        this.loader.getYiResourceRelationLang().add(yiResourceRelationLang);
    }

    /**
     * Eag {@link Eag>} JAXB object.
     */
    protected Eag eag;
    private Repository repository;
    private Location location;

    @Override
    public Eag LoaderEAG2012(Eag eag, EAG2012Loader eag2012Loader) {
        this.eag = eag;
        this.loader = eag2012Loader;
        main();

        return this.eag;
    }

    /**
     * Method to load all values of "your institution" tab of institution
     */
    private void main() {
        this.log.debug("Method start: \"Main of class LoadYourinstitutionTabValues\"");
        // Person/institution responsible for the description.
        loadYourinstitutionTabValuesPerson();
        // Country code.
        loadYourinstitutionTabValuesCountryCode();
        // Identifier of the institution.
        loadYourinstitutionTabValuesIdentifierOfInstitution();
        //Further IDs and ID used in APE.
        loadYourinstitutionTabValuesId();
        /// Name of the institution.
        loadYourinstitutionTabNameAndParallelInstitution();
        // Institution info.
        if (this.eag.getArchguide() != null && this.eag.getArchguide().getDesc() != null
                && this.eag.getArchguide().getDesc().getRepositories() != null) {
            if (!this.eag.getArchguide().getDesc().getRepositories().getRepository().isEmpty()) {
                // First repository equals institution.
                repository = this.eag.getArchguide().getDesc().getRepositories().getRepository().get(0);
                // Visitor & Postal address for institution.
                if (!repository.getLocation().isEmpty()) {
                    for (int i = 0; i < repository.getLocation().size(); i++) {
                        location = repository.getLocation().get(i);
                        if ((location.getLocalType() != null && location.getLocalType().equalsIgnoreCase(Eag2012.VISITORS_ADDRESS)) || (location.getLocalType() == null || location.getLocalType().isEmpty())) { //additional condition commented on #702
                            this.addYiNumberOfVisitorsAddress("");
                            // Street.
                            loadYourinstitutionTabAddressStreet();
                            // City.								
                            loadYourinstitutionTabAddressCity();
                            //country
                            loadYourinstitutionTabAddressCountry();//									
                            // Latitude and longitude.
                            loadYourinstitutionTabAddressLatitudeLongitude();
                        }
                        if (location.getLocalType() != null && location.getLocalType().equalsIgnoreCase(Eag2012.POSTAL_ADDRESS)) {
                            this.addYiNumberOfPostalAddress("");
                            // Postal street.//									
                            loadYourinstitutionTabPostalStreet();
                            // Postal city.
                            loadYourinstitutionTabPostalCity();
                        }
                    }
                    // Check language for "Visitor address" element and language for "Postal address" element.	
                    loadYourinstitutionTabCheck();
                }
                // Continent, Telephone and E-mail address for institution.
                loadYourinstitutionContinentTelephoneEmail();
                // Webpage for institution.
                loadYourinstitutionWebpage();
                // Timetable info
                loadYourinstitutionTimetable();
                // Accessible to the public.
                loadYourinstitutionFacilities();
                // Facilities for disabled people available.
                loadYourinstitutionAccessible();
            }
        }
        loadYourinstitutionTabHoldingsGuideInstitution();
        this.log.debug("End method: \"Main of class LoadYourinstitutionTabValues\"");
    }

    /**
     * Method to load all values of "Your institution" tab in the part of
     * Person/institution responsible for the description
     */
    private void loadYourinstitutionTabValuesPerson() {
        this.log.debug("Method start: \"loadYourinstitutionTabValuesPerson\"");
        // Person/institution responsible for the description.
        if (this.eag.getControl() != null && this.eag.getControl().getMaintenanceHistory() != null
                && !this.eag.getControl().getMaintenanceHistory().getMaintenanceEvent().isEmpty()) {
            if (this.eag.getControl().getMaintenanceHistory().getMaintenanceEvent().get(this.eag.getControl().getMaintenanceHistory().getMaintenanceEvent().size() - 1).getAgent() != null) {
                this.loader.setAgent(this.eag.getControl().getMaintenanceHistory().getMaintenanceEvent().get(this.eag.getControl().getMaintenanceHistory().getMaintenanceEvent().size() - 1).getAgent().getContent());
            }
        }
        this.log.debug("End method: \"loadYourinstitutionTabValuesPerson\"");
    }

    /**
     * Method to load all values of "Your institution" tab in the part of
     * Country code
     */
    private void loadYourinstitutionTabValuesCountryCode() {
        this.log.debug("Method start: \"loadYourinstitutionTabValuesCountryCode\"");
        // Country code.
        if (this.eag.getArchguide() != null
                && this.eag.getArchguide().getIdentity() != null
                && this.eag.getArchguide().getIdentity().getRepositorid() != null
                && this.eag.getArchguide().getIdentity().getRepositorid().getCountrycode() != null
                && !this.eag.getArchguide().getIdentity().getRepositorid().getCountrycode().isEmpty()) {
            this.loader.setCountryCode(this.eag.getArchguide().getIdentity().getRepositorid().getCountrycode());
        } else {
            this.loader.setCountryCode(new ArchivalLandscapeUtils().getmyCountry());
        }
        this.log.debug("End method: \"loadYourinstitutionTabValuesCountryCode\"");
    }

    /**
     * Method to load all values of "Your institution" tab in the part of
     * Identifier of the institution
     */
    private void loadYourinstitutionTabValuesIdentifierOfInstitution() {
        this.log.debug("Method start: \"loadYourinstitutionTabValuesIdentifierOfInstitution\"");
        // Identifier of the institution.
        if (this.eag.getArchguide() != null
                && this.eag.getArchguide().getIdentity() != null
                && this.eag.getArchguide().getIdentity().getOtherRepositorId() != null
                && this.eag.getArchguide().getIdentity().getOtherRepositorId().getContent() != null
                && !this.eag.getArchguide().getIdentity().getOtherRepositorId().getContent().isEmpty()) {
            this.loader.setOtherRepositorId(this.eag.getArchguide().getIdentity().getOtherRepositorId().getContent());
            if (this.eag.getControl() != null
                    && this.eag.getControl().getRecordId() != null
                    && this.eag.getControl().getRecordId().getValue() != null
                    && !this.eag.getControl().getRecordId().getValue().isEmpty()) {
                if (!this.eag.getArchguide().getIdentity().getOtherRepositorId().getContent().equalsIgnoreCase(this.eag.getControl().getRecordId().getValue())) {
                    this.loader.setRecordIdISIL(Eag2012.OPTION_YES);
                    this.loader.setRecordId(this.eag.getControl().getRecordId().getValue());
                    this.loader.setSelfRecordId(this.loader.getIdUsedInAPE());
                } else {
                    this.loader.setRecordIdISIL(Eag2012.OPTION_NO);
                    this.loader.setRecordId(this.loader.getRecordId());
                    this.loader.setSelfRecordId(this.loader.getIdUsedInAPE());
                }
            }
        } else {
            // Load the recordId value
            this.loader.setOtherRepositorId(this.eag.getControl().getRecordId().getValue());
            this.loader.setRecordIdISIL(Eag2012.OPTION_YES);
        }
        this.log.debug("End method: \"loadYourinstitutionTabValuesIdentifierOfInstitution\"");
    }

    /**
     * Method to load all values of "Your institution" tab in the part of futher
     * IDS and ID used in APE of the institution
     */
    private void loadYourinstitutionTabValuesId() {
        this.log.debug("Method start: \"loadYourinstitutionTabValuesId\"");
        // Further IDs.
        if (this.eag.getControl() != null
                && !this.eag.getControl().getOtherRecordId().isEmpty()) {
            for (int i = 0; i < this.eag.getControl().getOtherRecordId().size(); i++) {
                OtherRecordId otherRecordId = this.eag.getControl().getOtherRecordId().get(i);
                if (i == 0) {
                    this.loader.setOtherRepositorId(otherRecordId.getValue());
                    if (this.loader.getRecordId() != null && !this.loader.getRecordId().isEmpty()
                            && this.loader.getOtherRepositorId().equalsIgnoreCase(this.loader.getRecordId())) {
                        this.loader.setRecordIdISIL(Eag2012.OPTION_YES);
                    } else {
                        this.loader.setRecordIdISIL(Eag2012.OPTION_NO);
                    }
                } else {
                    this.addOtherRecordId(otherRecordId.getValue());
                    if (otherRecordId.getLocalType() != null
                            && !otherRecordId.getLocalType().isEmpty()) {
                        this.addOtherRecordIdLocalType(otherRecordId.getLocalType());
                    } else {
                        this.addOtherRecordIdLocalType(Eag2012.OPTION_NO);
                    }
                }
            }
        }
        // ID used in APE.
        if (this.eag.getControl() != null
                && this.eag.getControl().getRecordId() != null
                && this.eag.getControl().getRecordId().getValue() != null
                && !this.eag.getControl().getRecordId().getValue().isEmpty()) {
            this.loader.setRecordId(this.eag.getControl().getRecordId().getValue());
        }
        this.log.debug("End method: \"loadYourinstitutionTabValuesId\"");
    }

    /**
     * Method to load all values of "Your institution" tab in the part of name
     * and parallel of the institution
     */
    private void loadYourinstitutionTabNameAndParallelInstitution() {
        this.log.debug("Method start: \"loadYourinstitutionTabNameAndParallelInstitution\"");
        // Name of the institution.
        if (this.eag.getArchguide() != null
                && this.eag.getArchguide().getIdentity() != null
                && !this.eag.getArchguide().getIdentity().getAutform().isEmpty()) {
            for (int i = 0; i < this.eag.getArchguide().getIdentity().getAutform().size(); i++) {
                Autform autform = this.eag.getArchguide().getIdentity().getAutform().get(i);
                if (autform != null && autform.getContent() != null && !autform.getContent().isEmpty()) {
                    this.loader.addIdAutform(autform.getContent());
                    if (autform.getLang() != null && !autform.getLang().isEmpty()) {
                        this.addIdAutformLang(autform.getLang());
                    } else {
                        this.addIdAutformLang(Eag2012.OPTION_NONE);
                    }
                }
            }
            // First name of the institution.
            this.loader.setAutform(this.eag.getArchguide().getIdentity().getAutform().get(0).getContent());
            this.loader.setAutformLang(this.eag.getArchguide().getIdentity().getAutform().get(0).getLang());
        }
        // Parallel name of the institution.
        if (this.eag.getArchguide() != null
                && this.eag.getArchguide().getIdentity() != null
                && !this.eag.getArchguide().getIdentity().getParform().isEmpty()) {
            for (int i = 0; i < this.eag.getArchguide().getIdentity().getParform().size(); i++) {
                Parform parform = this.eag.getArchguide().getIdentity().getParform().get(i);
                if (parform != null && parform.getContent() != null && !parform.getContent().isEmpty()) {
                    this.addIdParform(parform.getContent());
                    if (parform.getLang() != null && !parform.getLang().isEmpty()) {
                        this.addIdParformLang(parform.getLang());
                    } else {
                        this.addIdParformLang(Eag2012.OPTION_NONE);
                    }
                }
            }
            // First parallel name of the institution.
            this.loader.setParform(this.eag.getArchguide().getIdentity().getParform().get(0).getContent());
            this.loader.setParformLang(this.eag.getArchguide().getIdentity().getParform().get(0).getLang());
        }
        this.log.debug("End method: \"loadYourinstitutionTabNameAndParallelInstitution\"");
    }

    /**
     * Method to load all values of "Your institution" tab in the part of
     * institution’s holdings guide
     */
    private void loadYourinstitutionTabHoldingsGuideInstitution() {
        this.log.debug("Method start: \"loadYourinstitutionTabHoldingsGuideInstitution\"");
        // Reference to your institution’s holdings guide.
        if (this.eag.getRelations() != null && !this.eag.getRelations().getResourceRelation().isEmpty()) {
            // Reference to your institution’s holdings guide for institution.
            for (int i = 0; i < this.eag.getRelations().getResourceRelation().size(); i++) {
                ResourceRelation resourceRelation = this.eag.getRelations().getResourceRelation().get(i);

                if (resourceRelation.getResourceRelationType() != null
                        && !resourceRelation.getResourceRelationType().isEmpty()
                        && Eag2012.OPTION_CREATOR_TEXT.equalsIgnoreCase(resourceRelation.getResourceRelationType())) {
                    if (resourceRelation.getHref() != null
                            && !resourceRelation.getHref().isEmpty()) {
                        this.addYiResourceRelationHref(resourceRelation.getHref());
                    } else {
                        this.addYiResourceRelationHref("");
                    }
                    if (resourceRelation.getRelationEntry() != null) {
                        if (resourceRelation.getRelationEntry().getContent() != null
                                && !resourceRelation.getRelationEntry().getContent().isEmpty()) {
                            this.addYiResourceRelationrelationEntry(resourceRelation.getRelationEntry().getContent());
                        } else {
                            this.addYiResourceRelationrelationEntry("");
                        }

                        if (resourceRelation.getRelationEntry().getLang() != null
                                && !resourceRelation.getRelationEntry().getLang().isEmpty()) {
                            this.addYiResourceRelationLang(resourceRelation.getRelationEntry().getLang());
                        } else {
                            this.addYiResourceRelationLang(Eag2012.OPTION_NONE);
                        }
                    }
                }
            }
        }
        this.log.debug("End method: \"loadYourinstitutionTabHoldingsGuideInstitution\"");
    }

    /**
     * Method to load all values of "Your institution" tab in the part of
     * Visitor address - Street of the institution
     */
    private void loadYourinstitutionTabAddressStreet() {
        this.log.debug("Method start: \"loadYourinstitutionTabAddressStreet\"");
        // Street.
        if (location.getStreet() != null) {
            if (location.getStreet().getContent() != null
                    && !location.getStreet().getContent().isEmpty()) {
                this.addYiStreet(location.getStreet().getContent());
            }
            if (location.getStreet().getLang() != null
                    && !location.getStreet().getLang().isEmpty()) {
                this.addYiStreetLang(location.getStreet().getLang());
            } else {
                this.addYiStreetLang(Eag2012.OPTION_NONE);
            }
        } else {
            this.addYiStreet("");
        }
        this.log.debug("End method: \"loadYourinstitutionTabAddressStreet\"");
    }

    /**
     * Method to load all values of "Your institution" tab in the part of
     * Visitor address - City of the institution
     */
    private void loadYourinstitutionTabAddressCity() {
        this.log.debug("Method start: \"loadYourinstitutionTabAddressCity\"");
        // City.
        if (location.getMunicipalityPostalcode() != null) {
            if (location.getMunicipalityPostalcode().getContent() != null
                    && !location.getMunicipalityPostalcode().getContent().isEmpty()) {
                this.addYiMunicipalityPostalcode(location.getMunicipalityPostalcode().getContent());
            }
            if (location.getMunicipalityPostalcode().getLang() != null
                    && !location.getMunicipalityPostalcode().getLang().isEmpty()) {
                this.addYiMunicipalityPostalcodeLang(location.getMunicipalityPostalcode().getLang());
            } else if (location.getStreet() != null
                    && location.getStreet().getLang() != null
                    && !location.getStreet().getLang().isEmpty()) {
                this.addYiMunicipalityPostalcodeLang(location.getStreet().getLang());
            } else {
                this.addYiMunicipalityPostalcodeLang(Eag2012.OPTION_NONE);
            }
        } else {
            this.addYiMunicipalityPostalcode("");
        }
        this.log.debug("End method: \"loadYourinstitutionTabAddressCity\"");
    }

    /**
     * Method to load all values of "Your institution" tab in the part of
     * Visitor address - Country of the institution
     */
    private void loadYourinstitutionTabAddressCountry() {
        this.log.debug("Method start: \"loadYourinstitutionTabAddressCountry\"");
        // Country.
        if (location.getCountry() != null) {
            if (location.getCountry().getContent() != null
                    && !location.getCountry().getContent().isEmpty()) {
                this.addYiCountry(location.getCountry().getContent());
            }
            if (location.getCountry().getLang() != null
                    && !location.getCountry().getLang().isEmpty()) {
                this.addYiCountryLang(location.getCountry().getLang());
            } else if (location.getStreet() != null
                    && location.getStreet().getLang() != null
                    && !location.getStreet().getLang().isEmpty()) {
                this.addYiCountryLang(location.getStreet().getLang());
            } else {
                this.addYiCountryLang(Eag2012.OPTION_NONE);
            }
        } else {
            this.addYiCountry("");
        }
        this.log.debug("End method: \"loadYourinstitutionTabAddressCountry\"");
    }

    /**
     * Method to load all values of "Your institution" tab in the part of
     * Visitor adress - Latitude and Longitude of the institution
     */
    private void loadYourinstitutionTabAddressLatitudeLongitude() {
        this.log.debug("Method start: \"loadYourinstitutionTabAddressLatitudeLongitude\"");
        // Latitude.
        if (location.getLatitude() != null) {
            this.addYiLatitude(location.getLatitude());
        } else {
            this.addYiLatitude("");
        }
        // Longitude.
        if (location.getLongitude() != null) {
            this.addYiLongitude(location.getLongitude());
        } else {
            this.addYiLongitude("");
        }
        this.log.debug("End method: \"loadYourinstitutionTabAddressLatitudeLongitude\"");
    }

    /**
     * Method to load all values of "Your institution" tab in the part of postal
     * adress - Street of the institution
     */
    private void loadYourinstitutionTabPostalStreet() {
        this.log.debug("Method start: \"loadYourinstitutionTabPostalStreet\"");
        // Postal street.
        if (location.getStreet() != null) {
            if (location.getStreet().getContent() != null
                    && !location.getStreet().getContent().isEmpty()) {
                this.addYiStreetPostal(location.getStreet().getContent());
            } else {
                this.addYiStreetPostal("");
            }
            if (location.getStreet().getLang() != null
                    && !location.getStreet().getLang().isEmpty()) {
                this.addYiStreetPostalLang(location.getStreet().getLang());
            } else {
                this.addYiStreetPostalLang(Eag2012.OPTION_NONE);
            }
        }
        this.log.debug("End method: \"loadYourinstitutionTabPostalStreet\"");
    }

    /**
     * Method to load all values of "Your institution" tab in the part of postal
     * adress - City of the institution
     */
    private void loadYourinstitutionTabPostalCity() {
        this.log.debug("Method start: \"loadYourinstitutionTabPostalCity\"");
        // Postal city.
        if (location.getMunicipalityPostalcode() != null) {
            if (location.getMunicipalityPostalcode().getContent() != null
                    && !location.getMunicipalityPostalcode().getContent().isEmpty()) {
                this.addYiMunicipalityPostalcodePostal(location.getMunicipalityPostalcode().getContent());
            } else {
                this.addYiMunicipalityPostalcodePostal("");
            }
            if (location.getMunicipalityPostalcode().getLang() != null
                    && !location.getMunicipalityPostalcode().getLang().isEmpty()) {
                this.addYiMunicipalityPostalcodePostalLang(location.getMunicipalityPostalcode().getLang());
            } else if (location.getStreet() != null
                    && location.getStreet().getLang() != null
                    && !location.getStreet().getLang().isEmpty()) {
                this.addYiMunicipalityPostalcodePostalLang(location.getStreet().getLang());
            } else {
                this.addYiMunicipalityPostalcodePostalLang(Eag2012.OPTION_NONE);
            }
        }
        this.log.debug("End method: \"loadYourinstitutionTabPostalCity\"");
    }

    /**
     * Method to check all values of "Your institution" tab of the institution
     */
    private void loadYourinstitutionTabCheck() {
        this.log.debug("Method start: \"loadYourinstitutionTabCheck\"");
        // Check language for "Visitor address" element.
        if (!this.loader.getYiStreetLang().isEmpty()) {
            for (int i = 0; i < this.loader.getYiStreetLang().size(); i++) {
                if (this.loader.getYiStreetLang().get(i) != null
                        && this.loader.getYiStreetLang().get(i).equalsIgnoreCase(Eag2012.OPTION_NONE)) {
                    if (!this.loader.getYiMunicipalityPostalcodeLang().isEmpty()
                            && this.loader.getYiMunicipalityPostalcodeLang().size() > i
                            && this.loader.getYiMunicipalityPostalcodeLang().get(i) != null
                            && !this.loader.getYiMunicipalityPostalcodeLang().get(i).equalsIgnoreCase(Eag2012.OPTION_NONE)) {
                        this.loader.getYiStreetLang().remove(i);
                        this.loader.getYiStreetLang().add(i, this.loader.getYiMunicipalityPostalcodeLang().get(i));
                    } else if (!this.loader.getYiCountryLang().isEmpty()
                            && this.loader.getYiCountryLang().size() > i
                            && this.loader.getYiCountryLang().get(i) != null
                            && !this.loader.getYiCountryLang().get(i).equalsIgnoreCase(Eag2012.OPTION_NONE)) {
                        this.loader.getYiStreetLang().remove(i);
                        this.loader.getYiStreetLang().add(i, this.loader.getYiCountryLang().get(i));
                    }
                }
            }
        }
        // Check language for "Postal address" element.
        if (!this.loader.getYiStreetPostalLang().isEmpty()) {
            for (int i = 0; i < this.loader.getYiStreetPostalLang().size(); i++) {
                if (this.loader.getYiStreetPostalLang().get(i) != null
                        && this.loader.getYiStreetPostalLang().get(i).equalsIgnoreCase(Eag2012.OPTION_NONE)) {
                    if (!this.loader.getYiMunicipalityPostalcodePostalLang().isEmpty()
                            && this.loader.getYiMunicipalityPostalcodePostalLang().size() > i
                            && this.loader.getYiMunicipalityPostalcodePostalLang().get(i) != null
                            && !this.loader.getYiMunicipalityPostalcodePostalLang().get(i).equalsIgnoreCase(Eag2012.OPTION_NONE)) {
                        this.loader.getYiStreetPostalLang().remove(i);
                        this.loader.getYiStreetPostalLang().add(i, this.loader.getYiMunicipalityPostalcodePostalLang().get(i));
                    }
                }
            }
        }
        this.log.debug("End method: \"loadYourinstitutionTabCheck\"");
    }

    /**
     * Method to load all values of "Your institution" tab in the part of
     * Continente telephone email of institution
     */
    private void loadYourinstitutionContinentTelephoneEmail() {
        this.log.debug("Method start: \"loadYourinstitutionContinentTelephoneEmail\"");
        // Continent.
        if (repository.getGeogarea() != null
                && repository.getGeogarea().getValue() != null
                && !repository.getGeogarea().getValue().isEmpty()) {
            this.loader.setGeogarea(getGeogareaString(repository.getGeogarea().getValue()));
        }
        // Telephone.
        if (!repository.getTelephone().isEmpty()) {
            this.loader.setTelephone(repository.getTelephone().get(0).getContent());
        }
        // E-mail address for institution.
        if (!repository.getEmail().isEmpty()) {
            for (int i = 0; i < repository.getEmail().size(); i++) {
                this.addYiNumberOfEmailAddress("");
                if (repository.getEmail().get(i).getHref() != null
                        && !repository.getEmail().get(i).getHref().isEmpty()) {
                    this.addYiEmail(repository.getEmail().get(i).getHref());
                } else {
                    this.addYiEmail("");
                }
                if (repository.getEmail().get(i).getContent() != null
                        && !repository.getEmail().get(i).getContent().isEmpty()) {
                    this.addYiEmailTitle(repository.getEmail().get(i).getContent());
                } else {
                    this.addYiEmailTitle("");
                }
                if (repository.getEmail().get(i).getLang() != null
                        && !repository.getEmail().get(i).getLang().isEmpty()) {
                    this.addYiEmailLang(repository.getEmail().get(i).getLang());
                } else {
                    this.addYiEmailLang(Eag2012.OPTION_NONE);
                }
            }
        }
        this.log.debug("End method: \"loadYourinstitutionContinentTelephoneEmail\"");
    }

    /**
     * Method to load all values of "Your institution" tab in the part of
     * webpage of institution
     */
    private void loadYourinstitutionWebpage() {
        this.log.debug("Method start: \"loadYourinstitutionWebpage\"");
        // Webpage for institution.
        if (!repository.getWebpage().isEmpty()) {
            for (int i = 0; i < repository.getWebpage().size(); i++) {
                this.addYiNumberOfWebpageAddress("");
                if (repository.getWebpage().get(i).getHref() != null
                        && !repository.getWebpage().get(i).getHref().isEmpty()) {
                    this.addYiWebpage(repository.getWebpage().get(i).getHref());
                } else {
                    this.addYiWebpage("");
                }
                if (repository.getWebpage().get(i).getContent() != null
                        && !repository.getWebpage().get(i).getContent().isEmpty()) {
                    this.addYiWebpageTitle(repository.getWebpage().get(i).getContent());
                } else {
                    this.addYiWebpageTitle("");
                }
                if (repository.getWebpage().get(i).getLang() != null
                        && !repository.getWebpage().get(i).getLang().isEmpty()) {
                    this.addYiWebpageLang(repository.getWebpage().get(i).getLang());
                } else {
                    this.addYiWebpageLang(Eag2012.OPTION_NONE);
                }
            }
        }
        this.log.debug("End method: \"loadYourinstitutionWebpage\"");
    }

    /**
     * Method to load all values of "Your institution" tab in the part of
     * timetable(opening times, closing dates) of institution
     */
    private void loadYourinstitutionTimetable() {
        this.log.debug("Method start: \"loadYourinstitutionTimetable\"");
        // Timetable info
        if (repository.getTimetable() != null) {
            Timetable timetable = repository.getTimetable();
            // Opening times.
            if (!timetable.getOpening().isEmpty()) {
                // Opening times for institution.
                for (int i = 0; i < timetable.getOpening().size(); i++) {
                    this.addYiNumberOfOpening("");
                    if (!timetable.getOpening().isEmpty()
                            && timetable.getOpening().size() >= i
                            && timetable.getOpening().get(i).getContent() != null
                            && !timetable.getOpening().get(i).getContent().isEmpty()) {
                        this.addYiOpeningContent(timetable.getOpening().get(i).getContent());
                    } else {
                        this.addYiOpeningContent("");
                    }
                    if (!timetable.getOpening().isEmpty()
                            && timetable.getOpening().size() >= i
                            && timetable.getOpening().get(i).getLang() != null
                            && !timetable.getOpening().get(i).getLang().isEmpty()) {
                        this.addYiOpeningLang(timetable.getOpening().get(i).getLang());
                    } else {
                        this.addYiOpeningLang(Eag2012.OPTION_NONE);
                    }
                    if (!timetable.getOpening().isEmpty()
                            && timetable.getOpening().size() >= i
                            && timetable.getOpening().get(i).getHref() != null
                            && !timetable.getOpening().get(i).getHref().isEmpty()) {
                        this.addYiOpeningHref(timetable.getOpening().get(i).getHref());
                    } else {
                        this.addYiOpeningHref("");
                    }
                }
            }
            // Closing dates.
            if (!timetable.getClosing().isEmpty()) {
                // Closing dates for institution.
                for (int i = 0; i < timetable.getClosing().size(); i++) {
                    if (!timetable.getClosing().isEmpty()
                            && timetable.getClosing().size() >= i
                            && timetable.getClosing().get(i).getContent() != null
                            && !timetable.getClosing().get(i).getContent().isEmpty()) {
                        this.addYiClosing(timetable.getClosing().get(i).getContent());
                    } else {
                        this.addYiClosing("");
                    }
                    if (!timetable.getClosing().isEmpty()
                            && timetable.getClosing().size() >= i
                            && timetable.getClosing().get(i).getLang() != null
                            && !timetable.getClosing().get(i).getLang().isEmpty()) {
                        this.addYiClosingLang(timetable.getClosing().get(i).getLang());
                    } else {
                        this.addYiClosingLang(Eag2012.OPTION_NONE);
                    }
                }
            }
        }
        this.log.debug("End method: \"loadYourinstitutionTimetable\"");
    }

    /**
     * Method to load all values of "Your institution" tab in the part of
     * Accessible to the public of institution
     */
    private void loadYourinstitutionAccessible() {
        this.log.debug("Method start: \"loadYourinstitutionAccessible\"");
        // Accessible to the public.
        if (repository.getAccess() != null) {
            if (repository.getAccess().getQuestion() != null
                    && !repository.getAccess().getQuestion().isEmpty()) {
                this.loader.setAccessQuestion(repository.getAccess().getQuestion());
            } else {
                this.loader.setAccessQuestion(Eag2012.OPTION_NO);
            }
            if (!repository.getAccess().getRestaccess().isEmpty()) {
                // Accessible to the public for institution.
                for (int i = 0; i < repository.getAccess().getRestaccess().size(); i++) {
                    if (repository.getAccess().getRestaccess().size() >= i
                            && repository.getAccess().getRestaccess().get(i).getContent() != null
                            && !repository.getAccess().getRestaccess().get(i).getContent().isEmpty()) {
                        this.addYiRestaccess(repository.getAccess().getRestaccess().get(i).getContent());
                    } else {
                        this.addYiRestaccess("");
                    }
                    if (repository.getAccess().getRestaccess().size() >= i
                            && repository.getAccess().getRestaccess().get(i).getLang() != null
                            && !repository.getAccess().getRestaccess().get(i).getLang().isEmpty()) {
                        this.addYiRestaccessLang(repository.getAccess().getRestaccess().get(i).getLang());
                    } else {
                        this.addYiRestaccessLang(Eag2012.OPTION_NONE);
                    }
                }
            }
        }
        this.log.debug("End method: \"loadYourinstitutionAccessible\"");
    }

    /**
     * Method to load all values of "Your institution" tab in the part of
     * Facilities for disabled people available of institution
     */
    private void loadYourinstitutionFacilities() {
        this.log.debug("Method start: \"loadYourinstitutionFacilities\"");
        // Facilities for disabled people available.
        if (!repository.getAccessibility().isEmpty()) {
            // Facilities for disabled people available for institution.
            for (int i = 0; i < repository.getAccessibility().size(); i++) {
                if (repository.getAccessibility().size() >= i
                        && repository.getAccessibility().get(i).getQuestion() != null
                        && !repository.getAccessibility().get(i).getQuestion().isEmpty()) {
                    this.addYiAccessibilityQuestion(repository.getAccessibility().get(i).getQuestion());
                } else {
                    this.addYiAccessibilityQuestion(Eag2012.OPTION_NO);
                }

                if (repository.getAccessibility().size() >= i
                        && repository.getAccessibility().get(i).getContent() != null
                        && !repository.getAccessibility().get(i).getContent().isEmpty()) {
                    this.addYiAccessibility(repository.getAccessibility().get(i).getContent());
                    if (repository.getAccessibility().size() >= i
                            && repository.getAccessibility().get(i).getLang() != null
                            && !repository.getAccessibility().get(i).getLang().isEmpty()) {
                        this.addYiAccessibilityLang(repository.getAccessibility().get(i).getLang());
                    } else {
                        this.addYiAccessibilityLang(Eag2012.OPTION_NONE);
                    }
                }
            }
        }
        this.log.debug("End method: \"loadYourinstitutionFacilities\"");
    }

    /**
     * Method to recover the value to set in XML file for {@code"<geogarea>"}
     */
    private String getGeogareaString(final String geogarea) {
        this.log.debug("Method start: \"getGeogareaString\"");
        String geogareaValue = geogarea;
        if (Eag2012.OPTION_AFRICA_TEXT.equalsIgnoreCase(geogareaValue)) {
            geogareaValue = Eag2012.OPTION_AFRICA;
        } else if (Eag2012.OPTION_ANTARCTICA_TEXT.equalsIgnoreCase(geogareaValue)) {
            geogareaValue = Eag2012.OPTION_ANTARCTICA;
        } else if (Eag2012.OPTION_ASIA_TEXT.equalsIgnoreCase(geogareaValue)) {
            geogareaValue = Eag2012.OPTION_ASIA;
        } else if (Eag2012.OPTION_AUSTRALIA_TEXT.equalsIgnoreCase(geogareaValue)) {
            geogareaValue = Eag2012.OPTION_AUSTRALIA;
        } else if (Eag2012.OPTION_EUROPE_TEXT.equalsIgnoreCase(geogareaValue)) {
            geogareaValue = Eag2012.OPTION_EUROPE;
        } else if (Eag2012.OPTION_NORTH_AMERICA_TEXT.equalsIgnoreCase(geogareaValue)) {
            geogareaValue = Eag2012.OPTION_NORTH_AMERICA;
        } else if (Eag2012.OPTION_SOUTH_AMERICA_TEXT.equalsIgnoreCase(geogareaValue)) {
            geogareaValue = Eag2012.OPTION_SOUTH_AMERICA;
        }
        this.log.debug("Method start: \"getGeogareaString\"");
        return geogareaValue;
    }

}
