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
/**
 * 
 * The abstract DAO that implements the basic CRUD operations and other nice features based in Criteria API.
 * 
 * @author bverhoef, Christian Bauer, Paul
 *
 * @param <T>
 * @param <ID>
 */
public abstract class AbstractHibernateDAO <T, ID extends Serializable> extends GenericAbstractDAO <T,ID> {
	
	 public void storeAndDeleteBatch(Collection entitiesToBeStored, Collection entitiesToBeDeleted) {
         try {
                 HibernateUtil.beginDatabaseTransaction();
                 for (Object entity : entitiesToBeStored) {
                         getSession().saveOrUpdate(entity);
                 }
                 for (Object entity : entitiesToBeDeleted) {
                         getSession().delete(entity);
                 }                        
                 HibernateUtil.commitDatabaseTransaction();
         } catch (HibernateException de) {
                 HibernateUtil.rollbackDatabaseTransaction();
                 throw de;
         }
	 }

    /*
     * This behavior is refactoring and go up to GenericAbstractDAO class
     * 
     * 
	private Class<T> persistentClass;

	@SuppressWarnings("unchecked")
	public AbstractHibernateDAO() {
		this.persistentClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass())
				.getActualTypeArguments()[0];
	}


	protected Class<T> getPersistentClass() {
		return persistentClass;
	}*/

	
	
	@Override
	@SuppressWarnings("unchecked")
	public T findById(ID id) {
		return (T) getSession().get(getPersistentClass(), id);
	}

    @Override
    @SuppressWarnings("unchecked")
	public T findById(ID id, Class clazz){
        return (T) getSession().get(clazz, id);
    }

	/*
	 * This method looks like deprecated in respect to the lockMode. Pending of debate
	 * That's the reason because is commented in 
	 * 
	
    @Override
    @SuppressWarnings("unchecked")
    public T findById(ID id, boolean lock) {
        T entity;
        if (lock)
            //entity = (T) getSession().load(getPersistentClass(), id, LockMode.UPGRADE);
        	entity = (T) getSession().get(getPersistentClass(), id, LockMode.UPGRADE);
        	
        else
            entity = (T) getSession().load(getPersistentClass(), id);
        return entity;
    }*/
	
	
	@Override
	public List<T> findByIds(Collection<ID> ids) {
		Disjunction disjunction = Restrictions.disjunction();
		for (ID id : ids) {
			disjunction.add(Restrictions.eq("id", id));
		}
		return findByCriteria(disjunction);
	}
	
	@Override
	public List<T> findAll() {
		return findByCriteria();
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

	@Override
	public void store(T entity) {
		try {
			HibernateUtil.beginDatabaseTransaction();
			getSession().saveOrUpdate(entity);
			HibernateUtil.commitDatabaseTransaction();
		} catch (HibernateException de) {
			HibernateUtil.rollbackDatabaseTransaction();
			throw de;
		}
	}

	
	@Override
	public void store(Collection<T> entities) {
		try {
			HibernateUtil.beginDatabaseTransaction();
			for (T entity : entities) {
				getSession().saveOrUpdate(entity);
			}
			HibernateUtil.commitDatabaseTransaction();
		} catch (HibernateException de) {
			HibernateUtil.rollbackDatabaseTransaction();
			throw de;
		}
	}

	@Override
	public void delete(T entity) {
		try {
			HibernateUtil.beginDatabaseTransaction();
			getSession().delete(entity);
			HibernateUtil.commitDatabaseTransaction();
		} catch (HibernateException de) {
			HibernateUtil.rollbackDatabaseTransaction();
			throw de;
		}
	}
	
	
	
	
	//public void delete(Class theClass, Serializable id) throws HibernateException {
	
	//	public void deleteById(Class theClass, Serializable id) {
	//		getSession().delete(theClass, new Integer((id));
	//	}
	//	
	
    @Override
    public T update (T entity) {
    	try {
			HibernateUtil.beginDatabaseTransaction();
			getSession().update(entity);
			HibernateUtil.commitDatabaseTransaction();
		} catch (HibernateException de) {
			HibernateUtil.rollbackDatabaseTransaction();
			throw de;
		}
		return entity;
    }
	
	
	
	/*Simple methods. This methods are here to do easy the transition to the new structure of class.
	 * Pending of discussion about structure of HibernateUtil...*/
	
    @Override
    public T insertSimple (T entity) {
        getSession().save(entity);
        return entity;
    }
    
  
    @Override
    public T updateSimple (T entity) {
        getSession().update(entity);
        return entity;
    }

    
    @Override
    public void deleteSimple (T entity) {
        getSession().delete(entity);
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
		return HibernateUtil.getDatabaseSession();
	}
	public void flush() {
		getSession().flush();
	}

	public void clear() {
		getSession().clear();
	}
}
