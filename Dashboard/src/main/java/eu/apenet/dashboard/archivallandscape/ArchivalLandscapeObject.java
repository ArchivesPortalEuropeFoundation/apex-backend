package eu.apenet.dashboard.archivallandscape;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import eu.apenet.persistence.factory.DAOFactory;
import org.apache.log4j.Logger;

import eu.apenet.persistence.vo.ArchivalInstitution;
import eu.apenet.persistence.vo.Country;

/**
 * @author Yoann
 */
public class ArchivalLandscapeObject {

    private final static Logger LOG = Logger.getLogger(ArchivalLandscapeObject.class);

    private List<Country> countries;

    public List<Country> getCountries() {
        return countries;
    }

    public void setCountries(List<Country> countries) {
        this.countries = countries;
    }

    public ArchivalLandscapeObject(List<Country> countries) {
        this.countries = countries;
        createHierarchy();
    }
    public ArchivalLandscapeObject(Country country) {
        this.countries = new ArrayList<Country>(1);
        this.countries.add(country);
        createHierarchy();
    }

    public void createHierarchy() {
        for(Country country : countries) {
            Set<ArchivalInstitution> archivalInstitutions = country.getArchivalInstitutions();
            for(ArchivalInstitution archivalInstitution : archivalInstitutions) {
                if(archivalInstitution.getParentAiId() == null) {
                    recurrenceLoop(archivalInstitution);
                }
            }
        }
    }

    public void recurrenceLoop(ArchivalInstitution archivalInstitution) {
        Set<ArchivalInstitution> archivalInstitutionChildren = archivalInstitution.getChildArchivalInstitutions();
        for(ArchivalInstitution archivalInstitutionChild : archivalInstitutionChildren) {
            recurrenceLoop(archivalInstitutionChild);
        }
    }

    public void deleteInstitution(Long archivalInstitutionId) {
        ArchivalInstitution archivalInstitution = searchInstitution(archivalInstitutionId);
        archivalInstitution.getParent().getChildArchivalInstitutions().remove(archivalInstitution);
    }
    public void addInstitution(Long countryId, ArchivalInstitution archivalInstitution) {
        DAOFactory.instance().getCountryDAO().findById(countryId.intValue()).getArchivalInstitutions().add(archivalInstitution);
    }

    public ArchivalInstitution searchInstitution(long id) {
        ArchivalInstitution archivalInstitutionWeSearch = null;
        for(Country country : countries) {
            Set<ArchivalInstitution> archivalInstitutions = country.getArchivalInstitutions();
            for(ArchivalInstitution archivalInstitution : archivalInstitutions) {
                if(archivalInstitution.getAiId() == id) {
                    return archivalInstitution;
                }
                if(archivalInstitution.getParentAiId() == null) {
                    archivalInstitutionWeSearch = recurrenceLoopSearch(archivalInstitution, id);
                    if(archivalInstitutionWeSearch != null)
                        return archivalInstitutionWeSearch;
                }
            }
        }
        return archivalInstitutionWeSearch;
    }

    public ArchivalInstitution recurrenceLoopSearch(ArchivalInstitution archivalInstitution, long id) {
        ArchivalInstitution archivalInstitutionWeSearch = null;
        Set<ArchivalInstitution> archivalInstitutionChildren = archivalInstitution.getChildArchivalInstitutions();
        for(ArchivalInstitution archivalInstitutionChild : archivalInstitutionChildren) {
            if(archivalInstitutionChild.getAiId() == id) {
                return archivalInstitutionChild;
            }
            archivalInstitutionWeSearch = recurrenceLoopSearch(archivalInstitutionChild, id);
            if(archivalInstitutionWeSearch != null)
                return archivalInstitutionWeSearch;
        }
        return archivalInstitutionWeSearch;
    }

}