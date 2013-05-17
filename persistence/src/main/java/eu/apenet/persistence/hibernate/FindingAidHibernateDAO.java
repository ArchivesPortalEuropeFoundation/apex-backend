package eu.apenet.persistence.hibernate;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;

import eu.apenet.persistence.dao.FindingAidDAO;
import eu.apenet.persistence.vo.ArchivalInstitution;
import eu.apenet.persistence.vo.CLevel;
import eu.apenet.persistence.vo.EadContent;
import eu.apenet.persistence.vo.FindingAid;
import eu.apenet.persistence.vo.HoldingsGuide;
import eu.apenet.persistence.vo.ValidatedState;

public class FindingAidHibernateDAO extends AbstractHibernateDAO<FindingAid, Integer> implements FindingAidDAO {

	private final Logger log = Logger.getLogger(getClass());


	@SuppressWarnings("unchecked")
	public List<FindingAid> getFindingAidsNotLinked(Integer aiId) {
		long startTime = System.currentTimeMillis();
		List<FindingAid> results = new ArrayList<FindingAid>();
		Criteria criteria = getSession().createCriteria(getPersistentClass(),"findingAid");
		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		DetachedCriteria subQuery = DetachedCriteria.forClass(CLevel.class,"cLevel");
			subQuery.setProjection(Property.forName("cLevel.hrefEadid"));
			subQuery.add(Restrictions.isNotNull("cLevel.hrefEadid"));
				DetachedCriteria subQuery2 = DetachedCriteria.forClass(EadContent.class,"eadContent");
				subQuery2.setProjection(Property.forName("eadContent.ecId"));
					DetachedCriteria subQuery3 = DetachedCriteria.forClass(HoldingsGuide.class, "holdingsGuide");
					subQuery3.setProjection(Property.forName("holdingsGuide.id"));
					subQuery3.add(Restrictions.eq("holdingsGuide.archivalInstitution.aiId", aiId));
				subQuery2.add(Subqueries.propertyIn("eadContent.hgId", subQuery3));
			subQuery.add(Subqueries.propertyIn("cLevel.ecId", subQuery2));
		criteria.add(Subqueries.propertyNotIn("findingAid.eadid", subQuery));
		criteria.add(Restrictions.eq("findingAid.archivalInstitution.aiId", aiId));
		criteria.add(Restrictions.eq("findingAid.validated", ValidatedState.VALIDATED));
		results = criteria.list();
		long endTime = System.currentTimeMillis();
		if (log.isDebugEnabled()) {
			log.debug("query took " + (endTime - startTime) + " ms to read " + results.size() + " objects");
		}

		return results;
	}



	@Override
	/**
	 * This method return a list with all finding aids related with a holding guide
	 */
	@SuppressWarnings("unchecked")
	public List<FindingAid> getAllFindingAidByHgId(HoldingsGuide holdingsGuide) {
		Criteria criteria = getSession().createCriteria(getPersistentClass(), "findingAid");
		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria.add(Restrictions.eq("holdingsGuide", holdingsGuide));
		List<FindingAid> list = criteria.list();
		return list;
	}

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


	@Override
	public Long getFindingAidsLinkedByHoldingsGuide(HoldingsGuide holdingsGuide) {
		Criteria criteria = getSession().createCriteria(getPersistentClass(), "findingAid");
		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria.add(Restrictions.eq("holdingsGuide",holdingsGuide));
		criteria.setProjection(Projections.count("findingAid.id"));
		return (Long)criteria.uniqueResult();
	}
	

	
	/**
	 * This function get all the finding aids within an indexed holdings guide 
	 * (they can be indexed or not), it's useful to replace the linking
	 * finding aids with holdings guide.
	 */
	@Override
	public List<FindingAid> getFindingAidsByHoldingsGuideId(Integer hgId,boolean published, Integer pageSize, Integer pageNumber){
		Criteria criteria = createCriteriaForFindingAidsByHoldingsGuide(hgId,true,null);
		criteria.setFirstResult(pageSize * (pageNumber - 1));
		criteria.setMaxResults(pageSize);
		return (List<FindingAid>)criteria.list();
	}
	
	/**
	 * Get the total figure of finding aids indexed into a holdings guide 
	 */
	@Override
	public Long countFindingAidsByHoldingsGuideId(Integer hgId,boolean published){
		Criteria criteria = createCriteriaForFindingAidsByHoldingsGuide(hgId,true, null);
		criteria.setProjection(Projections.count("findingAid.id"));
		return (Long)criteria.uniqueResult();
	}
	
	private Criteria createCriteriaForFindingAidsByHoldingsGuide(Integer hgId,boolean published,Boolean isLinked) {
		Criteria criteria = getSession().createCriteria(getPersistentClass(),"findingAid");
		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		DetachedCriteria subQuery = DetachedCriteria.forClass(CLevel.class,"cLevel");
			subQuery.setProjection(Property.forName("cLevel.hrefEadid"));
			subQuery.add(Restrictions.isNotNull("cLevel.hrefEadid"));
				DetachedCriteria subQuery2 = DetachedCriteria.forClass(EadContent.class,"eadContent");
				if(hgId!=null){
					subQuery2.add(Restrictions.eq("eadContent.holdingsGuide.id", hgId));
				}else{
					DetachedCriteria subQuery3 = DetachedCriteria.forClass(HoldingsGuide.class,"holdingsGuide");
						subQuery3.setProjection(Property.forName("holdingsGuide.id"));
					subQuery2.add(Subqueries.propertyIn("eadContent.hgId",subQuery3));
				}
				subQuery2.setProjection(Property.forName("eadContent.ecId"));
			subQuery.add(Subqueries.propertyIn("cLevel.ecId", subQuery2));
		if(hgId!=null || (isLinked!=null && isLinked)){
			criteria.add(Subqueries.propertyIn("findingAid.eadid", subQuery));
		}else if((isLinked!=null && !isLinked)){
			criteria.add(Subqueries.propertyNotIn("findingAid.eadid", subQuery));
		}
		criteria.add(Restrictions.eq("findingAid.published", published));
		return criteria;
	}
	


	@Override
	public Long countFindingAidsIndexedByInternalArchivalInstitutionId(String identifier) {
		Criteria criteria = getSession().createCriteria(getPersistentClass(),"findingAid");
		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			DetachedCriteria subQuery = DetachedCriteria.forClass(ArchivalInstitution.class,"archivalInstitution");
			subQuery.add(Restrictions.eq("archivalInstitution.internalAlId",identifier));
			subQuery.setProjection(Property.forName("archivalInstitution.id"));
		criteria.add(Subqueries.propertyIn("findingAid.archivalInstitution.aiId", subQuery));
		criteria.add(Restrictions.eq("findingAid.published", true));
		criteria.setProjection(Projections.count("findingAid.id"));
		Object counter = criteria.uniqueResult();
		if(counter!=null){
			return (Long)counter;
		}else{
			return new Long(0);
		}
	}
}
