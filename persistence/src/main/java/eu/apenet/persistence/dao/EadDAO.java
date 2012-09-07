package eu.apenet.persistence.dao;

import java.util.Collection;
import java.util.List;

import eu.apenet.persistence.vo.Ead;

public interface EadDAO extends GenericDAO<Ead, Integer> {
	
	public Long countEads(Ead eadExample );
	public boolean existEads(Ead eadExample );
	public Integer isEadidIndexed(String eadid, Integer aiId, Class<? extends Ead> clazz);

	public Integer isEadidUsed(String eadid, Integer aiId, Class<? extends Ead> clazz);

    public List<Ead> getEadsByStates(Collection<String> fileStates, Class<? extends Ead> clazz);

	public List<Ead> getEadPage(Collection<String> fileStates, Integer aiId,
			String sort, boolean ascending, Integer pageNumber,
			Integer pageSize, Class<? extends Ead> clazz);

	public List<Ead> getMatchEads(String queries, Integer ai, Integer page,
			Integer pageSize, String orderBy, Boolean orderDecreasing,
			Class<? extends Ead> clazz);

	public Long getMatchCountEads(String queries, Integer ai, Class<? extends Ead> clazz);
	
	List<Ead> getEadsByAiId(Class<? extends Ead> clazz, Integer aiId);
	
	public Ead getEadByEadid(Class<? extends Ead> clazz, Integer aiId, String eadid);
	public List<Integer> getAllIds(Class<? extends Ead> clazz, Integer aiId);
	public Long getTotalCountOfUnits();
	public Long getTotalCountOfUnitsWithDao();
	public Long countFilesbyInstitution(Class<? extends Ead> clazz, Integer aiId);
	public List<Ead> getEads(Ead eadExample, int firstResult, int maxResult);
}
