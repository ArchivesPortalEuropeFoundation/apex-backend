package eu.apenet.oaiserver.config.other.dao;

import java.util.HashMap;
import java.util.Map;



/**
 * Defines all DAOs and the concrete factories to get the concrete DAOs.
 * <p>
 * To get a concrete DAOFactory, call one of the classes that extend this factory.
 * <p>
 * Implementation: If you write a new DAO, this class has to know about it.
 * If you add a new persistence mechanism, add an additional concrete factory
 * for it as a constant, like <tt>HIBERNATE</tt>.
 *
 * @author Paul
 */
public abstract class DAOFactory {

    public static final Class<HibernateDAOFactory> HIBERNATE = eu.apenet.oaiserver.config.other.dao.HibernateDAOFactory.class;
    public static final Class<?> DEFAULT;
    @SuppressWarnings("rawtypes")
    private static Map<Class<? extends GenericDAO>, GenericDAO> daos = new HashMap<Class<? extends GenericDAO>, GenericDAO>();


    /*
     *  For the moment we use only Hibernate factory, this method can grow f.i. throught a config.properties
     */
    static {
        try {
            DEFAULT = HIBERNATE;
        } catch (Throwable ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

    /*
     * Factory method for instantiation of default factory.
     */
    public static DAOFactory instance() {
        try {
            return instance(DEFAULT);
        } catch (Exception ex) {
            throw new RuntimeException("Can't create default DAOFactory", ex);
        }
    }

    /*
     * Factory method for instantiation of concrete factories.
     */
    public static DAOFactory instance(Class<?> factory) {
        try {
            return (DAOFactory) factory.newInstance();
        } catch (Exception ex) {
            throw new RuntimeException(
                    "Can't create DAOFactory: " + factory, ex);
        }
    }

    /*
     * Use this method to instantiate DAOs from subclasses
     */
    @SuppressWarnings("rawtypes")
    protected GenericDAO instantiateDAO( Class<? extends GenericDAO> daoClass) {
        try {
            GenericDAO dao = daos.get(daoClass);
            if (dao == null){
                synchronized (daos){
                    dao = (GenericDAO) daoClass.newInstance();
                    daos.put(daoClass, dao);
                }
            }
            return dao;
        } catch (Exception ex) {
            throw new RuntimeException(
                    "Can not instantiate DAO: " + daoClass, ex);
        }
    }

    public abstract EadObjectDAO getEadObjectDAO();
    public abstract ResumptionTokenDAO getResumptionTokenDAO();
}