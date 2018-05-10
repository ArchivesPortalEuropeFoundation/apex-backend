/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.persistence.jpa.dao;

import eu.apenet.persistence.dao.ContentSearchOptions;
import eu.apenet.persistence.dao.Ead3DAO;
import eu.apenet.persistence.hibernate.AbstractHibernateDAO;
import eu.apenet.persistence.vo.AbstractContent;
import eu.apenet.persistence.vo.Ead3;
import eu.apenet.persistence.vo.QueuingState;
import eu.apenet.persistence.vo.ValidatedState;
import eu.archivesportaleurope.persistence.jpa.JpaUtil;
import eu.archivesportaleurope.util.ApeUtil;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author mahbub
 */
public class Ead3JpaDAO extends AbstractHibernateDAO<Ead3, Integer> implements Ead3DAO {

    private static final Logger LOGGER = Logger.getLogger(Ead3JpaDAO.class);

    @Override
    public Ead3 getFirstPublishedEad3ByIdentifier(String identifier, boolean isPublished) {
        Criteria criteria = getSession().createCriteria(Ead3.class, "ead3");
        criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        criteria.add(Restrictions.eq("identifier", ApeUtil.decodeSpecialCharacters(identifier)));
        if (isPublished) {
            criteria.add(Restrictions.eq("published", true));
        }
        criteria.setMaxResults(1);
        List<Ead3> list = criteria.list();
        if (list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    @Override
    public Integer isEad3idIndexed(String identifier, Integer aiId, Class<? extends AbstractContent> clazz) {
        Criteria criteria = getSession().createCriteria(clazz, "ead3").setProjection(Projections.property("id"));
        criteria.createAlias("ead3.archivalInstitution", "archivalInstitution");
        criteria.add(Restrictions.eq("archivalInstitution.aiId", aiId));
        criteria.add(Restrictions.eq("identifier", ApeUtil.decodeSpecialCharacters(identifier)));
        criteria.add(Restrictions.eq("published", true));
        criteria.setMaxResults(1);
        List<Integer> result = criteria.list();
        if (result.size() > 0) {
            return result.get(0);
        }
        return null;
    }

    @Override
    public Ead3 getEad3ByIdentifier(Integer aiId, String identifier) {
        TypedQuery<Ead3> query = getEntityManager().createQuery(
                "SELECT ead3 FROM Ead3 ead3 WHERE ead3.aiId = :aiId AND ead3.identifier  = :identifier ", Ead3.class);
        query.setParameter("identifier", ApeUtil.decodeSpecialCharacters(identifier));
        query.setParameter("aiId", aiId);
        query.setMaxResults(1);
        List<Ead3> list = query.getResultList();
        if (list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    @Override
    public int deleteById(Integer id) {
        JpaUtil.beginDatabaseTransaction();
        Query query = getEntityManager().createQuery(
                "DELETE  FROM Ead3 ead3 WHERE ead3.id = :id");
        query.setParameter("id", id);
        int res = query.executeUpdate();
        JpaUtil.commitDatabaseTransaction();
        return res;
    }

    @Override
    public Ead3 getEad3ByIdentifier(String repositorycode, String identifier, boolean onlyPublished) {
        Criteria criteria = getSession().createCriteria(Ead3.class, "ead3");
        criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        criteria = criteria.createAlias("ead3.archivalInstitution", "archivalInstitution");
        criteria.add(Restrictions.eq("archivalInstitution.repositorycode", ApeUtil.decodeRepositoryCode(repositorycode)));
        criteria.add(Restrictions.eq("identifier", ApeUtil.decodeSpecialCharacters(identifier)));
        if (onlyPublished) {
            criteria.add(Restrictions.eq("published", true));
        }
        criteria.setMaxResults(1);
        List<Ead3> list = criteria.list();
        if (list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    @Override
    public List<Ead3> getEad3s(ContentSearchOptions contentSearchOptions) {
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Ead3> cq = criteriaBuilder.createQuery(Ead3.class);
        if (contentSearchOptions.getContentClass() == null) {
            contentSearchOptions.setContentClass(Ead3.class);
        }
        Root<? extends Ead3> from = (Root<? extends Ead3>) cq.from(contentSearchOptions.getContentClass());
        cq.where(buildWhere(from, cq, contentSearchOptions));
        cq.select(from);
        if (contentSearchOptions.isOrderByAscending()) {
            cq.orderBy(criteriaBuilder.asc(from.get(contentSearchOptions.getOrderByField())));
        } else {
            cq.orderBy(criteriaBuilder.desc(from.get(contentSearchOptions.getOrderByField())));
        }
        TypedQuery<Ead3> query = getEntityManager().createQuery(cq);
        if (contentSearchOptions.getPageSize() > 0) {
            query.setMaxResults(contentSearchOptions.getPageSize());
            if (contentSearchOptions.getFirstResult() >= 0) {
                query.setFirstResult(((Long) contentSearchOptions.getFirstResult()).intValue());
            } else {
                query.setFirstResult(contentSearchOptions.getPageSize() * (contentSearchOptions.getPageNumber() - 1));
            }
        }
        LOGGER.debug("Ead3 query with criteria: " + query.unwrap(org.hibernate.Query.class).getQueryString());
        return query.getResultList();
    }

    @Override
    public long countEad3s(ContentSearchOptions contentSearchOptions) {
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        if (contentSearchOptions.getContentClass() == null) {
            contentSearchOptions.setContentClass(Ead3.class);
        }
        CriteriaQuery<Long> cq = criteriaBuilder.createQuery(Long.class);
        Root<? extends Ead3> from = (Root<? extends Ead3>) cq.from(contentSearchOptions.getContentClass());
        cq.where(buildWhere(from, cq, contentSearchOptions));
        cq.select(criteriaBuilder.countDistinct(from));

        return getEntityManager().createQuery(cq).getSingleResult();
    }

    @Override
    public Long countUnits(ContentSearchOptions contentSearchOptions) {
        if (contentSearchOptions.getContentClass() == null) {
            contentSearchOptions.setContentClass(Ead3.class);
        }
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Long> cq = criteriaBuilder.createQuery(Long.class);
        Root<? extends Ead3> from = (Root<? extends Ead3>) cq.from(contentSearchOptions.getContentClass());
        cq.where(criteriaBuilder.and(buildWhere(from, cq, contentSearchOptions),
                criteriaBuilder.greaterThan(from.<Integer>get("totalNumberOfUnits"), 0)));
        cq.select(criteriaBuilder.sum(from.<Long>get("totalNumberOfUnits")));

        return getEntityManager().createQuery(cq).getSingleResult();
    }

    @Override
    public Integer isEad3IdUsed(String identifier, Integer aiId, Class<? extends Ead3> clazz) {
        Criteria criteria = getSession().createCriteria(clazz, "ead3").setProjection(Projections.property("id"));
        criteria.createAlias("ead3.archivalInstitution", "archivalInstitution");
        criteria.add(Restrictions.eq("archivalInstitution.aiId", aiId));
        criteria.add(Restrictions.eq("identifier", identifier));
        List<Integer> result = criteria.list();
        if (result.size() > 0) {
            return result.get(0);
        }
        return null;
    }

    @Override
    public Ead3 getEad3ById(Integer aiId, String cpfId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Integer> getAllIds(Class<Ead3> aClass, int aiId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean existEad3s(ContentSearchOptions contentSearchOptions) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private Predicate buildWhere(Root<? extends Ead3> from, CriteriaQuery<?> cq, ContentSearchOptions contentSearchOptions) {
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        List<Predicate> whereClause = new ArrayList<>();
        if (contentSearchOptions.getIds() != null && contentSearchOptions.getIds().size() > 0) {
            List<Predicate> idPredicated = new ArrayList<>();
            for (Integer id : contentSearchOptions.getIds()) {
                idPredicated.add(criteriaBuilder.equal(from.get("id"), id));
            }
            whereClause.add(criteriaBuilder.or(idPredicated.toArray(new Predicate[0])));
        }
        if (contentSearchOptions.getArchivalInstitionId() != null) {
            whereClause.add(criteriaBuilder.equal(from.get("aiId"), contentSearchOptions.getArchivalInstitionId()));
        }
        if (contentSearchOptions.getPublished() != null) {
            whereClause.add(criteriaBuilder.equal(from.get("published"), contentSearchOptions.getPublished()));
        }
        if (contentSearchOptions.getConverted() != null) {
            whereClause.add(criteriaBuilder.equal(from.get("converted"), contentSearchOptions.getConverted()));
        }
        if (contentSearchOptions.getDynamic() != null) {
            whereClause.add(criteriaBuilder.equal(from.get("dynamic"), contentSearchOptions.getDynamic()));
        }
        if (StringUtils.isNotBlank(contentSearchOptions.getEadid())) {
            whereClause.add(criteriaBuilder.equal(from.get("id"), ApeUtil.decodeSpecialCharacters(contentSearchOptions.getEadid())));
        }
        if (contentSearchOptions.getValidated().size() > 0) {
            List<Predicate> validatedPredicated = new ArrayList<>();
            for (ValidatedState validateState : contentSearchOptions.getValidated()) {
                validatedPredicated.add(criteriaBuilder.equal(from.get("validated"), validateState));
            }
            whereClause.add(criteriaBuilder.or(validatedPredicated.toArray(new Predicate[0])));
        }
        /*
		 * only if findingaid
         */

        if (contentSearchOptions.getQueuing().size() > 0) {
            List<Predicate> queuingPredicated = new ArrayList<>();
            for (QueuingState queuingState : contentSearchOptions.getQueuing()) {
                queuingPredicated.add(criteriaBuilder.equal(from.get("queuing"), queuingState));
            }
            whereClause.add(criteriaBuilder.or(queuingPredicated.toArray(new Predicate[0])));
        }
        if (contentSearchOptions.getPublishedToAll() != null) {
            List<Predicate> orPredicated = new ArrayList<>();
            if (contentSearchOptions.getPublishedToAll()) {
                orPredicated.add(criteriaBuilder.equal(from.get("published"), true));
            } else {
                orPredicated.add(criteriaBuilder.equal(from.get("published"), false));
            }
            whereClause.add(criteriaBuilder.or(orPredicated.toArray(new Predicate[0])));
        }
        if (StringUtils.isNotBlank(contentSearchOptions.getSearchTerms())) {
            String[] searchTerms = StringUtils.split(contentSearchOptions.getSearchTerms(), " ");
            if ("id".equals(contentSearchOptions.getSearchTermsField())) {
                String searchTerm = contentSearchOptions.getSearchTerms().trim();
                searchTerm = searchTerm.replaceAll("\\*", "%");
                whereClause.add(criteriaBuilder.like(from.<String>get("identifier"), searchTerm));
            } else if ("title".equals(contentSearchOptions.getSearchTermsField())) {
                for (String searchTerm : searchTerms) {
                    whereClause.add(criteriaBuilder.like(from.<String>get("title"), "%" + searchTerm + "%"));
                }
            } else if ("path".equals(contentSearchOptions.getSearchTermsField())) {
                whereClause.add(criteriaBuilder.equal(from.<String>get("pathApenetead"), contentSearchOptions.getSearchTerms()));
            } else {
                String searchTermId = contentSearchOptions.getSearchTerms().trim();
                searchTermId = searchTermId.replaceAll("\\*", "%");
                Predicate identifierPredicate = criteriaBuilder.like(from.<String>get("identifier"), searchTermId);
                List<Predicate> titleAndPredicated = new ArrayList<>();
                for (String searchTerm : searchTerms) {
                    titleAndPredicated.add(criteriaBuilder.like(from.<String>get("title"), "%" + searchTerm + "%"));
                }
                whereClause.add(criteriaBuilder.or(criteriaBuilder.and(titleAndPredicated.toArray(new Predicate[0])), identifierPredicate));
            }
        }

        return criteriaBuilder.and(whereClause.toArray(new Predicate[0]));
    }

    @Override
    public Long getTotalCountOfUnits() {
        Criteria criteria = null;
        List<Long> result = null;
        Long value = 0L;
        criteria = getSession().createCriteria(getPersistentClass(), "ead3");
        criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        result = criteria.setProjection(Projections.sum("totalNumberOfUnits")).list();
        if (result.size() > 0) {
            int i = 0;
            while (result.size() > i) {
                if (result.get(i) != null) {
                    value += result.get(i);
                }
                i++;
            }
        }
        return value;
    }

}
