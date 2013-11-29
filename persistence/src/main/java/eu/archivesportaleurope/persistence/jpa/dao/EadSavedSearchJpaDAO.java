package eu.archivesportaleurope.persistence.jpa.dao;

import java.util.List;

import javax.persistence.TypedQuery;

import eu.apenet.persistence.dao.EadSavedSearchDAO;
import eu.apenet.persistence.hibernate.AbstractHibernateDAO;
import eu.apenet.persistence.vo.EadSavedSearch;

public class EadSavedSearchJpaDAO extends AbstractHibernateDAO<EadSavedSearch, Long> implements EadSavedSearchDAO {


	@Override
	public EadSavedSearch getEadSavedSearch(Long id, Long liferayUserId) {
		EadSavedSearch eadSavedSearch =  findById(id);
		if (liferayUserId != null && liferayUserId == eadSavedSearch.getLiferayUserId()){
			return eadSavedSearch;
		}else if (eadSavedSearch.isPublicSearch()){
			return eadSavedSearch;
		}
		return null;
	}

	@Override
	public Long countEadSavedSearches(Long liferayUserId) {
		TypedQuery<Long> query = getEntityManager().createQuery(
				"SELECT COUNT(eadSavedSearch) FROM EadSavedSearch eadSavedSearch WHERE eadSavedSearch.liferayUserId = :liferayUserId", Long.class);
		query.setParameter("liferayUserId", liferayUserId);
		return query.getSingleResult();
	}

	@Override
	public List<EadSavedSearch> getEadSavedSearches(Long liferayUserId, int pageNumber, int pageSize) {
		TypedQuery<EadSavedSearch> query = getEntityManager().createQuery(
				"SELECT eadSavedSearch FROM EadSavedSearch eadSavedSearch WHERE eadSavedSearch.liferayUserId = :liferayUserId ORDER BY eadSavedSearch.modifiedDate DESC" , EadSavedSearch.class);
		query.setParameter("liferayUserId", liferayUserId);
		query.setMaxResults(pageSize);
		query.setFirstResult(pageSize * (pageNumber - 1));
		return query.getResultList();
	}

}
