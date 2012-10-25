package eu.apenet.dashboard.listener;

import java.util.Date;
import java.util.List;
import java.util.TimerTask;

import org.apache.log4j.Logger;

import eu.apenet.commons.types.XmlType;
import eu.apenet.commons.utils.IndexUtils;
import eu.apenet.dashboard.manual.contentmanager.ContentManager;
import eu.apenet.persistence.dao.IndexQueueDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.Ead;
import eu.apenet.persistence.vo.IndexQueue;
import eu.archivesportaleurope.persistence.jpa.JpaUtil;

public class QueueTask extends TimerTask {
	private static final Logger LOGGER = Logger.getLogger(QueueDaemon.class);
	private long duration;
	private boolean active = true;
	private long INTERVAL = 60000;

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
			while (!stopped && active && System.currentTimeMillis() < endTime){
				processQueue(endTime);
				//LOGGER.info("Processing queue stopped");
				if ((System.currentTimeMillis() + INTERVAL)< endTime){
					//LOGGER.info("Start sleeping");
					Thread.sleep(INTERVAL);
				}else {
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
		active =true;
		return super.cancel();
	}
	public void processQueue(long endTime) throws Exception {
		//int indexed = 0;
		//long startTime = System.currentTimeMillis();
		IndexUtils.setIndexing(true);
		try
		{
			IndexQueueDAO indexqueueDAO =  DAOFactory.instance().getIndexQueueDAO();	
			boolean filesLeft = true;
			while (active && filesLeft && System.currentTimeMillis() < endTime){
				//LOGGER.info("Looking for files in queue");
				List<IndexQueue> filesToIndex = indexqueueDAO.getFirstItems(20);
				if (filesToIndex.size() == 0){
					//LOGGER.info("No files in queue");
					filesLeft = false;
				}else {
					indexSub(filesToIndex, endTime);
				}
			}
		}finally {
			IndexUtils.setIndexing(false);
			
		}
	    
	}
	public int indexSub(List<IndexQueue> filesToIndex, long endTime) throws Exception {
		int indexed = 0;
		IndexQueueDAO indexqueueDAO =  DAOFactory.instance().getIndexQueueDAO();	
		for(IndexQueue indexQueue : filesToIndex) {
            Ead ead;
            if(indexQueue.getHoldingsGuide() != null) {
                ead = indexQueue.getHoldingsGuide();
            } else if(indexQueue.getFindingAid() != null) {
                ead = indexQueue.getFindingAid();
            } else {
                ead = indexQueue.getSourceGuide();
            }
            XmlType xmlType = XmlType.getEadType(ead);

            if (active && System.currentTimeMillis() < endTime) {
                try {
                    ContentManager.indexFromQueue(ead);
                    if (indexQueue != null)
                        indexqueueDAO.delete(indexQueue);
                } catch (Exception e){
                    String err = "eadid: " + ead.getEadid() + " - id: " + ead.getId() + " - type: " + xmlType.getName();
                    LOGGER.error("Error indexing: " + err, e);
                    if (indexQueue != null) {
                        indexQueue.setErrors(new Date() + err + ". Error: " + e.getMessage() +"-"+ e.getCause());
                        indexqueueDAO.update(indexQueue);
                    }
                }
                indexed++;
            } else {
                break;
            }
            
        }
		return indexed;
	}
}
