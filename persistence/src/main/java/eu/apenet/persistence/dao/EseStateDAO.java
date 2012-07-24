package eu.apenet.persistence.dao;

import eu.apenet.persistence.vo.EseState;

/**
 * 
 * @author bverhoef
 *
 */

public interface EseStateDAO extends GenericDAO<EseState, Integer> {

	public EseState getEseStateByState(String state);
}
