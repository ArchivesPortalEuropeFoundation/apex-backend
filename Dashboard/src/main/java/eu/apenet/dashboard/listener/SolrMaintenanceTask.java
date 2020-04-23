package eu.apenet.dashboard.listener;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import eu.apenet.commons.solr.SolrUtil;
import eu.apenet.commons.utils.APEnetUtilities;

public class SolrMaintenanceTask implements Runnable {

    private static final Logger LOGGER = Logger.getLogger(SolrMaintenanceTask.class);
    private static final int WAIT_FOR_START = 30 * 60;
    private final ScheduledExecutorService scheduler;

    public SolrMaintenanceTask(ScheduledExecutorService scheduler) {
        this.scheduler = scheduler;
    }

    @Override
    public void run() {
        LOGGER.info("Maintenance process started");
        try {
            executeMaintenanceTasks();
        } catch (Throwable e) {
            LOGGER.error("Stopping processing for a while: " + APEnetUtilities.generateThrowableLog(e));
        }

        LOGGER.info("Maintenance process stopped");
        if (!scheduler.isShutdown()) {
            int delaySeconds = MaintenanceDaemon.calculateSeconds();
            LOGGER.info("Next maintenance task after " + MaintenanceDaemon.convertNumberToDuration(delaySeconds));
            scheduler.schedule(new SolrMaintenanceTask(scheduler), delaySeconds,
                    TimeUnit.SECONDS);
        }
    }

    public void executeMaintenanceTasks() throws Exception {
        boolean maintenanceActive = APEnetUtilities.getDashboardConfig().isMaintenanceMode();
        boolean queueActive = APEnetUtilities.getDashboardConfig().isDefaultQueueProcessing();
        APEnetUtilities.getDashboardConfig().setMaintenanceMode(true);
        QueueDaemon.stop();
        try {
            LOGGER.info("Maintenance task will start after: " + MaintenanceDaemon.convertNumberToDuration(WAIT_FOR_START));
            Thread.sleep(WAIT_FOR_START * 1000);
        } catch (InterruptedException e) {
        }
        SolrUtil.forceSolrCommit();
        SolrUtil.solrOptimize();
        SolrUtil.rebuildAutosuggestion();
        // restart queue only if it was exclusively stopped for the maintenance task
        if (!queueActive) {
            QueueDaemon.start();
        }
        // lift maintenance mode only if it had not been in place before
        if (!maintenanceActive) {
            APEnetUtilities.getDashboardConfig().setMaintenanceMode(false);
        }
    }
}
