package eu.apenet.dashboard.listener;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import eu.archivesportaleurope.harvester.oaipmh.HarvestObject;

/**
 * User: Yoann Moranville
 * Date: 23/10/2013
 *
 * @author Yoann Moranville
 */
public class HarvesterDaemon {
    private static final Logger LOGGER = Logger.getLogger(HarvesterDaemon.class);
    private static ScheduledExecutorService scheduler;
    private static boolean harvesterProcessing = false;
    private static boolean processOnceADay = true;
    private static HarvestObject harvestObject = null;

    public static synchronized void start(boolean processOnceADay) {
    	HarvesterDaemon.processOnceADay = processOnceADay;
        if (scheduler == null && !harvesterProcessing) {
            scheduler = Executors.newScheduledThreadPool(1);

            if(HarvesterDaemon.processOnceADay) {
                Calendar calendar = GregorianCalendar.getInstance();
                calendar.setTime(new Date());
                int startTonight = 23 - calendar.get(Calendar.HOUR_OF_DAY);
                if(startTonight < 0)
                    startTonight = 0;
                LOGGER.info("Daily harvesting started. Next harvester task will be started in " + startTonight + " hours.");
                addTask(new Duration(startTonight, 1, 0), new Duration(5, 0, 0), new Duration(24, 0, 0));
            } else {
            	LOGGER.info("Ten minutes harvesting started.");
                addTask(new Duration(0, 0, 0), new Duration(0, 10, 0), new Duration(0, 5, 0));
            }

            LOGGER.info("Harvester daemon started");
        }else if (harvesterProcessing){
            LOGGER.info("Could not start Harvester daemon, because the queue is still processing");
        }
    }

    public static void addTask(Duration startTime, Duration maxDuration, Duration delay){
        if (scheduler != null){
            LOGGER.info("Add harvester task");
            HarvesterTask harvesterTask = new HarvesterTask(scheduler, maxDuration, delay);
            scheduler.schedule(harvesterTask, startTime.getMilliseconds(), TimeUnit.MILLISECONDS);
        }
    }

    public static synchronized void stop(boolean killCurrentProcess) {
    	if (killCurrentProcess){
    		HarvestObject harvestObject = getHarvestObject();
    		if (harvestObject != null)
    			getHarvestObject().setStopHarvesting(true);
    	}
        if (HarvesterDaemon.isActive() && scheduler != null){
            scheduler.shutdownNow();
            scheduler = null;
            LOGGER.info("Harvester daemon stopped");
        }
    }

    public static boolean isActive() {
        return scheduler != null;
    }

    public static boolean isHarvesterProcessing() {
        return harvesterProcessing;
    }

    public static void setHarvesterProcessing(boolean harvesterProcessing) {
        HarvesterDaemon.harvesterProcessing = harvesterProcessing;
    }

	public static boolean isProcessOnceADay() {
		return processOnceADay;
	}

	public static HarvestObject getHarvestObject() {
			return harvestObject;
	}

	public static void setHarvestObject(HarvestObject harvestObject) {
		HarvesterDaemon.harvestObject = harvestObject;
	}
    
}
