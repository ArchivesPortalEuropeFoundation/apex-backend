package eu.apenet.persistence.hibernate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.persistence.Query;
import javax.persistence.TypedQuery;

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

import eu.apenet.persistence.dao.FindingAidDAO;
import eu.apenet.persistence.vo.ArchivalInstitution;
import eu.apenet.persistence.vo.CLevel;
import eu.apenet.persistence.vo.EadContent;
import eu.apenet.persistence.vo.FileState;
import eu.apenet.persistence.vo.FindingAid;
import eu.apenet.persistence.vo.HoldingsGuide;
import eu.apenet.persistence.vo.ValidatedState;

public class FindingAidHibernateDAO extends AbstractHibernateDAO<FindingAid, Integer> implements FindingAidDAO {

	private final Logger log = Logger.getLogger(getClass());

//	public Long countFindingAids(String faEadid, String faTitle, String uploadMethod, Collection<String> fileStates, Integer aiId,
//			String sort, boolean ascending) {
//		Criteria criteria = createFindingAidsCriteria(faEadid, faTitle, uploadMethod, fileStates, aiId, sort, ascending);
//		criteria.setProjection(Projections.countDistinct("findingAid.id"));
//		Object object = criteria.list().get(0);
//		if (object instanceof Integer) {
//			return ((Integer) object).longValue();
//		} else if (object instanceof Long) {
//			return (Long) object;
//		}
//		return (Long) new Long(0);
//	}
//
//	@SuppressWarnings("unchecked")
//	public List<FindingAid> getFindingAids(String faEadid, String faTitle, String uploadMethod,
//			Collection<String> fileStates, Integer aiId, String sort, boolean ascending) {
//		long startTime = System.currentTimeMillis();
//		List<FindingAid> results = new ArrayList<FindingAid>();
//		Criteria criteria = createFindingAidsCriteria(faEadid, faTitle, uploadMethod, fileStates, aiId, sort, ascending);
//		results = criteria.list();
//		long endTime = System.currentTimeMillis();
//		if (log.isDebugEnabled()) {
//			log.debug("query took " + (endTime - startTime) + " ms to read " + results.size() + " objects");
//		}
//
//		return results;
//	}

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

//	//This method retrieves finding aids not linked using pagination
//	@SuppressWarnings("unchecked")
//	public List<FindingAid> getFindingAidsNotLinkedBySegment(Integer aiId, Collection<String> fileStates, String sort, boolean ascending, Integer from, Integer maxNumberOfItems) {
//		long startTime = System.currentTimeMillis();
//		List<FindingAid> results = new ArrayList<FindingAid>();
//		Criteria criteria = createFindingAidsNotLinkedCriteria(aiId, fileStates, sort, ascending);
//		criteria.setFirstResult(from);
//		criteria.setMaxResults(maxNumberOfItems);
//		results = criteria.list();
//		long endTime = System.currentTimeMillis();
//		if (log.isDebugEnabled()) {
//			log.debug("query took " + (endTime - startTime) + " ms to read " + results.size() + " objects");
//		}
//
//		return results;
//	}
	
//	private Criteria createFindingAidsCriteria(String faEadid, String faTitle, String uploadMethod,
//			Collection<String> fileStates, Integer aiId, String sortValue, boolean ascending) {
//		Criteria criteria = getSession().createCriteria(getPersistentClass(), "findingAid");
//		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
//		if (fileStates.size() > 0) {
//			criteria = criteria.createAlias("findingAid.fileState", "fileState");
//			Disjunction disjunction = Restrictions.disjunction();
//			for (String fileState : fileStates) {
//				disjunction.add(Restrictions.eq("fileState.state", fileState));
//			}
//			criteria.add(disjunction);
//		}
//		if (StringUtils.isNotBlank(uploadMethod)) {
//			criteria.add(Restrictions.like("uploadMethod.method", uploadMethod, MatchMode.EXACT));
//		}
//		if (StringUtils.isNotBlank(faEadid)) {
//			criteria.add(Restrictions.like("findingAid.eadid", faEadid, MatchMode.EXACT));
//		}
//		if (StringUtils.isNotBlank(faTitle)) {
//			criteria.add(Restrictions.like("findingAid.title", faEadid, MatchMode.ANYWHERE));
//		}
//		if (aiId != null) {
//			criteria.add(Restrictions.eq("archivalInstitution.aiId", aiId.intValue()));
//		}
//		
//		if ("faEadid".equals(sortValue)) {
//			if (ascending) {
//				criteria.addOrder(Order.asc("findingAid.eadid"));
//			} else {
//				criteria.addOrder(Order.desc("findingAid.eadid"));
//			}
//		} else if ("uploadMethod".equals(sortValue)) {
//			if (ascending) {
//				criteria.addOrder(Order.asc("uploadMethod.method"));
//			} else {
//				criteria.addOrder(Order.desc("uploadMethod.method"));
//			}
//		} else if ("faTitle".equals(sortValue)) {
//			if (ascending) {
//				criteria.addOrder(Order.asc("findingAid.title"));
//			} else {
//				criteria.addOrder(Order.desc("findingAid.title"));
//			}
//		} else if ("uploadDate".equals(sortValue)) {
//			if (ascending) {
//				criteria.addOrder(Order.asc("findingAid.uploadDate"));
//				criteria.addOrder(Order.asc("findingAid.eadid"));
//				criteria.addOrder(Order.asc("findingAid.title"));
//				
//			} else {
//				criteria.addOrder(Order.desc("findingAid.uploadDate"));
//				criteria.addOrder(Order.asc("findingAid.eadid"));
//				criteria.addOrder(Order.asc("findingAid.title"));
//				
//			}
//		}
//		return criteria;
//	}

//	private Criteria setFileStates(Criteria criteria, Collection<String> fileStates) {
//		if (fileStates!= null && fileStates.size() > 0) {
//			criteria = criteria.createAlias("findingAid.fileState", "fileState");
//			Disjunction disjunction = Restrictions.disjunction();
//			for (String fileState : fileStates) {
//				disjunction.add(Restrictions.eq("fileState.state", fileState));
//			}
//			criteria.add(disjunction);
//		}
//		return criteria;
//	}
//
//	@Override
//	public Long countFindingAids(Integer ai, Collection<String> fileStates, List<String> statuses, Boolean isLinkedWithHoldingsGuide) {
//		Criteria criteria = createFindingAidsCriteria(ai, fileStates, statuses, isLinkedWithHoldingsGuide);
//		criteria.setProjection(Projections.countDistinct("findingAid.id"));
//		Object object = criteria.list().get(0);
//		if (object instanceof Integer) {
//			return ((Integer) object).longValue();
//		} else if (object instanceof Long) {
//			return (Long) object;
//		}
//		return (Long) new Long(0);
//	}

//	private Criteria createMatchFindingAidsCriteria(String queries, Integer ai, Integer pageNumber, Integer pageSize,
//			int option, String sortValue, boolean ascending, Collection<String> fileStates, List<String> statuses, Boolean isLinkedToHoldingsGuide) {
//
//		Criteria criteria = null;
//		if(isLinkedToHoldingsGuide==null){
//			criteria = getSession().createCriteria(getPersistentClass(), "findingAid");
//		}else{
//			criteria = createCriteriaForFindingAidsByHoldingsGuide(null,ai,null,isLinkedToHoldingsGuide);
//		}
//		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
//
//		if (pageNumber != null && pageSize != null) {
//			criteria.setFirstResult(pageSize * (pageNumber));
//			criteria.setMaxResults(pageSize);
//		}
//		if (StringUtils.isNotBlank(queries)) {
//			String[] query = StringUtils.split(queries, " ");
//			if (option == 0) {
//				for (int i = 0; i < query.length; i++) {
//					criteria.add(Restrictions.or(Restrictions.like("title", "%" + query[i] + "%"),
//							Restrictions.like("eadid", "%" + query[i] + "%")));
//				}
//			} else if (option == 1) {
//				for (int i = 0; i < query.length; i++) {
//					criteria.add(Restrictions.like("eadid", "%" + query[i] + "%"));
//				}
//			} else if (option == 2) {
//				for (int i = 0; i < query.length; i++) {
//					criteria.add(Restrictions.like("title", "%" + query[i] + "%"));
//				}
//			}
//		}
//		Disjunction disjunction = null;
//        if(fileStates != null || statuses != null || sortValue != null)
//            criteria = criteria.createAlias("findingAid.fileState", "fileState");
//
//		if (fileStates != null && fileStates.size() > 0) {
//			disjunction = Restrictions.disjunction();
//			for (String fileState : fileStates) {
//				disjunction.add(Restrictions.eq("fileState.state", fileState));
//			}
//			if(statuses==null || statuses.size() == 0){
//				criteria.add(disjunction);
//			}
//		}
//
//        if(statuses != null){
//        	if(fileStates==null || fileStates.size() == 0)
//        		disjunction = Restrictions.disjunction();
//            for(String value : statuses){
//                disjunction.add(Restrictions.eq("fileState.state", value));
//            }
//            criteria.add(disjunction);
//        }
//
//
//        if(sortValue != null) {
//            if(sortValue.equals("conversion")){
//                if(ascending)
//                    criteria.addOrder(OrderBySqlFormula.sqlFormula(
//                        "(CASE filestate1_.state " +
//                        "WHEN '" + FileState.NEW + "' THEN 1 " +
//                        "WHEN '" + FileState.NOT_VALIDATED_NOT_CONVERTED + "' THEN 1 " +
//                        "WHEN '" + FileState.VALIDATED_NOT_CONVERTED + "' THEN 1 " +
//                        "ELSE 100 END) ASC"));
//                else
//                    criteria.addOrder(OrderBySqlFormula.sqlFormula(
//                        "(CASE filestate1_.state " +
//                        "WHEN '" + FileState.NEW + "' THEN 100 " +
//                        "WHEN '" + FileState.NOT_VALIDATED_NOT_CONVERTED + "' THEN 100 " +
//                        "WHEN '" + FileState.VALIDATED_NOT_CONVERTED + "' THEN 100 " +
//                        "ELSE 1 END) ASC"));
//            } else if(sortValue.equals("validation")){
//                if(ascending)
//                    criteria.addOrder(OrderBySqlFormula.sqlFormula(
//                        "(CASE filestate1_.state " +
//                        "WHEN '" + FileState.VALIDATED_CONVERTED + "' THEN 1 " +
//                        "WHEN '" + FileState.VALIDATED_NOT_CONVERTED + "' THEN 1 " +
//                        "WHEN '" + FileState.INDEXING + "' THEN 1 " +
//                        "WHEN '" + FileState.INDEXED + "' THEN 1 " +
//                        "WHEN '" + FileState.INDEXED_CONVERTED_EUROPEANA + "' THEN 1 " +
//                        "WHEN '" + FileState.INDEXED_DELIVERED_EUROPEANA + "' THEN 1 " +
//                        "WHEN '" + FileState.INDEXED_HARVESTED_EUROPEANA + "' THEN 1 " +
//                        "WHEN '" + FileState.INDEXED_NO_HTML + "' THEN 1 " +
//                        "WHEN '" + FileState.INDEXED_NOT_LINKED + "' THEN 1 " +
//                        "WHEN '" + FileState.INDEXED_LINKED + "' THEN 1 " +
//                        "WHEN '" + FileState.READY_TO_INDEX + "' THEN 1 " +
//
//                        "WHEN '" + FileState.NOT_VALIDATED_NOT_CONVERTED + "' THEN 50 " +
//                        "WHEN '" + FileState.VALIDATING_FINAL_ERROR + "' THEN 50 " +
//
//                        "ELSE 100 END) ASC"));
//                else
//                    criteria.addOrder(OrderBySqlFormula.sqlFormula(
//                        "(CASE filestate1_.state " +
//                        "WHEN '" + FileState.VALIDATED_CONVERTED + "' THEN 100 " +
//                        "WHEN '" + FileState.VALIDATED_NOT_CONVERTED + "' THEN 100 " +
//                        "WHEN '" + FileState.INDEXING + "' THEN 100 " +
//                        "WHEN '" + FileState.INDEXED + "' THEN 100 " +
//                        "WHEN '" + FileState.INDEXED_CONVERTED_EUROPEANA + "' THEN 100 " +
//                        "WHEN '" + FileState.INDEXED_DELIVERED_EUROPEANA + "' THEN 100 " +
//                        "WHEN '" + FileState.INDEXED_HARVESTED_EUROPEANA + "' THEN 100 " +
//                        "WHEN '" + FileState.INDEXED_NO_HTML + "' THEN 100 " +
//                        "WHEN '" + FileState.INDEXED_NOT_LINKED + "' THEN 100 " +
//                        "WHEN '" + FileState.INDEXED_LINKED + "' THEN 100 " +
//                        "WHEN '" + FileState.READY_TO_INDEX + "' THEN 100 " +
//
//                        "WHEN '" + FileState.NOT_VALIDATED_NOT_CONVERTED + "' THEN 50 " +
//                        "WHEN '" + FileState.VALIDATING_FINAL_ERROR + "' THEN 50 " +
//
//                        "ELSE 1 END) ASC"));
//            } else if(sortValue.equals("indexation")){
//                if(ascending)
//                    criteria.addOrder(OrderBySqlFormula.sqlFormula(
//                        "(CASE filestate1_.state " +
//                        "WHEN '" + FileState.INDEXED + "' THEN 1 " +
//                        "WHEN '" + FileState.INDEXED_CONVERTED_EUROPEANA + "' THEN 1 " +
//                        "WHEN '" + FileState.INDEXED_DELIVERED_EUROPEANA + "' THEN 1 " +
//                        "WHEN '" + FileState.INDEXED_HARVESTED_EUROPEANA + "' THEN 1 " +
//                        "WHEN '" + FileState.INDEXED_NO_HTML + "' THEN 1 " +
//                        "WHEN '" + FileState.INDEXED_NOT_LINKED + "' THEN 1 " +
//                        "WHEN '" + FileState.INDEXED_LINKED + "' THEN 1 " +
//
//                        "WHEN '" + FileState.INDEXING + "' THEN 50 " +
//                        "WHEN '" + FileState.READY_TO_INDEX + "' THEN 50 " +
//
//                        "ELSE 100 END) ASC"));
//                else
//                    criteria.addOrder(OrderBySqlFormula.sqlFormula(
//                        "(CASE filestate1_.state " +
//                        "WHEN '" + FileState.INDEXED + "' THEN 100 " +
//                        "WHEN '" + FileState.INDEXED_CONVERTED_EUROPEANA + "' THEN 100 " +
//                        "WHEN '" + FileState.INDEXED_DELIVERED_EUROPEANA + "' THEN 100 " +
//                        "WHEN '" + FileState.INDEXED_HARVESTED_EUROPEANA + "' THEN 100 " +
//                        "WHEN '" + FileState.INDEXED_NO_HTML + "' THEN 100 " +
//                        "WHEN '" + FileState.INDEXED_NOT_LINKED + "' THEN 100 " +
//                        "WHEN '" + FileState.INDEXED_LINKED + "' THEN 100 " +
//
//                        "WHEN '" + FileState.INDEXING + "' THEN 50 " +
//                        "WHEN '" + FileState.READY_TO_INDEX + "' THEN 50 " +
//
//                        "ELSE 1 END) ASC"));
//            } else if(sortValue.equals("eseconversion")) {
//                if(ascending)
//                    criteria.addOrder(OrderBySqlFormula.sqlFormula(
//                        "(CASE filestate1_.state " +
//                        "WHEN '" + FileState.INDEXED_CONVERTED_EUROPEANA + "' THEN 1 " +
//                        "WHEN '" + FileState.INDEXED_DELIVERED_EUROPEANA + "' THEN 1 " +
//                        "WHEN '" + FileState.INDEXED_HARVESTED_EUROPEANA + "' THEN 1 " +
//
//                        "ELSE 100 END) ASC"));
//                else
//                    criteria.addOrder(OrderBySqlFormula.sqlFormula(
//                        "(CASE filestate1_.state " +
//                        "WHEN '" + FileState.INDEXED_CONVERTED_EUROPEANA + "' THEN 100 " +
//                        "WHEN '" + FileState.INDEXED_DELIVERED_EUROPEANA + "' THEN 100 " +
//                        "WHEN '" + FileState.INDEXED_HARVESTED_EUROPEANA + "' THEN 100 " +
//
//                        "ELSE 1 END) ASC"));
//            } else if(sortValue.equals("esedelivery")) {
//                if(ascending)
//                    criteria.addOrder(OrderBySqlFormula.sqlFormula(
//                        "(CASE filestate1_.state " +
//                        "WHEN '" + FileState.INDEXED_DELIVERED_EUROPEANA + "' THEN 1 " +
//                        "WHEN '" + FileState.INDEXED_HARVESTED_EUROPEANA + "' THEN 1 " +
//
//                        "ELSE 100 END) ASC"));
//                else
//                    criteria.addOrder(OrderBySqlFormula.sqlFormula(
//                        "(CASE filestate1_.state " +
//                        "WHEN '" + FileState.INDEXED_DELIVERED_EUROPEANA + "' THEN 100 " +
//                        "WHEN '" + FileState.INDEXED_HARVESTED_EUROPEANA + "' THEN 100 " +
//
//                        "ELSE 1 END) ASC"));
//            } else {
//                if(ascending){
//                    criteria.addOrder(Order.asc(sortValue));
//                } else {
//                    criteria.addOrder(Order.desc(sortValue));
//                }
//            }
//        }
//
//		criteria.add(Restrictions.eq("archivalInstitution.aiId", ai.intValue()));
//		return criteria;
//	}
//
//	@SuppressWarnings("unchecked")
//	@Override
//	public List<FindingAid> searchFindingAids(String query, Integer ai, Integer pageNumber, Integer pageSize,
//			int option, String sortValue, boolean ascending, Collection<String> fileStates, List<String> statuses, Boolean isLinkedToHoldingsGuide) {
//		
//		long startTime = System.currentTimeMillis();
//		Criteria criteria = createMatchFindingAidsCriteria(query, ai, pageNumber, pageSize, option, sortValue, ascending, fileStates, statuses, isLinkedToHoldingsGuide);
//		List<FindingAid> results = criteria.list();
//		long endTime = System.currentTimeMillis();
//		if (log.isDebugEnabled()) {
//			log.debug("query took " + (endTime - startTime) + " ms to read " + results.size() + " objects");
//		}
//
//		return results;
//	}
//
//
//	@Override
//	public Long countSearchFindingAids(String queries, Integer ai, int option, Collection<String> fileStates, List<String> statuses, Boolean isLinkedWithHoldingsGuide) {
//		Criteria criteria =  createMatchFindingAidsCriteria(queries, ai, null, null, option, null, false, fileStates, statuses, isLinkedWithHoldingsGuide);
//		criteria.setProjection(Projections.countDistinct("findingAid.id"));
//		Object object = criteria.list().get(0);
//		if (object instanceof Integer) {
//			return ((Integer) object).longValue();
//		} else if (object instanceof Long) {
//			return (Long) object;
//		}
//		return (Long) new Long(0);
//	}
//	
//	@Override
//	public Long countSearchFindingAidsUnits(String queries, Integer ai, int option, Collection<String> fileStates, List<String> statuses,Boolean isLinkedToHoldingsGuide) {
//		Criteria criteria =  createMatchFindingAidsCriteria(queries, ai, null, null, option, null, false, fileStates, statuses,isLinkedToHoldingsGuide);
//		criteria.setProjection(Projections.sum("findingAid.totalNumberOfUnits"));
//		Object object = criteria.uniqueResult();
//		if (object instanceof Integer) {
//			return ((Integer) object).longValue();
//		} else if (object instanceof Long) {
//			return (Long) object;
//		}
//		return (Long) new Long(0);
//	}
//
//	@SuppressWarnings("unchecked")
//	@Override
//	public List<FindingAid> getFindingAids(Integer aiId, Collection<String> fileStates) {
//		long startTime = System.currentTimeMillis();
//		List<FindingAid> results = new ArrayList<FindingAid>();
//		Criteria criteria = createFindingAidsCriteria(aiId, fileStates, null, null);
//		results = criteria.list();
//		long endTime = System.currentTimeMillis();
//		if (log.isDebugEnabled()) {
//			log.debug("query took " + (endTime - startTime) + " ms to read " + results.size() + " objects");
//		}
//
//		return results;
//	}
//
//    @Override
//    public List<FindingAid> getFindingAids(Integer aiId, List<Integer> selectedIds, Collection<String> fileStates) {
//        if(selectedIds == null || selectedIds.size() == 0)
//            return new ArrayList<FindingAid>();
//
//        Criteria criteria = getSession().createCriteria(getPersistentClass(), "findingAid");
//		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
//        if (aiId != null) {
//			criteria.add(Restrictions.eq("archivalInstitution.aiId", aiId.intValue()));
//		}
//		if (fileStates != null && fileStates.size() > 0) {
//			criteria = criteria.createAlias("findingAid.fileState", "fileState");
//			Disjunction disjunction = Restrictions.disjunction();
//			for (String fileState : fileStates) {
//				disjunction.add(Restrictions.eq("fileState.state", fileState));
//			}
//			criteria.add(disjunction);
//		}
//
//	    Disjunction disjunction = Restrictions.disjunction();
//		for (Integer selectedId : selectedIds) {
//		    disjunction.add(Restrictions.eq("findingAid.id", selectedId));
//		}
//		criteria.add(disjunction);
//
//        return criteria.list();
//    }
//
//	private Criteria createFindingAidsCriteria(Integer aiId, Collection<String> fileStates, List<String> statuses, Boolean isLinkedWithHoldingsGuide) {
//		Criteria criteria = null;
//		if(isLinkedWithHoldingsGuide==null){
//			criteria = getSession().createCriteria(getPersistentClass(), "findingAid");
//		}else{
//			criteria = createCriteriaForFindingAidsByHoldingsGuide(null,aiId,null,isLinkedWithHoldingsGuide);
//		}
//		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
//		if (aiId != null) {
//			criteria.add(Restrictions.eq("archivalInstitution.aiId", aiId.intValue()));
//		}
//        if ((fileStates != null && fileStates.size() > 0) || statuses != null)
//            criteria = criteria.createAlias("findingAid.fileState", "fileState");
//
//		if (fileStates != null && fileStates.size() > 0) {
//			Disjunction disjunction = Restrictions.disjunction();
//			for (String fileState : fileStates) {
//				disjunction.add(Restrictions.eq("fileState.state", fileState));
//			}
//			criteria.add(disjunction);
//		}
//
//        if(statuses != null){
//            Disjunction disjunction = Restrictions.disjunction();
//            for(String value : statuses){
//                disjunction.add(Restrictions.eq("fileState.state", value));
//            }
//            criteria.add(disjunction);
//        }
//		return criteria;
//	}


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

//	@Override
//	public Long countFindingAidsNotLinkedByArchivalInstitution(Integer aiId, Collection<String> fileStates){
//		Criteria criteria = createFindingAidsNotLinkedCriteria(aiId, fileStates, "", false);
//		criteria.setProjection(Projections.count("findingAid.id"));
//		return (Long)criteria.uniqueResult();
//	}
	@Override
	public Long getFindingAidsLinkedByHoldingsGuide(HoldingsGuide holdingsGuide) {
		Criteria criteria = getSession().createCriteria(getPersistentClass(), "findingAid");
		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria.add(Restrictions.eq("holdingsGuide",holdingsGuide));
		criteria.setProjection(Projections.count("findingAid.id"));
		return (Long)criteria.uniqueResult();
	}
	
//	public Long countFindingAids(List<ArchivalInstitution> archivalInstitutions, Collection<String> fileStates) {
//		Criteria criteria = createCountFindingAidsCriteria(archivalInstitutions, fileStates);
//		Object object = criteria.list().get(0);
//		if (object instanceof Integer) {
//			return ((Integer) object).longValue();
//		} else if (object instanceof Long) {
//			return (Long) object;		}
//		return (Long) new Long(0);
//	}
//		
//	private Criteria createCountFindingAidsCriteria(List<ArchivalInstitution> archivalInstitutions, Collection<String> fileStates) {
//		Criteria criteria = getSession().createCriteria(getPersistentClass(), "findingAid");
//		criteria.setProjection(Projections.countDistinct("archivalInstitution.aiId"));
//		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
//		
//		if (archivalInstitutions.size() > 0) {
//			Disjunction disjunction = Restrictions.disjunction();
//			for (int i = 0; i < archivalInstitutions.size(); i ++) {
//				disjunction.add(Restrictions.eq("archivalInstitution", archivalInstitutions.get(i)));
//			}
//			criteria.add(disjunction);
//		}
//		
//		if (fileStates != null && fileStates.size() > 0) {
//			criteria = criteria.createAlias("findingAid.fileState", "fileState");
//			Disjunction disjunction = Restrictions.disjunction();
//			for (String fileState : fileStates) {
//				disjunction.add(Restrictions.eq("fileState.state", fileState));
//			}
//			criteria.add(disjunction);
//		}
//		return criteria;
//	}
	
	/**
	 * This function get all the finding aids within an indexed holdings guide 
	 * (they can be indexed or not), it's useful to replace the linking
	 * finding aids with holdings guide.
	 */
	@Override
	public List<FindingAid> getFindingAidsByHoldingsGuideId(Integer hgId,Integer aiId,boolean published){
		Criteria criteria = createCriteriaForFindingAidsByHoldingsGuide(hgId,aiId,true,null);
		return (List<FindingAid>)criteria.list();
	}
	
	/**
	 * Get the total figure of finding aids indexed into a holdings guide 
	 */
	@Override
	public Long countFindingAidsIndexedByHoldingsGuideId(Integer hgId,Integer aiId){
		Criteria criteria = createCriteriaForFindingAidsByHoldingsGuide(hgId,aiId,true, null);
		criteria.setProjection(Projections.count("findingAid.id"));
		return (Long)criteria.uniqueResult();
	}
	
	private Criteria createCriteriaForFindingAidsByHoldingsGuide(Integer hgId,Integer aiId,boolean published,Boolean isLinked) {
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
						/*Disjunction disjunction = Restrictions.disjunction();
						for (String fileState : FileState.INDEXED_FILE_STATES) {
							disjunction.add(Restrictions.eq("holdingsGuide.fileState.state", fileState));
						}
						subQuery3.add(disjunction);*/
						if(aiId!=null){
							subQuery3.add(Restrictions.eq("holdingsGuide.archivalInstitution.aiId", aiId));
						}
					subQuery2.add(Subqueries.propertyIn("eadContent.hgId",subQuery3));
				}
				subQuery2.setProjection(Property.forName("eadContent.ecId"));
			subQuery.add(Subqueries.propertyIn("cLevel.ecId", subQuery2));
		if(hgId!=null || (isLinked!=null && isLinked)){
			criteria.add(Subqueries.propertyIn("findingAid.eadid", subQuery));
		}else if((isLinked!=null && !isLinked)){
			criteria.add(Subqueries.propertyNotIn("findingAid.eadid", subQuery));
		}
		if(aiId!=null){
			criteria.add(Restrictions.eq("findingAid.archivalInstitution.aiId", aiId));
		}
		criteria.add(Restrictions.eq("findingAid.published", published));
		return criteria;
	}
	
	
	@Override
	public boolean existFindingAidsNotLinkedByArchivalInstitution(Integer aiId) {
		Query query = getEntityManager().createQuery(
		        "SELECT fa.id FROM FindingAid fa WHERE fa.aiId = :aiId AND fa.searchable = true AND fa.eadid NOT IN (SELECT c.hrefEadid FROM CLevel c WHERE c.hrefEadid IS NOT NULL AND c.ecId IN (SELECT ec.ecId FROM EadContent ec WHERE ec.hgId IN (SELECT hg.id FROM HoldingsGuide hg WHERE hg.searchable = true AND hg.aiId = :aiId) OR ec.sgId IN (SELECT sg.id FROM SourceGuide sg WHERE sg.searchable = true AND sg.aiId = :aiId)))");
		query.setParameter("aiId", aiId);
		query.setMaxResults(1);
		return query.getResultList().size() > 0;
	}
	@Override
	public List<FindingAid> getFindingAidsNotLinkedByArchivalInstitution(Integer aiId, Integer start, Integer maxResults ) {
		TypedQuery<FindingAid> query = getEntityManager().createQuery(
		        "SELECT fa FROM FindingAid fa WHERE fa.aiId = :aiId AND fa.searchable = true AND fa.eadid NOT IN (SELECT c.hrefEadid FROM CLevel c WHERE c.hrefEadid IS NOT NULL AND c.ecId IN (SELECT ec.ecId FROM EadContent ec WHERE ec.hgId IN (SELECT hg.id FROM HoldingsGuide hg WHERE hg.searchable = true AND hg.aiId = :aiId) OR ec.sgId IN (SELECT sg.id FROM SourceGuide sg WHERE sg.searchable = true AND sg.aiId = :aiId))) ORDER BY fa.title", FindingAid.class);
		query.setParameter("aiId", aiId);
		query.setFirstResult(start);
		query.setMaxResults(maxResults);
		return query.getResultList();
	}
	/*
	SELECT fa_title FROM finding_aid 
	WHERE fa_eadid NOT IN 
		(SELECT href_eadid FROM c_level
		 WHERE ec_id IN 
			(SELECT ec_id FROM ead_content WHERE hg_id IN 
				(SELECT hg_id FROM holdings_guide WHERE ai_id = ?)
			)
		 AND href_eadid IS NOT Null
		) 
	AND (fs_id = ? OR fs_id = ? OR ...) AND ai_id = ?;
	*/
	//This criteria selects all the finding aids which belongs to a specific archival institution and have certain file states
	//and are not included within any holdings guide indexed for this specific archival institution
//	private Criteria createFindingAidsNotLinkedCriteria(Integer aiId,Collection<String> fileStates, String sortValue, boolean ascending) {
//		Criteria criteria = getSession().createCriteria(getPersistentClass(),"findingAid");
//		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
//		DetachedCriteria subQuery = DetachedCriteria.forClass(CLevel.class,"cLevel");
//			subQuery.setProjection(Property.forName("cLevel.hrefEadid"));
//			subQuery.add(Restrictions.isNotNull("cLevel.hrefEadid"));
//				DetachedCriteria subQuery2 = DetachedCriteria.forClass(EadContent.class,"eadContent");
//				subQuery2.setProjection(Property.forName("eadContent.ecId"));
//					DetachedCriteria subQuery3 = DetachedCriteria.forClass(HoldingsGuide.class, "holdingsGuide");
//					subQuery3.setProjection(Property.forName("holdingsGuide.id"));
//					subQuery3.add(Restrictions.eq("holdingsGuide.archivalInstitution.aiId", aiId));
//				subQuery2.add(Subqueries.propertyIn("eadContent.hgId", subQuery3));
//			subQuery.add(Subqueries.propertyIn("cLevel.ecId", subQuery2));
//		criteria.add(Subqueries.propertyNotIn("findingAid.eadid", subQuery));
//		criteria.add(Restrictions.eq("findingAid.archivalInstitution.aiId", aiId));
//		//File states
//		if(fileStates!=null){
//			criteria = setFileStates(criteria,fileStates);
//		}
//		//Sorting results
//		if ("faEadid".equals(sortValue)) {
//			if (ascending) {
//				criteria.addOrder(Order.asc("findingAid.eadid"));
//			} else {
//				criteria.addOrder(Order.desc("findingAid.eadid"));
//			}
//		} else if ("uploadMethod".equals(sortValue)) {
//			if (ascending) {
//				criteria.addOrder(Order.asc("uploadMethod.method"));
//			} else {
//				criteria.addOrder(Order.desc("uploadMethod.method"));
//			}
//		} else if ("faTitle".equals(sortValue)) {
//			if (ascending) {
//				criteria.addOrder(Order.asc("findingAid.title"));
//			} else {
//				criteria.addOrder(Order.desc("findingAid.title"));
//			}
//		} else if ("uploadDate".equals(sortValue)) {
//			if (ascending) {
//				criteria.addOrder(Order.asc("findingAid.uploadDate"));
//				criteria.addOrder(Order.asc("findingAid.eadid"));
//				criteria.addOrder(Order.asc("findingAid.title"));
//			} else {
//				criteria.addOrder(Order.desc("findingAid.uploadDate"));
//				criteria.addOrder(Order.asc("findingAid.eadid"));
//				criteria.addOrder(Order.asc("findingAid.title"));
//			}
//		}
//		return criteria;
//	}

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
