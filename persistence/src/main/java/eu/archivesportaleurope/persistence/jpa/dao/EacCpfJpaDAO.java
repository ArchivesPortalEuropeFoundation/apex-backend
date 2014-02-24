package eu.archivesportaleurope.persistence.jpa.dao;

import eu.apenet.persistence.dao.ContentSearchOptions;
import java.util.List;

import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;

import eu.apenet.persistence.dao.EacCpfDAO;
import eu.apenet.persistence.hibernate.AbstractHibernateDAO;
import eu.apenet.persistence.vo.EacCpf;
import eu.apenet.persistence.vo.EuropeanaState;
import eu.apenet.persistence.vo.QueuingState;
import eu.apenet.persistence.vo.ValidatedState;
import java.util.ArrayList;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.apache.commons.lang.StringUtils;

public class EacCpfJpaDAO extends AbstractHibernateDAO<EacCpf, Integer> implements EacCpfDAO {

    private final Logger log = Logger.getLogger(EacCpfJpaDAO.class);

    @Override
    @SuppressWarnings("unchecked")
    public boolean existEacCpf(String identifier) {
        TypedQuery<EacCpf> query = getEntityManager().createQuery(
                "SELECT id FROM EacCpf eacCpf WHERE eacCpf.identifier = :identifier ", EacCpf.class);
        query.setParameter("identifier", identifier);
        query.setMaxResults(1);
        return query.getResultList().size() > 0;
    }

    @SuppressWarnings("unchecked")
    @Override
    public EacCpf getEacCpfByIdentifier(Integer aiId, String identifier) {
        TypedQuery<EacCpf> query = getEntityManager().createQuery(
                "SELECT id FROM EacCpf eacCpf WHERE eacCpf.aiId = :aiId AND eacCpf.identifier  = :identifier ", EacCpf.class);
        query.setParameter("identifier", identifier);
        query.setParameter("aiId", aiId);
        query.setMaxResults(1);
        List<EacCpf> list = query.getResultList();
        if (list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    @Override
    public List<EacCpf> getEacCpfs(ContentSearchOptions contentSearchOptions) {
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<EacCpf> cq = criteriaBuilder.createQuery(EacCpf.class);
        Root<? extends EacCpf> from = (Root<? extends EacCpf>) cq.from(contentSearchOptions.getContentClass());
        cq.where(buildWhere(from, cq, contentSearchOptions));
        cq.select(from);
        /*
         * add ordering
         */
        if (contentSearchOptions.isOrderByAscending()) {
            cq.orderBy(criteriaBuilder.asc(from.get(contentSearchOptions.getOrderByField())));
        } else {
            cq.orderBy(criteriaBuilder.desc(from.get(contentSearchOptions.getOrderByField())));
        }

        /*
         * add pagination
         */
        TypedQuery<EacCpf> query = getEntityManager().createQuery(cq);
        if (contentSearchOptions.getPageSize() > 0) {
            query.setMaxResults(contentSearchOptions.getPageSize());
            if (contentSearchOptions.getFirstResult() >= 0) {
                query.setFirstResult(((Long) contentSearchOptions.getFirstResult()).intValue());
            } else {
                query.setFirstResult(contentSearchOptions.getPageSize() * (contentSearchOptions.getPageNumber() - 1));
            }
        }
        return query.getResultList();
    }

    @Override
    public long countEacCpfs(ContentSearchOptions contentSearchOptions) {
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Long> cq = criteriaBuilder.createQuery(Long.class);
        Root<? extends EacCpf> from = (Root<? extends EacCpf>) cq.from(contentSearchOptions.getContentClass());
        cq.where(buildWhere(from, cq, contentSearchOptions));
        cq.select(criteriaBuilder.countDistinct(from));

        return getEntityManager().createQuery(cq).getSingleResult();
    }

    private Predicate buildWhere(Root<? extends EacCpf> from, CriteriaQuery<?> cq, ContentSearchOptions contentSearchOptions) {
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        List<Predicate> whereClause = new ArrayList<Predicate>();
        if (contentSearchOptions.getIds() != null && contentSearchOptions.getIds().size() > 0) {
            List<Predicate> idPredicated = new ArrayList<Predicate>();
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
        if (StringUtils.isNotBlank(contentSearchOptions.getEadid())) {
            whereClause.add(criteriaBuilder.equal(from.get("identifier"), contentSearchOptions.getEadid()));
        }
        if (contentSearchOptions.getValidated().size() > 0) {
            List<Predicate> validatedPredicated = new ArrayList<Predicate>();
            for (ValidatedState validateState : contentSearchOptions.getValidated()) {
                validatedPredicated.add(criteriaBuilder.equal(from.get("validated"), validateState));
            }
            whereClause.add(criteriaBuilder.or(validatedPredicated.toArray(new Predicate[0])));
        }
        if (contentSearchOptions.getEuropeana().size() > 0) {
            List<Predicate> europeanaPredicated = new ArrayList<Predicate>();
            for (EuropeanaState europeanaState : contentSearchOptions.getEuropeana()) {
                europeanaPredicated.add(criteriaBuilder.equal(from.get("europeana"), europeanaState));
            }
            whereClause.add(criteriaBuilder.or(europeanaPredicated.toArray(new Predicate[0])));

        }
        if (contentSearchOptions.getQueuing().size() > 0) {
            List<Predicate> queuingPredicated = new ArrayList<Predicate>();
            for (QueuingState queuingState : contentSearchOptions.getQueuing()) {
                queuingPredicated.add(criteriaBuilder.equal(from.get("queuing"), queuingState));
            }
            whereClause.add(criteriaBuilder.or(queuingPredicated.toArray(new Predicate[0])));
        }
        if (contentSearchOptions.getPublishedToAll() != null) {
            List<Predicate> orPredicated = new ArrayList<Predicate>();
            if (contentSearchOptions.getPublishedToAll()) {
                orPredicated.add(criteriaBuilder.equal(from.get("published"), true));
                orPredicated.add(criteriaBuilder.equal(from.get("europeana"), EuropeanaState.CONVERTED));
                orPredicated.add(criteriaBuilder.equal(from.get("europeana"), EuropeanaState.DELIVERED));
            } else {
                orPredicated.add(criteriaBuilder.equal(from.get("published"), false));
                orPredicated.add(criteriaBuilder.equal(from.get("europeana"), EuropeanaState.NOT_CONVERTED));
            }
            whereClause.add(criteriaBuilder.or(orPredicated.toArray(new Predicate[0])));
        }
        if (StringUtils.isNotBlank(contentSearchOptions.getSearchTerms())) {

            String[] searchTerms = StringUtils.split(contentSearchOptions.getSearchTerms(), " ");
            if ("identifier".equals(contentSearchOptions.getSearchTermsField())) {
                for (String searchTerm : searchTerms) {
                    whereClause.add(criteriaBuilder.like(from.<String>get("identifier"), "%" + searchTerm + "%"));
                }
            } else if ("title".equals(contentSearchOptions.getSearchTermsField())) {
                for (String searchTerm : searchTerms) {
                    whereClause.add(criteriaBuilder.like(from.<String>get("title"), "%" + searchTerm + "%"));
                }
            } else {
                for (String searchTerm : searchTerms) {
                    Predicate titlePredicate = criteriaBuilder.like(from.<String>get("title"), "%" + searchTerm + "%");
                    Predicate eadidPredicate = criteriaBuilder.like(from.<String>get("identifier"), "%" + searchTerm + "%");
                    whereClause.add(criteriaBuilder.or(titlePredicate, eadidPredicate));
                }

            }
        }

        return criteriaBuilder.and(whereClause.toArray(new Predicate[0]));
    }
}
