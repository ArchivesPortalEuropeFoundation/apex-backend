package eu.apenet.dashboard.listener;

import java.net.SocketException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrServerException;

import eu.apenet.commons.exceptions.APEnetException;
import eu.apenet.commons.types.XmlType;
import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.dashboard.security.UserService;
import eu.apenet.dashboard.services.eaccpf.EacCpfService;
import eu.apenet.dashboard.services.ead.EadService;
import eu.apenet.dashboard.services.ead3.Ead3Service;
import eu.apenet.dashboard.services.opendata.OpenDataService;
import eu.apenet.persistence.dao.QueueItemDAO;
import eu.apenet.persistence.dao.ResumptionTokenDAO;
import eu.apenet.persistence.exception.PersistenceException;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.AbstractContent;
import eu.apenet.persistence.vo.EacCpf;
import eu.apenet.persistence.vo.Ead;
import eu.apenet.persistence.vo.Ead3;
import eu.apenet.persistence.vo.QueueAction;
import eu.apenet.persistence.vo.QueueItem;
import eu.apenet.persistence.vo.QueuingState;
import eu.apenet.persistence.vo.ResumptionToken;
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
        if (EadService.isHarvestingStarted()) {
            stopped = true;
            LOGGER.info("Queuing process active, but will be stopped immediately, because of harvesting");
        } else {
            LOGGER.debug("Queuing process active");
        }
        removeOldResumptionTokens();
        EadService.fixWrongQueueStates();
        EacCpfService.fixWrongQueueStates();
        long endTime = System.currentTimeMillis() + duration.getMilliseconds();

        int numberOfTries = 0;
        while (!stopped && !scheduler.isShutdown() && System.currentTimeMillis() < endTime) {
            if (EadService.isHarvestingStarted()) {
                if (numberOfTries >= 2) {
                    stopped = true;
                }
                if (numberOfTries == 0 && EadService.isHarvestingStarted()) {
                    LOGGER.info("Europeana harvesting process is started, the queue process is stopped");
                }
                cleanUp();
                numberOfTries++;
            } else {
                boolean exception = false;
                try {
                    QueueDaemon.setQueueProcessing(true);
                    processQueue(endTime);
                } catch (PersistenceException e) {
                    LOGGER.fatal("Database exception, the queue processing will be stopped. " + e.getMessage());
                    exception = true;
                    UserService.sendExceptionToAdmin("Queue processing is stopped and dashboard is in maintenance mode, due to database exception", e);
                } catch (Throwable e) {
                    exception = true;
                    UserService.sendExceptionToAdmin("Queue processing is stopped and dashboard is in maintenance mode, due to solr search engine exception", e);
                    try {
                        JpaUtil.rollbackDatabaseTransaction();
                    } catch (Exception de) {
                        LOGGER.error(de.getMessage());
                    }
                }
                if (exception) {
                    stopped = true;
                    APEnetUtilities.getDashboardConfig().setMaintenanceMode(true);
                    cleanUp();
                    QueueDaemon.stop();
                }
            }

            if (!stopped && !scheduler.isShutdown() && (System.currentTimeMillis() + INTERVAL) < endTime) {
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
        } else {
            LOGGER.info("Queue is going to terminate");
        }
    }

    private static void cleanUp() {
        try {
            QueueDaemon.setQueueProcessing(false);
            JpaUtil.closeDatabaseSession();
        } catch (Exception de) {
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
                QueueItem queueItem = queueItemDAO.getFirstItemWithAI();

                if (queueItem == null) {
                    JpaUtil.rollbackDatabaseTransaction();
                    hasItems = false;
                } else {
                    queueId = queueItem.getId();
                    AbstractContent content = queueItem.getAbstractContent();
                    if (content != null) {
                        content.setQueuing(QueuingState.BUSY);
                        if (content instanceof Ead) {
                            DAOFactory.instance().getEadDAO().updateSimple((Ead) content);
                        } else if (content instanceof EacCpf) {
                            DAOFactory.instance().getEacCpfDAO().updateSimple((EacCpf) content);
                        } else if (content instanceof Ead3) {
                            DAOFactory.instance().getEad3DAO().updateSimple((Ead3) content);
                        }
                        JpaUtil.commitDatabaseTransaction();
                        QueueAction queueAction;
                        if (content instanceof Ead) {
                            queueAction = EadService.processQueueItem(queueItem);
                            itemsPublished = itemsPublished || queueAction.isPublishAction();
                            hasItems = true;
                        } else if (content instanceof EacCpf) {
                            queueAction = EacCpfService.processQueueItem(queueItem);
                            itemsPublished = itemsPublished || queueAction.isPublishAction();
                            hasItems = true;
                        } else if (content instanceof Ead3) {
                            queueAction = Ead3Service.processQueueItem(queueItem);
                            itemsPublished = itemsPublished || queueAction.isPublishAction();
                            hasItems = true;
                        }
                    } else if (queueItem.getUpFileId() != null && !queueItem.getPreferences().isEmpty()) {
                        Properties preferences = EadService.readProperties(queueItem.getPreferences());
                        if (preferences.containsKey(QueueItem.XML_TYPE)) {
                            String xmlType = preferences.getProperty(QueueItem.XML_TYPE);
                            if (Integer.parseInt(xmlType) == XmlType.EAC_CPF.getIdentifier()) {
                                EacCpfService.processQueueItem(queueItem);
                            } else if (Integer.parseInt(xmlType) == XmlType.EAD_3.getIdentifier()) {
                                Ead3Service.processQueueItem(queueItem);
                            } else {
                                EadService.processQueueItem(queueItem);
                            }
                        }
                    } else if (!queueItem.getPreferences().isEmpty()) {
                        Properties preferences = EadService.readProperties(queueItem.getPreferences());
                        boolean hasKey = preferences.containsKey(OpenDataService.ENABLE_OPEN_DATA_KEY);
                        if (hasKey) {
                            OpenDataService.getInstance().processQueueItem(queueItem);
                        }
                    }
                }
            } catch (PersistenceException e) {
                LOGGER.error("queueId: " + queueId + " - " + APEnetUtilities.generateThrowableLog(e));
                throw e;
            } catch (Throwable e) {
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
                if (e instanceof APEnetException && e.getCause() instanceof SolrServerException) {
                    SolrServerException cause = (SolrServerException) e.getCause();
                    if (cause.getCause() instanceof SocketException) {
                        throw (Exception) e;
                    }

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
