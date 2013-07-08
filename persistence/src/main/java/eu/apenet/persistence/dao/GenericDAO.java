package eu.apenet.persistence.dao;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import org.hibernate.HibernateException;

/**
 * All CRUD (create, read, update, delete) basic data access operations are
 * isolated in this interface and shared accross all DAO implementations.
 * The current design is for a state-management oriented persistence layer
 * (for example, there is no UDPATE statement function) that provides
 * automatic transactional dirty checking of business objects in persistent
 * state.
 * Also it is adapted to the specific entitys and needs from APEnet.
 *
 * @author Paul, Bastiaan
 */
public interface GenericDAO<T, ID extends Serializable> {

    /* Temporal solution, resolving problems of compact rollback and avoiding corrupt states of DB */
	
	public abstract void storeAndDeleteBatch(Collection entitiesToBeStored, Collection entitiesToBeDeleted);
    
	public abstract T insertSimple(T entity);

	public abstract T updateSimple(T entity);

	public abstract void deleteSimple(T entity);
	
	//public abstract T findById(ID id, boolean lock); Pending of discussion due to deprecated methods.

    public abstract List<T> findAll();

    //public abstract T insert(T entity); Pending of discussion against store name.
    
    public abstract T update(T entity);

    public abstract void delete(T entity);
    public abstract void delete(Collection<T> entities);
    
    /* Refactoring signatures of down class from Bastiaan proposal */
    
    public abstract T findById(ID id);
    public abstract T findById(ID id, Class clazz);

	public abstract List<T> findByIds(Collection<ID> ids);
	
	public abstract List<T> findByExample(T exampleInstance, Collection<String> excludeProperty);
	
	public abstract List<T> findByExample(T exampleInstance);
	
	public abstract T store(T entity);
	
	public abstract void store(Collection<T> entities);
	
	//public void deleteById(Class theClass, Serializable id);
	
}