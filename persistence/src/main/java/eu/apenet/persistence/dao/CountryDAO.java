package eu.apenet.persistence.dao;

import java.util.List;

import eu.apenet.persistence.vo.Country;

/**
 * 
 * @author Patricia
 *
 */

public interface CountryDAO extends GenericDAO<Country, Integer> {
	public List<Country> getCountries(String isoname);
	public Country getCountryByCname (String cname);
	public List<Country> getCountriesOrderByCName();
	public List<Country> getCountriesWithContentIndexedOrderByCName();
	public List<Country> getCountriesWithFindingAidsIndexedOrderByCName();
	public List<Country> getCountriesWithArchivalInstitutionsWithEAG();
}

