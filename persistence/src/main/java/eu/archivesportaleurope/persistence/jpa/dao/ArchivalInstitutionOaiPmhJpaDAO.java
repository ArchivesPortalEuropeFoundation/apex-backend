package eu.archivesportaleurope.persistence.jpa.dao;

import eu.apenet.persistence.dao.ArchivalInstitutionOaiPmhDAO;
import eu.apenet.persistence.hibernate.AbstractHibernateDAO;
import eu.apenet.persistence.vo.ArchivalInstitutionOaiPmh;

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
        String query = "SELECT archivalInstitutionOaiPmh FROM ArchivalInstitutionOaiPmh archivalInstitutionOaiPmh WHERE archivalInstitutionOaiPmh.aiId  = :aiId ";
        TypedQuery<ArchivalInstitutionOaiPmh> typedQuery = getEntityManager().createQuery(query, ArchivalInstitutionOaiPmh.class);
        typedQuery.setParameter("aiId", aiId);
        return typedQuery.getResultList();
    }
}

