package eu.apenet.persistence.dao;

import eu.apenet.persistence.vo.EacCpf;

import java.util.List;

public interface EacCpfDAO extends GenericDAO<EacCpf, Integer> {

    public EacCpf getFirstPublishedEacCpfByIdentifier(String identifier, boolean isPublished);

    public EacCpf getEacCpfByIdentifier(Integer aiId, String identifier);

    public EacCpf getEacCpfByIdentifier(String repositorycode, String identifier, boolean onlyPublished);

    public List<EacCpf> getEacCpfs(ContentSearchOptions contentSearchOptions);

    public long countEacCpfs(ContentSearchOptions contentSearchOptions);

    public Integer isEacCpfIdUsed(String identifier, Integer aiId, Class<? extends EacCpf> clazz);

    public EacCpf getEacCpfById(Integer aiId, String cpfId);

    public List<Integer> getAllIds(Class<EacCpf> aClass, int aiId);

    public boolean existEacCpfs(ContentSearchOptions contentSearchOptions);
    
    public Integer isEacCpfIdIndexed(String eadid, Integer aiId, Class<? extends EacCpf> clazz);
}
