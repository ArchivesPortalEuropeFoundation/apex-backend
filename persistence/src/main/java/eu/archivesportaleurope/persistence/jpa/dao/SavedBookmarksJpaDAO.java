package eu.archivesportaleurope.persistence.jpa.dao;

import java.util.List;

import javax.persistence.TypedQuery;

import eu.apenet.persistence.dao.SavedBookmarksDAO;
import eu.apenet.persistence.hibernate.AbstractHibernateDAO;
import eu.apenet.persistence.vo.EadSavedSearch;
import eu.apenet.persistence.vo.SavedBookmarks;


public class SavedBookmarksJpaDAO extends AbstractHibernateDAO<SavedBookmarks, Long> implements SavedBookmarksDAO {
    
	@Override
	public Long countSavedBookmarks(Long liferayUserId) {
		try {
			TypedQuery<Long> query = getEntityManager()
					.createQuery(
							"SELECT COUNT (savedBookmarks) FROM SavedBookmarks savedBookmarks WHERE savedBookmarks.liferayUserId = :liferayUserId",
							Long.class);
			query.setParameter("liferayUserId", liferayUserId);
			return query.getSingleResult();
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public List<SavedBookmarks> getSavedBookmarks(Long liferayUserId, int pageNumber, int pagesize) {
		try {
			TypedQuery<SavedBookmarks> query = getEntityManager().createQuery(
					"SELECT savedBookmarks FROM SavedBookmarks savedBookmarks  WHERE savedBookmarks.liferayUserId = :liferayUserId ORDER BY savedBookmarks.modifiedDate DESC ", SavedBookmarks.class);
			query.setParameter("liferayUserId", liferayUserId);
			query.setMaxResults(pagesize);
			query.setFirstResult(pagesize * (pageNumber - 1));
			return query.getResultList();
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public SavedBookmarks getSavedBookmark(Long liferayUserId, long id) {
		SavedBookmarks bookmark =  findById(id);
		if (bookmark != null){
			if (liferayUserId != null && liferayUserId == bookmark.getLiferayUserId())
				return bookmark;
		}
		return null;
	}

}
