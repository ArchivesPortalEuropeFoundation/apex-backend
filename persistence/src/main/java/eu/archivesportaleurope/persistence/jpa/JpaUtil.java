package eu.archivesportaleurope.persistence.jpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.apache.log4j.Logger;

import eu.apenet.persistence.exception.PersistenceException;

/**
 * Basic Hibernate utility class
 * 
 * @author bverhoef
 *
 */
public class JpaUtil {
	private static final int MAX_ERROR_LINES = 2;

    private static final Logger LOGGER = Logger.getLogger(JpaUtil.class);

	private static EntityManagerFactory entityManagerFactory;
	
	private static final ThreadLocal<EntityManager> threadEntityManager = new ThreadLocal<EntityManager>();

	private static final ThreadLocal<EntityTransaction> threadTransaction = new ThreadLocal<EntityTransaction>();

	

	public static void init() {
		if (entityManagerFactory == null){
			entityManagerFactory = Persistence.createEntityManagerFactory("ape-database");
		}
	}



	
	public static void closeEntityManagerFactory(){
		if (entityManagerFactory != null){
			entityManagerFactory.close();
		}
	}

	/**
	 * Retrieves the current Session local to the thread.
	 * <p/>
	 * If no Session is open, opens a new Session for the running thread.
	 * 
	 * @return Session
	 */
	public static EntityManager getEntityManager() {
		EntityManager entityManager = threadEntityManager.get();
		if (entityManager == null) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Opening new Session for this thread.");
			}
			entityManager = entityManagerFactory.createEntityManager();
			threadEntityManager.set(entityManager);
		}

		return entityManager;
	}

	/**
	 * Closes the Session local to the thread.
	 */
	public static void closeDatabaseSession() {
		EntityManager entityManager = threadEntityManager.get();
		threadEntityManager.remove();
		if (entityManager != null && entityManager.isOpen()) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Closing Session of this thread.");
			}
			entityManager.close();
		}
	}


	/**
	 * Start a new database transaction.
	 */
	public static void beginDatabaseTransaction() {
		EntityTransaction tx = threadTransaction.get();

		if (tx == null) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Starting new database transaction in this thread.");
			}
			tx = getEntityManager().getTransaction();
			threadTransaction.set(tx);
			tx.begin();
		}

	}

	/**
	 * Commit the database transaction.
	 */
	public static void commitDatabaseTransaction() {
		EntityTransaction tx = threadTransaction.get();
		try {
			if (tx != null &&  !tx.getRollbackOnly()) {
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("Committing database transaction of this thread.");
				}
				tx.commit();
			}
			threadTransaction.remove();
		}catch (Exception ex) {
			LOGGER.error(generateThrowableLog(ex));
			rollbackDatabaseTransaction();
			throw new PersistenceException(ex);
		}
	}

	/**
	 * rollback the database transaction.
	 */
	public static void rollbackDatabaseTransaction() {
		EntityTransaction tx = threadTransaction.get();
		threadTransaction.remove();
		if (tx != null) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Trying to rollback database transaction of this thread.");
			}
			tx.rollback();
		}
	}

	public static boolean entityManagerClosed(){
		return threadEntityManager.get() == null;
	}
	public static boolean noTransaction(){
		return threadTransaction.get() == null;
	}
    public static String generateThrowableLog(Throwable throwable){
    	String result = "";
    	result+= throwable.getClass().getName()+ " " +throwable.getMessage()  + "\n";
    	result+= generateThrowableStackTraceLog(throwable.getStackTrace());
    	result+=generateThrowableCauseLog(throwable.getCause(), 0);
    	return result;
    }
    private static String generateThrowableCauseLog(Throwable throwable, int depth){
    	String result = "";
    	if (throwable != null){
    		result+= "Caused by: " +  throwable.getClass().getName()+ " " + throwable.getMessage()  +"\n";
    		result+= generateThrowableStackTraceLog(throwable.getStackTrace());
    		result+=generateThrowableCauseLog(throwable.getCause(), depth++);
    	}
    	return result;
    }
    private static String generateThrowableStackTraceLog(StackTraceElement[] elements){
    	String result = "";
    	for (int i = 0; i < MAX_ERROR_LINES && i < elements.length ;i++){
    		StackTraceElement element = elements[i];
    		result += "\t" + element.getClassName() + "." + element.getMethodName() + "(" + element.getFileName() + ":" + element.getLineNumber() + ")\n" ;
    	}
    	if (elements.length > MAX_ERROR_LINES){
    		result += "\t... " + (elements.length -MAX_ERROR_LINES) + " more\n";
    	}
    	return result;
    }
}
