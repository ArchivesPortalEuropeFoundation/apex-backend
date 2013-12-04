package eu.apenet.persistence.hibernate;

import eu.apenet.persistence.dao.DptUpdateDAO;
import eu.apenet.persistence.vo.DptUpdate;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import java.util.List;

/**
 * User: Yoann Moranville
 * Date: 23/05/2012
 *
 * @author Yoann Moranville
 */
public class DptUpdateHibernateDAO extends AbstractHibernateDAO<DptUpdate, Long> implements DptUpdateDAO {

    @Override
    public DptUpdate doesVersionExist(String version) {
        Criteria criteria = getSession().createCriteria(getPersistentClass());
        List<DptUpdate> result = criteria.add(Restrictions.eq("version", version)).list();
        if(result.size() > 0)
            return result.get(0);
        return null;
    }
}
