/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.apenet.dashboard.reindex;

import eu.apenet.commons.exceptions.ProcessBusyException;
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

    public int redindex(boolean testRun, String type) throws ProcessBusyException {
        if (reIndexInProgress) {
            throw new ProcessBusyException("ReIndex process still on going");
        }
        reIndexInProgress = true;

//        LOGGER.info("published eads: " + totalEADs+" -- "+publishedEads.size());
        Thread threadedReindexer = new Thread(new Reindexer(testRun, type));
        threadedReindexer.start();
        LOGGER.info("Reindexing thread " + threadedReindexer.getName() + " started");
        return 0;
    }

    private class Reindexer implements Runnable {

        boolean testRun = false;
        String type = "";

        public Reindexer(boolean testRun, String type) {
            this.testRun = testRun;
            this.type = type;
        }

        @Override
        public void run() {

            QueueItemDAO queueDao = DAOFactory.instance().getQueueItemDAO();
            if (this.type.equals("ead")) {
                EadDAO eadDAO = DAOFactory.instance().getEadDAO();
                this.addEads(queueDao, eadDAO);
            }
            if (this.type.equals("ead3")) {
                Ead3DAO ead3DAO = DAOFactory.instance().getEad3DAO();
                this.addEads(queueDao, ead3DAO);
            }
            reIndexInProgress = false;
        }

        private void addEads(QueueItemDAO queueDao, GenericDAO eadDAO) {
            ContentSearchOptions contentSearchOptions = new ContentSearchOptions();
            contentSearchOptions.setPublished(Boolean.TRUE);
            int dataSize = -1;
            if (this.testRun) {
                dataSize = 1;
            }
            contentSearchOptions.setPageSize(dataSize);
            JpaUtil.beginDatabaseTransaction();
            List publishedContents = null;
            if (eadDAO instanceof EadDAO) {
                contentSearchOptions.setContentClass(FindingAid.class);
                publishedContents = ((EadDAO) eadDAO).getEads(contentSearchOptions);

                contentSearchOptions.setContentClass(HoldingsGuide.class);
                publishedContents.addAll(((EadDAO) eadDAO).getEads(contentSearchOptions));

                contentSearchOptions.setContentClass(SourceGuide.class);
                publishedContents.addAll(((EadDAO) eadDAO).getEads(contentSearchOptions));

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
            for (int j = 0; j < publishedContents.size(); j++) {
                Object obj = publishedContents.get(i);
                AbstractContent ead = (AbstractContent) obj;
                try {
                    i++;
                    LOGGER.info(ead.getClass().getCanonicalName() + " Id: " + ead.getIdentifier() + " added for reindex");
                    QueueItem queueItem = this.genQueueItem(ead, QueueAction.REPUBLISH, new Properties(), 500);
                    ead.setQueuing(QueuingState.READY);
                    eadDAO.updateSimple(ead);
                    queueDao.updateSimple(queueItem);
                    if (i % 10 == 0) {
                        JpaUtil.commitDatabaseTransaction();
                        LOGGER.info("Sleep 5 sec");
                        Thread.sleep(5000);
                        JpaUtil.beginDatabaseTransaction();
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
