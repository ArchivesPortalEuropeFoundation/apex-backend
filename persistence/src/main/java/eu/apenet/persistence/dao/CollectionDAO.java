package eu.apenet.persistence.dao;

import java.util.List;

import eu.apenet.persistence.vo.Collection;
import eu.apenet.persistence.vo.CollectionContent;

public interface CollectionDAO extends GenericDAO<Collection, Long>{
	
	public List<Collection> getCollectionsByUserId(Long liferayUserId,Integer pageNumber, Integer pagesize, String field, boolean showDesc);
	public Collection getCollectionById(Long id);
	public Long countCollectionsByUserId(Long liferayUserId);
	public Collection getCollectionByIdAndUserId(Long id, Long liferayUserId);
}
