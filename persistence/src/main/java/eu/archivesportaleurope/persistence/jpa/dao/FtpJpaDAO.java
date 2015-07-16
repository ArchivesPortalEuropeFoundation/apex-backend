package eu.archivesportaleurope.persistence.jpa.dao;

import eu.apenet.persistence.dao.FtpDAO;
import eu.apenet.persistence.hibernate.AbstractHibernateDAO;
import eu.apenet.persistence.vo.Ftp;
import java.util.List;
import javax.persistence.TypedQuery;

/**
 *
 * @author apef
 */
public class FtpJpaDAO extends AbstractHibernateDAO<Ftp, Integer> implements FtpDAO{

    @Override
    public Ftp getFtpConfig(Integer aiId) {
        String query = "SELECT ftp FROM Ftp ftp WHERE ftp.aiId = :aiId ";
        TypedQuery<Ftp> typedQuery = getEntityManager().createQuery(query, Ftp.class);
        typedQuery.setParameter("aiId", aiId);
        typedQuery.setMaxResults(1);
        List<Ftp> list = typedQuery.getResultList();
        if (list.size() > 0) {
        	return list.get(0);
        }
        return null;
    }
}
