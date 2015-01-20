package eu.archivesportaleurope.persistence.jpa.dao;

import eu.apenet.persistence.dao.XslUploadDAO;
import eu.apenet.persistence.hibernate.AbstractHibernateDAO;
import eu.apenet.persistence.vo.XslUpload;

import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Created by yoannmoranville on 29/12/14.
 */
public class XslUploadJpaDAO extends AbstractHibernateDAO<XslUpload, Long> implements XslUploadDAO {

    @Override
    public List<XslUpload> getXslUploads() {
        String query = "SELECT xslUpload FROM XslUpload xslUpload ORDER BY xslUpload.id ASC";
        TypedQuery<XslUpload> typedQuery = getEntityManager().createQuery(query, XslUpload.class);
        return typedQuery.getResultList();
    }

    @Override
    public List<XslUpload> getXslUploads(Integer archivalInstitutionId) {
        String query = "SELECT xslUpload FROM XslUpload xslUpload WHERE xslUpload.archivalInstitutionId  = :archivalInstitutionId ORDER BY xslUpload.id ASC";
        TypedQuery<XslUpload> typedQuery = getEntityManager().createQuery(query, XslUpload.class);
        typedQuery.setParameter("archivalInstitutionId", archivalInstitutionId);
        return typedQuery.getResultList();
    }

    @Override
    public boolean hasXslUpload(Integer archivalInstitutionId) {
        String query = "SELECT xslUpload FROM XslUpload xslUpload WHERE xslUpload.archivalInstitutionId  = :archivalInstitutionId ORDER BY xslUpload.id";
        TypedQuery<XslUpload> typedQuery = getEntityManager().createQuery(query, XslUpload.class);
        typedQuery.setParameter("archivalInstitutionId", archivalInstitutionId);
        return typedQuery.getResultList().size() > 0;
    }

}
