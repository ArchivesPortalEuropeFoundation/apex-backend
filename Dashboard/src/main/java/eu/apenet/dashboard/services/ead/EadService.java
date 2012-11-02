package eu.apenet.dashboard.services.ead;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import eu.apenet.commons.types.XmlType;
import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.dashboard.security.SecurityContext;
import eu.apenet.persistence.dao.EadDAO;
import eu.apenet.persistence.dao.EadSearchOptions;
import eu.apenet.persistence.dao.QueueItemDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.Ead;
import eu.apenet.persistence.vo.FindingAid;
import eu.apenet.persistence.vo.HoldingsGuide;
import eu.apenet.persistence.vo.QueueAction;
import eu.apenet.persistence.vo.QueueItem;
import eu.apenet.persistence.vo.QueuingState;
import eu.apenet.persistence.vo.SourceGuide;
import eu.apenet.persistence.vo.ValidatedState;
import eu.archivesportaleurope.persistence.jpa.JpaUtil;

public class EadService {
	protected static final Logger LOGGER = Logger.getLogger(EadService.class);

	public static boolean validate(XmlType xmlType, Integer id) {
		EadDAO eadDAO = DAOFactory.instance().getEadDAO();
		Ead ead = eadDAO.findById(id, xmlType.getClazz());
		SecurityContext.get().checkAuthorized(ead);
		boolean processed = false;
		if (ValidatedState.NOT_VALIDATED.equals(ead.getValidated())) {
			if (APEnetUtilities.getDashboardConfig().isDirectIndexing()) {
				try {
					new ValidateTask().execute(ead);
					processed = true;
				} catch (Exception e) {
					LOGGER.error("Unable to validate: " + ead + " : " + e.getMessage(), e);
				}
			} else { // Add the file to the indexing queue
				try {
					addToQueue(ead, QueueAction.VALIDATE);
					processed = true;
				} catch (Exception e) {
					LOGGER.error("Unable to put: " + ead + " in a queue: " + e.getMessage(), e);
				}
			}
		}
		return processed;

	}

	public static boolean convert(XmlType xmlType, Integer id) {
		EadDAO eadDAO = DAOFactory.instance().getEadDAO();
		Ead ead = eadDAO.findById(id, xmlType.getClazz());
		SecurityContext.get().checkAuthorized(ead);
		boolean processed = false;
		if (!ead.isConverted()) {
			if (APEnetUtilities.getDashboardConfig().isDirectIndexing()) {
				try {
					new ConvertTask().execute(ead);
					processed = true;
				} catch (Exception e) {
					LOGGER.error("Unable to validate: " + ead + " : " + e.getMessage(), e);
				}
			} else { // Add the file to the indexing queue
				try {
					addToQueue(ead, QueueAction.VALIDATE);
					processed = true;
				} catch (Exception e) {
					LOGGER.error("Unable to put: " + ead + " in a queue: " + e.getMessage(), e);
				}
			}
		}
		return processed;
	}

	public static boolean publish(XmlType xmlType, Integer id) {
		EadDAO eadDAO = DAOFactory.instance().getEadDAO();
		Ead ead = eadDAO.findById(id, xmlType.getClazz());
		SecurityContext.get().checkAuthorized(ead);
		boolean processed = false;
		if (ValidatedState.VALIDATED.equals(ead.getValidated()) && !ead.isSearchable()) {
			if (APEnetUtilities.getDashboardConfig().isDirectIndexing()) {
				try {
					new PublishTask().execute(ead);
					processed = true;
				} catch (Exception e) {
					LOGGER.error("Unable to publish: " + ead + " : " + e.getMessage(), e);
				}
			} else { // Add the file to the indexing queue
				try {
					addToQueue(ead, QueueAction.PUBLISH);
					processed = true;
				} catch (Exception e) {
					LOGGER.error("Unable to put: " + ead + " in a queue: " + e.getMessage(), e);
				}
			}
		}
		return processed;
	}

	public static boolean convertValidatePublish(XmlType xmlType, Integer id) {
		EadDAO eadDAO = DAOFactory.instance().getEadDAO();
		Ead ead = eadDAO.findById(id, xmlType.getClazz());
		SecurityContext.get().checkAuthorized(ead);
		if (!ead.isSearchable()) {
			addToQueue(ead, QueueAction.CONVERT_VALIDATE_PUBLISH);
		}
		return true;
	}

	public static boolean unpublish(XmlType xmlType, Integer id) {
		EadDAO eadDAO = DAOFactory.instance().getEadDAO();
		Ead ead = eadDAO.findById(id, xmlType.getClazz());
		SecurityContext.get().checkAuthorized(ead);
		boolean processed = false;
		if (ead.isSearchable()) {
			if (APEnetUtilities.getDashboardConfig().isDirectIndexing()) {
				try {
					JpaUtil.beginDatabaseTransaction();
					new UnpublishTask().execute(ead);
					JpaUtil.commitDatabaseTransaction();
					processed = true;
				} catch (Exception e) {
					JpaUtil.rollbackDatabaseTransaction();
					LOGGER.error("Unable to unpublish: " + ead + " : " + e.getMessage(), e);
				}
			} else { // Add the file to the indexing queue
				try {
					addToQueue(ead, QueueAction.UNPUBLISH);
					processed = true;
				} catch (Exception e) {
					LOGGER.error("Unable to put: " + ead + " in a queue: " + e.getMessage(), e);
				}
			}

		}
		return processed;
	}

	public static boolean delete(XmlType xmlType, Integer id) {
		EadDAO eadDAO = DAOFactory.instance().getEadDAO();
		Ead ead = eadDAO.findById(id, xmlType.getClazz());
		SecurityContext.get().checkAuthorized(ead);
		boolean processed = false;
		if (APEnetUtilities.getDashboardConfig().isDirectIndexing()) {
			try {
				JpaUtil.beginDatabaseTransaction();
				new UnpublishTask().execute(ead);
				new DeleteTask().execute(ead);
				JpaUtil.commitDatabaseTransaction();
				processed = true;
			} catch (Exception e) {
				JpaUtil.rollbackDatabaseTransaction();
				LOGGER.error("Unable to delete: " + ead + " : " + e.getMessage(), e);
			}
		} else { // Add the file to the indexing queue
			try {
				addToQueue(ead, QueueAction.DELETE);
				processed = true;
			} catch (Exception e) {
				LOGGER.error("Unable to put: " + ead + " in a queue: " + e.getMessage(), e);
			}
		}
		return processed;
	}

	public static boolean processQueueItem(QueueItem queueItem) {
		boolean processed = false;
		Ead ead = queueItem.getEad();
		XmlType xmlType = XmlType.getEadType(ead);
		QueueItemDAO queueItemDAO = DAOFactory.instance().getQueueItemDAO();
		EadDAO eadDAO = DAOFactory.instance().getEadDAO();
		ead.setQueuing(QueuingState.BUSY);
		eadDAO.store(ead);
		try {
			if (queueItem.getAction().isDeleteAction()) {
				JpaUtil.beginDatabaseTransaction();
				new UnpublishTask().execute(ead);
				new DeleteTask().execute(ead);
				queueItemDAO.deleteSimple(queueItem);
				JpaUtil.commitDatabaseTransaction();
			} else {
				if (queueItem.getAction().isConvertAction() && !ead.isConverted()) {
					new ConvertTask().execute(ead);
				}
				if (queueItem.getAction().isValidateAction() && ValidatedState.NOT_VALIDATED.equals(ead.getValidated())) {
					new ValidateTask().execute(ead);
				}
				if (queueItem.getAction().isPublishAction() && ValidatedState.VALIDATED.equals(ead.getValidated())
						&& !ead.isSearchable()) {
					new PublishTask().execute(ead);
				}
				ead.setQueuing(QueuingState.NO);
				eadDAO.store(ead);
				queueItemDAO.delete(queueItem);
			}


			processed = true;
		} catch (Exception e) {
			String err = "eadid: " + ead.getEadid() + " - id: " + ead.getId() + " - type: " + xmlType.getName();
			LOGGER.error("Error indexing: " + err, e);
			queueItem.setErrors(new Date() + err + ". Error: " + e.getMessage() + "-" + e.getCause());
			ead.setQueuing(QueuingState.ERROR);
			eadDAO.store(ead);
			queueItem.setPriority(0);
			queueItemDAO.store(queueItem);

		}
		return processed;
	}

	private static void addToQueue(Ead ead, QueueAction queueAction) {
		QueueItemDAO indexqueueDao = DAOFactory.instance().getQueueItemDAO();
		EadDAO eadDAO = DAOFactory.instance().getEadDAO();
		QueueItem queueItem = new QueueItem();
		queueItem.setQueueDate(new Date());
		queueItem.setAction(queueAction);
		ead.setQueuing(QueuingState.READY);
		eadDAO.store(ead);
		int priority = 0;
		if (ead instanceof FindingAid) {
			queueItem.setFindingAid((FindingAid) ead);
		} else if (ead instanceof HoldingsGuide) {
			queueItem.setHoldingsGuide((HoldingsGuide) ead);
			priority += 100;
		} else if (ead instanceof SourceGuide) {
			queueItem.setSourceGuide((SourceGuide) ead);
			priority += 50;
		}
		if (queueAction.isConvertAction() || queueAction.isValidateAction() || queueAction.isPublishAction()) {
			priority += 25;
		}
		queueItem.setPriority(priority);
		indexqueueDao.store(queueItem);
	}

	public static void addBatchToQueue(Integer aiId, XmlType xmlType, QueueAction queueAction) {
		SecurityContext.get().checkAuthorized(aiId);
		QueueItemDAO indexqueueDao = DAOFactory.instance().getQueueItemDAO();
		EadDAO eadDAO = DAOFactory.instance().getEadDAO();
		EadSearchOptions eadSearchOptions = new EadSearchOptions();
		eadSearchOptions.setPageSize(1000);
		if (QueueAction.CONVERT.equals(queueAction)) {
			eadSearchOptions.setConverted(false);
		} else if (QueueAction.VALIDATE.equals(queueAction)) {
			eadSearchOptions.setConverted(true);
			eadSearchOptions.setValidated(ValidatedState.NOT_VALIDATED);
		} else if (QueueAction.PUBLISH.equals(queueAction)) {
			eadSearchOptions.setConverted(true);
			eadSearchOptions.setValidated(ValidatedState.VALIDATED);
			eadSearchOptions.setSearchable(false);
		} else if (QueueAction.CONVERT_VALIDATE_PUBLISH.equals(queueAction)) {
			eadSearchOptions.setSearchable(false);
		} else if (QueueAction.UNPUBLISH.equals(queueAction)) {
			eadSearchOptions.setSearchable(true);
		}
		eadSearchOptions.setEadClazz(xmlType.getClazz());
		eadSearchOptions.setArchivalInstitionId(aiId);
		JpaUtil.beginDatabaseTransaction();
		List<Ead> eads = eadDAO.getEads(eadSearchOptions);
		int size = 0;
		while ((size = eads.size()) > 0) {
			Ead ead = eads.get(size - 1);
			QueueItem queueItem = new QueueItem();
			queueItem.setQueueDate(new Date());
			queueItem.setAction(queueAction);
			ead.setQueuing(QueuingState.READY);
			eadDAO.updateSimple(ead);
			int priority = 0;
			if (ead instanceof FindingAid) {
				queueItem.setFindingAid((FindingAid) ead);
			} else if (ead instanceof HoldingsGuide) {
				queueItem.setHoldingsGuide((HoldingsGuide) ead);
				priority += 100;
			} else if (ead instanceof SourceGuide) {
				queueItem.setSourceGuide((SourceGuide) ead);
				priority += 50;
			}
			if (queueAction.isConvertAction() || queueAction.isValidateAction() || queueAction.isPublishAction()) {
				priority += 25;
			}
			if (queueAction.isDeleteAction() || queueAction.isUnpublishAction()) {
				priority += 1000;
			}
			queueItem.setPriority(priority);
			eads.remove(size - 1);
			indexqueueDao.updateSimple(queueItem);
		}
		JpaUtil.commitDatabaseTransaction();

	}
}
