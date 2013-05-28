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
import eu.apenet.dpt.utils.eag2012.Eag;
import eu.apenet.dpt.utils.eag2012.Location;
import eu.apenet.dpt.utils.eag2012.Parform;
import eu.apenet.dpt.utils.eag2012.Repository;
import eu.apenet.dpt.utils.eag2012.Timetable;
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
// TODO:
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
    private String citationHref;
    private String termsOfUse;
    private String termsOfUseLang;
    private String termsOfUseHref;

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

	public void fillEag2012() {
		Eag eag = null;
		ArchivalInstitution archivalInstitution = DAOFactory.instance().getArchivalInstitutionDAO().getArchivalInstitution(getId());
		String path = archivalInstitution.getEagPath();
		File eagFile = new File((APEnetUtilities.getConfig().getRepoDirPath() + path));

		try {
			InputStream eagStream = FileUtils.openInputStream(eagFile);

			JAXBContext jaxbContext = JAXBContext.newInstance(Eag.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            eag = (Eag) jaxbUnmarshaller.unmarshal(eagStream);

            eagStream.close();			
		} catch (JAXBException jaxbe) {
			log.info(jaxbe.getMessage());
			jaxbe.printStackTrace();
		} catch (IOException ioe) {
			log.info(ioe.getMessage());
			ioe.printStackTrace();
		}
		if (eag == null) {
			this.setCountryCode(this.getInitialCountryCode());
			this.setRecordId(this.getIdUsedInAPE());
			log.info("no previous EAG file");
		} else {
			this.eag = eag;
			this.loadValuesEAG2012();
		}
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
	
	/**
	 * @return the otherRepositorId
	 */
	public String getOtherRepositorId() {
		return this.otherRepositorId;
	}

	/**
	 * @param otherRepositorId the otherRepositorId to set
	 */
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

	/**
	 * @return the recordId
	 */
	public String getRecordId() {
		return this.recordId;
	}
	
	/**
	 * Generates unique isocode for ID used in APE.
	 * 
	 * @return ISO_CODE
	 */
	public String getIdUsedInAPE(){
		return Eag2012.generatesISOCode(getId());
	}

	/**
	 * @param recordId the recordId to set
	 */
	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}

	/**
	 * @return the autform
	 */
	public String getAutform() {
		return this.autform;
	}

	/**
	 * @param autform the autform to set
	 */
	public void setAutform(String autform) {
		this.autform = autform;
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
	 * @return the parform
	 */
	public String getParform() {
		return this.parform;
	}

	/**
	 * @param parform the parform to set
	 */
	public void setParform(String parform) {
		this.parform = parform;
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
	 * @return the localType
	 */
	public String getLocalType() {
		return this.localType;
	}

	/**
	 * @param localType the localType to set
	 */
	public void setLocalType(String localType) {
		this.localType = localType;
	}

	/**
	 * @return the longitude
	 */
	public String getLongitude() {
		return this.longitude;
	}

	/**
	 * @param longitude the longitude to set
	 */
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	/**
	 * @return the latitude
	 */
	public String getLatitude() {
		return this.latitude;
	}

	/**
	 * @param latitude the latitude to set
	 */
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	/**
	 * @return the country
	 */
	public String getCountry() {
		return this.country;
	}

	/**
	 * @param country the country to set
	 */
	public void setCountry(String country) {
		this.country = country;
	}

	/**
	 * @return the countryLang
	 */
	public String getCountryLang() {
		return this.countryLang;
	}

	/**
	 * @param countryLang the countryLang to set
	 */
	public void setCountryLang(String countryLang) {
		this.countryLang = countryLang;
	}

	/**
	 * @return the firstdem
	 */
	public String getFirstdem() {
		return this.firstdem;
	}

	/**
	 * @param firstdem the firstdem to set
	 */
	public void setFirstdem(String firstdem) {
		this.firstdem = firstdem;
	}

	/**
	 * @return the firstdemLang
	 */
	public String getFirstdemLang() {
		return this.firstdemLang;
	}

	/**
	 * @param firstdemLang the firstdemLang to set
	 */
	public void setFirstdemLang(String firstdemLang) {
		this.firstdemLang = firstdemLang;
	}

	/**
	 * @return the secondem
	 */
	public String getSecondem() {
		return this.secondem;
	}

	/**
	 * @param secondem the secondem to set
	 */
	public void setSecondem(String secondem) {
		this.secondem = secondem;
	}

	/**
	 * @return the secondemLang
	 */
	public String getSecondemLang() {
		return this.secondemLang;
	}

	/**
	 * @param secondemLang the secondemLang to set
	 */
	public void setSecondemLang(String secondemLang) {
		this.secondemLang = secondemLang;
	}

	/**
	 * @return the municipalityPostalcode
	 */
	public String getMunicipalityPostalcode() {
		return this.municipalityPostalcode;
	}

	/**
	 * @param municipalityPostalcode the municipalityPostalcode to set
	 */
	public void setMunicipalityPostalcode(String municipalityPostalcode) {
		this.municipalityPostalcode = municipalityPostalcode;
	}

	/**
	 * @return the municipalityPostalcodeLang
	 */
	public String getMunicipalityPostalcodeLang() {
		return this.municipalityPostalcodeLang;
	}

	/**
	 * @param municipalityPostalcodeLang the municipalityPostalcodeLang to set
	 */
	public void setMunicipalityPostalcodeLang(String municipalityPostalcodeLang) {
		this.municipalityPostalcodeLang = municipalityPostalcodeLang;
	}

	/**
	 * @return the localentity
	 */
	public String getLocalentity() {
		return this.localentity;
	}

	/**
	 * @param localentity the localentity to set
	 */
	public void setLocalentity(String localentity) {
		this.localentity = localentity;
	}

	/**
	 * @return the localentityLang
	 */
	public String getLocalentityLang() {
		return this.localentityLang;
	}

	/**
	 * @param localentityLang the localentityLang to set
	 */
	public void setLocalentityLang(String localentityLang) {
		this.localentityLang = localentityLang;
	}

	/**
	 * @return the street
	 */
	public String getStreet() {
		return this.street;
	}

	/**
	 * @param street the street to set
	 */
	public void setStreet(String street) {
		this.street = street;
	}

	/**
	 * @return the streetLang
	 */
	public String getStreetLang() {
		return this.streetLang;
	}

	/**
	 * @param streetLang the streetLang to set
	 */
	public void setStreetLang(String streetLang) {
		this.streetLang = streetLang;
	}

	/**
	 * @return the municipalityPostalcodePostal
	 */
	public String getMunicipalityPostalcodePostal() {
		return this.municipalityPostalcodePostal;
	}

	/**
	 * @param municipalityPostalcodePostal the municipalityPostalcodePostal to set
	 */
	public void setMunicipalityPostalcodePostal(String municipalityPostalcodePostal) {
		this.municipalityPostalcodePostal = municipalityPostalcodePostal;
	}

	/**
	 * @return the municipalityPostalcodePostalLang
	 */
	public String getMunicipalityPostalcodePostalLang() {
		return this.municipalityPostalcodePostalLang;
	}

	/**
	 * @param municipalityPostalcodePostalLang the municipalityPostalcodePostalLang to set
	 */
	public void setMunicipalityPostalcodePostalLang(String municipalityPostalcodePostalLang) {
		this.municipalityPostalcodePostalLang = municipalityPostalcodePostalLang;
	}

	/**
	 * @return the streetPostal
	 */
	public String getStreetPostal() {
		return this.streetPostal;
	}

	/**
	 * @param streetPostal the streetPostal to set
	 */
	public void setStreetPostal(String streetPostal) {
		this.streetPostal = streetPostal;
	}

	/**
	 * @return the streetPostalLang
	 */
	public String getStreetPostalLang() {
		return this.streetPostalLang;
	}

	/**
	 * @param streetPostalLang the streetPostalLang to set
	 */
	public void setStreetPostalLang(String streetPostalLang) {
		this.streetPostalLang = streetPostalLang;
	}

	/**
	 * @return the geogarea
	 */
	public String getGeogarea() {
		return this.geogarea;
	}

	/**
	 * @param geogarea the geogarea to set
	 */
	public void setGeogarea(String geogarea) {
		this.geogarea = geogarea;
	}

	/**
	 * @return the telephone
	 */
	public String getTelephone() {
		return this.telephone;
	}

	/**
	 * @param telephone the telephone to set
	 */
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return this.email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the emailTitle
	 */
	public String getEmailTitle() {
		return this.emailTitle;
	}

	/**
	 * @param emailTitle the emailTitle to set
	 */
	public void setEmailTitle(String emailTitle) {
		this.emailTitle = emailTitle;
	}

	/**
	 * @return the emailLang
	 */
	public String getEmailLang() {
		return this.emailLang;
	}

	/**
	 * @param emailLang the emailLang to set
	 */
	public void setEmailLang(String emailLang) {
		this.emailLang = emailLang;
	}

	/**
	 * @return the webpage
	 */
	public String getWebpage() {
		return this.webpage;
	}

	/**
	 * @param webpage the webpage to set
	 */
	public void setWebpage(String webpage) {
		this.webpage = webpage;
	}

	/**
	 * @return the webpageTitle
	 */
	public String getWebpageTitle() {
		return this.webpageTitle;
	}

	/**
	 * @param webpageTitle the webpageTitle to set
	 */
	public void setWebpageTitle(String webpageTitle) {
		this.webpageTitle = webpageTitle;
	}

	/**
	 * @return the webpageLang
	 */
	public String getWebpageLang() {
		return this.webpageLang;
	}

	/**
	 * @param webpageLang the webpageLang to set
	 */
	public void setWebpageLang(String webpageLang) {
		this.webpageLang = webpageLang;
	}

	/**
	 * @return the opening
	 */
	public String getOpening() {
		return this.opening;
	}

	/**
	 * @param opening the opening to set
	 */
	public void setOpening(String opening) {
		this.opening = opening;
	}

	/**
	 * @return the openingLang
	 */
	public String getOpeningLang() {
		return this.openingLang;
	}

	/**
	 * @param openingLang the openingLang to set
	 */
	public void setOpeningLang(String openingLang) {
		this.openingLang = openingLang;
	}

	/**
	 * @return the closing
	 */
	public String getClosing() {
		return this.closing;
	}

	/**
	 * @param closing the closing to set
	 */
	public void setClosing(String closing) {
		this.closing = closing;
	}

	/**
	 * @return the closingLang
	 */
	public String getClosingLang() {
		return this.closingLang;
	}

	/**
	 * @param closingLang the closingLang to set
	 */
	public void setClosingLang(String closingLang) {
		this.closingLang = closingLang;
	}

	/**
	 * @return the accessQuestion
	 */
	public String getAccessQuestion() {
		return this.accessQuestion;
	}

	/**
	 * @param accessQuestion the accessQuestion to set
	 */
	public void setAccessQuestion(String accessQuestion) {
		this.accessQuestion = accessQuestion;
	}

	/**
	 * @return the restaccess
	 */
	public String getRestaccess() {
		return this.restaccess;
	}

	/**
	 * @param restaccess the restaccess to set
	 */
	public void setRestaccess(String restaccess) {
		this.restaccess = restaccess;
	}

	/**
	 * @return the restaccessLang
	 */
	public String getRestaccessLang() {
		return this.restaccessLang;
	}

	/**
	 * @param restaccessLang the restaccessLang to set
	 */
	public void setRestaccessLang(String restaccessLang) {
		this.restaccessLang = restaccessLang;
	}

	/**
	 * @return the accessibility
	 */
	public String getAccessibility() {
		return this.accessibility;
	}

	/**
	 * @param accessibility the accessibility to set
	 */
	public void setAccessibility(String accessibility) {
		this.accessibility = accessibility;
	}

	/**
	 * @return the accessibilityQuestion
	 */
	public String getAccessibilityQuestion() {
		return this.accessibilityQuestion;
	}

	/**
	 * @param accessibilityQuestion the accessibilityQuestion to set
	 */
	public void setAccessibilityQuestion(String accessibilityQuestion) {
		this.accessibilityQuestion = accessibilityQuestion;
	}

	/**
	 * @return the accessibilityLang
	 */
	public String getAccessibilityLang() {
		return this.accessibilityLang;
	}

	/**
	 * @param accessibilityLang the accessibilityLang to set
	 */
	public void setAccessibilityLang(String accessibilityLang) {
		this.accessibilityLang = accessibilityLang;
	}

	/**
	 * @return the resourceRelationHref
	 */
	public String getResourceRelationHref() {
		return this.resourceRelationHref;
	}

	/**
	 * @param resourceRelationHref the resourceRelationHref to set
	 */
	public void setResourceRelationHref(String resourceRelationHref) {
		this.resourceRelationHref = resourceRelationHref;
	}

	/**
	 * @return the resourceRelationLang
	 */
	public String getResourceRelationLang() {
		return this.resourceRelationLang;
	}

	/**
	 * @param resourceRelationLang the resourceRelationLang to set
	 */
	public void setResourceRelationLang(String resourceRelationLang) {
		this.resourceRelationLang = resourceRelationLang;
	}

	/**
	 * @return the resourceRelationType
	 */
	public String getResourceRelationType() {
		return this.resourceRelationType;
	}

	/**
	 * @param resourceRelationType the resourceRelationType to set
	 */
	public void setResourceRelationType(String resourceRelationType) {
		this.resourceRelationType = resourceRelationType;
	}

	/**
	 * @return the resourceRelationrelationEntry
	 */
	public String getResourceRelationrelationEntry() {
		return this.resourceRelationrelationEntry;
	}

	/**
	 * @param resourceRelationrelationEntry the resourceRelationrelationEntry to set
	 */
	public void setResourceRelationrelationEntry(
			String resourceRelationrelationEntry) {
		this.resourceRelationrelationEntry = resourceRelationrelationEntry;
	}

	/**
	 * @return the resourceRelationrelationEntryLocalType
	 */
	public String getResourceRelationrelationEntryLocalType() {
		return this.resourceRelationrelationEntryLocalType;
	}

	/**
	 * @param resourceRelationrelationEntryLocalType the resourceRelationrelationEntryLocalType to set
	 */
	public void setResourceRelationrelationEntryLocalType(
			String resourceRelationrelationEntryLocalType) {
		this.resourceRelationrelationEntryLocalType = resourceRelationrelationEntryLocalType;
	}

	/**
	 * @return the resourceRelationrelationEntryScriptCode
	 */
	public String getResourceRelationrelationEntryScriptCode() {
		return this.resourceRelationrelationEntryScriptCode;
	}

	/**
	 * @param resourceRelationrelationEntryScriptCode the resourceRelationrelationEntryScriptCode to set
	 */
	public void setResourceRelationrelationEntryScriptCode(
			String resourceRelationrelationEntryScriptCode) {
		this.resourceRelationrelationEntryScriptCode = resourceRelationrelationEntryScriptCode;
	}

	/**
	 * @return the resourceRelationrelationEntryTransliteration
	 */
	public String getResourceRelationrelationEntryTransliteration() {
		return this.resourceRelationrelationEntryTransliteration;
	}

	/**
	 * @param resourceRelationrelationEntryTransliteration the resourceRelationrelationEntryTransliteration to set
	 */
	public void setResourceRelationrelationEntryTransliteration(
			String resourceRelationrelationEntryTransliteration) {
		this.resourceRelationrelationEntryTransliteration = resourceRelationrelationEntryTransliteration;
	}

	/**
	 * @return the resourceRelationrelationEntryLang
	 */
	public String getResourceRelationrelationEntryLang() {
		return this.resourceRelationrelationEntryLang;
	}

	/**
	 * @param resourceRelationrelationEntryLang the resourceRelationrelationEntryLang to set
	 */
	public void setResourceRelationrelationEntryLang(
			String resourceRelationrelationEntryLang) {
		this.resourceRelationrelationEntryLang = resourceRelationrelationEntryLang;
	}

	/**
	 * @return the resourceRelationrelationEntryDescription
	 */
	public String getResourceRelationrelationEntryDescription() {
		return this.resourceRelationrelationEntryDescription;
	}

	/**
	 * @param resourceRelationrelationEntryDescription the resourceRelationrelationEntryDescription to set
	 */
	public void setResourceRelationrelationEntryDescription(
			String resourceRelationrelationEntryDescription) {
		this.resourceRelationrelationEntryDescription = resourceRelationrelationEntryDescription;
	}

	/**
	 * @return the resourceRelationrelationEntryDescriptionLang
	 */
	public String getResourceRelationrelationEntryDescriptionLang() {
		return this.resourceRelationrelationEntryDescriptionLang;
	}

	/**
	 * @param resourceRelationrelationEntryDescriptionLang the resourceRelationrelationEntryDescriptionLang to set
	 */
	public void setResourceRelationrelationEntryDescriptionLang(
			String resourceRelationrelationEntryDescriptionLang) {
		this.resourceRelationrelationEntryDescriptionLang = resourceRelationrelationEntryDescriptionLang;
	}

	/**
	 * @return the repositoryType
	 */
	public List<String> getRepositoryType() {
        if (this.repositoryType == null) {
        	this.repositoryType = new ArrayList<String>();
        }
		return this.repositoryType;
	}

	/**
	 * @param repositoryType the repositoryType to set
	 */
	public void setRepositoryType(List<String> repositoryType) {
		this.repositoryType = repositoryType;
	}

	/**
	 * @return the fax
	 */
	public String getFax() {
		return this.fax;
	}

	/**
	 * @param fax the fax to set
	 */
	public void setFax(String fax) {
		this.fax = fax;
	}

	/**
	 * @return the repositorhist
	 */
	public String getRepositorhist() {
		return this.repositorhist;
	}

	/**
	 * @return the directions
	 */
	public String getDirections() {
		return this.directions;
	}

	/**
	 * @param directions the directions to set
	 */
	public void setDirections(String directions) {
		this.directions = directions;
	}

	/**
	 * @return the directionsLang
	 */
	public String getDirectionsLang() {
		return this.directionsLang;
	}

	/**
	 * @param directionsLang the directionsLang to set
	 */
	public void setDirectionsLang(String directionsLang) {
		this.directionsLang = directionsLang;
	}

	/**
	 * @return the citationHref
	 */
	public String getCitationHref() {
		return this.citationHref;
	}

	/**
	 * @param citationHref the citationHref to set
	 */
	public void setCitationHref(String citationHref) {
		this.citationHref = citationHref;
	}

	/**
	 * @return the termsOfUse
	 */
	public String getTermsOfUse() {
		return this.termsOfUse;
	}

	/**
	 * @param termsOfUse the termsOfUse to set
	 */
	public void setTermsOfUse(String termsOfUse) {
		this.termsOfUse = termsOfUse;
	}

	/**
	 * @return the termsOfUseLang
	 */
	public String getTermsOfUseLang() {
		return this.termsOfUseLang;
	}

	/**
	 * @param termsOfUseLang the termsOfUseLang to set
	 */
	public void setTermsOfUseLang(String termsOfUseLang) {
		this.termsOfUseLang = termsOfUseLang;
	}

	/**
	 * @return the termsOfUseHref
	 */
	public String getTermsOfUseHref() {
		return this.termsOfUseHref;
	}

	/**
	 * @param termsOfUseHref the termsOfUseHref to set
	 */
	public void setTermsOfUseHref(String termsOfUseHref) {
		this.termsOfUseHref = termsOfUseHref;
	}

	/**
	 * @param repositorhist the repositorhist to set
	 */
	public void setRepositorhist(String repositorhist) {
		this.repositorhist = repositorhist;
	}

	/**
	 * @return the repositorhistLang
	 */
	public String getRepositorhistLang() {
		return this.repositorhistLang;
	}

	/**
	 * @param repositorhistLang the repositorhistLang to set
	 */
	public void setRepositorhistLang(String repositorhistLang) {
		this.repositorhistLang = repositorhistLang;
	}

	/**
	 * @return the repositorFoundDate
	 */
	public String getRepositorFoundDate() {
		return this.repositorFoundDate;
	}

	/**
	 * @param repositorFoundDate the repositorFoundDate to set
	 */
	public void setRepositorFoundDate(String repositorFoundDate) {
		this.repositorFoundDate = repositorFoundDate;
	}

	/**
	 * @return the repositorFoundDateLocalType
	 */
	public String getRepositorFoundDateLocalType() {
		return this.repositorFoundDateLocalType;
	}

	/**
	 * @param repositorFoundDateLocalType the repositorFoundDateLocalType to set
	 */
	public void setRepositorFoundDateLocalType(
			String repositorFoundDateLocalType) {
		this.repositorFoundDateLocalType = repositorFoundDateLocalType;
	}

	/**
	 * @return the repositorFoundDateNotAfter
	 */
	public String getRepositorFoundDateNotAfter() {
		return this.repositorFoundDateNotAfter;
	}

	/**
	 * @param repositorFoundDateNotAfter the repositorFoundDateNotAfter to set
	 */
	public void setRepositorFoundDateNotAfter(
			String repositorFoundDateNotAfter) {
		this.repositorFoundDateNotAfter = repositorFoundDateNotAfter;
	}

	/**
	 * @return the repositorFoundDateNotBefore
	 */
	public String getRepositorFoundDateNotBefore() {
		return this.repositorFoundDateNotBefore;
	}

	/**
	 * @param repositorFoundDateNotBefore the repositorFoundDateNotBefore to set
	 */
	public void setRepositorFoundDateNotBefore(
			String repositorFoundDateNotBefore) {
		this.repositorFoundDateNotBefore = repositorFoundDateNotBefore;
	}

	/**
	 * @return the repositorFoundDateStandardDate
	 */
	public String getRepositorFoundDateStandardDate() {
		return this.repositorFoundDateStandardDate;
	}

	/**
	 * @param repositorFoundDateStandardDate the repositorFoundDateStandardDate to set
	 */
	public void setRepositorFoundDateStandardDate(
			String repositorFoundDateStandardDate) {
		this.repositorFoundDateStandardDate = repositorFoundDateStandardDate;
	}

	/**
	 * @return the repositorFoundDateLang
	 */
	public String getRepositorFoundDateLang() {
		return this.repositorFoundDateLang;
	}

	/**
	 * @param repositorFoundDateLang the repositorFoundDateLang to set
	 */
	public void setRepositorFoundDateLang(String repositorFoundDateLang) {
		this.repositorFoundDateLang = repositorFoundDateLang;
	}

	/**
	 * @return the repositorFoundRule
	 */
	public String getRepositorFoundRule() {
		return this.repositorFoundRule;
	}

	/**
	 * @param repositorFoundRule the repositorFoundRule to set
	 */
	public void setRepositorFoundRule(String repositorFoundRule) {
		this.repositorFoundRule = repositorFoundRule;
	}

	/**
	 * @return the repositorFoundRuleLang
	 */
	public String getRepositorFoundRuleLang() {
		return this.repositorFoundRuleLang;
	}

	/**
	 * @param repositorFoundRuleLang the repositorFoundRuleLang to set
	 */
	public void setRepositorFoundRuleLang(String repositorFoundRuleLang) {
		this.repositorFoundRuleLang = repositorFoundRuleLang;
	}

	/**
	 * @return the repositorSupDate
	 */
	public String getRepositorSupDate() {
		return this.repositorSupDate;
	}

	/**
	 * @param repositorSupDate the repositorSupDate to set
	 */
	public void setRepositorSupDate(String repositorSupDate) {
		this.repositorSupDate = repositorSupDate;
	}

	/**
	 * @return the repositorSupDateLocalType
	 */
	public String getRepositorSupDateLocalType() {
		return this.repositorSupDateLocalType;
	}

	/**
	 * @param repositorSupDateLocalType the repositorSupDateLocalType to set
	 */
	public void setRepositorSupDateLocalType(String repositorSupDateLocalType) {
		this.repositorSupDateLocalType = repositorSupDateLocalType;
	}

	/**
	 * @return the repositorSupDateNotAfter
	 */
	public String getRepositorSupDateNotAfter() {
		return this.repositorSupDateNotAfter;
	}

	/**
	 * @param repositorSupDateNotAfter the repositorSupDateNotAfter to set
	 */
	public void setRepositorSupDateNotAfter(String repositorSupDateNotAfter) {
		this.repositorSupDateNotAfter = repositorSupDateNotAfter;
	}

	/**
	 * @return the repositorSupDateNotBefore
	 */
	public String getRepositorSupDateNotBefore() {
		return this.repositorSupDateNotBefore;
	}

	/**
	 * @param repositorSupDateNotBefore the repositorSupDateNotBefore to set
	 */
	public void setRepositorSupDateNotBefore(String repositorSupDateNotBefore) {
		this.repositorSupDateNotBefore = repositorSupDateNotBefore;
	}

	/**
	 * @return the repositorSupDateStandardDate
	 */
	public String getRepositorSupDateStandardDate() {
		return this.repositorSupDateStandardDate;
	}

	/**
	 * @param repositorSupDateStandardDate the repositorSupDateStandardDate to set
	 */
	public void setRepositorSupDateStandardDate(
			String repositorSupDateStandardDate) {
		this.repositorSupDateStandardDate = repositorSupDateStandardDate;
	}

	/**
	 * @return the repositorSupDateLang
	 */
	public String getRepositorSupDateLang() {
		return this.repositorSupDateLang;
	}

	/**
	 * @param repositorSupDateLang the repositorSupDateLang to set
	 */
	public void setRepositorSupDateLang(String repositorSupDateLang) {
		this.repositorSupDateLang = repositorSupDateLang;
	}

	/**
	 * @return the repositorSupRule
	 */
	public String getRepositorSupRule() {
		return this.repositorSupRule;
	}

	/**
	 * @param repositorSupRule the repositorSupRule to set
	 */
	public void setRepositorSupRule(String repositorSupRule) {
		this.repositorSupRule = repositorSupRule;
	}

	/**
	 * @return the repositorSupRuleLang
	 */
	public String getRepositorSupRuleLang() {
		return this.repositorSupRuleLang;
	}

	/**
	 * @param repositorSupRuleLang the repositorSupRuleLang to set
	 */
	public void setRepositorSupRuleLang(String repositorSupRuleLang) {
		this.repositorSupRuleLang = repositorSupRuleLang;
	}

	/**
	 * @return the adminunit
	 */
	public String getAdminunit() {
		return this.adminunit;
	}

	/**
	 * @param adminunit the adminunit to set
	 */
	public void setAdminunit(String adminunit) {
		this.adminunit = adminunit;
	}

	/**
	 * @return the adminunitLang
	 */
	public String getAdminunitLang() {
		return this.adminunitLang;
	}

	/**
	 * @param adminunitLang the adminunitLang to set
	 */
	public void setAdminunitLang(String adminunitLang) {
		this.adminunitLang = adminunitLang;
	}

	/**
	 * @return the building
	 */
	public String getBuilding() {
		return this.building;
	}

	/**
	 * @param building the building to set
	 */
	public void setBuilding(String building) {
		this.building = building;
	}

	/**
	 * @return the buildingLang
	 */
	public String getBuildingLang() {
		return this.buildingLang;
	}

	/**
	 * @param buildingLang the buildingLang to set
	 */
	public void setBuildingLang(String buildingLang) {
		this.buildingLang = buildingLang;
	}

	/**
	 * @return the repositorarea
	 */
	public String getRepositorarea() {
		return this.repositorarea;
	}

	/**
	 * @param repositorarea the repositorarea to set
	 */
	public void setRepositorarea(String repositorarea) {
		this.repositorarea = repositorarea;
	}

	/**
	 * @return the repositorareaUnit
	 */
	public String getRepositorareaUnit() {
		return this.repositorareaUnit;
	}

	/**
	 * @param repositorareaUnit the repositorareaUnit to set
	 */
	public void setRepositorareaUnit(String repositorareaUnit) {
		this.repositorareaUnit = repositorareaUnit;
	}

	/**
	 * @return the lengthshelf
	 */
	public String getLengthshelf() {
		return this.lengthshelf;
	}

	/**
	 * @param lengthshelf the lengthshelf to set
	 */
	public void setLengthshelf(String lengthshelf) {
		this.lengthshelf = lengthshelf;
	}

	/**
	 * @return the lengthshelfUnit
	 */
	public String getLengthshelfUnit() {
		return this.lengthshelfUnit;
	}

	/**
	 * @param lengthshelfUnit the lengthshelfUnit to set
	 */
	public void setLengthshelfUnit(String lengthshelfUnit) {
		this.lengthshelfUnit = lengthshelfUnit;
	}

	/**
	 * @return the holdings
	 */
	public String getHoldings() {
		return this.holdings;
	}

	/**
	 * @param holdings the holdings to set
	 */
	public void setHoldings(String holdings) {
		this.holdings = holdings;
	}

	/**
	 * @return the holdingsLang
	 */
	public String getHoldingsLang() {
		return this.holdingsLang;
	}

	/**
	 * @param holdingsLang the holdingsLang to set
	 */
	public void setHoldingsLang(String holdingsLang) {
		this.holdingsLang = holdingsLang;
	}

	/**
	 * @return the holdingsDate
	 */
	public String getHoldingsDate() {
		return this.holdingsDate;
	}

	/**
	 * @param holdingsDate the holdingsDate to set
	 */
	public void setHoldingsDate(String holdingsDate) {
		this.holdingsDate = holdingsDate;
	}

	/**
	 * @return the holdingsDateLocalType
	 */
	public String getHoldingsDateLocalType() {
		return this.holdingsDateLocalType;
	}

	/**
	 * @param holdingsDateLocalType the holdingsDateLocalType to set
	 */
	public void setHoldingsDateLocalType(String holdingsDateLocalType) {
		this.holdingsDateLocalType = holdingsDateLocalType;
	}

	/**
	 * @return the holdingsDateNotAfter
	 */
	public String getHoldingsDateNotAfter() {
		return this.holdingsDateNotAfter;
	}

	/**
	 * @param holdingsDateNotAfter the holdingsDateNotAfter to set
	 */
	public void setHoldingsDateNotAfter(String holdingsDateNotAfter) {
		this.holdingsDateNotAfter = holdingsDateNotAfter;
	}

	/**
	 * @return the holdingsDateNotBefore
	 */
	public String getHoldingsDateNotBefore() {
		return this.holdingsDateNotBefore;
	}

	/**
	 * @param holdingsDateNotBefore the holdingsDateNotBefore to set
	 */
	public void setHoldingsDateNotBefore(String holdingsDateNotBefore) {
		this.holdingsDateNotBefore = holdingsDateNotBefore;
	}

	/**
	 * @return the holdingsDateStandardDate
	 */
	public String getHoldingsDateStandardDate() {
		return this.holdingsDateStandardDate;
	}

	/**
	 * @param holdingsDateStandardDate the holdingsDateStandardDate to set
	 */
	public void setHoldingsDateStandardDate(String holdingsDateStandardDate) {
		this.holdingsDateStandardDate = holdingsDateStandardDate;
	}

	/**
	 * @return the holdingsDateLang
	 */
	public String getHoldingsDateLang() {
		return this.holdingsDateLang;
	}

	/**
	 * @param holdingsDateLang the holdingsDateLang to set
	 */
	public void setHoldingsDateLang(String holdingsDateLang) {
		this.holdingsDateLang = holdingsDateLang;
	}

	/**
	 * @return the holdingsDateRangeLocalType
	 */
	public String getHoldingsDateRangeLocalType() {
		return this.holdingsDateRangeLocalType;
	}

	/**
	 * @param holdingsDateRangeLocalType the holdingsDateRangeLocalType to set
	 */
	public void setHoldingsDateRangeLocalType(
			String holdingsDateRangeLocalType) {
		this.holdingsDateRangeLocalType = holdingsDateRangeLocalType;
	}

	/**
	 * @return the holdingsDateRangeFromDate
	 */
	public String getHoldingsDateRangeFromDate() {
		return this.holdingsDateRangeFromDate;
	}

	/**
	 * @param holdingsDateRangeFromDate the holdingsDateRangeFromDate to set
	 */
	public void setHoldingsDateRangeFromDate(String holdingsDateRangeFromDate) {
		this.holdingsDateRangeFromDate = holdingsDateRangeFromDate;
	}

	/**
	 * @return the holdingsDateRangeFromDateNoAfter
	 */
	public String getHoldingsDateRangeFromDateNoAfter() {
		return this.holdingsDateRangeFromDateNoAfter;
	}

	/**
	 * @param holdingsDateRangeFromDateNoAfter the holdingsDateRangeFromDateNoAfter to set
	 */
	public void setHoldingsDateRangeFromDateNoAfter(
			String holdingsDateRangeFromDateNoAfter) {
		this.holdingsDateRangeFromDateNoAfter = holdingsDateRangeFromDateNoAfter;
	}

	/**
	 * @return the holdingsDateRangeFromDateNoBefore
	 */
	public String getHoldingsDateRangeFromDateNoBefore() {
		return this.holdingsDateRangeFromDateNoBefore;
	}

	/**
	 * @param holdingsDateRangeFromDateNoBefore the holdingsDateRangeFromDateNoBefore to set
	 */
	public void setHoldingsDateRangeFromDateNoBefore(
			String holdingsDateRangeFromDateNoBefore) {
		this.holdingsDateRangeFromDateNoBefore = holdingsDateRangeFromDateNoBefore;
	}

	/**
	 * @return the holdingsDateRangeFromDateStandardDate
	 */
	public String getHoldingsDateRangeFromDateStandardDate() {
		return this.holdingsDateRangeFromDateStandardDate;
	}

	/**
	 * @param holdingsDateRangeFromDateStandardDate the holdingsDateRangeFromDateStandardDate to set
	 */
	public void setHoldingsDateRangeFromDateStandardDate(
			String holdingsDateRangeFromDateStandardDate) {
		this.holdingsDateRangeFromDateStandardDate = holdingsDateRangeFromDateStandardDate;
	}

	/**
	 * @return the holdingsDateRangeFromDateLang
	 */
	public String getHoldingsDateRangeFromDateLang() {
		return this.holdingsDateRangeFromDateLang;
	}

	/**
	 * @param holdingsDateRangeFromDateLang the holdingsDateRangeFromDateLang to set
	 */
	public void setHoldingsDateRangeFromDateLang(
			String holdingsDateRangeFromDateLang) {
		this.holdingsDateRangeFromDateLang = holdingsDateRangeFromDateLang;
	}

	/**
	 * @return the holdingsDateRangeToDate
	 */
	public String getHoldingsDateRangeToDate() {
		return this.holdingsDateRangeToDate;
	}

	/**
	 * @param holdingsDateRangeToDate the holdingsDateRangeToDate to set
	 */
	public void setHoldingsDateRangeToDate(String holdingsDateRangeToDate) {
		this.holdingsDateRangeToDate = holdingsDateRangeToDate;
	}

	/**
	 * @return the holdingsDateRangeToDateNoAfter
	 */
	public String getHoldingsDateRangeToDateNoAfter() {
		return this.holdingsDateRangeToDateNoAfter;
	}

	/**
	 * @param holdingsDateRangeToDateNoAfter the holdingsDateRangeToDateNoAfter to set
	 */
	public void setHoldingsDateRangeToDateNoAfter(
			String holdingsDateRangeToDateNoAfter) {
		this.holdingsDateRangeToDateNoAfter = holdingsDateRangeToDateNoAfter;
	}

	/**
	 * @return the holdingsDateRangeToDateNoBefore
	 */
	public String getHoldingsDateRangeToDateNoBefore() {
		return this.holdingsDateRangeToDateNoBefore;
	}

	/**
	 * @param holdingsDateRangeToDateNoBefore the holdingsDateRangeToDateNoBefore to set
	 */
	public void setHoldingsDateRangeToDateNoBefore(
			String holdingsDateRangeToDateNoBefore) {
		this.holdingsDateRangeToDateNoBefore = holdingsDateRangeToDateNoBefore;
	}

	/**
	 * @return the holdingsDateRangeToDateStandardDate
	 */
	public String getHoldingsDateRangeToDateStandardDate() {
		return this.holdingsDateRangeToDateStandardDate;
	}

	/**
	 * @param holdingsDateRangeToDateStandardDate the holdingsDateRangeToDateStandardDate to set
	 */
	public void setHoldingsDateRangeToDateStandardDate(
			String holdingsDateRangeToDateStandardDate) {
		this.holdingsDateRangeToDateStandardDate = holdingsDateRangeToDateStandardDate;
	}

	/**
	 * @return the holdingsDateRangeToDateLang
	 */
	public String getHoldingsDateRangeToDateLang() {
		return this.holdingsDateRangeToDateLang;
	}

	/**
	 * @param holdingsDateRangeToDateLang the holdingsDateRangeToDateLang to set
	 */
	public void setHoldingsDateRangeToDateLang(
			String holdingsDateRangeToDateLang) {
		this.holdingsDateRangeToDateLang = holdingsDateRangeToDateLang;
	}

	/**
	 * @return the extent
	 */
	public String getExtent() {
		return this.extent;
	}

	/**
	 * @param extent the extent to set
	 */
	public void setExtent(String extent) {
		this.extent = extent;
	}

	/**
	 * @return the extentUnit
	 */
	public String getExtentUnit() {
		return this.extentUnit;
	}

	/**
	 * @param extentUnit the extentUnit to set
	 */
	public void setExtentUnit(String extentUnit) {
		this.extentUnit = extentUnit;
	}

	/**
	 * @return the agentLang
	 */
	public String getAgentLang() {
		return this.agentLang;
	}

	/**
	 * @param agentLang the agentLang to set
	 */
	public void setAgentLang(String agentLang) {
		this.agentLang = agentLang;
	}

	/**
	 * @return the agencyCode
	 */
	public String getAgencyCode() {
		return this.agencyCode;
	}

	/**
	 * @param agencyCode the agencyCode to set
	 */
	public void setAgencyCode(String agencyCode) {
		this.agencyCode = agencyCode;
	}

	/**
	 * @return the language
	 */
	public String getLanguage() {
		return this.language;
	}

	/**
	 * @param language the language to set
	 */
	public void setLanguage(String language) {
		this.language = language;
	}

	/**
	 * @return the script
	 */
	public String getScript() {
		return this.script;
	}

	/**
	 * @param script the script to set
	 */
	public void setScript(String script) {
		this.script = script;
	}

	/**
	 * @return the abbreviation
	 */
	public String getAbbreviation() {
		return this.abbreviation;
	}

	/**
	 * @param abbreviation the abbreviation to set
	 */
	public void setAbbreviation(String abbreviation) {
		this.abbreviation = abbreviation;
	}

	/**
	 * @return the citation
	 */
	public String getCitation() {
		return this.citation;
	}

	/**
	 * @param citation the citation to set
	 */
	public void setCitation(String citation) {
		this.citation = citation;
	}

	/**
	 * @return the eagRelationType
	 */
	public String getEagRelationType() {
		return this.eagRelationType;
	}

	/**
	 * @param eagRelationType the eagRelationType to set
	 */
	public void setEagRelationType(String eagRelationType) {
		this.eagRelationType = eagRelationType;
	}

	/**
	 * @return the eagRelationHref
	 */
	public String getEagRelationHref() {
		return this.eagRelationHref;
	}

	/**
	 * @param eagRelationHref the eagRelationHref to set
	 */
	public void setEagRelationHref(String eagRelationHref) {
		this.eagRelationHref = eagRelationHref;
	}

	/**
	 * @return the eagRelationLang
	 */
	public String getEagRelationLang() {
		return this.eagRelationLang;
	}

	/**
	 * @param eagRelationLang the eagRelationLang to set
	 */
	public void setEagRelationLang(String eagRelationLang) {
		this.eagRelationLang = eagRelationLang;
	}

	/**
	 * @return the eagRelationrelationEntry
	 */
	public String getEagRelationrelationEntry() {
		return this.eagRelationrelationEntry;
	}

	/**
	 * @param eagRelationrelationEntry the eagRelationrelationEntry to set
	 */
	public void setEagRelationrelationEntry(String eagRelationrelationEntry) {
		this.eagRelationrelationEntry = eagRelationrelationEntry;
	}

	/**
	 * @return the eagRelationrelationEntryLocalType
	 */
	public String getEagRelationrelationEntryLocalType() {
		return this.eagRelationrelationEntryLocalType;
	}

	/**
	 * @param eagRelationrelationEntryLocalType the eagRelationrelationEntryLocalType to set
	 */
	public void setEagRelationrelationEntryLocalType(
			String eagRelationrelationEntryLocalType) {
		this.eagRelationrelationEntryLocalType = eagRelationrelationEntryLocalType;
	}

	/**
	 * @return the eagRelationrelationEntryScriptCode
	 */
	public String getEagRelationrelationEntryScriptCode() {
		return this.eagRelationrelationEntryScriptCode;
	}

	/**
	 * @param eagRelationrelationEntryScriptCode the eagRelationrelationEntryScriptCode to set
	 */
	public void setEagRelationrelationEntryScriptCode(
			String eagRelationrelationEntryScriptCode) {
		this.eagRelationrelationEntryScriptCode = eagRelationrelationEntryScriptCode;
	}

	/**
	 * @return the eagRelationrelationEntryTransliteration
	 */
	public String getEagRelationrelationEntryTransliteration() {
		return this.eagRelationrelationEntryTransliteration;
	}

	/**
	 * @param eagRelationrelationEntryTransliteration the eagRelationrelationEntryTransliteration to set
	 */
	public void setEagRelationrelationEntryTransliteration(
			String eagRelationrelationEntryTransliteration) {
		this.eagRelationrelationEntryTransliteration = eagRelationrelationEntryTransliteration;
	}

	/**
	 * @return the eagRelationrelationEntryLang
	 */
	public String getEagRelationrelationEntryLang() {
		return this.eagRelationrelationEntryLang;
	}

	/**
	 * @param eagRelationrelationEntryLang the eagRelationrelationEntryLang to set
	 */
	public void setEagRelationrelationEntryLang(
			String eagRelationrelationEntryLang) {
		this.eagRelationrelationEntryLang = eagRelationrelationEntryLang;
	}

	/**
	 * @return the eagRelationrelationEntryDescription
	 */
	public String getEagRelationrelationEntryDescription() {
		return this.eagRelationrelationEntryDescription;
	}

	/**
	 * @param eagRelationrelationEntryDescription the eagRelationrelationEntryDescription to set
	 */
	public void setEagRelationrelationEntryDescription(
			String eagRelationrelationEntryDescription) {
		this.eagRelationrelationEntryDescription = eagRelationrelationEntryDescription;
	}

	/**
	 * @return the eagRelationrelationEntryDescriptionLang
	 */
	public String getEagRelationrelationEntryDescriptionLang() {
		return this.eagRelationrelationEntryDescriptionLang;
	}

	/**
	 * @param eagRelationrelationEntryDescriptionLang the eagRelationrelationEntryDescriptionLang to set
	 */
	public void setEagRelationrelationEntryDescriptionLang(
			String eagRelationrelationEntryDescriptionLang) {
		this.eagRelationrelationEntryDescriptionLang = eagRelationrelationEntryDescriptionLang;
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
	public void loadValuesEAG2012() {
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
		if (this.eag.getArchguide().getIdentity().getRepositorid() != null) {
			this.setCountryCode(this.eag.getArchguide().getIdentity().getRepositorid().getCountrycode());
		}

		// Identifier of the institution.
		if (this.eag.getArchguide().getIdentity().getOtherRepositorId() != null) {
			this.setOtherRepositorId(this.eag.getArchguide().getIdentity().getOtherRepositorId().getContent());
		}

		// ID used in APE.
		if (this.eag.getControl().getRecordId() != null) {
			this.setRecordId(this.eag.getControl().getRecordId().getValue());
		}

		// Name of the institution.
		if (!this.eag.getArchguide().getIdentity().getAutform().isEmpty()) {
			for (int i = 0; i < this.eag.getArchguide().getIdentity().getAutform().size(); i++) {
				Autform autform = this.eag.getArchguide().getIdentity().getAutform().get(i);
				if (autform.getLang().equalsIgnoreCase(this.getCountryCode())) {
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
				if (parform.getLang().equalsIgnoreCase(this.getCountryCode())) {
					this.setParform(parform.getContent());
					this.setParformLang(parform.getLang());
				}
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
					this.setGeogarea(repository.getGeogarea().getValue());
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
				this.getRepositoryType().add(this.eag.getArchguide().getIdentity().getRepositoryType().get(i).getValue());
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
							for (int j = 0; j < repository.getDirections().size(); j++) {
								// TODO: Review schema
//								this.setDirections(repository.getDirections().get(i).getContent().get(j).toString());
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
					this.setLanguage(this.eag.getControl().getLanguageDeclarations().getLanguageDeclaration().get(i).getLanguage().getLanguageCode());
					this.setScript(this.eag.getControl().getLanguageDeclarations().getLanguageDeclaration().get(i).getScript().getScriptCode());
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
					this.setResourceRelationType(this.eag.getRelations().getResourceRelation().get(i).getResourceRelationType());
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
					this.setResourceRelationType(this.eag.getRelations().getEagRelation().get(i).getEagRelationType());
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