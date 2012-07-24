package eu.apenet.persistence.dao;

import java.util.List;

import eu.apenet.persistence.vo.User;
import eu.apenet.persistence.vo.Searches;

/**
 * @author paul
 */

public interface SearchesDAO extends GenericDAO<Searches, Long> {
	public List<Searches> getSearchesByNormalUser(Long uId); 
}

