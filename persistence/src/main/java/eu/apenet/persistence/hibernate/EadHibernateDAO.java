package eu.apenet.persistence.hibernate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import eu.apenet.persistence.dao.EadDAO;
import eu.apenet.persistence.dao.EadSearchOptions;
import eu.apenet.persistence.vo.Ead;
import eu.apenet.persistence.vo.FileState;

public class EadHibernateDAO extends AbstractHibernateDAO<Ead, Integer> implements EadDAO {

	private static final Logger LOG = Logger.getLogger(EadHibernateDAO.class);

	@Override
	public Integer isEadidIndexed(String eadid, Integer aiId, Class<? extends Ead> clazz) {
		Criteria criteria = getSession().createCriteria(clazz, "ead").setProjection(Projections.property("id"));
		criteria.createAlias("ead.archivalInstitution", "archivalInstitution");
		criteria.add(Restrictions.eq("archivalInstitution.aiId", aiId));
		criteria.add(Restrictions.eq("eadid", eadid));
		criteria.add(Restrictions.eq("searchable", true));
		List<Integer> result = criteria.list();
		if (result.size() > 0)
			return result.get(0);
		return null;
	}
	@Override
	public Long countEads(Ead eadExample) {
		return countEads(eadExample, false);
	}
	private Long countEads(Ead eadExample, boolean allStates) {
		CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Long> cq = criteriaBuilder.createQuery(Long.class);
		Root<? extends Ead> from = cq.from(eadExample.getClass());
		cq.select(criteriaBuilder.countDistinct(from));
		List<Predicate> whereClause = new ArrayList<Predicate>();
		if (!allStates){
			whereClause.add(criteriaBuilder.equal(from.get("searchable"), eadExample.isSearchable()));
		}
		if (eadExample.getAiId() != null){
			whereClause.add(criteriaBuilder.equal(from.get("aiId"), eadExample.getAiId()));		 
		}
		if (eadExample.getEadid() != null){
			whereClause.add(criteriaBuilder.equal(from.get("eadid"), eadExample.getEadid()));		 
		}
		cq.where(criteriaBuilder.and(whereClause.toArray(new Predicate[0])));
		return getEntityManager().createQuery(cq).getSingleResult();
	}
	
	@Override
	public boolean existEads(Ead eadExample) {
		return existEads(eadExample, false);
	}
	private boolean existEads(Ead eadExample, boolean allStates) {
		CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Object> cq = criteriaBuilder.createQuery(Object.class);
		Root<? extends Ead> from = cq.from(eadExample.getClass());
		cq.select(from.get("id"));
		List<Predicate> whereClause = new ArrayList<Predicate>();
		if (!allStates){
			whereClause.add(criteriaBuilder.equal(from.get("searchable"), eadExample.isSearchable()));
		}
		if (eadExample.getAiId() != null){
			whereClause.add(criteriaBuilder.equal(from.get("aiId"), eadExample.getAiId()));		 
		}
		if (eadExample.getEadid() != null){
			whereClause.add(criteriaBuilder.equal(from.get("eadid"), eadExample.getEadid()));		 
		}
		cq.where(criteriaBuilder.and(whereClause.toArray(new Predicate[0])));
		TypedQuery<Object> query = getEntityManager().createQuery(cq);
		query.setMaxResults(1);
		List<Object> result = query.getResultList();
		return result.size()> 0;
	}
	@Override
	public List<Ead> getEads(Ead eadExample, int firstResult, int maxResult) {
		return getEads(eadExample, false, firstResult, maxResult);
	}
	private List<Ead> getEads(Ead eadExample, boolean allStates, int firstResult, int maxResult) {
		CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Ead> cq = criteriaBuilder.createQuery(Ead.class);
		Root<? extends Ead> from = cq.from(eadExample.getClass());
		cq.select(from);
		List<Predicate> whereClause = new ArrayList<Predicate>();
		if (!allStates){
			whereClause.add(criteriaBuilder.equal(from.get("searchable"), eadExample.isSearchable()));
		}
		if (eadExample.getAiId() != null){
			whereClause.add(criteriaBuilder.equal(from.get("aiId"), eadExample.getAiId()));		 
		}
		if (eadExample.getEadid() != null){
			whereClause.add(criteriaBuilder.equal(from.get("eadid"), eadExample.getEadid()));		 
		}
		cq.where(criteriaBuilder.and(whereClause.toArray(new Predicate[0])));
		TypedQuery<Ead> query = getEntityManager().createQuery(cq);
		query.setMaxResults(maxResult);
		query.setFirstResult(firstResult);
		return query.getResultList();
	}

	@Override
	public List<Ead> getEads(EadSearchOptions eadSearchOptions) {
		CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Ead> cq = criteriaBuilder.createQuery(Ead.class);
		Root<? extends Ead> from = cq.from(eadSearchOptions.getEadClazz());
		buildFromQuery(from, eadSearchOptions);
		cq.select(from);
		TypedQuery<Ead> query = getEntityManager().createQuery(cq);
		query.setMaxResults(eadSearchOptions.getPageSize());
		query.setFirstResult(eadSearchOptions.getPageSize()* (eadSearchOptions.getPageNumber()-1));
		return query.getResultList();
	}
	
	@Override
	public Long countEads(EadSearchOptions eadSearchOptions) {
		CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Long> cq = criteriaBuilder.createQuery(Long.class);
		Root<? extends Ead> from = cq.from(eadSearchOptions.getEadClazz());
		buildFromQuery(from, eadSearchOptions);
		cq.select(criteriaBuilder.countDistinct(from));
		return getEntityManager().createQuery(cq).getSingleResult();
	}
	private void buildFromQuery(Root<? extends Ead> from, EadSearchOptions eadSearchOptions) {
//		List<Predicate> whereClause = new ArrayList<Predicate>();
//		if (!allStates){
//			whereClause.add(criteriaBuilder.equal(from.get("searchable"), eadExample.isSearchable()));
//		}
//		if (eadExample.getAiId() != null){
//			whereClause.add(criteriaBuilder.equal(from.get("aiId"), eadExample.getAiId()));		 
//		}
//		if (eadExample.getEadid() != null){
//			whereClause.add(criteriaBuilder.equal(from.get("eadid"), eadExample.getEadid()));		 
//		}
//		cq.where(criteriaBuilder.and(whereClause.toArray(new Predicate[0])));
	}
	@Override
	public Integer isEadidUsed(String eadid, Integer aiId, Class<? extends Ead> clazz) {
		Criteria criteria = getSession().createCriteria(clazz, "ead").setProjection(Projections.property("id"));
		criteria.createAlias("ead.archivalInstitution", "archivalInstitution");
		criteria.add(Restrictions.eq("archivalInstitution.aiId", aiId));
		criteria.add(Restrictions.eq("eadid", eadid));
		List<Integer> result = criteria.list();
		if (result.size() > 0)
			return result.get(0);
		return null;
	}

    @Override
    @SuppressWarnings("unchecked")
    public List<Ead> getEadsByStates(Collection<String> fileStates, Class<? extends Ead> clazz) {
        long startTime = System.currentTimeMillis();
		Criteria criteria = getSession().createCriteria(clazz, "ead");
		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		if (fileStates.size() > 0) {
			criteria = criteria.createAlias("ead.fileState", "fileState");
			Disjunction disjunction = Restrictions.disjunction();
			for (String fileState : fileStates) {
				disjunction.add(Restrictions.eq("fileState.state", fileState));
			}
			criteria.add(disjunction);
		}
		List<Ead> results = criteria.list();
		long endTime = System.currentTimeMillis();
        LOG.debug("query took " + (endTime - startTime) + " ms to read " + results.size() + " objects");
		return results;
    }

	@Override
	@SuppressWarnings("unchecked")
	public List<Ead> getEadPage(Collection<String> fileStates, Integer aiId, String sort, boolean ascending,
			Integer pageNumber, Integer pageSize, Class<? extends Ead> clazz) {
		long startTime = System.currentTimeMillis();
		Criteria criteria = createCriteriaGetEadPage(fileStates, aiId, sort, ascending, clazz);
		criteria.setFirstResult(pageSize * pageNumber);
		criteria.setMaxResults(pageSize);
		List<Ead> results = criteria.list();
		long endTime = System.currentTimeMillis();
		LOG.debug("query took " + (endTime - startTime) + " ms to read " + results.size() + " objects");
		return results;
	}

	private Criteria createCriteriaGetEadPage(Collection<String> fileStates, Integer aiId, String sortValue,
			boolean ascending, Class<? extends Ead> clazz) {
		Criteria criteria = getSession().createCriteria(clazz, "ead");
		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria = criteria.createAlias("ead.uploadMethod", "uploadMethod");
		if (fileStates != null && !fileStates.isEmpty()) {
			criteria = criteria.createAlias("ead.fileState", "fileState");
			Disjunction disjunction = Restrictions.disjunction();
			for (String fileState : fileStates) {
				disjunction.add(Restrictions.eq("fileState.state", fileState));
			}
			criteria.add(disjunction);
		}
		if (aiId != null)
			criteria.add(Restrictions.eq("archivalInstitution.aiId", aiId));

		if ("eadid".equals(sortValue)) {
			if (ascending)
				criteria.addOrder(Order.asc("ead.eadid"));
			else
				criteria.addOrder(Order.desc("ead.eadid"));
		} else if ("uploadMethod".equals(sortValue)) {
			if (ascending)
				criteria.addOrder(Order.asc("uploadMethod.method"));
			else
				criteria.addOrder(Order.desc("uploadMethod.method"));
		} else if ("title".equals(sortValue)) {
			if (ascending) {
				criteria.addOrder(Order.asc("ead.title"));
			} else {
				criteria.addOrder(Order.desc("ead.title"));
			}
		} else if ("uploadDate".equals(sortValue)) {
			if (ascending) {
				criteria.addOrder(Order.asc("ead.uploadDate"));
				criteria.addOrder(Order.asc("ead.eadid"));
				criteria.addOrder(Order.asc("ead.title"));
			} else {
				criteria.addOrder(Order.desc("ead.uploadDate"));
				criteria.addOrder(Order.asc("ead.eadid"));
				criteria.addOrder(Order.asc("ead.title"));
			}
		}
		return criteria;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Ead> getMatchEads(String queries, Integer ai, Integer page, Integer pageSize, String orderBy,
			Boolean orderDecreasing, Class<? extends Ead> clazz) {
		Criteria criteria = getSession().createCriteria(clazz, "ead");
		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria.setFirstResult(page * pageSize);
		criteria.setMaxResults(pageSize);
		String[] query = StringUtils.split(queries, " ");
		for (String aQuery : query) {
			criteria.add(Restrictions.or(Restrictions.like("title", "%" + aQuery + "%"),
					Restrictions.like("eadid", "%" + aQuery + "%")));
		}
		if (orderDecreasing)
			criteria.addOrder(Order.desc(orderBy));
		else
			criteria.addOrder(Order.asc(orderBy));
		criteria.add(Restrictions.eq("archivalInstitution.aiId", ai));
		return criteria.list();
	}

	@Override
	public Long getMatchCountEads(String queries, Integer ai, Class<? extends Ead> clazz) {
		Criteria criteria = getSession().createCriteria(clazz, "ead");
		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		String[] query = StringUtils.split(queries, " ");
		for (String aQuery : query) {
			criteria.add(Restrictions.or(Restrictions.like("title", "%" + aQuery + "%"),
					Restrictions.like("eadid", "%" + aQuery + "%")));
		}
		criteria.add(Restrictions.eq("archivalInstitution.aiId", ai));
		return (long) criteria.list().size();
	}
	@Override
	@SuppressWarnings("unchecked")
	public List<Ead> getEadsByAiId(Class<? extends Ead> clazz, Integer aiId) {
		Criteria criteria = getSession().createCriteria(clazz, "ead");
		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria = criteria.createAlias("ead.archivalInstitution", "archivalInstitution");
		criteria.add(Restrictions.eq("archivalInstitution.aiId", aiId));
		
		List<Ead> list = criteria.list();
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Ead getEadByEadid(Class<? extends Ead> clazz, Integer aiId,
			String eadid) {
		Criteria criteria = getSession().createCriteria(clazz, "ead");
		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria = criteria.createAlias("ead.archivalInstitution", "archivalInstitution");
		criteria.add(Restrictions.eq("archivalInstitution.aiId", aiId));
		criteria.add(Restrictions.eq("eadid", eadid));	
		List<Ead> list = criteria.list();
		if (list.size() > 0)
			return list.get(0);
		return null;
	}

    @SuppressWarnings("unchecked")
	@Override
    public List<Integer> getAllIds(Class<? extends Ead> clazz, Integer aiId) {
        Criteria criteria = getSession().createCriteria(clazz, "ead");
        criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        criteria = criteria.createAlias("ead.archivalInstitution", "archivalInstitution");
		criteria.add(Restrictions.eq("archivalInstitution.aiId", aiId));
        criteria.setProjection(Projections.property("id"));
        return criteria.list();
    }
	
    @SuppressWarnings({ "unchecked"})
	public Long getTotalCountOfUnits(){

    	Criteria criteria = null;
    	List<Long> result=null;
    	Long value=0L;

		criteria = getSession().createCriteria(getPersistentClass(), "ead");
		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		result = criteria.setProjection(Projections.sum("totalNumberOfUnits")).list();
		if(result.size() > 0)
		{
			int i=0;			
			while(result.size()>i)	
			{
				if (result.get(i)!=null){
					value += result.get(i);
					}
				i++;
			}
		}
		return value;

	}
    
    @SuppressWarnings({ "unchecked"})
	public Long getTotalCountOfUnitsWithDao(){

    	Criteria criteria = null;
    	List<Long> result=null;
    	Long value=0L;

		criteria = getSession().createCriteria(getPersistentClass(), "ead");
		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		result = criteria.setProjection(Projections.sum("totalNumberOfUnitsWithDao")).list();
		if(result.size() > 0){
			int i=0;			
			while(result.size()>i)	
			{
				if (result.get(i)!=null){
					value += result.get(i);					
				}
				i++;
			}
		}
		return value;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Long countFilesbyInstitution(Class<? extends Ead> clazz, Integer aiId) {
		
		Criteria criteria = getSession().createCriteria(clazz, "ead");
		criteria = criteria.add(Restrictions.eq("archivalInstitution.aiId", aiId));
		List<Long> result = criteria.setProjection(Projections.countDistinct("id")).list();
		Long value =0L;
		
		if (result.size()>0)	
				value = result.get(0);
			
		return value;
	}
}
