package eu.apenet.persistence.dao;

import eu.apenet.persistence.vo.AbstractContent;
import java.util.List;

import eu.apenet.persistence.vo.Ead;
import eu.apenet.persistence.vo.HgSgFaRelation;

public interface HgSgFaRelationDAO extends GenericDAO<HgSgFaRelation, Long> {

    public List<HgSgFaRelation> getHgSgFaRelations(Integer id, Class<? extends AbstractContent> clazz, Boolean published);

    public Long countHgSgFaRelations(Integer id, Class<? extends AbstractContent> clazz, Boolean published);

    public boolean existHgFaRelations(Integer hgId, Integer faId);

    public boolean existSgFaRelations(Integer sgId, Integer faId);

    public HgSgFaRelation getHgSgFaRelationByHgFaCombination(Integer hgSgId, Integer faId);
}
