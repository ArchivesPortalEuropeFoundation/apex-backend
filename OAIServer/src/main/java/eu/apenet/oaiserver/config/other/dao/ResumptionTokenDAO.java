package eu.apenet.oaiserver.config.other.dao;


import eu.apenet.oaiserver.config.other.vo.ResumptionToken;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import java.util.Date;
import java.util.List;

/**
 * Created by yoannmoranville on 09/04/15.
 */
public class ResumptionTokenDAO extends AbstractJpaDAO<ResumptionToken, Integer> implements GenericDAO<ResumptionToken, Integer> {
    public List<ResumptionToken> getOldResumptionTokensThan(Date referenceDate) {
        Criteria criteria = getSession().createCriteria(getPersistentClass(), "resumptionToken");
        criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        criteria.add(Restrictions.lt("expirationDate", referenceDate));
        return criteria.list();
    }
}
