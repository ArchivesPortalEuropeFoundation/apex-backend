package eu.apenet.dashboard.manual.eag.utils.loaderEAG2012;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import eu.apenet.dashboard.manual.eag.Eag2012;
import eu.apenet.dashboard.manual.eag.utils.EAG2012Loader;
import eu.apenet.dpt.utils.eag2012.Eag;
import eu.apenet.dpt.utils.eag2012.Location;
import eu.apenet.dpt.utils.eag2012.Repository;

/**
 * Class for load contact tab values from the XML
 */
public class LoadContactTabValues implements  LoaderEAG2012{
	/**
	 * {@link Repository} the repository
	 */
	private Repository repository;
	/**
	 * {@link Location} the location
	 */
	private Location location;
	private final Logger log = Logger.getLogger(getClass());

	List<String> stringList;
	// Visitor & Postal address.
	private List<String> numberVisitorAdrressList;
	private List<String> latitudeList;
	private List<String> longitudeList;
	private List<String> countryList;
	private List<String> countryLangList;
	private List<String> firstdemList;
	private List<String> firstdemLangList;
	private List<String> secondemList;
	private List<String> secondemLangList;
	private List<String> municipalityList;
	private List<String> municipalityLangList;
	private List<String> localentityList;
	private List<String> localentityLangList;
	private List<String> streetList;
	private List<String> streetLangList;
	private List<String> numberPostalAdrressList;
	private List<String> postalCountryList;
	private List<String> postalCountryLangList;
	private List<String> postalMunicipalityList;
	private List<String> postalMunicipalityLangList;
	private List<String> postalStreetList;
	private List<String> postalStreetLangList;
	private int i;

	/**
	 * @param yiStreetPostal {@link String} the yiStreetPostal to add
	 */
	public void addYiStreetPostal(String yiStreetPostal) {
		this.loader.getYiStreetPostal().add(yiStreetPostal);
	}	
	/**
	 * @param repositoryRole {@link List<String>}  the repositoryRole to add
	 */
	public void addRepositoryRole(List<String> repositoryRole) {
		this.loader.getRepositoryRole().add(repositoryRole);
	}
	/**
	 * @param yiStreetLang {@link String} the yiStreetLang to add
	 */
	public void addYiStreetLang(String yiStreetLang) {
		this.loader.getYiStreetLang().add(yiStreetLang);
	}
	/**
	 * @param repositoryName {@link List<String>} the repositoryName to add
	 */
	public void addRepositoryName(List<String> repositoryName) {
		this.loader.getRepositoryName().add(repositoryName);
	}
	/**
	 * @param contactLatitude {@link List<String>} the contactLatitude to add
	 */
	public void addContactLatitude(List<String> contactLatitude) {
		this.loader.getContactLatitude().add(contactLatitude);
	}		
	/**
	 * @param contactLongitude {@link List<String>} the contactLongitude to add
	 */
	public void addContactLongitude(List<String> contactLongitude) {
		this.loader.getContactLongitude().add(contactLongitude);
	}
	/**
	 * @param contactCountry {@link List<String>} the contactCountry to add
	 */
	public void addContactCountry(List<String> contactCountry) {
		this.loader.getContactCountry().add(contactCountry);
	}
	/**
	 * @param contactCountryLang {@link List<String>} the contactCountryLang to add
	 */
	public void addContactCountryLang(List<String> contactCountryLang) {
		this.loader.getContactCountryLang().add(contactCountryLang);
	}
	/**
	 * @param contactFirstdem {@link List<String>} the contactFirstdem to add
	 */
	public void addContactFirstdem(List<String> contactFirstdem) {
		this.loader.getContactFirstdem().add(contactFirstdem);
	}
	/**
	 * @param contactFirstdemLang {@link List<String>} the contactFirstdemLang to add
	 */
	public void addContactFirstdemLang(List<String> contactFirstdemLang) {
		this.loader.getContactFirstdemLang().add(contactFirstdemLang);
	}
	/**
	 * @param contactSecondem {@link List<String>} the contactSecondem to add
	 */
	public void addContactSecondem(List<String> contactSecondem) {
		this.loader.getContactSecondem().add(contactSecondem);
	}
	/**
	 * @param contactSecondemLang {@link List<String>} the contactSecondemLang to add
	 */
	public void addContactSecondemLang(List<String> contactSecondemLang) {
		this.loader.getContactSecondemLang().add(contactSecondemLang);
	}
	/**
	 * @param contactMunicipality {@link List<String>} the contactMunicipality to add
	 */
	public void addContactMunicipality(List<String> contactMunicipality) {
		this.loader.getContactMunicipality().add(contactMunicipality);
	}		
	/**
	 * @param contactMunicipalityLang {@link List<String>} the contactMunicipalityLang to add
	 */
	public void addContactMunicipalityLang(List<String> contactMunicipalityLang) {
		this.loader.getContactMunicipalityLang().add(contactMunicipalityLang);
	}
	/**
	 * @param contactLocalentity {@link List<String>} the contactLocalentity to add
	 */
	public void addContactLocalentity(List<String> contactLocalentity) {
		this.loader.getContactLocalentity().add(contactLocalentity);
	}
	/**
	 * @param contactLocalentityLang {@link List<String>} the contactLocalentityLang to add
	 */
	public void addContactLocalentityLang(List<String> contactLocalentityLang) {
		this.loader.getContactLocalentityLang().add(contactLocalentityLang);
	}
	/**
	 * @param contactStreet {@link List<String>} the contactStreet to add
	 */
	public void addContactStreet(List<String> contactStreet) {
		this.loader.getContactStreet().add(contactStreet);
	}
	/**
	 * @param contactStreetLang {@link List<String>} the contactStreetLang to add
	 */
	public void addContactStreetLang(List<String> contactStreetLang) {
		this.loader.getContactStreetLang().add(contactStreetLang);
	}
	/**
	 * @param contactPostalCountry {@link List<String>} the contactPostalCountry to add
	 */
	public void addContactPostalCountry(List<String> contactPostalCountry) {
		this.loader.getContactPostalCountry().add(contactPostalCountry);
	}
	/**
	 * @param contactPostalCountryLang {@link List<String>} the contactPostalCountryLang to add
	 */
	public void addContactPostalCountryLang(List<String> contactPostalCountryLang) {
		this.loader.getContactPostalCountryLang().add(contactPostalCountryLang);
	}
	/**
	 * @param contactPostalMunicipality {@link List<String>} the contactPostalMunicipality to add
	 */
	public void addContactPostalMunicipality(List<String> contactPostalMunicipality) {
		this.loader.getContactPostalMunicipality().add(contactPostalMunicipality);
	}
	/**
	 * @param contactPostalMunicipalityLang {@link List<String>} the contactPostalMunicipalityLang to add
	 */
	public void addContactPostalMunicipalityLang(List<String> contactPostalMunicipalityLang) {
		this.loader.getContactPostalMunicipalityLang().add(contactPostalMunicipalityLang);
	}
	/**
	 * @param contactPostalStreet {@link List<String>} the contactPostalStreet to add
	 */
	public void addContactPostalStreet(List<String> contactPostalStreet) {
		this.loader.getContactPostalStreet().add(contactPostalStreet);
	}
	/**
	 * @param contactPostalStreetLang {@link List<String>} the contactPostalStreetLang to add
	 */
	public void addContactPostalStreetLang(List<String> contactPostalStreetLang) {
		this.loader.getContactPostalStreetLang().add(contactPostalStreetLang);
	}
	/**
	 * @param contactPostalStreetLang {@link List<String>} the contactPostalStreetLang to add
	 */
	public void addContactContinent(List<String> contactContinent) {
		this.loader.getContactContinent().add(contactContinent);
	}
	/**
	 * @param contactTelephone {@link List<String>} the contactTelephone to add
	 */
	public void addContactTelephone(List<String> contactTelephone) {
		this.loader.getContactTelephone().add(contactTelephone);
	}	
	/**
	 * @param contactFax {@link List<String>} the contactFax to add
	 */
	public void addContactFax(List<String> contactFax) {
		this.loader.getContactFax().add(contactFax);
	}
	/**
	 * @param contactEmailHref {@link List<String>} the contactEmailHref to add
	 */
	public void addContactEmailHref(List<String> contactEmailHref) {
		this.loader.getContactEmailHref().add(contactEmailHref);
	}
	/**
	 * @param contactEmailTitle {@link List<String>} the contactEmailTitle to add
	 */
	public void addContactEmailTitle(List<String> contactEmailTitle) {
		this.loader.getContactEmailTitle().add(contactEmailTitle);
	}
	/**
	 * @param contactEmailLang {@link List<String>} the contactEmailLang to add
	 */
	public void addContactEmailLang(List<String> contactEmailLang) {
		this.loader.getContactEmailLang().add(contactEmailLang);
	}
	/**
	 * @param contactWebpageHref {@link List<String>} the contactWebpageHref to add
	 */
	public void addContactWebpageHref(List<String> contactWebpageHref) {
		this.loader.getContactWebpageHref().add(contactWebpageHref);
	}		
	/**
	 * @param contactWebpageTitle {@link List<String>} the contactWebpageTitle to add
	 */
	public void addContactWebpageTitle(List<String> contactWebpageTitle) {
		this.loader.getContactWebpageTitle().add(contactWebpageTitle);
	}
	/**
	 * @param contactWebpageLang {@link List<String>} the contactWebpageLang to add
	 */
	public void addContactWebpageLang(List<String> contactWebpageLang) {
		this.loader.getContactWebpageLang().add(contactWebpageLang);
	}
	/**
	 * @param contactNumberOfVisitorsAddress {@link List<String>} the contactNumberOfVisitorsAddress to add
	 */
	public void addContactNumberOfVisitorsAddress(List<String> contactNumberOfVisitorsAddress) {
		this.loader.getContactNumberOfVisitorsAddress().add(contactNumberOfVisitorsAddress);
	}
	/**
	 * @param contactNumberOfPostalAddress {@link List<String>} the contactNumberOfPostalAddress to add
	 */
	public void addContactNumberOfPostalAddress(List<String> contactNumberOfPostalAddress) {
		this.loader.getContactNumberOfPostalAddress().add(contactNumberOfPostalAddress);
	}
	/**
	 * @param contactNumberOfEmailAddress {@link List<String>} the contactNumberOfEmailAddress to add
	 */
	public void addContactNumberOfEmailAddress(List<String> contactNumberOfEmailAddress) {
		this.loader.getContactNumberOfEmailAddress().add(contactNumberOfEmailAddress);
	}
	/**
	 * @param contactNumberOfWebpageAddress  {@link List<String>} the contactNumberOfWebpageAddress to add
	 */
	public void addContactNumberOfWebpageAddress(List<String> contactNumberOfWebpageAddress) {
		this.loader.getContactNumberOfWebpageAddress().add(contactNumberOfWebpageAddress);
	}		
	private EAG2012Loader loader;
	/**
	 * Eag {@link eag} JAXB object.
	 */
	protected Eag eag;

	@Override
	public Eag LoaderEAG2012(Eag eag, EAG2012Loader eag2012Loader) {
		
		this.eag=eag;
		this.loader = eag2012Loader;
		main();
		return this.eag;
	}

	/**
	 * Method for  get {@link String} geoarea
	 * @param {@link String} geoarea
	 * @return {@link String} geoareaValue
	 */
	private String getGeogareaString(final String geogarea) {
		this.log.debug("Method start: \"loadAccessAndServicesSearchRoomReaderTicket\"");
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
	 * Method main for class LoadContactTabValues
	 */
	private void main(){
		this.log.debug("Method start: \"Main of class LoadContactTabValues\"");
		// Repositories info.
		if (this.eag.getArchguide() != null && this.eag.getArchguide().getDesc() != null
				&& this.eag.getArchguide().getDesc().getRepositories() != null
				&& !this.eag.getArchguide().getDesc().getRepositories().getRepository().isEmpty()) {
			// For each repository
			for (i = 0; i < this.eag.getArchguide().getDesc().getRepositories().getRepository().size(); i++) {
				 repository = this.eag.getArchguide().getDesc().getRepositories().getRepository().get(i);
				if (repository != null) {
					// Repository name.
					stringList = new ArrayList<String>();
					if (repository.getRepositoryName() != null && repository.getRepositoryName().size()>0 
							&& repository.getRepositoryName().get(0).getContent() != null
							&& !repository.getRepositoryName().get(0).getContent().isEmpty()) {
						stringList.add(repository.getRepositoryName().get(0).getContent());
					} else {
						stringList.add("");
					}
					this.addRepositoryName(stringList);
						// Repository role.
					loadContactRepositoryRole();
					// Visitor & Postal address.
					loadContactAdress();
					// Add the language for "Your institution" tab for element "Visitor address".
					loadContactAddTheLanguage();
					// Continent.
					loadContactCTF();
					// E-mail address.
					loadContactEmailAdrdress();
					// Webpage.
					loadContactWebpage();
				}
			}
		}
		this.log.debug("End method: \"Main of class LoadContactTabValues\"");
	}

	/**
	 * Method to load all values of "contact" tab in the part of Repository role of institution
	 */
	private void loadContactRepositoryRole(){
		this.log.debug("Method start: \"loadContactRepositoryRole\"");
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
		this.log.debug("End method: \"loadContactRepositoryRole\"");
	}

	/**
	 * Method to load all values of "contact" tab in the part of Visitor & Postal address of institution
	 */
	private void loadContactAdress(){
		this.log.debug("Method start: \"loadContactAdress\"");
		// Visitor & Postal address.
		numberVisitorAdrressList = new ArrayList<String>();
		latitudeList = new ArrayList<String>();
		longitudeList = new ArrayList<String>();
		countryList = new ArrayList<String>();
		countryLangList = new ArrayList<String>();
		firstdemList = new ArrayList<String>();
		firstdemLangList = new ArrayList<String>();
		secondemList = new ArrayList<String>();
		secondemLangList = new ArrayList<String>();
		municipalityList = new ArrayList<String>();
		municipalityLangList = new ArrayList<String>();
		localentityList = new ArrayList<String>();
		localentityLangList = new ArrayList<String>();
		streetList = new ArrayList<String>();
		streetLangList = new ArrayList<String>();	
		numberPostalAdrressList = new ArrayList<String>();
		postalCountryList = new ArrayList<String>();
		postalCountryLangList = new ArrayList<String>();
		postalMunicipalityList = new ArrayList<String>();
		postalMunicipalityLangList = new ArrayList<String>();
		postalStreetList = new ArrayList<String>();
		postalStreetLangList = new ArrayList<String>();
		if (!repository.getLocation().isEmpty()) {
			for (int j = 0; j < repository.getLocation().size(); j++) {
				location = repository.getLocation().get(j);
				if (location.getLocalType() != null
						&& location.getLocalType().equalsIgnoreCase(Eag2012.VISITORS_ADDRESS)) {
					numberVisitorAdrressList.add("");					
					// Latitude.Longitude.
					loadContactVisitorAdressLatitudeLongitude();
					// Country.
					loadContactVisitorAdressCountry();
					// Autonomous community / region.
					loadContactVisitorAdressAutonomous();
					//County/local authority.
					loadContactVisitorAdressLocalAuthority();
					// City.
					loadContactVisitorAdressCity();
					// District/quarter in town.
					loadContactVisitorAdressDistrict();
					// Street.
				    loadContactVisitorAdressStreet();
				}	
				if (location.getLocalType()!=null && location.getLocalType().equalsIgnoreCase(Eag2012.POSTAL_ADDRESS)) {
					numberPostalAdrressList.add("");
					// Country.
					loadContactPostalCountry();
					// Postal city.
					loadContactPostalCity();
					// Postal street.
					loadContactPostalStreet();
				}
			}	
			// Check language for "Visitor address" element.
			loadContactVisitorAdressCheck();
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
		this.log.debug("End method: \"loadContactAdress\"");
	}

	/**
	 * Method to load all values of "contact" tab in the part of Visitor address in latitude and longitude of institution
	 */
	private void loadContactVisitorAdressLatitudeLongitude(){
		this.log.debug("Method start: \"loadContactVisitorAdressLatitudeLongitude\"");
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
		this.log.debug("End method: \"loadContactVisitorAdressLatitudeLongitude\"");
	}

	/**
	 * Method to load all values of "contact" tab in the part of Visitor address in Country of institution
	 */
	private void loadContactVisitorAdressCountry(){
		this.log.debug("Method start: \"loadContactVisitorAdressCountry\"");
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
		this.log.debug("End method: \"loadContactVisitorAdressCountry\"");
	}

	/**
	 * Method to load all values of "contact" tab in the part of Visitor address in Autonomous community / region of institution
	 */
	private void loadContactVisitorAdressAutonomous(){
		this.log.debug("Method start: \"loadContactVisitorAdressAutonomous\"");
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
		this.log.debug("End method: \"loadContactVisitorAdressAutonomous\"");
	}

	/**
	 * Method to load all values of "contact" tab in the part of Visitor address in city of institution
	 */
	private void loadContactVisitorAdressCity(){
		this.log.debug("Method start: \"loadContactVisitorAdressCity\"");
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
		this.log.debug("End method: \"loadContactVisitorAdressCity\"");		
		
	}

	/**
	 * Method to load all values of "contact" tab in the part of Visitor address in County/local authority of institution
	 */
	private void loadContactVisitorAdressLocalAuthority(){
		this.log.debug("Method start: \"loadContactVisitorAdressLocalAuthority\"");
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
		this.log.debug("End method: \"loadContactVisitorAdressLocalAuthority\"");
	}

	/**
	 * Method to load all values of "contact" tab in the part of Visitor address in istrict/quarter in town of institution
	 */
	private void loadContactVisitorAdressDistrict(){
		this.log.debug("Method start: \"loadContactVisitorAdressDistrict\"");
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
				this.log.debug("End method: \"loadContactVisitorAdressDistrict\"");
	}

	/**
	 * Method to load all values of "contact" tab in the part of Visitor address in Street of institution
	 */
	private void loadContactVisitorAdressStreet(){
		this.log.debug("Method start: \"loadContactVisitorAdressStreet\"");
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
		this.log.debug("End method: \"loadContactVisitorAdressStreet\"");
	}

	/**
	 * Method to load all values of "contact" tab in the part of Postal address in country of institution
	 */
	private void loadContactPostalCountry(){
		this.log.debug("Method start: \"loadContactPostalCountry\"");
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
		this.log.debug("End method: \"loadContactPostalCountry\"");		
	}

	/**
	 * Method to load all values of "contact" tab in the part of Postal address in city of institution
	 */
	private void loadContactPostalCity(){
		this.log.debug("Method start: \"loadContactPostalCity\"");
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
		this.log.debug("End method: \"loadContactPostalCity\"");
		
	}

	/**
	 * Method to load all values of "contact" tab in the part of Postal address in street of institution
	 */
	private void loadContactPostalStreet(){
		this.log.debug("Method start: \"loadContactPostalStreet\"");
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
		this.log.debug("End method: \"loadContactPostalStreet\"");
	}

	/**
	 * Method to load all values of "contact" tab Check language for "Visitor address" element and Check language for "Postal address" element.  of institution
	 */
	private void loadContactVisitorAdressCheck(){
		this.log.debug("Method start: \"loadContactVisitorAdressCheck\"");
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
		this.log.debug("End method: \"loadContactVisitorAdressCheck\"");
	}

	/**
	 * Method to load all values of "contact" tab in the part of Add the language for "Your institution" tab for element "Visitor address" and Add the language for "Your institution" tab for element "Postal address"  of institution
	 */
	private void loadContactAddTheLanguage(){
		this.log.debug("Method start: \"loadContactAddTheLanguage\"");
		// Add the language for "Your institution" tab for element "Visitor address".
		if (i == 0) {
			for (int j = 0; j < streetLangList.size(); j++) {
				if (this.loader.getYiStreetLang().size() > j
						&& this.loader.getYiStreetLang().get(j) != null) {
					this.loader.getYiStreetLang().remove(j);
					this.loader.getYiStreetLang().add(j, streetLangList.get(j));
				} else {
					this.loader.getYiStreetLang().add(streetLangList.get(j));
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
				if (this.loader.getYiStreetPostalLang().size() > j
						&& this.loader.getYiStreetPostalLang().get(j) != null) {
					this.loader.getYiStreetPostalLang().remove(j);
					this.loader.getYiStreetPostalLang().add(j, postalStreetLangList.get(j));
				} else {
					this.loader.getYiStreetPostalLang().add(postalStreetLangList.get(j));
				}
			}
		}	
		this.log.debug("End method: \"loadContactAddTheLanguage\"");
	}

	/**
	 * Method to load all values of "contact" tab in the part of Continent, telephone and fax of institution
	 */
	private void loadContactCTF(){
		this.log.debug("Method start: \"loadContactCTF\"");
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
		this.log.debug("End method: \"loadContactCTF\"");
	}

	/**
	 * Method to load all values of "contact" tab in the part of  E-mail address of institution
	 */
	private void loadContactEmailAdrdress(){
		this.log.debug("Method start: \"loadContactEmailAdrdress\"");
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
		this.log.debug("End method: \"loadContactEmailAdrdress\"");
	}

	/**
	 * Method to load all values of "contact" tab in the part of Webpage  of institution
	 */
	private void loadContactWebpage(){
		this.log.debug("Method start: \"loadContactWebpage\"");
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
		this.log.debug("End method: \"loadContactWebpage\"");	
	}
}
