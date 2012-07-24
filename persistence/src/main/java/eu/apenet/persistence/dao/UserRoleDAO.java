package eu.apenet.persistence.dao;

import eu.apenet.persistence.vo.UserRole;

/**
 * 
 * @author paul
 *
 */

public interface UserRoleDAO extends GenericDAO<UserRole, Integer> {
	public UserRole getUserRole(String type);
}

