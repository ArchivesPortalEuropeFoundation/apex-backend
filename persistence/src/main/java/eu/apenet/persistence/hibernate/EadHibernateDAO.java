package eu.apenet.persistence.hibernate;

import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import eu.apenet.persistence.dao.EadDAO;
import eu.apenet.persistence.vo.Ead;
import eu.apenet.persistence.vo.FileState;
import eu.apenet.persistence.vo.FindingAid;
import eu.apenet.persistence.vo.HoldingsGuide;
import eu.apenet.persistence.vo.SourceGuide;

public class EadHibernateDAO extends AbstractHibernateDAO<Ead, Integer> implements EadDAO {

	private static final Logger LOG = Logger.getLogger(EadHibernateDAO.class);

	@Override
	@SuppressWarnings("unchecked")
	public Integer isEadidIndexed(String eadid, Integer aiId, Class<? extends Ead> clazz) {
		Criteria criteria = getSession().createCriteria(clazz, "ead").setProjection(Projections.property("id"));
		criteria.createAlias("ead.archivalInstitution", "archivalInstitution");
		criteria.add(Restrictions.eq("archivalInstitution.aiId", aiId));
		criteria.add(Restrictions.eq("eadid", eadid));
		criteria = criteria.createAlias("ead.fileState", "fileState");
		Disjunction disjunction = Restrictions.disjunction();
		for (String fileState : FileState.INDEXED_FILE_STATES) {
			disjunction.add(Restrictions.eq("fileState.state", fileState));
		}
		criteria.add(disjunction);
		List<Integer> result = criteria.list();
		if (result.size() > 0)
			return result.get(0);
		return null;
	}

	@Override
	@SuppressWarnings("unchecked")
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
