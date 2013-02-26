package eu.apenet.dashboard.listener;

import java.io.IOException;
import java.util.Date;
import java.util.TimerTask;

import org.apache.log4j.Logger;

import eu.apenet.dashboard.services.ead.EadService;
import eu.apenet.persistence.dao.EadDAO;
import eu.apenet.persistence.dao.QueueItemDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.Ead;
import eu.apenet.persistence.vo.QueueItem;
import eu.apenet.persistence.vo.QueuingState;
import eu.archivesportaleurope.persistence.jpa.JpaUtil;

public class QueueTask extends TimerTask {
	private static final Logger LOGGER = Logger.getLogger(QueueDaemon.class);
	private long duration;
	private boolean active = true;
	private long INTERVAL = 30000;

	public QueueTask(long duration) {
		this.duration = duration;
		JpaUtil.init();
	}

	@Override
	public void run() {
		LOGGER.info("Queuing process active");
		long endTime = System.currentTimeMillis() + duration;
		boolean stopped = false;
		while (!stopped && active && System.currentTimeMillis() < endTime) {
			try {
				processQueue(endTime);
			} catch (Exception e) {
				LOGGER.error(e.getMessage(), e);
				try {
					JpaUtil.rollbackDatabaseTransaction();
				} catch (Exception de) {
					LOGGER.error(de.getMessage());
				}
			}
			if ((System.currentTimeMillis() + INTERVAL) < endTime) {
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
	}

	@Override
	public boolean cancel() {
		active = true;
		return super.cancel();
	}

	private static void cleanUp() {
		try {
			JpaUtil.closeDatabaseSession();
		}

		catch (Exception de) {
			LOGGER.error(de.getMessage());
		}
	}

	public void processQueue(long endTime) {
		QueueItemDAO queueItemDAO = DAOFactory.instance().getQueueItemDAO();
		EadDAO eadDAO = DAOFactory.instance().getEadDAO();
		boolean hasItems = true;
		while (hasItems && active && System.currentTimeMillis() < endTime) {
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
					ead.setQueuing(QueuingState.BUSY);
					eadDAO.updateSimple(ead);
					JpaUtil.commitDatabaseTransaction();
					EadService.processQueueItem(queueItem);
					hasItems = true;
				}
			}catch (Exception e) {
				LOGGER.error("queueId: " + queueId + " - " +e.getMessage(), e);
				JpaUtil.rollbackDatabaseTransaction();
				/*
				 * it is unexcepted that this error occurred. put priority on 0, to the queue could go futher.
				 */
				JpaUtil.beginDatabaseTransaction();
				if (queueId > -1){
					QueueItem queueItem = queueItemDAO.findById(queueId);
					if (queueItem.getPriority() > 0){
						queueItem.setPriority(0);
					}
					queueItem.setErrors(new Date()+ ". Error: "
							+ e.getMessage() + "-" + e.getCause());
					queueItemDAO.updateSimple(queueItem);
				}
				JpaUtil.commitDatabaseTransaction();
			}

		}

	}

}
