package eu.apenet.persistence.hibernate;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import eu.apenet.persistence.dao.EseStateDAO;
import eu.apenet.persistence.vo.EseState;

public class EseStateHibernateDAO extends AbstractHibernateDAO<EseState, Integer> implements EseStateDAO {

	@Override
	public EseState getEseStateByState(String state) {
		Criteria criteria = getSession().createCriteria(getPersistentClass(), "eseState");
		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria.add(Restrictions.eq("state", state));
		return (EseState) criteria.uniqueResult();
	}

}
