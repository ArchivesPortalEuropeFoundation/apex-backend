package eu.apenet.persistence.hibernate;

import java.util.Iterator;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import eu.apenet.persistence.dao.CollectionContentDAO;
import eu.apenet.persistence.vo.CollectionContent;

public class CollectionContentHibernateDAO extends AbstractHibernateDAO<CollectionContent, Long> implements CollectionContentDAO{

	
	private static final String BOOKMARK = "bookmark";
	
	@Override
	public List<CollectionContent> getCollectionContentsByCollectionId(long id) {
		List<CollectionContent> collectionContents = null;
		if(id>0){
			Criteria criteria = getSession().createCriteria(getPersistentClass(), "collectionContent");
			criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			criteria.add(Restrictions.eq("collectionContent.collection.id", id));
			collectionContents = criteria.list();
		}
		return collectionContents;
	}
	
	@Override
	public Long countCollectionContentsByCollectionId(Long id, Boolean type) {
		Criteria criteria = getCriteriaCollectionContentsByCollectionId(id,type,null,null);
		criteria.setProjection(Projections.rowCount());
		return (Long)criteria.uniqueResult();
	}
	
	@Override
	public List<CollectionContent> getCollectionContentsByCollectionId(Long id,Boolean isSavedSearch, int pageNumber, int pageSize) {
		Criteria criteria = getCriteriaCollectionContentsByCollectionId(id,isSavedSearch,pageNumber,pageSize);
		return criteria.list();
	}

	private Criteria getCriteriaCollectionContentsByCollectionId(Long id,Boolean isSavedSearch, Integer pageNumber, Integer pageSize) {
		Criteria criteria = getSession().createCriteria(getPersistentClass(), "collectionContent");
		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		if(id!=null && id>0){
			criteria.add(Restrictions.eq("collectionContent.collection.id", id));
			if(isSavedSearch!=null){
				if(isSavedSearch){
					criteria.add(Restrictions.isNotNull("collectionContent.eadSavedSearch.id"));
				}else{
					criteria.add(Restrictions.isNotNull("collectionContent.savedBookmarks.id"));
				}
			}
			if(pageNumber!=null && pageNumber>0){
				criteria.setFirstResult((pageNumber-1)*pageSize);
			}
			if(pageSize!=null && pageSize>0){
				criteria.setMaxResults(pageSize);
			}
		}
		return criteria;
	}

	@Override
	public List<CollectionContent> getAllCollectionContentWithoutIds(List<Long> collectionIds, Long id) {
		List<CollectionContent> collectionContents = null;
		if(id!=null && id>0){
			Criteria criteria = getSession().createCriteria(getPersistentClass(), "collectionContent");
			criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			criteria.add(Restrictions.eq("collectionContent.collection.id", id));
			Iterator<Long> iteratorIds = collectionIds.iterator();
			while(iteratorIds.hasNext()){
				criteria.add(Restrictions.not(Restrictions.eq("collectionContent.id", iteratorIds.next())));
			}
			collectionContents = criteria.list();
		}
		return collectionContents;
	}


	@Override
	public List<CollectionContent> getCollectionContentByElementId(String table, String elemetId) {
		List<CollectionContent> collectionContents = null;
		Criteria criteria = getSession().createCriteria(getPersistentClass(), "collectionContent"); //collection
		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

			if (table.equals(BOOKMARK))
				criteria.add(Restrictions.eq("collectionContent.savedBookmarks.id", Long.parseLong(elemetId)));
			else
				criteria.add(Restrictions.eq("collectionContent.eadSavedSearch.id", Long.parseLong(elemetId)));
		
			collectionContents = criteria.list();
			return collectionContents;
	}

}