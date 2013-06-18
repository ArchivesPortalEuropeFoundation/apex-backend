package eu.apenet.persistence.hibernate;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import eu.apenet.persistence.dao.FindingAidDAO;
import eu.apenet.persistence.vo.FindingAid;

public class FindingAidHibernateDAO extends AbstractHibernateDAO<FindingAid, Integer> implements FindingAidDAO {

	private final Logger log = Logger.getLogger(getClass());


	@Override
	public Long getTotalCountOfUnits(){
		Criteria criteria = getSession().createCriteria(getPersistentClass(), "findingAid");
		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria.setProjection(Projections.sum("findingAid.totalNumberOfUnits")); 
		return (Long)criteria.uniqueResult();
	}
	
	@Override
	public Long getTotalCountOfUnits(Integer aiId){
		Criteria criteria = getSession().createCriteria(getPersistentClass(), "findingAid");
		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria.setProjection(Projections.sum("findingAid.totalNumberOfUnits"));
		if(aiId!=null && aiId>0){
			criteria.add(Restrictions.eq("findingAid.archivalInstitution.aiId", aiId));
		}
		return (Long)criteria.uniqueResult();
	}

	

}
