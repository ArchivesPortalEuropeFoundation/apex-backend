package eu.apenet.dashboard.listener;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import eu.apenet.persistence.dao.UpFileDAO;
import eu.apenet.persistence.vo.*;
import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrServerException;

import eu.apenet.commons.exceptions.APEnetException;
import eu.apenet.dashboard.services.ead.EadService;
import eu.apenet.persistence.dao.EadDAO;
import eu.apenet.persistence.dao.QueueItemDAO;
import eu.apenet.persistence.dao.ResumptionTokenDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.archivesportaleurope.persistence.jpa.JpaUtil;

public class QueueTask implements Runnable {
	private static final Logger LOGGER = Logger.getLogger(QueueDaemon.class);
	private Duration duration;
	private Duration delay;
	private static final long INTERVAL = 30000;
	private final ScheduledExecutorService scheduler;

	public QueueTask(ScheduledExecutorService scheduler, Duration maxDuration, Duration delay) {
		this.duration = maxDuration;
		this.delay = delay;
		this.scheduler = scheduler;
		JpaUtil.init();
	}

	@Override
	public void run() {
		LOGGER.info("Queuing process active");
		removeOldResumptionTokens();
		long endTime = System.currentTimeMillis() + duration.getMilliseconds();
		boolean stopped = false;
		while (!stopped && !scheduler.isShutdown() && System.currentTimeMillis() < endTime) {
			if (EadService.isHarvestingStarted()) {
				LOGGER.info("Harvesting process is started, the queue process is stopped");
				cleanUp();
			} else {
				try {
					QueueDaemon.setQueueProcessing(true);
					processQueue(endTime);
				} catch (Throwable e) {
					LOGGER.error("Stopping processing for a while :" + e.getMessage(), e);
					try {
						JpaUtil.rollbackDatabaseTransaction();
					} catch (Exception de) {
						LOGGER.error(de.getMessage());
					}
					stopped = true;
				}
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
		LOGGER.info("Queuing process inactive");
		if (!scheduler.isShutdown()) {
			scheduler.schedule(new QueueTask(scheduler, duration, delay), delay.getMilliseconds(),
					TimeUnit.MILLISECONDS);
		}
	}

	private static void cleanUp() {
		try {
			QueueDaemon.setQueueProcessing(false);
			JpaUtil.closeDatabaseSession();
		}

		catch (Exception de) {
			LOGGER.error(de.getMessage());
		}
	}

	public boolean processQueue(long endTime) throws Exception {
		boolean itemsPublished = false;
		QueueItemDAO queueItemDAO = DAOFactory.instance().getQueueItemDAO();
		EadDAO eadDAO = DAOFactory.instance().getEadDAO();
        UpFileDAO upFileDAO = DAOFactory.instance().getUpFileDAO();
		boolean hasItems = true;
		while (hasItems && !scheduler.isShutdown() && System.currentTimeMillis() < endTime) {
			int queueId = -1;
			try {
				/*
				 * lock ead and queue item before going to process.
				 */
				JpaUtil.beginDatabaseTransaction();
				QueueItem queueItem = queueItemDAO.getFirstItem();

				if (queueItem == null) {
					JpaUtil.rollbackDatabaseTransaction();
					hasItems = false;
				} else {
					queueId = queueItem.getId();
					Ead ead = queueItem.getEad();
                    if(ead != null) {
                        ead.setQueuing(QueuingState.BUSY);
                        eadDAO.updateSimple(ead);
                        JpaUtil.commitDatabaseTransaction();
                    }
					QueueAction queueAction = EadService.processQueueItem(queueItem);
					itemsPublished = itemsPublished || queueAction.isPublishAction();
					hasItems = true;
				}
			} catch (Throwable e) {
				LOGGER.error("queueId: " + queueId + " - " + e.getMessage(), e);
				JpaUtil.rollbackDatabaseTransaction();
				/*
				 * it is unexcepted that this error occurred. put priority on 0,
				 * to the queue could go futher.
				 */
				JpaUtil.beginDatabaseTransaction();
				if (queueId > -1) {
					QueueItem queueItem = queueItemDAO.findById(queueId);
					if (queueItem.getPriority() > 0) {
						queueItem.setPriority(0);
					}
					queueItem.setErrors(new Date() + ". Error: " + e.getMessage() + "-" + e.getCause());
					queueItemDAO.updateSimple(queueItem);
				}
				JpaUtil.commitDatabaseTransaction();
				/*
				 * throw exception when solr has problem, so the queue will stop for a while.
				 */
				if (e instanceof APEnetException && e.getCause() instanceof SolrServerException){
					throw (Exception) e;
					
				}
			}

		}
		return itemsPublished;

	}

	private static void removeOldResumptionTokens() {
		ResumptionTokenDAO resumptionTokenDao = DAOFactory.instance().getResumptionTokenDAO();
		List<ResumptionToken> resumptionTokenList = resumptionTokenDao.getOldResumptionTokensThan(new Date());
		Iterator<ResumptionToken> iterator = resumptionTokenList.iterator();
		while (iterator.hasNext()) {
			resumptionTokenDao.delete(iterator.next());
		}
	}
}
