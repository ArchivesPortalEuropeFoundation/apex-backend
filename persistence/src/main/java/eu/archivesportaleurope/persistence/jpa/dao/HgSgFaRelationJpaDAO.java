package eu.archivesportaleurope.persistence.jpa.dao;

import java.util.List;

import javax.persistence.TypedQuery;

import eu.apenet.persistence.dao.HgSgFaRelationDAO;
import eu.apenet.persistence.hibernate.AbstractHibernateDAO;
import eu.apenet.persistence.vo.Ead;
import eu.apenet.persistence.vo.HgSgFaRelation;
import eu.apenet.persistence.vo.SourceGuide;
import javax.persistence.Query;

public class HgSgFaRelationJpaDAO extends AbstractHibernateDAO<HgSgFaRelation, Long> implements HgSgFaRelationDAO {

    @Override
    public List<HgSgFaRelation> getHgSgFaRelations(Integer id, Class<? extends Ead> clazz, Boolean published) {
        String varName = "hgId";
        if (SourceGuide.class.equals(clazz)) {
            varName = "sgId";
        }
        String jpaQuery = "";
        if (published == null) {
            jpaQuery = "SELECT hgSgFaRelation FROM HgSgFaRelation hgSgFaRelation JOIN hgSgFaRelation.findingAid findingAid WHERE hgSgFaRelation." + varName + " = :id";
        } else {
            jpaQuery = "SELECT hgSgFaRelation FROM HgSgFaRelation hgSgFaRelation JOIN hgSgFaRelation.findingAid findingAid WHERE hgSgFaRelation." + varName + " = :id AND findingAid.published = :published";
        }
        TypedQuery<HgSgFaRelation> query = getEntityManager().createQuery(jpaQuery, HgSgFaRelation.class);
        query.setParameter("id", id);
        if (published != null) {
            query.setParameter("published", published);
        }
        return query.getResultList();
    }

    @Override
    public Long countHgSgFaRelations(Integer id, Class<? extends Ead> clazz, Boolean published) {
        String varName = "hgId";
        if (SourceGuide.class.equals(clazz)) {
            varName = "sgId";
        }
        String jpaQuery = "";
        if (published == null) {
            jpaQuery = "SELECT count(findingAid) FROM HgSgFaRelation hgSgFaRelation JOIN hgSgFaRelation.findingAid findingAid WHERE hgSgFaRelation." + varName + " = :id";
        } else {
            jpaQuery = "SELECT count(findingAid) FROM HgSgFaRelation hgSgFaRelation JOIN hgSgFaRelation.findingAid findingAid WHERE hgSgFaRelation." + varName + " = :id AND findingAid.published = :published";
        }
        TypedQuery<Long> query = getEntityManager().createQuery(jpaQuery, Long.class);
        query.setParameter("id", id);
        if (published != null) {
            query.setParameter("published", published);
        }
        return query.getSingleResult();
    }

    @Override
    public boolean existHgFaRelations(Integer hgId, Integer faId) {
        String jpaQuery = "SELECT id FROM HgSgFaRelation hgSgFaRelation WHERE hgId = :hgId AND faId = :faId";

        TypedQuery<Long> query = getEntityManager().createQuery(jpaQuery, Long.class);
        query.setParameter("hgId", hgId);
        query.setParameter("faId", faId);
        query.setMaxResults(1);
        return query.getResultList().size() > 0;
    }

    @Override
    public boolean existSgFaRelations(Integer sgId, Integer faId) {
        String jpaQuery = "SELECT id FROM HgSgFaRelation hgSgFaRelation WHERE sgId = :sgId AND faId = :faId";

        TypedQuery<Long> query = getEntityManager().createQuery(jpaQuery, Long.class);
        query.setParameter("sgId", sgId);
        query.setParameter("faId", faId);
        query.setMaxResults(1);
        return query.getResultList().size() > 0;
    }

    @Override
    public HgSgFaRelation getHgSgFaRelationByHgFaCombination(Integer hgId, Integer faId) {
        String jpaQuery = "SELECT hgSgFaRelation FROM HgSgFaRelation hgSgFaRelation WHERE hgSgFaRelation.hgId = :hgId AND hgSgFaRelation.faId = :faId";
        TypedQuery<HgSgFaRelation> query = getEntityManager().createQuery(jpaQuery, HgSgFaRelation.class);
        query.setParameter("hgId", hgId);
        query.setParameter("faId", faId);
        query.setMaxResults(1);
        return query.getSingleResult();
    }
}
