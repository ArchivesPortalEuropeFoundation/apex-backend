/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.apenet.dashboard.reindex;

import eu.apenet.commons.exceptions.ProcessBusyException;
import eu.apenet.commons.types.XmlType;
import eu.apenet.dashboard.utils.PropertiesUtil;
import eu.apenet.persistence.dao.ContentSearchOptions;
import eu.apenet.persistence.dao.Ead3DAO;
import eu.apenet.persistence.dao.EadDAO;
import eu.apenet.persistence.dao.GenericDAO;
import eu.apenet.persistence.dao.QueueItemDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.AbstractContent;
import eu.apenet.persistence.vo.Ead3;
import eu.apenet.persistence.vo.FindingAid;
import eu.apenet.persistence.vo.HoldingsGuide;
import eu.apenet.persistence.vo.QueueAction;
import eu.apenet.persistence.vo.QueueItem;
import eu.apenet.persistence.vo.QueuingState;
import eu.apenet.persistence.vo.SourceGuide;
import eu.archivesportaleurope.persistence.jpa.JpaUtil;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import org.apache.log4j.Logger;

/**
 *
 * @author mahbub
 */
public class ReIndexAllDocumentsManager {

    private ReIndexAllDocumentsManager() {
    }

    public static ReIndexAllDocumentsManager getInstance() {
        return ReIndexAllDocumentsManagerHolder.INSTANCE;
    }

    private static class ReIndexAllDocumentsManagerHolder {

        private static final ReIndexAllDocumentsManager INSTANCE = new ReIndexAllDocumentsManager();
    }

    private static final Logger LOGGER = Logger.getLogger(ReIndexAllDocumentsManager.class);
    private boolean reIndexInProgress = false;
    private Reindexer reindexer;
    private static long alreadyAdded;

    public int redindex(boolean testRun, List<XmlType> types) throws ProcessBusyException {
        if (this.reIndexInProgress) {
            throw new ProcessBusyException("ReIndex process still on going");
        }
        this.reIndexInProgress = true;

//        LOGGER.info("published eads: " + totalEADs+" -- "+publishedEads.size());
        this.reindexer = new Reindexer(testRun, types);
        Thread threadedReindexer = new Thread(this.reindexer);

        threadedReindexer.start();
        LOGGER.info("Reindexing thread " + threadedReindexer.getName() + " started");
        return 0;
    }

    public boolean isReIndexInProgress() {
        return reIndexInProgress;
    }

    public void setReIndexInProgress(boolean reIndexInProgress) {
        this.reIndexInProgress = reIndexInProgress;
    }

    public long getAlreadyAdded() {
        return alreadyAdded;
    }

    public void setAlreadyAdded(long alreadyAdded) {
        ReIndexAllDocumentsManager.alreadyAdded = alreadyAdded;
    }

    public void incAlreadyAdded() {
        ReIndexAllDocumentsManager.alreadyAdded = getAlreadyAdded() + 10;
    }

    public void stopReindex() {
        if (this.reIndexInProgress && this.reindexer != null) {
            LOGGER.info("Stoping reindex process");
            this.reindexer.setStopSignal(true);
        } else {
            LOGGER.info("Reindexer was not running");
        }
    }

    private class Reindexer implements Runnable {

        private boolean testRun = false;
        private boolean stopSignal = false;
        List<XmlType> types;

        public Reindexer(boolean testRun, List<XmlType> types) {
            this.testRun = testRun;
            this.types = types;
        }

        @Override
        public void run() {

            QueueItemDAO queueDao = DAOFactory.instance().getQueueItemDAO();
            ContentSearchOptions contentSearchOptions = new ContentSearchOptions();
            contentSearchOptions.setPublished(Boolean.TRUE);

            try {
                for (int i = 0; i < types.size() && !this.stopSignal; i++) {
                    XmlType xmlType = types.get(i);
                    LOGGER.info("Going to add doc type: " + xmlType.getName() + " for re-index");
                    if (xmlType.getIdentifier() == XmlType.EAD_3.getIdentifier()) {
                        contentSearchOptions.setContentClass(xmlType.getClazz());
                        Ead3DAO ead3DAO = DAOFactory.instance().getEad3DAO();
                        this.addEads(queueDao, ead3DAO, contentSearchOptions);
                    } else if (xmlType.getIdentifier() == XmlType.EAC_CPF.getIdentifier()) {
                    } else {
                        contentSearchOptions.setContentClass(xmlType.getClazz());
                        EadDAO eadDAO = DAOFactory.instance().getEadDAO();
                        this.addEads(queueDao, eadDAO, contentSearchOptions);
                    }
                }
                reIndexInProgress = false;
                alreadyAdded = 0;
            } catch (Exception ex) {
                reIndexInProgress = false;
                LOGGER.debug("Unknown exception! " + ex.getMessage());
            }
        }

        public void setStopSignal(boolean stopSignal) {
            this.stopSignal = stopSignal;
        }

        private void addEads(QueueItemDAO queueDao, GenericDAO eadDAO, ContentSearchOptions contentSearchOptions) {
            int dataSize = -1;
            if (this.testRun) {
                dataSize = 1;
            }
            contentSearchOptions.setPageSize(dataSize);
            JpaUtil.beginDatabaseTransaction();
            List publishedContents = null;
            if (eadDAO instanceof EadDAO) {
//                contentSearchOptions.setContentClass(FindingAid.class);
                publishedContents = ((EadDAO) eadDAO).getEads(contentSearchOptions);
//
//                contentSearchOptions.setContentClass(HoldingsGuide.class);
//                publishedContents.addAll(((EadDAO) eadDAO).getEads(contentSearchOptions));
//
//                contentSearchOptions.setContentClass(SourceGuide.class);
//                publishedContents.addAll(((EadDAO) eadDAO).getEads(contentSearchOptions));

            } else if (eadDAO instanceof Ead3DAO) {
                publishedContents = ((Ead3DAO) eadDAO).getEad3s(contentSearchOptions);
            }

            if (publishedContents == null) {
                LOGGER.error("No reindexable content found for: " + eadDAO.getClass().getName());
                return;
            }
            LOGGER.info("Total " + eadDAO.getClass().getName() + ": " + publishedContents.size());
            int i = 0;
//            for (Object obj : publishedContents) {
            for (int j = 0; j < publishedContents.size() && !stopSignal; j++) {
                Object obj = publishedContents.get(i);
                AbstractContent ead = (AbstractContent) obj;
                try {
                    i++;
                    LOGGER.debug(ead.getClass().getCanonicalName() + " Id: " + ead.getIdentifier() + " added for reindex");
                    QueueItem queueItem = this.genQueueItem(ead, QueueAction.REPUBLISH, new Properties(), 500);
                    ead.setQueuing(QueuingState.READY);
                    eadDAO.updateSimple(ead);
                    queueDao.updateSimple(queueItem);
                    if (i % 10 == 0) {
                        JpaUtil.commitDatabaseTransaction();
                        Thread.sleep(5000);
                        JpaUtil.beginDatabaseTransaction();
                        incAlreadyAdded();
                        LOGGER.info("Number of documents added in the queue for re-indexing :  " + getAlreadyAdded());
                    }
                } catch (IOException ex) {
                    LOGGER.error("Queue exception: " + ex.getMessage());
                } catch (InterruptedException ex) {
                }
            }
            LOGGER.info("Total ead reindexed: " + i);
            JpaUtil.commitDatabaseTransaction();
        }

        private QueueItem genQueueItem(AbstractContent ead, QueueAction queueAction, Properties preferences, int basePriority)
                throws IOException {
            QueueItem queueItem = ead.getQueueItem();
            int priority = basePriority;
            if (queueItem == null) {
                queueItem = new QueueItem();
                if (ead instanceof FindingAid) {
                    queueItem.setFindingAid((FindingAid) ead);
                } else if (ead instanceof HoldingsGuide) {
                    queueItem.setHoldingsGuide((HoldingsGuide) ead);
                    priority += 100;
                } else if (ead instanceof SourceGuide) {
                    queueItem.setSourceGuide((SourceGuide) ead);
                    priority += 50;
                } else if (ead instanceof Ead3) {
                    queueItem.setEad3((Ead3) ead);
                }
                queueItem.setAiId(ead.getAiId());
            } else if (ead instanceof HoldingsGuide) {
                priority += 100;
            } else if (ead instanceof SourceGuide) {
                priority += 50;
            }
            queueItem.setQueueDate(new Date());
            queueItem.setAction(queueAction);
            if (preferences != null) {
                queueItem.setPreferences(PropertiesUtil.writeProperties(preferences));
            }
            if (queueAction.isDeleteAction() || queueAction.isUnpublishAction() || queueAction.isDeleteFromEuropeanaAction() || queueAction.isDeleteEseEdmAction()) {
                priority += 150;
            }
            queueItem.setPriority(priority);
            return queueItem;
        }
    }
}
