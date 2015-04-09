package eu.apenet.oaiserver.config.other.dao;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;

/**
 * This class generalize common behavior from old AbstractHibernateDAO, offering to you a way to get
 * the current PersistentClass
 * @author paul
 *
 * @param <T>
 * @param <ID>
 */


public abstract class GenericAbstractDAO<T, ID extends Serializable> implements eu.apenet.persistence.dao.GenericDAO<T, ID> {

    private Class<T> persistentClass;

    @SuppressWarnings("unchecked")
    protected Class<T> getPersistentClass() {
        if (persistentClass == null) {
            persistentClass = (Class<T>)
                    ((ParameterizedType) getClass().getGenericSuperclass())
                            .getActualTypeArguments()[0];
        }
        return persistentClass;
    }
}

