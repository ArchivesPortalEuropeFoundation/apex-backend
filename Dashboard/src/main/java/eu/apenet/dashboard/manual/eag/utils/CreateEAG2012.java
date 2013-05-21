package eu.apenet.dashboard.manual.eag.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import eu.apenet.dashboard.manual.eag.Eag2012;
import eu.apenet.dpt.utils.eag2012.Access;
import eu.apenet.dpt.utils.eag2012.Accessibility;
import eu.apenet.dpt.utils.eag2012.AgencyName;
import eu.apenet.dpt.utils.eag2012.Agent;
import eu.apenet.dpt.utils.eag2012.Archguide;
import eu.apenet.dpt.utils.eag2012.Autform;
import eu.apenet.dpt.utils.eag2012.Closing;
import eu.apenet.dpt.utils.eag2012.Control;
import eu.apenet.dpt.utils.eag2012.Country;
import eu.apenet.dpt.utils.eag2012.Desc;
import eu.apenet.dpt.utils.eag2012.Eag;
import eu.apenet.dpt.utils.eag2012.Email;
import eu.apenet.dpt.utils.eag2012.Fax;
import eu.apenet.dpt.utils.eag2012.Firstdem;
import eu.apenet.dpt.utils.eag2012.Identity;
import eu.apenet.dpt.utils.eag2012.Location;
import eu.apenet.dpt.utils.eag2012.MaintenanceAgency;
import eu.apenet.dpt.utils.eag2012.MaintenanceEvent;
import eu.apenet.dpt.utils.eag2012.MaintenanceHistory;
import eu.apenet.dpt.utils.eag2012.MaintenanceStatus;
import eu.apenet.dpt.utils.eag2012.MunicipalityPostalcode;
import eu.apenet.dpt.utils.eag2012.Nonpreform;
import eu.apenet.dpt.utils.eag2012.Opening;
import eu.apenet.dpt.utils.eag2012.OtherAgencyCode;
import eu.apenet.dpt.utils.eag2012.OtherRecordId;
import eu.apenet.dpt.utils.eag2012.OtherRepositorId;
import eu.apenet.dpt.utils.eag2012.Parform;
import eu.apenet.dpt.utils.eag2012.RecordId;
import eu.apenet.dpt.utils.eag2012.RelationEntry;
import eu.apenet.dpt.utils.eag2012.Relations;
import eu.apenet.dpt.utils.eag2012.Repositorid;
import eu.apenet.dpt.utils.eag2012.Repositories;
import eu.apenet.dpt.utils.eag2012.Repository;
import eu.apenet.dpt.utils.eag2012.RepositoryType;
import eu.apenet.dpt.utils.eag2012.ResourceRelation;
import eu.apenet.dpt.utils.eag2012.Restaccess;
import eu.apenet.dpt.utils.eag2012.Secondem;
import eu.apenet.dpt.utils.eag2012.Street;
import eu.apenet.dpt.utils.eag2012.Telephone;
import eu.apenet.dpt.utils.eag2012.Timetable;
import eu.apenet.dpt.utils.eag2012.Webpage;

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
	private static final String VISITORS_ADDRESS = "visitors address";
	private static final String POSTAL_ADDRESS = "postal address";

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
		if (this.eag2012.getParformValue() != null) {
			for (int i = 0; i < this.eag2012.getParformValue().size(); i++) {
				Parform parform = new Parform();
				// eag/archguide/identity/parform
				parform.setContent(this.eag2012.getParformValue().get(i));
				// eag/archguide/identity/parform/lang
				parform.setLang(this.eag2012.getParformLang().get(i));
	
				this.eag.getArchguide().getIdentity().getParform().add(parform);
			}
		}
		// eag/archguide/identity/nonpreform
		if (this.eag2012.getNonpreformValue() != null){
			for (int i = 0; i < this.eag2012.getNonpreformValue().size(); i++) {
				Nonpreform nonpreform = new Nonpreform();
				nonpreform.setLang(this.eag2012.getNonpreformLang().get(i));
				// TODO: Review schema.
				nonpreform.getContent().add(this.eag2012.getNonpreformValue().get(i));

				// eag/archguide/identity/nonpreform/useDates
				if (this.eag2012.getDateStandardDate() != null
						|| (this.eag2012.getFromDateStandardDate() != null
						&& this.eag2012.getToDateStandardDate() != null)) {
					// TODO: Asked by mail.
//					UseDates useDates = new UseDates();
//					if (!this.eag2012.getDateStandardDate().isEmpty()) {
//						
//					}
					// END TODO: Asked by mail.
				}

				this.eag.getArchguide().getIdentity().getNonpreform().add(nonpreform);
			}
		}
		
		// eag/archguide/identity/repositoryType
		if (this.eag2012.getRepositoryTypeValue() != null) {
			for (int i = 0; i < this.eag2012.getRepositoryTypeValue().size(); i++) {
				String[] repositoryTypeList = this.eag2012.getRepositoryTypeValue().get(i).split(",");
				for (int j = 0; j < repositoryTypeList.length; j++) {
					RepositoryType repositoryType = new RepositoryType();
					repositoryType.setValue(repositoryTypeList[j]);

					this.eag.getArchguide().getIdentity().getRepositoryType().add(repositoryType);
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
			if (this.eag2012.getRepositoryNameValue() != null) {
				for (int i = 0; i < this.eag2012.getRepositoryNameValue().size(); i++) {
					repositoryList.add(new Repository());
				}
			} else if (this.eag2012.getOtherRepositorId() != null) {
				repositoryList.add(new Repository());
			}
			this.eag.getArchguide().getDesc().getRepositories().setRepository(repositoryList);
		}

		// eag/archguide/desc/repositories/repository/repositoryName

		// eag/archguide/desc/repositories/repository/repositoryRole

		// eag/archguide/desc/repositories/repository/geogarea

		// eag/archguide/desc/repositories/repository/location
		if (this.eag2012.getCountryLang() != null) {
			for (int i =0; i < this.eag2012.getCountryLang().size(); i++) {
				if (this.eag2012.getCountryLang().get(i) != null) {
					// Recover repository.
					Repository repository = this.eag.getArchguide().getDesc().getRepositories().getRepository().get(i);

					// Visitor address.
					List<String> countryLangList = this.eag2012.getCountryLang().get(i);
					List<String> latitudeList = this.eag2012.getLocationLatitude().get(i);
					List<String> longitudeList = this.eag2012.getLocationLongitude().get(i);
					List<String> countryList = this.eag2012.getCountryValue().get(i);
					List<String> firstdemList = this.eag2012.getFirstdemValue().get(i);
					List<String> secondemList = this.eag2012.getSecondemValue().get(i);
					List<String> citiesList = this.eag2012.getCitiesValue().get(i);
					List<String> streetList = this.eag2012.getStreetValue().get(i);
					for (int j = 0; j < countryLangList.size(); j++) {
						Location location = new Location();
						String language = countryLangList.get(j);
						// eag/archguide/desc/repositories/repository/location/type
						location.setLocalType(CreateEAG2012.VISITORS_ADDRESS);
						// eag/archguide/desc/repositories/repository/location/latitude
						location.setLatitude(latitudeList.get(j));
						// eag/archguide/desc/repositories/repository/location/longitude
						location.setLongitude(longitudeList.get(j));
						// eag/archguide/desc/repositories/repository/location/country
						if (location.getCountry() == null) {
							location.setCountry(new Country());
						}
						location.getCountry().setContent(countryList.get(j));
						// eag/archguide/desc/repositories/repository/location/country/lang
						location.getCountry().setLang(language);
						// eag/archguide/desc/repositories/repository/location/firstdem
						if (location.getFirstdem() == null) {
							location.setFirstdem(new Firstdem());
						}
						if (firstdemList.size() > j) {
							location.getFirstdem().setContent(firstdemList.get(j));
							// eag/archguide/desc/repositories/repository/location/firstdem/lang
							location.getFirstdem().setLang(language);
						}
						// eag/archguide/desc/repositories/repository/location/secondem
						if (location.getSecondem() == null) {
							location.setSecondem(new Secondem());
						}
						if (secondemList.size() > j) {
							location.getSecondem().setContent(secondemList.get(j));
							// eag/archguide/desc/repositories/repository/location/secondem/lang
							location.getSecondem().setLang(language);
						}
						// eag/archguide/desc/repositories/repository/location/municipalityPostalcode
						if (location.getMunicipalityPostalcode() == null) {
							location.setMunicipalityPostalcode(new MunicipalityPostalcode());
						}
						location.getMunicipalityPostalcode().setContent(citiesList.get(j));
						// eag/archguide/desc/repositories/repository/location/municipalityPostalcode/lang
						location.getMunicipalityPostalcode().setLang(language);
						// eag/archguide/desc/repositories/repository/location/localentity
//TODO:					location.getLocalentity().setContent(this.eag2012.getLocalentityValue().get(i).get(j));
						// eag/archguide/desc/repositories/repository/location/localentity/lang
//TODO:					location.getLocalentity().setLang(language);
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

				if (this.eag2012.getPostalStreetLang().get(i) != null) {
					// Recover repository.
					Repository repository = this.eag.getArchguide().getDesc().getRepositories().getRepository().get(i);

					// Postal address.
					List<String> postalStreetLangList = this.eag2012.getPostalStreetLang().get(i);
					List<String> countryList = this.eag2012.getCountryValue().get(i);
					List<String> citiesList = this.eag2012.getMunicipalityPostalcodeValue().get(i);
					List<String> streetList = this.eag2012.getPostalStreetValue().get(i);
					for (int j = 0; j < postalStreetLangList.size(); j++) {
						Location location = new Location();
						String language = postalStreetLangList.get(j);
						// eag/archguide/desc/repositories/repository/location/type
						location.setLocalType(CreateEAG2012.POSTAL_ADDRESS);
						// eag/archguide/desc/repositories/repository/location/country
						if (location.getCountry() == null) {
							location.setCountry(new Country());
						}
						location.getCountry().setContent(countryList.get(j));
						// eag/archguide/desc/repositories/repository/location/country/lang
						location.getCountry().setLang(language);
						// eag/archguide/desc/repositories/repository/location/municipalityPostalcode
						if (location.getMunicipalityPostalcode() == null) {
							location.setMunicipalityPostalcode(new MunicipalityPostalcode());
						}
						location.getMunicipalityPostalcode().setContent(citiesList.get(j));
						// eag/archguide/desc/repositories/repository/location/municipalityPostalcode/lang
						location.getMunicipalityPostalcode().setLang(language);
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
						if (sectionKey == CreateEAG2012.ROOT) {
							List<String> telephoneList = sectionMap.get(sectionKey);
							for (int k = 0; k < telephoneList.size(); k++) {
								Telephone telephone = new Telephone();
								telephone.setContent(telephoneList.get(k));
								repository.getTelephone().add(telephone);
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
							Fax fax = new Fax();
							fax.setContent(faxList.get(k));
							repository.getFax().add(fax);
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
				Repository repository = this.eag.getArchguide().getDesc().getRepositories().getRepository().get(0);

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
						if (sectionValueKey == CreateEAG2012.ROOT
								&& sectionHrefKey == CreateEAG2012.ROOT) {
							List<String> emailValueList = sectionValueMap.get(sectionValueKey);
							List<String> emailHrefList = sectionHrefMap.get(sectionHrefKey);
							for (int k = 0; k < emailValueList.size(); k++) {
								Email email = new Email();
								email.setContent(emailValueList.get(k));
								email.setHref(emailHrefList.get(k));
								repository.getEmail().add(email);
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
				Repository repository = this.eag.getArchguide().getDesc().getRepositories().getRepository().get(0);

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
						if (sectionValueKey == CreateEAG2012.ROOT
								&& sectionHrefKey == CreateEAG2012.ROOT) {
							List<String> webpageValueList = sectionValueMap.get(sectionValueKey);
							List<String> webpageHrefList = sectionHrefMap.get(sectionHrefKey);
							for (int k = 0; k < webpageValueList.size(); k++) {
								Webpage webpage= new Webpage();
								webpage.setContent(webpageValueList.get(k));
								webpage.setHref(webpageHrefList.get(k));
								repository.getWebpage().add(webpage);
							}
						}
					}
				}
			}
		}

		// eag/archguide/desc/repositories/repository/directions
		if (this.eag2012.getDirectionsValue() != null) {
			
		}

		// eag/archguide/desc/repositories/repository/repositorhist

		// eag/archguide/desc/repositories/repository/repositorfuond

		// eag/archguide/desc/repositories/repository/repositorsup

		// eag/archguide/desc/repositories/repository/buildinginfo

		// eag/archguide/desc/repositories/repository/adminhierarchy

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
		//TODO: only for Your institution tab.
		if (this.eag2012.getRestaccessValue() != null /*&& this.eag2012.getRestaccessLang() != null*/) {
			for (int i = 0; i < this.eag2012.getRestaccessValue().size(); i++) {
				Map<String, List<String>> tabsValueMap = this.eag2012.getRestaccessValue().get(i);
// TODO:				Map<String, List<String>> tabsLangMap = this.eag2012.getRestaccessLang().get(i);
				Repository repository = this.eag.getArchguide().getDesc().getRepositories().getRepository().get(i);
				Iterator<String> accessValueIt = tabsValueMap.keySet().iterator();
// TODO:				Iterator<String> accessLangIt = tabsLangMap.keySet().iterator();
				while (accessValueIt.hasNext()) {
					String accessValueKey = accessValueIt.next();
// TODO:					String accessLangKey = accessLangIt.next();
					if (accessValueKey == CreateEAG2012.TAB_YOUR_INSTITUTION) {
						List<String> accessValueList = tabsValueMap.get(accessValueKey);
// TODO:						List<String> accessLangList = tabsLangMap.get(accessLangKey);
						for (int j = 0; j < accessValueList.size(); j++) {
							Restaccess restaccess = new Restaccess();
							restaccess.setContent(accessValueList.get(j));
// TODO:							restaccess.setLang(accessLangList.get(j));
							repository.getAccess().getRestaccess().add(restaccess);
						}
					}
				}
			}
		}

		// eag/archguide/desc/repositories/repository/access/termsOsUse

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
		//TODO: only for Your institution tab.
		if (this.eag2012.getAccessibilityValue() != null /*&& this.eag2012.getAccessibilityLang() != null*/) {
			for (int i = 0; i < this.eag2012.getAccessibilityValue().size(); i++) {
				Map<String, List<String>> tabsValueMap = this.eag2012.getAccessibilityValue().get(i);
// TODO:				Map<String, List<String>> tabsLangMap = this.eag2012.getAccessibilityLang().get(i);
				Repository repository = this.eag.getArchguide().getDesc().getRepositories().getRepository().get(i);
				Iterator<String> accessibilityValueIt = tabsValueMap.keySet().iterator();
// TODO:				Iterator<String> accessibilityLangIt = tabsLangMap.keySet().iterator();
				while (accessibilityValueIt.hasNext()) {
					String accessibilityValueKey = accessibilityValueIt.next();
					if (accessibilityValueKey == CreateEAG2012.TAB_YOUR_INSTITUTION) {
// TODO:						String accessibilityLangKey = accessibilityLangIt.next();
						List<String> accessibilityValueList = tabsValueMap.get(accessibilityValueKey);
// TODO:						List<String> accessibilityLangList = tabsLangMap.get(accessibilityLangKey);
						for (int j = 0; j < accessibilityValueList.size(); j++) {
							Accessibility accessibility = null;
							if (repository.getAccessibility().get(j) == null) {
								accessibility = new Accessibility();
							} else {
								accessibility = repository.getAccessibility().get(j);
							}
							accessibility.setContent(accessibilityValueList.get(j));
// TODO:							accessibility.setLang(accessibilityLangList.get(j));
						}
					}
				}
			}
		}

		// eag/archguide/desc/repositories/repository/services

		// eag/archguide/desc/repositories/repository/descriptivenote

	}
}
