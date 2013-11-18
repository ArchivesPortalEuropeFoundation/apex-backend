/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.persistence.jpa.dao;

import eu.apenet.persistence.dao.IngestionprofileDAO;
import eu.apenet.persistence.hibernate.AbstractHibernateDAO;
import eu.apenet.persistence.vo.Ingestionprofile;
import java.util.List;
import javax.persistence.TypedQuery;

/**
 *
 * @author papp
 */
public class IngestionprofileJpaDAO extends AbstractHibernateDAO<Ingestionprofile, Long> implements IngestionprofileDAO {

    @Override
    public List<Ingestionprofile> getIngestionprofiles(Integer aiId) {
        String query = "SELECT ingestionprofile FROM Ingestionprofile ingestionprofile WHERE ingestionprofile.aiId = :aiId ";
        TypedQuery<Ingestionprofile> typedQuery = getEntityManager().createQuery(query, Ingestionprofile.class);
        typedQuery.setParameter("aiId", aiId);
        return typedQuery.getResultList();
    }
}
