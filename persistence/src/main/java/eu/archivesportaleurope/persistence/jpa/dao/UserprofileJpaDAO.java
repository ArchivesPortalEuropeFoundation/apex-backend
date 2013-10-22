/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.persistence.jpa.dao;

import eu.apenet.persistence.dao.UserprofileDAO;
import eu.apenet.persistence.hibernate.AbstractHibernateDAO;
import eu.apenet.persistence.vo.Userprofile;
import java.util.List;
import javax.persistence.TypedQuery;

/**
 *
 * @author papp
 */
public class UserprofileJpaDAO extends AbstractHibernateDAO<Userprofile, Long> implements UserprofileDAO {

    @Override
    public List<Userprofile> getUserprofiles(Integer aiId) {
        String query = "SELECT userprofile FROM Userprofile userprofile WHERE userprofile.aiId = :aiId ";
        TypedQuery<Userprofile> typedQuery = getEntityManager().createQuery(query, Userprofile.class);
        typedQuery.setParameter("aiId", aiId);
        return typedQuery.getResultList();
    }
}
