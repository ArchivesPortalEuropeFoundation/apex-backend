package eu.apenet.dashboard.listener;

import eu.apenet.commons.exceptions.APEnetException;
import eu.apenet.dashboard.services.ead.EadService;
import eu.apenet.persistence.dao.ArchivalInstitutionOaiPmhDAO;
import eu.apenet.persistence.dao.EadDAO;
import eu.apenet.persistence.dao.QueueItemDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.*;
import eu.archivesportaleurope.persistence.jpa.JpaUtil;
import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrServerException;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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
    private static final long INTERVAL = 10000;
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
            if (!stopped && (System.currentTimeMillis() + INTERVAL) < endTime) {
                cleanUp();
                try {
                    Thread.sleep(INTERVAL);
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
            LOGGER.info("Checking if OAI profile is ready to be harvested: " + archivalInstitutionOaiPmh.getUrl() + " (set: " + archivalInstitutionOaiPmh.getSet() + ", metadataPrefix: " + archivalInstitutionOaiPmh.getMetadataPrefix() + ")");
            if(archivalInstitutionOaiPmh.getLastHarvesting() == null || (archivalInstitutionOaiPmh.getLastHarvesting().getTime() + archivalInstitutionOaiPmh.getIntervalHarvesting() >= System.currentTimeMillis())) { //Ok, do harvest
                LOGGER.info("This profile will be harvested now");
            }
        }

        return false;
    }
}
