package eu.apenet.dashboard.manual.eag.utils.createObjectJAXB;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import eu.apenet.dashboard.manual.eag.Eag2012;
import eu.apenet.dpt.utils.eag2012.ComputerPlaces;
import eu.apenet.dpt.utils.eag2012.Contact;
import eu.apenet.dpt.utils.eag2012.DescriptiveNote;
import eu.apenet.dpt.utils.eag2012.Email;
import eu.apenet.dpt.utils.eag2012.Exhibition;
import eu.apenet.dpt.utils.eag2012.InternetAccess;
import eu.apenet.dpt.utils.eag2012.Library;
import eu.apenet.dpt.utils.eag2012.OtherServices;
import eu.apenet.dpt.utils.eag2012.P;
import eu.apenet.dpt.utils.eag2012.Repository;
import eu.apenet.dpt.utils.eag2012.Reproductionser;
import eu.apenet.dpt.utils.eag2012.Restorationlab;
import eu.apenet.dpt.utils.eag2012.Searchroom;
import eu.apenet.dpt.utils.eag2012.Services;
import eu.apenet.dpt.utils.eag2012.Techservices;
import eu.apenet.dpt.utils.eag2012.ToursSessions;
import eu.apenet.dpt.utils.eag2012.Webpage;

/**
 * Class abstract for {@link ObjectJAXB}ObjectJAXB
 */
public abstract class AbstractObjectJAXB {	
	/**
	 * Method to check to date
	 * @param formatDate {@link String} of date
	 * @return {@link String} formatDate
	 */
	protected String parseDate(String formatDate) {
		boolean pattern1 = Pattern.matches("\\d{4}", formatDate); //yyyy
		boolean pattern2 = Pattern.matches("\\d{4}[\\-\\./:\\s]\\d{2}", formatDate); //yyyy-MM
		boolean pattern3 = Pattern.matches("\\d{4}[\\-\\./:\\s]\\d{2}[\\-\\./:\\s]\\d{2}", formatDate); //yyyy-MM-dd
		boolean pattern4 = Pattern.matches("\\d{2}[\\-\\./:\\s]\\d{2}[\\-\\./:\\s]\\d{4}", formatDate); //dd-MM-yyyy
		if (pattern4){
			String yearStandardDate = formatDate.substring(6);
			String monthStandardDate = formatDate.substring(2,6);
			String dateStandardDate = formatDate.substring(0,2);
			String reverseString =yearStandardDate+monthStandardDate+dateStandardDate;
	         formatDate = formatDate.replaceAll(formatDate, reverseString);
		}
		if (pattern1){
			return formatDate;
		} else if (pattern2) {
			String monthStandardDate = formatDate.substring(5,7);
			if (Integer.parseInt(monthStandardDate) <= 12) {
				formatDate = formatDate.replaceAll("[\\./:\\s]", "-");
				return formatDate;
			}
		} else if (pattern3 || pattern4) {
			formatDate = formatDate.replaceAll("[\\./:\\s]", "-");
			return formatDate;
		}
		return null;
	}

	/**
	 * Method to fill of searchroom
	 * @param repository {@link Repository} Repository
	 * @param isContact {@link boolean} for set contact the repository object
	 * @return Repository {@link Repository} 
	 */
	protected Repository createRepositorySearchroom(Repository repository,boolean isContact){		
		if (repository.getServices() == null) {
			repository.setServices(new Services());
		}
		if (repository.getServices().getSearchroom() == null) {
			repository.getServices().setSearchroom(new Searchroom());		
		}
		if(isContact){
			if (repository.getServices().getSearchroom().getContact() == null) {
				repository.getServices().getSearchroom().setContact(new Contact());
			}
		}
		return repository;		
	}

	/**
	 * Method to fill of library
	 * @param repository {@link Repository} Repository
	 * @param isContact boolean for set contact the repository object
	 * @return {@link Repository} Repository
	 */
	protected Repository createRepositoryLibrary(Repository repository,boolean isContact){
		if (repository.getServices() == null) {
			repository.setServices(new Services());
		}
		if (repository.getServices().getLibrary() == null) {
			repository.getServices().setLibrary(new Library());
		}
		if(isContact){
			if (repository.getServices().getLibrary().getContact() == null) {
				repository.getServices().getLibrary().setContact(new Contact());
			}
		}
		return repository;
	}

	/**
	 *  Method to fill of techservices and restoration lab
	 * @param repository {@link Repository} Repository
	 * @param isContact {@link boolean} for set contact the repository object
	 * @return {@link Repository} Repository
	 */	
	protected Repository createRepositoryTechservicesRestorationlab(Repository repository,boolean isContact){		
		if (repository.getServices() == null) {
			repository.setServices(new Services());
		}
		if (repository.getServices().getTechservices() == null) {
			repository.getServices().setTechservices(new Techservices());
		}
		
		if (repository.getServices().getTechservices().getRestorationlab() == null) {
			repository.getServices().getTechservices().setRestorationlab(new Restorationlab());
		}
		if(isContact){
			if (repository.getServices().getTechservices().getRestorationlab().getContact() == null) {
				repository.getServices().getTechservices().getRestorationlab().setContact(new Contact());
			}
		}
		return repository;
	}

	/**
	 * Method to fill of techservices and reproductionser
	 * @param repository {@link Repository} Repository
	 * @param isContact {@link boolean} for set contact the repository object
	 * @return {@link Repository} Repository
	 */
	protected Repository createRepositoryTechservicesReproductionser(Repository repository,boolean isContact){		
		if (repository.getServices() == null) {
			repository.setServices(new Services());
		}
		if (repository.getServices().getTechservices() == null) {
			repository.getServices().setTechservices(new Techservices());
		}
		if (repository.getServices().getTechservices().getReproductionser() == null) {
			repository.getServices().getTechservices().setReproductionser(new Reproductionser());
		}
		if(isContact){
			if (repository.getServices().getTechservices().getReproductionser().getContact() == null) {
				repository.getServices().getTechservices().getReproductionser().setContact(new Contact());
			}
		}
		return repository;
	}

	/**
	 * Method to fill "Desc" element  searchroom, library , restorationlab and reproductionser
	 * @param repository {@link Repository} Repository
	 * @param isEmail to see if you email or not
	 * @param sectionValueKey String  sectionValueKey
	 * @param sectionHrefKey String sectionHrefKey
	 * @param email {@link Email} Email
	 * @param webpage object Webpage
	 * @return object Repository
	 */
	protected Repository createServices(Repository repository,boolean isEmail,String sectionValueKey,String sectionHrefKey,Email email,Webpage webpage ){
		// eag/archguide/desc/repositories/repository/services/searchroom
		if (Eag2012.SEARCHROOM.equalsIgnoreCase(sectionValueKey) && 
				(Eag2012.SEARCHROOM.equalsIgnoreCase(sectionHrefKey) || sectionHrefKey==null && !isEmail )) {
			if(isEmail){
				repository = createRepositorySearchroom(repository,true);	
				repository.getServices().getSearchroom().getContact().getEmail().add(email);
			}else{
				repository = createRepositorySearchroom(repository,false);
				repository.getServices().getSearchroom().getWebpage().add(webpage);
			}
		}// eag/archguide/desc/repositories/repository/services/library
		
		if (Eag2012.LIBRARY.equalsIgnoreCase(sectionValueKey) && 
				(Eag2012.LIBRARY.equalsIgnoreCase(sectionHrefKey) || sectionHrefKey==null && !isEmail )) {
			if(isEmail){
				repository = createRepositoryLibrary(repository,true);
				repository.getServices().getLibrary().getContact().getEmail().add(email);
			}else{
				repository = createRepositoryLibrary(repository,false);
				repository.getServices().getLibrary().getWebpage().add(webpage);
			}
		}
		// eag/archguide/desc/repositories/repository/services/techservices/restorationlab
		if (Eag2012.RESTORATION_LAB.equalsIgnoreCase(sectionValueKey) && 
				(Eag2012.RESTORATION_LAB.equalsIgnoreCase(sectionHrefKey) || sectionHrefKey==null&& !isEmail )) {
			if(isEmail){
				repository = createRepositoryTechservicesRestorationlab(repository,true);
	
				repository.getServices().getTechservices().getRestorationlab().getContact().getEmail().add(email);
			}else{
				repository = createRepositoryTechservicesRestorationlab(repository,false);
				repository.getServices().getTechservices().getRestorationlab().getWebpage().add(webpage);
			}
		}
		// eag/archguide/desc/repositories/repository/services/techservices/reproductionser
		if (Eag2012.REPRODUCTIONSER.equalsIgnoreCase(sectionValueKey) && 
				(Eag2012.REPRODUCTIONSER.equalsIgnoreCase(sectionHrefKey) || sectionHrefKey==null&& !isEmail )) {
			if(isEmail){
				repository = createRepositoryTechservicesReproductionser(repository,true);
	
				repository.getServices().getTechservices().getReproductionser().getContact().getEmail().add(email);
			}else{
				repository = createRepositoryTechservicesReproductionser(repository,false);
				repository.getServices().getTechservices().getReproductionser().getWebpage().add(webpage);
			}
		}
		return repository;		
	}

	/**
	 * Method to recover descriptiveNote.
	 * @param object {@link Object} the object
	 * @param index {@link int} the index
	 * @param repoIndex {@link int} the repoIndex
	 * @param eag2012 {@link Eag2012} the eag2012
	 * @return eag2012 {@link Eag2012}
	 */
	protected Eag2012 getDescriptiveNoteServices(final Object object, final int index, final int repoIndex,Eag2012 eag2012) {
		if (eag2012.getDescriptiveNotePValue() != null
				&& eag2012.getDescriptiveNotePLang() != null) {
	//		for (int i = 0; i < this.eag2012.getDescriptiveNotePValue().size(); i++) {
			if (eag2012.getDescriptiveNotePValue().get(repoIndex) != null
					&& eag2012.getDescriptiveNotePLang().get(repoIndex) != null) {
				Map<String, Map<String, List<String>>> tabsValueMap = eag2012.getDescriptiveNotePValue().get(repoIndex);
				Map<String, Map<String, List<String>>> tabsLangMap = eag2012.getDescriptiveNotePLang().get(repoIndex);
				Iterator<String> tabsValueIt = tabsValueMap.keySet().iterator();
				Iterator<String> tabsLangIt = tabsLangMap.keySet().iterator();
				while (tabsValueIt.hasNext()) {
					String tabValueKey = tabsValueIt.next();
					String tabLangKey = tabsLangIt.next();
	
					if (Eag2012.TAB_ACCESS_AND_SERVICES.equalsIgnoreCase(tabValueKey)
							&& Eag2012.TAB_ACCESS_AND_SERVICES.equalsIgnoreCase(tabLangKey)) {
						Map<String, List<String>> sectionsValueMap = tabsValueMap.get(tabValueKey);
						Map<String, List<String>> sectionsLangMap = tabsLangMap.get(tabLangKey);
						Iterator<String> sectionsValueIt = sectionsValueMap.keySet().iterator();
						Iterator<String> sectionsLangIt = sectionsLangMap.keySet().iterator();
						while (sectionsValueIt.hasNext()) {
							String sectionValueKey = sectionsValueIt.next();
							String sectionLangKey = sectionsLangIt.next();
							List<String> valuesList = sectionsValueMap.get(sectionValueKey);
							List<String> langList = sectionsLangMap.get(sectionLangKey);
	//						for (int j = 0; j < valuesList.size(); j++) {
								if (index>-1 && valuesList!=null && valuesList.size() > index && !(valuesList.get(index) == null || valuesList.get(index).isEmpty())) {
									createRecreationalServicesOne(sectionValueKey, sectionLangKey, valuesList, langList,  index, object);
									
								}else if(index==-1){
									createRecreationalServicestwo(sectionValueKey, sectionLangKey, valuesList, langList,  index, object);
									
								}
							}
	//					}
					}
				}
			}
		}
		return eag2012;
	}

	/**
	 * Method to fill "Desc" element descrptiveNote
	 * @param sectionValueKey {@link String{ sectionValueKey
	 * @param sectionLangKey {@link String} sectionLangKey
	 * @param valuesList List<String> valuesList
	 * @param langList List<String> langList
	 * @param index {@link int} index
	 * @param object {@link Object} object
	 */
	protected void createRecreationalServicesOne(String sectionValueKey,String sectionLangKey,List<String> valuesList,List<String> langList, int index,Object object){		
		P p = new P();
		p.setContent(valuesList.get(index));
		if (!Eag2012.OPTION_NONE.equalsIgnoreCase(langList.get(index))) {
			p.setLang(langList.get(index));
		}	
		// eag/archguide/desc/repositories/repository/services/recreationalServices/exhibition/descriptiveNote/P
		if (Eag2012.EXHIBITION.equalsIgnoreCase(sectionValueKey)
				&& Eag2012.EXHIBITION.equalsIgnoreCase(sectionLangKey)) {
			if (object instanceof Exhibition) {
				Exhibition exhibition = (Exhibition) object;
				if (exhibition.getDescriptiveNote() == null) {
					exhibition.setDescriptiveNote(new DescriptiveNote());
					exhibition.getDescriptiveNote().getP().add(p);
				}
			}
		}
		// eag/archguide/desc/repositories/repository/services/recreationalServices/toursSessions/descriptiveNote/P
		if (Eag2012.TOURS_SESSIONS.equalsIgnoreCase(sectionValueKey)
				&& Eag2012.TOURS_SESSIONS.equalsIgnoreCase(sectionLangKey)) {
			if (object instanceof ToursSessions) {
				ToursSessions toursSessions = (ToursSessions) object;
				if (toursSessions.getDescriptiveNote() == null) {
					toursSessions.setDescriptiveNote(new DescriptiveNote());
					toursSessions.getDescriptiveNote().getP().add(p);
				}
			}
		}
		// eag/archguide/desc/repositories/repository/services/recreationalServices/otherServices/descriptiveNote/P
		if (Eag2012.OTHER_SERVICES.equalsIgnoreCase(sectionValueKey)
				&& Eag2012.OTHER_SERVICES.equalsIgnoreCase(sectionLangKey)) {
			if (object instanceof OtherServices) {
				OtherServices otherServices = (OtherServices) object;
				if (otherServices.getDescriptiveNote() == null) {
					otherServices.setDescriptiveNote(new DescriptiveNote());
					otherServices.getDescriptiveNote().getP().add(p);
				}
			}
		}		
	}

	/**
	 * Method to fill "Desc" element descrptiveNote
	 * @param sectionValueKey {@link String} sectionValueKey
	 * @param sectionLangKey {@link String} sectionLangKey
	 * @param valuesList List<String> valuesList
	 * @param langList List<String> langList
	 * @param index {@link int} index
	 * @param object  {@link Object} object
	 */
	protected void createRecreationalServicestwo(String sectionValueKey,String sectionLangKey,List<String> valuesList,List<String> langList, int index,Object object){
		for(int x=0;x<valuesList.size();x++){
			P p = new P();
			p.setContent(valuesList.get(x));
			if (!Eag2012.OPTION_NONE.equalsIgnoreCase(langList.get(x))) {
				p.setLang(langList.get(x));
			}
			if (p.getContent() != null && !p.getContent().isEmpty()) {
				// eag/archguide/desc/repositories/repository/services/searchroom/computerPlaces/descriptiveNote/P
				if (Eag2012.SEARCHROOM.equalsIgnoreCase(sectionValueKey)
						&& Eag2012.SEARCHROOM.equalsIgnoreCase(sectionLangKey)) {
					if (object instanceof ComputerPlaces) {
						ComputerPlaces computerPlaces = (ComputerPlaces) object;
						if (computerPlaces.getDescriptiveNote() == null) {
							computerPlaces.setDescriptiveNote(new DescriptiveNote());
						}
						computerPlaces.getDescriptiveNote().getP().add(p);
					}
				}
				// eag/archguide/desc/repositories/repository/services/internetAccess/descriptiveNote/P
				if (Eag2012.INTERNET_ACCESS.equalsIgnoreCase(sectionValueKey)
						&& Eag2012.INTERNET_ACCESS.equalsIgnoreCase(sectionLangKey)) {
					if (object instanceof InternetAccess) {
							InternetAccess internetAccess = (InternetAccess) object;
							if (internetAccess.getDescriptiveNote() == null) {
								internetAccess.setDescriptiveNote(new DescriptiveNote());
							}
							internetAccess.getDescriptiveNote().getP().add(p);
					}
				}
				// eag/archguide/desc/repositories/repository/services/techservices/restorationlab/descriptiveNote/P
				if (Eag2012.RESTORATION_LAB.equalsIgnoreCase(sectionValueKey)
						&& Eag2012.RESTORATION_LAB.equalsIgnoreCase(sectionLangKey)) {
					if (object instanceof Restorationlab) {
						Restorationlab restorationlab = (Restorationlab) object;
						if (restorationlab.getDescriptiveNote() == null) {
							restorationlab.setDescriptiveNote(new DescriptiveNote());
						}
						restorationlab.getDescriptiveNote().getP().add(p);
					}
				}
				// eag/archguide/desc/repositories/repository/services/techservices/reproductionser/descriptiveNote/P
				if (Eag2012.REPRODUCTIONSER.equalsIgnoreCase(sectionValueKey)
						&& Eag2012.REPRODUCTIONSER.equalsIgnoreCase(sectionLangKey)) {
					if (object instanceof Reproductionser) {
						Reproductionser reproductionser = (Reproductionser) object;
						if (reproductionser.getDescriptiveNote() == null) {
							reproductionser.setDescriptiveNote(new DescriptiveNote());
						}
						reproductionser.getDescriptiveNote().getP().add(p);
					}
				}
			}
		}
	}
}
