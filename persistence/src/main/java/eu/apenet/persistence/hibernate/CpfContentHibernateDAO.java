package eu.apenet.persistence.hibernate;

import eu.apenet.persistence.dao.CpfContentDAO;
import eu.apenet.persistence.vo.CpfContent;
import eu.apenet.persistence.vo.EadContent;
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import java.util.List;

/**
 * User: Yoann Moranville
 * Date: Mar 7, 2011
 *
 * @author Yoann Moranville
 */
public class CpfContentHibernateDAO extends AbstractHibernateDAO<CpfContent, Long> implements CpfContentDAO {

    @Override
    @SuppressWarnings("unchecked")
	public Long doesCpfExists(String cpfId){
        Criteria criteria = getSession().createCriteria(getPersistentClass()).setProjection(Projections.property("id"));
		List<Long> result = criteria.add(Restrictions.eq("cpfId", cpfId)).list();
        if (result.size() > 0)
            return result.get(0);
        return null;
    }

    @Override
    public CpfContent retrieveCpfContent(Long id){
        List<CpfContent> result = findByCriteria(Restrictions.eq("id", id));
		if (result.size() >0)
			return result.get(0);
		return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<CpfContent> retrieveCpfContentByArchivalInstitution(int pageNumber, int limit, int aiId){
        Criteria criteria = getSession().createCriteria(getPersistentClass());
        criteria.add(Restrictions.eq("archivalInstitution.aiId", aiId));
        criteria.setFirstResult(limit * pageNumber);
	    criteria.setMaxResults(limit);
        return criteria.list();
    }

    public Long countCpfContentByArchivalInstitution(int aiId){
        Criteria criteria = getSession().createCriteria(getPersistentClass()).setProjection(Projections.rowCount());
        Object result = criteria.uniqueResult();
        if(result != null)
            return (Long)result;
        return null;
    }
}
