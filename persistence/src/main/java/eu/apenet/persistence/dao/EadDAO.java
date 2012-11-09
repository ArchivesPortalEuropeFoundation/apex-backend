package eu.apenet.persistence.dao;

import java.util.Collection;
import java.util.List;

import eu.apenet.persistence.vo.Ead;

public interface EadDAO extends GenericDAO<Ead, Integer> {

	public Integer isEadidIndexed(String eadid, Integer aiId, Class<? extends Ead> clazz);

	public Integer isEadidUsed(String eadid, Integer aiId, Class<? extends Ead> clazz);

	public List<Ead> getEads(EadSearchOptions eadSearchOptions);
	public Long countEads(EadSearchOptions eadSearchOptions);
	public boolean existEads(EadSearchOptions eadSearchOptions);
	
	List<Ead> getEadsByAiId(Class<? extends Ead> clazz, Integer aiId);
	
	public Ead getEadByEadid(Class<? extends Ead> clazz, Integer aiId, String eadid);
	public List<Integer> getAllIds(Class<? extends Ead> clazz, Integer aiId);
	public Long getTotalCountOfUnits();
	public Long getTotalCountOfUnitsWithDao();
	public Long countFilesbyInstitution(Class<? extends Ead> clazz, Integer aiId);
}
