package eu.apenet.persistence.dao;

import java.util.List;

import eu.apenet.persistence.vo.Collection;

public interface CollectionDAO extends GenericDAO<Collection, Long>{
	
	public List<Collection> getCollectionsByUserId(Long liferayUserId,Integer pageNumber, Integer pagesize);
	public Collection getCollectionById(Long id);
	public Long countCollectionsByUserId(Long liferayUserId);
	public Collection getCollectionByIdAndUserId(Long id, Long liferayUserId);
}
