package eu.apenet.persistence.dao;

import java.util.Collection;
import java.util.List;

import eu.apenet.persistence.exception.NormalUserException;
import eu.apenet.persistence.vo.NormalUser;

/***
 * This class works like interface to the specific methods and features in respect to NormalUser entitys.
 * @author paul
 */

public interface NormalUserDAO extends GenericDAO<NormalUser, Long> {
	
	public NormalUser loginUser(String login, String password, boolean Active) throws NormalUserException;
	public NormalUser exitsUser(String login, String password) throws NormalUserException; 
	public NormalUser exitsNickUser(String nick) throws NormalUserException;
	public NormalUser exitsEmailUser(String email) throws NormalUserException;
	public Long countNormalUsers(String nick, String emailAddress, String pwd, Collection<String> userStates);
	public List<NormalUser> getNormalUsersPage (String nick, String emailAddress, String pwd, Collection<String> userStates,
			String sort, boolean ascending, Integer pageNumber, Integer pageSize);
}
