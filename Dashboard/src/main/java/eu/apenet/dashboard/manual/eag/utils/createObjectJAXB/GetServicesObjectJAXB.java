package eu.apenet.dashboard.manual.eag.utils.createObjectJAXB;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import eu.apenet.dashboard.manual.eag.Eag2012;
import eu.apenet.dpt.utils.eag2012.AdvancedOrders;
import eu.apenet.dpt.utils.eag2012.Buildinginfo;
import eu.apenet.dpt.utils.eag2012.ComputerPlaces;
import eu.apenet.dpt.utils.eag2012.Digitalser;
import eu.apenet.dpt.utils.eag2012.Eag;
import eu.apenet.dpt.utils.eag2012.Extent;
import eu.apenet.dpt.utils.eag2012.Holdings;
import eu.apenet.dpt.utils.eag2012.InternetAccess;
import eu.apenet.dpt.utils.eag2012.Lengthshelf;
import eu.apenet.dpt.utils.eag2012.Library;
import eu.apenet.dpt.utils.eag2012.MicrofilmPlaces;
import eu.apenet.dpt.utils.eag2012.Microformser;
import eu.apenet.dpt.utils.eag2012.Monographicpub;
import eu.apenet.dpt.utils.eag2012.Num;
import eu.apenet.dpt.utils.eag2012.Photocopyser;
import eu.apenet.dpt.utils.eag2012.PhotographAllowance;
import eu.apenet.dpt.utils.eag2012.Photographser;
import eu.apenet.dpt.utils.eag2012.ReadersTicket;
import eu.apenet.dpt.utils.eag2012.Repositorarea;
import eu.apenet.dpt.utils.eag2012.Repository;
import eu.apenet.dpt.utils.eag2012.Reproductionser;
import eu.apenet.dpt.utils.eag2012.Restorationlab;
import eu.apenet.dpt.utils.eag2012.Serialpub;
import eu.apenet.dpt.utils.eag2012.Services;
import eu.apenet.dpt.utils.eag2012.Techservices;
import eu.apenet.dpt.utils.eag2012.WorkPlaces;

/**
 * Class for fill elements of services element
 */
public class GetServicesObjectJAXB extends AbstractObjectJAXB{
	/**
	 * EAG2012 {@link Eag2012} internal object.
	 */
	protected Eag2012 eag2012;	
	/**
	 * Eag {@link Eag} JAXB object.
	 */
	protected Eag eag;
	private final Logger log = Logger.getLogger(getClass());
	private Repository repository;
	private Reproductionser reproductionser;

	/**
	 * Class GetServicesJAXB for fill the elements of Services element
	 * @param eag2012 {@link Eag2012} object Eag2012
	 * @param eag {@link Eag} object Eag
	 * @return {@link Eag} object Eag
	 */
	public Eag GetServicesJAXB(Eag2012 eag2012, Eag eag) {
		this.log.debug("Method start: \"GetServicesJAXB\"");
		this.eag2012=eag2012;
		this.eag=eag;		
		getAllNums();
		createAdvancedOrders();
		createPhotographAllowance();
		createReadersTicket();
		createLibrary();
		createQuestion();
		createInternetAccess();
		createTechservicesRestorationlab();
		createTechservicesReproductionser();
		this.log.debug("End method: \"GetServicesJAXB\"");
		return this.eag;
	}

	/**
	 * Method to fill all num elements.
	 */
	private void getAllNums() {
		this.log.debug("Method start: \"getAllNums\"");
		if (this.eag2012.getNumValue() != null) {
			for (int i = 0; i < this.eag2012.getNumValue().size(); i++) {
				Map<String, Map<String, Map<String, List<String>>>> tabsMap = this.eag2012.getNumValue().get(i);
				// Repository.
			    repository = this.eag.getArchguide().getDesc().getRepositories().getRepository().get(i);
				Iterator<String> tabsIt = tabsMap.keySet().iterator();
				while (tabsIt.hasNext()) {
					String tabKey = tabsIt.next();
					if (Eag2012.TAB_ACCESS_AND_SERVICES.equalsIgnoreCase(tabKey)) {
						Map<String, Map<String, List<String>>> sectionsMap = tabsMap.get(tabKey);
						Iterator<String> sectionsIt = sectionsMap.keySet().iterator();
						while (sectionsIt.hasNext()) {
							String sectionKey = sectionsIt.next();
							if (Eag2012.SEARCHROOM.equalsIgnoreCase(sectionKey)) {
								createSearchroom( sectionKey, i, sectionsMap);
							}
						}
					} else if (Eag2012.TAB_DESCRIPTION.equalsIgnoreCase(tabKey)) {
						parseTabDescription( tabKey, tabsMap);
					}
				}
			}
		}
		this.log.debug("End method: \"getAllNums\"");
	}

	/**
	 * Method to fill photographAllowance element 
	 */
	private void createPhotographAllowance(){
		this.log.debug("Method start: \"createPhotographAllowance\"");
	    // eag/archguide/desc/repositories/repository/services/searchroom/photographAllowance
			if (this.eag2012.getPhotographAllowanceValue() != null) {
				for (int i = 0; i < this.eag2012.getPhotographAllowanceValue().size(); i++) {
					String value = this.eag2012.getPhotographAllowanceValue().get(i);
					// Repository.
					Repository repository = this.eag.getArchguide().getDesc().getRepositories().getRepository().get(i);
					if (!Eag2012.OPTION_NONE.equalsIgnoreCase(value)) {
						if (Eag2012.OPTION_DEPENDING.equalsIgnoreCase(value)) {
							value = Eag2012.OPTION_DEPENDING_TEXT;
						} else if (Eag2012.OPTION_WITHOUT.equalsIgnoreCase(value)) {
							value = Eag2012.OPTION_WITHOUT_TEXT;
						}
						createRepositorySearchroom( repository,false);
						PhotographAllowance photographAllowance = new PhotographAllowance();
						photographAllowance.setValue(value);
						repository.getServices().getSearchroom().setPhotographAllowance(photographAllowance);
					}
				}
			}
		this.log.debug("End method: \"createPhotographAllowance\"");
	}

	/**
	 * Method to fill readersTicket element 
	 */
	private void createReadersTicket(){
		this.log.debug("Method start: \"createReadersTicket\"");
			// eag/archguide/desc/repositories/repository/services/searchroom/readersTicket
			if (this.eag2012.getReadersTicketValue() != null && this.eag2012.getReadersTicketHref() != null
					&& this.eag2012.getReadersTicketLang() != null) {
				for (int i = 0; i < this.eag2012.getReadersTicketValue().size(); i++) {
					List<String> valueList = this.eag2012.getReadersTicketValue().get(i);
					List<String> hrefList = this.eag2012.getReadersTicketHref().get(i);
					List<String> langList = this.eag2012.getReadersTicketLang().get(i);
					// Repository.
					Repository repository = this.eag.getArchguide().getDesc().getRepositories().getRepository().get(i);
					for (int j = 0; j < valueList.size(); j++) {
						if ((valueList.get(j) != null && !valueList.get(j).isEmpty())
								|| (hrefList.get(j) != null && !hrefList.get(j).isEmpty())) {
							repository = createRepositorySearchroom( repository,false);		
							ReadersTicket readersTicket = new ReadersTicket();
							readersTicket.setContent(valueList.get(j));
							if (hrefList.get(j) != null && !hrefList.get(j).isEmpty()) {
								readersTicket.setHref(hrefList.get(j));
							}
							if (!Eag2012.OPTION_NONE.equalsIgnoreCase(langList.get(j))) {
								readersTicket.setLang(langList.get(j));
							}		
							repository.getServices().getSearchroom().getReadersTicket().add(readersTicket);
						}
					}
				}
			}
			this.log.debug("End method: \"createReadersTicket\"");
	}

	/**
	 * Method to fill advancedOrders element 
	 */
	private void createAdvancedOrders(){
		this.log.debug("Method start: \"createAdvancedOrders\"");
			// eag/archguide/desc/repositories/repository/services/searchroom/advancedOrders
			if (this.eag2012.getAdvancedOrdersValue() != null && this.eag2012.getAdvancedOrdersHref() != null
					&& this.eag2012.getAdvancedOrdersLang() != null) {
				for (int i = 0; i < this.eag2012.getAdvancedOrdersValue().size(); i++) {
					List<String> valueList = this.eag2012.getAdvancedOrdersValue().get(i);
					List<String> hrefList = this.eag2012.getAdvancedOrdersHref().get(i);
					List<String> langList = this.eag2012.getAdvancedOrdersLang().get(i);
					// Repository.
					repository = this.eag.getArchguide().getDesc().getRepositories().getRepository().get(i);
					for (int j = 0; j < valueList.size(); j++) {
						if ((valueList.get(j) != null && !valueList.get(j).isEmpty())
								|| (hrefList.get(j) != null && !hrefList.get(j).isEmpty())) {
							repository = createRepositorySearchroom( repository,false);		
							AdvancedOrders advancedOrders = new AdvancedOrders();
							advancedOrders.setContent(valueList.get(j));
							if (hrefList.get(j) != null && !hrefList.get(j).isEmpty()) {
								advancedOrders.setHref(hrefList.get(j));
							}
							if (!Eag2012.OPTION_NONE.equalsIgnoreCase(langList.get(j))) {
								advancedOrders.setLang(langList.get(j));
							}		
							repository.getServices().getSearchroom().getAdvancedOrders().add(advancedOrders);
						}
					}
				}
			}
		this.log.debug("End method: \"createAdvancedOrders\"");
	}

	/**
	 * Method to fill searchroom element 
	 * @param sectionKey {@link String} the sectionKey
	 * @param i {@link int} i
	 * @param sectionsMap {@link Map<String, Map<String, List<String>>>} the sectionsMap
	 */
	private void createSearchroom(String sectionKey,int i,Map<String, Map<String, List<String>>> sectionsMap){
		// eag/archguide/desc/repositories/repository/services/searchroom
		this.log.debug("Method start: \"createSearchroom\"");
		Map<String, List<String>> subSectionsMap = sectionsMap.get(sectionKey);
		Iterator<String> subSectionsIt = subSectionsMap.keySet().iterator();
		while (subSectionsIt.hasNext()) {
			String subSectionKey = subSectionsIt.next();
			// eag/archguide/desc/repositories/repository/services/searchroom/workPlaces
			if (Eag2012.WORKING_PLACES.equalsIgnoreCase(subSectionKey)) {
				List<String> valueList = subSectionsMap.get(subSectionKey);
				for (int j = 0; j < valueList.size(); j++) {
					if (valueList.get(j) == null || valueList.get(j).isEmpty()) {
						break;
					}
					repository = createRepositorySearchroom( repository,false);					
					Num num = new Num();
					num.setContent(valueList.get(j));
					num.setUnit(Eag2012.UNIT_SITE);
					WorkPlaces workPlaces = new WorkPlaces();
					workPlaces.setNum(num);
					repository.getServices().getSearchroom().setWorkPlaces(workPlaces);
				}
			} else if (Eag2012.COMPUTER_PLACES.equalsIgnoreCase(subSectionKey)) {
				// eag/archguide/desc/repositories/repository/services/searchroom/ComputerPlaces
				List<String> valueList = subSectionsMap.get(subSectionKey);
				for (int j = 0; j < valueList.size(); j++) {
					if (valueList.get(j) == null || valueList.get(j).isEmpty()) {
						break;
					}
					repository = createRepositorySearchroom( repository,false);
					Num num = new Num();
					num.setContent(valueList.get(j));
					num.setUnit(Eag2012.UNIT_SITE);
					ComputerPlaces computerPlaces = new ComputerPlaces();
					computerPlaces.setNum(num);
					// eag/archguide/desc/repositories/repository/services/searchroom/ComputerPlaces/descriptiveNote
					this.eag2012 = getDescriptiveNoteServices(computerPlaces, -1, i,this.eag2012);
					repository.getServices().getSearchroom().setComputerPlaces(computerPlaces);
				}
			} else if (Eag2012.MICROFILM.equalsIgnoreCase(subSectionKey)) {
				// eag/archguide/desc/repositories/repository/services/searchroom/microfilmPlaces
				List<String> valueList = subSectionsMap.get(subSectionKey);
				for (int j = 0; j < valueList.size(); j++) {
					if (valueList.get(j) == null || valueList.get(j).isEmpty()) {
						break;
					}
					repository = createRepositorySearchroom( repository,false);
					Num num = new Num();
					num.setContent(valueList.get(j));
					num.setUnit(Eag2012.UNIT_SITE);
					MicrofilmPlaces microfilmPlaces = new MicrofilmPlaces();
					microfilmPlaces.setNum(num);
					repository.getServices().getSearchroom().setMicrofilmPlaces(microfilmPlaces);
				}
			}
		}
		this.log.debug("End method: \"createSearchroom\"");
	}

	/**
	 * Method parse tab description
	 * @param tabKey {@link String} tabKey
	 * @param tabsMap {@link Map<String, Map<String, Map<String, List<String>>>>} the tabsMap
	 */
	private void parseTabDescription(String tabKey,Map<String, Map<String, Map<String, List<String>>>> tabsMap){
		this.log.debug("Method start: \"parseTabDescription\"");
		Map<String, Map<String, List<String>>> sectionsMap = tabsMap.get(tabKey);
		Iterator<String> sectionsIt = sectionsMap.keySet().iterator();
		while (sectionsIt.hasNext()) {
			String sectionKey = sectionsIt.next();
			if (Eag2012.BUILDING.equalsIgnoreCase(sectionKey)) {
				Map<String, List<String>> subSectionsMap = sectionsMap.get(sectionKey);
				Iterator<String> subSectionsIt = subSectionsMap.keySet().iterator();
				while (subSectionsIt.hasNext()) {
					String subSectionKey = subSectionsIt.next();
					createBuildinginfo(subSectionsMap,subSectionsIt,subSectionKey);
				}
			} else if (Eag2012.HOLDINGS.equalsIgnoreCase(sectionKey)) {
				Map<String, List<String>> subSectionsMap = sectionsMap.get(sectionKey);
				Iterator<String> subSectionsIt = subSectionsMap.keySet().iterator();
				while (subSectionsIt.hasNext()) {
					String subSectionKey = subSectionsIt.next();
					if (Eag2012.HOLDING_EXTENT.equalsIgnoreCase(subSectionKey)) {
						// eag/archguide/desc/repositories/repository/holdings/extent
						List<String> valueList = subSectionsMap.get(subSectionKey);
						for (int j = 0; j < valueList.size(); j++) {
							if (valueList.get(j) == null || valueList.get(j).isEmpty()) {
								break;
							}
							if (repository.getHoldings() == null) {
								repository.setHoldings(new Holdings());
							}
							Num num = new Num();
							num.setContent(valueList.get(j));
							num.setUnit(Eag2012.UNIT_LINEAR_METRE);
							Extent extent = new Extent();
							extent.setNum(num);
							repository.getHoldings().setExtent(extent);
						}
					}
				}
			}
		}
		this.log.debug("End method: \"parseTabDescription\"");
	}

	/**
	 * Method to fill buildinginfo element 
	 * @param subSectionsMap {@link Map<String, List<String>>} the subSectionsMap
	 * @param subSectionsIt {@link Iterator<String>} the subSectionsIt
	 * @param subSectionKey {@link String} the subSectionKey
	 */
	private void createBuildinginfo(Map<String, List<String>> subSectionsMap,Iterator<String> subSectionsIt,String subSectionKey){
		this.log.debug("Method start: \"createBuildinginfo\"");
		// eag/archguide/desc/repositories/repository/buildinginfo/repositorarea
		if (Eag2012.BUILDING_AREA.equalsIgnoreCase(subSectionKey)) {
			List<String> valueList = subSectionsMap.get(subSectionKey);
			for (int j = 0; j < valueList.size(); j++) {
				if (valueList.get(j) == null || valueList.get(j).isEmpty()) {
					break;
				}
				if (repository.getBuildinginfo() == null) {
					repository.setBuildinginfo(new Buildinginfo());
				}
				Num num = new Num();
				num.setContent(valueList.get(j));
				num.setUnit(Eag2012.UNIT_SQUARE_METRE);
				Repositorarea repositorarea = new Repositorarea();
				repositorarea.setNum(num);
				repository.getBuildinginfo().setRepositorarea(repositorarea);
			}
		} else if (Eag2012.BUILDING_LENGTH.equalsIgnoreCase(subSectionKey)) {
			// eag/archguide/desc/repositories/repository/buildinginfo/lengthshelf
			List<String> valueList = subSectionsMap.get(subSectionKey);
			for (int j = 0; j < valueList.size(); j++) {
				if (valueList.get(j) == null || valueList.get(j).isEmpty()) {
					break;
				}
				if (repository.getBuildinginfo() == null) {
					repository.setBuildinginfo(new Buildinginfo());
				}
				Num num = new Num();
				num.setContent(valueList.get(j));
				num.setUnit(Eag2012.UNIT_LINEAR_METRE);
				Lengthshelf lengthshelf = new Lengthshelf();
				lengthshelf.setNum(num);
				repository.getBuildinginfo().setLengthshelf(lengthshelf);
			}
		}
		this.log.debug("End method: \"createBuildinginfo\"");
	}

	/**
	 * Method to fill library element 
	 */
	private void createLibrary(){
		this.log.debug("Method start: \"createLibrary\"");
		// eag/archguide/desc/repositories/repository/services/library
		if (this.eag2012.getNumValue() != null) {
			for (int i = 0; i < this.eag2012.getNumValue().size(); i++) {
				Map<String, Map<String, Map<String, List<String>>>> tabsMap = this.eag2012.getNumValue().get(i);
				// Repository.
				repository = this.eag.getArchguide().getDesc().getRepositories().getRepository().get(i);
				Iterator<String> tabsIt = tabsMap.keySet().iterator();
				while (tabsIt.hasNext()) {
					String tabKey = tabsIt.next();
					if (Eag2012.TAB_ACCESS_AND_SERVICES.equalsIgnoreCase(tabKey)) {
						Map<String, Map<String, List<String>>> sectionsMap = tabsMap.get(tabKey);
						Iterator<String> sectionsIt = sectionsMap.keySet().iterator();
						while (sectionsIt.hasNext()) {
							String sectionKey = sectionsIt.next();
							if (Eag2012.LIBRARY.equalsIgnoreCase(sectionKey)) {
								Map<String, List<String>> subSectionsMap = sectionsMap.get(sectionKey);
								Iterator<String> subSectionsIt = subSectionsMap.keySet().iterator();
								while (subSectionsIt.hasNext()) {
									String subSectionKey = subSectionsIt.next();
									// eag/archguide/desc/repositories/repository/services/library/monographicpub
									createMonographicpub( subSectionKey, subSectionsMap,repository);
									// eag/archguide/desc/repositories/repository/services/library/serialpub
									createSerialpub( subSectionKey, subSectionsMap,repository);									
								}
							}
						}
					}
				}
			}
		}
		this.log.debug("End method: \"createLibrary\"");
	}

	/**
	 * Method to fill serialpub element 
	 * @param subSectionKey {@link String}
	 * @param subSectionsMap {@link Map<String, List<String>>}
	 * @param repository {@link Repository} the repository
	 */
	private void createSerialpub(String subSectionKey,Map<String, List<String>> subSectionsMap,Repository repository){
		this.log.debug("Method start: \"createSerialpub\"");
		if (Eag2012.SERIAL_PUBLICATION.equalsIgnoreCase(subSectionKey)) {
			List<String> valueList = subSectionsMap.get(subSectionKey);
			for (int j = 0; j < valueList.size(); j++) {
				if (valueList.get(j) == null || valueList.get(j).isEmpty()) {
					break;
				}
				repository = createRepositoryLibrary( repository, false);
				Num num = new Num();
				num.setContent(valueList.get(j));
				num.setUnit(Eag2012.UNIT_TITLE);
				Serialpub serialpub = new Serialpub();
				serialpub.setNum(num);
				repository.getServices().getLibrary().setSerialpub(serialpub);
			}
		}
		this.log.debug("End method: \"createSerialpub\"");
	}

	/**
	 * Method to fill monographicpub element 
	 * @param subSectionKey {@link String}
	 * @param subSectionsMap {@link Map<String, List<String>>}
	 * @param repository {@link Repository} the repository
	 */
	private void createMonographicpub(String subSectionKey,Map<String, List<String>> subSectionsMap,Repository repository){
		this.log.debug("Method start: \"createMonographicpub\"");
		// eag/archguide/desc/repositories/repository/services/library/monographicpub
		if (Eag2012.MONOGRAPHIC_PUBLICATION.equalsIgnoreCase(subSectionKey)) {
			List<String> valueList = subSectionsMap.get(subSectionKey);
			for (int j = 0; j < valueList.size(); j++) {
				if (valueList.get(j) == null || valueList.get(j).isEmpty()) {
					break;
				}
				repository = createRepositoryLibrary( repository,false);
				Num num = new Num();
				num.setContent(valueList.get(j));
				num.setUnit(Eag2012.UNIT_BOOK);
				Monographicpub monographicpub = new Monographicpub();
				monographicpub.setNum(num);
				repository.getServices().getLibrary().setMonographicpub(monographicpub);
			}
		}
		this.log.debug("End method: \"createMonographicpub\"");
	}

	/**
	 * Method to fill question "Desc" element 
	 */
	private void createQuestion(){	
		this.log.debug("Method start: \"createQuestion\"");
		// eag/archguide/desc/repositories/repository/services/library/question
		if (this.eag2012.getLibraryQuestion() != null) {
			for (int i = 0; i < this.eag2012.getLibraryQuestion().size(); i++) {					
				if(!this.eag2012.getLibraryQuestion().get(i).equalsIgnoreCase(Eag2012.OPTION_NONE)){
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
		}
		this.log.debug("End method: \"createQuestion\"");
	}

	/**
	 * Method to fill internetAccess element 
	 */
	private void createInternetAccess(){
		this.log.debug("Method start: \"createInternetAccess\"");
		// eag/archguide/desc/repositories/repository/services/internetAccess
		if (this.eag2012.getInternetAccessQuestion() != null) {
			for (int i = 0; i < this.eag2012.getInternetAccessQuestion().size(); i++) {
				// Repository
				Repository repository = this.eag.getArchguide().getDesc().getRepositories().getRepository().get(i);
				// Normal case.
				if(!this.eag2012.getInternetAccessQuestion().get(i).equalsIgnoreCase(Eag2012.OPTION_NONE)){
					if (repository.getServices() == null) {
						repository.setServices(new Services());
					}
					if (repository.getServices().getInternetAccess() == null) {
						repository.getServices().setInternetAccess(new InternetAccess());
					}
					repository.getServices().getInternetAccess().setQuestion(this.eag2012.getInternetAccessQuestion().get(i));
					this.eag2012 = getDescriptiveNoteServices(repository.getServices().getInternetAccess(), -1, i,this.eag2012);
				} else {
					// Checks if question is not selected but added description.
					InternetAccess internetAccess = new InternetAccess();
					this.eag2012 = getDescriptiveNoteServices(internetAccess, -1, i,this.eag2012);
					if (internetAccess != null
							&& internetAccess.getDescriptiveNote() != null
							&& !internetAccess.getDescriptiveNote().getP().isEmpty()) {
						if (repository.getServices() == null) {
							repository.setServices(new Services());
						}
						repository.getServices().setInternetAccess(internetAccess);
					}
				} 
			}
		}
		this.log.debug("End method: \"createInternetAccess\"");
	}

	/**
	 * Method to fill restorationlab element 
	 */
	private void createTechservicesRestorationlab(){
		this.log.debug("Method start: \"createTechservicesRestorationlab\"");
		// eag/archguide/desc/repositories/repository/services/techservices/restorationlab
		if (this.eag2012.getRestorationlabQuestion() != null) {
			for (int i = 0; i < this.eag2012.getRestorationlabQuestion().size(); i++) {
				// Repository
				 repository = this.eag.getArchguide().getDesc().getRepositories().getRepository().get(i);
				// Normal case.
				if(!this.eag2012.getRestorationlabQuestion().get(i).equalsIgnoreCase(Eag2012.OPTION_NONE)){
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
					this.eag2012 = getDescriptiveNoteServices(repository.getServices().getTechservices().getRestorationlab(), -1, i,this.eag2012);
				} else {
					// Checks if question is not selected but added description.
					Restorationlab restorationlab = new Restorationlab();
					this.eag2012 = getDescriptiveNoteServices(restorationlab, -1, i,this.eag2012);
					if (restorationlab != null
							&& restorationlab.getDescriptiveNote() != null
							&& !restorationlab.getDescriptiveNote().getP().isEmpty()) {
						if (repository.getServices() == null) {
							repository.setServices(new Services());
						}
						if (repository.getServices().getTechservices() == null) {
							repository.getServices().setTechservices(new Techservices());
						}
						if (repository.getServices().getTechservices().getRestorationlab() != null) {
							// Add contact.
							if (repository.getServices().getTechservices().getRestorationlab().getContact() != null) {
								restorationlab.setContact(repository.getServices().getTechservices().getRestorationlab().getContact());
							}
							// Add webpage.
							if (!repository.getServices().getTechservices().getRestorationlab().getWebpage().isEmpty()) {
								for (int j = 0; j < repository.getServices().getTechservices().getRestorationlab().getWebpage().size(); j++) {
									if (repository.getServices().getTechservices().getRestorationlab().getWebpage().get(j) != null) {
										restorationlab.getWebpage().add(repository.getServices().getTechservices().getRestorationlab().getWebpage().get(j));
									}
								}
							}
						}
						repository.getServices().getTechservices().setRestorationlab(restorationlab);
					}
				}
			}
		}
		this.log.debug("End method: \"createTechservicesRestorationlab\"");
	}

	/**
	 * Method to fill reproductionser element 
	 */
		private void createTechservicesReproductionser(){
			this.log.debug("Method start: \"createTechservicesReproductionser\"");
		// eag/archguide/desc/repositories/repository/services/techservices/reproductionser
		if (this.eag2012.getReproductionserQuestion() != null) {
			for (int i = 0; i < this.eag2012.getReproductionserQuestion().size(); i++) {
				// Repository
				 repository = this.eag.getArchguide().getDesc().getRepositories().getRepository().get(i);

				// Normal case.
				if (this.eag2012.getReproductionserQuestion().get(i) != null
						&& !this.eag2012.getReproductionserQuestion().get(i).equalsIgnoreCase(Eag2012.OPTION_NONE)
						/*&& (!this.eag2012.getMicroformserQuestion().get(i).equalsIgnoreCase(Eag2012.OPTION_NONE)
						|| !this.eag2012.getPhotographserQuestion().get(i).equalsIgnoreCase(Eag2012.OPTION_NONE)
						|| !this.eag2012.getDigitalserQuestion().get(i).equalsIgnoreCase(Eag2012.OPTION_NONE)
						|| !this.eag2012.getPhotocopyserQuestion().get(i).equalsIgnoreCase(Eag2012.OPTION_NONE))*/){
					/*if (repository.getServices() == null) {
						repository.setServices(new Services());
					}
					if (repository.getServices().getTechservices() == null) {
						repository.getServices().setTechservices(new Techservices());
					}
					if (repository.getServices().getTechservices().getReproductionser() == null) {
						repository.getServices().getTechservices().setReproductionser(new Reproductionser());
					}*/
					repository = createRepositoryTechservicesReproductionser(repository,false);
					fillReproductionserNormalCase( i);
					
				} else {
					// Checks if question is not selected but added description.
					Reproductionser reproductionser = new Reproductionser();
					this.eag2012 = getDescriptiveNoteServices(reproductionser, -1, i,this.eag2012);

					if (reproductionser != null
							&& reproductionser.getDescriptiveNote() != null
							&& !reproductionser.getDescriptiveNote().getP().isEmpty()) {
						if (repository.getServices() == null) {
							repository.setServices(new Services());
						}
						if (repository.getServices().getTechservices() == null) {
							repository.getServices().setTechservices(new Techservices());
						}
						if (repository.getServices().getTechservices().getReproductionser() != null) {
							getAddReproductionser(i);							
						}

						repository.getServices().getTechservices().setReproductionser(reproductionser);
					} else if (!this.eag2012.getMicroformserQuestion().get(i).equalsIgnoreCase(Eag2012.OPTION_NONE)
						|| !this.eag2012.getPhotographserQuestion().get(i).equalsIgnoreCase(Eag2012.OPTION_NONE)
						|| !this.eag2012.getDigitalserQuestion().get(i).equalsIgnoreCase(Eag2012.OPTION_NONE)
						|| !this.eag2012.getPhotocopyserQuestion().get(i).equalsIgnoreCase(Eag2012.OPTION_NONE)) {
						// Check if question is not selected but microformser, photographser, digitalser or photocopyser is selected.
						repository = createRepositoryTechservicesReproductionser(repository,false);
						// eag/archguide/desc/repositories/repository/services/techservices/reproductionser/microformser
						fillReproductionser(i);					
					}
				}
			}
		}
		this.log.debug("End method: \"createTechservicesReproductionser\"");		
	}

	/**
	 * Method to fill reproductionser element in normal case
	 * @param i {@link int} 
	 */
	private void fillReproductionserNormalCase(int i){
		this.log.debug("Method start: \"fillReproductionserNormalCase\"");
		// eag/archguide/desc/repositories/repository/services/techservices/reproductionser/question
		if(!this.eag2012.getReproductionserQuestion().get(i).equalsIgnoreCase(Eag2012.OPTION_NONE)){
			repository.getServices().getTechservices().getReproductionser().setQuestion(this.eag2012.getReproductionserQuestion().get(i));
		}
		
		// eag/archguide/desc/repositories/repository/services/techservices/reproductionser/descriptiveNote
		this.eag2012 = getDescriptiveNoteServices(repository.getServices().getTechservices().getReproductionser(), -1, i,this.eag2012);

		// eag/archguide/desc/repositories/repository/services/techservices/reproductionser/microformser
		if (this.eag2012.getMicroformserQuestion() != null && !this.eag2012.getMicroformserQuestion().isEmpty() && !this.eag2012.getMicroformserQuestion().get(i).equalsIgnoreCase(Eag2012.OPTION_NONE)) {
			if (repository.getServices().getTechservices().getReproductionser().getMicroformser() == null) {
				repository.getServices().getTechservices().getReproductionser().setMicroformser(new Microformser());
			}
			repository.getServices().getTechservices().getReproductionser().getMicroformser().setQuestion(this.eag2012.getMicroformserQuestion().get(i));
		}

		// eag/archguide/desc/repositories/repository/services/techservices/reproductionser/photographser
		if (this.eag2012.getPhotographserQuestion() != null && !this.eag2012.getPhotographserQuestion().isEmpty() && !this.eag2012.getPhotographserQuestion().get(i).equalsIgnoreCase(Eag2012.OPTION_NONE)) {
			if (repository.getServices().getTechservices().getReproductionser().getPhotographser() == null) {
				repository.getServices().getTechservices().getReproductionser().setPhotographser(new Photographser());
			}
			repository.getServices().getTechservices().getReproductionser().getPhotographser().setQuestion(this.eag2012.getPhotographserQuestion().get(i));
		}

		// eag/archguide/desc/repositories/repository/services/techservices/reproductionser/digitalser
		if (this.eag2012.getDigitalserQuestion() != null && !this.eag2012.getDigitalserQuestion().isEmpty() && !this.eag2012.getDigitalserQuestion().get(i).equalsIgnoreCase(Eag2012.OPTION_NONE)) {
			if (repository.getServices().getTechservices().getReproductionser().getDigitalser() == null) {
				repository.getServices().getTechservices().getReproductionser().setDigitalser(new Digitalser());
			}
			repository.getServices().getTechservices().getReproductionser().getDigitalser().setQuestion(this.eag2012.getDigitalserQuestion().get(i));
		}

		// eag/archguide/desc/repositories/repository/services/techservices/reproductionser/photocopyser
		if (this.eag2012.getPhotocopyserQuestion() != null && !this.eag2012.getPhotocopyserQuestion().isEmpty() && !this.eag2012.getPhotocopyserQuestion().get(i).equalsIgnoreCase(Eag2012.OPTION_NONE)) {
			if(repository.getServices().getTechservices().getReproductionser()==null){
				repository.getServices().getTechservices().setReproductionser(new Reproductionser());
			}
			if (repository.getServices().getTechservices().getReproductionser().getPhotocopyser() == null) {
				repository.getServices().getTechservices().getReproductionser().setPhotocopyser(new Photocopyser());
			}
			repository.getServices().getTechservices().getReproductionser().getPhotocopyser().setQuestion(this.eag2012.getPhotocopyserQuestion().get(i));
		}
		this.log.debug("End method: \"fillReproductionserNormalCase\"");
	}

	/**
	 * Method to fill reproductionser element /reproductionser/question, /reproductionser/descriptiveNote, /reproductionser/microformser, /reproductionser/photographser and /reproductionser/digitalser second case
	 * @param i {@link int} 
	 */
	private void fillReproductionser(int i){
		this.log.debug("Method start: \"GetEmailJAXB\"");
		// eag/archguide/desc/repositories/repository/services/techservices/reproductionser/microformser
		if (this.eag2012.getMicroformserQuestion() != null && !this.eag2012.getMicroformserQuestion().isEmpty() && !this.eag2012.getMicroformserQuestion().get(i).equalsIgnoreCase(Eag2012.OPTION_NONE)) {
			if (repository.getServices().getTechservices().getReproductionser().getMicroformser() == null) {
				repository.getServices().getTechservices().getReproductionser().setMicroformser(new Microformser());
			}
			repository.getServices().getTechservices().getReproductionser().getMicroformser().setQuestion(this.eag2012.getMicroformserQuestion().get(i));
		}
		// eag/archguide/desc/repositories/repository/services/techservices/reproductionser/photographser
		if (this.eag2012.getPhotographserQuestion() != null && !this.eag2012.getPhotographserQuestion().isEmpty() && !this.eag2012.getPhotographserQuestion().get(i).equalsIgnoreCase(Eag2012.OPTION_NONE)) {
			if (repository.getServices().getTechservices().getReproductionser().getPhotographser() == null) {
				repository.getServices().getTechservices().getReproductionser().setPhotographser(new Photographser());
			}
			repository.getServices().getTechservices().getReproductionser().getPhotographser().setQuestion(this.eag2012.getPhotographserQuestion().get(i));
		}
		// eag/archguide/desc/repositories/repository/services/techservices/reproductionser/digitalser
		if (this.eag2012.getDigitalserQuestion() != null && !this.eag2012.getDigitalserQuestion().isEmpty() && !this.eag2012.getDigitalserQuestion().get(i).equalsIgnoreCase(Eag2012.OPTION_NONE)) {
			if (repository.getServices().getTechservices().getReproductionser().getDigitalser() == null) {
				repository.getServices().getTechservices().getReproductionser().setDigitalser(new Digitalser());
			}
			repository.getServices().getTechservices().getReproductionser().getDigitalser().setQuestion(this.eag2012.getDigitalserQuestion().get(i));
		}
		// eag/archguide/desc/repositories/repository/services/techservices/reproductionser/photocopyser
		if (this.eag2012.getPhotocopyserQuestion() != null && !this.eag2012.getPhotocopyserQuestion().isEmpty() && !this.eag2012.getPhotocopyserQuestion().get(i).equalsIgnoreCase(Eag2012.OPTION_NONE)) {
			if(repository.getServices().getTechservices().getReproductionser()==null){
				repository.getServices().getTechservices().setReproductionser(new Reproductionser());
			}
			if (repository.getServices().getTechservices().getReproductionser().getPhotocopyser() == null) {
				repository.getServices().getTechservices().getReproductionser().setPhotocopyser(new Photocopyser());
			}
			repository.getServices().getTechservices().getReproductionser().getPhotocopyser().setQuestion(this.eag2012.getPhotocopyserQuestion().get(i));
		}
		this.log.debug("End method: \"fillReproductionser\"");		
	}

	/**
	 * Method to add contact, webpage, microformser, photographser, digitalser and photocopyser
	 * @param i {@link int} the i
	 */
	private void getAddReproductionser( int i){
		this.log.debug("Method start: \"getAddReproductionser\"");
		// Add contact.
		if (repository.getServices().getTechservices().getReproductionser().getContact() != null) {
			reproductionser.setContact(repository.getServices().getTechservices().getReproductionser().getContact());
		}
		// Add webpage.
		if (!repository.getServices().getTechservices().getReproductionser().getWebpage().isEmpty()) {
			for (int j = 0; j < repository.getServices().getTechservices().getReproductionser().getWebpage().size(); j++) {
				if (repository.getServices().getTechservices().getReproductionser().getWebpage().get(j) != null) {
					reproductionser.getWebpage().add(repository.getServices().getTechservices().getReproductionser().getWebpage().get(j));
				}
			}
		}
		// Add microformser.
		if (repository.getServices().getTechservices().getReproductionser().getMicroformser() != null) {
			reproductionser.setMicroformser(repository.getServices().getTechservices().getReproductionser().getMicroformser());
		} else if (this.eag2012.getMicroformserQuestion() != null && !this.eag2012.getMicroformserQuestion().isEmpty() && !this.eag2012.getMicroformserQuestion().get(i).equalsIgnoreCase(Eag2012.OPTION_NONE)) {
			if (reproductionser.getMicroformser() == null) {
				reproductionser.setMicroformser(new Microformser());
			}
			reproductionser.getMicroformser().setQuestion(this.eag2012.getMicroformserQuestion().get(i));
		}
		// Add photographser.
		if (repository.getServices().getTechservices().getReproductionser().getPhotographser() != null) {
			reproductionser.setPhotographser(repository.getServices().getTechservices().getReproductionser().getPhotographser());
		} else if (this.eag2012.getPhotographserQuestion() != null && !this.eag2012.getPhotographserQuestion().isEmpty() && !this.eag2012.getPhotographserQuestion().get(i).equalsIgnoreCase(Eag2012.OPTION_NONE)) {
			if (reproductionser.getPhotographser() == null) {
				reproductionser.setPhotographser(new Photographser());
			}
			reproductionser.getPhotographser().setQuestion(this.eag2012.getPhotographserQuestion().get(i));
		}
		// Add digitalser.
		if (repository.getServices().getTechservices().getReproductionser().getDigitalser() != null) {
			reproductionser.setDigitalser(repository.getServices().getTechservices().getReproductionser().getDigitalser());
		} else if (this.eag2012.getDigitalserQuestion() != null && !this.eag2012.getDigitalserQuestion().isEmpty() && !this.eag2012.getDigitalserQuestion().get(i).equalsIgnoreCase(Eag2012.OPTION_NONE)) {
			if (reproductionser.getDigitalser() == null) {
				reproductionser.setDigitalser(new Digitalser());
			}
			reproductionser.getDigitalser().setQuestion(this.eag2012.getDigitalserQuestion().get(i));
		}
		// Add photocopyser.
		if (repository.getServices().getTechservices().getReproductionser().getPhotocopyser() != null) {
			reproductionser.setPhotocopyser(repository.getServices().getTechservices().getReproductionser().getPhotocopyser());
		} else if (this.eag2012.getPhotocopyserQuestion() != null && !this.eag2012.getPhotocopyserQuestion().isEmpty() && !this.eag2012.getPhotocopyserQuestion().get(i).equalsIgnoreCase(Eag2012.OPTION_NONE)) {
			if(repository.getServices().getTechservices().getReproductionser()==null){
				repository.getServices().getTechservices().setReproductionser(new Reproductionser());
			}
			if (reproductionser.getPhotocopyser() == null) {
				reproductionser.setPhotocopyser(new Photocopyser());
			}
			reproductionser.getPhotocopyser().setQuestion(this.eag2012.getPhotocopyserQuestion().get(i));
		}
		this.log.debug("End method: \"getAddReproductionser\"");		
	}	
}
