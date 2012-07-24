package eu.apenet.dashboard.manual.tasks;

import eu.apenet.persistence.hibernate.HibernateUtil;
import org.apache.log4j.Logger;

import java.util.TimerTask;

/**
 * User: Yoann Moranville
 * Date: 3/28/11
 *
 * @author Yoann Moranville
 */
public abstract class AbstractTask extends TimerTask {
    protected static final Logger LOG = Logger.getLogger(AbstractTask.class);

    protected int aiId;
    protected int id;

    protected AbstractTask(int aiId, int id){
        this.aiId = aiId;
        this.id = id;
    }

    public static void insertPipelineStatus(Status status){
        HibernateUtil.beginDatabaseTransaction();
        try {
            //Do something
            HibernateUtil.commitDatabaseTransaction();
        } catch (Exception e){
            HibernateUtil.rollbackDatabaseTransaction();
        } finally {
            HibernateUtil.closeDatabaseSession();
        }
    }

    public enum Status {
        UPLOADED("UPLOADED"),
        CONVERTED("CONVERTED"),
        VALIDATED("VALIDATED"),
        INDEXED("INDEXED"),
        FINISHED("FINISHED");

        private String name;
        Status(String name){
            this.name = name;
        }
    }
}
