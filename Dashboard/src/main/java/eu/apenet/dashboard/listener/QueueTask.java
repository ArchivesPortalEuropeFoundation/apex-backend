package eu.apenet.dashboard.listener;

import java.util.List;
import java.util.TimerTask;

import org.apache.log4j.Logger;

import eu.apenet.dashboard.services.ead.EadService;
import eu.apenet.persistence.dao.QueueItemDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.QueueItem;
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
		try {
			while (!stopped && active && System.currentTimeMillis() < endTime) {
				processQueue(endTime);
				// LOGGER.info("Processing queue stopped");
				if ((System.currentTimeMillis() + INTERVAL) < endTime) {
					// LOGGER.info("Start sleeping");
					Thread.sleep(INTERVAL);
				} else {
					stopped = true;
				}
			}

			// Commit any pending database transaction.
			try {
				JpaUtil.commitDatabaseTransaction();
			} catch (Exception de) {
				LOGGER.error(de.getMessage());
			}

		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			// If an exception occurs and there is an open transaction, roll
			// back the transaction.
			try {

				JpaUtil.rollbackDatabaseTransaction();
			}

			catch (Exception de) {
				LOGGER.error(de.getMessage());
			}

		} finally {

			// No matter what happens, close the Session.
			try {
				JpaUtil.closeDatabaseSession();
			}

			catch (Exception de) {
				LOGGER.error(de.getMessage());
			}

		}
		LOGGER.info("Queuing process inactive");
	}

	@Override
	public boolean cancel() {
		active = true;
		return super.cancel();
	}

	public void processQueue(long endTime) throws Exception {

		QueueItemDAO queueItemDAO = DAOFactory.instance().getQueueItemDAO();
		boolean filesLeft = true;
		while (active && filesLeft && System.currentTimeMillis() < endTime) {
			// LOGGER.info("Looking for files in queue");
			List<QueueItem> queueItems = queueItemDAO.getFirstItems();
			if (queueItems.size() == 0) {
				// LOGGER.info("No files in queue");
				filesLeft = false;
			} else {
				processQueueItem(queueItems, endTime);
			}
		}

	}

	public int processQueueItem(List<QueueItem> queueItems, long endTime) throws Exception {
		int indexed = 0;
		boolean expired = false;
		for (int i=0; !expired && i < queueItems.size();i++) {
			if (active && System.currentTimeMillis() < endTime) {
				boolean processed = EadService.processQueueItem(queueItems.get(i));
				if (processed)
					indexed++;
			} else {
				expired = true;
			}

		}
		return indexed;
	}
}
