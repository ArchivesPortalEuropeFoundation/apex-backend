/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.apenet.persistence.hibernate;

import eu.apenet.persistence.dao.ApiKeyDAO;
import eu.apenet.persistence.vo.ApiKey;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author kaisar
 */
public class ApiKeyHibernateDAO extends AbstractHibernateDAO<ApiKey, Integer> implements ApiKeyDAO {

    @Override
    public ApiKey findByEmail(String emailAddress) {
        Criteria criteria = getSession().createCriteria(getPersistentClass(), "apiKey");
        criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        criteria.add(Restrictions.like("emailAddress", emailAddress, MatchMode.EXACT).ignoreCase());
        return (ApiKey) criteria.uniqueResult();
    }
}
