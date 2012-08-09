package eu.apenet.persistence.hibernate;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.cfg.Configuration;

import eu.archivesportaleurope.persistence.jpa.JpaUtil;

/**
 * Basic Hibernate utility class
 * 
 * @author bverhoef
 *
 */
@Deprecated
public class HibernateUtil {

    private static final Logger log = Logger.getLogger(HibernateUtil.class);



	public static void init(String configFile) {

	}

    public static void init(String configFile, String datasourceValue) {

	}
	
	protected static void init(Configuration configuration) {

	}



	
	public static void closeSessionFactory(){
		JpaUtil.closeEntityManagerFactory();
	}

	/**
	 * Retrieves the current Session local to the thread.
	 * <p/>
	 * If no Session is open, opens a new Session for the running thread.
	 * 
	 * @return Session
	 */
	public static Session getDatabaseSession() {
		return (Session) JpaUtil.getEntityManager().getDelegate();
	}

	/**
	 * Closes the Session local to the thread.
	 */
	public static void closeDatabaseSession() {
		JpaUtil.closeDatabaseSession();
	}


	/**
	 * Start a new database transaction.
	 */
	public static void beginDatabaseTransaction() {
		JpaUtil.beginDatabaseTransaction();
	}

	/**
	 * Commit the database transaction.
	 */
	public static void commitDatabaseTransaction() {
		JpaUtil.commitDatabaseTransaction();
	}

	/**
	 * rollback the database transaction.
	 */
	public static void rollbackDatabaseTransaction() {
		JpaUtil.rollbackDatabaseTransaction();
	}

	public static boolean sessionClosed(){
		return JpaUtil.entityManagerClosed();
	}
	public static boolean noTransaction(){
		return JpaUtil.noTransaction();
	}

}
