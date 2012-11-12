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
import eu.apenet.persistence.vo.UpFile;
import eu.apenet.persistence.vo.ValidatedState;
import eu.archivesportaleurope.persistence.jpa.JpaUtil;

public class EadService {
	protected static final Logger LOGGER = Logger.getLogger(EadService.class);

	public static void validate(XmlType xmlType, Integer id) throws Exception {
		EadDAO eadDAO = DAOFactory.instance().getEadDAO();
		Ead ead = eadDAO.findById(id, xmlType.getClazz());
		SecurityContext.get().checkAuthorized(ead);
		if (ValidateTask.valid(ead)) {
			if (APEnetUtilities.getDashboardConfig().isDirectIndexing()) {
				new ValidateTask().execute(ead);
			} else { // Add the file to the indexing queue

				addToQueue(ead, QueueAction.VALIDATE, null);
			}
		}

	}

	public static void convert(XmlType xmlType, Integer id) throws Exception {
		EadDAO eadDAO = DAOFactory.instance().getEadDAO();
		Ead ead = eadDAO.findById(id, xmlType.getClazz());
		SecurityContext.get().checkAuthorized(ead);
		if (ConvertTask.valid(ead)) {
			if (APEnetUtilities.getDashboardConfig().isDirectIndexing()) {
				new ConvertTask().execute(ead);
			} else { // Add the file to the indexing queue
				addToQueue(ead, QueueAction.CONVERT, null);
			}
		}
	}

	public static void publish(XmlType xmlType, Integer id) throws Exception {
		EadDAO eadDAO = DAOFactory.instance().getEadDAO();
		Ead ead = eadDAO.findById(id, xmlType.getClazz());
		SecurityContext.get().checkAuthorized(ead);
		if (PublishTask.valid(ead)) {
			if (APEnetUtilities.getDashboardConfig().isDirectIndexing()) {
				new PublishTask().execute(ead);
			} else { // Add the file to the indexing queue
				addToQueue(ead, QueueAction.PUBLISH, null);
			}
		}

	}

	public static void deleteEseEdm(XmlType xmlType, Integer id) throws Exception {
		EadDAO eadDAO = DAOFactory.instance().getEadDAO();
		Ead ead = eadDAO.findById(id, xmlType.getClazz());
		SecurityContext.get().checkAuthorized(ead);
		if (DeleteEseEdmTask.valid(ead)) {
			if (APEnetUtilities.getDashboardConfig().isDirectIndexing()) {
				new DeleteEseEdmTask().execute(ead);
			} else { // Add the file to the indexing queue
				addToQueue(ead, QueueAction.DELETE_ESE_EDM, null);
			}
		}

	}

	public static void convertToEseEdm(Integer id, Properties preferences) throws Exception {
		EadDAO eadDAO = DAOFactory.instance().getEadDAO();
		Ead ead = eadDAO.findById(id, FindingAid.class);
		SecurityContext.get().checkAuthorized(ead);
		if (ConvertToEseEdmTask.valid(ead)) {
			if (APEnetUtilities.getDashboardConfig().isDirectIndexing()) {
				new ConvertToEseEdmTask().execute(ead, preferences);
			} else { // Add the file to the indexing queue
				addToQueue(ead, QueueAction.CONVERT_TO_ESE_EDM, preferences);
			}
		}
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

	public static void unpublish(XmlType xmlType, Integer id) throws Exception {
		EadDAO eadDAO = DAOFactory.instance().getEadDAO();
		Ead ead = eadDAO.findById(id, xmlType.getClazz());
		SecurityContext.get().checkAuthorized(ead);
		if (UnpublishTask.valid(ead)) {
			if (APEnetUtilities.getDashboardConfig().isDirectIndexing()) {
				new UnpublishTask().execute(ead);
			} else { // Add the file to the indexing queue
				addToQueue(ead, QueueAction.UNPUBLISH, null);
			}

		}
	}

	public static void unpublishAll(XmlType xmlType, Integer id) throws Exception {
		EadDAO eadDAO = DAOFactory.instance().getEadDAO();
		Ead ead = eadDAO.findById(id, xmlType.getClazz());
		SecurityContext.get().checkAuthorized(ead);
		if (APEnetUtilities.getDashboardConfig().isDirectIndexing()) {
			new DeleteFromEuropeanaTask().execute(ead);
			new DeleteEseEdmTask().execute(ead);
			new UnpublishTask().execute(ead);
		} else { // Add the file to the indexing queue
			addToQueue(ead, QueueAction.UNPUBLISH_ALL, null);
		}

	}

	public static void delete(XmlType xmlType, Integer id) throws Exception {
		EadDAO eadDAO = DAOFactory.instance().getEadDAO();
		Ead ead = eadDAO.findById(id, xmlType.getClazz());
		SecurityContext.get().checkAuthorized(ead);
		if (APEnetUtilities.getDashboardConfig().isDirectIndexing()) {
			new DeleteFromEuropeanaTask().execute(ead);
			new DeleteEseEdmTask().execute(ead);
			new UnpublishTask().execute(ead);
			new DeleteTask().execute(ead);
		} else { // Add the file to the indexing queue
			addToQueue(ead, QueueAction.DELETE, null);
		}
	}

	public static void deleteFromEuropeana(XmlType xmlType, Integer id) throws Exception {
		EadDAO eadDAO = DAOFactory.instance().getEadDAO();
		Ead ead = eadDAO.findById(id, xmlType.getClazz());
		SecurityContext.get().checkAuthorized(ead);
		if (APEnetUtilities.getDashboardConfig().isDirectIndexing()) {
			new DeleteFromEuropeanaTask().execute(ead);
		} else { // Add the file to the indexing queue
			addToQueue(ead, QueueAction.DELETE_FROM_EUROPEANA, null);
		}
	}

	public static void deliverToEuropeana(XmlType xmlType, Integer id) throws Exception {
		EadDAO eadDAO = DAOFactory.instance().getEadDAO();
		Ead ead = eadDAO.findById(id, xmlType.getClazz());
		SecurityContext.get().checkAuthorized(ead);
		if (APEnetUtilities.getDashboardConfig().isDirectIndexing()) {
			new DeliverToEuropeanaTask().execute(ead);
		} else { // Add the file to the indexing queue
			addToQueue(ead, QueueAction.DELIVER_TO_EUROPEANA, null);
		}
	}

	public static void overwrite(Ead oldEad, UpFile upFile) throws Exception {
		SecurityContext.get().checkAuthorized(oldEad);
		addToQueue(oldEad, QueueAction.OVERWRITE, null, upFile);
	}

	public static void create(XmlType xmlType, UpFile upFile, Integer aiId) throws Exception {
		SecurityContext.get().checkAuthorized(aiId);
		new CreateEadTask().execute(xmlType, upFile, aiId);
		DAOFactory.instance().getUpFileDAO().delete(upFile);
	}

	public static boolean processQueueItem(QueueItem queueItem) throws IOException {
		boolean processed = false;
		Ead ead = queueItem.getEad();
		XmlType xmlType = XmlType.getEadType(ead);
		LOGGER.info("Process queue item: " + queueItem.getId() + " " + queueItem.getAction() + " " + ead.getEadid()
				+ "(" + xmlType.getName() + ")");
		QueueItemDAO queueItemDAO = DAOFactory.instance().getQueueItemDAO();
		EadDAO eadDAO = DAOFactory.instance().getEadDAO();
		ead.setQueuing(QueuingState.BUSY);
		eadDAO.store(ead);
		Properties preferences = null;
		if (queueItem.getPreferences() != null) {
			preferences = readProperties(queueItem.getPreferences());
		}

		if (queueItem.getAction().isOverwriteAction() || queueItem.getAction().isDeleteAction()) {
			boolean eadDeleted = false;
			boolean upFileDeleted = false;
			UpFile upFile = queueItem.getUpFile();
			try {
				
				queueItem.setEad(null);
				queueItem.setUpFile(null);
				queueItemDAO.store(queueItem);
				
				if (queueItem.getAction().isOverwriteAction()) {
					boolean isPublished = ead.isPublished();
					Integer aiId = ead.getAiId();
					new DeleteFromEuropeanaTask().execute(ead, preferences);
					new DeleteEseEdmTask().execute(ead, preferences);
					new UnpublishTask().execute(ead, preferences);
					new DeleteTask().execute(ead, preferences);
					eadDeleted = true;
					Ead newEad = new CreateEadTask().execute(xmlType, upFile, aiId);
					if (isPublished) {
						new ConvertTask().execute(newEad);
						new ValidateTask().execute(newEad);
						new PublishTask().execute(newEad);
					}
					DAOFactory.instance().getUpFileDAO().delete(upFile);
					upFileDeleted = true;
				} else if (queueItem.getAction().isDeleteAction()) {
					new DeleteFromEuropeanaTask().execute(ead, preferences);
					new DeleteEseEdmTask().execute(ead, preferences);
					new UnpublishTask().execute(ead, preferences);
					new DeleteTask().execute(ead, preferences);
					eadDeleted = true;
				}
				queueItemDAO.delete(queueItem);
			} catch (Exception e) {
				if (!eadDeleted){
					queueItem.setEad(ead);
					ead.setQueuing(QueuingState.ERROR);
					eadDAO.store(ead);
				}
				if (!upFileDeleted && upFile != null){
					queueItem.setUpFile(upFile);
				}
				String err = "eadid: " + ead.getEadid() + " - id: " + ead.getId() + " - type: " + xmlType.getName();
				LOGGER.error("Error occured: " + err, e);
				queueItem.setErrors(new Date() + err + ". Error: " + e.getMessage() + "-" + e.getCause());
				queueItem.setPriority(0);
				queueItemDAO.store(queueItem);
			}
		} else {
			try {
				if (queueItem.getAction().isConvertAction()) {
					new ConvertTask().execute(ead, preferences);
				}
				if (queueItem.getAction().isValidateAction()) {
					new ValidateTask().execute(ead, preferences);
				}
				if (queueItem.getAction().isPublishAction()) {
					new PublishTask().execute(ead, preferences);
				}
				if (queueItem.getAction().isConvertToEseEdmAction()) {
					new ConvertToEseEdmTask().execute(ead, preferences);
				}
				if (queueItem.getAction().isUnpublishAction()) {
					new UnpublishTask().execute(ead, preferences);
				}
				if (queueItem.getAction().isDeleteFromEuropeanaAction()) {
					new DeleteFromEuropeanaTask().execute(ead, preferences);
				}
				if (queueItem.getAction().isDeleteEseEdmAction()) {
					new DeleteEseEdmTask().execute(ead, preferences);
				}

				if (queueItem.getAction().isDeliverToEuropeanaAction()) {
					new DeliverToEuropeanaTask().execute(ead, preferences);
				}
				ead.setQueuing(QueuingState.NO);
				eadDAO.store(ead);
				queueItemDAO.delete(queueItem);
			} catch (Exception e) {
				String err = "eadid: " + ead.getEadid() + " - id: " + ead.getId() + " - type: " + xmlType.getName();
				LOGGER.error("Error occured: " + err, e);
				queueItem.setErrors(new Date() + err + ". Error: " + e.getMessage() + "-" + e.getCause());
				ead.setQueuing(QueuingState.ERROR);
				eadDAO.store(ead);
				queueItem.setPriority(0);
				queueItemDAO.store(queueItem);

			}
		}
		processed = true;
		LOGGER.info("Process queue item finished");

		return processed;
	}

	private static void addToQueue(Ead ead, QueueAction queueAction, Properties preferences, UpFile upFile)
			throws IOException {
		QueueItemDAO indexqueueDao = DAOFactory.instance().getQueueItemDAO();
		EadDAO eadDAO = DAOFactory.instance().getEadDAO();
		ead.setQueuing(QueuingState.READY);
		eadDAO.store(ead);
		QueueItem queueItem = fillQueueItem(ead, queueAction, preferences);
		queueItem.setUpFile(upFile);
		indexqueueDao.store(queueItem);
	}

	private static void addToQueue(Ead ead, QueueAction queueAction, Properties preferences) throws IOException {
		QueueItemDAO indexqueueDao = DAOFactory.instance().getQueueItemDAO();
		EadDAO eadDAO = DAOFactory.instance().getEadDAO();
		ead.setQueuing(QueuingState.READY);
		eadDAO.store(ead);
		QueueItem queueItem = fillQueueItem(ead, queueAction, preferences);
		indexqueueDao.store(queueItem);
	}

	public static void addBatchToQueue(EadSearchOptions eadSearchOptions, QueueAction queueAction,
			Properties preferences) throws IOException {
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
		} else if (QueueAction.CONVERT_TO_ESE_EDM.equals(queueAction)) {
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

	public static void addBatchToQueue(List<Integer> ids, Integer aiId, XmlType xmlType, QueueAction queueAction,
			Properties preferences) throws IOException {
		EadSearchOptions eadSearchOptions = new EadSearchOptions();
		eadSearchOptions.setPageSize(0);
		eadSearchOptions.setEadClazz(xmlType.getClazz());
		eadSearchOptions.setArchivalInstitionId(aiId);
		if (ids != null && ids.size() > 0) {
			eadSearchOptions.setIds(ids);
		}
		addBatchToQueue(eadSearchOptions, queueAction, preferences);

	}

	private static Properties readProperties(String string) throws IOException {
		StringReader stringReader = new StringReader(string);
		Properties properties = new Properties();
		properties.load(stringReader);
		stringReader.close();
		return properties;
	}

	private static String writeProperties(Properties properties) throws IOException {
		StringWriter stringWriter = new StringWriter();
		properties.store(stringWriter, "");
		String result = stringWriter.toString();
		stringWriter.flush();
		stringWriter.close();
		return result;
	}

	private static QueueItem fillQueueItem(Ead ead, QueueAction queueAction, Properties preferences) throws IOException {
		QueueItem queueItem = new QueueItem();
		queueItem.setQueueDate(new Date());
		queueItem.setAction(queueAction);
		if (preferences != null) {
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
		queueItem.setPriority(priority);
		return queueItem;
	}
}
