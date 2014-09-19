package eu.apenet.persistence.dao;
import eu.apenet.persistence.vo.EacCpf;

import java.util.List;

public interface EacCpfDAO extends GenericDAO<EacCpf, Integer> {


    public EacCpf getFirstPublishedEacCpfByIdentifier(String identifier);

    public EacCpf getEacCpfByIdentifier(Integer aiId, String identifier);

    public EacCpf getEacCpfByIdentifier(String repositorycode, String identifier);

    public EacCpf getPublishedEacCpfByIdentifier(String repositorycode, String identifier);

    public List<EacCpf> getEacCpfs(ContentSearchOptions contentSearchOptions);

    public long countEacCpfs(ContentSearchOptions contentSearchOptions);

    public Integer isEacCpfIdUsed(String identifier, Integer aiId, Class<? extends EacCpf> clazz);

    public EacCpf getEacCpfById(Integer aiId, String cpfId);

    public List<Integer> getAllIds(Class<EacCpf> aClass, int aiId);
    
    public List<EacCpf> getAllEacCpfsPublishedByArchivalInstitutionId(int aiId);

	public List<EacCpf> getAllEacCpfsByArchivalInstitutionId(int aiId);
	
	public boolean existEacCpfs(ContentSearchOptions contentSearchOptions);
}
