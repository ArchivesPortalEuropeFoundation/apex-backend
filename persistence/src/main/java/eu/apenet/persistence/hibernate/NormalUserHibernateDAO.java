package eu.apenet.persistence.hibernate;

import java.sql.SQLException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;


import eu.apenet.persistence.dao.NormalUserDAO;
import eu.apenet.persistence.exception.NormalUserException;
import eu.apenet.persistence.vo.NormalUser;
import eu.apenet.persistence.vo.UserState;

/***
 * This class implements the specific methods and features in respect to NormalUser entitys with Hibernate(Criteria) technology.
 * @author paul
 */

public class NormalUserHibernateDAO extends AbstractHibernateDAO<NormalUser, Long> implements NormalUserDAO {

	private final Logger log = Logger.getLogger(getClass());

	
	@SuppressWarnings("unchecked")
	public void deleteUser (String nick, String pwd, boolean Active){
		String errorMessage="Error consulting DB for delete user with state Active: "+ Active;
		NormalUser result = new NormalUser();
		Collection <String> listActived = null;
		try {
		listActived = new LinkedList<String>();
		if (Active == true) {
			listActived.add("Actived");}
		else 
			{
			listActived.add("Blocked");}
		
		Criteria criteria = createNormalUsersCriteria(nick, "", pwd, listActived, "", false);
		result = (NormalUser) criteria.uniqueResult();
		}
		catch (HibernateException he) {throw new NormalUserException(" Message: "+he.getMessage()+" Cause: "+he.getCause()+" Error: "+errorMessage);} 
		catch(Exception e) {throw new NormalUserException(e.getMessage()+errorMessage);}
	}
	
	@SuppressWarnings("unchecked")
	public NormalUser loginUser (String nick, String pwd, boolean Active){
		String errorMessage="Error consulting DB for login user with state Active: "+ Active;
		NormalUser result = new NormalUser();
		Collection <String> listActived = null;
		try {
		listActived = new LinkedList<String>();
		if (Active == true) {
			listActived.add("Actived");}
		else 
			{
			listActived.add("Blocked");}
		
		Criteria criteria = createNormalUsersCriteria(nick, "", pwd, listActived, "", false);
		result = (NormalUser) criteria.uniqueResult();
		}
		catch (HibernateException he) {throw new NormalUserException(" Message: "+he.getMessage()+" Cause: "+he.getCause()+" Error: "+errorMessage);} 
		catch(Exception e) {throw new NormalUserException(e.getMessage()+errorMessage);}
		return result;
	}
	
	
	//@Override
	public NormalUser exitsUser(String login, String password) throws NormalUserException {
		String errorMessage="Error consulting DB for exits user";
		//TODO: Control multiple results.
		try {
		return (NormalUser) getSession().createCriteria(NormalUser.class)
			.add(Example.create(new NormalUser(null, login, null,password))).uniqueResult();
		}
		catch (HibernateException he) {throw new NormalUserException(" Message: "+he.getMessage()+" Cause: "+he.getCause()+" Error: "+errorMessage);} 
		catch(Exception e) {throw new NormalUserException(e.getMessage()+errorMessage);}
	}
		
	
	//@Override
	public NormalUser exitsNickUser(String nick) throws NormalUserException {
		String errorMessage="Error consulting DB for nick user";
		//TODO: Control multiple results.
		try {
		return (NormalUser) getSession().createCriteria(NormalUser.class)
			.add(Example.create(new NormalUser(null, nick, null,null)))
			.uniqueResult();
		}
		catch (HibernateException he) {throw new NormalUserException(he.getMessage()+errorMessage);} 
		catch(Exception e) {throw new NormalUserException(e.getMessage()+errorMessage);}
	}
	
	
	//@Override
	public NormalUser exitsEmailUser(String email) throws NormalUserException {
		String errorMessage="Error consulting DB for email user";
		//TODO: Control multiple results.
		try {
		return (NormalUser) getSession().createCriteria(NormalUser.class)
			.add(Example.create(new NormalUser(null, null, email,null)))
			.uniqueResult();
		}
		catch (HibernateException he) {throw new NormalUserException(" Message: "+he.getMessage()+" Cause: "+he.getCause()+" Error: "+errorMessage);}
		catch(Exception e) {throw new NormalUserException(e.getMessage()+errorMessage);}
	}
	
	
	public Long countNormalUsers(String nick, String emailAddress, String pwd, Collection<String> userStates){
		Criteria criteria = createNormalUsersCriteria(nick, emailAddress, pwd, userStates, "", false);
		criteria.setProjection(Projections.countDistinct("normalUser.UId"));
		Object object = criteria.list().get(0);
		if (object instanceof Integer) {
			return ((Integer) object).longValue();
		} else if (object instanceof Long) {
			return (Long) object;
		}
		return (Long) new Long(0);
	}
	
	@SuppressWarnings("unchecked")
	public List<NormalUser> getNormalUsersPage (String nick, String emailAddress, String pwd, Collection<String> userStates,
			String sort, boolean ascending, Integer pageNumber, Integer pageSize) {
		long startTime = System.currentTimeMillis();
		List<NormalUser> results = new ArrayList<NormalUser>();
		Criteria criteria = createNormalUsersCriteria(nick, emailAddress, pwd, userStates, sort, ascending);
		criteria.setFirstResult(pageSize * (pageNumber - 1));
		criteria.setMaxResults(pageSize);
		results = criteria.list();
		long endTime = System.currentTimeMillis();
		if (log.isDebugEnabled()) {
			log.debug("query took " + (endTime - startTime) + " ms to read " + results.size() + " objects");
		}

		return results;
	}

	private Criteria createNormalUsersCriteria(String nick, String emailAddress, String pwd,
			Collection<String> userStates, String sortValue, boolean ascending) {
		Criteria criteria = getSession().createCriteria(getPersistentClass(), "normalUser");
		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		if (userStates.size() > 0) {
			criteria = criteria.createAlias("normalUser.userState", "userState");
			Disjunction disjunction = Restrictions.disjunction();
			for (String userState : userStates) {
				disjunction.add(Restrictions.eq("userState.state", userState));
			}
			criteria.add(disjunction);
		}
		if (StringUtils.isNotBlank(nick)) {
			criteria.add(Restrictions.like("normalUser.nick", nick, MatchMode.EXACT));
		}
		if (StringUtils.isNotBlank(emailAddress)) {
			criteria.add(Restrictions.like("normalUser.emailAddress", emailAddress, MatchMode.EXACT));
		}
		
		if (StringUtils.isNotBlank(pwd)) {
			criteria.add(Restrictions.like("normalUser.pwd", pwd, MatchMode.EXACT));
		}
		if ("nick".equals(sortValue)) {
			if (ascending) {
				criteria.addOrder(Order.asc("normalUser.nick"));
			} else {
				criteria.addOrder(Order.desc("normalUser.nick"));
			}
		} else if ("emailAddress".equals(sortValue)) {
			if (ascending) {
				criteria.addOrder(Order.asc("normalUser.emailAddress"));
			} else {
				criteria.addOrder(Order.desc("normalUser.emailAddress"));
			}
		} else if ("pwd".equals(sortValue)) {
			if (ascending) {
				criteria.addOrder(Order.asc("normalUser.pwd"));
			} else {
				criteria.addOrder(Order.desc("normalUser.pwd"));
			}
		}
		return criteria;
	}
}