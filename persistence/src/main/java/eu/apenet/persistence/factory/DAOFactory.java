package eu.apenet.persistence.factory;

import java.util.HashMap;
import java.util.Map;

import eu.apenet.persistence.dao.AiAlternativeNameDAO;
import eu.apenet.persistence.dao.ArchivalInstitutionDAO;
import eu.apenet.persistence.dao.ArchivalInstitutionOaiPmhDAO;
import eu.apenet.persistence.dao.CLevelDAO;
import eu.apenet.persistence.dao.CouAlternativeNameDAO;
import eu.apenet.persistence.dao.CountryDAO;
import eu.apenet.persistence.dao.CpfContentDAO;
import eu.apenet.persistence.dao.DptUpdateDAO;
import eu.apenet.persistence.dao.EadContentDAO;
import eu.apenet.persistence.dao.EadDAO;
import eu.apenet.persistence.dao.EseDAO;
import eu.apenet.persistence.dao.EseStateDAO;
import eu.apenet.persistence.dao.FileStateDAO;
import eu.apenet.persistence.dao.FileTypeDAO;
import eu.apenet.persistence.dao.FindingAidDAO;
import eu.apenet.persistence.dao.GenericDAO;
import eu.apenet.persistence.dao.HoldingsGuideDAO;
import eu.apenet.persistence.dao.QueueItemDAO;
import eu.apenet.persistence.dao.LangDAO;
import eu.apenet.persistence.dao.MetadataFormatDAO;
import eu.apenet.persistence.dao.ResumptionTokenDAO;
import eu.apenet.persistence.dao.SentMailRegisterDAO;
import eu.apenet.persistence.dao.UpFileDAO;
import eu.apenet.persistence.dao.UploadMethodDAO;
import eu.apenet.persistence.dao.UserDAO;
import eu.apenet.persistence.dao.UserRoleDAO;
import eu.apenet.persistence.dao.WarningsDAO;
import eu.apenet.persistence.exception.PersistenceException;


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

    public static final Class<HibernateDAOFactory> HIBERNATE = eu.apenet.persistence.factory.HibernateDAOFactory.class;
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
			throw new PersistenceException(
				"Can't create default DAOFactory", ex);
		}
    }
    
    /*
     * Factory method for instantiation of concrete factories.
     */
    public static DAOFactory instance(Class<?> factory) {
        try {
            return (DAOFactory) factory.newInstance();
        } catch (Exception ex) {
            throw new PersistenceException(
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
            throw new PersistenceException(
            	"Can not instantiate DAO: " + daoClass, ex);
        }
    }
    
    public abstract SentMailRegisterDAO getSentMailRegisterDAO();
    public abstract FindingAidDAO getFindingAidDAO();
    public abstract UpFileDAO getUpFileDAO();
    public abstract HoldingsGuideDAO getHoldingsGuideDAO();
    public abstract WarningsDAO getWarningsDAO();
    public abstract ArchivalInstitutionDAO getArchivalInstitutionDAO();
    public abstract UserDAO getUserDAO();
    public abstract CountryDAO getCountryDAO();
    public abstract LangDAO getLangDAO();
    public abstract EseDAO getEseDAO();
    public abstract FileStateDAO getFileStateDAO();
    public abstract UploadMethodDAO getUploadMethodDAO();
    public abstract FileTypeDAO getFileTypeDAO();
    public abstract EseStateDAO getEseStateDAO();
    public abstract ArchivalInstitutionOaiPmhDAO getArchivalInstitutionOaiPmhDAO();
    public abstract CLevelDAO getCLevelDAO();
    public abstract EadContentDAO getEadContentDAO();    
	public abstract AiAlternativeNameDAO getAiAlternativeNameDAO();
	public abstract CouAlternativeNameDAO getCouAlternativeNameDAO();
	public abstract CpfContentDAO getCpfContentDAO();
	public abstract DptUpdateDAO getDptUpdateDAO();
	public abstract MetadataFormatDAO getMetadataFormatDAO();
	public abstract ResumptionTokenDAO getResumptionTokenDAO();
	public abstract QueueItemDAO getQueueItemDAO();
	public abstract UserRoleDAO getUserRoleDAO();
    public abstract EadDAO getEadDAO();
}