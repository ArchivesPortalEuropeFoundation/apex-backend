/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.persistence.jpa.dao;

import eu.apenet.persistence.dao.ReindexDocDAO;
import eu.apenet.persistence.hibernate.AbstractHibernateDAO;
import eu.apenet.persistence.vo.ReindexDoc;
import java.util.List;
import javax.persistence.TypedQuery;
import org.apache.log4j.Logger;

/**
 *
 * @author kaisar
 */
public class ReindexDocJpaDAO extends AbstractHibernateDAO<ReindexDoc, Integer> implements ReindexDocDAO {

    private final Logger log = Logger.getLogger(getClass());

    @Override
    public Long countDocs() {
        TypedQuery<Long> query = getEntityManager().createQuery(
                "SELECT count(reindexDoc) FROM ReindexDoc reindexDoc",
                Long.class);
        query.setMaxResults(1);
        return query.getResultList().get(0);
    }

    @Override
    public ReindexDoc getFirstDoc() {
        TypedQuery<ReindexDoc> query = getEntityManager().createQuery(
                "SELECT reindexDoc FROM ReindexDoc reindexDoc ORDER BY  id asc",
                ReindexDoc.class);
        query.setMaxResults(1);
        List<ReindexDoc> results = query.getResultList();
        if (results.size() > 0) {
            return results.get(0);
        }
        return null;
    }

    @Override
    public Long countDocs(int aiId) {
        TypedQuery<Long> query = getEntityManager().createQuery(
                "SELECT count(reindexDoc) FROM ReindexDoc reindexDoc WHERE aiId = :aiId",
                Long.class);
        query.setParameter("aiId", aiId);
        query.setMaxResults(1);
        return query.getResultList().get(0);
    }

    @Override
    public List<Object[]> countByArchivalInstitutions() {
        TypedQuery<Object[]> query = getEntityManager().createQuery(
                "SELECT archivalInstitution, count(reindexDoc) FROM ReindexDoc reindexDoc JOIN reindexDoc.archivalInstitution archivalInstitution GROUP BY archivalInstitution",
                Object[].class);
        return query.getResultList();
    }

    @Override
    public List<ReindexDoc> getItemsOfInstitution(Integer aiId) {
        TypedQuery<ReindexDoc> query = getEntityManager().createQuery(
                "SELECT queueItem FROM ReindexDoc reindexDoc WHERE aiId = :aiId ORDER BY id asc",
                ReindexDoc.class);
        query.setParameter("aiId", aiId);
        return query.getResultList();
    }

}
