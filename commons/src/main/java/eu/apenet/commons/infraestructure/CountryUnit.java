package eu.apenet.commons.infraestructure;

import java.util.List;

import eu.apenet.persistence.dao.ArchivalInstitutionDAO;
import eu.apenet.persistence.dao.CountryDAO;
import eu.apenet.persistence.dao.UserDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.Country;
import eu.apenet.persistence.vo.User;

/**
 *
 * @author Eloy Date: 8th, Nov
 *
 * This class represents a country in the Portal
 */
public class CountryUnit implements Comparable<CountryUnit> {

    //Attributes
    private Country country;		//Identifier in country table
    private String localizedName;	//Country name in the language selected
    private Boolean hasArchivalInstitutions = null;	//Number of the Archival Institutions which belong to this country

    public String getLocalizedName() {
        return localizedName;
    }

    public void setLocalizedName(String localizedName) {
        this.localizedName = localizedName;
    }

    public boolean isHasArchivalInstitutions() {
        if (hasArchivalInstitutions == null) {
            if (country != null) {
                hasArchivalInstitutions = DAOFactory.instance().getArchivalInstitutionDAO().countArchivalInstitutionsByCountryId(country.getId()) > 0;
            } else {
                hasArchivalInstitutions = false;
            }
        }
        return hasArchivalInstitutions;
    }
    //Constructor

    public CountryUnit(Country country, String localizedName) {
        this.country = country;
        this.localizedName = localizedName;
    }

    public Country getCountry() {
        return country;
    }

    @Override
    public int compareTo(CountryUnit o) {
        return localizedName.compareTo(o.getLocalizedName());
    }

}
