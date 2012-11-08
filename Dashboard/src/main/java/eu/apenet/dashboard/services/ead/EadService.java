package eu.apenet.dashboard.services.ead;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import eu.apenet.commons.types.XmlType;
import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.dashboard.security.SecurityContext;
import eu.apenet.persistence.dao.EadDAO;
import eu.apenet.persistence.dao.EadSearchOptions;
import eu.apenet.persistence.dao.QueueItemDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.Ead;
import eu.apenet.persistence.vo.EuropeanaState;
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
					addToQueue(ead, QueueAction.VALIDATE, null);
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
					addToQueue(ead, QueueAction.VALIDATE, null);
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
		if (ValidatedState.VALIDATED.equals(ead.getValidated()) && !ead.isPublished()) {
			if (APEnetUtilities.getDashboardConfig().isDirectIndexing()) {
				try {
					new PublishTask().execute(ead);
					processed = true;
				} catch (Exception e) {
					LOGGER.error("Unable to publish: " + ead + " : " + e.getMessage(), e);
				}
			} else { // Add the file to the indexing queue
				try {
					addToQueue(ead, QueueAction.PUBLISH, null);
					processed = true;
				} catch (Exception e) {
					LOGGER.error("Unable to put: " + ead + " in a queue: " + e.getMessage(), e);
				}
			}
		}
		return processed;
	}
	public static boolean convertToEse(Integer id, Properties preferences) {
		EadDAO eadDAO = DAOFactory.instance().getEadDAO();
		FindingAid findingAid = (FindingAid) eadDAO.findById(id, FindingAid.class);
		SecurityContext.get().checkAuthorized(findingAid);
		boolean processed = false;
		if (ValidatedState.VALIDATED.equals(findingAid.getValidated()) && EuropeanaState.NOT_CONVERTED.equals(findingAid.getEuropeana())) {
			if (APEnetUtilities.getDashboardConfig().isDirectIndexing()) {
				try {
					new ConvertToEseTask().execute(findingAid, preferences);
					processed = true;
				} catch (Exception e) {
					LOGGER.error("Unable to convert to ESE: " + findingAid + " : " + e.getMessage(), e);
				}
			} else { // Add the file to the indexing queue
				try {
					addToQueue(findingAid, QueueAction.CONVERT_TO_ESE_EDM,preferences);
					processed = true;
				} catch (Exception e) {
					LOGGER.error("Unable to put: " + findingAid + " in a queue: " + e.getMessage(), e);
				}
			}
		}
		return processed;
	}
	public static boolean convertValidatePublish(XmlType xmlType, Integer id) throws IOException {
		EadDAO eadDAO = DAOFactory.instance().getEadDAO();
		Ead ead = eadDAO.findById(id, xmlType.getClazz());
		SecurityContext.get().checkAuthorized(ead);
		if (!ead.isPublished()) {
			addToQueue(ead, QueueAction.CONVERT_VALIDATE_PUBLISH, null);
		}
		return true;
	}

	public static boolean unpublish(XmlType xmlType, Integer id) {
		EadDAO eadDAO = DAOFactory.instance().getEadDAO();
		Ead ead = eadDAO.findById(id, xmlType.getClazz());
		SecurityContext.get().checkAuthorized(ead);
		boolean processed = false;
		if (ead.isPublished()) {
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
					addToQueue(ead, QueueAction.UNPUBLISH, null);
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
				addToQueue(ead, QueueAction.DELETE, null);
				processed = true;
			} catch (Exception e) {
				LOGGER.error("Unable to put: " + ead + " in a queue: " + e.getMessage(), e);
			}
		}
		return processed;
	}

	public static boolean processQueueItem(QueueItem queueItem) throws IOException {
		boolean processed = false;
		Ead ead = queueItem.getEad();
		XmlType xmlType = XmlType.getEadType(ead);
		LOGGER.info("Process queue item: " + queueItem.getId() + " " + queueItem.getAction() + " " + ead.getEadid() + "(" + xmlType.getName() + ")");
		QueueItemDAO queueItemDAO = DAOFactory.instance().getQueueItemDAO();
		EadDAO eadDAO = DAOFactory.instance().getEadDAO();
		ead.setQueuing(QueuingState.BUSY);
		eadDAO.store(ead);
		Properties preferences = null;
		if (queueItem.getPreferences() != null){
			preferences = readProperties(queueItem.getPreferences());
		}
		try {
			if (queueItem.getAction().isDeleteAction()) {
				JpaUtil.beginDatabaseTransaction();
				new UnpublishTask().execute(ead,preferences);
				new DeleteTask().execute(ead,preferences);
				queueItemDAO.deleteSimple(queueItem);
				JpaUtil.commitDatabaseTransaction();
			}else if (queueItem.getAction().isUnpublishAction()) {
				JpaUtil.beginDatabaseTransaction();
				new UnpublishTask().execute(ead,preferences);
				ead.setQueuing(QueuingState.NO);
				eadDAO.updateSimple(ead);
				queueItemDAO.deleteSimple(queueItem);
				JpaUtil.commitDatabaseTransaction();
			} else {
				
				if (queueItem.getAction().isConvertAction() && !ead.isConverted()) {
					new ConvertTask().execute(ead,preferences);
				}
				if (queueItem.getAction().isValidateAction() && ValidatedState.NOT_VALIDATED.equals(ead.getValidated())) {
					new ValidateTask().execute(ead,preferences);
				}
				if (queueItem.getAction().isPublishAction() && ValidatedState.VALIDATED.equals(ead.getValidated())
						&& !ead.isPublished()) {
					new PublishTask().execute(ead,preferences);
				}
				if (ead instanceof FindingAid){
					FindingAid findingAid = (FindingAid) ead;
					if (queueItem.getAction().isEseEdmAction() && ValidatedState.VALIDATED.equals(ead.getValidated())
							&& EuropeanaState.NOT_CONVERTED.equals(findingAid.getEuropeana())) {
						new ConvertToEseTask().execute(ead,preferences);
					}
				}
				ead.setQueuing(QueuingState.NO);
				eadDAO.store(ead);
				queueItemDAO.delete(queueItem);
			}
			processed = true;
			LOGGER.info("Process queue item finished");
		} catch (Exception e) {
			String err = "eadid: " + ead.getEadid() + " - id: " + ead.getId() + " - type: " + xmlType.getName();
			LOGGER.error("Error occured: " + err, e);
			queueItem.setErrors(new Date() + err + ". Error: " + e.getMessage() + "-" + e.getCause());
			ead.setQueuing(QueuingState.ERROR);
			eadDAO.store(ead);
			queueItem.setPriority(0);
			queueItemDAO.store(queueItem);

		}
		return processed;
	}


	private static void addToQueue(Ead ead, QueueAction queueAction, Properties preferences) throws IOException {
		QueueItemDAO indexqueueDao = DAOFactory.instance().getQueueItemDAO();
		EadDAO eadDAO = DAOFactory.instance().getEadDAO();
		ead.setQueuing(QueuingState.READY);
		eadDAO.store(ead);
		QueueItem queueItem = fillQueueItem(ead, queueAction, preferences);
		indexqueueDao.store(queueItem);
	}
	public static void addBatchToQueue(EadSearchOptions eadSearchOptions, QueueAction queueAction, Properties preferences) throws IOException {
		SecurityContext.get().checkAuthorized(eadSearchOptions.getArchivalInstitionId());
		QueueItemDAO indexqueueDao = DAOFactory.instance().getQueueItemDAO();
		EadDAO eadDAO = DAOFactory.instance().getEadDAO();
		eadSearchOptions.setPageSize(0);
		if (QueueAction.CONVERT.equals(queueAction)) {
			eadSearchOptions.setConverted(false);
		} else if (QueueAction.VALIDATE.equals(queueAction)) {
			eadSearchOptions.setConverted(true);
			eadSearchOptions.setValidated(ValidatedState.NOT_VALIDATED);
		} else if (QueueAction.PUBLISH.equals(queueAction)) {
			eadSearchOptions.setValidated(ValidatedState.VALIDATED);
			eadSearchOptions.setPublished(false);
		} else if (QueueAction.CONVERT_VALIDATE_PUBLISH.equals(queueAction)) {
			eadSearchOptions.setPublished(false);
		} else if (QueueAction.UNPUBLISH.equals(queueAction)) {
			eadSearchOptions.setPublished(true);
		}else if (QueueAction.CONVERT_TO_ESE_EDM.equals(queueAction)){
			eadSearchOptions.setEuropeana(EuropeanaState.NOT_CONVERTED);
			eadSearchOptions.setValidated(ValidatedState.VALIDATED);
			
		}
		JpaUtil.beginDatabaseTransaction();
		List<Ead> eads = eadDAO.getEads(eadSearchOptions);
		int size = 0;
		while ((size = eads.size()) > 0) {
			Ead ead = eads.get(size - 1);
			QueueItem queueItem = fillQueueItem(ead, queueAction, preferences);
			ead.setQueuing(QueuingState.READY);
			eadDAO.updateSimple(ead);
			eads.remove(size - 1);
			indexqueueDao.updateSimple(queueItem);
		}
		JpaUtil.commitDatabaseTransaction();		
	}
	public static void addBatchToQueue(List<Integer> ids, Integer aiId, XmlType xmlType, QueueAction queueAction, Properties preferences) throws IOException {
		EadSearchOptions eadSearchOptions = new EadSearchOptions();
		eadSearchOptions.setPageSize(0);
		eadSearchOptions.setEadClazz(xmlType.getClazz());
		eadSearchOptions.setArchivalInstitionId(aiId);
		if (ids != null && ids.size() > 0){
			eadSearchOptions.setIds(ids);
		}
		addBatchToQueue(eadSearchOptions, queueAction, preferences);

	}
	private static Properties readProperties(String string) throws IOException{
		StringReader stringReader = new StringReader(string);
		Properties properties = new Properties();
		properties.load(stringReader);
		stringReader.close();
		return properties;
	}
	private static String writeProperties(Properties properties) throws IOException{
		StringWriter stringWriter = new StringWriter();
		properties.store(stringWriter, "");
		String result = stringWriter.toString();
		stringWriter.flush();
		stringWriter.close();
		return result;
	}
	private static QueueItem fillQueueItem(Ead ead, QueueAction queueAction, Properties preferences) throws IOException{
		QueueItem queueItem = new QueueItem();
		queueItem.setQueueDate(new Date());
		queueItem.setAction(queueAction);
		if (preferences != null){
			queueItem.setPreferences(writeProperties(preferences));
		}
		int priority = 1;
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
		if (queueAction.isEseEdmAction()){
			priority += 10;
		}
		queueItem.setPriority(priority);
		return queueItem;
	}
}
