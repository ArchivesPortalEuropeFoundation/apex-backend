package eu.apenet.persistence.hibernate;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Restrictions;

import eu.apenet.persistence.dao.GenericAbstractDAO;
import eu.archivesportaleurope.persistence.jpa.JpaUtil;
import eu.archivesportaleurope.persistence.jpa.dao.AbstractJpaDAO;
/**
 * 
 * The abstract DAO that implements the basic CRUD operations and other nice features based in Criteria API.
 * 
 * @author bverhoef, Paul
 *
 * @param <T>
 * @param <ID>
 */
@Deprecated
public abstract class AbstractHibernateDAO <T, ID extends Serializable> extends AbstractJpaDAO<T, ID> {


	
	
	@Override
	public List<T> findByIds(Collection<ID> ids) {
		Disjunction disjunction = Restrictions.disjunction();
		for (ID id : ids) {
			disjunction.add(Restrictions.eq("id", id));
		}
		return findByCriteria(disjunction);
	}
	

	@Override
	public List<T> findByExample(T exampleInstance, Collection<String> excludeProperty) {
		Example example = Example.create(exampleInstance);
		for (String exclude : excludeProperty) {
			example.excludeProperty(exclude);
		}
		   /* 
         * LIKE %<string>%
         * example.enableLike(MatchMode.ANYWHERE); 
         * 
         * LIKE <string>
         * example.enableLike();
         * 
         * Ignore case
         * example.ignoreCase(); 
         */
		return findByCriteria(example);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<T> findByExample(T exampleInstance) {
		Example example = Example.create(exampleInstance);
		return findByCriteria((Class<? extends T>) exampleInstance.getClass(), example);
	}




	/**
	 * Use this inside subclasses as a convenience method.
	 */
	@SuppressWarnings("unchecked")
	protected List<T> findByCriteria(Criterion... criterion) {
		Criteria crit = getSession().createCriteria(getPersistentClass());
		for (Criterion c : criterion) {
			crit.add(c);
		}
		return crit.list();
	}

	/**
	 * Use this inside subclasses as a convenience method.
	 */
	@SuppressWarnings("unchecked")
	protected List<T> findByCriteria(Class<? extends T> pClass, Criterion... criterion) {
		Criteria crit = getSession().createCriteria(pClass);
		for (Criterion c : criterion) {
			crit.add(c);
		}
		return crit.list();
	}


	protected Session getSession() {
		return (Session) JpaUtil.getEntityManager().getDelegate();
	}

}
