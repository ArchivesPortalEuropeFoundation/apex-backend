package eu.apenet.persistence.dao;

import java.util.List;

import eu.apenet.persistence.vo.Collection;
import eu.apenet.persistence.vo.CollectionContent;

public interface CollectionContentDAO extends GenericDAO<CollectionContent, Long>{

	public List<CollectionContent> getCollectionContentsByCollectionId(long id);
	public List<CollectionContent> getCollectionContentsByCollectionId(Long id,Boolean collectionContentType,int pageNumber,int pageSize);
	public List<CollectionContent> getAllCollectionContentWithoutIds(List<Long> collectionIds, Long id);
	public Long countCollectionContentsByCollectionId(Long id, Boolean type);
	public List<CollectionContent> getCollectionContentByElementId(String table, String elemetId);
}
