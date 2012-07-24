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
 * @author Eloy
 * Date: 8th, Nov
 *
 * This class represents a country in the Portal
 */

public class CountryUnit implements Comparable<CountryUnit>{

	//Attributes
	private Integer couId;		//Identifier in country table
	private String name;		//Country name in English
	private String localizedName;	//Country name in the language selected
	private boolean hasArchivalInstitutions = false;	//Number of the Archival Institutions which belong to this country
	
	//Getters and Setters
	public Integer getCouId() {
		return couId;
	}
	public void setCouId(Integer couId) {
		this.couId = couId;
	}
	public String getLocalizedName() {
		return localizedName;
	}
	public void setLocalizedName(String localizedName) {
		this.localizedName = localizedName;
	}
	public boolean isHasArchivalInstitutions() {
		return hasArchivalInstitutions;
	}
	public void setHasArchivalInstitutions(boolean hasArchivalInstitutions) {
		this.hasArchivalInstitutions = hasArchivalInstitutions;
	}
	//Constructor
	public CountryUnit(String name, String localizedName) {
		this(null, name, localizedName);
		
		CountryDAO countryDao = DAOFactory.instance().getCountryDAO();
		Country country = countryDao.getCountryByCname(name);
		
		if (country==null){
			//The country in the Archival Landscape is not in data base
			this.couId = null;
		}
		else {
			this.couId = country.getCouId();
			hasArchivalInstitutions = DAOFactory.instance().getArchivalInstitutionDAO().countArchivalInstitutionsByCountryId(couId) > 0;
		}
				
	}
	

	public CountryUnit(Integer couId, String name, String localizedName) {
		super();
		this.couId = couId;
		this.name = name;
		this.localizedName = localizedName;
	}



	@Override
	public int compareTo(CountryUnit o) {
		return localizedName.compareTo(o.getLocalizedName());
	}


}
