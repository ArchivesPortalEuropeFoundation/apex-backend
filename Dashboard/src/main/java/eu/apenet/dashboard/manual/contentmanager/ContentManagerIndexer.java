package eu.apenet.dashboard.manual.contentmanager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrServerException;

import eu.apenet.commons.types.XmlType;
import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.dashboard.exception.DashboardAPEnetException;
import eu.apenet.dashboard.indexing.EADParser;
import eu.apenet.dashboard.security.SecurityContext;
import eu.apenet.dashboard.utils.ChangeControl;
import eu.apenet.dashboard.utils.ContentUtils;
import eu.apenet.persistence.dao.EadContentDAO;
import eu.apenet.persistence.dao.EadDAO;
import eu.apenet.persistence.dao.EseDAO;
import eu.apenet.persistence.dao.EseStateDAO;
import eu.apenet.persistence.dao.QueueItemDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.hibernate.HibernateUtil;
import eu.apenet.persistence.vo.Ead;
import eu.apenet.persistence.vo.EadContent;
import eu.apenet.persistence.vo.Ese;
import eu.apenet.persistence.vo.EseState;
import eu.apenet.persistence.vo.FileState;
import eu.apenet.persistence.vo.FindingAid;
import eu.apenet.persistence.vo.HoldingsGuide;
import eu.apenet.persistence.vo.QueueItem;
import eu.apenet.persistence.vo.SourceGuide;

/**
 * This class is used for store all the actions which will be used in
 * ContentManager, the only different is these actions are related to indexing
 * process. It's used (extended) into ContentManager class.
 * 
 */
public abstract class ContentManagerIndexer {
	private static final Logger log = Logger.getLogger(ContentManagerIndexer.class);

	// Build the indexing queue accordingly with the algorithm required
	private static void queueFile(Ead ead, FileState previousState) throws Exception {
		insertQueueFile(ead, 0, previousState);
	}

	// Insert in the indexing queue
	private static void insertQueueFile(Ead ead, int position, FileState state) {
		QueueItemDAO indexqueueDao = DAOFactory.instance().getQueueItemDAO();

		QueueItem indexqueue = new QueueItem();
//		indexqueue.setPosition(position);
		indexqueue.setQueueDate(new Date());
//		indexqueue.setFileState(state);

		QueueItem indexQueue = ead.getQueueItem();
		if (indexQueue == null)
			indexqueueDao.store(setCorrectIndexQueue(ead, indexqueue));
	}

	private static QueueItem setCorrectIndexQueue(Ead ead, QueueItem indexQueue) {
		if (ead instanceof FindingAid)
			indexQueue.setFindingAid((FindingAid) ead);
		else if (ead instanceof HoldingsGuide)
			indexQueue.setHoldingsGuide((HoldingsGuide) ead);
		else if (ead instanceof SourceGuide)
			indexQueue.setSourceGuide((SourceGuide) ead);
		return indexQueue;
	}

	public static boolean indexProcess(XmlType xmlType, int id) throws Exception {
		boolean processed = true;
		Ead ead = DAOFactory.instance().getEadDAO().findById(id, xmlType.getClazz());
		SecurityContext.get().checkAuthorized(ead);
		FileState fileState = ead.getFileState();
		processed = processed && readyToIndex(ead);
		if (processed) {
			if (APEnetUtilities.getDashboardConfig().isDirectIndexing()) {
				try {
					EADParser.parseEadAndIndex(ead);
				} catch (Exception e) {
					log.error("Unable to index: " + ead + " : " + e.getMessage(), e);
					EadDAO eadDAO = DAOFactory.instance().getEadDAO();
					ead = eadDAO.findById(id, xmlType.getClazz());
					ead.setFileState(fileState);
					eadDAO.update(ead);
					processed = false;
				}
			} else { // Add the file to the indexing queue
				try {
					queueFile(ead, fileState);
				} catch (Exception e) {
					log.error("Unable to put: " + ead + " in a queue: " + e.getMessage(), e);
					ead.setFileState(fileState);
					DAOFactory.instance().getEadDAO().store(ead);
					processed = false;
				}
			}
		}

		return processed;
	}

	private static boolean readyToIndex(Ead ead) {
		EadDAO eadDAO = DAOFactory.instance().getEadDAO();
		if (ead.getFileState().getState().equals(FileState.VALIDATED_CONVERTED)
				|| ead.getFileState().getState().equals(FileState.VALIDATED_NOT_CONVERTED)) {
			FileState fileState = DAOFactory.instance().getFileStateDAO().getFileStateByState(FileState.READY_TO_INDEX);
			ead.setFileState(fileState);
			eadDAO.update(ead);
			log.info(ead + " correctly changed to '" + FileState.READY_TO_INDEX + "'");
			return true;
		} else {
			log.info(ead + " could not changed to '" + FileState.READY_TO_INDEX + "', because it has a wrong state: "
					+ ead.getFileState().getState());
			return false;
		}
	}

	public static void indexFromQueue(Ead ead) throws Exception {
		//SecurityContext.get().checkAuthorizedToIndexFromQueue();
		if (ead.getFileState().getState().equals(FileState.READY_TO_INDEX)) {
			// Parsing and Indexing
			EADParser.parseEadAndIndex(ead);
		}
	}

	public static void reindex(XmlType xmlType, int id) throws Exception {
		Ead ead = DAOFactory.instance().getEadDAO().findById(id, xmlType.getClazz());
		SecurityContext.get().checkAuthorized(ead);
		EADParser.parseEadAndIndex(ead);
	}


	public static void deletefromIndexQueue(XmlType xmltype, int id) {
		EadDAO eadDAO = DAOFactory.instance().getEadDAO();

		Ead ead = eadDAO.findById(id, xmltype.getClazz());
		SecurityContext.get().checkAuthorized(ead);
		try {

			QueueItem indexQueue = ead.getQueueItem();
			int queueId = indexQueue.getId();
//			FileState previousState = indexQueue.getFileState();
//			ead.setFileState(previousState);
//			eadDAO.update(ead);
			QueueItemDAO indexqueueDAO = DAOFactory.instance().getQueueItemDAO();
			QueueItem iq = indexqueueDAO.findById(queueId);

			if (iq != null) {
				indexqueueDAO.delete(iq);
			}
		} catch (Exception e) {
			log.info(ead + " unable to delete from index queue");
		}
	}

	/**
	 * This method provides the functionality of only delete a file from index
	 * and not on the system.
	 * 
	 * @param id
	 * @param xmlType
	 * @param aiId
	 * @throws IOException
	 * @throws SolrServerException
	 * @throws Exception
	 */
	public static void deleteOnlyFromIndex(Integer id, XmlType xmlType, Integer aiId, boolean commits) throws Exception {
		String eadid = null;
		EadDAO eadDAO = DAOFactory.instance().getEadDAO();
		Ead ead = eadDAO.findById(id, xmlType.getClazz());
		SecurityContext.get().checkAuthorized(ead);
		FileState oldFileState;

		List<Ese> eseList = new ArrayList<Ese>();
		List<String> eseRenamed = new ArrayList<String>();
		List<String> eseHtmlRenamed = new ArrayList<String>();
		String repoPath = APEnetUtilities.getConfig().getRepoDirPath() + APEnetUtilities.FILESEPARATOR;

		try {
			// 1. Delete from database ead_content and c_level
			HibernateUtil.beginDatabaseTransaction();

			oldFileState = ead.getFileState();
			eadid = ead.getEadid();

			EadContentDAO eadContentDAO = DAOFactory.instance().getEadContentDAO();
			EadContent eadContent;
			if (xmlType.equals(XmlType.EAD_HG))
				eadContent = eadContentDAO.getEadContentByHoldingsGuideId(id);
			else if (xmlType.equals(XmlType.EAD_FA))
				eadContent = eadContentDAO.getEadContentByFindingAidId(id);
			else
				eadContent = eadContentDAO.getEadContentBySourceGuideId(id);
			eadContentDAO.deleteSimple(eadContent);

			// 2. Changes file state to 5
			FileState fileState = DAOFactory.instance().getFileStateDAO()
					.getFileStateByState(FileState.VALIDATED_NOT_CONVERTED);
			log.debug("Changing EAD (" + xmlType.getName() + ") state of the EAD with eadid " + eadid);
			ead.setFileState(fileState);
			ContentUtils.changeSearchable(ead, false);
			ead.setTotalNumberOfDaos(0l);
			ead.setTotalNumberOfUnits(0l);
			ead.setTotalNumberOfUnitsWithDao(0l);

			eadDAO.updateSimple(ead);

			if (xmlType.equals(XmlType.EAD_FA)) {
				// 3. Remove ESEs if there are some one
				EseStateDAO eseStateDao = DAOFactory.instance().getEseStateDAO();
				EseDAO eseDao = DAOFactory.instance().getEseDAO();
				eseList = eseDao.getEses(ead.getId());
				for (Ese ese : eseList) {
					log.debug("Removing ESE for FA which EADID is " + eadid);
					if (ese.getEseState().getState().equals(EseState.PUBLISHED)) {
						ese.setPath(null);
						ese.setEseState(eseStateDao.getEseStateByState(EseState.REMOVED));
						eseDao.updateSimple(ese);
					} else if (ese.getEseState().getState().equals(EseState.NOT_PUBLISHED)) {
						eseDao.deleteSimple(ese);
					} else if (ese.getEseState().getState().equals(EseState.REMOVED) && ese.getPath() != null) {
						ese.setPath(null);
						eseDao.updateSimple(ese);
					}
				}
			}
		} catch (Exception e1) {
			// There were errors during Database Transaction, it is
			// necessary to make a Database rollback
			HibernateUtil.rollbackDatabaseTransaction();
			HibernateUtil.closeDatabaseSession();
			throw new DashboardAPEnetException(
					"There were errors during Database Transaction while deleting the file eadid '" + eadid
							+ "' in the index [Database Rollback]. Error: " + e1.getMessage(), e1);
		}
		try {
			// / DELETE FROM INDEX ///
			// Remove all the entries related to this EAD from the Index
			// only if it is indexed
			// Because of Solr doesn't support transactions as DDBB it is
			// necessary to make a commit immediately
			// 4. Removes the ead from index
			log.debug("Removing the EAD (" + xmlType.getName() + ") with eadid '" + eadid + "' from the index");
			ContentUtils.deleteFromIndex(eadid, aiId);
		} catch (Exception e2) {
			// There were errors during Index Transaction and it is supposed
			// that the EAD couldn't be removed from the Index
			// It is necessary to make a Database rollback
			HibernateUtil.rollbackDatabaseTransaction();
			HibernateUtil.closeDatabaseSession();
			throw new DashboardAPEnetException(
					"There were errors during Index Transaction while deleting the file eadid '" + eadid
							+ "' in the index [Database Rollback]. Error: " + e2.getMessage(), e2);
		}
		try {
			if (xmlType.equals(XmlType.EAD_FA)) {
				// / DELETE FROM FILE SYSTEM ///
				// Delete ESE files from file system.
				// Rename all ESE physically from the repository to
				// _remove...
				for (Ese ese : eseList) {
					// Rename ESE file
					log.debug("Renaming the ESE file " + ese.getPath());
					eseRenamed.add(ContentUtils.renameFileToRemove(repoPath + ese.getPath()));

					// Rename HTML files related to ESE file
					log.debug("Renaming the HTML ESE file " + ese.getPathHtml());
					eseHtmlRenamed.add(ContentUtils.renameFileToRemove(repoPath + ese.getPathHtml()));

					// Rename HTML folder related to ESE file
					log.debug("Renaming the ESE-HTML folder " + ese.getPathHtml() + "dir");
					File htmlDir = new File(repoPath + ese.getPathHtml() + "dir");
					if (htmlDir.exists())
						ContentUtils.renameFileToRemove(repoPath + ese.getPathHtml() + "dir");
				}
			}
		} catch (Exception e3) {
			// There were errors during File system Transaction
			// It is necessary to make a Database rollback
			HibernateUtil.rollbackDatabaseTransaction();
			HibernateUtil.closeDatabaseSession();

			// It is necessary to make a Index rollback if the file was
			// indexed
			if (Arrays.asList(FileState.INDEXED_FILE_STATES).contains(oldFileState.getState())) {
				try {
					ContentUtils.indexRollback(XmlType.EAD_FA, ead.getId());
				} catch (Exception ex) {
					log.error("FATAL ERROR. Error during Index Rollback [Re-indexing process because of the rollback]. The file affected is "
							+ eadid + ". Error:" + ex.getMessage());
				}

				// Due to the re-indexing of this HG, the current state is
				// "Indexed_Not Converted to ESE/EDM". It is necessary to
				// restore the original state if the original state is
				// different from "Indexed_Not Converted to ESE/EDM"
				if (!oldFileState.getState().equals(FileState.INDEXED)) {
					try {
						ead.setFileState(oldFileState);
						ContentUtils.changeSearchable(ead, true);
						DAOFactory.instance().getEadDAO().update(ead);
					} catch (Exception ex) {
						log.error("Error restoring the original state of the Finding Aid. Check Database");
					}
				}
			}

			// It is necessary to make a file system rollback
			// Rename the ESE file to the original name
			for (String anEseRenamed : eseRenamed) {
				File eseFile = new File(anEseRenamed);
				if (eseFile.exists()) {
					try {
						ContentUtils.rollbackRenameFileFromRemove(anEseRenamed);
					} catch (Exception ex) {
						log.error("FATAL ERROR. Error during File system Rollback renaming the file " + anEseRenamed);
					}
				}
			}

			// Rename the ESE-HTML file to the original name ant its folder
			for (String anEseHtmlRenamed : eseHtmlRenamed) {
				File eseFile = new File(anEseHtmlRenamed);
				if (eseFile.exists()) {
					try {
						ContentUtils.rollbackRenameFileFromRemove(anEseHtmlRenamed);
					} catch (Exception ex) {
						log.error("FATAL ERROR. Error during File system Rollback renaming the file "
								+ anEseHtmlRenamed);
					}
				}

				File eseHtmlDir = new File(anEseHtmlRenamed + "dir");
				if (eseHtmlDir.exists()) {
					try {
						ContentUtils.rollbackRenameFileFromRemove(anEseHtmlRenamed + "dir");
					} catch (Exception ex) {
						log.error("FATAL ERROR. Error during File system Rollback renaming the file "
								+ anEseHtmlRenamed + "dir");
					}
				}
			}
			throw new DashboardAPEnetException(
					"There were errors during File system Transaction while overwriting the file "
							+ eadid
							+ " in the repository [Database Rollback, Index Rollback and File system Rollback]. Error: "
							+ e3.getMessage(),e3);
		}

		try {
			if (commits)
				HibernateUtil.commitDatabaseTransaction();

			if (xmlType.equals(XmlType.EAD_FA)) {
				for (Ese ese : eseList)
					ChangeControl.logOperation((FindingAid) ead, ese, "Remove ese/edm");

				// Final commit in the File system
				// Remove all _remove ESE physically from the repository
				for (String anEseRenamed : eseRenamed) {
					File eseFile = new File(anEseRenamed);
					if (eseFile.exists())
						FileUtils.forceDelete(eseFile);
				}

				for (String anEseHtmlRenamed : eseHtmlRenamed) {
					// Remove _remove HTML files related to ESE files
					// removed and subfolders ESE-HTML
					File eseFile = new File(anEseHtmlRenamed);
					if (eseFile.exists())
						FileUtils.forceDelete(eseFile);

					File eseHtmlDir = new File(anEseHtmlRenamed + "dir");
					if (eseHtmlDir.exists())
						FileUtils.forceDelete(eseHtmlDir);
				}
			}
			log.info("The "
					+ xmlType.getName()
					+ " with eadid: "
					+ eadid
					+ "has been removed from index, changed its state in database and deleted its eses files correctly.");

			if (commits)
				HibernateUtil.closeDatabaseSession();
		} catch (Exception e4) {
			// There were errors during Database, Index commit
			HibernateUtil.closeDatabaseSession();
			throw new DashboardAPEnetException(
					"FATAL ERROR. Error during Database or Index commits. Please, check inconsistencies in Database and Index for "
							+ xmlType.getName() + " which eadid is: " + eadid, e4);
		}
	}
}
