package eu.apenet.persistence.hibernate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import eu.apenet.persistence.dao.EseDAO;
import eu.apenet.persistence.vo.Ese;
import eu.apenet.persistence.vo.EseState;
import eu.apenet.persistence.vo.FindingAid;
import eu.apenet.persistence.vo.MetadataFormat;

public class EseHibernateDAO extends AbstractHibernateDAO<Ese, Integer> implements EseDAO {

	private final Logger log = Logger.getLogger(getClass());
	
	@SuppressWarnings("unchecked")
	public List<Ese> getEses(Integer faId) {
		long startTime = System.currentTimeMillis();
		List<Ese> results = new ArrayList<Ese>();
		Criteria criteria = createEseCriteria(faId);
		results = criteria.list();
		long endTime = System.currentTimeMillis();
		if (log.isDebugEnabled()) {
			log.debug("query took " + (endTime - startTime) + " ms to read " + results.size() + " objects");
		}

		return results;
	}

	private Criteria createEseCriteria(Integer faId) {
		Criteria criteria = getSession().createCriteria(getPersistentClass(), "ese");
		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria = criteria.createAlias("ese.findingAid", "findingAid");

		if (faId != null) {
			criteria.add(Restrictions.eq("findingAid.id", faId.intValue()));
		}

		return criteria;
	}
	
	@SuppressWarnings("unchecked")	
	@Override
	public List<Ese> getEsesFromDeletedFindingaids(String oaiIdentifier) {
		long startTime = System.currentTimeMillis();
		List<Ese> results = new ArrayList<Ese>();
		Criteria criteria = getSession().createCriteria(getPersistentClass(), "ese");
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria.add(Restrictions.isNull("ese.path"));
		criteria.add(Restrictions.eq("ese.oaiIdentifier", oaiIdentifier));
		results = criteria.list();
		long endTime = System.currentTimeMillis();
		if (log.isDebugEnabled()) {
			log.debug("query took " + (endTime - startTime) + " ms to read " + results.size() + " objects");
		}

		return results;
	}

	@Override
	public Date getTheEarliestDatestamp(){
		Criteria criteria = getSession().createCriteria(getPersistentClass(),"ese");
		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria.setProjection(Projections.min("creationDate"));
		return (Date)criteria.uniqueResult();
	}
	@Override
	public List<Ese> getEsesByArguments(Date from,Date until,MetadataFormat metadataFormat,String set,Integer startElement,Integer limitPerResponse){
		Criteria criteria = getSession().createCriteria(getPersistentClass(),"ese");
		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		if(from!=null && until!=null){
			criteria.add(Restrictions.between("modificationDate", from, until));
		}else if(until!=null){
			criteria.add(Restrictions.le("modificationDate", until));
		}else if(from!=null){
			criteria.add(Restrictions.ge("modificationDate", from));
		}
		if(metadataFormat!=null){
			criteria.add(Restrictions.eq("metadataFormat",metadataFormat));
		}
		if(set!=null && !set.isEmpty()){
			/*
			if(set.endsWith(":")){
				criteria.add(Restrictions.ilike("eset",set+"%"));
			}else{
				criteria.add(Restrictions.like("eset",set));
			}
			*/
			criteria.add(Restrictions.like("eset",set + "%"));
		}
		Integer firstResult = null;
		if(startElement==null){
			firstResult = 0;
		}else{
			firstResult = startElement;
		}
		criteria.setFirstResult(firstResult);
		Integer limit = null;
		if(limitPerResponse==null){
			limit = 100;
		}else{
			limit = limitPerResponse+1;
		}
		criteria.setMaxResults(limit);
		return criteria.list();
	}

	@Override
	public Set<Ese> getAllEsesInSetFormat(){
		return null;
	}

	@Override
	public List<String> getSetsOfEses(Date startDate,Date endDate,String eset,Integer start,Integer maxResults) {
		Criteria criteria = getSession().createCriteria(getPersistentClass(),"ese");
		//criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria.add(Restrictions.eq("eseState.esId",2));//Published
		if(startDate!=null && endDate!=null){
			criteria.add(Restrictions.between("modificationDate", startDate, endDate));
		}else if(startDate!=null){
			criteria.add(Restrictions.le("modificationDate", startDate));
		}else if(endDate!=null){
			criteria.add(Restrictions.ge("modificationDate", endDate));
		}
		if(eset!=null){
			criteria.add(Restrictions.like("eset", eset));
		}if(start!=null){
			criteria.setFirstResult(start);
			if(maxResults!=null){
				criteria.setMaxResults(maxResults);
			}else{
				criteria.setMaxResults(101);
			}
		}
		return criteria.setProjection(Projections.distinct(Projections.property("eset"))).list();
	}

	@Override
	public List<Ese> getEsesWithoutFindingAid() {
		Criteria criteria = getSession().createCriteria(getPersistentClass(),"ese");
		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria.add(Restrictions.isNull("findingAid"));
		return criteria.list();
	}
	
	@Override
	public Ese getEseByIdentifierAndFormat(String identifier,MetadataFormat metadataFormat){
		Criteria criteria = getSession().createCriteria(getPersistentClass(),"ese");
		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		if(identifier!=null){
			criteria.add(Restrictions.like("oaiIdentifier",identifier));
		}
		if(metadataFormat!=null){
			criteria.add(Restrictions.eq("metadataFormat",metadataFormat));
		}
		if(identifier!=null){
			return (Ese) criteria.uniqueResult();
		}else if(metadataFormat!=null){
			List<Ese> tempList = criteria.list();
			if(tempList!=null && !tempList.isEmpty()){
				return (Ese) tempList.get(0);
			}
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Ese> getEsesByFindingAidAndState(Integer faId,EseState eseState){
		Criteria criteria = getSession().createCriteria(getPersistentClass(),"ese");
		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		if(faId!=null){
			criteria.add(Restrictions.eq("findingAid.id", faId));
		}
		if(eseState!=null){
			criteria.add(Restrictions.eq("eseState", eseState));
		}
		return criteria.list();
	}

	@Override
	public Long getNumberOfRecordsByAiId(Integer aiId){
		if(aiId!=null){
			Criteria criteria = getSession().createCriteria(getPersistentClass(),"ese");
			criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			DetachedCriteria subQuery = DetachedCriteria.forClass(FindingAid.class,"findingAid");
			subQuery.setProjection(Property.forName("findingAid.id"));
			subQuery.add(Restrictions.between("fileState.fsId",9,14));
			subQuery.add(Restrictions.eq("archivalInstitution.aiId", aiId));
			criteria.setProjection(Projections.sum("ese.numberOfRecords"));
			criteria.add(Restrictions.between("ese.eseState.esId",1,3));
			criteria.add(Subqueries.propertyIn("ese.findingAid.id",subQuery));
			return (Long)criteria.uniqueResult();
		}
		return new Long(0);
	}

	@Override
	public Long getNumberOfRecordsDeliveredByAiId(Integer aiId) {
		if(aiId!=null){
			Criteria criteria = getSession().createCriteria(getPersistentClass(),"ese");
			criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			DetachedCriteria subQuery = DetachedCriteria.forClass(FindingAid.class,"findingAid");
			subQuery.setProjection(Property.forName("findingAid.id"));
			subQuery.add(Restrictions.eq("archivalInstitution.aiId", aiId));
			criteria.setProjection(Projections.sum("ese.numberOfRecords"));
			criteria.add(Restrictions.eq("ese.eseState.esId",2));
			criteria.add(Subqueries.propertyIn("ese.findingAid.id",subQuery));
			return (Long)criteria.uniqueResult();
		}
		return new Long(0);
	}

	@Override
	public Long getSearchedNumberOfRecordsByAiId(String searchTerms,Integer aiId,int option,Set<String> convertedToESEFileStates) {
		if(aiId!=null){
			Criteria criteria = getSession().createCriteria(getPersistentClass(),"ese");
			criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			DetachedCriteria subQuery = getSubQueryRestrictions(searchTerms,aiId,option,convertedToESEFileStates);
			criteria.setProjection(Projections.sum("ese.numberOfRecords"));
			criteria.add(Restrictions.between("ese.eseState.esId",1,3));
			criteria.add(Subqueries.propertyIn("ese.findingAid.id",subQuery));
			return (Long)criteria.uniqueResult();
		}
		return new Long(0);
	}

	@Override
	public Long getTotalDeliveredToEuropeanaRecordsByAiId(String searchTerms,Integer aiId,int option,Set<String> deliveredToEuropeanaFileStates) {
		if(aiId!=null){
			Criteria criteria = getSession().createCriteria(getPersistentClass(),"ese");
			criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			DetachedCriteria subQuery = getSubQueryRestrictions(searchTerms,aiId,option,deliveredToEuropeanaFileStates);
			criteria.setProjection(Projections.sum("ese.numberOfRecords"));
			criteria.add(Restrictions.eq("ese.eseState.esId",2));
			criteria.add(Subqueries.propertyIn("ese.findingAid.id",subQuery));
			return (Long)criteria.uniqueResult();
		}
		return new Long(0);
	}

	private DetachedCriteria getSubQueryRestrictions(String searchTerms, Integer aiId,int option,Set<String> deliveredToEuropeanaFileStates) {
		DetachedCriteria subQuery = DetachedCriteria.forClass(FindingAid.class,"findingAid");
		subQuery.setProjection(Property.forName("findingAid.id"));
		subQuery.add(Restrictions.eq("archivalInstitution.aiId", aiId));
		subQuery = createMatchFindingAidsCriteria(subQuery, searchTerms, aiId, option, deliveredToEuropeanaFileStates);
		return subQuery;
	}
	
	private DetachedCriteria createMatchFindingAidsCriteria(DetachedCriteria subQuery,String queries, Integer ai,int option,Collection<String> fileStates) {
		if (StringUtils.isNotBlank(queries)) {
			String[] query = StringUtils.split(queries, " ");
			if (option == 0) {
				for (int i = 0; i < query.length; i++) {
					subQuery.add(Restrictions.or(Restrictions.like("findingAid.title", "%" + query[i] + "%"),
							Restrictions.like("findingAid.eadid", "%" + query[i] + "%")));
				}
			} else if (option == 1) {
				for (int i = 0; i < query.length; i++) {
					subQuery.add(Restrictions.like("findingAid.eadid", "%" + query[i] + "%"));
				}
			} else if (option == 2) {
				for (int i = 0; i < query.length; i++) {
					subQuery.add(Restrictions.like("findingAid.title", "%" + query[i] + "%"));
				}
			}
		}
		if (fileStates != null && fileStates.size() > 0) {
			subQuery = subQuery.createAlias("findingAid.fileState", "fileState");
			Disjunction disjunction = Restrictions.disjunction();
			for (String fileState : fileStates) {
				disjunction.add(Restrictions.eq("fileState.state", fileState));
			}
			subQuery.add(disjunction);
		}
        subQuery.add(Restrictions.eq("archivalInstitution.aiId", ai.intValue()));
		return subQuery;
	}
}
