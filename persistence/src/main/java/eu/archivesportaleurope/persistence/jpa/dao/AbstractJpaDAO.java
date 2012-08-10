package eu.archivesportaleurope.persistence.jpa.dao;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.HibernateException;

import eu.apenet.persistence.dao.GenericAbstractDAO;
import eu.archivesportaleurope.persistence.jpa.JpaUtil;
/**
 * 
 * The abstract DAO that implements the basic CRUD operations and other nice features based in Criteria API.
 * 
 * @author bverhoef, Christian Bauer, Paul
 *
 * @param <T>
 * @param <ID>
 */
public abstract class AbstractJpaDAO <T, ID extends Serializable> extends GenericAbstractDAO <T,ID> {
	
	 public final void storeAndDeleteBatch(Collection entitiesToBeStored, Collection entitiesToBeDeleted) {
         try {
                 JpaUtil.beginDatabaseTransaction();
                 for (Object entity : entitiesToBeStored) {
                         getEntityManager().merge(entity);
                 }
                 for (Object entity : entitiesToBeDeleted) {
                	 getEntityManager().remove(entity);
                 }                        
                 JpaUtil.commitDatabaseTransaction();
         } catch (HibernateException de) {
                 JpaUtil.rollbackDatabaseTransaction();
                 throw de;
         }
	 }


	
	@Override

	public final T findById(ID id) {
		return (T)  getEntityManager().find(getPersistentClass(), id);
	}

    @Override
    @SuppressWarnings("unchecked")
	public final T findById(ID id, Class clazz){
        return (T) getEntityManager().find(clazz, id);
    }

	@Override
	public List<T> findAll() {
		CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(getPersistentClass());
		Root<T> from = criteriaQuery.from(getPersistentClass());
		CriteriaQuery<T> select = criteriaQuery.select(from);
		TypedQuery<T> typedQuery = getEntityManager().createQuery(select);
		return typedQuery.getResultList();
	}



	@Override
	public final void store(T entity) {
		try {
			JpaUtil.beginDatabaseTransaction();
			getEntityManager().merge(entity);
			JpaUtil.commitDatabaseTransaction();
		} catch (HibernateException de) {
			JpaUtil.rollbackDatabaseTransaction();
			throw de;
		}
	}

	
	@Override
	public final void store(Collection<T> entities) {
		try {
			JpaUtil.beginDatabaseTransaction();
			for (T entity : entities) {
				getEntityManager().merge(entity);
			}
			JpaUtil.commitDatabaseTransaction();
		} catch (HibernateException de) {
			JpaUtil.rollbackDatabaseTransaction();
			throw de;
		}
	}

	@Override
	public final void delete(T entity) {
		try {
			JpaUtil.beginDatabaseTransaction();
			getEntityManager().remove(entity);
			JpaUtil.commitDatabaseTransaction();
		} catch (HibernateException de) {
			JpaUtil.rollbackDatabaseTransaction();
			throw de;
		}
	}
	
	
	
	
    @Override
    public final T update (T entity) {
    	try {
			JpaUtil.beginDatabaseTransaction();
			getEntityManager().merge(entity);
			JpaUtil.commitDatabaseTransaction();
		} catch (HibernateException de) {
			JpaUtil.rollbackDatabaseTransaction();
			throw de;
		}
		return entity;
    }
	
	

    @Override
    public final T insertSimple (T entity) {
    	getEntityManager().persist(entity);
        return entity;
    }
    
  
    @Override
    public final T updateSimple (T entity) {
    	getEntityManager().merge(entity);
        return entity;
    }

    
    @Override
    public final void deleteSimple (T entity) {
    	getEntityManager().remove(entity);
    }
	


	protected final EntityManager getEntityManager() {
		return JpaUtil.getEntityManager();
	}

	public final  void flush() {
		getEntityManager().flush();
	}

	public final void clear() {
		getEntityManager().clear();
	}
}
