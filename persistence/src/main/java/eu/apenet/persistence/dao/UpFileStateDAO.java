package eu.apenet.persistence.dao;

import eu.apenet.persistence.vo.UpFileState;

/**
 * 
 * @author eloy
 *
 */

public interface UpFileStateDAO extends GenericDAO<UpFileState, Integer> {

	public UpFileState getUpFileStateByState(String state);
}
