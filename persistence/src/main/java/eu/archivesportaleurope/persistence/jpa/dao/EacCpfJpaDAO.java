package eu.archivesportaleurope.persistence.jpa.dao;


import java.util.List;

import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;

import eu.apenet.persistence.dao.EacCpfDAO;
import eu.apenet.persistence.hibernate.AbstractHibernateDAO;
import eu.apenet.persistence.vo.EacCpf;

public class EacCpfJpaDAO extends AbstractHibernateDAO<EacCpf, Integer> implements EacCpfDAO {

	private final Logger log = Logger.getLogger(getClass());
	
	
	@Override
    @SuppressWarnings("unchecked")
	public boolean existEacCpf(String identifier){
		TypedQuery<EacCpf> query = getEntityManager().createQuery(
				"SELECT id FROM EacCpf eacCpf WHERE eacCpf.identifier = :identifier " , EacCpf.class);
		query.setParameter("identifier", identifier);
		query.setMaxResults(1);
		return query.getResultList().size() > 0;
    }
	
	@SuppressWarnings("unchecked")
	@Override
	public EacCpf getEacCpfByIdentifier(Integer aiId, String identifier) {
		TypedQuery<EacCpf> query = getEntityManager().createQuery(
				"SELECT id FROM EacCpf eacCpf WHERE eacCpf.aiId = :aiId AND eacCpf.identifier  = :identifier " , EacCpf.class);
		query.setParameter("identifier", identifier);
		query.setParameter("aiId", aiId);
		query.setMaxResults(1);
		List<EacCpf> list = query.getResultList();
		if (list.size() > 0)
			return list.get(0);
		return null;
	}
}
