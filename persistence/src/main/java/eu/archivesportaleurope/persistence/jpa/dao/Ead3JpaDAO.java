/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.persistence.jpa.dao;

import eu.apenet.persistence.dao.ContentSearchOptions;
import eu.apenet.persistence.dao.Ead3DAO;
import eu.apenet.persistence.hibernate.AbstractHibernateDAO;
import eu.apenet.persistence.vo.Ead3;
import eu.archivesportaleurope.util.ApeUtil;
import java.util.List;
import javax.persistence.TypedQuery;
import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author mahbub
 */
public class Ead3JpaDAO  extends AbstractHibernateDAO<Ead3, Integer> implements Ead3DAO {

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
    public Ead3 getEad3ByIdentifier(String repositorycode, String identifier, boolean onlyPublished) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Ead3> getEad3s(ContentSearchOptions contentSearchOptions) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public long countEad3s(ContentSearchOptions contentSearchOptions) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
    
}
