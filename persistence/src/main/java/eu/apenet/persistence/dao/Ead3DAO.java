package eu.apenet.persistence.dao;

import eu.apenet.persistence.vo.Ead3;

import java.util.List;

public interface Ead3DAO extends GenericDAO<Ead3, Integer> {

    public Ead3 getFirstPublishedEad3ByIdentifier(String identifier, boolean isPublished);

    public Ead3 getEad3ByIdentifier(Integer aiId, String identifier);

    public Ead3 getEad3ByIdentifier(String repositorycode, String identifier, boolean onlyPublished);

    public List<Ead3> getEad3s(ContentSearchOptions contentSearchOptions);

    public long countEad3s(ContentSearchOptions contentSearchOptions);

    public Long countUnits(ContentSearchOptions contentSearchOptions);

    public Integer isEad3IdUsed(String identifier, Integer aiId, Class<? extends Ead3> clazz);

    public Ead3 getEad3ById(Integer aiId, String ead3Id);

    public List<Integer> getAllIds(Class<Ead3> aClass, int aiId);

    public boolean existEad3s(ContentSearchOptions contentSearchOptions);
}
