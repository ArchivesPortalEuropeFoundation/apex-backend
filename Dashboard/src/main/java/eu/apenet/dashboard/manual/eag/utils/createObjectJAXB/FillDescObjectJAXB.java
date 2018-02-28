package eu.apenet.dashboard.manual.eag.utils.createObjectJAXB;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import eu.apenet.dashboard.manual.eag.Eag2012;
import eu.apenet.dpt.utils.eag2012.Accessibility;
import eu.apenet.dpt.utils.eag2012.Adminhierarchy;
import eu.apenet.dpt.utils.eag2012.Adminunit;
import eu.apenet.dpt.utils.eag2012.Building;
import eu.apenet.dpt.utils.eag2012.Buildinginfo;
import eu.apenet.dpt.utils.eag2012.Citation;
import eu.apenet.dpt.utils.eag2012.Closing;
import eu.apenet.dpt.utils.eag2012.ComputerPlaces;
import eu.apenet.dpt.utils.eag2012.DescriptiveNote;
import eu.apenet.dpt.utils.eag2012.Directions;
import eu.apenet.dpt.utils.eag2012.Eag;
import eu.apenet.dpt.utils.eag2012.Exhibition;
import eu.apenet.dpt.utils.eag2012.Fax;
import eu.apenet.dpt.utils.eag2012.Geogarea;
import eu.apenet.dpt.utils.eag2012.Holdings;
import eu.apenet.dpt.utils.eag2012.InternetAccess;
import eu.apenet.dpt.utils.eag2012.Opening;
import eu.apenet.dpt.utils.eag2012.OtherServices;
import eu.apenet.dpt.utils.eag2012.P;
import eu.apenet.dpt.utils.eag2012.RecreationalServices;
import eu.apenet.dpt.utils.eag2012.Refreshment;
import eu.apenet.dpt.utils.eag2012.Repositorfound;
import eu.apenet.dpt.utils.eag2012.Repositorhist;
import eu.apenet.dpt.utils.eag2012.Repositories;
import eu.apenet.dpt.utils.eag2012.Repositorsup;
import eu.apenet.dpt.utils.eag2012.Repository;
import eu.apenet.dpt.utils.eag2012.RepositoryName;
import eu.apenet.dpt.utils.eag2012.RepositoryRole;
import eu.apenet.dpt.utils.eag2012.Reproductionser;
import eu.apenet.dpt.utils.eag2012.ResearchServices;
import eu.apenet.dpt.utils.eag2012.Restorationlab;
import eu.apenet.dpt.utils.eag2012.Rule;
import eu.apenet.dpt.utils.eag2012.Searchroom;
import eu.apenet.dpt.utils.eag2012.Services;
import eu.apenet.dpt.utils.eag2012.Telephone;
import eu.apenet.dpt.utils.eag2012.Timetable;
import eu.apenet.dpt.utils.eag2012.ToursSessions;

/**
 * Class for fill "desc" element of object JAXB
 */
public class FillDescObjectJAXB extends AbstractObjectJAXB implements ObjectJAXB {

    /**
     * eag2012 {@link Eag2012} internal object.
     */
    protected Eag2012 eag2012;

    /**
     * eag {@link Eag} JAXB object.
     */
    protected Eag eag;
    /**
     * mainRepository {@link Repository} object Repository.
     */
    private Repository mainRepository;
    private final Logger log = Logger.getLogger(getClass());

    @Override
    public Eag ObjectJAXB(Eag2012 eag2012, Eag eag) {
        // TODO Auto-generated method stub
        this.eag2012 = eag2012;
        this.eag = eag;

        main();
        return this.eag;
    }

    /**
     * Method main with method for fill "desc" element of object JAXB
     */
    private void main() {
        this.log.debug("Method start: \"Main of class FillDescObjectJAXB\"");
        createRepositorys();

        // eag/archguide/identity/nonpreform/dates &&...
        //getAllDates();
        GetAllDatesObjectJAXB getAllDatesObject = new GetAllDatesObjectJAXB();
        this.eag = getAllDatesObject.GetAllDatesJAXB(this.eag2012, this.eag);
        createRepository();
        // eag/archguide/desc/repositories/repository/repositoryRole
        // Main institution.
        if (mainRepository != null) {
            RepositoryRole repositoryRole = null;
            if (mainRepository.getRepositoryRole() == null) {
                repositoryRole = new RepositoryRole();
            } else {
                repositoryRole = mainRepository.getRepositoryRole();
            }
            repositoryRole.setValue(Eag2012.OPTION_ROLE_HEADQUARTERS_TEXT);
            mainRepository.setRepositoryRole(repositoryRole);
        }
        // Rest of repositories.
        if (this.eag2012.getRepositoryRoleValue() != null) {
            for (int i = 0; i < this.eag2012.getRepositoryRoleValue().size(); i++) {
                Repository otherRepositories = this.eag.getArchguide().getDesc().getRepositories().getRepository().get(i + 1);

                RepositoryRole otherRepositoryRole = null;
                if (otherRepositories.getRepositoryRole() == null) {
                    otherRepositoryRole = new RepositoryRole();
                } else {
                    otherRepositoryRole = otherRepositories.getRepositoryRole();
                }

                if (Eag2012.OPTION_ROLE_BRANCH.equalsIgnoreCase(this.eag2012.getRepositoryRoleValue().get(i))) {
                    otherRepositoryRole.setValue(Eag2012.OPTION_ROLE_BRANCH_TEXT);
                } else if (Eag2012.OPTION_ROLE_HEADQUARTERS.equalsIgnoreCase(this.eag2012.getRepositoryRoleValue().get(i))) {
                    otherRepositoryRole.setValue(Eag2012.OPTION_ROLE_HEADQUARTERS_TEXT);
                } else if (Eag2012.OPTION_ROLE_INTERIM.equalsIgnoreCase(this.eag2012.getRepositoryRoleValue().get(i))) {
                    otherRepositoryRole.setValue(Eag2012.OPTION_ROLE_INTERIM_TEXT);
                }
                otherRepositories.setRepositoryRole(otherRepositoryRole);
            }
        }
        // eag/archguide/desc/repositories/repository/geogarea
        createGeoagarea();
        // eag/archguide/desc/repositories/repository/location
        GetLocationObjectJAXB getLocation = new GetLocationObjectJAXB();
        eag = getLocation.GetLocationJAXB(eag2012, eag);
        // eag/archguide/desc/repositories/repository/telephone
        createTelephone();
        // eag/archguide/desc/repositories/repository/fax
        createFax();
        // eag/archguide/desc/repositories/repository/email (href & lang)
        GetEmailObjectJAXB getEmail = new GetEmailObjectJAXB();
        eag = getEmail.GetEmailJAXB(eag2012, eag);
        // eag/archguide/desc/repositories/repository/webpage
        GetWebpageObjectJAXB getWebpage = new GetWebpageObjectJAXB();
        eag = getWebpage.GetWebpageJAXB(eag2012, eag);

        // eag/archguide/desc/repositories/repository/directions
        createDirections();
        // eag/archguide/desc/repositories/repository/repositorhist/descriptiveNote &&
        // eag/archguide/desc/repositories/repository/buildinginfo/building/descriptiveNote &&
        // eag/archguide/desc/repositories/repository/services/searchroom/researchServices/descriptiveNote &&
        // eag/archguide/desc/repositories/repository/services/recreationalServices/refreshment/descriptiveNote &&
        // eag/archguide/desc/repositories/repository/holdings/descriptiveNote
        createAllDescriptiveNote();
        // eag/archguide/desc/repositories/repository/repositorfound/rule &&  repositorsup/rule
        createAllRules();
        // eag/archguide/desc/repositories/repository/adminhierarchy
        createAdminhierarchy();
        // eag/archguide/desc/repositories/repository/adminhierarchy
        // eag/archguide/desc/repositories/repository/holdings
        createOpening();
        // eag/archguide/desc/repositories/repository/timetable/opening		
        createClosing();
        // eag/archguide/desc/repositories/repository/timetable/closing	
        GetAccessObjectJAXB getAccess = new GetAccessObjectJAXB();
        eag = getAccess.GetAccessJAXB(eag2012, eag);
        // eag/archguide/desc/repositories/repository/accessibility/question
        createAccessibilityQuestion();
        // eag/archguide/desc/repositories/repository/accessibility			
        createAccessibility();
        // eag/archguide/desc/repositories/repository/services/searchroom
        //getAllNums();
        GetServicesObjectJAXB getServices = new GetServicesObjectJAXB();
        eag = getServices.GetServicesJAXB(eag2012, eag);
        this.log.debug("End method: \"Main of class FillDescObjectJAXB\"");
    }

    /**
     * Method for:<br>
     * - fill Repositories element.<br>
     * - fill institution repository element.<br>
     * - create List {@link Repository} of Repository with Repository elements
     * and fill other repositories elements.
     */
    private void createRepositorys() {
        this.log.debug("Method start: \"createRepositorys\"");
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
        this.log.debug("End method: \"createRepositorys\"");
    }

    /**
     * Method for fill {@link Repository} main Repository and fill rest of
     * {@link RepositoryName} repositories.
     */
    private void createRepository() {
        this.log.debug("Method start: \"createRepository\"");
        // eag/archguide/desc/repositories/repository/repositoryName
        // Main institution. 
        /**
         * ISSUE #684 *
         */
        mainRepository = null;
        if (this.eag2012.getRepositoryNameValue() != null && this.eag2012.getRepositoryNameValue().size() > 0) {
            mainRepository = this.eag.getArchguide().getDesc().getRepositories().getRepository().get(0);
            /**
             * Code related to issue #684 related to #694 decisions *
             */
//					List<RepositoryName> repositoryNames = null;
//					if (mainRepository.getRepositoryName() == null) {
//						repositoryNames = new ArrayList<RepositoryName>();
//					} else {
//						repositoryNames = mainRepository.getRepositoryName();
//					}
//					if(this.eag2012.getAutformValue()!=null && this.eag2012.getAutformLang()!=null && this.eag2012.getAutformValue().size()==this.eag2012.getAutformLang().size()){
//						for(int i=0;i<this.eag2012.getAutformValue().size();i++){
//							RepositoryName repositoryName = new RepositoryName();
//							repositoryName.setContent(this.eag2012.getAutformValue().get(i));
//							repositoryName.setLang(this.eag2012.getAutformLang().get(i));
//							repositoryNames.add(repositoryName);
//						}
//						mainRepository.setRepositoryName(repositoryNames);
//					}
        }
        // Rest of repositories.
        if (this.eag2012.getRepositoryNameValue() != null /*&& this.eag2012.getRepositoryNameLang() != null && this.eag2012.getRepositoryNameValue().size()==this.eag2012.getRepositoryNameLang().size()*/) {
            for (int i = 0; i < this.eag2012.getRepositoryNameValue().size(); i++) {
                Repository otherRepositories = this.eag.getArchguide().getDesc().getRepositories().getRepository().get(i + 1);
                List<RepositoryName> otherRepositoryNames = null;
                if (otherRepositories.getRepositoryRole() == null) {
                    otherRepositoryNames = new ArrayList<RepositoryName>();
                } else {
                    otherRepositoryNames = otherRepositories.getRepositoryName();
                }
                RepositoryName otherRepositoryName = new RepositoryName();
                otherRepositoryName.setContent(this.eag2012.getRepositoryNameValue().get(i));
                //otherRepositoryName.setLang(this.eag2012.getRepositoryNameLang().get(i));
                otherRepositoryNames.add(otherRepositoryName);
                otherRepositories.setRepositoryName(otherRepositoryNames);
            }
        }
        this.log.debug("End method: \"createRepository\"");
    }

    /**
     * Method to fill descriptiveNote element of the elements:<br>
     * - repositorhist element.<br>
     * - researchServices element.<br>
     * - building element.<br>
     * - holdings element.
     */
    private void createAllDescriptiveNote() {
        this.log.debug("Method start: \"createAllDescriptiveNote\"");
        if (this.eag.getArchguide().getDesc().getRepositories().getRepository() != null
                && !this.eag.getArchguide().getDesc().getRepositories().getRepository().isEmpty()) {
            for (int i = 0; i < this.eag.getArchguide().getDesc().getRepositories().getRepository().size(); i++) {
                // Repository.
                Repository repository = this.eag.getArchguide().getDesc().getRepositories().getRepository().get(i);

                // Recover descriptiveNote for research services.
                if (this.eag2012.getDescriptiveNotePValue() != null
                        && this.eag2012.getDescriptiveNotePLang() != null) {
//					for (int j = 0; j < this.eag2012.getDescriptiveNotePValue().size(); j++) {
                    if (this.eag2012.getDescriptiveNotePValue().get(i) != null
                            && this.eag2012.getDescriptiveNotePLang().get(i) != null) {
                        Map<String, Map<String, List<String>>> tabsValueMap = this.eag2012.getDescriptiveNotePValue().get(i);
                        Map<String, Map<String, List<String>>> tabsLangMap = this.eag2012.getDescriptiveNotePLang().get(i);
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
                                    for (int k = 0; k < valuesList.size(); k++) {
                                        if (valuesList.get(k) != null && !valuesList.get(k).isEmpty()) {
                                            P p = new P();
                                            p.setContent(valuesList.get(k));
                                            if (!Eag2012.OPTION_NONE.equalsIgnoreCase(langList.get(k))) {
                                                p.setLang(langList.get(k));
                                            }
                                            // eag/archguide/desc/repositories/repository/services/searchroom/researchServices/descriptiveNote/P
                                            if (Eag2012.RESEARCH_SERVICES.equalsIgnoreCase(sectionValueKey)
                                                    && Eag2012.RESEARCH_SERVICES.equalsIgnoreCase(sectionLangKey)) {
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
                                            } else if (Eag2012.REFRESHMENT.equalsIgnoreCase(sectionValueKey)
                                                    && Eag2012.REFRESHMENT.equalsIgnoreCase(sectionLangKey)) {
                                                // eag/archguide/desc/repositories/repository/services/recreationalSerices/refreshment/descriptiveNote/P
                                                if (repository.getServices() == null) {
                                                    repository.setServices(new Services());
                                                }
                                                if (repository.getServices().getRecreationalServices() == null) {
                                                    repository.getServices().setRecreationalServices(new RecreationalServices());
                                                }
                                                Refreshment refreshment = null;
                                                if (repository.getServices().getRecreationalServices().getRefreshment() == null) {
                                                    refreshment = new Refreshment();
                                                } else {
                                                    refreshment = repository.getServices().getRecreationalServices().getRefreshment();
                                                }
                                                if (refreshment.getDescriptiveNote() == null) {
                                                    refreshment.setDescriptiveNote(new DescriptiveNote());
                                                }
                                                refreshment.getDescriptiveNote().getP().add(p);
                                                repository.getServices().getRecreationalServices().setRefreshment(refreshment);
                                            }
                                        }
                                    }
                                }
                            } else if (Eag2012.TAB_DESCRIPTION.equalsIgnoreCase(tabValueKey)
                                    && Eag2012.TAB_DESCRIPTION.equalsIgnoreCase(tabLangKey)) {
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
                                        if (valuesList.get(k) != null && !valuesList.get(k).isEmpty()) {
                                            P p = new P();
                                            p.setContent(valuesList.get(k));
                                            if (!Eag2012.OPTION_NONE.equalsIgnoreCase(langList.get(k))) {
                                                p.setLang(langList.get(k));
                                            }
                                            // eag/archguide/desc/repositories/repository/repositorhist/descriptiveNote/P
                                            if (Eag2012.REPOSITORHIST.equalsIgnoreCase(sectionValueKey)
                                                    && Eag2012.REPOSITORHIST.equalsIgnoreCase(sectionLangKey)) {
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
                                            } else if (Eag2012.BUILDING.equalsIgnoreCase(sectionValueKey)
                                                    && Eag2012.BUILDING.equalsIgnoreCase(sectionLangKey)) {
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
                                            } else if (Eag2012.HOLDINGS.equalsIgnoreCase(sectionValueKey)
                                                    && Eag2012.HOLDINGS.equalsIgnoreCase(sectionLangKey)) {
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
        this.log.debug("End method: \"createAllDescriptiveNote\"");
    }

    /**
     * Method to fill rule element.
     */
    private void createAllRules() {
        this.log.debug("Method start: \"createAllRules\"");
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
                        if (valuesList.get(j) != null && !valuesList.get(j).isEmpty()) {
                            // eag/archguide/desc/repositories/repository/repositorfound/rule
                            if (Eag2012.REPOSITOR_FOUND.equalsIgnoreCase(subsectionValueKey)
                                    && Eag2012.REPOSITOR_FOUND.equalsIgnoreCase(subsectionLangKey)) {
                                if (repository.getRepositorfound() == null) {
                                    repository.setRepositorfound(new Repositorfound());
                                }
                                Rule rule = new Rule();
                                rule.setContent(valuesList.get(j));
                                if (!Eag2012.OPTION_NONE.equalsIgnoreCase(langsList.get(j))) {
                                    rule.setLang(langsList.get(j));
                                }
                                repository.getRepositorfound().getRule().add(rule);
                            }
                            // eag/archguide/desc/repositories/repository/repositorsup/rule
                            if (Eag2012.REPOSITOR_SUP.equalsIgnoreCase(subsectionValueKey)
                                    && Eag2012.REPOSITOR_SUP.equalsIgnoreCase(subsectionLangKey)) {
                                if (repository.getRepositorsup() == null) {
                                    repository.setRepositorsup(new Repositorsup());
                                }
                                Rule rule = new Rule();
                                rule.setContent(valuesList.get(j));
                                if (!Eag2012.OPTION_NONE.equalsIgnoreCase(langsList.get(j))) {
                                    rule.setLang(langsList.get(j));
                                }
                                repository.getRepositorsup().getRule().add(rule);
                            }
                        }
                    }
                }
            }
        }
        this.log.debug("End method: \"createAllRules\"");
    }

    /**
     * Method to fill Geoagarea element.
     */
    public void createGeoagarea() {
        this.log.debug("Method start: \"createGeoagarea\"");
        // eag/archguide/desc/repositories/repository/Geoagarea
        if (this.eag2012.getGeogareaValue() != null) {
            String geogareaValue = this.eag2012.getGeogareaValue();
            Geogarea geogarea = new Geogarea();
            if (Eag2012.OPTION_AFRICA.equalsIgnoreCase(geogareaValue)) {
                geogarea.setValue(Eag2012.OPTION_AFRICA_TEXT);
            } else if (Eag2012.OPTION_ANTARCTICA.equalsIgnoreCase(geogareaValue)) {
                geogarea.setValue(Eag2012.OPTION_ANTARCTICA_TEXT);
            } else if (Eag2012.OPTION_ASIA.equalsIgnoreCase(geogareaValue)) {
                geogarea.setValue(Eag2012.OPTION_ASIA_TEXT);
            } else if (Eag2012.OPTION_AUSTRALIA.equalsIgnoreCase(geogareaValue)) {
                geogarea.setValue(Eag2012.OPTION_AUSTRALIA_TEXT);
            } else if (Eag2012.OPTION_EUROPE.equalsIgnoreCase(geogareaValue)) {
                geogarea.setValue(Eag2012.OPTION_EUROPE_TEXT);
            } else if (Eag2012.OPTION_NORTH_AMERICA.equalsIgnoreCase(geogareaValue)) {
                geogarea.setValue(Eag2012.OPTION_NORTH_AMERICA_TEXT);
            } else if (Eag2012.OPTION_SOUTH_AMERICA.equalsIgnoreCase(geogareaValue)) {
                geogarea.setValue(Eag2012.OPTION_SOUTH_AMERICA_TEXT);
            }
            // For every repository.
            for (int i = 0; i < this.eag.getArchguide().getDesc().getRepositories().getRepository().size(); i++) {
                this.eag.getArchguide().getDesc().getRepositories().getRepository().get(i).setGeogarea(geogarea);
            }
        }
        this.log.debug("End method: \"createGeoagarea\"");
    }

    /**
     * Method to fill telephone element.
     */
    private void createTelephone() {
        this.log.debug("Method start: \"createTelephone\"");
        // eag/archguide/desc/repositories/repository/telephone
        if (this.eag2012.getTelephoneValue() != null) {
            for (int i = 0; i < this.eag2012.getTelephoneValue().size(); i++) {
                Map<String, Map<String, List<String>>> tabsMap = this.eag2012.getTelephoneValue().get(i);
                // Repository.
                Repository repository = (this.eag.getArchguide().getDesc().getRepositories().getRepository().size() > i) ? this.eag.getArchguide().getDesc().getRepositories().getRepository().get(i) : null;
                if (repository != null) {
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
                                if (Eag2012.ROOT.equalsIgnoreCase(sectionKey)) {
                                    repository.getTelephone().add(telephone);
                                }
                                // eag/archguide/desc/repositories/repository/services/searchroom
                                if (Eag2012.SEARCHROOM.equalsIgnoreCase(sectionKey)) {
                                    repository = createRepositorySearchroom(repository, true);
                                    repository.getServices().getSearchroom().getContact().getTelephone().add(telephone);
                                }
                                // eag/archguide/desc/repositories/repository/services/library
                                if (Eag2012.LIBRARY.equalsIgnoreCase(sectionKey)) {
                                    repository = createRepositoryLibrary(repository, true);
                                    repository.getServices().getLibrary().getContact().getTelephone().add(telephone);
                                }
                                // eag/archguide/desc/repositories/repository/services/techservices/restorationlab
                                if (Eag2012.RESTORATION_LAB.equalsIgnoreCase(sectionKey)) {
                                    repository = createRepositoryTechservicesRestorationlab(repository, true);
                                    repository.getServices().getTechservices().getRestorationlab().getContact().getTelephone().add(telephone);
                                }
                                // eag/archguide/desc/repositories/repository/services/techservices/reproductionser
                                if (Eag2012.REPRODUCTIONSER.equalsIgnoreCase(sectionKey)) {
                                    repository = createRepositoryTechservicesReproductionser(repository, true);
                                    repository.getServices().getTechservices().getReproductionser().getContact().getTelephone().add(telephone);
                                }
                            }
                        }
                    }
                }
            }
        }
        this.log.debug("End method: \"createTelephone\"");
    }

    /**
     * Method to fill fax element.
     */
    private void createFax() {
        this.log.debug("Method start: \"createFax\"");
        // eag/archguide/desc/repositories/repository/fax
        if (this.eag2012.getFaxValue() != null) {
            for (int i = 0; i < this.eag2012.getFaxValue().size(); i++) {
                Map<String, List<String>> tabsMap = this.eag2012.getFaxValue().get(i);
                // Repository.
                Repository repository = this.eag.getArchguide().getDesc().getRepositories().getRepository().get(i);
                Iterator<String> tabsIt = tabsMap.keySet().iterator();
                while (tabsIt.hasNext()) {
                    String tabKey = tabsIt.next();
                    if (tabKey == Eag2012.TAB_CONTACT) {
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
        this.log.debug("End method: \"createFax\"");
    }

    /**
     * Method to fill directions element.
     */
    private void createDirections() {
        this.log.debug("Method start: \"createDirections\"");
        if (this.eag2012.getDirectionsValue() != null && this.eag2012.getDirectionsLang() != null
                && this.eag2012.getCitationHref() != null) {
            for (int i = 0; i < this.eag2012.getDirectionsValue().size(); i++) {
                List<String> directionsValueList = this.eag2012.getDirectionsValue().get(i);
                List<String> directionsLangList = this.eag2012.getDirectionsLang().get(i);
                Map<String, List<String>> citationHrefMap = this.eag2012.getCitationHref().get(i);
                // Repository
                Repository repository = this.eag.getArchguide().getDesc().getRepositories().getRepository().get(i);
                Iterator<String> citationHrefIt = citationHrefMap.keySet().iterator();
                while (citationHrefIt.hasNext()) {
                    List<String> citationHrefList = citationHrefMap.get(citationHrefIt.next());

                    for (int j = 0; j < directionsValueList.size(); j++) {
                        Directions directions = new Directions();
                        directions.getContent().add(directionsValueList.get(j));
                        // eag/archguide/desc/repositories/repository/directions/lang
                        if (!Eag2012.OPTION_NONE.equalsIgnoreCase(directionsLangList.get(j))) {
                            directions.setLang(directionsLangList.get(j));
                        }
                        // eag/archguide/desc/repositories/repository/directions/citation/href
                        Citation citation = new Citation();
                        citation.setHref(citationHrefList.get(j));
                        if ((directionsValueList.get(j) != null && !directionsValueList.get(j).isEmpty()) || (citation.getHref() != null && !citation.getHref().isEmpty())) {
                            directions.getContent().add(citation);
                            repository.getDirections().add(directions);
                        }
                    }
                }
            }
        }
        this.log.debug("End method: \"createDirections\"");
    }

    /**
     * Method to fill adminhierarchy element.
     */
    private void createAdminhierarchy() {
        this.log.debug("Method start: \"createAdminhierarchy\"");
        // eag/archguide/desc/repositories/repository/adminhierarchy
        if (this.eag2012.getAdminunitValue() != null
                && this.eag2012.getAdminunitLang() != null) {
            for (int i = 0; i < this.eag2012.getAdminunitValue().size(); i++) {
                // Repository
                Repository repository = this.eag.getArchguide().getDesc().getRepositories().getRepository().get(i);
                List<String> valuesList = this.eag2012.getAdminunitValue().get(i);
                List<String> langsList = this.eag2012.getAdminunitLang().get(i);
                for (int j = 0; j < valuesList.size(); j++) {
                    if (valuesList.get(j) != null && !valuesList.get(j).isEmpty()) {
                        if (repository.getAdminhierarchy() == null) {
                            repository.setAdminhierarchy(new Adminhierarchy());
                        }
                        Adminunit adminunit = new Adminunit();
                        adminunit.setContent(valuesList.get(j));
                        if (!Eag2012.OPTION_NONE.equalsIgnoreCase(langsList.get(j))) {
                            adminunit.setLang(langsList.get(j));
                        }
                        repository.getAdminhierarchy().getAdminunit().add(adminunit);
                    }
                }
            }
        }
        this.log.debug("End method: \"createAdminhierarchy\"");
    }

    /**
     * Method to fill opening element.
     */
    private void createOpening() {
        this.log.debug("Method start: \"createOpening\"");
        // eag/archguide/desc/repositories/repository/timetable/opening
        if (this.eag2012.getOpeningValue() != null) {
            for (int i = 0; i < this.eag2012.getOpeningValue().size(); i++) {
                Map<String, List<String>> tabsValueMap = this.eag2012.getOpeningValue().get(i);
                Map<String, List<String>> tabsLangMap = null;
                if (this.eag2012.getOpeningLang() != null && !this.eag2012.getOpeningLang().isEmpty()) {
                    tabsLangMap = this.eag2012.getOpeningLang().get(i);
                }
                Map<String, List<String>> tabsHrefMap = null;
                if (this.eag2012.getOpeningHref() != null && !this.eag2012.getOpeningHref().isEmpty()) {
                    tabsHrefMap = this.eag2012.getOpeningHref().get(i);
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
                Iterator<String> openingHrefIt = null;
                if (tabsHrefMap != null) {
                    openingHrefIt = tabsHrefMap.keySet().iterator();
                }
                while (openingValueIt.hasNext()) {
                    String openingValueKey = openingValueIt.next();
                    String openingLangKey = (openingLangIt != null && openingLangIt.hasNext()) ? openingLangIt.next() : null;
                    String openingHrefKey = (openingHrefIt != null && openingHrefIt.hasNext()) ? openingHrefIt.next() : null;
                    List<String> openingValueList = tabsValueMap.get(openingValueKey);
                    List<String> openingLangList = null;
                    if (openingLangIt != null) {
                        openingLangList = (openingLangKey != null) ? tabsLangMap.get(openingLangKey) : null;
                    }
                    List<String> openingHrefList = null;
                    if (openingHrefIt != null) {
                        openingHrefList = (openingHrefKey != null) ? tabsHrefMap.get(openingHrefKey) : null;
                    }
                    for (int j = 0; j < openingValueList.size(); j++) {
                        boolean found = false;
                        if (!repository.getTimetable().getOpening().isEmpty()) {
                            for (int k = 0; !found && k < repository.getTimetable().getOpening().size(); k++) {
                                Opening opening = repository.getTimetable().getOpening().get(k);
                                if (opening.getContent() != null
                                        && opening.getContent().equalsIgnoreCase(openingValueList.get(j)) && ((opening.getLang() != null && opening.getLang().equalsIgnoreCase(openingLangList.get(j)))
                                        || (opening.getLang() == null && (openingLangList.size() <= j || openingLangList.get(j) == null || openingLangList.get(j).equalsIgnoreCase("none"))))) {
                                    found = true;
                                }
                            }
                        }
                        if (!found) {
                            if (openingValueList.get(j) != null
                                    && !openingValueList.get(j).isEmpty()) {
                                Opening opening = new Opening();
                                opening.setContent(openingValueList.get(j));
                                if (openingLangList != null
                                        && !Eag2012.OPTION_NONE.equalsIgnoreCase(openingLangList.get(j))) {
                                    opening.setLang(openingLangList.get(j));
                                }
                                if (openingHrefList != null) {
                                    opening.setHref(openingHrefList.get(j));
                                }
                                repository.getTimetable().getOpening().add(opening);
                            }
                        }
                    }
                }
            }
        }
        this.log.debug("End method: \"createOpening\"");
    }

    /**
     * Method to fill closing element.
     */
    private void createClosing() {
        this.log.debug("Method start: \"createClosing\"");
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
                    closingLangIt = tabsLangMap.keySet().iterator();
                }
                while (closingValueIt.hasNext()) {
                    List<String> closingValueList = tabsValueMap.get(closingValueIt.next());
                    List<String> closingLangList = null;
                    if (closingLangIt != null) {
                        closingLangList = tabsLangMap.get(closingLangIt.next());
                    }
                    for (int j = 0; j < closingValueList.size(); j++) {
                        boolean found = false;
                        if (!repository.getTimetable().getClosing().isEmpty()) {
                            for (int k = 0; !found && k < repository.getTimetable().getClosing().size(); k++) {
                                Closing closing = repository.getTimetable().getClosing().get(k);
                                if (closing.getContent() != null
                                        && closing.getContent().equalsIgnoreCase(closingValueList.get(j)) && ((closing.getLang() != null && closing.getLang() != null && closing.getLang().equalsIgnoreCase(closingLangList.get(j)))
                                        || (closing.getLang() == null && (closingLangList == null || closingLangList.size() <= j || closingLangList.get(j).equalsIgnoreCase("none"))))) {
                                    found = true;
                                }
                            }
                        }
                        if (!found) {
                            if (closingValueList.get(j) != null
                                    && !closingValueList.get(j).isEmpty()) {
                                Closing closing = new Closing();
                                closing.setContent(closingValueList.get(j));
                                if (closingLangList != null
                                        && !Eag2012.OPTION_NONE.equalsIgnoreCase(closingLangList.get(j))) {
                                    closing.setLang(closingLangList.get(j));
                                }
                                repository.getTimetable().getClosing().add(closing);
                            }
                        }
                    }
                }
            }
        }
        this.log.debug("End method: \"createClosing\"");
    }

    /**
     * Method to fill question element.
     */
    private void createAccessibilityQuestion() {
        this.log.debug("Method start: \"createAccessibilityQuestion\"");
        // eag/archguide/desc/repositories/repository/accessibility/question
        if (this.eag2012.getAccessibilityQuestion() != null) {
            for (int i = 0; i < this.eag2012.getAccessibilityQuestion().size(); i++) {
                Map<String, String> tabsQuestionMap = this.eag2012.getAccessibilityQuestion().get(i);
                Repository repository = this.eag.getArchguide().getDesc().getRepositories().getRepository().get(i);
                Iterator<String> questionValueIt = tabsQuestionMap.keySet().iterator();
                while (questionValueIt.hasNext()) {
                    String key = questionValueIt.next();
                    // Rest of tabs.
                    if (!Eag2012.TAB_YOUR_INSTITUTION.equalsIgnoreCase(key)) {
                        Accessibility accessibility = new Accessibility();
                        accessibility.setQuestion(tabsQuestionMap.get(key));
                        repository.getAccessibility().add(accessibility);
                    }
                }
            }
        }
        this.log.debug("End method: \"createAccessibilityQuestion\"");
    }

    /**
     * Method to fill accessibility element.
     */
    private void createAccessibility() {
        this.log.debug("Method start: \"createAccessibility\"");
        // eag/archguide/desc/repositories/repository/accessibility
        if (this.eag2012.getAccessibilityValue() != null && this.eag2012.getAccessibilityLang() != null) {
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
//							if (!accessibilityValueKey.equalsIgnoreCase(Eag2012.TAB_YOUR_INSTITUTION)) {
                    List<String> accessibilityValueList = tabsValueMap.get(accessibilityValueKey);
                    List<String> accessibilityLangList = null;
                    if (accessibilityLangIt != null) {
                        accessibilityLangList = tabsLangMap.get(accessibilityLangIt.next());
                    }
                    for (int j = 0; j < accessibilityValueList.size(); j++) {
                        boolean exit = false;
                        if (accessibilityValueList.get(j) == null || accessibilityValueList.get(j).isEmpty()) {
                            exit = true;
                        }
                        if (!exit) {
                            for (int k = 0; k < repository.getAccessibility().size(); k++) {
                                if (accessibilityValueList.get(j).equalsIgnoreCase(repository.getAccessibility().get(k).getContent())) {
                                    exit = true;
                                }
                            }
                            if (!exit) {
                                List<Accessibility> list = repository.getAccessibility();
                                String question = null;
                                if (list.size() > 0) {
                                    question = repository.getAccessibility().get(0).getQuestion();
                                    if (list.size() == 1 && (list.get(0).getLang() == null || list.get(0).getLang().isEmpty())
                                            && (list.get(0).getContent() == null || list.get(0).getContent().isEmpty())) {
                                        list.clear();
                                    }
                                }
                                if (accessibilityValueList.get(j) != null
                                        && !accessibilityValueList.get(j).isEmpty()) {
                                    Accessibility accessibility = new Accessibility();
                                    accessibility.setContent(accessibilityValueList.get(j));
                                    if (accessibilityLangList != null && !Eag2012.OPTION_NONE.equalsIgnoreCase(accessibilityLangList.get(j))) {
                                        accessibility.setLang(accessibilityLangList.get(j));
                                    }
                                    accessibility.setQuestion(question);
                                    list.add(accessibility);
                                    repository.setAccessibility(list);
                                }
                            }
                        }
                    }
//							}
                }
            }
        }
        this.log.debug("End method: \"createAccessibility\"");
    }

    /**
     * Method to recover descriptiveNote.
     *
     * @param object The object to add the descriptive note.
     */
    private void getDescriptiveNote(final Object object, final int index, final int repoIndex) {
        if (this.eag2012.getDescriptiveNotePValue() != null
                && this.eag2012.getDescriptiveNotePLang() != null) {
//			for (int i = 0; i < this.eag2012.getDescriptiveNotePValue().size(); i++) {
            if (this.eag2012.getDescriptiveNotePValue().get(repoIndex) != null
                    && this.eag2012.getDescriptiveNotePLang().get(repoIndex) != null) {
                Map<String, Map<String, List<String>>> tabsValueMap = this.eag2012.getDescriptiveNotePValue().get(repoIndex);
                Map<String, Map<String, List<String>>> tabsLangMap = this.eag2012.getDescriptiveNotePLang().get(repoIndex);
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
//							for (int j = 0; j < valuesList.size(); j++) {
                            if (index > -1 && valuesList != null && valuesList.size() > index && !(valuesList.get(index) == null || valuesList.get(index).isEmpty())) {
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
                            } else if (index == -1) {
                                for (int x = 0; x < valuesList.size(); x++) {
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
//						}
                    }
                }
            }
        }
    }
}
