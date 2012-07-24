package eu.apenet.persistence.dao;

import java.util.Collection;
import java.util.List;

import eu.apenet.persistence.vo.Warnings;

/**
 * 
 * @author eloy
 *
 */

public interface WarningsDAO extends GenericDAO<Warnings, Integer> {
	public List<Warnings> getWarnings(Integer faId, Integer hgId);
}
