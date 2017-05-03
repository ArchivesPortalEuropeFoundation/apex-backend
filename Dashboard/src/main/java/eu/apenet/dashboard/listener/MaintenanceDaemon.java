package eu.apenet.dashboard.listener;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

public class MaintenanceDaemon {

    private static final Logger LOGGER = Logger.getLogger(
            MaintenanceDaemon.class);
    private static final int MAINTENANCE_DAY = 1;
    private final static int MINUTE_IN_SECONDS = 60;

    private final static int HOUR_IN_SECONDS = 60 * MINUTE_IN_SECONDS;

    private final static int DAY_IN_SECONDS = 24 * HOUR_IN_SECONDS;
    private static ScheduledExecutorService scheduler;

    public static synchronized void start() {
        if (scheduler == null) {
            ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
                                       .setNameFormat("maintenance-thread-%d").build();
            scheduler = Executors.newScheduledThreadPool(1, namedThreadFactory);
            LOGGER.info("Maintenance daemon started");
            LOGGER.info("-----------------------------");
            int delaySeconds = calculateSeconds();
            LOGGER.info(
                    "Weekly maintenance started. Next maintenance task after "
                    + convertNumberToDuration(delaySeconds));
            scheduler.schedule(new SolrMaintenanceTask(scheduler), delaySeconds,
                    TimeUnit.SECONDS);

            LOGGER.info("-----------------------------");
        }
    }

    public static synchronized void stop() {
        if (MaintenanceDaemon.isActive()) {
            scheduler.shutdownNow();
            scheduler = null;
            LOGGER.info("Maintenance daemon stopped");
        }
    }

    public static boolean isActive() {
        return scheduler != null;
    }

    public static int calculateSeconds() {
        Calendar currentDate = GregorianCalendar.getInstance();
        Calendar maintenanceDate = GregorianCalendar.getInstance();
        int currentDay = maintenanceDate.get(GregorianCalendar.DAY_OF_WEEK);

        maintenanceDate.set(GregorianCalendar.DAY_OF_WEEK, MAINTENANCE_DAY);
        maintenanceDate.set(GregorianCalendar.HOUR_OF_DAY, 2);
        maintenanceDate.set(GregorianCalendar.MINUTE, 0);
        maintenanceDate.set(GregorianCalendar.SECOND, 0);
        if (MAINTENANCE_DAY == currentDay || maintenanceDate.before(currentDate)) {
            maintenanceDate.set(GregorianCalendar.WEEK_OF_YEAR, maintenanceDate.
                    get(GregorianCalendar.WEEK_OF_YEAR) + 1);
        }
        LOGGER.info(maintenanceDate.getTime());
        long timeToWait = maintenanceDate.getTimeInMillis() - currentDate.
                getTimeInMillis();
        return (int) (timeToWait / 1000);
    }

    public static String convertNumberToDuration(int seconds) {

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
            } else {
                result += "00H ";
            }
            int minutes = seconds / MINUTE_IN_SECONDS;
            if (minutes > 0) {
                result += minutes + "M ";
                seconds = seconds % MINUTE_IN_SECONDS;
            } else {
                result += "00M ";
            }
            if (seconds > 0) {
                result += seconds + "S";
            } else {
                result += "00S";
            }
        }

        return result;

    }
}
