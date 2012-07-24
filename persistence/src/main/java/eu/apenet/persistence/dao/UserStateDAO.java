package eu.apenet.persistence.dao;

import eu.apenet.persistence.vo.UserState;

/**
 * 
 * @author paul
 *
 */

public interface UserStateDAO extends GenericDAO<UserState, Long> {
	// Only CRUD operations from GenericDAO
    UserState getUserStateByState(String state);
}

