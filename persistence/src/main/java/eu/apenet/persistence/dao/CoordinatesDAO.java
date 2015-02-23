package eu.apenet.persistence.dao;

import java.util.List;

import eu.apenet.persistence.vo.ArchivalInstitution;
import eu.apenet.persistence.vo.Coordinates;

public interface CoordinatesDAO extends GenericDAO<Coordinates, Integer>{
	
	/***
	 * Function gets the coordinates on an archival institution
	 * 
	 * @param archivalInstitution {@link ArchivalInstitution} ArchivalInstitution object to build the criteria
	 * 
	 * @return results List {@link Coordinates} criteria.list()
	 */
	public List<Coordinates> findCoordinatesByArchivalInstitution(ArchivalInstitution archivalInstitution);
	
	/**
	 * get all repos from a country to build country landscape
	 * 
	 * @param countryCode {@link String} The ISO name of the country name
	 * 
	 * @return results List {@link Coordinates} criteria.list()
	 */
	public List<Coordinates> getCoordinatesByCountryCode(String countryCode);
	
	/***
	 * Function gets the list of coordinates
	 * 
	 * @return results List {@link Coordinates} criteria.list()
	 */
	public List<Coordinates> getCoordinates();
}