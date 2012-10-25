package eu.apenet.dashboard.listener;

import java.util.Date;
import java.util.Timer;

import org.apache.log4j.Logger;

public class QueueDaemon {
	private static final Logger LOGGER = Logger.getLogger(QueueDaemon.class);
	private static Timer timer ;
	public static synchronized void  init (){
		if (timer == null){
			timer = new Timer(true);
			LOGGER.info("Queue daemon started");
		}
		
	}
	public static void addTask(Date startDate, Duration maxDuration, Duration delay){
		if (timer != null){
			LOGGER.info("Add queue task");
			QueueTask queueTask = new QueueTask(maxDuration.getMilliseconds());
			timer.scheduleAtFixedRate(queueTask, startDate, delay.getMilliseconds());
		}
	}
	public static void cancel(){
		if (timer != null){
			timer.cancel();
			timer = null;
			LOGGER.info("Queue daemon stopped");
		}
	}
}
