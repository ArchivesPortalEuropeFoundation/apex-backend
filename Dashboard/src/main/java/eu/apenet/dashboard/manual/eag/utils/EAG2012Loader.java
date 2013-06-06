package eu.apenet.dashboard.manual.eag.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;


import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.dashboard.archivallandscape.ArchivalLandscape;
import eu.apenet.dashboard.manual.eag.Eag2012;
import eu.apenet.dpt.utils.eag2012.Autform;
import eu.apenet.dpt.utils.eag2012.Citation;
import eu.apenet.dpt.utils.eag2012.Eag;
import eu.apenet.dpt.utils.eag2012.Email;
import eu.apenet.dpt.utils.eag2012.Library;
import eu.apenet.dpt.utils.eag2012.Location;
import eu.apenet.dpt.utils.eag2012.P;
import eu.apenet.dpt.utils.eag2012.Parform;
import eu.apenet.dpt.utils.eag2012.RecreationalServices;
import eu.apenet.dpt.utils.eag2012.Repository;
import eu.apenet.dpt.utils.eag2012.Searchroom;
import eu.apenet.dpt.utils.eag2012.Telephone;
import eu.apenet.dpt.utils.eag2012.Timetable;
import eu.apenet.dpt.utils.eag2012.Webpage;
import eu.apenet.dpt.utils.util.LanguageConverter;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.ArchivalInstitution;

/**
 * Class for load an EAG2012 XML file into a JAXB object.
 */
public class EAG2012Loader{

	/**
	 * Logger.
	 */
	private final Logger log = Logger.getLogger(getClass());

	/**
	 * EAG2012 JAXB object.
	 */
	protected Eag eag;

	private String eagPath;
	private Integer aiId;

	// Attributes.
	// Common to various tabs.
    private String agent;
    private String countryCode;
    private String otherRepositorId;
    private String recordId;
    private String selfRecordId;
    private String autform;
    private String autformLang;
    private String parform;
    private String parformLang;
    private String localType;
    private String longitude;
    private String latitude;
    private String country;
    private String countryLang;
    private String municipalityPostalcode;
    private String municipalityPostalcodeLang;
    private String street;
    private String streetLang;
    private String municipalityPostalcodePostal;
    private String municipalityPostalcodePostalLang;
    private String streetPostal;
    private String streetPostalLang;
    private String geogarea;
    private String telephone;
    private String email;
    private String emailTitle;
    private String emailLang;
    private String webpage;
    private String webpageTitle;
    private String webpageLang;
    private String opening;
    private String openingLang;
    private String closing;
    private String closingLang;
    private String accessQuestion;
    private String restaccess;
    private String restaccessLang;
    private String accessibility;
    private String accessibilityQuestion;
    private String accessibilityLang;
    private String resourceRelationHref;
    private String resourceRelationrelationEntry;
    

	// Your institution tab.
    private String recordIdISIL;

	// Identity tab.
    private List<String> repositoryType;

	// Contact.
    private String firstdem;
    private String firstdemLang;
    private String localentity;
    private String localentityLang;
    private String secondem;
    private String secondemLang;
    private String fax;

	// Access and Services.
    private String directions;
    private String directionsLang;
    private String directionsCitationHref;
    private String citationHref;
    private String termsOfUse;
    private String termsOfUseLang;
    private String termsOfUseHref;
    private String searchRoomTelephone;
    private String searchRoomEmail;
    private String searchRoomEmailLink;
    private String searchRoomWebpage;
    private String searchRoomWebpageLink;
    private String searchRoomWorkPlaces;
    private String searchRoomComputerPlaces;
    private String searchRoomMicrofilmReaders;
    private String searchRoomPhotographAllowance;
    private String searchRoomPhotographAllowanceContent;
    private String searchRoomPhotographAllowanceHref;
    private String searchRoomPhotographAllowanceLang;
    private String searchRoomAdvancedOrdersContent;
    private String searchRoomAdvancedOrdersLang;
    private String searchRoomAdvancedOrdersHref;
    private String searchRoomResearchServicesContent;
    private String searchRoomResearchServicesLang;
    
    private String libraryQuestion;
    private String libraryTelephone;
    private String libraryEmailContent;
    private String libraryEmailHref;
    private String libraryWebpageContent;
    private String libraryWebpageHref;
    private String libraryMonographPublication;
    private String librarySerialPublication;
    private String libraryInternetAccessQuestion;
    private String libraryDescription;
    private String libraryDescriptionLang;
    
    private String technicalServicesQuestion;
    private String technicalServicesDescription;
    private String technicalServicesDescriptionLang;
    private String technicalServicesTelephone;
    private String technicalServicesEmail;
    private String technicalServicesEmailLink;
    private String technicalServicesEmailLang;
    private String technicalServicesWebpageLink;
    private String technicalServicesWebpage;
    private String technicalServicesWebpageLang;
    
    private String reproductionserQuestion;
    private String reproductionserDescription;
    private String reproductionserDescriptionLang;
    private String reproductionserTelephone;
    private String reproductionserEmail;
    private String reproductionserEmailLink;
    private String reproductionserEmailLang;
    private String reproductionserWebpage;
    private String reproductionserWebpageLink;
    private String reproductionserWebpageLang;
    private String microfilmServices;
    private String photographicServices;
    private String digitisationServices;
    private String photocopyingServices;
    private String recreationalServicesRefreshmentArea;
    private String recreationalServicesRefreshmentAreaLang;
    private String recreationalServicesExhibition;
    private String recreationalServicesExhibitionLang;
    private String recreationalServicesWeb;
    private String recreationalServicesWebLink;
    private String toursSessionGuidesAndSessionsContent;
    private String toursSessionGuidesAndSessionsLang;
    private String toursSessionGuidesAndSessionsWebpage;
    private String toursSessionGuidesAndSessionsWebpageTitle;
    private String otherServices;
    private String otherServicesLang;
    private String otherServicesWebpage;
    private String otherServicesLink;


	// Description.
    private String repositorhist;
    private String repositorhistLang;
    private String repositorFoundDate;

	private String repositorFoundDateLocalType;
    private String repositorFoundDateNotAfter;
    private String repositorFoundDateNotBefore;
    private String repositorFoundDateStandardDate;
    private String repositorFoundDateLang;
    private String repositorFoundRule;
    private String repositorFoundRuleLang;
    private String repositorSupDate;
    private String repositorSupDateLocalType;
    private String repositorSupDateNotAfter;
    private String repositorSupDateNotBefore;
    private String repositorSupDateStandardDate;
    private String repositorSupDateLang;
    private String repositorSupRule;
    private String repositorSupRuleLang;
    private String adminunit;
    private String adminunitLang;
    private String building;
    private String buildingLang;
    private String repositorarea;
    private String repositorareaUnit;
    private String lengthshelf;
    private String lengthshelfUnit;
    private String holdings;
    private String holdingsLang;
    private String holdingsDate;
    private String holdingsDateLocalType;
    private String holdingsDateNotAfter;
    private String holdingsDateNotBefore;
    private String holdingsDateStandardDate;
    private String holdingsDateLang;
    private String holdingsDateRangeLocalType;
    private String holdingsDateRangeFromDate;
    private String holdingsDateRangeFromDateNoAfter;
    private String holdingsDateRangeFromDateNoBefore;
    private String holdingsDateRangeFromDateStandardDate;
    private String holdingsDateRangeFromDateLang;
    private String holdingsDateRangeToDate;
    private String holdingsDateRangeToDateNoAfter;
    private String holdingsDateRangeToDateNoBefore;
    private String holdingsDateRangeToDateStandardDate;
    private String holdingsDateRangeToDateLang;
    private String extent;
    private String extentUnit;
    
	// Control.
    private String agentLang;
    private String agencyCode;
    private String language;
    private String languageDeclaration;
    private String script;
    private String abbreviation;
    private String citation;

	// Relations.
    private String resourceRelationType;
    private String resourceRelationLang;
    private String resourceRelationrelationEntryLocalType;
    private String resourceRelationrelationEntryScriptCode;
    private String resourceRelationrelationEntryTransliteration;
    private String resourceRelationrelationEntryLang;
    private String resourceRelationrelationEntryDescription;
    private String resourceRelationrelationEntryDescriptionLang;
    private String eagRelationType;
    private String eagRelationHref;
    private String eagRelationLang;
    private String eagRelationrelationEntry;
    private String eagRelationrelationEntryLocalType;
    private String eagRelationrelationEntryScriptCode;
    private String eagRelationrelationEntryTransliteration;
    private String eagRelationrelationEntryLang;
    private String eagRelationrelationEntryDescription;
    private String eagRelationrelationEntryDescriptionLang;
    
	public EAG2012Loader(Integer aiId) {
		this.aiId = aiId;
	}
	
	public EAG2012Loader(){}

	// Getters and setters.

	public boolean fillEag2012() {
		boolean result = true;
		Eag eag = null;
		ArchivalInstitution archivalInstitution = DAOFactory.instance().getArchivalInstitutionDAO().getArchivalInstitution(getId());
		String path = archivalInstitution.getEagPath();
		String alCountry = new ArchivalLandscape().getmyCountry();
		String basePath = APEnetUtilities.FILESEPARATOR + alCountry + APEnetUtilities.FILESEPARATOR +
				this.getId() + APEnetUtilities.FILESEPARATOR + Eag2012.EAG_PATH + APEnetUtilities.FILESEPARATOR;
		String tempPath = basePath + Eag2012.EAG_TEMP_FILE_NAME;
		File eagFile = null;
		if (new File(APEnetUtilities.getConfig().getRepoDirPath() + tempPath).exists()) {
			eagFile = new File((APEnetUtilities.getConfig().getRepoDirPath() + tempPath));
		} else {
			eagFile = new File((APEnetUtilities.getConfig().getRepoDirPath() + path));
		}

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
		this.setRecordId(this.getIdUsedInAPE());
		this.setSelfRecordId(this.getIdUsedInAPE());
		if (eag == null) {
			this.setCountryCode(this.getInitialCountryCode());
			log.info("no previous EAG file");
		} else {
			this.eag = eag;
			result = this.loadValuesEAG2012();
		}

		return result;
	}

	public Integer getId() {
		return this.aiId;
	}
	public void setId(Integer aiId){
		this.aiId = aiId;
	}
	/**
	 * @return the agent
	 */
	public String getAgent() {
		return this.agent;
	}
	/**
	 * @param agent the agent to set
	 */
	public void setAgent(String agent) {
		this.agent = agent;
	}
	/**
	 * @return the countryCode
	 */
	public String getCountryCode() {
		return this.countryCode;
	}
	/**
	 * @param countryCode the countryCode to set
	 */
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}
	public String getOtherRepositorId() {
		return this.otherRepositorId;
	}
	public void setOtherRepositorId(String otherRepositorId) {
		this.otherRepositorId = otherRepositorId;
	}
	/**
	* Returns the into ISO_2Characteres.
	* @return CC 
	*/
	public String getInitialCountryCode() {
		return new ArchivalLandscape().getmyCountry();
	}
	public String getRecordId() {
		return this.recordId;
	}
	
	public String getSelfRecordId() {
		return selfRecordId;
	}

	public void setSelfRecordId(String selfRecordId) {
		this.selfRecordId = selfRecordId;
	}

	/**
	 * Generates unique isocode for ID used in APE.
	 * 
	 * @return ISO_CODE
	 */
	public String getIdUsedInAPE(){
		return Eag2012.generatesISOCode(getId());
	}
	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}
	public String getAutform() {
		return this.autform;
	}
	public void setAutform(String autform) {
		this.autform = autform;
	}
	public String getAutformLang() {
		return this.autformLang;
	}
	public void setAutformLang(String autformLang) {
		this.autformLang = autformLang;
	}
	public String getParform() {
		return this.parform;
	}
	public void setParform(String parform) {
		this.parform = parform;
	}
	public String getParformLang() {
		return this.parformLang;
	}
	public void setParformLang(String parformLang) {
		this.parformLang = parformLang;
	}
	public String getLocalType() {
		return this.localType;
	}
	public void setLocalType(String localType) {
		this.localType = localType;
	}
	public String getLongitude() {
		return this.longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public String getLatitude() {
		return this.latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getCountry() {
		return this.country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getCountryLang() {
		return this.countryLang;
	}
	public void setCountryLang(String countryLang) {
		this.countryLang = countryLang;
	}
	public String getFirstdem() {
		return this.firstdem;
	}
	public void setFirstdem(String firstdem) {
		this.firstdem = firstdem;
	}
	public String getFirstdemLang() {
		return this.firstdemLang;
	}
	public void setFirstdemLang(String firstdemLang) {
		this.firstdemLang = firstdemLang;
	}
	public String getSecondem() {
		return this.secondem;
	}
	public void setSecondem(String secondem) {
		this.secondem = secondem;
	}
	public String getSecondemLang() {
		return this.secondemLang;
	}
	public void setSecondemLang(String secondemLang) {
		this.secondemLang = secondemLang;
	}
	public String getMunicipalityPostalcode() {
		return this.municipalityPostalcode;
	}
	public void setMunicipalityPostalcode(String municipalityPostalcode) {
		this.municipalityPostalcode = municipalityPostalcode;
	}
	public String getMunicipalityPostalcodeLang() {
		return this.municipalityPostalcodeLang;
	}
	public void setMunicipalityPostalcodeLang(String municipalityPostalcodeLang) {
		this.municipalityPostalcodeLang = municipalityPostalcodeLang;
	}
	public String getLocalentity() {
		return this.localentity;
	}
	public void setLocalentity(String localentity) {
		this.localentity = localentity;
	}
	public String getLocalentityLang() {
		return this.localentityLang;
	}
	public void setLocalentityLang(String localentityLang) {
		this.localentityLang = localentityLang;
	}
	public String getStreet() {
		return this.street;
	}
	public void setStreet(String street) {
		this.street = street;
	}
	public String getStreetLang() {
		return this.streetLang;
	}
	public void setStreetLang(String streetLang) {
		this.streetLang = streetLang;
	}
	public String getMunicipalityPostalcodePostal() {
		return this.municipalityPostalcodePostal;
	}
	public void setMunicipalityPostalcodePostal(String municipalityPostalcodePostal) {
		this.municipalityPostalcodePostal = municipalityPostalcodePostal;
	}
	public String getMunicipalityPostalcodePostalLang() {
		return this.municipalityPostalcodePostalLang;
	}
	public void setMunicipalityPostalcodePostalLang(String municipalityPostalcodePostalLang) {
		this.municipalityPostalcodePostalLang = municipalityPostalcodePostalLang;
	}
	public String getStreetPostal() {
		return this.streetPostal;
	}
	public void setStreetPostal(String streetPostal) {
		this.streetPostal = streetPostal;
	}
	public String getStreetPostalLang() {
		return this.streetPostalLang;
	}
	public void setStreetPostalLang(String streetPostalLang) {
		this.streetPostalLang = streetPostalLang;
	}
	public String getGeogarea() {
		return this.geogarea;
	}
	public void setGeogarea(String geogarea) {
		this.geogarea = geogarea;
	}
	public String getTelephone() {
		return this.telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public String getEmail() {
		return this.email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getEmailTitle() {
		return this.emailTitle;
	}
	public void setEmailTitle(String emailTitle) {
		this.emailTitle = emailTitle;
	}
	public String getEmailLang() {
		return this.emailLang;
	}
	public void setEmailLang(String emailLang) {
		this.emailLang = emailLang;
	}
	public String getWebpage() {
		return this.webpage;
	}
	public void setWebpage(String webpage) {
		this.webpage = webpage;
	}
	public String getWebpageTitle() {
		return this.webpageTitle;
	}
	public void setWebpageTitle(String webpageTitle) {
		this.webpageTitle = webpageTitle;
	}
	public String getWebpageLang() {
		return this.webpageLang;
	}
	public void setWebpageLang(String webpageLang) {
		this.webpageLang = webpageLang;
	}
	public String getOpening() {
		return this.opening;
	}
	public void setOpening(String opening) {
		this.opening = opening;
	}
	public String getOpeningLang() {
		return this.openingLang;
	}
	public void setOpeningLang(String openingLang) {
		this.openingLang = openingLang;
	}
	public String getClosing() {
		return this.closing;
	}
	public void setClosing(String closing) {
		this.closing = closing;
	}
	public String getClosingLang() {
		return this.closingLang;
	}
	public void setClosingLang(String closingLang) {
		this.closingLang = closingLang;
	}
	public String getAccessQuestion() {
		return this.accessQuestion;
	}
	public void setAccessQuestion(String accessQuestion) {
		this.accessQuestion = accessQuestion;
	}
	public String getRestaccess() {
		return this.restaccess;
	}
	public void setRestaccess(String restaccess) {
		this.restaccess = restaccess;
	}
	public String getRestaccessLang() {
		return this.restaccessLang;
	}
	public void setRestaccessLang(String restaccessLang) {
		this.restaccessLang = restaccessLang;
	}
	public String getAccessibility() {
		return this.accessibility;
	}
	public void setAccessibility(String accessibility) {
		this.accessibility = accessibility;
	}
	public String getAccessibilityQuestion() {
		return this.accessibilityQuestion;
	}
	public void setAccessibilityQuestion(String accessibilityQuestion) {
		this.accessibilityQuestion = accessibilityQuestion;
	}
	public String getAccessibilityLang() {
		return this.accessibilityLang;
	}
	public void setAccessibilityLang(String accessibilityLang) {
		this.accessibilityLang = accessibilityLang;
	}
	public String getResourceRelationHref() {
		return this.resourceRelationHref;
	}
	public void setResourceRelationHref(String resourceRelationHref) {
		this.resourceRelationHref = resourceRelationHref;
	}
	public String getResourceRelationLang() {
		return this.resourceRelationLang;
	}
	public void setResourceRelationLang(String resourceRelationLang) {
		this.resourceRelationLang = resourceRelationLang;
	}
	public String getResourceRelationType() {
		return this.resourceRelationType;
	}
	public void setResourceRelationType(String resourceRelationType) {
		this.resourceRelationType = resourceRelationType;
	}
	public String getResourceRelationrelationEntry() {
		return this.resourceRelationrelationEntry;
	}
	public void setResourceRelationrelationEntry(
			String resourceRelationrelationEntry) {
		this.resourceRelationrelationEntry = resourceRelationrelationEntry;
	}
	public String getResourceRelationrelationEntryLocalType() {
		return this.resourceRelationrelationEntryLocalType;
	}
	public void setResourceRelationrelationEntryLocalType(
			String resourceRelationrelationEntryLocalType) {
		this.resourceRelationrelationEntryLocalType = resourceRelationrelationEntryLocalType;
	}
	public String getResourceRelationrelationEntryScriptCode() {
		return this.resourceRelationrelationEntryScriptCode;
	}
	public void setResourceRelationrelationEntryScriptCode(
			String resourceRelationrelationEntryScriptCode) {
		this.resourceRelationrelationEntryScriptCode = resourceRelationrelationEntryScriptCode;
	}
	public String getResourceRelationrelationEntryTransliteration() {
		return this.resourceRelationrelationEntryTransliteration;
	}
	public void setResourceRelationrelationEntryTransliteration(
			String resourceRelationrelationEntryTransliteration) {
		this.resourceRelationrelationEntryTransliteration = resourceRelationrelationEntryTransliteration;
	}
	public String getResourceRelationrelationEntryLang() {
		return this.resourceRelationrelationEntryLang;
	}
	public void setResourceRelationrelationEntryLang(
			String resourceRelationrelationEntryLang) {
		this.resourceRelationrelationEntryLang = resourceRelationrelationEntryLang;
	}
	public String getResourceRelationrelationEntryDescription() {
		return this.resourceRelationrelationEntryDescription;
	}
	public void setResourceRelationrelationEntryDescription(
			String resourceRelationrelationEntryDescription) {
		this.resourceRelationrelationEntryDescription = resourceRelationrelationEntryDescription;
	}
	public String getResourceRelationrelationEntryDescriptionLang() {
		return this.resourceRelationrelationEntryDescriptionLang;
	}
	public void setResourceRelationrelationEntryDescriptionLang(
			String resourceRelationrelationEntryDescriptionLang) {
		this.resourceRelationrelationEntryDescriptionLang = resourceRelationrelationEntryDescriptionLang;
	}
	public String getRecordIdISIL() {
		return this.recordIdISIL;
	}

	public void setRecordIdISIL(String recordIdISIL) {
		this.recordIdISIL = recordIdISIL;
	}

	public List<String> getRepositoryType() {
        if (this.repositoryType == null) {
        	this.repositoryType = new ArrayList<String>();
        }
		return this.repositoryType;
	}
	public void setRepositoryType(List<String> repositoryType) {
		this.repositoryType = repositoryType;
	}
	public String getFax() {
		return this.fax;
	}
	public void setFax(String fax) {
		this.fax = fax;
	}
	public String getRepositorhist() {
		return this.repositorhist;
	}
	public String getDirections() {
		return this.directions;
	}
	public void setDirections(String directions) {
		this.directions = directions;
	}
	public String getDirectionsLang() {
		return this.directionsLang;
	}
	public void setDirectionsLang(String directionsLang) {
		this.directionsLang = directionsLang;
	}
	public String getDirectionsCitationHref() {
		return this.directionsCitationHref;
	}

	public void setDirectionsCitationHref(String directionsCitationHref) {
		this.directionsCitationHref = directionsCitationHref;
	}

	public String getCitationHref() {
		return this.citationHref;
	}
	public void setCitationHref(String citationHref) {
		this.citationHref = citationHref;
	}
	public String getTermsOfUse() {
		return this.termsOfUse;
	}
	public void setTermsOfUse(String termsOfUse) {
		this.termsOfUse = termsOfUse;
	}
	public String getTermsOfUseLang() {
		return this.termsOfUseLang;
	}
	public void setTermsOfUseLang(String termsOfUseLang) {
		this.termsOfUseLang = termsOfUseLang;
	}
	public String getTermsOfUseHref() {
		return this.termsOfUseHref;
	}
	public void setTermsOfUseHref(String termsOfUseHref) {
		this.termsOfUseHref = termsOfUseHref;
	}
	public void setRepositorhist(String repositorhist) {
		this.repositorhist = repositorhist;
	}
	public String getRepositorhistLang() {
		return this.repositorhistLang;
	}
	public void setRepositorhistLang(String repositorhistLang) {
		this.repositorhistLang = repositorhistLang;
	}
	public String getRepositorFoundDate() {
		return this.repositorFoundDate;
	}
	public void setRepositorFoundDate(String repositorFoundDate) {
		this.repositorFoundDate = repositorFoundDate;
	}
	public String getRepositorFoundDateLocalType() {
		return this.repositorFoundDateLocalType;
	}
	public void setRepositorFoundDateLocalType(
			String repositorFoundDateLocalType) {
		this.repositorFoundDateLocalType = repositorFoundDateLocalType;
	}
	public String getRepositorFoundDateNotAfter() {
		return this.repositorFoundDateNotAfter;
	}
	public void setRepositorFoundDateNotAfter(
			String repositorFoundDateNotAfter) {
		this.repositorFoundDateNotAfter = repositorFoundDateNotAfter;
	}
	public String getRepositorFoundDateNotBefore() {
		return this.repositorFoundDateNotBefore;
	}
	public void setRepositorFoundDateNotBefore(
			String repositorFoundDateNotBefore) {
		this.repositorFoundDateNotBefore = repositorFoundDateNotBefore;
	}
	public String getRepositorFoundDateStandardDate() {
		return this.repositorFoundDateStandardDate;
	}
	public void setRepositorFoundDateStandardDate(
			String repositorFoundDateStandardDate) {
		this.repositorFoundDateStandardDate = repositorFoundDateStandardDate;
	}
	public String getRepositorFoundDateLang() {
		return this.repositorFoundDateLang;
	}
	public void setRepositorFoundDateLang(String repositorFoundDateLang) {
		this.repositorFoundDateLang = repositorFoundDateLang;
	}
	public String getRepositorFoundRule() {
		return this.repositorFoundRule;
	}
	public void setRepositorFoundRule(String repositorFoundRule) {
		this.repositorFoundRule = repositorFoundRule;
	}
	public String getRepositorFoundRuleLang() {
		return this.repositorFoundRuleLang;
	}
	public void setRepositorFoundRuleLang(String repositorFoundRuleLang) {
		this.repositorFoundRuleLang = repositorFoundRuleLang;
	}
	public String getRepositorSupDate() {
		return this.repositorSupDate;
	}
	public void setRepositorSupDate(String repositorSupDate) {
		this.repositorSupDate = repositorSupDate;
	}
	public String getRepositorSupDateLocalType() {
		return this.repositorSupDateLocalType;
	}
	public void setRepositorSupDateLocalType(String repositorSupDateLocalType) {
		this.repositorSupDateLocalType = repositorSupDateLocalType;
	}
	public String getRepositorSupDateNotAfter() {
		return this.repositorSupDateNotAfter;
	}
	public void setRepositorSupDateNotAfter(String repositorSupDateNotAfter) {
		this.repositorSupDateNotAfter = repositorSupDateNotAfter;
	}
	public String getRepositorSupDateNotBefore() {
		return this.repositorSupDateNotBefore;
	}
	public void setRepositorSupDateNotBefore(String repositorSupDateNotBefore) {
		this.repositorSupDateNotBefore = repositorSupDateNotBefore;
	}
	public String getRepositorSupDateStandardDate() {
		return this.repositorSupDateStandardDate;
	}
	public void setRepositorSupDateStandardDate(
			String repositorSupDateStandardDate) {
		this.repositorSupDateStandardDate = repositorSupDateStandardDate;
	}
	public String getRepositorSupDateLang() {
		return this.repositorSupDateLang;
	}
	public void setRepositorSupDateLang(String repositorSupDateLang) {
		this.repositorSupDateLang = repositorSupDateLang;
	}
	public String getRepositorSupRule() {
		return this.repositorSupRule;
	}
	public void setRepositorSupRule(String repositorSupRule) {
		this.repositorSupRule = repositorSupRule;
	}
	public String getRepositorSupRuleLang() {
		return this.repositorSupRuleLang;
	}
	public void setRepositorSupRuleLang(String repositorSupRuleLang) {
		this.repositorSupRuleLang = repositorSupRuleLang;
	}
	public String getAdminunit() {
		return this.adminunit;
	}
	public void setAdminunit(String adminunit) {
		this.adminunit = adminunit;
	}
	public String getAdminunitLang() {
		return this.adminunitLang;
	}
	public void setAdminunitLang(String adminunitLang) {
		this.adminunitLang = adminunitLang;
	}
	public String getBuilding() {
		return this.building;
	}
	public void setBuilding(String building) {
		this.building = building;
	}
	public String getBuildingLang() {
		return this.buildingLang;
	}
	public void setBuildingLang(String buildingLang) {
		this.buildingLang = buildingLang;
	}
	public String getRepositorarea() {
		return this.repositorarea;
	}
	public void setRepositorarea(String repositorarea) {
		this.repositorarea = repositorarea;
	}
	public String getRepositorareaUnit() {
		return this.repositorareaUnit;
	}
	public void setRepositorareaUnit(String repositorareaUnit) {
		this.repositorareaUnit = repositorareaUnit;
	}
	public String getLengthshelf() {
		return this.lengthshelf;
	}
	public void setLengthshelf(String lengthshelf) {
		this.lengthshelf = lengthshelf;
	}
	public String getLengthshelfUnit() {
		return this.lengthshelfUnit;
	}
	public void setLengthshelfUnit(String lengthshelfUnit) {
		this.lengthshelfUnit = lengthshelfUnit;
	}
	public String getHoldings() {
		return this.holdings;
	}
	public void setHoldings(String holdings) {
		this.holdings = holdings;
	}
	public String getHoldingsLang() {
		return this.holdingsLang;
	}
	public void setHoldingsLang(String holdingsLang) {
		this.holdingsLang = holdingsLang;
	}
	public String getHoldingsDate() {
		return this.holdingsDate;
	}
	public void setHoldingsDate(String holdingsDate) {
		this.holdingsDate = holdingsDate;
	}
	public String getHoldingsDateLocalType() {
		return this.holdingsDateLocalType;
	}
	public void setHoldingsDateLocalType(String holdingsDateLocalType) {
		this.holdingsDateLocalType = holdingsDateLocalType;
	}
	public String getHoldingsDateNotAfter() {
		return this.holdingsDateNotAfter;
	}
	public void setHoldingsDateNotAfter(String holdingsDateNotAfter) {
		this.holdingsDateNotAfter = holdingsDateNotAfter;
	}
	public String getHoldingsDateNotBefore() {
		return this.holdingsDateNotBefore;
	}
	public void setHoldingsDateNotBefore(String holdingsDateNotBefore) {
		this.holdingsDateNotBefore = holdingsDateNotBefore;
	}
	public String getHoldingsDateStandardDate() {
		return this.holdingsDateStandardDate;
	}
	public void setHoldingsDateStandardDate(String holdingsDateStandardDate) {
		this.holdingsDateStandardDate = holdingsDateStandardDate;
	}
	public String getHoldingsDateLang() {
		return this.holdingsDateLang;
	}
	public void setHoldingsDateLang(String holdingsDateLang) {
		this.holdingsDateLang = holdingsDateLang;
	}
	public String getHoldingsDateRangeLocalType() {
		return this.holdingsDateRangeLocalType;
	}
	public void setHoldingsDateRangeLocalType(
			String holdingsDateRangeLocalType) {
		this.holdingsDateRangeLocalType = holdingsDateRangeLocalType;
	}
	public String getHoldingsDateRangeFromDate() {
		return this.holdingsDateRangeFromDate;
	}
	public void setHoldingsDateRangeFromDate(String holdingsDateRangeFromDate) {
		this.holdingsDateRangeFromDate = holdingsDateRangeFromDate;
	}
	public String getHoldingsDateRangeFromDateNoAfter() {
		return this.holdingsDateRangeFromDateNoAfter;
	}
	public void setHoldingsDateRangeFromDateNoAfter(
			String holdingsDateRangeFromDateNoAfter) {
		this.holdingsDateRangeFromDateNoAfter = holdingsDateRangeFromDateNoAfter;
	}
	public String getHoldingsDateRangeFromDateNoBefore() {
		return this.holdingsDateRangeFromDateNoBefore;
	}
	public void setHoldingsDateRangeFromDateNoBefore(
			String holdingsDateRangeFromDateNoBefore) {
		this.holdingsDateRangeFromDateNoBefore = holdingsDateRangeFromDateNoBefore;
	}
	public String getHoldingsDateRangeFromDateStandardDate() {
		return this.holdingsDateRangeFromDateStandardDate;
	}
	public void setHoldingsDateRangeFromDateStandardDate(
			String holdingsDateRangeFromDateStandardDate) {
		this.holdingsDateRangeFromDateStandardDate = holdingsDateRangeFromDateStandardDate;
	}
	public String getHoldingsDateRangeFromDateLang() {
		return this.holdingsDateRangeFromDateLang;
	}
	public void setHoldingsDateRangeFromDateLang(
			String holdingsDateRangeFromDateLang) {
		this.holdingsDateRangeFromDateLang = holdingsDateRangeFromDateLang;
	}
	public String getHoldingsDateRangeToDate() {
		return this.holdingsDateRangeToDate;
	}
	public void setHoldingsDateRangeToDate(String holdingsDateRangeToDate) {
		this.holdingsDateRangeToDate = holdingsDateRangeToDate;
	}
	public String getHoldingsDateRangeToDateNoAfter() {
		return this.holdingsDateRangeToDateNoAfter;
	}
	public void setHoldingsDateRangeToDateNoAfter(
			String holdingsDateRangeToDateNoAfter) {
		this.holdingsDateRangeToDateNoAfter = holdingsDateRangeToDateNoAfter;
	}
	public String getHoldingsDateRangeToDateNoBefore() {
		return this.holdingsDateRangeToDateNoBefore;
	}
	public void setHoldingsDateRangeToDateNoBefore(
			String holdingsDateRangeToDateNoBefore) {
		this.holdingsDateRangeToDateNoBefore = holdingsDateRangeToDateNoBefore;
	}
	public String getHoldingsDateRangeToDateStandardDate() {
		return this.holdingsDateRangeToDateStandardDate;
	}
	public void setHoldingsDateRangeToDateStandardDate(
			String holdingsDateRangeToDateStandardDate) {
		this.holdingsDateRangeToDateStandardDate = holdingsDateRangeToDateStandardDate;
	}
	public String getHoldingsDateRangeToDateLang() {
		return this.holdingsDateRangeToDateLang;
	}
	public void setHoldingsDateRangeToDateLang(
			String holdingsDateRangeToDateLang) {
		this.holdingsDateRangeToDateLang = holdingsDateRangeToDateLang;
	}
	public String getExtent() {
		return this.extent;
	}
	public void setExtent(String extent) {
		this.extent = extent;
	}
	public String getExtentUnit() {
		return this.extentUnit;
	}
	public void setExtentUnit(String extentUnit) {
		this.extentUnit = extentUnit;
	}
	public String getAgentLang() {
		return this.agentLang;
	}
	public void setAgentLang(String agentLang) {
		this.agentLang = agentLang;
	}
	public String getAgencyCode() {
		return this.agencyCode;
	}
	public void setAgencyCode(String agencyCode) {
		this.agencyCode = agencyCode;
	}
	public String getLanguage() {
		return this.language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public String getLanguageDeclaration() {
		return this.languageDeclaration;
	}

	public void setLanguageDeclaration(String languageDeclaration) {
		this.languageDeclaration = languageDeclaration;
	}

	public String getScript() {
		return this.script;
	}
	public void setScript(String script) {
		this.script = script;
	}
	public String getAbbreviation() {
		return this.abbreviation;
	}
	public void setAbbreviation(String abbreviation) {
		this.abbreviation = abbreviation;
	}
	public String getCitation() {
		return this.citation;
	}
	public void setCitation(String citation) {
		this.citation = citation;
	}
	public String getEagRelationType() {
		return this.eagRelationType;
	}
	public void setEagRelationType(String eagRelationType) {
		this.eagRelationType = eagRelationType;
	}
	public String getEagRelationHref() {
		return this.eagRelationHref;
	}
	public void setEagRelationHref(String eagRelationHref) {
		this.eagRelationHref = eagRelationHref;
	}
	public String getEagRelationLang() {
		return this.eagRelationLang;
	}
	public void setEagRelationLang(String eagRelationLang) {
		this.eagRelationLang = eagRelationLang;
	}
	public String getEagRelationrelationEntry() {
		return this.eagRelationrelationEntry;
	}
	public void setEagRelationrelationEntry(String eagRelationrelationEntry) {
		this.eagRelationrelationEntry = eagRelationrelationEntry;
	}
	public String getEagRelationrelationEntryLocalType() {
		return this.eagRelationrelationEntryLocalType;
	}
	public void setEagRelationrelationEntryLocalType(
			String eagRelationrelationEntryLocalType) {
		this.eagRelationrelationEntryLocalType = eagRelationrelationEntryLocalType;
	}
	public String getEagRelationrelationEntryScriptCode() {
		return this.eagRelationrelationEntryScriptCode;
	}
	public void setEagRelationrelationEntryScriptCode(
			String eagRelationrelationEntryScriptCode) {
		this.eagRelationrelationEntryScriptCode = eagRelationrelationEntryScriptCode;
	}
	public String getEagRelationrelationEntryTransliteration() {
		return this.eagRelationrelationEntryTransliteration;
	}
	public void setEagRelationrelationEntryTransliteration(
			String eagRelationrelationEntryTransliteration) {
		this.eagRelationrelationEntryTransliteration = eagRelationrelationEntryTransliteration;
	}
	public String getEagRelationrelationEntryLang() {
		return this.eagRelationrelationEntryLang;
	}
	public void setEagRelationrelationEntryLang(
			String eagRelationrelationEntryLang) {
		this.eagRelationrelationEntryLang = eagRelationrelationEntryLang;
	}
	public String getEagRelationrelationEntryDescription() {
		return this.eagRelationrelationEntryDescription;
	}

	public void setEagRelationrelationEntryDescription(
			String eagRelationrelationEntryDescription) {
		this.eagRelationrelationEntryDescription = eagRelationrelationEntryDescription;
	}
	public String getEagRelationrelationEntryDescriptionLang() {
		return this.eagRelationrelationEntryDescriptionLang;
	}
	public void setEagRelationrelationEntryDescriptionLang(String eagRelationrelationEntryDescriptionLang) {
		this.eagRelationrelationEntryDescriptionLang = eagRelationrelationEntryDescriptionLang;
	}
	/** Access and services **/
	public String getSearchRoomTelephone() {
		return searchRoomTelephone;
	}
	public void setSearchRoomTelephone(String searchRoomTelephone) {
		this.searchRoomTelephone = searchRoomTelephone;
	}
	public String getSearchRoomEmail() {
		return searchRoomEmail;
	}
	public void setSearchRoomEmail(String searchRoomEmail) {
		this.searchRoomEmail = searchRoomEmail;
	}
	public String getSearchRoomEmailLink() {
		return searchRoomEmailLink;
	}
	public void setSearchRoomEmailLink(String searchRoomEmailLink) {
		this.searchRoomEmailLink = searchRoomEmailLink;
	}
	public String getSearchRoomWebpage() {
		return searchRoomWebpage;
	}
	public void setSearchRoomWebpage(String searchRoomWebpage) {
		this.searchRoomWebpage = searchRoomWebpage;
	}
	public String getSearchRoomWebpageLink() {
		return searchRoomWebpageLink;
	}
	public void setSearchRoomWebpageLink(String searchRoomWebpageLink) {
		this.searchRoomWebpageLink = searchRoomWebpageLink;
	}
	public String getSearchRoomWorkPlaces() {
		return searchRoomWorkPlaces;
	}
	public void setSearchRoomWorkPlaces(String searchRoomWorkPlaces) {
		this.searchRoomWorkPlaces = searchRoomWorkPlaces;
	}
	public String getSearchRoomComputerPlaces() {
		return searchRoomComputerPlaces;
	}
	public void setSearchRoomComputerPlaces(String searchRoomComputerPlaces) {
		this.searchRoomComputerPlaces = searchRoomComputerPlaces;
	}
	public String getSearchRoomMicrofilmReaders() {
		return searchRoomMicrofilmReaders;
	}
	public void setSearchRoomMicrofilmReaders(String searchRoomMicrofilmReaders) {
		this.searchRoomMicrofilmReaders = searchRoomMicrofilmReaders;
	}
	public String getSearchRoomPhotographAllowance() {
		return searchRoomPhotographAllowance;
	}
	public void setSearchRoomPhotographAllowance(String searchRoomPhotographAllowance) {
		this.searchRoomPhotographAllowance = searchRoomPhotographAllowance;
	}
	public String getSearchRoomPhotographAllowanceContent() {
		return searchRoomPhotographAllowanceContent;
	}
	public void setSearchRoomPhotographAllowanceContent(String searchRoomPhotographAllowanceContent) {
		this.searchRoomPhotographAllowanceContent = searchRoomPhotographAllowanceContent;
	}
	public String getSearchRoomPhotographAllowanceHref() {
		return searchRoomPhotographAllowanceHref;
	}
	public void setSearchRoomPhotographAllowanceHref(String searchRoomPhotographAllowanceHref) {
		this.searchRoomPhotographAllowanceHref = searchRoomPhotographAllowanceHref;
	}
	public String getSearchRoomPhotographAllowanceLang() {
		return searchRoomPhotographAllowanceLang;
	}
	public void setSearchRoomPhotographAllowanceLang(String searchRoomPhotographAllowanceLang) {
		this.searchRoomPhotographAllowanceLang = searchRoomPhotographAllowanceLang;
	}
	public String getSearchRoomAdvancedOrdersContent() {
		return searchRoomAdvancedOrdersContent;
	}
	public void setSearchRoomAdvancedOrdersContent(String searchRoomAdvancedOrdersContent) {
		this.searchRoomAdvancedOrdersContent = searchRoomAdvancedOrdersContent;
	}
	public String getSearchRoomAdvancedOrdersLang() {
		return searchRoomAdvancedOrdersLang;
	}
	public void setSearchRoomAdvancedOrdersLang(String searchRoomAdvancedOrdersLang) {
		this.searchRoomAdvancedOrdersLang = searchRoomAdvancedOrdersLang;
	}
	public String getSearchRoomAdvancedOrdersHref() {
		return searchRoomAdvancedOrdersHref;
	}
	public void setSearchRoomAdvancedOrdersHref(String searchRoomAdvancedOrdersHref) {
		this.searchRoomAdvancedOrdersHref = searchRoomAdvancedOrdersHref;
	}
	public String getSearchRoomResearchServicesContent() {
		return searchRoomResearchServicesContent;
	}
	public void setSearchRoomResearchServicesContent(String searchRoomResearchServicesContent) {
		this.searchRoomResearchServicesContent = searchRoomResearchServicesContent;
	}
	public String getSearchRoomResearchServicesLang() {
		return searchRoomResearchServicesLang;
	}
	public void setSearchRoomResearchServicesLang(String searchRoomResearchServicesLang) {
		this.searchRoomResearchServicesLang = searchRoomResearchServicesLang;
	}
	public String getLibraryQuestion(){
		return this.libraryQuestion;
	}
	public void setLibraryQuestion(String libraryQuestion){
		this.libraryQuestion = libraryQuestion;
	}
	public String getLibraryTelephone() {
		return libraryTelephone;
	}
	public void setLibraryTelephone(String libraryTelephone) {
		this.libraryTelephone = libraryTelephone;
	}
	public String getLibraryEmailContent() {
		return libraryEmailContent;
	}
	public void setLibraryEmailContent(String libraryEmailContent) {
		this.libraryEmailContent = libraryEmailContent;
	}
	public String getLibraryEmailHref() {
		return libraryEmailHref;
	}
	public void setLibraryEmailHref(String libraryEmailHref) {
		this.libraryEmailHref = libraryEmailHref;
	}
	public String getLibraryWebpageContent() {
		return libraryWebpageContent;
	}
	public void setLibraryWebpageContent(String libraryWebpageContent) {
		this.libraryWebpageContent = libraryWebpageContent;
	}
	public String getLibraryWebpageHref() {
		return libraryWebpageHref;
	}
	public void setLibraryWebpageHref(String libraryWebpageHref) {
		this.libraryWebpageHref = libraryWebpageHref;
	}
	public String getLibraryMonographPublication() {
		return libraryMonographPublication;
	}
	public void setLibraryMonographPublication(String libraryMonographPublication) {
		this.libraryMonographPublication = libraryMonographPublication;
	}
	public String getLibrarySerialPublication() {
		return librarySerialPublication;
	}
	public void setLibrarySerialPublication(String librarySerialPublication) {
		this.librarySerialPublication = librarySerialPublication;
	}
	public String getLibraryInternetAccessQuestion() {
		return libraryInternetAccessQuestion;
	}
	public void setLibraryInternetAccessQuestion(String libraryInternetAccessQuestion) {
		this.libraryInternetAccessQuestion = libraryInternetAccessQuestion;
	}
	public String getLibraryDescription() {
		return libraryDescription;
	}
	public void setLibraryDescription(String libraryDescription) {
		this.libraryDescription = libraryDescription;
	}
	public String getLibraryDescriptionLang() {
		return libraryDescriptionLang;
	}
	public void setLibraryDescriptionLang(String libraryDescriptionLang) {
		this.libraryDescriptionLang = libraryDescriptionLang;
	}
	public String getTechnicalServicesQuestion() {
		return technicalServicesQuestion;
	}
	public void setTechnicalServicesQuestion(String technicalServicesQuestion) {
		this.technicalServicesQuestion = technicalServicesQuestion;
	}
	public String getTechnicalServicesDescription() {
		return technicalServicesDescription;
	}
	public void setTechnicalServicesDescription(String technicalServicesDescription) {
		this.technicalServicesDescription = technicalServicesDescription;
	}
	public String getTechnicalServicesDescriptionLang() {
		return technicalServicesDescriptionLang;
	}
	public void setTechnicalServicesDescriptionLang(String technicalServicesDescriptionLang) {
		this.technicalServicesDescriptionLang = technicalServicesDescriptionLang;
	}
	public String getTechnicalServicesTelephone() {
		return technicalServicesTelephone;
	}
	public void setTechnicalServicesTelephone(String technicalServicesTelephone) {
		this.technicalServicesTelephone = technicalServicesTelephone;
	}
	public String getTechnicalServicesEmail() {
		return technicalServicesEmail;
	}
	public void setTechnicalServicesEmail(String technicalServicesEmail) {
		this.technicalServicesEmail = technicalServicesEmail;
	}
	public String getTechnicalServicesEmailLink() {
		return technicalServicesEmailLink;
	}
	public void setTechnicalServicesEmailLink(String technicalServicesEmailLink) {
		this.technicalServicesEmailLink = technicalServicesEmailLink;
	}
	public String getTechnicalServicesEmailLang() {
		return technicalServicesEmailLang;
	}
	public void setTechnicalServicesEmailLang(String technicalServicesEmailLang) {
		this.technicalServicesEmailLang = technicalServicesEmailLang;
	}
	public String getTechnicalServicesWebpageLink() {
		return technicalServicesWebpageLink;
	}
	public void setTechnicalServicesWebpageLink(String technicalServicesWebpageLink) {
		this.technicalServicesWebpageLink = technicalServicesWebpageLink;
	}
	public String getTechnicalServicesWebpage() {
		return technicalServicesWebpage;
	}
	public void setTechnicalServicesWebpage(String technicalServicesWebpage) {
		this.technicalServicesWebpage = technicalServicesWebpage;
	}
	public String getTechnicalServicesWebpageLang() {
		return technicalServicesWebpageLang;
	}
	public void setTechnicalServicesWebpageLang(String technicalServicesWebpageLang) {
		this.technicalServicesWebpageLang = technicalServicesWebpageLang;
	}
	public String getReproductionserQuestion() {
		return reproductionserQuestion;
	}
	public void setReproductionserQuestion(String reproductionserQuestion) {
		this.reproductionserQuestion = reproductionserQuestion;
	}
	public String getReproductionserDescription() {
		return reproductionserDescription;
	}
	public void setReproductionserDescription(String reproductionserDescription) {
		this.reproductionserDescription = reproductionserDescription;
	}
	public String getReproductionserDescriptionLang() {
		return reproductionserDescriptionLang;
	}
	public void setReproductionserDescriptionLang(String reproductionserDescriptionLang) {
		this.reproductionserDescriptionLang = reproductionserDescriptionLang;
	}
	public String getReproductionserTelephone() {
		return reproductionserTelephone;
	}
	public void setReproductionserTelephone(String reproductionserTelephone) {
		this.reproductionserTelephone = reproductionserTelephone;
	}
	public String getReproductionserEmail() {
		return reproductionserEmail;
	}
	public void setReproductionserEmail(String reproductionserEmail) {
		this.reproductionserEmail = reproductionserEmail;
	}
	public String getReproductionserEmailLink() {
		return reproductionserEmailLink;
	}
	public void setReproductionserEmailLink(String reproductionserEmailLink) {
		this.reproductionserEmailLink = reproductionserEmailLink;
	}
	public String getReproductionserEmailLang() {
		return reproductionserEmailLang;
	}
	public void setReproductionserEmailLang(String reproductionserEmailLang) {
		this.reproductionserEmailLang = reproductionserEmailLang;
	}
	public String getReproductionserWebpage() {
		return reproductionserWebpage;
	}
	public void setReproductionserWebpage(String reproductionserWebpage) {
		this.reproductionserWebpage = reproductionserWebpage;
	}
	public String getReproductionserWebpageLink() {
		return reproductionserWebpageLink;
	}
	public void setReproductionserWebpageLink(String reproductionserWebpageLink) {
		this.reproductionserWebpageLink = reproductionserWebpageLink;
	}
	public String getReproductionserWebpageLang() {
		return reproductionserWebpageLang;
	}
	public void setReproductionserWebpageLang(String reproductionserWebpageLang) {
		this.reproductionserWebpageLang = reproductionserWebpageLang;
	}
	public String getMicrofilmServices() {
		return microfilmServices;
	}
	public void setMicrofilmServices(String microfilmServices) {
		this.microfilmServices = microfilmServices;
	}
	public String getPhotographicServices() {
		return photographicServices;
	}
	public void setPhotographicServices(String photographicServices) {
		this.photographicServices = photographicServices;
	}
	public String getDigitisationServices() {
		return digitisationServices;
	}
	public void setDigitisationServices(String digitisationServices) {
		this.digitisationServices = digitisationServices;
	}
	public String getPhotocopyingServices() {
		return photocopyingServices;
	}
	public void setPhotocopyingServices(String photocopyingServices) {
		this.photocopyingServices = photocopyingServices;
	}
	public String getRecreationalServicesRefreshmentArea() {
		return recreationalServicesRefreshmentArea;
	}
	public void setRecreationalServicesRefreshmentArea(String recreationalServicesRefreshmentArea) {
		this.recreationalServicesRefreshmentArea = recreationalServicesRefreshmentArea;
	}
	public String getRecreationalServicesRefreshmentAreaLang() {
		return recreationalServicesRefreshmentAreaLang;
	}
	public void setRecreationalServicesRefreshmentAreaLang(String recreationalServicesRefreshmentAreaLang) {
		this.recreationalServicesRefreshmentAreaLang = recreationalServicesRefreshmentAreaLang;
	}
	public String getRecreationalServicesExhibition() {
		return recreationalServicesExhibition;
	}
	public void setRecreationalServicesExhibition(String recreationalServicesExhibition) {
		this.recreationalServicesExhibition = recreationalServicesExhibition;
	}
	public String getRecreationalServicesExhibitionLang() {
		return recreationalServicesExhibitionLang;
	}
	public void setRecreationalServicesExhibitionLang(String recreationalServicesExhibitionLang) {
		this.recreationalServicesExhibitionLang = recreationalServicesExhibitionLang;
	}
	public String getRecreationalServicesWeb() {
		return recreationalServicesWeb;
	}
	public void setRecreationalServicesWeb(String recreationalServicesWeb) {
		this.recreationalServicesWeb = recreationalServicesWeb;
	}
	public String getRecreationalServicesWebLink() {
		return recreationalServicesWebLink;
	}
	public void setRecreationalServicesWebLink(String recreationalServicesWebLink) {
		this.recreationalServicesWebLink = recreationalServicesWebLink;
	}
	public String getToursSessionGuidesAndSessionsContent() {
		return toursSessionGuidesAndSessionsContent;
	}
	public void setToursSessionGuidesAndSessionsContent(String toursSessionGuidesAndSessionsContent) {
		this.toursSessionGuidesAndSessionsContent = toursSessionGuidesAndSessionsContent;
	}
	public String getToursSessionGuidesAndSessionsLang() {
		return toursSessionGuidesAndSessionsLang;
	}
	public void setToursSessionGuidesAndSessionsLang(String toursSessionGuidesAndSessionsLang) {
		this.toursSessionGuidesAndSessionsLang = toursSessionGuidesAndSessionsLang;
	}
	public String getToursSessionGuidesAndSessionsWebpage() {
		return toursSessionGuidesAndSessionsWebpage;
	}
	public void setToursSessionGuidesAndSessionsWebpage(String toursSessionGuidesAndSessionsWebpage) {
		this.toursSessionGuidesAndSessionsWebpage = toursSessionGuidesAndSessionsWebpage;
	}
	public String getToursSessionGuidesAndSessionsWebpageTitle() {
		return toursSessionGuidesAndSessionsWebpageTitle;
	}
	public void setToursSessionGuidesAndSessionsWebpageTitle(String toursSessionGuidesAndSessionsWebpageTitle) {
		this.toursSessionGuidesAndSessionsWebpageTitle = toursSessionGuidesAndSessionsWebpageTitle;
	}
	public String getOtherServices() {
		return otherServices;
	}
	public void setOtherServices(String otherServices) {
		this.otherServices = otherServices;
	}
	public String getOtherServicesLang() {
		return otherServicesLang;
	}
	public void setOtherServicesLang(String otherServicesLang) {
		this.otherServicesLang = otherServicesLang;
	}
	public String getOtherServicesWebpage() {
		return otherServicesWebpage;
	}
	public void setOtherServicesWebpage(String otherServicesWebpage) {
		this.otherServicesWebpage = otherServicesWebpage;
	}
	public String getOtherServicesLink() {
		return otherServicesLink;
	}
	public void setOtherServicesLink(String otherServicesLink) {
		this.otherServicesLink = otherServicesLink;
	}
	
	public String editWebFormEAG2012() throws Exception {
		if(eagPath!=null && !eagPath.isEmpty() && aiId!=null){
			try{
				new EAG2012Loader(aiId);
			}catch(Exception e){
				log.error("ERROR trying to put EAG parameters to webFormEag2012");
			}
		}else{
			log.warn("aiId or eagPath are wrong");
		}
		return "success";
	}
	
	public void setEagPath(String eagPath){
		this.eagPath = eagPath;
	}

	/**
	 * Input method.
	 */
	public boolean loadValuesEAG2012() {
		if (this.eag.getControl() == null && this.eag.getArchguide() == null
				&& this.eag.getRelations() == null) {
			this.setCountryCode(new ArchivalLandscape().getmyCountry());
			this.setRecordId(Eag2012.generatesISOCode(this.getId()));
			return false;
		}

		// Fill values of "Your institution" tab.
		loadYourinstitutionTabValues();
		// Fill values of "Identity" tab.
		loadIdentityTabValues();
		// Fill values of "Contact" tab.
		loadContactTabValues();
		// Fill values of "Access and Services" tab.
		loadAccessAndServicesTabValues();
		// Fill values of "Description" tab.
		loadDescriptionTabValues();
		// Fill values of "Control" tab.
		loadControlTabValues();
		// Fill values of "Relations" tab.
		loadRelationsTabValues();

		return true;
	}

	/**
	 * Method to load all values of "Your institution" tab.
	 */
	private void loadYourinstitutionTabValues() {
		// Person/institution responsible for the description.
		if (!this.eag.getControl().getMaintenanceHistory().getMaintenanceEvent().isEmpty()) {
			if (this.eag.getControl().getMaintenanceHistory().getMaintenanceEvent().get(this.eag.getControl().getMaintenanceHistory().getMaintenanceEvent().size()-1).getAgent() != null) {
				this.setAgent(this.eag.getControl().getMaintenanceHistory().getMaintenanceEvent().get(this.eag.getControl().getMaintenanceHistory().getMaintenanceEvent().size()-1).getAgent().getContent());
			}
		}

		// Country code.
		if (this.eag.getArchguide().getIdentity().getRepositorid() != null
				&& this.eag.getArchguide().getIdentity().getRepositorid().getCountrycode() !=  null
				&& !this.eag.getArchguide().getIdentity().getRepositorid().getCountrycode().isEmpty()) {
			this.setCountryCode(this.eag.getArchguide().getIdentity().getRepositorid().getCountrycode());
		} else {
			this.setCountryCode(new ArchivalLandscape().getmyCountry());
		}

		// Identifier of the institution.
		if (this.eag.getArchguide().getIdentity().getOtherRepositorId() != null) {
			this.setOtherRepositorId(this.eag.getArchguide().getIdentity().getOtherRepositorId().getContent());
			if (this.eag.getControl().getRecordId() != null) {
				if (this.eag.getArchguide().getIdentity().getOtherRepositorId().getContent().equalsIgnoreCase(this.eag.getControl().getRecordId().getValue())) {
					this.setRecordIdISIL(Eag2012.OPTION_YES);
					this.setRecordId(this.eag.getControl().getRecordId().getValue());
				} else {
					this.setRecordIdISIL(Eag2012.OPTION_NO);
					this.setRecordId(this.getRecordId());
					this.setSelfRecordId(this.getIdUsedInAPE());
				}
			}
				
		} else {
			// Load the recordId value
			this.setOtherRepositorId(this.eag.getControl().getRecordId().getValue());
			this.setRecordIdISIL(Eag2012.OPTION_YES);
		}

		// ID used in APE.
		if (this.eag.getControl().getRecordId() != null) {
			this.setRecordId(this.eag.getControl().getRecordId().getValue());
		}

		// Name of the institution.
		if (!this.eag.getArchguide().getIdentity().getAutform().isEmpty()) {
			for (int i = 0; i < this.eag.getArchguide().getIdentity().getAutform().size(); i++) {
				Autform autform = this.eag.getArchguide().getIdentity().getAutform().get(i);
				if (autform!=null && autform.getLang()!=null && autform.getLang().equalsIgnoreCase(this.getCountryCode())) {
					this.setAutform(autform.getContent());
					this.setAutformLang((autform.getLang()!=null)?new Locale(LanguageConverter.get639_1Code(autform.getLang())).getISO3Language():null);
				}
			}
			if (this.getAutform() == null || this.getAutform() == "") {
				this.setAutform(this.eag.getArchguide().getIdentity().getAutform().get(0).getContent());
				String lang = this.eag.getArchguide().getIdentity().getAutform().get(0).getLang();
				this.setAutformLang(lang);
//				this.setAutformLang((lang!=null)?new Locale(LanguageConverter.get639_1Code(lang)).getISO3Country():lang);
			}
		}

		// Parallel name of the institution.
		if (!this.eag.getArchguide().getIdentity().getParform().isEmpty()) {
			for (int i = 0; i < this.eag.getArchguide().getIdentity().getParform().size(); i++) {
				Parform parform = this.eag.getArchguide().getIdentity().getParform().get(i);
//				if (parform.getLang().equalsIgnoreCase(this.getCountryCode())) {
					this.setParform(parform.getContent());
					this.setParformLang(parform.getLang());
//				}
			}
			if (this.getParform() == null || this.getParform() == "") {
				this.setParform(this.eag.getArchguide().getIdentity().getParform().get(0).getContent());
				this.setParformLang(this.eag.getArchguide().getIdentity().getParform().get(0).getLang());
			}
		}

		// Institution info.
		if (this.eag.getArchguide().getDesc().getRepositories() != null) {
			if (!this.eag.getArchguide().getDesc().getRepositories().getRepository().isEmpty()) {
				// First repository equals institution.
				Repository repository = this.eag.getArchguide().getDesc().getRepositories().getRepository().get(0);

				// Visitor & Postal address.
				if (!repository.getLocation().isEmpty()) {
					for (int i = 0; i < repository.getLocation().size(); i++) {
						Location location = repository.getLocation().get(i);
						// TODO: Review for multiple translations.
						if (location.getLocalType().equalsIgnoreCase(Eag2012.VISITORS_ADDRESS)) {
							// Street.
							this.setStreet(location.getStreet().getContent());
							this.setStreetLang(location.getStreet().getLang());
							// City.
							this.setMunicipalityPostalcode(location.getMunicipalityPostalcode().getContent());
							this.setMunicipalityPostalcodeLang(location.getStreet().getLang());
							// Country.
							this.setCountry(location.getCountry().getContent());
							this.setCountryLang(location.getCountry().getLang());
							// Latitude.
							this.setLatitude(location.getLatitude());
							// Longitude.
							this.setLongitude(location.getLongitude());
						}
						// TODO: Review for multiple translations.
						if (location.getLocalType().equalsIgnoreCase(Eag2012.POSTAL_ADDRESS)) {
							// Postal street.
							this.setStreetPostal(location.getStreet().getContent());
							this.setStreetPostalLang(location.getStreet().getLang());
							// Postal city.
							this.setMunicipalityPostalcodePostal(location.getMunicipalityPostalcode().getContent());
							this.setMunicipalityPostalcodePostalLang(location.getStreet().getLang());
						}
					}
				}

				// Continent.
				if (repository.getGeogarea() != null) {
					String geogareaValue = repository.getGeogarea().getValue();

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
					
					this.setGeogarea(geogareaValue);
				}

				// Telephone.
				if (!repository.getTelephone().isEmpty()) {
					// TODO: Review for multiple values.
					for (int i = 0; i < repository.getTelephone().size(); i++) {
						this.setTelephone(repository.getTelephone().get(i).getContent());
					}
				}

				// E-mail address.
				if (!repository.getEmail().isEmpty()) {
					// TODO: Review for multiple values.
					for (int i = 0; i < repository.getEmail().size(); i++) {
						this.setEmail(repository.getEmail().get(i).getHref());
						this.setEmailTitle(repository.getEmail().get(i).getContent());
						this.setEmailLang(repository.getEmail().get(i).getLang());
					}
				}

				// Webpage.
				if (!repository.getWebpage().isEmpty()) {
					// TODO: Review for multiple values.
					for (int i = 0; i < repository.getWebpage().size(); i++) {
						this.setWebpage(repository.getWebpage().get(i).getHref());
						this.setWebpageTitle(repository.getWebpage().get(i).getContent());
						this.setWebpageLang(repository.getWebpage().get(i).getLang());
					}
				}

				// Timetable info
				if (repository.getTimetable() != null) {
					Timetable timetable = repository.getTimetable();

					// Opening times.
					if (!timetable.getOpening().isEmpty()) {
						// TODO: Review for multiple values.
						for (int i = 0; i < timetable.getOpening().size(); i++) {
							this.setOpening(timetable.getOpening().get(i).getContent());
							this.setOpeningLang(timetable.getOpening().get(i).getLang());
						}
					}

					// Closing dates.
					if (!timetable.getClosing().isEmpty()) {
						// TODO: Review for multiple values.
						for (int i = 0; i < timetable.getClosing().size(); i++) {
							this.setClosing(timetable.getClosing().get(i).getContent());
							this.setClosingLang(timetable.getClosing().get(i).getLang());
						}
					}
				}

				// Accessible to the public.
				if (repository.getAccess() != null) {
					this.setAccessQuestion(repository.getAccess().getQuestion());
					if (!repository.getAccess().getRestaccess().isEmpty()) {
						// TODO: Review for multiple values.
						for (int i = 0; i < repository.getAccess().getRestaccess().size(); i++) {
							this.setRestaccess(repository.getAccess().getRestaccess().get(i).getContent());
							this.setRestaccessLang(repository.getAccess().getRestaccess().get(i).getLang());
						}
					}
				}

				// Facilities for disabled people available.
				if (!repository.getAccessibility().isEmpty()) {
					// TODO: Review for multiple values.
					for (int i = 0; i < repository.getAccessibility().size(); i++) {
						this.setAccessibilityQuestion(repository.getAccessibility().get(i).getQuestion());
						this.setAccessibility(repository.getAccessibility().get(i).getContent());
						this.setAccessibilityLang(repository.getAccessibility().get(i).getLang());
					}
				}
			}
		}

		// Reference to your institution’s holdings guide.
		if (!this.eag.getRelations().getResourceRelation().isEmpty()) {
			// TODO: Review for multiple values.
			for (int i = 0; i < this.eag.getRelations().getResourceRelation().size(); i++) {
				this.setResourceRelationHref(this.eag.getRelations().getResourceRelation().get(i).getHref());
				if (this.eag.getRelations().getResourceRelation().get(i).getRelationEntry() != null) {
					this.setResourceRelationrelationEntry(this.eag.getRelations().getResourceRelation().get(i).getRelationEntry().getContent());
					this.setResourceRelationLang(this.eag.getRelations().getResourceRelation().get(i).getRelationEntry().getLang());
				}
			}
		}
	}

	/**
	 * Method to load all values of "Identity" tab.
	 */
	private void loadIdentityTabValues() {
		// Select type of institution.
		if (!this.eag.getArchguide().getIdentity().getRepositoryType().isEmpty()) {
			for (int i = 0; i < this.eag.getArchguide().getIdentity().getRepositoryType().size(); i++) {
				String value = this.eag.getArchguide().getIdentity().getRepositoryType().get(i).getValue();

				if (Eag2012.OPTION_NATIONAL_TEXT.equalsIgnoreCase(value)) {
					value = Eag2012.OPTION_NATIONAL;
				} else if (Eag2012.OPTION_REGIONAL_TEXT.equalsIgnoreCase(value)) {
					value = Eag2012.OPTION_REGIONAL;
				} else if (Eag2012.OPTION_COUNTY_TEXT.equalsIgnoreCase(value)) {
					value = Eag2012.OPTION_COUNTY;
				} else if (Eag2012.OPTION_MUNICIPAL_TEXT.equalsIgnoreCase(value)) {
					value = Eag2012.OPTION_MUNICIPAL;
				} else if (Eag2012.OPTION_SPECIALISED_TEXT.equalsIgnoreCase(value)) {
					value = Eag2012.OPTION_SPECIALISED;
				} else if (Eag2012.OPTION_PRIVATE_TEXT.equalsIgnoreCase(value)) {
					value = Eag2012.OPTION_PRIVATE;
				} else if (Eag2012.OPTION_CHURCH_TEXT.equalsIgnoreCase(value)) {
					value = Eag2012.OPTION_CHURCH;
				} else if (Eag2012.OPTION_BUSINESS_TEXT.equalsIgnoreCase(value)) {
					value = Eag2012.OPTION_BUSINESS;
				} else if (Eag2012.OPTION_UNIVERSITY_TEXT.equalsIgnoreCase(value)) {
					value = Eag2012.OPTION_UNIVERSITY;
				} else if (Eag2012.OPTION_MEDIA_TEXT.equalsIgnoreCase(value)) {
					value = Eag2012.OPTION_MEDIA;
				} else if (Eag2012.OPTION_POLITICAL_TEXT.equalsIgnoreCase(value)) {
					value = Eag2012.OPTION_POLITICAL;
				} else if (Eag2012.OPTION_CULTURAL_TEXT.equalsIgnoreCase(value)) {
					value = Eag2012.OPTION_CULTURAL;
				}

				this.getRepositoryType().add(value);
			}
		}
	}

	/**
	 * Method to load all values of "Contact" tab.
	 */
	private void loadContactTabValues() {
		// Repositories info.
		if (this.eag.getArchguide().getDesc().getRepositories() != null) {
			if (!this.eag.getArchguide().getDesc().getRepositories().getRepository().isEmpty()) {
				// TODO: Review for multiple repositories.
				// First repository equals institution.
				Repository repository = this.eag.getArchguide().getDesc().getRepositories().getRepository().get(0);

				// Visitor & Postal address.
				if (!repository.getLocation().isEmpty()) {
					for (int i = 0; i < repository.getLocation().size(); i++) {
						Location location = repository.getLocation().get(i);
						// TODO: Review for multiple translations.
						if (location.getLocalType().equalsIgnoreCase(Eag2012.VISITORS_ADDRESS)) {
							// Street.
							this.setStreet(location.getStreet().getContent());
							this.setStreetLang(location.getStreet().getLang());
							// City.
							this.setMunicipalityPostalcode(location.getMunicipalityPostalcode().getContent());
							this.setMunicipalityPostalcodeLang(location.getStreet().getLang());
							// District/quarter in town.
							if (location.getLocalentity() != null) {
								this.setLocalentity(location.getLocalentity().getContent());
								this.setLocalentityLang(location.getLocalentity().getLang());
							}
							//County/local authority.
							if (location.getSecondem() != null) {
								this.setSecondem(location.getSecondem().getContent());
								this.setSecondemLang(location.getSecondem().getLang());
							}
							// Autonomous community / region.
							if (location.getFirstdem() != null) {
								this.setFirstdem(location.getFirstdem().getContent());
								this.setFirstdemLang(location.getFirstdem().getLang());
							}
							// Country.
							this.setCountry(location.getCountry().getContent());
							this.setCountryLang(location.getCountry().getLang());
							// Latitude.
							this.setLatitude(location.getLatitude());
							// Longitude.
							this.setLongitude(location.getLongitude());
						}
						// TODO: Review for multiple translations.
						if (location.getLocalType().equalsIgnoreCase(Eag2012.POSTAL_ADDRESS)) {
							// Postal street.
							this.setStreetPostal(location.getStreet().getContent());
							this.setStreetPostalLang(location.getStreet().getLang());
							// Postal city.
							this.setMunicipalityPostalcodePostal(location.getMunicipalityPostalcode().getContent());
							this.setMunicipalityPostalcodePostalLang(location.getStreet().getLang());
						}
					}

					// Fax.
					if (!repository.getFax().isEmpty()) {
						// TODO: Review for multiple values.
						for (int i = 0; i < repository.getFax().size(); i++) {
							this.setFax(repository.getFax().get(i).getContent());
						}
					}

					// E-mail address.
					if (!repository.getEmail().isEmpty()) {
						// TODO: Review for multiple values.
						for (int i = 0; i < repository.getEmail().size(); i++) {
							this.setEmail(repository.getEmail().get(i).getHref());
							this.setEmailTitle(repository.getEmail().get(i).getContent());
							this.setEmailLang(repository.getEmail().get(i).getLang());
						}
					}

					// Webpage.
					if (!repository.getWebpage().isEmpty()) {
						// TODO: Review for multiple values.
						for (int i = 0; i < repository.getWebpage().size(); i++) {
							this.setWebpage(repository.getWebpage().get(i).getHref());
							this.setWebpageTitle(repository.getWebpage().get(i).getContent());
							this.setWebpageLang(repository.getWebpage().get(i).getLang());
						}
					}
				}
			}
		}
		


	}

	/**
	 * Method to load all values of "Access and Services" tab.
	 */
	private void loadAccessAndServicesTabValues() {
		// Repositories info.
		if (this.eag.getArchguide().getDesc().getRepositories() != null) {
			if (!this.eag.getArchguide().getDesc().getRepositories().getRepository().isEmpty()) {
				// TODO: Review for multiple repositories.
				// First repository equals institution.
				Repository repository = this.eag.getArchguide().getDesc().getRepositories().getRepository().get(0);

				// Travelling directions.
				if (!repository.getDirections().isEmpty()) {
					// TODO: Review for multiple values.
					for (int i = 0; i < repository.getDirections().size(); i++) {
						// Travelling directions.
						if (!repository.getDirections().get(i).getContent().isEmpty()) {
							// TODO: Review for multiple values.
							for (int j = 0; j < repository.getDirections().get(i).getContent().size(); j++) {
								if (repository.getDirections().get(i).getContent().get(j) instanceof String
										&& repository.getDirections().get(i).getContent().get(j) != null
										&& !repository.getDirections().get(i).getContent().get(j).toString().isEmpty()
										&& !repository.getDirections().get(i).getContent().get(j).toString().startsWith("\n")) {
									this.setDirections(repository.getDirections().get(i).getContent().get(j).toString());
								}
								if (repository.getDirections().get(i).getContent().get(j) instanceof Citation) {
									Citation citation = (Citation) repository.getDirections().get(i).getContent().get(j);
									this.setDirectionsCitationHref(citation.getHref());
								}
							}
						}
						// Travelling directions language.
						this.setDirectionsLang(repository.getDirections().get(i).getLang());
					}
				}

				// Terms of use.
				if (repository.getAccess() != null) {
					if (!repository.getAccess().getTermsOfUse().isEmpty()) {
						// TODO: Review for multiple values.
						for (int i = 0; i < repository.getAccess().getTermsOfUse().size(); i++) {
							this.setTermsOfUse(repository.getAccess().getTermsOfUse().get(i).getContent());
							this.setTermsOfUseLang(repository.getAccess().getTermsOfUse().get(i).getLang());
							this.setTermsOfUseHref(repository.getAccess().getTermsOfUse().get(i).getHref());
						}
					}
				}
				
				// Searchroom
				if(repository.getServices().getSearchroom()!=null){
					Searchroom searchRoom = repository.getServices().getSearchroom();
					if(searchRoom.getContact()!=null){
						if(searchRoom.getContact().getTelephone()!=null){
							for (int i = 0; i < searchRoom.getContact().getTelephone().size(); i++) {
								this.setSearchRoomTelephone(searchRoom.getContact().getTelephone().get(i).getContent());
							}
						}
						if(searchRoom.getContact().getEmail()!=null){
							for (int i = 0; i < searchRoom.getContact().getEmail().size(); i++) {
								this.setSearchRoomEmail(searchRoom.getContact().getEmail().get(i).getHref());
								this.setSearchRoomEmailLink(searchRoom.getContact().getEmail().get(i).getContent());
							}
						}
					}
					if(searchRoom.getWebpage()!=null){
						for (int i = 0; i < searchRoom.getWebpage().size(); i++) {
							this.setSearchRoomWebpage(searchRoom.getWebpage().get(i).getHref());
							this.setSearchRoomWebpageLink(searchRoom.getWebpage().get(i).getContent());
						}
					}
					if(searchRoom.getWorkPlaces()!=null && searchRoom.getWorkPlaces().getNum()!=null){
						this.setSearchRoomWorkPlaces(searchRoom.getWorkPlaces().getNum().getContent());
					}
					if(searchRoom.getComputerPlaces()!=null && searchRoom.getComputerPlaces().getDescriptiveNote()!=null){
						for (int i = 0; i < searchRoom.getComputerPlaces().getDescriptiveNote().getP().size(); i++) {
							this.setSearchRoomComputerPlaces(searchRoom.getComputerPlaces().getDescriptiveNote().getP().get(i).getContent());
						}
					}
					if(searchRoom.getMicrofilmPlaces()!=null && searchRoom.getMicrofilmPlaces().getNum()!=null){
						this.setSearchRoomMicrofilmReaders(searchRoom.getMicrofilmPlaces().getNum().getContent());
					}
					if(searchRoom.getPhotographAllowance()!=null){
						this.setSearchRoomPhotographAllowance(searchRoom.getPhotographAllowance().getValue());
					}
					if(searchRoom.getReadersTicket()!=null){
						for (int i = 0; i < searchRoom.getReadersTicket().size(); i++) {
							this.setSearchRoomPhotographAllowanceContent(searchRoom.getReadersTicket().get(i).getContent());
							this.setSearchRoomPhotographAllowanceHref(searchRoom.getReadersTicket().get(i).getHref());
							this.setSearchRoomPhotographAllowanceLang(searchRoom.getReadersTicket().get(i).getLang());
						}
					}
					if(searchRoom.getAdvancedOrders()!=null){
						for (int i = 0; i < searchRoom.getAdvancedOrders().size(); i++) {
							this.setSearchRoomAdvancedOrdersContent(searchRoom.getAdvancedOrders().get(i).getContent());
							this.setSearchRoomAdvancedOrdersLang(searchRoom.getAdvancedOrders().get(i).getLang());
							this.setSearchRoomAdvancedOrdersHref(searchRoom.getAdvancedOrders().get(i).getHref());
						}
					}
					if(searchRoom.getResearchServices()!=null){
						for (int i = 0; i < searchRoom.getResearchServices().size(); i++) {
							if(searchRoom.getResearchServices().get(i).getDescriptiveNote()!=null){
								for(int j = 0; j < searchRoom.getResearchServices().get(i).getDescriptiveNote().getP().size(); j++) {
									this.setSearchRoomResearchServicesContent(searchRoom.getResearchServices().get(i).getDescriptiveNote().getP().get(j).getContent());
									this.setSearchRoomResearchServicesLang(searchRoom.getResearchServices().get(i).getDescriptiveNote().getP().get(j).getLang());
								}
							}
						}
					}
				}
				
				// Library
				Library library = repository.getServices().getLibrary();
				this.setLibraryQuestion(library.getQuestion());
				if(library.getContact() != null){
					if(library.getContact().getTelephone()!=null){
						for (int i = 0; i < library.getContact().getTelephone().size(); i++) {
							this.setLibraryTelephone(library.getContact().getTelephone().get(i).getContent());
						}
					}
					if(library.getContact().getEmail()!=null){
						for (int i = 0; i < library.getContact().getEmail().size(); i++) {
							this.setLibraryEmailContent(library.getContact().getEmail().get(i).getContent());
							this.setLibraryEmailHref(library.getContact().getEmail().get(i).getHref());
						}
					}
					if(library.getWebpage()!=null){
						for (int i = 0; i < library.getWebpage().size(); i++) {
							this.setLibraryWebpageContent(library.getWebpage().get(i).getContent());
							this.setLibraryWebpageHref(library.getWebpage().get(i).getHref());
						}
					}
				}
				if(library.getMonographicpub()!=null){
					this.setLibraryMonographPublication(library.getMonographicpub().getNum().getContent());
				}
				if(library.getSerialpub()!=null){
					this.setLibrarySerialPublication(library.getSerialpub().getNum().getContent());
				}
				List<P> ps = null;
				if(repository.getServices().getInternetAccess()!=null){
					this.setLibraryInternetAccessQuestion(repository.getServices().getInternetAccess().getQuestion());
					if(repository.getServices().getInternetAccess().getDescriptiveNote()!=null){
						ps = repository.getServices().getInternetAccess().getDescriptiveNote().getP();
						for (int i = 0; i < ps.size(); i++) {
							this.setLibraryDescription(ps.get(i).getContent());
							this.setLibraryDescriptionLang(ps.get(i).getLang());
						}
					}
				}
				
				// Technical Services
				this.setTechnicalServicesQuestion(repository.getServices().getTechservices().getRestorationlab().getQuestion());
				if(repository.getServices().getTechservices().getRestorationlab().getDescriptiveNote()!=null){
					ps = repository.getServices().getTechservices().getRestorationlab().getDescriptiveNote().getP();
					for (int i = 0; i < ps.size(); i++) {
						this.setTechnicalServicesDescription(ps.get(i).getContent());
						this.setTechnicalServicesDescriptionLang(ps.get(i).getLang());
					}
				}
				List<Telephone> telephones = null;
				List<Email> emails = null;
				List<Webpage> webpages = null;
				if(repository.getServices().getTechservices().getRestorationlab()!=null){
					if(repository.getServices().getTechservices().getRestorationlab().getContact()!=null){
						telephones = repository.getServices().getTechservices().getRestorationlab().getContact().getTelephone();
						for (int i = 0; i < telephones.size(); i++) {
							this.setTechnicalServicesTelephone(telephones.get(i).getContent());
						}
						emails = repository.getServices().getTechservices().getRestorationlab().getContact().getEmail();
						for (int i = 0; i < emails.size(); i++) {
							this.setTechnicalServicesEmail(emails.get(i).getHref());
							this.setTechnicalServicesEmailLink(emails.get(i).getContent());
							this.setTechnicalServicesEmailLang(emails.get(i).getLang());
						}
					}
					if(repository.getServices().getTechservices().getRestorationlab().getWebpage()!=null){
						webpages = repository.getServices().getTechservices().getRestorationlab().getWebpage();
						for (int i = 0; i < webpages.size(); i++) {
							this.setTechnicalServicesWebpageLink(webpages.get(i).getContent());
							this.setTechnicalServicesWebpage(webpages.get(i).getHref());
							this.setTechnicalServicesWebpageLang(webpages.get(i).getLang());
						}
					}
				}
				if(repository.getServices().getTechservices().getReproductionser()!=null){
					this.setReproductionserQuestion(repository.getServices().getTechservices().getReproductionser().getQuestion());
					if(repository.getServices().getTechservices().getReproductionser().getDescriptiveNote()!=null){
						ps = repository.getServices().getTechservices().getReproductionser().getDescriptiveNote().getP();
						for (int i = 0; i < ps.size(); i++) {
							this.setReproductionserDescription(ps.get(i).getContent());
							this.setReproductionserDescriptionLang(ps.get(i).getLang());
						}
					}
					if(repository.getServices().getTechservices().getReproductionser().getContact()!=null){
						telephones = repository.getServices().getTechservices().getReproductionser().getContact().getTelephone();
						for (int i = 0; i < telephones.size(); i++) {
							this.setReproductionserTelephone(telephones.get(i).getContent());
						}
						emails = repository.getServices().getTechservices().getReproductionser().getContact().getEmail();
						for (int i = 0; i < emails.size(); i++) {
							this.setReproductionserEmail(emails.get(i).getHref());
							this.setReproductionserEmailLink(emails.get(i).getContent());
							this.setReproductionserEmailLang(emails.get(i).getLang());
						}
						webpages = repository.getServices().getTechservices().getReproductionser().getWebpage();
						for (int i = 0; i < webpages.size(); i++) {
							this.setReproductionserWebpageLink(webpages.get(i).getContent());
							this.setReproductionserWebpage(webpages.get(i).getHref());
							this.setReproductionserWebpageLang(webpages.get(i).getLang());
						}
					}
					if(repository.getServices().getTechservices().getReproductionser()!=null){
						if(repository.getServices().getTechservices().getReproductionser().getMicroformser()!=null){
							this.setMicrofilmServices(repository.getServices().getTechservices().getReproductionser().getMicroformser().getQuestion());
						}
						if(repository.getServices().getTechservices().getReproductionser().getPhotographser()!=null){
							this.setPhotographicServices(repository.getServices().getTechservices().getReproductionser().getPhotographser().getQuestion());
						}
						if(repository.getServices().getTechservices().getReproductionser().getDigitalser()!=null){
							this.setDigitisationServices(repository.getServices().getTechservices().getReproductionser().getDigitalser().getQuestion());
						}
						if(repository.getServices().getTechservices().getReproductionser().getPhotocopyser()!=null){
							this.setPhotocopyingServices(repository.getServices().getTechservices().getReproductionser().getPhotocopyser().getQuestion());
						}
					}
				}
				
				// Recreational services
				RecreationalServices recreationalServices = repository.getServices().getRecreationalServices();
				if(recreationalServices!=null){
					if(recreationalServices.getRefreshment()!=null && recreationalServices.getRefreshment().getDescriptiveNote()!=null){
						for (int i = 0; i < recreationalServices.getRefreshment().getDescriptiveNote().getP().size(); i++) {
							this.setRecreationalServicesRefreshmentArea(recreationalServices.getRefreshment().getDescriptiveNote().getP().get(i).getContent());
							this.setRecreationalServicesRefreshmentAreaLang(recreationalServices.getRefreshment().getDescriptiveNote().getP().get(i).getLang());
						}
					}
					if(recreationalServices.getExhibition()!=null){
						for (int i = 0; i < recreationalServices.getExhibition().size(); i++) {
							for (int j = 0; j < recreationalServices.getExhibition().get(i).getDescriptiveNote().getP().size(); j++) {
								this.setRecreationalServicesExhibition(recreationalServices.getExhibition().get(i).getDescriptiveNote().getP().get(j).getContent());
								this.setRecreationalServicesExhibitionLang(recreationalServices.getExhibition().get(i).getDescriptiveNote().getP().get(j).getLang());
							}
							this.setRecreationalServicesWeb(recreationalServices.getExhibition().get(i).getWebpage().getHref());
							this.setRecreationalServicesWebLink(recreationalServices.getExhibition().get(i).getWebpage().getContent());
						}
					}
					if(recreationalServices.getToursSessions()!=null){
						for (int i = 0; i < recreationalServices.getToursSessions().size(); i++) {
							for (int j = 0; j < recreationalServices.getToursSessions().get(i).getDescriptiveNote().getP().size(); j++) {
								this.setToursSessionGuidesAndSessionsContent(recreationalServices.getToursSessions().get(i).getDescriptiveNote().getP().get(j).getContent());
								this.setToursSessionGuidesAndSessionsLang(recreationalServices.getToursSessions().get(i).getDescriptiveNote().getP().get(j).getLang());
							}
							this.setToursSessionGuidesAndSessionsWebpage(recreationalServices.getToursSessions().get(i).getWebpage().getHref());
							this.setToursSessionGuidesAndSessionsWebpageTitle(recreationalServices.getToursSessions().get(i).getWebpage().getContent());
						}
					}
					if(recreationalServices.getOtherServices()!=null){
						for (int i = 0; i < recreationalServices.getOtherServices().size(); i++) {
							for (int j = 0; j < recreationalServices.getToursSessions().get(i).getDescriptiveNote().getP().size(); j++) {
								this.setOtherServices(recreationalServices.getOtherServices().get(i).getDescriptiveNote().getP().get(j).getContent());
								this.setOtherServicesLang(recreationalServices.getOtherServices().get(i).getDescriptiveNote().getP().get(j).getLang());
							}
							this.setOtherServicesWebpage(recreationalServices.getOtherServices().get(i).getWebpage().getHref());
							this.setOtherServicesLink(recreationalServices.getOtherServices().get(i).getWebpage().getContent());
						}
					}
				}
			}
		}
	}

	/**
	 * Method to load all values of "Description" tab.
	 */
	private void loadDescriptionTabValues() {
		// Repositories info.
		if (this.eag.getArchguide().getDesc().getRepositories() != null) {
			if (!this.eag.getArchguide().getDesc().getRepositories().getRepository().isEmpty()) {
				// TODO: Review for multiple repositories.
				// First repository equals institution.
				Repository repository = this.eag.getArchguide().getDesc().getRepositories().getRepository().get(0);

				// Repository history.
				if (repository.getRepositorhist() != null) {
					if (repository.getRepositorhist().getDescriptiveNote() != null) {
						if (!repository.getRepositorhist().getDescriptiveNote().getP().isEmpty()) {
							// TODO: Review for multiple values.
							for (int i = 0; i < repository.getRepositorhist().getDescriptiveNote().getP().size(); i++) {
								this.setRepositorhist(repository.getRepositorhist().getDescriptiveNote().getP().get(i).getContent());
								this.setRepositorhistLang(repository.getRepositorhist().getDescriptiveNote().getP().get(i).getLang());
							}
						}
					}
				}

				// Date of repository foundation.
				if (repository.getRepositorfound() != null) {
					this.setRepositorFoundDate(repository.getRepositorfound().getDate().getContent());
					this.setRepositorFoundDateLang(repository.getRepositorfound().getDate().getLang());
					this.setRepositorFoundDateLocalType(repository.getRepositorfound().getDate().getLocalType());
					this.setRepositorFoundDateNotAfter(repository.getRepositorfound().getDate().getNotAfter());
					this.setRepositorFoundDateNotBefore(repository.getRepositorfound().getDate().getNotBefore());
					this.setRepositorFoundDateStandardDate(repository.getRepositorfound().getDate().getStandardDate());

					// Rule of repository foundation.
					if (!repository.getRepositorfound().getRule().isEmpty()) {
						// TODO: Review for multiple values.
						for (int i = 0; i < repository.getRepositorfound().getRule().size(); i++) {
							this.setRepositorFoundRule(repository.getRepositorfound().getRule().get(i).getContent());
							this.setRepositorFoundRuleLang(repository.getRepositorfound().getRule().get(i).getLang());
						}
					}
				}

				// Date of repository suppression.
				if (repository.getRepositorsup() != null) {
					this.setRepositorSupDate(repository.getRepositorsup().getDate().getContent());
					this.setRepositorSupDateLang(repository.getRepositorsup().getDate().getLang());
					this.setRepositorSupDateLocalType(repository.getRepositorsup().getDate().getLocalType());
					this.setRepositorSupDateNotAfter(repository.getRepositorsup().getDate().getNotAfter());
					this.setRepositorSupDateNotBefore(repository.getRepositorsup().getDate().getNotBefore());
					this.setRepositorSupDateStandardDate(repository.getRepositorsup().getDate().getStandardDate());

					// Rule of repository suppression.
					if (!repository.getRepositorsup().getRule().isEmpty()) {
						// TODO: Review for multiple values.
						for (int i = 0; i < repository.getRepositorsup().getRule().size(); i++) {
							this.setRepositorSupRule(repository.getRepositorsup().getRule().get(i).getContent());
							this.setRepositorSupRuleLang(repository.getRepositorsup().getRule().get(i).getLang());
						}
					}
				}

				// Unit of administrative structure.
				if (repository.getAdminhierarchy() != null) {
					if (!repository.getAdminhierarchy().getAdminunit().isEmpty()) {
						// TODO: Review for multiple values.
						for (int i = 0; i < repository.getAdminhierarchy().getAdminunit().size(); i++) {
							this.setAdminunit(repository.getAdminhierarchy().getAdminunit().get(i).getContent());
							this.setAdminunitLang(repository.getAdminhierarchy().getAdminunit().get(i).getLang());
						}
					}
				}

				// Building.
				if (repository.getBuildinginfo() != null) {
					if (repository.getBuildinginfo().getBuilding() != null) {
						if (repository.getBuildinginfo().getBuilding().getDescriptiveNote() != null) {
							if (!repository.getBuildinginfo().getBuilding().getDescriptiveNote().getP().isEmpty()) {
								// TODO: Review for multiple values.
								for (int i = 0; i < repository.getBuildinginfo().getBuilding().getDescriptiveNote().getP().size(); i++) {
									this.setBuilding(repository.getBuildinginfo().getBuilding().getDescriptiveNote().getP().get(i).getContent());
									this.setBuildingLang(repository.getBuildinginfo().getBuilding().getDescriptiveNote().getP().get(i).getLang());
								}
							}
						}
					}

					// Repository Area.
					if (repository.getBuildinginfo().getRepositorarea() != null) {
						this.setRepositorarea(repository.getBuildinginfo().getRepositorarea().getNum().getContent());
						this.setRepositorareaUnit(repository.getBuildinginfo().getRepositorarea().getNum().getUnit());
					}

					// Length of shelf.
					if (repository.getBuildinginfo().getLengthshelf() != null) {
						this.setLengthshelf(repository.getBuildinginfo().getLengthshelf().getNum().getContent());
						this.setLengthshelfUnit(repository.getBuildinginfo().getLengthshelf().getNum().getUnit());
					}
				}

				// Holding description.
				if (repository.getHoldings() != null) {
					if (repository.getHoldings().getDescriptiveNote() != null) {
						if (!repository.getHoldings().getDescriptiveNote().getP().isEmpty()) {
							// TODO: Review for multiple values.
							for (int i = 0; i < repository.getHoldings().getDescriptiveNote().getP().size(); i++) {
								this.setHoldings(repository.getHoldings().getDescriptiveNote().getP().get(i).getContent());
								this.setHoldingsLang(repository.getHoldings().getDescriptiveNote().getP().get(i).getLang());
							}
						}
					}
						
					// Date of holdings.
					if (repository.getHoldings().getDate() != null) {
						this.setHoldingsDate(repository.getHoldings().getDate().getContent());
						this.setHoldingsDateLang(repository.getHoldings().getDate().getLang());
						this.setHoldingsDateLocalType(repository.getHoldings().getDate().getLocalType());
						this.setHoldingsDateNotAfter(repository.getHoldings().getDate().getNotAfter());
						this.setHoldingsDateNotBefore(repository.getHoldings().getDate().getNotBefore());
						this.setHoldingsDateStandardDate(repository.getHoldings().getDate().getStandardDate());
					}
					
					// Date range of holdings.
					if (repository.getHoldings().getDateRange() != null) {
						this.setHoldingsDateRangeLocalType(repository.getHoldings().getDateRange().getLocalType());

						// From date.
						if (repository.getHoldings().getDateRange().getFromDate() != null) {
							this.setHoldingsDateRangeFromDate(repository.getHoldings().getDateRange().getFromDate().getContent());
							this.setHoldingsDateRangeFromDateNoAfter(repository.getHoldings().getDateRange().getFromDate().getNotAfter());
							this.setHoldingsDateRangeFromDateNoBefore(repository.getHoldings().getDateRange().getFromDate().getNotBefore());
							this.setHoldingsDateRangeFromDateStandardDate(repository.getHoldings().getDateRange().getFromDate().getStandardDate());
						}

						// To date.
						if (repository.getHoldings().getDateRange().getToDate() != null) {
							this.setHoldingsDateRangeToDate(repository.getHoldings().getDateRange().getToDate().getContent());
							this.setHoldingsDateRangeToDateNoAfter(repository.getHoldings().getDateRange().getToDate().getNotAfter());
							this.setHoldingsDateRangeToDateNoBefore(repository.getHoldings().getDateRange().getToDate().getNotBefore());
							this.setHoldingsDateRangeToDateStandardDate(repository.getHoldings().getDateRange().getToDate().getStandardDate());
						}
					}

					// Extent.
					if (repository.getHoldings().getExtent() != null) {
						if (repository.getHoldings().getExtent().getNum() != null) {
							this.setExtent(repository.getHoldings().getExtent().getNum().getContent());
							this.setExtentUnit(repository.getHoldings().getExtent().getNum().getUnit());
						}
					}
				}
			}
		}
	}

	/**
	 * Method to load all values of "Control" tab.
	 */
	private void loadControlTabValues() {
		// Lang of person/institution responsible for the description.
		if (!this.eag.getControl().getMaintenanceHistory().getMaintenanceEvent().isEmpty()) {
			if (this.eag.getControl().getMaintenanceHistory().getMaintenanceEvent().get(this.eag.getControl().getMaintenanceHistory().getMaintenanceEvent().size()-1).getAgent() != null) {
				this.setAgentLang(this.eag.getControl().getMaintenanceHistory().getMaintenanceEvent().get(this.eag.getControl().getMaintenanceHistory().getMaintenanceEvent().size()-1).getAgent().getLang());
			}
		}

		// Identifier of responsible institution.
		if (this.eag.getControl().getMaintenanceAgency() != null) {
			if (this.eag.getControl().getMaintenanceAgency().getAgencyCode() != null) {
				this.setAgencyCode(this.eag.getControl().getMaintenanceAgency().getAgencyCode().getContent());
			}
		}

		// Identifier of responsible institution.
		if (this.eag.getControl().getMaintenanceAgency() != null) {
			if (this.eag.getControl().getMaintenanceAgency().getAgencyCode() != null) {
				this.setAgencyCode(this.eag.getControl().getMaintenanceAgency().getAgencyCode().getContent());
			}
		}

		// Used languages and scripts for the description.
		if (this.eag.getControl().getLanguageDeclarations() != null) {
			if (!this.eag.getControl().getLanguageDeclarations().getLanguageDeclaration().isEmpty()) {
				// TODO: Review for multiple values.
				for (int i = 0; i < this.eag.getControl().getLanguageDeclarations().getLanguageDeclaration().size(); i++) {
					if (this.eag.getControl().getLanguageDeclarations().getLanguageDeclaration().get(i).getLanguage()!= null
							&& this.eag.getControl().getLanguageDeclarations().getLanguageDeclaration().get(i).getLanguage().getLanguageCode() != null) {
						this.setLanguageDeclaration(this.eag.getControl().getLanguageDeclarations().getLanguageDeclaration().get(i).getLanguage().getLanguageCode());
					}
					if (this.eag.getControl().getLanguageDeclarations().getLanguageDeclaration().get(i).getScript() != null
							&& this.eag.getControl().getLanguageDeclarations().getLanguageDeclaration().get(i).getScript().getScriptCode() != null) {
						this.setScript(this.eag.getControl().getLanguageDeclarations().getLanguageDeclaration().get(i).getScript().getScriptCode());
					}
				}
			}
		}

		//Used rules / conventions / standards.
		if (this.eag.getControl().getConventionDeclaration() != null) {
			if (!this.eag.getControl().getConventionDeclaration().isEmpty()) {
				// TODO: Review for multiple values.
				for (int i = 0; i < this.eag.getControl().getConventionDeclaration().size(); i++) {
					if (this.eag.getControl().getConventionDeclaration().get(i).getAbbreviation() != null) {
						this.setLanguage(this.eag.getControl().getConventionDeclaration().get(i).getAbbreviation().getContent());
					}
					if (!this.eag.getControl().getConventionDeclaration().get(i).getCitation().isEmpty()) {
						// TODO: Review for multiple values.
						for (int j = 0; j < this.eag.getControl().getConventionDeclaration().get(i).getCitation().size(); j++) {
							this.setCitation(this.eag.getControl().getConventionDeclaration().get(i).getCitation().get(j).getContent());
						}
					}
				}
			}
		}
	}

	/**
	 * Method to load all values of "Relations" tab.
	 */
	private void loadRelationsTabValues() {
		// Resource relations.
		if (!this.eag.getRelations().getResourceRelation().isEmpty()) {
			// TODO: Review for multiple values.
			for (int i = 0; i < this.eag.getRelations().getResourceRelation().size(); i++) {
				// Website of your resource.
				this.setResourceRelationHref(this.eag.getRelations().getResourceRelation().get(i).getHref());

				// Title & ID of the related material.
				if (this.eag.getRelations().getResourceRelation().get(i).getRelationEntry() != null) {
					this.setResourceRelationrelationEntry(this.eag.getRelations().getResourceRelation().get(i).getRelationEntry().getContent());
				}

				// Type of your relation.
				if (this.eag.getRelations().getResourceRelation().get(i).getResourceRelationType() != null) {
					String resourceRelationTypeValue = this.eag.getRelations().getResourceRelation().get(i).getResourceRelationType();

					if (Eag2012.OPTION_CREATOR_TEXT.equalsIgnoreCase(resourceRelationTypeValue)) {
						resourceRelationTypeValue = Eag2012.OPTION_CREATOR;
					}
					if (Eag2012.OPTION_SUBJECT_TEXT.equalsIgnoreCase(resourceRelationTypeValue)) {
						resourceRelationTypeValue = Eag2012.OPTION_SUBJECT;
					}
					if (Eag2012.OPTION_OTHER_TEXT.equalsIgnoreCase(resourceRelationTypeValue)) {
						resourceRelationTypeValue = Eag2012.OPTION_OTHER;
					}

					this.setResourceRelationType(resourceRelationTypeValue);
				}

				// Description of relation.
				if (this.eag.getRelations().getResourceRelation().get(i).getDescriptiveNote() != null) {
					if (!this.eag.getRelations().getResourceRelation().get(i).getDescriptiveNote().getP().isEmpty()) {
						// TODO: Review for multiple values.
						for (int j = 0; j < this.eag.getRelations().getResourceRelation().get(i).getDescriptiveNote().getP().size(); j++) {
							this.setResourceRelationrelationEntryDescription(this.eag.getRelations().getResourceRelation().get(i).getDescriptiveNote().getP().get(j).getContent());
							this.setResourceRelationrelationEntryDescriptionLang(this.eag.getRelations().getResourceRelation().get(i).getDescriptiveNote().getP().get(j).getLang());
						}
					}
				}
			}
		}

		// Institution/Repository relation.
		if (!this.eag.getRelations().getEagRelation().isEmpty()) {
			// TODO: Review for multiple values.
			for (int i = 0; i < this.eag.getRelations().getEagRelation().size(); i++) {
				// Website of the description of the institution.
				this.setEagRelationHref(this.eag.getRelations().getEagRelation().get(i).getHref());

				// Title & ID of the related institution.
				if (!this.eag.getRelations().getEagRelation().get(i).getRelationEntry().isEmpty()) {
					// TODO: Review for multiple values.
					for (int j = 0; j < this.eag.getRelations().getEagRelation().size(); j++) {
//						this.setEagRelationrelationEntry(this.eag.getRelations().getEagRelation().get(i).getRelationEntry().get(j).getContent());
					}
				}

				// Type of your relation.
				if (this.eag.getRelations().getEagRelation().get(i).getEagRelationType() != null) {
					String eagRelationType = this.eag.getRelations().getEagRelation().get(i).getEagRelationType();

					if (Eag2012.OPTION_CHILD.equalsIgnoreCase(eagRelationType)) {
						eagRelationType = Eag2012.OPTION_CHILD_TEXT;
					}
					if (Eag2012.OPTION_PARENT.equalsIgnoreCase(eagRelationType)) {
						eagRelationType = Eag2012.OPTION_PARENT_TEXT;
					}
					if (Eag2012.OPTION_EARLIER.equalsIgnoreCase(eagRelationType)) {
						eagRelationType = Eag2012.OPTION_EARLIER_TEXT;
					}
					if (Eag2012.OPTION_LATER.equalsIgnoreCase(eagRelationType)) {
						eagRelationType = Eag2012.OPTION_LATER_TEXT;
					}
					if (Eag2012.OPTION_ASSOCIATIVE.equalsIgnoreCase(eagRelationType)) {
						eagRelationType = Eag2012.OPTION_ASSOCIATIVE_TEXT;
					}

					this.setResourceRelationType(eagRelationType);
				}

				// Description of relation.
				if (this.eag.getRelations().getEagRelation().get(i).getDescriptiveNote() != null) {
					if (!this.eag.getRelations().getEagRelation().get(i).getDescriptiveNote().getP().isEmpty()) {
						// TODO: Review for multiple values.
						for (int j = 0; j < this.eag.getRelations().getEagRelation().get(i).getDescriptiveNote().getP().size(); j++) {
							this.setEagRelationrelationEntryDescription(this.eag.getRelations().getEagRelation().get(i).getDescriptiveNote().getP().get(j).getContent());
							this.setEagRelationrelationEntryDescriptionLang(this.eag.getRelations().getEagRelation().get(i).getDescriptiveNote().getP().get(j).getLang());
						}
					}
				}
			}
		}
	}
	
	public String toString(){
		return "Loader with record id: "+this.recordId;
	}
}