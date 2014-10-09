package eu.apenet.persistence.dao;

import java.util.List;

import eu.apenet.persistence.vo.SavedBookmarks;


public interface SavedBookmarksDAO extends GenericDAO<SavedBookmarks, Long> {

	public SavedBookmarks getSavedBookmark(Long liferayUserId,long id);
	public Long countSavedBookmarks(Long liferayUserId);
	public List<SavedBookmarks> getSavedBookmarks(Long liferayUserId, int pageNumber, int pageSize);
	public List<SavedBookmarks> getSavedBookmarksOutOfCollectionByCollectionIdAndLiferayUser(Long id, long liferayUserId, int pageNumber ,int pageSize);
	public List<SavedBookmarks> getSavedSearchesByIdsAndUserid(List<Long> bookmarksOut, Long liferayUserId);
	public Long countSavedBookmarksOutOfCollectionByCollectionIdAndLiferayUser(Long id, long liferayUserId);
}
