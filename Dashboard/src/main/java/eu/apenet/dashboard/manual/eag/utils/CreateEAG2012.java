package eu.apenet.dashboard.manual.eag.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import eu.apenet.dashboard.manual.eag.Eag2012;
import eu.apenet.dpt.utils.eag2012.Access;
import eu.apenet.dpt.utils.eag2012.Accessibility;
import eu.apenet.dpt.utils.eag2012.Adminhierarchy;
import eu.apenet.dpt.utils.eag2012.Adminunit;
import eu.apenet.dpt.utils.eag2012.AdvancedOrders;
import eu.apenet.dpt.utils.eag2012.AgencyName;
import eu.apenet.dpt.utils.eag2012.Agent;
import eu.apenet.dpt.utils.eag2012.Archguide;
import eu.apenet.dpt.utils.eag2012.Autform;
import eu.apenet.dpt.utils.eag2012.Building;
import eu.apenet.dpt.utils.eag2012.Buildinginfo;
import eu.apenet.dpt.utils.eag2012.Citation;
import eu.apenet.dpt.utils.eag2012.Closing;
import eu.apenet.dpt.utils.eag2012.ComputerPlaces;
import eu.apenet.dpt.utils.eag2012.Contact;
import eu.apenet.dpt.utils.eag2012.Control;
import eu.apenet.dpt.utils.eag2012.Country;
import eu.apenet.dpt.utils.eag2012.Date;
import eu.apenet.dpt.utils.eag2012.DateRange;
import eu.apenet.dpt.utils.eag2012.DateSet;
import eu.apenet.dpt.utils.eag2012.Desc;
import eu.apenet.dpt.utils.eag2012.DescriptiveNote;
import eu.apenet.dpt.utils.eag2012.Digitalser;
import eu.apenet.dpt.utils.eag2012.Directions;
import eu.apenet.dpt.utils.eag2012.Eag;
import eu.apenet.dpt.utils.eag2012.Email;
import eu.apenet.dpt.utils.eag2012.Exhibition;
import eu.apenet.dpt.utils.eag2012.Extent;
import eu.apenet.dpt.utils.eag2012.Fax;
import eu.apenet.dpt.utils.eag2012.Firstdem;
import eu.apenet.dpt.utils.eag2012.FromDate;
import eu.apenet.dpt.utils.eag2012.Geogarea;
import eu.apenet.dpt.utils.eag2012.Holdings;
import eu.apenet.dpt.utils.eag2012.Identity;
import eu.apenet.dpt.utils.eag2012.InternetAccess;
import eu.apenet.dpt.utils.eag2012.Lengthshelf;
import eu.apenet.dpt.utils.eag2012.Library;
import eu.apenet.dpt.utils.eag2012.Localentity;
import eu.apenet.dpt.utils.eag2012.Location;
import eu.apenet.dpt.utils.eag2012.MaintenanceAgency;
import eu.apenet.dpt.utils.eag2012.MaintenanceEvent;
import eu.apenet.dpt.utils.eag2012.MaintenanceHistory;
import eu.apenet.dpt.utils.eag2012.MaintenanceStatus;
import eu.apenet.dpt.utils.eag2012.MicrofilmPlaces;
import eu.apenet.dpt.utils.eag2012.Microformser;
import eu.apenet.dpt.utils.eag2012.Monographicpub;
import eu.apenet.dpt.utils.eag2012.MunicipalityPostalcode;
import eu.apenet.dpt.utils.eag2012.Nonpreform;
import eu.apenet.dpt.utils.eag2012.Num;
import eu.apenet.dpt.utils.eag2012.Opening;
import eu.apenet.dpt.utils.eag2012.OtherAgencyCode;
import eu.apenet.dpt.utils.eag2012.OtherRecordId;
import eu.apenet.dpt.utils.eag2012.OtherRepositorId;
import eu.apenet.dpt.utils.eag2012.OtherServices;
import eu.apenet.dpt.utils.eag2012.P;
import eu.apenet.dpt.utils.eag2012.Parform;
import eu.apenet.dpt.utils.eag2012.Photocopyser;
import eu.apenet.dpt.utils.eag2012.PhotographAllowance;
import eu.apenet.dpt.utils.eag2012.Photographser;
import eu.apenet.dpt.utils.eag2012.ReadersTicket;
import eu.apenet.dpt.utils.eag2012.RecordId;
import eu.apenet.dpt.utils.eag2012.RecreationalServices;
import eu.apenet.dpt.utils.eag2012.Refreshment;
import eu.apenet.dpt.utils.eag2012.RelationEntry;
import eu.apenet.dpt.utils.eag2012.Relations;
import eu.apenet.dpt.utils.eag2012.Repositorarea;
import eu.apenet.dpt.utils.eag2012.Repositorfound;
import eu.apenet.dpt.utils.eag2012.Repositorhist;
import eu.apenet.dpt.utils.eag2012.Repositorid;
import eu.apenet.dpt.utils.eag2012.Repositories;
import eu.apenet.dpt.utils.eag2012.Repositorsup;
import eu.apenet.dpt.utils.eag2012.Repository;
import eu.apenet.dpt.utils.eag2012.RepositoryType;
import eu.apenet.dpt.utils.eag2012.Reproductionser;
import eu.apenet.dpt.utils.eag2012.ResearchServices;
import eu.apenet.dpt.utils.eag2012.ResourceRelation;
import eu.apenet.dpt.utils.eag2012.Restaccess;
import eu.apenet.dpt.utils.eag2012.Restorationlab;
import eu.apenet.dpt.utils.eag2012.Rule;
import eu.apenet.dpt.utils.eag2012.Searchroom;
import eu.apenet.dpt.utils.eag2012.Secondem;
import eu.apenet.dpt.utils.eag2012.Serialpub;
import eu.apenet.dpt.utils.eag2012.Services;
import eu.apenet.dpt.utils.eag2012.Street;
import eu.apenet.dpt.utils.eag2012.Techservices;
import eu.apenet.dpt.utils.eag2012.Telephone;
import eu.apenet.dpt.utils.eag2012.TermsOfUse;
import eu.apenet.dpt.utils.eag2012.Timetable;
import eu.apenet.dpt.utils.eag2012.ToDate;
import eu.apenet.dpt.utils.eag2012.ToursSessions;
import eu.apenet.dpt.utils.eag2012.UseDates;
import eu.apenet.dpt.utils.eag2012.Webpage;
import eu.apenet.dpt.utils.eag2012.WorkPlaces;

/**
 * Class for fill EAG2012 JAXB object.
 */
public class CreateEAG2012 {
	/**
	 * EAG2012 internal object.
	 */
	protected Eag2012 eag2012;

	/**
	 * EAG2012 JAXB object.
	 */
	protected Eag eag;

	// Content for aeg.
    private static final String EAG_XMLNS = "http://www.archivesportaleurope.net/profiles/APEnet_EAG/";
	private static final String XML_AUDIENCE = "external";
	private static final String XML_BASE = "http://www.archivesportaleurope.net/";

	private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";

	// Content for agencyType.
	private static final String AGENT_TYPE_MACHINE = "machine";
	private static final String AGENT_TYPE_HUMAN = "human";

	// Content for otherRecordId.
	private static final String LOCAL_TYPE = "localId";

	// Constants for TABs indexes.
	public static final String TAB_YOUR_INSTITUTION = "your_institution";
	public static final String TAB_IDENTITY = "identity";
	public static final String TAB_CONTACT = "contact";
	public static final String TAB_ACCESS_AND_SERVICES = "access_and_services";
	public static final String TAB_DESCRIPTION = "description";
	public static final String TAB_CONTROL = "control";
	public static final String TAB_RELATION = "relation";

	// Constants for address.
	public static final String VISITORS_ADDRESS = "visitors address";
	public static final String POSTAL_ADDRESS = "postal address";

	// Content for eventType.
	private static final String EVENTTYPE_CANCELLED = "cancelled";
	private static final String EVENTTYPE_CREATED = "created";
	private static final String EVENTTYPE_DELETED = "deleted";
	private static final String EVENTTYPE_DELETED_MERGED = "deletedMerged";
	private static final String EVENTTYPE_DELETED_REPLACED = "deletedReplaced";
	private static final String EVENTTYPE_DELETED_SPLIT = "deletedSplit";
	private static final String EVENTTYPE_DERIVED = "derived";
	private static final String EVENTTYPE_REVISED = "revised";
	private static final String EVENTTYPE_NEW = "new";

	// Content for units.
	private static final String UNIT_SITE = "site";
	private static final String UNIT_BOOK = "book";
	private static final String UNIT_TITLE = "title";
	private static final String UNIT_SQUARE_METRE = "squaremetre";
	private static final String UNIT_LINEAR_METRE = "linearmetre";

	// Constants for section indexes.
	public static final String ROOT = "root";
	public static final String LANGUAGE_DECLARATIONS = "languageDeclaration";
	public static final String RESOURCE_RELATION = "resourceRelation";
	public static final String EAG_RELATION = "eagRelation";
	public static final String LOCAL_TYPE_DECLARATION = "localTypeDeclaration";
	public static final String CONVENTION_DECLARATION = "conventionDeclaration";
	public static final String MAINTENANCE_AGENCY = "maintenanceAgency";
	public static final String SOURCE = "source";
	public static final String REPOSITORY = "repository";
	public static final String OTHER_SERVICES = "otherServices";
	public static final String TOURS_SESSIONS = "toursSessions";
	public static final String EXHIBITION = "exhibition";
	public static final String REFRESHMENT = "refreshment";
	public static final String RESTORATION_LAB = "restorationlab";
	public static final String REPRODUCTIONSER = "reproductionser";
	public static final String INTERNET_ACCESS = "internetAccess";
	public static final String RESEARCH_SERVICES = "researchServices";
	public static final String HOLDINGS = "holdings";
	public static final String BUILDING = "building";
	public static final String REPOSITORHIST = "repositorhist";
	public static final String LIBRARY = "library";
	public static final String SEARCHROOM = "searchroom";

	// Constants for subsection indexes
	public static final String ROOT_SUBSECTION = "root_section";
	public static final String WORKING_PLACES = "workingPlaces";
	public static final String COMPUTER_PLACES = "computerPlaces";
	public static final String MICROFILM = "microfilm";
	public static final String MONOGRAPHIC_PUBLICATION ="monographicPublication";
    public static final String SERIAL_PUBLICATION ="serialPublication";
    public static final String REPOSITOR_FOUND ="repositorfound";
    public static final String REPOSITOR_SUP ="repositorsup";
	public static final String BUILDING_AREA = "building_area";
	public static final String BUILDING_LENGTH = "building_length";
	public static final String HOLDING_EXTENT = "holding_extent";
	public static final String HOLDING_SUBSECTION = "holding_subsection";

	/**
	 * Constructor.
	 */
	public CreateEAG2012(final Eag2012 eag2012) {
		super();
		this.eag2012 = eag2012;
		this.eag = new Eag();
	}

	/**
	 * Input method.
	 *
	 * @return The EAG2012 JAXB object.
	 */
	public Eag fillEAG2012() {
		// Constructs elements.
		constructAllParentElements();
		// Fill "Control" element.
		fillControl();
		// Fill "Archguide" element.
		fillArchguide();
		// Fill "Relations" element.
		fillRelations();

		return this.eag;
	}

	/**
	 * Method to construct all parent elements.
	 */
	private void constructAllParentElements() {
		// Fill EAG attibutes.
		this.eag.setAudience(CreateEAG2012.XML_AUDIENCE);

		// Cosntruct "control".
		if (this.eag.getControl() == null) {
			this.eag.setControl(new Control());
		}

		// Constructs "archguide".
		if (this.eag.getArchguide() == null) {
			this.eag.setArchguide(new Archguide());
		}

		// Constructs "relations".
		if (this.eag.getRelations() == null) {
			this.eag.setRelations(new Relations());
		}
	}

	/**
	 * Method to fill "Control" element.
	 */
	private void fillControl() {
		// eag/control/recordId
		if (this.eag.getControl().getRecordId() == null) {
			this.eag.getControl().setRecordId(new RecordId());
		}
		this.eag.getControl().getRecordId().setValue(this.eag2012.getRecordIdValue());
		// eag/control/otherRecordId
		if (this.eag2012.getOtherRecordIdValue() != null) {
			for (int i = 0; i < this.eag2012.getOtherRecordIdValue().size(); i++) {
				OtherRecordId otherRecordId = new OtherRecordId();
				otherRecordId.setValue(this.eag2012.getOtherRecordIdValue().get(i));
				otherRecordId.setLocalType(CreateEAG2012.LOCAL_TYPE);
				this.eag.getControl().getOtherRecordId().add(otherRecordId);
			}
		}

		// eag/control/maintenanceAgency
		if (this.eag.getControl().getMaintenanceAgency() == null) {
			this.eag.getControl().setMaintenanceAgency(new MaintenanceAgency());
		}

/* TODO:		// eag/control/maintenanceAgency/agencyCode
		if (this.eag.getControl().getMaintenanceAgency().getAgencyCode() == null) {
			this.eag.getControl().getMaintenanceAgency().setAgencyCode(new AgencyCode());
		}
		this.eag.getControl().getMaintenanceAgency().getAgencyCode().setContent(this.eag2012.getAgencyCodeValue());
*/
		// eag/control/maintenanceAgency/agencyName
		if (this.eag.getControl().getMaintenanceAgency().getAgencyName() == null) {
			this.eag.getControl().getMaintenanceAgency().setAgencyName(new AgencyName());
		}
		this.eag.getControl().getMaintenanceAgency().getAgencyName().setContent(this.eag2012.getAgencyNameValue());
		this.eag.getControl().getMaintenanceAgency().getAgencyName().setLang(this.eag2012.getAgencyNameLang());

		// eag/control/maintenanceAgency/otherAgencyCode
		if (this.eag2012.getOtherAgencyCodeValue() != null && !this.eag2012.getOtherAgencyCodeValue().isEmpty()) {
			for (int i = 0; i < this.eag2012.getOtherRecordIdValue().size(); i++) {
				OtherAgencyCode otherAgencyCode = new OtherAgencyCode();
				otherAgencyCode.setContent(this.eag2012.getOtherAgencyCodeValue().get(i));
// TODO:				otherAgencyCode.setLocalType(this.eag2012.getOtherAgencyCodeLocalType().get(i));
				this.eag.getControl().getMaintenanceAgency().getOtherAgencyCode().add(otherAgencyCode);
			}
		}

		// eag/control/maintenanceAgency/descriptiveNote
// TODO:

		// eag/control/maintenanceStatus
		if (this.eag.getControl().getMaintenanceStatus() == null) {
			MaintenanceStatus maintenanceStatus = new MaintenanceStatus();
			maintenanceStatus.setValue(CreateEAG2012.EVENTTYPE_NEW);
			this.eag.getControl().setMaintenanceStatus(maintenanceStatus);
		} else {
			this.eag.getControl().getMaintenanceStatus().setValue(CreateEAG2012.EVENTTYPE_REVISED);
		}

		// eag/control/maintenanceHistory
		if (this.eag.getControl().getMaintenanceHistory() == null) {
			this.eag.getControl().setMaintenanceHistory(new MaintenanceHistory());
		}

		// eag/control/maintenanceHistory/maintenanceEvent
		MaintenanceEvent maintenanceEvent = new MaintenanceEvent();

		// eag/control/maintenanceHistory/maintenanceEvent/agent
		if (maintenanceEvent.getAgent() == null) {
			maintenanceEvent.setAgent(new Agent());
		}
		maintenanceEvent.getAgent().setContent(this.eag2012.getAgentValue());

		// eag/control/maintenanceHistory/maintenanceEvent/agentType

		// eag/control/maintenanceHistory/maintenanceEvent/eventDateTime

		// eag/control/maintenanceHistory/maintenanceEvent/eventType

		// MaintenanceHistory
		this.eag.getControl().getMaintenanceHistory().getMaintenanceEvent().add(maintenanceEvent);

		// eag/control/languageDeclarations

		// eag/control/conventionDeclaration

		// eag/control/localControl

		// eag/control/localTypeDeclaration

		// eag/control/publicationStatus

		// eag/control/sources

	}

	/**
	 * Method to fill "Archguide" element.
	 */
	private void fillArchguide() {
		// Fill "Identity" element.
		if (this.eag.getArchguide().getIdentity() == null) {
			this.eag.getArchguide().setIdentity(new Identity());
		}
		fillIdentity();
		// Fill "Desc" element.
		if (this.eag.getArchguide().getDesc() == null) {
			this.eag.getArchguide().setDesc(new Desc());
		}
		fillDesc();
	}

	/**
	 * Method to fill "Relations" element.
	 */
	private void fillRelations() {
		// eag/relations

		// eag/relations/resourceRelation
		if (!this.eag2012.getResourceRelationHref().isEmpty()) {
			for (int i = 0; i < this.eag2012.getResourceRelationHref().size(); i++) {
				ResourceRelation resourceRelation = new ResourceRelation();
				resourceRelation.setHref(this.eag2012.getResourceRelationHref().get(i));
				if (this.eag2012.getResourceRelationResourceRelationType() != null
						&& !this.eag2012.getResourceRelationResourceRelationType().isEmpty()) {
					resourceRelation.setResourceRelationType(this.eag2012.getResourceRelationResourceRelationType().get(i));
				}
				RelationEntry relationEntry = new RelationEntry();
				relationEntry.setContent(this.eag2012.getRelationEntryValue().get(i));
				resourceRelation.setRelationEntry(relationEntry);
				this.eag.getRelations().getResourceRelation().add(resourceRelation);
			}
		}

		// eag/relations/eagRelation
		
	}

	/**
	 * Method to fill "Identity" element.
	 */
	private void fillIdentity() {
		// eag/archguide/identity/repositorid/countrycode
		if (this.eag.getArchguide().getIdentity().getRepositorid() == null) {
			this.eag.getArchguide().getIdentity().setRepositorid(new Repositorid());
		}
		this.eag.getArchguide().getIdentity().getRepositorid().setCountrycode(this.eag2012.getRepositoridCountrycode());
		// eag/archguide/identity/repositorid/repositorycode
		this.eag.getArchguide().getIdentity().getRepositorid().setRepositorycode(this.eag2012.getRepositoridRepositorycode());
		// eag/archguide/identity/otherRepositorid
		if (this.eag.getArchguide().getIdentity().getOtherRepositorId() == null) {
			this.eag.getArchguide().getIdentity().setOtherRepositorId(new OtherRepositorId());
		}
		this.eag.getArchguide().getIdentity().getOtherRepositorId().setContent(this.eag2012.getOtherRepositorId());
		// eag/archguide/identity/autform
		if (this.eag2012.getAutformValue() != null) {
			for (int i = 0; i < this.eag2012.getAutformValue().size(); i++) {
				Autform autform = new Autform();
				// eag/archguide/identity/autform
				autform.setContent(this.eag2012.getAutformValue().get(i));
				// eag/archguide/identity/autform/lang
				autform.setLang(this.eag2012.getAutformLang().get(i));
	
				this.eag.getArchguide().getIdentity().getAutform().add(autform);
			}
		}
		// eag/archguide/identity/parform
		if (this.eag2012.getParformValue() != null
				&& this.eag2012.getParformValue().size() > 0) {
			for (int i = 0; i < this.eag2012.getParformValue().size(); i++) {
				if (this.eag2012.getParformValue().get(i) != null
						&& !this.eag2012.getParformValue().get(i).isEmpty()) {
					Parform parform = new Parform();
					// eag/archguide/identity/parform
					parform.setContent(this.eag2012.getParformValue().get(i));
					// eag/archguide/identity/parform/lang
					parform.setLang(this.eag2012.getParformLang().get(i));
		
					this.eag.getArchguide().getIdentity().getParform().add(parform);
				}
			}
		}

		// eag/archguide/identity/nonpreform
		if (this.eag2012.getNonpreformValue() != null){
			for (int i = 0; i < this.eag2012.getNonpreformValue().size(); i++) {
				Nonpreform nonpreform = new Nonpreform();
				nonpreform.setLang(this.eag2012.getNonpreformLang().get(i));
				// TODO: Review schema.
				nonpreform.getContent().add(this.eag2012.getNonpreformValue().get(i));

// TODO:				// eag/archguide/identity/nonpreform/dates
//				getAllDates(nonpreform);

				this.eag.getArchguide().getIdentity().getNonpreform().add(nonpreform);
			}
		}
		
		// eag/archguide/identity/repositoryType
		if (this.eag2012.getRepositoryTypeValue() != null) {
			for (int i = 0; i < this.eag2012.getRepositoryTypeValue().size(); i++) {
				if (this.eag2012.getRepositoryTypeValue().get(i) != null
						&& !this.eag2012.getRepositoryTypeValue().get(i).isEmpty()) {
					String[] repositoryTypeList = this.eag2012.getRepositoryTypeValue().get(i).split("_");
					for (int j = 0; j < repositoryTypeList.length; j++) {
						RepositoryType repositoryType = new RepositoryType();
						repositoryType.setValue(repositoryTypeList[j]);
	
						this.eag.getArchguide().getIdentity().getRepositoryType().add(repositoryType);
					}
				}
			}
		}
	}

	/**
	 * Method to fill "Desc" element.
	 */
	private void fillDesc() {
		// Create repositories element.
		if (this.eag.getArchguide().getDesc().getRepositories() == null) {
			this.eag.getArchguide().getDesc().setRepositories(new Repositories());
		}
		// Create repository elements.
		if (this.eag.getArchguide().getDesc().getRepositories().getRepository().isEmpty()) {
			List<Repository> repositoryList = new ArrayList<Repository>();

			// Add institution repository.
			repositoryList.add(new Repository());

			// Add other repositories.
			if (this.eag2012.getRepositoryNameValue() != null) {
				for (int i = 0; i < this.eag2012.getRepositoryNameValue().size(); i++) {
					repositoryList.add(new Repository());
				}
			}

			this.eag.getArchguide().getDesc().getRepositories().setRepository(repositoryList);
		}

		// eag/archguide/identity/nonpreform/dates &&...
// TODO: Structure under revision.
		getAllDates();

		// eag/archguide/desc/repositories/repository/repositoryName

		// eag/archguide/desc/repositories/repository/repositoryRole

		// eag/archguide/desc/repositories/repository/geogarea
		if (this.eag2012.getGeogareaValue() != null) {
			Geogarea geogarea = new Geogarea();
			geogarea.setValue(this.eag2012.getGeogareaValue());

			// For every repository.
			for (int i = 0; i < this.eag.getArchguide().getDesc().getRepositories().getRepository().size(); i ++) {
				this.eag.getArchguide().getDesc().getRepositories().getRepository().get(i).setGeogarea(geogarea);
			}
		}

		// eag/archguide/desc/repositories/repository/location
		if (this.eag2012.getStreetLang() != null) {
			for (int i =0; i < this.eag2012.getCountryLang().size(); i++) {
				if (this.eag2012.getStreetLang().get(i) != null) {
					// Recover repository.
					Repository repository = this.eag.getArchguide().getDesc().getRepositories().getRepository().get(i);

					// Visitor address maps.
					Map<String, List<String>> streetLangMap = this.eag2012.getStreetLang().get(i);
					Map<String, List<String>> latitudeMap = this.eag2012.getLocationLatitude().get(i);
					Map<String, List<String>> longitudeMap = this.eag2012.getLocationLongitude().get(i);
					Map<String, List<String>> countryMap = this.eag2012.getCountryValue().get(i);
					Map<String, List<String>> citiesMap = this.eag2012.getCitiesValue().get(i);
					Map<String, List<String>> streetMap = this.eag2012.getStreetValue().get(i);

					// Visitor address iterators.
					Iterator<String> streetLangIt = streetLangMap.keySet().iterator();
					Iterator<String> latitudeIt = latitudeMap.keySet().iterator();
					Iterator<String> longitudeIt = longitudeMap.keySet().iterator();
					Iterator<String> countryIt = countryMap.keySet().iterator();
					Iterator<String> citiesIt = citiesMap.keySet().iterator();
					Iterator<String> streetIt = streetMap.keySet().iterator();
					while (streetLangIt.hasNext()) {
						// Visitor address keys.
						String streetLangKey = streetLangIt.next();
						String latitudeKey = latitudeIt.next();
						String longitudeKey = longitudeIt.next();
						String countryKey = countryIt.next();
						String citiesKey = citiesIt.next();
						String streetKey = streetIt.next();

						// Rest of tabs.
						if (!streetLangKey.equalsIgnoreCase(CreateEAG2012.TAB_YOUR_INSTITUTION))  {
							// Visitor address lists.
							List<String> streetLangList = streetLangMap.get(streetLangKey);
							List<String> latitudeList = latitudeMap.get(latitudeKey);
							List<String> longitudeList = latitudeMap.get(longitudeKey);
							List<String> countryList = countryMap.get(countryKey);
							List<String> firstdemList = this.eag2012.getFirstdemValue().get(i);
							List<String> secondemList = this.eag2012.getSecondemValue().get(i);
							List<String> localentityList = this.eag2012.getLocalentityValue().get(i);
							List<String> citiesList = citiesMap.get(citiesKey);
							List<String> streetList = streetMap.get(streetKey);
							for (int j = 0; j < streetLangList.size(); j++) {
								Location location = new Location();
								String language = streetLangList.get(j);
								// eag/archguide/desc/repositories/repository/location/type
								location.setLocalType(CreateEAG2012.VISITORS_ADDRESS);
								if (latitudeList.get(j) != null
										&& !latitudeList.get(j).isEmpty()) {
									// eag/archguide/desc/repositories/repository/location/latitude
									location.setLatitude(latitudeList.get(j));
								}
								if (longitudeList.get(j) != null
										&& !longitudeList.get(j).isEmpty()) {
									// eag/archguide/desc/repositories/repository/location/longitude
									location.setLongitude(longitudeList.get(j));
								}
								// eag/archguide/desc/repositories/repository/location/country
								if (location.getCountry() == null) {
									location.setCountry(new Country());
								}
								location.getCountry().setContent(countryList.get(j));
								// eag/archguide/desc/repositories/repository/location/country/lang
								location.getCountry().setLang(language);
								if (firstdemList.get(j) != null
										&& !firstdemList.get(j).isEmpty()) {
									// eag/archguide/desc/repositories/repository/location/firstdem
									if (location.getFirstdem() == null) {
										location.setFirstdem(new Firstdem());
									}
									if (firstdemList.size() > j) {
										location.getFirstdem().setContent(firstdemList.get(j));
										// eag/archguide/desc/repositories/repository/location/firstdem/lang
										location.getFirstdem().setLang(language);
									}
								}
								if (secondemList.get(j) != null
										&& !secondemList.get(j).isEmpty()) {
									// eag/archguide/desc/repositories/repository/location/secondem
									if (location.getSecondem() == null) {
										location.setSecondem(new Secondem());
									}
									if (secondemList.size() > j) {
										location.getSecondem().setContent(secondemList.get(j));
										// eag/archguide/desc/repositories/repository/location/secondem/lang
										location.getSecondem().setLang(language);
									}
								}
								// eag/archguide/desc/repositories/repository/location/municipalityPostalcode
								if (location.getMunicipalityPostalcode() == null) {
									location.setMunicipalityPostalcode(new MunicipalityPostalcode());
								}
								location.getMunicipalityPostalcode().setContent(citiesList.get(j));
								// eag/archguide/desc/repositories/repository/location/municipalityPostalcode/lang
								location.getMunicipalityPostalcode().setLang(language);
								if (localentityList.get(j) != null
										&& !localentityList.get(j).isEmpty()) {
									// eag/archguide/desc/repositories/repository/location/localentity
									if (location.getLocalentity() == null) {
										location.setLocalentity(new Localentity());
									}
									if (localentityList.size() > j) {
										location.getLocalentity().setContent(localentityList.get(j));
										// eag/archguide/desc/repositories/repository/location/localentity/lang
										location.getLocalentity().setLang(language);
									}
								}
								// eag/archguide/desc/repositories/repository/location/street
								if (location.getStreet() == null) {
									location.setStreet(new Street());
								}
								location.getStreet().setContent(streetList.get(j));
								// eag/archguide/desc/repositories/repository/location/street/lang
								location.getStreet().setLang(language);
	
								repository.getLocation().add(location);
							}
						}
					}
				}

				if (this.eag2012.getPostalStreetLang().get(i) != null) {
					// Recover repository.
					Repository repository = this.eag.getArchguide().getDesc().getRepositories().getRepository().get(i);

					// Postal address maps.
					Map<String, List<String>> postalStreetLangMap = this.eag2012.getPostalStreetLang().get(i);
					Map<String, List<String>> postalCountryMap = this.eag2012.getCountryValue().get(i);
					Map<String, List<String>> postalCitiesMap = this.eag2012.getMunicipalityPostalcodeValue().get(i);
					Map<String, List<String>> postalStreetMap = this.eag2012.getPostalStreetValue().get(i);

					// Postal address iterators.
					Iterator<String>  postalStreetLangIt = postalStreetLangMap.keySet().iterator();
					Iterator<String>  postalCountryIt = postalCountryMap.keySet().iterator();
					Iterator<String>  postalCitiesIt = postalCitiesMap.keySet().iterator();
					Iterator<String>  postalStreetIt = postalStreetMap.keySet().iterator();
					while (postalStreetLangIt.hasNext()) {
						// Postal address keys.
						String postalStreetLangKey = postalStreetLangIt.next();
						String postalCountryKey = postalCountryIt.next();
						String postalCitiesKey = postalCitiesIt.next();
						String postalStreetKey = postalStreetIt.next();

						// Rest of tabs.
						if (!postalStreetLangKey.equalsIgnoreCase(CreateEAG2012.TAB_YOUR_INSTITUTION))  {
							// Postal address lists.
							List<String> postalStreetLangList = postalStreetLangMap.get(postalStreetLangKey);
							List<String> postalCountryList = postalCountryMap.get(postalCountryKey);
							List<String> postalCitiesList = postalCitiesMap.get(postalCitiesKey);
							List<String> postalStreetList = postalStreetMap.get(postalStreetKey);
							for (int j = 0; j < postalStreetLangList.size(); j++) {
								Location location = new Location();
								String language = postalStreetLangList.get(j);
								// eag/archguide/desc/repositories/repository/location/type
								location.setLocalType(CreateEAG2012.POSTAL_ADDRESS);
								// eag/archguide/desc/repositories/repository/location/country
								if (location.getCountry() == null) {
									location.setCountry(new Country());
								}
								location.getCountry().setContent(postalCountryList.get(j));
								// eag/archguide/desc/repositories/repository/location/country/lang
								location.getCountry().setLang(language);
								// eag/archguide/desc/repositories/repository/location/municipalityPostalcode
								if (location.getMunicipalityPostalcode() == null) {
									location.setMunicipalityPostalcode(new MunicipalityPostalcode());
								}
								location.getMunicipalityPostalcode().setContent(postalCitiesList.get(j));
								// eag/archguide/desc/repositories/repository/location/municipalityPostalcode/lang
								location.getMunicipalityPostalcode().setLang(language);
								// eag/archguide/desc/repositories/repository/location/street
								if (location.getStreet() == null) {
									location.setStreet(new Street());
								}
								location.getStreet().setContent(postalStreetList.get(j));
								// eag/archguide/desc/repositories/repository/location/street/lang
								location.getStreet().setLang(language);
			
								repository.getLocation().add(location);
							}
						}
					}
				}
			}
		}

		// eag/archguide/desc/repositories/repository/telephone
		if (this.eag2012.getTelephoneValue() != null) {
			for (int i = 0; i < this.eag2012.getTelephoneValue().size(); i++) {
				Map<String, Map<String, List<String>>> tabsMap = this.eag2012.getTelephoneValue().get(i);
				// Repository.
				Repository repository = this.eag.getArchguide().getDesc().getRepositories().getRepository().get(i);

				Iterator<String> tabsIt = tabsMap.keySet().iterator();
				while (tabsIt.hasNext()) {
					String tabKey = tabsIt.next();
					Map<String, List<String>> sectionMap = tabsMap.get(tabKey);
					Iterator<String> sectionIt = sectionMap.keySet().iterator();
					while (sectionIt.hasNext()) {
						String sectionKey = sectionIt.next();
						List<String> telephoneList = sectionMap.get(sectionKey);
						for (int k = 0; k < telephoneList.size(); k++) {
							if (telephoneList.get(k) == null
									|| telephoneList.get(k).isEmpty()) {
								break;
							}

							Telephone telephone = new Telephone();
							telephone.setContent(telephoneList.get(k));
							if (CreateEAG2012.ROOT.equalsIgnoreCase(sectionKey)) {
								repository.getTelephone().add(telephone);
							}

							// eag/archguide/desc/repositories/repository/services/searchroom
							if (CreateEAG2012.SEARCHROOM.equalsIgnoreCase(sectionKey)) {
								if (repository.getServices() == null) {
									repository.setServices(new Services());
								}
								if (repository.getServices().getSearchroom() == null) {
									repository.getServices().setSearchroom(new Searchroom());
								}
								if (repository.getServices().getSearchroom().getContact() == null) {
									repository.getServices().getSearchroom().setContact(new Contact());
								}
									repository.getServices().getSearchroom().getContact().getTelephone().add(telephone);
							}

							// eag/archguide/desc/repositories/repository/services/library
							if (CreateEAG2012.LIBRARY.equalsIgnoreCase(sectionKey)) {
								if (repository.getServices() == null) {
									repository.setServices(new Services());
								}
								if (repository.getServices().getLibrary() == null) {
									repository.getServices().setLibrary(new Library());
								}
								if (repository.getServices().getLibrary().getContact() == null) {
									repository.getServices().getLibrary().setContact(new Contact());
								}
									repository.getServices().getLibrary().getContact().getTelephone().add(telephone);
							}

							// eag/archguide/desc/repositories/repository/services/techservices/restorationlab
							if (CreateEAG2012.RESTORATION_LAB.equalsIgnoreCase(sectionKey)) {
								if (repository.getServices() == null) {
									repository.setServices(new Services());
								}
								if (repository.getServices().getTechservices() == null) {
									repository.getServices().setTechservices(new Techservices());
								}
								if (repository.getServices().getTechservices().getRestorationlab() == null) {
									repository.getServices().getTechservices().setRestorationlab(new Restorationlab());
								}
								if (repository.getServices().getTechservices().getRestorationlab().getContact() == null) {
									repository.getServices().getTechservices().getRestorationlab().setContact(new Contact());
								}
									repository.getServices().getTechservices().getRestorationlab().getContact().getTelephone().add(telephone);
							}

							// eag/archguide/desc/repositories/repository/services/techservices/reproductionser
							if (CreateEAG2012.REPRODUCTIONSER.equalsIgnoreCase(sectionKey)) {
								if (repository.getServices() == null) {
									repository.setServices(new Services());
								}
								if (repository.getServices().getTechservices() == null) {
									repository.getServices().setTechservices(new Techservices());
								}
								if (repository.getServices().getTechservices().getReproductionser() == null) {
									repository.getServices().getTechservices().setReproductionser(new Reproductionser());
								}
								if (repository.getServices().getTechservices().getReproductionser().getContact() == null) {
									repository.getServices().getTechservices().getReproductionser().setContact(new Contact());
								}
									repository.getServices().getTechservices().getReproductionser().getContact().getTelephone().add(telephone);
							}
						}
					}
				}
			}
		}

		// eag/archguide/desc/repositories/repository/fax
		if (this.eag2012.getFaxValue() != null) {
			for (int i = 0; i < this.eag2012.getFaxValue().size(); i++) {
				Map<String, List<String>> tabsMap = this.eag2012.getFaxValue().get(i);
				// Repository.
				Repository repository = this.eag.getArchguide().getDesc().getRepositories().getRepository().get(i);

				Iterator<String> tabsIt = tabsMap.keySet().iterator();
				while (tabsIt.hasNext()) {
					String tabKey = tabsIt.next();
					if (tabKey == CreateEAG2012.TAB_CONTACT) {
						List<String> faxList = tabsMap.get(tabKey);
						for (int k = 0; k < faxList.size(); k++) {
							if (faxList.get(k) != null
									&& !faxList.get(k).isEmpty()) {
								Fax fax = new Fax();
								fax.setContent(faxList.get(k));
								repository.getFax().add(fax);
							}
						}
					}
				}
			}
		}

		// eag/archguide/desc/repositories/repository/email (href & lang)
		if (this.eag2012.getEmailValue() != null || this.eag2012.getEmailHref() != null) {
			for (int i = 0; i < this.eag2012.getEmailValue().size(); i++) {
				Map<String, Map<String, List<String>>> tabsValueMap = this.eag2012.getEmailValue().get(i);
				Map<String, Map<String, List<String>>> tabsHrefMap = this.eag2012.getEmailHref().get(i);
				// Repository
				Repository repository = this.eag.getArchguide().getDesc().getRepositories().getRepository().get(i);

				Iterator<String> tabsValueIt = tabsValueMap.keySet().iterator();
				Iterator<String> tabsHrefIt = tabsHrefMap.keySet().iterator();
				while (tabsValueIt.hasNext()) {
					String tabValueKey = tabsValueIt.next();
					String tabHrefKey = tabsHrefIt.next();
					Map<String, List<String>> sectionValueMap = tabsValueMap.get(tabValueKey);
					Map<String, List<String>> sectionHrefMap = tabsHrefMap.get(tabHrefKey);
					Iterator<String> sectionValueIt = sectionValueMap.keySet().iterator();
					Iterator<String> sectionHrefIt = sectionHrefMap.keySet().iterator();
					while (sectionValueIt.hasNext()) {
						String sectionValueKey = sectionValueIt.next();
						String sectionHrefKey = sectionHrefIt.next();
						List<String> emailValueList = sectionValueMap.get(sectionValueKey);
						List<String> emailHrefList = sectionHrefMap.get(sectionHrefKey);

						for (int k = 0; k < emailValueList.size(); k++) {
							if (emailValueList.get(k) == null
									|| emailValueList.get(k).isEmpty()) {
								break;
							}

							Email email = new Email();
							email.setContent(emailValueList.get(k));
							email.setHref(emailHrefList.get(k));

							if (CreateEAG2012.ROOT.equalsIgnoreCase(sectionValueKey)
									&& CreateEAG2012.ROOT.equalsIgnoreCase(sectionHrefKey)) {
									repository.getEmail().add(email);
							}

							// eag/archguide/desc/repositories/repository/services/searchroom
							if (CreateEAG2012.SEARCHROOM.equalsIgnoreCase(sectionValueKey)
									&& CreateEAG2012.SEARCHROOM.equalsIgnoreCase(sectionHrefKey)) {
								if (repository.getServices() == null) {
									repository.setServices(new Services());
								}
								if (repository.getServices().getSearchroom() == null) {
									repository.getServices().setSearchroom(new Searchroom());
								}
								if (repository.getServices().getSearchroom().getContact() == null) {
									repository.getServices().getSearchroom().setContact(new Contact());
								}
	
								repository.getServices().getSearchroom().getContact().getEmail().add(email);
							}

							// eag/archguide/desc/repositories/repository/services/library
							if (CreateEAG2012.LIBRARY.equalsIgnoreCase(sectionValueKey)
									&& CreateEAG2012.LIBRARY.equalsIgnoreCase(sectionHrefKey)) {
								if (repository.getServices() == null) {
									repository.setServices(new Services());
								}
								if (repository.getServices().getLibrary() == null) {
									repository.getServices().setLibrary(new Library());
								}
								if (repository.getServices().getLibrary().getContact() == null) {
									repository.getServices().getLibrary().setContact(new Contact());
								}
	
								repository.getServices().getLibrary().getContact().getEmail().add(email);
							}

							// eag/archguide/desc/repositories/repository/services/techservices/restorationlab
							if (CreateEAG2012.RESTORATION_LAB.equalsIgnoreCase(sectionValueKey)
									&& CreateEAG2012.RESTORATION_LAB.equalsIgnoreCase(sectionHrefKey)) {
								if (repository.getServices() == null) {
									repository.setServices(new Services());
								}
								if (repository.getServices().getTechservices() == null) {
									repository.getServices().setTechservices(new Techservices());
								}
								if (repository.getServices().getTechservices().getRestorationlab() == null) {
									repository.getServices().getTechservices().setRestorationlab(new Restorationlab());
								}
								if (repository.getServices().getTechservices().getRestorationlab().getContact() == null) {
									repository.getServices().getTechservices().getRestorationlab().setContact(new Contact());
								}
	
								repository.getServices().getTechservices().getRestorationlab().getContact().getEmail().add(email);
							}

							// eag/archguide/desc/repositories/repository/services/techservices/reproductionser
							if (CreateEAG2012.REPRODUCTIONSER.equalsIgnoreCase(sectionValueKey)
									&& CreateEAG2012.REPRODUCTIONSER.equalsIgnoreCase(sectionHrefKey)) {
								if (repository.getServices() == null) {
									repository.setServices(new Services());
								}
								if (repository.getServices().getTechservices() == null) {
									repository.getServices().setTechservices(new Techservices());
								}
								if (repository.getServices().getTechservices().getReproductionser() == null) {
									repository.getServices().getTechservices().setReproductionser(new Reproductionser());
								}
								if (repository.getServices().getTechservices().getReproductionser().getContact() == null) {
									repository.getServices().getTechservices().getReproductionser().setContact(new Contact());
								}
	
								repository.getServices().getTechservices().getReproductionser().getContact().getEmail().add(email);
							}
						}
					}
				}
			}
		}

		// eag/archguide/desc/repositories/repository/webpage
		if (this.eag2012.getWebpageValue() != null || this.eag2012.getWebpageHref() != null) {
			for (int i = 0; i < this.eag2012.getWebpageValue().size(); i++) {
				Map<String, Map<String, List<String>>> tabsValueMap = this.eag2012.getWebpageValue().get(i);
				Map<String, Map<String, List<String>>> tabsHrefMap = this.eag2012.getWebpageHref().get(i);
				// Repository
				Repository repository = this.eag.getArchguide().getDesc().getRepositories().getRepository().get(i);

				Iterator<String> tabsValueIt = tabsValueMap.keySet().iterator();
				Iterator<String> tabsHrefIt = tabsHrefMap.keySet().iterator();
				while (tabsValueIt.hasNext()) {
					String tabValueKey = tabsValueIt.next();
					String tabHrefKey = tabsHrefIt.next();
					Map<String, List<String>> sectionValueMap = tabsValueMap.get(tabValueKey);
					Map<String, List<String>> sectionHrefMap = tabsHrefMap.get(tabHrefKey);
					Iterator<String> sectionValueIt = sectionValueMap.keySet().iterator();
					Iterator<String> sectionHrefIt = sectionHrefMap.keySet().iterator();
					while (sectionValueIt.hasNext()) {
						String sectionValueKey = sectionValueIt.next();
						String sectionHrefKey = sectionHrefIt.next();
						List<String> webpageValueList = sectionValueMap.get(sectionValueKey);
						List<String> webpageHrefList = sectionHrefMap.get(sectionHrefKey);
						for (int k = 0; k < webpageValueList.size(); k++) {
							if (webpageValueList.get(k) == null
									|| webpageValueList.get(k).isEmpty()) {
								break;
							}

							Webpage webpage= new Webpage();
							webpage.setContent(webpageValueList.get(k));
							webpage.setHref(webpageHrefList.get(k));
							
							if (CreateEAG2012.ROOT.equalsIgnoreCase(sectionValueKey)
									&& CreateEAG2012.ROOT.equalsIgnoreCase(sectionHrefKey)) {
								repository.getWebpage().add(webpage);
							}

							// eag/archguide/desc/repositories/repository/services/searchroom
							if (CreateEAG2012.SEARCHROOM.equalsIgnoreCase(sectionValueKey)
									&& CreateEAG2012.SEARCHROOM.equalsIgnoreCase(sectionHrefKey)) {
								if (repository.getServices() == null) {
									repository.setServices(new Services());
								}
								if (repository.getServices().getSearchroom() == null) {
									repository.getServices().setSearchroom(new Searchroom());
								}
	
								repository.getServices().getSearchroom().getWebpage().add(webpage);
							}

							// eag/archguide/desc/repositories/repository/services/library
							if (CreateEAG2012.LIBRARY.equalsIgnoreCase(sectionValueKey)
									&& CreateEAG2012.LIBRARY.equalsIgnoreCase(sectionHrefKey)) {
								if (repository.getServices() == null) {
									repository.setServices(new Services());
								}
								if (repository.getServices().getLibrary() == null) {
									repository.getServices().setLibrary(new Library());
								}
	
								repository.getServices().getLibrary().getWebpage().add(webpage);
							}

							// eag/archguide/desc/repositories/repository/services/techservices/restorationlab
							if (CreateEAG2012.RESTORATION_LAB.equalsIgnoreCase(sectionValueKey)
									&& CreateEAG2012.RESTORATION_LAB.equalsIgnoreCase(sectionHrefKey)) {
								if (repository.getServices() == null) {
									repository.setServices(new Services());
								}
								if (repository.getServices().getTechservices() == null) {
									repository.getServices().setTechservices(new Techservices());
								}
								if (repository.getServices().getTechservices().getRestorationlab() == null) {
									repository.getServices().getTechservices().setRestorationlab(new Restorationlab());
								}
	
								repository.getServices().getTechservices().getRestorationlab().getWebpage().add(webpage);
							}

							// eag/archguide/desc/repositories/repository/services/techservices/reproductionser
							if (CreateEAG2012.REPRODUCTIONSER.equalsIgnoreCase(sectionValueKey)
									&& CreateEAG2012.REPRODUCTIONSER.equalsIgnoreCase(sectionHrefKey)) {
								if (repository.getServices() == null) {
									repository.setServices(new Services());
								}
								if (repository.getServices().getTechservices() == null) {
									repository.getServices().setTechservices(new Techservices());
								}
								if (repository.getServices().getTechservices().getReproductionser() == null) {
									repository.getServices().getTechservices().setReproductionser(new Reproductionser());
								}
	
								repository.getServices().getTechservices().getReproductionser().getWebpage().add(webpage);
							}

							// eag/archguide/desc/repositories/repository/services/recreationalServices/exhibition
							if (CreateEAG2012.EXHIBITION.equalsIgnoreCase(sectionValueKey)
									&& CreateEAG2012.EXHIBITION.equalsIgnoreCase(sectionHrefKey)) {
								if (repository.getServices() == null) {
									repository.setServices(new Services());
								}
								if (repository.getServices().getRecreationalServices() == null) {
									repository.getServices().setRecreationalServices(new RecreationalServices());
								}
								if (repository.getServices().getRecreationalServices().getExhibition().size() < (k + 1)) {
									repository.getServices().getRecreationalServices().getExhibition().add(new Exhibition());
								}

								Exhibition exhibition = repository.getServices().getRecreationalServices().getExhibition().get(k);
								getDescriptiveNote(exhibition);
								exhibition.setWebpage(webpage);
							}

							// eag/archguide/desc/repositories/repository/services/recreationalServices/toursSessions
							if (CreateEAG2012.TOURS_SESSIONS.equalsIgnoreCase(sectionValueKey)
									&& CreateEAG2012.TOURS_SESSIONS.equalsIgnoreCase(sectionHrefKey)) {
								if (repository.getServices() == null) {
									repository.setServices(new Services());
								}
								if (repository.getServices().getRecreationalServices() == null) {
									repository.getServices().setRecreationalServices(new RecreationalServices());
								}
								if (repository.getServices().getRecreationalServices().getToursSessions().size() < (k + 1)) {
									repository.getServices().getRecreationalServices().getToursSessions().add(new ToursSessions());
								}

								ToursSessions toursSessions = repository.getServices().getRecreationalServices().getToursSessions().get(k);
								getDescriptiveNote(toursSessions);
								toursSessions.setWebpage(webpage);
							}

							// eag/archguide/desc/repositories/repository/services/recreationalServices/otherServices
							if (CreateEAG2012.OTHER_SERVICES.equalsIgnoreCase(sectionValueKey)
									&& CreateEAG2012.OTHER_SERVICES.equalsIgnoreCase(sectionHrefKey)) {
								if (repository.getServices() == null) {
									repository.setServices(new Services());
								}
								if (repository.getServices().getRecreationalServices() == null) {
									repository.getServices().setRecreationalServices(new RecreationalServices());
								}
								if (repository.getServices().getRecreationalServices().getOtherServices().size() < (k + 1)) {
									repository.getServices().getRecreationalServices().getOtherServices().add(new OtherServices());
								}

								OtherServices otherServices = repository.getServices().getRecreationalServices().getOtherServices().get(k);
								getDescriptiveNote(otherServices);
								otherServices.setWebpage(webpage);
							}
						}
					}
				}
			}
		}

		// eag/archguide/desc/repositories/repository/directions
		if (this.eag2012.getDirectionsValue() != null && this.eag2012.getDirectionsLang() != null
				&& this.eag2012.getCitationHref() != null ) {
			for (int i = 0; i < this.eag2012.getDirectionsValue().size(); i++) {
				List<String> directionsValueList = this.eag2012.getDirectionsValue().get(i);
				List<String> directionsLangList = this.eag2012.getDirectionsLang().get(i);
				List<String> citationHrefList = this.eag2012.getCitationHref().get(i);

				// Repository
				Repository repository = this.eag.getArchguide().getDesc().getRepositories().getRepository().get(i);

				for (int j = 0; j < directionsValueList.size(); j++) {
					Directions directions = new Directions();
					directions.getContent().add(directionsValueList.get(j));
					// eag/archguide/desc/repositories/repository/directions/lang
					directions.setLang(directionsLangList.get(j));
					// eag/archguide/desc/repositories/repository/directions/citation/href
					Citation citation = new Citation();
					citation.setHref(citationHrefList.get(j));

					directions.getContent().add(citation);
					repository.getDirections().add(directions);
				}
			}
		}

		// eag/archguide/desc/repositories/repository/repositorhist/descriptiveNote &&
		// eag/archguide/desc/repositories/repository/buildinginfo/building/descriptiveNote &&
		// eag/archguide/desc/repositories/repository/services/searchroom/researchServices/descriptiveNote &&
		// eag/archguide/desc/repositories/repository/services/recreationalServices/refreshment/descriptiveNote &&
		// eag/archguide/desc/repositories/repository/holdings/descriptiveNote
		getAllDescriptiveNote();

		// eag/archguide/desc/repositories/repository/repositorfound/rule &&  repositorsup/rule
		getAllRules();

		// eag/archguide/desc/repositories/repository/adminhierarchy
		if (this.eag2012.getAdminunitValue() != null
				&& this.eag2012.getAdminunitLang() != null) {
			for (int i = 0; i < this.eag2012.getAdminunitValue().size(); i++) {
				// Repository
				Repository repository = this.eag.getArchguide().getDesc().getRepositories().getRepository().get(i);
				if (repository.getAdminhierarchy() == null) {
					repository.setAdminhierarchy(new Adminhierarchy());
				}

				List<String> valuesList = this.eag2012.getAdminunitValue().get(i);
				List<String> langsList = this.eag2012.getAdminunitLang().get(i);
				for (int j = 0; j < valuesList.size(); j++) {
					Adminunit adminunit = new Adminunit();
					adminunit.setContent(valuesList.get(j));
					adminunit.setLang(langsList.get(j));

					repository.getAdminhierarchy().getAdminunit().add(adminunit);
				}
			}
		}

		// eag/archguide/desc/repositories/repository/holdings

		// eag/archguide/desc/repositories/repository/timetable/opening
		if (this.eag2012.getOpeningValue() != null) {
			for (int i = 0; i < this.eag2012.getOpeningValue().size(); i++) {
				Map<String, List<String>> tabsValueMap = this.eag2012.getOpeningValue().get(i);
				Map<String, List<String>> tabsLangMap = null;
				if (this.eag2012.getOpeningLang() != null && !this.eag2012.getOpeningLang().isEmpty()) {
					tabsLangMap = this.eag2012.getOpeningLang().get(i);
				}
				Repository repository = this.eag.getArchguide().getDesc().getRepositories().getRepository().get(i);
				if (repository.getTimetable() == null) {
					repository.setTimetable(new Timetable());
				}
				Iterator<String> openingValueIt = tabsValueMap.keySet().iterator();
				Iterator<String> openingLangIt = null;
				if (tabsLangMap != null) {
					openingLangIt = tabsLangMap.keySet().iterator();
				}
				while (openingValueIt.hasNext()) {
					String openingValueKey = openingValueIt.next();

					// Rest of tabs.
					if (!openingValueKey.equalsIgnoreCase(CreateEAG2012.TAB_YOUR_INSTITUTION))  {
						List<String> openingValueList = tabsValueMap.get(openingValueKey);
						List<String> openingLangList = null;
						if (openingLangIt != null) {
							openingLangList = tabsLangMap.get(openingLangIt.next());
						}
						for (int j = 0; j < openingValueList.size(); j++) {
							Opening opening = new Opening();
							opening.setContent(openingValueList.get(j));
							if (openingLangList != null) {
								opening.setLang(openingLangList.get(j));
							}
							repository.getTimetable().getOpening().add(opening);
						}
					}
				}
			}
		}

		// eag/archguide/desc/repositories/repository/timetable/closing
		if (this.eag2012.getClosingStandardDate() != null) {
			for (int i = 0; i < this.eag2012.getClosingStandardDate().size(); i++) {
				Map<String, List<String>> tabsValueMap = this.eag2012.getClosingStandardDate().get(i);
				Map<String, List<String>> tabsLangMap = null;
				if (this.eag2012.getClosingLang() != null && !this.eag2012.getClosingLang().isEmpty()) {
					tabsLangMap = this.eag2012.getClosingLang().get(i);
				}
				Repository repository = this.eag.getArchguide().getDesc().getRepositories().getRepository().get(i);
				if (repository.getTimetable() == null) {
					repository.setTimetable(new Timetable());
				}
				Iterator<String> closingValueIt = tabsValueMap.keySet().iterator();
				Iterator<String> closingLangIt = null;
				if (tabsLangMap != null) {
					closingLangIt =tabsLangMap.keySet().iterator();
				}
				while (closingValueIt.hasNext()) {
					List<String> closingValueList = tabsValueMap.get(closingValueIt.next());
					List<String> closingLangList = null;
					if (closingLangIt != null) {
						closingLangList = tabsLangMap.get(closingLangIt.next());
					}
					for (int j = 0; j < closingValueList.size(); j++) {
						Closing closing = new Closing();
						closing.setContent(closingValueList.get(j));
						if (closingLangList !=  null) {
							closing.setLang(closingLangList.get(j));
						}
	
						repository.getTimetable().getClosing().add(closing);
					}
				}
			}
		}

		// eag/archguide/desc/repositories/repository/access/question
		if (this.eag2012.getAccessQuestion() != null) {
			for (int i = 0; i < this.eag2012.getAccessQuestion().size(); i++) {
				Map<String, String> tabsQuestionMap = this.eag2012.getAccessQuestion().get(i);
				Repository repository = this.eag.getArchguide().getDesc().getRepositories().getRepository().get(i);
				if (repository.getAccess() == null) {
					repository.setAccess(new Access());
				}
				Iterator<String> questionValueIt = tabsQuestionMap.keySet().iterator();
				while (questionValueIt.hasNext()) {
					String key = questionValueIt.next();
					repository.getAccess().setQuestion(tabsQuestionMap.get(key));
				}
			}
		}

		// eag/archguide/desc/repositories/repository/access/restaccess
		if (this.eag2012.getRestaccessValue() != null) {
			for (int i = 0; i < this.eag2012.getRestaccessValue().size(); i++) {
				Map<String, List<String>> tabsValueMap = this.eag2012.getRestaccessValue().get(i);
				Map<String, List<String>> tabsLangMap = null;
				if (this.eag2012.getRestaccessLang() != null && !this.eag2012.getRestaccessLang().isEmpty()) {
					tabsLangMap = this.eag2012.getRestaccessLang().get(i);
				}
				// Repository.
				Repository repository = this.eag.getArchguide().getDesc().getRepositories().getRepository().get(i);

				Iterator<String> accessValueIt = tabsValueMap.keySet().iterator();
				Iterator<String> accessLangIt = null;
				if (tabsLangMap != null) {
					accessLangIt = tabsLangMap.keySet().iterator();
				}
				while (accessValueIt.hasNext()) {
					String accessValueKey = accessValueIt.next();

					// Rest of tabs.
					if (!accessValueKey.equalsIgnoreCase(CreateEAG2012.TAB_YOUR_INSTITUTION)) {
						List<String> accessValueList = tabsValueMap.get(accessValueKey);
						List<String> accessLangList = null;
						if (accessLangIt != null) {
							accessLangList = tabsLangMap.get(accessLangIt.next());
						}
						for (int j = 0; j < accessValueList.size(); j++) {
							Restaccess restaccess = new Restaccess();
							restaccess.setContent(accessValueList.get(j));
							if (accessLangList != null) {
								restaccess.setLang(accessLangList.get(j));
							}
							repository.getAccess().getRestaccess().add(restaccess);
						}
					}
				}
			}
		}

		// eag/archguide/desc/repositories/repository/access/termsOfUse
		if (this.eag2012.getTermsOfUseValue() != null && !this.eag2012.getTermsOfUseValue().isEmpty()
				&& this.eag2012.getTermsOfUseLang() != null && !this.eag2012.getTermsOfUseLang().isEmpty()
				&& this.eag2012.getTermsOfUseHref() != null && !this.eag2012.getTermsOfUseHref().isEmpty()) {
			for (int i = 0; i < this.eag2012.getTermsOfUseValue().size(); i++) {
				List<String> termsOfUseValueList = this.eag2012.getTermsOfUseValue().get(i);
				List<String> termsOfUseLangList = this.eag2012.getTermsOfUseLang().get(i);
				List<String> termsOfUseHrefList = this.eag2012.getTermsOfUseHref().get(i);

				// Repository.
				Repository repository = this.eag.getArchguide().getDesc().getRepositories().getRepository().get(i);

				for (int j = 0; j < termsOfUseValueList.size(); j++) {
					TermsOfUse termsOfUse = new TermsOfUse();
					termsOfUse.setContent(termsOfUseValueList.get(j));
					termsOfUse.setLang(termsOfUseLangList.get(j));
					termsOfUse.setHref(termsOfUseHrefList.get(j));

					repository.getAccess().getTermsOfUse().add(termsOfUse);
				}
			}
		}

		// eag/archguide/desc/repositories/repository/accessibility/question
		if (this.eag2012.getAccessibilityQuestion() != null) {
			for (int i = 0; i < this.eag2012.getAccessibilityQuestion().size(); i++) {
				Map<String, String> tabsQuestionMap = this.eag2012.getAccessibilityQuestion().get(i);
				Repository repository = this.eag.getArchguide().getDesc().getRepositories().getRepository().get(i);
				Iterator<String> questionValueIt = tabsQuestionMap.keySet().iterator();
				while (questionValueIt.hasNext()) {
					String key = questionValueIt.next();
					Accessibility accessibility = new Accessibility();
					accessibility.setQuestion(tabsQuestionMap.get(key));
					repository.getAccessibility().add(accessibility);
				}
			}
		}

		// eag/archguide/desc/repositories/repository/accessibility
		if (this.eag2012.getAccessibilityValue() != null /*&& this.eag2012.getAccessibilityLang() != null*/) {
			for (int i = 0; i < this.eag2012.getAccessibilityValue().size(); i++) {
				Map<String, List<String>> tabsValueMap = this.eag2012.getAccessibilityValue().get(i);
				Map<String, List<String>> tabsLangMap = null;
				if (this.eag2012.getAccessibilityLang() != null && !this.eag2012.getAccessibilityLang().isEmpty()) {
					tabsLangMap = this.eag2012.getAccessibilityLang().get(i);
				}
				Repository repository = this.eag.getArchguide().getDesc().getRepositories().getRepository().get(i);
				Iterator<String> accessibilityValueIt = tabsValueMap.keySet().iterator();
				Iterator<String> accessibilityLangIt = null;
				if (tabsLangMap != null) {
					accessibilityLangIt = tabsLangMap.keySet().iterator();
				}
				while (accessibilityValueIt.hasNext()) {
					String accessibilityValueKey = accessibilityValueIt.next();

					// Rest of tabs.
					if (!accessibilityValueKey.equalsIgnoreCase(CreateEAG2012.TAB_YOUR_INSTITUTION)) {
						List<String> accessibilityValueList = tabsValueMap.get(accessibilityValueKey);
						List<String> accessibilityLangList = null;
						if (accessibilityLangIt != null) {
							accessibilityLangList = tabsLangMap.get(accessibilityLangIt.next());
						}
						for (int j = 0; j < accessibilityValueList.size(); j++) {
							Accessibility accessibility = null;
							if (repository.getAccessibility().get(j) == null) {
								accessibility = new Accessibility();
							} else {
								accessibility = repository.getAccessibility().get(j);
							}
							accessibility.setContent(accessibilityValueList.get(j));
							if (accessibilityLangList != null) {
								accessibility.setLang(accessibilityLangList.get(j));
							}
						}
					}
				}
			}
		}

		// eag/archguide/desc/repositories/repository/services/searchroom
		getAllNums();

		// eag/archguide/desc/repositories/repository/services/searchroom/photographAllowance
		if (this.eag2012.getPhotographAllowanceValue() != null) {
			for (int i = 0; i < this.eag2012.getPhotographAllowanceValue().size(); i++) {
				String value = this.eag2012.getPhotographAllowanceValue().get(i);
				// Repository.
				Repository repository = this.eag.getArchguide().getDesc().getRepositories().getRepository().get(i);

				if (repository.getServices() == null) {
					repository.setServices(new Services());
				}
				if (repository.getServices().getSearchroom() == null) {
					repository.getServices().setSearchroom(new Searchroom());
				}
				PhotographAllowance photographAllowance = new PhotographAllowance();
				photographAllowance.setValue(value);

				repository.getServices().getSearchroom().setPhotographAllowance(photographAllowance);
			}
		}

		// eag/archguide/desc/repositories/repository/services/searchroom/readersTicket
		if (this.eag2012.getReadersTicketValue() != null && this.eag2012.getReadersTicketHref() != null
				&& this.eag2012.getReadersTicketLang() != null) {
			for (int i = 0; i < this.eag2012.getReadersTicketValue().size(); i++) {
				List<String> valueList = this.eag2012.getReadersTicketValue().get(i);
				List<String> hrefList = this.eag2012.getReadersTicketHref().get(i);
				List<String> langList = this.eag2012.getReadersTicketLang().get(i);
				// Repository.
				Repository repository = this.eag.getArchguide().getDesc().getRepositories().getRepository().get(i);

				if (repository.getServices() == null) {
					repository.setServices(new Services());
				}
				if (repository.getServices().getSearchroom() == null) {
					repository.getServices().setSearchroom(new Searchroom());
				}
				for (int j = 0; j < valueList.size(); j++) {
					ReadersTicket readersTicket = new ReadersTicket();
					readersTicket.setContent(valueList.get(j));
					readersTicket.setHref(hrefList.get(j));
					readersTicket.setLang(langList.get(j));

					repository.getServices().getSearchroom().getReadersTicket().add(readersTicket);
				}
			}
		}

		// eag/archguide/desc/repositories/repository/services/searchroom/advancedOrders
		if (this.eag2012.getAdvancedOrdersValue() != null && this.eag2012.getAdvancedOrdersHref() != null
				&& this.eag2012.getAdvancedOrdersLang() != null) {
			for (int i = 0; i < this.eag2012.getAdvancedOrdersValue().size(); i++) {
				List<String> valueList = this.eag2012.getAdvancedOrdersValue().get(i);
				List<String> hrefList = this.eag2012.getAdvancedOrdersHref().get(i);
				List<String> langList = this.eag2012.getAdvancedOrdersLang().get(i);
				// Repository.
				Repository repository = this.eag.getArchguide().getDesc().getRepositories().getRepository().get(i);

				if (repository.getServices() == null) {
					repository.setServices(new Services());
				}
				if (repository.getServices().getSearchroom() == null) {
					repository.getServices().setSearchroom(new Searchroom());
				}
				for (int j = 0; j < valueList.size(); j++) {
					AdvancedOrders advancedOrders = new AdvancedOrders();
					advancedOrders.setContent(valueList.get(j));
					advancedOrders.setHref(hrefList.get(j));
					advancedOrders.setLang(langList.get(j));

					repository.getServices().getSearchroom().getAdvancedOrders().add(advancedOrders);
				}
			}
		}

		// eag/archguide/desc/repositories/repository/services/library
		if (this.eag2012.getNumValue() != null) {
			for (int i = 0; i < this.eag2012.getNumValue().size(); i++) {
				Map<String, Map<String, Map<String, List<String>>>> tabsMap = this.eag2012.getNumValue().get(i);
				// Repository.
				Repository repository = this.eag.getArchguide().getDesc().getRepositories().getRepository().get(i);

				Iterator<String> tabsIt = tabsMap.keySet().iterator();
				while (tabsIt.hasNext()) {
					String tabKey = tabsIt.next();
					if (CreateEAG2012.TAB_ACCESS_AND_SERVICES.equalsIgnoreCase(tabKey)) {
						Map<String, Map<String, List<String>>> sectionsMap = tabsMap.get(tabKey);
						Iterator<String> sectionsIt = sectionsMap.keySet().iterator();
						while (sectionsIt.hasNext()) {
							String sectionKey = sectionsIt.next();
							if (CreateEAG2012.LIBRARY.equalsIgnoreCase(sectionKey)) {
								Map<String, List<String>> subSectionsMap = sectionsMap.get(sectionKey);
								Iterator<String> subSectionsIt = subSectionsMap.keySet().iterator();
								while (subSectionsIt.hasNext()) {
									String subSectionKey = subSectionsIt.next();
									// eag/archguide/desc/repositories/repository/services/library/monographicpub
									if (CreateEAG2012.MONOGRAPHIC_PUBLICATION.equalsIgnoreCase(subSectionKey)) {
										List<String> valueList = subSectionsMap.get(subSectionKey);
										for (int j = 0; j < valueList.size(); j++) {
											if (repository.getServices() == null) {
												repository.setServices(new Services());
											}
											if (repository.getServices().getLibrary() == null) {
												repository.getServices().setLibrary(new Library());
											}

											Num num = new Num();
											num.setContent(valueList.get(j));
											num.setUnit(CreateEAG2012.UNIT_BOOK);

											Monographicpub monographicpub = new Monographicpub();
											monographicpub.setNum(num);

											repository.getServices().getLibrary().setMonographicpub(monographicpub);
										}
									}
									// eag/archguide/desc/repositories/repository/services/library/serialpub
									if (CreateEAG2012.SERIAL_PUBLICATION.equalsIgnoreCase(subSectionKey)) {
										List<String> valueList = subSectionsMap.get(subSectionKey);
										for (int j = 0; j < valueList.size(); j++) {
											if (repository.getServices() == null) {
												repository.setServices(new Services());
											}
											if (repository.getServices().getLibrary() == null) {
												repository.getServices().setLibrary(new Library());
											}

											Num num = new Num();
											num.setContent(valueList.get(j));
											num.setUnit(CreateEAG2012.UNIT_TITLE);

											Serialpub serialpub = new Serialpub();
											serialpub.setNum(num);

											repository.getServices().getLibrary().setSerialpub(serialpub);
										}
									}
								}
							}
						}
					}
				}
			}
		}

		// eag/archguide/desc/repositories/repository/services/library/question
		if (this.eag2012.getLibraryQuestion() != null) {
			for (int i = 0; i < this.eag2012.getLibraryQuestion().size(); i++) {
				// Repository
				Repository repository = this.eag.getArchguide().getDesc().getRepositories().getRepository().get(i);
				if (repository.getServices() == null) {
					repository.setServices(new Services());
				}
				if (repository.getServices().getLibrary() == null) {
					repository.getServices().setLibrary(new Library());
				}

				repository.getServices().getLibrary().setQuestion(this.eag2012.getLibraryQuestion().get(i));
			}
		}

		// eag/archguide/desc/repositories/repository/services/internetAccess
		if (this.eag2012.getInternetAccessQuestion() != null) {
			for (int i = 0; i < this.eag2012.getInternetAccessQuestion().size(); i++) {
				// Repository
				Repository repository = this.eag.getArchguide().getDesc().getRepositories().getRepository().get(i);
				if (repository.getServices() == null) {
					repository.setServices(new Services());
				}
				if (repository.getServices().getInternetAccess() == null) {
					repository.getServices().setInternetAccess(new InternetAccess());
				}

				repository.getServices().getInternetAccess().setQuestion(this.eag2012.getInternetAccessQuestion().get(i));
				getDescriptiveNote(repository.getServices().getInternetAccess());
			}
		}

		// eag/archguide/desc/repositories/repository/services/techservices/restorationlab
		if (this.eag2012.getRestorationlabQuestion() != null) {
			for (int i = 0; i < this.eag2012.getRestorationlabQuestion().size(); i++) {
				// Repository
				Repository repository = this.eag.getArchguide().getDesc().getRepositories().getRepository().get(i);
				if (repository.getServices() == null) {
					repository.setServices(new Services());
				}
				if (repository.getServices().getTechservices() == null) {
					repository.getServices().setTechservices(new Techservices());
				}
				if (repository.getServices().getTechservices().getRestorationlab() == null) {
					repository.getServices().getTechservices().setRestorationlab(new Restorationlab());
				}

				repository.getServices().getTechservices().getRestorationlab().setQuestion(this.eag2012.getRestorationlabQuestion().get(i));
				getDescriptiveNote(repository.getServices().getTechservices().getRestorationlab());
			}
		}

		// eag/archguide/desc/repositories/repository/services/techservices/reproductionser
		if (this.eag2012.getReproductionserQuestion() != null) {
			for (int i = 0; i < this.eag2012.getReproductionserQuestion().size(); i++) {
				// Repository
				Repository repository = this.eag.getArchguide().getDesc().getRepositories().getRepository().get(i);
				if (repository.getServices() == null) {
					repository.setServices(new Services());
				}
				if (repository.getServices().getTechservices() == null) {
					repository.getServices().setTechservices(new Techservices());
				}
				if (repository.getServices().getTechservices().getReproductionser() == null) {
					repository.getServices().getTechservices().setReproductionser(new Reproductionser());
				}

				// eag/archguide/desc/repositories/repository/services/techservices/reproductionser/question
				repository.getServices().getTechservices().getReproductionser().setQuestion(this.eag2012.getReproductionserQuestion().get(i));

				// eag/archguide/desc/repositories/repository/services/techservices/reproductionser/descriptiveNote
				getDescriptiveNote(repository.getServices().getTechservices().getReproductionser());

				// eag/archguide/desc/repositories/repository/services/techservices/reproductionser/microformser
				if (this.eag2012.getMicroformserQuestion() != null && !this.eag2012.getMicroformserQuestion().isEmpty()) {
					if (repository.getServices().getTechservices().getReproductionser().getMicroformser() == null) {
						repository.getServices().getTechservices().getReproductionser().setMicroformser(new Microformser());
					}
					repository.getServices().getTechservices().getReproductionser().getMicroformser().setQuestion(this.eag2012.getMicroformserQuestion().get(i));
				}

				// eag/archguide/desc/repositories/repository/services/techservices/reproductionser/photographser
				if (this.eag2012.getPhotographserQuestion() != null && !this.eag2012.getPhotographserQuestion().isEmpty()) {
					if (repository.getServices().getTechservices().getReproductionser().getPhotographser() == null) {
						repository.getServices().getTechservices().getReproductionser().setPhotographser(new Photographser());
					}
					repository.getServices().getTechservices().getReproductionser().getPhotographser().setQuestion(this.eag2012.getPhotographserQuestion().get(i));
				}

				// eag/archguide/desc/repositories/repository/services/techservices/reproductionser/digitalser
				if (this.eag2012.getDigitalserQuestion() != null && !this.eag2012.getDigitalserQuestion().isEmpty()) {
					if (repository.getServices().getTechservices().getReproductionser().getDigitalser() == null) {
						repository.getServices().getTechservices().getReproductionser().setDigitalser(new Digitalser());
					}
					repository.getServices().getTechservices().getReproductionser().getDigitalser().setQuestion(this.eag2012.getDigitalserQuestion().get(i));
				}

				// eag/archguide/desc/repositories/repository/services/techservices/reproductionser/photocopyser
				if (this.eag2012.getPhotocopyserQuestion() != null && !this.eag2012.getPhotocopyserQuestion().isEmpty()) {
					if (repository.getServices().getTechservices().getReproductionser().getPhotocopyser() == null) {
						repository.getServices().getTechservices().getReproductionser().setPhotocopyser(new Photocopyser());
					}
					repository.getServices().getTechservices().getReproductionser().getPhotocopyser().setQuestion(this.eag2012.getPhotocopyserQuestion().get(i));
				}
			}
		}

		// eag/archguide/desc/repositories/repository/descriptivenote

	}

	/**
	 * Method to recover dates.
	 */
	private void getAllDates() {
//		if (this.eag2012.getDateStandardDate() != null) {
//			// TODO: Structure under revision.
//			for (int i = 0; i < this.eag2012.getDateStandardDate().size(); i++) {
//				// Repository.
//				Repository repository = this.eag.getArchguide().getDesc().getRepositories().getRepository().get(i);
//
//				Map<String, Map<String, Map<String, List<String>>>> tabsValueMap = this.eag2012.getDateStandardDate().get(i);
//				Iterator<String> tabsValueIt = tabsValueMap.keySet().iterator();
//				while (tabsValueIt.hasNext()) {
//					String tabValueKey = tabsValueIt.next();
//					Map<String, Map<String, List<String>>> sectionsValueMap = tabsValueMap.get(tabValueKey);
//					Iterator<String> sectionsValueIt = sectionsValueMap.keySet().iterator();
//					if (CreateEAG2012.TAB_IDENTITY.equalsIgnoreCase(tabValueKey)) {
//						while (sectionsValueIt.hasNext()) {
//							String sectionValueKey = sectionsValueIt.next();
//							Map<String, List<String>> subsectionsValueMap = sectionsValueMap.get(sectionValueKey);
//							Iterator<String> subsectionsValueIt = subsectionsValueMap.keySet().iterator();
//							if (CreateEAG2012.ROOT.equalsIgnoreCase(sectionValueKey)) {
//								while (subsectionsValueIt.hasNext()) {
//									String subsectionValueKey = subsectionsValueIt.next();
//									List<String> valuesList = subsectionsValueMap.get(subsectionValueKey);
//									for (int j = 0; j < valuesList.size(); j++) {
//										Date date = new Date();
//										date.setStandardDate(valuesList.get(j));
//										date.setContent(valuesList.get(j));
//										
//										if (CreateEAG2012.ROOT_SUBSECTION.equalsIgnoreCase(subsectionValueKey)) {
////											Nonpreform nonpreform = this.eag.getArchguide().getIdentity().getNonpreform();
//// TODO: Structure under revision.
//										}
//									}
//								}
//							}
//						}
//					}
//					if (CreateEAG2012.TAB_DESCRIPTION.equalsIgnoreCase(tabValueKey)) {
//						while (sectionsValueIt.hasNext()) {
//							String sectionValueKey = sectionsValueIt.next();
//							Map<String, List<String>> subsectionsValueMap = sectionsValueMap.get(sectionValueKey);
//							Iterator<String> subsectionsValueIt = subsectionsValueMap.keySet().iterator();
//							if (CreateEAG2012.REPOSITORHIST.equalsIgnoreCase(sectionValueKey)) {
//								while (subsectionsValueIt.hasNext()) {
//									String subsectionValueKey = subsectionsValueIt.next();
//									List<String> valuesList = subsectionsValueMap.get(subsectionValueKey);
//									for (int j = 0; j < valuesList.size(); j++) {
//										Date date = new Date();
//										date.setStandardDate(valuesList.get(j));
//										date.setContent(valuesList.get(j));
//
//										if (CreateEAG2012.REPOSITOR_FOUND.equalsIgnoreCase(subsectionValueKey)) {
//											// eag/archguide/desc/repositories/repository/repositorfuond/date
//											if (repository.getRepositorfound() == null) {
//												repository.setRepositorfound(new Repositorfound());
//											}
//											repository.getRepositorfound().setDate(date);
//										} else if (CreateEAG2012.REPOSITOR_SUP.equalsIgnoreCase(subsectionValueKey)) {
//											// eag/archguide/desc/repositories/repository/repositorsup/date
//											if (repository.getRepositorsup() == null) {
//												repository.setRepositorsup(new Repositorsup());
//											}
//											repository.getRepositorsup().setDate(date);
//										}
//									}
//								}
//							} else if (CreateEAG2012.HOLDINGS.equalsIgnoreCase(sectionValueKey)) {
//								while (subsectionsValueIt.hasNext()) {
//									String subsectionValueKey = subsectionsValueIt.next();
//									List<String> valuesList = subsectionsValueMap.get(subsectionValueKey);
//									for (int j = 0; j < valuesList.size(); j++) {
//										Date date = new Date();
//										date.setStandardDate(valuesList.get(j));
//										date.setContent(valuesList.get(j));
//
//										if (CreateEAG2012.HOLDING_SUBSECTION.equalsIgnoreCase(subsectionValueKey)) {
//											if (repository.getHoldings() == null) {
//												repository.setHoldings(new Holdings());
//											}
//											if (repository.getHoldings().getDateSet() == null) {
//												repository.getHoldings().setDateSet(new DateSet());
//											}
//											repository.getHoldings().getDateSet().getDateOrDateRange().add(date);
//										}
//									}
//								}
//							}
//						}
//					}
//				}
//			}
//		}
//
//		if (this.eag2012.getFromDateStandardDate() != null
//				&& this.eag2012.getToDateStandardDate() != null) {
//			// TODO: Structure under revision.
//			for (int i = 0; i < this.eag2012.getDateStandardDate().size(); i++) {
//				// Repository.
//				Repository repository = this.eag.getArchguide().getDesc().getRepositories().getRepository().get(i);
//
//				Map<String, Map<String, Map<String, List<String>>>> tabsValueFromMap = this.eag2012.getFromDateStandardDate().get(i);
//				Map<String, Map<String, Map<String, List<String>>>> tabsValueToMap = this.eag2012.getToDateStandardDate().get(i);
//				Iterator<String> tabsValueFromIt = tabsValueFromMap.keySet().iterator();
//				Iterator<String> tabsValueToIt = tabsValueToMap.keySet().iterator();
//				while (tabsValueFromIt.hasNext()) {
//					String tabValueFromKey = tabsValueFromIt.next();
//					String tabValueToKey = tabsValueToIt.next();
//					Map<String, Map<String, List<String>>> sectionsValueFromMap = tabsValueFromMap.get(tabValueFromKey);
//					Map<String, Map<String, List<String>>> sectionsValueToMap = tabsValueToMap.get(tabValueToKey);
//					Iterator<String> sectionsValueFromIt = sectionsValueFromMap.keySet().iterator();
//					Iterator<String> sectionsValueToIt = sectionsValueToMap.keySet().iterator();
//					if (CreateEAG2012.TAB_DESCRIPTION.equalsIgnoreCase(tabValueFromKey)
//							&& CreateEAG2012.TAB_DESCRIPTION.equalsIgnoreCase(tabValueToKey)) {
//						while (sectionsValueFromIt.hasNext()) {
//							String sectionValueFromKey = sectionsValueFromIt.next();
//							String sectionValueToKey = sectionsValueToIt.next();
//							Map<String, List<String>> subsectionsValueFromMap = sectionsValueFromMap.get(sectionValueFromKey);
//							Map<String, List<String>> subsectionsValueToMap = sectionsValueToMap.get(sectionValueToKey);
//							Iterator<String> subsectionsValueFromIt = subsectionsValueFromMap.keySet().iterator();
//							Iterator<String> subsectionsValueToIt = subsectionsValueToMap.keySet().iterator();
//							while (subsectionsValueFromIt.hasNext()) {
//							if (CreateEAG2012.HOLDINGS.equalsIgnoreCase(sectionValueFromKey)
//									&& CreateEAG2012.HOLDINGS.equalsIgnoreCase(sectionValueToKey)) {
//									String subsectionValueFromKey = subsectionsValueFromIt.next();
//									String subsectionValueToKey = subsectionsValueToIt.next();
//									List<String> valuesFromList = subsectionsValueFromMap.get(subsectionValueFromKey);
//									List<String> valuesToList = subsectionsValueToMap.get(subsectionValueToKey);
//									for (int j = 0; j < valuesFromList.size(); j++) {
//										FromDate fromDate = new FromDate();
//										fromDate.setStandardDate(valuesFromList.get(j));
//										fromDate.setContent(valuesFromList.get(j));
//
//										ToDate toDate = new ToDate();
//										toDate.setStandardDate(valuesToList.get(j));
//										toDate.setContent(valuesToList.get(j));
//										
//
//										DateRange dateRange = new DateRange();
//										dateRange.setFromDate(fromDate);
//										dateRange.setToDate(toDate);
//
//										if (CreateEAG2012.HOLDING_SUBSECTION.equalsIgnoreCase(subsectionValueFromKey)
//												&& CreateEAG2012.HOLDING_SUBSECTION.equalsIgnoreCase(subsectionValueToKey)) {
//											if (repository.getHoldings() == null) {
//												repository.setHoldings(new Holdings());
//											}
//											if (repository.getHoldings().getDateSet() == null) {
//												repository.getHoldings().setDateSet(new DateSet());
//											}
//											repository.getHoldings().getDateSet().getDateOrDateRange().add(dateRange);
//										}
//									}
//								}
//							}
//						}
//					}
//				}
//			}
//		}
	}

	/**
	 * Method to recover descriptiveNote for research services.
	 */
	private void getAllDescriptiveNote() {
		if (this.eag.getArchguide().getDesc().getRepositories().getRepository() != null
				&& !this.eag.getArchguide().getDesc().getRepositories().getRepository().isEmpty()) {
			for (int i = 0; i < this.eag.getArchguide().getDesc().getRepositories().getRepository().size(); i++) {
				// Repository.
				Repository repository = this.eag.getArchguide().getDesc().getRepositories().getRepository().get(i);

				// Recover descriptiveNote for research services.
				if (this.eag2012.getDescriptiveNotePValue() != null
						&& this.eag2012.getDescriptiveNotePLang() != null) {
					for (int j = 0; j < this.eag2012.getDescriptiveNotePValue().size(); j++) {
						Map<String, Map<String, List<String>>> tabsValueMap = this.eag2012.getDescriptiveNotePValue().get(j);
						Map<String, Map<String, List<String>>> tabsLangMap = this.eag2012.getDescriptiveNotePLang().get(j);
						Iterator<String> tabsValueIt = tabsValueMap.keySet().iterator();
						Iterator<String> tabsLangIt = tabsLangMap.keySet().iterator();
						while (tabsValueIt.hasNext()) {
							String tabValueKey = tabsValueIt.next();
							String tabLangKey = tabsLangIt.next();

							if (CreateEAG2012.TAB_ACCESS_AND_SERVICES.equalsIgnoreCase(tabValueKey)
									&& CreateEAG2012.TAB_ACCESS_AND_SERVICES.equalsIgnoreCase(tabLangKey)) {
								Map<String, List<String>> sectionsValueMap = tabsValueMap.get(tabValueKey);
								Map<String, List<String>> sectionsLangMap = tabsLangMap.get(tabLangKey);
								Iterator<String> sectionsValueIt = sectionsValueMap.keySet().iterator();
								Iterator<String> sectionsLangIt = sectionsLangMap.keySet().iterator();
								while (sectionsValueIt.hasNext()) {
									String sectionValueKey = sectionsValueIt.next();
									String sectionLangKey = sectionsLangIt.next();
									List<String> valuesList = sectionsValueMap.get(sectionValueKey);
									List<String> langList = sectionsLangMap.get(sectionLangKey);
									for (int k = 0; k < valuesList.size(); k++) {
										P p = new P();
										p.setContent(valuesList.get(k));
										p.setLang(langList.get(k));

										// eag/archguide/desc/repositories/repository/services/searchroom/researchServices/descriptiveNote/P
										if (CreateEAG2012.RESEARCH_SERVICES.equalsIgnoreCase(sectionValueKey)
												&& CreateEAG2012.RESEARCH_SERVICES.equalsIgnoreCase(sectionLangKey)) {
											if (repository.getServices() == null) {
												repository.setServices(new Services());
											}
											if (repository.getServices().getSearchroom() == null) {
												repository.getServices().setSearchroom(new Searchroom());
											}

											ResearchServices researchServices = new ResearchServices();
											if (researchServices.getDescriptiveNote() == null) {
												researchServices.setDescriptiveNote(new DescriptiveNote());
											}

											researchServices.getDescriptiveNote().getP().add(p);
											repository.getServices().getSearchroom().getResearchServices().add(researchServices);
										} else  if (CreateEAG2012.REFRESHMENT.equalsIgnoreCase(sectionValueKey)
												&& CreateEAG2012.REFRESHMENT.equalsIgnoreCase(sectionLangKey)) {
											// eag/archguide/desc/repositories/repository/services/recreationalSerices/refreshment/descriptiveNote/P
											if (repository.getServices() == null) {
												repository.setServices(new Services());
											}
											if (repository.getServices().getRecreationalServices() == null) {
												repository.getServices().setRecreationalServices(new RecreationalServices());
											}

											Refreshment refreshment = new Refreshment();
											if (refreshment.getDescriptiveNote() == null) {
												refreshment.setDescriptiveNote(new DescriptiveNote());
											}

											refreshment.getDescriptiveNote().getP().add(p);

											repository.getServices().getRecreationalServices().setRefreshment(refreshment);
										}
									}
								}
							} else  if (CreateEAG2012.TAB_DESCRIPTION.equalsIgnoreCase(tabValueKey)
									&& CreateEAG2012.TAB_DESCRIPTION.equalsIgnoreCase(tabLangKey)) {
								Map<String, List<String>> sectionsValueMap = tabsValueMap.get(tabValueKey);
								Map<String, List<String>> sectionsLangMap = tabsLangMap.get(tabLangKey);
								Iterator<String> sectionsValueIt = sectionsValueMap.keySet().iterator();
								Iterator<String> sectionsLangIt = sectionsLangMap.keySet().iterator();
								while (sectionsValueIt.hasNext()) {
									String sectionValueKey = sectionsValueIt.next();
									String sectionLangKey = sectionsLangIt.next();
									List<String> valuesList = sectionsValueMap.get(sectionValueKey);
									List<String> langList = sectionsLangMap.get(sectionLangKey);
									for (int k = 0; k < valuesList.size(); k++) {
										P p = new P();
										p.setContent(valuesList.get(k));
										p.setLang(langList.get(k));

										// eag/archguide/desc/repositories/repository/repositorhist/descriptiveNote/P
										if (CreateEAG2012.REPOSITORHIST.equalsIgnoreCase(sectionValueKey)
												&& CreateEAG2012.REPOSITORHIST.equalsIgnoreCase(sectionLangKey)) {
											Repositorhist repositorhist = null;
											if (repository.getRepositorhist() == null) {
												repositorhist = new Repositorhist();
											} else {
												repositorhist = repository.getRepositorhist();
											}
											if (repositorhist.getDescriptiveNote() == null) {
												repositorhist.setDescriptiveNote(new DescriptiveNote());
											}

											repositorhist.getDescriptiveNote().getP().add(p);

											repository.setRepositorhist(repositorhist);
										} else if (CreateEAG2012.BUILDING.equalsIgnoreCase(sectionValueKey)
												&& CreateEAG2012.BUILDING.equalsIgnoreCase(sectionLangKey)) {
											// eag/archguide/desc/repositories/repository/buildinginfo/building/descriptiveNote/P
											Building building = null; 
											if (repository.getBuildinginfo() == null) {
												repository.setBuildinginfo(new Buildinginfo());
												building = new Building();
											} else {
												building = repository.getBuildinginfo().getBuilding();
											}

											if (building.getDescriptiveNote() == null) {
												building.setDescriptiveNote(new DescriptiveNote());
											}

											building.getDescriptiveNote().getP().add(p);

											repository.getBuildinginfo().setBuilding(building);
										} else if (CreateEAG2012.HOLDINGS.equalsIgnoreCase(sectionValueKey)
												&& CreateEAG2012.HOLDINGS.equalsIgnoreCase(sectionLangKey)) {
											// eag/archguide/desc/repositories/repository/holdings/descriptiveNote/P
											Holdings holdings = null;
											if (repository.getHoldings() == null) {
												holdings = new Holdings();
											} else {
												holdings = repository.getHoldings();
											}

											if (holdings.getDescriptiveNote() == null) {
												holdings.setDescriptiveNote(new DescriptiveNote());
											}

											holdings.getDescriptiveNote().getP().add(p);

											repository.setHoldings(holdings);
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}

	/**
	 * Method to recover all num elements.
	 */
	private void getAllNums() {
		if (this.eag2012.getNumValue() != null) {
			for (int i = 0; i < this.eag2012.getNumValue().size(); i++) {
				Map<String, Map<String, Map<String, List<String>>>> tabsMap = this.eag2012.getNumValue().get(i);
				// Repository.
				Repository repository = this.eag.getArchguide().getDesc().getRepositories().getRepository().get(i);

				Iterator<String> tabsIt = tabsMap.keySet().iterator();
				while (tabsIt.hasNext()) {
					String tabKey = tabsIt.next();
					if (CreateEAG2012.TAB_ACCESS_AND_SERVICES.equalsIgnoreCase(tabKey)) {
						Map<String, Map<String, List<String>>> sectionsMap = tabsMap.get(tabKey);
						Iterator<String> sectionsIt = sectionsMap.keySet().iterator();
						while (sectionsIt.hasNext()) {
							String sectionKey = sectionsIt.next();
							if (CreateEAG2012.SEARCHROOM.equalsIgnoreCase(sectionKey)) {
								Map<String, List<String>> subSectionsMap = sectionsMap.get(sectionKey);
								Iterator<String> subSectionsIt = subSectionsMap.keySet().iterator();
								while (subSectionsIt.hasNext()) {
									String subSectionKey = subSectionsIt.next();
									// eag/archguide/desc/repositories/repository/services/searchroom/workPlaces
									if (CreateEAG2012.WORKING_PLACES.equalsIgnoreCase(subSectionKey)) {
										List<String> valueList = subSectionsMap.get(subSectionKey);
										for (int j = 0; j < valueList.size(); j++) {
											if (repository.getServices() == null) {
												repository.setServices(new Services());
											}
											if (repository.getServices().getSearchroom() == null) {
												repository.getServices().setSearchroom(new Searchroom());
											}

											Num num = new Num();
											num.setContent(valueList.get(j));
											num.setUnit(CreateEAG2012.UNIT_SITE);

											WorkPlaces workPlaces = new WorkPlaces();
											workPlaces.setNum(num);

											repository.getServices().getSearchroom().setWorkPlaces(workPlaces);
										}
									} else if (CreateEAG2012.COMPUTER_PLACES.equalsIgnoreCase(subSectionKey)) {
										// eag/archguide/desc/repositories/repository/services/searchroom/ComputerPlaces
										List<String> valueList = subSectionsMap.get(subSectionKey);
										for (int j = 0; j < valueList.size(); j++) {
											if (repository.getServices() == null) {
												repository.setServices(new Services());
											}
											if (repository.getServices().getSearchroom() == null) {
												repository.getServices().setSearchroom(new Searchroom());
											}

											Num num = new Num();
											num.setContent(valueList.get(j));
											num.setUnit(CreateEAG2012.UNIT_SITE);

											ComputerPlaces computerPlaces = new ComputerPlaces();
											computerPlaces.setNum(num);

											// eag/archguide/desc/repositories/repository/services/searchroom/ComputerPlaces/descriptiveNote
											getDescriptiveNote(computerPlaces);

											repository.getServices().getSearchroom().setComputerPlaces(computerPlaces);
										}
									} else if (CreateEAG2012.MICROFILM.equalsIgnoreCase(subSectionKey)) {
										// eag/archguide/desc/repositories/repository/services/searchroom/microfilmPlaces
										List<String> valueList = subSectionsMap.get(subSectionKey);
										for (int j = 0; j < valueList.size(); j++) {
											if (repository.getServices() == null) {
												repository.setServices(new Services());
											}
											if (repository.getServices().getSearchroom() == null) {
												repository.getServices().setSearchroom(new Searchroom());
											}

											Num num = new Num();
											num.setContent(valueList.get(j));
											num.setUnit(CreateEAG2012.UNIT_SITE);

											MicrofilmPlaces microfilmPlaces = new MicrofilmPlaces();
											microfilmPlaces.setNum(num);

											repository.getServices().getSearchroom().setMicrofilmPlaces(microfilmPlaces);
										}
									}
								}
							}
						}
					} else if (CreateEAG2012.TAB_DESCRIPTION.equalsIgnoreCase(tabKey)) {
						Map<String, Map<String, List<String>>> sectionsMap = tabsMap.get(tabKey);
						Iterator<String> sectionsIt = sectionsMap.keySet().iterator();
						while (sectionsIt.hasNext()) {
							String sectionKey = sectionsIt.next();
							if (CreateEAG2012.BUILDING.equalsIgnoreCase(sectionKey)) {
								Map<String, List<String>> subSectionsMap = sectionsMap.get(sectionKey);
								Iterator<String> subSectionsIt = subSectionsMap.keySet().iterator();
								while (subSectionsIt.hasNext()) {
									String subSectionKey = subSectionsIt.next();
									// eag/archguide/desc/repositories/repository/buildinginfo/repositorarea
									if (CreateEAG2012.BUILDING_AREA.equalsIgnoreCase(subSectionKey)) {
										List<String> valueList = subSectionsMap.get(subSectionKey);
										for (int j = 0; j < valueList.size(); j++) {
											if (repository.getBuildinginfo() == null) {
												repository.setBuildinginfo(new Buildinginfo());
											}

											Num num = new Num();
											num.setContent(valueList.get(j));
											num.setUnit(CreateEAG2012.UNIT_SQUARE_METRE);

											Repositorarea repositorarea = new Repositorarea();
											repositorarea.setNum(num);

											repository.getBuildinginfo().setRepositorarea(repositorarea);
										}
									} else if (CreateEAG2012.BUILDING_LENGTH.equalsIgnoreCase(subSectionKey)) {
										// eag/archguide/desc/repositories/repository/buildinginfo/lengthshelf
										List<String> valueList = subSectionsMap.get(subSectionKey);
										for (int j = 0; j < valueList.size(); j++) {
											if (repository.getBuildinginfo() == null) {
												repository.setBuildinginfo(new Buildinginfo());
											}

											Num num = new Num();
											num.setContent(valueList.get(j));
											num.setUnit(CreateEAG2012.UNIT_LINEAR_METRE);

											Lengthshelf lengthshelf = new Lengthshelf();
											lengthshelf.setNum(num);

											repository.getBuildinginfo().setLengthshelf(lengthshelf);
										}
									}
								}
							} else if (CreateEAG2012.HOLDINGS.equalsIgnoreCase(sectionKey)) {
								Map<String, List<String>> subSectionsMap = sectionsMap.get(sectionKey);
								Iterator<String> subSectionsIt = subSectionsMap.keySet().iterator();
								while (subSectionsIt.hasNext()) {
									String subSectionKey = subSectionsIt.next();
									if (CreateEAG2012.HOLDING_EXTENT.equalsIgnoreCase(subSectionKey)) {
										// eag/archguide/desc/repositories/repository/holdings/extent
										List<String> valueList = subSectionsMap.get(subSectionKey);
										for (int j = 0; j < valueList.size(); j++) {
											if (repository.getHoldings() == null) {
												repository.setHoldings(new Holdings());
											}
		
											Num num = new Num();
											num.setContent(valueList.get(j));
											num.setUnit(CreateEAG2012.UNIT_LINEAR_METRE);
		
											Extent extent = new Extent();
											extent.setNum(num);
		
											repository.getHoldings().setExtent(extent);
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}

	/**
	 * Method to recover all rule elements.
	 */
	private void getAllRules() {
		if (this.eag2012.getRuleValue() != null
				&& this.eag2012.getRuleLang() != null) {
			for (int i = 0; i < this.eag2012.getNumValue().size(); i++) {
				// Repository.
				Repository repository = this.eag.getArchguide().getDesc().getRepositories().getRepository().get(i);

				Map<String, List<String>> subsectionValueMap = this.eag2012.getRuleValue().get(i);
				Map<String, List<String>> subsectionLangMap = this.eag2012.getRuleLang().get(i);

				Iterator<String> subsectionValueIt = subsectionValueMap.keySet().iterator();
				Iterator<String> subsectionLangIt = subsectionLangMap.keySet().iterator();
				while (subsectionValueIt.hasNext()) {
					String subsectionValueKey = subsectionValueIt.next();
					String subsectionLangKey = subsectionLangIt.next();
					List<String> valuesList = subsectionValueMap.get(subsectionValueKey);
					List<String> langsList = subsectionLangMap.get(subsectionLangKey);
					for (int j = 0; j < valuesList.size(); j++) {
						// eag/archguide/desc/repositories/repository/repositorfound/rule
						if (CreateEAG2012.REPOSITOR_FOUND.equalsIgnoreCase(subsectionValueKey)
								&& CreateEAG2012.REPOSITOR_FOUND.equalsIgnoreCase(subsectionLangKey)) {
							if (repository.getRepositorfound() == null) {
								repository.setRepositorfound(new Repositorfound());
							}

							Rule rule = new Rule();
							rule.setContent(valuesList.get(j));
							rule.setLang(langsList.get(j));

							repository.getRepositorfound().getRule().add(rule);
						}
						// eag/archguide/desc/repositories/repository/repositorsup/rule
						if (CreateEAG2012.REPOSITOR_SUP.equalsIgnoreCase(subsectionValueKey)
								&& CreateEAG2012.REPOSITOR_SUP.equalsIgnoreCase(subsectionLangKey)) {
							if (repository.getRepositorsup() == null) {
								repository.setRepositorsup(new Repositorsup());
							}

							Rule rule = new Rule();
							rule.setContent(valuesList.get(j));
							rule.setLang(langsList.get(j));

							repository.getRepositorsup().getRule().add(rule);
						}
					}
				}
			}
		}
	}

	/**
	 * Method to recover descriptiveNote.
	 *
	 * @param object The object to add the descriptive note.
	 */
	private void getDescriptiveNote(final Object object) {
		if (this.eag2012.getDescriptiveNotePValue() != null
				&& this.eag2012.getDescriptiveNotePLang() != null) {
			for (int i = 0; i < this.eag2012.getDescriptiveNotePValue().size(); i++) {
				Map<String, Map<String, List<String>>> tabsValueMap = this.eag2012.getDescriptiveNotePValue().get(i);
				Map<String, Map<String, List<String>>> tabsLangMap = this.eag2012.getDescriptiveNotePLang().get(i);
				Iterator<String> tabsValueIt = tabsValueMap.keySet().iterator();
				Iterator<String> tabsLangIt = tabsLangMap.keySet().iterator();
				while (tabsValueIt.hasNext()) {
					String tabValueKey = tabsValueIt.next();
					String tabLangKey = tabsLangIt.next();

					if (CreateEAG2012.TAB_ACCESS_AND_SERVICES.equalsIgnoreCase(tabValueKey)
							&& CreateEAG2012.TAB_ACCESS_AND_SERVICES.equalsIgnoreCase(tabLangKey)) {
						Map<String, List<String>> sectionsValueMap = tabsValueMap.get(tabValueKey);
						Map<String, List<String>> sectionsLangMap = tabsLangMap.get(tabLangKey);
						Iterator<String> sectionsValueIt = sectionsValueMap.keySet().iterator();
						Iterator<String> sectionsLangIt = sectionsLangMap.keySet().iterator();
						while (sectionsValueIt.hasNext()) {
							String sectionValueKey = sectionsValueIt.next();
							String sectionLangKey = sectionsLangIt.next();
							List<String> valuesList = sectionsValueMap.get(sectionValueKey);
							List<String> langList = sectionsLangMap.get(sectionLangKey);
							for (int j = 0; j < valuesList.size(); j++) {
								P p = new P();
								p.setContent(valuesList.get(j));
								p.setLang(langList.get(j));

								// eag/archguide/desc/repositories/repository/services/searchroom/computerPlaces/descriptiveNote/P
								if (CreateEAG2012.SEARCHROOM.equalsIgnoreCase(sectionValueKey)
										&& CreateEAG2012.SEARCHROOM.equalsIgnoreCase(sectionLangKey)) {
									if (object instanceof ComputerPlaces) {
										ComputerPlaces computerPlaces = (ComputerPlaces) object;
										if (computerPlaces.getDescriptiveNote() == null) {
											computerPlaces.setDescriptiveNote(new DescriptiveNote());
										}
										computerPlaces.getDescriptiveNote().getP().add(p);
									}
								}
								// eag/archguide/desc/repositories/repository/services/internetAccess/descriptiveNote/P
								if (CreateEAG2012.INTERNET_ACCESS.equalsIgnoreCase(sectionValueKey)
										&& CreateEAG2012.INTERNET_ACCESS.equalsIgnoreCase(sectionLangKey)) {
									if (object instanceof InternetAccess) {
										InternetAccess internetAccess = (InternetAccess) object;
										if (internetAccess.getDescriptiveNote() == null) {
											internetAccess.setDescriptiveNote(new DescriptiveNote());
										}
										internetAccess.getDescriptiveNote().getP().add(p);
									}
								}
								// eag/archguide/desc/repositories/repository/services/techservices/restorationlab/descriptiveNote/P
								if (CreateEAG2012.RESTORATION_LAB.equalsIgnoreCase(sectionValueKey)
										&& CreateEAG2012.RESTORATION_LAB.equalsIgnoreCase(sectionLangKey)) {
									if (object instanceof Restorationlab) {
										Restorationlab restorationlab = (Restorationlab) object;
										if (restorationlab.getDescriptiveNote() == null) {
											restorationlab.setDescriptiveNote(new DescriptiveNote());
										}
										restorationlab.getDescriptiveNote().getP().add(p);
									}
								}
								// eag/archguide/desc/repositories/repository/services/techservices/reproductionser/descriptiveNote/P
								if (CreateEAG2012.REPRODUCTIONSER.equalsIgnoreCase(sectionValueKey)
										&& CreateEAG2012.REPRODUCTIONSER.equalsIgnoreCase(sectionLangKey)) {
									if (object instanceof Reproductionser) {
										Reproductionser reproductionser = (Reproductionser) object;
										if (reproductionser.getDescriptiveNote() == null) {
											reproductionser.setDescriptiveNote(new DescriptiveNote());
										}
										reproductionser.getDescriptiveNote().getP().add(p);
									}
								}
								// eag/archguide/desc/repositories/repository/services/recreationalServices/exhibition/descriptiveNote/P
								if (CreateEAG2012.EXHIBITION.equalsIgnoreCase(sectionValueKey)
										&& CreateEAG2012.EXHIBITION.equalsIgnoreCase(sectionLangKey)) {
									if (object instanceof Exhibition) {
										Exhibition exhibition = (Exhibition) object;
										if (exhibition.getDescriptiveNote() == null) {
											exhibition.setDescriptiveNote(new DescriptiveNote());
											exhibition.getDescriptiveNote().getP().add(p);
										}
									}
								}
								// eag/archguide/desc/repositories/repository/services/recreationalServices/toursSessions/descriptiveNote/P
								if (CreateEAG2012.TOURS_SESSIONS.equalsIgnoreCase(sectionValueKey)
										&& CreateEAG2012.TOURS_SESSIONS.equalsIgnoreCase(sectionLangKey)) {
									if (object instanceof ToursSessions) {
										ToursSessions toursSessions = (ToursSessions) object;
										if (toursSessions.getDescriptiveNote() == null) {
											toursSessions.setDescriptiveNote(new DescriptiveNote());
											toursSessions.getDescriptiveNote().getP().add(p);
										}
									}
								}
								// eag/archguide/desc/repositories/repository/services/recreationalServices/otherServices/descriptiveNote/P
								if (CreateEAG2012.OTHER_SERVICES.equalsIgnoreCase(sectionValueKey)
										&& CreateEAG2012.OTHER_SERVICES.equalsIgnoreCase(sectionLangKey)) {
									if (object instanceof OtherServices) {
										OtherServices otherServices = (OtherServices) object;
										if (otherServices.getDescriptiveNote() == null) {
											otherServices.setDescriptiveNote(new DescriptiveNote());
											otherServices.getDescriptiveNote().getP().add(p);
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}
}
