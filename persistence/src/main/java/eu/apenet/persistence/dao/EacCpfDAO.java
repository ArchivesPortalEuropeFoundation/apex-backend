package eu.apenet.persistence.dao;

import eu.apenet.persistence.vo.EacCpf;
import java.util.List;

public interface EacCpfDAO extends GenericDAO<EacCpf, Integer> {

    public boolean existEacCpf(String identifier);

    public EacCpf getEacCpfByIdentifier(Integer aiId, String identifier);

    public List<EacCpf> getEacCpfs(ContentSearchOptions contentSearchOptions);

    public long countEacCpfs(ContentSearchOptions contentSearchOptions);
}
