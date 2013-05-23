package eu.apenet.persistence.hibernate;

import java.util.List;

import eu.apenet.persistence.vo.Ead;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import eu.apenet.persistence.dao.EadContentDAO;
import eu.apenet.persistence.vo.EadContent;
import eu.apenet.persistence.vo.FindingAid;
import eu.apenet.persistence.vo.HoldingsGuide;



public class EadContentHibernateDAO extends AbstractHibernateDAO<EadContent, Long> implements EadContentDAO{
	//private final Logger log = Logger.getLogger(getClass());
	@Override
	public EadContent getEadContentByFindingAidId(Integer findingAidId) {
		List<EadContent> result = findByCriteria(Restrictions.eq("faId", findingAidId));
		if (result.size() >0){
			return result.get(0);
		}
		return null;
	}

	@Override
	public EadContent getEadContentByHoldingsGuideId(Integer holdingsGuideId) {
		List<EadContent> result = findByCriteria(Restrictions.eq("hgId", holdingsGuideId));
		if (result.size() >0){
			return result.get(0);
		}
		return null;
	}

    @Override
    public EadContent getEadContentBySourceGuideId(Integer sgId) {
        List<EadContent> result = findByCriteria(Restrictions.eq("sgId", sgId));
        if (result.size() > 0)
            return result.get(0);
        return null;
    }

    
    @Override
	public EadContent getEadContentByEadid(String eadid){
        List<EadContent> result = findByCriteria(Restrictions.eq("eadid", eadid));
        if(result.size() > 0)
			return result.get(0);
        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public EadContent getEadContentByFileId(Integer fileId, Class<? extends Ead> clazz) {
        Criteria criteria = getSession().createCriteria(getPersistentClass(), "eadContent");
		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        List<EadContent> result;
        if(clazz.equals(FindingAid.class))
            result = criteria.add(Restrictions.eq("faId", fileId)).list();
        else if(clazz.equals(HoldingsGuide.class))
            result = criteria.add(Restrictions.eq("hgId", fileId)).list();
        else
            result = criteria.add(Restrictions.eq("sgId", fileId)).list();

        if(result.size() > 0)
			return result.get(0);
        return null;
    }
}