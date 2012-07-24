package eu.apenet.persistence.hibernate;

import java.sql.BatchUpdateException;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.LockOptions;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.exception.SQLGrammarException;

/**
 * Basic Hibernate utility class
 * 
 * @author bverhoef
 *
 */
public class HibernateUtil {

    private static final Logger log = Logger.getLogger(HibernateUtil.class);

	private static SessionFactory sessionFactory;
	
	private static Configuration configuration;

	private static final ThreadLocal<Session> threadSession = new ThreadLocal<Session>();

	private static final ThreadLocal<Transaction> threadTransaction = new ThreadLocal<Transaction>();


	public static void init(String configFile) {
		log.debug("HibernateUtil: init() method is called");
		if (log.isDebugEnabled()) {
			log.debug("Loading hibernate configuration file: " + configFile);
		}
		configuration = new Configuration().configure(configFile);
		sessionFactory = configuration.buildSessionFactory();
		if (log.isDebugEnabled()) {
			log.debug("Finished loading hibernate configuration file: " + configFile);
		}
	}

    public static void init(String configFile, String datasourceValue) {
    	log.debug("HibernateUtil: init() method is called");
		if (log.isDebugEnabled()) {
			log.debug("Loading hibernate configuration file: " + configFile);
		}
		configuration = new Configuration();
        configuration.setProperty("hibernate.connection.datasource", "java:comp/env/" + datasourceValue);
        configuration = configuration.configure(configFile);

        sessionFactory = configuration.buildSessionFactory();
		if (log.isDebugEnabled()) {
			log.debug("Finished loading hibernate configuration file: " + configFile);
            log.debug("Finished initialization with datasource: " + datasourceValue);
		}
	}
	
	protected static void init(Configuration configuration) {
		log.debug("HibernateUtil: init() method is called");
		if (log.isDebugEnabled()) {
			log.debug("Loading hibernate configuration ");
		}
		sessionFactory = configuration.buildSessionFactory();
		if (log.isDebugEnabled()) {
			log.debug("Finished loading hibernate configuration ");
		}
	}

	public static Configuration getConfiguration() {
		log.debug("HibernateUtil: getConfiguration() method is called");
		if (configuration != null){
			return configuration;
		}else {
			throw new BadConfigurationException("Hibernate not configured");
		}
	}

	private static SessionFactory getSessionFactory() {
		log.debug("HibernateUtil: getSessionFactory() method is called");
		if (sessionFactory != null){
			return sessionFactory;
		}else {
			throw new BadConfigurationException("No SessionFactory configured");
		}
	}
	
	public static void closeSessionFactory(){
		log.debug("HibernateUtil: closeSessionFactory() method is called");
		if (sessionFactory != null){
			sessionFactory.close();
		}
	}

	/**
	 * Retrieves the current Session local to the thread.
	 * <p/>
	 * If no Session is open, opens a new Session for the running thread.
	 * 
	 * @return Session
	 */
	public static Session getDatabaseSession() {
		log.debug("HibernateUtil: getDatabaseSession() method is called");
		Session session = threadSession.get();
		if (session == null) {
			if (log.isDebugEnabled()) {
				log.debug("Opening new Session for this thread.");
			}
			session = getSessionFactory().openSession();
			threadSession.set(session);
		}

		return session;
	}

	/**
	 * Closes the Session local to the thread.
	 */
	public static void closeDatabaseSession() {
		log.debug("HibernateUtil: closeDatabaseSession() method is called");
		Session session = threadSession.get();
		threadSession.remove();
		if (session != null && session.isOpen()) {
			if (log.isDebugEnabled()) {
				log.debug("Closing Session of this thread.");
			}
			session.close();
		}
	}


	/**
	 * Start a new database transaction.
	 */
	public static void beginDatabaseTransaction() {
		log.debug("HibernateUtil: beginDatabaseTransaction() method is called");
		Transaction tx = threadTransaction.get();

		if (tx == null) {
			if (log.isDebugEnabled()) {
				log.debug("Starting new database transaction in this thread.");
			}
			tx = getDatabaseSession().beginTransaction();
			threadTransaction.set(tx);
		}

	}

	/**
	 * Commit the database transaction.
	 */
	public static void commitDatabaseTransaction() {
		log.debug("HibernateUtil: commitDatabaseTransaction() method is called");
		Transaction tx = threadTransaction.get();
		try {
			if (tx != null && !tx.wasCommitted() && !tx.wasRolledBack()) {
				if (log.isDebugEnabled()) {
					log.debug("Committing database transaction of this thread.");
				}
				tx.commit();
			}
			threadTransaction.remove();
		}catch (SQLGrammarException sqe){
			log.error(sqe);
			if (sqe.getSQLException() instanceof BatchUpdateException){
				BatchUpdateException bue = (BatchUpdateException) sqe.getSQLException();
				if (bue.getNextException() != null){
					log.error(bue.getNextException().getMessage(), bue.getNextException());
				}
			}
			
			rollbackDatabaseTransaction();
			throw sqe;
		}catch (HibernateException ex) {
			log.error(ex);
			rollbackDatabaseTransaction();
			throw ex;
		}
	}

	/**
	 * rollback the database transaction.
	 */
	public static void rollbackDatabaseTransaction() {
		log.debug("HibernateUtil: rollbackDatabaseTransaction() method is called");
		Transaction tx = threadTransaction.get();
		threadTransaction.remove();
		if (tx != null && !tx.wasCommitted() && !tx.wasRolledBack()) {
			if (log.isDebugEnabled()) {
				log.debug("Trying to rollback database transaction of this thread.");
			}
			tx.rollback();
		}
	}

	public static boolean sessionClosed(){
		log.debug("HibernateUtil: sessionClosed() method is called");
		return threadSession.get() == null;
	}
	public static boolean noTransaction(){
		log.debug("HibernateUtil: noTransaction() method is called");
		return threadTransaction.get() == null;
	}

}
