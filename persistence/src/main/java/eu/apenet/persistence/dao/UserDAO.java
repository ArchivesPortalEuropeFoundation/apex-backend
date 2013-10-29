package eu.apenet.persistence.dao;

import java.util.List;

import eu.apenet.persistence.exception.PartnerException;
import eu.apenet.persistence.vo.Country;
import eu.apenet.persistence.vo.User;

/**
 * 
 * @author paul
 *
 */

public interface UserDAO extends GenericDAO<User, Integer> {
	public User loginUser(String login, String password) throws PartnerException;
	public User exitsEmailUser(String email) throws PartnerException;
	public List<User> getPartnersByCountryAndByRoleType (Integer couId, String role);
	public List<User> getPartnersByRoleType(String role);
    public User getCountryManagerOfCountry(Country country);
    public boolean doesAdminExist();
}

