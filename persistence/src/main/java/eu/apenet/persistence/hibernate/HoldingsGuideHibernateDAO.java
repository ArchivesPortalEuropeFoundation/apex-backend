package eu.apenet.persistence.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import eu.apenet.persistence.dao.HoldingsGuideDAO;
import eu.apenet.persistence.vo.HoldingsGuide;



public class HoldingsGuideHibernateDAO extends AbstractHibernateDAO<HoldingsGuide, Integer> implements HoldingsGuideDAO {

	@Override
	public List<HoldingsGuide> getHoldingsGuidesByArchivalInstitutionId(Integer aiId,Boolean indexed){
		List<HoldingsGuide> listHoldingsGuide = null;
		if(aiId!=null){
			Criteria criteria = getSession().createCriteria(getPersistentClass(), "holdingsGuide");
			criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			criteria.add(Restrictions.eq("holdingsGuide.aiId",aiId));
			if(indexed != null){
				criteria.add(Restrictions.eq("holdingsGuide.published",indexed));
			}
			listHoldingsGuide = criteria.list();
		}
		return listHoldingsGuide;
	}

	public List<HoldingsGuide> getHoldingsGuidesByArchivalInstitutionId(Integer aiId){
		return getHoldingsGuidesByArchivalInstitutionId(aiId, null);
	}
}
