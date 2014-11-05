/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.persistence.jpa.dao;

import java.util.List;

import javax.persistence.TypedQuery;

import eu.apenet.persistence.dao.IngestionprofileDAO;
import eu.apenet.persistence.hibernate.AbstractHibernateDAO;
import eu.apenet.persistence.vo.Ingestionprofile;

/**
 *
 * @author papp
 */
public class IngestionprofileJpaDAO extends AbstractHibernateDAO<Ingestionprofile, Long> implements IngestionprofileDAO {

	@Override
	public Ingestionprofile getIngestionprofile(Long id) {
        String query = "SELECT ingestionprofile FROM Ingestionprofile ingestionprofile WHERE ingestionprofile.id = :id ";
        TypedQuery<Ingestionprofile> typedQuery = getEntityManager().createQuery(query, Ingestionprofile.class);
        typedQuery.setParameter("id", id);
        typedQuery.setMaxResults(1);
        List<Ingestionprofile> list = typedQuery.getResultList();
        if (list.size() > 0) {
        	return list.get(0);
        }
        return null;
	}

    @Override
    public List<Ingestionprofile> getIngestionprofiles(Integer aiId) {
        String query = "SELECT ingestionprofile FROM Ingestionprofile ingestionprofile WHERE ingestionprofile.aiId = :aiId ";
        TypedQuery<Ingestionprofile> typedQuery = getEntityManager().createQuery(query, Ingestionprofile.class);
        typedQuery.setParameter("aiId", aiId);
        return typedQuery.getResultList();
    }

    @Override
    public List<Ingestionprofile> getIngestionprofiles(Integer aiId, int fileType) {
        String query = "SELECT ingestionprofile FROM Ingestionprofile ingestionprofile WHERE ingestionprofile.aiId = :aiId AND ingestionprofile.fileType = :fileType ";
        TypedQuery<Ingestionprofile> typedQuery = getEntityManager().createQuery(query, Ingestionprofile.class);
        typedQuery.setParameter("aiId", aiId);
        typedQuery.setParameter("fileType", fileType);
        return typedQuery.getResultList();
    }
}
