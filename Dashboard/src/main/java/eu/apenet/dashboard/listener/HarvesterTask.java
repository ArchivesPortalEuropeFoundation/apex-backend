package eu.apenet.dashboard.listener;

import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import eu.apenet.dashboard.harvest.DataHarvester;
import eu.apenet.persistence.dao.ArchivalInstitutionOaiPmhDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.ArchivalInstitutionOaiPmh;
import eu.archivesportaleurope.persistence.jpa.JpaUtil;

/**
 * User: Yoann Moranville
 * Date: 23/10/2013
 *
 * @author Yoann Moranville
 */
public class HarvesterTask implements Runnable {
    private static final Logger LOGGER = Logger.getLogger(HarvesterTask.class);
    private Duration duration;
    private Duration delay;
    private static final Duration INTERVAL = new Duration(0, 10, 0);
    private final ScheduledExecutorService scheduler;

    public HarvesterTask(ScheduledExecutorService scheduler, Duration maxDuration, Duration delay) {
        this.duration = maxDuration;
        this.scheduler = scheduler;
        this.delay = delay;
        JpaUtil.init();
    }

    @Override
    public void run() {
        LOGGER.info("Harvester process active");
        long endTime = System.currentTimeMillis() + duration.getMilliseconds();
        boolean stopped = false;
        while (!stopped && !scheduler.isShutdown() && System.currentTimeMillis() < endTime) {
            try {
                HarvesterDaemon.setHarvesterProcessing(true);
                processHarvester(endTime);
            } catch (Throwable e) {
                LOGGER.error("Stopping processing for a while: " + e.getMessage(), e);
                try {
                    JpaUtil.rollbackDatabaseTransaction();
                } catch (Exception de) {
                    LOGGER.error(de.getMessage());
                }
                stopped = true;
            }
            if (!stopped && (System.currentTimeMillis() + INTERVAL.getMilliseconds()) < endTime) {
                cleanUp();
                try {
                    Thread.sleep(INTERVAL.getMilliseconds());
                } catch (InterruptedException e) {
                }
            } else {
                cleanUp();
                stopped = true;
            }
        }
        LOGGER.info("Harvester process inactive");
        if (!scheduler.isShutdown()) {
            scheduler.schedule(new HarvesterTask(scheduler, duration, delay), delay.getMilliseconds(), TimeUnit.MILLISECONDS);
        }
    }

    private static void cleanUp() {
        try {
            HarvesterDaemon.setHarvesterProcessing(false);
            JpaUtil.closeDatabaseSession();
        } catch (Exception de) {
            LOGGER.error(de.getMessage());
        }
    }

    public boolean processHarvester(long endTime) throws Exception {
        ArchivalInstitutionOaiPmhDAO archivalInstitutionOaiPmhDAO = DAOFactory.instance().getArchivalInstitutionOaiPmhDAO();
        List<ArchivalInstitutionOaiPmh> archivalInstitutionOaiPmhList = archivalInstitutionOaiPmhDAO.findAll();

        //Check for each if the interval is right and which one should be done right now
        for(ArchivalInstitutionOaiPmh archivalInstitutionOaiPmh : archivalInstitutionOaiPmhList) {
            if(scheduler.isShutdown() || System.currentTimeMillis() > endTime) {
                break;
            }
            if(archivalInstitutionOaiPmh.isEnabled()) {
                if(archivalInstitutionOaiPmh.getLastHarvesting() == null || (archivalInstitutionOaiPmh.getLastHarvesting().getTime() + archivalInstitutionOaiPmh.getIntervalHarvesting() >= System.currentTimeMillis())) { //Ok, do harvest
                    boolean continueTask = true;
                    if(archivalInstitutionOaiPmh.isHarvestOnlyWeekend()) {
                        continueTask = false;
                        Calendar calendar = Calendar.getInstance();
                        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
                        if(dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY) {
                            continueTask = true;
                        }
                    }

                    if(continueTask) {
                        new DataHarvester(archivalInstitutionOaiPmh.getId(), true).run();
                    }
                }
            }
        }

        return false;
    }
}
