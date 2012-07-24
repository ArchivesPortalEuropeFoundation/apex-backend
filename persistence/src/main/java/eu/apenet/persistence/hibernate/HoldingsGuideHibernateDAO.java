package eu.apenet.persistence.hibernate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;

import eu.apenet.persistence.dao.HoldingsGuideDAO;
import eu.apenet.persistence.vo.ArchivalInstitution;
import eu.apenet.persistence.vo.CLevel;
import eu.apenet.persistence.vo.EadContent;
import eu.apenet.persistence.vo.FileState;
import eu.apenet.persistence.vo.HoldingsGuide;



public class HoldingsGuideHibernateDAO extends AbstractHibernateDAO<HoldingsGuide, Integer> implements HoldingsGuideDAO {

	private final Logger log = Logger.getLogger(getClass());

	@SuppressWarnings("unchecked")
	public List<HoldingsGuide> getHoldingsGuides(String hgEadid, String hgTitle, String uploadMethod,
			Collection<String> fileStates, Integer aiId, String sort, boolean ascending) {
		long startTime = System.currentTimeMillis();
		List<HoldingsGuide> results = new ArrayList<HoldingsGuide>();
		Criteria criteria = createHoldingsGuidesCriteria(hgEadid, hgTitle, uploadMethod, fileStates, aiId, sort, ascending);
		results = criteria.list();
		long endTime = System.currentTimeMillis();

		if (log.isDebugEnabled()) {
			log.debug("query took " + (endTime - startTime) + " ms to read " + results.size() + " objects");
		}

		return results;
	}

	private Criteria createHoldingsGuidesCriteria(String hgEadid, String hgTitle, String uploadMethod,
			Collection<String> fileStates, Integer aiId, String sortValue, boolean ascending) {
		Criteria criteria = getSession().createCriteria(getPersistentClass(), "holdingsGuide");
		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria = criteria.createAlias("holdingsGuide.uploadMethod", "uploadMethod");
		if (fileStates.size() > 0) {
			criteria = criteria.createAlias("holdingsGuide.fileState", "fileState");
			Disjunction disjunction = Restrictions.disjunction();
			for (String fileState : fileStates) {
				disjunction.add(Restrictions.eq("fileState.state", fileState));
			}
			criteria.add(disjunction);
		}
		if (StringUtils.isNotBlank(uploadMethod)) {
			criteria.add(Restrictions.like("uploadMethod.method", uploadMethod, MatchMode.ANYWHERE));
		}
		if (StringUtils.isNotBlank(hgEadid)) {
			criteria.add(Restrictions.like("holdingsGuide.eadid", hgEadid, MatchMode.ANYWHERE));
		}
		if (StringUtils.isNotBlank(hgTitle)) {
			criteria.add(Restrictions.like("holdingsGuide.title", hgTitle, MatchMode.ANYWHERE));
		}
		if (aiId != null) {
			criteria.add(Restrictions.eq("archivalInstitution.aiId", aiId.intValue()));
		}
		
		if ("hgEadid".equals(sortValue)) {
			if (ascending) {
				criteria.addOrder(Order.asc("holdingsGuide.eadid"));
			} else {
				criteria.addOrder(Order.desc("holdingsGuide.eadid"));
			}
		} else if ("uploadMethod".equals(sortValue)) {
			if (ascending) {
				criteria.addOrder(Order.asc("uploadMethod.method"));
			} else {
				criteria.addOrder(Order.desc("uploadMethod.method"));
			}
		} else if ("hgTittle".equals(sortValue)) {
			if (ascending) {
				criteria.addOrder(Order.asc("holdingsGuide.title"));
			} else {
				criteria.addOrder(Order.desc("holdingsGuide.title"));
			}
		} else if ("uploadDate".equals(sortValue)) {
			if (ascending) {
				criteria.addOrder(Order.asc("holdingsGuide.uploadDate"));
				criteria.addOrder(Order.asc("holdingsGuide.eadid"));
				criteria.addOrder(Order.asc("holdingsGuide.title"));
			} else {
				criteria.addOrder(Order.desc("holdingsGuide.uploadDate"));
				criteria.addOrder(Order.asc("holdingsGuide.eadid"));
				criteria.addOrder(Order.asc("holdingsGuide.title"));
			}
		}
		return criteria;
	}

	@Override
	public Long countHoldingsGuideByArchivalInstitution(Integer ai, Collection<String> fileStates){
		Criteria criteria = getSession().createCriteria(getPersistentClass(), "holdingsGuide");
		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria.add(Restrictions.eq("holdingsGuide.archivalInstitution.aiId", ai));
		if (fileStates.size() > 0) {
			criteria = criteria.createAlias("holdingsGuide.fileState", "fileState");
			Disjunction disjunction = Restrictions.disjunction();
			for (String fileState : fileStates) {
				disjunction.add(Restrictions.eq("fileState.state", fileState));
			}
			criteria.add(disjunction);
		}
		criteria.setProjection(Projections.count("holdingsGuide.id"));
		return (Long)criteria.uniqueResult();
		
		//return new Long(getHoldingsGuides("","", "",new ArrayList<String>(), ai, "uploadDate", true).size());
	}
		
	@SuppressWarnings("unchecked")
	@Override
	public List<HoldingsGuide> getHoldingsGuidePage(String hgEadid,String hgTitle, String uploadMethod, Collection<String> fileStates,
			Integer aiId, String sort, boolean ascending, Integer pageNumber,Integer pageSize) {
		long startTime = System.currentTimeMillis();
		List<HoldingsGuide> results = new ArrayList<HoldingsGuide>();
		Criteria criteria = createHoldingsGuidesCriteria(hgEadid, hgTitle, uploadMethod, fileStates, aiId, sort, ascending);
		criteria.setFirstResult(pageSize * (pageNumber));
		criteria.setMaxResults(pageSize);
		results = criteria.list();
		long endTime = System.currentTimeMillis();
		if (log.isDebugEnabled()) {
			log.debug("query took " + (endTime - startTime) + " ms to read " + results.size() + " objects");
		}

		return results;
	}

	@Override
	public List<HoldingsGuide> getMatchHoldingsGuides(String queries, Integer ai, Integer page, Integer option, String orderBy, Boolean orderDecreasing) {
		Criteria criteria = getSession().createCriteria(getPersistentClass(), "holdingsGuide");
		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria.setFirstResult(page*4);
		criteria.setMaxResults(4);
		String[] query = StringUtils.split(queries," ");
		for(int i=0;i<query.length;i++){
			criteria.add(Restrictions.or(
					Restrictions.like("title", "%"+query[i]+"%"),
					Restrictions.like("eadid", "%"+query[i]+"%")
				));
		}
		if(!orderDecreasing){
			criteria.addOrder(Order.asc(orderBy));
		}else{
			criteria.addOrder(Order.desc(orderBy));
		}		
		criteria.add(Restrictions.eq("archivalInstitution.aiId", ai.intValue()));
		return criteria.list();
	}

	@Override
	public Long getMatchCountHoldingsGuides(String queries, Integer ai, Integer page, Integer option) {
		Criteria criteria = getSession().createCriteria(getPersistentClass(), "holdingsGuide");
		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		String[] query = StringUtils.split(queries," ");
		if(option==0){
			for(int i=0;i<query.length;i++){
				criteria.add(Restrictions.or(
						Restrictions.like("title", "%" + query[i] + "%"),
						Restrictions.like("eadid", "%" + query[i] + "%")));
			}
		}else if(option==1){
			for(int i=0;i<query.length;i++){
				criteria.add(Restrictions.like("eadid", "%" + query[i] + "%"));
			}
		}else if(option==2){
			for(int i=0;i<query.length;i++){
				criteria.add(Restrictions.like("title", "%" + query[i] + "%"));
			}
		}
		criteria.add(Restrictions.eq("archivalInstitution.aiId", ai.intValue()));
		return new Long(criteria.list().size());
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<HoldingsGuide> getHoldingsGuides(Integer aiId) {
		long startTime = System.currentTimeMillis();
		List<HoldingsGuide> results = new ArrayList<HoldingsGuide>();
		Criteria criteria = createHoldingsGuidesCriteria(aiId);
		results = criteria.list();
		long endTime = System.currentTimeMillis();
		if (log.isDebugEnabled()) {
			log.debug("query took " + (endTime - startTime) + " ms to read " + results.size() + " objects");
		}

		return results;
	}

	private Criteria createHoldingsGuidesCriteria(Integer aiId) {
		Criteria criteria = getSession().createCriteria(getPersistentClass(), "holdingsGuide");
		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		if (aiId != null) {
			criteria.add(Restrictions.eq("archivalInstitution.aiId", aiId.intValue()));
		}
		return criteria;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<HoldingsGuide> getHoldingsGuidesByStateAndArchivalInstitution(Integer ai, Collection<String> fileStates) {
		long startTime = System.currentTimeMillis();
		Criteria criteria = createHoldingsGuideCriteria(ai, fileStates);
		List<HoldingsGuide> results = criteria.list();
		long endTime = System.currentTimeMillis();
		if (log.isDebugEnabled()) {
			log.debug("query took " + (endTime - startTime) + " ms to read " + results.size() + " objects");
		}

		return results;
	}

	@Override
	public Long countHoldingsGuide(Integer ai, Collection<String> fileStates) {
		Criteria criteria = createHoldingsGuideCriteria(ai, fileStates);
		criteria.setProjection(Projections.countDistinct("holdingsGuide.id"));
		Object object = criteria.list().get(0);
		if (object instanceof Integer) {
			return ((Integer) object).longValue();
		} else if (object instanceof Long) {
			return (Long) object;
		}
		return (Long) new Long(0);
	}
	
	private Criteria createHoldingsGuideCriteria(Integer aiId, Collection<String> fileStates) {
		Criteria criteria = getSession().createCriteria(getPersistentClass(), "holdingsGuide");
		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		if (aiId != null) {
			criteria.add(Restrictions.eq("archivalInstitution.aiId", aiId.intValue()));
		}
		if (fileStates != null && fileStates.size() > 0) {
			criteria = criteria.createAlias("holdingsGuide.fileState", "fileState");
			Disjunction disjunction = Restrictions.disjunction();
			for (String fileState : fileStates) {
				disjunction.add(Restrictions.eq("fileState.state", fileState));
			}
			criteria.add(disjunction);
		}
		return criteria;
	}

	@Override
	public Long getTotalCountOfUnits(){
		Criteria criteria = getSession().createCriteria(getPersistentClass(), "holdingsGuide");
		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria.setProjection(Projections.sum("holdingsGuide.totalNumberOfUnits")); //select sum()
		return (Long)criteria.uniqueResult();
	}
	
	public Long countHoldingsGuide(List<ArchivalInstitution> archivalInstitutions, Collection<String> fileStates) {
		Criteria criteria = createCountHoldingsGuideCriteria(archivalInstitutions, fileStates);
		Object object = criteria.list().get(0);
		if (object instanceof Integer) {
			return ((Integer) object).longValue();
		} else if (object instanceof Long) {
			return (Long) object;		}
		return (Long) new Long(0);
	}
	
	private Criteria createCountHoldingsGuideCriteria(List<ArchivalInstitution> archivalInstitutions, Collection<String> fileStates) {
		Criteria criteria = getSession().createCriteria(getPersistentClass(), "holdingsGuide");
		criteria.setProjection(Projections.countDistinct("archivalInstitution.aiId"));
		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		
		if (archivalInstitutions.size() > 0) {
			Disjunction disjunction = Restrictions.disjunction();
			for (int i = 0; i < archivalInstitutions.size(); i ++) {
				disjunction.add(Restrictions.eq("archivalInstitution", archivalInstitutions.get(i)));
			}
			criteria.add(disjunction);
		}
		
		if (fileStates != null && fileStates.size() > 0) {
			criteria = criteria.createAlias("holdingsGuide.fileState", "fileState");
			Disjunction disjunction = Restrictions.disjunction();
			for (String fileState : fileStates) {
				disjunction.add(Restrictions.eq("fileState.state", fileState));
			}
			criteria.add(disjunction);
		}
		return criteria;
	}
	
	/**
	 * Returns the title of a holdings guide indexed into the system by a finding_aid.eadid
	 * and a archival institution id.
	 */
	@Override
	public String getLinkedHoldingsGuideTitleByFindingAidEadid(String eadid,Integer aiId) {
		Criteria criteria = getSession().createCriteria(getPersistentClass(), "holdingsGuide");
		criteria.setProjection(Property.forName("holdingsGuide.eadid"));
			DetachedCriteria subQuery = DetachedCriteria.forClass(EadContent.class,"eadContent");
			subQuery.setProjection(Property.forName("eadContent.hgId"));
				DetachedCriteria subQuery2 = DetachedCriteria.forClass(CLevel.class,"cLevel");
				subQuery2.add(Restrictions.eq("cLevel.hrefEadid", eadid));
				subQuery2.setProjection(Property.forName("cLevel.ecId"));
			subQuery.add(Subqueries.propertyIn("eadContent.ecId", subQuery2));
		criteria.add(Subqueries.propertyIn("holdingsGuide.id", subQuery));
		criteria = setFileStates(criteria,Arrays.asList(FileState.INDEXED_FILE_STATES));
		if(aiId!=null){
			criteria.add(Restrictions.eq("holdingsGuide.archivalInstitution.aiId", aiId));
		}
		List<?> object = criteria.list();
		if(object!=null && !object.isEmpty()){
			return (String)object.get(0).toString();
		}
		return null;
	}
	
	private Criteria setFileStates(Criteria criteria, Collection<String> fileStates) {
		if (fileStates!= null && fileStates.size() > 0) {
			criteria = criteria.createAlias("holdingsGuide.fileState", "fileState");
			Disjunction disjunction = Restrictions.disjunction();
			for (String fileState : fileStates) {
				disjunction.add(Restrictions.eq("fileState.state", fileState));
			}
			criteria.add(disjunction);
		}
		return criteria;
	}
}
