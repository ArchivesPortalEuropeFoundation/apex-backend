package eu.apenet.persistence.hibernate;


import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import eu.apenet.persistence.dao.EacCpfDAO;
import eu.apenet.persistence.vo.EacCpf;
import eu.apenet.persistence.vo.Ead;

public class EacCpfHibernateDAO extends AbstractHibernateDAO<EacCpf, Integer> implements EacCpfDAO {

	private final Logger log = Logger.getLogger(getClass());
	
	@Override
    @SuppressWarnings("unchecked")
	public Integer doesCpfExists(String cpfId){
        Criteria criteria = getSession().createCriteria(getPersistentClass()).setProjection(Projections.property("id"));
		List<Integer> result = criteria.add(Restrictions.eq("cpfId", cpfId)).list();
        if (result.size() > 0)
            return result.get(0);
        return null;
    }
	
	@SuppressWarnings("unchecked")
	@Override
	public EacCpf getEacByCpfId(Integer aiId, String cpfId) {
		Criteria criteria = getSession().createCriteria(getPersistentClass(), "eac");
		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria = criteria.createAlias("eac.archivalInstitution", "archivalInstitution");
		criteria.add(Restrictions.eq("archivalInstitution.aiId", aiId));
		criteria.add(Restrictions.eq("cpfId", cpfId));
		List<EacCpf> list = criteria.list();
		if (list.size() > 0)
			return list.get(0);
		return null;
	}
}
