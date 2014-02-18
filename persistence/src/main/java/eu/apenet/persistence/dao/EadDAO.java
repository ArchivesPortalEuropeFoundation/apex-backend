package eu.apenet.persistence.dao;

import java.util.List;

import eu.apenet.persistence.vo.Ead;

public interface EadDAO extends GenericDAO<Ead, Integer> {

	public Integer isEadidIndexed(String eadid, Integer aiId, Class<? extends Ead> clazz);

	public Integer isEadidUsed(String eadid, Integer aiId, Class<? extends Ead> clazz);

	public List<Ead> getEads(ContentSearchOptions eadSearchOptions);

	public Long countEads(ContentSearchOptions eadSearchOptions);

	public boolean existEads(ContentSearchOptions eadSearchOptions);

	public Long countUnits(ContentSearchOptions eadSearchOptions);

	public Long countDaos(ContentSearchOptions eadSearchOptions);
	
	public Long countChos(ContentSearchOptions eadSearchOptions);

	List<Ead> getEadsByAiId(Class<? extends Ead> clazz, Integer aiId);

	public Ead getEadByEadid(Class<? extends Ead> clazz, Integer aiId, String eadid);

	public Ead getEadByEadid(Class<? extends Ead> clazz, String repositorycode, String eadid);
	
	public List<Integer> getAllIds(Class<? extends Ead> clazz, Integer aiId);

	public Long getTotalCountOfUnits();

	public Long getTotalCountOfUnitsWithDao();


}
