package eu.apenet.persistence.dao;

import java.util.List;

import eu.apenet.persistence.vo.ArchivalInstitution;
import eu.apenet.persistence.vo.Coordinates;

public interface CoordinatesDAO extends GenericDAO<Coordinates, Integer>{
	public List<Coordinates> findCoordinatesByArchivalInstitution(ArchivalInstitution archivalInstitution);
	public List<Coordinates> getCoordinatesByCountryCode(String countryCode);
	public List<Coordinates> getCoordinates();
}