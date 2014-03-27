package eu.apenet.dashboard.listener;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.persistence.PersistenceException;

import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrServerException;

import eu.apenet.commons.exceptions.APEnetException;
import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.dashboard.services.eaccpf.EacCpfService;
import eu.apenet.dashboard.services.ead.EadService;
import eu.apenet.persistence.dao.QueueItemDAO;
import eu.apenet.persistence.dao.ResumptionTokenDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.AbstractContent;
import eu.apenet.persistence.vo.EacCpf;
import eu.apenet.persistence.vo.Ead;
import eu.apenet.persistence.vo.FindingAid;
import eu.apenet.persistence.vo.HoldingsGuide;
import eu.apenet.persistence.vo.QueueAction;
import eu.apenet.persistence.vo.QueueItem;
import eu.apenet.persistence.vo.QueuingState;
import eu.apenet.persistence.vo.ResumptionToken;
import eu.apenet.persistence.vo.SourceGuide;
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
		boolean stopped = false;
		if (HarvesterDaemon.isHarvesterProcessing() || EadService.isHarvestingStarted()){
			stopped = true;
			LOGGER.info("Queuing process active, but will be stopped immediately, because of harvesting");
		}else {
			LOGGER.debug("Queuing process active");
		}
		removeOldResumptionTokens();
		long endTime = System.currentTimeMillis() + duration.getMilliseconds();

		int numberOfTries = 0; 
		while (!stopped && !scheduler.isShutdown() && System.currentTimeMillis() < endTime) {
			if (EadService.isHarvestingStarted() || HarvesterDaemon.isHarvesterProcessing()) {
				if (numberOfTries >= 2){
					stopped = true;
				}
				if (numberOfTries == 0 && HarvesterDaemon.isHarvesterProcessing()){
					LOGGER.info("Harvesting process is started, the queue process is stopped");
				}
				if (numberOfTries == 0 && EadService.isHarvestingStarted()){
					LOGGER.info("Europeana harvesting process is started, the queue process is stopped");
				}
				cleanUp();
				numberOfTries++;
			} else {
				try {
					QueueDaemon.setQueueProcessing(true);
					processQueue(endTime);
				}catch (PersistenceException e) {
					LOGGER.fatal("Database exception, the queue processing will be stopped.");
					QueueDaemon.stop();
				}catch (Throwable e) {
					LOGGER.error("Stopping processing for a while :" + APEnetUtilities.generateThrowableLog(e));
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
		LOGGER.debug("Queuing process inactive");
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
					AbstractContent content = queueItem.getAbstractContent();
                    if(content != null) {
                        content.setQueuing(QueuingState.BUSY);
                        if(content instanceof Ead){
                        	if(content instanceof FindingAid){
                        		DAOFactory.instance().getFindingAidDAO().updateSimple((FindingAid)content);
                        	}else if(content instanceof HoldingsGuide){
                        		DAOFactory.instance().getHoldingsGuideDAO().updateSimple((HoldingsGuide)content);
                        	}else if(content instanceof SourceGuide){
                        		DAOFactory.instance().getEadDAO().updateSimple((SourceGuide)content);
                        	}
                        }else if(content instanceof EacCpf){
                        	DAOFactory.instance().getEacCpfDAO().updateSimple((EacCpf)content);
                        }
                        JpaUtil.commitDatabaseTransaction();
                    }
                    
                    QueueAction queueAction;
                    if (content instanceof Ead){
					   queueAction = EadService.processQueueItem(queueItem);
					   itemsPublished = itemsPublished || queueAction.isPublishAction();
					   hasItems = true;
                    }else if (content instanceof EacCpf){
                    	queueAction = EacCpfService.processQueueItem(queueItem);
                    	itemsPublished = itemsPublished || queueAction.isPublishAction();
    					hasItems = true;
                    }
				}
			} catch (PersistenceException e) {
				LOGGER.error("queueId: " + queueId + " - " + APEnetUtilities.generateThrowableLog(e));
				throw e;
			}catch (Throwable e) {
				LOGGER.error("queueId: " + queueId + " - " + APEnetUtilities.generateThrowableLog(e));
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
					queueItem.setErrors(new Date() + ". Error: " + APEnetUtilities.generateThrowableLog(e));
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
