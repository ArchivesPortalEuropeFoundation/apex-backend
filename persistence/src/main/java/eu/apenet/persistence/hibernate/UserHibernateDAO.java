package eu.apenet.persistence.hibernate;

import java.util.List;

import eu.apenet.persistence.vo.Country;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

import eu.apenet.persistence.dao.UserDAO;
import eu.apenet.persistence.exception.PartnerException;
import eu.apenet.persistence.vo.User;
import eu.apenet.persistence.vo.UserRole;

/***
 * This class implements the specific methods and features in respect to Partner entitys with Hibernate(Criteria) technology.
 * @author paul
 */

public class UserHibernateDAO extends AbstractHibernateDAO<User, Integer> implements UserDAO{

	private final Logger log = Logger.getLogger(getClass());
	
	@SuppressWarnings("unchecked")
	public List<User> getPartnersByCountryAndByRoleType(Integer couId, String role) {
		log.debug("PartnerHibernateDAO: getPartnersByCountry() with couId:"+couId);
		long startTime = System.currentTimeMillis();		
		List<User> results = null;
		Criteria criteria = getSession().createCriteria(getPersistentClass(), "user");
		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		if (couId != null && role != null) {
			criteria = criteria.createAlias("user.userRole", "userRole");
			criteria.add(Restrictions.eq("user.countryId", couId.intValue()));
			criteria.add(Restrictions.eq("userRole.role", role));
		}	
		results = criteria.list();
		long endTime = System.currentTimeMillis();
		if (log.isDebugEnabled()) {
			log.debug("query took " + (endTime - startTime) + " ms to read " + results.toString() + " object");
		}
		return results;
	}


	@Override
	public User loginUser (String emailAddress, String pwd){
		Criteria criteria = getSession().createCriteria(getPersistentClass(), "user");
		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria.add(Restrictions.like("emailAddress", emailAddress, MatchMode.EXACT).ignoreCase());
		criteria.add(Restrictions.like("password", pwd, MatchMode.EXACT));
       return (User) criteria.uniqueResult();

	}
	


	
	@Override
	public User exitsEmailUser(String emailAddress) throws PartnerException {
		Criteria criteria = getSession().createCriteria(getPersistentClass(), "user");
		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria.add(Restrictions.like("emailAddress", emailAddress, MatchMode.EXACT).ignoreCase());
       return (User) criteria.uniqueResult();
	}
	
	



	@SuppressWarnings("unchecked")
	public List<User> getPartnersByRoleType(String role) {
		Criteria criteria = getSession().createCriteria(getPersistentClass(), "user");
		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		if (role != null) {
			criteria = criteria.createAlias("user.userRole", "userRole");			
			criteria.add(Restrictions.eq("userRole.role", role));
		}		
		return criteria.list();
	}

    public User getCountryManagerOfCountry(Country country) {
        Criteria criteria = getSession().createCriteria(getPersistentClass(), "user");
        criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        criteria = criteria.createAlias("user.userRole", "userRole");
        criteria.add(Restrictions.eq("userRole.role", UserRole.ROLE_COUNTRY_MANAGER));
        criteria.add(Restrictions.eq("user.countryId", country.getId()));
        return (User)criteria.uniqueResult();
    }

    @SuppressWarnings("unchecked")
    public boolean doesAdminExist() {
        Criteria criteria = getSession().createCriteria(getPersistentClass(), "user");
        criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        criteria = criteria.createAlias("user.userRole", "userRole");
        criteria.add(Restrictions.eq("userRole.role", UserRole.ROLE_ADMIN));
        List<User> results = criteria.list();
        return results.size() > 0;
    }
	
}