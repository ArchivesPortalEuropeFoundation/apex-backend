package eu.apenet.dashboard.manual.eag.utils.createObjectJAXB;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import eu.apenet.dashboard.manual.eag.Eag2012;
import eu.apenet.dpt.utils.eag2012.Country;
import eu.apenet.dpt.utils.eag2012.Eag;
import eu.apenet.dpt.utils.eag2012.Firstdem;
import eu.apenet.dpt.utils.eag2012.Localentity;
import eu.apenet.dpt.utils.eag2012.Location;
import eu.apenet.dpt.utils.eag2012.MunicipalityPostalcode;
import eu.apenet.dpt.utils.eag2012.Repository;
import eu.apenet.dpt.utils.eag2012.Secondem;
import eu.apenet.dpt.utils.eag2012.Street;

/**
 * Class for fill elements of location element
 */
public class GetLocationObjectJAXB {
	/**
	 * EAG2012 {@link Eag2012} internal object.
	 */
	protected Eag2012 eag2012;	
	/**
	 * EAG {@link Eag} JAXB object.
	 */
	protected Eag eag;
	private final Logger log = Logger.getLogger(getClass());
	private Repository repository;
	private Location location;
	private String currentLang;

	/**
	 * Method to receives {@link Eag2012} Eag2012 and {@link Eag} Eag for to fill location element
	 * @param eag2012 {@link Eag2012} object Eag2012
	 * @param eag {@link Eag} object Eag
	 * @return object {@link Eag} Eag
	 */
	public Eag GetLocationJAXB(Eag2012 eag2012, Eag eag) {
		// TODO Auto-generated method stub
		this.eag2012=eag2012;
		this.eag=eag;		
		main();
		return this.eag;
	}

	/**
	 * Method main of class GetLocationObjectJAXB for fill elements of all location object JAXB
	 */
	private void main(){
		this.log.debug("Method start: \"Main of class GetLocationObjectJAXB\"");
		if (this.eag2012.getStreetLang() != null) {
			for (int i =0; i < this.eag2012.getStreetLang().size(); i++) {
				if (this.eag2012.getStreetLang().get(i) != null) {
					createStreetLang(i);
					}

				if (this.eag2012.getPostalStreetLang().size() > i
						&& this.eag2012.getPostalStreetLang().get(i) != null) {
					createPostalStreetLang(i);
					}
			}
		}
		this.log.debug("End method: \"Main of class GetLocationObjectJAXB\"");
	}

	/**
	 * Method to fill  visitor addres:<br>
	 * -Fill streetLang.<br>
	 * -Fill latitude.<br>
	 * -Fill country.<br>
	 * -Fill cities.<br>
	 * -Fill street.
	 * @param i {@link int} int
	 */
	private void createStreetLang(int i){
		this.log.debug("Method start: \"createStreetLang\"");
		// Recover repository.
		repository = this.eag.getArchguide().getDesc().getRepositories().getRepository().get(i);
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
			//if (!streetLangKey.equalsIgnoreCase(Eag2012.TAB_YOUR_INSTITUTION))  {
			// Visitor address lists.
			List<String> streetLangList = streetLangMap.get(streetLangKey);
			List<String> latitudeList = latitudeMap.get(latitudeKey);
			List<String> longitudeList = longitudeMap.get(longitudeKey);
			List<String> countryList = countryMap.get(countryKey);
			List<String> firstdemList = null;
			if (this.eag2012.getFirstdemValue() != null) {
				firstdemList = this.eag2012.getFirstdemValue().get(i);
			}
			List<String> secondemList = null;
			if (this.eag2012.getSecondemValue() != null) {
				secondemList = this.eag2012.getSecondemValue().get(i);
			}
			List<String> localentityList = null;
			if (this.eag2012.getLocalentityValue() != null) {
				localentityList = this.eag2012.getLocalentityValue().get(i);
			}
			List<String> citiesList = citiesMap.get(citiesKey);
			List<String> streetList = streetMap.get(streetKey);
			for (int j = 0; j < streetLangList.size(); j++) {
				location = new Location();
				String language = streetLangList.get(j);
				// eag/archguide/desc/repositories/repository/location/type
				location.setLocalType(Eag2012.VISITORS_ADDRESS);
				createLatitudeAndLongitude(latitudeList,longitudeList,j);
				// eag/archguide/desc/repositories/repository/location/country
				createCountryList(countryList,language,j,false);
				// eag/archguide/desc/repositories/repository/location/firstdem
				// eag/archguide/desc/repositories/repository/location/firstdem/lang
				createFirstdemList(firstdemList,language,j);
				// eag/archguide/desc/repositories/repository/location/secondem
				// eag/archguide/desc/repositories/repository/location/secondem/lang
				 createSecondemList(secondemList,language,j);
				// eag/archguide/desc/repositories/repository/location/municipalityPostalcode
				// eag/archguide/desc/repositories/repository/location/municipalityPostalcode/lan
				 createCitiesList(citiesList,language,j,false);
				// eag/archguide/desc/repositories/repository/location/localentity
				// eag/archguide/desc/repositories/repository/location/localentity/lang
				createLocalentityList(localentityList,language,j);
				// eag/archguide/desc/repositories/repository/location/street
				// eag/archguide/desc/repositories/repository/location/street/lang
				createStreetList(streetList,language,j,false);
				//TODO: add looper only for check repeated fields for different contact and your_institution tabs information
				boolean found = false;
				for (int x = 0; !found && x < repository.getLocation().size(); x++) {
					Location target = repository.getLocation().get(x);
					// Current lang.					
					currentLang = Eag2012.OPTION_NONE;
					createCurrentLang(target,false);					
					if (((target.getStreet() != null
								&& target.getStreet().getContent() != null
								&& target.getStreet().getContent().equalsIgnoreCase(streetList.get(j)))
								|| (target.getStreet() == null
									&& streetList.get(j).isEmpty()))
							&& currentLang.equalsIgnoreCase(streetLangList.get(j))
							&& Eag2012.VISITORS_ADDRESS.equalsIgnoreCase(target.getLocalType())
							&& ((target.getMunicipalityPostalcode()!= null
								&& target.getMunicipalityPostalcode().getContent() != null
								&& target.getMunicipalityPostalcode().getContent().equalsIgnoreCase(citiesList.get(j)))
								|| (target.getMunicipalityPostalcode() == null
									&& citiesList.get(j).isEmpty()))
							&& ((target.getCountry() != null
								&& target.getCountry().getContent() != null
								&& target.getCountry().getContent().equalsIgnoreCase(countryList.get(j)))
								|| (target.getCountry() == null
									&& countryList.get(j).isEmpty()))
							&& ((target.getLatitude() != null
								&& target.getLatitude().equalsIgnoreCase(latitudeList.get(j)))
								|| (target.getLatitude() == null
									&& latitudeList.get(j).isEmpty()))
							&& ((target.getLongitude() != null
								&& target.getLongitude().equalsIgnoreCase(longitudeList.get(j)))
								|| (target.getLongitude() == null
									&& longitudeList.get(j).isEmpty()))
							&& (firstdemList != null && firstdemList.size() > j
								&& (target.getFirstdem() != null
									&& target.getFirstdem().getContent() != null
									&& target.getFirstdem().getContent().equalsIgnoreCase(firstdemList.get(j)))
								|| (target.getFirstdem() == null
									&& firstdemList.get(j).isEmpty()))
							&& (secondemList != null && secondemList.size() > j
								&& (target.getSecondem() != null
								&& target.getSecondem().getContent() != null
								&& target.getSecondem().getContent().equalsIgnoreCase(secondemList.get(j)))
								|| (target.getSecondem() == null
									&& secondemList.get(j).isEmpty()))
							&& (localentityList != null && localentityList.size() > j
								&& (target.getLocalentity() != null
									&& target.getLocalentity().getContent() != null
									&& target.getLocalentity().getContent().equalsIgnoreCase(localentityList.get(j)))
								|| (target.getLocalentity() == null
									&& localentityList.get(j).isEmpty()))) {
						found = true;
					}
				}
				if (!found
						&& (location.getCountry() != null
						|| location.getFirstdem() != null
						|| location.getLocalentity() != null
						|| location.getMunicipalityPostalcode() != null
						|| location.getSecondem() != null
						|| location.getStreet() != null)) {
					repository.getLocation().add(location);
				}
			}
//				}
		}
		this.log.debug("End method: \"createStreetLang\"");
	}

	/**
	 * Method to fill postal address:<br>
	 * -Fill postal streetLang.<br>
	 * -Fill postalCountry.<br>
	 * -Fill postalCities.<br>
	 * -Fill postalStreeet.
	 * @param i {@link int} the i
	 */
	private void createPostalStreetLang(int i){
		this.log.debug("Method start: \"createPostalStreetLang\"");
		// Recover repository.
		Repository repository = this.eag.getArchguide().getDesc().getRepositories().getRepository().get(i);
		// Postal address maps.
		Map<String, List<String>> postalStreetLangMap = this.eag2012.getPostalStreetLang().get(i);
		Map<String, List<String>> postalCountryMap = this.eag2012.getCountryValue().get(i);
		Map<String, List<String>> postalCitiesMap = this.eag2012.getMunicipalityPostalcodeValue().get(i);
		Map<String, List<String>> postalStreetMap = this.eag2012.getPostalStreetValue().get(i);
		// Postal address iterators.
		Iterator<String>  postalStreetLangIt = (postalStreetLangMap!=null && postalStreetLangMap.keySet()!=null)?postalStreetLangMap.keySet().iterator():null;;
		Iterator<String>  postalCountryIt = (postalCountryMap!=null && postalCountryMap.keySet()!=null)?postalCountryMap.keySet().iterator():null;
		Iterator<String>  postalCitiesIt = (postalCitiesMap!=null && postalCitiesMap.keySet()!=null)?postalCitiesMap.keySet().iterator():null;;
		Iterator<String>  postalStreetIt = (postalStreetMap!=null && postalStreetMap.keySet()!=null)?postalStreetMap.keySet().iterator():null;;
		while (postalStreetLangIt.hasNext()) {
			// Postal address keys.
			String postalStreetLangKey = (postalStreetLangIt.hasNext())?postalStreetLangIt.next():null;
			String postalCountryKey = (postalCountryIt.hasNext())?postalCountryIt.next():null;
			String postalCitiesKey = (postalCitiesIt.hasNext())?postalCitiesIt.next():null;
			String postalStreetKey = (postalStreetIt.hasNext())?postalStreetIt.next():null;
			// Rest of tabs.
//				if (!postalStreetLangKey.equalsIgnoreCase(Eag2012.TAB_YOUR_INSTITUTION))  {
				// Postal address lists.
				List<String> postalStreetLangList = postalStreetLangMap.get(postalStreetLangKey);
				List<String> postalCountryList = postalCountryMap.get(postalCountryKey);
				List<String> postalCitiesList = postalCitiesMap.get(postalCitiesKey);
				List<String> postalStreetList = postalStreetMap.get(postalStreetKey);
				for (int j = 0; postalStreetLangList!=null && j < postalStreetLangList.size(); j++) {
					 location = new Location();
					String language = postalStreetLangList.get(j);
					// eag/archguide/desc/repositories/repository/location/type
					location.setLocalType(Eag2012.POSTAL_ADDRESS);
					// eag/archguide/desc/repositories/repository/location/country
					// eag/archguide/desc/repositories/repository/location/country/lang
					createCountryList(postalCountryList,language,j,true);
					// eag/archguide/desc/repositories/repository/location/municipalityPostalcode
					// eag/archguide/desc/repositories/repository/location/municipalityPostalcode/lang
					createCitiesList(postalCitiesList,language,j,true);
					// eag/archguide/desc/repositories/repository/location/street
					// eag/archguide/desc/repositories/repository/location/street/lang
					createStreetList(postalStreetList,language,j,true);					
					boolean found = false;
					for (int x = 0; !found && repository.getLocation()!=null && x < repository.getLocation().size(); x++) {
						Location target = repository.getLocation().get(x);
						// Current lang.
						currentLang = Eag2012.OPTION_NONE;
						createCurrentLang(target,true);
						if (((target.getStreet() != null
								&& target.getStreet().getContent() != null
								&& target.getStreet().getContent().equalsIgnoreCase(postalStreetList.get(j)))
								|| (target.getStreet() == null
									&& postalStreetList.get(j).isEmpty()))
								&& currentLang.equalsIgnoreCase(postalStreetLangList.get(j))
								&& Eag2012.POSTAL_ADDRESS.equalsIgnoreCase(target.getLocalType())
								&& ((target.getMunicipalityPostalcode() != null
									&& target.getMunicipalityPostalcode().getContent() != null
									&& target.getMunicipalityPostalcode().getContent().equalsIgnoreCase(postalCitiesList.get(j)))
									|| (target.getMunicipalityPostalcode() == null
										&& postalCitiesList.get(j).isEmpty()))) {
							found = true;
						}
					}
					if (!found
							&& (location.getMunicipalityPostalcode() != null
							|| location.getStreet() != null)) {
						repository.getLocation().add(location);
					}
				}
//				}
		}
		this.log.debug("End method: \"createPostalStreetLang\"");
	}

	/**
	 * Method to fill latitude and longitude element 
	 * @param latitudeList {@link List<String>} latitudeList
	 * @param longitudeList {@link List<String>} longitudeList
	 * @param j {@link int}
	 */
	private void createLatitudeAndLongitude(List<String> latitudeList,List<String> longitudeList,int j){
		// eag/archguide/desc/repositories/repository/location/latitude and longitude
		this.log.debug("Method start: \"createLatitudeAndLongitude\"");
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
		this.log.debug("End method: \"createLatitudeAndLongitude\"");
	}

	/**
	 * Method to fill country element 
	 * @param {@link List<String>} countryList
	 * @param {@link String}language
	 * @param {@link int} j
	 * @param {@link boolean} isPostal
	 */
	private void createCountryList(List<String> countryList1,String language,int j,boolean isPostal){
		this.log.debug("Method start: \"createCountryList\"");
		// eag/archguide/desc/repositories/repository/location/country
		if((countryList1.size() > j && countryList1.get(j) != null	&& !countryList1.get(j).isEmpty()&& !isPostal)
				|| (countryList1!=null && countryList1.size() > 0 && isPostal)){
			if (location.getCountry() == null) {
				location.setCountry(new Country());
			}
			if ((countryList1!=null && countryList1.size() > j && isPostal)
					|| !isPostal) {
				location.getCountry().setContent(countryList1.get(j));
			} else {
				location.getCountry().setContent(countryList1.get(0));
			}
			if (!Eag2012.OPTION_NONE.equalsIgnoreCase(language)) {
				location.getCountry().setLang(language);
			}
			
		}
		
		this.log.debug("End method: \"createCountryList\"");
	}

	/**
	 * Method to fill firstdem element 
	 * @param firstdemList {@link List<String>}  firstdemList
	 * @param language {@link String} String languge
	 * @param j {@link int} int
	 */
	private void createFirstdemList(List<String> firstdemList,String language, int j){
		this.log.debug("Method start: \"createFirstdemList\"");
		// eag/archguide/desc/repositories/repository/location/firstdem and 
		// eag/archguide/desc/repositories/repository/location/firstdem/lang
		if (firstdemList != null && firstdemList.size() > j
				&& firstdemList.get(j) != null
				&& !firstdemList.get(j).isEmpty()) {
			// eag/archguide/desc/repositories/repository/location/firstdem
			if (location.getFirstdem() == null) {
				location.setFirstdem(new Firstdem());
			}
			if (firstdemList.size() > j) {
				location.getFirstdem().setContent(firstdemList.get(j));
				// eag/archguide/desc/repositories/repository/location/firstdem/lang
				if (!Eag2012.OPTION_NONE.equalsIgnoreCase(language)) {
					location.getFirstdem().setLang(language);
				}
			}
		}	
		this.log.debug("End method: \"createFirstdemList\"");
	}

	/**
	 * Method to fill secondem element 
	 * @param secondemList {@link List<String>} secondemList
	 * @param language {@link String} String language
	 * @param j {@link int} int
	 */
	private void createSecondemList(List<String> secondemList,String language, int j){
		this.log.debug("Method start: \"createSecondemList\"");
		// eag/archguide/desc/repositories/repository/location/secondem and  // eag/archguide/desc/repositories/repository/location/secondem/lang
		if (secondemList != null && secondemList.size() > j
				&& secondemList.get(j) != null
				&& !secondemList.get(j).isEmpty()) {
			// eag/archguide/desc/repositories/repository/location/secondem
			if (location.getSecondem() == null) {
				location.setSecondem(new Secondem());
			}
			if (secondemList.size() > j) {
				location.getSecondem().setContent(secondemList.get(j));
				// eag/archguide/desc/repositories/repository/location/secondem/lang
				if (!Eag2012.OPTION_NONE.equalsIgnoreCase(language)) {
					location.getSecondem().setLang(language);
				}
			}
		}
		this.log.debug("End method: \"createSecondemList\"");
	}

	/**
	 * Method to fill municipalityPostalcode element 
	 * @param citiesList {@link List<String>} citiesList
	 * @param language {@link String} String languaje
	 * @param j {@link int} 
	 * @param {@link boolean} isPostal 
	 */
	private void createCitiesList(List<String> citiesList,String language, int j,boolean isPostal){
		this.log.debug("Method start: \"createCitiesList\"");
		// eag/archguide/desc/repositories/repository/location/municipalityPostalcode and // eag/archguide/desc/repositories/repository/location/municipalityPostalcode/lang
		if(citiesList.get(j) != null && !citiesList.get(j).isEmpty() && !isPostal
				|| citiesList!=null && citiesList.get(j) != null && !citiesList.get(j).isEmpty() && isPostal){
			if (location.getMunicipalityPostalcode() == null) {
				location.setMunicipalityPostalcode(new MunicipalityPostalcode());
			}
			location.getMunicipalityPostalcode().setContent(citiesList.get(j));
			// eag/archguide/desc/repositories/repository/location/municipalityPostalcode/lang
			if (!Eag2012.OPTION_NONE.equalsIgnoreCase(language)) {
				location.getMunicipalityPostalcode().setLang(language);
			}
		}
		this.log.debug("End method: \"createCitiesList\"");
	}

	/**
	 * Method to fill localentity element 
	 * @param localentityList {@link List<String>} localentityList
	 * @param language {@link String} String language
	 * @param j {@link int} int
	 */
	private void createLocalentityList(List<String> localentityList,String language, int j){
		this.log.debug("Method start: \"createLocalentityList\"");
		// eag/archguide/desc/repositories/repository/location/localentity and  // eag/archguide/desc/repositories/repository/location/localentity/lang
		if (localentityList != null && localentityList.size() > j
				&& localentityList.get(j) != null
				&& !localentityList.get(j).isEmpty()) {
			// eag/archguide/desc/repositories/repository/location/localentity
			if (location.getLocalentity() == null) {
				location.setLocalentity(new Localentity());
			}
			if (localentityList.size() > j) {
				location.getLocalentity().setContent(localentityList.get(j));
				// eag/archguide/desc/repositories/repository/location/localentity/lang
				if (!Eag2012.OPTION_NONE.equalsIgnoreCase(language)) {
					location.getLocalentity().setLang(language);
				}
			}
		}
		this.log.debug("End method: \"createLocalentityList\"");
	}

	/**
	 * Method to fill street element 
	 * @param streetList {@link List<String>} streetList
	 * @param language {@link String} String language
	 * @param j {@link int} int
	 * @param isPostal {@link boolean}
	 */
	private void createStreetList(List<String> streetList,String language, int j,boolean isPostal){
		this.log.debug("Method start: \"createStreetList\"");
		// eag/archguide/desc/repositories/repository/location/street and	// eag/archguide/desc/repositories/repository/location/street/lang
		if(streetList.get(j) != null  && ! streetList.get(j).isEmpty() && !isPostal
				|| streetList!=null && streetList.get(j) != null && !streetList.get(j).isEmpty()&& isPostal){
			if (location.getStreet() == null) {
				location.setStreet(new Street());
				}
			location.getStreet().setContent(streetList.get(j));
			if (!Eag2012.OPTION_NONE.equalsIgnoreCase(language)) {
				location.getStreet().setLang(language);
				}
		}
		this.log.debug("End method: \"createStreetList\"");		
	}

	/**
	 * Method to fill currentLang
	 * @param target {@link Location} Location target
	 * @param currentLang {@link String} String currentLang
	 * @param isPostal {@link boolean}
	 * @return object {@link Location} Location
	 */
	private void createCurrentLang(Location target, boolean isPostal){
		this.log.debug("Method start: \"createCurrentLang\"");
			if (target.getStreet() != null
					&& target.getStreet().getLang() != null
					&& !target.getStreet().getLang().isEmpty()) {
				currentLang = target.getStreet().getLang();
			} else if (target.getMunicipalityPostalcode() != null
					&& target.getMunicipalityPostalcode().getLang() != null
					&& !target.getMunicipalityPostalcode().getLang().isEmpty()) {
				currentLang = target.getMunicipalityPostalcode().getLang();
			}
		else if(!isPostal){
			 if (target.getCountry() != null
					&& target.getCountry().getLang() != null
					&& !target.getCountry().getLang().isEmpty()) {
				currentLang = target.getCountry().getLang();
			} else if (target.getFirstdem() != null
					&& target.getFirstdem().getLang() != null
					&& !target.getFirstdem().getLang().isEmpty()) {
				currentLang = target.getFirstdem().getLang();
			} else if (target.getSecondem() != null
					&& target.getSecondem().getLang() != null
					&& !target.getSecondem().getLang().isEmpty()) {
				currentLang = target.getSecondem().getLang();
			} else if (target.getLocalentity() != null
					&& target.getLocalentity().getLang() != null
					&& !target.getLocalentity().getLang().isEmpty()) {
				currentLang = target.getLocalentity().getLang();
			}
		}			
		this.log.debug("End method: \"createCurrentLang\"");
		
	}	
}
