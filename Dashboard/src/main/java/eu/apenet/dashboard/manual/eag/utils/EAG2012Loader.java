package eu.apenet.dashboard.manual.eag.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

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
import eu.apenet.dpt.utils.eag2012.Date;
import eu.apenet.dpt.utils.eag2012.DateRange;
import eu.apenet.dpt.utils.eag2012.Eag;
import eu.apenet.dpt.utils.eag2012.Email;
import eu.apenet.dpt.utils.eag2012.Library;
import eu.apenet.dpt.utils.eag2012.Location;
import eu.apenet.dpt.utils.eag2012.Nonpreform;
import eu.apenet.dpt.utils.eag2012.OtherRecordId;
import eu.apenet.dpt.utils.eag2012.P;
import eu.apenet.dpt.utils.eag2012.Parform;
import eu.apenet.dpt.utils.eag2012.RecreationalServices;
import eu.apenet.dpt.utils.eag2012.Repository;
import eu.apenet.dpt.utils.eag2012.ResourceRelation;
import eu.apenet.dpt.utils.eag2012.Searchroom;
import eu.apenet.dpt.utils.eag2012.Telephone;
import eu.apenet.dpt.utils.eag2012.Timetable;
import eu.apenet.dpt.utils.eag2012.UseDates;
import eu.apenet.dpt.utils.eag2012.Webpage;
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
    private List<String> otherRecordId;
    private List<String> otherRecordIdLocalType;
    private String otherRepositorId;
    private String recordId;
    private String selfRecordId;
    private String autform;
    private String autformLang;
    private List<String> idAutform;
    private List<String> idAutformLang;
    private String parform;
    private String parformLang;
    private List<String> idParform;
    private List<String> idParformLang;
    private String localType;
    private String longitude;
    private String latitude;
    private String country;
    private String countryLang;
    private String municipalityPostalcode;
    private String municipalityPostalcodeLang;
    private String street;
    private String streetLang;
    private List<String> yiLongitude;
    private List<String> yiLatitude;
    private List<String> yiCountry;
    private List<String> yiCountryLang;
    private List<String> yiMunicipalityPostalcode;
    private List<String> yiMunicipalityPostalcodeLang;
    private List<String> yiStreet;
    private List<String> yiStreetLang;
    private String municipalityPostalcodePostal;
    private String municipalityPostalcodePostalLang;
    private String streetPostal;
    private String streetPostalLang;
    private List<String> yiMunicipalityPostalcodePostal;
    private List<String> yiMunicipalityPostalcodePostalLang;
    private List<String> yiStreetPostal;
    private List<String> yiStreetPostalLang;
    private String geogarea;
    private String telephone;
    private String email;
    private String emailTitle;
    private String emailLang;
    private List<String> yiEmail;
    private List<String> yiEmailTitle;
    private List<String> yiEmailLang;
    private String webpage;
    private String webpageTitle;
    private String webpageLang;
    private List<String> yiWebpage;
    private List<String> yiWebpageTitle;
    private List<String> yiWebpageLang;
    private String opening;
    private String openingLang;
    private List<String> yiOpening;
    private List<String> yiOpeningLang;
    private String closing;
    private String closingLang;
    private List<String> yiClosing;
    private List<String> yiClosingLang;
    private String accessQuestion;
    private String restaccess;
    private String restaccessLang;
    private List<String> yiRestaccess;
    private List<String> yiRestaccessLang;
    private String accessibilityQuestion;
    private String accessibility;
    private String accessibilityLang;
    private List<String> yiAccessibility;
    private List<String> yiAccessibilityLang;
    private String resourceRelationHref;
    private String resourceRelationrelationEntry;
    private List<String> yiResourceRelationHref;
    private List<String> yiResourceRelationrelationEntry;
    

	// Your institution tab.
    private String recordIdISIL;

	// Identity tab.
    private List<String> nonpreform;
    private List<String> nonpreformLang;
    private List<List<String>> nonpreformDate;
    private List<List<String>> nonpreformDateFrom;
    private List<List<String>> nonpreformDateTo;
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
    private List<String> abbreviation;
    private List<String> citation;

	// Relations.
    private String resourceRelationType;
    private String resourceRelationLang;
    private List<String> yiResourceRelationLang;
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
	/**
	 * @return the otherRecordId
	 */
	public List<String> getOtherRecordId() {
		if (this.otherRecordId == null) {
			this.otherRecordId = new ArrayList<String>();
		}
		return this.otherRecordId;
	}

	/**
	 * @param otherRecordId the otherRecordId to set
	 */
	public void setOtherRecordId(List<String> otherRecordId) {
		this.otherRecordId = otherRecordId;
	}

	/**
	 * @param otherRecordId the otherRecordId to add
	 */
	public void addOtherRecordId(String otherRecordId) {
		this.getOtherRecordId().add(otherRecordId);
	}
	/**
	 * @return the otherRecordIdLocalType
	 */
	public List<String> getOtherRecordIdLocalType() {
		if (this.otherRecordIdLocalType == null) {
			this.otherRecordIdLocalType = new ArrayList<String>();
		}
		return this.otherRecordIdLocalType;
	}

	/**
	 * @param otherRecordIdLocalType the otherRecordIdLocalType to set
	 */
	public void setOtherRecordIdLocalType(List<String> otherRecordIdLocalType) {
		this.otherRecordIdLocalType = otherRecordIdLocalType;
	}

	/**
	 * @param otherRecordIdLocalType the otherRecordIdLocalType to add
	 */
	public void addOtherRecordIdLocalType(String otherRecordIdLocalType) {
		this.getOtherRecordIdLocalType().add(otherRecordIdLocalType);
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
	/**
	 * @return the idAutform
	 */
	public List<String> getIdAutform() {
		if (this.idAutform == null) {
			this.idAutform = new ArrayList<String>();
		}
		return this.idAutform;
	}

	/**
	 * @param idAutform the idAutform to set
	 */
	public void setIdAutform(List<String> idAutform) {
		this.idAutform = idAutform;
	}

	/**
	 * @param idAutform the idAutform to add
	 */
	public void addIdAutform(String idAutform) {
		this.getIdAutform().add(idAutform);
	}

	/**
	 * @return the idAutformLang
	 */
	public List<String> getIdAutformLang() {
		if (this.idAutformLang == null) {
			this.idAutformLang = new ArrayList<String>();
		}
		return this.idAutformLang;
	}

	/**
	 * @param idAutformLang the idAutformLang to set
	 */
	public void setIdAutformLang(List<String> idAutformLang) {
		this.idAutformLang = idAutformLang;
	}

	/**
	 * @param idAutformLang the idAutformLang to add
	 */
	public void addIdAutformLang(String idAutformLang) {
		this.getIdAutformLang().add(idAutformLang);
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
	/**
	 * @return the idParform
	 */
	public List<String> getIdParform() {
		if (this.idParform == null) {
			this.idParform = new ArrayList<String>();
		}
		return this.idParform;
	}

	/**
	 * @param idParform the idParform to set
	 */
	public void setIdParform(List<String> idParform) {
		this.idParform = idParform;
	}

	/**
	 * @param idParform the idParform to add
	 */
	public void addIdParform(String idParform) {
		this.getIdParform().add(idParform);
	}

	/**
	 * @return the idParformLang
	 */
	public List<String> getIdParformLang() {
		if (this.idParformLang == null) {
			this.idParformLang = new ArrayList<String>();
		}
		return this.idParformLang;
	}

	/**
	 * @param idParformLang the idParformLang to set
	 */
	public void setIdParformLang(List<String> idParformLang) {
		this.idParformLang = idParformLang;
	}

	/**
	 * @param idParformLang the idParformLang to add
	 */
	public void addIdParformLang(String idParformLang) {
		this.getIdParformLang().add(idParformLang);
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
	/**
	 * @return the yiLongitude
	 */
	public List<String> getYiLongitude() {
		if (this.yiLongitude == null) {
			this.yiLongitude = new ArrayList<String>();
		}
		return this.yiLongitude;
	}

	/**
	 * @param yiLongitude the yiLongitude to set
	 */
	public void setYiLongitude(List<String> yiLongitude) {
		this.yiLongitude = yiLongitude;
	}

	/**
	 * @param yiLongitude the yiLongitude to add
	 */
	public void addYiLongitude(String yiLongitude) {
		this.getYiLongitude().add(yiLongitude);
	}

	/**
	 * @return the yiLatitude
	 */
	public List<String> getYiLatitude() {
		if (this.yiLatitude == null) {
			this.yiLatitude = new ArrayList<String>();
		}
		return this.yiLatitude;
	}

	/**
	 * @param yiLatitude the yiLatitude to set
	 */
	public void setYiLatitude(List<String> yiLatitude) {
		this.yiLatitude = yiLatitude;
	}

	/**
	 * @param yiLatitude the yiLatitude to add
	 */
	public void addYiLatitude(String yiLatitude) {
		this.getYiLatitude().add(yiLatitude);
	}

	/**
	 * @return the yiCountry
	 */
	public List<String> getYiCountry() {
		if (this.yiCountry == null) {
			this.yiCountry = new ArrayList<String>();
		}
		return this.yiCountry;
	}

	/**
	 * @param yiCountry the yiCountry to set
	 */
	public void setYiCountry(List<String> yiCountry) {
		this.yiCountry = yiCountry;
	}

	/**
	 * @param yiCountry the yiCountry to add
	 */
	public void addYiCountry(String yiCountry) {
		this.getYiCountry().add(yiCountry);
	}

	/**
	 * @return the yiCountryLang
	 */
	public List<String> getYiCountryLang() {
		if (this.yiCountryLang == null) {
			this.yiCountryLang = new ArrayList<String>();
		}
		return this.yiCountryLang;
	}

	/**
	 * @param yiCountryLang the yiCountryLang to set
	 */
	public void setYiCountryLang(List<String> yiCountryLang) {
		this.yiCountryLang = yiCountryLang;
	}

	/**
	 * @param yiCountryLang the yiCountryLang to add
	 */
	public void addYiCountryLang(String yiCountryLang) {
		this.getYiCountryLang().add(yiCountryLang);
	}

	/**
	 * @return the yiMunicipalityPostalcode
	 */
	public List<String> getYiMunicipalityPostalcode() {
		if (this.yiMunicipalityPostalcode == null) {
			this.yiMunicipalityPostalcode = new ArrayList<String>();
		}
		return this.yiMunicipalityPostalcode;
	}

	/**
	 * @param yiMunicipalityPostalcode the yiMunicipalityPostalcode to set
	 */
	public void setYiMunicipalityPostalcode(List<String> yiMunicipalityPostalcode) {
		this.yiMunicipalityPostalcode = yiMunicipalityPostalcode;
	}

	/**
	 * @param yiMunicipalityPostalcode the yiMunicipalityPostalcode to add
	 */
	public void addYiMunicipalityPostalcode(String yiMunicipalityPostalcode) {
		this.getYiMunicipalityPostalcode().add(yiMunicipalityPostalcode);
	}

	/**
	 * @return the yiMunicipalityPostalcodeLang
	 */
	public List<String> getYiMunicipalityPostalcodeLang() {
		if (this.yiMunicipalityPostalcodeLang == null) {
			this.yiMunicipalityPostalcodeLang = new ArrayList<String>();
		}
		return this.yiMunicipalityPostalcodeLang;
	}

	/**
	 * @param yiMunicipalityPostalcodeLang the yiMunicipalityPostalcodeLang to set
	 */
	public void setYiMunicipalityPostalcodeLang(
			List<String> yiMunicipalityPostalcodeLang) {
		this.yiMunicipalityPostalcodeLang = yiMunicipalityPostalcodeLang;
	}

	/**
	 * @param yiMunicipalityPostalcodeLang the yiMunicipalityPostalcodeLang to add
	 */
	public void addYiMunicipalityPostalcodeLang(String yiMunicipalityPostalcodeLang) {
		this.getYiMunicipalityPostalcodeLang().add(yiMunicipalityPostalcodeLang);
	}

	/**
	 * @return the yiStreet
	 */
	public List<String> getYiStreet() {
		if (this.yiStreet == null) {
			this.yiStreet = new ArrayList<String>();
		}
		return this.yiStreet;
	}

	/**
	 * @param yiStreet the yiStreet to set
	 */
	public void setYiStreet(List<String> yiStreet) {
		this.yiStreet = yiStreet;
	}

	/**
	 * @param yiStreet the yiStreet to add
	 */
	public void addYiStreet(String yiStreet) {
		this.getYiStreet().add(yiStreet);
	}

	/**
	 * @return the yiStreetLang
	 */
	public List<String> getYiStreetLang() {
		if (this.yiStreetLang == null) {
			this.yiStreetLang = new ArrayList<String>();
		}
		return this.yiStreetLang;
	}

	/**
	 * @param yiStreetLang the yiStreetLang to set
	 */
	public void setYiStreetLang(List<String> yiStreetLang) {
		this.yiStreetLang = yiStreetLang;
	}

	/**
	 * @param yiStreetLang the yiStreetLang to add
	 */
	public void addYiStreetLang(String yiStreetLang) {
		this.getYiStreetLang().add(yiStreetLang);
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
	/**
	 * @return the yiMunicipalityPostalcodePostal
	 */
	public List<String> getYiMunicipalityPostalcodePostal() {
		if (this.yiMunicipalityPostalcodePostal == null) {
			this.yiMunicipalityPostalcodePostal = new ArrayList<String>();
		}
		return this.yiMunicipalityPostalcodePostal;
	}

	/**
	 * @param yiMunicipalityPostalcodePostal the yiMunicipalityPostalcodePostal to set
	 */
	public void setYiMunicipalityPostalcodePostal(
			List<String> yiMunicipalityPostalcodePostal) {
		this.yiMunicipalityPostalcodePostal = yiMunicipalityPostalcodePostal;
	}

	/**
	 * @param yiMunicipalityPostalcodePostal the yiMunicipalityPostalcodePostal to add
	 */
	public void addYiMunicipalityPostalcodePostal(String yiMunicipalityPostalcodePostal) {
		this.getYiMunicipalityPostalcodePostal().add(yiMunicipalityPostalcodePostal);
	}

	/**
	 * @return the yiMunicipalityPostalcodePostalLang
	 */
	public List<String> getYiMunicipalityPostalcodePostalLang() {
		if (this.yiMunicipalityPostalcodePostalLang == null) {
			this.yiMunicipalityPostalcodePostalLang = new ArrayList<String>();
		}
		return this.yiMunicipalityPostalcodePostalLang;
	}

	/**
	 * @param yiMunicipalityPostalcodePostalLang the yiMunicipalityPostalcodePostalLang to set
	 */
	public void setYiMunicipalityPostalcodePostalLang(
			List<String> yiMunicipalityPostalcodePostalLang) {
		this.yiMunicipalityPostalcodePostalLang = yiMunicipalityPostalcodePostalLang;
	}

	/**
	 * @param yiMunicipalityPostalcodePostalLang the yiMunicipalityPostalcodePostalLang to add
	 */
	public void addYiMunicipalityPostalcodePostalLang(String yiMunicipalityPostalcodePostalLang) {
		this.getYiMunicipalityPostalcodePostalLang().add(yiMunicipalityPostalcodePostalLang);
	}

	/**
	 * @return the yiStreetPostal
	 */
	public List<String> getYiStreetPostal() {
		if (this.yiStreetPostal == null) {
			this.yiStreetPostal = new ArrayList<String>();
		}
		return this.yiStreetPostal;
	}

	/**
	 * @param yiStreetPostal the yiStreetPostal to set
	 */
	public void setYiStreetPostal(List<String> yiStreetPostal) {
		this.yiStreetPostal = yiStreetPostal;
	}

	/**
	 * @param yiStreetPostal the yiStreetPostal to add
	 */
	public void addYiStreetPostal(String yiStreetPostal) {
		this.getYiStreetPostal().add(yiStreetPostal);
	}

	/**
	 * @return the yiStreetPostalLang
	 */
	public List<String> getYiStreetPostalLang() {
		if (this.yiStreetPostalLang == null) {
			this.yiStreetPostalLang = new ArrayList<String>();
		}
		return this.yiStreetPostalLang;
	}

	/**
	 * @param yiStreetPostalLang the yiStreetPostalLang to set
	 */
	public void setYiStreetPostalLang(List<String> yiStreetPostalLang) {
		this.yiStreetPostalLang = yiStreetPostalLang;
	}

	/**
	 * @param yiStreetPostalLang the yiStreetPostalLang to add
	 */
	public void addYiStreetPostalLang(String yiStreetPostalLang) {
		this.getYiStreetPostalLang().add(yiStreetPostalLang);
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
	/**
	 * @return the yiEmail
	 */
	public List<String> getYiEmail() {
		if (this.yiEmail == null) {
			this.yiEmail = new ArrayList<String>();
		}
		return this.yiEmail;
	}

	/**
	 * @param yiEmail the yiEmail to set
	 */
	public void setYiEmail(List<String> yiEmail) {
		this.yiEmail = yiEmail;
	}

	/**
	 * @param yiEmail the yiEmail to add
	 */
	public void addYiEmail(String yiEmail) {
		this.getYiEmail().add(yiEmail);
	}

	/**
	 * @return the yiEmailTitle
	 */
	public List<String> getYiEmailTitle() {
		if (this.yiEmailTitle == null) {
			this.yiEmailTitle = new ArrayList<String>();
		}
		return this.yiEmailTitle;
	}

	/**
	 * @param yiEmailTitle the yiEmailTitle to set
	 */
	public void setYiEmailTitle(List<String> yiEmailTitle) {
		this.yiEmailTitle = yiEmailTitle;
	}

	/**
	 * @param yiEmailTitle the yiEmailTitle to add
	 */
	public void addYiEmailTitle(String yiEmailTitle) {
		this.getYiEmailTitle().add(yiEmailTitle);
	}

	/**
	 * @return the yiEmailLang
	 */
	public List<String> getYiEmailLang() {
		if (this.yiEmailLang == null) {
			this.yiEmailLang = new ArrayList<String>();
		}
		return this.yiEmailLang;
	}

	/**
	 * @param yiEmailLang the yiEmailLang to set
	 */
	public void setYiEmailLang(List<String> yiEmailLang) {
		this.yiEmailLang = yiEmailLang;
	}

	/**
	 * @param yiEmailLang the yiEmailLang to add
	 */
	public void addYiEmailLang(String yiEmailLang) {
		this.getYiEmailLang().add(yiEmailLang);
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
	/**
	 * @return the yiWebpage
	 */
	public List<String> getYiWebpage() {
		if (this.yiWebpage == null) {
			this.yiWebpage = new ArrayList<String>();
		}
		return this.yiWebpage;
	}

	/**
	 * @param yiWebpage the yiWebpage to set
	 */
	public void setYiWebpage(List<String> yiWebpage) {
		this.yiWebpage = yiWebpage;
	}

	/**
	 * @param yiWebpage the yiWebpage to add
	 */
	public void addYiWebpage(String yiWebpage) {
		this.getYiWebpage().add(yiWebpage);
	}

	/**
	 * @return the yiWebpageTitle
	 */
	public List<String> getYiWebpageTitle() {
		if (this.yiWebpageTitle == null) {
			this.yiWebpageTitle = new ArrayList<String>();
		}
		return this.yiWebpageTitle;
	}

	/**
	 * @param yiWebpageTitle the yiWebpageTitle to set
	 */
	public void setYiWebpageTitle(List<String> yiWebpageTitle) {
		this.yiWebpageTitle = yiWebpageTitle;
	}

	/**
	 * @param yiWebpageTitle the yiWebpageTitle to add
	 */
	public void addYiWebpageTitle(String yiWebpageTitle) {
		this.getYiWebpageTitle().add(yiWebpageTitle);
	}

	/**
	 * @return the yiWebpageLang
	 */
	public List<String> getYiWebpageLang() {
		if (this.yiWebpageLang == null) {
			this.yiWebpageLang = new ArrayList<String>();
		}
		return this.yiWebpageLang;
	}

	/**
	 * @param yiWebpageLang the yiWebpageLang to set
	 */
	public void setYiWebpageLang(List<String> yiWebpageLang) {
		this.yiWebpageLang = yiWebpageLang;
	}

	/**
	 * @param yiWebpageLang the yiWebpageLang to add
	 */
	public void addYiWebpageLang(String yiWebpageLang) {
		this.getYiWebpageLang().add(yiWebpageLang);
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
	/**
	 * @return the yiOpening
	 */
	public List<String> getYiOpening() {
		if (this.yiOpening == null) {
			this.yiOpening = new ArrayList<String>();
		}
		return this.yiOpening;
	}

	/**
	 * @param yiOpening the yiOpening to set
	 */
	public void setYiOpening(List<String> yiOpening) {
		this.yiOpening = yiOpening;
	}

	/**
	 * @param yiOpening the yiOpening to add
	 */
	public void addYiOpening(String yiOpening) {
		this.getYiOpening().add(yiOpening);
	}

	/**
	 * @return the yiOpeningLang
	 */
	public List<String> getYiOpeningLang() {
		if (this.yiOpeningLang == null) {
			this.yiOpeningLang = new ArrayList<String>();
		}
		return this.yiOpeningLang;
	}

	/**
	 * @param yiOpeningLang the yiOpeningLang to set
	 */
	public void setYiOpeningLang(List<String> yiOpeningLang) {
		this.yiOpeningLang = yiOpeningLang;
	}

	/**
	 * @param yiOpeningLang the yiOpeningLang to add
	 */
	public void addYiOpeningLang(String yiOpeningLang) {
		this.getYiOpeningLang().add(yiOpeningLang);
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
	/**
	 * @return the yiClosing
	 */
	public List<String> getYiClosing() {
		if (this.yiClosing == null) {
			this.yiClosing = new ArrayList<String>();
		}
		return this.yiClosing;
	}

	/**
	 * @param yiClosing the yiClosing to set
	 */
	public void setYiClosing(List<String> yiClosing) {
		this.yiClosing = yiClosing;
	}

	/**
	 * @param yiClosing the yiClosing to add
	 */
	public void addYiClosing(String yiClosing) {
		this.getYiClosing().add(yiClosing);
	}

	/**
	 * @return the yiClosingLang
	 */
	public List<String> getYiClosingLang() {
		if (this.yiClosingLang == null) {
			this.yiClosingLang = new ArrayList<String>();
		}
		return yiClosingLang;
	}

	/**
	 * @param yiClosingLang the yiClosingLang to set
	 */
	public void setYiClosingLang(List<String> yiClosingLang) {
		this.yiClosingLang = yiClosingLang;
	}

	/**
	 * @param yiClosingLang the yiClosingLang to add
	 */
	public void addYiClosingLang(String yiClosingLang) {
		this.getYiClosingLang().add(yiClosingLang);
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
	/**
	 * @return the yiRestaccess
	 */
	public List<String> getYiRestaccess() {
		if (this.yiRestaccess == null) {
			this.yiRestaccess = new ArrayList<String>();
		}
		return this.yiRestaccess;
	}

	/**
	 * @param yiRestaccess the yiRestaccess to set
	 */
	public void setYiRestaccess(List<String> yiRestaccess) {
		this.yiRestaccess = yiRestaccess;
	}

	/**
	 * @param yiRestaccess the yiRestaccess to add
	 */
	public void addYiRestaccess(String yiRestaccess) {
		this.getYiRestaccess().add(yiRestaccess);
	}

	/**
	 * @return the yiRestaccessLang
	 */
	public List<String> getYiRestaccessLang() {
		if (this.yiRestaccessLang == null) {
			this.yiRestaccessLang = new ArrayList<String>();
		}
		return this.yiRestaccessLang;
	}

	/**
	 * @param yiRestaccessLang the yiRestaccessLang to set
	 */
	public void setYiRestaccessLang(List<String> yiRestaccessLang) {
		this.yiRestaccessLang = yiRestaccessLang;
	}

	/**
	 * @param yiRestaccessLang the yiRestaccessLang to add
	 */
	public void addYiRestaccessLang(String yiRestaccessLang) {
		this.getYiRestaccessLang().add(yiRestaccessLang);
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
	/**
	 * @return the yiAccessibility
	 */
	public List<String> getYiAccessibility() {
		if (this.yiAccessibility == null) {
			this.yiAccessibility = new ArrayList<String>();
		}
		return yiAccessibility;
	}

	/**
	 * @param yiAccessibility the yiAccessibility to set
	 */
	public void setYiAccessibility(List<String> yiAccessibility) {
		this.yiAccessibility = yiAccessibility;
	}

	/**
	 * @param yiAccessibility the yiAccessibility to add
	 */
	public void addYiAccessibility(String yiAccessibility) {
		this.getYiAccessibility().add(yiAccessibility);
	}

	/**
	 * @return the yiAccessibilityLang
	 */
	public List<String> getYiAccessibilityLang() {
		if (this.yiAccessibilityLang == null) {
			this.yiAccessibilityLang = new ArrayList<String>();
		}
		return this.yiAccessibilityLang;
	}

	/**
	 * @param yiAccessibilityLang the yiAccessibilityLang to set
	 */
	public void setYiAccessibilityLang(List<String> yiAccessibilityLang) {
		this.yiAccessibilityLang = yiAccessibilityLang;
	}

	/**
	 * @param yiAccessibilityLang the yiAccessibilityLang to add
	 */
	public void addYiAccessibilityLang(String yiAccessibilityLang) {
		this.getYiAccessibilityLang().add(yiAccessibilityLang);
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
	/**
	 * @return the yiResourceRelationHref
	 */
	public List<String> getYiResourceRelationHref() {
		if (this.yiResourceRelationHref == null) {
			this.yiResourceRelationHref = new ArrayList<String>();
		}
		return this.yiResourceRelationHref;
	}

	/**
	 * @param yiResourceRelationHref the yiResourceRelationHref to set
	 */
	public void setYiResourceRelationHref(List<String> yiResourceRelationHref) {
		this.yiResourceRelationHref = yiResourceRelationHref;
	}

	/**
	 * @param yiResourceRelationHref the yiResourceRelationHref to add
	 */
	public void addYiResourceRelationHref(String yiResourceRelationHref) {
		this.getYiResourceRelationHref().add(yiResourceRelationHref);
	}

	/**
	 * @return the yiResourceRelationrelationEntry
	 */
	public List<String> getYiResourceRelationrelationEntry() {
		if (this.yiResourceRelationrelationEntry == null) {
			this.yiResourceRelationrelationEntry = new ArrayList<String>();
		}
		return this.yiResourceRelationrelationEntry;
	}

	/**
	 * @param yiResourceRelationrelationEntry the yiResourceRelationrelationEntry to set
	 */
	public void setYiResourceRelationrelationEntry(List<String> yiResourceRelationrelationEntry) {
		this.yiResourceRelationrelationEntry = yiResourceRelationrelationEntry;
	}

	/**
	 * @param yiResourceRelationrelationEntry the yiResourceRelationrelationEntry to add
	 */
	public void addYiResourceRelationrelationEntry(String yiResourceRelationrelationEntry) {
		this.getYiResourceRelationrelationEntry().add(yiResourceRelationrelationEntry);
	}

	/**
	 * @return the yiResourceRelationLang
	 */
	public List<String> getYiResourceRelationLang() {
		if (this.yiResourceRelationLang == null) {
			this.yiResourceRelationLang = new ArrayList<String>();
		}
		return this.yiResourceRelationLang;
	}

	/**
	 * @param yiResourceRelationLang the yiResourceRelationLang to set
	 */
	public void setYiResourceRelationLang(List<String> yiResourceRelationLang) {
		this.yiResourceRelationLang = yiResourceRelationLang;
	}

	/**
	 * @param yiResourceRelationLang the yiResourceRelationLang to add
	 */
	public void addYiResourceRelationLang(String yiResourceRelationLang) {
		this.getYiResourceRelationLang().add(yiResourceRelationLang);
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

	/**
	 * @return the nonpreform
	 */
	public List<String> getNonpreform() {
		if (this.nonpreform == null) {
			this.nonpreform = new ArrayList<String>();
		}
		return this.nonpreform;
	}

	/**
	 * @param nonpreform the nonpreform to set
	 */
	public void setNonpreform(List<String> nonpreform) {
		this.nonpreform = nonpreform;
	}

	/**
	 * @param nonpreform the nonpreform to add
	 */
	public void addNonpreform(String nonpreform) {
		this.getNonpreform().add(nonpreform);
	}

	/**
	 * @return the nonpreformLang
	 */
	public List<String> getNonpreformLang() {
		if (this.nonpreformLang == null) {
			this.nonpreformLang = new ArrayList<String>();
		}
		return this.nonpreformLang;
	}

	/**
	 * @param nonpreformLang the nonpreformLang to set
	 */
	public void setNonpreformLang(List<String> nonpreformLang) {
		this.nonpreformLang = nonpreformLang;
	}

	/**
	 * @param nonpreformLang the nonpreformLang to add
	 */
	public void addNonpreformLang(String nonpreformLang) {
		this.getNonpreformLang().add(nonpreformLang);
	}

	/**
	 * @return the nonpreformDate
	 */
	public List<List<String>> getNonpreformDate() {
		if (this.nonpreformDate == null) {
			this.nonpreformDate = new ArrayList<List<String>>();
			
		}
		return this.nonpreformDate;
	}

	/**
	 * @param nonpreformDate the nonpreformDate to set
	 */
	public void setNonpreformDate(List<List<String>> nonpreformDate) {
		this.nonpreformDate = nonpreformDate;
	}

	/**
	 * @param nonpreformDate the nonpreformDate to add
	 */
	public void addNonpreformDate(List<String> nonpreformDate) {
		this.getNonpreformDate().add(nonpreformDate);
	}

	/**
	 * @return the nonpreformDateFrom
	 */
	public List<List<String>> getNonpreformDateFrom() {
		if (this.nonpreformDateFrom == null) {
			this.nonpreformDateFrom = new ArrayList<List<String>>();
		}
		return this.nonpreformDateFrom;
	}

	/**
	 * @param nonpreformDateFrom the nonpreformDateFrom to set
	 */
	public void setNonpreformDateFrom(List<List<String>> nonpreformDateFrom) {
		this.nonpreformDateFrom = nonpreformDateFrom;
	}

	/**
	 * @param nonpreformDateFrom the nonpreformDateFrom to add
	 */
	public void addNonpreformDateFrom(List<String> nonpreformDateFrom) {
		this.getNonpreformDateFrom().add(nonpreformDateFrom);
	}

	/**
	 * @return the nonpreformDateTo
	 */
	public List<List<String>> getNonpreformDateTo() {
		if (this.nonpreformDateTo == null) {
			this.nonpreformDateTo = new ArrayList<List<String>>();
		}
		return this.nonpreformDateTo;
	}

	/**
	 * @param nonpreformDateTo the nonpreformDateTo to set
	 */
	public void setNonpreformDateTo(List<List<String>> nonpreformDateTo) {
		this.nonpreformDateTo = nonpreformDateTo;
	}

	/**
	 * @param nonpreformDateTo the nonpreformDateTo to add
	 */
	public void addNonpreformDateTo(List<String> nonpreformDateTo) {
		this.getNonpreformDateTo().add(nonpreformDateTo);
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

	/**
	 * @param repositoryType the repositoryType to add
	 */
	public void addRepositoryType(String repositoryType) {
		this.getRepositoryType().add(repositoryType);
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
	public List<String> getAbbreviation() {
		return this.abbreviation;
	}
	public void setAbbreviation(List<String> abbreviation) {
		this.abbreviation = abbreviation;
	}
	public List<String> getCitation() {
		return this.citation;
	}
	public void setCitation(List<String> citation) {
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
				&& this.eag.getArchguide().getIdentity() != null
				&& this.eag.getArchguide().getIdentity().getRepositorid().getCountrycode() !=  null
				&& !this.eag.getArchguide().getIdentity().getRepositorid().getCountrycode().isEmpty()) {
			this.setCountryCode(this.eag.getArchguide().getIdentity().getRepositorid().getCountrycode());
		} else {
			this.setCountryCode(new ArchivalLandscape().getmyCountry());
		}

		// Identifier of the institution.
		if (this.eag.getArchguide().getIdentity() != null
				&& this.eag.getArchguide().getIdentity().getOtherRepositorId() != null) {
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

		// Further IDs.
		if (!this.eag.getControl().getOtherRecordId().isEmpty()) {
			for (int i = 0; i < this.eag.getControl().getOtherRecordId().size(); i++) {
				OtherRecordId otherRecordId = this.eag.getControl().getOtherRecordId().get(i);
				if (!otherRecordId.getValue().equalsIgnoreCase(this.getRecordId())
						&& !otherRecordId.getValue().equalsIgnoreCase(this.getOtherRepositorId())) {
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
		if (this.eag.getControl().getRecordId() != null) {
			this.setRecordId(this.eag.getControl().getRecordId().getValue());
		}

		// Name of the institution.
		if (!this.eag.getArchguide().getIdentity().getAutform().isEmpty()) {
			for (int i = 0; i < this.eag.getArchguide().getIdentity().getAutform().size(); i++) {
				Autform autform = this.eag.getArchguide().getIdentity().getAutform().get(i);
//				if (autform!=null && autform.getLang()!=null && autform.getLang().equalsIgnoreCase(this.getCountryCode())) {
//					this.setAutform(autform.getContent());
//					this.setAutformLang((autform.getLang()!=null)?new Locale(LanguageConverter.get639_1Code(autform.getLang())).getISO3Language():null);
//				}
				if (autform != null && autform.getContent() != null && !autform.getContent().isEmpty()) {
					this.addIdAutform(autform.getContent());
					if (autform.getLang() != null && !autform.getLang().isEmpty()) {
						this.addIdAutformLang(autform.getLang());
					} else {
						this.addIdAutformLang(Eag2012.OPTION_NONE);
					}
				}
			}
//			if (this.getAutform() == null || this.getAutform() == "") {
				this.setAutform(this.eag.getArchguide().getIdentity().getAutform().get(0).getContent());
				String lang = this.eag.getArchguide().getIdentity().getAutform().get(0).getLang();
				this.setAutformLang(lang);
//				this.setAutformLang((lang!=null)?new Locale(LanguageConverter.get639_1Code(lang)).getISO3Country():lang);
//			}
		}

		// Parallel name of the institution.
		if (!this.eag.getArchguide().getIdentity().getParform().isEmpty()) {
			for (int i = 0; i < this.eag.getArchguide().getIdentity().getParform().size(); i++) {
				Parform parform = this.eag.getArchguide().getIdentity().getParform().get(i);
//				if (parform.getLang().equalsIgnoreCase(this.getCountryCode())) {
//					this.setParform(parform.getContent());
//					this.setParformLang(parform.getLang());
//				}
				if (parform != null && parform.getContent() != null && !parform.getContent().isEmpty()) {
					this.addIdParform(parform.getContent());
					if (parform.getLang() != null && !parform.getLang().isEmpty()) {
						this.addIdParformLang(parform.getLang());
					} else {
						this.addIdParformLang(Eag2012.OPTION_NONE);
					}
				}
			}
//			if (this.getParform() == null || this.getParform() == "") {
				this.setParform(this.eag.getArchguide().getIdentity().getParform().get(0).getContent());
				this.setParformLang(this.eag.getArchguide().getIdentity().getParform().get(0).getLang());
//			}
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
							if (location.getStreet() != null) {
								if (location.getStreet().getContent() != null
										&& !location.getStreet().getContent().isEmpty()) {
									this.setStreet(location.getStreet().getContent());
								}
								if (location.getStreet().getLang() != null
										&& !location.getStreet().getLang().isEmpty()) {
									this.setStreetLang(location.getStreet().getLang());
								} else {
									this.setStreetLang(Eag2012.OPTION_NONE);
								}
							}
							// City.
							if (location.getMunicipalityPostalcode() != null) {
								if (location.getMunicipalityPostalcode().getContent() != null
										&& !location.getMunicipalityPostalcode().getContent().isEmpty()) {
									this.setMunicipalityPostalcode(location.getMunicipalityPostalcode().getContent());
								}
								if (location.getMunicipalityPostalcode().getLang() != null
										&& !location.getMunicipalityPostalcode().getLang().isEmpty()) {
									this.setMunicipalityPostalcodeLang(location.getStreet().getLang());
								} else {
									this.setMunicipalityPostalcodeLang(Eag2012.OPTION_NONE);
								}
							}
							// Country.
							if (location.getCountry() != null) {
								if (location.getCountry().getContent() != null
										&& !location.getCountry().getContent().isEmpty()) {
									this.setCountry(location.getCountry().getContent());
								}
								if (location.getCountry().getLang() != null
										&& !location.getCountry().getLang().isEmpty()) {
									this.setCountryLang(location.getCountry().getLang());
								} else {
									this.setCountryLang(Eag2012.OPTION_NONE);
								}
							}
							// Latitude.
							this.setLatitude(location.getLatitude());
							// Longitude.
							this.setLongitude(location.getLongitude());
						}
						// TODO: Review for multiple translations.
						if (location.getLocalType().equalsIgnoreCase(Eag2012.POSTAL_ADDRESS)) {
							// Postal street.
							if (location.getStreet() != null) {
								if (location.getStreet().getContent() != null
										&& !location.getStreet().getContent().isEmpty()) {
									this.setStreetPostal(location.getStreet().getContent());
								}
								if (location.getStreet().getLang() != null
										&& !location.getStreet().getLang().isEmpty()) {
									this.setStreetPostalLang(location.getStreet().getLang());
								} else {
									this.setStreetPostalLang(Eag2012.OPTION_NONE);
								}
							}
							// Postal city.
							if (location.getMunicipalityPostalcode() != null) {
								if (location.getMunicipalityPostalcode().getContent() != null
										&& !location.getMunicipalityPostalcode().getContent().isEmpty()) {
									this.setMunicipalityPostalcodePostal(location.getMunicipalityPostalcode().getContent());
								}
								if (location.getMunicipalityPostalcode().getLang() != null
										&& !location.getMunicipalityPostalcode().getLang().isEmpty()) {
									this.setMunicipalityPostalcodePostalLang(location.getStreet().getLang());
								} else {
									this.setMunicipalityPostalcodePostalLang(Eag2012.OPTION_NONE);
								}
							}
						}
					}
				}
				// Visitor & Postal address for institution.
				if (!repository.getLocation().isEmpty()) {
					for (int i = 0; i < repository.getLocation().size(); i++) {
						Location location = repository.getLocation().get(i);
						if (location.getLocalType().equalsIgnoreCase(Eag2012.VISITORS_ADDRESS)) {
							// Street.
							if (location.getStreet() != null) {
								if (location.getStreet().getContent() != null
										&& !location.getStreet().getContent().isEmpty()) {
									this.addYiStreet(location.getStreet().getContent());
								} else {
									this.addYiStreet("");
								}
								if (location.getStreet().getLang() != null
										&& !location.getStreet().getLang().isEmpty()) {
									this.addYiStreetLang(location.getStreet().getLang());
								} else {
									this.addYiStreetLang(Eag2012.OPTION_NONE);
								}
							}
							// City.
							if (location.getMunicipalityPostalcode() != null) {
								if (location.getMunicipalityPostalcode().getContent() != null
										&& !location.getMunicipalityPostalcode().getContent().isEmpty()) {
									this.addYiMunicipalityPostalcode(location.getMunicipalityPostalcode().getContent());
								} else {
									this.addYiMunicipalityPostalcode("");
								}
								if (location.getMunicipalityPostalcode().getLang() != null
										&& !location.getMunicipalityPostalcode().getLang().isEmpty()) {
									this.addYiMunicipalityPostalcodeLang(location.getMunicipalityPostalcode().getLang());
								} else if (location.getStreet().getLang() != null
										&& !location.getStreet().getLang().isEmpty()) {
									this.addYiMunicipalityPostalcodeLang(location.getStreet().getLang());
								} else {
									this.addYiMunicipalityPostalcodeLang(Eag2012.OPTION_NONE);
								}
							}
							// Country.
							if (location.getCountry() != null) {
								if (location.getCountry().getContent() != null
										&& !location.getCountry().getContent().isEmpty()) {
									this.addYiCountry(location.getCountry().getContent());
								} else {
									this.addYiCountry("");
								}
								if (location.getCountry().getLang() != null
										&& !location.getCountry().getLang().isEmpty()) {
									this.addYiCountryLang(location.getCountry().getLang());
								} else if (location.getStreet().getLang() != null
										&& !location.getStreet().getLang().isEmpty()) {
									this.addYiCountryLang(location.getStreet().getLang());
								} else {
									this.addYiCountryLang(Eag2012.OPTION_NONE);
								}
							}
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
						}
						if (location.getLocalType().equalsIgnoreCase(Eag2012.POSTAL_ADDRESS)) {
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

					// E-mail address for institution.
					for (int i = 0; i < repository.getEmail().size(); i++) {
						this.addYiEmail(repository.getEmail().get(i).getHref());
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

				// Webpage.
				if (!repository.getWebpage().isEmpty()) {
					// TODO: Review for multiple values.
					for (int i = 0; i < repository.getWebpage().size(); i++) {
						this.setWebpage(repository.getWebpage().get(i).getHref());
						this.setWebpageTitle(repository.getWebpage().get(i).getContent());
						this.setWebpageLang(repository.getWebpage().get(i).getLang());
					}

					// Webpage address for institution.
					for (int i = 0; i < repository.getWebpage().size(); i++) {
						this.addYiWebpage(repository.getWebpage().get(i).getHref());
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
						// Opening times for institution.
						for (int i = 0; i < timetable.getOpening().size(); i++) {
							this.addYiOpening(timetable.getOpening().get(i).getContent());
							if (!timetable.getOpening().isEmpty()
									&& timetable.getOpening().size() >= i
									&& timetable.getOpening().get(i).getLang() != null
									&& !timetable.getOpening().get(i).getLang().isEmpty()) {
								this.addYiOpeningLang(timetable.getOpening().get(i).getLang());
							} else {
								this.addYiOpeningLang(Eag2012.OPTION_NONE);
							}
						}
						
					}

					// Closing dates.
					if (!timetable.getClosing().isEmpty()) {
						// TODO: Review for multiple values.
						for (int i = 0; i < timetable.getClosing().size(); i++) {
							this.setClosing(timetable.getClosing().get(i).getContent());
							this.setClosingLang(timetable.getClosing().get(i).getLang());
						}
						// Closing dates for institution.
						for (int i = 0; i < timetable.getClosing().size(); i++) {
							this.addYiClosing(timetable.getClosing().get(i).getContent());
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

				// Accessible to the public.
				if (repository.getAccess() != null) {
					this.setAccessQuestion(repository.getAccess().getQuestion());
					if (!repository.getAccess().getRestaccess().isEmpty()) {
						// TODO: Review for multiple values.
						for (int i = 0; i < repository.getAccess().getRestaccess().size(); i++) {
							this.setRestaccess(repository.getAccess().getRestaccess().get(i).getContent());
							this.setRestaccessLang(repository.getAccess().getRestaccess().get(i).getLang());
						}

						// Accessible to the public for institution.
						for (int i = 0; i < repository.getAccess().getRestaccess().size(); i++) {
							this.addYiRestaccess(repository.getAccess().getRestaccess().get(i).getContent());
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

				// Facilities for disabled people available.
				if (!repository.getAccessibility().isEmpty()) {
					// TODO: Review for multiple values.
					for (int i = 0; i < repository.getAccessibility().size(); i++) {
						this.setAccessibilityQuestion(repository.getAccessibility().get(i).getQuestion());
						if (repository.getAccessibility().get(i).getContent() != null
								&& !repository.getAccessibility().get(i).getContent().isEmpty()) {
							this.setAccessibility(repository.getAccessibility().get(i).getContent());
							this.setAccessibilityLang(repository.getAccessibility().get(i).getLang());
						}
					}

					// Facilities for disabled people available for institution.
					for (int i = 0; i < repository.getAccessibility().size(); i++) {
						if (repository.getAccessibility().get(i).getContent() != null
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
			}
		}

		// Reference to your institution’s holdings guide.
		if (this.eag.getRelations()!=null && this.eag.getRelations().getResourceRelation()!=null && !this.eag.getRelations().getResourceRelation().isEmpty()) {
			// TODO: Review for multiple values.
			for (int i = 0; i < this.eag.getRelations().getResourceRelation().size(); i++) {
				this.setResourceRelationHref(this.eag.getRelations().getResourceRelation().get(i).getHref());
				if (this.eag.getRelations().getResourceRelation().get(i).getRelationEntry() != null) {
					this.setResourceRelationrelationEntry(this.eag.getRelations().getResourceRelation().get(i).getRelationEntry().getContent());
					this.setResourceRelationLang(this.eag.getRelations().getResourceRelation().get(i).getRelationEntry().getLang());
				}
			}

			// Reference to your institution’s holdings guide for institution.
			for (int i = 0; i < this.eag.getRelations().getResourceRelation().size(); i++) {
				ResourceRelation resourceRelation = this.eag.getRelations().getResourceRelation().get(i);

				if (Eag2012.OPTION_CREATOR_TEXT.equalsIgnoreCase(resourceRelation.getResourceRelationType())) {
					this.addYiResourceRelationHref(resourceRelation.getHref());
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
	}

	/**
	 * Method to load all values of "Identity" tab.
	 */
	private void loadIdentityTabValues() {
		//  Formerly used names of the institution.
		if (!this.eag.getArchguide().getIdentity().getNonpreform().isEmpty()) {
			for (int i = 0; i < this.eag.getArchguide().getIdentity().getNonpreform().size(); i++) {
				Nonpreform nonpreform = this.eag.getArchguide().getIdentity().getNonpreform().get(i);
				if (nonpreform != null && !nonpreform.getContent().isEmpty()) {
					// Lang.
					if (nonpreform.getLang() != null && !nonpreform.getLang().isEmpty()) {
						this.addNonpreformLang(nonpreform.getLang());
					} else {
						this.addNonpreformLang(Eag2012.OPTION_NONE);
					}

					// Value and dates.
					for (int j = 0; j < nonpreform.getContent().size(); j++) {
						Object object = nonpreform.getContent().get(j);

						// Value.
						if (object != null && object instanceof String) {
							if (!((String) object).startsWith("\n")) {
								this.addNonpreform((String) object);
							}
						} else if (object != null && object instanceof UseDates) {
							// Dates.
							UseDates useDates = (UseDates) object;
							if (useDates != null) {
								if (useDates.getDate() != null && useDates.getDate().getContent() != null
										&& !useDates.getDate().getContent().isEmpty()) {
									List<String> dateList = new ArrayList<String>();
									dateList.add(useDates.getDate().getContent());
									this.addNonpreformDate(dateList);
								}
								if (useDates.getDateRange() != null
										&& ((useDates.getDateRange().getFromDate() != null
											&& useDates.getDateRange().getFromDate().getContent() != null
											&& !useDates.getDateRange().getFromDate().getContent().isEmpty())
										|| (useDates.getDateRange().getToDate() != null
											&& useDates.getDateRange().getToDate().getContent() != null
											&& !useDates.getDateRange().getToDate().getContent().isEmpty()))) {
									List<String> dateFromList = new ArrayList<String>();
									if (useDates.getDateRange().getFromDate() != null
											&& useDates.getDateRange().getFromDate() != null
											&& useDates.getDateRange().getFromDate().getContent() != null
											&& !useDates.getDateRange().getFromDate().getContent().isEmpty()) {
										dateFromList.add(useDates.getDateRange().getFromDate().getContent());
									} else {
										dateFromList.add("");
									}
									List<String> dateToList = new ArrayList<String>();
									if (useDates.getDateRange().getToDate() != null
											&& useDates.getDateRange().getToDate() != null
											&& useDates.getDateRange().getToDate().getContent() != null
											&& !useDates.getDateRange().getToDate().getContent().isEmpty()) {
										dateToList.add(useDates.getDateRange().getToDate().getContent());
									} else {
										dateToList.add("");
									}

									this.addNonpreformDateFrom(dateFromList);
									this.addNonpreformDateTo(dateToList);
								}
								if (useDates.getDateSet() != null && !useDates.getDateSet().getDateOrDateRange().isEmpty()) {
									List<String> dateList = new ArrayList<String>();
									List<String> dateFromList = new ArrayList<String>();
									List<String> dateToList = new ArrayList<String>();
									for (int k = 0; k < useDates.getDateSet().getDateOrDateRange().size(); k ++) {
										Object dateObject = useDates.getDateSet().getDateOrDateRange().get(k);
										if (dateObject instanceof Date) {
											Date date = (Date) dateObject;
											if (date != null && date.getContent() != null
												&& !date.getContent().isEmpty()) {
												dateList.add(date.getContent());
											}
										}
										if (dateObject instanceof DateRange) {
											DateRange dateRange = (DateRange) dateObject;
											if (dateRange != null
													&& ((dateRange.getFromDate() != null
														&& dateRange.getFromDate().getContent() != null
														&& !dateRange.getFromDate().getContent().isEmpty())
													|| (dateRange.getToDate() != null
														&& dateRange.getToDate().getContent() != null
														&& !dateRange.getToDate().getContent().isEmpty()))) {
												if (dateRange.getFromDate() != null
														&& dateRange.getFromDate() != null
														&& dateRange.getFromDate().getContent() != null
														&& !dateRange.getFromDate().getContent().isEmpty()) {
													dateFromList.add(dateRange.getFromDate().getContent());
												} else {
													dateFromList.add("");
												}
												if (dateRange.getToDate() != null
														&& dateRange.getToDate() != null
														&& dateRange.getToDate().getContent() != null
														&& !dateRange.getToDate().getContent().isEmpty()) {
													dateToList.add(dateRange.getToDate().getContent());
												} else {
													dateToList.add("");
												}
											}
										}
									}
									this.addNonpreformDate(dateList);
									this.addNonpreformDateFrom(dateFromList);
									this.addNonpreformDateTo(dateToList);
								}
							}
						}
					}
				} 
				// Check if list of "Date" and "DateRange" has the same size.
				if (this.getNonpreformDate().size() > this.getNonpreformDateFrom().size()) {
					this.getNonpreformDateFrom().add(new ArrayList<String>());
					this.getNonpreformDateTo().add(new ArrayList<String>());
				} else if (this.getNonpreformDate().size() < this.getNonpreformDateFrom().size()) {
					this.getNonpreformDate().add(new ArrayList<String>());
				}
			}
		}

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

				this.addRepositoryType(value);
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
							if (location.getStreet() != null) {
								if (location.getStreet().getContent() != null
										&& !location.getStreet().getContent().isEmpty()) {
									this.setStreetPostal(location.getStreet().getContent());
								} else {
									this.setStreetPostal("");
								}
								if (location.getStreet().getLang() != null
										&& !location.getStreet().getLang().isEmpty()) {
									this.setStreetPostalLang(location.getStreet().getLang());
								} else {
									this.setStreetPostalLang(Eag2012.OPTION_NONE);
								}
							}
							// Postal city.
							if (location.getMunicipalityPostalcode() != null) {
								if (location.getMunicipalityPostalcode().getContent() != null
										&& !location.getMunicipalityPostalcode().getContent().isEmpty()) {
									this.setMunicipalityPostalcodePostal(location.getMunicipalityPostalcode().getContent());
								} else {
									this.setMunicipalityPostalcodePostal("");
								}
								if (location.getMunicipalityPostalcode().getLang() != null
										&& !location.getMunicipalityPostalcode().getLang().isEmpty()) {
									this.setMunicipalityPostalcodePostalLang(location.getMunicipalityPostalcode().getLang());
								} else if (location.getStreet() != null
										&& location.getStreet().getLang() != null
										&& !location.getStreet().getLang().isEmpty()) {
									this.setMunicipalityPostalcodePostalLang(location.getStreet().getLang());
								} else {
									this.setMunicipalityPostalcodePostalLang(Eag2012.OPTION_NONE);
								}
							}
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
				if(repository.getServices()!=null && repository.getServices().getSearchroom()!=null){
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
					if(searchRoom.getComputerPlaces()!=null && searchRoom.getComputerPlaces().getNum()!=null){
						this.setSearchRoomComputerPlaces(searchRoom.getComputerPlaces().getNum().getContent());
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
				if(repository.getServices()!=null){
					Library library = repository.getServices().getLibrary();
					if(library!=null){
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
					}
				}
				List<P> ps = null;
				if(repository.getServices()!=null){
					
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
					if(repository.getServices().getTechservices()!=null){
						this.setTechnicalServicesQuestion((repository.getServices().getTechservices().getRestorationlab()!=null)?repository.getServices().getTechservices().getRestorationlab().getQuestion():Eag2012.OPTION_NONE);
						if(repository.getServices().getTechservices().getRestorationlab()!=null && repository.getServices().getTechservices().getRestorationlab().getDescriptiveNote()!=null){
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
							if(repository.getServices().getTechservices().getReproductionser().getQuestion()!=null && !repository.getServices().getTechservices().getReproductionser().getQuestion().equalsIgnoreCase(Eag2012.OPTION_NONE)){
								this.setReproductionserQuestion(repository.getServices().getTechservices().getReproductionser().getQuestion());
							}
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
								if(repository.getServices().getTechservices().getReproductionser().getMicroformser()!=null && repository.getServices().getTechservices().getReproductionser().getMicroformser().getQuestion()!=null && !repository.getServices().getTechservices().getReproductionser().getMicroformser().getQuestion().equalsIgnoreCase(Eag2012.OPTION_NONE)){
									this.setMicrofilmServices(repository.getServices().getTechservices().getReproductionser().getMicroformser().getQuestion());
								}
								if(repository.getServices().getTechservices().getReproductionser().getPhotographser()!=null && repository.getServices().getTechservices().getReproductionser().getPhotographser().getQuestion()!=null && !repository.getServices().getTechservices().getReproductionser().getPhotographser().getQuestion().equalsIgnoreCase(Eag2012.OPTION_NONE)){
									this.setPhotographicServices(repository.getServices().getTechservices().getReproductionser().getPhotographser().getQuestion());
								}
								if(repository.getServices().getTechservices().getReproductionser().getDigitalser()!=null && repository.getServices().getTechservices().getReproductionser().getDigitalser().getQuestion()!=null && !repository.getServices().getTechservices().getReproductionser().getDigitalser().getQuestion().equalsIgnoreCase(Eag2012.OPTION_NONE)){
									this.setDigitisationServices(repository.getServices().getTechservices().getReproductionser().getDigitalser().getQuestion());
								}
								if(repository.getServices().getTechservices().getReproductionser().getPhotocopyser()!=null && repository.getServices().getTechservices().getReproductionser().getPhotocopyser().getQuestion()!=null && !repository.getServices().getTechservices().getReproductionser().getPhotocopyser().getQuestion().equalsIgnoreCase(Eag2012.OPTION_NONE)){
									this.setPhotocopyingServices(repository.getServices().getTechservices().getReproductionser().getPhotocopyser().getQuestion());
								}
							}
						}
					}
					// Recreational services
					RecreationalServices recreationalServices = repository.getServices().getRecreationalServices();
					if(recreationalServices!=null){
						if(recreationalServices.getRefreshment()!=null && recreationalServices.getRefreshment().getDescriptiveNote()!=null && recreationalServices.getRefreshment().getDescriptiveNote().getP()!=null){
							for (int i = 0; i < recreationalServices.getRefreshment().getDescriptiveNote().getP().size(); i++) {
								this.setRecreationalServicesRefreshmentArea(recreationalServices.getRefreshment().getDescriptiveNote().getP().get(i).getContent());
								this.setRecreationalServicesRefreshmentAreaLang(recreationalServices.getRefreshment().getDescriptiveNote().getP().get(i).getLang());
							}
						}
						if(recreationalServices.getExhibition()!=null){
							for (int i = 0; i < recreationalServices.getExhibition().size(); i++) {
								if(recreationalServices.getExhibition().get(i).getDescriptiveNote()!=null && recreationalServices.getExhibition().get(i).getDescriptiveNote().getP()!=null){
									for (int j = 0; j < recreationalServices.getExhibition().get(i).getDescriptiveNote().getP().size(); j++) {
										this.setRecreationalServicesExhibition(recreationalServices.getExhibition().get(i).getDescriptiveNote().getP().get(j).getContent());
										this.setRecreationalServicesExhibitionLang(recreationalServices.getExhibition().get(i).getDescriptiveNote().getP().get(j).getLang());
									}
								}
								if(recreationalServices.getExhibition().get(i).getWebpage()!=null){
									this.setRecreationalServicesWeb(recreationalServices.getExhibition().get(i).getWebpage().getHref());
									this.setRecreationalServicesWebLink(recreationalServices.getExhibition().get(i).getWebpage().getContent());
								}
							}
						}
						if(recreationalServices.getToursSessions()!=null){
							for (int i = 0; i < recreationalServices.getToursSessions().size(); i++) {
								if(recreationalServices.getToursSessions().get(i).getDescriptiveNote()!=null && recreationalServices.getToursSessions().get(i).getDescriptiveNote().getP()!=null){
									for (int j = 0; j < recreationalServices.getToursSessions().get(i).getDescriptiveNote().getP().size(); j++) {
										this.setToursSessionGuidesAndSessionsContent(recreationalServices.getToursSessions().get(i).getDescriptiveNote().getP().get(j).getContent());
										this.setToursSessionGuidesAndSessionsLang(recreationalServices.getToursSessions().get(i).getDescriptiveNote().getP().get(j).getLang());
									}
								}
								if(recreationalServices.getToursSessions().get(i).getWebpage()!=null){
									this.setToursSessionGuidesAndSessionsWebpage(recreationalServices.getToursSessions().get(i).getWebpage().getHref());
									this.setToursSessionGuidesAndSessionsWebpageTitle(recreationalServices.getToursSessions().get(i).getWebpage().getContent());
								}
							}
						}
						if(recreationalServices.getOtherServices()!=null){
							for (int i = 0; i < recreationalServices.getOtherServices().size(); i++) {
								if(recreationalServices.getToursSessions().get(i).getDescriptiveNote()!=null && recreationalServices.getToursSessions().get(i).getDescriptiveNote().getP()!=null){
									for (int j = 0; j < recreationalServices.getToursSessions().get(i).getDescriptiveNote().getP().size(); j++) {
										this.setOtherServices(recreationalServices.getOtherServices().get(i).getDescriptiveNote().getP().get(j).getContent());
										this.setOtherServicesLang(recreationalServices.getOtherServices().get(i).getDescriptiveNote().getP().get(j).getLang());
									}
								}
								if(recreationalServices.getOtherServices().get(i).getWebpage()!=null){
									this.setOtherServicesWebpage(recreationalServices.getOtherServices().get(i).getWebpage().getHref());
									this.setOtherServicesLink(recreationalServices.getOtherServices().get(i).getWebpage().getContent());
								}
							}
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
						List<String> abbreviations = this.getAbbreviation();
						if(abbreviations==null){
							abbreviations = new ArrayList<String>();
						}
						abbreviations.add(this.eag.getControl().getConventionDeclaration().get(i).getAbbreviation().getContent());
						this.setAbbreviation(abbreviations);
					}
					if (!this.eag.getControl().getConventionDeclaration().get(i).getCitation().isEmpty()) {
						// TODO: Review for multiple values.
						for (int j = 0; j < this.eag.getControl().getConventionDeclaration().get(i).getCitation().size(); j++) {
							List<String> citations = this.getCitation();
							if(citations==null){
								citations = new ArrayList<String>();
							}
							citations.add(this.eag.getControl().getConventionDeclaration().get(i).getCitation().get(j).getContent());
							this.setCitation(citations);
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
		if(this.eag.getRelations()!=null){
			// Resource relations.
			if (this.eag.getRelations().getResourceRelation()!=null && !this.eag.getRelations().getResourceRelation().isEmpty()) {
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
			if (this.eag.getRelations().getEagRelation()!=null && !this.eag.getRelations().getEagRelation().isEmpty()) {
				// TODO: Review for multiple values.
				for (int i = 0; i < this.eag.getRelations().getEagRelation().size(); i++) {
					// Website of the description of the institution.
					this.setEagRelationHref(this.eag.getRelations().getEagRelation().get(i).getHref());
//					this.setEagRelationLang(this.eag.getRelations().getEagRelation().get(i).getLang());
					
					// Title & ID of the related institution.
					if (!this.eag.getRelations().getEagRelation().get(i).getRelationEntry().isEmpty()) {
						// TODO: Review for multiple values.
						for (int j = 0; j < this.eag.getRelations().getEagRelation().size(); j++) {
//							this.setEagRelationrelationEntry(this.eag.getRelations().getEagRelation().get(i).getRelationEntry().get(j).getContent());
						}
					}

					// Type of your relation.
					if (this.eag.getRelations().getEagRelation().get(i).getEagRelationType() != null) {
						String eagRelationType = this.eag.getRelations().getEagRelation().get(i).getEagRelationType();
						
						if (Eag2012.OPTION_CHILD_TEXT.equalsIgnoreCase(eagRelationType)) {
							eagRelationType = Eag2012.OPTION_CHILD;
						}
						if (Eag2012.OPTION_PARENT_TEXT.equalsIgnoreCase(eagRelationType)) {
							eagRelationType = Eag2012.OPTION_PARENT;
						}
						if (Eag2012.OPTION_EARLIER_TEXT.equalsIgnoreCase(eagRelationType)) {
							eagRelationType = Eag2012.OPTION_EARLIER;
						}
						if (Eag2012.OPTION_LATER_TEXT.equalsIgnoreCase(eagRelationType)) {
							eagRelationType = Eag2012.OPTION_LATER;
						}
						if (Eag2012.OPTION_ASSOCIATIVE_TEXT.equalsIgnoreCase(eagRelationType)) {
							eagRelationType = Eag2012.OPTION_ASSOCIATIVE;
						}

						this.setEagRelationType(eagRelationType);
					}

					if(this.eag.getRelations().getEagRelation().get(i).getRelationEntry()!=null){
						for(int x=0;x<this.eag.getRelations().getEagRelation().get(i).getRelationEntry().size();x++){
							this.setEagRelationrelationEntry(this.eag.getRelations().getEagRelation().get(i).getRelationEntry().get(x).getContent());
						}
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
	}
	
	public String toString(){
		return "Loader with record id: "+this.recordId;
	}
}
