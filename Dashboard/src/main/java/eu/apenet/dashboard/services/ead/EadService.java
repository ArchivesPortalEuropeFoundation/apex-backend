package eu.apenet.dashboard.services.ead;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import com.ctc.wstx.exc.WstxParsingException;
import eu.apenet.dashboard.manual.ExistingFilesChecker;
import eu.apenet.persistence.vo.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import eu.apenet.commons.exceptions.APEnetRuntimeException;
import eu.apenet.commons.types.XmlType;
import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.dashboard.security.SecurityContext;
import eu.apenet.dashboard.services.ead.xml.XmlEadParser;
import eu.apenet.dashboard.utils.ContentUtils;
import eu.apenet.persistence.dao.EadDAO;
import eu.apenet.persistence.dao.EadSearchOptions;
import eu.apenet.persistence.dao.EseDAO;
import eu.apenet.persistence.dao.EseStateDAO;
import eu.apenet.persistence.dao.QueueItemDAO;
import eu.apenet.persistence.dao.UpFileDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.archivesportaleurope.persistence.jpa.JpaUtil;

public class EadService {

	protected static final Logger LOGGER = Logger.getLogger(EadService.class);
	private static final long NOT_USED_TIME = 60*60*24*7;
	
	public static boolean isHarvestingStarted() {
		return DAOFactory.instance().getResumptionTokenDAO().containsValidResumptionTokens(new Date());
	}

	public static void createPreviewHTML(XmlType xmlType, Integer id) {
		EadDAO eadDAO = DAOFactory.instance().getEadDAO();
		Ead ead = eadDAO.findById(id, xmlType.getClazz());
		SecurityContext.get().checkAuthorized(ead);
		if (ead.getEadContent() == null) {
			try {
				XmlEadParser.parseEad(ead);
			} catch (Exception e) {
				throw new APEnetRuntimeException(e);
			}
		}
	}

	public static boolean hasEdmPublished(Integer eadId) {

		boolean result;

		// At the moment, only FA can have ESE files converted. Please, change
		// this behavior if another kind of EAD can have
		// ESE files converted
		EseDAO eseDao = DAOFactory.instance().getEseDAO();
		EseStateDAO eseStateDao = DAOFactory.instance().getEseStateDAO();
		EseState eseState = eseStateDao.getEseStateByState(EseState.PUBLISHED);
		List<Ese> eseList = eseDao.getEsesByFindingAidAndState(eadId, eseState);
		if (eseList == null || eseList.isEmpty()) {
			result = false;
		} else {
			result = true;
		}

		eseDao = null;
		eseStateDao = null;
		eseState = null;
		return result;
	}

	public static File download(Integer id, XmlType xmlType) {
		Ead ead = DAOFactory.instance().getEadDAO().findById(id, xmlType.getClazz());
		SecurityContext.get().checkAuthorized(ead);
		String path = APEnetUtilities.getConfig().getRepoDirPath() + ead.getPathApenetead();
		try {
			File file = new File(path);
			if (file.exists())
				return file;
		} catch (Exception e) {
			LOGGER.error("Download function error, trying to open the file '" + path + "'", e);
		}
		return null;
	}

	public static void validate(XmlType xmlType, Integer id) throws Exception {
		EadDAO eadDAO = DAOFactory.instance().getEadDAO();
		Ead ead = eadDAO.findById(id, xmlType.getClazz());
		SecurityContext.get().checkAuthorized(ead);
		if (ValidateTask.valid(ead)) {
			addToQueue(ead, QueueAction.VALIDATE, null);
		}

	}

	public static void convert(XmlType xmlType, Integer id, Properties properties) throws Exception {
		EadDAO eadDAO = DAOFactory.instance().getEadDAO();
		Ead ead = eadDAO.findById(id, xmlType.getClazz());
		SecurityContext.get().checkAuthorized(ead);
		if (ConvertTask.valid(ead)) {
			addToQueue(ead, QueueAction.CONVERT, properties);
		}
	}

	public static void publish(XmlType xmlType, Integer id) throws Exception {
		EadDAO eadDAO = DAOFactory.instance().getEadDAO();
		Ead ead = eadDAO.findById(id, xmlType.getClazz());
		SecurityContext.get().checkAuthorized(ead);
		if (PublishTask.valid(ead)) {
			addToQueue(ead, QueueAction.PUBLISH, null);
		}

	}

	public static void deleteEseEdm(XmlType xmlType, Integer id) throws Exception {
		EadDAO eadDAO = DAOFactory.instance().getEadDAO();
		Ead ead = eadDAO.findById(id, xmlType.getClazz());
		SecurityContext.get().checkAuthorized(ead);
		if (DeleteEseEdmTask.valid(ead)) {
			addToQueue(ead, QueueAction.DELETE_ESE_EDM, null);
		}

	}

	public static void convertToEseEdm(Integer id, Properties preferences) throws Exception {
		EadDAO eadDAO = DAOFactory.instance().getEadDAO();
		Ead ead = eadDAO.findById(id, FindingAid.class);
		SecurityContext.get().checkAuthorized(ead);
		if (ConvertToEseEdmTask.valid(ead)) {
			addToQueue(ead, QueueAction.CONVERT_TO_ESE_EDM, preferences);
		}
	}

	public static boolean convertValidate(XmlType xmlType, Integer id, Properties properties) throws IOException {
		EadDAO eadDAO = DAOFactory.instance().getEadDAO();
		Ead ead = eadDAO.findById(id, xmlType.getClazz());
		SecurityContext.get().checkAuthorized(ead);
        addToQueue(ead, QueueAction.CONVERT_VALIDATE, properties);
		return true;
	}

    public static boolean convertValidatePublish(XmlType xmlType, Integer id, Properties properties) throws IOException {
        EadDAO eadDAO = DAOFactory.instance().getEadDAO();
        Ead ead = eadDAO.findById(id, xmlType.getClazz());
        SecurityContext.get().checkAuthorized(ead);
        if (!ead.isPublished()) {
            addToQueue(ead, QueueAction.CONVERT_VALIDATE_PUBLISH, properties);
        }
        return true;
    }

	public static void unpublish(XmlType xmlType, Integer id) throws Exception {
		EadDAO eadDAO = DAOFactory.instance().getEadDAO();
		Ead ead = eadDAO.findById(id, xmlType.getClazz());
		SecurityContext.get().checkAuthorized(ead);
		if (UnpublishTask.valid(ead)) {
			addToQueue(ead, QueueAction.UNPUBLISH, null);

		}
	}

	public static void delete(XmlType xmlType, Integer id) throws Exception {
		EadDAO eadDAO = DAOFactory.instance().getEadDAO();
		Ead ead = eadDAO.findById(id, xmlType.getClazz());
		SecurityContext.get().checkAuthorized(ead);
		addToQueue(ead, QueueAction.DELETE, null);
	}

	public static void deleteFromEuropeana(XmlType xmlType, Integer id) throws Exception {
		EadDAO eadDAO = DAOFactory.instance().getEadDAO();
		Ead ead = eadDAO.findById(id, xmlType.getClazz());
		SecurityContext.get().checkAuthorized(ead);
		if (DeleteFromEuropeanaTask.valid(ead)) {
			addToQueue(ead, QueueAction.DELETE_FROM_EUROPEANA, null);
		}

	}

	public static void deliverToEuropeana(XmlType xmlType, Integer id) throws Exception {
		EadDAO eadDAO = DAOFactory.instance().getEadDAO();
		Ead ead = eadDAO.findById(id, xmlType.getClazz());
		SecurityContext.get().checkAuthorized(ead);
		if (DeliverToEuropeanaTask.valid(ead)) {
			addToQueue(ead, QueueAction.DELIVER_TO_EUROPEANA, null);
		}
	}

	public static void makeStatic(XmlType xmlType, Integer id) throws Exception {
		EadDAO eadDAO = DAOFactory.instance().getEadDAO();
		Ead ead = eadDAO.findById(id, xmlType.getClazz());
		SecurityContext.get().checkAuthorized(ead);
		if (ChangeStaticTask.valid(ead)) {
			addToQueue(ead, QueueAction.CHANGE_TO_STATIC, null);
		}
	}

	public static void makeDynamic(XmlType xmlType, Integer id) throws Exception {
		EadDAO eadDAO = DAOFactory.instance().getEadDAO();
		Ead ead = eadDAO.findById(id, xmlType.getClazz());
		SecurityContext.get().checkAuthorized(ead);
		if (ChangeDynamicTask.valid(ead)) {
			addToQueue(ead, QueueAction.CHANGE_TO_DYNAMIC, null);
		}
	}

	public static void deleteFromQueue(XmlType xmlType, Integer id) throws Exception {
		EadDAO eadDAO = DAOFactory.instance().getEadDAO();
		JpaUtil.beginDatabaseTransaction();
		Ead ead = eadDAO.findById(id, xmlType.getClazz());
		SecurityContext.get().checkAuthorized(ead);
		if (QueuingState.ERROR.equals(ead.getQueuing()) || QueuingState.READY.equals(ead.getQueuing())) {
			QueueItem queueItem = ead.getQueueItem();
			ead.setQueuing(QueuingState.NO);
			eadDAO.updateSimple(ead);
			deleteFromQueueInternal(queueItem);
		}
		JpaUtil.commitDatabaseTransaction();
	}

	private static void deleteFromQueueInternal(QueueItem queueItem) throws IOException {
		UpFile upFile = queueItem.getUpFile();
		if (upFile != null) {
			String filename = APEnetUtilities.getDashboardConfig().getTempAndUpDirPath() + upFile.getPath() + upFile.getFilename();
			File file = new File(filename);
			File aiDir = file.getParentFile();
			ContentUtils.deleteFile(file, false);
			if (aiDir.exists() && aiDir.listFiles().length == 0) {
				ContentUtils.deleteFile(aiDir, false);
			}
		}
		DAOFactory.instance().getQueueItemDAO().deleteSimple(queueItem);
		if (upFile != null) {
			DAOFactory.instance().getUpFileDAO().deleteSimple(upFile);
		}
	}

	public static void deleteAllUnusedUploadFiles() {
		UpFileDAO upFileDAO = DAOFactory.instance().getUpFileDAO();
		List<UpFile> upFiles = upFileDAO.getAllNotAssociatedFiles();
		for (UpFile upFile : upFiles) {
			String filename = APEnetUtilities.getDashboardConfig().getTempAndUpDirPath() + upFile.getPath() + upFile.getFilename();
			try {
				
				File file = new File(filename);
				boolean shouldDeleted = false;
				if (file.exists() && file.lastModified() < (System.currentTimeMillis() - NOT_USED_TIME)){
					LOGGER.info("Delete unused file(" + upFile.getId() + ") : " + filename);
					File aiDir = file.getParentFile();
					ContentUtils.deleteFile(file, false);
					if (aiDir.exists() && aiDir.listFiles().length == 0) {
						ContentUtils.deleteFile(aiDir, false);
					}
					shouldDeleted = true;
				}else if (!file.exists()){
					LOGGER.info("Delete not existing file(" + upFile.getId() + ") : " + filename);
					shouldDeleted = true;
				}
				if (shouldDeleted){
					upFileDAO.delete(upFile);
				}
			} catch (Exception e) {
				LOGGER.error("Unable to delete unused file(" + upFile.getId() + ") : " + filename, e);
			}
		}

	}

	public static void deleteFromQueue(QueueItem queueItem) throws Exception {
		SecurityContext.get().checkAuthorizedToManageQueue();
		EadDAO eadDAO = DAOFactory.instance().getEadDAO();
		JpaUtil.beginDatabaseTransaction();
		Ead ead = queueItem.getEad();
		if (ead != null) {
			ead.setQueuing(QueuingState.NO);
			eadDAO.updateSimple(ead);
		}
		deleteFromQueueInternal(queueItem);
		JpaUtil.commitDatabaseTransaction();
	}

	public static void overwrite(Ead oldEad, UpFile upFile) throws Exception {
		SecurityContext.get().checkAuthorized(oldEad);
		addToQueue(oldEad, QueueAction.OVERWRITE, null, upFile);
	}

	public static Ead create(XmlType xmlType, UpFile upFile, Integer aiId) throws Exception {
		SecurityContext.get().checkAuthorized(aiId);
		Ead ead = new CreateEadTask().execute(xmlType, upFile, aiId);
		DAOFactory.instance().getUpFileDAO().delete(upFile);
        return ead;
	}

    public static void useProfileAction(UpFile upFile, Properties properties) throws Exception {
        //todo: SecurityContext?
        addToQueue(QueueAction.USE_PROFILE, properties, upFile);
    }

	public static QueueAction processQueueItem(QueueItem queueItem) throws Exception {
		QueueItemDAO queueItemDAO = DAOFactory.instance().getQueueItemDAO();
        EadDAO eadDAO = DAOFactory.instance().getEadDAO();
		QueueAction queueAction = queueItem.getAction();
        Properties preferences = null;
        if (queueItem.getPreferences() != null) {
            preferences = readProperties(queueItem.getPreferences());
        }

        if (!queueAction.isUseProfileAction()) {
            Ead ead = queueItem.getEad();
            XmlType xmlType = XmlType.getEadType(ead);
            LOGGER.info("Process queue item: " + queueItem.getId() + " " + queueItem.getAction() + " " + ead.getEadid()
                    + "(" + xmlType.getName() + ")");

            if (queueAction.isOverwriteAction() || queueAction.isDeleteAction()) {
                boolean eadDeleted = false;
                boolean upFileDeleted = false;
                UpFile upFile = queueItem.getUpFile();
                try {

                    queueItem.setEad(null);
                    queueItem.setUpFile(null);
                    queueItemDAO.store(queueItem);

                    if (queueAction.isOverwriteAction()) {
                        boolean isPublished = ead.isPublished();
                        Integer aiId = ead.getAiId();
                        new DeleteFromEuropeanaTask().execute(ead, preferences);
                        new DeleteEseEdmTask().execute(ead, preferences);
                        new UnpublishTask().execute(ead, preferences);
                        new DeleteTask().execute(ead, preferences);
                        eadDeleted = true;
                        Ead newEad = new CreateEadTask().execute(xmlType, upFile, aiId);
                        if (isPublished) {
                            new ValidateTask().execute(newEad);
                            new ConvertTask().execute(newEad, preferences);
                            new ValidateTask().execute(newEad);
                            new PublishTask().execute(newEad);
                        }
                        DAOFactory.instance().getUpFileDAO().delete(upFile);
                        upFileDeleted = true;
                    } else if (queueAction.isDeleteAction()) {
                        new DeleteFromEuropeanaTask().execute(ead, preferences);
                        new DeleteEseEdmTask().execute(ead, preferences);
                        new UnpublishTask().execute(ead, preferences);
                        new DeleteTask().execute(ead, preferences);
                        eadDeleted = true;
                    }
                    queueItemDAO.delete(queueItem);
                } catch (Exception e) {
                    if (!eadDeleted) {
                        queueItem.setEad(ead);
                        ead.setQueuing(QueuingState.ERROR);
                        eadDAO.store(ead);
                    }
                    if (!upFileDeleted && upFile != null) {
                        queueItem.setUpFile(upFile);
                    }
                    String err = "eadid: " + ead.getEadid() + " - id: " + ead.getId() + " - type: " + xmlType.getName();
                    LOGGER.error("Error occured: " + err, e);
                    queueItem.setErrors(new Date() + " - " + err + ". Error: " + e.getMessage() + " - " + e.getCause());
                    queueItem.setPriority(0);
                    queueItemDAO.store(queueItem);
                }
            } else {
                try {
                    if (queueAction.isValidateAction()) {
                        new ValidateTask().execute(ead, preferences);
                    }
                    if (queueAction.isConvertAction()) {
                        new ConvertTask().execute(ead, preferences);
                    }
                    if (queueAction.isValidateAction()) {
                        new ValidateTask().execute(ead, preferences);
                    }
                    if (queueAction.isPublishAction()) {
                        new PublishTask().execute(ead, preferences);
                    }
                    if (queueAction.isRePublishAction()) {
                        new UnpublishTask().execute(ead, preferences);
                        new PublishTask().execute(ead, preferences);
                    }
                    if (queueAction.isConvertToEseEdmAction()) {
                        new ConvertToEseEdmTask().execute(ead, preferences);
                    }
                    if (queueAction.isUnpublishAction()) {
                        new UnpublishTask().execute(ead, preferences);
                    }
                    if (queueAction.isDeleteFromEuropeanaAction()) {
                        new DeleteFromEuropeanaTask().execute(ead, preferences);
                    }
                    if (queueAction.isDeleteEseEdmAction()) {
                        new DeleteEseEdmTask().execute(ead, preferences);
                    }

                    if (queueAction.isDeliverToEuropeanaAction()) {
                        new DeliverToEuropeanaTask().execute(ead, preferences);
                    }
                    if (queueAction.isCreateStaticEadAction()) {
                        new ChangeStaticTask().execute(ead, preferences);
                    }

                    if (queueAction.isCreateDynamicEadAction()) {
                        new ChangeDynamicTask().execute(ead, preferences);
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
        } else { //USE_PROFILE
            UserprofileDefaultNoEadidAction userprofileDefaultNoEadidAction = UserprofileDefaultNoEadidAction.getExistingFileAction(preferences.getProperty(QueueItem.NO_EADID_ACTION));
            UserprofileDefaultUploadAction userprofileDefaultUploadAction = UserprofileDefaultUploadAction.getUploadAction(preferences.getProperty(QueueItem.UPLOAD_ACTION));
            UserprofileDefaultExistingFileAction userprofileDefaultExistingFileAction = UserprofileDefaultExistingFileAction.getExistingFileAction(preferences.getProperty(QueueItem.EXIST_ACTION));
            UserprofileDefaultDaoType userprofileDefaultDaoType = UserprofileDefaultDaoType.getDaoType(preferences.getProperty(QueueItem.DAO_TYPE));
            XmlType xmlType = XmlType.getType(Integer.parseInt(preferences.getProperty(QueueItem.XML_TYPE)));
            LOGGER.info(QueueItem.XML_TYPE);
            LOGGER.info(xmlType == null);
            LOGGER.info(xmlType.getName());


            //About EADID
            UpFile upFile = queueItem.getUpFile();
            LOGGER.info("Process queue item: " + queueItem.getId() + " " + queueItem.getAction() + ", upFile id: " + queueItem.getUpFileId() + "(" + xmlType.getName() + ")");

            String eadid = ExistingFilesChecker.extractAttributeFromEad(APEnetUtilities.getDashboardConfig().getTempAndUpDirPath() + upFile.getPath() + upFile.getFilename(), "eadheader/eadid", null, true).trim();
            if(StringUtils.isEmpty(eadid)) {
                if(userprofileDefaultNoEadidAction.isRemove()) {
                    deleteUpFile(upFile);
                } else if (userprofileDefaultNoEadidAction.isAddLater()) {
                    //todo: Do nothing actually, right? Just leave the file in the existing file checker (page before content manager)
                }
            } else {
                boolean continueTask = true;
                Ead ead;
                Ead newEad = null;
                if((ead = doesFileExist(upFile, eadid, xmlType)) != null) {
                    if(userprofileDefaultExistingFileAction.isOverwrite()) {
                        boolean eadDeleted = false;
                        boolean upFileDeleted = false;
                        try {
                            queueItem.setEad(null);
                            queueItem.setUpFile(null);
                            queueItemDAO.store(queueItem);

                            Integer aiId = ead.getAiId();
                            new DeleteFromEuropeanaTask().execute(ead, preferences);
                            new DeleteEseEdmTask().execute(ead, preferences);
                            new UnpublishTask().execute(ead, preferences);
                            new DeleteTask().execute(ead, preferences);
                            eadDeleted = true;
                            newEad = new CreateEadTask().execute(xmlType, upFile, aiId);
                            DAOFactory.instance().getUpFileDAO().delete(upFile);
                            upFileDeleted = true;
                        } catch (Exception e) {
                            if (!eadDeleted) {
                                queueItem.setEad(ead);
                                ead.setQueuing(QueuingState.ERROR);
                                eadDAO.store(ead);
                            }
                            if (!upFileDeleted && upFile != null) {
                                queueItem.setUpFile(upFile);
                            }
                            String err = "eadid: " + ead.getEadid() + " - id: " + ead.getId() + " - type: " + xmlType.getName();
                            LOGGER.error("Error occured: " + err, e);
                            queueItem.setErrors(new Date() + " - " + err + ". Error: " + e.getMessage() + " - " + e.getCause());
                            queueItem.setPriority(0);
                            queueItemDAO.store(queueItem);
                        }
                    } else if(userprofileDefaultExistingFileAction.isKeep()) {
                        deleteUpFile(upFile);
                        continueTask = false;
                    }
                } else {
                    newEad = EadService.create(xmlType, upFile, upFile.getAiId());
                }

                if(continueTask) {
                    newEad.setQueuing(QueuingState.BUSY);
                    eadDAO.store(newEad);

                    Properties conversionProperties = new Properties();
                    if(userprofileDefaultDaoType == null || userprofileDefaultDaoType.isUnspecified()) {
                        conversionProperties.put("defaultRoleType", "UNSPECIFIED");
                    } else {
                        if(userprofileDefaultDaoType.is3D()) {
                            conversionProperties.put("defaultRoleType", "3D");
                        } else if (userprofileDefaultDaoType.isImage()) {
                            conversionProperties.put("defaultRoleType", "IMAGE");
                        } else if (userprofileDefaultDaoType.isSound()) {
                            conversionProperties.put("defaultRoleType", "SOUND");
                        } else if (userprofileDefaultDaoType.isText()) {
                            conversionProperties.put("defaultRoleType", "TEXT");
                        } else if (userprofileDefaultDaoType.isVideo()) {
                            conversionProperties.put("defaultRoleType", "VIDEO");
                        }
                    }
                    conversionProperties.put("useDefaultRoleType", true); //todo: Take from the userprofile when Stefan adds it

                    try {
                        if(userprofileDefaultUploadAction.isConvert()) {
                            new ConvertTask().execute(newEad, conversionProperties);
                        } else if(userprofileDefaultUploadAction.isValidate()) {
                            new ValidateTask().execute(newEad);
                        } else if(userprofileDefaultUploadAction.isConvertValidatePublish() || userprofileDefaultUploadAction.isConvertValidatePublishEuropeana()) {
                            new ValidateTask().execute(newEad);
                            new ConvertTask().execute(newEad, conversionProperties);
                            new ValidateTask().execute(newEad);
                            new PublishTask().execute(newEad);
                            if(userprofileDefaultUploadAction.isConvertValidatePublishEuropeana()) {
                                new ConvertToEseEdmTask().execute(newEad); //todo: Add properties
                                new DeliverToEuropeanaTask().execute(newEad);
                            }
                        }
                        newEad.setQueuing(QueuingState.NO);
                        eadDAO.store(newEad);
                        queueItemDAO.delete(queueItem);
                    } catch (Exception e) {
                        newEad.setQueuing(QueuingState.ERROR);
                        eadDAO.store(newEad);

                        String err = "eadid: " + newEad.getEadid() + " - id: " + newEad.getId() + " - type: " + xmlType.getName();
                        LOGGER.error("Error occured: " + err, e);
                        queueItem.setErrors(new Date() + " - " + err + ". Error: " + e.getMessage() + " - " + e.getCause());
                        queueItem.setPriority(0);
                        queueItemDAO.store(queueItem);
                    }
                }
            }
        }
		LOGGER.info("Process queue item finished");

		return queueAction;
	}

    private static void deleteUpFile(UpFile upFile) throws IOException {
        JpaUtil.beginDatabaseTransaction();
        DAOFactory.instance().getUpFileDAO().deleteSimple(upFile);
        JpaUtil.commitDatabaseTransaction();
        ContentUtils.deleteFile(APEnetUtilities.getDashboardConfig().getTempAndUpDirPath() + upFile.getPath() + upFile.getFilename());
        File uploadDir = new File(APEnetUtilities.getDashboardConfig().getTempAndUpDirPath() + upFile.getPath());
        if (uploadDir.listFiles().length == 0)
            FileUtils.forceDelete(uploadDir);
    }

    private static Ead doesFileExist(UpFile upFile, String eadid, XmlType xmlType) {
        return DAOFactory.instance().getEadDAO().getEadByEadid(xmlType.getClazz(), upFile.getAiId(), eadid);
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

    private static void addToQueue(QueueAction queueAction, Properties preferences, UpFile upFile) throws IOException {
        QueueItem queueItem = fillQueueItem(upFile, queueAction, preferences);
        DAOFactory.instance().getQueueItemDAO().store(queueItem);
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
		} else if (QueueAction.DELIVER_TO_EUROPEANA.equals(queueAction)) {
			eadSearchOptions.setEuropeana(EuropeanaState.CONVERTED);

		} else if (QueueAction.DELETE_FROM_EUROPEANA.equals(queueAction)) {
			eadSearchOptions.setEuropeana(EuropeanaState.DELIVERED);
		} else if (QueueAction.DELETE_ESE_EDM.equals(queueAction)) {
			eadSearchOptions.setEuropeana(EuropeanaState.CONVERTED);
		} else if (QueueAction.CHANGE_TO_STATIC.equals(queueAction)) {
			eadSearchOptions.setDynamic(true);
		} else if (QueueAction.CHANGE_TO_DYNAMIC.equals(queueAction)) {
			eadSearchOptions.setDynamic(false);
		}
		eadSearchOptions.setQueuing(QueuingState.NO);
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

	public static void updateEverything(EadSearchOptions eadSearchOptions, QueueAction queueAction) throws IOException {
		QueueItemDAO indexqueueDao = DAOFactory.instance().getQueueItemDAO();
		EadDAO eadDAO = DAOFactory.instance().getEadDAO();
		long itemsLeft = eadDAO.countEads(eadSearchOptions);
		LOGGER.info(itemsLeft + " " + eadSearchOptions.getEadClass().getSimpleName() + " left to add to queue");
		while (itemsLeft > 0) {
			JpaUtil.beginDatabaseTransaction();
			List<Ead> eads = eadDAO.getEads(eadSearchOptions);
			int size = 0;
			while ((size = eads.size()) > 0) {
				Ead ead = eads.get(size - 1);
				QueueItem queueItem = fillQueueItem(ead, queueAction, null, 1);
				ead.setQueuing(QueuingState.READY);
				if (queueAction.isPublishAction()) {
					ead.setPublished(false);
				}
				eadDAO.updateSimple(ead);
				eads.remove(size - 1);
				indexqueueDao.updateSimple(queueItem);
			}
			JpaUtil.commitDatabaseTransaction();
			itemsLeft = eadDAO.countEads(eadSearchOptions);
			LOGGER.info(itemsLeft + " " + eadSearchOptions.getEadClass().getSimpleName() + " left to add to queue");

		}

	}

	public static void deleteBatchFromQueue(List<Integer> ids, Integer aiId, XmlType xmlType) throws IOException {
		EadSearchOptions eadSearchOptions = new EadSearchOptions();
		eadSearchOptions.setPageSize(0);
		eadSearchOptions.setEadClass(xmlType.getClazz());
		eadSearchOptions.setArchivalInstitionId(aiId);
		if (ids != null && ids.size() > 0) {
			eadSearchOptions.setIds(ids);
		}
		deleteBatchFromQueue(eadSearchOptions);

	}

	public static void deleteBatchFromQueue(EadSearchOptions eadSearchOptions) throws IOException {
		SecurityContext.get().checkAuthorized(eadSearchOptions.getArchivalInstitionId());
		EadDAO eadDAO = DAOFactory.instance().getEadDAO();
		eadSearchOptions.setPageSize(0);
		List<QueuingState> queueStates = new ArrayList<QueuingState>();
		queueStates.add(QueuingState.READY);
		queueStates.add(QueuingState.ERROR);
		eadSearchOptions.setQueuing(queueStates);
		JpaUtil.beginDatabaseTransaction();
		List<Ead> eads = eadDAO.getEads(eadSearchOptions);
		int size = 0;
		while ((size = eads.size()) > 0) {
			Ead ead = eads.get(size - 1);
			QueueItem queueItem = ead.getQueueItem();
			ead.setQueuing(QueuingState.NO);
			eadDAO.updateSimple(ead);
			deleteFromQueueInternal(queueItem);
			eads.remove(size - 1);
		}
		JpaUtil.commitDatabaseTransaction();
	}

	public static void addBatchToQueue(List<Integer> ids, Integer aiId, XmlType xmlType, QueueAction queueAction,
			Properties preferences) throws IOException {
		EadSearchOptions eadSearchOptions = new EadSearchOptions();
		eadSearchOptions.setPageSize(0);
		eadSearchOptions.setEadClass(xmlType.getClazz());
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
		return fillQueueItem(ead, queueAction, preferences, 1000);
	}

	private static QueueItem fillQueueItem(Ead ead, QueueAction queueAction, Properties preferences, int basePriority)
			throws IOException {
		QueueItem queueItem = new QueueItem();
		queueItem.setQueueDate(new Date());
		queueItem.setAction(queueAction);
		if (preferences != null) {
			queueItem.setPreferences(writeProperties(preferences));
		}
		int priority = basePriority;
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
		} else if (queueAction.isDeleteAction() || queueAction.isOverwriteAction()) {
			priority += 50;
		}
		queueItem.setPriority(priority);
		return queueItem;
	}

    private static QueueItem fillQueueItem(UpFile upFile, QueueAction queueAction, Properties preferences) throws IOException {
        return fillQueueItem(upFile, queueAction, preferences, 1000);
    }

    private static QueueItem fillQueueItem(UpFile upFile, QueueAction queueAction, Properties preferences, int basePriority) throws IOException {
        QueueItem queueItem = new QueueItem();
        queueItem.setQueueDate(new Date());
        queueItem.setAction(queueAction);
        if (preferences != null) {
            queueItem.setPreferences(writeProperties(preferences));
        }

        queueItem.setUpFile(upFile);

        queueItem.setPriority(basePriority);
        return queueItem;
    }
}
