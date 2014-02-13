package eu.apenet.persistence.dao;


import eu.apenet.persistence.vo.EacCpf;

public interface EacCpfDAO extends GenericDAO<EacCpf, Integer> {
	public Integer doesCpfExists(String cpfId);
	public EacCpf getEacByCpfId(Integer aiId, String cpfId);
}
