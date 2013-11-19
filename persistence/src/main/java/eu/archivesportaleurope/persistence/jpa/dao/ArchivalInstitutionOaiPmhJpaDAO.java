package eu.archivesportaleurope.persistence.jpa.dao;

import eu.apenet.persistence.dao.ArchivalInstitutionOaiPmhDAO;
import eu.apenet.persistence.hibernate.AbstractHibernateDAO;
import eu.apenet.persistence.vo.ArchivalInstitutionOaiPmh;
import eu.apenet.persistence.vo.QueueItem;

import javax.persistence.TypedQuery;
import java.util.List;

/**
 * User: Yoann Moranville
 * Date: 16/10/2013
 *
 * @author Yoann Moranville
 */
public class ArchivalInstitutionOaiPmhJpaDAO extends AbstractHibernateDAO<ArchivalInstitutionOaiPmh, Long> implements ArchivalInstitutionOaiPmhDAO {
    @Override
    public List<ArchivalInstitutionOaiPmh> getArchivalInstitutionOaiPmhs(Integer aiId) {
        String query = "SELECT archivalInstitutionOaiPmh FROM ArchivalInstitutionOaiPmh archivalInstitutionOaiPmh WHERE archivalInstitutionOaiPmh.aiId  = :aiId ORDER BY archivalInstitutionOaiPmh.id";
        TypedQuery<ArchivalInstitutionOaiPmh> typedQuery = getEntityManager().createQuery(query, ArchivalInstitutionOaiPmh.class);
        typedQuery.setParameter("aiId", aiId);
        return typedQuery.getResultList();
    }

    @Override
    public Long countEnabledItems() {
        TypedQuery<Long> query = getEntityManager().createQuery("SELECT count(archivalInstitutionOaiPmh) FROM ArchivalInstitutionOaiPmh archivalInstitutionOaiPmh WHERE archivalInstitutionOaiPmh.enabled = 'TRUE'", Long.class);
        query.setMaxResults(1);
        return query.getResultList().get(0);
    }

    public List<ArchivalInstitutionOaiPmh> getFirstItems() {
        TypedQuery<ArchivalInstitutionOaiPmh> query = getEntityManager().createQuery("SELECT archivalInstitutionOaiPmh FROM ArchivalInstitutionOaiPmh archivalInstitutionOaiPmh ORDER BY archivalInstitutionOaiPmh.lastHarvesting desc, archivalInstitutionOaiPmh.id asc", ArchivalInstitutionOaiPmh.class);
        query.setMaxResults(20);
        return query.getResultList();
    }
}

