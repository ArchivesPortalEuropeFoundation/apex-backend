package eu.apenet.dashboard.listener;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import eu.archivesportaleurope.harvester.oaipmh.HarvestObject;
import java.util.concurrent.ThreadFactory;

/**
 * User: Yoann Moranville
 * Date: 23/10/2013
 *
 * @author Yoann Moranville
 */
public class HarvesterDaemon {
    private static final Logger LOGGER = Logger.getLogger(HarvesterDaemon.class);
	private final static int MINUTE_IN_SECONDS = 60;

	private final static int HOUR_IN_SECONDS = 60 * MINUTE_IN_SECONDS;

	private final static int DAY_IN_SECONDS = 24 * HOUR_IN_SECONDS;
    private static ScheduledExecutorService scheduler;
    private static boolean harvesterProcessing = false;
    private static boolean processOnceADay = true;
    private static HarvestObject harvestObject = null;
    private static final Duration DAILY_HARVESTING_DURATION = new Duration(4, 0, 0);
    private static final Duration TEN_MINUTES_HARVESTING_DURATION = new Duration(0, 10, 0);
    private static final Duration TEN_MINUTES_HARVESTING_DELAY = new Duration(0, 5, 0);
    public static synchronized void start(boolean processOnceADay) {
    	HarvesterDaemon.processOnceADay = processOnceADay;
        if (scheduler == null && !harvesterProcessing) {
            ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
                                       .setNameFormat("hervester-thread-%d").build();
            scheduler = Executors.newScheduledThreadPool(1, namedThreadFactory);
            LOGGER.info("Harvester daemon started");
            LOGGER.info("-----------------------------");
            if(HarvesterDaemon.processOnceADay) {
                Calendar currentDate = GregorianCalendar.getInstance();
        	    int currentTimeInSeconds = convertToSeconds(currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE), currentDate.get(Calendar.SECOND));
        	    int startTime = convertToSeconds(22,0,0);
        	    int intDelayInSeconds = calculateDelaySeconds(currentTimeInSeconds, startTime);
        	    LOGGER.info("Daily harvesting started. Next harvester task will be started at " + convertNumberToDuration(startTime) + ", so task have to wait " + convertNumberToDuration(intDelayInSeconds));
                scheduler.scheduleAtFixedRate(new HarvesterTask(scheduler,DAILY_HARVESTING_DURATION), intDelayInSeconds, DAY_IN_SECONDS, TimeUnit.SECONDS);
            } else {
            	LOGGER.info("Ten minutes harvesting started.");
                HarvesterTask harvesterTask = new HarvesterTask(scheduler, TEN_MINUTES_HARVESTING_DURATION, TEN_MINUTES_HARVESTING_DELAY);
                scheduler.schedule(harvesterTask, 0, TimeUnit.SECONDS);
            }

            LOGGER.info("-----------------------------");
        }else if (harvesterProcessing){
            LOGGER.info("Could not start Harvester daemon, because the queue is still processing");
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
	private static int convertToSeconds (int hours, int minutes, int seconds){
		return seconds + minutes*60 + hours*60*60;
	}
	private static int calculateDelaySeconds(int currentTime, int startTime){
	    int intDelayInSeconds = 0;
	    if (currentTime < startTime){
	    	intDelayInSeconds = startTime- currentTime;
	    }else {
	    	intDelayInSeconds = DAY_IN_SECONDS - currentTime + startTime;
	    }	
	    return intDelayInSeconds;
	}
	private static String convertNumberToDuration(int seconds) {

			String result = "";
			// check if days
			if (seconds > 0) {
				int days = seconds / DAY_IN_SECONDS;
				if (days > 0) {
					result += days + "D ";
					seconds = seconds % DAY_IN_SECONDS;
				}
				int hours = seconds / HOUR_IN_SECONDS;
				if (hours > 0) {
					result += hours + "H ";
					seconds = seconds % HOUR_IN_SECONDS;
				}else {
					result +=  "00H ";
				}
				int minutes = seconds / MINUTE_IN_SECONDS;
				if (minutes > 0) {
					result += minutes + "M ";
					seconds = seconds % MINUTE_IN_SECONDS;
				}else {
					result += "00M ";
				}
				if (seconds > 0) {
					result += seconds + "S";
				}else {
					result += "00S";
				}
			}

			return result;


	}
}
