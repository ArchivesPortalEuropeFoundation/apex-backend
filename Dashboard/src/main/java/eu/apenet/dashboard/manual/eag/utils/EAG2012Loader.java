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
import eu.apenet.dpt.utils.eag2012.Library;
import eu.apenet.dpt.utils.eag2012.Location;
import eu.apenet.dpt.utils.eag2012.Nonpreform;
import eu.apenet.dpt.utils.eag2012.OtherRecordId;
import eu.apenet.dpt.utils.eag2012.Parform;
import eu.apenet.dpt.utils.eag2012.Repository;
import eu.apenet.dpt.utils.eag2012.ResourceRelation;
import eu.apenet.dpt.utils.eag2012.Searchroom;
import eu.apenet.dpt.utils.eag2012.Timetable;
import eu.apenet.dpt.utils.eag2012.UseDates;
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
	private String initialAutform;
	private String initialAutformEscaped;
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
	private List<String> yiNumberOfVisitorsAddress;
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
	private List<String> yiNumberOfPostalAddress;
	private List<String> yiMunicipalityPostalcodePostal;
	private List<String> yiMunicipalityPostalcodePostalLang;
	private List<String> yiStreetPostal;
	private List<String> yiStreetPostalLang;
	private String geogarea;
	private String telephone;
	private String email;
	private String emailTitle;
	private String emailLang;
	private List<String> yiNumberOfEmailAddress;
	private List<String> yiEmail;
	private List<String> yiEmailTitle;
	private List<String> yiEmailLang;
	private String webpage;
	private String webpageTitle;
	private String webpageLang;
	private List<String> yiNumberOfWebpageAddress;
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
	private List<String> yiAccessibilityQuestion;
	private List<String> yiAccessibility;
	private List<String> yiAccessibilityLang;
	private List<String> yiResourceRelationHref;
	private List<String> yiResourceRelationrelationEntry;

	// Repo tabs.
	private int numberOfRepositories;

	// Your institution tab.
	private String recordIdISIL;
	private List<String> yiResourceRelationLang;

	// Identity tab.
	private List<List<String>> repositoryName;
	private List<List<String>> repositoryRole;
	private List<String> nonpreform;
	private List<String> nonpreformLang;
	private List<List<String>> nonpreformDate;
	private List<List<String>> nonpreformDateFrom;
	private List<List<String>> nonpreformDateTo;
	private List<String> repositoryType;

	// Contact.
	private List<List<String>> contactNumberOfVisitorsAddress;
	private List<List<String>> contactLatitude;
	private List<List<String>> contactLongitude;
	private List<List<String>> contactCountry;
	private List<List<String>> contactCountryLang;
	private List<List<String>> contactFirstdem;
	private List<List<String>> contactFirstdemLang;
	private List<List<String>> contactSecondem;
	private List<List<String>> contactSecondemLang;
	private List<List<String>> contactMunicipality;
	private List<List<String>> contactMunicipalityLang;
	private List<List<String>> contactLocalentity;
	private List<List<String>> contactLocalentityLang;
	private List<List<String>> contactStreet;
	private List<List<String>> contactStreetLang;
	private List<List<String>> contactNumberOfPostalAddress;
	private List<List<String>> contactPostalCountry;
	private List<List<String>> contactPostalCountryLang;
	private List<List<String>> contactPostalMunicipality;
	private List<List<String>> contactPostalMunicipalityLang;
	private List<List<String>> contactPostalStreet;
	private List<List<String>> contactPostalStreetLang;
	private List<List<String>> contactContinent;
	private List<List<String>> contactTelephone;
	private List<List<String>> contactFax;
	private List<List<String>> contactNumberOfEmailAddress;
	private List<List<String>> contactEmailHref;
	private List<List<String>> contactEmailTitle;
	private List<List<String>> contactEmailLang;
	private List<List<String>> contactNumberOfWebpageAddress;
	private List<List<String>> contactWebpageHref;
	private List<List<String>> contactWebpageTitle;
	private List<List<String>> contactWebpageLang;

	// Access and Services, general.
	private List<List<String>> asOpening;
	private List<List<String>> asOpeningLang;
	private List<List<String>> asClosing;
	private List<List<String>> asClosingLang;
	private List<List<String>> asNumberOfDirections;
	private List<List<String>> asDirections;
	private List<List<String>> asDirectionsLang;
	private List<List<String>> asDirectionsCitationHref;
	private List<List<String>> asAccessQuestion;
	private List<List<String>> asRestaccess;
	private List<List<String>> asRestaccessLang;
	private List<List<String>> asNumberOfTermsOfUse;
	private List<List<String>> asTermsOfUse;
	private List<List<String>> asTermsOfUseLang;
	private List<List<String>> asTermsOfUseHref;
	private List<List<String>> asAccessibilityQuestion;
	private List<List<String>> asAccessibility;
	private List<List<String>> asAccessibilityLang;
	// Access and Services, searchroom.
	private List<List<String>> asSearchRoomTelephone;
	private List<List<String>> asSearchRoomNumberOfEmail;
	private List<List<String>> asSearchRoomEmailHref;
	private List<List<String>> asSearchRoomEmailTitle;
	private List<List<String>> asSearchRoomEmailLang;
	private List<List<String>> asSearchRoomNumberOfWebpage;
	private List<List<String>> asSearchRoomWebpageHref;
	private List<List<String>> asSearchRoomWebpageTitle;
	private List<List<String>> asSearchRoomWebpageLang;
	private List<List<String>> asSearchRoomWorkPlaces;
	private List<List<String>> asSearchRoomComputerPlaces;
	private List<List<String>> asSearchRoomComputerPlacesDescription;
	private List<List<String>> asSearchRoomComputerPlacesDescriptionLang;
	private List<List<String>> asSearchRoomMicrofilmReaders;
	private List<List<String>> asSearchRoomPhotographAllowance;
	private List<List<String>> asSearchRoomNumberOfReadersTicket;
	private List<List<String>> asSearchRoomReadersTicketHref;
	private List<List<String>> asSearchRoomReadersTicketContent;
	private List<List<String>> asSearchRoomReadersTicketLang;
	private List<List<String>> asSearchRoomNumberOfAdvancedOrders;
	private List<List<String>> asSearchRoomAdvancedOrdersHref;
	private List<List<String>> asSearchRoomAdvancedOrdersContent;
	private List<List<String>> asSearchRoomAdvancedOrdersLang;
	private List<List<String>> asSearchRoomResearchServicesContent;
	private List<List<String>> asSearchRoomResearchServicesLang;
	// Access and Services, library.
	private List<List<String>> asLibraryQuestion;
	private List<List<String>> asLibraryTelephone;
	private List<List<String>> asLibraryNumberOfEmail;
	private List<List<String>> asLibraryEmailHref;
	private List<List<String>> asLibraryEmailTitle;
	private List<List<String>> asLibraryEmailLang;
	private List<List<String>> asLibraryNumberOfWebpage;
	private List<List<String>> asLibraryWebpageHref;
	private List<List<String>> asLibraryWebpageTitle;
	private List<List<String>> asLibraryWebpageLang;
	private List<List<String>> asLibraryMonographPublication;
	private List<List<String>> asLibrarySerialPublication;
	// Access and Services, internet access.
	private List<List<String>> asInternetAccessQuestion;
	private List<List<String>> asInternetAccessDescription;
	private List<List<String>> asInternetAccessDescriptionLang;
	// Access and Services, technical services, restauration laboratory.
	private List<List<String>> asRestorationlabQuestion;
	private List<List<String>> asRestorationlabDescription;
	private List<List<String>> asRestorationlabDescriptionLang;
	private List<List<String>> asRestorationlabTelephone;
	private List<List<String>> asRestorationlabNumberOfEmail;
	private List<List<String>> asRestorationlabEmailHref;
	private List<List<String>> asRestorationlabEmailTitle;
	private List<List<String>> asRestorationlabEmailLang;
	private List<List<String>> asRestorationlabNumberOfWebpage;
	private List<List<String>> asRestorationlabWebpageHref;
	private List<List<String>> asRestorationlabWebpageTitle;
	private List<List<String>> asRestorationlabWebpageLang;
	// Access and Services, technical services, reproduction service.
	private List<List<String>> asReproductionserQuestion;
	private List<List<String>> asReproductionserDescription;
	private List<List<String>> asReproductionserDescriptionLang;
	private List<List<String>> asReproductionserTelephone;
	private List<List<String>> asReproductionserNumberOfEmail;
	private List<List<String>> asReproductionserEmailHref;
	private List<List<String>> asReproductionserEmailTitle;
	private List<List<String>> asReproductionserEmailLang;
	private List<List<String>> asReproductionserNumberOfWebpage;
	private List<List<String>> asReproductionserWebpageHref;
	private List<List<String>> asReproductionserWebpageTitle;
	private List<List<String>> asReproductionserWebpageLang;
	private List<List<String>> asReproductionserMicrofilmServices;
	private List<List<String>> asReproductionserPhotographicServices;
	private List<List<String>> asReproductionserDigitisationServices;
	private List<List<String>> asReproductionserPhotocopyingServices;
	// Access and Services, recreational services.
	private List<List<String>> asRecreationalServicesRefreshmentArea;
	private List<List<String>> asRecreationalServicesRefreshmentAreaLang;
	private List<List<String>> asRSNumberOfExhibition;
	private List<List<String>> asRSExhibition;
	private List<List<String>> asRSExhibitionLang;
	private List<List<String>> asRSExhibitionWebpageHref;
	private List<List<String>> asRSExhibitionWebpageTitle;
	private List<List<String>> asRSExhibitionWebpageLang;
	private List<List<String>> asRSNumberOfToursSessions;
	private List<List<String>> asRSToursSessions;
	private List<List<String>> asRSToursSessionsLang;
	private List<List<String>> asRSToursSessionsWebpageHref;
	private List<List<String>> asRSToursSessionsWebpageTitle;
	private List<List<String>> asRSToursSessionsWebpageLang;
	private List<List<String>> asRSNumberOfOtherServices;
	private List<List<String>> asRSOtherServices;
	private List<List<String>> asRSOtherServicesLang;
	private List<List<String>> asRSOtherServicesWebpageHref;
	private List<List<String>> asRSOtherServicesWebpageTitle;
	private List<List<String>> asRSOtherServicesWebpageLang;

	// Description, repository description.
	private List<List<String>> descRepositorhist;
	private List<List<String>> descRepositorhistLang;
	private List<List<String>> descRepositorFoundDate;
	private List<List<String>> descRepositorFoundRule;
	private List<List<String>> descRepositorFoundRuleLang;
	private List<List<String>> descRepositorSupDate;
	private List<List<String>> descRepositorSupRule;
	private List<List<String>> descRepositorSupRuleLang;
	// Description, administrative structure.
	private List<List<String>> descAdminunit;
	private List<List<String>> descAdminunitLang;
	// Description, building description.
	private List<List<String>> descBuilding;
	private List<List<String>> descBuildingLang;
	private List<List<String>> descRepositorarea;
	private List<List<String>> descRepositorareaUnit;
	private List<List<String>> descLengthshelf;
	private List<List<String>> descLengthshelfUnit;
	// Description, holding description.
	private List<List<String>> descHoldings;
	private List<List<String>> descHoldingsLang;
	private List<List<String>> descHoldingsDate;
	private List<List<String>> descNumberOfHoldingsDateRange;
	private List<List<String>> descHoldingsDateRangeFromDate;
	private List<List<String>> descHoldingsDateRangeToDate;
	private List<List<String>> descExtent;
	private List<List<String>> descExtentUnit;

	// Control.
	private String controlAgentLang;
	private String controlAgencyCode;
	private List<String> controlNumberOfLanguages;
	private List<String> controlLanguageDeclaration;
	private List<String> controlScript;
	private List<String> controlNumberOfRules;
	private List<String> controlAbbreviation;
	private List<String> controlCitation;

	// Relations.
	private List<String> relationsResourceRelationType;
	private List<String> relationsResourceRelationHref;
	private List<String> relationsResourceRelationEntry;
	private List<String> relationsResourceRelationEntryLang;
	private List<String> relationsResourceRelationEntryDescription;
	private List<String> relationsResourceRelationEntryDescriptionLang;
	private List<String> relationsNumberOfEagRelations;
	private List<String> relationsEagRelationType;
	private List<String> relationsEagRelationHref;
	private List<String> relationsEagRelationEntry;
	private List<String> relationsEagRelationEntryLang;
	private List<String> relationsEagRelationEntryDescription;
	private List<String> relationsEagRelationEntryDescriptionLang;

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

	/**
	 * @return the initialAutform
	 */
	public String getInitialAutform() {
		if (this.initialAutform == null
				|| this.initialAutform.isEmpty()) {
			this.initialAutform = DAOFactory.instance().getArchivalInstitutionDAO().getArchivalInstitution(getId()).getAiname();
		}
		this.initialAutform = removeDuplicateWhiteSpaces(this.initialAutform);
		return this.initialAutform;
	}

	/**
	 * @param initialAutform the initialAutform to set
	 */
	public void setInitialAutform(String initialAutform) {
		this.initialAutform = initialAutform;
	}

	/**
	 * @return the initialAutformEscaped
	 */
	public String getInitialAutformEscaped() {
		if (this.initialAutformEscaped == null
				|| this.initialAutformEscaped.isEmpty()) {
			this.initialAutformEscaped = DAOFactory.instance().getArchivalInstitutionDAO().getArchivalInstitution(getId()).getAiname();
		}
		this.initialAutformEscaped = removeDuplicateWhiteSpaces(this.initialAutformEscaped);
		if(this.initialAutformEscaped.contains("\'")){
		   this.initialAutformEscaped = this.initialAutformEscaped.replaceAll("\'", "%27");
		}
		if(this.initialAutformEscaped.contains("\"")){
		   this.initialAutformEscaped = this.initialAutformEscaped.replaceAll("\"", "%22");
		}
		return this.initialAutformEscaped;
	}

	/**
	 * @param initialAutformEscaped the initialAutformEscaped to set
	 */
	public void setInitialAutformEscaped(String initialAutformEscaped) {
		this.initialAutformEscaped = removeDuplicateWhiteSpaces(initialAutformEscaped);

		if(this.initialAutformEscaped.contains("\'")){
		   this.initialAutformEscaped = this.initialAutformEscaped.replaceAll("\'", "%27");
		}

		if(this.initialAutformEscaped.contains("\"")){
		   this.initialAutformEscaped = this.initialAutformEscaped.replaceAll("\"", "%22");
		}
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
		return this.yiClosingLang;
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
	 * @return the yiAccessibilityQuestion
	 */
	public List<String> getYiAccessibilityQuestion() {
		if (this.yiAccessibilityQuestion == null) {
			this.yiAccessibilityQuestion = new ArrayList<String>();
		}
		return this.yiAccessibilityQuestion;
	}

	/**
	 * @param yiAccessibilityQuestion the yiAccessibilityQuestion to set
	 */
	public void setYiAccessibilityQuestion(List<String> yiAccessibilityQuestion) {
		this.yiAccessibilityQuestion = yiAccessibilityQuestion;
	}

	/**
	 * @param yiAccessibilityQuestion the yiAccessibilityQuestion to add
	 */
	public void addYiAccessibilityQuestion(String yiAccessibilityQuestion) {
		this.getYiAccessibilityQuestion().add(yiAccessibilityQuestion);
	}

	/**
	 * @return the yiAccessibility
	 */
	public List<String> getYiAccessibility() {
		if (this.yiAccessibility == null) {
			this.yiAccessibility = new ArrayList<String>();
		}
		return this.yiAccessibility;
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

	/**
	 * @return the yiNumberOfVisitorsAddress
	 */
	public List<String> getYiNumberOfVisitorsAddress() {
		if (this.yiNumberOfVisitorsAddress == null) {
			this.yiNumberOfVisitorsAddress = new ArrayList<String>();
		}
		return this.yiNumberOfVisitorsAddress;
	}

	/**
	 * @param yiNumberOfVisitorsAddress the yiNumberOfVisitorsAddress to set
	 */
	public void setYiNumberOfVisitorsAddress(List<String> yiNumberOfVisitorsAddress) {
		this.yiNumberOfVisitorsAddress = yiNumberOfVisitorsAddress;
	}

	/**
	 * @param yiNumberOfVisitorsAddress the yiNumberOfVisitorsAddress to add
	 */
	public void addYiNumberOfVisitorsAddress(String yiNumberOfVisitorsAddress) {
		this.getYiNumberOfVisitorsAddress().add(yiNumberOfVisitorsAddress);
	}

	/**
	 * @return the yiNumberOfPostalAddress
	 */
	public List<String> getYiNumberOfPostalAddress() {
		if (this.yiNumberOfPostalAddress == null) {
			this.yiNumberOfPostalAddress = new ArrayList<String>();
		}
		return this.yiNumberOfPostalAddress;
	}

	/**
	 * @param yiNumberOfPostalAddress the yiNumberOfPostalAddress to set
	 */
	public void setYiNumberOfPostalAddress(List<String> yiNumberOfPostalAddress) {
		this.yiNumberOfPostalAddress = yiNumberOfPostalAddress;
	}

	/**
	 * @param yiNumberOfPostalAddress the yiNumberOfPostalAddress to add
	 */
	public void addYiNumberOfPostalAddress(String yiNumberOfPostalAddress) {
		this.getYiNumberOfPostalAddress().add(yiNumberOfPostalAddress);
	}

	/**
	 * @return the yiNumberOfEmailAddress
	 */
	public List<String> getYiNumberOfEmailAddress() {
		if (this.yiNumberOfEmailAddress == null) {
			this.yiNumberOfEmailAddress = new ArrayList<String>();
		}
		return this.yiNumberOfEmailAddress;
	}

	/**
	 * @param yiNumberOfEmailAddress the yiNumberOfEmailAddress to set
	 */
	public void setYiNumberOfEmailAddress(List<String> yiNumberOfEmailAddress) {
		this.yiNumberOfEmailAddress = yiNumberOfEmailAddress;
	}

	/**
	 * @param yiNumberOfEmailAddress the yiNumberOfEmailAddress to add
	 */
	public void addYiNumberOfEmailAddress(String yiNumberOfEmailAddress) {
		this.getYiNumberOfEmailAddress().add(yiNumberOfEmailAddress);
	}

	/**
	 * @return the yiNumberOfWebpageAddress
	 */
	public List<String> getYiNumberOfWebpageAddress() {
		if (this.yiNumberOfWebpageAddress == null) {
			this.yiNumberOfWebpageAddress = new ArrayList<String>();
		}
		return this.yiNumberOfWebpageAddress;
	}

	/**
	 * @param yiNumberOfWebpageAddress the yiNumberOfWebpageAddress to set
	 */
	public void setYiNumberOfWebpageAddress(List<String> yiNumberOfWebpageAddress) {
		this.yiNumberOfWebpageAddress = yiNumberOfWebpageAddress;
	}

	/**
	 * @param yiNumberOfWebpageAddress the yiNumberOfWebpageAddress to add
	 */
	public void addYiNumberOfWebpageAddress(String yiNumberOfWebpageAddress) {
		this.getYiNumberOfWebpageAddress().add(yiNumberOfWebpageAddress);
	}

	/**
	 * @return the numberOfRepositories
	 */
	public int getNumberOfRepositories() {
		return this.numberOfRepositories;
	}

	/**
	 * @param numberOfRepositories the numberOfRepositories to set
	 */
	public void setNumberOfRepositories(int numberOfRepositories) {
		this.numberOfRepositories = numberOfRepositories;
	}

	public String getRecordIdISIL() {
		return this.recordIdISIL;
	}

	public void setRecordIdISIL(String recordIdISIL) {
		this.recordIdISIL = recordIdISIL;
	}

	/**
	 * @return the repositoryName
	 */
	public List<List<String>> getRepositoryName() {
		if (this.repositoryName == null) {
			this.repositoryName = new ArrayList<List<String>>();
		}
		return this.repositoryName;
	}

	/**
	 * @param repositoryName the repositoryName to set
	 */
	public void setRepositoryName(List<List<String>> repositoryName) {
		this.repositoryName = repositoryName;
	}

	/**
	 * @param repositoryName the repositoryName to add
	 */
	public void addRepositoryName(List<String> repositoryName) {
		this.getRepositoryName().add(repositoryName);
	}

	/**
	 * @return the repositoryRole
	 */
	public List<List<String>> getRepositoryRole() {
		if (this.repositoryRole == null) {
			this.repositoryRole = new ArrayList<List<String>>();
		}
		return this.repositoryRole;
	}

	/**
	 * @param repositoryRole the repositoryRole to set
	 */
	public void setRepositoryRole(List<List<String>> repositoryRole) {
		this.repositoryRole = repositoryRole;
	}

	/**
	 * @param repositoryRole the repositoryRole to add
	 */
	public void addRepositoryRole(List<String> repositoryRole) {
		this.getRepositoryRole().add(repositoryRole);
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

	/**
	 * @return the contactLatitude
	 */
	public List<List<String>> getContactLatitude() {
        if (this.contactLatitude == null) {
        	this.contactLatitude = new ArrayList<List<String>>();
        }
		return this.contactLatitude;
	}

	/**
	 * @param contactLatitude the contactLatitude to set
	 */
	public void setContactLatitude(List<List<String>> contactLatitude) {
		this.contactLatitude = contactLatitude;
	}

	/**
	 * @param contactLatitude the contactLatitude to add
	 */
	public void addContactLatitude(List<String> contactLatitude) {
		this.getContactLatitude().add(contactLatitude);
	}

	/**
	 * @return the contactLongitude
	 */
	public List<List<String>> getContactLongitude() {
        if (this.contactLongitude == null) {
        	this.contactLongitude = new ArrayList<List<String>>();
        }
		return this.contactLongitude;
	}

	/**
	 * @param contactLongitude the contactLongitude to set
	 */
	public void setContactLongitude(List<List<String>> contactLongitude) {
		this.contactLongitude = contactLongitude;
	}

	/**
	 * @param contactLongitude the contactLongitude to add
	 */
	public void addContactLongitude(List<String> contactLongitude) {
		this.getContactLongitude().add(contactLongitude);
	}

	/**
	 * @return the contactCountry
	 */
	public List<List<String>> getContactCountry() {
        if (this.contactCountry == null) {
        	this.contactCountry = new ArrayList<List<String>>();
        }
		return this.contactCountry;
	}

	/**
	 * @param contactCountry the contactCountry to set
	 */
	public void setContactCountry(List<List<String>> contactCountry) {
		this.contactCountry = contactCountry;
	}

	/**
	 * @param contactCountry the contactCountry to add
	 */
	public void addContactCountry(List<String> contactCountry) {
		this.getContactCountry().add(contactCountry);
	}

	/**
	 * @return the contactCountryLang
	 */
	public List<List<String>> getContactCountryLang() {
        if (this.contactCountryLang == null) {
        	this.contactCountryLang = new ArrayList<List<String>>();
        }
		return this.contactCountryLang;
	}

	/**
	 * @param contactCountryLang the contactCountryLang to set
	 */
	public void setContactCountryLang(List<List<String>> contactCountryLang) {
		this.contactCountryLang = contactCountryLang;
	}

	/**
	 * @param contactCountryLang the contactCountryLang to add
	 */
	public void addContactCountryLang(List<String> contactCountryLang) {
		this.getContactCountryLang().add(contactCountryLang);
	}

	/**
	 * @return the contactFirstdem
	 */
	public List<List<String>> getContactFirstdem() {
        if (this.contactFirstdem == null) {
        	this.contactFirstdem = new ArrayList<List<String>>();
        }
		return this.contactFirstdem;
	}

	/**
	 * @param contactFirstdem the contactFirstdem to set
	 */
	public void setContactFirstdem(List<List<String>> contactFirstdem) {
		this.contactFirstdem = contactFirstdem;
	}

	/**
	 * @param contactFirstdem the contactFirstdem to add
	 */
	public void addContactFirstdem(List<String> contactFirstdem) {
		this.getContactFirstdem().add(contactFirstdem);
	}

	/**
	 * @return the contactFirstdemLang
	 */
	public List<List<String>> getContactFirstdemLang() {
        if (this.contactFirstdemLang == null) {
        	this.contactFirstdemLang = new ArrayList<List<String>>();
        }
		return this.contactFirstdemLang;
	}

	/**
	 * @param contactFirstdemLang the contactFirstdemLang to set
	 */
	public void setContactFirstdemLang(List<List<String>> contactFirstdemLang) {
		this.contactFirstdemLang = contactFirstdemLang;
	}

	/**
	 * @param contactFirstdemLang the contactFirstdemLang to add
	 */
	public void addContactFirstdemLang(List<String> contactFirstdemLang) {
		this.getContactFirstdemLang().add(contactFirstdemLang);
	}

	/**
	 * @return the contactSecondem
	 */
	public List<List<String>> getContactSecondem() {
        if (this.contactSecondem == null) {
        	this.contactSecondem = new ArrayList<List<String>>();
        }
		return this.contactSecondem;
	}

	/**
	 * @param contactSecondem the contactSecondem to set
	 */
	public void setContactSecondem(List<List<String>> contactSecondem) {
		this.contactSecondem = contactSecondem;
	}

	/**
	 * @param contactSecondem the contactSecondem to add
	 */
	public void addContactSecondem(List<String> contactSecondem) {
		this.getContactSecondem().add(contactSecondem);
	}

	/**
	 * @return the contactSecondemLang
	 */
	public List<List<String>> getContactSecondemLang() {
        if (this.contactSecondemLang == null) {
        	this.contactSecondemLang = new ArrayList<List<String>>();
        }
		return this.contactSecondemLang;
	}

	/**
	 * @param contactSecondemLang the contactSecondemLang to set
	 */
	public void setContactSecondemLang(List<List<String>> contactSecondemLang) {
		this.contactSecondemLang = contactSecondemLang;
	}

	/**
	 * @param contactSecondemLang the contactSecondemLang to add
	 */
	public void addContactSecondemLang(List<String> contactSecondemLang) {
		this.getContactSecondemLang().add(contactSecondemLang);
	}

	/**
	 * @return the contactMunicipality
	 */
	public List<List<String>> getContactMunicipality() {
        if (this.contactMunicipality == null) {
        	this.contactMunicipality = new ArrayList<List<String>>();
        }
		return this.contactMunicipality;
	}

	/**
	 * @param contactMunicipality the contactMunicipality to set
	 */
	public void setContactMunicipality(List<List<String>> contactMunicipality) {
		this.contactMunicipality = contactMunicipality;
	}

	/**
	 * @param contactMunicipality the contactMunicipality to add
	 */
	public void addContactMunicipality(List<String> contactMunicipality) {
		this.getContactMunicipality().add(contactMunicipality);
	}

	/**
	 * @return the contactMunicipalityLang
	 */
	public List<List<String>> getContactMunicipalityLang() {
        if (this.contactMunicipalityLang == null) {
        	this.contactMunicipalityLang = new ArrayList<List<String>>();
        }
		return this.contactMunicipalityLang;
	}

	/**
	 * @param contactMunicipalityLang the contactMunicipalityLang to set
	 */
	public void setContactMunicipalityLang(
			List<List<String>> contactMunicipalityLang) {
		this.contactMunicipalityLang = contactMunicipalityLang;
	}

	/**
	 * @param contactMunicipalityLang the contactMunicipalityLang to add
	 */
	public void addContactMunicipalityLang(List<String> contactMunicipalityLang) {
		this.getContactMunicipalityLang().add(contactMunicipalityLang);
	}

	/**
	 * @return the contactLocalentity
	 */
	public List<List<String>> getContactLocalentity() {
        if (this.contactLocalentity == null) {
        	this.contactLocalentity = new ArrayList<List<String>>();
        }
		return this.contactLocalentity;
	}

	/**
	 * @param contactLocalentity the contactLocalentity to set
	 */
	public void setContactLocalentity(List<List<String>> contactLocalentity) {
		this.contactLocalentity = contactLocalentity;
	}

	/**
	 * @param contactLocalentity the contactLocalentity to add
	 */
	public void addContactLocalentity(List<String> contactLocalentity) {
		this.getContactLocalentity().add(contactLocalentity);
	}

	/**
	 * @return the contactLocalentityLang
	 */
	public List<List<String>> getContactLocalentityLang() {
        if (this.contactLocalentityLang == null) {
        	this.contactLocalentityLang = new ArrayList<List<String>>();
        }
		return this.contactLocalentityLang;
	}

	/**
	 * @param contactLocalentityLang the contactLocalentityLang to set
	 */
	public void setContactLocalentityLang(List<List<String>> contactLocalentityLang) {
		this.contactLocalentityLang = contactLocalentityLang;
	}

	/**
	 * @param contactLocalentityLang the contactLocalentityLang to add
	 */
	public void addContactLocalentityLang(List<String> contactLocalentityLang) {
		this.getContactLocalentityLang().add(contactLocalentityLang);
	}

	/**
	 * @return the contactStreet
	 */
	public List<List<String>> getContactStreet() {
        if (this.contactStreet == null) {
        	this.contactStreet = new ArrayList<List<String>>();
        }
		return this.contactStreet;
	}

	/**
	 * @param contactStreet the contactStreet to set
	 */
	public void setContactStreet(List<List<String>> contactStreet) {
		this.contactStreet = contactStreet;
	}

	/**
	 * @param contactStreet the contactStreet to add
	 */
	public void addContactStreet(List<String> contactStreet) {
		this.getContactStreet().add(contactStreet);
	}

	/**
	 * @return the contactStreetLang
	 */
	public List<List<String>> getContactStreetLang() {
        if (this.contactStreetLang == null) {
        	this.contactStreetLang = new ArrayList<List<String>>();
        }
		return this.contactStreetLang;
	}

	/**
	 * @param contactStreetLang the contactStreetLang to set
	 */
	public void setContactStreetLang(List<List<String>> contactStreetLang) {
		this.contactStreetLang = contactStreetLang;
	}

	/**
	 * @param contactStreetLang the contactStreetLang to add
	 */
	public void addContactStreetLang(List<String> contactStreetLang) {
		this.getContactStreetLang().add(contactStreetLang);
	}

	/**
	 * @return the contactPostalCountry
	 */
	public List<List<String>> getContactPostalCountry() {
        if (this.contactPostalCountry == null) {
        	this.contactPostalCountry = new ArrayList<List<String>>();
        }
		return this.contactPostalCountry;
	}

	/**
	 * @param contactPostalCountry the contactPostalCountry to set
	 */
	public void setContactPostalCountry(List<List<String>> contactPostalCountry) {
		this.contactPostalCountry = contactPostalCountry;
	}

	/**
	 * @param contactPostalCountry the contactPostalCountry to add
	 */
	public void addContactPostalCountry(List<String> contactPostalCountry) {
		this.getContactPostalCountry().add(contactPostalCountry);
	}

	/**
	 * @return the contactPostalCountryLang
	 */
	public List<List<String>> getContactPostalCountryLang() {
        if (this.contactPostalCountryLang == null) {
        	this.contactPostalCountryLang = new ArrayList<List<String>>();
        }
		return this.contactPostalCountryLang;
	}

	/**
	 * @param contactPostalCountryLang the contactPostalCountryLang to set
	 */
	public void setContactPostalCountryLang(
			List<List<String>> contactPostalCountryLang) {
		this.contactPostalCountryLang = contactPostalCountryLang;
	}

	/**
	 * @param contactPostalCountryLang the contactPostalCountryLang to add
	 */
	public void addContactPostalCountryLang(List<String> contactPostalCountryLang) {
		this.getContactPostalCountryLang().add(contactPostalCountryLang);
	}

	/**
	 * @return the contactPostalMunicipality
	 */
	public List<List<String>> getContactPostalMunicipality() {
        if (this.contactPostalMunicipality == null) {
        	this.contactPostalMunicipality = new ArrayList<List<String>>();
        }
		return this.contactPostalMunicipality;
	}

	/**
	 * @param contactPostalMunicipality the contactPostalMunicipality to set
	 */
	public void setContactPostalMunicipality(
			List<List<String>> contactPostalMunicipality) {
		this.contactPostalMunicipality = contactPostalMunicipality;
	}

	/**
	 * @param contactPostalMunicipality the contactPostalMunicipality to add
	 */
	public void addContactPostalMunicipality(List<String> contactPostalMunicipality) {
		this.getContactPostalMunicipality().add(contactPostalMunicipality);
	}

	/**
	 * @return the contactPostalMunicipalityLang
	 */
	public List<List<String>> getContactPostalMunicipalityLang() {
        if (this.contactPostalMunicipalityLang == null) {
        	this.contactPostalMunicipalityLang = new ArrayList<List<String>>();
        }
		return this.contactPostalMunicipalityLang;
	}

	/**
	 * @param contactPostalMunicipalityLang the contactPostalMunicipalityLang to set
	 */
	public void setContactPostalMunicipalityLang(
			List<List<String>> contactPostalMunicipalityLang) {
		this.contactPostalMunicipalityLang = contactPostalMunicipalityLang;
	}

	/**
	 * @param contactPostalMunicipalityLang the contactPostalMunicipalityLang to add
	 */
	public void addContactPostalMunicipalityLang(List<String> contactPostalMunicipalityLang) {
		this.getContactPostalMunicipalityLang().add(contactPostalMunicipalityLang);
	}

	/**
	 * @return the contactPostalStreet
	 */
	public List<List<String>> getContactPostalStreet() {
        if (this.contactPostalStreet == null) {
        	this.contactPostalStreet = new ArrayList<List<String>>();
        }
		return this.contactPostalStreet;
	}

	/**
	 * @param contactPostalStreet the contactPostalStreet to set
	 */
	public void setContactPostalStreet(List<List<String>> contactPostalStreet) {
		this.contactPostalStreet = contactPostalStreet;
	}

	/**
	 * @param contactPostalStreet the contactPostalStreet to add
	 */
	public void addContactPostalStreet(List<String> contactPostalStreet) {
		this.getContactPostalStreet().add(contactPostalStreet);
	}

	/**
	 * @return the contactPostalStreetLang
	 */
	public List<List<String>> getContactPostalStreetLang() {
        if (this.contactPostalStreetLang == null) {
        	this.contactPostalStreetLang = new ArrayList<List<String>>();
        }
		return this.contactPostalStreetLang;
	}

	/**
	 * @param contactPostalStreetLang the contactPostalStreetLang to set
	 */
	public void setContactPostalStreetLang(
			List<List<String>> contactPostalStreetLang) {
		this.contactPostalStreetLang = contactPostalStreetLang;
	}

	/**
	 * @param contactPostalStreetLang the contactPostalStreetLang to add
	 */
	public void addContactPostalStreetLang(List<String> contactPostalStreetLang) {
		this.getContactPostalStreetLang().add(contactPostalStreetLang);
	}

	/**
	 * @return the contactContinent
	 */
	public List<List<String>> getContactContinent() {
        if (this.contactContinent == null) {
        	this.contactContinent = new ArrayList<List<String>>();
        }
		return this.contactContinent;
	}

	/**
	 * @param contactContinent the contactContinent to set
	 */
	public void setContactContinent(List<List<String>> contactContinent) {
		this.contactContinent = contactContinent;
	}

	/**
	 * @param contactPostalStreetLang the contactPostalStreetLang to add
	 */
	public void addContactContinent(List<String> contactContinent) {
		this.getContactContinent().add(contactContinent);
	}

	/**
	 * @return the contactTelephone
	 */
	public List<List<String>> getContactTelephone() {
        if (this.contactTelephone == null) {
        	this.contactTelephone = new ArrayList<List<String>>();
        }
		return this.contactTelephone;
	}

	/**
	 * @param contactTelephone the contactTelephone to set
	 */
	public void setContactTelephone(List<List<String>> contactTelephone) {
		this.contactTelephone = contactTelephone;
	}

	/**
	 * @param contactTelephone the contactTelephone to add
	 */
	public void addContactTelephone(List<String> contactTelephone) {
		this.getContactTelephone().add(contactTelephone);
	}

	/**
	 * @return the contactFax
	 */
	public List<List<String>> getContactFax() {
        if (this.contactFax == null) {
        	this.contactFax = new ArrayList<List<String>>();
        }
		return this.contactFax;
	}

	/**
	 * @param contactFax the contactFax to set
	 */
	public void setContactFax(List<List<String>> contactFax) {
		this.contactFax = contactFax;
	}

	/**
	 * @param contactFax the contactFax to add
	 */
	public void addContactFax(List<String> contactFax) {
		this.getContactFax().add(contactFax);
	}

	/**
	 * @return the contactEmailHref
	 */
	public List<List<String>> getContactEmailHref() {
        if (this.contactEmailHref == null) {
        	this.contactEmailHref = new ArrayList<List<String>>();
        }
		return this.contactEmailHref;
	}

	/**
	 * @param contactEmailHref the contactEmailHref to set
	 */
	public void setContactEmailHref(List<List<String>> contactEmailHref) {
		this.contactEmailHref = contactEmailHref;
	}

	/**
	 * @param contactEmailHref the contactEmailHref to add
	 */
	public void addContactEmailHref(List<String> contactEmailHref) {
		this.getContactEmailHref().add(contactEmailHref);
	}

	/**
	 * @return the contactEmailTitle
	 */
	public List<List<String>> getContactEmailTitle() {
        if (this.contactEmailTitle == null) {
        	this.contactEmailTitle = new ArrayList<List<String>>();
        }
		return this.contactEmailTitle;
	}

	/**
	 * @param contactEmailTitle the contactEmailTitle to set
	 */
	public void setContactEmailTitle(List<List<String>> contactEmailTitle) {
		this.contactEmailTitle = contactEmailTitle;
	}

	/**
	 * @param contactEmailTitle the contactEmailTitle to add
	 */
	public void addContactEmailTitle(List<String> contactEmailTitle) {
		this.getContactEmailTitle().add(contactEmailTitle);
	}

	/**
	 * @return the contactEmailLang
	 */
	public List<List<String>> getContactEmailLang() {
        if (this.contactEmailLang == null) {
        	this.contactEmailLang = new ArrayList<List<String>>();
        }
		return this.contactEmailLang;
	}

	/**
	 * @param contactEmailLang the contactEmailLang to set
	 */
	public void setContactEmailLang(List<List<String>> contactEmailLang) {
		this.contactEmailLang = contactEmailLang;
	}

	/**
	 * @param contactEmailLang the contactEmailLang to add
	 */
	public void addContactEmailLang(List<String> contactEmailLang) {
		this.getContactEmailLang().add(contactEmailLang);
	}

	/**
	 * @return the contactWebpageHref
	 */
	public List<List<String>> getContactWebpageHref() {
        if (this.contactWebpageHref == null) {
        	this.contactWebpageHref = new ArrayList<List<String>>();
        }
		return this.contactWebpageHref;
	}

	/**
	 * @param contactWebpageHref the contactWebpageHref to set
	 */
	public void setContactWebpageHref(List<List<String>> contactWebpageHref) {
		this.contactWebpageHref = contactWebpageHref;
	}

	/**
	 * @param contactWebpageHref the contactWebpageHref to add
	 */
	public void addContactWebpageHref(List<String> contactWebpageHref) {
		this.getContactWebpageHref().add(contactWebpageHref);
	}

	/**
	 * @return the contactWebpageTitle
	 */
	public List<List<String>> getContactWebpageTitle() {
        if (this.contactWebpageTitle == null) {
        	this.contactWebpageTitle = new ArrayList<List<String>>();
        }
		return this.contactWebpageTitle;
	}

	/**
	 * @param contactWebpageTitle the contactWebpageTitle to set
	 */
	public void setContactWebpageTitle(List<List<String>> contactWebpageTitle) {
		this.contactWebpageTitle = contactWebpageTitle;
	}

	/**
	 * @param contactWebpageTitle the contactWebpageTitle to add
	 */
	public void addContactWebpageTitle(List<String> contactWebpageTitle) {
		this.getContactWebpageTitle().add(contactWebpageTitle);
	}

	/**
	 * @return the contactWebpageLang
	 */
	public List<List<String>> getContactWebpageLang() {
        if (this.contactWebpageLang == null) {
        	this.contactWebpageLang = new ArrayList<List<String>>();
        }
		return this.contactWebpageLang;
	}

	/**
	 * @param contactWebpageLang the contactWebpageLang to set
	 */
	public void setContactWebpageLang(List<List<String>> contactWebpageLang) {
		this.contactWebpageLang = contactWebpageLang;
	}

	/**
	 * @param contactWebpageLang the contactWebpageLang to add
	 */
	public void addContactWebpageLang(List<String> contactWebpageLang) {
		this.getContactWebpageLang().add(contactWebpageLang);
	}

	/**
	 * @return the contactNumberOfVisitorsAddress
	 */
	public List<List<String>> getContactNumberOfVisitorsAddress() {
        if (this.contactNumberOfVisitorsAddress == null) {
        	this.contactNumberOfVisitorsAddress = new ArrayList<List<String>>();
        }
		return this.contactNumberOfVisitorsAddress;
	}

	/**
	 * @param contactNumberOfVisitorsAddress the contactNumberOfVisitorsAddress to set
	 */
	public void setContactNumberOfVisitorsAddress(
			List<List<String>> contactNumberOfVisitorsAddress) {
		this.contactNumberOfVisitorsAddress = contactNumberOfVisitorsAddress;
	}

	/**
	 * @param contactNumberOfVisitorsAddress the contactNumberOfVisitorsAddress to add
	 */
	public void addContactNumberOfVisitorsAddress(List<String> contactNumberOfVisitorsAddress) {
		this.getContactNumberOfVisitorsAddress().add(contactNumberOfVisitorsAddress);
	}

	/**
	 * @return the contactNumberOfPostalAddress
	 */
	public List<List<String>> getContactNumberOfPostalAddress() {
        if (this.contactNumberOfPostalAddress == null) {
        	this.contactNumberOfPostalAddress = new ArrayList<List<String>>();
        }
		return this.contactNumberOfPostalAddress;
	}

	/**
	 * @param contactNumberOfPostalAddress the contactNumberOfPostalAddress to set
	 */
	public void setContactNumberOfPostalAddress(
			List<List<String>> contactNumberOfPostalAddress) {
		this.contactNumberOfPostalAddress = contactNumberOfPostalAddress;
	}

	/**
	 * @param contactNumberOfPostalAddress the contactNumberOfPostalAddress to add
	 */
	public void addContactNumberOfPostalAddress(List<String> contactNumberOfPostalAddress) {
		this.getContactNumberOfPostalAddress().add(contactNumberOfPostalAddress);
	}

	/**
	 * @return the contactNumberOfEmailAddress
	 */
	public List<List<String>> getContactNumberOfEmailAddress() {
        if (this.contactNumberOfEmailAddress == null) {
        	this.contactNumberOfEmailAddress = new ArrayList<List<String>>();
        }
		return this.contactNumberOfEmailAddress;
	}

	/**
	 * @param contactNumberOfEmailAddress the contactNumberOfEmailAddress to set
	 */
	public void setContactNumberOfEmailAddress(List<List<String>> contactNumberOfEmailAddress) {
		this.contactNumberOfEmailAddress = contactNumberOfEmailAddress;
	}

	/**
	 * @param contactNumberOfEmailAddress the contactNumberOfEmailAddress to add
	 */
	public void addContactNumberOfEmailAddress(List<String> contactNumberOfEmailAddress) {
		this.getContactNumberOfEmailAddress().add(contactNumberOfEmailAddress);
	}

	/**
	 * @return the contactNumberOfWebpageAddress
	 */
	public List<List<String>> getContactNumberOfWebpageAddress() {
        if (this.contactNumberOfWebpageAddress == null) {
        	this.contactNumberOfWebpageAddress = new ArrayList<List<String>>();
        }
		return this.contactNumberOfWebpageAddress;
	}

	/**
	 * @param contactNumberOfWebpageAddress the contactNumberOfWebpageAddress to set
	 */
	public void setContactNumberOfWebpageAddress(List<List<String>> contactNumberOfWebpageAddress) {
		this.contactNumberOfWebpageAddress = contactNumberOfWebpageAddress;
	}

	/**
	 * @param contactNumberOfWebpageAddress the contactNumberOfWebpageAddress to add
	 */
	public void addContactNumberOfWebpageAddress(List<String> contactNumberOfWebpageAddress) {
		this.getContactNumberOfWebpageAddress().add(contactNumberOfWebpageAddress);
	}

	/**
	 * @return the asOpening
	 */
	public List<List<String>> getAsOpening() {
		if (this.asOpening == null) {
			this.asOpening = new ArrayList<List<String>>();
		}
		return this.asOpening;
	}

	/**
	 * @param asOpening the asOpening to set
	 */
	public void setAsOpening(List<List<String>> asOpening) {
		this.asOpening = asOpening;
	}

	/**
	 * @param asOpening the asOpening to add
	 */
	public void addAsOpening(List<String> asOpening) {
		this.getAsOpening().add(asOpening);
	}

	/**
	 * @return the asOpeningLang
	 */
	public List<List<String>> getAsOpeningLang() {
		if (this.asOpeningLang == null) {
			this.asOpeningLang = new ArrayList<List<String>>();
		}
		return this.asOpeningLang;
	}

	/**
	 * @param asOpeningLang the asOpeningLang to set
	 */
	public void setAsOpeningLang(List<List<String>> asOpeningLang) {
		this.asOpeningLang = asOpeningLang;
	}

	/**
	 * @param asOpeningLang the asOpeningLang to add
	 */
	public void addAsOpeningLang(List<String> asOpeningLang) {
		this.getAsOpeningLang().add(asOpeningLang);
	}

	/**
	 * @return the asClosing
	 */
	public List<List<String>> getAsClosing() {
		if (this.asClosing == null) {
			this.asClosing = new ArrayList<List<String>>();
		}
		return this.asClosing;
	}

	/**
	 * @param asClosing the asClosing to set
	 */
	public void setAsClosing(List<List<String>> asClosing) {
		this.asClosing = asClosing;
	}

	/**
	 * @param asClosing the asClosing to add
	 */
	public void addAsClosing(List<String> asClosing) {
		this.getAsClosing().add(asClosing);
	}

	/**
	 * @return the asClosingLang
	 */
	public List<List<String>> getAsClosingLang() {
		if (this.asClosingLang == null) {
			this.asClosingLang = new ArrayList<List<String>>();
		}
		return this.asClosingLang;
	}

	/**
	 * @param asClosingLang the asClosingLang to set
	 */
	public void setAsClosingLang(List<List<String>> asClosingLang) {
		this.asClosingLang = asClosingLang;
	}

	/**
	 * @param asClosingLang the asClosingLang to add
	 */
	public void addAsClosingLang(List<String> asClosingLang) {
		this.getAsClosingLang().add(asClosingLang);
	}

	/**
	 * @return the asNumberOfDirections
	 */
	public List<List<String>> getAsNumberOfDirections() {
		if (this.asNumberOfDirections == null) {
			this.asNumberOfDirections = new ArrayList<List<String>>();
		}
		return this.asNumberOfDirections;
	}

	/**
	 * @param asNumberOfDirections the asNumberOfDirections to set
	 */
	public void setAsNumberOfDirections(List<List<String>> asNumberOfDirections) {
		this.asNumberOfDirections = asNumberOfDirections;
	}

	/**
	 * @param asNumberOfDirections the asNumberOfDirections to add
	 */
	public void addAsNumberOfDirections(List<String> asNumberOfDirections) {
		this.getAsNumberOfDirections().add(asNumberOfDirections);
	}

	/**
	 * @return the asDirections
	 */
	public List<List<String>> getAsDirections() {
		if (this.asDirections == null) {
			this.asDirections = new ArrayList<List<String>>();
		}
		return this.asDirections;
	}

	/**
	 * @param asDirections the asDirections to set
	 */
	public void setAsDirections(List<List<String>> asDirections) {
		this.asDirections = asDirections;
	}

	/**
	 * @param asDirections the asDirections to add
	 */
	public void addAsDirections(List<String> asDirections) {
		this.getAsDirections().add(asDirections);
	}

	/**
	 * @return the asDirectionsLang
	 */
	public List<List<String>> getAsDirectionsLang() {
		if (this.asDirectionsLang == null) {
			this.asDirectionsLang = new ArrayList<List<String>>();
		}
		return this.asDirectionsLang;
	}

	/**
	 * @param asDirectionsLang the asDirectionsLang to set
	 */
	public void setAsDirectionsLang(List<List<String>> asDirectionsLang) {
		this.asDirectionsLang = asDirectionsLang;
	}

	/**
	 * @param asDirectionsLang the asDirectionsLang to add
	 */
	public void addAsDirectionsLang(List<String> asDirectionsLang) {
		this.getAsDirectionsLang().add(asDirectionsLang);
	}

	/**
	 * @return the asDirectionsCitationHref
	 */
	public List<List<String>> getAsDirectionsCitationHref() {
		if (this.asDirectionsCitationHref == null) {
			this.asDirectionsCitationHref = new ArrayList<List<String>>();
		}
		return this.asDirectionsCitationHref;
	}

	/**
	 * @param asDirectionsCitationHref the asDirectionsCitationHref to set
	 */
	public void setAsDirectionsCitationHref(
			List<List<String>> asDirectionsCitationHref) {
		this.asDirectionsCitationHref = asDirectionsCitationHref;
	}

	/**
	 * @param asDirectionsCitationHref the asDirectionsCitationHref to add
	 */
	public void addAsDirectionsCitationHref(List<String> asDirectionsCitationHref) {
		this.getAsDirectionsCitationHref().add(asDirectionsCitationHref);
	}

	/**
	 * @return the asAccessQuestion
	 */
	public List<List<String>> getAsAccessQuestion() {
		if (this.asAccessQuestion == null) {
			this.asAccessQuestion = new ArrayList<List<String>>();
		}
		return this.asAccessQuestion;
	}

	/**
	 * @param asAccessQuestion the asAccessQuestion to set
	 */
	public void setAsAccessQuestion(List<List<String>> asAccessQuestion) {
		this.asAccessQuestion = asAccessQuestion;
	}

	/**
	 * @param asAccessQuestion the asAccessQuestion to add
	 */
	public void addAsAccessQuestion(List<String> asAccessQuestion) {
		this.getAsAccessQuestion().add(asAccessQuestion);
	}

	/**
	 * @return the asRestaccess
	 */
	public List<List<String>> getAsRestaccess() {
		if (this.asRestaccess == null) {
			this.asRestaccess = new ArrayList<List<String>>();
		}
		return this.asRestaccess;
	}

	/**
	 * @param asRestaccess the asRestaccess to set
	 */
	public void setAsRestaccess(List<List<String>> asRestaccess) {
		this.asRestaccess = asRestaccess;
	}

	/**
	 * @param asRestaccess the asRestaccess to add
	 */
	public void addAsRestaccess(List<String> asRestaccess) {
		this.getAsRestaccess().add(asRestaccess);
	}

	/**
	 * @return the asRestaccessLang
	 */
	public List<List<String>> getAsRestaccessLang() {
		if (this.asRestaccessLang == null) {
			this.asRestaccessLang = new ArrayList<List<String>>();
		}
		return this.asRestaccessLang;
	}

	/**
	 * @param asRestaccessLang the asRestaccessLang to set
	 */
	public void setAsRestaccessLang(List<List<String>> asRestaccessLang) {
		this.asRestaccessLang = asRestaccessLang;
	}

	/**
	 * @param asRestaccessLang the asRestaccessLang to add
	 */
	public void addAsRestaccessLang(List<String> asRestaccessLang) {
		this.getAsRestaccessLang().add(asRestaccessLang);
	}

	/**
	 * @return the asNumberOfTermsOfUse
	 */
	public List<List<String>> getAsNumberOfTermsOfUse() {
		if (this.asNumberOfTermsOfUse == null) {
			this.asNumberOfTermsOfUse = new ArrayList<List<String>>();
		}
		return this.asNumberOfTermsOfUse;
	}

	/**
	 * @param asNumberOfTermsOfUse the asNumberOfTermsOfUse to set
	 */
	public void setAsNumberOfTermsOfUse(List<List<String>> asNumberOfTermsOfUse) {
		this.asNumberOfTermsOfUse = asNumberOfTermsOfUse;
	}

	/**
	 * @param asNumberOfTermsOfUse the asNumberOfTermsOfUse to add
	 */
	public void addAsNumberOfTermsOfUse(List<String> asNumberOfTermsOfUse) {
		this.getAsNumberOfTermsOfUse().add(asNumberOfTermsOfUse);
	}

	/**
	 * @return the asTermsOfUse
	 */
	public List<List<String>> getAsTermsOfUse() {
		if (this.asTermsOfUse == null) {
			this.asTermsOfUse = new ArrayList<List<String>>();
		}
		return this.asTermsOfUse;
	}

	/**
	 * @param asTermsOfUse the asTermsOfUse to set
	 */
	public void setAsTermsOfUse(List<List<String>> asTermsOfUse) {
		this.asTermsOfUse = asTermsOfUse;
	}

	/**
	 * @param asTermsOfUse the asTermsOfUse to add
	 */
	public void addAsTermsOfUse(List<String> asTermsOfUse) {
		this.getAsTermsOfUse().add(asTermsOfUse);
	}

	/**
	 * @return the asTermsOfUseLang
	 */
	public List<List<String>> getAsTermsOfUseLang() {
		if (this.asTermsOfUseLang == null) {
			this.asTermsOfUseLang = new ArrayList<List<String>>();
		}
		return this.asTermsOfUseLang;
	}

	/**
	 * @param asTermsOfUseLang the asTermsOfUseLang to set
	 */
	public void setAsTermsOfUseLang(List<List<String>> asTermsOfUseLang) {
		this.asTermsOfUseLang = asTermsOfUseLang;
	}

	/**
	 * @param asTermsOfUseLang the asTermsOfUseLang to add
	 */
	public void addAsTermsOfUseLang(List<String> asTermsOfUseLang) {
		this.getAsTermsOfUseLang().add(asTermsOfUseLang);
	}

	/**
	 * @return the asTermsOfUseHref
	 */
	public List<List<String>> getAsTermsOfUseHref() {
		if (this.asTermsOfUseHref == null) {
			this.asTermsOfUseHref = new ArrayList<List<String>>();
		}
		return this.asTermsOfUseHref;
	}

	/**
	 * @param asTermsOfUseHref the asTermsOfUseHref to set
	 */
	public void setAsTermsOfUseHref(List<List<String>> asTermsOfUseHref) {
		this.asTermsOfUseHref = asTermsOfUseHref;
	}

	/**
	 * @param asTermsOfUseHref the asTermsOfUseHref to add
	 */
	public void addAsTermsOfUseHref(List<String> asTermsOfUseHref) {
		this.getAsTermsOfUseHref().add(asTermsOfUseHref);
	}

	/**
	 * @return the asAccessibilityQuestion
	 */
	public List<List<String>> getAsAccessibilityQuestion() {
		if (this.asAccessibilityQuestion == null) {
			this.asAccessibilityQuestion = new ArrayList<List<String>>();
		}
		return this.asAccessibilityQuestion;
	}

	/**
	 * @param asAccessibilityQuestion the asAccessibilityQuestion to set
	 */
	public void setAsAccessibilityQuestion(
			List<List<String>> asAccessibilityQuestion) {
		this.asAccessibilityQuestion = asAccessibilityQuestion;
	}

	/**
	 * @param asAccessibilityQuestion the asAccessibilityQuestion to add
	 */
	public void addAsAccessibilityQuestion(List<String> asAccessibilityQuestion) {
		this.getAsAccessibilityQuestion().add(asAccessibilityQuestion);
	}

	/**
	 * @return the asAccessibility
	 */
	public List<List<String>> getAsAccessibility() {
		if (this.asAccessibility == null) {
			this.asAccessibility = new ArrayList<List<String>>();
		}
		return this.asAccessibility;
	}

	/**
	 * @param asAccessibility the asAccessibility to set
	 */
	public void setAsAccessibility(List<List<String>> asAccessibility) {
		this.asAccessibility = asAccessibility;
	}

	/**
	 * @param asAccessibility the asAccessibility to add
	 */
	public void addAsAccessibility(List<String> asAccessibility) {
		this.getAsAccessibility().add(asAccessibility);
	}

	/**
	 * @return the asAccessibilityLang
	 */
	public List<List<String>> getAsAccessibilityLang() {
		if (this.asAccessibilityLang == null) {
			this.asAccessibilityLang = new ArrayList<List<String>>();
		}
		return this.asAccessibilityLang;
	}

	/**
	 * @param asAccessibilityLang the asAccessibilityLang to set
	 */
	public void setAsAccessibilityLang(List<List<String>> asAccessibilityLang) {
		this.asAccessibilityLang = asAccessibilityLang;
	}

	/**
	 * @param asAccessibilityLang the asAccessibilityLang to add
	 */
	public void addAsAccessibilityLang(List<String> asAccessibilityLang) {
		this.getAsAccessibilityLang().add(asAccessibilityLang);
	}

	/**
	 * @return the asSearchRoomTelephone
	 */
	public List<List<String>> getAsSearchRoomTelephone() {
		if (this.asSearchRoomTelephone == null) {
			this.asSearchRoomTelephone = new ArrayList<List<String>>();
		}
		return this.asSearchRoomTelephone;
	}

	/**
	 * @param asSearchRoomTelephone the asSearchRoomTelephone to set
	 */
	public void setAsSearchRoomTelephone(List<List<String>> asSearchRoomTelephone) {
		this.asSearchRoomTelephone = asSearchRoomTelephone;
	}

	/**
	 * @param asSearchRoomTelephone the asSearchRoomTelephone to add
	 */
	public void addAsSearchRoomTelephone(List<String> asSearchRoomTelephone) {
		this.getAsSearchRoomTelephone().add(asSearchRoomTelephone);
	}

	/**
	 * @return the asSearchRoomNumberOfEmail
	 */
	public List<List<String>> getAsSearchRoomNumberOfEmail() {
		if (this.asSearchRoomNumberOfEmail == null) {
			this.asSearchRoomNumberOfEmail = new ArrayList<List<String>>();
		}
		return this.asSearchRoomNumberOfEmail;
	}

	/**
	 * @param asSearchRoomNumberOfEmail the asSearchRoomNumberOfEmail to set
	 */
	public void setAsSearchRoomNumberOfEmail(
			List<List<String>> asSearchRoomNumberOfEmail) {
		this.asSearchRoomNumberOfEmail = asSearchRoomNumberOfEmail;
	}

	/**
	 * @param asSearchRoomNumberOfEmail the asSearchRoomNumberOfEmail to add
	 */
	public void addAsSearchRoomNumberOfEmail(List<String> asSearchRoomNumberOfEmail) {
		this.getAsSearchRoomNumberOfEmail().add(asSearchRoomNumberOfEmail);
	}

	/**
	 * @return the asSearchRoomEmailHref
	 */
	public List<List<String>> getAsSearchRoomEmailHref() {
		if (this.asSearchRoomEmailHref == null) {
			this.asSearchRoomEmailHref = new ArrayList<List<String>>();
		}
		return this.asSearchRoomEmailHref;
	}

	/**
	 * @param asSearchRoomEmailHref the asSearchRoomEmailHref to set
	 */
	public void setAsSearchRoomEmailHref(List<List<String>> asSearchRoomEmailHref) {
		this.asSearchRoomEmailHref = asSearchRoomEmailHref;
	}

	/**
	 * @param asSearchRoomEmailHref the asSearchRoomEmailHref to add
	 */
	public void addAsSearchRoomEmailHref(List<String> asSearchRoomEmailHref) {
		this.getAsSearchRoomEmailHref().add(asSearchRoomEmailHref);
	}

	/**
	 * @return the asSearchRoomEmailTitle
	 */
	public List<List<String>> getAsSearchRoomEmailTitle() {
		if (this.asSearchRoomEmailTitle == null) {
			this.asSearchRoomEmailTitle = new ArrayList<List<String>>();
		}
		return this.asSearchRoomEmailTitle;
	}

	/**
	 * @param asSearchRoomEmailTitle the asSearchRoomEmailTitle to set
	 */
	public void setAsSearchRoomEmailTitle(List<List<String>> asSearchRoomEmailTitle) {
		this.asSearchRoomEmailTitle = asSearchRoomEmailTitle;
	}

	/**
	 * @param asSearchRoomEmailTitle the asSearchRoomEmailTitle to add
	 */
	public void addAsSearchRoomEmailTitle(List<String> asSearchRoomEmailTitle) {
		this.getAsSearchRoomEmailTitle().add(asSearchRoomEmailTitle);
	}

	/**
	 * @return the asSearchRoomEmailLang
	 */
	public List<List<String>> getAsSearchRoomEmailLang() {
		if (this.asSearchRoomEmailLang == null) {
			this.asSearchRoomEmailLang = new ArrayList<List<String>>();
		}
		return this.asSearchRoomEmailLang;
	}

	/**
	 * @param asSearchRoomEmailLang the asSearchRoomEmailLang to set
	 */
	public void setAsSearchRoomEmailLang(List<List<String>> asSearchRoomEmailLang) {
		this.asSearchRoomEmailLang = asSearchRoomEmailLang;
	}

	/**
	 * @param asSearchRoomEmailLang the asSearchRoomEmailLang to add
	 */
	public void addAsSearchRoomEmailLang(List<String> asSearchRoomEmailLang) {
		this.getAsSearchRoomEmailLang().add(asSearchRoomEmailLang);
	}

	/**
	 * @return the asSearchRoomNumberOfWebpage
	 */
	public List<List<String>> getAsSearchRoomNumberOfWebpage() {
		if (this.asSearchRoomNumberOfWebpage == null) {
			this.asSearchRoomNumberOfWebpage = new ArrayList<List<String>>();
		}
		return this.asSearchRoomNumberOfWebpage;
	}

	/**
	 * @param asSearchRoomNumberOfWebpage the asSearchRoomNumberOfWebpage to set
	 */
	public void setAsSearchRoomNumberOfWebpage(
			List<List<String>> asSearchRoomNumberOfWebpage) {
		this.asSearchRoomNumberOfWebpage = asSearchRoomNumberOfWebpage;
	}

	/**
	 * @param asSearchRoomNumberOfWebpage the asSearchRoomNumberOfWebpage to add
	 */
	public void addAsSearchRoomNumberOfWebpage(List<String> asSearchRoomNumberOfWebpage) {
		this.getAsSearchRoomNumberOfWebpage().add(asSearchRoomNumberOfWebpage);
	}

	/**
	 * @return the asSearchRoomWebpageHref
	 */
	public List<List<String>> getAsSearchRoomWebpageHref() {
		if (this.asSearchRoomWebpageHref == null) {
			this.asSearchRoomWebpageHref = new ArrayList<List<String>>();
		}
		return this.asSearchRoomWebpageHref;
	}

	/**
	 * @param asSearchRoomWebpageHref the asSearchRoomWebpageHref to set
	 */
	public void setAsSearchRoomWebpageHref(
			List<List<String>> asSearchRoomWebpageHref) {
		this.asSearchRoomWebpageHref = asSearchRoomWebpageHref;
	}

	/**
	 * @param asSearchRoomWebpageHref the asSearchRoomWebpageHref to add
	 */
	public void addAsSearchRoomWebpageHref(List<String> asSearchRoomWebpageHref) {
		this.getAsSearchRoomWebpageHref().add(asSearchRoomWebpageHref);
	}

	/**
	 * @return the asSearchRoomWebpageTitle
	 */
	public List<List<String>> getAsSearchRoomWebpageTitle() {
		if (this.asSearchRoomWebpageTitle == null) {
			this.asSearchRoomWebpageTitle = new ArrayList<List<String>>();
		}
		return this.asSearchRoomWebpageTitle;
	}

	/**
	 * @param asSearchRoomWebpageTitle the asSearchRoomWebpageTitle to set
	 */
	public void setAsSearchRoomWebpageTitle(
			List<List<String>> asSearchRoomWebpageTitle) {
		this.asSearchRoomWebpageTitle = asSearchRoomWebpageTitle;
	}

	/**
	 * @param asSearchRoomWebpageTitle the asSearchRoomWebpageTitle to add
	 */
	public void addAsSearchRoomWebpageTitle(List<String> asSearchRoomWebpageTitle) {
		this.getAsSearchRoomWebpageTitle().add(asSearchRoomWebpageTitle);
	}

	/**
	 * @return the asSearchRoomWebpageLang
	 */
	public List<List<String>> getAsSearchRoomWebpageLang() {
		if (this.asSearchRoomWebpageLang == null) {
			this.asSearchRoomWebpageLang = new ArrayList<List<String>>();
		}
		return this.asSearchRoomWebpageLang;
	}

	/**
	 * @param asSearchRoomWebpageLang the asSearchRoomWebpageLang to set
	 */
	public void setAsSearchRoomWebpageLang(
			List<List<String>> asSearchRoomWebpageLang) {
		this.asSearchRoomWebpageLang = asSearchRoomWebpageLang;
	}

	/**
	 * @param asSearchRoomWebpageLang the asSearchRoomWebpageLang to add
	 */
	public void addAsSearchRoomWebpageLang(List<String> asSearchRoomWebpageLang) {
		this.getAsSearchRoomWebpageLang().add(asSearchRoomWebpageLang);
	}

	/**
	 * @return the asSearchRoomWorkPlaces
	 */
	public List<List<String>> getAsSearchRoomWorkPlaces() {
		if (this.asSearchRoomWorkPlaces == null) {
			this.asSearchRoomWorkPlaces = new ArrayList<List<String>>();
		}
		return this.asSearchRoomWorkPlaces;
	}

	/**
	 * @param asSearchRoomWorkPlaces the asSearchRoomWorkPlaces to set
	 */
	public void setAsSearchRoomWorkPlaces(List<List<String>> asSearchRoomWorkPlaces) {
		this.asSearchRoomWorkPlaces = asSearchRoomWorkPlaces;
	}

	/**
	 * @param asSearchRoomWorkPlaces the asSearchRoomWorkPlaces to add
	 */
	public void addAsSearchRoomWorkPlaces(List<String> asSearchRoomWorkPlaces) {
		this.getAsSearchRoomWorkPlaces().add(asSearchRoomWorkPlaces);
	}

	/**
	 * @return the asSearchRoomComputerPlaces
	 */
	public List<List<String>> getAsSearchRoomComputerPlaces() {
		if (this.asSearchRoomComputerPlaces == null) {
			this.asSearchRoomComputerPlaces = new ArrayList<List<String>>();
		}
		return this.asSearchRoomComputerPlaces;
	}

	/**
	 * @param asSearchRoomComputerPlaces the asSearchRoomComputerPlaces to set
	 */
	public void setAsSearchRoomComputerPlaces(
			List<List<String>> asSearchRoomComputerPlaces) {
		this.asSearchRoomComputerPlaces = asSearchRoomComputerPlaces;
	}

	/**
	 * @param asSearchRoomComputerPlaces the asSearchRoomComputerPlaces to add
	 */
	public void addAsSearchRoomComputerPlaces(List<String> asSearchRoomComputerPlaces) {
		this.getAsSearchRoomComputerPlaces().add(asSearchRoomComputerPlaces);
	}

	/**
	 * @return the asSearchRoomComputerPlacesDescription
	 */
	public List<List<String>> getAsSearchRoomComputerPlacesDescription() {
		if (this.asSearchRoomComputerPlacesDescription == null) {
			this.asSearchRoomComputerPlacesDescription = new ArrayList<List<String>>();
		}
		return this.asSearchRoomComputerPlacesDescription;
	}

	/**
	 * @param asSearchRoomComputerPlacesDescription the asSearchRoomComputerPlacesDescription to set
	 */
	public void setAsSearchRoomComputerPlacesDescription(
			List<List<String>> asSearchRoomComputerPlacesDescription) {
		this.asSearchRoomComputerPlacesDescription = asSearchRoomComputerPlacesDescription;
	}

	/**
	 * @param asSearchRoomComputerPlacesDescription the asSearchRoomComputerPlacesDescription to add
	 */
	public void addAsSearchRoomComputerPlacesDescription(List<String> asSearchRoomComputerPlacesDescription) {
		this.getAsSearchRoomComputerPlacesDescription().add(asSearchRoomComputerPlacesDescription);
	}

	/**
	 * @return the asSearchRoomComputerPlacesDescriptionLang
	 */
	public List<List<String>> getAsSearchRoomComputerPlacesDescriptionLang() {
		if (this.asSearchRoomComputerPlacesDescriptionLang == null) {
			this.asSearchRoomComputerPlacesDescriptionLang = new ArrayList<List<String>>();
		}
		return this.asSearchRoomComputerPlacesDescriptionLang;
	}

	/**
	 * @param asSearchRoomComputerPlacesDescriptionLang the asSearchRoomComputerPlacesDescriptionLang to set
	 */
	public void setAsSearchRoomComputerPlacesDescriptionLang(
			List<List<String>> asSearchRoomComputerPlacesDescriptionLang) {
		this.asSearchRoomComputerPlacesDescriptionLang = asSearchRoomComputerPlacesDescriptionLang;
	}

	/**
	 * @param asSearchRoomComputerPlacesDescriptionLang the asSearchRoomComputerPlacesDescriptionLang to add
	 */
	public void addAsSearchRoomComputerPlacesDescriptionLang(List<String> asSearchRoomComputerPlacesDescriptionLang) {
		this.getAsSearchRoomComputerPlacesDescriptionLang().add(asSearchRoomComputerPlacesDescriptionLang);
	}

	/**
	 * @return the asSearchRoomMicrofilmReaders
	 */
	public List<List<String>> getAsSearchRoomMicrofilmReaders() {
		if (this.asSearchRoomMicrofilmReaders == null) {
			this.asSearchRoomMicrofilmReaders = new ArrayList<List<String>>();
		}
		return this.asSearchRoomMicrofilmReaders;
	}

	/**
	 * @param asSearchRoomMicrofilmReaders the asSearchRoomMicrofilmReaders to set
	 */
	public void setAsSearchRoomMicrofilmReaders(
			List<List<String>> asSearchRoomMicrofilmReaders) {
		this.asSearchRoomMicrofilmReaders = asSearchRoomMicrofilmReaders;
	}

	/**
	 * @param asSearchRoomMicrofilmReaders the asSearchRoomMicrofilmReaders to add
	 */
	public void addAsSearchRoomMicrofilmReaders(List<String> asSearchRoomMicrofilmReaders) {
		this.getAsSearchRoomMicrofilmReaders().add(asSearchRoomMicrofilmReaders);
	}

	/**
	 * @return the asSearchRoomPhotographAllowance
	 */
	public List<List<String>> getAsSearchRoomPhotographAllowance() {
		if (this.asSearchRoomPhotographAllowance == null) {
			this.asSearchRoomPhotographAllowance = new ArrayList<List<String>>();
		}
		return this.asSearchRoomPhotographAllowance;
	}

	/**
	 * @param asSearchRoomPhotographAllowance the asSearchRoomPhotographAllowance to set
	 */
	public void setAsSearchRoomPhotographAllowance(
			List<List<String>> asSearchRoomPhotographAllowance) {
		this.asSearchRoomPhotographAllowance = asSearchRoomPhotographAllowance;
	}

	/**
	 * @param asSearchRoomPhotographAllowance the asSearchRoomPhotographAllowance to add
	 */
	public void addAsSearchRoomPhotographAllowance(List<String> asSearchRoomPhotographAllowance) {
		this.getAsSearchRoomPhotographAllowance().add(asSearchRoomPhotographAllowance);
	}

	/**
	 * @return the asSearchRoomNumberOfReadersTicket
	 */
	public List<List<String>> getAsSearchRoomNumberOfReadersTicket() {
		if (this.asSearchRoomNumberOfReadersTicket == null) {
			this.asSearchRoomNumberOfReadersTicket = new ArrayList<List<String>>();
		}
		return this.asSearchRoomNumberOfReadersTicket;
	}

	/**
	 * @param asSearchRoomNumberOfReadersTicket the asSearchRoomNumberOfReadersTicket to set
	 */
	public void setAsSearchRoomNumberOfReadersTicket(
			List<List<String>> asSearchRoomNumberOfReadersTicket) {
		this.asSearchRoomNumberOfReadersTicket = asSearchRoomNumberOfReadersTicket;
	}

	/**
	 * @param asSearchRoomNumberOfReadersTicket the asSearchRoomNumberOfReadersTicket to add
	 */
	public void addAsSearchRoomNumberOfReadersTicket(List<String> asSearchRoomNumberOfReadersTicket) {
		this.getAsSearchRoomNumberOfReadersTicket().add(asSearchRoomNumberOfReadersTicket);
	}

	/**
	 * @return the asSearchRoomReadersTicketHref
	 */
	public List<List<String>> getAsSearchRoomReadersTicketHref() {
		if (this.asSearchRoomReadersTicketHref == null) {
			this.asSearchRoomReadersTicketHref = new ArrayList<List<String>>();
		}
		return this.asSearchRoomReadersTicketHref;
	}

	/**
	 * @param asSearchRoomReadersTicketHref the asSearchRoomReadersTicketHref to set
	 */
	public void setAsSearchRoomReadersTicketHref(
			List<List<String>> asSearchRoomReadersTicketHref) {
		this.asSearchRoomReadersTicketHref = asSearchRoomReadersTicketHref;
	}

	/**
	 * @param asSearchRoomReadersTicketHref the asSearchRoomReadersTicketHref to add
	 */
	public void addAsSearchRoomReadersTicketHref(List<String> asSearchRoomReadersTicketHref) {
		this.getAsSearchRoomReadersTicketHref().add(asSearchRoomReadersTicketHref);
	}

	/**
	 * @return the asSearchRoomReadersTicketContent
	 */
	public List<List<String>> getAsSearchRoomReadersTicketContent() {
		if (this.asSearchRoomReadersTicketContent == null) {
			this.asSearchRoomReadersTicketContent = new ArrayList<List<String>>();
		}
		return this.asSearchRoomReadersTicketContent;
	}

	/**
	 * @param asSearchRoomReadersTicketContent the asSearchRoomReadersTicketContent to set
	 */
	public void setAsSearchRoomReadersTicketContent(
			List<List<String>> asSearchRoomReadersTicketContent) {
		this.asSearchRoomReadersTicketContent = asSearchRoomReadersTicketContent;
	}

	/**
	 * @param asSearchRoomReadersTicketContent the asSearchRoomReadersTicketContent to add
	 */
	public void addAsSearchRoomReadersTicketContent(List<String> asSearchRoomReadersTicketContent) {
		this.getAsSearchRoomReadersTicketContent().add(asSearchRoomReadersTicketContent);
	}

	/**
	 * @return the asSearchRoomReadersTicketLang
	 */
	public List<List<String>> getAsSearchRoomReadersTicketLang() {
		if (this.asSearchRoomReadersTicketLang == null) {
			this.asSearchRoomReadersTicketLang = new ArrayList<List<String>>();
		}
		return this.asSearchRoomReadersTicketLang;
	}

	/**
	 * @param asSearchRoomReadersTicketLang the asSearchRoomReadersTicketLang to set
	 */
	public void setAsSearchRoomReadersTicketLang(
			List<List<String>> asSearchRoomReadersTicketLang) {
		this.asSearchRoomReadersTicketLang = asSearchRoomReadersTicketLang;
	}

	/**
	 * @param asSearchRoomReadersTicketLang the asSearchRoomReadersTicketLang to add
	 */
	public void addAsSearchRoomReadersTicketLang(List<String> asSearchRoomReadersTicketLang) {
		this.getAsSearchRoomReadersTicketLang().add(asSearchRoomReadersTicketLang);
	}

	/**
	 * @return the asSearchRoomNumberOfAdvancedOrders
	 */
	public List<List<String>> getAsSearchRoomNumberOfAdvancedOrders() {
		if (this.asSearchRoomNumberOfAdvancedOrders == null) {
			this.asSearchRoomNumberOfAdvancedOrders = new ArrayList<List<String>>();
		}
		return this.asSearchRoomNumberOfAdvancedOrders;
	}

	/**
	 * @param asSearchRoomNumberOfAdvancedOrders the asSearchRoomNumberOfAdvancedOrders to set
	 */
	public void setAsSearchRoomNumberOfAdvancedOrders(
			List<List<String>> asSearchRoomNumberOfAdvancedOrders) {
		this.asSearchRoomNumberOfAdvancedOrders = asSearchRoomNumberOfAdvancedOrders;
	}

	/**
	 * @param asSearchRoomNumberOfAdvancedOrders the asSearchRoomNumberOfAdvancedOrders to add
	 */
	public void addAsSearchRoomNumberOfAdvancedOrders(List<String> asSearchRoomNumberOfAdvancedOrders) {
		this.getAsSearchRoomNumberOfAdvancedOrders().add(asSearchRoomNumberOfAdvancedOrders);
	}

	/**
	 * @return the asSearchRoomAdvancedOrdersHref
	 */
	public List<List<String>> getAsSearchRoomAdvancedOrdersHref() {
		if (this.asSearchRoomAdvancedOrdersHref == null) {
			this.asSearchRoomAdvancedOrdersHref = new ArrayList<List<String>>();
		}
		return this.asSearchRoomAdvancedOrdersHref;
	}

	/**
	 * @param asSearchRoomAdvancedOrdersHref the asSearchRoomAdvancedOrdersHref to set
	 */
	public void setAsSearchRoomAdvancedOrdersHref(
			List<List<String>> asSearchRoomAdvancedOrdersHref) {
		this.asSearchRoomAdvancedOrdersHref = asSearchRoomAdvancedOrdersHref;
	}

	/**
	 * @param asSearchRoomAdvancedOrdersHref the asSearchRoomAdvancedOrdersHref to add
	 */
	public void addAsSearchRoomAdvancedOrdersHref(List<String> asSearchRoomAdvancedOrdersHref) {
		this.getAsSearchRoomAdvancedOrdersHref().add(asSearchRoomAdvancedOrdersHref);
	}

	/**
	 * @return the asSearchRoomAdvancedOrdersContent
	 */
	public List<List<String>> getAsSearchRoomAdvancedOrdersContent() {
		if (this.asSearchRoomAdvancedOrdersContent == null) {
			this.asSearchRoomAdvancedOrdersContent = new ArrayList<List<String>>();
		}
		return this.asSearchRoomAdvancedOrdersContent;
	}

	/**
	 * @param asSearchRoomAdvancedOrdersContent the asSearchRoomAdvancedOrdersContent to set
	 */
	public void setAsSearchRoomAdvancedOrdersContent(
			List<List<String>> asSearchRoomAdvancedOrdersContent) {
		this.asSearchRoomAdvancedOrdersContent = asSearchRoomAdvancedOrdersContent;
	}

	/**
	 * @param asSearchRoomAdvancedOrdersContent the asSearchRoomAdvancedOrdersContent to add
	 */
	public void addAsSearchRoomAdvancedOrdersContent(List<String> asSearchRoomAdvancedOrdersContent) {
		this.getAsSearchRoomAdvancedOrdersContent().add(asSearchRoomAdvancedOrdersContent);
	}

	/**
	 * @return the asSearchRoomAdvancedOrdersLang
	 */
	public List<List<String>> getAsSearchRoomAdvancedOrdersLang() {
		if (this.asSearchRoomAdvancedOrdersLang == null) {
			this.asSearchRoomAdvancedOrdersLang = new ArrayList<List<String>>();
		}
		return this.asSearchRoomAdvancedOrdersLang;
	}

	/**
	 * @param asSearchRoomAdvancedOrdersLang the asSearchRoomAdvancedOrdersLang to set
	 */
	public void setAsSearchRoomAdvancedOrdersLang(
			List<List<String>> asSearchRoomAdvancedOrdersLang) {
		this.asSearchRoomAdvancedOrdersLang = asSearchRoomAdvancedOrdersLang;
	}

	/**
	 * @param asSearchRoomAdvancedOrdersLang the asSearchRoomAdvancedOrdersLang to add
	 */
	public void addAsSearchRoomAdvancedOrdersLang(List<String> asSearchRoomAdvancedOrdersLang) {
		this.getAsSearchRoomAdvancedOrdersLang().add(asSearchRoomAdvancedOrdersLang);
	}

	/**
	 * @return the asSearchRoomResearchServicesContent
	 */
	public List<List<String>> getAsSearchRoomResearchServicesContent() {
		if (this.asSearchRoomResearchServicesContent == null) {
			this.asSearchRoomResearchServicesContent = new ArrayList<List<String>>();
		}
		return this.asSearchRoomResearchServicesContent;
	}

	/**
	 * @param asSearchRoomResearchServicesContent the asSearchRoomResearchServicesContent to set
	 */
	public void setAsSearchRoomResearchServicesContent(
			List<List<String>> asSearchRoomResearchServicesContent) {
		this.asSearchRoomResearchServicesContent = asSearchRoomResearchServicesContent;
	}

	/**
	 * @param asSearchRoomResearchServicesContent the asSearchRoomResearchServicesContent to add
	 */
	public void addAsSearchRoomResearchServicesContent(List<String> asSearchRoomResearchServicesContent) {
		this.getAsSearchRoomResearchServicesContent().add(asSearchRoomResearchServicesContent);
	}

	/**
	 * @return the asSearchRoomResearchServicesLang
	 */
	public List<List<String>> getAsSearchRoomResearchServicesLang() {
		if (this.asSearchRoomResearchServicesLang == null) {
			this.asSearchRoomResearchServicesLang = new ArrayList<List<String>>();
		}
		return this.asSearchRoomResearchServicesLang;
	}

	/**
	 * @param asSearchRoomResearchServicesLang the asSearchRoomResearchServicesLang to set
	 */
	public void setAsSearchRoomResearchServicesLang(
			List<List<String>> asSearchRoomResearchServicesLang) {
		this.asSearchRoomResearchServicesLang = asSearchRoomResearchServicesLang;
	}

	/**
	 * @param asSearchRoomResearchServicesLang the asSearchRoomResearchServicesLang to add
	 */
	public void addAsSearchRoomResearchServicesLang(List<String> asSearchRoomResearchServicesLang) {
		this.getAsSearchRoomResearchServicesLang().add(asSearchRoomResearchServicesLang);
	}

	/**
	 * @return the asLibraryQuestion
	 */
	public List<List<String>> getAsLibraryQuestion() {
		if (this.asLibraryQuestion == null) {
			this.asLibraryQuestion = new ArrayList<List<String>>();
		}
		return this.asLibraryQuestion;
	}

	/**
	 * @param asLibraryQuestion the asLibraryQuestion to set
	 */
	public void setAsLibraryQuestion(List<List<String>> asLibraryQuestion) {
		this.asLibraryQuestion = asLibraryQuestion;
	}

	/**
	 * @param asLibraryQuestion the asLibraryQuestion to add
	 */
	public void addAsLibraryQuestion(List<String> asLibraryQuestion) {
		this.getAsLibraryQuestion().add(asLibraryQuestion);
	}

	/**
	 * @return the asLibraryTelephone
	 */
	public List<List<String>> getAsLibraryTelephone() {
		if (this.asLibraryTelephone == null) {
			this.asLibraryTelephone = new ArrayList<List<String>>();
		}
		return this.asLibraryTelephone;
	}

	/**
	 * @param asLibraryTelephone the asLibraryTelephone to set
	 */
	public void setAsLibraryTelephone(List<List<String>> asLibraryTelephone) {
		this.asLibraryTelephone = asLibraryTelephone;
	}

	/**
	 * @param asLibraryTelephone the asLibraryTelephone to add
	 */
	public void addAsLibraryTelephone(List<String> asLibraryTelephone) {
		this.getAsLibraryTelephone().add(asLibraryTelephone);
	}

	/**
	 * @return the asLibraryNumberOfEmail
	 */
	public List<List<String>> getAsLibraryNumberOfEmail() {
		if (this.asLibraryNumberOfEmail == null) {
			this.asLibraryNumberOfEmail = new ArrayList<List<String>>();
		}
		return this.asLibraryNumberOfEmail;
	}

	/**
	 * @param asLibraryNumberOfEmail the asLibraryNumberOfEmail to set
	 */
	public void setAsLibraryNumberOfEmail(
			List<List<String>> asLibraryNumberOfEmail) {
		this.asLibraryNumberOfEmail = asLibraryNumberOfEmail;
	}

	/**
	 * @param asLibraryNumberOfEmail the asLibraryNumberOfEmail to add
	 */
	public void addAsLibraryNumberOfEmail(List<String> asLibraryNumberOfEmail) {
		this.getAsLibraryNumberOfEmail().add(asLibraryNumberOfEmail);
	}

	/**
	 * @return the asLibraryEmailHref
	 */
	public List<List<String>> getAsLibraryEmailHref() {
		if (this.asLibraryEmailHref == null) {
			this.asLibraryEmailHref = new ArrayList<List<String>>();
		}
		return this.asLibraryEmailHref;
	}

	/**
	 * @param asLibraryEmailHref the asLibraryEmailHref to set
	 */
	public void setAsLibraryEmailHref(List<List<String>> asLibraryEmailHref) {
		this.asLibraryEmailHref = asLibraryEmailHref;
	}

	/**
	 * @param asLibraryEmailHref the asLibraryEmailHref to add
	 */
	public void addAsLibraryEmailHref(List<String> asLibraryEmailHref) {
		this.getAsLibraryEmailHref().add(asLibraryEmailHref);
	}

	/**
	 * @return the asLibraryEmailTitle
	 */
	public List<List<String>> getAsLibraryEmailTitle() {
		if (this.asLibraryEmailTitle == null) {
			this.asLibraryEmailTitle = new ArrayList<List<String>>();
		}
		return this.asLibraryEmailTitle;
	}

	/**
	 * @param asLibraryEmailTitle the asLibraryEmailTitle to set
	 */
	public void setAsLibraryEmailTitle(List<List<String>> asLibraryEmailTitle) {
		this.asLibraryEmailTitle = asLibraryEmailTitle;
	}

	/**
	 * @param asLibraryEmailTitle the asLibraryEmailTitle to add
	 */
	public void addAsLibraryEmailTitle(List<String> asLibraryEmailTitle) {
		this.getAsLibraryEmailTitle().add(asLibraryEmailTitle);
	}

	/**
	 * @return the asLibraryEmailLang
	 */
	public List<List<String>> getAsLibraryEmailLang() {
		if (this.asLibraryEmailLang == null) {
			this.asLibraryEmailLang = new ArrayList<List<String>>();
		}
		return this.asLibraryEmailLang;
	}

	/**
	 * @param asLibraryEmailLang the asLibraryEmailLang to set
	 */
	public void setAsLibraryEmailLang(List<List<String>> asLibraryEmailLang) {
		this.asLibraryEmailLang = asLibraryEmailLang;
	}

	/**
	 * @param asLibraryEmailLang the asLibraryEmailLang to add
	 */
	public void addAsLibraryEmailLang(List<String> asLibraryEmailLang) {
		this.getAsLibraryEmailLang().add(asLibraryEmailLang);
	}

	/**
	 * @return the asLibraryNumberOfWebpage
	 */
	public List<List<String>> getAsLibraryNumberOfWebpage() {
		if (this.asLibraryNumberOfWebpage == null) {
			this.asLibraryNumberOfWebpage = new ArrayList<List<String>>();
		}
		return this.asLibraryNumberOfWebpage;
	}

	/**
	 * @param asLibraryNumberOfWebpage the asLibraryNumberOfWebpage to set
	 */
	public void setAsLibraryNumberOfWebpage(
			List<List<String>> asLibraryNumberOfWebpage) {
		this.asLibraryNumberOfWebpage = asLibraryNumberOfWebpage;
	}

	/**
	 * @param asLibraryNumberOfWebpage the asLibraryNumberOfWebpage to add
	 */
	public void addAsLibraryNumberOfWebpage(List<String> asLibraryNumberOfWebpage) {
		this.getAsLibraryNumberOfWebpage().add(asLibraryNumberOfWebpage);
	}

	/**
	 * @return the asLibraryWebpageHref
	 */
	public List<List<String>> getAsLibraryWebpageHref() {
		if (this.asLibraryWebpageHref == null) {
			this.asLibraryWebpageHref = new ArrayList<List<String>>();
		}
		return this.asLibraryWebpageHref;
	}

	/**
	 * @param asLibraryWebpageHref the asLibraryWebpageHref to set
	 */
	public void setAsLibraryWebpageHref(List<List<String>> asLibraryWebpageHref) {
		this.asLibraryWebpageHref = asLibraryWebpageHref;
	}

	/**
	 * @param asLibraryWebpageHref the asLibraryWebpageHref to add
	 */
	public void addAsLibraryWebpageHref(List<String> asLibraryWebpageHref) {
		this.getAsLibraryWebpageHref().add(asLibraryWebpageHref);
	}

	/**
	 * @return the asLibraryWebpageTitle
	 */
	public List<List<String>> getAsLibraryWebpageTitle() {
		if (this.asLibraryWebpageTitle == null) {
			this.asLibraryWebpageTitle = new ArrayList<List<String>>();
		}
		return this.asLibraryWebpageTitle;
	}

	/**
	 * @param asLibraryWebpageTitle the asLibraryWebpageTitle to set
	 */
	public void setAsLibraryWebpageTitle(List<List<String>> asLibraryWebpageTitle) {
		this.asLibraryWebpageTitle = asLibraryWebpageTitle;
	}

	/**
	 * @param asLibraryWebpageTitle the asLibraryWebpageTitle to add
	 */
	public void addAsLibraryWebpageTitle(List<String> asLibraryWebpageTitle) {
		this.getAsLibraryWebpageTitle().add(asLibraryWebpageTitle);
	}

	/**
	 * @return the asLibraryWebpageLang
	 */
	public List<List<String>> getAsLibraryWebpageLang() {
		if (this.asLibraryWebpageLang == null) {
			this.asLibraryWebpageLang = new ArrayList<List<String>>();
		}
		return this.asLibraryWebpageLang;
	}

	/**
	 * @param asLibraryWebpageLang the asLibraryWebpageLang to set
	 */
	public void setAsLibraryWebpageLang(List<List<String>> asLibraryWebpageLang) {
		this.asLibraryWebpageLang = asLibraryWebpageLang;
	}

	/**
	 * @param asLibraryWebpageLang the asLibraryWebpageLang to add
	 */
	public void addAsLibraryWebpageLang(List<String> asLibraryWebpageLang) {
		this.getAsLibraryWebpageLang().add(asLibraryWebpageLang);
	}

	/**
	 * @return the asLibraryMonographPublication
	 */
	public List<List<String>> getAsLibraryMonographPublication() {
		if (this.asLibraryMonographPublication == null) {
			this.asLibraryMonographPublication = new ArrayList<List<String>>();
		}
		return this.asLibraryMonographPublication;
	}

	/**
	 * @param asLibraryMonographPublication the asLibraryMonographPublication to set
	 */
	public void setAsLibraryMonographPublication(
			List<List<String>> asLibraryMonographPublication) {
		this.asLibraryMonographPublication = asLibraryMonographPublication;
	}

	/**
	 * @param asLibraryMonographPublication the asLibraryMonographPublication to add
	 */
	public void addAsLibraryMonographPublication(List<String> asLibraryMonographPublication) {
		this.getAsLibraryMonographPublication().add(asLibraryMonographPublication);
	}

	/**
	 * @return the asLibrarySerialPublication
	 */
	public List<List<String>> getAsLibrarySerialPublication() {
		if (this.asLibrarySerialPublication == null) {
			this.asLibrarySerialPublication = new ArrayList<List<String>>();
		}
		return this.asLibrarySerialPublication;
	}

	/**
	 * @param asLibrarySerialPublication the asLibrarySerialPublication to set
	 */
	public void setAsLibrarySerialPublication(
			List<List<String>> asLibrarySerialPublication) {
		this.asLibrarySerialPublication = asLibrarySerialPublication;
	}

	/**
	 * @param asLibrarySerialPublication the asLibrarySerialPublication to add
	 */
	public void addAsLibrarySerialPublication(List<String> asLibrarySerialPublication) {
		this.getAsLibrarySerialPublication().add(asLibrarySerialPublication);
	}

	/**
	 * @return the asInternetAccessQuestion
	 */
	public List<List<String>> getAsInternetAccessQuestion() {
		if (this.asInternetAccessQuestion == null) {
			this.asInternetAccessQuestion = new ArrayList<List<String>>();
		}
		return this.asInternetAccessQuestion;
	}

	/**
	 * @param asInternetAccessQuestion the asInternetAccessQuestion to set
	 */
	public void setAsInternetAccessQuestion(
			List<List<String>> asInternetAccessQuestion) {
		this.asInternetAccessQuestion = asInternetAccessQuestion;
	}

	/**
	 * @param asInternetAccessQuestion the asInternetAccessQuestion to add
	 */
	public void addAsInternetAccessQuestion(List<String> asInternetAccessQuestion) {
		this.getAsInternetAccessQuestion().add(asInternetAccessQuestion);
	}

	/**
	 * @return the asInternetAccessDescription
	 */
	public List<List<String>> getAsInternetAccessDescription() {
		if (this.asInternetAccessDescription == null) {
			this.asInternetAccessDescription = new ArrayList<List<String>>();
		}
		return this.asInternetAccessDescription;
	}

	/**
	 * @param asInternetAccessDescription the asInternetAccessDescription to set
	 */
	public void setAsInternetAccessDescription(
			List<List<String>> asInternetAccessDescription) {
		this.asInternetAccessDescription = asInternetAccessDescription;
	}

	/**
	 * @param asInternetAccessDescription the asInternetAccessDescription to add
	 */
	public void addAsInternetAccessDescription(List<String> asInternetAccessDescription) {
		this.getAsInternetAccessDescription().add(asInternetAccessDescription);
	}

	/**
	 * @return the asInternetAccessDescriptionLang
	 */
	public List<List<String>> getAsInternetAccessDescriptionLang() {
		if (this.asInternetAccessDescriptionLang == null) {
			this.asInternetAccessDescriptionLang = new ArrayList<List<String>>();
		}
		return this.asInternetAccessDescriptionLang;
	}

	/**
	 * @param asInternetAccessDescriptionLang the asInternetAccessDescriptionLang to set
	 */
	public void setAsInternetAccessDescriptionLang(
			List<List<String>> asInternetAccessDescriptionLang) {
		this.asInternetAccessDescriptionLang = asInternetAccessDescriptionLang;
	}

	/**
	 * @param asInternetAccessDescriptionLang the asInternetAccessDescriptionLang to add
	 */
	public void addAsInternetAccessDescriptionLang(List<String> asInternetAccessDescriptionLang) {
		this.getAsInternetAccessDescriptionLang().add(asInternetAccessDescriptionLang);
	}

	/**
	 * @return the asRestorationlabQuestion
	 */
	public List<List<String>> getAsRestorationlabQuestion() {
		if (this.asRestorationlabQuestion == null) {
			this.asRestorationlabQuestion = new ArrayList<List<String>>();
		}
		return this.asRestorationlabQuestion;
	}

	/**
	 * @param asRestorationlabQuestion the asRestorationlabQuestion to set
	 */
	public void setAsRestorationlabQuestion(
			List<List<String>> asRestorationlabQuestion) {
		this.asRestorationlabQuestion = asRestorationlabQuestion;
	}

	/**
	 * @param asRestorationlabQuestion the asRestorationlabQuestion to add
	 */
	public void addAsRestorationlabQuestion(List<String> asRestorationlabQuestion) {
		this.getAsRestorationlabQuestion().add(asRestorationlabQuestion);
	}

	/**
	 * @return the asRestorationlabDescription
	 */
	public List<List<String>> getAsRestorationlabDescription() {
		if (this.asRestorationlabDescription == null) {
			this.asRestorationlabDescription = new ArrayList<List<String>>();
		}
		return this.asRestorationlabDescription;
	}

	/**
	 * @param asRestorationlabDescription the asRestorationlabDescription to set
	 */
	public void setAsRestorationlabDescription(
			List<List<String>> asRestorationlabDescription) {
		this.asRestorationlabDescription = asRestorationlabDescription;
	}

	/**
	 * @param asRestorationlabDescription the asRestorationlabDescription to add
	 */
	public void addAsRestorationlabDescription(List<String> asRestorationlabDescription) {
		this.getAsRestorationlabDescription().add(asRestorationlabDescription);
	}

	/**
	 * @return the asRestorationlabDescriptionLang
	 */
	public List<List<String>> getAsRestorationlabDescriptionLang() {
		if (this.asRestorationlabDescriptionLang == null) {
			this.asRestorationlabDescriptionLang = new ArrayList<List<String>>();
		}
		return this.asRestorationlabDescriptionLang;
	}

	/**
	 * @param asRestorationlabDescriptionLang the asRestorationlabDescriptionLang to set
	 */
	public void setAsRestorationlabDescriptionLang(
			List<List<String>> asRestorationlabDescriptionLang) {
		this.asRestorationlabDescriptionLang = asRestorationlabDescriptionLang;
	}

	/**
	 * @param asRestorationlabDescriptionLang the asRestorationlabDescriptionLang to add
	 */
	public void addAsRestorationlabDescriptionLang(List<String> asRestorationlabDescriptionLang) {
		this.getAsRestorationlabDescriptionLang().add(asRestorationlabDescriptionLang);
	}

	/**
	 * @return the asRestorationlabTelephone
	 */
	public List<List<String>> getAsRestorationlabTelephone() {
		if (this.asRestorationlabTelephone == null) {
			this.asRestorationlabTelephone = new ArrayList<List<String>>();
		}
		return this.asRestorationlabTelephone;
	}

	/**
	 * @param asRestorationlabTelephone the asRestorationlabTelephone to set
	 */
	public void setAsRestorationlabTelephone(
			List<List<String>> asRestorationlabTelephone) {
		this.asRestorationlabTelephone = asRestorationlabTelephone;
	}

	/**
	 * @param asRestorationlabTelephone the asRestorationlabTelephone to add
	 */
	public void addAsRestorationlabTelephone(List<String> asRestorationlabTelephone) {
		this.getAsRestorationlabTelephone().add(asRestorationlabTelephone);
	}

	/**
	 * @return the asRestorationlabNumberOfEmail
	 */
	public List<List<String>> getAsRestorationlabNumberOfEmail() {
		if (this.asRestorationlabNumberOfEmail == null) {
			this.asRestorationlabNumberOfEmail = new ArrayList<List<String>>();
		}
		return this.asRestorationlabNumberOfEmail;
	}

	/**
	 * @param asRestorationlabNumberOfEmail the asRestorationlabNumberOfEmail to set
	 */
	public void setAsRestorationlabNumberOfEmail(
			List<List<String>> asRestorationlabNumberOfEmail) {
		this.asRestorationlabNumberOfEmail = asRestorationlabNumberOfEmail;
	}

	/**
	 * @param asRestorationlabNumberOfEmail the asRestorationlabNumberOfEmail to add
	 */
	public void addAsRestorationlabNumberOfEmail(List<String> asRestorationlabNumberOfEmail) {
		this.getAsRestorationlabNumberOfEmail().add(asRestorationlabNumberOfEmail);
	}

	/**
	 * @return the asRestorationlabEmailHref
	 */
	public List<List<String>> getAsRestorationlabEmailHref() {
		if (this.asRestorationlabEmailHref == null) {
			this.asRestorationlabEmailHref = new ArrayList<List<String>>();
		}
		return this.asRestorationlabEmailHref;
	}

	/**
	 * @param asRestorationlabEmailHref the asRestorationlabEmailHref to set
	 */
	public void setAsRestorationlabEmailHref(
			List<List<String>> asRestorationlabEmailHref) {
		this.asRestorationlabEmailHref = asRestorationlabEmailHref;
	}

	/**
	 * @param asRestorationlabEmailHref the asRestorationlabEmailHref to add
	 */
	public void addAsRestorationlabEmailHref(List<String> asRestorationlabEmailHref) {
		this.getAsRestorationlabEmailHref().add(asRestorationlabEmailHref);
	}

	/**
	 * @return the asRestorationlabEmailTitle
	 */
	public List<List<String>> getAsRestorationlabEmailTitle() {
		if (this.asRestorationlabEmailTitle == null) {
			this.asRestorationlabEmailTitle = new ArrayList<List<String>>();
		}
		return this.asRestorationlabEmailTitle;
	}

	/**
	 * @param asRestorationlabEmailTitle the asRestorationlabEmailTitle to set
	 */
	public void setAsRestorationlabEmailTitle(
			List<List<String>> asRestorationlabEmailTitle) {
		this.asRestorationlabEmailTitle = asRestorationlabEmailTitle;
	}

	/**
	 * @param asRestorationlabEmailTitle the asRestorationlabEmailTitle to add
	 */
	public void addAsRestorationlabEmailTitle(List<String> asRestorationlabEmailTitle) {
		this.getAsRestorationlabEmailTitle().add(asRestorationlabEmailTitle);
	}

	/**
	 * @return the asRestorationlabEmailLang
	 */
	public List<List<String>> getAsRestorationlabEmailLang() {
		if (this.asRestorationlabEmailLang == null) {
			this.asRestorationlabEmailLang = new ArrayList<List<String>>();
		}
		return this.asRestorationlabEmailLang;
	}

	/**
	 * @param asRestorationlabEmailLang the asRestorationlabEmailLang to set
	 */
	public void setAsRestorationlabEmailLang(
			List<List<String>> asRestorationlabEmailLang) {
		this.asRestorationlabEmailLang = asRestorationlabEmailLang;
	}

	/**
	 * @param asRestorationlabEmailLang the asRestorationlabEmailLang to add
	 */
	public void addAsRestorationlabEmailLang(List<String> asRestorationlabEmailLang) {
		this.getAsRestorationlabEmailLang().add(asRestorationlabEmailLang);
	}

	/**
	 * @return the asRestorationlabNumberOfWebpage
	 */
	public List<List<String>> getAsRestorationlabNumberOfWebpage() {
		if (this.asRestorationlabNumberOfWebpage == null) {
			this.asRestorationlabNumberOfWebpage = new ArrayList<List<String>>();
		}
		return this.asRestorationlabNumberOfWebpage;
	}

	/**
	 * @param asRestorationlabNumberOfWebpage the asRestorationlabNumberOfWebpage to set
	 */
	public void setAsRestorationlabNumberOfWebpage(
			List<List<String>> asRestorationlabNumberOfWebpage) {
		this.asRestorationlabNumberOfWebpage = asRestorationlabNumberOfWebpage;
	}

	/**
	 * @param asRestorationlabNumberOfWebpage the asRestorationlabNumberOfWebpage to add
	 */
	public void addAsRestorationlabNumberOfWebpage(List<String> asRestorationlabNumberOfWebpage) {
		this.getAsRestorationlabNumberOfWebpage().add(asRestorationlabNumberOfWebpage);
	}

	/**
	 * @return the asRestorationlabWebpageHref
	 */
	public List<List<String>> getAsRestorationlabWebpageHref() {
		if (this.asRestorationlabWebpageHref == null) {
			this.asRestorationlabWebpageHref = new ArrayList<List<String>>();
		}
		return this.asRestorationlabWebpageHref;
	}

	/**
	 * @param asRestorationlabWebpageHref the asRestorationlabWebpageHref to set
	 */
	public void setAsRestorationlabWebpageHref(
			List<List<String>> asRestorationlabWebpageHref) {
		this.asRestorationlabWebpageHref = asRestorationlabWebpageHref;
	}

	/**
	 * @param asRestorationlabWebpageHref the asRestorationlabWebpageHref to add
	 */
	public void addAsRestorationlabWebpageHref(List<String> asRestorationlabWebpageHref) {
		this.getAsRestorationlabWebpageHref().add(asRestorationlabWebpageHref);
	}

	/**
	 * @return the asRestorationlabWebpageTitle
	 */
	public List<List<String>> getAsRestorationlabWebpageTitle() {
		if (this.asRestorationlabWebpageTitle == null) {
			this.asRestorationlabWebpageTitle = new ArrayList<List<String>>();
		}
		return this.asRestorationlabWebpageTitle;
	}

	/**
	 * @param asRestorationlabWebpageTitle the asRestorationlabWebpageTitle to set
	 */
	public void setAsRestorationlabWebpageTitle(
			List<List<String>> asRestorationlabWebpageTitle) {
		this.asRestorationlabWebpageTitle = asRestorationlabWebpageTitle;
	}

	/**
	 * @param asRestorationlabWebpageTitle the asRestorationlabWebpageTitle to add
	 */
	public void addAsRestorationlabWebpageTitle(List<String> asRestorationlabWebpageTitle) {
		this.getAsRestorationlabWebpageTitle().add(asRestorationlabWebpageTitle);
	}

	/**
	 * @return the asRestorationlabWebpageLang
	 */
	public List<List<String>> getAsRestorationlabWebpageLang() {
		if (this.asRestorationlabWebpageLang == null) {
			this.asRestorationlabWebpageLang = new ArrayList<List<String>>();
		}
		return this.asRestorationlabWebpageLang;
	}

	/**
	 * @param asRestorationlabWebpageLang the asRestorationlabWebpageLang to set
	 */
	public void setAsRestorationlabWebpageLang(
			List<List<String>> asRestorationlabWebpageLang) {
		this.asRestorationlabWebpageLang = asRestorationlabWebpageLang;
	}

	/**
	 * @param asRestorationlabWebpageLang the asRestorationlabWebpageLang to add
	 */
	public void addAsRestorationlabWebpageLang(List<String> asRestorationlabWebpageLang) {
		this.getAsRestorationlabWebpageLang().add(asRestorationlabWebpageLang);
	}

	/**
	 * @return the asReproductionserQuestion
	 */
	public List<List<String>> getAsReproductionserQuestion() {
		if (this.asReproductionserQuestion == null) {
			this.asReproductionserQuestion = new ArrayList<List<String>>();
		}
		return this.asReproductionserQuestion;
	}

	/**
	 * @param asReproductionserQuestion the asReproductionserQuestion to set
	 */
	public void setAsReproductionserQuestion(
			List<List<String>> asReproductionserQuestion) {
		this.asReproductionserQuestion = asReproductionserQuestion;
	}

	/**
	 * @param asReproductionserQuestion the asReproductionserQuestion to add
	 */
	public void addAsReproductionserQuestion(List<String> asReproductionserQuestion) {
		this.getAsReproductionserQuestion().add(asReproductionserQuestion);
	}

	/**
	 * @return the asReproductionserDescription
	 */
	public List<List<String>> getAsReproductionserDescription() {
		if (this.asReproductionserDescription == null) {
			this.asReproductionserDescription = new ArrayList<List<String>>();
		}
		return this.asReproductionserDescription;
	}

	/**
	 * @param asReproductionserDescription the asReproductionserDescription to set
	 */
	public void setAsReproductionserDescription(
			List<List<String>> asReproductionserDescription) {
		this.asReproductionserDescription = asReproductionserDescription;
	}

	/**
	 * @param asReproductionserDescription the asReproductionserDescription to add
	 */
	public void addAsReproductionserDescription(List<String> asReproductionserDescription) {
		this.getAsReproductionserDescription().add(asReproductionserDescription);
	}

	/**
	 * @return the asReproductionserDescriptionLang
	 */
	public List<List<String>> getAsReproductionserDescriptionLang() {
		if (this.asReproductionserDescriptionLang == null) {
			this.asReproductionserDescriptionLang = new ArrayList<List<String>>();
		}
		return this.asReproductionserDescriptionLang;
	}

	/**
	 * @param asReproductionserDescriptionLang the asReproductionserDescriptionLang to set
	 */
	public void setAsReproductionserDescriptionLang(
			List<List<String>> asReproductionserDescriptionLang) {
		this.asReproductionserDescriptionLang = asReproductionserDescriptionLang;
	}

	/**
	 * @param asReproductionserDescriptionLang the asReproductionserDescriptionLang to add
	 */
	public void addAsReproductionserDescriptionLang(List<String> asReproductionserDescriptionLang) {
		this.getAsReproductionserDescriptionLang().add(asReproductionserDescriptionLang);
	}

	/**
	 * @return the asReproductionserTelephone
	 */
	public List<List<String>> getAsReproductionserTelephone() {
		if (this.asReproductionserTelephone == null) {
			this.asReproductionserTelephone = new ArrayList<List<String>>();
		}
		return this.asReproductionserTelephone;
	}

	/**
	 * @param asReproductionserTelephone the asReproductionserTelephone to set
	 */
	public void setAsReproductionserTelephone(
			List<List<String>> asReproductionserTelephone) {
		this.asReproductionserTelephone = asReproductionserTelephone;
	}

	/**
	 * @param asReproductionserTelephone the asReproductionserTelephone to add
	 */
	public void addAsReproductionserTelephone(List<String> asReproductionserTelephone) {
		this.getAsReproductionserTelephone().add(asReproductionserTelephone);
	}

	/**
	 * @return the asReproductionserNumberOfEmail
	 */
	public List<List<String>> getAsReproductionserNumberOfEmail() {
		if (this.asReproductionserNumberOfEmail == null) {
			this.asReproductionserNumberOfEmail = new ArrayList<List<String>>();
		}
		return this.asReproductionserNumberOfEmail;
	}

	/**
	 * @param asReproductionserNumberOfEmail the asReproductionserNumberOfEmail to set
	 */
	public void setAsReproductionserNumberOfEmail(
			List<List<String>> asReproductionserNumberOfEmail) {
		this.asRestorationlabNumberOfEmail = asReproductionserNumberOfEmail;
	}

	/**
	 * @param asReproductionserNumberOfEmail the asReproductionserNumberOfEmail to add
	 */
	public void addAsReproductionserNumberOfEmail(List<String> asReproductionserNumberOfEmail) {
		this.getAsReproductionserNumberOfEmail().add(asReproductionserNumberOfEmail);
	}

	/**
	 * @return the asReproductionserEmailHref
	 */
	public List<List<String>> getAsReproductionserEmailHref() {
		if (this.asReproductionserEmailHref == null) {
			this.asReproductionserEmailHref = new ArrayList<List<String>>();
		}
		return this.asReproductionserEmailHref;
	}

	/**
	 * @param asReproductionserEmailHref the asReproductionserEmailHref to set
	 */
	public void setAsReproductionserEmailHref(
			List<List<String>> asReproductionserEmailHref) {
		this.asReproductionserEmailHref = asReproductionserEmailHref;
	}

	/**
	 * @param asReproductionserEmailHref the asReproductionserEmailHref to add
	 */
	public void addAsReproductionserEmailHref(List<String> asReproductionserEmailHref) {
		this.getAsReproductionserEmailHref().add(asReproductionserEmailHref);
	}

	/**
	 * @return the asReproductionserEmailTitle
	 */
	public List<List<String>> getAsReproductionserEmailTitle() {
		if (this.asReproductionserEmailTitle == null) {
			this.asReproductionserEmailTitle = new ArrayList<List<String>>();
		}
		return this.asReproductionserEmailTitle;
	}

	/**
	 * @param asReproductionserEmailTitle the asReproductionserEmailTitle to set
	 */
	public void setAsReproductionserEmailTitle(
			List<List<String>> asReproductionserEmailTitle) {
		this.asReproductionserEmailTitle = asReproductionserEmailTitle;
	}

	/**
	 * @param asReproductionserEmailTitle the asReproductionserEmailTitle to add
	 */
	public void addAsReproductionserEmailTitle(List<String> asReproductionserEmailTitle) {
		this.getAsReproductionserEmailTitle().add(asReproductionserEmailTitle);
	}

	/**
	 * @return the asReproductionserEmailLang
	 */
	public List<List<String>> getAsReproductionserEmailLang() {
		if (this.asReproductionserEmailLang == null) {
			this.asReproductionserEmailLang = new ArrayList<List<String>>();
		}
		return this.asReproductionserEmailLang;
	}

	/**
	 * @param asReproductionserEmailLang the asReproductionserEmailLang to set
	 */
	public void setAsReproductionserEmailLang(
			List<List<String>> asReproductionserEmailLang) {
		this.asReproductionserEmailLang = asReproductionserEmailLang;
	}

	/**
	 * @param asReproductionserEmailLang the asReproductionserEmailLang to add
	 */
	public void addAsReproductionserEmailLang(List<String> asReproductionserEmailLang) {
		this.getAsReproductionserEmailLang().add(asReproductionserEmailLang);
	}

	/**
	 * @return the asReproductionserNumberOfWebpage
	 */
	public List<List<String>> getAsReproductionserNumberOfWebpage() {
		if (this.asReproductionserNumberOfWebpage == null) {
			this.asReproductionserNumberOfWebpage = new ArrayList<List<String>>();
		}
		return this.asReproductionserNumberOfWebpage;
	}

	/**
	 * @param asReproductionserNumberOfWebpage the asReproductionserNumberOfWebpage to set
	 */
	public void setAsReproductionserNumberOfWebpage(
			List<List<String>> asReproductionserNumberOfWebpage) {
		this.asReproductionserNumberOfWebpage = asReproductionserNumberOfWebpage;
	}

	/**
	 * @param asReproductionserNumberOfWebpage the asReproductionserNumberOfWebpage to add
	 */
	public void addAsReproductionserNumberOfWebpage(List<String> asReproductionserNumberOfWebpage) {
		this.getAsReproductionserNumberOfWebpage().add(asReproductionserNumberOfWebpage);
	}

	/**
	 * @return the asReproductionserWebpageHref
	 */
	public List<List<String>> getAsReproductionserWebpageHref() {
		if (this.asReproductionserWebpageHref == null) {
			this.asReproductionserWebpageHref = new ArrayList<List<String>>();
		}
		return this.asReproductionserWebpageHref;
	}

	/**
	 * @param asReproductionserWebpageHref the asReproductionserWebpageHref to set
	 */
	public void setAsReproductionserWebpageHref(
			List<List<String>> asReproductionserWebpageHref) {
		this.asReproductionserWebpageHref = asReproductionserWebpageHref;
	}

	/**
	 * @param asReproductionserWebpageHref the asReproductionserWebpageHref to add
	 */
	public void addAsReproductionserWebpageHref(List<String> asReproductionserWebpageHref) {
		this.getAsReproductionserWebpageHref().add(asReproductionserWebpageHref);
	}

	/**
	 * @return the asReproductionserWebpageTitle
	 */
	public List<List<String>> getAsReproductionserWebpageTitle() {
		if (this.asReproductionserWebpageTitle == null) {
			this.asReproductionserWebpageTitle = new ArrayList<List<String>>();
		}
		return this.asReproductionserWebpageTitle;
	}

	/**
	 * @param asReproductionserWebpageTitle the asReproductionserWebpageTitle to set
	 */
	public void setAsReproductionserWebpageTitle(
			List<List<String>> asReproductionserWebpageTitle) {
		this.asReproductionserWebpageTitle = asReproductionserWebpageTitle;
	}

	/**
	 * @param asReproductionserWebpageTitle the asReproductionserWebpageTitle to add
	 */
	public void addAsReproductionserWebpageTitle(List<String> asReproductionserWebpageTitle) {
		this.getAsReproductionserWebpageTitle().add(asReproductionserWebpageTitle);
	}

	/**
	 * @return the asReproductionserWebpageLang
	 */
	public List<List<String>> getAsReproductionserWebpageLang() {
		if (this.asReproductionserWebpageLang == null) {
			this.asReproductionserWebpageLang = new ArrayList<List<String>>();
		}
		return this.asReproductionserWebpageLang;
	}

	/**
	 * @param asReproductionserWebpageLang the asReproductionserWebpageLang to set
	 */
	public void setAsReproductionserWebpageLang(
			List<List<String>> asReproductionserWebpageLang) {
		this.asReproductionserWebpageLang = asReproductionserWebpageLang;
	}

	/**
	 * @param asReproductionserWebpageLang the asReproductionserWebpageLang to add
	 */
	public void addAsReproductionserWebpageLang(List<String> asReproductionserWebpageLang) {
		this.getAsReproductionserWebpageLang().add(asReproductionserWebpageLang);
	}

	/**
	 * @return the asReproductionserMicrofilmServices
	 */
	public List<List<String>> getAsReproductionserMicrofilmServices() {
		if (this.asReproductionserMicrofilmServices == null) {
			this.asReproductionserMicrofilmServices = new ArrayList<List<String>>();
		}
		return this.asReproductionserMicrofilmServices;
	}

	/**
	 * @param asReproductionserMicrofilmServices the asReproductionserMicrofilmServices to set
	 */
	public void setAsReproductionserMicrofilmServices(
			List<List<String>> asReproductionserMicrofilmServices) {
		this.asReproductionserMicrofilmServices = asReproductionserMicrofilmServices;
	}

	/**
	 * @param asReproductionserMicrofilmServices the asReproductionserMicrofilmServices to add
	 */
	public void addAsReproductionserMicrofilmServices(List<String> asReproductionserMicrofilmServices) {
		this.getAsReproductionserMicrofilmServices().add(asReproductionserMicrofilmServices);
	}

	/**
	 * @return the asReproductionserPhotographicServices
	 */
	public List<List<String>> getAsReproductionserPhotographicServices() {
		if (this.asReproductionserPhotographicServices == null) {
			this.asReproductionserPhotographicServices = new ArrayList<List<String>>();
		}
		return this.asReproductionserPhotographicServices;
	}

	/**
	 * @param asReproductionserPhotographicServices the asReproductionserPhotographicServices to set
	 */
	public void setAsReproductionserPhotographicServices(
			List<List<String>> asReproductionserPhotographicServices) {
		this.asReproductionserPhotographicServices = asReproductionserPhotographicServices;
	}

	/**
	 * @param asReproductionserPhotographicServices the asReproductionserPhotographicServices to add
	 */
	public void addAsReproductionserPhotographicServices(List<String> asReproductionserPhotographicServices) {
		this.getAsReproductionserPhotographicServices().add(asReproductionserPhotographicServices);
	}

	/**
	 * @return the asReproductionserDigitisationServices
	 */
	public List<List<String>> getAsReproductionserDigitisationServices() {
		if (this.asReproductionserDigitisationServices == null) {
			this.asReproductionserDigitisationServices = new ArrayList<List<String>>();
		}
		return this.asReproductionserDigitisationServices;
	}

	/**
	 * @param asReproductionserDigitisationServices the asReproductionserDigitisationServices to set
	 */
	public void setAsReproductionserDigitisationServices(
			List<List<String>> asReproductionserDigitisationServices) {
		this.asReproductionserDigitisationServices = asReproductionserDigitisationServices;
	}

	/**
	 * @param asReproductionserDigitisationServices the asReproductionserDigitisationServices to add
	 */
	public void addAsReproductionserDigitisationServices(List<String> asReproductionserDigitisationServices) {
		this.getAsReproductionserDigitisationServices().add(asReproductionserDigitisationServices);
	}

	/**
	 * @return the asReproductionserPhotocopyingServices
	 */
	public List<List<String>> getAsReproductionserPhotocopyingServices() {
		if (this.asReproductionserPhotocopyingServices == null) {
			this.asReproductionserPhotocopyingServices = new ArrayList<List<String>>();
		}
		return this.asReproductionserPhotocopyingServices;
	}

	/**
	 * @param asReproductionserPhotocopyingServices the asReproductionserPhotocopyingServices to set
	 */
	public void setAsReproductionserPhotocopyingServices(
			List<List<String>> asReproductionserPhotocopyingServices) {
		this.asReproductionserPhotocopyingServices = asReproductionserPhotocopyingServices;
	}

	/**
	 * @param asReproductionserPhotocopyingServices the asReproductionserPhotocopyingServices to add
	 */
	public void addAsReproductionserPhotocopyingServices(List<String> asReproductionserPhotocopyingServices) {
		this.getAsReproductionserPhotocopyingServices().add(asReproductionserPhotocopyingServices);
	}

	/**
	 * @return the asRecreationalServicesRefreshmentArea
	 */
	public List<List<String>> getAsRecreationalServicesRefreshmentArea() {
		if (this.asRecreationalServicesRefreshmentArea == null) {
			this.asRecreationalServicesRefreshmentArea = new ArrayList<List<String>>();
		}
		return this.asRecreationalServicesRefreshmentArea;
	}

	/**
	 * @param asRecreationalServicesRefreshmentArea the asRecreationalServicesRefreshmentArea to set
	 */
	public void setAsRecreationalServicesRefreshmentArea(
			List<List<String>> asRecreationalServicesRefreshmentArea) {
		this.asRecreationalServicesRefreshmentArea = asRecreationalServicesRefreshmentArea;
	}

	/**
	 * @param asRecreationalServicesRefreshmentArea the asRecreationalServicesRefreshmentArea to add
	 */
	public void addAsRecreationalServicesRefreshmentArea(List<String> asRecreationalServicesRefreshmentArea) {
		this.getAsRecreationalServicesRefreshmentArea().add(asRecreationalServicesRefreshmentArea);
	}

	/**
	 * @return the asRecreationalServicesRefreshmentAreaLang
	 */
	public List<List<String>> getAsRecreationalServicesRefreshmentAreaLang() {
		if (this.asRecreationalServicesRefreshmentAreaLang == null) {
			this.asRecreationalServicesRefreshmentAreaLang = new ArrayList<List<String>>();
		}
		return this.asRecreationalServicesRefreshmentAreaLang;
	}

	/**
	 * @param asRecreationalServicesRefreshmentAreaLang the asRecreationalServicesRefreshmentAreaLang to set
	 */
	public void setAsRecreationalServicesRefreshmentAreaLang(
			List<List<String>> asRecreationalServicesRefreshmentAreaLang) {
		this.asRecreationalServicesRefreshmentAreaLang = asRecreationalServicesRefreshmentAreaLang;
	}

	/**
	 * @param asRecreationalServicesRefreshmentAreaLang the asRecreationalServicesRefreshmentAreaLang to add
	 */
	public void addAsRecreationalServicesRefreshmentAreaLang(List<String> asRecreationalServicesRefreshmentAreaLang) {
		this.getAsRecreationalServicesRefreshmentAreaLang().add(asRecreationalServicesRefreshmentAreaLang);
	}

	/**
	 * @return the asRSNumberOfExhibition
	 */
	public List<List<String>> getAsRSNumberOfExhibition() {
		if (this.asRSNumberOfExhibition == null) {
			this.asRSNumberOfExhibition = new ArrayList<List<String>>();
		}
		return this.asRSNumberOfExhibition;
	}

	/**
	 * @param asRSNumberOfExhibition the asRSNumberOfExhibition to set
	 */
	public void setAsRSNumberOfExhibition(List<List<String>> asRSNumberOfExhibition) {
		this.asRSNumberOfExhibition = asRSNumberOfExhibition;
	}

	/**
	 * @param asRSNumberOfExhibition the asRSNumberOfExhibition to add
	 */
	public void addAsRSNumberOfExhibition(List<String> asRSNumberOfExhibition) {
		this.getAsRSNumberOfExhibition().add(asRSNumberOfExhibition);
	}

	/**
	 * @return the asRSExhibition
	 */
	public List<List<String>> getAsRSExhibition() {
		if (this.asRSExhibition == null) {
			this.asRSExhibition = new ArrayList<List<String>>();
		}
		return this.asRSExhibition;
	}

	/**
	 * @param asRSExhibition the asRSExhibition to set
	 */
	public void setAsRSExhibition(List<List<String>> asRSExhibition) {
		this.asRSExhibition = asRSExhibition;
	}

	/**
	 * @param asRSExhibition the asRSExhibition to add
	 */
	public void addAsRSExhibition(List<String> asRSExhibition) {
		this.getAsRSExhibition().add(asRSExhibition);
	}

	/**
	 * @return the asRSExhibitionLang
	 */
	public List<List<String>> getAsRSExhibitionLang() {
		if (this.asRSExhibitionLang == null) {
			this.asRSExhibitionLang = new ArrayList<List<String>>();
		}
		return this.asRSExhibitionLang;
	}

	/**
	 * @param asRSExhibitionLang the asRSExhibitionLang to set
	 */
	public void setAsRSExhibitionLang(List<List<String>> asRSExhibitionLang) {
		this.asRSExhibitionLang = asRSExhibitionLang;
	}

	/**
	 * @param asRSExhibitionLang the asRSExhibitionLang to add
	 */
	public void addAsRSExhibitionLang(List<String> asRSExhibitionLang) {
		this.getAsRSExhibitionLang().add(asRSExhibitionLang);
	}

	/**
	 * @return the asRSExhibitionWebpageHref
	 */
	public List<List<String>> getAsRSExhibitionWebpageHref() {
		if (this.asRSExhibitionWebpageHref == null) {
			this.asRSExhibitionWebpageHref = new ArrayList<List<String>>();
		}
		return this.asRSExhibitionWebpageHref;
	}

	/**
	 * @param asRSExhibitionWebpageHref the asRSExhibitionWebpageHref to set
	 */
	public void setAsRSExhibitionWebpageHref(
			List<List<String>> asRSExhibitionWebpageHref) {
		this.asRSExhibitionWebpageHref = asRSExhibitionWebpageHref;
	}

	/**
	 * @param asRSExhibitionWebpageHref the asRSExhibitionWebpageHref to add
	 */
	public void addAsRSExhibitionWebpageHref(List<String> asRSExhibitionWebpageHref) {
		this.getAsRSExhibitionWebpageHref().add(asRSExhibitionWebpageHref);
	}

	/**
	 * @return the asRSExhibitionWebpageTitle
	 */
	public List<List<String>> getAsRSExhibitionWebpageTitle() {
		if (this.asRSExhibitionWebpageTitle == null) {
			this.asRSExhibitionWebpageTitle = new ArrayList<List<String>>();
		}
		return this.asRSExhibitionWebpageTitle;
	}

	/**
	 * @param asRSExhibitionWebpageTitle the asRSExhibitionWebpageTitle to set
	 */
	public void setAsRSExhibitionWebpageTitle(
			List<List<String>> asRSExhibitionWebpageTitle) {
		this.asRSExhibitionWebpageTitle = asRSExhibitionWebpageTitle;
	}

	/**
	 * @param asRSExhibitionWebpageTitle the asRSExhibitionWebpageTitle to add
	 */
	public void addAsRSExhibitionWebpageTitle(List<String> asRSExhibitionWebpageTitle) {
		this.getAsRSExhibitionWebpageTitle().add(asRSExhibitionWebpageTitle);
	}

	/**
	 * @return the asRSExhibitionWebpageLang
	 */
	public List<List<String>> getAsRSExhibitionWebpageLang() {
		if (this.asRSExhibitionWebpageLang == null) {
			this.asRSExhibitionWebpageLang = new ArrayList<List<String>>();
		}
		return this.asRSExhibitionWebpageLang;
	}

	/**
	 * @param asRSExhibitionWebpageLang the asRSExhibitionWebpageLang to set
	 */
	public void setAsRSExhibitionWebpageLang(
			List<List<String>> asRSExhibitionWebpageLang) {
		this.asRSExhibitionWebpageLang = asRSExhibitionWebpageLang;
	}

	/**
	 * @param asRSExhibitionWebpageLang the asRSExhibitionWebpageLang to add
	 */
	public void addAsRSExhibitionWebpageLang(List<String> asRSExhibitionWebpageLang) {
		this.getAsRSExhibitionWebpageLang().add(asRSExhibitionWebpageLang);
	}

	/**
	 * @return the asRSNumberOfToursSessions
	 */
	public List<List<String>> getAsRSNumberOfToursSessions() {
		if (this.asRSNumberOfToursSessions == null) {
			this.asRSNumberOfToursSessions = new ArrayList<List<String>>();
		}
		return this.asRSNumberOfToursSessions;
	}

	/**
	 * @param asRSNumberOfToursSessions the asRSNumberOfToursSessions to set
	 */
	public void setAsRSNumberOfToursSessions(
			List<List<String>> asRSNumberOfToursSessions) {
		this.asRSNumberOfToursSessions = asRSNumberOfToursSessions;
	}

	/**
	 * @param asRSNumberOfToursSessions the asRSNumberOfToursSessions to add
	 */
	public void addAsRSNumberOfToursSessions(List<String> asRSNumberOfToursSessions) {
		this.getAsRSNumberOfToursSessions().add(asRSNumberOfToursSessions);
	}

	/**
	 * @return the asRSToursSessions
	 */
	public List<List<String>> getAsRSToursSessions() {
		if (this.asRSToursSessions == null) {
			this.asRSToursSessions = new ArrayList<List<String>>();
		}
		return this.asRSToursSessions;
	}

	/**
	 * @param asRSToursSessions the asRSToursSessions to set
	 */
	public void setAsRSToursSessions(List<List<String>> asRSToursSessions) {
		this.asRSToursSessions = asRSToursSessions;
	}

	/**
	 * @param asRSToursSessions the asRSToursSessions to add
	 */
	public void addAsRSToursSessions(List<String> asRSToursSessions) {
		this.getAsRSToursSessions().add(asRSToursSessions);
	}

	/**
	 * @return the asRSToursSessionsLang
	 */
	public List<List<String>> getAsRSToursSessionsLang() {
		if (this.asRSToursSessionsLang == null) {
			this.asRSToursSessionsLang = new ArrayList<List<String>>();
		}
		return this.asRSToursSessionsLang;
	}

	/**
	 * @param asRSToursSessionsLang the asRSToursSessionsLang to set
	 */
	public void setAsRSToursSessionsLang(List<List<String>> asRSToursSessionsLang) {
		this.asRSToursSessionsLang = asRSToursSessionsLang;
	}

	/**
	 * @param asRSToursSessionsLang the asRSToursSessionsLang to add
	 */
	public void addAsRSToursSessionsLang(List<String> asRSToursSessionsLang) {
		this.getAsRSToursSessionsLang().add(asRSToursSessionsLang);
	}

	/**
	 * @return the asRSToursSessionsWebpageHref
	 */
	public List<List<String>> getAsRSToursSessionsWebpageHref() {
		if (this.asRSToursSessionsWebpageHref == null) {
			this.asRSToursSessionsWebpageHref = new ArrayList<List<String>>();
		}
		return this.asRSToursSessionsWebpageHref;
	}

	/**
	 * @param asRSToursSessionsWebpageHref the asRSToursSessionsWebpageHref to set
	 */
	public void setAsRSToursSessionsWebpageHref(
			List<List<String>> asRSToursSessionsWebpageHref) {
		this.asRSToursSessionsWebpageHref = asRSToursSessionsWebpageHref;
	}

	/**
	 * @param asRSToursSessionsWebpageHref the asRSToursSessionsWebpageHref to add
	 */
	public void addAsRSToursSessionsWebpageHref(List<String> asRSToursSessionsWebpageHref) {
		this.getAsRSToursSessionsWebpageHref().add(asRSToursSessionsWebpageHref);
	}

	/**
	 * @return the asRSToursSessionsWebpageTitle
	 */
	public List<List<String>> getAsRSToursSessionsWebpageTitle() {
		if (this.asRSToursSessionsWebpageTitle == null) {
			this.asRSToursSessionsWebpageTitle = new ArrayList<List<String>>();
		}
		return this.asRSToursSessionsWebpageTitle;
	}

	/**
	 * @param asRSToursSessionsWebpageTitle the asRSToursSessionsWebpageTitle to set
	 */
	public void setAsRSToursSessionsWebpageTitle(
			List<List<String>> asRSToursSessionsWebpageTitle) {
		this.asRSToursSessionsWebpageTitle = asRSToursSessionsWebpageTitle;
	}

	/**
	 * @param asRSToursSessionsWebpageTitle the asRSToursSessionsWebpageTitle to add
	 */
	public void addAsRSToursSessionsWebpageTitle(List<String> asRSToursSessionsWebpageTitle) {
		this.getAsRSToursSessionsWebpageTitle().add(asRSToursSessionsWebpageTitle);
	}

	/**
	 * @return the asRSToursSessionsWebpageLang
	 */
	public List<List<String>> getAsRSToursSessionsWebpageLang() {
		if (this.asRSToursSessionsWebpageLang == null) {
			this.asRSToursSessionsWebpageLang = new ArrayList<List<String>>();
		}
		return this.asRSToursSessionsWebpageLang;
	}

	/**
	 * @param asRSToursSessionsWebpageLang the asRSToursSessionsWebpageLang to set
	 */
	public void setAsRSToursSessionsWebpageLang(
			List<List<String>> asRSToursSessionsWebpageLang) {
		this.asRSToursSessionsWebpageLang = asRSToursSessionsWebpageLang;
	}

	/**
	 * @param asRSToursSessionsWebpageLang the asRSToursSessionsWebpageLang to add
	 */
	public void addAsRSToursSessionsWebpageLang(List<String> asRSToursSessionsWebpageLang) {
		this.getAsRSToursSessionsWebpageLang().add(asRSToursSessionsWebpageLang);
	}

	/**
	 * @return the asRSNumberOfOtherServices
	 */
	public List<List<String>> getAsRSNumberOfOtherServices() {
		if (this.asRSNumberOfOtherServices == null) {
			this.asRSNumberOfOtherServices = new ArrayList<List<String>>();
		}
		return this.asRSNumberOfOtherServices;
	}

	/**
	 * @param asRSNumberOfOtherServices the asRSNumberOfOtherServices to set
	 */
	public void setAsRSNumberOfOtherServices(
			List<List<String>> asRSNumberOfOtherServices) {
		this.asRSNumberOfOtherServices = asRSNumberOfOtherServices;
	}

	/**
	 * @param asRSNumberOfOtherServices the asRSNumberOfOtherServices to add
	 */
	public void addAsRSNumberOfOtherServices(List<String> asRSNumberOfOtherServices) {
		this.getAsRSNumberOfOtherServices().add(asRSNumberOfOtherServices);
	}

	/**
	 * @return the asRSOtherServices
	 */
	public List<List<String>> getAsRSOtherServices() {
		if (this.asRSOtherServices == null) {
			this.asRSOtherServices = new ArrayList<List<String>>();
		}
		return this.asRSOtherServices;
	}

	/**
	 * @param asRSOtherServices the asRSOtherServices to set
	 */
	public void setAsRSOtherServices(List<List<String>> asRSOtherServices) {
		this.asRSOtherServices = asRSOtherServices;
	}

	/**
	 * @param asRSOtherServices the asRSOtherServices to add
	 */
	public void addAsRSOtherServices(List<String> asRSOtherServices) {
		this.getAsRSOtherServices().add(asRSOtherServices);
	}

	/**
	 * @return the asRSOtherServicesLang
	 */
	public List<List<String>> getAsRSOtherServicesLang() {
		if (this.asRSOtherServicesLang == null) {
			this.asRSOtherServicesLang = new ArrayList<List<String>>();
		}
		return this.asRSOtherServicesLang;
	}

	/**
	 * @param asRSOtherServicesLang the asRSOtherServicesLang to set
	 */
	public void setAsRSOtherServicesLang(List<List<String>> asRSOtherServicesLang) {
		this.asRSOtherServicesLang = asRSOtherServicesLang;
	}

	/**
	 * @param asRSOtherServicesLang the asRSOtherServicesLang to add
	 */
	public void addAsRSOtherServicesLang(List<String> asRSOtherServicesLang) {
		this.getAsRSOtherServicesLang().add(asRSOtherServicesLang);
	}

	/**
	 * @return the asRSOtherServicesWebpageHref
	 */
	public List<List<String>> getAsRSOtherServicesWebpageHref() {
		if (this.asRSOtherServicesWebpageHref == null) {
			this.asRSOtherServicesWebpageHref = new ArrayList<List<String>>();
		}
		return this.asRSOtherServicesWebpageHref;
	}

	/**
	 * @param asRSOtherServicesWebpageHref the asRSOtherServicesWebpageHref to set
	 */
	public void setAsRSOtherServicesWebpageHref(
			List<List<String>> asRSOtherServicesWebpageHref) {
		this.asRSOtherServicesWebpageHref = asRSOtherServicesWebpageHref;
	}

	/**
	 * @param asRSOtherServicesWebpageHref the asRSOtherServicesWebpageHref to add
	 */
	public void addAsRSOtherServicesWebpageHref(List<String> asRSOtherServicesWebpageHref) {
		this.getAsRSOtherServicesWebpageHref().add(asRSOtherServicesWebpageHref);
	}

	/**
	 * @return the asRSOtherServicesWebpageTitle
	 */
	public List<List<String>> getAsRSOtherServicesWebpageTitle() {
		if (this.asRSOtherServicesWebpageTitle == null) {
			this.asRSOtherServicesWebpageTitle = new ArrayList<List<String>>();
		}
		return this.asRSOtherServicesWebpageTitle;
	}

	/**
	 * @param asRSOtherServicesWebpageTitle the asRSOtherServicesWebpageTitle to set
	 */
	public void setAsRSOtherServicesWebpageTitle(
			List<List<String>> asRSOtherServicesWebpageTitle) {
		this.asRSOtherServicesWebpageTitle = asRSOtherServicesWebpageTitle;
	}

	/**
	 * @param asRSOtherServicesWebpageTitle the asRSOtherServicesWebpageTitle to add
	 */
	public void addAsRSOtherServicesWebpageTitle(List<String> asRSOtherServicesWebpageTitle) {
		this.getAsRSOtherServicesWebpageTitle().add(asRSOtherServicesWebpageTitle);
	}

	/**
	 * @return the asRSOtherServicesWebpageLang
	 */
	public List<List<String>> getAsRSOtherServicesWebpageLang() {
		if (this.asRSOtherServicesWebpageLang == null) {
			this.asRSOtherServicesWebpageLang = new ArrayList<List<String>>();
		}
		return this.asRSOtherServicesWebpageLang;
	}

	/**
	 * @param asRSOtherServicesWebpageLang the asRSOtherServicesWebpageLang to set
	 */
	public void setAsRSOtherServicesWebpageLang(
			List<List<String>> asRSOtherServicesWebpageLang) {
		this.asRSOtherServicesWebpageLang = asRSOtherServicesWebpageLang;
	}

	/**
	 * @param asRSOtherServicesWebpageLang the asRSOtherServicesWebpageLang to add
	 */
	public void addAsRSOtherServicesWebpageLang(List<String> asRSOtherServicesWebpageLang) {
		this.getAsRSOtherServicesWebpageLang().add(asRSOtherServicesWebpageLang);
	}

	/**
	 * @return the descRepositorhist
	 */
	public List<List<String>> getDescRepositorhist() {
		if (this.descRepositorhist == null) {
			this.descRepositorhist = new ArrayList<List<String>>();
		}
		return this.descRepositorhist;
	}

	/**
	 * @param descRepositorhist the descRepositorhist to set
	 */
	public void setDescRepositorhist(List<List<String>> descRepositorhist) {
		this.descRepositorhist = descRepositorhist;
	}

	/**
	 * @param descRepositorhist the descRepositorhist to add
	 */
	public void addDescRepositorhist(List<String> descRepositorhist) {
		this.getDescRepositorhist().add(descRepositorhist);
	}

	/**
	 * @return the descRepositorhistLang
	 */
	public List<List<String>> getDescRepositorhistLang() {
		if (this.descRepositorhistLang == null) {
			this.descRepositorhistLang = new ArrayList<List<String>>();
		}
		return this.descRepositorhistLang;
	}

	/**
	 * @param descRepositorhistLang the descRepositorhistLang to set
	 */
	public void setDescRepositorhistLang(List<List<String>> descRepositorhistLang) {
		this.descRepositorhistLang = descRepositorhistLang;
	}

	/**
	 * @param descRepositorhistLang the descRepositorhistLang to add
	 */
	public void addDescRepositorhistLang(List<String> descRepositorhistLang) {
		this.getDescRepositorhistLang().add(descRepositorhistLang);
	}

	/**
	 * @return the descRepositorFoundDate
	 */
	public List<List<String>> getDescRepositorFoundDate() {
		if (this.descRepositorFoundDate == null) {
			this.descRepositorFoundDate = new ArrayList<List<String>>();
		}
		return this.descRepositorFoundDate;
	}

	/**
	 * @param descRepositorFoundDate the descRepositorFoundDate to set
	 */
	public void setDescRepositorFoundDate(List<List<String>> descRepositorFoundDate) {
		this.descRepositorFoundDate = descRepositorFoundDate;
	}

	/**
	 * @param descRepositorFoundDate the descRepositorFoundDate to add
	 */
	public void addDescRepositorFoundDate(List<String> descRepositorFoundDate) {
		this.getDescRepositorFoundDate().add(descRepositorFoundDate);
	}

	/**
	 * @return the descRepositorFoundRule
	 */
	public List<List<String>> getDescRepositorFoundRule() {
		if (this.descRepositorFoundRule == null) {
			this.descRepositorFoundRule = new ArrayList<List<String>>();
		}
		return this.descRepositorFoundRule;
	}

	/**
	 * @param descRepositorFoundRule the descRepositorFoundRule to set
	 */
	public void setDescRepositorFoundRule(List<List<String>> descRepositorFoundRule) {
		this.descRepositorFoundRule = descRepositorFoundRule;
	}

	/**
	 * @param descRepositorFoundRule the descRepositorFoundRule to add
	 */
	public void addDescRepositorFoundRule(List<String> descRepositorFoundRule) {
		this.getDescRepositorFoundRule().add(descRepositorFoundRule);
	}

	/**
	 * @return the descRepositorFoundRuleLang
	 */
	public List<List<String>> getDescRepositorFoundRuleLang() {
		if (this.descRepositorFoundRuleLang == null) {
			this.descRepositorFoundRuleLang = new ArrayList<List<String>>();
		}
		return this.descRepositorFoundRuleLang;
	}

	/**
	 * @param descRepositorFoundRuleLang the descRepositorFoundRuleLang to set
	 */
	public void setDescRepositorFoundRuleLang(
			List<List<String>> descRepositorFoundRuleLang) {
		this.descRepositorFoundRuleLang = descRepositorFoundRuleLang;
	}

	/**
	 * @param descRepositorFoundRuleLang the descRepositorFoundRuleLang to add
	 */
	public void addDescRepositorFoundRuleLang(List<String> descRepositorFoundRuleLang) {
		this.getDescRepositorFoundRuleLang().add(descRepositorFoundRuleLang);
	}

	/**
	 * @return the descRepositorSupDate
	 */
	public List<List<String>> getDescRepositorSupDate() {
		if (this.descRepositorSupDate == null) {
			this.descRepositorSupDate = new ArrayList<List<String>>();
		}
		return this.descRepositorSupDate;
	}

	/**
	 * @param descRepositorSupDate the descRepositorSupDate to set
	 */
	public void setDescRepositorSupDate(List<List<String>> descRepositorSupDate) {
		this.descRepositorSupDate = descRepositorSupDate;
	}

	/**
	 * @param descRepositorSupDate the descRepositorSupDate to add
	 */
	public void addDescRepositorSupDate(List<String> descRepositorSupDate) {
		this.getDescRepositorSupDate().add(descRepositorSupDate);
	}

	/**
	 * @return the descRepositorSupRule
	 */
	public List<List<String>> getDescRepositorSupRule() {
		if (this.descRepositorSupRule == null) {
			this.descRepositorSupRule = new ArrayList<List<String>>();
		}
		return this.descRepositorSupRule;
	}

	/**
	 * @param descRepositorSupRule the descRepositorSupRule to set
	 */
	public void setDescRepositorSupRule(List<List<String>> descRepositorSupRule) {
		this.descRepositorSupRule = descRepositorSupRule;
	}

	/**
	 * @param descRepositorSupRule the descRepositorSupRule to add
	 */
	public void addDescRepositorSupRule(List<String> descRepositorSupRule) {
		this.getDescRepositorSupRule().add(descRepositorSupRule);
	}

	/**
	 * @return the descRepositorSupRuleLang
	 */
	public List<List<String>> getDescRepositorSupRuleLang() {
		if (this.descRepositorSupRuleLang == null) {
			this.descRepositorSupRuleLang = new ArrayList<List<String>>();
		}
		return this.descRepositorSupRuleLang;
	}

	/**
	 * @param descRepositorSupRuleLang the descRepositorSupRuleLang to set
	 */
	public void setDescRepositorSupRuleLang(
			List<List<String>> descRepositorSupRuleLang) {
		this.descRepositorSupRuleLang = descRepositorSupRuleLang;
	}

	/**
	 * @param descRepositorSupRuleLang the descRepositorSupRuleLang to add
	 */
	public void addDescRepositorSupRuleLang(List<String> descRepositorSupRuleLang) {
		this.getDescRepositorSupRuleLang().add(descRepositorSupRuleLang);
	}

	/**
	 * @return the descAdminunit
	 */
	public List<List<String>> getDescAdminunit() {
		if (this.descAdminunit == null) {
			this.descAdminunit = new ArrayList<List<String>>();
		}
		return this.descAdminunit;
	}

	/**
	 * @param descAdminunit the descAdminunit to set
	 */
	public void setDescAdminunit(List<List<String>> descAdminunit) {
		this.descAdminunit = descAdminunit;
	}

	/**
	 * @param descAdminunit the descAdminunit to add
	 */
	public void addDescAdminunit(List<String> descAdminunit) {
		this.getDescAdminunit().add(descAdminunit);
	}

	/**
	 * @return the descAdminunitLang
	 */
	public List<List<String>> getDescAdminunitLang() {
		if (this.descAdminunitLang == null) {
			this.descAdminunitLang = new ArrayList<List<String>>();
		}
		return this.descAdminunitLang;
	}

	/**
	 * @param descAdminunitLang the descAdminunitLang to set
	 */
	public void setDescAdminunitLang(List<List<String>> descAdminunitLang) {
		this.descAdminunitLang = descAdminunitLang;
	}

	/**
	 * @param descAdminunitLang the descAdminunitLang to add
	 */
	public void addDescAdminunitLang(List<String> descAdminunitLang) {
		this.getDescAdminunitLang().add(descAdminunitLang);
	}

	/**
	 * @return the descBuilding
	 */
	public List<List<String>> getDescBuilding() {
		if (this.descBuilding == null) {
			this.descBuilding = new ArrayList<List<String>>();
		}
		return this.descBuilding;
	}

	/**
	 * @param descBuilding the descBuilding to set
	 */
	public void setDescBuilding(List<List<String>> descBuilding) {
		this.descBuilding = descBuilding;
	}

	/**
	 * @param descBuilding the descBuilding to add
	 */
	public void addDescBuilding(List<String> descBuilding) {
		this.getDescBuilding().add(descBuilding);
	}

	/**
	 * @return the descBuildingLang
	 */
	public List<List<String>> getDescBuildingLang() {
		if (this.descBuildingLang == null) {
			this.descBuildingLang = new ArrayList<List<String>>();
		}
		return this.descBuildingLang;
	}

	/**
	 * @param descBuildingLang the descBuildingLang to set
	 */
	public void setDescBuildingLang(List<List<String>> descBuildingLang) {
		this.descBuildingLang = descBuildingLang;
	}

	/**
	 * @param descBuildingLang the descBuildingLang to add
	 */
	public void addDescBuildingLang(List<String> descBuildingLang) {
		this.getDescBuildingLang().add(descBuildingLang);
	}

	/**
	 * @return the descRepositorarea
	 */
	public List<List<String>> getDescRepositorarea() {
		if (this.descRepositorarea == null) {
			this.descRepositorarea = new ArrayList<List<String>>();
		}
		return this.descRepositorarea;
	}

	/**
	 * @param descRepositorarea the descRepositorarea to set
	 */
	public void setDescRepositorarea(List<List<String>> descRepositorarea) {
		this.descRepositorarea = descRepositorarea;
	}

	/**
	 * @param descRepositorarea the descRepositorarea to add
	 */
	public void addDescRepositorarea(List<String> descRepositorarea) {
		this.getDescRepositorarea().add(descRepositorarea);
	}

	/**
	 * @return the descRepositorareaUnit
	 */
	public List<List<String>> getDescRepositorareaUnit() {
		if (this.descRepositorareaUnit == null) {
			this.descRepositorareaUnit = new ArrayList<List<String>>();
		}
		return this.descRepositorareaUnit;
	}

	/**
	 * @param descRepositorareaUnit the descRepositorareaUnit to set
	 */
	public void setDescRepositorareaUnit(List<List<String>> descRepositorareaUnit) {
		this.descRepositorareaUnit = descRepositorareaUnit;
	}

	/**
	 * @param descRepositorareaUnit the descRepositorareaUnit to add
	 */
	public void addDescRepositorareaUnit(List<String> descRepositorareaUnit) {
		this.getDescRepositorareaUnit().add(descRepositorareaUnit);
	}

	/**
	 * @return the descLengthshelf
	 */
	public List<List<String>> getDescLengthshelf() {
		if (this.descLengthshelf == null) {
			this.descLengthshelf = new ArrayList<List<String>>();
		}
		return this.descLengthshelf;
	}

	/**
	 * @param descLengthshelf the descLengthshelf to set
	 */
	public void setDescLengthshelf(List<List<String>> descLengthshelf) {
		this.descLengthshelf = descLengthshelf;
	}

	/**
	 * @param descLengthshelf the descLengthshelf to add
	 */
	public void addDescLengthshelf(List<String> descLengthshelf) {
		this.getDescLengthshelf().add(descLengthshelf);
	}

	/**
	 * @return the descLengthshelfUnit
	 */
	public List<List<String>> getDescLengthshelfUnit() {
		if (this.descLengthshelfUnit == null) {
			this.descLengthshelfUnit = new ArrayList<List<String>>();
		}
		return this.descLengthshelfUnit;
	}

	/**
	 * @param descLengthshelfUnit the descLengthshelfUnit to set
	 */
	public void setDescLengthshelfUnit(List<List<String>> descLengthshelfUnit) {
		this.descLengthshelfUnit = descLengthshelfUnit;
	}

	/**
	 * @param descLengthshelfUnit the descLengthshelfUnit to add
	 */
	public void addDescLengthshelfUnit(List<String> descLengthshelfUnit) {
		this.getDescLengthshelfUnit().add(descLengthshelfUnit);
	}

	/**
	 * @return the descHoldings
	 */
	public List<List<String>> getDescHoldings() {
		if (this.descHoldings == null) {
			this.descHoldings = new ArrayList<List<String>>();
		}
		return this.descHoldings;
	}

	/**
	 * @param descHoldings the descHoldings to set
	 */
	public void setDescHoldings(List<List<String>> descHoldings) {
		this.descHoldings = descHoldings;
	}

	/**
	 * @param descHoldings the descHoldings to add
	 */
	public void addDescHoldings(List<String> descHoldings) {
		this.getDescHoldings().add(descHoldings);
	}

	/**
	 * @return the descHoldingsLang
	 */
	public List<List<String>> getDescHoldingsLang() {
		if (this.descHoldingsLang == null) {
			this.descHoldingsLang = new ArrayList<List<String>>();
		}
		return this.descHoldingsLang;
	}

	/**
	 * @param descHoldingsLang the descHoldingsLang to set
	 */
	public void setDescHoldingsLang(List<List<String>> descHoldingsLang) {
		this.descHoldingsLang = descHoldingsLang;
	}

	/**
	 * @param descHoldingsLang the descHoldingsLang to add
	 */
	public void addDescHoldingsLang(List<String> descHoldingsLang) {
		this.getDescHoldingsLang().add(descHoldingsLang);
	}

	/**
	 * @return the descHoldingsDate
	 */
	public List<List<String>> getDescHoldingsDate() {
		if (this.descHoldingsDate == null) {
			this.descHoldingsDate = new ArrayList<List<String>>();
		}
		return this.descHoldingsDate;
	}

	/**
	 * @param descHoldingsDate the descHoldingsDate to set
	 */
	public void setDescHoldingsDate(List<List<String>> descHoldingsDate) {
		this.descHoldingsDate = descHoldingsDate;
	}

	/**
	 * @param descHoldingsDate the descHoldingsDate to add
	 */
	public void addDescHoldingsDate(List<String> descHoldingsDate) {
		this.getDescHoldingsDate().add(descHoldingsDate);
	}

	/**
	 * @return the descNumberOfHoldingsDateRange
	 */
	public List<List<String>> getDescNumberOfHoldingsDateRange() {
		if (this.descNumberOfHoldingsDateRange == null) {
			this.descNumberOfHoldingsDateRange = new ArrayList<List<String>>();
		}
		return this.descNumberOfHoldingsDateRange;
	}

	/**
	 * @param descNumberOfHoldingsDateRange the descNumberOfHoldingsDateRange to set
	 */
	public void setDescNumberOfHoldingsDateRange(
			List<List<String>> descNumberOfHoldingsDateRange) {
		this.descNumberOfHoldingsDateRange = descNumberOfHoldingsDateRange;
	}

	/**
	 * @param descNumberOfHoldingsDateRange the descNumberOfHoldingsDateRange to add
	 */
	public void addDescNumberOfHoldingsDateRange(List<String> descNumberOfHoldingsDateRange) {
		this.getDescNumberOfHoldingsDateRange().add(descNumberOfHoldingsDateRange);
	}

	/**
	 * @return the descHoldingsDateRangeFromDate
	 */
	public List<List<String>> getDescHoldingsDateRangeFromDate() {
		if (this.descHoldingsDateRangeFromDate == null) {
			this.descHoldingsDateRangeFromDate = new ArrayList<List<String>>();
		}
		return this.descHoldingsDateRangeFromDate;
	}

	/**
	 * @param descHoldingsDateRangeFromDate the descHoldingsDateRangeFromDate to set
	 */
	public void setDescHoldingsDateRangeFromDate(
			List<List<String>> descHoldingsDateRangeFromDate) {
		this.descHoldingsDateRangeFromDate = descHoldingsDateRangeFromDate;
	}

	/**
	 * @param descHoldingsDateRangeFromDate the descHoldingsDateRangeFromDate to add
	 */
	public void addDescHoldingsDateRangeFromDate(List<String> descHoldingsDateRangeFromDate) {
		this.getDescHoldingsDateRangeFromDate().add(descHoldingsDateRangeFromDate);
	}

	/**
	 * @return the descHoldingsDateRangeToDate
	 */
	public List<List<String>> getDescHoldingsDateRangeToDate() {
		if (this.descHoldingsDateRangeToDate == null) {
			this.descHoldingsDateRangeToDate = new ArrayList<List<String>>();
		}
		return this.descHoldingsDateRangeToDate;
	}

	/**
	 * @param descHoldingsDateRangeToDate the descHoldingsDateRangeToDate to set
	 */
	public void setDescHoldingsDateRangeToDate(
			List<List<String>> descHoldingsDateRangeToDate) {
		this.descHoldingsDateRangeToDate = descHoldingsDateRangeToDate;
	}

	/**
	 * @param descHoldingsDateRangeToDate the descHoldingsDateRangeToDate to add
	 */
	public void addDescHoldingsDateRangeToDate(List<String> descHoldingsDateRangeToDate) {
		this.getDescHoldingsDateRangeToDate().add(descHoldingsDateRangeToDate);
	}

	/**
	 * @return the descExtent
	 */
	public List<List<String>> getDescExtent() {
		if (this.descExtent == null) {
			this.descExtent = new ArrayList<List<String>>();
		}
		return this.descExtent;
	}

	/**
	 * @param descExtent the descExtent to set
	 */
	public void setDescExtent(List<List<String>> descExtent) {
		this.descExtent = descExtent;
	}

	/**
	 * @param descExtent the descExtent to add
	 */
	public void addDescExtent(List<String> descExtent) {
		this.getDescExtent().add(descExtent);
	}

	/**
	 * @return the descExtentUnit
	 */
	public List<List<String>> getDescExtentUnit() {
		if (this.descExtentUnit == null) {
			this.descExtentUnit = new ArrayList<List<String>>();
		}
		return this.descExtentUnit;
	}

	/**
	 * @param descExtentUnit the descExtentUnit to set
	 */
	public void setDescExtentUnit(List<List<String>> descExtentUnit) {
		this.descExtentUnit = descExtentUnit;
	}

	/**
	 * @param descExtentUnit the descExtentUnit to add
	 */
	public void addDescExtentUnit(List<String> descExtentUnit) {
		this.getDescExtentUnit().add(descExtentUnit);
	}

	/**
	 * @return the controlAgentLang
	 */
	public String getControlAgentLang() {
		return this.controlAgentLang;
	}

	/**
	 * @param controlAgentLang the controlAgentLang to set
	 */
	public void setControlAgentLang(String controlAgentLang) {
		this.controlAgentLang = controlAgentLang;
	}

	/**
	 * @return the controlAgencyCode
	 */
	public String getControlAgencyCode() {
		return this.controlAgencyCode;
	}

	/**
	 * @param controlAgencyCode the controlAgencyCode to set
	 */
	public void setControlAgencyCode(String controlAgencyCode) {
		this.controlAgencyCode = controlAgencyCode;
	}

	/**
	 * @return the controlLanguageDeclaration
	 */
	public List<String> getControlLanguageDeclaration() {
		if (this.controlLanguageDeclaration == null) {
			this.controlLanguageDeclaration = new ArrayList<String>();
		}
		return this.controlLanguageDeclaration;
	}

	/**
	 * @param controlLanguageDeclaration the controlLanguageDeclaration to set
	 */
	public void setControlLanguageDeclaration(
			List<String> controlLanguageDeclaration) {
		this.controlLanguageDeclaration = controlLanguageDeclaration;
	}

	/**
	 * @param controlLanguageDeclaration the controlLanguageDeclaration to add
	 */
	public void addControlLanguageDeclaration(String controlLanguageDeclaration) {
		this.getControlLanguageDeclaration().add(controlLanguageDeclaration);
	}

	/**
	 * @return the controlScript
	 */
	public List<String> getControlScript() {
		if (this.controlScript == null) {
			this.controlScript = new ArrayList<String>();
		}
		return this.controlScript;
	}

	/**
	 * @param controlScript the controlScript to set
	 */
	public void setControlScript(List<String> controlScript) {
		this.controlScript = controlScript;
	}

	/**
	 * @param controlScript the controlScript to add
	 */
	public void addControlScript(String controlScript) {
		this.getControlScript().add(controlScript);
	}

	/**
	 * @return the controlNumberOfLanguages
	 */
	public List<String> getControlNumberOfLanguages() {
		if (this.controlNumberOfLanguages == null) {
			this.controlNumberOfLanguages = new ArrayList<String>();
		}
		return this.controlNumberOfLanguages;
	}

	/**
	 * @param controlNumberOfLanguages the controlNumberOfLanguages to set
	 */
	public void setControlNumberOfLanguages(List<String> controlNumberOfLanguages) {
		this.controlNumberOfLanguages = controlNumberOfLanguages;
	}

	/**
	 * @param controlNumberOfLanguages the controlNumberOfLanguages to add
	 */
	public void addControlNumberOfLanguages(String controlNumberOfLanguages) {
		this.getControlNumberOfLanguages().add(controlNumberOfLanguages);
	}

	/**
	 * @return the controlNumberOfRules
	 */
	public List<String> getControlNumberOfRules() {
		if (this.controlNumberOfRules == null) {
			this.controlNumberOfRules = new ArrayList<String>();
		}
		return this.controlNumberOfRules;
	}

	/**
	 * @param controlNumberOfRules the controlNumberOfRules to set
	 */
	public void setControlNumberOfRules(List<String> controlNumberOfRules) {
		this.controlNumberOfRules = controlNumberOfRules;
	}

	/**
	 * @param controlNumberOfRules the controlNumberOfRules to add
	 */
	public void addControlNumberOfRules(String controlNumberOfRules) {
		this.getControlNumberOfRules().add(controlNumberOfRules);
	}

	/**
	 * @return the controlAbbreviation
	 */
	public List<String> getControlAbbreviation() {
		if (this.controlAbbreviation == null) {
			this.controlAbbreviation = new ArrayList<String>();
		}
		return this.controlAbbreviation;
	}

	/**
	 * @param controlAbbreviation the controlAbbreviation to set
	 */
	public void setControlAbbreviation(List<String> controlAbbreviation) {
		this.controlAbbreviation = controlAbbreviation;
	}

	/**
	 * @param controlAbbreviation the controlAbbreviation to add
	 */
	public void addControlAbbreviation(String controlAbbreviation) {
		this.getControlAbbreviation().add(controlAbbreviation);
	}

	/**
	 * @return the controlCitation
	 */
	public List<String> getControlCitation() {
		if (this.controlCitation == null) {
			this.controlCitation = new ArrayList<String>();
		}
		return this.controlCitation;
	}

	/**
	 * @param controlCitation the controlCitation to set
	 */
	public void setControlCitation(List<String> controlCitation) {
		this.controlCitation = controlCitation;
	}

	/**
	 * @param controlCitation the controlCitation to add
	 */
	public void addControlCitation(String controlCitation) {
		this.getControlCitation().add(controlCitation);
	}

	/**
	 * @return the relationsResourceRelationType
	 */
	public List<String> getRelationsResourceRelationType() {
		if (this.relationsResourceRelationType == null) {
			this.relationsResourceRelationType = new ArrayList<String>();
		}
		return this.relationsResourceRelationType;
	}

	/**
	 * @param relationsResourceRelationType the relationsResourceRelationType to set
	 */
	public void setRelationsResourceRelationType(
			List<String> relationsResourceRelationType) {
		this.relationsResourceRelationType = relationsResourceRelationType;
	}

	/**
	 * @param relationsResourceRelationType the relationsResourceRelationType to add
	 */
	public void addRelationsResourceRelationType(String relationsResourceRelationType) {
		this.getRelationsResourceRelationType().add(relationsResourceRelationType);
	}

	/**
	 * @return the relationsResourceRelationHref
	 */
	public List<String> getRelationsResourceRelationHref() {
		if (this.relationsResourceRelationHref == null) {
			this.relationsResourceRelationHref = new ArrayList<String>();
		}
		return this.relationsResourceRelationHref;
	}

	/**
	 * @param relationsResourceRelationHref the relationsResourceRelationHref to set
	 */
	public void setRelationsResourceRelationHref(
			List<String> relationsResourceRelationHref) {
		this.relationsResourceRelationHref = relationsResourceRelationHref;
	}

	/**
	 * @param relationsResourceRelationHref the relationsResourceRelationHref to add
	 */
	public void addRelationsResourceRelationHref(String relationsResourceRelationHref) {
		this.getRelationsResourceRelationHref().add(relationsResourceRelationHref);
	}

	/**
	 * @return the relationsResourceRelationEntry
	 */
	public List<String> getRelationsResourceRelationEntry() {
		if (this.relationsResourceRelationEntry == null) {
			this.relationsResourceRelationEntry = new ArrayList<String>();
		}
		return this.relationsResourceRelationEntry;
	}

	/**
	 * @param relationsResourceRelationEntry the relationsResourceRelationEntry to set
	 */
	public void setRelationsResourceRelationEntry(
			List<String> relationsResourceRelationEntry) {
		this.relationsResourceRelationEntry = relationsResourceRelationEntry;
	}

	/**
	 * @param relationsResourceRelationEntry the relationsResourceRelationEntry to add
	 */
	public void addRelationsResourceRelationEntry(String relationsResourceRelationEntry) {
		this.getRelationsResourceRelationEntry().add(relationsResourceRelationEntry);
	}

	/**
	 * @return the relationsResourceRelationEntryLang
	 */
	public List<String> getRelationsResourceRelationEntryLang() {
		if (this.relationsResourceRelationEntryLang == null) {
			this.relationsResourceRelationEntryLang = new ArrayList<String>();
		}
		return this.relationsResourceRelationEntryLang;
	}

	/**
	 * @param relationsResourceRelationEntryLang the relationsResourceRelationEntryLang to set
	 */
	public void setRelationsResourceRelationEntryLang(
			List<String> relationsResourceRelationEntryLang) {
		this.relationsResourceRelationEntryLang = relationsResourceRelationEntryLang;
	}

	/**
	 * @param relationsResourceRelationEntryLang the relationsResourceRelationEntryLang to add
	 */
	public void addRelationsResourceRelationEntryLang(String relationsResourceRelationEntryLang) {
		this.getRelationsResourceRelationEntryLang().add(relationsResourceRelationEntryLang);
	}

	/**
	 * @return the relationsResourceRelationEntryDescription
	 */
	public List<String> getRelationsResourceRelationEntryDescription() {
		if (this.relationsResourceRelationEntryDescription == null) {
			this.relationsResourceRelationEntryDescription = new ArrayList<String>();
		}
		return this.relationsResourceRelationEntryDescription;
	}

	/**
	 * @param relationsResourceRelationEntryDescription the relationsResourceRelationEntryDescription to set
	 */
	public void setRelationsResourceRelationEntryDescription(
			List<String> relationsResourceRelationEntryDescription) {
		this.relationsResourceRelationEntryDescription = relationsResourceRelationEntryDescription;
	}

	/**
	 * @param relationsResourceRelationEntryDescription the relationsResourceRelationEntryDescription to add
	 */
	public void addRelationsResourceRelationEntryDescription(String relationsResourceRelationEntryDescription) {
		this.getRelationsResourceRelationEntryDescription().add(relationsResourceRelationEntryDescription);
	}

	/**
	 * @return the relationsResourceRelationEntryDescriptionLang
	 */
	public List<String> getRelationsResourceRelationEntryDescriptionLang() {
		if (this.relationsResourceRelationEntryDescriptionLang == null) {
			this.relationsResourceRelationEntryDescriptionLang = new ArrayList<String>();
		}
		return this.relationsResourceRelationEntryDescriptionLang;
	}

	/**
	 * @param relationsResourceRelationEntryDescriptionLang the relationsResourceRelationEntryDescriptionLang to set
	 */
	public void setRelationsResourceRelationEntryDescriptionLang(
			List<String> relationsResourceRelationEntryDescriptionLang) {
		this.relationsResourceRelationEntryDescriptionLang = relationsResourceRelationEntryDescriptionLang;
	}

	/**
	 * @param relationsResourceRelationEntryDescriptionLang the relationsResourceRelationEntryDescriptionLang to add
	 */
	public void addRelationsResourceRelationEntryDescriptionLang(String relationsResourceRelationEntryDescriptionLang) {
		this.getRelationsResourceRelationEntryDescriptionLang().add(relationsResourceRelationEntryDescriptionLang);
	}

	/**
	 * @return the relationsNumberOfEagRelations
	 */
	public List<String> getRelationsNumberOfEagRelations() {
		if (this.relationsNumberOfEagRelations == null) {
			this.relationsNumberOfEagRelations = new ArrayList<String>();
		}
		return this.relationsNumberOfEagRelations;
	}

	/**
	 * @param relationsNumberOfEagRelations the relationsNumberOfEagRelations to set
	 */
	public void setRelationsNumberOfEagRelations(
			List<String> relationsNumberOfEagRelations) {
		this.relationsNumberOfEagRelations = relationsNumberOfEagRelations;
	}

	/**
	 * @param relationsNumberOfEagRelations the relationsNumberOfEagRelations to add
	 */
	public void addRelationsNumberOfEagRelations(String relationsNumberOfEagRelations) {
		this.getRelationsNumberOfEagRelations().add(relationsNumberOfEagRelations);
	}

	/**
	 * @return the relationsEagRelationType
	 */
	public List<String> getRelationsEagRelationType() {
		if (this.relationsEagRelationType == null) {
			this.relationsEagRelationType = new ArrayList<String>();
		}
		return this.relationsEagRelationType;
	}

	/**
	 * @param relationsEagRelationType the relationsEagRelationType to set
	 */
	public void setRelationsEagRelationType(List<String> relationsEagRelationType) {
		this.relationsEagRelationType = relationsEagRelationType;
	}

	/**
	 * @param relationsEagRelationType the relationsEagRelationType to add
	 */
	public void addRelationsEagRelationType(String relationsEagRelationType) {
		this.getRelationsEagRelationType().add(relationsEagRelationType);
	}

	/**
	 * @return the relationsEagRelationHref
	 */
	public List<String> getRelationsEagRelationHref() {
		if (this.relationsEagRelationHref == null) {
			this.relationsEagRelationHref = new ArrayList<String>();
		}
		return this.relationsEagRelationHref;
	}

	/**
	 * @param relationsEagRelationHref the relationsEagRelationHref to set
	 */
	public void setRelationsEagRelationHref(List<String> relationsEagRelationHref) {
		this.relationsEagRelationHref = relationsEagRelationHref;
	}

	/**
	 * @param relationsEagRelationHref the relationsEagRelationHref to add
	 */
	public void addRelationsEagRelationHref(String relationsEagRelationHref) {
		this.getRelationsEagRelationHref().add(relationsEagRelationHref);
	}

	/**
	 * @return the relationsEagRelationEntry
	 */
	public List<String> getRelationsEagRelationEntry() {
		if (this.relationsEagRelationEntry == null) {
			this.relationsEagRelationEntry = new ArrayList<String>();
		}
		return this.relationsEagRelationEntry;
	}

	/**
	 * @param relationsEagRelationEntry the relationsEagRelationEntry to set
	 */
	public void setRelationsEagRelationEntry(List<String> relationsEagRelationEntry) {
		this.relationsEagRelationEntry = relationsEagRelationEntry;
	}

	/**
	 * @param relationsEagRelationEntry the relationsEagRelationEntry to add
	 */
	public void addRelationsEagRelationEntry(String relationsEagRelationEntry) {
		this.getRelationsEagRelationEntry().add(relationsEagRelationEntry);
	}

	/**
	 * @return the relationsEagRelationEntryLang
	 */
	public List<String> getRelationsEagRelationEntryLang() {
		if (this.relationsEagRelationEntryLang == null) {
			this.relationsEagRelationEntryLang = new ArrayList<String>();
		}
		return this.relationsEagRelationEntryLang;
	}

	/**
	 * @param relationsEagRelationEntryLang the relationsEagRelationEntryLang to set
	 */
	public void setRelationsEagRelationEntryLang(
			List<String> relationsEagRelationEntryLang) {
		this.relationsEagRelationEntryLang = relationsEagRelationEntryLang;
	}

	/**
	 * @param relationsEagRelationEntryLang the relationsEagRelationEntryLang to add
	 */
	public void addRelationsEagRelationEntryLang(String relationsEagRelationEntryLang) {
		this.getRelationsEagRelationEntryLang().add(relationsEagRelationEntryLang);
	}

	/**
	 * @return the relationsEagRelationEntryDescription
	 */
	public List<String> getRelationsEagRelationEntryDescription() {
		if (this.relationsEagRelationEntryDescription == null) {
			this.relationsEagRelationEntryDescription = new ArrayList<String>();
		}
		return this.relationsEagRelationEntryDescription;
	}

	/**
	 * @param relationsEagRelationEntryDescription the relationsEagRelationEntryDescription to set
	 */
	public void setRelationsEagRelationEntryDescription(
			List<String> relationsEagRelationEntryDescription) {
		this.relationsEagRelationEntryDescription = relationsEagRelationEntryDescription;
	}

	/**
	 * @param relationsEagRelationEntryDescription the relationsEagRelationEntryDescription to add
	 */
	public void addRelationsEagRelationEntryDescription(String relationsEagRelationEntryDescription) {
		this.getRelationsEagRelationEntryDescription().add(relationsEagRelationEntryDescription);
	}

	/**
	 * @return the relationsEagRelationEntryDescriptionLang
	 */
	public List<String> getRelationsEagRelationEntryDescriptionLang() {
		if (this.relationsEagRelationEntryDescriptionLang == null) {
			this.relationsEagRelationEntryDescriptionLang = new ArrayList<String>();
		}
		return this.relationsEagRelationEntryDescriptionLang;
	}

	/**
	 * @param relationsEagRelationEntryDescriptionLang the relationsEagRelationEntryDescriptionLang to set
	 */
	public void setRelationsEagRelationEntryDescriptionLang(
			List<String> relationsEagRelationEntryDescriptionLang) {
		this.relationsEagRelationEntryDescriptionLang = relationsEagRelationEntryDescriptionLang;
	}

	/**
	 * @param relationsEagRelationEntryDescriptionLang the relationsEagRelationEntryDescriptionLang to add
	 */
	public void addRelationsEagRelationEntryDescriptionLang(String relationsEagRelationEntryDescriptionLang) {
		this.getRelationsEagRelationEntryDescriptionLang().add(relationsEagRelationEntryDescriptionLang);
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
		if (this.eag == null
				|| (this.eag.getControl() == null && this.eag.getArchguide() == null
				&& this.eag.getRelations() == null)) {
			this.setCountryCode(new ArchivalLandscape().getmyCountry());
			this.setRecordId(Eag2012.generatesISOCode(this.getId()));
			return false;
		}

		// Fill number of repositories.
		if (this.eag != null && this.eag.getArchguide() != null
				&& this.eag.getArchguide().getDesc() != null
				&& this.eag.getArchguide().getDesc().getRepositories() != null
				&& !this.eag.getArchguide().getDesc().getRepositories().getRepository().isEmpty()) {
			this.setNumberOfRepositories(this.eag.getArchguide().getDesc().getRepositories().getRepository().size());
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
		if (this.eag.getControl() != null && this.eag.getControl().getMaintenanceHistory() != null
				&& !this.eag.getControl().getMaintenanceHistory().getMaintenanceEvent().isEmpty()) {
			if (this.eag.getControl().getMaintenanceHistory().getMaintenanceEvent().get(this.eag.getControl().getMaintenanceHistory().getMaintenanceEvent().size()-1).getAgent() != null) {
				this.setAgent(this.eag.getControl().getMaintenanceHistory().getMaintenanceEvent().get(this.eag.getControl().getMaintenanceHistory().getMaintenanceEvent().size()-1).getAgent().getContent());
			}
		}

		// Country code.
		if (this.eag.getArchguide() != null
				&& this.eag.getArchguide().getIdentity() != null
				&& this.eag.getArchguide().getIdentity().getRepositorid() != null
				&& this.eag.getArchguide().getIdentity().getRepositorid().getCountrycode() !=  null
				&& !this.eag.getArchguide().getIdentity().getRepositorid().getCountrycode().isEmpty()) {
			this.setCountryCode(this.eag.getArchguide().getIdentity().getRepositorid().getCountrycode());
		} else {
			this.setCountryCode(new ArchivalLandscape().getmyCountry());
		}

		// Identifier of the institution.
		if (this.eag.getArchguide() != null
				&& this.eag.getArchguide().getIdentity() != null
				&& this.eag.getArchguide().getIdentity().getOtherRepositorId() != null
				&& this.eag.getArchguide().getIdentity().getOtherRepositorId().getContent() != null
				&& !this.eag.getArchguide().getIdentity().getOtherRepositorId().getContent().isEmpty()) {
			this.setOtherRepositorId(this.eag.getArchguide().getIdentity().getOtherRepositorId().getContent());
			if (this.eag.getControl() != null
					&& this.eag.getControl().getRecordId() != null
					&& this.eag.getControl().getRecordId().getValue() != null
					&& !this.eag.getControl().getRecordId().getValue().isEmpty()) {
				if (!this.eag.getArchguide().getIdentity().getOtherRepositorId().getContent().equalsIgnoreCase(this.eag.getControl().getRecordId().getValue())) {
					this.setRecordIdISIL(Eag2012.OPTION_YES);
					this.setRecordId(this.eag.getControl().getRecordId().getValue());
					this.setSelfRecordId(this.getIdUsedInAPE());
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
		if (this.eag.getControl() != null
				&& !this.eag.getControl().getOtherRecordId().isEmpty()) {
			for (int i = 0; i < this.eag.getControl().getOtherRecordId().size(); i++) {
				OtherRecordId otherRecordId = this.eag.getControl().getOtherRecordId().get(i);
				if (i == 0) {
					this.setOtherRepositorId(otherRecordId.getValue());
					if (this.getRecordId() != null && !this.getRecordId().isEmpty()
							&& this.getOtherRepositorId().equalsIgnoreCase(this.getRecordId())) {
						this.setRecordIdISIL(Eag2012.OPTION_YES);
					} else {
						this.setRecordIdISIL(Eag2012.OPTION_NO);
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
			this.setRecordId(this.eag.getControl().getRecordId().getValue());
		}

		// Name of the institution.
		if (this.eag.getArchguide() != null
				&& this.eag.getArchguide().getIdentity() != null
				&& !this.eag.getArchguide().getIdentity().getAutform().isEmpty()) {
			for (int i = 0; i < this.eag.getArchguide().getIdentity().getAutform().size(); i++) {
				Autform autform = this.eag.getArchguide().getIdentity().getAutform().get(i);
				if (autform != null && autform.getContent() != null && !autform.getContent().isEmpty()) {
					this.addIdAutform(autform.getContent());
					if (autform.getLang() != null && !autform.getLang().isEmpty()) {
						this.addIdAutformLang(autform.getLang());
					} else {
						this.addIdAutformLang(Eag2012.OPTION_NONE);
					}
				}
			}
			// First name of the institution.
			this.setAutform(this.eag.getArchguide().getIdentity().getAutform().get(0).getContent());
			this.setAutformLang(this.eag.getArchguide().getIdentity().getAutform().get(0).getLang());
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
			this.setParform(this.eag.getArchguide().getIdentity().getParform().get(0).getContent());
			this.setParformLang(this.eag.getArchguide().getIdentity().getParform().get(0).getLang());
		}

		// Institution info.
		if (this.eag.getArchguide() != null && this.eag.getArchguide().getDesc() != null
				&& this.eag.getArchguide().getDesc().getRepositories() != null) {
			if (!this.eag.getArchguide().getDesc().getRepositories().getRepository().isEmpty()) {
				// First repository equals institution.
				Repository repository = this.eag.getArchguide().getDesc().getRepositories().getRepository().get(0);

				// Visitor & Postal address for institution.
				if (!repository.getLocation().isEmpty()) {
					for (int i = 0; i < repository.getLocation().size(); i++) {
						Location location = repository.getLocation().get(i);
						if ((location.getLocalType()!=null && location.getLocalType().equalsIgnoreCase(Eag2012.VISITORS_ADDRESS)) || (location.getLocalType()==null || location.getLocalType().isEmpty())) { //additional condition commented on #702
							this.addYiNumberOfVisitorsAddress("");
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
							}else{
								this.addYiStreet("");
							}
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
							}else{
								this.addYiMunicipalityPostalcode("");
							}
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
							}else{
								this.addYiCountry("");
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
						if (location.getLocalType()!=null && location.getLocalType().equalsIgnoreCase(Eag2012.POSTAL_ADDRESS)) {
							this.addYiNumberOfPostalAddress("");
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
							}/*else{
								this.addYiStreetPostal("");
							}*/
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
							}/*else{
								this.addYiMunicipalityPostalcodePostal("");
							}*/
						}
					}

					// Check language for "Visitor address" element.
					if (!this.getYiStreetLang().isEmpty()) {
						for (int i = 0; i < this.getYiStreetLang().size(); i++) {
							if (this.getYiStreetLang().get(i) != null
									&& this.getYiStreetLang().get(i).equalsIgnoreCase(Eag2012.OPTION_NONE)) {
								if (!this.getYiMunicipalityPostalcodeLang().isEmpty()
										&& this.getYiMunicipalityPostalcodeLang().size() > i
										&& this.getYiMunicipalityPostalcodeLang().get(i) != null
										&& !this.getYiMunicipalityPostalcodeLang().get(i).equalsIgnoreCase(Eag2012.OPTION_NONE)) {
									this.getYiStreetLang().remove(i);
									this.getYiStreetLang().add(i, this.getYiMunicipalityPostalcodeLang().get(i));
								} else if (!this.getYiCountryLang().isEmpty()
										&& this.getYiCountryLang().size() > i
										&& this.getYiCountryLang().get(i) != null
										&& !this.getYiCountryLang().get(i).equalsIgnoreCase(Eag2012.OPTION_NONE)) {
									this.getYiStreetLang().remove(i);
									this.getYiStreetLang().add(i, this.getYiCountryLang().get(i));
								}
							}
						}
					}

					// Check language for "Postal address" element.
					if (!this.getYiStreetPostalLang().isEmpty()) {
						for (int i = 0; i < this.getYiStreetPostalLang().size(); i++) {
							if (this.getYiStreetPostalLang().get(i) != null
									&& this.getYiStreetPostalLang().get(i).equalsIgnoreCase(Eag2012.OPTION_NONE)) {
								if (!this.getYiMunicipalityPostalcodePostalLang().isEmpty()
										&& this.getYiMunicipalityPostalcodePostalLang().size() > i
										&& this.getYiMunicipalityPostalcodePostalLang().get(i) != null
										&& !this.getYiMunicipalityPostalcodePostalLang().get(i).equalsIgnoreCase(Eag2012.OPTION_NONE)) {
									this.getYiStreetPostalLang().remove(i);
									this.getYiStreetPostalLang().add(i, this.getYiMunicipalityPostalcodePostalLang().get(i));
								}
							}
						}
					}
				}

				// Continent.
				if (repository.getGeogarea() != null
						&& repository.getGeogarea().getValue() != null
						&& !repository.getGeogarea().getValue().isEmpty()) {
					this.setGeogarea(getGeogareaString(repository.getGeogarea().getValue()));
				}

				// Telephone.
				if (!repository.getTelephone().isEmpty()) {
					this.setTelephone(repository.getTelephone().get(0).getContent());
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

				// Timetable info
				if (repository.getTimetable() != null) {
					Timetable timetable = repository.getTimetable();

					// Opening times.
					if (!timetable.getOpening().isEmpty()) {
						// Opening times for institution.
						for (int i = 0; i < timetable.getOpening().size(); i++) {
							if (!timetable.getOpening().isEmpty()
									&& timetable.getOpening().size() >= i
									&& timetable.getOpening().get(i).getContent() != null
									&& !timetable.getOpening().get(i).getContent().isEmpty()) {
								this.addYiOpening(timetable.getOpening().get(i).getContent());
							} else {
								this.addYiOpening("");
							}
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

				// Accessible to the public.
				if (repository.getAccess() != null) {
					if (repository.getAccess().getQuestion() != null
							&& !repository.getAccess().getQuestion().isEmpty()) {
						this.setAccessQuestion(repository.getAccess().getQuestion());
					} else {
						this.setAccessQuestion(Eag2012.OPTION_NO);
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
			}
		}

		// Reference to your institution’s holdings guide.
		if (this.eag.getRelations()!=null && !this.eag.getRelations().getResourceRelation().isEmpty()) {
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
	}

	/**
	 * Method to load all values of "Identity" tab.
	 */
	private void loadIdentityTabValues() {
		//  Formerly used names of the institution.
		if (this.eag.getArchguide() != null && this.eag.getArchguide().getIdentity() != null
				&& !this.eag.getArchguide().getIdentity().getNonpreform().isEmpty()) {
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
		if (this.eag.getArchguide() != null && this.eag.getArchguide().getIdentity() != null
				&& !this.eag.getArchguide().getIdentity().getRepositoryType().isEmpty()) {
			for (int i = 0; i < this.eag.getArchguide().getIdentity().getRepositoryType().size(); i++) {
				if (this.eag.getArchguide().getIdentity().getRepositoryType().get(i).getValue() != null
						&& !this.eag.getArchguide().getIdentity().getRepositoryType().get(i).getValue().isEmpty()) {
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
	}

	/**
	 * Method to load all values of "Contact" tab.
	 */
	private void loadContactTabValues() {
		// Repositories info.
		if (this.eag.getArchguide() != null && this.eag.getArchguide().getDesc() != null
				&& this.eag.getArchguide().getDesc().getRepositories() != null
				&& !this.eag.getArchguide().getDesc().getRepositories().getRepository().isEmpty()) {
			// For each repository
			for (int i = 0; i < this.eag.getArchguide().getDesc().getRepositories().getRepository().size(); i++) {
				Repository repository = this.eag.getArchguide().getDesc().getRepositories().getRepository().get(i);
				if (repository != null) {
					// Repository name.
					List<String> stringList = new ArrayList<String>();
					if (repository.getRepositoryName() != null && repository.getRepositoryName().size()>0 
							&& repository.getRepositoryName().get(0).getContent() != null
							&& !repository.getRepositoryName().get(0).getContent().isEmpty()) {
						stringList.add(repository.getRepositoryName().get(0).getContent());
					} else {
						stringList.add("");
					}
					this.addRepositoryName(stringList);
	
					// Repository role.
					stringList = new ArrayList<String>();
					if (repository.getRepositoryRole() != null
							&& repository.getRepositoryRole().getValue() != null
							&& !repository.getRepositoryRole().getValue().isEmpty()) {
						String roleValue = repository.getRepositoryRole().getValue();
						if (roleValue != null && !roleValue.isEmpty()
								&& Eag2012.OPTION_ROLE_BRANCH_TEXT.equalsIgnoreCase(roleValue)) {
							roleValue = Eag2012.OPTION_ROLE_BRANCH;
						} else if (roleValue != null && !roleValue.isEmpty()
								&& Eag2012.OPTION_ROLE_HEADQUARTERS_TEXT.equalsIgnoreCase(roleValue)) {
							roleValue = Eag2012.OPTION_ROLE_HEADQUARTERS;
						} else if (roleValue != null && !roleValue.isEmpty()
								&& Eag2012.OPTION_ROLE_INTERIM_TEXT.equalsIgnoreCase(roleValue)) {
							roleValue = Eag2012.OPTION_ROLE_INTERIM;
						}

						stringList.add(roleValue);
					} else {
						stringList.add("");
					}
					this.addRepositoryRole(stringList);
	
					// Visitor & Postal address.
					List<String> numberVisitorAdrressList = new ArrayList<String>();
					List<String> latitudeList = new ArrayList<String>();
					List<String> longitudeList = new ArrayList<String>();
					List<String> countryList = new ArrayList<String>();
					List<String> countryLangList = new ArrayList<String>();
					List<String> firstdemList = new ArrayList<String>();
					List<String> firstdemLangList = new ArrayList<String>();
					List<String> secondemList = new ArrayList<String>();
					List<String> secondemLangList = new ArrayList<String>();
					List<String> municipalityList = new ArrayList<String>();
					List<String> municipalityLangList = new ArrayList<String>();
					List<String> localentityList = new ArrayList<String>();
					List<String> localentityLangList = new ArrayList<String>();
					List<String> streetList = new ArrayList<String>();
					List<String> streetLangList = new ArrayList<String>();

					List<String> numberPostalAdrressList = new ArrayList<String>();
					List<String> postalCountryList = new ArrayList<String>();
					List<String> postalCountryLangList = new ArrayList<String>();
					List<String> postalMunicipalityList = new ArrayList<String>();
					List<String> postalMunicipalityLangList = new ArrayList<String>();
					List<String> postalStreetList = new ArrayList<String>();
					List<String> postalStreetLangList = new ArrayList<String>();
					if (!repository.getLocation().isEmpty()) {
						for (int j = 0; j < repository.getLocation().size(); j++) {
							Location location = repository.getLocation().get(j);
							if (location.getLocalType() != null
									&& location.getLocalType().equalsIgnoreCase(Eag2012.VISITORS_ADDRESS)) {
								numberVisitorAdrressList.add("");
								// Latitude.
								if (location.getLatitude() != null) {
									latitudeList.add(location.getLatitude());
								} else {
									latitudeList.add("");
								}
								// Longitude.
								if (location.getLongitude() != null) {
									longitudeList.add(location.getLongitude());
								} else {
									longitudeList.add("");
								}
								// Country.
								if (location.getCountry() != null) {
									if (location.getCountry().getContent() != null
											&& !location.getCountry().getContent().isEmpty()) {
										countryList.add(location.getCountry().getContent());
									} else {
										countryList.add("");
									}
									if (location.getCountry().getLang() != null
											&& !location.getCountry().getLang().isEmpty()) {
										countryLangList.add(location.getCountry().getLang());
									} else {
										countryLangList.add(Eag2012.OPTION_NONE);
									}
								} else {
									countryList.add("");
									countryLangList.add(Eag2012.OPTION_NONE);
								}
								// Autonomous community / region.
								if (location.getFirstdem() != null) {
									if (location.getFirstdem().getContent() != null
											&& !location.getFirstdem().getContent().isEmpty()) {
										firstdemList.add(location.getFirstdem().getContent());
									} else {
										firstdemList.add("");
									}
									if (location.getFirstdem().getLang() != null
											&& !location.getFirstdem().getLang().isEmpty()) {
										firstdemLangList.add(location.getFirstdem().getLang());
									} else {
										firstdemLangList.add(Eag2012.OPTION_NONE);
									}
								} else {
									firstdemList.add("");
									firstdemLangList.add(Eag2012.OPTION_NONE);
								}
								//County/local authority.
								if (location.getSecondem() != null) {
									if (location.getSecondem().getContent() != null
											&& !location.getSecondem().getContent().isEmpty()) {
										secondemList.add(location.getSecondem().getContent());
									} else {
										secondemList.add("");
									}
									if (location.getSecondem().getLang() != null
											&& !location.getSecondem().getLang().isEmpty()) {
										secondemLangList.add(location.getSecondem().getLang());
									} else {
										secondemLangList.add(Eag2012.OPTION_NONE);
									}
								} else {
									secondemList.add("");
									secondemLangList.add(Eag2012.OPTION_NONE);
								}
								// City.
								if (location.getMunicipalityPostalcode() != null) {
									if (location.getMunicipalityPostalcode().getContent() != null
											&& !location.getMunicipalityPostalcode().getContent().isEmpty()) {
										municipalityList.add(location.getMunicipalityPostalcode().getContent());
									} else {
										municipalityList.add("");
									}
									if (location.getMunicipalityPostalcode().getLang() != null
											&& !location.getMunicipalityPostalcode().getLang().isEmpty()) {
										municipalityLangList.add(location.getMunicipalityPostalcode().getLang());
									} else {
										municipalityLangList.add(Eag2012.OPTION_NONE);
									}
								} else {
									municipalityList.add("");
									municipalityLangList.add(Eag2012.OPTION_NONE);
								}
								// District/quarter in town.
								if (location.getLocalentity() != null) {
									if (location.getLocalentity().getContent() != null
											&& !location.getLocalentity().getContent().isEmpty()) {
										localentityList.add(location.getLocalentity().getContent());
									} else {
										localentityList.add("");
									}
									if (location.getLocalentity().getLang() != null
											&& !location.getLocalentity().getLang().isEmpty()) {
										localentityLangList.add(location.getLocalentity().getLang());
									} else {
										localentityLangList.add(Eag2012.OPTION_NONE);
									}
								} else {
									localentityList.add("");
									localentityLangList.add(Eag2012.OPTION_NONE);
								}
								// Street.
								if (location.getStreet() != null) {
									if (location.getStreet().getContent() != null
											&& !location.getStreet().getContent().isEmpty()) {
										streetList.add(location.getStreet().getContent());
									} else {
										streetList.add("");
									}
									if (location.getStreet().getLang() != null
											&& !location.getStreet().getLang().isEmpty()) {
										streetLangList.add(location.getStreet().getLang());
									} else {
										streetLangList.add(Eag2012.OPTION_NONE);
									}
								} else {
									streetList.add("");
									streetLangList.add(Eag2012.OPTION_NONE);
								}
							}
	
							if (location.getLocalType()!=null && location.getLocalType().equalsIgnoreCase(Eag2012.POSTAL_ADDRESS)) {
								numberPostalAdrressList.add("");
								// Country.
								if (location.getCountry() != null) {
									if (location.getCountry().getContent() != null
											&& !location.getCountry().getContent().isEmpty()) {
										postalCountryList.add(location.getCountry().getContent());
									} else {
										postalCountryList.add("");
									}
									if (location.getCountry().getLang() != null
											&& !location.getCountry().getLang().isEmpty()) {
										postalCountryLangList.add(location.getCountry().getLang());
									} else {
										postalCountryLangList.add(Eag2012.OPTION_NONE);
									}
								} else {
									postalCountryList.add("");
									postalCountryLangList.add(Eag2012.OPTION_NONE);
								}
								// Postal city.
								if (location.getMunicipalityPostalcode() != null) {
									if (location.getMunicipalityPostalcode().getContent() != null
											&& !location.getMunicipalityPostalcode().getContent().isEmpty()) {
										postalMunicipalityList.add(location.getMunicipalityPostalcode().getContent());
									} else {
										postalMunicipalityList.add("");
									}
									if (location.getMunicipalityPostalcode().getLang() != null
											&& !location.getMunicipalityPostalcode().getLang().isEmpty()) {
										postalMunicipalityLangList.add(location.getMunicipalityPostalcode().getLang());
									} else {
										postalMunicipalityLangList.add(Eag2012.OPTION_NONE);
									}
								} else {
									postalMunicipalityList.add("");
									postalMunicipalityLangList.add(Eag2012.OPTION_NONE);
								}
								// Postal street.
								if (location.getStreet() != null) {
									if (location.getStreet().getContent() != null
											&& !location.getStreet().getContent().isEmpty()) {
										postalStreetList.add(location.getStreet().getContent());
									} else {
										postalStreetList.add("");
									}
									if (location.getStreet().getLang() != null
											&& !location.getStreet().getLang().isEmpty()) {
										postalStreetLangList.add(location.getStreet().getLang());
									} else {
										postalStreetLangList.add(Eag2012.OPTION_NONE);
									}
								} else {
									postalStreetList.add("");
									postalStreetLangList.add(Eag2012.OPTION_NONE);
								}
							}
						}

						// Check language for "Visitor address" element.
						if (!streetLangList.isEmpty()) {
							for (int j = 0; j < streetLangList.size(); j++) {
								if (streetLangList.get(j) != null
										&& streetLangList.get(j).equalsIgnoreCase(Eag2012.OPTION_NONE)) {
									if (!municipalityLangList.isEmpty()
											&& municipalityLangList.size() > j
											&& municipalityLangList.get(j) != null
											&& !municipalityLangList.get(j).equalsIgnoreCase(Eag2012.OPTION_NONE)) {
										streetLangList.remove(j);
										streetLangList.add(j, municipalityLangList.get(j));
									} else if (!countryLangList.isEmpty()
											&& countryLangList.size() > j
											&& countryLangList.get(j) != null
											&& !countryLangList.get(j).equalsIgnoreCase(Eag2012.OPTION_NONE)) {
										streetLangList.remove(j);
										streetLangList.add(j, countryLangList.get(j));
									} else if (!firstdemLangList.isEmpty()
											&& firstdemLangList.size() > j
											&& firstdemLangList.get(j) != null
											&& !firstdemLangList.get(j).equalsIgnoreCase(Eag2012.OPTION_NONE)) {
										streetLangList.remove(j);
										streetLangList.add(j, firstdemLangList.get(j));
									} else if (!secondemLangList.isEmpty()
											&& secondemLangList.size() > j
											&& secondemLangList.get(j) != null
											&& !secondemLangList.get(j).equalsIgnoreCase(Eag2012.OPTION_NONE)) {
										streetLangList.remove(j);
										streetLangList.add(j, secondemLangList.get(j));
									} else if (!localentityLangList.isEmpty()
											&& localentityLangList.size() > j
											&& localentityLangList.get(j) != null
											&& !localentityLangList.get(j).equalsIgnoreCase(Eag2012.OPTION_NONE)) {
										streetLangList.remove(j);
										streetLangList.add(j, localentityLangList.get(j));
									}
								}
							}
						}

						// Check language for "Postal address" element.
						if (!postalStreetLangList.isEmpty()) {
							for (int j = 0; j < postalStreetLangList.size(); j++) {
								if (postalStreetLangList.get(j) != null
										&& postalStreetLangList.get(j).equalsIgnoreCase(Eag2012.OPTION_NONE)) {
									if (!postalMunicipalityLangList.isEmpty()
											&& postalMunicipalityLangList.size() > j
											&& postalMunicipalityLangList.get(j) != null
											&& !postalMunicipalityLangList.get(j).equalsIgnoreCase(Eag2012.OPTION_NONE)) {
										postalStreetLangList.remove(j);
										postalStreetLangList.add(j, postalMunicipalityLangList.get(j));
									}
								}
							}
						}
					}
					this.addContactNumberOfVisitorsAddress(numberVisitorAdrressList);
					this.addContactLatitude(latitudeList);
					this.addContactLongitude(longitudeList);
					this.addContactCountry(countryList);
					this.addContactCountryLang(countryLangList);
					this.addContactFirstdem(firstdemList);
					this.addContactFirstdemLang(firstdemLangList);
					this.addContactSecondem(secondemList);
					this.addContactSecondemLang(secondemLangList);
					this.addContactMunicipality(municipalityList);
					this.addContactMunicipalityLang(municipalityLangList);
					this.addContactLocalentity(localentityList);
					this.addContactLocalentityLang(localentityLangList);
					this.addContactStreet(streetList);
					this.addContactStreetLang(streetLangList);

					// Add the language for "Your institution" tab for element "Visitor address".
					if (i == 0) {
						for (int j = 0; j < streetLangList.size(); j++) {
							if (this.getYiStreetLang().size() > j
									&& this.getYiStreetLang().get(j) != null) {
								this.getYiStreetLang().remove(j);
								this.getYiStreetLang().add(j, streetLangList.get(j));
							} else {
								this.getYiStreetLang().add(streetLangList.get(j));
							}
						}
					}

					this.addContactNumberOfPostalAddress(numberPostalAdrressList);
					this.addContactPostalCountry(postalCountryList);
					this.addContactPostalCountryLang(postalCountryLangList);
					this.addContactPostalMunicipality(postalMunicipalityList);
					this.addContactPostalMunicipalityLang(postalMunicipalityLangList);
					this.addContactPostalStreet(postalStreetList);
					this.addContactPostalStreetLang(postalStreetLangList);

					// Add the language for "Your institution" tab for element "Postal address".
					if (i == 0) {
						for (int j = 0; j < postalStreetLangList.size(); j++) {
							if (this.getYiStreetPostalLang().size() > j
									&& this.getYiStreetPostalLang().get(j) != null) {
								this.getYiStreetPostalLang().remove(j);
								this.getYiStreetPostalLang().add(j, postalStreetLangList.get(j));
							} else {
								this.getYiStreetPostalLang().add(postalStreetLangList.get(j));
							}
						}
					}

					// Continent.
					stringList = new ArrayList<String>();
					if (repository.getGeogarea() != null
							&& repository.getGeogarea().getValue() != null
							&& !repository.getGeogarea().getValue().isEmpty()) {
						stringList.add(getGeogareaString(repository.getGeogarea().getValue()));
					}
					this.addContactContinent(stringList);

					// Telephone.
					stringList = new ArrayList<String>();
					if (!repository.getTelephone().isEmpty()) {
						for (int j = 0; j < repository.getTelephone().size(); j++) {
							if (repository.getTelephone().get(j).getContent() != null
									&& !repository.getTelephone().get(j).getContent().isEmpty()) {
								stringList.add(repository.getTelephone().get(j).getContent());
							} else {
								stringList.add("");
							}
						}
					}
					this.addContactTelephone(stringList);

					// Fax
					stringList = new ArrayList<String>();
					if (!repository.getFax().isEmpty()) {
						for (int j = 0; j < repository.getFax().size(); j++) {
							if (repository.getFax().get(j).getContent() != null
									&& !repository.getFax().get(j).getContent().isEmpty()) {
								stringList.add(repository.getFax().get(j).getContent());
							} else {
								stringList.add("");
							}
						}
					}
					this.addContactFax(stringList);

					// E-mail address.
					List<String> numberEmailList = new ArrayList<String>();
					List<String> emailHrefList = new ArrayList<String>();
					List<String> emailTitleList = new ArrayList<String>();
					List<String> emailLangList = new ArrayList<String>();
					if (!repository.getEmail().isEmpty()) {
						for (int j = 0; j < repository.getEmail().size(); j++) {
							numberEmailList.add("");
							// Href.
							if (repository.getEmail().get(j).getHref() != null
									&& !repository.getEmail().get(j).getHref().isEmpty()) {
								emailHrefList.add(repository.getEmail().get(j).getHref());
							} else {
								emailHrefList.add("");
							}
							// Title.
							if (repository.getEmail().get(j).getContent() != null
									&& !repository.getEmail().get(j).getContent().isEmpty()) {
								emailTitleList.add(repository.getEmail().get(j).getContent());
							} else {
								emailTitleList.add("");
							}
							// Lang.
							if (repository.getEmail().get(j).getLang() != null
									&& !repository.getEmail().get(j).getLang().isEmpty()) {
								emailLangList.add(repository.getEmail().get(j).getLang());
							} else {
								emailLangList.add("");
							}
						}
					}
					this.addContactNumberOfEmailAddress(numberEmailList);
					this.addContactEmailHref(emailHrefList);
					this.addContactEmailTitle(emailTitleList);
					this.addContactEmailLang(emailLangList);

					// Webpage.
					List<String> numberWebpageList = new ArrayList<String>();
					List<String> webpageHrefList = new ArrayList<String>();
					List<String> webpageTitleList = new ArrayList<String>();
					List<String> webpageLangList = new ArrayList<String>();
					for (int j = 0; j < repository.getWebpage().size(); j++) {
						numberWebpageList.add("");
						// Href.
						if (repository.getWebpage().get(j).getHref() != null
								&& !repository.getWebpage().get(j).getHref().isEmpty()) {
							webpageHrefList.add(repository.getWebpage().get(j).getHref());
						} else {
							webpageHrefList.add("");
						}
						// Title.
						if (repository.getWebpage().get(j).getContent() != null
								&& !repository.getWebpage().get(j).getContent().isEmpty()) {
							webpageTitleList.add(repository.getWebpage().get(j).getContent());
						} else {
							webpageTitleList.add("");
						}
						// Lang.
						if (repository.getWebpage().get(j).getLang() != null
								&& !repository.getWebpage().get(j).getLang().isEmpty()) {
							webpageLangList.add(repository.getWebpage().get(j).getLang());
						} else {
							webpageLangList.add("");
						}
					}
					this.addContactNumberOfWebpageAddress(numberWebpageList);
					this.addContactWebpageHref(webpageHrefList);
					this.addContactWebpageTitle(webpageTitleList);
					this.addContactWebpageLang(webpageLangList);
				}
			}
		}
	}

	/**
	 * Method to load all values of "Access and Services" tab.
	 */
	private void loadAccessAndServicesTabValues() {
		// Repositories info.
		if (this.eag.getArchguide() != null && this.eag.getArchguide().getDesc() != null
				&& this.eag.getArchguide().getDesc().getRepositories() != null
				&& !this.eag.getArchguide().getDesc().getRepositories().getRepository().isEmpty()) {
			// For each repository
			for (int i = 0; i < this.eag.getArchguide().getDesc().getRepositories().getRepository().size(); i++) {
				Repository repository = this.eag.getArchguide().getDesc().getRepositories().getRepository().get(i);
				if (repository != null) {
					// Timetable info
					List<String> openingList = new ArrayList<String>();
					List<String> openingLangList = new ArrayList<String>();
					List<String> closingList = new ArrayList<String>();
					List<String> closingLangList = new ArrayList<String>();
					if (repository.getTimetable() != null) {
						Timetable timetable = repository.getTimetable();

						// Opening times.
						if (!timetable.getOpening().isEmpty()) {
							for (int j = 0; j < timetable.getOpening().size(); j++) {
								if (!timetable.getOpening().isEmpty()
										&& timetable.getOpening().size() >= j
										&& timetable.getOpening().get(j).getContent() != null
										&& !timetable.getOpening().get(j).getContent().isEmpty()) {
									openingList.add(timetable.getOpening().get(j).getContent());
								} else {
									openingList.add("");
								}
								if (!timetable.getOpening().isEmpty()
										&& timetable.getOpening().size() >= j
										&& timetable.getOpening().get(j).getLang() != null
										&& !timetable.getOpening().get(j).getLang().isEmpty()) {
									openingLangList.add(timetable.getOpening().get(j).getLang());
								} else {
									openingLangList.add(Eag2012.OPTION_NONE);
								}
							}
						}

						// Closing dates.
						if (!timetable.getClosing().isEmpty()) {
							// Closing dates for institution.
							for (int j = 0; j < timetable.getClosing().size(); j++) {
								if (!timetable.getClosing().isEmpty()
										&& timetable.getClosing().size() >= j
										&& timetable.getClosing().get(j).getContent() != null
										&& !timetable.getClosing().get(j).getContent().isEmpty()) {
									closingList.add(timetable.getClosing().get(j).getContent());
								} else {
									closingList.add("");
								}
								if (!timetable.getClosing().isEmpty()
										&& timetable.getClosing().size() >= j
										&& timetable.getClosing().get(j).getLang() != null
										&& !timetable.getClosing().get(j).getLang().isEmpty()) {
									closingLangList.add(timetable.getClosing().get(j).getLang());
								} else {
									closingLangList.add(Eag2012.OPTION_NONE);
								}
							}
						}
					}
					this.addAsOpening(openingList);
					this.addAsOpeningLang(openingLangList);
					this.addAsClosing(closingList);
					this.addAsClosingLang(closingLangList);

					// Travelling directions.
					List<String> numberOfDirectios = new ArrayList<String>();
					List<String> directiosValue = new ArrayList<String>();
					List<String> directiosLang = new ArrayList<String>();
					List<String> directiosHref = new ArrayList<String>();
					if (!repository.getDirections().isEmpty()) {
						for (int j = 0; j < repository.getDirections().size(); j++) {
							numberOfDirectios.add("");
							if (repository.getDirections().get(j) != null
									&& !repository.getDirections().get(j).getContent().isEmpty()) {
								// TODO: Review for multiple values.
								for (int k = 0; k < repository.getDirections().get(j).getContent().size(); k++) {
									if (repository.getDirections().get(j).getContent().get(k) != null
											&& repository.getDirections().get(j).getContent().get(k) instanceof String
											&& repository.getDirections().get(j).getContent().get(k).toString() != null
											&& !repository.getDirections().get(j).getContent().get(k).toString().isEmpty()
											&& !repository.getDirections().get(j).getContent().get(k).toString().startsWith("\n")) {
										directiosValue.add(repository.getDirections().get(j).getContent().get(k).toString());
									}
									if (repository.getDirections().get(j).getContent().get(k) != null
											&& repository.getDirections().get(j).getContent().get(k) instanceof Citation) {
										Citation citation = (Citation) repository.getDirections().get(j).getContent().get(k);
										if (citation.getHref() != null
												&& !citation.getHref().isEmpty()) {
											directiosHref.add(citation.getHref());
										} else {
											directiosHref.add("");
										}
									}
								}
							}

							// Travelling directions language.
							if (repository.getDirections().get(j) != null
									&& repository.getDirections().get(j).getLang() != null
									&& !repository.getDirections().get(j).getLang().isEmpty()) {
								directiosLang.add(repository.getDirections().get(j).getLang());
							} else {
								directiosLang.add(Eag2012.OPTION_NONE);
							}
						}
					}
					this.addAsNumberOfDirections(numberOfDirectios);
					this.addAsDirections(directiosValue);
					this.addAsDirectionsLang(directiosLang);
					this.addAsDirectionsCitationHref(directiosHref);

					// Accessible to the public.
					List<String> accessQuestion = new ArrayList<String>();
					List<String> accessValue = new ArrayList<String>();
					List<String> accessLang = new ArrayList<String>();
					if (repository.getAccess() != null) {
						if (repository.getAccess().getQuestion() != null
								&& !repository.getAccess().getQuestion().isEmpty()) {
							accessQuestion.add(repository.getAccess().getQuestion());
						} else {
							accessQuestion.add(Eag2012.OPTION_NO);
						}

						if (!repository.getAccess().getRestaccess().isEmpty()) {
							for (int j = 0; j < repository.getAccess().getRestaccess().size(); j++) {
								if (repository.getAccess().getRestaccess().size() >= j
										&& repository.getAccess().getRestaccess().get(j).getContent() != null
										&& !repository.getAccess().getRestaccess().get(j).getContent().isEmpty()) {
									accessValue.add(repository.getAccess().getRestaccess().get(j).getContent());
								} else {
									accessValue.add("");
								}
								if (repository.getAccess().getRestaccess().size() >= j
										&& repository.getAccess().getRestaccess().get(j).getLang() != null
										&& !repository.getAccess().getRestaccess().get(j).getLang().isEmpty()) {
									accessLang.add(repository.getAccess().getRestaccess().get(j).getLang());
								} else {
									accessLang.add(Eag2012.OPTION_NONE);
								}
							}
						}
					}
					this.addAsAccessQuestion(accessQuestion);
					this.addAsRestaccess(accessValue);
					this.addAsRestaccessLang(accessLang);
	
					// Terms of use.
					List<String> numberOfTermsOfUse = new ArrayList<String>();
					List<String> termsOfUseValue = new ArrayList<String>();
					List<String> termsOfUseHref = new ArrayList<String>();
					List<String> termsOfUseLang = new ArrayList<String>();
					if (repository.getAccess() != null
							&& !repository.getAccess().getTermsOfUse().isEmpty()) {
						for (int j = 0; j < repository.getAccess().getTermsOfUse().size(); j++) {
							numberOfTermsOfUse.add("");
							termsOfUseValue.add(repository.getAccess().getTermsOfUse().get(j).getContent());
							termsOfUseLang.add(repository.getAccess().getTermsOfUse().get(j).getLang());
							termsOfUseHref.add(repository.getAccess().getTermsOfUse().get(j).getHref());
						}
					}
					this.addAsNumberOfTermsOfUse(numberOfTermsOfUse);
					this.addAsTermsOfUse(termsOfUseValue);
					this.addAsTermsOfUseHref(termsOfUseHref);
					this.addAsTermsOfUseLang(termsOfUseLang);

					// Facilities for disabled people available.
					List<String> accesibilityQuestion = new ArrayList<String>();
					List<String> accesibilityValue = new ArrayList<String>();
					List<String> accesibilityLang = new ArrayList<String>();
					if (!repository.getAccessibility().isEmpty()) {
						for (int j = 0; j < repository.getAccessibility().size(); j++) {
							if (repository.getAccessibility().size() >= j
									&& repository.getAccessibility().get(j).getQuestion() != null
									&& !repository.getAccessibility().get(j).getQuestion().isEmpty()) {
								accesibilityQuestion.add(repository.getAccessibility().get(j).getQuestion());
							} else {
								accesibilityQuestion.add(Eag2012.OPTION_NO);
							}

							if (repository.getAccessibility().size() >= j
									&& repository.getAccessibility().get(j).getContent() != null
									&& !repository.getAccessibility().get(j).getContent().isEmpty()) {
								accesibilityValue.add(repository.getAccessibility().get(j).getContent());

								if (repository.getAccessibility().size() >= j
										&& repository.getAccessibility().get(j).getLang() != null
										&& !repository.getAccessibility().get(j).getLang().isEmpty()) {
									accesibilityLang.add(repository.getAccessibility().get(j).getLang());
								} else {
									accesibilityLang.add(Eag2012.OPTION_NONE);
								}
							}
						}
					}
					this.addAsAccessibilityQuestion(accesibilityQuestion);
					this.addAsAccessibility(accesibilityValue);
					this.addAsAccessibilityLang(accesibilityLang);
					
					// Searchroom.
					List<String> srTelephone = new ArrayList<String>();
					List<String> srNumberOfEmail = new ArrayList<String>();
					List<String> srEmailHref = new ArrayList<String>();
					List<String> srEmailTitle = new ArrayList<String>();
					List<String> srNumberOfWebpage = new ArrayList<String>();
					List<String> srWebpageHref = new ArrayList<String>();
					List<String> srWebpageTitle = new ArrayList<String>();
					List<String> srWorkingPlaces = new ArrayList<String>();
					List<String> srComputerPlacesNum = new ArrayList<String>();
					List<String> srComputerPlacesValue = new ArrayList<String>();
					List<String> srComputerPlacesLang = new ArrayList<String>();
					List<String> srMicrofilmReadersNum = new ArrayList<String>();
					List<String> srPhotographAllowance = new ArrayList<String>();
					List<String> srNumberOfReadersTicket = new ArrayList<String>();
					List<String> srReadersTicket = new ArrayList<String>();
					List<String> srReadersTicketHref = new ArrayList<String>();
					List<String> srReadersTicketLang = new ArrayList<String>();
					List<String> srNumberOfAdvancedOrders = new ArrayList<String>();
					List<String> srAdvancedOrders = new ArrayList<String>();
					List<String> srAdvancedOrdersHref = new ArrayList<String>();
					List<String> srAdvancedOrdersLang = new ArrayList<String>();
					List<String> srResearchServices = new ArrayList<String>();
					List<String> srResearchServicesLang = new ArrayList<String>();
					if (repository.getServices() != null && repository.getServices().getSearchroom() != null) {
						Searchroom searchRoom = repository.getServices().getSearchroom();
						// Contact.
						if (searchRoom.getContact() != null) {
							// Contact -Telephone.
							if (!searchRoom.getContact().getTelephone().isEmpty()) {
								for (int j = 0; j < searchRoom.getContact().getTelephone().size(); j++) {
									if (searchRoom.getContact().getTelephone().get(j).getContent() != null
											&& !searchRoom.getContact().getTelephone().get(j).getContent().isEmpty()) {
										srTelephone.add(searchRoom.getContact().getTelephone().get(j).getContent());
									} else {
										srTelephone.add("");
									}
								}
							} else {
								srTelephone.add("");
							}
							// Contact -Email.
							if (!searchRoom.getContact().getEmail().isEmpty()) {
								for (int j = 0; j < searchRoom.getContact().getEmail().size(); j++) {
									srNumberOfEmail.add("");
									if (searchRoom.getContact().getEmail().get(j).getHref() != null
											&& !searchRoom.getContact().getEmail().get(j).getHref().isEmpty()) {
										srEmailHref.add(searchRoom.getContact().getEmail().get(j).getHref());
									} else {
										srEmailHref.add("");
									}
									if (searchRoom.getContact().getEmail().get(j).getContent() != null
											&& !searchRoom.getContact().getEmail().get(j).getContent().isEmpty()) {
										srEmailTitle.add(searchRoom.getContact().getEmail().get(j).getContent());
									} else {
										srEmailTitle.add("");
									}
								}
							} else {
								srNumberOfEmail.add("");
								srEmailHref.add("");
								srEmailTitle.add("");
							}
						}

						// Webpage
						if (!searchRoom.getWebpage().isEmpty()) {
							for (int j = 0; j < searchRoom.getWebpage().size(); j++) {
								srNumberOfWebpage.add("");
								if (searchRoom.getWebpage().get(j).getHref() != null
										&& !searchRoom.getWebpage().get(j).getHref().isEmpty()) {
									srWebpageHref.add(searchRoom.getWebpage().get(j).getHref());
								} else {
									srWebpageHref.add("");
								}
								if (searchRoom.getWebpage().get(j).getContent() != null
										&& !searchRoom.getWebpage().get(j).getContent().isEmpty()) {
									srWebpageTitle.add(searchRoom.getWebpage().get(j).getContent());
								} else {
									srWebpageTitle.add("");
								}
							}
						} else {
							srNumberOfWebpage.add("");
							srWebpageHref.add("");
							srWebpageTitle.add("");
						}

						// Number of working places.
						if (searchRoom.getWorkPlaces() !=null
								&& searchRoom.getWorkPlaces().getNum() !=null 
								&& searchRoom.getWorkPlaces().getNum().getContent() != null
								&& !searchRoom.getWorkPlaces().getNum().getContent().isEmpty()) {
							srWorkingPlaces.add(searchRoom.getWorkPlaces().getNum().getContent());
						} else {
							srWorkingPlaces.add("");
						}

						// Number of computer places.
						if (searchRoom.getComputerPlaces() != null
								&& searchRoom.getComputerPlaces().getNum() != null
								&& searchRoom.getComputerPlaces().getNum().getContent() != null
								&& !searchRoom.getComputerPlaces().getNum().getContent().isEmpty()) {
							srComputerPlacesNum.add(searchRoom.getComputerPlaces().getNum().getContent());

							// Description of computer places.
							if (searchRoom.getComputerPlaces().getDescriptiveNote() != null
									&& !searchRoom.getComputerPlaces().getDescriptiveNote().getP().isEmpty()) {
								for (int j = 0; j < searchRoom.getComputerPlaces().getDescriptiveNote().getP().size(); j++) {
									if (searchRoom.getComputerPlaces().getDescriptiveNote().getP().get(j) != null
											&& searchRoom.getComputerPlaces().getDescriptiveNote().getP().get(j).getContent() != null
											&& !searchRoom.getComputerPlaces().getDescriptiveNote().getP().get(j).getContent().isEmpty()) {
										srComputerPlacesValue.add(searchRoom.getComputerPlaces().getDescriptiveNote().getP().get(j).getContent());

										if (searchRoom.getComputerPlaces().getDescriptiveNote().getP().get(j).getLang() != null
												&& !searchRoom.getComputerPlaces().getDescriptiveNote().getP().get(j).getLang().isEmpty()) {
											srComputerPlacesLang.add(searchRoom.getComputerPlaces().getDescriptiveNote().getP().get(j).getLang());
										} else {
											srComputerPlacesLang.add(Eag2012.OPTION_NONE);
										}
									}
								}
							}
						} else {
							srComputerPlacesNum.add("");
						}

						// Number of microfilm/fiche readers.
						if (searchRoom.getMicrofilmPlaces() != null
								&& searchRoom.getMicrofilmPlaces().getNum() != null
								&& searchRoom.getMicrofilmPlaces().getNum().getContent() != null
								&& !searchRoom.getMicrofilmPlaces().getNum().getContent().isEmpty()) {
							srMicrofilmReadersNum.add(searchRoom.getMicrofilmPlaces().getNum().getContent());
						} else {
							srMicrofilmReadersNum.add("");
						}

						// Photograph allowance.
						if (searchRoom.getPhotographAllowance() != null
								&& searchRoom.getPhotographAllowance().getValue() != null
								&& !searchRoom.getPhotographAllowance().getValue().isEmpty()) {
							String photographAllowanceValue = searchRoom.getPhotographAllowance().getValue();
							if (Eag2012.OPTION_DEPENDING_TEXT.equalsIgnoreCase(photographAllowanceValue)) {
								photographAllowanceValue = Eag2012.OPTION_DEPENDING;
							} else if (Eag2012.OPTION_WITHOUT_TEXT.equalsIgnoreCase(photographAllowanceValue)) {
								photographAllowanceValue = Eag2012.OPTION_WITHOUT;
							}
	
							srPhotographAllowance.add(photographAllowanceValue);
						} else {
							srPhotographAllowance.add(Eag2012.OPTION_NONE);
						}

						// Readerʼs ticket.
						if (!searchRoom.getReadersTicket().isEmpty()) {
							for (int j = 0; j < searchRoom.getReadersTicket().size(); j++) {
								srNumberOfReadersTicket.add("");
								if (searchRoom.getReadersTicket().get(j) != null
										&& searchRoom.getReadersTicket().get(j).getContent() != null
										&& !searchRoom.getReadersTicket().get(j).getContent().isEmpty()) {
									srReadersTicket.add(searchRoom.getReadersTicket().get(j).getContent());
								} else {
									srReadersTicket.add("");
								}

								if (searchRoom.getReadersTicket().get(j) != null
										&& searchRoom.getReadersTicket().get(j).getHref() != null
										&& !searchRoom.getReadersTicket().get(j).getHref().isEmpty()) {
									srReadersTicketHref.add(searchRoom.getReadersTicket().get(j).getHref());
								} else {
									srReadersTicketHref.add("");
								}

								if (searchRoom.getReadersTicket().get(j) != null
										&& searchRoom.getReadersTicket().get(j).getLang() != null
										&& !searchRoom.getReadersTicket().get(j).getLang().isEmpty()) {
									srReadersTicketLang.add(searchRoom.getReadersTicket().get(j).getLang());
								} else {
									srReadersTicketLang.add("");
								}
							}
						} else {
							srNumberOfReadersTicket.add("");
							srReadersTicket.add("");
							srReadersTicketHref.add("");
							srReadersTicketLang.add(Eag2012.OPTION_NONE);
						}

						// Advanced orders.
						if (!searchRoom.getAdvancedOrders().isEmpty()) {
							for (int j = 0; j < searchRoom.getAdvancedOrders().size(); j++) {
								srNumberOfAdvancedOrders.add("");
								if (searchRoom.getAdvancedOrders().get(j) != null
										&& searchRoom.getAdvancedOrders().get(j).getContent() != null
										&& !searchRoom.getAdvancedOrders().get(j).getContent().isEmpty()) {
									srAdvancedOrders.add(searchRoom.getAdvancedOrders().get(j).getContent());
								} else {
									srAdvancedOrders.add("");
								}

								if (searchRoom.getAdvancedOrders().get(j) != null
										&& searchRoom.getAdvancedOrders().get(j).getHref() != null
										&& !searchRoom.getAdvancedOrders().get(j).getHref().isEmpty()) {
									srAdvancedOrdersHref.add(searchRoom.getAdvancedOrders().get(j).getHref());
								} else {
									srAdvancedOrdersHref.add("");
								}

								if (searchRoom.getAdvancedOrders().get(j) != null
										&& searchRoom.getAdvancedOrders().get(j).getLang() != null
										&& !searchRoom.getAdvancedOrders().get(j).getLang().isEmpty()) {
									srAdvancedOrdersLang.add(searchRoom.getAdvancedOrders().get(j).getLang());
								} else {
									srAdvancedOrdersLang.add("");
								}
							}
						} else {
							srNumberOfAdvancedOrders.add("");
							srAdvancedOrders.add("");
							srAdvancedOrdersHref.add("");
							srAdvancedOrdersLang.add(Eag2012.OPTION_NONE);
						}

						// Research services.
						if (!searchRoom.getResearchServices().isEmpty()) {
							for (int j = 0; j < searchRoom.getResearchServices().size(); j++) {
								if (searchRoom.getResearchServices().get(j) != null
										&& searchRoom.getResearchServices().get(j).getDescriptiveNote() != null
										&& !searchRoom.getResearchServices().get(j).getDescriptiveNote().getP().isEmpty()) {
									for(int k = 0; k < searchRoom.getResearchServices().get(j).getDescriptiveNote().getP().size(); k++) {
										if (searchRoom.getResearchServices().get(j).getDescriptiveNote().getP().get(k) != null
												&& searchRoom.getResearchServices().get(j).getDescriptiveNote().getP().get(k).getContent() != null
												&& !searchRoom.getResearchServices().get(j).getDescriptiveNote().getP().get(k).getContent().isEmpty()) {
											srResearchServices.add(searchRoom.getResearchServices().get(j).getDescriptiveNote().getP().get(k).getContent());
										} else {
											srResearchServices.add("");
										}

										if (searchRoom.getResearchServices().get(j).getDescriptiveNote().getP().get(k) != null
												&& searchRoom.getResearchServices().get(j).getDescriptiveNote().getP().get(k).getLang() != null
												&& !searchRoom.getResearchServices().get(j).getDescriptiveNote().getP().get(k).getLang().isEmpty()) {
											srResearchServicesLang.add(searchRoom.getResearchServices().get(j).getDescriptiveNote().getP().get(k).getLang());
										} else {
											srResearchServicesLang.add("");
										}
									}
								} else {
									srResearchServices.add("");
									srResearchServicesLang.add(Eag2012.OPTION_NONE);
								}
							}
						} else {
							srResearchServices.add("");
							srResearchServicesLang.add(Eag2012.OPTION_NONE);
						}
					}
					this.addAsSearchRoomTelephone(srTelephone);
					this.addAsSearchRoomNumberOfEmail(srNumberOfEmail);
					this.addAsSearchRoomEmailHref(srEmailHref);
					this.addAsSearchRoomEmailTitle(srEmailTitle);
					this.addAsSearchRoomNumberOfWebpage(srNumberOfWebpage);
					this.addAsSearchRoomWebpageHref(srWebpageHref);
					this.addAsSearchRoomWebpageTitle(srWebpageTitle);
					this.addAsSearchRoomWorkPlaces(srWorkingPlaces);
					this.addAsSearchRoomComputerPlaces(srComputerPlacesNum);
					this.addAsSearchRoomComputerPlacesDescription(srComputerPlacesValue);
					this.addAsSearchRoomComputerPlacesDescriptionLang(srComputerPlacesLang);
					this.addAsSearchRoomMicrofilmReaders(srMicrofilmReadersNum);
					this.addAsSearchRoomPhotographAllowance(srPhotographAllowance);
					this.addAsSearchRoomNumberOfReadersTicket(srNumberOfReadersTicket);
					this.addAsSearchRoomReadersTicketContent(srReadersTicket);
					this.addAsSearchRoomReadersTicketHref(srReadersTicketHref);
					this.addAsSearchRoomReadersTicketLang(srReadersTicketLang);
					this.addAsSearchRoomNumberOfAdvancedOrders(srNumberOfAdvancedOrders);
					this.addAsSearchRoomAdvancedOrdersContent(srAdvancedOrders);
					this.addAsSearchRoomAdvancedOrdersHref(srAdvancedOrdersHref);
					this.addAsSearchRoomAdvancedOrdersLang(srAdvancedOrdersLang);
					this.addAsSearchRoomResearchServicesContent(srResearchServices);
					this.addAsSearchRoomResearchServicesLang(srResearchServicesLang);
					
					// Library
					List<String> libQuestion = new ArrayList<String>();
					List<String> libTelephone = new ArrayList<String>();
					List<String> libNumberOfEmail = new ArrayList<String>();
					List<String> libEmailHref = new ArrayList<String>();
					List<String> libEmailTitle = new ArrayList<String>();
					List<String> libNumberOfWebpage = new ArrayList<String>();
					List<String> libWebpageHref = new ArrayList<String>();
					List<String> libWebpageTitle = new ArrayList<String>();
					List<String> libMonographicPubNum = new ArrayList<String>();
					List<String> libSerialPubNum = new ArrayList<String>();
					if (repository.getServices() != null
							&& repository.getServices().getLibrary() != null) {
						Library library = repository.getServices().getLibrary();

						// Question.
						if (library.getQuestion() != null
								&& !library.getQuestion().isEmpty()) {
							libQuestion.add(library.getQuestion());
						} else {
							libQuestion.add(Eag2012.OPTION_NONE);
						}

						// Contact.
						if (library.getContact() != null) {
							if (!library.getContact().getTelephone().isEmpty()) {
								for (int j = 0; j < library.getContact().getTelephone().size(); j++) {
									if (library.getContact().getTelephone().get(j) != null
											&& library.getContact().getTelephone().get(j).getContent() != null
											&& !library.getContact().getTelephone().get(j).getContent().isEmpty()) {
										libTelephone.add(library.getContact().getTelephone().get(j).getContent());
									} else {
										libTelephone.add("");
									}
								}
							} else {
								libTelephone.add("");
							}

							if (!library.getContact().getEmail().isEmpty()) {
								for (int j = 0; j < library.getContact().getEmail().size(); j++) {
									libNumberOfEmail.add("");
									if (library.getContact().getEmail().get(j) != null
											&& library.getContact().getEmail().get(j).getContent() != null
											&& !library.getContact().getEmail().get(j).getContent().isEmpty()) {
										libEmailTitle.add(library.getContact().getEmail().get(j).getContent());
									} else {
										libEmailTitle.add("");
									}

									if (library.getContact().getEmail().get(j) != null
											&& library.getContact().getEmail().get(j).getHref() != null
											&& !library.getContact().getEmail().get(j).getHref().isEmpty()) {
										libEmailHref.add(library.getContact().getEmail().get(j).getHref());
									} else {
										libEmailHref.add("");
									}
								}
							} else {
								libNumberOfEmail.add("");
								libEmailHref.add("");
								libEmailTitle.add("");
							}
						} else {
							libTelephone.add("");
							libNumberOfEmail.add("");
							libEmailHref.add("");
							libEmailTitle.add("");
						}

						// Webpage.
						if (!library.getWebpage().isEmpty()){
							for (int j = 0; j < library.getWebpage().size(); j++) {
								libNumberOfWebpage.add("");
								if (library.getWebpage().get(j) != null
										&& library.getWebpage().get(j).getContent() != null
										&& !library.getWebpage().get(j).getContent().isEmpty()) {
									libWebpageTitle.add(library.getWebpage().get(j).getContent());
								} else {
									libWebpageTitle.add("");
								}

								if (library.getWebpage().get(j) != null
										&& library.getWebpage().get(j).getHref() != null
										&& !library.getWebpage().get(j).getHref().isEmpty()) {
									libWebpageHref.add(library.getWebpage().get(j).getHref());
								} else {
									libWebpageHref.add("");
								}
							}
						} else {
							libNumberOfWebpage.add("");
							libWebpageHref.add("");
							libWebpageTitle.add("");
						}

						// Monographic publications.
						if (library.getMonographicpub() != null
								&& library.getMonographicpub().getNum() != null
								&& library.getMonographicpub().getNum().getContent() != null
								&& !library.getMonographicpub().getNum().getContent().isEmpty()) {
							libMonographicPubNum.add(library.getMonographicpub().getNum().getContent());
						} else {
							libMonographicPubNum.add("");
						}

						// Serial publications.
						if (library.getSerialpub() != null
								&& library.getSerialpub().getNum() != null
								&& library.getSerialpub().getNum().getContent() != null
								&& !library.getSerialpub().getNum().getContent().isEmpty()) {
							libSerialPubNum.add(library.getSerialpub().getNum().getContent());
						} else {
							libSerialPubNum.add("");
						}
					}
					this.addAsLibraryQuestion(libQuestion);
					this.addAsLibraryTelephone(libTelephone);
					this.addAsLibraryNumberOfEmail(libNumberOfEmail);
					this.addAsLibraryEmailHref(libEmailHref);
					this.addAsLibraryEmailTitle(libEmailTitle);
					this.addAsLibraryNumberOfWebpage(libNumberOfWebpage);
					this.addAsLibraryWebpageHref(libWebpageHref);
					this.addAsLibraryWebpageTitle(libWebpageTitle);
					this.addAsLibraryMonographPublication(libMonographicPubNum);
					this.addAsLibrarySerialPublication(libSerialPubNum);

					// Internet access.
					List<String> iaQuestion = new ArrayList<String>();
					List<String> iaValue = new ArrayList<String>();
					List<String> iaLang = new ArrayList<String>();
					if(repository.getServices() != null
							&& repository.getServices().getInternetAccess() != null) {
						if (repository.getServices().getInternetAccess().getQuestion() != null
								&& !repository.getServices().getInternetAccess().getQuestion().isEmpty()) {
							iaQuestion.add(repository.getServices().getInternetAccess().getQuestion());
						} else {
							iaQuestion.add(Eag2012.OPTION_NONE);
						}

						if (repository.getServices().getInternetAccess().getDescriptiveNote() != null
								&& !repository.getServices().getInternetAccess().getDescriptiveNote().getP().isEmpty()) {
							for (int j = 0; j < repository.getServices().getInternetAccess().getDescriptiveNote().getP().size(); j++) {
								if (repository.getServices().getInternetAccess().getDescriptiveNote().getP().get(j) != null
										&& repository.getServices().getInternetAccess().getDescriptiveNote().getP().get(j).getContent() != null
										&& !repository.getServices().getInternetAccess().getDescriptiveNote().getP().get(j).getContent().isEmpty()) {
									iaValue.add(repository.getServices().getInternetAccess().getDescriptiveNote().getP().get(j).getContent());
								} else {
									iaValue.add("");
								}

								if (repository.getServices().getInternetAccess().getDescriptiveNote().getP().get(j) != null
										&& repository.getServices().getInternetAccess().getDescriptiveNote().getP().get(j).getLang() != null
										&& !repository.getServices().getInternetAccess().getDescriptiveNote().getP().get(j).getLang().isEmpty()) {
									iaLang.add(repository.getServices().getInternetAccess().getDescriptiveNote().getP().get(j).getLang());
								} else {
									iaLang.add("");
								}
							}
						} else {
							iaValue.add("");
							iaLang.add("");
						}
					}
					this.addAsInternetAccessQuestion(iaQuestion);
					this.addAsInternetAccessDescription(iaValue);
					this.addAsInternetAccessDescriptionLang(iaLang);

					// Technical services.
					// Restoration laboratory.
					List<String> tsRLQuestion = new ArrayList<String>();
					List<String> tsRLValue = new ArrayList<String>();
					List<String> tsRLValueLang = new ArrayList<String>();
					List<String> tsRLTelephone = new ArrayList<String>();
					List<String> tsRLNumberOfEmail = new ArrayList<String>();
					List<String> tsRLEmailHref = new ArrayList<String>();
					List<String> tsRLEmailTitle = new ArrayList<String>();
					List<String> tsRLNumberOfWebpage = new ArrayList<String>();
					List<String> tsRLWebpageHref = new ArrayList<String>();
					List<String> tsRLWebpageTitle = new ArrayList<String>();
					if (repository.getServices() != null
							&& repository.getServices().getTechservices() != null
							&& repository.getServices().getTechservices().getRestorationlab() != null) {
						// Question.
						if (repository.getServices().getTechservices().getRestorationlab().getQuestion() != null
								&& !repository.getServices().getTechservices().getRestorationlab().getQuestion().isEmpty()) {
							tsRLQuestion.add(repository.getServices().getTechservices().getRestorationlab().getQuestion());
						} else {
							tsRLQuestion.add(Eag2012.OPTION_NONE);
						}

						// Description.
						if (repository.getServices().getTechservices().getRestorationlab().getDescriptiveNote() != null
								&& !repository.getServices().getTechservices().getRestorationlab().getDescriptiveNote().getP().isEmpty()) {
							for (int j = 0; j < repository.getServices().getTechservices().getRestorationlab().getDescriptiveNote().getP().size(); j++) {
								if (repository.getServices().getTechservices().getRestorationlab().getDescriptiveNote().getP().get(j) != null
										&& repository.getServices().getTechservices().getRestorationlab().getDescriptiveNote().getP().get(j).getContent() != null
										&& !repository.getServices().getTechservices().getRestorationlab().getDescriptiveNote().getP().get(j).getContent().isEmpty()) {
									tsRLValue.add(repository.getServices().getTechservices().getRestorationlab().getDescriptiveNote().getP().get(j).getContent());

									if (repository.getServices().getTechservices().getRestorationlab().getDescriptiveNote().getP().get(j).getLang() != null
											&& !repository.getServices().getTechservices().getRestorationlab().getDescriptiveNote().getP().get(j).getLang().isEmpty()) {
										tsRLValueLang.add(repository.getServices().getTechservices().getRestorationlab().getDescriptiveNote().getP().get(j).getLang());
									} else {
										tsRLValueLang.add(Eag2012.OPTION_NONE);
									}
								} else {
									tsRLValue.add("");
									tsRLValueLang.add(Eag2012.OPTION_NONE);
								}
							}
						} else {
							tsRLValue.add("");
							tsRLValueLang.add(Eag2012.OPTION_NONE);
						}
						

						// Contact.
						if (repository.getServices().getTechservices().getRestorationlab().getContact() != null) {
							if (!repository.getServices().getTechservices().getRestorationlab().getContact().getTelephone().isEmpty()) {
								for (int j = 0; j < repository.getServices().getTechservices().getRestorationlab().getContact().getTelephone().size(); j++) {
									if (repository.getServices().getTechservices().getRestorationlab().getContact().getTelephone().get(j) != null
											&& repository.getServices().getTechservices().getRestorationlab().getContact().getTelephone().get(j).getContent() != null
											&& !repository.getServices().getTechservices().getRestorationlab().getContact().getTelephone().get(j).getContent().isEmpty()) {
										tsRLTelephone.add(repository.getServices().getTechservices().getRestorationlab().getContact().getTelephone().get(j).getContent());
									} else {
										tsRLTelephone.add("");
									}
								}
							} else {
								tsRLTelephone.add("");
							}

							if (!repository.getServices().getTechservices().getRestorationlab().getContact().getEmail().isEmpty()) {
								for (int j = 0; j < repository.getServices().getTechservices().getRestorationlab().getContact().getEmail().size(); j++) {
									tsRLNumberOfEmail.add("");
									if (repository.getServices().getTechservices().getRestorationlab().getContact().getEmail().get(j) != null
											&& repository.getServices().getTechservices().getRestorationlab().getContact().getEmail().get(j).getContent() != null
											&& !repository.getServices().getTechservices().getRestorationlab().getContact().getEmail().get(j).getContent().isEmpty()) {
										tsRLEmailTitle.add(repository.getServices().getTechservices().getRestorationlab().getContact().getEmail().get(j).getContent());
									} else {
										tsRLEmailTitle.add("");
									}

									if (repository.getServices().getTechservices().getRestorationlab().getContact().getEmail().get(j) != null
											&& repository.getServices().getTechservices().getRestorationlab().getContact().getEmail().get(j).getHref() != null
											&& !repository.getServices().getTechservices().getRestorationlab().getContact().getEmail().get(j).getHref().isEmpty()) {
										tsRLEmailHref.add(repository.getServices().getTechservices().getRestorationlab().getContact().getEmail().get(j).getHref());
									} else {
										tsRLEmailHref.add("");
									}
								}
							} else {
								tsRLNumberOfEmail.add("");
								tsRLEmailHref.add("");
								tsRLEmailTitle.add("");
							}
						} else {
							tsRLNumberOfEmail.add("");
							tsRLEmailHref.add("");
							tsRLEmailTitle.add("");
							tsRLTelephone.add("");
						}

						// Webpage
						if (!repository.getServices().getTechservices().getRestorationlab().getWebpage().isEmpty()) {
							for (int j = 0; j < repository.getServices().getTechservices().getRestorationlab().getWebpage().size(); j++) {
								tsRLNumberOfWebpage.add("");
								if (repository.getServices().getTechservices().getRestorationlab().getWebpage().get(j) != null
										&& repository.getServices().getTechservices().getRestorationlab().getWebpage().get(j).getContent() != null
										&& !repository.getServices().getTechservices().getRestorationlab().getWebpage().get(j).getContent().isEmpty()) {
									tsRLWebpageTitle.add(repository.getServices().getTechservices().getRestorationlab().getWebpage().get(j).getContent());
								} else {
									tsRLWebpageTitle.add("");
								}

								if (repository.getServices().getTechservices().getRestorationlab().getWebpage().get(j) != null
										&& repository.getServices().getTechservices().getRestorationlab().getWebpage().get(j).getHref() != null
										&& !repository.getServices().getTechservices().getRestorationlab().getWebpage().get(j).getHref().isEmpty()) {
									tsRLWebpageHref.add(repository.getServices().getTechservices().getRestorationlab().getWebpage().get(j).getHref());
								} else {
									tsRLWebpageHref.add("");
								}
							}
						} else {
							tsRLNumberOfWebpage.add("");
							tsRLWebpageHref.add("");
							tsRLWebpageTitle.add("");
						}
					}
					this.addAsRestorationlabQuestion(tsRLQuestion);
					this.addAsRestorationlabDescription(tsRLValue);
					this.addAsRestorationlabDescriptionLang(tsRLValueLang);
					this.addAsRestorationlabTelephone(tsRLTelephone);
					this.addAsRestorationlabNumberOfEmail(tsRLNumberOfEmail);
					this.addAsRestorationlabEmailHref(tsRLEmailHref);
					this.addAsRestorationlabEmailTitle(tsRLEmailTitle);
					this.addAsRestorationlabNumberOfWebpage(tsRLNumberOfWebpage);
					this.addAsRestorationlabWebpageHref(tsRLWebpageHref);
					this.addAsRestorationlabWebpageTitle(tsRLWebpageTitle);

					// Reproduction services.
					List<String> tsRSQuestion = new ArrayList<String>();
					List<String> tsRSValue = new ArrayList<String>();
					List<String> tsRSValueLang = new ArrayList<String>();
					List<String> tsRSTelephone = new ArrayList<String>();
					List<String> tsRSNumberOfEmail = new ArrayList<String>();
					List<String> tsRSEmailHref = new ArrayList<String>();
					List<String> tsRSEmailTitle = new ArrayList<String>();
					List<String> tsRSNumberOfWebpage = new ArrayList<String>();
					List<String> tsRSWebpageHref = new ArrayList<String>();
					List<String> tsRSWebpageTitle = new ArrayList<String>();
					List<String> tsRSMicroformser = new ArrayList<String>();
					List<String> tsRSPhotographser = new ArrayList<String>();
					List<String> tsRSDigitalser = new ArrayList<String>();
					List<String> tsRSPhotocopyser = new ArrayList<String>();
					if (repository.getServices() != null
							&& repository.getServices().getTechservices() != null
							&& repository.getServices().getTechservices().getReproductionser() != null) {
						// Question.
						if (repository.getServices().getTechservices().getReproductionser().getQuestion() != null
								&& !repository.getServices().getTechservices().getReproductionser().getQuestion().isEmpty()) {
							tsRSQuestion.add(repository.getServices().getTechservices().getReproductionser().getQuestion());
						} else {
							tsRSQuestion.add(Eag2012.OPTION_NONE);
						}

						// Description.
						if (repository.getServices().getTechservices().getReproductionser().getDescriptiveNote() != null
								&& !repository.getServices().getTechservices().getReproductionser().getDescriptiveNote().getP().isEmpty()) {
							for (int j = 0; j < repository.getServices().getTechservices().getReproductionser().getDescriptiveNote().getP().size(); j++) {
								if (repository.getServices().getTechservices().getReproductionser().getDescriptiveNote().getP().get(j) != null
										&& repository.getServices().getTechservices().getReproductionser().getDescriptiveNote().getP().get(j).getContent() != null
										&& !repository.getServices().getTechservices().getReproductionser().getDescriptiveNote().getP().get(j).getContent().isEmpty()) {
									tsRSValue.add(repository.getServices().getTechservices().getReproductionser().getDescriptiveNote().getP().get(j).getContent());

									if (repository.getServices().getTechservices().getReproductionser().getDescriptiveNote().getP().get(j).getLang() != null
											&& !repository.getServices().getTechservices().getReproductionser().getDescriptiveNote().getP().get(j).getLang().isEmpty()) {
										tsRSValueLang.add(repository.getServices().getTechservices().getReproductionser().getDescriptiveNote().getP().get(j).getLang());
									} else {
										tsRSValueLang.add(Eag2012.OPTION_NONE);
									}
								} else {
									tsRSValue.add("");
									tsRSValueLang.add(Eag2012.OPTION_NONE);
								}
							}
						} else {
							tsRSValue.add("");
							tsRSValueLang.add(Eag2012.OPTION_NONE);
						}

						// Contact.
						if (repository.getServices().getTechservices().getReproductionser().getContact() != null) {
							if (!repository.getServices().getTechservices().getReproductionser().getContact().getTelephone().isEmpty()) {
								for (int j = 0; j < repository.getServices().getTechservices().getReproductionser().getContact().getTelephone().size(); j++) {
									if (repository.getServices().getTechservices().getReproductionser().getContact().getTelephone().get(j) != null
											&& repository.getServices().getTechservices().getReproductionser().getContact().getTelephone().get(j).getContent() != null
											&& !repository.getServices().getTechservices().getReproductionser().getContact().getTelephone().get(j).getContent().isEmpty()) {
										tsRSTelephone.add(repository.getServices().getTechservices().getReproductionser().getContact().getTelephone().get(j).getContent());
									} else {
										tsRSTelephone.add("");
									}
								}
							} else {
								tsRSTelephone.add("");
							}

							if (!repository.getServices().getTechservices().getReproductionser().getContact().getEmail().isEmpty()) {
								for (int j = 0; j < repository.getServices().getTechservices().getReproductionser().getContact().getEmail().size(); j++) {
									tsRSNumberOfEmail.add("");
									if (repository.getServices().getTechservices().getReproductionser().getContact().getEmail().get(j) != null
											&& repository.getServices().getTechservices().getReproductionser().getContact().getEmail().get(j).getContent() != null
											&& !repository.getServices().getTechservices().getReproductionser().getContact().getEmail().get(j).getContent().isEmpty()) {
										tsRSEmailTitle.add(repository.getServices().getTechservices().getReproductionser().getContact().getEmail().get(j).getContent());
									} else {
										tsRSEmailTitle.add("");
									}

									if (repository.getServices().getTechservices().getReproductionser().getContact().getEmail().get(j) != null
											&& repository.getServices().getTechservices().getReproductionser().getContact().getEmail().get(j).getHref() != null
											&& !repository.getServices().getTechservices().getReproductionser().getContact().getEmail().get(j).getHref().isEmpty()) {
										tsRSEmailHref.add(repository.getServices().getTechservices().getReproductionser().getContact().getEmail().get(j).getHref());
									} else {
										tsRSEmailHref.add("");
									}
								}
							} else {
								tsRSNumberOfEmail.add("");
								tsRSEmailHref.add("");
								tsRSEmailTitle.add("");
							}
						} else {
							tsRSNumberOfEmail.add("");
							tsRSEmailHref.add("");
							tsRSEmailTitle.add("");
							tsRSTelephone.add("");
						}

						// Webpage
						if (!repository.getServices().getTechservices().getReproductionser().getWebpage().isEmpty()) {
							for (int j = 0; j < repository.getServices().getTechservices().getReproductionser().getWebpage().size(); j++) {
								tsRSNumberOfWebpage.add("");
								if (repository.getServices().getTechservices().getReproductionser().getWebpage().get(j) != null
										&& repository.getServices().getTechservices().getReproductionser().getWebpage().get(j).getContent() != null
										&& !repository.getServices().getTechservices().getReproductionser().getWebpage().get(j).getContent().isEmpty()) {
									tsRSWebpageTitle.add(repository.getServices().getTechservices().getReproductionser().getWebpage().get(j).getContent());
								} else {
									tsRSWebpageTitle.add("");
								}

								if (repository.getServices().getTechservices().getReproductionser().getWebpage().get(j) != null
										&& repository.getServices().getTechservices().getReproductionser().getWebpage().get(j).getHref() != null
										&& !repository.getServices().getTechservices().getReproductionser().getWebpage().get(j).getHref().isEmpty()) {
									tsRSWebpageHref.add(repository.getServices().getTechservices().getReproductionser().getWebpage().get(j).getHref());
								} else {
									tsRSWebpageHref.add("");
								}
							}
						} else {
							tsRSNumberOfWebpage.add("");
							tsRSWebpageHref.add("");
							tsRSWebpageTitle.add("");
						}

						// Microformser.
						if (repository.getServices().getTechservices().getReproductionser().getMicroformser() != null
								&& repository.getServices().getTechservices().getReproductionser().getMicroformser().getQuestion() != null
								&& !repository.getServices().getTechservices().getReproductionser().getMicroformser().getQuestion().isEmpty()) {
							tsRSMicroformser.add(repository.getServices().getTechservices().getReproductionser().getMicroformser().getQuestion());
						} else {
							tsRSMicroformser.add(Eag2012.OPTION_NONE);
						}

						// Photographser.
						if (repository.getServices().getTechservices().getReproductionser().getPhotographser() != null
								&& repository.getServices().getTechservices().getReproductionser().getPhotographser().getQuestion() != null
								&& !repository.getServices().getTechservices().getReproductionser().getPhotographser().getQuestion().isEmpty()) {
							tsRSPhotographser.add(repository.getServices().getTechservices().getReproductionser().getPhotographser().getQuestion());
						} else {
							tsRSPhotographser.add(Eag2012.OPTION_NONE);
						}

						// Digitalser.
						if (repository.getServices().getTechservices().getReproductionser().getDigitalser() != null
								&& repository.getServices().getTechservices().getReproductionser().getDigitalser().getQuestion() != null
								&& !repository.getServices().getTechservices().getReproductionser().getDigitalser().getQuestion().isEmpty()) {
							tsRSDigitalser.add(repository.getServices().getTechservices().getReproductionser().getDigitalser().getQuestion());
						} else {
							tsRSDigitalser.add(Eag2012.OPTION_NONE);
						}

						// Photocopyser.
						if (repository.getServices().getTechservices().getReproductionser().getPhotocopyser() != null
								&& repository.getServices().getTechservices().getReproductionser().getPhotocopyser().getQuestion() != null
								&& !repository.getServices().getTechservices().getReproductionser().getPhotocopyser().getQuestion().isEmpty()) {
							tsRSPhotocopyser.add(repository.getServices().getTechservices().getReproductionser().getPhotocopyser().getQuestion());
						} else {
							tsRSPhotocopyser.add(Eag2012.OPTION_NONE);
						}
					}
					this.addAsReproductionserQuestion(tsRSQuestion);
					this.addAsReproductionserDescription(tsRSValue);
					this.addAsReproductionserDescriptionLang(tsRSValueLang);
					this.addAsReproductionserTelephone(tsRSTelephone);
					this.addAsReproductionserNumberOfEmail(tsRSNumberOfEmail);
					this.addAsReproductionserEmailHref(tsRSEmailHref);
					this.addAsReproductionserEmailTitle(tsRSEmailTitle);
					this.addAsReproductionserNumberOfWebpage(tsRSNumberOfWebpage);
					this.addAsReproductionserWebpageHref(tsRSWebpageHref);
					this.addAsReproductionserWebpageTitle(tsRSWebpageTitle);
					this.addAsReproductionserMicrofilmServices(tsRSMicroformser);
					this.addAsReproductionserPhotographicServices(tsRSPhotographser);
					this.addAsReproductionserDigitisationServices(tsRSDigitalser);
					this.addAsReproductionserPhotocopyingServices(tsRSPhotocopyser);

					// Recreational services
					List<String> rsRefreshment = new ArrayList<String>();
					List<String> rsRefreshmentLang = new ArrayList<String>();
					List<String> rsNumberOfExhibitions = new ArrayList<String>();
					List<String> rsExhibition = new ArrayList<String>();
					List<String> rsExhibitionLang = new ArrayList<String>();
					List<String> rsExhibitionWebpage = new ArrayList<String>();
					List<String> rsExhibitionWebpageTitle = new ArrayList<String>();
					List<String> rsNumberOfToursSessions = new ArrayList<String>();
					List<String> rsToursSessions = new ArrayList<String>();
					List<String> rsToursSessionsLang = new ArrayList<String>();
					List<String> rsToursSessionsWebpage = new ArrayList<String>();
					List<String> rsToursSessionsWebpageTitle = new ArrayList<String>();
					List<String> rsNumberOfOtherServices = new ArrayList<String>();
					List<String> rsOtherServices = new ArrayList<String>();
					List<String> rsOtherServicesLang = new ArrayList<String>();
					List<String> rsOtherServicesWebpage = new ArrayList<String>();
					List<String> rsOtherServicesWebpageTitle = new ArrayList<String>();
					if (repository.getServices() != null
							&& repository.getServices().getRecreationalServices() != null) {
						// Refresment area.
						if (repository.getServices().getRecreationalServices().getRefreshment() != null
								&& repository.getServices().getRecreationalServices().getRefreshment().getDescriptiveNote() != null
								&& !repository.getServices().getRecreationalServices().getRefreshment().getDescriptiveNote().getP().isEmpty()) {
							for (int j = 0; j < repository.getServices().getRecreationalServices().getRefreshment().getDescriptiveNote().getP().size(); j++) {
								if (repository.getServices().getRecreationalServices().getRefreshment().getDescriptiveNote().getP().get(j) != null
										&& repository.getServices().getRecreationalServices().getRefreshment().getDescriptiveNote().getP().get(j).getContent() != null
										&& !repository.getServices().getRecreationalServices().getRefreshment().getDescriptiveNote().getP().get(j).getContent().isEmpty()) {
									rsRefreshment.add(repository.getServices().getRecreationalServices().getRefreshment().getDescriptiveNote().getP().get(j).getContent());
								} else {
									rsRefreshment.add("");
								}

								if (repository.getServices().getRecreationalServices().getRefreshment().getDescriptiveNote().getP().get(j) != null
										&& repository.getServices().getRecreationalServices().getRefreshment().getDescriptiveNote().getP().get(j).getLang() != null
										&& !repository.getServices().getRecreationalServices().getRefreshment().getDescriptiveNote().getP().get(j).getLang().isEmpty()) {
									rsRefreshmentLang.add(repository.getServices().getRecreationalServices().getRefreshment().getDescriptiveNote().getP().get(j).getLang());
								} else {
									rsRefreshmentLang.add(Eag2012.OPTION_NONE);
								}
							}
						} else {
							rsRefreshment.add("");
							rsRefreshmentLang.add(Eag2012.OPTION_NONE);
						}

						// Exhibitions.
						if (!repository.getServices().getRecreationalServices().getExhibition().isEmpty()) {
							for (int j = 0; j < repository.getServices().getRecreationalServices().getExhibition().size(); j++) {
								rsNumberOfExhibitions.add("");
								if (repository.getServices().getRecreationalServices().getExhibition().get(j) != null) {
									// Description.
									if (repository.getServices().getRecreationalServices().getExhibition().get(j).getDescriptiveNote() != null
											&& repository.getServices().getRecreationalServices().getExhibition().get(j).getDescriptiveNote().getP() != null
											&& !repository.getServices().getRecreationalServices().getExhibition().get(j).getDescriptiveNote().getP().isEmpty()) {
										for (int k = 0; k < repository.getServices().getRecreationalServices().getExhibition().get(j).getDescriptiveNote().getP().size(); k++) {
											if (repository.getServices().getRecreationalServices().getExhibition().get(j).getDescriptiveNote().getP().get(k) != null
													&& repository.getServices().getRecreationalServices().getExhibition().get(j).getDescriptiveNote().getP().get(k).getContent() != null
													&& !repository.getServices().getRecreationalServices().getExhibition().get(j).getDescriptiveNote().getP().get(k).getContent().isEmpty()) {
												rsExhibition.add(repository.getServices().getRecreationalServices().getExhibition().get(j).getDescriptiveNote().getP().get(k).getContent());
											} else {
												rsExhibition.add("");
											}

											if (repository.getServices().getRecreationalServices().getExhibition().get(j).getDescriptiveNote().getP().get(k) != null
													&& repository.getServices().getRecreationalServices().getExhibition().get(j).getDescriptiveNote().getP().get(k).getLang() != null
													&& !repository.getServices().getRecreationalServices().getExhibition().get(j).getDescriptiveNote().getP().get(k).getLang().isEmpty()) {
												rsExhibitionLang.add(repository.getServices().getRecreationalServices().getExhibition().get(j).getDescriptiveNote().getP().get(k).getLang());
											} else {
												rsExhibitionLang.add(Eag2012.OPTION_NONE);
											}
										}
									} else {
										rsExhibition.add("");
										rsExhibitionLang.add(Eag2012.OPTION_NONE);
									}

									// Webpage.
									if (repository.getServices().getRecreationalServices().getExhibition().get(j) != null
											&& repository.getServices().getRecreationalServices().getExhibition().get(j).getWebpage() != null) {
										if (repository.getServices().getRecreationalServices().getExhibition().get(j).getWebpage().getHref() != null
												&& !repository.getServices().getRecreationalServices().getExhibition().get(j).getWebpage().getHref().isEmpty()) {
											rsExhibitionWebpage.add(repository.getServices().getRecreationalServices().getExhibition().get(j).getWebpage().getHref());
										} else {
											rsExhibitionWebpage.add("");
										}

										if (repository.getServices().getRecreationalServices().getExhibition().get(j).getWebpage().getContent() != null
												&& !repository.getServices().getRecreationalServices().getExhibition().get(j).getWebpage().getContent().isEmpty()) {
											rsExhibitionWebpageTitle.add(repository.getServices().getRecreationalServices().getExhibition().get(j).getWebpage().getContent());
										} else {
											rsExhibitionWebpageTitle.add("");
										}
									} else {
										rsExhibitionWebpage.add("");
										rsExhibitionWebpageTitle.add("");
									}
								} else {
									rsExhibition.add("");
									rsExhibitionLang.add(Eag2012.OPTION_NONE);
									rsExhibitionWebpage.add("");
									rsExhibitionWebpageTitle.add("");
								}
							}
						} else {
							rsNumberOfExhibitions.add("");
							rsExhibition.add("");
							rsExhibitionLang.add(Eag2012.OPTION_NONE);
							rsExhibitionWebpage.add("");
							rsExhibitionWebpageTitle.add("");
						}

						// Tours and Sessions.
						if (!repository.getServices().getRecreationalServices().getToursSessions().isEmpty()) {
							for (int j = 0; j < repository.getServices().getRecreationalServices().getToursSessions().size(); j++) {
								rsNumberOfToursSessions.add("");
								if (repository.getServices().getRecreationalServices().getToursSessions().get(j) != null) {
									// Description.
									if (repository.getServices().getRecreationalServices().getToursSessions().get(j).getDescriptiveNote() != null
											&& repository.getServices().getRecreationalServices().getToursSessions().get(j).getDescriptiveNote().getP() != null
											&& !repository.getServices().getRecreationalServices().getToursSessions().get(j).getDescriptiveNote().getP().isEmpty()) {
										for (int k = 0; k < repository.getServices().getRecreationalServices().getToursSessions().get(j).getDescriptiveNote().getP().size(); k++) {
											if (repository.getServices().getRecreationalServices().getToursSessions().get(j).getDescriptiveNote().getP().get(k) != null
													&& repository.getServices().getRecreationalServices().getToursSessions().get(j).getDescriptiveNote().getP().get(k).getContent() != null
													&& !repository.getServices().getRecreationalServices().getToursSessions().get(j).getDescriptiveNote().getP().get(k).getContent().isEmpty()) {
												rsToursSessions.add(repository.getServices().getRecreationalServices().getToursSessions().get(j).getDescriptiveNote().getP().get(k).getContent());
											} else {
												rsToursSessions.add("");
											}

											if (repository.getServices().getRecreationalServices().getToursSessions().get(j).getDescriptiveNote().getP().get(k) != null
													&& repository.getServices().getRecreationalServices().getToursSessions().get(j).getDescriptiveNote().getP().get(k).getLang() != null
													&& !repository.getServices().getRecreationalServices().getToursSessions().get(j).getDescriptiveNote().getP().get(k).getLang().isEmpty()) {
												rsToursSessionsLang.add(repository.getServices().getRecreationalServices().getToursSessions().get(j).getDescriptiveNote().getP().get(k).getLang());
											} else {
												rsToursSessionsLang.add(Eag2012.OPTION_NONE);
											}
										}
									} else {
										rsToursSessions.add("");
										rsToursSessionsLang.add(Eag2012.OPTION_NONE);
									}

									// Webpage.
									if (repository.getServices().getRecreationalServices().getToursSessions().get(j) != null
											&& repository.getServices().getRecreationalServices().getToursSessions().get(j).getWebpage() != null) {
										if (repository.getServices().getRecreationalServices().getToursSessions().get(j).getWebpage().getHref() != null
												&& !repository.getServices().getRecreationalServices().getToursSessions().get(j).getWebpage().getHref().isEmpty()) {
											rsToursSessionsWebpage.add(repository.getServices().getRecreationalServices().getToursSessions().get(j).getWebpage().getHref());
										} else {
											rsToursSessionsWebpage.add("");
										}

										if (repository.getServices().getRecreationalServices().getToursSessions().get(j).getWebpage().getContent() != null
												&& !repository.getServices().getRecreationalServices().getToursSessions().get(j).getWebpage().getContent().isEmpty()) {
											rsToursSessionsWebpageTitle.add(repository.getServices().getRecreationalServices().getToursSessions().get(j).getWebpage().getContent());
										} else {
											rsToursSessionsWebpageTitle.add("");
										}
									} else {
										rsToursSessionsWebpage.add("");
										rsToursSessionsWebpageTitle.add("");
									}
								} else {
									rsToursSessions.add("");
									rsToursSessionsLang.add(Eag2012.OPTION_NONE);
									rsToursSessionsWebpage.add("");
									rsToursSessionsWebpageTitle.add("");
								}
							}
						} else {
							rsNumberOfToursSessions.add("");
							rsToursSessions.add("");
							rsToursSessionsLang.add(Eag2012.OPTION_NONE);
							rsToursSessionsWebpage.add("");
							rsToursSessionsWebpageTitle.add("");
						}

						// Other services.
						if (!repository.getServices().getRecreationalServices().getOtherServices().isEmpty()) {
							for (int j = 0; j < repository.getServices().getRecreationalServices().getOtherServices().size(); j++) {
								rsNumberOfOtherServices.add("");
								if (repository.getServices().getRecreationalServices().getOtherServices().get(j) != null) {
									// Description.
									if (repository.getServices().getRecreationalServices().getOtherServices().get(j).getDescriptiveNote() != null
											&& repository.getServices().getRecreationalServices().getOtherServices().get(j).getDescriptiveNote().getP() != null
											&& !repository.getServices().getRecreationalServices().getOtherServices().get(j).getDescriptiveNote().getP().isEmpty()) {
										for (int k = 0; k < repository.getServices().getRecreationalServices().getOtherServices().get(j).getDescriptiveNote().getP().size(); k++) {
											if (repository.getServices().getRecreationalServices().getOtherServices().get(j).getDescriptiveNote().getP().get(k) != null
													&& repository.getServices().getRecreationalServices().getOtherServices().get(j).getDescriptiveNote().getP().get(k).getContent() != null
													&& !repository.getServices().getRecreationalServices().getOtherServices().get(j).getDescriptiveNote().getP().get(k).getContent().isEmpty()) {
												rsOtherServices.add(repository.getServices().getRecreationalServices().getOtherServices().get(j).getDescriptiveNote().getP().get(k).getContent());
											} else {
												rsOtherServices.add("");
											}

											if (repository.getServices().getRecreationalServices().getOtherServices().get(j).getDescriptiveNote().getP().get(k) != null
													&& repository.getServices().getRecreationalServices().getOtherServices().get(j).getDescriptiveNote().getP().get(k).getLang() != null
													&& !repository.getServices().getRecreationalServices().getOtherServices().get(j).getDescriptiveNote().getP().get(k).getLang().isEmpty()) {
												rsOtherServicesLang.add(repository.getServices().getRecreationalServices().getOtherServices().get(j).getDescriptiveNote().getP().get(k).getLang());
											} else {
												rsOtherServicesLang.add(Eag2012.OPTION_NONE);
											}
										}
									} else {
										rsOtherServices.add("");
										rsOtherServicesLang.add(Eag2012.OPTION_NONE);
									}

									// Webpage.
									if (repository.getServices().getRecreationalServices().getOtherServices().get(j) != null
											&& repository.getServices().getRecreationalServices().getOtherServices().get(j).getWebpage() != null) {
										if (repository.getServices().getRecreationalServices().getOtherServices().get(j).getWebpage().getHref() != null
												&& !repository.getServices().getRecreationalServices().getOtherServices().get(j).getWebpage().getHref().isEmpty()) {
											rsOtherServicesWebpage.add(repository.getServices().getRecreationalServices().getOtherServices().get(j).getWebpage().getHref());
										} else {
											rsOtherServicesWebpage.add("");
										}

										if (repository.getServices().getRecreationalServices().getOtherServices().get(j).getWebpage().getContent() != null
												&& !repository.getServices().getRecreationalServices().getOtherServices().get(j).getWebpage().getContent().isEmpty()) {
											rsOtherServicesWebpageTitle.add(repository.getServices().getRecreationalServices().getOtherServices().get(j).getWebpage().getContent());
										} else {
											rsOtherServicesWebpageTitle.add("");
										}
									} else {
										rsOtherServicesWebpage.add("");
										rsOtherServicesWebpageTitle.add("");
									}
								} else {
									rsOtherServices.add("");
									rsOtherServicesLang.add(Eag2012.OPTION_NONE);
									rsOtherServicesWebpage.add("");
									rsOtherServicesWebpageTitle.add("");
								}
							}
						} else {
							rsNumberOfOtherServices.add("");
							rsOtherServices.add("");
							rsOtherServicesLang.add(Eag2012.OPTION_NONE);
							rsOtherServicesWebpage.add("");
							rsOtherServicesWebpageTitle.add("");
						}
					}
					this.addAsRecreationalServicesRefreshmentArea(rsRefreshment);
					this.addAsRecreationalServicesRefreshmentAreaLang(rsRefreshmentLang);
					this.addAsRSNumberOfExhibition(rsNumberOfExhibitions);
					this.addAsRSExhibition(rsExhibition);
					this.addAsRSExhibitionLang(rsExhibitionLang);
					this.addAsRSExhibitionWebpageHref(rsExhibitionWebpage);
					this.addAsRSExhibitionWebpageTitle(rsExhibitionWebpageTitle);
					this.addAsRSNumberOfToursSessions(rsNumberOfToursSessions);
					this.addAsRSToursSessions(rsToursSessions);
					this.addAsRSToursSessionsLang(rsToursSessionsLang);
					this.addAsRSToursSessionsWebpageHref(rsToursSessionsWebpage);
					this.addAsRSToursSessionsWebpageTitle(rsToursSessionsWebpageTitle);
					this.addAsRSNumberOfOtherServices(rsNumberOfOtherServices);
					this.addAsRSOtherServices(rsOtherServices);
					this.addAsRSOtherServicesLang(rsOtherServicesLang);
					this.addAsRSOtherServicesWebpageHref(rsOtherServicesWebpage);
					this.addAsRSOtherServicesWebpageTitle(rsOtherServicesWebpageTitle);
				}
			}
		}
	}

	/**
	 * Method to load all values of "Description" tab.
	 */
	private void loadDescriptionTabValues() {
		// Repositories info.
		if (this.eag.getArchguide() != null && this.eag.getArchguide().getDesc() != null
				&& this.eag.getArchguide().getDesc().getRepositories() != null
				&& !this.eag.getArchguide().getDesc().getRepositories().getRepository().isEmpty()) {
			// For each repository
			for (int i = 0; i < this.eag.getArchguide().getDesc().getRepositories().getRepository().size(); i++) {
				Repository repository = this.eag.getArchguide().getDesc().getRepositories().getRepository().get(i);

				// Repository history.
				List<String> descRHDescription = new ArrayList<String>();
				List<String> descRHDescriptionLang = new ArrayList<String>();
				if (repository.getRepositorhist() != null
						&& repository.getRepositorhist().getDescriptiveNote() != null
						&& !repository.getRepositorhist().getDescriptiveNote().getP().isEmpty()) {
					for (int j = 0; j < repository.getRepositorhist().getDescriptiveNote().getP().size(); j++) {
						if (repository.getRepositorhist().getDescriptiveNote().getP().get(j) != null
								&& repository.getRepositorhist().getDescriptiveNote().getP().get(j).getContent() != null
								&& !repository.getRepositorhist().getDescriptiveNote().getP().get(j).getContent().isEmpty()) {
							descRHDescription.add(repository.getRepositorhist().getDescriptiveNote().getP().get(j).getContent());
							if (repository.getRepositorhist().getDescriptiveNote().getP().get(j).getLang() != null
									&& !repository.getRepositorhist().getDescriptiveNote().getP().get(j).getLang().isEmpty()) {
								descRHDescriptionLang.add(repository.getRepositorhist().getDescriptiveNote().getP().get(j).getLang());
							} else {
								descRHDescriptionLang.add(Eag2012.OPTION_NONE);
							}
						} else {
							descRHDescription.add("");
							descRHDescriptionLang.add(Eag2012.OPTION_NONE);
						}
					}
				}
				this.addDescRepositorhist(descRHDescription);
				this.addDescRepositorhistLang(descRHDescriptionLang);

				// Date of repository foundation.
				List<String> descRHFoundationDate = new ArrayList<String>();
				List<String> descRHFoundationRule = new ArrayList<String>();
				List<String> descRHFoundationLang = new ArrayList<String>();
				if (repository.getRepositorfound() != null) {
					if (repository.getRepositorfound().getDate() != null
							&& repository.getRepositorfound().getDate().getContent() != null
							&& !repository.getRepositorfound().getDate().getContent().isEmpty()) {
						descRHFoundationDate.add(repository.getRepositorfound().getDate().getContent());
					} else {
						descRHFoundationDate.add("");
					}

					// Rule of repository foundation.
					if (repository.getRepositorfound().getRule() != null
							&& !repository.getRepositorfound().getRule().isEmpty()) {
						for (int j = 0; j < repository.getRepositorfound().getRule().size(); j++) {
							if (repository.getRepositorfound().getRule().get(j) != null
									&& repository.getRepositorfound().getRule().get(j).getContent() != null
									&& !repository.getRepositorfound().getRule().get(j).getContent().isEmpty()) {
								descRHFoundationRule.add(repository.getRepositorfound().getRule().get(j).getContent());
								if (repository.getRepositorfound().getRule().get(j).getLang() != null
										&& !repository.getRepositorfound().getRule().get(j).getLang().isEmpty()) {
									descRHFoundationLang.add(repository.getRepositorfound().getRule().get(j).getLang());
								} else {
									descRHFoundationLang.add(Eag2012.OPTION_NONE);
								}
							} else {
								descRHFoundationRule.add("");
								descRHFoundationLang.add(Eag2012.OPTION_NONE);
							}
						}
					} else {
						descRHFoundationRule.add("");
						descRHFoundationLang.add(Eag2012.OPTION_NONE);
					}
				}
				this.addDescRepositorFoundDate(descRHFoundationDate);
				this.addDescRepositorFoundRule(descRHFoundationRule);
				this.addDescRepositorFoundRuleLang(descRHFoundationLang);

				// Date of repository suppression.
				List<String> descRHSuppressionDate = new ArrayList<String>();
				List<String> descRHSuppressionRule = new ArrayList<String>();
				List<String> descRHSuppressionLang = new ArrayList<String>();
				if (repository.getRepositorsup() != null) {
					if (repository.getRepositorsup().getDate() != null
							&& repository.getRepositorsup().getDate().getContent() != null
							&& !repository.getRepositorsup().getDate().getContent().isEmpty()) {
						descRHSuppressionDate.add(repository.getRepositorsup().getDate().getContent());
					} else {
						descRHSuppressionDate.add("");
					}

					// Rule of repository suppression.
					if (repository.getRepositorsup().getRule() != null
							&& !repository.getRepositorsup().getRule().isEmpty()) {
						for (int j = 0; j < repository.getRepositorsup().getRule().size(); j++) {
							if (repository.getRepositorsup().getRule().get(j) != null
									&& repository.getRepositorsup().getRule().get(j).getContent() != null
									&& !repository.getRepositorsup().getRule().get(j).getContent().isEmpty()) {
								descRHSuppressionRule.add(repository.getRepositorsup().getRule().get(j).getContent());
								if (repository.getRepositorsup().getRule().get(j).getLang() != null
										&& !repository.getRepositorsup().getRule().get(j).getLang().isEmpty()) {
									descRHSuppressionLang.add(repository.getRepositorsup().getRule().get(j).getLang());
								} else {
									descRHSuppressionLang.add(Eag2012.OPTION_NONE);
								}
							} else {
								descRHSuppressionRule.add("");
								descRHSuppressionLang.add(Eag2012.OPTION_NONE);
							}
						}
					} else {
						descRHSuppressionRule.add("");
						descRHSuppressionLang.add(Eag2012.OPTION_NONE);
					}
				}
				this.addDescRepositorSupDate(descRHSuppressionDate);
				this.addDescRepositorSupRule(descRHSuppressionRule);
				this.addDescRepositorSupRuleLang(descRHSuppressionLang);

				// Unit of administrative structure.
				List<String> descAdminStructure = new ArrayList<String>();
				List<String> descAdminStructureLang = new ArrayList<String>();
				if (repository.getAdminhierarchy() != null
						&& !repository.getAdminhierarchy().getAdminunit().isEmpty()) {
					for (int j = 0; j < repository.getAdminhierarchy().getAdminunit().size(); j++) {
						if (repository.getAdminhierarchy().getAdminunit().get(j) != null
								&& repository.getAdminhierarchy().getAdminunit().get(j).getContent() != null
								&& !repository.getAdminhierarchy().getAdminunit().get(j).getContent().isEmpty()) {
							descAdminStructure.add(repository.getAdminhierarchy().getAdminunit().get(j).getContent());
							if (repository.getAdminhierarchy().getAdminunit().get(j).getLang() != null
									&& !repository.getAdminhierarchy().getAdminunit().get(j).getLang().isEmpty()) {
								descAdminStructureLang.add(repository.getAdminhierarchy().getAdminunit().get(j).getLang());
							} else {
								descAdminStructureLang.add(Eag2012.OPTION_NONE);
							}
						} else {
							descAdminStructure.add("");
							descAdminStructureLang.add(Eag2012.OPTION_NONE);
						}
					}
				}
				this.addDescAdminunit(descAdminStructure);
				this.addDescAdminunitLang(descAdminStructureLang);

				// Building.
				List<String> descBuildingDescription = new ArrayList<String>();
				List<String> descBuildingDescriptionLang = new ArrayList<String>();
				List<String> descBuildingArea = new ArrayList<String>();
				List<String> descBuildingAreaUnit = new ArrayList<String>();
				List<String> descBuildingShelf = new ArrayList<String>();
				List<String> descBuildingShelfUnit = new ArrayList<String>();
				if (repository.getBuildinginfo() != null) {
					if (repository.getBuildinginfo().getBuilding() != null
							&& repository.getBuildinginfo().getBuilding().getDescriptiveNote() != null
							&& !repository.getBuildinginfo().getBuilding().getDescriptiveNote().getP().isEmpty()) {
						for (int j = 0; j < repository.getBuildinginfo().getBuilding().getDescriptiveNote().getP().size(); j++) {
							if (repository.getBuildinginfo().getBuilding().getDescriptiveNote().getP().get(j) != null
									&& repository.getBuildinginfo().getBuilding().getDescriptiveNote().getP().get(j).getContent() != null
									&& !repository.getBuildinginfo().getBuilding().getDescriptiveNote().getP().get(j).getContent().isEmpty()) {
								descBuildingDescription.add(repository.getBuildinginfo().getBuilding().getDescriptiveNote().getP().get(j).getContent());
								if (repository.getBuildinginfo().getBuilding().getDescriptiveNote().getP().get(j).getLang() != null
										&& !repository.getBuildinginfo().getBuilding().getDescriptiveNote().getP().get(j).getLang().isEmpty()) {
									descBuildingDescriptionLang.add(repository.getBuildinginfo().getBuilding().getDescriptiveNote().getP().get(j).getLang());
								} else {
									descBuildingDescriptionLang.add(Eag2012.OPTION_NONE);
								}
							} else {
								descBuildingDescription.add("");
								descBuildingDescriptionLang.add(Eag2012.OPTION_NONE);
							}
						}
					} else {
						descBuildingDescription.add("");
						descBuildingDescriptionLang.add(Eag2012.OPTION_NONE);
					}

					// Repository Area.
					if (repository.getBuildinginfo().getRepositorarea() != null
							&& repository.getBuildinginfo().getRepositorarea().getNum() != null
							&& repository.getBuildinginfo().getRepositorarea().getNum().getContent() != null
							&& !repository.getBuildinginfo().getRepositorarea().getNum().getContent().isEmpty()) {
						descBuildingArea.add(repository.getBuildinginfo().getRepositorarea().getNum().getContent());

						if (repository.getBuildinginfo().getRepositorarea().getNum().getUnit() != null
								&& !repository.getBuildinginfo().getRepositorarea().getNum().getUnit().isEmpty()) {
							descBuildingAreaUnit.add(repository.getBuildinginfo().getRepositorarea().getNum().getUnit());
						} else {
							descBuildingAreaUnit.add("");
						}
					} else {
						descBuildingArea.add("");
						descBuildingAreaUnit.add("");
					}

					// Length of shelf.
					if (repository.getBuildinginfo().getLengthshelf() != null
							&& repository.getBuildinginfo().getLengthshelf().getNum() != null
							&& repository.getBuildinginfo().getLengthshelf().getNum().getContent() != null
							&& !repository.getBuildinginfo().getLengthshelf().getNum().getContent().isEmpty()) {
						descBuildingShelf.add(repository.getBuildinginfo().getLengthshelf().getNum().getContent());

						if (repository.getBuildinginfo().getLengthshelf().getNum().getUnit() != null
								&& !repository.getBuildinginfo().getLengthshelf().getNum().getUnit().isEmpty()) {
							descBuildingShelfUnit.add(repository.getBuildinginfo().getLengthshelf().getNum().getUnit());
						} else {
							descBuildingShelfUnit.add("");
						}
					} else {
						descBuildingShelf.add("");
						descBuildingShelfUnit.add("");
					}
				}
				this.addDescBuilding(descBuildingDescription);
				this.addDescBuildingLang(descBuildingDescriptionLang);
				this.addDescRepositorarea(descBuildingArea);
				this.addDescRepositorareaUnit(descBuildingAreaUnit);
				this.addDescLengthshelf(descBuildingShelf);
				this.addDescLengthshelfUnit(descBuildingShelfUnit);

				// Building.
				List<String> descHoldingsDescription = new ArrayList<String>();
				List<String> descHoldingsDescriptionLang = new ArrayList<String>();
				List<String> descDateHoldings = new ArrayList<String>();
				List<String> descNumberOfDateRange = new ArrayList<String>();
				List<String> descHoldingsDateFrom= new ArrayList<String>();
				List<String> descHoldingsDateTo = new ArrayList<String>();
				List<String> descHoldingsExtent = new ArrayList<String>();
				List<String> descHoldingsExtentUnit = new ArrayList<String>();
				if (repository.getHoldings() != null) {
					if (repository.getHoldings().getDescriptiveNote() != null
							&& !repository.getHoldings().getDescriptiveNote().getP().isEmpty()) {
						for (int j = 0; j < repository.getHoldings().getDescriptiveNote().getP().size(); j++) {
							if (repository.getHoldings().getDescriptiveNote().getP().get(j) != null
									&& repository.getHoldings().getDescriptiveNote().getP().get(j).getContent() != null
									&& !repository.getHoldings().getDescriptiveNote().getP().get(j).getContent().isEmpty()) {
								descHoldingsDescription.add(repository.getHoldings().getDescriptiveNote().getP().get(j).getContent());
								if (repository.getHoldings().getDescriptiveNote().getP().get(j).getLang() != null
										&& !repository.getHoldings().getDescriptiveNote().getP().get(j).getLang().isEmpty()) {
									descHoldingsDescriptionLang.add(repository.getHoldings().getDescriptiveNote().getP().get(j).getLang());
								} else {
									descHoldingsDescriptionLang.add(Eag2012.OPTION_NONE);
								}
							} else {
								descHoldingsDescription.add("");
								descHoldingsDescriptionLang.add(Eag2012.OPTION_NONE);
							}
						}
					} else {
						descHoldingsDescription.add("");
						descHoldingsDescriptionLang.add(Eag2012.OPTION_NONE);
					}

					// Date of holdings.
					if (repository.getHoldings().getDate() != null) {
						if (repository.getHoldings().getDate().getContent() != null
							&& !repository.getHoldings().getDate().getContent().isEmpty()) {
						descDateHoldings.add(repository.getHoldings().getDate().getContent());
						} else {
							descDateHoldings.add("");
						}
					}

					// Date range of holdings.
					if (repository.getHoldings().getDateRange() != null) {
						descNumberOfDateRange.add("");
						// From date.
						if (repository.getHoldings().getDateRange().getFromDate() != null
								&& repository.getHoldings().getDateRange().getFromDate().getContent() != null
								&& !repository.getHoldings().getDateRange().getFromDate().getContent().isEmpty()) {
							descHoldingsDateFrom.add(repository.getHoldings().getDateRange().getFromDate().getContent());
						} else {
							descHoldingsDateFrom.add("");
						}

						// To date.
						if (repository.getHoldings().getDateRange().getToDate() != null
								&& repository.getHoldings().getDateRange().getToDate().getContent() != null
								&& !repository.getHoldings().getDateRange().getToDate().getContent().isEmpty()) {
							descHoldingsDateTo.add(repository.getHoldings().getDateRange().getToDate().getContent());
						} else {
							descHoldingsDateTo.add("");
						}
					}

					// DateSet of holdings.
					if (repository.getHoldings().getDateSet() != null) {
						if (!repository.getHoldings().getDateSet().getDateOrDateRange().isEmpty()) {
							for (int j = 0; j < repository.getHoldings().getDateSet().getDateOrDateRange().size(); j++) {
								Object dateObject = repository.getHoldings().getDateSet().getDateOrDateRange().get(j);

								// Date inside DateSet.
								if (dateObject != null
										&& dateObject instanceof Date) {
									Date date = (Date) dateObject;
									if (date != null
											&& date.getContent() != null
											&& !date.getContent().isEmpty()) {
										descDateHoldings.add(date.getContent());
									} else {
										descDateHoldings.add("");
									}
								} else if (dateObject != null
										&& dateObject instanceof DateRange) {
									// DateRange inside DateSet.
									DateRange dateRange = (DateRange) dateObject;

									descNumberOfDateRange.add("");
									// From date.
									if (dateRange.getFromDate() != null
											&& dateRange.getFromDate().getContent() != null
											&& !dateRange.getFromDate().getContent().isEmpty()) {
										descHoldingsDateFrom.add(dateRange.getFromDate().getContent());
									} else {
										descHoldingsDateFrom.add("");
									}

									// To date.
									if (dateRange.getToDate() != null
											&& dateRange.getToDate().getContent() != null
											&& !dateRange.getToDate().getContent().isEmpty()) {
										descHoldingsDateTo.add(dateRange.getToDate().getContent());
									} else {
										descHoldingsDateTo.add("");
									}
								}
							}
						} else {
							descDateHoldings.add("");
							descNumberOfDateRange.add("");
							descHoldingsDateFrom.add("");
							descHoldingsDateTo.add("");
						}
					}

					// Extent.
					if (repository.getHoldings().getExtent() != null
							&& repository.getHoldings().getExtent().getNum() != null
							&& repository.getHoldings().getExtent().getNum().getContent() != null
							&& !repository.getHoldings().getExtent().getNum().getContent().isEmpty()) {
						descHoldingsExtent.add(repository.getHoldings().getExtent().getNum().getContent());

						if (repository.getHoldings().getExtent().getNum().getUnit() != null
								&& !repository.getHoldings().getExtent().getNum().getUnit().isEmpty()) {
							descHoldingsExtentUnit.add(repository.getHoldings().getExtent().getNum().getUnit());
						} else {
							descHoldingsExtentUnit.add("");
						}
					} else {
						descHoldingsExtent.add("");
						descHoldingsExtentUnit.add("");
					}
				}
				this.addDescHoldings(descHoldingsDescription);
				this.addDescHoldingsLang(descHoldingsDescriptionLang);
				this.addDescHoldingsDate(descDateHoldings);
				this.addDescNumberOfHoldingsDateRange(descNumberOfDateRange);
				this.addDescHoldingsDateRangeFromDate(descHoldingsDateFrom);
				this.addDescHoldingsDateRangeToDate(descHoldingsDateTo);
				this.addDescExtent(descHoldingsExtent);
				this.addDescExtentUnit(descHoldingsExtentUnit);
			}
		}
	}

	/**
	 * Method to load all values of "Control" tab.
	 */
	private void loadControlTabValues() {
		// Lang of person/institution responsible for the description.
		if (this.eag.getControl() != null && this.eag.getControl().getMaintenanceHistory() != null
				&& !this.eag.getControl().getMaintenanceHistory().getMaintenanceEvent().isEmpty()) {
			if (this.eag.getControl().getMaintenanceHistory().getMaintenanceEvent().get(this.eag.getControl().getMaintenanceHistory().getMaintenanceEvent().size()-1).getAgent() != null) {
				this.setControlAgentLang(this.eag.getControl().getMaintenanceHistory().getMaintenanceEvent().get(this.eag.getControl().getMaintenanceHistory().getMaintenanceEvent().size()-1).getAgent().getLang());
			}
		}

		// Identifier of responsible institution.
		if (this.eag.getControl() != null && this.eag.getControl().getMaintenanceAgency() != null) {
			if (this.eag.getControl().getMaintenanceAgency().getAgencyCode() != null
					&& this.eag.getControl().getMaintenanceAgency().getAgencyCode().getContent() != null
					&& !this.eag.getControl().getMaintenanceAgency().getAgencyCode().getContent().isEmpty()) {
				this.setControlAgencyCode(this.eag.getControl().getMaintenanceAgency().getAgencyCode().getContent());
			}
		}

		// Used languages and scripts for the description.
		if (this.eag.getControl() != null && this.eag.getControl().getLanguageDeclarations() != null) {
			if (!this.eag.getControl().getLanguageDeclarations().getLanguageDeclaration().isEmpty()) {
				for (int i = 0; i < this.eag.getControl().getLanguageDeclarations().getLanguageDeclaration().size(); i++) {
					this.addControlNumberOfLanguages("");
					if (this.eag.getControl().getLanguageDeclarations().getLanguageDeclaration().get(i) != null
							&& this.eag.getControl().getLanguageDeclarations().getLanguageDeclaration().get(i).getLanguage() != null
							&& this.eag.getControl().getLanguageDeclarations().getLanguageDeclaration().get(i).getLanguage().getLanguageCode() != null
							&& !this.eag.getControl().getLanguageDeclarations().getLanguageDeclaration().get(i).getLanguage().getLanguageCode().isEmpty()) {
						this.addControlLanguageDeclaration(this.eag.getControl().getLanguageDeclarations().getLanguageDeclaration().get(i).getLanguage().getLanguageCode());
					} else {
						this.addControlLanguageDeclaration("");
					}
					if (this.eag.getControl().getLanguageDeclarations().getLanguageDeclaration().get(i) != null
							&& this.eag.getControl().getLanguageDeclarations().getLanguageDeclaration().get(i).getScript() != null
							&& this.eag.getControl().getLanguageDeclarations().getLanguageDeclaration().get(i).getScript().getScriptCode() != null
							&& !this.eag.getControl().getLanguageDeclarations().getLanguageDeclaration().get(i).getScript().getScriptCode().isEmpty()) {
						this.addControlScript(this.eag.getControl().getLanguageDeclarations().getLanguageDeclaration().get(i).getScript().getScriptCode());
					} else {
						this.addControlScript("");
					}
				}
			}
		}

		//Used rules / conventions / standards.
		if (this.eag.getControl() != null && this.eag.getControl().getConventionDeclaration() != null) {
			if (!this.eag.getControl().getConventionDeclaration().isEmpty()) {
				for (int i = 0; i < this.eag.getControl().getConventionDeclaration().size(); i++) {
					this.addControlNumberOfRules("");
					if (this.eag.getControl().getConventionDeclaration().get(i) != null
							&& this.eag.getControl().getConventionDeclaration().get(i).getAbbreviation() != null
							&& this.eag.getControl().getConventionDeclaration().get(i).getAbbreviation().getContent() != null
							&& !this.eag.getControl().getConventionDeclaration().get(i).getAbbreviation().getContent().isEmpty()) {
						this.addControlAbbreviation(this.eag.getControl().getConventionDeclaration().get(i).getAbbreviation().getContent());
					} else {
						this.addControlAbbreviation("");
					}
					if (this.eag.getControl().getConventionDeclaration().get(i) != null
							&& !this.eag.getControl().getConventionDeclaration().get(i).getCitation().isEmpty()) {
						for (int j = 0; j < this.eag.getControl().getConventionDeclaration().get(i).getCitation().size(); j++) {
							if (this.eag.getControl().getConventionDeclaration().get(i).getCitation().get(j) != null
									&& this.eag.getControl().getConventionDeclaration().get(i).getCitation().get(j).getContent() != null
									&& !this.eag.getControl().getConventionDeclaration().get(i).getCitation().get(j).getContent().isEmpty()) {
								this.addControlCitation(this.eag.getControl().getConventionDeclaration().get(i).getCitation().get(j).getContent());
							} else {
								this.addControlCitation("");
							}
						}
					} else {
						this.addControlCitation("");
					}
				}
			}
		}
	}

	/**
	 * Method to load all values of "Relations" tab.
	 */
	private void loadRelationsTabValues() {
		if (this.eag.getRelations() != null) {
			// Resource relations.
			if (!this.eag.getRelations().getResourceRelation().isEmpty()) {
				for (int i = 0; i < this.eag.getRelations().getResourceRelation().size(); i++) {
					// Type of your relation.
					if (this.eag.getRelations().getResourceRelation().get(i) != null
							&& this.eag.getRelations().getResourceRelation().get(i).getResourceRelationType() != null
							&& !this.eag.getRelations().getResourceRelation().get(i).getResourceRelationType().isEmpty()) {
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

						this.addRelationsResourceRelationType(resourceRelationTypeValue);
					} else {
						this.addRelationsResourceRelationType(Eag2012.OPTION_CREATOR);
					}

					// Website of your resource.
					if (this.eag.getRelations().getResourceRelation().get(i) != null
							&& this.eag.getRelations().getResourceRelation().get(i).getHref() != null
							&& !this.eag.getRelations().getResourceRelation().get(i).getHref().isEmpty()) {
						this.addRelationsResourceRelationHref(this.eag.getRelations().getResourceRelation().get(i).getHref());
					} else {
						this.addRelationsResourceRelationHref("");
					}

					// Title & ID of the related material.
					if (this.eag.getRelations().getResourceRelation().get(i) != null
							&& this.eag.getRelations().getResourceRelation().get(i).getRelationEntry() != null
							&& this.eag.getRelations().getResourceRelation().get(i).getRelationEntry().getContent() != null
							&& !this.eag.getRelations().getResourceRelation().get(i).getRelationEntry().getContent().isEmpty()) {						
						this.addRelationsResourceRelationEntry(this.eag.getRelations().getResourceRelation().get(i).getRelationEntry().getContent());

						if (this.eag.getRelations().getResourceRelation().get(i).getRelationEntry().getLang() != null
								&& !this.eag.getRelations().getResourceRelation().get(i).getRelationEntry().getLang().isEmpty()) {
							this.addRelationsResourceRelationEntryLang(this.eag.getRelations().getResourceRelation().get(i).getRelationEntry().getLang());
						} else {
							this.addRelationsResourceRelationEntryLang(Eag2012.OPTION_NONE);
						}
					} else {
						this.addRelationsResourceRelationEntry("");
						this.addRelationsResourceRelationEntryLang(Eag2012.OPTION_NONE);
					}

					// Description of relation.
					if (this.eag.getRelations().getResourceRelation().get(i) != null
							&& this.eag.getRelations().getResourceRelation().get(i).getDescriptiveNote() != null
							&& !this.eag.getRelations().getResourceRelation().get(i).getDescriptiveNote().getP().isEmpty()) {
						for (int j = 0; j < this.eag.getRelations().getResourceRelation().get(i).getDescriptiveNote().getP().size(); j++) {
							if (this.eag.getRelations().getResourceRelation().get(i).getDescriptiveNote().getP().get(j) != null
									&& this.eag.getRelations().getResourceRelation().get(i).getDescriptiveNote().getP().get(j).getContent() != null
									&& !this.eag.getRelations().getResourceRelation().get(i).getDescriptiveNote().getP().get(j).getContent().isEmpty()) {
								this.addRelationsResourceRelationEntryDescription(this.eag.getRelations().getResourceRelation().get(i).getDescriptiveNote().getP().get(j).getContent());

								if (this.eag.getRelations().getResourceRelation().get(i).getDescriptiveNote().getP().get(j).getLang() != null
										&& !this.eag.getRelations().getResourceRelation().get(i).getDescriptiveNote().getP().get(j).getLang().isEmpty()) {
									this.addRelationsResourceRelationEntryDescriptionLang(this.eag.getRelations().getResourceRelation().get(i).getDescriptiveNote().getP().get(j).getLang());
								} else {
									this.addRelationsResourceRelationEntryDescriptionLang(Eag2012.OPTION_NONE);
								}
							} else {
								this.addRelationsResourceRelationEntryDescription("");
								this.addRelationsResourceRelationEntryDescriptionLang(Eag2012.OPTION_NONE);
							}
						}
					} else {
						this.addRelationsResourceRelationEntryDescription("");
						this.addRelationsResourceRelationEntryDescriptionLang(Eag2012.OPTION_NONE);
					}
				}
			}

			// Institution/Repository relation.
			if (!this.eag.getRelations().getEagRelation().isEmpty()) {
				for (int i = 0; i < this.eag.getRelations().getEagRelation().size(); i++) {
					this.addRelationsNumberOfEagRelations("");
					// Type of the relation.
					if (this.eag.getRelations().getEagRelation().get(i) != null
							&& this.eag.getRelations().getEagRelation().get(i).getEagRelationType() != null
							&& !this.eag.getRelations().getEagRelation().get(i).getEagRelationType().isEmpty()) {
						String eagRelationTypeValue = this.eag.getRelations().getEagRelation().get(i).getEagRelationType();
						
						if (Eag2012.OPTION_CHILD_TEXT.equalsIgnoreCase(eagRelationTypeValue)) {
							eagRelationTypeValue = Eag2012.OPTION_CHILD;
						}
						if (Eag2012.OPTION_PARENT_TEXT.equalsIgnoreCase(eagRelationTypeValue)) {
							eagRelationTypeValue = Eag2012.OPTION_PARENT;
						}
						if (Eag2012.OPTION_EARLIER_TEXT.equalsIgnoreCase(eagRelationTypeValue)) {
							eagRelationTypeValue = Eag2012.OPTION_EARLIER;
						}
						if (Eag2012.OPTION_LATER_TEXT.equalsIgnoreCase(eagRelationTypeValue)) {
							eagRelationTypeValue = Eag2012.OPTION_LATER;
						}
						if (Eag2012.OPTION_ASSOCIATIVE_TEXT.equalsIgnoreCase(eagRelationTypeValue)) {
							eagRelationTypeValue = Eag2012.OPTION_ASSOCIATIVE;
						}

						this.addRelationsEagRelationType(eagRelationTypeValue);
					} else {
						this.addRelationsEagRelationType(Eag2012.OPTION_NONE);
					}

					// Website of the description of the institution.
					if (this.eag.getRelations().getEagRelation().get(i) != null
							&& this.eag.getRelations().getEagRelation().get(i).getHref() != null
							&& !this.eag.getRelations().getEagRelation().get(i).getHref().isEmpty()) {
						this.addRelationsEagRelationHref(this.eag.getRelations().getEagRelation().get(i).getHref());
					} else {
						this.addRelationsEagRelationHref("");
					}

					// Title & ID of the related institution.
					if (this.eag.getRelations().getEagRelation().get(i) != null
							&& !this.eag.getRelations().getEagRelation().get(i).getRelationEntry().isEmpty()) {
						for (int j = 0 ; j < this.eag.getRelations().getEagRelation().get(i).getRelationEntry().size(); j++) {
							if (this.eag.getRelations().getEagRelation().get(i).getRelationEntry().get(j) != null
									&& this.eag.getRelations().getEagRelation().get(i).getRelationEntry().get(j).getContent() != null
									&& !this.eag.getRelations().getEagRelation().get(i).getRelationEntry().get(j).getContent().isEmpty()) {
								this.addRelationsEagRelationEntry(this.eag.getRelations().getEagRelation().get(i).getRelationEntry().get(j).getContent());

								if (this.eag.getRelations().getEagRelation().get(i).getRelationEntry().get(j).getLang() != null
										&& !this.eag.getRelations().getEagRelation().get(i).getRelationEntry().get(j).getLang().isEmpty()) {
									this.addRelationsEagRelationEntryLang(this.eag.getRelations().getEagRelation().get(i).getRelationEntry().get(j).getLang());
								} else {
									this.addRelationsEagRelationEntryLang(Eag2012.OPTION_NONE);
								}
							} else {
								this.addRelationsEagRelationEntry("");
								this.addRelationsEagRelationEntryLang(Eag2012.OPTION_NONE);
							}
						}
					} else {
						this.addRelationsEagRelationEntry("");
						this.addRelationsEagRelationEntryLang(Eag2012.OPTION_NONE);
					}

					// Description of relation.
					if (this.eag.getRelations().getEagRelation().get(i) != null
							&& this.eag.getRelations().getEagRelation().get(i).getDescriptiveNote() != null
							&& !this.eag.getRelations().getEagRelation().get(i).getDescriptiveNote().getP().isEmpty()) {
						for (int j = 0; j < this.eag.getRelations().getEagRelation().get(i).getDescriptiveNote().getP().size(); j++) {
							if (this.eag.getRelations().getEagRelation().get(i).getDescriptiveNote().getP().get(j) != null
									&& this.eag.getRelations().getEagRelation().get(i).getDescriptiveNote().getP().get(j).getContent() != null
									&& !this.eag.getRelations().getEagRelation().get(i).getDescriptiveNote().getP().get(j).getContent().isEmpty()) {
								this.addRelationsEagRelationEntryDescription(this.eag.getRelations().getEagRelation().get(i).getDescriptiveNote().getP().get(j).getContent());

								if (this.eag.getRelations().getEagRelation().get(i).getDescriptiveNote().getP().get(j).getLang() != null
										&& !this.eag.getRelations().getEagRelation().get(i).getDescriptiveNote().getP().get(j).getLang().isEmpty()) {
									this.addRelationsEagRelationEntryDescriptionLang(this.eag.getRelations().getEagRelation().get(i).getDescriptiveNote().getP().get(j).getLang());
								} else {
									this.addRelationsEagRelationEntryDescriptionLang(Eag2012.OPTION_NONE);
								}
							} else {
								this.addRelationsEagRelationEntryDescription("");
								this.addRelationsEagRelationEntryDescriptionLang(Eag2012.OPTION_NONE);
							}
						}
					} else {
						this.addRelationsEagRelationEntryDescription("");
						this.addRelationsEagRelationEntryDescriptionLang(Eag2012.OPTION_NONE);
					}
				}
			}
		}
	}
	
	public String toString(){
		return "Loader with record id: "+this.recordId;
	}

	/**
	 * Method to recover the value to set in XML file for "<geogarea>"
	 */
	private String getGeogareaString(final String geogarea) {
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

		return geogareaValue;
	}

	/**
	 * Method to replace the duplicated whitespaces inside a string.
	 * @param str
	 * @return The string without whitespaces.
	 */
	public String removeDuplicateWhiteSpaces(final String str) {
		String strWith = str.trim().replaceAll("[\\s+]", " ");
		StringBuilder sb = new StringBuilder();
		boolean space = false;
		for (int i = 0; i < strWith.length() ; i ++) {
			if (!space && strWith.charAt(i) == ' ') {
				sb.append(' ');
				space = true;
			} else if (strWith.charAt(i) != ' ') {
				sb.append(strWith.charAt(i));
				space = false;
			}
		}
		return sb.toString();
	}
}
