package eu.apenet.dashboard.listener;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

public class QueueDaemon {
	private static final Logger LOGGER = Logger.getLogger(QueueDaemon.class);
	private static ScheduledExecutorService scheduler;
	private static boolean queueProcessing = false;
	public static synchronized void start(){
		if (scheduler == null && !queueProcessing){
			scheduler = Executors.newScheduledThreadPool(1);
			addTask(new Duration(0, 0, 0),new Duration(0, 10, 0) , new Duration(0, 2, 0));
			LOGGER.info("Queue daemon started");
		}
		
	}
	public static void addTask(Duration startDelay, Duration maxDuration, Duration delay){
		if (scheduler != null){
			LOGGER.info("Add queue task");
			QueueTask queueTask = new QueueTask(scheduler, maxDuration, delay);
			scheduler.schedule(queueTask, startDelay.getMilliseconds(), TimeUnit.MILLISECONDS);
		}
	}
	public static synchronized void stop(){
		if (scheduler != null){
			scheduler.shutdownNow();
			scheduler = null;
			LOGGER.info("Queue daemon stopped");
		}
	}
	public static boolean isActive(){
		if (scheduler != null){
			return true;
		}else {
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
