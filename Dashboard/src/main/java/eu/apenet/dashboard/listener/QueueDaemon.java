package eu.apenet.dashboard.listener;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

public class QueueDaemon {

    private static final Logger LOGGER = Logger.getLogger(QueueDaemon.class);
    private static ScheduledExecutorService scheduler;
    private static boolean queueProcessing = false;

    public static synchronized void start() {
        clean();
        if (scheduler == null && !queueProcessing) {
            ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
                                       .setNameFormat("queue-thread-%d").build();
            scheduler = Executors.newScheduledThreadPool(1, namedThreadFactory);
            addTask(new Duration(0, 0, 0), new Duration(0, 10, 0), new Duration(
                    0, 2, 0));
            LOGGER.info("Queue daemon started");
        } else if (queueProcessing) {
            LOGGER.info(
                    "Could not start Queue daemon, because the queue is still processing");
        }
    }

    public static void addTask(Duration startDelay, Duration maxDuration,
                               Duration delay) {
        if (scheduler != null) {
            LOGGER.info("Add queue task");
            QueueTask queueTask = new QueueTask(scheduler, maxDuration, delay);
            scheduler.schedule(queueTask, startDelay.getMilliseconds(),
                    TimeUnit.MILLISECONDS);
        }
    }

    public static synchronized void stop() {
        clean();
        if (isActive()) {
            if (isQueueProcessing()) {
                LOGGER.info("Queue is in progress...");
                scheduler.shutdown();
            } else {
                scheduler.shutdownNow();
            }
            LOGGER.info("Queue is going to shutdown...");
        }
    }

    private static synchronized void clean() {
        if (scheduler != null && scheduler.isTerminated()) {
            scheduler = null;
        }
    }

    public static String getQueueStatus() {
        clean();
        String result = "";
        if (scheduler == null) {
            result = "Queue is not started";
        } else if (scheduler.isShutdown()) {
            result = "Queue is going to shutdown...";
        } else if (scheduler.isTerminated()) {
            result = "Queue is terminated";
        } else {
            result = "Queue is started";
        }
        return result;
    }

    public static String getQueueStatusCss() {
        String result = "";
        if (scheduler == null) {
            result = "queueStopped";
        } else if (scheduler.isShutdown()) {
            result = "queueStopping";
        } else if (scheduler.isTerminated()) {
            result = "queueStopping";
        } else {
            result = "queueStarted";
        }
        return result;
    }

    public static boolean isActive() {
        if (scheduler != null && !scheduler.isTerminated()) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isQueueProcessing() {
        return queueProcessing;
    }

    protected static void setQueueProcessing(boolean queueProcessing) {
        QueueDaemon.queueProcessing = queueProcessing;
    }

}
