package eu.apenet.persistence.dao;


import eu.apenet.persistence.vo.EacCpf;

public interface EacCpfDAO extends GenericDAO<EacCpf, Integer> {
	public boolean existEacCpf(String identifier);;
	public EacCpf getEacCpfByIdentifier(Integer aiId, String identifier);
}
