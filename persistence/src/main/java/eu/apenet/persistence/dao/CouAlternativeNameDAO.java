package eu.apenet.persistence.dao;

import java.util.List;

import eu.apenet.persistence.vo.CouAlternativeName;
import eu.apenet.persistence.vo.Country;
import eu.apenet.persistence.vo.Lang;

public interface CouAlternativeNameDAO extends GenericDAO<CouAlternativeName, Integer>{
	public String getLocalizedCountry(String countryIso2name, String languageIso2name);
	public List<CouAlternativeName> getAltNamesbyCountry(Country country);
	public CouAlternativeName getAltNamebyCountryAndLang(Country country, Lang language);
}
