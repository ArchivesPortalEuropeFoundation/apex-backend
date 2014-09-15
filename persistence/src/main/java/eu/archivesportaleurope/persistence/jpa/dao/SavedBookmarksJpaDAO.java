package eu.archivesportaleurope.persistence.jpa.dao;

import java.util.List;

import javax.persistence.TypedQuery;

import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;

import eu.apenet.persistence.dao.SavedBookmarksDAO;
import eu.apenet.persistence.hibernate.AbstractHibernateDAO;
import eu.apenet.persistence.vo.CollectionContent;
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

	@Override
	public List<SavedBookmarks> getSavedBookmarksOutOfCollectionByCollectionIdAndLiferayUser(Long id, long liferayUserId) {
		List<SavedBookmarks> searches = null;
		Criteria criteria = getSession().createCriteria(getPersistentClass(), "savedBookmarks");
		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		if(liferayUserId>0){
			criteria.add(Restrictions.eq("savedBookmarks.liferayUserId", liferayUserId));
		}
		if(id!=null && id>0){
			DetachedCriteria collectionSubquery = DetachedCriteria.forClass(CollectionContent.class, "collectionContent");
			collectionSubquery.setProjection(Projections.property("collectionContent.savedBookmarks.id"));
			collectionSubquery.add(Restrictions.eq("collectionContent.collection.id",id));
			collectionSubquery.add(Restrictions.isNotNull("collectionContent.savedBookmarks.id"));
			criteria.add(Subqueries.propertyNotIn("savedBookmarks.id",collectionSubquery));
		}
		searches = criteria.list();
		return searches;
	}

	@Override
	public List<SavedBookmarks> getSavedSearchesByIdsAndUserid(List<Long> bookmarksOut, Long liferayUserId) {
		List<SavedBookmarks> searches = null;
		Criteria criteria = getSession().createCriteria(getPersistentClass(), "savedBookmarks");
		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		if(liferayUserId>0){
			criteria.add(Restrictions.eq("savedBookmarks.liferayUserId", liferayUserId));
			if(bookmarksOut!=null && bookmarksOut.size()>0){
				Disjunction restrinction = Restrictions.disjunction();
				for(long targetId:bookmarksOut){
					restrinction.add(Restrictions.eq("savedBookmarks.id",targetId));
				}
				criteria.add(restrinction);
			}
			searches = criteria.list();
		}
		return searches;
	}

}
